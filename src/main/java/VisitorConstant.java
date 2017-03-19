package main.java;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.StringLiteral;

public class VisitorConstant implements NodeVisitor{

	@Override
	public boolean visit(AstNode astNode) {
        if (astNode.getClass() == NumberLiteral.class) {
			this.recodeNumber((NumberLiteral) astNode);
		} else if (astNode.getClass() == StringLiteral.class) {
            this.recodeString((StringLiteral) astNode);
		}
		return true;
	}

	private void recodeString(StringLiteral stringLiteral) {
        String value = stringLiteral.getValue();
		stringLiteral.setValue(this.getUCode(value));
	}

	private String getUCode(String value) {
        String codeString = new String();
        char[] valueChar = value.toCharArray();
        for (int i = 0; i < valueChar.length; i++) {
            codeString += String.format("\\u%04x", Character.codePointAt(valueChar, i));
        }
        return codeString;
	}

	private void recodeNumber(NumberLiteral numberLiteral) {
        try {
            int number = Integer.parseInt(numberLiteral.getValue());
            numberLiteral.setValue("0x" + Integer.toHexString(number));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
}
