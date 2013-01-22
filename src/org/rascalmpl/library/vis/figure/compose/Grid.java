/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.figure.compose;

import static org.rascalmpl.library.vis.properties.TwoDProperties.ALIGN;
import static org.rascalmpl.library.vis.properties.TwoDProperties.END_GAP;
import static org.rascalmpl.library.vis.properties.TwoDProperties.GAP;
import static org.rascalmpl.library.vis.properties.TwoDProperties.GROW;
import static org.rascalmpl.library.vis.properties.TwoDProperties.SHRINK;
import static org.rascalmpl.library.vis.properties.TwoDProperties.START_GAP;
import static org.rascalmpl.library.vis.util.Util.flatten;
import static org.rascalmpl.library.vis.util.Util.makeRectangular;
import static org.rascalmpl.library.vis.util.vector.Dimension.HOR_VER;
import static org.rascalmpl.library.vis.util.vector.Dimension.Y;

import java.util.Arrays;
import java.util.List;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.containers.Space;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.vector.Dimension;
import org.rascalmpl.library.vis.util.vector.Rectangle;
import org.rascalmpl.library.vis.util.vector.TwoDimensional;


/* TODO: This is horibly horibly horibly overcomplicated! */

public class Grid extends Compose {
	
	public static final String OVERCONSTRAINED_MESSAGE = "Grid Overconstrained!";
	
	Figure[][] figureMatrix ;
	int nrColumns, nrRows;
	TwoDimensional<double[]> columnBorders;
	TwoDimensional<Size[]> columnsSize;
	TwoDimensional<Double> totalShrinkAllSetColumns;
	TwoDimensional<double[]> someShrinksSetShrinks;
	TwoDimensional<Integer> nrUnresizableColumns;
	TwoDimensional<Integer> nrShrinkNoneColumns;
	TwoDimensional<Integer> nrShrinkSomeColumns;
	TwoDimensional<Integer> nrShrinkAllColumns;
	TwoDimensional<Double> unresizableColumnsWidth;
	boolean overConstrained;

	
	static enum SizeInfo{
		ALL_SHRINK_SET,
		SOME_SHRINK_SET,
		NONE_SHRINK_SET,
		UNRESIZABLE;
	}
	
	static class Size{
		SizeInfo sizeInfo;
		double minSize;
		double maxShrink;
		double minSizeOfGrid;
		Size(SizeInfo sizeInfo, double minSize,double maxShrink, double minSizeOfGrid){
			this.sizeInfo = sizeInfo;
			this.minSize = minSize;
			this.maxShrink = maxShrink;
			this.minSizeOfGrid = minSizeOfGrid;
		}
		
		Size(SizeInfo sizeInfo, double minSize){
			this(sizeInfo, minSize,0,0);
		}
	}
	
	public Grid(Figure[][] figureMatrix,PropertyManager properties) {
		super(flatten(Figure.class,figureMatrix),properties);
		this.figureMatrix = figureMatrix;
		makeRectangular(figureMatrix,Space.empty);
		
		nrRows = figureMatrix.length;
		nrColumns = figureMatrix[0].length;
		overConstrained = false;
		columnBorders = new TwoDimensional<double[]>(new double[nrColumns], new double[nrRows]) ;
		columnsSize = new TwoDimensional<Grid.Size[]>(new Size[nrColumns], new Size[nrRows]);
		unresizableColumnsWidth = new TwoDimensional<Double>(0.0,0.0);
		totalShrinkAllSetColumns =  new TwoDimensional<Double>(0.0,0.0);
		nrUnresizableColumns = new TwoDimensional<Integer>(0, 0);
		nrShrinkNoneColumns = new TwoDimensional<Integer>(0, 0);
		nrShrinkSomeColumns= new TwoDimensional<Integer>(0, 0);
		nrShrinkAllColumns= new TwoDimensional<Integer>(0, 0);
		someShrinksSetShrinks = new TwoDimensional<double[]>(null,null);
	}
	
	public void computeMinSize(){
		overConstrained = false;
		for(Dimension d : HOR_VER){
			computeMinWidth(d);
		}
		if(overConstrained){
			minSize.set(getTextWidth(OVERCONSTRAINED_MESSAGE),getTextAscent() + getTextDescent());
		}
	}
	

