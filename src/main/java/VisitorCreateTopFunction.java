package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.mozilla.javascript.ast.ScriptNode;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.Symbol;

public class VisitorCreateTopFunction implements NodeVisitor{

    private Map<String, String> paramMap = new HashMap<String, String>();
    private Name thisName;

    private List<AstNode> createArguments(List<String> globalNames) {
        List<AstNode> arguments = new ArrayList<AstNode>();
        KeywordLiteral keywordLiteral = new KeywordLiteral();
        keywordLiteral.setType(Token.THIS);
        arguments.add(keywordLiteral);
        
        for (String globalName : globalNames) {
            StringLiteral stringLiteral = new StringLiteral();
            stringLiteral.setValue(globalName);
            stringLiteral.setQuoteCharacter('"');
            arguments.add(stringLiteral);
        }
        return arguments;
    }

    
// private List<AstNode> createArguments(List<Symbol> symbols) {
//     List<AstNode> arguments = new ArrayList<AstNode>();
//     KeywordLiteral keywordLiteral = new KeywordLiteral();
//     keywordLiteral.setType(Token.THIS);
//     arguments.add(keywordLiteral);
//     for (Symbol symbol : symbols) {
//         StringLiteral stringLiteral = new StringLiteral();
//         stringLiteral.setValue(symbol.getName());
//         stringLiteral.setQuoteCharacter('"');
//         arguments.add(stringLiteral);
//     }
//     return arguments;
// }

    private List<AstNode> createParams(List<String> globalNames) {
        List<AstNode> params = new ArrayList<AstNode>();
        Name name = new Name();
        String nameStr = "_";
        name.setIdentifier(nameStr);
        params.add(name);
        this.thisName = name;
        for (int i = 0; i < globalNames.size(); i++) {
            name = new Name();
            nameStr = "_" + i;
            name.setIdentifier(nameStr);
            params.add(name);
            this.paramMap.put(globalNames.get(i), nameStr);
        }
        return params;
    }

    // private List<AstNode> createParams(List<Symbol> symbols) {
    //     List<AstNode> params = new ArrayList<AstNode>();
    //     Name name = new Name();
    //     String nameStr = "_";
    //     name.setIdentifier(nameStr);
    //     params.add(name);
    //     this.thisName = name;
    //     for (int i = 0; i < symbols.size(); i++) {
    //         name = new Name();
    //         nameStr = "_" + i;
    //         name.setIdentifier(nameStr);
    //         params.add(name);
    //         this.paramMap.put(symbols.get(i).getName(), nameStr);
    //     }
    //     return params;
    // }

    public Map<String, String> getParamMap(){
        return this.paramMap;
    }

    public Name getThisName() {
        return this.thisName;
    }

    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == AstRoot.class) {
            AstRoot astRoot = (AstRoot)astNode;
            VisitorGetName visitorGetName = new VisitorGetName();
            astRoot.visit(visitorGetName);
            List<String> globalNames = visitorGetName.getGlobalNames();
            ExpressionStatement expressionStatement = new ExpressionStatement();
            FunctionCall functionCall = new FunctionCall();
            List<AstNode> arguments = createArguments(globalNames);
            ParenthesizedExpression parenthesizedExpression = new ParenthesizedExpression();
            FunctionNode functionNode = new FunctionNode();
            List<AstNode> params = createParams(globalNames);
            Block block = new Block();
            List<AstNode> statements = astRoot.getStatements();
            for (AstNode statement : statements) {
                block.addChild(statement);
            }
            astNode.removeChildren();
            functionNode.setParams(params);
            functionNode.setBody(block);
            parenthesizedExpression.setExpression(functionNode);
            functionCall.setTarget(parenthesizedExpression);
            functionCall.setArguments(arguments); 
            expressionStatement.setExpression(functionCall);
            astNode.addChild(expressionStatement);
        }
        return false;
    }
}
