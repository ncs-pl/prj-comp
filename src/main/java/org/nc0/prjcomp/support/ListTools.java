// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.support;

import java.util.ArrayList;
import java.util.List;

public class ListTools {
    public static <T> List<T> mklist(T t) {
        List<T> list = new ArrayList<>();
        list.add(t);
        return list;
    }

    public static <T> List<T> mklist(T t1, T t2) {
        List<T> list = new ArrayList<>();
        list.add(t1);
        list.add(t2);
        return list;
    }
}
