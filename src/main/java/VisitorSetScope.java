package main.java;

import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.ScriptNode;
import org.mozilla.javascript.ast.Symbol;

public class VisitorSetScope implements NodeVisitor {

    private boolean symbolsHasName(List<Symbol> symbols, Name name) {
        for (Symbol symbol : symbols) {
            String nameString = symbol.getName();
            if (nameString.equals(name.getIdentifier())){
                return true;
            }
        }
        return false;
    }

    private Scope findScope(Name name) {
        Scope scope = null;
        AstNode parentNode = name.getParent();
        while (scope == null) {
            if (parentNode.getClass() == AstRoot.class) {
                scope = (Scope) parentNode;
            }
            else if (parentNode instanceof ScriptNode){
                ScriptNode scriptNode = (ScriptNode)parentNode;
                List<Symbol> symbols = scriptNode.getSymbols();
                if (symbolsHasName(symbols, name)) {
                    scope = scriptNode;
                } else {
                    parentNode = parentNode.getParent();
                }
            }
        }
        return scope;
    }

    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == Name.class) {
            Name name = (Name)astNode;
            Scope definingScope = name.getDefiningScope();
            if (definingScope != null) {
                name.setScope(definingScope);
            } else {
                name.setScope(findScope(name));
            }
        }
        return true;
    }

}
