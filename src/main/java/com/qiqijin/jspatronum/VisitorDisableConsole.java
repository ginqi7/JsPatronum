package com.qiqijin.jspatronum;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.ObjectLiteral;
import org.mozilla.javascript.ast.ObjectProperty;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.ReturnStatement;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.ThrowStatement;

public class VisitorDisableConsole implements NodeVisitor {

    private AstNode createDisableConsoleNode() {

        // Object.defineProperty(window, "console", {
        //     get: function() {
        //             throw "抱歉，为了用户安全，本网站已禁用console脚本功能";
        //             return {}
        //         }
        //     })
        
        ExpressionStatement expressionStatement = new ExpressionStatement();
        FunctionCall functionCall = new FunctionCall();
        PropertyGet propertyGet = new PropertyGet();
        Name nameLeft = new Name();
        Name nameRight = new Name();
        Name argumName = new Name();
        StringLiteral stringLiteral = new StringLiteral();
		ObjectLiteral objectLiteral = new ObjectLiteral();
        ObjectProperty objectProperty = new ObjectProperty();
        Name propertyName = new Name();
        FunctionNode functionNode = new FunctionNode();
        Block block = new Block();
        ThrowStatement throwStatement = new ThrowStatement();
        StringLiteral throwStringLiteral = new StringLiteral();
        ReturnStatement returnStatement = new ReturnStatement();
        ObjectLiteral returnObjectLiteral = new ObjectLiteral();

        // return {}
        returnStatement.setReturnValue(returnObjectLiteral);
        // throw  "something"
        throwStringLiteral.setQuoteCharacter('"');
        throwStringLiteral.setValue("Sorry, for the sake of user security, this site has disabled console scripting functionality");
        throwStatement.setExpression(throwStringLiteral);
        //get:  function(){}
        block.addStatement(throwStatement);
        block.addStatement(returnStatement);
        functionNode.setBody(block);
        propertyName.setIdentifier("get");
        objectProperty.setLeft(propertyName);
        objectProperty.setRight(functionNode);
        objectLiteral.addElement(objectProperty);
        // propertyGet
        nameLeft.setIdentifier("Object");
        nameRight.setIdentifier("defineProperty");
        propertyGet.setTarget(nameLeft);
        propertyGet.setProperty(nameRight);
        // functionCall
        functionCall.setTarget(propertyGet);
        argumName.setIdentifier("window");
        stringLiteral.setQuoteCharacter('"');
        stringLiteral.setValue("console");
        functionCall.addArgument(argumName);
        functionCall.addArgument(stringLiteral);
        functionCall.addArgument(objectLiteral);
        // expressionStatement
        expressionStatement.setExpression(functionCall);

        return expressionStatement;
    }
    
    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == AstRoot.class) {
            astNode.addChild(this.createDisableConsoleNode());
        }
        return false;
	}
}
