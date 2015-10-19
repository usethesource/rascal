/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.figure.graph.layered;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.FigureFactory;
import org.rascalmpl.library.vis.figure.combine.LayoutProxy;
import org.rascalmpl.library.vis.figure.interaction.MouseOver;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.NameResolver;
import org.rascalmpl.value.IConstructor;
import org.rascalmpl.value.IList;

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
