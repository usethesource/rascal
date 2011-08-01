package org.rascalmpl.library.vis.swt;

import java.util.Stack;
import java.util.Vector;

import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.containers.Overlap;
import org.rascalmpl.library.vis.containers.Space;
import org.rascalmpl.library.vis.containers.WhiteSpace;
import org.rascalmpl.library.vis.interaction.MouseOver;
import org.rascalmpl.library.vis.swtwidgets.SWTWidgetFigure;
import org.rascalmpl.library.vis.util.Rectangle;

public class SWTZOrderManager implements ISWTZOrdering {
	
	private Figure parentFig;
	private Composite floor;
	private Vector<Control> currentZOrder;
	private Stack<Integer> overlapIndexes;
	private Vector<OverlapCanvas> overlapPool;
	private int currentIndexOverlapPool;
	private FigureSWTApplet parent;

	
	public SWTZOrderManager(FigureSWTApplet parent,Composite floor,Figure parentFig) {
		currentZOrder = new Vector<Control>();
		overlapIndexes = new Stack<Integer>();
		overlapPool = new Vector<OverlapCanvas>();
		currentIndexOverlapPool = 0;
		this.parentFig = parentFig;
		this.floor = floor;
		this.parent = parent;
	}

	public void begin(){
		currentZOrder.clear();
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
	}

	@Override
	public void popOverlap() {
		while(overlapIndexes.peek() > currentZOrder.size() ){
			currentZOrder.remove(overlapIndexes.peek());
		}
		overlapIndexes.pop();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void register(Figure fig) {
		if(overlapIndexes.isEmpty()){
			return;
		}
		if(!(fig instanceof SWTWidgetFigure)){
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
			overlapPool.add(new OverlapCanvas(parent,floor));
		}
		currentIndexOverlapPool++;
		return overlapPool.get(currentIndexOverlapPool-1);
	}
	
	private void addOverlap(Figure fig){
		
		Rectangle overlap = fig.getRectangleIncludingOuterLines();
		OverlapCanvas canv = getOverlapCanvasFromPool();
		canv.setOverlap(fig,overlap);
		moveAboveAll(canv);
		currentZOrder.add(canv);
	}
	
	private void moveAboveAll(Control c){
		c.moveBelow(null);
		if(!currentZOrder.isEmpty()){
			c.moveAbove(currentZOrder.lastElement());
		} else {
			//System.out.printf("Moving to bottom");
			
		}
	}

	@Override
	public void registerOverlap(Overlap overlap) {
		if(overlap.nonLocalFigure.isVisible()){
			parent.addOverlapFigure(overlap);
			register(overlap.nonLocalFigure);
		}
	}

	@Override
	public void registerMouseOver(MouseOver mouseOver) {
		parent.registerMouseOver(mouseOver);
	}

	@Override
	public void registerControl(Control c) {
		moveAboveAll(c);
		currentZOrder.add(c);
	}
	
	


}
