package org.rascalmpl.library.lang.rascal.syntax;

import java.io.IOException;
import java.io.StringReader;

import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.ISourceLocation;
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

@SuppressWarnings("all")
public class RascalRascal extends org.rascalmpl.parser.gtd.SGTDBF<IConstructor, IConstructor, ISourceLocation> {
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
    
    
    
    
    _putDontNest(result, -176, -176);
    
    _putDontNest(result, -2381, -2503);
    
    _putDontNest(result, -2602, -2616);
    
    _putDontNest(result, -611, -619);
    
    _putDontNest(result, -2631, -2631);
    
    _putDontNest(result, -2596, -2606);
    
    _putDontNest(result, -2636, -2646);
    
    _putDontNest(result, -2562, -2576);
    
    _putDontNest(result, -2571, -2571);
    
    _putDontNest(result, -2577, -2621);
    
    _putDontNest(result, -2556, -2586);
    
    _putDontNest(result, -2529, -2534);
    
    _putDontNest(result, -2381, -2551);
    
    _putDontNest(result, -2561, -2621);
    
    _putDontNest(result, -2597, -2641);
    
    _putDontNest(result, -619, -611);
    
    _putDontNest(result, -2516, -2631);
    
    _putDontNest(result, -2510, -2529);
    
    _putDontNest(result, -2547, -2556);
    
    _putDontNest(result, -2513, -2534);
    
    _putDontNest(result, -2611, -2611);
    
    _putDontNest(result, -2637, -2665);
    
    _putDontNest(result, -2581, -2641);
    
    _putDontNest(result, -2566, -2596);
    
    _putDontNest(result, -2587, -2651);
    
    _putDontNest(result, -2319, -2601);
    
    _putDontNest(result, -2488, -2611);
    
    _putDontNest(result, -2524, -2586);
    
    _putDontNest(result, -2291, -2546);
    
    _putDontNest(result, -2541, -2546);
    
    _putDontNest(result, -2428, -2510);
    
    _putDontNest(result, -2319, -2513);
    
    _putDontNest(result, -2562, -2656);
    
    _putDontNest(result, -2571, -2651);
    
    _putDontNest(result, -2582, -2596);
    
    _putDontNest(result, -2851, -2878);
    
    _putDontNest(result, -2586, -2616);
    
    _putDontNest(result, -2547, -2626);
    
    _putDontNest(result, -2783, -2873);
    
    _putDontNest(result, -2572, -2646);
    
    _putDontNest(result, -2567, -2631);
    
    _putDontNest(result, -2510, -2581);
    
    _putDontNest(result, -2520, -2611);
    
    _putDontNest(result, -2535, -2606);
    
    _putDontNest(result, -2541, -2616);
    
    _putDontNest(result, -2501, -2576);
    
    _putDontNest(result, -2546, -2641);
    
    _putDontNest(result, -2529, -2596);
    
    _putDontNest(result, -2621, -2665);
    
    _putDontNest(result, -2428, -2616);
    
    _putDontNest(result, -2557, -2616);
    
    _putDontNest(result, -2530, -2641);
    
    _putDontNest(result, -2551, -2606);
    
    _putDontNest(result, -2291, -2596);
    
    _putDontNest(result, -2319, -2529);
    
    _putDontNest(result, -2804, -2878);
    
    _putDontNest(result, -2592, -2591);
    
    _putDontNest(result, -2651, -2651);
    
    _putDontNest(result, -2642, -2656);
    
    _putDontNest(result, -2319, -2665);
    
    _putDontNest(result, -2376, -2596);
    
    _putDontNest(result, -2542, -2581);
    
    _putDontNest(result, -2530, -2561);
    
    _putDontNest(result, -2552, -2611);
    
    _putDontNest(result, -2288, -2581);
    
    _putDontNest(result, -2381, -2519);
    
    _putDontNest(result, -2788, -2878);
    
    _putDontNest(result, -2769, -2863);
    
    _putDontNest(result, -2576, -2591);
    
    _putDontNest(result, -2626, -2656);
    
    _putDontNest(result, -556, -717);
    
    _putDontNest(result, -2403, -2621);
    
    _putDontNest(result, -2392, -2596);
    
    _putDontNest(result, -2525, -2616);
    
    _putDontNest(result, -2513, -2596);
    
    _putDontNest(result, -2519, -2606);
    
    _putDontNest(result, -2534, -2621);
    
    _putDontNest(result, -2546, -2561);
    
    _putDontNest(result, -2501, -2656);
    
    _putDontNest(result, -2304, -2581);
    
    _putDontNest(result, -2516, -2556);
    
    _putDontNest(result, -2541, -2529);
    
    _putDontNest(result, -2546, -2534);
    
    _putDontNest(result, -2414, -2529);
    
    _putDontNest(result, -2601, -2616);
    
    _putDontNest(result, -2631, -2626);
    
    _putDontNest(result, -2566, -2561);
    
    _putDontNest(result, -2561, -2576);
    
    _putDontNest(result, -2597, -2596);
    
    _putDontNest(result, -2606, -2601);
    
    _putDontNest(result, -2501, -2631);
    
    _putDontNest(result, -2556, -2591);
    
    _putDontNest(result, -2428, -2586);
    
    _putDontNest(result, -2304, -2534);
    
    _putDontNest(result, -420, -420);
    
    _putDontNest(result, -2510, -2546);
    
    _putDontNest(result, -2381, -2546);
    
    _putDontNest(result, -2562, -2621);
    
    _putDontNest(result, -2571, -2566);
    
    _putDontNest(result, -2586, -2581);
    
    _putDontNest(result, -611, -614);
    
    _putDontNest(result, -2488, -2596);
    
    _putDontNest(result, -2288, -2534);
    
    _putDontNest(result, -2291, -2529);
    
    _putDontNest(result, -2612, -2611);
    
    _putDontNest(result, -2868, -2878);
    
    _putDontNest(result, -2577, -2656);
    
    _putDontNest(result, -2582, -2641);
    
    _putDontNest(result, -2524, -2591);
    
    _putDontNest(result, -2288, -2646);
    
    _putDontNest(result, -2403, -2556);
    
    _putDontNest(result, -39, -133);
    
    _putDontNest(result, -2561, -2656);
    
    _putDontNest(result, -2572, -2651);
    
    _putDontNest(result, -2566, -2641);
    
    _putDontNest(result, -2581, -2596);
    
    _putDontNest(result, -2596, -2611);
    
    _putDontNest(result, -2863, -2873);
    
    _putDontNest(result, -2304, -2646);
    
    _putDontNest(result, -2291, -2631);
    
    _putDontNest(result, -2567, -2626);
    
    _putDontNest(result, -2571, -2646);
    
    _putDontNest(result, -2606, -2665);
    
    _putDontNest(result, -2319, -2636);
    
    _putDontNest(result, -2376, -2561);
    
    _putDontNest(result, -2530, -2606);
    
    _putDontNest(result, -2551, -2641);
    
    _putDontNest(result, -2513, -2611);
    
    _putDontNest(result, -2291, -2513);
    
    _putDontNest(result, -2414, -2513);
    
    _putDontNest(result, -2403, -2524);
    
    _putDontNest(result, -2561, -2556);
    
    _putDontNest(result, -2587, -2646);
    
    _putDontNest(result, -521, -717);
    
    _putDontNest(result, -2403, -2656);
    
    _putDontNest(result, -2392, -2561);
    
    _putDontNest(result, -2546, -2606);
    
    _putDontNest(result, -2519, -2561);
    
    _putDontNest(result, -2556, -2616);
    
    _putDontNest(result, -2535, -2641);
    
    _putDontNest(result, -2516, -2576);
    
    _putDontNest(result, -2552, -2596);
    
    _putDontNest(result, -2516, -2524);
    
    _putDontNest(result, -60, -121);
    
    _putDontNest(result, -2577, -2591);
    
    _putDontNest(result, -2641, -2656);
    
    _putDontNest(result, -2646, -2641);
    
    _putDontNest(result, -2403, -2576);
    
    _putDontNest(result, -2392, -2641);
    
    _putDontNest(result, -2535, -2561);
    
    _putDontNest(result, -2516, -2656);
    
    _putDontNest(result, -2547, -2621);
    
    _putDontNest(result, -2519, -2641);
    
    _putDontNest(result, -2304, -2566);
    
    _putDontNest(result, -2561, -2591);
    
    _putDontNest(result, -2562, -2586);
    
    _putDontNest(result, -2769, -2858);
    
    _putDontNest(result, -2636, -2651);
    
    _putDontNest(result, -2428, -2581);
    
    _putDontNest(result, -2376, -2641);
    
    _putDontNest(result, -2529, -2611);
    
    _putDontNest(result, -2524, -2616);
    
    _putDontNest(result, -2551, -2561);
    
    _putDontNest(result, -2520, -2596);
    
    _putDontNest(result, -2288, -2566);
    
    _putDontNest(result, -2571, -2561);
    
    _putDontNest(result, -2591, -2621);
    
    _putDontNest(result, -2567, -2581);
    
    _putDontNest(result, -2606, -2606);
    
    _putDontNest(result, -2576, -2576);
    
    _putDontNest(result, -2304, -2586);
    
    _putDontNest(result, -2376, -2626);
    
    _putDontNest(result, -2488, -2641);
    
    _putDontNest(result, -2501, -2626);
    
    _putDontNest(result, -2557, -2591);
    
    _putDontNest(result, -2428, -2591);
    
    _putDontNest(result, -2525, -2556);
    
    _putDontNest(result, -420, -425);
    
    _putDontNest(result, -2616, -2616);
    
    _putDontNest(result, -2646, -2646);
    
    _putDontNest(result, -2566, -2566);
    
    _putDontNest(result, -2597, -2651);
    
    _putDontNest(result, -2392, -2626);
    
    _putDontNest(result, -2288, -2586);
    
    _putDontNest(result, -2541, -2591);
    
    _putDontNest(result, -2542, -2586);
    
    _putDontNest(result, -2381, -2529);
    
    _putDontNest(result, -2572, -2596);
    
    _putDontNest(result, -2592, -2656);
    
    _putDontNest(result, -2581, -2651);
    
    _putDontNest(result, -2587, -2641);
    
    _putDontNest(result, -2381, -2665);
    
    _putDontNest(result, -2414, -2636);
    
    _putDontNest(result, -2525, -2591);
    
    _putDontNest(result, -2546, -2551);
    
    _putDontNest(result, -415, -430);
    
    _putDontNest(result, -2319, -2519);
    
    _putDontNest(result, -2414, -2546);
    
    _putDontNest(result, -39, -136);
    
    _putDontNest(result, -2571, -2641);
    
    _putDontNest(result, -2576, -2656);
    
    _putDontNest(result, -2607, -2621);
    
    _putDontNest(result, -2428, -2646);
    
    _putDontNest(result, -2488, -2561);
    
    _putDontNest(result, -2510, -2586);
    
    _putDontNest(result, -2291, -2626);
    
    _putDontNest(result, -2520, -2541);
    
    _putDontNest(result, -2541, -2556);
    
    _putDontNest(result, -2530, -2551);
    
    _putDontNest(result, -2516, -2529);
    
    _putDontNest(result, -2319, -2503);
    
    _putDontNest(result, -2428, -2516);
    
    _putDontNest(result, -2769, -2873);
    
    _putDontNest(result, -2566, -2646);
    
    _putDontNest(result, -2516, -2621);
    
    _putDontNest(result, -2547, -2656);
    
    _putDontNest(result, -2529, -2606);
    
    _putDontNest(result, -2535, -2596);
    
    _putDontNest(result, -2552, -2641);
    
    _putDontNest(result, -2304, -2651);
    
    _putDontNest(result, -2319, -2551);
    
    _putDontNest(result, -2764, -2868);
    
    _putDontNest(result, -2611, -2665);
    
    _putDontNest(result, -2582, -2646);
    
    _putDontNest(result, -2551, -2596);
    
    _putDontNest(result, -2520, -2561);
    
    _putDontNest(result, -2524, -2581);
    
    _putDontNest(result, -2288, -2651);
    
    _putDontNest(result, -60, -114);
    
    _putDontNest(result, -2488, -2541);
    
    _putDontNest(result, -2577, -2586);
    
    _putDontNest(result, -2602, -2626);
    
    _putDontNest(result, -2656, -2656);
    
    _putDontNest(result, -2428, -2566);
    
    _putDontNest(result, -2381, -2601);
    
    _putDontNest(result, -2546, -2611);
    
    _putDontNest(result, -2520, -2641);
    
    _putDontNest(result, -2288, -2571);
    
    _putDontNest(result, -2381, -2513);
    
    _putDontNest(result, -2561, -2586);
    
    _putDontNest(result, -2562, -2591);
    
    _putDontNest(result, -2783, -2863);
    
    _putDontNest(result, -2617, -2631);
    
    _putDontNest(result, -2612, -2636);
    
    _putDontNest(result, -2403, -2611);
    
    _putDontNest(result, -2547, -2576);
    
    _putDontNest(result, -2513, -2606);
    
    _putDontNest(result, -2519, -2596);
    
    _putDontNest(result, -2530, -2611);
    
    _putDontNest(result, -2556, -2581);
    
    _putDontNest(result, -2304, -2571);
    
    _putDontNest(result, -2501, -2516);
    
    _putDontNest(result, -2592, -2621);
    
    _putDontNest(result, -2566, -2571);
    
    _putDontNest(result, -2596, -2601);
    
    _putDontNest(result, -2381, -2636);
    
    _putDontNest(result, -2414, -2665);
    
    _putDontNest(result, -2304, -2591);
    
    _putDontNest(result, -2376, -2631);
    
    _putDontNest(result, -2510, -2636);
    
    _putDontNest(result, -2541, -2665);
    
    _putDontNest(result, -2557, -2586);
    
    _putDontNest(result, -2513, -2551);
    
    _putDontNest(result, -2516, -2546);
    
    _putDontNest(result, -614, -611);
    
    _putDontNest(result, -2607, -2656);
    
    _putDontNest(result, -2576, -2621);
    
    _putDontNest(result, -2288, -2591);
    
    _putDontNest(result, -2392, -2631);
    
    _putDontNest(result, -2557, -2665);
    
    _putDontNest(result, -2520, -2626);
    
    _putDontNest(result, -2519, -2631);
    
    _putDontNest(result, -2524, -2646);
    
    _putDontNest(result, -2542, -2591);
    
    _putDontNest(result, -2541, -2586);
    
    _putDontNest(result, -2414, -2586);
    
    _putDontNest(result, -2510, -2556);
    
    _putDontNest(result, -2381, -2556);
    
    _putDontNest(result, -2571, -2596);
    
    _putDontNest(result, -2582, -2651);
    
    _putDontNest(result, -2591, -2656);
    
    _putDontNest(result, -2567, -2616);
    
    _putDontNest(result, -2878, -2878);
    
    _putDontNest(result, -2542, -2636);
    
    _putDontNest(result, -2535, -2631);
    
    _putDontNest(result, -2525, -2586);
    
    _putDontNest(result, -2288, -2636);
    
    _putDontNest(result, -2291, -2551);
    
    _putDontNest(result, -2304, -2556);
    
    _putDontNest(result, -416, -430);
    
    _putDontNest(result, -2376, -2541);
    
    _putDontNest(result, -2606, -2611);
    
    _putDontNest(result, -2572, -2641);
    
    _putDontNest(result, -2566, -2651);
    
    _putDontNest(result, -2587, -2596);
    
    _putDontNest(result, -2525, -2665);
    
    _putDontNest(result, -2552, -2626);
    
    _putDontNest(result, -2556, -2646);
    
    _putDontNest(result, -2510, -2591);
    
    _putDontNest(result, -2304, -2636);
    
    _putDontNest(result, -2381, -2591);
    
    _putDontNest(result, -2291, -2503);
    
    _putDontNest(result, -2288, -2556);
    
    _putDontNest(result, -2529, -2551);
    
    _putDontNest(result, -2542, -2556);
    
    _putDontNest(result, -2519, -2541);
    
    _putDontNest(result, -2392, -2541);
    
    _putDontNest(result, -2403, -2546);
    
    _putDontNest(result, -2783, -2878);
    
    _putDontNest(result, -2596, -2665);
    
    _putDontNest(result, -2414, -2601);
    
    _putDontNest(result, -2403, -2606);
    
    _putDontNest(result, -2428, -2651);
    
    _putDontNest(result, -2546, -2656);
    
    _putDontNest(result, -2541, -2601);
    
    _putDontNest(result, -2524, -2566);
    
    _putDontNest(result, -2501, -2561);
    
    _putDontNest(result, -2534, -2596);
    
    _putDontNest(result, -2513, -2621);
    
    _putDontNest(result, -2291, -2519);
    
    _putDontNest(result, -2304, -2524);
    
    _putDontNest(result, -2761, -2868);
    
    _putDontNest(result, -2581, -2646);
    
    _putDontNest(result, -2612, -2665);
    
    _putDontNest(result, -2557, -2601);
    
    _putDontNest(result, -2530, -2656);
    
    _putDontNest(result, -2288, -2524);
    
    _putDontNest(result, -2804, -2873);
    
    _putDontNest(result, -2602, -2631);
    
    _putDontNest(result, -2646, -2651);
    
    _putDontNest(result, -2597, -2646);
    
    _putDontNest(result, -2556, -2566);
    
    _putDontNest(result, -2547, -2611);
    
    _putDontNest(result, -2530, -2576);
    
    _putDontNest(result, -2291, -2621);
    
    _putDontNest(result, -2788, -2873);
    
    _putDontNest(result, -2636, -2641);
    
    _putDontNest(result, -2617, -2626);
    
    _putDontNest(result, -2428, -2571);
    
    _putDontNest(result, -2525, -2601);
    
    _putDontNest(result, -2529, -2621);
    
    _putDontNest(result, -2546, -2576);
    
    _putDontNest(result, -2488, -2626);
    
    _putDontNest(result, -2516, -2606);
    
    _putDontNest(result, -2501, -2641);
    
    _putDontNest(result, -2488, -2510);
    
    _putDontNest(result, -2510, -2524);
    
    _putDontNest(result, -2381, -2524);
    
    _putDontNest(result, -2319, -2534);
    
    _putDontNest(result, -2851, -2858);
    
    _putDontNest(result, -2626, -2636);
    
    _putDontNest(result, -2657, -2665);
    
    _putDontNest(result, -2841, -2868);
    
    _putDontNest(result, -2602, -2596);
    
    _putDontNest(result, -2617, -2641);
    
    _putDontNest(result, -2591, -2611);
    
    _putDontNest(result, -2636, -2626);
    
    _putDontNest(result, -2403, -2665);
    
    _putDontNest(result, -2542, -2665);
    
    _putDontNest(result, -2288, -2665);
    
    _putDontNest(result, -2513, -2546);
    
    _putDontNest(result, -2501, -2510);
    
    _putDontNest(result, -2516, -2551);
    
    _putDontNest(result, -2534, -2541);
    
    _putDontNest(result, -2403, -2529);
    
    _putDontNest(result, -2376, -2510);
    
    _putDontNest(result, -2606, -2656);
    
    _putDontNest(result, -624, -614);
    
    _putDontNest(result, -614, -624);
    
    _putDontNest(result, -2601, -2641);
    
    _putDontNest(result, -2581, -2581);
    
    _putDontNest(result, -2319, -2621);
    
    _putDontNest(result, -2525, -2636);
    
    _putDontNest(result, -2519, -2626);
    
    _putDontNest(result, -2520, -2631);
    
    _putDontNest(result, -2414, -2591);
    
    _putDontNest(result, -2304, -2665);
    
    _putDontNest(result, -2541, -2534);
    
    _putDontNest(result, -2546, -2529);
    
    _putDontNest(result, -2392, -2510);
    
    _putDontNest(result, -2566, -2616);
    
    _putDontNest(result, -2621, -2621);
    
    _putDontNest(result, -2576, -2606);
    
    _putDontNest(result, -2873, -2868);
    
    _putDontNest(result, -2561, -2601);
    
    _putDontNest(result, -2868, -2873);
    
    _putDontNest(result, -2510, -2665);
    
    _putDontNest(result, -2535, -2626);
    
    _putDontNest(result, -415, -420);
    
    _putDontNest(result, -2641, -2665);
    
    _putDontNest(result, -2577, -2601);
    
    _putDontNest(result, -2582, -2616);
    
    _putDontNest(result, -2586, -2596);
    
    _putDontNest(result, -2592, -2606);
    
    _putDontNest(result, -2863, -2878);
    
    _putDontNest(result, -2607, -2611);
    
    _putDontNest(result, -2567, -2651);
    
    _putDontNest(result, -2557, -2636);
    
    _putDontNest(result, -2552, -2631);
    
    _putDontNest(result, -2381, -2586);
    
    _putDontNest(result, -2291, -2510);
    
    _putDontNest(result, -2529, -2546);
    
    _putDontNest(result, -2414, -2556);
    
    _putDontNest(result, -2788, -2858);
    
    _putDontNest(result, -2562, -2636);
    
    _putDontNest(result, -2572, -2626);
    
    _putDontNest(result, -521, -714);
    
    _putDontNest(result, -2414, -2606);
    
    _putDontNest(result, -2403, -2601);
    
    _putDontNest(result, -2516, -2611);
    
    _putDontNest(result, -2542, -2601);
    
    _putDontNest(result, -2556, -2651);
    
    _putDontNest(result, -2291, -2656);
    
    _putDontNest(result, -2288, -2601);
    
    _putDontNest(result, -2804, -2858);
    
    _putDontNest(result, -2596, -2591);
    
    _putDontNest(result, -2587, -2631);
    
    _putDontNest(result, -2428, -2596);
    
    _putDontNest(result, -2381, -2611);
    
    _putDontNest(result, -2529, -2656);
    
    _putDontNest(result, -2513, -2576);
    
    _putDontNest(result, -2524, -2571);
    
    _putDontNest(result, -2534, -2641);
    
    _putDontNest(result, -2547, -2606);
    
    _putDontNest(result, -2304, -2601);
    
    _putDontNest(result, -2414, -2524);
    
    _putDontNest(result, -2403, -2513);
    
    _putDontNest(result, -2561, -2665);
    
    _putDontNest(result, -2376, -2616);
    
    _putDontNest(result, -2510, -2601);
    
    _putDontNest(result, -2534, -2561);
    
    _putDontNest(result, -2546, -2621);
    
    _putDontNest(result, -2529, -2576);
    
    _putDontNest(result, -2513, -2656);
    
    _putDontNest(result, -2524, -2651);
    
    _putDontNest(result, -2501, -2596);
    
    _putDontNest(result, -2577, -2665);
    
    _putDontNest(result, -2616, -2646);
    
    _putDontNest(result, -2631, -2651);
    
    _putDontNest(result, -2392, -2616);
    
    _putDontNest(result, -2488, -2631);
    
    _putDontNest(result, -2556, -2571);
    
    _putDontNest(result, -2530, -2621);
    
    _putDontNest(result, -2291, -2576);
    
    _putDontNest(result, -39, -121);
    
    _putDontNest(result, -2592, -2611);
    
    _putDontNest(result, -2621, -2656);
    
    _putDontNest(result, -2601, -2596);
    
    _putDontNest(result, -2566, -2581);
    
    _putDontNest(result, -2616, -2651);
    
    _putDontNest(result, -2597, -2616);
    
    _putDontNest(result, -2636, -2631);
    
    _putDontNest(result, -624, -619);
    
    _putDontNest(result, -2631, -2646);
    
    _putDontNest(result, -2319, -2576);
    
    _putDontNest(result, -2529, -2529);
    
    _putDontNest(result, -2488, -2516);
    
    _putDontNest(result, -2582, -2581);
    
    _putDontNest(result, -2602, -2641);
    
    _putDontNest(result, -2576, -2611);
    
    _putDontNest(result, -2547, -2665);
    
    _putDontNest(result, -2516, -2636);
    
    _putDontNest(result, -2488, -2616);
    
    _putDontNest(result, -2291, -2541);
    
    _putDontNest(result, -430, -430);
    
    _putDontNest(result, -2562, -2601);
    
    _putDontNest(result, -2586, -2641);
    
    _putDontNest(result, -2626, -2665);
    
    _putDontNest(result, -2403, -2636);
    
    _putDontNest(result, -2534, -2646);
    
    _putDontNest(result, -2546, -2546);
    
    _putDontNest(result, -2501, -2541);
    
    _putDontNest(result, -60, -136);
    
    _putDontNest(result, -2858, -2868);
    
    _putDontNest(result, -2642, -2665);
    
    _putDontNest(result, -2851, -2873);
    
    _putDontNest(result, -2591, -2606);
    
    _putDontNest(result, -2581, -2616);
    
    _putDontNest(result, -2606, -2621);
    
    _putDontNest(result, -2864, -2878);
    
    _putDontNest(result, -2319, -2656);
    
    _putDontNest(result, -2510, -2534);
    
    _putDontNest(result, -415, -435);
    
    _putDontNest(result, -2513, -2529);
    
    _putDontNest(result, -2381, -2534);
    
    _putDontNest(result, -2319, -2524);
    
    _putDontNest(result, -2783, -2868);
    
    _putDontNest(result, -2572, -2631);
    
    _putDontNest(result, -2567, -2646);
    
    _putDontNest(result, -2428, -2641);
    
    _putDontNest(result, -2376, -2581);
    
    _putDontNest(result, -2501, -2571);
    
    _putDontNest(result, -2542, -2606);
    
    _putDontNest(result, -2525, -2611);
    
    _putDontNest(result, -2761, -2878);
    
    _putDontNest(result, -2597, -2591);
    
    _putDontNest(result, -2587, -2626);
    
    _putDontNest(result, -2577, -2636);
    
    _putDontNest(result, -2392, -2581);
    
    _putDontNest(result, -2556, -2596);
    
    _putDontNest(result, -2552, -2616);
    
    _putDontNest(result, -2519, -2581);
    
    _putDontNest(result, -2547, -2601);
    
    _putDontNest(result, -2319, -2556);
    
    _putDontNest(result, -2581, -2591);
    
    _putDontNest(result, -2582, -2586);
    
    _putDontNest(result, -2764, -2863);
    
    _putDontNest(result, -2562, -2665);
    
    _putDontNest(result, -556, -714);
    
    _putDontNest(result, -2381, -2606);
    
    _putDontNest(result, -2510, -2606);
    
    _putDontNest(result, -2535, -2581);
    
    _putDontNest(result, -2557, -2611);
    
    _putDontNest(result, -2291, -2611);
    
    _putDontNest(result, -2566, -2586);
    
    _putDontNest(result, -2637, -2656);
    
    _putDontNest(result, -2414, -2611);
    
    _putDontNest(result, -2428, -2561);
    
    _putDontNest(result, -2501, -2651);
    
    _putDontNest(result, -2520, -2616);
    
    _putDontNest(result, -2524, -2596);
    
    _putDontNest(result, -2551, -2581);
    
    _putDontNest(result, -2541, -2611);
    
    _putDontNest(result, -2534, -2566);
    
    _putDontNest(result, -2319, -2591);
    
    _putDontNest(result, -2602, -2606);
    
    _putDontNest(result, -2561, -2571);
    
    _putDontNest(result, -2841, -2878);
    
    _putDontNest(result, -2587, -2621);
    
    _putDontNest(result, -2596, -2616);
    
    _putDontNest(result, -2626, -2646);
    
    _putDontNest(result, -2291, -2591);
    
    _putDontNest(result, -2376, -2646);
    
    _putDontNest(result, -2501, -2646);
    
    _putDontNest(result, -2546, -2586);
    
    _putDontNest(result, -2488, -2513);
    
    _putDontNest(result, -2519, -2546);
    
    _putDontNest(result, -2571, -2621);
    
    _putDontNest(result, -2607, -2641);
    
    _putDontNest(result, -2612, -2616);
    
    _putDontNest(result, -2642, -2646);
    
    _putDontNest(result, -2392, -2646);
    
    _putDontNest(result, -2319, -2611);
    
    _putDontNest(result, -2529, -2591);
    
    _putDontNest(result, -2530, -2586);
    
    _putDontNest(result, -2414, -2534);
    
    _putDontNest(result, -2631, -2665);
    
    _putDontNest(result, -2591, -2641);
    
    _putDontNest(result, -2577, -2651);
    
    _putDontNest(result, -2873, -2878);
    
    _putDontNest(result, -2576, -2596);
    
    _putDontNest(result, -2567, -2601);
    
    _putDontNest(result, -619, -714);
    
    _putDontNest(result, -2513, -2591);
    
    _putDontNest(result, -60, -133);
    
    _putDontNest(result, -2403, -2503);
    
    _putDontNest(result, -2561, -2651);
    
    _putDontNest(result, -2586, -2606);
    
    _putDontNest(result, -2647, -2665);
    
    _putDontNest(result, -2572, -2656);
    
    _putDontNest(result, -2601, -2611);
    
    _putDontNest(result, -2863, -2868);
    
    _putDontNest(result, -2592, -2596);
    
    _putDontNest(result, -614, -717);
    
    _putDontNest(result, -2428, -2626);
    
    _putDontNest(result, -2488, -2581);
    
    _putDontNest(result, -2516, -2665);
    
    _putDontNest(result, -2547, -2636);
    
    _putDontNest(result, -2534, -2551);
    
    _putDontNest(result, -2520, -2529);
    
    _putDontNest(result, -2516, -2541);
    
    _putDontNest(result, -2403, -2551);
    
    _putDontNest(result, -2562, -2646);
    
    _putDontNest(result, -2392, -2566);
    
    _putDontNest(result, -2535, -2616);
    
    _putDontNest(result, -2556, -2641);
    
    _putDontNest(result, -2541, -2606);
    
    _putDontNest(result, -2291, -2516);
    
    _putDontNest(result, -2376, -2566);
    
    _putDontNest(result, -2381, -2621);
    
    _putDontNest(result, -2414, -2656);
    
    _putDontNest(result, -2557, -2606);
    
    _putDontNest(result, -2501, -2566);
    
    _putDontNest(result, -2534, -2651);
    
    _putDontNest(result, -2524, -2561);
    
    _putDontNest(result, -2510, -2611);
    
    _putDontNest(result, -2551, -2616);
    
    _putDontNest(result, -2520, -2581);
    
    _putDontNest(result, -2291, -2606);
    
    _putDontNest(result, -2488, -2529);
    
    _putDontNest(result, -2403, -2519);
    
    _putDontNest(result, -2581, -2586);
    
    _putDontNest(result, -2582, -2591);
    
    _putDontNest(result, -2641, -2651);
    
    _putDontNest(result, -2567, -2665);
    
    _putDontNest(result, -2414, -2576);
    
    _putDontNest(result, -2534, -2571);
    
    _putDontNest(result, -2524, -2641);
    
    _putDontNest(result, -2392, -2516);
    
    _putDontNest(result, -2566, -2591);
    
    _putDontNest(result, -2636, -2656);
    
    _putDontNest(result, -2525, -2606);
    
    _putDontNest(result, -2542, -2611);
    
    _putDontNest(result, -2556, -2561);
    
    _putDontNest(result, -2519, -2616);
    
    _putDontNest(result, -2552, -2581);
    
    _putDontNest(result, -2516, -2601);
    
    _putDontNest(result, -2319, -2586);
    
    _putDontNest(result, -2376, -2516);
    
    _putDontNest(result, -2601, -2606);
    
    _putDontNest(result, -2586, -2611);
    
    _putDontNest(result, -2854, -2858);
    
    _putDontNest(result, -2859, -2863);
    
    _putDontNest(result, -2631, -2636);
    
    _putDontNest(result, -2571, -2576);
    
    _putDontNest(result, -2291, -2586);
    
    _putDontNest(result, -2529, -2665);
    
    _putDontNest(result, -2546, -2591);
    
    _putDontNest(result, -2520, -2546);
    
    _putDontNest(result, -2572, -2621);
    
    _putDontNest(result, -708, -717);
    
    _putDontNest(result, -2561, -2566);
    
    _putDontNest(result, -611, -624);
    
    _putDontNest(result, -2611, -2616);
    
    _putDontNest(result, -2641, -2646);
    
    _putDontNest(result, -2520, -2646);
    
    _putDontNest(result, -2529, -2586);
    
    _putDontNest(result, -2530, -2591);
    
    _putDontNest(result, -2291, -2665);
    
    _putDontNest(result, -2501, -2551);
    
    _putDontNest(result, -2587, -2656);
    
    _putDontNest(result, -2592, -2641);
    
    _putDontNest(result, -2319, -2606);
    
    _putDontNest(result, -2488, -2566);
    
    _putDontNest(result, -2530, -2636);
    
    _putDontNest(result, -2513, -2586);
    
    _putDontNest(result, -2524, -2534);
    
    _putDontNest(result, -2403, -2510);
    
    _putDontNest(result, -2376, -2529);
    
    _putDontNest(result, -61, -133);
    
    _putDontNest(result, -2858, -2878);
    
    _putDontNest(result, -2591, -2596);
    
    _putDontNest(result, -2562, -2651);
    
    _putDontNest(result, -2602, -2611);
    
    _putDontNest(result, -2576, -2641);
    
    _putDontNest(result, -2571, -2656);
    
    _putDontNest(result, -638, -714);
    
    _putDontNest(result, -611, -717);
    
    _putDontNest(result, -2428, -2631);
    
    _putDontNest(result, -2552, -2646);
    
    _putDontNest(result, -2513, -2665);
    
    _putDontNest(result, -2519, -2529);
    
    _putDontNest(result, -45, -133);
    
    _putDontNest(result, -2392, -2529);
    
    _putDontNest(result, -2567, -2636);
    
    _putDontNest(result, -2561, -2646);
    
    _putDontNest(result, -653, -714);
    
    _putDontNest(result, -2376, -2571);
    
    _putDontNest(result, -2381, -2576);
    
    _putDontNest(result, -2501, -2581);
    
    _putDontNest(result, -2520, -2566);
    
    _putDontNest(result, -2534, -2616);
    
    _putDontNest(result, -2551, -2651);
    
    _putDontNest(result, -2529, -2601);
    
    _putDontNest(result, -2510, -2576);
    
    _putDontNest(result, -2525, -2621);
    
    _putDontNest(result, -2304, -2656);
    
    _putDontNest(result, -2577, -2646);
    
    _putDontNest(result, -2616, -2665);
    
    _putDontNest(result, -2392, -2571);
    
    _putDontNest(result, -2542, -2656);
    
    _putDontNest(result, -2535, -2651);
    
    _putDontNest(result, -2519, -2571);
    
    _putDontNest(result, -2291, -2601);
    
    _putDontNest(result, -2288, -2656);
    
    _putDontNest(result, -2642, -2651);
    
    _putDontNest(result, -2651, -2656);
    
    _putDontNest(result, -2392, -2651);
    
    _putDontNest(result, -2519, -2651);
    
    _putDontNest(result, -2542, -2576);
    
    _putDontNest(result, -2535, -2571);
    
    _putDontNest(result, -2557, -2621);
    
    _putDontNest(result, -2288, -2576);
    
    _putDontNest(result, -2488, -2546);
    
    _putDontNest(result, -2392, -2513);
    
    _putDontNest(result, -2567, -2591);
    
    _putDontNest(result, -2626, -2651);
    
    _putDontNest(result, -2376, -2651);
    
    _putDontNest(result, -2381, -2656);
    
    _putDontNest(result, -2414, -2621);
    
    _putDontNest(result, -2488, -2646);
    
    _putDontNest(result, -2510, -2656);
    
    _putDontNest(result, -2513, -2601);
    
    _putDontNest(result, -2541, -2621);
    
    _putDontNest(result, -2551, -2571);
    
    _putDontNest(result, -2304, -2576);
    
    _putDontNest(result, -2501, -2519);
    
    _putDontNest(result, -2376, -2513);
    
    _putDontNest(result, -2587, -2611);
    
    _putDontNest(result, -2606, -2596);
    
    _putDontNest(result, -2597, -2601);
    
    _putDontNest(result, -2621, -2641);
    
    _putDontNest(result, -2561, -2581);
    
    _putDontNest(result, -619, -619);
    
    _putDontNest(result, -2611, -2651);
    
    _putDontNest(result, -2319, -2561);
    
    _putDontNest(result, -2376, -2636);
    
    _putDontNest(result, -2381, -2631);
    
    _putDontNest(result, -2530, -2665);
    
    _putDontNest(result, -2488, -2651);
    
    _putDontNest(result, -2547, -2591);
    
    _putDontNest(result, -2288, -2541);
    
    _putDontNest(result, -2534, -2529);
    
    _putDontNest(result, -2520, -2551);
    
    _putDontNest(result, -2488, -2519);
    
    _putDontNest(result, -2428, -2534);
    
    _putDontNest(result, -2403, -2541);
    
    _putDontNest(result, -2392, -2546);
    
    _putDontNest(result, -611, -611);
    
    _putDontNest(result, -2577, -2581);
    
    _putDontNest(result, -2571, -2611);
    
    _putDontNest(result, -2602, -2656);
    
    _putDontNest(result, -2392, -2636);
    
    _putDontNest(result, -2513, -2636);
    
    _putDontNest(result, -2546, -2665);
    
    _putDontNest(result, -2519, -2646);
    
    _putDontNest(result, -2304, -2541);
    
    _putDontNest(result, -2546, -2541);
    
    _putDontNest(result, -2501, -2546);
    
    _putDontNest(result, -425, -430);
    
    _putDontNest(result, -2376, -2546);
    
    _putDontNest(result, -2617, -2621);
    
    _putDontNest(result, -2586, -2656);
    
    _putDontNest(result, -2562, -2616);
    
    _putDontNest(result, -2572, -2606);
    
    _putDontNest(result, -2414, -2626);
    
    _putDontNest(result, -2535, -2646);
    
    _putDontNest(result, -2516, -2586);
    
    _putDontNest(result, -61, -136);
    
    _putDontNest(result, -2859, -2878);
    
    _putDontNest(result, -2601, -2621);
    
    _putDontNest(result, -2581, -2601);
    
    _putDontNest(result, -2319, -2641);
    
    _putDontNest(result, -2488, -2571);
    
    _putDontNest(result, -2551, -2646);
    
    _putDontNest(result, -2291, -2636);
    
    _putDontNest(result, -45, -136);
    
    _putDontNest(result, -2597, -2665);
    
    _putDontNest(result, -2428, -2656);
    
    _putDontNest(result, -2552, -2651);
    
    _putDontNest(result, -2519, -2566);
    
    _putDontNest(result, -2530, -2601);
    
    _putDontNest(result, -2557, -2656);
    
    _putDontNest(result, -2764, -2878);
    
    _putDontNest(result, -2592, -2626);
    
    _putDontNest(result, -2582, -2636);
    
    _putDontNest(result, -2546, -2601);
    
