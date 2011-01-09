package org.rascalmpl.library.vis.interaction;


import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.FigureFactory;
import org.rascalmpl.library.vis.FigurePApplet;
import org.rascalmpl.library.vis.Text;
import org.rascalmpl.library.vis.properties.ConstantStringProperty;
import org.rascalmpl.library.vis.properties.IPropertyManager;
import org.rascalmpl.library.vis.properties.IStringPropertyValue;

public class SelectFigure extends Figure {
	
	private String tname;
	Figure figure = null;
	Figure noSelectionPossible = null;
	
	private HashMap<String,Figure> figChoices = new HashMap<String,Figure> ();

	public SelectFigure(FigurePApplet fpa, IPropertyManager properties, IString tname, IMap choices, IEvaluatorContext ctx) {
		super(fpa, properties, ctx);
		this.tname = tname.getValue();
		
		IStringPropertyValue text = new ConstantStringProperty(null, "NO SELECTION POSSIBLE");
		noSelectionPossible = new Text(fpa, properties, text, ctx);
		Iterator<Entry<IValue,IValue>> iter =  choices.entryIterator();
		while(iter.hasNext()){
			Entry<IValue,IValue> e = iter.next();
			String key = ((IString)e.getKey()).getValue();
			Figure val = FigureFactory.make(fpa, (IConstructor)e.getValue(), properties, ctx);
			figChoices.put(key, val);
		}
	}

	@Override
	public void bbox() {
		String s = fpa.getStrTrigger(tname);
		figure = figChoices.get(s);
		if(figure == null){
			figure = noSelectionPossible;
		}
		figure.bbox();
		width = figure.width;
		height = figure.width;
	}

	@Override
	public void draw(float left, float top) {
		figure.draw(left,top);
	}
	
	@Override
	public boolean mouseInside(int mouseX, int mouseY){
		if(figure != null)
			return figure.mouseInside(mouseX, mouseY);
		return false;
	}
	
	@Override
	public boolean mouseInside(int mouseX, int mouseY, float centerX, float centerY){
		if(figure != null)
			return figure.mouseInside(mouseX, mouseY, centerX, centerY);
		return false;
	}
	
	@Override
	public boolean mouseOver(int mouseX, int mouseY, boolean mouseInParent){
		if(figure != null)
			return figure.mouseOver(mouseX, mouseY, mouseInParent);
		return false;
	}
	
	@Override
	public boolean mouseOver(int mouseX, int mouseY, float centerX, float centerY, boolean mouseInParent){
		if(figure != null)
			return figure.mouseOver(mouseX, mouseY, centerX, centerY, mouseInParent);
		return false;
	}
	
	@Override
	public boolean mousePressed(int mouseX, int mouseY, MouseEvent e){
		if(figure != null)
			return figure.mousePressed(mouseX, mouseY, e);
		return false;
	}
	
	@Override
	public boolean keyPressed(int key, int keyCode){
		if(figure != null)
			return figure.keyPressed(key, keyCode);
		return false;
	}

}
