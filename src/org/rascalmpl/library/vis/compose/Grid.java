package org.rascalmpl.library.vis.compose;

import java.util.Vector;

import org.rascalmpl.library.vis.EmptyFigure;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.BoundingBox;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.ForBothDimensions;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.Util;


public class Grid extends Figure {
	
	Figure[][] figureMatrix ;
	Coordinate[][] pos;
	int nrColumns, nrRows;
	ForBothDimensions<double[]> columnBorders;
	ForBothDimensions<double[]> columnsShrinkWidth;
	ForBothDimensions<boolean[]> columnsResizeableX;
	ForBothDimensions<Double> unresizableColumnsWidth;
	ForBothDimensions<Boolean> anyColumnResizable;
	
	public Grid(IFigureApplet fpa, Figure[][] figureMatrix,
			PropertyManager properties) {
		super(fpa, properties);
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
		
		columnBorders = new ForBothDimensions<double[]>(new double[nrColumns], new double[nrRows]) ;
		columnsShrinkWidth  = new ForBothDimensions<double[]>(new double[nrColumns], new double[nrRows]) ;
		columnsResizeableX = new ForBothDimensions<boolean[]>(new boolean[nrColumns], new boolean[nrRows]) ;
		unresizableColumnsWidth = new ForBothDimensions<Double>(0.0,0.0);
		anyColumnResizable = new ForBothDimensions<Boolean>(false,false);
	}
	
	public void bbox(){
		for(Figure[] row : figureMatrix){
			for(Figure elem : row){
				elem.bbox();
			}
		}
		for(boolean flip : BOTH_DIMENSIONS){
			computeMinWidth(flip);
		}
		setResizable();
		super.bbox();
	}
	

	public void computeMinWidth(boolean flip){
		columnsResizeableX.setForX(flip, columnsResizeableX(flip));
		double[] columnsShrinkWidth = columnsShrinkWidth(flip);
		int nrAutoColumns = Util.count(columnsShrinkWidth, AUTO_SIZE);
		double totalColumnsShrink = Util.sum(columnsShrinkWidth) - nrAutoColumns * AUTO_SIZE;
		double autoColumnsShrink = (1.0 - totalColumnsShrink) / (double)nrAutoColumns;
		Util.replaceVal(columnsShrinkWidth, AUTO_SIZE, autoColumnsShrink);
		this.columnsShrinkWidth.setForX(flip, columnsShrinkWidth);
		double unresizableColumnsWidth = setShrinkWidthOfUnresizableColumnsToMinWidth(flip);
		this.unresizableColumnsWidth.setForX(flip, unresizableColumnsWidth);
		anyColumnResizable.setForX(flip, totalColumnsShrink != 0.0 || nrAutoColumns !=0);
		double minWidth = computeMinWidthOfResizableColumns(flip) + unresizableColumnsWidth;
		minWidth*= getHGrowProperty(flip);
		minSize.setWidth(flip, minWidth);
	}

	private double setShrinkWidthOfUnresizableColumnsToMinWidth(boolean flip){
		double totalUnresizableWidth = 0;
		double[] columnsShrinkWidth = this.columnsShrinkWidth.getForX(flip);
		for(int column = 0 ; column < getNrColumns(flip); column++){
			if(columnsResizeableX.getForX(flip)[column]) continue;
			double minWidth = 0.0;
			for(int row = 0 ; row < getNrRows(flip) ; row++){
				minWidth = Math.max(minWidth,getFigureFromMatrix(flip, row, column).minSize.getWidth(flip));
			}
			columnsShrinkWidth[column] = minWidth;
			totalUnresizableWidth+= minWidth;
		}
		return totalUnresizableWidth;
	}
	
	private double computeMinWidthOfResizableColumns(boolean flip) {
		double minWidth = 0;
		double[] columnsShrinkWidth = this.columnsShrinkWidth.getForX(flip);
		for(int column = 0 ; column < getNrColumns(flip); column++){
			if(!columnsResizeableX.getForX(flip)[column]) continue;
			double columnShrinkWidth = columnsShrinkWidth[column];
			for(int row = 0 ; row < getNrRows(flip) ; row++){
				Figure fig = getFigureFromMatrix(flip, row, column);
				double figShrink = fig.getHShrinkProperty(flip);
				if(figShrink == AUTO_SIZE){
					figShrink = columnShrinkWidth;
				} 
				minWidth += fig.minSize.getWidth(flip)/figShrink;
			}
		}
		return minWidth;
	}
	
	
	
