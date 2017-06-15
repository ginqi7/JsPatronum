package main.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.ObjectProperty;
import org.mozilla.javascript.ast.Scope;

/**
 *
 * 局部变量重命名
 * @author
 */
public class VisitorLocalVar implements NodeVisitor {

    private int number = 0;
    List<String> names = new ArrayList<String>();
    Map<String, String> nameMap = new HashMap<String, String>();
    private Map<Integer, Map<String, String>> scopeNamesMap = new HashMap<Integer, Map<String, String>>();

    private boolean isObjectPropertyName(Name name) {
        AstNode parentNode = name.getParent();
        if (parentNode != null &&
            parentNode.getClass() == ObjectProperty.class) {
            InfixExpression infixExpression = (InfixExpression)parentNode;
            if (infixExpression.getLeft() == name) {
                return true;
            }
        }
        return false;
    }

    private boolean isLegalVariableName(String name) {
        if (Build_in.keepKeywords.contains(name)) {
            return false;
        }
        return true;
    }
    
    private void randomRename(Name name) {
        if (isObjectPropertyName(name)) {
            return;
        } 
        Scope scope = name.getDefiningScope(); 
        if (scope != null && this.scopeNamesMap.containsKey(scope.hashCode())) {
            Map<String, String> nameMap = this.scopeNamesMap.get(scope.hashCode());
            if (!nameMap.containsKey(name.getIdentifier())) {
                String newName = Tool.getRandomName(Tool.lengthOfVar(this.number));
                while (!isLegalVariableName(newName) || names.contains(newName)) {
                    newName = Tool.getRandomName(Tool.lengthOfVar(this.number));
                }
                names.add(newName); 
                nameMap.put(name.getIdentifier(), newName);
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

    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == AstRoot.class) {
            number = this.countVar((AstRoot) astNode);
        } else if (astNode instanceof Scope) {
            scopeNamesMap.put(astNode.hashCode(), new HashMap<String, String>());
        } else if (astNode.getClass() == Name.class)  {
            this.randomRename((Name) astNode);
        }
		return true;
    }
}
