/*******************************************************************************
 * Copyright (c) 2009-2013 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
package org.rascalmpl.library.vis.util;


public class LinearSolver {
	
	class TridiagonalRow{
		double a, b, c, d;
		void normalize(){
			a/=b;
			c/=b;
			d/=b;
			b = 1;
		}
		
		void substract(double factor, TridiagonalRow other){
			a-=other.a*factor;
			b-=other.b*factor;
			c-=other.c*factor;
			d-=other.d*factor;
		}
	}
	
//	
//	public static double[] solveTridiagonal(TridiagonalRow[] rows){
//		
//		// downwards sweep
//		TridiagonalRow prev = rows[0];
//		for(int i = 1 ; i < rows.length ; i++){
//			TridiagonalRow cur = rows[i];
//			cur.substract(cur.a/prev.b, prev);
//			prev = cur;
//		}
//		// upwards sweep
//		prev = rows[rows.length-1];
//		for(int i = rows.length - 1 ; i >= 0 ; i--){
//			TridiagonalRow cur = rows[i];
//			cur.substract(cur.c/prev.b, prev);
//			prev = cur;
//		}
//		for(TridiagonalRow r : rows){
//			r.normalize();
//		}
//	}
	
	// Solves a triadiagonal matrix, see http://en.wikipedia.org/wiki/Tridiagonal_matrix_algorithm
//	public static double[] solveTridiagonal(double[] a, double []b, double[] c, double[] d){
//		// forward sweep
//		c[0] = c[0]/b[0];
//		d[0] = d[0]/b[0];
//		for(int i = 1 ; i < c.length ; i++){
//			double m = c[i-1] * a[i];
//			c[i] = c[i]/(b[i]-m);
//			d[i] = (d[i] - d[i-1]*a[i])/(b[i] - m);
//		}
//		// backward sweep
//		int last = c.length -1;
//		double[] res = new double[c.length];
//		res[last] = d[last];
//		for(int i = last - 1 ; i >= 0 ; i--){
//			res[i] = d[i] - c[i]*res[i+1];
//		}
//		return res;
//	}
	
	// Solves a system of linear equations
	// assumes a that all rows are of same length, the last elements is the constant
	// only works when forall i.coefficients[i][i] != 0 
	// and nrEquations == nrUnkowns i.e. coefficients.length == coefficients[0].length - 1
	// also the system should be solvable :)
	// works in place!
	// uses partial pivoting for more numerical stability (see wikipeadia gaussian elim)
	public static void gaussianElim(double[][] coefficients){
		toRowEchelonForm(coefficients);
		toRowCaconicalForm(coefficients);
		toRowCaconicalForm(coefficients);
	}

	private static void toRowEchelonForm(double[][] coefficients) {
		for(int curUnkown = 0 ; curUnkown < coefficients.length ; curUnkown++){
			swapMax(coefficients, curUnkown);
			normalize(coefficients,curUnkown);
			substractFromRest(coefficients,curUnkown);
		}
	}

	private static void normalize(double[][] coefficients, int curUnkown){
		double factor = coefficients[curUnkown][curUnkown];
		for(int i = 0 ; i < coefficients.length+1; i++){
			coefficients[curUnkown][i]/=factor;
		}
	}
	
	private static void substractFromRest(double[][] coefficients, int curUnkown) {
		int nrEqs ;
		nrEqs  = coefficients.length;
		for(int curEq = curUnkown + 1 ; curEq < nrEqs ; curEq++){
			substractToEliminate(coefficients, curUnkown, curEq);
		}
	}

	private static void substractToEliminate(double[][] coefficients,
			int curUnkown, int curEq) {
		double factor = coefficients[curEq][curUnkown] / coefficients[curUnkown][curUnkown];
		for(int i = 0 ; i < coefficients.length + 1 ; i++){
			coefficients[curEq][i] -= factor *  coefficients[curUnkown][i];
		}
	}

	private static void swapMax(double[][] coefficients, 
			int curUnkown) {
		int maxUnkownEq = 0;
		for(int curEq = 1 ; curEq < coefficients.length ; curEq++){
			if(coefficients[curEq][curUnkown] > coefficients[maxUnkownEq][curUnkown]){
				maxUnkownEq = curEq;
			}
		}
		double[] tmp = coefficients[maxUnkownEq];
		coefficients[maxUnkownEq] = coefficients[curUnkown];
		coefficients[curUnkown] = tmp;
	}
	
	//assumes row echelon form
	private static void toRowCaconicalForm(double[][] coefficients){
		for(int curUnkown = coefficients.length-1; curUnkown >= 0 ; curUnkown--){
			for(int i = 0 ; i < curUnkown ; i++){
				substractToEliminate(coefficients,curUnkown,i);
			}
		}
	}
	
}
