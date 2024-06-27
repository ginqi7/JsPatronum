package com.qiqijin.jspatronum;

import java.net.URLEncoder;

import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.StringLiteral;

public class VisitorConstant implements NodeVisitor{

    private boolean isIntStr(String numStr) {
        for (int i = 0; i < numStr.length(); i++) {
            if (numStr.charAt(i) == '.') {
                return false;
            }
        }
        return true;
    }
    
    private void recodeNumber(NumberLiteral numberLiteral) {
        try {
            String numStr = numberLiteral.getValue();
            if (isIntStr(numStr)) {
                if (numStr.length() >= 2 && numStr.substring(0, 2).equals("0x")) {
                    return;
                }
                int number = Integer.parseInt(numStr);
                numberLiteral.setValue("0x" + Integer.toHexString(number));
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }
    
    private String getUCode(String value) {
        String codeString = new String();
        char[] valueChar = value.toCharArray();
        for (int i = 0; i < valueChar.length; i++) {
            codeString += String.format("\\u%04x", Character.codePointAt(valueChar, i));
        }
        return codeString;
    }

    private String encode(String s) {
        StringBuilder sb = new StringBuilder(s.length() * 3);
        for (char c : s.toCharArray()) {
                sb.append("\\u");
                sb.append(Character.forDigit((c >>> 12) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 8) & 0xf, 16));
                sb.append(Character.forDigit((c >>> 4) & 0xf, 16));
                sb.append(Character.forDigit((c) & 0xf, 16));

        }
        return sb.toString();
    }
    
    private void recodeString(StringLiteral stringLiteral) {
        String value = stringLiteral.getValue();
		// stringLiteral.setValue(this.getUCode(value));
        AstNode parent = stringLiteral.getParent();
        if (parent.getClass() == ArrayLiteral.class) {
            stringLiteral.setValue(this.encode(value));         
        }
    }
    
    @Override
	public boolean visit(AstNode astNode) {
        if (astNode.getClass() == NumberLiteral.class) {
            this.recodeNumber((NumberLiteral) astNode);
		} else if (astNode.getClass() == StringLiteral.class) {
            this.recodeString((StringLiteral) astNode);
		}
		return true;
	}






}
