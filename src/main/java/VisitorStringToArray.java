package main.java;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.UnaryExpression;

public class VisitorStringToArray implements NodeVisitor {
    private AstRoot astRoot;
    private List<String> stringList = new ArrayList<String>();
    private List<Character> charList = new ArrayList<Character>();

    private void createTopImmediateFunction() {
        ExpressionStatement expressionStatement = new ExpressionStatement();
        FunctionCall functionCall = new FunctionCall();
        ParenthesizedExpression parenthesizedExpression = new ParenthesizedExpression();
        FunctionNode functionNode = new FunctionNode();
        List<AstNode> params = new ArrayList<AstNode>();
        List<AstNode> arguments = new ArrayList<AstNode>();
        Block block = new Block();
        List<AstNode> statements = this.astRoot.getStatements();
        for (AstNode statement : statements) {
            block.addChild(statement);
        }
        Name name = new Name();
        name.setIdentifier("strArr");
        params.add(name);
        this.astRoot.removeChildren();
        functionNode.setParams(params);
        functionNode.setBody(block);
        parenthesizedExpression.setExpression(functionNode);
        functionCall.setTarget(parenthesizedExpression);
        arguments.add(this.createImmediateFunction());
        functionCall.setArguments(arguments); 
        expressionStatement.setExpression(functionCall);
        this.astRoot.addChild(expressionStatement);
    }

    private List<AstNode> createArguments() {
        List<AstNode> arguments = new ArrayList<AstNode>();
        ArrayLiteral arrayLiteral = new ArrayLiteral();
        this.charList = new ArrayList<Character>();
        
        for (String str : this.stringList) {
            for (int i = 0; i < str.length(); i++) {
                char c = str.charAt(i);
                if (!this.charList.contains(c)) {
                    this.charList.add(c);
                }
            }
        }
        for (Character c : this.charList) {
            StringLiteral stringLiteral = new StringLiteral();
            stringLiteral.setQuoteCharacter('\"');
            stringLiteral.setValue(c.toString());
            arrayLiteral.addElement(stringLiteral);
        }
        arguments.add(arrayLiteral);
        return arguments;
    }

    private void getStringList() {
		ExpressionStatement expressionStatement = (ExpressionStatement) this.astRoot.getFirstChild();
		FunctionCall functionCall = (FunctionCall) expressionStatement.getExpression();
        List<AstNode> arguments = functionCall.getArguments();
        for (AstNode argument : arguments) {
            if (argument.getClass() == StringLiteral.class) {
                StringLiteral stringLiteral = (StringLiteral)argument;
                this.stringList.add(stringLiteral.getValue());
            }
        }
    }

    private AstNode createImmediateFunction() {
        String tmpSource = "!function(n) {" +
            "return function() {" +
            "for (var t = arguments, r = \"\", u = 0, i = t.length; i > u; u++)" +
            " r += n[t[u]];" +
            "return r}} "+
            "([\"a\"])";
        AstRoot tmpRoot = new Parser(new CompilerEnvirons()).parse(tmpSource, null, 1);
        ExpressionStatement expressionStatement = (ExpressionStatement) tmpRoot.getFirstChild();
		UnaryExpression unaryExpression = (UnaryExpression) expressionStatement.getExpression();
		FunctionCall functionCall = (FunctionCall) unaryExpression.getOperand();
        functionCall.setArguments(this.createArguments());
        return functionCall;
    }

    private void changeOldArguments() {
        ExpressionStatement expressionStatement = (ExpressionStatement) this.astRoot.getFirstChild();
		FunctionCall functionCall = (FunctionCall) expressionStatement.getExpression();
        List<AstNode> arguments = functionCall.getArguments();

        for (int i = 0; i < arguments.size(); i++){
            if (arguments.get(i).getClass() == StringLiteral.class) {
                StringLiteral stringLiteral = (StringLiteral)arguments.get(i);
                FunctionCall newArgument = new FunctionCall();
                Name name = new Name();
                List<AstNode> functionArguments = new ArrayList<AstNode>();
                name.setIdentifier("strArr");
                newArgument.setTarget(name);
                String argStr = stringLiteral.getValue();
                for (int j = 0; j < argStr.length(); j++) {
                    NumberLiteral numberLiteral = new NumberLiteral();
                    numberLiteral.setValue(""+this.charList.indexOf(argStr.charAt(j)));
                    functionArguments.add(numberLiteral);
                }
                newArgument.setArguments(functionArguments);
                arguments.set(i, newArgument);
            }
        }
    }
    
    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == AstRoot.class) {
            this.astRoot = (AstRoot)astNode;
			this.getStringList();
            this.createArguments();
            this.changeOldArguments();
            this.createTopImmediateFunction();
        }
		return false;
	}
}
