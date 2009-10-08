package org.meta_environment.rascal.std.Chart;

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
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

/**
 * BarChart functions for Rascal library
 */
public class BarChart {

	private static String[] supportedSettings = {
		"dim3",
		"domainLabel", 
		"horizontal",
		"noLegend",
		"noToolTips",
		"rangeLabel",  
		"seriesLabels",
		"stacked",
		"subtitle",
		"vertical",
	};

	/*
	 * Add value to a dataset
	 */
	private static void setValue(DefaultCategoryDataset dataset, IValue val, String series, String category){
	    if(val.getType().isIntegerType()){
	    	dataset.setValue(((IInteger) val).intValue(), series, category);
	    } else if (val.getType().isRealType()){
	    	dataset.setValue(((IReal) val).floatValue(), series, category);
	    } else {
	    	throw RuntimeExceptionFactory.illegalArgument(val, null,null);
	    } 	
	}
	
	/*
	 * Get the possible categories (= column names)
	 */
	
	private static String[] getCategories(Type t){
 		int nField = t.getArity();
 		
 		String [] categories = new String[nField];
 		for(int i = 1; i < nField; i++){
 			String fname = t.getFieldName(i);
 			categories[i] = (fname != null) ? fname : ("Series " + Integer.toString(i));
 		}
 		if(Settings.has("seriesLabels")){
 			String[] setCategories = Settings.getListString();
 			for(int i = 0; i < setCategories.length && i < nField; i++){
 				categories[i+1] = setCategories[i];
 			}
 		}
 		return categories;
	}

    /**
     * Transform an IValue into a dataset
     *
     * @return The dataset.
     */
    
    private static CategoryDataset createDataset(IValue facts) {
    	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    	
    	 if(facts.getType().isMapType()){
         	IMap factMap = (IMap) facts;
 	        Iterator<Entry<IValue,IValue>> iter = factMap.entryIterator();
 	        while (iter.hasNext()) {
 	    	    Entry<IValue,IValue> entry = iter.next();
 	    	    String v = ((IString) entry.getKey()).getValue();
 	    	    setValue(dataset, entry.getValue(), "default", v);
 	        }
 	        return dataset;
         } else if(facts.getType().isListType()){
         	IList factList = (IList) facts;
         	if(factList.getElementType().isTupleType()){
         		String[] categories = getCategories(factList.getElementType());         			
         		for(IValue v : factList){
         			ITuple tup = (ITuple) v;
         			for(int i = 1; i < tup.arity(); i++){
         				setValue(dataset, tup.get(i), ((IString) tup.get(0)).getValue(), categories[i]);
         			}
         		}
         		return dataset;
         	}
         } else if(facts.getType().isSetType()){
         	ISet factSet = (ISet) facts;
         	if(factSet.getElementType().isTupleType()){
         		String[] categories = getCategories(factSet.getElementType());   
         		for(IValue v : factSet){
         			ITuple tup = (ITuple) v;
         			for(int i = 1; i < tup.arity(); i++){
         				setValue(dataset, tup.get(i), ((IString) tup.get(0)).getValue(), categories[i]);
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
     * @param dataset  the dataset.
     *
     * @return The chart.
     */
    private static JFreeChart createChart(java.lang.String title, 	CategoryDataset dataset) {
    	
    	String domainLabel = Settings.has("domainLabel") ? Settings.getString() : "";
    	String rangeLabel = Settings.has("rangeLabel") ? Settings.getString() : "";
    	PlotOrientation orientation = Settings.has("horizontal") ?  PlotOrientation.HORIZONTAL :  PlotOrientation.VERTICAL;

    	JFreeChart chart;
    	
        // create the chart...
    	
    	if(Settings.has("dim3") && Settings.has("stacked")){
	        chart = ChartFactory.createStackedBarChart3D(
	            title,                    	// chart title
	            domainLabel,              	// domain axis label
	            rangeLabel,               	// range axis label
	            dataset,                  	// data
	            orientation,             	// orientation
	            !Settings.has("noLegend"),  // include legend
	            !Settings.has("noToolTips"),//tooltips?
	            false                    	// URLs?
	        );
    	} else if(Settings.has("dim3") && !Settings.has("stacked")){
	        chart = ChartFactory.createBarChart3D(
	            title,                    	// chart title
	            domainLabel,              	// domain axis label
	            rangeLabel,               	// range axis label
	            dataset,                  	// data
	            orientation,             	// orientation
	            !Settings.has("noLegend"),  // include legend
	            !Settings.has("noToolTips"),//tooltips?
	            false                    	// URLs?
	        );
    	} else if(!Settings.has("dim3") && Settings.has("stacked")){
	        chart = ChartFactory.createStackedBarChart(
	            title,                    	// chart title
	            domainLabel,              	// domain axis label
	            rangeLabel,               	// range axis label
	            dataset,                  	// data
	            orientation,             	// orientation
	            !Settings.has("noLegend"),  // include legend
	            !Settings.has("noToolTips"),//tooltips?
	            false                    	// URLs?
	        );
        } else	{
	        chart = ChartFactory.createBarChart(
	            title,                    	// chart title
	            domainLabel,              	// domain axis label
	            rangeLabel,               	// range axis label
	            dataset,                  	// data
	            orientation,             	// orientation
	            !Settings.has("noLegend"),  // include legend
	            !Settings.has("noToolTips"),//tooltips?
	            false                    	// URLs?
	        );
		}
    	
    	if(Settings.has("subtitle")){
    		Common.setSubtitle(chart, Settings.getString());
    	}

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...

        // get a reference to the plot for further customisation...
        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        
        plot.setBackgroundPaint(Settings.LighterGrey);

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

      //  CategoryAxis domainAxis = plot.getDomainAxis();
      //  domainAxis.setCategoryLabelPositions(
      //          CategoryLabelPositions.createUpRotationLabelPositions(
      //                  Math.PI / 6.0));
        // OPTIONAL CUSTOMISATION COMPLETED.

        return chart;

    }
    
    /*
     * makeBarchart: a reusable function to a create a barchart
     */
    
    public static JFreeChart makeBarchart(IString title, IValue facts, IValue settings){
    	Settings.validate(supportedSettings, (IList)settings);
    	return createChart(title.getValue(), createDataset(facts));
    }
    
    /**
     * The barchart library function
     *
     * @param title title of the chart
     * @param facts the data (a map)
     */
    public static void barChart(IString title, IValue facts, IValue settings)
    {
    	DisplayChart dc = new DisplayChart(title.getValue(), makeBarchart(title, facts, settings));
    	dc.run();
    }

}

