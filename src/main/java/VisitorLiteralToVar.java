package main.java;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.StringLiteral;

/**
 *
 * 字面量转换为函数参数
 * @author
 */
public class VisitorLiteralToVar implements NodeVisitor {
    private List<AstNode> params = new ArrayList<AstNode>();
    private List<AstNode> arguments = new ArrayList<AstNode>();
    private int count = 0;

    private Name createParam() {
        this.count += 1;
        Name name = new Name();
        name.setIdentifier("gin" + this.count);
        return name;
    }

    private void infixToVar(InfixExpression infixExpression, AstNode astNode, Name name) {
        if (infixExpression.getLeft() == astNode) {
            infixExpression.setLeft(name);
        } else {
            infixExpression.setRight(name);
        }
    }

    private void elementGetToVar(ElementGet elementGet, AstNode astNode, Name name) {
        if (elementGet.getTarget() == astNode) {
            elementGet.setTarget(name);
        } else {
            elementGet.setElement(name);
        }
    }

    private boolean argumentsHasNode(AstNode astNode) {
        for (AstNode argument : this.arguments) {
            if (argument.toSource().equals(astNode.toSource())) {
                return true;
            }
        }
        return false;
    }

    private AstNode getParamFromArgument(AstNode astNode) {
        for (int i = 0; i < this.arguments.size(); i++) {
            if (this.arguments.get(i).toSource().equals(astNode.toSource())) {
                return this.params.get(i);
            }
        }
        return null;
    }
    
    private void literalToVar(AstNode astNode) {
        Name name;
        if (this.argumentsHasNode(astNode)) {
			name = (Name) this.getParamFromArgument(astNode);
        }
        else {
            name = this.createParam();
            this.arguments.add(astNode);
            this.params.add(name);
        }
        AstNode parentNode = astNode.getParent();
        if (parentNode instanceof InfixExpression) {
            this.infixToVar((InfixExpression) parentNode, astNode, name);
        } else if (parentNode.getClass() == ElementGet.class) {
            this.elementGetToVar((ElementGet) parentNode, astNode, name);
        } else if (parentNode.getClass() == FunctionCall.class) {
            this.functionCallToVar((FunctionCall) parentNode, astNode, name);
        }
    }

    private void functionCallToVar(FunctionCall functionCall, AstNode astNode, Name name) {
        List<AstNode> arguments = functionCall.getArguments();
        for (int i = 0; i < arguments.size(); i++) {
            if (arguments.get(i) == astNode) {
                arguments.set(i, name);
            }
        }
    }

    private boolean isThisKeyword(AstNode astNode) {
        if (astNode.getClass() == KeywordLiteral.class &&
            ((KeywordLiteral) astNode).getType() == Token.THIS) {
            return true;
        }
        return false;
    }
        
    @Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == StringLiteral.class) {
            StringLiteral stringLiteral = (StringLiteral) astNode;
            stringLiteral.setQuoteCharacter('\"');
        }
        if (astNode.getClass() == StringLiteral.class || isThisKeyword(astNode)) {
            this.literalToVar(astNode);
        }
        return true;
	}

	/**
	 * @return the params
	 */
	public List<AstNode> getParams() {
		return params;
	}

	/**
	 * @return the arguments
	 */
	public List<AstNode> getArguments() {
		return arguments;
	}
}
