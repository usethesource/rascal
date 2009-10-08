package org.meta_environment.rascal.std.Chart;

import java.awt.Font;
import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.type.Type;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

/**
 * PieChart functions for Rascal library
 */
public class PieChart {
	private static String[] provides = {
		"dim3", 
		"noSectionLabels", 
		"noLegend", 
		"noToolTips", 
		"ring"
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
     * Converts a Rascal Map to a PieDataset.
     * 
     * @param  facts as Rascal map
     * @return A dataset.
     */
    private static PieDataset createDataset(IValue facts) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        if(facts.getType().isMapType()){
        	IMap factMap = (IMap) facts;
	        Iterator<Entry<IValue,IValue>> iter = factMap.entryIterator();
	        while (iter.hasNext()) {
	    	    Entry<IValue,IValue> entry = iter.next();
	    	    String keyString = ((IString) entry.getKey()).getValue();
	    	    IValue val = entry.getValue();
	    	    dataset.setValue(keyString, getFloat(val));	
	        }
	        return dataset;
        } else if(facts.getType().isListType()){
        	IList factList = (IList) facts;
        	if(facts.getType().getElementType().isTupleType()){
        		Type et = facts.getType().getElementType();
        		if(et.getFieldType(0).isStringType() && et.getArity() > 1){
        			for(IValue v : factList){
        				ITuple tup = (ITuple) v;
        				String keyString = ((IString) tup.get(0)).getValue();
        				dataset.setValue(keyString, getFloat(tup.get(1)));	
        			}
        		} 
        		return dataset;
        	}
        	
        } else if(facts.getType().isSetType()){
        	ISet factList = (ISet) facts;
        	if(facts.getType().getElementType().isTupleType()){
        		Type et = facts.getType().getElementType();
        		if(et.getFieldType(0).isStringType() && et.getArity() > 1){
        			for(IValue v : factList){
        				ITuple tup = (ITuple) v;
        				String keyString = ((IString) tup.get(0)).getValue();
        				dataset.setValue(keyString, getFloat(tup.get(1)));	
        			}
        		}  
        		return dataset;
        	}
        }
        throw RuntimeExceptionFactory.illegalArgument(facts, null,null);
    }
    
    /**
     * Creates a chart.
     * 
     * @param title	    name of the chart
     * @param dataset  the dataset.
     * 
     * @return A chart.
     */
    private static JFreeChart createChart(java.lang.String title, PieDataset dataset) {
    	JFreeChart chart;
    	 PiePlot plot;
    	
    	if(Settings.has("dim3")){
            chart = ChartFactory.createPieChart3D(
            			title,  						// chart title
            			dataset,    					// data
            			!Settings.has("noLegend"),      // include legend
            			!Settings.has("noToolTips"),    // tooltips
            			false);
            plot = (PiePlot3D) chart.getPlot();
    	} else {
        	 chart = ChartFactory.createPieChart(
         				title,  						// chart title
         				dataset,    					// data
         				!Settings.has("noLegend"),      // include legend
         				!Settings.has("noToolTips"),   	// tooltips
         				false);
        	 plot = (PiePlot) chart.getPlot();
    	}
    	
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        
        plot.setBackgroundPaint(Settings.LighterGrey);

    	if(Settings.has("subtitle")){
    		Common.setSubtitle(chart, Settings.getString());
    	}    
        plot.setNoDataMessage("No data available");
        plot.setCircular(Settings.has("circular"));
        plot.setSectionOutlinesVisible(false);
        if(Settings.has("noSectionLabels")){
        	 plot.setInteriorGap(0.0);
             plot.setLabelGenerator(null);
        } else {
        	plot.setLabelGap(0.02);
        }
        plot.setForegroundAlpha(0.5f);
        return chart;
    }
    
    /*
     * makePiechart: a reusable function to a create a piechart
     */
    
    public static JFreeChart makePiechart(IString title, IValue facts, IValue settings){
    	String titleString = title.getValue();
    	Settings.validate(provides, (IList)settings);
    	return createChart(titleString, createDataset(facts));
    }
    
    /**
     * The pieChart library function
     *
     * @param title title of the chart
     * @param facts the data (a map)
     */
    public static void pieChart(IString title, IValue facts, IValue settings)
    {
    	DisplayChart dc = new DisplayChart(title.getValue(), makePiechart(title, facts, settings));
    	dc.run();
    }

}

