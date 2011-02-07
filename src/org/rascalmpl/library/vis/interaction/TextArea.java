package org.rascalmpl.library.vis.interaction;

import java.awt.Graphics;

import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

import processing.core.PApplet;

public class TextArea extends Figure {
	
	final JEditorPane area;
	boolean added = false;

	public TextArea(FigurePApplet fpa, IPropertyManager properties, IList lines, IMap colored) {
		super(fpa, properties);
		StringBuffer text = new StringBuffer();
		for(IValue iline : lines){
			text.append(((IString) iline).getValue()).append("\n");
		}
		
		//area = new java.awr.textarea(text.toString(), 10, 50, java.awt.TextArea.SCROLLBARS_BOTH);
		
		area = new javax.swing.JEditorPane();
		area.setText(text.toString());
		fpa.add(area);
	}

	@Override
	public void bbox() {
		width = area.getWidth();
		height = area.getHeight();
	}

	@Override
	public void draw(float left, float top) {
		System.err.println("DRAW TEXTAREA: " + left + ", " + top);
		this.setLeft(left);
		this.setTop(top);
		//area.setForeground(new Color(getFontColorProperty()));
		area.setLocation(PApplet.round(left), PApplet.round(top));
		area.validate();
	}
	
	@Override
	public void destroy(){
		fpa.remove(area);
		fpa.invalidate();
		fpa.setComputedValueChanged();
	}
	
}
