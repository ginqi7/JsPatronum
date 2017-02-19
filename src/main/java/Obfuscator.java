package main.java;

import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.ast.*;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

public class Obfuscator {
    private AstRoot astRoot;
    public Obfuscator(Reader in) throws IOException {
        this.astRoot = new Parser(new CompilerEnvirons()).parse(in, null, 1);
    }

    private void freshAST() {
        this.astRoot = new Parser(new CompilerEnvirons()).parse(this.astRoot.toSource(), null, 1);
    }

    private void globalVarToLocalVar() {
        this.freshAST();
        VisitorCreateTopFunction visitorCreateTopFunction = new VisitorCreateTopFunction();
        this.astRoot.visit(visitorCreateTopFunction);
        VisitorGlobalToLocal visitorGlobalToLocal = new VisitorGlobalToLocal(visitorCreateTopFunction.getParamMap());
        this.freshAST();
        this.astRoot.visit(visitorGlobalToLocal);
        //     final Map<String, String> paramMapF = paramMap;
            //     freshAST();
        //     this.astRoot.visit(new NodeVisitor(){
        //                 private List<Scope> scopes = new ArrayList<Scope>();
        //                 public boolean visit(AstNode astNode) {
        //                     if (astNode.getClass() == FunctionNode.class) {
        //                         this.scopes.add((Scope) astNode);
        //                     }
        //                     if (astNode.getClass() == Name.class) {
        //                         Name name = (Name)astNode;
        //                         name.setIdentifier(paramMapF.get(name.getIdentifier()));
        //                         AstNode parent = name.getParent();
        //                         if (parent.getClass() == VariableInitializer.class) {
        //                             VariableInitializer variableInitializer = (VariableInitializer)parent;
        //                             AstNode variableDeclaration = variableInitializer.getParent();
        //                             AstNode top = variableDeclaration.getParent();
        //                             ExpressionStatement expressionStatement = new ExpressionStatement();
        //                             Assignment assignment = new Assignment();
        //                             assignment.setLeft(name);
        //                             assignment.setRight(variableInitializer.getInitializer());
        //                             System.out.println(variableInitializer.getTarget().getClass());
        //                             assignment.setOperator(Token.ASSIGN);
        //                             expressionStatement.setExpression(assignment);
        //                             top.replaceChild(variableDeclaration, expressionStatement);
        //                         }
        //                     }
        //                     return true;
        //                 }
        //         });
    }

    private void renameVar() {
        freshAST();
        VisitorRename visitorRename = new VisitorRename(this.astRoot);
        this.astRoot.visit(visitorRename);
    }
    
    public void obfuscate() {
        // this.astRoot.visit(new NodeVisitor(){
        //         public boolean visit(AstNode astNode) {
        //             System.out.println(astNode.getClass());
        //             return true;
        //         }
        //     });
        //        this.globalVarToLocalVar();
        this.globalVarToLocalVar();
        this.freshAST();
        this.renameVar();
    
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
