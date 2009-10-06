package org.meta_environment.rascal.std.Chart;

import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultKeyedValueDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

/**
 * BarChart functions for Rascal library
 */
public class BarChart {

    /**
     * Returns a dataset.
     *
     * @return The dataset.
     */
    private static CategoryDataset createDataset(IList series, IList categories, IList data) {
 
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		int nSeries = series.length();
		int nCategories = categories.length();

		int seriesCnt = -1;
	    Iterator<IValue> iter = data.iterator();
        while (iter.hasNext()) {
        	seriesCnt++;
        	String seriesName = seriesCnt < nSeries ? ((IString)series.get(seriesCnt)).getValue() : "";
        	
    	    IList list = (IList)iter.next();
    	    Iterator<IValue> subiter = list.iterator();
    	    int categoryCnt = -1;
    	    
    	    while(subiter.hasNext()){
    	    	IValue val = subiter.next();
    	    	categoryCnt++;
    	  
    	    	String categoryName = categoryCnt < nCategories ? ((IString)categories.get(categoryCnt)).getValue() : "";
    	    
    	    	if(val.getType().isIntegerType()){
    	    		dataset.setValue(((IInteger) val).intValue(), seriesName, categoryName);
    	    	} else if (val.getType().isRealType()){
    	    		dataset.setValue(((IReal) val).floatValue(), seriesName, categoryName);
    	    	} else {
    	    		throw RuntimeExceptionFactory.illegalArgument(val, null,null);
    	    	} 	
    	    }
        }
        return dataset; 
    }

    /**
     * Creates a sample chart.
     *
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    private static JFreeChart createChart(java.lang.String title, 
    		java.lang.String domainLabel, java.lang.String rangeLabel, 
    		CategoryDataset dataset) {

        // create the chart...
        JFreeChart chart = ChartFactory.createBarChart(
            title,                    // chart title
            domainLabel,              // domain axis label
            rangeLabel,               // range axis label
            dataset,                  // data
            PlotOrientation.VERTICAL, // orientation
            true,                    // include legend
            true,                    // tooltips?
            false                    // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();

        // set the range axis to display integers only...
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        // disable bar outlines...
        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(false);

        // the SWTGraphics2D class doesn't handle GradientPaint well, so
        // replace the gradient painter from the default theme with a
        // standard painter...
        renderer.setBarPainter(new StandardBarPainter());

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryLabelPositions(
                CategoryLabelPositions.createUpRotationLabelPositions(
                        Math.PI / 6.0));
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }
    
    /*
     * makeBarchart: a reusable function to a create a barchart
     */
    
    public static JFreeChart makeBarchart(IString title, IString domainLabel, IString rangeLabel, IList series, IList categories, IList facts){
    	return createChart(title.getValue(), domainLabel.getValue(), rangeLabel.getValue(), createDataset(series, categories, facts));
    }
    
    /**
     * The barchart library function
     *
     * @param title title of the chart
     * @param facts the data (a map)
     */
    public static void barchart(IString title, IString domainLabel, IString rangeLabel, IList series, IList categories, IList facts)
    {
       PieChart.display(title.getValue(), makeBarchart(title, domainLabel, rangeLabel, series, categories, facts));
    }

}

