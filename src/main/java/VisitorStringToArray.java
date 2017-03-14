package main.java;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.Block;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.ParenthesizedExpression;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.UnaryExpression;

public class VisitorStringToArray implements NodeVisitor {
    
    private void addParamsAndArguments(AstRoot astRoot) {
        astRoot.visit(new NodeVisitor() {
                private void addParams(FunctionNode functionNode) {
                    Name name = new Name();
                    name.setIdentifier("Gin");
                    functionNode.addParam(name);
                }

                @Override
                public boolean visit(AstNode astNode) {
                    if (astNode.getClass() == FunctionCall.class) {
                        this.addArguments(astNode);
                    } else if (astNode.getClass() == FunctionNode.class) {
                        this.addParams(astNode);
                    }
                    return true;
                }
            });
    }
    
    private void stringToArray(AstRoot astRoot) {
        this.addParamsAndArguments(astRoot);
    }
    
    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == AstRoot.class) {
            this.stringToArray((AstRoot) astNode);
        }
		return false;
	}

}
