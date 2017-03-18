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
    
	/**
	 * 代码混淆器的主要功能类
	 * 
	 * @param in 待混淆的代码
	 * @param errorReporter 记录代码的 error
	 *
	 * @throws IOException
	 */
    public Obfuscator(Reader in, ErrorReporter errorReporter) throws IOException {
        this.errorReporter = errorReporter;
        this.astRoot = new Parser(this.compilerEnvirons, this.errorReporter).parse(in, null, 1);
    }

	/**
	 *
	 * 刷新修改后的语法树
	 */
    private void freshAST() {
        this.astRoot = new Parser(this.compilerEnvirons, this.errorReporter).parse(this.astRoot.toSource(), null, 1);
    }

	/**
	 *
	 * 打印语法树信息
	 */
    private void printAst() {
        this.astRoot.visit(new NodeVisitor() {
                @Override
                public boolean visit(AstNode astNode) {
                    String indent = "%1$Xs".replace("X", String.valueOf(astNode.depth() + 1));
                    System.out.format(indent, "").println(astNode.getClass());
                    if (astNode.getParent() != null) {
                        System.out.println(astNode.getParent().getClass());
                    }
                    return true;
                }
            });
    }

	/**
	 *
	 * 测试各个类是否正常执行
	 */
    private void Test() {
        VisitorSetScope visitorSetScope = new VisitorSetScope();
        this.astRoot.visit(visitorSetScope);
        VisitorGlobalVar visitorGlobalVar = new VisitorGlobalVar();
        this.astRoot.visit(visitorGlobalVar);
        VisitorPropertyToElement visitorPropertyToElement = new VisitorPropertyToElement();
        this.astRoot.visit(visitorPropertyToElement);
        VisitorLiteralToVar visitorLiteralToVar = new VisitorLiteralToVar();
        this.astRoot.visit(visitorLiteralToVar);
        VisitorTopFunction visitorTopFunction = new VisitorTopFunction(visitorLiteralToVar.getParams(), visitorLiteralToVar.getArguments());
        this.astRoot.visit(visitorTopFunction);
        VisitorStringToArray visitorStringToArray = new VisitorStringToArray();
        this.astRoot.visit(visitorStringToArray);
        freshAST();
        this.astRoot.visit(visitorSetScope);
        VisitorLocalVar visitorLocalVar = new VisitorLocalVar();
        this.astRoot.visit(visitorLocalVar);
    }

	/**
	 *
	 * 对外接口，执行混淆操作
	 */
    public void obfuscate() {
        this.printAst();
        // this.globalVarToLocalVar();
        // this.propertyToElement();
        // this.stringLiteralToGloableVar();
        // this.stringToArray();
        // this.renameVar();
        // this.changeNumber();
        this.Test();
    }

	/**
	 *
	 * 对外接口，执行压缩操作。
     * 生成混淆后的代码写到新文件中。
	 * @param out
	 *
	 * @throws IOException
	 */
    public void compress(Writer out) throws IOException {
        // this.freshAST();
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
