package main.java;

import java.util.HashMap;
import java.util.Map;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.Scope;

public class VisitorRename implements NodeVisitor {
    private VisitorGetName visitorGetName = new VisitorGetName();
    private Map<Scope, Map<String, String>> scopeMap = new HashMap<Scope, Map<String, String>>();
    
    public VisitorRename(AstRoot astRoot) {
        astRoot.visit(this.visitorGetName);
    }
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == Name.class && ((Name)astNode).getDefiningScope() != null) {
            Name name = (Name)astNode;
            Scope scope = name.getDefiningScope();
            int nums = this.visitorGetName.getNumbers();
            if (!scopeMap.containsKey(scope)) {
                this.scopeMap.put(scope, new HashMap<String, String>());
            }
            String nameStr = name.getIdentifier();
            Map<String, String> nameMap = scopeMap.get(scope);
            if (!nameMap.containsKey(nameStr)) {
                String newName = Tool.getRandomName(Tool.lengthOfVar(nums));
                while (nameMap.containsValue(newName) ||
                       Build_in.keepKeywords.contains(newName)) {
                    newName = Tool.getRandomName(nums);
                }
                nameMap.put(nameStr, newName);
            }
            name.setIdentifier(nameMap.get(nameStr));
        }
        return true;
    }
}
