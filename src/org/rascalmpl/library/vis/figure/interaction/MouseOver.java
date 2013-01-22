/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.figure.interaction;

import java.util.Set;
import java.util.TreeSet;

import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.LayoutProxy;
import org.rascalmpl.library.vis.figure.combine.Overlap;
import org.rascalmpl.library.vis.figure.combine.containers.Space;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.NameResolver;


public class MouseOver extends Overlap {

	Figure mover;
	boolean showMouseOver;
	Set<MouseOver> children;
	int lastComputeClock;
	MouseOver parent;
	
	static class MouseOverListener extends LayoutProxy{

		public MouseOverListener(Figure inner) {
			super(inner, new PropertyManager());
		}
		
		@Override
		public void executeMouseMoveHandlers(ICallbackEnv env, boolean enter) {
			env.fakeRascalCallBack();
			super.executeMouseMoveHandlers(env, enter);
		}
		

		@Override
		public boolean handlesInput(){
			return true;
		}
		
		public String toString(){
			return String.format("Mouse Over %s %s %s %d",globalLocation,size,innerFig,sequenceNr);
		}
	}
	
	public MouseOver(Figure under, Figure over, PropertyManager properties) {
		super(new MouseOverListener(under), Space.empty, properties);
		this.mover = new MouseOverListener(over);
		showMouseOver =false;
		children = new TreeSet<MouseOver>();
		lastComputeClock = -1;
	}

	@Override
	public void setChildren(IFigureConstructionEnv env, NameResolver resolver){
		computeMouseOver(env);
		if(showMouseOver){
			setOverlap(mover);
		} else {
			setOverlap(Space.empty);
			mover.hide(env);
		}
	}
	
	@Override
	public void initElem(IFigureConstructionEnv env, MouseOver mparent, boolean swtSeen, boolean visible, NameResolver resolver){
		super.initElem(env, mparent, swtSeen, visible, resolver);
		this.parent = mparent;
		if(parent!=null){
			parent.registerChild(this);
		}

	}
	
	@Override
	public boolean initChildren(IFigureConstructionEnv env,
			NameResolver resolver, MouseOver mparent, boolean swtSeen, boolean visible) {
		swtSeen = innerFig.init(env, resolver, mparent, swtSeen, visible);
		if(showMouseOver){
			mover.init(env, resolver, this, swtSeen, visible);
		}
		return swtSeen;
	}
	
	
	public boolean computeMouseOver(IFigureConstructionEnv env){
		if(lastComputeClock == env.getCallBackEnv().getComputeClock()){
			return showMouseOver;
		} else {
			lastComputeClock = env.getCallBackEnv().getComputeClock();
			if(innerFig.mouseOver || mover.mouseOver){
				showMouseOver = true;
				return true;
			} else {
				showMouseOver = false;
				for(MouseOver child : children){
					showMouseOver = showMouseOver || child.computeMouseOver(env);
				}
				return showMouseOver;
			}
		}
	}
	
	
	@Override	
	public void destroyElement(IFigureConstructionEnv env) {
		super.destroyElement(env);
		if(parent!=null){
			parent.unregisterChild(this);
		}
	}
	
	public void registerChild(MouseOver child){
		children.add(child);
	}
	

	public void unregisterChild(MouseOver child){
		children.remove(child);
	}


}
