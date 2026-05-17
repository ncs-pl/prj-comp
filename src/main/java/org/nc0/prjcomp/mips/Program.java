// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.mips;

import org.nc0.prjcomp.ir.com.Command;
import org.nc0.prjcomp.ir.com.Label;
import org.nc0.prjcomp.support.Errors;
import org.nc0.prjcomp.support.Pair;

import java.util.LinkedList;
import java.util.List;

public class Program {
    public static final Errors errors = new Errors();
    static final int DEFAULT_SIZE = 4;

    static public List<String> generate(Label mainLabel, List<Pair<org.nc0.prjcomp.ir.Frame, List<Command>>> fragments) {
        var assembly = new LinkedList<String>();
        var generator = new Frame(errors);

        // Object file
        assembly.add(Asm.directive("data"));
        assembly.add(Asm.label("buffer"));
        assembly.add(Asm.directive("asciiz \"  \""));
        assembly.add(Asm.directive("text"));
        assembly.add(Asm.label("main"));
        assembly.add(Asm.command("jal " + mainLabel));
        assembly.addAll(Asm.exit());

        // Frames generation
        for (var fragment : fragments) {
            assembly.addAll(generator.generate(fragment));
        }

        // Runtime
        // TODO(nico): use Asm module.
        assembly.add("");
        assembly.add("_print_bool_ENTRY:");
        assembly.add("\tbne $a0, 0, _print_bool_true");
        assembly.add("\tj _print_bool_false");
        assembly.add("_print_bool_true:");
        assembly.add("\tli $a0, 84");
        assembly.add("\tli $v0, 11");
        assembly.add("\tsyscall");
        assembly.add("\tj _print_bool_EXIT");
        assembly.add("_print_bool_false:");
        assembly.add("\tli $a0, 70");
        assembly.add("\tli $v0, 11");
        assembly.add("\tsyscall");
        assembly.add("_print_bool_EXIT:");
        assembly.add("\tj $ra");

        assembly.add("");
        assembly.add("_print_int_ENTRY:");
        assembly.add("\tli $v0, 1");
        assembly.add("\tsyscall");
        assembly.add("_print_int_EXIT:");
        assembly.add("\tj $ra");

        assembly.add("");
        assembly.add("_read_bool_ENTRY:");
        assembly.add("\tla $a0, buffer");
        assembly.add("\tli $a1, 3");
        assembly.add("\tli $v0, 8");
        assembly.add("\tsyscall");
        assembly.add("\tlb $v0, buffer");
        assembly.add("\tli $t1, 84");
        assembly.add("\tseq $v0, $v0, $t1");
        assembly.add("_read_bool_EXIT:");
        assembly.add("\tj $ra");

        assembly.add("");
        assembly.add("_read_int_ENTRY:");
        assembly.add("\tli $v0, 5");
        assembly.add("\tsyscall");
        assembly.add("_read_int_EXIT:");
        assembly.add("\tj $ra");
        return assembly;
    }
}