	public void computeMinWidth(Dimension d){
		setSizeInfoOfColumns(d);
		setColumnTypeCounts(d);
		this.unresizableColumnsWidth.set(d, totalMinWidthOfUnresizableColumns(d));
		this.totalShrinkAllSetColumns.set(d,totalShrinkAllSetColumns(d));
		double minWidth = minWidthByUnShrinking(d);
		if( totalShrinkAllSetColumns.get(d) == 1.0 && nrShrinkAllColumns.get(d) < getNrColumns(d)){
			overConstrained = true;
			return;
		}
		if(totalShrinkAllSetColumns.get(d) > 1.0){
			overConstrained = true;
			return;
		}
		minWidth = Math.max(minWidth,unresizableColumnsWidth.get(d)/ (1.0 - totalShrinkAllSetColumns.get(d)));
		double maxMinWidthOfAutoElem = maxMinWidthOfAutoElement(d);
		double shrinkLeftOver = 1.0 - totalShrinkAllSetColumns.get(d);
		minWidth = getAutoElementsShrinkMinWidth(shrinkLeftOver,d,minWidth,maxMinWidthOfAutoElem);
		if(minWidth == -1){
			overConstrained = true;	
			return;
		}
		double minWidthWithGrow = minWidth * prop.get2DReal(d, GROW);
		double minWidthWithGaps = minWidth +  (double)nrHGaps(d) * prop.get2DReal(d, GAP);
		minWidth = Math.max(minWidthWithGaps, minWidthWithGrow);
		
		if(nrUnresizableColumns.get(d) == 1 && getNrColumns(d) == 1){
			
			resizable.set(d,false);
		}
		this.minSize.set(d, minWidth);
	}
	
	private double totalMinWidthOfUnresizableColumns(Dimension d){
		double result = 0;
		Size[] columnSize = this.columnsSize.get(d);
		for(Size s : columnSize){
			if(s.sizeInfo == SizeInfo.UNRESIZABLE){
				result += s.minSize;
			}
		}
		return result;
	}
	
	private double minWidthByUnShrinking(Dimension d){
		double minWidth = 0;
		for(Figure[] row : figureMatrix){
			for(Figure elem : row){
				if(elem.prop.is2DPropertySet(d, SHRINK) && elem.resizable.get(d)){
					minWidth = Math.max(minWidth,elem.minSize.get(d) / elem.prop.get2DReal(d, SHRINK));
				}
			}
		}
		return minWidth;
	}
	
	private double maxMinWidthOfAutoElement(Dimension d){
		double maxMinWidth = 0;
		for(int i = 0; i < getNrColumns(d); i++){
			boolean columnUnresizable  = columnsSize.get(d)[i].sizeInfo == SizeInfo.UNRESIZABLE;
			for(int j = 0 ; j < getNrRows(d) ; j++){
				Figure elem = getFigureFromMatrix(d, j,i);
				
				if(!elem.prop.is2DPropertySet(d, SHRINK) && !columnUnresizable){
					maxMinWidth = Math.max(maxMinWidth,elem.minSize.get(d));
				}
			}
		}
		return maxMinWidth;
	}
	
	private double totalShrinkAllSetColumns(Dimension d){
		double result = 0;
		Size[] columnSize = this.columnsSize.get(d);
		for(Size s : columnSize){
			if(s.sizeInfo == SizeInfo.ALL_SHRINK_SET){
				result += s.maxShrink;
			}
		}
		return result;
	}
	
	private void setSomeShrinkSetSorted(Dimension d) {
		double[] result;
		if(someShrinksSetShrinks.get(d) == null || someShrinksSetShrinks.get(d).length != nrShrinkSomeColumns.get(d)){
			result = new double[nrShrinkSomeColumns.get(d)];
			someShrinksSetShrinks.set(d,result);
		} else {
			result = someShrinksSetShrinks.get(d);
		}
		int i = 0;
		for(Size s : columnsSize.get(d)){
			if(s.sizeInfo == SizeInfo.SOME_SHRINK_SET){
				result[i] = s.maxShrink;
				i++;
			}
		}
		Arrays.sort(result);
	}
	
	
	private void setColumnTypeCounts(Dimension d){
		int nrAllSet = 0;
		int nrSomeSet = 0;
		int nrNoneSet = 0;
		int nrUnresizable = 0;
		Size[] columnSize = this.columnsSize.get(d);
		for(Size s : columnSize){
			switch(s.sizeInfo){
			case ALL_SHRINK_SET : nrAllSet++; break;
			case SOME_SHRINK_SET : nrSomeSet++; break;
			case NONE_SHRINK_SET: nrNoneSet++; break;
			case UNRESIZABLE: nrUnresizable++; break;
			}
		}
		this.nrShrinkAllColumns.set(d, nrAllSet);
		this.nrShrinkSomeColumns.set(d, nrSomeSet);
		this.nrShrinkNoneColumns.set(d,nrNoneSet);
		this.nrUnresizableColumns.set(d,nrUnresizable);
	}
	
