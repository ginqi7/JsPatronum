package main.java;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.StringLiteral;

public class VisitorStringLiteral implements NodeVisitor {
    private AstRoot astRoot;

    public VisitorStringLiteral(AstRoot astRoot) {
        this.astRoot = astRoot;
    }
    
    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == StringLiteral.class) {
            StringLiteral stringLiteral = (StringLiteral)astNode;
			ExpressionStatement expressionStatement = (ExpressionStatement) this.astRoot.getFirstChild();
			FunctionCall functionCall = (FunctionCall) expressionStatement.getExpression();
        }
        return true;
    }

}
