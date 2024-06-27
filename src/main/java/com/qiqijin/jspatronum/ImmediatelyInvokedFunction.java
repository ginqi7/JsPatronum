package com.qiqijin.jspatronum;
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
                boolean flag = true;
                @Override
                public boolean visit(AstNode astNode) {
                    if (astNode.getClass() == FunctionCall.class && this.flag) {
                        FunctionCall functionCall = (FunctionCall) astNode;
                        functionCall.setArguments(arguments);
                        this.flag = false;
                    } else if (astNode.getClass() == FunctionNode.class) {
                        FunctionNode functionNode = (FunctionNode) astNode;
                        functionNode.setParams(params);
                        return false;
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
                private boolean flag = true;
                @Override
                public boolean visit(AstNode astNode) {
                    if (astNode.getClass() == Block.class && flag) {
                        for (AstNode statement : statements) {
                            astNode.addChild(statement);
                        }
                        flag = false;
                        return false;
                    }
                    return true;
                }
            });
	}
}
