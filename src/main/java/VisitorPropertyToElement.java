package main.java;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.StringLiteral;

public class VisitorPropertyToElement implements NodeVisitor {

	@Override
	public boolean visit(AstNode astNode) {
        if (astNode.getClass() == PropertyGet.class) {
            PropertyGet propertyGet = (PropertyGet)astNode;
            AstNode parent = propertyGet.getParent();
            // System.out.println(propertyGet.getProperty().getIdentifier());
                ElementGet elementGet = new ElementGet();
            StringLiteral stringLiteral = new StringLiteral(); 
            AstNode target = propertyGet.getTarget();
            Name name  = propertyGet.getProperty();
            String nameStr = name.getIdentifier();
            stringLiteral.setQuoteCharacter('\"');
            stringLiteral.setValue(nameStr);
            elementGet.setTarget(target);
            elementGet.setElement(stringLiteral);
            System.out.println(parent.getClass());
            if (parent.getClass() == FunctionCall.class) {
                FunctionCall functionCall = (FunctionCall)parent;
                functionCall.setTarget(elementGet);
            }
        }
        return true;
	}

}
