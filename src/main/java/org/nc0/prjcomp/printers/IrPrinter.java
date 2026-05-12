package org.nc0.prjcomp.printers;
import java.util.List;

import org.nc0.prjcomp.ir.com.*;
import org.nc0.prjcomp.support.Pair;
import org.nc0.prjcomp.ir.Frame;

public class IrPrinter{
	public void print(Pair<Label,List<Pair<Frame,List<Command>>>> code ){
		Label label=code.fst();
		List<Pair<Frame,List<Command>>> list = code.snd();
		System.out.println("Label "+label.toString());
		for(Pair<Frame,List<Command>> l : list){
			System.out.println(l.fst().toString());
			for(Command c : l.snd()){
				System.out.println(c);
			}
		}
	}

}
