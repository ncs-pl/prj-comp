package org.nc0.prjcomp;

import org.antlr.v4.runtime.CharStream;
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
import org.nc0.prjcomp.semantic.SymbolTable;
import org.nc0.prjcomp.semantic.TableBuilder;
import org.nc0.prjcomp.semantic.TypeChecker;
import org.nc0.prjcomp.support.Errors;
import org.nc0.prjcomp.support.Pair;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;


public class Main {

    public static void main(String[] args) throws IOException {

        System.out.println("---- Analyse Syntaxique -----");
        InputStream inputStream = System.in;
        ParseTree tree = parse(inputStream);

        System.out.println("---- Construction AST -----");
        AstBuild astB = new AstBuild();
        Program ast = (Program) tree.accept(astB);

        System.out.println("---- Affichage AST -----");
        AstPrinter printer = new AstPrinter();
        ast.accept(printer);
        System.out.print("\n");


        System.out.println("\n---- Construction Table -----\n");
        TableBuilder tb = new TableBuilder();
        ast.accept(tb);
        SymbolTable st = tb.getTable();
        System.out.println("\n---- Vérif de Types -----\n");
        TypeChecker tc = new TypeChecker(st);
        ast.accept(tc);
        tc.check();

        System.out.println("\n--- Traduction en code intermédiaire ---\n");

        Pair<Label, List<Pair<Frame, List<Command>>>> irCode = Translate.run(st, ast);

        //IrPrinter ip = new IrPrinter();
        //ip.print(irCode);


        System.out.println("\n--- Traduction en assembleur ---\n");

        String name = "out.asm";
        if (args.length == 1) {
            name = args[0];
        }

        Path path = FileSystems.getDefault().getPath(name);
        compile(path, irCode.fst(), irCode.snd());
    }

    private static ParseTree parse(InputStream inputStream) throws IOException {
        CharStream input = CharStreams.fromStream(inputStream);
        sdmLexer lexer = new sdmLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        sdmParser parser = new sdmParser(tokens);
        ParseTree tree = parser.program();
        if (parser.getNumberOfSyntaxErrors() != 0) {
            System.out.println("erreur de syntaxe : sortie après analyse syntaxique");
            System.exit(1);
        }
        return tree;
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
