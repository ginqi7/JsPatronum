package main.java;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.VariableDeclaration;
import org.mozilla.javascript.ast.VariableInitializer;

/**
 *
 * 全局变量转换为this属性
 * @author
 */
public class VisitorGlobalVar implements NodeVisitor {

    private boolean isGlobalVar(Name name) {
        if (name.getScope().getClass() == AstRoot.class) {
            return true;
        }
        return false;
    }

    private PropertyGet createPropertyGet(Name name) {
        PropertyGet propertyGet = new PropertyGet();
        KeywordLiteral keywordLiteral = new KeywordLiteral();
        keywordLiteral.setType(Token.THIS);
        propertyGet.setTarget(keywordLiteral);
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
		}
	}

	private void functionCallToProperty(FunctionCall functionCall, Name name) {
        List<AstNode> arguments = new ArrayList<AstNode>();
        PropertyGet propertyGet = this.createPropertyGet(name);
        arguments.add(propertyGet);
        functionCall.setArguments(arguments);
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