    _putDontNest(result, -2541, -2656);
    
    _putDontNest(result, -2510, -2621);
    
    _putDontNest(result, -2525, -2576);
    
    _putDontNest(result, -2520, -2571);
    
    _putDontNest(result, -2761, -2863);
    
    _putDontNest(result, -2596, -2646);
    
    _putDontNest(result, -2607, -2631);
    
    _putDontNest(result, -2403, -2581);
    
    _putDontNest(result, -2534, -2581);
    
    _putDontNest(result, -2501, -2616);
    
    _putDontNest(result, -2551, -2566);
    
    _putDontNest(result, -2520, -2651);
    
    _putDontNest(result, -2541, -2576);
    
    _putDontNest(result, -2525, -2656);
    
    _putDontNest(result, -2304, -2621);
    
    _putDontNest(result, -2488, -2551);
    
    _putDontNest(result, -2567, -2586);
    
    _putDontNest(result, -2612, -2646);
    
    _putDontNest(result, -2581, -2665);
    
    _putDontNest(result, -2637, -2641);
    
    _putDontNest(result, -2428, -2576);
    
    _putDontNest(result, -2542, -2621);
    
    _putDontNest(result, -2535, -2566);
    
    _putDontNest(result, -2557, -2576);
    
    _putDontNest(result, -2288, -2621);
    
    _putDontNest(result, -2488, -2503);
    
    _putDontNest(result, -2612, -2651);
    
    _putDontNest(result, -2562, -2581);
    
    _putDontNest(result, -2586, -2621);
    
    _putDontNest(result, -2617, -2656);
    
    _putDontNest(result, -2381, -2626);
    
    _putDontNest(result, -2510, -2626);
    
    _putDontNest(result, -2535, -2665);
    
    _putDontNest(result, -2547, -2586);
    
    _putDontNest(result, -2529, -2541);
    
    _putDontNest(result, -167, -176);
    
    _putDontNest(result, -2519, -2551);
    
    _putDontNest(result, -2524, -2556);
    
    _putDontNest(result, -2392, -2551);
    
    _putDontNest(result, -2376, -2503);
    
    _putDontNest(result, -2606, -2641);
    
    _putDontNest(result, -2601, -2656);
    
    _putDontNest(result, -2596, -2651);
    
    _putDontNest(result, -619, -614);
    
    _putDontNest(result, -2572, -2611);
    
    _putDontNest(result, -608, -714);
    
    _putDontNest(result, -2520, -2636);
    
    _putDontNest(result, -2551, -2665);
    
    _putDontNest(result, -2525, -2631);
    
    _putDontNest(result, -2403, -2591);
    
    _putDontNest(result, -2376, -2551);
    
    _putDontNest(result, -2392, -2503);
    
    _putDontNest(result, -2566, -2601);
    
    _putDontNest(result, -2561, -2616);
    
    _putDontNest(result, -2571, -2606);
    
    _putDontNest(result, -624, -714);
    
    _putDontNest(result, -2319, -2596);
    
    _putDontNest(result, -2414, -2631);
    
    _putDontNest(result, -2376, -2665);
    
    _putDontNest(result, -2542, -2626);
    
    _putDontNest(result, -2530, -2646);
    
    _putDontNest(result, -2516, -2591);
    
    _putDontNest(result, -2288, -2510);
    
    _putDontNest(result, -2551, -2551);
    
    _putDontNest(result, -2556, -2556);
    
    _putDontNest(result, -2501, -2529);
    
    _putDontNest(result, -2646, -2665);
    
    _putDontNest(result, -2577, -2616);
    
    _putDontNest(result, -2602, -2621);
    
    _putDontNest(result, -2582, -2601);
    
    _putDontNest(result, -2587, -2606);
    
    _putDontNest(result, -2841, -2863);
    
    _putDontNest(result, -2392, -2665);
    
    _putDontNest(result, -2552, -2636);
    
    _putDontNest(result, -2546, -2646);
    
    _putDontNest(result, -2519, -2665);
    
    _putDontNest(result, -2557, -2631);
    
    _putDontNest(result, -2304, -2510);
    
    _putDontNest(result, -2535, -2551);
    
    _putDontNest(result, -2534, -2546);
    
    _putDontNest(result, -2513, -2541);
    
    _putDontNest(result, -2403, -2616);
    
    _putDontNest(result, -2535, -2601);
    
    _putDontNest(result, -2556, -2656);
    
    _putDontNest(result, -2288, -2606);
    
    _putDontNest(result, -45, -114);
    
    _putDontNest(result, -2592, -2631);
    
    _putDontNest(result, -2551, -2601);
    
    _putDontNest(result, -2529, -2651);
    
    _putDontNest(result, -2524, -2576);
    
    _putDontNest(result, -2513, -2571);
    
    _putDontNest(result, -2304, -2606);
    
    _putDontNest(result, -61, -114);
    
    _putDontNest(result, -2403, -2516);
    
    _putDontNest(result, -2601, -2591);
    
    _putDontNest(result, -2566, -2665);
    
    _putDontNest(result, -2597, -2636);
    
    _putDontNest(result, -2607, -2626);
    
    _putDontNest(result, -2376, -2601);
    
    _putDontNest(result, -2428, -2621);
    
    _putDontNest(result, -2513, -2651);
    
    _putDontNest(result, -2529, -2571);
    
    _putDontNest(result, -2546, -2566);
    
    _putDontNest(result, -2524, -2656);
    
    _putDontNest(result, -2524, -2524);
    
    _putDontNest(result, -2392, -2519);
    
    _putDontNest(result, -2761, -2858);
    
    _putDontNest(result, -2586, -2586);
    
    _putDontNest(result, -2611, -2646);
    
    _putDontNest(result, -2582, -2665);
    
    _putDontNest(result, -2392, -2601);
    
    _putDontNest(result, -2519, -2601);
    
    _putDontNest(result, -2516, -2616);
    
    _putDontNest(result, -2556, -2576);
    
    _putDontNest(result, -2530, -2566);
    
    _putDontNest(result, -2488, -2636);
    
    _putDontNest(result, -2547, -2581);
    
    _putDontNest(result, -2501, -2513);
    
    _putDontNest(result, -39, -128);
    
    _putDontNest(result, -2376, -2519);
    
    _putDontNest(result, -2510, -2631);
    
    _putDontNest(result, -2626, -2626);
    
    _putDontNest(result, -2596, -2596);
    
    _putDontNest(result, -2611, -2641);
    
    _putDontNest(result, -2621, -2651);
    
    _putDontNest(result, -2636, -2636);
    
    _putDontNest(result, -2581, -2611);
    
    _putDontNest(result, -2616, -2656);
    
    _putDontNest(result, -2488, -2601);
    
    _putDontNest(result, -2552, -2665);
    
    _putDontNest(result, -2513, -2646);
    
    _putDontNest(result, -2525, -2626);
    
    _putDontNest(result, -2519, -2636);
    
    _putDontNest(result, -2534, -2586);
    
    _putDontNest(result, -2403, -2586);
    
    _putDontNest(result, -2501, -2556);
    
    _putDontNest(result, -2567, -2621);
    
    _putDontNest(result, -614, -614);
    
    _putDontNest(result, -624, -624);
    
    _putDontNest(result, -2542, -2631);
    
    _putDontNest(result, -2535, -2636);
    
    _putDontNest(result, -2529, -2646);
    
    _putDontNest(result, -2519, -2534);
    
    _putDontNest(result, -2428, -2556);
    
    _putDontNest(result, -2566, -2606);
    
    _putDontNest(result, -2576, -2616);
    
    _putDontNest(result, -2571, -2601);
    
    _putDontNest(result, -2869, -2878);
    
    _putDontNest(result, -2520, -2665);
    
    _putDontNest(result, -2557, -2626);
    
    _putDontNest(result, -2501, -2591);
    
    _putDontNest(result, -2524, -2529);
    
    _putDontNest(result, -2597, -2611);
    
    _putDontNest(result, -2582, -2606);
    
    _putDontNest(result, -2851, -2868);
    
    _putDontNest(result, -2858, -2873);
    
    _putDontNest(result, -2592, -2616);
    
    _putDontNest(result, -2651, -2665);
    
    _putDontNest(result, -2587, -2601);
    
    _putDontNest(result, -2841, -2858);
    
    _putDontNest(result, -2381, -2561);
    
    _putDontNest(result, -2319, -2631);
    
    _putDontNest(result, -2414, -2596);
    
    _putDontNest(result, -2513, -2566);
    
    _putDontNest(result, -2546, -2651);
    
    _putDontNest(result, -2524, -2621);
    
    _putDontNest(result, -2428, -2524);
    
    _putDontNest(result, -2562, -2626);
    
    _putDontNest(result, -2572, -2636);
    
    _putDontNest(result, -2428, -2606);
    
    _putDontNest(result, -2403, -2651);
    
    _putDontNest(result, -2530, -2651);
    
    _putDontNest(result, -2547, -2616);
    
    _putDontNest(result, -2516, -2581);
    
    _putDontNest(result, -2488, -2665);
    
    _putDontNest(result, -2552, -2601);
    
    _putDontNest(result, -2761, -2873);
    
    _putDontNest(result, -2602, -2591);
    
    _putDontNest(result, -2577, -2631);
    
    _putDontNest(result, -2403, -2571);
    
    _putDontNest(result, -2530, -2571);
    
    _putDontNest(result, -2556, -2621);
    
    _putDontNest(result, -2304, -2611);
    
    _putDontNest(result, -61, -121);
    
    _putDontNest(result, -2804, -2868);
    
    _putDontNest(result, -2586, -2591);
    
    _putDontNest(result, -2606, -2646);
    
    _putDontNest(result, -2571, -2665);
    
    _putDontNest(result, -2381, -2641);
    
    _putDontNest(result, -2529, -2566);
    
    _putDontNest(result, -2546, -2571);
    
    _putDontNest(result, -2520, -2601);
    
    _putDontNest(result, -2288, -2611);
    
    _putDontNest(result, -2501, -2524);
    
    _putDontNest(result, -45, -121);
    
    _putDontNest(result, -2788, -2868);
    
    _putDontNest(result, -2637, -2651);
    
    _putDontNest(result, -2587, -2665);
    
    _putDontNest(result, -2524, -2546);
    
    _putDontNest(result, -2607, -2616);
    
    _putDontNest(result, -2567, -2576);
    
    _putDontNest(result, -2626, -2631);
    
    _putDontNest(result, -614, -619);
    
    _putDontNest(result, -2582, -2611);
    
    _putDontNest(result, -2576, -2581);
    
    _putDontNest(result, -2597, -2606);
    
    _putDontNest(result, -2637, -2646);
    
    _putDontNest(result, -2841, -2873);
    
    _putDontNest(result, -2612, -2641);
    
    _putDontNest(result, -2516, -2646);
    
    _putDontNest(result, -2534, -2591);
    
    _putDontNest(result, -2428, -2529);
    
    _putDontNest(result, -2596, -2641);
    
    _putDontNest(result, -2566, -2611);
    
    _putDontNest(result, -2606, -2651);
    
    _putDontNest(result, -2403, -2646);
    
    _putDontNest(result, -2501, -2665);
    
    _putDontNest(result, -2520, -2534);
    
    _putDontNest(result, -2868, -2868);
    
    _putDontNest(result, -2636, -2665);
    
    _putDontNest(result, -2572, -2601);
    
    _putDontNest(result, -2873, -2873);
    
    _putDontNest(result, -2616, -2621);
    
    _putDontNest(result, -2501, -2586);
    
    _putDontNest(result, -2534, -2556);
    
    _putDontNest(result, -2581, -2606);
    
    _putDontNest(result, -2854, -2878);
    
    _putDontNest(result, -2591, -2616);
    
    _putDontNest(result, -2567, -2656);
    
    _putDontNest(result, -2652, -2665);
    
    _putDontNest(result, -2319, -2626);
    
    _putDontNest(result, -2542, -2596);
    
    _putDontNest(result, -2547, -2651);
    
    _putDontNest(result, -2530, -2616);
    
    _putDontNest(result, -2557, -2641);
    
    _putDontNest(result, -2516, -2566);
    
    _putDontNest(result, -2288, -2596);
    
    _putDontNest(result, -2304, -2516);
    
    _putDontNest(result, -2488, -2534);
    
    _putDontNest(result, -2428, -2513);
    
    _putDontNest(result, -2319, -2510);
    
    _putDontNest(result, -2769, -2868);
    
    _putDontNest(result, -2562, -2631);
    
    _putDontNest(result, -2414, -2641);
    
    _putDontNest(result, -2513, -2581);
    
    _putDontNest(result, -2546, -2616);
    
    _putDontNest(result, -2525, -2561);
    
    _putDontNest(result, -2556, -2606);
    
    _putDontNest(result, -2541, -2641);
    
    _putDontNest(result, -2304, -2596);
    
    _putDontNest(result, -2288, -2516);
    
    _putDontNest(result, -2571, -2556);
    
    _putDontNest(result, -2764, -2873);
    
    _putDontNest(result, -2587, -2636);
    
    _putDontNest(result, -2577, -2626);
    
    _putDontNest(result, -2414, -2561);
    
    _putDontNest(result, -2381, -2596);
    
    _putDontNest(result, -2428, -2611);
    
    _putDontNest(result, -2510, -2596);
    
    _putDontNest(result, -2541, -2561);
    
    _putDontNest(result, -2529, -2581);
    
    _putDontNest(result, -2501, -2601);
    
    _putDontNest(result, -2525, -2641);
    
    _putDontNest(result, -2587, -2591);
    
    _putDontNest(result, -2572, -2665);
    
    _putDontNest(result, -2647, -2656);
    
    _putDontNest(result, -2403, -2566);
    
    _putDontNest(result, -2547, -2571);
    
    _putDontNest(result, -2524, -2606);
    
    _putDontNest(result, -2291, -2581);
    
    _putDontNest(result, -2381, -2516);
    
    _putDontNest(result, -2571, -2591);
    
    _putDontNest(result, -2572, -2586);
    
    _putDontNest(result, -2621, -2646);
    
    _putDontNest(result, -2631, -2656);
    
    _putDontNest(result, -2534, -2665);
    
    _putDontNest(result, -2501, -2636);
    
    _putDontNest(result, -2552, -2586);
    
    _putDontNest(result, -2551, -2591);
    
    _putDontNest(result, -2291, -2534);
    
    _putDontNest(result, -2288, -2529);
    
    _putDontNest(result, -2519, -2556);
    
    _putDontNest(result, -2524, -2551);
    
    _putDontNest(result, -2601, -2601);
    
    _putDontNest(result, -2859, -2858);
    
    _putDontNest(result, -2606, -2616);
    
    _putDontNest(result, -2581, -2621);
    
    _putDontNest(result, -708, -714);
    
    _putDontNest(result, -2561, -2561);
    
    _putDontNest(result, -2566, -2576);
    
    _putDontNest(result, -2854, -2863);
    
    _putDontNest(result, -608, -717);
    
    _putDontNest(result, -2319, -2581);
    
    _putDontNest(result, -2535, -2591);
    
    _putDontNest(result, -2304, -2529);
    
    _putDontNest(result, -421, -430);
    
    _putDontNest(result, -2607, -2651);
    
    _putDontNest(result, -2567, -2611);
    
    _putDontNest(result, -2520, -2586);
    
    _putDontNest(result, -2519, -2591);
    
    _putDontNest(result, -2551, -2556);
    
    _putDontNest(result, -2510, -2541);
    
    _putDontNest(result, -2392, -2534);
    
    _putDontNest(result, -2428, -2546);
    
    _putDontNest(result, -2562, -2596);
    
    _putDontNest(result, -2591, -2651);
    
    _putDontNest(result, -2577, -2641);
    
    _putDontNest(result, -2582, -2656);
    
    _putDontNest(result, -2414, -2646);
    
    _putDontNest(result, -2547, -2646);
    
    _putDontNest(result, -2501, -2534);
    
    _putDontNest(result, -2535, -2556);
    
    _putDontNest(result, -2376, -2534);
    
    _putDontNest(result, -2597, -2621);
    
    _putDontNest(result, -2566, -2656);
    
    _putDontNest(result, -2561, -2641);
    
    _putDontNest(result, -624, -717);
    
    _putDontNest(result, -2428, -2636);
    
    _putDontNest(result, -2541, -2596);
    
    _putDontNest(result, -2510, -2561);
    
    _putDontNest(result, -2529, -2616);
    
    _putDontNest(result, -2534, -2601);
    
    _putDontNest(result, -2524, -2611);
    
    _putDontNest(result, -2488, -2586);
    
    _putDontNest(result, -2304, -2641);
    
    _putDontNest(result, -2304, -2513);
    
    _putDontNest(result, -2576, -2646);
    
    _putDontNest(result, -2601, -2665);
    
    _putDontNest(result, -2376, -2576);
    
    _putDontNest(result, -2381, -2571);
    
    _putDontNest(result, -2542, -2641);
    
    _putDontNest(result, -2516, -2571);
    
    _putDontNest(result, -2557, -2596);
    
    _putDontNest(result, -2288, -2641);
    
    _putDontNest(result, -2291, -2616);
    
    _putDontNest(result, -2288, -2513);
    
    _putDontNest(result, -2414, -2516);
    
    _putDontNest(result, -2592, -2646);
    
    _putDontNest(result, -653, -717);
    
    _putDontNest(result, -2617, -2665);
    
    _putDontNest(result, -2403, -2641);
    
    _putDontNest(result, -2392, -2576);
    
    _putDontNest(result, -2547, -2566);
    
    _putDontNest(result, -2530, -2581);
    
    _putDontNest(result, -2542, -2561);
    
    _putDontNest(result, -2516, -2651);
    
    _putDontNest(result, -2556, -2611);
    
    _putDontNest(result, -2288, -2561);
    
    _putDontNest(result, -60, -128);
    
    _putDontNest(result, -2519, -2524);
    
    _putDontNest(result, -2319, -2541);
    
    _putDontNest(result, -2764, -2858);
    
    _putDontNest(result, -2602, -2636);
    
    _putDontNest(result, -2646, -2656);
    
    _putDontNest(result, -2641, -2641);
    
    _putDontNest(result, -2392, -2656);
    
    _putDontNest(result, -2403, -2561);
    
    _putDontNest(result, -2510, -2641);
    
    _putDontNest(result, -2513, -2616);
    
    _putDontNest(result, -2546, -2581);
    
    _putDontNest(result, -2525, -2596);
    
    _putDontNest(result, -2304, -2561);
    
    _putDontNest(result, -2572, -2591);
    
    _putDontNest(result, -2571, -2586);
    
    _putDontNest(result, -2612, -2626);
    
    _putDontNest(result, -2376, -2656);
    
    _putDontNest(result, -2381, -2651);
    
    _putDontNest(result, -2414, -2566);
    
    _putDontNest(result, -2551, -2586);
    
    _putDontNest(result, -2552, -2591);
    
    _putDontNest(result, -171, -176);
    
    _putDontNest(result, -2541, -2541);
    
    _putDontNest(result, -2520, -2556);
    
    _putDontNest(result, -2488, -2524);
    
    _putDontNest(result, -425, -425);
    
    _putDontNest(result, -2414, -2541);
    
    _putDontNest(result, -2381, -2510);
    
    _putDontNest(result, -2602, -2601);
    
    _putDontNest(result, -2582, -2621);
    
    _putDontNest(result, -2381, -2646);
    
    _putDontNest(result, -2510, -2646);
    
    _putDontNest(result, -2488, -2656);
    
    _putDontNest(result, -2535, -2586);
    
    _putDontNest(result, -2534, -2534);
    
    _putDontNest(result, -2597, -2656);
    
    _putDontNest(result, -624, -611);
    
    _putDontNest(result, -2566, -2621);
    
    _putDontNest(result, -2428, -2665);
    
    _putDontNest(result, -2513, -2631);
    
    _putDontNest(result, -2519, -2586);
    
    _putDontNest(result, -2520, -2591);
    
    _putDontNest(result, -2392, -2586);
    
    _putDontNest(result, -2288, -2626);
    
    _putDontNest(result, -2304, -2546);
    
    _putDontNest(result, -2428, -2551);
    
    _putDontNest(result, -2592, -2651);
    
    _putDontNest(result, -2567, -2606);
    
    _putDontNest(result, -2581, -2656);
    
    _putDontNest(result, -2616, -2611);
    
    _putDontNest(result, -2561, -2596);
    
    _putDontNest(result, -2319, -2616);
    
    _putDontNest(result, -2530, -2626);
    
    _putDontNest(result, -2542, -2646);
    
    _putDontNest(result, -2304, -2626);
    
    _putDontNest(result, -2376, -2586);
    
    _putDontNest(result, -2288, -2546);
    
    _putDontNest(result, -2319, -2516);
    
    _putDontNest(result, -2428, -2503);
    
    _putDontNest(result, -2586, -2601);
    
    _putDontNest(result, -2577, -2596);
    
    _putDontNest(result, -2576, -2651);
    
    _putDontNest(result, -2562, -2641);
    
    _putDontNest(result, -2488, -2576);
    
    _putDontNest(result, -2488, -2591);
    
    _putDontNest(result, -2291, -2651);
    
    _putDontNest(result, -2428, -2519);
    
    _putDontNest(result, -2769, -2878);
    
    _putDontNest(result, -2788, -2863);
    
    _putDontNest(result, -2602, -2665);
    
    _putDontNest(result, -2392, -2621);
    
    _putDontNest(result, -2403, -2596);
    
    _putDontNest(result, -2534, -2606);
    
    _putDontNest(result, -2519, -2621);
    
    _putDontNest(result, -2557, -2651);
    
    _putDontNest(result, -2552, -2656);
    
    _putDontNest(result, -2547, -2641);
    
    _putDontNest(result, -2804, -2863);
    
    _putDontNest(result, -2591, -2646);
    
    _putDontNest(result, -2376, -2621);
    
    _putDontNest(result, -2428, -2601);
    
    _putDontNest(result, -2381, -2566);
    
    _putDontNest(result, -2414, -2651);
    
    _putDontNest(result, -2541, -2651);
    
    _putDontNest(result, -2525, -2571);
    
    _putDontNest(result, -2520, -2576);
    
    _putDontNest(result, -2510, -2566);
    
    _putDontNest(result, -2501, -2611);
    
    _putDontNest(result, -2488, -2556);
    
    _putDontNest(result, -2642, -2641);
    
    _putDontNest(result, -2607, -2646);
    
    _putDontNest(result, -2414, -2571);
    
    _putDontNest(result, -2520, -2656);
    
    _putDontNest(result, -2551, -2621);
    
    _putDontNest(result, -2525, -2651);
    
    _putDontNest(result, -2541, -2571);
    
    _putDontNest(result, -2291, -2571);
    
    _putDontNest(result, -2586, -2665);
    
    _putDontNest(result, -2626, -2641);
    
    _putDontNest(result, -2612, -2631);
    
    _putDontNest(result, -2617, -2636);
    
    _putDontNest(result, -2535, -2621);
    
    _putDontNest(result, -2547, -2561);
    
    _putDontNest(result, -2516, -2596);
    
    _putDontNest(result, -2542, -2566);
    
    _putDontNest(result, -2552, -2576);
    
    _putDontNest(result, -2513, -2556);
    
    _putDontNest(result, -171, -171);
    
    _putDontNest(result, -2392, -2556);
    
    _putDontNest(result, -2612, -2656);
    
    _putDontNest(result, -2617, -2651);
    
    _putDontNest(result, -2571, -2581);
    
    _putDontNest(result, -2577, -2611);
    
    _putDontNest(result, -2319, -2571);
    
    _putDontNest(result, -2510, -2551);
    
    _putDontNest(result, -2376, -2556);
    
    _putDontNest(result, -2561, -2611);
    
    _putDontNest(result, -2601, -2651);
    
    _putDontNest(result, -2596, -2656);
    
    _putDontNest(result, -2513, -2626);
    
    _putDontNest(result, -2556, -2665);
    
    _putDontNest(result, -2525, -2646);
    
    _putDontNest(result, -2392, -2591);
    
    _putDontNest(result, -2288, -2631);
    
    _putDontNest(result, -2291, -2556);
    
    _putDontNest(result, -2288, -2503);
    
    _putDontNest(result, -2304, -2551);
    
    _putDontNest(result, -2381, -2541);
    
    _putDontNest(result, -2414, -2510);
    
    _putDontNest(result, -2611, -2621);
    
    _putDontNest(result, -2572, -2616);
    
    _putDontNest(result, -2562, -2606);
    
    _putDontNest(result, -611, -714);
    
    _putDontNest(result, -638, -717);
    
    _putDontNest(result, -2403, -2631);
    
    _putDontNest(result, -2530, -2631);
    
    _putDontNest(result, -2488, -2621);
    
    _putDontNest(result, -2541, -2646);
    
    _putDontNest(result, -2304, -2631);
    
    _putDontNest(result, -2291, -2646);
    
    _putDontNest(result, -2376, -2591);
    
    _putDontNest(result, -2288, -2551);
    
    _putDontNest(result, -2304, -2503);
    
    _putDontNest(result, -2542, -2551);
    
    _putDontNest(result, -2529, -2556);
    
    _putDontNest(result, -2524, -2541);
    
    _putDontNest(result, -416, -425);
    
    _putDontNest(result, -2591, -2601);
    
    _putDontNest(result, -2567, -2641);
    
    _putDontNest(result, -2319, -2651);
    
    _putDontNest(result, -2524, -2665);
    
    _putDontNest(result, -2557, -2646);
    
    _putDontNest(result, -2291, -2524);
    
    _putDontNest(result, -2304, -2519);
    
    _putDontNest(result, -2607, -2665);
    
    _putDontNest(result, -2381, -2581);
    
    _putDontNest(result, -2414, -2616);
    
    _putDontNest(result, -2551, -2656);
    
    _putDontNest(result, -2510, -2571);
    
    _putDontNest(result, -2525, -2566);
    
    _putDontNest(result, -2520, -2621);
    
    _putDontNest(result, -2288, -2519);
    
    _putDontNest(result, -2566, -2556);
    
    _putDontNest(result, -2606, -2591);
    
    _putDontNest(result, -2582, -2626);
    
    _putDontNest(result, -2586, -2646);
    
    _putDontNest(result, -2592, -2636);
    
    _putDontNest(result, -2516, -2561);
    
    _putDontNest(result, -2535, -2656);
    
    _putDontNest(result, -2556, -2601);
    
    _putDontNest(result, -2542, -2651);
    
    _putDontNest(result, -2547, -2596);
    
    _putDontNest(result, -2519, -2576);
    
    _putDontNest(result, -2291, -2566);
    
    _putDontNest(result, -2513, -2524);
    
    _putDontNest(result, -2392, -2524);
    
    _putDontNest(result, -2602, -2646);
    
    _putDontNest(result, -2597, -2631);
    
    _putDontNest(result, -2376, -2606);
    
    _putDontNest(result, -2542, -2571);
    
    _putDontNest(result, -2516, -2641);
    
    _putDontNest(result, -2501, -2606);
    
    _putDontNest(result, -2552, -2621);
    
    _putDontNest(result, -2519, -2656);
    
    _putDontNest(result, -2535, -2576);
    
    _putDontNest(result, -2376, -2524);
    
    _putDontNest(result, -2591, -2665);
    
    _putDontNest(result, -2631, -2641);
    
    _putDontNest(result, -2392, -2606);
    
    _putDontNest(result, -2510, -2651);
    
    _putDontNest(result, -2541, -2566);
    
    _putDontNest(result, -2551, -2576);
    
    _putDontNest(result, -2534, -2611);
    
    _putDontNest(result, -2524, -2601);
    
    _putDontNest(result, -2525, -2551);
    
    _putDontNest(result, -2611, -2656);
    
    _putDontNest(result, -2572, -2581);
    
    _putDontNest(result, -2616, -2641);
    
    _putDontNest(result, -2851, -2863);
    
    _putDontNest(result, -420, -430);
    
    _putDontNest(result, -2403, -2534);
    
    _putDontNest(result, -2428, -2541);
    
    _putDontNest(result, -2562, -2611);
    
    _putDontNest(result, -619, -624);
    
    _putDontNest(result, -2602, -2651);
    
    _putDontNest(result, -2319, -2566);
    
    _putDontNest(result, -2488, -2606);
    
    _putDontNest(result, -2516, -2626);
    
    _putDontNest(result, -2516, -2534);
    
    _putDontNest(result, -2546, -2556);
    
    _putDontNest(result, -2414, -2503);
    
    _putDontNest(result, -2612, -2621);
    
    _putDontNest(result, -2586, -2651);
    
    _putDontNest(result, -2561, -2606);
    
    _putDontNest(result, -2571, -2616);
    
    _putDontNest(result, -2576, -2601);
    
    _putDontNest(result, -2567, -2596);
    
    _putDontNest(result, -614, -714);
    
    _putDontNest(result, -2403, -2626);
    
    _putDontNest(result, -415, -425);
    
    _putDontNest(result, -2541, -2551);
    
    _putDontNest(result, -2530, -2556);
    
    _putDontNest(result, -2414, -2551);
    
    _putDontNest(result, -2577, -2606);
    
    _putDontNest(result, -2596, -2621);
    
    _putDontNest(result, -2587, -2616);
    
    _putDontNest(result, -2656, -2665);
    
    _putDontNest(result, -2592, -2601);
    
    _putDontNest(result, -619, -717);
    
    _putDontNest(result, -2547, -2631);
    
    _putDontNest(result, -39, -114);
    
    _putDontNest(result, -2392, -2611);
    
    _putDontNest(result, -2319, -2646);
    
    _putDontNest(result, -2530, -2596);
    
    _putDontNest(result, -2519, -2611);
    
    _putDontNest(result, -2542, -2616);
    
    _putDontNest(result, -2288, -2616);
    
    _putDontNest(result, -2291, -2641);
    
    _putDontNest(result, -2319, -2546);
    
    _putDontNest(result, -2414, -2519);
    
    _putDontNest(result, -2582, -2631);
    
    _putDontNest(result, -2376, -2611);
    
    _putDontNest(result, -2552, -2606);
    
    _putDontNest(result, -2525, -2581);
    
    _putDontNest(result, -2534, -2656);
    
    _putDontNest(result, -2546, -2596);
    
    _putDontNest(result, -2513, -2561);
    
    _putDontNest(result, -2501, -2621);
    
    _putDontNest(result, -2529, -2641);
    
    _putDontNest(result, -2304, -2616);
    
    _putDontNest(result, -61, -128);
    
    _putDontNest(result, -2591, -2591);
    
    _putDontNest(result, -2601, -2646);
    
    _putDontNest(result, -2597, -2626);
    
    _putDontNest(result, -2607, -2636);
    
    _putDontNest(result, -2576, -2665);
    
    _putDontNest(result, -2414, -2581);
    
    _putDontNest(result, -2381, -2616);
    
    _putDontNest(result, -2513, -2641);
    
    _putDontNest(result, -2551, -2611);
    
    _putDontNest(result, -2529, -2561);
    
    _putDontNest(result, -2534, -2576);
    
    _putDontNest(result, -2510, -2616);
    
    _putDontNest(result, -2541, -2581);
    
    _putDontNest(result, -45, -128);
    
    _putDontNest(result, -2783, -2858);
    
    _putDontNest(result, -2576, -2586);
    
    _putDontNest(result, -2617, -2646);
    
    _putDontNest(result, -2592, -2665);
    
    _putDontNest(result, -2520, -2606);
    
    _putDontNest(result, -2535, -2611);
    
    _putDontNest(result, -2557, -2581);
    
