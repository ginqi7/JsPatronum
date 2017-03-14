package main.java;

import java.util.List;

import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.NodeVisitor;


public class VisitorTopFunction implements NodeVisitor {

    private List<AstNode> params;
    private List<AstNode> arguments;

    public VisitorTopFunction(List<AstNode> params, List<AstNode> arguments) {
        this.params = params;
        this.arguments = arguments;
    }
    
    private AstRoot addToTopFunction(AstRoot astRoot) {
        String string = "! function() {}()";
        AstRoot root = ImmediatelyInvokedFunction.createImmediatelyInvokedFunction(string);
        ImmediatelyInvokedFunction.addParamsAndArguments(root, this.params, this.arguments);
        ImmediatelyInvokedFunction.addFunctionBody(root, astRoot);
        return root;
    }

    private void changeRoot(AstRoot root, AstRoot astRoot) {
        root.removeChildren();
        List<AstNode> statements = astRoot.getStatements();
        for (AstNode statement : statements) {
            root.addChild(statement);
        }
    }
    
    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == AstRoot.class) {
            AstRoot astRoot = this.addToTopFunction((AstRoot) astNode);
			this.changeRoot((AstRoot) astNode, astRoot);
        }
        return false;
    }
}