	private void setSizeInfoOfColumns(Dimension d){
		Size[] result = columnsSize.get(d);
		for(int column = 0 ; column < getNrColumns(d); column++){
			result[column] = getSizeInfoOfColumn(d, column);
		}
	}
	
	private Size getSizeInfoOfColumn(Dimension d, int column){
		double minSize = 0;
		double maxShrink = 0;
		double minSizeOfGrid = 0;
		boolean resizable = false;
		boolean autoSize = true;
		boolean allShrinkSet = true;
		for(int row = 0 ; row < getNrRows(d); row++){
			Figure fig = getFigureFromMatrix(d, row, column);
			resizable= resizable || fig.resizable.get(d);
			if(fig.resizable.get(d) && fig.prop.is2DPropertySet(d, SHRINK)){
				autoSize = false;
				maxShrink = Math.max(maxShrink, fig.prop.get2DReal(d, SHRINK));
				minSizeOfGrid = Math.max(minSizeOfGrid, fig.minSize.get(d) / fig.prop.get2DReal(d, SHRINK));
			} else {
				allShrinkSet = false;
			}
			minSize= Math.max(minSize,fig.minSize.get(d));
		}
		SizeInfo sizeInfo;
		if(!resizable){
			sizeInfo = SizeInfo.UNRESIZABLE;
		} else if(autoSize){
			sizeInfo = SizeInfo.NONE_SHRINK_SET;
		} else if(allShrinkSet){
			sizeInfo = SizeInfo.ALL_SHRINK_SET;
		} else {
			sizeInfo = SizeInfo.SOME_SHRINK_SET;
		}
		return new Size(sizeInfo,minSize,maxShrink,minSizeOfGrid);
	}
	
