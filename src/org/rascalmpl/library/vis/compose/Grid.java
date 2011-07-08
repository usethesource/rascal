package org.rascalmpl.library.vis.compose;

import java.util.Vector;

import org.rascalmpl.library.vis.EmptyFigure;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.ForBothDimensions;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.Util;


public class Grid extends Figure {
	
	Figure[][] figureMatrix ;
	Coordinate[][] pos;
	int nrColumns, nrRows;
	ForBothDimensions<double[]> columnBorders;
	ForBothDimensions<Size[]> columnsSize;
	ForBothDimensions<Double> totalShrink;
	ForBothDimensions<Integer> nrAutoColumns;
	ForBothDimensions<Double> unresizableOrAutoColumnsWidth;
	ForBothDimensions<Boolean> anyColumnResizable;
	

	
	static enum SizeInfo{
		SHRINK_SET,
		AUTO_SIZE,
		UNRESIZABLE;
	}
	
	static class Size{
		SizeInfo sizeInfo;
		double minSize;
		double shrink;
		Size(SizeInfo sizeInfo, double minSize,double shrink){
			this.sizeInfo = sizeInfo;
			this.minSize = minSize;
			this.shrink = shrink;
		}
		
		Size(SizeInfo sizeInfo, double minSize){
			this(sizeInfo, minSize,0);
		}
	}
	
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
		nrAutoColumns = new ForBothDimensions<Integer>(0, 0);
		columnsSize = new ForBothDimensions<Grid.Size[]>(new Size[nrColumns], new Size[nrRows]);
		unresizableOrAutoColumnsWidth = new ForBothDimensions<Double>(0.0,0.0);
		totalShrink = new ForBothDimensions<Double>(0.0,0.0);
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
		//System.out.printf("Grid minsize %s\n",minSize);
	}
	

	public void computeMinWidth(boolean flip){
		this.columnsSize.setForX(flip,getSizeInfoOfColumns(flip));
		this.unresizableOrAutoColumnsWidth.setForX(flip, unresizableOrAutoColumnsWidth(flip));
		this.nrAutoColumns.setForX(flip, nrAutoColumns(flip));
		double minSize = 0;
		double totalShrink = 0;
		for(Size s : this.columnsSize.getForX(flip)){
			if(s.sizeInfo == SizeInfo.SHRINK_SET){
				minSize= Math.max(minSize, s.minSize / s.shrink);
				totalShrink+= s.shrink;
			}
		}
		//System.out.printf("Unresizable columns width %f\n",unresizableOrAutoColumnsWidth.getForX(flip));
		if(totalShrink != 1.0){
			minSize = Math.max(minSize, unresizableOrAutoColumnsWidth.getForX(flip) / (1.0 - totalShrink) );
		}
		minSize*= getHGrowProperty(flip);
		anyColumnResizable.setForX(flip, !(nrAutoColumns.getForX(flip) == 0 && totalShrink == 0));
		this.totalShrink.setForX(flip, totalShrink);
		this.minSize.setWidth(flip, minSize);
		
	}
	
	private double unresizableOrAutoColumnsWidth(boolean flip){
		double result = 0;
		Size[] columnSize = this.columnsSize.getForX(flip);
		for(Size s : columnSize){
			if(s.sizeInfo == SizeInfo.UNRESIZABLE || s.sizeInfo == SizeInfo.AUTO_SIZE){
				result += s.minSize;
			}
		}
		return result;
	}
	
	private int nrAutoColumns(boolean flip){
		int result = 0;
		Size[] columnSize = this.columnsSize.getForX(flip);
		for(Size s : columnSize){
			if(s.sizeInfo == SizeInfo.AUTO_SIZE){
				result++;
			}
		}
		return result;
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
		double shrink = 0;
		boolean resizable = false;
		for(int row = 0 ; row < getNrRows(flip); row++){
			Figure fig = getFigureFromMatrix(flip, row, column);
			resizable= resizable || fig.getResizableX(flip);
			if(fig.getResizableX(flip) && fig.isHShrinkPropertySet(flip)){
				shrink = Math.max(shrink, fig.getHShrinkProperty(flip));
			}
			minSize= Math.max(minSize,fig.minSize.getWidth(flip));
		}
		SizeInfo sizeInfo;
		if(!resizable){
			sizeInfo = SizeInfo.UNRESIZABLE;
		} else if(shrink == 0){
			sizeInfo = SizeInfo.AUTO_SIZE;
		} else {
			sizeInfo = SizeInfo.SHRINK_SET;
		}
		return new Size(sizeInfo,minSize,shrink);
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
	
	
	public void layoutX(boolean flip) {
		double spaceForColumns;
		if(!anyColumnResizable.getForX(flip)){
			spaceForColumns = unresizableOrAutoColumnsWidth.getForX(flip);
		} else {
			spaceForColumns = size.getWidth(flip) / getHGrowProperty(flip);
		}
		double spaceLeftOver = (((1.0 - totalShrink.getForX(flip)) * spaceForColumns) 
				- unresizableOrAutoColumnsWidth.getForX(flip));
		double extraSpacePerAutoColumn ;
		//System.out.printf("Space for columns %f nr auto cols %d\n", spaceForColumns, nrAutoColumns.getForX(flip));
		if(nrAutoColumns.getForX(flip) == 0 && totalShrink.getForX(flip) == 0.0){
			spaceForColumns-=spaceLeftOver;
			extraSpacePerAutoColumn = 0.0;
		}  else {
			extraSpacePerAutoColumn = spaceLeftOver / (double)nrAutoColumns.getForX(flip);
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
			columnBorders.getForX(flip)[column]=left;
			Size s = columnsSize.getForX(flip)[column];
			double colWidth = 0;
			switch(s.sizeInfo){
				case SHRINK_SET: colWidth = s.shrink * spaceForColumns; break;
				case AUTO_SIZE : colWidth = s.minSize + extraSpacePerAutoColumn; break;
				case UNRESIZABLE: colWidth = s.minSize; break;
			}
			
			
			for(int row = 0 ; row < getNrRows(flip); row++){
				Figure elem = getFigureFromMatrix(flip,row,column);
				if(elem.isHShrinkPropertySet(flip)){
					elem.takeDesiredWidth(flip, elem.getHShrinkProperty(flip)*spaceForColumns);
				} else {
					elem.takeDesiredWidth(flip,colWidth);
				}
				//System.out.printf("Took width %f %s\n",elem.size.getWidth(flip),elem);
				double margin =Math.max(0.0,(colWidth- elem.size.getWidth(flip))  * elem.getHAlignProperty(flip)) ;
				setXPos(flip, row, column, left + margin);
			}
			left+=gapSize + colWidth;
		}
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
