package com.qiqijin.jspatronum;

import java.util.ArrayList;
import java.util.List;

import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.ArrayLiteral;
import org.mozilla.javascript.ast.AstNode;
import org.mozilla.javascript.ast.AstRoot;
import org.mozilla.javascript.ast.ConditionalExpression;
import org.mozilla.javascript.ast.ElementGet;
import org.mozilla.javascript.ast.FunctionCall;
import org.mozilla.javascript.ast.InfixExpression;
import org.mozilla.javascript.ast.KeywordLiteral;
import org.mozilla.javascript.ast.Name;
import org.mozilla.javascript.ast.NodeVisitor;
import org.mozilla.javascript.ast.ObjectProperty;
import org.mozilla.javascript.ast.PropertyGet;
import org.mozilla.javascript.ast.Scope;
import org.mozilla.javascript.ast.StringLiteral;
import org.mozilla.javascript.ast.VariableInitializer;

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

    private void functionCallToVar(FunctionCall functionCall, AstNode astNode, Name name) {
        List<AstNode> arguments = functionCall.getArguments();
        for (int i = 0; i < arguments.size(); i++) {
            if (arguments.get(i) == astNode) {
                arguments.set(i, name);
            }
        }
    }

    private void arrayLiteralToVar(ArrayLiteral arrayLiteral, AstNode astNode, Name name) {
        List<AstNode> arrayList = arrayLiteral.getElements();
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i) == astNode) {
                arrayList.set(i, name);
            }
        }
    }

    private void conditionalExpressionToVar(ConditionalExpression conditionalExpression, AstNode astNode, Name name) {
        if (conditionalExpression.getFalseExpression() == astNode) {
            conditionalExpression.setFalseExpression(name);
        } else if (conditionalExpression.getTestExpression() == astNode) {
            conditionalExpression.setTestExpression(name);
        } else if (conditionalExpression.getTrueExpression() == astNode) {
            conditionalExpression.setTrueExpression(name);
        } 
    }

    private void variableInitializerToVar(VariableInitializer variableInitializer, AstNode astNode, Name name) {
        variableInitializer.setInitializer(name);
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
        } else if (parentNode instanceof FunctionCall) {
            this.functionCallToVar((FunctionCall) parentNode, astNode, name);
        } else if (parentNode.getClass() == ArrayLiteral.class) {
			this.arrayLiteralToVar((ArrayLiteral) parentNode, astNode, name);
		} else if (parentNode.getClass() == ConditionalExpression.class) {
			this.conditionalExpressionToVar((ConditionalExpression) parentNode, astNode, name);
		} else if (parentNode.getClass() == VariableInitializer.class) {
			this.variableInitializerToVar((VariableInitializer) parentNode, astNode, name);
		}
	}

	private boolean isThisKeyword(AstNode astNode) {
        if (astNode.getClass() == KeywordLiteral.class &&
            ((KeywordLiteral) astNode).getType() == Token.THIS) {
            AstNode parent = astNode.getParent();
            if (parent.getClass() == PropertyGet.class) {
                PropertyGet propertyGet = (PropertyGet) parent;
                AstNode property = propertyGet.getProperty();
                if (property.getClass() == Name.class) {
                    Name name = (Name)property;
                    Scope scope = name.getDefiningScope();
                    if (scope != null && scope.getClass() == AstRoot.class) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    
    private boolean isGlobalWindow(AstNode astNode) {
        if (astNode.getClass() == Name.class) {
            Name name = (Name) astNode;
            if (name.getIdentifier().equals("window")) {
                Scope scope = name.getScope();
                if (scope != null) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isPropertyStringLiteral(AstNode astNode) {
        if (astNode.getClass() == StringLiteral.class) {
            AstNode parentNode = astNode.getParent();
            if (parentNode != null && parentNode.getClass() == ObjectProperty.class) {
                InfixExpression infixExpression = (InfixExpression)parentNode;
                if (infixExpression.getLeft() == astNode) {
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
            public boolean visit(AstNode astNode) {
            if (astNode.getClass() == StringLiteral.class) {
                StringLiteral stringLiteral = (StringLiteral) astNode;
                stringLiteral.setQuoteCharacter('\"');
            }
            if (isPropertyStringLiteral(astNode)) {
                return true;
            }
            if (astNode.getClass() == StringLiteral.class ||
                isGlobalWindow(astNode)) {
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
