package org.meta_environment.rascal.std.Chart;

import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
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
		"rangeLabel",  
		"stacked",
		"subtitle",
		"vertical",
	};
	
	private static IList categories;
	
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
	
	private static void getCategories(IList categories){
 		BarChart.categories = categories;
	}
	
	private static String getCategoryLabel(int i){
		return (i < categories.length()) ? ((IString)categories.get(i)).getValue() : "Cat " + String.valueOf(i);
	}

    /**
     * Transform an IValue into a dataset
     *
     * @return The dataset.
     */
	
	private static CategoryDataset createDataset(IMap facts) {
    	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    	
        Iterator<Entry<IValue,IValue>> iter = facts.entryIterator();
        while (iter.hasNext()) {
    	    Entry<IValue,IValue> entry = iter.next();
    	    String v = ((IString) entry.getKey()).getValue();
    	    setValue(dataset, entry.getValue(), "default", v);
        }
        return dataset;
     } 

    private static CategoryDataset createDataset(IList facts, IList categories) {
    	DefaultCategoryDataset dataset = new DefaultCategoryDataset();
    	
 		getCategories(categories);         			
 		for(IValue v : facts){
 			ITuple tup = (ITuple) v;
 			String name = ((IString) tup.get(0)).getValue();
 			IList elms = (IList) tup.get(1);
 			int nelms = elms.length();
 			for(int i = 0; i < nelms; i++){
 				setValue(dataset, elms.get(i), name, getCategoryLabel(i));
 			}
 		}
 		return dataset;
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
	            true,                      // include legend
	            true,                      // include tooltips
	            false                    	// no URLs
	        );
    	} else if(Settings.has("dim3") && !Settings.has("stacked")){
	        chart = ChartFactory.createBarChart3D(
	            title,                    	// chart title
	            domainLabel,              	// domain axis label
	            rangeLabel,               	// range axis label
	            dataset,                  	// data
	            orientation,             	// orientation
	            true,                      // include legend
	            true,                      // include tooltips
	            false                    	// no URLs
	        );
    	} else if(!Settings.has("dim3") && Settings.has("stacked")){
	        chart = ChartFactory.createStackedBarChart(
	            title,                    	// chart title
	            domainLabel,              	// domain axis label
	            rangeLabel,               	// range axis label
	            dataset,                  	// data
	            orientation,             	// orientation
	            true,                      // include legend
	            true,                      // include tooltips
	            false                    	// no URLs
	        );
        } else	{
	        chart = ChartFactory.createBarChart(
	            title,                    	// chart title
	            domainLabel,              	// domain axis label
	            rangeLabel,               	// range axis label
	            dataset,                  	// data
	            orientation,             	// orientation
	            true,                      // include legend
	            true,                      // include ooltips
	            false                    	// no URLs
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
    
    public static JFreeChart makeBarchart(IString title, IMap facts, IValue settings){
    	Settings.validate(supportedSettings, (IList)settings);
    	return createChart(title.getValue(), createDataset(facts));
    }
    
    public static JFreeChart makeBarchart(IString title, IList categories, IList facts, IValue settings){
    	Settings.validate(supportedSettings, (IList)settings);
    	return createChart(title.getValue(), createDataset(facts, categories));
    }
    
    /**
     * The barchart library function
     *
     * @param title title of the chart
     * @param facts the data (a map)
     */
    public static void barChart(IString title, IMap facts, IValue settings)
    {
    	DisplayChart dc = new DisplayChart(title.getValue(), makeBarchart(title, facts, settings));
    	dc.run();
    }
    
    public static void barChart(IString title, IList categories, IList facts, IValue settings)
    {
    	DisplayChart dc = new DisplayChart(title.getValue(), makeBarchart(title, categories, facts, settings));
    	dc.run();
    }

}

