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

import java.util.EnumMap;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.interpreter.IEvaluatorContext;
import org.rascalmpl.library.vis.Figure;
import org.rascalmpl.library.vis.IFigureApplet;
import org.rascalmpl.library.vis.properties.descriptions.BoolProp;
import org.rascalmpl.library.vis.properties.descriptions.ColorProp;
import org.rascalmpl.library.vis.properties.descriptions.FigureProp;
import org.rascalmpl.library.vis.properties.descriptions.HandlerProp;
import org.rascalmpl.library.vis.properties.descriptions.IntProp;
import org.rascalmpl.library.vis.properties.descriptions.MeasureProp;
import org.rascalmpl.library.vis.properties.descriptions.RealProp;
import org.rascalmpl.library.vis.properties.descriptions.StrProp;

public class PropertySetters {

	public static interface PropertySetter<Prop extends Enum<Prop>,PropValue>{
		void execute(EnumMap<Prop, IPropertyValue<PropValue>> values,IConstructor c, IFigureApplet fpa,IEvaluatorContext ctx, PropertyManager pm);
	}
	
	public static class SinglePropertySetter<Prop extends Enum<Prop>,PropValue> implements PropertySetter<Prop,PropValue>{
		Prop property;
		PropertyParsers.PropertyParser<Prop,PropValue> parser;
		
		SinglePropertySetter(Prop property,PropertyParsers.PropertyParser<Prop,PropValue> parser){
			this.property = property;
			this.parser = parser;
		}
		
		public void execute(EnumMap<Prop, IPropertyValue<PropValue>> values,IConstructor c, IFigureApplet fpa,
				IEvaluatorContext ctx, PropertyManager pm){
			values.put(property,parser.parseProperty(property, c, pm, 0, fpa, ctx));
		}
	}
	

	public static class DualOrRepeatSinglePropertySetter <Prop extends Enum<Prop>,PropValue> implements PropertySetter<Prop,PropValue>{
		Prop property1,property2;
		PropertyParsers.PropertyParser<Prop,PropValue> parser;
		
		DualOrRepeatSinglePropertySetter(Prop property1,Prop property2,PropertyParsers.PropertyParser<Prop,PropValue> parser){
			this.property1 = property1;
			this.property2 = property2;
			this.parser = parser;
		}
		
		public void execute(EnumMap<Prop, IPropertyValue<PropValue>> values,IConstructor c, IFigureApplet fpa,
				IEvaluatorContext ctx, PropertyManager pm){
			int secondIndex;
			if(c.arity() == 1){
				secondIndex = 0;
			} else {
				secondIndex = 1;
			}
			values.put(property1,parser.parseProperty(property1, c, pm, 0, fpa, ctx));
			values.put(property2,parser.parseProperty(property2, c, pm, secondIndex, fpa, ctx));
		}
	}
	
	public static class SingleBooleanPropertySetter extends SinglePropertySetter<BoolProp,Boolean>{
		public SingleBooleanPropertySetter(BoolProp property) {
			super(property, new PropertyParsers.BooleanArgParser());
		}
	}
	
	public static class DualOrRepeatSingleBooleanPropertySetter extends DualOrRepeatSinglePropertySetter<BoolProp,Boolean> {
		public DualOrRepeatSingleBooleanPropertySetter(BoolProp property1,BoolProp property2) {
			super(property1, property2, new PropertyParsers.BooleanArgParser());
		}
	}
	
	public static class SingleIntPropertySetter extends SinglePropertySetter<IntProp,Integer>{
		public SingleIntPropertySetter(IntProp property) {
			super(property, new PropertyParsers.IntegerArgParser());
		}
	}
	
	public static class SingleRealPropertySetter extends SinglePropertySetter<RealProp,Double>{
		public SingleRealPropertySetter(RealProp property) {
			super(property, new PropertyParsers.RealArgParser());
		}
	}
	
	public static class SingleIntOrRealPropertySetter extends SinglePropertySetter<RealProp,Double>{
		public SingleIntOrRealPropertySetter(RealProp property) {
			super(property, new PropertyParsers.IntOrRealArgParser());
		}
	}
	
	public static class SingleMeasurePropertySetter extends SinglePropertySetter<MeasureProp,Measure>{
		public SingleMeasurePropertySetter(MeasureProp property) {
			super(property, new PropertyParsers.MeasureArgParser());
		}
	}
	

	public static class DualOrRepeatMeasurePropertySetter extends DualOrRepeatSinglePropertySetter<MeasureProp,Measure>{
		public DualOrRepeatMeasurePropertySetter(MeasureProp property1,MeasureProp property2) {
			super(property1,property2, new PropertyParsers.MeasureArgParser());
		}
	}
	
	public static class DualOrRepeatSingleRealPropertySetter extends DualOrRepeatSinglePropertySetter<RealProp,Double>{
		public DualOrRepeatSingleRealPropertySetter(RealProp property1,RealProp property2) {
			super(property1,property2, new PropertyParsers.RealArgParser());
		}
	}
	
	public static class DualOrRepeatSingleIntOrRealPropertySetter extends DualOrRepeatSinglePropertySetter<RealProp,Double>{
		public DualOrRepeatSingleIntOrRealPropertySetter(RealProp property1,RealProp property2) {
			super(property1,property2, new PropertyParsers.IntOrRealArgParser());
		}
	}
	
	public static class SingleStrPropertySetter extends SinglePropertySetter<StrProp,String>{
		public SingleStrPropertySetter(StrProp property) {
			super(property, new PropertyParsers.StringArgParser());
		}
	}
	
	public static class SingleColorPropertySetter extends SinglePropertySetter<ColorProp,Integer>{
		public SingleColorPropertySetter(ColorProp property) {
			super(property, new PropertyParsers.ColorArgParser());
		}
	}
	
	public static class SingleFigurePropertySetter extends SinglePropertySetter<FigureProp,Figure>{
		public SingleFigurePropertySetter(FigureProp property) {
			super(property, new PropertyParsers.FigureArgParser());
		}
	}
	
	public static class SingleHandlerPropertySetter extends SinglePropertySetter<HandlerProp,Void>{
		public SingleHandlerPropertySetter(HandlerProp property) {
			super(property, new PropertyParsers.HandlerArgParser());
		}
	}
}
