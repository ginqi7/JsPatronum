package main.java;

import java.util.List;

import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.NodeVisitor;

public class ImmediatelyInvokedFunction {

    private List<AstNode> arguments;
    private List<AstNode> params;

    public ImmediatelyInvokedFunction(String string, List<AstNode> params, List<AstNode> arguments, AstRoot astRoot) {
        
    }

    private void addParamsAndArguments(AstRoot root, final List<AstNode> params, final List<AstNode> arguments) {
        root.visit(new NodeVisitor() {
                @Override
                public boolean visit(AstNode astNode) {
                    if (astNode.getClass() == FunctionCall.class) {
                        FunctionCall functionCall = (FunctionCall) astNode;
                        functionCall.setArguments(arguments);
                    } else if (astNode.getClass() == FunctionNode.class) {
                        FunctionNode functionNode = (FunctionNode) astNode;
                        functionNode.setParams(params);
                    }
                    return true;
                }
                
            });
    }
    
    public AstRoot createImmediatelyInvokedFunction(String string, List<AstNode> params, List<AstNode> arguments) {
        AstRoot root = new Parser().parse(string, null, 1);
        this.addParamsAndArguments(root, params, arguments);
        return root;
    }

    
}
