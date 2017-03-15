package main.java;

import java.util.List;

import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.NodeVisitor;

public class ImmediatelyInvokedFunction {
    public static void addParamsAndArguments(AstRoot root, final List<AstNode> params, final List<AstNode> arguments) {
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
    
    public static AstRoot createImmediatelyInvokedFunction(String string) {
        AstRoot root = new Parser().parse(string, null, 1);
        return root;
    }
    
    public static void addFunctionBody(AstRoot root, AstRoot astRoot) {
        final List<AstNode> statements = astRoot.getStatements();
        root.visit(new NodeVisitor() {
                    @Override
                    public boolean visit(AstNode astNode) {
                        if (astNode.getClass() == Block.class) {
                            for (AstNode statement : statements) {
                                astNode.addChild(statement);
                            }
                            return false;
                        }
                        return true;
                    }

            });
	}
}
