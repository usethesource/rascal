package org.rascalmpl.library.vis.figure.interaction;

import static org.rascalmpl.library.vis.util.vector.Dimension.HOR_VER;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.WithInnerFig;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.PropertyValue;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.library.vis.util.vector.Dimension;

public class FigureSwitch extends WithInnerFig{

	Figure[] choices;
	PropertyValue<Integer> choice;
	boolean visible;
	
	public FigureSwitch(PropertyValue<Integer> choice, Figure[] choices, PropertyManager properties) {
		super(null, properties);
		this.choices = choices;
		this.choice = choice;
	}

	@Override
	public void initElem(IFigureConstructionEnv env, MouseOver mparent, boolean swtSeen, boolean visible){
		children = choices;
		this.visible = visible;
	}

	@Override
	public void computeMinSize() {
		resizable.set(false,false);
		for(Figure fig : choices){
			setInnerFig(fig);
			super.computeMinSize();
			minSize.setMax(fig.minSize);
			for(Dimension d : HOR_VER){
				resizable.set(d,resizable.get(d) || fig.resizable.get(d));
			}
		}
	}
	

	public boolean initChildren(IFigureConstructionEnv env,
			NameResolver resolver, MouseOver mparent, boolean swtSeen, boolean visible) {
		
		boolean swtSeenResult = false;
		for(int i = 0; i < children.length ; i++){
			boolean nextVisible = visible && i == choice.getValue();
			boolean swt = children[i].init(env, resolver,mparent, swtSeen, nextVisible);
			if(nextVisible){
				swtSeenResult = swt;
			}
		}
		return swtSeenResult;
	}
	
	public void finalize(boolean needsRecompute){
		if(visible){
			setInnerFig(choices[choice.getValue()]);
		}
	}
	
	@Override
	public void hideElement(IFigureConstructionEnv env) {
		for(int i = 0 ; i < choices.length ; i++){
				choices[i].hide(env);
		}
	}
	
	@Override	
	public void destroyElement(IFigureConstructionEnv env) {
		for(int i = 0 ; i < choices.length ; i++){
			if(i != choice.getValue()){
				choices[i].destroy(env);
			}
		}
	}

}
