package org.rascalmpl.library.vis.graphics;

import org.eclipse.swt.SWT;

public enum FontStyle {

	NORMAL(SWT.NORMAL), BOLD(SWT.BOLD), ITALIC(SWT.BOLD);
	
	FontStyle(int swtConstant){
		this.swtConstant = swtConstant;
	}
	
	public int swtConstant;
	
	public static int toStyleMask(FontStyle... styles){
		int styleMask = 0;
		for(FontStyle style : styles){
			styleMask = styleMask | style.swtConstant; 
		}
		return styleMask;
	}
}
