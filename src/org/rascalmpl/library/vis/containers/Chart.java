package org.rascalmpl.library.vis.containers;

import java.util.Vector;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.PropertyManager;

public class Chart extends Container {

	boolean xAxisBelow = false;
	boolean yAxisRight = false;
	
	public static class Projection{
		float position;
		Figure fig;
		float gap;
		boolean xaxis;
		float xPos,yPos;
		
		public Projection(float position, float gap,Figure fig, boolean xaxis){
			this.position = position;
			this.gap = gap;
			this.fig = fig;
			this.xaxis = xaxis;
		}
	}
	
	Vector<Projection> projections; 
	
	
	public Chart(IFigureApplet fpa, PropertyManager properties,
			IConstructor innerCons, IList childProps, IEvaluatorContext ctx) {
		super(fpa, properties, innerCons, childProps, ctx);
		projections = new Vector<Projection>();
	}


	

	@Override
	void drawContainer() {
		

	}

	@Override
	String containerName() {
		return "chart";
	}

	@Override
	public void bbox(float desiredWidth, float desiredHeight) {
		innerFig.bbox(desiredWidth, desiredHeight);
		width = innerFig.width;
		height = innerFig.height;
		innerFigX = innerFigY = 0.0f;
		System.err.printf("Gathering projections");
		innerFig.gatherProjections(0.0f, 0.0f, projections);
		for(Projection p : projections){
			p.fig.bbox(AUTO_SIZE,AUTO_SIZE);
			if(p.xaxis){
				if(xAxisBelow){
					p.xPos = p.position - p.fig.leftAlign();
					p.yPos = innerFig.height +  p.gap - p.fig.topAlign();
					width = max(width,p.xPos + p.fig.width);
				} else {
					p.xPos = p.position - p.fig.leftAlign();
					p.yPos = - p.gap - p.fig.topAlign();
				}
				
			} else {
				if(yAxisRight){
					p.xPos = innerFig.width +   p.gap - p.fig.leftAlign();
					p.yPos = p.position - p.fig.topAlign();
					height = max(height,p.yPos + p.fig.height);
				} else {
					p.xPos = -p.gap - p.fig.leftAlign();
					p.yPos = p.position - p.fig.topAlign();
				}
			}
		}
		if(!xAxisBelow){
			float yShift = 0.0f;
			for(Projection p : projections){
				yShift = max(yShift,-p.yPos);
			}
			innerFigY = yShift;
			for(Projection p : projections){
				p.yPos += yShift;
			}
		}
		if(!yAxisRight){
			float xShift = 0.0f;
			for(Projection p : projections){
				xShift = max(xShift,-p.xPos);
			}
			innerFigX = xShift;
			for(Projection p : projections){
				p.xPos += xShift;
			}
		}
		
	}

	@Override
	public void draw(float left, float top) {
		
		super.draw(left, top);
		for(Projection p : projections){
			p.fig.draw(left + p.xPos, top + p.yPos );
		}
		System.err.printf("Drawing projections");
	}

	public void gatherProjections(float left, float top, Vector<Chart.Projection> projections){
		// cannot nest charts .... ?
	}
	
}
