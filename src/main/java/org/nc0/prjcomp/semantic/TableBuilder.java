// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.semantic;

import org.nc0.prjcomp.ast.*;
import org.nc0.prjcomp.support.Errors;

import java.util.List;

public class TableBuilder extends BaseVisitor<Void> {
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
    public Void visit(MethodDecl declaration) {
        Block body = declaration.getBlock();
        List<Formal> parameters = declaration.getFormal();
        String id = declaration.getId().getName();
        String type = declaration.getType().toString();
        MethodSig signature = MethodSig.signatureOf(declaration);
        symbolTable.addMethod(id, signature);
        symbolTable.localTable(body);
        visitedBlocks.enter(body);
        parameters.forEach(f -> symbolTable.addLocalVariable(visitedBlocks.current(), f.getId().getName(), f.getType()));
        super.visit(body);
        visitedBlocks.exit();
        return null;
    }

    @Override
    public Void visit(StatVarDecl declaration) {
        String id = declaration.getId().getName();
        Type type = declaration.getType();
        Type t = symbolTable.variableLookup(id, visitedBlocks);

        if (t != null) {
            errors.add(declaration, " : variable " + id + " déjà déclarée en " + t.getPosition());
        }
        //Si on est dans un bloc :
        if (visitedBlocks.getStack().isEmpty()) {
            errors.add(declaration, "erreur : pile des blocs vide");
        }
        symbolTable.addLocalVariable(visitedBlocks.current(), id, type);

        return null;
    }

    @Override
    public Void visit(Block block) {
        // enregistrer le bloc dans la table :
        symbolTable.localTable(block);
        visitedBlocks.enter(block);
        super.visit(block);
        visitedBlocks.exit();
        return null;
    }
}
