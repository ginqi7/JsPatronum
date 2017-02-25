package main.java;

import org.mozilla.javascript.ast.Assignment;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.StringLiteral;

public class VisitorStringLiteral implements NodeVisitor {
    private FunctionCall functionCall;
    private FunctionNode functionNode;
    private int count = 0;

    public VisitorStringLiteral(FunctionCall functionCall, FunctionNode functionNode) {
        this.functionCall = functionCall;
        this.functionNode = functionNode;
    }
    
    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == StringLiteral.class) {
            count += 1;
            StringLiteral stringLiteral = (StringLiteral)astNode;
            Name name = new Name();
            name.setIdentifier("str"+count);
            AstNode parentNode = stringLiteral.getParent();
            if (parentNode.getClass() == Assignment.class) {
                Assignment assignment = (Assignment)parentNode;
                assignment.setRight(name);
            }
            this.functionNode.addParam(name);
            this.functionCall.addArgument(stringLiteral);
        }
        return true;
    }
}
