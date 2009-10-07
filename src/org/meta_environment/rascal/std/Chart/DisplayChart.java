package org.meta_environment.rascal.std.Chart;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.jfree.chart.JFreeChart;
import org.jfree.experimental.chart.swt.ChartComposite;

public class DisplayChart implements Runnable {
	private JFreeChart chart;
	private String title;

	 /**
     * Display a titled chart
     *
     * @param title	   title of the chart
     * @param chart   the actual chart
     */
   public DisplayChart(String title, JFreeChart chart)
    {
	   this.title = title;
	   this.chart = chart;
    }
   
   public void run() {
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
        display.dispose();
    }
}
