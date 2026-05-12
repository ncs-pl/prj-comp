package org.nc0.prjcomp.printers;

import org.nc0.prjcomp.ir.Frame;
import org.nc0.prjcomp.ir.com.Command;
import org.nc0.prjcomp.ir.com.Label;
import org.nc0.prjcomp.support.Pair;

import java.util.List;

public class IrPrinter {
    public void print(Pair<Label, List<Pair<Frame, List<Command>>>> code) {
        Label label = code.fst();
        List<Pair<Frame, List<Command>>> list = code.snd();
        System.out.println("Label " + label.toString());
        for (Pair<Frame, List<Command>> l : list) {
            System.out.println(l.fst().toString());
            for (Command c : l.snd()) {
                System.out.println(c);
            }
        }
    }

}
