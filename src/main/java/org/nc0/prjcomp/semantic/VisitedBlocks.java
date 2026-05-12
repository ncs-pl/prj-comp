// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.semantic;

import org.nc0.prjcomp.ast.Block;

import java.util.Stack;

public class VisitedBlocks {
    private final Stack<Block> blocks;

    public VisitedBlocks() {
        this.blocks = new Stack<>();
    }

    public Stack<Block> getStack() {
        return blocks;
    }

    public void enter(Block b) {
        blocks.push(b);
    }

    public void exit() {
        blocks.pop();
    }

    public Block current() {
        return blocks.peek();
    }
}
