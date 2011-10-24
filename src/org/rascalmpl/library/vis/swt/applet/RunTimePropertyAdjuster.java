package org.rascalmpl.library.vis.swt.applet;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.rascalmpl.library.vis.properties.IRunTimePropertyChanges;
import org.rascalmpl.library.vis.properties.Properties;

public class RunTimePropertyAdjuster implements IRunTimePropertyChanges, IFigureChangedListener{

	private int fontSizeOffset;
	private double lineWidthOffset;
	private FigureSWTApplet parent;
	
	public RunTimePropertyAdjuster(FigureSWTApplet parent) {
		fontSizeOffset = 0;
		lineWidthOffset = 0;
		this.parent = parent;
	}
	
	@Override
	public Object adoptPropertyVal(Properties prop, Object val) {
		switch(prop){
		case FONT_SIZE :  return Math.max(1,((Integer)val) + fontSizeOffset); 
		case LINE_WIDTH : 	if((Double)val == 0){ 
			return 0;
		} else {
			return Math.max(0,((Double)val) + lineWidthOffset); 
		}
		default : return val;
		}
	}
	
	public boolean handleKeyPress(KeyEvent e){
		
		if((e.stateMask & SWT.CONTROL) != 0){
			if(e.keyCode == '='){
				fontSizeOffset+=1;
				parent.getExectutionEnv().fakeRascalCallBack();
				return true;
			} else if(e.keyCode == '-'){
				fontSizeOffset-=1;
				parent.getExectutionEnv().fakeRascalCallBack();
				return true;
			}
		} else if((e.stateMask & SWT.SHIFT) != 0){
			if(e.keyCode == '='){
				lineWidthOffset+=0.5;
				parent.getExectutionEnv().fakeRascalCallBack();
				return true;
			} else if(e.keyCode == '-'){
				lineWidthOffset-=0.5;
				parent.getExectutionEnv().fakeRascalCallBack();
				return true;
			}
		} 
		return false;
	}

	@Override
	public void notifyFigureChanged() {
	}

	public void dispose() {
	}

	
}
