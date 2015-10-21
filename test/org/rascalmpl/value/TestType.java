/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation, 2008 CWI
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Robert Fuhrer (rfuhrer@watson.ibm.com) - initial API and implementation
 *    Jurgen Vinju (jurgen@vinju.org)
 *    Anya Helene Bagge
 *******************************************************************************/

package org.rascalmpl.value;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.rascalmpl.value.exceptions.FactTypeDeclarationException;
import org.rascalmpl.value.exceptions.FactTypeUseException;
import org.rascalmpl.value.random.RandomTypeGenerator;
import org.rascalmpl.value.type.Type;
import org.rascalmpl.value.type.TypeFactory;
import org.rascalmpl.value.type.TypeStore;

import junit.framework.TestCase;

public class TestType extends TestCase {
	private static final int COMBINATION_UPPERBOUND = 5;

	private static TypeFactory ft = TypeFactory.getInstance();
	private static TypeStore ts = new TypeStore();

	private static List<Type> basic = new LinkedList<>();
	private static List<Type> allTypes = new LinkedList<>();

	static {
		try {
			basic.add(ft.integerType());
			basic.add(ft.realType());
			basic.add(ft.sourceLocationType());
			basic.add(ft.stringType());
			basic.add(ft.nodeType());

			allTypes.add(ft.valueType());
			allTypes.add(ft.numberType());
			allTypes.addAll(basic);

			for (int i = 0; i < 2; i++) {
				recombine();
			}
			
			RandomTypeGenerator rg = new RandomTypeGenerator();
			for (int i = 0; i < 1000; i++) {
			  allTypes.add(rg.next(10));
			}
			
		} catch (FactTypeUseException e) {
			throw new RuntimeException("fact type error in setup", e);
		}
	}

	private static void recombine() throws FactTypeUseException {
		List<Type> newTypes = new LinkedList<>();
		int max1 = COMBINATION_UPPERBOUND;

		for (Type t1 : allTypes) {
			newTypes.add(ft.tupleType(t1));
			newTypes.add(ft.relType(t1));
			newTypes.add(ft.setType(t1));
			newTypes.add(ft.aliasType(ts, "type_" + allTypes.size()
					+ newTypes.size(), t1));
			Type adt = ft.abstractDataType(ts, "adt_" + newTypes.size());
			newTypes.add(ft.constructor(ts, adt, "cons_" + newTypes.size()));
			newTypes.add(adt);
			
			int max2 = COMBINATION_UPPERBOUND;

			for (Type t2 : allTypes) {
				newTypes.add(ft.tupleType(t1, t2));
				newTypes.add(ft.tupleType(t1, "a" + newTypes.size(), t2, "b" + newTypes.size()));
				newTypes.add(ft.relType(t1, t2));
				newTypes.add(ft.mapType(t1, t2));
				newTypes.add(ft.mapType(t1, "a" + newTypes.size(), t2, "b" + newTypes.size()));
				newTypes.add(ft.constructor(ts, adt, "cons_" + newTypes.size(), t1, "a" + newTypes.size(), t2, "b" + newTypes.size()));
				int max3 = COMBINATION_UPPERBOUND;

				for (Type t3 : allTypes) {
					newTypes.add(ft.tupleType(t1, t2, t3));
					newTypes.add(ft.relType(t1, t2, t3));
					newTypes.add(ft.constructor(ts, adt, "cons_" + newTypes.size(), t1, "a" + newTypes.size(), t2, "b"+ newTypes.size(), t3, "c" + newTypes.size()));
					
					if (max3-- == 0) {
						break;
					}
				}
				if (max2-- == 0) {
					break;
				}
			}

			if (max1-- == 0) {
				break;
			}
		}

		allTypes.addAll(newTypes);
	}

	public void testRelations() {
		for (Type t : allTypes) {
			if (t.isSet() && t.getElementType().isTuple()
					&& !t.isRelation()) {
				fail("Sets of tuples should be relations");
			}
			if (t.isRelation() && !t.getElementType().isTuple()) {
				fail("Relations should contain tuples");
			}
		}
	}

