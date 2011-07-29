package org.rascalmpl.library.vis.swt;

import java.util.Stack;
import java.util.Vector;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.interaction.SWTWidgetFigure;
import org.rascalmpl.library.vis.util.Rectangle;

public class SWTZOrderManager implements ISWTZOrdering {
	
	private Figure parentFig;
	private Composite floor;
	private Vector<Control> newElements;
	private Vector<Control> currentZOrder;
	private Stack<Integer> overlapIndexes;
	private Vector<OverlapCanvas> overlapPool;
	private int currentIndexOverlapPool;
	
	public SWTZOrderManager(Composite floor,Figure parentFig) {
		currentZOrder = new Vector<Control>();
		newElements = new Vector<Control>();
		overlapIndexes = new Stack<Integer>();
		overlapPool = new Vector<OverlapCanvas>();
		currentIndexOverlapPool = 0;
		this.parentFig = parentFig;
		this.floor = floor;
	}

	public void begin(){
		currentZOrder.clear();
		newElements.clear();
		overlapIndexes.clear();
		currentIndexOverlapPool = 0;
		overlapIndexes.push(0);
	}
	
	public void end(){
		while(currentIndexOverlapPool < overlapPool.size()){
			overlapPool.get(currentIndexOverlapPool).dispose();
			overlapPool.remove(currentIndexOverlapPool);
		}
	}
	
	@Override
	public void pushOverlap() {
		overlapIndexes.push(currentZOrder.size());
		currentZOrder.addAll(newElements);
		newElements.clear();
	}

	@Override
	public void popOverlap() {
		while(overlapIndexes.peek() > currentZOrder.size()){
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
			Control n = ((SWTWidgetFigure)fig).widget;
			moveAboveAll(n);
			newElements.add(n);
		} else {
			addSWTFigureOverlaps(fig);
		}
	}

	private void addSWTFigureOverlaps(Figure fig) {
		if(overlapsWithSWT(fig)){
			addOverlap(fig);
		}
	}
	
	private boolean overlapsWithSWT(Figure fig){
		Rectangle figRect = new Rectangle(fig.globalLocation,fig.size);
		for(Control c : currentZOrder){
			Rectangle cRect = new Rectangle(c.getLocation().x, c.getLocation().y,c.getSize().x,c.getSize().y );
			if(figRect.overlapsWith(cRect)){ return true; }
		}
		return false;
		
	}
	
	private OverlapCanvas getOverlapCanvasFromPool(){
		if(currentIndexOverlapPool == overlapPool.size()){
			overlapPool.add(new OverlapCanvas(floor));
		}
		currentIndexOverlapPool++;
		return overlapPool.get(currentIndexOverlapPool-1);
	}
	
	private void addOverlap(Figure fig){
		Rectangle overlap = fig.getRectangle();
		OverlapCanvas canv = getOverlapCanvasFromPool();
		canv.setOverlap(parentFig,overlap);
		moveAboveAll(canv);
	}
	
	private void moveAboveAll(Control c){
		if(!currentZOrder.isEmpty()){
			c.moveAbove(currentZOrder.lastElement());
		}
	}
	
	


}
