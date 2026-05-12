// Copyright (c) 2026.  All rights reserved.

package org.nc0.prjcomp.support;

import org.nc0.prjcomp.ast.Node;

import java.util.LinkedList;
import java.util.List;

public class Errors {
    private List<String> errors;

    public Errors() {
        errors = new LinkedList<>();
    }

    public void add(Node n, String e) {
        if (n == null) {
            errors.add(e);
        } else {
            errors.add(n.getPosition() + " : " + e);
        }
    }

    public void add(String e) {
        errors.add(e);
    }

    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    public void print() {
        for (String s : errors) {
            System.out.println(s);
        }
    }

    public void reset() {
        errors = new LinkedList<>();
    }

    public void merge(Errors e) {
        errors.addAll(e.get());
    }

    public List<String> get() {
        return errors;
    }
}

