/*******************************************************************************
 * Copyright (c) 2009-2011 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   * Paul Klint - Paul.Klint@cwi.nl - CWI
 *   * Atze van der Ploeg - Atze.van.der.Ploeg@cwi.nl - CWI
*******************************************************************************/

package org.rascalmpl.library.vis.properties;

import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.interpreter.utils.RuntimeExceptionFactory;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.descriptions.BoolProp;
import org.rascalmpl.library.vis.properties.descriptions.ColorProp;
import org.rascalmpl.library.vis.properties.descriptions.IntProp;
import org.rascalmpl.library.vis.properties.descriptions.RealProp;
import org.rascalmpl.library.vis.properties.descriptions.StrProp;

public class LikeProperties {

	private static abstract class LikeProperty<PropType> implements IPropertyValue<PropType> {
		final Figure fig;

		public LikeProperty(String id, IFigureApplet fpa, IEvaluatorContext ctx){
			this.fig = fpa.getRegisteredId(id);
			if(this.fig == null)
				throw RuntimeExceptionFactory.figureException("Cannot be the same as not (yet) existing figure", ctx.getValueFactory().string(id), ctx.getCurrentAST(),
					ctx.getStackTrace());
		}
		
		
		public abstract PropType getValue() ;
	}
	
	
	static class LikeBooleanProperty extends LikeProperty<Boolean>{
		BoolProp property;
		
		public LikeBooleanProperty(BoolProp property,String id, IFigureApplet fpa,
				IEvaluatorContext ctx) {
			super(id, fpa, ctx);
			this.property = property;
		}

		@Override
		public Boolean getValue() {
			return fig.properties.getBooleanProperty(property);
		}
	}
	
	static class LikeIntegerProperty extends LikeProperty<Integer>{
		IntProp property;
		
		public LikeIntegerProperty(IntProp property, String id, IFigureApplet fpa,
				IEvaluatorContext ctx) {
			super(id, fpa, ctx);
			this.property = property;
		}

		@Override
		public Integer getValue() {
			return fig.properties.getIntegerProperty(property);
		}
	}
	
	static class LikeColorProperty extends LikeProperty<Integer>{
		ColorProp property;
		
		public LikeColorProperty(ColorProp property, String id, IFigureApplet fpa,
				IEvaluatorContext ctx) {
			super( id, fpa, ctx);
			this.property = property;
		}
		
		@Override
		public Integer getValue() {
			return fig.properties.getColorProperty(property);
		}
	}
	
	static class LikeRealProperty extends LikeProperty<Float>{
		RealProp property;
		
		public LikeRealProperty(RealProp property, String id, IFigureApplet fpa,
				IEvaluatorContext ctx) {
			super(id, fpa, ctx);
			this.property = property;
		}

		@Override
		public Float getValue() {
			return fig.properties.getRealProperty(property);
		}
	}
	
	static class LikeStringProperty extends LikeProperty<String>{

		StrProp property;
		
		public LikeStringProperty(StrProp property, String id, IFigureApplet fpa,
				IEvaluatorContext ctx) {
			super( id, fpa, ctx);
			this.property = property;
		}

		@Override
		public String getValue() {
			return fig.properties.getStringProperty(property);
		}
	}
	
	static class LikeFigureProperty extends LikeProperty<Figure>{
		
		public LikeFigureProperty(String id, IFigureApplet fpa,
				IEvaluatorContext ctx) {
			super( id, fpa, ctx);
		}

		@Override
		public Figure getValue() {
			Figure res = fig.getMouseOver();
			res.setVisibleInMouseOver(true);
			return res;
		}
	}

}
