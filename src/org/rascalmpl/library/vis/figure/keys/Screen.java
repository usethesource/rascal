package org.rascalmpl.library.vis.figure.keys;

import static org.rascalmpl.library.vis.properties.TwoDProperties.ALIGN;
import static org.rascalmpl.library.vis.properties.TwoDProperties.SHRINK;
import static org.rascalmpl.library.vis.util.vector.Dimension.HOR_VER;

import java.util.ArrayList;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.WithInnerFig;
import org.rascalmpl.library.vis.figure.interaction.MouseOver;
import org.rascalmpl.library.vis.properties.Properties;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.vector.BoundingBox;
import org.rascalmpl.library.vis.util.vector.Dimension;
import org.rascalmpl.library.vis.util.vector.Rectangle;
import org.rascalmpl.library.vis.util.vector.TransformMatrix;

public class Screen extends WithInnerFig{

	ArrayList<Projection> projections;
	Dimension major,minor;
	BoundingBox minExtraSizeForProjections;
	
	public Screen(Figure inner, PropertyManager properties) {
		super(inner, properties);
		projections = new ArrayList<Projection>();
		minExtraSizeForProjections = new BoundingBox();
	}
	
	// TODO: this is copied from newtree, maybe put in figure...
	private void setMajorDimension() {
		if(prop.getBool(Properties.MAJOR_X)){
			major = Dimension.X;
		} else {
			major = Dimension.Y;
		}
		this.minor = major.other();
	}
	
	@Override
	public void initElem(IFigureConstructionEnv env, MouseOver mparent, boolean swtSeen, boolean visible, NameResolver resolver){
		setMajorDimension();
		projections.clear();
	}
	
	public void registerProjection(Projection p){
		projections.add(p);
	}
	
	@Override
	public boolean initChildren(IFigureConstructionEnv env,
			NameResolver resolver, MouseOver mparent, boolean swtSeen, boolean visible) {
		return innerFig.init(env, resolver,mparent, swtSeen, visible);
	}

	public void setChildren() {
		children = new Figure[projections.size() + 1];
		children[0] = innerFig;
		for(int i = 0 ; i < projections.size(); i++){
			children[i+1] = projections.get(i).projection;
		}
	}
	
	@Override
	public void computeMinSize(){
		setChildren();
		BoundingBox spaceForProjections = new BoundingBox();
		minSize.set(innerFig.minSize);
		for(Dimension d : HOR_VER){
			minSize.set(d,minSize.get(d) / innerFig.prop.get2DReal(d, SHRINK));
			spaceForProjections.set(d,minSize.get(d) * (1.0-innerFig.prop.get2DReal(d, SHRINK)));
		}
		for(Projection p : projections){
			for(Dimension d : HOR_VER){
				spaceForProjections.setMax(d, p.projection.minSize.get(d) / p.projection.prop.get2DReal(d, SHRINK));
			}
		}
		for(Dimension d: HOR_VER){
			double left = innerFig.minSize.get(d) * prop.get2DReal(d, ALIGN) - spaceForProjections.get(d);
			double right = innerFig.minSize.get(d) * prop.get2DReal(d, ALIGN) + spaceForProjections.get(d);
			double oldMinSize = minSize.get(d);
			minSize.setMax(d,right - left);
			minExtraSizeForProjections.set(d,minSize.get(d) - oldMinSize);
		}
	}
	
	@Override
	public void resizeChildren(Rectangle view, TransformMatrix transform) {
		for(Dimension d: HOR_VER){
			innerFig.size.set(d,(size.get(d) - minExtraSizeForProjections.get(d)) * innerFig.prop.get2DReal(d, SHRINK));
			innerFig.location.set(d,(size.get(d) - innerFig.size.get(d)) * innerFig.prop.get2DReal(d, ALIGN));
			innerFig.location.add(location);

		}
		innerFig.resize(view,transform);
		double majorSpaceForProjection = size.get(major) - innerFig.size.get(major);
		double majorProjectionOffset = innerFig.size.get(major) * (1.0 - innerFig.prop.get2DReal(major, ALIGN));
		for(Projection p : projections){
			Figure pFrom = p.projectFrom;
			Figure pr = p.projection;
			double projectFromMinor = 
				pFrom.location.get(minor) - location.get(minor);
			pr.size.set(minor, pFrom.size.get(minor) * pr.prop.get2DReal(minor, SHRINK));
			pr.size.set(major,majorSpaceForProjection * pr.prop.get2DReal(major, SHRINK ));
			pr.size.setMax(pr.minSize);
			pr.location.set(minor,projectFromMinor + (pFrom.size.get(minor) - pr.size.get(minor)) * pr.prop.get2DReal(minor, ALIGN));
			pr.location.set(major,majorProjectionOffset + (majorSpaceForProjection - pr.size.get(major))* pr.prop.get2DReal(minor, ALIGN));
			pr.resize(view, transform);
		}
	}
}