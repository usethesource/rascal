package org.rascalmpl.library.vis.swt.zorder;

import java.util.Collections;
import java.util.Stack;
import java.util.Vector;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.Overlap;
import org.rascalmpl.library.vis.figure.interaction.MouseOver;
import org.rascalmpl.library.vis.figure.interaction.swtwidgets.SWTWidgetFigure;
import org.rascalmpl.library.vis.swt.FigureSWTApplet;
import org.rascalmpl.library.vis.util.Rectangle;

public class SWTZOrderManager implements ISWTZOrdering {
	
	private Composite floor;
	private Vector<Control> currentZOrder;
	private Stack<Integer> overlapIndexes;
	private Vector<OverlapCanvas> overlapPool;
	private int currentIndexOverlapPool;
	private FigureSWTApplet parent;
	private Vector<IHasZOrder> allElements;
	private int currentDepth;

	
	public SWTZOrderManager(FigureSWTApplet parent,Composite floor) {
		currentZOrder = new Vector<Control>();
		overlapIndexes = new Stack<Integer>();
		overlapPool = new Vector<OverlapCanvas>();
		allElements = new Vector<IHasZOrder>();
		currentIndexOverlapPool = 0;
		this.floor = floor;
		this.parent = parent;
	}

	public void begin(){
		currentDepth = 0;
		allElements.clear();
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
		setZOrder();
	}

	@Override
	public void pushOverlap() {
		overlapIndexes.push(currentZOrder.size());
		currentDepth++;
	}

	@Override
	public void popOverlap() {
		while(overlapIndexes.peek() > currentZOrder.size() ){
			currentZOrder.remove(overlapIndexes.peek());
		}
		overlapIndexes.pop();
		currentDepth--;
	}
	
	@Override
	public void register(Figure fig) {
		if(overlapIndexes.isEmpty()){
			System.out.printf("Weird.. no overlaps\n");
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
		currentZOrder.add(canv);
		canv.setZOrder(currentDepth);
		allElements.add(canv);
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

	@SuppressWarnings("rawtypes")
	@Override
	public void registerControl(SWTWidgetFigure c) {
		c.setZOrder(currentDepth);
		currentZOrder.add(c.widget);
		allElements.add(c);
	}
	
	
	private void setZOrder() {
		Collections.sort(allElements, IHasZOrderComparator.instance);
		Control prev = null;
		for(IHasZOrder elem : allElements){
			Control cur = elem.getElement();
			if(prev == null) cur.moveBelow(parent);
			else cur.moveAbove(prev);
			prev = cur;
		}
		
	}


}
