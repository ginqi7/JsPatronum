package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.Scope;

public class VisitorLocalVar implements NodeVisitor {

    private int number = 0;
    private Map<Scope, Map<String, String>> scopeNamesMap = new HashMap<Scope, Map<String, String>>();
        
    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == AstRoot.class) {
			number = this.countVar((AstRoot) astNode);
        } else if (astNode instanceof Scope) {
			scopeNamesMap.put((Scope) astNode, new HashMap<String, String>());
        } else if (astNode.getClass() == Name.class)  {
            this.randomRename((Name) astNode);
		}
		return true;
	}

	private void randomRename(Name name) {
        Scope scope = name.getScope();
        if (this.scopeNamesMap.containsKey(scope)) {
            Map<String, String> nameMap = this.scopeNamesMap.get(scope);
            if (!nameMap.containsKey(name.getIdentifier())) {
                System.out.println(this.number);
                nameMap.put(name.getIdentifier(), Tool.getRandomName(Tool.lengthOfVar(this.number)));
            }
            name.setIdentifier(nameMap.get(name.getIdentifier()));
        }
	}
    
	private int countVar(AstRoot astRoot) {
        
        class VisitorCountVar implements NodeVisitor {
            private int varNumber = 0;
            
            @Override
            public boolean visit(AstNode astNode) {
                if (astNode.getClass() == Name.class) {
                    varNumber += 1;
                }
                return true;
			}

			/**
			 * @return the varNumber
			 */
			public int getVarNumber() {
				return varNumber;
			}

		}

        VisitorCountVar visitorCountVar = new VisitorCountVar();
        astRoot.visit(visitorCountVar);
        return visitorCountVar.getVarNumber();
	}

}
