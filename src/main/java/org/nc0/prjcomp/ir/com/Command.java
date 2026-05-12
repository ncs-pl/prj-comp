// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.ir.com;

abstract public class Command {
    public abstract <T> T accept(Visitor<T> visitor);
}
