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
import org.jfree.chart.renderer.xy.StandardXYBarPainter;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.statistics.HistogramDataset;
import org.jfree.data.xy.IntervalXYDataset;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

public class Histogram {
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
     * Converts a Rascal list[str,list[real]] to a HistogramDataset.
     * 
     * @param  facts as Rascal map
     * @return A dataset.
     */
    private static IntervalXYDataset createDataset(IList facts, int nbins) {
        HistogramDataset dataset = new HistogramDataset();

		for(IValue v : facts){
			ITuple tup = (ITuple) v;
			String keyString = ((IString) tup.get(0)).getValue();
			IList elms = (IList) tup.get(1);
			int ndata = elms.length();
			double[] data = new double[ndata];
			for(int i = 0; i < ndata; i++){
				data[i] = getFloat(elms.get(i));
			}
			dataset.addSeries(keyString, data, nbins);
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
    private static JFreeChart createChart(java.lang.String title, IntervalXYDataset dataset) {
     	String domainLabel = Settings.has("domainLabel") ? Settings.getString() : "";
    	String rangeLabel = Settings.has("rangeLabel") ? Settings.getString() : "";
    	PlotOrientation orientation = Settings.has("horizontal") ?  PlotOrientation.HORIZONTAL :  PlotOrientation.VERTICAL;

    	JFreeChart chart = ChartFactory.createHistogram(
		            			title,  						// chart title
		            			domainLabel,
		            			rangeLabel,
		            			dataset,    					// data
		            			orientation,
		            			true,                          // include legend
		            			true,                          // include tooltips
		            			false                          // no URLs
		            		);
    	XYPlot plot = (XYPlot) chart.getPlot();
    	plot.setNoDataMessage("No data available");

    	if(Settings.has("subtitle")){
    		Common.setSubtitle(chart, Settings.getString());
    	}
   	
    	// disable bar outlines...
        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();
        renderer.setDrawBarOutline(true);

        // the SWTGraphics2D class doesn't handle GradientPaint well, so
        // replace the gradient painter from the default theme with a
        // standard painter...
        renderer.setBarPainter(new StandardXYBarPainter());
        
    	plot.setBackgroundPaint(Settings.LighterGrey); 

    	//plot.setForegroundAlpha(0.5f);
    	return chart;
    }
    
    /*
     * makePiechart: a reusable function to a create a piechart
     */
    
    public static JFreeChart makeHistogram(IString title, IList facts, IInteger nbins, IValue settings){
    	String titleString = title.getValue();
    	Settings.validate(provides, (IList)settings);
    	return createChart(titleString, createDataset(facts, nbins.intValue()));
    }
    
    /**
     * The histogram library function
     *
     * @param title title of the chart
     * @param facts the data (a map)
     */
    public static void histogram(IString title, IList facts, IInteger nbins, IValue settings)
    {
    	DisplayChart dc = new DisplayChart(title.getValue(), makeHistogram(title, facts, nbins, settings));
    	dc.run();
    }

}
