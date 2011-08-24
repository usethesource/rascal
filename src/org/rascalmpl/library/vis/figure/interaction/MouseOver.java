package org.rascalmpl.library.vis.figure.interaction;

import java.util.Set;
import java.util.TreeSet;

import org.eclipse.imp.pdb.facts.IBool;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.figure.combine.LayoutProxy;
import org.rascalmpl.library.vis.figure.combine.Overlap;
import org.rascalmpl.library.vis.figure.combine.containers.Space;
import org.rascalmpl.library.vis.properties.PropertyManager;
import org.rascalmpl.library.vis.swt.ICallbackEnv;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;
import org.rascalmpl.library.vis.util.Mutable;
import org.rascalmpl.library.vis.util.NameResolver;


public class MouseOver extends Overlap {

	Figure over;
	boolean showMouseOver;
	Set<MouseOver> children;
	int lastComputeClock;
	MouseOver parent;
	
	static class MouseOverListener extends LayoutProxy{

		public MouseOverListener(Figure inner) {
			super(inner, new PropertyManager());
		}
		
		@Override
		public void executeMouseMoveHandlers(ICallbackEnv env, IBool enter) {
			env.fakeRascalCallBack();
			super.executeMouseMoveHandlers(env, enter);
		}
		

		@Override
		public boolean handlesInput(){
			return true;
		}
		
		public String toString(){
			return String.format("Mouse Over %d",sequenceNr);
		}
	}
	
	public MouseOver(Figure under, Figure over, PropertyManager properties) {
		super(new MouseOverListener(under), Space.empty, properties);
		this.over = new MouseOverListener(over);
		showMouseOver =false;
		children = new TreeSet<MouseOver>();
		lastComputeClock = -1;
	}

	@Override
	public void initElem(IFigureConstructionEnv env, MouseOver mparent, boolean swtSeen, boolean visible){
		super.initElem(env, mparent, swtSeen, visible);
		this.parent = mparent;
		if(parent!=null){
			parent.registerChild(this);
		}
		computeMouseOver(env);
		System.out.printf("Computing mover %s!\n",showMouseOver);
		if(showMouseOver){
			setOverlap(over);
		} else {
			setOverlap(Space.empty);
			over.hide(env);
		}
	}
	
	@Override
	public boolean initChildren(IFigureConstructionEnv env,
			NameResolver resolver, MouseOver mparent, boolean swtSeen, boolean visible) {
		swtSeen = innerFig.init(env, resolver, mparent, swtSeen, visible);
		over.init(env, resolver, this, swtSeen, visible);
		return swtSeen;
	}
	
	
	public boolean computeMouseOver(IFigureConstructionEnv env){
		if(lastComputeClock == env.getCallBackEnv().getComputeClock()){
			System.out.printf("Compute clock the same!\n");
			return showMouseOver;
		} else {
			lastComputeClock = env.getCallBackEnv().getComputeClock();
			if(innerFig.mouseOver || over.mouseOver){
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
