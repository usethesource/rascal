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
	ForBothDimensions<double[]> columnsIWidth;
	ForBothDimensions<double[]> columnsMinWidth;
	ForBothDimensions<double[]> columnsNegMinWidth;
	ForBothDimensions<boolean[]> columnsResizeableX;
	ForBothDimensions<Boolean> anyColumnResizeable;
	ForBothDimensions<Double> totalMinWidthCollumns;
	ForBothDimensions<Double> unresizableColumnsWidth;
	ForBothDimensions<Double> autoColumnsMaxMinWidth;
	ForBothDimensions<Integer> nrAutoColumns;
	ForBothDimensions<Double> totalColumnsShrink;
	
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
		columnsIWidth  = new ForBothDimensions<double[]>(new double[nrColumns], new double[nrRows]) ;
		columnsMinWidth  = new ForBothDimensions<double[]>(new double[nrColumns], new double[nrRows]) ;
		columnsNegMinWidth  = new ForBothDimensions<double[]>(new double[nrColumns], new double[nrRows]) ;
		columnsResizeableX = new ForBothDimensions<boolean[]>(new boolean[nrColumns], new boolean[nrRows]) ;
		anyColumnResizeable = new ForBothDimensions<Boolean>(false, false);
		totalMinWidthCollumns = new ForBothDimensions<Double>(0.0,0.0);
		unresizableColumnsWidth = new ForBothDimensions<Double>(0.0,0.0);
		autoColumnsMaxMinWidth = new ForBothDimensions<Double>(0.0,0.0);
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
		columnsNegMinWidth.setForX(flip,columnsNegMinWidth(flip));
		nrAutoColumns.setForX(flip, nrAutoColumns(flip));
		columnsMinWidth.setForX(flip,columnsMinWidth(flip));
		autoColumnsMaxMinWidth.setForX(flip, autoColumnsMaxMinWidth(flip));
		setAutoCollumnsToSameWidth(flip);
		//System.out.printf("auto col min width %f\n",autoColumnsMinWidth.getForX(flip));
		columnsIWidth.setForX(flip, columnsIWidth(flip));
		unresizableColumnsWidth.setForX(flip, unresizableColumnsWidth(flip));
		columnsResizeableX.setForX(flip, columnsResizeableX(flip));
		
		totalColumnsShrink.setForX(flip,totalColumnsShrink(flip));
		
		
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
		double nonShrinkWidth = autoColumnsMaxMinWidth.getForX(flip) * nrAutoColumns.getForX(flip)+ unresizableColumnsWidth.getForX(flip);
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
		double extraSpaceForAutoColumns = spaceForAutoColumns - (autoColumnsMaxMinWidth.getForX(flip) *  nrAutoColumns.getForX(flip));
		double extraSpacePerAutoColumn = extraSpaceForAutoColumns / nrAutoColumns.getForX(flip);
		if(nrAutoColumns.getForX(flip) == 0){
			extraSpacePerAutoColumn = 0.0;
		}
		double whitespace = size.getWidth(flip) - spaceForColumns;
		
		
		double left = 0;
		if(nrAutoColumns.getForX(flip) == 0 && totalColumnsShrink.getForX(flip) < 1.0 ){
			if(getNrColumns(flip) == 1 &&spaceForUnresizableColumns == 0.0){
				left+= getHAlignProperty(flip) * (1.0 - totalColumnsShrink.getForX(flip)) * (spaceForColumns - spaceForUnresizableColumns);
			} else {
				//System.out.printf("Extra whitespace %f %f %f\n",spaceForColumns,(1.0 - totalColumnsShrink.getForX(flip)), (1.0 - totalColumnsShrink.getForX(flip)) *(spaceForColumns - spaceForUnresizableColumns) );
				// TODO figure this out	
				whitespace+= (1.0 - totalColumnsShrink.getForX(flip))*0.5 *(spaceForColumns - spaceForUnresizableColumns);
			}
		}
		double gapSize = whitespace / (double)nrHGaps(flip) ;
		if(nrHGaps(flip) == 0.0){
			gapSize = 0.0;
		}
		if(getHStartGapProperty(flip)){
			left+=gapSize*0.5;
		}
		
		
		for(int column = 0 ; column < getNrColumns(flip) ; column++){
			double columnWidth;
			if(columnsIWidth.getForX(flip)[column] == AUTO_SIZE){	
				 if(!columnsResizeableX.getForX(flip)[column]){
					 columnWidth = columnsMinWidth.getForX(flip)[column];
				 } else {
					 //System.out.printf("col min width %f \n",extraSpacePerAutoColumn);
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
		//System.out.printf("left %f\n", left);
		
	}
	
	private void layoutColumn(boolean flip, int column,double left, double iWidth, double columnWidth){
		
		for(int row = 0 ; row < getNrRows(flip); row++){
			Figure elem = getFigureFromMatrix(flip,row,column);
			double iiWidth ; 
			double offset = 0; // -columnsNegMinWidth.getForX(flip)[column];
			if(iWidth == AUTO_SIZE){
				iiWidth = 1.0;
			} else {
				iiWidth = elem.getHShrinkProperty(flip) / iWidth;
			}
			double desiredWidth = iiWidth * columnWidth;
			
			elem.takeDesiredWidth(flip, desiredWidth);
			double margin = columnWidth - elem.size.getWidth(flip) ;
			//System.out.printf("MARGIN %f %f %s %s %f %f %f,%f\n", offset, margin * elem.getHAlignProperty(flip),elem,flip,columnWidth - desiredWidth,columnWidth,iiWidth,desiredWidth);
			setXPos(flip, row, column, offset + left + margin * elem.getHAlignProperty(flip));
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
	
	double autoColumnsMaxMinWidth(boolean flip){
		double maxMinWidth = 0;
		for(int column = 0 ; column < getNrColumns(flip); column++){
			if(columnsResizeableX.getForX(flip)[column] 
			    && columnsIWidth.getForX(flip)[column] == AUTO_SIZE){
				maxMinWidth = Math.max(maxMinWidth, columnsMinWidth.getForX(flip)[column]);
			}
		}
		//System.out.printf("Auto collumns min width %f %f\n",  maxMinWidth * nrAutoColumns(flip),maxMinWidth);
		return maxMinWidth ;
	}
	
	void setAutoCollumnsToSameWidth(boolean flip){
		for(int column = 0 ; column < getNrColumns(flip); column++){
			if(columnsResizeableX.getForX(flip)[column] 
			    && columnsIWidth.getForX(flip)[column] == AUTO_SIZE){
				columnsMinWidth.getForX(flip)[column] = autoColumnsMaxMinWidth.getForX(flip);
			}
		}
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
		return result + columnsNegMinWidth.getForX(flip)[collumn] ;
	}
	
	private double columnNegMinWidth(boolean flip,int collumn){
		// for when height is set to negative in chart
		double result = 0;
		for(int row = 0 ; row < getNrRows(flip); row++){
			Figure fig = getFigureFromMatrix(flip, row, collumn);
			result = Math.max(result,-fig.minSize.getWidth(flip));
		}
		return result ;
	}
	

	private double[] columnsMinWidth(boolean flip){
		double[] result = new double[getNrColumns(flip)];
		for(int collumn = 0 ; collumn < getNrColumns(flip); collumn++){
			result[collumn]= columnMinWidth(flip, collumn);
		}
		return result;
	}
	
	private double[] columnsNegMinWidth(boolean flip){
		double[] result = new double[getNrColumns(flip)];
		for(int collumn = 0 ; collumn < getNrColumns(flip); collumn++){
			result[collumn]= columnNegMinWidth(flip, collumn);
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
