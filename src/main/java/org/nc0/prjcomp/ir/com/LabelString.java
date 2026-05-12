// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ir.com;

class LabelString extends Label {
    final private String label;

    public LabelString(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return label;
    }
}