	private double getAutoElementsShrinkMinWidth(double shrinkLeftOver,Dimension d,double minWidthEstimate,double maxMinWidthOfAutoElem){
		if(shrinkLeftOver < 0) return -1;
		// this is where the meat of the layout is, which is fairly complicated, but very fast
		// this required some thinking, get ready : 
		// we want to solve f in :   
		// 		sum({max(f,someShrinksSetShrinks[i]) | i <- [0..nrSomeShrinkSetColumns-1]}) + f * nrOfNoneShrinkSetCollumns  = shrinkLeftOver
		
		// i.e. shrinkLeftOver 	= sum({someShrinksSetShrinks[j] | someShrinksSetShrinks[j] > f, j in [0..nrSomeShrinkSetColumns-1]})
		//						+ f * (size({someShrinksSetShrinks[i] | someShrinksSetShrinks[i] <= f,j in [0..nrColumnsSomeShrinkSet-1]}) + nrOfNoneShrinkSetCollumns
		// i.e. shrinkLeftOver = sum(S) + (nrColumnsSomeOrNoneShrinkSet - size(S) )*f 
		//		where S = {someShrinksSetShrinks[j] | someShrinksSetShrinks[j] > f, j in [0..nrSomeShrinkSetColumns-1]}
		// to do this we first sort the columns with some shrinkset in descending max shrink order
		 setSomeShrinkSetSorted(d);
		 double[] someShrinksSetShrinks = this.someShrinksSetShrinks.get(d);
		// (the array is actually sorted in ascending order because 
		// java does not offer an fast,easy way to sort doubles in descending order
		// therefore we simply index from the back)
		// to estimate let us assume that S == {}
		// now we begin with the highest estimate f = shrinkLeftOver / nrColumnsSomeOrNoneShrinkSet
		int nrColumnsSomeOrNoneShrinkSet = nrShrinkSomeColumns.get(d) + nrShrinkNoneColumns.get(d);
		double fEstimate = shrinkLeftOver / nrColumnsSomeOrNoneShrinkSet;
		// some corner cases:
		
		if(nrShrinkSomeColumns.get(d) == 0){
			double totalMinWidth = (maxMinWidthOfAutoElem * (nrColumnsSomeOrNoneShrinkSet) + unresizableColumnsWidth.get(d)) /shrinkLeftOver;
			minWidthEstimate = Math.max(minWidthEstimate, totalMinWidth);
			return minWidthEstimate = Math.max(minWidthEstimate, totalMinWidth);
		}
		if(nrColumnsSomeOrNoneShrinkSet == 0) {
			if(unresizableColumnsWidth.get(d) != 0|| shrinkLeftOver < 0.0){
				return -1; // Overspecified!
			} else {
				return minWidthEstimate; // there is nothing to do here
			}
		}
		
		// now we start from the front (highest shrink) (i=1) to see if maxColumShrink(i) <= f
		// if so, we are done (because the other values are all smaller or equal than maxColumShrink(i)
		// if not, then we know that 
		// S' = {someShrinksSetShrinks[j] |  j in [0..i]}  is a subset of S 
		// because the array is sorted and all previous values where higher
		// thus sum(S') <= sum(S)
		// for the new estimate assume that S' == S
		// so that we get the highest estimate again
		// the new estimate then becomes shrinkLeftOver = sum(S') + f * (nrColumnsSomeOrNoneShrinkSet - size(S'))
		//			i.e. f = (shrinkLeftOver - sum(S')) / (nrColumnsSomeOrNoneShrinkSet - size(S'))
		// 				 f = (shrinkLeftOver - sum(S')) / (nrColumnsSomeOrNoneShrinkSet - i)
		// time complexity: O(n*log(n)) (because of sorting)
		// space complexity: O(n)
		double currentSumSPrime = 0;
		int i = 1;
		for(int j = someShrinksSetShrinks.length-1; j >= 0 ; j--,i++){
			if(someShrinksSetShrinks[j] < fEstimate){
				// however, up until now we assumed that there were no unresizable columns
				// if we drop this assumption, thing turn even nastier
				// the unresizable column also needs a part of the available space
				// so the f described thus far is only an upper bound
				// the spaceleftOver is decreased by the unresizable collumns by unresizableColumns/totalMinWidth
				// however this depends on the totalMinWidth which relies on f....
				// let shrinkLeftOver -=  currentSumSPrime
				shrinkLeftOver-= currentSumSPrime;
				// then we know that f = (shrinkLeftOver - ( unresizableColumns/totalMinWidth)) / (nrColumnsSomeOrNoneShrinkSet - (i-1))
				// and totalMinWidth = maxMinSizeAutoElement / f
				// combining these two gives 
				// totalMinWidth =  maxMinSizeAutoElement / ((shrinkLeftOver - ( unresizableColumns/totalMinWidth)) / (nrColumnsSomeOrNoneShrinkSet - (i-1)))
				// rewriting gives totalMinWidth = (maxMinSizeAutoElement * (nrColumnsSomeOrNoneShrinkSet - (i-1)) + unresizableColumns) /shrinkLeftOver
				double totalMinWidth = (maxMinWidthOfAutoElem * (nrColumnsSomeOrNoneShrinkSet - (i-1)) + unresizableColumnsWidth.get(d)) /shrinkLeftOver;
				minWidthEstimate = Math.max(minWidthEstimate, totalMinWidth);
				// however f = (shrinkLeftOver - unresizableColumns/totalMinWidth) /  (nrColumnsSomeOrNoneShrinkSet - (i-1))
				double mayBeF =  (shrinkLeftOver - unresizableColumnsWidth.get(d)/totalMinWidth) /  (nrColumnsSomeOrNoneShrinkSet - (i-1));
				// so changing the totalMinWidth might cause our estimation of f to drop below someShrinksSetShrinks[i]
				if(mayBeF >= someShrinksSetShrinks[j]){
					// if this doesn't happen, we are done!
					fEstimate = mayBeF;
					break;
				} else {
					// if our new fEstimate caused f to drop below someShrinksSetShrinks[i]
					// we must take special measures
					// first undo the shrinkLeftOver-= currentSumSPrime;
					shrinkLeftOver+= currentSumSPrime;
					// set estimate to 
					fEstimate = someShrinksSetShrinks[j] ;
					// and try again! (rollback counters to goto second case)
					j++; i--;
				}
			} else {
				currentSumSPrime += someShrinksSetShrinks[j];
				if(i == nrColumnsSomeOrNoneShrinkSet){ // prevent division by zero when overconstrained
					fEstimate = -1;
					break;
				}
				fEstimate = (shrinkLeftOver - currentSumSPrime) / (nrColumnsSomeOrNoneShrinkSet -i);
					
			}
		}
		if(fEstimate <= 0){
			return -1;
		}
		return Math.max(minWidthEstimate,  maxMinWidthOfAutoElem / fEstimate); // return actual minwidth
	}
	
	
	private double getActualShrinkOfAutoElements(double shrinkLeftOver,Dimension d){
		// TODO: this is pretty much the same as above, merge
		double currentSumSPrime = 0;
		int i = 1;
		double[] someShrinksSetShrinks = this.someShrinksSetShrinks.get(d);
		int nrColumnsSomeOrNoneShrinkSet = nrShrinkSomeColumns.get(d) + nrShrinkNoneColumns.get(d);

		if(nrShrinkSomeColumns.get(d) == 0){
			return shrinkLeftOver /  nrColumnsSomeOrNoneShrinkSet;
		}
		if(nrColumnsSomeOrNoneShrinkSet == 0) {
			return 1.0;
		}
		double fEstimate = shrinkLeftOver / nrColumnsSomeOrNoneShrinkSet;
		for(int j = someShrinksSetShrinks.length-1; j >= 0 ; j--,i++){
			if(someShrinksSetShrinks[j] < fEstimate){
				return fEstimate;
			} else {
				currentSumSPrime += someShrinksSetShrinks[j];
				fEstimate = (shrinkLeftOver - currentSumSPrime) / (nrColumnsSomeOrNoneShrinkSet -i);
			}
		}
		return fEstimate;
	}
	
