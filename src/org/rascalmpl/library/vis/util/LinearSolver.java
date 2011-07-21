package org.rascalmpl.library.vis.util;


public class LinearSolver {
	
	
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
