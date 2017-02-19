package main.java;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;

public class VisitorGetName implements NodeVisitor{
    private int numbers = 0;
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == Name.class) {
            numbers++;
        }
        return true;
    }
    public int getNumbers() {
        return this.numbers;
    }
}
