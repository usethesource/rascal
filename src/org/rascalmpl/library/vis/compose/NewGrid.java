package org.rascalmpl.library.vis.compose;

import java.util.Vector;

import org.rascalmpl.library.vis.EmptyFigure;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.ForBothDimensions;
import org.rascalmpl.library.vis.util.Util;


public class NewGrid extends Figure {
	
	Figure[][] figureMatrix ;
	Coordinate[][] pos;
	int nrColumns, nrRows;
	ForBothDimensions<double[]> columnBorders;
	ForBothDimensions<double[]> columnsIWidth;
	ForBothDimensions<double[]> columnsMinWidth;
	ForBothDimensions<boolean[]> columnsResizeableX;
	ForBothDimensions<Boolean> anyColumnResizeable;
	ForBothDimensions<Double> totalMinWidthCollumns;
	ForBothDimensions<Double> unresizableColumnsWidth;
	ForBothDimensions<Double> autoColumnsMinWidth;
	ForBothDimensions<Integer> nrAutoColumns;
	ForBothDimensions<Double> totalColumnsShrink;
	
	public NewGrid(IFigureApplet fpa, Figure[][] figureMatrix,
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
		columnsIWidth  = new ForBothDimensions<double[]>(new double[nrColumns], new double[nrRows]) ;
		columnsMinWidth  = new ForBothDimensions<double[]>(new double[nrColumns], new double[nrRows]) ;
		columnsResizeableX = new ForBothDimensions<boolean[]>(new boolean[nrColumns], new boolean[nrRows]) ;
		anyColumnResizeable = new ForBothDimensions<Boolean>(false, false);
		totalMinWidthCollumns = new ForBothDimensions<Double>(0.0,0.0);
		unresizableColumnsWidth = new ForBothDimensions<Double>(0.0,0.0);
		autoColumnsMinWidth = new ForBothDimensions<Double>(0.0,0.0);
		nrAutoColumns = new ForBothDimensions<Integer>(0,0);
		totalColumnsShrink = new ForBothDimensions<Double>(0.0,0.0);
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
		super.bbox();
	}
	

	public void computeMinWidth(boolean flip){
		autoColumnsMinWidth.setForX(flip, autoColumnsMinWidth(flip));
		columnsIWidth.setForX(flip, columnsIWidth(flip));
		unresizableColumnsWidth.setForX(flip, unresizableColumnsWidth(flip));
		
		nrAutoColumns.setForX(flip, nrAutoColumns(flip));
		totalColumnsShrink.setForX(flip,totalColumnsShrink(flip));
		columnsMinWidth.setForX(flip,columnsMinWidth(flip));
		columnsResizeableX.setForX(flip, columnsResizeableX(flip));
		anyColumnResizeable.setForX(flip,Util.any(columnsResizeableX.getForX(flip)));
		totalMinWidthCollumns.setForX(flip, totalMinWidthCollumns(flip));
		//System.out.printf("totalMinWidthCollumns %f%s\n",totalMinWidthCollumns.getForX(flip),flip);
		minSize.setWidth(flip, totalMinWidthCollumns.getForX(flip) *  getHGrowProperty(flip));
		setResizableX(flip, !(getNrColumns(flip) == 1 &&  getNrRows(flip) != 0 && !anyColumnResizeable.getForX(flip)));
	}
	
	double totalMinWidthCollumns(boolean flip){
		double totalMinWidth = 0.0;
		
		for(int column = 0; column < getNrColumns(flip) ; column++){
			double columnMaxIWidth = columnsIWidth.getForX(flip)[column];
			for(int row = 0; row < getNrRows(flip); row++){
				Figure fig = getFigureFromMatrix(flip, row, column);
				if(columnMaxIWidth != AUTO_SIZE){
					if(fig.isHShrinkPropertySet(flip)){
						totalMinWidth = Math.max(totalMinWidth,fig.minSize.getWidth(flip) * fig.getHShrinkProperty(flip));
					} else {
						totalMinWidth = Math.max(totalMinWidth,fig.minSize.getWidth(flip) * columnMaxIWidth);
					}
				} 
			}
		}
		double nonShrinkWidth = autoColumnsMinWidth.getForX(flip) + unresizableColumnsWidth.getForX(flip);
		return Math.max(totalMinWidth,nonShrinkWidth / (1.0 - totalColumnsShrink.getForX(flip)));
		//System.out.printf("MinSize %f %s\n", Math.max(totalMinWidth,elementsWidth),flip);
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
		if(anyColumnResizeable.getForX(flip)){
			spaceForColumns = size.getWidth(flip) / getHGrowProperty(flip);
		} else {
			spaceForColumns = totalMinWidthCollumns.getForX(flip);
		}
		double spaceForSetShrinkColumns = spaceForColumns * totalColumnsShrink.getForX(flip);
		double spaceForOtherColumns = spaceForColumns - spaceForSetShrinkColumns;
		double spaceForUnresizableColumns = unresizableColumnsWidth.getForX(flip);
		double spaceForAutoColumns = spaceForOtherColumns - spaceForUnresizableColumns;
		double extraSpaceForAutoColumns = spaceForAutoColumns - autoColumnsMinWidth.getForX(flip);
		double extraSpacePerAutoColumn = extraSpaceForAutoColumns / nrAutoColumns.getForX(flip);
		//System.out.printf("set desired width: %f",autoColumnsMinWidth.getForX(flip));
		double whitespace = size.getWidth(flip) - spaceForColumns;
		double gapSize = whitespace / (double)nrHGaps(flip) ;
		double left = 0;
		if(getHStartGapProperty(flip)){
			left+=gapSize*0.5;
		}
		
		for(int column = 0 ; column < getNrColumns(flip) ; column++){
			double columnWidth;
			if(columnsIWidth.getForX(flip)[column] == AUTO_SIZE){
				 if(!columnsResizeableX.getForX(flip)[column]){
					 columnWidth = columnsMinWidth.getForX(flip)[column];
				 } else {
					 columnWidth = columnsMinWidth.getForX(flip)[column] + extraSpacePerAutoColumn;
				 }
			} else {
				columnWidth = spaceForColumns * columnsIWidth.getForX(flip)[column];
			}
			columnBorders.getForX(flip)[column]=left;
			//System.out.printf("col %s %d %f %f\n", flip , column, columnsIWidth.getForX(flip)[column], columnWidth);
			layoutColumn(flip,column,left,columnsIWidth.getForX(flip)[column],columnWidth);
			left+=gapSize + columnWidth;
		}
		
	}
	
