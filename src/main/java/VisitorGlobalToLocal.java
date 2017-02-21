package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.VariableInitializer;

public class VisitorGlobalToLocal implements NodeVisitor {

    private List<Scope> scopes = new ArrayList<Scope>();
    private Map<String, String> paramMap;

    public VisitorGlobalToLocal(Map<String, String> paramMap) {
        this.paramMap = paramMap;
    }
    
    @Override
    public boolean visit(AstNode astNode) {
        
        if (astNode.getClass() == FunctionNode.class) {
            this.scopes.add((Scope) astNode);
        }
        if (astNode.getClass() == Name.class) {
            Name name = (Name)astNode;
            String nameStr = name.getIdentifier();
            if (!paramMap.containsKey(nameStr)) {
                return true;
            }
            name.setIdentifier(paramMap.get(name.getIdentifier()));
            AstNode parent = name.getParent();
            if (parent.getClass() == VariableInitializer.class) {
                VariableInitializer variableInitializer = (VariableInitializer)parent;
                AstNode variableDeclaration = variableInitializer.getParent();
                AstNode top = variableDeclaration.getParent();
                ExpressionStatement expressionStatement = new ExpressionStatement();
                Assignment assignment = new Assignment();
                assignment.setLeft(name);
                assignment.setRight(variableInitializer.getInitializer());
                System.out.println(variableInitializer.getTarget().getClass());
                assignment.setOperator(Token.ASSIGN);
                expressionStatement.setExpression(assignment);
                top.replaceChild(variableDeclaration, expressionStatement);
            }
        }
        return true;
    }

}
