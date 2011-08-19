package org.rascalmpl.library.vis.figure.compose;

import java.util.Arrays;
import java.util.Vector;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.containers.EmptyFigure;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.zorder.ISWTZOrdering;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.ForBothDimensions;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.Rectangle;
import org.rascalmpl.library.vis.util.Util;


public class Grid extends Figure {
	
	public static final String OVERCONSTRAINED_MESSAGE = "Grid Overconstrained!";
	
	Figure[][] figureMatrix ;
	Coordinate[][] pos;
	int nrColumns, nrRows;
	ForBothDimensions<double[]> columnBorders;
	ForBothDimensions<Size[]> columnsSize;
	ForBothDimensions<Double> totalShrinkAllSetColumns;
	ForBothDimensions<double[]> someShrinksSetShrinks;
	ForBothDimensions<Integer> nrShrinkNoneColumns;
	ForBothDimensions<Integer> nrShrinkSomeColumns;
	ForBothDimensions<Integer> nrShrinkAllColumns;
	ForBothDimensions<Double> unresizableColumnsWidth;
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
		super(properties);
		this.figureMatrix = figureMatrix;
		Util.makeRectangular(figureMatrix,EmptyFigure.instance);
		
		nrRows = figureMatrix.length;
		nrColumns = figureMatrix[0].length;
		pos= new Coordinate[nrRows][nrColumns];
		for(int i = 0 ; i < nrRows ; i++) { 
			for(int j = 0 ; j < nrColumns ; j++) {
				pos[i][j] = new Coordinate();
			}
		}
		overConstrained = false;
		columnBorders = new ForBothDimensions<double[]>(new double[nrColumns], new double[nrRows]) ;
		columnsSize = new ForBothDimensions<Grid.Size[]>(new Size[nrColumns], new Size[nrRows]);
		unresizableColumnsWidth = new ForBothDimensions<Double>(0.0,0.0);
		totalShrinkAllSetColumns =  new ForBothDimensions<Double>(0.0,0.0);
		nrShrinkNoneColumns = new ForBothDimensions<Integer>(0, 0);
		nrShrinkSomeColumns= new ForBothDimensions<Integer>(0, 0);
		nrShrinkAllColumns= new ForBothDimensions<Integer>(0, 0);
		someShrinksSetShrinks = new ForBothDimensions<double[]>(null,null);
	}
	
	public void bbox(){
		overConstrained = false;
		for(Figure[] row : figureMatrix){
			for(Figure elem : row){
				elem.bbox();
			}
		}
		for(boolean flip : BOTH_DIMENSIONS){
			computeMinWidth(flip);
		}
		if(overConstrained){
			minSize.set(getTextWidth(OVERCONSTRAINED_MESSAGE),getTextAscent() + getTextDescent());
		}
		setResizable();
	}
	

	public void computeMinWidth(boolean flip){
		this.columnsSize.setForX(flip,getSizeInfoOfColumns(flip));
		setColumnTypeCounts(flip);
		this.unresizableColumnsWidth.setForX(flip, totalMinWidthOfUnresizableColumns(flip));
		this.totalShrinkAllSetColumns.setForX(flip,totalShrinkAllSetColumns(flip));
		double minWidth = minWidthByUnShrinking(flip);
		minWidth = Math.max(minWidth,unresizableColumnsWidth.getForX(flip)/ (1.0 - totalShrinkAllSetColumns.getForX(flip)));
		double maxMinWidthOfAutoElem = maxMinWidthOfAutoElement(flip);
		double shrinkLeftOver = 1.0 - totalShrinkAllSetColumns.getForX(flip);
		minWidth = getAutoElementsShrinkMinWidth(shrinkLeftOver,flip,minWidth,maxMinWidthOfAutoElem);
		if(minWidth == -1){
			overConstrained = true;
			
		}
		minWidth*= getHGrowProperty(flip);
		this.minSize.setWidth(flip, minWidth);
	}
	
	private double totalMinWidthOfUnresizableColumns(boolean flip){
		double result = 0;
		Size[] columnSize = this.columnsSize.getForX(flip);
		for(Size s : columnSize){
			if(s.sizeInfo == SizeInfo.UNRESIZABLE){
				result += s.minSize;
			}
		}
		return result;
	}
	
	private double minWidthByUnShrinking(boolean flip){
		double minWidth = 0;
		for(Figure[] row : figureMatrix){
			for(Figure elem : row){
				if(elem.isHShrinkPropertySet(flip) && elem.getResizableX(flip)){
					minWidth = Math.max(minWidth,elem.minSize.getWidth(flip) / elem.getHShrinkProperty(flip));
				}
			}
		}
		return minWidth;
	}
	
	private double maxMinWidthOfAutoElement(boolean flip){
		double maxMinWidth = 0;
		for(Figure[] row : figureMatrix){
			for(Figure elem : row){
				if(!elem.isHShrinkPropertySet(flip) && elem.getResizableX(flip)){
					maxMinWidth = Math.max(maxMinWidth,elem.minSize.getWidth(flip));
				}
			}
		}
		return maxMinWidth;
	}
	
	private double totalShrinkAllSetColumns(boolean flip){
		double result = 0;
		Size[] columnSize = this.columnsSize.getForX(flip);
		for(Size s : columnSize){
			if(s.sizeInfo == SizeInfo.ALL_SHRINK_SET){
				result += s.maxShrink;
			}
		}
		return result;
	}
	
	private double[] getSomeShrinkSetSorted(boolean flip) {
		double[] someShrinkSetShrinks = new double[nrShrinkSomeColumns.getForX(flip)];
		int i = 0;
		for(Size s : columnsSize.getForX(flip)){
			if(s.sizeInfo == SizeInfo.SOME_SHRINK_SET){
				someShrinkSetShrinks[i] = s.maxShrink;
				i++;
			}
		}
		Arrays.sort(someShrinkSetShrinks);
		return someShrinkSetShrinks;
	}
	
	
	private void setColumnTypeCounts(boolean flip){
		int nrAllSet = 0;
		int nrSomeSet = 0;
		int nrNoneSet = 0;
		Size[] columnSize = this.columnsSize.getForX(flip);
		for(Size s : columnSize){
			switch(s.sizeInfo){
			case ALL_SHRINK_SET : nrAllSet++; break;
			case SOME_SHRINK_SET : nrSomeSet++; break;
			case NONE_SHRINK_SET: nrNoneSet++; break;
			}
		}
		this.nrShrinkAllColumns.setForX(flip, nrAllSet);
		this.nrShrinkSomeColumns.setForX(flip, nrSomeSet);
		this.nrShrinkNoneColumns.setForX(flip,nrNoneSet);
	}
	
	private Size[] getSizeInfoOfColumns(boolean flip){
		Size[] result = new Size[getNrColumns(flip)];
		for(int column = 0 ; column < getNrColumns(flip); column++){
			result[column] = getSizeInfoOfColumn(flip, column);
		}
		return result;
	}
	
	private Size getSizeInfoOfColumn(boolean flip, int column){
		double minSize = 0;
		double maxShrink = 0;
		double minSizeOfGrid = 0;
		boolean resizable = false;
		boolean autoSize = true;
		boolean allShrinkSet = true;
		for(int row = 0 ; row < getNrRows(flip); row++){
			Figure fig = getFigureFromMatrix(flip, row, column);
			resizable= resizable || fig.getResizableX(flip);
			if(fig.getResizableX(flip) && fig.isHShrinkPropertySet(flip)){
				autoSize = false;
				maxShrink = Math.max(maxShrink, fig.getHShrinkProperty(flip));
				minSizeOfGrid = Math.max(minSizeOfGrid, fig.minSize.getWidth(flip) / fig.getHShrinkProperty(flip));
			} else {
				allShrinkSet = false;
			}
			minSize= Math.max(minSize,fig.minSize.getWidth(flip));
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
	
	private double getAutoElementsShrinkMinWidth(double shrinkLeftOver,boolean flip,double minWidthEstimate,double maxMinWidthOfAutoElem){
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
		double[] someShrinksSetShrinks = getSomeShrinkSetSorted(flip);
		this.someShrinksSetShrinks.setForX(flip, someShrinksSetShrinks);
		// (the array is actually sorted in ascending order because 
		// java does not offer an fast,easy way to sort doubles in descending order
		// therefore we simply index from the back)
		// to estimate let us assume that S == {}
		// now we begin with the highest estimate f = shrinkLeftOver / nrColumnsSomeOrNoneShrinkSet
		int nrColumnsSomeOrNoneShrinkSet = nrShrinkSomeColumns.getForX(flip) + nrShrinkNoneColumns.getForX(flip);
		double fEstimate = shrinkLeftOver / nrColumnsSomeOrNoneShrinkSet;
		// some corner cases:
		
		if(nrShrinkSomeColumns.getForX(flip) == 0){
			double totalMinWidth = (maxMinWidthOfAutoElem * (nrColumnsSomeOrNoneShrinkSet) + unresizableColumnsWidth.getForX(flip)) /shrinkLeftOver;
			minWidthEstimate = Math.max(minWidthEstimate, totalMinWidth);
			return minWidthEstimate = Math.max(minWidthEstimate, totalMinWidth);
		}
		if(nrColumnsSomeOrNoneShrinkSet == 0) {
			if(unresizableColumnsWidth.getForX(flip) != 0|| shrinkLeftOver < 0.0){
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
				double totalMinWidth = (maxMinWidthOfAutoElem * (nrColumnsSomeOrNoneShrinkSet - (i-1)) + unresizableColumnsWidth.getForX(flip)) /shrinkLeftOver;
				minWidthEstimate = Math.max(minWidthEstimate, totalMinWidth);
				// however f = (shrinkLeftOver - unresizableColumns/totalMinWidth) /  (nrColumnsSomeOrNoneShrinkSet - (i-1))
				double mayBeF =  (shrinkLeftOver - unresizableColumnsWidth.getForX(flip)/totalMinWidth) /  (nrColumnsSomeOrNoneShrinkSet - (i-1));
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
	
	public void layout(){
		//System.out.printf("Grid layout %s\n",size);
		for(boolean flip: BOTH_DIMENSIONS){
			layoutX(flip);
		}
		for(Figure[] row : figureMatrix){
			for(Figure elem : row){
				elem.layout();
			}
		}
	}
	
	private double getActualShrinkOfAutoElements(double shrinkLeftOver,boolean flip){
		// TODO: this is pretty much the same as above, merge
		double currentSumSPrime = 0;
		int i = 1;
		double[] someShrinksSetShrinks = this.someShrinksSetShrinks.getForX(flip);
		int nrColumnsSomeOrNoneShrinkSet = nrShrinkSomeColumns.getForX(flip) + nrShrinkNoneColumns.getForX(flip);

		if(nrShrinkSomeColumns.getForX(flip) == 0){
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
	
	public void layoutX(boolean flip) {
		if(overConstrained) return;
		
		double spaceForColumns = size.getWidth(flip) / getHGrowProperty(flip);
		double spaceLeftOver = spaceForColumns - unresizableColumnsWidth.getForX(flip);
		double shrinkLeftOver = (spaceLeftOver / spaceForColumns) - totalShrinkAllSetColumns.getForX(flip);
		double shrinkOfAutoElement = getActualShrinkOfAutoElements(shrinkLeftOver,flip);
		double sizeOfAutoElement = shrinkOfAutoElement * spaceForColumns;
		double whitespace = size.getWidth(flip) - spaceForColumns;
		double left = 0;
		double gapSize = whitespace / nrHGaps(flip) ;
		if(nrHGaps(flip) == 0.0){
			gapSize = 0.0;
		}
		if(getHStartGapProperty(flip)){
			left+=gapSize*0.5;
		}
		for(int column = 0 ; column < getNrColumns(flip) ; column++){
			columnBorders.getForX(flip)[column]=left;
			Size s = columnsSize.getForX(flip)[column];
			double colWidth = 0;
			switch(s.sizeInfo){
				case ALL_SHRINK_SET : colWidth = s.maxShrink * spaceForColumns; break;
				case SOME_SHRINK_SET: colWidth = Math.max(sizeOfAutoElement,s.maxShrink * spaceForColumns); break;
				case NONE_SHRINK_SET: colWidth =sizeOfAutoElement ; break;
				case UNRESIZABLE: colWidth = s.minSize; break;
			}
			
			for(int row = 0 ; row < getNrRows(flip); row++){
				Figure elem = getFigureFromMatrix(flip,row,column);
				if(elem.isHShrinkPropertySet(flip)){
					elem.takeDesiredWidth(flip, elem.getHShrinkProperty(flip)*spaceForColumns);
				} else {
					elem.takeDesiredWidth(flip,sizeOfAutoElement);
				}
				double margin =Math.max(0.0,(colWidth- elem.size.getWidth(flip))  * elem.getHAlignProperty(flip)) ;
				setXPos(flip, row, column, left + margin);
			}
			left+=gapSize + colWidth;
		}
		size.setWidth(flip, left - gapSize);
	}

	double nrHGaps(boolean flip) {
		double nrGaps = getNrColumns(flip)-1 ;
		if(getHStartGapProperty(flip)){
			nrGaps+=0.5;
		} 
		if(getHEndGapProperty(flip)){
			nrGaps+=0.5;
		}
		return nrGaps;
	}
	
	int getNrColumns(boolean flip){
		if(flip) return nrRows;
		else return nrColumns;
	}
	
	int getNrRows(boolean flip){
		if(flip) return nrColumns;
		else return nrRows;
	}
	


	private Figure getFigureFromMatrix(boolean flip, int row, int collumn){
		if(flip) return figureMatrix[collumn][row];
		else return figureMatrix[row][collumn];
	}


	
	
	private void setXPos(boolean flip, int row, int collumn,double val){
		int r,c;
		if(flip){
			r = collumn;
			c = row;
		} else {
			r = row;
			c = collumn;
		}
		pos[r][c].setX(flip, val);
	}
	

	public void setLocationOfChildren(){
		for(int row = 0 ; row < figureMatrix.length ; row++){
			for(int column = 0 ; column < figureMatrix[0].length ; column++){
				figureMatrix[row][column].globalLocation.set(globalLocation);
				figureMatrix[row][column].globalLocation.add(pos[row][column]);
				figureMatrix[row][column].setLocationOfChildren();
			}
		}
	}

	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		if(!mouseInside(c.getX(), c.getY())) return false;
		int row = Util.binaryIntervalSearch(columnBorders.getForY(), c.getY() - getTop());
		int column = Util.binaryIntervalSearch(columnBorders.getForX(), c.getX() - getLeft());
		//System.out.printf("row %d  col %d",row, column);
		if(row >= 0 && column >= 0){
			figureMatrix[row][column].getFiguresUnderMouse(c, result);
		}
		result.add(this);
		return true;
	}
	
	@Override
	public
	void draw(GraphicsContext gc){
		if(overConstrained) {
			gc.text(OVERCONSTRAINED_MESSAGE, globalLocation.getX() + 0.5 * size.getWidth() - getTextWidth(OVERCONSTRAINED_MESSAGE),
					globalLocation.getY() + 0.5 * size.getHeight()  - getTextAscent());
			return;
		}
			
		for(int row = 0 ; row < nrRows ; row++){
			for(int collumn = 0 ; collumn < nrColumns ; collumn++){
				figureMatrix[row][collumn].draw(gc);
			}
		}
	}
	
	@Override
	public void drawPart(Rectangle rect,GraphicsContext gc){
		if(overConstrained) {
			gc.text(OVERCONSTRAINED_MESSAGE, globalLocation.getX() + 0.5 * size.getWidth() - getTextWidth(OVERCONSTRAINED_MESSAGE),
					globalLocation.getY() + 0.5 * size.getHeight() - getTextAscent());
			return;
		}
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
					figureMatrix[row][collumn].drawPart(rect,gc);
				} else {
					figureMatrix[row][collumn].draw(gc);
				}
			}
		}
	}
	

	public void init(){
		super.init();
		for(Figure[] row : figureMatrix){
			for(Figure fig : row){
				fig.init();
			}
		}
	}
	
	public void computeFiguresAndProperties(ICallbackEnv env) {
		super.computeFiguresAndProperties(env);
		for(Figure[] row : figureMatrix){
			for(Figure fig : row){
				fig.computeFiguresAndProperties(env);
			}
		}
	}
	
	public void registerNames(NameResolver resolver){
		super.registerNames(resolver);
		for(Figure[] row : figureMatrix){
			for(Figure fig : row){
				fig.registerNames(resolver);
			}
		}
	}
	

	public void registerValues(NameResolver resolver){
		super.registerValues(resolver);
		for(Figure[] row : figureMatrix){
			for(Figure fig : row){
				fig.registerValues(resolver);
			}
		}
	}
	

	public void getLikes(NameResolver resolver){
		super.getLikes(resolver);
		for(Figure[] row : figureMatrix){
			for(Figure fig : row){
				fig.getLikes(resolver);
			}
		}
	}
	
	public void finalize(){
		super.finalize();
		for(Figure[] row : figureMatrix){
			for(Figure fig : row){
				fig.finalize();
			}
		}
	}
	
	public void destroy(){
		super.destroy();
		for(Figure[] row : figureMatrix){
			for(Figure fig : row){
				fig.destroy();
			}
		}
	}
	
	public void setSWTZOrder(ISWTZOrdering zorder){
		for(Figure[] row : figureMatrix){
			for(Figure fig : row){
				fig.setSWTZOrder(zorder);
			}
		}
	}
	

	public boolean isVisible(){
		for(Figure[] row : figureMatrix){
			for(Figure fig : row){
				if(fig.isVisible()){
					return true;
				}
			}
		}
		return false;
	}
	
}
