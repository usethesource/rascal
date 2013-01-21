/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.properties;

public class GenRascalPropertyCode {
	
	public static void main(String[] argv){
		String formatString = "\t|%-11s(%-8s %-3s)\n";
		int c = 0;
		for(Properties p : Properties.values()){
			
			if(p.type != Types.HANDLER){
				final String[] types = {p.type.rascalName, p.type.rascalName +"()", "Measure"};
				final String[] names = {p.type.shortName, "c" + p.type.shortName, "mv"};
				for(int i = 0 ; i < types.length ; i++){
					System.out.printf(formatString,p.name,types[i],names[i]);
				}
				if(p.type == Types.COLOR){
					System.out.printf(formatString,p.name,"str","ds");
				}
			} else {
				System.out.printf(formatString,p.name,p.callBackType,"h" + c);
				c++;
			}
		}
		System.out.printf(";\n\n");
		for(TwoDProperties p : TwoDProperties.values()){
			final String[] types = {p.hor.type.rascalName, p.hor.type.rascalName +"()", "Measure"};
			final String[] names = {p.hor.type.shortName, "c" + p.hor.type.shortName, "mv"};
			for(int i = 0 ; i < types.length ; i++){
				System.out.printf("public FProperty %-11s(%-8s %-3s){",p.commonName, types[i], names[i]);
				System.out.printf(" return unpack([%-11s(%-3s),%-11s(%-3s)]);",p.hor.name, names[i], p.ver.name, names[i]);
				System.out.printf(" }\n");
			}
			
			
		}
		for(TwoDProperties p : TwoDProperties.values()){
			final String[] types = {p.hor.type.rascalName, p.hor.type.rascalName +"()", "Measure"};
			final String[] names = {p.hor.type.shortName, "c" + p.hor.type.shortName, "mv"};
			for(int i = 0 ; i < types.length ; i++){
				for(int j = 0 ; j < types.length; j++){
					System.out.printf("public FProperty %-11s(%-8s %-5s,%-8s %-5s){",p.commonName, types[i], names[i]+i +j,types[j], names[j]+"2"+i +j);
					System.out.printf(" return unpack([%-11s(%-5s),%-11s(%-5s)]);",p.hor.name,names[i]+i +j, p.ver.name,   names[j]+"2"+i +j);
					System.out.printf(" }\n");
				}
			}
		}
	}
}
