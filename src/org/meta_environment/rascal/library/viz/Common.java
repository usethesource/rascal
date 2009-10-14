package org.meta_environment.rascal.library.viz;

import java.awt.Font;

import org.jfree.chart.JFreeChart;
import org.jfree.chart.title.TextTitle;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.VerticalAlignment;

public class Common {
	
	public static void setSubtitle(JFreeChart chart, String text){
	 final TextTitle subtitle = new TextTitle(text);
	        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
	        subtitle.setPosition(RectangleEdge.TOP);
//	        subtitle.setSpacer(new Spacer(Spacer.RELATIVE, 0.05, 0.05, 0.05, 0.05));
	        subtitle.setVerticalAlignment(VerticalAlignment.BOTTOM);
	        chart.addSubtitle(subtitle);
	}
}
