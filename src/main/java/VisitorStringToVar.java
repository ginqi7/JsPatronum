package main.java;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.StringLiteral;

public class VisitorStringToVar implements NodeVisitor {
    private List<StringLiteral> stringLiterals = new ArrayList<StringLiteral>();
    private List<Name> names = new ArrayList<Name>();
    private int count = 0;

    private Name addName() {
        this.count += 1;
        Name name = new Name();
        name.setIdentifier("gin" + this.count);
        this.names.add(name);
        return name;
    }

    private void infixToVar(InfixExpression infixExpression, StringLiteral stringLiteral, Name name) {
        if (infixExpression.getLeft() == stringLiteral) {
            infixExpression.setLeft(name);
        } else {
            infixExpression.setRight(name);
        }
    }

    private void elementGetToVar(ElementGet elementGet, Name name) {
        elementGet.setElement(name);
    }
    
    private void stringToVar(AstNode astNode) {
        this.stringLiterals.add((StringLiteral) astNode);
        Name name = this.addName();

        StringLiteral stringLiteral = (StringLiteral) astNode;
        AstNode parentNode = astNode.getParent();
        if (parentNode instanceof InfixExpression) {
            this.infixToVar((InfixExpression) parentNode, stringLiteral, name);
        } else if (parentNode.getClass() == ElementGet.class) {
			this.elementGetToVar((ElementGet) parentNode, name);
        }
        
    }
        
    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == StringLiteral.class) {
            this.stringToVar(astNode);    
        }
        return true;
    }

/**
 * @return the stringLiterals
 */
    public List<StringLiteral> getStringLiterals() {
        return stringLiterals;
    }

/**
 * @return the names
 */
    public List<Name> getNames() {
        return names;
    }

}
