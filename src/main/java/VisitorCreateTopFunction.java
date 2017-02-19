package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.mozilla.javascript.ast.ScriptNode;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.Symbol;

public class VisitorCreateTopFunction implements NodeVisitor{
    private Map<String, String> paramMap = new HashMap<String, String>();
    private List<AstNode> createArguments(List<Symbol> symbols) {
        List<AstNode> arguments = new ArrayList<AstNode>();
        for (Symbol symbol : symbols) {
            StringLiteral stringLiteral = new StringLiteral();
            stringLiteral.setValue(symbol.getName());
            stringLiteral.setQuoteCharacter('"');
            arguments.add(stringLiteral);
        }
        return arguments;
        // class org.mozilla.javascript.ast.AstRoot
        //     class org.mozilla.javascript.ast.ExpressionStatement
        //     class org.mozilla.javascript.ast.FunctionCall
        //     class org.mozilla.javascript.ast.ParenthesizedExpression
        //     class org.mozilla.javascript.ast.FunctionNode
        //     class org.mozilla.javascript.ast.Name
        //     class org.mozilla.javascript.ast.Block
        //     class org.mozilla.javascript.ast.StringLiteral
    }

    private List<AstNode> createParams(List<Symbol> symbols, Map<String, String> paramMap) {
        List<AstNode> params = new ArrayList<AstNode>();
        int index = 0;
        for (Symbol symbol : symbols) {
            Name name = new Name();
            String nameStr = "_" + index;
            name.setIdentifier(nameStr);
            params.add(name);
            paramMap.put(symbol.getName(), nameStr);
            index++;
        }
        return params;
    }

    public Map<String, String> getParamMap() {
        return this.paramMap;
    }
    
    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == AstRoot.class) {
            ScriptNode scriptNode = (ScriptNode)astNode;
            List<Symbol> symbols = scriptNode.getSymbols();
            ExpressionStatement expressionStatement = new ExpressionStatement();
            FunctionCall functionCall = new FunctionCall();
            List<AstNode> arguments = createArguments(symbols);
            ParenthesizedExpression parenthesizedExpression = new ParenthesizedExpression();
            FunctionNode functionNode = new FunctionNode();
            List<AstNode> params = createParams(symbols, paramMap);
            Block block = new Block();
            List<AstNode> statements = scriptNode.getStatements();
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
