// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.nc0.prjcomp.ast.AstBuild;
import org.nc0.prjcomp.ast.Program;
import org.nc0.prjcomp.ir.Frame;
import org.nc0.prjcomp.ir.com.Command;
import org.nc0.prjcomp.ir.com.Label;
import org.nc0.prjcomp.ir.translation.Translate;
import org.nc0.prjcomp.parser.sdmLexer;
import org.nc0.prjcomp.parser.sdmParser;
import org.nc0.prjcomp.printers.IrPrinter;
import org.nc0.prjcomp.semantic.SymbolTable;
import org.nc0.prjcomp.semantic.TableBuilder;
import org.nc0.prjcomp.semantic.TypeChecker;
import org.nc0.prjcomp.support.Errors;
import org.nc0.prjcomp.support.Pair;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        // 1. Read input parameters
        var input = CharStreams.fromStream(System.in);
        // TODO(nico): make it read a file path and open the file instead!

        // 2. Lexing
        // System.out.println("--- Lexing ---");
        var lexer = new sdmLexer(input);
        var tokens = new CommonTokenStream(lexer);

        // 3. Parsing
        // System.out.println("--- Parsing ---");
        var parser = new sdmParser(tokens);
        ParseTree tree = parser.program();
        if (parser.getNumberOfSyntaxErrors() != 0) {
            System.out.println("erreur de syntaxe : sortie après analyse syntaxique");
            System.exit(1);
        }

        var ast = (Program) tree.accept(new AstBuild());
        // ast.accept(new AstPrinter());
        // System.out.print("\n");

        // 4. Symbol table resolution
        // System.out.println("--- Symbol resolution ---");
        var tableBuilder = new TableBuilder();
        ast.accept(tableBuilder);
        SymbolTable symbols = tableBuilder.getTable();

        // 5. Type checking
        // System.out.println("--- Type checking ---");
        var typeChecker = new TypeChecker(symbols);
        ast.accept(typeChecker);
        typeChecker.check();

        // 6. IR lowering
        System.out.println("--- Lowering ---");
        Pair<Label, List<Pair<Frame, List<Command>>>> ir = Translate.run(symbols, ast);
        Label main = ir.fst();
        List<Pair<Frame, List<Command>>> fragments = ir.snd();
        var irPrinter = new IrPrinter();
        irPrinter.print(ir);

        // 7. Assembly code generation
        System.out.println("--- Code generating ---");
        List<String> assembly = org.nc0.prjcomp.mips.Program.generate(main, fragments);
        Errors errs = org.nc0.prjcomp.mips.Program.errors;
        if (errs.hasErrors()) {
            System.out.println("Errors while generating assembly:");
            errs.print();
            System.exit(1);
        }
        System.out.println(String.join("\n", assembly));
    }
}
