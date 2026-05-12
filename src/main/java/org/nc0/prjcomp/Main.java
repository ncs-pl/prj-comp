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
import org.nc0.prjcomp.printers.AstPrinter;
import org.nc0.prjcomp.printers.IrPrinter;
import org.nc0.prjcomp.semantic.SymbolTable;
import org.nc0.prjcomp.semantic.TableBuilder;
import org.nc0.prjcomp.semantic.TypeChecker;
import org.nc0.prjcomp.support.Errors;
import org.nc0.prjcomp.support.Pair;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException {
        // 1. Read input parameters
        var input = CharStreams.fromStream(System.in);
        // TODO(nico): ensure there are some parameters

        // 2. Lexing
        var lexer = new sdmLexer(input);
        var tokens = new CommonTokenStream(lexer);

        // 3. Parsing
        var parser = new sdmParser(tokens);
        ParseTree tree = parser.program();
        if (parser.getNumberOfSyntaxErrors() != 0) {
            System.out.println("erreur de syntaxe : sortie après analyse syntaxique");
            System.exit(1);
        }

        var astBuilder = new AstBuild();
        Program ast = (Program) tree.accept(astBuilder);

        var astPrinter = new AstPrinter();
        ast.accept(astPrinter);
        System.out.print("\n");

        // 4. Symbol table resolution
        var tableBuilder = new TableBuilder();
        ast.accept(tableBuilder);
        SymbolTable symbols = tableBuilder.getTable();

        // 5. Type checking
        var typeChecker = new TypeChecker(symbols);
        ast.accept(typeChecker);
        typeChecker.check();

        // 6. IR lowering
        Pair<Label, List<Pair<Frame, List<Command>>>> ir = Translate.run(symbols, ast);
        Label main = ir.fst();
        List<Pair<Frame, List<Command>>> fragments = ir.snd();
        var irPrinter = new IrPrinter();
        irPrinter.print(ir);

        // 7. Assembly code generation
        String name = "out.asm";
        if (args.length == 1) {
            name = args[0];
        }
        Path path = FileSystems.getDefault().getPath(name);
        compile(path, main, fragments);
    }

    public static void compile(Path path, Label mainLabel, List<Pair<Frame, List<Command>>> fragments) {
        Path newPath = FileSystems.getDefault().getPath(changeExtension(path, ".sdm", ".asm").getFileName().toString());
        org.nc0.prjcomp.mips.Program.generate(newPath, mainLabel, fragments);
        Errors errs = org.nc0.prjcomp.mips.Program.errors;
        if (errs.hasErrors()) {
            System.out.println("Erreur génération MIPS");
            errs.print();
            System.exit(1);
        }
    }

    private static Path changeExtension(Path path, String oldExt, String newExt) {
        PathMatcher pm = FileSystems.getDefault().getPathMatcher("glob:*" + oldExt);
        if (pm.matches(path.getFileName())) {
            String nameWithExtension = path.getFileName().toString();
            int endIndex = nameWithExtension.length() - oldExt.length();
            String name = nameWithExtension.substring(0, endIndex);
            if (path.getParent() != null)
                return path.getParent().resolve(name + newExt);
            else
                return FileSystems.getDefault().getPath(name + newExt);
        }
        return path;
    }
}