	public void testParameterizedAlias() {
		Type T = ft.parameterType("T");
		TypeStore ts = new TypeStore();
		// DiGraph[&T] = rel[&T from ,&T to]
		Type DiGraph = ft.aliasType(ts, "DiGraph", ft.relType(T, "from", T, "to"),
				T);
		Type IntInstance = ft.relType(ft.integerType(), ft.integerType());
		Type ValueInstance = ft.relType(ft.valueType(), ft.valueType());

		// before instantiation, the parameterized type rel[&T, &T] is a
		// sub-type of rel[value, value]
		assertTrue(IntInstance.isSubtypeOf(DiGraph));
		assertFalse(DiGraph.isSubtypeOf(IntInstance));
		assertTrue(DiGraph.isSubtypeOf(ValueInstance));

		Map<Type, Type> bindings = new HashMap<>();
		DiGraph.match(IntInstance, bindings);
		assertTrue(bindings.get(T) == ft.integerType());

		// after instantiation, the parameterized type is an alias for rel[int,
		// int]
		Type ComputedInstance = DiGraph.instantiate(bindings); // DiGraph[int]
		assertTrue(ComputedInstance.equivalent(IntInstance));
		assertFalse(ValueInstance.isSubtypeOf(ComputedInstance));

		// and sub-typing remains co-variant:
		assertTrue(IntInstance.isSubtypeOf(ValueInstance));
		assertTrue(ComputedInstance.isSubtypeOf(ValueInstance));

		try {
			ft.aliasType(ts, "DiGraph", ft.setType(T), T);
			fail("should not be able to redefine alias");
		} catch (FactTypeDeclarationException e) {
			// this should happen
		}
	}

	public void testADT() {
		Type E = ft.abstractDataType(ts, "E");

		assertTrue(
				"Abstract data-types are composed of constructors which are tree nodes",
				E.isSubtypeOf(ft.nodeType()));

		assertTrue(E.isSubtypeOf(ft.valueType()));
		assertTrue(E.isSubtypeOf(ft.nodeType()));
		assertTrue(E.lub(ft.nodeType()).isNode());
		assertTrue(ft.nodeType().lub(E).isNode());
		
		Type f = ft.constructor(ts, E, "f", ft.integerType(), "i");
		Type g = ft.constructor(ts, E, "g", ft.integerType(), "j");

		assertTrue(f.isSubtypeOf(ft.nodeType()));
		
		assertTrue(f.lub(ft.nodeType()).isNode());
		assertTrue(ft.nodeType().lub(f).isNode());
		
		
		Type a = ft.aliasType(ts, "a", ft.integerType());

		assertFalse(f.isSubtypeOf(ft.integerType())
				|| f.isSubtypeOf(ft.stringType()) || f.isSubtypeOf(a));
		assertFalse(g.isSubtypeOf(ft.integerType())
				|| g.isSubtypeOf(ft.stringType()) || g.isSubtypeOf(a));
		assertFalse("constructors are subtypes of the adt", !f.isSubtypeOf(E)
				|| !g.isSubtypeOf(E));

		assertFalse("alternative constructors should be incomparable", f
				.isSubtypeOf(g)
				|| g.isSubtypeOf(f));

		assertTrue("A constructor should be a node", f.isSubtypeOf(ft
				.nodeType()));
		assertTrue("A constructor should be a node", g.isSubtypeOf(ft
				.nodeType()));
	}

	public void testVoid() {
		for (Type t : allTypes) {
			if(t.isSubtypeOf(ft.voidType())) {
				assertFalse(true);
			}
		}
	}
	
	public void testVoidProblem1() {
	  assertFalse(ft.listType(ft.voidType()).isSubtypeOf(ft.voidType()));
	  assertFalse(ft.setType(ft.voidType()).isSubtypeOf(ft.voidType()));
	  assertFalse(ft.relType(ft.voidType()).isSubtypeOf(ft.voidType()));
	  assertFalse(ft.tupleType(ft.voidType()).isSubtypeOf(ft.voidType()));
	  assertFalse(ft.mapType(ft.voidType(),ft.voidType()).isSubtypeOf(ft.voidType()));
	}
	