	public void layoutX(Dimension d) {
		if(overConstrained) return;
		
		double spaceForColumns = size.get(d) / prop.get2DReal(d, GROW);
		spaceForColumns = Math.min(spaceForColumns, size.get(d) - (double)nrHGaps(d) * prop.get2DReal(d, GAP));
		double spaceLeftOver = spaceForColumns - unresizableColumnsWidth.get(d);
		double shrinkLeftOver = (spaceLeftOver / spaceForColumns) - totalShrinkAllSetColumns.get(d);
		double shrinkOfAutoElement = getActualShrinkOfAutoElements(shrinkLeftOver,d);
		double sizeOfAutoElement = shrinkOfAutoElement * spaceForColumns;
		double whitespace = size.get(d) - spaceForColumns;
		double extraSpaceForUnresizableCols = 0;
		if(nrUnresizableColumns.get(d) == getNrColumns(d)){
			extraSpaceForUnresizableCols = spaceLeftOver / getNrColumns(d);
		}
		double left = 0;
		double gapSize = whitespace / (double)nrHGaps(d) ;
		if(nrHGaps(d) == 0.0){
			gapSize = 0.0;
		}
		if(prop.get2DBool(d, START_GAP)){
			left+=gapSize*0.5;
		}
		for(int column = 0 ; column < getNrColumns(d) ; column++){
			columnBorders.get(d)[column]=left;
			Size s = columnsSize.get(d)[column];
			double colWidth = 0;
			switch(s.sizeInfo){
				case ALL_SHRINK_SET : colWidth = s.maxShrink * spaceForColumns; break;
				case SOME_SHRINK_SET: colWidth = Math.max(sizeOfAutoElement,s.maxShrink * spaceForColumns); break;
				case NONE_SHRINK_SET: colWidth =sizeOfAutoElement ; break;
				case UNRESIZABLE: colWidth = s.minSize + extraSpaceForUnresizableCols; break;
			}
			
			for(int row = 0 ; row < getNrRows(d); row++){
				Figure elem = getFigureFromMatrix(d,row,column);
				if(elem.prop.is2DPropertySet(d, SHRINK)){
					elem.size.set(d, elem.prop.get2DReal(d, SHRINK)*spaceForColumns);
				} else if(!elem.resizable.get(d)){
					elem.size.set(d,elem.minSize.get(d));
				} else {
					elem.size.set(d,sizeOfAutoElement);
				}
				double margin =Math.max(0.0,(colWidth- elem.size.get(d))  * elem.prop.get2DReal(d, ALIGN)) ;
				setXPos(d, row, column, left + margin);
			}
			left+=gapSize + colWidth;
		}
		size.set(d, left - gapSize);
	}

