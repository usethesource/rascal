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
	
	private Composite floor;
	private Vector<Control> newElements;
	private Vector<Control> currentZOrder;
	private Stack<Integer> overlapIndexes;
	private Vector<OverlapCanvas> overlapPool;
	private int currentIndexOverlapPool;
	
	public SWTZOrderManager(Composite floor) {
		currentZOrder = new Vector<Control>();
		newElements = new Vector<Control>();
		overlapIndexes = new Stack<Integer>();
		overlapPool = new Vector<OverlapCanvas>();
		this.floor = floor;
		currentIndexOverlapPool = 0;
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
		System.out.printf("Push\n");
		overlapIndexes.push(currentZOrder.size());
		currentZOrder.addAll(newElements);
		newElements.clear();
	}

	@Override
	public void popOverlap() {
		System.out.printf("Pop\n");
		while(overlapIndexes.peek() > currentZOrder.size()){
			newElements.remove(overlapIndexes.peek());
		}
		overlapIndexes.pop();
	}
	
	@SuppressWarnings("rawtypes")
	@Override
	public void register(Figure fig) {
		System.out.printf("Registering %s\n",fig);
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
		System.out.printf("Adding SWT overlaps %s %d\n",fig,currentZOrder.size());
		Rectangle figRect = new Rectangle(fig.globalLocation,fig.size);
		for(int i = currentZOrder.size()-1 ; i >= 0; i--){
			Control c = currentZOrder.get(i);
			Rectangle cRect = new Rectangle(c.getLocation().x, c.getLocation().y,c.getSize().x,c.getSize().y );
			if(figRect.overlapsWith(cRect)){
				System.out.printf("Adding actual SWT overlap %s\n",fig);
				Rectangle overlap = figRect.getOverlap(cRect);
				addOverlap(fig,c,overlap,i);
			} else {
				System.out.printf("No actual SWT overlap %s with \n",fig, c);
			}
		}
	}
	
	private OverlapCanvas getOverlapCanvasFromPool(){
		if(currentIndexOverlapPool == overlapPool.size()){
			overlapPool.add(new OverlapCanvas(floor));
		}
		currentIndexOverlapPool++;
		return overlapPool.get(currentIndexOverlapPool-1);
	}
	
	private void addOverlap(Figure fig, Control below, Rectangle overlap, int aboveIndex){
		System.out.printf("Setting overlap canvas %s %d\n",overlap,aboveIndex);
		OverlapCanvas canv = getOverlapCanvasFromPool();
		canv.setOverlap(fig, overlap);
		setZOrder(canv, aboveIndex);
		newElements.add(canv);
	}
	
	private void moveAboveAll(Control c){
		setZOrder(c,currentZOrder.size());
	}
	
	private void setZOrder(Control c, int aboveIndex){
		for(int j = 0 ; j <= aboveIndex &&  j < currentZOrder.size(); j++){
			c.moveAbove(currentZOrder.get(j));
		}
		for(int j = aboveIndex+1 ; j < currentZOrder.size() ; j++){
			c.moveBelow(currentZOrder.get(j));
		}
	}
	


}
