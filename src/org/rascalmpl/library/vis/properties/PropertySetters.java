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

import org.eclipse.imp.pdb.facts.IConstructor;
import org.rascalmpl.library.vis.figure.Figure;
import org.rascalmpl.library.vis.swt.IFigureConstructionEnv;

public class PropertySetters {

	public interface PropertySetter<PropValue>{
		int execute(PropertyValue[] array,int startIndex,IConstructor c, IFigureConstructionEnv fpa, PropertyManager pm);
		int nrOfPropertiesProduced();
		int minNrOfArguments();
		int maxNrOfArguments();
		Properties getProperty(int arg);
	}
	
	public static class SinglePropertySetter<PropValue> implements PropertySetter<PropValue>{
		
		PropertyParsers.PropertyParser<PropValue> parser;
		
		SinglePropertySetter(PropertyParsers.PropertyParser<PropValue> parser){
			this.parser = parser;
		}
		
		@SuppressWarnings("rawtypes")
		public int execute(PropertyValue[] array,int startIndex, IConstructor c, IFigureConstructionEnv fpa,
				 PropertyManager pm){
			array[startIndex] = parser.parseProperty(c, pm, 0, fpa);
			return startIndex+1;
		}
		
		public int nrOfPropertiesProduced() {
			return 1;
		}
		
		public int minNrOfArguments(){
			return 1;
		}
		
		public int maxNrOfArguments(){
			return 1;
		}
		
		public Properties getProperty(int arg){
			return parser.getProperty();
		}
	}

	public static class DualOrRepeatSinglePropertySetter <PropValue> implements PropertySetter<PropValue>{
		
		PropertyParsers.PropertyParser<PropValue> parser1,parser2;
		
		DualOrRepeatSinglePropertySetter(PropertyParsers.PropertyParser<PropValue> parser1,
				PropertyParsers.PropertyParser<PropValue> parser2){
			this.parser1 = parser1;
			this.parser2 = parser2;
		}
		
		@SuppressWarnings("rawtypes")
		public int execute(PropertyValue[] array,int startIndex, IConstructor c, IFigureConstructionEnv fpa,
				PropertyManager pm){
			int secondIndex;
			if(c.arity() == 1){
				secondIndex = 0;
			} else {
				secondIndex = 1;
			}
			array[startIndex] = parser1.parseProperty(c, pm, 0, fpa);
			array[startIndex+1] = parser2.parseProperty( c, pm, secondIndex, fpa);
			return startIndex+2;
		}
		
		public int nrOfPropertiesProduced() {
			return 2;
		}
		
		public int minNrOfArguments(){
			return 1;
		}
		
		public int maxNrOfArguments(){
			return 2;
		}
		
		public Properties getProperty(int arg){
			if(arg == 0){
				return parser1.getProperty();
			} else {
				return parser2.getProperty();
			}
		}
	}
	
	public static class SingleBooleanPropertySetter extends SinglePropertySetter<Boolean>{
		public SingleBooleanPropertySetter(Properties property) {
			super( new PropertyParsers.BooleanArgParser(property));
		}
	}
	
	public static class DualOrRepeatSingleBooleanPropertySetter extends DualOrRepeatSinglePropertySetter<Boolean> {

		DualOrRepeatSingleBooleanPropertySetter(Properties property1,Properties property2) {
			super(new PropertyParsers.BooleanArgParser(property1),new PropertyParsers.BooleanArgParser(property2));
		}

	}
	
	public static class SingleIntPropertySetter extends SinglePropertySetter<Integer>{
		public SingleIntPropertySetter(Properties property) {
			super( new PropertyParsers.IntegerArgParser(property));
		}
	}
	
	public static class SingleRealPropertySetter extends SinglePropertySetter<Double>{
		public SingleRealPropertySetter(Properties property) {
			super( new PropertyParsers.RealArgParser(property));
		}
	}
	
	public static class SingleIntOrRealPropertySetter extends SinglePropertySetter<Double>{
		public SingleIntOrRealPropertySetter(Properties property) {
			super(new PropertyParsers.IntOrRealArgParser(property));
		}
	}
	
	public static class DualOrRepeatSingleRealPropertySetter extends DualOrRepeatSinglePropertySetter<Double>{
		public DualOrRepeatSingleRealPropertySetter(Properties property1,Properties property2) {
			super(new PropertyParsers.IntOrRealArgParser(property1),new PropertyParsers.IntOrRealArgParser(property2));
		}
	}
	
	public static class DualOrRepeatSingleIntOrRealPropertySetter extends DualOrRepeatSinglePropertySetter<Double>{
		public DualOrRepeatSingleIntOrRealPropertySetter(Properties property1,Properties property2) {
			super(new PropertyParsers.IntOrRealArgParser(property1),new PropertyParsers.IntOrRealArgParser(property2));
		}
	}
	
	public static class SingleStrPropertySetter extends SinglePropertySetter<String>{
		public SingleStrPropertySetter(Properties property) {
			super( new PropertyParsers.StringArgParser(property));
		}
	}
	
	public static class SingleColorPropertySetter extends SinglePropertySetter<Integer>{
		public SingleColorPropertySetter(Properties property) {
			super( new PropertyParsers.ColorArgParser(property));
		}
	}
	
	public static class SingleFigurePropertySetter extends SinglePropertySetter<Figure>{
		public SingleFigurePropertySetter(Properties property) {
			super(new PropertyParsers.FigureArgParser(property));
		}
	}
	
	public static class SingleHandlerPropertySetter extends SinglePropertySetter<Void>{
		public SingleHandlerPropertySetter(Properties property) {
			super(new PropertyParsers.HandlerArgParser(property));
		}
	}
}
