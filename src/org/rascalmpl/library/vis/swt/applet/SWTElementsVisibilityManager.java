package org.rascalmpl.library.vis.swt.applet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import org.rascalmpl.library.vis.util.BogusList;
import org.rascalmpl.library.vis.util.Util;

public class SWTElementsVisibilityManager {
	
	@SuppressWarnings("unchecked")
	private static final BogusList<IHasSWTElement> bogusIHasZOrderList = (BogusList<IHasSWTElement>)BogusList.instance;
	private List<IHasSWTElement> visibleSWTElements; // this is mutated during draw by viewport
	private List<IHasSWTElement> prevVisibleSWTElements;
	private List<IHasSWTElement> offscreen;
	
	public SWTElementsVisibilityManager() {
		visibleSWTElements = new ArrayList<IHasSWTElement>();
		prevVisibleSWTElements = new ArrayList<IHasSWTElement>();
		offscreen = new ArrayList<IHasSWTElement>();
	}

	public List<IHasSWTElement> getVisibleSWTElementsVector() {
		return visibleSWTElements;
	}
	
	public void makeOffscreenElementsInvisble(){
		Collections.sort(visibleSWTElements, IHasSWTElement.ICanBeInvisibleComparator.instance);
		offscreen.clear();
		Util.diffSorted(visibleSWTElements, prevVisibleSWTElements, bogusIHasZOrderList, bogusIHasZOrderList, offscreen);
		for(IHasSWTElement f : offscreen){
			System.out.printf("Making invisible %s\n", f);
			f.setVisible(false);
		}
		List<IHasSWTElement> tmp = prevVisibleSWTElements;
		prevVisibleSWTElements = visibleSWTElements;
		visibleSWTElements = tmp;
		visibleSWTElements.clear();
	}

	public void dispose() {
	} 

}
