package org.rascalmpl.library;

import org.eclipse.imp.pdb.facts.IValueFactory;

public class Matrix {
	private final IValueFactory values;
	
	public Matrix(IValueFactory values){
		super();
		this.values = values;
	}

	
//	public IValue closure(IList carrier, IRelation rel) {
//		int dim = carrier.length();
//		long[] dims = new long[] {dim, dim};
//		
//		SparseMatrix m = new DefaultSparseDoubleMatrix(dims);
//		SparseMatrix id = new DefaultSparseDoubleMatrix(dims);
//		
//		IValue[] indices = new IValue[dim];
//		
//		// This nested loop performs three tasks
//		// - constructing the sparse matrix representation of rel
//		// - constructing identity(dim)
//		// - populating the indices array which is used to convert back to rel
//		
//		int i = 0;
//		for (IValue a: carrier) {
//			indices[i] = a;
//			
//			java.util.Map<IValue, IReal> map = new HashMap<IValue, IReal>();
//			for (IValue v: rel) {
//				ITuple t = (ITuple)v;
//				if (t.get(0).isEqual(a)) {
//					map.put(t.get(1), (IReal)t.get(2));
//				}
//			}
//			int j = 0;
//			for (IValue b: carrier) {
//				if (i == j) {
//					id.setAsBigDecimal(BigDecimal.ONE, i, j);
//				}
//				if (map.containsKey(b)) {
//					IReal ir = (IReal)map.get(b);
//					m.setAsDouble(ir.doubleValue(), i, j);
//				}
//				j++;
//			}
//			i++;
//		}
//		
//		org.ujmp.core.Matrix result = id.minus(m).inv();
//		
//		IRelationWriter w = values.relationWriter(rel.getElementType());
//		
//		for (long[] coords: result.availableCoordinates()) {
//			int a = (int) coords[0];
//			int b = (int) coords[1];
//			IValue x = indices[a];
//			IValue y = indices[b];
//			double d = result.getAsDouble(a, b);
//			if (d != 0.0) { 
//				w.insert(values.tuple(x, y, values.real(d)));
//			}
//		}
//
//		
//		return w.done();
//	}
//	
//	public IValue invert(IList carrier, IRelation rel) {
//		int dim = carrier.length();
//		long[] dims = new long[] {dim, dim};
//		
//		SparseMatrix m = new DefaultSparseDoubleMatrix(dims);
//		
//		IValue[] indices = new IValue[dim];
//		
//		// This nested loop performs three tasks
//		// - constructing the sparse matrix representation of rel
//		// - populating the indices array which is used to convert back to rel
//		
//		int i = 0;
//		for (IValue a: carrier) {
//			indices[i] = a;
//			
//			java.util.Map<IValue, IReal> map = new HashMap<IValue, IReal>();
//			for (IValue v: rel) {
//				ITuple t = (ITuple)v;
//				if (t.get(0).isEqual(a)) {
//					map.put(t.get(1), (IReal)t.get(2));
//				}
//			}
//			int j = 0;
//			for (IValue b: carrier) {
//				if (map.containsKey(b)) {
//					m.setAsDouble(((IReal)map.get(b)).doubleValue(), i, j);
//				}
//				j++;
//			}
//			i++;
//		}
//		
//		org.ujmp.core.Matrix result = m.inv();
//		
//		IRelationWriter w = values.relationWriter(rel.getElementType());
//		
//		for (long[] coords: result.availableCoordinates()) {
//			int a = (int) coords[0];
//			int b = (int) coords[1];
//			IValue x = indices[a];
//			IValue y = indices[b];
//			double d = result.getAsDouble(a, b);
//			if (d != 0.0) { 
//				w.insert(values.tuple(x, y, values.real(d)));
//			}
//		}
//		
//		return w.done();
//	}
}
