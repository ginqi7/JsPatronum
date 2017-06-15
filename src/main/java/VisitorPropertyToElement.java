package main.java;

import java.util.List;

import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.ConditionalExpression;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.ExpressionStatement;
import org.mozilla.javascript.ast.ForInLoop;
import org.mozilla.javascript.ast.ForLoop;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.IfStatement;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.ReturnStatement;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.UnaryExpression;
import org.mozilla.javascript.ast.VariableInitializer;
import org.mozilla.javascript.ast.WhileLoop;

/**
 *
 * 对象属性转换为对象元素
 * @author
 */
public class VisitorPropertyToElement implements NodeVisitor {

    private ElementGet createElementGet(PropertyGet propertyGet) {
        ElementGet elementGet = new ElementGet();
        StringLiteral stringLiteral = new StringLiteral();
        stringLiteral.setQuoteCharacter('\"');
        stringLiteral.setValue(propertyGet.getProperty().getIdentifier());
        elementGet.setTarget(propertyGet.getTarget());
        elementGet.setElement(stringLiteral);
        return elementGet;
    }
    
    private void infixToElement(InfixExpression infixExpression, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        if (infixExpression.getLeft() == propertyGet) {
            infixExpression.setLeft(elementGet);
        } else {
            infixExpression.setRight(elementGet);
        } 
    }

    private void elementGetToElement(ElementGet parentNode, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        if (parentNode.getTarget() == propertyGet) {
            parentNode.setTarget(elementGet);
        } else {
            parentNode.setElement(elementGet);
        }
    }

    private void functionCallToElement(FunctionCall functionCall, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        if (functionCall.getTarget() == propertyGet) {
            functionCall.setTarget(elementGet);
        } else {
            List<AstNode> arguments = functionCall.getArguments();
            arguments.set(arguments.lastIndexOf(propertyGet), elementGet);
            elementGet.setParent(functionCall);
        }
    }

    private void variableInitializerToElement(VariableInitializer variableInitializer, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        variableInitializer.setInitializer(elementGet);
    }

    private void unaryExpressionToElement(UnaryExpression unaryExpression, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        if (unaryExpression.getOperand() == propertyGet) {
            unaryExpression.setOperand(elementGet);
        }
    }
	private void conditionalExpressionToElement(ConditionalExpression conditionalExpression, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        if (conditionalExpression.getTestExpression() == propertyGet) {
            conditionalExpression.setTestExpression(elementGet);
        } else if (conditionalExpression.getTrueExpression() == propertyGet) {
            conditionalExpression.setTrueExpression(elementGet);
        } else if (conditionalExpression.getFalseExpression() == propertyGet) {
            conditionalExpression.setFalseExpression(elementGet);
        }
    }

    private void ifStatementToElement(IfStatement ifStatement, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        if (ifStatement.getCondition() == propertyGet) {
            ifStatement.setCondition(elementGet);
        } else if (ifStatement.getElsePart() == propertyGet) {
            ifStatement.setElsePart(propertyGet);
        } else if (ifStatement.getThenPart() == propertyGet) {
            ifStatement.setThenPart(propertyGet);
        } 
    }

    private void expressionStatementToElement(ExpressionStatement expressionStatement, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        expressionStatement.setExpression(elementGet);
    }

    private void returnStatementToElement(ReturnStatement returnStatement, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        returnStatement.setReturnValue(elementGet);
    }

    private void forInLoopToElement(ForInLoop forInLoop, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        if (forInLoop.getIteratedObject() == propertyGet) {
            forInLoop.setIteratedObject(elementGet);
        } else if (forInLoop.getIterator() == propertyGet) {
            forInLoop.setIterator(elementGet);
        } else if (forInLoop.getBody() == propertyGet) {
            forInLoop.setBody(elementGet);
        }
    }

    private void forLoopToElement(ForLoop forLoop, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        if (forLoop.getCondition() == propertyGet) {
            forLoop.setCondition(elementGet);
        } else if (forLoop.getInitializer() == propertyGet) {
            forLoop.setInitializer(elementGet);
        } else if (forLoop.getIncrement() == propertyGet) {
            forLoop.setIncrement(elementGet);
        } else if (forLoop.getBody() == propertyGet) {
            forLoop.setBody(elementGet);
        }
    }

    private void whileLoopToElement(WhileLoop whileLoop, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        if (whileLoop.getCondition() == propertyGet) {
            whileLoop.setCondition(elementGet);
        } else if (whileLoop.getBody() == propertyGet) {
            whileLoop.setBody(elementGet);
        }
    }

    private void arrayLiteralToElement(ArrayLiteral arrayLiteral, PropertyGet propertyGet) {
        ElementGet elementGet = this.createElementGet(propertyGet);
        List<AstNode> elements = arrayLiteral.getElements();
        for (int i = 0; i < elements.size(); i++) {
            if (elements.get(i) == propertyGet) {
                elements.set(i, elementGet);
            }
        }
    }

    private void propertyToElement(PropertyGet propertyGet) {
        AstNode parentNode = propertyGet.getParent();
        if (parentNode instanceof InfixExpression) {
            this.infixToElement((InfixExpression) parentNode, propertyGet);
        } else if (parentNode.getClass() == ElementGet.class) {
            this.elementGetToElement((ElementGet) parentNode, propertyGet);
        } else if (parentNode instanceof FunctionCall) {
            this.functionCallToElement((FunctionCall) parentNode, propertyGet);
        } else if (parentNode.getClass() == VariableInitializer.class) {
            this.variableInitializerToElement((VariableInitializer) parentNode, propertyGet);
        } else if (parentNode.getClass() == UnaryExpression.class) {
			this.unaryExpressionToElement((UnaryExpression) parentNode, propertyGet);
		} else if (parentNode.getClass() == ConditionalExpression.class) {
            this.conditionalExpressionToElement((ConditionalExpression) parentNode, propertyGet);
		} else if (parentNode.getClass() == ExpressionStatement.class) {
			this.expressionStatementToElement((ExpressionStatement) parentNode, propertyGet);
		} else if (parentNode.getClass() == IfStatement.class) {
			this.ifStatementToElement((IfStatement) parentNode, propertyGet);
		} else if (parentNode.getClass() == ReturnStatement.class) {
			this.returnStatementToElement((ReturnStatement) parentNode, propertyGet);
		}  else if (parentNode.getClass() == ForInLoop.class) {
			this.forInLoopToElement((ForInLoop) parentNode, propertyGet);
		} else if (parentNode.getClass() == ForLoop.class) {
			this.forLoopToElement((ForLoop) parentNode, propertyGet);
		} else if (parentNode.getClass() == WhileLoop.class) {
			this.whileLoopToElement((WhileLoop) parentNode, propertyGet);
		} else if (parentNode.getClass() == ArrayLiteral.class) {
			this.arrayLiteralToElement((ArrayLiteral) parentNode, propertyGet);
		}
	}

	@Override
    public boolean visit(AstNode astNode) {
        if (astNode.getClass() == PropertyGet.class) {
            propertyToElement((PropertyGet) astNode);
        }
        return true;
    }
}
