package org.nc0.prjcomp.semantic;

import org.nc0.prjcomp.ast.Formal;
import org.nc0.prjcomp.ast.MethodDecl;
import org.nc0.prjcomp.ast.Type;

import java.util.List;

public record MethodSig(List<Formal> params, Type returnType) {
    public static MethodSig signatureOf(MethodDecl md) {
        return new MethodSig(md.getFormal(), md.getType());
    }
}

