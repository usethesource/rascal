package org.rascalmpl.library.lang.rascal.syntax;

import java.io.IOException;
import java.io.StringReader;

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
      return new StandardTextReader().read(VF, org.rascalmpl.values.uptr.Factory.uptr, type, new StringReader(s));
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
    
    
    
    
    _putDontNest(result, -2498, -2517);
    
    _putDontNest(result, -2517, -2554);
    
    _putDontNest(result, -2535, -2544);
    
    _putDontNest(result, -2530, -2549);
    
    _putDontNest(result, -2639, -2639);
    
    _putDontNest(result, -2852, -2861);
    
    _putDontNest(result, -515, -523);
    
    _putDontNest(result, -2599, -2599);
    
    _putDontNest(result, -2564, -2574);
    
    _putDontNest(result, -2609, -2653);
    
    _putDontNest(result, -2570, -2584);
    
    _putDontNest(result, -2614, -2644);
    
    _putDontNest(result, -2604, -2614);
    
    _putDontNest(result, -2508, -2639);
    
    _putDontNest(result, -2507, -2634);
    
    _putDontNest(result, -2549, -2589);
    
    _putDontNest(result, -2276, -2529);
    
    _putDontNest(result, -2501, -2554);
    
    _putDontNest(result, -2529, -2534);
    
    _putDontNest(result, -168, -168);
    
    _putDontNest(result, -2504, -2559);
    
    _putDontNest(result, -523, -515);
    
    _putDontNest(result, -2565, -2609);
    
    _putDontNest(result, -2523, -2634);
    
    _putDontNest(result, -2489, -2604);
    
    _putDontNest(result, -2535, -2579);
    
    _putDontNest(result, -2292, -2529);
    
    _putDontNest(result, -2549, -2554);
    
    _putDontNest(result, -2498, -2549);
    
    _putDontNest(result, -2388, -2534);
    
    _putDontNest(result, -2416, -2498);
    
    _putDontNest(result, -2574, -2604);
    
    _putDontNest(result, -2841, -2856);
    
    _putDontNest(result, -2619, -2619);
    
    _putDontNest(result, -2361, -2619);
    
    _putDontNest(result, -2539, -2634);
    
    _putDontNest(result, -2540, -2639);
    
    _putDontNest(result, -2517, -2589);
    
    _putDontNest(result, -412, -427);
    
    _putDontNest(result, -2399, -2529);
    
    _putDontNest(result, -2590, -2604);
    
    _putDontNest(result, -2584, -2594);
    
    _putDontNest(result, -2594, -2624);
    
    _putDontNest(result, -629, -714);
    
    _putDontNest(result, -2307, -2653);
    
    _putDontNest(result, -2555, -2634);
    
    _putDontNest(result, -2501, -2589);
    
    _putDontNest(result, -42, -118);
    
    _putDontNest(result, -2307, -2517);
    
    _putDontNest(result, -2787, -2861);
    
    _putDontNest(result, -2575, -2639);
    
    _putDontNest(result, -2585, -2629);
    
    _putDontNest(result, -2388, -2624);
    
    _putDontNest(result, -2416, -2604);
    
    _putDontNest(result, -2545, -2644);
    
    _putDontNest(result, -2550, -2653);
    
    _putDontNest(result, -2517, -2624);
    
    _putDontNest(result, -58, -118);
    
    _putDontNest(result, -2307, -2501);
    
    _putDontNest(result, -2594, -2589);
    
    _putDontNest(result, -2569, -2629);
    
    _putDontNest(result, -2399, -2569);
    
    _putDontNest(result, -2377, -2619);
    
    _putDontNest(result, -2522, -2569);
    
    _putDontNest(result, -2498, -2609);
    
    _putDontNest(result, -2534, -2653);
    
    _putDontNest(result, -2529, -2644);
    
    _putDontNest(result, -2512, -2619);
    
    _putDontNest(result, -2559, -2614);
    
    _putDontNest(result, -2513, -2564);
    
    _putDontNest(result, -2501, -2624);
    
    _putDontNest(result, -2517, -2522);
    
    _putDontNest(result, -2307, -2549);
    
    _putDontNest(result, -2580, -2579);
    
    _putDontNest(result, -2529, -2564);
    
    _putDontNest(result, -2535, -2574);
    
    _putDontNest(result, -2549, -2624);
    
    _putDontNest(result, -2476, -2639);
    
    _putDontNest(result, -2518, -2653);
    
    _putDontNest(result, -2560, -2619);
    
    _putDontNest(result, -2513, -2644);
    
    _putDontNest(result, -2292, -2609);
    
    _putDontNest(result, -2501, -2522);
    
    _putDontNest(result, -2366, -2504);
    
    _putDontNest(result, -2771, -2861);
    
    _putDontNest(result, -2564, -2579);
    
    _putDontNest(result, -2630, -2644);
    
    _putDontNest(result, -2625, -2653);
    
    _putDontNest(result, -2624, -2634);
    
    _putDontNest(result, -2366, -2634);
    
    _putDontNest(result, -2530, -2609);
    
    _putDontNest(result, -2824, -2846);
    
    _putDontNest(result, -2544, -2619);
    
    _putDontNest(result, -2545, -2564);
    
    _putDontNest(result, -2554, -2569);
    
    _putDontNest(result, -2279, -2624);
    
    _putDontNest(result, -2276, -2609);
    
    _putDontNest(result, -427, -427);
    
    _putDontNest(result, -2504, -2512);
    
    _putDontNest(result, -2529, -2549);
    
    _putDontNest(result, -2518, -2554);
    
    _putDontNest(result, -710, -713);
    
    _putDontNest(result, -2851, -2861);
    
    _putDontNest(result, -2600, -2599);
    
    _putDontNest(result, -2569, -2584);
    
    _putDontNest(result, -2574, -2569);
    
    _putDontNest(result, -2292, -2589);
    
    _putDontNest(result, -2523, -2629);
    
    _putDontNest(result, -2550, -2589);
    
    _putDontNest(result, -2388, -2507);
    
    _putDontNest(result, -2399, -2504);
    
    _putDontNest(result, -2594, -2653);
    
    _putDontNest(result, -2634, -2629);
    
    _putDontNest(result, -515, -518);
    
    _putDontNest(result, -2619, -2614);
    
    _putDontNest(result, -523, -526);
    
    _putDontNest(result, -2585, -2584);
    
    _putDontNest(result, -2276, -2589);
    
    _putDontNest(result, -2307, -2624);
    
    _putDontNest(result, -2361, -2614);
    
    _putDontNest(result, -2507, -2629);
    
    _putDontNest(result, -2534, -2589);
    
    _putDontNest(result, -2529, -2517);
    
    _putDontNest(result, -2504, -2544);
    
    _putDontNest(result, -2609, -2624);
    
    _putDontNest(result, -2555, -2629);
    
    _putDontNest(result, -2489, -2619);
    
    _putDontNest(result, -2476, -2584);
    
    _putDontNest(result, -2518, -2589);
    
    _putDontNest(result, -2276, -2498);
    
    _putDontNest(result, -2513, -2549);
    
    _putDontNest(result, -2498, -2534);
    
    _putDontNest(result, -2535, -2559);
    
    _putDontNest(result, -2534, -2554);
    
    _putDontNest(result, -2388, -2539);
    
    _putDontNest(result, -2604, -2619);
    
    _putDontNest(result, -2565, -2644);
    
    _putDontNest(result, -2584, -2599);
    
    _putDontNest(result, -2589, -2604);
    
    _putDontNest(result, -629, -713);
    
    _putDontNest(result, -2539, -2629);
    
    _putDontNest(result, -2504, -2579);
    
    _putDontNest(result, -2292, -2498);
    
    _putDontNest(result, -2388, -2491);
    
    _putDontNest(result, -2575, -2634);
    
    _putDontNest(result, -2504, -2564);
    
    _putDontNest(result, -2498, -2574);
    
    _putDontNest(result, -2544, -2604);
    
    _putDontNest(result, -2534, -2594);
    
    _putDontNest(result, -2508, -2584);
    
    _putDontNest(result, -2276, -2594);
    
    _putDontNest(result, -57, -118);
    
    _putDontNest(result, -2534, -2522);
    
    _putDontNest(result, -2366, -2529);
    
    _putDontNest(result, -2570, -2629);
    
    _putDontNest(result, -2550, -2594);
    
    _putDontNest(result, -2549, -2599);
    
    _putDontNest(result, -2554, -2614);
    
    _putDontNest(result, -2560, -2604);
    
    _putDontNest(result, -2292, -2594);
    
    _putDontNest(result, -2766, -2861);
    
    _putDontNest(result, -2589, -2579);
    
    _putDontNest(result, -2416, -2569);
    
    _putDontNest(result, -2388, -2653);
    
    _putDontNest(result, -2377, -2614);
    
    _putDontNest(result, -2501, -2599);
    
    _putDontNest(result, -2512, -2604);
    
    _putDontNest(result, -2540, -2584);
    
    _putDontNest(result, -2530, -2574);
    
    _putDontNest(result, -2575, -2589);
    
    _putDontNest(result, -2624, -2639);
    
    _putDontNest(result, -2629, -2644);
    
    _putDontNest(result, -2366, -2639);
    
    _putDontNest(result, -2399, -2604);
    
    _putDontNest(result, -2517, -2599);
    
    _putDontNest(result, -2535, -2609);
    
    _putDontNest(result, -2522, -2614);
    
    _putDontNest(result, -2504, -2644);
    
    _putDontNest(result, -2559, -2569);
    
    _putDontNest(result, -2518, -2594);
    
    _putDontNest(result, -2574, -2574);
    
    _putDontNest(result, -2625, -2639);
    
    _putDontNest(result, -582, -713);
    
    _putDontNest(result, -2545, -2579);
    
    _putDontNest(result, -2544, -2549);
    
    _putDontNest(result, -2523, -2554);
    
    _putDontNest(result, -2522, -2559);
    
    _putDontNest(result, -2512, -2517);
    
    _putDontNest(result, -417, -432);
    
    _putDontNest(result, -422, -427);
    
    _putDontNest(result, -2307, -2491);
    
    _putDontNest(result, -2749, -2861);
    
    _putDontNest(result, -2565, -2619);
    
    _putDontNest(result, -2584, -2584);
    
    _putDontNest(result, -518, -526);
    
    _putDontNest(result, -2614, -2614);
    
    _putDontNest(result, -526, -518);
    
    _putDontNest(result, -2604, -2644);
    
    _putDontNest(result, -2366, -2604);
    
    _putDontNest(result, -2399, -2639);
    
    _putDontNest(result, -2377, -2629);
    
    _putDontNest(result, -2489, -2614);
    
    _putDontNest(result, -2508, -2629);
    
    _putDontNest(result, -2529, -2579);
    
    _putDontNest(result, -2507, -2554);
    
    _putDontNest(result, -2416, -2504);
    
    _putDontNest(result, -2564, -2604);
    
    _putDontNest(result, -2569, -2599);
    
    _putDontNest(result, -2570, -2594);
    
    _putDontNest(result, -2624, -2624);
    
    _putDontNest(result, -2416, -2634);
    
    _putDontNest(result, -2513, -2579);
    
    _putDontNest(result, -2512, -2549);
    
    _putDontNest(result, -2554, -2559);
    
    _putDontNest(result, -412, -417);
    
    _putDontNest(result, -2575, -2653);
    
    _putDontNest(result, -2846, -2861);
    
    _putDontNest(result, -2580, -2604);
    
    _putDontNest(result, -2585, -2599);
    
    _putDontNest(result, -2540, -2629);
    
    _putDontNest(result, -2766, -2841);
    
    _putDontNest(result, -2539, -2554);
    
    _putDontNest(result, -2307, -2507);
    
    _putDontNest(result, -518, -713);
    
    _putDontNest(result, -2507, -2584);
    
    _putDontNest(result, -2522, -2619);
    
    _putDontNest(result, -2512, -2569);
    
    _putDontNest(result, -2534, -2599);
    
    _putDontNest(result, -2276, -2599);
    
    _putDontNest(result, -2388, -2574);
    
    _putDontNest(result, -2523, -2584);
    
    _putDontNest(result, -2550, -2599);
    
    _putDontNest(result, -2504, -2609);
    
    _putDontNest(result, -2549, -2594);
    
    _putDontNest(result, -2513, -2574);
    
    _putDontNest(result, -2559, -2604);
    
    _putDontNest(result, -2535, -2644);
    
    _putDontNest(result, -2292, -2599);
    
    _putDontNest(result, -2489, -2544);
    
    _putDontNest(result, -2307, -2539);
    
    _putDontNest(result, -2590, -2579);
    
    _putDontNest(result, -2594, -2634);
    
    _putDontNest(result, -2361, -2629);
    
    _putDontNest(result, -2834, -2846);
    
    _putDontNest(result, -2535, -2564);
    
    _putDontNest(result, -2554, -2619);
    
    _putDontNest(result, -2529, -2574);
    
    _putDontNest(result, -2539, -2584);
    
    _putDontNest(result, -2544, -2569);
    
    _putDontNest(result, -2501, -2594);
    
    _putDontNest(result, -2279, -2574);
    
    _putDontNest(result, -2366, -2498);
    
    _putDontNest(result, -2574, -2579);
    
    _putDontNest(result, -2639, -2653);
    
    _putDontNest(result, -2599, -2629);
    
    _putDontNest(result, -2609, -2639);
    
    _putDontNest(result, -2476, -2629);
    
    _putDontNest(result, -2555, -2584);
    
    _putDontNest(result, -2560, -2569);
    
    _putDontNest(result, -2545, -2574);
    
    _putDontNest(result, -2517, -2594);
    
    _putDontNest(result, -2518, -2599);
    
    _putDontNest(result, -2507, -2522);
    
    _putDontNest(result, -2489, -2512);
    
    _putDontNest(result, -2564, -2569);
    
    _putDontNest(result, -2624, -2653);
    
    _putDontNest(result, -2625, -2634);
    
    _putDontNest(result, -2595, -2604);
    
    _putDontNest(result, -2861, -2861);
    
    _putDontNest(result, -2619, -2644);
    
    _putDontNest(result, -526, -523);
    
    _putDontNest(result, -2307, -2574);
    
    _putDontNest(result, -2292, -2579);
    
    _putDontNest(result, -2476, -2614);
    
    _putDontNest(result, -2512, -2634);
    
    _putDontNest(result, -418, -432);
    
    _putDontNest(result, -2377, -2512);
    
    _putDontNest(result, -2416, -2549);
    
    _putDontNest(result, -2752, -2861);
    
    _putDontNest(result, -2856, -2856);
    
    _putDontNest(result, -518, -515);
    
    _putDontNest(result, -2575, -2624);
    
    _putDontNest(result, -2276, -2579);
    
    _putDontNest(result, -2399, -2634);
    
    _putDontNest(result, -2530, -2579);
    
    _putDontNest(result, -2508, -2554);
    
    _putDontNest(result, -2559, -2549);
    
    _putDontNest(result, -2476, -2522);
    
    _putDontNest(result, -2554, -2544);
    
    _putDontNest(result, -2569, -2594);
    
    _putDontNest(result, -2565, -2614);
    
    _putDontNest(result, -2570, -2599);
    
    _putDontNest(result, -2614, -2619);
    
    _putDontNest(result, -2416, -2639);
    
    _putDontNest(result, -2544, -2634);
    
    _putDontNest(result, -2276, -2504);
    
    _putDontNest(result, -57, -133);
    
    _putDontNest(result, -2366, -2491);
    
    _putDontNest(result, -2399, -2534);
    
    _putDontNest(result, -2377, -2544);
    
    _putDontNest(result, -2416, -2517);
    
    _putDontNest(result, -2579, -2604);
    
    _putDontNest(result, -2824, -2856);
    
    _putDontNest(result, -2604, -2609);
    
    _putDontNest(result, -2585, -2594);
    
    _putDontNest(result, -2366, -2569);
    
    _putDontNest(result, -2361, -2584);
    
    _putDontNest(result, -2559, -2639);
    
    _putDontNest(result, -2560, -2634);
    
    _putDontNest(result, -2498, -2579);
    
    _putDontNest(result, -2292, -2504);
    
    _putDontNest(result, -2522, -2544);
    
    _putDontNest(result, -2512, -2534);
    
    _putDontNest(result, -2416, -2501);
    
    _putDontNest(result, -2388, -2529);
    
    _putDontNest(result, -2584, -2629);
    
    _putDontNest(result, -523, -713);
    
    _putDontNest(result, -2377, -2584);
    
    _putDontNest(result, -2523, -2619);
    
    _putDontNest(result, -2540, -2614);
    
    _putDontNest(result, -2535, -2599);
    
    _putDontNest(result, -2545, -2653);
    
    _putDontNest(result, -2517, -2609);
    
    _putDontNest(result, -2550, -2644);
    
    _putDontNest(result, -2749, -2841);
    
    _putDontNest(result, -36, -111);
    
    _putDontNest(result, -2307, -2498);
    
    _putDontNest(result, -2507, -2619);
    
    _putDontNest(result, -2522, -2584);
    
    _putDontNest(result, -2498, -2624);
    
    _putDontNest(result, -2501, -2609);
    
    _putDontNest(result, -2518, -2564);
    
    _putDontNest(result, -2529, -2653);
    
    _putDontNest(result, -2534, -2644);
    
    _putDontNest(result, -2366, -2539);
    
    _putDontNest(result, -2361, -2544);
    
    _putDontNest(result, -2589, -2589);
    
    _putDontNest(result, -2594, -2639);
    
    _putDontNest(result, -2549, -2609);
    
    _putDontNest(result, -2504, -2594);
    
    _putDontNest(result, -2518, -2644);
    
    _putDontNest(result, -2508, -2614);
    
    _putDontNest(result, -2555, -2619);
    
    _putDontNest(result, -2513, -2653);
    
    _putDontNest(result, -2534, -2564);
    
    _putDontNest(result, -2489, -2629);
    
    _putDontNest(result, -2292, -2624);
    
    _putDontNest(result, -2489, -2559);
    
    _putDontNest(result, -2575, -2579);
    
    _putDontNest(result, -2640, -2653);
    
    _putDontNest(result, -2600, -2629);
    
    _putDontNest(result, -2609, -2634);
    
    _putDontNest(result, -2635, -2644);
    
    _putDontNest(result, -2530, -2624);
    
    _putDontNest(result, -2550, -2564);
    
    _putDontNest(result, -2554, -2584);
    
    _putDontNest(result, -2539, -2619);
    
    _putDontNest(result, -2307, -2589);
    
    _putDontNest(result, -2279, -2609);
    
    _putDontNest(result, -2276, -2624);
    
    _putDontNest(result, -2476, -2554);
    
    _putDontNest(result, -2508, -2522);
    
    _putDontNest(result, -2361, -2512);
    
    _putDontNest(result, -2366, -2507);
    
    _putDontNest(result, -2594, -2604);
    
    _putDontNest(result, -2590, -2624);
    
    _putDontNest(result, -2600, -2614);
    
    _putDontNest(result, -2585, -2609);
    
    _putDontNest(result, -2307, -2569);
    
    _putDontNest(result, -2512, -2639);
    
    _putDontNest(result, -2522, -2629);
    
    _putDontNest(result, -2545, -2589);
    
    _putDontNest(result, -2539, -2544);
    
    _putDontNest(result, -2507, -2512);
    
    _putDontNest(result, -2489, -2522);
    
    _putDontNest(result, -2399, -2501);
    
    _putDontNest(result, -2574, -2624);
    
    _putDontNest(result, -2629, -2629);
    
    _putDontNest(result, -2569, -2609);
    
    _putDontNest(result, -2605, -2653);
    
    _putDontNest(result, -2361, -2599);
    
    _putDontNest(result, -2787, -2846);
    
    _putDontNest(result, -2529, -2589);
    
    _putDontNest(result, -2508, -2559);
    
    _putDontNest(result, -2399, -2517);
    
    _putDontNest(result, -2377, -2559);
    
    _putDontNest(result, -2388, -2498);
    
    _putDontNest(result, -2416, -2534);
    
    _putDontNest(result, -2589, -2653);
    
    _putDontNest(result, -2544, -2639);
    
    _putDontNest(result, -2554, -2629);
    
    _putDontNest(result, -2771, -2846);
    
    _putDontNest(result, -2513, -2589);
    
    _putDontNest(result, -2388, -2589);
    
    _putDontNest(result, -2279, -2498);
    
    _putDontNest(result, -2498, -2529);
    
    _putDontNest(result, -2512, -2539);
    
    _putDontNest(result, -2507, -2544);
    
    _putDontNest(result, -58, -133);
    
    _putDontNest(result, -2584, -2614);
    
    _putDontNest(result, -2570, -2644);
    
    _putDontNest(result, -2599, -2619);
    
    _putDontNest(result, -2366, -2574);
    
    _putDontNest(result, -2476, -2619);
    
    _putDontNest(result, -2559, -2634);
    
    _putDontNest(result, -2489, -2584);
    
    _putDontNest(result, -2560, -2639);
    
    _putDontNest(result, -2523, -2544);
    
    _putDontNest(result, -42, -133);
    
    _putDontNest(result, -2399, -2549);
    
    _putDontNest(result, -2564, -2634);
    
    _putDontNest(result, -2539, -2614);
    
    _putDontNest(result, -2501, -2564);
    
    _putDontNest(result, -2535, -2594);
    
    _putDontNest(result, -2549, -2644);
    
    _putDontNest(result, -2513, -2624);
    
    _putDontNest(result, -2518, -2609);
    
    _putDontNest(result, -2752, -2841);
    
    _putDontNest(result, -2292, -2653);
    
    _putDontNest(result, -2565, -2629);
    
    _putDontNest(result, -2579, -2639);
    
    _putDontNest(result, -2580, -2634);
    
    _putDontNest(result, -2508, -2619);
    
    _putDontNest(result, -2555, -2614);
    
    _putDontNest(result, -2530, -2653);
    
    _putDontNest(result, -2517, -2564);
    
    _putDontNest(result, -2276, -2653);
    
    _putDontNest(result, -2279, -2644);
    
    _putDontNest(result, -2590, -2589);
    
    _putDontNest(result, -2595, -2639);
    
    _putDontNest(result, -2399, -2653);
    
    _putDontNest(result, -2377, -2599);
    
    _putDontNest(result, -2550, -2609);
    
    _putDontNest(result, -2517, -2644);
    
    _putDontNest(result, -2545, -2624);
    
    _putDontNest(result, -2504, -2599);
    
    _putDontNest(result, -2507, -2614);
    
    _putDontNest(result, -2279, -2564);
    
    _putDontNest(result, -2489, -2554);
    
    _putDontNest(result, -2361, -2559);
    
    _putDontNest(result, -2307, -2529);
    
    _putDontNest(result, -2574, -2589);
    
    _putDontNest(result, -2634, -2644);
    
    _putDontNest(result, -2388, -2604);
    
    _putDontNest(result, -2416, -2624);
    
    _putDontNest(result, -2498, -2653);
    
    _putDontNest(result, -2529, -2624);
    
    _putDontNest(result, -2523, -2614);
    
    _putDontNest(result, -2501, -2644);
    
    _putDontNest(result, -2549, -2564);
    
    _putDontNest(result, -2540, -2619);
    
    _putDontNest(result, -2534, -2609);
    
    _putDontNest(result, -2476, -2559);
    
    _putDontNest(result, -2584, -2619);
    
    _putDontNest(result, -2589, -2624);
    
    _putDontNest(result, -2604, -2599);
    
    _putDontNest(result, -2599, -2614);
    
    _putDontNest(result, -2565, -2584);
    
    _putDontNest(result, -2498, -2634);
    
    _putDontNest(result, -2279, -2529);
    
    _putDontNest(result, -2522, -2554);
    
    _putDontNest(result, -164, -173);
    
    _putDontNest(result, -2529, -2529);
    
    _putDontNest(result, -2523, -2559);
    
    _putDontNest(result, -2416, -2539);
    
    _putDontNest(result, -2361, -2522);
    
    _putDontNest(result, -2752, -2851);
    
    _putDontNest(result, -2630, -2629);
    
    _putDontNest(result, -2570, -2609);
    
    _putDontNest(result, -2361, -2594);
    
    _putDontNest(result, -2513, -2639);
    
    _putDontNest(result, -2476, -2644);
    
    _putDontNest(result, -2489, -2599);
    
    _putDontNest(result, -2530, -2589);
    
    _putDontNest(result, -2507, -2559);
    
    _putDontNest(result, -422, -422);
    
    _putDontNest(result, -2377, -2554);
    
    _putDontNest(result, -2590, -2653);
    
    _putDontNest(result, -2585, -2644);
    
    _putDontNest(result, -2307, -2604);
    
    _putDontNest(result, -2476, -2564);
    
    _putDontNest(result, -2529, -2639);
    
    _putDontNest(result, -2530, -2634);
    
    _putDontNest(result, -2554, -2554);
    
    _putDontNest(result, -2508, -2544);
    
    _putDontNest(result, -58, -130);
    
    _putDontNest(result, -2416, -2507);
    
    _putDontNest(result, -2847, -2861);
    
    _putDontNest(result, -2841, -2851);
    
    _putDontNest(result, -2600, -2619);
    
    _putDontNest(result, -2569, -2644);
    
    _putDontNest(result, -2574, -2653);
    
    _putDontNest(result, -2605, -2624);
    
    _putDontNest(result, -2545, -2639);
    
    _putDontNest(result, -2535, -2629);
    
    _putDontNest(result, -2498, -2589);
    
    _putDontNest(result, -2539, -2559);
    
    _putDontNest(result, -42, -130);
    
    _putDontNest(result, -2564, -2639);
    
    _putDontNest(result, -2399, -2624);
    
    _putDontNest(result, -2559, -2653);
    
    _putDontNest(result, -2508, -2564);
    
    _putDontNest(result, -2517, -2619);
    
    _putDontNest(result, -2523, -2609);
    
    _putDontNest(result, -2504, -2584);
    
    _putDontNest(result, -2534, -2614);
    
    _putDontNest(result, -2279, -2599);
    
    _putDontNest(result, -2476, -2544);
    
    _putDontNest(result, -2307, -2504);
    
    _putDontNest(result, -2787, -2856);
    
    _putDontNest(result, -2580, -2639);
    
    _putDontNest(result, -2579, -2634);
    
    _putDontNest(result, -2388, -2569);
    
    _putDontNest(result, -2416, -2653);
    
    _putDontNest(result, -2550, -2614);
    
    _putDontNest(result, -2554, -2594);
    
    _putDontNest(result, -2512, -2624);
    
    _putDontNest(result, -2540, -2644);
    
    _putDontNest(result, -2507, -2609);
    
    _putDontNest(result, -2501, -2619);
    
    _putDontNest(result, -2366, -2549);
    
    _putDontNest(result, -2771, -2856);
    
    _putDontNest(result, -2585, -2579);
    
    _putDontNest(result, -2614, -2629);
    
    _putDontNest(result, -2595, -2634);
    
    _putDontNest(result, -2377, -2594);
    
    _putDontNest(result, -2834, -2841);
    
    _putDontNest(result, -2549, -2619);
    
    _putDontNest(result, -2540, -2564);
    
    _putDontNest(result, -2555, -2609);
    
    _putDontNest(result, -2560, -2624);
    
    _putDontNest(result, -2276, -2574);
    
    _putDontNest(result, -2522, -2522);
    
    _putDontNest(result, -2476, -2512);
    
    _putDontNest(result, -2366, -2501);
    
    _putDontNest(result, -2361, -2554);
    
    _putDontNest(result, -2569, -2579);
    
    _putDontNest(result, -2544, -2624);
    
    _putDontNest(result, -2508, -2644);
    
    _putDontNest(result, -2539, -2609);
    
    _putDontNest(result, -2522, -2594);
    
    _putDontNest(result, -2518, -2614);
    
    _putDontNest(result, -2292, -2574);
    
    _putDontNest(result, -2307, -2579);
    
    _putDontNest(result, -36, -125);
    
    _putDontNest(result, -2366, -2517);
    
    _putDontNest(result, -2416, -2491);
    
    _putDontNest(result, -2377, -2522);
    
    _putDontNest(result, -2630, -2634);
    
    _putDontNest(result, -2629, -2639);
    
    _putDontNest(result, -2594, -2614);
    
    _putDontNest(result, -2619, -2653);
    
    _putDontNest(result, -2570, -2574);
    
    _putDontNest(result, -2600, -2604);
    
    _putDontNest(result, -2564, -2584);
    
    _putDontNest(result, -2624, -2644);
    
    _putDontNest(result, -2498, -2639);
    
    _putDontNest(result, -2559, -2589);
    
    _putDontNest(result, -2508, -2517);
    
    _putDontNest(result, -418, -427);
    
    _putDontNest(result, -2580, -2584);
    
    _putDontNest(result, -2575, -2609);
    
    _putDontNest(result, -2851, -2856);
    
    _putDontNest(result, -2388, -2634);
    
    _putDontNest(result, -2513, -2634);
    
    _putDontNest(result, -2504, -2629);
    
    _putDontNest(result, -2489, -2594);
    
    _putDontNest(result, -2388, -2504);
    
    _putDontNest(result, -2399, -2507);
    
    _putDontNest(result, -2609, -2619);
    
    _putDontNest(result, -2574, -2594);
    
    _putDontNest(result, -2366, -2624);
    
    _putDontNest(result, -2361, -2609);
    
    _putDontNest(result, -2529, -2634);
    
    _putDontNest(result, -2530, -2639);
    
    _putDontNest(result, -2388, -2579);
    
    _putDontNest(result, -2279, -2504);
    
    _putDontNest(result, -2508, -2549);
    
    _putDontNest(result, -2559, -2554);
    
    _putDontNest(result, -2504, -2529);
    
    _putDontNest(result, -57, -130);
    
    _putDontNest(result, -2604, -2624);
    
    _putDontNest(result, -2842, -2861);
    
    _putDontNest(result, -2589, -2599);
    
    _putDontNest(result, -2590, -2594);
    
    _putDontNest(result, -2584, -2604);
    
    _putDontNest(result, -2545, -2634);
    
    _putDontNest(result, -2476, -2609);
    
    _putDontNest(result, -2507, -2534);
    
    _putDontNest(result, -2522, -2539);
    
    _putDontNest(result, -2399, -2539);
    
    _putDontNest(result, -2579, -2629);
    
    _putDontNest(result, -2565, -2639);
    
    _putDontNest(result, -523, -714);
    
    _putDontNest(result, -2307, -2639);
    
    _putDontNest(result, -2507, -2564);
    
    _putDontNest(result, -2555, -2644);
    
    _putDontNest(result, -2518, -2619);
    
    _putDontNest(result, -2560, -2653);
    
    _putDontNest(result, -2749, -2846);
    
    _putDontNest(result, -2279, -2594);
    
    _putDontNest(result, -36, -118);
    
    _putDontNest(result, -2476, -2549);
    
    _putDontNest(result, -2366, -2534);
    
    _putDontNest(result, -2399, -2491);
    
    _putDontNest(result, -2377, -2609);
    
    _putDontNest(result, -2508, -2609);
    
    _putDontNest(result, -2523, -2564);
    
    _putDontNest(result, -2544, -2653);
    
    _putDontNest(result, -2549, -2614);
    
    _putDontNest(result, -2539, -2644);
    
    _putDontNest(result, -2554, -2599);
    
    _putDontNest(result, -2416, -2574);
    
    _putDontNest(result, -2539, -2564);
    
    _putDontNest(result, -2559, -2624);
    
    _putDontNest(result, -2535, -2584);
    
    _putDontNest(result, -2523, -2644);
    
    _putDontNest(result, -2501, -2614);
    
    _putDontNest(result, -2550, -2619);
    
    _putDontNest(result, -2476, -2517);
    
    _putDontNest(result, -2570, -2579);
    
    _putDontNest(result, -2614, -2634);
    
    _putDontNest(result, -2595, -2629);
    
    _putDontNest(result, -2635, -2653);
    
    _putDontNest(result, -2517, -2614);
    
    _putDontNest(result, -2522, -2599);
    
    _putDontNest(result, -2555, -2564);
    
    _putDontNest(result, -2534, -2619);
    
    _putDontNest(result, -2512, -2653);
    
    _putDontNest(result, -2507, -2644);
    
    _putDontNest(result, -2540, -2609);
    
    _putDontNest(result, -2476, -2501);
    
    _putDontNest(result, -2630, -2639);
    
    _putDontNest(result, -2569, -2574);
    
    _putDontNest(result, -2629, -2634);
    
    _putDontNest(result, -2599, -2604);
    
    _putDontNest(result, -2361, -2644);
    
    _putDontNest(result, -2366, -2653);
    
    _putDontNest(result, -2476, -2594);
    
    _putDontNest(result, -2517, -2629);
    
    _putDontNest(result, -2560, -2589);
    
    _putDontNest(result, -2507, -2517);
    
    _putDontNest(result, -417, -427);
    
    _putDontNest(result, -422, -432);
    
    _putDontNest(result, -2539, -2549);
    
    _putDontNest(result, -2399, -2554);
    
    _putDontNest(result, -2579, -2584);
    
    _putDontNest(result, -2609, -2614);
    
    _putDontNest(result, -2604, -2653);
    
    _putDontNest(result, -606, -714);
    
    _putDontNest(result, -2388, -2639);
    
    _putDontNest(result, -2501, -2629);
    
    _putDontNest(result, -2544, -2589);
    
    _putDontNest(result, -2512, -2554);
    
    _putDontNest(result, -2619, -2624);
    
    _putDontNest(result, -2574, -2599);
    
    _putDontNest(result, -2489, -2609);
    
    _putDontNest(result, -2549, -2629);
    
    _putDontNest(result, -2399, -2589);
    
    _putDontNest(result, -2507, -2549);
    
    _putDontNest(result, -2841, -2861);
    
    _putDontNest(result, -2575, -2644);
    
    _putDontNest(result, -2589, -2594);
    
    _putDontNest(result, -2590, -2599);
    
    _putDontNest(result, -2594, -2619);
    
    _putDontNest(result, -2361, -2564);
    
    _putDontNest(result, -2766, -2846);
    
    _putDontNest(result, -2512, -2589);
    
    _putDontNest(result, -2279, -2629);
    
    _putDontNest(result, -2523, -2549);
    
    _putDontNest(result, -2508, -2534);
    
    _putDontNest(result, -2544, -2554);
    
    _putDontNest(result, -2580, -2629);
    
    _putDontNest(result, -2565, -2634);
    
    _putDontNest(result, -526, -714);
    
    _putDontNest(result, -2377, -2564);
    
    _putDontNest(result, -2307, -2634);
    
    _putDontNest(result, -2512, -2574);
    
    _putDontNest(result, -2540, -2594);
    
    _putDontNest(result, -2530, -2604);
    
    _putDontNest(result, -2539, -2599);
    
    _putDontNest(result, -2554, -2644);
    
    _putDontNest(result, -2276, -2604);
    
    _putDontNest(result, -2564, -2629);
    
    _putDontNest(result, -2399, -2574);
    
    _putDontNest(result, -2555, -2599);
    
    _putDontNest(result, -2522, -2564);
    
    _putDontNest(result, -2518, -2584);
    
    _putDontNest(result, -2513, -2569);
    
    _putDontNest(result, -2292, -2604);
    
    _putDontNest(result, -2489, -2539);
    
    _putDontNest(result, -2476, -2534);
    
    _putDontNest(result, -2766, -2851);
    
    _putDontNest(result, -2585, -2589);
    
    _putDontNest(result, -2522, -2644);
    
    _putDontNest(result, -2507, -2599);
    
    _putDontNest(result, -2529, -2569);
    
    _putDontNest(result, -2498, -2604);
    
    _putDontNest(result, -2508, -2594);
    
    _putDontNest(result, -2534, -2584);
    
    _putDontNest(result, -2504, -2614);
    
    _putDontNest(result, -2544, -2574);
    
    _putDontNest(result, -2366, -2559);
    
    _putDontNest(result, -2399, -2522);
    
    _putDontNest(result, -2569, -2589);
    
    _putDontNest(result, -2614, -2639);
    
    _putDontNest(result, -2639, -2644);
    
    _putDontNest(result, -2377, -2644);
    
    _putDontNest(result, -2560, -2574);
    
    _putDontNest(result, -2550, -2584);
    
    _putDontNest(result, -2535, -2619);
    
    _putDontNest(result, -2545, -2569);
    
    _putDontNest(result, -2523, -2599);
    
    _putDontNest(result, -2554, -2564);
    
    _putDontNest(result, -2489, -2507);
    
    _putDontNest(result, -2512, -2522);
    
    _putDontNest(result, -523, -523);
    
    _putDontNest(result, -2589, -2609);
    
    _putDontNest(result, -2579, -2619);
    
    _putDontNest(result, -2565, -2569);
    
    _putDontNest(result, -2476, -2599);
    
    _putDontNest(result, -2518, -2629);
    
    _putDontNest(result, -2489, -2644);
    
    _putDontNest(result, -2559, -2579);
    
    _putDontNest(result, -2534, -2529);
    
    _putDontNest(result, -2377, -2507);
    
    _putDontNest(result, -2749, -2851);
    
    _putDontNest(result, -2570, -2624);
    
    _putDontNest(result, -515, -515);
    
    _putDontNest(result, -2625, -2629);
    
    _putDontNest(result, -2366, -2594);
    
    _putDontNest(result, -2416, -2589);
    
    _putDontNest(result, -2279, -2534);
    
    _putDontNest(result, -2559, -2544);
    
    _putDontNest(result, -417, -422);
    
    _putDontNest(result, -2512, -2559);
    
    _putDontNest(result, -2522, -2517);
    
    _putDontNest(result, -2554, -2549);
    
    _putDontNest(result, -2585, -2653);
    
    _putDontNest(result, -2590, -2644);
    
    _putDontNest(result, -2594, -2584);
    
    _putDontNest(result, -2564, -2614);
    
    _putDontNest(result, -2575, -2599);
    
    _putDontNest(result, -2550, -2629);
    
    _putDontNest(result, -2508, -2539);
    
    _putDontNest(result, -2377, -2539);
    
    _putDontNest(result, -2361, -2491);
    
    _putDontNest(result, -2595, -2619);
    
    _putDontNest(result, -2569, -2653);
    
    _putDontNest(result, -2605, -2609);
    
    _putDontNest(result, -2580, -2614);
    
    _putDontNest(result, -2574, -2644);
    
    _putDontNest(result, -2846, -2851);
    
    _putDontNest(result, -510, -714);
    
    _putDontNest(result, -2534, -2629);
    
    _putDontNest(result, -2489, -2564);
    
    _putDontNest(result, -2544, -2559);
    
    _putDontNest(result, -2522, -2549);
    
    _putDontNest(result, -2476, -2491);
    
    _putDontNest(result, -2416, -2554);
    
    _putDontNest(result, -2399, -2609);
    
    _putDontNest(result, -2498, -2569);
    
    _putDontNest(result, -2522, -2609);
    
    _putDontNest(result, -2535, -2614);
    
    _putDontNest(result, -2501, -2584);
    
    _putDontNest(result, -2540, -2599);
    
    _putDontNest(result, -2529, -2604);
    
    _putDontNest(result, -2539, -2594);
    
    _putDontNest(result, -2366, -2579);
    
    _putDontNest(result, -2476, -2539);
    
    _putDontNest(result, -2489, -2534);
    
    _putDontNest(result, -2584, -2634);
    
    _putDontNest(result, -2388, -2584);
    
    _putDontNest(result, -2416, -2644);
    
    _putDontNest(result, -2307, -2629);
    
    _putDontNest(result, -2504, -2619);
    
    _putDontNest(result, -2545, -2604);
    
    _putDontNest(result, -2555, -2594);
    
    _putDontNest(result, -2517, -2584);
    
    _putDontNest(result, -2361, -2539);
    
    _putDontNest(result, -2416, -2522);
    
    _putDontNest(result, -2366, -2544);
    
    _putDontNest(result, -2377, -2491);
    
    _putDontNest(result, -2599, -2639);
    
    _putDontNest(result, -2600, -2634);
    
    _putDontNest(result, -2609, -2629);
    
    _putDontNest(result, -2416, -2564);
    
    _putDontNest(result, -2530, -2569);
    
    _putDontNest(result, -2507, -2594);
    
    _putDontNest(result, -2554, -2609);
    
    _putDontNest(result, -2508, -2599);
    
    _putDontNest(result, -2276, -2569);
    
    _putDontNest(result, -2279, -2584);
    
    _putDontNest(result, -2476, -2507);
    
    _putDontNest(result, -2570, -2589);
    
    _putDontNest(result, -2559, -2574);
    
    _putDontNest(result, -2549, -2584);
    
    _putDontNest(result, -2523, -2594);
    
    _putDontNest(result, -2513, -2604);
    
    _putDontNest(result, -2292, -2569);
    
    _putDontNest(result, -2361, -2507);
    
    _putDontNest(result, -2366, -2512);
    
    _putDontNest(result, -2595, -2614);
    
    _putDontNest(result, -2580, -2619);
    
    _putDontNest(result, -2590, -2609);
    
    _putDontNest(result, -2585, -2624);
    
    _putDontNest(result, -2388, -2629);
    
    _putDontNest(result, -2307, -2584);
    
    _putDontNest(result, -2501, -2639);
    
    _putDontNest(result, -2560, -2579);
    
    _putDontNest(result, -2279, -2549);
    
    _putDontNest(result, -2544, -2544);
    
    _putDontNest(result, -2539, -2539);
    
    _putDontNest(result, -2512, -2512);
    
    _putDontNest(result, -168, -173);
    
    _putDontNest(result, -2564, -2619);
    
    _putDontNest(result, -2605, -2644);
    
    _putDontNest(result, -523, -518);
    
    _putDontNest(result, -515, -526);
    
    _putDontNest(result, -2569, -2624);
    
    _putDontNest(result, -2574, -2609);
    
    _putDontNest(result, -2366, -2599);
    
    _putDontNest(result, -2518, -2634);
    
    _putDontNest(result, -2517, -2639);
    
    _putDontNest(result, -2544, -2579);
    
    _putDontNest(result, -2787, -2841);
    
    _putDontNest(result, -2399, -2512);
    
    _putDontNest(result, -2565, -2604);
    
    _putDontNest(result, -2834, -2856);
    
    _putDontNest(result, -2575, -2594);
    
    _putDontNest(result, -2589, -2644);
    
    _putDontNest(result, -2534, -2634);
    
    _putDontNest(result, -2771, -2841);
    
    _putDontNest(result, -2276, -2634);
    
    _putDontNest(result, -2399, -2579);
    
    _putDontNest(result, -2292, -2554);
    
    _putDontNest(result, -2279, -2517);
    
    _putDontNest(result, -2559, -2559);
    
    _putDontNest(result, -2522, -2534);
    
    _putDontNest(result, -2512, -2544);
    
    _putDontNest(result, -2501, -2529);
    
    _putDontNest(result, -2507, -2539);
    
    _putDontNest(result, -2579, -2614);
    
    _putDontNest(result, -2570, -2653);
    
    _putDontNest(result, -2361, -2574);
    
    _putDontNest(result, -2549, -2639);
    
    _putDontNest(result, -2476, -2624);
    
    _putDontNest(result, -2550, -2634);
    
    _putDontNest(result, -2512, -2579);
    
    _putDontNest(result, -2292, -2634);
    
    _putDontNest(result, -2279, -2501);
    
    _putDontNest(result, -2276, -2554);
    
    _putDontNest(result, -2523, -2539);
    
    _putDontNest(result, -2517, -2529);
    
    _putDontNest(result, -2377, -2534);
    
    _putDontNest(result, -2399, -2544);
    
    _putDontNest(result, -2416, -2559);
    
    _putDontNest(result, -2377, -2574);
    
    _putDontNest(result, -2512, -2564);
    
    _putDontNest(result, -2530, -2614);
    
    _putDontNest(result, -2560, -2644);
    
    _putDontNest(result, -2555, -2653);
    
    _putDontNest(result, -2513, -2619);
    
    _putDontNest(result, -2752, -2846);
    
    _putDontNest(result, -2292, -2522);
    
    _putDontNest(result, -2361, -2534);
    
    _putDontNest(result, -2584, -2639);
    
    _putDontNest(result, -2399, -2564);
    
    _putDontNest(result, -2508, -2624);
    
    _putDontNest(result, -2544, -2644);
    
    _putDontNest(result, -2539, -2653);
    
    _putDontNest(result, -2522, -2574);
    
    _putDontNest(result, -2276, -2522);
    
    _putDontNest(result, -2489, -2549);
    
    _putDontNest(result, -2599, -2634);
    
    _putDontNest(result, -2600, -2639);
    
    _putDontNest(result, -2399, -2644);
    
    _putDontNest(result, -2504, -2604);
    
    _putDontNest(result, -2523, -2653);
    
    _putDontNest(result, -2535, -2569);
    
    _putDontNest(result, -2545, -2619);
    
    _putDontNest(result, -2559, -2609);
    
    _putDontNest(result, -2544, -2564);
    
    _putDontNest(result, -2498, -2614);
    
    _putDontNest(result, -2489, -2501);
    
    _putDontNest(result, -2594, -2629);
    
    _putDontNest(result, -2634, -2653);
    
    _putDontNest(result, -2416, -2609);
    
    _putDontNest(result, -2512, -2644);
    
    _putDontNest(result, -2507, -2653);
    
    _putDontNest(result, -2554, -2574);
    
    _putDontNest(result, -2540, -2624);
    
    _putDontNest(result, -2529, -2619);
    
    _putDontNest(result, -2279, -2619);
    
    _putDontNest(result, -2489, -2517);
    
    _putDontNest(result, -2565, -2579);
    
    _putDontNest(result, -2366, -2644);
    
    _putDontNest(result, -2361, -2653);
    
    _putDontNest(result, -2501, -2634);
    
    _putDontNest(result, -2555, -2589);
    
    _putDontNest(result, -2529, -2544);
    
    _putDontNest(result, -2534, -2539);
    
    _putDontNest(result, -2504, -2517);
    
    _putDontNest(result, -2377, -2517);
    
    _putDontNest(result, -2399, -2559);
    
    _putDontNest(result, -2416, -2544);
    
    _putDontNest(result, -2366, -2522);
    
    _putDontNest(result, -2752, -2856);
    
    _putDontNest(result, -2584, -2624);
    
    _putDontNest(result, -2594, -2594);
    
    _putDontNest(result, -2604, -2604);
    
    _putDontNest(result, -2564, -2564);
    
    _putDontNest(result, -2579, -2609);
    
    _putDontNest(result, -2634, -2634);
    
    _putDontNest(result, -2589, -2619);
    
    _putDontNest(result, -606, -713);
    
    _putDontNest(result, -2307, -2619);
    
    _putDontNest(result, -2476, -2653);
    
    _putDontNest(result, -2518, -2639);
    
    _putDontNest(result, -2517, -2634);
    
    _putDontNest(result, -2539, -2589);
    
    _putDontNest(result, -2416, -2579);
    
    _putDontNest(result, -2498, -2559);
    
    _putDontNest(result, -2377, -2501);
    
    _putDontNest(result, -518, -518);
    
    _putDontNest(result, -2599, -2653);
    
    _putDontNest(result, -526, -526);
    
    _putDontNest(result, -2534, -2639);
    
    _putDontNest(result, -2523, -2589);
    
    _putDontNest(result, -2276, -2639);
    
    _putDontNest(result, -2292, -2559);
    
    _putDontNest(result, -2508, -2529);
    
    _putDontNest(result, -2504, -2549);
    
    _putDontNest(result, -2377, -2549);
    
    _putDontNest(result, -2416, -2512);
    
    _putDontNest(result, -2580, -2644);
    
    _putDontNest(result, -2574, -2614);
    
    _putDontNest(result, -2366, -2564);
    
    _putDontNest(result, -2549, -2634);
    
    _putDontNest(result, -2489, -2574);
    
    _putDontNest(result, -2550, -2639);
    
    _putDontNest(result, -2507, -2589);
    
    _putDontNest(result, -2292, -2639);
    
    _putDontNest(result, -2276, -2559);
    
    _putDontNest(result, -2513, -2544);
    
    _putDontNest(result, -2518, -2539);
    
    _putDontNest(result, -2530, -2559);
    
    _putDontNest(result, -2595, -2609);
    
    _putDontNest(result, -2590, -2614);
    
    _putDontNest(result, -2564, -2644);
    
    _putDontNest(result, -2605, -2619);
    
    _putDontNest(result, -2600, -2624);
    
    _putDontNest(result, -526, -713);
    
    _putDontNest(result, -2416, -2594);
    
    _putDontNest(result, -2559, -2644);
    
    _putDontNest(result, -2535, -2604);
    
    _putDontNest(result, -2529, -2614);
    
    _putDontNest(result, -2523, -2624);
    
    _putDontNest(result, -2504, -2569);
    
    _putDontNest(result, -2489, -2579);
    
    _putDontNest(result, -2366, -2589);
    
    _putDontNest(result, -2279, -2614);
    
    _putDontNest(result, -2476, -2529);
    
    _putDontNest(result, -2787, -2851);
    
    _putDontNest(result, -2569, -2639);
    
    _putDontNest(result, -2570, -2634);
    
    _putDontNest(result, -2498, -2619);
    
    _putDontNest(result, -2545, -2614);
    
    _putDontNest(result, -2512, -2609);
    
    _putDontNest(result, -2540, -2653);
    
    _putDontNest(result, -2507, -2624);
    
    _putDontNest(result, -2361, -2549);
    
    _putDontNest(result, -2575, -2629);
    
    _putDontNest(result, -2585, -2639);
    
    _putDontNest(result, -2842, -2846);
    
    _putDontNest(result, -2837, -2841);
    
    _putDontNest(result, -2560, -2609);
    
    _putDontNest(result, -2555, -2624);
    
    _putDontNest(result, -57, -125);
    
    _putDontNest(result, -2366, -2554);
    
    _putDontNest(result, -2361, -2501);
    
    _putDontNest(result, -2584, -2589);
    
    _putDontNest(result, -2644, -2644);
    
    _putDontNest(result, -2388, -2614);
    
    _putDontNest(result, -2399, -2599);
    
    _putDontNest(result, -2377, -2653);
    
    _putDontNest(result, -2539, -2624);
    
    _putDontNest(result, -2508, -2653);
    
    _putDontNest(result, -2559, -2564);
    
    _putDontNest(result, -2544, -2609);
    
    _putDontNest(result, -2530, -2619);
    
    _putDontNest(result, -2513, -2614);
    
    _putDontNest(result, -2489, -2504);
    
    _putDontNest(result, -2361, -2517);
    
    _putDontNest(result, -2771, -2851);
    
    _putDontNest(result, -2504, -2634);
    
    _putDontNest(result, -2489, -2653);
    
    _putDontNest(result, -2513, -2629);
    
    _putDontNest(result, -2554, -2579);
    
    _putDontNest(result, -2276, -2544);
    
    _putDontNest(result, -2279, -2539);
    
    _putDontNest(result, -2535, -2549);
    
    _putDontNest(result, -2513, -2559);
    
    _putDontNest(result, -2498, -2512);
    
    _putDontNest(result, -2530, -2544);
    
    _putDontNest(result, -2377, -2504);
    
    _putDontNest(result, -2749, -2856);
    
    _putDontNest(result, -2590, -2619);
    
    _putDontNest(result, -2634, -2639);
    
    _putDontNest(result, -2851, -2851);
    
    _putDontNest(result, -518, -523);
    
    _putDontNest(result, -2594, -2599);
    
    _putDontNest(result, -2580, -2609);
    
    _putDontNest(result, -2575, -2584);
    
    _putDontNest(result, -2565, -2574);
    
    _putDontNest(result, -2605, -2614);
    
    _putDontNest(result, -2540, -2589);
    
    _putDontNest(result, -2292, -2544);
    
    _putDontNest(result, -2564, -2609);
    
    _putDontNest(result, -526, -515);
    
    _putDontNest(result, -2600, -2653);
    
    _putDontNest(result, -2574, -2619);
    
    _putDontNest(result, -2595, -2644);
    
    _putDontNest(result, -2307, -2614);
    
    _putDontNest(result, -2366, -2609);
    
    _putDontNest(result, -2361, -2624);
    
    _putDontNest(result, -2476, -2574);
    
    _putDontNest(result, -2545, -2629);
    
    _putDontNest(result, -2535, -2639);
    
    _putDontNest(result, -2522, -2579);
    
    _putDontNest(result, -2279, -2507);
    
    _putDontNest(result, -2276, -2512);
    
    _putDontNest(result, -2498, -2544);
    
    _putDontNest(result, -2501, -2539);
    
    _putDontNest(result, -2507, -2529);
    
    _putDontNest(result, -2579, -2644);
    
    _putDontNest(result, -2584, -2653);
    
    _putDontNest(result, -611, -713);
    
    _putDontNest(result, -510, -713);
    
    _putDontNest(result, -2529, -2629);
    
    _putDontNest(result, -2508, -2589);
    
    _putDontNest(result, -2377, -2579);
    
    _putDontNest(result, -2292, -2512);
    
    _putDontNest(result, -413, -427);
    
    _putDontNest(result, -2517, -2539);
    
    _putDontNest(result, -2529, -2559);
    
    _putDontNest(result, -2504, -2534);
    
    _putDontNest(result, -2307, -2522);
    
    _putDontNest(result, -36, -130);
    
    _putDontNest(result, -2589, -2614);
    
    _putDontNest(result, -2599, -2624);
    
    _putDontNest(result, -2837, -2861);
    
    _putDontNest(result, -2388, -2619);
    
    _putDontNest(result, -2416, -2599);
    
    _putDontNest(result, -2508, -2574);
    
    _putDontNest(result, -2501, -2569);
    
    _putDontNest(result, -2544, -2594);
    
    _putDontNest(result, -2522, -2624);
    
    _putDontNest(result, -2534, -2604);
    
    _putDontNest(result, -2498, -2584);
    
    _putDontNest(result, -2361, -2579);
    
    _putDontNest(result, -2569, -2634);
    
    _putDontNest(result, -2570, -2639);
    
    _putDontNest(result, -515, -713);
    
    _putDontNest(result, -2377, -2624);
    
    _putDontNest(result, -2560, -2594);
    
    _putDontNest(result, -2559, -2599);
    
    _putDontNest(result, -2550, -2604);
    
    _putDontNest(result, -2517, -2569);
    
    _putDontNest(result, -2476, -2589);
    
    _putDontNest(result, -2307, -2554);
    
    _putDontNest(result, -2585, -2634);
    
    _putDontNest(result, -2554, -2624);
    
    _putDontNest(result, -2512, -2594);
    
    _putDontNest(result, -2530, -2584);
    
    _putDontNest(result, -2540, -2574);
    
    _putDontNest(result, -2276, -2584);
    
    _putDontNest(result, -2279, -2569);
    
    _putDontNest(result, -58, -125);
    
    _putDontNest(result, -2476, -2498);
    
    _putDontNest(result, -2624, -2629);
    
    _putDontNest(result, -2399, -2594);
    
    _putDontNest(result, -2549, -2569);
    
    _putDontNest(result, -2824, -2841);
    
    _putDontNest(result, -2518, -2604);
    
    _putDontNest(result, -2292, -2584);
    
    _putDontNest(result, -2279, -2491);
    
    _putDontNest(result, -42, -125);
    
    _putDontNest(result, -2361, -2504);
    
    _putDontNest(result, -2565, -2589);
    
    _putDontNest(result, -2504, -2639);
    
    _putDontNest(result, -2555, -2579);
    
    _putDontNest(result, -2276, -2549);
    
    _putDontNest(result, -2513, -2554);
    
    _putDontNest(result, -2534, -2549);
    
    _putDontNest(result, -2388, -2554);
    
    _putDontNest(result, -2569, -2569);
    
    _putDontNest(result, -2574, -2584);
    
    _putDontNest(result, -2595, -2599);
    
    _putDontNest(result, -2856, -2861);
    
    _putDontNest(result, -2377, -2639);
    
    _putDontNest(result, -2399, -2629);
    
    _putDontNest(result, -2498, -2629);
    
    _putDontNest(result, -2539, -2579);
    
    _putDontNest(result, -2292, -2549);
    
    _putDontNest(result, -2624, -2614);
    
    _putDontNest(result, -2594, -2644);
    
    _putDontNest(result, -2575, -2619);
    
    _putDontNest(result, -2590, -2584);
    
    _putDontNest(result, -2366, -2614);
    
    _putDontNest(result, -2307, -2609);
    
    _putDontNest(result, -2279, -2589);
    
    _putDontNest(result, -2535, -2634);
    
    _putDontNest(result, -2489, -2624);
    
    _putDontNest(result, -2523, -2579);
    
    _putDontNest(result, -2292, -2629);
    
    _putDontNest(result, -2276, -2517);
    
    _putDontNest(result, -2292, -2501);
    
    _putDontNest(result, -2534, -2517);
    
    _putDontNest(result, -2504, -2539);
    
    _putDontNest(result, -413, -422);
    
    _putDontNest(result, -2517, -2534);
    
    _putDontNest(result, -2609, -2609);
    
    _putDontNest(result, -2564, -2594);
    
    _putDontNest(result, -2614, -2624);
    
    _putDontNest(result, -2824, -2861);
    
    _putDontNest(result, -2570, -2604);
    
    _putDontNest(result, -2530, -2629);
    
    _putDontNest(result, -2507, -2579);
    
    _putDontNest(result, -2276, -2629);
    
    _putDontNest(result, -2292, -2517);
    
    _putDontNest(result, -2279, -2554);
    
    _putDontNest(result, -2276, -2501);
    
    _putDontNest(result, -2522, -2529);
    
    _putDontNest(result, -2501, -2534);
    
    _putDontNest(result, -2518, -2549);
    
    _putDontNest(result, -2529, -2554);
    
    _putDontNest(result, -2565, -2653);
    
    _putDontNest(result, -2579, -2599);
    
    _putDontNest(result, -2580, -2594);
    
    _putDontNest(result, -2834, -2851);
    
    _putDontNest(result, -2544, -2599);
    
    _putDontNest(result, -2507, -2574);
    
    _putDontNest(result, -2554, -2653);
    
    _putDontNest(result, -2489, -2589);
    
    _putDontNest(result, -2279, -2604);
    
    _putDontNest(result, -2589, -2629);
    
    _putDontNest(result, -2560, -2599);
    
    _putDontNest(result, -2523, -2574);
    
    _putDontNest(result, -2549, -2604);
    
    _putDontNest(result, -2559, -2594);
    
    _putDontNest(result, -2513, -2584);
    
    _putDontNest(result, -2518, -2569);
    
    _putDontNest(result, -2279, -2522);
    
    _putDontNest(result, -2529, -2522);
    
    _putDontNest(result, -57, -111);
    
    _putDontNest(result, -2388, -2564);
    
    _putDontNest(result, -2522, -2653);
    
    _putDontNest(result, -2512, -2599);
    
    _putDontNest(result, -2539, -2574);
    
    _putDontNest(result, -2501, -2604);
    
    _putDontNest(result, -2529, -2584);
    
    _putDontNest(result, -2534, -2569);
    
    _putDontNest(result, -2388, -2522);
    
    _putDontNest(result, -2584, -2579);
    
    _putDontNest(result, -2645, -2653);
    
    _putDontNest(result, -2604, -2634);
    
    _putDontNest(result, -2388, -2644);
    
    _putDontNest(result, -2416, -2584);
    
    _putDontNest(result, -2517, -2604);
    
    _putDontNest(result, -2545, -2584);
    
    _putDontNest(result, -2550, -2569);
    
    _putDontNest(result, -2555, -2574);
    
    _putDontNest(result, -2489, -2498);
    
    _putDontNest(result, -2766, -2856);
    
    _putDontNest(result, -2605, -2629);
    
    _putDontNest(result, -2629, -2653);
    
    _putDontNest(result, -2619, -2639);
    
    _putDontNest(result, -2361, -2639);
    
    _putDontNest(result, -2554, -2589);
    
    _putDontNest(result, -2292, -2534);
    
    _putDontNest(result, -2501, -2517);
    
    _putDontNest(result, -2388, -2559);
    
    _putDontNest(result, -2377, -2498);
    
    _putDontNest(result, -2570, -2569);
    
    _putDontNest(result, -2609, -2644);
    
    _putDontNest(result, -2614, -2653);
    
    _putDontNest(result, -2307, -2564);
    
    _putDontNest(result, -2377, -2634);
    
    _putDontNest(result, -2476, -2604);
    
    _putDontNest(result, -2540, -2579);
    
    _putDontNest(result, -2276, -2534);
    
    _putDontNest(result, -2517, -2517);
    
    _putDontNest(result, -2534, -2534);
    
    _putDontNest(result, -417, -417);
    
    _putDontNest(result, -2549, -2549);
    
    _putDontNest(result, -2498, -2554);
    
    _putDontNest(result, -2589, -2584);
    
    _putDontNest(result, -2565, -2624);
    
    _putDontNest(result, -2522, -2634);
    
    _putDontNest(result, -2522, -2589);
    
    _putDontNest(result, -2279, -2639);
    
    _putDontNest(result, -2501, -2549);
    
    _putDontNest(result, -2624, -2619);
    
    _putDontNest(result, -2564, -2599);
    
    _putDontNest(result, -2846, -2856);
    
    _putDontNest(result, -2569, -2604);
    
    _putDontNest(result, -2575, -2614);
    
    _putDontNest(result, -2366, -2619);
    
    _putDontNest(result, -2559, -2629);
    
    _putDontNest(result, -2508, -2579);
    
    _putDontNest(result, -2377, -2589);
    
    _putDontNest(result, -412, -432);
    
    _putDontNest(result, -2517, -2549);
    
    _putDontNest(result, -2530, -2554);
    
    _putDontNest(result, -2585, -2604);
    
    _putDontNest(result, -2594, -2609);
    
    _putDontNest(result, -2579, -2594);
    
    _putDontNest(result, -2580, -2599);
    
    _putDontNest(result, -2416, -2629);
    
    _putDontNest(result, -2307, -2644);
    
    _putDontNest(result, -2554, -2634);
    
    _putDontNest(result, -2276, -2614);
    
    _putDontNest(result, -2361, -2589);
    
    _putDontNest(result, -42, -111);
    
    _putDontNest(result, -2307, -2512);
    
    _putDontNest(result, -2590, -2629);
    
    _putDontNest(result, -2388, -2609);
    
    _putDontNest(result, -2507, -2569);
    
    _putDontNest(result, -2540, -2604);
    
    _putDontNest(result, -2530, -2594);
    
    _putDontNest(result, -2512, -2584);
    
    _putDontNest(result, -2529, -2599);
    
    _putDontNest(result, -2476, -2579);
    
    _putDontNest(result, -2292, -2614);
    
    _putDontNest(result, -2489, -2529);
    
    _putDontNest(result, -58, -111);
    
    _putDontNest(result, -2574, -2629);
    
    _putDontNest(result, -2399, -2584);
    
    _putDontNest(result, -2518, -2574);
    
    _putDontNest(result, -2504, -2624);
    
    _putDontNest(result, -2535, -2653);
    
    _putDontNest(result, -2523, -2569);
    
    _putDontNest(result, -2545, -2599);
    
    _putDontNest(result, -2476, -2504);
    
    _putDontNest(result, -2307, -2544);
    
    _putDontNest(result, -2579, -2589);
    
    _putDontNest(result, -2604, -2639);
    
    _putDontNest(result, -2842, -2841);
    
    _putDontNest(result, -2498, -2594);
    
    _putDontNest(result, -2837, -2846);
    
    _putDontNest(result, -2508, -2604);
    
    _putDontNest(result, -2539, -2569);
    
    _putDontNest(result, -2544, -2584);
    
    _putDontNest(result, -2534, -2574);
    
    _putDontNest(result, -2498, -2522);
    
    _putDontNest(result, -2361, -2498);
    
    _putDontNest(result, -2625, -2644);
    
    _putDontNest(result, -2630, -2653);
    
    _putDontNest(result, -2619, -2634);
    
    _putDontNest(result, -2361, -2634);
    
    _putDontNest(result, -2555, -2569);
    
    _putDontNest(result, -2513, -2599);
    
    _putDontNest(result, -2489, -2639);
    
    _putDontNest(result, -2550, -2574);
    
    _putDontNest(result, -2560, -2584);
    
    _putDontNest(result, -2279, -2544);
    
    _putDontNest(result, -2276, -2539);
    
    _putDontNest(result, -2518, -2559);
    
    _putDontNest(result, -2530, -2539);
    
    _putDontNest(result, -2501, -2512);
    
    _putDontNest(result, -173, -173);
    
    _putDontNest(result, -2585, -2619);
    
    _putDontNest(result, -2856, -2851);
    
    _putDontNest(result, -2580, -2624);
    
    _putDontNest(result, -710, -714);
    
    _putDontNest(result, -2549, -2579);
    
    _putDontNest(result, -2292, -2539);
    
    _putDontNest(result, -2549, -2544);
    
    _putDontNest(result, -2388, -2512);
    
    _putDontNest(result, -2600, -2644);
    
    _putDontNest(result, -2595, -2653);
    
    _putDontNest(result, -2569, -2619);
    
    _putDontNest(result, -2564, -2624);
    
    _putDontNest(result, -2279, -2579);
    
    _putDontNest(result, -2512, -2629);
    
    _putDontNest(result, -2522, -2639);
    
    _putDontNest(result, -2535, -2589);
    
    _putDontNest(result, -2279, -2634);
    
    _putDontNest(result, -2279, -2512);
    
    _putDontNest(result, -2276, -2507);
    
    _putDontNest(result, -2498, -2539);
    
    _putDontNest(result, -2512, -2529);
    
    _putDontNest(result, -2501, -2544);
    
    _putDontNest(result, -2377, -2529);
    
    _putDontNest(result, -2824, -2851);
    
    _putDontNest(result, -2570, -2614);
    
    _putDontNest(result, -2584, -2644);
    
    _putDontNest(result, -2565, -2599);
    
    _putDontNest(result, -2579, -2653);
    
    _putDontNest(result, -611, -714);
    
    _putDontNest(result, -2307, -2599);
    
    _putDontNest(result, -2560, -2629);
    
    _putDontNest(result, -2476, -2569);
    
    _putDontNest(result, -2517, -2579);
    
    _putDontNest(result, -2292, -2507);
    
    _putDontNest(result, -2534, -2559);
    
    _putDontNest(result, -413, -432);
    
    _putDontNest(result, -2535, -2554);
    
    _putDontNest(result, -2517, -2544);
    
    _putDontNest(result, -2388, -2544);
    
    _putDontNest(result, -36, -133);
    
    _putDontNest(result, -2834, -2861);
    
    _putDontNest(result, -2599, -2609);
    
    _putDontNest(result, -2366, -2584);
    
    _putDontNest(result, -2361, -2569);
    
    _putDontNest(result, -2544, -2629);
    
    _putDontNest(result, -2554, -2639);
    
    _putDontNest(result, -2501, -2579);
    
    _putDontNest(result, -2574, -2634);
    
    _putDontNest(result, -515, -714);
    
    _putDontNest(result, -2399, -2619);
    
    _putDontNest(result, -2416, -2614);
    
    _putDontNest(result, -2377, -2569);
    
    _putDontNest(result, -2539, -2604);
    
    _putDontNest(result, -2529, -2594);
    
    _putDontNest(result, -2530, -2599);
    
    _putDontNest(result, -2501, -2574);
    
    _putDontNest(result, -2508, -2569);
    
    _putDontNest(result, -2361, -2529);
    
    _putDontNest(result, -2307, -2559);
    
    _putDontNest(result, -2594, -2579);
    
    _putDontNest(result, -2589, -2639);
    
    _putDontNest(result, -2590, -2634);
    
    _putDontNest(result, -2545, -2594);
    
    _putDontNest(result, -2555, -2604);
    
    _putDontNest(result, -2517, -2574);
    
    _putDontNest(result, -2292, -2619);
    
    _putDontNest(result, -2292, -2491);
    
    _putDontNest(result, -2580, -2589);
    
    _putDontNest(result, -2605, -2639);
    
    _putDontNest(result, -2619, -2629);
    
    _putDontNest(result, -2540, -2569);
    
    _putDontNest(result, -2507, -2604);
    
    _putDontNest(result, -2498, -2599);
    
    _putDontNest(result, -2276, -2619);
    
    _putDontNest(result, -2276, -2491);
    
    _putDontNest(result, -2564, -2589);
    
    _putDontNest(result, -2388, -2594);
    
    _putDontNest(result, -2549, -2574);
    
    _putDontNest(result, -2523, -2604);
    
    _putDontNest(result, -2504, -2653);
    
    _putDontNest(result, -2535, -2624);
    
    _putDontNest(result, -2489, -2634);
    
    _putDontNest(result, -2513, -2594);
    
    _putDontNest(result, -2559, -2584);
    
    _putDontNest(result, -2529, -2539);
    
    _putDontNest(result, -2534, -2544);
    
    _putDontNest(result, -2517, -2559);
    
    _putDontNest(result, -2388, -2501);
    
    _putDontNest(result, -2416, -2529);
    
    _putDontNest(result, -2579, -2624);
    
    _putDontNest(result, -2584, -2609);
    
    _putDontNest(result, -582, -714);
    
    _putDontNest(result, -2508, -2634);
    
    _putDontNest(result, -2507, -2639);
    
    _putDontNest(result, -2550, -2579);
    
    _putDontNest(result, -2504, -2554);
    
    _putDontNest(result, -2501, -2559);
    
    _putDontNest(result, -2399, -2498);
    
    _putDontNest(result, -2388, -2517);
    
    _putDontNest(result, -2570, -2619);
    
    _putDontNest(result, -2599, -2644);
    
    _putDontNest(result, -2361, -2604);
    
    _putDontNest(result, -2523, -2639);
    
    _putDontNest(result, -2534, -2579);
    
    _putDontNest(result, -412, -422);
    
    _putDontNest(result, -2549, -2559);
    
    _putDontNest(result, -2565, -2594);
    
    _putDontNest(result, -2580, -2653);
    
    _putDontNest(result, -2569, -2614);
    
    _putDontNest(result, -2575, -2604);
    
    _putDontNest(result, -2307, -2594);
    
    _putDontNest(result, -2539, -2639);
    
    _putDontNest(result, -2540, -2634);
    
    _putDontNest(result, -2518, -2579);
    
    _putDontNest(result, -2279, -2559);
    
    _putDontNest(result, -2513, -2539);
    
    _putDontNest(result, -2518, -2544);
    
    _putDontNest(result, -2388, -2549);
    
    _putDontNest(result, -2600, -2609);
    
    _putDontNest(result, -2564, -2653);
    
    _putDontNest(result, -2595, -2624);
    
    _putDontNest(result, -2585, -2614);
    
    _putDontNest(result, -2489, -2569);
    
    _putDontNest(result, -2555, -2639);
    
    _putDontNest(result, -2504, -2589);
    
    _putDontNest(result, -2574, -2639);
    
    _putDontNest(result, -518, -714);
    
    _putDontNest(result, -2549, -2653);
    
    _putDontNest(result, -2504, -2574);
    
    _putDontNest(result, -2498, -2564);
    
    _putDontNest(result, -2544, -2614);
    
    _putDontNest(result, -2513, -2609);
    
    _putDontNest(result, -2518, -2624);
    
    _putDontNest(result, -2292, -2644);
    
    _putDontNest(result, -2589, -2634);
    
    _putDontNest(result, -2590, -2639);
    
    _putDontNest(result, -2530, -2644);
    
    _putDontNest(result, -2554, -2604);
    
    _putDontNest(result, -2560, -2614);
    
    _putDontNest(result, -2279, -2653);
    
    _putDontNest(result, -2276, -2644);
    
    _putDontNest(result, -2579, -2579);
    
    _putDontNest(result, -2605, -2634);
    
    _putDontNest(result, -2644, -2653);
    
    _putDontNest(result, -2377, -2604);
    
    _putDontNest(result, -2366, -2629);
    
    _putDontNest(result, -2550, -2624);
    
    _putDontNest(result, -2545, -2609);
    
    _putDontNest(result, -2512, -2614);
    
    _putDontNest(result, -2559, -2619);
    
    _putDontNest(result, -2530, -2564);
    
    _putDontNest(result, -2517, -2653);
    
    _putDontNest(result, -2476, -2634);
    
    _putDontNest(result, -2276, -2564);
    
    _putDontNest(result, -2504, -2522);
    
    _putDontNest(result, -2307, -2534);
    
    _putDontNest(result, -2604, -2629);
    
    _putDontNest(result, -2399, -2614);
    
    _putDontNest(result, -2416, -2619);
    
    _putDontNest(result, -2388, -2599);
    
    _putDontNest(result, -2534, -2624);
    
    _putDontNest(result, -2529, -2609);
    
    _putDontNest(result, -2522, -2604);
    
    _putDontNest(result, -2498, -2644);
    
    _putDontNest(result, -2501, -2653);
    
    _putDontNest(result, -2292, -2564);
   return result;
  }
    
  protected static IntegerMap _initDontNestGroups() {
    IntegerMap result = new IntegerMap();
    int resultStoreId = result.size();
    
    
    ++resultStoreId;
    
    result.putUnsafe(-515, resultStoreId);
    result.putUnsafe(-518, resultStoreId);
    result.putUnsafe(-523, resultStoreId);
    result.putUnsafe(-526, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2639, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2852, resultStoreId);
    result.putUnsafe(-2861, resultStoreId);
    result.putUnsafe(-2847, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2513, resultStoreId);
    result.putUnsafe(-2530, resultStoreId);
    result.putUnsafe(-2518, resultStoreId);
    result.putUnsafe(-2539, resultStoreId);
    result.putUnsafe(-2523, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-422, resultStoreId);
    result.putUnsafe(-413, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2594, resultStoreId);
    result.putUnsafe(-2579, resultStoreId);
    result.putUnsafe(-2580, resultStoreId);
    result.putUnsafe(-2584, resultStoreId);
    result.putUnsafe(-2585, resultStoreId);
    result.putUnsafe(-2589, resultStoreId);
    result.putUnsafe(-2590, resultStoreId);
    result.putUnsafe(-2575, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2489, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2614, resultStoreId);
    result.putUnsafe(-2619, resultStoreId);
    result.putUnsafe(-2624, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2545, resultStoreId);
    result.putUnsafe(-2564, resultStoreId);
    result.putUnsafe(-2550, resultStoreId);
    result.putUnsafe(-2555, resultStoreId);
    result.putUnsafe(-2540, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2834, resultStoreId);
    result.putUnsafe(-2787, resultStoreId);
    result.putUnsafe(-2771, resultStoreId);
    result.putUnsafe(-2824, resultStoreId);
    result.putUnsafe(-2749, resultStoreId);
    result.putUnsafe(-2766, resultStoreId);
    result.putUnsafe(-2752, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2498, resultStoreId);
    result.putUnsafe(-2501, resultStoreId);
    result.putUnsafe(-2504, resultStoreId);
    result.putUnsafe(-2507, resultStoreId);
    result.putUnsafe(-2512, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-427, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2609, resultStoreId);
    result.putUnsafe(-2605, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2565, resultStoreId);
    result.putUnsafe(-2569, resultStoreId);
    result.putUnsafe(-2570, resultStoreId);
    result.putUnsafe(-2574, resultStoreId);
    result.putUnsafe(-2560, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-611, resultStoreId);
    result.putUnsafe(-629, resultStoreId);
    result.putUnsafe(-710, resultStoreId);
    result.putUnsafe(-582, resultStoreId);
    result.putUnsafe(-606, resultStoreId);
    result.putUnsafe(-510, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-168, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2549, resultStoreId);
    result.putUnsafe(-2535, resultStoreId);
    result.putUnsafe(-2554, resultStoreId);
    result.putUnsafe(-2559, resultStoreId);
    result.putUnsafe(-2544, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-36, resultStoreId);
    result.putUnsafe(-57, resultStoreId);
    result.putUnsafe(-58, resultStoreId);
    result.putUnsafe(-42, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2644, resultStoreId);
    result.putUnsafe(-2635, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2307, resultStoreId);
    result.putUnsafe(-2388, resultStoreId);
    result.putUnsafe(-2276, resultStoreId);
    result.putUnsafe(-2292, resultStoreId);
    result.putUnsafe(-2279, resultStoreId);
    result.putUnsafe(-2361, resultStoreId);
    result.putUnsafe(-2377, resultStoreId);
    result.putUnsafe(-2476, resultStoreId);
    result.putUnsafe(-2366, resultStoreId);
    result.putUnsafe(-2399, resultStoreId);
    result.putUnsafe(-2416, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2851, resultStoreId);
    result.putUnsafe(-2856, resultStoreId);
    result.putUnsafe(-2841, resultStoreId);
    result.putUnsafe(-2846, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2595, resultStoreId);
    result.putUnsafe(-2599, resultStoreId);
    result.putUnsafe(-2600, resultStoreId);
    result.putUnsafe(-2604, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-417, resultStoreId);
    result.putUnsafe(-412, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-418, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2837, resultStoreId);
    result.putUnsafe(-2842, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2529, resultStoreId);
    result.putUnsafe(-2517, resultStoreId);
    result.putUnsafe(-2534, resultStoreId);
    result.putUnsafe(-2522, resultStoreId);
    result.putUnsafe(-2508, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2625, resultStoreId);
    result.putUnsafe(-2629, resultStoreId);
    result.putUnsafe(-2630, resultStoreId);
    result.putUnsafe(-2634, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2645, resultStoreId);
    result.putUnsafe(-2640, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-164, resultStoreId);
    result.putUnsafe(-173, resultStoreId);
      
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
  private static final IConstructor prod__Equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"Equals\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"==\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"fail\"),[\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(105,105)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__Mid_$PathTail__mid_$MidPathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_ = (IConstructor) _read("prod(label(\"Mid\",sort(\"PathTail\")),[label(\"mid\",lex(\"MidPathChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"PathTail\"))],{})", Factory.Production);
  private static final IConstructor prod__Tuple_$Assignable__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"Assignable\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__$LAYOUT = (IConstructor) _read("regular(\\iter-star(lex(\"LAYOUT\")))", Factory.Production);
  private static final IConstructor prod__Or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Or\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"||\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
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
  private static final IConstructor prod__Statement_$EvalCommand__statement_$Statement_ = (IConstructor) _read("prod(label(\"Statement\",sort(\"EvalCommand\")),[label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Division_$Assignment__lit___47_61_ = (IConstructor) _read("prod(label(\"Division\",sort(\"Assignment\")),[lit(\"/=\")],{})", Factory.Production);
  private static final IConstructor prod__Toplevels_$Body__toplevels_iter_star_seps__$Toplevel__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Toplevels\",sort(\"Body\")),[label(\"toplevels\",\\iter-star-seps(sort(\"Toplevel\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__$PreStringChars__char_class___range__34_34_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"PreStringChars\"),[\\char-class([range(34,34)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(60,60)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Tag_$Kind__lit_tag_ = (IConstructor) _read("prod(label(\"Tag\",sort(\"Kind\")),[lit(\"tag\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Import__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"://\"),[\\char-class([range(58,58)]),\\char-class([range(47,47)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__Alias_$Kind__lit_alias_ = (IConstructor) _read("prod(label(\"Alias\",sort(\"Kind\")),[lit(\"alias\")],{})", Factory.Production);
  private static final IConstructor prod__TryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement_ = (IConstructor) _read("prod(label(\"TryFinally\",sort(\"Statement\")),[lit(\"try\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),label(\"handlers\",\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"finally\"),layouts(\"LAYOUTLIST\"),label(\"finallyBody\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__Negative_$Pattern__lit___$layouts_LAYOUTLIST_argument_$Pattern_ = (IConstructor) _read("prod(label(\"Negative\",sort(\"Pattern\")),[lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__Default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_$Rest_ = (IConstructor) _read("prod(label(\"Default\",sort(\"PreModule\")),[label(\"header\",sort(\"Header\")),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(keywords(\"HeaderKeyword\"))}),layouts(\"LAYOUTLIST\"),label(\"rest\",lex(\"Rest\"))],{})", Factory.Production);
  private static final IConstructor prod__$JustDate__lit___36_$DatePart_ = (IConstructor) _read("prod(lex(\"JustDate\"),[lit(\"$\"),lex(\"DatePart\")],{})", Factory.Production);
  private static final IConstructor prod__Map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Map\",sort(\"Comprehension\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"from\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Quit_$ShellCommand__lit_quit_ = (IConstructor) _read("prod(label(\"Quit\",sort(\"ShellCommand\")),[lit(\"quit\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(100,100),range(105,105),range(109,109),range(115,115)])))", Factory.Production);
  private static final IConstructor prod__lit___33_62_62__char_class___range__33_33_char_class___range__62_62_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"!\\>\\>\"),[\\char-class([range(33,33)]),\\char-class([range(62,62)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__Subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Subscript\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"subscripts\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__Default_$ImportedModule__name_$QualifiedName_ = (IConstructor) _read("prod(label(\"Default\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_ = (IConstructor) _read("prod(lit(\"..\"),[\\char-class([range(46,46)]),\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__ascii_$UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(label(\"ascii\",lex(\"UnicodeEscape\")),[lit(\"\\\\\"),\\char-class([range(97,97)]),\\char-class([range(48,55)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
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
  private static final IConstructor prod__ReifiedType_$Expression__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Expression_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"ReifiedType\",sort(\"Expression\")),[lit(\"type\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"definitions\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__lit_is__char_class___range__105_105_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"is\"),[\\char-class([range(105,105)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_it__char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"it\"),[\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Import_$Command__imported_$Import_ = (IConstructor) _read("prod(label(\"Import\",sort(\"Command\")),[label(\"imported\",sort(\"Import\"))],{})", Factory.Production);
  private static final IConstructor prod__Descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_ = (IConstructor) _read("prod(label(\"Descendant\",sort(\"Pattern\")),[lit(\"/\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__Template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_ = (IConstructor) _read("prod(label(\"Template\",sort(\"StringLiteral\")),[label(\"pre\",lex(\"PreStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"loc\"),[\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__CaseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_ = (IConstructor) _read("prod(label(\"CaseInsensitiveLiteral\",sort(\"Sym\")),[label(\"cistring\",lex(\"CaseInsensitiveStringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__Mid_$StringMiddle__mid_$MidStringChars_ = (IConstructor) _read("prod(label(\"Mid\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_any_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"any\")],{})", Factory.Production);
  private static final IConstructor prod__Variable_$Type__typeVar_$TypeVar_ = (IConstructor) _read("prod(label(\"Variable\",sort(\"Type\")),[label(\"typeVar\",sort(\"TypeVar\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_all_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"all\")],{})", Factory.Production);
  private static final IConstructor prod__lit_in__char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"in\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__Default_$ModuleActuals__lit___91_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Default\",sort(\"ModuleActuals\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"types\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__All_$Kind__lit_all_ = (IConstructor) _read("prod(label(\"All\",sort(\"Kind\")),[lit(\"all\")],{})", Factory.Production);
  private static final IConstructor prod__$MidStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"MidStringChars\"),[\\char-class([range(62,62)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(60,60)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Folded_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"Expression\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{tag(Folded()),tag(category(\"Comment\"))})", Factory.Production);
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
  private static final IConstructor prod__lit___61__char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"=\"),[\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_ = (IConstructor) _read("prod(lit(\"data\"),[\\char-class([range(100,100)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(97,97)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60__char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\<\"),[\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__lit___63__char_class___range__63_63_ = (IConstructor) _read("prod(lit(\"?\"),[\\char-class([range(63,63)])],{})", Factory.Production);
  private static final IConstructor prod__Difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left = (IConstructor) _read("prod(label(\"Difference\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__43_43_range__45_45 = (IConstructor) _read("regular(opt(\\char-class([range(43,43),range(45,45)])))", Factory.Production);
  private static final IConstructor prod__lit___33_60_60__char_class___range__33_33_char_class___range__60_60_char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"!\\<\\<\"),[\\char-class([range(33,33)]),\\char-class([range(60,60)]),\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__lit___62__char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\>\"),[\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit_0__char_class___range__48_48_ = (IConstructor) _read("prod(lit(\"0\"),[\\char-class([range(48,48)])],{})", Factory.Production);
  private static final IConstructor prod__Expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Expression\",sort(\"Statement\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__InsertBefore_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"InsertBefore\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__DoWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"DoWhile\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"do\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Default_$Renamings__lit_renaming_$layouts_LAYOUTLIST_renamings_iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Renamings\")),[lit(\"renaming\"),layouts(\"LAYOUTLIST\"),label(\"renamings\",\\iter-seps(sort(\"Renaming\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__lit___41__char_class___range__41_41_ = (IConstructor) _read("prod(lit(\")\"),[\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___40__char_class___range__40_40_ = (IConstructor) _read("prod(lit(\"(\"),[\\char-class([range(40,40)])],{})", Factory.Production);
  private static final IConstructor prod__lit___43__char_class___range__43_43_ = (IConstructor) _read("prod(lit(\"+\"),[\\char-class([range(43,43)])],{})", Factory.Production);
  private static final IConstructor prod__Declaration_$EvalCommand__declaration_$Declaration_ = (IConstructor) _read("prod(label(\"Declaration\",sort(\"EvalCommand\")),[label(\"declaration\",sort(\"Declaration\"))],{})", Factory.Production);
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
  private static final IConstructor regular__seq___lit___92_char_class___range__123_123_range__125_125 = (IConstructor) _read("regular(seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])]))", Factory.Production);
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
  private static final IConstructor prod__Append_$Assignment__lit___60_60_61_ = (IConstructor) _read("prod(label(\"Append\",sort(\"Assignment\")),[lit(\"\\<\\<=\")],{})", Factory.Production);
  private static final IConstructor prod__lit_mod__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"mod\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)])],{})", Factory.Production);
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
  private static final IConstructor prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[\\char-class([range(0,33),range(35,38),range(40,59),range(61,61),range(63,91),range(93,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__Extend_$Import__lit_extend_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Extend\",sort(\"Import\")),[lit(\"extend\"),layouts(\"LAYOUTLIST\"),label(\"module\",sort(\"ImportedModule\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lit___125__char_class___range__125_125_ = (IConstructor) _read("prod(lit(\"}\"),[\\char-class([range(125,125)])],{})", Factory.Production);
  private static final IConstructor prod__lit___124__char_class___range__124_124_ = (IConstructor) _read("prod(lit(\"|\"),[\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__Set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Set\",sort(\"Expression\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__lit___42_47__char_class___range__42_42_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"*/\"),[\\char-class([range(42,42)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__Default_$Tags__tags_iter_star_seps__$Tag__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Tags\")),[label(\"tags\",\\iter-star-seps(sort(\"Tag\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__utf32_$UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(label(\"utf32\",lex(\"UnicodeEscape\")),[lit(\"\\\\\"),\\char-class([range(85,85)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__Interpolated_$ProtocolPart__pre_$PreProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_ = (IConstructor) _read("prod(label(\"Interpolated\",sort(\"ProtocolPart\")),[label(\"pre\",lex(\"PreProtocolChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"ProtocolTail\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_true_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"true\")],{})", Factory.Production);
  private static final IConstructor prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__Labeled_$DataTarget__label_$Name_$layouts_LAYOUTLIST_lit___58_ = (IConstructor) _read("prod(label(\"Labeled\",sort(\"DataTarget\")),[label(\"label\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\")],{})", Factory.Production);
  private static final IConstructor prod__Outermost_$Strategy__lit_outermost_ = (IConstructor) _read("prod(label(\"Outermost\",sort(\"Strategy\")),[lit(\"outermost\")],{})", Factory.Production);
  private static final IConstructor prod__Addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Addition\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"+\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(48,57)]),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__NonAssociative_$Assoc__lit_non_assoc_ = (IConstructor) _read("prod(label(\"NonAssociative\",sort(\"Assoc\")),[lit(\"non-assoc\")],{})", Factory.Production);
  private static final IConstructor prod__lit_o__char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"o\"),[\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Toplevel__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Toplevel\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit_unimport__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"unimport\"),[\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__Switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Switch\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"switch\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__$Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(lex(\"Comment\"),[lit(\"/*\"),\\iter-star(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,16777215)])})),lit(\"*/\")],{tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__Bool_$BasicType__lit_bool_ = (IConstructor) _read("prod(label(\"Bool\",sort(\"BasicType\")),[lit(\"bool\")],{})", Factory.Production);
  private static final IConstructor prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"*=\"),[\\char-class([range(42,42)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__Syntax_$Import__syntax_$SyntaxDefinition_ = (IConstructor) _read("prod(label(\"Syntax\",sort(\"Import\")),[label(\"syntax\",sort(\"SyntaxDefinition\"))],{})", Factory.Production);
  private static final IConstructor prod__Type_$BasicType__lit_type_ = (IConstructor) _read("prod(label(\"Type\",sort(\"BasicType\")),[lit(\"type\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_ = (IConstructor) _read("prod(lex(\"RegExpModifier\"),[\\iter-star(\\char-class([range(100,100),range(105,105),range(109,109),range(115,115)]))],{})", Factory.Production);
  private static final IConstructor prod__$NamedRegExp__lit___60_$Name_lit___62_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__Default_$StructuredType__basicType_$BasicType_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_arguments_iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Default\",sort(\"StructuredType\")),[label(\"basicType\",sort(\"BasicType\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__Join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Join\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"join\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Default_$FunctionModifier__lit_default_ = (IConstructor) _read("prod(label(\"Default\",sort(\"FunctionModifier\")),[lit(\"default\")],{})", Factory.Production);
  private static final IConstructor prod__lit_edit__char_class___range__101_101_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"edit\"),[\\char-class([range(101,101)]),\\char-class([range(100,100)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215 = (IConstructor) _read("regular(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,16777215)])}))", Factory.Production);
  private static final IConstructor prod__Empty_$Label__ = (IConstructor) _read("prod(label(\"Empty\",sort(\"Label\")),[],{})", Factory.Production);
  private static final IConstructor prod__Bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"Bracket\",sort(\"Assignable\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arg\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__Function_$Type__function_$FunctionType_ = (IConstructor) _read("prod(label(\"Function\",sort(\"Type\")),[label(\"function\",sort(\"FunctionType\"))],{})", Factory.Production);
  private static final IConstructor prod__Others_$Prod__lit___46_46_46_ = (IConstructor) _read("prod(label(\"Others\",sort(\"Prod\")),[lit(\"...\")],{})", Factory.Production);
  private static final IConstructor regular__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125 = (IConstructor) _read("regular(alt({lex(\"TagString\"),\\char-class([range(0,122),range(124,124),range(126,16777215)]),seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])])}))", Factory.Production);
  private static final IConstructor prod__IfThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"IfThenElse\",sort(\"StringTemplate\")),[lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStatsThen\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"thenString\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStatsThen\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"else\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStatsElse\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"elseString\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStatsElse\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Import_$EvalCommand__imported_$Import_ = (IConstructor) _read("prod(label(\"Import\",sort(\"EvalCommand\")),[label(\"imported\",sort(\"Import\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_finally_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"finally\")],{})", Factory.Production);
  private static final IConstructor prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"node\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__Expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Expression\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Splice_$Expression__lit___42_$layouts_LAYOUTLIST_argument_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"Splice\",sort(\"Expression\")),[lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right = (IConstructor) _read("prod(label(\"Precede\",sort(\"Sym\")),[label(\"match\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\<\\<\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__ReifyType_$Expression__lit___35_$layouts_LAYOUTLIST_type_$Type_ = (IConstructor) _read("prod(label(\"ReifyType\",sort(\"Expression\")),[lit(\"#\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\"))],{})", Factory.Production);
  private static final IConstructor prod__Reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Reducer\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"init\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"result\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_filter_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"filter\")],{})", Factory.Production);
  private static final IConstructor prod__$Comment__lit___47_47_conditional__iter_star__char_class___range__0_9_range__11_16777215__not_follow__char_class___range__9_9_range__13_13_range__32_32_end_of_line__tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(lex(\"Comment\"),[lit(\"//\"),conditional(\\iter-star(\\char-class([range(0,9),range(11,16777215)])),{\\not-follow(\\char-class([range(9,9),range(13,13),range(32,32)])),\\end-of-line()})],{tag(category(\"Comment\"))})", Factory.Production);
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
  private static final IConstructor prod__Public_$Visibility__lit_public_ = (IConstructor) _read("prod(label(\"Public\",sort(\"Visibility\")),[lit(\"public\")],{})", Factory.Production);
  private static final IConstructor prod__Intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Intersection\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit_quit__char_class___range__113_113_char_class___range__117_117_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"quit\"),[\\char-class([range(113,113)]),\\char-class([range(117,117)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"tuple\"),[\\char-class([range(116,116)]),\\char-class([range(117,117)]),\\char-class([range(112,112)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__$OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"OctalIntegerLiteral\"),[\\char-class([range(48,48)]),conditional(iter(\\char-class([range(48,55)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Variant\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__VoidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"VoidClosure\",sort(\"Expression\")),[label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_try_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"try\")],{})", Factory.Production);
  private static final IConstructor prod__Real_$BasicType__lit_real_ = (IConstructor) _read("prod(label(\"Real\",sort(\"BasicType\")),[lit(\"real\")],{})", Factory.Production);
  private static final IConstructor prod__lit_has__char_class___range__104_104_char_class___range__97_97_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"has\"),[\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__Int_$BasicType__lit_int_ = (IConstructor) _read("prod(label(\"Int\",sort(\"BasicType\")),[lit(\"int\")],{})", Factory.Production);
  private static final IConstructor prod__$TagString__lit___123_contents_iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_ = (IConstructor) _read("prod(lex(\"TagString\"),[lit(\"{\"),label(\"contents\",\\iter-star(alt({lex(\"TagString\"),\\char-class([range(0,122),range(124,124),range(126,16777215)]),seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])])}))),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_anno_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"anno\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_insert_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"insert\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__char_class___range__60_60_expression_$Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101 = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(60,60)]),label(\"expression\",sort(\"Expression\")),\\char-class([range(62,62)])],{tag(category(\"MetaVariable\"))})", Factory.Production);
  private static final IConstructor prod__Alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Alternative\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"alternatives\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Left_$Assoc__lit_left_ = (IConstructor) _read("prod(label(\"Left\",sort(\"Assoc\")),[lit(\"left\")],{})", Factory.Production);
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
  private static final IConstructor prod__IfThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__right = (IConstructor) _read("prod(label(\"IfThenElse\",sort(\"Expression\")),[label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"thenExp\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"elseExp\",sort(\"Expression\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__Node_$BasicType__lit_node_ = (IConstructor) _read("prod(label(\"Node\",sort(\"BasicType\")),[lit(\"node\")],{})", Factory.Production);
  private static final IConstructor prod__$BooleanLiteral__lit_false_ = (IConstructor) _read("prod(lex(\"BooleanLiteral\"),[lit(\"false\")],{})", Factory.Production);
  private static final IConstructor prod__GlobalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"GlobalDirective\",sort(\"Statement\")),[lit(\"global\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"names\",\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Function_$Declaration__functionDeclaration_$FunctionDeclaration_ = (IConstructor) _read("prod(label(\"Function\",sort(\"Declaration\")),[label(\"functionDeclaration\",sort(\"FunctionDeclaration\"))],{})", Factory.Production);
  private static final IConstructor prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_ = (IConstructor) _read("prod(lex(\"URLChars\"),[\\iter-star(\\char-class([range(0,8),range(11,12),range(14,31),range(33,59),range(61,123),range(125,16777215)]))],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"FunctionModifier\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Constructor\",sort(\"Assignable\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__CallOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"CallOrTree\",sort(\"Pattern\")),[label(\"expression\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"&=\"),[\\char-class([range(38,38)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__Tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"Pattern\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__TransitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_ = (IConstructor) _read("prod(label(\"TransitiveClosure\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"+\"),{\\not-follow(lit(\"=\"))})],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_break_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"break\")],{})", Factory.Production);
  private static final IConstructor prod__Product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit___42_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Product\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"*\"),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(lit(\"*\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Labeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Labeled\",sort(\"Prod\")),[label(\"modifiers\",\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"args\",\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Test_$ShellCommand__lit_test_ = (IConstructor) _read("prod(label(\"Test\",sort(\"ShellCommand\")),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_real_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"real\")],{})", Factory.Production);
  private static final IConstructor prod__Filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Filter\",sort(\"Statement\")),[lit(\"filter\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"!=\"),[\\char-class([range(33,33)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__$StringConstant__lit___34_iter_star__$StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"StringConstant\"),[lit(\"\\\"\"),\\iter-star(lex(\"StringCharacter\")),lit(\"\\\"\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__IfDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_ = (IConstructor) _read("prod(label(\"IfDefinedOrDefault\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"defaultExpression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__$PreProtocolChars__lit___124_$URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"PreProtocolChars\"),[lit(\"|\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__Default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Catch\")),[lit(\"catch\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(92,92)]),\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__Modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_mod_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Modulo\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"mod\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Iter_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___43_ = (IConstructor) _read("prod(label(\"Iter\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"+\")],{})", Factory.Production);
  private static final IConstructor prod__EndOfLine_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___36_ = (IConstructor) _read("prod(label(\"EndOfLine\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"$\")],{})", Factory.Production);
  private static final IConstructor prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"anno\"),[\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(110,110)]),\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__NAryConstructor_$Variant__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"NAryConstructor\",sort(\"Variant\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
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
  private static final IConstructor prod__AppendAfter_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60_60__not_follow__lit___61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"AppendAfter\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\<\\<\"),{\\not-follow(lit(\"=\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_join_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"join\")],{})", Factory.Production);
  private static final IConstructor prod__$Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[lit(\"\\\\\"),\\char-class([range(32,32),range(34,34),range(39,39),range(45,45),range(60,60),range(62,62),range(91,93),range(98,98),range(102,102),range(110,110),range(114,114),range(116,116)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__lit_view__char_class___range__118_118_char_class___range__105_105_char_class___range__101_101_char_class___range__119_119_ = (IConstructor) _read("prod(lit(\"view\"),[\\char-class([range(118,118)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(119,119)])],{})", Factory.Production);
  private static final IConstructor prod__lit_innermost__char_class___range__105_105_char_class___range__110_110_char_class___range__110_110_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"innermost\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(110,110)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_one__char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"one\"),[\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Renaming\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Tuple_$BasicType__lit_tuple_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"BasicType\")),[lit(\"tuple\")],{})", Factory.Production);
  private static final IConstructor prod__Interpolated_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_ = (IConstructor) _read("prod(label(\"Interpolated\",sort(\"StringLiteral\")),[label(\"pre\",lex(\"PreStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_import_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"import\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$ProtocolChars__char_class___range__124_124_$URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_ = (IConstructor) _read("prod(lex(\"ProtocolChars\"),[\\char-class([range(124,124)]),lex(\"URLChars\"),conditional(lit(\"://\"),{\\not-follow(\\char-class([range(9,10),range(13,13),range(32,32)]))})],{})", Factory.Production);
  private static final IConstructor prod__$CaseInsensitiveStringConstant__lit___39_iter_star__$StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"CaseInsensitiveStringConstant\"),[lit(\"\\'\\\\\"),\\iter-star(lex(\"StringCharacter\")),lit(\"\\'\\\\\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"while\"),[\\char-class([range(119,119)]),\\char-class([range(104,104)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"notin\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__Parameters_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_params_$ModuleParameters_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Parameters\",sort(\"Header\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),lit(\"module\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"params\",sort(\"ModuleParameters\")),layouts(\"LAYOUTLIST\"),label(\"imports\",\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))])))", Factory.Production);
  private static final IConstructor prod__Literal_$Sym__string_$StringConstant_ = (IConstructor) _read("prod(label(\"Literal\",sort(\"Sym\")),[label(\"string\",lex(\"StringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__$Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalReservedKeywords_not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"Nonterminal\"),[conditional(\\char-class([range(65,90)]),{\\not-precede(\\char-class([range(65,90)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),delete(sort(\"RascalReservedKeywords\"))})],{})", Factory.Production);
  private static final IConstructor prod__View_$Kind__lit_view_ = (IConstructor) _read("prod(label(\"View\",sort(\"Kind\")),[lit(\"view\")],{})", Factory.Production);
  private static final IConstructor prod__AssertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"AssertWithMessage\",sort(\"Statement\")),[lit(\"assert\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"message\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__NotFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left = (IConstructor) _read("prod(label(\"NotFollow\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_void_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__Binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_ = (IConstructor) _read("prod(label(\"Binding\",sort(\"Catch\")),[lit(\"catch\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__Real_$Literal__realLiteral_$RealLiteral_ = (IConstructor) _read("prod(label(\"Real\",sort(\"Literal\")),[label(\"realLiteral\",lex(\"RealLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__VariableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"VariableDeclaration\",sort(\"Statement\")),[label(\"declaration\",sort(\"LocalVariableDeclaration\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_ = (IConstructor) _read("prod(label(\"Solve\",sort(\"Statement\")),[lit(\"solve\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"bound\",sort(\"Bound\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__$JustTime__lit___36_84_$TimePartNoTZ_opt__$TimeZonePart_ = (IConstructor) _read("prod(lex(\"JustTime\"),[lit(\"$T\"),lex(\"TimePartNoTZ\"),opt(lex(\"TimeZonePart\"))],{})", Factory.Production);
  private static final IConstructor prod__Boolean_$Literal__booleanLiteral_$BooleanLiteral_ = (IConstructor) _read("prod(label(\"Boolean\",sort(\"Literal\")),[label(\"booleanLiteral\",lex(\"BooleanLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__VarArgs_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___46_46_46_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"VarArgs\",sort(\"Parameters\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"formals\",sort(\"Formals\")),layouts(\"LAYOUTLIST\"),lit(\"...\"),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Statement_$Command__statement_$Statement_ = (IConstructor) _read("prod(label(\"Statement\",sort(\"Command\")),[label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__ReifiedType_$Pattern__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Pattern_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Pattern_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"ReifiedType\",sort(\"Pattern\")),[lit(\"type\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"definitions\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__BottomUpBreak_$Strategy__lit_bottom_up_break_ = (IConstructor) _read("prod(label(\"BottomUpBreak\",sort(\"Strategy\")),[lit(\"bottom-up-break\")],{})", Factory.Production);
  private static final IConstructor prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"default\"),[\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__TransitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_ = (IConstructor) _read("prod(label(\"TransitiveReflexiveClosure\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"*\"),{\\not-follow(lit(\"=\"))})],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__List_$Commands__commands_iter_seps__$EvalCommand__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"List\",sort(\"Commands\")),[label(\"commands\",\\iter-seps(sort(\"EvalCommand\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"bool\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(111,111)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102 = (IConstructor) _read("regular(opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)])))", Factory.Production);
  private static final IConstructor prod__Remainder_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Remainder\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"%\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left = (IConstructor) _read("prod(label(\"Unequal\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\\\\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Field\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$TimeZonePart__lit_Z_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[lit(\"Z\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_default_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"default\")],{})", Factory.Production);
  private static final IConstructor prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(49,57)]),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(114,114)]),\\char-class([range(48,57)]),conditional(\\iter-star(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_layout_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"layout\")],{})", Factory.Production);
  private static final IConstructor prod__Default_$FunctionBody__lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Default\",sort(\"FunctionBody\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__GetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"GetAnnotation\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_variable__char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"variable\"),[\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(97,97)]),\\char-class([range(98,98)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_61_61_62__char_class___range__60_60_char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\<==\\>\"),[\\char-class([range(60,60)]),\\char-class([range(61,61)]),\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[\\char-class([range(92,92)]),\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__First_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left = (IConstructor) _read("prod(label(\"First\",sort(\"Prod\")),[label(\"lhs\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\>\"),{\\not-follow(lit(\"\\>\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Prod\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Default_$Module__header_$Header_$layouts_LAYOUTLIST_body_$Body_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Module\")),[label(\"header\",sort(\"Header\")),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Body\"))],{})", Factory.Production);
  private static final IConstructor prod__Visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_ = (IConstructor) _read("prod(label(\"Visit\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),label(\"visit\",sort(\"Visit\"))],{})", Factory.Production);
  private static final IConstructor prod__NotIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"NotIn\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"notin\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Relation_$BasicType__lit_rel_ = (IConstructor) _read("prod(label(\"Relation\",sort(\"BasicType\")),[lit(\"rel\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_fail_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"fail\")],{})", Factory.Production);
  private static final IConstructor prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"str\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__String_$Literal__stringLiteral_$StringLiteral_ = (IConstructor) _read("prod(label(\"String\",sort(\"Literal\")),[label(\"stringLiteral\",sort(\"StringLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__$Rest__conditional__iter_star__char_class___range__0_16777215__not_follow__char_class___range__0_16777215_ = (IConstructor) _read("prod(lex(\"Rest\"),[conditional(\\iter-star(\\char-class([range(0,16777215)])),{\\not-follow(\\char-class([range(0,16777215)]))})],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_solve_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"solve\")],{})", Factory.Production);
  private static final IConstructor prod__Language_$SyntaxDefinition__start_$Start_$layouts_LAYOUTLIST_lit_syntax_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Language\",sort(\"SyntaxDefinition\")),[label(\"start\",sort(\"Start\")),layouts(\"LAYOUTLIST\"),lit(\"syntax\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Help_$ShellCommand__lit_help_ = (IConstructor) _read("prod(label(\"Help\",sort(\"ShellCommand\")),[lit(\"help\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])))", Factory.Production);
  private static final IConstructor prod__Anno_$Kind__lit_anno_ = (IConstructor) _read("prod(label(\"Anno\",sort(\"Kind\")),[lit(\"anno\")],{})", Factory.Production);
  private static final IConstructor prod__$NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"NonterminalLabel\"),[\\char-class([range(97,122)]),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"=\\>\"),[\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"==\"),[\\char-class([range(61,61)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[\\char-class([range(0,46),range(48,59),range(61,61),range(63,91),range(93,16777215)])],{})", Factory.Production);
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
  private static final IConstructor prod__$RascalKeywords__lit_continue_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"continue\")],{})", Factory.Production);
  private static final IConstructor prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_ = (IConstructor) _read("prod(lit(\"||\"),[\\char-class([range(124,124)]),\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__Bag_$BasicType__lit_bag_ = (IConstructor) _read("prod(label(\"Bag\",sort(\"BasicType\")),[lit(\"bag\")],{})", Factory.Production);
  private static final IConstructor prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\<=\"),[\\char-class([range(60,60)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"finally\"),[\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(108,108)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__$PostPathChars__lit___62_$URLChars_lit___124_ = (IConstructor) _read("prod(lex(\"PostPathChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"|\")],{})", Factory.Production);
  private static final IConstructor prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\<\\<\"),[\\char-class([range(60,60)]),\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__Associative_$Assoc__lit_assoc_ = (IConstructor) _read("prod(label(\"Associative\",sort(\"Assoc\")),[lit(\"assoc\")],{})", Factory.Production);
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
  private static final IConstructor regular__iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215 = (IConstructor) _read("regular(\\iter-star(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,16777215)])})))", Factory.Production);
  private static final IConstructor prod__DoWhile_$StringTemplate__lit_do_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"DoWhile\",sort(\"StringTemplate\")),[lit(\"do\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Free_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"Free\",sort(\"TypeVar\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__VariableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_ = (IConstructor) _read("prod(label(\"VariableBecomes\",sort(\"Pattern\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__FunctionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration_ = (IConstructor) _read("prod(label(\"FunctionDeclaration\",sort(\"Statement\")),[label(\"functionDeclaration\",sort(\"FunctionDeclaration\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__$BasicType_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[sort(\"BasicType\")],{})", Factory.Production);
  private static final IConstructor prod__User_$Type__conditional__user_$UserType__delete__$HeaderKeyword_ = (IConstructor) _read("prod(label(\"User\",sort(\"Type\")),[conditional(label(\"user\",sort(\"UserType\")),{delete(keywords(\"HeaderKeyword\"))})],{})", Factory.Production);
  private static final IConstructor regular__iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125 = (IConstructor) _read("regular(\\iter-star(alt({lex(\"TagString\"),\\char-class([range(0,122),range(124,124),range(126,16777215)]),seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])])})))", Factory.Production);
  private static final IConstructor prod__StepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"StepRange\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"second\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"..\"),layouts(\"LAYOUTLIST\"),label(\"last\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__utf16_$UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(label(\"utf16\",lex(\"UnicodeEscape\")),[lit(\"\\\\\"),\\char-class([range(117,117)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__$PostProtocolChars__lit___62_$URLChars_lit___58_47_47_ = (IConstructor) _read("prod(lex(\"PostProtocolChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"://\")],{})", Factory.Production);
  private static final IConstructor prod__Location_$Literal__locationLiteral_$LocationLiteral_ = (IConstructor) _read("prod(label(\"Location\",sort(\"Literal\")),[label(\"locationLiteral\",sort(\"LocationLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__It_$Expression__conditional__lit_it__not_precede__char_class___range__65_90_range__95_95_range__97_122_not_follow__char_class___range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(label(\"It\",sort(\"Expression\")),[conditional(lit(\"it\"),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)])),\\not-follow(\\char-class([range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__Unlabeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Unlabeled\",sort(\"Prod\")),[label(\"modifiers\",\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"args\",\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Innermost_$Strategy__lit_innermost_ = (IConstructor) _read("prod(label(\"Innermost\",sort(\"Strategy\")),[lit(\"innermost\")],{})", Factory.Production);
  private static final IConstructor prod__Column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_ = (IConstructor) _read("prod(label(\"Column\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"column\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__Default_$Label__name_$Name_$layouts_LAYOUTLIST_lit___58_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Label\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\":\"),\\iter-star(lex(\"NamedRegExp\")),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_loc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"loc\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star__$NamedRegExp = (IConstructor) _read("regular(\\iter-star(lex(\"NamedRegExp\")))", Factory.Production);
  private static final IConstructor prod__Tag_$ProdModifier__tag_$Tag_ = (IConstructor) _read("prod(label(\"Tag\",sort(\"ProdModifier\")),[label(\"tag\",sort(\"Tag\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"keyword\"),[\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(121,121)]),\\char-class([range(119,119)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__SplicePlus_$Pattern__lit___43_$layouts_LAYOUTLIST_argument_$Pattern_ = (IConstructor) _read("prod(label(\"SplicePlus\",sort(\"Pattern\")),[lit(\"+\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(0,46),range(48,59),range(61,61),range(63,91),range(93,16777215)])],{})", Factory.Production);
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
  private static final IConstructor prod__Default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Folded_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"Default\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"contents\",lex(\"TagString\"))],{tag(Folded()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_throw_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"throw\")],{})", Factory.Production);
  private static final IConstructor prod__lit_bottom_up__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"bottom-up\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(45,45)]),\\char-class([range(117,117)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"break\"),[\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__$layouts_LAYOUTLIST__conditional__iter_star__$LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_ = (IConstructor) _read("prod(layouts(\"LAYOUTLIST\"),[conditional(\\iter-star(lex(\"LAYOUT\")),{\\not-follow(\\char-class([range(9,10),range(13,13),range(32,32)])),\\not-follow(lit(\"//\")),\\not-follow(lit(\"/*\"))})],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_mod_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"mod\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_rat_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"rat\")],{})", Factory.Production);
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
  private static final IConstructor prod__Selector_$Type__selector_$DataTypeSelector_ = (IConstructor) _read("prod(label(\"Selector\",sort(\"Type\")),[label(\"selector\",sort(\"DataTypeSelector\"))],{})", Factory.Production);
  private static final IConstructor prod__BottomUp_$Strategy__lit_bottom_up_ = (IConstructor) _read("prod(label(\"BottomUp\",sort(\"Strategy\")),[lit(\"bottom-up\")],{})", Factory.Production);
  private static final IConstructor prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"assoc\"),[\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__All_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left = (IConstructor) _read("prod(label(\"All\",sort(\"Prod\")),[label(\"lhs\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Prod\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Map\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"mappings\",\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Expression\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_ = (IConstructor) _read("prod(label(\"Template\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringMiddle\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_bool_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bool\")],{})", Factory.Production);
  private static final IConstructor prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"map\"),[\\char-class([range(109,109)]),\\char-class([range(97,97)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__Named_$TypeArg__type_$Type_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"Named\",sort(\"TypeArg\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"real\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)])))", Factory.Production);
  private static final IConstructor prod__Default_$Assignment__lit___61_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Assignment\")),[lit(\"=\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_datetime_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"datetime\")],{})", Factory.Production);
  private static final IConstructor prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[\\char-class([range(0,31),range(33,33),range(35,38),range(40,44),range(46,59),range(61,61),range(63,90),range(94,16777215)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Character_$Range__character_$Char_ = (IConstructor) _read("prod(label(\"Character\",sort(\"Range\")),[label(\"character\",lex(\"Char\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"void\"),[\\char-class([range(118,118)]),\\char-class([range(111,111)]),\\char-class([range(105,105)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__Replacing_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_replacement_$Replacement_ = (IConstructor) _read("prod(label(\"Replacing\",sort(\"PatternWithAction\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"=\\>\"),layouts(\"LAYOUTLIST\"),label(\"replacement\",sort(\"Replacement\"))],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[conditional(lit(\".\"),{\\not-precede(\\char-class([range(46,46)]))}),iter(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_set_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"set\")],{})", Factory.Production);
  private static final IConstructor prod__Union_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Class__assoc__left = (IConstructor) _read("prod(label(\"Union\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"||\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__AssociativityGroup_$Prod__associativity_$Assoc_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_group_$Prod_$layouts_LAYOUTLIST_lit___41__tag__Foldable = (IConstructor) _read("prod(label(\"AssociativityGroup\",sort(\"Prod\")),[label(\"associativity\",sort(\"Assoc\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"group\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Splice_$Pattern__lit___42_$layouts_LAYOUTLIST_argument_$Pattern_ = (IConstructor) _read("prod(label(\"Splice\",sort(\"Pattern\")),[lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_return_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"return\")],{})", Factory.Production);
  private static final IConstructor prod__Selector_$DataTypeSelector__sort_$QualifiedName_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_production_$Name_ = (IConstructor) _read("prod(label(\"Selector\",sort(\"DataTypeSelector\")),[label(\"sort\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"production\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__Rational_$BasicType__lit_rat_ = (IConstructor) _read("prod(label(\"Rational\",sort(\"BasicType\")),[lit(\"rat\")],{})", Factory.Production);
  private static final IConstructor prod__empty__ = (IConstructor) _read("prod(empty(),[],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_str_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"str\")],{})", Factory.Production);
  private static final IConstructor prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_ = (IConstructor) _read("prod(lit(\"switch\"),[\\char-class([range(115,115)]),\\char-class([range(119,119)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(99,99)]),\\char-class([range(104,104)])],{})", Factory.Production);
  private static final IConstructor prod__NonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"NonEmptyBlock\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"Bracket\",sort(\"Class\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"charclass\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"solve\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(108,108)]),\\char-class([range(118,118)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__IterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"IterStarSep\",sort(\"Sym\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sep\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"layout\"),[\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(121,121)]),\\char-class([range(111,111)]),\\char-class([range(117,117)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))", Factory.Production);
  private static final IConstructor prod__Function_$Kind__lit_function_ = (IConstructor) _read("prod(label(\"Function\",sort(\"Kind\")),[lit(\"function\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_extend_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"extend\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Catch__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__Arbitrary_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement_ = (IConstructor) _read("prod(label(\"Arbitrary\",sort(\"PatternWithAction\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__List_$Comprehension__lit___91_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"List\",sort(\"Comprehension\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"results\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_tag_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"tag\")],{})", Factory.Production);
  private static final IConstructor prod__Fail_$Statement__lit_fail_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Fail\",sort(\"Statement\")),[lit(\"fail\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__$StringCharacter = (IConstructor) _read("regular(\\iter-star(lex(\"StringCharacter\")))", Factory.Production);
  private static final IConstructor prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[\\char-class([range(10,10)]),\\iter-star(\\char-class([range(9,9),range(32,32)])),\\char-class([range(39,39)])],{})", Factory.Production);
  private static final IConstructor prod__And_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"And\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"&&\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_keyword_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"keyword\")],{})", Factory.Production);
  private static final IConstructor prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_opt__$TimeZonePart_ = (IConstructor) _read("prod(lex(\"DateAndTime\"),[lit(\"$\"),lex(\"DatePart\"),lit(\"T\"),lex(\"TimePartNoTZ\"),opt(lex(\"TimeZonePart\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_list_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"list\")],{})", Factory.Production);
  private static final IConstructor prod__ListModules_$ShellCommand__lit_modules_ = (IConstructor) _read("prod(label(\"ListModules\",sort(\"ShellCommand\")),[lit(\"modules\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$EvalCommand__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"EvalCommand\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
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
  private static final IConstructor prod__Empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Folded_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"Empty\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{tag(Folded()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__Annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_ = (IConstructor) _read("prod(label(\"Annotation\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"annotation\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"lexical\"),[\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(105,105)]),\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_45__char_class___range__60_60_char_class___range__45_45_ = (IConstructor) _read("prod(lit(\"\\<-\"),[\\char-class([range(60,60)]),\\char-class([range(45,45)])],{})", Factory.Production);
  private static final IConstructor prod__GivenStrategy_$Visit__strategy_$Strategy_$layouts_LAYOUTLIST_lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"GivenStrategy\",sort(\"Visit\")),[label(\"strategy\",sort(\"Strategy\")),layouts(\"LAYOUTLIST\"),lit(\"visit\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"subject\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__HexIntegerLiteral_$IntegerLiteral__hex_$HexIntegerLiteral_ = (IConstructor) _read("prod(label(\"HexIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"hex\",lex(\"HexIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"filter\"),[\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__TypedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_ = (IConstructor) _read("prod(label(\"TypedVariableBecomes\",sort(\"Pattern\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__Assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"Assert\",sort(\"Statement\")),[lit(\"assert\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimePartNoTZ\"),[\\char-class([range(48,50)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))],{})", Factory.Production);
  private static final IConstructor prod__IterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_ = (IConstructor) _read("prod(label(\"IterSep\",sort(\"Sym\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sep\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"+\")],{})", Factory.Production);
  private static final IConstructor prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"start\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58_58__char_class___range__58_58_char_class___range__58_58_ = (IConstructor) _read("prod(lit(\"::\"),[\\char-class([range(58,58)]),\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__$Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"Backslash\"),[conditional(\\char-class([range(92,92)]),{\\not-follow(\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)]))})],{})", Factory.Production);
  private static final IConstructor prod__lit___58_61__char_class___range__58_58_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\":=\"),[\\char-class([range(58,58)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__Bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"Bracket\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__FieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_ = (IConstructor) _read("prod(label(\"FieldAccess\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"field\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_throws_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"throws\")],{})", Factory.Production);
  private static final IConstructor prod__Conditional_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"Conditional\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"when\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__Return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc = (IConstructor) _read("prod(label(\"Return\",sort(\"Statement\")),[lit(\"return\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__Bracket_$ProdModifier__lit_bracket_ = (IConstructor) _read("prod(label(\"Bracket\",sort(\"ProdModifier\")),[lit(\"bracket\")],{})", Factory.Production);
  private static final IConstructor prod__NonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"NonEmptyBlock\",sort(\"Expression\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__GreaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"GreaterThanOrEq\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_switch_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"switch\")],{})", Factory.Production);
  private static final IConstructor prod__Conditional_$Replacement__replacementExpression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Conditional\",sort(\"Replacement\")),[label(\"replacementExpression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"when\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"DatePart\"),[\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),lit(\"-\"),\\char-class([range(48,49)]),\\char-class([range(48,57)]),lit(\"-\"),\\char-class([range(48,51)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__Name_$UserType__name_$QualifiedName_ = (IConstructor) _read("prod(label(\"Name\",sort(\"UserType\")),[label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__Tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"Tuple\",sort(\"Expression\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__$PostStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"PostStringChars\"),[\\char-class([range(62,62)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(34,34)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Expression_$Command__expression_$Expression_ = (IConstructor) _read("prod(label(\"Expression\",sort(\"Command\")),[label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"for\"),[\\char-class([range(102,102)]),\\char-class([range(111,111)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__Test_$FunctionModifier__lit_test_ = (IConstructor) _read("prod(label(\"Test\",sort(\"FunctionModifier\")),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__UnInitialized_$Variable__name_$Name_ = (IConstructor) _read("prod(label(\"UnInitialized\",sort(\"Variable\")),[label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__Insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc = (IConstructor) _read("prod(label(\"Insert\",sort(\"Statement\")),[lit(\"insert\"),layouts(\"LAYOUTLIST\"),label(\"dataTarget\",sort(\"DataTarget\")),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lit_left__char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"left\"),[\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__IterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"IterStar\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimePartNoTZ\"),[\\char-class([range(48,50)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))],{})", Factory.Production);
  private static final IConstructor regular__empty = (IConstructor) _read("regular(empty())", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_9_range__11_16777215 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,9),range(11,16777215)])))", Factory.Production);
  private static final IConstructor prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"bracket\"),[\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(99,99)]),\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,8),range(11,12),range(14,31),range(33,59),range(61,123),range(125,16777215)])))", Factory.Production);
  private static final IConstructor prod__CharacterClass_$Sym__charClass_$Class_ = (IConstructor) _read("prod(label(\"CharacterClass\",sort(\"Sym\")),[label(\"charClass\",sort(\"Class\"))],{})", Factory.Production);
  private static final IConstructor prod__List_$BasicType__lit_list_ = (IConstructor) _read("prod(label(\"List\",sort(\"BasicType\")),[lit(\"list\")],{})", Factory.Production);
  private static final IConstructor prod__Map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"Map\",sort(\"Pattern\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"mappings\",\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"Division\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"/\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Post_$StringTail__post_$PostStringChars_ = (IConstructor) _read("prod(label(\"Post\",sort(\"StringTail\")),[label(\"post\",lex(\"PostStringChars\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_for_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"for\")],{})", Factory.Production);
  private static final IConstructor prod__ActualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_ = (IConstructor) _read("prod(label(\"ActualsRenaming\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"actuals\",sort(\"ModuleActuals\")),layouts(\"LAYOUTLIST\"),label(\"renamings\",sort(\"Renamings\"))],{})", Factory.Production);
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
  private static final IConstructor prod__start__$EvalCommand__$layouts_LAYOUTLIST_top_$EvalCommand_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"EvalCommand\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"EvalCommand\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
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
  private static final IConstructor prod__Set_$Comprehension__lit___123_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"Set\",sort(\"Comprehension\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"results\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Default_$Formals__formals_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"Default\",sort(\"Formals\")),[label(\"formals\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__Bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"Bracket\",sort(\"Type\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__All_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"All\",sort(\"Expression\")),[lit(\"all\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Parametrized_$Sym__conditional__nonterminal_$Nonterminal__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"Parametrized\",sort(\"Sym\")),[conditional(label(\"nonterminal\",lex(\"Nonterminal\")),{follow(lit(\"[\"))}),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
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
  private static final IConstructor prod__lit___60_60_61__char_class___range__60_60_char_class___range__60_60_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\<\\<=\"),[\\char-class([range(60,60)]),\\char-class([range(60,60)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__$LAYOUT__$Comment_ = (IConstructor) _read("prod(lex(\"LAYOUT\"),[lex(\"Comment\")],{})", Factory.Production);
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
  private static final IConstructor regular__iter_star__char_class___range__0_16777215 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,16777215)])))", Factory.Production);
  private static final IConstructor prod__While_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"While\",sort(\"StringTemplate\")),[lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__NonInterpolated_$ProtocolPart__protocolChars_$ProtocolChars_ = (IConstructor) _read("prod(label(\"NonInterpolated\",sort(\"ProtocolPart\")),[label(\"protocolChars\",lex(\"ProtocolChars\"))],{})", Factory.Production);
  private static final IConstructor prod__Associativity_$ProdModifier__associativity_$Assoc_ = (IConstructor) _read("prod(label(\"Associativity\",sort(\"ProdModifier\")),[label(\"associativity\",sort(\"Assoc\"))],{})", Factory.Production);
  private static final IConstructor prod__DataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"DataAbstract\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"data\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__LessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"LessThanOrEq\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\<=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__String_$BasicType__lit_str_ = (IConstructor) _read("prod(label(\"String\",sort(\"BasicType\")),[lit(\"str\")],{})", Factory.Production);
  private static final IConstructor prod__Negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_ = (IConstructor) _read("prod(label(\"Negation\",sort(\"Expression\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__WithThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit_throws_$layouts_LAYOUTLIST_exceptions_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"WithThrows\",sort(\"Signature\")),[label(\"modifiers\",sort(\"FunctionModifiers\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"throws\"),layouts(\"LAYOUTLIST\"),label(\"exceptions\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__SetOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_ = (IConstructor) _read("prod(label(\"SetOption\",sort(\"ShellCommand\")),[lit(\"set\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__Index_$Field__fieldIndex_$IntegerLiteral_ = (IConstructor) _read("prod(label(\"Index\",sort(\"Field\")),[label(\"fieldIndex\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"public\"),[\\char-class([range(112,112)]),\\char-class([range(117,117)]),\\char-class([range(98,98)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__For_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_ = (IConstructor) _read("prod(label(\"For\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"for\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[conditional(lit(\".\"),{\\not-precede(\\char-class([range(46,46)]))}),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__lit_rat__char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"rat\"),[\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__$Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"Name\"),[\\char-class([range(92,92)]),\\char-class([range(65,90),range(95,95),range(97,122)]),conditional(\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__lit_renaming__char_class___range__114_114_char_class___range__101_101_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"renaming\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_alias_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"alias\")],{})", Factory.Production);
  private static final IConstructor prod__QualifiedName_$Expression__qualifiedName_$QualifiedName_ = (IConstructor) _read("prod(label(\"QualifiedName\",sort(\"Expression\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
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
    
    protected static final void _init_prod__Default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Folded_tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-8, 4, "$TagString", null, null);
      tmp[3] = new NonTerminalStackNode(-7, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-6, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-5, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-4, 0, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Folded_tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__Expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Folded_tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-15, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-14, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-13, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode(-12, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-11, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-10, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-9, 0, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      builder.addAlternative(RascalRascal.prod__Expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Folded_tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__Empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Folded_tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-18, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-17, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-16, 0, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      builder.addAlternative(RascalRascal.prod__Empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Folded_tag__category___67_111_109_109_101_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Folded_tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__Expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Folded_tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__Empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Folded_tag__category___67_111_109_109_101_110_116(builder);
      
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
      tmp[2] = new LiteralStackNode(-27, 2, prod__lit_T__char_class___range__84_84_, new int[] {84}, null, null);
      tmp[1] = new NonTerminalStackNode(-26, 1, "$DatePart", null, null);
      tmp[0] = new LiteralStackNode(-25, 0, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
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
    
    protected static final void _init_prod__Negative_$Pattern__lit___$layouts_LAYOUTLIST_argument_$Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-36, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode(-35, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-34, 0, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      builder.addAlternative(RascalRascal.prod__Negative_$Pattern__lit___$layouts_LAYOUTLIST_argument_$Pattern_, tmp);
	}
    protected static final void _init_prod__MultiVariable_$Pattern__qualifiedName_$QualifiedName_$layouts_LAYOUTLIST_lit___42_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-39, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode(-38, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-37, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__MultiVariable_$Pattern__qualifiedName_$QualifiedName_$layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__Descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-111, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode(-110, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-109, 0, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      builder.addAlternative(RascalRascal.prod__Descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    protected static final void _init_prod__SplicePlus_$Pattern__lit___43_$layouts_LAYOUTLIST_argument_$Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-42, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode(-41, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-40, 0, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      builder.addAlternative(RascalRascal.prod__SplicePlus_$Pattern__lit___43_$layouts_LAYOUTLIST_argument_$Pattern_, tmp);
	}
    protected static final void _init_prod__TypedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-118, 6, "$Pattern", null, null);
      tmp[5] = new NonTerminalStackNode(-117, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-116, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(-115, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-114, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-113, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-112, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__TypedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    protected static final void _init_prod__AsType_$Pattern__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-125, 6, "$Pattern", null, null);
      tmp[5] = new NonTerminalStackNode(-124, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-123, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-122, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-121, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-120, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-119, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__AsType_$Pattern__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Pattern_, tmp);
	}
    protected static final void _init_prod__List_$Pattern__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-54, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-53, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-48, 2, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-49, 0, "$Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-50, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-51, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-52, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-47, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-46, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__List_$Pattern__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__VariableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-130, 4, "$Pattern", null, null);
      tmp[3] = new NonTerminalStackNode(-129, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-128, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-127, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-126, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__VariableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    protected static final void _init_prod__Splice_$Pattern__lit___42_$layouts_LAYOUTLIST_argument_$Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-57, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode(-56, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-55, 0, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      builder.addAlternative(RascalRascal.prod__Splice_$Pattern__lit___42_$layouts_LAYOUTLIST_argument_$Pattern_, tmp);
	}
    protected static final void _init_prod__Set_$Pattern__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-77, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode(-76, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-71, 2, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-72, 0, "$Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-73, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-74, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-75, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-70, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-69, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__Set_$Pattern__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Literal_$Pattern__literal_$Literal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-79, 0, "$Literal", null, null);
      builder.addAlternative(RascalRascal.prod__Literal_$Pattern__literal_$Literal_, tmp);
	}
    protected static final void _init_prod__TypedVariable_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-45, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-44, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-43, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__TypedVariable_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__CallOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-68, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-67, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-62, 4, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-63, 0, "$Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-64, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-65, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-66, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-61, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-60, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-59, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-58, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__CallOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__QualifiedName_$Pattern__qualifiedName_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-78, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__QualifiedName_$Pattern__qualifiedName_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-88, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-87, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-82, 2, regular__iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-83, 0, "$Mapping__$Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-84, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-85, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-86, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-81, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-80, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Anti_$Pattern__lit___33_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-133, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode(-132, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-131, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      builder.addAlternative(RascalRascal.prod__Anti_$Pattern__lit___33_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    protected static final void _init_prod__Tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-108, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode(-107, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-102, 2, regular__iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-103, 0, "$Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-104, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-105, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-106, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-101, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-100, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__Tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__ReifiedType_$Pattern__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Pattern_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Pattern_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode(-99, 10, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[9] = new NonTerminalStackNode(-98, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-97, 8, "$Pattern", null, null);
      tmp[7] = new NonTerminalStackNode(-96, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-95, 6, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[5] = new NonTerminalStackNode(-94, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-93, 4, "$Pattern", null, null);
      tmp[3] = new NonTerminalStackNode(-92, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-91, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-90, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-89, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedType_$Pattern__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Pattern_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Pattern_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Negative_$Pattern__lit___$layouts_LAYOUTLIST_argument_$Pattern_(builder);
      
        _init_prod__MultiVariable_$Pattern__qualifiedName_$QualifiedName_$layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__Descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
        _init_prod__SplicePlus_$Pattern__lit___43_$layouts_LAYOUTLIST_argument_$Pattern_(builder);
      
        _init_prod__TypedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
        _init_prod__AsType_$Pattern__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Pattern_(builder);
      
        _init_prod__List_$Pattern__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__VariableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
        _init_prod__Splice_$Pattern__lit___42_$layouts_LAYOUTLIST_argument_$Pattern_(builder);
      
        _init_prod__Set_$Pattern__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__Literal_$Pattern__literal_$Literal_(builder);
      
        _init_prod__TypedVariable_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__CallOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__QualifiedName_$Pattern__qualifiedName_$QualifiedName_(builder);
      
        _init_prod__Map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Anti_$Pattern__lit___33_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
        _init_prod__Tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__ReifiedType_$Pattern__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Pattern_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Pattern_$layouts_LAYOUTLIST_lit___41_(builder);
      
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
      
      tmp[2] = new SeparatedListStackNode(-139, 2, regular__iter_star_seps__$Sym__$layouts_LAYOUTLIST, new NonTerminalStackNode(-140, 0, "$Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-141, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-138, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode(-135, 0, regular__iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST, new NonTerminalStackNode(-136, 0, "$ProdModifier", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-137, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalRascal.prod__Unlabeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__All_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-168, 4, "$Prod", null, null);
      tmp[3] = new NonTerminalStackNode(-167, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-166, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[1] = new NonTerminalStackNode(-165, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-164, 0, "$Prod", null, null);
      builder.addAlternative(RascalRascal.prod__All_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left, tmp);
	}
    protected static final void _init_prod__AssociativityGroup_$Prod__associativity_$Assoc_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_group_$Prod_$layouts_LAYOUTLIST_lit___41__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-148, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-147, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-146, 4, "$Prod", null, null);
      tmp[3] = new NonTerminalStackNode(-145, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-144, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-143, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-142, 0, "$Assoc", null, null);
      builder.addAlternative(RascalRascal.prod__AssociativityGroup_$Prod__associativity_$Assoc_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_group_$Prod_$layouts_LAYOUTLIST_lit___41__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Reference_$Prod__lit___58_$layouts_LAYOUTLIST_referenced_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-151, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-150, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-149, 0, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(RascalRascal.prod__Reference_$Prod__lit___58_$layouts_LAYOUTLIST_referenced_$Name_, tmp);
	}
    protected static final void _init_prod__First_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-173, 4, "$Prod", null, null);
      tmp[3] = new NonTerminalStackNode(-172, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-171, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {62})});
      tmp[1] = new NonTerminalStackNode(-170, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-169, 0, "$Prod", null, null);
      builder.addAlternative(RascalRascal.prod__First_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left, tmp);
	}
    protected static final void _init_prod__Others_$Prod__lit___46_46_46_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-152, 0, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new int[] {46,46,46}, null, null);
      builder.addAlternative(RascalRascal.prod__Others_$Prod__lit___46_46_46_, tmp);
	}
    protected static final void _init_prod__Labeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new SeparatedListStackNode(-161, 6, regular__iter_star_seps__$Sym__$layouts_LAYOUTLIST, new NonTerminalStackNode(-162, 0, "$Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-163, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode(-160, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-159, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(-158, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-157, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-156, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode(-153, 0, regular__iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST, new NonTerminalStackNode(-154, 0, "$ProdModifier", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-155, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
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
    
    protected static final void _init_prod__ascii_$UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[4];
      
      tmp[3] = new CharStackNode(-177, 3, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode(-176, 2, new int[][]{{48,55}}, null, null);
      tmp[1] = new CharStackNode(-175, 1, new int[][]{{97,97}}, null, null);
      tmp[0] = new LiteralStackNode(-174, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__ascii_$UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    protected static final void _init_prod__utf16_$UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[6];
      
      tmp[5] = new CharStackNode(-183, 5, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[4] = new CharStackNode(-182, 4, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[3] = new CharStackNode(-181, 3, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode(-180, 2, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[1] = new CharStackNode(-179, 1, new int[][]{{117,117}}, null, null);
      tmp[0] = new LiteralStackNode(-178, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__utf16_$UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    protected static final void _init_prod__utf32_$UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[8];
      
      tmp[7] = new CharStackNode(-191, 7, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[6] = new CharStackNode(-190, 6, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[5] = new CharStackNode(-189, 5, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[4] = new CharStackNode(-188, 4, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[3] = new CharStackNode(-187, 3, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode(-186, 2, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[1] = new CharStackNode(-185, 1, new int[][]{{85,85}}, null, null);
      tmp[0] = new LiteralStackNode(-184, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__utf32_$UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__ascii_$UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
        _init_prod__utf16_$UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
        _init_prod__utf32_$UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
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
      
      tmp[1] = new ListStackNode(-198, 1, regular__iter__char_class___range__48_55, new CharStackNode(-199, 0, new int[][]{{48,55}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode(-197, 0, new int[][]{{48,48}}, null, null);
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
    
    protected static final void _init_prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new ListStackNode(-207, 4, regular__iter_star__char_class___range__48_57, new CharStackNode(-208, 0, new int[][]{{48,57}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[3] = new CharStackNode(-206, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode(-205, 2, new int[][]{{114,114}}, null, null);
      tmp[1] = new ListStackNode(-203, 1, regular__iter_star__char_class___range__48_57, new CharStackNode(-204, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[0] = new CharStackNode(-202, 0, new int[][]{{49,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__$RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(-212, 2, new int[][]{{114,114}}, null, null);
      tmp[1] = new ListStackNode(-210, 1, regular__iter_star__char_class___range__48_57, new CharStackNode(-211, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[0] = new CharStackNode(-209, 0, new int[][]{{48,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__$RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_(builder);
      
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
      
      tmp[0] = new LiteralStackNode(-221, 0, prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {102,97,108,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$BooleanLiteral__lit_false_, tmp);
	}
    protected static final void _init_prod__$BooleanLiteral__lit_true_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-222, 0, prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_, new int[] {116,114,117,101}, null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-220, 0, "$Declaration", null, null);
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
      
      tmp[0] = new LiteralStackNode(-223, 0, prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_, new int[] {98,114,97,99,107,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Bracket_$ProdModifier__lit_bracket_, tmp);
	}
    protected static final void _init_prod__Associativity_$ProdModifier__associativity_$Assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-224, 0, "$Assoc", null, null);
      builder.addAlternative(RascalRascal.prod__Associativity_$ProdModifier__associativity_$Assoc_, tmp);
	}
    protected static final void _init_prod__Tag_$ProdModifier__tag_$Tag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-225, 0, "$Tag", null, null);
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
      
      tmp[6] = new NonTerminalStackNode(-232, 6, "$Type", null, null);
      tmp[5] = new NonTerminalStackNode(-231, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-230, 4, prod__lit___60_58__char_class___range__60_60_char_class___range__58_58_, new int[] {60,58}, null, null);
      tmp[3] = new NonTerminalStackNode(-229, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-228, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-227, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-226, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      builder.addAlternative(RascalRascal.prod__Bounded_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___60_58_$layouts_LAYOUTLIST_bound_$Type_, tmp);
	}
    protected static final void _init_prod__Free_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-235, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-234, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-233, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
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
      
      tmp[8] = new LiteralStackNode(-269, 8, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode(-268, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-263, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-264, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-265, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-266, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-267, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-262, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-261, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(-260, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-255, 2, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-256, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-257, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-258, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-259, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-254, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-253, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__Set_$Comprehension__lit___123_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-286, 12, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode(-285, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-280, 10, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-281, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-282, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-283, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-284, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(-279, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-278, 8, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode(-277, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-276, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-275, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-274, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(-273, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-272, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-271, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-270, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__List_$Comprehension__lit___91_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-303, 8, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[7] = new NonTerminalStackNode(-302, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-297, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-298, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-299, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-300, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-301, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-296, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-295, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(-294, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-289, 2, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-290, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-291, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-292, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-293, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-288, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-287, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
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
      
      tmp[0] = new SeparatedListStackNode(-250, 0, regular__iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST, new NonTerminalStackNode(-251, 0, "$FunctionModifier", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-252, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-304, 0, "$DataTypeSelector", null, null);
      builder.addAlternative(RascalRascal.prod__Selector_$Type__selector_$DataTypeSelector_, tmp);
	}
    protected static final void _init_prod__Structured_$Type__structured_$StructuredType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-305, 0, "$StructuredType", null, null);
      builder.addAlternative(RascalRascal.prod__Structured_$Type__structured_$StructuredType_, tmp);
	}
    protected static final void _init_prod__Bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-310, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-309, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-308, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-307, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-306, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Function_$Type__function_$FunctionType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-311, 0, "$FunctionType", null, null);
      builder.addAlternative(RascalRascal.prod__Function_$Type__function_$FunctionType_, tmp);
	}
    protected static final void _init_prod__User_$Type__conditional__user_$UserType__delete__$HeaderKeyword_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-312, 0, "$UserType", null, new ICompletionFilter[] {new StringMatchRestriction(new int[] {108,101,120,105,99,97,108}), new StringMatchRestriction(new int[] {105,109,112,111,114,116}), new StringMatchRestriction(new int[] {115,116,97,114,116}), new StringMatchRestriction(new int[] {115,121,110,116,97,120}), new StringMatchRestriction(new int[] {107,101,121,119,111,114,100}), new StringMatchRestriction(new int[] {108,97,121,111,117,116}), new StringMatchRestriction(new int[] {101,120,116,101,110,100})});
      builder.addAlternative(RascalRascal.prod__User_$Type__conditional__user_$UserType__delete__$HeaderKeyword_, tmp);
	}
    protected static final void _init_prod__Basic_$Type__basic_$BasicType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-313, 0, "$BasicType", null, null);
      builder.addAlternative(RascalRascal.prod__Basic_$Type__basic_$BasicType_, tmp);
	}
    protected static final void _init_prod__Variable_$Type__typeVar_$TypeVar_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-314, 0, "$TypeVar", null, null);
      builder.addAlternative(RascalRascal.prod__Variable_$Type__typeVar_$TypeVar_, tmp);
	}
    protected static final void _init_prod__Symbol_$Type__symbol_$Sym_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-315, 0, "$Sym", null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-317, 0, "$Tags", null, null);
      tmp[1] = new NonTerminalStackNode(-318, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-319, 2, "$Visibility", null, null);
      tmp[3] = new NonTerminalStackNode(-320, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-321, 4, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new int[] {97,108,105,97,115}, null, null);
      tmp[5] = new NonTerminalStackNode(-322, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-323, 6, "$UserType", null, null);
      tmp[7] = new NonTerminalStackNode(-324, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-325, 8, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[9] = new NonTerminalStackNode(-326, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-327, 10, "$Type", null, null);
      tmp[11] = new NonTerminalStackNode(-328, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode(-329, 12, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__Alias_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_alias_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_base_$Type_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Function_$Declaration__functionDeclaration_$FunctionDeclaration_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-316, 0, "$FunctionDeclaration", null, null);
      builder.addAlternative(RascalRascal.prod__Function_$Declaration__functionDeclaration_$FunctionDeclaration_, tmp);
	}
    protected static final void _init_prod__Annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(-344, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode(-343, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(-342, 12, "$Name", null, null);
      tmp[11] = new NonTerminalStackNode(-341, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-340, 10, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[9] = new NonTerminalStackNode(-339, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-338, 8, "$Type", null, null);
      tmp[7] = new NonTerminalStackNode(-337, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-336, 6, "$Type", null, null);
      tmp[5] = new NonTerminalStackNode(-335, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-334, 4, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new int[] {97,110,110,111}, null, null);
      tmp[3] = new NonTerminalStackNode(-333, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-332, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-331, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-330, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(-363, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode(-362, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(-357, 12, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-358, 0, "$Type", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-359, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-360, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-361, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode(-356, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-355, 10, prod__lit_on__char_class___range__111_111_char_class___range__110_110_, new int[] {111,110}, null, null);
      tmp[9] = new NonTerminalStackNode(-354, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-353, 8, "$Name", null, null);
      tmp[7] = new NonTerminalStackNode(-352, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-351, 6, "$Kind", null, null);
      tmp[5] = new NonTerminalStackNode(-350, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-349, 4, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new int[] {116,97,103}, null, null);
      tmp[3] = new NonTerminalStackNode(-348, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-347, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-346, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-345, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-376, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(-375, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-370, 6, regular__iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-371, 0, "$Variable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-372, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-373, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-374, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-369, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-368, 4, "$Type", null, null);
      tmp[3] = new NonTerminalStackNode(-367, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-366, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-365, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-364, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__DataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-385, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(-384, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-383, 6, "$UserType", null, null);
      tmp[5] = new NonTerminalStackNode(-382, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-381, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode(-380, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-379, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-378, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-377, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__DataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Data_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_variants_iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-402, 12, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[11] = new NonTerminalStackNode(-401, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-396, 10, regular__iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST, new NonTerminalStackNode(-397, 0, "$Variant", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-398, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-399, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null), new NonTerminalStackNode(-400, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(-395, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-394, 8, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(-393, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-392, 6, "$UserType", null, null);
      tmp[5] = new NonTerminalStackNode(-391, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-390, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode(-389, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-388, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-387, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-386, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Data_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_variants_iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Alias_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_alias_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_base_$Type_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Function_$Declaration__functionDeclaration_$FunctionDeclaration_(builder);
      
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
      
      tmp[4] = new NonTerminalStackNode(-427, 4, "$Class", null, null);
      tmp[3] = new NonTerminalStackNode(-426, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-425, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new int[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode(-424, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-423, 0, "$Class", null, null);
      builder.addAlternative(RascalRascal.prod__Union_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Class__assoc__left, tmp);
	}
    protected static final void _init_prod__SimpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-409, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-408, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-405, 2, regular__iter_star_seps__$Range__$layouts_LAYOUTLIST, new NonTerminalStackNode(-406, 0, "$Range", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-407, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-404, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-403, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__SimpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-432, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-431, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-430, 2, "$Class", null, null);
      tmp[1] = new NonTerminalStackNode(-429, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-428, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-412, 2, "$Class", null, null);
      tmp[1] = new NonTerminalStackNode(-411, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-410, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      builder.addAlternative(RascalRascal.prod__Complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_, tmp);
	}
    protected static final void _init_prod__Difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-417, 4, "$Class", null, null);
      tmp[3] = new NonTerminalStackNode(-416, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-415, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode(-414, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-413, 0, "$Class", null, null);
      builder.addAlternative(RascalRascal.prod__Difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left, tmp);
	}
    protected static final void _init_prod__Intersection_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Class__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-422, 4, "$Class", null, null);
      tmp[3] = new NonTerminalStackNode(-421, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-420, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new int[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode(-419, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-418, 0, "$Class", null, null);
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
      
      tmp[0] = new EpsilonStackNode(-433, 0);
      builder.addAlternative(RascalRascal.prod__Empty_$Bound__, tmp);
	}
    protected static final void _init_prod__Default_$Bound__lit___59_$layouts_LAYOUTLIST_expression_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-436, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-435, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-434, 0, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
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
      
      tmp[6] = new LiteralStackNode(-456, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-455, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-450, 4, regular__iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-451, 0, "$TypeArg", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-452, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-453, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-454, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-449, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-448, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-447, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-446, 0, "$Type", null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-463, 2, "$PatternWithAction", null, null);
      tmp[1] = new NonTerminalStackNode(-462, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-461, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new int[] {99,97,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__PatternWithAction_$Case__lit_case_$layouts_LAYOUTLIST_patternWithAction_$PatternWithAction__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Default_$Case__lit_default_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-468, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-467, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-466, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-465, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-464, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
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
      
      tmp[0] = new LiteralStackNode(-527, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new int[] {102,97,105,108}, null, null);
      tmp[1] = new NonTerminalStackNode(-528, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-529, 2, "$Target", null, null);
      tmp[3] = new NonTerminalStackNode(-530, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-531, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__Fail_$Statement__lit_fail_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__TryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode(-510, 8, "$Statement", null, null);
      tmp[7] = new NonTerminalStackNode(-509, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-508, 6, prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_, new int[] {102,105,110,97,108,108,121}, null, null);
      tmp[5] = new NonTerminalStackNode(-507, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-504, 4, regular__iter_seps__$Catch__$layouts_LAYOUTLIST, new NonTerminalStackNode(-505, 0, "$Catch", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-506, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-503, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-502, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode(-501, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-500, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new int[] {116,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__TryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement_, tmp);
	}
    protected static final void _init_prod__Insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-515, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-514, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-513, 2, "$DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode(-512, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-511, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-536, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(-535, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-534, 2, "$Target", null, null);
      tmp[1] = new NonTerminalStackNode(-533, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-532, 0, prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_, new int[] {99,111,110,116,105,110,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-541, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(-540, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-539, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-538, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-537, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__FunctionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-714, 0, "$FunctionDeclaration", null, null);
      builder.addAlternative(RascalRascal.prod__FunctionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration_, tmp);
	}
    protected static final void _init_prod__Expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode(-543, 0, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-544, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-545, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__Expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__EmptyStatement_$Statement__lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-542, 0, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__EmptyStatement_$Statement__lit___59_, tmp);
	}
    protected static final void _init_prod__Try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode(-550, 4, regular__iter_seps__$Catch__$layouts_LAYOUTLIST, new NonTerminalStackNode(-551, 0, "$Catch", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-552, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-549, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-548, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode(-547, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-546, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new int[] {116,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__Try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__DoWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(-567, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode(-566, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode(-565, 12, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode(-564, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-563, 10, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode(-562, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-561, 8, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[7] = new NonTerminalStackNode(-560, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-559, 6, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[5] = new NonTerminalStackNode(-558, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-557, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-556, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-555, 2, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new int[] {100,111}, null, null);
      tmp[1] = new NonTerminalStackNode(-554, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-553, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__DoWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__For_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode(-582, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode(-581, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-580, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-579, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-574, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-575, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-576, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-577, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-578, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-573, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-572, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-571, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-570, 2, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      tmp[1] = new NonTerminalStackNode(-569, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-568, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__For_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_, tmp);
	}
    protected static final void _init_prod__AssertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-591, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(-590, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-589, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-588, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-587, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(-586, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-585, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-584, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-583, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__AssertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__While_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode(-606, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode(-605, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-604, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-603, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-598, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-599, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-600, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-601, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-602, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-597, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-596, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-595, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-594, 2, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode(-593, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-592, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__While_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_, tmp);
	}
    protected static final void _init_prod__Assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-611, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-610, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-609, 2, "$Assignment", null, null);
      tmp[1] = new NonTerminalStackNode(-608, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-607, 0, "$Assignable", null, null);
      builder.addAlternative(RascalRascal.prod__Assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement_, tmp);
	}
    protected static final void _init_prod__Filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-614, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode(-613, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-612, 0, prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new int[] {102,105,108,116,101,114}, null, null);
      builder.addAlternative(RascalRascal.prod__Filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode(-629, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode(-628, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-627, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-626, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-625, 6, "$Bound", null, null);
      tmp[5] = new NonTerminalStackNode(-624, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-619, 4, regular__iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-620, 0, "$QualifiedName", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-621, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-622, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-623, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-618, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-617, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-616, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-615, 0, prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_, new int[] {115,111,108,118,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_, tmp);
	}
    protected static final void _init_prod__Break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-645, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(-644, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-643, 2, "$Target", null, null);
      tmp[1] = new NonTerminalStackNode(-642, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-641, 0, prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,114,101,97,107}, null, null);
      builder.addAlternative(RascalRascal.prod__Break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__VariableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-713, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode(-712, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-711, 0, "$LocalVariableDeclaration", null, null);
      builder.addAlternative(RascalRascal.prod__VariableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__GlobalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-640, 6, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode(-639, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-634, 4, regular__iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-635, 0, "$QualifiedName", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-636, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-637, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-638, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-633, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-632, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-631, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-630, 0, prod__lit_global__char_class___range__103_103_char_class___range__108_108_char_class___range__111_111_char_class___range__98_98_char_class___range__97_97_char_class___range__108_108_, new int[] {103,108,111,98,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__GlobalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode(-516, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new int[] {114,101,116,117,114,110}, null, null);
      tmp[1] = new NonTerminalStackNode(-517, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-518, 2, "$Statement", null, null);
      builder.addAlternative(RascalRascal.prod__Return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__NonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-654, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode(-653, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-650, 4, regular__iter_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-651, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-652, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-649, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-646, 0, "$Label", null, null);
      tmp[1] = new NonTerminalStackNode(-647, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-648, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__NonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-523, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-522, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-521, 2, "$DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode(-520, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-519, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__Append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-526, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode(-525, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-524, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new int[] {116,104,114,111,119}, null, null);
      builder.addAlternative(RascalRascal.prod__Throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-674, 2, "$Visit", null, null);
      tmp[1] = new NonTerminalStackNode(-673, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-672, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__Visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_, tmp);
	}
    protected static final void _init_prod__IfThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new EmptyStackNode(-671, 12, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {101,108,115,101})});
      tmp[11] = new NonTerminalStackNode(-670, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-669, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode(-668, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-667, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-666, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-661, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-662, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-663, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-664, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-665, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-660, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-659, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-658, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-657, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      tmp[1] = new NonTerminalStackNode(-656, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-655, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__IfThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else_, tmp);
	}
    protected static final void _init_prod__Switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(-691, 14, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode(-690, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(-687, 12, regular__iter_seps__$Case__$layouts_LAYOUTLIST, new NonTerminalStackNode(-688, 0, "$Case", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-689, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode(-686, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-685, 10, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode(-684, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-683, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-682, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-681, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-680, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-679, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-678, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-677, 2, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {115,119,105,116,99,104}, null, null);
      tmp[1] = new NonTerminalStackNode(-676, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-675, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__Switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__IfThenElse_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_elseStatement_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new NonTerminalStackNode(-710, 14, "$Statement", null, null);
      tmp[13] = new NonTerminalStackNode(-709, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode(-708, 12, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      tmp[11] = new NonTerminalStackNode(-707, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-706, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode(-705, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-704, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-703, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-698, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-699, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-700, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-701, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-702, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-697, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-696, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-695, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-694, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      tmp[1] = new NonTerminalStackNode(-693, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-692, 0, "$Label", null, null);
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
      
      tmp[2] = new SeparatedListStackNode(-737, 2, regular__iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-738, 0, "$Renaming", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-739, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-740, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-741, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-736, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-735, 0, prod__lit_renaming__char_class___range__114_114_char_class___range__101_101_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_, new int[] {114,101,110,97,109,105,110,103}, null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-742, 0, "$StringConstant", null, null);
      builder.addAlternative(RascalRascal.prod__NonInterpolated_$StringLiteral__constant_$StringConstant_, tmp);
	}
    protected static final void _init_prod__Template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-747, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(-746, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-745, 2, "$StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode(-744, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-743, 0, "$PreStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__Template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    protected static final void _init_prod__Interpolated_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-752, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(-751, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-750, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-749, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-748, 0, "$PreStringChars", null, null);
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
      
      tmp[0] = new LiteralStackNode(-766, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new int[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__Public_$Visibility__lit_public_, tmp);
	}
    protected static final void _init_prod__Default_$Visibility__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(-767, 0);
      builder.addAlternative(RascalRascal.prod__Default_$Visibility__, tmp);
	}
    protected static final void _init_prod__Private_$Visibility__lit_private_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-768, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new int[] {112,114,105,118,97,116,101}, null, null);
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
      
      tmp[2] = new LiteralStackNode(-771, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[1] = new NonTerminalStackNode(-770, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode(-769, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
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
      
      tmp[0] = new CharStackNode(-777, 0, new int[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{60,60},{62,62},{92,92}})});
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
      
      tmp[0] = new NonTerminalStackNode(-814, 0, "$Import", null, null);
      builder.addAlternative(RascalRascal.prod__Import_$Command__imported_$Import_, tmp);
	}
    protected static final void _init_prod__Expression_$Command__expression_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-815, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Expression_$Command__expression_$Expression_, tmp);
	}
    protected static final void _init_prod__Statement_$Command__statement_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-816, 0, "$Statement", null, null);
      builder.addAlternative(RascalRascal.prod__Statement_$Command__statement_$Statement_, tmp);
	}
    protected static final void _init_prod__Declaration_$Command__declaration_$Declaration_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-817, 0, "$Declaration", null, null);
      builder.addAlternative(RascalRascal.prod__Declaration_$Command__declaration_$Declaration_, tmp);
	}
    protected static final void _init_prod__Shell_$Command__lit___58_$layouts_LAYOUTLIST_command_$ShellCommand_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-820, 2, "$ShellCommand", null, null);
      tmp[1] = new NonTerminalStackNode(-819, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-818, 0, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
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
      
      tmp[12] = new LiteralStackNode(-796, 12, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[11] = new NonTerminalStackNode(-795, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-792, 10, regular__iter_seps__$Case__$layouts_LAYOUTLIST, new NonTerminalStackNode(-793, 0, "$Case", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-794, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(-791, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-790, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(-789, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-788, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-787, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-786, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-785, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-784, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-783, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-782, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new int[] {118,105,115,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__DefaultStrategy_$Visit__lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__GivenStrategy_$Visit__strategy_$Strategy_$layouts_LAYOUTLIST_lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(-813, 14, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode(-812, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(-809, 12, regular__iter_seps__$Case__$layouts_LAYOUTLIST, new NonTerminalStackNode(-810, 0, "$Case", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-811, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode(-808, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-807, 10, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode(-806, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-805, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-804, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-803, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-802, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-801, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-800, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-799, 2, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new int[] {118,105,115,105,116}, null, null);
      tmp[1] = new NonTerminalStackNode(-798, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-797, 0, "$Strategy", null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-821, 0, "$PostProtocolChars", null, null);
      builder.addAlternative(RascalRascal.prod__Post_$ProtocolTail__post_$PostProtocolChars_, tmp);
	}
    protected static final void _init_prod__Mid_$ProtocolTail__mid_$MidProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-826, 4, "$ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode(-825, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-824, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-823, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-822, 0, "$MidProtocolChars", null, null);
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
      
      tmp[2] = new CharStackNode(-838, 2, new int[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode(-836, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode(-837, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode(-835, 0, new int[][]{{34,34}}, null, null);
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
      
      tmp[0] = new SeparatedListStackNode(-850, 0, regular__iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST, new NonTerminalStackNode(-851, 0, "$Name", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-852, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-853, 2, prod__lit___58_58__char_class___range__58_58_char_class___range__58_58_, new int[] {58,58}, null, null), new NonTerminalStackNode(-854, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {58,58})});
      builder.addAlternative(RascalRascal.prod__Default_$QualifiedName__conditional__names_iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST__not_follow__lit___58_58_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$QualifiedName__conditional__names_iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST__not_follow__lit___58_58_(builder);
      
    }
  }
	
  protected static class $URLChars {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode(-870, 0, regular__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215, new CharStackNode(-871, 0, new int[][]{{0,8},{11,12},{14,31},{33,59},{61,123},{125,16777215}}, null, null), false, null, null);
      builder.addAlternative(RascalRascal.prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_(builder);
      
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
      
      tmp[4] = new NonTerminalStackNode(-863, 4, "$StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode(-862, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-861, 2, "$StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode(-860, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-859, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__Template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_, tmp);
	}
    protected static final void _init_prod__Interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-868, 4, "$StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode(-867, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-866, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-865, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-864, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__Interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_, tmp);
	}
    protected static final void _init_prod__Mid_$StringMiddle__mid_$MidStringChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-869, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__Mid_$StringMiddle__mid_$MidStringChars_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_(builder);
      
        _init_prod__Interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_(builder);
      
        _init_prod__Mid_$StringMiddle__mid_$MidStringChars_(builder);
      
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
      
      tmp[0] = new LiteralStackNode(-872, 0, prod__lit_Z__char_class___range__90_90_, new int[] {90}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimeZonePart__lit_Z_, tmp);
	}
    protected static final void _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(-875, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(-874, 1, new int[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode(-873, 0, new int[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode(-880, 4, new int[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode(-879, 3, new int[][]{{48,53}}, null, null);
      tmp[2] = new CharStackNode(-878, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(-877, 1, new int[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode(-876, 0, new int[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[6];
      
      tmp[5] = new CharStackNode(-886, 5, new int[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode(-885, 4, new int[][]{{48,53}}, null, null);
      tmp[3] = new LiteralStackNode(-884, 3, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[2] = new CharStackNode(-883, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(-882, 1, new int[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode(-881, 0, new int[][]{{43,43},{45,45}}, null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-889, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(-888, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-887, 0, prod__lit_edit__char_class___range__101_101_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_, new int[] {101,100,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Edit_$ShellCommand__lit_edit_$layouts_LAYOUTLIST_name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Help_$ShellCommand__lit_help_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-890, 0, prod__lit_help__char_class___range__104_104_char_class___range__101_101_char_class___range__108_108_char_class___range__112_112_, new int[] {104,101,108,112}, null, null);
      builder.addAlternative(RascalRascal.prod__Help_$ShellCommand__lit_help_, tmp);
	}
    protected static final void _init_prod__Quit_$ShellCommand__lit_quit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-891, 0, prod__lit_quit__char_class___range__113_113_char_class___range__117_117_char_class___range__105_105_char_class___range__116_116_, new int[] {113,117,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Quit_$ShellCommand__lit_quit_, tmp);
	}
    protected static final void _init_prod__SetOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-896, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-895, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-894, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(-893, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-892, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new int[] {115,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__SetOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_, tmp);
	}
    protected static final void _init_prod__History_$ShellCommand__lit_history_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-897, 0, prod__lit_history__char_class___range__104_104_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__121_121_, new int[] {104,105,115,116,111,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__History_$ShellCommand__lit_history_, tmp);
	}
    protected static final void _init_prod__Unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-900, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(-899, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-898, 0, prod__lit_unimport__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {117,110,105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-903, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(-902, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-901, 0, prod__lit_undeclare__char_class___range__117_117_char_class___range__110_110_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__101_101_, new int[] {117,110,100,101,99,108,97,114,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__ListModules_$ShellCommand__lit_modules_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-904, 0, prod__lit_modules__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_, new int[] {109,111,100,117,108,101,115}, null, null);
      builder.addAlternative(RascalRascal.prod__ListModules_$ShellCommand__lit_modules_, tmp);
	}
    protected static final void _init_prod__ListDeclarations_$ShellCommand__lit_declarations_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-906, 0, prod__lit_declarations__char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_, new int[] {100,101,99,108,97,114,97,116,105,111,110,115}, null, null);
      builder.addAlternative(RascalRascal.prod__ListDeclarations_$ShellCommand__lit_declarations_, tmp);
	}
    protected static final void _init_prod__Test_$ShellCommand__lit_test_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-905, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new int[] {116,101,115,116}, null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-913, 2, "$PathPart", null, null);
      tmp[1] = new NonTerminalStackNode(-912, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-911, 0, "$ProtocolPart", null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-922, 0, "$Char", null, null);
      builder.addAlternative(RascalRascal.prod__Character_$Range__character_$Char_, tmp);
	}
    protected static final void _init_prod__FromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-927, 4, "$Char", null, null);
      tmp[3] = new NonTerminalStackNode(-926, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-925, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode(-924, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-923, 0, "$Char", null, null);
      builder.addAlternative(RascalRascal.prod__FromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Character_$Range__character_$Char_(builder);
      
        _init_prod__FromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_(builder);
      
    }
  }
	
  protected static class $Rest {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Rest__conditional__iter_star__char_class___range__0_16777215__not_follow__char_class___range__0_16777215_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode(-928, 0, regular__iter_star__char_class___range__0_16777215, new CharStackNode(-929, 0, new int[][]{{0,16777215}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{0,16777215}})});
      builder.addAlternative(RascalRascal.prod__$Rest__conditional__iter_star__char_class___range__0_16777215__not_follow__char_class___range__0_16777215_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$Rest__conditional__iter_star__char_class___range__0_16777215__not_follow__char_class___range__0_16777215_(builder);
      
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
      tmp[0] = new LiteralStackNode(-932, 0, prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_, new int[] {36,84}, null, null);
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
      
      tmp[2] = new CharStackNode(-951, 2, new int[][]{{39,39}}, null, null);
      tmp[1] = new ListStackNode(-949, 1, regular__iter_star__char_class___range__9_9_range__32_32, new CharStackNode(-950, 0, new int[][]{{9,9},{32,32}}, null, null), false, null, null);
      tmp[0] = new CharStackNode(-948, 0, new int[][]{{10,10}}, null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__$UnicodeEscape_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-952, 0, "$UnicodeEscape", null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__$UnicodeEscape_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(-953, 0, new int[][]{{0,33},{35,38},{40,59},{61,61},{63,91},{93,16777215}}, null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-955, 1, new int[][]{{34,34},{39,39},{60,60},{62,62},{92,92},{98,98},{102,102},{110,110},{114,114},{116,116}}, null, null);
      tmp[0] = new LiteralStackNode(-954, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_(builder);
      
        _init_prod__$StringCharacter__$UnicodeEscape_(builder);
      
        _init_prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_(builder);
      
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
      
      tmp[0] = new ListStackNode(-957, 0, regular__iter_star__$LAYOUT, new NonTerminalStackNode(-958, 0, "$LAYOUT", null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,10},{13,13},{32,32}}), new StringFollowRestriction(new int[] {47,47}), new StringFollowRestriction(new int[] {47,42})});
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
      
      tmp[2] = new LiteralStackNode(-1004, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-1003, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1002, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Labeled_$DataTarget__label_$Name_$layouts_LAYOUTLIST_lit___58_, tmp);
	}
    protected static final void _init_prod__Empty_$DataTarget__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(-1005, 0);
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
      
      tmp[1] = new CharStackNode(-1008, 1, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null);
      tmp[0] = new ListStackNode(-1006, 0, regular__iter__char_class___range__48_57, new CharStackNode(-1007, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new OptionalStackNode(-1016, 4, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(-1017, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[3] = new ListStackNode(-1014, 3, regular__iter__char_class___range__48_57, new CharStackNode(-1015, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new OptionalStackNode(-1012, 2, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode(-1013, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[1] = new CharStackNode(-1011, 1, new int[][]{{69,69},{101,101}}, null, null);
      tmp[0] = new ListStackNode(-1009, 0, regular__iter__char_class___range__48_57, new CharStackNode(-1010, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new OptionalStackNode(-1028, 6, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(-1029, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[5] = new ListStackNode(-1026, 5, regular__iter__char_class___range__48_57, new CharStackNode(-1027, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[4] = new OptionalStackNode(-1024, 4, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode(-1025, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[3] = new CharStackNode(-1023, 3, new int[][]{{69,69},{101,101}}, null, null);
      tmp[2] = new ListStackNode(-1021, 2, regular__iter_star__char_class___range__48_57, new CharStackNode(-1022, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[1] = new LiteralStackNode(-1020, 1, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[0] = new ListStackNode(-1018, 0, regular__iter__char_class___range__48_57, new CharStackNode(-1019, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode(-1030, 0, prod__lit___46__char_class___range__46_46_, new int[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{46,46}})}, null);
      tmp[1] = new ListStackNode(-1031, 1, regular__iter__char_class___range__48_57, new CharStackNode(-1032, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new OptionalStackNode(-1033, 2, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(-1034, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[6];
      
      tmp[5] = new OptionalStackNode(-1050, 5, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(-1051, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[4] = new ListStackNode(-1048, 4, regular__iter__char_class___range__48_57, new CharStackNode(-1049, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[3] = new OptionalStackNode(-1046, 3, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode(-1047, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[2] = new CharStackNode(-1045, 2, new int[][]{{69,69},{101,101}}, null, null);
      tmp[1] = new ListStackNode(-1043, 1, regular__iter__char_class___range__48_57, new CharStackNode(-1044, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new LiteralStackNode(-1042, 0, prod__lit___46__char_class___range__46_46_, new int[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{46,46}})}, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[4];
      
      tmp[3] = new OptionalStackNode(-1040, 3, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(-1041, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[2] = new ListStackNode(-1038, 2, regular__iter_star__char_class___range__48_57, new CharStackNode(-1039, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[1] = new LiteralStackNode(-1037, 1, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {46})});
      tmp[0] = new ListStackNode(-1035, 0, regular__iter__char_class___range__48_57, new CharStackNode(-1036, 0, new int[][]{{48,57}}, null, null), true, null, null);
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
      
      tmp[0] = new EpsilonStackNode(-1054, 0);
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
      
      tmp[0] = new NonTerminalStackNode(-1057, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Unconditional_$Replacement__replacementExpression_$Expression_, tmp);
	}
    protected static final void _init_prod__Conditional_$Replacement__replacementExpression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode(-1062, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1063, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1064, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1065, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-1066, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1061, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1060, 2, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new int[] {119,104,101,110}, null, null);
      tmp[1] = new NonTerminalStackNode(-1059, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1058, 0, "$Expression", null, null);
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
      
      tmp[0] = new LiteralStackNode(-1079, 0, prod__lit_right__char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_, new int[] {114,105,103,104,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Right_$Assoc__lit_right_, tmp);
	}
    protected static final void _init_prod__NonAssociative_$Assoc__lit_non_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1080, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__NonAssociative_$Assoc__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__Left_$Assoc__lit_left_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1081, 0, prod__lit_left__char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_, new int[] {108,101,102,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Left_$Assoc__lit_left_, tmp);
	}
    protected static final void _init_prod__Associative_$Assoc__lit_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1082, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {97,115,115,111,99}, null, null);
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
      
      tmp[0] = new ListStackNode(-1085, 0, regular__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115, new CharStackNode(-1086, 0, new int[][]{{100,100},{105,105},{109,109},{115,115}}, null, null), false, null, null);
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
      
      tmp[2] = new CharStackNode(-1090, 2, new int[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode(-1088, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode(-1089, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode(-1087, 0, new int[][]{{62,62}}, null, null);
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
    
    protected static final void _init_prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(-1091, 0, new int[][]{{0,46},{48,59},{61,61},{63,91},{93,16777215}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_, tmp);
	}
    protected static final void _init_prod__$RegExp__char_class___range__60_60_expression_$Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(-1094, 2, new int[][]{{62,62}}, null, null);
      tmp[1] = new NonTerminalStackNode(-1093, 1, "$Expression", null, null);
      tmp[0] = new CharStackNode(-1092, 0, new int[][]{{60,60}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__char_class___range__60_60_expression_$Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101, tmp);
	}
    protected static final void _init_prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-1100, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new ListStackNode(-1098, 3, regular__iter_star__$NamedRegExp, new NonTerminalStackNode(-1099, 0, "$NamedRegExp", null, null), false, null, null);
      tmp[2] = new LiteralStackNode(-1097, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-1096, 1, "$Name", null, null);
      tmp[0] = new LiteralStackNode(-1095, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_, tmp);
	}
    protected static final void _init_prod__$RegExp__lit___60_$Name_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-1103, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode(-1102, 1, "$Name", null, null);
      tmp[0] = new LiteralStackNode(-1101, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__lit___60_$Name_lit___62_, tmp);
	}
    protected static final void _init_prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-1105, 1, new int[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode(-1104, 0, new int[][]{{92,92}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__$RegExp__$Backslash_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1106, 0, "$Backslash", null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__$Backslash_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(builder);
      
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
      
      tmp[0] = new NonTerminalStackNode(-1114, 0, "$PathChars", null, null);
      builder.addAlternative(RascalRascal.prod__NonInterpolated_$PathPart__pathChars_$PathChars_, tmp);
	}
    protected static final void _init_prod__Interpolated_$PathPart__pre_$PrePathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-1119, 4, "$PathTail", null, null);
      tmp[3] = new NonTerminalStackNode(-1118, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1117, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-1116, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1115, 0, "$PrePathChars", null, null);
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
      
      tmp[18] = new LiteralStackNode(-1155, 18, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[17] = new NonTerminalStackNode(-1154, 17, "$layouts_LAYOUTLIST", null, null);
      tmp[16] = new NonTerminalStackNode(-1153, 16, "$Expression", null, null);
      tmp[15] = new NonTerminalStackNode(-1152, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new LiteralStackNode(-1151, 14, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[13] = new NonTerminalStackNode(-1150, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode(-1149, 12, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[11] = new NonTerminalStackNode(-1148, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-1147, 10, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[9] = new NonTerminalStackNode(-1146, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new SeparatedListStackNode(-1143, 8, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1144, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1145, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode(-1142, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-1141, 6, "$StringMiddle", null, null);
      tmp[5] = new NonTerminalStackNode(-1140, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1137, 4, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1138, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1139, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-1136, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1135, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode(-1134, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1133, 0, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new int[] {100,111}, null, null);
      builder.addAlternative(RascalRascal.prod__DoWhile_$StringTemplate__lit_do_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__While_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode(-1176, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(-1175, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(-1172, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1173, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1174, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(-1171, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(-1170, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(-1169, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-1166, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1167, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1168, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(-1165, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-1164, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(-1163, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1162, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-1161, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1160, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-1159, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1158, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-1157, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1156, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__While_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__IfThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[29];
      
      tmp[28] = new LiteralStackNode(-1242, 28, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[27] = new NonTerminalStackNode(-1241, 27, "$layouts_LAYOUTLIST", null, null);
      tmp[26] = new SeparatedListStackNode(-1238, 26, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1239, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1240, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[25] = new NonTerminalStackNode(-1237, 25, "$layouts_LAYOUTLIST", null, null);
      tmp[24] = new NonTerminalStackNode(-1236, 24, "$StringMiddle", null, null);
      tmp[23] = new NonTerminalStackNode(-1235, 23, "$layouts_LAYOUTLIST", null, null);
      tmp[22] = new SeparatedListStackNode(-1232, 22, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1233, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1234, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[21] = new NonTerminalStackNode(-1231, 21, "$layouts_LAYOUTLIST", null, null);
      tmp[20] = new LiteralStackNode(-1230, 20, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[19] = new NonTerminalStackNode(-1229, 19, "$layouts_LAYOUTLIST", null, null);
      tmp[18] = new LiteralStackNode(-1228, 18, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      tmp[17] = new NonTerminalStackNode(-1227, 17, "$layouts_LAYOUTLIST", null, null);
      tmp[16] = new LiteralStackNode(-1226, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(-1225, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(-1222, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1223, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1224, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(-1221, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(-1220, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(-1219, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-1216, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1217, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1218, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(-1215, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-1214, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(-1213, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1212, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-1211, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1206, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1207, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1208, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1209, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-1210, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1205, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1204, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-1203, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1202, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      builder.addAlternative(RascalRascal.prod__IfThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__For_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode(-1201, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(-1200, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(-1197, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1198, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1199, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(-1196, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(-1195, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(-1194, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-1191, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1192, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1193, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(-1190, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-1189, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(-1188, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1187, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-1186, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1181, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1182, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1183, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1184, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-1185, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1180, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1179, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-1178, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1177, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      builder.addAlternative(RascalRascal.prod__For_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__IfThen_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode(-1267, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(-1266, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(-1263, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1264, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1265, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(-1262, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(-1261, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(-1260, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-1257, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1258, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1259, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(-1256, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-1255, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(-1254, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1253, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-1252, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1247, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1248, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1249, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1250, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-1251, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1246, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1245, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-1244, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1243, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
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
      
      tmp[1] = new ListStackNode(-1269, 1, regular__iter_star__char_class___range__48_57, new CharStackNode(-1270, 0, new int[][]{{48,57}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode(-1268, 0, new int[][]{{49,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__$DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1271, 0, prod__lit_0__char_class___range__48_48_, new int[] {48}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
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
      
      tmp[0] = new CharStackNode(-1284, 0, new int[][]{{9,10},{13,13},{32,32}}, null, null);
      builder.addAlternative(RascalRascal.prod__$LAYOUT__char_class___range__9_10_range__13_13_range__32_32_, tmp);
	}
    protected static final void _init_prod__$LAYOUT__$Comment_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1285, 0, "$Comment", null, null);
      builder.addAlternative(RascalRascal.prod__$LAYOUT__$Comment_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$LAYOUT__char_class___range__9_10_range__13_13_range__32_32_(builder);
      
        _init_prod__$LAYOUT__$Comment_(builder);
      
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
      
      tmp[0] = new SeparatedListStackNode(-1281, 0, regular__iter_star_seps__$Toplevel__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1282, 0, "$Toplevel", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1283, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-1298, 0, "$SyntaxDefinition", null, null);
      builder.addAlternative(RascalRascal.prod__Syntax_$Import__syntax_$SyntaxDefinition_, tmp);
	}
    protected static final void _init_prod__Default_$Import__lit_import_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-1303, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(-1302, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1301, 2, "$ImportedModule", null, null);
      tmp[1] = new NonTerminalStackNode(-1300, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1299, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Import__lit_import_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Extend_$Import__lit_extend_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-1308, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(-1307, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1306, 2, "$ImportedModule", null, null);
      tmp[1] = new NonTerminalStackNode(-1305, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1304, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {101,120,116,101,110,100}, null, null);
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
      
      tmp[6] = new LiteralStackNode(-1296, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(-1295, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1290, 4, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1291, 0, "$Type", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1292, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1293, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-1294, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1289, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1288, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-1287, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1286, 0, "$QualifiedName", null, new ICompletionFilter[] {new StringFollowRequirement(new int[] {91})});
      builder.addAlternative(RascalRascal.prod__Parametric_$UserType__conditional__name_$QualifiedName__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Name_$UserType__name_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1297, 0, "$QualifiedName", null, null);
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
      
      tmp[4] = new LiteralStackNode(-1319, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode(-1318, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-1315, 2, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1316, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1317, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-1314, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1313, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-1329, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(-1328, 1, "$Command", null, null);
      tmp[0] = new NonTerminalStackNode(-1327, 0, "$layouts_LAYOUTLIST", null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-1335, 0, "$DecimalIntegerLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__DecimalIntegerLiteral_$IntegerLiteral__decimal_$DecimalIntegerLiteral_, tmp);
	}
    protected static final void _init_prod__OctalIntegerLiteral_$IntegerLiteral__octal_$OctalIntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1336, 0, "$OctalIntegerLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__OctalIntegerLiteral_$IntegerLiteral__octal_$OctalIntegerLiteral_, tmp);
	}
    protected static final void _init_prod__HexIntegerLiteral_$IntegerLiteral__hex_$HexIntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1337, 0, "$HexIntegerLiteral", null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-1333, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Labeled_$Target__name_$Name_, tmp);
	}
    protected static final void _init_prod__Empty_$Target__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(-1334, 0);
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
      
      tmp[10] = new LiteralStackNode(-1358, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode(-1357, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-1356, 8, "$Prod", null, null);
      tmp[7] = new NonTerminalStackNode(-1355, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1354, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode(-1353, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1352, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-1351, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1350, 2, prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_, new int[] {115,121,110,116,97,120}, null, null);
      tmp[1] = new NonTerminalStackNode(-1349, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1348, 0, "$Start", null, null);
      builder.addAlternative(RascalRascal.prod__Language_$SyntaxDefinition__start_$Start_$layouts_LAYOUTLIST_lit_syntax_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Keyword_$SyntaxDefinition__lit_keyword_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-1367, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(-1366, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-1365, 6, "$Prod", null, null);
      tmp[5] = new NonTerminalStackNode(-1364, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-1363, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode(-1362, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1361, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-1360, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1359, 0, prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_, new int[] {107,101,121,119,111,114,100}, null, null);
      builder.addAlternative(RascalRascal.prod__Keyword_$SyntaxDefinition__lit_keyword_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Lexical_$SyntaxDefinition__lit_lexical_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-1376, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(-1375, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-1374, 6, "$Prod", null, null);
      tmp[5] = new NonTerminalStackNode(-1373, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-1372, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode(-1371, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1370, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-1369, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1368, 0, prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_, new int[] {108,101,120,105,99,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__Lexical_$SyntaxDefinition__lit_lexical_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Layout_$SyntaxDefinition__vis_$Visibility_$layouts_LAYOUTLIST_lit_layout_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode(-1387, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode(-1386, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-1385, 8, "$Prod", null, null);
      tmp[7] = new NonTerminalStackNode(-1384, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1383, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode(-1382, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1381, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-1380, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1379, 2, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new int[] {108,97,121,111,117,116}, null, null);
      tmp[1] = new NonTerminalStackNode(-1378, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1377, 0, "$Visibility", null, null);
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
      
      tmp[0] = new LiteralStackNode(-1388, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new int[] {97,108,108}, null, null);
      builder.addAlternative(RascalRascal.prod__All_$Kind__lit_all_, tmp);
	}
    protected static final void _init_prod__Module_$Kind__lit_module_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1389, 0, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Module_$Kind__lit_module_, tmp);
	}
    protected static final void _init_prod__Variable_$Kind__lit_variable_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1390, 0, prod__lit_variable__char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_, new int[] {118,97,114,105,97,98,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Variable_$Kind__lit_variable_, tmp);
	}
    protected static final void _init_prod__Anno_$Kind__lit_anno_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1391, 0, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new int[] {97,110,110,111}, null, null);
      builder.addAlternative(RascalRascal.prod__Anno_$Kind__lit_anno_, tmp);
	}
    protected static final void _init_prod__Data_$Kind__lit_data_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1392, 0, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      builder.addAlternative(RascalRascal.prod__Data_$Kind__lit_data_, tmp);
	}
    protected static final void _init_prod__Tag_$Kind__lit_tag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1393, 0, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new int[] {116,97,103}, null, null);
      builder.addAlternative(RascalRascal.prod__Tag_$Kind__lit_tag_, tmp);
	}
    protected static final void _init_prod__Function_$Kind__lit_function_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1394, 0, prod__lit_function__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_, new int[] {102,117,110,99,116,105,111,110}, null, null);
      builder.addAlternative(RascalRascal.prod__Function_$Kind__lit_function_, tmp);
	}
    protected static final void _init_prod__View_$Kind__lit_view_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1395, 0, prod__lit_view__char_class___range__118_118_char_class___range__105_105_char_class___range__101_101_char_class___range__119_119_, new int[] {118,105,101,119}, null, null);
      builder.addAlternative(RascalRascal.prod__View_$Kind__lit_view_, tmp);
	}
    protected static final void _init_prod__Alias_$Kind__lit_alias_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1396, 0, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new int[] {97,108,105,97,115}, null, null);
      builder.addAlternative(RascalRascal.prod__Alias_$Kind__lit_alias_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__All_$Kind__lit_all_(builder);
      
        _init_prod__Module_$Kind__lit_module_(builder);
      
        _init_prod__Variable_$Kind__lit_variable_(builder);
      
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
      
      tmp[4] = new NonTerminalStackNode(-1411, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-1410, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1409, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-1408, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1407, 0, "$Expression", null, null);
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
      
      tmp[2] = new LiteralStackNode(-1418, 2, prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_, new int[] {58,47,47}, null, null);
      tmp[1] = new NonTerminalStackNode(-1417, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode(-1416, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
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
      
      tmp[2] = new LiteralStackNode(-1439, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(-1438, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode(-1437, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
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
    
    protected static final void _init_prod__$Char__$UnicodeEscape__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1440, 0, "$UnicodeEscape", null, null);
      builder.addAlternative(RascalRascal.prod__$Char__$UnicodeEscape__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(-1441, 0, new int[][]{{0,31},{33,33},{35,38},{40,44},{46,59},{61,61},{63,90},{94,16777215}}, null, null);
      builder.addAlternative(RascalRascal.prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__$Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-1443, 1, new int[][]{{32,32},{34,34},{39,39},{45,45},{60,60},{62,62},{91,93},{98,98},{102,102},{110,110},{114,114},{116,116}}, null, null);
      tmp[0] = new LiteralStackNode(-1442, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__$Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$Char__$UnicodeEscape__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116(builder);
      
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
      
      tmp[1] = new LiteralStackNode(-1445, 1, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[0] = new NonTerminalStackNode(-1444, 0, "$URLChars", null, null);
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
      
      tmp[0] = new LiteralStackNode(-1448, 0, prod__lit_bottom_up_break__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,111,116,116,111,109,45,117,112,45,98,114,101,97,107}, null, null);
      builder.addAlternative(RascalRascal.prod__BottomUpBreak_$Strategy__lit_bottom_up_break_, tmp);
	}
    protected static final void _init_prod__Outermost_$Strategy__lit_outermost_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1449, 0, prod__lit_outermost__char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_, new int[] {111,117,116,101,114,109,111,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Outermost_$Strategy__lit_outermost_, tmp);
	}
    protected static final void _init_prod__BottomUp_$Strategy__lit_bottom_up_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1450, 0, prod__lit_bottom_up__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_, new int[] {98,111,116,116,111,109,45,117,112}, null, null);
      builder.addAlternative(RascalRascal.prod__BottomUp_$Strategy__lit_bottom_up_, tmp);
	}
    protected static final void _init_prod__Innermost_$Strategy__lit_innermost_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1451, 0, prod__lit_innermost__char_class___range__105_105_char_class___range__110_110_char_class___range__110_110_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_, new int[] {105,110,110,101,114,109,111,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Innermost_$Strategy__lit_innermost_, tmp);
	}
    protected static final void _init_prod__TopDownBreak_$Strategy__lit_top_down_break_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1452, 0, prod__lit_top_down_break__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {116,111,112,45,100,111,119,110,45,98,114,101,97,107}, null, null);
      builder.addAlternative(RascalRascal.prod__TopDownBreak_$Strategy__lit_top_down_break_, tmp);
	}
    protected static final void _init_prod__TopDown_$Strategy__lit_top_down_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1453, 0, prod__lit_top_down__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_, new int[] {116,111,112,45,100,111,119,110}, null, null);
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
      
      tmp[4] = new LiteralStackNode(-1473, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-1472, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-1467, 2, regular__iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1468, 0, "$TypeVar", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1469, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1470, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-1471, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-1466, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1465, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
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
      
      tmp[0] = new LiteralStackNode(-1478, 0, prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_, new int[] {115,111,108,118,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_solve_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_rat_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1477, 0, prod__lit_rat__char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_, new int[] {114,97,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_rat_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_break_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1479, 0, prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,114,101,97,107}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_break_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_rel_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1480, 0, prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new int[] {114,101,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_rel_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_false_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1481, 0, prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {102,97,108,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_false_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_any_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1482, 0, prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_, new int[] {97,110,121}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_any_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_all_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1483, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new int[] {97,108,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_all_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_real_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1485, 0, prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_, new int[] {114,101,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_real_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_finally_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1484, 0, prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_, new int[] {102,105,110,97,108,108,121}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_finally_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_bool_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1486, 0, prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_, new int[] {98,111,111,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_bool_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_filter_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1487, 0, prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new int[] {102,105,108,116,101,114}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_filter_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_datetime_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1488, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new int[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_datetime_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_while_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1491, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_while_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_case_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1490, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new int[] {99,97,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_case_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_layout_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1489, 0, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new int[] {108,97,121,111,117,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_layout_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_num_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1492, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new int[] {110,117,109}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_num_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_set_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1493, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new int[] {115,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_set_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_bag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1494, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new int[] {98,97,103}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_bag_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1495, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {97,115,115,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_assoc_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_for_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1496, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_for_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_continue_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1497, 0, prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_, new int[] {99,111,110,116,105,110,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_continue_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_bracket_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1498, 0, prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_, new int[] {98,114,97,99,107,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_bracket_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_visit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1499, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new int[] {118,105,115,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_visit_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_return_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1504, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new int[] {114,101,116,117,114,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_return_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_else_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1503, 0, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_else_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_in_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1502, 0, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new int[] {105,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_in_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_it_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1501, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new int[] {105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_it_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_join_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1500, 0, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new int[] {106,111,105,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_join_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_if_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1505, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_if_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_non_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1506, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_one_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1507, 0, prod__lit_one__char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_, new int[] {111,110,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_one_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_str_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1508, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new int[] {115,116,114}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_str_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_node_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1509, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new int[] {110,111,100,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_node_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_catch_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1510, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {99,97,116,99,104}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_catch_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1512, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_type_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_notin_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1511, 0, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new int[] {110,111,116,105,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_notin_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_append_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1514, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_append_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_extend_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1513, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_extend_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_data_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1515, 0, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_data_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_tag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1516, 0, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new int[] {116,97,103}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_tag_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_dynamic_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1517, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new int[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_dynamic_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_list_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1518, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new int[] {108,105,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_list_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_value_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1519, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new int[] {118,97,108,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_value_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__$BasicType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1520, 0, "$BasicType", null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__$BasicType_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_try_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1521, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new int[] {116,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_try_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_void_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1522, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new int[] {118,111,105,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_void_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_public_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1523, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new int[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_public_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_anno_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1524, 0, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new int[] {97,110,110,111}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_anno_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_insert_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1525, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_insert_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_loc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1527, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new int[] {108,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_loc_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_assert_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1526, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_assert_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_import_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1528, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_import_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_start_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1529, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_start_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_test_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1530, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new int[] {116,101,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_test_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_map_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1531, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new int[] {109,97,112}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_map_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_private_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1533, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new int[] {112,114,105,118,97,116,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_private_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_true_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1532, 0, prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_, new int[] {116,114,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_true_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_module_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1534, 0, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_module_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_throws_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1535, 0, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new int[] {116,104,114,111,119,115}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_throws_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_default_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1536, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_default_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_alias_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1537, 0, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new int[] {97,108,105,97,115}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_alias_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_throw_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1538, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new int[] {116,104,114,111,119}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_throw_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_switch_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1539, 0, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {115,119,105,116,99,104}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_switch_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_mod_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1541, 0, prod__lit_mod__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_, new int[] {109,111,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_mod_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_fail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1540, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new int[] {102,97,105,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_fail_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_int_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1542, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new int[] {105,110,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_int_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_tuple_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1543, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new int[] {116,117,112,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_tuple_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$RascalKeywords__lit_solve_(builder);
      
        _init_prod__$RascalKeywords__lit_rat_(builder);
      
        _init_prod__$RascalKeywords__lit_break_(builder);
      
        _init_prod__$RascalKeywords__lit_rel_(builder);
      
        _init_prod__$RascalKeywords__lit_false_(builder);
      
        _init_prod__$RascalKeywords__lit_any_(builder);
      
        _init_prod__$RascalKeywords__lit_all_(builder);
      
        _init_prod__$RascalKeywords__lit_real_(builder);
      
        _init_prod__$RascalKeywords__lit_finally_(builder);
      
        _init_prod__$RascalKeywords__lit_bool_(builder);
      
        _init_prod__$RascalKeywords__lit_filter_(builder);
      
        _init_prod__$RascalKeywords__lit_datetime_(builder);
      
        _init_prod__$RascalKeywords__lit_while_(builder);
      
        _init_prod__$RascalKeywords__lit_case_(builder);
      
        _init_prod__$RascalKeywords__lit_layout_(builder);
      
        _init_prod__$RascalKeywords__lit_num_(builder);
      
        _init_prod__$RascalKeywords__lit_set_(builder);
      
        _init_prod__$RascalKeywords__lit_bag_(builder);
      
        _init_prod__$RascalKeywords__lit_assoc_(builder);
      
        _init_prod__$RascalKeywords__lit_for_(builder);
      
        _init_prod__$RascalKeywords__lit_continue_(builder);
      
        _init_prod__$RascalKeywords__lit_bracket_(builder);
      
        _init_prod__$RascalKeywords__lit_visit_(builder);
      
        _init_prod__$RascalKeywords__lit_return_(builder);
      
        _init_prod__$RascalKeywords__lit_else_(builder);
      
        _init_prod__$RascalKeywords__lit_in_(builder);
      
        _init_prod__$RascalKeywords__lit_it_(builder);
      
        _init_prod__$RascalKeywords__lit_join_(builder);
      
        _init_prod__$RascalKeywords__lit_if_(builder);
      
        _init_prod__$RascalKeywords__lit_non_assoc_(builder);
      
        _init_prod__$RascalKeywords__lit_one_(builder);
      
        _init_prod__$RascalKeywords__lit_str_(builder);
      
        _init_prod__$RascalKeywords__lit_node_(builder);
      
        _init_prod__$RascalKeywords__lit_catch_(builder);
      
        _init_prod__$RascalKeywords__lit_type_(builder);
      
        _init_prod__$RascalKeywords__lit_notin_(builder);
      
        _init_prod__$RascalKeywords__lit_append_(builder);
      
        _init_prod__$RascalKeywords__lit_extend_(builder);
      
        _init_prod__$RascalKeywords__lit_data_(builder);
      
        _init_prod__$RascalKeywords__lit_tag_(builder);
      
        _init_prod__$RascalKeywords__lit_dynamic_(builder);
      
        _init_prod__$RascalKeywords__lit_list_(builder);
      
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
      
        _init_prod__$RascalKeywords__lit_private_(builder);
      
        _init_prod__$RascalKeywords__lit_true_(builder);
      
        _init_prod__$RascalKeywords__lit_module_(builder);
      
        _init_prod__$RascalKeywords__lit_throws_(builder);
      
        _init_prod__$RascalKeywords__lit_default_(builder);
      
        _init_prod__$RascalKeywords__lit_alias_(builder);
      
        _init_prod__$RascalKeywords__lit_throw_(builder);
      
        _init_prod__$RascalKeywords__lit_switch_(builder);
      
        _init_prod__$RascalKeywords__lit_mod_(builder);
      
        _init_prod__$RascalKeywords__lit_fail_(builder);
      
        _init_prod__$RascalKeywords__lit_int_(builder);
      
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
      
      tmp[2] = new LiteralStackNode(-1547, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode(-1546, 1, "$Name", null, null);
      tmp[0] = new LiteralStackNode(-1545, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__$NamedRegExp__lit___60_$Name_lit___62_, tmp);
	}
    protected static final void _init_prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-1549, 1, new int[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode(-1548, 0, new int[][]{{92,92}}, null, null);
      builder.addAlternative(RascalRascal.prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__$NamedRegExp__$NamedBackslash_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1550, 0, "$NamedBackslash", null, null);
      builder.addAlternative(RascalRascal.prod__$NamedRegExp__$NamedBackslash_, tmp);
	}
    protected static final void _init_prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(-1551, 0, new int[][]{{0,46},{48,59},{61,61},{63,91},{93,16777215}}, null, null);
      builder.addAlternative(RascalRascal.prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$NamedRegExp__lit___60_$Name_lit___62_(builder);
      
        _init_prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
        _init_prod__$NamedRegExp__$NamedBackslash_(builder);
      
        _init_prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(builder);
      
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
      
      tmp[3] = new NonTerminalStackNode(-1568, 3, "$RegExpModifier", null, null);
      tmp[2] = new LiteralStackNode(-1567, 2, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      tmp[1] = new ListStackNode(-1565, 1, regular__iter_star__$RegExp, new NonTerminalStackNode(-1566, 0, "$RegExp", null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-1564, 0, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExpLiteral__lit___47_iter_star__$RegExp_lit___47_$RegExpModifier_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$RegExpLiteral__lit___47_iter_star__$RegExp_lit___47_$RegExpModifier_(builder);
      
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
      
      tmp[2] = new ListStackNode(-1609, 2, regular__iter__char_class___range__48_57_range__65_70_range__97_102, new CharStackNode(-1610, 0, new int[][]{{48,57},{65,70},{97,102}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode(-1608, 1, new int[][]{{88,88},{120,120}}, null, null);
      tmp[0] = new CharStackNode(-1607, 0, new int[][]{{48,48}}, null, null);
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
      
      tmp[2] = new CharStackNode(-1606, 2, new int[][]{{34,34}}, null, null);
      tmp[1] = new ListStackNode(-1604, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode(-1605, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode(-1603, 0, new int[][]{{62,62}}, null, null);
      builder.addAlternative(RascalRascal.prod__$PostStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$PostStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class start__$EvalCommand {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__$EvalCommand__$layouts_LAYOUTLIST_top_$EvalCommand_$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-1615, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(-1614, 1, "$EvalCommand", null, null);
      tmp[0] = new NonTerminalStackNode(-1613, 0, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalRascal.prod__start__$EvalCommand__$layouts_LAYOUTLIST_top_$EvalCommand_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__start__$EvalCommand__$layouts_LAYOUTLIST_top_$EvalCommand_$layouts_LAYOUTLIST_(builder);
      
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
      
      tmp[4] = new LiteralStackNode(-1624, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-1623, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-1618, 2, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1619, 0, "$Type", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1620, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1621, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-1622, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-1617, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1616, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
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
      
      tmp[1] = new CharStackNode(-1626, 1, new int[][]{{124,124}}, null, null);
      tmp[0] = new NonTerminalStackNode(-1625, 0, "$URLChars", null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-1632, 0, "$JustTime", null, null);
      builder.addAlternative(RascalRascal.prod__TimeLiteral_$DateTimeLiteral__time_$JustTime_, tmp);
	}
    protected static final void _init_prod__DateLiteral_$DateTimeLiteral__date_$JustDate_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1633, 0, "$JustDate", null, null);
      builder.addAlternative(RascalRascal.prod__DateLiteral_$DateTimeLiteral__date_$JustDate_, tmp);
	}
    protected static final void _init_prod__DateAndTimeLiteral_$DateTimeLiteral__dateAndTime_$DateAndTime_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1634, 0, "$DateAndTime", null, null);
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
    
    protected static final void _init_prod__Type_$BasicType__lit_type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1646, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Type_$BasicType__lit_type_, tmp);
	}
    protected static final void _init_prod__Set_$BasicType__lit_set_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1647, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new int[] {115,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Set_$BasicType__lit_set_, tmp);
	}
    protected static final void _init_prod__String_$BasicType__lit_str_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1648, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new int[] {115,116,114}, null, null);
      builder.addAlternative(RascalRascal.prod__String_$BasicType__lit_str_, tmp);
	}
    protected static final void _init_prod__DateTime_$BasicType__lit_datetime_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1649, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new int[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(RascalRascal.prod__DateTime_$BasicType__lit_datetime_, tmp);
	}
    protected static final void _init_prod__Bag_$BasicType__lit_bag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1650, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new int[] {98,97,103}, null, null);
      builder.addAlternative(RascalRascal.prod__Bag_$BasicType__lit_bag_, tmp);
	}
    protected static final void _init_prod__Rational_$BasicType__lit_rat_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1651, 0, prod__lit_rat__char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_, new int[] {114,97,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Rational_$BasicType__lit_rat_, tmp);
	}
    protected static final void _init_prod__Real_$BasicType__lit_real_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1652, 0, prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_, new int[] {114,101,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__Real_$BasicType__lit_real_, tmp);
	}
    protected static final void _init_prod__Node_$BasicType__lit_node_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1653, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new int[] {110,111,100,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Node_$BasicType__lit_node_, tmp);
	}
    protected static final void _init_prod__Int_$BasicType__lit_int_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1654, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new int[] {105,110,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Int_$BasicType__lit_int_, tmp);
	}
    protected static final void _init_prod__Map_$BasicType__lit_map_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1655, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new int[] {109,97,112}, null, null);
      builder.addAlternative(RascalRascal.prod__Map_$BasicType__lit_map_, tmp);
	}
    protected static final void _init_prod__Tuple_$BasicType__lit_tuple_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1656, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new int[] {116,117,112,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Tuple_$BasicType__lit_tuple_, tmp);
	}
    protected static final void _init_prod__List_$BasicType__lit_list_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1657, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new int[] {108,105,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__List_$BasicType__lit_list_, tmp);
	}
    protected static final void _init_prod__Num_$BasicType__lit_num_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1658, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new int[] {110,117,109}, null, null);
      builder.addAlternative(RascalRascal.prod__Num_$BasicType__lit_num_, tmp);
	}
    protected static final void _init_prod__Loc_$BasicType__lit_loc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1659, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new int[] {108,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__Loc_$BasicType__lit_loc_, tmp);
	}
    protected static final void _init_prod__Value_$BasicType__lit_value_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1660, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new int[] {118,97,108,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Value_$BasicType__lit_value_, tmp);
	}
    protected static final void _init_prod__Relation_$BasicType__lit_rel_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1661, 0, prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new int[] {114,101,108}, null, null);
      builder.addAlternative(RascalRascal.prod__Relation_$BasicType__lit_rel_, tmp);
	}
    protected static final void _init_prod__Void_$BasicType__lit_void_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1662, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new int[] {118,111,105,100}, null, null);
      builder.addAlternative(RascalRascal.prod__Void_$BasicType__lit_void_, tmp);
	}
    protected static final void _init_prod__Bool_$BasicType__lit_bool_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1663, 0, prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_, new int[] {98,111,111,108}, null, null);
      builder.addAlternative(RascalRascal.prod__Bool_$BasicType__lit_bool_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Type_$BasicType__lit_type_(builder);
      
        _init_prod__Set_$BasicType__lit_set_(builder);
      
        _init_prod__String_$BasicType__lit_str_(builder);
      
        _init_prod__DateTime_$BasicType__lit_datetime_(builder);
      
        _init_prod__Bag_$BasicType__lit_bag_(builder);
      
        _init_prod__Rational_$BasicType__lit_rat_(builder);
      
        _init_prod__Real_$BasicType__lit_real_(builder);
      
        _init_prod__Node_$BasicType__lit_node_(builder);
      
        _init_prod__Int_$BasicType__lit_int_(builder);
      
        _init_prod__Map_$BasicType__lit_map_(builder);
      
        _init_prod__Tuple_$BasicType__lit_tuple_(builder);
      
        _init_prod__List_$BasicType__lit_list_(builder);
      
        _init_prod__Num_$BasicType__lit_num_(builder);
      
        _init_prod__Loc_$BasicType__lit_loc_(builder);
      
        _init_prod__Value_$BasicType__lit_value_(builder);
      
        _init_prod__Relation_$BasicType__lit_rel_(builder);
      
        _init_prod__Void_$BasicType__lit_void_(builder);
      
        _init_prod__Bool_$BasicType__lit_bool_(builder);
      
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
      
      tmp[0] = new NonTerminalStackNode(-1688, 0, "$Tags", null, null);
      tmp[1] = new NonTerminalStackNode(-1689, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1690, 2, "$Visibility", null, null);
      tmp[3] = new NonTerminalStackNode(-1691, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1692, 4, "$Signature", null, null);
      tmp[5] = new NonTerminalStackNode(-1693, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1694, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(-1695, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-1696, 8, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode(-1697, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-1698, 10, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new int[] {119,104,101,110}, null, null);
      tmp[11] = new NonTerminalStackNode(-1699, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(-1700, 12, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1701, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1702, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1703, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-1704, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[13] = new NonTerminalStackNode(-1705, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new LiteralStackNode(-1706, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__Conditional_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode(-1687, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode(-1686, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-1685, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode(-1684, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1683, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode(-1682, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1681, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode(-1680, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1679, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-1678, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1677, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-1713, 6, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode(-1712, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1711, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode(-1710, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1709, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-1708, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1707, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Default_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_body_$FunctionBody__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-1720, 6, "$FunctionBody", null, null);
      tmp[5] = new NonTerminalStackNode(-1719, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1718, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode(-1717, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1716, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-1715, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1714, 0, "$Tags", null, null);
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
      
      tmp[6] = new LiteralStackNode(-1735, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-1734, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1729, 4, regular__iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1730, 0, "$TypeArg", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1731, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1732, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-1733, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-1728, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1727, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-1726, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1725, 0, "$Name", null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-1741, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(-1740, 1, "$PreModule", null, null);
      tmp[0] = new NonTerminalStackNode(-1739, 0, "$layouts_LAYOUTLIST", null, null);
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
      
      tmp[2] = new SeparatedListStackNode(-1767, 2, regular__iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1768, 0, "$Variable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1769, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1770, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-1771, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-1766, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1765, 0, "$Type", null, null);
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
      
      tmp[6] = new LiteralStackNode(-1804, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(-1803, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1798, 4, regular__iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1799, 0, "$TypeArg", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1800, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1801, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-1802, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1797, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1796, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-1795, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1794, 0, "$BasicType", null, null);
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
      
      tmp[8] = new OptionalStackNode(-1816, 8, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(-1817, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(-1818, 0, new int[][]{{44,44},{46,46}}, null, null), new CharStackNode(-1819, 1, new int[][]{{48,57}}, null, null), new OptionalStackNode(-1820, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(-1821, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(-1822, 0, new int[][]{{48,57}}, null, null), new OptionalStackNode(-1823, 1, regular__opt__char_class___range__48_57, new CharStackNode(-1824, 0, new int[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[7] = new CharStackNode(-1815, 7, new int[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode(-1814, 6, new int[][]{{48,53}}, null, null);
      tmp[5] = new LiteralStackNode(-1813, 5, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode(-1812, 4, new int[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode(-1811, 3, new int[][]{{48,53}}, null, null);
      tmp[2] = new LiteralStackNode(-1810, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new CharStackNode(-1809, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(-1808, 0, new int[][]{{48,50}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new OptionalStackNode(-1831, 6, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(-1832, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(-1833, 0, new int[][]{{44,44},{46,46}}, null, null), new CharStackNode(-1834, 1, new int[][]{{48,57}}, null, null), new OptionalStackNode(-1835, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(-1836, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(-1837, 0, new int[][]{{48,57}}, null, null), new OptionalStackNode(-1838, 1, regular__opt__char_class___range__48_57, new CharStackNode(-1839, 0, new int[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[5] = new CharStackNode(-1830, 5, new int[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode(-1829, 4, new int[][]{{48,53}}, null, null);
      tmp[3] = new CharStackNode(-1828, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode(-1827, 2, new int[][]{{48,53}}, null, null);
      tmp[1] = new CharStackNode(-1826, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(-1825, 0, new int[][]{{48,50}}, null, null);
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
      
      tmp[0] = new SequenceStackNode(-1847, 0, regular__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new AbstractStackNode[]{new CharStackNode(-1848, 0, new int[][]{{65,90},{95,95},{97,122}}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{65,90},{95,95},{97,122}})}, null), new ListStackNode(-1849, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(-1850, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})})}, null, new ICompletionFilter[] {new StringMatchRestriction(new int[] {114,97,116}), new StringMatchRestriction(new int[] {105,110}), new StringMatchRestriction(new int[] {105,109,112,111,114,116}), new StringMatchRestriction(new int[] {99,111,110,116,105,110,117,101}), new StringMatchRestriction(new int[] {97,108,108}), new StringMatchRestriction(new int[] {102,97,108,115,101}), new StringMatchRestriction(new int[] {97,110,110,111}), new StringMatchRestriction(new int[] {98,114,97,99,107,101,116}), new StringMatchRestriction(new int[] {100,97,116,97}), new StringMatchRestriction(new int[] {106,111,105,110}), new StringMatchRestriction(new int[] {108,97,121,111,117,116}), new StringMatchRestriction(new int[] {105,116}), new StringMatchRestriction(new int[] {115,119,105,116,99,104}), new StringMatchRestriction(new int[] {99,97,115,101}), new StringMatchRestriction(new int[] {114,101,116,117,114,110}), new StringMatchRestriction(new int[] {115,116,114}), new StringMatchRestriction(new int[] {119,104,105,108,101}), new StringMatchRestriction(new int[] {115,111,108,118,101}), new StringMatchRestriction(new int[] {100,121,110,97,109,105,99}), new StringMatchRestriction(new int[] {110,111,116,105,110}), new StringMatchRestriction(new int[] {101,108,115,101}), new StringMatchRestriction(new int[] {105,110,115,101,114,116}), new StringMatchRestriction(new int[] {116,121,112,101}), new StringMatchRestriction(new int[] {116,114,121}), new StringMatchRestriction(new int[] {99,97,116,99,104}), new StringMatchRestriction(new int[] {110,117,109}), new StringMatchRestriction(new int[] {110,111,100,101}), new StringMatchRestriction(new int[] {109,111,100}), new StringMatchRestriction(new int[] {112,114,105,118,97,116,101}), new StringMatchRestriction(new int[] {102,105,110,97,108,108,121}), new StringMatchRestriction(new int[] {116,114,117,101}), new StringMatchRestriction(new int[] {98,97,103}), new StringMatchRestriction(new int[] {118,111,105,100}), new StringMatchRestriction(new int[] {97,115,115,111,99}), new StringMatchRestriction(new int[] {110,111,110,45,97,115,115,111,99}), new StringMatchRestriction(new int[] {116,101,115,116}), new StringMatchRestriction(new int[] {105,102}), new StringMatchRestriction(new int[] {114,101,97,108}), new StringMatchRestriction(new int[] {108,105,115,116}), new StringMatchRestriction(new int[] {102,97,105,108}), new StringMatchRestriction(new int[] {114,101,108}), new StringMatchRestriction(new int[] {101,120,116,101,110,100}), new StringMatchRestriction(new int[] {97,112,112,101,110,100}), new StringMatchRestriction(new int[] {116,97,103}), new StringMatchRestriction(new int[] {111,110,101}), new StringMatchRestriction(new int[] {116,104,114,111,119}), new StringMatchRestriction(new int[] {115,101,116}), new StringMatchRestriction(new int[] {115,116,97,114,116}), new StringMatchRestriction(new int[] {97,110,121}), new StringMatchRestriction(new int[] {109,111,100,117,108,101}), new StringMatchRestriction(new int[] {105,110,116}), new StringMatchRestriction(new int[] {112,117,98,108,105,99}), new StringMatchRestriction(new int[] {98,111,111,108}), new StringMatchRestriction(new int[] {118,97,108,117,101}), new StringMatchRestriction(new int[] {98,114,101,97,107}), new StringMatchRestriction(new int[] {102,105,108,116,101,114}), new StringMatchRestriction(new int[] {100,97,116,101,116,105,109,101}), new StringMatchRestriction(new int[] {97,115,115,101,114,116}), new StringMatchRestriction(new int[] {108,111,99}), new StringMatchRestriction(new int[] {100,101,102,97,117,108,116}), new StringMatchRestriction(new int[] {116,104,114,111,119,115}), new StringMatchRestriction(new int[] {116,117,112,108,101}), new StringMatchRestriction(new int[] {102,111,114}), new StringMatchRestriction(new int[] {118,105,115,105,116}), new StringMatchRestriction(new int[] {97,108,105,97,115}), new StringMatchRestriction(new int[] {109,97,112})});
      builder.addAlternative(RascalRascal.prod__$Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalKeywords_, tmp);
	}
    protected static final void _init_prod__$Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new ListStackNode(-1853, 2, regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(-1854, 0, new int[][]{{45,45},{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{45,45},{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode(-1852, 1, new int[][]{{65,90},{95,95},{97,122}}, null, null);
      tmp[0] = new CharStackNode(-1851, 0, new int[][]{{92,92}}, null, null);
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
      
      tmp[0] = new EpsilonStackNode(-1856, 0);
      builder.addAlternative(RascalRascal.prod__Absent_$Start__, tmp);
	}
    protected static final void _init_prod__Present_$Start__lit_start_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1857, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
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
      
      tmp[0] = new SeparatedListStackNode(-1860, 0, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1861, 0, "$Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1862, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1863, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-1864, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
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
      
      tmp[0] = new SeparatedListStackNode(-1865, 0, regular__iter_star_seps__$Tag__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1866, 0, "$Tag", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1867, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-1870, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(-1869, 1, "$Commands", null, null);
      tmp[0] = new NonTerminalStackNode(-1868, 0, "$layouts_LAYOUTLIST", null, null);
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
      
      tmp[2] = new LiteralStackNode(-1874, 2, prod__lit___39__char_class___range__39_39_, new int[] {39}, null, null);
      tmp[1] = new ListStackNode(-1872, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode(-1873, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-1871, 0, prod__lit___39__char_class___range__39_39_, new int[] {39}, null, null);
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
      
      tmp[0] = new CharStackNode(-1875, 0, new int[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{47,47},{60,60},{62,62},{92,92}})});
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
      
      tmp[1] = new NonTerminalStackNode(-1898, 1, "$DatePart", null, null);
      tmp[0] = new LiteralStackNode(-1897, 0, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
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
      
      tmp[4] = new NonTerminalStackNode(-1908, 4, "$PathTail", null, null);
      tmp[3] = new NonTerminalStackNode(-1907, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1906, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-1905, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1904, 0, "$MidPathChars", null, null);
      builder.addAlternative(RascalRascal.prod__Mid_$PathTail__mid_$MidPathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_, tmp);
	}
    protected static final void _init_prod__Post_$PathTail__post_$PostPathChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1909, 0, "$PostPathChars", null, null);
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
      
      tmp[2] = new LiteralStackNode(-1912, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(-1911, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode(-1910, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
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
      
      tmp[4] = new NonTerminalStackNode(-1927, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-1926, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1925, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-1924, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1923, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__Arbitrary_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement_, tmp);
	}
    protected static final void _init_prod__Replacing_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_replacement_$Replacement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-1932, 4, "$Replacement", null, null);
      tmp[3] = new NonTerminalStackNode(-1931, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1930, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new int[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-1929, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1928, 0, "$Pattern", null, null);
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
      
      tmp[4] = new NonTerminalStackNode(-1937, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-1936, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1935, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode(-1934, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1933, 0, "$QualifiedName", null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-1938, 0, "$PostStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__Post_$StringTail__post_$PostStringChars_, tmp);
	}
    protected static final void _init_prod__MidTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-1943, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(-1942, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1941, 2, "$StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode(-1940, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1939, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__MidTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    protected static final void _init_prod__MidInterpolated_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-1948, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(-1947, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1946, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-1945, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1944, 0, "$MidStringChars", null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-1951, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(-1950, 1, "$Module", null, null);
      tmp[0] = new NonTerminalStackNode(-1949, 0, "$layouts_LAYOUTLIST", null, null);
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
      
      tmp[2] = new LiteralStackNode(-1970, 2, prod__lit___34__char_class___range__34_34_, new int[] {34}, null, null);
      tmp[1] = new ListStackNode(-1968, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode(-1969, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-1967, 0, prod__lit___34__char_class___range__34_34_, new int[] {34}, null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-1987, 2, "$Declarator", null, null);
      tmp[1] = new NonTerminalStackNode(-1986, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1985, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new int[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__Dynamic_$LocalVariableDeclaration__lit_dynamic_$layouts_LAYOUTLIST_declarator_$Declarator_, tmp);
	}
    protected static final void _init_prod__Default_$LocalVariableDeclaration__declarator_$Declarator_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1988, 0, "$Declarator", null, null);
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
      
      tmp[4] = new LiteralStackNode(-2011, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-2010, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2009, 2, "$Formals", null, null);
      tmp[1] = new NonTerminalStackNode(-2008, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2007, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__VarArgs_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___46_46_46_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2018, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-2017, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2016, 4, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new int[] {46,46,46}, null, null);
      tmp[3] = new NonTerminalStackNode(-2015, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2014, 2, "$Formals", null, null);
      tmp[1] = new NonTerminalStackNode(-2013, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2012, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
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
      
      tmp[7] = new CharStackNode(-2026, 7, new int[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode(-2025, 6, new int[][]{{48,51}}, null, null);
      tmp[5] = new CharStackNode(-2024, 5, new int[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode(-2023, 4, new int[][]{{48,49}}, null, null);
      tmp[3] = new CharStackNode(-2022, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode(-2021, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(-2020, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(-2019, 0, new int[][]{{48,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[10];
      
      tmp[9] = new CharStackNode(-2036, 9, new int[][]{{48,57}}, null, null);
      tmp[8] = new CharStackNode(-2035, 8, new int[][]{{48,51}}, null, null);
      tmp[7] = new LiteralStackNode(-2034, 7, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[6] = new CharStackNode(-2033, 6, new int[][]{{48,57}}, null, null);
      tmp[5] = new CharStackNode(-2032, 5, new int[][]{{48,49}}, null, null);
      tmp[4] = new LiteralStackNode(-2031, 4, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[3] = new CharStackNode(-2030, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode(-2029, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(-2028, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(-2027, 0, new int[][]{{48,57}}, null, null);
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
      
      tmp[0] = new LiteralStackNode(-2057, 0, prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_, new int[] {108,101,120,105,99,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_lexical_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_layout_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2058, 0, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new int[] {108,97,121,111,117,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_layout_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_extend_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2059, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_extend_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_syntax_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2060, 0, prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_, new int[] {115,121,110,116,97,120}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_syntax_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_keyword_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2061, 0, prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_, new int[] {107,101,121,119,111,114,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_keyword_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_import_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2062, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_import_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_start_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2063, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
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
      
      tmp[4] = new LiteralStackNode(-2091, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode(-2090, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2085, 2, regular__iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2086, 0, "$Assignable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2087, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2088, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-2089, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-2084, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2083, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__Tuple_$Assignable__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__Bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2096, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-2095, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2094, 2, "$Assignable", null, null);
      tmp[1] = new NonTerminalStackNode(-2093, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2092, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2107, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-2106, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2101, 4, regular__iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2102, 0, "$Assignable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2103, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2104, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-2105, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-2100, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2099, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2098, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2097, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__FieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[0] = new NonTerminalStackNode(-2108, 0, "$Assignable", null, null);
      tmp[1] = new NonTerminalStackNode(-2109, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2110, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[3] = new NonTerminalStackNode(-2111, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2112, 4, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__FieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_, tmp);
	}
    protected static final void _init_prod__Variable_$Assignable__qualifiedName_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2123, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__Variable_$Assignable__qualifiedName_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2117, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2116, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2115, 2, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode(-2114, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2113, 0, "$Assignable", null, null);
      builder.addAlternative(RascalRascal.prod__Annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_, tmp);
	}
    protected static final void _init_prod__IfDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2122, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2121, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2120, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(-2119, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2118, 0, "$Assignable", null, null);
      builder.addAlternative(RascalRascal.prod__IfDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_, tmp);
	}
    protected static final void _init_prod__Subscript_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscript_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2130, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(-2129, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2128, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2127, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2126, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-2125, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2124, 0, "$Assignable", null, null);
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
	
  protected static class $EvalCommand {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Statement_$EvalCommand__statement_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2134, 0, "$Statement", null, null);
      builder.addAlternative(RascalRascal.prod__Statement_$EvalCommand__statement_$Statement_, tmp);
	}
    protected static final void _init_prod__Declaration_$EvalCommand__declaration_$Declaration_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2135, 0, "$Declaration", null, null);
      builder.addAlternative(RascalRascal.prod__Declaration_$EvalCommand__declaration_$Declaration_, tmp);
	}
    protected static final void _init_prod__Import_$EvalCommand__imported_$Import_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2136, 0, "$Import", null, null);
      builder.addAlternative(RascalRascal.prod__Import_$EvalCommand__imported_$Import_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Statement_$EvalCommand__statement_$Statement_(builder);
      
        _init_prod__Declaration_$EvalCommand__declaration_$Declaration_(builder);
      
        _init_prod__Import_$EvalCommand__imported_$Import_(builder);
      
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
      
      tmp[0] = new LiteralStackNode(-2131, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new int[] {116,101,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Test_$FunctionModifier__lit_test_, tmp);
	}
    protected static final void _init_prod__Default_$FunctionModifier__lit_default_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2132, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$FunctionModifier__lit_default_, tmp);
	}
    protected static final void _init_prod__Java_$FunctionModifier__lit_java_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2133, 0, prod__lit_java__char_class___range__106_106_char_class___range__97_97_char_class___range__118_118_char_class___range__97_97_, new int[] {106,97,118,97}, null, null);
      builder.addAlternative(RascalRascal.prod__Java_$FunctionModifier__lit_java_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Test_$FunctionModifier__lit_test_(builder);
      
        _init_prod__Default_$FunctionModifier__lit_default_(builder);
      
        _init_prod__Java_$FunctionModifier__lit_java_(builder);
      
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
      
      tmp[2] = new LiteralStackNode(-2147, 2, prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_, new int[] {58,47,47}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,10},{13,13},{32,32}})});
      tmp[1] = new NonTerminalStackNode(-2146, 1, "$URLChars", null, null);
      tmp[0] = new CharStackNode(-2145, 0, new int[][]{{124,124}}, null, null);
      builder.addAlternative(RascalRascal.prod__$ProtocolChars__char_class___range__124_124_$URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$ProtocolChars__char_class___range__124_124_$URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_(builder);
      
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
      
      tmp[0] = new LiteralStackNode(-2137, 0, prod__lit___43_61__char_class___range__43_43_char_class___range__61_61_, new int[] {43,61}, null, null);
      builder.addAlternative(RascalRascal.prod__Addition_$Assignment__lit___43_61_, tmp);
	}
    protected static final void _init_prod__Product_$Assignment__lit___42_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2138, 0, prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_, new int[] {42,61}, null, null);
      builder.addAlternative(RascalRascal.prod__Product_$Assignment__lit___42_61_, tmp);
	}
    protected static final void _init_prod__Division_$Assignment__lit___47_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2139, 0, prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_, new int[] {47,61}, null, null);
      builder.addAlternative(RascalRascal.prod__Division_$Assignment__lit___47_61_, tmp);
	}
    protected static final void _init_prod__IfDefined_$Assignment__lit___63_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2140, 0, prod__lit___63_61__char_class___range__63_63_char_class___range__61_61_, new int[] {63,61}, null, null);
      builder.addAlternative(RascalRascal.prod__IfDefined_$Assignment__lit___63_61_, tmp);
	}
    protected static final void _init_prod__Default_$Assignment__lit___61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2141, 0, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Assignment__lit___61_, tmp);
	}
    protected static final void _init_prod__Append_$Assignment__lit___60_60_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2142, 0, prod__lit___60_60_61__char_class___range__60_60_char_class___range__60_60_char_class___range__61_61_, new int[] {60,60,61}, null, null);
      builder.addAlternative(RascalRascal.prod__Append_$Assignment__lit___60_60_61_, tmp);
	}
    protected static final void _init_prod__Intersection_$Assignment__lit___38_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2143, 0, prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_, new int[] {38,61}, null, null);
      builder.addAlternative(RascalRascal.prod__Intersection_$Assignment__lit___38_61_, tmp);
	}
    protected static final void _init_prod__Subtraction_$Assignment__lit___45_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2144, 0, prod__lit___45_61__char_class___range__45_45_char_class___range__61_61_, new int[] {45,61}, null, null);
      builder.addAlternative(RascalRascal.prod__Subtraction_$Assignment__lit___45_61_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Addition_$Assignment__lit___43_61_(builder);
      
        _init_prod__Product_$Assignment__lit___42_61_(builder);
      
        _init_prod__Division_$Assignment__lit___47_61_(builder);
      
        _init_prod__IfDefined_$Assignment__lit___63_61_(builder);
      
        _init_prod__Default_$Assignment__lit___61_(builder);
      
        _init_prod__Append_$Assignment__lit___60_60_61_(builder);
      
        _init_prod__Intersection_$Assignment__lit___38_61_(builder);
      
        _init_prod__Subtraction_$Assignment__lit___45_61_(builder);
      
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
      
      tmp[0] = new NonTerminalStackNode(-2157, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Name_$Field__fieldName_$Name_, tmp);
	}
    protected static final void _init_prod__Index_$Field__fieldIndex_$IntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2158, 0, "$IntegerLiteral", null, null);
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
    
    protected static final void _init_prod__$Comment__lit___47_47_conditional__iter_star__char_class___range__0_9_range__11_16777215__not_follow__char_class___range__9_9_range__13_13_range__32_32_end_of_line__tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode(-2170, 1, regular__iter_star__char_class___range__0_9_range__11_16777215, new CharStackNode(-2171, 0, new int[][]{{0,9},{11,16777215}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,9},{13,13},{32,32}}), new AtEndOfLineRequirement()});
      tmp[0] = new LiteralStackNode(-2169, 0, prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_, new int[] {47,47}, null, null);
      builder.addAlternative(RascalRascal.prod__$Comment__lit___47_47_conditional__iter_star__char_class___range__0_9_range__11_16777215__not_follow__char_class___range__9_9_range__13_13_range__32_32_end_of_line__tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__$Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2177, 2, prod__lit___42_47__char_class___range__42_42_char_class___range__47_47_, new int[] {42,47}, null, null);
      tmp[1] = new ListStackNode(-2173, 1, regular__iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215, new AlternativeStackNode(-2174, 0, regular__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215, new AbstractStackNode[]{new CharStackNode(-2175, 0, new int[][]{{42,42}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{47,47}})}), new CharStackNode(-2176, 0, new int[][]{{0,41},{43,16777215}}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-2172, 0, prod__lit___47_42__char_class___range__47_47_char_class___range__42_42_, new int[] {47,42}, null, null);
      builder.addAlternative(RascalRascal.prod__$Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$Comment__lit___47_47_conditional__iter_star__char_class___range__0_9_range__11_16777215__not_follow__char_class___range__9_9_range__13_13_range__32_32_end_of_line__tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__$Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116(builder);
      
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
      
      tmp[2] = new LiteralStackNode(-2190, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-2189, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2188, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Label__name_$Name_$layouts_LAYOUTLIST_lit___58_, tmp);
	}
    protected static final void _init_prod__Empty_$Label__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(-2191, 0);
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
      
      tmp[0] = new NonTerminalStackNode(-2192, 0, "$ProtocolChars", null, null);
      builder.addAlternative(RascalRascal.prod__NonInterpolated_$ProtocolPart__protocolChars_$ProtocolChars_, tmp);
	}
    protected static final void _init_prod__Interpolated_$ProtocolPart__pre_$PreProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2197, 4, "$ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode(-2196, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2195, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2194, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2193, 0, "$PreProtocolChars", null, null);
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
    
    protected static final void _init_prod__Default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_$Rest_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2212, 4, "$Rest", null, null);
      tmp[3] = new NonTerminalStackNode(-2211, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new EmptyStackNode(-2210, 2, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {108,101,120,105,99,97,108}), new StringFollowRestriction(new int[] {105,109,112,111,114,116}), new StringFollowRestriction(new int[] {115,116,97,114,116}), new StringFollowRestriction(new int[] {115,121,110,116,97,120}), new StringFollowRestriction(new int[] {108,97,121,111,117,116}), new StringFollowRestriction(new int[] {101,120,116,101,110,100}), new StringFollowRestriction(new int[] {107,101,121,119,111,114,100})});
      tmp[1] = new NonTerminalStackNode(-2209, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2208, 0, "$Header", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_$Rest_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_$Rest_(builder);
      
    }
  }
	
  protected static class $TagString {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$TagString__lit___123_contents_iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2230, 2, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[1] = new ListStackNode(-2223, 1, regular__iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125, new AlternativeStackNode(-2224, 0, regular__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125, new AbstractStackNode[]{new NonTerminalStackNode(-2225, 0, "$TagString", null, null), new CharStackNode(-2226, 0, new int[][]{{0,122},{124,124},{126,16777215}}, null, null), new SequenceStackNode(-2227, 0, regular__seq___lit___92_char_class___range__123_123_range__125_125, new AbstractStackNode[]{new LiteralStackNode(-2228, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null), new CharStackNode(-2229, 1, new int[][]{{123,123},{125,125}}, null, null)}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-2222, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__$TagString__lit___123_contents_iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$TagString__lit___123_contents_iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_(builder);
      
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
      
      tmp[1] = new ListStackNode(-2232, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(-2233, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode(-2231, 0, new int[][]{{65,90}}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{65,90}})}, null);
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
      
      tmp[4] = new NonTerminalStackNode(-2250, 4, "$Pattern", null, null);
      tmp[3] = new NonTerminalStackNode(-2249, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2248, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-2247, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2246, 0, "$Pattern", null, null);
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
      
      tmp[2] = new LiteralStackNode(-2278, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {61})});
      tmp[1] = new NonTerminalStackNode(-2277, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2276, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__TransitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_, tmp);
	}
    protected static final void _init_prod__ReifiedType_$Expression__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Expression_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode(-2266, 10, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[9] = new NonTerminalStackNode(-2265, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-2264, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode(-2263, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2262, 6, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[5] = new NonTerminalStackNode(-2261, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2260, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2259, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2258, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2257, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2256, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedType_$Expression__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Expression_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2522, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2521, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2520, 2, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new int[] {106,111,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode(-2519, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2518, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__And_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2639, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2638, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2637, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new int[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode(-2636, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2635, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__And_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__In_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2569, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2568, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2567, 2, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new int[] {105,110}, null, null);
      tmp[1] = new NonTerminalStackNode(-2566, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2565, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__In_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__List_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2291, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-2290, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2285, 2, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2286, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2287, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2288, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-2289, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-2284, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2283, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__List_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2539, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2538, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2537, 2, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      tmp[1] = new NonTerminalStackNode(-2536, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2535, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2306, 2, "$Visit", null, null);
      tmp[1] = new NonTerminalStackNode(-2305, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2304, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__Visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_, tmp);
	}
    protected static final void _init_prod__Product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit___42_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-2529, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-2528, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new EmptyStackNode(-2527, 4, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {42})});
      tmp[3] = new NonTerminalStackNode(-2526, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2525, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode(-2524, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2523, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit___42_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__All_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2337, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-2336, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2331, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2332, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2333, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2334, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-2335, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-2330, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2329, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2328, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2327, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new int[] {97,108,108}, null, null);
      builder.addAlternative(RascalRascal.prod__All_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__VoidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[0] = new NonTerminalStackNode(-2351, 0, "$Parameters", null, null);
      tmp[1] = new NonTerminalStackNode(-2352, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2353, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[3] = new NonTerminalStackNode(-2354, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2355, 4, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2356, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2357, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode(-2358, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2359, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      builder.addAlternative(RascalRascal.prod__VoidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__StepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-2350, 12, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[11] = new NonTerminalStackNode(-2349, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-2348, 10, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode(-2347, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-2346, 8, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new int[] {46,46}, null, null);
      tmp[7] = new NonTerminalStackNode(-2345, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2344, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-2343, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2342, 4, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[3] = new NonTerminalStackNode(-2341, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2340, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2339, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2338, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__StepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2599, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2598, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2597, 2, prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_, new int[] {61,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2596, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2595, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[0] = new NonTerminalStackNode(-2361, 0, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2362, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2363, 2, prod__lit_has__char_class___range__104_104_char_class___range__97_97_char_class___range__115_115_, new int[] {104,97,115}, null, null);
      tmp[3] = new NonTerminalStackNode(-2364, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2365, 4, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__It_$Expression__conditional__lit_it__not_precede__char_class___range__65_90_range__95_95_range__97_122_not_follow__char_class___range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2360, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new int[] {105,116}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{65,90},{95,95},{97,122}})}, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{65,90},{95,95},{97,122}})});
      builder.addAlternative(RascalRascal.prod__It_$Expression__conditional__lit_it__not_precede__char_class___range__65_90_range__95_95_range__97_122_not_follow__char_class___range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__FieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[0] = new NonTerminalStackNode(-2366, 0, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2367, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2368, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[3] = new NonTerminalStackNode(-2369, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2370, 4, "$Name", null, null);
      tmp[5] = new NonTerminalStackNode(-2371, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2372, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(-2373, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-2374, 8, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode(-2375, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-2376, 10, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      builder.addAlternative(RascalRascal.prod__FieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Splice_$Expression__lit___42_$layouts_LAYOUTLIST_argument_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2501, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2500, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2499, 0, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      builder.addAlternative(RascalRascal.prod__Splice_$Expression__lit___42_$layouts_LAYOUTLIST_argument_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2412, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-2411, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2406, 2, regular__iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2407, 0, "$Mapping__$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2408, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2409, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-2410, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-2405, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2404, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__ReifyType_$Expression__lit___35_$layouts_LAYOUTLIST_type_$Type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2415, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-2414, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2413, 0, prod__lit___35__char_class___range__35_35_, new int[] {35}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifyType_$Expression__lit___35_$layouts_LAYOUTLIST_type_$Type_, tmp);
	}
    protected static final void _init_prod__Is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2420, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2419, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2418, 2, prod__lit_is__char_class___range__105_105_char_class___range__115_115_, new int[] {105,115}, null, null);
      tmp[1] = new NonTerminalStackNode(-2417, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2416, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__Reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-2437, 12, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode(-2436, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-2431, 10, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2432, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2433, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2434, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-2435, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(-2430, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-2429, 8, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode(-2428, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2427, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-2426, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2425, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(-2424, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2423, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2422, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2421, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2644, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2643, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2642, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new int[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode(-2641, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2640, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2475, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-2474, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2469, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2470, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2471, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2472, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-2473, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-2468, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2467, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2466, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2465, 0, prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_, new int[] {97,110,121}, null, null);
      builder.addAlternative(RascalRascal.prod__Any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_mod_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2564, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2563, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2562, 2, prod__lit_mod__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_, new int[] {109,111,100}, null, null);
      tmp[1] = new NonTerminalStackNode(-2561, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2560, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_mod_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__LessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2594, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2593, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2592, 2, prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_, new int[] {60,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2591, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2590, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__LessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__SetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-2488, 12, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[11] = new NonTerminalStackNode(-2487, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-2486, 10, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode(-2485, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-2484, 8, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(-2483, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2482, 6, "$Name", null, null);
      tmp[5] = new NonTerminalStackNode(-2481, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2480, 4, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[3] = new NonTerminalStackNode(-2479, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2478, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-2477, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2476, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__SetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[0] = new LiteralStackNode(-2267, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-2268, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2269, 2, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2270, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2271, 4, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new int[] {46,46}, null, null);
      tmp[5] = new NonTerminalStackNode(-2272, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2273, 6, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode(-2274, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-2275, 8, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      builder.addAlternative(RascalRascal.prod__Range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__TransitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2281, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {61})});
      tmp[1] = new NonTerminalStackNode(-2280, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2279, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__TransitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_, tmp);
	}
    protected static final void _init_prod__Division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2517, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2516, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2515, 2, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      tmp[1] = new NonTerminalStackNode(-2514, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2513, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Literal_$Expression__literal_$Literal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2282, 0, "$Literal", null, null);
      builder.addAlternative(RascalRascal.prod__Literal_$Expression__literal_$Literal_, tmp);
	}
    protected static final void _init_prod__Negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2504, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2503, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2502, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      builder.addAlternative(RascalRascal.prod__Negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_, tmp);
	}
    protected static final void _init_prod__CallOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2302, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-2301, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2296, 4, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2297, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2298, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2299, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-2300, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-2295, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2294, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2293, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2292, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__CallOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Comprehension_$Expression__comprehension_$Comprehension_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2303, 0, "$Comprehension", null, null);
      builder.addAlternative(RascalRascal.prod__Comprehension_$Expression__comprehension_$Comprehension_, tmp);
	}
    protected static final void _init_prod__Subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2317, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(-2316, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2311, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2312, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2313, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2314, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-2315, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-2310, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2309, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-2308, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2307, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2326, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode(-2325, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2320, 2, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2321, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2322, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2323, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-2324, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-2319, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2318, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__Set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__AppendAfter_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60_60__not_follow__lit___61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2554, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2553, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2552, 2, prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_, new int[] {60,60}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {61})});
      tmp[1] = new NonTerminalStackNode(-2551, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2550, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__AppendAfter_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60_60__not_follow__lit___61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__LessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2589, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2588, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2587, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {45})});
      tmp[1] = new NonTerminalStackNode(-2586, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2585, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__LessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Remainder_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2534, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2533, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2532, 2, prod__lit___37__char_class___range__37_37_, new int[] {37}, null, null);
      tmp[1] = new NonTerminalStackNode(-2531, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2530, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Remainder_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__InsertBefore_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2559, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2558, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2557, 2, prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_, new int[] {62,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2556, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2555, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__InsertBefore_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__IfDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2609, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2608, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2607, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(-2606, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2605, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__IfDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2634, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2633, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2632, 2, prod__lit___61_61_62__char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new int[] {61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2631, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2630, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2512, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2511, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2510, 2, prod__lit_o__char_class___range__111_111_, new int[] {111}, null, null);
      tmp[1] = new NonTerminalStackNode(-2509, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2508, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__NonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2604, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2603, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2602, 2, prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_, new int[] {33,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2601, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2600, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__NonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__IfThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__right(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode(-2653, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode(-2652, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2651, 6, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[5] = new NonTerminalStackNode(-2650, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2649, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2648, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2647, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(-2646, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2645, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__IfThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__AsType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-2498, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-2497, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2496, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-2495, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2494, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-2493, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2492, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__AsType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_, tmp);
	}
    protected static final void _init_prod__FieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2381, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2380, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2379, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode(-2378, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2377, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__FieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_, tmp);
	}
    protected static final void _init_prod__Bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2386, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-2385, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2384, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2383, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2382, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__QualifiedName_$Expression__qualifiedName_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2387, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__QualifiedName_$Expression__qualifiedName_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[0] = new NonTerminalStackNode(-2540, 0, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2541, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2542, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[3] = new NonTerminalStackNode(-2543, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2544, 4, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__FieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2398, 6, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[5] = new NonTerminalStackNode(-2397, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2392, 4, regular__iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2393, 0, "$Field", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2394, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2395, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-2396, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-2391, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2390, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(-2389, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2388, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__FieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__NoMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2614, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2613, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2612, 2, prod__lit___33_58_61__char_class___range__33_33_char_class___range__58_58_char_class___range__61_61_, new int[] {33,58,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2611, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2610, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__NoMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__GetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2403, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2402, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2401, 2, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode(-2400, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2399, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__GetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__Equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2629, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2628, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2627, 2, prod__lit___60_61_61_62__char_class___range__60_60_char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new int[] {60,61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2626, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2625, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__IsDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2491, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(-2490, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2489, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__IsDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_, tmp);
	}
    protected static final void _init_prod__Negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2507, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2506, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2505, 0, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      builder.addAlternative(RascalRascal.prod__Negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_, tmp);
	}
    protected static final void _init_prod__GreaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2579, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2578, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2577, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2576, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2575, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__GreaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__NonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2444, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode(-2443, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2440, 2, regular__iter_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2441, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2442, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-2439, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2438, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__NonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__GreaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2584, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2583, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2582, 2, prod__lit___62_61__char_class___range__62_62_char_class___range__61_61_, new int[] {62,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2581, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2580, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__GreaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2619, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2618, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2617, 2, prod__lit___60_45__char_class___range__60_60_char_class___range__45_45_, new int[] {60,45}, null, null);
      tmp[1] = new NonTerminalStackNode(-2616, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2615, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__Enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2549, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2548, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2547, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode(-2546, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2545, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-2455, 8, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode(-2454, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-2451, 6, regular__iter_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2452, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2453, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-2450, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2449, 4, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[3] = new NonTerminalStackNode(-2448, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2447, 2, "$Parameters", null, null);
      tmp[1] = new NonTerminalStackNode(-2446, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2445, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__Closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__NotIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2574, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2573, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2572, 2, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new int[] {110,111,116,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode(-2571, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2570, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__NotIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2464, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode(-2463, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2458, 2, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2459, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2460, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2461, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-2462, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-2457, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2456, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__Tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__Match_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2624, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2623, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2622, 2, prod__lit___58_61__char_class___range__58_58_char_class___range__61_61_, new int[] {58,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2621, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2620, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__Match_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__TransitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_(builder);
      
        _init_prod__ReifiedType_$Expression__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Expression_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__And_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__In_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__List_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__Visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_(builder);
      
        _init_prod__Product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit___42_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__All_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__VoidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__StepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__Has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__It_$Expression__conditional__lit_it__not_precede__char_class___range__65_90_range__95_95_range__97_122_not_follow__char_class___range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__FieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Splice_$Expression__lit___42_$layouts_LAYOUTLIST_argument_$Expression__assoc__non_assoc(builder);
      
        _init_prod__Map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__ReifyType_$Expression__lit___35_$layouts_LAYOUTLIST_type_$Type_(builder);
      
        _init_prod__Is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__Reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__Any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_mod_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__LessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__SetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__TransitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_(builder);
      
        _init_prod__Division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__Literal_$Expression__literal_$Literal_(builder);
      
        _init_prod__Negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_(builder);
      
        _init_prod__CallOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Comprehension_$Expression__comprehension_$Comprehension_(builder);
      
        _init_prod__Subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__AppendAfter_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60_60__not_follow__lit___61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__LessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__Remainder_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__InsertBefore_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__IfDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__Implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__Composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__NonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__IfThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__right(builder);
      
        _init_prod__AsType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_(builder);
      
        _init_prod__FieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(builder);
      
        _init_prod__Bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__QualifiedName_$Expression__qualifiedName_$QualifiedName_(builder);
      
        _init_prod__Addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__FieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__NoMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(builder);
      
        _init_prod__GetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__Equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__IsDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_(builder);
      
        _init_prod__Negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_(builder);
      
        _init_prod__GreaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__NonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
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
      
      tmp[2] = new NonTerminalStackNode(-2671, 2, "$ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode(-2670, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2669, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__Actuals_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_, tmp);
	}
    protected static final void _init_prod__ActualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2676, 4, "$Renamings", null, null);
      tmp[3] = new NonTerminalStackNode(-2675, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2674, 2, "$ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode(-2673, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2672, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__ActualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_, tmp);
	}
    protected static final void _init_prod__Default_$ImportedModule__name_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2677, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$ImportedModule__name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Renamings_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_renamings_$Renamings_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2680, 2, "$Renamings", null, null);
      tmp[1] = new NonTerminalStackNode(-2679, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2678, 0, "$QualifiedName", null, null);
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
    
    protected static final void _init_prod__List_$Commands__commands_iter_seps__$EvalCommand__$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode(-2705, 0, regular__iter_seps__$EvalCommand__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2706, 0, "$EvalCommand", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2707, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      builder.addAlternative(RascalRascal.prod__List_$Commands__commands_iter_seps__$EvalCommand__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__List_$Commands__commands_iter_seps__$EvalCommand__$layouts_LAYOUTLIST_(builder);
      
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
      
      tmp[6] = new SeparatedListStackNode(-2724, 6, regular__iter_star_seps__$Import__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2725, 0, "$Import", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2726, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode(-2723, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2722, 4, "$QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode(-2721, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2720, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode(-2719, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2718, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__Parameters_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_params_$ModuleParameters_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new SeparatedListStackNode(-2735, 8, regular__iter_star_seps__$Import__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2736, 0, "$Import", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2737, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode(-2734, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2733, 6, "$ModuleParameters", null, null);
      tmp[5] = new NonTerminalStackNode(-2732, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2731, 4, "$QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode(-2730, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2729, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode(-2728, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2727, 0, "$Tags", null, null);
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
      
      tmp[1] = new ListStackNode(-2739, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(-2740, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode(-2738, 0, new int[][]{{97,122}}, null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-2749, 0, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-2750, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2751, 2, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      builder.addAlternative(RascalRascal.prod__EndOfLine_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___36_, tmp);
	}
    protected static final void _init_prod__Parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2748, 2, "$Nonterminal", null, null);
      tmp[1] = new NonTerminalStackNode(-2747, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2746, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      builder.addAlternative(RascalRascal.prod__Parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_, tmp);
	}
    protected static final void _init_prod__Optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2754, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(-2753, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2752, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_, tmp);
	}
    protected static final void _init_prod__CaseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2755, 0, "$CaseInsensitiveStringConstant", null, null);
      builder.addAlternative(RascalRascal.prod__CaseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_, tmp);
	}
    protected static final void _init_prod__IterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-2764, 8, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[7] = new NonTerminalStackNode(-2763, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2762, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode(-2761, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2760, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2759, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2758, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-2757, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2756, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__IterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_, tmp);
	}
    protected static final void _init_prod__Literal_$Sym__string_$StringConstant_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2765, 0, "$StringConstant", null, null);
      builder.addAlternative(RascalRascal.prod__Literal_$Sym__string_$StringConstant_, tmp);
	}
    protected static final void _init_prod__IterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2768, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode(-2767, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2766, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__IterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__Unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2861, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2860, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2859, 2, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      tmp[1] = new NonTerminalStackNode(-2858, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2857, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__NotFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2856, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2855, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2854, 2, prod__lit___33_62_62__char_class___range__33_33_char_class___range__62_62_char_class___range__62_62_, new int[] {33,62,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2853, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2852, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__NotFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__StartOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2771, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-2770, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2769, 0, prod__lit___94__char_class___range__94_94_, new int[] {94}, null, null);
      builder.addAlternative(RascalRascal.prod__StartOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_, tmp);
	}
    protected static final void _init_prod__Empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2785, 2, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[1] = new NonTerminalStackNode(-2784, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2783, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Parametrized_$Sym__conditional__nonterminal_$Nonterminal__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[0] = new NonTerminalStackNode(-2772, 0, "$Nonterminal", null, new ICompletionFilter[] {new StringFollowRequirement(new int[] {91})});
      tmp[1] = new NonTerminalStackNode(-2773, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2774, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[3] = new NonTerminalStackNode(-2775, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2776, 4, regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2777, 0, "$Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2778, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2779, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-2780, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-2781, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2782, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      builder.addAlternative(RascalRascal.prod__Parametrized_$Sym__conditional__nonterminal_$Nonterminal__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[0] = new LiteralStackNode(-2792, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2793, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2794, 2, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2795, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2796, 4, regular__iter_seps__$Sym__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2797, 0, "$Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2798, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-2799, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2800, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      builder.addAlternative(RascalRascal.prod__Sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2841, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2840, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2839, 2, prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_, new int[] {60,60}, null, null);
      tmp[1] = new NonTerminalStackNode(-2838, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2837, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__Nonterminal_$Sym__conditional__nonterminal_$Nonterminal__not_follow__lit___91_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2786, 0, "$Nonterminal", null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {91})});
      builder.addAlternative(RascalRascal.prod__Nonterminal_$Sym__conditional__nonterminal_$Nonterminal__not_follow__lit___91_, tmp);
	}
    protected static final void _init_prod__Column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2791, 4, "$IntegerLiteral", null, null);
      tmp[3] = new NonTerminalStackNode(-2790, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2789, 2, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode(-2788, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2787, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__Follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2851, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2850, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2849, 2, prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_, new int[] {62,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2848, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2847, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__IterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-2822, 8, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[7] = new NonTerminalStackNode(-2821, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2820, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode(-2819, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2818, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2817, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2816, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-2815, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2814, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__IterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__CharacterClass_$Sym__charClass_$Class_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2823, 0, "$Class", null, null);
      builder.addAlternative(RascalRascal.prod__CharacterClass_$Sym__charClass_$Class_, tmp);
	}
    protected static final void _init_prod__Alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-2813, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-2812, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-2807, 6, regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2808, 0, "$Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2809, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2810, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null), new NonTerminalStackNode(-2811, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-2806, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2805, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(-2804, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2803, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-2802, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2801, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2826, 2, "$NonterminalLabel", null, null);
      tmp[1] = new NonTerminalStackNode(-2825, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2824, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_, tmp);
	}
    protected static final void _init_prod__Start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2833, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(-2832, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2831, 4, "$Nonterminal", null, null);
      tmp[3] = new NonTerminalStackNode(-2830, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2829, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-2828, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2827, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__NotPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2846, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2845, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2844, 2, prod__lit___33_60_60__char_class___range__33_33_char_class___range__60_60_char_class___range__60_60_, new int[] {33,60,60}, null, null);
      tmp[1] = new NonTerminalStackNode(-2843, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2842, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__NotPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__Iter_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___43_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2836, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[1] = new NonTerminalStackNode(-2835, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2834, 0, "$Sym", null, null);
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
    
    protected static final void _init_prod__Binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-2887, 6, "$Statement", null, null);
      tmp[5] = new NonTerminalStackNode(-2886, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2885, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(-2884, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2883, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode(-2882, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2881, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {99,97,116,99,104}, null, null);
      builder.addAlternative(RascalRascal.prod__Binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_, tmp);
	}
    protected static final void _init_prod__Default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2892, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-2891, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2890, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-2889, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2888, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {99,97,116,99,104}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_(builder);
      
        _init_prod__Default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_(builder);
      
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
      
      tmp[4] = new NonTerminalStackNode(-2880, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2879, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2878, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new int[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2877, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2876, 0, "$Name", null, null);
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
      
      tmp[6] = new NonTerminalStackNode(-2909, 6, "$Parameters", null, null);
      tmp[5] = new NonTerminalStackNode(-2908, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2907, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2906, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2905, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-2904, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2903, 0, "$FunctionModifiers", null, null);
      builder.addAlternative(RascalRascal.prod__NoThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_, tmp);
	}
    protected static final void _init_prod__WithThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit_throws_$layouts_LAYOUTLIST_exceptions_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new SeparatedListStackNode(-2920, 10, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2921, 0, "$Type", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2922, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2923, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode(-2924, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(-2919, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-2918, 8, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new int[] {116,104,114,111,119,115}, null, null);
      tmp[7] = new NonTerminalStackNode(-2917, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2916, 6, "$Parameters", null, null);
      tmp[5] = new NonTerminalStackNode(-2915, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2914, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2913, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2912, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-2911, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2910, 0, "$FunctionModifiers", null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-2933, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-2932, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2931, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__Named_$TypeArg__type_$Type_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__Default_$TypeArg__type_$Type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2934, 0, "$Type", null, null);
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
      
      tmp[4] = new NonTerminalStackNode(-2929, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2928, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2927, 2, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2926, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2925, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Initialized_$Variable__name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_initial_$Expression_, tmp);
	}
    protected static final void _init_prod__UnInitialized_$Variable__name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2930, 0, "$Name", null, null);
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
      
      tmp[2] = new LiteralStackNode(-2949, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(-2948, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode(-2947, 0, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-2961, 0, "$LocationLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Location_$Literal__locationLiteral_$LocationLiteral_, tmp);
	}
    protected static final void _init_prod__Real_$Literal__realLiteral_$RealLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2962, 0, "$RealLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Real_$Literal__realLiteral_$RealLiteral_, tmp);
	}
    protected static final void _init_prod__RegExp_$Literal__regExpLiteral_$RegExpLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2963, 0, "$RegExpLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__RegExp_$Literal__regExpLiteral_$RegExpLiteral_, tmp);
	}
    protected static final void _init_prod__Rational_$Literal__rationalLiteral_$RationalLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2964, 0, "$RationalLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Rational_$Literal__rationalLiteral_$RationalLiteral_, tmp);
	}
    protected static final void _init_prod__Integer_$Literal__integerLiteral_$IntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2965, 0, "$IntegerLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Integer_$Literal__integerLiteral_$IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__Boolean_$Literal__booleanLiteral_$BooleanLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2966, 0, "$BooleanLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Boolean_$Literal__booleanLiteral_$BooleanLiteral_, tmp);
	}
    protected static final void _init_prod__DateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2967, 0, "$DateTimeLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__DateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_, tmp);
	}
    protected static final void _init_prod__String_$Literal__stringLiteral_$StringLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2968, 0, "$StringLiteral", null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-2977, 2, "$Body", null, null);
      tmp[1] = new NonTerminalStackNode(-2976, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2975, 0, "$Header", null, null);
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
  public AbstractStackNode[] $StringMiddle() {
    return $StringMiddle.EXPECTS;
  }
  public AbstractStackNode[] $URLChars() {
    return $URLChars.EXPECTS;
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
  public AbstractStackNode[] $Rest() {
    return $Rest.EXPECTS;
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
  public AbstractStackNode[] $PostStringChars() {
    return $PostStringChars.EXPECTS;
  }
  public AbstractStackNode[] $HexIntegerLiteral() {
    return $HexIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode[] start__$EvalCommand() {
    return start__$EvalCommand.EXPECTS;
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
  public AbstractStackNode[] $EvalCommand() {
    return $EvalCommand.EXPECTS;
  }
  public AbstractStackNode[] $Assignment() {
    return $Assignment.EXPECTS;
  }
  public AbstractStackNode[] $ProtocolChars() {
    return $ProtocolChars.EXPECTS;
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