/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class Util {
	
	public static <T> void makeRectangular(T[][] source, T pad){
		int nrRows = source.length;
		int nrColumns = 0;
		for(T[] row : source){
			nrColumns = Math.max(nrColumns, row.length);
		}
		for(int row = 0 ; row < nrRows ; row++){
			int oldLength = source[row].length;
			if(oldLength == nrColumns) continue;
			T[] newRow = Arrays.copyOf(source[row], nrColumns);
			Arrays.fill(newRow, oldLength, nrColumns,pad);
			source[row]=newRow;
		}
	}
	
	
	/*
	 * Given n borders of interval returns the index of the interval in which the value lies
	 * -1 means (-infinity, intervalBorders[0]);
	 * 0 means  (intervalBorders[0],intervalBorders[1])
	 * ...
	 * intervalBorders.length - 1 means (intervalBorders[intervalBorders.length -1], infinity) 
	 */
	public static int binaryIntervalSearch(double[] intervalBorders, double toFind){
		int minIndex = -1; // lowest index of an intervalBorder <= toFind
		int maxIndex = intervalBorders.length ;// highest index of an intervalBorder > toFind
		while(maxIndex - minIndex > 1){
			int mid = ( maxIndex + minIndex ) >>> 1;
			if(toFind <= intervalBorders[mid]) maxIndex = mid;
			else minIndex = mid; 
		}
		return minIndex;
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T[] flatten(Class<T> t, T[][] m){
		int size = 0;
		for(T[] mr : m){
			size+= mr.length;
		}
		T[] result = (T[]) Array.newInstance(t, size);
		int i = 0;
		for(T[] mr : m){
			for(T e: mr) {
				result[i] = e;
				i++;
			}
		}
		return result;
	}
	
	public static boolean any(boolean[] vals){
		for(boolean b : vals) { if(b) return true; }
		return false;
	}

	public static double sum(double[] vals){
		double result = 0.0;
		for(double b : vals) { result+=b; }
		return result;
	}
	
	public static int count(double[] vals,double toCount){
		int result = 0;
		for(double b : vals) { if(b == toCount) result++; }
		return result;
	}
	
	public static void mapMul(double[] vals,double factor){
		for(int i = 0 ; i  < vals.length ; i++){
			vals[i]*=factor;
		}
	}
	
	public static void replaceVal(double[] vals, double from, double to){
		for(int i = 0 ; i  < vals.length ; i++){
			if(vals[i]==from){
				vals[i] = to;
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	static public <T> ArrayList<T> merge(List<T> lhs,List<T> rhs){
		return merge(NaturalComparator.instance,lhs,rhs);
	}
	
	static public <T> ArrayList<T> merge(Comparator<T> comp,List<T> lhs,List<T> rhs){
		 ArrayList<T> result = new ArrayList<T>(lhs.size() + rhs.size());
		int i, j;
		i = j = 0;
		while(i < lhs.size() || j < rhs.size()){
			int cmp;
			if(i == lhs.size()){
				cmp = 1;
			} else if (j == rhs.size()){
				cmp = -1;
			} else {
				cmp = comp.compare(lhs.get(i),rhs.get(j));
			}
			if(cmp < 0){
				result.add(lhs.get(i));
				i++;
			} else if(cmp == 0){
				result.add(lhs.get(i));
				result.add(rhs.get(j));
				i++;
				j++;
			} else {
				result.add(rhs.get(j));
				j++;
			}
		}
		return result;
	}
	
	/* Compares elements of lhs and rhs fills in the three argument vectors with elements that are only in the left, in both, or only in the right
	 * vector. lhs and rhs should be sorted!
	 * 
	 * O(n) where n is the max(lhs.size(),rhs.size())
	 */
	@SuppressWarnings("unchecked")
	static public <T> void diffSorted(List<T> lhs, List<T> rhs, List<T> inLeft, List<T> inBoth, List<T> inRight){
		diffSorted(lhs,rhs,inLeft,inBoth,inRight,NaturalComparator.instance);
	}
	
	static public <T> void diffSorted(List<T> lhs, List<T> rhs, List<T> inLeft, List<T> inBoth, List<T> inRight, Comparator<T> comp){
		int i, j;
		i = j = 0;
		while(i < lhs.size() || j < rhs.size()){
			int cmp;
			if(i == lhs.size()){
				cmp = 1;
			} else if (j == rhs.size()){
				cmp = -1;
			} else {
				cmp = comp.compare(lhs.get(i),rhs.get(j));
			}
			if(cmp < 0){
				inLeft.add(lhs.get(i));
				i++;
			} else if(cmp == 0){
				inBoth.add(lhs.get(i));
				i++;
				j++;
			} else {
				inRight.add(rhs.get(j));
				j++;
			}
		}
	}
	
	public static String tabs2spaces(int tabWidth, String in){
		int from = 0;
		int where;
		StringBuffer b = new StringBuffer(in.length()*2);
		while((where = in.indexOf('\t', from)) != -1){
			if(from <= where){
				b.append(in.subSequence(from,where-1));
			}
			int nrSpaces = tabWidth - (b.length() % tabWidth);
			for(int i = 0 ; i < nrSpaces ; i++){
				b.append(' ');
			}
			from = where + 1;
		}
		if(from > 0){
			b.append(in.substring(from));
		}
		return b.toString();
	}
	
	public static String intercalate(String between,String[] s){
		StringBuffer b = new StringBuffer();
		for(int i = 0 ; i < s.length ; i++){
			b.append(s[i]);
			if(i != s.length-1){
				b.append(between);
			}
		}
		return b.toString();
	}
	
	
}
