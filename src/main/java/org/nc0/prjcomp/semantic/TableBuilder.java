package org.nc0.prjcomp.semantic;

import org.nc0.prjcomp.ast.*;
import org.nc0.prjcomp.support.Errors;

import java.util.List;

public class TableBuilder extends org.nc0.prjcomp.ast.BaseVisitor<Void> {
    //Todo : tester si les mots ne sont pas réservés.

    private final SymbolTable symbolTable;
    private final VisitedBlocks visitedBlocks;
    private final Errors errors;

    public TableBuilder() {
        super(null);
        errors = new Errors();
        visitedBlocks = new VisitedBlocks();
        symbolTable = new SymbolTable();
    }

    public SymbolTable getTable() {
        if (symbolTable.getErrors().hasErrors()) {
            System.out.println("erreurs dans table");
            symbolTable.getErrors().print();
            System.out.println("Sortie après construction de la table des symboles.");
            System.exit(1);
        }
        if (errors.hasErrors()) {
            System.out.println("erreurs dans construction table");
            errors.print();
            System.out.println("Sortie après construction de la table des symboles.");
            System.exit(1);
        }
        return symbolTable;
    }

    @Override
    public Void visit(StatVarDecl vd) {
        String id = vd.getId().getName();
        Type type = vd.getType();
        Type t = symbolTable.variableLookup(id, visitedBlocks);

        if (t != null) {
            errors.add(vd, " : variable " + id + " déjà déclarée en " + t.getPosition());
        }
        //Si on est dans un bloc :
        if (visitedBlocks.getStack().isEmpty()) {
            errors.add(vd, "erreur : pile des blocs vide");
        }
        symbolTable.addLocalVariable(visitedBlocks.current(), id, type);

        return null;
    }

    @Override
    public Void visit(MethodDecl md) {
        Block b = md.getBlock();
        List<Formal> lf = md.getFormal();
        String id = md.getId().getName();
        String name = md.getType().toString();

        MethodSig ms = MethodSig.signatureOf(md);
        symbolTable.addMethod(id, ms);

        symbolTable.localTable(b);

        visitedBlocks.enter(b);

        for (Formal f : lf) {
            symbolTable.addLocalVariable(visitedBlocks.current(), f.getId().getName(), f.getType());
        }
        super.visit(b);
        visitedBlocks.exit();
        return null;
    }

    @Override
    public Void visit(Block b) {
        //enregistrer le bloc dans la table :
        symbolTable.localTable(b);

        visitedBlocks.enter(b);
        super.visit(b);
        visitedBlocks.exit();
        return null;
    }
}