	public void layout(){
		//System.out.printf("Actual size %s\n",size);
		for(boolean flip: BOTH_DIMENSIONS){
			layoutX(flip);
		}
		for(Figure[] row : figureMatrix){
			for(Figure elem : row){
				elem.layout();
			}
		}
	}
	
	
	public void layoutX(boolean flip) {
		double spaceForColumns;
		double spaceForResizableColumns;
		if(!anyColumnResizable.getForX()){
			spaceForColumns = unresizableColumnsWidth.getForX(flip);
			spaceForResizableColumns = 0.0;
		} else {
			spaceForColumns = size.getWidth(flip) / getHGrowProperty(flip);
			spaceForResizableColumns= spaceForColumns - unresizableColumnsWidth.getForX(flip);
		}
		double whitespace = size.getWidth(flip) - spaceForColumns;
		double left = 0;
		double gapSize = whitespace / (double)nrHGaps(flip) ;
		if(nrHGaps(flip) == 0.0){
			gapSize = 0.0;
		}
		if(getHStartGapProperty(flip)){
			left+=gapSize*0.5;
		}
		for(int column = 0 ; column < getNrColumns(flip) ; column++){
			double colWidth;
			if(columnsResizeableX.getForX(flip)[column]){
				//System.out.printf("Column %d resizable! factor %f\n", column, columnsShrinkWidth.getForX(flip)[column]);
				colWidth= spaceForResizableColumns * columnsShrinkWidth.getForX(flip)[column];
			} else {
				colWidth= columnsShrinkWidth.getForX(flip)[column];
			}
			columnBorders.getForX(flip)[column]=left;
			for(int row = 0 ; row < getNrRows(flip); row++){
				Figure elem = getFigureFromMatrix(flip,row,column);
				if(elem.isHShrinkPropertySet(flip)){
					elem.takeDesiredWidth(flip, elem.getHShrinkProperty(flip)*spaceForResizableColumns);
				} else {
					elem.takeDesiredWidth(flip,colWidth);
				}
				//System.out.printf("Took width %f %s\n",elem.size.getWidth(flip),elem);
				double margin =(colWidth- elem.size.getWidth(flip))  * elem.getHAlignProperty(flip) ;
				setXPos(flip, row, column, left + margin);
			}
			left+=gapSize + colWidth;
		}
	}
	
	int nrAutoColumns(boolean flip){
		int total = 0;
		for(int column = 0 ; column < getNrColumns(flip); column++){
			if(columnsResizeableX.getForX(flip)[column] 
			    && columnsShrinkWidth.getForX(flip)[column] == AUTO_SIZE){
				total++;
			}
		}
		return total;
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

	


	private double columnShrinkWidth(boolean flip,int collumn){
		if(!columnResizeableX(flip, collumn)) return 0.0;
		double result = AUTO_SIZE;
		for(int row = 0 ; row < getNrRows(flip); row++){
			Figure fig = getFigureFromMatrix(flip, row, collumn);
			if(fig.isHShrinkPropertySet(flip)){
				result = Math.max(result,fig.getHShrinkProperty(flip));
			}
		}
		return result;
	}
	
	private double[] columnsShrinkWidth(boolean flip){
		double[] result = new double[getNrColumns(flip)];
		for(int collumn = 0 ; collumn < getNrColumns(flip); collumn++){
			result[collumn]= columnShrinkWidth(flip, collumn);
		}
		return result;
	}
	
	private boolean columnResizeableX(boolean flip,int collumn){
		boolean result = false;
		for(int row = 0 ; row < getNrRows(flip); row++){
			Figure fig = getFigureFromMatrix(flip, row, collumn);
			result = result || fig.getResizableX(flip);
		}
		return result;
	}
	
	private boolean[] columnsResizeableX(boolean flip){
		boolean[] result = new boolean[getNrColumns(flip)];
		for(int collumn = 0 ; collumn < getNrColumns(flip); collumn++){
			result[collumn]= columnResizeableX(flip, collumn);
		}
		return result;
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
		figureMatrix[r][c].globalLocation.setX(flip, globalLocation.getX(flip) + val);
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
	void draw(double left, double top){
		setLeft(left);
		setTop(top);
		applyProperties();
		for(int row = 0 ; row < nrRows; row++){
			//fpa.line(left , top + rowCenters[row], left + 5000, top + rowCenters[row]);
		}
		for(int row = 0 ; row < nrColumns; row++){
			//fpa.line(left + collumnCenters[row], top , left + collumnCenters[row], top + 5000);
		}
		for(int row = 0 ; row < nrRows ; row++){
			for(int collumn = 0 ; collumn < nrColumns ; collumn++){
				//System.out.printf("Drawing %d %d at %f %f of width %f height %f\n", row, collumn,left + xPos[row][collumn], top + yPos[row][collumn],desiredWidths[row][collumn],desiredHeights[row][collumn]);
				figureMatrix[row][collumn].draw(left + pos[row][collumn].getX(),top + pos[row][collumn].getY());
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
	
	public void computeFiguresAndProperties() {
		super.computeFiguresAndProperties();
		for(Figure[] row : figureMatrix){
			for(Figure fig : row){
				fig.computeFiguresAndProperties();
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
}
