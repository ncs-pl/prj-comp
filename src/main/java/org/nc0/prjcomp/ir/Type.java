// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ir;

public enum Type {
    BYTE {
        @Override
        public String toString() {
            return "byte";
        }
    }, INT {
        @Override
        public String toString() {
            return "int";
        }
    }
}
