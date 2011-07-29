package org.rascalmpl.library.vis.compose;

import java.util.Vector;

import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.containers.Space;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.ISWTZOrdering;
import org.rascalmpl.library.vis.util.Coordinate;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.Rectangle;

public class Overlap extends Figure{

	public Figure under;
	public Figure over;
	
	public Overlap(Figure under, Figure over, PropertyManager properties){
		super(properties);
		this.under = under;
		this.over = over;
	}
	

	public boolean getFiguresUnderMouse(Coordinate c,Vector<Figure> result){
		return under.getFiguresUnderMouse(c, result);
	}
	
	@Override
	public void drawPart(Rectangle r,GraphicsContext gc){
		under.drawPart(r, gc);
	}

	public void init(){
		under.init();
		over.init();
	}
	
	public void computeFiguresAndProperties(ICallbackEnv env){
		super.computeFiguresAndProperties(env);
		under.computeFiguresAndProperties(env);
		over.computeFiguresAndProperties(env);
	}
	
	public void registerNames(NameResolver resolver){
		super.registerNames(resolver);
		under.registerNames(resolver);
		over.registerNames(resolver);
	}
	
	public void registerValues(NameResolver resolver){
		super.registerValues(resolver);
		under.registerValues(resolver);
		over.registerValues(resolver);
	}
	

	public void getLikes(NameResolver resolver){
		super.getLikes(resolver);
		under.getLikes(resolver);
		over.getLikes(resolver);
	}
	
	@Override
	public void bbox(){
		under.bbox();
		over.bbox();
		for(boolean flip : BOTH_DIMENSIONS){
			setResizableX(flip, under.getResizableX(flip));
		}
		minSize.set(under.minSize);
	}
	
	public void finalize(){
		super.finalize();
		under.finalize();
		over.finalize();
	}
	

	public void layout(){
		under.globalLocation.set(globalLocation);
		for(boolean flip : BOTH_DIMENSIONS){
			under.takeDesiredWidth(flip,size.getWidth() * under.getHShrinkProperty(flip));
		}
		under.layout();
		for(boolean flip : BOTH_DIMENSIONS){
			under.globalLocation.addX(flip, (size.getWidth(flip) - under.size.getWidth(flip)) * under.getHAlignProperty(flip));
		}
		over.globalLocation.set(under.globalLocation);
		for(boolean flip : BOTH_DIMENSIONS){
			System.out.printf("HSHRINK IS %s SET!!! %s %f\n",over.isHShrinkPropertySet(flip),flip,over.getHGrowProperty(flip));
			if(!over.isHShrinkPropertySet(flip)){
				over.takeDesiredWidth(flip,under.size.getWidth(flip) * over.getHGrowProperty(flip));
			}
		}
	}

	public void destroy(){
		super.destroy();
		under.destroy();
		over.destroy();
	}
	
	public void setSWTZOrder(ISWTZOrdering zorder){
		under.setSWTZOrder(zorder);
		zorder.pushOverlap();
		zorder.registerOverlap(this);
		zorder.popOverlap();
	}
	
	@Override
	public void draw(GraphicsContext gc) {
		under.draw(gc);
	}

}
