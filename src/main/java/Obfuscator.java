package main.java;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.ErrorReporter;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.*;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class Obfuscator {
    private AstRoot astRoot;
    private CompilerEnvirons compilerEnvirons = new CompilerEnvirons();
    private ErrorReporter errorReporter;
    public Obfuscator(Reader in, ErrorReporter errorReporter) throws IOException {
        this.errorReporter = errorReporter;
        compilerEnvirons.setLanguageVersion(Context.VERSION_1_8);
        this.astRoot = new Parser(this.compilerEnvirons, this.errorReporter).parse(in, null, 1);
    }

    private void freshAST() {
        this.astRoot = new Parser(this.compilerEnvirons, this.errorReporter).parse(this.astRoot.toSource(), null, 1);
    }

    private void printAst() {
        this.astRoot.visit(new NodeVisitor() {
                @Override
                public boolean visit(AstNode astNode) {
                    // String indent = "%1$Xs".replace("X", String.valueOf(astNode.depth() + 1));
                    // System.out.format(indent, "").println(astNode.getClass());
                    if (astNode.getClass() == Name.class) {
                        Name name = (Name)astNode;
                        System.out.println(name.getIdentifier());
                        System.out.println(name.isLocalName());
                        System.out.println(((Name) astNode).isLocalName());
                    }
                    // if (astNode.getClass() == FunctionNode.class) {
                    //     ScriptNode sn = (ScriptNode)astNode;
                    //     for (Symbol symbol : sn.getSymbols()) {
                    //         System.out.println(symbol);
                    //     }
                    // }
                    return true;
                }
            });
    }

    private void globalVarToLocalVar() {
        this.freshAST();
        VisitorNullScope visitorNullScope = new VisitorNullScope();
        this.astRoot.visit(visitorNullScope);
        VisitorCreateTopFunction visitorCreateTopFunction = new VisitorCreateTopFunction();
        this.astRoot.visit(visitorCreateTopFunction);
        VisitorGlobalToLocal visitorGlobalToLocal = new VisitorGlobalToLocal(visitorCreateTopFunction.getParamMap(), visitorCreateTopFunction.getThisName());
        this.freshAST();
        this.astRoot.visit(visitorGlobalToLocal);
    }

    private void renameVar() {
        freshAST();
        VisitorSetScope visitorSetScope = new VisitorSetScope();
        this.astRoot.visit(visitorSetScope);
        VisitorRename visitorRename = new VisitorRename(this.astRoot);
        this.astRoot.visit(visitorRename);
    }

    private void changeNumber() {
        freshAST();
        VisitorChangeNumber visitorChangeNumber = new VisitorChangeNumber();
        this.astRoot.visit(visitorChangeNumber);
    }

    private void stringLiteralToGloableVar() {
        freshAST();
        ExpressionStatement expressionStatement = (ExpressionStatement) this.astRoot.getFirstChild();
        FunctionCall functionCall = (FunctionCall) expressionStatement.getExpression();
        ParenthesizedExpression parenthesizedExpression = (ParenthesizedExpression) functionCall.getTarget();
        FunctionNode functionNode = (FunctionNode) parenthesizedExpression.getExpression();
        VisitorStringLiteral visitorStringLiteral = new VisitorStringLiteral(functionCall, functionNode);
        functionNode.visit(visitorStringLiteral);
    }

    private void propertyToElement() {
        this.freshAST();
        VisitorPropertyToElement visitorPropertyToElement = new VisitorPropertyToElement();
        this.astRoot.visit(visitorPropertyToElement);
    }

    private void stringToArray() {
        this.freshAST();
        VisitorStringToArray visitorStringToArray = new VisitorStringToArray();
        this.astRoot.visit(visitorStringToArray);
    }
    
    public void obfuscate() {
        this.printAst();
        this.globalVarToLocalVar();
        this.propertyToElement();
        this.stringLiteralToGloableVar();
        this.stringToArray();
        this.renameVar();
        this.changeNumber(); 
    }

    public void compress(Writer out) throws IOException {
        this.freshAST();
        // boolean isString = false;
        // String source = this.astRoot.toSource();
        // StringBuffer compressedBuffer = new StringBuffer();
        // String[] sources = source.split(" |\n");
        // for (String word : sources) {
        //     String[] split = word.split("\"");
        //     //System.out.println(split.length);
        //     if (!word.isEmpty()) {
        //         compressedBuffer.append(word);
        //         if (!isString &&
        //             split.length % 2 == 0) {
        //             isString = true;
        //         } else if (split.length % 2 == 0) {
        //             isString = false;
        //         }
        //     }
        //     if (isString) {
        //         compressedBuffer.append(" ");
        //     }
        // }
        // out.write(compressedBuffer.toString());
        out.write(this.astRoot.toSource());

    }
}
