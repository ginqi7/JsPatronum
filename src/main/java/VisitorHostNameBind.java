package main.java;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.ForInLoop;
import org.mozilla.javascript.ast.ForLoop;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.IfStatement;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.ThrowStatement;

public class VisitorHostNameBind implements NodeVisitor {
    private String hostName;

    VisitorHostNameBind(String hostName) {
        this.hostName = hostName;
    }

	private AstNode createHostNameBindNode() {
        
        // if (location.hostname != "hello") {
        //     for (key in Object.keys(window)) {
        //         window[key] = undefined;
        //     }
        //     throw "js 程序无法在该域名下运行。"
        //         }

        IfStatement ifStatement = new IfStatement();
        InfixExpression infixExpression = new InfixExpression();
        PropertyGet propertyGet1 = new PropertyGet();
        Name locationNode = new Name();
        Name hostNameNode = new Name();
        StringLiteral hostNameLiteral = new StringLiteral();
        Scope ifScope = new Scope();
        ForInLoop forInLoop = new ForInLoop();
        Name keyNode = new Name();
        FunctionCall functionCall = new FunctionCall();
        PropertyGet propertyGet2 = new PropertyGet();
        Name objectNode = new Name();
        Name keysNode = new Name();
        Name windowNode = new Name();
        Scope forScope = new Scope();
        ExpressionStatement expressionStatement = new ExpressionStatement();
        Assignment assignment = new Assignment();
        ElementGet elementGet = new ElementGet();
        Name undefinedNode = new Name();
        ThrowStatement throwStatement = new ThrowStatement();
        StringLiteral throwLiteral = new StringLiteral();

        // throw 
        throwLiteral.setQuoteCharacter('"');
        throwLiteral.setValue("The JS program cannot run on this site.");
        throwStatement.setExpression(throwLiteral);
        
        // ExpressionStatement Assignment
        windowNode.setIdentifier("window");
        keyNode.setIdentifier("key");
        undefinedNode.setIdentifier("undefined");
        elementGet.setTarget(windowNode);
        elementGet.setElement(keyNode);
        assignment.setOperator(Token.ASSIGN);
        assignment.setLeft(elementGet);
        assignment.setRight(undefinedNode);
        expressionStatement.setExpression(assignment);
        // forLoop
        forScope.addChild(expressionStatement);
        objectNode.setIdentifier("Object");
        keysNode.setIdentifier("keys");
        propertyGet2.setTarget(objectNode);
        propertyGet2.setProperty(keysNode);
        functionCall.setTarget(propertyGet2);
        functionCall.addArgument(windowNode);
        // forInLoop.setIteratedObject(functionCall);
        // forInLoop.setIterator(keyNode);
        forInLoop.setIteratedObject(windowNode);
        forInLoop.setIterator(keyNode);
        forInLoop.setBody(forScope);
        // if
        ifScope.addChild(forInLoop);
        ifScope.addChild(throwStatement);
        locationNode.setIdentifier("location");
        hostNameNode.setIdentifier("hostname");
        hostNameLiteral.setQuoteCharacter('"');
        hostNameLiteral.setValue(this.hostName);
        propertyGet1.setTarget(locationNode);
        propertyGet1.setProperty(hostNameNode);
        infixExpression.setOperator(Token.NE);
        infixExpression.setLeft(propertyGet1);
        infixExpression.setRight(hostNameLiteral);
        ifStatement.setCondition(infixExpression);
        ifStatement.setThenPart(ifScope);
        return ifStatement;
    }
    
    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == AstRoot.class) {
            astNode.addChildToBack(createHostNameBindNode());
        }
        return false;
	}
}
