package main.java;

import org.mozilla.javascript.CompilerEnvirons;
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
        this.astRoot = new Parser(this.compilerEnvirons, this.errorReporter).parse(in, null, 1);
    }

    private void freshAST() {
        this.astRoot = new Parser(this.compilerEnvirons, this.errorReporter).parse(this.astRoot.toSource(), null, 1);
    }

    private void printAst() {
        this.astRoot.visit(new NodeVisitor() {
                @Override
                public boolean visit(AstNode astNode) {
                    System.out.println(astNode.getClass());
                    return true;
                }
                
            });
    }

    private void globalVarToLocalVar() {
        this.freshAST();
        VisitorCreateTopFunction visitorCreateTopFunction = new VisitorCreateTopFunction();
        this.astRoot.visit(visitorCreateTopFunction);
        VisitorGlobalToLocal visitorGlobalToLocal = new VisitorGlobalToLocal(visitorCreateTopFunction.getParamMap());
        this.freshAST();
        this.astRoot.visit(visitorGlobalToLocal);
    }

    private void renameVar() {
        freshAST();
        VisitorRename visitorRename = new VisitorRename(this.astRoot);
        this.astRoot.visit(visitorRename);
    }
    
    public void obfuscate() {
        this.printAst();
        // this.globalVarToLocalVar();
        // this.freshAST();
        // this.renameVar();
    
    }

    enum State {
        BEGIN, S_STR, D_STR, END
    }

    public void compress(Writer out) throws IOException {
        State state = State.BEGIN;
        String source = this.astRoot.toSource();
        StringBuffer compressedBuffer = new StringBuffer();
        String[] sources = source.split(" |\n");
        //        out.write(astRoot.toSource());
		out.write(compressedBuffer.toString());
    }
}
