// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ir.com;

class LabelAuto extends Label {
    private static int lastLabel = 0;
    final private int label;

    public LabelAuto() {
        this.label = lastLabel;
        lastLabel += 1;
    }

    @Override
    public String toString() {
        return "L" + label;
    }
}

