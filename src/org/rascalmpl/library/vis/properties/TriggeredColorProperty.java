package org.rascalmpl.library.vis.properties;

import org.eclipse.imp.pdb.facts.IInteger;
import org.rascalmpl.library.vis.FigureLibrary;
import org.rascalmpl.library.vis.FigurePApplet;

public class TriggeredColorProperty extends TriggeredIntegerProperty  implements IColorPropertyValue  {

	public TriggeredColorProperty(Property prop, String tname, FigurePApplet fpa){
		super(prop,tname,fpa);
	}
	
	public Property getProperty(){
		return property;
	}
	
	public int getValue() {
		String s = fpa.getStrTrigger(tname);
		
		IInteger ival = FigureLibrary.colorNames.get(s);
		if(ival != null){
			return ival.intValue();
		}
		try {
			fpa.setStrTrigger(tname, s);
		} catch (NumberFormatException e){
			
		}
		return 0;
	}

}
