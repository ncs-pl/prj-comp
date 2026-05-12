// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.support;

public record Pair<U, V>(U fst, V snd) {
    @Override
    public boolean equals(Object o) {
        System.out.println("eq");
        if (o instanceof Pair(Object fst1, Object snd1)) {
            return this.fst.equals(fst1) && this.snd.equals(snd1);
        }
        return false;
    }
}
