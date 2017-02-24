package main.java;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.NumberLiteral;

public class VisitorChangeNumber implements NodeVisitor {

	@Override
	public boolean visit(AstNode astNode) {
        if (astNode.getClass() == NumberLiteral.class) {
            NumberLiteral numberLiteral = (NumberLiteral)astNode;
            double number = numberLiteral.getNumber();
            Double numObj = new Double(number); 
            System.out.println(numObj.intValue() == number);
            if (numObj.intValue() == number) {
                numberLiteral.setValue("0x"+Integer.toHexString(numObj.intValue()));
            }
        }
        return true;
	}

}

