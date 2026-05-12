package org.nc0.prjcomp.support;

import org.nc0.prjcomp.ast.Block;
import org.nc0.prjcomp.ir.Register;

import java.util.Map;

public class Maps {
    public static void print(Map<Block, Map<String, Register>> map) {
        System.out.println("--printMap--");
        for (Map.Entry en : map.entrySet()) {
            Object b = en.getKey();
            Map<String, Register> m = map.get(b);
            System.out.println("Bloc : " + b);
            for (Map.Entry e : m.entrySet()) {
                System.out.println("\t" + e.getKey() + " -> " + e.getValue());
            }
        }
        System.out.println("--printMap[sortie]--");
    }

}