	private void layoutColumn(boolean flip, int column,double left, double iWidth, double columnWidth){
		
		for(int row = 0 ; row < getNrRows(flip); row++){
			Figure elem = getFigureFromMatrix(flip,row,column);
			double iiWidth ; 
			if(iWidth == AUTO_SIZE){
				iiWidth = 1.0;
			} else {
				iiWidth = elem.getHShrinkProperty(flip) / iWidth;
			}
			double desiredWidth = iiWidth * columnWidth;
			
			elem.takeDesiredWidth(flip, desiredWidth);
			
			double margin = columnWidth - elem.size.getWidth(flip) ;
			//System.out.printf("set width %f %s %d\n", elem.size.getWidth(flip),flip,column);
			setXPos(flip, row, column, left + margin * elem.getHAlignProperty(flip));
		}
	}
	

	double unresizableColumnsWidth(boolean flip){
		double total = 0.0;
		for(int column = 0 ; column < getNrColumns(flip); column++){
			if(!columnsResizeableX.getForX(flip)[column] && columnsIWidth.getForX(flip)[column] == AUTO_SIZE){
				total+= columnsMinWidth.getForX(flip)[column];
			}
		}
		return total;
	}
	
	double autoColumnsMinWidth(boolean flip){
		double total = 0.0;
		for(int column = 0 ; column < getNrColumns(flip); column++){
			if(columnsResizeableX.getForX(flip)[column] 
			    && columnsIWidth.getForX(flip)[column] == AUTO_SIZE){
				total+= columnsMinWidth.getForX(flip)[column];
			}
		}
		return total;
	}
	
	int nrAutoColumns(boolean flip){
		int total = 0;
		for(int column = 0 ; column < getNrColumns(flip); column++){
			if(columnsResizeableX.getForX(flip)[column] 
			    && columnsIWidth.getForX(flip)[column] == AUTO_SIZE){
				total++;
			}
		}
		return total;
	}
	
	double totalColumnsShrink(boolean flip){
		double total = 0;
		for(int column = 0 ; column < getNrColumns(flip); column++){
			if(columnsResizeableX.getForX(flip)[column] 
			    && columnsIWidth.getForX(flip)[column] != AUTO_SIZE){
				total+= columnsIWidth.getForX(flip)[column];
			}
		}
		return total;
	}
	
	/*
	public void newDraw(){
		for(int row = 0 ; row < nrRows ; row++){
			for(int collumn = 0 ; collumn < nrColumns ; collumn++){
				fpa.pushMatrix();
				fpa.translate(pos[row][collumn].getX(), pos[row][collumn].getY());
				figureMatrix[row][collumn].newDraw();
				fpa.popMatrix();
			}
		}
	}
	*/

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

	
	
	private double columnMinWidth(boolean flip,int collumn){
		double result = 0;
		for(int row = 0 ; row < getNrRows(flip); row++){
			Figure fig = getFigureFromMatrix(flip, row, collumn);
			result = Math.max(result,fig.minSize.getWidth(flip));
		}
		return result;
	}
	

	private double[] columnsMinWidth(boolean flip){
		double[] result = new double[getNrColumns(flip)];
		for(int collumn = 0 ; collumn < getNrColumns(flip); collumn++){
			result[collumn]= columnMinWidth(flip, collumn);
		}
		return result;
	}
	


	private double columnIWidth(boolean flip,int collumn){
		double result = AUTO_SIZE;
		for(int row = 0 ; row < getNrRows(flip); row++){
			Figure fig = getFigureFromMatrix(flip, row, collumn);
			if(fig.isHShrinkPropertySet(flip)){
				result = Math.max(result,fig.getHShrinkProperty(flip));
			}
		}
		return result;
	}
	
	private double[] columnsIWidth(boolean flip){
		double[] result = new double[getNrColumns(flip)];
		for(int collumn = 0 ; collumn < getNrColumns(flip); collumn++){
			result[collumn]= columnIWidth(flip, collumn);
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
		if(flip) pos[collumn][row].setX(flip, val);
		else  pos[row][collumn].setX(flip, val);
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
	
	public void computeFiguresAndProperties() {
		super.computeFiguresAndProperties();
		for(Figure[] row : figureMatrix){
			for(Figure fig : row){
				fig.computeFiguresAndProperties();
			}
		}
	}
}