	public void testIsSubtypeOf() {
		for (Type t : allTypes) {
			if (!t.isSubtypeOf(t)) {
				fail("any type should be a subtype of itself: " + t);
			}

			if (t.isSet() && t.getElementType().isTuple()
					&& !t.isRelation()) {
				fail("Sets of tuples should be relations");
			}
		}

		for (Type t1 : allTypes) {
			for (Type t2 : allTypes) {
				assertEquals(t1.equivalent(t2), t1.isSubtypeOf(t2) && t2.isSubtypeOf(t1)); 
			}
		}

		for (Type t1 : allTypes) {
			for (Type t2 : allTypes) {
				if (t1.isSubtypeOf(t2)) {
					for (Type t3 : allTypes) {
						if (t2.isSubtypeOf(t3)) {
							if (!t1.isSubtypeOf(t3)) {
								System.err.println("FAILURE");
								System.err.println("\t" + t1 + " <= " + t2
										+ " <= " + t3);
								System.err.println("\t" + t1 + " !<= " + t3);
								fail("subtype should be transitive: " + t1 + ", " + t2 + ", " + t3);
							}
						}
					}
				}
			}
		}
	}

	public void testEquiv() {
		for (Type t : allTypes) {
			if (!t.equals(t)) {
				fail("any type should be equal to itself: " + t);
			}
			if (!t.equivalent(t)) {
				fail("any type should be equivalent to itself: " + t);
			}
		}

		for (Type t1 : allTypes) {
			for (Type t2 : allTypes) {
				if (t1.equals(t2) && !t2.equals(t1)) {
					fail("equals() should be symmetric: " + t1 + ", " + t2);
				}
				if (t1.equivalent(t2) && !t2.equivalent(t1)) {
					fail("equivalent() should be symmetric: " + t1 + ", " + t2);
				}
			}
		}

		for (Type t1 : allTypes) {
			for (Type t2 : allTypes) {
				if (t1.equals(t2) || t1.equivalent(t2)) {
					for (Type t3 : allTypes) {
						if (t1.equals(t2) && t2.equals(t3)) {
							if (!t1.equals(t3)) {
								fail("equals() should be transitive: " + t1 + ", " + t2 + ", " + t3);
							}
						}
						if (t1.equivalent(t2) && t2.equivalent(t3)) {
							if (!t1.equivalent(t3)) {
								fail("equivalent() should be transitive: " + t1 + ", " + t2 + ", " + t3);
							}
						}
					}
				}
			}
		}
	}

	public void testLub() {
		for (Type t : allTypes) {
			if (t.lub(t) != t) {
				fail("lub should be idempotent: " + t + " != " + t.lub(t));
			}
		}

		for (Type t1 : allTypes) {
			for (Type t2 : allTypes) {
				Type lub1 = t1.lub(t2);
				Type lub2 = t2.lub(t1);

				if (lub1 != lub2) {
					System.err.println("Failure:");
					System.err.println(t1 + ".lub(" + t2 + ") = " + lub1);
					System.err.println(t2 + ".lub(" + t1 + ") = " + lub2);
					fail("lub should be commutative");
				}
				
				 if (t1.comparable(t2)) {
	          if (t1.isSubtypeOf(t2)) {
	            assertTrue(t1.lub(t2).equivalent(t2));
	          }
	          if (t2.isSubtypeOf(t1)) {
	            assertTrue(t1.lub(t2).equivalent(t1));
	          }
	        }
			}
		}
		
		for (Type t1 : allTypes) {
			if (!t1.isAliased() && t1.lub(TypeFactory.getInstance().voidType()) != t1) {
				System.err.println(t1 + " lub void is not " + t1 + "? its "+ t1.lub(TypeFactory.getInstance().voidType()));
				fail("void should be bottom: " + t1 + ".lub = " + t1.lub(TypeFactory.getInstance().voidType()));
			}
			if (t1.isAliased() && t1.lub(TypeFactory.getInstance().voidType()) != t1.getAliased()) {
				fail("void should be bottom:" + t1);
			}
			if (t1.lub(TypeFactory.getInstance().valueType()) != TypeFactory.getInstance().valueType()) {
				System.err.println(t1 + " lub value is not value?");
				fail("value should be top:" + t1);
			}
		}
	}

