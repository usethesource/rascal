package org.rascalmpl.library.vis.figure.combine;

import static org.rascalmpl.library.vis.properties.TwoDProperties.ALIGN;
import static org.rascalmpl.library.vis.properties.TwoDProperties.GROW;
import static org.rascalmpl.library.vis.properties.TwoDProperties.SHRINK;
import static org.rascalmpl.library.vis.util.vector.Dimension.HOR_VER;

import java.util.List;
import java.util.Vector;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.interaction.MouseOver;
import org.rascalmpl.library.vis.graphics.GraphicsContext;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.swt.applet.IHasSWTElement;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.vector.Coordinate;
import org.rascalmpl.library.vis.util.vector.Dimension;
import org.rascalmpl.library.vis.util.vector.Rectangle;

public class Overlap extends LayoutProxy{
	
	public Figure over;
	
	public Overlap(Figure under, Figure over, PropertyManager properties){
		super(under,properties);
		children = new Figure[2];
		children[0] = under;
		children[1] = over;
		this.over = over;
	}
	
	
	@Override
	public void initElem(IFigureConstructionEnv env, MouseOver mparent, boolean swtSeen, boolean visible, NameResolver resolver){
		super.initElem(env, mparent, swtSeen, visible, resolver);
		env.registerOverlap(this);
	}
	
	public void setOverlap(Figure fig){
		children[1] = fig;
		over = fig;
	}
	
	@Override
	public void resizeElement(Rectangle view) {
		super.resizeElement(view);
		for(Dimension d : HOR_VER){
			/* if(over.prop.is2DPropertySet(d, SHRINK)){
				double sizeLeft = Math.max(0,location.get(d)  - view.getLocation().get(d));
				double sizeRight = 
					Math.max(0,view.getSize().get(d) - ((location.get(d) - view.getLocation().get(d)) + size.get(d)));
				
				double align = over.prop.get2DReal(d, ALIGN);
				double sizeMiddle = size.get(d) * 2*(0.5 - Math.abs(align - 0.5 ));
				if(align > 0.5){
					sizeLeft*= 1.0 - (align - 0.5)*2.0;
				}
				if(align < 0.5){
					sizeRight*= 1.0 - (0.5 - align)*2.0;
				}
				over.size.set(d,over.prop.get2DReal(d, SHRINK) * (sizeLeft + sizeMiddle + sizeRight));
				
			} else { */
				over.size.set(d,innerFig.size.get(d) * over.prop.get2DReal(d, SHRINK));
//				System.out.printf("Over %s %f\n", over, over.prop.get2DReal(d, SHRINK));
//			}
			
			if(over.size.get(d) < over.minSize.get(d)){
				over.size.set(d, over.minSize.get(d));
			}
			over.location.set(d, 
					(over.prop.get2DReal(d, ALIGN)  * (innerFig.size.get(d) - over.size.get(d))) + 
					(over.prop.get2DReal(d,ALIGN) -0.5)*2.0 * over.size.get(d));
			
		}
//		System.out.printf("OVer %s %s \n",over.size,innerFig.size);
	}
	
	@Override	
	public void destroyElement(IFigureConstructionEnv env) {
		env.unregisterOverlap(this);
	}

	@Override
	public void drawChildren(Coordinate zoom, GraphicsContext gc,
			Rectangle part, List<IHasSWTElement> visibleSWTElements) {
		innerFig.draw(zoom, gc, part, visibleSWTElements);
	}
	
	public void getFiguresUnderMouse(Coordinate c,List<Figure> result){
		if(!mouseInside(c)){
			return;
		}
		innerFig.getFiguresUnderMouse(c, result);
		if(handlesInput()){
			result.add(this);
		}
	}
	
}
