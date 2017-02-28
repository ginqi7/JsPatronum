package main.java;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.Scope;

public class VisitorGetName implements NodeVisitor{
    private int numbers = 0;
    private AstRoot astRoot;
    private List<String> globalNames = new ArrayList<String>();
    
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == AstRoot.class) {
            this.astRoot = (AstRoot) astNode;
        }  else if (astNode.getClass() == Name.class) {
            numbers++;
            Name name = (Name) astNode;
            Scope scope = name.getScope();
            if (scope == astRoot) {
                globalNames.add(name.getIdentifier());
            }
        }
        return true;
    }
    public int getNumbers() {
        return this.numbers;
    }

    public List<String> getGlobalNames() {
        return this.globalNames;
    }
}
