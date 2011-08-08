package org.rascalmpl.library.lang.rascal.syntax;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IValueFactory;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.gtd.stack.*;
import org.rascalmpl.parser.gtd.stack.filter.*;
import org.rascalmpl.parser.gtd.stack.filter.follow.*;
import org.rascalmpl.parser.gtd.stack.filter.match.*;
import org.rascalmpl.parser.gtd.stack.filter.precede.*;
import org.rascalmpl.parser.gtd.preprocessing.ExpectBuilder;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.IntegerMap;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.uptr.Factory;

public class RascalRascal extends org.rascalmpl.parser.gtd.SGTDBF {
  protected final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
  
  protected static IValue _read(java.lang.String s, org.eclipse.imp.pdb.facts.type.Type type) {
    try {
      return new StandardTextReader().read(VF, org.rascalmpl.values.uptr.Factory.uptr, type, new ByteArrayInputStream(s.getBytes()));
    }
    catch (FactTypeUseException e) {
      throw new RuntimeException("unexpected exception in generated parser", e);  
    } catch (IOException e) {
      throw new RuntimeException("unexpected exception in generated parser", e);  
    }
  }
	
  protected static java.lang.String _concat(String ...args) {
    int length = 0;
    for (java.lang.String s :args) {
      length += s.length();
    }
    java.lang.StringBuilder b = new java.lang.StringBuilder(length);
    for (java.lang.String s : args) {
      b.append(s);
    }
    return b.toString();
  }
  protected static final TypeFactory _tf = TypeFactory.getInstance();
  
  private static final IntegerMap _resultStoreIdMappings;
  private static final IntegerKeyedHashMap<IntegerList> _dontNest;
	
  protected static void _putDontNest(IntegerKeyedHashMap<IntegerList> result, int parentId, int childId) {
    IntegerList donts = result.get(childId);
    if (donts == null) {
      donts = new IntegerList();
      result.put(childId, donts);
    }
    donts.add(parentId);
  }
    
  protected int getResultStoreId(int parentId) {
    return _resultStoreIdMappings.get(parentId);
  }
    
  protected static IntegerKeyedHashMap<IntegerList> _initDontNest() {
    IntegerKeyedHashMap<IntegerList> result = new IntegerKeyedHashMap<IntegerList>(); 
    
    
    _putDontNest(result, -2518, -2553);
    _putDontNest(result, -2521, -2558);
    _putDontNest(result, -2499, -2508);
    _putDontNest(result, -2667, -2667);
    _putDontNest(result, -2589, -2617);
    _putDontNest(result, -2594, -2608);
    _putDontNest(result, -2563, -2563);
    _putDontNest(result, -2627, -2627);
    _putDontNest(result, -2603, -2603);
    _putDontNest(result, -2622, -2652);
    _putDontNest(result, -2483, -2642);
    _putDontNest(result, -2559, -2662);
    _putDontNest(result, -2544, -2667);
    _putDontNest(result, -2548, -2578);
    _putDontNest(result, -2554, -2588);
    _putDontNest(result, -2514, -2517);
    _putDontNest(result, -164, -164);
    _putDontNest(result, -2320, -2482);
    _putDontNest(result, -705, -709);
    _putDontNest(result, -2599, -2647);
    _putDontNest(result, -2564, -2622);
    _putDontNest(result, -2573, -2617);
    _putDontNest(result, -2604, -2598);
    _putDontNest(result, -2543, -2662);
    _putDontNest(result, -2456, -2563);
    _putDontNest(result, -2467, -2642);
    _putDontNest(result, -2518, -2637);
    _putDontNest(result, -2553, -2558);
    _putDontNest(result, -404, -419);
    _putDontNest(result, -2320, -2466);
    _putDontNest(result, -2791, -2881);
    _putDontNest(result, -2652, -2662);
    _putDontNest(result, -2583, -2647);
    _putDontNest(result, -2584, -2642);
    _putDontNest(result, -2588, -2598);
    _putDontNest(result, -2472, -2563);
    _putDontNest(result, -2451, -2642);
    _putDontNest(result, -2515, -2583);
    _putDontNest(result, -2539, -2548);
    _putDontNest(result, -2320, -2514);
    _putDontNest(result, -2807, -2881);
    _putDontNest(result, -2578, -2608);
    _putDontNest(result, -2568, -2642);
    _putDontNest(result, -2861, -2876);
    _putDontNest(result, -2574, -2652);
    _putDontNest(result, -2320, -2652);
    _putDontNest(result, -2488, -2563);
    _putDontNest(result, -2511, -2662);
    _putDontNest(result, -2514, -2657);
    _putDontNest(result, -2499, -2583);
    _putDontNest(result, -2472, -2543);
    _putDontNest(result, -2320, -2498);
    _putDontNest(result, -2603, -2667);
    _putDontNest(result, -2563, -2627);
    _putDontNest(result, -2548, -2647);
    _putDontNest(result, -2559, -2598);
    _putDontNest(result, -2544, -2603);
    _putDontNest(result, -2553, -2652);
    _putDontNest(result, -2483, -2583);
    _putDontNest(result, -2456, -2511);
    _putDontNest(result, -2451, -2508);
    _putDontNest(result, -2488, -2543);
    _putDontNest(result, -2579, -2627);
    _putDontNest(result, -2604, -2662);
    _putDontNest(result, -2518, -2573);
    _putDontNest(result, -2456, -2627);
    _putDontNest(result, -2543, -2598);
    _putDontNest(result, -2549, -2608);
    _putDontNest(result, -2467, -2583);
    _putDontNest(result, -60, -124);
    _putDontNest(result, -2467, -2508);
    _putDontNest(result, -2472, -2511);
    _putDontNest(result, -518, -510);
    _putDontNest(result, -2584, -2583);
    _putDontNest(result, -2593, -2637);
    _putDontNest(result, -2618, -2632);
    _putDontNest(result, -2588, -2662);
    _putDontNest(result, -2647, -2647);
    _putDontNest(result, -2515, -2642);
    _putDontNest(result, -2472, -2627);
    _putDontNest(result, -2521, -2652);
    _putDontNest(result, -2554, -2617);
    _putDontNest(result, -2451, -2583);
    _putDontNest(result, -2488, -2511);
    _putDontNest(result, -2320, -2455);
    _putDontNest(result, -2456, -2543);
    _putDontNest(result, -2511, -2520);
    _putDontNest(result, -2483, -2508);
    _putDontNest(result, -2568, -2583);
    _putDontNest(result, -2573, -2588);
    _putDontNest(result, -2872, -2881);
    _putDontNest(result, -2609, -2637);
    _putDontNest(result, -2632, -2642);
    _putDontNest(result, -2488, -2627);
    _putDontNest(result, -2499, -2642);
    _putDontNest(result, -2514, -2593);
    _putDontNest(result, -2511, -2598);
    _putDontNest(result, -2598, -2593);
    _putDontNest(result, -2604, -2603);
    _putDontNest(result, -2593, -2608);
    _putDontNest(result, -2579, -2622);
    _putDontNest(result, -518, -521);
    _putDontNest(result, -606, -708);
    _putDontNest(result, -2554, -2662);
    _putDontNest(result, -2499, -2637);
    _putDontNest(result, -2548, -2583);
    _putDontNest(result, -2553, -2588);
    _putDontNest(result, -419, -419);
    _putDontNest(result, -2508, -2548);
    _putDontNest(result, -2320, -2487);
    _putDontNest(result, -2563, -2622);
    _putDontNest(result, -2578, -2573);
    _putDontNest(result, -2574, -2617);
    _putDontNest(result, -2603, -2598);
    _putDontNest(result, -2642, -2637);
    _putDontNest(result, -2599, -2642);
    _putDontNest(result, -577, -709);
    _putDontNest(result, -2320, -2617);
    _putDontNest(result, -2515, -2637);
    _putDontNest(result, -2508, -2632);
    _putDontNest(result, -2553, -2667);
    _putDontNest(result, -2451, -2573);
    _putDontNest(result, -2320, -2471);
    _putDontNest(result, -2584, -2647);
    _putDontNest(result, -2589, -2652);
    _putDontNest(result, -2583, -2642);
    _putDontNest(result, -2467, -2573);
    _putDontNest(result, -2515, -2578);
    _putDontNest(result, -2521, -2588);
    _putDontNest(result, -2518, -2538);
    _putDontNest(result, -2568, -2647);
    _putDontNest(result, -2573, -2652);
    _putDontNest(result, -2588, -2603);
    _putDontNest(result, -2652, -2667);
    _putDontNest(result, -2521, -2667);
    _putDontNest(result, -2483, -2573);
    _putDontNest(result, -2499, -2578);
    _putDontNest(result, -2564, -2627);
    _putDontNest(result, -2598, -2657);
    _putDontNest(result, -2604, -2667);
    _putDontNest(result, -2514, -2622);
    _putDontNest(result, -2499, -2573);
    _putDontNest(result, -2554, -2598);
    _putDontNest(result, -2549, -2647);
    _putDontNest(result, -2483, -2578);
    _putDontNest(result, -2603, -2662);
    _putDontNest(result, -2578, -2637);
    _putDontNest(result, -2627, -2622);
    _putDontNest(result, -2569, -2632);
    _putDontNest(result, -513, -709);
    _putDontNest(result, -2508, -2568);
    _putDontNest(result, -2515, -2573);
    _putDontNest(result, -2511, -2617);
    _putDontNest(result, -2553, -2603);
    _putDontNest(result, -2451, -2637);
    _putDontNest(result, -2544, -2652);
    _putDontNest(result, -2548, -2608);
    _putDontNest(result, -2467, -2578);
    _putDontNest(result, -60, -121);
    _putDontNest(result, -2647, -2642);
    _putDontNest(result, -2617, -2632);
    _putDontNest(result, -2653, -2652);
    _putDontNest(result, -2594, -2637);
    _putDontNest(result, -2518, -2642);
    _putDontNest(result, -2559, -2617);
    _putDontNest(result, -2467, -2637);
    _putDontNest(result, -2451, -2578);
    _putDontNest(result, -2569, -2583);
    _putDontNest(result, -2564, -2588);
    _putDontNest(result, -2637, -2652);
    _putDontNest(result, -2871, -2881);
    _putDontNest(result, -2632, -2647);
    _putDontNest(result, -2588, -2667);
    _putDontNest(result, -2521, -2603);
    _putDontNest(result, -2483, -2637);
    _putDontNest(result, -2543, -2617);
    _putDontNest(result, -2579, -2617);
    _putDontNest(result, -2608, -2608);
    _putDontNest(result, -2862, -2861);
    _putDontNest(result, -2618, -2642);
    _putDontNest(result, -2617, -2647);
    _putDontNest(result, -2603, -2593);
    _putDontNest(result, -2467, -2608);
    _putDontNest(result, -2472, -2593);
    _putDontNest(result, -2553, -2662);
    _putDontNest(result, -2549, -2583);
    _putDontNest(result, -2488, -2525);
    _putDontNest(result, -2483, -2514);
    _putDontNest(result, -2662, -2662);
    _putDontNest(result, -2598, -2598);
    _putDontNest(result, -2632, -2632);
    _putDontNest(result, -2563, -2617);
    _putDontNest(result, -2568, -2568);
    _putDontNest(result, -2574, -2622);
    _putDontNest(result, -2514, -2627);
    _putDontNest(result, -2483, -2608);
    _putDontNest(result, -2488, -2593);
    _putDontNest(result, -2554, -2667);
    _putDontNest(result, -2544, -2588);
    _putDontNest(result, -2508, -2553);
    _putDontNest(result, -2472, -2525);
    _putDontNest(result, -2467, -2514);
    _putDontNest(result, -2622, -2622);
    _putDontNest(result, -510, -708);
    _putDontNest(result, -2521, -2662);
    _putDontNest(result, -2518, -2578);
    _putDontNest(result, -2451, -2514);
    _putDontNest(result, -414, -419);
    _putDontNest(result, -2456, -2525);
    _putDontNest(result, -409, -424);
    _putDontNest(result, -2320, -2520);
    _putDontNest(result, -2564, -2652);
    _putDontNest(result, -2857, -2866);
    _putDontNest(result, -2569, -2647);
    _putDontNest(result, -2456, -2593);
    _putDontNest(result, -2539, -2632);
    _putDontNest(result, -2548, -2637);
    _putDontNest(result, -2451, -2608);
    _putDontNest(result, -2791, -2871);
    _putDontNest(result, -2772, -2876);
    _putDontNest(result, -2603, -2657);
    _putDontNest(result, -2573, -2627);
    _putDontNest(result, -2584, -2632);
    _putDontNest(result, -2559, -2652);
    _putDontNest(result, -2472, -2657);
    _putDontNest(result, -2553, -2598);
    _putDontNest(result, -2549, -2642);
    _putDontNest(result, -2807, -2871);
    _putDontNest(result, -2568, -2632);
    _putDontNest(result, -2598, -2662);
    _putDontNest(result, -2589, -2627);
    _putDontNest(result, -2514, -2563);
    _putDontNest(result, -2543, -2652);
    _putDontNest(result, -2554, -2603);
    _putDontNest(result, -2488, -2657);
    _putDontNest(result, -2579, -2588);
    _putDontNest(result, -2521, -2598);
    _putDontNest(result, -2518, -2647);
    _putDontNest(result, -2499, -2608);
    _putDontNest(result, -2563, -2588);
    _putDontNest(result, -2623, -2637);
    _putDontNest(result, -2628, -2652);
    _putDontNest(result, -2539, -2568);
    _putDontNest(result, -2511, -2652);
    _putDontNest(result, -2456, -2657);
    _putDontNest(result, -2515, -2608);
    _putDontNest(result, -2548, -2573);
    _putDontNest(result, -2544, -2617);
    _putDontNest(result, -2499, -2514);
    _putDontNest(result, -2598, -2603);
    _putDontNest(result, -2618, -2647);
    _putDontNest(result, -2589, -2622);
    _putDontNest(result, -2617, -2642);
    _putDontNest(result, -2604, -2593);
    _putDontNest(result, -2662, -2667);
    _putDontNest(result, -2488, -2642);
    _putDontNest(result, -2539, -2667);
    _putDontNest(result, -2499, -2627);
    _putDontNest(result, -2549, -2578);
    _putDontNest(result, -2559, -2588);
    _putDontNest(result, -2511, -2517);
    _putDontNest(result, -2518, -2548);
    _putDontNest(result, -2573, -2622);
    _putDontNest(result, -2871, -2871);
    _putDontNest(result, -2564, -2617);
    _putDontNest(result, -2515, -2627);
    _putDontNest(result, -2472, -2642);
    _putDontNest(result, -2549, -2657);
    _putDontNest(result, -2451, -2563);
    _putDontNest(result, -2543, -2588);
    _putDontNest(result, -2579, -2652);
    _putDontNest(result, -2467, -2563);
    _putDontNest(result, -2554, -2632);
    _putDontNest(result, -2456, -2642);
    _putDontNest(result, -2518, -2583);
    _putDontNest(result, -410, -424);
    _putDontNest(result, -2508, -2538);
    _putDontNest(result, -2563, -2652);
    _putDontNest(result, -2569, -2642);
    _putDontNest(result, -2652, -2657);
    _putDontNest(result, -2588, -2593);
    _putDontNest(result, -2508, -2662);
    _putDontNest(result, -2483, -2563);
    _putDontNest(result, -2511, -2588);
    _putDontNest(result, -2521, -2543);
    _putDontNest(result, -2539, -2553);
    _putDontNest(result, -2769, -2876);
    _putDontNest(result, -2598, -2667);
    _putDontNest(result, -2574, -2627);
    _putDontNest(result, -2604, -2657);
    _putDontNest(result, -2583, -2632);
    _putDontNest(result, -2499, -2563);
    _putDontNest(result, -2539, -2603);
    _putDontNest(result, -2558, -2652);
    _putDontNest(result, -2456, -2558);
    _putDontNest(result, -2320, -2517);
    _putDontNest(result, -2549, -2593);
    _putDontNest(result, -2515, -2563);
    _putDontNest(result, -2451, -2627);
    _putDontNest(result, -2320, -2501);
    _putDontNest(result, -513, -510);
    _putDontNest(result, -2578, -2588);
    _putDontNest(result, -2857, -2881);
    _putDontNest(result, -2608, -2637);
    _putDontNest(result, -2554, -2568);
    _putDontNest(result, -2548, -2622);
    _putDontNest(result, -2526, -2652);
    _putDontNest(result, -2467, -2627);
    _putDontNest(result, -2514, -2520);
    _putDontNest(result, -2488, -2558);
    _putDontNest(result, -404, -409);
    _putDontNest(result, -2627, -2652);
    _putDontNest(result, -2599, -2632);
    _putDontNest(result, -2588, -2657);
    _putDontNest(result, -2622, -2627);
    _putDontNest(result, -2483, -2627);
    _putDontNest(result, -2514, -2608);
    _putDontNest(result, -2508, -2598);
    _putDontNest(result, -2320, -2588);
    _putDontNest(result, -2472, -2558);
    _putDontNest(result, -2573, -2573);
    _putDontNest(result, -2637, -2637);
    _putDontNest(result, -2854, -2871);
    _putDontNest(result, -521, -521);
    _putDontNest(result, -2599, -2603);
    _putDontNest(result, -2488, -2647);
    _putDontNest(result, -2521, -2632);
    _putDontNest(result, -2451, -2622);
    _putDontNest(result, -2558, -2588);
    _putDontNest(result, -2483, -2520);
    _putDontNest(result, -2604, -2642);
    _putDontNest(result, -2603, -2647);
    _putDontNest(result, -2594, -2652);
    _putDontNest(result, -513, -513);
    _putDontNest(result, -2472, -2647);
    _putDontNest(result, -2539, -2662);
    _putDontNest(result, -2514, -2637);
    _putDontNest(result, -2467, -2520);
    _putDontNest(result, -2574, -2608);
    _putDontNest(result, -2578, -2652);
    _putDontNest(result, -2584, -2598);
    _putDontNest(result, -2569, -2593);
    _putDontNest(result, -2588, -2642);
    _putDontNest(result, -2648, -2662);
    _putDontNest(result, -2320, -2608);
    _putDontNest(result, -2553, -2632);
    _putDontNest(result, -2456, -2647);
    _putDontNest(result, -2483, -2622);
    _putDontNest(result, -2508, -2667);
    _putDontNest(result, -2526, -2588);
    _putDontNest(result, -2508, -2543);
    _putDontNest(result, -2451, -2520);
    _putDontNest(result, -2549, -2558);
    _putDontNest(result, -2647, -2667);
    _putDontNest(result, -2593, -2617);
    _putDontNest(result, -2583, -2603);
    _putDontNest(result, -2608, -2622);
    _putDontNest(result, -2632, -2662);
    _putDontNest(result, -2568, -2598);
    _putDontNest(result, -624, -709);
    _putDontNest(result, -2548, -2627);
    _putDontNest(result, -2518, -2657);
    _putDontNest(result, -2467, -2622);
    _putDontNest(result, -2599, -2667);
    _putDontNest(result, -2573, -2637);
    _putDontNest(result, -2521, -2568);
    _putDontNest(result, -2515, -2622);
    _putDontNest(result, -2526, -2617);
    _putDontNest(result, -2488, -2578);
    _putDontNest(result, -2844, -2881);
    _putDontNest(result, -2617, -2657);
    _putDontNest(result, -2589, -2637);
    _putDontNest(result, -2539, -2598);
    _putDontNest(result, -2499, -2622);
    _putDontNest(result, -2514, -2573);
    _putDontNest(result, -2472, -2578);
    _putDontNest(result, -2588, -2583);
    _putDontNest(result, -2642, -2652);
    _putDontNest(result, -2569, -2657);
    _putDontNest(result, -2584, -2662);
    _putDontNest(result, -2508, -2603);
    _putDontNest(result, -2558, -2617);
    _putDontNest(result, -510, -513);
    _putDontNest(result, -2553, -2568);
    _putDontNest(result, -2456, -2578);
    _putDontNest(result, -2518, -2525);
    _putDontNest(result, -2786, -2876);
    _putDontNest(result, -2568, -2662);
    _putDontNest(result, -2876, -2881);
    _putDontNest(result, -2598, -2632);
    _putDontNest(result, -2623, -2627);
    _putDontNest(result, -2583, -2667);
    _putDontNest(result, -2548, -2563);
    _putDontNest(result, -2518, -2593);
    _putDontNest(result, -2499, -2520);
    _putDontNest(result, -2844, -2866);
    _putDontNest(result, -2609, -2652);
    _putDontNest(result, -2578, -2617);
    _putDontNest(result, -2320, -2573);
    _putDontNest(result, -2456, -2568);
    _putDontNest(result, -2539, -2657);
    _putDontNest(result, -2511, -2637);
    _putDontNest(result, -2451, -2617);
    _putDontNest(result, -2518, -2558);
    _putDontNest(result, -2521, -2553);
    _putDontNest(result, -705, -708);
    _putDontNest(result, -2604, -2647);
    _putDontNest(result, -2593, -2652);
    _putDontNest(result, -2599, -2598);
    _putDontNest(result, -2603, -2642);
    _putDontNest(result, -2549, -2667);
    _putDontNest(result, -510, -510);
    _putDontNest(result, -414, -414);
    _putDontNest(result, -2568, -2603);
    _putDontNest(result, -2583, -2598);
    _putDontNest(result, -2866, -2876);
    _putDontNest(result, -2623, -2622);
    _putDontNest(result, -2647, -2662);
    _putDontNest(result, -2573, -2608);
    _putDontNest(result, -2632, -2667);
    _putDontNest(result, -2588, -2647);
    _putDontNest(result, -2543, -2637);
    _putDontNest(result, -2518, -2662);
    _putDontNest(result, -2483, -2617);
    _putDontNest(result, -2488, -2568);
    _putDontNest(result, -2553, -2553);
    _putDontNest(result, -159, -164);
    _putDontNest(result, -2589, -2608);
    _putDontNest(result, -2584, -2603);
    _putDontNest(result, -2648, -2667);
    _putDontNest(result, -2594, -2617);
    _putDontNest(result, -2472, -2568);
    _putDontNest(result, -2559, -2637);
    _putDontNest(result, -2467, -2617);
    _putDontNest(result, -2574, -2637);
    _putDontNest(result, -2320, -2637);
    _putDontNest(result, -2526, -2622);
    _putDontNest(result, -2544, -2608);
    _putDontNest(result, -2539, -2593);
    _putDontNest(result, -2456, -2632);
    _putDontNest(result, -2553, -2647);
    _putDontNest(result, -2511, -2573);
    _putDontNest(result, -2554, -2642);
    _putDontNest(result, -2515, -2617);
    _putDontNest(result, -2548, -2652);
    _putDontNest(result, -2488, -2583);
    _putDontNest(result, -2456, -2548);
    _putDontNest(result, -2599, -2662);
    _putDontNest(result, -2657, -2652);
    _putDontNest(result, -2618, -2657);
    _putDontNest(result, -2499, -2617);
    _putDontNest(result, -2549, -2603);
    _putDontNest(result, -2472, -2583);
    _putDontNest(result, -2807, -2876);
    _putDontNest(result, -2584, -2588);
    _putDontNest(result, -2568, -2667);
    _putDontNest(result, -2583, -2662);
    _putDontNest(result, -2608, -2627);
    _putDontNest(result, -2558, -2622);
    _putDontNest(result, -510, -518);
    _putDontNest(result, -2488, -2632);
    _putDontNest(result, -2521, -2647);
    _putDontNest(result, -2543, -2573);
    _putDontNest(result, -2518, -2598);
    _putDontNest(result, -2456, -2583);
    _putDontNest(result, -2488, -2548);
    _putDontNest(result, -2568, -2588);
    _putDontNest(result, -2772, -2871);
    _putDontNest(result, -2791, -2876);
    _putDontNest(result, -2573, -2583);
    _putDontNest(result, -2584, -2667);
    _putDontNest(result, -2622, -2637);
    _putDontNest(result, -2472, -2632);
    _putDontNest(result, -2559, -2573);
    _putDontNest(result, -2472, -2548);
    _putDontNest(result, -2593, -2603);
    _putDontNest(result, -2657, -2667);
    _putDontNest(result, -2622, -2642);
    _putDontNest(result, -2604, -2608);
    _putDontNest(result, -2583, -2617);
    _putDontNest(result, -606, -709);
    _putDontNest(result, -2549, -2662);
    _putDontNest(result, -2483, -2652);
    _putDontNest(result, -2553, -2583);
    _putDontNest(result, -2554, -2578);
    _putDontNest(result, -2548, -2588);
    _putDontNest(result, -2515, -2558);
    _putDontNest(result, -2508, -2517);
    _putDontNest(result, -2628, -2632);
    _putDontNest(result, -2594, -2598);
    _putDontNest(result, -577, -708);
    _putDontNest(result, -2320, -2622);
    _putDontNest(result, -2526, -2627);
    _putDontNest(result, -2467, -2652);
    _putDontNest(result, -2499, -2558);
    _putDontNest(result, -2618, -2622);
    _putDontNest(result, -2578, -2598);
    _putDontNest(result, -2642, -2662);
    _putDontNest(result, -2584, -2652);
    _putDontNest(result, -2871, -2876);
    _putDontNest(result, -2589, -2647);
    _putDontNest(result, -2508, -2657);
    _putDontNest(result, -2451, -2652);
    _putDontNest(result, -2544, -2637);
    _putDontNest(result, -2521, -2583);
    _putDontNest(result, -410, -419);
    _putDontNest(result, -2511, -2538);
    _putDontNest(result, -2574, -2642);
    _putDontNest(result, -2568, -2652);
    _putDontNest(result, -2573, -2647);
    _putDontNest(result, -2588, -2608);
    _putDontNest(result, -2599, -2617);
    _putDontNest(result, -2558, -2627);
    _putDontNest(result, -2518, -2667);
    _putDontNest(result, -2526, -2543);
    _putDontNest(result, -2569, -2627);
    _putDontNest(result, -2593, -2667);
    _putDontNest(result, -2553, -2642);
    _putDontNest(result, -2543, -2608);
    _putDontNest(result, -2549, -2598);
    _putDontNest(result, -2554, -2647);
    _putDontNest(result, -2451, -2558);
    _putDontNest(result, -2320, -2508);
    _putDontNest(result, -2594, -2662);
    _putDontNest(result, -2623, -2657);
    _putDontNest(result, -2564, -2632);
    _putDontNest(result, -2609, -2667);
    _putDontNest(result, -513, -708);
    _putDontNest(result, -2559, -2608);
    _putDontNest(result, -2526, -2563);
    _putDontNest(result, -2583, -2588);
    _putDontNest(result, -2578, -2662);
    _putDontNest(result, -2603, -2637);
    _putDontNest(result, -2862, -2881);
    _putDontNest(result, -2648, -2652);
    _putDontNest(result, -2521, -2642);
    _putDontNest(result, -2508, -2593);
    _putDontNest(result, -2511, -2608);
    _putDontNest(result, -2548, -2617);
    _putDontNest(result, -2515, -2652);
    _putDontNest(result, -2544, -2573);
    _putDontNest(result, -2483, -2558);
    _putDontNest(result, -2573, -2578);
    _putDontNest(result, -2574, -2583);
    _putDontNest(result, -2637, -2647);
    _putDontNest(result, -2617, -2627);
    _putDontNest(result, -2632, -2652);
    _putDontNest(result, -2518, -2603);
    _putDontNest(result, -2499, -2652);
    _putDontNest(result, -2558, -2563);
    _putDontNest(result, -2467, -2558);
    _putDontNest(result, -2857, -2861);
    _putDontNest(result, -2603, -2608);
    _putDontNest(result, -2584, -2617);
    _putDontNest(result, -2608, -2593);
    _putDontNest(result, -2622, -2647);
    _putDontNest(result, -2658, -2667);
    _putDontNest(result, -2594, -2603);
    _putDontNest(result, -2320, -2563);
    _putDontNest(result, -2511, -2627);
    _putDontNest(result, -2488, -2598);
    _putDontNest(result, -2518, -2632);
    _putDontNest(result, -2553, -2578);
    _putDontNest(result, -2554, -2583);
    _putDontNest(result, -2514, -2548);
    _putDontNest(result, -2488, -2514);
    _putDontNest(result, -2483, -2525);
    _putDontNest(result, -2593, -2598);
    _putDontNest(result, -2568, -2617);
    _putDontNest(result, -2657, -2662);
    _putDontNest(result, -2569, -2622);
    _putDontNest(result, -2627, -2632);
    _putDontNest(result, -2563, -2568);
    _putDontNest(result, -2599, -2652);
    _putDontNest(result, -521, -518);
    _putDontNest(result, -2472, -2598);
    _putDontNest(result, -2553, -2657);
    _putDontNest(result, -2467, -2525);
    _putDontNest(result, -2472, -2514);
    _putDontNest(result, -2511, -2553);
    _putDontNest(result, -2617, -2622);
    _putDontNest(result, -2583, -2652);
    _putDontNest(result, -2589, -2642);
    _putDontNest(result, -510, -709);
    _putDontNest(result, -2456, -2598);
    _putDontNest(result, -2543, -2627);
    _putDontNest(result, -2515, -2588);
    _putDontNest(result, -2521, -2578);
    _putDontNest(result, -2451, -2525);
    _putDontNest(result, -2456, -2514);
    _putDontNest(result, -409, -419);
    _putDontNest(result, -414, -424);
    _putDontNest(result, -2548, -2558);
    _putDontNest(result, -2862, -2866);
    _putDontNest(result, -2574, -2647);
    _putDontNest(result, -2573, -2642);
    _putDontNest(result, -2578, -2603);
    _putDontNest(result, -2642, -2667);
    _putDontNest(result, -2521, -2657);
    _putDontNest(result, -2559, -2627);
    _putDontNest(result, -2499, -2588);
    _putDontNest(result, -2543, -2553);
    _putDontNest(result, -2786, -2871);
    _putDontNest(result, -2608, -2657);
    _putDontNest(result, -2609, -2662);
    _putDontNest(result, -2594, -2667);
    _putDontNest(result, -2579, -2632);
    _putDontNest(result, -2320, -2627);
    _putDontNest(result, -2518, -2568);
    _putDontNest(result, -2511, -2563);
    _putDontNest(result, -2488, -2662);
    _putDontNest(result, -2483, -2588);
    _putDontNest(result, -2588, -2637);
    _putDontNest(result, -2563, -2632);
    _putDontNest(result, -2593, -2662);
    _putDontNest(result, -2539, -2647);
    _putDontNest(result, -2472, -2662);
    _putDontNest(result, -2553, -2593);
    _putDontNest(result, -2558, -2608);
    _putDontNest(result, -2467, -2588);
    _putDontNest(result, -2320, -2553);
    _putDontNest(result, -2861, -2881);
    _putDontNest(result, -2604, -2637);
    _putDontNest(result, -2647, -2652);
    _putDontNest(result, -2456, -2662);
    _putDontNest(result, -2514, -2652);
    _putDontNest(result, -2543, -2563);
    _putDontNest(result, -2451, -2588);
    _putDontNest(result, -2515, -2525);
    _putDontNest(result, -2578, -2667);
    _putDontNest(result, -2618, -2627);
    _putDontNest(result, -2637, -2642);
    _putDontNest(result, -2508, -2642);
    _putDontNest(result, -2526, -2608);
    _putDontNest(result, -2544, -2622);
    _putDontNest(result, -2521, -2593);
    _putDontNest(result, -2499, -2525);
    _putDontNest(result, -2623, -2647);
    _putDontNest(result, -2588, -2622);
    _putDontNest(result, -2472, -2603);
    _putDontNest(result, -2515, -2548);
    _putDontNest(result, -2526, -2553);
    _putDontNest(result, -2608, -2642);
    _putDontNest(result, -2866, -2871);
    _putDontNest(result, -2598, -2652);
    _putDontNest(result, -2526, -2637);
    _putDontNest(result, -2554, -2657);
    _putDontNest(result, -2488, -2603);
    _putDontNest(result, -2539, -2583);
    _putDontNest(result, -409, -414);
    _putDontNest(result, -2499, -2548);
    _putDontNest(result, -2637, -2657);
    _putDontNest(result, -2573, -2593);
    _putDontNest(result, -2627, -2667);
    _putDontNest(result, -2563, -2603);
    _putDontNest(result, -2544, -2627);
    _putDontNest(result, -2549, -2632);
    _putDontNest(result, -2514, -2588);
    _putDontNest(result, -2579, -2603);
    _putDontNest(result, -2844, -2861);
    _putDontNest(result, -2604, -2622);
    _putDontNest(result, -2589, -2593);
    _putDontNest(result, -2564, -2598);
    _putDontNest(result, -2653, -2657);
    _putDontNest(result, -2628, -2662);
    _putDontNest(result, -2456, -2603);
    _putDontNest(result, -2558, -2637);
    _putDontNest(result, -2508, -2578);
    _putDontNest(result, -2578, -2632);
    _putDontNest(result, -2569, -2637);
    _putDontNest(result, -2514, -2617);
    _putDontNest(result, -2472, -2667);
    _putDontNest(result, -2451, -2548);
    _putDontNest(result, -2526, -2573);
    _putDontNest(result, -2554, -2593);
    _putDontNest(result, -2539, -2642);
    _putDontNest(result, -2511, -2622);
    _putDontNest(result, -2488, -2667);
    _putDontNest(result, -60, -116);
    _putDontNest(result, -2573, -2657);
    _putDontNest(result, -2603, -2627);
    _putDontNest(result, -2563, -2667);
    _putDontNest(result, -2544, -2563);
    _putDontNest(result, -2549, -2568);
    _putDontNest(result, -2559, -2622);
    _putDontNest(result, -2514, -2525);
    _putDontNest(result, -2483, -2548);
    _putDontNest(result, -2320, -2538);
    _putDontNest(result, -2769, -2871);
    _putDontNest(result, -2579, -2667);
    _putDontNest(result, -2594, -2632);
    _putDontNest(result, -2617, -2637);
    _putDontNest(result, -2589, -2657);
    _putDontNest(result, -2564, -2662);
    _putDontNest(result, -2543, -2622);
    _putDontNest(result, -2456, -2667);
    _putDontNest(result, -2558, -2573);
    _putDontNest(result, -2508, -2647);
    _putDontNest(result, -2467, -2548);
    _putDontNest(result, -2623, -2642);
    _putDontNest(result, -601, -709);
    _putDontNest(result, -2488, -2652);
    _putDontNest(result, -2543, -2657);
    _putDontNest(result, -2488, -2520);
    _putDontNest(result, -2514, -2558);
    _putDontNest(result, -2543, -2543);
    _putDontNest(result, -2608, -2647);
    _putDontNest(result, -518, -513);
    _putDontNest(result, -2472, -2652);
    _putDontNest(result, -2521, -2627);
    _putDontNest(result, -2559, -2657);
    _putDontNest(result, -2539, -2578);
    _putDontNest(result, -2472, -2520);
    _putDontNest(result, -2564, -2603);
    _putDontNest(result, -2628, -2667);
    _putDontNest(result, -2569, -2608);
    _putDontNest(result, -2574, -2593);
    _putDontNest(result, -2579, -2598);
    _putDontNest(result, -2320, -2593);
    _putDontNest(result, -2511, -2657);
    _putDontNest(result, -2539, -2637);
    _putDontNest(result, -2548, -2632);
    _putDontNest(result, -2514, -2662);
    _putDontNest(result, -2456, -2652);
    _putDontNest(result, -2511, -2543);
    _putDontNest(result, -155, -164);
    _putDontNest(result, -2456, -2520);
    _putDontNest(result, -2548, -2548);
    _putDontNest(result, -2786, -2881);
    _putDontNest(result, -2598, -2617);
    _putDontNest(result, -2854, -2876);
    _putDontNest(result, -2627, -2662);
    _putDontNest(result, -2603, -2622);
    _putDontNest(result, -2563, -2598);
    _putDontNest(result, -2553, -2627);
    _putDontNest(result, -2508, -2583);
    _putDontNest(result, -2558, -2642);
    _putDontNest(result, -2543, -2593);
    _putDontNest(result, -2320, -2511);
    _putDontNest(result, -2769, -2866);
    _putDontNest(result, -2622, -2657);
    _putDontNest(result, -2588, -2627);
    _putDontNest(result, -521, -709);
    _putDontNest(result, -518, -708);
    _putDontNest(result, -2521, -2563);
    _putDontNest(result, -2559, -2593);
    _putDontNest(result, -2563, -2558);
    _putDontNest(result, -2604, -2627);
    _putDontNest(result, -2574, -2657);
    _putDontNest(result, -2564, -2667);
    _putDontNest(result, -2609, -2632);
    _putDontNest(result, -2579, -2662);
    _putDontNest(result, -2320, -2657);
    _putDontNest(result, -2514, -2598);
    _putDontNest(result, -2526, -2642);
    _putDontNest(result, -2508, -2608);
    _putDontNest(result, -2554, -2622);
    _putDontNest(result, -2539, -2573);
    _putDontNest(result, -2548, -2568);
    _putDontNest(result, -2511, -2593);
    _putDontNest(result, -2320, -2543);
    _putDontNest(result, -2588, -2588);
    _putDontNest(result, -2578, -2578);
    _putDontNest(result, -2618, -2637);
    _putDontNest(result, -2593, -2632);
    _putDontNest(result, -2563, -2662);
    _putDontNest(result, -2553, -2563);
    _putDontNest(result, -2451, -2568);
    _putDontNest(result, -2544, -2657);
    _putDontNest(result, -2456, -2617);
    _putDontNest(result, -2515, -2632);
    _putDontNest(result, -2508, -2637);
    _putDontNest(result, -2558, -2578);
    _putDontNest(result, -2854, -2861);
    _putDontNest(result, -2609, -2647);
    _putDontNest(result, -2499, -2632);
    _putDontNest(result, -2511, -2558);
    _putDontNest(result, -2769, -2881);
    _putDontNest(result, -2604, -2652);
    _putDontNest(result, -2647, -2637);
    _putDontNest(result, -2593, -2647);
    _putDontNest(result, -518, -518);
    _putDontNest(result, -2594, -2642);
    _putDontNest(result, -505, -709);
    _putDontNest(result, -2483, -2568);
    _putDontNest(result, -2488, -2617);
    _putDontNest(result, -2526, -2578);
    _putDontNest(result, -2548, -2553);
    _putDontNest(result, -2499, -2538);
    _putDontNest(result, -2573, -2603);
    _putDontNest(result, -2588, -2652);
    _putDontNest(result, -2627, -2657);
    _putDontNest(result, -2563, -2593);
    _putDontNest(result, -2568, -2608);
    _putDontNest(result, -2637, -2667);
    _putDontNest(result, -2578, -2642);
    _putDontNest(result, -2320, -2642);
    _putDontNest(result, -2467, -2568);
    _putDontNest(result, -2472, -2617);
    _putDontNest(result, -2554, -2627);
    _putDontNest(result, -2514, -2667);
    _putDontNest(result, -2514, -2543);
    _putDontNest(result, -2515, -2538);
    _putDontNest(result, -2543, -2558);
    _putDontNest(result, -2653, -2667);
    _putDontNest(result, -2574, -2598);
    _putDontNest(result, -2579, -2593);
    _putDontNest(result, -2598, -2622);
    _putDontNest(result, -2584, -2608);
    _putDontNest(result, -2603, -2617);
    _putDontNest(result, -2589, -2603);
    _putDontNest(result, -2508, -2573);
    _putDontNest(result, -2515, -2568);
    _putDontNest(result, -2544, -2593);
    _putDontNest(result, -2521, -2622);
    _putDontNest(result, -2539, -2608);
    _putDontNest(result, -2451, -2632);
    _putDontNest(result, -2558, -2647);
    _putDontNest(result, -2488, -2588);
    _putDontNest(result, -2467, -2538);
    _putDontNest(result, -2456, -2501);
    _putDontNest(result, -2622, -2662);
    _putDontNest(result, -2499, -2568);
    _putDontNest(result, -2472, -2588);
    _putDontNest(result, -2456, -2517);
    _putDontNest(result, -2483, -2538);
    _putDontNest(result, -2583, -2637);
    _putDontNest(result, -2553, -2622);
    _putDontNest(result, -2526, -2647);
    _putDontNest(result, -2483, -2632);
    _putDontNest(result, -2456, -2588);
    _putDontNest(result, -2472, -2517);
    _putDontNest(result, -2488, -2501);
    _putDontNest(result, -2578, -2583);
    _putDontNest(result, -2573, -2667);
    _putDontNest(result, -2652, -2652);
    _putDontNest(result, -2563, -2657);
    _putDontNest(result, -2642, -2642);
    _putDontNest(result, -2599, -2637);
    _putDontNest(result, -2514, -2603);
    _putDontNest(result, -2467, -2632);
    _putDontNest(result, -2451, -2538);
    _putDontNest(result, -2488, -2517);
    _putDontNest(result, -2472, -2501);
    _putDontNest(result, -2786, -2866);
    _putDontNest(result, -2589, -2667);
    _putDontNest(result, -2574, -2662);
    _putDontNest(result, -2866, -2881);
    _putDontNest(result, -2579, -2657);
    _putDontNest(result, -2608, -2632);
    _putDontNest(result, -2514, -2632);
    _putDontNest(result, -2548, -2662);
    _putDontNest(result, -2467, -2603);
    _putDontNest(result, -2558, -2583);
    _putDontNest(result, -2515, -2553);
    _putDontNest(result, -2526, -2548);
    _putDontNest(result, -2844, -2876);
    _putDontNest(result, -2609, -2642);
    _putDontNest(result, -2568, -2573);
    _putDontNest(result, -2881, -2881);
    _putDontNest(result, -2632, -2637);
    _putDontNest(result, -2588, -2617);
    _putDontNest(result, -2599, -2608);
    _putDontNest(result, -2521, -2637);
    _putDontNest(result, -2483, -2603);
    _putDontNest(result, -2499, -2553);
    _putDontNest(result, -2772, -2881);
    _putDontNest(result, -2593, -2642);
    _putDontNest(result, -2594, -2647);
    _putDontNest(result, -2603, -2652);
    _putDontNest(result, -2499, -2667);
    _putDontNest(result, -2539, -2627);
    _putDontNest(result, -2526, -2583);
    _putDontNest(result, -405, -419);
    _putDontNest(result, -2653, -2662);
    _putDontNest(result, -2876, -2876);
    _putDontNest(result, -2628, -2657);
    _putDontNest(result, -2574, -2603);
    _putDontNest(result, -2578, -2647);
    _putDontNest(result, -2589, -2598);
    _putDontNest(result, -2564, -2593);
    _putDontNest(result, -2320, -2647);
    _putDontNest(result, -2515, -2667);
    _putDontNest(result, -2553, -2637);
    _putDontNest(result, -2451, -2603);
    _putDontNest(result, -2544, -2558);
    _putDontNest(result, -2320, -2525);
    _putDontNest(result, -2637, -2662);
    _putDontNest(result, -2604, -2617);
    _putDontNest(result, -2583, -2608);
    _putDontNest(result, -2573, -2598);
    _putDontNest(result, -2548, -2598);
    _putDontNest(result, -2559, -2647);
    _putDontNest(result, -2467, -2667);
    _putDontNest(result, -2514, -2568);
    _putDontNest(result, -2451, -2553);
    _putDontNest(result, -2568, -2637);
    _putDontNest(result, -2483, -2667);
    _putDontNest(result, -2521, -2573);
    _putDontNest(result, -2554, -2608);
    _putDontNest(result, -2544, -2642);
    _putDontNest(result, -2543, -2647);
    _putDontNest(result, -2508, -2622);
    _putDontNest(result, -2584, -2637);
    _putDontNest(result, -2622, -2667);
    _putDontNest(result, -2539, -2563);
    _putDontNest(result, -2499, -2603);
    _putDontNest(result, -2518, -2652);
    _putDontNest(result, -2549, -2617);
    _putDontNest(result, -2483, -2553);
    _putDontNest(result, -521, -510);
    _putDontNest(result, -2579, -2583);
    _putDontNest(result, -2623, -2632);
    _putDontNest(result, -2564, -2657);
    _putDontNest(result, -2574, -2667);
    _putDontNest(result, -2642, -2647);
    _putDontNest(result, -2589, -2662);
    _putDontNest(result, -2598, -2627);
    _putDontNest(result, -2515, -2603);
    _putDontNest(result, -2451, -2667);
    _putDontNest(result, -2553, -2573);
    _putDontNest(result, -2511, -2647);
    _putDontNest(result, -2511, -2525);
    _putDontNest(result, -2467, -2553);
    _putDontNest(result, -2564, -2578);
    _putDontNest(result, -2563, -2583);
    _putDontNest(result, -2573, -2662);
    _putDontNest(result, -2508, -2627);
    _putDontNest(result, -2483, -2598);
    _putDontNest(result, -2559, -2583);
    _putDontNest(result, -2514, -2553);
    _putDontNest(result, -2593, -2593);
    _putDontNest(result, -513, -521);
    _putDontNest(result, -2598, -2608);
    _putDontNest(result, -2618, -2652);
    _putDontNest(result, -2657, -2657);
    _putDontNest(result, -2584, -2622);
    _putDontNest(result, -2548, -2667);
    _putDontNest(result, -2558, -2657);
    _putDontNest(result, -2467, -2598);
    _putDontNest(result, -2543, -2583);
    _putDontNest(result, -2544, -2578);
    _putDontNest(result, -2511, -2548);
    _putDontNest(result, -405, -414);
    _putDontNest(result, -2569, -2617);
    _putDontNest(result, -2568, -2622);
    _putDontNest(result, -521, -513);
    _putDontNest(result, -2608, -2598);
    _putDontNest(result, -2320, -2568);
    _putDontNest(result, -2515, -2662);
    _putDontNest(result, -2451, -2598);
    _putDontNest(result, -2518, -2588);
    _putDontNest(result, -2579, -2647);
    _putDontNest(result, -2617, -2617);
    _putDontNest(result, -2499, -2662);
    _putDontNest(result, -2554, -2637);
    _putDontNest(result, -2526, -2657);
    _putDontNest(result, -2511, -2583);
    _putDontNest(result, -2543, -2548);
    _putDontNest(result, -2563, -2647);
    _putDontNest(result, -2564, -2642);
    _putDontNest(result, -2483, -2662);
    _putDontNest(result, -2549, -2652);
    _putDontNest(result, -2508, -2563);
    _putDontNest(result, -2559, -2642);
    _putDontNest(result, -2518, -2617);
    _putDontNest(result, -2791, -2861);
    _putDontNest(result, -2772, -2866);
    _putDontNest(result, -2593, -2657);
    _putDontNest(result, -2467, -2662);
    _putDontNest(result, -2558, -2593);
    _putDontNest(result, -2548, -2603);
    _putDontNest(result, -2543, -2642);
    _putDontNest(result, -2553, -2608);
    _putDontNest(result, -2544, -2647);
    _putDontNest(result, -2320, -2558);
    _putDontNest(result, -2807, -2861);
    _putDontNest(result, -2623, -2667);
    _putDontNest(result, -2609, -2657);
    _putDontNest(result, -2608, -2662);
    _putDontNest(result, -2574, -2632);
    _putDontNest(result, -2583, -2627);
    _putDontNest(result, -2320, -2632);
    _putDontNest(result, -2451, -2662);
    _putDontNest(result, -2515, -2598);
    _putDontNest(result, -2599, -2627);
    _putDontNest(result, -2622, -2632);
    _putDontNest(result, -2526, -2593);
    _putDontNest(result, -2511, -2642);
    _putDontNest(result, -510, -521);
    _putDontNest(result, -2499, -2598);
    _putDontNest(result, -2521, -2608);
    _putDontNest(result, -2554, -2573);
    _putDontNest(result, -2539, -2622);
    _putDontNest(result, -2564, -2583);
    _putDontNest(result, -2569, -2588);
    _putDontNest(result, -2563, -2578);
    _putDontNest(result, -2627, -2647);
    _putDontNest(result, -2628, -2642);
    _putDontNest(result, -2549, -2588);
    _putDontNest(result, -2559, -2578);
    _putDontNest(result, -2499, -2511);
    _putDontNest(result, -2526, -2558);
    _putDontNest(result, -2594, -2593);
    _putDontNest(result, -2617, -2652);
    _putDontNest(result, -2583, -2622);
    _putDontNest(result, -2861, -2871);
    _putDontNest(result, -2608, -2603);
    _putDontNest(result, -2472, -2608);
    _putDontNest(result, -2467, -2593);
    _putDontNest(result, -2558, -2662);
    _putDontNest(result, -2543, -2578);
    _putDontNest(result, -2544, -2583);
    _putDontNest(result, -2483, -2593);
    _putDontNest(result, -2488, -2608);
    _putDontNest(result, -404, -424);
    _putDontNest(result, -2558, -2558);
    _putDontNest(result, -2499, -2543);
    _putDontNest(result, -2579, -2642);
    _putDontNest(result, -2499, -2657);
    _putDontNest(result, -2526, -2662);
    _putDontNest(result, -2511, -2578);
    _putDontNest(result, -2514, -2538);
    _putDontNest(result, -2515, -2543);
    _putDontNest(result, -2569, -2652);
    _putDontNest(result, -2563, -2642);
    _putDontNest(result, -2642, -2657);
    _putDontNest(result, -2564, -2647);
    _putDontNest(result, -2578, -2593);
    _putDontNest(result, -2599, -2622);
    _putDontNest(result, -2451, -2593);
    _putDontNest(result, -2515, -2657);
    _putDontNest(result, -2456, -2608);
    _putDontNest(result, -2544, -2632);
    _putDontNest(result, -2549, -2627);
    _putDontNest(result, -2467, -2543);
    _putDontNest(result, -2786, -2861);
    _putDontNest(result, -2568, -2627);
    _putDontNest(result, -2608, -2667);
    _putDontNest(result, -2623, -2662);
    _putDontNest(result, -2589, -2632);
    _putDontNest(result, -2594, -2657);
    _putDontNest(result, -2518, -2622);
    _putDontNest(result, -2558, -2598);
    _putDontNest(result, -2467, -2657);
    _putDontNest(result, -2483, -2543);
    _putDontNest(result, -2451, -2511);
    _putDontNest(result, -60, -109);
    _putDontNest(result, -2456, -2508);
    _putDontNest(result, -2584, -2627);
    _putDontNest(result, -2573, -2632);
    _putDontNest(result, -2483, -2657);
    _putDontNest(result, -2472, -2508);
    _putDontNest(result, -2467, -2511);
    _putDontNest(result, -2598, -2637);
    _putDontNest(result, -2514, -2642);
    _putDontNest(result, -2499, -2593);
    _putDontNest(result, -2526, -2598);
    _putDontNest(result, -2320, -2578);
    _putDontNest(result, -2483, -2511);
    _putDontNest(result, -2488, -2508);
    _putDontNest(result, -2508, -2520);
    _putDontNest(result, -2451, -2543);
    _putDontNest(result, -2867, -2881);
    _putDontNest(result, -2628, -2647);
    _putDontNest(result, -2578, -2657);
    _putDontNest(result, -2627, -2642);
    _putDontNest(result, -2451, -2657);
    _putDontNest(result, -2515, -2593);
    _putDontNest(result, -2508, -2652);
    _putDontNest(result, -2544, -2568);
    _putDontNest(result, -2539, -2617);
    _putDontNest(result, -2549, -2563);
    _putDontNest(result, -2521, -2548);
    _putDontNest(result, -2578, -2622);
    _putDontNest(result, -2563, -2573);
    _putDontNest(result, -2627, -2637);
    _putDontNest(result, -2599, -2593);
    _putDontNest(result, -601, -708);
    _putDontNest(result, -2608, -2652);
    _putDontNest(result, -2876, -2871);
    _putDontNest(result, -2598, -2642);
    _putDontNest(result, -2518, -2627);
    _putDontNest(result, -2456, -2573);
    _putDontNest(result, -2558, -2667);
    _putDontNest(result, -2511, -2632);
    _putDontNest(result, -2548, -2657);
    _putDontNest(result, -405, -424);
    _putDontNest(result, -2553, -2548);
    _putDontNest(result, -2569, -2603);
    _putDontNest(result, -2564, -2608);
    _putDontNest(result, -2559, -2632);
    _putDontNest(result, -2472, -2573);
    _putDontNest(result, -2514, -2578);
    _putDontNest(result, -2539, -2558);
    _putDontNest(result, -2518, -2543);
    _putDontNest(result, -2844, -2871);
    _putDontNest(result, -2583, -2593);
    _putDontNest(result, -2647, -2657);
    _putDontNest(result, -2594, -2622);
    _putDontNest(result, -2320, -2598);
    _putDontNest(result, -2488, -2573);
    _putDontNest(result, -2543, -2632);
    _putDontNest(result, -2526, -2667);
    _putDontNest(result, -2508, -2588);
    _putDontNest(result, -60, -102);
    _putDontNest(result, -2456, -2553);
    _putDontNest(result, -2599, -2657);
    _putDontNest(result, -2588, -2632);
    _putDontNest(result, -2618, -2662);
    _putDontNest(result, -2563, -2637);
    _putDontNest(result, -2320, -2548);
    _putDontNest(result, -2579, -2637);
    _putDontNest(result, -2617, -2667);
    _putDontNest(result, -518, -709);
    _putDontNest(result, -521, -708);
    _putDontNest(result, -2456, -2637);
    _putDontNest(result, -2558, -2603);
    _putDontNest(result, -2548, -2593);
    _putDontNest(result, -2508, -2617);
    _putDontNest(result, -2518, -2563);
    _putDontNest(result, -2511, -2568);
    _putDontNest(result, -2539, -2652);
    _putDontNest(result, -2488, -2553);
    _putDontNest(result, -409, -409);
    _putDontNest(result, -2569, -2667);
    _putDontNest(result, -2854, -2881);
    _putDontNest(result, -2593, -2627);
    _putDontNest(result, -2472, -2637);
    _putDontNest(result, -2514, -2647);
    _putDontNest(result, -2559, -2568);
    _putDontNest(result, -2549, -2622);
    _putDontNest(result, -2320, -2583);
    _putDontNest(result, -2508, -2525);
    _putDontNest(result, -2472, -2553);
    _putDontNest(result, -2769, -2861);
    _putDontNest(result, -2583, -2657);
    _putDontNest(result, -2604, -2632);
    _putDontNest(result, -2609, -2627);
    _putDontNest(result, -2320, -2662);
    _putDontNest(result, -2488, -2637);
    _putDontNest(result, -2543, -2568);
    _putDontNest(result, -2526, -2603);
    _putDontNest(result, -2499, -2517);
    _putDontNest(result, -2564, -2573);
    _putDontNest(result, -2623, -2652);
    _putDontNest(result, -2628, -2637);
    _putDontNest(result, -2543, -2667);
    _putDontNest(result, -2526, -2632);
    _putDontNest(result, -2456, -2622);
    _putDontNest(result, -2483, -2647);
    _putDontNest(result, -2508, -2558);
    _putDontNest(result, -404, -414);
    _putDontNest(result, -2598, -2647);
    _putDontNest(result, -513, -518);
    _putDontNest(result, -2544, -2662);
    _putDontNest(result, -2559, -2667);
    _putDontNest(result, -2467, -2647);
    _putDontNest(result, -2539, -2588);
    _putDontNest(result, -2568, -2593);
    _putDontNest(result, -2563, -2608);
    _putDontNest(result, -2609, -2622);
    _putDontNest(result, -2632, -2657);
    _putDontNest(result, -505, -708);
    _putDontNest(result, -2320, -2603);
    _putDontNest(result, -2558, -2632);
    _putDontNest(result, -2488, -2622);
    _putDontNest(result, -2511, -2667);
    _putDontNest(result, -2451, -2647);
    _putDontNest(result, -2514, -2583);
    _putDontNest(result, -2648, -2657);
    _putDontNest(result, -2854, -2866);
    _putDontNest(result, -2593, -2622);
    _putDontNest(result, -2608, -2617);
    _putDontNest(result, -2579, -2608);
    _putDontNest(result, -2569, -2598);
    _putDontNest(result, -2584, -2593);
    _putDontNest(result, -624, -708);
    _putDontNest(result, -2472, -2622);
    _putDontNest(result, -2549, -2637);
    _putDontNest(result, -2472, -2538);
    _putDontNest(result, -2451, -2501);
    _putDontNest(result, -2564, -2637);
    _putDontNest(result, -2617, -2662);
    _putDontNest(result, -2521, -2617);
    _putDontNest(result, -2543, -2603);
    _putDontNest(result, -2548, -2642);
    _putDontNest(result, -2554, -2652);
    _putDontNest(result, -2526, -2568);
    _putDontNest(result, -2451, -2517);
    _putDontNest(result, -2488, -2538);
    _putDontNest(result, -2578, -2627);
    _putDontNest(result, -2618, -2667);
    _putDontNest(result, -2559, -2603);
    _putDontNest(result, -2544, -2598);
    _putDontNest(result, -2483, -2501);
    _putDontNest(result, -2467, -2517);
    _putDontNest(result, -159, -159);
    _putDontNest(result, -2807, -2866);
    _putDontNest(result, -2583, -2583);
    _putDontNest(result, -2568, -2657);
    _putDontNest(result, -2594, -2627);
    _putDontNest(result, -2320, -2667);
    _putDontNest(result, -2553, -2617);
    _putDontNest(result, -2515, -2647);
    _putDontNest(result, -2558, -2568);
    _putDontNest(result, -2511, -2603);
    _putDontNest(result, -2456, -2538);
    _putDontNest(result, -2467, -2501);
    _putDontNest(result, -2483, -2517);
    _putDontNest(result, -2791, -2866);
    _putDontNest(result, -2568, -2578);
    _putDontNest(result, -2772, -2861);
    _putDontNest(result, -2574, -2588);
    _putDontNest(result, -2603, -2632);
    _putDontNest(result, -2569, -2662);
    _putDontNest(result, -2584, -2657);
    _putDontNest(result, -2499, -2647);
    _putDontNest(result, -2518, -2608);
    _putDontNest(result, -2549, -2573);
      
    return result;
  }
    
  protected static IntegerMap _initDontNestGroups() {
    IntegerMap result = new IntegerMap();
    int resultStoreId = result.size();
    
    
    ++resultStoreId;
    
    result.putUnsafe(-2642, resultStoreId);
    result.putUnsafe(-2647, resultStoreId);
    result.putUnsafe(-2637, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-410, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-60, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2579, resultStoreId);
    result.putUnsafe(-2583, resultStoreId);
    result.putUnsafe(-2584, resultStoreId);
    result.putUnsafe(-2569, resultStoreId);
    result.putUnsafe(-2588, resultStoreId);
    result.putUnsafe(-2574, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2866, resultStoreId);
    result.putUnsafe(-2871, resultStoreId);
    result.putUnsafe(-2876, resultStoreId);
    result.putUnsafe(-2861, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2857, resultStoreId);
    result.putUnsafe(-2862, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2657, resultStoreId);
    result.putUnsafe(-2648, resultStoreId);
    result.putUnsafe(-2652, resultStoreId);
    result.putUnsafe(-2653, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-164, resultStoreId);
    result.putUnsafe(-155, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2320, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-404, resultStoreId);
    result.putUnsafe(-409, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2514, resultStoreId);
    result.putUnsafe(-2508, resultStoreId);
    result.putUnsafe(-2511, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-705, resultStoreId);
    result.putUnsafe(-577, resultStoreId);
    result.putUnsafe(-601, resultStoreId);
    result.putUnsafe(-505, resultStoreId);
    result.putUnsafe(-606, resultStoreId);
    result.putUnsafe(-624, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-405, resultStoreId);
    result.putUnsafe(-414, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2483, resultStoreId);
    result.putUnsafe(-2451, resultStoreId);
    result.putUnsafe(-2467, resultStoreId);
    result.putUnsafe(-2488, resultStoreId);
    result.putUnsafe(-2456, resultStoreId);
    result.putUnsafe(-2472, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2662, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2617, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2568, resultStoreId);
    result.putUnsafe(-2554, resultStoreId);
    result.putUnsafe(-2559, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2593, resultStoreId);
    result.putUnsafe(-2594, resultStoreId);
    result.putUnsafe(-2598, resultStoreId);
    result.putUnsafe(-2599, resultStoreId);
    result.putUnsafe(-2603, resultStoreId);
    result.putUnsafe(-2604, resultStoreId);
    result.putUnsafe(-2589, resultStoreId);
    result.putUnsafe(-2608, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2521, resultStoreId);
    result.putUnsafe(-2526, resultStoreId);
    result.putUnsafe(-2543, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2769, resultStoreId);
    result.putUnsafe(-2786, resultStoreId);
    result.putUnsafe(-2772, resultStoreId);
    result.putUnsafe(-2854, resultStoreId);
    result.putUnsafe(-2791, resultStoreId);
    result.putUnsafe(-2807, resultStoreId);
    result.putUnsafe(-2844, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2563, resultStoreId);
    result.putUnsafe(-2549, resultStoreId);
    result.putUnsafe(-2558, resultStoreId);
    result.putUnsafe(-2544, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2609, resultStoreId);
    result.putUnsafe(-2627, resultStoreId);
    result.putUnsafe(-2618, resultStoreId);
    result.putUnsafe(-2622, resultStoreId);
    result.putUnsafe(-2623, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2499, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-513, resultStoreId);
    result.putUnsafe(-518, resultStoreId);
    result.putUnsafe(-521, resultStoreId);
    result.putUnsafe(-510, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2548, resultStoreId);
    result.putUnsafe(-2553, resultStoreId);
    result.putUnsafe(-2539, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2515, resultStoreId);
    result.putUnsafe(-2518, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2881, resultStoreId);
    result.putUnsafe(-2867, resultStoreId);
    result.putUnsafe(-2872, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2578, resultStoreId);
    result.putUnsafe(-2564, resultStoreId);
    result.putUnsafe(-2573, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2658, resultStoreId);
    result.putUnsafe(-2667, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2628, resultStoreId);
    result.putUnsafe(-2632, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-419, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-159, resultStoreId);
      
    return result;
  }
  
  protected boolean hasNestingRestrictions(String name){
		return (_dontNest.size() != 0); // TODO Make more specific.
  }
    
  protected IntegerList getFilteredParents(int childId) {
		return _dontNest.get(childId);
  }
    
  // initialize priorities     
  static {
    _dontNest = _initDontNest();
    _resultStoreIdMappings = _initDontNestGroups();
  }
    
  // Production declarations
	
  private static final IConstructor prod__TimeLiteral_$DateTimeLiteral__time_$JustTime_ = (IConstructor) _read("prod(label(\"TimeLiteral\",sort(\"DateTimeLiteral\")),[label(\"time\",lex(\"JustTime\"))],{})", Factory.Production);
  private static final IConstructor prod__DateLiteral_$DateTimeLiteral__date_$JustDate_ = (IConstructor) _read("prod(label(\"DateLiteral\",sort(\"DateTimeLiteral\")),[label(\"date\",lex(\"JustDate\"))],{})", Factory.Production);
  private static final IConstructor prod__$RegExpLiteral__lit___47_iter_star__$RegExp_lit___47_$RegExpModifier_ = (IConstructor) _read("prod(lex(\"RegExpLiteral\"),[lit(\"/\"),\\iter-star(lex(\"RegExp\")),lit(\"/\"),lex(\"RegExpModifier\")],{})", Factory.Production);
  private static final IConstructor prod__List_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"List\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__Name_$Field__fieldName_$Name_ = (IConstructor) _read("prod(label(\"Name\",sort(\"Field\")),[label(\"fieldName\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__Default_$Renaming__from_$Name_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_to_$Name_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Renaming\")),[label(\"from\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\\>\"),layouts(\"LAYOUTLIST\"),label(\"to\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_start_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"start\")],{})", Factory.Production);
  private static final IConstructor prod__DateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_ = (IConstructor) _read("prod(label(\"DateTime\",sort(\"Literal\")),[label(\"dateTimeLiteral\",sort(\"DateTimeLiteral\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__List_$Commands__commands_iter_seps__$Command__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"List\",sort(\"Commands\")),[label(\"commands\",\\iter-seps(sort(\"Command\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"Equals\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"==\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"fail\"),[\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(105,105)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__Mid_$PathTail__mid_$MidPathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_ = (IConstructor) _read("prod(label(\"Mid\",sort(\"PathTail\")),[label(\"mid\",lex(\"MidPathChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"PathTail\"))],{})", Factory.Production);
  private static final IConstructor prod__Tuple_$Assignable__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"Assignable\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__$StringCharacter__$OctalEscapeSequence_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[lex(\"OctalEscapeSequence\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__$LAYOUT = (IConstructor) _read("regular(\\iter-star(lex(\"LAYOUT\")))", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__117_117 = (IConstructor) _read("regular(iter(\\char-class([range(117,117)])))", Factory.Production);
  private static final IConstructor prod__lit_repeat__char_class___range__114_114_char_class___range__101_101_char_class___range__112_112_char_class___range__101_101_char_class___range__97_97_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"repeat\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Or\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"||\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit_parameter__char_class___range__112_112_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"parameter\"),[\\char-class([range(112,112)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__Labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_ = (IConstructor) _read("prod(label(\"Labeled\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"label\",lex(\"NonterminalLabel\"))],{})", Factory.Production);
  private static final IConstructor prod__Num_$BasicType__lit_num_ = (IConstructor) _read("prod(label(\"Num\",sort(\"BasicType\")),[lit(\"num\")],{})", Factory.Production);
  private static final IConstructor prod__Loc_$BasicType__lit_loc_ = (IConstructor) _read("prod(label(\"Loc\",sort(\"BasicType\")),[lit(\"loc\")],{})", Factory.Production);
  private static final IConstructor prod__lit___45_61__char_class___range__45_45_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"-=\"),[\\char-class([range(45,45)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__48_57 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor prod__Renamings_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_renamings_$Renamings_ = (IConstructor) _read("prod(label(\"Renamings\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"renamings\",sort(\"Renamings\"))],{})", Factory.Production);
  private static final IConstructor prod__Default_$Declarator__type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Declarator\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Parametric_$UserType__conditional__name_$QualifiedName__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Parametric\",sort(\"UserType\")),[conditional(label(\"name\",sort(\"QualifiedName\")),{follow(lit(\"[\"))}),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_assoc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"assoc\")],{})", Factory.Production);
  private static final IConstructor prod__MidTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_ = (IConstructor) _read("prod(label(\"MidTemplate\",sort(\"StringTail\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor prod__CallOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"CallOrTree\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__lit_function__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"function\"),[\\char-class([range(102,102)]),\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"int\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__IfDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"IfDefinedOtherwise\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Variable_$Assignable__qualifiedName_$QualifiedName_ = (IConstructor) _read("prod(label(\"Variable\",sort(\"Assignable\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__Default_$TypeArg__type_$Type_ = (IConstructor) _read("prod(label(\"Default\",sort(\"TypeArg\")),[label(\"type\",sort(\"Type\"))],{})", Factory.Production);
  private static final IConstructor prod__Shell_$Command__lit___58_$layouts_LAYOUTLIST_command_$ShellCommand_ = (IConstructor) _read("prod(label(\"Shell\",sort(\"Command\")),[lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"command\",sort(\"ShellCommand\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"when\"),[\\char-class([range(119,119)]),\\char-class([range(104,104)]),\\char-class([range(101,101)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"type\"),[\\char-class([range(116,116)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_ = (IConstructor) _read("prod(label(\"Negative\",sort(\"Expression\")),[lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_catch_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"catch\")],{})", Factory.Production);
  private static final IConstructor prod__Default_$QualifiedName__conditional__names_iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST__not_follow__lit___58_58_ = (IConstructor) _read("prod(label(\"Default\",sort(\"QualifiedName\")),[conditional(label(\"names\",\\iter-seps(lex(\"Name\"),[layouts(\"LAYOUTLIST\"),lit(\"::\"),layouts(\"LAYOUTLIST\")])),{\\not-follow(lit(\"::\"))})],{})", Factory.Production);
  private static final IConstructor prod__Append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc = (IConstructor) _read("prod(label(\"Append\",sort(\"Statement\")),[lit(\"append\"),layouts(\"LAYOUTLIST\"),label(\"dataTarget\",sort(\"DataTarget\")),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lit_modules__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"modules\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_ = (IConstructor) _read("prod(lit(\"catch\"),[\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(99,99)]),\\char-class([range(104,104)])],{})", Factory.Production);
  private static final IConstructor prod__$Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalKeywords_ = (IConstructor) _read("prod(lex(\"Name\"),[conditional(seq([conditional(\\char-class([range(65,90),range(95,95),range(97,122)]),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})]),{delete(keywords(\"RascalKeywords\"))})],{})", Factory.Production);
  private static final IConstructor prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"case\"),[\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_data_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"data\")],{})", Factory.Production);
  private static final IConstructor prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"/=\"),[\\char-class([range(47,47)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_dynamic_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"dynamic\")],{})", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))", Factory.Production);
  private static final IConstructor prod__DateTime_$BasicType__lit_datetime_ = (IConstructor) _read("prod(label(\"DateTime\",sort(\"BasicType\")),[lit(\"datetime\")],{})", Factory.Production);
  private static final IConstructor prod__Structured_$Type__structured_$StructuredType_ = (IConstructor) _read("prod(label(\"Structured\",sort(\"Type\")),[label(\"structured\",sort(\"StructuredType\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"TypeVar\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__TopDownBreak_$Strategy__lit_top_down_break_ = (IConstructor) _read("prod(label(\"TopDownBreak\",sort(\"Strategy\")),[lit(\"top-down-break\")],{})", Factory.Production);
  private static final IConstructor prod__History_$ShellCommand__lit_history_ = (IConstructor) _read("prod(label(\"History\",sort(\"ShellCommand\")),[lit(\"history\")],{})", Factory.Production);
  private static final IConstructor prod__Basic_$Type__basic_$BasicType_ = (IConstructor) _read("prod(label(\"Basic\",sort(\"Type\")),[label(\"basic\",sort(\"BasicType\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"//\"),[\\char-class([range(47,47)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__lit_help__char_class___range__104_104_char_class___range__101_101_char_class___range__108_108_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"help\"),[\\char-class([range(104,104)]),\\char-class([range(101,101)]),\\char-class([range(108,108)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__Undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_ = (IConstructor) _read("prod(label(\"Undeclare\",sort(\"ShellCommand\")),[lit(\"undeclare\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_ = (IConstructor) _read("prod(lit(\"num\"),[\\char-class([range(110,110)]),\\char-class([range(117,117)]),\\char-class([range(109,109)])],{})", Factory.Production);
  private static final IConstructor prod__FieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"FieldUpdate\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"key\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"replacement\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"tag\"),[\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__Product_$Assignment__lit___42_61_ = (IConstructor) _read("prod(label(\"Product\",sort(\"Assignment\")),[lit(\"*=\")],{})", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_57 = (IConstructor) _read("regular(iter(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_55 = (IConstructor) _read("regular(iter(\\char-class([range(48,55)])))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Division_$Assignment__lit___47_61_ = (IConstructor) _read("prod(label(\"Division\",sort(\"Assignment\")),[lit(\"/=\")],{})", Factory.Production);
  private static final IConstructor prod__ReifiedFunction_$BasicType__lit_fun_ = (IConstructor) _read("prod(label(\"ReifiedFunction\",sort(\"BasicType\")),[lit(\"fun\")],{})", Factory.Production);
  private static final IConstructor prod__Toplevels_$Body__toplevels_iter_star_seps__$Toplevel__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Toplevels\",sort(\"Body\")),[label(\"toplevels\",\\iter-star-seps(sort(\"Toplevel\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__$PreStringChars__char_class___range__34_34_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"PreStringChars\"),[\\char-class([range(34,34)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(60,60)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Tag_$Kind__lit_tag_ = (IConstructor) _read("prod(label(\"Tag\",sort(\"Kind\")),[lit(\"tag\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Import__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"://\"),[\\char-class([range(58,58)]),\\char-class([range(47,47)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__Alias_$Kind__lit_alias_ = (IConstructor) _read("prod(label(\"Alias\",sort(\"Kind\")),[lit(\"alias\")],{})", Factory.Production);
  private static final IConstructor prod__TryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement_ = (IConstructor) _read("prod(label(\"TryFinally\",sort(\"Statement\")),[lit(\"try\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),label(\"handlers\",\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"finally\"),layouts(\"LAYOUTLIST\"),label(\"finallyBody\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__$JustDate__lit___36_$DatePart_ = (IConstructor) _read("prod(lex(\"JustDate\"),[lit(\"$\"),lex(\"DatePart\")],{})", Factory.Production);
  private static final IConstructor prod__Map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Map\",sort(\"Comprehension\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"from\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Quit_$ShellCommand__lit_quit_ = (IConstructor) _read("prod(label(\"Quit\",sort(\"ShellCommand\")),[lit(\"quit\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(100,100),range(105,105),range(109,109),range(115,115)])))", Factory.Production);
  private static final IConstructor prod__lit___33_62_62__char_class___range__33_33_char_class___range__62_62_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"!\\>\\>\"),[\\char-class([range(33,33)]),\\char-class([range(62,62)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__Subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Subscript\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"subscripts\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__Default_$ImportedModule__name_$QualifiedName_ = (IConstructor) _read("prod(label(\"Default\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_ = (IConstructor) _read("prod(lit(\"..\"),[\\char-class([range(46,46)]),\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_ = (IConstructor) _read("prod(lit(\"throw\"),[\\char-class([range(116,116)]),\\char-class([range(104,104)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(119,119)])],{})", Factory.Production);
  private static final IConstructor prod__Dynamic_$LocalVariableDeclaration__lit_dynamic_$layouts_LAYOUTLIST_declarator_$Declarator_ = (IConstructor) _read("prod(label(\"Dynamic\",sort(\"LocalVariableDeclaration\")),[lit(\"dynamic\"),layouts(\"LAYOUTLIST\"),label(\"declarator\",sort(\"Declarator\"))],{})", Factory.Production);
  private static final IConstructor prod__Equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"Equivalence\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\<==\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lit_on__char_class___range__111_111_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"on\"),[\\char-class([range(111,111)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__Labeled_$Target__name_$Name_ = (IConstructor) _read("prod(label(\"Labeled\",sort(\"Target\")),[label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__$StringCharacter__$UnicodeEscape_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[lex(\"UnicodeEscape\")],{})", Factory.Production);
  private static final IConstructor prod__lit___47_42__char_class___range__47_47_char_class___range__42_42_ = (IConstructor) _read("prod(lit(\"/*\"),[\\char-class([range(47,47)]),\\char-class([range(42,42)])],{})", Factory.Production);
  private static final IConstructor prod__TopDown_$Strategy__lit_top_down_ = (IConstructor) _read("prod(label(\"TopDown\",sort(\"Strategy\")),[lit(\"top-down\")],{})", Factory.Production);
  private static final IConstructor prod__Default_$LocationLiteral__protocolPart_$ProtocolPart_$layouts_LAYOUTLIST_pathPart_$PathPart_ = (IConstructor) _read("prod(label(\"Default\",sort(\"LocationLiteral\")),[label(\"protocolPart\",sort(\"ProtocolPart\")),layouts(\"LAYOUTLIST\"),label(\"pathPart\",sort(\"PathPart\"))],{})", Factory.Production);
  private static final IConstructor prod__$DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"DecimalIntegerLiteral\"),[conditional(lit(\"0\"),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__IfThenElse_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_elseStatement_$Statement_ = (IConstructor) _read("prod(label(\"IfThenElse\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"thenStatement\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),lit(\"else\"),layouts(\"LAYOUTLIST\"),label(\"elseStatement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__Match_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"Match\",sort(\"Expression\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lit_is__char_class___range__105_105_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"is\"),[\\char-class([range(105,105)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_it__char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"it\"),[\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Import_$Command__imported_$Import_ = (IConstructor) _read("prod(label(\"Import\",sort(\"Command\")),[label(\"imported\",sort(\"Import\"))],{})", Factory.Production);
  private static final IConstructor prod__Descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_ = (IConstructor) _read("prod(label(\"Descendant\",sort(\"Pattern\")),[lit(\"/\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__Template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_ = (IConstructor) _read("prod(label(\"Template\",sort(\"StringLiteral\")),[label(\"pre\",lex(\"PreStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"loc\"),[\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__CaseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_ = (IConstructor) _read("prod(label(\"CaseInsensitiveLiteral\",sort(\"Sym\")),[label(\"cistring\",lex(\"CaseInsensitiveStringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__Mid_$StringMiddle__mid_$MidStringChars_ = (IConstructor) _read("prod(label(\"Mid\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\"))],{})", Factory.Production);
  private static final IConstructor prod__Binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement__tag__Foldable = (IConstructor) _read("prod(label(\"Binding\",sort(\"Catch\")),[lit(\"catch\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_any_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"any\")],{})", Factory.Production);
  private static final IConstructor prod__Variable_$Type__typeVar_$TypeVar_ = (IConstructor) _read("prod(label(\"Variable\",sort(\"Type\")),[label(\"typeVar\",sort(\"TypeVar\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_all_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"all\")],{})", Factory.Production);
  private static final IConstructor prod__lit_in__char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"in\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__Default_$ModuleActuals__lit___91_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Default\",sort(\"ModuleActuals\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"types\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__All_$Kind__lit_all_ = (IConstructor) _read("prod(label(\"All\",sort(\"Kind\")),[lit(\"all\")],{})", Factory.Production);
  private static final IConstructor prod__$MidStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"MidStringChars\"),[\\char-class([range(62,62)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(60,60)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Foldable_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"Expression\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{tag(Foldable()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__lit_declarations__char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"declarations\"),[\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_right__char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"right\"),[\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(103,103)]),\\char-class([range(104,104)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__DefaultStrategy_$Visit__lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"DefaultStrategy\",sort(\"Visit\")),[lit(\"visit\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"subject\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Unconditional_$Replacement__replacementExpression_$Expression_ = (IConstructor) _read("prod(label(\"Unconditional\",sort(\"Replacement\")),[label(\"replacementExpression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_case_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"case\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_layout_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"layout\")],{})", Factory.Production);
  private static final IConstructor prod__QualifiedName_$Pattern__qualifiedName_$QualifiedName_ = (IConstructor) _read("prod(label(\"QualifiedName\",sort(\"Pattern\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___59__char_class___range__59_59_ = (IConstructor) _read("prod(lit(\";\"),[\\char-class([range(59,59)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_bag_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bag\")],{})", Factory.Production);
  private static final IConstructor prod__lit___58__char_class___range__58_58_ = (IConstructor) _read("prod(lit(\":\"),[\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__ReifiedType_$Expression__basicType_$BasicType_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"ReifiedType\",sort(\"Expression\")),[label(\"basicType\",sort(\"BasicType\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__lit___61__char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"=\"),[\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_ = (IConstructor) _read("prod(lit(\"data\"),[\\char-class([range(100,100)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(97,97)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60__char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\<\"),[\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__lit___63__char_class___range__63_63_ = (IConstructor) _read("prod(lit(\"?\"),[\\char-class([range(63,63)])],{})", Factory.Production);
  private static final IConstructor prod__Difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left = (IConstructor) _read("prod(label(\"Difference\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__43_43_range__45_45 = (IConstructor) _read("regular(opt(\\char-class([range(43,43),range(45,45)])))", Factory.Production);
  private static final IConstructor prod__lit___33_60_60__char_class___range__33_33_char_class___range__60_60_char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"!\\<\\<\"),[\\char-class([range(33,33)]),\\char-class([range(60,60)]),\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__lit___62__char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\>\"),[\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__Expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Expression\",sort(\"Statement\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lit_0__char_class___range__48_48_ = (IConstructor) _read("prod(lit(\"0\"),[\\char-class([range(48,48)])],{})", Factory.Production);
  private static final IConstructor prod__DoWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"DoWhile\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"do\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Default_$Renamings__lit_renaming_$layouts_LAYOUTLIST_renamings_iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Renamings\")),[lit(\"renaming\"),layouts(\"LAYOUTLIST\"),label(\"renamings\",\\iter-seps(sort(\"Renaming\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__lit___41__char_class___range__41_41_ = (IConstructor) _read("prod(lit(\")\"),[\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___40__char_class___range__40_40_ = (IConstructor) _read("prod(lit(\"(\"),[\\char-class([range(40,40)])],{})", Factory.Production);
  private static final IConstructor prod__lit___43__char_class___range__43_43_ = (IConstructor) _read("prod(lit(\"+\"),[\\char-class([range(43,43)])],{})", Factory.Production);
  private static final IConstructor prod__lit___42__char_class___range__42_42_ = (IConstructor) _read("prod(lit(\"*\"),[\\char-class([range(42,42)])],{})", Factory.Production);
  private static final IConstructor prod__Integer_$Literal__integerLiteral_$IntegerLiteral_ = (IConstructor) _read("prod(label(\"Integer\",sort(\"Literal\")),[label(\"integerLiteral\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__lit____char_class___range__45_45_ = (IConstructor) _read("prod(lit(\"-\"),[\\char-class([range(45,45)])],{})", Factory.Production);
  private static final IConstructor prod__lit___44__char_class___range__44_44_ = (IConstructor) _read("prod(lit(\",\"),[\\char-class([range(44,44)])],{})", Factory.Production);
  private static final IConstructor prod__Default_$Case__lit_default_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement__tag__Foldable = (IConstructor) _read("prod(label(\"Default\",sort(\"Case\")),[lit(\"default\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit___47__char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"/\"),[\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__lit___46__char_class___range__46_46_ = (IConstructor) _read("prod(lit(\".\"),[\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__lit_if__char_class___range__105_105_char_class___range__102_102_ = (IConstructor) _read("prod(lit(\"if\"),[\\char-class([range(105,105)]),\\char-class([range(102,102)])],{})", Factory.Production);
  private static final IConstructor prod__start__$PreModule__$layouts_LAYOUTLIST_top_$PreModule_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"PreModule\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"PreModule\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__lit___33__char_class___range__33_33_ = (IConstructor) _read("prod(lit(\"!\"),[\\char-class([range(33,33)])],{})", Factory.Production);
  private static final IConstructor prod__lit___35__char_class___range__35_35_ = (IConstructor) _read("prod(lit(\"#\"),[\\char-class([range(35,35)])],{})", Factory.Production);
  private static final IConstructor prod__lit___34__char_class___range__34_34_ = (IConstructor) _read("prod(lit(\"\\\"\"),[\\char-class([range(34,34)])],{})", Factory.Production);
  private static final IConstructor prod__lit___37__char_class___range__37_37_ = (IConstructor) _read("prod(lit(\"%\"),[\\char-class([range(37,37)])],{})", Factory.Production);
  private static final IConstructor prod__lit___36__char_class___range__36_36_ = (IConstructor) _read("prod(lit(\"$\"),[\\char-class([range(36,36)])],{})", Factory.Production);
  private static final IConstructor prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"return\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(117,117)]),\\char-class([range(114,114)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit___39__char_class___range__39_39_ = (IConstructor) _read("prod(lit(\"\\'\\\\\"),[\\char-class([range(39,39)])],{})", Factory.Production);
  private static final IConstructor prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"datetime\"),[\\char-class([range(100,100)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit___38__char_class___range__38_38_ = (IConstructor) _read("prod(lit(\"&\"),[\\char-class([range(38,38)])],{})", Factory.Production);
  private static final IConstructor prod__Default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement__tag__Foldable = (IConstructor) _read("prod(label(\"Default\",sort(\"Catch\")),[lit(\"catch\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit_Z__char_class___range__90_90_ = (IConstructor) _read("prod(lit(\"Z\"),[\\char-class([range(90,90)])],{})", Factory.Production);
  private static final IConstructor prod__lit___91__char_class___range__91_91_ = (IConstructor) _read("prod(lit(\"[\"),[\\char-class([range(91,91)])],{})", Factory.Production);
  private static final IConstructor prod__Default_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Parameters\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"formals\",sort(\"Formals\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__NoThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_ = (IConstructor) _read("prod(label(\"NoThrows\",sort(\"Signature\")),[label(\"modifiers\",sort(\"FunctionModifiers\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___94__char_class___range__94_94_ = (IConstructor) _read("prod(lit(\"^\"),[\\char-class([range(94,94)])],{})", Factory.Production);
  private static final IConstructor prod__Actuals_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_ = (IConstructor) _read("prod(label(\"Actuals\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"actuals\",sort(\"ModuleActuals\"))],{})", Factory.Production);
  private static final IConstructor prod__Visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_ = (IConstructor) _read("prod(label(\"Visit\",sort(\"Expression\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),label(\"visit\",sort(\"Visit\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___92__char_class___range__92_92_ = (IConstructor) _read("prod(lit(\"\\\\\"),[\\char-class([range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__Default_$LocalVariableDeclaration__declarator_$Declarator_ = (IConstructor) _read("prod(label(\"Default\",sort(\"LocalVariableDeclaration\")),[label(\"declarator\",sort(\"Declarator\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___93__char_class___range__93_93_ = (IConstructor) _read("prod(lit(\"]\"),[\\char-class([range(93,93)])],{})", Factory.Production);
  private static final IConstructor regular__opt__$TimeZonePart = (IConstructor) _read("regular(opt(lex(\"TimeZonePart\")))", Factory.Production);
  private static final IConstructor prod__$NamedRegExp__$NamedBackslash_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[lex(\"NamedBackslash\")],{})", Factory.Production);
  private static final IConstructor prod__Subtraction_$Assignment__lit___45_61_ = (IConstructor) _read("prod(label(\"Subtraction\",sort(\"Assignment\")),[lit(\"-=\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__$Backslash_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lex(\"Backslash\")],{})", Factory.Production);
  private static final IConstructor prod__lit_T__char_class___range__84_84_ = (IConstructor) _read("prod(lit(\"T\"),[\\char-class([range(84,84)])],{})", Factory.Production);
  private static final IConstructor prod__While_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_ = (IConstructor) _read("prod(label(\"While\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__Assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement_ = (IConstructor) _read("prod(label(\"Assignment\",sort(\"Statement\")),[label(\"assignable\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),label(\"operator\",sort(\"Assignment\")),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"alias\"),[\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(97,97)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__DecimalIntegerLiteral_$IntegerLiteral__decimal_$DecimalIntegerLiteral_ = (IConstructor) _read("prod(label(\"DecimalIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"decimal\",lex(\"DecimalIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__9_9_range__32_32 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(9,9),range(32,32)])))", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_assert_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"assert\")],{})", Factory.Production);
  private static final IConstructor prod__lit___64__char_class___range__64_64_ = (IConstructor) _read("prod(lit(\"@\"),[\\char-class([range(64,64)])],{})", Factory.Production);
  private static final IConstructor prod__lit___43_61__char_class___range__43_43_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"+=\"),[\\char-class([range(43,43)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_import_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"import\")],{})", Factory.Production);
  private static final IConstructor prod__ListDeclarations_$ShellCommand__lit_declarations_ = (IConstructor) _read("prod(label(\"ListDeclarations\",sort(\"ShellCommand\")),[lit(\"declarations\")],{})", Factory.Production);
  private static final IConstructor prod__Range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Range\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"..\"),layouts(\"LAYOUTLIST\"),label(\"last\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit___123__char_class___range__123_123_ = (IConstructor) _read("prod(lit(\"{\"),[\\char-class([range(123,123)])],{})", Factory.Production);
  private static final IConstructor prod__lit___33_58_61__char_class___range__33_33_char_class___range__58_58_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"!:=\"),[\\char-class([range(33,33)]),\\char-class([range(58,58)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Tag__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Tag\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Literal_$Expression__literal_$Literal_ = (IConstructor) _read("prod(label(\"Literal\",sort(\"Expression\")),[label(\"literal\",sort(\"Literal\"))],{})", Factory.Production);
  private static final IConstructor prod__Continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Continue\",sort(\"Statement\")),[lit(\"continue\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Extend_$Import__lit_extend_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Extend\",sort(\"Import\")),[lit(\"extend\"),layouts(\"LAYOUTLIST\"),label(\"module\",sort(\"ImportedModule\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lit___125__char_class___range__125_125_ = (IConstructor) _read("prod(lit(\"}\"),[\\char-class([range(125,125)])],{})", Factory.Production);
  private static final IConstructor prod__lit___124__char_class___range__124_124_ = (IConstructor) _read("prod(lit(\"|\"),[\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__Set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Set\",sort(\"Expression\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__lit___42_47__char_class___range__42_42_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"*/\"),[\\char-class([range(42,42)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__Default_$Tags__tags_iter_star_seps__$Tag__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Tags\")),[label(\"tags\",\\iter-star-seps(sort(\"Tag\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Interpolated_$ProtocolPart__pre_$PreProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_ = (IConstructor) _read("prod(label(\"Interpolated\",sort(\"ProtocolPart\")),[label(\"pre\",lex(\"PreProtocolChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"ProtocolTail\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_true_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"true\")],{})", Factory.Production);
  private static final IConstructor prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__Labeled_$DataTarget__label_$Name_$layouts_LAYOUTLIST_lit___58_ = (IConstructor) _read("prod(label(\"Labeled\",sort(\"DataTarget\")),[label(\"label\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\")],{})", Factory.Production);
  private static final IConstructor prod__Outermost_$Strategy__lit_outermost_ = (IConstructor) _read("prod(label(\"Outermost\",sort(\"Strategy\")),[lit(\"outermost\")],{})", Factory.Production);
  private static final IConstructor prod__Addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Addition\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"+\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(49,57)]),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__NonAssociative_$Assoc__lit_non_assoc_ = (IConstructor) _read("prod(label(\"NonAssociative\",sort(\"Assoc\")),[lit(\"non-assoc\")],{})", Factory.Production);
  private static final IConstructor prod__lit_o__char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"o\"),[\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Toplevel__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Toplevel\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit_unimport__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"unimport\"),[\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Switch\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"switch\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Bool_$BasicType__lit_bool_ = (IConstructor) _read("prod(label(\"Bool\",sort(\"BasicType\")),[lit(\"bool\")],{})", Factory.Production);
  private static final IConstructor prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"*=\"),[\\char-class([range(42,42)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__Syntax_$Import__syntax_$SyntaxDefinition_ = (IConstructor) _read("prod(label(\"Syntax\",sort(\"Import\")),[label(\"syntax\",sort(\"SyntaxDefinition\"))],{})", Factory.Production);
  private static final IConstructor prod__$RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_ = (IConstructor) _read("prod(lex(\"RegExpModifier\"),[\\iter-star(\\char-class([range(100,100),range(105,105),range(109,109),range(115,115)]))],{})", Factory.Production);
  private static final IConstructor prod__$NamedRegExp__lit___60_$Name_lit___62_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__Default_$StructuredType__basicType_$BasicType_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_arguments_iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Default\",sort(\"StructuredType\")),[label(\"basicType\",sort(\"BasicType\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__Join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Join\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"join\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Default_$FunctionModifier__lit_default_ = (IConstructor) _read("prod(label(\"Default\",sort(\"FunctionModifier\")),[lit(\"default\")],{})", Factory.Production);
  private static final IConstructor prod__lit_edit__char_class___range__101_101_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"edit\"),[\\char-class([range(101,101)]),\\char-class([range(100,100)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535 = (IConstructor) _read("regular(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,65535)])}))", Factory.Production);
  private static final IConstructor prod__Empty_$Label__ = (IConstructor) _read("prod(label(\"Empty\",sort(\"Label\")),[],{})", Factory.Production);
  private static final IConstructor prod__Bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"Bracket\",sort(\"Assignable\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arg\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__Function_$Type__function_$FunctionType_ = (IConstructor) _read("prod(label(\"Function\",sort(\"Type\")),[label(\"function\",sort(\"FunctionType\"))],{})", Factory.Production);
  private static final IConstructor prod__Others_$Prod__lit___46_46_46_ = (IConstructor) _read("prod(label(\"Others\",sort(\"Prod\")),[lit(\"...\")],{})", Factory.Production);
  private static final IConstructor prod__IfThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"IfThenElse\",sort(\"StringTemplate\")),[lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStatsThen\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"thenString\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStatsThen\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"else\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStatsElse\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"elseString\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStatsElse\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_finally_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"finally\")],{})", Factory.Production);
  private static final IConstructor prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"node\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Expression\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right = (IConstructor) _read("prod(label(\"Precede\",sort(\"Sym\")),[label(\"match\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\<\\<\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__ReifyType_$Expression__lit___35_$layouts_LAYOUTLIST_type_$Type_ = (IConstructor) _read("prod(label(\"ReifyType\",sort(\"Expression\")),[lit(\"#\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\"))],{})", Factory.Production);
  private static final IConstructor prod__Reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Reducer\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"init\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"result\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__FieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_ = (IConstructor) _read("prod(label(\"FieldAccess\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"field\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_while_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"while\")],{})", Factory.Production);
  private static final IConstructor prod__Void_$BasicType__lit_void_ = (IConstructor) _read("prod(label(\"Void\",sort(\"BasicType\")),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_57_range__65_70_range__97_102 = (IConstructor) _read("regular(iter(\\char-class([range(48,57),range(65,70),range(97,102)])))", Factory.Production);
  private static final IConstructor prod__Post_$ProtocolTail__post_$PostProtocolChars_ = (IConstructor) _read("prod(label(\"Post\",sort(\"ProtocolTail\")),[label(\"post\",lex(\"PostProtocolChars\"))],{})", Factory.Production);
  private static final IConstructor prod__Default_$Bound__lit___59_$layouts_LAYOUTLIST_expression_$Expression_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Bound\")),[lit(\";\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(lex(\"Name\"),[layouts(\"LAYOUTLIST\"),lit(\"::\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_ = (IConstructor) _read("prod(lit(\"$T\"),[\\char-class([range(36,36)]),\\char-class([range(84,84)])],{})", Factory.Production);
  private static final IConstructor prod__lit_do__char_class___range__100_100_char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"do\"),[\\char-class([range(100,100)]),\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__Alias_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_alias_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_base_$Type_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Alias\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"alias\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"base\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__MidInterpolated_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_ = (IConstructor) _read("prod(label(\"MidInterpolated\",sort(\"StringTail\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__Optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_ = (IConstructor) _read("prod(label(\"Optional\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"?\")],{})", Factory.Production);
  private static final IConstructor prod__start__$Command__$layouts_LAYOUTLIST_top_$Command_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Command\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Command\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_visit_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"visit\")],{})", Factory.Production);
  private static final IConstructor prod__$MidProtocolChars__lit___62_$URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"MidProtocolChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__lit_java__char_class___range__106_106_char_class___range__97_97_char_class___range__118_118_char_class___range__97_97_ = (IConstructor) _read("prod(lit(\"java\"),[\\char-class([range(106,106)]),\\char-class([range(97,97)]),\\char-class([range(118,118)]),\\char-class([range(97,97)])],{})", Factory.Production);
  private static final IConstructor prod__Data_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_variants_iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Data\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"data\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"variants\",\\iter-seps(sort(\"Variant\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__PatternWithAction_$Case__lit_case_$layouts_LAYOUTLIST_patternWithAction_$PatternWithAction__tag__Foldable = (IConstructor) _read("prod(label(\"PatternWithAction\",sort(\"Case\")),[lit(\"case\"),layouts(\"LAYOUTLIST\"),label(\"patternWithAction\",sort(\"PatternWithAction\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__NoMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"NoMatch\",sort(\"Expression\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"!:=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__TypedVariable_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"TypedVariable\",sort(\"Pattern\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_notin_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"notin\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_append_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"append\")],{})", Factory.Production);
  private static final IConstructor prod__Declaration_$Command__declaration_$Declaration_ = (IConstructor) _read("prod(label(\"Declaration\",sort(\"Command\")),[label(\"declaration\",sort(\"Declaration\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Sym__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_ = (IConstructor) _read("prod(label(\"Parameter\",sort(\"Sym\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"nonterminal\",lex(\"Nonterminal\"))],{})", Factory.Production);
  private static final IConstructor prod__$TagString__lit___123_iter_star__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535_lit___125_ = (IConstructor) _read("prod(lex(\"TagString\"),[lit(\"{\"),\\iter-star(alt({seq([\\char-class([range(92,92)]),\\char-class([range(125,125)])]),\\char-class([range(0,124),range(126,65535)])})),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Public_$Visibility__lit_public_ = (IConstructor) _read("prod(label(\"Public\",sort(\"Visibility\")),[lit(\"public\")],{})", Factory.Production);
  private static final IConstructor prod__Intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Intersection\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit_quit__char_class___range__113_113_char_class___range__117_117_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"quit\"),[\\char-class([range(113,113)]),\\char-class([range(117,117)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"tuple\"),[\\char-class([range(116,116)]),\\char-class([range(117,117)]),\\char-class([range(112,112)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_non_terminal_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"non-terminal\")],{})", Factory.Production);
  private static final IConstructor prod__$OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"OctalIntegerLiteral\"),[\\char-class([range(48,48)]),conditional(iter(\\char-class([range(48,55)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Variant\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__VoidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"VoidClosure\",sort(\"Expression\")),[label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_try_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"try\")],{})", Factory.Production);
  private static final IConstructor prod__Real_$BasicType__lit_real_ = (IConstructor) _read("prod(label(\"Real\",sort(\"BasicType\")),[lit(\"real\")],{})", Factory.Production);
  private static final IConstructor prod__lit_has__char_class___range__104_104_char_class___range__97_97_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"has\"),[\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__Int_$BasicType__lit_int_ = (IConstructor) _read("prod(label(\"Int\",sort(\"BasicType\")),[lit(\"int\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_anno_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"anno\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_insert_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"insert\")],{})", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__92_92_char_class___range__125_125 = (IConstructor) _read("regular(seq([\\char-class([range(92,92)]),\\char-class([range(125,125)])]))", Factory.Production);
  private static final IConstructor prod__$RegExp__char_class___range__60_60_expression_$Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101 = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(60,60)]),label(\"expression\",sort(\"Expression\")),\\char-class([range(62,62)])],{tag(category(\"MetaVariable\"))})", Factory.Production);
  private static final IConstructor prod__Alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Alternative\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"alternatives\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Left_$Assoc__lit_left_ = (IConstructor) _read("prod(label(\"Left\",sort(\"Assoc\")),[lit(\"left\")],{})", Factory.Production);
  private static final IConstructor prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_65535__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[\\char-class([range(0,31),range(33,33),range(35,38),range(40,44),range(46,59),range(61,61),range(63,90),range(94,65535)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_start_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"start\")],{})", Factory.Production);
  private static final IConstructor regular__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(seq([conditional(\\char-class([range(65,90),range(95,95),range(97,122)]),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})]))", Factory.Production);
  private static final IConstructor prod__lit_outermost__char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"outermost\"),[\\char-class([range(111,111)]),\\char-class([range(117,117)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Module_$Kind__lit_module_ = (IConstructor) _read("prod(label(\"Module\",sort(\"Kind\")),[lit(\"module\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_test_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__lit___60_$Name_lit___62_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"DatePart\"),[\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),\\char-class([range(48,51)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__Implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"Implication\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"==\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_ = (IConstructor) _read("prod(lit(\"&&\"),[\\char-class([range(38,38)]),\\char-class([range(38,38)])],{})", Factory.Production);
  private static final IConstructor prod__Default_$Mapping__$Expression__from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_ = (IConstructor) _read("prod(label(\"Default\",\\parameterized-sort(\"Mapping\",[sort(\"Expression\")])),[label(\"from\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Statement__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Node_$BasicType__lit_node_ = (IConstructor) _read("prod(label(\"Node\",sort(\"BasicType\")),[lit(\"node\")],{})", Factory.Production);
  private static final IConstructor prod__$BooleanLiteral__lit_false_ = (IConstructor) _read("prod(lex(\"BooleanLiteral\"),[lit(\"false\")],{})", Factory.Production);
  private static final IConstructor prod__GlobalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"GlobalDirective\",sort(\"Statement\")),[lit(\"global\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"names\",\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Function_$Declaration__functionDeclaration_$FunctionDeclaration_ = (IConstructor) _read("prod(label(\"Function\",sort(\"Declaration\")),[label(\"functionDeclaration\",sort(\"FunctionDeclaration\"))],{})", Factory.Production);
  private static final IConstructor prod__Rule_$Kind__lit_rule_ = (IConstructor) _read("prod(label(\"Rule\",sort(\"Kind\")),[lit(\"rule\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"FunctionModifier\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Modulo\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"%\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Constructor\",sort(\"Assignable\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__CallOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"CallOrTree\",sort(\"Pattern\")),[label(\"expression\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"&=\"),[\\char-class([range(38,38)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__Tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"Pattern\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__TransitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_ = (IConstructor) _read("prod(label(\"TransitiveClosure\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"+\"),{\\not-follow(lit(\"=\"))})],{})", Factory.Production);
  private static final IConstructor prod__Labeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Labeled\",sort(\"Prod\")),[label(\"modifiers\",\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"args\",\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Test_$ShellCommand__lit_test_ = (IConstructor) _read("prod(label(\"Test\",sort(\"ShellCommand\")),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_real_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"real\")],{})", Factory.Production);
  private static final IConstructor prod__Filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Filter\",sort(\"Statement\")),[lit(\"filter\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"!=\"),[\\char-class([range(33,33)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__$StringConstant__lit___34_iter_star__$StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"StringConstant\"),[lit(\"\\\"\"),\\iter-star(lex(\"StringCharacter\")),lit(\"\\\"\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__IfDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_ = (IConstructor) _read("prod(label(\"IfDefinedOrDefault\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"defaultExpression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__$PreProtocolChars__lit___124_$URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"PreProtocolChars\"),[lit(\"|\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(92,92)]),\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__Iter_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___43_ = (IConstructor) _read("prod(label(\"Iter\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"+\")],{})", Factory.Production);
  private static final IConstructor prod__EndOfLine_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___36_ = (IConstructor) _read("prod(label(\"EndOfLine\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"$\")],{})", Factory.Production);
  private static final IConstructor prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"anno\"),[\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(110,110)]),\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__NAryConstructor_$Variant__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"NAryConstructor\",sort(\"Variant\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_ = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(0,46),range(48,59),range(61,61),range(63,91),range(93,65535)])],{})", Factory.Production);
  private static final IConstructor prod__Default_$ModuleParameters__lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Default\",sort(\"ModuleParameters\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"TypeVar\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__NonInterpolated_$StringLiteral__constant_$StringConstant_ = (IConstructor) _read("prod(label(\"NonInterpolated\",sort(\"StringLiteral\")),[label(\"constant\",lex(\"StringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__Absent_$Start__ = (IConstructor) _read("prod(label(\"Absent\",sort(\"Start\")),[],{})", Factory.Production);
  private static final IConstructor prod__Default_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_body_$FunctionBody__tag__Foldable = (IConstructor) _read("prod(label(\"Default\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"FunctionBody\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit_top_down__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"top-down\"),[\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(100,100)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__DateAndTimeLiteral_$DateTimeLiteral__dateAndTime_$DateAndTime_ = (IConstructor) _read("prod(label(\"DateAndTimeLiteral\",sort(\"DateTimeLiteral\")),[label(\"dateAndTime\",lex(\"DateAndTime\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_ = (IConstructor) _read("prod(lit(\"...\"),[\\char-class([range(46,46)]),\\char-class([range(46,46)]),\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__Variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Variable\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lit_top_down_break__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"top-down-break\"),[\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(100,100)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(110,110)]),\\char-class([range(45,45)]),\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__LessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"LessThan\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\<\"),{\\not-follow(lit(\"-\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_join_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"join\")],{})", Factory.Production);
  private static final IConstructor prod__$Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[lit(\"\\\\\"),\\char-class([range(32,32),range(34,34),range(39,39),range(45,45),range(60,60),range(62,62),range(91,93),range(98,98),range(102,102),range(110,110),range(114,114),range(116,116)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__lit_view__char_class___range__118_118_char_class___range__105_105_char_class___range__101_101_char_class___range__119_119_ = (IConstructor) _read("prod(lit(\"view\"),[\\char-class([range(118,118)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(119,119)])],{})", Factory.Production);
  private static final IConstructor prod__lit_innermost__char_class___range__105_105_char_class___range__110_110_char_class___range__110_110_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"innermost\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(110,110)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_one__char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"one\"),[\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_reified__char_class___range__114_114_char_class___range__101_101_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"reified\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(105,105)]),\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Renaming\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Tuple_$BasicType__lit_tuple_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"BasicType\")),[lit(\"tuple\")],{})", Factory.Production);
  private static final IConstructor prod__Interpolated_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_ = (IConstructor) _read("prod(label(\"Interpolated\",sort(\"StringLiteral\")),[label(\"pre\",lex(\"PreStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_import_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"import\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$ProtocolChars__char_class___range__124_124_$URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_ = (IConstructor) _read("prod(lex(\"ProtocolChars\"),[\\char-class([range(124,124)]),lex(\"URLChars\"),conditional(lit(\"://\"),{\\not-follow(\\char-class([range(9,10),range(13,13),range(32,32)]))})],{})", Factory.Production);
  private static final IConstructor prod__$CaseInsensitiveStringConstant__lit___39_iter_star__$StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"CaseInsensitiveStringConstant\"),[lit(\"\\'\\\\\"),\\iter-star(lex(\"StringCharacter\")),lit(\"\\'\\\\\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"while\"),[\\char-class([range(119,119)]),\\char-class([range(104,104)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"notin\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor regular__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535 = (IConstructor) _read("regular(alt({seq([\\char-class([range(92,92)]),\\char-class([range(125,125)])]),\\char-class([range(0,124),range(126,65535)])}))", Factory.Production);
  private static final IConstructor prod__Parameters_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_params_$ModuleParameters_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Parameters\",sort(\"Header\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),lit(\"module\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"params\",sort(\"ModuleParameters\")),layouts(\"LAYOUTLIST\"),label(\"imports\",\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))])))", Factory.Production);
  private static final IConstructor prod__Literal_$Sym__string_$StringConstant_ = (IConstructor) _read("prod(label(\"Literal\",sort(\"Sym\")),[label(\"string\",lex(\"StringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__$Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalReservedKeywords_not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"Nonterminal\"),[conditional(\\char-class([range(65,90)]),{\\not-precede(\\char-class([range(65,90)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),delete(sort(\"RascalReservedKeywords\"))})],{})", Factory.Production);
  private static final IConstructor prod__View_$Kind__lit_view_ = (IConstructor) _read("prod(label(\"View\",sort(\"Kind\")),[lit(\"view\")],{})", Factory.Production);
  private static final IConstructor prod__AssertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"AssertWithMessage\",sort(\"Statement\")),[lit(\"assert\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"message\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__NotFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left = (IConstructor) _read("prod(label(\"NotFollow\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_void_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[\\char-class([range(0,46),range(48,59),range(61,61),range(63,91),range(93,65535)])],{})", Factory.Production);
  private static final IConstructor prod__Real_$Literal__realLiteral_$RealLiteral_ = (IConstructor) _read("prod(label(\"Real\",sort(\"Literal\")),[label(\"realLiteral\",lex(\"RealLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__VariableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"VariableDeclaration\",sort(\"Statement\")),[label(\"declaration\",sort(\"LocalVariableDeclaration\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_ = (IConstructor) _read("prod(label(\"Solve\",sort(\"Statement\")),[lit(\"solve\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"bound\",sort(\"Bound\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__$JustTime__lit___36_84_$TimePartNoTZ_opt__$TimeZonePart_ = (IConstructor) _read("prod(lex(\"JustTime\"),[lit(\"$T\"),lex(\"TimePartNoTZ\"),opt(lex(\"TimeZonePart\"))],{})", Factory.Production);
  private static final IConstructor prod__Boolean_$Literal__booleanLiteral_$BooleanLiteral_ = (IConstructor) _read("prod(label(\"Boolean\",sort(\"Literal\")),[label(\"booleanLiteral\",lex(\"BooleanLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__Default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_iter_star_seps__$Word__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Default\",sort(\"PreModule\")),[label(\"header\",sort(\"Header\")),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(keywords(\"HeaderKeyword\"))}),layouts(\"LAYOUTLIST\"),label(\"rest\",\\iter-star-seps(lex(\"Word\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__VarArgs_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___46_46_46_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"VarArgs\",sort(\"Parameters\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"formals\",sort(\"Formals\")),layouts(\"LAYOUTLIST\"),lit(\"...\"),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Statement_$Command__statement_$Statement_ = (IConstructor) _read("prod(label(\"Statement\",sort(\"Command\")),[label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__BottomUpBreak_$Strategy__lit_bottom_up_break_ = (IConstructor) _read("prod(label(\"BottomUpBreak\",sort(\"Strategy\")),[lit(\"bottom-up-break\")],{})", Factory.Production);
  private static final IConstructor prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"default\"),[\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__TransitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_ = (IConstructor) _read("prod(label(\"TransitiveReflexiveClosure\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"*\"),{\\not-follow(lit(\"=\"))})],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"bool\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(111,111)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__ReifiedReifiedType_$BasicType__lit_reified_ = (IConstructor) _read("prod(label(\"ReifiedReifiedType\",sort(\"BasicType\")),[lit(\"reified\")],{})", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102 = (IConstructor) _read("regular(opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)])))", Factory.Production);
  private static final IConstructor prod__ReifiedTypeParameter_$BasicType__lit_parameter_ = (IConstructor) _read("prod(label(\"ReifiedTypeParameter\",sort(\"BasicType\")),[lit(\"parameter\")],{})", Factory.Production);
  private static final IConstructor prod__Unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left = (IConstructor) _read("prod(label(\"Unequal\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\\\\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$RationalLiteral__char_class___range__48_48_char_class___range__114_114_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(48,48)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Field\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$TimeZonePart__lit_Z_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[lit(\"Z\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_default_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"default\")],{})", Factory.Production);
  private static final IConstructor prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(49,57)]),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(114,114)]),\\char-class([range(49,57)]),conditional(\\iter-star(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_layout_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"layout\")],{})", Factory.Production);
  private static final IConstructor prod__Default_$FunctionBody__lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Default\",sort(\"FunctionBody\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__GetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"GetAnnotation\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_variable__char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"variable\"),[\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(97,97)]),\\char-class([range(98,98)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_rule__char_class___range__114_114_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"rule\"),[\\char-class([range(114,114)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_61_61_62__char_class___range__60_60_char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\<==\\>\"),[\\char-class([range(60,60)]),\\char-class([range(61,61)]),\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__It_$Expression__lit_it_ = (IConstructor) _read("prod(label(\"It\",sort(\"Expression\")),[lit(\"it\")],{})", Factory.Production);
  private static final IConstructor prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[\\char-class([range(92,92)]),\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__First_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left = (IConstructor) _read("prod(label(\"First\",sort(\"Prod\")),[label(\"lhs\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\>\"),{\\not-follow(lit(\"\\>\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Prod\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Default_$Module__header_$Header_$layouts_LAYOUTLIST_body_$Body_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Module\")),[label(\"header\",sort(\"Header\")),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Body\"))],{})", Factory.Production);
  private static final IConstructor prod__Visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_ = (IConstructor) _read("prod(label(\"Visit\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),label(\"visit\",sort(\"Visit\"))],{})", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__0_8_range__11_12_range__14_31_range__33_65535 = (IConstructor) _read("regular(iter(\\char-class([range(0,8),range(11,12),range(14,31),range(33,65535)])))", Factory.Production);
  private static final IConstructor prod__NotIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"NotIn\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"notin\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Relation_$BasicType__lit_rel_ = (IConstructor) _read("prod(label(\"Relation\",sort(\"BasicType\")),[lit(\"rel\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_fail_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"fail\")],{})", Factory.Production);
  private static final IConstructor prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"str\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_constructor_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"constructor\")],{})", Factory.Production);
  private static final IConstructor prod__String_$Literal__stringLiteral_$StringLiteral_ = (IConstructor) _read("prod(label(\"String\",sort(\"Literal\")),[label(\"stringLiteral\",sort(\"StringLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_solve_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"solve\")],{})", Factory.Production);
  private static final IConstructor prod__Language_$SyntaxDefinition__start_$Start_$layouts_LAYOUTLIST_lit_syntax_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Language\",sort(\"SyntaxDefinition\")),[label(\"start\",sort(\"Start\")),layouts(\"LAYOUTLIST\"),lit(\"syntax\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Help_$ShellCommand__lit_help_ = (IConstructor) _read("prod(label(\"Help\",sort(\"ShellCommand\")),[lit(\"help\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])))", Factory.Production);
  private static final IConstructor prod__Anno_$Kind__lit_anno_ = (IConstructor) _read("prod(label(\"Anno\",sort(\"Kind\")),[lit(\"anno\")],{})", Factory.Production);
  private static final IConstructor prod__$Word__conditional__iter__char_class___range__0_8_range__11_12_range__14_31_range__33_65535__not_follow__char_class___range__0_8_range__11_12_range__14_31_range__33_65535_ = (IConstructor) _read("prod(lex(\"Word\"),[conditional(iter(\\char-class([range(0,8),range(11,12),range(14,31),range(33,65535)])),{\\not-follow(\\char-class([range(0,8),range(11,12),range(14,31),range(33,65535)]))})],{})", Factory.Production);
  private static final IConstructor prod__$NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"NonterminalLabel\"),[\\char-class([range(97,122)]),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"=\\>\"),[\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"==\"),[\\char-class([range(61,61)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit___62_61__char_class___range__62_62_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\>=\"),[\\char-class([range(62,62)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__$BooleanLiteral__lit_true_ = (IConstructor) _read("prod(lex(\"BooleanLiteral\"),[lit(\"true\")],{})", Factory.Production);
  private static final IConstructor prod__Is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"Is\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"is\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__Private_$Visibility__lit_private_ = (IConstructor) _read("prod(label(\"Private\",sort(\"Visibility\")),[lit(\"private\")],{})", Factory.Production);
  private static final IConstructor prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"non-assoc\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(45,45)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__Throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc = (IConstructor) _read("prod(label(\"Throw\",sort(\"Statement\")),[lit(\"throw\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__$LAYOUT__char_class___range__9_10_range__13_13_range__32_32_ = (IConstructor) _read("prod(lex(\"LAYOUT\"),[\\char-class([range(9,10),range(13,13),range(32,32)])],{})", Factory.Production);
  private static final IConstructor prod__Start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Start\",sort(\"Sym\")),[lit(\"start\"),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"nonterminal\",lex(\"Nonterminal\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),lit(\".\"),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"private\"),[\\char-class([range(112,112)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Mid_$ProtocolTail__mid_$MidProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_ = (IConstructor) _read("prod(label(\"Mid\",sort(\"ProtocolTail\")),[label(\"mid\",lex(\"MidProtocolChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"ProtocolTail\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_num_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"num\")],{})", Factory.Production);
  private static final IConstructor prod__Anti_$Pattern__lit___33_$layouts_LAYOUTLIST_pattern_$Pattern_ = (IConstructor) _read("prod(label(\"Anti\",sort(\"Pattern\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__Edit_$ShellCommand__lit_edit_$layouts_LAYOUTLIST_name_$QualifiedName_ = (IConstructor) _read("prod(label(\"Edit\",sort(\"ShellCommand\")),[lit(\"edit\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)])],{})", Factory.Production);
  private static final IConstructor prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"any\"),[\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"join\"),[\\char-class([range(106,106)]),\\char-class([range(111,111)]),\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_bracket_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bracket\")],{})", Factory.Production);
  private static final IConstructor prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_ = (IConstructor) _read("prod(lit(\"||\"),[\\char-class([range(124,124)]),\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__Bag_$BasicType__lit_bag_ = (IConstructor) _read("prod(label(\"Bag\",sort(\"BasicType\")),[lit(\"bag\")],{})", Factory.Production);
  private static final IConstructor prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\<=\"),[\\char-class([range(60,60)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"finally\"),[\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(108,108)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__$PostPathChars__lit___62_$URLChars_lit___124_ = (IConstructor) _read("prod(lex(\"PostPathChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"|\")],{})", Factory.Production);
  private static final IConstructor prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\<\\<\"),[\\char-class([range(60,60)]),\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__Associative_$Assoc__lit_assoc_ = (IConstructor) _read("prod(label(\"Associative\",sort(\"Assoc\")),[lit(\"assoc\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_repeat_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"repeat\")],{})", Factory.Production);
  private static final IConstructor prod__Empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Empty\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__StartOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_ = (IConstructor) _read("prod(label(\"StartOfLine\",sort(\"Sym\")),[lit(\"^\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star__$RegExp = (IConstructor) _read("regular(\\iter-star(lex(\"RegExp\")))", Factory.Production);
  private static final IConstructor prod__AsType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_ = (IConstructor) _read("prod(label(\"AsType\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"]\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_one_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"one\")],{})", Factory.Production);
  private static final IConstructor prod__Rational_$Literal__rationalLiteral_$RationalLiteral_ = (IConstructor) _read("prod(label(\"Rational\",sort(\"Literal\")),[label(\"rationalLiteral\",lex(\"RationalLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__RegExp_$Literal__regExpLiteral_$RegExpLiteral_ = (IConstructor) _read("prod(label(\"RegExp\",sort(\"Literal\")),[label(\"regExpLiteral\",lex(\"RegExpLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_node_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"node\")],{})", Factory.Production);
  private static final IConstructor prod__Sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Sequence\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sequence\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Map_$BasicType__lit_map_ = (IConstructor) _read("prod(label(\"Map\",sort(\"BasicType\")),[lit(\"map\")],{})", Factory.Production);
  private static final IConstructor prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"import\"),[\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"else\"),[\\char-class([range(101,101)]),\\char-class([range(108,108)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Intersection_$Assignment__lit___38_61_ = (IConstructor) _read("prod(label(\"Intersection\",sort(\"Assignment\")),[lit(\"&=\")],{})", Factory.Production);
  private static final IConstructor prod__lit_global__char_class___range__103_103_char_class___range__108_108_char_class___range__111_111_char_class___range__98_98_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"global\"),[\\char-class([range(103,103)]),\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(98,98)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__Enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"Enumerator\",sort(\"Expression\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"\\<-\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor regular__iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535 = (IConstructor) _read("regular(\\iter-star(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,65535)])})))", Factory.Production);
  private static final IConstructor prod__DoWhile_$StringTemplate__lit_do_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"DoWhile\",sort(\"StringTemplate\")),[lit(\"do\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Free_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"Free\",sort(\"TypeVar\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__VariableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_ = (IConstructor) _read("prod(label(\"VariableBecomes\",sort(\"Pattern\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__FunctionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration_ = (IConstructor) _read("prod(label(\"FunctionDeclaration\",sort(\"Statement\")),[label(\"functionDeclaration\",sort(\"FunctionDeclaration\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__$BasicType_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[sort(\"BasicType\")],{})", Factory.Production);
  private static final IConstructor prod__User_$Type__conditional__user_$UserType__delete__$HeaderKeyword_ = (IConstructor) _read("prod(label(\"User\",sort(\"Type\")),[conditional(label(\"user\",sort(\"UserType\")),{delete(keywords(\"HeaderKeyword\"))})],{})", Factory.Production);
  private static final IConstructor prod__StepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"StepRange\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"second\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"..\"),layouts(\"LAYOUTLIST\"),label(\"last\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__$PostProtocolChars__lit___62_$URLChars_lit___58_47_47_ = (IConstructor) _read("prod(lex(\"PostProtocolChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"://\")],{})", Factory.Production);
  private static final IConstructor prod__Location_$Literal__locationLiteral_$LocationLiteral_ = (IConstructor) _read("prod(label(\"Location\",sort(\"Literal\")),[label(\"locationLiteral\",sort(\"LocationLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__Unlabeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Unlabeled\",sort(\"Prod\")),[label(\"modifiers\",\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"args\",\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Innermost_$Strategy__lit_innermost_ = (IConstructor) _read("prod(label(\"Innermost\",sort(\"Strategy\")),[lit(\"innermost\")],{})", Factory.Production);
  private static final IConstructor prod__Column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_ = (IConstructor) _read("prod(label(\"Column\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"column\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__Default_$Label__name_$Name_$layouts_LAYOUTLIST_lit___58_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Label\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\":\"),\\iter-star(lex(\"NamedRegExp\")),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_loc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"loc\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Word__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(lex(\"Word\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star__$NamedRegExp = (IConstructor) _read("regular(\\iter-star(lex(\"NamedRegExp\")))", Factory.Production);
  private static final IConstructor prod__ReifiedConstructor_$BasicType__lit_constructor_ = (IConstructor) _read("prod(label(\"ReifiedConstructor\",sort(\"BasicType\")),[lit(\"constructor\")],{})", Factory.Production);
  private static final IConstructor prod__Tag_$ProdModifier__tag_$Tag_ = (IConstructor) _read("prod(label(\"Tag\",sort(\"ProdModifier\")),[label(\"tag\",sort(\"Tag\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"keyword\"),[\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(121,121)]),\\char-class([range(119,119)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_ = (IConstructor) _read("prod(lit(\"syntax\"),[\\char-class([range(115,115)]),\\char-class([range(121,121)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(120,120)])],{})", Factory.Production);
  private static final IConstructor prod__AsType_$Pattern__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Pattern_ = (IConstructor) _read("prod(label(\"AsType\",sort(\"Pattern\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"]\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"all\"),[\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_map_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"map\")],{})", Factory.Production);
  private static final IConstructor prod__Data_$Kind__lit_data_ = (IConstructor) _read("prod(label(\"Data\",sort(\"Kind\")),[lit(\"data\")],{})", Factory.Production);
  private static final IConstructor prod__Composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Composition\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"o\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\>\\>\"),[\\char-class([range(62,62)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__Symbol_$Type__symbol_$Sym_ = (IConstructor) _read("prod(label(\"Symbol\",sort(\"Type\")),[label(\"symbol\",sort(\"Sym\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_private_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"private\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_module_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"module\")],{})", Factory.Production);
  private static final IConstructor prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"try\"),[\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__lit___63_61__char_class___range__63_63_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"?=\"),[\\char-class([range(63,63)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__NonInterpolated_$PathPart__pathChars_$PathChars_ = (IConstructor) _read("prod(label(\"NonInterpolated\",sort(\"PathPart\")),[label(\"pathChars\",lex(\"PathChars\"))],{})", Factory.Production);
  private static final IConstructor prod__Default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Foldable_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"Default\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"contents\",lex(\"TagString\"))],{tag(Foldable()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_throw_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"throw\")],{})", Factory.Production);
  private static final IConstructor prod__lit_bottom_up__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"bottom-up\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(45,45)]),\\char-class([range(117,117)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"break\"),[\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__$layouts_LAYOUTLIST__conditional__iter_star__$LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_ = (IConstructor) _read("prod(layouts(\"LAYOUTLIST\"),[conditional(\\iter-star(lex(\"LAYOUT\")),{\\not-follow(\\char-class([range(9,10),range(13,13),range(32,32)])),\\not-follow(lit(\"//\")),\\not-follow(lit(\"/*\"))})],{})", Factory.Production);
  private static final IConstructor prod__Java_$FunctionModifier__lit_java_ = (IConstructor) _read("prod(label(\"Java\",sort(\"FunctionModifier\")),[lit(\"java\")],{})", Factory.Production);
  private static final IConstructor prod__IfDefined_$Assignment__lit___63_61_ = (IConstructor) _read("prod(label(\"IfDefined\",sort(\"Assignment\")),[lit(\"?=\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Range__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Range\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$MidPathChars__lit___62_$URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"MidPathChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"true\"),[\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Sym__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_rel_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"rel\")],{})", Factory.Production);
  private static final IConstructor prod__List_$FunctionModifiers__modifiers_iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"List\",sort(\"FunctionModifiers\")),[label(\"modifiers\",\\iter-star-seps(sort(\"FunctionModifier\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"extend\"),[\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"append\"),[\\char-class([range(97,97)]),\\char-class([range(112,112)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),conditional(lit(\".\"),{\\not-follow(lit(\".\"))}),\\iter-star(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__lit_constructor__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"constructor\"),[\\char-class([range(99,99)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(117,117)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__Selector_$Type__selector_$DataTypeSelector_ = (IConstructor) _read("prod(label(\"Selector\",sort(\"Type\")),[label(\"selector\",sort(\"DataTypeSelector\"))],{})", Factory.Production);
  private static final IConstructor prod__BottomUp_$Strategy__lit_bottom_up_ = (IConstructor) _read("prod(label(\"BottomUp\",sort(\"Strategy\")),[lit(\"bottom-up\")],{})", Factory.Production);
  private static final IConstructor prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"assoc\"),[\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__All_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left = (IConstructor) _read("prod(label(\"All\",sort(\"Prod\")),[label(\"lhs\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Prod\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Map\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"mappings\",\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Expression\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_ = (IConstructor) _read("prod(label(\"Template\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringMiddle\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_bool_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bool\")],{})", Factory.Production);
  private static final IConstructor prod__$OctalEscapeSequence__lit___92_char_class___range__48_51_char_class___range__48_55_char_class___range__48_55_ = (IConstructor) _read("prod(lex(\"OctalEscapeSequence\"),[lit(\"\\\\\"),\\char-class([range(48,51)]),\\char-class([range(48,55)]),\\char-class([range(48,55)])],{})", Factory.Production);
  private static final IConstructor prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"map\"),[\\char-class([range(109,109)]),\\char-class([range(97,97)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__Named_$TypeArg__type_$Type_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"Named\",sort(\"TypeArg\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"real\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)])))", Factory.Production);
  private static final IConstructor prod__Default_$Assignment__lit___61_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Assignment\")),[lit(\"=\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_datetime_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"datetime\")],{})", Factory.Production);
  private static final IConstructor prod__Character_$Range__character_$Char_ = (IConstructor) _read("prod(label(\"Character\",sort(\"Range\")),[label(\"character\",lex(\"Char\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"void\"),[\\char-class([range(118,118)]),\\char-class([range(111,111)]),\\char-class([range(105,105)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__Replacing_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_replacement_$Replacement_ = (IConstructor) _read("prod(label(\"Replacing\",sort(\"PatternWithAction\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"=\\>\"),layouts(\"LAYOUTLIST\"),label(\"replacement\",sort(\"Replacement\"))],{})", Factory.Production);
  private static final IConstructor prod__ReifiedAdt_$BasicType__lit_adt_ = (IConstructor) _read("prod(label(\"ReifiedAdt\",sort(\"BasicType\")),[lit(\"adt\")],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[conditional(lit(\".\"),{\\not-precede(\\char-class([range(46,46)]))}),iter(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_set_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"set\")],{})", Factory.Production);
  private static final IConstructor prod__Union_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Class__assoc__left = (IConstructor) _read("prod(label(\"Union\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"||\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10__tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(lex(\"Comment\"),[lit(\"//\"),\\iter-star(\\char-class([range(0,9),range(11,65535)])),\\char-class([range(10,10)])],{tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__AssociativityGroup_$Prod__associativity_$Assoc_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_group_$Prod_$layouts_LAYOUTLIST_lit___41__tag__Foldable = (IConstructor) _read("prod(label(\"AssociativityGroup\",sort(\"Prod\")),[label(\"associativity\",sort(\"Assoc\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"group\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_return_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"return\")],{})", Factory.Production);
  private static final IConstructor prod__Selector_$DataTypeSelector__sort_$QualifiedName_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_production_$Name_ = (IConstructor) _read("prod(label(\"Selector\",sort(\"DataTypeSelector\")),[label(\"sort\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"production\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__empty__ = (IConstructor) _read("prod(empty(),[],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_str_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"str\")],{})", Factory.Production);
  private static final IConstructor prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_ = (IConstructor) _read("prod(lit(\"switch\"),[\\char-class([range(115,115)]),\\char-class([range(119,119)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(99,99)]),\\char-class([range(104,104)])],{})", Factory.Production);
  private static final IConstructor prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_65535_ = (IConstructor) _read("prod(lex(\"URLChars\"),[\\iter-star(\\char-class([range(0,8),range(11,12),range(14,31),range(33,59),range(61,123),range(125,65535)]))],{})", Factory.Production);
  private static final IConstructor prod__NonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"NonEmptyBlock\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"Bracket\",sort(\"Class\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"charclass\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__lit_fun__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"fun\"),[\\char-class([range(102,102)]),\\char-class([range(117,117)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"solve\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(108,108)]),\\char-class([range(118,118)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__IterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"IterStarSep\",sort(\"Sym\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sep\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"layout\"),[\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(121,121)]),\\char-class([range(111,111)]),\\char-class([range(117,117)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))", Factory.Production);
  private static final IConstructor prod__Function_$Kind__lit_function_ = (IConstructor) _read("prod(label(\"Function\",sort(\"Kind\")),[lit(\"function\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_extend_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"extend\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Catch__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Arbitrary_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement_ = (IConstructor) _read("prod(label(\"Arbitrary\",sort(\"PatternWithAction\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__List_$Comprehension__lit___91_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"List\",sort(\"Comprehension\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"results\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_non_terminal__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"non-terminal\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(45,45)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(109,109)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_tag_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"tag\")],{})", Factory.Production);
  private static final IConstructor prod__Fail_$Statement__lit_fail_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Fail\",sort(\"Statement\")),[lit(\"fail\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__$StringCharacter = (IConstructor) _read("regular(\\iter-star(lex(\"StringCharacter\")))", Factory.Production);
  private static final IConstructor prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[\\char-class([range(10,10)]),\\iter-star(\\char-class([range(9,9),range(32,32)])),\\char-class([range(39,39)])],{})", Factory.Production);
  private static final IConstructor prod__Rule_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_lit_rule_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_patternAction_$PatternWithAction_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Rule\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),lit(\"rule\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"patternAction\",sort(\"PatternWithAction\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__And_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"And\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"&&\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_keyword_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"keyword\")],{})", Factory.Production);
  private static final IConstructor prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_opt__$TimeZonePart_ = (IConstructor) _read("prod(lex(\"DateAndTime\"),[lit(\"$\"),lex(\"DatePart\"),lit(\"T\"),lex(\"TimePartNoTZ\"),opt(lex(\"TimeZonePart\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_list_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"list\")],{})", Factory.Production);
  private static final IConstructor prod__ListModules_$ShellCommand__lit_modules_ = (IConstructor) _read("prod(label(\"ListModules\",sort(\"ShellCommand\")),[lit(\"modules\")],{})", Factory.Production);
  private static final IConstructor prod__Has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"Has\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"has\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_public_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"public\")],{})", Factory.Production);
  private static final IConstructor prod__Initialized_$Variable__name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_initial_$Expression_ = (IConstructor) _read("prod(label(\"Initialized\",sort(\"Variable\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"initial\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__Break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Break\",sort(\"Statement\")),[lit(\"break\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Variable_$Kind__lit_variable_ = (IConstructor) _read("prod(label(\"Variable\",sort(\"Kind\")),[lit(\"variable\")],{})", Factory.Production);
  private static final IConstructor prod__$layouts_$default$__ = (IConstructor) _read("prod(layouts(\"$default$\"),[],{})", Factory.Production);
  private static final IConstructor prod__lit___60_58__char_class___range__60_60_char_class___range__58_58_ = (IConstructor) _read("prod(lit(\"\\<:\"),[\\char-class([range(60,60)]),\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"assert\"),[\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left = (IConstructor) _read("prod(label(\"Follow\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Default_$Mapping__$Pattern__from_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Pattern_ = (IConstructor) _read("prod(label(\"Default\",\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")])),[label(\"from\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__Interpolated_$PathPart__pre_$PrePathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_ = (IConstructor) _read("prod(label(\"Interpolated\",sort(\"PathPart\")),[label(\"pre\",lex(\"PrePathChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"PathTail\"))],{})", Factory.Production);
  private static final IConstructor prod__Empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Foldable_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"Empty\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{tag(Foldable()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__Annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_ = (IConstructor) _read("prod(label(\"Annotation\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"annotation\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"lexical\"),[\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(105,105)]),\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_45__char_class___range__60_60_char_class___range__45_45_ = (IConstructor) _read("prod(lit(\"\\<-\"),[\\char-class([range(60,60)]),\\char-class([range(45,45)])],{})", Factory.Production);
  private static final IConstructor prod__GivenStrategy_$Visit__strategy_$Strategy_$layouts_LAYOUTLIST_lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"GivenStrategy\",sort(\"Visit\")),[label(\"strategy\",sort(\"Strategy\")),layouts(\"LAYOUTLIST\"),lit(\"visit\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"subject\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__$OctalEscapeSequence__lit___92_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_ = (IConstructor) _read("prod(lex(\"OctalEscapeSequence\"),[lit(\"\\\\\"),conditional(\\char-class([range(48,55)]),{\\not-follow(\\char-class([range(48,55)]))})],{})", Factory.Production);
  private static final IConstructor prod__HexIntegerLiteral_$IntegerLiteral__hex_$HexIntegerLiteral_ = (IConstructor) _read("prod(label(\"HexIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"hex\",lex(\"HexIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"filter\"),[\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__TypedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_ = (IConstructor) _read("prod(label(\"TypedVariableBecomes\",sort(\"Pattern\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__Assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Assert\",sort(\"Statement\")),[lit(\"assert\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimePartNoTZ\"),[\\char-class([range(48,50)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))],{})", Factory.Production);
  private static final IConstructor prod__IterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_ = (IConstructor) _read("prod(label(\"IterSep\",sort(\"Sym\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sep\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"+\")],{})", Factory.Production);
  private static final IConstructor prod__$Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"Backslash\"),[conditional(\\char-class([range(92,92)]),{\\not-follow(\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)]))})],{})", Factory.Production);
  private static final IConstructor prod__lit___58_58__char_class___range__58_58_char_class___range__58_58_ = (IConstructor) _read("prod(lit(\"::\"),[\\char-class([range(58,58)]),\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"start\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58_61__char_class___range__58_58_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\":=\"),[\\char-class([range(58,58)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__FieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_ = (IConstructor) _read("prod(label(\"FieldAccess\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"field\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__Bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"Bracket\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_throws_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"throws\")],{})", Factory.Production);
  private static final IConstructor prod__Conditional_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Conditional\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"when\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc = (IConstructor) _read("prod(label(\"Return\",sort(\"Statement\")),[lit(\"return\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Bracket_$ProdModifier__lit_bracket_ = (IConstructor) _read("prod(label(\"Bracket\",sort(\"ProdModifier\")),[lit(\"bracket\")],{})", Factory.Production);
  private static final IConstructor prod__NonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"NonEmptyBlock\",sort(\"Expression\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__GreaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"GreaterThanOrEq\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor regular__iter_star__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535 = (IConstructor) _read("regular(\\iter-star(alt({seq([\\char-class([range(92,92)]),\\char-class([range(125,125)])]),\\char-class([range(0,124),range(126,65535)])})))", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_switch_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"switch\")],{})", Factory.Production);
  private static final IConstructor prod__Conditional_$Replacement__replacementExpression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Conditional\",sort(\"Replacement\")),[label(\"replacementExpression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"when\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"DatePart\"),[\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),lit(\"-\"),\\char-class([range(48,49)]),\\char-class([range(48,57)]),lit(\"-\"),\\char-class([range(48,51)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__Name_$UserType__name_$QualifiedName_ = (IConstructor) _read("prod(label(\"Name\",sort(\"UserType\")),[label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__Tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"Expression\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__$PostStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"PostStringChars\"),[\\char-class([range(62,62)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(34,34)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Expression_$Command__expression_$Expression_ = (IConstructor) _read("prod(label(\"Expression\",sort(\"Command\")),[label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__$UnicodeEscape__lit___92_iter__char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(lex(\"UnicodeEscape\"),[lit(\"\\\\\"),iter(\\char-class([range(117,117)])),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"for\"),[\\char-class([range(102,102)]),\\char-class([range(111,111)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__Test_$FunctionModifier__lit_test_ = (IConstructor) _read("prod(label(\"Test\",sort(\"FunctionModifier\")),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__UnInitialized_$Variable__name_$Name_ = (IConstructor) _read("prod(label(\"UnInitialized\",sort(\"Variable\")),[label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__Insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc = (IConstructor) _read("prod(label(\"Insert\",sort(\"Statement\")),[lit(\"insert\"),layouts(\"LAYOUTLIST\"),label(\"dataTarget\",sort(\"DataTarget\")),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lit_left__char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"left\"),[\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Command__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Command\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__IterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"IterStar\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimePartNoTZ\"),[\\char-class([range(48,50)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))],{})", Factory.Production);
  private static final IConstructor regular__empty = (IConstructor) _read("regular(empty())", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_9_range__11_65535 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,9),range(11,65535)])))", Factory.Production);
  private static final IConstructor prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"bracket\"),[\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(99,99)]),\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_65535 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,8),range(11,12),range(14,31),range(33,59),range(61,123),range(125,65535)])))", Factory.Production);
  private static final IConstructor prod__CharacterClass_$Sym__charClass_$Class_ = (IConstructor) _read("prod(label(\"CharacterClass\",sort(\"Sym\")),[label(\"charClass\",sort(\"Class\"))],{})", Factory.Production);
  private static final IConstructor prod__ReifiedNonTerminal_$BasicType__lit_non_terminal_ = (IConstructor) _read("prod(label(\"ReifiedNonTerminal\",sort(\"BasicType\")),[lit(\"non-terminal\")],{})", Factory.Production);
  private static final IConstructor prod__List_$BasicType__lit_list_ = (IConstructor) _read("prod(label(\"List\",sort(\"BasicType\")),[lit(\"list\")],{})", Factory.Production);
  private static final IConstructor prod__Map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Map\",sort(\"Pattern\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"mappings\",\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Product\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Division\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"/\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Post_$StringTail__post_$PostStringChars_ = (IConstructor) _read("prod(label(\"Post\",sort(\"StringTail\")),[label(\"post\",lex(\"PostStringChars\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_for_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"for\")],{})", Factory.Production);
  private static final IConstructor prod__ActualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_ = (IConstructor) _read("prod(label(\"ActualsRenaming\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"actuals\",sort(\"ModuleActuals\")),layouts(\"LAYOUTLIST\"),label(\"renamings\",sort(\"Renamings\"))],{})", Factory.Production);
  private static final IConstructor prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_65535_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[\\char-class([range(0,33),range(35,38),range(40,59),range(61,61),range(63,91),range(93,65535)])],{})", Factory.Production);
  private static final IConstructor prod__ReifiedType_$Pattern__basicType_$BasicType_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"ReifiedType\",sort(\"Pattern\")),[label(\"basicType\",sort(\"BasicType\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_fun_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"fun\")],{})", Factory.Production);
  private static final IConstructor prod__ReifiedType_$BasicType__lit_type_ = (IConstructor) _read("prod(label(\"ReifiedType\",sort(\"BasicType\")),[lit(\"type\")],{})", Factory.Production);
  private static final IConstructor prod__$StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[lit(\"\\\\\"),\\char-class([range(34,34),range(39,39),range(60,60),range(62,62),range(92,92),range(98,98),range(102,102),range(110,110),range(114,114),range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__OctalIntegerLiteral_$IntegerLiteral__octal_$OctalIntegerLiteral_ = (IConstructor) _read("prod(label(\"OctalIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"octal\",lex(\"OctalIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__EmptyStatement_$Statement__lit___59_ = (IConstructor) _read("prod(label(\"EmptyStatement\",sort(\"Statement\")),[lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_else_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"else\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_in_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"in\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_it_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"it\")],{})", Factory.Production);
  private static final IConstructor prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"insert\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_if_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"if\")],{})", Factory.Production);
  private static final IConstructor prod__lit_history__char_class___range__104_104_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"history\"),[\\char-class([range(104,104)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__SimpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"SimpleCharclass\",sort(\"Class\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"ranges\",\\iter-star-seps(sort(\"Range\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__FieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"FieldProject\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"fields\",\\iter-seps(sort(\"Field\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__$Char__$UnicodeEscape__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[lex(\"UnicodeEscape\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__IfThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else_ = (IConstructor) _read("prod(label(\"IfThen\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"thenStatement\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(lit(\"else\"))})],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_type_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"type\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Expression\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Empty_$Bound__ = (IConstructor) _read("prod(label(\"Empty\",sort(\"Bound\")),[],{})", Factory.Production);
  private static final IConstructor prod__lit_undeclare__char_class___range__117_117_char_class___range__110_110_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"undeclare\"),[\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_ = (IConstructor) _read("prod(label(\"Unimport\",sort(\"ShellCommand\")),[lit(\"unimport\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))])))", Factory.Production);
  private static final IConstructor prod__FromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_ = (IConstructor) _read("prod(label(\"FromTo\",sort(\"Range\")),[label(\"start\",lex(\"Char\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"end\",lex(\"Char\"))],{})", Factory.Production);
  private static final IConstructor prod__IfThen_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"IfThen\",sort(\"StringTemplate\")),[lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Bounded_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___60_58_$layouts_LAYOUTLIST_bound_$Type_ = (IConstructor) _read("prod(label(\"Bounded\",sort(\"TypeVar\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"\\<:\"),layouts(\"LAYOUTLIST\"),label(\"bound\",sort(\"Type\"))],{})", Factory.Production);
  private static final IConstructor prod__IfThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__left = (IConstructor) _read("prod(label(\"IfThenElse\",sort(\"Expression\")),[label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"thenExp\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"elseExp\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Set_$Comprehension__lit___123_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Set\",sort(\"Comprehension\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"results\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Default_$Formals__formals_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Formals\")),[label(\"formals\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"Bracket\",sort(\"Type\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__All_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"All\",sort(\"Expression\")),[lit(\"all\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Parametrized_$Sym__conditional__nonterminal_$Nonterminal__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Parametrized\",sort(\"Sym\")),[conditional(label(\"nonterminal\",lex(\"Nonterminal\")),{follow(lit(\"[\"))}),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__$Char__$OctalEscapeSequence__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[lex(\"OctalEscapeSequence\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"rel\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__Annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Annotation\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"anno\"),layouts(\"LAYOUTLIST\"),label(\"annoType\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"onType\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Lexical_$SyntaxDefinition__lit_lexical_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Lexical\",sort(\"SyntaxDefinition\")),[lit(\"lexical\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Layout_$SyntaxDefinition__vis_$Visibility_$layouts_LAYOUTLIST_lit_layout_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Layout\",sort(\"SyntaxDefinition\")),[label(\"vis\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"layout\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__lit___61_61_62__char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"==\\>\"),[\\char-class([range(61,61)]),\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__SetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"SetAnnotation\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"value\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__Set_$Pattern__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Set\",sort(\"Pattern\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__For_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"For\",sort(\"StringTemplate\")),[lit(\"for\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__NonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"NonEquals\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"!=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Case__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$PrePathChars__$URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"PrePathChars\"),[lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__Nonterminal_$Sym__conditional__nonterminal_$Nonterminal__not_follow__lit___91_ = (IConstructor) _read("prod(label(\"Nonterminal\",sort(\"Sym\")),[conditional(label(\"nonterminal\",lex(\"Nonterminal\")),{\\not-follow(lit(\"[\"))})],{})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_syntax_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"syntax\")],{})", Factory.Production);
  private static final IConstructor prod__TypeArguments_$FunctionType__type_$Type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"TypeArguments\",sort(\"FunctionType\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"continue\"),[\\char-class([range(99,99)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Reference_$Prod__lit___58_$layouts_LAYOUTLIST_referenced_$Name_ = (IConstructor) _read("prod(label(\"Reference\",sort(\"Prod\")),[lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"referenced\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_adt__char_class___range__97_97_char_class___range__100_100_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"adt\"),[\\char-class([range(97,97)]),\\char-class([range(100,100)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__$LAYOUT__$Comment_ = (IConstructor) _read("prod(lex(\"LAYOUT\"),[lex(\"Comment\")],{})", Factory.Production);
  private static final IConstructor prod__$Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535_lit___42_47__tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(lex(\"Comment\"),[lit(\"/*\"),\\iter-star(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,65535)])})),lit(\"*/\")],{tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_tuple_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"tuple\")],{})", Factory.Production);
  private static final IConstructor prod__Empty_$DataTarget__ = (IConstructor) _read("prod(label(\"Empty\",sort(\"DataTarget\")),[],{})", Factory.Production);
  private static final IConstructor prod__Addition_$Assignment__lit___43_61_ = (IConstructor) _read("prod(label(\"Addition\",sort(\"Assignment\")),[lit(\"+=\")],{})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_lexical_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"lexical\")],{})", Factory.Production);
  private static final IConstructor prod__MultiVariable_$Pattern__qualifiedName_$QualifiedName_$layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"MultiVariable\",sort(\"Pattern\")),[label(\"qualifiedName\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__Set_$BasicType__lit_set_ = (IConstructor) _read("prod(label(\"Set\",sort(\"BasicType\")),[lit(\"set\")],{})", Factory.Production);
  private static final IConstructor prod__Default_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Header\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),lit(\"module\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"imports\",\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"dynamic\"),[\\char-class([range(100,100)]),\\char-class([range(121,121)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(105,105)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__Default_$Visibility__ = (IConstructor) _read("prod(label(\"Default\",sort(\"Visibility\")),[],{})", Factory.Production);
  private static final IConstructor prod__List_$Pattern__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"List\",sort(\"Pattern\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_false_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"false\")],{})", Factory.Production);
  private static final IConstructor prod__Try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc = (IConstructor) _read("prod(label(\"Try\",sort(\"Statement\")),[lit(\"try\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),label(\"handlers\",\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")]))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Subscript_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscript_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Subscript\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"subscript\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Literal_$Pattern__literal_$Literal_ = (IConstructor) _read("prod(label(\"Literal\",sort(\"Pattern\")),[label(\"literal\",sort(\"Literal\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"list\"),[\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Right_$Assoc__lit_right_ = (IConstructor) _read("prod(label(\"Right\",sort(\"Assoc\")),[lit(\"right\")],{})", Factory.Production);
  private static final IConstructor prod__$DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"DecimalIntegerLiteral\"),[\\char-class([range(49,57)]),conditional(\\iter-star(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__Tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Tag\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"tag\"),layouts(\"LAYOUTLIST\"),label(\"kind\",sort(\"Kind\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"on\"),layouts(\"LAYOUTLIST\"),label(\"types\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"test\"),[\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Any\",sort(\"Expression\")),[lit(\"any\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Empty_$Target__ = (IConstructor) _read("prod(label(\"Empty\",sort(\"Target\")),[],{})", Factory.Production);
  private static final IConstructor prod__Post_$PathTail__post_$PostPathChars_ = (IConstructor) _read("prod(label(\"Post\",sort(\"PathTail\")),[label(\"post\",lex(\"PostPathChars\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"false\"),[\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Default_$Import__lit_import_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Import\")),[lit(\"import\"),layouts(\"LAYOUTLIST\"),label(\"module\",sort(\"ImportedModule\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__$NamedBackslash__conditional__char_class___range__92_92__not_follow__char_class___range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"NamedBackslash\"),[conditional(\\char-class([range(92,92)]),{\\not-follow(\\char-class([range(60,60),range(62,62),range(92,92)]))})],{})", Factory.Production);
  private static final IConstructor prod__Interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_ = (IConstructor) _read("prod(label(\"Interpolated\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringMiddle\"))],{})", Factory.Production);
  private static final IConstructor prod__Comprehension_$Expression__comprehension_$Comprehension_ = (IConstructor) _read("prod(label(\"Comprehension\",sort(\"Expression\")),[label(\"comprehension\",sort(\"Comprehension\"))],{})", Factory.Production);
  private static final IConstructor prod__start__$Commands__$layouts_LAYOUTLIST_top_$Commands_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Commands\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Commands\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"set\"),[\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_non_assoc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"non-assoc\")],{})", Factory.Production);
  private static final IConstructor prod__lit_bottom_up_break__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"bottom-up-break\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(45,45)]),\\char-class([range(117,117)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__Keyword_$SyntaxDefinition__lit_keyword_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Keyword\",sort(\"SyntaxDefinition\")),[lit(\"keyword\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__GreaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"GreaterThan\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Subtraction\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Present_$Start__lit_start_ = (IConstructor) _read("prod(label(\"Present\",sort(\"Start\")),[lit(\"start\")],{})", Factory.Production);
  private static final IConstructor prod__Closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Closure\",sort(\"Expression\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__NotPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right = (IConstructor) _read("prod(label(\"NotPrecede\",sort(\"Sym\")),[label(\"match\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\\<\\<\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"throws\"),[\\char-class([range(116,116)]),\\char-class([range(104,104)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"bag\"),[\\char-class([range(98,98)]),\\char-class([range(97,97)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"visit\"),[\\char-class([range(118,118)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__In_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"In\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"in\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_ = (IConstructor) _read("prod(label(\"Complement\",sort(\"Class\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"charClass\",sort(\"Class\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_value_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"value\")],{})", Factory.Production);
  private static final IConstructor prod__GivenVisibility_$Toplevel__declaration_$Declaration_ = (IConstructor) _read("prod(label(\"GivenVisibility\",sort(\"Toplevel\")),[label(\"declaration\",sort(\"Declaration\"))],{})", Factory.Production);
  private static final IConstructor prod__Intersection_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Class__assoc__left = (IConstructor) _read("prod(label(\"Intersection\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"&&\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_extend_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"extend\")],{})", Factory.Production);
  private static final IConstructor prod__While_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"While\",sort(\"StringTemplate\")),[lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__NonInterpolated_$ProtocolPart__protocolChars_$ProtocolChars_ = (IConstructor) _read("prod(label(\"NonInterpolated\",sort(\"ProtocolPart\")),[label(\"protocolChars\",lex(\"ProtocolChars\"))],{})", Factory.Production);
  private static final IConstructor prod__Associativity_$ProdModifier__associativity_$Assoc_ = (IConstructor) _read("prod(label(\"Associativity\",sort(\"ProdModifier\")),[label(\"associativity\",sort(\"Assoc\"))],{})", Factory.Production);
  private static final IConstructor prod__DataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"DataAbstract\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"data\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__LessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"LessThanOrEq\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\<=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__String_$BasicType__lit_str_ = (IConstructor) _read("prod(label(\"String\",sort(\"BasicType\")),[lit(\"str\")],{})", Factory.Production);
  private static final IConstructor prod__Negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_ = (IConstructor) _read("prod(label(\"Negation\",sort(\"Expression\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__WithThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit_throws_$layouts_LAYOUTLIST_exceptions_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"WithThrows\",sort(\"Signature\")),[label(\"modifiers\",sort(\"FunctionModifiers\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"throws\"),layouts(\"LAYOUTLIST\"),label(\"exceptions\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__SetOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_ = (IConstructor) _read("prod(label(\"SetOption\",sort(\"ShellCommand\")),[lit(\"set\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__$OctalEscapeSequence__lit___92_char_class___range__48_55_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_ = (IConstructor) _read("prod(lex(\"OctalEscapeSequence\"),[lit(\"\\\\\"),\\char-class([range(48,55)]),conditional(\\char-class([range(48,55)]),{\\not-follow(\\char-class([range(48,55)]))})],{})", Factory.Production);
  private static final IConstructor prod__Index_$Field__fieldIndex_$IntegerLiteral_ = (IConstructor) _read("prod(label(\"Index\",sort(\"Field\")),[label(\"fieldIndex\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"public\"),[\\char-class([range(112,112)]),\\char-class([range(117,117)]),\\char-class([range(98,98)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__For_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_ = (IConstructor) _read("prod(label(\"For\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"for\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[conditional(lit(\".\"),{\\not-precede(\\char-class([range(46,46)]))}),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__$Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"Name\"),[\\char-class([range(92,92)]),\\char-class([range(65,90),range(95,95),range(97,122)]),conditional(\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__lit_renaming__char_class___range__114_114_char_class___range__101_101_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"renaming\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_alias_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"alias\")],{})", Factory.Production);
  private static final IConstructor prod__QualifiedName_$Expression__qualifiedName_$QualifiedName_ = (IConstructor) _read("prod(label(\"QualifiedName\",sort(\"Expression\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_rule_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"rule\")],{})", Factory.Production);
  private static final IConstructor prod__IsDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_ = (IConstructor) _read("prod(label(\"IsDefined\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\")],{})", Factory.Production);
  private static final IConstructor prod__Abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Abstract\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__$HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_conditional__iter__char_class___range__48_57_range__65_70_range__97_102__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"HexIntegerLiteral\"),[\\char-class([range(48,48)]),\\char-class([range(88,88),range(120,120)]),conditional(iter(\\char-class([range(48,57),range(65,70),range(97,102)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__$PathChars__$URLChars_char_class___range__124_124_ = (IConstructor) _read("prod(lex(\"PathChars\"),[lex(\"URLChars\"),\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"value\"),[\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Value_$BasicType__lit_value_ = (IConstructor) _read("prod(label(\"Value\",sort(\"BasicType\")),[lit(\"value\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_int_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"int\")],{})", Factory.Production);
  private static final IConstructor prod__start__$Module__$layouts_LAYOUTLIST_top_$Module_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Module\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Module\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"module\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
    
  // Item declarations
	
	
  protected static class $Tag {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Foldable_tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-8, 4, "$TagString", null, null);
      tmp[3] = new NonTerminalStackNode(-7, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-6, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-5, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-4, 0, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Foldable_tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__Expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Foldable_tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-15, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-14, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-13, 4, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode(-12, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-11, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-10, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-9, 0, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      builder.addAlternative(RascalRascal.prod__Expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Foldable_tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__Empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Foldable_tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-18, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-17, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-16, 0, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      builder.addAlternative(RascalRascal.prod__Empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Foldable_tag__category___67_111_109_109_101_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Foldable_tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__Expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Foldable_tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__Empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Foldable_tag__category___67_111_109_109_101_110_116(builder);
      
    }
  }
	
  protected static class $DateAndTime {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_opt__$TimeZonePart_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new OptionalStackNode(-29, 4, regular__opt__$TimeZonePart, new NonTerminalStackNode(-30, 0, "$TimeZonePart", null, null), null, null);
      tmp[3] = new NonTerminalStackNode(-28, 3, "$TimePartNoTZ", null, null);
      tmp[2] = new LiteralStackNode(-27, 2, prod__lit_T__char_class___range__84_84_, new char[] {84}, null, null);
      tmp[1] = new NonTerminalStackNode(-26, 1, "$DatePart", null, null);
      tmp[0] = new LiteralStackNode(-25, 0, prod__lit___36__char_class___range__36_36_, new char[] {36}, null, null);
      builder.addAlternative(RascalRascal.prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_opt__$TimeZonePart_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_opt__$TimeZonePart_(builder);
      
    }
  }
	
  protected static class $Pattern {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__MultiVariable_$Pattern__qualifiedName_$QualifiedName_$layouts_LAYOUTLIST_lit___42_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-36, 2, prod__lit___42__char_class___range__42_42_, new char[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode(-35, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-34, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__MultiVariable_$Pattern__qualifiedName_$QualifiedName_$layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__Descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-102, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode(-101, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-100, 0, prod__lit___47__char_class___range__47_47_, new char[] {47}, null, null);
      builder.addAlternative(RascalRascal.prod__Descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    protected static final void _init_prod__TypedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-109, 6, "$Pattern", null, null);
      tmp[5] = new NonTerminalStackNode(-108, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-107, 4, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(-106, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-105, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-104, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-103, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__TypedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    protected static final void _init_prod__TypedVariable_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-39, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-38, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-37, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__TypedVariable_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__AsType_$Pattern__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-116, 6, "$Pattern", null, null);
      tmp[5] = new NonTerminalStackNode(-115, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-114, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-113, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-112, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-111, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-110, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__AsType_$Pattern__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Pattern_, tmp);
	}
    protected static final void _init_prod__List_$Pattern__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-48, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-47, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-42, 2, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-43, 0, "$Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-44, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-45, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-46, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-41, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-40, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__List_$Pattern__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__ReifiedType_$Pattern__basicType_$BasicType_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-59, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-58, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-53, 4, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-54, 0, "$Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-55, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-56, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-57, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-52, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-51, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-50, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-49, 0, "$BasicType", null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedType_$Pattern__basicType_$BasicType_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__VariableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-121, 4, "$Pattern", null, null);
      tmp[3] = new NonTerminalStackNode(-120, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-119, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-118, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-117, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__VariableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    protected static final void _init_prod__CallOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-70, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-69, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-64, 4, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-65, 0, "$Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-66, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-67, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-68, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-63, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-62, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-61, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-60, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__CallOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Set_$Pattern__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-79, 4, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode(-78, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-73, 2, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-74, 0, "$Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-75, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-76, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-77, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-72, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-71, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__Set_$Pattern__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__QualifiedName_$Pattern__qualifiedName_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-80, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__QualifiedName_$Pattern__qualifiedName_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-90, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-89, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-84, 2, regular__iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-85, 0, "$Mapping__$Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-86, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-87, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-88, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-83, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-82, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Literal_$Pattern__literal_$Literal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-81, 0, "$Literal", null, null);
      builder.addAlternative(RascalRascal.prod__Literal_$Pattern__literal_$Literal_, tmp);
	}
    protected static final void _init_prod__Anti_$Pattern__lit___33_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-124, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode(-123, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-122, 0, prod__lit___33__char_class___range__33_33_, new char[] {33}, null, null);
      builder.addAlternative(RascalRascal.prod__Anti_$Pattern__lit___33_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    protected static final void _init_prod__Tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-99, 4, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode(-98, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-93, 2, regular__iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-94, 0, "$Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-95, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-96, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-97, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-92, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-91, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__Tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__MultiVariable_$Pattern__qualifiedName_$QualifiedName_$layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__Descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
        _init_prod__TypedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
        _init_prod__TypedVariable_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__AsType_$Pattern__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Pattern_(builder);
      
        _init_prod__List_$Pattern__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__ReifiedType_$Pattern__basicType_$BasicType_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__VariableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
        _init_prod__CallOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Set_$Pattern__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__QualifiedName_$Pattern__qualifiedName_$QualifiedName_(builder);
      
        _init_prod__Map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Literal_$Pattern__literal_$Literal_(builder);
      
        _init_prod__Anti_$Pattern__lit___33_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
        _init_prod__Tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(builder);
      
    }
  }
	
  protected static class $Prod {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Unlabeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode(-130, 2, regular__iter_star_seps__$Sym__$layouts_LAYOUTLIST, new NonTerminalStackNode(-131, 0, "$Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-132, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-129, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode(-126, 0, regular__iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST, new NonTerminalStackNode(-127, 0, "$ProdModifier", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-128, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalRascal.prod__Unlabeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__All_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-159, 4, "$Prod", null, null);
      tmp[3] = new NonTerminalStackNode(-158, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-157, 2, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[1] = new NonTerminalStackNode(-156, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-155, 0, "$Prod", null, null);
      builder.addAlternative(RascalRascal.prod__All_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left, tmp);
	}
    protected static final void _init_prod__AssociativityGroup_$Prod__associativity_$Assoc_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_group_$Prod_$layouts_LAYOUTLIST_lit___41__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-139, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-138, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-137, 4, "$Prod", null, null);
      tmp[3] = new NonTerminalStackNode(-136, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-135, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-134, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-133, 0, "$Assoc", null, null);
      builder.addAlternative(RascalRascal.prod__AssociativityGroup_$Prod__associativity_$Assoc_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_group_$Prod_$layouts_LAYOUTLIST_lit___41__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Reference_$Prod__lit___58_$layouts_LAYOUTLIST_referenced_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-142, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-141, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-140, 0, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      builder.addAlternative(RascalRascal.prod__Reference_$Prod__lit___58_$layouts_LAYOUTLIST_referenced_$Name_, tmp);
	}
    protected static final void _init_prod__First_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-164, 4, "$Prod", null, null);
      tmp[3] = new NonTerminalStackNode(-163, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-162, 2, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {62})});
      tmp[1] = new NonTerminalStackNode(-161, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-160, 0, "$Prod", null, null);
      builder.addAlternative(RascalRascal.prod__First_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left, tmp);
	}
    protected static final void _init_prod__Others_$Prod__lit___46_46_46_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-143, 0, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new char[] {46,46,46}, null, null);
      builder.addAlternative(RascalRascal.prod__Others_$Prod__lit___46_46_46_, tmp);
	}
    protected static final void _init_prod__Labeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new SeparatedListStackNode(-152, 6, regular__iter_star_seps__$Sym__$layouts_LAYOUTLIST, new NonTerminalStackNode(-153, 0, "$Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-154, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode(-151, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-150, 4, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(-149, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-148, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-147, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode(-144, 0, regular__iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST, new NonTerminalStackNode(-145, 0, "$ProdModifier", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-146, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalRascal.prod__Labeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Unlabeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_(builder);
      
        _init_prod__All_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left(builder);
      
        _init_prod__AssociativityGroup_$Prod__associativity_$Assoc_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_group_$Prod_$layouts_LAYOUTLIST_lit___41__tag__Foldable(builder);
      
        _init_prod__Reference_$Prod__lit___58_$layouts_LAYOUTLIST_referenced_$Name_(builder);
      
        _init_prod__First_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left(builder);
      
        _init_prod__Others_$Prod__lit___46_46_46_(builder);
      
        _init_prod__Labeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $UnicodeEscape {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$UnicodeEscape__lit___92_iter__char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[6];
      
      tmp[5] = new CharStackNode(-171, 5, new char[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[4] = new CharStackNode(-170, 4, new char[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[3] = new CharStackNode(-169, 3, new char[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode(-168, 2, new char[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[1] = new ListStackNode(-166, 1, regular__iter__char_class___range__117_117, new CharStackNode(-167, 0, new char[][]{{117,117}}, null, null), true, null, null);
      tmp[0] = new LiteralStackNode(-165, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__$UnicodeEscape__lit___92_iter__char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$UnicodeEscape__lit___92_iter__char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
    }
  }
	
  protected static class $OctalIntegerLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode(-178, 1, regular__iter__char_class___range__48_55, new CharStackNode(-179, 0, new char[][]{{48,55}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode(-177, 0, new char[][]{{48,48}}, null, null);
      builder.addAlternative(RascalRascal.prod__$OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class $RationalLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new ListStackNode(-187, 4, regular__iter_star__char_class___range__48_57, new CharStackNode(-188, 0, new char[][]{{48,57}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[3] = new CharStackNode(-186, 3, new char[][]{{49,57}}, null, null);
      tmp[2] = new CharStackNode(-185, 2, new char[][]{{114,114}}, null, null);
      tmp[1] = new ListStackNode(-183, 1, regular__iter_star__char_class___range__48_57, new CharStackNode(-184, 0, new char[][]{{48,57}}, null, null), false, null, null);
      tmp[0] = new CharStackNode(-182, 0, new char[][]{{49,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(-192, 2, new char[][]{{114,114}}, null, null);
      tmp[1] = new ListStackNode(-190, 1, regular__iter_star__char_class___range__48_57, new CharStackNode(-191, 0, new char[][]{{48,57}}, null, null), false, null, null);
      tmp[0] = new CharStackNode(-189, 0, new char[][]{{49,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_, tmp);
	}
    protected static final void _init_prod__$RationalLiteral__char_class___range__48_48_char_class___range__114_114_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-194, 1, new char[][]{{114,114}}, null, null);
      tmp[0] = new CharStackNode(-193, 0, new char[][]{{48,48}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RationalLiteral__char_class___range__48_48_char_class___range__114_114_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_(builder);
      
        _init_prod__$RationalLiteral__char_class___range__48_48_char_class___range__114_114_(builder);
      
    }
  }
	
  protected static class $BooleanLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$BooleanLiteral__lit_false_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-203, 0, prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new char[] {102,97,108,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$BooleanLiteral__lit_false_, tmp);
	}
    protected static final void _init_prod__$BooleanLiteral__lit_true_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-204, 0, prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_, new char[] {116,114,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$BooleanLiteral__lit_true_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$BooleanLiteral__lit_false_(builder);
      
        _init_prod__$BooleanLiteral__lit_true_(builder);
      
    }
  }
	
  protected static class $Toplevel {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__GivenVisibility_$Toplevel__declaration_$Declaration_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-202, 0, "$Declaration", null, null);
      builder.addAlternative(RascalRascal.prod__GivenVisibility_$Toplevel__declaration_$Declaration_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__GivenVisibility_$Toplevel__declaration_$Declaration_(builder);
      
    }
  }
	
  protected static class $ProdModifier {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Bracket_$ProdModifier__lit_bracket_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-205, 0, prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_, new char[] {98,114,97,99,107,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Bracket_$ProdModifier__lit_bracket_, tmp);
	}
    protected static final void _init_prod__Associativity_$ProdModifier__associativity_$Assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-206, 0, "$Assoc", null, null);
      builder.addAlternative(RascalRascal.prod__Associativity_$ProdModifier__associativity_$Assoc_, tmp);
	}
    protected static final void _init_prod__Tag_$ProdModifier__tag_$Tag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-207, 0, "$Tag", null, null);
      builder.addAlternative(RascalRascal.prod__Tag_$ProdModifier__tag_$Tag_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Bracket_$ProdModifier__lit_bracket_(builder);
      
        _init_prod__Associativity_$ProdModifier__associativity_$Assoc_(builder);
      
        _init_prod__Tag_$ProdModifier__tag_$Tag_(builder);
      
    }
  }
	
  protected static class $TypeVar {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Bounded_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___60_58_$layouts_LAYOUTLIST_bound_$Type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-215, 6, "$Type", null, null);
      tmp[5] = new NonTerminalStackNode(-214, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-213, 4, prod__lit___60_58__char_class___range__60_60_char_class___range__58_58_, new char[] {60,58}, null, null);
      tmp[3] = new NonTerminalStackNode(-212, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-211, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-210, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-209, 0, prod__lit___38__char_class___range__38_38_, new char[] {38}, null, null);
      builder.addAlternative(RascalRascal.prod__Bounded_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___60_58_$layouts_LAYOUTLIST_bound_$Type_, tmp);
	}
    protected static final void _init_prod__Free_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-218, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-217, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-216, 0, prod__lit___38__char_class___range__38_38_, new char[] {38}, null, null);
      builder.addAlternative(RascalRascal.prod__Free_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Bounded_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___60_58_$layouts_LAYOUTLIST_bound_$Type_(builder);
      
        _init_prod__Free_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_(builder);
      
    }
  }
	
  protected static class $Comprehension {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Set_$Comprehension__lit___123_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-252, 8, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode(-251, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-246, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-247, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-248, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-249, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-250, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-245, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-244, 4, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(-243, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-238, 2, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-239, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-240, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-241, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-242, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-237, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-236, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__Set_$Comprehension__lit___123_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-269, 12, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode(-268, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-263, 10, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-264, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-265, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-266, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-267, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(-262, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-261, 8, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode(-260, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-259, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-258, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-257, 4, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(-256, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-255, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-254, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-253, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__List_$Comprehension__lit___91_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-286, 8, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[7] = new NonTerminalStackNode(-285, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-280, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-281, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-282, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-283, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-284, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-279, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-278, 4, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(-277, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-272, 2, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-273, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-274, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-275, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-276, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-271, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-270, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__List_$Comprehension__lit___91_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Set_$Comprehension__lit___123_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__Map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__List_$Comprehension__lit___91_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class $FunctionModifiers {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__List_$FunctionModifiers__modifiers_iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode(-233, 0, regular__iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST, new NonTerminalStackNode(-234, 0, "$FunctionModifier", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-235, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalRascal.prod__List_$FunctionModifiers__modifiers_iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__List_$FunctionModifiers__modifiers_iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Type {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Selector_$Type__selector_$DataTypeSelector_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-287, 0, "$DataTypeSelector", null, null);
      builder.addAlternative(RascalRascal.prod__Selector_$Type__selector_$DataTypeSelector_, tmp);
	}
    protected static final void _init_prod__Structured_$Type__structured_$StructuredType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-288, 0, "$StructuredType", null, null);
      builder.addAlternative(RascalRascal.prod__Structured_$Type__structured_$StructuredType_, tmp);
	}
    protected static final void _init_prod__Bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-293, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-292, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-291, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-290, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-289, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Function_$Type__function_$FunctionType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-294, 0, "$FunctionType", null, null);
      builder.addAlternative(RascalRascal.prod__Function_$Type__function_$FunctionType_, tmp);
	}
    protected static final void _init_prod__User_$Type__conditional__user_$UserType__delete__$HeaderKeyword_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-295, 0, "$UserType", null, new ICompletionFilter[] {new StringMatchRestriction(new char[] {108,101,120,105,99,97,108}), new StringMatchRestriction(new char[] {105,109,112,111,114,116}), new StringMatchRestriction(new char[] {115,116,97,114,116}), new StringMatchRestriction(new char[] {115,121,110,116,97,120}), new StringMatchRestriction(new char[] {107,101,121,119,111,114,100}), new StringMatchRestriction(new char[] {101,120,116,101,110,100}), new StringMatchRestriction(new char[] {108,97,121,111,117,116})});
      builder.addAlternative(RascalRascal.prod__User_$Type__conditional__user_$UserType__delete__$HeaderKeyword_, tmp);
	}
    protected static final void _init_prod__Basic_$Type__basic_$BasicType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-296, 0, "$BasicType", null, null);
      builder.addAlternative(RascalRascal.prod__Basic_$Type__basic_$BasicType_, tmp);
	}
    protected static final void _init_prod__Variable_$Type__typeVar_$TypeVar_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-297, 0, "$TypeVar", null, null);
      builder.addAlternative(RascalRascal.prod__Variable_$Type__typeVar_$TypeVar_, tmp);
	}
    protected static final void _init_prod__Symbol_$Type__symbol_$Sym_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-298, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Symbol_$Type__symbol_$Sym_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Selector_$Type__selector_$DataTypeSelector_(builder);
      
        _init_prod__Structured_$Type__structured_$StructuredType_(builder);
      
        _init_prod__Bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__Function_$Type__function_$FunctionType_(builder);
      
        _init_prod__User_$Type__conditional__user_$UserType__delete__$HeaderKeyword_(builder);
      
        _init_prod__Basic_$Type__basic_$BasicType_(builder);
      
        _init_prod__Variable_$Type__typeVar_$TypeVar_(builder);
      
        _init_prod__Symbol_$Type__symbol_$Sym_(builder);
      
    }
  }
	
  protected static class $Declaration {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Alias_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_alias_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_base_$Type_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-312, 12, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[11] = new NonTerminalStackNode(-311, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-310, 10, "$Type", null, null);
      tmp[9] = new NonTerminalStackNode(-309, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-308, 8, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(-307, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-306, 6, "$UserType", null, null);
      tmp[5] = new NonTerminalStackNode(-305, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-304, 4, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new char[] {97,108,105,97,115}, null, null);
      tmp[3] = new NonTerminalStackNode(-303, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-302, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-301, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-300, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Alias_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_alias_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_base_$Type_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Function_$Declaration__functionDeclaration_$FunctionDeclaration_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-299, 0, "$FunctionDeclaration", null, null);
      builder.addAlternative(RascalRascal.prod__Function_$Declaration__functionDeclaration_$FunctionDeclaration_, tmp);
	}
    protected static final void _init_prod__Rule_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_lit_rule_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_patternAction_$PatternWithAction_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[3] = new NonTerminalStackNode(-316, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-317, 4, "$Name", null, null);
      tmp[5] = new NonTerminalStackNode(-318, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-319, 6, "$PatternWithAction", null, null);
      tmp[7] = new NonTerminalStackNode(-320, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-321, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[0] = new NonTerminalStackNode(-313, 0, "$Tags", null, null);
      tmp[1] = new NonTerminalStackNode(-314, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-315, 2, prod__lit_rule__char_class___range__114_114_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {114,117,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Rule_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_lit_rule_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_patternAction_$PatternWithAction_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(-336, 14, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode(-335, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(-334, 12, "$Name", null, null);
      tmp[11] = new NonTerminalStackNode(-333, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-332, 10, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      tmp[9] = new NonTerminalStackNode(-331, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-330, 8, "$Type", null, null);
      tmp[7] = new NonTerminalStackNode(-329, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-328, 6, "$Type", null, null);
      tmp[5] = new NonTerminalStackNode(-327, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-326, 4, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new char[] {97,110,110,111}, null, null);
      tmp[3] = new NonTerminalStackNode(-325, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-324, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-323, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-322, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(-355, 14, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode(-354, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(-349, 12, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-350, 0, "$Type", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-351, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-352, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-353, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode(-348, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-347, 10, prod__lit_on__char_class___range__111_111_char_class___range__110_110_, new char[] {111,110}, null, null);
      tmp[9] = new NonTerminalStackNode(-346, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-345, 8, "$Name", null, null);
      tmp[7] = new NonTerminalStackNode(-344, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-343, 6, "$Kind", null, null);
      tmp[5] = new NonTerminalStackNode(-342, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-341, 4, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new char[] {116,97,103}, null, null);
      tmp[3] = new NonTerminalStackNode(-340, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-339, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-338, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-337, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-368, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(-367, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-362, 6, regular__iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-363, 0, "$Variable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-364, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-365, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-366, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-361, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-360, 4, "$Type", null, null);
      tmp[3] = new NonTerminalStackNode(-359, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-358, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-357, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-356, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__DataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-377, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(-376, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-375, 6, "$UserType", null, null);
      tmp[5] = new NonTerminalStackNode(-374, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-373, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new char[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode(-372, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-371, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-370, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-369, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__DataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Data_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_variants_iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-394, 12, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[11] = new NonTerminalStackNode(-393, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-388, 10, regular__iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST, new NonTerminalStackNode(-389, 0, "$Variant", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-390, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-391, 2, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null), new NonTerminalStackNode(-392, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(-387, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-386, 8, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(-385, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-384, 6, "$UserType", null, null);
      tmp[5] = new NonTerminalStackNode(-383, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-382, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new char[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode(-381, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-380, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-379, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-378, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Data_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_variants_iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Alias_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_alias_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_base_$Type_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Function_$Declaration__functionDeclaration_$FunctionDeclaration_(builder);
      
        _init_prod__Rule_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_lit_rule_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_patternAction_$PatternWithAction_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__DataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Data_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_variants_iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
    }
  }
	
  protected static class $Class {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Union_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Class__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-419, 4, "$Class", null, null);
      tmp[3] = new NonTerminalStackNode(-418, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-417, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new char[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode(-416, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-415, 0, "$Class", null, null);
      builder.addAlternative(RascalRascal.prod__Union_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Class__assoc__left, tmp);
	}
    protected static final void _init_prod__SimpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-401, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-400, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-397, 2, regular__iter_star_seps__$Range__$layouts_LAYOUTLIST, new NonTerminalStackNode(-398, 0, "$Range", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-399, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-396, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-395, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__SimpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-424, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-423, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-422, 2, "$Class", null, null);
      tmp[1] = new NonTerminalStackNode(-421, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-420, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-404, 2, "$Class", null, null);
      tmp[1] = new NonTerminalStackNode(-403, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-402, 0, prod__lit___33__char_class___range__33_33_, new char[] {33}, null, null);
      builder.addAlternative(RascalRascal.prod__Complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_, tmp);
	}
    protected static final void _init_prod__Difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-409, 4, "$Class", null, null);
      tmp[3] = new NonTerminalStackNode(-408, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-407, 2, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode(-406, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-405, 0, "$Class", null, null);
      builder.addAlternative(RascalRascal.prod__Difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left, tmp);
	}
    protected static final void _init_prod__Intersection_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Class__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-414, 4, "$Class", null, null);
      tmp[3] = new NonTerminalStackNode(-413, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-412, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new char[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode(-411, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-410, 0, "$Class", null, null);
      builder.addAlternative(RascalRascal.prod__Intersection_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Class__assoc__left, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Union_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Class__assoc__left(builder);
      
        _init_prod__SimpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__Complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_(builder);
      
        _init_prod__Difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left(builder);
      
        _init_prod__Intersection_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Class__assoc__left(builder);
      
    }
  }
	
  protected static class $Bound {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Empty_$Bound__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(-425, 0);
      builder.addAlternative(RascalRascal.prod__Empty_$Bound__, tmp);
	}
    protected static final void _init_prod__Default_$Bound__lit___59_$layouts_LAYOUTLIST_expression_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-428, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-427, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-426, 0, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Bound__lit___59_$layouts_LAYOUTLIST_expression_$Expression_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Empty_$Bound__(builder);
      
        _init_prod__Default_$Bound__lit___59_$layouts_LAYOUTLIST_expression_$Expression_(builder);
      
    }
  }
	
  protected static class $FunctionType {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TypeArguments_$FunctionType__type_$Type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-448, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-447, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-442, 4, regular__iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-443, 0, "$TypeArg", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-444, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-445, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-446, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-441, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-440, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-439, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-438, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__TypeArguments_$FunctionType__type_$Type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__TypeArguments_$FunctionType__type_$Type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class $Case {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PatternWithAction_$Case__lit_case_$layouts_LAYOUTLIST_patternWithAction_$PatternWithAction__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-455, 2, "$PatternWithAction", null, null);
      tmp[1] = new NonTerminalStackNode(-454, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-453, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new char[] {99,97,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__PatternWithAction_$Case__lit_case_$layouts_LAYOUTLIST_patternWithAction_$PatternWithAction__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Default_$Case__lit_default_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-460, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-459, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-458, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-457, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-456, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new char[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Case__lit_default_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__PatternWithAction_$Case__lit_case_$layouts_LAYOUTLIST_patternWithAction_$PatternWithAction__tag__Foldable(builder);
      
        _init_prod__Default_$Case__lit_default_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement__tag__Foldable(builder);
      
    }
  }
	
  protected static class $Statement {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Fail_$Statement__lit_fail_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[0] = new LiteralStackNode(-522, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new char[] {102,97,105,108}, null, null);
      tmp[1] = new NonTerminalStackNode(-523, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-524, 2, "$Target", null, null);
      tmp[3] = new NonTerminalStackNode(-525, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-526, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__Fail_$Statement__lit_fail_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__TryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode(-505, 8, "$Statement", null, null);
      tmp[7] = new NonTerminalStackNode(-504, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-503, 6, prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_, new char[] {102,105,110,97,108,108,121}, null, null);
      tmp[5] = new NonTerminalStackNode(-502, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-499, 4, regular__iter_seps__$Catch__$layouts_LAYOUTLIST, new NonTerminalStackNode(-500, 0, "$Catch", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-501, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-498, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-497, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode(-496, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-495, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new char[] {116,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__TryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement_, tmp);
	}
    protected static final void _init_prod__Insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-510, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-509, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-508, 2, "$DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode(-507, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-506, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new char[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-531, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(-530, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-529, 2, "$Target", null, null);
      tmp[1] = new NonTerminalStackNode(-528, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-527, 0, prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_, new char[] {99,111,110,116,105,110,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-536, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(-535, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-534, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-533, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-532, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new char[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__FunctionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-709, 0, "$FunctionDeclaration", null, null);
      builder.addAlternative(RascalRascal.prod__FunctionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration_, tmp);
	}
    protected static final void _init_prod__Expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode(-538, 0, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-539, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-540, 2, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__Expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__EmptyStatement_$Statement__lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-537, 0, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__EmptyStatement_$Statement__lit___59_, tmp);
	}
    protected static final void _init_prod__Try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode(-545, 4, regular__iter_seps__$Catch__$layouts_LAYOUTLIST, new NonTerminalStackNode(-546, 0, "$Catch", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-547, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-544, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-543, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode(-542, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-541, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new char[] {116,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__Try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__DoWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(-562, 14, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode(-561, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode(-560, 12, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode(-559, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-558, 10, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode(-557, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-556, 8, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[7] = new NonTerminalStackNode(-555, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-554, 6, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new char[] {119,104,105,108,101}, null, null);
      tmp[5] = new NonTerminalStackNode(-553, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-552, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-551, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-550, 2, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new char[] {100,111}, null, null);
      tmp[1] = new NonTerminalStackNode(-549, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-548, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__DoWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__For_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode(-577, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode(-576, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-575, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-574, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-569, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-570, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-571, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-572, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-573, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-568, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-567, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-566, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-565, 2, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new char[] {102,111,114}, null, null);
      tmp[1] = new NonTerminalStackNode(-564, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-563, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__For_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_, tmp);
	}
    protected static final void _init_prod__AssertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-586, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(-585, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-584, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-583, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-582, 4, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(-581, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-580, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-579, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-578, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new char[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__AssertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__While_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode(-601, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode(-600, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-599, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-598, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-593, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-594, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-595, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-596, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-597, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-592, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-591, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-590, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-589, 2, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new char[] {119,104,105,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode(-588, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-587, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__While_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_, tmp);
	}
    protected static final void _init_prod__Assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-606, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-605, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-604, 2, "$Assignment", null, null);
      tmp[1] = new NonTerminalStackNode(-603, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-602, 0, "$Assignable", null, null);
      builder.addAlternative(RascalRascal.prod__Assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement_, tmp);
	}
    protected static final void _init_prod__Filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-609, 2, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode(-608, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-607, 0, prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new char[] {102,105,108,116,101,114}, null, null);
      builder.addAlternative(RascalRascal.prod__Filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode(-624, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode(-623, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-622, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-621, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-620, 6, "$Bound", null, null);
      tmp[5] = new NonTerminalStackNode(-619, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-614, 4, regular__iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-615, 0, "$QualifiedName", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-616, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-617, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-618, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-613, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-612, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-611, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-610, 0, prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_, new char[] {115,111,108,118,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_, tmp);
	}
    protected static final void _init_prod__Break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-640, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(-639, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-638, 2, "$Target", null, null);
      tmp[1] = new NonTerminalStackNode(-637, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-636, 0, prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new char[] {98,114,101,97,107}, null, null);
      builder.addAlternative(RascalRascal.prod__Break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__VariableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-708, 2, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode(-707, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-706, 0, "$LocalVariableDeclaration", null, null);
      builder.addAlternative(RascalRascal.prod__VariableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__GlobalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-635, 6, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode(-634, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-629, 4, regular__iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-630, 0, "$QualifiedName", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-631, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-632, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-633, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-628, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-627, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-626, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-625, 0, prod__lit_global__char_class___range__103_103_char_class___range__108_108_char_class___range__111_111_char_class___range__98_98_char_class___range__97_97_char_class___range__108_108_, new char[] {103,108,111,98,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__GlobalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode(-511, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new char[] {114,101,116,117,114,110}, null, null);
      tmp[1] = new NonTerminalStackNode(-512, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-513, 2, "$Statement", null, null);
      builder.addAlternative(RascalRascal.prod__Return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__NonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-649, 6, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode(-648, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-645, 4, regular__iter_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-646, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-647, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-644, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-641, 0, "$Label", null, null);
      tmp[1] = new NonTerminalStackNode(-642, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-643, 2, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__NonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-518, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-517, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-516, 2, "$DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode(-515, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-514, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new char[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__Append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-521, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode(-520, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-519, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new char[] {116,104,114,111,119}, null, null);
      builder.addAlternative(RascalRascal.prod__Throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-669, 2, "$Visit", null, null);
      tmp[1] = new NonTerminalStackNode(-668, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-667, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__Visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_, tmp);
	}
    protected static final void _init_prod__IfThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new EmptyStackNode(-666, 12, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {101,108,115,101})});
      tmp[11] = new NonTerminalStackNode(-665, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-664, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode(-663, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-662, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-661, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-656, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-657, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-658, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-659, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-660, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-655, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-654, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-653, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-652, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new char[] {105,102}, null, null);
      tmp[1] = new NonTerminalStackNode(-651, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-650, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__IfThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else_, tmp);
	}
    protected static final void _init_prod__Switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(-686, 14, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode(-685, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(-682, 12, regular__iter_seps__$Case__$layouts_LAYOUTLIST, new NonTerminalStackNode(-683, 0, "$Case", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-684, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode(-681, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-680, 10, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode(-679, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-678, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-677, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-676, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-675, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-674, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-673, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-672, 2, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new char[] {115,119,105,116,99,104}, null, null);
      tmp[1] = new NonTerminalStackNode(-671, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-670, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__Switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__IfThenElse_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_elseStatement_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new NonTerminalStackNode(-705, 14, "$Statement", null, null);
      tmp[13] = new NonTerminalStackNode(-704, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode(-703, 12, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new char[] {101,108,115,101}, null, null);
      tmp[11] = new NonTerminalStackNode(-702, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-701, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode(-700, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-699, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-698, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-693, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-694, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-695, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-696, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-697, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-692, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-691, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-690, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-689, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new char[] {105,102}, null, null);
      tmp[1] = new NonTerminalStackNode(-688, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-687, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__IfThenElse_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_elseStatement_$Statement_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Fail_$Statement__lit_fail_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__TryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement_(builder);
      
        _init_prod__Insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(builder);
      
        _init_prod__Continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__FunctionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration_(builder);
      
        _init_prod__Expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__EmptyStatement_$Statement__lit___59_(builder);
      
        _init_prod__Try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc(builder);
      
        _init_prod__DoWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__For_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_(builder);
      
        _init_prod__AssertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__While_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_(builder);
      
        _init_prod__Assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement_(builder);
      
        _init_prod__Filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_(builder);
      
        _init_prod__Break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__VariableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__GlobalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(builder);
      
        _init_prod__NonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__Append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(builder);
      
        _init_prod__Throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(builder);
      
        _init_prod__Visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_(builder);
      
        _init_prod__IfThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else_(builder);
      
        _init_prod__Switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__IfThenElse_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_elseStatement_$Statement_(builder);
      
    }
  }
	
  protected static class $Renamings {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$Renamings__lit_renaming_$layouts_LAYOUTLIST_renamings_iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode(-728, 2, regular__iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-729, 0, "$Renaming", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-730, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-731, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-732, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-727, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-726, 0, prod__lit_renaming__char_class___range__114_114_char_class___range__101_101_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_, new char[] {114,101,110,97,109,105,110,103}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Renamings__lit_renaming_$layouts_LAYOUTLIST_renamings_iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$Renamings__lit_renaming_$layouts_LAYOUTLIST_renamings_iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $StringLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NonInterpolated_$StringLiteral__constant_$StringConstant_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-733, 0, "$StringConstant", null, null);
      builder.addAlternative(RascalRascal.prod__NonInterpolated_$StringLiteral__constant_$StringConstant_, tmp);
	}
    protected static final void _init_prod__Template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-738, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(-737, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-736, 2, "$StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode(-735, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-734, 0, "$PreStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__Template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    protected static final void _init_prod__Interpolated_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-743, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(-742, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-741, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-740, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-739, 0, "$PreStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__Interpolated_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__NonInterpolated_$StringLiteral__constant_$StringConstant_(builder);
      
        _init_prod__Template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(builder);
      
        _init_prod__Interpolated_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(builder);
      
    }
  }
	
  protected static class $Visibility {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Public_$Visibility__lit_public_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-757, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new char[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__Public_$Visibility__lit_public_, tmp);
	}
    protected static final void _init_prod__Default_$Visibility__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(-758, 0);
      builder.addAlternative(RascalRascal.prod__Default_$Visibility__, tmp);
	}
    protected static final void _init_prod__Private_$Visibility__lit_private_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-759, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new char[] {112,114,105,118,97,116,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Private_$Visibility__lit_private_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Public_$Visibility__lit_public_(builder);
      
        _init_prod__Default_$Visibility__(builder);
      
        _init_prod__Private_$Visibility__lit_private_(builder);
      
    }
  }
	
  protected static class $PostPathChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PostPathChars__lit___62_$URLChars_lit___124_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-762, 2, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[1] = new NonTerminalStackNode(-761, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode(-760, 0, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      builder.addAlternative(RascalRascal.prod__$PostPathChars__lit___62_$URLChars_lit___124_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$PostPathChars__lit___62_$URLChars_lit___124_(builder);
      
    }
  }
	
  protected static class $NamedBackslash {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$NamedBackslash__conditional__char_class___range__92_92__not_follow__char_class___range__60_60_range__62_62_range__92_92_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(-768, 0, new char[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{60,60},{62,62},{92,92}})});
      builder.addAlternative(RascalRascal.prod__$NamedBackslash__conditional__char_class___range__92_92__not_follow__char_class___range__60_60_range__62_62_range__92_92_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$NamedBackslash__conditional__char_class___range__92_92__not_follow__char_class___range__60_60_range__62_62_range__92_92_(builder);
      
    }
  }
	
  protected static class $Command {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Import_$Command__imported_$Import_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-809, 0, "$Import", null, null);
      builder.addAlternative(RascalRascal.prod__Import_$Command__imported_$Import_, tmp);
	}
    protected static final void _init_prod__Expression_$Command__expression_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-810, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Expression_$Command__expression_$Expression_, tmp);
	}
    protected static final void _init_prod__Statement_$Command__statement_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-811, 0, "$Statement", null, null);
      builder.addAlternative(RascalRascal.prod__Statement_$Command__statement_$Statement_, tmp);
	}
    protected static final void _init_prod__Declaration_$Command__declaration_$Declaration_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-812, 0, "$Declaration", null, null);
      builder.addAlternative(RascalRascal.prod__Declaration_$Command__declaration_$Declaration_, tmp);
	}
    protected static final void _init_prod__Shell_$Command__lit___58_$layouts_LAYOUTLIST_command_$ShellCommand_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-815, 2, "$ShellCommand", null, null);
      tmp[1] = new NonTerminalStackNode(-814, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-813, 0, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      builder.addAlternative(RascalRascal.prod__Shell_$Command__lit___58_$layouts_LAYOUTLIST_command_$ShellCommand_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Import_$Command__imported_$Import_(builder);
      
        _init_prod__Expression_$Command__expression_$Expression_(builder);
      
        _init_prod__Statement_$Command__statement_$Statement_(builder);
      
        _init_prod__Declaration_$Command__declaration_$Declaration_(builder);
      
        _init_prod__Shell_$Command__lit___58_$layouts_LAYOUTLIST_command_$ShellCommand_(builder);
      
    }
  }
	
  protected static class $Visit {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DefaultStrategy_$Visit__lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-791, 12, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[11] = new NonTerminalStackNode(-790, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-787, 10, regular__iter_seps__$Case__$layouts_LAYOUTLIST, new NonTerminalStackNode(-788, 0, "$Case", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-789, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(-786, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-785, 8, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(-784, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-783, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-782, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-781, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-780, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-779, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-778, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-777, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new char[] {118,105,115,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__DefaultStrategy_$Visit__lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__GivenStrategy_$Visit__strategy_$Strategy_$layouts_LAYOUTLIST_lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(-808, 14, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode(-807, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(-804, 12, regular__iter_seps__$Case__$layouts_LAYOUTLIST, new NonTerminalStackNode(-805, 0, "$Case", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-806, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode(-803, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-802, 10, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode(-801, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-800, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-799, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-798, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-797, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-796, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-795, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-794, 2, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new char[] {118,105,115,105,116}, null, null);
      tmp[1] = new NonTerminalStackNode(-793, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-792, 0, "$Strategy", null, null);
      builder.addAlternative(RascalRascal.prod__GivenStrategy_$Visit__strategy_$Strategy_$layouts_LAYOUTLIST_lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__DefaultStrategy_$Visit__lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__GivenStrategy_$Visit__strategy_$Strategy_$layouts_LAYOUTLIST_lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class $ProtocolTail {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Post_$ProtocolTail__post_$PostProtocolChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-816, 0, "$PostProtocolChars", null, null);
      builder.addAlternative(RascalRascal.prod__Post_$ProtocolTail__post_$PostProtocolChars_, tmp);
	}
    protected static final void _init_prod__Mid_$ProtocolTail__mid_$MidProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-821, 4, "$ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode(-820, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-819, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-818, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-817, 0, "$MidProtocolChars", null, null);
      builder.addAlternative(RascalRascal.prod__Mid_$ProtocolTail__mid_$MidProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Post_$ProtocolTail__post_$PostProtocolChars_(builder);
      
        _init_prod__Mid_$ProtocolTail__mid_$MidProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(builder);
      
    }
  }
	
  protected static class $PreStringChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PreStringChars__char_class___range__34_34_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(-833, 2, new char[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode(-831, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode(-832, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode(-830, 0, new char[][]{{34,34}}, null, null);
      builder.addAlternative(RascalRascal.prod__$PreStringChars__char_class___range__34_34_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$PreStringChars__char_class___range__34_34_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $QualifiedName {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$QualifiedName__conditional__names_iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST__not_follow__lit___58_58_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode(-845, 0, regular__iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST, new NonTerminalStackNode(-846, 0, "$Name", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-847, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-848, 2, prod__lit___58_58__char_class___range__58_58_char_class___range__58_58_, new char[] {58,58}, null, null), new NonTerminalStackNode(-849, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {58,58})});
      builder.addAlternative(RascalRascal.prod__Default_$QualifiedName__conditional__names_iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST__not_follow__lit___58_58_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$QualifiedName__conditional__names_iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST__not_follow__lit___58_58_(builder);
      
    }
  }
	
  protected static class $StringMiddle {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-860, 4, "$StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode(-859, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-858, 2, "$StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode(-857, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-856, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__Template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_, tmp);
	}
    protected static final void _init_prod__Interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-865, 4, "$StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode(-864, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-863, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-862, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-861, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__Interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_, tmp);
	}
    protected static final void _init_prod__Mid_$StringMiddle__mid_$MidStringChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-866, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__Mid_$StringMiddle__mid_$MidStringChars_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_(builder);
      
        _init_prod__Interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_(builder);
      
        _init_prod__Mid_$StringMiddle__mid_$MidStringChars_(builder);
      
    }
  }
	
  protected static class $URLChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_65535_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode(-854, 0, regular__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_65535, new CharStackNode(-855, 0, new char[][]{{0,8},{11,12},{14,31},{33,59},{61,123},{125,65535}}, null, null), false, null, null);
      builder.addAlternative(RascalRascal.prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_65535_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_65535_(builder);
      
    }
  }
	
  protected static class $TimeZonePart {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$TimeZonePart__lit_Z_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-867, 0, prod__lit_Z__char_class___range__90_90_, new char[] {90}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimeZonePart__lit_Z_, tmp);
	}
    protected static final void _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(-870, 2, new char[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(-869, 1, new char[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode(-868, 0, new char[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode(-875, 4, new char[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode(-874, 3, new char[][]{{48,53}}, null, null);
      tmp[2] = new CharStackNode(-873, 2, new char[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(-872, 1, new char[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode(-871, 0, new char[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[6];
      
      tmp[5] = new CharStackNode(-881, 5, new char[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode(-880, 4, new char[][]{{48,53}}, null, null);
      tmp[3] = new LiteralStackNode(-879, 3, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[2] = new CharStackNode(-878, 2, new char[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(-877, 1, new char[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode(-876, 0, new char[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$TimeZonePart__lit_Z_(builder);
      
        _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_(builder);
      
        _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_(builder);
      
        _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class $ShellCommand {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Edit_$ShellCommand__lit_edit_$layouts_LAYOUTLIST_name_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-884, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(-883, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-882, 0, prod__lit_edit__char_class___range__101_101_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_, new char[] {101,100,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Edit_$ShellCommand__lit_edit_$layouts_LAYOUTLIST_name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Help_$ShellCommand__lit_help_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-885, 0, prod__lit_help__char_class___range__104_104_char_class___range__101_101_char_class___range__108_108_char_class___range__112_112_, new char[] {104,101,108,112}, null, null);
      builder.addAlternative(RascalRascal.prod__Help_$ShellCommand__lit_help_, tmp);
	}
    protected static final void _init_prod__Quit_$ShellCommand__lit_quit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-886, 0, prod__lit_quit__char_class___range__113_113_char_class___range__117_117_char_class___range__105_105_char_class___range__116_116_, new char[] {113,117,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Quit_$ShellCommand__lit_quit_, tmp);
	}
    protected static final void _init_prod__SetOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-891, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-890, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-889, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(-888, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-887, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new char[] {115,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__SetOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_, tmp);
	}
    protected static final void _init_prod__History_$ShellCommand__lit_history_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-892, 0, prod__lit_history__char_class___range__104_104_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__121_121_, new char[] {104,105,115,116,111,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__History_$ShellCommand__lit_history_, tmp);
	}
    protected static final void _init_prod__Unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-895, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(-894, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-893, 0, prod__lit_unimport__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new char[] {117,110,105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-898, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(-897, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-896, 0, prod__lit_undeclare__char_class___range__117_117_char_class___range__110_110_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__101_101_, new char[] {117,110,100,101,99,108,97,114,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__ListModules_$ShellCommand__lit_modules_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-899, 0, prod__lit_modules__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_, new char[] {109,111,100,117,108,101,115}, null, null);
      builder.addAlternative(RascalRascal.prod__ListModules_$ShellCommand__lit_modules_, tmp);
	}
    protected static final void _init_prod__ListDeclarations_$ShellCommand__lit_declarations_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-901, 0, prod__lit_declarations__char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_, new char[] {100,101,99,108,97,114,97,116,105,111,110,115}, null, null);
      builder.addAlternative(RascalRascal.prod__ListDeclarations_$ShellCommand__lit_declarations_, tmp);
	}
    protected static final void _init_prod__Test_$ShellCommand__lit_test_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-900, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new char[] {116,101,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Test_$ShellCommand__lit_test_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Edit_$ShellCommand__lit_edit_$layouts_LAYOUTLIST_name_$QualifiedName_(builder);
      
        _init_prod__Help_$ShellCommand__lit_help_(builder);
      
        _init_prod__Quit_$ShellCommand__lit_quit_(builder);
      
        _init_prod__SetOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_(builder);
      
        _init_prod__History_$ShellCommand__lit_history_(builder);
      
        _init_prod__Unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_(builder);
      
        _init_prod__Undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_(builder);
      
        _init_prod__ListModules_$ShellCommand__lit_modules_(builder);
      
        _init_prod__ListDeclarations_$ShellCommand__lit_declarations_(builder);
      
        _init_prod__Test_$ShellCommand__lit_test_(builder);
      
    }
  }
	
  protected static class $LocationLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$LocationLiteral__protocolPart_$ProtocolPart_$layouts_LAYOUTLIST_pathPart_$PathPart_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-908, 2, "$PathPart", null, null);
      tmp[1] = new NonTerminalStackNode(-907, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-906, 0, "$ProtocolPart", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$LocationLiteral__protocolPart_$ProtocolPart_$layouts_LAYOUTLIST_pathPart_$PathPart_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$LocationLiteral__protocolPart_$ProtocolPart_$layouts_LAYOUTLIST_pathPart_$PathPart_(builder);
      
    }
  }
	
  protected static class $Range {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Character_$Range__character_$Char_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-917, 0, "$Char", null, null);
      builder.addAlternative(RascalRascal.prod__Character_$Range__character_$Char_, tmp);
	}
    protected static final void _init_prod__FromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-922, 4, "$Char", null, null);
      tmp[3] = new NonTerminalStackNode(-921, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-920, 2, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode(-919, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-918, 0, "$Char", null, null);
      builder.addAlternative(RascalRascal.prod__FromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Character_$Range__character_$Char_(builder);
      
        _init_prod__FromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_(builder);
      
    }
  }
	
  protected static class $JustTime {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$JustTime__lit___36_84_$TimePartNoTZ_opt__$TimeZonePart_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new OptionalStackNode(-934, 2, regular__opt__$TimeZonePart, new NonTerminalStackNode(-935, 0, "$TimeZonePart", null, null), null, null);
      tmp[1] = new NonTerminalStackNode(-933, 1, "$TimePartNoTZ", null, null);
      tmp[0] = new LiteralStackNode(-932, 0, prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_, new char[] {36,84}, null, null);
      builder.addAlternative(RascalRascal.prod__$JustTime__lit___36_84_$TimePartNoTZ_opt__$TimeZonePart_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$JustTime__lit___36_84_$TimePartNoTZ_opt__$TimeZonePart_(builder);
      
    }
  }
	
  protected static class $StringCharacter {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(-951, 2, new char[][]{{39,39}}, null, null);
      tmp[1] = new ListStackNode(-949, 1, regular__iter_star__char_class___range__9_9_range__32_32, new CharStackNode(-950, 0, new char[][]{{9,9},{32,32}}, null, null), false, null, null);
      tmp[0] = new CharStackNode(-948, 0, new char[][]{{10,10}}, null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__$OctalEscapeSequence_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-952, 0, "$OctalEscapeSequence", null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__$OctalEscapeSequence_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__$UnicodeEscape_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-953, 0, "$UnicodeEscape", null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__$UnicodeEscape_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_65535_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(-954, 0, new char[][]{{0,33},{35,38},{40,59},{61,61},{63,91},{93,65535}}, null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_65535_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-956, 1, new char[][]{{34,34},{39,39},{60,60},{62,62},{92,92},{98,98},{102,102},{110,110},{114,114},{116,116}}, null, null);
      tmp[0] = new LiteralStackNode(-955, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_(builder);
      
        _init_prod__$StringCharacter__$OctalEscapeSequence_(builder);
      
        _init_prod__$StringCharacter__$UnicodeEscape_(builder);
      
        _init_prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_65535_(builder);
      
        _init_prod__$StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_(builder);
      
    }
  }
	
  protected static class $layouts_LAYOUTLIST {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$layouts_LAYOUTLIST__conditional__iter_star__$LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode(-958, 0, regular__iter_star__$LAYOUT, new NonTerminalStackNode(-959, 0, "$LAYOUT", null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{9,10},{13,13},{32,32}}), new StringFollowRestriction(new char[] {47,47}), new StringFollowRestriction(new char[] {47,42})});
      builder.addAlternative(RascalRascal.prod__$layouts_LAYOUTLIST__conditional__iter_star__$LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$layouts_LAYOUTLIST__conditional__iter_star__$LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_(builder);
      
    }
  }
	
  protected static class $DataTarget {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Labeled_$DataTarget__label_$Name_$layouts_LAYOUTLIST_lit___58_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-1002, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-1001, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1000, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Labeled_$DataTarget__label_$Name_$layouts_LAYOUTLIST_lit___58_, tmp);
	}
    protected static final void _init_prod__Empty_$DataTarget__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(-1003, 0);
      builder.addAlternative(RascalRascal.prod__Empty_$DataTarget__, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Labeled_$DataTarget__label_$Name_$layouts_LAYOUTLIST_lit___58_(builder);
      
        _init_prod__Empty_$DataTarget__(builder);
      
    }
  }
	
  protected static class $RealLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-1006, 1, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null);
      tmp[0] = new ListStackNode(-1004, 0, regular__iter__char_class___range__48_57, new CharStackNode(-1005, 0, new char[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new OptionalStackNode(-1014, 4, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(-1015, 0, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[3] = new ListStackNode(-1012, 3, regular__iter__char_class___range__48_57, new CharStackNode(-1013, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new OptionalStackNode(-1010, 2, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode(-1011, 0, new char[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[1] = new CharStackNode(-1009, 1, new char[][]{{69,69},{101,101}}, null, null);
      tmp[0] = new ListStackNode(-1007, 0, regular__iter__char_class___range__48_57, new CharStackNode(-1008, 0, new char[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new OptionalStackNode(-1026, 6, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(-1027, 0, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[5] = new ListStackNode(-1024, 5, regular__iter__char_class___range__48_57, new CharStackNode(-1025, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[4] = new OptionalStackNode(-1022, 4, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode(-1023, 0, new char[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[3] = new CharStackNode(-1021, 3, new char[][]{{69,69},{101,101}}, null, null);
      tmp[2] = new ListStackNode(-1019, 2, regular__iter_star__char_class___range__48_57, new CharStackNode(-1020, 0, new char[][]{{48,57}}, null, null), false, null, null);
      tmp[1] = new LiteralStackNode(-1018, 1, prod__lit___46__char_class___range__46_46_, new char[] {46}, null, null);
      tmp[0] = new ListStackNode(-1016, 0, regular__iter__char_class___range__48_57, new CharStackNode(-1017, 0, new char[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode(-1028, 0, prod__lit___46__char_class___range__46_46_, new char[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new char[][]{{46,46}})}, null);
      tmp[1] = new ListStackNode(-1029, 1, regular__iter__char_class___range__48_57, new CharStackNode(-1030, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new OptionalStackNode(-1031, 2, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(-1032, 0, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[6];
      
      tmp[5] = new OptionalStackNode(-1048, 5, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(-1049, 0, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[4] = new ListStackNode(-1046, 4, regular__iter__char_class___range__48_57, new CharStackNode(-1047, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[3] = new OptionalStackNode(-1044, 3, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode(-1045, 0, new char[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[2] = new CharStackNode(-1043, 2, new char[][]{{69,69},{101,101}}, null, null);
      tmp[1] = new ListStackNode(-1041, 1, regular__iter__char_class___range__48_57, new CharStackNode(-1042, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new LiteralStackNode(-1040, 0, prod__lit___46__char_class___range__46_46_, new char[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new char[][]{{46,46}})}, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[4];
      
      tmp[3] = new OptionalStackNode(-1038, 3, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(-1039, 0, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[2] = new ListStackNode(-1036, 2, regular__iter_star__char_class___range__48_57, new CharStackNode(-1037, 0, new char[][]{{48,57}}, null, null), false, null, null);
      tmp[1] = new LiteralStackNode(-1035, 1, prod__lit___46__char_class___range__46_46_, new char[] {46}, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {46})});
      tmp[0] = new ListStackNode(-1033, 0, regular__iter__char_class___range__48_57, new CharStackNode(-1034, 0, new char[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__$RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
    }
  }
	
  protected static class $layouts_$default$ {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$layouts_$default$__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(-1052, 0);
      builder.addAlternative(RascalRascal.prod__$layouts_$default$__, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$layouts_$default$__(builder);
      
    }
  }
	
  protected static class $Replacement {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Unconditional_$Replacement__replacementExpression_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1055, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Unconditional_$Replacement__replacementExpression_$Expression_, tmp);
	}
    protected static final void _init_prod__Conditional_$Replacement__replacementExpression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode(-1060, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1061, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1062, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1063, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1064, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1059, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1058, 2, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new char[] {119,104,101,110}, null, null);
      tmp[1] = new NonTerminalStackNode(-1057, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1056, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Conditional_$Replacement__replacementExpression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Unconditional_$Replacement__replacementExpression_$Expression_(builder);
      
        _init_prod__Conditional_$Replacement__replacementExpression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Assoc {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Right_$Assoc__lit_right_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1077, 0, prod__lit_right__char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_, new char[] {114,105,103,104,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Right_$Assoc__lit_right_, tmp);
	}
    protected static final void _init_prod__NonAssociative_$Assoc__lit_non_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1078, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new char[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__NonAssociative_$Assoc__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__Left_$Assoc__lit_left_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1079, 0, prod__lit_left__char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_, new char[] {108,101,102,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Left_$Assoc__lit_left_, tmp);
	}
    protected static final void _init_prod__Associative_$Assoc__lit_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1080, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new char[] {97,115,115,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__Associative_$Assoc__lit_assoc_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Right_$Assoc__lit_right_(builder);
      
        _init_prod__NonAssociative_$Assoc__lit_non_assoc_(builder);
      
        _init_prod__Left_$Assoc__lit_left_(builder);
      
        _init_prod__Associative_$Assoc__lit_assoc_(builder);
      
    }
  }
	
  protected static class $RegExpModifier {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode(-1083, 0, regular__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115, new CharStackNode(-1084, 0, new char[][]{{100,100},{105,105},{109,109},{115,115}}, null, null), false, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_(builder);
      
    }
  }
	
  protected static class $MidStringChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MidStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(-1088, 2, new char[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode(-1086, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode(-1087, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode(-1085, 0, new char[][]{{62,62}}, null, null);
      builder.addAlternative(RascalRascal.prod__$MidStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$MidStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $RegExp {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(-1089, 0, new char[][]{{0,46},{48,59},{61,61},{63,91},{93,65535}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_, tmp);
	}
    protected static final void _init_prod__$RegExp__char_class___range__60_60_expression_$Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(-1092, 2, new char[][]{{62,62}}, null, null);
      tmp[1] = new NonTerminalStackNode(-1091, 1, "$Expression", null, null);
      tmp[0] = new CharStackNode(-1090, 0, new char[][]{{60,60}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__char_class___range__60_60_expression_$Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101, tmp);
	}
    protected static final void _init_prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-1098, 4, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[3] = new ListStackNode(-1096, 3, regular__iter_star__$NamedRegExp, new NonTerminalStackNode(-1097, 0, "$NamedRegExp", null, null), false, null, null);
      tmp[2] = new LiteralStackNode(-1095, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-1094, 1, "$Name", null, null);
      tmp[0] = new LiteralStackNode(-1093, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_, tmp);
	}
    protected static final void _init_prod__$RegExp__lit___60_$Name_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-1101, 2, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode(-1100, 1, "$Name", null, null);
      tmp[0] = new LiteralStackNode(-1099, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__lit___60_$Name_lit___62_, tmp);
	}
    protected static final void _init_prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-1103, 1, new char[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode(-1102, 0, new char[][]{{92,92}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__$RegExp__$Backslash_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1104, 0, "$Backslash", null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__$Backslash_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_(builder);
      
        _init_prod__$RegExp__char_class___range__60_60_expression_$Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101(builder);
      
        _init_prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_(builder);
      
        _init_prod__$RegExp__lit___60_$Name_lit___62_(builder);
      
        _init_prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
        _init_prod__$RegExp__$Backslash_(builder);
      
    }
  }
	
  protected static class $PathPart {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NonInterpolated_$PathPart__pathChars_$PathChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1112, 0, "$PathChars", null, null);
      builder.addAlternative(RascalRascal.prod__NonInterpolated_$PathPart__pathChars_$PathChars_, tmp);
	}
    protected static final void _init_prod__Interpolated_$PathPart__pre_$PrePathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-1117, 4, "$PathTail", null, null);
      tmp[3] = new NonTerminalStackNode(-1116, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1115, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-1114, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1113, 0, "$PrePathChars", null, null);
      builder.addAlternative(RascalRascal.prod__Interpolated_$PathPart__pre_$PrePathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__NonInterpolated_$PathPart__pathChars_$PathChars_(builder);
      
        _init_prod__Interpolated_$PathPart__pre_$PrePathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_(builder);
      
    }
  }
	
  protected static class $StringTemplate {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DoWhile_$StringTemplate__lit_do_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[19];
      
      tmp[18] = new LiteralStackNode(-1157, 18, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[17] = new NonTerminalStackNode(-1156, 17, "$layouts_LAYOUTLIST", null, null);
      tmp[16] = new NonTerminalStackNode(-1155, 16, "$Expression", null, null);
      tmp[15] = new NonTerminalStackNode(-1154, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new LiteralStackNode(-1153, 14, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[13] = new NonTerminalStackNode(-1152, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode(-1151, 12, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new char[] {119,104,105,108,101}, null, null);
      tmp[11] = new NonTerminalStackNode(-1150, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-1149, 10, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[9] = new NonTerminalStackNode(-1148, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new SeparatedListStackNode(-1145, 8, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1146, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1147, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode(-1144, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-1143, 6, "$StringMiddle", null, null);
      tmp[5] = new NonTerminalStackNode(-1142, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1139, 4, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1140, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1141, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-1138, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1137, 2, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode(-1136, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1135, 0, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new char[] {100,111}, null, null);
      builder.addAlternative(RascalRascal.prod__DoWhile_$StringTemplate__lit_do_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__While_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode(-1178, 16, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(-1177, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(-1174, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1175, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1176, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(-1173, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(-1172, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(-1171, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-1168, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1169, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1170, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(-1167, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-1166, 8, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(-1165, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1164, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-1163, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1162, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-1161, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1160, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-1159, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1158, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new char[] {119,104,105,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__While_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__IfThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[29];
      
      tmp[28] = new LiteralStackNode(-1244, 28, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[27] = new NonTerminalStackNode(-1243, 27, "$layouts_LAYOUTLIST", null, null);
      tmp[26] = new SeparatedListStackNode(-1240, 26, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1241, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1242, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[25] = new NonTerminalStackNode(-1239, 25, "$layouts_LAYOUTLIST", null, null);
      tmp[24] = new NonTerminalStackNode(-1238, 24, "$StringMiddle", null, null);
      tmp[23] = new NonTerminalStackNode(-1237, 23, "$layouts_LAYOUTLIST", null, null);
      tmp[22] = new SeparatedListStackNode(-1234, 22, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1235, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1236, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[21] = new NonTerminalStackNode(-1233, 21, "$layouts_LAYOUTLIST", null, null);
      tmp[20] = new LiteralStackNode(-1232, 20, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[19] = new NonTerminalStackNode(-1231, 19, "$layouts_LAYOUTLIST", null, null);
      tmp[18] = new LiteralStackNode(-1230, 18, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new char[] {101,108,115,101}, null, null);
      tmp[17] = new NonTerminalStackNode(-1229, 17, "$layouts_LAYOUTLIST", null, null);
      tmp[16] = new LiteralStackNode(-1228, 16, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(-1227, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(-1224, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1225, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1226, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(-1223, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(-1222, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(-1221, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-1218, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1219, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1220, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(-1217, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-1216, 8, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(-1215, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1214, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-1213, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1208, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1209, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1210, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1211, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1212, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1207, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1206, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-1205, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1204, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new char[] {105,102}, null, null);
      builder.addAlternative(RascalRascal.prod__IfThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__For_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode(-1203, 16, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(-1202, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(-1199, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1200, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1201, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(-1198, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(-1197, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(-1196, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-1193, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1194, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1195, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(-1192, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-1191, 8, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(-1190, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1189, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-1188, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1183, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1184, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1185, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1186, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1187, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1182, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1181, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-1180, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1179, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new char[] {102,111,114}, null, null);
      builder.addAlternative(RascalRascal.prod__For_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__IfThen_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode(-1269, 16, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(-1268, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(-1265, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1266, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1267, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(-1264, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(-1263, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(-1262, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-1259, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1260, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1261, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(-1258, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-1257, 8, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(-1256, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1255, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-1254, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1249, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1250, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1251, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1252, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1253, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1248, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1247, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-1246, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1245, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new char[] {105,102}, null, null);
      builder.addAlternative(RascalRascal.prod__IfThen_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__DoWhile_$StringTemplate__lit_do_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__While_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__IfThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__For_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__IfThen_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class $DecimalIntegerLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode(-1271, 1, regular__iter_star__char_class___range__48_57, new CharStackNode(-1272, 0, new char[][]{{48,57}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode(-1270, 0, new char[][]{{49,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__$DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1273, 0, prod__lit_0__char_class___range__48_48_, new char[] {48}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      builder.addAlternative(RascalRascal.prod__$DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__$DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class $LAYOUT {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$LAYOUT__char_class___range__9_10_range__13_13_range__32_32_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(-1288, 0, new char[][]{{9,10},{13,13},{32,32}}, null, null);
      builder.addAlternative(RascalRascal.prod__$LAYOUT__char_class___range__9_10_range__13_13_range__32_32_, tmp);
	}
    protected static final void _init_prod__$LAYOUT__$Comment_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1289, 0, "$Comment", null, null);
      builder.addAlternative(RascalRascal.prod__$LAYOUT__$Comment_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$LAYOUT__char_class___range__9_10_range__13_13_range__32_32_(builder);
      
        _init_prod__$LAYOUT__$Comment_(builder);
      
    }
  }
	
  protected static class $Word {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Word__conditional__iter__char_class___range__0_8_range__11_12_range__14_31_range__33_65535__not_follow__char_class___range__0_8_range__11_12_range__14_31_range__33_65535_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode(-1286, 0, regular__iter__char_class___range__0_8_range__11_12_range__14_31_range__33_65535, new CharStackNode(-1287, 0, new char[][]{{0,8},{11,12},{14,31},{33,65535}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{0,8},{11,12},{14,31},{33,65535}})});
      builder.addAlternative(RascalRascal.prod__$Word__conditional__iter__char_class___range__0_8_range__11_12_range__14_31_range__33_65535__not_follow__char_class___range__0_8_range__11_12_range__14_31_range__33_65535_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$Word__conditional__iter__char_class___range__0_8_range__11_12_range__14_31_range__33_65535__not_follow__char_class___range__0_8_range__11_12_range__14_31_range__33_65535_(builder);
      
    }
  }
	
  protected static class $Body {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Toplevels_$Body__toplevels_iter_star_seps__$Toplevel__$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode(-1283, 0, regular__iter_star_seps__$Toplevel__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1284, 0, "$Toplevel", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1285, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalRascal.prod__Toplevels_$Body__toplevels_iter_star_seps__$Toplevel__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Toplevels_$Body__toplevels_iter_star_seps__$Toplevel__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Import {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Syntax_$Import__syntax_$SyntaxDefinition_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1304, 0, "$SyntaxDefinition", null, null);
      builder.addAlternative(RascalRascal.prod__Syntax_$Import__syntax_$SyntaxDefinition_, tmp);
	}
    protected static final void _init_prod__Default_$Import__lit_import_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-1309, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(-1308, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1307, 2, "$ImportedModule", null, null);
      tmp[1] = new NonTerminalStackNode(-1306, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1305, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new char[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Import__lit_import_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Extend_$Import__lit_extend_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-1314, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(-1313, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1312, 2, "$ImportedModule", null, null);
      tmp[1] = new NonTerminalStackNode(-1311, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1310, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new char[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__Extend_$Import__lit_extend_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Syntax_$Import__syntax_$SyntaxDefinition_(builder);
      
        _init_prod__Default_$Import__lit_import_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Extend_$Import__lit_extend_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_(builder);
      
    }
  }
	
  protected static class $UserType {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Parametric_$UserType__conditional__name_$QualifiedName__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-1302, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(-1301, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1296, 4, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1297, 0, "$Type", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1298, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1299, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1300, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1295, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1294, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-1293, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1292, 0, "$QualifiedName", null, new ICompletionFilter[] {new StringFollowRequirement(new char[] {91})});
      builder.addAlternative(RascalRascal.prod__Parametric_$UserType__conditional__name_$QualifiedName__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Name_$UserType__name_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1303, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__Name_$UserType__name_$QualifiedName_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Parametric_$UserType__conditional__name_$QualifiedName__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Name_$UserType__name_$QualifiedName_(builder);
      
    }
  }
	
  protected static class $FunctionBody {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$FunctionBody__lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-1325, 4, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode(-1324, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-1321, 2, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1322, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1323, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-1320, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1319, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$FunctionBody__lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$FunctionBody__lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class start__$Command {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__$Command__$layouts_LAYOUTLIST_top_$Command_$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-1335, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(-1334, 1, "$Command", null, null);
      tmp[0] = new NonTerminalStackNode(-1333, 0, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalRascal.prod__start__$Command__$layouts_LAYOUTLIST_top_$Command_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__start__$Command__$layouts_LAYOUTLIST_top_$Command_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $IntegerLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DecimalIntegerLiteral_$IntegerLiteral__decimal_$DecimalIntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1341, 0, "$DecimalIntegerLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__DecimalIntegerLiteral_$IntegerLiteral__decimal_$DecimalIntegerLiteral_, tmp);
	}
    protected static final void _init_prod__OctalIntegerLiteral_$IntegerLiteral__octal_$OctalIntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1342, 0, "$OctalIntegerLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__OctalIntegerLiteral_$IntegerLiteral__octal_$OctalIntegerLiteral_, tmp);
	}
    protected static final void _init_prod__HexIntegerLiteral_$IntegerLiteral__hex_$HexIntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1343, 0, "$HexIntegerLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__HexIntegerLiteral_$IntegerLiteral__hex_$HexIntegerLiteral_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__DecimalIntegerLiteral_$IntegerLiteral__decimal_$DecimalIntegerLiteral_(builder);
      
        _init_prod__OctalIntegerLiteral_$IntegerLiteral__octal_$OctalIntegerLiteral_(builder);
      
        _init_prod__HexIntegerLiteral_$IntegerLiteral__hex_$HexIntegerLiteral_(builder);
      
    }
  }
	
  protected static class $Target {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Labeled_$Target__name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1339, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Labeled_$Target__name_$Name_, tmp);
	}
    protected static final void _init_prod__Empty_$Target__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(-1340, 0);
      builder.addAlternative(RascalRascal.prod__Empty_$Target__, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Labeled_$Target__name_$Name_(builder);
      
        _init_prod__Empty_$Target__(builder);
      
    }
  }
	
  protected static class $SyntaxDefinition {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Language_$SyntaxDefinition__start_$Start_$layouts_LAYOUTLIST_lit_syntax_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode(-1364, 10, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode(-1363, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-1362, 8, "$Prod", null, null);
      tmp[7] = new NonTerminalStackNode(-1361, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1360, 6, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode(-1359, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1358, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-1357, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1356, 2, prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_, new char[] {115,121,110,116,97,120}, null, null);
      tmp[1] = new NonTerminalStackNode(-1355, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1354, 0, "$Start", null, null);
      builder.addAlternative(RascalRascal.prod__Language_$SyntaxDefinition__start_$Start_$layouts_LAYOUTLIST_lit_syntax_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Keyword_$SyntaxDefinition__lit_keyword_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-1373, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(-1372, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-1371, 6, "$Prod", null, null);
      tmp[5] = new NonTerminalStackNode(-1370, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-1369, 4, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode(-1368, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1367, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-1366, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1365, 0, prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_, new char[] {107,101,121,119,111,114,100}, null, null);
      builder.addAlternative(RascalRascal.prod__Keyword_$SyntaxDefinition__lit_keyword_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Lexical_$SyntaxDefinition__lit_lexical_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-1382, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(-1381, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-1380, 6, "$Prod", null, null);
      tmp[5] = new NonTerminalStackNode(-1379, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-1378, 4, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode(-1377, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1376, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-1375, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1374, 0, prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_, new char[] {108,101,120,105,99,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__Lexical_$SyntaxDefinition__lit_lexical_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Layout_$SyntaxDefinition__vis_$Visibility_$layouts_LAYOUTLIST_lit_layout_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode(-1393, 10, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode(-1392, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-1391, 8, "$Prod", null, null);
      tmp[7] = new NonTerminalStackNode(-1390, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1389, 6, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode(-1388, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1387, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-1386, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1385, 2, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new char[] {108,97,121,111,117,116}, null, null);
      tmp[1] = new NonTerminalStackNode(-1384, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1383, 0, "$Visibility", null, null);
      builder.addAlternative(RascalRascal.prod__Layout_$SyntaxDefinition__vis_$Visibility_$layouts_LAYOUTLIST_lit_layout_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Language_$SyntaxDefinition__start_$Start_$layouts_LAYOUTLIST_lit_syntax_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__Keyword_$SyntaxDefinition__lit_keyword_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__Lexical_$SyntaxDefinition__lit_lexical_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__Layout_$SyntaxDefinition__vis_$Visibility_$layouts_LAYOUTLIST_lit_layout_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
    }
  }
	
  protected static class $Kind {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__All_$Kind__lit_all_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1394, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new char[] {97,108,108}, null, null);
      builder.addAlternative(RascalRascal.prod__All_$Kind__lit_all_, tmp);
	}
    protected static final void _init_prod__Module_$Kind__lit_module_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1395, 0, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {109,111,100,117,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Module_$Kind__lit_module_, tmp);
	}
    protected static final void _init_prod__Variable_$Kind__lit_variable_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1396, 0, prod__lit_variable__char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_, new char[] {118,97,114,105,97,98,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Variable_$Kind__lit_variable_, tmp);
	}
    protected static final void _init_prod__Rule_$Kind__lit_rule_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1397, 0, prod__lit_rule__char_class___range__114_114_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {114,117,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Rule_$Kind__lit_rule_, tmp);
	}
    protected static final void _init_prod__Anno_$Kind__lit_anno_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1398, 0, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new char[] {97,110,110,111}, null, null);
      builder.addAlternative(RascalRascal.prod__Anno_$Kind__lit_anno_, tmp);
	}
    protected static final void _init_prod__Data_$Kind__lit_data_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1399, 0, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new char[] {100,97,116,97}, null, null);
      builder.addAlternative(RascalRascal.prod__Data_$Kind__lit_data_, tmp);
	}
    protected static final void _init_prod__Tag_$Kind__lit_tag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1400, 0, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new char[] {116,97,103}, null, null);
      builder.addAlternative(RascalRascal.prod__Tag_$Kind__lit_tag_, tmp);
	}
    protected static final void _init_prod__Function_$Kind__lit_function_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1401, 0, prod__lit_function__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_, new char[] {102,117,110,99,116,105,111,110}, null, null);
      builder.addAlternative(RascalRascal.prod__Function_$Kind__lit_function_, tmp);
	}
    protected static final void _init_prod__View_$Kind__lit_view_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1402, 0, prod__lit_view__char_class___range__118_118_char_class___range__105_105_char_class___range__101_101_char_class___range__119_119_, new char[] {118,105,101,119}, null, null);
      builder.addAlternative(RascalRascal.prod__View_$Kind__lit_view_, tmp);
	}
    protected static final void _init_prod__Alias_$Kind__lit_alias_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1403, 0, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new char[] {97,108,105,97,115}, null, null);
      builder.addAlternative(RascalRascal.prod__Alias_$Kind__lit_alias_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__All_$Kind__lit_all_(builder);
      
        _init_prod__Module_$Kind__lit_module_(builder);
      
        _init_prod__Variable_$Kind__lit_variable_(builder);
      
        _init_prod__Rule_$Kind__lit_rule_(builder);
      
        _init_prod__Anno_$Kind__lit_anno_(builder);
      
        _init_prod__Data_$Kind__lit_data_(builder);
      
        _init_prod__Tag_$Kind__lit_tag_(builder);
      
        _init_prod__Function_$Kind__lit_function_(builder);
      
        _init_prod__View_$Kind__lit_view_(builder);
      
        _init_prod__Alias_$Kind__lit_alias_(builder);
      
    }
  }
	
  protected static class $Mapping__$Expression {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$Mapping__$Expression__from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-1416, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-1415, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1414, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-1413, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1412, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Mapping__$Expression__from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$Mapping__$Expression__from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_(builder);
      
    }
  }
	
  protected static class $PostProtocolChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PostProtocolChars__lit___62_$URLChars_lit___58_47_47_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-1425, 2, prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_, new char[] {58,47,47}, null, null);
      tmp[1] = new NonTerminalStackNode(-1424, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode(-1423, 0, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      builder.addAlternative(RascalRascal.prod__$PostProtocolChars__lit___62_$URLChars_lit___58_47_47_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$PostProtocolChars__lit___62_$URLChars_lit___58_47_47_(builder);
      
    }
  }
	
  protected static class $MidPathChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MidPathChars__lit___62_$URLChars_lit___60_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-1444, 2, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(-1443, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode(-1442, 0, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      builder.addAlternative(RascalRascal.prod__$MidPathChars__lit___62_$URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$MidPathChars__lit___62_$URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class $Char {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Char__$OctalEscapeSequence__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1445, 0, "$OctalEscapeSequence", null, null);
      builder.addAlternative(RascalRascal.prod__$Char__$OctalEscapeSequence__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__$Char__$UnicodeEscape__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1446, 0, "$UnicodeEscape", null, null);
      builder.addAlternative(RascalRascal.prod__$Char__$UnicodeEscape__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_65535__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(-1447, 0, new char[][]{{0,31},{33,33},{35,38},{40,44},{46,59},{61,61},{63,90},{94,65535}}, null, null);
      builder.addAlternative(RascalRascal.prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_65535__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__$Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-1449, 1, new char[][]{{32,32},{34,34},{39,39},{45,45},{60,60},{62,62},{91,93},{98,98},{102,102},{110,110},{114,114},{116,116}}, null, null);
      tmp[0] = new LiteralStackNode(-1448, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__$Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$Char__$OctalEscapeSequence__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__$Char__$UnicodeEscape__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_65535__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__$Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $PrePathChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PrePathChars__$URLChars_lit___60_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new LiteralStackNode(-1451, 1, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      tmp[0] = new NonTerminalStackNode(-1450, 0, "$URLChars", null, null);
      builder.addAlternative(RascalRascal.prod__$PrePathChars__$URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$PrePathChars__$URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class $Strategy {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__BottomUpBreak_$Strategy__lit_bottom_up_break_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1456, 0, prod__lit_bottom_up_break__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new char[] {98,111,116,116,111,109,45,117,112,45,98,114,101,97,107}, null, null);
      builder.addAlternative(RascalRascal.prod__BottomUpBreak_$Strategy__lit_bottom_up_break_, tmp);
	}
    protected static final void _init_prod__Outermost_$Strategy__lit_outermost_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1457, 0, prod__lit_outermost__char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_, new char[] {111,117,116,101,114,109,111,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Outermost_$Strategy__lit_outermost_, tmp);
	}
    protected static final void _init_prod__BottomUp_$Strategy__lit_bottom_up_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1458, 0, prod__lit_bottom_up__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_, new char[] {98,111,116,116,111,109,45,117,112}, null, null);
      builder.addAlternative(RascalRascal.prod__BottomUp_$Strategy__lit_bottom_up_, tmp);
	}
    protected static final void _init_prod__Innermost_$Strategy__lit_innermost_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1459, 0, prod__lit_innermost__char_class___range__105_105_char_class___range__110_110_char_class___range__110_110_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_, new char[] {105,110,110,101,114,109,111,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Innermost_$Strategy__lit_innermost_, tmp);
	}
    protected static final void _init_prod__TopDownBreak_$Strategy__lit_top_down_break_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1460, 0, prod__lit_top_down_break__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new char[] {116,111,112,45,100,111,119,110,45,98,114,101,97,107}, null, null);
      builder.addAlternative(RascalRascal.prod__TopDownBreak_$Strategy__lit_top_down_break_, tmp);
	}
    protected static final void _init_prod__TopDown_$Strategy__lit_top_down_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1461, 0, prod__lit_top_down__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_, new char[] {116,111,112,45,100,111,119,110}, null, null);
      builder.addAlternative(RascalRascal.prod__TopDown_$Strategy__lit_top_down_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__BottomUpBreak_$Strategy__lit_bottom_up_break_(builder);
      
        _init_prod__Outermost_$Strategy__lit_outermost_(builder);
      
        _init_prod__BottomUp_$Strategy__lit_bottom_up_(builder);
      
        _init_prod__Innermost_$Strategy__lit_innermost_(builder);
      
        _init_prod__TopDownBreak_$Strategy__lit_top_down_break_(builder);
      
        _init_prod__TopDown_$Strategy__lit_top_down_(builder);
      
    }
  }
	
  protected static class $ModuleParameters {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$ModuleParameters__lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-1481, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-1480, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-1475, 2, regular__iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1476, 0, "$TypeVar", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1477, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1478, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1479, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-1474, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1473, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$ModuleParameters__lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$ModuleParameters__lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class $RascalKeywords {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RascalKeywords__lit_solve_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1482, 0, prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_, new char[] {115,111,108,118,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_solve_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_rel_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1483, 0, prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new char[] {114,101,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_rel_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_false_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1484, 0, prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new char[] {102,97,108,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_false_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_any_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1485, 0, prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_, new char[] {97,110,121}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_any_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_all_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1486, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new char[] {97,108,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_all_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_real_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1488, 0, prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_, new char[] {114,101,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_real_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_finally_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1487, 0, prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_, new char[] {102,105,110,97,108,108,121}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_finally_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_bool_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1489, 0, prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_, new char[] {98,111,111,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_bool_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_datetime_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1490, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new char[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_datetime_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_while_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1493, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new char[] {119,104,105,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_while_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_case_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1492, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new char[] {99,97,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_case_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_layout_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1491, 0, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new char[] {108,97,121,111,117,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_layout_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_num_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1494, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new char[] {110,117,109}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_num_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_set_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1495, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new char[] {115,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_set_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_bag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1496, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new char[] {98,97,103}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_bag_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1497, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new char[] {97,115,115,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_assoc_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_for_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1498, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new char[] {102,111,114}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_for_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_bracket_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1499, 0, prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_, new char[] {98,114,97,99,107,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_bracket_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_fun_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1500, 0, prod__lit_fun__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_, new char[] {102,117,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_fun_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_visit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1501, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new char[] {118,105,115,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_visit_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_return_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1506, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new char[] {114,101,116,117,114,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_return_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_else_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1505, 0, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new char[] {101,108,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_else_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_in_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1504, 0, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new char[] {105,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_in_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_it_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1503, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new char[] {105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_it_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_join_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1502, 0, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new char[] {106,111,105,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_join_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_if_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1507, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new char[] {105,102}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_if_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_non_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1508, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new char[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_repeat_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1509, 0, prod__lit_repeat__char_class___range__114_114_char_class___range__101_101_char_class___range__112_112_char_class___range__101_101_char_class___range__97_97_char_class___range__116_116_, new char[] {114,101,112,101,97,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_repeat_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_one_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1510, 0, prod__lit_one__char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_, new char[] {111,110,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_one_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_str_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1511, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new char[] {115,116,114}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_str_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_node_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1512, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new char[] {110,111,100,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_node_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_catch_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1513, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new char[] {99,97,116,99,104}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_catch_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1515, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new char[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_type_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_notin_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1514, 0, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new char[] {110,111,116,105,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_notin_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_extend_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1517, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new char[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_extend_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_append_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1516, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new char[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_append_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_data_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1518, 0, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new char[] {100,97,116,97}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_data_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_tag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1519, 0, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new char[] {116,97,103}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_tag_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_dynamic_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1520, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new char[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_dynamic_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_list_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1521, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new char[] {108,105,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_list_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_non_terminal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1522, 0, prod__lit_non_terminal__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_, new char[] {110,111,110,45,116,101,114,109,105,110,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_non_terminal_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_value_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1523, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new char[] {118,97,108,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_value_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__$BasicType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1524, 0, "$BasicType", null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__$BasicType_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_try_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1525, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new char[] {116,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_try_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_void_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1526, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new char[] {118,111,105,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_void_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_public_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1527, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new char[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_public_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_anno_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1528, 0, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new char[] {97,110,110,111}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_anno_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_insert_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1529, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new char[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_insert_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_loc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1531, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new char[] {108,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_loc_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_assert_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1530, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new char[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_assert_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_import_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1532, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new char[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_import_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_start_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1533, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new char[] {115,116,97,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_start_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_test_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1534, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new char[] {116,101,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_test_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_map_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1535, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new char[] {109,97,112}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_map_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_true_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1536, 0, prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_, new char[] {116,114,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_true_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_private_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1537, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new char[] {112,114,105,118,97,116,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_private_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_module_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1538, 0, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {109,111,100,117,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_module_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_throws_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1539, 0, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new char[] {116,104,114,111,119,115}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_throws_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_default_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1540, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new char[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_default_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_alias_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1541, 0, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new char[] {97,108,105,97,115}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_alias_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_throw_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1542, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new char[] {116,104,114,111,119}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_throw_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_rule_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1543, 0, prod__lit_rule__char_class___range__114_114_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {114,117,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_rule_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_switch_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1544, 0, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new char[] {115,119,105,116,99,104}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_switch_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_fail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1545, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new char[] {102,97,105,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_fail_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_int_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1546, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new char[] {105,110,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_int_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_constructor_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1547, 0, prod__lit_constructor__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_, new char[] {99,111,110,115,116,114,117,99,116,111,114}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_constructor_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_tuple_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1548, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new char[] {116,117,112,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_tuple_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$RascalKeywords__lit_solve_(builder);
      
        _init_prod__$RascalKeywords__lit_rel_(builder);
      
        _init_prod__$RascalKeywords__lit_false_(builder);
      
        _init_prod__$RascalKeywords__lit_any_(builder);
      
        _init_prod__$RascalKeywords__lit_all_(builder);
      
        _init_prod__$RascalKeywords__lit_real_(builder);
      
        _init_prod__$RascalKeywords__lit_finally_(builder);
      
        _init_prod__$RascalKeywords__lit_bool_(builder);
      
        _init_prod__$RascalKeywords__lit_datetime_(builder);
      
        _init_prod__$RascalKeywords__lit_while_(builder);
      
        _init_prod__$RascalKeywords__lit_case_(builder);
      
        _init_prod__$RascalKeywords__lit_layout_(builder);
      
        _init_prod__$RascalKeywords__lit_num_(builder);
      
        _init_prod__$RascalKeywords__lit_set_(builder);
      
        _init_prod__$RascalKeywords__lit_bag_(builder);
      
        _init_prod__$RascalKeywords__lit_assoc_(builder);
      
        _init_prod__$RascalKeywords__lit_for_(builder);
      
        _init_prod__$RascalKeywords__lit_bracket_(builder);
      
        _init_prod__$RascalKeywords__lit_fun_(builder);
      
        _init_prod__$RascalKeywords__lit_visit_(builder);
      
        _init_prod__$RascalKeywords__lit_return_(builder);
      
        _init_prod__$RascalKeywords__lit_else_(builder);
      
        _init_prod__$RascalKeywords__lit_in_(builder);
      
        _init_prod__$RascalKeywords__lit_it_(builder);
      
        _init_prod__$RascalKeywords__lit_join_(builder);
      
        _init_prod__$RascalKeywords__lit_if_(builder);
      
        _init_prod__$RascalKeywords__lit_non_assoc_(builder);
      
        _init_prod__$RascalKeywords__lit_repeat_(builder);
      
        _init_prod__$RascalKeywords__lit_one_(builder);
      
        _init_prod__$RascalKeywords__lit_str_(builder);
      
        _init_prod__$RascalKeywords__lit_node_(builder);
      
        _init_prod__$RascalKeywords__lit_catch_(builder);
      
        _init_prod__$RascalKeywords__lit_type_(builder);
      
        _init_prod__$RascalKeywords__lit_notin_(builder);
      
        _init_prod__$RascalKeywords__lit_extend_(builder);
      
        _init_prod__$RascalKeywords__lit_append_(builder);
      
        _init_prod__$RascalKeywords__lit_data_(builder);
      
        _init_prod__$RascalKeywords__lit_tag_(builder);
      
        _init_prod__$RascalKeywords__lit_dynamic_(builder);
      
        _init_prod__$RascalKeywords__lit_list_(builder);
      
        _init_prod__$RascalKeywords__lit_non_terminal_(builder);
      
        _init_prod__$RascalKeywords__lit_value_(builder);
      
        _init_prod__$RascalKeywords__$BasicType_(builder);
      
        _init_prod__$RascalKeywords__lit_try_(builder);
      
        _init_prod__$RascalKeywords__lit_void_(builder);
      
        _init_prod__$RascalKeywords__lit_public_(builder);
      
        _init_prod__$RascalKeywords__lit_anno_(builder);
      
        _init_prod__$RascalKeywords__lit_insert_(builder);
      
        _init_prod__$RascalKeywords__lit_loc_(builder);
      
        _init_prod__$RascalKeywords__lit_assert_(builder);
      
        _init_prod__$RascalKeywords__lit_import_(builder);
      
        _init_prod__$RascalKeywords__lit_start_(builder);
      
        _init_prod__$RascalKeywords__lit_test_(builder);
      
        _init_prod__$RascalKeywords__lit_map_(builder);
      
        _init_prod__$RascalKeywords__lit_true_(builder);
      
        _init_prod__$RascalKeywords__lit_private_(builder);
      
        _init_prod__$RascalKeywords__lit_module_(builder);
      
        _init_prod__$RascalKeywords__lit_throws_(builder);
      
        _init_prod__$RascalKeywords__lit_default_(builder);
      
        _init_prod__$RascalKeywords__lit_alias_(builder);
      
        _init_prod__$RascalKeywords__lit_throw_(builder);
      
        _init_prod__$RascalKeywords__lit_rule_(builder);
      
        _init_prod__$RascalKeywords__lit_switch_(builder);
      
        _init_prod__$RascalKeywords__lit_fail_(builder);
      
        _init_prod__$RascalKeywords__lit_int_(builder);
      
        _init_prod__$RascalKeywords__lit_constructor_(builder);
      
        _init_prod__$RascalKeywords__lit_tuple_(builder);
      
    }
  }
	
  protected static class $NamedRegExp {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$NamedRegExp__lit___60_$Name_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-1552, 2, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode(-1551, 1, "$Name", null, null);
      tmp[0] = new LiteralStackNode(-1550, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__$NamedRegExp__lit___60_$Name_lit___62_, tmp);
	}
    protected static final void _init_prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-1554, 1, new char[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode(-1553, 0, new char[][]{{92,92}}, null, null);
      builder.addAlternative(RascalRascal.prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__$NamedRegExp__$NamedBackslash_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1555, 0, "$NamedBackslash", null, null);
      builder.addAlternative(RascalRascal.prod__$NamedRegExp__$NamedBackslash_, tmp);
	}
    protected static final void _init_prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(-1556, 0, new char[][]{{0,46},{48,59},{61,61},{63,91},{93,65535}}, null, null);
      builder.addAlternative(RascalRascal.prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$NamedRegExp__lit___60_$Name_lit___62_(builder);
      
        _init_prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
        _init_prod__$NamedRegExp__$NamedBackslash_(builder);
      
        _init_prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_(builder);
      
    }
  }
	
  protected static class $RegExpLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RegExpLiteral__lit___47_iter_star__$RegExp_lit___47_$RegExpModifier_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[4];
      
      tmp[3] = new NonTerminalStackNode(-1573, 3, "$RegExpModifier", null, null);
      tmp[2] = new LiteralStackNode(-1572, 2, prod__lit___47__char_class___range__47_47_, new char[] {47}, null, null);
      tmp[1] = new ListStackNode(-1570, 1, regular__iter_star__$RegExp, new NonTerminalStackNode(-1571, 0, "$RegExp", null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-1569, 0, prod__lit___47__char_class___range__47_47_, new char[] {47}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExpLiteral__lit___47_iter_star__$RegExp_lit___47_$RegExpModifier_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$RegExpLiteral__lit___47_iter_star__$RegExp_lit___47_$RegExpModifier_(builder);
      
    }
  }
	
  protected static class $OctalEscapeSequence {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$OctalEscapeSequence__lit___92_char_class___range__48_51_char_class___range__48_55_char_class___range__48_55_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[4];
      
      tmp[3] = new CharStackNode(-1611, 3, new char[][]{{48,55}}, null, null);
      tmp[2] = new CharStackNode(-1610, 2, new char[][]{{48,55}}, null, null);
      tmp[1] = new CharStackNode(-1609, 1, new char[][]{{48,51}}, null, null);
      tmp[0] = new LiteralStackNode(-1608, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__$OctalEscapeSequence__lit___92_char_class___range__48_51_char_class___range__48_55_char_class___range__48_55_, tmp);
	}
    protected static final void _init_prod__$OctalEscapeSequence__lit___92_char_class___range__48_55_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(-1614, 2, new char[][]{{48,55}}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,55}})});
      tmp[1] = new CharStackNode(-1613, 1, new char[][]{{48,55}}, null, null);
      tmp[0] = new LiteralStackNode(-1612, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__$OctalEscapeSequence__lit___92_char_class___range__48_55_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_, tmp);
	}
    protected static final void _init_prod__$OctalEscapeSequence__lit___92_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-1616, 1, new char[][]{{48,55}}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,55}})});
      tmp[0] = new LiteralStackNode(-1615, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__$OctalEscapeSequence__lit___92_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$OctalEscapeSequence__lit___92_char_class___range__48_51_char_class___range__48_55_char_class___range__48_55_(builder);
      
        _init_prod__$OctalEscapeSequence__lit___92_char_class___range__48_55_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_(builder);
      
        _init_prod__$OctalEscapeSequence__lit___92_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_(builder);
      
    }
  }
	
  protected static class $HexIntegerLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_conditional__iter__char_class___range__48_57_range__65_70_range__97_102__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new ListStackNode(-1623, 2, regular__iter__char_class___range__48_57_range__65_70_range__97_102, new CharStackNode(-1624, 0, new char[][]{{48,57},{65,70},{97,102}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode(-1622, 1, new char[][]{{88,88},{120,120}}, null, null);
      tmp[0] = new CharStackNode(-1621, 0, new char[][]{{48,48}}, null, null);
      builder.addAlternative(RascalRascal.prod__$HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_conditional__iter__char_class___range__48_57_range__65_70_range__97_102__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_conditional__iter__char_class___range__48_57_range__65_70_range__97_102__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class $PostStringChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PostStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(-1620, 2, new char[][]{{34,34}}, null, null);
      tmp[1] = new ListStackNode(-1618, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode(-1619, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode(-1617, 0, new char[][]{{62,62}}, null, null);
      builder.addAlternative(RascalRascal.prod__$PostStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$PostStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $ModuleActuals {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$ModuleActuals__lit___91_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-1635, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-1634, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-1629, 2, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1630, 0, "$Type", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1631, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1632, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1633, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-1628, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1627, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$ModuleActuals__lit___91_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$ModuleActuals__lit___91_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class $PathChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PathChars__$URLChars_char_class___range__124_124_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-1637, 1, new char[][]{{124,124}}, null, null);
      tmp[0] = new NonTerminalStackNode(-1636, 0, "$URLChars", null, null);
      builder.addAlternative(RascalRascal.prod__$PathChars__$URLChars_char_class___range__124_124_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$PathChars__$URLChars_char_class___range__124_124_(builder);
      
    }
  }
	
  protected static class $DateTimeLiteral {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TimeLiteral_$DateTimeLiteral__time_$JustTime_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1643, 0, "$JustTime", null, null);
      builder.addAlternative(RascalRascal.prod__TimeLiteral_$DateTimeLiteral__time_$JustTime_, tmp);
	}
    protected static final void _init_prod__DateLiteral_$DateTimeLiteral__date_$JustDate_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1644, 0, "$JustDate", null, null);
      builder.addAlternative(RascalRascal.prod__DateLiteral_$DateTimeLiteral__date_$JustDate_, tmp);
	}
    protected static final void _init_prod__DateAndTimeLiteral_$DateTimeLiteral__dateAndTime_$DateAndTime_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1645, 0, "$DateAndTime", null, null);
      builder.addAlternative(RascalRascal.prod__DateAndTimeLiteral_$DateTimeLiteral__dateAndTime_$DateAndTime_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__TimeLiteral_$DateTimeLiteral__time_$JustTime_(builder);
      
        _init_prod__DateLiteral_$DateTimeLiteral__date_$JustDate_(builder);
      
        _init_prod__DateAndTimeLiteral_$DateTimeLiteral__dateAndTime_$DateAndTime_(builder);
      
    }
  }
	
  protected static class $BasicType {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Set_$BasicType__lit_set_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1657, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new char[] {115,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Set_$BasicType__lit_set_, tmp);
	}
    protected static final void _init_prod__DateTime_$BasicType__lit_datetime_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1658, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new char[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(RascalRascal.prod__DateTime_$BasicType__lit_datetime_, tmp);
	}
    protected static final void _init_prod__String_$BasicType__lit_str_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1659, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new char[] {115,116,114}, null, null);
      builder.addAlternative(RascalRascal.prod__String_$BasicType__lit_str_, tmp);
	}
    protected static final void _init_prod__ReifiedReifiedType_$BasicType__lit_reified_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1660, 0, prod__lit_reified__char_class___range__114_114_char_class___range__101_101_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__100_100_, new char[] {114,101,105,102,105,101,100}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedReifiedType_$BasicType__lit_reified_, tmp);
	}
    protected static final void _init_prod__ReifiedType_$BasicType__lit_type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1661, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new char[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedType_$BasicType__lit_type_, tmp);
	}
    protected static final void _init_prod__ReifiedTypeParameter_$BasicType__lit_parameter_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1662, 0, prod__lit_parameter__char_class___range__112_112_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new char[] {112,97,114,97,109,101,116,101,114}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedTypeParameter_$BasicType__lit_parameter_, tmp);
	}
    protected static final void _init_prod__Bag_$BasicType__lit_bag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1663, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new char[] {98,97,103}, null, null);
      builder.addAlternative(RascalRascal.prod__Bag_$BasicType__lit_bag_, tmp);
	}
    protected static final void _init_prod__Real_$BasicType__lit_real_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1664, 0, prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_, new char[] {114,101,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__Real_$BasicType__lit_real_, tmp);
	}
    protected static final void _init_prod__Node_$BasicType__lit_node_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1665, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new char[] {110,111,100,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Node_$BasicType__lit_node_, tmp);
	}
    protected static final void _init_prod__Int_$BasicType__lit_int_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1666, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new char[] {105,110,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Int_$BasicType__lit_int_, tmp);
	}
    protected static final void _init_prod__Map_$BasicType__lit_map_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1667, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new char[] {109,97,112}, null, null);
      builder.addAlternative(RascalRascal.prod__Map_$BasicType__lit_map_, tmp);
	}
    protected static final void _init_prod__Tuple_$BasicType__lit_tuple_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1668, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new char[] {116,117,112,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Tuple_$BasicType__lit_tuple_, tmp);
	}
    protected static final void _init_prod__ReifiedFunction_$BasicType__lit_fun_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1669, 0, prod__lit_fun__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_, new char[] {102,117,110}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedFunction_$BasicType__lit_fun_, tmp);
	}
    protected static final void _init_prod__Loc_$BasicType__lit_loc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1673, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new char[] {108,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__Loc_$BasicType__lit_loc_, tmp);
	}
    protected static final void _init_prod__Value_$BasicType__lit_value_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1674, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new char[] {118,97,108,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Value_$BasicType__lit_value_, tmp);
	}
    protected static final void _init_prod__List_$BasicType__lit_list_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1670, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new char[] {108,105,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__List_$BasicType__lit_list_, tmp);
	}
    protected static final void _init_prod__ReifiedNonTerminal_$BasicType__lit_non_terminal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1671, 0, prod__lit_non_terminal__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_, new char[] {110,111,110,45,116,101,114,109,105,110,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedNonTerminal_$BasicType__lit_non_terminal_, tmp);
	}
    protected static final void _init_prod__Num_$BasicType__lit_num_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1672, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new char[] {110,117,109}, null, null);
      builder.addAlternative(RascalRascal.prod__Num_$BasicType__lit_num_, tmp);
	}
    protected static final void _init_prod__Relation_$BasicType__lit_rel_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1675, 0, prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new char[] {114,101,108}, null, null);
      builder.addAlternative(RascalRascal.prod__Relation_$BasicType__lit_rel_, tmp);
	}
    protected static final void _init_prod__Void_$BasicType__lit_void_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1676, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new char[] {118,111,105,100}, null, null);
      builder.addAlternative(RascalRascal.prod__Void_$BasicType__lit_void_, tmp);
	}
    protected static final void _init_prod__ReifiedConstructor_$BasicType__lit_constructor_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1677, 0, prod__lit_constructor__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_, new char[] {99,111,110,115,116,114,117,99,116,111,114}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedConstructor_$BasicType__lit_constructor_, tmp);
	}
    protected static final void _init_prod__Bool_$BasicType__lit_bool_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1678, 0, prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_, new char[] {98,111,111,108}, null, null);
      builder.addAlternative(RascalRascal.prod__Bool_$BasicType__lit_bool_, tmp);
	}
    protected static final void _init_prod__ReifiedAdt_$BasicType__lit_adt_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1679, 0, prod__lit_adt__char_class___range__97_97_char_class___range__100_100_char_class___range__116_116_, new char[] {97,100,116}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedAdt_$BasicType__lit_adt_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Set_$BasicType__lit_set_(builder);
      
        _init_prod__DateTime_$BasicType__lit_datetime_(builder);
      
        _init_prod__String_$BasicType__lit_str_(builder);
      
        _init_prod__ReifiedReifiedType_$BasicType__lit_reified_(builder);
      
        _init_prod__ReifiedType_$BasicType__lit_type_(builder);
      
        _init_prod__ReifiedTypeParameter_$BasicType__lit_parameter_(builder);
      
        _init_prod__Bag_$BasicType__lit_bag_(builder);
      
        _init_prod__Real_$BasicType__lit_real_(builder);
      
        _init_prod__Node_$BasicType__lit_node_(builder);
      
        _init_prod__Int_$BasicType__lit_int_(builder);
      
        _init_prod__Map_$BasicType__lit_map_(builder);
      
        _init_prod__Tuple_$BasicType__lit_tuple_(builder);
      
        _init_prod__ReifiedFunction_$BasicType__lit_fun_(builder);
      
        _init_prod__Loc_$BasicType__lit_loc_(builder);
      
        _init_prod__Value_$BasicType__lit_value_(builder);
      
        _init_prod__List_$BasicType__lit_list_(builder);
      
        _init_prod__ReifiedNonTerminal_$BasicType__lit_non_terminal_(builder);
      
        _init_prod__Num_$BasicType__lit_num_(builder);
      
        _init_prod__Relation_$BasicType__lit_rel_(builder);
      
        _init_prod__Void_$BasicType__lit_void_(builder);
      
        _init_prod__ReifiedConstructor_$BasicType__lit_constructor_(builder);
      
        _init_prod__Bool_$BasicType__lit_bool_(builder);
      
        _init_prod__ReifiedAdt_$BasicType__lit_adt_(builder);
      
    }
  }
	
  protected static class $FunctionDeclaration {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Conditional_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[0] = new NonTerminalStackNode(-1709, 0, "$Tags", null, null);
      tmp[1] = new NonTerminalStackNode(-1710, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1711, 2, "$Visibility", null, null);
      tmp[3] = new NonTerminalStackNode(-1712, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1713, 4, "$Signature", null, null);
      tmp[5] = new NonTerminalStackNode(-1714, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1715, 6, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(-1716, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-1717, 8, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode(-1718, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-1719, 10, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new char[] {119,104,101,110}, null, null);
      tmp[11] = new NonTerminalStackNode(-1720, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(-1721, 12, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1722, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1723, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1724, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1725, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[13] = new NonTerminalStackNode(-1726, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new LiteralStackNode(-1727, 14, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__Conditional_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode(-1708, 10, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode(-1707, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-1706, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode(-1705, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1704, 6, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode(-1703, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1702, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode(-1701, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1700, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-1699, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1698, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-1734, 6, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode(-1733, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1732, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode(-1731, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1730, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-1729, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1728, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Default_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_body_$FunctionBody__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-1741, 6, "$FunctionBody", null, null);
      tmp[5] = new NonTerminalStackNode(-1740, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1739, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode(-1738, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1737, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-1736, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1735, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_body_$FunctionBody__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Conditional_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__Expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__Abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Default_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_body_$FunctionBody__tag__Foldable(builder);
      
    }
  }
	
  protected static class $Variant {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NAryConstructor_$Variant__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-1756, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-1755, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1750, 4, regular__iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1751, 0, "$TypeArg", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1752, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1753, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1754, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-1749, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1748, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-1747, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1746, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__NAryConstructor_$Variant__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__NAryConstructor_$Variant__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class start__$PreModule {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__$PreModule__$layouts_LAYOUTLIST_top_$PreModule_$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-1762, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(-1761, 1, "$PreModule", null, null);
      tmp[0] = new NonTerminalStackNode(-1760, 0, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalRascal.prod__start__$PreModule__$layouts_LAYOUTLIST_top_$PreModule_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__start__$PreModule__$layouts_LAYOUTLIST_top_$PreModule_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Declarator {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$Declarator__type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode(-1786, 2, regular__iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1787, 0, "$Variable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1788, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1789, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1790, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-1785, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1784, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Declarator__type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$Declarator__type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $StructuredType {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$StructuredType__basicType_$BasicType_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_arguments_iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-1836, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(-1835, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1830, 4, regular__iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1831, 0, "$TypeArg", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1832, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1833, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1834, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1829, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1828, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-1827, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1826, 0, "$BasicType", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$StructuredType__basicType_$BasicType_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_arguments_iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$StructuredType__basicType_$BasicType_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_arguments_iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class $TimePartNoTZ {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new OptionalStackNode(-1848, 8, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(-1849, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(-1850, 0, new char[][]{{44,44},{46,46}}, null, null), new CharStackNode(-1851, 1, new char[][]{{48,57}}, null, null), new OptionalStackNode(-1852, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(-1853, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(-1854, 0, new char[][]{{48,57}}, null, null), new OptionalStackNode(-1855, 1, regular__opt__char_class___range__48_57, new CharStackNode(-1856, 0, new char[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[7] = new CharStackNode(-1847, 7, new char[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode(-1846, 6, new char[][]{{48,53}}, null, null);
      tmp[5] = new LiteralStackNode(-1845, 5, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[4] = new CharStackNode(-1844, 4, new char[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode(-1843, 3, new char[][]{{48,53}}, null, null);
      tmp[2] = new LiteralStackNode(-1842, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new CharStackNode(-1841, 1, new char[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(-1840, 0, new char[][]{{48,50}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new OptionalStackNode(-1863, 6, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(-1864, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(-1865, 0, new char[][]{{44,44},{46,46}}, null, null), new CharStackNode(-1866, 1, new char[][]{{48,57}}, null, null), new OptionalStackNode(-1867, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(-1868, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(-1869, 0, new char[][]{{48,57}}, null, null), new OptionalStackNode(-1870, 1, regular__opt__char_class___range__48_57, new CharStackNode(-1871, 0, new char[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[5] = new CharStackNode(-1862, 5, new char[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode(-1861, 4, new char[][]{{48,53}}, null, null);
      tmp[3] = new CharStackNode(-1860, 3, new char[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode(-1859, 2, new char[][]{{48,53}}, null, null);
      tmp[1] = new CharStackNode(-1858, 1, new char[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(-1857, 0, new char[][]{{48,50}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(builder);
      
        _init_prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class $Name {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalKeywords_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SequenceStackNode(-1879, 0, regular__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new AbstractStackNode[]{new CharStackNode(-1880, 0, new char[][]{{65,90},{95,95},{97,122}}, new IEnterFilter[] {new CharPrecedeRestriction(new char[][]{{65,90},{95,95},{97,122}})}, null), new ListStackNode(-1881, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(-1882, 0, new char[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})})}, null, new ICompletionFilter[] {new StringMatchRestriction(new char[] {105,109,112,111,114,116}), new StringMatchRestriction(new char[] {105,110}), new StringMatchRestriction(new char[] {97,108,108}), new StringMatchRestriction(new char[] {102,97,108,115,101}), new StringMatchRestriction(new char[] {97,110,110,111}), new StringMatchRestriction(new char[] {98,114,97,99,107,101,116}), new StringMatchRestriction(new char[] {108,97,121,111,117,116}), new StringMatchRestriction(new char[] {106,111,105,110}), new StringMatchRestriction(new char[] {100,97,116,97}), new StringMatchRestriction(new char[] {105,116}), new StringMatchRestriction(new char[] {115,119,105,116,99,104}), new StringMatchRestriction(new char[] {114,101,116,117,114,110}), new StringMatchRestriction(new char[] {99,97,115,101}), new StringMatchRestriction(new char[] {115,116,114}), new StringMatchRestriction(new char[] {119,104,105,108,101}), new StringMatchRestriction(new char[] {100,121,110,97,109,105,99}), new StringMatchRestriction(new char[] {115,111,108,118,101}), new StringMatchRestriction(new char[] {110,111,116,105,110}), new StringMatchRestriction(new char[] {105,110,115,101,114,116}), new StringMatchRestriction(new char[] {101,108,115,101}), new StringMatchRestriction(new char[] {116,121,112,101}), new StringMatchRestriction(new char[] {99,97,116,99,104}), new StringMatchRestriction(new char[] {116,114,121}), new StringMatchRestriction(new char[] {110,117,109}), new StringMatchRestriction(new char[] {110,111,100,101}), new StringMatchRestriction(new char[] {112,114,105,118,97,116,101}), new StringMatchRestriction(new char[] {102,105,110,97,108,108,121}), new StringMatchRestriction(new char[] {116,114,117,101}), new StringMatchRestriction(new char[] {98,97,103}), new StringMatchRestriction(new char[] {118,111,105,100}), new StringMatchRestriction(new char[] {110,111,110,45,97,115,115,111,99}), new StringMatchRestriction(new char[] {97,115,115,111,99}), new StringMatchRestriction(new char[] {116,101,115,116}), new StringMatchRestriction(new char[] {105,102}), new StringMatchRestriction(new char[] {102,97,105,108}), new StringMatchRestriction(new char[] {108,105,115,116}), new StringMatchRestriction(new char[] {114,101,97,108}), new StringMatchRestriction(new char[] {114,101,108}), new StringMatchRestriction(new char[] {116,97,103}), new StringMatchRestriction(new char[] {101,120,116,101,110,100}), new StringMatchRestriction(new char[] {97,112,112,101,110,100}), new StringMatchRestriction(new char[] {114,101,112,101,97,116}), new StringMatchRestriction(new char[] {116,104,114,111,119}), new StringMatchRestriction(new char[] {111,110,101}), new StringMatchRestriction(new char[] {115,116,97,114,116}), new StringMatchRestriction(new char[] {115,101,116}), new StringMatchRestriction(new char[] {109,111,100,117,108,101}), new StringMatchRestriction(new char[] {97,110,121}), new StringMatchRestriction(new char[] {105,110,116}), new StringMatchRestriction(new char[] {112,117,98,108,105,99}), new StringMatchRestriction(new char[] {98,111,111,108}), new StringMatchRestriction(new char[] {118,97,108,117,101}), new StringMatchRestriction(new char[] {114,117,108,101}), new StringMatchRestriction(new char[] {110,111,110,45,116,101,114,109,105,110,97,108}), new StringMatchRestriction(new char[] {102,117,110}), new StringMatchRestriction(new char[] {99,111,110,115,116,114,117,99,116,111,114}), new StringMatchRestriction(new char[] {100,97,116,101,116,105,109,101}), new StringMatchRestriction(new char[] {97,115,115,101,114,116}), new StringMatchRestriction(new char[] {108,111,99}), new StringMatchRestriction(new char[] {100,101,102,97,117,108,116}), new StringMatchRestriction(new char[] {116,104,114,111,119,115}), new StringMatchRestriction(new char[] {116,117,112,108,101}), new StringMatchRestriction(new char[] {102,111,114}), new StringMatchRestriction(new char[] {118,105,115,105,116}), new StringMatchRestriction(new char[] {97,108,105,97,115}), new StringMatchRestriction(new char[] {109,97,112})});
      builder.addAlternative(RascalRascal.prod__$Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalKeywords_, tmp);
	}
    protected static final void _init_prod__$Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new ListStackNode(-1885, 2, regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(-1886, 0, new char[][]{{45,45},{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{45,45},{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode(-1884, 1, new char[][]{{65,90},{95,95},{97,122}}, null, null);
      tmp[0] = new CharStackNode(-1883, 0, new char[][]{{92,92}}, null, null);
      builder.addAlternative(RascalRascal.prod__$Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalKeywords_(builder);
      
        _init_prod__$Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class $Start {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Absent_$Start__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(-1888, 0);
      builder.addAlternative(RascalRascal.prod__Absent_$Start__, tmp);
	}
    protected static final void _init_prod__Present_$Start__lit_start_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1889, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new char[] {115,116,97,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Present_$Start__lit_start_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Absent_$Start__(builder);
      
        _init_prod__Present_$Start__lit_start_(builder);
      
    }
  }
	
  protected static class $Formals {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$Formals__formals_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode(-1892, 0, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1893, 0, "$Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1894, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1895, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1896, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Formals__formals_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$Formals__formals_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Tags {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$Tags__tags_iter_star_seps__$Tag__$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode(-1897, 0, regular__iter_star_seps__$Tag__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1898, 0, "$Tag", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1899, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Tags__tags_iter_star_seps__$Tag__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$Tags__tags_iter_star_seps__$Tag__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class start__$Commands {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__$Commands__$layouts_LAYOUTLIST_top_$Commands_$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-1902, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(-1901, 1, "$Commands", null, null);
      tmp[0] = new NonTerminalStackNode(-1900, 0, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalRascal.prod__start__$Commands__$layouts_LAYOUTLIST_top_$Commands_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__start__$Commands__$layouts_LAYOUTLIST_top_$Commands_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $CaseInsensitiveStringConstant {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$CaseInsensitiveStringConstant__lit___39_iter_star__$StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-1906, 2, prod__lit___39__char_class___range__39_39_, new char[] {39}, null, null);
      tmp[1] = new ListStackNode(-1904, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode(-1905, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-1903, 0, prod__lit___39__char_class___range__39_39_, new char[] {39}, null, null);
      builder.addAlternative(RascalRascal.prod__$CaseInsensitiveStringConstant__lit___39_iter_star__$StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$CaseInsensitiveStringConstant__lit___39_iter_star__$StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $Backslash {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(-1907, 0, new char[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{47,47},{60,60},{62,62},{92,92}})});
      builder.addAlternative(RascalRascal.prod__$Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
    }
  }
	
  protected static class $JustDate {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$JustDate__lit___36_$DatePart_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new NonTerminalStackNode(-1930, 1, "$DatePart", null, null);
      tmp[0] = new LiteralStackNode(-1929, 0, prod__lit___36__char_class___range__36_36_, new char[] {36}, null, null);
      builder.addAlternative(RascalRascal.prod__$JustDate__lit___36_$DatePart_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$JustDate__lit___36_$DatePart_(builder);
      
    }
  }
	
  protected static class $PathTail {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Mid_$PathTail__mid_$MidPathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-1940, 4, "$PathTail", null, null);
      tmp[3] = new NonTerminalStackNode(-1939, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1938, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-1937, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1936, 0, "$MidPathChars", null, null);
      builder.addAlternative(RascalRascal.prod__Mid_$PathTail__mid_$MidPathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_, tmp);
	}
    protected static final void _init_prod__Post_$PathTail__post_$PostPathChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1941, 0, "$PostPathChars", null, null);
      builder.addAlternative(RascalRascal.prod__Post_$PathTail__post_$PostPathChars_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Mid_$PathTail__mid_$MidPathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_(builder);
      
        _init_prod__Post_$PathTail__post_$PostPathChars_(builder);
      
    }
  }
	
  protected static class $MidProtocolChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MidProtocolChars__lit___62_$URLChars_lit___60_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-1944, 2, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(-1943, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode(-1942, 0, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      builder.addAlternative(RascalRascal.prod__$MidProtocolChars__lit___62_$URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$MidProtocolChars__lit___62_$URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class $PatternWithAction {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Arbitrary_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-1956, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-1955, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1954, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-1953, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1952, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__Arbitrary_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement_, tmp);
	}
    protected static final void _init_prod__Replacing_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_replacement_$Replacement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-1961, 4, "$Replacement", null, null);
      tmp[3] = new NonTerminalStackNode(-1960, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1959, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new char[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-1958, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1957, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__Replacing_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_replacement_$Replacement_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Arbitrary_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement_(builder);
      
        _init_prod__Replacing_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_replacement_$Replacement_(builder);
      
    }
  }
	
  protected static class $DataTypeSelector {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Selector_$DataTypeSelector__sort_$QualifiedName_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_production_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-1966, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-1965, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1964, 2, prod__lit___46__char_class___range__46_46_, new char[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode(-1963, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1962, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__Selector_$DataTypeSelector__sort_$QualifiedName_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_production_$Name_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Selector_$DataTypeSelector__sort_$QualifiedName_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_production_$Name_(builder);
      
    }
  }
	
  protected static class $StringTail {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Post_$StringTail__post_$PostStringChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1967, 0, "$PostStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__Post_$StringTail__post_$PostStringChars_, tmp);
	}
    protected static final void _init_prod__MidTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-1972, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(-1971, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1970, 2, "$StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode(-1969, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1968, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__MidTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    protected static final void _init_prod__MidInterpolated_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-1977, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(-1976, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1975, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-1974, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1973, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__MidInterpolated_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Post_$StringTail__post_$PostStringChars_(builder);
      
        _init_prod__MidTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(builder);
      
        _init_prod__MidInterpolated_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(builder);
      
    }
  }
	
  protected static class start__$Module {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__$Module__$layouts_LAYOUTLIST_top_$Module_$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-1980, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(-1979, 1, "$Module", null, null);
      tmp[0] = new NonTerminalStackNode(-1978, 0, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalRascal.prod__start__$Module__$layouts_LAYOUTLIST_top_$Module_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__start__$Module__$layouts_LAYOUTLIST_top_$Module_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $StringConstant {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$StringConstant__lit___34_iter_star__$StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-1999, 2, prod__lit___34__char_class___range__34_34_, new char[] {34}, null, null);
      tmp[1] = new ListStackNode(-1997, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode(-1998, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-1996, 0, prod__lit___34__char_class___range__34_34_, new char[] {34}, null, null);
      builder.addAlternative(RascalRascal.prod__$StringConstant__lit___34_iter_star__$StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$StringConstant__lit___34_iter_star__$StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $LocalVariableDeclaration {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Dynamic_$LocalVariableDeclaration__lit_dynamic_$layouts_LAYOUTLIST_declarator_$Declarator_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2016, 2, "$Declarator", null, null);
      tmp[1] = new NonTerminalStackNode(-2015, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2014, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new char[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__Dynamic_$LocalVariableDeclaration__lit_dynamic_$layouts_LAYOUTLIST_declarator_$Declarator_, tmp);
	}
    protected static final void _init_prod__Default_$LocalVariableDeclaration__declarator_$Declarator_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2017, 0, "$Declarator", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$LocalVariableDeclaration__declarator_$Declarator_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Dynamic_$LocalVariableDeclaration__lit_dynamic_$layouts_LAYOUTLIST_declarator_$Declarator_(builder);
      
        _init_prod__Default_$LocalVariableDeclaration__declarator_$Declarator_(builder);
      
    }
  }
	
  protected static class $Parameters {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2040, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-2039, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2038, 2, "$Formals", null, null);
      tmp[1] = new NonTerminalStackNode(-2037, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2036, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__VarArgs_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___46_46_46_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2047, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-2046, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2045, 4, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new char[] {46,46,46}, null, null);
      tmp[3] = new NonTerminalStackNode(-2044, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2043, 2, "$Formals", null, null);
      tmp[1] = new NonTerminalStackNode(-2042, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2041, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__VarArgs_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___46_46_46_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__VarArgs_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___46_46_46_$layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class $DatePart {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[8];
      
      tmp[7] = new CharStackNode(-2055, 7, new char[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode(-2054, 6, new char[][]{{48,51}}, null, null);
      tmp[5] = new CharStackNode(-2053, 5, new char[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode(-2052, 4, new char[][]{{48,49}}, null, null);
      tmp[3] = new CharStackNode(-2051, 3, new char[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode(-2050, 2, new char[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(-2049, 1, new char[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(-2048, 0, new char[][]{{48,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[10];
      
      tmp[9] = new CharStackNode(-2065, 9, new char[][]{{48,57}}, null, null);
      tmp[8] = new CharStackNode(-2064, 8, new char[][]{{48,51}}, null, null);
      tmp[7] = new LiteralStackNode(-2063, 7, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      tmp[6] = new CharStackNode(-2062, 6, new char[][]{{48,57}}, null, null);
      tmp[5] = new CharStackNode(-2061, 5, new char[][]{{48,49}}, null, null);
      tmp[4] = new LiteralStackNode(-2060, 4, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      tmp[3] = new CharStackNode(-2059, 3, new char[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode(-2058, 2, new char[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(-2057, 1, new char[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(-2056, 0, new char[][]{{48,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_(builder);
      
        _init_prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class $HeaderKeyword {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$HeaderKeyword__lit_lexical_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2086, 0, prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_, new char[] {108,101,120,105,99,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_lexical_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_layout_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2087, 0, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new char[] {108,97,121,111,117,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_layout_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_extend_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2088, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new char[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_extend_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_syntax_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2089, 0, prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_, new char[] {115,121,110,116,97,120}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_syntax_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_keyword_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2090, 0, prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_, new char[] {107,101,121,119,111,114,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_keyword_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_import_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2091, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new char[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_import_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_start_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2092, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new char[] {115,116,97,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_start_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$HeaderKeyword__lit_lexical_(builder);
      
        _init_prod__$HeaderKeyword__lit_layout_(builder);
      
        _init_prod__$HeaderKeyword__lit_extend_(builder);
      
        _init_prod__$HeaderKeyword__lit_syntax_(builder);
      
        _init_prod__$HeaderKeyword__lit_keyword_(builder);
      
        _init_prod__$HeaderKeyword__lit_import_(builder);
      
        _init_prod__$HeaderKeyword__lit_start_(builder);
      
    }
  }
	
  protected static class $Assignable {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Tuple_$Assignable__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2120, 4, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode(-2119, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2114, 2, regular__iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2115, 0, "$Assignable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2116, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2117, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2118, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-2113, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2112, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__Tuple_$Assignable__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__Bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2125, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-2124, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2123, 2, "$Assignable", null, null);
      tmp[1] = new NonTerminalStackNode(-2122, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2121, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2136, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-2135, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2130, 4, regular__iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2131, 0, "$Assignable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2132, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2133, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2134, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-2129, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2128, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2127, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2126, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__FieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[0] = new NonTerminalStackNode(-2137, 0, "$Assignable", null, null);
      tmp[1] = new NonTerminalStackNode(-2138, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2139, 2, prod__lit___46__char_class___range__46_46_, new char[] {46}, null, null);
      tmp[3] = new NonTerminalStackNode(-2140, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2141, 4, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__FieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_, tmp);
	}
    protected static final void _init_prod__Variable_$Assignable__qualifiedName_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2152, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__Variable_$Assignable__qualifiedName_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2146, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2145, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2144, 2, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode(-2143, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2142, 0, "$Assignable", null, null);
      builder.addAlternative(RascalRascal.prod__Annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_, tmp);
	}
    protected static final void _init_prod__IfDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2151, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2150, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2149, 2, prod__lit___63__char_class___range__63_63_, new char[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(-2148, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2147, 0, "$Assignable", null, null);
      builder.addAlternative(RascalRascal.prod__IfDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_, tmp);
	}
    protected static final void _init_prod__Subscript_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscript_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2159, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(-2158, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2157, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2156, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2155, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-2154, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2153, 0, "$Assignable", null, null);
      builder.addAlternative(RascalRascal.prod__Subscript_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscript_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Tuple_$Assignable__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__Bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__Constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__FieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(builder);
      
        _init_prod__Variable_$Assignable__qualifiedName_$QualifiedName_(builder);
      
        _init_prod__Annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_(builder);
      
        _init_prod__IfDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_(builder);
      
        _init_prod__Subscript_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscript_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class $FunctionModifier {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Test_$FunctionModifier__lit_test_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2160, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new char[] {116,101,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Test_$FunctionModifier__lit_test_, tmp);
	}
    protected static final void _init_prod__Default_$FunctionModifier__lit_default_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2161, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new char[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$FunctionModifier__lit_default_, tmp);
	}
    protected static final void _init_prod__Java_$FunctionModifier__lit_java_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2162, 0, prod__lit_java__char_class___range__106_106_char_class___range__97_97_char_class___range__118_118_char_class___range__97_97_, new char[] {106,97,118,97}, null, null);
      builder.addAlternative(RascalRascal.prod__Java_$FunctionModifier__lit_java_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Test_$FunctionModifier__lit_test_(builder);
      
        _init_prod__Default_$FunctionModifier__lit_default_(builder);
      
        _init_prod__Java_$FunctionModifier__lit_java_(builder);
      
    }
  }
	
  protected static class $Assignment {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Addition_$Assignment__lit___43_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2166, 0, prod__lit___43_61__char_class___range__43_43_char_class___range__61_61_, new char[] {43,61}, null, null);
      builder.addAlternative(RascalRascal.prod__Addition_$Assignment__lit___43_61_, tmp);
	}
    protected static final void _init_prod__Product_$Assignment__lit___42_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2167, 0, prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_, new char[] {42,61}, null, null);
      builder.addAlternative(RascalRascal.prod__Product_$Assignment__lit___42_61_, tmp);
	}
    protected static final void _init_prod__Division_$Assignment__lit___47_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2168, 0, prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_, new char[] {47,61}, null, null);
      builder.addAlternative(RascalRascal.prod__Division_$Assignment__lit___47_61_, tmp);
	}
    protected static final void _init_prod__IfDefined_$Assignment__lit___63_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2169, 0, prod__lit___63_61__char_class___range__63_63_char_class___range__61_61_, new char[] {63,61}, null, null);
      builder.addAlternative(RascalRascal.prod__IfDefined_$Assignment__lit___63_61_, tmp);
	}
    protected static final void _init_prod__Default_$Assignment__lit___61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2170, 0, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Assignment__lit___61_, tmp);
	}
    protected static final void _init_prod__Intersection_$Assignment__lit___38_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2171, 0, prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_, new char[] {38,61}, null, null);
      builder.addAlternative(RascalRascal.prod__Intersection_$Assignment__lit___38_61_, tmp);
	}
    protected static final void _init_prod__Subtraction_$Assignment__lit___45_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2172, 0, prod__lit___45_61__char_class___range__45_45_char_class___range__61_61_, new char[] {45,61}, null, null);
      builder.addAlternative(RascalRascal.prod__Subtraction_$Assignment__lit___45_61_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Addition_$Assignment__lit___43_61_(builder);
      
        _init_prod__Product_$Assignment__lit___42_61_(builder);
      
        _init_prod__Division_$Assignment__lit___47_61_(builder);
      
        _init_prod__IfDefined_$Assignment__lit___63_61_(builder);
      
        _init_prod__Default_$Assignment__lit___61_(builder);
      
        _init_prod__Intersection_$Assignment__lit___38_61_(builder);
      
        _init_prod__Subtraction_$Assignment__lit___45_61_(builder);
      
    }
  }
	
  protected static class $ProtocolChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$ProtocolChars__char_class___range__124_124_$URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2165, 2, prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_, new char[] {58,47,47}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{9,10},{13,13},{32,32}})});
      tmp[1] = new NonTerminalStackNode(-2164, 1, "$URLChars", null, null);
      tmp[0] = new CharStackNode(-2163, 0, new char[][]{{124,124}}, null, null);
      builder.addAlternative(RascalRascal.prod__$ProtocolChars__char_class___range__124_124_$URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$ProtocolChars__char_class___range__124_124_$URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_(builder);
      
    }
  }
	
  protected static class $Field {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Name_$Field__fieldName_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2182, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Name_$Field__fieldName_$Name_, tmp);
	}
    protected static final void _init_prod__Index_$Field__fieldIndex_$IntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2183, 0, "$IntegerLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Index_$Field__fieldIndex_$IntegerLiteral_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Name_$Field__fieldName_$Name_(builder);
      
        _init_prod__Index_$Field__fieldIndex_$IntegerLiteral_(builder);
      
    }
  }
	
  protected static class $Comment {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10__tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(-2202, 2, new char[][]{{10,10}}, null, null);
      tmp[1] = new ListStackNode(-2200, 1, regular__iter_star__char_class___range__0_9_range__11_65535, new CharStackNode(-2201, 0, new char[][]{{0,9},{11,65535}}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-2199, 0, prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_, new char[] {47,47}, null, null);
      builder.addAlternative(RascalRascal.prod__$Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10__tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__$Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535_lit___42_47__tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2208, 2, prod__lit___42_47__char_class___range__42_42_char_class___range__47_47_, new char[] {42,47}, null, null);
      tmp[1] = new ListStackNode(-2204, 1, regular__iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535, new AlternativeStackNode(-2205, 0, regular__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535, new AbstractStackNode[]{new CharStackNode(-2206, 0, new char[][]{{42,42}}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{47,47}})}), new CharStackNode(-2207, 0, new char[][]{{0,41},{43,65535}}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-2203, 0, prod__lit___47_42__char_class___range__47_47_char_class___range__42_42_, new char[] {47,42}, null, null);
      builder.addAlternative(RascalRascal.prod__$Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535_lit___42_47__tag__category___67_111_109_109_101_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10__tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__$Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535_lit___42_47__tag__category___67_111_109_109_101_110_116(builder);
      
    }
  }
	
  protected static class $Label {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$Label__name_$Name_$layouts_LAYOUTLIST_lit___58_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2221, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-2220, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2219, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Label__name_$Name_$layouts_LAYOUTLIST_lit___58_, tmp);
	}
    protected static final void _init_prod__Empty_$Label__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(-2222, 0);
      builder.addAlternative(RascalRascal.prod__Empty_$Label__, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$Label__name_$Name_$layouts_LAYOUTLIST_lit___58_(builder);
      
        _init_prod__Empty_$Label__(builder);
      
    }
  }
	
  protected static class $ProtocolPart {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NonInterpolated_$ProtocolPart__protocolChars_$ProtocolChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2223, 0, "$ProtocolChars", null, null);
      builder.addAlternative(RascalRascal.prod__NonInterpolated_$ProtocolPart__protocolChars_$ProtocolChars_, tmp);
	}
    protected static final void _init_prod__Interpolated_$ProtocolPart__pre_$PreProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2228, 4, "$ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode(-2227, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2226, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2225, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2224, 0, "$PreProtocolChars", null, null);
      builder.addAlternative(RascalRascal.prod__Interpolated_$ProtocolPart__pre_$PreProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__NonInterpolated_$ProtocolPart__protocolChars_$ProtocolChars_(builder);
      
        _init_prod__Interpolated_$ProtocolPart__pre_$PreProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(builder);
      
    }
  }
	
  protected static class $PreModule {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_iter_star_seps__$Word__$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode(-2243, 4, regular__iter_star_seps__$Word__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2244, 0, "$Word", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2245, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-2242, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new EmptyStackNode(-2241, 2, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {108,101,120,105,99,97,108}), new StringFollowRestriction(new char[] {105,109,112,111,114,116}), new StringFollowRestriction(new char[] {115,116,97,114,116}), new StringFollowRestriction(new char[] {115,121,110,116,97,120}), new StringFollowRestriction(new char[] {101,120,116,101,110,100}), new StringFollowRestriction(new char[] {108,97,121,111,117,116}), new StringFollowRestriction(new char[] {107,101,121,119,111,114,100})});
      tmp[1] = new NonTerminalStackNode(-2240, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2239, 0, "$Header", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_iter_star_seps__$Word__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_iter_star_seps__$Word__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $TagString {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$TagString__lit___123_iter_star__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2264, 2, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[1] = new ListStackNode(-2258, 1, regular__iter_star__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535, new AlternativeStackNode(-2259, 0, regular__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535, new AbstractStackNode[]{new SequenceStackNode(-2260, 0, regular__seq___char_class___range__92_92_char_class___range__125_125, new AbstractStackNode[]{new CharStackNode(-2261, 0, new char[][]{{92,92}}, null, null), new CharStackNode(-2262, 1, new char[][]{{125,125}}, null, null)}, null, null), new CharStackNode(-2263, 0, new char[][]{{0,124},{126,65535}}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-2257, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__$TagString__lit___123_iter_star__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535_lit___125_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$TagString__lit___123_iter_star__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535_lit___125_(builder);
      
    }
  }
	
  protected static class $Nonterminal {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalReservedKeywords_not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode(-2266, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(-2267, 0, new char[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode(-2265, 0, new char[][]{{65,90}}, new IEnterFilter[] {new CharPrecedeRestriction(new char[][]{{65,90}})}, null);
      builder.addAlternative(RascalRascal.prod__$Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalReservedKeywords_not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalReservedKeywords_not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class $Mapping__$Pattern {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$Mapping__$Pattern__from_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2284, 4, "$Pattern", null, null);
      tmp[3] = new NonTerminalStackNode(-2283, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2282, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-2281, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2280, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Mapping__$Pattern__from_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Pattern_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$Mapping__$Pattern__from_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Pattern_(builder);
      
    }
  }
	
  protected static class $Expression {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TransitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2517, 2, prod__lit___43__char_class___range__43_43_, new char[] {43}, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {61})});
      tmp[1] = new NonTerminalStackNode(-2516, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2515, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__TransitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_, tmp);
	}
    protected static final void _init_prod__IfThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode(-2617, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode(-2616, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2615, 6, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[5] = new NonTerminalStackNode(-2614, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2613, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2612, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2611, 2, prod__lit___63__char_class___range__63_63_, new char[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(-2610, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2609, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__IfThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2553, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2552, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2551, 2, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new char[] {106,111,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode(-2550, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2549, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__And_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2662, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2661, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2660, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new char[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode(-2659, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2658, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__And_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__In_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2583, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2582, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2581, 2, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new char[] {105,110}, null, null);
      tmp[1] = new NonTerminalStackNode(-2580, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2579, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__In_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__List_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2319, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-2318, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2313, 2, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2314, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2315, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2316, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2317, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-2312, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2311, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__List_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2568, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2567, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2566, 2, prod__lit___38__char_class___range__38_38_, new char[] {38}, null, null);
      tmp[1] = new NonTerminalStackNode(-2565, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2564, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode(-2332, 0, "$Label", null, null);
      tmp[1] = new NonTerminalStackNode(-2333, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2334, 2, "$Visit", null, null);
      builder.addAlternative(RascalRascal.prod__Visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_, tmp);
	}
    protected static final void _init_prod__All_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2354, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-2353, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2348, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2349, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2350, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2351, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2352, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-2347, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2346, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2345, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2344, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new char[] {97,108,108}, null, null);
      builder.addAlternative(RascalRascal.prod__All_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__VoidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[0] = new NonTerminalStackNode(-2368, 0, "$Parameters", null, null);
      tmp[1] = new NonTerminalStackNode(-2369, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2370, 2, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[3] = new NonTerminalStackNode(-2371, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2372, 4, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2373, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2374, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode(-2375, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2376, 6, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      builder.addAlternative(RascalRascal.prod__VoidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__StepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-2367, 12, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[11] = new NonTerminalStackNode(-2366, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-2365, 10, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode(-2364, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-2363, 8, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new char[] {46,46}, null, null);
      tmp[7] = new NonTerminalStackNode(-2362, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2361, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-2360, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2359, 4, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null);
      tmp[3] = new NonTerminalStackNode(-2358, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2357, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2356, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2355, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__StepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2622, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2621, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2620, 2, prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_, new char[] {61,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2619, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2618, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2455, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2454, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2453, 2, prod__lit_has__char_class___range__104_104_char_class___range__97_97_char_class___range__115_115_, new char[] {104,97,115}, null, null);
      tmp[1] = new NonTerminalStackNode(-2452, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2451, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__FieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode(-2466, 10, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[9] = new NonTerminalStackNode(-2465, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-2464, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode(-2463, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2462, 6, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode(-2461, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2456, 0, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2457, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2458, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[3] = new NonTerminalStackNode(-2459, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2460, 4, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__FieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2391, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-2390, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2385, 2, regular__iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2386, 0, "$Mapping__$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2387, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2388, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2389, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-2384, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2383, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__ReifyType_$Expression__lit___35_$layouts_LAYOUTLIST_type_$Type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2394, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-2393, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2392, 0, prod__lit___35__char_class___range__35_35_, new char[] {35}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifyType_$Expression__lit___35_$layouts_LAYOUTLIST_type_$Type_, tmp);
	}
    protected static final void _init_prod__Is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2487, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2486, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2485, 2, prod__lit_is__char_class___range__105_105_char_class___range__115_115_, new char[] {105,115}, null, null);
      tmp[1] = new NonTerminalStackNode(-2484, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2483, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__Reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-2412, 12, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode(-2411, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-2406, 10, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2407, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2408, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2409, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2410, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(-2405, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-2404, 8, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode(-2403, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2402, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-2401, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2400, 4, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(-2399, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2398, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2397, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2396, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2667, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2666, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2665, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new char[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode(-2664, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2663, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2450, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-2449, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2444, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2445, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2446, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2447, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2448, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-2443, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2442, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2441, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2440, 0, prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_, new char[] {97,110,121}, null, null);
      builder.addAlternative(RascalRascal.prod__Any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__LessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2608, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2607, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2606, 2, prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_, new char[] {60,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2605, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2604, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__LessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__SetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-2538, 12, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[11] = new NonTerminalStackNode(-2537, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-2536, 10, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode(-2535, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-2534, 8, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(-2533, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2532, 6, "$Name", null, null);
      tmp[5] = new NonTerminalStackNode(-2531, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2530, 4, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      tmp[3] = new NonTerminalStackNode(-2529, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2528, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-2527, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2526, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__SetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-2298, 8, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[7] = new NonTerminalStackNode(-2297, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2296, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-2295, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2294, 4, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new char[] {46,46}, null, null);
      tmp[3] = new NonTerminalStackNode(-2293, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2292, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2291, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2290, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__Range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2548, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2547, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2546, 2, prod__lit___42__char_class___range__42_42_, new char[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode(-2545, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2544, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__TransitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2520, 2, prod__lit___42__char_class___range__42_42_, new char[] {42}, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {61})});
      tmp[1] = new NonTerminalStackNode(-2519, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2518, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__TransitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_, tmp);
	}
    protected static final void _init_prod__Division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2558, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2557, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2556, 2, prod__lit___47__char_class___range__47_47_, new char[] {47}, null, null);
      tmp[1] = new NonTerminalStackNode(-2555, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2554, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Literal_$Expression__literal_$Literal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2299, 0, "$Literal", null, null);
      builder.addAlternative(RascalRascal.prod__Literal_$Expression__literal_$Literal_, tmp);
	}
    protected static final void _init_prod__Negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2511, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2510, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2509, 0, prod__lit___33__char_class___range__33_33_, new char[] {33}, null, null);
      builder.addAlternative(RascalRascal.prod__Negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_, tmp);
	}
    protected static final void _init_prod__ReifiedType_$Expression__basicType_$BasicType_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2310, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-2309, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2304, 4, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2305, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2306, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2307, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2308, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-2303, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2302, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2301, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2300, 0, "$BasicType", null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedType_$Expression__basicType_$BasicType_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__CallOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[0] = new NonTerminalStackNode(-2320, 0, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2321, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2322, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-2323, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2324, 4, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2325, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2326, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2327, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2328, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode(-2329, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2330, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      builder.addAlternative(RascalRascal.prod__CallOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Comprehension_$Expression__comprehension_$Comprehension_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2331, 0, "$Comprehension", null, null);
      builder.addAlternative(RascalRascal.prod__Comprehension_$Expression__comprehension_$Comprehension_, tmp);
	}
    protected static final void _init_prod__Subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2498, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(-2497, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2492, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2493, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2494, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2495, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2496, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-2491, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2490, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-2489, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2488, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2343, 4, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode(-2342, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2337, 2, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2338, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2339, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2340, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2341, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-2336, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2335, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__Set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__LessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2603, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2602, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2601, 2, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {45})});
      tmp[1] = new NonTerminalStackNode(-2600, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2599, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__LessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__IfDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2632, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2631, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2630, 2, prod__lit___63__char_class___range__63_63_, new char[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(-2629, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2628, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__IfDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2657, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2656, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2655, 2, prod__lit___61_61_62__char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new char[] {61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2654, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2653, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2543, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2542, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2541, 2, prod__lit_o__char_class___range__111_111_, new char[] {111}, null, null);
      tmp[1] = new NonTerminalStackNode(-2540, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2539, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__NonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2627, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2626, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2625, 2, prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_, new char[] {33,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2624, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2623, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__NonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__AsType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-2508, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-2507, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2506, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-2505, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2504, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-2503, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2502, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__AsType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_, tmp);
	}
    protected static final void _init_prod__Bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2381, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-2380, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2379, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2378, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2377, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__FieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2471, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2470, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2469, 2, prod__lit___46__char_class___range__46_46_, new char[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode(-2468, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2467, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__FieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_, tmp);
	}
    protected static final void _init_prod__QualifiedName_$Expression__qualifiedName_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2382, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__QualifiedName_$Expression__qualifiedName_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[0] = new NonTerminalStackNode(-2569, 0, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2570, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2571, 2, prod__lit___43__char_class___range__43_43_, new char[] {43}, null, null);
      tmp[3] = new NonTerminalStackNode(-2572, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2573, 4, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__FieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2482, 6, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[5] = new NonTerminalStackNode(-2481, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2476, 4, regular__iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2477, 0, "$Field", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2478, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2479, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2480, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-2475, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2474, 2, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(-2473, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2472, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__FieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__NoMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2637, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2636, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2635, 2, prod__lit___33_58_61__char_class___range__33_33_char_class___range__58_58_char_class___range__61_61_, new char[] {33,58,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2634, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2633, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__NoMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__GetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2525, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2524, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2523, 2, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode(-2522, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2521, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__GetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__Equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2652, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2651, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2650, 2, prod__lit___60_61_61_62__char_class___range__60_60_char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new char[] {60,61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2649, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2648, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__IsDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2501, 2, prod__lit___63__char_class___range__63_63_, new char[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(-2500, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2499, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__IsDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_, tmp);
	}
    protected static final void _init_prod__Negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2514, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2513, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2512, 0, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      builder.addAlternative(RascalRascal.prod__Negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_, tmp);
	}
    protected static final void _init_prod__It_$Expression__lit_it_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2395, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new char[] {105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__It_$Expression__lit_it_, tmp);
	}
    protected static final void _init_prod__GreaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2593, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2592, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2591, 2, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2590, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2589, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__GreaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__NonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2419, 4, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode(-2418, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2415, 2, regular__iter_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2416, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2417, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-2414, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2413, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__NonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2563, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2562, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2561, 2, prod__lit___37__char_class___range__37_37_, new char[] {37}, null, null);
      tmp[1] = new NonTerminalStackNode(-2560, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2559, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__GreaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2598, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2597, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2596, 2, prod__lit___62_61__char_class___range__62_62_char_class___range__61_61_, new char[] {62,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2595, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2594, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__GreaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2642, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2641, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2640, 2, prod__lit___60_45__char_class___range__60_60_char_class___range__45_45_, new char[] {60,45}, null, null);
      tmp[1] = new NonTerminalStackNode(-2639, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2638, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__Enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2578, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2577, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2576, 2, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode(-2575, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2574, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-2430, 8, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode(-2429, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-2426, 6, regular__iter_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2427, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2428, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-2425, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2424, 4, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[3] = new NonTerminalStackNode(-2423, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2422, 2, "$Parameters", null, null);
      tmp[1] = new NonTerminalStackNode(-2421, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2420, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__Closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__NotIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2588, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2587, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2586, 2, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new char[] {110,111,116,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode(-2585, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2584, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__NotIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2439, 4, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode(-2438, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2433, 2, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2434, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2435, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2436, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2437, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-2432, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2431, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__Tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__Match_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2647, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2646, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2645, 2, prod__lit___58_61__char_class___range__58_58_char_class___range__61_61_, new char[] {58,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2644, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2643, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__Match_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__TransitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_(builder);
      
        _init_prod__IfThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__left(builder);
      
        _init_prod__Join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__And_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__In_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__List_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__Visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_(builder);
      
        _init_prod__All_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__VoidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__StepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__Has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__FieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__ReifyType_$Expression__lit___35_$layouts_LAYOUTLIST_type_$Type_(builder);
      
        _init_prod__Is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__Reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__Any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__LessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__SetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__TransitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_(builder);
      
        _init_prod__Division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__Literal_$Expression__literal_$Literal_(builder);
      
        _init_prod__Negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_(builder);
      
        _init_prod__ReifiedType_$Expression__basicType_$BasicType_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__CallOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Comprehension_$Expression__comprehension_$Comprehension_(builder);
      
        _init_prod__Subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__LessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__IfDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__Implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__Composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__NonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__AsType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_(builder);
      
        _init_prod__Bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__FieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(builder);
      
        _init_prod__QualifiedName_$Expression__qualifiedName_$QualifiedName_(builder);
      
        _init_prod__Addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__FieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__NoMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(builder);
      
        _init_prod__GetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__Equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__IsDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_(builder);
      
        _init_prod__Negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_(builder);
      
        _init_prod__It_$Expression__lit_it_(builder);
      
        _init_prod__GreaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__NonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__Modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__GreaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__Enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(builder);
      
        _init_prod__Subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__Closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__NotIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__Tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__Match_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(builder);
      
    }
  }
	
  protected static class $ImportedModule {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Actuals_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2691, 2, "$ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode(-2690, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2689, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__Actuals_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_, tmp);
	}
    protected static final void _init_prod__ActualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2696, 4, "$Renamings", null, null);
      tmp[3] = new NonTerminalStackNode(-2695, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2694, 2, "$ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode(-2693, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2692, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__ActualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_, tmp);
	}
    protected static final void _init_prod__Default_$ImportedModule__name_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2697, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$ImportedModule__name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Renamings_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_renamings_$Renamings_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2700, 2, "$Renamings", null, null);
      tmp[1] = new NonTerminalStackNode(-2699, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2698, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__Renamings_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_renamings_$Renamings_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Actuals_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_(builder);
      
        _init_prod__ActualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_(builder);
      
        _init_prod__Default_$ImportedModule__name_$QualifiedName_(builder);
      
        _init_prod__Renamings_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_renamings_$Renamings_(builder);
      
    }
  }
	
  protected static class $Commands {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__List_$Commands__commands_iter_seps__$Command__$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode(-2725, 0, regular__iter_seps__$Command__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2726, 0, "$Command", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2727, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      builder.addAlternative(RascalRascal.prod__List_$Commands__commands_iter_seps__$Command__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__List_$Commands__commands_iter_seps__$Command__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Header {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new SeparatedListStackNode(-2744, 6, regular__iter_star_seps__$Import__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2745, 0, "$Import", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2746, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode(-2743, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2742, 4, "$QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode(-2741, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2740, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode(-2739, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2738, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__Parameters_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_params_$ModuleParameters_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new SeparatedListStackNode(-2755, 8, regular__iter_star_seps__$Import__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2756, 0, "$Import", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2757, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode(-2754, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2753, 6, "$ModuleParameters", null, null);
      tmp[5] = new NonTerminalStackNode(-2752, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2751, 4, "$QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode(-2750, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2749, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode(-2748, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2747, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Parameters_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_params_$ModuleParameters_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_(builder);
      
        _init_prod__Parameters_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_params_$ModuleParameters_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $NonterminalLabel {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode(-2759, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(-2760, 0, new char[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode(-2758, 0, new char[][]{{97,122}}, null, null);
      builder.addAlternative(RascalRascal.prod__$NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class $Sym {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__EndOfLine_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___36_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode(-2769, 0, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-2770, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2771, 2, prod__lit___36__char_class___range__36_36_, new char[] {36}, null, null);
      builder.addAlternative(RascalRascal.prod__EndOfLine_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___36_, tmp);
	}
    protected static final void _init_prod__Parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2768, 2, "$Nonterminal", null, null);
      tmp[1] = new NonTerminalStackNode(-2767, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2766, 0, prod__lit___38__char_class___range__38_38_, new char[] {38}, null, null);
      builder.addAlternative(RascalRascal.prod__Parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_, tmp);
	}
    protected static final void _init_prod__Optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2774, 2, prod__lit___63__char_class___range__63_63_, new char[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(-2773, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2772, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_, tmp);
	}
    protected static final void _init_prod__CaseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2775, 0, "$CaseInsensitiveStringConstant", null, null);
      builder.addAlternative(RascalRascal.prod__CaseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_, tmp);
	}
    protected static final void _init_prod__IterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-2784, 8, prod__lit___43__char_class___range__43_43_, new char[] {43}, null, null);
      tmp[7] = new NonTerminalStackNode(-2783, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2782, 6, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode(-2781, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2780, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2779, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2778, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-2777, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2776, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__IterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_, tmp);
	}
    protected static final void _init_prod__Literal_$Sym__string_$StringConstant_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2785, 0, "$StringConstant", null, null);
      builder.addAlternative(RascalRascal.prod__Literal_$Sym__string_$StringConstant_, tmp);
	}
    protected static final void _init_prod__IterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2788, 2, prod__lit___42__char_class___range__42_42_, new char[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode(-2787, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2786, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__IterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__Unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2881, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2880, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2879, 2, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      tmp[1] = new NonTerminalStackNode(-2878, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2877, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__NotFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2876, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2875, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2874, 2, prod__lit___33_62_62__char_class___range__33_33_char_class___range__62_62_char_class___range__62_62_, new char[] {33,62,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2873, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2872, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__NotFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__StartOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2791, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-2790, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2789, 0, prod__lit___94__char_class___range__94_94_, new char[] {94}, null, null);
      builder.addAlternative(RascalRascal.prod__StartOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_, tmp);
	}
    protected static final void _init_prod__Empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2805, 2, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[1] = new NonTerminalStackNode(-2804, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2803, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Parametrized_$Sym__conditional__nonterminal_$Nonterminal__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[0] = new NonTerminalStackNode(-2792, 0, "$Nonterminal", null, new ICompletionFilter[] {new StringFollowRequirement(new char[] {91})});
      tmp[1] = new NonTerminalStackNode(-2793, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2794, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[3] = new NonTerminalStackNode(-2795, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2796, 4, regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2797, 0, "$Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2798, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2799, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2800, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-2801, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2802, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      builder.addAlternative(RascalRascal.prod__Parametrized_$Sym__conditional__nonterminal_$Nonterminal__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[0] = new LiteralStackNode(-2812, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2813, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2814, 2, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2815, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2816, 4, regular__iter_seps__$Sym__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2817, 0, "$Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2818, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-2819, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2820, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      builder.addAlternative(RascalRascal.prod__Sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2861, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2860, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2859, 2, prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_, new char[] {60,60}, null, null);
      tmp[1] = new NonTerminalStackNode(-2858, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2857, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__Nonterminal_$Sym__conditional__nonterminal_$Nonterminal__not_follow__lit___91_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2806, 0, "$Nonterminal", null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {91})});
      builder.addAlternative(RascalRascal.prod__Nonterminal_$Sym__conditional__nonterminal_$Nonterminal__not_follow__lit___91_, tmp);
	}
    protected static final void _init_prod__Column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2811, 4, "$IntegerLiteral", null, null);
      tmp[3] = new NonTerminalStackNode(-2810, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2809, 2, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode(-2808, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2807, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__Follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2871, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2870, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2869, 2, prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_, new char[] {62,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2868, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2867, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__IterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-2842, 8, prod__lit___42__char_class___range__42_42_, new char[] {42}, null, null);
      tmp[7] = new NonTerminalStackNode(-2841, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2840, 6, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode(-2839, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2838, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2837, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2836, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-2835, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2834, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__IterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__CharacterClass_$Sym__charClass_$Class_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2843, 0, "$Class", null, null);
      builder.addAlternative(RascalRascal.prod__CharacterClass_$Sym__charClass_$Class_, tmp);
	}
    protected static final void _init_prod__Alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-2833, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-2832, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-2827, 6, regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2828, 0, "$Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2829, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2830, 2, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null), new NonTerminalStackNode(-2831, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-2826, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2825, 4, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(-2824, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2823, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-2822, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2821, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2846, 2, "$NonterminalLabel", null, null);
      tmp[1] = new NonTerminalStackNode(-2845, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2844, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_, tmp);
	}
    protected static final void _init_prod__Start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2853, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(-2852, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2851, 4, "$Nonterminal", null, null);
      tmp[3] = new NonTerminalStackNode(-2850, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2849, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-2848, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2847, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new char[] {115,116,97,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__NotPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2866, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2865, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2864, 2, prod__lit___33_60_60__char_class___range__33_33_char_class___range__60_60_char_class___range__60_60_, new char[] {33,60,60}, null, null);
      tmp[1] = new NonTerminalStackNode(-2863, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2862, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__NotPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__Iter_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___43_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2856, 2, prod__lit___43__char_class___range__43_43_, new char[] {43}, null, null);
      tmp[1] = new NonTerminalStackNode(-2855, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2854, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Iter_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___43_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__EndOfLine_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___36_(builder);
      
        _init_prod__Parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_(builder);
      
        _init_prod__Optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_(builder);
      
        _init_prod__CaseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_(builder);
      
        _init_prod__IterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_(builder);
      
        _init_prod__Literal_$Sym__string_$StringConstant_(builder);
      
        _init_prod__IterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__Unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left(builder);
      
        _init_prod__NotFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(builder);
      
        _init_prod__StartOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_(builder);
      
        _init_prod__Empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Parametrized_$Sym__conditional__nonterminal_$Nonterminal__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(builder);
      
        _init_prod__Nonterminal_$Sym__conditional__nonterminal_$Nonterminal__not_follow__lit___91_(builder);
      
        _init_prod__Column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_(builder);
      
        _init_prod__Follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(builder);
      
        _init_prod__IterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__CharacterClass_$Sym__charClass_$Class_(builder);
      
        _init_prod__Alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_(builder);
      
        _init_prod__Start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__NotPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(builder);
      
        _init_prod__Iter_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___43_(builder);
      
    }
  }
	
  protected static class $Catch {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2905, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-2904, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2903, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-2902, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2901, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new char[] {99,97,116,99,104}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-2912, 6, "$Statement", null, null);
      tmp[5] = new NonTerminalStackNode(-2911, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2910, 4, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(-2909, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2908, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode(-2907, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2906, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new char[] {99,97,116,99,104}, null, null);
      builder.addAlternative(RascalRascal.prod__Binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement__tag__Foldable(builder);
      
        _init_prod__Binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement__tag__Foldable(builder);
      
    }
  }
	
  protected static class $Renaming {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$Renaming__from_$Name_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_to_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2900, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2899, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2898, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new char[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2897, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2896, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Renaming__from_$Name_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_to_$Name_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$Renaming__from_$Name_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_to_$Name_(builder);
      
    }
  }
	
  protected static class $Signature {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NoThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-2929, 6, "$Parameters", null, null);
      tmp[5] = new NonTerminalStackNode(-2928, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2927, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2926, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2925, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-2924, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2923, 0, "$FunctionModifiers", null, null);
      builder.addAlternative(RascalRascal.prod__NoThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_, tmp);
	}
    protected static final void _init_prod__WithThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit_throws_$layouts_LAYOUTLIST_exceptions_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new SeparatedListStackNode(-2940, 10, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2941, 0, "$Type", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2942, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2943, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2944, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(-2939, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-2938, 8, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new char[] {116,104,114,111,119,115}, null, null);
      tmp[7] = new NonTerminalStackNode(-2937, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2936, 6, "$Parameters", null, null);
      tmp[5] = new NonTerminalStackNode(-2935, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2934, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2933, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2932, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-2931, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2930, 0, "$FunctionModifiers", null, null);
      builder.addAlternative(RascalRascal.prod__WithThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit_throws_$layouts_LAYOUTLIST_exceptions_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__NoThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_(builder);
      
        _init_prod__WithThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit_throws_$layouts_LAYOUTLIST_exceptions_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $TypeArg {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Named_$TypeArg__type_$Type_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2953, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-2952, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2951, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__Named_$TypeArg__type_$Type_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__Default_$TypeArg__type_$Type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2954, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$TypeArg__type_$Type_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Named_$TypeArg__type_$Type_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__Default_$TypeArg__type_$Type_(builder);
      
    }
  }
	
  protected static class $Variable {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Initialized_$Variable__name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_initial_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2949, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2948, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2947, 2, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2946, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2945, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Initialized_$Variable__name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_initial_$Expression_, tmp);
	}
    protected static final void _init_prod__UnInitialized_$Variable__name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2950, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__UnInitialized_$Variable__name_$Name_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Initialized_$Variable__name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_initial_$Expression_(builder);
      
        _init_prod__UnInitialized_$Variable__name_$Name_(builder);
      
    }
  }
	
  protected static class $PreProtocolChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PreProtocolChars__lit___124_$URLChars_lit___60_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2970, 2, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(-2969, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode(-2968, 0, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      builder.addAlternative(RascalRascal.prod__$PreProtocolChars__lit___124_$URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$PreProtocolChars__lit___124_$URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class $Literal {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Location_$Literal__locationLiteral_$LocationLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2981, 0, "$LocationLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Location_$Literal__locationLiteral_$LocationLiteral_, tmp);
	}
    protected static final void _init_prod__Real_$Literal__realLiteral_$RealLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2982, 0, "$RealLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Real_$Literal__realLiteral_$RealLiteral_, tmp);
	}
    protected static final void _init_prod__RegExp_$Literal__regExpLiteral_$RegExpLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2983, 0, "$RegExpLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__RegExp_$Literal__regExpLiteral_$RegExpLiteral_, tmp);
	}
    protected static final void _init_prod__Rational_$Literal__rationalLiteral_$RationalLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2984, 0, "$RationalLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Rational_$Literal__rationalLiteral_$RationalLiteral_, tmp);
	}
    protected static final void _init_prod__Integer_$Literal__integerLiteral_$IntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2985, 0, "$IntegerLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Integer_$Literal__integerLiteral_$IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__Boolean_$Literal__booleanLiteral_$BooleanLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2986, 0, "$BooleanLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Boolean_$Literal__booleanLiteral_$BooleanLiteral_, tmp);
	}
    protected static final void _init_prod__DateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2987, 0, "$DateTimeLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__DateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_, tmp);
	}
    protected static final void _init_prod__String_$Literal__stringLiteral_$StringLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2988, 0, "$StringLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__String_$Literal__stringLiteral_$StringLiteral_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Location_$Literal__locationLiteral_$LocationLiteral_(builder);
      
        _init_prod__Real_$Literal__realLiteral_$RealLiteral_(builder);
      
        _init_prod__RegExp_$Literal__regExpLiteral_$RegExpLiteral_(builder);
      
        _init_prod__Rational_$Literal__rationalLiteral_$RationalLiteral_(builder);
      
        _init_prod__Integer_$Literal__integerLiteral_$IntegerLiteral_(builder);
      
        _init_prod__Boolean_$Literal__booleanLiteral_$BooleanLiteral_(builder);
      
        _init_prod__DateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_(builder);
      
        _init_prod__String_$Literal__stringLiteral_$StringLiteral_(builder);
      
    }
  }
	
  protected static class $Module {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Default_$Module__header_$Header_$layouts_LAYOUTLIST_body_$Body_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2997, 2, "$Body", null, null);
      tmp[1] = new NonTerminalStackNode(-2996, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2995, 0, "$Header", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Module__header_$Header_$layouts_LAYOUTLIST_body_$Body_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$Module__header_$Header_$layouts_LAYOUTLIST_body_$Body_(builder);
      
    }
  }
	
  public RascalRascal() {
    super();
  }

  // Parse methods    
  
  public AbstractStackNode[] $Tag() {
    return $Tag.EXPECTS;
  }
  public AbstractStackNode[] $DateAndTime() {
    return $DateAndTime.EXPECTS;
  }
  public AbstractStackNode[] $Pattern() {
    return $Pattern.EXPECTS;
  }
  public AbstractStackNode[] $Prod() {
    return $Prod.EXPECTS;
  }
  public AbstractStackNode[] $UnicodeEscape() {
    return $UnicodeEscape.EXPECTS;
  }
  public AbstractStackNode[] $OctalIntegerLiteral() {
    return $OctalIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode[] $RationalLiteral() {
    return $RationalLiteral.EXPECTS;
  }
  public AbstractStackNode[] $Toplevel() {
    return $Toplevel.EXPECTS;
  }
  public AbstractStackNode[] $BooleanLiteral() {
    return $BooleanLiteral.EXPECTS;
  }
  public AbstractStackNode[] $ProdModifier() {
    return $ProdModifier.EXPECTS;
  }
  public AbstractStackNode[] $TypeVar() {
    return $TypeVar.EXPECTS;
  }
  public AbstractStackNode[] $FunctionModifiers() {
    return $FunctionModifiers.EXPECTS;
  }
  public AbstractStackNode[] $Comprehension() {
    return $Comprehension.EXPECTS;
  }
  public AbstractStackNode[] $Type() {
    return $Type.EXPECTS;
  }
  public AbstractStackNode[] $Declaration() {
    return $Declaration.EXPECTS;
  }
  public AbstractStackNode[] $Class() {
    return $Class.EXPECTS;
  }
  public AbstractStackNode[] $Bound() {
    return $Bound.EXPECTS;
  }
  public AbstractStackNode[] $FunctionType() {
    return $FunctionType.EXPECTS;
  }
  public AbstractStackNode[] $Case() {
    return $Case.EXPECTS;
  }
  public AbstractStackNode[] $Statement() {
    return $Statement.EXPECTS;
  }
  public AbstractStackNode[] $Renamings() {
    return $Renamings.EXPECTS;
  }
  public AbstractStackNode[] $StringLiteral() {
    return $StringLiteral.EXPECTS;
  }
  public AbstractStackNode[] $Visibility() {
    return $Visibility.EXPECTS;
  }
  public AbstractStackNode[] $PostPathChars() {
    return $PostPathChars.EXPECTS;
  }
  public AbstractStackNode[] $NamedBackslash() {
    return $NamedBackslash.EXPECTS;
  }
  public AbstractStackNode[] $Visit() {
    return $Visit.EXPECTS;
  }
  public AbstractStackNode[] $Command() {
    return $Command.EXPECTS;
  }
  public AbstractStackNode[] $ProtocolTail() {
    return $ProtocolTail.EXPECTS;
  }
  public AbstractStackNode[] $PreStringChars() {
    return $PreStringChars.EXPECTS;
  }
  public AbstractStackNode[] $QualifiedName() {
    return $QualifiedName.EXPECTS;
  }
  public AbstractStackNode[] $URLChars() {
    return $URLChars.EXPECTS;
  }
  public AbstractStackNode[] $StringMiddle() {
    return $StringMiddle.EXPECTS;
  }
  public AbstractStackNode[] $TimeZonePart() {
    return $TimeZonePart.EXPECTS;
  }
  public AbstractStackNode[] $ShellCommand() {
    return $ShellCommand.EXPECTS;
  }
  public AbstractStackNode[] $LocationLiteral() {
    return $LocationLiteral.EXPECTS;
  }
  public AbstractStackNode[] $Range() {
    return $Range.EXPECTS;
  }
  public AbstractStackNode[] $JustTime() {
    return $JustTime.EXPECTS;
  }
  public AbstractStackNode[] $StringCharacter() {
    return $StringCharacter.EXPECTS;
  }
  public AbstractStackNode[] $layouts_LAYOUTLIST() {
    return $layouts_LAYOUTLIST.EXPECTS;
  }
  public AbstractStackNode[] $DataTarget() {
    return $DataTarget.EXPECTS;
  }
  public AbstractStackNode[] $RealLiteral() {
    return $RealLiteral.EXPECTS;
  }
  public AbstractStackNode[] $layouts_$default$() {
    return $layouts_$default$.EXPECTS;
  }
  public AbstractStackNode[] $Replacement() {
    return $Replacement.EXPECTS;
  }
  public AbstractStackNode[] $Assoc() {
    return $Assoc.EXPECTS;
  }
  public AbstractStackNode[] $RegExpModifier() {
    return $RegExpModifier.EXPECTS;
  }
  public AbstractStackNode[] $MidStringChars() {
    return $MidStringChars.EXPECTS;
  }
  public AbstractStackNode[] $RegExp() {
    return $RegExp.EXPECTS;
  }
  public AbstractStackNode[] $PathPart() {
    return $PathPart.EXPECTS;
  }
  public AbstractStackNode[] $StringTemplate() {
    return $StringTemplate.EXPECTS;
  }
  public AbstractStackNode[] $DecimalIntegerLiteral() {
    return $DecimalIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode[] $Body() {
    return $Body.EXPECTS;
  }
  public AbstractStackNode[] $Word() {
    return $Word.EXPECTS;
  }
  public AbstractStackNode[] $LAYOUT() {
    return $LAYOUT.EXPECTS;
  }
  public AbstractStackNode[] $UserType() {
    return $UserType.EXPECTS;
  }
  public AbstractStackNode[] $Import() {
    return $Import.EXPECTS;
  }
  public AbstractStackNode[] $FunctionBody() {
    return $FunctionBody.EXPECTS;
  }
  public AbstractStackNode[] start__$Command() {
    return start__$Command.EXPECTS;
  }
  public AbstractStackNode[] $Target() {
    return $Target.EXPECTS;
  }
  public AbstractStackNode[] $IntegerLiteral() {
    return $IntegerLiteral.EXPECTS;
  }
  public AbstractStackNode[] $SyntaxDefinition() {
    return $SyntaxDefinition.EXPECTS;
  }
  public AbstractStackNode[] $Kind() {
    return $Kind.EXPECTS;
  }
  public AbstractStackNode[] $Mapping__$Expression() {
    return $Mapping__$Expression.EXPECTS;
  }
  public AbstractStackNode[] $PostProtocolChars() {
    return $PostProtocolChars.EXPECTS;
  }
  public AbstractStackNode[] $MidPathChars() {
    return $MidPathChars.EXPECTS;
  }
  public AbstractStackNode[] $Char() {
    return $Char.EXPECTS;
  }
  public AbstractStackNode[] $PrePathChars() {
    return $PrePathChars.EXPECTS;
  }
  public AbstractStackNode[] $Strategy() {
    return $Strategy.EXPECTS;
  }
  public AbstractStackNode[] $ModuleParameters() {
    return $ModuleParameters.EXPECTS;
  }
  public AbstractStackNode[] $RascalKeywords() {
    return $RascalKeywords.EXPECTS;
  }
  public AbstractStackNode[] $NamedRegExp() {
    return $NamedRegExp.EXPECTS;
  }
  public AbstractStackNode[] $RegExpLiteral() {
    return $RegExpLiteral.EXPECTS;
  }
  public AbstractStackNode[] $OctalEscapeSequence() {
    return $OctalEscapeSequence.EXPECTS;
  }
  public AbstractStackNode[] $PostStringChars() {
    return $PostStringChars.EXPECTS;
  }
  public AbstractStackNode[] $HexIntegerLiteral() {
    return $HexIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode[] $ModuleActuals() {
    return $ModuleActuals.EXPECTS;
  }
  public AbstractStackNode[] $PathChars() {
    return $PathChars.EXPECTS;
  }
  public AbstractStackNode[] $DateTimeLiteral() {
    return $DateTimeLiteral.EXPECTS;
  }
  public AbstractStackNode[] $BasicType() {
    return $BasicType.EXPECTS;
  }
  public AbstractStackNode[] $FunctionDeclaration() {
    return $FunctionDeclaration.EXPECTS;
  }
  public AbstractStackNode[] $Variant() {
    return $Variant.EXPECTS;
  }
  public AbstractStackNode[] start__$PreModule() {
    return start__$PreModule.EXPECTS;
  }
  public AbstractStackNode[] $Declarator() {
    return $Declarator.EXPECTS;
  }
  public AbstractStackNode[] $StructuredType() {
    return $StructuredType.EXPECTS;
  }
  public AbstractStackNode[] $TimePartNoTZ() {
    return $TimePartNoTZ.EXPECTS;
  }
  public AbstractStackNode[] $Name() {
    return $Name.EXPECTS;
  }
  public AbstractStackNode[] $Start() {
    return $Start.EXPECTS;
  }
  public AbstractStackNode[] $Formals() {
    return $Formals.EXPECTS;
  }
  public AbstractStackNode[] $Tags() {
    return $Tags.EXPECTS;
  }
  public AbstractStackNode[] start__$Commands() {
    return start__$Commands.EXPECTS;
  }
  public AbstractStackNode[] $CaseInsensitiveStringConstant() {
    return $CaseInsensitiveStringConstant.EXPECTS;
  }
  public AbstractStackNode[] $Backslash() {
    return $Backslash.EXPECTS;
  }
  public AbstractStackNode[] $JustDate() {
    return $JustDate.EXPECTS;
  }
  public AbstractStackNode[] $PathTail() {
    return $PathTail.EXPECTS;
  }
  public AbstractStackNode[] $MidProtocolChars() {
    return $MidProtocolChars.EXPECTS;
  }
  public AbstractStackNode[] $PatternWithAction() {
    return $PatternWithAction.EXPECTS;
  }
  public AbstractStackNode[] $DataTypeSelector() {
    return $DataTypeSelector.EXPECTS;
  }
  public AbstractStackNode[] $StringTail() {
    return $StringTail.EXPECTS;
  }
  public AbstractStackNode[] start__$Module() {
    return start__$Module.EXPECTS;
  }
  public AbstractStackNode[] $StringConstant() {
    return $StringConstant.EXPECTS;
  }
  public AbstractStackNode[] $LocalVariableDeclaration() {
    return $LocalVariableDeclaration.EXPECTS;
  }
  public AbstractStackNode[] $Parameters() {
    return $Parameters.EXPECTS;
  }
  public AbstractStackNode[] $DatePart() {
    return $DatePart.EXPECTS;
  }
  public AbstractStackNode[] $HeaderKeyword() {
    return $HeaderKeyword.EXPECTS;
  }
  public AbstractStackNode[] $Assignable() {
    return $Assignable.EXPECTS;
  }
  public AbstractStackNode[] $FunctionModifier() {
    return $FunctionModifier.EXPECTS;
  }
  public AbstractStackNode[] $ProtocolChars() {
    return $ProtocolChars.EXPECTS;
  }
  public AbstractStackNode[] $Assignment() {
    return $Assignment.EXPECTS;
  }
  public AbstractStackNode[] $Field() {
    return $Field.EXPECTS;
  }
  public AbstractStackNode[] $Comment() {
    return $Comment.EXPECTS;
  }
  public AbstractStackNode[] $Label() {
    return $Label.EXPECTS;
  }
  public AbstractStackNode[] $ProtocolPart() {
    return $ProtocolPart.EXPECTS;
  }
  public AbstractStackNode[] $PreModule() {
    return $PreModule.EXPECTS;
  }
  public AbstractStackNode[] $TagString() {
    return $TagString.EXPECTS;
  }
  public AbstractStackNode[] $Nonterminal() {
    return $Nonterminal.EXPECTS;
  }
  public AbstractStackNode[] $Mapping__$Pattern() {
    return $Mapping__$Pattern.EXPECTS;
  }
  public AbstractStackNode[] $Expression() {
    return $Expression.EXPECTS;
  }
  public AbstractStackNode[] $ImportedModule() {
    return $ImportedModule.EXPECTS;
  }
  public AbstractStackNode[] $Commands() {
    return $Commands.EXPECTS;
  }
  public AbstractStackNode[] $Header() {
    return $Header.EXPECTS;
  }
  public AbstractStackNode[] $NonterminalLabel() {
    return $NonterminalLabel.EXPECTS;
  }
  public AbstractStackNode[] $Sym() {
    return $Sym.EXPECTS;
  }
  public AbstractStackNode[] $Renaming() {
    return $Renaming.EXPECTS;
  }
  public AbstractStackNode[] $Catch() {
    return $Catch.EXPECTS;
  }
  public AbstractStackNode[] $Signature() {
    return $Signature.EXPECTS;
  }
  public AbstractStackNode[] $Variable() {
    return $Variable.EXPECTS;
  }
  public AbstractStackNode[] $TypeArg() {
    return $TypeArg.EXPECTS;
  }
  public AbstractStackNode[] $PreProtocolChars() {
    return $PreProtocolChars.EXPECTS;
  }
  public AbstractStackNode[] $Literal() {
    return $Literal.EXPECTS;
  }
  public AbstractStackNode[] $Module() {
    return $Module.EXPECTS;
  }
}