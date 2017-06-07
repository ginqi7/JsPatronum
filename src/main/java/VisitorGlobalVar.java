package main.java;

import java.util.List;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;

/**
 *
 * 全局变量转换为this属性
 * @author
 */
public class VisitorGlobalVar implements NodeVisitor {

    private boolean isGlobalVar(Name name) {
        Scope defineScope = name.getDefiningScope();
        if (defineScope != null && defineScope.getClass() == AstRoot.class) {
            return true;
        }
        return false;
    }

    private PropertyGet createPropertyGet(Name name) {
        PropertyGet propertyGet = new PropertyGet();
        // KeywordLiteral keywordLiteral = new KeywordLiteral();
        // keywordLiteral.setType(Token.THIS);
        // propertyGet.setTarget(keywordLiteral);
        // propertyGet.setProperty(name);
        // return propertyGet;

        Name window = new Name();
        window.setIdentifier("window");
        propertyGet.setTarget(window);
        propertyGet.setProperty(name);
        return propertyGet;

    }

    private void varDeclarToProperty(VariableInitializer variableInitializer, Name name) {
		VariableDeclaration variableDeclaration = (VariableDeclaration) variableInitializer.getParent();
	    AstNode parentNode = variableDeclaration.getParent();
        ExpressionStatement expressionStatement = new ExpressionStatement();
        Assignment assignment = new Assignment();
        PropertyGet propertyGet = this.createPropertyGet(name);
        assignment.setLeft(propertyGet);
        assignment.setRight(variableInitializer.getInitializer());
        assignment.setOperator(Token.ASSIGN);
        expressionStatement.setExpression(assignment);
        System.out.println(variableDeclaration.toSource());
        parentNode.replaceChild(variableDeclaration, expressionStatement);
    }

    private void elementGetToProperty(ElementGet elementGet, Name name) {
        PropertyGet propertyGet = this.createPropertyGet(name);
        elementGet.setTarget(propertyGet);
    }

    private void infixExpressionToProperty(InfixExpression infixExpression, Name name) {
        PropertyGet propertyGet = this.createPropertyGet(name);
        if (infixExpression.getLeft() == name) {
            infixExpression.setLeft(propertyGet);
        } else {
            infixExpression.setRight(propertyGet);
        }
    }
    
    private void globalToProperty(Name name) {
        AstNode parentNode = name.getParent();
        if (parentNode.getClass() == VariableInitializer.class) {
            this.varDeclarToProperty((VariableInitializer) parentNode, name);
        } else if (parentNode instanceof InfixExpression) {
            this.infixExpressionToProperty((InfixExpression) parentNode, name);
        } else if (parentNode.getClass() == ElementGet.class) {
            this.elementGetToProperty((ElementGet) parentNode, name);
        } else if (parentNode.getClass() == FunctionCall.class) {
            this.functionCallToProperty((FunctionCall) parentNode, name);
        } else if (parentNode.getClass() == FunctionNode.class) {
            this.functionNodeToProperty((FunctionNode) parentNode, name);
        }
	}

	private void functionNodeToProperty(FunctionNode functionNode, Name name) {
        AstNode parentNode = functionNode.getParent();
        if (parentNode.getClass() == AstRoot.class) {
            AstRoot astRoot = (AstRoot) parentNode;
            ExpressionStatement expressionStatement = new ExpressionStatement();
            Assignment assignment = new Assignment();
            PropertyGet propertyGet = new PropertyGet();
            KeywordLiteral keywordLiteral = new KeywordLiteral();
            keywordLiteral.setType(Token.THIS);
            propertyGet.setTarget(keywordLiteral);
            propertyGet.setProperty(name);
            functionNode.setFunctionName(null);
            assignment.setLeft(propertyGet);
            assignment.setRight(functionNode);
            assignment.setOperator(Token.ASSIGN);
            expressionStatement.setExpression(assignment);
            astRoot.replaceChild(functionNode, expressionStatement);
        }
        
    }

    private void functionCallToProperty(FunctionCall functionCall, Name name) {
        PropertyGet propertyGet = this.createPropertyGet(name);
        if (functionCall.getTarget() == name) {
            functionCall.setTarget(propertyGet);
        } else {
            List<AstNode> arguments = functionCall.getArguments();
            arguments.set(arguments.lastIndexOf(name), propertyGet);
            propertyGet.setParent(functionCall);
        }
    }

    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == Name.class) {
            Name name = (Name)astNode;
            if (isGlobalVar(name)) {
                globalToProperty(name);
            }
        }
        return true;
    }
}
