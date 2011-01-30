package org.rascalmpl.library.vis.interaction;

import java.awt.Color;
import java.awt.Dimension;

import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.properties.IPropertyManager;

import processing.core.PApplet;

public class TextArea extends Figure {
	
	final java.awt.TextArea area;

	public TextArea(FigurePApplet fpa, IPropertyManager properties, IList lines, IMap colored, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		StringBuffer text = new StringBuffer();
		for(IValue iline : lines){
			text.append(((IString) iline).getValue()).append("\n");
		}
		
		area = new java.awt.TextArea(text.toString(), 50, 50, java.awt.TextArea.SCROLLBARS_VERTICAL_ONLY);
		area.setEditable(false);
		
//	    area.addKeyListener(
//	    		  new KeyListener(){
//	    			  public void keyTyped(KeyEvent e){ 
//	    			  }
//
//					@Override
//					public void keyPressed(KeyEvent e) {
//					}
//
//					@Override
//					public void keyReleased(KeyEvent e) {
//					}
//	    		  });
		
//		area.addMouseListener(
//				 new MouseAdapter(){
//					 public void mousePressed(){}
//					 public void mouseReleased(){}
//					 public void mouseClicked(){}
//					 public void mouseEntered(MouseEvent e) {}
//					 public void mouseExited(MouseEvent e) {}
//				 }
//		);
		
	    fpa.add(area);
	}

	@Override
	public void bbox() {
		width = getWidthProperty();
		height = getHeightProperty();
		area.setSize(PApplet.round(width), PApplet.round(height));
		area.setPreferredSize(new Dimension(PApplet.round(width), PApplet.round(height)));
	}

	@Override
	public void draw(float left, float top) {
		this.setLeft(left);
		this.setTop(top);
		area.setForeground(new Color(getFontColorProperty()));
		area.setLocation(PApplet.round(left), PApplet.round(top));
	}

}
