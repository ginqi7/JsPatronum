package main.java;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.VariableInitializer;

public class VisitorGlobalToLocal implements NodeVisitor {

    private List<Scope> scopes = new ArrayList<Scope>();
    private Map<String, String> paramMap;
    private Name thisName;

    public VisitorGlobalToLocal(Map<String, String> paramMap, Name thisName) {
        this.paramMap = paramMap;
        this.thisName = thisName;
    }
    
    @Override
    public boolean visit(AstNode astNode) {
        
        if (astNode.getClass() == FunctionNode.class) {
            this.scopes.add((Scope)astNode);
        } else if (astNode.getClass() == Name.class) {
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
                ElementGet elementGet = new ElementGet();
                elementGet.setTarget(this.thisName);
                elementGet.setElement(name);
                assignment.setLeft(elementGet);
                assignment.setRight(variableInitializer.getInitializer());
                assignment.setOperator(Token.ASSIGN);
                expressionStatement.setExpression(assignment);
                top.replaceChild(variableDeclaration, expressionStatement);
            } else if (parent.getClass() == FunctionCall.class ) {
                FunctionCall functionCall = (FunctionCall)parent;
                ElementGet elementGet = new ElementGet();
                List<AstNode> arguments = new ArrayList<AstNode>();
                elementGet.setTarget(this.thisName);
                elementGet.setElement(name);
                arguments.add(elementGet);
                functionCall.setArguments(arguments);
            } else if (parent.getClass() == Assignment.class) {
                Assignment assignment = (Assignment)parent;
                ElementGet elementGet = new ElementGet();
                elementGet.setTarget(this.thisName);
                elementGet.setElement(name);
                assignment.setLeft(elementGet);
            } else if (parent.getClass() == InfixExpression.class) {
                InfixExpression infixExpression = (InfixExpression)parent;
                ElementGet elementGet = new ElementGet();
                elementGet.setTarget(this.thisName);
                elementGet.setElement(name);
                if (infixExpression.getLeft().getClass() == Name.class) {
                    infixExpression.setLeft(elementGet);
                } else {
                    infixExpression.setRight(elementGet);
                }
            }
        }
        return true;
    }

}
