package main.java;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.FunctionNode;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.NumberLiteral;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.UnaryExpression;

/**
 *
 * 遍历 AST 树将字符串实参转换为字符数组
 * @author
 */
public class VisitorStringToArray implements NodeVisitor {

	/**
	 * 创造最外层 FunctionCall 的参数
	 */
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
			return this.randomArrayArguments();
		}

		/**
		 *
		 * 将字符数组打乱
		 * @return 返回新的字符数组参数
		 */
		private List<AstNode> randomArrayArguments() {
            List<AstNode> arguments = new ArrayList<AstNode>();
            ArrayLiteral arrayLiteral = (ArrayLiteral) this.arguments.get(0);
            List<AstNode> astNodes = arrayLiteral.getElements();
            for (int i = 0; i < astNodes.size(); i++) {
                int randomNum = (int)(Math.random()*astNodes.size());
                AstNode tmpNode = astNodes.get(randomNum);
                astNodes.set(randomNum, astNodes.get(i));
                astNodes.set(i, tmpNode);
            }
            arguments.add(arrayLiteral);
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

	/**
	 *
	 * 内层 FunctionCall 的实参转换为函数调用
	 * @param astRoot
	 */
    private void changeLowArguments(AstRoot astRoot) {
        astRoot.visit(new NodeVisitor() {
                private int functionCallNum = 0;
                private int functionNodeNum = 0;
                private Name name;
                private ArrayLiteral arrayLiteral;
                @Override
                public boolean visit(AstNode astNode) {
                    if (astNode.getClass() == FunctionCall.class) {
                        FunctionCall functionCall = (FunctionCall) astNode;
                        functionCallNum += 1;
                        if (functionCallNum == 1) {
                            FunctionCall subFunctionCall = (FunctionCall) functionCall.getArguments().get(0);
                            arrayLiteral = (ArrayLiteral) subFunctionCall.getArguments().get(0);
                        } else if (functionCallNum == 2) {
                            List<AstNode> oldArguments = functionCall.getArguments();
                            List<AstNode> newArguments = this.createNewArguments(oldArguments);
                            functionCall.setArguments(newArguments);
                        }
                    } else if (astNode.getClass() == FunctionNode.class) {
                        FunctionNode functionNode = (FunctionNode) astNode;
                        functionNodeNum += 1;
                        if (functionNodeNum == 1) {
                            this.name = (Name) functionNode.getParams().get(0);
                        }
                    }
                    return true;
                }

                private List<AstNode> createNewArguments(List<AstNode> oldArguments) {
                    List<AstNode> newArguments = new ArrayList<AstNode>();
                    for (AstNode oldArgument : oldArguments) {
                        if (oldArgument.getClass() == StringLiteral.class) {
                            newArguments.add(this.oldToNewArgument((StringLiteral) oldArgument));
                        } else {
                            newArguments.add(oldArgument);
                        }
                    }
                    return newArguments;
                }

                private AstNode oldToNewArgument(StringLiteral oldArgument) {
                    FunctionCall functionCall = new FunctionCall();
                    functionCall.setTarget(this.name);
                    List<AstNode> numArguments = this.createNumArguments(oldArgument);
                    functionCall.setArguments(numArguments);
                    return functionCall;
                }

                private List<AstNode> createNumArguments(StringLiteral oldArgument) {
                    List<AstNode> numArguments = new ArrayList<AstNode>();
                    String oldArgumentString = oldArgument.getValue();
                    List<AstNode> charNodes = this.arrayLiteral.getElements();
                    for (int i = 0; i < oldArgumentString.length(); i++) {
                        String charString = oldArgumentString.charAt(i) + "";
                        int index = this.indexOfCharNodes(charNodes, charString);
                        if (index != -1) {
                            NumberLiteral numberLiteral = new NumberLiteral();
                            numberLiteral.setValue(index + "");
                            numArguments.add(numberLiteral);
                        }
                    }
                    return numArguments;
                }

                private int indexOfCharNodes(List<AstNode> charNodes, String charString) {
                    for (int i = 0; i < charNodes.size(); i++) {
                        StringLiteral stringLiteral = (StringLiteral) charNodes.get(i);
                        if (stringLiteral.getValue().equals(charString)) {
                            return i;
                        }
                    }
                    return -1;
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