	public void testGlb() {
    for (Type t : allTypes) {
      if (t.glb(t) != t) {
        fail("glb should be idempotent: " + t + " != " + t.glb(t));
      }
    }

    for (Type t1 : allTypes) {
      for (Type t2 : allTypes) {
        Type glb1 = t1.glb(t2);
        Type glb2 = t2.glb(t1);

        if (glb1 != glb2) {
          System.err.println("Failure:");
          System.err.println(t1 + ".glb(" + t2 + ") = " + glb1);
          System.err.println(t2 + ".glb(" + t1 + ") = " + glb2);
          fail("glb should be commutative");
        }
        
        if (t1.comparable(t2)) {
          if (t1.isSubtypeOf(t2)) {
            assertTrue(t1.glb(t2).equivalent(t1));
          }
          if (t2.isSubtypeOf(t1)) {
            assertTrue(t1.glb(t2).equivalent(t2));
          }
        }
      }
    }
    
    for (Type t1 : allTypes) {
      if (!t1.isAliased() && t1.glb(TypeFactory.getInstance().valueType()) != t1) {
        System.err.println(t1 + " glb value is not " + t1 + "? its "+ t1.glb(TypeFactory.getInstance().valueType()));
        fail("value should be top: " + t1 + ".lub = " + t1.lub(TypeFactory.getInstance().valueType()));
      }
      if (t1.isAliased() && t1.glb(TypeFactory.getInstance().valueType()) != t1.getAliased()) {
        fail("value should be top:" + t1);
      }
      if (t1.glb(TypeFactory.getInstance().voidType()) != TypeFactory.getInstance().voidType()) {
        System.err.println(t1 + " glb void is not void?");
        fail("void should be bottom:" + t1);
      }
    }
  }
	public void testGetTypeDescriptor() {
		int count = 0;
		for (Type t1 : allTypes) {
			for (Type t2 : allTypes) {
				if (t1.toString().equals(t2.toString())) {
					if (t1 != t2) {
						System.err
								.println("Type descriptors should be canonical:"
										+ t1.toString()
										+ " == "
										+ t2.toString());
					}
				}
				if (count++ > 10000) {
					return;
				}
			}
		}
	}

	public void testMatchAndInstantiate() {
		Type X = ft.parameterType("X");
		Map<Type, Type> bindings = new HashMap<>();

		Type subject = ft.integerType();
		X.match(subject, bindings);

		if (!bindings.get(X).equals(subject)) {
			fail("simple match failed");
		}

		if (!X.instantiate(bindings).equals(subject)) {
			fail("instantiate failed");
		}

		Type relXX = ft.relType(X, X);
		bindings.clear();
		subject = ft.relType(ft.integerType(), ft.integerType());
		relXX.match(subject, bindings);

		if (!bindings.get(X).equals(ft.integerType())) {
			fail("relation match failed");
		}

		if (!relXX.instantiate(bindings).equals(subject)) {
			fail("instantiate failed");
		}

		bindings.clear();
		subject = ft.relType(ft.integerType(), ft.realType());
		relXX.match(subject, bindings);

		Type lub = ft.integerType().lub(ft.realType());
		if (!bindings.get(X).equals(lub)) {
			fail("lubbing during matching failed");
		}

		if (!relXX.instantiate(bindings).equals(ft.relType(lub, lub))) {
			fail("instantiate failed");
		}

	}
	
	public void testAlias() {
		Type alias = ft.aliasType(new TypeStore(), "myValue", ft.valueType());
		
		assertTrue(alias.isSubtypeOf(ft.valueType()));
		assertTrue(ft.valueType().isSubtypeOf(alias));
	}

}
