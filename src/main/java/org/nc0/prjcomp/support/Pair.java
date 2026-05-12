package org.nc0.prjcomp.support;

public record Pair<U, V>(U fst, V snd) {

  @Override
  public boolean equals(Object o) {
    System.out.println("eq");
    if (o instanceof Pair p) {
      return this.fst.equals(p.fst()) && this.snd.equals(p.snd());
    }
    return false;
  }
}
