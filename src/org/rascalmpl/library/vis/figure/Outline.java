/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:

 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
*******************************************************************************/
package org.rascalmpl.library.vis.figure;


/**
 * Outline element: a rectangle with colored horizontal lines
 * 
 * @author paulk
 *
// */
//public class Outline extends Figure {
//
//	private boolean debug = false;
//	private final IList lineInfo;
//	private final int maxLine;
//
//	public Outline(PropertyManager properties, IList lineInfo, IInteger maxLine) {
//		super(properties);
//		this.lineInfo = lineInfo;
//		this.maxLine = maxLine.intValue();
//	}
//
//	@Override
//	public
//	void bbox(){
//		double lw = getLineWidthProperty();
//		minSize.setWidth(getWidthProperty());
//		minSize.setHeight(getHeightProperty());
//		minSize.setWidth(minSize.getWidth() + 2*lw);
//		minSize.setHeight(minSize.getHeight() + 2*lw);
//		if(debug) System.err.println("Outline.bbox => " + minSize.getWidth() + ", " + minSize.getHeight());
//		//if(debug)System.err.printf("Outline.bbox: topAnchor=%f, bottomAnchor=%f\n", topAlign(), bottomAlign());
//		setNonResizable();
//		super.bbox();
//	
//	}
//	
//	@Override
//	public
//	void draw(GraphicsContext gc) {
//		
//	    double lw = getLineWidthProperty();
//		applyProperties(gc);
//		/* if(debug) */ System.err.println("Outline.draw => " + minSize.getWidth() + ", " + minSize.getHeight());
//		if(minSize.getHeight() > 0 && minSize.getWidth() > 0){
//			gc.rect(getLeft(), getTop(), minSize.getWidth(), minSize.getHeight());
//			for(IValue v : lineInfo){
//				IConstructor lineDecor = (IConstructor) v;
//				int lino = ((IInteger) lineDecor.get(0)).intValue();
//				String name = lineDecor.getName();
//				
//				int color;
//				
//				if(name.equals("info"))
//					color = FigureColorUtils.getErrorColor(0);
//				else if(name.equals("warning"))
//					color = FigureColorUtils.getErrorColor(1);
//				else if(name.equals("error"))
//				color = FigureColorUtils.getErrorColor(2);
//				else {
//					int highlightKind = 0;
//					
//					if(lineDecor.arity() > 2){
//						highlightKind = ((IInteger)lineDecor.get(2)).intValue();
//						if(highlightKind < 0)
//							highlightKind = 0;
//						if(highlightKind >= FigureColorUtils.highlightColors.length)
//							highlightKind = FigureColorUtils.highlightColors.length - 1;
//					}
//					color = FigureColorUtils.getHighlightColor(highlightKind);
//				}
//
//				gc.stroke(color);
//				double vpos = getTop() + (lino * minSize.getHeight()) /maxLine ;
//				gc.line(getLeft() + + lw, vpos, getLeft() + minSize.getWidth() - lw, vpos);
//			}
//		}
//	}
//
//	@Override
//	public void layout() {
//		size.set(minSize);
//	}
//
//	
//}
