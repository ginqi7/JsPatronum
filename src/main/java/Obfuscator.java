package main.java;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.Token;
import org.mozilla.javascript.ast.*;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

import java.util.*;

public class Obfuscator {
    private AstRoot astRoot;
    public Obfuscator(Reader in) throws IOException {
        this.astRoot = new Parser(new CompilerEnvirons()).parse(in, null, 1);
    }

    private void freshAST() {
        this.astRoot = new Parser(new CompilerEnvirons()).parse(this.astRoot.toSource(), null, 1);
    }
    
    private List<AstNode> createParams(List<Symbol> symbols, Map<String, String> paramMap) {
        List<AstNode> params = new ArrayList<AstNode>();
        int index = 0;
        for (Symbol symbol : symbols) {
            Name name = new Name();
            String nameStr = "_" + index;
            name.setIdentifier(nameStr);
            params.add(name);
            paramMap.put(symbol.getName(), nameStr);
            index++;
        }
        return params;
    }
    
    private List<AstNode> createArguments(List<Symbol> symbols) {
        List<AstNode> arguments = new ArrayList<AstNode>();
        for (Symbol symbol : symbols) {
            StringLiteral stringLiteral = new StringLiteral();
            stringLiteral.setValue(symbol.getName());
            stringLiteral.setQuoteCharacter('"');
            arguments.add(stringLiteral);
        }
        return arguments;
        // class org.mozilla.javascript.ast.AstRoot
        //     class org.mozilla.javascript.ast.ExpressionStatement
        //     class org.mozilla.javascript.ast.FunctionCall
        //     class org.mozilla.javascript.ast.ParenthesizedExpression
        //     class org.mozilla.javascript.ast.FunctionNode
        //     class org.mozilla.javascript.ast.Name
        //     class org.mozilla.javascript.ast.Block
        //     class org.mozilla.javascript.ast.StringLiteral
    }

    private void globalVarToLocalVar() {
        ScriptNode scriptNode = (ScriptNode) this.astRoot;
        final Map<String, Symbol> symbolMap = ((Scope)this.astRoot).getSymbolTable();
        List<Symbol> symbols = scriptNode.getSymbols();
        Map<String, String> paramMap = new HashMap<String, String>();

        ExpressionStatement expressionStatement = new ExpressionStatement();
        FunctionCall functionCall = new FunctionCall();
        List<AstNode> arguments = createArguments(symbols);
        ParenthesizedExpression parenthesizedExpression = new ParenthesizedExpression();
        FunctionNode functionNode = new FunctionNode();
        List<AstNode> params = createParams(symbols, paramMap);
        Block block = new Block();
        List<AstNode> statements = scriptNode.getStatements();
        for (AstNode statement : statements) {
            block.addChild(statement);
        }
        this.astRoot.removeChildren();
        functionNode.setParams(params);
        functionNode.setBody(block);
        parenthesizedExpression.setExpression(functionNode);
        functionCall.setTarget(parenthesizedExpression);
        functionCall.setArguments(arguments); 
        expressionStatement.setExpression(functionCall);
        this.astRoot.addChild(expressionStatement);

        final Map<String, String> paramMapF = paramMap;
        freshAST();
        this.astRoot.visit(new NodeVisitor(){
                    private List<Scope> scopes = new ArrayList<Scope>();
                    public boolean visit(AstNode astNode) {
                        if (astNode.getClass() == FunctionNode.class) {
                            this.scopes.add((Scope) astNode);
                        }
                        if (astNode.getClass() == Name.class &&
                            // ((Name)astNode).getDefiningScope() == scopes.get(0) &&
                            symbolMap.get(((Name)astNode).getIdentifier()) != null
                            ) {
                            Name name = (Name)astNode;
                            name.setIdentifier(paramMapF.get(name.getIdentifier()));
                            AstNode parent = name.getParent();
                            if (parent.getClass() == VariableInitializer.class) {
                                VariableInitializer variableInitializer = (VariableInitializer)parent;
                                AstNode variableDeclaration = variableInitializer.getParent();
                                AstNode top = variableDeclaration.getParent();
                                ExpressionStatement expressionStatement = new ExpressionStatement();
                                Assignment assignment = new Assignment();
                                assignment.setLeft(name);
                                assignment.setRight(variableInitializer.getInitializer());
                                System.out.println(variableInitializer.getTarget().getClass());
                                assignment.setOperator(Token.ASSIGN);
                                expressionStatement.setExpression(assignment);
                                top.replaceChild(variableDeclaration, expressionStatement);
                            }
                        }
                        return true;
                    }
            });
    }

    private void getRandomName(int nums) {
        int len = 
            }

    private void renameVar() {
        freshAST();
        this.astRoot.visit(new NodeVisitor() {
                private List<Scope> scopes = new ArrayList<Scope>();
                public boolean visit(AstNode astNode) {
                    if (astNode instanceof Scope) {
                        scopes.add((Scope)astNode);
                    } else if (astNode.getClass() == Name.class) {
                        Name name = (Name)astNode;
                    }
                    return true;
                }
            });
    }
    
    public void obfuscate() {
        // this.astRoot.visit(new NodeVisitor(){
        //         public boolean visit(AstNode astNode) {
        //             System.out.println(astNode.getClass());
        //             return true;
        //         }
        //     });
        this.globalVarToLocalVar();
    }

    public void compress(Writer out) throws IOException {
        out.write(this.astRoot.toSource());
   
        // String source = astRoot.toSource();
        // StringBuffer compressedSource = new StringBuffer();
        // int position = 0;
        // while (position < source.length()) {
        //     char currentChar = source.charAt(position);
        //     if (currentChar == '\n') {
        //         position++;
        //         while (position < source.length() &&
        //                source.charAt(position) == ' ') {
        //             position++;
        //         }
        //     }
        //     else if (currentChar == ' ' &&
        //              (Build_in.notNameSymbol.contains(source.charAt(position+1)) ||
        //               source.charAt(position+1) == ' ')) {
        //         position++;
        //     }
        //     else if (Build_in.notNameSymbol.contains(currentChar) && source.charAt(position+1) == ' ') {
        //         compressedSource.append(currentChar);
        //         position += 2;
        //     }
        //     else if (currentChar == '\t') {
        //         position++;
        //     } else {
        //         compressedSource.append(currentChar);
        //         position++;
        //     }
        // }
        // try {
        //     //out.write(astRoot.toSource());
        //     out.write(compressedSource.toString());
        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
       
    }
}
