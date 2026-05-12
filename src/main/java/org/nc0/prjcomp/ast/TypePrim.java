// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ast;

public class TypePrim extends Type {
    private final Prim prim;

    public TypePrim(Position pos, Prim p) {
        this.prim = p;
        this.position = pos;
    }

    public Prim getPrim() {
        return prim;
    }

    public <T> T accept(Visitor<T> visitor) {
        return visitor.visit(this);
    }

    public String toString() {
        return prim.toString();
    }

    public Type copy() {
        return new TypePrim(position, prim);
    }

    public enum Prim {
        INT {
            public String toString() {
                return "int";
            }
        }, BOOL {
            public String toString() {
                return "bool";
            }
        }, STAT {
            public String toString() {
                return "statement";
            }
        }, IND {
            public String toString() {
                return "indéfini";
            }
        }
    }


}
