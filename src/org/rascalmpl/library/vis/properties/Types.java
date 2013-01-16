/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
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

import static org.rascalmpl.library.vis.properties.CombinedProperty.Combine.FIRST;
import static org.rascalmpl.library.vis.properties.CombinedProperty.Combine.INTERPOLATECOLOR;
import static org.rascalmpl.library.vis.properties.CombinedProperty.Combine.OR;

import org.rascalmpl.library.vis.properties.CombinedProperty.Combine;
import org.rascalmpl.library.vis.util.RascalToJavaValueConverters.Convert;
import org.rascalmpl.library.vis.util.RascalToJavaValueConverters.ConvertBool;
import org.rascalmpl.library.vis.util.RascalToJavaValueConverters.ConvertColor;
import org.rascalmpl.library.vis.util.RascalToJavaValueConverters.ConvertFig;
import org.rascalmpl.library.vis.util.RascalToJavaValueConverters.ConvertInt;
import org.rascalmpl.library.vis.util.RascalToJavaValueConverters.ConvertReal;
import org.rascalmpl.library.vis.util.RascalToJavaValueConverters.ConvertStr;
import org.rascalmpl.library.vis.util.RascalToJavaValueConverters.DoNotConvert;


@SuppressWarnings("rawtypes")
public enum Types {
	BOOL("bool","b",OR){
		public Convert getConverter(){
			return ConvertBool.instance;
		}
	},
	COLOR("Color","c",INTERPOLATECOLOR){
		public Convert getConverter(){
			return ConvertColor.instance;
		}
	},
	FIGURE("Figure","f",FIRST){
		public Convert getConverter(){
			return ConvertFig.instance;
		}
	},
	HANDLER("","h", false,FIRST){
		public Convert getConverter(){
			return DoNotConvert.instance;
		}
	},
	INT("int","i", FIRST){
		public Convert getConverter(){
			return ConvertInt.instance;
		}
	},
	REAL("num","r", FIRST){
		public Convert getConverter(){
			return ConvertReal.instance;
		}
	},
	STR("str","s",FIRST){
		public Convert getConverter(){
			return ConvertStr.instance;
		}
	},
	VALUE("value","vv",FIRST){
		public Convert getConverter(){
			return DoNotConvert.instance;
		}
	};
	
	public abstract Convert getConverter();
	
	public String rascalName;
	public String shortName;
	public boolean canBeComputed;
	public Combine defaultCombine;
	
	
	Types(String rascalName,String shortName,Combine defaultCombine){
		this(rascalName, shortName, true,defaultCombine);
	}
	
	Types(String rascalName,String shortName, boolean canBeComputed,Combine defaultCombine){
		this.rascalName = rascalName;
		this.shortName = shortName;
		this.canBeComputed = canBeComputed;
		this.defaultCombine = defaultCombine;
	}
	
}
