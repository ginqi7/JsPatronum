package main.java;

import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.Scope;

public class VisitorNullScope implements NodeVisitor{

    private AstRoot astRoot;
	@Override
	public boolean visit(AstNode astNode) {
        if (astNode.getClass() == AstRoot.class) {
            this.astRoot = (AstRoot)astNode;
        }
        if (astNode.getClass() == Name.class) {
            Name name = (Name)astNode;
            Scope scope = name.getDefiningScope();
            if (scope == null) {
                AstNode parent = name.getParent();
                if (parent.getClass() == Assignment.class) {
                    name.setScope(this.astRoot);
                }
                if (parent.getClass() == PropertyGet.class &&
                    ((PropertyGet)parent).getTarget() == name) {
                    name.setScope(this.astRoot);
                }
            } else {
                name.setScope(scope);
            } 
        }
        return true;
    }
}
