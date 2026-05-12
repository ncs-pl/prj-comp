// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ast;

public enum BinOp {
    ADD {
        public String toString() {
            return "+";
        }
    }, MIN {
        public String toString() {
            return "-";
        }
    }, LT {
        public String toString() {
            return "<";
        }
    }, LEQ {
        public String toString() {
            return "<=";
        }
    }, GT {
        public String toString() {
            return ">";
        }
    }, GEQ {
        public String toString() {
            return ">=";
        }
    }, MULT {
        public String toString() {
            return "*";
        }
    }, DIV {
        public String toString() {
            return "/";
        }
    }, AND {
        public String toString() {
            return "&&";
        }
    }, OR {
        public String toString() {
            return "||";
        }
    }, EQ {
        public String toString() {
            return "==";
        }
    }, NEQ {
        public String toString() {
            return "!=";
        }
    }
}
