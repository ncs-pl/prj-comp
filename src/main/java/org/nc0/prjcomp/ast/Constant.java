// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ast;

public enum Constant {
    TRUE {
        public String toString() {
            return "true";
        }
    }, FALSE {
        public String toString() {
            return "false";
        }
    }
}
