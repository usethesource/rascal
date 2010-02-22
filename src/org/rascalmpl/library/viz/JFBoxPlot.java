package org.rascalmpl.library.viz;

import java.util.ArrayList;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.statistics.BoxAndWhiskerCategoryDataset;
import org.jfree.data.statistics.DefaultBoxAndWhiskerCategoryDataset;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;

public class JFBoxPlot {
	private static String[] provides = {
		"domainLabel",
		"horizontal",
		"rangeLabel",
		"subtitle",
		"vertical"
	};
	
	private static float getFloat(IValue val){
		 if(val.getType().isIntegerType()){
 	    	return ((IInteger) val).intValue();
 	    } else if (val.getType().isRealType()){
 	    	return ((IReal) val).floatValue();
 	    } else {
 	    	throw RuntimeExceptionFactory.illegalArgument(val, null,null);
 	    } 		
	}
		
    /**
     * Converts a Rascal 
     * 	alias intSeriesMultipleData  = tuple[str series,list[tuple[str category, list[int] values]]];
     * 	alias realSeriesMultipleData = tuple[str series,list[tuple[str category, list[real] value]]];
     * to a BoxAndWhiskerDataset.
     *
     * @param  facts as Rascal intSeriesMultipleData or realSeriesMultipleData
     * @return A dataset.
     */
    private static BoxAndWhiskerCategoryDataset createDataset(IList facts) {
    	DefaultBoxAndWhiskerCategoryDataset dataset = new DefaultBoxAndWhiskerCategoryDataset();

		for(IValue fact : facts){
			ITuple series = (ITuple) fact;
			String seriesName = ((IString) series.get(0)).getValue();
			IList values = (IList) series.get(1);
			for(IValue value : values){
				ITuple tup = (ITuple) value;
				String categoryName = ((IString) tup.get(0)).getValue();
				IList elms = (IList) tup.get(1);
				int nelms = elms.length();
				java.util.List<Double> data = new ArrayList<Double>();
				for(int i = 0; i < nelms; i++){
					data.add(new Double(getFloat(elms.get(i))));
				}
				dataset.add(data, seriesName, categoryName);
			}
		} 
		return dataset;
	}
    
    /**
     * Creates a histogram chart.
     * 
     * @param title	    name of the chart
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    private static JFreeChart createChart(java.lang.String title, BoxAndWhiskerCategoryDataset dataset) {
     	String domainLabel = JFSettings.has("domainLabel") ? JFSettings.getString() : "";
    	String rangeLabel = JFSettings.has("rangeLabel") ? JFSettings.getString() : "";
    	PlotOrientation orientation = JFSettings.has("horizontal") ?  PlotOrientation.HORIZONTAL :  PlotOrientation.VERTICAL;
    	
    	JFreeChart chart = ChartFactory.createBoxAndWhiskerChart(
    							title,  						// chart title
		            			domainLabel,
		            			rangeLabel,
		            			dataset,    					// data
		            			true                           // include legend
		            		);
    	
    	CategoryPlot plot = (CategoryPlot) chart.getPlot();
    	plot.setNoDataMessage("No data available");

    	if(JFSettings.has("subtitle")){
    		JFCommon.setSubtitle(chart, JFSettings.getString());
    	}
    	plot.setOrientation(orientation);
    	
   	/*
    	// disable bar outlines...
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(true);

        // the SWTGraphics2D class doesn't handle GradientPaint well, so
        // replace the gradient painter from the default theme with a
        // standard painter...
        renderer.setBarPainter(new StandardXYBarPainter());
     */
    	plot.setBackgroundPaint(JFSettings.LighterGrey); 

    	//plot.setForegroundAlpha(0.5f);
    	return chart;
    }
    
    /*
     * makePiechart: a reusable function to a create a boxplot
     */
    
    public static JFreeChart makeBoxPlot(IString title, IList facts, IList settings){
    	String titleString = title.getValue();
    	JFSettings.validate(provides, settings);
    	return createChart(titleString, createDataset(facts));
    }
    
    /**
     * The boxplot library function
     *
     * @param title title of the chart
     * @param facts the data (a map)
     */
    public static void boxplot(IString title, IList facts, IList settings)
    {
    	JFDisplayChart dc = new JFDisplayChart(title.getValue(), makeBoxPlot(title, facts, settings));
    	dc.run();
    }

}
