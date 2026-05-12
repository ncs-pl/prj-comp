// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.semantic;

import org.nc0.prjcomp.ast.Formal;
import org.nc0.prjcomp.ast.TypePrim;
import org.nc0.prjcomp.ast.TypePrim.Prim;

import java.util.ArrayList;

public class Signatures {
    public static final MethodSig printInt = buildUnaryVoid(Prim.INT);
    public static final MethodSig unaryBoolRTVoid = buildUnaryVoid(Prim.BOOL);
    //int à remplacer par string
    public static final MethodSig inputMethodSig = new MethodSig(new ArrayList<>(), new TypePrim(null, Prim.INT));

    public static MethodSig buildUnaryVoid(Prim tp) {
        var type = new TypePrim(null, tp);
        var formals = new ArrayList<Formal>();
        formals.add(new Formal(null, type, null));
        return new MethodSig(formals, null);
    }

    public static void addPredefinedSignature(SymbolTable symbolTable) {
        symbolTable.addMethod("print", printInt);
        symbolTable.addMethod("print", unaryBoolRTVoid);
        symbolTable.addMethod("input", inputMethodSig);
    }
}
