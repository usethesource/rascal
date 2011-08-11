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
    
    
    _putDontNest(result, -2607, -2607);
    _putDontNest(result, -2631, -2631);
    _putDontNest(result, -2602, -2616);
    _putDontNest(result, -2861, -2860);
    _putDontNest(result, -2567, -2567);
    _putDontNest(result, -2577, -2621);
    _putDontNest(result, -2636, -2646);
    _putDontNest(result, -2572, -2582);
    _putDontNest(result, -2414, -2666);
    _putDontNest(result, -2558, -2661);
    _putDontNest(result, -164, -164);
    _putDontNest(result, -2875, -2870);
    _putDontNest(result, -2597, -2641);
    _putDontNest(result, -2603, -2651);
    _putDontNest(result, -2542, -2661);
    _putDontNest(result, -2543, -2587);
    _putDontNest(result, -2414, -2587);
    _putDontNest(result, -408, -423);
    _putDontNest(result, -2587, -2651);
    _putDontNest(result, -2865, -2880);
    _putDontNest(result, -2578, -2656);
    _putDontNest(result, -512, -708);
    _putDontNest(result, -2507, -2666);
    _putDontNest(result, -2543, -2646);
    _putDontNest(result, -2513, -2577);
    _putDontNest(result, -2538, -2557);
    _putDontNest(result, -2414, -2552);
    _putDontNest(result, -2319, -2513);
    _putDontNest(result, -2592, -2602);
    _putDontNest(result, -2843, -2870);
    _putDontNest(result, -2656, -2666);
    _putDontNest(result, -2593, -2621);
    _putDontNest(result, -2562, -2656);
    _putDontNest(result, -2856, -2865);
    _putDontNest(result, -623, -708);
    _putDontNest(result, -2334, -2602);
    _putDontNest(result, -2547, -2626);
    _putDontNest(result, -2437, -2656);
    _putDontNest(result, -2510, -2661);
    _putDontNest(result, -2553, -2636);
    _putDontNest(result, -2548, -2631);
    _putDontNest(result, -2334, -2552);
    _putDontNest(result, -2572, -2646);
    _putDontNest(result, -2568, -2626);
    _putDontNest(result, -2567, -2631);
    _putDontNest(result, -2617, -2661);
    _putDontNest(result, -2608, -2666);
    _putDontNest(result, -2414, -2602);
    _putDontNest(result, -2387, -2621);
    _putDontNest(result, -2558, -2597);
    _putDontNest(result, -2593, -2592);
    _putDontNest(result, -2588, -2646);
    _putDontNest(result, -2583, -2631);
    _putDontNest(result, -2557, -2616);
    _putDontNest(result, -2542, -2597);
    _putDontNest(result, -60, -124);
    _putDontNest(result, -517, -509);
    _putDontNest(result, -2577, -2592);
    _putDontNest(result, -2651, -2651);
    _putDontNest(result, -2514, -2641);
    _putDontNest(result, -2517, -2656);
    _putDontNest(result, -2507, -2602);
    _putDontNest(result, -2543, -2582);
    _putDontNest(result, -2334, -2587);
    _putDontNest(result, -2507, -2516);
    _putDontNest(result, -2572, -2587);
    _putDontNest(result, -2622, -2636);
    _putDontNest(result, -2626, -2656);
    _putDontNest(result, -2616, -2626);
    _putDontNest(result, -2592, -2666);
    _putDontNest(result, -2403, -2621);
    _putDontNest(result, -2334, -2666);
    _putDontNest(result, -2547, -2562);
    _putDontNest(result, -2498, -2641);
    _putDontNest(result, -2525, -2616);
    _putDontNest(result, -2553, -2572);
    _putDontNest(result, -2510, -2597);
    _putDontNest(result, -2548, -2567);
    _putDontNest(result, -2578, -2621);
    _putDontNest(result, -2860, -2870);
    _putDontNest(result, -2607, -2602);
    _putDontNest(result, -2319, -2572);
    _putDontNest(result, -2437, -2567);
    _putDontNest(result, -2542, -2666);
    _putDontNest(result, -2507, -2547);
    _putDontNest(result, -408, -408);
    _putDontNest(result, -2562, -2621);
    _putDontNest(result, -2602, -2597);
    _putDontNest(result, -2598, -2641);
    _putDontNest(result, -2587, -2582);
    _putDontNest(result, -2593, -2656);
    _putDontNest(result, -2517, -2631);
    _putDontNest(result, -2558, -2666);
    _putDontNest(result, -512, -512);
    _putDontNest(result, -2334, -2513);
    _putDontNest(result, -2866, -2880);
    _putDontNest(result, -2588, -2651);
    _putDontNest(result, -2577, -2656);
    _putDontNest(result, -2582, -2641);
    _putDontNest(result, -2538, -2646);
    _putDontNest(result, -2510, -2666);
    _putDontNest(result, -2514, -2577);
    _putDontNest(result, -2517, -2592);
    _putDontNest(result, -2517, -2537);
    _putDontNest(result, -2592, -2607);
    _putDontNest(result, -2572, -2651);
    _putDontNest(result, -2334, -2607);
    _putDontNest(result, -2507, -2661);
    _putDontNest(result, -2498, -2577);
    _putDontNest(result, -2567, -2626);
    _putDontNest(result, -2573, -2636);
    _putDontNest(result, -2607, -2666);
    _putDontNest(result, -2568, -2631);
    _putDontNest(result, -2414, -2607);
    _putDontNest(result, -2319, -2636);
    _putDontNest(result, -2542, -2602);
    _putDontNest(result, -2553, -2651);
    _putDontNest(result, -2548, -2656);
    _putDontNest(result, -2437, -2631);
    _putDontNest(result, -2403, -2524);
    _putDontNest(result, -2319, -2552);
    _putDontNest(result, -2414, -2513);
    _putDontNest(result, -2602, -2661);
    _putDontNest(result, -2587, -2646);
    _putDontNest(result, -2583, -2626);
    _putDontNest(result, -2626, -2621);
    _putDontNest(result, -2403, -2656);
    _putDontNest(result, -2517, -2567);
    _putDontNest(result, -2558, -2602);
    _putDontNest(result, -2557, -2607);
    _putDontNest(result, -2437, -2537);
    _putDontNest(result, -60, -121);
    _putDontNest(result, -2387, -2524);
    _putDontNest(result, -2592, -2592);
    _putDontNest(result, -2646, -2641);
    _putDontNest(result, -2641, -2656);
    _putDontNest(result, -2652, -2651);
    _putDontNest(result, -2603, -2646);
    _putDontNest(result, -2387, -2656);
    _putDontNest(result, -2392, -2641);
    _putDontNest(result, -2547, -2621);
    _putDontNest(result, -2538, -2582);
    _putDontNest(result, -2510, -2602);
    _putDontNest(result, -2771, -2860);
    _putDontNest(result, -2636, -2651);
    _putDontNest(result, -2616, -2631);
    _putDontNest(result, -2621, -2636);
    _putDontNest(result, -2525, -2607);
    _putDontNest(result, -2507, -2597);
    _putDontNest(result, -2437, -2592);
    _putDontNest(result, -2319, -2587);
    _putDontNest(result, -2593, -2607);
    _putDontNest(result, -2853, -2870);
    _putDontNest(result, -605, -708);
    _putDontNest(result, -2437, -2562);
    _putDontNest(result, -2548, -2592);
    _putDontNest(result, -2553, -2587);
    _putDontNest(result, -2520, -2557);
    _putDontNest(result, -2513, -2552);
    _putDontNest(result, -2387, -2507);
    _putDontNest(result, -2603, -2641);
    _putDontNest(result, -2582, -2582);
    _putDontNest(result, -2597, -2651);
    _putDontNest(result, -2646, -2646);
    _putDontNest(result, -2616, -2616);
    _putDontNest(result, -2608, -2656);
    _putDontNest(result, -2392, -2626);
    _putDontNest(result, -2334, -2572);
    _putDontNest(result, -2517, -2626);
    _putDontNest(result, -2557, -2666);
    _putDontNest(result, -512, -509);
    _putDontNest(result, -2403, -2507);
    _putDontNest(result, -2587, -2641);
    _putDontNest(result, -2583, -2597);
    _putDontNest(result, -2568, -2616);
    _putDontNest(result, -2626, -2666);
    _putDontNest(result, -2592, -2656);
    _putDontNest(result, -2562, -2602);
    _putDontNest(result, -2647, -2661);
    _putDontNest(result, -2414, -2636);
    _putDontNest(result, -2319, -2607);
    _putDontNest(result, -2543, -2636);
    _putDontNest(result, -2552, -2557);
    _putDontNest(result, -2319, -2519);
    _putDontNest(result, -2578, -2602);
    _putDontNest(result, -2577, -2607);
    _putDontNest(result, -2607, -2621);
    _putDontNest(result, -2631, -2661);
    _putDontNest(result, -2567, -2597);
    _putDontNest(result, -2525, -2666);
    _putDontNest(result, -2553, -2646);
    _putDontNest(result, -2437, -2516);
    _putDontNest(result, -2790, -2870);
    _putDontNest(result, -2771, -2875);
    _putDontNest(result, -2552, -2641);
    _putDontNest(result, -2542, -2607);
    _putDontNest(result, -2437, -2626);
    _putDontNest(result, -2547, -2656);
    _putDontNest(result, -2437, -2500);
    _putDontNest(result, -2607, -2592);
    _putDontNest(result, -2768, -2880);
    _putDontNest(result, -2806, -2870);
    _putDontNest(result, -2582, -2646);
    _putDontNest(result, -2334, -2636);
    _putDontNest(result, -2392, -2562);
    _putDontNest(result, -2557, -2602);
    _putDontNest(result, -2558, -2607);
    _putDontNest(result, -2517, -2562);
    _putDontNest(result, -2538, -2651);
    _putDontNest(result, -2582, -2587);
    _putDontNest(result, -2602, -2626);
    _putDontNest(result, -2656, -2656);
    _putDontNest(result, -2598, -2646);
    _putDontNest(result, -2583, -2661);
    _putDontNest(result, -2562, -2666);
    _putDontNest(result, -2414, -2572);
    _putDontNest(result, -2507, -2616);
    _putDontNest(result, -2520, -2641);
    _putDontNest(result, -2510, -2607);
    _putDontNest(result, -2548, -2621);
    _putDontNest(result, -2543, -2572);
    _putDontNest(result, -2572, -2577);
    _putDontNest(result, -2617, -2631);
    _putDontNest(result, -2567, -2661);
    _putDontNest(result, -2578, -2666);
    _putDontNest(result, -2553, -2582);
    _putDontNest(result, -2525, -2602);
    _putDontNest(result, -2498, -2519);
    _putDontNest(result, -2563, -2572);
    _putDontNest(result, -2592, -2621);
    _putDontNest(result, -2657, -2666);
    _putDontNest(result, -2627, -2636);
    _putDontNest(result, -2593, -2602);
    _putDontNest(result, -2553, -2661);
    _putDontNest(result, -2510, -2636);
    _putDontNest(result, -2552, -2577);
    _putDontNest(result, -2547, -2592);
    _putDontNest(result, -2517, -2547);
    _putDontNest(result, -2514, -2552);
    _putDontNest(result, -2392, -2557);
    _putDontNest(result, -2607, -2656);
    _putDontNest(result, -2870, -2870);
    _putDontNest(result, -2598, -2651);
    _putDontNest(result, -2392, -2631);
    _putDontNest(result, -2520, -2626);
    _putDontNest(result, -2538, -2587);
    _putDontNest(result, -2498, -2552);
    _putDontNest(result, -2582, -2651);
    _putDontNest(result, -2880, -2880);
    _putDontNest(result, -2567, -2616);
    _putDontNest(result, -2562, -2607);
    _putDontNest(result, -2588, -2641);
    _putDontNest(result, -2319, -2602);
    _putDontNest(result, -509, -708);
    _putDontNest(result, -2542, -2636);
    _putDontNest(result, -2520, -2577);
    _putDontNest(result, -409, -423);
    _putDontNest(result, -2507, -2537);
    _putDontNest(result, -2414, -2507);
    _putDontNest(result, -2641, -2666);
    _putDontNest(result, -2608, -2621);
    _putDontNest(result, -2568, -2597);
    _putDontNest(result, -2577, -2602);
    _putDontNest(result, -2583, -2616);
    _putDontNest(result, -2578, -2607);
    _putDontNest(result, -2572, -2641);
    _putDontNest(result, -2437, -2641);
    _putDontNest(result, -2558, -2636);
    _putDontNest(result, -2552, -2626);
    _putDontNest(result, -2520, -2542);
    _putDontNest(result, -2785, -2860);
    _putDontNest(result, -2563, -2636);
    _putDontNest(result, -2593, -2666);
    _putDontNest(result, -2616, -2661);
    _putDontNest(result, -2513, -2621);
    _putDontNest(result, -2543, -2607);
    _putDontNest(result, -2553, -2597);
    _putDontNest(result, -2538, -2616);
    _putDontNest(result, -2510, -2572);
    _putDontNest(result, -517, -707);
    _putDontNest(result, -2392, -2567);
    _putDontNest(result, -2520, -2562);
    _putDontNest(result, -2437, -2547);
    _putDontNest(result, -2334, -2507);
    _putDontNest(result, -2583, -2587);
    _putDontNest(result, -2597, -2646);
    _putDontNest(result, -2602, -2631);
    _putDontNest(result, -2646, -2651);
    _putDontNest(result, -2319, -2666);
    _putDontNest(result, -2517, -2641);
    _putDontNest(result, -2514, -2656);
    _putDontNest(result, -2542, -2572);
    _putDontNest(result, -2513, -2519);
    _putDontNest(result, -2567, -2587);
    _putDontNest(result, -2636, -2641);
    _putDontNest(result, -2577, -2666);
    _putDontNest(result, -2568, -2661);
    _putDontNest(result, -2617, -2626);
    _putDontNest(result, -2507, -2651);
    _putDontNest(result, -2498, -2656);
    _putDontNest(result, -2558, -2572);
    _putDontNest(result, -2552, -2562);
    _putDontNest(result, -2510, -2524);
    _putDontNest(result, -2568, -2582);
    _putDontNest(result, -2617, -2641);
    _putDontNest(result, -2622, -2656);
    _putDontNest(result, -2626, -2636);
    _putDontNest(result, -2562, -2572);
    _putDontNest(result, -2598, -2616);
    _putDontNest(result, -2507, -2646);
    _putDontNest(result, -2543, -2666);
    _putDontNest(result, -2520, -2547);
    _putDontNest(result, -2661, -2661);
    _putDontNest(result, -2597, -2597);
    _putDontNest(result, -2573, -2621);
    _putDontNest(result, -2319, -2621);
    _putDontNest(result, -2525, -2636);
    _putDontNest(result, -2538, -2661);
    _putDontNest(result, -2520, -2631);
    _putDontNest(result, -2403, -2592);
    _putDontNest(result, -2392, -2510);
    _putDontNest(result, -2621, -2621);
    _putDontNest(result, -2563, -2607);
    _putDontNest(result, -2583, -2651);
    _putDontNest(result, -2514, -2592);
    _putDontNest(result, -2517, -2577);
    _putDontNest(result, -2387, -2592);
    _putDontNest(result, -2392, -2577);
    _putDontNest(result, -409, -418);
    _putDontNest(result, -2547, -2552);
    _putDontNest(result, -2552, -2547);
    _putDontNest(result, -2334, -2524);
    _putDontNest(result, -2861, -2880);
    _putDontNest(result, -2567, -2651);
    _putDontNest(result, -2582, -2616);
    _putDontNest(result, -2557, -2636);
    _putDontNest(result, -2552, -2631);
    _putDontNest(result, -2507, -2587);
    _putDontNest(result, -2498, -2592);
    _putDontNest(result, -2517, -2542);
    _putDontNest(result, -2392, -2542);
    _putDontNest(result, -2572, -2626);
    _putDontNest(result, -2562, -2636);
    _putDontNest(result, -2568, -2646);
    _putDontNest(result, -2507, -2582);
    _putDontNest(result, -2543, -2602);
    _putDontNest(result, -2514, -2621);
    _putDontNest(result, -2437, -2510);
    _putDontNest(result, -2578, -2636);
    _putDontNest(result, -2587, -2631);
    _putDontNest(result, -2597, -2661);
    _putDontNest(result, -2588, -2626);
    _putDontNest(result, -2525, -2572);
    _putDontNest(result, -2538, -2597);
    _putDontNest(result, -2553, -2616);
    _putDontNest(result, -2520, -2567);
    _putDontNest(result, -2498, -2621);
    _putDontNest(result, -2403, -2513);
    _putDontNest(result, -2414, -2524);
    _putDontNest(result, -2647, -2651);
    _putDontNest(result, -2603, -2631);
    _putDontNest(result, -2513, -2656);
    _putDontNest(result, -2437, -2542);
    _putDontNest(result, -2387, -2513);
    _putDontNest(result, -2785, -2875);
    _putDontNest(result, -2568, -2587);
    _putDontNest(result, -2573, -2592);
    _putDontNest(result, -2616, -2646);
    _putDontNest(result, -2631, -2651);
    _putDontNest(result, -2392, -2616);
    _putDontNest(result, -2557, -2572);
    _putDontNest(result, -2552, -2567);
    _putDontNest(result, -2437, -2577);
    _putDontNest(result, -2498, -2513);
    _putDontNest(result, -2843, -2865);
    _putDontNest(result, -2616, -2651);
    _putDontNest(result, -2621, -2656);
    _putDontNest(result, -2631, -2646);
    _putDontNest(result, -2567, -2582);
    _putDontNest(result, -2597, -2616);
    _putDontNest(result, -2517, -2557);
    _putDontNest(result, -2387, -2552);
    _putDontNest(result, -2392, -2547);
    _putDontNest(result, -2577, -2572);
    _putDontNest(result, -2583, -2582);
    _putDontNest(result, -2641, -2636);
    _putDontNest(result, -2598, -2597);
    _putDontNest(result, -2602, -2641);
    _putDontNest(result, -2387, -2636);
    _putDontNest(result, -413, -413);
    _putDontNest(result, -509, -509);
    _putDontNest(result, -2563, -2602);
    _putDontNest(result, -2627, -2666);
    _putDontNest(result, -2646, -2661);
    _putDontNest(result, -2622, -2621);
    _putDontNest(result, -2865, -2875);
    _putDontNest(result, -2582, -2597);
    _putDontNest(result, -2392, -2661);
    _putDontNest(result, -2403, -2636);
    _putDontNest(result, -2498, -2666);
    _putDontNest(result, -2538, -2626);
    _putDontNest(result, -2513, -2592);
    _putDontNest(result, -159, -164);
    _putDontNest(result, -2568, -2651);
    _putDontNest(result, -2573, -2656);
    _putDontNest(result, -2319, -2656);
    _putDontNest(result, -2514, -2666);
    _putDontNest(result, -2437, -2651);
    _putDontNest(result, -2553, -2631);
    _putDontNest(result, -2548, -2636);
    _putDontNest(result, -2403, -2552);
    _putDontNest(result, -2319, -2524);
    _putDontNest(result, -2785, -2870);
    _putDontNest(result, -2567, -2646);
    _putDontNest(result, -2572, -2631);
    _putDontNest(result, -2583, -2646);
    _putDontNest(result, -2598, -2661);
    _putDontNest(result, -2577, -2636);
    _putDontNest(result, -2587, -2626);
    _putDontNest(result, -2588, -2631);
    _putDontNest(result, -2387, -2572);
    _putDontNest(result, -2552, -2616);
    _putDontNest(result, -2437, -2557);
    _putDontNest(result, -2577, -2587);
    _putDontNest(result, -2588, -2592);
    _putDontNest(result, -2603, -2626);
    _putDontNest(result, -2582, -2661);
    _putDontNest(result, -2593, -2636);
    _putDontNest(result, -2563, -2666);
    _putDontNest(result, -2392, -2597);
    _putDontNest(result, -2403, -2572);
    _putDontNest(result, -2538, -2562);
    _putDontNest(result, -512, -520);
    _putDontNest(result, -2498, -2602);
    _putDontNest(result, -2517, -2651);
    _putDontNest(result, -2572, -2592);
    _putDontNest(result, -2514, -2602);
    _putDontNest(result, -2507, -2641);
    _putDontNest(result, -2553, -2567);
    _putDontNest(result, -2520, -2616);
    _putDontNest(result, -2543, -2621);
    _putDontNest(result, -2513, -2607);
    _putDontNest(result, -2548, -2572);
    _putDontNest(result, -2843, -2880);
    _putDontNest(result, -2626, -2646);
    _putDontNest(result, -2562, -2582);
    _putDontNest(result, -2597, -2607);
    _putDontNest(result, -2598, -2602);
    _putDontNest(result, -2587, -2621);
    _putDontNest(result, -600, -707);
    _putDontNest(result, -2437, -2582);
    _putDontNest(result, -2552, -2661);
    _putDontNest(result, -2514, -2547);
    _putDontNest(result, -2517, -2552);
    _putDontNest(result, -2865, -2870);
    _putDontNest(result, -2578, -2582);
    _putDontNest(result, -2593, -2651);
    _putDontNest(result, -2607, -2641);
    _putDontNest(result, -2392, -2646);
    _putDontNest(result, -2517, -2646);
    _putDontNest(result, -2498, -2547);
    _putDontNest(result, -2392, -2500);
    _putDontNest(result, -2875, -2880);
    _putDontNest(result, -2577, -2651);
    _putDontNest(result, -2588, -2656);
    _putDontNest(result, -2334, -2656);
    _putDontNest(result, -2538, -2631);
    _putDontNest(result, -2520, -2661);
    _putDontNest(result, -2517, -2587);
    _putDontNest(result, -2548, -2557);
    _putDontNest(result, -2572, -2656);
    _putDontNest(result, -2603, -2621);
    _putDontNest(result, -2646, -2666);
    _putDontNest(result, -2563, -2597);
    _putDontNest(result, -2627, -2661);
    _putDontNest(result, -2582, -2602);
    _putDontNest(result, -2547, -2636);
    _putDontNest(result, -2553, -2626);
    _putDontNest(result, -2513, -2666);
    _putDontNest(result, -2507, -2577);
    _putDontNest(result, -2573, -2631);
    _putDontNest(result, -2598, -2666);
    _putDontNest(result, -2568, -2636);
    _putDontNest(result, -2562, -2646);
    _putDontNest(result, -2403, -2607);
    _putDontNest(result, -2437, -2646);
    _putDontNest(result, -2552, -2597);
    _putDontNest(result, -2319, -2507);
    _putDontNest(result, -2603, -2592);
    _putDontNest(result, -2578, -2646);
    _putDontNest(result, -520, -707);
    _putDontNest(result, -2392, -2582);
    _putDontNest(result, -2414, -2656);
    _putDontNest(result, -2543, -2656);
    _putDontNest(result, -2517, -2582);
    _putDontNest(result, -2437, -2552);
    _putDontNest(result, -2403, -2519);
    _putDontNest(result, -2587, -2592);
    _putDontNest(result, -2578, -2587);
    _putDontNest(result, -2652, -2656);
    _putDontNest(result, -2641, -2651);
    _putDontNest(result, -2538, -2567);
    _putDontNest(result, -2520, -2597);
    _putDontNest(result, -512, -517);
    _putDontNest(result, -2498, -2607);
    _putDontNest(result, -2513, -2516);
    _putDontNest(result, -2392, -2516);
    _putDontNest(result, -2387, -2519);
    _putDontNest(result, -2562, -2587);
    _putDontNest(result, -2636, -2656);
    _putDontNest(result, -2621, -2631);
    _putDontNest(result, -2616, -2636);
    _putDontNest(result, -2622, -2626);
    _putDontNest(result, -2582, -2666);
    _putDontNest(result, -2563, -2661);
    _putDontNest(result, -2387, -2607);
    _putDontNest(result, -2513, -2602);
    _putDontNest(result, -2547, -2572);
    _putDontNest(result, -2514, -2607);
    _putDontNest(result, -2437, -2587);
    _putDontNest(result, -2597, -2602);
    _putDontNest(result, -2856, -2860);
    _putDontNest(result, -2631, -2636);
    _putDontNest(result, -2588, -2621);
    _putDontNest(result, -2567, -2572);
    _putDontNest(result, -2661, -2666);
    _putDontNest(result, -2598, -2607);
    _putDontNest(result, -600, -708);
    _putDontNest(result, -2403, -2666);
    _putDontNest(result, -2334, -2621);
    _putDontNest(result, -2507, -2631);
    _putDontNest(result, -2498, -2636);
    _putDontNest(result, -2513, -2547);
    _putDontNest(result, -2403, -2542);
    _putDontNest(result, -2572, -2621);
    _putDontNest(result, -2641, -2646);
    _putDontNest(result, -2577, -2582);
    _putDontNest(result, -2608, -2641);
    _putDontNest(result, -2603, -2656);
    _putDontNest(result, -2514, -2636);
    _putDontNest(result, -2520, -2646);
    _putDontNest(result, -2548, -2666);
    _putDontNest(result, -2543, -2592);
    _putDontNest(result, -2387, -2510);
    _putDontNest(result, -2587, -2656);
    _putDontNest(result, -2578, -2651);
    _putDontNest(result, -2563, -2616);
    _putDontNest(result, -2592, -2641);
    _putDontNest(result, -504, -707);
    _putDontNest(result, -2517, -2661);
    _putDontNest(result, -413, -423);
    _putDontNest(result, -2547, -2557);
    _putDontNest(result, -2403, -2510);
    _putDontNest(result, -2861, -2865);
    _putDontNest(result, -2860, -2880);
    _putDontNest(result, -2582, -2607);
    _putDontNest(result, -2562, -2651);
    _putDontNest(result, -2387, -2666);
    _putDontNest(result, -2437, -2597);
    _putDontNest(result, -2552, -2646);
    _putDontNest(result, -2437, -2519);
    _putDontNest(result, -2387, -2542);
    _putDontNest(result, -2567, -2636);
    _putDontNest(result, -2573, -2626);
    _putDontNest(result, -2597, -2666);
    _putDontNest(result, -2403, -2602);
    _putDontNest(result, -2525, -2621);
    _putDontNest(result, -2507, -2567);
    _putDontNest(result, -2498, -2572);
    _putDontNest(result, -2553, -2641);
    _putDontNest(result, -2558, -2656);
    _putDontNest(result, -2768, -2865);
    _putDontNest(result, -2602, -2592);
    _putDontNest(result, -2583, -2636);
    _putDontNest(result, -2577, -2646);
    _putDontNest(result, -520, -708);
    _putDontNest(result, -2514, -2572);
    _putDontNest(result, -2520, -2582);
    _putDontNest(result, -2542, -2656);
    _putDontNest(result, -2548, -2602);
    _putDontNest(result, -2547, -2607);
    _putDontNest(result, -2806, -2875);
    _putDontNest(result, -2651, -2656);
    _putDontNest(result, -2593, -2646);
    _putDontNest(result, -2392, -2651);
    _putDontNest(result, -2557, -2621);
    _putDontNest(result, -509, -517);
    _putDontNest(result, -2517, -2597);
    _putDontNest(result, -2392, -2513);
    _putDontNest(result, -2771, -2870);
    _putDontNest(result, -2563, -2587);
    _putDontNest(result, -2790, -2875);
    _putDontNest(result, -2622, -2631);
    _putDontNest(result, -2626, -2651);
    _putDontNest(result, -2621, -2626);
    _putDontNest(result, -2414, -2621);
    _putDontNest(result, -2387, -2602);
    _putDontNest(result, -2552, -2582);
    _putDontNest(result, -2437, -2661);
    _putDontNest(result, -2510, -2656);
    _putDontNest(result, -2498, -2516);
    _putDontNest(result, -2853, -2860);
    _putDontNest(result, -2621, -2641);
    _putDontNest(result, -2507, -2626);
    _putDontNest(result, -2553, -2577);
    _putDontNest(result, -2558, -2592);
    _putDontNest(result, -2514, -2557);
    _putDontNest(result, -2387, -2557);
    _putDontNest(result, -2602, -2656);
    _putDontNest(result, -2593, -2597);
    _putDontNest(result, -2646, -2636);
    _putDontNest(result, -2392, -2636);
    _putDontNest(result, -2334, -2562);
    _putDontNest(result, -2403, -2661);
    _putDontNest(result, -2547, -2666);
    _putDontNest(result, -2513, -2636);
    _putDontNest(result, -2542, -2592);
    _putDontNest(result, -2498, -2557);
    _putDontNest(result, -2568, -2602);
    _putDontNest(result, -2617, -2621);
    _putDontNest(result, -2577, -2597);
    _putDontNest(result, -2567, -2607);
    _putDontNest(result, -2562, -2616);
    _putDontNest(result, -2641, -2661);
    _putDontNest(result, -2870, -2875);
    _putDontNest(result, -2387, -2661);
    _putDontNest(result, -2414, -2626);
    _putDontNest(result, -504, -708);
    _putDontNest(result, -2437, -2616);
    _putDontNest(result, -2510, -2537);
    _putDontNest(result, -413, -418);
    _putDontNest(result, -2578, -2616);
    _putDontNest(result, -2573, -2641);
    _putDontNest(result, -2583, -2607);
    _putDontNest(result, -2563, -2651);
    _putDontNest(result, -2319, -2641);
    _putDontNest(result, -2510, -2592);
    _putDontNest(result, -2513, -2542);
    _putDontNest(result, -2403, -2557);
    _putDontNest(result, -2790, -2860);
    _putDontNest(result, -2507, -2562);
    _putDontNest(result, -2557, -2656);
    _putDontNest(result, -2552, -2651);
    _putDontNest(result, -2806, -2860);
    _putDontNest(result, -2593, -2661);
    _putDontNest(result, -2592, -2626);
    _putDontNest(result, -2616, -2666);
    _putDontNest(result, -2582, -2636);
    _putDontNest(result, -2392, -2572);
    _putDontNest(result, -2334, -2626);
    _putDontNest(result, -2403, -2597);
    _putDontNest(result, -2547, -2602);
    _putDontNest(result, -2538, -2641);
    _putDontNest(result, -2510, -2621);
    _putDontNest(result, -2513, -2572);
    _putDontNest(result, -2548, -2607);
    _putDontNest(result, -60, -116);
    _putDontNest(result, -2577, -2661);
    _putDontNest(result, -2568, -2666);
    _putDontNest(result, -2607, -2631);
    _putDontNest(result, -2608, -2626);
    _putDontNest(result, -2598, -2636);
    _putDontNest(result, -2414, -2562);
    _putDontNest(result, -2387, -2597);
    _putDontNest(result, -2525, -2656);
    _putDontNest(result, -2520, -2651);
    _putDontNest(result, -509, -520);
    _putDontNest(result, -2558, -2621);
    _putDontNest(result, -2319, -2537);
    _putDontNest(result, -2627, -2651);
    _putDontNest(result, -2542, -2621);
    _putDontNest(result, -2517, -2616);
    _putDontNest(result, -2319, -2592);
    _putDontNest(result, -2507, -2524);
    _putDontNest(result, -2627, -2646);
    _putDontNest(result, -2563, -2582);
    _putDontNest(result, -2622, -2641);
    _putDontNest(result, -2593, -2616);
    _putDontNest(result, -2617, -2656);
    _putDontNest(result, -2510, -2626);
    _putDontNest(result, -2498, -2646);
    _putDontNest(result, -2547, -2661);
    _putDontNest(result, -2557, -2592);
    _putDontNest(result, -2552, -2587);
    _putDontNest(result, -2498, -2510);
    _putDontNest(result, -2520, -2552);
    _putDontNest(result, -2513, -2557);
    _putDontNest(result, -2414, -2537);
    _putDontNest(result, -2334, -2567);
    _putDontNest(result, -2520, -2636);
    _putDontNest(result, -2525, -2631);
    _putDontNest(result, -2514, -2646);
    _putDontNest(result, -2538, -2577);
    _putDontNest(result, -2414, -2592);
    _putDontNest(result, -2387, -2500);
    _putDontNest(result, -2567, -2602);
    _putDontNest(result, -2631, -2666);
    _putDontNest(result, -2568, -2607);
    _putDontNest(result, -2578, -2597);
    _putDontNest(result, -2414, -2631);
    _putDontNest(result, -2542, -2626);
    _putDontNest(result, -2437, -2607);
    _putDontNest(result, -2525, -2592);
    _putDontNest(result, -2520, -2587);
    _putDontNest(result, -2552, -2552);
    _putDontNest(result, -155, -164);
    _putDontNest(result, -2547, -2547);
    _putDontNest(result, -2498, -2542);
    _putDontNest(result, -2403, -2500);
    _putDontNest(result, -2853, -2875);
    _putDontNest(result, -2626, -2661);
    _putDontNest(result, -2583, -2602);
    _putDontNest(result, -2602, -2621);
    _putDontNest(result, -2647, -2666);
    _putDontNest(result, -2562, -2597);
    _putDontNest(result, -2577, -2616);
    _putDontNest(result, -2557, -2631);
    _putDontNest(result, -2558, -2626);
    _putDontNest(result, -2552, -2636);
    _putDontNest(result, -2514, -2542);
    _putDontNest(result, -2437, -2513);
    _putDontNest(result, -2563, -2646);
    _putDontNest(result, -2403, -2616);
    _putDontNest(result, -2547, -2597);
    _putDontNest(result, -2498, -2582);
    _putDontNest(result, -2510, -2562);
    _putDontNest(result, -2592, -2631);
    _putDontNest(result, -2334, -2631);
    _putDontNest(result, -2520, -2572);
    _putDontNest(result, -2514, -2582);
    _putDontNest(result, -2525, -2567);
    _putDontNest(result, -2543, -2641);
    _putDontNest(result, -2548, -2616);
    _putDontNest(result, -2507, -2621);
    _putDontNest(result, -2403, -2516);
    _putDontNest(result, -517, -512);
    _putDontNest(result, -2597, -2636);
    _putDontNest(result, -2567, -2666);
    _putDontNest(result, -2607, -2626);
    _putDontNest(result, -2608, -2631);
    _putDontNest(result, -2578, -2661);
    _putDontNest(result, -2414, -2567);
    _putDontNest(result, -2513, -2651);
    _putDontNest(result, -2542, -2562);
    _putDontNest(result, -2334, -2592);
    _putDontNest(result, -2392, -2519);
    _putDontNest(result, -2387, -2516);
    _putDontNest(result, -2562, -2661);
    _putDontNest(result, -2583, -2666);
    _putDontNest(result, -2387, -2616);
    _putDontNest(result, -2552, -2572);
    _putDontNest(result, -2557, -2567);
    _putDontNest(result, -2517, -2607);
    _putDontNest(result, -2334, -2537);
    _putDontNest(result, -2563, -2577);
    _putDontNest(result, -2568, -2592);
    _putDontNest(result, -2573, -2587);
    _putDontNest(result, -2319, -2567);
    _putDontNest(result, -2510, -2631);
    _putDontNest(result, -2548, -2661);
    _putDontNest(result, -2387, -2547);
    _putDontNest(result, -2392, -2552);
    _putDontNest(result, -2666, -2666);
    _putDontNest(result, -2572, -2572);
    _putDontNest(result, -2562, -2562);
    _putDontNest(result, -2626, -2626);
    _putDontNest(result, -2602, -2602);
    _putDontNest(result, -2583, -2621);
    _putDontNest(result, -2621, -2651);
    _putDontNest(result, -2636, -2636);
    _putDontNest(result, -2616, -2656);
    _putDontNest(result, -2525, -2626);
    _putDontNest(result, -2513, -2646);
    _putDontNest(result, -2543, -2577);
    _putDontNest(result, -2510, -2547);
    _putDontNest(result, -408, -413);
    _putDontNest(result, -2567, -2621);
    _putDontNest(result, -2607, -2597);
    _putDontNest(result, -520, -520);
    _putDontNest(result, -2542, -2631);
    _putDontNest(result, -2437, -2602);
    _putDontNest(result, -2513, -2587);
    _putDontNest(result, -2392, -2587);
    _putDontNest(result, -403, -418);
    _putDontNest(result, -2871, -2880);
    _putDontNest(result, -2392, -2666);
    _putDontNest(result, -2557, -2626);
    _putDontNest(result, -2558, -2631);
    _putDontNest(result, -2517, -2666);
    _putDontNest(result, -2542, -2547);
    _putDontNest(result, -2437, -2524);
    _putDontNest(result, -2403, -2547);
    _putDontNest(result, -2563, -2641);
    _putDontNest(result, -2592, -2616);
    _putDontNest(result, -2568, -2656);
    _putDontNest(result, -2843, -2860);
    _putDontNest(result, -2860, -2875);
    _putDontNest(result, -2573, -2651);
    _putDontNest(result, -2319, -2631);
    _putDontNest(result, -2510, -2567);
    _putDontNest(result, -2548, -2597);
    _putDontNest(result, -2602, -2666);
    _putDontNest(result, -2572, -2636);
    _putDontNest(result, -2562, -2626);
    _putDontNest(result, -517, -708);
    _putDontNest(result, -2403, -2651);
    _putDontNest(result, -2513, -2582);
    _putDontNest(result, -2525, -2562);
    _putDontNest(result, -2547, -2616);
    _putDontNest(result, -2577, -2631);
    _putDontNest(result, -2578, -2626);
    _putDontNest(result, -2607, -2661);
    _putDontNest(result, -2588, -2636);
    _putDontNest(result, -2387, -2651);
    _putDontNest(result, -2437, -2666);
    _putDontNest(result, -2542, -2567);
    _putDontNest(result, -2514, -2651);
    _putDontNest(result, -2517, -2524);
    _putDontNest(result, -2583, -2592);
    _putDontNest(result, -2593, -2631);
    _putDontNest(result, -2392, -2602);
    _putDontNest(result, -2507, -2656);
    _putDontNest(result, -2558, -2567);
    _putDontNest(result, -2517, -2602);
    _putDontNest(result, -2498, -2651);
    _putDontNest(result, -2557, -2562);
    _putDontNest(result, -2567, -2592);
    _putDontNest(result, -2785, -2865);
    _putDontNest(result, -2622, -2646);
    _putDontNest(result, -2627, -2641);
    _putDontNest(result, -2319, -2562);
    _putDontNest(result, -2525, -2547);
    _putDontNest(result, -2573, -2582);
    _putDontNest(result, -2607, -2616);
    _putDontNest(result, -2626, -2631);
    _putDontNest(result, -2602, -2607);
    _putDontNest(result, -2843, -2875);
    _putDontNest(result, -2562, -2567);
    _putDontNest(result, -2622, -2651);
    _putDontNest(result, -605, -707);
    _putDontNest(result, -2387, -2646);
    _putDontNest(result, -2552, -2666);
    _putDontNest(result, -520, -517);
    _putDontNest(result, -2568, -2621);
    _putDontNest(result, -2334, -2641);
    _putDontNest(result, -2403, -2646);
    _putDontNest(result, -2513, -2661);
    _putDontNest(result, -2543, -2631);
    _putDontNest(result, -2514, -2587);
    _putDontNest(result, -404, -418);
    _putDontNest(result, -2592, -2597);
    _putDontNest(result, -2616, -2621);
    _putDontNest(result, -2875, -2875);
    _putDontNest(result, -2656, -2661);
    _putDontNest(result, -2583, -2656);
    _putDontNest(result, -2520, -2666);
    _putDontNest(result, -2548, -2646);
    _putDontNest(result, -2498, -2587);
    _putDontNest(result, -2507, -2592);
    _putDontNest(result, -2538, -2552);
    _putDontNest(result, -2543, -2557);
    _putDontNest(result, -2414, -2547);
    _putDontNest(result, -2567, -2656);
    _putDontNest(result, -2856, -2880);
    _putDontNest(result, -2319, -2626);
    _putDontNest(result, -2557, -2641);
    _putDontNest(result, -2547, -2651);
    _putDontNest(result, -2437, -2507);
    _putDontNest(result, -2334, -2547);
    _putDontNest(result, -2319, -2510);
    _putDontNest(result, -2573, -2646);
    _putDontNest(result, -2562, -2631);
    _putDontNest(result, -2414, -2641);
    _putDontNest(result, -2387, -2582);
    _putDontNest(result, -2538, -2656);
    _putDontNest(result, -2552, -2602);
    _putDontNest(result, -2562, -2557);
    _putDontNest(result, -2598, -2592);
    _putDontNest(result, -2608, -2661);
    _putDontNest(result, -2587, -2636);
    _putDontNest(result, -2578, -2631);
    _putDontNest(result, -2577, -2626);
    _putDontNest(result, -2617, -2666);
    _putDontNest(result, -2403, -2582);
    _putDontNest(result, -2513, -2597);
    _putDontNest(result, -2525, -2641);
    _putDontNest(result, -2543, -2567);
    _putDontNest(result, -2553, -2621);
    _putDontNest(result, -2498, -2616);
    _putDontNest(result, -2319, -2542);
    _putDontNest(result, -2582, -2592);
    _putDontNest(result, -2577, -2577);
    _putDontNest(result, -2647, -2656);
    _putDontNest(result, -2603, -2636);
    _putDontNest(result, -2593, -2626);
    _putDontNest(result, -2592, -2661);
    _putDontNest(result, -2392, -2607);
    _putDontNest(result, -2520, -2602);
    _putDontNest(result, -2548, -2582);
    _putDontNest(result, -2514, -2616);
    _putDontNest(result, -2319, -2577);
    _putDontNest(result, -2510, -2516);
    _putDontNest(result, -2621, -2646);
    _putDontNest(result, -2631, -2656);
    _putDontNest(result, -2437, -2572);
    _putDontNest(result, -2557, -2577);
    _putDontNest(result, -2547, -2587);
    _putDontNest(result, -2403, -2537);
    _putDontNest(result, -2627, -2631);
    _putDontNest(result, -2603, -2607);
    _putDontNest(result, -2517, -2636);
    _putDontNest(result, -2538, -2592);
    _putDontNest(result, -2507, -2552);
    _putDontNest(result, -2510, -2557);
    _putDontNest(result, -403, -408);
    _putDontNest(result, -2607, -2651);
    _putDontNest(result, -517, -517);
    _putDontNest(result, -2598, -2656);
    _putDontNest(result, -2593, -2641);
    _putDontNest(result, -2334, -2582);
    _putDontNest(result, -2514, -2661);
    _putDontNest(result, -2543, -2626);
    _putDontNest(result, -2525, -2577);
    _putDontNest(result, -404, -423);
    _putDontNest(result, -2498, -2537);
    _putDontNest(result, -2334, -2516);
    _putDontNest(result, -2414, -2500);
    _putDontNest(result, -2582, -2656);
    _putDontNest(result, -2572, -2602);
    _putDontNest(result, -2636, -2666);
    _putDontNest(result, -2577, -2641);
    _putDontNest(result, -2414, -2646);
    _putDontNest(result, -509, -707);
    _putDontNest(result, -2547, -2646);
    _putDontNest(result, -2498, -2661);
    _putDontNest(result, -2525, -2542);
    _putDontNest(result, -2514, -2537);
    _putDontNest(result, -2542, -2557);
    _putDontNest(result, -2387, -2537);
    _putDontNest(result, -2652, -2666);
    _putDontNest(result, -2573, -2597);
    _putDontNest(result, -2597, -2621);
    _putDontNest(result, -2853, -2880);
    _putDontNest(result, -2587, -2607);
    _putDontNest(result, -2588, -2602);
    _putDontNest(result, -2319, -2597);
    _putDontNest(result, -2553, -2656);
    _putDontNest(result, -2548, -2651);
    _putDontNest(result, -2437, -2636);
    _putDontNest(result, -2558, -2641);
    _putDontNest(result, -2771, -2865);
    _putDontNest(result, -2621, -2661);
    _putDontNest(result, -2563, -2631);
    _putDontNest(result, -2517, -2572);
    _putDontNest(result, -2542, -2641);
    _putDontNest(result, -2552, -2607);
    _putDontNest(result, -2319, -2557);
    _putDontNest(result, -2414, -2516);
    _putDontNest(result, -2334, -2500);
    _putDontNest(result, -520, -512);
    _putDontNest(result, -2597, -2592);
    _putDontNest(result, -2592, -2646);
    _putDontNest(result, -2334, -2646);
    _putDontNest(result, -2403, -2641);
    _putDontNest(result, -2514, -2597);
    _putDontNest(result, -2543, -2562);
    _putDontNest(result, -2768, -2870);
    _putDontNest(result, -2806, -2880);
    _putDontNest(result, -2572, -2666);
    _putDontNest(result, -2602, -2636);
    _putDontNest(result, -2646, -2656);
    _putDontNest(result, -2641, -2641);
    _putDontNest(result, -2608, -2646);
    _putDontNest(result, -2392, -2656);
    _putDontNest(result, -2414, -2582);
    _putDontNest(result, -2387, -2641);
    _putDontNest(result, -2513, -2616);
    _putDontNest(result, -2510, -2641);
    _putDontNest(result, -2547, -2582);
    _putDontNest(result, -2498, -2597);
    _putDontNest(result, -2520, -2607);
    _putDontNest(result, -2538, -2621);
    _putDontNest(result, -2790, -2880);
    _putDontNest(result, -2562, -2577);
    _putDontNest(result, -2588, -2666);
    _putDontNest(result, -2573, -2661);
    _putDontNest(result, -2319, -2661);
    _putDontNest(result, -2548, -2587);
    _putDontNest(result, -2553, -2592);
    _putDontNest(result, -2558, -2577);
    _putDontNest(result, -2525, -2557);
    _putDontNest(result, -2542, -2542);
    _putDontNest(result, -2603, -2602);
    _putDontNest(result, -2582, -2621);
    _putDontNest(result, -2510, -2646);
    _putDontNest(result, -2538, -2666);
    _putDontNest(result, -2498, -2626);
    _putDontNest(result, -2542, -2577);
    _putDontNest(result, -2403, -2587);
    _putDontNest(result, -418, -418);
    _putDontNest(result, -2392, -2507);
    _putDontNest(result, -2608, -2651);
    _putDontNest(result, -2597, -2656);
    _putDontNest(result, -517, -520);
    _putDontNest(result, -2514, -2626);
    _putDontNest(result, -2543, -2661);
    _putDontNest(result, -2513, -2631);
    _putDontNest(result, -2387, -2587);
    _putDontNest(result, -403, -423);
    _putDontNest(result, -2557, -2557);
    _putDontNest(result, -2510, -2542);
    _putDontNest(result, -2578, -2641);
    _putDontNest(result, -2573, -2616);
    _putDontNest(result, -2870, -2880);
    _putDontNest(result, -2572, -2607);
    _putDontNest(result, -2592, -2651);
    _putDontNest(result, -2319, -2616);
    _putDontNest(result, -2334, -2651);
    _putDontNest(result, -2542, -2646);
    _putDontNest(result, -2510, -2577);
    _putDontNest(result, -2513, -2537);
    _putDontNest(result, -2414, -2557);
    _putDontNest(result, -2319, -2516);
    _putDontNest(result, -2562, -2641);
    _putDontNest(result, -2588, -2607);
    _putDontNest(result, -2598, -2621);
    _putDontNest(result, -2587, -2602);
    _putDontNest(result, -2651, -2666);
    _putDontNest(result, -623, -707);
    _putDontNest(result, -2558, -2646);
    _putDontNest(result, -2334, -2557);
    _putDontNest(result, -2319, -2500);
    _putDontNest(result, -2771, -2880);
    _putDontNest(result, -2622, -2661);
    _putDontNest(result, -2563, -2626);
    _putDontNest(result, -2603, -2666);
    _putDontNest(result, -2392, -2621);
    _putDontNest(result, -2498, -2562);
    _putDontNest(result, -2547, -2641);
    _putDontNest(result, -2538, -2602);
    _putDontNest(result, -2510, -2582);
    _putDontNest(result, -2557, -2651);
    _putDontNest(result, -2552, -2656);
    _putDontNest(result, -60, -109);
    _putDontNest(result, -2768, -2875);
    _putDontNest(result, -2414, -2651);
    _putDontNest(result, -2553, -2607);
    _putDontNest(result, -2514, -2562);
    _putDontNest(result, -2543, -2597);
    _putDontNest(result, -2513, -2567);
    _putDontNest(result, -520, -509);
    _putDontNest(result, -2806, -2865);
    _putDontNest(result, -704, -707);
    _putDontNest(result, -2656, -2651);
    _putDontNest(result, -2607, -2646);
    _putDontNest(result, -2520, -2656);
    _putDontNest(result, -2542, -2582);
    _putDontNest(result, -2525, -2651);
    _putDontNest(result, -2507, -2519);
    _putDontNest(result, -2790, -2865);
    _putDontNest(result, -2567, -2577);
    _putDontNest(result, -2617, -2636);
    _putDontNest(result, -2626, -2641);
    _putDontNest(result, -2587, -2666);
    _putDontNest(result, -2558, -2582);
    _putDontNest(result, -2525, -2552);
    _putDontNest(result, -2498, -2507);
    _putDontNest(result, -2414, -2542);
    _putDontNest(result, -2617, -2651);
    _putDontNest(result, -2498, -2631);
    _putDontNest(result, -2507, -2636);
    _putDontNest(result, -2547, -2577);
    _putDontNest(result, -2552, -2592);
    _putDontNest(result, -2557, -2587);
    _putDontNest(result, -2414, -2577);
    _putDontNest(result, -404, -413);
    _putDontNest(result, -2563, -2621);
    _putDontNest(result, -2603, -2597);
    _putDontNest(result, -2387, -2631);
    _putDontNest(result, -2513, -2626);
    _putDontNest(result, -2525, -2646);
    _putDontNest(result, -2514, -2631);
    _putDontNest(result, -2553, -2666);
    _putDontNest(result, -509, -512);
    _putDontNest(result, -2507, -2542);
    _putDontNest(result, -2414, -2510);
    _putDontNest(result, -2583, -2641);
    _putDontNest(result, -2587, -2597);
    _putDontNest(result, -2572, -2616);
    _putDontNest(result, -2651, -2661);
    _putDontNest(result, -2573, -2607);
    _putDontNest(result, -2403, -2631);
    _putDontNest(result, -2525, -2587);
    _putDontNest(result, -2520, -2592);
    _putDontNest(result, -2538, -2547);
    _putDontNest(result, -2567, -2641);
    _putDontNest(result, -2588, -2616);
    _putDontNest(result, -2334, -2616);
    _putDontNest(result, -2319, -2651);
    _putDontNest(result, -2557, -2646);
    _putDontNest(result, -60, -102);
    _putDontNest(result, -2414, -2616);
    _putDontNest(result, -2507, -2572);
    _putDontNest(result, -2498, -2567);
    _putDontNest(result, -2558, -2651);
    _putDontNest(result, -2520, -2621);
    _putDontNest(result, -2538, -2607);
    _putDontNest(result, -2548, -2641);
    _putDontNest(result, -2543, -2616);
    _putDontNest(result, -2319, -2547);
    _putDontNest(result, -2334, -2510);
    _putDontNest(result, -2622, -2666);
    _putDontNest(result, -2592, -2636);
    _putDontNest(result, -2582, -2626);
    _putDontNest(result, -2603, -2661);
    _putDontNest(result, -2387, -2567);
    _putDontNest(result, -2513, -2562);
    _putDontNest(result, -2514, -2567);
    _putDontNest(result, -2542, -2651);
    _putDontNest(result, -2553, -2602);
    _putDontNest(result, -2525, -2582);
    _putDontNest(result, -2334, -2577);
    _putDontNest(result, -2513, -2524);
    _putDontNest(result, -2392, -2524);
    _putDontNest(result, -2768, -2860);
    _putDontNest(result, -2598, -2626);
    _putDontNest(result, -2597, -2631);
    _putDontNest(result, -2587, -2661);
    _putDontNest(result, -2602, -2646);
    _putDontNest(result, -704, -708);
    _putDontNest(result, -2608, -2636);
    _putDontNest(result, -576, -707);
    _putDontNest(result, -2403, -2567);
    _putDontNest(result, -2552, -2621);
    _putDontNest(result, -2510, -2519);
    _putDontNest(result, -2334, -2542);
    _putDontNest(result, -2563, -2592);
    _putDontNest(result, -2631, -2641);
    _putDontNest(result, -2510, -2651);
    _putDontNest(result, -2557, -2582);
    _putDontNest(result, -2603, -2616);
    _putDontNest(result, -2616, -2641);
    _putDontNest(result, -2319, -2582);
    _putDontNest(result, -2557, -2661);
    _putDontNest(result, -2548, -2577);
    _putDontNest(result, -2558, -2587);
    _putDontNest(result, -2510, -2552);
    _putDontNest(result, -2507, -2557);
    _putDontNest(result, -403, -413);
    _putDontNest(result, -2602, -2651);
    _putDontNest(result, -2414, -2661);
    _putDontNest(result, -2387, -2626);
    _putDontNest(result, -2437, -2621);
    _putDontNest(result, -2542, -2587);
    _putDontNest(result, -2403, -2577);
    _putDontNest(result, -408, -418);
    _putDontNest(result, -2334, -2519);
    _putDontNest(result, -2652, -2661);
    _putDontNest(result, -2573, -2602);
    _putDontNest(result, -2588, -2597);
    _putDontNest(result, -2334, -2597);
    _putDontNest(result, -512, -707);
    _putDontNest(result, -2403, -2626);
    _putDontNest(result, -2538, -2636);
    _putDontNest(result, -2525, -2661);
    _putDontNest(result, -2392, -2592);
    _putDontNest(result, -2387, -2577);
    _putDontNest(result, -2542, -2552);
    _putDontNest(result, -2392, -2537);
    _putDontNest(result, -2636, -2661);
    _putDontNest(result, -2563, -2656);
    _putDontNest(result, -2572, -2597);
    _putDontNest(result, -2568, -2641);
    _putDontNest(result, -2853, -2865);
    _putDontNest(result, -2587, -2616);
    _putDontNest(result, -2547, -2631);
    _putDontNest(result, -2548, -2626);
    _putDontNest(result, -2510, -2587);
    _putDontNest(result, -2319, -2646);
    _putDontNest(result, -2517, -2621);
    _putDontNest(result, -2557, -2597);
    _putDontNest(result, -2542, -2616);
    _putDontNest(result, -2414, -2519);
    _putDontNest(result, -2621, -2666);
    _putDontNest(result, -2582, -2631);
    _putDontNest(result, -2414, -2597);
    _putDontNest(result, -2387, -2562);
    _putDontNest(result, -2543, -2651);
    _putDontNest(result, -2558, -2616);
    _putDontNest(result, -2514, -2524);
    _putDontNest(result, -159, -159);
    _putDontNest(result, -2578, -2592);
    _putDontNest(result, -2587, -2587);
    _putDontNest(result, -2588, -2661);
    _putDontNest(result, -2573, -2666);
    _putDontNest(result, -2598, -2631);
    _putDontNest(result, -2607, -2636);
    _putDontNest(result, -2597, -2626);
    _putDontNest(result, -576, -708);
    _putDontNest(result, -2403, -2562);
    _putDontNest(result, -2334, -2661);
    _putDontNest(result, -2507, -2607);
    _putDontNest(result, -2513, -2641);
    _putDontNest(result, -2538, -2572);
    _putDontNest(result, -2510, -2616);
    _putDontNest(result, -2525, -2597);
    _putDontNest(result, -2498, -2524);
    _putDontNest(result, -2562, -2592);
    _putDontNest(result, -2785, -2880);
    _putDontNest(result, -2617, -2646);
    _putDontNest(result, -2627, -2656);
    _putDontNest(result, -2572, -2661);
    _putDontNest(result, -2547, -2567);
    _putDontNest(result, -2548, -2562);
      
    return result;
  }
    
  protected static IntegerMap _initDontNestGroups() {
    IntegerMap result = new IntegerMap();
    int resultStoreId = result.size();
    
    
    ++resultStoreId;
    
    result.putUnsafe(-517, resultStoreId);
    result.putUnsafe(-520, resultStoreId);
    result.putUnsafe(-509, resultStoreId);
    result.putUnsafe(-512, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-60, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2593, resultStoreId);
    result.putUnsafe(-2597, resultStoreId);
    result.putUnsafe(-2598, resultStoreId);
    result.putUnsafe(-2602, resultStoreId);
    result.putUnsafe(-2603, resultStoreId);
    result.putUnsafe(-2588, resultStoreId);
    result.putUnsafe(-2607, resultStoreId);
    result.putUnsafe(-2592, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-409, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2647, resultStoreId);
    result.putUnsafe(-2651, resultStoreId);
    result.putUnsafe(-2652, resultStoreId);
    result.putUnsafe(-2656, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2616, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-418, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2641, resultStoreId);
    result.putUnsafe(-2646, resultStoreId);
    result.putUnsafe(-2636, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2627, resultStoreId);
    result.putUnsafe(-2631, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2387, resultStoreId);
    result.putUnsafe(-2403, resultStoreId);
    result.putUnsafe(-2437, resultStoreId);
    result.putUnsafe(-2392, resultStoreId);
    result.putUnsafe(-2414, resultStoreId);
    result.putUnsafe(-2334, resultStoreId);
    result.putUnsafe(-2319, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2578, resultStoreId);
    result.putUnsafe(-2582, resultStoreId);
    result.putUnsafe(-2583, resultStoreId);
    result.putUnsafe(-2568, resultStoreId);
    result.putUnsafe(-2587, resultStoreId);
    result.putUnsafe(-2573, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2547, resultStoreId);
    result.putUnsafe(-2552, resultStoreId);
    result.putUnsafe(-2538, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-164, resultStoreId);
    result.putUnsafe(-155, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2562, resultStoreId);
    result.putUnsafe(-2548, resultStoreId);
    result.putUnsafe(-2557, resultStoreId);
    result.putUnsafe(-2543, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-404, resultStoreId);
    result.putUnsafe(-413, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2626, resultStoreId);
    result.putUnsafe(-2617, resultStoreId);
    result.putUnsafe(-2621, resultStoreId);
    result.putUnsafe(-2622, resultStoreId);
    result.putUnsafe(-2608, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-403, resultStoreId);
    result.putUnsafe(-408, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2520, resultStoreId);
    result.putUnsafe(-2525, resultStoreId);
    result.putUnsafe(-2542, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2513, resultStoreId);
    result.putUnsafe(-2507, resultStoreId);
    result.putUnsafe(-2510, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2567, resultStoreId);
    result.putUnsafe(-2553, resultStoreId);
    result.putUnsafe(-2558, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2785, resultStoreId);
    result.putUnsafe(-2771, resultStoreId);
    result.putUnsafe(-2853, resultStoreId);
    result.putUnsafe(-2806, resultStoreId);
    result.putUnsafe(-2790, resultStoreId);
    result.putUnsafe(-2843, resultStoreId);
    result.putUnsafe(-2768, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-600, resultStoreId);
    result.putUnsafe(-504, resultStoreId);
    result.putUnsafe(-605, resultStoreId);
    result.putUnsafe(-623, resultStoreId);
    result.putUnsafe(-576, resultStoreId);
    result.putUnsafe(-704, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2661, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2866, resultStoreId);
    result.putUnsafe(-2871, resultStoreId);
    result.putUnsafe(-2880, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2577, resultStoreId);
    result.putUnsafe(-2563, resultStoreId);
    result.putUnsafe(-2572, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2856, resultStoreId);
    result.putUnsafe(-2861, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2498, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2865, resultStoreId);
    result.putUnsafe(-2870, resultStoreId);
    result.putUnsafe(-2875, resultStoreId);
    result.putUnsafe(-2860, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2514, resultStoreId);
    result.putUnsafe(-2517, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(-2657, resultStoreId);
    result.putUnsafe(-2666, resultStoreId);
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
  private static final IConstructor regular__opt__$Rest = (IConstructor) _read("regular(opt(lex(\"Rest\")))", Factory.Production);
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
  private static final IConstructor prod__$RascalKeywords__lit_filter_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"filter\")],{})", Factory.Production);
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
  private static final IConstructor prod__Default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_opt__$Rest_ = (IConstructor) _read("prod(label(\"Default\",sort(\"PreModule\")),[label(\"header\",sort(\"Header\")),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(keywords(\"HeaderKeyword\"))}),layouts(\"LAYOUTLIST\"),label(\"rest\",opt(lex(\"Rest\")))],{})", Factory.Production);
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
  private static final IConstructor regular__iter_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star__$NamedRegExp = (IConstructor) _read("regular(\\iter-star(lex(\"NamedRegExp\")))", Factory.Production);
  private static final IConstructor prod__$Rest__conditional__iter__char_class___range__0_65535__not_follow__char_class___range__0_65535_ = (IConstructor) _read("prod(lex(\"Rest\"),[conditional(iter(\\char-class([range(0,65535)])),{\\not-follow(\\char-class([range(0,65535)]))})],{})", Factory.Production);
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
  private static final IConstructor prod__Bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"Bracket\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__FieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_ = (IConstructor) _read("prod(label(\"FieldAccess\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"field\",lex(\"Name\"))],{})", Factory.Production);
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
  private static final IConstructor regular__iter__char_class___range__0_65535 = (IConstructor) _read("regular(iter(\\char-class([range(0,65535)])))", Factory.Production);
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
      
      tmp[6] = new NonTerminalStackNode(-214, 6, "$Type", null, null);
      tmp[5] = new NonTerminalStackNode(-213, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-212, 4, prod__lit___60_58__char_class___range__60_60_char_class___range__58_58_, new char[] {60,58}, null, null);
      tmp[3] = new NonTerminalStackNode(-211, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-210, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-209, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-208, 0, prod__lit___38__char_class___range__38_38_, new char[] {38}, null, null);
      builder.addAlternative(RascalRascal.prod__Bounded_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___60_58_$layouts_LAYOUTLIST_bound_$Type_, tmp);
	}
    protected static final void _init_prod__Free_$TypeVar__lit___38_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-217, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-216, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-215, 0, prod__lit___38__char_class___range__38_38_, new char[] {38}, null, null);
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
      
      tmp[8] = new LiteralStackNode(-251, 8, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode(-250, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-245, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-246, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-247, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-248, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-249, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-244, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-243, 4, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(-242, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-237, 2, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-238, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-239, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-240, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-241, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-236, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-235, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__Set_$Comprehension__lit___123_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-268, 12, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode(-267, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-262, 10, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-263, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-264, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-265, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-266, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(-261, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-260, 8, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode(-259, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-258, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-257, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-256, 4, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(-255, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-254, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-253, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-252, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Map_$Comprehension__lit___40_$layouts_LAYOUTLIST_from_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_to_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__List_$Comprehension__lit___91_$layouts_LAYOUTLIST_results_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-285, 8, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[7] = new NonTerminalStackNode(-284, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-279, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-280, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-281, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-282, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-283, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-278, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-277, 4, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(-276, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-271, 2, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-272, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-273, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-274, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-275, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-270, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-269, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
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
      
      tmp[0] = new SeparatedListStackNode(-232, 0, regular__iter_star_seps__$FunctionModifier__$layouts_LAYOUTLIST, new NonTerminalStackNode(-233, 0, "$FunctionModifier", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-234, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-286, 0, "$DataTypeSelector", null, null);
      builder.addAlternative(RascalRascal.prod__Selector_$Type__selector_$DataTypeSelector_, tmp);
	}
    protected static final void _init_prod__Structured_$Type__structured_$StructuredType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-287, 0, "$StructuredType", null, null);
      builder.addAlternative(RascalRascal.prod__Structured_$Type__structured_$StructuredType_, tmp);
	}
    protected static final void _init_prod__Bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-292, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-291, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-290, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-289, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-288, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Bracket_$Type__lit___40_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Function_$Type__function_$FunctionType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-293, 0, "$FunctionType", null, null);
      builder.addAlternative(RascalRascal.prod__Function_$Type__function_$FunctionType_, tmp);
	}
    protected static final void _init_prod__User_$Type__conditional__user_$UserType__delete__$HeaderKeyword_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-294, 0, "$UserType", null, new ICompletionFilter[] {new StringMatchRestriction(new char[] {108,101,120,105,99,97,108}), new StringMatchRestriction(new char[] {105,109,112,111,114,116}), new StringMatchRestriction(new char[] {115,116,97,114,116}), new StringMatchRestriction(new char[] {115,121,110,116,97,120}), new StringMatchRestriction(new char[] {107,101,121,119,111,114,100}), new StringMatchRestriction(new char[] {101,120,116,101,110,100}), new StringMatchRestriction(new char[] {108,97,121,111,117,116})});
      builder.addAlternative(RascalRascal.prod__User_$Type__conditional__user_$UserType__delete__$HeaderKeyword_, tmp);
	}
    protected static final void _init_prod__Basic_$Type__basic_$BasicType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-295, 0, "$BasicType", null, null);
      builder.addAlternative(RascalRascal.prod__Basic_$Type__basic_$BasicType_, tmp);
	}
    protected static final void _init_prod__Variable_$Type__typeVar_$TypeVar_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-296, 0, "$TypeVar", null, null);
      builder.addAlternative(RascalRascal.prod__Variable_$Type__typeVar_$TypeVar_, tmp);
	}
    protected static final void _init_prod__Symbol_$Type__symbol_$Sym_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-297, 0, "$Sym", null, null);
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
      
      tmp[12] = new LiteralStackNode(-311, 12, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[11] = new NonTerminalStackNode(-310, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-309, 10, "$Type", null, null);
      tmp[9] = new NonTerminalStackNode(-308, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-307, 8, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(-306, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-305, 6, "$UserType", null, null);
      tmp[5] = new NonTerminalStackNode(-304, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-303, 4, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new char[] {97,108,105,97,115}, null, null);
      tmp[3] = new NonTerminalStackNode(-302, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-301, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-300, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-299, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Alias_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_alias_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_base_$Type_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Function_$Declaration__functionDeclaration_$FunctionDeclaration_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-298, 0, "$FunctionDeclaration", null, null);
      builder.addAlternative(RascalRascal.prod__Function_$Declaration__functionDeclaration_$FunctionDeclaration_, tmp);
	}
    protected static final void _init_prod__Rule_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_lit_rule_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_patternAction_$PatternWithAction_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[3] = new NonTerminalStackNode(-315, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-316, 4, "$Name", null, null);
      tmp[5] = new NonTerminalStackNode(-317, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-318, 6, "$PatternWithAction", null, null);
      tmp[7] = new NonTerminalStackNode(-319, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-320, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[0] = new NonTerminalStackNode(-312, 0, "$Tags", null, null);
      tmp[1] = new NonTerminalStackNode(-313, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-314, 2, prod__lit_rule__char_class___range__114_114_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {114,117,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Rule_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_lit_rule_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_patternAction_$PatternWithAction_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(-335, 14, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode(-334, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(-333, 12, "$Name", null, null);
      tmp[11] = new NonTerminalStackNode(-332, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-331, 10, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      tmp[9] = new NonTerminalStackNode(-330, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-329, 8, "$Type", null, null);
      tmp[7] = new NonTerminalStackNode(-328, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-327, 6, "$Type", null, null);
      tmp[5] = new NonTerminalStackNode(-326, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-325, 4, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new char[] {97,110,110,111}, null, null);
      tmp[3] = new NonTerminalStackNode(-324, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-323, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-322, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-321, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Annotation_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_anno_$layouts_LAYOUTLIST_annoType_$Type_$layouts_LAYOUTLIST_onType_$Type_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(-354, 14, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode(-353, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(-348, 12, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-349, 0, "$Type", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-350, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-351, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-352, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode(-347, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-346, 10, prod__lit_on__char_class___range__111_111_char_class___range__110_110_, new char[] {111,110}, null, null);
      tmp[9] = new NonTerminalStackNode(-345, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-344, 8, "$Name", null, null);
      tmp[7] = new NonTerminalStackNode(-343, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-342, 6, "$Kind", null, null);
      tmp[5] = new NonTerminalStackNode(-341, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-340, 4, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new char[] {116,97,103}, null, null);
      tmp[3] = new NonTerminalStackNode(-339, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-338, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-337, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-336, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Tag_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_tag_$layouts_LAYOUTLIST_kind_$Kind_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit_on_$layouts_LAYOUTLIST_types_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-367, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(-366, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-361, 6, regular__iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-362, 0, "$Variable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-363, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-364, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-365, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-360, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-359, 4, "$Type", null, null);
      tmp[3] = new NonTerminalStackNode(-358, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-357, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-356, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-355, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Variable_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_variables_iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__DataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-376, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(-375, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-374, 6, "$UserType", null, null);
      tmp[5] = new NonTerminalStackNode(-373, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-372, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new char[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode(-371, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-370, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-369, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-368, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__DataAbstract_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Data_$Declaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_lit_data_$layouts_LAYOUTLIST_user_$UserType_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_variants_iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-393, 12, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[11] = new NonTerminalStackNode(-392, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-387, 10, regular__iter_seps__$Variant__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST, new NonTerminalStackNode(-388, 0, "$Variant", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-389, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-390, 2, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null), new NonTerminalStackNode(-391, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(-386, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-385, 8, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(-384, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-383, 6, "$UserType", null, null);
      tmp[5] = new NonTerminalStackNode(-382, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-381, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new char[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode(-380, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-379, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-378, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-377, 0, "$Tags", null, null);
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
      
      tmp[4] = new NonTerminalStackNode(-418, 4, "$Class", null, null);
      tmp[3] = new NonTerminalStackNode(-417, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-416, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new char[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode(-415, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-414, 0, "$Class", null, null);
      builder.addAlternative(RascalRascal.prod__Union_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Class__assoc__left, tmp);
	}
    protected static final void _init_prod__SimpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-400, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-399, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-396, 2, regular__iter_star_seps__$Range__$layouts_LAYOUTLIST, new NonTerminalStackNode(-397, 0, "$Range", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-398, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-395, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-394, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__SimpleCharclass_$Class__lit___91_$layouts_LAYOUTLIST_ranges_iter_star_seps__$Range__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-423, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-422, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-421, 2, "$Class", null, null);
      tmp[1] = new NonTerminalStackNode(-420, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-419, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Bracket_$Class__lit___40_$layouts_LAYOUTLIST_charclass_$Class_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-403, 2, "$Class", null, null);
      tmp[1] = new NonTerminalStackNode(-402, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-401, 0, prod__lit___33__char_class___range__33_33_, new char[] {33}, null, null);
      builder.addAlternative(RascalRascal.prod__Complement_$Class__lit___33_$layouts_LAYOUTLIST_charClass_$Class_, tmp);
	}
    protected static final void _init_prod__Difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-408, 4, "$Class", null, null);
      tmp[3] = new NonTerminalStackNode(-407, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-406, 2, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode(-405, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-404, 0, "$Class", null, null);
      builder.addAlternative(RascalRascal.prod__Difference_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Class__assoc__left, tmp);
	}
    protected static final void _init_prod__Intersection_$Class__lhs_$Class_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Class__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-413, 4, "$Class", null, null);
      tmp[3] = new NonTerminalStackNode(-412, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-411, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new char[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode(-410, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-409, 0, "$Class", null, null);
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
      
      tmp[0] = new EpsilonStackNode(-424, 0);
      builder.addAlternative(RascalRascal.prod__Empty_$Bound__, tmp);
	}
    protected static final void _init_prod__Default_$Bound__lit___59_$layouts_LAYOUTLIST_expression_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-427, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-426, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-425, 0, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
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
      
      tmp[6] = new LiteralStackNode(-447, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-446, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-441, 4, regular__iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-442, 0, "$TypeArg", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-443, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-444, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-445, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-440, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-439, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-438, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-437, 0, "$Type", null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-454, 2, "$PatternWithAction", null, null);
      tmp[1] = new NonTerminalStackNode(-453, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-452, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new char[] {99,97,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__PatternWithAction_$Case__lit_case_$layouts_LAYOUTLIST_patternWithAction_$PatternWithAction__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Default_$Case__lit_default_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-459, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-458, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-457, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-456, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-455, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new char[] {100,101,102,97,117,108,116}, null, null);
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
      
      tmp[0] = new LiteralStackNode(-521, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new char[] {102,97,105,108}, null, null);
      tmp[1] = new NonTerminalStackNode(-522, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-523, 2, "$Target", null, null);
      tmp[3] = new NonTerminalStackNode(-524, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-525, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__Fail_$Statement__lit_fail_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__TryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode(-504, 8, "$Statement", null, null);
      tmp[7] = new NonTerminalStackNode(-503, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-502, 6, prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_, new char[] {102,105,110,97,108,108,121}, null, null);
      tmp[5] = new NonTerminalStackNode(-501, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-498, 4, regular__iter_seps__$Catch__$layouts_LAYOUTLIST, new NonTerminalStackNode(-499, 0, "$Catch", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-500, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-497, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-496, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode(-495, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-494, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new char[] {116,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__TryFinally_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit_finally_$layouts_LAYOUTLIST_finallyBody_$Statement_, tmp);
	}
    protected static final void _init_prod__Insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-509, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-508, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-507, 2, "$DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode(-506, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-505, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new char[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Insert_$Statement__lit_insert_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-530, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(-529, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-528, 2, "$Target", null, null);
      tmp[1] = new NonTerminalStackNode(-527, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-526, 0, prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_, new char[] {99,111,110,116,105,110,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Continue_$Statement__lit_continue_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-535, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(-534, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-533, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-532, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-531, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new char[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Assert_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__FunctionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-708, 0, "$FunctionDeclaration", null, null);
      builder.addAlternative(RascalRascal.prod__FunctionDeclaration_$Statement__functionDeclaration_$FunctionDeclaration_, tmp);
	}
    protected static final void _init_prod__Expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode(-537, 0, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-538, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-539, 2, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__Expression_$Statement__expression_$Expression_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__EmptyStatement_$Statement__lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-536, 0, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__EmptyStatement_$Statement__lit___59_, tmp);
	}
    protected static final void _init_prod__Try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode(-544, 4, regular__iter_seps__$Catch__$layouts_LAYOUTLIST, new NonTerminalStackNode(-545, 0, "$Catch", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-546, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-543, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-542, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode(-541, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-540, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new char[] {116,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__Try_$Statement__lit_try_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_handlers_iter_seps__$Catch__$layouts_LAYOUTLIST__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__DoWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(-561, 14, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode(-560, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode(-559, 12, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode(-558, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-557, 10, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode(-556, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-555, 8, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[7] = new NonTerminalStackNode(-554, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-553, 6, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new char[] {119,104,105,108,101}, null, null);
      tmp[5] = new NonTerminalStackNode(-552, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-551, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-550, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-549, 2, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new char[] {100,111}, null, null);
      tmp[1] = new NonTerminalStackNode(-548, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-547, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__DoWhile_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_do_$layouts_LAYOUTLIST_body_$Statement_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__For_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode(-576, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode(-575, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-574, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-573, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-568, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-569, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-570, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-571, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-572, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-567, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-566, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-565, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-564, 2, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new char[] {102,111,114}, null, null);
      tmp[1] = new NonTerminalStackNode(-563, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-562, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__For_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_, tmp);
	}
    protected static final void _init_prod__AssertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-585, 8, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode(-584, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-583, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-582, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-581, 4, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(-580, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-579, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-578, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-577, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new char[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__AssertWithMessage_$Statement__lit_assert_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_message_$Expression_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__While_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode(-600, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode(-599, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-598, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-597, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-592, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-593, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-594, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-595, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-596, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-591, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-590, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-589, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-588, 2, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new char[] {119,104,105,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode(-587, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-586, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__While_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_, tmp);
	}
    protected static final void _init_prod__Assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-605, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-604, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-603, 2, "$Assignment", null, null);
      tmp[1] = new NonTerminalStackNode(-602, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-601, 0, "$Assignable", null, null);
      builder.addAlternative(RascalRascal.prod__Assignment_$Statement__assignable_$Assignable_$layouts_LAYOUTLIST_operator_$Assignment_$layouts_LAYOUTLIST_statement_$Statement_, tmp);
	}
    protected static final void _init_prod__Filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-608, 2, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode(-607, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-606, 0, prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new char[] {102,105,108,116,101,114}, null, null);
      builder.addAlternative(RascalRascal.prod__Filter_$Statement__lit_filter_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode(-623, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode(-622, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-621, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-620, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-619, 6, "$Bound", null, null);
      tmp[5] = new NonTerminalStackNode(-618, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-613, 4, regular__iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-614, 0, "$QualifiedName", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-615, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-616, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-617, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-612, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-611, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-610, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-609, 0, prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_, new char[] {115,111,108,118,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Solve_$Statement__lit_solve_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_variables_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_bound_$Bound_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_body_$Statement_, tmp);
	}
    protected static final void _init_prod__Break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-639, 4, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode(-638, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-637, 2, "$Target", null, null);
      tmp[1] = new NonTerminalStackNode(-636, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-635, 0, prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new char[] {98,114,101,97,107}, null, null);
      builder.addAlternative(RascalRascal.prod__Break_$Statement__lit_break_$layouts_LAYOUTLIST_target_$Target_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__VariableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-707, 2, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode(-706, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-705, 0, "$LocalVariableDeclaration", null, null);
      builder.addAlternative(RascalRascal.prod__VariableDeclaration_$Statement__declaration_$LocalVariableDeclaration_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__GlobalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-634, 6, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode(-633, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-628, 4, regular__iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-629, 0, "$QualifiedName", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-630, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-631, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-632, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-627, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-626, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-625, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-624, 0, prod__lit_global__char_class___range__103_103_char_class___range__108_108_char_class___range__111_111_char_class___range__98_98_char_class___range__97_97_char_class___range__108_108_, new char[] {103,108,111,98,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__GlobalDirective_$Statement__lit_global_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_names_iter_seps__$QualifiedName__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode(-510, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new char[] {114,101,116,117,114,110}, null, null);
      tmp[1] = new NonTerminalStackNode(-511, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-512, 2, "$Statement", null, null);
      builder.addAlternative(RascalRascal.prod__Return_$Statement__lit_return_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__NonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-648, 6, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode(-647, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-644, 4, regular__iter_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-645, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-646, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-643, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-640, 0, "$Label", null, null);
      tmp[1] = new NonTerminalStackNode(-641, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-642, 2, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__NonEmptyBlock_$Statement__label_$Label_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-517, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-516, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-515, 2, "$DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode(-514, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-513, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new char[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__Append_$Statement__lit_append_$layouts_LAYOUTLIST_dataTarget_$DataTarget_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-520, 2, "$Statement", null, null);
      tmp[1] = new NonTerminalStackNode(-519, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-518, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new char[] {116,104,114,111,119}, null, null);
      builder.addAlternative(RascalRascal.prod__Throw_$Statement__lit_throw_$layouts_LAYOUTLIST_statement_$Statement__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-668, 2, "$Visit", null, null);
      tmp[1] = new NonTerminalStackNode(-667, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-666, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__Visit_$Statement__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_, tmp);
	}
    protected static final void _init_prod__IfThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new EmptyStackNode(-665, 12, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {101,108,115,101})});
      tmp[11] = new NonTerminalStackNode(-664, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-663, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode(-662, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-661, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-660, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-655, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-656, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-657, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-658, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-659, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-654, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-653, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-652, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-651, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new char[] {105,102}, null, null);
      tmp[1] = new NonTerminalStackNode(-650, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-649, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__IfThen_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_conditional__empty__not_follow__lit_else_, tmp);
	}
    protected static final void _init_prod__Switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(-685, 14, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode(-684, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(-681, 12, regular__iter_seps__$Case__$layouts_LAYOUTLIST, new NonTerminalStackNode(-682, 0, "$Case", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-683, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode(-680, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-679, 10, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode(-678, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-677, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-676, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-675, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-674, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-673, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-672, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-671, 2, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new char[] {115,119,105,116,99,104}, null, null);
      tmp[1] = new NonTerminalStackNode(-670, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-669, 0, "$Label", null, null);
      builder.addAlternative(RascalRascal.prod__Switch_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_switch_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__IfThenElse_$Statement__label_$Label_$layouts_LAYOUTLIST_lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_thenStatement_$Statement_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_elseStatement_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new NonTerminalStackNode(-704, 14, "$Statement", null, null);
      tmp[13] = new NonTerminalStackNode(-703, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode(-702, 12, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new char[] {101,108,115,101}, null, null);
      tmp[11] = new NonTerminalStackNode(-701, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-700, 10, "$Statement", null, null);
      tmp[9] = new NonTerminalStackNode(-699, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-698, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-697, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-692, 6, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-693, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-694, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-695, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-696, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-691, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-690, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-689, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-688, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new char[] {105,102}, null, null);
      tmp[1] = new NonTerminalStackNode(-687, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-686, 0, "$Label", null, null);
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
      
      tmp[2] = new SeparatedListStackNode(-727, 2, regular__iter_seps__$Renaming__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-728, 0, "$Renaming", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-729, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-730, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-731, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-726, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-725, 0, prod__lit_renaming__char_class___range__114_114_char_class___range__101_101_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_, new char[] {114,101,110,97,109,105,110,103}, null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-732, 0, "$StringConstant", null, null);
      builder.addAlternative(RascalRascal.prod__NonInterpolated_$StringLiteral__constant_$StringConstant_, tmp);
	}
    protected static final void _init_prod__Template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-737, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(-736, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-735, 2, "$StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode(-734, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-733, 0, "$PreStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__Template_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    protected static final void _init_prod__Interpolated_$StringLiteral__pre_$PreStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-742, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(-741, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-740, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-739, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-738, 0, "$PreStringChars", null, null);
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
      
      tmp[0] = new LiteralStackNode(-756, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new char[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__Public_$Visibility__lit_public_, tmp);
	}
    protected static final void _init_prod__Default_$Visibility__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(-757, 0);
      builder.addAlternative(RascalRascal.prod__Default_$Visibility__, tmp);
	}
    protected static final void _init_prod__Private_$Visibility__lit_private_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-758, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new char[] {112,114,105,118,97,116,101}, null, null);
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
      
      tmp[2] = new LiteralStackNode(-761, 2, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[1] = new NonTerminalStackNode(-760, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode(-759, 0, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
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
      
      tmp[0] = new CharStackNode(-767, 0, new char[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{60,60},{62,62},{92,92}})});
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
      
      tmp[0] = new NonTerminalStackNode(-808, 0, "$Import", null, null);
      builder.addAlternative(RascalRascal.prod__Import_$Command__imported_$Import_, tmp);
	}
    protected static final void _init_prod__Expression_$Command__expression_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-809, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Expression_$Command__expression_$Expression_, tmp);
	}
    protected static final void _init_prod__Statement_$Command__statement_$Statement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-810, 0, "$Statement", null, null);
      builder.addAlternative(RascalRascal.prod__Statement_$Command__statement_$Statement_, tmp);
	}
    protected static final void _init_prod__Declaration_$Command__declaration_$Declaration_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-811, 0, "$Declaration", null, null);
      builder.addAlternative(RascalRascal.prod__Declaration_$Command__declaration_$Declaration_, tmp);
	}
    protected static final void _init_prod__Shell_$Command__lit___58_$layouts_LAYOUTLIST_command_$ShellCommand_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-814, 2, "$ShellCommand", null, null);
      tmp[1] = new NonTerminalStackNode(-813, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-812, 0, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
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
      
      tmp[12] = new LiteralStackNode(-790, 12, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[11] = new NonTerminalStackNode(-789, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-786, 10, regular__iter_seps__$Case__$layouts_LAYOUTLIST, new NonTerminalStackNode(-787, 0, "$Case", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-788, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(-785, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-784, 8, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(-783, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-782, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-781, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-780, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-779, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-778, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-777, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-776, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new char[] {118,105,115,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__DefaultStrategy_$Visit__lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__GivenStrategy_$Visit__strategy_$Strategy_$layouts_LAYOUTLIST_lit_visit_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_subject_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_cases_iter_seps__$Case__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode(-807, 14, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode(-806, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(-803, 12, regular__iter_seps__$Case__$layouts_LAYOUTLIST, new NonTerminalStackNode(-804, 0, "$Case", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-805, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode(-802, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-801, 10, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode(-800, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-799, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-798, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-797, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-796, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-795, 4, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-794, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-793, 2, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new char[] {118,105,115,105,116}, null, null);
      tmp[1] = new NonTerminalStackNode(-792, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-791, 0, "$Strategy", null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-815, 0, "$PostProtocolChars", null, null);
      builder.addAlternative(RascalRascal.prod__Post_$ProtocolTail__post_$PostProtocolChars_, tmp);
	}
    protected static final void _init_prod__Mid_$ProtocolTail__mid_$MidProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-820, 4, "$ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode(-819, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-818, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-817, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-816, 0, "$MidProtocolChars", null, null);
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
      
      tmp[2] = new CharStackNode(-832, 2, new char[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode(-830, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode(-831, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode(-829, 0, new char[][]{{34,34}}, null, null);
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
      
      tmp[0] = new SeparatedListStackNode(-844, 0, regular__iter_seps__$Name__$layouts_LAYOUTLIST_lit___58_58_$layouts_LAYOUTLIST, new NonTerminalStackNode(-845, 0, "$Name", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-846, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-847, 2, prod__lit___58_58__char_class___range__58_58_char_class___range__58_58_, new char[] {58,58}, null, null), new NonTerminalStackNode(-848, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {58,58})});
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
      
      tmp[4] = new NonTerminalStackNode(-859, 4, "$StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode(-858, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-857, 2, "$StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode(-856, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-855, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__Template_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringMiddle_, tmp);
	}
    protected static final void _init_prod__Interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-864, 4, "$StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode(-863, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-862, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-861, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-860, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__Interpolated_$StringMiddle__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringMiddle_, tmp);
	}
    protected static final void _init_prod__Mid_$StringMiddle__mid_$MidStringChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-865, 0, "$MidStringChars", null, null);
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
      
      tmp[0] = new ListStackNode(-853, 0, regular__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_65535, new CharStackNode(-854, 0, new char[][]{{0,8},{11,12},{14,31},{33,59},{61,123},{125,65535}}, null, null), false, null, null);
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
      
      tmp[0] = new LiteralStackNode(-866, 0, prod__lit_Z__char_class___range__90_90_, new char[] {90}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimeZonePart__lit_Z_, tmp);
	}
    protected static final void _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(-869, 2, new char[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(-868, 1, new char[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode(-867, 0, new char[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode(-874, 4, new char[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode(-873, 3, new char[][]{{48,53}}, null, null);
      tmp[2] = new CharStackNode(-872, 2, new char[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(-871, 1, new char[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode(-870, 0, new char[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[6];
      
      tmp[5] = new CharStackNode(-880, 5, new char[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode(-879, 4, new char[][]{{48,53}}, null, null);
      tmp[3] = new LiteralStackNode(-878, 3, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[2] = new CharStackNode(-877, 2, new char[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(-876, 1, new char[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode(-875, 0, new char[][]{{43,43},{45,45}}, null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-883, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(-882, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-881, 0, prod__lit_edit__char_class___range__101_101_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_, new char[] {101,100,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Edit_$ShellCommand__lit_edit_$layouts_LAYOUTLIST_name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Help_$ShellCommand__lit_help_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-884, 0, prod__lit_help__char_class___range__104_104_char_class___range__101_101_char_class___range__108_108_char_class___range__112_112_, new char[] {104,101,108,112}, null, null);
      builder.addAlternative(RascalRascal.prod__Help_$ShellCommand__lit_help_, tmp);
	}
    protected static final void _init_prod__Quit_$ShellCommand__lit_quit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-885, 0, prod__lit_quit__char_class___range__113_113_char_class___range__117_117_char_class___range__105_105_char_class___range__116_116_, new char[] {113,117,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Quit_$ShellCommand__lit_quit_, tmp);
	}
    protected static final void _init_prod__SetOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-890, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-889, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-888, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(-887, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-886, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new char[] {115,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__SetOption_$ShellCommand__lit_set_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_expression_$Expression_, tmp);
	}
    protected static final void _init_prod__History_$ShellCommand__lit_history_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-891, 0, prod__lit_history__char_class___range__104_104_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__121_121_, new char[] {104,105,115,116,111,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__History_$ShellCommand__lit_history_, tmp);
	}
    protected static final void _init_prod__Unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-894, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(-893, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-892, 0, prod__lit_unimport__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new char[] {117,110,105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Unimport_$ShellCommand__lit_unimport_$layouts_LAYOUTLIST_name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-897, 2, "$QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode(-896, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-895, 0, prod__lit_undeclare__char_class___range__117_117_char_class___range__110_110_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__101_101_, new char[] {117,110,100,101,99,108,97,114,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Undeclare_$ShellCommand__lit_undeclare_$layouts_LAYOUTLIST_name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__ListModules_$ShellCommand__lit_modules_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-898, 0, prod__lit_modules__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_, new char[] {109,111,100,117,108,101,115}, null, null);
      builder.addAlternative(RascalRascal.prod__ListModules_$ShellCommand__lit_modules_, tmp);
	}
    protected static final void _init_prod__ListDeclarations_$ShellCommand__lit_declarations_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-900, 0, prod__lit_declarations__char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_, new char[] {100,101,99,108,97,114,97,116,105,111,110,115}, null, null);
      builder.addAlternative(RascalRascal.prod__ListDeclarations_$ShellCommand__lit_declarations_, tmp);
	}
    protected static final void _init_prod__Test_$ShellCommand__lit_test_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-899, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new char[] {116,101,115,116}, null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-907, 2, "$PathPart", null, null);
      tmp[1] = new NonTerminalStackNode(-906, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-905, 0, "$ProtocolPart", null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-916, 0, "$Char", null, null);
      builder.addAlternative(RascalRascal.prod__Character_$Range__character_$Char_, tmp);
	}
    protected static final void _init_prod__FromTo_$Range__start_$Char_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_end_$Char_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-921, 4, "$Char", null, null);
      tmp[3] = new NonTerminalStackNode(-920, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-919, 2, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode(-918, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-917, 0, "$Char", null, null);
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
    
    protected static final void _init_prod__$Rest__conditional__iter__char_class___range__0_65535__not_follow__char_class___range__0_65535_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode(-922, 0, regular__iter__char_class___range__0_65535, new CharStackNode(-923, 0, new char[][]{{0,65535}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{0,65535}})});
      builder.addAlternative(RascalRascal.prod__$Rest__conditional__iter__char_class___range__0_65535__not_follow__char_class___range__0_65535_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__$Rest__conditional__iter__char_class___range__0_65535__not_follow__char_class___range__0_65535_(builder);
      
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
      
      tmp[2] = new OptionalStackNode(-935, 2, regular__opt__$TimeZonePart, new NonTerminalStackNode(-936, 0, "$TimeZonePart", null, null), null, null);
      tmp[1] = new NonTerminalStackNode(-934, 1, "$TimePartNoTZ", null, null);
      tmp[0] = new LiteralStackNode(-933, 0, prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_, new char[] {36,84}, null, null);
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
      
      tmp[2] = new CharStackNode(-952, 2, new char[][]{{39,39}}, null, null);
      tmp[1] = new ListStackNode(-950, 1, regular__iter_star__char_class___range__9_9_range__32_32, new CharStackNode(-951, 0, new char[][]{{9,9},{32,32}}, null, null), false, null, null);
      tmp[0] = new CharStackNode(-949, 0, new char[][]{{10,10}}, null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_char_class___range__39_39_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__$OctalEscapeSequence_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-953, 0, "$OctalEscapeSequence", null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__$OctalEscapeSequence_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__$UnicodeEscape_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-954, 0, "$UnicodeEscape", null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__$UnicodeEscape_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_65535_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(-955, 0, new char[][]{{0,33},{35,38},{40,59},{61,61},{63,91},{93,65535}}, null, null);
      builder.addAlternative(RascalRascal.prod__$StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_65535_, tmp);
	}
    protected static final void _init_prod__$StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-957, 1, new char[][]{{34,34},{39,39},{60,60},{62,62},{92,92},{98,98},{102,102},{110,110},{114,114},{116,116}}, null, null);
      tmp[0] = new LiteralStackNode(-956, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
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
      
      tmp[0] = new ListStackNode(-959, 0, regular__iter_star__$LAYOUT, new NonTerminalStackNode(-960, 0, "$LAYOUT", null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{9,10},{13,13},{32,32}}), new StringFollowRestriction(new char[] {47,47}), new StringFollowRestriction(new char[] {47,42})});
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
      
      tmp[2] = new LiteralStackNode(-1004, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
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
      
      tmp[1] = new CharStackNode(-1008, 1, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null);
      tmp[0] = new ListStackNode(-1006, 0, regular__iter__char_class___range__48_57, new CharStackNode(-1007, 0, new char[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new OptionalStackNode(-1016, 4, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(-1017, 0, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[3] = new ListStackNode(-1014, 3, regular__iter__char_class___range__48_57, new CharStackNode(-1015, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new OptionalStackNode(-1012, 2, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode(-1013, 0, new char[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[1] = new CharStackNode(-1011, 1, new char[][]{{69,69},{101,101}}, null, null);
      tmp[0] = new ListStackNode(-1009, 0, regular__iter__char_class___range__48_57, new CharStackNode(-1010, 0, new char[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new OptionalStackNode(-1028, 6, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(-1029, 0, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[5] = new ListStackNode(-1026, 5, regular__iter__char_class___range__48_57, new CharStackNode(-1027, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[4] = new OptionalStackNode(-1024, 4, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode(-1025, 0, new char[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[3] = new CharStackNode(-1023, 3, new char[][]{{69,69},{101,101}}, null, null);
      tmp[2] = new ListStackNode(-1021, 2, regular__iter_star__char_class___range__48_57, new CharStackNode(-1022, 0, new char[][]{{48,57}}, null, null), false, null, null);
      tmp[1] = new LiteralStackNode(-1020, 1, prod__lit___46__char_class___range__46_46_, new char[] {46}, null, null);
      tmp[0] = new ListStackNode(-1018, 0, regular__iter__char_class___range__48_57, new CharStackNode(-1019, 0, new char[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode(-1030, 0, prod__lit___46__char_class___range__46_46_, new char[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new char[][]{{46,46}})}, null);
      tmp[1] = new ListStackNode(-1031, 1, regular__iter__char_class___range__48_57, new CharStackNode(-1032, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new OptionalStackNode(-1033, 2, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(-1034, 0, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[6];
      
      tmp[5] = new OptionalStackNode(-1050, 5, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(-1051, 0, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[4] = new ListStackNode(-1048, 4, regular__iter__char_class___range__48_57, new CharStackNode(-1049, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[3] = new OptionalStackNode(-1046, 3, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode(-1047, 0, new char[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[2] = new CharStackNode(-1045, 2, new char[][]{{69,69},{101,101}}, null, null);
      tmp[1] = new ListStackNode(-1043, 1, regular__iter__char_class___range__48_57, new CharStackNode(-1044, 0, new char[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new LiteralStackNode(-1042, 0, prod__lit___46__char_class___range__46_46_, new char[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new char[][]{{46,46}})}, null);
      builder.addAlternative(RascalRascal.prod__$RealLiteral__conditional__lit___46__not_precede__char_class___range__46_46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__$RealLiteral__iter__char_class___range__48_57_conditional__lit___46__not_follow__lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[4];
      
      tmp[3] = new OptionalStackNode(-1040, 3, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode(-1041, 0, new char[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[2] = new ListStackNode(-1038, 2, regular__iter_star__char_class___range__48_57, new CharStackNode(-1039, 0, new char[][]{{48,57}}, null, null), false, null, null);
      tmp[1] = new LiteralStackNode(-1037, 1, prod__lit___46__char_class___range__46_46_, new char[] {46}, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {46})});
      tmp[0] = new ListStackNode(-1035, 0, regular__iter__char_class___range__48_57, new CharStackNode(-1036, 0, new char[][]{{48,57}}, null, null), true, null, null);
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
      
      tmp[4] = new SeparatedListStackNode(-1062, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1063, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1064, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1065, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1066, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1061, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1060, 2, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new char[] {119,104,101,110}, null, null);
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
      
      tmp[0] = new LiteralStackNode(-1079, 0, prod__lit_right__char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_, new char[] {114,105,103,104,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Right_$Assoc__lit_right_, tmp);
	}
    protected static final void _init_prod__NonAssociative_$Assoc__lit_non_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1080, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new char[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__NonAssociative_$Assoc__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__Left_$Assoc__lit_left_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1081, 0, prod__lit_left__char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_, new char[] {108,101,102,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Left_$Assoc__lit_left_, tmp);
	}
    protected static final void _init_prod__Associative_$Assoc__lit_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1082, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new char[] {97,115,115,111,99}, null, null);
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
      
      tmp[0] = new ListStackNode(-1085, 0, regular__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115, new CharStackNode(-1086, 0, new char[][]{{100,100},{105,105},{109,109},{115,115}}, null, null), false, null, null);
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
      
      tmp[2] = new CharStackNode(-1090, 2, new char[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode(-1088, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode(-1089, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode(-1087, 0, new char[][]{{62,62}}, null, null);
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
      
      tmp[0] = new CharStackNode(-1091, 0, new char[][]{{0,46},{48,59},{61,61},{63,91},{93,65535}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_, tmp);
	}
    protected static final void _init_prod__$RegExp__char_class___range__60_60_expression_$Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(-1094, 2, new char[][]{{62,62}}, null, null);
      tmp[1] = new NonTerminalStackNode(-1093, 1, "$Expression", null, null);
      tmp[0] = new CharStackNode(-1092, 0, new char[][]{{60,60}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__char_class___range__60_60_expression_$Expression_char_class___range__62_62__tag__category___77_101_116_97_86_97_114_105_97_98_108_101, tmp);
	}
    protected static final void _init_prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-1100, 4, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[3] = new ListStackNode(-1098, 3, regular__iter_star__$NamedRegExp, new NonTerminalStackNode(-1099, 0, "$NamedRegExp", null, null), false, null, null);
      tmp[2] = new LiteralStackNode(-1097, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-1096, 1, "$Name", null, null);
      tmp[0] = new LiteralStackNode(-1095, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__lit___60_$Name_lit___58_iter_star__$NamedRegExp_lit___62_, tmp);
	}
    protected static final void _init_prod__$RegExp__lit___60_$Name_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-1103, 2, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode(-1102, 1, "$Name", null, null);
      tmp[0] = new LiteralStackNode(-1101, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__lit___60_$Name_lit___62_, tmp);
	}
    protected static final void _init_prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-1105, 1, new char[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode(-1104, 0, new char[][]{{92,92}}, null, null);
      builder.addAlternative(RascalRascal.prod__$RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__$RegExp__$Backslash_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1106, 0, "$Backslash", null, null);
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
      
      tmp[18] = new LiteralStackNode(-1159, 18, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[17] = new NonTerminalStackNode(-1158, 17, "$layouts_LAYOUTLIST", null, null);
      tmp[16] = new NonTerminalStackNode(-1157, 16, "$Expression", null, null);
      tmp[15] = new NonTerminalStackNode(-1156, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new LiteralStackNode(-1155, 14, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[13] = new NonTerminalStackNode(-1154, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode(-1153, 12, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new char[] {119,104,105,108,101}, null, null);
      tmp[11] = new NonTerminalStackNode(-1152, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-1151, 10, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[9] = new NonTerminalStackNode(-1150, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new SeparatedListStackNode(-1147, 8, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1148, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1149, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode(-1146, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-1145, 6, "$StringMiddle", null, null);
      tmp[5] = new NonTerminalStackNode(-1144, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1141, 4, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1142, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1143, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-1140, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1139, 2, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode(-1138, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1137, 0, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new char[] {100,111}, null, null);
      builder.addAlternative(RascalRascal.prod__DoWhile_$StringTemplate__lit_do_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__While_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode(-1180, 16, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(-1179, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(-1176, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1177, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1178, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(-1175, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(-1174, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(-1173, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-1170, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1171, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1172, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(-1169, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-1168, 8, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(-1167, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1166, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-1165, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1164, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-1163, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1162, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-1161, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1160, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new char[] {119,104,105,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__While_$StringTemplate__lit_while_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_condition_$Expression_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__IfThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[29];
      
      tmp[28] = new LiteralStackNode(-1246, 28, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[27] = new NonTerminalStackNode(-1245, 27, "$layouts_LAYOUTLIST", null, null);
      tmp[26] = new SeparatedListStackNode(-1242, 26, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1243, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1244, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[25] = new NonTerminalStackNode(-1241, 25, "$layouts_LAYOUTLIST", null, null);
      tmp[24] = new NonTerminalStackNode(-1240, 24, "$StringMiddle", null, null);
      tmp[23] = new NonTerminalStackNode(-1239, 23, "$layouts_LAYOUTLIST", null, null);
      tmp[22] = new SeparatedListStackNode(-1236, 22, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1237, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1238, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[21] = new NonTerminalStackNode(-1235, 21, "$layouts_LAYOUTLIST", null, null);
      tmp[20] = new LiteralStackNode(-1234, 20, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[19] = new NonTerminalStackNode(-1233, 19, "$layouts_LAYOUTLIST", null, null);
      tmp[18] = new LiteralStackNode(-1232, 18, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new char[] {101,108,115,101}, null, null);
      tmp[17] = new NonTerminalStackNode(-1231, 17, "$layouts_LAYOUTLIST", null, null);
      tmp[16] = new LiteralStackNode(-1230, 16, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(-1229, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(-1226, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1227, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1228, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(-1225, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(-1224, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(-1223, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-1220, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1221, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1222, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(-1219, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-1218, 8, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(-1217, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1216, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-1215, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1210, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1211, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1212, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1213, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1214, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1209, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1208, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-1207, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1206, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new char[] {105,102}, null, null);
      builder.addAlternative(RascalRascal.prod__IfThenElse_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_thenString_$StringMiddle_$layouts_LAYOUTLIST_postStatsThen_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit_else_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_elseString_$StringMiddle_$layouts_LAYOUTLIST_postStatsElse_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__For_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode(-1205, 16, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(-1204, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(-1201, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1202, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1203, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(-1200, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(-1199, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(-1198, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-1195, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1196, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1197, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(-1194, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-1193, 8, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(-1192, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1191, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-1190, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1185, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1186, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1187, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1188, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1189, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1184, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1183, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-1182, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1181, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new char[] {102,111,114}, null, null);
      builder.addAlternative(RascalRascal.prod__For_$StringTemplate__lit_for_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__IfThen_$StringTemplate__lit_if_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_preStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_body_$StringMiddle_$layouts_LAYOUTLIST_postStats_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode(-1271, 16, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode(-1270, 15, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode(-1267, 14, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1268, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1269, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode(-1266, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode(-1265, 12, "$StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode(-1264, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-1261, 10, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1262, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1263, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode(-1260, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-1259, 8, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode(-1258, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1257, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-1256, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1251, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1252, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1253, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1254, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1255, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1250, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1249, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-1248, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1247, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new char[] {105,102}, null, null);
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
      
      tmp[1] = new ListStackNode(-1273, 1, regular__iter_star__char_class___range__48_57, new CharStackNode(-1274, 0, new char[][]{{48,57}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode(-1272, 0, new char[][]{{49,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$DecimalIntegerLiteral__char_class___range__49_57_conditional__iter_star__char_class___range__48_57__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__$DecimalIntegerLiteral__conditional__lit_0__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1275, 0, prod__lit_0__char_class___range__48_48_, new char[] {48}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
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
	
  protected static class $Body {
    public final static AbstractStackNode[] EXPECTS;
    static{
      ExpectBuilder builder = new ExpectBuilder(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Toplevels_$Body__toplevels_iter_star_seps__$Toplevel__$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode(-1285, 0, regular__iter_star_seps__$Toplevel__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1286, 0, "$Toplevel", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1287, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
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
    protected static final void _init_prod__$RascalKeywords__lit_filter_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1490, 0, prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new char[] {102,105,108,116,101,114}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_filter_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_datetime_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1491, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new char[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_datetime_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_while_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1494, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new char[] {119,104,105,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_while_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_case_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1493, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new char[] {99,97,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_case_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_layout_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1492, 0, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new char[] {108,97,121,111,117,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_layout_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_num_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1495, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new char[] {110,117,109}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_num_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_set_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1496, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new char[] {115,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_set_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_bag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1497, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new char[] {98,97,103}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_bag_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1498, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new char[] {97,115,115,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_assoc_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_for_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1499, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new char[] {102,111,114}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_for_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_bracket_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1500, 0, prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_, new char[] {98,114,97,99,107,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_bracket_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_fun_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1501, 0, prod__lit_fun__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_, new char[] {102,117,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_fun_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_visit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1502, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new char[] {118,105,115,105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_visit_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_return_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1507, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new char[] {114,101,116,117,114,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_return_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_else_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1506, 0, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new char[] {101,108,115,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_else_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_in_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1505, 0, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new char[] {105,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_in_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_it_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1504, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new char[] {105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_it_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_join_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1503, 0, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new char[] {106,111,105,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_join_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_if_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1508, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new char[] {105,102}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_if_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_non_assoc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1509, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new char[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_repeat_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1510, 0, prod__lit_repeat__char_class___range__114_114_char_class___range__101_101_char_class___range__112_112_char_class___range__101_101_char_class___range__97_97_char_class___range__116_116_, new char[] {114,101,112,101,97,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_repeat_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_one_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1511, 0, prod__lit_one__char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_, new char[] {111,110,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_one_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_str_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1512, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new char[] {115,116,114}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_str_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_node_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1513, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new char[] {110,111,100,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_node_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_catch_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1514, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new char[] {99,97,116,99,104}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_catch_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1516, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new char[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_type_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_notin_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1515, 0, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new char[] {110,111,116,105,110}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_notin_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_append_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1517, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new char[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_append_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_extend_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1518, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new char[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_extend_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_data_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1519, 0, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new char[] {100,97,116,97}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_data_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_tag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1520, 0, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new char[] {116,97,103}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_tag_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_dynamic_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1521, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new char[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_dynamic_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_list_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1522, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new char[] {108,105,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_list_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_non_terminal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1523, 0, prod__lit_non_terminal__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_, new char[] {110,111,110,45,116,101,114,109,105,110,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_non_terminal_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_value_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1524, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new char[] {118,97,108,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_value_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__$BasicType_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1525, 0, "$BasicType", null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__$BasicType_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_try_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1526, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new char[] {116,114,121}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_try_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_void_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1527, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new char[] {118,111,105,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_void_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_public_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1528, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new char[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_public_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_anno_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1529, 0, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new char[] {97,110,110,111}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_anno_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_insert_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1530, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new char[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_insert_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_loc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1532, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new char[] {108,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_loc_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_assert_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1531, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new char[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_assert_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_import_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1533, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new char[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_import_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_start_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1534, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new char[] {115,116,97,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_start_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_test_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1535, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new char[] {116,101,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_test_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_map_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1536, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new char[] {109,97,112}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_map_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_true_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1537, 0, prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_, new char[] {116,114,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_true_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_private_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1538, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new char[] {112,114,105,118,97,116,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_private_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_module_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1539, 0, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {109,111,100,117,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_module_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_throws_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1540, 0, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new char[] {116,104,114,111,119,115}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_throws_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_default_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1541, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new char[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_default_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_alias_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1542, 0, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new char[] {97,108,105,97,115}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_alias_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_throw_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1543, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new char[] {116,104,114,111,119}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_throw_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_rule_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1544, 0, prod__lit_rule__char_class___range__114_114_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {114,117,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_rule_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_switch_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1545, 0, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new char[] {115,119,105,116,99,104}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_switch_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_fail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1546, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new char[] {102,97,105,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_fail_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_int_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1547, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new char[] {105,110,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_int_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_constructor_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1548, 0, prod__lit_constructor__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_, new char[] {99,111,110,115,116,114,117,99,116,111,114}, null, null);
      builder.addAlternative(RascalRascal.prod__$RascalKeywords__lit_constructor_, tmp);
	}
    protected static final void _init_prod__$RascalKeywords__lit_tuple_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1549, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new char[] {116,117,112,108,101}, null, null);
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
      
        _init_prod__$RascalKeywords__lit_append_(builder);
      
        _init_prod__$RascalKeywords__lit_extend_(builder);
      
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
      
      tmp[2] = new LiteralStackNode(-1553, 2, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode(-1552, 1, "$Name", null, null);
      tmp[0] = new LiteralStackNode(-1551, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__$NamedRegExp__lit___60_$Name_lit___62_, tmp);
	}
    protected static final void _init_prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-1555, 1, new char[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode(-1554, 0, new char[][]{{92,92}}, null, null);
      builder.addAlternative(RascalRascal.prod__$NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__$NamedRegExp__$NamedBackslash_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1556, 0, "$NamedBackslash", null, null);
      builder.addAlternative(RascalRascal.prod__$NamedRegExp__$NamedBackslash_, tmp);
	}
    protected static final void _init_prod__$NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_65535_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode(-1557, 0, new char[][]{{0,46},{48,59},{61,61},{63,91},{93,65535}}, null, null);
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
      
      tmp[3] = new NonTerminalStackNode(-1574, 3, "$RegExpModifier", null, null);
      tmp[2] = new LiteralStackNode(-1573, 2, prod__lit___47__char_class___range__47_47_, new char[] {47}, null, null);
      tmp[1] = new ListStackNode(-1571, 1, regular__iter_star__$RegExp, new NonTerminalStackNode(-1572, 0, "$RegExp", null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-1570, 0, prod__lit___47__char_class___range__47_47_, new char[] {47}, null, null);
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
      
      tmp[3] = new CharStackNode(-1612, 3, new char[][]{{48,55}}, null, null);
      tmp[2] = new CharStackNode(-1611, 2, new char[][]{{48,55}}, null, null);
      tmp[1] = new CharStackNode(-1610, 1, new char[][]{{48,51}}, null, null);
      tmp[0] = new LiteralStackNode(-1609, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__$OctalEscapeSequence__lit___92_char_class___range__48_51_char_class___range__48_55_char_class___range__48_55_, tmp);
	}
    protected static final void _init_prod__$OctalEscapeSequence__lit___92_char_class___range__48_55_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode(-1615, 2, new char[][]{{48,55}}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,55}})});
      tmp[1] = new CharStackNode(-1614, 1, new char[][]{{48,55}}, null, null);
      tmp[0] = new LiteralStackNode(-1613, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      builder.addAlternative(RascalRascal.prod__$OctalEscapeSequence__lit___92_char_class___range__48_55_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_, tmp);
	}
    protected static final void _init_prod__$OctalEscapeSequence__lit___92_conditional__char_class___range__48_55__not_follow__char_class___range__48_55_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode(-1617, 1, new char[][]{{48,55}}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,55}})});
      tmp[0] = new LiteralStackNode(-1616, 0, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
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
      
      tmp[2] = new ListStackNode(-1624, 2, regular__iter__char_class___range__48_57_range__65_70_range__97_102, new CharStackNode(-1625, 0, new char[][]{{48,57},{65,70},{97,102}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode(-1623, 1, new char[][]{{88,88},{120,120}}, null, null);
      tmp[0] = new CharStackNode(-1622, 0, new char[][]{{48,48}}, null, null);
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
      
      tmp[2] = new CharStackNode(-1621, 2, new char[][]{{34,34}}, null, null);
      tmp[1] = new ListStackNode(-1619, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode(-1620, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode(-1618, 0, new char[][]{{62,62}}, null, null);
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
      
      tmp[4] = new LiteralStackNode(-1636, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-1635, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-1630, 2, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1631, 0, "$Type", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1632, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1633, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1634, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-1629, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-1628, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
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
      
      tmp[1] = new CharStackNode(-1638, 1, new char[][]{{124,124}}, null, null);
      tmp[0] = new NonTerminalStackNode(-1637, 0, "$URLChars", null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-1644, 0, "$JustTime", null, null);
      builder.addAlternative(RascalRascal.prod__TimeLiteral_$DateTimeLiteral__time_$JustTime_, tmp);
	}
    protected static final void _init_prod__DateLiteral_$DateTimeLiteral__date_$JustDate_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1645, 0, "$JustDate", null, null);
      builder.addAlternative(RascalRascal.prod__DateLiteral_$DateTimeLiteral__date_$JustDate_, tmp);
	}
    protected static final void _init_prod__DateAndTimeLiteral_$DateTimeLiteral__dateAndTime_$DateAndTime_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1646, 0, "$DateAndTime", null, null);
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
      
      tmp[0] = new LiteralStackNode(-1658, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new char[] {115,101,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Set_$BasicType__lit_set_, tmp);
	}
    protected static final void _init_prod__DateTime_$BasicType__lit_datetime_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1659, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new char[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(RascalRascal.prod__DateTime_$BasicType__lit_datetime_, tmp);
	}
    protected static final void _init_prod__String_$BasicType__lit_str_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1660, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new char[] {115,116,114}, null, null);
      builder.addAlternative(RascalRascal.prod__String_$BasicType__lit_str_, tmp);
	}
    protected static final void _init_prod__ReifiedReifiedType_$BasicType__lit_reified_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1661, 0, prod__lit_reified__char_class___range__114_114_char_class___range__101_101_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__100_100_, new char[] {114,101,105,102,105,101,100}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedReifiedType_$BasicType__lit_reified_, tmp);
	}
    protected static final void _init_prod__ReifiedType_$BasicType__lit_type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1662, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new char[] {116,121,112,101}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedType_$BasicType__lit_type_, tmp);
	}
    protected static final void _init_prod__ReifiedTypeParameter_$BasicType__lit_parameter_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1663, 0, prod__lit_parameter__char_class___range__112_112_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new char[] {112,97,114,97,109,101,116,101,114}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedTypeParameter_$BasicType__lit_parameter_, tmp);
	}
    protected static final void _init_prod__Bag_$BasicType__lit_bag_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1664, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new char[] {98,97,103}, null, null);
      builder.addAlternative(RascalRascal.prod__Bag_$BasicType__lit_bag_, tmp);
	}
    protected static final void _init_prod__Real_$BasicType__lit_real_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1665, 0, prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_, new char[] {114,101,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__Real_$BasicType__lit_real_, tmp);
	}
    protected static final void _init_prod__Node_$BasicType__lit_node_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1666, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new char[] {110,111,100,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Node_$BasicType__lit_node_, tmp);
	}
    protected static final void _init_prod__Int_$BasicType__lit_int_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1667, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new char[] {105,110,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Int_$BasicType__lit_int_, tmp);
	}
    protected static final void _init_prod__Map_$BasicType__lit_map_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1668, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new char[] {109,97,112}, null, null);
      builder.addAlternative(RascalRascal.prod__Map_$BasicType__lit_map_, tmp);
	}
    protected static final void _init_prod__Tuple_$BasicType__lit_tuple_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1669, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new char[] {116,117,112,108,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Tuple_$BasicType__lit_tuple_, tmp);
	}
    protected static final void _init_prod__ReifiedFunction_$BasicType__lit_fun_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1670, 0, prod__lit_fun__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_, new char[] {102,117,110}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedFunction_$BasicType__lit_fun_, tmp);
	}
    protected static final void _init_prod__Loc_$BasicType__lit_loc_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1674, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new char[] {108,111,99}, null, null);
      builder.addAlternative(RascalRascal.prod__Loc_$BasicType__lit_loc_, tmp);
	}
    protected static final void _init_prod__Value_$BasicType__lit_value_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1675, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new char[] {118,97,108,117,101}, null, null);
      builder.addAlternative(RascalRascal.prod__Value_$BasicType__lit_value_, tmp);
	}
    protected static final void _init_prod__List_$BasicType__lit_list_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1671, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new char[] {108,105,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__List_$BasicType__lit_list_, tmp);
	}
    protected static final void _init_prod__ReifiedNonTerminal_$BasicType__lit_non_terminal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1672, 0, prod__lit_non_terminal__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_, new char[] {110,111,110,45,116,101,114,109,105,110,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedNonTerminal_$BasicType__lit_non_terminal_, tmp);
	}
    protected static final void _init_prod__Num_$BasicType__lit_num_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1673, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new char[] {110,117,109}, null, null);
      builder.addAlternative(RascalRascal.prod__Num_$BasicType__lit_num_, tmp);
	}
    protected static final void _init_prod__Relation_$BasicType__lit_rel_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1676, 0, prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new char[] {114,101,108}, null, null);
      builder.addAlternative(RascalRascal.prod__Relation_$BasicType__lit_rel_, tmp);
	}
    protected static final void _init_prod__Void_$BasicType__lit_void_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1677, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new char[] {118,111,105,100}, null, null);
      builder.addAlternative(RascalRascal.prod__Void_$BasicType__lit_void_, tmp);
	}
    protected static final void _init_prod__ReifiedConstructor_$BasicType__lit_constructor_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1678, 0, prod__lit_constructor__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_, new char[] {99,111,110,115,116,114,117,99,116,111,114}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedConstructor_$BasicType__lit_constructor_, tmp);
	}
    protected static final void _init_prod__Bool_$BasicType__lit_bool_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1679, 0, prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_, new char[] {98,111,111,108}, null, null);
      builder.addAlternative(RascalRascal.prod__Bool_$BasicType__lit_bool_, tmp);
	}
    protected static final void _init_prod__ReifiedAdt_$BasicType__lit_adt_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1680, 0, prod__lit_adt__char_class___range__97_97_char_class___range__100_100_char_class___range__116_116_, new char[] {97,100,116}, null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-1711, 0, "$Tags", null, null);
      tmp[1] = new NonTerminalStackNode(-1712, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1713, 2, "$Visibility", null, null);
      tmp[3] = new NonTerminalStackNode(-1714, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1715, 4, "$Signature", null, null);
      tmp[5] = new NonTerminalStackNode(-1716, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1717, 6, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(-1718, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-1719, 8, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode(-1720, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-1721, 10, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new char[] {119,104,101,110}, null, null);
      tmp[11] = new NonTerminalStackNode(-1722, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode(-1723, 12, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1724, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1725, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1726, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1727, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[13] = new NonTerminalStackNode(-1728, 13, "$layouts_LAYOUTLIST", null, null);
      tmp[14] = new LiteralStackNode(-1729, 14, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      builder.addAlternative(RascalRascal.prod__Conditional_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit_when_$layouts_LAYOUTLIST_conditions_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode(-1710, 10, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode(-1709, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-1708, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode(-1707, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-1706, 6, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode(-1705, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1704, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode(-1703, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1702, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-1701, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1700, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Expression_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-1736, 6, prod__lit___59__char_class___range__59_59_, new char[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode(-1735, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1734, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode(-1733, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1732, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-1731, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1730, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Abstract_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Default_$FunctionDeclaration__tags_$Tags_$layouts_LAYOUTLIST_visibility_$Visibility_$layouts_LAYOUTLIST_signature_$Signature_$layouts_LAYOUTLIST_body_$FunctionBody__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-1743, 6, "$FunctionBody", null, null);
      tmp[5] = new NonTerminalStackNode(-1742, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-1741, 4, "$Signature", null, null);
      tmp[3] = new NonTerminalStackNode(-1740, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1739, 2, "$Visibility", null, null);
      tmp[1] = new NonTerminalStackNode(-1738, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1737, 0, "$Tags", null, null);
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
      
      tmp[6] = new LiteralStackNode(-1758, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-1757, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1752, 4, regular__iter_star_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1753, 0, "$TypeArg", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1754, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1755, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1756, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-1751, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1750, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-1749, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1748, 0, "$Name", null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-1764, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(-1763, 1, "$PreModule", null, null);
      tmp[0] = new NonTerminalStackNode(-1762, 0, "$layouts_LAYOUTLIST", null, null);
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
      
      tmp[2] = new SeparatedListStackNode(-1788, 2, regular__iter_seps__$Variable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1789, 0, "$Variable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1790, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1791, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1792, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-1787, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1786, 0, "$Type", null, null);
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
      
      tmp[6] = new LiteralStackNode(-1838, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(-1837, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-1832, 4, regular__iter_seps__$TypeArg__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1833, 0, "$TypeArg", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1834, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1835, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1836, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-1831, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1830, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-1829, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1828, 0, "$BasicType", null, null);
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
      
      tmp[8] = new OptionalStackNode(-1850, 8, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(-1851, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(-1852, 0, new char[][]{{44,44},{46,46}}, null, null), new CharStackNode(-1853, 1, new char[][]{{48,57}}, null, null), new OptionalStackNode(-1854, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(-1855, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(-1856, 0, new char[][]{{48,57}}, null, null), new OptionalStackNode(-1857, 1, regular__opt__char_class___range__48_57, new CharStackNode(-1858, 0, new char[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[7] = new CharStackNode(-1849, 7, new char[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode(-1848, 6, new char[][]{{48,53}}, null, null);
      tmp[5] = new LiteralStackNode(-1847, 5, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[4] = new CharStackNode(-1846, 4, new char[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode(-1845, 3, new char[][]{{48,53}}, null, null);
      tmp[2] = new LiteralStackNode(-1844, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new CharStackNode(-1843, 1, new char[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(-1842, 0, new char[][]{{48,50}}, null, null);
      builder.addAlternative(RascalRascal.prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new OptionalStackNode(-1865, 6, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(-1866, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(-1867, 0, new char[][]{{44,44},{46,46}}, null, null), new CharStackNode(-1868, 1, new char[][]{{48,57}}, null, null), new OptionalStackNode(-1869, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode(-1870, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, new AbstractStackNode[]{new CharStackNode(-1871, 0, new char[][]{{48,57}}, null, null), new OptionalStackNode(-1872, 1, regular__opt__char_class___range__48_57, new CharStackNode(-1873, 0, new char[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[5] = new CharStackNode(-1864, 5, new char[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode(-1863, 4, new char[][]{{48,53}}, null, null);
      tmp[3] = new CharStackNode(-1862, 3, new char[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode(-1861, 2, new char[][]{{48,53}}, null, null);
      tmp[1] = new CharStackNode(-1860, 1, new char[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(-1859, 0, new char[][]{{48,50}}, null, null);
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
      
      tmp[0] = new SequenceStackNode(-1881, 0, regular__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new AbstractStackNode[]{new CharStackNode(-1882, 0, new char[][]{{65,90},{95,95},{97,122}}, new IEnterFilter[] {new CharPrecedeRestriction(new char[][]{{65,90},{95,95},{97,122}})}, null), new ListStackNode(-1883, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(-1884, 0, new char[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})})}, null, new ICompletionFilter[] {new StringMatchRestriction(new char[] {105,109,112,111,114,116}), new StringMatchRestriction(new char[] {105,110}), new StringMatchRestriction(new char[] {97,108,108}), new StringMatchRestriction(new char[] {102,97,108,115,101}), new StringMatchRestriction(new char[] {97,110,110,111}), new StringMatchRestriction(new char[] {98,114,97,99,107,101,116}), new StringMatchRestriction(new char[] {108,97,121,111,117,116}), new StringMatchRestriction(new char[] {106,111,105,110}), new StringMatchRestriction(new char[] {100,97,116,97}), new StringMatchRestriction(new char[] {105,116}), new StringMatchRestriction(new char[] {115,119,105,116,99,104}), new StringMatchRestriction(new char[] {114,101,116,117,114,110}), new StringMatchRestriction(new char[] {99,97,115,101}), new StringMatchRestriction(new char[] {115,116,114}), new StringMatchRestriction(new char[] {119,104,105,108,101}), new StringMatchRestriction(new char[] {100,121,110,97,109,105,99}), new StringMatchRestriction(new char[] {115,111,108,118,101}), new StringMatchRestriction(new char[] {110,111,116,105,110}), new StringMatchRestriction(new char[] {105,110,115,101,114,116}), new StringMatchRestriction(new char[] {101,108,115,101}), new StringMatchRestriction(new char[] {116,121,112,101}), new StringMatchRestriction(new char[] {99,97,116,99,104}), new StringMatchRestriction(new char[] {116,114,121}), new StringMatchRestriction(new char[] {110,117,109}), new StringMatchRestriction(new char[] {110,111,100,101}), new StringMatchRestriction(new char[] {112,114,105,118,97,116,101}), new StringMatchRestriction(new char[] {102,105,110,97,108,108,121}), new StringMatchRestriction(new char[] {116,114,117,101}), new StringMatchRestriction(new char[] {98,97,103}), new StringMatchRestriction(new char[] {118,111,105,100}), new StringMatchRestriction(new char[] {110,111,110,45,97,115,115,111,99}), new StringMatchRestriction(new char[] {97,115,115,111,99}), new StringMatchRestriction(new char[] {116,101,115,116}), new StringMatchRestriction(new char[] {105,102}), new StringMatchRestriction(new char[] {102,97,105,108}), new StringMatchRestriction(new char[] {108,105,115,116}), new StringMatchRestriction(new char[] {114,101,97,108}), new StringMatchRestriction(new char[] {114,101,108}), new StringMatchRestriction(new char[] {116,97,103}), new StringMatchRestriction(new char[] {101,120,116,101,110,100}), new StringMatchRestriction(new char[] {97,112,112,101,110,100}), new StringMatchRestriction(new char[] {114,101,112,101,97,116}), new StringMatchRestriction(new char[] {116,104,114,111,119}), new StringMatchRestriction(new char[] {111,110,101}), new StringMatchRestriction(new char[] {115,116,97,114,116}), new StringMatchRestriction(new char[] {115,101,116}), new StringMatchRestriction(new char[] {109,111,100,117,108,101}), new StringMatchRestriction(new char[] {97,110,121}), new StringMatchRestriction(new char[] {105,110,116}), new StringMatchRestriction(new char[] {112,117,98,108,105,99}), new StringMatchRestriction(new char[] {98,111,111,108}), new StringMatchRestriction(new char[] {118,97,108,117,101}), new StringMatchRestriction(new char[] {114,117,108,101}), new StringMatchRestriction(new char[] {110,111,110,45,116,101,114,109,105,110,97,108}), new StringMatchRestriction(new char[] {102,117,110}), new StringMatchRestriction(new char[] {102,105,108,116,101,114}), new StringMatchRestriction(new char[] {99,111,110,115,116,114,117,99,116,111,114}), new StringMatchRestriction(new char[] {100,97,116,101,116,105,109,101}), new StringMatchRestriction(new char[] {97,115,115,101,114,116}), new StringMatchRestriction(new char[] {108,111,99}), new StringMatchRestriction(new char[] {100,101,102,97,117,108,116}), new StringMatchRestriction(new char[] {116,104,114,111,119,115}), new StringMatchRestriction(new char[] {116,117,112,108,101}), new StringMatchRestriction(new char[] {102,111,114}), new StringMatchRestriction(new char[] {118,105,115,105,116}), new StringMatchRestriction(new char[] {97,108,105,97,115}), new StringMatchRestriction(new char[] {109,97,112})});
      builder.addAlternative(RascalRascal.prod__$Name__conditional__seq___conditional__char_class___range__65_90_range__95_95_range__97_122__not_precede__char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__48_57_range__65_90_range__95_95_range__97_122__delete__$RascalKeywords_, tmp);
	}
    protected static final void _init_prod__$Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_conditional__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122__not_follow__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new ListStackNode(-1887, 2, regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(-1888, 0, new char[][]{{45,45},{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{45,45},{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode(-1886, 1, new char[][]{{65,90},{95,95},{97,122}}, null, null);
      tmp[0] = new CharStackNode(-1885, 0, new char[][]{{92,92}}, null, null);
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
      
      tmp[0] = new EpsilonStackNode(-1890, 0);
      builder.addAlternative(RascalRascal.prod__Absent_$Start__, tmp);
	}
    protected static final void _init_prod__Present_$Start__lit_start_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-1891, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new char[] {115,116,97,114,116}, null, null);
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
      
      tmp[0] = new SeparatedListStackNode(-1894, 0, regular__iter_star_seps__$Pattern__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-1895, 0, "$Pattern", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1896, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-1897, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-1898, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
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
      
      tmp[0] = new SeparatedListStackNode(-1899, 0, regular__iter_star_seps__$Tag__$layouts_LAYOUTLIST, new NonTerminalStackNode(-1900, 0, "$Tag", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-1901, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-1904, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(-1903, 1, "$Commands", null, null);
      tmp[0] = new NonTerminalStackNode(-1902, 0, "$layouts_LAYOUTLIST", null, null);
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
      
      tmp[2] = new LiteralStackNode(-1908, 2, prod__lit___39__char_class___range__39_39_, new char[] {39}, null, null);
      tmp[1] = new ListStackNode(-1906, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode(-1907, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-1905, 0, prod__lit___39__char_class___range__39_39_, new char[] {39}, null, null);
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
      
      tmp[0] = new CharStackNode(-1909, 0, new char[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{47,47},{60,60},{62,62},{92,92}})});
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
      
      tmp[1] = new NonTerminalStackNode(-1932, 1, "$DatePart", null, null);
      tmp[0] = new LiteralStackNode(-1931, 0, prod__lit___36__char_class___range__36_36_, new char[] {36}, null, null);
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
      
      tmp[4] = new NonTerminalStackNode(-1942, 4, "$PathTail", null, null);
      tmp[3] = new NonTerminalStackNode(-1941, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1940, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-1939, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1938, 0, "$MidPathChars", null, null);
      builder.addAlternative(RascalRascal.prod__Mid_$PathTail__mid_$MidPathChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$PathTail_, tmp);
	}
    protected static final void _init_prod__Post_$PathTail__post_$PostPathChars_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-1943, 0, "$PostPathChars", null, null);
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
      
      tmp[2] = new LiteralStackNode(-1946, 2, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(-1945, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode(-1944, 0, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
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
      
      tmp[4] = new NonTerminalStackNode(-1958, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-1957, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1956, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-1955, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1954, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__Arbitrary_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_statement_$Statement_, tmp);
	}
    protected static final void _init_prod__Replacing_$PatternWithAction__pattern_$Pattern_$layouts_LAYOUTLIST_lit___61_62_$layouts_LAYOUTLIST_replacement_$Replacement_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-1963, 4, "$Replacement", null, null);
      tmp[3] = new NonTerminalStackNode(-1962, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1961, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new char[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-1960, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1959, 0, "$Pattern", null, null);
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
      
      tmp[4] = new NonTerminalStackNode(-1968, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-1967, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-1966, 2, prod__lit___46__char_class___range__46_46_, new char[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode(-1965, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1964, 0, "$QualifiedName", null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-1969, 0, "$PostStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__Post_$StringTail__post_$PostStringChars_, tmp);
	}
    protected static final void _init_prod__MidTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-1974, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(-1973, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1972, 2, "$StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode(-1971, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1970, 0, "$MidStringChars", null, null);
      builder.addAlternative(RascalRascal.prod__MidTemplate_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_template_$StringTemplate_$layouts_LAYOUTLIST_tail_$StringTail_, tmp);
	}
    protected static final void _init_prod__MidInterpolated_$StringTail__mid_$MidStringChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$StringTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-1979, 4, "$StringTail", null, null);
      tmp[3] = new NonTerminalStackNode(-1978, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-1977, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-1976, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-1975, 0, "$MidStringChars", null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-1982, 2, "$layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode(-1981, 1, "$Module", null, null);
      tmp[0] = new NonTerminalStackNode(-1980, 0, "$layouts_LAYOUTLIST", null, null);
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
      
      tmp[2] = new LiteralStackNode(-2001, 2, prod__lit___34__char_class___range__34_34_, new char[] {34}, null, null);
      tmp[1] = new ListStackNode(-1999, 1, regular__iter_star__$StringCharacter, new NonTerminalStackNode(-2000, 0, "$StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-1998, 0, prod__lit___34__char_class___range__34_34_, new char[] {34}, null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-2018, 2, "$Declarator", null, null);
      tmp[1] = new NonTerminalStackNode(-2017, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2016, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new char[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(RascalRascal.prod__Dynamic_$LocalVariableDeclaration__lit_dynamic_$layouts_LAYOUTLIST_declarator_$Declarator_, tmp);
	}
    protected static final void _init_prod__Default_$LocalVariableDeclaration__declarator_$Declarator_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2019, 0, "$Declarator", null, null);
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
      
      tmp[4] = new LiteralStackNode(-2042, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-2041, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2040, 2, "$Formals", null, null);
      tmp[1] = new NonTerminalStackNode(-2039, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2038, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__VarArgs_$Parameters__lit___40_$layouts_LAYOUTLIST_formals_$Formals_$layouts_LAYOUTLIST_lit___46_46_46_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2049, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-2048, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2047, 4, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new char[] {46,46,46}, null, null);
      tmp[3] = new NonTerminalStackNode(-2046, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2045, 2, "$Formals", null, null);
      tmp[1] = new NonTerminalStackNode(-2044, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2043, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
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
      
      tmp[7] = new CharStackNode(-2057, 7, new char[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode(-2056, 6, new char[][]{{48,51}}, null, null);
      tmp[5] = new CharStackNode(-2055, 5, new char[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode(-2054, 4, new char[][]{{48,49}}, null, null);
      tmp[3] = new CharStackNode(-2053, 3, new char[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode(-2052, 2, new char[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(-2051, 1, new char[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(-2050, 0, new char[][]{{48,57}}, null, null);
      builder.addAlternative(RascalRascal.prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__$DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[10];
      
      tmp[9] = new CharStackNode(-2067, 9, new char[][]{{48,57}}, null, null);
      tmp[8] = new CharStackNode(-2066, 8, new char[][]{{48,51}}, null, null);
      tmp[7] = new LiteralStackNode(-2065, 7, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      tmp[6] = new CharStackNode(-2064, 6, new char[][]{{48,57}}, null, null);
      tmp[5] = new CharStackNode(-2063, 5, new char[][]{{48,49}}, null, null);
      tmp[4] = new LiteralStackNode(-2062, 4, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      tmp[3] = new CharStackNode(-2061, 3, new char[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode(-2060, 2, new char[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode(-2059, 1, new char[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode(-2058, 0, new char[][]{{48,57}}, null, null);
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
      
      tmp[0] = new LiteralStackNode(-2088, 0, prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_, new char[] {108,101,120,105,99,97,108}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_lexical_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_layout_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2089, 0, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new char[] {108,97,121,111,117,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_layout_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_extend_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2090, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new char[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_extend_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_syntax_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2091, 0, prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_, new char[] {115,121,110,116,97,120}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_syntax_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_keyword_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2092, 0, prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_, new char[] {107,101,121,119,111,114,100}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_keyword_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_import_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2093, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new char[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__$HeaderKeyword__lit_import_, tmp);
	}
    protected static final void _init_prod__$HeaderKeyword__lit_start_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2094, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new char[] {115,116,97,114,116}, null, null);
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
      
      tmp[4] = new LiteralStackNode(-2122, 4, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode(-2121, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2116, 2, regular__iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2117, 0, "$Assignable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2118, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2119, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2120, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-2115, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2114, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__Tuple_$Assignable__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__Bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2127, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-2126, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2125, 2, "$Assignable", null, null);
      tmp[1] = new NonTerminalStackNode(-2124, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2123, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Bracket_$Assignable__lit___40_$layouts_LAYOUTLIST_arg_$Assignable_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__Constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2138, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-2137, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2132, 4, regular__iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2133, 0, "$Assignable", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2134, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2135, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2136, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-2131, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2130, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2129, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2128, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Constructor_$Assignable__name_$Name_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_seps__$Assignable__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__FieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[0] = new NonTerminalStackNode(-2139, 0, "$Assignable", null, null);
      tmp[1] = new NonTerminalStackNode(-2140, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2141, 2, prod__lit___46__char_class___range__46_46_, new char[] {46}, null, null);
      tmp[3] = new NonTerminalStackNode(-2142, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2143, 4, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__FieldAccess_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_, tmp);
	}
    protected static final void _init_prod__Variable_$Assignable__qualifiedName_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2154, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__Variable_$Assignable__qualifiedName_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2148, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2147, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2146, 2, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode(-2145, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2144, 0, "$Assignable", null, null);
      builder.addAlternative(RascalRascal.prod__Annotation_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_annotation_$Name_, tmp);
	}
    protected static final void _init_prod__IfDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2153, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2152, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2151, 2, prod__lit___63__char_class___range__63_63_, new char[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(-2150, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2149, 0, "$Assignable", null, null);
      builder.addAlternative(RascalRascal.prod__IfDefinedOrDefault_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_defaultExpression_$Expression_, tmp);
	}
    protected static final void _init_prod__Subscript_$Assignable__receiver_$Assignable_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscript_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2161, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(-2160, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2159, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2158, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2157, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-2156, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2155, 0, "$Assignable", null, null);
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
      
      tmp[0] = new LiteralStackNode(-2162, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new char[] {116,101,115,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Test_$FunctionModifier__lit_test_, tmp);
	}
    protected static final void _init_prod__Default_$FunctionModifier__lit_default_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2163, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new char[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$FunctionModifier__lit_default_, tmp);
	}
    protected static final void _init_prod__Java_$FunctionModifier__lit_java_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2164, 0, prod__lit_java__char_class___range__106_106_char_class___range__97_97_char_class___range__118_118_char_class___range__97_97_, new char[] {106,97,118,97}, null, null);
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
      
      tmp[0] = new LiteralStackNode(-2168, 0, prod__lit___43_61__char_class___range__43_43_char_class___range__61_61_, new char[] {43,61}, null, null);
      builder.addAlternative(RascalRascal.prod__Addition_$Assignment__lit___43_61_, tmp);
	}
    protected static final void _init_prod__Product_$Assignment__lit___42_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2169, 0, prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_, new char[] {42,61}, null, null);
      builder.addAlternative(RascalRascal.prod__Product_$Assignment__lit___42_61_, tmp);
	}
    protected static final void _init_prod__Division_$Assignment__lit___47_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2170, 0, prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_, new char[] {47,61}, null, null);
      builder.addAlternative(RascalRascal.prod__Division_$Assignment__lit___47_61_, tmp);
	}
    protected static final void _init_prod__IfDefined_$Assignment__lit___63_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2171, 0, prod__lit___63_61__char_class___range__63_63_char_class___range__61_61_, new char[] {63,61}, null, null);
      builder.addAlternative(RascalRascal.prod__IfDefined_$Assignment__lit___63_61_, tmp);
	}
    protected static final void _init_prod__Default_$Assignment__lit___61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2172, 0, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Assignment__lit___61_, tmp);
	}
    protected static final void _init_prod__Intersection_$Assignment__lit___38_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2173, 0, prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_, new char[] {38,61}, null, null);
      builder.addAlternative(RascalRascal.prod__Intersection_$Assignment__lit___38_61_, tmp);
	}
    protected static final void _init_prod__Subtraction_$Assignment__lit___45_61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2174, 0, prod__lit___45_61__char_class___range__45_45_char_class___range__61_61_, new char[] {45,61}, null, null);
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
      
      tmp[2] = new LiteralStackNode(-2167, 2, prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_, new char[] {58,47,47}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{9,10},{13,13},{32,32}})});
      tmp[1] = new NonTerminalStackNode(-2166, 1, "$URLChars", null, null);
      tmp[0] = new CharStackNode(-2165, 0, new char[][]{{124,124}}, null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-2184, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Name_$Field__fieldName_$Name_, tmp);
	}
    protected static final void _init_prod__Index_$Field__fieldIndex_$IntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2185, 0, "$IntegerLiteral", null, null);
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
      
      tmp[2] = new CharStackNode(-2204, 2, new char[][]{{10,10}}, null, null);
      tmp[1] = new ListStackNode(-2202, 1, regular__iter_star__char_class___range__0_9_range__11_65535, new CharStackNode(-2203, 0, new char[][]{{0,9},{11,65535}}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-2201, 0, prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_, new char[] {47,47}, null, null);
      builder.addAlternative(RascalRascal.prod__$Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10__tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__$Comment__lit___47_42_iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535_lit___42_47__tag__category___67_111_109_109_101_110_116(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2210, 2, prod__lit___42_47__char_class___range__42_42_char_class___range__47_47_, new char[] {42,47}, null, null);
      tmp[1] = new ListStackNode(-2206, 1, regular__iter_star__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535, new AlternativeStackNode(-2207, 0, regular__alt___conditional__char_class___range__42_42__not_follow__char_class___range__47_47_char_class___range__0_41_range__43_65535, new AbstractStackNode[]{new CharStackNode(-2208, 0, new char[][]{{42,42}}, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{47,47}})}), new CharStackNode(-2209, 0, new char[][]{{0,41},{43,65535}}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-2205, 0, prod__lit___47_42__char_class___range__47_47_char_class___range__42_42_, new char[] {47,42}, null, null);
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
      
      tmp[2] = new LiteralStackNode(-2223, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-2222, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2221, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Label__name_$Name_$layouts_LAYOUTLIST_lit___58_, tmp);
	}
    protected static final void _init_prod__Empty_$Label__(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode(-2224, 0);
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
      
      tmp[0] = new NonTerminalStackNode(-2225, 0, "$ProtocolChars", null, null);
      builder.addAlternative(RascalRascal.prod__NonInterpolated_$ProtocolPart__protocolChars_$ProtocolChars_, tmp);
	}
    protected static final void _init_prod__Interpolated_$ProtocolPart__pre_$PreProtocolChars_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_tail_$ProtocolTail_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2230, 4, "$ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode(-2229, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2228, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2227, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2226, 0, "$PreProtocolChars", null, null);
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
    
    protected static final void _init_prod__Default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_opt__$Rest_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new OptionalStackNode(-2245, 4, regular__opt__$Rest, new NonTerminalStackNode(-2246, 0, "$Rest", null, null), null, null);
      tmp[3] = new NonTerminalStackNode(-2244, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new EmptyStackNode(-2243, 2, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {108,101,120,105,99,97,108}), new StringFollowRestriction(new char[] {105,109,112,111,114,116}), new StringFollowRestriction(new char[] {115,116,97,114,116}), new StringFollowRestriction(new char[] {115,121,110,116,97,120}), new StringFollowRestriction(new char[] {101,120,116,101,110,100}), new StringFollowRestriction(new char[] {108,97,121,111,117,116}), new StringFollowRestriction(new char[] {107,101,121,119,111,114,100})});
      tmp[1] = new NonTerminalStackNode(-2242, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2241, 0, "$Header", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_opt__$Rest_, tmp);
	}
    public static void init(ExpectBuilder builder){
      
      
        _init_prod__Default_$PreModule__header_$Header_$layouts_LAYOUTLIST_conditional__empty__not_follow__$HeaderKeyword_$layouts_LAYOUTLIST_rest_opt__$Rest_(builder);
      
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
      
      tmp[2] = new LiteralStackNode(-2263, 2, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[1] = new ListStackNode(-2257, 1, regular__iter_star__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535, new AlternativeStackNode(-2258, 0, regular__alt___seq___char_class___range__92_92_char_class___range__125_125_char_class___range__0_124_range__126_65535, new AbstractStackNode[]{new SequenceStackNode(-2259, 0, regular__seq___char_class___range__92_92_char_class___range__125_125, new AbstractStackNode[]{new CharStackNode(-2260, 0, new char[][]{{92,92}}, null, null), new CharStackNode(-2261, 1, new char[][]{{125,125}}, null, null)}, null, null), new CharStackNode(-2262, 0, new char[][]{{0,124},{126,65535}}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode(-2256, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
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
      
      tmp[1] = new ListStackNode(-2265, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(-2266, 0, new char[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode(-2264, 0, new char[][]{{65,90}}, new IEnterFilter[] {new CharPrecedeRestriction(new char[][]{{65,90}})}, null);
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
      
      tmp[4] = new NonTerminalStackNode(-2283, 4, "$Pattern", null, null);
      tmp[3] = new NonTerminalStackNode(-2282, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2281, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-2280, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2279, 0, "$Pattern", null, null);
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
      
      tmp[2] = new LiteralStackNode(-2516, 2, prod__lit___43__char_class___range__43_43_, new char[] {43}, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {61})});
      tmp[1] = new NonTerminalStackNode(-2515, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2514, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__TransitiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___43__not_follow__lit___61_, tmp);
	}
    protected static final void _init_prod__IfThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode(-2616, 8, "$Expression", null, null);
      tmp[7] = new NonTerminalStackNode(-2615, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2614, 6, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[5] = new NonTerminalStackNode(-2613, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2612, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2611, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2610, 2, prod__lit___63__char_class___range__63_63_, new char[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(-2609, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2608, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__IfThenElse_$Expression__condition_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_thenExp_$Expression_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_elseExp_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2552, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2551, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2550, 2, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new char[] {106,111,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode(-2549, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2548, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Join_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_join_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__And_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2661, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2660, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2659, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new char[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode(-2658, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2657, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__And_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__In_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2582, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2581, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2580, 2, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new char[] {105,110}, null, null);
      tmp[1] = new NonTerminalStackNode(-2579, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2578, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__In_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_in_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__List_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2318, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-2317, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2312, 2, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2313, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2314, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2315, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2316, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-2311, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2310, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__List_$Expression__lit___91_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2567, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2566, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2565, 2, prod__lit___38__char_class___range__38_38_, new char[] {38}, null, null);
      tmp[1] = new NonTerminalStackNode(-2564, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2563, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Intersection_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___38_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode(-2331, 0, "$Label", null, null);
      tmp[1] = new NonTerminalStackNode(-2332, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2333, 2, "$Visit", null, null);
      builder.addAlternative(RascalRascal.prod__Visit_$Expression__label_$Label_$layouts_LAYOUTLIST_visit_$Visit_, tmp);
	}
    protected static final void _init_prod__All_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2364, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-2363, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2358, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2359, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2360, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2361, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2362, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-2357, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2356, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2355, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2354, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new char[] {97,108,108}, null, null);
      builder.addAlternative(RascalRascal.prod__All_$Expression__lit_all_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__VoidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[0] = new NonTerminalStackNode(-2378, 0, "$Parameters", null, null);
      tmp[1] = new NonTerminalStackNode(-2379, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2380, 2, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[3] = new NonTerminalStackNode(-2381, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2382, 4, regular__iter_star_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2383, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2384, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode(-2385, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2386, 6, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      builder.addAlternative(RascalRascal.prod__VoidClosure_$Expression__parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_star_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__StepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-2377, 12, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[11] = new NonTerminalStackNode(-2376, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-2375, 10, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode(-2374, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-2373, 8, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new char[] {46,46}, null, null);
      tmp[7] = new NonTerminalStackNode(-2372, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2371, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-2370, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2369, 4, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null);
      tmp[3] = new NonTerminalStackNode(-2368, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2367, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2366, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2365, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__StepRange_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_second_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2621, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2620, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2619, 2, prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_, new char[] {61,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2618, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2617, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Equals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2391, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2390, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2389, 2, prod__lit_has__char_class___range__104_104_char_class___range__97_97_char_class___range__115_115_, new char[] {104,97,115}, null, null);
      tmp[1] = new NonTerminalStackNode(-2388, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2387, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Has_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_has_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__FieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[0] = new NonTerminalStackNode(-2392, 0, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2393, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2394, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[3] = new NonTerminalStackNode(-2395, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2396, 4, "$Name", null, null);
      tmp[5] = new NonTerminalStackNode(-2397, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2398, 6, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(-2399, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode(-2400, 8, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode(-2401, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode(-2402, 10, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      builder.addAlternative(RascalRascal.prod__FieldUpdate_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_key_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_replacement_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2433, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-2432, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2427, 2, regular__iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2428, 0, "$Mapping__$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2429, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2430, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2431, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-2426, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2425, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Map_$Expression__lit___40_$layouts_LAYOUTLIST_mappings_iter_star_seps__$Mapping__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__ReifyType_$Expression__lit___35_$layouts_LAYOUTLIST_type_$Type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2436, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-2435, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2434, 0, prod__lit___35__char_class___range__35_35_, new char[] {35}, null, null);
      builder.addAlternative(RascalRascal.prod__ReifyType_$Expression__lit___35_$layouts_LAYOUTLIST_type_$Type_, tmp);
	}
    protected static final void _init_prod__Is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2441, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2440, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2439, 2, prod__lit_is__char_class___range__105_105_char_class___range__115_115_, new char[] {105,115}, null, null);
      tmp[1] = new NonTerminalStackNode(-2438, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2437, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Is_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit_is_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__Reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-2459, 12, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode(-2458, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode(-2453, 10, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2454, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2455, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2456, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2457, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(-2452, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-2451, 8, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode(-2450, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2449, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-2448, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2447, 4, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(-2446, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2445, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2444, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2443, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Reducer_$Expression__lit___40_$layouts_LAYOUTLIST_init_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_result_$Expression_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2666, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2665, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2664, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new char[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode(-2663, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2662, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Or_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___124_124_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2497, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-2496, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2491, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2492, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2493, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2494, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2495, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-2490, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2489, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2488, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2487, 0, prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_, new char[] {97,110,121}, null, null);
      builder.addAlternative(RascalRascal.prod__Any_$Expression__lit_any_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_generators_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__LessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2607, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2606, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2605, 2, prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_, new char[] {60,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2604, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2603, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__LessThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__SetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode(-2537, 12, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[11] = new NonTerminalStackNode(-2536, 11, "$layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode(-2535, 10, "$Expression", null, null);
      tmp[9] = new NonTerminalStackNode(-2534, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-2533, 8, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode(-2532, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2531, 6, "$Name", null, null);
      tmp[5] = new NonTerminalStackNode(-2530, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2529, 4, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      tmp[3] = new NonTerminalStackNode(-2528, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2527, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-2526, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2525, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__SetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_value_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-2297, 8, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[7] = new NonTerminalStackNode(-2296, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2295, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-2294, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2293, 4, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new char[] {46,46}, null, null);
      tmp[3] = new NonTerminalStackNode(-2292, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2291, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2290, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2289, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__Range_$Expression__lit___91_$layouts_LAYOUTLIST_first_$Expression_$layouts_LAYOUTLIST_lit___46_46_$layouts_LAYOUTLIST_last_$Expression_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2547, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2546, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2545, 2, prod__lit___42__char_class___range__42_42_, new char[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode(-2544, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2543, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Product_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___42_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__TransitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2519, 2, prod__lit___42__char_class___range__42_42_, new char[] {42}, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {61})});
      tmp[1] = new NonTerminalStackNode(-2518, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2517, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__TransitiveReflexiveClosure_$Expression__argument_$Expression_$layouts_LAYOUTLIST_conditional__lit___42__not_follow__lit___61_, tmp);
	}
    protected static final void _init_prod__Division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2557, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2556, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2555, 2, prod__lit___47__char_class___range__47_47_, new char[] {47}, null, null);
      tmp[1] = new NonTerminalStackNode(-2554, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2553, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Division_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___47_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Literal_$Expression__literal_$Literal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2298, 0, "$Literal", null, null);
      builder.addAlternative(RascalRascal.prod__Literal_$Expression__literal_$Literal_, tmp);
	}
    protected static final void _init_prod__Negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2510, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2509, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2508, 0, prod__lit___33__char_class___range__33_33_, new char[] {33}, null, null);
      builder.addAlternative(RascalRascal.prod__Negation_$Expression__lit___33_$layouts_LAYOUTLIST_argument_$Expression_, tmp);
	}
    protected static final void _init_prod__ReifiedType_$Expression__basicType_$BasicType_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2309, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode(-2308, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2303, 4, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2304, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2305, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2306, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2307, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode(-2302, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2301, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2300, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2299, 0, "$BasicType", null, null);
      builder.addAlternative(RascalRascal.prod__ReifiedType_$Expression__basicType_$BasicType_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__CallOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[0] = new NonTerminalStackNode(-2319, 0, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2320, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2321, 2, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode(-2322, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2323, 4, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2324, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2325, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2326, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2327, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode(-2328, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2329, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      builder.addAlternative(RascalRascal.prod__CallOrTree_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___40_$layouts_LAYOUTLIST_arguments_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Comprehension_$Expression__comprehension_$Comprehension_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2330, 0, "$Comprehension", null, null);
      builder.addAlternative(RascalRascal.prod__Comprehension_$Expression__comprehension_$Comprehension_, tmp);
	}
    protected static final void _init_prod__Subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2344, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(-2343, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2338, 4, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2339, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2340, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2341, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2342, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-2337, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2336, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-2335, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2334, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Subscript_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_subscripts_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2353, 4, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode(-2352, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2347, 2, regular__iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2348, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2349, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2350, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2351, 3, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode(-2346, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2345, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__Set_$Expression__lit___123_$layouts_LAYOUTLIST_elements_iter_star_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__LessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2602, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2601, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2600, 2, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {45})});
      tmp[1] = new NonTerminalStackNode(-2599, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2598, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__LessThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_conditional__lit___60__not_follow__lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__IfDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2631, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2630, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2629, 2, prod__lit___63__char_class___range__63_63_, new char[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(-2628, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2627, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__IfDefinedOtherwise_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___63_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2656, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2655, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2654, 2, prod__lit___61_61_62__char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new char[] {61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2653, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2652, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Implication_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2542, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2541, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2540, 2, prod__lit_o__char_class___range__111_111_, new char[] {111}, null, null);
      tmp[1] = new NonTerminalStackNode(-2539, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2538, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Composition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_o_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__NonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2626, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2625, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2624, 2, prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_, new char[] {33,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2623, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2622, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__NonEquals_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___33_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__AsType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-2507, 6, "$Expression", null, null);
      tmp[5] = new NonTerminalStackNode(-2506, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2505, 4, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode(-2504, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2503, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-2502, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2501, 0, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      builder.addAlternative(RascalRascal.prod__AsType_$Expression__lit___91_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_lit___93_$layouts_LAYOUTLIST_argument_$Expression_, tmp);
	}
    protected static final void _init_prod__FieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2407, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2406, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2405, 2, prod__lit___46__char_class___range__46_46_, new char[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode(-2404, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2403, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__FieldAccess_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___46_$layouts_LAYOUTLIST_field_$Name_, tmp);
	}
    protected static final void _init_prod__Bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2412, 4, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode(-2411, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2410, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2409, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2408, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Bracket_$Expression__lit___40_$layouts_LAYOUTLIST_expression_$Expression_$layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__QualifiedName_$Expression__qualifiedName_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2413, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__QualifiedName_$Expression__qualifiedName_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[0] = new NonTerminalStackNode(-2568, 0, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2569, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2570, 2, prod__lit___43__char_class___range__43_43_, new char[] {43}, null, null);
      tmp[3] = new NonTerminalStackNode(-2571, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2572, 4, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Addition_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___43_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__FieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2424, 6, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[5] = new NonTerminalStackNode(-2423, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2418, 4, regular__iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2419, 0, "$Field", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2420, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2421, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2422, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode(-2417, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2416, 2, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(-2415, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2414, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__FieldProject_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___60_$layouts_LAYOUTLIST_fields_iter_seps__$Field__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__NoMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2636, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2635, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2634, 2, prod__lit___33_58_61__char_class___range__33_33_char_class___range__58_58_char_class___range__61_61_, new char[] {33,58,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2633, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2632, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__NoMatch_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___33_58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__GetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2524, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2523, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2522, 2, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode(-2521, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2520, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__GetAnnotation_$Expression__expression_$Expression_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__Equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2651, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2650, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2649, 2, prod__lit___60_61_61_62__char_class___range__60_60_char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new char[] {60,61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2648, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2647, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Equivalence_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___60_61_61_62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__IsDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2500, 2, prod__lit___63__char_class___range__63_63_, new char[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(-2499, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2498, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__IsDefined_$Expression__argument_$Expression_$layouts_LAYOUTLIST_lit___63_, tmp);
	}
    protected static final void _init_prod__Negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2513, 2, "$Expression", null, null);
      tmp[1] = new NonTerminalStackNode(-2512, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2511, 0, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      builder.addAlternative(RascalRascal.prod__Negative_$Expression__lit___$layouts_LAYOUTLIST_argument_$Expression_, tmp);
	}
    protected static final void _init_prod__It_$Expression__lit_it_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode(-2442, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new char[] {105,116}, null, null);
      builder.addAlternative(RascalRascal.prod__It_$Expression__lit_it_, tmp);
	}
    protected static final void _init_prod__GreaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2592, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2591, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2590, 2, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2589, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2588, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__GreaterThan_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__NonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2466, 4, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode(-2465, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2462, 2, regular__iter_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2463, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2464, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-2461, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2460, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__NonEmptyBlock_$Expression__lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2562, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2561, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2560, 2, prod__lit___37__char_class___range__37_37_, new char[] {37}, null, null);
      tmp[1] = new NonTerminalStackNode(-2559, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2558, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Modulo_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___37_$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__GreaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2597, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2596, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2595, 2, prod__lit___62_61__char_class___range__62_62_char_class___range__61_61_, new char[] {62,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2594, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2593, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__GreaterThanOrEq_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___62_61_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2641, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2640, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2639, 2, prod__lit___60_45__char_class___range__60_60_char_class___range__45_45_, new char[] {60,45}, null, null);
      tmp[1] = new NonTerminalStackNode(-2638, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2637, 0, "$Pattern", null, null);
      builder.addAlternative(RascalRascal.prod__Enumerator_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___60_45_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2577, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2576, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2575, 2, prod__lit____char_class___range__45_45_, new char[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode(-2574, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2573, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__Subtraction_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit___$layouts_LAYOUTLIST_rhs_$Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-2477, 8, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode(-2476, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-2473, 6, regular__iter_seps__$Statement__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2474, 0, "$Statement", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2475, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-2472, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2471, 4, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      tmp[3] = new NonTerminalStackNode(-2470, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2469, 2, "$Parameters", null, null);
      tmp[1] = new NonTerminalStackNode(-2468, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2467, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__Closure_$Expression__type_$Type_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit___123_$layouts_LAYOUTLIST_statements_iter_seps__$Statement__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__NotIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2587, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2586, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2585, 2, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new char[] {110,111,116,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode(-2584, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2583, 0, "$Expression", null, null);
      builder.addAlternative(RascalRascal.prod__NotIn_$Expression__lhs_$Expression_$layouts_LAYOUTLIST_lit_notin_$layouts_LAYOUTLIST_rhs_$Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__Tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode(-2486, 4, prod__lit___62__char_class___range__62_62_, new char[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode(-2485, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode(-2480, 2, regular__iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2481, 0, "$Expression", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2482, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2483, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2484, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode(-2479, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2478, 0, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      builder.addAlternative(RascalRascal.prod__Tuple_$Expression__lit___60_$layouts_LAYOUTLIST_elements_iter_seps__$Expression__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__Match_$Expression__pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_61_$layouts_LAYOUTLIST_expression_$Expression__assoc__non_assoc(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2646, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2645, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2644, 2, prod__lit___58_61__char_class___range__58_58_char_class___range__61_61_, new char[] {58,61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2643, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2642, 0, "$Pattern", null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-2690, 2, "$ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode(-2689, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2688, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__Actuals_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_, tmp);
	}
    protected static final void _init_prod__ActualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2695, 4, "$Renamings", null, null);
      tmp[3] = new NonTerminalStackNode(-2694, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2693, 2, "$ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode(-2692, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2691, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__ActualsRenaming_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_actuals_$ModuleActuals_$layouts_LAYOUTLIST_renamings_$Renamings_, tmp);
	}
    protected static final void _init_prod__Default_$ImportedModule__name_$QualifiedName_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2696, 0, "$QualifiedName", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$ImportedModule__name_$QualifiedName_, tmp);
	}
    protected static final void _init_prod__Renamings_$ImportedModule__name_$QualifiedName_$layouts_LAYOUTLIST_renamings_$Renamings_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2699, 2, "$Renamings", null, null);
      tmp[1] = new NonTerminalStackNode(-2698, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2697, 0, "$QualifiedName", null, null);
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
      
      tmp[0] = new SeparatedListStackNode(-2724, 0, regular__iter_seps__$Command__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2725, 0, "$Command", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2726, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
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
      
      tmp[6] = new SeparatedListStackNode(-2743, 6, regular__iter_star_seps__$Import__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2744, 0, "$Import", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2745, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode(-2742, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2741, 4, "$QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode(-2740, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2739, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode(-2738, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2737, 0, "$Tags", null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__Parameters_$Header__tags_$Tags_$layouts_LAYOUTLIST_lit_module_$layouts_LAYOUTLIST_name_$QualifiedName_$layouts_LAYOUTLIST_params_$ModuleParameters_$layouts_LAYOUTLIST_imports_iter_star_seps__$Import__$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new SeparatedListStackNode(-2754, 8, regular__iter_star_seps__$Import__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2755, 0, "$Import", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2756, 1, "$layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode(-2753, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2752, 6, "$ModuleParameters", null, null);
      tmp[5] = new NonTerminalStackNode(-2751, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2750, 4, "$QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode(-2749, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2748, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new char[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode(-2747, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2746, 0, "$Tags", null, null);
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
      
      tmp[1] = new ListStackNode(-2758, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode(-2759, 0, new char[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new char[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode(-2757, 0, new char[][]{{97,122}}, null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-2768, 0, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-2769, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2770, 2, prod__lit___36__char_class___range__36_36_, new char[] {36}, null, null);
      builder.addAlternative(RascalRascal.prod__EndOfLine_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___36_, tmp);
	}
    protected static final void _init_prod__Parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2767, 2, "$Nonterminal", null, null);
      tmp[1] = new NonTerminalStackNode(-2766, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2765, 0, prod__lit___38__char_class___range__38_38_, new char[] {38}, null, null);
      builder.addAlternative(RascalRascal.prod__Parameter_$Sym__lit___38_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_, tmp);
	}
    protected static final void _init_prod__Optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2773, 2, prod__lit___63__char_class___range__63_63_, new char[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode(-2772, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2771, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Optional_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___63_, tmp);
	}
    protected static final void _init_prod__CaseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2774, 0, "$CaseInsensitiveStringConstant", null, null);
      builder.addAlternative(RascalRascal.prod__CaseInsensitiveLiteral_$Sym__cistring_$CaseInsensitiveStringConstant_, tmp);
	}
    protected static final void _init_prod__IterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-2783, 8, prod__lit___43__char_class___range__43_43_, new char[] {43}, null, null);
      tmp[7] = new NonTerminalStackNode(-2782, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2781, 6, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode(-2780, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2779, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2778, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2777, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-2776, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2775, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__IterSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___43_, tmp);
	}
    protected static final void _init_prod__Literal_$Sym__string_$StringConstant_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2784, 0, "$StringConstant", null, null);
      builder.addAlternative(RascalRascal.prod__Literal_$Sym__string_$StringConstant_, tmp);
	}
    protected static final void _init_prod__IterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2787, 2, prod__lit___42__char_class___range__42_42_, new char[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode(-2786, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2785, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__IterStar_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__Unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2880, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2879, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2878, 2, prod__lit___92__char_class___range__92_92_, new char[] {92}, null, null);
      tmp[1] = new NonTerminalStackNode(-2877, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2876, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Unequal_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___92_$layouts_LAYOUTLIST_match_$Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__NotFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2875, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2874, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2873, 2, prod__lit___33_62_62__char_class___range__33_33_char_class___range__62_62_char_class___range__62_62_, new char[] {33,62,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2872, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2871, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__NotFollow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___33_62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__StartOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2790, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-2789, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2788, 0, prod__lit___94__char_class___range__94_94_, new char[] {94}, null, null);
      builder.addAlternative(RascalRascal.prod__StartOfLine_$Sym__lit___94_$layouts_LAYOUTLIST_symbol_$Sym_, tmp);
	}
    protected static final void _init_prod__Empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2804, 2, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[1] = new NonTerminalStackNode(-2803, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2802, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Empty_$Sym__lit___40_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Parametrized_$Sym__conditional__nonterminal_$Nonterminal__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[0] = new NonTerminalStackNode(-2791, 0, "$Nonterminal", null, new ICompletionFilter[] {new StringFollowRequirement(new char[] {91})});
      tmp[1] = new NonTerminalStackNode(-2792, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2793, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[3] = new NonTerminalStackNode(-2794, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2795, 4, regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2796, 0, "$Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2797, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2798, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2799, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-2800, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2801, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      builder.addAlternative(RascalRascal.prod__Parametrized_$Sym__conditional__nonterminal_$Nonterminal__follow__lit___91_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_parameters_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[0] = new LiteralStackNode(-2811, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode(-2812, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2813, 2, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2814, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode(-2815, 4, regular__iter_seps__$Sym__$layouts_LAYOUTLIST, new NonTerminalStackNode(-2816, 0, "$Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2817, 1, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-2818, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2819, 6, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      builder.addAlternative(RascalRascal.prod__Sequence_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_sequence_iter_seps__$Sym__$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2860, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2859, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2858, 2, prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_, new char[] {60,60}, null, null);
      tmp[1] = new NonTerminalStackNode(-2857, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2856, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Precede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__Nonterminal_$Sym__conditional__nonterminal_$Nonterminal__not_follow__lit___91_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2805, 0, "$Nonterminal", null, new ICompletionFilter[] {new StringFollowRestriction(new char[] {91})});
      builder.addAlternative(RascalRascal.prod__Nonterminal_$Sym__conditional__nonterminal_$Nonterminal__not_follow__lit___91_, tmp);
	}
    protected static final void _init_prod__Column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2810, 4, "$IntegerLiteral", null, null);
      tmp[3] = new NonTerminalStackNode(-2809, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2808, 2, prod__lit___64__char_class___range__64_64_, new char[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode(-2807, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2806, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Column_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___64_$layouts_LAYOUTLIST_column_$IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__Follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2870, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2869, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2868, 2, prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_, new char[] {62,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2867, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2866, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Follow_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___62_62_$layouts_LAYOUTLIST_match_$Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__IterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-2841, 8, prod__lit___42__char_class___range__42_42_, new char[] {42}, null, null);
      tmp[7] = new NonTerminalStackNode(-2840, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode(-2839, 6, prod__lit___125__char_class___range__125_125_, new char[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode(-2838, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2837, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2836, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2835, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-2834, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2833, 0, prod__lit___123__char_class___range__123_123_, new char[] {123}, null, null);
      builder.addAlternative(RascalRascal.prod__IterStarSep_$Sym__lit___123_$layouts_LAYOUTLIST_symbol_$Sym_$layouts_LAYOUTLIST_sep_$Sym_$layouts_LAYOUTLIST_lit___125_$layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__CharacterClass_$Sym__charClass_$Class_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2842, 0, "$Class", null, null);
      builder.addAlternative(RascalRascal.prod__CharacterClass_$Sym__charClass_$Class_, tmp);
	}
    protected static final void _init_prod__Alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode(-2832, 8, prod__lit___41__char_class___range__41_41_, new char[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode(-2831, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode(-2826, 6, regular__iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2827, 0, "$Sym", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2828, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2829, 2, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null), new NonTerminalStackNode(-2830, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode(-2825, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2824, 4, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode(-2823, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2822, 2, "$Sym", null, null);
      tmp[1] = new NonTerminalStackNode(-2821, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2820, 0, prod__lit___40__char_class___range__40_40_, new char[] {40}, null, null);
      builder.addAlternative(RascalRascal.prod__Alternative_$Sym__lit___40_$layouts_LAYOUTLIST_first_$Sym_$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_alternatives_iter_seps__$Sym__$layouts_LAYOUTLIST_lit___124_$layouts_LAYOUTLIST_$layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode(-2845, 2, "$NonterminalLabel", null, null);
      tmp[1] = new NonTerminalStackNode(-2844, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2843, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__Labeled_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_label_$NonterminalLabel_, tmp);
	}
    protected static final void _init_prod__Start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode(-2852, 6, prod__lit___93__char_class___range__93_93_, new char[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode(-2851, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2850, 4, "$Nonterminal", null, null);
      tmp[3] = new NonTerminalStackNode(-2849, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2848, 2, prod__lit___91__char_class___range__91_91_, new char[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode(-2847, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2846, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new char[] {115,116,97,114,116}, null, null);
      builder.addAlternative(RascalRascal.prod__Start_$Sym__lit_start_$layouts_LAYOUTLIST_lit___91_$layouts_LAYOUTLIST_nonterminal_$Nonterminal_$layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__NotPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode(-2865, 4, "$Sym", null, null);
      tmp[3] = new NonTerminalStackNode(-2864, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2863, 2, prod__lit___33_60_60__char_class___range__33_33_char_class___range__60_60_char_class___range__60_60_, new char[] {33,60,60}, null, null);
      tmp[1] = new NonTerminalStackNode(-2862, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2861, 0, "$Sym", null, null);
      builder.addAlternative(RascalRascal.prod__NotPrecede_$Sym__match_$Sym_$layouts_LAYOUTLIST_lit___33_60_60_$layouts_LAYOUTLIST_symbol_$Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__Iter_$Sym__symbol_$Sym_$layouts_LAYOUTLIST_lit___43_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode(-2855, 2, prod__lit___43__char_class___range__43_43_, new char[] {43}, null, null);
      tmp[1] = new NonTerminalStackNode(-2854, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2853, 0, "$Sym", null, null);
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
      
      tmp[4] = new NonTerminalStackNode(-2904, 4, "$Statement", null, null);
      tmp[3] = new NonTerminalStackNode(-2903, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2902, 2, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode(-2901, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2900, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new char[] {99,97,116,99,104}, null, null);
      builder.addAlternative(RascalRascal.prod__Default_$Catch__lit_catch_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement__tag__Foldable, tmp);
	}
    protected static final void _init_prod__Binding_$Catch__lit_catch_$layouts_LAYOUTLIST_pattern_$Pattern_$layouts_LAYOUTLIST_lit___58_$layouts_LAYOUTLIST_body_$Statement__tag__Foldable(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode(-2911, 6, "$Statement", null, null);
      tmp[5] = new NonTerminalStackNode(-2910, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode(-2909, 4, prod__lit___58__char_class___range__58_58_, new char[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode(-2908, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2907, 2, "$Pattern", null, null);
      tmp[1] = new NonTerminalStackNode(-2906, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode(-2905, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new char[] {99,97,116,99,104}, null, null);
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
      
      tmp[4] = new NonTerminalStackNode(-2899, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2898, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2897, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new char[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode(-2896, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2895, 0, "$Name", null, null);
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
      
      tmp[6] = new NonTerminalStackNode(-2928, 6, "$Parameters", null, null);
      tmp[5] = new NonTerminalStackNode(-2927, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2926, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2925, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2924, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-2923, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2922, 0, "$FunctionModifiers", null, null);
      builder.addAlternative(RascalRascal.prod__NoThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_, tmp);
	}
    protected static final void _init_prod__WithThrows_$Signature__modifiers_$FunctionModifiers_$layouts_LAYOUTLIST_type_$Type_$layouts_LAYOUTLIST_name_$Name_$layouts_LAYOUTLIST_parameters_$Parameters_$layouts_LAYOUTLIST_lit_throws_$layouts_LAYOUTLIST_exceptions_iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[11];
      
      tmp[10] = new SeparatedListStackNode(-2939, 10, regular__iter_seps__$Type__$layouts_LAYOUTLIST_lit___44_$layouts_LAYOUTLIST, new NonTerminalStackNode(-2940, 0, "$Type", null, null), new AbstractStackNode[]{new NonTerminalStackNode(-2941, 1, "$layouts_LAYOUTLIST", null, null), new LiteralStackNode(-2942, 2, prod__lit___44__char_class___range__44_44_, new char[] {44}, null, null), new NonTerminalStackNode(-2943, 3, "$layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode(-2938, 9, "$layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode(-2937, 8, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new char[] {116,104,114,111,119,115}, null, null);
      tmp[7] = new NonTerminalStackNode(-2936, 7, "$layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode(-2935, 6, "$Parameters", null, null);
      tmp[5] = new NonTerminalStackNode(-2934, 5, "$layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode(-2933, 4, "$Name", null, null);
      tmp[3] = new NonTerminalStackNode(-2932, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode(-2931, 2, "$Type", null, null);
      tmp[1] = new NonTerminalStackNode(-2930, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2929, 0, "$FunctionModifiers", null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-2952, 2, "$Name", null, null);
      tmp[1] = new NonTerminalStackNode(-2951, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2950, 0, "$Type", null, null);
      builder.addAlternative(RascalRascal.prod__Named_$TypeArg__type_$Type_$layouts_LAYOUTLIST_name_$Name_, tmp);
	}
    protected static final void _init_prod__Default_$TypeArg__type_$Type_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2953, 0, "$Type", null, null);
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
      
      tmp[4] = new NonTerminalStackNode(-2948, 4, "$Expression", null, null);
      tmp[3] = new NonTerminalStackNode(-2947, 3, "$layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode(-2946, 2, prod__lit___61__char_class___range__61_61_, new char[] {61}, null, null);
      tmp[1] = new NonTerminalStackNode(-2945, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2944, 0, "$Name", null, null);
      builder.addAlternative(RascalRascal.prod__Initialized_$Variable__name_$Name_$layouts_LAYOUTLIST_lit___61_$layouts_LAYOUTLIST_initial_$Expression_, tmp);
	}
    protected static final void _init_prod__UnInitialized_$Variable__name_$Name_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2949, 0, "$Name", null, null);
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
      
      tmp[2] = new LiteralStackNode(-2969, 2, prod__lit___60__char_class___range__60_60_, new char[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode(-2968, 1, "$URLChars", null, null);
      tmp[0] = new LiteralStackNode(-2967, 0, prod__lit___124__char_class___range__124_124_, new char[] {124}, null, null);
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
      
      tmp[0] = new NonTerminalStackNode(-2980, 0, "$LocationLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Location_$Literal__locationLiteral_$LocationLiteral_, tmp);
	}
    protected static final void _init_prod__Real_$Literal__realLiteral_$RealLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2981, 0, "$RealLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Real_$Literal__realLiteral_$RealLiteral_, tmp);
	}
    protected static final void _init_prod__RegExp_$Literal__regExpLiteral_$RegExpLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2982, 0, "$RegExpLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__RegExp_$Literal__regExpLiteral_$RegExpLiteral_, tmp);
	}
    protected static final void _init_prod__Rational_$Literal__rationalLiteral_$RationalLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2983, 0, "$RationalLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Rational_$Literal__rationalLiteral_$RationalLiteral_, tmp);
	}
    protected static final void _init_prod__Integer_$Literal__integerLiteral_$IntegerLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2984, 0, "$IntegerLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Integer_$Literal__integerLiteral_$IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__Boolean_$Literal__booleanLiteral_$BooleanLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2985, 0, "$BooleanLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__Boolean_$Literal__booleanLiteral_$BooleanLiteral_, tmp);
	}
    protected static final void _init_prod__DateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2986, 0, "$DateTimeLiteral", null, null);
      builder.addAlternative(RascalRascal.prod__DateTime_$Literal__dateTimeLiteral_$DateTimeLiteral_, tmp);
	}
    protected static final void _init_prod__String_$Literal__stringLiteral_$StringLiteral_(ExpectBuilder builder) {
      AbstractStackNode[] tmp = new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode(-2987, 0, "$StringLiteral", null, null);
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
      
      tmp[2] = new NonTerminalStackNode(-2996, 2, "$Body", null, null);
      tmp[1] = new NonTerminalStackNode(-2995, 1, "$layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode(-2994, 0, "$Header", null, null);
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