package org.meta_environment.rascal.std.Chart;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class XYChart {

	private static String[] supportedSettings = { 
		"area",
		"domainLabel",
		"horizontal",
		"rangeLabel",
		"subtitle",
		"vertical",
	};

	/**
	 * Transform an IValue into a dataset
	 * 
	 * @return The dataset.
	 */

	private static float getFloat(IValue val) {
		if (val.getType().isIntegerType())
			return ((IInteger) val).intValue();
		else if (val.getType().isRealType()) {
			return ((IReal) val).floatValue();
		} else {
			throw RuntimeExceptionFactory.illegalArgument(val, null, null);
		}
	}
	
	private static XYSeriesCollection createAllSeries(IList facts) {
		XYSeriesCollection xyDatasets = new XYSeriesCollection();
		for (IValue v : facts) {
			ITuple tup = (ITuple) v;
			String fun = ((IString) tup.get(0)).getValue();
			IList elms = (IList) tup.get(1);
			int nelems = elms.length();
		
			XYSeries series = new XYSeries(fun);
				
			for(int i = 0; i < nelems - 1; i += 2){  // TODO: last odd element may get lost
				series.add(getFloat(elms.get(i)), getFloat(elms.get(i+1)));
			}
			xyDatasets.addSeries(series);
		}
		return xyDatasets;
	}
	
	 private static JFreeChart createChart(java.lang.String title, XYSeriesCollection datasets) {
		 
	   	String domainLabel = Settings.has("domainLabel") ? Settings.getString() : "";
    	String rangeLabel = Settings.has("rangeLabel") ? Settings.getString() : "";
    	PlotOrientation orientation = Settings.has("horizontal") ?  PlotOrientation.HORIZONTAL :  PlotOrientation.VERTICAL;

    	JFreeChart chart;
    
    	if(Settings.has("area")){
        	 chart = ChartFactory.createXYAreaChart(
         				title,  						// chart title
         				domainLabel,                    // xAxis
         				rangeLabel,                     // yAxis
         				datasets,    					// data sets
         				orientation,                    // hor or vertical orientation
         				true,                          // include legend
         				true,   	                    // include tooltips
         				false                          // no URLs
         			);
    	} else {
    		chart = ChartFactory.createXYLineChart(
     					title,  						// chart title
     					domainLabel,                    // xAxis
     					rangeLabel,                     // yAxis
     					datasets,    					// data sets
     					orientation,					// hor or vertical orientation
     					true,                          // include legend
     					true,   	                    // include tooltips
     					false                          // no URLs
     				);
    	}
    	
    	if(Settings.has("subtitle")){
    		Common.setSubtitle(chart, Settings.getString());
    	}
        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setNoDataMessage("No data available");
        
        plot.setBackgroundPaint(Settings.LighterGrey);
        
        if(!Settings.has("area")){
	        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	        
	        for(int k = 0; k < datasets.getSeriesCount(); k++){
	        	renderer.setSeriesLinesVisible(k, true);
	        	renderer.setSeriesShapesVisible(k, true);
	        }
	        plot.setRenderer(renderer);
        }
        
        return chart;
    }
	  
    /*
     * makeXYChart: a reusable function to a create an XYChart
     */
    
    public static JFreeChart makeXYChart(IString title, IList facts, IValue settings){
    	Settings.validate(supportedSettings, (IList)settings);
    	return createChart(title.getValue(), createAllSeries(facts));
    }
    
    /**
     * The XYChart library function
     *
     * @param title title of the chart
     * @param facts the data (a map)
     */
    public static void xyChart(IString title, IList facts, IValue settings)
    {
    	DisplayChart dc = new DisplayChart(title.getValue(), makeXYChart(title, facts, settings));
    	dc.run();
    }


}
