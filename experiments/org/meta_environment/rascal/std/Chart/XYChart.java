package org.meta_environment.rascal.std.Chart;

import java.awt.Color;
import java.util.HashMap;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
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
		"noLegend",
		"noToolTips",
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

	private static HashMap<String, XYSeries> allSeries;
	
	private static XYSeriesCollection make(){
	
		XYSeriesCollection xyDatasets = new XYSeriesCollection();
		for(String fun : allSeries.keySet()){
			xyDatasets.addSeries(allSeries.get(fun));
		}
		return xyDatasets;
	}

	private static XYSeriesCollection createAllSeries(IValue facts) {

		if (facts.getType().isListType()) {
			IList factList = (IList) facts;
			if (factList.getElementType().isTupleType()) {
				Type t = factList.getElementType();
				if (t.getArity() == 3 && t.getFieldType(0).isStringType()) {

					for (IValue v : factList) {
						ITuple tup = (ITuple) v;
						String fun = ((IString) tup.get(0)).getValue();
						XYSeries series = allSeries.get(fun);
						if (series == null) {
							series = new XYSeries(fun);
							allSeries.put(fun, series);
						}
						series.add(getFloat(tup.get(1)), getFloat(tup.get(2)));
					}
					return make();
				}
			}
		}
		if (facts.getType().isListType()) {
			ISet factSet = (ISet) facts;
			if (factSet.getElementType().isTupleType()) {
				Type t = factSet.getElementType();
				if (t.getArity() == 3 && t.getFieldType(0).isStringType()) {

					for (IValue v : factSet) {
						ITuple tup = (ITuple) v;
						String fun = ((IString) tup.get(0)).getValue();
						XYSeries series = allSeries.get(fun);
						if (series == null) {
							series = new XYSeries(fun);
							allSeries.put(fun, series);
						}
						series.add(getFloat(tup.get(1)), getFloat(tup.get(2)));
					}
					return make();
				} 
			}
		}
		throw RuntimeExceptionFactory.illegalArgument(facts, null, null);
	}
	
	 private static JFreeChart createChart(java.lang.String title, XYSeriesCollection datasets) {
		 
		   	String domainLabel = Settings.has("domainLabel") ? Settings.getString() : "";
	    	String rangeLabel = Settings.has("rangeLabel") ? Settings.getString() : "";
	    	PlotOrientation orientation = Settings.has("horizontal") ?  PlotOrientation.HORIZONTAL :  PlotOrientation.VERTICAL;

	    	JFreeChart chart;
	    	XYPlot plot;
	    
	    	if(Settings.has("area")){
	        	 chart = ChartFactory.createXYAreaChart(
	         				title,  						// chart title
	         				domainLabel,                    // xAxis
	         				rangeLabel,                     // yAxis
	         				datasets,    					// data sets
	         				orientation,                    // hor or vertical orientation
	         				!Settings.has("noLegend"),      // include legend
	         				!Settings.has("noToolTips"),   	// tooltips
	         				false);
	    	} else {
	    		chart = ChartFactory.createXYLineChart(
         					title,  						// chart title
         					domainLabel,                    // xAxis
         					rangeLabel,                     // yAxis
         					datasets,    					// data sets
         					orientation,					// hor or vertical orientation
         					!Settings.has("noLegend"),      // include legend
         					!Settings.has("noToolTips"),   	// tooltips
         					false);
	    	}
	    	
	    	if(Settings.has("subtitle")){
	    		Common.setSubtitle(chart, Settings.getString());
	    	}
	        plot = (XYPlot) chart.getPlot();
	        plot.setNoDataMessage("No data available");
	        
	        plot.setBackgroundPaint(Settings.LighterGrey);
	        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
	        
	        for(int k = 0; k < allSeries.size(); k++){
	        	renderer.setSeriesLinesVisible(k, true);
	        	renderer.setSeriesShapesVisible(k, true);
	        	k++;
	        }
	        plot.setRenderer(renderer);
	        
	        return chart;
	    }
	  
    /*
     * makeXYChart: a reusable function to a create an XYChart
     */
    
    public static JFreeChart makeXYChart(IString title, IValue facts, IValue settings){
    	Settings.validate(supportedSettings, (IList)settings);
    	allSeries = new HashMap<String, XYSeries>();
    	
    	return createChart(title.getValue(), createAllSeries(facts));
    }
    
    /**
     * The XYChart library function
     *
     * @param title title of the chart
     * @param facts the data (a map)
     */
    public static void xyChart(IString title, IValue facts, IValue settings)
    {
    	DisplayChart dc = new DisplayChart(title.getValue(), makeXYChart(title, facts, settings));
    	dc.run();
    }


}
