package org.rascalmpl.library.vis.swt;

import java.util.Stack;
import java.util.Vector;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.interaction.SWTWidgetFigure;
import org.rascalmpl.library.vis.util.Rectangle;

public class SWTZOrderManager implements ISWTZOrdering {
	
	Composite floor;
	Vector<Control> newElements;
	Vector<Control> currentZOrder;
	Stack<Integer> overlapIndexes;
	Vector<Rectangle> curOverlaps;
	
	public SWTZOrderManager(Composite floor) {
		currentZOrder = new Vector<Control>();
		newElements = new Vector<Control>();
		overlapIndexes = new Stack<Integer>();
		this.floor = floor;
	}

	public void start(){
		currentZOrder.clear();
		newElements.clear();
		overlapIndexes.clear();
	}
	
	@Override
	public void pushOverlap() {
		if(!newElements.isEmpty()){
			overlapIndexes.push(currentZOrder.size());
			currentZOrder.addAll(newElements);
			newElements.clear();
		}
	}

	@Override
	public void popOverlap() {
		for(int i = 0 ; i < currentZOrder.size() - overlapIndexes.peek(); i++){
			newElements.remove(overlapIndexes.peek());
		}
		overlapIndexes.pop();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void register(Figure fig) {
		if(overlapIndexes.isEmpty()){
			return;
		}
		if(fig instanceof SWTWidgetFigure){
			for(Control c : currentZOrder){
				Control n = ((SWTWidgetFigure)fig).widget;
				n.moveAbove(c);
			}
		} else {
			Rectangle figRect = new Rectangle(fig.globalLocation,fig.size);
			
			for(Control c : currentZOrder){
				Rectangle cRect = new Rectangle(c.getLocation().x, c.getLocation().y,c.getSize().x,c.getSize().y );
				
			}
		}
		
	}

}
