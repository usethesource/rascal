package org.rascalmpl.library.vis.figure.graph.layered;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IList;
import org.eclipse.imp.pdb.facts.IValue;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.FigureFactory;
import org.rascalmpl.library.vis.figure.combine.LayoutProxy;
import org.rascalmpl.library.vis.figure.interaction.MouseOver;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.NameResolver;

public class LayerGraphRecompute extends LayoutProxy {
	
	IFigureConstructionEnv fpa;
	Figure[] nodes;
	IList edges;
	int count;
	
	public LayerGraphRecompute(IFigureConstructionEnv fpa, PropertyManager properties, IList nodes,
			IList edges){
		super(null,properties);
		this.fpa = fpa;
		this.nodes = new Figure[nodes.length()];
		for(int i = 0 ; i < nodes.length() ; i++){
			IConstructor c = (IConstructor) nodes.get(i);
			this.nodes[i] = FigureFactory.make(fpa, c, properties, null);
		}
		this.edges = edges;
		count = 0;
	}

	public boolean initChildren(IFigureConstructionEnv env,
			NameResolver resolver, MouseOver mparent, boolean swtSeen, boolean visible) {
		setInnerFig(new LayeredGraph(fpa, prop, nodes, edges));
		return children[0].init(env, resolver, mparent, swtSeen, visible);
	}
	


}
