package main.java;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.UnaryExpression;

import jdk.nashorn.internal.runtime.arrays.ArrayLikeIterator;

public class VisitorStringToArray implements NodeVisitor {

    private class VisitorCreateArguments implements NodeVisitor {
        private List<AstNode> arguments;

        public VisitorCreateArguments() {
            this.arguments = new ArrayList<AstNode>();
            ArrayLiteral arrayLiteral = new ArrayLiteral();
            this.arguments.add(arrayLiteral);
        }
        
        @Override
        public boolean visit(AstNode astNode) {
            if (astNode.getClass() == StringLiteral.class) {
				this.addCharToArguments((StringLiteral) astNode);
			}
			return true;
		}

		private void addCharToArguments(StringLiteral astNode) {
            String literalString = astNode.getValue();
            for (int i = 0; i < literalString.length(); i++) {
                String c = literalString.charAt(i) + "";
                if (!this.argumentsHasChar(c)) {
                    ArrayLiteral arrayLiteral = (ArrayLiteral) this.arguments.get(0);
                    StringLiteral stringLiteral = new StringLiteral();
                    stringLiteral.setQuoteCharacter('\"');
                    stringLiteral.setValue(c);
                    arrayLiteral.addElement(stringLiteral);
                }
			}
		}

		private boolean argumentsHasChar(String c) {
			ArrayLiteral arrayLiteral = (ArrayLiteral) this.arguments.get(0);
            List<AstNode> astNodes = arrayLiteral.getElements();
            for (AstNode astNode : astNodes) {
				StringLiteral stringLiteral = (StringLiteral) astNode;
                if (stringLiteral.getValue().equals(c)) {
                    return true;
                }
            }
            return false;
		}

		/**
		 * @return the arguments
		 */
		public List<AstNode> getArguments() {
			return arguments;
		}
    }
    
    private void stringToArray(AstRoot astRoot) {
		List<AstNode> params = this.createParams();
		List<AstNode> arguments = this.createArguments(astRoot);
        VisitorTopFunction visitorTopFunction = new VisitorTopFunction(params, arguments);
        astRoot.visit(visitorTopFunction);
		this.changeLowArguments(astRoot);
    }

    private void changeLowArguments(AstRoot astRoot) {
        astRoot.visit(new NodeVisitor() {
                private int functionCallNum = 0;
                private ArrayLiteral arrayLiteral;
                @Override
                public boolean visit(AstNode astNode) {
                    if (astNode.getClass() == FunctionCall.class) {
                        FunctionCall functionCall = (FunctionCall) astNode;
                        functionCallNum += 1;
                        if (functionCallNum == 1) {
                            arrayLiteral = (ArrayLiteral) functionCall.getArguments().get(0);
                        } else if (functionCallNum == 2) {
                            List<AstNode> arguments = functionCall.getArguments();
                            for (int i = 0; i < arguments.size(); i++) {
                                AstNode argument = this.createNewArgument(arguments.get(i));
                            }
                            return false;
                        }
                    }
                    return true;
                }

                
            });
    }

    private List<AstNode> createLowArguments(AstRoot astRoot) {
        VisitorCreateArguments visitorCreateArguments = new VisitorCreateArguments();
        astRoot.visit(visitorCreateArguments);
        return visitorCreateArguments.getArguments(); 
    }

    private List<AstNode> createArguments(AstRoot astRoot) {
        List<AstNode> arguments = new ArrayList<AstNode>();
        String string = "!function() {" +
            "return function() {" +
            "for (var t = arguments, r = \"\", u = 0, i = t.length; i > u; u++)r += Gin[t[u]];"  +
            "return r; " +
            "}}()";
        AstRoot root = ImmediatelyInvokedFunction.createImmediatelyInvokedFunction(string);
        ImmediatelyInvokedFunction.addParamsAndArguments(root, this.createParams(), this.createLowArguments(astRoot));
        FunctionCall functionCall = this.getFunctionCall(root);
        arguments.add(functionCall);
        System.out.println(functionCall.toSource());
        return arguments;
	}

	private FunctionCall getFunctionCall(AstRoot root) {
		ExpressionStatement expressionStatement = (ExpressionStatement) root.getFirstChild();
		UnaryExpression unaryExpression = (UnaryExpression) expressionStatement.getExpression();
		FunctionCall functionCall = (FunctionCall) unaryExpression.getOperand();
        return functionCall;
    }

	private List<AstNode> createParams() {
        List<AstNode> params = new ArrayList<AstNode>();
        Name name = new Name();
        name.setIdentifier("Gin");
        params.add(name);
        return params;
	}

	@Override
    public boolean visit(AstNode astNode) {
		if (astNode.getClass() == AstRoot.class) {
			this.stringToArray((AstRoot) astNode);
		}
		return false;
	}

}
