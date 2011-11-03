package org.rascalmpl.library.vis.graphics;

import org.eclipse.swt.SWT;

public enum FontStyle {

	NORMAL(SWT.NORMAL), BOLD(SWT.BOLD), ITALIC(SWT.ITALIC);
	
	FontStyle(int swtConstant){
		this.swtConstant = swtConstant;
	}
	
	public int swtConstant;
	
	public static FontStyle[] getFontStyles(boolean bold, boolean italic){
		if(bold && italic){
			FontStyle[] s = {BOLD,ITALIC};
			return s;
		} else if(bold){
			FontStyle[] s = {BOLD};
			return s;
		} else if(italic){
			FontStyle[] s = {ITALIC};
			return s;
		} else {
			FontStyle[] s = {NORMAL};
			return s;
		}
	}
	
	
	public static int toStyleMask(FontStyle... styles){
		int styleMask = 0;
		for(FontStyle style : styles){
			styleMask = styleMask | style.swtConstant; 
		}
		return styleMask;
	}
}
