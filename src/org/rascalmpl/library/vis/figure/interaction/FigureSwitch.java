package org.rascalmpl.library.vis.figure.interaction;

import static org.rascalmpl.library.vis.util.vector.Dimension.HOR_VER;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.LayoutProxy;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.properties.PropertyValue;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.vector.Dimension;

public class FigureSwitch extends LayoutProxy{

	Figure[] choices;
	PropertyValue<Integer> choice;
	
	public FigureSwitch(PropertyValue<Integer> choice, Figure[] choices, PropertyManager properties) {
		super(null, properties);
		this.choices = choices;
		this.choice = choice;
	}

	@Override
	public void initElem(IFigureConstructionEnv env, MouseOver mparent, boolean swtSeen){
		setInnerFig(choices[choice.getValue()]);
		for(int i = 0 ; i < choices.length ; i++){
			if(i != choice.getValue()){
				choices[i].hide(env);
			}
		}
	}

	@Override
	public void computeMinSize() {
		resizable.set(false,false);
		for(Figure fig : choices){
			minSize.setMax(fig.minSize);
			for(Dimension d : HOR_VER){
				resizable.set(d,resizable.get(d) || innerFig.resizable.get(d));
			}
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