	double nrHGaps(Dimension d) {
		double nrGaps = getNrColumns(d)-1 ;
		if(prop.get2DBool(d, START_GAP)){
			nrGaps+=0.5;
		} 
		if(prop.get2DBool(d, END_GAP)){
			nrGaps+=0.5;
		}
		return nrGaps;
	}
	
	int getNrColumns(Dimension d){
		if(d == Y) return nrRows;
		else return nrColumns;
	}
	
	int getNrRows(Dimension d){
		if(d == Y) return nrColumns;
		else return nrRows;
	}
	


	private Figure getFigureFromMatrix(Dimension d, int row, int collumn){
		if(d == Y) return figureMatrix[collumn][row];
		else return figureMatrix[row][collumn];
	}


	
	
	private void setXPos(Dimension d, int row, int collumn,double val){
		getFigureFromMatrix(d, row, collumn).localLocation.set(d,val);
	}
	
	
	@Override
	public void drawElement(GraphicsContext gc, List<IHasSWTElement> visibleSWTElements){
		if(overConstrained) {
			gc.text(OVERCONSTRAINED_MESSAGE, globalLocation.getX() + 0.5 * size.getX() - getTextWidth(OVERCONSTRAINED_MESSAGE),
					globalLocation.getY() + 0.5 * size.getY()  - getTextAscent());
			return;
		}
	}

	@Override
	public void resizeElement(Rectangle view) {
		for(Dimension d: HOR_VER){
			layoutX(d);
		}
		
	}
	
	
	public String toString(){
		return "GRID";
	}

	/*
	public void getFiguresUnderMouseSmart(Coordinate c, Vector<Figure> result){
		int row = binaryIntervalSearch(columnBorders.getY(), c.getY() - location.getY());
		int column = binaryIntervalSearch(columnBorders.getX(), c.getX() - location.getX());
		if(row >= 0 && column >= 0){
			figureMatrix[row][column].getFiguresUnderMouse(c, result);
		}
	}
	*/
	
	/*
	@Override
	public void drawVisibleChildrenSmart(
			Vector<IHasSWTElement> visibleSWTElements, GraphicsContext gc,
			Rectangle rect){
		int startRow = Math.max(0,Util.binaryIntervalSearch(columnBorders.getForY(), rect.getY() - getTop()));
		int endRow = Math.max(0,Util.binaryIntervalSearch(columnBorders.getForY(), rect.getYDown() - getTop()));
		int startColumn = Math.max(0,Util.binaryIntervalSearch(columnBorders.getForX(), rect.getX() - getLeft()));
		int endColumn = Math.max(0,Util.binaryIntervalSearch(columnBorders.getForX(), rect.getXRight() - getLeft()));
		//System.out.printf("Drawpart grid rows %d till %d of %d  collumns %d till %d of %d\n", startRow, endRow, figureMatrix.length, startColumn, endColumn, figureMatrix[0].length);
		for(int row = startRow ; row <= endRow ; row++){
			for(int collumn = startColumn ; collumn <= endColumn; collumn++){
				if(		(row == startRow && columnBorders.getForY()[startRow] < rect.getY())
					|| 	(row == endRow && columnBorders.getForY()[endRow] > rect.getYDown())
					|| 	(collumn == startColumn && columnBorders.getForX()[startColumn] < rect.getX())
					|| 	(collumn == endColumn && columnBorders.getForX()[endColumn] > rect.getXRight())){
					figureMatrix[row][collumn].draw(visibleSWTElements,gc,rect);
				} else {
					figureMatrix[row][collumn].draw(visibleSWTElements,gc,null);
				}
			}
		}
	}
	*/

	

}
