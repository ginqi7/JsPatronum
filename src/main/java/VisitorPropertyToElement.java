package main.java;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.StringLiteral;

public class VisitorPropertyToElement implements NodeVisitor {

    private ElementGet createElementGet(PropertyGet propertyGet) {
        ElementGet elementGet = new ElementGet();
        StringLiteral stringLiteral = new StringLiteral();
        stringLiteral.setQuoteCharacter('\"');
        stringLiteral.setValue(propertyGet.getProperty().getIdentifier());
        elementGet.setTarget(propertyGet.getTarget());
        elementGet.setElement(stringLiteral);
        return elementGet;
    }
    
    private void infixToElement(InfixExpression infixExpression, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        if (infixExpression.getLeft() == propertyGet) {
            infixExpression.setLeft(elementGet);
        } else {
            infixExpression.setRight(elementGet);
        } 
    }

    private void elementGetToElement(ElementGet parentNode, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        parentNode.setTarget(elementGet);
        
    }

    private void functionCallToElement(FunctionCall functionCall, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        if (functionCall.getTarget() == propertyGet) {
            functionCall.setTarget(elementGet);
        } else {
            List<AstNode> arguments = new ArrayList<AstNode>();
            arguments.add(elementGet);
            functionCall.setArguments(arguments);
        }

    }

    private void propertyToElement(PropertyGet propertyGet) {
        AstNode parentNode = propertyGet.getParent();
        if (parentNode instanceof InfixExpression) {
			this.infixToElement((InfixExpression) parentNode, propertyGet);
        } else if (parentNode.getClass() == ElementGet.class) {
			this.elementGetToElement((ElementGet) parentNode, propertyGet);
        } else if (parentNode.getClass() == FunctionCall.class) {
			this.functionCallToElement((FunctionCall) parentNode, propertyGet);
        }
    }
    
    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == PropertyGet.class) {
            propertyToElement((PropertyGet) astNode);
        }
        return true;
    }

}
