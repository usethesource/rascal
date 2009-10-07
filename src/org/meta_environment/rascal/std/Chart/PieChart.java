package org.meta_environment.rascal.std.Chart;

import java.awt.Font;
import java.util.Iterator;
import java.util.Map.Entry;

import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.IReal;
import org.eclipse.imp.pdb.facts.IString;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.experimental.chart.swt.ChartComposite;
import org.meta_environment.rascal.interpreter.utils.RuntimeExceptionFactory;

/**
 * PieChart functions for Rascal library
 */
public class PieChart {
    
    /**
     * Converts a Rascal Map to a PieDataset.
     * 
     * @param  facts as Rascal map
     * @return A dataset.
     */
    private static PieDataset createDataset(IMap facts) {
        DefaultPieDataset dataset = new DefaultPieDataset();
        
        Iterator<Entry<IValue,IValue>> iter = facts.entryIterator();
        while (iter.hasNext()) {
    	    Entry<IValue,IValue> entry = iter.next();
    	    String keyString = ((IString) entry.getKey()).getValue();
    	    IValue val = entry.getValue();
    	    if(val.getType().isIntegerType()){
    	    	dataset.setValue(keyString, ((IInteger) val).intValue());
    	    } else if (val.getType().isRealType()){
    	    	dataset.setValue(keyString, ((IReal) val).floatValue());
    	    } else {
    	    	throw RuntimeExceptionFactory.illegalArgument(val, null,null);
    	    } 		
        }
        return dataset;        
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
        
        JFreeChart chart = ChartFactory.createPieChart3D(
            title,  	// chart title
            dataset,    // data
            true,      // include legend
            true,      // tooltips
            false
        );

        PiePlot3D plot = (PiePlot3D) chart.getPlot();
        plot.setSectionOutlinesVisible(false);
        plot.setLabelFont(new Font("SansSerif", Font.PLAIN, 12));
        plot.setNoDataMessage("No data available");
        plot.setCircular(false);
        plot.setLabelGap(0.02);
        plot.setForegroundAlpha(0.5f);
        return chart;
    }
    
    /*
     * makePiechart: a reusable function to a create a piechart
     */
    
    public static JFreeChart makePiechart(IString title, IMap facts){
    	String titleString = title.getValue();
    	return createChart(titleString, createDataset(facts));
    }
    
    /**
     * The piechart library function
     *
     * @param title title of the chart
     * @param facts the data (a map)
     */
    public static void piechart(IString title, IMap facts)
    {
    	DisplayChart dc = new DisplayChart(title.getValue(), makePiechart(title, facts));
    	dc.run();
    }
    
    /**
     * Display a titled chart
     *
     * @param title	   title of the chart
     * @param chart   the actual chart
     */
    static void display(String title, JFreeChart chart)
    {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setSize(600, 400);
        shell.setLayout(new FillLayout());
        shell.setText(title);
        final ChartComposite frame = new ChartComposite(shell, SWT.NONE, chart, true);
        //frame.setDisplayToolTips(false);
        frame.pack();
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch())
                display.sleep();
        }
    }

}

