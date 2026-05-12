// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ast;

public enum UnOp {
    NOT {
        public String toString() {
            return "!";
        }
    }, MIN {
        public String toString() {
            return "-";
        }
    }
}