    _putDontNest(result, -2291, -2561);
   return result;
  }
    
  protected static IntegerMap _initDontNestGroups() {
    IntegerMap result = new IntegerMap();
    int resultStoreId = result.size();
    
    
    ++resultStoreId;
    
    result.putUnsafe(-2869, resultStoreId);
    result.putUnsafe(-2878, resultStoreId);
    result.putUnsafe(-2864, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2576, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2769, resultStoreId);
    result.putUnsafe(-2851, resultStoreId);
    result.putUnsafe(-2804, resultStoreId);
    result.putUnsafe(-2788, resultStoreId);
    result.putUnsafe(-2841, resultStoreId);
    result.putUnsafe(-2761, resultStoreId);
    result.putUnsafe(-2764, resultStoreId);
    result.putUnsafe(-2783, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2577, resultStoreId);
    result.putUnsafe(-2582, resultStoreId);
    result.putUnsafe(-2572, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2581, resultStoreId);
    result.putUnsafe(-2586, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2562, resultStoreId);
    result.putUnsafe(-2567, resultStoreId);
    result.putUnsafe(-2552, resultStoreId);
    result.putUnsafe(-2557, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2547, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2641, resultStoreId);
    result.putUnsafe(-2642, resultStoreId);
    result.putUnsafe(-2646, resultStoreId);
    result.putUnsafe(-2637, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2596, resultStoreId);
    result.putUnsafe(-2601, resultStoreId);
    result.putUnsafe(-2606, resultStoreId);
    result.putUnsafe(-2591, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2291, resultStoreId);
    result.putUnsafe(-2403, resultStoreId);
    result.putUnsafe(-2488, resultStoreId);
    result.putUnsafe(-2376, resultStoreId);
    result.putUnsafe(-2392, resultStoreId);
    result.putUnsafe(-2428, resultStoreId);
    result.putUnsafe(-2381, resultStoreId);
    result.putUnsafe(-2414, resultStoreId);
    result.putUnsafe(-2319, resultStoreId);
    result.putUnsafe(-2288, resultStoreId);
    result.putUnsafe(-2304, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2561, resultStoreId);
    result.putUnsafe(-2566, resultStoreId);
    result.putUnsafe(-2571, resultStoreId);
    result.putUnsafe(-2556, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-611, resultStoreId);
    result.putUnsafe(-614, resultStoreId);
    result.putUnsafe(-619, resultStoreId);
    result.putUnsafe(-624, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2501, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2647, resultStoreId);
    result.putUnsafe(-2656, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2621, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2651, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2868, resultStoreId);
    result.putUnsafe(-2873, resultStoreId);
    result.putUnsafe(-2858, resultStoreId);
    result.putUnsafe(-2863, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-39, resultStoreId);
    result.putUnsafe(-60, resultStoreId);
    result.putUnsafe(-45, resultStoreId);
    result.putUnsafe(-61, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-420, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2617, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2529, resultStoreId);
    result.putUnsafe(-2546, resultStoreId);
    result.putUnsafe(-2534, resultStoreId);
    result.putUnsafe(-2541, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2854, resultStoreId);
    result.putUnsafe(-2859, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2530, resultStoreId);
    result.putUnsafe(-2535, resultStoreId);
    result.putUnsafe(-2525, resultStoreId);
    result.putUnsafe(-2542, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-708, resultStoreId);
    result.putUnsafe(-521, resultStoreId);
    result.putUnsafe(-556, resultStoreId);
    result.putUnsafe(-653, resultStoreId);
    result.putUnsafe(-638, resultStoreId);
    result.putUnsafe(-608, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2657, resultStoreId);
    result.putUnsafe(-2652, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-415, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2520, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-425, resultStoreId);
    result.putUnsafe(-416, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-171, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2597, resultStoreId);
    result.putUnsafe(-2602, resultStoreId);
    result.putUnsafe(-2587, resultStoreId);
    result.putUnsafe(-2592, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2611, resultStoreId);
    result.putUnsafe(-2616, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-421, resultStoreId);
    result.putUnsafe(-430, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-167, resultStoreId);
    result.putUnsafe(-176, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2524, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2612, resultStoreId);
    result.putUnsafe(-2607, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2551, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2626, resultStoreId);
    result.putUnsafe(-2631, resultStoreId);
    result.putUnsafe(-2636, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2513, resultStoreId);
    result.putUnsafe(-2516, resultStoreId);
    result.putUnsafe(-2519, resultStoreId);
    result.putUnsafe(-2510, resultStoreId);
      
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
	
  private static final IConstructor prod__negative_$Pattern__lit___$layouts_LAYOUTLIST_argument_$Pattern_ = (IConstructor) _read("prod(label(\"negative\",sort(\"Pattern\")),[lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_$Rest_ = (IConstructor) _read("prod(label(\"default\",sort(\"PreModule\")),[label(\"header\",sort(\"Header\")),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(keywords(\"HeaderKeyword\"))}),layouts(\"LAYOUTLIST\"),label(\"rest\",lex(\"Rest\"))],{})", Factory.Production);
  private static final IConstructor prod__parametric_$UserType__conditional__name_$QualifiedName__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"parametric\",sort(\"UserType\")),[conditional(label(\"name\",sort(\"QualifiedName\")),{follow(lit(\"[\"))}),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExpLiteral__lit___47_iter_star__$RegExp_lit___47_$RegExpModifier_ = (IConstructor) _read("prod(lex(\"RegExpLiteral\"),[lit(\"/\"),\\iter-star(lex(\"RegExp\")),lit(\"/\"),lex(\"RegExpModifier\")],{})", Factory.Production);
  private static final IConstructor prod__midTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_ = (IConstructor) _read("prod(label(\"midTemplate\",sort(\"StringTail\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__callOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"callOrTree\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_start_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"start\")],{})", Factory.Production);
  private static final IConstructor prod__default_$ImportedModule__name_$QualifiedName_ = (IConstructor) _read("prod(label(\"default\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__globalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"globalDirective\",sort(\"Statement\")),[lit(\"global\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"names\",\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__shell_$Command__lit___58_$layouts_LAYOUTLIST_command_$ShellCommand_ = (IConstructor) _read("prod(label(\"shell\",sort(\"Command\")),[lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"command\",sort(\"ShellCommand\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"fail\"),[\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(105,105)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__labeled_$DataTarget__label_$Name_$layouts_LAYOUTLIST_lit___58_ = (IConstructor) _read("prod(label(\"labeled\",sort(\"DataTarget\")),[label(\"label\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__$LAYOUT = (IConstructor) _read("regular(\\iter-star(lex(\"LAYOUT\")))", Factory.Production);
  private static final IConstructor prod__default_$LocationLiteral__protocolPart_$ProtocolPart_$layouts_LAYOUTLIST_pathPart_$PathPart_ = (IConstructor) _read("prod(label(\"default\",sort(\"LocationLiteral\")),[label(\"protocolPart\",sort(\"ProtocolPart\")),layouts(\"LAYOUTLIST\"),label(\"pathPart\",sort(\"PathPart\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___45_61__char_class___range__45_45_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"-=\"),[\\char-class([range(45,45)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__48_57 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor prod__match_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"match\",sort(\"Expression\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_125 = (IConstructor) _read("prod(label(\"expression\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable()),tag(breakable(\"{expression}\"))})", Factory.Production);
  private static final IConstructor prod__timeLiteral_$DateTimeLiteral__time_$JustTime_ = (IConstructor) _read("prod(label(\"timeLiteral\",sort(\"DateTimeLiteral\")),[label(\"time\",lex(\"JustTime\"))],{})", Factory.Production);
  private static final IConstructor prod__dateLiteral_$DateTimeLiteral__date_$JustDate_ = (IConstructor) _read("prod(label(\"dateLiteral\",sort(\"DateTimeLiteral\")),[label(\"date\",lex(\"JustDate\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_assoc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"assoc\")],{})", Factory.Production);
  private static final IConstructor prod__noThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_ = (IConstructor) _read("prod(label(\"noThrows\",sort(\"Signature\")),[label(\"modifiers\",sort(\"FunctionModifiers\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\"))],{})", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor prod__list_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"list\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_function__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"function\"),[\\char-class([range(102,102)]),\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"int\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__name_$Field__fieldName_$Name_ = (IConstructor) _read("prod(label(\"name\",sort(\"Field\")),[label(\"fieldName\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__topDownBreak_$Strategy__lit_top_down_break_ = (IConstructor) _read("prod(label(\"topDownBreak\",sort(\"Strategy\")),[lit(\"top-down-break\")],{})", Factory.Production);
  private static final IConstructor prod__$JustTime__lit___36_84_$TimePartNoTZ_$TimeZonePart_ = (IConstructor) _read("prod(lex(\"JustTime\"),[lit(\"$T\"),lex(\"TimePartNoTZ\"),lex(\"TimeZonePart\")],{})", Factory.Production);
  private static final IConstructor prod__dateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_ = (IConstructor) _read("prod(label(\"dateTime\",sort(\"Literal\")),[label(\"dateTimeLiteral\",sort(\"DateTimeLiteral\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"filter\",sort(\"Statement\")),[lit(\"filter\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"when\"),[\\char-class([range(119,119)]),\\char-class([range(104,104)]),\\char-class([range(101,101)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"type\"),[\\char-class([range(116,116)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__division_$Assignment__lit___47_61_ = (IConstructor) _read("prod(label(\"division\",sort(\"Assignment\")),[lit(\"/=\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_catch_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"catch\")],{})", Factory.Production);
  private static final IConstructor prod__lit_modules__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"modules\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_ = (IConstructor) _read("prod(lit(\"catch\"),[\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(99,99)]),\\char-class([range(104,104)])],{})", Factory.Production);
  private static final IConstructor prod__$Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalKeywords_ = (IConstructor) _read("prod(lex(\"Name\"),[conditional(seq([conditional(\\char-class([range(65,90),range(95,95),range(97,122)]),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})]),{delete(keywords(\"RascalKeywords\"))})],{})", Factory.Production);
  private static final IConstructor prod__dataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"dataAbstract\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"data\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"lessThanOrEq\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\<=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"case\"),[\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_data_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"data\")],{})", Factory.Production);
  private static final IConstructor prod__alias_$Kind__lit_alias_ = (IConstructor) _read("prod(label(\"alias\",sort(\"Kind\")),[lit(\"alias\")],{})", Factory.Production);
  private static final IConstructor prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"/=\"),[\\char-class([range(47,47)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__default_$Declarator__type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"Declarator\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_dynamic_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"dynamic\")],{})", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))", Factory.Production);
  private static final IConstructor prod__quit_$ShellCommand__lit_quit_ = (IConstructor) _read("prod(label(\"quit\",sort(\"ShellCommand\")),[lit(\"quit\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"TypeVar\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__conditional_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_44_99_111_110_100_105_116_105_111_110_115_125 = (IConstructor) _read("prod(label(\"conditional\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"when\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable()),tag(breakable(\"{expression,conditions}\"))})", Factory.Production);
  private static final IConstructor prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"//\"),[\\char-class([range(47,47)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__lit_help__char_class___range__104_104_char_class___range__101_101_char_class___range__108_108_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"help\"),[\\char-class([range(104,104)]),\\char-class([range(101,101)]),\\char-class([range(108,108)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__default_$TypeArg__type_$Type_ = (IConstructor) _read("prod(label(\"default\",sort(\"TypeArg\")),[label(\"type\",sort(\"Type\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_ = (IConstructor) _read("prod(lit(\"num\"),[\\char-class([range(110,110)]),\\char-class([range(117,117)]),\\char-class([range(109,109)])],{})", Factory.Production);
  private static final IConstructor prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"tag\"),[\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__dynamic_$LocalVariableDeclaration__lit_dynamic_$layouts_LAYOUTLIST_declarator_$Declarator_ = (IConstructor) _read("prod(label(\"dynamic\",sort(\"LocalVariableDeclaration\")),[lit(\"dynamic\"),layouts(\"LAYOUTLIST\"),label(\"declarator\",sort(\"Declarator\"))],{})", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_57 = (IConstructor) _read("regular(iter(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_55 = (IConstructor) _read("regular(iter(\\char-class([range(48,55)])))", Factory.Production);
  private static final IConstructor prod__equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"equivalence\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\<==\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_ = (IConstructor) _read("prod(label(\"negative\",sort(\"Expression\")),[lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__default_$QualifiedName__conditional__names_iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST__not_follow__lit___58_58_ = (IConstructor) _read("prod(label(\"default\",sort(\"QualifiedName\")),[conditional(label(\"names\",\\iter-seps(lex(\"Name\"),[layouts(\"LAYOUTLIST\"),lit(\"::\"),layouts(\"LAYOUTLIST\")])),{\\not-follow(lit(\"::\"))})],{})", Factory.Production);
  private static final IConstructor prod__$PreStringChars__char_class___range__34_34_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"PreStringChars\"),[\\char-class([range(34,34)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(60,60)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Import__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"://\"),[\\char-class([range(58,58)]),\\char-class([range(47,47)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__reifiedType_$Expression__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Expression_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"reifiedType\",sort(\"Expression\")),[lit(\"type\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"definitions\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__dateTime_$BasicType__lit_datetime_ = (IConstructor) _read("prod(label(\"dateTime\",sort(\"BasicType\")),[lit(\"datetime\")],{})", Factory.Production);
  private static final IConstructor prod__structured_$Type__structured_$StructuredType_ = (IConstructor) _read("prod(label(\"structured\",sort(\"Type\")),[label(\"structured\",sort(\"StructuredType\"))],{})", Factory.Production);
  private static final IConstructor prod__$JustDate__lit___36_$DatePart_ = (IConstructor) _read("prod(lex(\"JustDate\"),[lit(\"$\"),lex(\"DatePart\")],{})", Factory.Production);
  private static final IConstructor prod__template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_ = (IConstructor) _read("prod(label(\"template\",sort(\"StringLiteral\")),[label(\"pre\",lex(\"PreStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(100,100),range(105,105),range(109,109),range(115,115)])))", Factory.Production);
  private static final IConstructor prod__lit___33_62_62__char_class___range__33_33_char_class___range__62_62_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"!\\>\\>\"),[\\char-class([range(33,33)]),\\char-class([range(62,62)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__default_$Renaming__from_$Name_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_to_$Name_ = (IConstructor) _read("prod(label(\"default\",sort(\"Renaming\")),[label(\"from\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\\>\"),layouts(\"LAYOUTLIST\"),label(\"to\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__basic_$Type__basic_$BasicType_ = (IConstructor) _read("prod(label(\"basic\",sort(\"Type\")),[label(\"basic\",sort(\"BasicType\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_ = (IConstructor) _read("prod(lit(\"..\"),[\\char-class([range(46,46)]),\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__ascii_$UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(label(\"ascii\",lex(\"UnicodeEscape\")),[lit(\"\\\\\"),\\char-class([range(97,97)]),\\char-class([range(48,55)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_ = (IConstructor) _read("prod(label(\"undeclare\",sort(\"ShellCommand\")),[lit(\"undeclare\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__variable_$Type__typeVar_$TypeVar_ = (IConstructor) _read("prod(label(\"variable\",sort(\"Type\")),[label(\"typeVar\",sort(\"TypeVar\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_ = (IConstructor) _read("prod(lit(\"throw\"),[\\char-class([range(116,116)]),\\char-class([range(104,104)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(119,119)])],{})", Factory.Production);
  private static final IConstructor prod__equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"equals\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"==\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__fieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"fieldUpdate\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"key\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"replacement\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__mid_$PathTail__mid_$MidPathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_ = (IConstructor) _read("prod(label(\"mid\",sort(\"PathTail\")),[label(\"mid\",lex(\"MidPathChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"PathTail\"))],{})", Factory.Production);
  private static final IConstructor prod__product_$Assignment__lit___42_61_ = (IConstructor) _read("prod(label(\"product\",sort(\"Assignment\")),[lit(\"*=\")],{})", Factory.Production);
  private static final IConstructor prod__tuple_$Assignable__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"tuple\",sort(\"Assignable\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__statement_$EvalCommand__statement_$Statement_ = (IConstructor) _read("prod(label(\"statement\",sort(\"EvalCommand\")),[label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__right_$Assoc__lit_right_ = (IConstructor) _read("prod(label(\"right\",sort(\"Assoc\")),[lit(\"right\")],{})", Factory.Production);
  private static final IConstructor prod__lit_on__char_class___range__111_111_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"on\"),[\\char-class([range(111,111)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__$StringCharacter__$UnicodeEscape_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[lex(\"UnicodeEscape\")],{})", Factory.Production);
  private static final IConstructor prod__lit___47_42__char_class___range__47_47_char_class___range__42_42_ = (IConstructor) _read("prod(lit(\"/*\"),[\\char-class([range(47,47)]),\\char-class([range(42,42)])],{})", Factory.Production);
  private static final IConstructor prod__loc_$BasicType__lit_loc_ = (IConstructor) _read("prod(label(\"loc\",sort(\"BasicType\")),[lit(\"loc\")],{})", Factory.Production);
  private static final IConstructor prod__$DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"DecimalIntegerLiteral\"),[conditional(lit(\"0\"),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__renamings_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_renamings_$Renamings_ = (IConstructor) _read("prod(label(\"renamings\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"renamings\",sort(\"Renamings\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_is__char_class___range__105_105_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"is\"),[\\char-class([range(105,105)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_it__char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"it\"),[\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"loc\"),[\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__insertBefore_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"insertBefore\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__default_$Tags__tags_iter_star_seps__$Tag__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"Tags\")),[label(\"tags\",\\iter-star-seps(sort(\"Tag\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_any_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"any\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_all_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"all\")],{})", Factory.Production);
  private static final IConstructor prod__lit_in__char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"in\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__declaration_$EvalCommand__declaration_$Declaration_ = (IConstructor) _read("prod(label(\"declaration\",sort(\"EvalCommand\")),[label(\"declaration\",sort(\"Declaration\"))],{})", Factory.Production);
  private static final IConstructor prod__integer_$Literal__integerLiteral_$IntegerLiteral_ = (IConstructor) _read("prod(label(\"integer\",sort(\"Literal\")),[label(\"integerLiteral\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__$MidStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"MidStringChars\"),[\\char-class([range(62,62)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(60,60)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__labeled_$Target__name_$Name_ = (IConstructor) _read("prod(label(\"labeled\",sort(\"Target\")),[label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_right__char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"right\"),[\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(103,103)]),\\char-class([range(104,104)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_declarations__char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"declarations\"),[\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_case_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"case\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_layout_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"layout\")],{})", Factory.Production);
  private static final IConstructor prod__topDown_$Strategy__lit_top_down_ = (IConstructor) _read("prod(label(\"topDown\",sort(\"Strategy\")),[lit(\"top-down\")],{})", Factory.Production);
  private static final IConstructor prod__lit___59__char_class___range__59_59_ = (IConstructor) _read("prod(lit(\";\"),[\\char-class([range(59,59)])],{})", Factory.Production);
  private static final IConstructor prod__default_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"default\",sort(\"Parameters\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"formals\",sort(\"Formals\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_bag_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bag\")],{})", Factory.Production);
  private static final IConstructor prod__lit___58__char_class___range__58_58_ = (IConstructor) _read("prod(lit(\":\"),[\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__lit___61__char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"=\"),[\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_ = (IConstructor) _read("prod(lit(\"data\"),[\\char-class([range(100,100)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(97,97)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60__char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\<\"),[\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_ = (IConstructor) _read("prod(label(\"visit\",sort(\"Expression\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),label(\"visit\",sort(\"Visit\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___63__char_class___range__63_63_ = (IConstructor) _read("prod(lit(\"?\"),[\\char-class([range(63,63)])],{})", Factory.Production);
  private static final IConstructor prod__default_$LocalVariableDeclaration__declarator_$Declarator_ = (IConstructor) _read("prod(label(\"default\",sort(\"LocalVariableDeclaration\")),[label(\"declarator\",sort(\"Declarator\"))],{})", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__43_43_range__45_45 = (IConstructor) _read("regular(opt(\\char-class([range(43,43),range(45,45)])))", Factory.Production);
  private static final IConstructor prod__lit___33_60_60__char_class___range__33_33_char_class___range__60_60_char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"!\\<\\<\"),[\\char-class([range(33,33)]),\\char-class([range(60,60)]),\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__lit___62__char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\>\"),[\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__history_$ShellCommand__lit_history_ = (IConstructor) _read("prod(label(\"history\",sort(\"ShellCommand\")),[lit(\"history\")],{})", Factory.Production);
  private static final IConstructor prod__lit_0__char_class___range__48_48_ = (IConstructor) _read("prod(lit(\"0\"),[\\char-class([range(48,48)])],{})", Factory.Production);
  private static final IConstructor prod__solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable = (IConstructor) _read("prod(label(\"solve\",sort(\"Statement\")),[lit(\"solve\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"bound\",sort(\"Bound\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__lit___41__char_class___range__41_41_ = (IConstructor) _read("prod(lit(\")\"),[\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__all_$Kind__lit_all_ = (IConstructor) _read("prod(label(\"all\",sort(\"Kind\")),[lit(\"all\")],{})", Factory.Production);
  private static final IConstructor prod__decimalIntegerLiteral_$IntegerLiteral__decimal_$DecimalIntegerLiteral_ = (IConstructor) _read("prod(label(\"decimalIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"decimal\",lex(\"DecimalIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___40__char_class___range__40_40_ = (IConstructor) _read("prod(lit(\"(\"),[\\char-class([range(40,40)])],{})", Factory.Production);
  private static final IConstructor prod__assertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"assertWithMessage\",sort(\"Statement\")),[lit(\"assert\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"message\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__lit___43__char_class___range__43_43_ = (IConstructor) _read("prod(lit(\"+\"),[\\char-class([range(43,43)])],{})", Factory.Production);
  private static final IConstructor prod__lit___42__char_class___range__42_42_ = (IConstructor) _read("prod(lit(\"*\"),[\\char-class([range(42,42)])],{})", Factory.Production);
  private static final IConstructor prod__lit____char_class___range__45_45_ = (IConstructor) _read("prod(lit(\"-\"),[\\char-class([range(45,45)])],{})", Factory.Production);
  private static final IConstructor prod__expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Folded_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"expression\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{tag(Folded()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__lit___44__char_class___range__44_44_ = (IConstructor) _read("prod(lit(\",\"),[\\char-class([range(44,44)])],{})", Factory.Production);
  private static final IConstructor prod__lit___47__char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"/\"),[\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__lit___46__char_class___range__46_46_ = (IConstructor) _read("prod(lit(\".\"),[\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__lit_if__char_class___range__105_105_char_class___range__102_102_ = (IConstructor) _read("prod(lit(\"if\"),[\\char-class([range(105,105)]),\\char-class([range(102,102)])],{})", Factory.Production);
  private static final IConstructor prod__lit___33__char_class___range__33_33_ = (IConstructor) _read("prod(lit(\"!\"),[\\char-class([range(33,33)])],{})", Factory.Production);
  private static final IConstructor prod__start__$PreModule__$layouts_LAYOUTLIST_top_$PreModule_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"PreModule\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"PreModule\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__lit___35__char_class___range__35_35_ = (IConstructor) _read("prod(lit(\"#\"),[\\char-class([range(35,35)])],{})", Factory.Production);
  private static final IConstructor prod__tag_$Kind__lit_tag_ = (IConstructor) _read("prod(label(\"tag\",sort(\"Kind\")),[lit(\"tag\")],{})", Factory.Production);
  private static final IConstructor prod__lit___34__char_class___range__34_34_ = (IConstructor) _read("prod(lit(\"\\\"\"),[\\char-class([range(34,34)])],{})", Factory.Production);
  private static final IConstructor prod__lit___37__char_class___range__37_37_ = (IConstructor) _read("prod(lit(\"%\"),[\\char-class([range(37,37)])],{})", Factory.Production);
  private static final IConstructor prod__lit___36__char_class___range__36_36_ = (IConstructor) _read("prod(lit(\"$\"),[\\char-class([range(36,36)])],{})", Factory.Production);
  private static final IConstructor prod__lit___39__char_class___range__39_39_ = (IConstructor) _read("prod(lit(\"\\'\\\\\"),[\\char-class([range(39,39)])],{})", Factory.Production);
  private static final IConstructor prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"return\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(117,117)]),\\char-class([range(114,114)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"datetime\"),[\\char-class([range(100,100)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit___38__char_class___range__38_38_ = (IConstructor) _read("prod(lit(\"&\"),[\\char-class([range(38,38)])],{})", Factory.Production);
  private static final IConstructor prod__range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"range\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"..\"),layouts(\"LAYOUTLIST\"),label(\"last\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor regular__seq___lit___92_char_class___range__123_123_range__125_125 = (IConstructor) _read("regular(seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])]))", Factory.Production);
  private static final IConstructor prod__lit_Z__char_class___range__90_90_ = (IConstructor) _read("prod(lit(\"Z\"),[\\char-class([range(90,90)])],{})", Factory.Production);
  private static final IConstructor prod__lit___91__char_class___range__91_91_ = (IConstructor) _read("prod(lit(\"[\"),[\\char-class([range(91,91)])],{})", Factory.Production);
  private static final IConstructor prod__literal_$Expression__literal_$Literal_ = (IConstructor) _read("prod(label(\"literal\",sort(\"Expression\")),[label(\"literal\",sort(\"Literal\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___94__char_class___range__94_94_ = (IConstructor) _read("prod(lit(\"^\"),[\\char-class([range(94,94)])],{})", Factory.Production);
  private static final IConstructor prod__visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit__tag__breakable = (IConstructor) _read("prod(label(\"visit\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),label(\"visit\",sort(\"Visit\"))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__extend_$Import__lit_extend_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"extend\",sort(\"Import\")),[lit(\"extend\"),layouts(\"LAYOUTLIST\"),label(\"module\",sort(\"ImportedModule\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left = (IConstructor) _read("prod(label(\"difference\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit___92__char_class___range__92_92_ = (IConstructor) _read("prod(lit(\"\\\\\"),[\\char-class([range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__lit___93__char_class___range__93_93_ = (IConstructor) _read("prod(lit(\"]\"),[\\char-class([range(93,93)])],{})", Factory.Production);
  private static final IConstructor prod__set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"set\",sort(\"Expression\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__lit_mod__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"mod\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__variable_$Assignable__qualifiedName_$QualifiedName_ = (IConstructor) _read("prod(label(\"variable\",sort(\"Assignable\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__$NamedRegExp__$NamedBackslash_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[lex(\"NamedBackslash\")],{})", Factory.Production);
  private static final IConstructor prod__default_$Renamings__lit_renaming_$layouts_LAYOUTLIST_renamings_iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"Renamings\")),[lit(\"renaming\"),layouts(\"LAYOUTLIST\"),label(\"renamings\",\\iter-seps(sort(\"Renaming\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__interpolated_$ProtocolPart__pre_$PreProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_ = (IConstructor) _read("prod(label(\"interpolated\",sort(\"ProtocolPart\")),[label(\"pre\",lex(\"PreProtocolChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"ProtocolTail\"))],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__$Backslash_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lex(\"Backslash\")],{})", Factory.Production);
  private static final IConstructor prod__lit_T__char_class___range__84_84_ = (IConstructor) _read("prod(lit(\"T\"),[\\char-class([range(84,84)])],{})", Factory.Production);
  private static final IConstructor prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"alias\"),[\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(97,97)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__outermost_$Strategy__lit_outermost_ = (IConstructor) _read("prod(label(\"outermost\",sort(\"Strategy\")),[lit(\"outermost\")],{})", Factory.Production);
  private static final IConstructor prod__nonAssociative_$Assoc__lit_non_assoc_ = (IConstructor) _read("prod(label(\"nonAssociative\",sort(\"Assoc\")),[lit(\"non-assoc\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__9_9_range__32_32 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(9,9),range(32,32)])))", Factory.Production);
  private static final IConstructor prod__default_$Case__lit_default_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement__tag__Foldable = (IConstructor) _read("prod(label(\"default\",sort(\"Case\")),[lit(\"default\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__typedVariable_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"typedVariable\",sort(\"Pattern\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_assert_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"assert\")],{})", Factory.Production);
  private static final IConstructor prod__lit___64__char_class___range__64_64_ = (IConstructor) _read("prod(lit(\"@\"),[\\char-class([range(64,64)])],{})", Factory.Production);
  private static final IConstructor prod__lit___43_61__char_class___range__43_43_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"+=\"),[\\char-class([range(43,43)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_import_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"import\")],{})", Factory.Production);
  private static final IConstructor prod__lit___123__char_class___range__123_123_ = (IConstructor) _read("prod(lit(\"{\"),[\\char-class([range(123,123)])],{})", Factory.Production);
  private static final IConstructor prod__import_$Command__imported_$Import_ = (IConstructor) _read("prod(label(\"import\",sort(\"Command\")),[label(\"imported\",sort(\"Import\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___33_58_61__char_class___range__33_33_char_class___range__58_58_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"!:=\"),[\\char-class([range(33,33)]),\\char-class([range(58,58)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Tag__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Tag\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__actuals_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_ = (IConstructor) _read("prod(label(\"actuals\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"actuals\",sort(\"ModuleActuals\"))],{})", Factory.Production);
  private static final IConstructor prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[\\char-class([range(0,33),range(35,38),range(40,59),range(61,61),range(63,91),range(93,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__lit___125__char_class___range__125_125_ = (IConstructor) _read("prod(lit(\"}\"),[\\char-class([range(125,125)])],{})", Factory.Production);
  private static final IConstructor prod__lit___124__char_class___range__124_124_ = (IConstructor) _read("prod(lit(\"|\"),[\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__append_$Assignment__lit___60_60_61_ = (IConstructor) _read("prod(label(\"append\",sort(\"Assignment\")),[lit(\"\\<\\<=\")],{})", Factory.Production);
  private static final IConstructor prod__caseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_ = (IConstructor) _read("prod(label(\"caseInsensitiveLiteral\",sort(\"Sym\")),[label(\"cistring\",lex(\"CaseInsensitiveStringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___42_47__char_class___range__42_42_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"*/\"),[\\char-class([range(42,42)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__mid_$StringMiddle__mid_$MidStringChars_ = (IConstructor) _read("prod(label(\"mid\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\"))],{})", Factory.Production);
  private static final IConstructor prod__list_$Comprehension__lit___91_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125 = (IConstructor) _read("prod(label(\"list\",sort(\"Comprehension\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"results\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{tag(breakable(\"{results,generators}\"))})", Factory.Production);
  private static final IConstructor prod__variableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"variableDeclaration\",sort(\"Statement\")),[label(\"declaration\",sort(\"LocalVariableDeclaration\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__utf32_$UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(label(\"utf32\",lex(\"UnicodeEscape\")),[lit(\"\\\\\"),\\char-class([range(85,85)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__subtraction_$Assignment__lit___45_61_ = (IConstructor) _read("prod(label(\"subtraction\",sort(\"Assignment\")),[lit(\"-=\")],{})", Factory.Production);
  private static final IConstructor prod__default_$ModuleActuals__lit___91_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"default\",sort(\"ModuleActuals\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"types\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_true_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"true\")],{})", Factory.Production);
  private static final IConstructor prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__$RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(48,57)]),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__lit_o__char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"o\"),[\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Toplevel__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Toplevel\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__defaultStrategy_$Visit__lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"defaultStrategy\",sort(\"Visit\")),[lit(\"visit\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"subject\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__$DateAndTime__lit___36_$DatePart_lit_T_conditional__$TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_ = (IConstructor) _read("prod(lex(\"DateAndTime\"),[lit(\"$\"),lex(\"DatePart\"),lit(\"T\"),conditional(lex(\"TimePartNoTZ\"),{\\not-follow(\\char-class([range(43,43),range(45,45)]))})],{})", Factory.Production);
  private static final IConstructor prod__labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_ = (IConstructor) _read("prod(label(\"labeled\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"label\",lex(\"NonterminalLabel\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_unimport__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"unimport\"),[\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__num_$BasicType__lit_num_ = (IConstructor) _read("prod(label(\"num\",sort(\"BasicType\")),[lit(\"num\")],{})", Factory.Production);
  private static final IConstructor prod__unconditional_$Replacement__replacementExpression_$Expression_ = (IConstructor) _read("prod(label(\"unconditional\",sort(\"Replacement\")),[label(\"replacementExpression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__$Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(lex(\"Comment\"),[lit(\"/*\"),\\iter-star(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,16777215)])})),lit(\"*/\")],{tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__qualifiedName_$Pattern__qualifiedName_$QualifiedName_ = (IConstructor) _read("prod(label(\"qualifiedName\",sort(\"Pattern\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"*=\"),[\\char-class([range(42,42)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__$RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_ = (IConstructor) _read("prod(lex(\"RegExpModifier\"),[\\iter-star(\\char-class([range(100,100),range(105,105),range(109,109),range(115,115)]))],{})", Factory.Production);
  private static final IConstructor prod__$NamedRegExp__lit___60_$Name_lit___62_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__midInterpolated_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_ = (IConstructor) _read("prod(label(\"midInterpolated\",sort(\"StringTail\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor regular__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215 = (IConstructor) _read("regular(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,16777215)])}))", Factory.Production);
  private static final IConstructor prod__lit_edit__char_class___range__101_101_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"edit\"),[\\char-class([range(101,101)]),\\char-class([range(100,100)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125 = (IConstructor) _read("regular(alt({lex(\"TagString\"),\\char-class([range(0,122),range(124,124),range(126,16777215)]),seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])])}))", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_finally_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"finally\")],{})", Factory.Production);
  private static final IConstructor prod__default_$Mapping__$Expression__from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_ = (IConstructor) _read("prod(label(\"default\",\\parameterized-sort(\"Mapping\",[sort(\"Expression\")])),[label(\"from\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"node\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__ifThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__right = (IConstructor) _read("prod(label(\"ifThenElse\",sort(\"Expression\")),[label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"thenExp\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"elseExp\",sort(\"Expression\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"addition\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"+\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__set_$Comprehension__lit___123_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125 = (IConstructor) _read("prod(label(\"set\",sort(\"Comprehension\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"results\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{tag(breakable(\"{results,generators}\"))})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_filter_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"filter\")],{})", Factory.Production);
  private static final IConstructor prod__$Comment__lit___47_47_conditional__iter_star__char_class___range__0_9_range__11_16777215__not_follow__char_class___range__9_9_range__13_13_range__32_32_end_of_line__tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(lex(\"Comment\"),[lit(\"//\"),conditional(\\iter-star(\\char-class([range(0,9),range(11,16777215)])),{\\not-follow(\\char-class([range(9,9),range(13,13),range(32,32)])),\\end-of-line()})],{tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_while_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"while\")],{})", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_57_range__65_70_range__97_102 = (IConstructor) _read("regular(iter(\\char-class([range(48,57),range(65,70),range(97,102)])))", Factory.Production);
  private static final IConstructor prod__tryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement__tag__breakable = (IConstructor) _read("prod(label(\"tryFinally\",sort(\"Statement\")),[lit(\"try\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),label(\"handlers\",\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"finally\"),layouts(\"LAYOUTLIST\"),label(\"finallyBody\",sort(\"Statement\"))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__declaration_$Command__declaration_$Declaration_ = (IConstructor) _read("prod(label(\"declaration\",sort(\"Command\")),[label(\"declaration\",sort(\"Declaration\"))],{})", Factory.Production);
  private static final IConstructor prod__bool_$BasicType__lit_bool_ = (IConstructor) _read("prod(label(\"bool\",sort(\"BasicType\")),[lit(\"bool\")],{})", Factory.Production);
  private static final IConstructor prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_ = (IConstructor) _read("prod(lit(\"$T\"),[\\char-class([range(36,36)]),\\char-class([range(84,84)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(lex(\"Name\"),[layouts(\"LAYOUTLIST\"),lit(\"::\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"tuple\",sort(\"Pattern\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__lit_do__char_class___range__100_100_char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"do\"),[\\char-class([range(100,100)]),\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"Assignable\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arg\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__start__$Command__$layouts_LAYOUTLIST_top_$Command_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Command\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Command\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_visit_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"visit\")],{})", Factory.Production);
  private static final IConstructor prod__function_$Type__function_$FunctionType_ = (IConstructor) _read("prod(label(\"function\",sort(\"Type\")),[label(\"function\",sort(\"FunctionType\"))],{})", Factory.Production);
  private static final IConstructor prod__$JustTime__lit___36_84_conditional__$TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_ = (IConstructor) _read("prod(lex(\"JustTime\"),[lit(\"$T\"),conditional(lex(\"TimePartNoTZ\"),{\\not-follow(\\char-class([range(43,43),range(45,45)]))})],{})", Factory.Production);
  private static final IConstructor prod__$MidProtocolChars__lit___62_$URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"MidProtocolChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__lit_java__char_class___range__106_106_char_class___range__97_97_char_class___range__118_118_char_class___range__97_97_ = (IConstructor) _read("prod(lit(\"java\"),[\\char-class([range(106,106)]),\\char-class([range(97,97)]),\\char-class([range(118,118)]),\\char-class([range(97,97)])],{})", Factory.Production);
  private static final IConstructor prod__binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_ = (IConstructor) _read("prod(label(\"binding\",sort(\"Catch\")),[lit(\"catch\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__fieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_ = (IConstructor) _read("prod(label(\"fieldAccess\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"field\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_notin_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"notin\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_append_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"append\")],{})", Factory.Production);
  private static final IConstructor prod__void_$BasicType__lit_void_ = (IConstructor) _read("prod(label(\"void\",sort(\"BasicType\")),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__givenStrategy_$Visit__strategy_$Strategy_$layouts_LAYOUTLIST_lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"givenStrategy\",sort(\"Visit\")),[label(\"strategy\",sort(\"Strategy\")),layouts(\"LAYOUTLIST\"),lit(\"visit\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"subject\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Sym__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__reifiedType_$Pattern__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Pattern_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Pattern_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"reifiedType\",sort(\"Pattern\")),[lit(\"type\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"definitions\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__ifThenElse_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_elseStatement_$Statement__tag__breakable = (IConstructor) _read("prod(label(\"ifThenElse\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"thenStatement\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),lit(\"else\"),layouts(\"LAYOUTLIST\"),label(\"elseStatement\",sort(\"Statement\"))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__nAryConstructor_$Variant__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"nAryConstructor\",sort(\"Variant\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__alias_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_alias_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_base_$Type_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"alias\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"alias\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"base\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lit_quit__char_class___range__113_113_char_class___range__117_117_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"quit\"),[\\char-class([range(113,113)]),\\char-class([range(117,117)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"tuple\"),[\\char-class([range(116,116)]),\\char-class([range(117,117)]),\\char-class([range(112,112)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__$OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"OctalIntegerLiteral\"),[\\char-class([range(48,48)]),conditional(iter(\\char-class([range(48,55)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"variable\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Variant\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable = (IConstructor) _read("prod(label(\"append\",sort(\"Statement\")),[lit(\"append\"),layouts(\"LAYOUTLIST\"),label(\"dataTarget\",sort(\"DataTarget\")),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc()),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"implication\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"==\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_try_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"try\")],{})", Factory.Production);
  private static final IConstructor prod__data_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_variants_iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"data\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"data\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"variants\",\\iter-seps(sort(\"Variant\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__patternWithAction_$Case__lit_case_$layouts_LAYOUTLIST_patternWithAction_$PatternWithAction__tag__Foldable = (IConstructor) _read("prod(label(\"patternWithAction\",sort(\"Case\")),[lit(\"case\"),layouts(\"LAYOUTLIST\"),label(\"patternWithAction\",sort(\"PatternWithAction\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__node_$BasicType__lit_node_ = (IConstructor) _read("prod(label(\"node\",sort(\"BasicType\")),[lit(\"node\")],{})", Factory.Production);
  private static final IConstructor prod__lit_has__char_class___range__104_104_char_class___range__97_97_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"has\"),[\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__function_$Declaration__functionDeclaration_$FunctionDeclaration_ = (IConstructor) _read("prod(label(\"function\",sort(\"Declaration\")),[label(\"functionDeclaration\",sort(\"FunctionDeclaration\"))],{})", Factory.Production);
  private static final IConstructor prod__$TagString__lit___123_contents_iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_ = (IConstructor) _read("prod(lex(\"TagString\"),[lit(\"{\"),label(\"contents\",\\iter-star(alt({lex(\"TagString\"),\\char-class([range(0,122),range(124,124),range(126,16777215)]),seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])])}))),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_insert_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"insert\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_anno_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"anno\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__char_class___range__60_60_expression_$Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101 = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(60,60)]),label(\"expression\",sort(\"Expression\")),\\char-class([range(62,62)])],{tag(category(\"MetaVariable\"))})", Factory.Production);
  private static final IConstructor prod__constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"constructor\",sort(\"Assignable\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__callOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"callOrTree\",sort(\"Pattern\")),[label(\"expression\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_ = (IConstructor) _read("prod(label(\"parameter\",sort(\"Sym\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"nonterminal\",lex(\"Nonterminal\"))],{})", Factory.Production);
  private static final IConstructor prod__type_$BasicType__lit_type_ = (IConstructor) _read("prod(label(\"type\",sort(\"BasicType\")),[lit(\"type\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_start_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"start\")],{})", Factory.Production);
  private static final IConstructor regular__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(seq([conditional(\\char-class([range(65,90),range(95,95),range(97,122)]),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})]))", Factory.Production);
  private static final IConstructor prod__lit_outermost__char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"outermost\"),[\\char-class([range(111,111)]),\\char-class([range(117,117)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_ = (IConstructor) _read("prod(label(\"descendant\",sort(\"Pattern\")),[lit(\"/\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__default_$StructuredType__basicType_$BasicType_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_arguments_iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"default\",sort(\"StructuredType\")),[label(\"basicType\",sort(\"BasicType\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"join\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"join\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__default_$FunctionModifier__lit_default_ = (IConstructor) _read("prod(label(\"default\",sort(\"FunctionModifier\")),[lit(\"default\")],{})", Factory.Production);
  private static final IConstructor prod__intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"intersection\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__empty_$Label__ = (IConstructor) _read("prod(label(\"empty\",sort(\"Label\")),[],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_test_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__lit___60_$Name_lit___62_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"DatePart\"),[\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),\\char-class([range(48,51)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_ = (IConstructor) _read("prod(lit(\"&&\"),[\\char-class([range(38,38)]),\\char-class([range(38,38)])],{})", Factory.Production);
  private static final IConstructor prod__labeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"labeled\",sort(\"Prod\")),[label(\"modifiers\",\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"args\",\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__ifThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"ifThenElse\",sort(\"StringTemplate\")),[lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStatsThen\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"thenString\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStatsThen\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"else\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStatsElse\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"elseString\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStatsElse\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__import_$EvalCommand__imported_$Import_ = (IConstructor) _read("prod(label(\"import\",sort(\"EvalCommand\")),[label(\"imported\",sort(\"Import\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Statement__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__real_$BasicType__lit_real_ = (IConstructor) _read("prod(label(\"real\",sort(\"BasicType\")),[lit(\"real\")],{})", Factory.Production);
  private static final IConstructor prod__$BooleanLiteral__lit_false_ = (IConstructor) _read("prod(lex(\"BooleanLiteral\"),[lit(\"false\")],{})", Factory.Production);
  private static final IConstructor prod__int_$BasicType__lit_int_ = (IConstructor) _read("prod(label(\"int\",sort(\"BasicType\")),[lit(\"int\")],{})", Factory.Production);
  private static final IConstructor prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_ = (IConstructor) _read("prod(lex(\"URLChars\"),[\\iter-star(\\char-class([range(0,8),range(11,12),range(14,31),range(33,59),range(61,123),range(125,16777215)]))],{})", Factory.Production);
  private static final IConstructor prod__reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"reducer\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"init\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"result\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"FunctionModifier\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"alternative\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"alternatives\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__left_$Assoc__lit_left_ = (IConstructor) _read("prod(label(\"left\",sort(\"Assoc\")),[lit(\"left\")],{})", Factory.Production);
  private static final IConstructor prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"&=\"),[\\char-class([range(38,38)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__post_$ProtocolTail__post_$PostProtocolChars_ = (IConstructor) _read("prod(label(\"post\",sort(\"ProtocolTail\")),[label(\"post\",lex(\"PostProtocolChars\"))],{})", Factory.Production);
  private static final IConstructor prod__modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_mod_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"modulo\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"mod\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__default_$Bound__lit___59_$layouts_LAYOUTLIST_expression_$Expression_ = (IConstructor) _read("prod(label(\"default\",sort(\"Bound\")),[lit(\";\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__endOfLine_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___36_ = (IConstructor) _read("prod(label(\"endOfLine\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"$\")],{})", Factory.Production);
  private static final IConstructor prod__transitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_ = (IConstructor) _read("prod(label(\"transitiveReflexiveClosure\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"*\"),{\\not-follow(lit(\"=\"))})],{})", Factory.Production);
  private static final IConstructor prod__module_$Kind__lit_module_ = (IConstructor) _read("prod(label(\"module\",sort(\"Kind\")),[lit(\"module\")],{})", Factory.Production);
  private static final IConstructor prod__nonInterpolated_$StringLiteral__constant_$StringConstant_ = (IConstructor) _read("prod(label(\"nonInterpolated\",sort(\"StringLiteral\")),[label(\"constant\",lex(\"StringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_break_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"break\")],{})", Factory.Production);
  private static final IConstructor prod__subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"subscript\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"subscripts\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__dateAndTimeLiteral_$DateTimeLiteral__dateAndTime_$DateAndTime_ = (IConstructor) _read("prod(label(\"dateAndTimeLiteral\",sort(\"DateTimeLiteral\")),[label(\"dateAndTime\",lex(\"DateAndTime\"))],{})", Factory.Production);
  private static final IConstructor prod__lessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"lessThan\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\<\"),{\\not-follow(lit(\"-\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__appendAfter_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60_60__not_follow__lit___61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"appendAfter\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\<\\<\"),{\\not-follow(lit(\"=\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_real_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"real\")],{})", Factory.Production);
  private static final IConstructor prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"!=\"),[\\char-class([range(33,33)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__default_$FunctionBody__lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"default\",sort(\"FunctionBody\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__interpolated_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_ = (IConstructor) _read("prod(label(\"interpolated\",sort(\"StringLiteral\")),[label(\"pre\",lex(\"PreStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__default_$Module__header_$Header_$layouts_LAYOUTLIST_body_$Body_ = (IConstructor) _read("prod(label(\"default\",sort(\"Module\")),[label(\"header\",sort(\"Header\")),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Body\"))],{})", Factory.Production);
  private static final IConstructor prod__$StringConstant__lit___34_iter_star__$StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"StringConstant\"),[lit(\"\\\"\"),\\iter-star(lex(\"StringCharacter\")),lit(\"\\\"\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__notIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"notIn\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"notin\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__$PreProtocolChars__lit___124_$URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"PreProtocolChars\"),[lit(\"|\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(92,92)]),\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"anno\"),[\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(110,110)]),\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__public_$Visibility__lit_public_ = (IConstructor) _read("prod(label(\"public\",sort(\"Visibility\")),[lit(\"public\")],{})", Factory.Production);
  private static final IConstructor prod__lit_top_down__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"top-down\"),[\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(100,100)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit___42_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"product\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"*\"),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(lit(\"*\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_ = (IConstructor) _read("prod(lit(\"...\"),[\\char-class([range(46,46)]),\\char-class([range(46,46)]),\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__lit_top_down_break__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"top-down-break\"),[\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(100,100)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(110,110)]),\\char-class([range(45,45)]),\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_join_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"join\")],{})", Factory.Production);
  private static final IConstructor prod__$Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[lit(\"\\\\\"),\\char-class([range(32,32),range(34,34),range(39,39),range(45,45),range(60,60),range(62,62),range(91,93),range(98,98),range(102,102),range(110,110),range(114,114),range(116,116)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__view_$Kind__lit_view_ = (IConstructor) _read("prod(label(\"view\",sort(\"Kind\")),[lit(\"view\")],{})", Factory.Production);
  private static final IConstructor prod__lit_view__char_class___range__118_118_char_class___range__105_105_char_class___range__101_101_char_class___range__119_119_ = (IConstructor) _read("prod(lit(\"view\"),[\\char-class([range(118,118)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(119,119)])],{})", Factory.Production);
  private static final IConstructor prod__lit_innermost__char_class___range__105_105_char_class___range__110_110_char_class___range__110_110_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"innermost\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(110,110)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__test_$ShellCommand__lit_test_ = (IConstructor) _read("prod(label(\"test\",sort(\"ShellCommand\")),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__lit_one__char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"one\"),[\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Renaming\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement__tag__breakable = (IConstructor) _read("prod(label(\"assignment\",sort(\"Statement\")),[label(\"assignable\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),label(\"operator\",sort(\"Assignment\")),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__while_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable = (IConstructor) _read("prod(label(\"while\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_import_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"import\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$ProtocolChars__char_class___range__124_124_$URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_ = (IConstructor) _read("prod(lex(\"ProtocolChars\"),[\\char-class([range(124,124)]),lex(\"URLChars\"),conditional(lit(\"://\"),{\\not-follow(\\char-class([range(9,10),range(13,13),range(32,32)]))})],{})", Factory.Production);
  private static final IConstructor prod__$CaseInsensitiveStringConstant__lit___39_iter_star__$StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"CaseInsensitiveStringConstant\"),[lit(\"\\'\\\\\"),\\iter-star(lex(\"StringCharacter\")),lit(\"\\'\\\\\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__toplevels_$Body__toplevels_iter_star_seps__$Toplevel__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"toplevels\",sort(\"Body\")),[label(\"toplevels\",\\iter-star-seps(sort(\"Toplevel\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__ifDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_ = (IConstructor) _read("prod(label(\"ifDefinedOrDefault\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"defaultExpression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"while\"),[\\char-class([range(119,119)]),\\char-class([range(104,104)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"notin\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__iter_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___43_ = (IConstructor) _read("prod(label(\"iter\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"+\")],{})", Factory.Production);
  private static final IConstructor prod__bottomUpBreak_$Strategy__lit_bottom_up_break_ = (IConstructor) _read("prod(label(\"bottomUpBreak\",sort(\"Strategy\")),[lit(\"bottom-up-break\")],{})", Factory.Production);
  private static final IConstructor prod__list_$Commands__commands_iter_seps__$EvalCommand__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"list\",sort(\"Commands\")),[label(\"commands\",\\iter-seps(sort(\"EvalCommand\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__default_$ModuleParameters__lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"default\",sort(\"ModuleParameters\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"TypeVar\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_ = (IConstructor) _read("prod(label(\"optional\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"?\")],{})", Factory.Production);
  private static final IConstructor prod__default_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_body_$FunctionBody__tag__Foldable = (IConstructor) _read("prod(label(\"default\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"FunctionBody\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__absent_$Start__ = (IConstructor) _read("prod(label(\"absent\",sort(\"Start\")),[],{})", Factory.Production);
  private static final IConstructor regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))])))", Factory.Production);
  private static final IConstructor prod__switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable = (IConstructor) _read("prod(label(\"switch\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"switch\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__remainder_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"remainder\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"%\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalReservedKeywords_not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"Nonterminal\"),[conditional(\\char-class([range(65,90)]),{\\not-precede(\\char-class([range(65,90)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),delete(sort(\"RascalReservedKeywords\"))})],{})", Factory.Production);
  private static final IConstructor prod__ifDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"ifDefinedOtherwise\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__bag_$BasicType__lit_bag_ = (IConstructor) _read("prod(label(\"bag\",sort(\"BasicType\")),[lit(\"bag\")],{})", Factory.Production);
  private static final IConstructor prod__unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left = (IConstructor) _read("prod(label(\"unequal\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\\\\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_void_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__noMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"noMatch\",sort(\"Expression\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"!:=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__getAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"getAnnotation\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__tuple_$BasicType__lit_tuple_ = (IConstructor) _read("prod(label(\"tuple\",sort(\"BasicType\")),[lit(\"tuple\")],{})", Factory.Production);
  private static final IConstructor prod__expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"expression\",sort(\"Statement\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__doWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"doWhile\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"do\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__first_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left = (IConstructor) _read("prod(label(\"first\",sort(\"Prod\")),[label(\"lhs\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\>\"),{\\not-follow(lit(\"\\>\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Prod\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"continue\",sort(\"Statement\")),[lit(\"continue\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__string_$Literal__stringLiteral_$StringLiteral_ = (IConstructor) _read("prod(label(\"string\",sort(\"Literal\")),[label(\"stringLiteral\",sort(\"StringLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__transitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_ = (IConstructor) _read("prod(label(\"transitiveClosure\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"+\"),{\\not-follow(lit(\"=\"))})],{})", Factory.Production);
  private static final IConstructor prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"default\"),[\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"bool\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(111,111)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__for_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable___123_103_101_110_101_114_97_116_111_114_115_125_tag__breakable = (IConstructor) _read("prod(label(\"for\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"for\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{tag(breakable(\"{generators}\")),tag(breakable())})", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102 = (IConstructor) _read("regular(opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)])))", Factory.Production);
  private static final IConstructor prod__literal_$Sym__string_$StringConstant_ = (IConstructor) _read("prod(label(\"literal\",sort(\"Sym\")),[label(\"string\",lex(\"StringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__others_$Prod__lit___46_46_46_ = (IConstructor) _read("prod(label(\"others\",sort(\"Prod\")),[lit(\"...\")],{})", Factory.Production);
  private static final IConstructor prod__notFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left = (IConstructor) _read("prod(label(\"notFollow\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Field\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__real_$Literal__realLiteral_$RealLiteral_ = (IConstructor) _read("prod(label(\"real\",sort(\"Literal\")),[label(\"realLiteral\",lex(\"RealLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__$TimeZonePart__lit_Z_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[lit(\"Z\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_default_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"default\")],{})", Factory.Production);
  private static final IConstructor prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(49,57)]),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(114,114)]),\\char-class([range(48,57)]),conditional(\\iter-star(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_layout_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"layout\")],{})", Factory.Production);
  private static final IConstructor prod__splice_$Expression__lit___42_$layouts_LAYOUTLIST_argument_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"splice\",sort(\"Expression\")),[lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right = (IConstructor) _read("prod(label(\"precede\",sort(\"Sym\")),[label(\"match\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\<\\<\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__lit_variable__char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"variable\"),[\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(97,97)]),\\char-class([range(98,98)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__boolean_$Literal__booleanLiteral_$BooleanLiteral_ = (IConstructor) _read("prod(label(\"boolean\",sort(\"Literal\")),[label(\"booleanLiteral\",lex(\"BooleanLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___60_61_61_62__char_class___range__60_60_char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\<==\\>\"),[\\char-class([range(60,60)]),\\char-class([range(61,61)]),\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[\\char-class([range(92,92)]),\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__varArgs_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___46_46_46_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"varArgs\",sort(\"Parameters\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"formals\",sort(\"Formals\")),layouts(\"LAYOUTLIST\"),lit(\"...\"),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_fail_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"fail\")],{})", Factory.Production);
  private static final IConstructor prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"str\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_ = (IConstructor) _read("prod(label(\"default\",sort(\"Catch\")),[lit(\"catch\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__statement_$Command__statement_$Statement_ = (IConstructor) _read("prod(label(\"statement\",sort(\"Command\")),[label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__$Rest__conditional__iter_star__char_class___range__0_16777215__not_follow__char_class___range__0_16777215_ = (IConstructor) _read("prod(lex(\"Rest\"),[conditional(\\iter-star(\\char-class([range(0,16777215)])),{\\not-follow(\\char-class([range(0,16777215)]))})],{})", Factory.Production);
  private static final IConstructor prod__edit_$ShellCommand__lit_edit_$layouts_LAYOUTLIST_name_$QualifiedName_ = (IConstructor) _read("prod(label(\"edit\",sort(\"ShellCommand\")),[lit(\"edit\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__except_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_$layouts_LAYOUTLIST_label_$NonterminalLabel_ = (IConstructor) _read("prod(label(\"except\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"label\",lex(\"NonterminalLabel\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_solve_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"solve\")],{})", Factory.Production);
  private static final IConstructor prod__splicePlus_$Pattern__lit___43_$layouts_LAYOUTLIST_argument_$Pattern_ = (IConstructor) _read("prod(label(\"splicePlus\",sort(\"Pattern\")),[lit(\"+\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])))", Factory.Production);
  private static final IConstructor prod__$NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"NonterminalLabel\"),[\\char-class([range(97,122)]),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__data_$Kind__lit_data_ = (IConstructor) _read("prod(label(\"data\",sort(\"Kind\")),[lit(\"data\")],{})", Factory.Production);
  private static final IConstructor prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"=\\>\"),[\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"==\"),[\\char-class([range(61,61)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[\\char-class([range(0,46),range(48,59),range(61,61),range(63,91),range(93,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__startOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_ = (IConstructor) _read("prod(label(\"startOfLine\",sort(\"Sym\")),[lit(\"^\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{})", Factory.Production);
  private static final IConstructor prod__asType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_ = (IConstructor) _read("prod(label(\"asType\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"]\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___62_61__char_class___range__62_62_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\>=\"),[\\char-class([range(62,62)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Folded_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"default\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"contents\",lex(\"TagString\"))],{tag(Folded()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"sequence\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sequence\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__$BooleanLiteral__lit_true_ = (IConstructor) _read("prod(lex(\"BooleanLiteral\"),[lit(\"true\")],{})", Factory.Production);
  private static final IConstructor prod__intersection_$Assignment__lit___38_61_ = (IConstructor) _read("prod(label(\"intersection\",sort(\"Assignment\")),[lit(\"&=\")],{})", Factory.Production);
  private static final IConstructor prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"non-assoc\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(45,45)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"enumerator\",sort(\"Expression\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"\\<-\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__$LAYOUT__char_class___range__9_10_range__13_13_range__32_32_ = (IConstructor) _read("prod(lex(\"LAYOUT\"),[\\char-class([range(9,10),range(13,13),range(32,32)])],{})", Factory.Production);
  private static final IConstructor prod__conditional_$Replacement__replacementExpression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"conditional\",sort(\"Replacement\")),[label(\"replacementExpression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"when\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__relation_$BasicType__lit_rel_ = (IConstructor) _read("prod(label(\"relation\",sort(\"BasicType\")),[lit(\"rel\")],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),lit(\".\"),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"private\"),[\\char-class([range(112,112)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_num_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"num\")],{})", Factory.Production);
  private static final IConstructor prod__language_$SyntaxDefinition__start_$Start_$layouts_LAYOUTLIST_lit_syntax_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"language\",sort(\"SyntaxDefinition\")),[label(\"start\",sort(\"Start\")),layouts(\"LAYOUTLIST\"),lit(\"syntax\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)])],{})", Factory.Production);
  private static final IConstructor prod__help_$ShellCommand__lit_help_ = (IConstructor) _read("prod(label(\"help\",sort(\"ShellCommand\")),[lit(\"help\")],{})", Factory.Production);
  private static final IConstructor prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"any\"),[\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__parameters_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_params_$ModuleParameters_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"parameters\",sort(\"Header\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),lit(\"module\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"params\",sort(\"ModuleParameters\")),layouts(\"LAYOUTLIST\"),label(\"imports\",\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"join\"),[\\char-class([range(106,106)]),\\char-class([range(111,111)]),\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_bracket_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bracket\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_continue_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"continue\")],{})", Factory.Production);
  private static final IConstructor prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_ = (IConstructor) _read("prod(lit(\"||\"),[\\char-class([range(124,124)]),\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\<=\"),[\\char-class([range(60,60)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"finally\"),[\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(108,108)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__stepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"stepRange\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"second\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"..\"),layouts(\"LAYOUTLIST\"),label(\"last\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__$PostPathChars__lit___62_$URLChars_lit___124_ = (IConstructor) _read("prod(lex(\"PostPathChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"|\")],{})", Factory.Production);
  private static final IConstructor prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\<\\<\"),[\\char-class([range(60,60)]),\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__location_$Literal__locationLiteral_$LocationLiteral_ = (IConstructor) _read("prod(label(\"location\",sort(\"Literal\")),[label(\"locationLiteral\",sort(\"LocationLiteral\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star__$RegExp = (IConstructor) _read("regular(\\iter-star(lex(\"RegExp\")))", Factory.Production);
  private static final IConstructor prod__has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"has\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"has\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__it_$Expression__conditional__lit_it__not_precede__char_class___range__65_90_range__95_95_range__97_122_not_follow__char_class___range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(label(\"it\",sort(\"Expression\")),[conditional(lit(\"it\"),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)])),\\not-follow(\\char-class([range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_one_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"one\")],{})", Factory.Production);
  private static final IConstructor prod__variable_$Kind__lit_variable_ = (IConstructor) _read("prod(label(\"variable\",sort(\"Kind\")),[lit(\"variable\")],{})", Factory.Production);
  private static final IConstructor prod__unlabeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"unlabeled\",sort(\"Prod\")),[label(\"modifiers\",\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"args\",\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_node_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"node\")],{})", Factory.Production);
  private static final IConstructor prod__innermost_$Strategy__lit_innermost_ = (IConstructor) _read("prod(label(\"innermost\",sort(\"Strategy\")),[lit(\"innermost\")],{})", Factory.Production);
  private static final IConstructor prod__default_$Label__name_$Name_$layouts_LAYOUTLIST_lit___58_ = (IConstructor) _read("prod(label(\"default\",sort(\"Label\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\")],{})", Factory.Production);
  private static final IConstructor prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"import\"),[\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"else\"),[\\char-class([range(101,101)]),\\char-class([range(108,108)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable = (IConstructor) _read("prod(label(\"insert\",sort(\"Statement\")),[lit(\"insert\"),layouts(\"LAYOUTLIST\"),label(\"dataTarget\",sort(\"DataTarget\")),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc()),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__lit_global__char_class___range__103_103_char_class___range__108_108_char_class___range__111_111_char_class___range__98_98_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"global\"),[\\char-class([range(103,103)]),\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(98,98)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"start\",sort(\"Sym\")),[lit(\"start\"),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"nonterminal\",lex(\"Nonterminal\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__hexIntegerLiteral_$IntegerLiteral__hex_$HexIntegerLiteral_ = (IConstructor) _read("prod(label(\"hexIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"hex\",lex(\"HexIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215 = (IConstructor) _read("regular(\\iter-star(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,16777215)])})))", Factory.Production);
  private static final IConstructor prod__ifThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else__tag__breakable = (IConstructor) _read("prod(label(\"ifThen\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"thenStatement\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(lit(\"else\"))})],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__asType_$Pattern__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Pattern_ = (IConstructor) _read("prod(label(\"asType\",sort(\"Pattern\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"]\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__$BasicType_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[sort(\"BasicType\")],{})", Factory.Production);
  private static final IConstructor prod__splice_$Pattern__lit___42_$layouts_LAYOUTLIST_argument_$Pattern_ = (IConstructor) _read("prod(label(\"splice\",sort(\"Pattern\")),[lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125 = (IConstructor) _read("regular(\\iter-star(alt({lex(\"TagString\"),\\char-class([range(0,122),range(124,124),range(126,16777215)]),seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])])})))", Factory.Production);
  private static final IConstructor prod__composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"composition\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"o\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__symbol_$Type__symbol_$Sym_ = (IConstructor) _read("prod(label(\"symbol\",sort(\"Type\")),[label(\"symbol\",sort(\"Sym\"))],{})", Factory.Production);
  private static final IConstructor prod__utf16_$UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(label(\"utf16\",lex(\"UnicodeEscape\")),[lit(\"\\\\\"),\\char-class([range(117,117)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__$PostProtocolChars__lit___62_$URLChars_lit___58_47_47_ = (IConstructor) _read("prod(lex(\"PostProtocolChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"://\")],{})", Factory.Production);
  private static final IConstructor prod__associative_$Assoc__lit_assoc_ = (IConstructor) _read("prod(label(\"associative\",sort(\"Assoc\")),[lit(\"assoc\")],{})", Factory.Production);
  private static final IConstructor prod__empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"empty\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__rational_$Literal__rationalLiteral_$RationalLiteral_ = (IConstructor) _read("prod(label(\"rational\",sort(\"Literal\")),[label(\"rationalLiteral\",lex(\"RationalLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__nonInterpolated_$PathPart__pathChars_$PathChars_ = (IConstructor) _read("prod(label(\"nonInterpolated\",sort(\"PathPart\")),[label(\"pathChars\",lex(\"PathChars\"))],{})", Factory.Production);
  private static final IConstructor prod__map_$BasicType__lit_map_ = (IConstructor) _read("prod(label(\"map\",sort(\"BasicType\")),[lit(\"map\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\":\"),\\iter-star(lex(\"NamedRegExp\")),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_loc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"loc\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star__$NamedRegExp = (IConstructor) _read("regular(\\iter-star(lex(\"NamedRegExp\")))", Factory.Production);
  private static final IConstructor prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"keyword\"),[\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(121,121)]),\\char-class([range(119,119)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__addition_$Assignment__lit___43_61_ = (IConstructor) _read("prod(label(\"addition\",sort(\"Assignment\")),[lit(\"+=\")],{})", Factory.Production);
  private static final IConstructor prod__doWhile_$StringTemplate__lit_do_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"doWhile\",sort(\"StringTemplate\")),[lit(\"do\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(0,46),range(48,59),range(61,61),range(63,91),range(93,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_ = (IConstructor) _read("prod(lit(\"syntax\"),[\\char-class([range(115,115)]),\\char-class([range(121,121)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(120,120)])],{})", Factory.Production);
  private static final IConstructor prod__anno_$Kind__lit_anno_ = (IConstructor) _read("prod(label(\"anno\",sort(\"Kind\")),[lit(\"anno\")],{})", Factory.Production);
  private static final IConstructor prod__variableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_ = (IConstructor) _read("prod(label(\"variableBecomes\",sort(\"Pattern\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"all\"),[\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__reifyType_$Expression__lit___35_$layouts_LAYOUTLIST_conditional__type_$Type__not_follow__lit___91_ = (IConstructor) _read("prod(label(\"reifyType\",sort(\"Expression\")),[lit(\"#\"),layouts(\"LAYOUTLIST\"),conditional(label(\"type\",sort(\"Type\")),{\\not-follow(lit(\"[\"))})],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_map_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"map\")],{})", Factory.Production);
  private static final IConstructor prod__user_$Type__conditional__user_$UserType__delete__$HeaderKeyword_ = (IConstructor) _read("prod(label(\"user\",sort(\"Type\")),[conditional(label(\"user\",sort(\"UserType\")),{delete(keywords(\"HeaderKeyword\"))})],{})", Factory.Production);
  private static final IConstructor prod__subscript_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscript_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"subscript\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"subscript\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\>\\>\"),[\\char-class([range(62,62)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_private_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"private\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_module_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"module\")],{})", Factory.Production);
  private static final IConstructor prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"try\"),[\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__lit___63_61__char_class___range__63_63_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"?=\"),[\\char-class([range(63,63)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_ = (IConstructor) _read("prod(label(\"column\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"column\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_throw_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"throw\")],{})", Factory.Production);
  private static final IConstructor prod__template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_ = (IConstructor) _read("prod(label(\"template\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringMiddle\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_bottom_up__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"bottom-up\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(45,45)]),\\char-class([range(117,117)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"is\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"is\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"break\"),[\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__$layouts_LAYOUTLIST__conditional__iter_star__$LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_ = (IConstructor) _read("prod(layouts(\"LAYOUTLIST\"),[conditional(\\iter-star(lex(\"LAYOUT\")),{\\not-follow(\\char-class([range(9,10),range(13,13),range(32,32)])),\\not-follow(lit(\"//\")),\\not-follow(lit(\"/*\"))})],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_mod_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"mod\")],{})", Factory.Production);
  private static final IConstructor prod__mid_$ProtocolTail__mid_$MidProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_ = (IConstructor) _read("prod(label(\"mid\",sort(\"ProtocolTail\")),[label(\"mid\",lex(\"MidProtocolChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"ProtocolTail\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_rat_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"rat\")],{})", Factory.Production);
  private static final IConstructor prod__typedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_ = (IConstructor) _read("prod(label(\"typedVariableBecomes\",sort(\"Pattern\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Range__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Range\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__associativityGroup_$Prod__associativity_$Assoc_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_group_$Prod_$layouts_LAYOUTLIST_lit___41__tag__Foldable = (IConstructor) _read("prod(label(\"associativityGroup\",sort(\"Prod\")),[label(\"associativity\",sort(\"Assoc\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"group\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__$MidPathChars__lit___62_$URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"MidPathChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"true\"),[\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Sym__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_rel_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"rel\")],{})", Factory.Production);
  private static final IConstructor prod__selector_$DataTypeSelector__sort_$QualifiedName_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_production_$Name_ = (IConstructor) _read("prod(label(\"selector\",sort(\"DataTypeSelector\")),[label(\"sort\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"production\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"extend\"),[\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"append\"),[\\char-class([range(97,97)]),\\char-class([range(112,112)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),conditional(lit(\".\"),{\\not-follow(lit(\".\"))}),\\iter-star(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"assoc\"),[\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_bool_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bool\")],{})", Factory.Production);
  private static final IConstructor prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"map\"),[\\char-class([range(109,109)]),\\char-class([range(97,97)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"real\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)])))", Factory.Production);
  private static final IConstructor prod__nonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"nonEmptyBlock\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"Class\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"charclass\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__iterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"iterStarSep\",sort(\"Sym\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sep\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_datetime_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"datetime\")],{})", Factory.Production);
  private static final IConstructor prod__greaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"greaterThanOrEq\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__arbitrary_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement_ = (IConstructor) _read("prod(label(\"arbitrary\",sort(\"PatternWithAction\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__function_$Kind__lit_function_ = (IConstructor) _read("prod(label(\"function\",sort(\"Kind\")),[lit(\"function\")],{})", Factory.Production);
  private static final IConstructor prod__tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"tuple\",sort(\"Expression\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[\\char-class([range(0,31),range(33,33),range(35,38),range(40,44),range(46,59),range(61,61),range(63,90),range(94,16777215)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"void\"),[\\char-class([range(118,118)]),\\char-class([range(111,111)]),\\char-class([range(105,105)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[conditional(lit(\".\"),{\\not-precede(\\char-class([range(46,46)]))}),iter(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_set_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"set\")],{})", Factory.Production);
  private static final IConstructor prod__and_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"and\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"&&\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__free_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"free\",sort(\"TypeVar\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__ifDefined_$Assignment__lit___63_61_ = (IConstructor) _read("prod(label(\"ifDefined\",sort(\"Assignment\")),[lit(\"?=\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_return_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"return\")],{})", Factory.Production);
  private static final IConstructor prod__voidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"voidClosure\",sort(\"Expression\")),[label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__empty__ = (IConstructor) _read("prod(empty(),[],{})", Factory.Production);
  private static final IConstructor prod__list_$FunctionModifiers__modifiers_iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"list\",sort(\"FunctionModifiers\")),[label(\"modifiers\",\\iter-star-seps(sort(\"FunctionModifier\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__selector_$Type__selector_$DataTypeSelector_ = (IConstructor) _read("prod(label(\"selector\",sort(\"Type\")),[label(\"selector\",sort(\"DataTypeSelector\"))],{})", Factory.Production);
  private static final IConstructor prod__initialized_$Variable__name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_initial_$Expression_ = (IConstructor) _read("prod(label(\"initialized\",sort(\"Variable\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"initial\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_str_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"str\")],{})", Factory.Production);
  private static final IConstructor prod__all_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left = (IConstructor) _read("prod(label(\"all\",sort(\"Prod\")),[label(\"lhs\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Prod\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_ = (IConstructor) _read("prod(lit(\"switch\"),[\\char-class([range(115,115)]),\\char-class([range(119,119)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(99,99)]),\\char-class([range(104,104)])],{})", Factory.Production);
  private static final IConstructor prod__named_$TypeArg__type_$Type_$layouts_LAYOUTLIST_name_$Name_ = (IConstructor) _read("prod(label(\"named\",sort(\"TypeArg\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left = (IConstructor) _read("prod(label(\"follow\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"solve\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(108,108)]),\\char-class([range(118,118)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__default_$Mapping__$Pattern__from_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Pattern_ = (IConstructor) _read("prod(label(\"default\",\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")])),[label(\"from\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"layout\"),[\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(121,121)]),\\char-class([range(111,111)]),\\char-class([range(117,117)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))", Factory.Production);
  private static final IConstructor prod__empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Folded_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"empty\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{tag(Folded()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_ = (IConstructor) _read("prod(label(\"annotation\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"annotation\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_extend_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"extend\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Catch__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__character_$Range__character_$Char_ = (IConstructor) _read("prod(label(\"character\",sort(\"Range\")),[label(\"character\",lex(\"Char\"))],{})", Factory.Production);
  private static final IConstructor prod__tag_$ProdModifier__tag_$Tag_ = (IConstructor) _read("prod(label(\"tag\",sort(\"ProdModifier\")),[label(\"tag\",sort(\"Tag\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_tag_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"tag\")],{})", Factory.Production);
  private static final IConstructor prod__replacing_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_replacement_$Replacement_ = (IConstructor) _read("prod(label(\"replacing\",sort(\"PatternWithAction\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"=\\>\"),layouts(\"LAYOUTLIST\"),label(\"replacement\",sort(\"Replacement\"))],{})", Factory.Production);
  private static final IConstructor prod__union_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Class__assoc__left = (IConstructor) _read("prod(label(\"union\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"||\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor regular__iter_star__$StringCharacter = (IConstructor) _read("regular(\\iter-star(lex(\"StringCharacter\")))", Factory.Production);
  private static final IConstructor prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[\\char-class([range(10,10)]),\\iter-star(\\char-class([range(9,9),range(32,32)])),\\char-class([range(39,39)])],{})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_keyword_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"keyword\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_list_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"list\")],{})", Factory.Production);
  private static final IConstructor prod__iterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_ = (IConstructor) _read("prod(label(\"iterSep\",sort(\"Sym\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sep\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"+\")],{})", Factory.Production);
  private static final IConstructor prod__rational_$BasicType__lit_rat_ = (IConstructor) _read("prod(label(\"rational\",sort(\"BasicType\")),[lit(\"rat\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$EvalCommand__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"EvalCommand\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_public_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"public\")],{})", Factory.Production);
  private static final IConstructor prod__bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__fieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_ = (IConstructor) _read("prod(label(\"fieldAccess\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"field\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__$layouts_$default$__ = (IConstructor) _read("prod(layouts(\"$default$\"),[],{})", Factory.Production);
  private static final IConstructor prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"assert\"),[\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_58__char_class___range__60_60_char_class___range__58_58_ = (IConstructor) _read("prod(lit(\"\\<:\"),[\\char-class([range(60,60)]),\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__bracket_$ProdModifier__lit_bracket_ = (IConstructor) _read("prod(label(\"bracket\",sort(\"ProdModifier\")),[lit(\"bracket\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__nonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"nonEmptyBlock\",sort(\"Expression\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"lexical\"),[\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(105,105)]),\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__name_$UserType__name_$QualifiedName_ = (IConstructor) _read("prod(label(\"name\",sort(\"UserType\")),[label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__lit___60_45__char_class___range__60_60_char_class___range__45_45_ = (IConstructor) _read("prod(lit(\"\\<-\"),[\\char-class([range(60,60)]),\\char-class([range(45,45)])],{})", Factory.Production);
  private static final IConstructor prod__expression_$Command__expression_$Expression_ = (IConstructor) _read("prod(label(\"expression\",sort(\"Command\")),[label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__syntax_$Import__syntax_$SyntaxDefinition_ = (IConstructor) _read("prod(label(\"syntax\",sort(\"Import\")),[label(\"syntax\",sort(\"SyntaxDefinition\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"filter\"),[\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__java_$FunctionModifier__lit_java_ = (IConstructor) _read("prod(label(\"java\",sort(\"FunctionModifier\")),[lit(\"java\")],{})", Factory.Production);
  private static final IConstructor prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimePartNoTZ\"),[\\char-class([range(48,50)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))],{})", Factory.Production);
  private static final IConstructor prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"start\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58_58__char_class___range__58_58_char_class___range__58_58_ = (IConstructor) _read("prod(lit(\"::\"),[\\char-class([range(58,58)]),\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__$Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"Backslash\"),[conditional(\\char-class([range(92,92)]),{\\not-follow(\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)]))})],{})", Factory.Production);
  private static final IConstructor prod__lit___58_61__char_class___range__58_58_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\":=\"),[\\char-class([range(58,58)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_throws_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"throws\")],{})", Factory.Production);
  private static final IConstructor prod__bottomUp_$Strategy__lit_bottom_up_ = (IConstructor) _read("prod(label(\"bottomUp\",sort(\"Strategy\")),[lit(\"bottom-up\")],{})", Factory.Production);
  private static final IConstructor prod__map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"map\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"mappings\",\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Expression\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__private_$Visibility__lit_private_ = (IConstructor) _read("prod(label(\"private\",sort(\"Visibility\")),[lit(\"private\")],{})", Factory.Production);
  private static final IConstructor prod__try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc_tag__breakable = (IConstructor) _read("prod(label(\"try\",sort(\"Statement\")),[lit(\"try\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),label(\"handlers\",\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")]))],{assoc(\\non-assoc()),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__default_$Assignment__lit___61_ = (IConstructor) _read("prod(label(\"default\",sort(\"Assignment\")),[lit(\"=\")],{})", Factory.Production);
  private static final IConstructor prod__interpolated_$PathPart__pre_$PrePathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_ = (IConstructor) _read("prod(label(\"interpolated\",sort(\"PathPart\")),[label(\"pre\",lex(\"PrePathChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"PathTail\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_switch_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"switch\")],{})", Factory.Production);
  private static final IConstructor prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"DatePart\"),[\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),lit(\"-\"),\\char-class([range(48,49)]),\\char-class([range(48,57)]),lit(\"-\"),\\char-class([range(48,51)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__$PostStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"PostStringChars\"),[\\char-class([range(62,62)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(34,34)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__anti_$Pattern__lit___33_$layouts_LAYOUTLIST_pattern_$Pattern_ = (IConstructor) _read("prod(label(\"anti\",sort(\"Pattern\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"for\"),[\\char-class([range(102,102)]),\\char-class([range(111,111)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_ = (IConstructor) _read("prod(label(\"negation\",sort(\"Expression\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__octalIntegerLiteral_$IntegerLiteral__octal_$OctalIntegerLiteral_ = (IConstructor) _read("prod(label(\"octalIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"octal\",lex(\"OctalIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_left__char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"left\"),[\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__for_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"for\",sort(\"StringTemplate\")),[lit(\"for\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimePartNoTZ\"),[\\char-class([range(48,50)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))],{})", Factory.Production);
  private static final IConstructor regular__empty = (IConstructor) _read("regular(empty())", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_9_range__11_16777215 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,9),range(11,16777215)])))", Factory.Production);
  private static final IConstructor prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"bracket\"),[\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(99,99)]),\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__nonterminal_$Sym__conditional__nonterminal_$Nonterminal__not_follow__lit___91_ = (IConstructor) _read("prod(label(\"nonterminal\",sort(\"Sym\")),[conditional(label(\"nonterminal\",lex(\"Nonterminal\")),{\\not-follow(lit(\"[\"))})],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,8),range(11,12),range(14,31),range(33,59),range(61,123),range(125,16777215)])))", Factory.Production);
  private static final IConstructor prod__reference_$Prod__lit___58_$layouts_LAYOUTLIST_referenced_$Name_ = (IConstructor) _read("prod(label(\"reference\",sort(\"Prod\")),[lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"referenced\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__fromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_ = (IConstructor) _read("prod(label(\"fromTo\",sort(\"Range\")),[label(\"start\",lex(\"Char\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"end\",lex(\"Char\"))],{})", Factory.Production);
  private static final IConstructor prod__ifThen_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"ifThen\",sort(\"StringTemplate\")),[lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__empty_$DataTarget__ = (IConstructor) _read("prod(label(\"empty\",sort(\"DataTarget\")),[],{})", Factory.Production);
  private static final IConstructor prod__test_$FunctionModifier__lit_test_ = (IConstructor) _read("prod(label(\"test\",sort(\"FunctionModifier\")),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__default_$Formals__formals_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"Formals\")),[label(\"formals\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__unInitialized_$Variable__name_$Name_ = (IConstructor) _read("prod(label(\"unInitialized\",sort(\"Variable\")),[label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_for_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"for\")],{})", Factory.Production);
  private static final IConstructor prod__$StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[lit(\"\\\\\"),\\char-class([range(34,34),range(39,39),range(60,60),range(62,62),range(92,92),range(98,98),range(102,102),range(110,110),range(114,114),range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"insert\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_it_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"it\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_in_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"in\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_else_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"else\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_if_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"if\")],{})", Factory.Production);
  private static final IConstructor prod__iterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"iterStar\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__lit_history__char_class___range__104_104_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"history\"),[\\char-class([range(104,104)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__listModules_$ShellCommand__lit_modules_ = (IConstructor) _read("prod(label(\"listModules\",sort(\"ShellCommand\")),[lit(\"modules\")],{})", Factory.Production);
  private static final IConstructor prod__external_$Import__lit_import_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_at_$LocationLiteral_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"external\",sort(\"Import\")),[lit(\"import\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"at\",sort(\"LocationLiteral\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"annotation\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"anno\"),layouts(\"LAYOUTLIST\"),label(\"annoType\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"onType\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__start__$EvalCommand__$layouts_LAYOUTLIST_top_$EvalCommand_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"EvalCommand\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"EvalCommand\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__$Char__$UnicodeEscape__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[lex(\"UnicodeEscape\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__characterClass_$Sym__charClass_$Class_ = (IConstructor) _read("prod(label(\"characterClass\",sort(\"Sym\")),[label(\"charClass\",sort(\"Class\"))],{})", Factory.Production);
  private static final IConstructor prod__layout_$SyntaxDefinition__vis_$Visibility_$layouts_LAYOUTLIST_lit_layout_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"layout\",sort(\"SyntaxDefinition\")),[label(\"vis\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"layout\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__list_$BasicType__lit_list_ = (IConstructor) _read("prod(label(\"list\",sort(\"BasicType\")),[lit(\"list\")],{})", Factory.Production);
  private static final IConstructor prod__associativity_$ProdModifier__associativity_$Assoc_ = (IConstructor) _read("prod(label(\"associativity\",sort(\"ProdModifier\")),[label(\"associativity\",sort(\"Assoc\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_type_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"type\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Expression\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__lit_undeclare__char_class___range__117_117_char_class___range__110_110_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"undeclare\"),[\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))])))", Factory.Production);
  private static final IConstructor prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_$TimeZonePart_ = (IConstructor) _read("prod(lex(\"DateAndTime\"),[lit(\"$\"),lex(\"DatePart\"),lit(\"T\"),lex(\"TimePartNoTZ\"),lex(\"TimeZonePart\")],{})", Factory.Production);
  private static final IConstructor prod__listDeclarations_$ShellCommand__lit_declarations_ = (IConstructor) _read("prod(label(\"listDeclarations\",sort(\"ShellCommand\")),[lit(\"declarations\")],{})", Factory.Production);
  private static final IConstructor prod__division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"division\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"/\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__post_$StringTail__post_$PostStringChars_ = (IConstructor) _read("prod(label(\"post\",sort(\"StringTail\")),[label(\"post\",lex(\"PostStringChars\"))],{})", Factory.Production);
  private static final IConstructor prod__actualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_ = (IConstructor) _read("prod(label(\"actualsRenaming\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"actuals\",sort(\"ModuleActuals\")),layouts(\"LAYOUTLIST\"),label(\"renamings\",sort(\"Renamings\"))],{})", Factory.Production);
  private static final IConstructor prod__comprehension_$Expression__comprehension_$Comprehension_ = (IConstructor) _read("prod(label(\"comprehension\",sort(\"Expression\")),[label(\"comprehension\",sort(\"Comprehension\"))],{})", Factory.Production);
  private static final IConstructor prod__emptyStatement_$Statement__lit___59_ = (IConstructor) _read("prod(label(\"emptyStatement\",sort(\"Statement\")),[lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__set_$Pattern__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"set\",sort(\"Pattern\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__nonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"nonEquals\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"!=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__simpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"simpleCharclass\",sort(\"Class\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"ranges\",\\iter-star-seps(sort(\"Range\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"rel\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__fieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"fieldProject\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"fields\",\\iter-seps(sort(\"Field\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__typeArguments_$FunctionType__type_$Type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"typeArguments\",sort(\"FunctionType\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__empty_$Bound__ = (IConstructor) _read("prod(label(\"empty\",sort(\"Bound\")),[],{})", Factory.Production);
  private static final IConstructor prod__lit___61_61_62__char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"==\\>\"),[\\char-class([range(61,61)]),\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable = (IConstructor) _read("prod(label(\"throw\",sort(\"Statement\")),[lit(\"throw\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc()),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"Type\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__all_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"all\",sort(\"Expression\")),[lit(\"all\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__parametrized_$Sym__conditional__nonterminal_$Nonterminal__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"parametrized\",sort(\"Sym\")),[conditional(label(\"nonterminal\",lex(\"Nonterminal\")),{follow(lit(\"[\"))}),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Case__$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__$PrePathChars__$URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"PrePathChars\"),[lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_syntax_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"syntax\")],{})", Factory.Production);
  private static final IConstructor prod__lexical_$SyntaxDefinition__lit_lexical_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"lexical\",sort(\"SyntaxDefinition\")),[lit(\"lexical\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"continue\"),[\\char-class([range(99,99)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"tag\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"tag\"),layouts(\"LAYOUTLIST\"),label(\"kind\",sort(\"Kind\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"on\"),layouts(\"LAYOUTLIST\"),label(\"types\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__lit___60_60_61__char_class___range__60_60_char_class___range__60_60_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\<\\<=\"),[\\char-class([range(60,60)]),\\char-class([range(60,60)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__$LAYOUT__$Comment_ = (IConstructor) _read("prod(lex(\"LAYOUT\"),[lex(\"Comment\")],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_tuple_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"tuple\")],{})", Factory.Production);
  private static final IConstructor prod__map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"map\",sort(\"Pattern\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"mappings\",\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__setAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"setAnnotation\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"value\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_lexical_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"lexical\")],{})", Factory.Production);
  private static final IConstructor prod__interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_ = (IConstructor) _read("prod(label(\"interpolated\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringMiddle\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"dynamic\"),[\\char-class([range(100,100)]),\\char-class([range(121,121)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(105,105)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__withThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit_throws_$layouts_LAYOUTLIST_exceptions_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"withThrows\",sort(\"Signature\")),[label(\"modifiers\",sort(\"FunctionModifiers\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"throws\"),layouts(\"LAYOUTLIST\"),label(\"exceptions\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41__tag__breakable___123_102_114_111_109_44_116_111_44_103_101_110_101_114_97_116_111_114_115_125 = (IConstructor) _read("prod(label(\"map\",sort(\"Comprehension\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"from\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{tag(breakable(\"{from,to,generators}\"))})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__index_$Field__fieldIndex_$IntegerLiteral_ = (IConstructor) _read("prod(label(\"index\",sort(\"Field\")),[label(\"fieldIndex\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_false_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"false\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__qualifiedName_$Expression__qualifiedName_$QualifiedName_ = (IConstructor) _read("prod(label(\"qualifiedName\",sort(\"Expression\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"list\"),[\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__keyword_$SyntaxDefinition__lit_keyword_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"keyword\",sort(\"SyntaxDefinition\")),[lit(\"keyword\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__$DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"DecimalIntegerLiteral\"),[\\char-class([range(49,57)]),conditional(\\iter-star(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"abstract\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__greaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"greaterThan\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"test\"),[\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"subtraction\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__present_$Start__lit_start_ = (IConstructor) _read("prod(label(\"present\",sort(\"Start\")),[lit(\"start\")],{})", Factory.Production);
  private static final IConstructor prod__assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"assert\",sort(\"Statement\")),[lit(\"assert\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"closure\",sort(\"Expression\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__bounded_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___60_58_$layouts_LAYOUTLIST_bound_$Type_ = (IConstructor) _read("prod(label(\"bounded\",sort(\"TypeVar\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"\\<:\"),layouts(\"LAYOUTLIST\"),label(\"bound\",sort(\"Type\"))],{})", Factory.Production);
  private static final IConstructor prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"false\"),[\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__set_$BasicType__lit_set_ = (IConstructor) _read("prod(label(\"set\",sort(\"BasicType\")),[lit(\"set\")],{})", Factory.Production);
  private static final IConstructor prod__$NamedBackslash__conditional__char_class___range__92_92__not_follow__char_class___range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"NamedBackslash\"),[conditional(\\char-class([range(92,92)]),{\\not-follow(\\char-class([range(60,60),range(62,62),range(92,92)]))})],{})", Factory.Production);
  private static final IConstructor prod__in_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"in\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"in\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_ = (IConstructor) _read("prod(label(\"complement\",sort(\"Class\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"charClass\",sort(\"Class\"))],{})", Factory.Production);
  private static final IConstructor prod__list_$Pattern__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"list\",sort(\"Pattern\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__start__$Commands__$layouts_LAYOUTLIST_top_$Commands_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Commands\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Commands\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"set\"),[\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_non_assoc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"non-assoc\")],{})", Factory.Production);
  private static final IConstructor prod__lit_bottom_up_break__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"bottom-up-break\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(45,45)]),\\char-class([range(117,117)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"break\",sort(\"Statement\")),[lit(\"break\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__literal_$Pattern__literal_$Literal_ = (IConstructor) _read("prod(label(\"literal\",sort(\"Pattern\")),[label(\"literal\",sort(\"Literal\"))],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__fail_$Statement__lit_fail_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"fail\",sort(\"Statement\")),[lit(\"fail\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"any\",sort(\"Expression\")),[lit(\"any\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"throws\"),[\\char-class([range(116,116)]),\\char-class([range(104,104)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"bag\"),[\\char-class([range(98,98)]),\\char-class([range(97,97)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"visit\"),[\\char-class([range(118,118)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__default_$Import__lit_import_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"default\",sort(\"Import\")),[lit(\"import\"),layouts(\"LAYOUTLIST\"),label(\"module\",sort(\"ImportedModule\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__string_$BasicType__lit_str_ = (IConstructor) _read("prod(label(\"string\",sort(\"BasicType\")),[lit(\"str\")],{})", Factory.Production);
  private static final IConstructor regular__iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor prod__setOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_ = (IConstructor) _read("prod(label(\"setOption\",sort(\"ShellCommand\")),[lit(\"set\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_value_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"value\")],{})", Factory.Production);
  private static final IConstructor prod__return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable = (IConstructor) _read("prod(label(\"return\",sort(\"Statement\")),[lit(\"return\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{assoc(\\non-assoc()),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__regExp_$Literal__regExpLiteral_$RegExpLiteral_ = (IConstructor) _read("prod(label(\"regExp\",sort(\"Literal\")),[label(\"regExpLiteral\",lex(\"RegExpLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__$HeaderKeyword__lit_extend_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"extend\")],{})", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_16777215 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,16777215)])))", Factory.Production);
  private static final IConstructor prod__isDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_ = (IConstructor) _read("prod(label(\"isDefined\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\")],{})", Factory.Production);
  private static final IConstructor prod__value_$BasicType__lit_value_ = (IConstructor) _read("prod(label(\"value\",sort(\"BasicType\")),[lit(\"value\")],{})", Factory.Production);
  private static final IConstructor prod__unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_ = (IConstructor) _read("prod(label(\"unimport\",sort(\"ShellCommand\")),[lit(\"unimport\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__notPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right = (IConstructor) _read("prod(label(\"notPrecede\",sort(\"Sym\")),[label(\"match\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\\<\\<\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__multiVariable_$Pattern__qualifiedName_$QualifiedName_$layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"multiVariable\",sort(\"Pattern\")),[label(\"qualifiedName\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__default_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"Header\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),lit(\"module\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"imports\",\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__default_$Visibility__ = (IConstructor) _read("prod(label(\"default\",sort(\"Visibility\")),[],{})", Factory.Production);
  private static final IConstructor prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"public\"),[\\char-class([range(112,112)]),\\char-class([range(117,117)]),\\char-class([range(98,98)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__givenVisibility_$Toplevel__declaration_$Declaration_ = (IConstructor) _read("prod(label(\"givenVisibility\",sort(\"Toplevel\")),[label(\"declaration\",sort(\"Declaration\"))],{})", Factory.Production);
  private static final IConstructor prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[conditional(lit(\".\"),{\\not-precede(\\char-class([range(46,46)]))}),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__lit_rat__char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"rat\"),[\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__$Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"Name\"),[\\char-class([range(92,92)]),\\char-class([range(65,90),range(95,95),range(97,122)]),conditional(\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__intersection_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Class__assoc__left = (IConstructor) _read("prod(label(\"intersection\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"&&\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit_renaming__char_class___range__114_114_char_class___range__101_101_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"renaming\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_alias_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"alias\")],{})", Factory.Production);
  private static final IConstructor prod__functionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration__tag__breakable = (IConstructor) _read("prod(label(\"functionDeclaration\",sort(\"Statement\")),[label(\"functionDeclaration\",sort(\"FunctionDeclaration\"))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__while_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"while\",sort(\"StringTemplate\")),[lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left = (IConstructor) _read("prod(label(\"or\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"||\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"value\"),[\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__$PathChars__$URLChars_char_class___range__124_124_ = (IConstructor) _read("prod(lex(\"PathChars\"),[lex(\"URLChars\"),\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__$HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_conditional__iter__char_class___range__48_57_range__65_70_range__97_102__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"HexIntegerLiteral\"),[\\char-class([range(48,48)]),\\char-class([range(88,88),range(120,120)]),conditional(iter(\\char-class([range(48,57),range(65,70),range(97,102)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__nonInterpolated_$ProtocolPart__protocolChars_$ProtocolChars_ = (IConstructor) _read("prod(label(\"nonInterpolated\",sort(\"ProtocolPart\")),[label(\"protocolChars\",lex(\"ProtocolChars\"))],{})", Factory.Production);
  private static final IConstructor prod__$RascalKeywords__lit_int_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"int\")],{})", Factory.Production);
  private static final IConstructor prod__empty_$Target__ = (IConstructor) _read("prod(label(\"empty\",sort(\"Target\")),[],{})", Factory.Production);
  private static final IConstructor prod__start__$Module__$layouts_LAYOUTLIST_top_$Module_$layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Module\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Module\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"module\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__post_$PathTail__post_$PostPathChars_ = (IConstructor) _read("prod(label(\"post\",sort(\"PathTail\")),[label(\"post\",lex(\"PostPathChars\"))],{})", Factory.Production);
    
  // Item declarations
	
	
  protected static class $Tag {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Folded_tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-8, 4, "$TagString", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-7, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-6, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-5, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-4, 0, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      builder.addAlternative(RascalRascal.prod__default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Folded_tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Folded_tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-15, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-14, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-13, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-12, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-11, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-10, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-9, 0, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      builder.addAlternative(RascalRascal.prod__expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Folded_tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Folded_tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-18, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-17, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-16, 0, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      builder.addAlternative(RascalRascal.prod__empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Folded_tag__category___67_111_109_109_101_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_contents_$TagString__tag__Folded_tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__expression_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression__tag__Folded_tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__empty_$Tag__lit___64_$layouts_LAYOUTLIST_name_$Name__tag__Folded_tag__category___67_111_109_109_101_110_116(builder);
      
    }
  }
	
  protected static class $DateAndTime {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$DateAndTime__lit___36_$DatePart_lit_T_conditional__$TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(-28, 3, "$TimePartNoTZ", null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{43,43},{45,45}})});
      tmp[2] = new LiteralStackNode<IConstructor>(-27, 2, prod__lit_T__char_class___range__84_84_, new int[] {84}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-26, 1, "$DatePart", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-25, 0, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      builder.addAlternative(RascalRascal.prod__$DateAndTime__lit___36_$DatePart_lit_T_conditional__$TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_, tmp);
	}
    protected static final void _init_prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_$TimeZonePart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-33, 4, "$TimeZonePart", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-32, 3, "$TimePartNoTZ", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-31, 2, prod__lit_T__char_class___range__84_84_, new int[] {84}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-30, 1, "$DatePart", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-29, 0, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      builder.addAlternative(RascalRascal.prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_$TimeZonePart_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$DateAndTime__lit___36_$DatePart_lit_T_conditional__$TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_(builder);
      
        _init_prod__$DateAndTime__lit___36_$DatePart_lit_T_$TimePartNoTZ_$TimeZonePart_(builder);
      
    }
  }
	
  protected static class $Pattern {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__negative_$Pattern__lit___$layouts_LAYOUTLIST_argument_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-39, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-38, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-37, 0, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      builder.addAlternative(RascalRascal.prod__negative_$Pattern__lit___$layouts_LAYOUTLIST_argument_$Pattern_, tmp);
	}
    protected static final void _init_prod__multiVariable_$Pattern__qualifiedName_$QualifiedName_$layouts_LAYOUTLIST_lit___42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-42, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-41, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-40, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__multiVariable_$Pattern__qualifiedName_$QualifiedName_$layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-114, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-113, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-112, 0, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      builder.addAlternative(RascalRascal.prod__descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    protected static final void _init_prod__splicePlus_$Pattern__lit___43_$layouts_LAYOUTLIST_argument_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-45, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-44, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-43, 0, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      builder.addAlternative(RascalRascal.prod__splicePlus_$Pattern__lit___43_$layouts_LAYOUTLIST_argument_$Pattern_, tmp);
	}
    protected static final void _init_prod__typedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-121, 6, "$Pattern", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-120, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-119, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-118, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-117, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-116, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-115, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__typedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    protected static final void _init_prod__asType_$Pattern__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-128, 6, "$Pattern", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-127, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-126, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-125, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-124, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-123, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-122, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__asType_$Pattern__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Pattern_, tmp);
	}
    protected static final void _init_prod__list_$Pattern__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-57, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-56, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-51, 2, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-52, 0, "$Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-53, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-54, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-55, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-50, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-49, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__list_$Pattern__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__variableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-133, 4, "$Pattern", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-132, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-131, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-130, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-129, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__variableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    protected static final void _init_prod__splice_$Pattern__lit___42_$layouts_LAYOUTLIST_argument_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-60, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-59, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-58, 0, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      builder.addAlternative(RascalRascal.prod__splice_$Pattern__lit___42_$layouts_LAYOUTLIST_argument_$Pattern_, tmp);
	}
    protected static final void _init_prod__set_$Pattern__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-80, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-79, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-74, 2, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-75, 0, "$Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-76, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-77, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-78, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-73, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-72, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__set_$Pattern__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__literal_$Pattern__literal_$Literal_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-82, 0, "$Literal", null, null);
      builder.addAlternative(RascalRascal.prod__literal_$Pattern__literal_$Literal_, tmp);
	}
    protected static final void _init_prod__typedVariable_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-48, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-47, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-46, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__typedVariable_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__callOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-71, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-70, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-65, 4, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-66, 0, "$Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-67, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-68, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-69, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-64, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-63, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-62, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-61, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__callOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__qualifiedName_$Pattern__qualifiedName_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-81, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__qualifiedName_$Pattern__qualifiedName_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-91, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-90, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-85, 2, regular__iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-86, 0, "$Mapping__$Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-87, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-88, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-89, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-84, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-83, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__reifiedType_$Pattern__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Pattern_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Pattern_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(-102, 10, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-101, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-100, 8, "$Pattern", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-99, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-98, 6, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-97, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-96, 4, "$Pattern", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-95, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-94, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-93, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-92, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalRascal.prod__reifiedType_$Pattern__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Pattern_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Pattern_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-111, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-110, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-105, 2, regular__iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-106, 0, "$Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-107, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-108, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-109, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-104, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-103, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__anti_$Pattern__lit___33_$layouts_LAYOUTLIST_pattern_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-136, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-135, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-134, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      builder.addAlternative(RascalRascal.prod__anti_$Pattern__lit___33_$layouts_LAYOUTLIST_pattern_$Pattern_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__negative_$Pattern__lit___$layouts_LAYOUTLIST_argument_$Pattern_(builder);
      
        _init_prod__multiVariable_$Pattern__qualifiedName_$QualifiedName_$layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__descendant_$Pattern__lit___47_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
        _init_prod__splicePlus_$Pattern__lit___43_$layouts_LAYOUTLIST_argument_$Pattern_(builder);
      
        _init_prod__typedVariableBecomes_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
        _init_prod__asType_$Pattern__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Pattern_(builder);
      
        _init_prod__list_$Pattern__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__variableBecomes_$Pattern__name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
        _init_prod__splice_$Pattern__lit___42_$layouts_LAYOUTLIST_argument_$Pattern_(builder);
      
        _init_prod__set_$Pattern__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__literal_$Pattern__literal_$Literal_(builder);
      
        _init_prod__typedVariable_$Pattern__type_$Type_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__callOrTree_$Pattern__expression_$Pattern_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__qualifiedName_$Pattern__qualifiedName_$QualifiedName_(builder);
      
        _init_prod__map_$Pattern__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__reifiedType_$Pattern__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Pattern_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Pattern_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__tuple_$Pattern__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__anti_$Pattern__lit___33_$layouts_LAYOUTLIST_pattern_$Pattern_(builder);
      
    }
  }
	
  protected static class $Prod {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__unlabeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode<IConstructor>(-142, 2, regular__iter_star_seps__$Sym__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-143, 0, "$Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-144, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-141, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(-138, 0, regular__iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-139, 0, "$ProdModifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-140, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalRascal.prod__unlabeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__all_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-171, 4, "$Prod", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-170, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-169, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-168, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-167, 0, "$Prod", null, null);
      builder.addAlternative(RascalRascal.prod__all_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left, tmp);
	}
    protected static final void _init_prod__associativityGroup_$Prod__associativity_$Assoc_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_group_$Prod_$layouts_LAYOUTLIST_lit___41__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-151, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-150, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-149, 4, "$Prod", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-148, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-147, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-146, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-145, 0, "$Assoc", null, null);
      builder.addAlternative(RascalRascal.prod__associativityGroup_$Prod__associativity_$Assoc_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_group_$Prod_$layouts_LAYOUTLIST_lit___41__tag__Foldable, tmp);
	}
    protected static final void _init_prod__reference_$Prod__lit___58_$layouts_LAYOUTLIST_referenced_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-154, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-153, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-152, 0, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(RascalRascal.prod__reference_$Prod__lit___58_$layouts_LAYOUTLIST_referenced_$Name_, tmp);
	}
    protected static final void _init_prod__first_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-176, 4, "$Prod", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-175, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-174, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {62})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-173, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-172, 0, "$Prod", null, null);
      builder.addAlternative(RascalRascal.prod__first_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left, tmp);
	}
    protected static final void _init_prod__others_$Prod__lit___46_46_46_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-155, 0, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new int[] {46,46,46}, null, null);
      builder.addAlternative(RascalRascal.prod__others_$Prod__lit___46_46_46_, tmp);
	}
    protected static final void _init_prod__labeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new SeparatedListStackNode<IConstructor>(-164, 6, regular__iter_star_seps__$Sym__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-165, 0, "$Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-166, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-163, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-162, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-161, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-160, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-159, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(-156, 0, regular__iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-157, 0, "$ProdModifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-158, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalRascal.prod__labeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__unlabeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_(builder);
      
        _init_prod__all_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left(builder);
      
        _init_prod__associativityGroup_$Prod__associativity_$Assoc_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_group_$Prod_$layouts_LAYOUTLIST_lit___41__tag__Foldable(builder);
      
        _init_prod__reference_$Prod__lit___58_$layouts_LAYOUTLIST_referenced_$Name_(builder);
      
        _init_prod__first_$Prod__lhs_$Prod_$layouts_LAYOUTLIST_conditional__lit___62__not_follow__lit___62_$layouts_LAYOUTLIST_rhs_$Prod__assoc__left(builder);
      
        _init_prod__others_$Prod__lit___46_46_46_(builder);
      
        _init_prod__labeled_$Prod__modifiers_iter_star_seps__$ProdModifier__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_args_iter_star_seps__$Sym__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $UnicodeEscape {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__ascii_$UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new CharStackNode<IConstructor>(-180, 3, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-179, 2, new int[][]{{48,55}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-178, 1, new int[][]{{97,97}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-177, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__ascii_$UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    protected static final void _init_prod__utf16_$UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[6];
      
      tmp[5] = new CharStackNode<IConstructor>(-186, 5, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(-185, 4, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(-184, 3, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-183, 2, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-182, 1, new int[][]{{117,117}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-181, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__utf16_$UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    protected static final void _init_prod__utf32_$UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[8];
      
      tmp[7] = new CharStackNode<IConstructor>(-194, 7, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(-193, 6, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[5] = new CharStackNode<IConstructor>(-192, 5, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(-191, 4, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(-190, 3, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-189, 2, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-188, 1, new int[][]{{85,85}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-187, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__utf32_$UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__ascii_$UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
        _init_prod__utf16_$UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
        _init_prod__utf32_$UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
    }
  }
	
  protected static class $OctalIntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(-202, 1, regular__iter__char_class___range__48_55, new CharStackNode<IConstructor>(-203, 0, new int[][]{{48,55}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(-201, 0, new int[][]{{48,48}}, null, null);
      builder.addAlternative(RascalRascal.prod__$OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$OctalIntegerLiteral__char_class___range__48_48_conditional__iter__char_class___range__48_55__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class $RationalLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new ListStackNode<IConstructor>(-210, 4, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(-211, 0, new int[][]{{48,57}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[3] = new CharStackNode<IConstructor>(-209, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-208, 2, new int[][]{{114,114}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-206, 1, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(-207, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-205, 0, new int[][]{{49,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__$RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(-215, 2, new int[][]{{114,114}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-213, 1, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(-214, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-212, 0, new int[][]{{48,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__$RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_(builder);
      
    }
  }
	
  protected static class $BooleanLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$BooleanLiteral__lit_false_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-224, 0, prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {102,97,108,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$BooleanLiteral__lit_false_, tmp);
	}
    protected static final void _init_prod__$BooleanLiteral__lit_true_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-225, 0, prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_, new int[] {116,114,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$BooleanLiteral__lit_true_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$BooleanLiteral__lit_false_(builder);
      
        _init_prod__$BooleanLiteral__lit_true_(builder);
      
    }
  }
	
  protected static class $Toplevel {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__givenVisibility_$Toplevel__declaration_$Declaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-223, 0, "$Declaration", null, null);
      builder.addAlternative(RascalRascal.prod__givenVisibility_$Toplevel__declaration_$Declaration_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__givenVisibility_$Toplevel__declaration_$Declaration_(builder);
      
    }
  }
	
  protected static class $ProdModifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__bracket_$ProdModifier__lit_bracket_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-226, 0, prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_, new int[] {98,114,97,99,107,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__bracket_$ProdModifier__lit_bracket_, tmp);
	}
    protected static final void _init_prod__associativity_$ProdModifier__associativity_$Assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-227, 0, "$Assoc", null, null);
      builder.addAlternative(RascalRascal.prod__associativity_$ProdModifier__associativity_$Assoc_, tmp);
	}
    protected static final void _init_prod__tag_$ProdModifier__tag_$Tag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-228, 0, "$Tag", null, null);
      builder.addAlternative(RascalRascal.prod__tag_$ProdModifier__tag_$Tag_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__bracket_$ProdModifier__lit_bracket_(builder);
      
        _init_prod__associativity_$ProdModifier__associativity_$Assoc_(builder);
      
        _init_prod__tag_$ProdModifier__tag_$Tag_(builder);
      
    }
  }
	
  protected static class $TypeVar {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__bounded_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___60_58_$layouts_LAYOUTLIST_bound_$Type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-235, 6, "$Type", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-234, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-233, 4, prod__lit___60_58__char_class___range__60_60_char_class___range__58_58_, new int[] {60,58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-232, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-231, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-230, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-229, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      builder.addAlternative(RascalRascal.prod__bounded_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___60_58_$layouts_LAYOUTLIST_bound_$Type_, tmp);
	}
    protected static final void _init_prod__free_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-238, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-237, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-236, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      builder.addAlternative(RascalRascal.prod__free_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__bounded_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___60_58_$layouts_LAYOUTLIST_bound_$Type_(builder);
      
        _init_prod__free_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_(builder);
      
    }
  }
	
  protected static class $FunctionModifiers {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__list_$FunctionModifiers__modifiers_iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(-304, 0, regular__iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-305, 0, "$FunctionModifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-306, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalRascal.prod__list_$FunctionModifiers__modifiers_iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__list_$FunctionModifiers__modifiers_iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Comprehension {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__set_$Comprehension__lit___123_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-269, 8, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-268, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-263, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-264, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-265, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-266, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-267, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-262, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-261, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-260, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-255, 2, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-256, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-257, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-258, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-259, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-254, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-253, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__set_$Comprehension__lit___123_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125, tmp);
	}
    protected static final void _init_prod__map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41__tag__breakable___123_102_114_111_109_44_116_111_44_103_101_110_101_114_97_116_111_114_115_125(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(-286, 12, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-285, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-280, 10, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-281, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-282, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-283, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-284, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-279, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-278, 8, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-277, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-276, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-275, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-274, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-273, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-272, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-271, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-270, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41__tag__breakable___123_102_114_111_109_44_116_111_44_103_101_110_101_114_97_116_111_114_115_125, tmp);
	}
    protected static final void _init_prod__list_$Comprehension__lit___91_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-303, 8, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-302, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-297, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-298, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-299, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-300, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-301, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-296, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-295, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-294, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-289, 2, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-290, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-291, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-292, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-293, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-288, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-287, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__list_$Comprehension__lit___91_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__set_$Comprehension__lit___123_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125(builder);
      
        _init_prod__map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41__tag__breakable___123_102_114_111_109_44_116_111_44_103_101_110_101_114_97_116_111_114_115_125(builder);
      
        _init_prod__list_$Comprehension__lit___91_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125(builder);
      
    }
  }
	
  protected static class $Type {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__selector_$Type__selector_$DataTypeSelector_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-307, 0, "$DataTypeSelector", null, null);
      builder.addAlternative(RascalRascal.prod__selector_$Type__selector_$DataTypeSelector_, tmp);
	}
    protected static final void _init_prod__structured_$Type__structured_$StructuredType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-308, 0, "$StructuredType", null, null);
      builder.addAlternative(RascalRascal.prod__structured_$Type__structured_$StructuredType_, tmp);
	}
    protected static final void _init_prod__bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-313, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-312, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-311, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-310, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-309, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__function_$Type__function_$FunctionType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-314, 0, "$FunctionType", null, null);
      builder.addAlternative(RascalRascal.prod__function_$Type__function_$FunctionType_, tmp);
	}
    protected static final void _init_prod__user_$Type__conditional__user_$UserType__delete__$HeaderKeyword_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-315, 0, "$UserType", null, new ICompletionFilter[] {new StringMatchRestriction(new int[] {108,101,120,105,99,97,108}), new StringMatchRestriction(new int[] {105,109,112,111,114,116}), new StringMatchRestriction(new int[] {115,116,97,114,116}), new StringMatchRestriction(new int[] {115,121,110,116,97,120}), new StringMatchRestriction(new int[] {107,101,121,119,111,114,100}), new StringMatchRestriction(new int[] {108,97,121,111,117,116}), new StringMatchRestriction(new int[] {101,120,116,101,110,100})});
      builder.addAlternative(RascalRascal.prod__user_$Type__conditional__user_$UserType__delete__$HeaderKeyword_, tmp);
	}
    protected static final void _init_prod__basic_$Type__basic_$BasicType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-316, 0, "$BasicType", null, null);
      builder.addAlternative(RascalRascal.prod__basic_$Type__basic_$BasicType_, tmp);
	}
    protected static final void _init_prod__variable_$Type__typeVar_$TypeVar_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-317, 0, "$TypeVar", null, null);
      builder.addAlternative(RascalRascal.prod__variable_$Type__typeVar_$TypeVar_, tmp);
	}
    protected static final void _init_prod__symbol_$Type__symbol_$Sym_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-318, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__symbol_$Type__symbol_$Sym_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__selector_$Type__selector_$DataTypeSelector_(builder);
      
        _init_prod__structured_$Type__structured_$StructuredType_(builder);
      
        _init_prod__bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__function_$Type__function_$FunctionType_(builder);
      
        _init_prod__user_$Type__conditional__user_$UserType__delete__$HeaderKeyword_(builder);
      
        _init_prod__basic_$Type__basic_$BasicType_(builder);
      
        _init_prod__variable_$Type__typeVar_$TypeVar_(builder);
      
        _init_prod__symbol_$Type__symbol_$Sym_(builder);
      
    }
  }
	
  protected static class $Declaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__function_$Declaration__functionDeclaration_$FunctionDeclaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-319, 0, "$FunctionDeclaration", null, null);
      builder.addAlternative(RascalRascal.prod__function_$Declaration__functionDeclaration_$FunctionDeclaration_, tmp);
	}
    protected static final void _init_prod__alias_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_alias_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_base_$Type_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-320, 0, "$Tags", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-321, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-322, 2, "$Visibility", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-323, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-324, 4, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new int[] {97,108,105,97,115}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-325, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-326, 6, "$UserType", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-327, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-328, 8, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-329, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(-330, 10, "$Type", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-331, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(-332, 12, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__alias_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_alias_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_base_$Type_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(-347, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-346, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(-345, 12, "$Name", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-344, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(-343, 10, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-342, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-341, 8, "$Type", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-340, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-339, 6, "$Type", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-338, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-337, 4, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new int[] {97,110,110,111}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-336, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-335, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-334, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-333, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(-366, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-365, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(-360, 12, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-361, 0, "$Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-362, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-363, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-364, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-359, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(-358, 10, prod__lit_on__char_class___range__111_111_char_class___range__110_110_, new int[] {111,110}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-357, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-356, 8, "$Name", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-355, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-354, 6, "$Kind", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-353, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-352, 4, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new int[] {116,97,103}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-351, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-350, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-349, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-348, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-379, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-378, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-373, 6, regular__iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-374, 0, "$Variable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-375, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-376, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-377, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-372, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-371, 4, "$Type", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-370, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-369, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-368, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-367, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__dataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-388, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-387, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-386, 6, "$UserType", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-385, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-384, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-383, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-382, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-381, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-380, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__dataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__data_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_variants_iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(-405, 12, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-404, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-399, 10, regular__iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-400, 0, "$Variant", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-401, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-402, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null), new NonTerminalStackNode<IConstructor>(-403, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-398, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-397, 8, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-396, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-395, 6, "$UserType", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-394, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-393, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-392, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-391, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-390, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-389, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__data_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_variants_iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__function_$Declaration__functionDeclaration_$FunctionDeclaration_(builder);
      
        _init_prod__alias_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_alias_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_base_$Type_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__dataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__data_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_variants_iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
    }
  }
	
  protected static class $Class {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__union_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Class__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-430, 4, "$Class", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-429, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-428, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new int[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-427, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-426, 0, "$Class", null, null);
      builder.addAlternative(RascalRascal.prod__union_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Class__assoc__left, tmp);
	}
    protected static final void _init_prod__simpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-412, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-411, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-408, 2, regular__iter_star_seps__$Range__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-409, 0, "$Range", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-410, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-407, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-406, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__simpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-435, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-434, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-433, 2, "$Class", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-432, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-431, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-415, 2, "$Class", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-414, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-413, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      builder.addAlternative(RascalRascal.prod__complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_, tmp);
	}
    protected static final void _init_prod__difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-420, 4, "$Class", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-419, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-418, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-417, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-416, 0, "$Class", null, null);
      builder.addAlternative(RascalRascal.prod__difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left, tmp);
	}
    protected static final void _init_prod__intersection_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Class__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-425, 4, "$Class", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-424, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-423, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new int[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-422, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-421, 0, "$Class", null, null);
      builder.addAlternative(RascalRascal.prod__intersection_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Class__assoc__left, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__union_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Class__assoc__left(builder);
      
        _init_prod__simpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_(builder);
      
        _init_prod__difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left(builder);
      
        _init_prod__intersection_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Class__assoc__left(builder);
      
    }
  }
	
  protected static class $Bound {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__empty_$Bound__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(-436, 0);
      builder.addAlternative(RascalRascal.prod__empty_$Bound__, tmp);
	}
    protected static final void _init_prod__default_$Bound__lit___59_$layouts_LAYOUTLIST_expression_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-439, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-438, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-437, 0, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__default_$Bound__lit___59_$layouts_LAYOUTLIST_expression_$Expression_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__empty_$Bound__(builder);
      
        _init_prod__default_$Bound__lit___59_$layouts_LAYOUTLIST_expression_$Expression_(builder);
      
    }
  }
	
  protected static class $FunctionType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__typeArguments_$FunctionType__type_$Type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-459, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-458, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-453, 4, regular__iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-454, 0, "$TypeArg", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-455, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-456, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-457, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-452, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-451, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-450, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-449, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__typeArguments_$FunctionType__type_$Type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__typeArguments_$FunctionType__type_$Type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class $Case {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__patternWithAction_$Case__lit_case_$layouts_LAYOUTLIST_patternWithAction_$PatternWithAction__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-466, 2, "$PatternWithAction", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-465, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-464, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new int[] {99,97,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__patternWithAction_$Case__lit_case_$layouts_LAYOUTLIST_patternWithAction_$PatternWithAction__tag__Foldable, tmp);
	}
    protected static final void _init_prod__default_$Case__lit_default_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-471, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-470, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-469, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-468, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-467, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(RascalRascal.prod__default_$Case__lit_default_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__patternWithAction_$Case__lit_case_$layouts_LAYOUTLIST_patternWithAction_$PatternWithAction__tag__Foldable(builder);
      
        _init_prod__default_$Case__lit_default_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement__tag__Foldable(builder);
      
    }
  }
	
  protected static class $Statement {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__ifThenElse_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_elseStatement_$Statement__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new NonTerminalStackNode<IConstructor>(-521, 14, "$Statement", null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-520, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(-519, 12, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-518, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(-517, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-516, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-515, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-514, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-509, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-510, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-511, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-512, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-513, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-508, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-507, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-506, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-505, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-504, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-503, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__ifThenElse_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_elseStatement_$Statement__tag__breakable, tmp);
	}
    protected static final void _init_prod__throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-611, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-610, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-609, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new int[] {116,104,114,111,119}, null, null);
      builder.addAlternative(RascalRascal.prod__throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable, tmp);
	}
    protected static final void _init_prod__visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-524, 2, "$Visit", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-523, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-522, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit__tag__breakable, tmp);
	}
    protected static final void _init_prod__ifThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-525, 0, "$Label", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-526, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-527, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-528, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-529, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-530, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-531, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-532, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-533, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-534, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-535, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-536, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-537, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-538, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(-539, 10, "$Statement", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-540, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new EmptyStackNode<IConstructor>(-541, 12, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {101,108,115,101})});
      builder.addAlternative(RascalRascal.prod__ifThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else__tag__breakable, tmp);
	}
    protected static final void _init_prod__for_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable___123_103_101_110_101_114_97_116_111_114_115_125_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode<IConstructor>(-556, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-555, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-554, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-553, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-548, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-549, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-550, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-551, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-552, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-547, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-546, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-545, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-544, 2, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-543, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-542, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__for_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable___123_103_101_110_101_114_97_116_111_114_115_125_tag__breakable, tmp);
	}
    protected static final void _init_prod__switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(-573, 14, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-572, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(-569, 12, regular__iter_seps__$Case__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-570, 0, "$Case", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-571, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-568, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(-567, 10, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-566, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-565, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-564, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-563, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-562, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-561, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-560, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-559, 2, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {115,119,105,116,99,104}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-558, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-557, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable, tmp);
	}
    protected static final void _init_prod__return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-614, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-613, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-612, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new int[] {114,101,116,117,114,110}, null, null);
      builder.addAlternative(RascalRascal.prod__return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable, tmp);
	}
    protected static final void _init_prod__emptyStatement_$Statement__lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-574, 0, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__emptyStatement_$Statement__lit___59_, tmp);
	}
    protected static final void _init_prod__append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-624, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-623, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-622, 2, "$DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-621, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-620, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable, tmp);
	}
    protected static final void _init_prod__filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-577, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-576, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-575, 0, prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new int[] {102,105,108,116,101,114}, null, null);
      builder.addAlternative(RascalRascal.prod__filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-582, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-581, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-580, 2, "$Target", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-579, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-578, 0, prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,114,101,97,107}, null, null);
      builder.addAlternative(RascalRascal.prod__break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode<IConstructor>(-608, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-607, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-606, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-605, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-604, 6, "$Bound", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-603, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-598, 4, regular__iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-599, 0, "$QualifiedName", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-600, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-601, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-602, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-597, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-596, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-595, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-594, 0, prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_, new int[] {115,111,108,118,101}, null, null);
      builder.addAlternative(RascalRascal.prod__solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable, tmp);
	}
    protected static final void _init_prod__variableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-717, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-716, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-715, 0, "$LocalVariableDeclaration", null, null);
      builder.addAlternative(RascalRascal.prod__variableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__globalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-593, 6, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-592, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-587, 4, regular__iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-588, 0, "$QualifiedName", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-589, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-590, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-591, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-586, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-585, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-584, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-583, 0, prod__lit_global__char_class___range__103_103_char_class___range__108_108_char_class___range__111_111_char_class___range__98_98_char_class___range__97_97_char_class___range__108_108_, new int[] {103,108,111,98,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__globalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__assertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-633, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-632, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-631, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-630, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-629, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-628, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-627, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-626, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-625, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__assertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-638, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-637, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-636, 2, "$Assignment", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-635, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-634, 0, "$Assignable", null, null);
      builder.addAlternative(RascalRascal.prod__assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement__tag__breakable, tmp);
	}
    protected static final void _init_prod__while_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode<IConstructor>(-653, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-652, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-651, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-650, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-645, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-646, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-647, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-648, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-649, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-644, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-643, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-642, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-641, 2, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-640, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-639, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__while_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable, tmp);
	}
    protected static final void _init_prod__functionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-714, 0, "$FunctionDeclaration", null, null);
      builder.addAlternative(RascalRascal.prod__functionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration__tag__breakable, tmp);
	}
    protected static final void _init_prod__expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-656, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-655, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-654, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__nonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-672, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-671, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-668, 4, regular__iter_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-669, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-670, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-667, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-666, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-665, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-664, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__nonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode<IConstructor>(-661, 4, regular__iter_seps__$Catch__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-662, 0, "$Catch", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-663, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-660, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-659, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-658, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-657, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new int[] {116,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc_tag__breakable, tmp);
	}
    protected static final void _init_prod__doWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(-687, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-686, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(-685, 12, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-684, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(-683, 10, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-682, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-681, 8, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-680, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-679, 6, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-678, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-677, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-676, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-675, 2, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new int[] {100,111}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-674, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-673, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__doWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-619, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-618, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-617, 2, "$DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-616, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-615, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable, tmp);
	}
    protected static final void _init_prod__continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-692, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-691, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-690, 2, "$Target", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-689, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-688, 0, prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_, new int[] {99,111,110,116,105,110,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-697, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-696, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-695, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-694, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-693, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__fail_$Statement__lit_fail_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-713, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-712, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-711, 2, "$Target", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-710, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-709, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new int[] {102,97,105,108}, null, null);
      builder.addAlternative(RascalRascal.prod__fail_$Statement__lit_fail_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__tryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode<IConstructor>(-708, 8, "$Statement", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-707, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-706, 6, prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_, new int[] {102,105,110,97,108,108,121}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-705, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-702, 4, regular__iter_seps__$Catch__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-703, 0, "$Catch", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-704, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-701, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-700, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-699, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-698, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new int[] {116,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__tryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement__tag__breakable, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__ifThenElse_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_elseStatement_$Statement__tag__breakable(builder);
      
        _init_prod__throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(builder);
      
        _init_prod__visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit__tag__breakable(builder);
      
        _init_prod__ifThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else__tag__breakable(builder);
      
        _init_prod__for_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable___123_103_101_110_101_114_97_116_111_114_115_125_tag__breakable(builder);
      
        _init_prod__switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125__tag__breakable(builder);
      
        _init_prod__return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(builder);
      
        _init_prod__emptyStatement_$Statement__lit___59_(builder);
      
        _init_prod__append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(builder);
      
        _init_prod__filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable(builder);
      
        _init_prod__variableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__globalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__assertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement__tag__breakable(builder);
      
        _init_prod__while_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement__tag__breakable(builder);
      
        _init_prod__functionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration__tag__breakable(builder);
      
        _init_prod__expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__nonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc_tag__breakable(builder);
      
        _init_prod__doWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc_tag__breakable(builder);
      
        _init_prod__continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__fail_$Statement__lit_fail_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__tryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement__tag__breakable(builder);
      
    }
  }
	
  protected static class $Renamings {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Renamings__lit_renaming_$layouts_LAYOUTLIST_renamings_iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode<IConstructor>(-740, 2, regular__iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-741, 0, "$Renaming", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-742, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-743, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-744, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-739, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-738, 0, prod__lit_renaming__char_class___range__114_114_char_class___range__101_101_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_, new int[] {114,101,110,97,109,105,110,103}, null, null);
      builder.addAlternative(RascalRascal.prod__default_$Renamings__lit_renaming_$layouts_LAYOUTLIST_renamings_iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Renamings__lit_renaming_$layouts_LAYOUTLIST_renamings_iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $StringLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__nonInterpolated_$StringLiteral__constant_$StringConstant_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-745, 0, "$StringConstant", null, null);
      builder.addAlternative(RascalRascal.prod__nonInterpolated_$StringLiteral__constant_$StringConstant_, tmp);
	}
    protected static final void _init_prod__template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-750, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-749, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-748, 2, "$StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-747, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-746, 0, "$PreStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    protected static final void _init_prod__interpolated_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-755, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-754, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-753, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-752, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-751, 0, "$PreStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__interpolated_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__nonInterpolated_$StringLiteral__constant_$StringConstant_(builder);
      
        _init_prod__template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(builder);
      
        _init_prod__interpolated_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(builder);
      
    }
  }
	
  protected static class $Visibility {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__public_$Visibility__lit_public_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-769, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new int[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__public_$Visibility__lit_public_, tmp);
	}
    protected static final void _init_prod__default_$Visibility__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(-770, 0);
      builder.addAlternative(RascalRascal.prod__default_$Visibility__, tmp);
	}
    protected static final void _init_prod__private_$Visibility__lit_private_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-771, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new int[] {112,114,105,118,97,116,101}, null, null);
      builder.addAlternative(RascalRascal.prod__private_$Visibility__lit_private_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__public_$Visibility__lit_public_(builder);
      
        _init_prod__default_$Visibility__(builder);
      
        _init_prod__private_$Visibility__lit_private_(builder);
      
    }
  }
	
  protected static class $PostPathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PostPathChars__lit___62_$URLChars_lit___124_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-774, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-773, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-772, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(RascalRascal.prod__$PostPathChars__lit___62_$URLChars_lit___124_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$PostPathChars__lit___62_$URLChars_lit___124_(builder);
      
    }
  }
	
  protected static class $NamedBackslash {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$NamedBackslash__conditional__char_class___range__92_92__not_follow__char_class___range__60_60_range__62_62_range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(-775, 0, new int[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{60,60},{62,62},{92,92}})});
      builder.addAlternative(RascalRascal.prod__$NamedBackslash__conditional__char_class___range__92_92__not_follow__char_class___range__60_60_range__62_62_range__92_92_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$NamedBackslash__conditional__char_class___range__92_92__not_follow__char_class___range__60_60_range__62_62_range__92_92_(builder);
      
    }
  }
	
  protected static class $Command {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__import_$Command__imported_$Import_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-817, 0, "$Import", null, null);
      builder.addAlternative(RascalRascal.prod__import_$Command__imported_$Import_, tmp);
	}
    protected static final void _init_prod__expression_$Command__expression_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-818, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__expression_$Command__expression_$Expression_, tmp);
	}
    protected static final void _init_prod__statement_$Command__statement_$Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-819, 0, "$Statement", null, null);
      builder.addAlternative(RascalRascal.prod__statement_$Command__statement_$Statement_, tmp);
	}
    protected static final void _init_prod__declaration_$Command__declaration_$Declaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-820, 0, "$Declaration", null, null);
      builder.addAlternative(RascalRascal.prod__declaration_$Command__declaration_$Declaration_, tmp);
	}
    protected static final void _init_prod__shell_$Command__lit___58_$layouts_LAYOUTLIST_command_$ShellCommand_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-823, 2, "$ShellCommand", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-822, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-821, 0, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(RascalRascal.prod__shell_$Command__lit___58_$layouts_LAYOUTLIST_command_$ShellCommand_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__import_$Command__imported_$Import_(builder);
      
        _init_prod__expression_$Command__expression_$Expression_(builder);
      
        _init_prod__statement_$Command__statement_$Statement_(builder);
      
        _init_prod__declaration_$Command__declaration_$Declaration_(builder);
      
        _init_prod__shell_$Command__lit___58_$layouts_LAYOUTLIST_command_$ShellCommand_(builder);
      
    }
  }
	
  protected static class $Visit {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__defaultStrategy_$Visit__lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(-799, 12, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-798, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-795, 10, regular__iter_seps__$Case__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-796, 0, "$Case", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-797, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-794, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-793, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-792, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-791, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-790, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-789, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-788, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-787, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-786, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-785, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new int[] {118,105,115,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__defaultStrategy_$Visit__lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__givenStrategy_$Visit__strategy_$Strategy_$layouts_LAYOUTLIST_lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(-816, 14, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-815, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(-812, 12, regular__iter_seps__$Case__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-813, 0, "$Case", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-814, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-811, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(-810, 10, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-809, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-808, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-807, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-806, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-805, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-804, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-803, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-802, 2, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new int[] {118,105,115,105,116}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-801, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-800, 0, "$Strategy", null, null);
      builder.addAlternative(RascalRascal.prod__givenStrategy_$Visit__strategy_$Strategy_$layouts_LAYOUTLIST_lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__defaultStrategy_$Visit__lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__givenStrategy_$Visit__strategy_$Strategy_$layouts_LAYOUTLIST_lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class $ProtocolTail {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__post_$ProtocolTail__post_$PostProtocolChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-824, 0, "$PostProtocolChars", null, null);
      builder.addAlternative(RascalRascal.prod__post_$ProtocolTail__post_$PostProtocolChars_, tmp);
	}
    protected static final void _init_prod__mid_$ProtocolTail__mid_$MidProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-829, 4, "$ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-828, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-827, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-826, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-825, 0, "$MidProtocolChars", null, null);
      builder.addAlternative(RascalRascal.prod__mid_$ProtocolTail__mid_$MidProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__post_$ProtocolTail__post_$PostProtocolChars_(builder);
      
        _init_prod__mid_$ProtocolTail__mid_$MidProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(builder);
      
    }
  }
	
  protected static class $PreStringChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PreStringChars__char_class___range__34_34_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(-852, 2, new int[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-850, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode<IConstructor>(-851, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-849, 0, new int[][]{{34,34}}, null, null);
      builder.addAlternative(RascalRascal.prod__$PreStringChars__char_class___range__34_34_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$PreStringChars__char_class___range__34_34_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $QualifiedName {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$QualifiedName__conditional__names_iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST__not_follow__lit___58_58_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(-853, 0, regular__iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-854, 0, "$Name", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-855, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-856, 2, prod__lit___58_58__char_class___range__58_58_char_class___range__58_58_, new int[] {58,58}, null, null), new NonTerminalStackNode<IConstructor>(-857, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {58,58})});
      builder.addAlternative(RascalRascal.prod__default_$QualifiedName__conditional__names_iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST__not_follow__lit___58_58_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$QualifiedName__conditional__names_iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST__not_follow__lit___58_58_(builder);
      
    }
  }
	
  protected static class $StringMiddle {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-868, 4, "$StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-867, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-866, 2, "$StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-865, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-864, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_, tmp);
	}
    protected static final void _init_prod__interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-873, 4, "$StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-872, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-871, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-870, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-869, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_, tmp);
	}
    protected static final void _init_prod__mid_$StringMiddle__mid_$MidStringChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-874, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__mid_$StringMiddle__mid_$MidStringChars_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_(builder);
      
        _init_prod__interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_(builder);
      
        _init_prod__mid_$StringMiddle__mid_$MidStringChars_(builder);
      
    }
  }
	
  protected static class $URLChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(-862, 0, regular__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215, new CharStackNode<IConstructor>(-863, 0, new int[][]{{0,8},{11,12},{14,31},{33,59},{61,123},{125,16777215}}, null, null), false, null, null);
      builder.addAlternative(RascalRascal.prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_(builder);
      
    }
  }
	
  protected static class $TimeZonePart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$TimeZonePart__lit_Z_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-875, 0, prod__lit_Z__char_class___range__90_90_, new int[] {90}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimeZonePart__lit_Z_, tmp);
	}
    protected static final void _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(-878, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-877, 1, new int[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-876, 0, new int[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(-883, 4, new int[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(-882, 3, new int[][]{{48,53}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-881, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-880, 1, new int[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-879, 0, new int[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[6];
      
      tmp[5] = new CharStackNode<IConstructor>(-889, 5, new int[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(-888, 4, new int[][]{{48,53}}, null, null);
      tmp[3] = new LiteralStackNode<IConstructor>(-887, 3, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-886, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-885, 1, new int[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-884, 0, new int[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$TimeZonePart__lit_Z_(builder);
      
        _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_(builder);
      
        _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_(builder);
      
        _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class $ShellCommand {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__edit_$ShellCommand__lit_edit_$layouts_LAYOUTLIST_name_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-892, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-891, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-890, 0, prod__lit_edit__char_class___range__101_101_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_, new int[] {101,100,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__edit_$ShellCommand__lit_edit_$layouts_LAYOUTLIST_name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__help_$ShellCommand__lit_help_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-893, 0, prod__lit_help__char_class___range__104_104_char_class___range__101_101_char_class___range__108_108_char_class___range__112_112_, new int[] {104,101,108,112}, null, null);
      builder.addAlternative(RascalRascal.prod__help_$ShellCommand__lit_help_, tmp);
	}
    protected static final void _init_prod__quit_$ShellCommand__lit_quit_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-894, 0, prod__lit_quit__char_class___range__113_113_char_class___range__117_117_char_class___range__105_105_char_class___range__116_116_, new int[] {113,117,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__quit_$ShellCommand__lit_quit_, tmp);
	}
    protected static final void _init_prod__setOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-899, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-898, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-897, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-896, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-895, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new int[] {115,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__setOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_, tmp);
	}
    protected static final void _init_prod__history_$ShellCommand__lit_history_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-900, 0, prod__lit_history__char_class___range__104_104_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__121_121_, new int[] {104,105,115,116,111,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__history_$ShellCommand__lit_history_, tmp);
	}
    protected static final void _init_prod__unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-903, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-902, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-901, 0, prod__lit_unimport__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {117,110,105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-906, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-905, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-904, 0, prod__lit_undeclare__char_class___range__117_117_char_class___range__110_110_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__101_101_, new int[] {117,110,100,101,99,108,97,114,101}, null, null);
      builder.addAlternative(RascalRascal.prod__undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__listModules_$ShellCommand__lit_modules_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-907, 0, prod__lit_modules__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_, new int[] {109,111,100,117,108,101,115}, null, null);
      builder.addAlternative(RascalRascal.prod__listModules_$ShellCommand__lit_modules_, tmp);
	}
    protected static final void _init_prod__listDeclarations_$ShellCommand__lit_declarations_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-909, 0, prod__lit_declarations__char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_, new int[] {100,101,99,108,97,114,97,116,105,111,110,115}, null, null);
      builder.addAlternative(RascalRascal.prod__listDeclarations_$ShellCommand__lit_declarations_, tmp);
	}
    protected static final void _init_prod__test_$ShellCommand__lit_test_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-908, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new int[] {116,101,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__test_$ShellCommand__lit_test_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__edit_$ShellCommand__lit_edit_$layouts_LAYOUTLIST_name_$QualifiedName_(builder);
      
        _init_prod__help_$ShellCommand__lit_help_(builder);
      
        _init_prod__quit_$ShellCommand__lit_quit_(builder);
      
        _init_prod__setOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_(builder);
      
        _init_prod__history_$ShellCommand__lit_history_(builder);
      
        _init_prod__unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_(builder);
      
        _init_prod__undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_(builder);
      
        _init_prod__listModules_$ShellCommand__lit_modules_(builder);
      
        _init_prod__listDeclarations_$ShellCommand__lit_declarations_(builder);
      
        _init_prod__test_$ShellCommand__lit_test_(builder);
      
    }
  }
	
  protected static class $LocationLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$LocationLiteral__protocolPart_$ProtocolPart_$layouts_LAYOUTLIST_pathPart_$PathPart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-912, 2, "$PathPart", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-911, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-910, 0, "$ProtocolPart", null, null);
      builder.addAlternative(RascalRascal.prod__default_$LocationLiteral__protocolPart_$ProtocolPart_$layouts_LAYOUTLIST_pathPart_$PathPart_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$LocationLiteral__protocolPart_$ProtocolPart_$layouts_LAYOUTLIST_pathPart_$PathPart_(builder);
      
    }
  }
	
  protected static class $Range {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__character_$Range__character_$Char_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-925, 0, "$Char", null, null);
      builder.addAlternative(RascalRascal.prod__character_$Range__character_$Char_, tmp);
	}
    protected static final void _init_prod__fromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-930, 4, "$Char", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-929, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-928, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-927, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-926, 0, "$Char", null, null);
      builder.addAlternative(RascalRascal.prod__fromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__character_$Range__character_$Char_(builder);
      
        _init_prod__fromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_(builder);
      
    }
  }
	
  protected static class $Rest {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Rest__conditional__iter_star__char_class___range__0_16777215__not_follow__char_class___range__0_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(-931, 0, regular__iter_star__char_class___range__0_16777215, new CharStackNode<IConstructor>(-932, 0, new int[][]{{0,16777215}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{0,16777215}})});
      builder.addAlternative(RascalRascal.prod__$Rest__conditional__iter_star__char_class___range__0_16777215__not_follow__char_class___range__0_16777215_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$Rest__conditional__iter_star__char_class___range__0_16777215__not_follow__char_class___range__0_16777215_(builder);
      
    }
  }
	
  protected static class $JustTime {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$JustTime__lit___36_84_$TimePartNoTZ_$TimeZonePart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-937, 2, "$TimeZonePart", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-936, 1, "$TimePartNoTZ", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-935, 0, prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_, new int[] {36,84}, null, null);
      builder.addAlternative(RascalRascal.prod__$JustTime__lit___36_84_$TimePartNoTZ_$TimeZonePart_, tmp);
	}
    protected static final void _init_prod__$JustTime__lit___36_84_conditional__$TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(-939, 1, "$TimePartNoTZ", null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{43,43},{45,45}})});
      tmp[0] = new LiteralStackNode<IConstructor>(-938, 0, prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_, new int[] {36,84}, null, null);
      builder.addAlternative(RascalRascal.prod__$JustTime__lit___36_84_conditional__$TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$JustTime__lit___36_84_$TimePartNoTZ_$TimeZonePart_(builder);
      
        _init_prod__$JustTime__lit___36_84_conditional__$TimePartNoTZ__not_follow__char_class___range__43_43_range__45_45_(builder);
      
    }
  }
	
  protected static class $StringCharacter {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(-955, 2, new int[][]{{39,39}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-953, 1, regular__iter_star__char_class___range__9_9_range__32_32, new CharStackNode<IConstructor>(-954, 0, new int[][]{{9,9},{32,32}}, null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-952, 0, new int[][]{{10,10}}, null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__$UnicodeEscape_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-956, 0, "$UnicodeEscape", null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__$UnicodeEscape_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(-957, 0, new int[][]{{0,33},{35,38},{40,59},{61,61},{63,91},{93,16777215}}, null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(-959, 1, new int[][]{{34,34},{39,39},{60,60},{62,62},{92,92},{98,98},{102,102},{110,110},{114,114},{116,116}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-958, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_(builder);
      
        _init_prod__$StringCharacter__$UnicodeEscape_(builder);
      
        _init_prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_(builder);
      
        _init_prod__$StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_(builder);
      
    }
  }
	
  protected static class $layouts_LAYOUTLIST {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$layouts_LAYOUTLIST__conditional__iter_star__$LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(-961, 0, regular__iter_star__$LAYOUT, new NonTerminalStackNode<IConstructor>(-962, 0, "$LAYOUT", null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,10},{13,13},{32,32}}), new StringFollowRestriction(new int[] {47,47}), new StringFollowRestriction(new int[] {47,42})});
      builder.addAlternative(RascalRascal.prod__$layouts_LAYOUTLIST__conditional__iter_star__$LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$layouts_LAYOUTLIST__conditional__iter_star__$LAYOUT__not_follow__char_class___range__9_10_range__13_13_range__32_32_not_follow__lit___47_47_not_follow__lit___47_42_(builder);
      
    }
  }
	
  protected static class $DataTarget {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__labeled_$DataTarget__label_$Name_$layouts_LAYOUTLIST_lit___58_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-1008, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1007, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1006, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__labeled_$DataTarget__label_$Name_$layouts_LAYOUTLIST_lit___58_, tmp);
	}
    protected static final void _init_prod__empty_$DataTarget__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(-1009, 0);
      builder.addAlternative(RascalRascal.prod__empty_$DataTarget__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__labeled_$DataTarget__label_$Name_$layouts_LAYOUTLIST_lit___58_(builder);
      
        _init_prod__empty_$DataTarget__(builder);
      
    }
  }
	
  protected static class $RealLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(-1012, 1, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null);
      tmp[0] = new ListStackNode<IConstructor>(-1010, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-1011, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new OptionalStackNode<IConstructor>(-1020, 4, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(-1021, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[3] = new ListStackNode<IConstructor>(-1018, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-1019, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new OptionalStackNode<IConstructor>(-1016, 2, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode<IConstructor>(-1017, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[1] = new CharStackNode<IConstructor>(-1015, 1, new int[][]{{69,69},{101,101}}, null, null);
      tmp[0] = new ListStackNode<IConstructor>(-1013, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-1014, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new OptionalStackNode<IConstructor>(-1032, 6, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(-1033, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[5] = new ListStackNode<IConstructor>(-1030, 5, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-1031, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[4] = new OptionalStackNode<IConstructor>(-1028, 4, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode<IConstructor>(-1029, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[3] = new CharStackNode<IConstructor>(-1027, 3, new int[][]{{69,69},{101,101}}, null, null);
      tmp[2] = new ListStackNode<IConstructor>(-1025, 2, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(-1026, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(-1024, 1, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[0] = new ListStackNode<IConstructor>(-1022, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-1023, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1034, 0, prod__lit___46__char_class___range__46_46_, new int[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{46,46}})}, null);
      tmp[1] = new ListStackNode<IConstructor>(-1035, 1, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-1036, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new OptionalStackNode<IConstructor>(-1037, 2, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(-1038, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[6];
      
      tmp[5] = new OptionalStackNode<IConstructor>(-1054, 5, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(-1055, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[4] = new ListStackNode<IConstructor>(-1052, 4, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-1053, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[3] = new OptionalStackNode<IConstructor>(-1050, 3, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode<IConstructor>(-1051, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[2] = new CharStackNode<IConstructor>(-1049, 2, new int[][]{{69,69},{101,101}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-1047, 1, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-1048, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1046, 0, prod__lit___46__char_class___range__46_46_, new int[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{46,46}})}, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new OptionalStackNode<IConstructor>(-1044, 3, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(-1045, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[2] = new ListStackNode<IConstructor>(-1042, 2, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(-1043, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(-1041, 1, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {46})});
      tmp[0] = new ListStackNode<IConstructor>(-1039, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(-1040, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__$RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
    }
  }
	
  protected static class $layouts_$default$ {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$layouts_$default$__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(-1058, 0);
      builder.addAlternative(RascalRascal.prod__$layouts_$default$__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$layouts_$default$__(builder);
      
    }
  }
	
  protected static class $Replacement {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__unconditional_$Replacement__replacementExpression_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1061, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__unconditional_$Replacement__replacementExpression_$Expression_, tmp);
	}
    protected static final void _init_prod__conditional_$Replacement__replacementExpression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode<IConstructor>(-1066, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1067, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1068, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1069, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1070, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1065, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1064, 2, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new int[] {119,104,101,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1063, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1062, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__conditional_$Replacement__replacementExpression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__unconditional_$Replacement__replacementExpression_$Expression_(builder);
      
        _init_prod__conditional_$Replacement__replacementExpression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Assoc {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__right_$Assoc__lit_right_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1083, 0, prod__lit_right__char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_, new int[] {114,105,103,104,116}, null, null);
      builder.addAlternative(RascalRascal.prod__right_$Assoc__lit_right_, tmp);
	}
    protected static final void _init_prod__nonAssociative_$Assoc__lit_non_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1084, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__nonAssociative_$Assoc__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__left_$Assoc__lit_left_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1085, 0, prod__lit_left__char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_, new int[] {108,101,102,116}, null, null);
      builder.addAlternative(RascalRascal.prod__left_$Assoc__lit_left_, tmp);
	}
    protected static final void _init_prod__associative_$Assoc__lit_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1086, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {97,115,115,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__associative_$Assoc__lit_assoc_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__right_$Assoc__lit_right_(builder);
      
        _init_prod__nonAssociative_$Assoc__lit_non_assoc_(builder);
      
        _init_prod__left_$Assoc__lit_left_(builder);
      
        _init_prod__associative_$Assoc__lit_assoc_(builder);
      
    }
  }
	
  protected static class $RegExpModifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(-1089, 0, regular__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115, new CharStackNode<IConstructor>(-1090, 0, new int[][]{{100,100},{105,105},{109,109},{115,115}}, null, null), false, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_(builder);
      
    }
  }
	
  protected static class $MidStringChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MidStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(-1094, 2, new int[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-1092, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode<IConstructor>(-1093, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-1091, 0, new int[][]{{62,62}}, null, null);
      builder.addAlternative(RascalRascal.prod__$MidStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$MidStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $RegExp {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(-1095, 0, new int[][]{{0,46},{48,59},{61,61},{63,91},{93,16777215}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_, tmp);
	}
    protected static final void _init_prod__$RegExp__char_class___range__60_60_expression_$Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(-1098, 2, new int[][]{{62,62}}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1097, 1, "$Expression", null, null);
      tmp[0] = new CharStackNode<IConstructor>(-1096, 0, new int[][]{{60,60}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__char_class___range__60_60_expression_$Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101, tmp);
	}
    protected static final void _init_prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-1104, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(-1102, 3, regular__iter_star__$NamedRegExp, new NonTerminalStackNode<IConstructor>(-1103, 0, "$NamedRegExp", null, null), false, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1101, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1100, 1, "$Name", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1099, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_, tmp);
	}
    protected static final void _init_prod__$RegExp__lit___60_$Name_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-1107, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1106, 1, "$Name", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1105, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__lit___60_$Name_lit___62_, tmp);
	}
    protected static final void _init_prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(-1109, 1, new int[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-1108, 0, new int[][]{{92,92}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__$RegExp__$Backslash_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1110, 0, "$Backslash", null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__$Backslash_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(builder);
      
        _init_prod__$RegExp__char_class___range__60_60_expression_$Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101(builder);
      
        _init_prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_(builder);
      
        _init_prod__$RegExp__lit___60_$Name_lit___62_(builder);
      
        _init_prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
        _init_prod__$RegExp__$Backslash_(builder);
      
    }
  }
	
  protected static class $PathPart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__nonInterpolated_$PathPart__pathChars_$PathChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1118, 0, "$PathChars", null, null);
      builder.addAlternative(RascalRascal.prod__nonInterpolated_$PathPart__pathChars_$PathChars_, tmp);
	}
    protected static final void _init_prod__interpolated_$PathPart__pre_$PrePathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1123, 4, "$PathTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1122, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1121, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1120, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1119, 0, "$PrePathChars", null, null);
      builder.addAlternative(RascalRascal.prod__interpolated_$PathPart__pre_$PrePathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__nonInterpolated_$PathPart__pathChars_$PathChars_(builder);
      
        _init_prod__interpolated_$PathPart__pre_$PrePathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_(builder);
      
    }
  }
	
  protected static class $StringTemplate {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__doWhile_$StringTemplate__lit_do_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[19];
      
      tmp[18] = new LiteralStackNode<IConstructor>(-1159, 18, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[17] = new NonTerminalStackNode<IConstructor>(-1158, 17, "$layouts_LAYOUTLIST", null, null);
      tmp[16] = new NonTerminalStackNode<IConstructor>(-1157, 16, "$Expression", null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(-1156, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new LiteralStackNode<IConstructor>(-1155, 14, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-1154, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(-1153, 12, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-1152, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(-1151, 10, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1150, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new SeparatedListStackNode<IConstructor>(-1147, 8, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1148, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1149, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1146, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-1145, 6, "$StringMiddle", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1144, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-1141, 4, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1142, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1143, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1140, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1139, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1138, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1137, 0, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new int[] {100,111}, null, null);
      builder.addAlternative(RascalRascal.prod__doWhile_$StringTemplate__lit_do_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__while_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode<IConstructor>(-1180, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(-1179, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(-1176, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1177, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1178, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-1175, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(-1174, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-1173, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-1170, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1171, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1172, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1169, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-1168, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1167, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-1166, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1165, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1164, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1163, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1162, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1161, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1160, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__while_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__for_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode<IConstructor>(-1205, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(-1204, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(-1201, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1202, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1203, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-1200, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(-1199, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-1198, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-1195, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1196, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1197, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1194, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-1193, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1192, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-1191, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1190, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-1185, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1186, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1187, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1188, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1189, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1184, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1183, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1182, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1181, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      builder.addAlternative(RascalRascal.prod__for_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__ifThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[29];
      
      tmp[28] = new LiteralStackNode<IConstructor>(-1246, 28, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[27] = new NonTerminalStackNode<IConstructor>(-1245, 27, "$layouts_LAYOUTLIST", null, null);
      tmp[26] = new SeparatedListStackNode<IConstructor>(-1242, 26, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1243, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1244, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[25] = new NonTerminalStackNode<IConstructor>(-1241, 25, "$layouts_LAYOUTLIST", null, null);
      tmp[24] = new NonTerminalStackNode<IConstructor>(-1240, 24, "$StringMiddle", null, null);
      tmp[23] = new NonTerminalStackNode<IConstructor>(-1239, 23, "$layouts_LAYOUTLIST", null, null);
      tmp[22] = new SeparatedListStackNode<IConstructor>(-1236, 22, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1237, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1238, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[21] = new NonTerminalStackNode<IConstructor>(-1235, 21, "$layouts_LAYOUTLIST", null, null);
      tmp[20] = new LiteralStackNode<IConstructor>(-1234, 20, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[19] = new NonTerminalStackNode<IConstructor>(-1233, 19, "$layouts_LAYOUTLIST", null, null);
      tmp[18] = new LiteralStackNode<IConstructor>(-1232, 18, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      tmp[17] = new NonTerminalStackNode<IConstructor>(-1231, 17, "$layouts_LAYOUTLIST", null, null);
      tmp[16] = new LiteralStackNode<IConstructor>(-1230, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(-1229, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(-1226, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1227, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1228, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-1225, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(-1224, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-1223, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-1220, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1221, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1222, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1219, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-1218, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1217, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-1216, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1215, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-1210, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1211, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1212, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1213, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1214, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1209, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1208, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1207, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1206, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      builder.addAlternative(RascalRascal.prod__ifThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__ifThen_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode<IConstructor>(-1271, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(-1270, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(-1267, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1268, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1269, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-1266, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(-1265, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-1264, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-1261, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1262, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1263, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1260, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-1259, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1258, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-1257, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1256, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-1251, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1252, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1253, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1254, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1255, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1250, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1249, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1248, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1247, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      builder.addAlternative(RascalRascal.prod__ifThen_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__doWhile_$StringTemplate__lit_do_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__while_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__for_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__ifThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__ifThen_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class $DecimalIntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(-1273, 1, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(-1274, 0, new int[][]{{48,57}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(-1272, 0, new int[][]{{49,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__$DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1275, 0, prod__lit_0__char_class___range__48_48_, new int[] {48}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      builder.addAlternative(RascalRascal.prod__$DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__$DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class $LAYOUT {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$LAYOUT__char_class___range__9_10_range__13_13_range__32_32_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(-1288, 0, new int[][]{{9,10},{13,13},{32,32}}, null, null);
      builder.addAlternative(RascalRascal.prod__$LAYOUT__char_class___range__9_10_range__13_13_range__32_32_, tmp);
	}
    protected static final void _init_prod__$LAYOUT__$Comment_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1289, 0, "$Comment", null, null);
      builder.addAlternative(RascalRascal.prod__$LAYOUT__$Comment_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$LAYOUT__char_class___range__9_10_range__13_13_range__32_32_(builder);
      
        _init_prod__$LAYOUT__$Comment_(builder);
      
    }
  }
	
  protected static class $Body {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__toplevels_$Body__toplevels_iter_star_seps__$Toplevel__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(-1285, 0, regular__iter_star_seps__$Toplevel__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1286, 0, "$Toplevel", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1287, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalRascal.prod__toplevels_$Body__toplevels_iter_star_seps__$Toplevel__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__toplevels_$Body__toplevels_iter_star_seps__$Toplevel__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Import {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__syntax_$Import__syntax_$SyntaxDefinition_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1302, 0, "$SyntaxDefinition", null, null);
      builder.addAlternative(RascalRascal.prod__syntax_$Import__syntax_$SyntaxDefinition_, tmp);
	}
    protected static final void _init_prod__default_$Import__lit_import_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-1307, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1306, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1305, 2, "$ImportedModule", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1304, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1303, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__default_$Import__lit_import_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__extend_$Import__lit_extend_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-1312, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1311, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1310, 2, "$ImportedModule", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1309, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1308, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__extend_$Import__lit_extend_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__external_$Import__lit_import_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_at_$LocationLiteral_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-1321, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1320, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-1319, 6, "$LocationLiteral", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1318, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-1317, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1316, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1315, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1314, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1313, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__external_$Import__lit_import_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_at_$LocationLiteral_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__syntax_$Import__syntax_$SyntaxDefinition_(builder);
      
        _init_prod__default_$Import__lit_import_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__extend_$Import__lit_extend_$layouts_LAYOUTLIST_module_$ImportedModule_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__external_$Import__lit_import_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_at_$LocationLiteral_$layouts_LAYOUTLIST_lit___59_(builder);
      
    }
  }
	
  protected static class $UserType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__parametric_$UserType__conditional__name_$QualifiedName__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-1300, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1299, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-1294, 4, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1295, 0, "$Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1296, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1297, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1298, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1293, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1292, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1291, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1290, 0, "$QualifiedName", null, new ICompletionFilter[] {new StringFollowRequirement(new int[] {91})});
      builder.addAlternative(RascalRascal.prod__parametric_$UserType__conditional__name_$QualifiedName__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__name_$UserType__name_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1301, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__name_$UserType__name_$QualifiedName_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__parametric_$UserType__conditional__name_$QualifiedName__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__name_$UserType__name_$QualifiedName_(builder);
      
    }
  }
	
  protected static class $FunctionBody {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$FunctionBody__lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-1332, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1331, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-1328, 2, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1329, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1330, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1327, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1326, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__default_$FunctionBody__lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$FunctionBody__lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class start__$Command {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__$Command__$layouts_LAYOUTLIST_top_$Command_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1342, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1341, 1, "$Command", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1340, 0, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalRascal.prod__start__$Command__$layouts_LAYOUTLIST_top_$Command_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__$Command__$layouts_LAYOUTLIST_top_$Command_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Target {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__labeled_$Target__name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1349, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__labeled_$Target__name_$Name_, tmp);
	}
    protected static final void _init_prod__empty_$Target__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(-1350, 0);
      builder.addAlternative(RascalRascal.prod__empty_$Target__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__labeled_$Target__name_$Name_(builder);
      
        _init_prod__empty_$Target__(builder);
      
    }
  }
	
  protected static class $IntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__decimalIntegerLiteral_$IntegerLiteral__decimal_$DecimalIntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1346, 0, "$DecimalIntegerLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__decimalIntegerLiteral_$IntegerLiteral__decimal_$DecimalIntegerLiteral_, tmp);
	}
    protected static final void _init_prod__octalIntegerLiteral_$IntegerLiteral__octal_$OctalIntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1347, 0, "$OctalIntegerLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__octalIntegerLiteral_$IntegerLiteral__octal_$OctalIntegerLiteral_, tmp);
	}
    protected static final void _init_prod__hexIntegerLiteral_$IntegerLiteral__hex_$HexIntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1348, 0, "$HexIntegerLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__hexIntegerLiteral_$IntegerLiteral__hex_$HexIntegerLiteral_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__decimalIntegerLiteral_$IntegerLiteral__decimal_$DecimalIntegerLiteral_(builder);
      
        _init_prod__octalIntegerLiteral_$IntegerLiteral__octal_$OctalIntegerLiteral_(builder);
      
        _init_prod__hexIntegerLiteral_$IntegerLiteral__hex_$HexIntegerLiteral_(builder);
      
    }
  }
	
  protected static class $SyntaxDefinition {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__language_$SyntaxDefinition__start_$Start_$layouts_LAYOUTLIST_lit_syntax_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(-1371, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1370, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-1369, 8, "$Prod", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1368, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-1367, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1366, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1365, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1364, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1363, 2, prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_, new int[] {115,121,110,116,97,120}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1362, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1361, 0, "$Start", null, null);
      builder.addAlternative(RascalRascal.prod__language_$SyntaxDefinition__start_$Start_$layouts_LAYOUTLIST_lit_syntax_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__keyword_$SyntaxDefinition__lit_keyword_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-1380, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1379, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-1378, 6, "$Prod", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1377, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-1376, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1375, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1374, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1373, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1372, 0, prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_, new int[] {107,101,121,119,111,114,100}, null, null);
      builder.addAlternative(RascalRascal.prod__keyword_$SyntaxDefinition__lit_keyword_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__lexical_$SyntaxDefinition__lit_lexical_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-1389, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1388, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-1387, 6, "$Prod", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1386, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-1385, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1384, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1383, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1382, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1381, 0, prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_, new int[] {108,101,120,105,99,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__lexical_$SyntaxDefinition__lit_lexical_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__layout_$SyntaxDefinition__vis_$Visibility_$layouts_LAYOUTLIST_lit_layout_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(-1400, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1399, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-1398, 8, "$Prod", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1397, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-1396, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1395, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1394, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1393, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1392, 2, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new int[] {108,97,121,111,117,116}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1391, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1390, 0, "$Visibility", null, null);
      builder.addAlternative(RascalRascal.prod__layout_$SyntaxDefinition__vis_$Visibility_$layouts_LAYOUTLIST_lit_layout_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__language_$SyntaxDefinition__start_$Start_$layouts_LAYOUTLIST_lit_syntax_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__keyword_$SyntaxDefinition__lit_keyword_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__lexical_$SyntaxDefinition__lit_lexical_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__layout_$SyntaxDefinition__vis_$Visibility_$layouts_LAYOUTLIST_lit_layout_$layouts_LAYOUTLIST_defined_$Sym_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_production_$Prod_$layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
    }
  }
	
  protected static class $Kind {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__all_$Kind__lit_all_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1401, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new int[] {97,108,108}, null, null);
      builder.addAlternative(RascalRascal.prod__all_$Kind__lit_all_, tmp);
	}
    protected static final void _init_prod__module_$Kind__lit_module_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1402, 0, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__module_$Kind__lit_module_, tmp);
	}
    protected static final void _init_prod__variable_$Kind__lit_variable_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1403, 0, prod__lit_variable__char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_, new int[] {118,97,114,105,97,98,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__variable_$Kind__lit_variable_, tmp);
	}
    protected static final void _init_prod__anno_$Kind__lit_anno_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1404, 0, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new int[] {97,110,110,111}, null, null);
      builder.addAlternative(RascalRascal.prod__anno_$Kind__lit_anno_, tmp);
	}
    protected static final void _init_prod__data_$Kind__lit_data_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1405, 0, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      builder.addAlternative(RascalRascal.prod__data_$Kind__lit_data_, tmp);
	}
    protected static final void _init_prod__tag_$Kind__lit_tag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1406, 0, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new int[] {116,97,103}, null, null);
      builder.addAlternative(RascalRascal.prod__tag_$Kind__lit_tag_, tmp);
	}
    protected static final void _init_prod__function_$Kind__lit_function_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1407, 0, prod__lit_function__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_, new int[] {102,117,110,99,116,105,111,110}, null, null);
      builder.addAlternative(RascalRascal.prod__function_$Kind__lit_function_, tmp);
	}
    protected static final void _init_prod__view_$Kind__lit_view_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1408, 0, prod__lit_view__char_class___range__118_118_char_class___range__105_105_char_class___range__101_101_char_class___range__119_119_, new int[] {118,105,101,119}, null, null);
      builder.addAlternative(RascalRascal.prod__view_$Kind__lit_view_, tmp);
	}
    protected static final void _init_prod__alias_$Kind__lit_alias_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1409, 0, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new int[] {97,108,105,97,115}, null, null);
      builder.addAlternative(RascalRascal.prod__alias_$Kind__lit_alias_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__all_$Kind__lit_all_(builder);
      
        _init_prod__module_$Kind__lit_module_(builder);
      
        _init_prod__variable_$Kind__lit_variable_(builder);
      
        _init_prod__anno_$Kind__lit_anno_(builder);
      
        _init_prod__data_$Kind__lit_data_(builder);
      
        _init_prod__tag_$Kind__lit_tag_(builder);
      
        _init_prod__function_$Kind__lit_function_(builder);
      
        _init_prod__view_$Kind__lit_view_(builder);
      
        _init_prod__alias_$Kind__lit_alias_(builder);
      
    }
  }
	
  protected static class $Mapping__$Expression {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Mapping__$Expression__from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1422, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1421, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1420, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1419, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1418, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__default_$Mapping__$Expression__from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Mapping__$Expression__from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_(builder);
      
    }
  }
	
  protected static class $PostProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PostProtocolChars__lit___62_$URLChars_lit___58_47_47_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-1431, 2, prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_, new int[] {58,47,47}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1430, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1429, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(RascalRascal.prod__$PostProtocolChars__lit___62_$URLChars_lit___58_47_47_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$PostProtocolChars__lit___62_$URLChars_lit___58_47_47_(builder);
      
    }
  }
	
  protected static class $MidPathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MidPathChars__lit___62_$URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-1452, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1451, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1450, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(RascalRascal.prod__$MidPathChars__lit___62_$URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$MidPathChars__lit___62_$URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class $Char {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Char__$UnicodeEscape__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1453, 0, "$UnicodeEscape", null, null);
      builder.addAlternative(RascalRascal.prod__$Char__$UnicodeEscape__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(-1454, 0, new int[][]{{0,31},{33,33},{35,38},{40,44},{46,59},{61,61},{63,90},{94,16777215}}, null, null);
      builder.addAlternative(RascalRascal.prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__$Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(-1456, 1, new int[][]{{32,32},{34,34},{39,39},{45,45},{60,60},{62,62},{91,93},{98,98},{102,102},{110,110},{114,114},{116,116}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1455, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__$Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$Char__$UnicodeEscape__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__$Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__$Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $PrePathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PrePathChars__$URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new LiteralStackNode<IConstructor>(-1458, 1, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1457, 0, "$URLChars", null, null);
      builder.addAlternative(RascalRascal.prod__$PrePathChars__$URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$PrePathChars__$URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class $Strategy {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__bottomUpBreak_$Strategy__lit_bottom_up_break_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1461, 0, prod__lit_bottom_up_break__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,111,116,116,111,109,45,117,112,45,98,114,101,97,107}, null, null);
      builder.addAlternative(RascalRascal.prod__bottomUpBreak_$Strategy__lit_bottom_up_break_, tmp);
	}
    protected static final void _init_prod__outermost_$Strategy__lit_outermost_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1462, 0, prod__lit_outermost__char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_, new int[] {111,117,116,101,114,109,111,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__outermost_$Strategy__lit_outermost_, tmp);
	}
    protected static final void _init_prod__bottomUp_$Strategy__lit_bottom_up_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1463, 0, prod__lit_bottom_up__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_, new int[] {98,111,116,116,111,109,45,117,112}, null, null);
      builder.addAlternative(RascalRascal.prod__bottomUp_$Strategy__lit_bottom_up_, tmp);
	}
    protected static final void _init_prod__innermost_$Strategy__lit_innermost_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1464, 0, prod__lit_innermost__char_class___range__105_105_char_class___range__110_110_char_class___range__110_110_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_, new int[] {105,110,110,101,114,109,111,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__innermost_$Strategy__lit_innermost_, tmp);
	}
    protected static final void _init_prod__topDownBreak_$Strategy__lit_top_down_break_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1465, 0, prod__lit_top_down_break__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {116,111,112,45,100,111,119,110,45,98,114,101,97,107}, null, null);
      builder.addAlternative(RascalRascal.prod__topDownBreak_$Strategy__lit_top_down_break_, tmp);
	}
    protected static final void _init_prod__topDown_$Strategy__lit_top_down_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1466, 0, prod__lit_top_down__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_, new int[] {116,111,112,45,100,111,119,110}, null, null);
      builder.addAlternative(RascalRascal.prod__topDown_$Strategy__lit_top_down_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__bottomUpBreak_$Strategy__lit_bottom_up_break_(builder);
      
        _init_prod__outermost_$Strategy__lit_outermost_(builder);
      
        _init_prod__bottomUp_$Strategy__lit_bottom_up_(builder);
      
        _init_prod__innermost_$Strategy__lit_innermost_(builder);
      
        _init_prod__topDownBreak_$Strategy__lit_top_down_break_(builder);
      
        _init_prod__topDown_$Strategy__lit_top_down_(builder);
      
    }
  }
	
  protected static class $ModuleParameters {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$ModuleParameters__lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-1486, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1485, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-1480, 2, regular__iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1481, 0, "$TypeVar", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1482, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1483, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1484, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1479, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1478, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__default_$ModuleParameters__lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$ModuleParameters__lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$TypeVar__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class $RascalKeywords {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RascalKeywords__lit_solve_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1491, 0, prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_, new int[] {115,111,108,118,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_solve_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_rat_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1490, 0, prod__lit_rat__char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_, new int[] {114,97,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_rat_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_break_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1492, 0, prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,114,101,97,107}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_break_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_rel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1493, 0, prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new int[] {114,101,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_rel_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_false_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1494, 0, prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {102,97,108,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_false_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_any_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1495, 0, prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_, new int[] {97,110,121}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_any_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_all_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1496, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new int[] {97,108,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_all_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_real_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1498, 0, prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_, new int[] {114,101,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_real_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_finally_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1497, 0, prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_, new int[] {102,105,110,97,108,108,121}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_finally_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_bool_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1499, 0, prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_, new int[] {98,111,111,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_bool_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_filter_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1500, 0, prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new int[] {102,105,108,116,101,114}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_filter_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_datetime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1501, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new int[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_datetime_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_while_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1504, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_while_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_case_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1503, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new int[] {99,97,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_case_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_layout_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1502, 0, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new int[] {108,97,121,111,117,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_layout_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_num_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1505, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new int[] {110,117,109}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_num_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_set_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1506, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new int[] {115,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_set_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_bag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1507, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new int[] {98,97,103}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_bag_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1508, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {97,115,115,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_assoc_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_for_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1509, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_for_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_continue_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1510, 0, prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_, new int[] {99,111,110,116,105,110,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_continue_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_bracket_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1511, 0, prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_, new int[] {98,114,97,99,107,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_bracket_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_visit_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1512, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new int[] {118,105,115,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_visit_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_return_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1517, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new int[] {114,101,116,117,114,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_return_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_else_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1516, 0, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_else_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_in_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1515, 0, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new int[] {105,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_in_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_it_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1514, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new int[] {105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_it_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_join_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1513, 0, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new int[] {106,111,105,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_join_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_if_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1518, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_if_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_non_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1519, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_one_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1520, 0, prod__lit_one__char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_, new int[] {111,110,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_one_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_str_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1521, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new int[] {115,116,114}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_str_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_node_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1522, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new int[] {110,111,100,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_node_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_catch_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1523, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {99,97,116,99,104}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_catch_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1525, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_type_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_notin_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1524, 0, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new int[] {110,111,116,105,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_notin_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_append_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1527, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_append_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_extend_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1526, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_extend_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_data_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1528, 0, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_data_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_tag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1529, 0, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new int[] {116,97,103}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_tag_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_dynamic_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1530, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new int[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_dynamic_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_list_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1531, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new int[] {108,105,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_list_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_value_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1532, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new int[] {118,97,108,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_value_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__$BasicType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1533, 0, "$BasicType", null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__$BasicType_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_try_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1534, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new int[] {116,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_try_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_void_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1535, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new int[] {118,111,105,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_void_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_public_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1536, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new int[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_public_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_anno_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1537, 0, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new int[] {97,110,110,111}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_anno_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_insert_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1538, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_insert_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_loc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1540, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new int[] {108,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_loc_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_assert_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1539, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_assert_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_import_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1541, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_import_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_start_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1542, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_start_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_test_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1543, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new int[] {116,101,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_test_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_map_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1544, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new int[] {109,97,112}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_map_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_private_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1546, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new int[] {112,114,105,118,97,116,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_private_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_true_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1545, 0, prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_, new int[] {116,114,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_true_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_module_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1547, 0, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_module_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_throws_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1548, 0, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new int[] {116,104,114,111,119,115}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_throws_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_default_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1549, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_default_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_alias_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1550, 0, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new int[] {97,108,105,97,115}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_alias_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_throw_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1551, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new int[] {116,104,114,111,119}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_throw_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_switch_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1552, 0, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {115,119,105,116,99,104}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_switch_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_mod_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1554, 0, prod__lit_mod__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_, new int[] {109,111,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_mod_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_fail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1553, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new int[] {102,97,105,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_fail_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_int_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1555, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new int[] {105,110,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_int_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_tuple_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1556, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new int[] {116,117,112,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_tuple_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
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
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$NamedRegExp__lit___60_$Name_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-1560, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1559, 1, "$Name", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1558, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__$NamedRegExp__lit___60_$Name_lit___62_, tmp);
	}
    protected static final void _init_prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(-1562, 1, new int[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-1561, 0, new int[][]{{92,92}}, null, null);
      builder.addAlternative(RascalRascal.prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__$NamedRegExp__$NamedBackslash_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1563, 0, "$NamedBackslash", null, null);
      builder.addAlternative(RascalRascal.prod__$NamedRegExp__$NamedBackslash_, tmp);
	}
    protected static final void _init_prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(-1564, 0, new int[][]{{0,46},{48,59},{61,61},{63,91},{93,16777215}}, null, null);
      builder.addAlternative(RascalRascal.prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$NamedRegExp__lit___60_$Name_lit___62_(builder);
      
        _init_prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
        _init_prod__$NamedRegExp__$NamedBackslash_(builder);
      
        _init_prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(builder);
      
    }
  }
	
  protected static class $RegExpLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$RegExpLiteral__lit___47_iter_star__$RegExp_lit___47_$RegExpModifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1581, 3, "$RegExpModifier", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1580, 2, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-1578, 1, regular__iter_star__$RegExp, new NonTerminalStackNode<IConstructor>(-1579, 0, "$RegExp", null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1577, 0, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExpLiteral__lit___47_iter_star__$RegExp_lit___47_$RegExpModifier_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$RegExpLiteral__lit___47_iter_star__$RegExp_lit___47_$RegExpModifier_(builder);
      
    }
  }
	
  protected static class $HexIntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_conditional__iter__char_class___range__48_57_range__65_70_range__97_102__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new ListStackNode<IConstructor>(-1622, 2, regular__iter__char_class___range__48_57_range__65_70_range__97_102, new CharStackNode<IConstructor>(-1623, 0, new int[][]{{48,57},{65,70},{97,102}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode<IConstructor>(-1621, 1, new int[][]{{88,88},{120,120}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-1620, 0, new int[][]{{48,48}}, null, null);
      builder.addAlternative(RascalRascal.prod__$HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_conditional__iter__char_class___range__48_57_range__65_70_range__97_102__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_conditional__iter__char_class___range__48_57_range__65_70_range__97_102__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class $PostStringChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PostStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(-1619, 2, new int[][]{{34,34}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-1617, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode<IConstructor>(-1618, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-1616, 0, new int[][]{{62,62}}, null, null);
      builder.addAlternative(RascalRascal.prod__$PostStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$PostStringChars__char_class___range__62_62_iter_star__$StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class start__$EvalCommand {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__$EvalCommand__$layouts_LAYOUTLIST_top_$EvalCommand_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1628, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1627, 1, "$EvalCommand", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1626, 0, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalRascal.prod__start__$EvalCommand__$layouts_LAYOUTLIST_top_$EvalCommand_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__$EvalCommand__$layouts_LAYOUTLIST_top_$EvalCommand_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $ModuleActuals {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$ModuleActuals__lit___91_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-1637, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1636, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-1631, 2, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1632, 0, "$Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1633, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1634, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1635, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1630, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1629, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__default_$ModuleActuals__lit___91_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$ModuleActuals__lit___91_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class $PathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PathChars__$URLChars_char_class___range__124_124_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(-1639, 1, new int[][]{{124,124}}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1638, 0, "$URLChars", null, null);
      builder.addAlternative(RascalRascal.prod__$PathChars__$URLChars_char_class___range__124_124_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$PathChars__$URLChars_char_class___range__124_124_(builder);
      
    }
  }
	
  protected static class $DateTimeLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__timeLiteral_$DateTimeLiteral__time_$JustTime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1645, 0, "$JustTime", null, null);
      builder.addAlternative(RascalRascal.prod__timeLiteral_$DateTimeLiteral__time_$JustTime_, tmp);
	}
    protected static final void _init_prod__dateLiteral_$DateTimeLiteral__date_$JustDate_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1646, 0, "$JustDate", null, null);
      builder.addAlternative(RascalRascal.prod__dateLiteral_$DateTimeLiteral__date_$JustDate_, tmp);
	}
    protected static final void _init_prod__dateAndTimeLiteral_$DateTimeLiteral__dateAndTime_$DateAndTime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1647, 0, "$DateAndTime", null, null);
      builder.addAlternative(RascalRascal.prod__dateAndTimeLiteral_$DateTimeLiteral__dateAndTime_$DateAndTime_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__timeLiteral_$DateTimeLiteral__time_$JustTime_(builder);
      
        _init_prod__dateLiteral_$DateTimeLiteral__date_$JustDate_(builder);
      
        _init_prod__dateAndTimeLiteral_$DateTimeLiteral__dateAndTime_$DateAndTime_(builder);
      
    }
  }
	
  protected static class $BasicType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__type_$BasicType__lit_type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1659, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalRascal.prod__type_$BasicType__lit_type_, tmp);
	}
    protected static final void _init_prod__set_$BasicType__lit_set_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1660, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new int[] {115,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__set_$BasicType__lit_set_, tmp);
	}
    protected static final void _init_prod__string_$BasicType__lit_str_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1661, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new int[] {115,116,114}, null, null);
      builder.addAlternative(RascalRascal.prod__string_$BasicType__lit_str_, tmp);
	}
    protected static final void _init_prod__dateTime_$BasicType__lit_datetime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1662, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new int[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(RascalRascal.prod__dateTime_$BasicType__lit_datetime_, tmp);
	}
    protected static final void _init_prod__bag_$BasicType__lit_bag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1663, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new int[] {98,97,103}, null, null);
      builder.addAlternative(RascalRascal.prod__bag_$BasicType__lit_bag_, tmp);
	}
    protected static final void _init_prod__rational_$BasicType__lit_rat_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1664, 0, prod__lit_rat__char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_, new int[] {114,97,116}, null, null);
      builder.addAlternative(RascalRascal.prod__rational_$BasicType__lit_rat_, tmp);
	}
    protected static final void _init_prod__real_$BasicType__lit_real_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1665, 0, prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_, new int[] {114,101,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__real_$BasicType__lit_real_, tmp);
	}
    protected static final void _init_prod__node_$BasicType__lit_node_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1666, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new int[] {110,111,100,101}, null, null);
      builder.addAlternative(RascalRascal.prod__node_$BasicType__lit_node_, tmp);
	}
    protected static final void _init_prod__int_$BasicType__lit_int_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1667, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new int[] {105,110,116}, null, null);
      builder.addAlternative(RascalRascal.prod__int_$BasicType__lit_int_, tmp);
	}
    protected static final void _init_prod__map_$BasicType__lit_map_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1668, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new int[] {109,97,112}, null, null);
      builder.addAlternative(RascalRascal.prod__map_$BasicType__lit_map_, tmp);
	}
    protected static final void _init_prod__tuple_$BasicType__lit_tuple_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1669, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new int[] {116,117,112,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__tuple_$BasicType__lit_tuple_, tmp);
	}
    protected static final void _init_prod__list_$BasicType__lit_list_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1670, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new int[] {108,105,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__list_$BasicType__lit_list_, tmp);
	}
    protected static final void _init_prod__num_$BasicType__lit_num_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1671, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new int[] {110,117,109}, null, null);
      builder.addAlternative(RascalRascal.prod__num_$BasicType__lit_num_, tmp);
	}
    protected static final void _init_prod__loc_$BasicType__lit_loc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1672, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new int[] {108,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__loc_$BasicType__lit_loc_, tmp);
	}
    protected static final void _init_prod__value_$BasicType__lit_value_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1673, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new int[] {118,97,108,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__value_$BasicType__lit_value_, tmp);
	}
    protected static final void _init_prod__relation_$BasicType__lit_rel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1674, 0, prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new int[] {114,101,108}, null, null);
      builder.addAlternative(RascalRascal.prod__relation_$BasicType__lit_rel_, tmp);
	}
    protected static final void _init_prod__void_$BasicType__lit_void_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1675, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new int[] {118,111,105,100}, null, null);
      builder.addAlternative(RascalRascal.prod__void_$BasicType__lit_void_, tmp);
	}
    protected static final void _init_prod__bool_$BasicType__lit_bool_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1676, 0, prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_, new int[] {98,111,111,108}, null, null);
      builder.addAlternative(RascalRascal.prod__bool_$BasicType__lit_bool_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__type_$BasicType__lit_type_(builder);
      
        _init_prod__set_$BasicType__lit_set_(builder);
      
        _init_prod__string_$BasicType__lit_str_(builder);
      
        _init_prod__dateTime_$BasicType__lit_datetime_(builder);
      
        _init_prod__bag_$BasicType__lit_bag_(builder);
      
        _init_prod__rational_$BasicType__lit_rat_(builder);
      
        _init_prod__real_$BasicType__lit_real_(builder);
      
        _init_prod__node_$BasicType__lit_node_(builder);
      
        _init_prod__int_$BasicType__lit_int_(builder);
      
        _init_prod__map_$BasicType__lit_map_(builder);
      
        _init_prod__tuple_$BasicType__lit_tuple_(builder);
      
        _init_prod__list_$BasicType__lit_list_(builder);
      
        _init_prod__num_$BasicType__lit_num_(builder);
      
        _init_prod__loc_$BasicType__lit_loc_(builder);
      
        _init_prod__value_$BasicType__lit_value_(builder);
      
        _init_prod__relation_$BasicType__lit_rel_(builder);
      
        _init_prod__void_$BasicType__lit_void_(builder);
      
        _init_prod__bool_$BasicType__lit_bool_(builder);
      
    }
  }
	
  protected static class $FunctionDeclaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_125(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(-1700, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1699, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-1698, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1697, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-1696, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1695, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1694, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1693, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1692, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1691, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1690, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_125, tmp);
	}
    protected static final void _init_prod__abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-1707, 6, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1706, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1705, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1704, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1703, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1702, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1701, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__default_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_body_$FunctionBody__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-1714, 6, "$FunctionBody", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1713, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1712, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1711, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1710, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1709, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1708, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__default_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_body_$FunctionBody__tag__Foldable, tmp);
	}
    protected static final void _init_prod__conditional_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_44_99_111_110_100_105_116_105_111_110_115_125(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(-1733, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(-1732, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(-1727, 12, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1728, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1729, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1730, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1731, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-1726, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(-1725, 10, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new int[] {119,104,101,110}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-1724, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-1723, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-1722, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-1721, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1720, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1719, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1718, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1717, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1716, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1715, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__conditional_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_44_99_111_110_100_105_116_105_111_110_115_125, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_125(builder);
      
        _init_prod__abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__default_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_body_$FunctionBody__tag__Foldable(builder);
      
        _init_prod__conditional_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_44_99_111_110_100_105_116_105_111_110_115_125(builder);
      
    }
  }
	
  protected static class $Variant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__nAryConstructor_$Variant__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-1748, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1747, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-1742, 4, regular__iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1743, 0, "$TypeArg", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1744, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1745, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1746, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1741, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1740, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1739, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1738, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__nAryConstructor_$Variant__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__nAryConstructor_$Variant__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class start__$PreModule {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__$PreModule__$layouts_LAYOUTLIST_top_$PreModule_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1753, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1752, 1, "$PreModule", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1751, 0, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalRascal.prod__start__$PreModule__$layouts_LAYOUTLIST_top_$PreModule_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__$PreModule__$layouts_LAYOUTLIST_top_$PreModule_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Declarator {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Declarator__type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode<IConstructor>(-1779, 2, regular__iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1780, 0, "$Variable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1781, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1782, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1783, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1778, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1777, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__default_$Declarator__type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Declarator__type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $StructuredType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$StructuredType__basicType_$BasicType_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_arguments_iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-1816, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-1815, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-1810, 4, regular__iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1811, 0, "$TypeArg", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1812, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1813, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1814, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1809, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1808, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1807, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1806, 0, "$BasicType", null, null);
      builder.addAlternative(RascalRascal.prod__default_$StructuredType__basicType_$BasicType_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_arguments_iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$StructuredType__basicType_$BasicType_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_arguments_iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class $TimePartNoTZ {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new OptionalStackNode<IConstructor>(-1828, 8, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(-1829, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(-1830, 0, new int[][]{{44,44},{46,46}}, null, null), new CharStackNode<IConstructor>(-1831, 1, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(-1832, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(-1833, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(-1834, 0, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(-1835, 1, regular__opt__char_class___range__48_57, new CharStackNode<IConstructor>(-1836, 0, new int[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[7] = new CharStackNode<IConstructor>(-1827, 7, new int[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(-1826, 6, new int[][]{{48,53}}, null, null);
      tmp[5] = new LiteralStackNode<IConstructor>(-1825, 5, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(-1824, 4, new int[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(-1823, 3, new int[][]{{48,53}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1822, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-1821, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-1820, 0, new int[][]{{48,50}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new OptionalStackNode<IConstructor>(-1843, 6, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(-1844, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(-1845, 0, new int[][]{{44,44},{46,46}}, null, null), new CharStackNode<IConstructor>(-1846, 1, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(-1847, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(-1848, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(-1849, 0, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(-1850, 1, regular__opt__char_class___range__48_57, new CharStackNode<IConstructor>(-1851, 0, new int[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[5] = new CharStackNode<IConstructor>(-1842, 5, new int[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(-1841, 4, new int[][]{{48,53}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(-1840, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-1839, 2, new int[][]{{48,53}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-1838, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-1837, 0, new int[][]{{48,50}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(builder);
      
        _init_prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class $Name {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalKeywords_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SequenceStackNode<IConstructor>(-1859, 0, regular__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(-1860, 0, new int[][]{{65,90},{95,95},{97,122}}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{65,90},{95,95},{97,122}})}, null), new ListStackNode<IConstructor>(-1861, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(-1862, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})})}, null, new ICompletionFilter[] {new StringMatchRestriction(new int[] {114,97,116}), new StringMatchRestriction(new int[] {105,110}), new StringMatchRestriction(new int[] {105,109,112,111,114,116}), new StringMatchRestriction(new int[] {99,111,110,116,105,110,117,101}), new StringMatchRestriction(new int[] {97,108,108}), new StringMatchRestriction(new int[] {102,97,108,115,101}), new StringMatchRestriction(new int[] {97,110,110,111}), new StringMatchRestriction(new int[] {98,114,97,99,107,101,116}), new StringMatchRestriction(new int[] {100,97,116,97}), new StringMatchRestriction(new int[] {106,111,105,110}), new StringMatchRestriction(new int[] {108,97,121,111,117,116}), new StringMatchRestriction(new int[] {105,116}), new StringMatchRestriction(new int[] {115,119,105,116,99,104}), new StringMatchRestriction(new int[] {99,97,115,101}), new StringMatchRestriction(new int[] {114,101,116,117,114,110}), new StringMatchRestriction(new int[] {115,116,114}), new StringMatchRestriction(new int[] {119,104,105,108,101}), new StringMatchRestriction(new int[] {115,111,108,118,101}), new StringMatchRestriction(new int[] {100,121,110,97,109,105,99}), new StringMatchRestriction(new int[] {110,111,116,105,110}), new StringMatchRestriction(new int[] {101,108,115,101}), new StringMatchRestriction(new int[] {105,110,115,101,114,116}), new StringMatchRestriction(new int[] {116,121,112,101}), new StringMatchRestriction(new int[] {116,114,121}), new StringMatchRestriction(new int[] {99,97,116,99,104}), new StringMatchRestriction(new int[] {110,117,109}), new StringMatchRestriction(new int[] {110,111,100,101}), new StringMatchRestriction(new int[] {109,111,100}), new StringMatchRestriction(new int[] {112,114,105,118,97,116,101}), new StringMatchRestriction(new int[] {102,105,110,97,108,108,121}), new StringMatchRestriction(new int[] {116,114,117,101}), new StringMatchRestriction(new int[] {98,97,103}), new StringMatchRestriction(new int[] {118,111,105,100}), new StringMatchRestriction(new int[] {97,115,115,111,99}), new StringMatchRestriction(new int[] {110,111,110,45,97,115,115,111,99}), new StringMatchRestriction(new int[] {116,101,115,116}), new StringMatchRestriction(new int[] {105,102}), new StringMatchRestriction(new int[] {114,101,97,108}), new StringMatchRestriction(new int[] {108,105,115,116}), new StringMatchRestriction(new int[] {102,97,105,108}), new StringMatchRestriction(new int[] {114,101,108}), new StringMatchRestriction(new int[] {101,120,116,101,110,100}), new StringMatchRestriction(new int[] {97,112,112,101,110,100}), new StringMatchRestriction(new int[] {116,97,103}), new StringMatchRestriction(new int[] {111,110,101}), new StringMatchRestriction(new int[] {116,104,114,111,119}), new StringMatchRestriction(new int[] {115,101,116}), new StringMatchRestriction(new int[] {115,116,97,114,116}), new StringMatchRestriction(new int[] {97,110,121}), new StringMatchRestriction(new int[] {109,111,100,117,108,101}), new StringMatchRestriction(new int[] {105,110,116}), new StringMatchRestriction(new int[] {112,117,98,108,105,99}), new StringMatchRestriction(new int[] {98,111,111,108}), new StringMatchRestriction(new int[] {118,97,108,117,101}), new StringMatchRestriction(new int[] {98,114,101,97,107}), new StringMatchRestriction(new int[] {102,105,108,116,101,114}), new StringMatchRestriction(new int[] {100,97,116,101,116,105,109,101}), new StringMatchRestriction(new int[] {97,115,115,101,114,116}), new StringMatchRestriction(new int[] {108,111,99}), new StringMatchRestriction(new int[] {100,101,102,97,117,108,116}), new StringMatchRestriction(new int[] {116,104,114,111,119,115}), new StringMatchRestriction(new int[] {116,117,112,108,101}), new StringMatchRestriction(new int[] {102,111,114}), new StringMatchRestriction(new int[] {118,105,115,105,116}), new StringMatchRestriction(new int[] {97,108,105,97,115}), new StringMatchRestriction(new int[] {109,97,112})});
      builder.addAlternative(RascalRascal.prod__$Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalKeywords_, tmp);
	}
    protected static final void _init_prod__$Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new ListStackNode<IConstructor>(-1865, 2, regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(-1866, 0, new int[][]{{45,45},{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{45,45},{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode<IConstructor>(-1864, 1, new int[][]{{65,90},{95,95},{97,122}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-1863, 0, new int[][]{{92,92}}, null, null);
      builder.addAlternative(RascalRascal.prod__$Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalKeywords_(builder);
      
        _init_prod__$Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class $Start {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__absent_$Start__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(-1868, 0);
      builder.addAlternative(RascalRascal.prod__absent_$Start__, tmp);
	}
    protected static final void _init_prod__present_$Start__lit_start_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-1869, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__present_$Start__lit_start_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__absent_$Start__(builder);
      
        _init_prod__present_$Start__lit_start_(builder);
      
    }
  }
	
  protected static class $Formals {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Formals__formals_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(-1872, 0, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1873, 0, "$Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1874, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-1875, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-1876, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalRascal.prod__default_$Formals__formals_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Formals__formals_iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Tags {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Tags__tags_iter_star_seps__$Tag__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(-1877, 0, regular__iter_star_seps__$Tag__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-1878, 0, "$Tag", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-1879, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(RascalRascal.prod__default_$Tags__tags_iter_star_seps__$Tag__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Tags__tags_iter_star_seps__$Tag__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class start__$Commands {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__$Commands__$layouts_LAYOUTLIST_top_$Commands_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1882, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1881, 1, "$Commands", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1880, 0, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalRascal.prod__start__$Commands__$layouts_LAYOUTLIST_top_$Commands_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__$Commands__$layouts_LAYOUTLIST_top_$Commands_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $CaseInsensitiveStringConstant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$CaseInsensitiveStringConstant__lit___39_iter_star__$StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-1886, 2, prod__lit___39__char_class___range__39_39_, new int[] {39}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-1884, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode<IConstructor>(-1885, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1883, 0, prod__lit___39__char_class___range__39_39_, new int[] {39}, null, null);
      builder.addAlternative(RascalRascal.prod__$CaseInsensitiveStringConstant__lit___39_iter_star__$StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$CaseInsensitiveStringConstant__lit___39_iter_star__$StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $Backslash {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(-1887, 0, new int[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{47,47},{60,60},{62,62},{92,92}})});
      builder.addAlternative(RascalRascal.prod__$Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$Backslash__conditional__char_class___range__92_92__not_follow__char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
    }
  }
	
  protected static class $JustDate {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$JustDate__lit___36_$DatePart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1910, 1, "$DatePart", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1909, 0, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      builder.addAlternative(RascalRascal.prod__$JustDate__lit___36_$DatePart_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$JustDate__lit___36_$DatePart_(builder);
      
    }
  }
	
  protected static class $MidProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MidProtocolChars__lit___62_$URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-1918, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1917, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1916, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(RascalRascal.prod__$MidProtocolChars__lit___62_$URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$MidProtocolChars__lit___62_$URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class $PathTail {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__mid_$PathTail__mid_$MidPathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1923, 4, "$PathTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1922, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1921, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1920, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1919, 0, "$MidPathChars", null, null);
      builder.addAlternative(RascalRascal.prod__mid_$PathTail__mid_$MidPathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_, tmp);
	}
    protected static final void _init_prod__post_$PathTail__post_$PostPathChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1924, 0, "$PostPathChars", null, null);
      builder.addAlternative(RascalRascal.prod__post_$PathTail__post_$PostPathChars_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__mid_$PathTail__mid_$MidPathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_(builder);
      
        _init_prod__post_$PathTail__post_$PostPathChars_(builder);
      
    }
  }
	
  protected static class $PatternWithAction {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__arbitrary_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1939, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1938, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1937, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1936, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1935, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__arbitrary_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement_, tmp);
	}
    protected static final void _init_prod__replacing_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_replacement_$Replacement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1944, 4, "$Replacement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1943, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1942, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new int[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1941, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1940, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__replacing_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_replacement_$Replacement_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__arbitrary_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement_(builder);
      
        _init_prod__replacing_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_replacement_$Replacement_(builder);
      
    }
  }
	
  protected static class $DataTypeSelector {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__selector_$DataTypeSelector__sort_$QualifiedName_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_production_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1949, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1948, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-1947, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1946, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1945, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__selector_$DataTypeSelector__sort_$QualifiedName_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_production_$Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__selector_$DataTypeSelector__sort_$QualifiedName_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_production_$Name_(builder);
      
    }
  }
	
  protected static class $StringTail {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__post_$StringTail__post_$PostStringChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1950, 0, "$PostStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__post_$StringTail__post_$PostStringChars_, tmp);
	}
    protected static final void _init_prod__midTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1955, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1954, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1953, 2, "$StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1952, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1951, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__midTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    protected static final void _init_prod__midInterpolated_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-1960, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-1959, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1958, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1957, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1956, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__midInterpolated_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__post_$StringTail__post_$PostStringChars_(builder);
      
        _init_prod__midTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(builder);
      
        _init_prod__midInterpolated_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(builder);
      
    }
  }
	
  protected static class start__$Module {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__$Module__$layouts_LAYOUTLIST_top_$Module_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1963, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1962, 1, "$Module", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-1961, 0, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalRascal.prod__start__$Module__$layouts_LAYOUTLIST_top_$Module_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__$Module__$layouts_LAYOUTLIST_top_$Module_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $StringConstant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$StringConstant__lit___34_iter_star__$StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-1982, 2, prod__lit___34__char_class___range__34_34_, new int[] {34}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-1980, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode<IConstructor>(-1981, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1979, 0, prod__lit___34__char_class___range__34_34_, new int[] {34}, null, null);
      builder.addAlternative(RascalRascal.prod__$StringConstant__lit___34_iter_star__$StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$StringConstant__lit___34_iter_star__$StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class $LocalVariableDeclaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__dynamic_$LocalVariableDeclaration__lit_dynamic_$layouts_LAYOUTLIST_declarator_$Declarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-1999, 2, "$Declarator", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-1998, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-1997, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new int[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__dynamic_$LocalVariableDeclaration__lit_dynamic_$layouts_LAYOUTLIST_declarator_$Declarator_, tmp);
	}
    protected static final void _init_prod__default_$LocalVariableDeclaration__declarator_$Declarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2000, 0, "$Declarator", null, null);
      builder.addAlternative(RascalRascal.prod__default_$LocalVariableDeclaration__declarator_$Declarator_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__dynamic_$LocalVariableDeclaration__lit_dynamic_$layouts_LAYOUTLIST_declarator_$Declarator_(builder);
      
        _init_prod__default_$LocalVariableDeclaration__declarator_$Declarator_(builder);
      
    }
  }
	
  protected static class $Parameters {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-2023, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2022, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2021, 2, "$Formals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2020, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2019, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__default_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__varArgs_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___46_46_46_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-2030, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2029, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-2028, 4, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new int[] {46,46,46}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2027, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2026, 2, "$Formals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2025, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2024, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__varArgs_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___46_46_46_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__varArgs_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___46_46_46_$layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class $DatePart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[8];
      
      tmp[7] = new CharStackNode<IConstructor>(-2038, 7, new int[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(-2037, 6, new int[][]{{48,51}}, null, null);
      tmp[5] = new CharStackNode<IConstructor>(-2036, 5, new int[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(-2035, 4, new int[][]{{48,49}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(-2034, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-2033, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-2032, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-2031, 0, new int[][]{{48,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[10];
      
      tmp[9] = new CharStackNode<IConstructor>(-2048, 9, new int[][]{{48,57}}, null, null);
      tmp[8] = new CharStackNode<IConstructor>(-2047, 8, new int[][]{{48,51}}, null, null);
      tmp[7] = new LiteralStackNode<IConstructor>(-2046, 7, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(-2045, 6, new int[][]{{48,57}}, null, null);
      tmp[5] = new CharStackNode<IConstructor>(-2044, 5, new int[][]{{48,49}}, null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-2043, 4, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(-2042, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(-2041, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(-2040, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(-2039, 0, new int[][]{{48,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_(builder);
      
        _init_prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class $HeaderKeyword {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$HeaderKeyword__lit_lexical_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2069, 0, prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_, new int[] {108,101,120,105,99,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_lexical_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_layout_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2070, 0, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new int[] {108,97,121,111,117,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_layout_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_extend_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2071, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_extend_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_syntax_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2072, 0, prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_, new int[] {115,121,110,116,97,120}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_syntax_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_keyword_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2073, 0, prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_, new int[] {107,101,121,119,111,114,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_keyword_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_import_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2074, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_import_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_start_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2075, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_start_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
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
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__tuple_$Assignable__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-2103, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2102, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-2097, 2, regular__iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2098, 0, "$Assignable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2099, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2100, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-2101, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2096, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2095, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__tuple_$Assignable__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-2108, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2107, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2106, 2, "$Assignable", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2105, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2104, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__fieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2120, 0, "$Assignable", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2121, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2122, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2123, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2124, 4, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__fieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_, tmp);
	}
    protected static final void _init_prod__constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-2119, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2118, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-2113, 4, regular__iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2114, 0, "$Assignable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2115, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2116, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-2117, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2112, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2111, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2110, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2109, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2129, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2128, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2127, 2, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2126, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2125, 0, "$Assignable", null, null);
      builder.addAlternative(RascalRascal.prod__annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_, tmp);
	}
    protected static final void _init_prod__ifDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2134, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2133, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2132, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2131, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2130, 0, "$Assignable", null, null);
      builder.addAlternative(RascalRascal.prod__ifDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_, tmp);
	}
    protected static final void _init_prod__variable_$Assignable__qualifiedName_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2135, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__variable_$Assignable__qualifiedName_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__subscript_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscript_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-2142, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2141, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2140, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2139, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2138, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2137, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2136, 0, "$Assignable", null, null);
      builder.addAlternative(RascalRascal.prod__subscript_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscript_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__tuple_$Assignable__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__fieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(builder);
      
        _init_prod__constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_(builder);
      
        _init_prod__ifDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_(builder);
      
        _init_prod__variable_$Assignable__qualifiedName_$QualifiedName_(builder);
      
        _init_prod__subscript_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscript_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class $EvalCommand {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__statement_$EvalCommand__statement_$Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2146, 0, "$Statement", null, null);
      builder.addAlternative(RascalRascal.prod__statement_$EvalCommand__statement_$Statement_, tmp);
	}
    protected static final void _init_prod__declaration_$EvalCommand__declaration_$Declaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2147, 0, "$Declaration", null, null);
      builder.addAlternative(RascalRascal.prod__declaration_$EvalCommand__declaration_$Declaration_, tmp);
	}
    protected static final void _init_prod__import_$EvalCommand__imported_$Import_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2148, 0, "$Import", null, null);
      builder.addAlternative(RascalRascal.prod__import_$EvalCommand__imported_$Import_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__statement_$EvalCommand__statement_$Statement_(builder);
      
        _init_prod__declaration_$EvalCommand__declaration_$Declaration_(builder);
      
        _init_prod__import_$EvalCommand__imported_$Import_(builder);
      
    }
  }
	
  protected static class $FunctionModifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__test_$FunctionModifier__lit_test_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2143, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new int[] {116,101,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__test_$FunctionModifier__lit_test_, tmp);
	}
    protected static final void _init_prod__default_$FunctionModifier__lit_default_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2144, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(RascalRascal.prod__default_$FunctionModifier__lit_default_, tmp);
	}
    protected static final void _init_prod__java_$FunctionModifier__lit_java_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2145, 0, prod__lit_java__char_class___range__106_106_char_class___range__97_97_char_class___range__118_118_char_class___range__97_97_, new int[] {106,97,118,97}, null, null);
      builder.addAlternative(RascalRascal.prod__java_$FunctionModifier__lit_java_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__test_$FunctionModifier__lit_test_(builder);
      
        _init_prod__default_$FunctionModifier__lit_default_(builder);
      
        _init_prod__java_$FunctionModifier__lit_java_(builder);
      
    }
  }
	
  protected static class $Assignment {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__addition_$Assignment__lit___43_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2152, 0, prod__lit___43_61__char_class___range__43_43_char_class___range__61_61_, new int[] {43,61}, null, null);
      builder.addAlternative(RascalRascal.prod__addition_$Assignment__lit___43_61_, tmp);
	}
    protected static final void _init_prod__product_$Assignment__lit___42_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2153, 0, prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_, new int[] {42,61}, null, null);
      builder.addAlternative(RascalRascal.prod__product_$Assignment__lit___42_61_, tmp);
	}
    protected static final void _init_prod__division_$Assignment__lit___47_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2154, 0, prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_, new int[] {47,61}, null, null);
      builder.addAlternative(RascalRascal.prod__division_$Assignment__lit___47_61_, tmp);
	}
    protected static final void _init_prod__ifDefined_$Assignment__lit___63_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2155, 0, prod__lit___63_61__char_class___range__63_63_char_class___range__61_61_, new int[] {63,61}, null, null);
      builder.addAlternative(RascalRascal.prod__ifDefined_$Assignment__lit___63_61_, tmp);
	}
    protected static final void _init_prod__default_$Assignment__lit___61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2156, 0, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      builder.addAlternative(RascalRascal.prod__default_$Assignment__lit___61_, tmp);
	}
    protected static final void _init_prod__append_$Assignment__lit___60_60_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2157, 0, prod__lit___60_60_61__char_class___range__60_60_char_class___range__60_60_char_class___range__61_61_, new int[] {60,60,61}, null, null);
      builder.addAlternative(RascalRascal.prod__append_$Assignment__lit___60_60_61_, tmp);
	}
    protected static final void _init_prod__intersection_$Assignment__lit___38_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2158, 0, prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_, new int[] {38,61}, null, null);
      builder.addAlternative(RascalRascal.prod__intersection_$Assignment__lit___38_61_, tmp);
	}
    protected static final void _init_prod__subtraction_$Assignment__lit___45_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2159, 0, prod__lit___45_61__char_class___range__45_45_char_class___range__61_61_, new int[] {45,61}, null, null);
      builder.addAlternative(RascalRascal.prod__subtraction_$Assignment__lit___45_61_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__addition_$Assignment__lit___43_61_(builder);
      
        _init_prod__product_$Assignment__lit___42_61_(builder);
      
        _init_prod__division_$Assignment__lit___47_61_(builder);
      
        _init_prod__ifDefined_$Assignment__lit___63_61_(builder);
      
        _init_prod__default_$Assignment__lit___61_(builder);
      
        _init_prod__append_$Assignment__lit___60_60_61_(builder);
      
        _init_prod__intersection_$Assignment__lit___38_61_(builder);
      
        _init_prod__subtraction_$Assignment__lit___45_61_(builder);
      
    }
  }
	
  protected static class $ProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$ProtocolChars__char_class___range__124_124_$URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-2151, 2, prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_, new int[] {58,47,47}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,10},{13,13},{32,32}})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2150, 1, "$URLChars", null, null);
      tmp[0] = new CharStackNode<IConstructor>(-2149, 0, new int[][]{{124,124}}, null, null);
      builder.addAlternative(RascalRascal.prod__$ProtocolChars__char_class___range__124_124_$URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$ProtocolChars__char_class___range__124_124_$URLChars_conditional__lit___58_47_47__not_follow__char_class___range__9_10_range__13_13_range__32_32_(builder);
      
    }
  }
	
  protected static class $Field {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__name_$Field__fieldName_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2169, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__name_$Field__fieldName_$Name_, tmp);
	}
    protected static final void _init_prod__index_$Field__fieldIndex_$IntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2170, 0, "$IntegerLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__index_$Field__fieldIndex_$IntegerLiteral_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__name_$Field__fieldName_$Name_(builder);
      
        _init_prod__index_$Field__fieldIndex_$IntegerLiteral_(builder);
      
    }
  }
	
  protected static class $Comment {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Comment__lit___47_47_conditional__iter_star__char_class___range__0_9_range__11_16777215__not_follow__char_class___range__9_9_range__13_13_range__32_32_end_of_line__tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(-2182, 1, regular__iter_star__char_class___range__0_9_range__11_16777215, new CharStackNode<IConstructor>(-2183, 0, new int[][]{{0,9},{11,16777215}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,9},{13,13},{32,32}}), new AtEndOfLineRequirement()});
      tmp[0] = new LiteralStackNode<IConstructor>(-2181, 0, prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_, new int[] {47,47}, null, null);
      builder.addAlternative(RascalRascal.prod__$Comment__lit___47_47_conditional__iter_star__char_class___range__0_9_range__11_16777215__not_follow__char_class___range__9_9_range__13_13_range__32_32_end_of_line__tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__$Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-2189, 2, prod__lit___42_47__char_class___range__42_42_char_class___range__47_47_, new int[] {42,47}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-2185, 1, regular__iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215, new AlternativeStackNode<IConstructor>(-2186, 0, regular__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(-2187, 0, new int[][]{{42,42}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{47,47}})}), new CharStackNode<IConstructor>(-2188, 0, new int[][]{{0,41},{43,16777215}}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2184, 0, prod__lit___47_42__char_class___range__47_47_char_class___range__42_42_, new int[] {47,42}, null, null);
      builder.addAlternative(RascalRascal.prod__$Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$Comment__lit___47_47_conditional__iter_star__char_class___range__0_9_range__11_16777215__not_follow__char_class___range__9_9_range__13_13_range__32_32_end_of_line__tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__$Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116(builder);
      
    }
  }
	
  protected static class $Label {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Label__name_$Name_$layouts_LAYOUTLIST_lit___58_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-2202, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2201, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2200, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__default_$Label__name_$Name_$layouts_LAYOUTLIST_lit___58_, tmp);
	}
    protected static final void _init_prod__empty_$Label__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(-2203, 0);
      builder.addAlternative(RascalRascal.prod__empty_$Label__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Label__name_$Name_$layouts_LAYOUTLIST_lit___58_(builder);
      
        _init_prod__empty_$Label__(builder);
      
    }
  }
	
  protected static class $ProtocolPart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__nonInterpolated_$ProtocolPart__protocolChars_$ProtocolChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2204, 0, "$ProtocolChars", null, null);
      builder.addAlternative(RascalRascal.prod__nonInterpolated_$ProtocolPart__protocolChars_$ProtocolChars_, tmp);
	}
    protected static final void _init_prod__interpolated_$ProtocolPart__pre_$PreProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2209, 4, "$ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2208, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2207, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2206, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2205, 0, "$PreProtocolChars", null, null);
      builder.addAlternative(RascalRascal.prod__interpolated_$ProtocolPart__pre_$PreProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__nonInterpolated_$ProtocolPart__protocolChars_$ProtocolChars_(builder);
      
        _init_prod__interpolated_$ProtocolPart__pre_$PreProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(builder);
      
    }
  }
	
  protected static class $PreModule {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_$Rest_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2224, 4, "$Rest", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2223, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new EmptyStackNode<IConstructor>(-2222, 2, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {108,101,120,105,99,97,108}), new StringFollowRestriction(new int[] {105,109,112,111,114,116}), new StringFollowRestriction(new int[] {115,116,97,114,116}), new StringFollowRestriction(new int[] {115,121,110,116,97,120}), new StringFollowRestriction(new int[] {108,97,121,111,117,116}), new StringFollowRestriction(new int[] {101,120,116,101,110,100}), new StringFollowRestriction(new int[] {107,101,121,119,111,114,100})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2221, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2220, 0, "$Header", null, null);
      builder.addAlternative(RascalRascal.prod__default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_$Rest_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_$Rest_(builder);
      
    }
  }
	
  protected static class $TagString {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$TagString__lit___123_contents_iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-2242, 2, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(-2235, 1, regular__iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125, new AlternativeStackNode<IConstructor>(-2236, 0, regular__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2237, 0, "$TagString", null, null), new CharStackNode<IConstructor>(-2238, 0, new int[][]{{0,122},{124,124},{126,16777215}}, null, null), new SequenceStackNode<IConstructor>(-2239, 0, regular__seq___lit___92_char_class___range__123_123_range__125_125, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new LiteralStackNode<IConstructor>(-2240, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null), new CharStackNode<IConstructor>(-2241, 1, new int[][]{{123,123},{125,125}}, null, null)}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2234, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__$TagString__lit___123_contents_iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$TagString__lit___123_contents_iter_star__alt___$TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_(builder);
      
    }
  }
	
  protected static class $Nonterminal {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalReservedKeywords_not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(-2244, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(-2245, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(-2243, 0, new int[][]{{65,90}}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{65,90}})}, null);
      builder.addAlternative(RascalRascal.prod__$Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalReservedKeywords_not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$Nonterminal__conditional__char_class___range__65_90__not_precede__char_class___range__65_90_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalReservedKeywords_not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class $Mapping__$Pattern {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Mapping__$Pattern__from_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2262, 4, "$Pattern", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2261, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2260, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2259, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2258, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__default_$Mapping__$Pattern__from_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Pattern_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Mapping__$Pattern__from_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Pattern_(builder);
      
    }
  }
	
  protected static class $Expression {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2279, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2280, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2281, 2, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2282, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-2283, 4, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new int[] {46,46}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2284, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-2285, 6, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-2286, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-2287, 8, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      builder.addAlternative(RascalRascal.prod__range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__transitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-2293, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {61})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2292, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2291, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__transitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_, tmp);
	}
    protected static final void _init_prod__division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2529, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2528, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2527, 2, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2526, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2525, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__literal_$Expression__literal_$Literal_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2294, 0, "$Literal", null, null);
      builder.addAlternative(RascalRascal.prod__literal_$Expression__literal_$Literal_, tmp);
	}
    protected static final void _init_prod__negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2516, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2515, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2514, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      builder.addAlternative(RascalRascal.prod__negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_, tmp);
	}
    protected static final void _init_prod__callOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-2314, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2313, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-2308, 4, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2309, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2310, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2311, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-2312, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2307, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2306, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2305, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2304, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__callOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__comprehension_$Expression__comprehension_$Comprehension_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2315, 0, "$Comprehension", null, null);
      builder.addAlternative(RascalRascal.prod__comprehension_$Expression__comprehension_$Comprehension_, tmp);
	}
    protected static final void _init_prod__subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-2329, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2328, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-2323, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2324, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2325, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2326, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-2327, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2322, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2321, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2320, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2319, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-2338, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2337, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-2332, 2, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2333, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2334, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2335, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-2336, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2331, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2330, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__appendAfter_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60_60__not_follow__lit___61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2566, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2565, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2564, 2, prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_, new int[] {60,60}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {61})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2563, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2562, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__appendAfter_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60_60__not_follow__lit___61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__lessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2601, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2600, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2599, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {45})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2598, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2597, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__lessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__remainder_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2546, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2545, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2544, 2, prod__lit___37__char_class___range__37_37_, new int[] {37}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2543, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2542, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__remainder_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__ifDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2621, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2620, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2619, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2618, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2617, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__ifDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__insertBefore_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2571, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2570, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2569, 2, prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_, new int[] {62,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2568, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2567, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__insertBefore_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2646, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2645, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2644, 2, prod__lit___61_61_62__char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new int[] {61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2643, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2642, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2524, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2523, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2522, 2, prod__lit_o__char_class___range__111_111_, new int[] {111}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2521, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2520, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__nonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2616, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2615, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2614, 2, prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_, new int[] {33,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2613, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2612, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__nonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__ifThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode<IConstructor>(-2665, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-2664, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-2663, 6, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2662, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2661, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2660, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2659, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2658, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2657, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__ifThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__asType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-2510, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2509, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-2508, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2507, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2506, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2505, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2504, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__asType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_, tmp);
	}
    protected static final void _init_prod__fieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2396, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2395, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2394, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2393, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2392, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__fieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_, tmp);
	}
    protected static final void _init_prod__bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-2401, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2400, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2399, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2398, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2397, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__qualifiedName_$Expression__qualifiedName_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2402, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__qualifiedName_$Expression__qualifiedName_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2556, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2555, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2554, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2553, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2552, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__noMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2626, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2625, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2624, 2, prod__lit___33_58_61__char_class___range__33_33_char_class___range__58_58_char_class___range__61_61_, new int[] {33,58,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2623, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2622, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__noMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__fieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-2413, 6, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2412, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-2407, 4, regular__iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2408, 0, "$Field", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2409, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2410, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-2411, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2406, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2405, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2404, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2403, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__fieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__getAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2418, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2417, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2416, 2, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2415, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2414, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__getAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2641, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2640, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2639, 2, prod__lit___60_61_61_62__char_class___range__60_60_char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new int[] {60,61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2638, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2637, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__isDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-2503, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2502, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2501, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__isDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_, tmp);
	}
    protected static final void _init_prod__negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2519, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2518, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2517, 0, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      builder.addAlternative(RascalRascal.prod__negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_, tmp);
	}
    protected static final void _init_prod__greaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2591, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2590, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2589, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2588, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2587, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__greaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__nonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-2456, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2455, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-2452, 2, regular__iter_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2453, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2454, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2451, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2450, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__nonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__greaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2596, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2595, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2594, 2, prod__lit___62_61__char_class___range__62_62_char_class___range__61_61_, new int[] {62,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2593, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2592, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__greaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2631, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2630, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2629, 2, prod__lit___60_45__char_class___range__60_60_char_class___range__45_45_, new int[] {60,45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2628, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2627, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2561, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2560, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2559, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2558, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2557, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-2467, 8, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-2466, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-2463, 6, regular__iter_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2464, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2465, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2462, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-2461, 4, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2460, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2459, 2, "$Parameters", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2458, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2457, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__notIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2586, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2585, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2584, 2, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new int[] {110,111,116,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2583, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2582, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__notIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-2476, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2475, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-2470, 2, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2471, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2472, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2473, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-2474, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2469, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2468, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__match_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2636, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2635, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2634, 2, prod__lit___58_61__char_class___range__58_58_char_class___range__61_61_, new int[] {58,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2633, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2632, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__match_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__transitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-2290, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {61})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2289, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2288, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__transitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_, tmp);
	}
    protected static final void _init_prod__reifiedType_$Expression__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Expression_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(-2278, 10, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-2277, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-2276, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-2275, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-2274, 6, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2273, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2272, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2271, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2270, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2269, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2268, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalRascal.prod__reifiedType_$Expression__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Expression_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2534, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2533, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2532, 2, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new int[] {106,111,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2531, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2530, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__and_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2651, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2650, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2649, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new int[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2648, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2647, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__and_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__in_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2581, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2580, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2579, 2, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new int[] {105,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2578, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2577, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__in_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__list_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-2303, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2302, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-2297, 2, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2298, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2299, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2300, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-2301, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2296, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2295, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__list_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2551, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2550, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2549, 2, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2548, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2547, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2318, 2, "$Visit", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2317, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2316, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_, tmp);
	}
    protected static final void _init_prod__product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit___42_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-2541, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2540, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new EmptyStackNode<IConstructor>(-2539, 4, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {42})});
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2538, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2537, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2536, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2535, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit___42_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__reifyType_$Expression__lit___35_$layouts_LAYOUTLIST_conditional__type_$Type__not_follow__lit___91_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2341, 2, "$Type", null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {91})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2340, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2339, 0, prod__lit___35__char_class___range__35_35_, new int[] {35}, null, null);
      builder.addAlternative(RascalRascal.prod__reifyType_$Expression__lit___35_$layouts_LAYOUTLIST_conditional__type_$Type__not_follow__lit___91_, tmp);
	}
    protected static final void _init_prod__all_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-2352, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2351, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-2346, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2347, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2348, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2349, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-2350, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2345, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2344, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2343, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2342, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new int[] {97,108,108}, null, null);
      builder.addAlternative(RascalRascal.prod__all_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__stepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(-2365, 12, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-2364, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(-2363, 10, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-2362, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-2361, 8, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new int[] {46,46}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-2360, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-2359, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2358, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-2357, 4, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2356, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2355, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2354, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2353, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__stepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__voidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2366, 0, "$Parameters", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2367, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2368, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2369, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-2370, 4, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2371, 0, "$Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2372, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2373, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-2374, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      builder.addAlternative(RascalRascal.prod__voidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2611, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2610, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2609, 2, prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_, new int[] {61,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2608, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2607, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__fieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(-2391, 10, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-2390, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(-2389, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-2388, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-2387, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2386, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2385, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2384, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2383, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2382, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2381, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__fieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2380, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2379, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2378, 2, prod__lit_has__char_class___range__104_104_char_class___range__97_97_char_class___range__115_115_, new int[] {104,97,115}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2377, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2376, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__it_$Expression__conditional__lit_it__not_precede__char_class___range__65_90_range__95_95_range__97_122_not_follow__char_class___range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2375, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new int[] {105,116}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{65,90},{95,95},{97,122}})}, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{65,90},{95,95},{97,122}})});
      builder.addAlternative(RascalRascal.prod__it_$Expression__conditional__lit_it__not_precede__char_class___range__65_90_range__95_95_range__97_122_not_follow__char_class___range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__splice_$Expression__lit___42_$layouts_LAYOUTLIST_argument_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2513, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2512, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2511, 0, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      builder.addAlternative(RascalRascal.prod__splice_$Expression__lit___42_$layouts_LAYOUTLIST_argument_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(-2427, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2426, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(-2421, 2, regular__iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2422, 0, "$Mapping__$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2423, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2424, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-2425, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2420, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2419, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2432, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2431, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2430, 2, prod__lit_is__char_class___range__105_105_char_class___range__115_115_, new int[] {105,115}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2429, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2428, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(-2449, 12, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-2448, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(-2443, 10, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2444, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2445, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2446, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-2447, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-2442, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-2441, 8, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-2440, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-2439, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2438, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-2437, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2436, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2435, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2434, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2433, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2656, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2655, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2654, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new int[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2653, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2652, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-2487, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2486, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-2481, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2482, 0, "$Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2483, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2484, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-2485, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2480, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2479, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2478, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2477, 0, prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_, new int[] {97,110,121}, null, null);
      builder.addAlternative(RascalRascal.prod__any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_mod_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2576, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2575, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2574, 2, prod__lit_mod__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_, new int[] {109,111,100}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2573, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2572, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_mod_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__lessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2606, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2605, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2604, 2, prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_, new int[] {60,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2603, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2602, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__lessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__setAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(-2500, 12, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(-2499, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(-2498, 10, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-2497, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-2496, 8, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-2495, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-2494, 6, "$Name", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2493, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-2492, 4, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2491, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2490, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2489, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2488, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__setAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__transitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_(builder);
      
        _init_prod__division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__literal_$Expression__literal_$Literal_(builder);
      
        _init_prod__negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_(builder);
      
        _init_prod__callOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__comprehension_$Expression__comprehension_$Comprehension_(builder);
      
        _init_prod__subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__appendAfter_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60_60__not_follow__lit___61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__lessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__remainder_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__ifDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__insertBefore_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__nonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__ifThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__right(builder);
      
        _init_prod__asType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_(builder);
      
        _init_prod__fieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(builder);
      
        _init_prod__bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__qualifiedName_$Expression__qualifiedName_$QualifiedName_(builder);
      
        _init_prod__addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__noMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(builder);
      
        _init_prod__fieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__getAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__isDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_(builder);
      
        _init_prod__negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_(builder);
      
        _init_prod__greaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__nonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__greaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(builder);
      
        _init_prod__subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__notIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__match_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(builder);
      
        _init_prod__transitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_(builder);
      
        _init_prod__reifiedType_$Expression__lit_type_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_symbol_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_definitions_$Expression_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__and_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__in_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__list_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_(builder);
      
        _init_prod__product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit___42_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__reifyType_$Expression__lit___35_$layouts_LAYOUTLIST_conditional__type_$Type__not_follow__lit___91_(builder);
      
        _init_prod__all_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__stepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__voidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__fieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__it_$Expression__conditional__lit_it__not_precede__char_class___range__65_90_range__95_95_range__97_122_not_follow__char_class___range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__splice_$Expression__lit___42_$layouts_LAYOUTLIST_argument_$Expression__assoc__non_assoc(builder);
      
        _init_prod__map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_mod_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(builder);
      
        _init_prod__lessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(builder);
      
        _init_prod__setAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class $ImportedModule {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__actuals_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2683, 2, "$ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2682, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2681, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__actuals_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_, tmp);
	}
    protected static final void _init_prod__actualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2688, 4, "$Renamings", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2687, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2686, 2, "$ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2685, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2684, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__actualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_, tmp);
	}
    protected static final void _init_prod__default_$ImportedModule__name_$QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2689, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__default_$ImportedModule__name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__renamings_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_renamings_$Renamings_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2692, 2, "$Renamings", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2691, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2690, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__renamings_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_renamings_$Renamings_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__actuals_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_(builder);
      
        _init_prod__actualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_(builder);
      
        _init_prod__default_$ImportedModule__name_$QualifiedName_(builder);
      
        _init_prod__renamings_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_renamings_$Renamings_(builder);
      
    }
  }
	
  protected static class $Commands {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__list_$Commands__commands_iter_seps__$EvalCommand__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(-2717, 0, regular__iter_seps__$EvalCommand__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2718, 0, "$EvalCommand", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2719, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      builder.addAlternative(RascalRascal.prod__list_$Commands__commands_iter_seps__$EvalCommand__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__list_$Commands__commands_iter_seps__$EvalCommand__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Header {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new SeparatedListStackNode<IConstructor>(-2736, 6, regular__iter_star_seps__$Import__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2737, 0, "$Import", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2738, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2735, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2734, 4, "$QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2733, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2732, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2731, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2730, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__default_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__parameters_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_params_$ModuleParameters_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new SeparatedListStackNode<IConstructor>(-2747, 8, regular__iter_star_seps__$Import__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2748, 0, "$Import", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2749, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-2746, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-2745, 6, "$ModuleParameters", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2744, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2743, 4, "$QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2742, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2741, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2740, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2739, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__parameters_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_params_$ModuleParameters_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_(builder);
      
        _init_prod__parameters_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_params_$ModuleParameters_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $NonterminalLabel {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(-2751, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(-2752, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(-2750, 0, new int[][]{{97,122}}, null, null);
      builder.addAlternative(RascalRascal.prod__$NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$NonterminalLabel__char_class___range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class $Sym {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2760, 2, "$Nonterminal", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2759, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2758, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      builder.addAlternative(RascalRascal.prod__parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_, tmp);
	}
    protected static final void _init_prod__endOfLine_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___36_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2761, 0, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2762, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2763, 2, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      builder.addAlternative(RascalRascal.prod__endOfLine_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___36_, tmp);
	}
    protected static final void _init_prod__except_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_$layouts_LAYOUTLIST_label_$NonterminalLabel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2768, 4, "$NonterminalLabel", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2767, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2766, 2, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2765, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2764, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__except_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_$layouts_LAYOUTLIST_label_$NonterminalLabel_, tmp);
	}
    protected static final void _init_prod__optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-2771, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2770, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2769, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_, tmp);
	}
    protected static final void _init_prod__caseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2772, 0, "$CaseInsensitiveStringConstant", null, null);
      builder.addAlternative(RascalRascal.prod__caseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_, tmp);
	}
    protected static final void _init_prod__literal_$Sym__string_$StringConstant_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2782, 0, "$StringConstant", null, null);
      builder.addAlternative(RascalRascal.prod__literal_$Sym__string_$StringConstant_, tmp);
	}
    protected static final void _init_prod__iterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-2781, 8, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-2780, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-2779, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2778, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2777, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2776, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2775, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2774, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2773, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__iterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_, tmp);
	}
    protected static final void _init_prod__iterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-2785, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2784, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2783, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__iterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__notFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2873, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2872, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2871, 2, prod__lit___33_62_62__char_class___range__33_33_char_class___range__62_62_char_class___range__62_62_, new int[] {33,62,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2870, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2869, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__notFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2878, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2877, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2876, 2, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2875, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2874, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__parametrized_$Sym__conditional__nonterminal_$Nonterminal__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-2799, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2798, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-2793, 4, regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2794, 0, "$Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2795, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2796, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-2797, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2792, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2791, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2789, 0, "$Nonterminal", null, new ICompletionFilter[] {new StringFollowRequirement(new int[] {91})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2790, 1, "$layouts_LAYOUTLIST", null, null);
      builder.addAlternative(RascalRascal.prod__parametrized_$Sym__conditional__nonterminal_$Nonterminal__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__startOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2788, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2787, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2786, 0, prod__lit___94__char_class___range__94_94_, new int[] {94}, null, null);
      builder.addAlternative(RascalRascal.prod__startOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_, tmp);
	}
    protected static final void _init_prod__empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode<IConstructor>(-2800, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2801, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2802, 2, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      builder.addAlternative(RascalRascal.prod__empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__nonterminal_$Sym__conditional__nonterminal_$Nonterminal__not_follow__lit___91_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2803, 0, "$Nonterminal", null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {91})});
      builder.addAlternative(RascalRascal.prod__nonterminal_$Sym__conditional__nonterminal_$Nonterminal__not_follow__lit___91_, tmp);
	}
    protected static final void _init_prod__sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-2817, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2816, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(-2813, 4, regular__iter_seps__$Sym__$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2814, 0, "$Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2815, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2812, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2811, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2810, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2809, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2808, 4, "$IntegerLiteral", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2807, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2806, 2, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2805, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2804, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2858, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2857, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2856, 2, prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_, new int[] {60,60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2855, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2854, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2868, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2867, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2866, 2, prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_, new int[] {62,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2865, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2864, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__characterClass_$Sym__charClass_$Class_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2840, 0, "$Class", null, null);
      builder.addAlternative(RascalRascal.prod__characterClass_$Sym__charClass_$Class_, tmp);
	}
    protected static final void _init_prod__iterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-2839, 8, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-2838, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(-2837, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2836, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2835, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2834, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2833, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2832, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2831, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__iterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(-2830, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-2829, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(-2824, 6, regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2825, 0, "$Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2826, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2827, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null), new NonTerminalStackNode<IConstructor>(-2828, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2823, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-2822, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2821, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2820, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2819, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2818, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2843, 2, "$NonterminalLabel", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2842, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2841, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_, tmp);
	}
    protected static final void _init_prod__start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(-2850, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2849, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2848, 4, "$Nonterminal", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2847, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2846, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2845, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2844, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__notPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2863, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2862, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2861, 2, prod__lit___33_60_60__char_class___range__33_33_char_class___range__60_60_char_class___range__60_60_, new int[] {33,60,60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2860, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2859, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__notPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__iter_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___43_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-2853, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2852, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2851, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__iter_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___43_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_(builder);
      
        _init_prod__endOfLine_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___36_(builder);
      
        _init_prod__except_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_$layouts_LAYOUTLIST_label_$NonterminalLabel_(builder);
      
        _init_prod__optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_(builder);
      
        _init_prod__caseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_(builder);
      
        _init_prod__literal_$Sym__string_$StringConstant_(builder);
      
        _init_prod__iterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_(builder);
      
        _init_prod__iterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__notFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(builder);
      
        _init_prod__unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left(builder);
      
        _init_prod__parametrized_$Sym__conditional__nonterminal_$Nonterminal__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__startOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_(builder);
      
        _init_prod__empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__nonterminal_$Sym__conditional__nonterminal_$Nonterminal__not_follow__lit___91_(builder);
      
        _init_prod__sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_(builder);
      
        _init_prod__precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(builder);
      
        _init_prod__follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(builder);
      
        _init_prod__characterClass_$Sym__charClass_$Class_(builder);
      
        _init_prod__iterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_(builder);
      
        _init_prod__start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__notPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(builder);
      
        _init_prod__iter_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___43_(builder);
      
    }
  }
	
  protected static class $Catch {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-2904, 6, "$Statement", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2903, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(-2902, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2901, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2900, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2899, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2898, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {99,97,116,99,104}, null, null);
      builder.addAlternative(RascalRascal.prod__binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_, tmp);
	}
    protected static final void _init_prod__default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2909, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2908, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2907, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2906, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2905, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {99,97,116,99,104}, null, null);
      builder.addAlternative(RascalRascal.prod__default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_(builder);
      
        _init_prod__default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement_(builder);
      
    }
  }
	
  protected static class $Renaming {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Renaming__from_$Name_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_to_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2897, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2896, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2895, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new int[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2894, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2893, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__default_$Renaming__from_$Name_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_to_$Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Renaming__from_$Name_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_to_$Name_(builder);
      
    }
  }
	
  protected static class $Signature {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__noThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(-2926, 6, "$Parameters", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2925, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2924, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2923, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2922, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2921, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2920, 0, "$FunctionModifiers", null, null);
      builder.addAlternative(RascalRascal.prod__noThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_, tmp);
	}
    protected static final void _init_prod__withThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit_throws_$layouts_LAYOUTLIST_exceptions_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new SeparatedListStackNode<IConstructor>(-2937, 10, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(-2938, 0, "$Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(-2939, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(-2940, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(-2941, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(-2936, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(-2935, 8, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new int[] {116,104,114,111,119,115}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(-2934, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(-2933, 6, "$Parameters", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(-2932, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2931, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2930, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2929, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2928, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2927, 0, "$FunctionModifiers", null, null);
      builder.addAlternative(RascalRascal.prod__withThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit_throws_$layouts_LAYOUTLIST_exceptions_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__noThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_(builder);
      
        _init_prod__withThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit_throws_$layouts_LAYOUTLIST_exceptions_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class $Variable {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__initialized_$Variable__name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_initial_$Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(-2950, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(-2949, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(-2948, 2, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2947, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2946, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__initialized_$Variable__name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_initial_$Expression_, tmp);
	}
    protected static final void _init_prod__unInitialized_$Variable__name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2951, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__unInitialized_$Variable__name_$Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__initialized_$Variable__name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_initial_$Expression_(builder);
      
        _init_prod__unInitialized_$Variable__name_$Name_(builder);
      
    }
  }
	
  protected static class $TypeArg {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__named_$TypeArg__type_$Type_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2944, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2943, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2942, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__named_$TypeArg__type_$Type_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__default_$TypeArg__type_$Type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2945, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__default_$TypeArg__type_$Type_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__named_$TypeArg__type_$Type_$layouts_LAYOUTLIST_name_$Name_(builder);
      
        _init_prod__default_$TypeArg__type_$Type_(builder);
      
    }
  }
	
  protected static class $PreProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$PreProtocolChars__lit___124_$URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(-2966, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2965, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(-2964, 0, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      builder.addAlternative(RascalRascal.prod__$PreProtocolChars__lit___124_$URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__$PreProtocolChars__lit___124_$URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class $Literal {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__location_$Literal__locationLiteral_$LocationLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2978, 0, "$LocationLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__location_$Literal__locationLiteral_$LocationLiteral_, tmp);
	}
    protected static final void _init_prod__real_$Literal__realLiteral_$RealLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2979, 0, "$RealLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__real_$Literal__realLiteral_$RealLiteral_, tmp);
	}
    protected static final void _init_prod__regExp_$Literal__regExpLiteral_$RegExpLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2980, 0, "$RegExpLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__regExp_$Literal__regExpLiteral_$RegExpLiteral_, tmp);
	}
    protected static final void _init_prod__rational_$Literal__rationalLiteral_$RationalLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2981, 0, "$RationalLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__rational_$Literal__rationalLiteral_$RationalLiteral_, tmp);
	}
    protected static final void _init_prod__integer_$Literal__integerLiteral_$IntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2982, 0, "$IntegerLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__integer_$Literal__integerLiteral_$IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__boolean_$Literal__booleanLiteral_$BooleanLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2983, 0, "$BooleanLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__boolean_$Literal__booleanLiteral_$BooleanLiteral_, tmp);
	}
    protected static final void _init_prod__dateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2984, 0, "$DateTimeLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__dateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_, tmp);
	}
    protected static final void _init_prod__string_$Literal__stringLiteral_$StringLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2985, 0, "$StringLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__string_$Literal__stringLiteral_$StringLiteral_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__location_$Literal__locationLiteral_$LocationLiteral_(builder);
      
        _init_prod__real_$Literal__realLiteral_$RealLiteral_(builder);
      
        _init_prod__regExp_$Literal__regExpLiteral_$RegExpLiteral_(builder);
      
        _init_prod__rational_$Literal__rationalLiteral_$RationalLiteral_(builder);
      
        _init_prod__integer_$Literal__integerLiteral_$IntegerLiteral_(builder);
      
        _init_prod__boolean_$Literal__booleanLiteral_$BooleanLiteral_(builder);
      
        _init_prod__dateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_(builder);
      
        _init_prod__string_$Literal__stringLiteral_$StringLiteral_(builder);
      
    }
  }
	
  protected static class $Module {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_$Module__header_$Header_$layouts_LAYOUTLIST_body_$Body_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(-2994, 2, "$Body", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(-2993, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(-2992, 0, "$Header", null, null);
      builder.addAlternative(RascalRascal.prod__default_$Module__header_$Header_$layouts_LAYOUTLIST_body_$Body_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_$Module__header_$Header_$layouts_LAYOUTLIST_body_$Body_(builder);
      
    }
  }
	
  public RascalRascal() {
    super();
  }

  // Parse methods    
  
  public AbstractStackNode<IConstructor>[] $Tag() {
    return $Tag.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $DateAndTime() {
    return $DateAndTime.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Pattern() {
    return $Pattern.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Prod() {
    return $Prod.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $UnicodeEscape() {
    return $UnicodeEscape.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $OctalIntegerLiteral() {
    return $OctalIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $RationalLiteral() {
    return $RationalLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Toplevel() {
    return $Toplevel.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $BooleanLiteral() {
    return $BooleanLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ProdModifier() {
    return $ProdModifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $TypeVar() {
    return $TypeVar.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Comprehension() {
    return $Comprehension.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $FunctionModifiers() {
    return $FunctionModifiers.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Type() {
    return $Type.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Declaration() {
    return $Declaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Class() {
    return $Class.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Bound() {
    return $Bound.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $FunctionType() {
    return $FunctionType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Case() {
    return $Case.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Statement() {
    return $Statement.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Renamings() {
    return $Renamings.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $StringLiteral() {
    return $StringLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Visibility() {
    return $Visibility.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PostPathChars() {
    return $PostPathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $NamedBackslash() {
    return $NamedBackslash.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Visit() {
    return $Visit.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Command() {
    return $Command.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ProtocolTail() {
    return $ProtocolTail.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PreStringChars() {
    return $PreStringChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $QualifiedName() {
    return $QualifiedName.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $URLChars() {
    return $URLChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $StringMiddle() {
    return $StringMiddle.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $TimeZonePart() {
    return $TimeZonePart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ShellCommand() {
    return $ShellCommand.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $LocationLiteral() {
    return $LocationLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Range() {
    return $Range.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Rest() {
    return $Rest.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $JustTime() {
    return $JustTime.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $StringCharacter() {
    return $StringCharacter.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $layouts_LAYOUTLIST() {
    return $layouts_LAYOUTLIST.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $DataTarget() {
    return $DataTarget.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $RealLiteral() {
    return $RealLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $layouts_$default$() {
    return $layouts_$default$.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Replacement() {
    return $Replacement.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Assoc() {
    return $Assoc.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $RegExpModifier() {
    return $RegExpModifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $MidStringChars() {
    return $MidStringChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $RegExp() {
    return $RegExp.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PathPart() {
    return $PathPart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $StringTemplate() {
    return $StringTemplate.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $DecimalIntegerLiteral() {
    return $DecimalIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Body() {
    return $Body.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $LAYOUT() {
    return $LAYOUT.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $UserType() {
    return $UserType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Import() {
    return $Import.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $FunctionBody() {
    return $FunctionBody.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__$Command() {
    return start__$Command.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $IntegerLiteral() {
    return $IntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Target() {
    return $Target.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $SyntaxDefinition() {
    return $SyntaxDefinition.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Kind() {
    return $Kind.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Mapping__$Expression() {
    return $Mapping__$Expression.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PostProtocolChars() {
    return $PostProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $MidPathChars() {
    return $MidPathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Char() {
    return $Char.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PrePathChars() {
    return $PrePathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Strategy() {
    return $Strategy.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ModuleParameters() {
    return $ModuleParameters.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $RascalKeywords() {
    return $RascalKeywords.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $NamedRegExp() {
    return $NamedRegExp.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $RegExpLiteral() {
    return $RegExpLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PostStringChars() {
    return $PostStringChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $HexIntegerLiteral() {
    return $HexIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__$EvalCommand() {
    return start__$EvalCommand.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ModuleActuals() {
    return $ModuleActuals.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PathChars() {
    return $PathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $DateTimeLiteral() {
    return $DateTimeLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $BasicType() {
    return $BasicType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $FunctionDeclaration() {
    return $FunctionDeclaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Variant() {
    return $Variant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__$PreModule() {
    return start__$PreModule.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Declarator() {
    return $Declarator.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $StructuredType() {
    return $StructuredType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $TimePartNoTZ() {
    return $TimePartNoTZ.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Name() {
    return $Name.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Start() {
    return $Start.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Formals() {
    return $Formals.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Tags() {
    return $Tags.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__$Commands() {
    return start__$Commands.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $CaseInsensitiveStringConstant() {
    return $CaseInsensitiveStringConstant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Backslash() {
    return $Backslash.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $JustDate() {
    return $JustDate.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $MidProtocolChars() {
    return $MidProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PathTail() {
    return $PathTail.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PatternWithAction() {
    return $PatternWithAction.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $DataTypeSelector() {
    return $DataTypeSelector.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $StringTail() {
    return $StringTail.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__$Module() {
    return start__$Module.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $StringConstant() {
    return $StringConstant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $LocalVariableDeclaration() {
    return $LocalVariableDeclaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Parameters() {
    return $Parameters.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $DatePart() {
    return $DatePart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $HeaderKeyword() {
    return $HeaderKeyword.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Assignable() {
    return $Assignable.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $FunctionModifier() {
    return $FunctionModifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $EvalCommand() {
    return $EvalCommand.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ProtocolChars() {
    return $ProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Assignment() {
    return $Assignment.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Field() {
    return $Field.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Comment() {
    return $Comment.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Label() {
    return $Label.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ProtocolPart() {
    return $ProtocolPart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PreModule() {
    return $PreModule.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $TagString() {
    return $TagString.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Nonterminal() {
    return $Nonterminal.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Mapping__$Pattern() {
    return $Mapping__$Pattern.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Expression() {
    return $Expression.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $ImportedModule() {
    return $ImportedModule.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Commands() {
    return $Commands.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Header() {
    return $Header.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $NonterminalLabel() {
    return $NonterminalLabel.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Sym() {
    return $Sym.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Renaming() {
    return $Renaming.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Catch() {
    return $Catch.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Signature() {
    return $Signature.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $TypeArg() {
    return $TypeArg.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Variable() {
    return $Variable.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $PreProtocolChars() {
    return $PreProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Literal() {
    return $Literal.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] $Module() {
    return $Module.EXPECTS;
  }
}