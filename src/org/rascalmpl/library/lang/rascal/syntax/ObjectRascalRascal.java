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
public class ObjectRascalRascal extends org.rascalmpl.library.lang.rascal.syntax.RascalRascal {
  protected final static IValueFactory VF = ValueFactoryFactory.getValueFactory();
  
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
    IntegerKeyedHashMap<IntegerList> result = org.rascalmpl.library.lang.rascal.syntax.RascalRascal._initDontNest(); 
    
    
    
    
    _putDontNest(result, 644, 946);
    
    _putDontNest(result, 644, 964);
    
    _putDontNest(result, 644, 986);
    
    _putDontNest(result, 644, 1004);
    
    _putDontNest(result, 644, 1022);
    
    _putDontNest(result, 656, 946);
    
    _putDontNest(result, 656, 964);
    
    _putDontNest(result, 656, 986);
    
    _putDontNest(result, 656, 1004);
    
    _putDontNest(result, 656, 1022);
    
    _putDontNest(result, 742, 946);
    
    _putDontNest(result, 742, 964);
    
    _putDontNest(result, 742, 986);
    
    _putDontNest(result, 742, 1004);
    
    _putDontNest(result, 742, 1022);
    
    _putDontNest(result, 754, 946);
    
    _putDontNest(result, 754, 964);
    
    _putDontNest(result, 754, 986);
    
    _putDontNest(result, 754, 1004);
    
    _putDontNest(result, 754, 1022);
    
    _putDontNest(result, 784, 946);
    
    _putDontNest(result, 784, 964);
    
    _putDontNest(result, 784, 986);
    
    _putDontNest(result, 784, 1004);
    
    _putDontNest(result, 784, 1022);
    
    _putDontNest(result, 896, 946);
    
    _putDontNest(result, 896, 964);
    
    _putDontNest(result, 896, 986);
    
    _putDontNest(result, 896, 1004);
    
    _putDontNest(result, 896, 1022);
    
    _putDontNest(result, 904, 946);
    
    _putDontNest(result, 904, 964);
    
    _putDontNest(result, 904, 986);
    
    _putDontNest(result, 904, 1004);
    
    _putDontNest(result, 904, 1022);
    
    _putDontNest(result, 916, 946);
    
    _putDontNest(result, 916, 964);
    
    _putDontNest(result, 916, 986);
    
    _putDontNest(result, 916, 1004);
    
    _putDontNest(result, 916, 1022);
    
    _putDontNest(result, 936, 1022);
    
    _putDontNest(result, 946, 946);
    
    _putDontNest(result, 946, 964);
    
    _putDontNest(result, 946, 1022);
    
    _putDontNest(result, 954, 1022);
    
    _putDontNest(result, 964, 946);
    
    _putDontNest(result, 964, 964);
    
    _putDontNest(result, 964, 1022);
    
    _putDontNest(result, 976, 986);
    
    _putDontNest(result, 976, 1004);
    
    _putDontNest(result, 976, 1022);
    
    _putDontNest(result, 986, 946);
    
    _putDontNest(result, 986, 964);
    
    _putDontNest(result, 986, 1022);
    
    _putDontNest(result, 994, 986);
    
    _putDontNest(result, 994, 1004);
    
    _putDontNest(result, 994, 1022);
    
    _putDontNest(result, 1004, 946);
    
    _putDontNest(result, 1004, 964);
    
    _putDontNest(result, 1004, 1022);
    
    _putDontNest(result, 1022, 1022);
    
    _putDontNest(result, 1498, 2234);
    
    _putDontNest(result, 1498, 2246);
    
    _putDontNest(result, 1498, 2270);
    
    _putDontNest(result, 1498, 2284);
    
    _putDontNest(result, 1498, 2306);
    
    _putDontNest(result, 1498, 2324);
    
    _putDontNest(result, 1498, 2346);
    
    _putDontNest(result, 1498, 2374);
    
    _putDontNest(result, 1498, 2392);
    
    _putDontNest(result, 1498, 2410);
    
    _putDontNest(result, 1498, 2432);
    
    _putDontNest(result, 1498, 2456);
    
    _putDontNest(result, 1498, 2478);
    
    _putDontNest(result, 1498, 2496);
    
    _putDontNest(result, 1498, 2516);
    
    _putDontNest(result, 1498, 2536);
    
    _putDontNest(result, 1498, 2554);
    
    _putDontNest(result, 1498, 2576);
    
    _putDontNest(result, 1498, 2594);
    
    _putDontNest(result, 1498, 2616);
    
    _putDontNest(result, 1498, 2638);
    
    _putDontNest(result, 1498, 2656);
    
    _putDontNest(result, 1498, 2674);
    
    _putDontNest(result, 1498, 2696);
    
    _putDontNest(result, 1498, 2714);
    
    _putDontNest(result, 1498, 2732);
    
    _putDontNest(result, 1498, 2754);
    
    _putDontNest(result, 1498, 2772);
    
    _putDontNest(result, 1498, 2790);
    
    _putDontNest(result, 1498, 2812);
    
    _putDontNest(result, 1498, 2830);
    
    _putDontNest(result, 1498, 2848);
    
    _putDontNest(result, 1498, 2866);
    
    _putDontNest(result, 1498, 2894);
    
    _putDontNest(result, 1610, 1710);
    
    _putDontNest(result, 1610, 1770);
    
    _putDontNest(result, 1610, 2222);
    
    _putDontNest(result, 1610, 2234);
    
    _putDontNest(result, 1610, 2246);
    
    _putDontNest(result, 1610, 2270);
    
    _putDontNest(result, 1610, 2284);
    
    _putDontNest(result, 1610, 2306);
    
    _putDontNest(result, 1610, 2324);
    
    _putDontNest(result, 1610, 2346);
    
    _putDontNest(result, 1610, 2374);
    
    _putDontNest(result, 1610, 2392);
    
    _putDontNest(result, 1610, 2410);
    
    _putDontNest(result, 1610, 2432);
    
    _putDontNest(result, 1610, 2456);
    
    _putDontNest(result, 1610, 2478);
    
    _putDontNest(result, 1610, 2496);
    
    _putDontNest(result, 1610, 2516);
    
    _putDontNest(result, 1610, 2536);
    
    _putDontNest(result, 1610, 2554);
    
    _putDontNest(result, 1610, 2576);
    
    _putDontNest(result, 1610, 2594);
    
    _putDontNest(result, 1610, 2616);
    
    _putDontNest(result, 1610, 2638);
    
    _putDontNest(result, 1610, 2656);
    
    _putDontNest(result, 1610, 2674);
    
    _putDontNest(result, 1610, 2696);
    
    _putDontNest(result, 1610, 2714);
    
    _putDontNest(result, 1610, 2732);
    
    _putDontNest(result, 1610, 2754);
    
    _putDontNest(result, 1610, 2772);
    
    _putDontNest(result, 1610, 2790);
    
    _putDontNest(result, 1610, 2812);
    
    _putDontNest(result, 1610, 2830);
    
    _putDontNest(result, 1610, 2848);
    
    _putDontNest(result, 1610, 2866);
    
    _putDontNest(result, 1610, 2894);
    
    _putDontNest(result, 1682, 2234);
    
    _putDontNest(result, 1682, 2246);
    
    _putDontNest(result, 1682, 2270);
    
    _putDontNest(result, 1682, 2284);
    
    _putDontNest(result, 1682, 2306);
    
    _putDontNest(result, 1682, 2324);
    
    _putDontNest(result, 1682, 2346);
    
    _putDontNest(result, 1682, 2374);
    
    _putDontNest(result, 1682, 2392);
    
    _putDontNest(result, 1682, 2410);
    
    _putDontNest(result, 1682, 2432);
    
    _putDontNest(result, 1682, 2456);
    
    _putDontNest(result, 1682, 2478);
    
    _putDontNest(result, 1682, 2496);
    
    _putDontNest(result, 1682, 2516);
    
    _putDontNest(result, 1682, 2536);
    
    _putDontNest(result, 1682, 2554);
    
    _putDontNest(result, 1682, 2576);
    
    _putDontNest(result, 1682, 2594);
    
    _putDontNest(result, 1682, 2616);
    
    _putDontNest(result, 1682, 2638);
    
    _putDontNest(result, 1682, 2656);
    
    _putDontNest(result, 1682, 2674);
    
    _putDontNest(result, 1682, 2696);
    
    _putDontNest(result, 1682, 2714);
    
    _putDontNest(result, 1682, 2732);
    
    _putDontNest(result, 1682, 2754);
    
    _putDontNest(result, 1682, 2772);
    
    _putDontNest(result, 1682, 2790);
    
    _putDontNest(result, 1682, 2812);
    
    _putDontNest(result, 1682, 2830);
    
    _putDontNest(result, 1682, 2848);
    
    _putDontNest(result, 1682, 2866);
    
    _putDontNest(result, 1682, 2894);
    
    _putDontNest(result, 1700, 2234);
    
    _putDontNest(result, 1700, 2246);
    
    _putDontNest(result, 1700, 2270);
    
    _putDontNest(result, 1700, 2284);
    
    _putDontNest(result, 1700, 2306);
    
    _putDontNest(result, 1700, 2324);
    
    _putDontNest(result, 1700, 2346);
    
    _putDontNest(result, 1700, 2374);
    
    _putDontNest(result, 1700, 2392);
    
    _putDontNest(result, 1700, 2410);
    
    _putDontNest(result, 1700, 2432);
    
    _putDontNest(result, 1700, 2456);
    
    _putDontNest(result, 1700, 2478);
    
    _putDontNest(result, 1700, 2496);
    
    _putDontNest(result, 1700, 2516);
    
    _putDontNest(result, 1700, 2536);
    
    _putDontNest(result, 1700, 2554);
    
    _putDontNest(result, 1700, 2576);
    
    _putDontNest(result, 1700, 2594);
    
    _putDontNest(result, 1700, 2616);
    
    _putDontNest(result, 1700, 2638);
    
    _putDontNest(result, 1700, 2656);
    
    _putDontNest(result, 1700, 2674);
    
    _putDontNest(result, 1700, 2696);
    
    _putDontNest(result, 1700, 2714);
    
    _putDontNest(result, 1700, 2732);
    
    _putDontNest(result, 1700, 2754);
    
    _putDontNest(result, 1700, 2772);
    
    _putDontNest(result, 1700, 2790);
    
    _putDontNest(result, 1700, 2812);
    
    _putDontNest(result, 1700, 2830);
    
    _putDontNest(result, 1700, 2848);
    
    _putDontNest(result, 1700, 2866);
    
    _putDontNest(result, 1700, 2894);
    
    _putDontNest(result, 1742, 2234);
    
    _putDontNest(result, 1742, 2246);
    
    _putDontNest(result, 1742, 2270);
    
    _putDontNest(result, 1742, 2284);
    
    _putDontNest(result, 1742, 2306);
    
    _putDontNest(result, 1742, 2324);
    
    _putDontNest(result, 1742, 2346);
    
    _putDontNest(result, 1742, 2374);
    
    _putDontNest(result, 1742, 2392);
    
    _putDontNest(result, 1742, 2410);
    
    _putDontNest(result, 1742, 2432);
    
    _putDontNest(result, 1742, 2456);
    
    _putDontNest(result, 1742, 2478);
    
    _putDontNest(result, 1742, 2496);
    
    _putDontNest(result, 1742, 2516);
    
    _putDontNest(result, 1742, 2536);
    
    _putDontNest(result, 1742, 2554);
    
    _putDontNest(result, 1742, 2576);
    
    _putDontNest(result, 1742, 2594);
    
    _putDontNest(result, 1742, 2616);
    
    _putDontNest(result, 1742, 2638);
    
    _putDontNest(result, 1742, 2656);
    
    _putDontNest(result, 1742, 2674);
    
    _putDontNest(result, 1742, 2696);
    
    _putDontNest(result, 1742, 2714);
    
    _putDontNest(result, 1742, 2732);
    
    _putDontNest(result, 1742, 2754);
    
    _putDontNest(result, 1742, 2772);
    
    _putDontNest(result, 1742, 2790);
    
    _putDontNest(result, 1742, 2812);
    
    _putDontNest(result, 1742, 2830);
    
    _putDontNest(result, 1742, 2848);
    
    _putDontNest(result, 1742, 2866);
    
    _putDontNest(result, 1742, 2894);
    
    _putDontNest(result, 1760, 2234);
    
    _putDontNest(result, 1760, 2246);
    
    _putDontNest(result, 1760, 2270);
    
    _putDontNest(result, 1760, 2284);
    
    _putDontNest(result, 1760, 2306);
    
    _putDontNest(result, 1760, 2324);
    
    _putDontNest(result, 1760, 2346);
    
    _putDontNest(result, 1760, 2374);
    
    _putDontNest(result, 1760, 2392);
    
    _putDontNest(result, 1760, 2410);
    
    _putDontNest(result, 1760, 2432);
    
    _putDontNest(result, 1760, 2456);
    
    _putDontNest(result, 1760, 2478);
    
    _putDontNest(result, 1760, 2496);
    
    _putDontNest(result, 1760, 2516);
    
    _putDontNest(result, 1760, 2536);
    
    _putDontNest(result, 1760, 2554);
    
    _putDontNest(result, 1760, 2576);
    
    _putDontNest(result, 1760, 2594);
    
    _putDontNest(result, 1760, 2616);
    
    _putDontNest(result, 1760, 2638);
    
    _putDontNest(result, 1760, 2656);
    
    _putDontNest(result, 1760, 2674);
    
    _putDontNest(result, 1760, 2696);
    
    _putDontNest(result, 1760, 2714);
    
    _putDontNest(result, 1760, 2732);
    
    _putDontNest(result, 1760, 2754);
    
    _putDontNest(result, 1760, 2772);
    
    _putDontNest(result, 1760, 2790);
    
    _putDontNest(result, 1760, 2812);
    
    _putDontNest(result, 1760, 2830);
    
    _putDontNest(result, 1760, 2848);
    
    _putDontNest(result, 1760, 2866);
    
    _putDontNest(result, 1760, 2894);
    
    _putDontNest(result, 1784, 2234);
    
    _putDontNest(result, 1784, 2246);
    
    _putDontNest(result, 1784, 2270);
    
    _putDontNest(result, 1784, 2284);
    
    _putDontNest(result, 1784, 2306);
    
    _putDontNest(result, 1784, 2324);
    
    _putDontNest(result, 1784, 2346);
    
    _putDontNest(result, 1784, 2374);
    
    _putDontNest(result, 1784, 2392);
    
    _putDontNest(result, 1784, 2410);
    
    _putDontNest(result, 1784, 2432);
    
    _putDontNest(result, 1784, 2456);
    
    _putDontNest(result, 1784, 2478);
    
    _putDontNest(result, 1784, 2496);
    
    _putDontNest(result, 1784, 2516);
    
    _putDontNest(result, 1784, 2536);
    
    _putDontNest(result, 1784, 2554);
    
    _putDontNest(result, 1784, 2576);
    
    _putDontNest(result, 1784, 2594);
    
    _putDontNest(result, 1784, 2616);
    
    _putDontNest(result, 1784, 2638);
    
    _putDontNest(result, 1784, 2656);
    
    _putDontNest(result, 1784, 2674);
    
    _putDontNest(result, 1784, 2696);
    
    _putDontNest(result, 1784, 2714);
    
    _putDontNest(result, 1784, 2732);
    
    _putDontNest(result, 1784, 2754);
    
    _putDontNest(result, 1784, 2772);
    
    _putDontNest(result, 1784, 2790);
    
    _putDontNest(result, 1784, 2812);
    
    _putDontNest(result, 1784, 2830);
    
    _putDontNest(result, 1784, 2848);
    
    _putDontNest(result, 1784, 2866);
    
    _putDontNest(result, 1784, 2894);
    
    _putDontNest(result, 1802, 2234);
    
    _putDontNest(result, 1802, 2246);
    
    _putDontNest(result, 1802, 2270);
    
    _putDontNest(result, 1802, 2284);
    
    _putDontNest(result, 1802, 2306);
    
    _putDontNest(result, 1802, 2324);
    
    _putDontNest(result, 1802, 2346);
    
    _putDontNest(result, 1802, 2374);
    
    _putDontNest(result, 1802, 2392);
    
    _putDontNest(result, 1802, 2410);
    
    _putDontNest(result, 1802, 2432);
    
    _putDontNest(result, 1802, 2456);
    
    _putDontNest(result, 1802, 2478);
    
    _putDontNest(result, 1802, 2496);
    
    _putDontNest(result, 1802, 2516);
    
    _putDontNest(result, 1802, 2536);
    
    _putDontNest(result, 1802, 2554);
    
    _putDontNest(result, 1802, 2576);
    
    _putDontNest(result, 1802, 2594);
    
    _putDontNest(result, 1802, 2616);
    
    _putDontNest(result, 1802, 2638);
    
    _putDontNest(result, 1802, 2656);
    
    _putDontNest(result, 1802, 2674);
    
    _putDontNest(result, 1802, 2696);
    
    _putDontNest(result, 1802, 2714);
    
    _putDontNest(result, 1802, 2732);
    
    _putDontNest(result, 1802, 2754);
    
    _putDontNest(result, 1802, 2772);
    
    _putDontNest(result, 1802, 2790);
    
    _putDontNest(result, 1802, 2812);
    
    _putDontNest(result, 1802, 2830);
    
    _putDontNest(result, 1802, 2848);
    
    _putDontNest(result, 1802, 2866);
    
    _putDontNest(result, 1802, 2894);
    
    _putDontNest(result, 1844, 2234);
    
    _putDontNest(result, 1844, 2246);
    
    _putDontNest(result, 1844, 2270);
    
    _putDontNest(result, 1844, 2284);
    
    _putDontNest(result, 1844, 2306);
    
    _putDontNest(result, 1844, 2324);
    
    _putDontNest(result, 1844, 2346);
    
    _putDontNest(result, 1844, 2374);
    
    _putDontNest(result, 1844, 2392);
    
    _putDontNest(result, 1844, 2410);
    
    _putDontNest(result, 1844, 2432);
    
    _putDontNest(result, 1844, 2456);
    
    _putDontNest(result, 1844, 2478);
    
    _putDontNest(result, 1844, 2496);
    
    _putDontNest(result, 1844, 2516);
    
    _putDontNest(result, 1844, 2536);
    
    _putDontNest(result, 1844, 2554);
    
    _putDontNest(result, 1844, 2576);
    
    _putDontNest(result, 1844, 2594);
    
    _putDontNest(result, 1844, 2616);
    
    _putDontNest(result, 1844, 2638);
    
    _putDontNest(result, 1844, 2656);
    
    _putDontNest(result, 1844, 2674);
    
    _putDontNest(result, 1844, 2696);
    
    _putDontNest(result, 1844, 2714);
    
    _putDontNest(result, 1844, 2732);
    
    _putDontNest(result, 1844, 2754);
    
    _putDontNest(result, 1844, 2772);
    
    _putDontNest(result, 1844, 2790);
    
    _putDontNest(result, 1844, 2812);
    
    _putDontNest(result, 1844, 2830);
    
    _putDontNest(result, 1844, 2848);
    
    _putDontNest(result, 1844, 2866);
    
    _putDontNest(result, 1844, 2894);
    
    _putDontNest(result, 1912, 1710);
    
    _putDontNest(result, 1912, 1770);
    
    _putDontNest(result, 1912, 2222);
    
    _putDontNest(result, 1912, 2234);
    
    _putDontNest(result, 1912, 2246);
    
    _putDontNest(result, 1912, 2270);
    
    _putDontNest(result, 1912, 2284);
    
    _putDontNest(result, 1912, 2306);
    
    _putDontNest(result, 1912, 2324);
    
    _putDontNest(result, 1912, 2346);
    
    _putDontNest(result, 1912, 2374);
    
    _putDontNest(result, 1912, 2392);
    
    _putDontNest(result, 1912, 2410);
    
    _putDontNest(result, 1912, 2432);
    
    _putDontNest(result, 1912, 2456);
    
    _putDontNest(result, 1912, 2478);
    
    _putDontNest(result, 1912, 2496);
    
    _putDontNest(result, 1912, 2516);
    
    _putDontNest(result, 1912, 2536);
    
    _putDontNest(result, 1912, 2554);
    
    _putDontNest(result, 1912, 2576);
    
    _putDontNest(result, 1912, 2594);
    
    _putDontNest(result, 1912, 2616);
    
    _putDontNest(result, 1912, 2638);
    
    _putDontNest(result, 1912, 2656);
    
    _putDontNest(result, 1912, 2674);
    
    _putDontNest(result, 1912, 2696);
    
    _putDontNest(result, 1912, 2714);
    
    _putDontNest(result, 1912, 2732);
    
    _putDontNest(result, 1912, 2754);
    
    _putDontNest(result, 1912, 2772);
    
    _putDontNest(result, 1912, 2790);
    
    _putDontNest(result, 1912, 2812);
    
    _putDontNest(result, 1912, 2830);
    
    _putDontNest(result, 1912, 2848);
    
    _putDontNest(result, 1912, 2866);
    
    _putDontNest(result, 1912, 2894);
    
    _putDontNest(result, 2076, 10228);
    
    _putDontNest(result, 2186, 1710);
    
    _putDontNest(result, 2186, 1770);
    
    _putDontNest(result, 2186, 2222);
    
    _putDontNest(result, 2186, 2234);
    
    _putDontNest(result, 2186, 2246);
    
    _putDontNest(result, 2186, 2270);
    
    _putDontNest(result, 2186, 2284);
    
    _putDontNest(result, 2186, 2306);
    
    _putDontNest(result, 2186, 2324);
    
    _putDontNest(result, 2186, 2346);
    
    _putDontNest(result, 2186, 2374);
    
    _putDontNest(result, 2186, 2392);
    
    _putDontNest(result, 2186, 2410);
    
    _putDontNest(result, 2186, 2432);
    
    _putDontNest(result, 2186, 2456);
    
    _putDontNest(result, 2186, 2478);
    
    _putDontNest(result, 2186, 2496);
    
    _putDontNest(result, 2186, 2516);
    
    _putDontNest(result, 2186, 2536);
    
    _putDontNest(result, 2186, 2554);
    
    _putDontNest(result, 2186, 2576);
    
    _putDontNest(result, 2186, 2594);
    
    _putDontNest(result, 2186, 2616);
    
    _putDontNest(result, 2186, 2638);
    
    _putDontNest(result, 2186, 2656);
    
    _putDontNest(result, 2186, 2674);
    
    _putDontNest(result, 2186, 2696);
    
    _putDontNest(result, 2186, 2714);
    
    _putDontNest(result, 2186, 2732);
    
    _putDontNest(result, 2186, 2754);
    
    _putDontNest(result, 2186, 2772);
    
    _putDontNest(result, 2186, 2790);
    
    _putDontNest(result, 2186, 2812);
    
    _putDontNest(result, 2186, 2830);
    
    _putDontNest(result, 2186, 2848);
    
    _putDontNest(result, 2186, 2866);
    
    _putDontNest(result, 2186, 2894);
    
    _putDontNest(result, 2216, 2234);
    
    _putDontNest(result, 2216, 2246);
    
    _putDontNest(result, 2216, 2270);
    
    _putDontNest(result, 2216, 2284);
    
    _putDontNest(result, 2216, 2306);
    
    _putDontNest(result, 2216, 2324);
    
    _putDontNest(result, 2216, 2346);
    
    _putDontNest(result, 2216, 2374);
    
    _putDontNest(result, 2216, 2392);
    
    _putDontNest(result, 2216, 2410);
    
    _putDontNest(result, 2216, 2432);
    
    _putDontNest(result, 2216, 2456);
    
    _putDontNest(result, 2216, 2478);
    
    _putDontNest(result, 2216, 2496);
    
    _putDontNest(result, 2216, 2516);
    
    _putDontNest(result, 2216, 2536);
    
    _putDontNest(result, 2216, 2554);
    
    _putDontNest(result, 2216, 2576);
    
    _putDontNest(result, 2216, 2594);
    
    _putDontNest(result, 2216, 2616);
    
    _putDontNest(result, 2216, 2638);
    
    _putDontNest(result, 2216, 2656);
    
    _putDontNest(result, 2216, 2674);
    
    _putDontNest(result, 2216, 2696);
    
    _putDontNest(result, 2216, 2714);
    
    _putDontNest(result, 2216, 2732);
    
    _putDontNest(result, 2216, 2754);
    
    _putDontNest(result, 2216, 2772);
    
    _putDontNest(result, 2216, 2790);
    
    _putDontNest(result, 2216, 2812);
    
    _putDontNest(result, 2216, 2830);
    
    _putDontNest(result, 2216, 2848);
    
    _putDontNest(result, 2216, 2866);
    
    _putDontNest(result, 2216, 2894);
    
    _putDontNest(result, 2234, 2306);
    
    _putDontNest(result, 2234, 2324);
    
    _putDontNest(result, 2234, 2346);
    
    _putDontNest(result, 2234, 2374);
    
    _putDontNest(result, 2234, 2392);
    
    _putDontNest(result, 2234, 2410);
    
    _putDontNest(result, 2234, 2432);
    
    _putDontNest(result, 2234, 2456);
    
    _putDontNest(result, 2234, 2478);
    
    _putDontNest(result, 2234, 2496);
    
    _putDontNest(result, 2234, 2516);
    
    _putDontNest(result, 2234, 2536);
    
    _putDontNest(result, 2234, 2554);
    
    _putDontNest(result, 2234, 2576);
    
    _putDontNest(result, 2234, 2594);
    
    _putDontNest(result, 2234, 2616);
    
    _putDontNest(result, 2234, 2638);
    
    _putDontNest(result, 2234, 2656);
    
    _putDontNest(result, 2234, 2674);
    
    _putDontNest(result, 2234, 2696);
    
    _putDontNest(result, 2234, 2714);
    
    _putDontNest(result, 2234, 2732);
    
    _putDontNest(result, 2234, 2812);
    
    _putDontNest(result, 2234, 2830);
    
    _putDontNest(result, 2234, 2848);
    
    _putDontNest(result, 2234, 2866);
    
    _putDontNest(result, 2234, 2894);
    
    _putDontNest(result, 2246, 2306);
    
    _putDontNest(result, 2246, 2324);
    
    _putDontNest(result, 2246, 2346);
    
    _putDontNest(result, 2246, 2374);
    
    _putDontNest(result, 2246, 2392);
    
    _putDontNest(result, 2246, 2410);
    
    _putDontNest(result, 2246, 2432);
    
    _putDontNest(result, 2246, 2456);
    
    _putDontNest(result, 2246, 2478);
    
    _putDontNest(result, 2246, 2496);
    
    _putDontNest(result, 2246, 2516);
    
    _putDontNest(result, 2246, 2536);
    
    _putDontNest(result, 2246, 2554);
    
    _putDontNest(result, 2246, 2576);
    
    _putDontNest(result, 2246, 2594);
    
    _putDontNest(result, 2246, 2616);
    
    _putDontNest(result, 2246, 2638);
    
    _putDontNest(result, 2246, 2656);
    
    _putDontNest(result, 2246, 2674);
    
    _putDontNest(result, 2246, 2696);
    
    _putDontNest(result, 2246, 2714);
    
    _putDontNest(result, 2246, 2732);
    
    _putDontNest(result, 2246, 2812);
    
    _putDontNest(result, 2246, 2830);
    
    _putDontNest(result, 2246, 2848);
    
    _putDontNest(result, 2246, 2866);
    
    _putDontNest(result, 2246, 2894);
    
    _putDontNest(result, 2270, 2306);
    
    _putDontNest(result, 2270, 2324);
    
    _putDontNest(result, 2270, 2346);
    
    _putDontNest(result, 2270, 2374);
    
    _putDontNest(result, 2270, 2392);
    
    _putDontNest(result, 2270, 2410);
    
    _putDontNest(result, 2270, 2432);
    
    _putDontNest(result, 2270, 2456);
    
    _putDontNest(result, 2270, 2478);
    
    _putDontNest(result, 2270, 2496);
    
    _putDontNest(result, 2270, 2516);
    
    _putDontNest(result, 2270, 2536);
    
    _putDontNest(result, 2270, 2554);
    
    _putDontNest(result, 2270, 2576);
    
    _putDontNest(result, 2270, 2594);
    
    _putDontNest(result, 2270, 2616);
    
    _putDontNest(result, 2270, 2638);
    
    _putDontNest(result, 2270, 2656);
    
    _putDontNest(result, 2270, 2674);
    
    _putDontNest(result, 2270, 2696);
    
    _putDontNest(result, 2270, 2714);
    
    _putDontNest(result, 2270, 2732);
    
    _putDontNest(result, 2270, 2754);
    
    _putDontNest(result, 2270, 2790);
    
    _putDontNest(result, 2270, 2812);
    
    _putDontNest(result, 2270, 2830);
    
    _putDontNest(result, 2270, 2848);
    
    _putDontNest(result, 2270, 2866);
    
    _putDontNest(result, 2270, 2894);
    
    _putDontNest(result, 2284, 2306);
    
    _putDontNest(result, 2284, 2324);
    
    _putDontNest(result, 2284, 2346);
    
    _putDontNest(result, 2284, 2374);
    
    _putDontNest(result, 2284, 2392);
    
    _putDontNest(result, 2284, 2410);
    
    _putDontNest(result, 2284, 2432);
    
    _putDontNest(result, 2284, 2456);
    
    _putDontNest(result, 2284, 2478);
    
    _putDontNest(result, 2284, 2496);
    
    _putDontNest(result, 2284, 2516);
    
    _putDontNest(result, 2284, 2536);
    
    _putDontNest(result, 2284, 2554);
    
    _putDontNest(result, 2284, 2576);
    
    _putDontNest(result, 2284, 2594);
    
    _putDontNest(result, 2284, 2616);
    
    _putDontNest(result, 2284, 2638);
    
    _putDontNest(result, 2284, 2656);
    
    _putDontNest(result, 2284, 2674);
    
    _putDontNest(result, 2284, 2696);
    
    _putDontNest(result, 2284, 2714);
    
    _putDontNest(result, 2284, 2732);
    
    _putDontNest(result, 2284, 2754);
    
    _putDontNest(result, 2284, 2790);
    
    _putDontNest(result, 2284, 2812);
    
    _putDontNest(result, 2284, 2830);
    
    _putDontNest(result, 2284, 2848);
    
    _putDontNest(result, 2284, 2866);
    
    _putDontNest(result, 2284, 2894);
    
    _putDontNest(result, 2296, 2346);
    
    _putDontNest(result, 2296, 2374);
    
    _putDontNest(result, 2296, 2392);
    
    _putDontNest(result, 2296, 2410);
    
    _putDontNest(result, 2296, 2432);
    
    _putDontNest(result, 2296, 2456);
    
    _putDontNest(result, 2296, 2478);
    
    _putDontNest(result, 2296, 2496);
    
    _putDontNest(result, 2296, 2516);
    
    _putDontNest(result, 2296, 2536);
    
    _putDontNest(result, 2296, 2554);
    
    _putDontNest(result, 2296, 2576);
    
    _putDontNest(result, 2296, 2594);
    
    _putDontNest(result, 2296, 2616);
    
    _putDontNest(result, 2296, 2638);
    
    _putDontNest(result, 2296, 2656);
    
    _putDontNest(result, 2296, 2674);
    
    _putDontNest(result, 2296, 2696);
    
    _putDontNest(result, 2296, 2714);
    
    _putDontNest(result, 2296, 2732);
    
    _putDontNest(result, 2296, 2754);
    
    _putDontNest(result, 2296, 2772);
    
    _putDontNest(result, 2296, 2790);
    
    _putDontNest(result, 2296, 2812);
    
    _putDontNest(result, 2296, 2830);
    
    _putDontNest(result, 2296, 2848);
    
    _putDontNest(result, 2296, 2866);
    
    _putDontNest(result, 2296, 2894);
    
    _putDontNest(result, 2306, 2306);
    
    _putDontNest(result, 2306, 2324);
    
    _putDontNest(result, 2306, 2346);
    
    _putDontNest(result, 2306, 2374);
    
    _putDontNest(result, 2306, 2392);
    
    _putDontNest(result, 2306, 2410);
    
    _putDontNest(result, 2306, 2432);
    
    _putDontNest(result, 2306, 2456);
    
    _putDontNest(result, 2306, 2478);
    
    _putDontNest(result, 2306, 2496);
    
    _putDontNest(result, 2306, 2516);
    
    _putDontNest(result, 2306, 2536);
    
    _putDontNest(result, 2306, 2554);
    
    _putDontNest(result, 2306, 2576);
    
    _putDontNest(result, 2306, 2594);
    
    _putDontNest(result, 2306, 2616);
    
    _putDontNest(result, 2306, 2638);
    
    _putDontNest(result, 2306, 2656);
    
    _putDontNest(result, 2306, 2674);
    
    _putDontNest(result, 2306, 2696);
    
    _putDontNest(result, 2306, 2714);
    
    _putDontNest(result, 2306, 2732);
    
    _putDontNest(result, 2306, 2812);
    
    _putDontNest(result, 2306, 2830);
    
    _putDontNest(result, 2306, 2848);
    
    _putDontNest(result, 2306, 2866);
    
    _putDontNest(result, 2306, 2894);
    
    _putDontNest(result, 2314, 2346);
    
    _putDontNest(result, 2314, 2374);
    
    _putDontNest(result, 2314, 2392);
    
    _putDontNest(result, 2314, 2410);
    
    _putDontNest(result, 2314, 2432);
    
    _putDontNest(result, 2314, 2456);
    
    _putDontNest(result, 2314, 2478);
    
    _putDontNest(result, 2314, 2496);
    
    _putDontNest(result, 2314, 2516);
    
    _putDontNest(result, 2314, 2536);
    
    _putDontNest(result, 2314, 2554);
    
    _putDontNest(result, 2314, 2576);
    
    _putDontNest(result, 2314, 2594);
    
    _putDontNest(result, 2314, 2616);
    
    _putDontNest(result, 2314, 2638);
    
    _putDontNest(result, 2314, 2656);
    
    _putDontNest(result, 2314, 2674);
    
    _putDontNest(result, 2314, 2696);
    
    _putDontNest(result, 2314, 2714);
    
    _putDontNest(result, 2314, 2732);
    
    _putDontNest(result, 2314, 2754);
    
    _putDontNest(result, 2314, 2772);
    
    _putDontNest(result, 2314, 2790);
    
    _putDontNest(result, 2314, 2812);
    
    _putDontNest(result, 2314, 2830);
    
    _putDontNest(result, 2314, 2848);
    
    _putDontNest(result, 2314, 2866);
    
    _putDontNest(result, 2314, 2894);
    
    _putDontNest(result, 2324, 2306);
    
    _putDontNest(result, 2324, 2324);
    
    _putDontNest(result, 2324, 2346);
    
    _putDontNest(result, 2324, 2374);
    
    _putDontNest(result, 2324, 2392);
    
    _putDontNest(result, 2324, 2410);
    
    _putDontNest(result, 2324, 2432);
    
    _putDontNest(result, 2324, 2456);
    
    _putDontNest(result, 2324, 2478);
    
    _putDontNest(result, 2324, 2496);
    
    _putDontNest(result, 2324, 2516);
    
    _putDontNest(result, 2324, 2536);
    
    _putDontNest(result, 2324, 2554);
    
    _putDontNest(result, 2324, 2576);
    
    _putDontNest(result, 2324, 2594);
    
    _putDontNest(result, 2324, 2616);
    
    _putDontNest(result, 2324, 2638);
    
    _putDontNest(result, 2324, 2656);
    
    _putDontNest(result, 2324, 2674);
    
    _putDontNest(result, 2324, 2696);
    
    _putDontNest(result, 2324, 2714);
    
    _putDontNest(result, 2324, 2732);
    
    _putDontNest(result, 2324, 2812);
    
    _putDontNest(result, 2324, 2830);
    
    _putDontNest(result, 2324, 2848);
    
    _putDontNest(result, 2324, 2866);
    
    _putDontNest(result, 2324, 2894);
    
    _putDontNest(result, 2336, 2432);
    
    _putDontNest(result, 2336, 2456);
    
    _putDontNest(result, 2336, 2478);
    
    _putDontNest(result, 2336, 2496);
    
    _putDontNest(result, 2336, 2516);
    
    _putDontNest(result, 2336, 2536);
    
    _putDontNest(result, 2336, 2554);
    
    _putDontNest(result, 2336, 2576);
    
    _putDontNest(result, 2336, 2594);
    
    _putDontNest(result, 2336, 2616);
    
    _putDontNest(result, 2336, 2638);
    
    _putDontNest(result, 2336, 2656);
    
    _putDontNest(result, 2336, 2674);
    
    _putDontNest(result, 2336, 2696);
    
    _putDontNest(result, 2336, 2714);
    
    _putDontNest(result, 2336, 2732);
    
    _putDontNest(result, 2336, 2754);
    
    _putDontNest(result, 2336, 2772);
    
    _putDontNest(result, 2336, 2790);
    
    _putDontNest(result, 2336, 2812);
    
    _putDontNest(result, 2336, 2830);
    
    _putDontNest(result, 2336, 2848);
    
    _putDontNest(result, 2336, 2866);
    
    _putDontNest(result, 2336, 2894);
    
    _putDontNest(result, 2346, 2346);
    
    _putDontNest(result, 2346, 2374);
    
    _putDontNest(result, 2346, 2392);
    
    _putDontNest(result, 2346, 2410);
    
    _putDontNest(result, 2346, 2432);
    
    _putDontNest(result, 2346, 2456);
    
    _putDontNest(result, 2346, 2478);
    
    _putDontNest(result, 2346, 2496);
    
    _putDontNest(result, 2346, 2516);
    
    _putDontNest(result, 2346, 2536);
    
    _putDontNest(result, 2346, 2554);
    
    _putDontNest(result, 2346, 2576);
    
    _putDontNest(result, 2346, 2594);
    
    _putDontNest(result, 2346, 2616);
    
    _putDontNest(result, 2346, 2638);
    
    _putDontNest(result, 2346, 2656);
    
    _putDontNest(result, 2346, 2674);
    
    _putDontNest(result, 2346, 2696);
    
    _putDontNest(result, 2346, 2714);
    
    _putDontNest(result, 2346, 2732);
    
    _putDontNest(result, 2346, 2812);
    
    _putDontNest(result, 2346, 2830);
    
    _putDontNest(result, 2346, 2848);
    
    _putDontNest(result, 2346, 2866);
    
    _putDontNest(result, 2346, 2894);
    
    _putDontNest(result, 2354, 2432);
    
    _putDontNest(result, 2354, 2456);
    
    _putDontNest(result, 2354, 2478);
    
    _putDontNest(result, 2354, 2496);
    
    _putDontNest(result, 2354, 2516);
    
    _putDontNest(result, 2354, 2536);
    
    _putDontNest(result, 2354, 2554);
    
    _putDontNest(result, 2354, 2576);
    
    _putDontNest(result, 2354, 2594);
    
    _putDontNest(result, 2354, 2616);
    
    _putDontNest(result, 2354, 2638);
    
    _putDontNest(result, 2354, 2656);
    
    _putDontNest(result, 2354, 2674);
    
    _putDontNest(result, 2354, 2696);
    
    _putDontNest(result, 2354, 2714);
    
    _putDontNest(result, 2354, 2732);
    
    _putDontNest(result, 2354, 2754);
    
    _putDontNest(result, 2354, 2772);
    
    _putDontNest(result, 2354, 2790);
    
    _putDontNest(result, 2354, 2812);
    
    _putDontNest(result, 2354, 2830);
    
    _putDontNest(result, 2354, 2848);
    
    _putDontNest(result, 2354, 2866);
    
    _putDontNest(result, 2354, 2894);
    
    _putDontNest(result, 2374, 2346);
    
    _putDontNest(result, 2374, 2374);
    
    _putDontNest(result, 2374, 2392);
    
    _putDontNest(result, 2374, 2410);
    
    _putDontNest(result, 2374, 2432);
    
    _putDontNest(result, 2374, 2456);
    
    _putDontNest(result, 2374, 2478);
    
    _putDontNest(result, 2374, 2496);
    
    _putDontNest(result, 2374, 2516);
    
    _putDontNest(result, 2374, 2536);
    
    _putDontNest(result, 2374, 2554);
    
    _putDontNest(result, 2374, 2576);
    
    _putDontNest(result, 2374, 2594);
    
    _putDontNest(result, 2374, 2616);
    
    _putDontNest(result, 2374, 2638);
    
    _putDontNest(result, 2374, 2656);
    
    _putDontNest(result, 2374, 2674);
    
    _putDontNest(result, 2374, 2696);
    
    _putDontNest(result, 2374, 2714);
    
    _putDontNest(result, 2374, 2732);
    
    _putDontNest(result, 2374, 2754);
    
    _putDontNest(result, 2374, 2790);
    
    _putDontNest(result, 2374, 2812);
    
    _putDontNest(result, 2374, 2830);
    
    _putDontNest(result, 2374, 2848);
    
    _putDontNest(result, 2374, 2866);
    
    _putDontNest(result, 2374, 2894);
    
    _putDontNest(result, 2382, 2432);
    
    _putDontNest(result, 2382, 2456);
    
    _putDontNest(result, 2382, 2478);
    
    _putDontNest(result, 2382, 2496);
    
    _putDontNest(result, 2382, 2516);
    
    _putDontNest(result, 2382, 2536);
    
    _putDontNest(result, 2382, 2554);
    
    _putDontNest(result, 2382, 2576);
    
    _putDontNest(result, 2382, 2594);
    
    _putDontNest(result, 2382, 2616);
    
    _putDontNest(result, 2382, 2638);
    
    _putDontNest(result, 2382, 2656);
    
    _putDontNest(result, 2382, 2674);
    
    _putDontNest(result, 2382, 2696);
    
    _putDontNest(result, 2382, 2714);
    
    _putDontNest(result, 2382, 2732);
    
    _putDontNest(result, 2382, 2754);
    
    _putDontNest(result, 2382, 2772);
    
    _putDontNest(result, 2382, 2790);
    
    _putDontNest(result, 2382, 2812);
    
    _putDontNest(result, 2382, 2830);
    
    _putDontNest(result, 2382, 2848);
    
    _putDontNest(result, 2382, 2866);
    
    _putDontNest(result, 2382, 2894);
    
    _putDontNest(result, 2392, 2346);
    
    _putDontNest(result, 2392, 2374);
    
    _putDontNest(result, 2392, 2392);
    
    _putDontNest(result, 2392, 2410);
    
    _putDontNest(result, 2392, 2432);
    
    _putDontNest(result, 2392, 2456);
    
    _putDontNest(result, 2392, 2478);
    
    _putDontNest(result, 2392, 2496);
    
    _putDontNest(result, 2392, 2516);
    
    _putDontNest(result, 2392, 2536);
    
    _putDontNest(result, 2392, 2554);
    
    _putDontNest(result, 2392, 2576);
    
    _putDontNest(result, 2392, 2594);
    
    _putDontNest(result, 2392, 2616);
    
    _putDontNest(result, 2392, 2638);
    
    _putDontNest(result, 2392, 2656);
    
    _putDontNest(result, 2392, 2674);
    
    _putDontNest(result, 2392, 2696);
    
    _putDontNest(result, 2392, 2714);
    
    _putDontNest(result, 2392, 2732);
    
    _putDontNest(result, 2392, 2812);
    
    _putDontNest(result, 2392, 2830);
    
    _putDontNest(result, 2392, 2848);
    
    _putDontNest(result, 2392, 2866);
    
    _putDontNest(result, 2392, 2894);
    
    _putDontNest(result, 2400, 2432);
    
    _putDontNest(result, 2400, 2456);
    
    _putDontNest(result, 2400, 2478);
    
    _putDontNest(result, 2400, 2496);
    
    _putDontNest(result, 2400, 2516);
    
    _putDontNest(result, 2400, 2536);
    
    _putDontNest(result, 2400, 2554);
    
    _putDontNest(result, 2400, 2576);
    
    _putDontNest(result, 2400, 2594);
    
    _putDontNest(result, 2400, 2616);
    
    _putDontNest(result, 2400, 2638);
    
    _putDontNest(result, 2400, 2656);
    
    _putDontNest(result, 2400, 2674);
    
    _putDontNest(result, 2400, 2696);
    
    _putDontNest(result, 2400, 2714);
    
    _putDontNest(result, 2400, 2732);
    
    _putDontNest(result, 2400, 2754);
    
    _putDontNest(result, 2400, 2772);
    
    _putDontNest(result, 2400, 2790);
    
    _putDontNest(result, 2400, 2812);
    
    _putDontNest(result, 2400, 2830);
    
    _putDontNest(result, 2400, 2848);
    
    _putDontNest(result, 2400, 2866);
    
    _putDontNest(result, 2400, 2894);
    
    _putDontNest(result, 2410, 2346);
    
    _putDontNest(result, 2410, 2374);
    
    _putDontNest(result, 2410, 2392);
    
    _putDontNest(result, 2410, 2410);
    
    _putDontNest(result, 2410, 2432);
    
    _putDontNest(result, 2410, 2456);
    
    _putDontNest(result, 2410, 2478);
    
    _putDontNest(result, 2410, 2496);
    
    _putDontNest(result, 2410, 2516);
    
    _putDontNest(result, 2410, 2536);
    
    _putDontNest(result, 2410, 2554);
    
    _putDontNest(result, 2410, 2576);
    
    _putDontNest(result, 2410, 2594);
    
    _putDontNest(result, 2410, 2616);
    
    _putDontNest(result, 2410, 2638);
    
    _putDontNest(result, 2410, 2656);
    
    _putDontNest(result, 2410, 2674);
    
    _putDontNest(result, 2410, 2696);
    
    _putDontNest(result, 2410, 2714);
    
    _putDontNest(result, 2410, 2732);
    
    _putDontNest(result, 2410, 2812);
    
    _putDontNest(result, 2410, 2830);
    
    _putDontNest(result, 2410, 2848);
    
    _putDontNest(result, 2410, 2866);
    
    _putDontNest(result, 2410, 2894);
    
    _putDontNest(result, 2418, 2456);
    
    _putDontNest(result, 2418, 2478);
    
    _putDontNest(result, 2418, 2496);
    
    _putDontNest(result, 2418, 2516);
    
    _putDontNest(result, 2418, 2536);
    
    _putDontNest(result, 2418, 2554);
    
    _putDontNest(result, 2418, 2576);
    
    _putDontNest(result, 2418, 2594);
    
    _putDontNest(result, 2418, 2616);
    
    _putDontNest(result, 2418, 2638);
    
    _putDontNest(result, 2418, 2656);
    
    _putDontNest(result, 2418, 2674);
    
    _putDontNest(result, 2418, 2696);
    
    _putDontNest(result, 2418, 2714);
    
    _putDontNest(result, 2418, 2732);
    
    _putDontNest(result, 2418, 2754);
    
    _putDontNest(result, 2418, 2772);
    
    _putDontNest(result, 2418, 2790);
    
    _putDontNest(result, 2418, 2812);
    
    _putDontNest(result, 2418, 2830);
    
    _putDontNest(result, 2418, 2848);
    
    _putDontNest(result, 2418, 2866);
    
    _putDontNest(result, 2418, 2894);
    
    _putDontNest(result, 2432, 2432);
    
    _putDontNest(result, 2432, 2456);
    
    _putDontNest(result, 2432, 2478);
    
    _putDontNest(result, 2432, 2496);
    
    _putDontNest(result, 2432, 2516);
    
    _putDontNest(result, 2432, 2536);
    
    _putDontNest(result, 2432, 2554);
    
    _putDontNest(result, 2432, 2576);
    
    _putDontNest(result, 2432, 2594);
    
    _putDontNest(result, 2432, 2616);
    
    _putDontNest(result, 2432, 2638);
    
    _putDontNest(result, 2432, 2656);
    
    _putDontNest(result, 2432, 2674);
    
    _putDontNest(result, 2432, 2696);
    
    _putDontNest(result, 2432, 2714);
    
    _putDontNest(result, 2432, 2732);
    
    _putDontNest(result, 2432, 2812);
    
    _putDontNest(result, 2432, 2830);
    
    _putDontNest(result, 2432, 2848);
    
    _putDontNest(result, 2432, 2866);
    
    _putDontNest(result, 2432, 2894);
    
    _putDontNest(result, 2444, 2554);
    
    _putDontNest(result, 2444, 2576);
    
    _putDontNest(result, 2444, 2594);
    
    _putDontNest(result, 2444, 2616);
    
    _putDontNest(result, 2444, 2638);
    
    _putDontNest(result, 2444, 2656);
    
    _putDontNest(result, 2444, 2674);
    
    _putDontNest(result, 2444, 2696);
    
    _putDontNest(result, 2444, 2714);
    
    _putDontNest(result, 2444, 2732);
    
    _putDontNest(result, 2444, 2754);
    
    _putDontNest(result, 2444, 2772);
    
    _putDontNest(result, 2444, 2790);
    
    _putDontNest(result, 2444, 2812);
    
    _putDontNest(result, 2444, 2830);
    
    _putDontNest(result, 2444, 2848);
    
    _putDontNest(result, 2444, 2866);
    
    _putDontNest(result, 2444, 2894);
    
    _putDontNest(result, 2456, 2456);
    
    _putDontNest(result, 2456, 2478);
    
    _putDontNest(result, 2456, 2496);
    
    _putDontNest(result, 2456, 2516);
    
    _putDontNest(result, 2456, 2536);
    
    _putDontNest(result, 2456, 2554);
    
    _putDontNest(result, 2456, 2576);
    
    _putDontNest(result, 2456, 2594);
    
    _putDontNest(result, 2456, 2616);
    
    _putDontNest(result, 2456, 2638);
    
    _putDontNest(result, 2456, 2656);
    
    _putDontNest(result, 2456, 2674);
    
    _putDontNest(result, 2456, 2696);
    
    _putDontNest(result, 2456, 2714);
    
    _putDontNest(result, 2456, 2732);
    
    _putDontNest(result, 2456, 2754);
    
    _putDontNest(result, 2456, 2790);
    
    _putDontNest(result, 2456, 2812);
    
    _putDontNest(result, 2456, 2830);
    
    _putDontNest(result, 2456, 2848);
    
    _putDontNest(result, 2456, 2866);
    
    _putDontNest(result, 2456, 2894);
    
    _putDontNest(result, 2464, 2554);
    
    _putDontNest(result, 2464, 2576);
    
    _putDontNest(result, 2464, 2594);
    
    _putDontNest(result, 2464, 2616);
    
    _putDontNest(result, 2464, 2638);
    
    _putDontNest(result, 2464, 2656);
    
    _putDontNest(result, 2464, 2674);
    
    _putDontNest(result, 2464, 2696);
    
    _putDontNest(result, 2464, 2714);
    
    _putDontNest(result, 2464, 2732);
    
    _putDontNest(result, 2464, 2754);
    
    _putDontNest(result, 2464, 2772);
    
    _putDontNest(result, 2464, 2790);
    
    _putDontNest(result, 2464, 2812);
    
    _putDontNest(result, 2464, 2830);
    
    _putDontNest(result, 2464, 2848);
    
    _putDontNest(result, 2464, 2866);
    
    _putDontNest(result, 2464, 2894);
    
    _putDontNest(result, 2478, 2456);
    
    _putDontNest(result, 2478, 2478);
    
    _putDontNest(result, 2478, 2496);
    
    _putDontNest(result, 2478, 2516);
    
    _putDontNest(result, 2478, 2536);
    
    _putDontNest(result, 2478, 2554);
    
    _putDontNest(result, 2478, 2576);
    
    _putDontNest(result, 2478, 2594);
    
    _putDontNest(result, 2478, 2616);
    
    _putDontNest(result, 2478, 2638);
    
    _putDontNest(result, 2478, 2656);
    
    _putDontNest(result, 2478, 2674);
    
    _putDontNest(result, 2478, 2696);
    
    _putDontNest(result, 2478, 2714);
    
    _putDontNest(result, 2478, 2732);
    
    _putDontNest(result, 2478, 2812);
    
    _putDontNest(result, 2478, 2830);
    
    _putDontNest(result, 2478, 2848);
    
    _putDontNest(result, 2478, 2866);
    
    _putDontNest(result, 2478, 2894);
    
    _putDontNest(result, 2486, 2554);
    
    _putDontNest(result, 2486, 2576);
    
    _putDontNest(result, 2486, 2594);
    
    _putDontNest(result, 2486, 2616);
    
    _putDontNest(result, 2486, 2638);
    
    _putDontNest(result, 2486, 2656);
    
    _putDontNest(result, 2486, 2674);
    
    _putDontNest(result, 2486, 2696);
    
    _putDontNest(result, 2486, 2714);
    
    _putDontNest(result, 2486, 2732);
    
    _putDontNest(result, 2486, 2754);
    
    _putDontNest(result, 2486, 2772);
    
    _putDontNest(result, 2486, 2790);
    
    _putDontNest(result, 2486, 2812);
    
    _putDontNest(result, 2486, 2830);
    
    _putDontNest(result, 2486, 2848);
    
    _putDontNest(result, 2486, 2866);
    
    _putDontNest(result, 2486, 2894);
    
    _putDontNest(result, 2496, 2456);
    
    _putDontNest(result, 2496, 2478);
    
    _putDontNest(result, 2496, 2496);
    
    _putDontNest(result, 2496, 2516);
    
    _putDontNest(result, 2496, 2536);
    
    _putDontNest(result, 2496, 2554);
    
    _putDontNest(result, 2496, 2576);
    
    _putDontNest(result, 2496, 2594);
    
    _putDontNest(result, 2496, 2616);
    
    _putDontNest(result, 2496, 2638);
    
    _putDontNest(result, 2496, 2656);
    
    _putDontNest(result, 2496, 2674);
    
    _putDontNest(result, 2496, 2696);
    
    _putDontNest(result, 2496, 2714);
    
    _putDontNest(result, 2496, 2732);
    
    _putDontNest(result, 2496, 2812);
    
    _putDontNest(result, 2496, 2830);
    
    _putDontNest(result, 2496, 2848);
    
    _putDontNest(result, 2496, 2866);
    
    _putDontNest(result, 2496, 2894);
    
    _putDontNest(result, 2504, 2554);
    
    _putDontNest(result, 2504, 2576);
    
    _putDontNest(result, 2504, 2594);
    
    _putDontNest(result, 2504, 2616);
    
    _putDontNest(result, 2504, 2638);
    
    _putDontNest(result, 2504, 2656);
    
    _putDontNest(result, 2504, 2674);
    
    _putDontNest(result, 2504, 2696);
    
    _putDontNest(result, 2504, 2714);
    
    _putDontNest(result, 2504, 2732);
    
    _putDontNest(result, 2504, 2754);
    
    _putDontNest(result, 2504, 2772);
    
    _putDontNest(result, 2504, 2790);
    
    _putDontNest(result, 2504, 2812);
    
    _putDontNest(result, 2504, 2830);
    
    _putDontNest(result, 2504, 2848);
    
    _putDontNest(result, 2504, 2866);
    
    _putDontNest(result, 2504, 2894);
    
    _putDontNest(result, 2516, 2456);
    
    _putDontNest(result, 2516, 2478);
    
    _putDontNest(result, 2516, 2496);
    
    _putDontNest(result, 2516, 2516);
    
    _putDontNest(result, 2516, 2536);
    
    _putDontNest(result, 2516, 2554);
    
    _putDontNest(result, 2516, 2576);
    
    _putDontNest(result, 2516, 2594);
    
    _putDontNest(result, 2516, 2616);
    
    _putDontNest(result, 2516, 2638);
    
    _putDontNest(result, 2516, 2656);
    
    _putDontNest(result, 2516, 2674);
    
    _putDontNest(result, 2516, 2696);
    
    _putDontNest(result, 2516, 2714);
    
    _putDontNest(result, 2516, 2732);
    
    _putDontNest(result, 2516, 2754);
    
    _putDontNest(result, 2516, 2790);
    
    _putDontNest(result, 2516, 2812);
    
    _putDontNest(result, 2516, 2830);
    
    _putDontNest(result, 2516, 2848);
    
    _putDontNest(result, 2516, 2866);
    
    _putDontNest(result, 2516, 2894);
    
    _putDontNest(result, 2526, 1710);
    
    _putDontNest(result, 2526, 1770);
    
    _putDontNest(result, 2526, 2554);
    
    _putDontNest(result, 2526, 2576);
    
    _putDontNest(result, 2526, 2594);
    
    _putDontNest(result, 2526, 2616);
    
    _putDontNest(result, 2526, 2638);
    
    _putDontNest(result, 2526, 2656);
    
    _putDontNest(result, 2526, 2674);
    
    _putDontNest(result, 2526, 2696);
    
    _putDontNest(result, 2526, 2714);
    
    _putDontNest(result, 2526, 2732);
    
    _putDontNest(result, 2526, 2754);
    
    _putDontNest(result, 2526, 2772);
    
    _putDontNest(result, 2526, 2790);
    
    _putDontNest(result, 2526, 2812);
    
    _putDontNest(result, 2526, 2830);
    
    _putDontNest(result, 2526, 2848);
    
    _putDontNest(result, 2526, 2866);
    
    _putDontNest(result, 2526, 2894);
    
    _putDontNest(result, 2536, 2456);
    
    _putDontNest(result, 2536, 2478);
    
    _putDontNest(result, 2536, 2496);
    
    _putDontNest(result, 2536, 2516);
    
    _putDontNest(result, 2536, 2536);
    
    _putDontNest(result, 2536, 2554);
    
    _putDontNest(result, 2536, 2576);
    
    _putDontNest(result, 2536, 2594);
    
    _putDontNest(result, 2536, 2616);
    
    _putDontNest(result, 2536, 2638);
    
    _putDontNest(result, 2536, 2656);
    
    _putDontNest(result, 2536, 2674);
    
    _putDontNest(result, 2536, 2696);
    
    _putDontNest(result, 2536, 2714);
    
    _putDontNest(result, 2536, 2732);
    
    _putDontNest(result, 2536, 2812);
    
    _putDontNest(result, 2536, 2830);
    
    _putDontNest(result, 2536, 2848);
    
    _putDontNest(result, 2536, 2866);
    
    _putDontNest(result, 2536, 2894);
    
    _putDontNest(result, 2544, 2576);
    
    _putDontNest(result, 2544, 2594);
    
    _putDontNest(result, 2544, 2616);
    
    _putDontNest(result, 2544, 2638);
    
    _putDontNest(result, 2544, 2656);
    
    _putDontNest(result, 2544, 2674);
    
    _putDontNest(result, 2544, 2696);
    
    _putDontNest(result, 2544, 2714);
    
    _putDontNest(result, 2544, 2732);
    
    _putDontNest(result, 2544, 2754);
    
    _putDontNest(result, 2544, 2772);
    
    _putDontNest(result, 2544, 2790);
    
    _putDontNest(result, 2544, 2812);
    
    _putDontNest(result, 2544, 2830);
    
    _putDontNest(result, 2544, 2848);
    
    _putDontNest(result, 2544, 2866);
    
    _putDontNest(result, 2544, 2894);
    
    _putDontNest(result, 2554, 2554);
    
    _putDontNest(result, 2554, 2576);
    
    _putDontNest(result, 2554, 2594);
    
    _putDontNest(result, 2554, 2616);
    
    _putDontNest(result, 2554, 2638);
    
    _putDontNest(result, 2554, 2656);
    
    _putDontNest(result, 2554, 2674);
    
    _putDontNest(result, 2554, 2696);
    
    _putDontNest(result, 2554, 2714);
    
    _putDontNest(result, 2554, 2732);
    
    _putDontNest(result, 2554, 2812);
    
    _putDontNest(result, 2554, 2830);
    
    _putDontNest(result, 2554, 2848);
    
    _putDontNest(result, 2554, 2866);
    
    _putDontNest(result, 2554, 2894);
    
    _putDontNest(result, 2566, 2576);
    
    _putDontNest(result, 2566, 2594);
    
    _putDontNest(result, 2566, 2616);
    
    _putDontNest(result, 2566, 2638);
    
    _putDontNest(result, 2566, 2656);
    
    _putDontNest(result, 2566, 2674);
    
    _putDontNest(result, 2566, 2696);
    
    _putDontNest(result, 2566, 2714);
    
    _putDontNest(result, 2566, 2732);
    
    _putDontNest(result, 2566, 2754);
    
    _putDontNest(result, 2566, 2772);
    
    _putDontNest(result, 2566, 2790);
    
    _putDontNest(result, 2566, 2812);
    
    _putDontNest(result, 2566, 2830);
    
    _putDontNest(result, 2566, 2848);
    
    _putDontNest(result, 2566, 2866);
    
    _putDontNest(result, 2566, 2894);
    
    _putDontNest(result, 2576, 2576);
    
    _putDontNest(result, 2576, 2594);
    
    _putDontNest(result, 2576, 2616);
    
    _putDontNest(result, 2576, 2638);
    
    _putDontNest(result, 2576, 2656);
    
    _putDontNest(result, 2576, 2674);
    
    _putDontNest(result, 2576, 2696);
    
    _putDontNest(result, 2576, 2714);
    
    _putDontNest(result, 2576, 2732);
    
    _putDontNest(result, 2576, 2812);
    
    _putDontNest(result, 2576, 2830);
    
    _putDontNest(result, 2576, 2848);
    
    _putDontNest(result, 2576, 2866);
    
    _putDontNest(result, 2576, 2894);
    
    _putDontNest(result, 2584, 2576);
    
    _putDontNest(result, 2584, 2594);
    
    _putDontNest(result, 2584, 2616);
    
    _putDontNest(result, 2584, 2638);
    
    _putDontNest(result, 2584, 2656);
    
    _putDontNest(result, 2584, 2674);
    
    _putDontNest(result, 2584, 2696);
    
    _putDontNest(result, 2584, 2714);
    
    _putDontNest(result, 2584, 2732);
    
    _putDontNest(result, 2584, 2754);
    
    _putDontNest(result, 2584, 2772);
    
    _putDontNest(result, 2584, 2790);
    
    _putDontNest(result, 2584, 2812);
    
    _putDontNest(result, 2584, 2830);
    
    _putDontNest(result, 2584, 2848);
    
    _putDontNest(result, 2584, 2866);
    
    _putDontNest(result, 2584, 2894);
    
    _putDontNest(result, 2594, 2576);
    
    _putDontNest(result, 2594, 2594);
    
    _putDontNest(result, 2594, 2616);
    
    _putDontNest(result, 2594, 2638);
    
    _putDontNest(result, 2594, 2656);
    
    _putDontNest(result, 2594, 2674);
    
    _putDontNest(result, 2594, 2696);
    
    _putDontNest(result, 2594, 2714);
    
    _putDontNest(result, 2594, 2732);
    
    _putDontNest(result, 2594, 2812);
    
    _putDontNest(result, 2594, 2830);
    
    _putDontNest(result, 2594, 2848);
    
    _putDontNest(result, 2594, 2866);
    
    _putDontNest(result, 2594, 2894);
    
    _putDontNest(result, 2606, 2616);
    
    _putDontNest(result, 2606, 2638);
    
    _putDontNest(result, 2606, 2656);
    
    _putDontNest(result, 2606, 2674);
    
    _putDontNest(result, 2606, 2696);
    
    _putDontNest(result, 2606, 2714);
    
    _putDontNest(result, 2606, 2732);
    
    _putDontNest(result, 2606, 2754);
    
    _putDontNest(result, 2606, 2772);
    
    _putDontNest(result, 2606, 2790);
    
    _putDontNest(result, 2606, 2812);
    
    _putDontNest(result, 2606, 2830);
    
    _putDontNest(result, 2606, 2848);
    
    _putDontNest(result, 2606, 2866);
    
    _putDontNest(result, 2606, 2894);
    
    _putDontNest(result, 2616, 2616);
    
    _putDontNest(result, 2616, 2638);
    
    _putDontNest(result, 2616, 2656);
    
    _putDontNest(result, 2616, 2674);
    
    _putDontNest(result, 2616, 2696);
    
    _putDontNest(result, 2616, 2714);
    
    _putDontNest(result, 2616, 2732);
    
    _putDontNest(result, 2616, 2812);
    
    _putDontNest(result, 2616, 2830);
    
    _putDontNest(result, 2616, 2848);
    
    _putDontNest(result, 2616, 2866);
    
    _putDontNest(result, 2616, 2894);
    
    _putDontNest(result, 2624, 2616);
    
    _putDontNest(result, 2624, 2638);
    
    _putDontNest(result, 2624, 2656);
    
    _putDontNest(result, 2624, 2674);
    
    _putDontNest(result, 2624, 2696);
    
    _putDontNest(result, 2624, 2714);
    
    _putDontNest(result, 2624, 2732);
    
    _putDontNest(result, 2624, 2754);
    
    _putDontNest(result, 2624, 2772);
    
    _putDontNest(result, 2624, 2790);
    
    _putDontNest(result, 2624, 2812);
    
    _putDontNest(result, 2624, 2830);
    
    _putDontNest(result, 2624, 2848);
    
    _putDontNest(result, 2624, 2866);
    
    _putDontNest(result, 2624, 2894);
    
    _putDontNest(result, 2638, 2616);
    
    _putDontNest(result, 2638, 2638);
    
    _putDontNest(result, 2638, 2656);
    
    _putDontNest(result, 2638, 2674);
    
    _putDontNest(result, 2638, 2696);
    
    _putDontNest(result, 2638, 2714);
    
    _putDontNest(result, 2638, 2732);
    
    _putDontNest(result, 2638, 2812);
    
    _putDontNest(result, 2638, 2830);
    
    _putDontNest(result, 2638, 2848);
    
    _putDontNest(result, 2638, 2866);
    
    _putDontNest(result, 2638, 2894);
    
    _putDontNest(result, 2646, 2616);
    
    _putDontNest(result, 2646, 2638);
    
    _putDontNest(result, 2646, 2656);
    
    _putDontNest(result, 2646, 2674);
    
    _putDontNest(result, 2646, 2696);
    
    _putDontNest(result, 2646, 2714);
    
    _putDontNest(result, 2646, 2732);
    
    _putDontNest(result, 2646, 2754);
    
    _putDontNest(result, 2646, 2772);
    
    _putDontNest(result, 2646, 2790);
    
    _putDontNest(result, 2646, 2812);
    
    _putDontNest(result, 2646, 2830);
    
    _putDontNest(result, 2646, 2848);
    
    _putDontNest(result, 2646, 2866);
    
    _putDontNest(result, 2646, 2894);
    
    _putDontNest(result, 2656, 2616);
    
    _putDontNest(result, 2656, 2638);
    
    _putDontNest(result, 2656, 2656);
    
    _putDontNest(result, 2656, 2674);
    
    _putDontNest(result, 2656, 2696);
    
    _putDontNest(result, 2656, 2714);
    
    _putDontNest(result, 2656, 2732);
    
    _putDontNest(result, 2656, 2812);
    
    _putDontNest(result, 2656, 2830);
    
    _putDontNest(result, 2656, 2848);
    
    _putDontNest(result, 2656, 2866);
    
    _putDontNest(result, 2656, 2894);
    
    _putDontNest(result, 2664, 2616);
    
    _putDontNest(result, 2664, 2638);
    
    _putDontNest(result, 2664, 2656);
    
    _putDontNest(result, 2664, 2674);
    
    _putDontNest(result, 2664, 2696);
    
    _putDontNest(result, 2664, 2714);
    
    _putDontNest(result, 2664, 2732);
    
    _putDontNest(result, 2664, 2754);
    
    _putDontNest(result, 2664, 2772);
    
    _putDontNest(result, 2664, 2790);
    
    _putDontNest(result, 2664, 2812);
    
    _putDontNest(result, 2664, 2830);
    
    _putDontNest(result, 2664, 2848);
    
    _putDontNest(result, 2664, 2866);
    
    _putDontNest(result, 2664, 2894);
    
    _putDontNest(result, 2674, 2616);
    
    _putDontNest(result, 2674, 2638);
    
    _putDontNest(result, 2674, 2656);
    
    _putDontNest(result, 2674, 2674);
    
    _putDontNest(result, 2674, 2696);
    
    _putDontNest(result, 2674, 2714);
    
    _putDontNest(result, 2674, 2732);
    
    _putDontNest(result, 2674, 2812);
    
    _putDontNest(result, 2674, 2830);
    
    _putDontNest(result, 2674, 2848);
    
    _putDontNest(result, 2674, 2866);
    
    _putDontNest(result, 2674, 2894);
    
    _putDontNest(result, 2686, 2696);
    
    _putDontNest(result, 2686, 2714);
    
    _putDontNest(result, 2686, 2732);
    
    _putDontNest(result, 2686, 2754);
    
    _putDontNest(result, 2686, 2772);
    
    _putDontNest(result, 2686, 2790);
    
    _putDontNest(result, 2686, 2812);
    
    _putDontNest(result, 2686, 2830);
    
    _putDontNest(result, 2686, 2848);
    
    _putDontNest(result, 2686, 2866);
    
    _putDontNest(result, 2686, 2894);
    
    _putDontNest(result, 2696, 2696);
    
    _putDontNest(result, 2696, 2714);
    
    _putDontNest(result, 2696, 2732);
    
    _putDontNest(result, 2696, 2812);
    
    _putDontNest(result, 2696, 2830);
    
    _putDontNest(result, 2696, 2848);
    
    _putDontNest(result, 2696, 2866);
    
    _putDontNest(result, 2696, 2894);
    
    _putDontNest(result, 2704, 2696);
    
    _putDontNest(result, 2704, 2714);
    
    _putDontNest(result, 2704, 2732);
    
    _putDontNest(result, 2704, 2754);
    
    _putDontNest(result, 2704, 2772);
    
    _putDontNest(result, 2704, 2790);
    
    _putDontNest(result, 2704, 2812);
    
    _putDontNest(result, 2704, 2830);
    
    _putDontNest(result, 2704, 2848);
    
    _putDontNest(result, 2704, 2866);
    
    _putDontNest(result, 2704, 2894);
    
    _putDontNest(result, 2714, 2696);
    
    _putDontNest(result, 2714, 2714);
    
    _putDontNest(result, 2714, 2732);
    
    _putDontNest(result, 2714, 2812);
    
    _putDontNest(result, 2714, 2830);
    
    _putDontNest(result, 2714, 2848);
    
    _putDontNest(result, 2714, 2866);
    
    _putDontNest(result, 2714, 2894);
    
    _putDontNest(result, 2722, 2732);
    
    _putDontNest(result, 2722, 2754);
    
    _putDontNest(result, 2722, 2772);
    
    _putDontNest(result, 2722, 2790);
    
    _putDontNest(result, 2722, 2812);
    
    _putDontNest(result, 2722, 2830);
    
    _putDontNest(result, 2722, 2848);
    
    _putDontNest(result, 2722, 2866);
    
    _putDontNest(result, 2722, 2894);
    
    _putDontNest(result, 2732, 2732);
    
    _putDontNest(result, 2732, 2812);
    
    _putDontNest(result, 2732, 2830);
    
    _putDontNest(result, 2732, 2848);
    
    _putDontNest(result, 2732, 2866);
    
    _putDontNest(result, 2732, 2894);
    
    _putDontNest(result, 2754, 2754);
    
    _putDontNest(result, 2754, 2772);
    
    _putDontNest(result, 2754, 2790);
    
    _putDontNest(result, 2754, 2812);
    
    _putDontNest(result, 2754, 2830);
    
    _putDontNest(result, 2754, 2848);
    
    _putDontNest(result, 2754, 2866);
    
    _putDontNest(result, 2754, 2894);
    
    _putDontNest(result, 2772, 2754);
    
    _putDontNest(result, 2772, 2772);
    
    _putDontNest(result, 2772, 2790);
    
    _putDontNest(result, 2772, 2812);
    
    _putDontNest(result, 2772, 2830);
    
    _putDontNest(result, 2772, 2848);
    
    _putDontNest(result, 2772, 2866);
    
    _putDontNest(result, 2772, 2894);
    
    _putDontNest(result, 2790, 2754);
    
    _putDontNest(result, 2790, 2772);
    
    _putDontNest(result, 2790, 2790);
    
    _putDontNest(result, 2790, 2812);
    
    _putDontNest(result, 2790, 2830);
    
    _putDontNest(result, 2790, 2848);
    
    _putDontNest(result, 2790, 2866);
    
    _putDontNest(result, 2790, 2894);
    
    _putDontNest(result, 2802, 2812);
    
    _putDontNest(result, 2802, 2830);
    
    _putDontNest(result, 2802, 2848);
    
    _putDontNest(result, 2802, 2866);
    
    _putDontNest(result, 2802, 2894);
    
    _putDontNest(result, 2812, 2812);
    
    _putDontNest(result, 2812, 2830);
    
    _putDontNest(result, 2812, 2848);
    
    _putDontNest(result, 2812, 2866);
    
    _putDontNest(result, 2812, 2894);
    
    _putDontNest(result, 2820, 2812);
    
    _putDontNest(result, 2820, 2830);
    
    _putDontNest(result, 2820, 2848);
    
    _putDontNest(result, 2820, 2866);
    
    _putDontNest(result, 2820, 2894);
    
    _putDontNest(result, 2830, 2812);
    
    _putDontNest(result, 2830, 2830);
    
    _putDontNest(result, 2830, 2848);
    
    _putDontNest(result, 2830, 2866);
    
    _putDontNest(result, 2830, 2894);
    
    _putDontNest(result, 2838, 2866);
    
    _putDontNest(result, 2838, 2894);
    
    _putDontNest(result, 2848, 2848);
    
    _putDontNest(result, 2848, 2866);
    
    _putDontNest(result, 2848, 2894);
    
    _putDontNest(result, 2856, 2894);
    
    _putDontNest(result, 2866, 2866);
    
    _putDontNest(result, 2866, 2894);
    
    _putDontNest(result, 2874, 2894);
    
    _putDontNest(result, 3530, 9336);
    
    _putDontNest(result, 3530, 9580);
    
    _putDontNest(result, 3530, 9830);
    
    _putDontNest(result, 8628, 9336);
    
    _putDontNest(result, 8628, 9580);
    
    _putDontNest(result, 8628, 9830);
    
    _putDontNest(result, 8646, 1984);
    
    _putDontNest(result, 9246, 9246);
    
    _putDontNest(result, 9246, 9260);
    
    _putDontNest(result, 9246, 9280);
    
    _putDontNest(result, 9246, 9300);
    
    _putDontNest(result, 9246, 9336);
    
    _putDontNest(result, 9246, 9580);
    
    _putDontNest(result, 9260, 9246);
    
    _putDontNest(result, 9260, 9260);
    
    _putDontNest(result, 9260, 9280);
    
    _putDontNest(result, 9260, 9300);
    
    _putDontNest(result, 9260, 9336);
    
    _putDontNest(result, 9260, 9580);
    
    _putDontNest(result, 9280, 9246);
    
    _putDontNest(result, 9280, 9260);
    
    _putDontNest(result, 9280, 9280);
    
    _putDontNest(result, 9280, 9300);
    
    _putDontNest(result, 9280, 9336);
    
    _putDontNest(result, 9280, 9580);
    
    _putDontNest(result, 9300, 9246);
    
    _putDontNest(result, 9300, 9260);
    
    _putDontNest(result, 9300, 9280);
    
    _putDontNest(result, 9300, 9300);
    
    _putDontNest(result, 9300, 9336);
    
    _putDontNest(result, 9300, 9580);
    
    _putDontNest(result, 9322, 9336);
    
    _putDontNest(result, 9322, 9580);
    
    _putDontNest(result, 9376, 9336);
    
    _putDontNest(result, 9376, 9580);
    
    _putDontNest(result, 9496, 9336);
    
    _putDontNest(result, 9496, 9580);
    
    _putDontNest(result, 9674, 9336);
    
    _putDontNest(result, 9674, 9580);
    
    _putDontNest(result, 9684, 1984);
    
    _putDontNest(result, 9684, 2088);
    
    _putDontNest(result, 9760, 9336);
    
    _putDontNest(result, 9760, 9580);
    
    _putDontNest(result, 9888, 9336);
    
    _putDontNest(result, 9888, 9580);
    
    _putDontNest(result, 10220, 662);
    
    _putDontNest(result, 10220, 674);
    
    _putDontNest(result, 10220, 778);
    
    _putDontNest(result, 10220, 860);
    
    _putDontNest(result, 10548, 10586);
    
    _putDontNest(result, 10548, 10604);
    
    _putDontNest(result, 10548, 10640);
    
    _putDontNest(result, 10576, 10604);
    
    _putDontNest(result, 10576, 10640);
    
    _putDontNest(result, 10586, 10586);
    
    _putDontNest(result, 10586, 10604);
    
    _putDontNest(result, 10586, 10640);
    
    _putDontNest(result, 10594, 10640);
    
    _putDontNest(result, 10604, 10604);
    
    _putDontNest(result, 10604, 10640);
    
    _putDontNest(result, 10640, 10640);
    
    _putDontNest(result, 10678, 2732);
    
    _putDontNest(result, 11254, 11286);
    
    _putDontNest(result, 11264, 11264);
    
    _putDontNest(result, 11264, 11286);
    
    _putDontNest(result, 11286, 11286);
    
    _putDontNest(result, 11556, 11614);
    
    _putDontNest(result, 11556, 11626);
    
    _putDontNest(result, 11556, 11644);
    
    _putDontNest(result, 11556, 11656);
    
    _putDontNest(result, 11556, 11680);
   return result;
  }
    
  protected static IntegerMap _initDontNestGroups() {
    IntegerMap result = org.rascalmpl.library.lang.rascal.syntax.RascalRascal._initDontNestGroups();
    int resultStoreId = result.size();
    
    
    ++resultStoreId;
    
    result.putUnsafe(8646, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2076, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(10678, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2856, resultStoreId);
    result.putUnsafe(2874, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(11254, resultStoreId);
    result.putUnsafe(11286, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(10594, resultStoreId);
    result.putUnsafe(10640, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(936, resultStoreId);
    result.putUnsafe(954, resultStoreId);
    result.putUnsafe(1022, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(9684, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(11264, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(10576, resultStoreId);
    result.putUnsafe(10604, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2838, resultStoreId);
    result.putUnsafe(2866, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(9322, resultStoreId);
    result.putUnsafe(9376, resultStoreId);
    result.putUnsafe(9496, resultStoreId);
    result.putUnsafe(9674, resultStoreId);
    result.putUnsafe(9760, resultStoreId);
    result.putUnsafe(9888, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2848, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(976, resultStoreId);
    result.putUnsafe(994, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(10548, resultStoreId);
    result.putUnsafe(10586, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(3530, resultStoreId);
    result.putUnsafe(8628, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(946, resultStoreId);
    result.putUnsafe(964, resultStoreId);
    result.putUnsafe(986, resultStoreId);
    result.putUnsafe(1004, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(10220, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(11556, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2802, resultStoreId);
    result.putUnsafe(2812, resultStoreId);
    result.putUnsafe(2820, resultStoreId);
    result.putUnsafe(2830, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(644, resultStoreId);
    result.putUnsafe(656, resultStoreId);
    result.putUnsafe(742, resultStoreId);
    result.putUnsafe(754, resultStoreId);
    result.putUnsafe(784, resultStoreId);
    result.putUnsafe(896, resultStoreId);
    result.putUnsafe(904, resultStoreId);
    result.putUnsafe(916, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2732, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(9246, resultStoreId);
    result.putUnsafe(9260, resultStoreId);
    result.putUnsafe(9280, resultStoreId);
    result.putUnsafe(9300, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2696, resultStoreId);
    result.putUnsafe(2714, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2754, resultStoreId);
    result.putUnsafe(2772, resultStoreId);
    result.putUnsafe(2790, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2722, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2686, resultStoreId);
    result.putUnsafe(2704, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2616, resultStoreId);
    result.putUnsafe(2638, resultStoreId);
    result.putUnsafe(2656, resultStoreId);
    result.putUnsafe(2674, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2576, resultStoreId);
    result.putUnsafe(2594, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2554, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2606, resultStoreId);
    result.putUnsafe(2624, resultStoreId);
    result.putUnsafe(2646, resultStoreId);
    result.putUnsafe(2664, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2544, resultStoreId);
    result.putUnsafe(2566, resultStoreId);
    result.putUnsafe(2584, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2444, resultStoreId);
    result.putUnsafe(2464, resultStoreId);
    result.putUnsafe(2486, resultStoreId);
    result.putUnsafe(2504, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2526, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2478, resultStoreId);
    result.putUnsafe(2496, resultStoreId);
    result.putUnsafe(2536, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2432, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2456, resultStoreId);
    result.putUnsafe(2516, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2418, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2336, resultStoreId);
    result.putUnsafe(2354, resultStoreId);
    result.putUnsafe(2382, resultStoreId);
    result.putUnsafe(2400, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2346, resultStoreId);
    result.putUnsafe(2392, resultStoreId);
    result.putUnsafe(2410, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2374, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2234, resultStoreId);
    result.putUnsafe(2246, resultStoreId);
    result.putUnsafe(2306, resultStoreId);
    result.putUnsafe(2324, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2296, resultStoreId);
    result.putUnsafe(2314, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(2270, resultStoreId);
    result.putUnsafe(2284, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1498, resultStoreId);
    result.putUnsafe(1682, resultStoreId);
    result.putUnsafe(1700, resultStoreId);
    result.putUnsafe(1742, resultStoreId);
    result.putUnsafe(1760, resultStoreId);
    result.putUnsafe(1784, resultStoreId);
    result.putUnsafe(1802, resultStoreId);
    result.putUnsafe(1844, resultStoreId);
    result.putUnsafe(2216, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1610, resultStoreId);
    result.putUnsafe(1912, resultStoreId);
    result.putUnsafe(2186, resultStoreId);
      
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
	
  private static final IConstructor prod__empty__ = (IConstructor) _read("prod(empty(),[],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_extend_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"extend\")],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_import_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"import\")],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_keyword_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"keyword\")],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_layout_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"layout\")],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_lexical_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"lexical\")],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_start_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"start\")],{})", Factory.Production);
  private static final IConstructor prod__HeaderKeyword__lit_syntax_ = (IConstructor) _read("prod(keywords(\"HeaderKeyword\"),[lit(\"syntax\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_alias_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"alias\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_all_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"all\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_anno_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"anno\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_any_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"any\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_append_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"append\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_assert_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"assert\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_assoc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"assoc\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_bag_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bag\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_bool_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bool\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_bracket_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"bracket\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_break_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"break\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_case_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"case\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_catch_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"catch\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_continue_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"continue\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_data_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"data\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_datetime_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"datetime\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_default_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"default\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_dynamic_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"dynamic\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_else_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"else\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_extend_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"extend\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_fail_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"fail\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_false_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"false\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_filter_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"filter\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_finally_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"finally\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_for_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"for\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_if_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"if\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_import_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"import\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_in_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"in\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_insert_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"insert\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_int_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"int\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_it_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"it\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_join_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"join\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_layout_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"layout\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_list_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"list\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_loc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"loc\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_map_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"map\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_mod_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"mod\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_module_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"module\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_node_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"node\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_non_assoc_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"non-assoc\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_notin_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"notin\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_num_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"num\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_o_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"o\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_one_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"one\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_private_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"private\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_public_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"public\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_rat_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"rat\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_real_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"real\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_rel_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"rel\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_return_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"return\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_set_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"set\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_solve_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"solve\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_start_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"start\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_str_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"str\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_switch_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"switch\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_tag_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"tag\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_test_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_throw_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"throw\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_throws_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"throws\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_true_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"true\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_try_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"try\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_tuple_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"tuple\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_type_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"type\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_value_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"value\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_visit_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"visit\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_void_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__lit_while_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[lit(\"while\")],{})", Factory.Production);
  private static final IConstructor prod__RascalKeywords__BasicType_ = (IConstructor) _read("prod(keywords(\"RascalKeywords\"),[sort(\"BasicType\")],{})", Factory.Production);
  private static final IConstructor prod__absent_Start__ = (IConstructor) _read("prod(label(\"absent\",sort(\"Start\")),[],{})", Factory.Production);
  private static final IConstructor prod__abstract_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"abstract\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__actuals_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_ = (IConstructor) _read("prod(label(\"actuals\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"actuals\",sort(\"ModuleActuals\"))],{})", Factory.Production);
  private static final IConstructor prod__actualsRenaming_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_layouts_LAYOUTLIST_renamings_Renamings_ = (IConstructor) _read("prod(label(\"actualsRenaming\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"actuals\",sort(\"ModuleActuals\")),layouts(\"LAYOUTLIST\"),label(\"renamings\",sort(\"Renamings\"))],{})", Factory.Production);
  private static final IConstructor prod__addition_Assignment__lit___43_61_ = (IConstructor) _read("prod(label(\"addition\",sort(\"Assignment\")),[lit(\"+=\")],{})", Factory.Production);
  private static final IConstructor prod__addition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___43_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"addition\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"+\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",conditional(sort(\"Expression\"),{except(\"noMatch\"),except(\"match\")}))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__alias_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_alias_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_base_Type_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"alias\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"alias\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"base\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__alias_Kind__lit_alias_ = (IConstructor) _read("prod(label(\"alias\",sort(\"Kind\")),[lit(\"alias\")],{})", Factory.Production);
  private static final IConstructor prod__all_Expression__lit_all_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"all\",sort(\"Expression\")),[lit(\"all\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__all_Kind__lit_all_ = (IConstructor) _read("prod(label(\"all\",sort(\"Kind\")),[lit(\"all\")],{})", Factory.Production);
  private static final IConstructor prod__all_Prod__lhs_Prod_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_rhs_Prod__assoc__left = (IConstructor) _read("prod(label(\"all\",sort(\"Prod\")),[label(\"lhs\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Prod\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__alternative_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_alternatives_iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"alternative\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"alternatives\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__and_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"and\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"&&\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__anno_Kind__lit_anno_ = (IConstructor) _read("prod(label(\"anno\",sort(\"Kind\")),[lit(\"anno\")],{})", Factory.Production);
  private static final IConstructor prod__annotation_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_annotation_Name_ = (IConstructor) _read("prod(label(\"annotation\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"annotation\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__annotation_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_anno_layouts_LAYOUTLIST_annoType_Type_layouts_LAYOUTLIST_onType_Type_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"annotation\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"anno\"),layouts(\"LAYOUTLIST\"),label(\"annoType\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"onType\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__anti_Pattern__lit___33_layouts_LAYOUTLIST_pattern_Pattern_ = (IConstructor) _read("prod(label(\"anti\",sort(\"Pattern\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__any_Expression__lit_any_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"any\",sort(\"Expression\")),[lit(\"any\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__append_Assignment__lit___60_60_61_ = (IConstructor) _read("prod(label(\"append\",sort(\"Assignment\")),[lit(\"\\<\\<=\")],{})", Factory.Production);
  private static final IConstructor prod__append_Statement__lit_append_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable = (IConstructor) _read("prod(label(\"append\",sort(\"Statement\")),[lit(\"append\"),layouts(\"LAYOUTLIST\"),label(\"dataTarget\",sort(\"DataTarget\")),layouts(\"LAYOUTLIST\"),label(\"statement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{assoc(\\non-assoc()),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__appendAfter_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"appendAfter\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\<\\<\"),{\\not-follow(lit(\"=\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__arbitrary_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement_ = (IConstructor) _read("prod(label(\"arbitrary\",sort(\"PatternWithAction\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__asType_Expression__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Expression_ = (IConstructor) _read("prod(label(\"asType\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"]\"),layouts(\"LAYOUTLIST\"),label(\"argument\",conditional(sort(\"Expression\"),{except(\"noMatch\"),except(\"match\")}))],{})", Factory.Production);
  private static final IConstructor prod__asType_Pattern__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Pattern_ = (IConstructor) _read("prod(label(\"asType\",sort(\"Pattern\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"]\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__ascii_UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(label(\"ascii\",lex(\"UnicodeEscape\")),[lit(\"\\\\\"),\\char-class([range(97,97)]),\\char-class([range(48,55)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__assert_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"assert\",sort(\"Statement\")),[lit(\"assert\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__assertWithMessage_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_message_Expression_layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"assertWithMessage\",sort(\"Statement\")),[lit(\"assert\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"message\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__assignment_Statement__assignable_Assignable_layouts_LAYOUTLIST_operator_Assignment_layouts_LAYOUTLIST_statement_Statement__tag__breakable = (IConstructor) _read("prod(label(\"assignment\",sort(\"Statement\")),[label(\"assignable\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),label(\"operator\",sort(\"Assignment\")),layouts(\"LAYOUTLIST\"),label(\"statement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__associative_Assoc__lit_assoc_ = (IConstructor) _read("prod(label(\"associative\",sort(\"Assoc\")),[lit(\"assoc\")],{})", Factory.Production);
  private static final IConstructor prod__associativity_ProdModifier__associativity_Assoc_ = (IConstructor) _read("prod(label(\"associativity\",sort(\"ProdModifier\")),[label(\"associativity\",sort(\"Assoc\"))],{})", Factory.Production);
  private static final IConstructor prod__associativityGroup_Prod__associativity_Assoc_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_group_Prod_layouts_LAYOUTLIST_lit___41__tag__Foldable = (IConstructor) _read("prod(label(\"associativityGroup\",sort(\"Prod\")),[label(\"associativity\",sort(\"Assoc\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"group\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__bag_BasicType__lit_bag_ = (IConstructor) _read("prod(label(\"bag\",sort(\"BasicType\")),[lit(\"bag\")],{})", Factory.Production);
  private static final IConstructor prod__basic_Type__basic_BasicType_ = (IConstructor) _read("prod(label(\"basic\",sort(\"Type\")),[label(\"basic\",sort(\"BasicType\"))],{})", Factory.Production);
  private static final IConstructor prod__binding_Catch__lit_catch_layouts_LAYOUTLIST_pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_ = (IConstructor) _read("prod(label(\"binding\",sort(\"Catch\")),[lit(\"catch\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__bool_BasicType__lit_bool_ = (IConstructor) _read("prod(label(\"bool\",sort(\"BasicType\")),[lit(\"bool\")],{})", Factory.Production);
  private static final IConstructor prod__boolean_Literal__booleanLiteral_BooleanLiteral_ = (IConstructor) _read("prod(label(\"boolean\",sort(\"Literal\")),[label(\"booleanLiteral\",lex(\"BooleanLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__bottomUp_Strategy__lit_bottom_up_ = (IConstructor) _read("prod(label(\"bottomUp\",sort(\"Strategy\")),[lit(\"bottom-up\")],{})", Factory.Production);
  private static final IConstructor prod__bottomUpBreak_Strategy__lit_bottom_up_break_ = (IConstructor) _read("prod(label(\"bottomUpBreak\",sort(\"Strategy\")),[lit(\"bottom-up-break\")],{})", Factory.Production);
  private static final IConstructor prod__bounded_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___60_58_layouts_LAYOUTLIST_bound_Type_ = (IConstructor) _read("prod(label(\"bounded\",sort(\"TypeVar\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"\\<:\"),layouts(\"LAYOUTLIST\"),label(\"bound\",sort(\"Type\"))],{})", Factory.Production);
  private static final IConstructor prod__bracket_Assignable__lit___40_layouts_LAYOUTLIST_arg_Assignable_layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"Assignable\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arg\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__bracket_Class__lit___40_layouts_LAYOUTLIST_charclass_Class_layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"Class\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"charclass\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__bracket_Expression__lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__bracket_ProdModifier__lit_bracket_ = (IConstructor) _read("prod(label(\"bracket\",sort(\"ProdModifier\")),[lit(\"bracket\")],{})", Factory.Production);
  private static final IConstructor prod__bracket_Type__lit___40_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"Type\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__break_Statement__lit_break_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"break\",sort(\"Statement\")),[lit(\"break\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__callOrTree_Expression__expression_Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"callOrTree\",sort(\"Expression\")),[label(\"expression\",conditional(sort(\"Expression\"),{except(\"transitiveReflexiveClosure\"),except(\"transitiveClosure\"),except(\"isDefined\")})),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__callOrTree_Pattern__expression_Pattern_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"callOrTree\",sort(\"Pattern\")),[label(\"expression\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__caseInsensitiveLiteral_Sym__cistring_CaseInsensitiveStringConstant_ = (IConstructor) _read("prod(label(\"caseInsensitiveLiteral\",sort(\"Sym\")),[label(\"cistring\",lex(\"CaseInsensitiveStringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__character_Range__character_Char_ = (IConstructor) _read("prod(label(\"character\",sort(\"Range\")),[label(\"character\",lex(\"Char\"))],{})", Factory.Production);
  private static final IConstructor prod__characterClass_Sym__charClass_Class_ = (IConstructor) _read("prod(label(\"characterClass\",sort(\"Sym\")),[label(\"charClass\",sort(\"Class\"))],{})", Factory.Production);
  private static final IConstructor prod__closedRecursiveAddition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___46_43_46_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"closedRecursiveAddition\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\".+.\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",conditional(sort(\"Expression\"),{except(\"noMatch\"),except(\"match\")}))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__closedRecursiveComposition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___111_46_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"closedRecursiveComposition\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"o.\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__closure_Expression__type_Type_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"closure\",sort(\"Expression\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__column_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_column_IntegerLiteral_ = (IConstructor) _read("prod(label(\"column\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"column\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__complement_Class__lit___33_layouts_LAYOUTLIST_charClass_Class_ = (IConstructor) _read("prod(label(\"complement\",sort(\"Class\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"charClass\",sort(\"Class\"))],{})", Factory.Production);
  private static final IConstructor prod__composition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_o_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"composition\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"o\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__comprehension_Expression__comprehension_Comprehension_ = (IConstructor) _read("prod(label(\"comprehension\",sort(\"Expression\")),[label(\"comprehension\",sort(\"Comprehension\"))],{})", Factory.Production);
  private static final IConstructor prod__conditional_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_44_99_111_110_100_105_116_105_111_110_115_125 = (IConstructor) _read("prod(label(\"conditional\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"when\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable()),tag(breakable(\"{expression,conditions}\"))})", Factory.Production);
  private static final IConstructor prod__conditional_Replacement__replacementExpression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"conditional\",sort(\"Replacement\")),[label(\"replacementExpression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"when\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__constructor_Assignable__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"constructor\",sort(\"Assignable\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__continue_Statement__lit_continue_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"continue\",sort(\"Statement\")),[lit(\"continue\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__data_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_variants_iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"data\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"data\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"variants\",\\iter-seps(sort(\"Variant\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__data_Kind__lit_data_ = (IConstructor) _read("prod(label(\"data\",sort(\"Kind\")),[lit(\"data\")],{})", Factory.Production);
  private static final IConstructor prod__dataAbstract_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"dataAbstract\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"data\"),layouts(\"LAYOUTLIST\"),label(\"user\",sort(\"UserType\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__dateAndTimeLiteral_DateTimeLiteral__dateAndTime_DateAndTime_ = (IConstructor) _read("prod(label(\"dateAndTimeLiteral\",sort(\"DateTimeLiteral\")),[label(\"dateAndTime\",lex(\"DateAndTime\"))],{})", Factory.Production);
  private static final IConstructor prod__dateLiteral_DateTimeLiteral__date_JustDate_ = (IConstructor) _read("prod(label(\"dateLiteral\",sort(\"DateTimeLiteral\")),[label(\"date\",lex(\"JustDate\"))],{})", Factory.Production);
  private static final IConstructor prod__dateTime_BasicType__lit_datetime_ = (IConstructor) _read("prod(label(\"dateTime\",sort(\"BasicType\")),[lit(\"datetime\")],{})", Factory.Production);
  private static final IConstructor prod__dateTime_Literal__dateTimeLiteral_DateTimeLiteral_ = (IConstructor) _read("prod(label(\"dateTime\",sort(\"Literal\")),[label(\"dateTimeLiteral\",sort(\"DateTimeLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__decimalIntegerLiteral_IntegerLiteral__decimal_DecimalIntegerLiteral_ = (IConstructor) _read("prod(label(\"decimalIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"decimal\",lex(\"DecimalIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__declaration_Command__declaration_Declaration_ = (IConstructor) _read("prod(label(\"declaration\",sort(\"Command\")),[label(\"declaration\",sort(\"Declaration\"))],{})", Factory.Production);
  private static final IConstructor prod__declaration_EvalCommand__declaration_Declaration_ = (IConstructor) _read("prod(label(\"declaration\",sort(\"EvalCommand\")),[label(\"declaration\",sort(\"Declaration\"))],{})", Factory.Production);
  private static final IConstructor prod__default_Mapping__Expression__from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_ = (IConstructor) _read("prod(label(\"default\",\\parameterized-sort(\"Mapping\",[sort(\"Expression\")])),[label(\"from\",conditional(sort(\"Expression\"),{except(\"ifDefinedOtherwise\")})),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__default_Mapping__Pattern__from_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Pattern_ = (IConstructor) _read("prod(label(\"default\",\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")])),[label(\"from\",conditional(sort(\"Pattern\"),{except(\"ifDefinedOtherwise\")})),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__default_Assignment__lit___61_ = (IConstructor) _read("prod(label(\"default\",sort(\"Assignment\")),[lit(\"=\")],{})", Factory.Production);
  private static final IConstructor prod__default_Bound__lit___59_layouts_LAYOUTLIST_expression_Expression_ = (IConstructor) _read("prod(label(\"default\",sort(\"Bound\")),[lit(\";\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__default_Case__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement__tag__Foldable = (IConstructor) _read("prod(label(\"default\",sort(\"Case\")),[lit(\"default\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"statement\",sort(\"Statement\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__default_Catch__lit_catch_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_ = (IConstructor) _read("prod(label(\"default\",sort(\"Catch\")),[lit(\"catch\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{})", Factory.Production);
  private static final IConstructor prod__default_Declarator__type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"Declarator\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__default_Formals__formals_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"Formals\")),[label(\"formals\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__default_FunctionBody__lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"default\",sort(\"FunctionBody\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__default_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_body_FunctionBody__tag__Foldable = (IConstructor) _read("prod(label(\"default\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"FunctionBody\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__default_FunctionModifier__lit_default_ = (IConstructor) _read("prod(label(\"default\",sort(\"FunctionModifier\")),[lit(\"default\")],{})", Factory.Production);
  private static final IConstructor prod__default_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"Header\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),lit(\"module\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"imports\",\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__default_Import__lit_import_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"default\",sort(\"Import\")),[lit(\"import\"),layouts(\"LAYOUTLIST\"),label(\"module\",sort(\"ImportedModule\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__default_ImportedModule__name_QualifiedName_ = (IConstructor) _read("prod(label(\"default\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__default_Label__name_Name_layouts_LAYOUTLIST_lit___58_ = (IConstructor) _read("prod(label(\"default\",sort(\"Label\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\")],{})", Factory.Production);
  private static final IConstructor prod__default_LocalVariableDeclaration__declarator_Declarator_ = (IConstructor) _read("prod(label(\"default\",sort(\"LocalVariableDeclaration\")),[label(\"declarator\",sort(\"Declarator\"))],{})", Factory.Production);
  private static final IConstructor prod__default_LocationLiteral__protocolPart_ProtocolPart_layouts_LAYOUTLIST_pathPart_PathPart_ = (IConstructor) _read("prod(label(\"default\",sort(\"LocationLiteral\")),[label(\"protocolPart\",sort(\"ProtocolPart\")),layouts(\"LAYOUTLIST\"),label(\"pathPart\",sort(\"PathPart\"))],{})", Factory.Production);
  private static final IConstructor prod__default_Module__header_Header_layouts_LAYOUTLIST_body_Body_ = (IConstructor) _read("prod(label(\"default\",sort(\"Module\")),[label(\"header\",sort(\"Header\")),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Body\"))],{})", Factory.Production);
  private static final IConstructor prod__default_ModuleActuals__lit___91_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"default\",sort(\"ModuleActuals\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"types\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__default_ModuleParameters__lit___91_layouts_LAYOUTLIST_parameters_iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"default\",sort(\"ModuleParameters\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"TypeVar\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__default_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"default\",sort(\"Parameters\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"formals\",sort(\"Formals\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__default_PreModule__header_Header_layouts_LAYOUTLIST_empty_layouts_LAYOUTLIST_rest_Rest_ = (IConstructor) _read("prod(label(\"default\",sort(\"PreModule\")),[label(\"header\",sort(\"Header\")),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(keywords(\"HeaderKeyword\"))}),layouts(\"LAYOUTLIST\"),label(\"rest\",lex(\"Rest\"))],{})", Factory.Production);
  private static final IConstructor prod__default_QualifiedName__names_iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"QualifiedName\")),[conditional(label(\"names\",\\iter-seps(lex(\"Name\"),[layouts(\"LAYOUTLIST\"),lit(\"::\"),layouts(\"LAYOUTLIST\")])),{\\not-follow(lit(\"::\"))})],{})", Factory.Production);
  private static final IConstructor prod__default_Renaming__from_Name_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_to_Name_ = (IConstructor) _read("prod(label(\"default\",sort(\"Renaming\")),[label(\"from\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\\>\"),layouts(\"LAYOUTLIST\"),label(\"to\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__default_Renamings__lit_renaming_layouts_LAYOUTLIST_renamings_iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"Renamings\")),[lit(\"renaming\"),layouts(\"LAYOUTLIST\"),label(\"renamings\",\\iter-seps(sort(\"Renaming\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__default_StructuredType__basicType_BasicType_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_arguments_iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"default\",sort(\"StructuredType\")),[label(\"basicType\",sort(\"BasicType\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__default_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_contents_TagString__tag__Folded_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"default\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"contents\",lex(\"TagString\"))],{tag(Folded()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__default_Tags__tags_iter_star_seps__Tag__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"default\",sort(\"Tags\")),[label(\"tags\",\\iter-star-seps(sort(\"Tag\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__default_TypeArg__type_Type_ = (IConstructor) _read("prod(label(\"default\",sort(\"TypeArg\")),[label(\"type\",sort(\"Type\"))],{})", Factory.Production);
  private static final IConstructor prod__default_Visibility__ = (IConstructor) _read("prod(label(\"default\",sort(\"Visibility\")),[],{})", Factory.Production);
  private static final IConstructor prod__defaultStrategy_Visit__lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"defaultStrategy\",sort(\"Visit\")),[lit(\"visit\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"subject\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__descendant_Pattern__lit___47_layouts_LAYOUTLIST_pattern_Pattern_ = (IConstructor) _read("prod(label(\"descendant\",sort(\"Pattern\")),[lit(\"/\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__difference_Class__lhs_Class_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Class__assoc__left = (IConstructor) _read("prod(label(\"difference\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__division_Assignment__lit___47_61_ = (IConstructor) _read("prod(label(\"division\",sort(\"Assignment\")),[lit(\"/=\")],{})", Factory.Production);
  private static final IConstructor prod__division_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"division\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"/\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__doWhile_Statement__label_Label_layouts_LAYOUTLIST_lit_do_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"doWhile\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"do\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__doWhile_StringTemplate__lit_do_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"doWhile\",sort(\"StringTemplate\")),[lit(\"do\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__dynamic_LocalVariableDeclaration__lit_dynamic_layouts_LAYOUTLIST_declarator_Declarator_ = (IConstructor) _read("prod(label(\"dynamic\",sort(\"LocalVariableDeclaration\")),[lit(\"dynamic\"),layouts(\"LAYOUTLIST\"),label(\"declarator\",sort(\"Declarator\"))],{})", Factory.Production);
  private static final IConstructor prod__edit_ShellCommand__lit_edit_layouts_LAYOUTLIST_name_QualifiedName_ = (IConstructor) _read("prod(label(\"edit\",sort(\"ShellCommand\")),[lit(\"edit\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__empty_Bound__ = (IConstructor) _read("prod(label(\"empty\",sort(\"Bound\")),[],{})", Factory.Production);
  private static final IConstructor prod__empty_DataTarget__ = (IConstructor) _read("prod(label(\"empty\",sort(\"DataTarget\")),[],{})", Factory.Production);
  private static final IConstructor prod__empty_Label__ = (IConstructor) _read("prod(label(\"empty\",sort(\"Label\")),[],{})", Factory.Production);
  private static final IConstructor prod__empty_Sym__lit___40_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"empty\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__empty_Tag__lit___64_layouts_LAYOUTLIST_name_Name__tag__Folded_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"empty\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{tag(Folded()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__empty_Target__ = (IConstructor) _read("prod(label(\"empty\",sort(\"Target\")),[],{})", Factory.Production);
  private static final IConstructor prod__emptyStatement_Statement__lit___59_ = (IConstructor) _read("prod(label(\"emptyStatement\",sort(\"Statement\")),[lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__endOfLine_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___36_ = (IConstructor) _read("prod(label(\"endOfLine\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"$\")],{})", Factory.Production);
  private static final IConstructor prod__enumerator_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___60_45_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"enumerator\",sort(\"Expression\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"\\<-\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__equals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"equals\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"==\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__equivalence_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"equivalence\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\<==\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__except_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_layouts_LAYOUTLIST_label_NonterminalLabel_ = (IConstructor) _read("prod(label(\"except\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"label\",lex(\"NonterminalLabel\"))],{})", Factory.Production);
  private static final IConstructor prod__expression_Command__expression_Expression_ = (IConstructor) _read("prod(label(\"expression\",sort(\"Command\")),[label(\"expression\",conditional(sort(\"Expression\"),{except(\"nonEmptyBlock\")}))],{})", Factory.Production);
  private static final IConstructor prod__expression_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_125 = (IConstructor) _read("prod(label(\"expression\",sort(\"FunctionDeclaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"signature\",sort(\"Signature\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable()),tag(breakable(\"{expression}\"))})", Factory.Production);
  private static final IConstructor prod__expression_Statement__expression_Expression_layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"expression\",sort(\"Statement\")),[label(\"expression\",conditional(sort(\"Expression\"),{except(\"visit\"),except(\"nonEmptyBlock\")})),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__expression_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression__tag__Folded_tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"expression\",sort(\"Tag\")),[lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{tag(Folded()),tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__extend_Import__lit_extend_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"extend\",sort(\"Import\")),[lit(\"extend\"),layouts(\"LAYOUTLIST\"),label(\"module\",sort(\"ImportedModule\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__external_Import__lit_import_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_at_LocationLiteral_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"external\",sort(\"Import\")),[lit(\"import\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"at\",sort(\"LocationLiteral\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__fail_Statement__lit_fail_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"fail\",sort(\"Statement\")),[lit(\"fail\"),layouts(\"LAYOUTLIST\"),label(\"target\",sort(\"Target\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__fieldAccess_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_ = (IConstructor) _read("prod(label(\"fieldAccess\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"field\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__fieldAccess_Expression__expression_Expression_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_ = (IConstructor) _read("prod(label(\"fieldAccess\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"field\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__fieldProject_Expression__expression_Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_fields_iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"fieldProject\",sort(\"Expression\")),[conditional(label(\"expression\",sort(\"Expression\")),{except(\"transitiveReflexiveClosure\"),except(\"transitiveClosure\"),except(\"isDefined\")}),layouts(\"LAYOUTLIST\"),lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"fields\",\\iter-seps(sort(\"Field\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__fieldUpdate_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_key_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_replacement_Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"fieldUpdate\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"key\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"replacement\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__filter_Statement__lit_filter_layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"filter\",sort(\"Statement\")),[lit(\"filter\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__first_Prod__lhs_Prod_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_rhs_Prod__assoc__left = (IConstructor) _read("prod(label(\"first\",sort(\"Prod\")),[label(\"lhs\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\>\"),{\\not-follow(lit(\"\\>\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Prod\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__follow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_match_Sym__assoc__left = (IConstructor) _read("prod(label(\"follow\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__for_Statement__label_Label_layouts_LAYOUTLIST_lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement__tag__breakable___123_103_101_110_101_114_97_116_111_114_115_125_tag__breakable = (IConstructor) _read("prod(label(\"for\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"for\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\"))],{tag(breakable(\"{generators}\")),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__for_StringTemplate__lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"for\",sort(\"StringTemplate\")),[lit(\"for\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__free_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_ = (IConstructor) _read("prod(label(\"free\",sort(\"TypeVar\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__fromTo_Range__start_Char_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_end_Char_ = (IConstructor) _read("prod(label(\"fromTo\",sort(\"Range\")),[label(\"start\",lex(\"Char\")),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"end\",lex(\"Char\"))],{})", Factory.Production);
  private static final IConstructor prod__function_Declaration__functionDeclaration_FunctionDeclaration_ = (IConstructor) _read("prod(label(\"function\",sort(\"Declaration\")),[label(\"functionDeclaration\",sort(\"FunctionDeclaration\"))],{})", Factory.Production);
  private static final IConstructor prod__function_Kind__lit_function_ = (IConstructor) _read("prod(label(\"function\",sort(\"Kind\")),[lit(\"function\")],{})", Factory.Production);
  private static final IConstructor prod__function_Type__function_FunctionType_ = (IConstructor) _read("prod(label(\"function\",sort(\"Type\")),[label(\"function\",sort(\"FunctionType\"))],{})", Factory.Production);
  private static final IConstructor prod__functionDeclaration_Statement__functionDeclaration_FunctionDeclaration__tag__breakable = (IConstructor) _read("prod(label(\"functionDeclaration\",sort(\"Statement\")),[label(\"functionDeclaration\",sort(\"FunctionDeclaration\"))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__getAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_ = (IConstructor) _read("prod(label(\"getAnnotation\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__givenStrategy_Visit__strategy_Strategy_layouts_LAYOUTLIST_lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"givenStrategy\",sort(\"Visit\")),[label(\"strategy\",sort(\"Strategy\")),layouts(\"LAYOUTLIST\"),lit(\"visit\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"subject\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__givenVisibility_Toplevel__declaration_Declaration_ = (IConstructor) _read("prod(label(\"givenVisibility\",sort(\"Toplevel\")),[label(\"declaration\",sort(\"Declaration\"))],{})", Factory.Production);
  private static final IConstructor prod__globalDirective_Statement__lit_global_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_names_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"globalDirective\",sort(\"Statement\")),[lit(\"global\"),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"names\",\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__greaterThan_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"greaterThan\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__greaterThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"greaterThanOrEq\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__has_Expression__expression_Expression_layouts_LAYOUTLIST_lit_has_layouts_LAYOUTLIST_name_Name_ = (IConstructor) _read("prod(label(\"has\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"has\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__help_ShellCommand__lit_help_ = (IConstructor) _read("prod(label(\"help\",sort(\"ShellCommand\")),[lit(\"help\")],{})", Factory.Production);
  private static final IConstructor prod__hexIntegerLiteral_IntegerLiteral__hex_HexIntegerLiteral_ = (IConstructor) _read("prod(label(\"hexIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"hex\",lex(\"HexIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__history_ShellCommand__lit_history_ = (IConstructor) _read("prod(label(\"history\",sort(\"ShellCommand\")),[lit(\"history\")],{})", Factory.Production);
  private static final IConstructor prod__ifDefined_Assignment__lit___63_61_ = (IConstructor) _read("prod(label(\"ifDefined\",sort(\"Assignment\")),[lit(\"?=\")],{})", Factory.Production);
  private static final IConstructor prod__ifDefinedOrDefault_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_defaultExpression_Expression_ = (IConstructor) _read("prod(label(\"ifDefinedOrDefault\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"defaultExpression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__ifDefinedOtherwise_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"ifDefinedOtherwise\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__ifThen_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_empty__tag__breakable = (IConstructor) _read("prod(label(\"ifThen\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"thenStatement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")})),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(lit(\"else\"))})],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__ifThen_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"ifThen\",sort(\"StringTemplate\")),[lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__ifThenElse_Expression__condition_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_thenExp_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_elseExp_Expression__assoc__right = (IConstructor) _read("prod(label(\"ifThenElse\",sort(\"Expression\")),[label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),label(\"thenExp\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"elseExp\",sort(\"Expression\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__ifThenElse_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_elseStatement_Statement__tag__breakable = (IConstructor) _read("prod(label(\"ifThenElse\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"thenStatement\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),lit(\"else\"),layouts(\"LAYOUTLIST\"),label(\"elseStatement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__ifThenElse_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_thenString_StringMiddle_layouts_LAYOUTLIST_postStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_elseString_StringMiddle_layouts_LAYOUTLIST_postStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"ifThenElse\",sort(\"StringTemplate\")),[lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStatsThen\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"thenString\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStatsThen\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"else\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStatsElse\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"elseString\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStatsElse\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__implication_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"implication\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"==\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__import_Command__imported_Import_ = (IConstructor) _read("prod(label(\"import\",sort(\"Command\")),[label(\"imported\",sort(\"Import\"))],{})", Factory.Production);
  private static final IConstructor prod__import_EvalCommand__imported_Import_ = (IConstructor) _read("prod(label(\"import\",sort(\"EvalCommand\")),[label(\"imported\",sort(\"Import\"))],{})", Factory.Production);
  private static final IConstructor prod__in_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_in_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"in\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"in\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__index_Field__fieldIndex_IntegerLiteral_ = (IConstructor) _read("prod(label(\"index\",sort(\"Field\")),[label(\"fieldIndex\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__initialized_Variable__name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_initial_Expression_ = (IConstructor) _read("prod(label(\"initialized\",sort(\"Variable\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"initial\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__innermost_Strategy__lit_innermost_ = (IConstructor) _read("prod(label(\"innermost\",sort(\"Strategy\")),[lit(\"innermost\")],{})", Factory.Production);
  private static final IConstructor prod__insert_Statement__lit_insert_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable = (IConstructor) _read("prod(label(\"insert\",sort(\"Statement\")),[lit(\"insert\"),layouts(\"LAYOUTLIST\"),label(\"dataTarget\",sort(\"DataTarget\")),layouts(\"LAYOUTLIST\"),label(\"statement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{assoc(\\non-assoc()),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__insertBefore_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"insertBefore\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__int_BasicType__lit_int_ = (IConstructor) _read("prod(label(\"int\",sort(\"BasicType\")),[lit(\"int\")],{})", Factory.Production);
  private static final IConstructor prod__integer_Literal__integerLiteral_IntegerLiteral_ = (IConstructor) _read("prod(label(\"integer\",sort(\"Literal\")),[label(\"integerLiteral\",sort(\"IntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__interpolated_PathPart__pre_PrePathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_ = (IConstructor) _read("prod(label(\"interpolated\",sort(\"PathPart\")),[label(\"pre\",lex(\"PrePathChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"PathTail\"))],{})", Factory.Production);
  private static final IConstructor prod__interpolated_ProtocolPart__pre_PreProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_ = (IConstructor) _read("prod(label(\"interpolated\",sort(\"ProtocolPart\")),[label(\"pre\",lex(\"PreProtocolChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"ProtocolTail\"))],{})", Factory.Production);
  private static final IConstructor prod__interpolated_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_ = (IConstructor) _read("prod(label(\"interpolated\",sort(\"StringLiteral\")),[label(\"pre\",lex(\"PreStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__interpolated_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringMiddle_ = (IConstructor) _read("prod(label(\"interpolated\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringMiddle\"))],{})", Factory.Production);
  private static final IConstructor prod__intersection_Assignment__lit___38_61_ = (IConstructor) _read("prod(label(\"intersection\",sort(\"Assignment\")),[lit(\"&=\")],{})", Factory.Production);
  private static final IConstructor prod__intersection_Class__lhs_Class_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Class__assoc__left = (IConstructor) _read("prod(label(\"intersection\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"&&\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__intersection_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"intersection\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"&\"),{\\not-follow(lit(\"&\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__is_Expression__expression_Expression_layouts_LAYOUTLIST_lit_is_layouts_LAYOUTLIST_name_Name_ = (IConstructor) _read("prod(label(\"is\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"is\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__isDefined_Expression__argument_Expression_layouts_LAYOUTLIST_lit___63_ = (IConstructor) _read("prod(label(\"isDefined\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"?\")],{})", Factory.Production);
  private static final IConstructor prod__it_Expression__lit_it_ = (IConstructor) _read("prod(label(\"it\",sort(\"Expression\")),[conditional(lit(\"it\"),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)])),\\not-follow(\\char-class([range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__iter_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___43_ = (IConstructor) _read("prod(label(\"iter\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"+\")],{})", Factory.Production);
  private static final IConstructor prod__iterSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_ = (IConstructor) _read("prod(label(\"iterSep\",sort(\"Sym\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sep\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"+\")],{})", Factory.Production);
  private static final IConstructor prod__iterStar_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"iterStar\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__iterStarSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"iterStarSep\",sort(\"Sym\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sep\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__java_FunctionModifier__lit_java_ = (IConstructor) _read("prod(label(\"java\",sort(\"FunctionModifier\")),[lit(\"java\")],{})", Factory.Production);
  private static final IConstructor prod__join_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_join_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"join\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"join\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__keyword_SyntaxDefinition__lit_keyword_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"keyword\",sort(\"SyntaxDefinition\")),[lit(\"keyword\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__labeled_DataTarget__label_Name_layouts_LAYOUTLIST_lit___58_ = (IConstructor) _read("prod(label(\"labeled\",sort(\"DataTarget\")),[label(\"label\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\")],{})", Factory.Production);
  private static final IConstructor prod__labeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"labeled\",sort(\"Prod\")),[label(\"modifiers\",\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"args\",\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__labeled_Sym__symbol_Sym_layouts_LAYOUTLIST_label_NonterminalLabel_ = (IConstructor) _read("prod(label(\"labeled\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"label\",lex(\"NonterminalLabel\"))],{})", Factory.Production);
  private static final IConstructor prod__labeled_Target__name_Name_ = (IConstructor) _read("prod(label(\"labeled\",sort(\"Target\")),[label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__language_SyntaxDefinition__start_Start_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"language\",sort(\"SyntaxDefinition\")),[label(\"start\",sort(\"Start\")),layouts(\"LAYOUTLIST\"),lit(\"syntax\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__layout_SyntaxDefinition__vis_Visibility_layouts_LAYOUTLIST_lit_layout_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"layout\",sort(\"SyntaxDefinition\")),[label(\"vis\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"layout\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__left_Assoc__lit_left_ = (IConstructor) _read("prod(label(\"left\",sort(\"Assoc\")),[lit(\"left\")],{})", Factory.Production);
  private static final IConstructor prod__lessThan_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"lessThan\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"\\<\"),{\\not-follow(lit(\"-\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lessThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"lessThanOrEq\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"\\<=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__lexical_SyntaxDefinition__lit_lexical_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable = (IConstructor) _read("prod(label(\"lexical\",sort(\"SyntaxDefinition\")),[lit(\"lexical\"),layouts(\"LAYOUTLIST\"),label(\"defined\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"production\",sort(\"Prod\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__list_BasicType__lit_list_ = (IConstructor) _read("prod(label(\"list\",sort(\"BasicType\")),[lit(\"list\")],{})", Factory.Production);
  private static final IConstructor prod__list_Commands__commands_iter_seps__EvalCommand__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"list\",sort(\"Commands\")),[label(\"commands\",\\iter-seps(sort(\"EvalCommand\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__list_Comprehension__lit___91_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125 = (IConstructor) _read("prod(label(\"list\",sort(\"Comprehension\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"results\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{tag(breakable(\"{results,generators}\"))})", Factory.Production);
  private static final IConstructor prod__list_Expression__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"list\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__list_FunctionModifiers__modifiers_iter_star_seps__FunctionModifier__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"list\",sort(\"FunctionModifiers\")),[label(\"modifiers\",\\iter-star-seps(sort(\"FunctionModifier\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__list_Pattern__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"list\",sort(\"Pattern\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__listDeclarations_ShellCommand__lit_declarations_ = (IConstructor) _read("prod(label(\"listDeclarations\",sort(\"ShellCommand\")),[lit(\"declarations\")],{})", Factory.Production);
  private static final IConstructor prod__listModules_ShellCommand__lit_modules_ = (IConstructor) _read("prod(label(\"listModules\",sort(\"ShellCommand\")),[lit(\"modules\")],{})", Factory.Production);
  private static final IConstructor prod__literal_Expression__literal_Literal_ = (IConstructor) _read("prod(label(\"literal\",sort(\"Expression\")),[label(\"literal\",sort(\"Literal\"))],{})", Factory.Production);
  private static final IConstructor prod__literal_Pattern__literal_Literal_ = (IConstructor) _read("prod(label(\"literal\",sort(\"Pattern\")),[label(\"literal\",sort(\"Literal\"))],{})", Factory.Production);
  private static final IConstructor prod__literal_Sym__string_StringConstant_ = (IConstructor) _read("prod(label(\"literal\",sort(\"Sym\")),[label(\"string\",lex(\"StringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__loc_BasicType__lit_loc_ = (IConstructor) _read("prod(label(\"loc\",sort(\"BasicType\")),[lit(\"loc\")],{})", Factory.Production);
  private static final IConstructor prod__location_Literal__locationLiteral_LocationLiteral_ = (IConstructor) _read("prod(label(\"location\",sort(\"Literal\")),[label(\"locationLiteral\",sort(\"LocationLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__map_BasicType__lit_map_ = (IConstructor) _read("prod(label(\"map\",sort(\"BasicType\")),[lit(\"map\")],{})", Factory.Production);
  private static final IConstructor prod__map_Comprehension__lit___40_layouts_LAYOUTLIST_from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41__tag__breakable___123_102_114_111_109_44_116_111_44_103_101_110_101_114_97_116_111_114_115_125 = (IConstructor) _read("prod(label(\"map\",sort(\"Comprehension\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"from\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"to\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{tag(breakable(\"{from,to,generators}\"))})", Factory.Production);
  private static final IConstructor prod__map_Expression__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"map\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"mappings\",\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Expression\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__map_Pattern__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"map\",sort(\"Pattern\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"mappings\",\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__match_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"match\",sort(\"Expression\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\":=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__mid_PathTail__mid_MidPathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_ = (IConstructor) _read("prod(label(\"mid\",sort(\"PathTail\")),[label(\"mid\",lex(\"MidPathChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"PathTail\"))],{})", Factory.Production);
  private static final IConstructor prod__mid_ProtocolTail__mid_MidProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_ = (IConstructor) _read("prod(label(\"mid\",sort(\"ProtocolTail\")),[label(\"mid\",lex(\"MidProtocolChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"ProtocolTail\"))],{})", Factory.Production);
  private static final IConstructor prod__mid_StringMiddle__mid_MidStringChars_ = (IConstructor) _read("prod(label(\"mid\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\"))],{})", Factory.Production);
  private static final IConstructor prod__midInterpolated_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_ = (IConstructor) _read("prod(label(\"midInterpolated\",sort(\"StringTail\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__midTemplate_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_ = (IConstructor) _read("prod(label(\"midTemplate\",sort(\"StringTail\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__module_Kind__lit_module_ = (IConstructor) _read("prod(label(\"module\",sort(\"Kind\")),[lit(\"module\")],{})", Factory.Production);
  private static final IConstructor prod__modulo_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_mod_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"modulo\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"mod\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__multiVariable_Pattern__qualifiedName_QualifiedName_layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"multiVariable\",sort(\"Pattern\")),[label(\"qualifiedName\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\"*\")],{})", Factory.Production);
  private static final IConstructor prod__nAryConstructor_Variant__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"nAryConstructor\",sort(\"Variant\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__name_Field__fieldName_Name_ = (IConstructor) _read("prod(label(\"name\",sort(\"Field\")),[label(\"fieldName\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__name_UserType__name_QualifiedName_ = (IConstructor) _read("prod(label(\"name\",sort(\"UserType\")),[label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__named_TypeArg__type_Type_layouts_LAYOUTLIST_name_Name_ = (IConstructor) _read("prod(label(\"named\",sort(\"TypeArg\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__negation_Expression__lit___33_layouts_LAYOUTLIST_argument_Expression_ = (IConstructor) _read("prod(label(\"negation\",sort(\"Expression\")),[lit(\"!\"),layouts(\"LAYOUTLIST\"),label(\"argument\",conditional(sort(\"Expression\"),{except(\"noMatch\"),except(\"match\")}))],{})", Factory.Production);
  private static final IConstructor prod__negative_Expression__lit___layouts_LAYOUTLIST_argument_Expression_ = (IConstructor) _read("prod(label(\"negative\",sort(\"Expression\")),[lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__negative_Pattern__lit___layouts_LAYOUTLIST_argument_Pattern_ = (IConstructor) _read("prod(label(\"negative\",sort(\"Pattern\")),[lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__noMatch_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___33_58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"noMatch\",sort(\"Expression\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"!:=\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__noThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_ = (IConstructor) _read("prod(label(\"noThrows\",sort(\"Signature\")),[label(\"modifiers\",sort(\"FunctionModifiers\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\"))],{})", Factory.Production);
  private static final IConstructor prod__node_BasicType__lit_node_ = (IConstructor) _read("prod(label(\"node\",sort(\"BasicType\")),[lit(\"node\")],{})", Factory.Production);
  private static final IConstructor prod__nonAssociative_Assoc__lit_non_assoc_ = (IConstructor) _read("prod(label(\"nonAssociative\",sort(\"Assoc\")),[lit(\"non-assoc\")],{})", Factory.Production);
  private static final IConstructor prod__nonEmptyBlock_Expression__lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"nonEmptyBlock\",sort(\"Expression\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__nonEmptyBlock_Statement__label_Label_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"nonEmptyBlock\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__nonEquals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"nonEquals\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"!=\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__nonInterpolated_PathPart__pathChars_PathChars_ = (IConstructor) _read("prod(label(\"nonInterpolated\",sort(\"PathPart\")),[label(\"pathChars\",lex(\"PathChars\"))],{})", Factory.Production);
  private static final IConstructor prod__nonInterpolated_ProtocolPart__protocolChars_ProtocolChars_ = (IConstructor) _read("prod(label(\"nonInterpolated\",sort(\"ProtocolPart\")),[label(\"protocolChars\",lex(\"ProtocolChars\"))],{})", Factory.Production);
  private static final IConstructor prod__nonInterpolated_StringLiteral__constant_StringConstant_ = (IConstructor) _read("prod(label(\"nonInterpolated\",sort(\"StringLiteral\")),[label(\"constant\",lex(\"StringConstant\"))],{})", Factory.Production);
  private static final IConstructor prod__nonterminal_Sym__nonterminal_Nonterminal_ = (IConstructor) _read("prod(label(\"nonterminal\",sort(\"Sym\")),[conditional(label(\"nonterminal\",lex(\"Nonterminal\")),{\\not-follow(lit(\"[\"))})],{})", Factory.Production);
  private static final IConstructor prod__notFollow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_62_62_layouts_LAYOUTLIST_match_Sym__assoc__left = (IConstructor) _read("prod(label(\"notFollow\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\\>\\>\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__notIn_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_notin_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"notIn\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"notin\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__notPrecede_Sym__match_Sym_layouts_LAYOUTLIST_lit___33_60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right = (IConstructor) _read("prod(label(\"notPrecede\",sort(\"Sym\")),[label(\"match\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"!\\<\\<\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__num_BasicType__lit_num_ = (IConstructor) _read("prod(label(\"num\",sort(\"BasicType\")),[lit(\"num\")],{})", Factory.Production);
  private static final IConstructor prod__octalIntegerLiteral_IntegerLiteral__octal_OctalIntegerLiteral_ = (IConstructor) _read("prod(label(\"octalIntegerLiteral\",sort(\"IntegerLiteral\")),[label(\"octal\",lex(\"OctalIntegerLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__optional_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___63_ = (IConstructor) _read("prod(label(\"optional\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"?\")],{})", Factory.Production);
  private static final IConstructor prod__or_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"or\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"||\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__others_Prod__lit___46_46_46_ = (IConstructor) _read("prod(label(\"others\",sort(\"Prod\")),[lit(\"...\")],{})", Factory.Production);
  private static final IConstructor prod__outermost_Strategy__lit_outermost_ = (IConstructor) _read("prod(label(\"outermost\",sort(\"Strategy\")),[lit(\"outermost\")],{})", Factory.Production);
  private static final IConstructor prod__parameter_Sym__lit___38_layouts_LAYOUTLIST_nonterminal_Nonterminal_ = (IConstructor) _read("prod(label(\"parameter\",sort(\"Sym\")),[lit(\"&\"),layouts(\"LAYOUTLIST\"),label(\"nonterminal\",lex(\"Nonterminal\"))],{})", Factory.Production);
  private static final IConstructor prod__parameters_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_params_ModuleParameters_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"parameters\",sort(\"Header\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),lit(\"module\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"params\",sort(\"ModuleParameters\")),layouts(\"LAYOUTLIST\"),label(\"imports\",\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__parametric_UserType__name_QualifiedName_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"parametric\",sort(\"UserType\")),[conditional(label(\"name\",sort(\"QualifiedName\")),{follow(lit(\"[\"))}),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__parametrized_Sym__nonterminal_Nonterminal_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"parametrized\",sort(\"Sym\")),[conditional(label(\"nonterminal\",lex(\"Nonterminal\")),{follow(lit(\"[\"))}),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"parameters\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__patternWithAction_Case__lit_case_layouts_LAYOUTLIST_patternWithAction_PatternWithAction__tag__Foldable = (IConstructor) _read("prod(label(\"patternWithAction\",sort(\"Case\")),[lit(\"case\"),layouts(\"LAYOUTLIST\"),label(\"patternWithAction\",sort(\"PatternWithAction\"))],{tag(Foldable())})", Factory.Production);
  private static final IConstructor prod__post_PathTail__post_PostPathChars_ = (IConstructor) _read("prod(label(\"post\",sort(\"PathTail\")),[label(\"post\",lex(\"PostPathChars\"))],{})", Factory.Production);
  private static final IConstructor prod__post_ProtocolTail__post_PostProtocolChars_ = (IConstructor) _read("prod(label(\"post\",sort(\"ProtocolTail\")),[label(\"post\",lex(\"PostProtocolChars\"))],{})", Factory.Production);
  private static final IConstructor prod__post_StringTail__post_PostStringChars_ = (IConstructor) _read("prod(label(\"post\",sort(\"StringTail\")),[label(\"post\",lex(\"PostStringChars\"))],{})", Factory.Production);
  private static final IConstructor prod__precede_Sym__match_Sym_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right = (IConstructor) _read("prod(label(\"precede\",sort(\"Sym\")),[label(\"match\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\<\\<\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__present_Start__lit_start_ = (IConstructor) _read("prod(label(\"present\",sort(\"Start\")),[lit(\"start\")],{})", Factory.Production);
  private static final IConstructor prod__private_Visibility__lit_private_ = (IConstructor) _read("prod(label(\"private\",sort(\"Visibility\")),[lit(\"private\")],{})", Factory.Production);
  private static final IConstructor prod__product_Assignment__lit___42_61_ = (IConstructor) _read("prod(label(\"product\",sort(\"Assignment\")),[lit(\"*=\")],{})", Factory.Production);
  private static final IConstructor prod__product_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_empty_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"product\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"*\"),layouts(\"LAYOUTLIST\"),conditional(empty(),{\\not-follow(lit(\"*\"))}),layouts(\"LAYOUTLIST\"),label(\"rhs\",conditional(sort(\"Expression\"),{except(\"noMatch\"),except(\"match\")}))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__public_Visibility__lit_public_ = (IConstructor) _read("prod(label(\"public\",sort(\"Visibility\")),[lit(\"public\")],{})", Factory.Production);
  private static final IConstructor prod__qualifiedName_Expression__qualifiedName_QualifiedName_ = (IConstructor) _read("prod(label(\"qualifiedName\",sort(\"Expression\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__qualifiedName_Pattern__qualifiedName_QualifiedName_ = (IConstructor) _read("prod(label(\"qualifiedName\",sort(\"Pattern\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__quit_ShellCommand__lit_quit_ = (IConstructor) _read("prod(label(\"quit\",sort(\"ShellCommand\")),[lit(\"quit\")],{})", Factory.Production);
  private static final IConstructor prod__range_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"range\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"..\"),layouts(\"LAYOUTLIST\"),label(\"last\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__rational_BasicType__lit_rat_ = (IConstructor) _read("prod(label(\"rational\",sort(\"BasicType\")),[lit(\"rat\")],{})", Factory.Production);
  private static final IConstructor prod__rational_Literal__rationalLiteral_RationalLiteral_ = (IConstructor) _read("prod(label(\"rational\",sort(\"Literal\")),[label(\"rationalLiteral\",lex(\"RationalLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__real_BasicType__lit_real_ = (IConstructor) _read("prod(label(\"real\",sort(\"BasicType\")),[lit(\"real\")],{})", Factory.Production);
  private static final IConstructor prod__real_Literal__realLiteral_RealLiteral_ = (IConstructor) _read("prod(label(\"real\",sort(\"Literal\")),[label(\"realLiteral\",lex(\"RealLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__reducer_Expression__lit___40_layouts_LAYOUTLIST_init_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_result_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"reducer\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"init\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"result\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__reference_Prod__lit___58_layouts_LAYOUTLIST_referenced_Name_ = (IConstructor) _read("prod(label(\"reference\",sort(\"Prod\")),[lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"referenced\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__regExp_Literal__regExpLiteral_RegExpLiteral_ = (IConstructor) _read("prod(label(\"regExp\",sort(\"Literal\")),[label(\"regExpLiteral\",lex(\"RegExpLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__reifiedType_Expression__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Expression_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"reifiedType\",sort(\"Expression\")),[lit(\"type\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"definitions\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__reifiedType_Pattern__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Pattern_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Pattern_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"reifiedType\",sort(\"Pattern\")),[lit(\"type\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"definitions\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__reifyType_Expression__lit___35_layouts_LAYOUTLIST_type_Type_ = (IConstructor) _read("prod(label(\"reifyType\",sort(\"Expression\")),[lit(\"#\"),layouts(\"LAYOUTLIST\"),conditional(label(\"type\",sort(\"Type\")),{\\not-follow(lit(\"[\")),except(\"selector\")})],{})", Factory.Production);
  private static final IConstructor prod__relation_BasicType__lit_rel_ = (IConstructor) _read("prod(label(\"relation\",sort(\"BasicType\")),[lit(\"rel\")],{})", Factory.Production);
  private static final IConstructor prod__remainder_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"remainder\",sort(\"Expression\")),[label(\"lhs\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"%\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__renamings_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_renamings_Renamings_ = (IConstructor) _read("prod(label(\"renamings\",sort(\"ImportedModule\")),[label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"renamings\",sort(\"Renamings\"))],{})", Factory.Production);
  private static final IConstructor prod__replacing_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_replacement_Replacement_ = (IConstructor) _read("prod(label(\"replacing\",sort(\"PatternWithAction\")),[label(\"pattern\",sort(\"Pattern\")),layouts(\"LAYOUTLIST\"),lit(\"=\\>\"),layouts(\"LAYOUTLIST\"),label(\"replacement\",sort(\"Replacement\"))],{})", Factory.Production);
  private static final IConstructor prod__return_Statement__lit_return_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable = (IConstructor) _read("prod(label(\"return\",sort(\"Statement\")),[lit(\"return\"),layouts(\"LAYOUTLIST\"),label(\"statement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{assoc(\\non-assoc()),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__right_Assoc__lit_right_ = (IConstructor) _read("prod(label(\"right\",sort(\"Assoc\")),[lit(\"right\")],{})", Factory.Production);
  private static final IConstructor prod__selector_DataTypeSelector__sort_QualifiedName_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_production_Name_ = (IConstructor) _read("prod(label(\"selector\",sort(\"DataTypeSelector\")),[label(\"sort\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),label(\"production\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__selector_Type__selector_DataTypeSelector_ = (IConstructor) _read("prod(label(\"selector\",sort(\"Type\")),[label(\"selector\",sort(\"DataTypeSelector\"))],{})", Factory.Production);
  private static final IConstructor prod__sequence_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_sequence_iter_seps__Sym__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"sequence\",sort(\"Sym\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),label(\"sequence\",\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__set_BasicType__lit_set_ = (IConstructor) _read("prod(label(\"set\",sort(\"BasicType\")),[lit(\"set\")],{})", Factory.Production);
  private static final IConstructor prod__set_Comprehension__lit___123_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125 = (IConstructor) _read("prod(label(\"set\",sort(\"Comprehension\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"results\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),label(\"generators\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{tag(breakable(\"{results,generators}\"))})", Factory.Production);
  private static final IConstructor prod__set_Expression__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"set\",sort(\"Expression\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__set_Pattern__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"set\",sort(\"Pattern\")),[lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__setAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_value_Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"setAnnotation\",sort(\"Expression\")),[label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),lit(\"@\"),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),label(\"value\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__setOption_ShellCommand__lit_set_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_expression_Expression_ = (IConstructor) _read("prod(label(\"setOption\",sort(\"ShellCommand\")),[lit(\"set\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\")),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__shell_Command__lit___58_layouts_LAYOUTLIST_command_ShellCommand_ = (IConstructor) _read("prod(label(\"shell\",sort(\"Command\")),[lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"command\",sort(\"ShellCommand\"))],{})", Factory.Production);
  private static final IConstructor prod__simpleCharclass_Class__lit___91_layouts_LAYOUTLIST_ranges_iter_star_seps__Range__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"simpleCharclass\",sort(\"Class\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"ranges\",\\iter-star-seps(sort(\"Range\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__solve_Statement__lit_solve_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_variables_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_bound_Bound_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement__tag__breakable = (IConstructor) _read("prod(label(\"solve\",sort(\"Statement\")),[lit(\"solve\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"bound\",sort(\"Bound\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__splice_Expression__lit___42_layouts_LAYOUTLIST_argument_Expression__assoc__non_assoc = (IConstructor) _read("prod(label(\"splice\",sort(\"Expression\")),[lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Expression\"))],{assoc(\\non-assoc())})", Factory.Production);
  private static final IConstructor prod__splice_Pattern__lit___42_layouts_LAYOUTLIST_argument_Pattern_ = (IConstructor) _read("prod(label(\"splice\",sort(\"Pattern\")),[lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__splicePlus_Pattern__lit___43_layouts_LAYOUTLIST_argument_Pattern_ = (IConstructor) _read("prod(label(\"splicePlus\",sort(\"Pattern\")),[lit(\"+\"),layouts(\"LAYOUTLIST\"),label(\"argument\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__start_Sym__lit_start_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_nonterminal_Nonterminal_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"start\",sort(\"Sym\")),[lit(\"start\"),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"nonterminal\",lex(\"Nonterminal\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__startOfLine_Sym__lit___94_layouts_LAYOUTLIST_symbol_Sym_ = (IConstructor) _read("prod(label(\"startOfLine\",sort(\"Sym\")),[lit(\"^\"),layouts(\"LAYOUTLIST\"),label(\"symbol\",sort(\"Sym\"))],{})", Factory.Production);
  private static final IConstructor prod__statement_Command__statement_Statement_ = (IConstructor) _read("prod(label(\"statement\",sort(\"Command\")),[label(\"statement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"visit\"),except(\"variableDeclaration\")}))],{})", Factory.Production);
  private static final IConstructor prod__statement_EvalCommand__statement_Statement_ = (IConstructor) _read("prod(label(\"statement\",sort(\"EvalCommand\")),[label(\"statement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"visit\"),except(\"variableDeclaration\")}))],{})", Factory.Production);
  private static final IConstructor prod__stepRange_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_second_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"stepRange\",sort(\"Expression\")),[lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"first\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),label(\"second\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"..\"),layouts(\"LAYOUTLIST\"),label(\"last\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__string_BasicType__lit_str_ = (IConstructor) _read("prod(label(\"string\",sort(\"BasicType\")),[lit(\"str\")],{})", Factory.Production);
  private static final IConstructor prod__string_Literal__stringLiteral_StringLiteral_ = (IConstructor) _read("prod(label(\"string\",sort(\"Literal\")),[label(\"stringLiteral\",sort(\"StringLiteral\"))],{})", Factory.Production);
  private static final IConstructor prod__structured_Type__structured_StructuredType_ = (IConstructor) _read("prod(label(\"structured\",sort(\"Type\")),[label(\"structured\",sort(\"StructuredType\"))],{})", Factory.Production);
  private static final IConstructor prod__subscript_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscript_Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"subscript\",sort(\"Assignable\")),[label(\"receiver\",sort(\"Assignable\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"subscript\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__subscript_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscripts_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"subscript\",sort(\"Expression\")),[conditional(label(\"expression\",sort(\"Expression\")),{except(\"transitiveReflexiveClosure\"),except(\"transitiveClosure\"),except(\"isDefined\")}),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"subscripts\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__subtraction_Assignment__lit___45_61_ = (IConstructor) _read("prod(label(\"subtraction\",sort(\"Assignment\")),[lit(\"-=\")],{})", Factory.Production);
  private static final IConstructor prod__subtraction_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Expression__assoc__left = (IConstructor) _read("prod(label(\"subtraction\",sort(\"Expression\")),[label(\"lhs\",conditional(sort(\"Expression\"),{except(\"transitiveReflexiveClosure\"),except(\"transitiveClosure\")})),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__switch_Statement__label_Label_layouts_LAYOUTLIST_lit_switch_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125__tag__breakable = (IConstructor) _read("prod(label(\"switch\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"switch\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"expression\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"cases\",\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__symbol_Type__symbol_Sym_ = (IConstructor) _read("prod(label(\"symbol\",sort(\"Type\")),[label(\"symbol\",conditional(sort(\"Sym\"),{except(\"labeled\"),except(\"parametrized\"),except(\"parameter\"),except(\"nonterminal\")}))],{})", Factory.Production);
  private static final IConstructor prod__syntax_Import__syntax_SyntaxDefinition_ = (IConstructor) _read("prod(label(\"syntax\",sort(\"Import\")),[label(\"syntax\",sort(\"SyntaxDefinition\"))],{})", Factory.Production);
  private static final IConstructor prod__tag_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_tag_layouts_LAYOUTLIST_kind_Kind_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit_on_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"tag\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),lit(\"tag\"),layouts(\"LAYOUTLIST\"),label(\"kind\",sort(\"Kind\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\"on\"),layouts(\"LAYOUTLIST\"),label(\"types\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__tag_Kind__lit_tag_ = (IConstructor) _read("prod(label(\"tag\",sort(\"Kind\")),[lit(\"tag\")],{})", Factory.Production);
  private static final IConstructor prod__tag_ProdModifier__tag_Tag_ = (IConstructor) _read("prod(label(\"tag\",sort(\"ProdModifier\")),[label(\"tag\",sort(\"Tag\"))],{})", Factory.Production);
  private static final IConstructor prod__template_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_ = (IConstructor) _read("prod(label(\"template\",sort(\"StringLiteral\")),[label(\"pre\",lex(\"PreStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringTail\"))],{})", Factory.Production);
  private static final IConstructor prod__template_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringMiddle_ = (IConstructor) _read("prod(label(\"template\",sort(\"StringMiddle\")),[label(\"mid\",lex(\"MidStringChars\")),layouts(\"LAYOUTLIST\"),label(\"template\",sort(\"StringTemplate\")),layouts(\"LAYOUTLIST\"),label(\"tail\",sort(\"StringMiddle\"))],{})", Factory.Production);
  private static final IConstructor prod__test_FunctionModifier__lit_test_ = (IConstructor) _read("prod(label(\"test\",sort(\"FunctionModifier\")),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__test_ShellCommand__lit_test_ = (IConstructor) _read("prod(label(\"test\",sort(\"ShellCommand\")),[lit(\"test\")],{})", Factory.Production);
  private static final IConstructor prod__throw_Statement__lit_throw_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable = (IConstructor) _read("prod(label(\"throw\",sort(\"Statement\")),[lit(\"throw\"),layouts(\"LAYOUTLIST\"),label(\"statement\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{assoc(\\non-assoc()),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__timeLiteral_DateTimeLiteral__time_JustTime_ = (IConstructor) _read("prod(label(\"timeLiteral\",sort(\"DateTimeLiteral\")),[label(\"time\",lex(\"JustTime\"))],{})", Factory.Production);
  private static final IConstructor prod__topDown_Strategy__lit_top_down_ = (IConstructor) _read("prod(label(\"topDown\",sort(\"Strategy\")),[lit(\"top-down\")],{})", Factory.Production);
  private static final IConstructor prod__topDownBreak_Strategy__lit_top_down_break_ = (IConstructor) _read("prod(label(\"topDownBreak\",sort(\"Strategy\")),[lit(\"top-down-break\")],{})", Factory.Production);
  private static final IConstructor prod__toplevels_Body__toplevels_iter_star_seps__Toplevel__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"toplevels\",sort(\"Body\")),[label(\"toplevels\",\\iter-star-seps(sort(\"Toplevel\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__transitiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_lit___43_ = (IConstructor) _read("prod(label(\"transitiveClosure\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"+\"),{\\not-follow(lit(\"=\"))})],{})", Factory.Production);
  private static final IConstructor prod__transitiveReflexiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_lit___42_ = (IConstructor) _read("prod(label(\"transitiveReflexiveClosure\",sort(\"Expression\")),[label(\"argument\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),conditional(lit(\"*\"),{\\not-follow(lit(\"=\"))})],{})", Factory.Production);
  private static final IConstructor prod__try_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST__assoc__non_assoc_tag__breakable = (IConstructor) _read("prod(label(\"try\",sort(\"Statement\")),[lit(\"try\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),label(\"handlers\",\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")]))],{assoc(\\non-assoc()),tag(breakable())})", Factory.Production);
  private static final IConstructor prod__tryFinally_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit_finally_layouts_LAYOUTLIST_finallyBody_Statement__tag__breakable = (IConstructor) _read("prod(label(\"tryFinally\",sort(\"Statement\")),[lit(\"try\"),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"Statement\")),layouts(\"LAYOUTLIST\"),label(\"handlers\",\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"finally\"),layouts(\"LAYOUTLIST\"),label(\"finallyBody\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__tuple_Assignable__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"tuple\",sort(\"Assignable\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__tuple_BasicType__lit_tuple_ = (IConstructor) _read("prod(label(\"tuple\",sort(\"BasicType\")),[lit(\"tuple\")],{})", Factory.Production);
  private static final IConstructor prod__tuple_Expression__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"tuple\",sort(\"Expression\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__tuple_Pattern__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ = (IConstructor) _read("prod(label(\"tuple\",sort(\"Pattern\")),[lit(\"\\<\"),layouts(\"LAYOUTLIST\"),label(\"elements\",\\iter-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__type_BasicType__lit_type_ = (IConstructor) _read("prod(label(\"type\",sort(\"BasicType\")),[lit(\"type\")],{})", Factory.Production);
  private static final IConstructor prod__typeArguments_FunctionType__type_Type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"typeArguments\",sort(\"FunctionType\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"arguments\",\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__typedVariable_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_ = (IConstructor) _read("prod(label(\"typedVariable\",sort(\"Pattern\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__typedVariableBecomes_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_ = (IConstructor) _read("prod(label(\"typedVariableBecomes\",sort(\"Pattern\")),[label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__unInitialized_Variable__name_Name_ = (IConstructor) _read("prod(label(\"unInitialized\",sort(\"Variable\")),[label(\"name\",lex(\"Name\"))],{})", Factory.Production);
  private static final IConstructor prod__unconditional_Replacement__replacementExpression_Expression_ = (IConstructor) _read("prod(label(\"unconditional\",sort(\"Replacement\")),[label(\"replacementExpression\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__undeclare_ShellCommand__lit_undeclare_layouts_LAYOUTLIST_name_QualifiedName_ = (IConstructor) _read("prod(label(\"undeclare\",sort(\"ShellCommand\")),[lit(\"undeclare\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__unequal_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___92_layouts_LAYOUTLIST_match_Sym__assoc__left = (IConstructor) _read("prod(label(\"unequal\",sort(\"Sym\")),[label(\"symbol\",sort(\"Sym\")),layouts(\"LAYOUTLIST\"),lit(\"\\\\\"),layouts(\"LAYOUTLIST\"),label(\"match\",sort(\"Sym\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__unimport_ShellCommand__lit_unimport_layouts_LAYOUTLIST_name_QualifiedName_ = (IConstructor) _read("prod(label(\"unimport\",sort(\"ShellCommand\")),[lit(\"unimport\"),layouts(\"LAYOUTLIST\"),label(\"name\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__union_Class__lhs_Class_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Class__assoc__left = (IConstructor) _read("prod(label(\"union\",sort(\"Class\")),[label(\"lhs\",sort(\"Class\")),layouts(\"LAYOUTLIST\"),lit(\"||\"),layouts(\"LAYOUTLIST\"),label(\"rhs\",sort(\"Class\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__unlabeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"unlabeled\",sort(\"Prod\")),[label(\"modifiers\",\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"args\",\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__user_Type__user_UserType_ = (IConstructor) _read("prod(label(\"user\",sort(\"Type\")),[conditional(label(\"user\",sort(\"UserType\")),{delete(keywords(\"HeaderKeyword\"))})],{})", Factory.Production);
  private static final IConstructor prod__utf16_UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(label(\"utf16\",lex(\"UnicodeEscape\")),[lit(\"\\\\\"),\\char-class([range(117,117)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__utf32_UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(label(\"utf32\",lex(\"UnicodeEscape\")),[lit(\"\\\\\"),\\char-class([range(85,85)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)]),\\char-class([range(48,57),range(65,70),range(97,102)])],{})", Factory.Production);
  private static final IConstructor prod__value_BasicType__lit_value_ = (IConstructor) _read("prod(label(\"value\",sort(\"BasicType\")),[lit(\"value\")],{})", Factory.Production);
  private static final IConstructor prod__varArgs_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___46_46_46_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"varArgs\",sort(\"Parameters\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"formals\",sort(\"Formals\")),layouts(\"LAYOUTLIST\"),lit(\"...\"),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__variable_Assignable__qualifiedName_QualifiedName_ = (IConstructor) _read("prod(label(\"variable\",sort(\"Assignable\")),[label(\"qualifiedName\",sort(\"QualifiedName\"))],{})", Factory.Production);
  private static final IConstructor prod__variable_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"variable\",sort(\"Declaration\")),[label(\"tags\",sort(\"Tags\")),layouts(\"LAYOUTLIST\"),label(\"visibility\",sort(\"Visibility\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"variables\",\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__variable_Kind__lit_variable_ = (IConstructor) _read("prod(label(\"variable\",sort(\"Kind\")),[lit(\"variable\")],{})", Factory.Production);
  private static final IConstructor prod__variable_Type__typeVar_TypeVar_ = (IConstructor) _read("prod(label(\"variable\",sort(\"Type\")),[label(\"typeVar\",sort(\"TypeVar\"))],{})", Factory.Production);
  private static final IConstructor prod__variableBecomes_Pattern__name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_ = (IConstructor) _read("prod(label(\"variableBecomes\",sort(\"Pattern\")),[label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),label(\"pattern\",sort(\"Pattern\"))],{})", Factory.Production);
  private static final IConstructor prod__variableDeclaration_Statement__declaration_LocalVariableDeclaration_layouts_LAYOUTLIST_lit___59__tag__breakable = (IConstructor) _read("prod(label(\"variableDeclaration\",sort(\"Statement\")),[label(\"declaration\",sort(\"LocalVariableDeclaration\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__view_Kind__lit_view_ = (IConstructor) _read("prod(label(\"view\",sort(\"Kind\")),[lit(\"view\")],{})", Factory.Production);
  private static final IConstructor prod__visit_Expression__label_Label_layouts_LAYOUTLIST_visit_Visit_ = (IConstructor) _read("prod(label(\"visit\",sort(\"Expression\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),label(\"visit\",sort(\"Visit\"))],{})", Factory.Production);
  private static final IConstructor prod__visit_Statement__label_Label_layouts_LAYOUTLIST_visit_Visit__tag__breakable = (IConstructor) _read("prod(label(\"visit\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),label(\"visit\",sort(\"Visit\"))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__void_BasicType__lit_void_ = (IConstructor) _read("prod(label(\"void\",sort(\"BasicType\")),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__voidClosure_Expression__parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"voidClosure\",sort(\"Expression\")),[label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"statements\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__while_Statement__label_Label_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement__tag__breakable = (IConstructor) _read("prod(label(\"while\",sort(\"Statement\")),[label(\"label\",sort(\"Label\")),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"conditions\",\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),label(\"body\",conditional(sort(\"Statement\"),{except(\"functionDeclaration\"),except(\"variableDeclaration\")}))],{tag(breakable())})", Factory.Production);
  private static final IConstructor prod__while_StringTemplate__lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"while\",sort(\"StringTemplate\")),[lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"condition\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),label(\"preStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"body\",sort(\"StringMiddle\")),layouts(\"LAYOUTLIST\"),label(\"postStats\",\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__withThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit_throws_layouts_LAYOUTLIST_exceptions_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(label(\"withThrows\",sort(\"Signature\")),[label(\"modifiers\",sort(\"FunctionModifiers\")),layouts(\"LAYOUTLIST\"),label(\"type\",sort(\"Type\")),layouts(\"LAYOUTLIST\"),label(\"name\",lex(\"Name\")),layouts(\"LAYOUTLIST\"),label(\"parameters\",sort(\"Parameters\")),layouts(\"LAYOUTLIST\"),lit(\"throws\"),layouts(\"LAYOUTLIST\"),label(\"exceptions\",\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))],{})", Factory.Production);
  private static final IConstructor prod__layouts_$BACKTICKS__ = (IConstructor) _read("prod(layouts(\"$BACKTICKS\"),[],{})", Factory.Production);
  private static final IConstructor prod__layouts_$QUOTES__iter_star__char_class___range__9_10_range__13_13_range__32_32_ = (IConstructor) _read("prod(layouts(\"$QUOTES\"),[conditional(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)])),{\\not-follow(\\char-class([range(9,10),range(13,13),range(32,32)]))})],{})", Factory.Production);
  private static final IConstructor prod__layouts_$default$__ = (IConstructor) _read("prod(layouts(\"$default$\"),[],{})", Factory.Production);
  private static final IConstructor prod__layouts_LAYOUTLIST__iter_star__LAYOUT_ = (IConstructor) _read("prod(layouts(\"LAYOUTLIST\"),[conditional(\\iter-star(lex(\"LAYOUT\")),{\\not-follow(lit(\"//\")),\\not-follow(lit(\"/*\")),\\not-follow(\\char-class([range(9,13),range(32,32),range(133,133),range(160,160),range(5760,5760),range(6158,6158),range(8192,8202),range(8232,8233),range(8239,8239),range(8287,8287),range(12288,12288)]))})],{})", Factory.Production);
  private static final IConstructor prod__Backslash__char_class___range__92_92_ = (IConstructor) _read("prod(lex(\"Backslash\"),[conditional(\\char-class([range(92,92)]),{\\not-follow(\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)]))})],{})", Factory.Production);
  private static final IConstructor prod__BooleanLiteral__lit_false_ = (IConstructor) _read("prod(lex(\"BooleanLiteral\"),[lit(\"false\")],{})", Factory.Production);
  private static final IConstructor prod__BooleanLiteral__lit_true_ = (IConstructor) _read("prod(lex(\"BooleanLiteral\"),[lit(\"true\")],{})", Factory.Production);
  private static final IConstructor prod__CaseInsensitiveStringConstant__lit___39_iter_star__StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"CaseInsensitiveStringConstant\"),[lit(\"\\'\\\\\"),\\iter-star(lex(\"StringCharacter\")),lit(\"\\'\\\\\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[\\char-class([range(0,31),range(33,33),range(35,38),range(40,44),range(46,59),range(61,61),range(63,90),range(94,16777215)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Char__UnicodeEscape__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[lex(\"UnicodeEscape\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"Char\"),[lit(\"\\\\\"),\\char-class([range(32,32),range(34,34),range(39,39),range(45,45),range(60,60),range(62,62),range(91,93),range(98,98),range(102,102),range(110,110),range(114,114),range(116,116)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_16777215__tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(lex(\"Comment\"),[lit(\"//\"),conditional(\\iter-star(\\char-class([range(0,9),range(11,16777215)])),{\\not-follow(\\char-class([range(9,9),range(13,13),range(32,32),range(65,65),range(160,160),range(5760,5760),range(8192,8192),range(8239,8239),range(8287,8287),range(12288,12288)])),\\end-of-line()})],{tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__Comment__lit___47_42_iter_star__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(lex(\"Comment\"),[lit(\"/*\"),\\iter-star(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,16777215)])})),lit(\"*/\")],{tag(category(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_ = (IConstructor) _read("prod(lex(\"DateAndTime\"),[lit(\"$\"),lex(\"DatePart\"),lit(\"T\"),conditional(lex(\"TimePartNoTZ\"),{\\not-follow(\\char-class([range(43,43),range(45,45)]))})],{})", Factory.Production);
  private static final IConstructor prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_TimeZonePart_ = (IConstructor) _read("prod(lex(\"DateAndTime\"),[lit(\"$\"),lex(\"DatePart\"),lit(\"T\"),lex(\"TimePartNoTZ\"),lex(\"TimeZonePart\")],{})", Factory.Production);
  private static final IConstructor prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"DatePart\"),[\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),\\char-class([range(48,51)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"DatePart\"),[\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),lit(\"-\"),\\char-class([range(48,49)]),\\char-class([range(48,57)]),lit(\"-\"),\\char-class([range(48,51)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__DecimalIntegerLiteral__lit_0_ = (IConstructor) _read("prod(lex(\"DecimalIntegerLiteral\"),[conditional(lit(\"0\"),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__DecimalIntegerLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"DecimalIntegerLiteral\"),[\\char-class([range(49,57)]),conditional(\\iter-star(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_iter__char_class___range__48_57_range__65_70_range__97_102_ = (IConstructor) _read("prod(lex(\"HexIntegerLiteral\"),[\\char-class([range(48,48)]),\\char-class([range(88,88),range(120,120)]),conditional(iter(\\char-class([range(48,57),range(65,70),range(97,102)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__JustDate__lit___36_DatePart_ = (IConstructor) _read("prod(lex(\"JustDate\"),[lit(\"$\"),lex(\"DatePart\")],{})", Factory.Production);
  private static final IConstructor prod__JustTime__lit___36_84_TimePartNoTZ_ = (IConstructor) _read("prod(lex(\"JustTime\"),[lit(\"$T\"),conditional(lex(\"TimePartNoTZ\"),{\\not-follow(\\char-class([range(43,43),range(45,45)]))})],{})", Factory.Production);
  private static final IConstructor prod__JustTime__lit___36_84_TimePartNoTZ_TimeZonePart_ = (IConstructor) _read("prod(lex(\"JustTime\"),[lit(\"$T\"),lex(\"TimePartNoTZ\"),lex(\"TimeZonePart\")],{})", Factory.Production);
  private static final IConstructor prod__LAYOUT__char_class___range__9_13_range__32_32_range__133_133_range__160_160_range__5760_5760_range__6158_6158_range__8192_8202_range__8232_8233_range__8239_8239_range__8287_8287_range__12288_12288_ = (IConstructor) _read("prod(lex(\"LAYOUT\"),[\\char-class([range(9,13),range(32,32),range(133,133),range(160,160),range(5760,5760),range(6158,6158),range(8192,8202),range(8232,8233),range(8239,8239),range(8287,8287),range(12288,12288)])],{})", Factory.Production);
  private static final IConstructor prod__LAYOUT__Comment_ = (IConstructor) _read("prod(lex(\"LAYOUT\"),[lex(\"Comment\")],{})", Factory.Production);
  private static final IConstructor prod__MidPathChars__lit___62_URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"MidPathChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__MidProtocolChars__lit___62_URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"MidProtocolChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__MidStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"MidStringChars\"),[\\char-class([range(62,62)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(60,60)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Name__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"Name\"),[conditional(seq([conditional(\\char-class([range(65,90),range(95,95),range(97,122)]),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})]),{delete(keywords(\"RascalKeywords\"))})],{})", Factory.Production);
  private static final IConstructor prod__Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"Name\"),[\\char-class([range(92,92)]),\\char-class([range(65,90),range(95,95),range(97,122)]),conditional(\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__NamedBackslash__char_class___range__92_92_ = (IConstructor) _read("prod(lex(\"NamedBackslash\"),[conditional(\\char-class([range(92,92)]),{\\not-follow(\\char-class([range(60,60),range(62,62),range(92,92)]))})],{})", Factory.Production);
  private static final IConstructor prod__NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[\\char-class([range(0,46),range(48,59),range(61,61),range(63,91),range(93,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__NamedRegExp__NamedBackslash_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[lex(\"NamedBackslash\")],{})", Factory.Production);
  private static final IConstructor prod__NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[\\char-class([range(92,92)]),\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__NamedRegExp__lit___60_Name_lit___62_ = (IConstructor) _read("prod(lex(\"NamedRegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__Nonterminal__char_class___range__65_90_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"Nonterminal\"),[conditional(\\char-class([range(65,90)]),{\\not-precede(\\char-class([range(65,90)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),delete(sort(\"RascalReservedKeywords\"))})],{})", Factory.Production);
  private static final IConstructor prod__NonterminalLabel__char_class___range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"NonterminalLabel\"),[\\char-class([range(97,122)]),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__OctalIntegerLiteral__char_class___range__48_48_iter__char_class___range__48_55_ = (IConstructor) _read("prod(lex(\"OctalIntegerLiteral\"),[\\char-class([range(48,48)]),conditional(iter(\\char-class([range(48,55)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__PathChars__URLChars_char_class___range__124_124_ = (IConstructor) _read("prod(lex(\"PathChars\"),[lex(\"URLChars\"),\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__PostPathChars__lit___62_URLChars_lit___124_ = (IConstructor) _read("prod(lex(\"PostPathChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"|\")],{})", Factory.Production);
  private static final IConstructor prod__PostProtocolChars__lit___62_URLChars_lit___58_47_47_ = (IConstructor) _read("prod(lex(\"PostProtocolChars\"),[lit(\"\\>\"),lex(\"URLChars\"),lit(\"://\")],{})", Factory.Production);
  private static final IConstructor prod__PostStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"PostStringChars\"),[\\char-class([range(62,62)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(34,34)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__PrePathChars__URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"PrePathChars\"),[lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__PreProtocolChars__lit___124_URLChars_lit___60_ = (IConstructor) _read("prod(lex(\"PreProtocolChars\"),[lit(\"|\"),lex(\"URLChars\"),lit(\"\\<\")],{})", Factory.Production);
  private static final IConstructor prod__PreStringChars__char_class___range__34_34_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"PreStringChars\"),[\\char-class([range(34,34)]),\\iter-star(lex(\"StringCharacter\")),\\char-class([range(60,60)])],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__ProtocolChars__char_class___range__124_124_URLChars_lit___58_47_47_ = (IConstructor) _read("prod(lex(\"ProtocolChars\"),[\\char-class([range(124,124)]),lex(\"URLChars\"),conditional(lit(\"://\"),{\\not-follow(\\char-class([range(9,10),range(13,13),range(32,32),range(65,65),range(160,160),range(5760,5760),range(8192,8192),range(8239,8239),range(8287,8287),range(12288,12288)]))})],{})", Factory.Production);
  private static final IConstructor prod__RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(48,57)]),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_iter_star__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"RationalLiteral\"),[\\char-class([range(49,57)]),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(114,114)]),\\char-class([range(48,57)]),conditional(\\iter-star(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})],{})", Factory.Production);
  private static final IConstructor prod__RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)])],{})", Factory.Production);
  private static final IConstructor prod__RealLiteral__lit___46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[conditional(lit(\".\"),{\\not-precede(\\char-class([range(46,46)]))}),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),conditional(lit(\".\"),{\\not-follow(lit(\".\"))}),\\iter-star(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__RealLiteral__lit___46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[conditional(lit(\".\"),{\\not-precede(\\char-class([range(46,46)]))}),iter(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_ = (IConstructor) _read("prod(lex(\"RealLiteral\"),[iter(\\char-class([range(48,57)])),lit(\".\"),\\iter-star(\\char-class([range(48,57)])),\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),iter(\\char-class([range(48,57)])),opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)]))],{})", Factory.Production);
  private static final IConstructor prod__RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(0,46),range(48,59),range(61,61),range(63,91),range(93,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__RegExp__Backslash_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lex(\"Backslash\")],{})", Factory.Production);
  private static final IConstructor prod__RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_ = (IConstructor) _read("prod(lex(\"RegExp\"),[\\char-class([range(92,92)]),\\char-class([range(47,47),range(60,60),range(62,62),range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__RegExp__lit___60_Name_lit___62_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__RegExp__lit___60_Name_lit___58_iter_star__NamedRegExp_lit___62_ = (IConstructor) _read("prod(lex(\"RegExp\"),[lit(\"\\<\"),lex(\"Name\"),lit(\":\"),\\iter-star(lex(\"NamedRegExp\")),lit(\"\\>\")],{})", Factory.Production);
  private static final IConstructor prod__RegExpLiteral__lit___47_iter_star__RegExp_lit___47_RegExpModifier_ = (IConstructor) _read("prod(lex(\"RegExpLiteral\"),[lit(\"/\"),\\iter-star(lex(\"RegExp\")),lit(\"/\"),lex(\"RegExpModifier\")],{})", Factory.Production);
  private static final IConstructor prod__RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_ = (IConstructor) _read("prod(lex(\"RegExpModifier\"),[\\iter-star(\\char-class([range(100,100),range(105,105),range(109,109),range(115,115)]))],{})", Factory.Production);
  private static final IConstructor prod__Rest__iter_star__char_class___range__0_16777215_ = (IConstructor) _read("prod(lex(\"Rest\"),[conditional(\\iter-star(\\char-class([range(0,16777215)])),{\\not-follow(\\char-class([range(0,16777215)]))})],{})", Factory.Production);
  private static final IConstructor prod__StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[\\char-class([range(0,33),range(35,38),range(40,59),range(61,61),range(63,91),range(93,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__StringCharacter__UnicodeEscape_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[lex(\"UnicodeEscape\")],{})", Factory.Production);
  private static final IConstructor prod__StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[lit(\"\\\\\"),\\char-class([range(34,34),range(39,39),range(60,60),range(62,62),range(92,92),range(98,98),range(102,102),range(110,110),range(114,114),range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288_char_class___range__39_39_ = (IConstructor) _read("prod(lex(\"StringCharacter\"),[\\char-class([range(10,10)]),\\iter-star(\\char-class([range(9,9),range(32,32),range(65,65),range(160,160),range(5760,5760),range(8192,8192),range(8239,8239),range(8287,8287),range(12288,12288)])),\\char-class([range(39,39)])],{})", Factory.Production);
  private static final IConstructor prod__StringConstant__lit___34_iter_star__StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(lex(\"StringConstant\"),[lit(\"\\\"\"),\\iter-star(lex(\"StringCharacter\")),lit(\"\\\"\")],{tag(category(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__TagString__lit___123_contents_iter_star__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_ = (IConstructor) _read("prod(lex(\"TagString\"),[conditional(lit(\"{\"),{\\not-precede(lit(\"\\\\\"))}),label(\"contents\",\\iter-star(alt({lex(\"TagString\"),\\char-class([range(0,122),range(124,124),range(126,16777215)]),seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])])}))),conditional(lit(\"}\"),{\\not-precede(lit(\"\\\\\"))})],{})", Factory.Production);
  private static final IConstructor prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimePartNoTZ\"),[\\char-class([range(48,50)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))],{})", Factory.Production);
  private static final IConstructor prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimePartNoTZ\"),[\\char-class([range(48,50)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))],{})", Factory.Production);
  private static final IConstructor prod__TimeZonePart__lit_Z_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[lit(\"Z\")],{})", Factory.Production);
  private static final IConstructor prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),\\char-class([range(48,53)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"TimeZonePart\"),[\\char-class([range(43,43),range(45,45)]),\\char-class([range(48,49)]),\\char-class([range(48,57)]),lit(\":\"),\\char-class([range(48,53)]),\\char-class([range(48,57)])],{})", Factory.Production);
  private static final IConstructor prod__URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_ = (IConstructor) _read("prod(lex(\"URLChars\"),[\\iter-star(\\char-class([range(0,8),range(11,12),range(14,31),range(33,59),range(61,123),range(125,16777215)]))],{})", Factory.Production);
  private static final IConstructor prod__lit___33__char_class___range__33_33_ = (IConstructor) _read("prod(lit(\"!\"),[\\char-class([range(33,33)])],{})", Factory.Production);
  private static final IConstructor prod__lit___33_58_61__char_class___range__33_33_char_class___range__58_58_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"!:=\"),[\\char-class([range(33,33)]),\\char-class([range(58,58)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___33_60_60__char_class___range__33_33_char_class___range__60_60_char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"!\\<\\<\"),[\\char-class([range(33,33)]),\\char-class([range(60,60)]),\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"!=\"),[\\char-class([range(33,33)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___33_62_62__char_class___range__33_33_char_class___range__62_62_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"!\\>\\>\"),[\\char-class([range(33,33)]),\\char-class([range(62,62)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___34__char_class___range__34_34_ = (IConstructor) _read("prod(lit(\"\\\"\"),[\\char-class([range(34,34)])],{})", Factory.Production);
  private static final IConstructor prod__lit___35__char_class___range__35_35_ = (IConstructor) _read("prod(lit(\"#\"),[\\char-class([range(35,35)])],{})", Factory.Production);
  private static final IConstructor prod__lit___36__char_class___range__36_36_ = (IConstructor) _read("prod(lit(\"$\"),[\\char-class([range(36,36)])],{})", Factory.Production);
  private static final IConstructor prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_ = (IConstructor) _read("prod(lit(\"$T\"),[\\char-class([range(36,36)]),\\char-class([range(84,84)])],{})", Factory.Production);
  private static final IConstructor prod__lit___37__char_class___range__37_37_ = (IConstructor) _read("prod(lit(\"%\"),[\\char-class([range(37,37)])],{})", Factory.Production);
  private static final IConstructor prod__lit___38__char_class___range__38_38_ = (IConstructor) _read("prod(lit(\"&\"),[\\char-class([range(38,38)])],{})", Factory.Production);
  private static final IConstructor prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_ = (IConstructor) _read("prod(lit(\"&&\"),[\\char-class([range(38,38)]),\\char-class([range(38,38)])],{})", Factory.Production);
  private static final IConstructor prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"&=\"),[\\char-class([range(38,38)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___39__char_class___range__39_39_ = (IConstructor) _read("prod(lit(\"\\'\\\\\"),[\\char-class([range(39,39)])],{})", Factory.Production);
  private static final IConstructor prod__lit___40__char_class___range__40_40_ = (IConstructor) _read("prod(lit(\"(\"),[\\char-class([range(40,40)])],{})", Factory.Production);
  private static final IConstructor prod__lit___41__char_class___range__41_41_ = (IConstructor) _read("prod(lit(\")\"),[\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___42__char_class___range__42_42_ = (IConstructor) _read("prod(lit(\"*\"),[\\char-class([range(42,42)])],{})", Factory.Production);
  private static final IConstructor prod__lit___42_47__char_class___range__42_42_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"*/\"),[\\char-class([range(42,42)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"*=\"),[\\char-class([range(42,42)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___43__char_class___range__43_43_ = (IConstructor) _read("prod(lit(\"+\"),[\\char-class([range(43,43)])],{})", Factory.Production);
  private static final IConstructor prod__lit___43_61__char_class___range__43_43_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"+=\"),[\\char-class([range(43,43)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___44__char_class___range__44_44_ = (IConstructor) _read("prod(lit(\",\"),[\\char-class([range(44,44)])],{})", Factory.Production);
  private static final IConstructor prod__lit____char_class___range__45_45_ = (IConstructor) _read("prod(lit(\"-\"),[\\char-class([range(45,45)])],{})", Factory.Production);
  private static final IConstructor prod__lit___45_61__char_class___range__45_45_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"-=\"),[\\char-class([range(45,45)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___46__char_class___range__46_46_ = (IConstructor) _read("prod(lit(\".\"),[\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__lit___46_43_46__char_class___range__46_46_char_class___range__43_43_char_class___range__46_46_ = (IConstructor) _read("prod(lit(\".+.\"),[\\char-class([range(46,46)]),\\char-class([range(43,43)]),\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_ = (IConstructor) _read("prod(lit(\"..\"),[\\char-class([range(46,46)]),\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_ = (IConstructor) _read("prod(lit(\"...\"),[\\char-class([range(46,46)]),\\char-class([range(46,46)]),\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__lit___47__char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"/\"),[\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__lit___47_42__char_class___range__47_47_char_class___range__42_42_ = (IConstructor) _read("prod(lit(\"/*\"),[\\char-class([range(47,47)]),\\char-class([range(42,42)])],{})", Factory.Production);
  private static final IConstructor prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"//\"),[\\char-class([range(47,47)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"/=\"),[\\char-class([range(47,47)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit_0__char_class___range__48_48_ = (IConstructor) _read("prod(lit(\"0\"),[\\char-class([range(48,48)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58__char_class___range__58_58_ = (IConstructor) _read("prod(lit(\":\"),[\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"://\"),[\\char-class([range(58,58)]),\\char-class([range(47,47)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58_58__char_class___range__58_58_char_class___range__58_58_ = (IConstructor) _read("prod(lit(\"::\"),[\\char-class([range(58,58)]),\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58_61__char_class___range__58_58_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\":=\"),[\\char-class([range(58,58)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___59__char_class___range__59_59_ = (IConstructor) _read("prod(lit(\";\"),[\\char-class([range(59,59)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60__char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\<\"),[\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_45__char_class___range__60_60_char_class___range__45_45_ = (IConstructor) _read("prod(lit(\"\\<-\"),[\\char-class([range(60,60)]),\\char-class([range(45,45)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_58__char_class___range__60_60_char_class___range__58_58_ = (IConstructor) _read("prod(lit(\"\\<:\"),[\\char-class([range(60,60)]),\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\<\\<\"),[\\char-class([range(60,60)]),\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_60_61__char_class___range__60_60_char_class___range__60_60_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\<\\<=\"),[\\char-class([range(60,60)]),\\char-class([range(60,60)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\<=\"),[\\char-class([range(60,60)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_61_61_62__char_class___range__60_60_char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\<==\\>\"),[\\char-class([range(60,60)]),\\char-class([range(61,61)]),\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___61__char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"=\"),[\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"==\"),[\\char-class([range(61,61)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___61_61_62__char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"==\\>\"),[\\char-class([range(61,61)]),\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"=\\>\"),[\\char-class([range(61,61)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___62__char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\>\"),[\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___62_61__char_class___range__62_62_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\>=\"),[\\char-class([range(62,62)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\>\\>\"),[\\char-class([range(62,62)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___63__char_class___range__63_63_ = (IConstructor) _read("prod(lit(\"?\"),[\\char-class([range(63,63)])],{})", Factory.Production);
  private static final IConstructor prod__lit___63_61__char_class___range__63_63_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"?=\"),[\\char-class([range(63,63)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___64__char_class___range__64_64_ = (IConstructor) _read("prod(lit(\"@\"),[\\char-class([range(64,64)])],{})", Factory.Production);
  private static final IConstructor prod__lit_T__char_class___range__84_84_ = (IConstructor) _read("prod(lit(\"T\"),[\\char-class([range(84,84)])],{})", Factory.Production);
  private static final IConstructor prod__lit_Z__char_class___range__90_90_ = (IConstructor) _read("prod(lit(\"Z\"),[\\char-class([range(90,90)])],{})", Factory.Production);
  private static final IConstructor prod__lit___91__char_class___range__91_91_ = (IConstructor) _read("prod(lit(\"[\"),[\\char-class([range(91,91)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92__char_class___range__92_92_ = (IConstructor) _read("prod(lit(\"\\\\\"),[\\char-class([range(92,92)])],{})", Factory.Production);
  private static final IConstructor prod__lit___93__char_class___range__93_93_ = (IConstructor) _read("prod(lit(\"]\"),[\\char-class([range(93,93)])],{})", Factory.Production);
  private static final IConstructor prod__lit___94__char_class___range__94_94_ = (IConstructor) _read("prod(lit(\"^\"),[\\char-class([range(94,94)])],{})", Factory.Production);
  private static final IConstructor prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"alias\"),[\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(97,97)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"all\"),[\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"anno\"),[\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(110,110)]),\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"any\"),[\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"append\"),[\\char-class([range(97,97)]),\\char-class([range(112,112)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"assert\"),[\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"assoc\"),[\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"bag\"),[\\char-class([range(98,98)]),\\char-class([range(97,97)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"bool\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(111,111)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__lit_bottom_up__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"bottom-up\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(45,45)]),\\char-class([range(117,117)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__lit_bottom_up_break__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"bottom-up-break\"),[\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(45,45)]),\\char-class([range(117,117)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"bracket\"),[\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(99,99)]),\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"break\"),[\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"case\"),[\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_ = (IConstructor) _read("prod(lit(\"catch\"),[\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(99,99)]),\\char-class([range(104,104)])],{})", Factory.Production);
  private static final IConstructor prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"continue\"),[\\char-class([range(99,99)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_ = (IConstructor) _read("prod(lit(\"data\"),[\\char-class([range(100,100)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(97,97)])],{})", Factory.Production);
  private static final IConstructor prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"datetime\"),[\\char-class([range(100,100)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_declarations__char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"declarations\"),[\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"default\"),[\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_do__char_class___range__100_100_char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"do\"),[\\char-class([range(100,100)]),\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"dynamic\"),[\\char-class([range(100,100)]),\\char-class([range(121,121)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(105,105)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__lit_edit__char_class___range__101_101_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"edit\"),[\\char-class([range(101,101)]),\\char-class([range(100,100)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"else\"),[\\char-class([range(101,101)]),\\char-class([range(108,108)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"extend\"),[\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"fail\"),[\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(105,105)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"false\"),[\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"filter\"),[\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"finally\"),[\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(108,108)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"for\"),[\\char-class([range(102,102)]),\\char-class([range(111,111)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__lit_function__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"function\"),[\\char-class([range(102,102)]),\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_global__char_class___range__103_103_char_class___range__108_108_char_class___range__111_111_char_class___range__98_98_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"global\"),[\\char-class([range(103,103)]),\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(98,98)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__lit_has__char_class___range__104_104_char_class___range__97_97_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"has\"),[\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_help__char_class___range__104_104_char_class___range__101_101_char_class___range__108_108_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"help\"),[\\char-class([range(104,104)]),\\char-class([range(101,101)]),\\char-class([range(108,108)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__lit_history__char_class___range__104_104_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"history\"),[\\char-class([range(104,104)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__lit_if__char_class___range__105_105_char_class___range__102_102_ = (IConstructor) _read("prod(lit(\"if\"),[\\char-class([range(105,105)]),\\char-class([range(102,102)])],{})", Factory.Production);
  private static final IConstructor prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"import\"),[\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_in__char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"in\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_innermost__char_class___range__105_105_char_class___range__110_110_char_class___range__110_110_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"innermost\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(110,110)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"insert\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"int\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_is__char_class___range__105_105_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"is\"),[\\char-class([range(105,105)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_it__char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"it\"),[\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_java__char_class___range__106_106_char_class___range__97_97_char_class___range__118_118_char_class___range__97_97_ = (IConstructor) _read("prod(lit(\"java\"),[\\char-class([range(106,106)]),\\char-class([range(97,97)]),\\char-class([range(118,118)]),\\char-class([range(97,97)])],{})", Factory.Production);
  private static final IConstructor prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"join\"),[\\char-class([range(106,106)]),\\char-class([range(111,111)]),\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"keyword\"),[\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(121,121)]),\\char-class([range(119,119)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"layout\"),[\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(121,121)]),\\char-class([range(111,111)]),\\char-class([range(117,117)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_left__char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"left\"),[\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"lexical\"),[\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(105,105)]),\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"list\"),[\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"loc\"),[\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_ = (IConstructor) _read("prod(lit(\"map\"),[\\char-class([range(109,109)]),\\char-class([range(97,97)]),\\char-class([range(112,112)])],{})", Factory.Production);
  private static final IConstructor prod__lit_mod__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"mod\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"module\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_modules__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"modules\"),[\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"node\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"non-assoc\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(45,45)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"notin\"),[\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_ = (IConstructor) _read("prod(lit(\"num\"),[\\char-class([range(110,110)]),\\char-class([range(117,117)]),\\char-class([range(109,109)])],{})", Factory.Production);
  private static final IConstructor prod__lit_o__char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"o\"),[\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__lit___111_46__char_class___range__111_111_char_class___range__46_46_ = (IConstructor) _read("prod(lit(\"o.\"),[\\char-class([range(111,111)]),\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__lit_on__char_class___range__111_111_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"on\"),[\\char-class([range(111,111)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_one__char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"one\"),[\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_outermost__char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"outermost\"),[\\char-class([range(111,111)]),\\char-class([range(117,117)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"private\"),[\\char-class([range(112,112)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"public\"),[\\char-class([range(112,112)]),\\char-class([range(117,117)]),\\char-class([range(98,98)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__lit_quit__char_class___range__113_113_char_class___range__117_117_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"quit\"),[\\char-class([range(113,113)]),\\char-class([range(117,117)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_rat__char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"rat\"),[\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"real\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_ = (IConstructor) _read("prod(lit(\"rel\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(108,108)])],{})", Factory.Production);
  private static final IConstructor prod__lit_renaming__char_class___range__114_114_char_class___range__101_101_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"renaming\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"return\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(117,117)]),\\char-class([range(114,114)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_right__char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"right\"),[\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(103,103)]),\\char-class([range(104,104)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"set\"),[\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"solve\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(108,108)]),\\char-class([range(118,118)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"start\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"str\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_ = (IConstructor) _read("prod(lit(\"switch\"),[\\char-class([range(115,115)]),\\char-class([range(119,119)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(99,99)]),\\char-class([range(104,104)])],{})", Factory.Production);
  private static final IConstructor prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_ = (IConstructor) _read("prod(lit(\"syntax\"),[\\char-class([range(115,115)]),\\char-class([range(121,121)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(120,120)])],{})", Factory.Production);
  private static final IConstructor prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"tag\"),[\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"test\"),[\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_ = (IConstructor) _read("prod(lit(\"throw\"),[\\char-class([range(116,116)]),\\char-class([range(104,104)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(119,119)])],{})", Factory.Production);
  private static final IConstructor prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_ = (IConstructor) _read("prod(lit(\"throws\"),[\\char-class([range(116,116)]),\\char-class([range(104,104)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(115,115)])],{})", Factory.Production);
  private static final IConstructor prod__lit_top_down__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"top-down\"),[\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(100,100)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_top_down_break__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"top-down-break\"),[\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(45,45)]),\\char-class([range(100,100)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(110,110)]),\\char-class([range(45,45)]),\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"true\"),[\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_ = (IConstructor) _read("prod(lit(\"try\"),[\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(121,121)])],{})", Factory.Production);
  private static final IConstructor prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"tuple\"),[\\char-class([range(116,116)]),\\char-class([range(117,117)]),\\char-class([range(112,112)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"type\"),[\\char-class([range(116,116)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_undeclare__char_class___range__117_117_char_class___range__110_110_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"undeclare\"),[\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_unimport__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"unimport\"),[\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"value\"),[\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_variable__char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"variable\"),[\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(97,97)]),\\char-class([range(98,98)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_view__char_class___range__118_118_char_class___range__105_105_char_class___range__101_101_char_class___range__119_119_ = (IConstructor) _read("prod(lit(\"view\"),[\\char-class([range(118,118)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(119,119)])],{})", Factory.Production);
  private static final IConstructor prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"visit\"),[\\char-class([range(118,118)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(105,105)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"void\"),[\\char-class([range(118,118)]),\\char-class([range(111,111)]),\\char-class([range(105,105)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"when\"),[\\char-class([range(119,119)]),\\char-class([range(104,104)]),\\char-class([range(101,101)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"while\"),[\\char-class([range(119,119)]),\\char-class([range(104,104)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit___123__char_class___range__123_123_ = (IConstructor) _read("prod(lit(\"{\"),[\\char-class([range(123,123)])],{})", Factory.Production);
  private static final IConstructor prod__lit___124__char_class___range__124_124_ = (IConstructor) _read("prod(lit(\"|\"),[\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_ = (IConstructor) _read("prod(lit(\"||\"),[\\char-class([range(124,124)]),\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__lit___125__char_class___range__125_125_ = (IConstructor) _read("prod(lit(\"}\"),[\\char-class([range(125,125)])],{})", Factory.Production);
  private static final IConstructor prod__start__Command__layouts_LAYOUTLIST_top_Command_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Command\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Command\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__start__Commands__layouts_LAYOUTLIST_top_Commands_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Commands\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Commands\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__start__EvalCommand__layouts_LAYOUTLIST_top_EvalCommand_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"EvalCommand\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"EvalCommand\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__start__Module__layouts_LAYOUTLIST_top_Module_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"Module\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"Module\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor prod__start__PreModule__layouts_LAYOUTLIST_top_PreModule_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"PreModule\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"PreModule\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor regular__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215 = (IConstructor) _read("regular(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,16777215)])}))", Factory.Production);
  private static final IConstructor regular__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125 = (IConstructor) _read("regular(alt({lex(\"TagString\"),\\char-class([range(0,122),range(124,124),range(126,16777215)]),seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])])}))", Factory.Production);
  private static final IConstructor regular__empty = (IConstructor) _read("regular(empty())", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_55 = (IConstructor) _read("regular(iter(\\char-class([range(48,55)])))", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_57 = (IConstructor) _read("regular(iter(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_57_range__65_70_range__97_102 = (IConstructor) _read("regular(iter(\\char-class([range(48,57),range(65,70),range(97,102)])))", Factory.Production);
  private static final IConstructor regular__iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(lex(\"Name\"),[layouts(\"LAYOUTLIST\"),lit(\"::\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Assignable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Case__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Case\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Catch__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Catch\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__EvalCommand__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"EvalCommand\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Field\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"QualifiedName\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Renaming\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Statement__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Sym__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Type\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"TypeVar\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Variable\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Variant\"),[layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215 = (IConstructor) _read("regular(\\iter-star(alt({conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))}),\\char-class([range(0,41),range(43,16777215)])})))", Factory.Production);
  private static final IConstructor regular__iter_star__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125 = (IConstructor) _read("regular(\\iter-star(alt({lex(\"TagString\"),\\char-class([range(0,122),range(124,124),range(126,16777215)]),seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])])})))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_16777215 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,16777215)])))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__48_57 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_9_range__11_16777215 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,9),range(11,16777215)])))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__9_10_range__13_13_range__32_32 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)])))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(100,100),range(105,105),range(109,109),range(115,115)])))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)])))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,8),range(11,12),range(14,31),range(33,59),range(61,123),range(125,16777215)])))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(9,9),range(32,32),range(65,65),range(160,160),range(5760,5760),range(8192,8192),range(8239,8239),range(8287,8287),range(12288,12288)])))", Factory.Production);
  private static final IConstructor regular__iter_star__LAYOUT = (IConstructor) _read("regular(\\iter-star(lex(\"LAYOUT\")))", Factory.Production);
  private static final IConstructor regular__iter_star__NamedRegExp = (IConstructor) _read("regular(\\iter-star(lex(\"NamedRegExp\")))", Factory.Production);
  private static final IConstructor regular__iter_star__RegExp = (IConstructor) _read("regular(\\iter-star(lex(\"RegExp\")))", Factory.Production);
  private static final IConstructor regular__iter_star__StringCharacter = (IConstructor) _read("regular(\\iter-star(lex(\"StringCharacter\")))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Expression\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(\\parameterized-sort(\"Mapping\",[sort(\"Pattern\")]),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Expression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__FunctionModifier__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"FunctionModifier\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Import__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Pattern\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__ProdModifier__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"ProdModifier\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Range__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Range\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Statement__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Sym__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Sym\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Tag__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Tag\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Toplevel__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Toplevel\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"TypeArg\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__43_43_range__45_45 = (IConstructor) _read("regular(opt(\\char-class([range(43,43),range(45,45)])))", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102 = (IConstructor) _read("regular(opt(\\char-class([range(68,68),range(70,70),range(100,100),range(102,102)])))", Factory.Production);
  private static final IConstructor regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))])))", Factory.Production);
  private static final IConstructor regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(opt(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))])))", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(seq([conditional(\\char-class([range(65,90),range(95,95),range(97,122)]),{\\not-precede(\\char-class([range(65,90),range(95,95),range(97,122)]))}),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})]))", Factory.Production);
  private static final IConstructor regular__seq___lit___92_char_class___range__123_123_range__125_125 = (IConstructor) _read("regular(seq([lit(\"\\\\\"),\\char-class([range(123,123),range(125,125)])]))", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57 = (IConstructor) _read("regular(seq([\\char-class([range(44,44),range(46,46)]),\\char-class([range(48,57)]),opt(seq([\\char-class([range(48,57)]),opt(\\char-class([range(48,57)]))]))]))", Factory.Production);
    
  // Item declarations
	
	
  protected static class HeaderKeyword {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__HeaderKeyword__lit_extend_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4060, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_extend_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_import_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4052, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_import_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_keyword_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4044, 0, prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_, new int[] {107,101,121,119,111,114,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_keyword_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_layout_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4064, 0, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new int[] {108,97,121,111,117,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_layout_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_lexical_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4056, 0, prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_, new int[] {108,101,120,105,99,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_lexical_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_start_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4040, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_start_, tmp);
	}
    protected static final void _init_prod__HeaderKeyword__lit_syntax_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4048, 0, prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_, new int[] {115,121,110,116,97,120}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HeaderKeyword__lit_syntax_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__HeaderKeyword__lit_extend_(builder);
      
        _init_prod__HeaderKeyword__lit_import_(builder);
      
        _init_prod__HeaderKeyword__lit_keyword_(builder);
      
        _init_prod__HeaderKeyword__lit_layout_(builder);
      
        _init_prod__HeaderKeyword__lit_lexical_(builder);
      
        _init_prod__HeaderKeyword__lit_start_(builder);
      
        _init_prod__HeaderKeyword__lit_syntax_(builder);
      
    }
  }
	
  protected static class RascalKeywords {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RascalKeywords__lit_alias_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5978, 0, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new int[] {97,108,105,97,115}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_alias_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_all_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6198, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new int[] {97,108,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_all_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_anno_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6030, 0, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new int[] {97,110,110,111}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_anno_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_any_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6186, 0, prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_, new int[] {97,110,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_any_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_append_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6074, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_append_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_assert_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6022, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_assert_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6150, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {97,115,115,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_assoc_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_bag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6154, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new int[] {98,97,103}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_bag_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_bool_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6182, 0, prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_, new int[] {98,111,111,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_bool_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_bracket_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6138, 0, prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_, new int[] {98,114,97,99,107,101,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_bracket_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_break_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6206, 0, prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,114,101,97,107}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_break_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_case_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6174, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new int[] {99,97,115,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_case_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_catch_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6086, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {99,97,116,99,104}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_catch_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_continue_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6142, 0, prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_, new int[] {99,111,110,116,105,110,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_continue_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_data_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6066, 0, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_data_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_datetime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6162, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new int[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_datetime_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_default_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5982, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_default_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_dynamic_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6058, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new int[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_dynamic_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_else_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6118, 0, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_else_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_extend_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6070, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_extend_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_fail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5966, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new int[] {102,97,105,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_fail_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_false_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6202, 0, prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {102,97,108,115,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_false_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_filter_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6166, 0, prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new int[] {102,105,108,116,101,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_filter_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_finally_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6190, 0, prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_, new int[] {102,105,110,97,108,108,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_finally_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_for_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6134, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_for_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_if_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6110, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_if_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_import_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6014, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_import_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_in_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6122, 0, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new int[] {105,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_in_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_insert_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6026, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_insert_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_int_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5958, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new int[] {105,110,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_int_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_it_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6126, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new int[] {105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_it_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_join_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6130, 0, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new int[] {106,111,105,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_join_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_layout_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6178, 0, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new int[] {108,97,121,111,117,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_layout_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_list_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6050, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new int[] {108,105,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_list_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_loc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6018, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new int[] {108,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_loc_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_map_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6002, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new int[] {109,97,112}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_map_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_mod_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5962, 0, prod__lit_mod__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_, new int[] {109,111,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_mod_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_module_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5990, 0, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_module_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_node_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6094, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new int[] {110,111,100,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_node_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_non_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6106, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_notin_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6078, 0, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new int[] {110,111,116,105,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_notin_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_num_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6158, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new int[] {110,117,109}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_num_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_o_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6054, 0, prod__lit_o__char_class___range__111_111_, new int[] {111}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_o_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_one_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6090, 0, prod__lit_one__char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_, new int[] {111,110,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_one_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_private_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5994, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new int[] {112,114,105,118,97,116,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_private_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_public_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6034, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new int[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_public_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_rat_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6222, 0, prod__lit_rat__char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_, new int[] {114,97,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_rat_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_real_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6194, 0, prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_, new int[] {114,101,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_real_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_rel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6210, 0, prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new int[] {114,101,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_rel_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_return_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6114, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new int[] {114,101,116,117,114,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_return_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_set_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6146, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new int[] {115,101,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_set_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_solve_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6218, 0, prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_, new int[] {115,111,108,118,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_solve_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_start_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6010, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_start_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_str_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6098, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new int[] {115,116,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_str_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_switch_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5970, 0, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {115,119,105,116,99,104}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_switch_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_tag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6062, 0, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new int[] {116,97,103}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_tag_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_test_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6006, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new int[] {116,101,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_test_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_throw_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5974, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new int[] {116,104,114,111,119}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_throw_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_throws_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5986, 0, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new int[] {116,104,114,111,119,115}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_throws_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_true_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5998, 0, prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_, new int[] {116,114,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_true_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_try_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6042, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new int[] {116,114,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_try_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_tuple_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5954, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new int[] {116,117,112,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_tuple_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6082, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_type_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_value_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6046, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new int[] {118,97,108,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_value_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_visit_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6102, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new int[] {118,105,115,105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_visit_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_void_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6038, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new int[] {118,111,105,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_void_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__lit_while_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6170, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__lit_while_, tmp);
	}
    protected static final void _init_prod__RascalKeywords__BasicType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(6214, 0, "BasicType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RascalKeywords__BasicType_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__RascalKeywords__lit_alias_(builder);
      
        _init_prod__RascalKeywords__lit_all_(builder);
      
        _init_prod__RascalKeywords__lit_anno_(builder);
      
        _init_prod__RascalKeywords__lit_any_(builder);
      
        _init_prod__RascalKeywords__lit_append_(builder);
      
        _init_prod__RascalKeywords__lit_assert_(builder);
      
        _init_prod__RascalKeywords__lit_assoc_(builder);
      
        _init_prod__RascalKeywords__lit_bag_(builder);
      
        _init_prod__RascalKeywords__lit_bool_(builder);
      
        _init_prod__RascalKeywords__lit_bracket_(builder);
      
        _init_prod__RascalKeywords__lit_break_(builder);
      
        _init_prod__RascalKeywords__lit_case_(builder);
      
        _init_prod__RascalKeywords__lit_catch_(builder);
      
        _init_prod__RascalKeywords__lit_continue_(builder);
      
        _init_prod__RascalKeywords__lit_data_(builder);
      
        _init_prod__RascalKeywords__lit_datetime_(builder);
      
        _init_prod__RascalKeywords__lit_default_(builder);
      
        _init_prod__RascalKeywords__lit_dynamic_(builder);
      
        _init_prod__RascalKeywords__lit_else_(builder);
      
        _init_prod__RascalKeywords__lit_extend_(builder);
      
        _init_prod__RascalKeywords__lit_fail_(builder);
      
        _init_prod__RascalKeywords__lit_false_(builder);
      
        _init_prod__RascalKeywords__lit_filter_(builder);
      
        _init_prod__RascalKeywords__lit_finally_(builder);
      
        _init_prod__RascalKeywords__lit_for_(builder);
      
        _init_prod__RascalKeywords__lit_if_(builder);
      
        _init_prod__RascalKeywords__lit_import_(builder);
      
        _init_prod__RascalKeywords__lit_in_(builder);
      
        _init_prod__RascalKeywords__lit_insert_(builder);
      
        _init_prod__RascalKeywords__lit_int_(builder);
      
        _init_prod__RascalKeywords__lit_it_(builder);
      
        _init_prod__RascalKeywords__lit_join_(builder);
      
        _init_prod__RascalKeywords__lit_layout_(builder);
      
        _init_prod__RascalKeywords__lit_list_(builder);
      
        _init_prod__RascalKeywords__lit_loc_(builder);
      
        _init_prod__RascalKeywords__lit_map_(builder);
      
        _init_prod__RascalKeywords__lit_mod_(builder);
      
        _init_prod__RascalKeywords__lit_module_(builder);
      
        _init_prod__RascalKeywords__lit_node_(builder);
      
        _init_prod__RascalKeywords__lit_non_assoc_(builder);
      
        _init_prod__RascalKeywords__lit_notin_(builder);
      
        _init_prod__RascalKeywords__lit_num_(builder);
      
        _init_prod__RascalKeywords__lit_o_(builder);
      
        _init_prod__RascalKeywords__lit_one_(builder);
      
        _init_prod__RascalKeywords__lit_private_(builder);
      
        _init_prod__RascalKeywords__lit_public_(builder);
      
        _init_prod__RascalKeywords__lit_rat_(builder);
      
        _init_prod__RascalKeywords__lit_real_(builder);
      
        _init_prod__RascalKeywords__lit_rel_(builder);
      
        _init_prod__RascalKeywords__lit_return_(builder);
      
        _init_prod__RascalKeywords__lit_set_(builder);
      
        _init_prod__RascalKeywords__lit_solve_(builder);
      
        _init_prod__RascalKeywords__lit_start_(builder);
      
        _init_prod__RascalKeywords__lit_str_(builder);
      
        _init_prod__RascalKeywords__lit_switch_(builder);
      
        _init_prod__RascalKeywords__lit_tag_(builder);
      
        _init_prod__RascalKeywords__lit_test_(builder);
      
        _init_prod__RascalKeywords__lit_throw_(builder);
      
        _init_prod__RascalKeywords__lit_throws_(builder);
      
        _init_prod__RascalKeywords__lit_true_(builder);
      
        _init_prod__RascalKeywords__lit_try_(builder);
      
        _init_prod__RascalKeywords__lit_tuple_(builder);
      
        _init_prod__RascalKeywords__lit_type_(builder);
      
        _init_prod__RascalKeywords__lit_value_(builder);
      
        _init_prod__RascalKeywords__lit_visit_(builder);
      
        _init_prod__RascalKeywords__lit_void_(builder);
      
        _init_prod__RascalKeywords__lit_while_(builder);
      
        _init_prod__RascalKeywords__BasicType_(builder);
      
    }
  }
	
  protected static class layouts_$BACKTICKS {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__layouts_$BACKTICKS__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(7738, 0);
      builder.addAlternative(ObjectRascalRascal.prod__layouts_$BACKTICKS__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__layouts_$BACKTICKS__(builder);
      
    }
  }
	
  protected static class layouts_$QUOTES {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__layouts_$QUOTES__iter_star__char_class___range__9_10_range__13_13_range__32_32_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(9176, 0, regular__iter_star__char_class___range__9_10_range__13_13_range__32_32, new CharStackNode<IConstructor>(9170, 0, new int[][]{{9,10},{13,13},{32,32}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,10},{13,13},{32,32}})});
      builder.addAlternative(ObjectRascalRascal.prod__layouts_$QUOTES__iter_star__char_class___range__9_10_range__13_13_range__32_32_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__layouts_$QUOTES__iter_star__char_class___range__9_10_range__13_13_range__32_32_(builder);
      
    }
  }
	
  protected static class layouts_$default$ {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__layouts_$default$__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(7952, 0);
      builder.addAlternative(ObjectRascalRascal.prod__layouts_$default$__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__layouts_$default$__(builder);
      
    }
  }
	
  protected static class layouts_LAYOUTLIST {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__layouts_LAYOUTLIST__iter_star__LAYOUT_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(8184, 0, regular__iter_star__LAYOUT, new NonTerminalStackNode<IConstructor>(8174, 0, "LAYOUT", null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,13},{32,32},{133,133},{160,160},{5760,5760},{6158,6158},{8192,8202},{8232,8233},{8239,8239},{8287,8287},{12288,12288}}), new StringFollowRestriction(new int[] {47,47}), new StringFollowRestriction(new int[] {47,42})});
      builder.addAlternative(ObjectRascalRascal.prod__layouts_LAYOUTLIST__iter_star__LAYOUT_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__layouts_LAYOUTLIST__iter_star__LAYOUT_(builder);
      
    }
  }
	
  protected static class Backslash {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Backslash__char_class___range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(4726, 0, new int[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{47,47},{60,60},{62,62},{92,92}})});
      builder.addAlternative(ObjectRascalRascal.prod__Backslash__char_class___range__92_92_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Backslash__char_class___range__92_92_(builder);
      
    }
  }
	
  protected static class BooleanLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__BooleanLiteral__lit_false_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(10898, 0, prod__lit_false__char_class___range__102_102_char_class___range__97_97_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {102,97,108,115,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__BooleanLiteral__lit_false_, tmp);
	}
    protected static final void _init_prod__BooleanLiteral__lit_true_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(10894, 0, prod__lit_true__char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__101_101_, new int[] {116,114,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__BooleanLiteral__lit_true_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__BooleanLiteral__lit_false_(builder);
      
        _init_prod__BooleanLiteral__lit_true_(builder);
      
    }
  }
	
  protected static class CaseInsensitiveStringConstant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__CaseInsensitiveStringConstant__lit___39_iter_star__StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(4714, 2, prod__lit___39__char_class___range__39_39_, new int[] {39}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(4712, 1, regular__iter_star__StringCharacter, new NonTerminalStackNode<IConstructor>(4710, 0, "StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4708, 0, prod__lit___39__char_class___range__39_39_, new int[] {39}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__CaseInsensitiveStringConstant__lit___39_iter_star__StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__CaseInsensitiveStringConstant__lit___39_iter_star__StringCharacter_lit___39__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class Char {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(6318, 0, new int[][]{{0,31},{33,33},{35,38},{40,44},{46,59},{61,61},{63,90},{94,16777215}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__Char__UnicodeEscape__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(6322, 0, "UnicodeEscape", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Char__UnicodeEscape__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(6314, 1, new int[][]{{32,32},{34,34},{39,39},{45,45},{60,60},{62,62},{91,93},{98,98},{102,102},{110,110},{114,114},{116,116}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(6312, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Char__char_class___range__0_31_range__33_33_range__35_38_range__40_44_range__46_59_range__61_61_range__63_90_range__94_16777215__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__Char__UnicodeEscape__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__Char__lit___92_char_class___range__32_32_range__34_34_range__39_39_range__45_45_range__60_60_range__62_62_range__91_93_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class Comment {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_16777215__tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(3178, 1, regular__iter_star__char_class___range__0_9_range__11_16777215, new CharStackNode<IConstructor>(3172, 0, new int[][]{{0,9},{11,16777215}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,9},{13,13},{32,32},{65,65},{160,160},{5760,5760},{8192,8192},{8239,8239},{8287,8287},{12288,12288}}), new AtEndOfLineRequirement()});
      tmp[0] = new LiteralStackNode<IConstructor>(3170, 0, prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_, new int[] {47,47}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_16777215__tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__Comment__lit___47_42_iter_star__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(3166, 2, prod__lit___42_47__char_class___range__42_42_char_class___range__47_47_, new int[] {42,47}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(3164, 1, regular__iter_star__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215, new AlternativeStackNode<IConstructor>(3162, 0, regular__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(3158, 0, new int[][]{{42,42}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{47,47}})}), new CharStackNode<IConstructor>(3160, 0, new int[][]{{0,41},{43,16777215}}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3152, 0, prod__lit___47_42__char_class___range__47_47_char_class___range__42_42_, new int[] {47,42}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Comment__lit___47_42_iter_star__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Comment__lit___47_47_iter_star__char_class___range__0_9_range__11_16777215__tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__Comment__lit___47_42_iter_star__alt___char_class___range__42_42_char_class___range__0_41_range__43_16777215_lit___42_47__tag__category___67_111_109_109_101_110_116(builder);
      
    }
  }
	
  protected static class DateAndTime {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(11732, 3, "TimePartNoTZ", null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{43,43},{45,45}})});
      tmp[2] = new LiteralStackNode<IConstructor>(11726, 2, prod__lit_T__char_class___range__84_84_, new int[] {84}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11724, 1, "DatePart", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11722, 0, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_, tmp);
	}
    protected static final void _init_prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_TimeZonePart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(11744, 4, "TimeZonePart", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11742, 3, "TimePartNoTZ", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(11740, 2, prod__lit_T__char_class___range__84_84_, new int[] {84}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11738, 1, "DatePart", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11736, 0, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_TimeZonePart_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_(builder);
      
        _init_prod__DateAndTime__lit___36_DatePart_lit_T_TimePartNoTZ_TimeZonePart_(builder);
      
    }
  }
	
  protected static class DatePart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[8];
      
      tmp[7] = new CharStackNode<IConstructor>(4300, 7, new int[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(4298, 6, new int[][]{{48,51}}, null, null);
      tmp[5] = new CharStackNode<IConstructor>(4296, 5, new int[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4294, 4, new int[][]{{48,49}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(4292, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(4290, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(4288, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4286, 0, new int[][]{{48,57}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[10];
      
      tmp[9] = new CharStackNode<IConstructor>(4282, 9, new int[][]{{48,57}}, null, null);
      tmp[8] = new CharStackNode<IConstructor>(4280, 8, new int[][]{{48,51}}, null, null);
      tmp[7] = new LiteralStackNode<IConstructor>(4278, 7, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(4276, 6, new int[][]{{48,57}}, null, null);
      tmp[5] = new CharStackNode<IConstructor>(4274, 5, new int[][]{{48,49}}, null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(4272, 4, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(4270, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(4268, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(4266, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4264, 0, new int[][]{{48,57}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_49_char_class___range__48_57_char_class___range__48_51_char_class___range__48_57_(builder);
      
        _init_prod__DatePart__char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_lit___char_class___range__48_49_char_class___range__48_57_lit___char_class___range__48_51_char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class DecimalIntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__DecimalIntegerLiteral__lit_0_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(7098, 0, prod__lit_0__char_class___range__48_48_, new int[] {48}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      builder.addAlternative(ObjectRascalRascal.prod__DecimalIntegerLiteral__lit_0_, tmp);
	}
    protected static final void _init_prod__DecimalIntegerLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(7110, 1, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(7104, 0, new int[][]{{48,57}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(7102, 0, new int[][]{{49,57}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__DecimalIntegerLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__DecimalIntegerLiteral__lit_0_(builder);
      
        _init_prod__DecimalIntegerLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class HexIntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_iter__char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new ListStackNode<IConstructor>(5832, 2, regular__iter__char_class___range__48_57_range__65_70_range__97_102, new CharStackNode<IConstructor>(5826, 0, new int[][]{{48,57},{65,70},{97,102}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode<IConstructor>(5824, 1, new int[][]{{88,88},{120,120}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(5822, 0, new int[][]{{48,48}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_iter__char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__HexIntegerLiteral__char_class___range__48_48_char_class___range__88_88_range__120_120_iter__char_class___range__48_57_range__65_70_range__97_102_(builder);
      
    }
  }
	
  protected static class JustDate {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__JustDate__lit___36_DatePart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(4666, 1, "DatePart", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4664, 0, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__JustDate__lit___36_DatePart_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__JustDate__lit___36_DatePart_(builder);
      
    }
  }
	
  protected static class JustTime {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__JustTime__lit___36_84_TimePartNoTZ_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(8254, 1, "TimePartNoTZ", null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{43,43},{45,45}})});
      tmp[0] = new LiteralStackNode<IConstructor>(8248, 0, prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_, new int[] {36,84}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__JustTime__lit___36_84_TimePartNoTZ_, tmp);
	}
    protected static final void _init_prod__JustTime__lit___36_84_TimePartNoTZ_TimeZonePart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(8244, 2, "TimeZonePart", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(8242, 1, "TimePartNoTZ", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(8240, 0, prod__lit___36_84__char_class___range__36_36_char_class___range__84_84_, new int[] {36,84}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__JustTime__lit___36_84_TimePartNoTZ_TimeZonePart_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__JustTime__lit___36_84_TimePartNoTZ_(builder);
      
        _init_prod__JustTime__lit___36_84_TimePartNoTZ_TimeZonePart_(builder);
      
    }
  }
	
  protected static class LAYOUT {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__LAYOUT__char_class___range__9_13_range__32_32_range__133_133_range__160_160_range__5760_5760_range__6158_6158_range__8192_8202_range__8232_8233_range__8239_8239_range__8287_8287_range__12288_12288_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(6980, 0, new int[][]{{9,13},{32,32},{133,133},{160,160},{5760,5760},{6158,6158},{8192,8202},{8232,8233},{8239,8239},{8287,8287},{12288,12288}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__LAYOUT__char_class___range__9_13_range__32_32_range__133_133_range__160_160_range__5760_5760_range__6158_6158_range__8192_8202_range__8232_8233_range__8239_8239_range__8287_8287_range__12288_12288_, tmp);
	}
    protected static final void _init_prod__LAYOUT__Comment_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(6984, 0, "Comment", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__LAYOUT__Comment_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__LAYOUT__char_class___range__9_13_range__32_32_range__133_133_range__160_160_range__5760_5760_range__6158_6158_range__8192_8202_range__8232_8233_range__8239_8239_range__8287_8287_range__12288_12288_(builder);
      
        _init_prod__LAYOUT__Comment_(builder);
      
    }
  }
	
  protected static class MidPathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__MidPathChars__lit___62_URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(6396, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6394, 1, "URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(6392, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__MidPathChars__lit___62_URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__MidPathChars__lit___62_URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class MidProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__MidProtocolChars__lit___62_URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(4608, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4606, 1, "URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4604, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__MidProtocolChars__lit___62_URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__MidProtocolChars__lit___62_URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class MidStringChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__MidStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(7692, 2, new int[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(7690, 1, regular__iter_star__StringCharacter, new NonTerminalStackNode<IConstructor>(7688, 0, "StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(7686, 0, new int[][]{{62,62}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__MidStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__MidStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class Name {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Name__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SequenceStackNode<IConstructor>(4830, 0, regular__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(4816, 0, new int[][]{{65,90},{95,95},{97,122}}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{65,90},{95,95},{97,122}})}, null), new ListStackNode<IConstructor>(4824, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(4818, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})})}, null, new ICompletionFilter[] {new StringMatchRestriction(new int[] {105,109,112,111,114,116}), new StringMatchRestriction(new int[] {105,110}), new StringMatchRestriction(new int[] {114,97,116}), new StringMatchRestriction(new int[] {99,111,110,116,105,110,117,101}), new StringMatchRestriction(new int[] {97,108,108}), new StringMatchRestriction(new int[] {102,97,108,115,101}), new StringMatchRestriction(new int[] {97,110,110,111}), new StringMatchRestriction(new int[] {98,114,97,99,107,101,116}), new StringMatchRestriction(new int[] {100,97,116,97}), new StringMatchRestriction(new int[] {106,111,105,110}), new StringMatchRestriction(new int[] {108,97,121,111,117,116}), new StringMatchRestriction(new int[] {105,116}), new StringMatchRestriction(new int[] {115,119,105,116,99,104}), new StringMatchRestriction(new int[] {114,101,116,117,114,110}), new StringMatchRestriction(new int[] {99,97,115,101}), new StringMatchRestriction(new int[] {115,116,114}), new StringMatchRestriction(new int[] {119,104,105,108,101}), new StringMatchRestriction(new int[] {100,121,110,97,109,105,99}), new StringMatchRestriction(new int[] {115,111,108,118,101}), new StringMatchRestriction(new int[] {110,111,116,105,110}), new StringMatchRestriction(new int[] {105,110,115,101,114,116}), new StringMatchRestriction(new int[] {101,108,115,101}), new StringMatchRestriction(new int[] {116,121,112,101}), new StringMatchRestriction(new int[] {116,114,121}), new StringMatchRestriction(new int[] {99,97,116,99,104}), new StringMatchRestriction(new int[] {110,117,109}), new StringMatchRestriction(new int[] {109,111,100}), new StringMatchRestriction(new int[] {110,111,100,101}), new StringMatchRestriction(new int[] {102,105,110,97,108,108,121}), new StringMatchRestriction(new int[] {112,114,105,118,97,116,101}), new StringMatchRestriction(new int[] {116,114,117,101}), new StringMatchRestriction(new int[] {98,97,103}), new StringMatchRestriction(new int[] {118,111,105,100}), new StringMatchRestriction(new int[] {110,111,110,45,97,115,115,111,99}), new StringMatchRestriction(new int[] {97,115,115,111,99}), new StringMatchRestriction(new int[] {116,101,115,116}), new StringMatchRestriction(new int[] {105,102}), new StringMatchRestriction(new int[] {102,97,105,108}), new StringMatchRestriction(new int[] {108,105,115,116}), new StringMatchRestriction(new int[] {114,101,97,108}), new StringMatchRestriction(new int[] {114,101,108}), new StringMatchRestriction(new int[] {116,97,103}), new StringMatchRestriction(new int[] {101,120,116,101,110,100}), new StringMatchRestriction(new int[] {97,112,112,101,110,100}), new StringMatchRestriction(new int[] {116,104,114,111,119}), new StringMatchRestriction(new int[] {111,110,101}), new StringMatchRestriction(new int[] {115,116,97,114,116}), new StringMatchRestriction(new int[] {115,101,116}), new StringMatchRestriction(new int[] {109,111,100,117,108,101}), new StringMatchRestriction(new int[] {97,110,121}), new StringMatchRestriction(new int[] {105,110,116}), new StringMatchRestriction(new int[] {111}), new StringMatchRestriction(new int[] {112,117,98,108,105,99}), new StringMatchRestriction(new int[] {98,111,111,108}), new StringMatchRestriction(new int[] {118,97,108,117,101}), new StringMatchRestriction(new int[] {98,114,101,97,107}), new StringMatchRestriction(new int[] {102,105,108,116,101,114}), new StringMatchRestriction(new int[] {100,97,116,101,116,105,109,101}), new StringMatchRestriction(new int[] {97,115,115,101,114,116}), new StringMatchRestriction(new int[] {108,111,99}), new StringMatchRestriction(new int[] {100,101,102,97,117,108,116}), new StringMatchRestriction(new int[] {116,104,114,111,119,115}), new StringMatchRestriction(new int[] {116,117,112,108,101}), new StringMatchRestriction(new int[] {102,111,114}), new StringMatchRestriction(new int[] {118,105,115,105,116}), new StringMatchRestriction(new int[] {97,108,105,97,115}), new StringMatchRestriction(new int[] {109,97,112})});
      builder.addAlternative(ObjectRascalRascal.prod__Name__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    protected static final void _init_prod__Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new ListStackNode<IConstructor>(4808, 2, regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(4802, 0, new int[][]{{45,45},{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{45,45},{48,57},{65,90},{95,95},{97,122}})});
      tmp[1] = new CharStackNode<IConstructor>(4800, 1, new int[][]{{65,90},{95,95},{97,122}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4798, 0, new int[][]{{92,92}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Name__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
        _init_prod__Name__char_class___range__92_92_char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class NamedBackslash {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NamedBackslash__char_class___range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(8946, 0, new int[][]{{92,92}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{60,60},{62,62},{92,92}})});
      builder.addAlternative(ObjectRascalRascal.prod__NamedBackslash__char_class___range__92_92_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__NamedBackslash__char_class___range__92_92_(builder);
      
    }
  }
	
  protected static class NamedRegExp {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(5864, 0, new int[][]{{0,46},{48,59},{61,61},{63,91},{93,16777215}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_, tmp);
	}
    protected static final void _init_prod__NamedRegExp__NamedBackslash_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5874, 0, "NamedBackslash", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NamedRegExp__NamedBackslash_, tmp);
	}
    protected static final void _init_prod__NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(5870, 1, new int[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(5868, 0, new int[][]{{92,92}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__NamedRegExp__lit___60_Name_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(5860, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5858, 1, "Name", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5856, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NamedRegExp__lit___60_Name_lit___62_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__NamedRegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(builder);
      
        _init_prod__NamedRegExp__NamedBackslash_(builder);
      
        _init_prod__NamedRegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
        _init_prod__NamedRegExp__lit___60_Name_lit___62_(builder);
      
    }
  }
	
  protected static class Nonterminal {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Nonterminal__char_class___range__65_90_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(2992, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(2984, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(2982, 0, new int[][]{{65,90}}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{65,90}})}, null);
      builder.addAlternative(ObjectRascalRascal.prod__Nonterminal__char_class___range__65_90_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Nonterminal__char_class___range__65_90_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class NonterminalLabel {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__NonterminalLabel__char_class___range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(1070, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(1064, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(1062, 0, new int[][]{{97,122}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__NonterminalLabel__char_class___range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__NonterminalLabel__char_class___range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class OctalIntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__OctalIntegerLiteral__char_class___range__48_48_iter__char_class___range__48_55_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(11314, 1, regular__iter__char_class___range__48_55, new CharStackNode<IConstructor>(11308, 0, new int[][]{{48,55}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[0] = new CharStackNode<IConstructor>(11306, 0, new int[][]{{48,48}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__OctalIntegerLiteral__char_class___range__48_48_iter__char_class___range__48_55_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__OctalIntegerLiteral__char_class___range__48_48_iter__char_class___range__48_55_(builder);
      
    }
  }
	
  protected static class PathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PathChars__URLChars_char_class___range__124_124_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(5596, 1, new int[][]{{124,124}}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5594, 0, "URLChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PathChars__URLChars_char_class___range__124_124_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__PathChars__URLChars_char_class___range__124_124_(builder);
      
    }
  }
	
  protected static class PostPathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PostPathChars__lit___62_URLChars_lit___124_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(8978, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(8976, 1, "URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(8974, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PostPathChars__lit___62_URLChars_lit___124_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__PostPathChars__lit___62_URLChars_lit___124_(builder);
      
    }
  }
	
  protected static class PostProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PostProtocolChars__lit___62_URLChars_lit___58_47_47_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(6484, 2, prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_, new int[] {58,47,47}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6482, 1, "URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(6480, 0, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PostProtocolChars__lit___62_URLChars_lit___58_47_47_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__PostProtocolChars__lit___62_URLChars_lit___58_47_47_(builder);
      
    }
  }
	
  protected static class PostStringChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PostStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(5814, 2, new int[][]{{34,34}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(5812, 1, regular__iter_star__StringCharacter, new NonTerminalStackNode<IConstructor>(5810, 0, "StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(5808, 0, new int[][]{{62,62}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PostStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__PostStringChars__char_class___range__62_62_iter_star__StringCharacter_char_class___range__34_34__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class PrePathChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PrePathChars__URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new LiteralStackNode<IConstructor>(6348, 1, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(6346, 0, "URLChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PrePathChars__URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__PrePathChars__URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class PreProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PreProtocolChars__lit___124_URLChars_lit___60_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(212, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(210, 1, "URLChars", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(208, 0, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PreProtocolChars__lit___124_URLChars_lit___60_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__PreProtocolChars__lit___124_URLChars_lit___60_(builder);
      
    }
  }
	
  protected static class PreStringChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__PreStringChars__char_class___range__34_34_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(8598, 2, new int[][]{{60,60}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(8596, 1, regular__iter_star__StringCharacter, new NonTerminalStackNode<IConstructor>(8594, 0, "StringCharacter", null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(8592, 0, new int[][]{{34,34}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__PreStringChars__char_class___range__34_34_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__PreStringChars__char_class___range__34_34_iter_star__StringCharacter_char_class___range__60_60__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class ProtocolChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__ProtocolChars__char_class___range__124_124_URLChars_lit___58_47_47_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(3630, 2, prod__lit___58_47_47__char_class___range__58_58_char_class___range__47_47_char_class___range__47_47_, new int[] {58,47,47}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,10},{13,13},{32,32},{65,65},{160,160},{5760,5760},{8192,8192},{8239,8239},{8287,8287},{12288,12288}})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(3624, 1, "URLChars", null, null);
      tmp[0] = new CharStackNode<IConstructor>(3622, 0, new int[][]{{124,124}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ProtocolChars__char_class___range__124_124_URLChars_lit___58_47_47_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__ProtocolChars__char_class___range__124_124_URLChars_lit___58_47_47_(builder);
      
    }
  }
	
  protected static class RationalLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(11064, 2, new int[][]{{114,114}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(11062, 1, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(11060, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(11058, 0, new int[][]{{48,57}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_, tmp);
	}
    protected static final void _init_prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_iter_star__char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new ListStackNode<IConstructor>(11054, 4, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(11048, 0, new int[][]{{48,57}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})});
      tmp[3] = new CharStackNode<IConstructor>(11046, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(11044, 2, new int[][]{{114,114}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(11042, 1, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(11040, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(11038, 0, new int[][]{{49,57}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_iter_star__char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__RationalLiteral__char_class___range__48_57_iter_star__char_class___range__48_57_char_class___range__114_114_(builder);
      
        _init_prod__RationalLiteral__char_class___range__49_57_iter_star__char_class___range__48_57_char_class___range__114_114_char_class___range__48_57_iter_star__char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class RealLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(8072, 1, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null);
      tmp[0] = new ListStackNode<IConstructor>(8070, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(8068, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__RealLiteral__lit___46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode<IConstructor>(7964, 0, prod__lit___46__char_class___range__46_46_, new int[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{46,46}})}, null);
      tmp[1] = new ListStackNode<IConstructor>(7968, 1, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(7966, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new OptionalStackNode<IConstructor>(7972, 2, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(7970, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__lit___46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new OptionalStackNode<IConstructor>(8044, 3, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(8042, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[2] = new ListStackNode<IConstructor>(8040, 2, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(8038, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(8036, 1, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {46})});
      tmp[0] = new ListStackNode<IConstructor>(8030, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(8028, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new OptionalStackNode<IConstructor>(8064, 4, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(8062, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[3] = new ListStackNode<IConstructor>(8060, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(8058, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new OptionalStackNode<IConstructor>(8056, 2, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode<IConstructor>(8054, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[1] = new CharStackNode<IConstructor>(8052, 1, new int[][]{{69,69},{101,101}}, null, null);
      tmp[0] = new ListStackNode<IConstructor>(8050, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(8048, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__RealLiteral__lit___46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[6];
      
      tmp[5] = new OptionalStackNode<IConstructor>(8024, 5, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(8022, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[4] = new ListStackNode<IConstructor>(8020, 4, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(8018, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[3] = new OptionalStackNode<IConstructor>(8016, 3, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode<IConstructor>(8014, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(8006, 0, prod__lit___46__char_class___range__46_46_, new int[] {46}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{46,46}})}, null);
      tmp[1] = new ListStackNode<IConstructor>(8010, 1, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(8008, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new CharStackNode<IConstructor>(8012, 2, new int[][]{{69,69},{101,101}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__lit___46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    protected static final void _init_prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new OptionalStackNode<IConstructor>(7998, 6, regular__opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102, new CharStackNode<IConstructor>(7996, 0, new int[][]{{68,68},{70,70},{100,100},{102,102}}, null, null), null, null);
      tmp[5] = new ListStackNode<IConstructor>(7994, 5, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(7992, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[4] = new OptionalStackNode<IConstructor>(7990, 4, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode<IConstructor>(7988, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[3] = new CharStackNode<IConstructor>(7986, 3, new int[][]{{69,69},{101,101}}, null, null);
      tmp[2] = new ListStackNode<IConstructor>(7984, 2, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(7982, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(7980, 1, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[0] = new ListStackNode<IConstructor>(7978, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(7976, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__RealLiteral__iter__char_class___range__48_57_char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__RealLiteral__lit___46_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__RealLiteral__iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__RealLiteral__lit___46_iter__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
        _init_prod__RealLiteral__iter__char_class___range__48_57_lit___46_iter_star__char_class___range__48_57_char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_opt__char_class___range__68_68_range__70_70_range__100_100_range__102_102_(builder);
      
    }
  }
	
  protected static class RegExp {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(7622, 0, new int[][]{{0,46},{48,59},{61,61},{63,91},{93,16777215}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_, tmp);
	}
    protected static final void _init_prod__RegExp__Backslash_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(7634, 0, "Backslash", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__Backslash_, tmp);
	}
    protected static final void _init_prod__RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(7604, 1, new int[][]{{47,47},{60,60},{62,62},{92,92}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(7602, 0, new int[][]{{92,92}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_, tmp);
	}
    protected static final void _init_prod__RegExp__lit___60_Name_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(7630, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(7628, 1, "Name", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(7626, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__lit___60_Name_lit___62_, tmp);
	}
    protected static final void _init_prod__RegExp__lit___60_Name_lit___58_iter_star__NamedRegExp_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(7618, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(7616, 3, regular__iter_star__NamedRegExp, new NonTerminalStackNode<IConstructor>(7614, 0, "NamedRegExp", null, null), false, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(7612, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(7610, 1, "Name", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(7608, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExp__lit___60_Name_lit___58_iter_star__NamedRegExp_lit___62_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__RegExp__char_class___range__0_46_range__48_59_range__61_61_range__63_91_range__93_16777215_(builder);
      
        _init_prod__RegExp__Backslash_(builder);
      
        _init_prod__RegExp__char_class___range__92_92_char_class___range__47_47_range__60_60_range__62_62_range__92_92_(builder);
      
        _init_prod__RegExp__lit___60_Name_lit___62_(builder);
      
        _init_prod__RegExp__lit___60_Name_lit___58_iter_star__NamedRegExp_lit___62_(builder);
      
    }
  }
	
  protected static class RegExpLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RegExpLiteral__lit___47_iter_star__RegExp_lit___47_RegExpModifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(5848, 3, "RegExpModifier", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5846, 2, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(5844, 1, regular__iter_star__RegExp, new NonTerminalStackNode<IConstructor>(5842, 0, "RegExp", null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5840, 0, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExpLiteral__lit___47_iter_star__RegExp_lit___47_RegExpModifier_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__RegExpLiteral__lit___47_iter_star__RegExp_lit___47_RegExpModifier_(builder);
      
    }
  }
	
  protected static class RegExpModifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(7716, 0, regular__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115, new CharStackNode<IConstructor>(7714, 0, new int[][]{{100,100},{105,105},{109,109},{115,115}}, null, null), false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__RegExpModifier__iter_star__char_class___range__100_100_range__105_105_range__109_109_range__115_115_(builder);
      
    }
  }
	
  protected static class Rest {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Rest__iter_star__char_class___range__0_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(8284, 0, regular__iter_star__char_class___range__0_16777215, new CharStackNode<IConstructor>(8278, 0, new int[][]{{0,16777215}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{0,16777215}})});
      builder.addAlternative(ObjectRascalRascal.prod__Rest__iter_star__char_class___range__0_16777215_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__Rest__iter_star__char_class___range__0_16777215_(builder);
      
    }
  }
	
  protected static class StringCharacter {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(8232, 0, new int[][]{{0,33},{35,38},{40,59},{61,61},{63,91},{93,16777215}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_, tmp);
	}
    protected static final void _init_prod__StringCharacter__UnicodeEscape_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(8228, 0, "UnicodeEscape", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringCharacter__UnicodeEscape_, tmp);
	}
    protected static final void _init_prod__StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new CharStackNode<IConstructor>(8214, 1, new int[][]{{34,34},{39,39},{60,60},{62,62},{92,92},{98,98},{102,102},{110,110},{114,114},{116,116}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(8212, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_, tmp);
	}
    protected static final void _init_prod__StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288_char_class___range__39_39_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(8224, 2, new int[][]{{39,39}}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(8222, 1, regular__iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288, new CharStackNode<IConstructor>(8220, 0, new int[][]{{9,9},{32,32},{65,65},{160,160},{5760,5760},{8192,8192},{8239,8239},{8287,8287},{12288,12288}}, null, null), false, null, null);
      tmp[0] = new CharStackNode<IConstructor>(8218, 0, new int[][]{{10,10}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288_char_class___range__39_39_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__StringCharacter__char_class___range__0_33_range__35_38_range__40_59_range__61_61_range__63_91_range__93_16777215_(builder);
      
        _init_prod__StringCharacter__UnicodeEscape_(builder);
      
        _init_prod__StringCharacter__lit___92_char_class___range__34_34_range__39_39_range__60_60_range__62_62_range__92_92_range__98_98_range__102_102_range__110_110_range__114_114_range__116_116_(builder);
      
        _init_prod__StringCharacter__char_class___range__10_10_iter_star__char_class___range__9_9_range__32_32_range__65_65_range__160_160_range__5760_5760_range__8192_8192_range__8239_8239_range__8287_8287_range__12288_12288_char_class___range__39_39_(builder);
      
    }
  }
	
  protected static class StringConstant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__StringConstant__lit___34_iter_star__StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(4390, 2, prod__lit___34__char_class___range__34_34_, new int[] {34}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(4388, 1, regular__iter_star__StringCharacter, new NonTerminalStackNode<IConstructor>(4386, 0, "StringCharacter", null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4384, 0, prod__lit___34__char_class___range__34_34_, new int[] {34}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__StringConstant__lit___34_iter_star__StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__StringConstant__lit___34_iter_star__StringCharacter_lit___34__tag__category___67_111_110_115_116_97_110_116(builder);
      
    }
  }
	
  protected static class TagString {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TagString__lit___123_contents_iter_star__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(2954, 2, prod__lit___125__char_class___range__125_125_, new int[] {125}, new IEnterFilter[] {new StringPrecedeRestriction(new int[] {92})}, null);
      tmp[1] = new ListStackNode<IConstructor>(2946, 1, regular__iter_star__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125, new AlternativeStackNode<IConstructor>(2944, 0, regular__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2934, 0, "TagString", null, null), new CharStackNode<IConstructor>(2936, 0, new int[][]{{0,122},{124,124},{126,16777215}}, null, null), new SequenceStackNode<IConstructor>(2942, 0, regular__seq___lit___92_char_class___range__123_123_range__125_125, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new LiteralStackNode<IConstructor>(2938, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null), new CharStackNode<IConstructor>(2940, 1, new int[][]{{123,123},{125,125}}, null, null)}, null, null)}, null, null), false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2932, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, new IEnterFilter[] {new StringPrecedeRestriction(new int[] {92})}, null);
      builder.addAlternative(ObjectRascalRascal.prod__TagString__lit___123_contents_iter_star__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__TagString__lit___123_contents_iter_star__alt___TagString_char_class___range__0_122_range__124_124_range__126_16777215_seq___lit___92_char_class___range__123_123_range__125_125_lit___125_(builder);
      
    }
  }
	
  protected static class TimePartNoTZ {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new OptionalStackNode<IConstructor>(4898, 6, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(4896, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(4882, 0, new int[][]{{44,44},{46,46}}, null, null), new CharStackNode<IConstructor>(4884, 1, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(4894, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(4892, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(4886, 0, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(4890, 1, regular__opt__char_class___range__48_57, new CharStackNode<IConstructor>(4888, 0, new int[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[5] = new CharStackNode<IConstructor>(4880, 5, new int[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4878, 4, new int[][]{{48,53}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(4876, 3, new int[][]{{48,57}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(4874, 2, new int[][]{{48,53}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(4872, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4870, 0, new int[][]{{48,50}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new OptionalStackNode<IConstructor>(4934, 8, regular__opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(4932, 0, regular__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(4918, 0, new int[][]{{44,44},{46,46}}, null, null), new CharStackNode<IConstructor>(4920, 1, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(4930, 2, regular__opt__seq___char_class___range__48_57_opt__char_class___range__48_57, new SequenceStackNode<IConstructor>(4928, 0, regular__seq___char_class___range__48_57_opt__char_class___range__48_57, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(4922, 0, new int[][]{{48,57}}, null, null), new OptionalStackNode<IConstructor>(4926, 1, regular__opt__char_class___range__48_57, new CharStackNode<IConstructor>(4924, 0, new int[][]{{48,57}}, null, null), null, null)}, null, null), null, null)}, null, null), null, null);
      tmp[7] = new CharStackNode<IConstructor>(4916, 7, new int[][]{{48,57}}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(4914, 6, new int[][]{{48,53}}, null, null);
      tmp[5] = new LiteralStackNode<IConstructor>(4912, 5, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4910, 4, new int[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(4908, 3, new int[][]{{48,53}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4906, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(4904, 1, new int[][]{{48,57}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4902, 0, new int[][]{{48,50}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(builder);
      
        _init_prod__TimePartNoTZ__char_class___range__48_50_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_opt__seq___char_class___range__44_44_range__46_46_char_class___range__48_57_opt__seq___char_class___range__48_57_opt__char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class TimeZonePart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__TimeZonePart__lit_Z_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(8554, 0, prod__lit_Z__char_class___range__90_90_, new int[] {90}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimeZonePart__lit_Z_, tmp);
	}
    protected static final void _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(8550, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(8548, 1, new int[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(8546, 0, new int[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(8542, 4, new int[][]{{48,57}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(8540, 3, new int[][]{{48,53}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(8538, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(8536, 1, new int[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(8534, 0, new int[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_, tmp);
	}
    protected static final void _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[6];
      
      tmp[5] = new CharStackNode<IConstructor>(8530, 5, new int[][]{{48,57}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(8528, 4, new int[][]{{48,53}}, null, null);
      tmp[3] = new LiteralStackNode<IConstructor>(8526, 3, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(8524, 2, new int[][]{{48,57}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(8522, 1, new int[][]{{48,49}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(8520, 0, new int[][]{{43,43},{45,45}}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__TimeZonePart__lit_Z_(builder);
      
        _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_(builder);
      
        _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_char_class___range__48_53_char_class___range__48_57_(builder);
      
        _init_prod__TimeZonePart__char_class___range__43_43_range__45_45_char_class___range__48_49_char_class___range__48_57_lit___58_char_class___range__48_53_char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class URLChars {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(8436, 0, regular__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215, new CharStackNode<IConstructor>(8434, 0, new int[][]{{0,8},{11,12},{14,31},{33,59},{61,123},{125,16777215}}, null, null), false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__URLChars__iter_star__char_class___range__0_8_range__11_12_range__14_31_range__33_59_range__61_123_range__125_16777215_(builder);
      
    }
  }
	
  protected static class UnicodeEscape {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__ascii_UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new CharStackNode<IConstructor>(11116, 3, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(11114, 2, new int[][]{{48,55}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(11112, 1, new int[][]{{97,97}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11110, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ascii_UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    protected static final void _init_prod__utf16_UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[6];
      
      tmp[5] = new CharStackNode<IConstructor>(11104, 5, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(11102, 4, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(11100, 3, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(11098, 2, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(11096, 1, new int[][]{{117,117}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11094, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__utf16_UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    protected static final void _init_prod__utf32_UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[8];
      
      tmp[7] = new CharStackNode<IConstructor>(11088, 7, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[6] = new CharStackNode<IConstructor>(11086, 6, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[5] = new CharStackNode<IConstructor>(11084, 5, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(11082, 4, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(11080, 3, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[2] = new CharStackNode<IConstructor>(11078, 2, new int[][]{{48,57},{65,70},{97,102}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(11076, 1, new int[][]{{85,85}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11074, 0, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__utf32_UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__ascii_UnicodeEscape__lit___92_char_class___range__97_97_char_class___range__48_55_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
        _init_prod__utf16_UnicodeEscape__lit___92_char_class___range__117_117_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
        _init_prod__utf32_UnicodeEscape__lit___92_char_class___range__85_85_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_char_class___range__48_57_range__65_70_range__97_102_(builder);
      
    }
  }
	
  protected static class Mapping__Expression {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Mapping__Expression__from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(10688, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10686, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(10684, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10682, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(10678, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Mapping__Expression__from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Mapping__Expression__from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_(builder);
      
    }
  }
	
  protected static class Mapping__Pattern {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Mapping__Pattern__from_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4328, 4, "Pattern", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4326, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4324, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4322, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4318, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Mapping__Pattern__from_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Pattern_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Mapping__Pattern__from_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Pattern_(builder);
      
    }
  }
	
  protected static class Assignable {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__annotation_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_annotation_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(3824, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3822, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3820, 2, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3818, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3814, 0, "Assignable", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__annotation_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_annotation_Name_, tmp);
	}
    protected static final void _init_prod__bracket_Assignable__lit___40_layouts_LAYOUTLIST_arg_Assignable_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(3758, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3756, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(3752, 2, "Assignable", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3750, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3748, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__bracket_Assignable__lit___40_layouts_LAYOUTLIST_arg_Assignable_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__constructor_Assignable__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(3896, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3894, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(3890, 4, regular__iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3882, 0, "Assignable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3884, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(3886, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(3888, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3880, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3878, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3876, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3872, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__constructor_Assignable__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__fieldAccess_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(3842, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3840, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3838, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3836, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3832, 0, "Assignable", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__fieldAccess_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_, tmp);
	}
    protected static final void _init_prod__ifDefinedOrDefault_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_defaultExpression_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(3782, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3780, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3778, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3776, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3772, 0, "Assignable", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ifDefinedOrDefault_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_defaultExpression_Expression_, tmp);
	}
    protected static final void _init_prod__subscript_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscript_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(3866, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3864, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(3860, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3858, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3856, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3854, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3850, 0, "Assignable", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__subscript_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscript_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__tuple_Assignable__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3790, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3792, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(3802, 2, regular__iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3794, 0, "Assignable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3796, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(3798, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(3800, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3806, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(3808, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__tuple_Assignable__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__variable_Assignable__qualifiedName_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3764, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__variable_Assignable__qualifiedName_QualifiedName_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__annotation_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_annotation_Name_(builder);
      
        _init_prod__bracket_Assignable__lit___40_layouts_LAYOUTLIST_arg_Assignable_layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__constructor_Assignable__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__fieldAccess_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_(builder);
      
        _init_prod__ifDefinedOrDefault_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_defaultExpression_Expression_(builder);
      
        _init_prod__subscript_Assignable__receiver_Assignable_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscript_Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__tuple_Assignable__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Assignable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__variable_Assignable__qualifiedName_QualifiedName_(builder);
      
    }
  }
	
  protected static class Assignment {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__addition_Assignment__lit___43_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3614, 0, prod__lit___43_61__char_class___range__43_43_char_class___range__61_61_, new int[] {43,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__addition_Assignment__lit___43_61_, tmp);
	}
    protected static final void _init_prod__append_Assignment__lit___60_60_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3584, 0, prod__lit___60_60_61__char_class___range__60_60_char_class___range__60_60_char_class___range__61_61_, new int[] {60,60,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__append_Assignment__lit___60_60_61_, tmp);
	}
    protected static final void _init_prod__default_Assignment__lit___61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3590, 0, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Assignment__lit___61_, tmp);
	}
    protected static final void _init_prod__division_Assignment__lit___47_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3602, 0, prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_, new int[] {47,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__division_Assignment__lit___47_61_, tmp);
	}
    protected static final void _init_prod__ifDefined_Assignment__lit___63_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3596, 0, prod__lit___63_61__char_class___range__63_63_char_class___range__61_61_, new int[] {63,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ifDefined_Assignment__lit___63_61_, tmp);
	}
    protected static final void _init_prod__intersection_Assignment__lit___38_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3578, 0, prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_, new int[] {38,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__intersection_Assignment__lit___38_61_, tmp);
	}
    protected static final void _init_prod__product_Assignment__lit___42_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3608, 0, prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_, new int[] {42,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__product_Assignment__lit___42_61_, tmp);
	}
    protected static final void _init_prod__subtraction_Assignment__lit___45_61_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3572, 0, prod__lit___45_61__char_class___range__45_45_char_class___range__61_61_, new int[] {45,61}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__subtraction_Assignment__lit___45_61_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__addition_Assignment__lit___43_61_(builder);
      
        _init_prod__append_Assignment__lit___60_60_61_(builder);
      
        _init_prod__default_Assignment__lit___61_(builder);
      
        _init_prod__division_Assignment__lit___47_61_(builder);
      
        _init_prod__ifDefined_Assignment__lit___63_61_(builder);
      
        _init_prod__intersection_Assignment__lit___38_61_(builder);
      
        _init_prod__product_Assignment__lit___42_61_(builder);
      
        _init_prod__subtraction_Assignment__lit___45_61_(builder);
      
    }
  }
	
  protected static class Assoc {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__associative_Assoc__lit_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(7748, 0, prod__lit_assoc__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {97,115,115,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__associative_Assoc__lit_assoc_, tmp);
	}
    protected static final void _init_prod__left_Assoc__lit_left_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(7754, 0, prod__lit_left__char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_, new int[] {108,101,102,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__left_Assoc__lit_left_, tmp);
	}
    protected static final void _init_prod__nonAssociative_Assoc__lit_non_assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(7760, 0, prod__lit_non_assoc__char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_, new int[] {110,111,110,45,97,115,115,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__nonAssociative_Assoc__lit_non_assoc_, tmp);
	}
    protected static final void _init_prod__right_Assoc__lit_right_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(7766, 0, prod__lit_right__char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_, new int[] {114,105,103,104,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__right_Assoc__lit_right_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__associative_Assoc__lit_assoc_(builder);
      
        _init_prod__left_Assoc__lit_left_(builder);
      
        _init_prod__nonAssociative_Assoc__lit_non_assoc_(builder);
      
        _init_prod__right_Assoc__lit_right_(builder);
      
    }
  }
	
  protected static class BasicType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__bag_BasicType__lit_bag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5416, 0, prod__lit_bag__char_class___range__98_98_char_class___range__97_97_char_class___range__103_103_, new int[] {98,97,103}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__bag_BasicType__lit_bag_, tmp);
	}
    protected static final void _init_prod__bool_BasicType__lit_bool_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5446, 0, prod__lit_bool__char_class___range__98_98_char_class___range__111_111_char_class___range__111_111_char_class___range__108_108_, new int[] {98,111,111,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__bool_BasicType__lit_bool_, tmp);
	}
    protected static final void _init_prod__dateTime_BasicType__lit_datetime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5422, 0, prod__lit_datetime__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__116_116_char_class___range__105_105_char_class___range__109_109_char_class___range__101_101_, new int[] {100,97,116,101,116,105,109,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__dateTime_BasicType__lit_datetime_, tmp);
	}
    protected static final void _init_prod__int_BasicType__lit_int_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5500, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new int[] {105,110,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__int_BasicType__lit_int_, tmp);
	}
    protected static final void _init_prod__list_BasicType__lit_list_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5482, 0, prod__lit_list__char_class___range__108_108_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_, new int[] {108,105,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__list_BasicType__lit_list_, tmp);
	}
    protected static final void _init_prod__loc_BasicType__lit_loc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5470, 0, prod__lit_loc__char_class___range__108_108_char_class___range__111_111_char_class___range__99_99_, new int[] {108,111,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__loc_BasicType__lit_loc_, tmp);
	}
    protected static final void _init_prod__map_BasicType__lit_map_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5494, 0, prod__lit_map__char_class___range__109_109_char_class___range__97_97_char_class___range__112_112_, new int[] {109,97,112}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__map_BasicType__lit_map_, tmp);
	}
    protected static final void _init_prod__node_BasicType__lit_node_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5506, 0, prod__lit_node__char_class___range__110_110_char_class___range__111_111_char_class___range__100_100_char_class___range__101_101_, new int[] {110,111,100,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__node_BasicType__lit_node_, tmp);
	}
    protected static final void _init_prod__num_BasicType__lit_num_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5476, 0, prod__lit_num__char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new int[] {110,117,109}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__num_BasicType__lit_num_, tmp);
	}
    protected static final void _init_prod__rational_BasicType__lit_rat_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5410, 0, prod__lit_rat__char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_, new int[] {114,97,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__rational_BasicType__lit_rat_, tmp);
	}
    protected static final void _init_prod__real_BasicType__lit_real_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5512, 0, prod__lit_real__char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__108_108_, new int[] {114,101,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__real_BasicType__lit_real_, tmp);
	}
    protected static final void _init_prod__relation_BasicType__lit_rel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5452, 0, prod__lit_rel__char_class___range__114_114_char_class___range__101_101_char_class___range__108_108_, new int[] {114,101,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__relation_BasicType__lit_rel_, tmp);
	}
    protected static final void _init_prod__set_BasicType__lit_set_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5434, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new int[] {115,101,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__set_BasicType__lit_set_, tmp);
	}
    protected static final void _init_prod__string_BasicType__lit_str_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5428, 0, prod__lit_str__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_, new int[] {115,116,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__string_BasicType__lit_str_, tmp);
	}
    protected static final void _init_prod__tuple_BasicType__lit_tuple_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5488, 0, prod__lit_tuple__char_class___range__116_116_char_class___range__117_117_char_class___range__112_112_char_class___range__108_108_char_class___range__101_101_, new int[] {116,117,112,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__tuple_BasicType__lit_tuple_, tmp);
	}
    protected static final void _init_prod__type_BasicType__lit_type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5440, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__type_BasicType__lit_type_, tmp);
	}
    protected static final void _init_prod__value_BasicType__lit_value_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5464, 0, prod__lit_value__char_class___range__118_118_char_class___range__97_97_char_class___range__108_108_char_class___range__117_117_char_class___range__101_101_, new int[] {118,97,108,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__value_BasicType__lit_value_, tmp);
	}
    protected static final void _init_prod__void_BasicType__lit_void_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(5458, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new int[] {118,111,105,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__void_BasicType__lit_void_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__bag_BasicType__lit_bag_(builder);
      
        _init_prod__bool_BasicType__lit_bool_(builder);
      
        _init_prod__dateTime_BasicType__lit_datetime_(builder);
      
        _init_prod__int_BasicType__lit_int_(builder);
      
        _init_prod__list_BasicType__lit_list_(builder);
      
        _init_prod__loc_BasicType__lit_loc_(builder);
      
        _init_prod__map_BasicType__lit_map_(builder);
      
        _init_prod__node_BasicType__lit_node_(builder);
      
        _init_prod__num_BasicType__lit_num_(builder);
      
        _init_prod__rational_BasicType__lit_rat_(builder);
      
        _init_prod__real_BasicType__lit_real_(builder);
      
        _init_prod__relation_BasicType__lit_rel_(builder);
      
        _init_prod__set_BasicType__lit_set_(builder);
      
        _init_prod__string_BasicType__lit_str_(builder);
      
        _init_prod__tuple_BasicType__lit_tuple_(builder);
      
        _init_prod__type_BasicType__lit_type_(builder);
      
        _init_prod__value_BasicType__lit_value_(builder);
      
        _init_prod__void_BasicType__lit_void_(builder);
      
    }
  }
	
  protected static class Body {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__toplevels_Body__toplevels_iter_star_seps__Toplevel__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(6998, 0, regular__iter_star_seps__Toplevel__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(6994, 0, "Toplevel", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(6996, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__toplevels_Body__toplevels_iter_star_seps__Toplevel__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__toplevels_Body__toplevels_iter_star_seps__Toplevel__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Bound {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Bound__lit___59_layouts_LAYOUTLIST_expression_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(10140, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10138, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(10136, 0, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Bound__lit___59_layouts_LAYOUTLIST_expression_Expression_, tmp);
	}
    protected static final void _init_prod__empty_Bound__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(10130, 0);
      builder.addAlternative(ObjectRascalRascal.prod__empty_Bound__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Bound__lit___59_layouts_LAYOUTLIST_expression_Expression_(builder);
      
        _init_prod__empty_Bound__(builder);
      
    }
  }
	
  protected static class Case {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Case__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(10120, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10118, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(10116, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10114, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(10112, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Case__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement__tag__Foldable, tmp);
	}
    protected static final void _init_prod__patternWithAction_Case__lit_case_layouts_LAYOUTLIST_patternWithAction_PatternWithAction__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(10104, 2, "PatternWithAction", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10102, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(10100, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new int[] {99,97,115,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__patternWithAction_Case__lit_case_layouts_LAYOUTLIST_patternWithAction_PatternWithAction__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Case__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement__tag__Foldable(builder);
      
        _init_prod__patternWithAction_Case__lit_case_layouts_LAYOUTLIST_patternWithAction_PatternWithAction__tag__Foldable(builder);
      
    }
  }
	
  protected static class Catch {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__binding_Catch__lit_catch_layouts_LAYOUTLIST_pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(442, 6, "Statement", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(440, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(438, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(436, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(432, 2, "Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(430, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(428, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {99,97,116,99,104}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__binding_Catch__lit_catch_layouts_LAYOUTLIST_pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_, tmp);
	}
    protected static final void _init_prod__default_Catch__lit_catch_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(458, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(456, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(454, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(452, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(450, 0, prod__lit_catch__char_class___range__99_99_char_class___range__97_97_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {99,97,116,99,104}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Catch__lit_catch_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__binding_Catch__lit_catch_layouts_LAYOUTLIST_pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_(builder);
      
        _init_prod__default_Catch__lit_catch_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_body_Statement_(builder);
      
    }
  }
	
  protected static class Class {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__bracket_Class__lit___40_layouts_LAYOUTLIST_charclass_Class_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(10624, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10622, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(10618, 2, "Class", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10616, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(10614, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__bracket_Class__lit___40_layouts_LAYOUTLIST_charclass_Class_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__complement_Class__lit___33_layouts_LAYOUTLIST_charClass_Class_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(10548, 2, "Class", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10546, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(10544, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__complement_Class__lit___33_layouts_LAYOUTLIST_charClass_Class_, tmp);
	}
    protected static final void _init_prod__difference_Class__lhs_Class_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Class__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(10586, 4, "Class", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10584, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(10582, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10580, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(10576, 0, "Class", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__difference_Class__lhs_Class_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Class__assoc__left, tmp);
	}
    protected static final void _init_prod__intersection_Class__lhs_Class_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Class__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(10604, 4, "Class", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10602, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(10600, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new int[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10598, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(10594, 0, "Class", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__intersection_Class__lhs_Class_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Class__assoc__left, tmp);
	}
    protected static final void _init_prod__simpleCharclass_Class__lit___91_layouts_LAYOUTLIST_ranges_iter_star_seps__Range__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(10570, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10568, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(10564, 2, regular__iter_star_seps__Range__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(10560, 0, "Range", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(10562, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10558, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(10556, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__simpleCharclass_Class__lit___91_layouts_LAYOUTLIST_ranges_iter_star_seps__Range__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__union_Class__lhs_Class_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Class__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(10640, 4, "Class", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10638, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(10636, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new int[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10634, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(10630, 0, "Class", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__union_Class__lhs_Class_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Class__assoc__left, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__bracket_Class__lit___40_layouts_LAYOUTLIST_charclass_Class_layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__complement_Class__lit___33_layouts_LAYOUTLIST_charClass_Class_(builder);
      
        _init_prod__difference_Class__lhs_Class_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Class__assoc__left(builder);
      
        _init_prod__intersection_Class__lhs_Class_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Class__assoc__left(builder);
      
        _init_prod__simpleCharclass_Class__lit___91_layouts_LAYOUTLIST_ranges_iter_star_seps__Range__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__union_Class__lhs_Class_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Class__assoc__left(builder);
      
    }
  }
	
  protected static class Command {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__declaration_Command__declaration_Declaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(8636, 0, "Declaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__declaration_Command__declaration_Declaration_, tmp);
	}
    protected static final void _init_prod__expression_Command__expression_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(8646, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__expression_Command__expression_Expression_, tmp);
	}
    protected static final void _init_prod__import_Command__imported_Import_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(8654, 0, "Import", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__import_Command__imported_Import_, tmp);
	}
    protected static final void _init_prod__shell_Command__lit___58_layouts_LAYOUTLIST_command_ShellCommand_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(8666, 2, "ShellCommand", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(8664, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(8662, 0, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__shell_Command__lit___58_layouts_LAYOUTLIST_command_ShellCommand_, tmp);
	}
    protected static final void _init_prod__statement_Command__statement_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(8628, 0, "Statement", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__statement_Command__statement_Statement_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__declaration_Command__declaration_Declaration_(builder);
      
        _init_prod__expression_Command__expression_Expression_(builder);
      
        _init_prod__import_Command__imported_Import_(builder);
      
        _init_prod__shell_Command__lit___58_layouts_LAYOUTLIST_command_ShellCommand_(builder);
      
        _init_prod__statement_Command__statement_Statement_(builder);
      
    }
  }
	
  protected static class Commands {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__list_Commands__commands_iter_seps__EvalCommand__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(1300, 0, regular__iter_seps__EvalCommand__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1296, 0, "EvalCommand", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1298, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__list_Commands__commands_iter_seps__EvalCommand__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__list_Commands__commands_iter_seps__EvalCommand__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Comprehension {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__list_Comprehension__lit___91_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(10790, 8, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(10788, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(10784, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(10776, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(10778, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(10780, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(10782, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(10774, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(10772, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10770, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(10766, 2, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(10758, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(10760, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(10762, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(10764, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10756, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(10754, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__list_Comprehension__lit___91_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125, tmp);
	}
    protected static final void _init_prod__map_Comprehension__lit___40_layouts_LAYOUTLIST_from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41__tag__breakable___123_102_114_111_109_44_116_111_44_103_101_110_101_114_97_116_111_114_115_125(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(10748, 12, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(10746, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(10742, 10, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(10734, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(10736, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(10738, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(10740, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(10732, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(10730, 8, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(10728, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(10724, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(10722, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(10720, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10718, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(10714, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10712, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(10710, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__map_Comprehension__lit___40_layouts_LAYOUTLIST_from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41__tag__breakable___123_102_114_111_109_44_116_111_44_103_101_110_101_114_97_116_111_114_115_125, tmp);
	}
    protected static final void _init_prod__set_Comprehension__lit___123_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(10832, 8, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(10830, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(10826, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(10818, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(10820, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(10822, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(10824, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(10816, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(10814, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10812, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(10808, 2, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(10800, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(10802, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(10804, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(10806, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10798, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(10796, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__set_Comprehension__lit___123_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__list_Comprehension__lit___91_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125(builder);
      
        _init_prod__map_Comprehension__lit___40_layouts_LAYOUTLIST_from_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_to_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41__tag__breakable___123_102_114_111_109_44_116_111_44_103_101_110_101_114_97_116_111_114_115_125(builder);
      
        _init_prod__set_Comprehension__lit___123_layouts_LAYOUTLIST_results_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125__tag__breakable___123_114_101_115_117_108_116_115_44_103_101_110_101_114_97_116_111_114_115_125(builder);
      
    }
  }
	
  protected static class DataTarget {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__empty_DataTarget__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(7934, 0);
      builder.addAlternative(ObjectRascalRascal.prod__empty_DataTarget__, tmp);
	}
    protected static final void _init_prod__labeled_DataTarget__label_Name_layouts_LAYOUTLIST_lit___58_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(7946, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(7944, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(7940, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__labeled_DataTarget__label_Name_layouts_LAYOUTLIST_lit___58_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__empty_DataTarget__(builder);
      
        _init_prod__labeled_DataTarget__label_Name_layouts_LAYOUTLIST_lit___58_(builder);
      
    }
  }
	
  protected static class DataTypeSelector {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__selector_DataTypeSelector__sort_QualifiedName_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_production_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4442, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4440, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4438, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4436, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4432, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__selector_DataTypeSelector__sort_QualifiedName_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_production_Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__selector_DataTypeSelector__sort_QualifiedName_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_production_Name_(builder);
      
    }
  }
	
  protected static class DateTimeLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__dateAndTimeLiteral_DateTimeLiteral__dateAndTime_DateAndTime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5546, 0, "DateAndTime", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__dateAndTimeLiteral_DateTimeLiteral__dateAndTime_DateAndTime_, tmp);
	}
    protected static final void _init_prod__dateLiteral_DateTimeLiteral__date_JustDate_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5562, 0, "JustDate", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__dateLiteral_DateTimeLiteral__date_JustDate_, tmp);
	}
    protected static final void _init_prod__timeLiteral_DateTimeLiteral__time_JustTime_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5554, 0, "JustTime", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__timeLiteral_DateTimeLiteral__time_JustTime_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__dateAndTimeLiteral_DateTimeLiteral__dateAndTime_DateAndTime_(builder);
      
        _init_prod__dateLiteral_DateTimeLiteral__date_JustDate_(builder);
      
        _init_prod__timeLiteral_DateTimeLiteral__time_JustTime_(builder);
      
    }
  }
	
  protected static class Declaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__alias_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_alias_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_base_Type_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(10280, 12, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(10278, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(10274, 10, "Type", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(10272, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(10270, 8, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(10268, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(10264, 6, "UserType", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(10262, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(10260, 4, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new int[] {97,108,105,97,115}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10258, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(10254, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10252, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(10248, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__alias_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_alias_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_base_Type_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__annotation_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_anno_layouts_LAYOUTLIST_annoType_Type_layouts_LAYOUTLIST_onType_Type_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(10390, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(10388, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(10384, 12, "Name", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(10382, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(10380, 10, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(10378, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(10374, 8, "Type", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(10372, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(10368, 6, "Type", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(10366, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(10364, 4, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new int[] {97,110,110,111}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10362, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(10358, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10356, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(10352, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__annotation_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_anno_layouts_LAYOUTLIST_annoType_Type_layouts_LAYOUTLIST_onType_Type_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__data_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_variants_iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(10444, 12, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(10442, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(10438, 10, regular__iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(10430, 0, "Variant", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(10432, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(10434, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null), new NonTerminalStackNode<IConstructor>(10436, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(10428, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(10426, 8, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(10424, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(10420, 6, "UserType", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(10418, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(10416, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10414, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(10410, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10408, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(10404, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__data_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_variants_iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__dataAbstract_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(10308, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(10306, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(10302, 6, "UserType", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(10300, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(10298, 4, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10296, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(10292, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10290, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(10286, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__dataAbstract_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__function_Declaration__functionDeclaration_FunctionDeclaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(10396, 0, "FunctionDeclaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__function_Declaration__functionDeclaration_FunctionDeclaration_, tmp);
	}
    protected static final void _init_prod__tag_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_tag_layouts_LAYOUTLIST_kind_Kind_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit_on_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(10496, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(10494, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(10490, 12, regular__iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(10482, 0, "Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(10484, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(10486, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(10488, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(10480, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(10478, 10, prod__lit_on__char_class___range__111_111_char_class___range__110_110_, new int[] {111,110}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(10476, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(10472, 8, "Name", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(10470, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(10466, 6, "Kind", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(10464, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(10462, 4, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new int[] {116,97,103}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10460, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(10456, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10454, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(10450, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__tag_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_tag_layouts_LAYOUTLIST_kind_Kind_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit_on_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__variable_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(10346, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(10344, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(10340, 6, regular__iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(10332, 0, "Variable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(10334, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(10336, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(10338, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(10330, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(10326, 4, "Type", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10324, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(10320, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10318, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(10314, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__variable_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__alias_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_alias_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_base_Type_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__annotation_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_anno_layouts_LAYOUTLIST_annoType_Type_layouts_LAYOUTLIST_onType_Type_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__data_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_variants_iter_seps__Variant__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__dataAbstract_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_data_layouts_LAYOUTLIST_user_UserType_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__function_Declaration__functionDeclaration_FunctionDeclaration_(builder);
      
        _init_prod__tag_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_lit_tag_layouts_LAYOUTLIST_kind_Kind_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit_on_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__variable_Declaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(builder);
      
    }
  }
	
  protected static class Declarator {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Declarator__type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode<IConstructor>(5096, 2, regular__iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5088, 0, "Variable", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5090, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5092, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5094, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5086, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5082, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Declarator__type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Declarator__type_Type_layouts_LAYOUTLIST_variables_iter_seps__Variable__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class EvalCommand {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__declaration_EvalCommand__declaration_Declaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3520, 0, "Declaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__declaration_EvalCommand__declaration_Declaration_, tmp);
	}
    protected static final void _init_prod__import_EvalCommand__imported_Import_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3512, 0, "Import", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__import_EvalCommand__imported_Import_, tmp);
	}
    protected static final void _init_prod__statement_EvalCommand__statement_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3530, 0, "Statement", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__statement_EvalCommand__statement_Statement_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__declaration_EvalCommand__declaration_Declaration_(builder);
      
        _init_prod__import_EvalCommand__imported_Import_(builder);
      
        _init_prod__statement_EvalCommand__statement_Statement_(builder);
      
    }
  }
	
  protected static class Expression {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__addition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___43_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2516, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2512, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2510, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2508, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2504, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__addition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___43_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__all_Expression__lit_all_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(1902, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1900, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(1896, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1888, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1890, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(1892, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(1894, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1886, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1884, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1882, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1880, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new int[] {97,108,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__all_Expression__lit_all_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__and_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2848, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2846, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2844, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new int[] {38,38}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2842, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2838, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__and_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__any_Expression__lit_any_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(1676, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1674, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(1670, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1662, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1664, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(1666, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(1668, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1660, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1658, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1656, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1654, 0, prod__lit_any__char_class___range__97_97_char_class___range__110_110_char_class___range__121_121_, new int[] {97,110,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__any_Expression__lit_any_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__appendAfter_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2478, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2476, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2474, 2, prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_, new int[] {60,60}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {61})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(2468, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2464, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__appendAfter_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__asType_Expression__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(2270, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(2266, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(2264, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2262, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(2258, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2256, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2254, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__asType_Expression__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Expression_, tmp);
	}
    protected static final void _init_prod__bracket_Expression__lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1638, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1640, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(1642, 2, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1646, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(1648, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__bracket_Expression__lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__callOrTree_Expression__expression_Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(2210, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(2208, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(2204, 4, regular__iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2196, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2198, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(2200, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(2202, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2194, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2192, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2190, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2186, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__callOrTree_Expression__expression_Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__closedRecursiveAddition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___46_43_46_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2456, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2452, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2450, 2, prod__lit___46_43_46__char_class___range__46_46_char_class___range__43_43_char_class___range__46_46_, new int[] {46,43,46}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2448, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2444, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__closedRecursiveAddition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___46_43_46_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__closedRecursiveComposition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___111_46_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2324, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2322, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2320, 2, prod__lit___111_46__char_class___range__111_111_char_class___range__46_46_, new int[] {111,46}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2318, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2314, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__closedRecursiveComposition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___111_46_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__closure_Expression__type_Type_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(2060, 8, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(2058, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(2054, 6, regular__iter_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2050, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2052, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(2048, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(2046, 4, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2044, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(2040, 2, "Parameters", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2038, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2034, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__closure_Expression__type_Type_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__composition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_o_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2306, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2304, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2302, 2, prod__lit_o__char_class___range__111_111_, new int[] {111}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2300, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2296, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__composition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_o_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__comprehension_Expression__comprehension_Comprehension_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(2176, 0, "Comprehension", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__comprehension_Expression__comprehension_Comprehension_, tmp);
	}
    protected static final void _init_prod__division_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2410, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2408, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2406, 2, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2404, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2400, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__division_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__enumerator_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___60_45_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2772, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2770, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2768, 2, prod__lit___60_45__char_class___range__60_60_char_class___range__45_45_, new int[] {60,45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2766, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2762, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__enumerator_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___60_45_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__equals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2714, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2712, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2710, 2, prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_, new int[] {61,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2708, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2704, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__equals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__equivalence_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2830, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2828, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2826, 2, prod__lit___60_61_61_62__char_class___range__60_60_char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new int[] {60,61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2824, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2820, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__equivalence_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__fieldAccess_Expression__expression_Expression_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1812, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1810, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1808, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1806, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1802, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__fieldAccess_Expression__expression_Expression_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_, tmp);
	}
    protected static final void _init_prod__fieldProject_Expression__expression_Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_fields_iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(1934, 6, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1932, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(1928, 4, regular__iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1920, 0, "Field", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1922, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(1924, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(1926, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1918, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1916, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1914, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1912, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__fieldProject_Expression__expression_Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_fields_iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__fieldUpdate_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_key_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_replacement_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1498, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1502, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1504, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1506, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(1508, 4, "Name", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1512, 5, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(1514, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(1516, 7, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(1518, 8, "Expression", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(1522, 9, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(1524, 10, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__fieldUpdate_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_key_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_replacement_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__getAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1752, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1750, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1748, 2, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1746, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1742, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__getAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    protected static final void _init_prod__greaterThan_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2656, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2654, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2652, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2650, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2646, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__greaterThan_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__greaterThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2674, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2672, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2670, 2, prod__lit___62_61__char_class___range__62_62_char_class___range__61_61_, new int[] {62,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2668, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2664, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__greaterThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__has_Expression__expression_Expression_layouts_LAYOUTLIST_lit_has_layouts_LAYOUTLIST_name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1794, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1792, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1790, 2, prod__lit_has__char_class___range__104_104_char_class___range__97_97_char_class___range__115_115_, new int[] {104,97,115}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1788, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1784, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__has_Expression__expression_Expression_layouts_LAYOUTLIST_lit_has_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    protected static final void _init_prod__ifDefinedOtherwise_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2732, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2730, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2728, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2726, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2722, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ifDefinedOtherwise_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__ifThenElse_Expression__condition_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_thenExp_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_elseExp_Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode<IConstructor>(2894, 8, "Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(2892, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(2890, 6, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(2888, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(2884, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2882, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2880, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2878, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2874, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ifThenElse_Expression__condition_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_thenExp_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_elseExp_Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__implication_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2812, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2810, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2808, 2, prod__lit___61_61_62__char_class___range__61_61_char_class___range__61_61_char_class___range__62_62_, new int[] {61,61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2806, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2802, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__implication_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__in_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_in_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2594, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2592, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2590, 2, prod__lit_in__char_class___range__105_105_char_class___range__110_110_, new int[] {105,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2588, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2584, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__in_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_in_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__insertBefore_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2496, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2494, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2492, 2, prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_, new int[] {62,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2490, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2486, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__insertBefore_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__intersection_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(2418, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2422, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2428, 2, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {38})});
      tmp[3] = new NonTerminalStackNode<IConstructor>(2430, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(2432, 4, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__intersection_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__is_Expression__expression_Expression_layouts_LAYOUTLIST_lit_is_layouts_LAYOUTLIST_name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1692, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1690, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1688, 2, prod__lit_is__char_class___range__105_105_char_class___range__115_115_, new int[] {105,115}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1686, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1682, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__is_Expression__expression_Expression_layouts_LAYOUTLIST_lit_is_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    protected static final void _init_prod__isDefined_Expression__argument_Expression_layouts_LAYOUTLIST_lit___63_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(2222, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2220, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2216, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__isDefined_Expression__argument_Expression_layouts_LAYOUTLIST_lit___63_, tmp);
	}
    protected static final void _init_prod__it_Expression__lit_it_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2170, 0, prod__lit_it__char_class___range__105_105_char_class___range__116_116_, new int[] {105,116}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{65,90},{95,95},{97,122}})}, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{65,90},{95,95},{97,122}})});
      builder.addAlternative(ObjectRascalRascal.prod__it_Expression__lit_it_, tmp);
	}
    protected static final void _init_prod__join_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_join_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2392, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2390, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2388, 2, prod__lit_join__char_class___range__106_106_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_, new int[] {106,111,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2386, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2382, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__join_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_join_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__lessThan_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2638, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2636, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2634, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {45})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(2628, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2624, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__lessThan_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__lessThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2616, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2614, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2612, 2, prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_, new int[] {60,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2610, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2606, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__lessThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__list_Expression__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(1574, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1572, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(1568, 2, regular__iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1560, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1562, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(1564, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(1566, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1558, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1556, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__list_Expression__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__literal_Expression__literal_Literal_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(2096, 0, "Literal", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__literal_Expression__literal_Literal_, tmp);
	}
    protected static final void _init_prod__map_Expression__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(1736, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1734, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(1730, 2, regular__iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1722, 0, "Mapping__Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1724, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(1726, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(1728, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1718, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1716, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__map_Expression__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__match_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2754, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2752, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2750, 2, prod__lit___58_61__char_class___range__58_58_char_class___range__61_61_, new int[] {58,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2748, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2744, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__match_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__modulo_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_mod_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2554, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2552, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2550, 2, prod__lit_mod__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_, new int[] {109,111,100}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2548, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2544, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__modulo_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_mod_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__negation_Expression__lit___33_layouts_LAYOUTLIST_argument_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(2284, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2280, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2278, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__negation_Expression__lit___33_layouts_LAYOUTLIST_argument_Expression_, tmp);
	}
    protected static final void _init_prod__negative_Expression__lit___layouts_LAYOUTLIST_argument_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(2234, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2232, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2230, 0, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__negative_Expression__lit___layouts_LAYOUTLIST_argument_Expression_, tmp);
	}
    protected static final void _init_prod__noMatch_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___33_58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2790, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2788, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2786, 2, prod__lit___33_58_61__char_class___range__33_33_char_class___range__58_58_char_class___range__61_61_, new int[] {33,58,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2784, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2780, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__noMatch_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___33_58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__nonEmptyBlock_Expression__lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(1984, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1982, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(1978, 2, regular__iter_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1974, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1976, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1972, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1970, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__nonEmptyBlock_Expression__lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__nonEquals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2696, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2694, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2692, 2, prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_, new int[] {33,61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2690, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2686, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__nonEquals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__notIn_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_notin_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2576, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2574, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2572, 2, prod__lit_notin__char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_, new int[] {110,111,116,105,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2570, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2566, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__notIn_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_notin_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__or_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2866, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2864, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2862, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new int[] {124,124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2860, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2856, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__or_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__product_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_empty_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(2374, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(2370, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new EmptyStackNode<IConstructor>(2368, 4, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {42})});
      tmp[3] = new NonTerminalStackNode<IConstructor>(2362, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2360, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2358, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2354, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__product_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_empty_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__qualifiedName_Expression__qualifiedName_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1776, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__qualifiedName_Expression__qualifiedName_QualifiedName_, tmp);
	}
    protected static final void _init_prod__range_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(1550, 8, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(1548, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(1544, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1542, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(1540, 4, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new int[] {46,46}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1538, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(1534, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1532, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1530, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__range_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__reducer_Expression__lit___40_layouts_LAYOUTLIST_init_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_result_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(2028, 12, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(2026, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(2022, 10, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2014, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2016, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(2018, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(2020, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(2012, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(2010, 8, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(2008, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(2004, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(2002, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(2000, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1998, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(1994, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1992, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1990, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__reducer_Expression__lit___40_layouts_LAYOUTLIST_init_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_result_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__reifiedType_Expression__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Expression_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(1964, 10, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(1962, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(1958, 8, "Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(1956, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(1954, 6, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1952, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(1948, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1946, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1944, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1942, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1940, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__reifiedType_Expression__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Expression_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__reifyType_Expression__lit___35_layouts_LAYOUTLIST_type_Type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(2076, 2, "Type", null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {91})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(2068, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2066, 0, prod__lit___35__char_class___range__35_35_, new int[] {35}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__reifyType_Expression__lit___35_layouts_LAYOUTLIST_type_Type_, tmp);
	}
    protected static final void _init_prod__remainder_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2346, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2344, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2342, 2, prod__lit___37__char_class___range__37_37_, new int[] {37}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2340, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2336, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__remainder_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__set_Expression__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(1838, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1836, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(1832, 2, regular__iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1824, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1826, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(1828, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(1830, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1822, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1820, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__set_Expression__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__setAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_value_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(1874, 12, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(1872, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(1868, 10, "Expression", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(1866, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(1864, 8, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(1862, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(1858, 6, "Name", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1856, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(1854, 4, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1852, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1850, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1848, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1844, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__setAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_value_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__splice_Expression__lit___42_layouts_LAYOUTLIST_argument_Expression__assoc__non_assoc(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(2246, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2244, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2242, 0, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__splice_Expression__lit___42_layouts_LAYOUTLIST_argument_Expression__assoc__non_assoc, tmp);
	}
    protected static final void _init_prod__stepRange_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_second_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(2158, 12, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(2156, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(2152, 10, "Expression", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(2150, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(2148, 8, prod__lit___46_46__char_class___range__46_46_char_class___range__46_46_, new int[] {46,46}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(2146, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(2142, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(2140, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(2138, 4, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2136, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(2132, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2130, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2128, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__stepRange_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_second_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__subscript_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscripts_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(1632, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1630, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(1626, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1618, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1620, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(1622, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(1624, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1616, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1614, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1612, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1610, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__subscript_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscripts_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__subtraction_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(2536, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2534, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2532, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2530, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2526, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__subtraction_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__transitiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_lit___43_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1700, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1704, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1710, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {61})});
      builder.addAlternative(ObjectRascalRascal.prod__transitiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_lit___43_, tmp);
	}
    protected static final void _init_prod__transitiveReflexiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_lit___42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1760, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1764, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1770, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {61})});
      builder.addAlternative(ObjectRascalRascal.prod__transitiveReflexiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__tuple_Expression__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(2122, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2120, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(2116, 2, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2108, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2110, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(2112, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(2114, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2106, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2104, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__tuple_Expression__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__visit_Expression__label_Label_layouts_LAYOUTLIST_visit_Visit_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(2088, 2, "Visit", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2086, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(2082, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__visit_Expression__label_Label_layouts_LAYOUTLIST_visit_Visit_, tmp);
	}
    protected static final void _init_prod__voidClosure_Expression__parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(1600, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1598, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(1594, 4, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1590, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1592, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1588, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1586, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1584, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1580, 0, "Parameters", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__voidClosure_Expression__parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__addition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___43_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__all_Expression__lit_all_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__and_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__any_Expression__lit_any_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__appendAfter_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__asType_Expression__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Expression_(builder);
      
        _init_prod__bracket_Expression__lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__callOrTree_Expression__expression_Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__closedRecursiveAddition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___46_43_46_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__closedRecursiveComposition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___111_46_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__closure_Expression__type_Type_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__composition_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_o_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__comprehension_Expression__comprehension_Comprehension_(builder);
      
        _init_prod__division_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__enumerator_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___60_45_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(builder);
      
        _init_prod__equals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__equivalence_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__fieldAccess_Expression__expression_Expression_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_field_Name_(builder);
      
        _init_prod__fieldProject_Expression__expression_Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_fields_iter_seps__Field__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__fieldUpdate_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_key_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_replacement_Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__getAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_(builder);
      
        _init_prod__greaterThan_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__greaterThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__has_Expression__expression_Expression_layouts_LAYOUTLIST_lit_has_layouts_LAYOUTLIST_name_Name_(builder);
      
        _init_prod__ifDefinedOtherwise_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__ifThenElse_Expression__condition_Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_thenExp_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_elseExp_Expression__assoc__right(builder);
      
        _init_prod__implication_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___61_61_62_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__in_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_in_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__insertBefore_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__intersection_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___38_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__is_Expression__expression_Expression_layouts_LAYOUTLIST_lit_is_layouts_LAYOUTLIST_name_Name_(builder);
      
        _init_prod__isDefined_Expression__argument_Expression_layouts_LAYOUTLIST_lit___63_(builder);
      
        _init_prod__it_Expression__lit_it_(builder);
      
        _init_prod__join_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_join_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__lessThan_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__lessThanOrEq_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___60_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__list_Expression__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__literal_Expression__literal_Literal_(builder);
      
        _init_prod__map_Expression__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__match_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(builder);
      
        _init_prod__modulo_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_mod_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__negation_Expression__lit___33_layouts_LAYOUTLIST_argument_Expression_(builder);
      
        _init_prod__negative_Expression__lit___layouts_LAYOUTLIST_argument_Expression_(builder);
      
        _init_prod__noMatch_Expression__pattern_Pattern_layouts_LAYOUTLIST_lit___33_58_61_layouts_LAYOUTLIST_expression_Expression__assoc__non_assoc(builder);
      
        _init_prod__nonEmptyBlock_Expression__lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__nonEquals_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__notIn_Expression__lhs_Expression_layouts_LAYOUTLIST_lit_notin_layouts_LAYOUTLIST_rhs_Expression__assoc__non_assoc(builder);
      
        _init_prod__or_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__product_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_empty_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__qualifiedName_Expression__qualifiedName_QualifiedName_(builder);
      
        _init_prod__range_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__reducer_Expression__lit___40_layouts_LAYOUTLIST_init_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_result_Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__reifiedType_Expression__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Expression_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__reifyType_Expression__lit___35_layouts_LAYOUTLIST_type_Type_(builder);
      
        _init_prod__remainder_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__set_Expression__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__setAnnotation_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_value_Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__splice_Expression__lit___42_layouts_LAYOUTLIST_argument_Expression__assoc__non_assoc(builder);
      
        _init_prod__stepRange_Expression__lit___91_layouts_LAYOUTLIST_first_Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_second_Expression_layouts_LAYOUTLIST_lit___46_46_layouts_LAYOUTLIST_last_Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__subscript_Expression__expression_Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_subscripts_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__subtraction_Expression__lhs_Expression_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_rhs_Expression__assoc__left(builder);
      
        _init_prod__transitiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_lit___43_(builder);
      
        _init_prod__transitiveReflexiveClosure_Expression__argument_Expression_layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__tuple_Expression__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__visit_Expression__label_Label_layouts_LAYOUTLIST_visit_Visit_(builder);
      
        _init_prod__voidClosure_Expression__parameters_Parameters_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class Field {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__index_Field__fieldIndex_IntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3386, 0, "IntegerLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__index_Field__fieldIndex_IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__name_Field__fieldName_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3394, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__name_Field__fieldName_Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__index_Field__fieldIndex_IntegerLiteral_(builder);
      
        _init_prod__name_Field__fieldName_Name_(builder);
      
    }
  }
	
  protected static class Formals {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Formals__formals_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(4774, 0, regular__iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4766, 0, "Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4768, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(4770, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(4772, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Formals__formals_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Formals__formals_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class FunctionBody {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_FunctionBody__lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(6856, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(6854, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(6850, 2, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(6846, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(6848, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6844, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(6842, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_FunctionBody__lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_FunctionBody__lit___123_layouts_LAYOUTLIST_statements_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class FunctionDeclaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__abstract_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(5250, 6, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5248, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(5244, 4, "Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5242, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5238, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5236, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5232, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__abstract_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__conditional_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_44_99_111_110_100_105_116_105_111_110_115_125(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(5336, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(5334, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(5330, 12, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5322, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5324, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5326, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5328, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(5320, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(5318, 10, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new int[] {119,104,101,110}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(5316, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(5312, 8, "Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(5310, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(5308, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5306, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(5302, 4, "Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5300, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5296, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5294, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5290, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__conditional_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_44_99_111_110_100_105_116_105_111_110_115_125, tmp);
	}
    protected static final void _init_prod__default_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_body_FunctionBody__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(5224, 6, "FunctionBody", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5222, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(5218, 4, "Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5216, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5212, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5210, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5206, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_body_FunctionBody__tag__Foldable, tmp);
	}
    protected static final void _init_prod__expression_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_125(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(5284, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(5282, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(5278, 8, "Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(5276, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(5274, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5272, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(5268, 4, "Signature", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5266, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(5262, 2, "Visibility", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5260, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5256, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__expression_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_125, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__abstract_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__conditional_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_44_99_111_110_100_105_116_105_111_110_115_125(builder);
      
        _init_prod__default_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_body_FunctionBody__tag__Foldable(builder);
      
        _init_prod__expression_FunctionDeclaration__tags_Tags_layouts_LAYOUTLIST_visibility_Visibility_layouts_LAYOUTLIST_signature_Signature_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59__tag__Foldable_tag__breakable___123_101_120_112_114_101_115_115_105_111_110_125(builder);
      
    }
  }
	
  protected static class FunctionModifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_FunctionModifier__lit_default_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3496, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_FunctionModifier__lit_default_, tmp);
	}
    protected static final void _init_prod__java_FunctionModifier__lit_java_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3490, 0, prod__lit_java__char_class___range__106_106_char_class___range__97_97_char_class___range__118_118_char_class___range__97_97_, new int[] {106,97,118,97}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__java_FunctionModifier__lit_java_, tmp);
	}
    protected static final void _init_prod__test_FunctionModifier__lit_test_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3502, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new int[] {116,101,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__test_FunctionModifier__lit_test_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_FunctionModifier__lit_default_(builder);
      
        _init_prod__java_FunctionModifier__lit_java_(builder);
      
        _init_prod__test_FunctionModifier__lit_test_(builder);
      
    }
  }
	
  protected static class FunctionModifiers {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__list_FunctionModifiers__modifiers_iter_star_seps__FunctionModifier__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(10846, 0, regular__iter_star_seps__FunctionModifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(10842, 0, "FunctionModifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(10844, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__list_FunctionModifiers__modifiers_iter_star_seps__FunctionModifier__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__list_FunctionModifiers__modifiers_iter_star_seps__FunctionModifier__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class FunctionType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__typeArguments_FunctionType__type_Type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(10070, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(10068, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(10064, 4, regular__iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(10056, 0, "TypeArg", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(10058, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(10060, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(10062, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10054, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(10052, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10050, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(10046, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__typeArguments_FunctionType__type_Type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__typeArguments_FunctionType__type_Type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class Header {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new SeparatedListStackNode<IConstructor>(1166, 6, regular__iter_star_seps__Import__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1162, 0, "Import", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1164, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1160, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(1156, 4, "QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1154, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1152, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1150, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1146, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__parameters_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_params_ModuleParameters_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new SeparatedListStackNode<IConstructor>(1200, 8, regular__iter_star_seps__Import__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1196, 0, "Import", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1198, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(1194, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(1190, 6, "ModuleParameters", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1188, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(1184, 4, "QualifiedName", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1182, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1180, 2, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1178, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1174, 0, "Tags", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__parameters_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_params_ModuleParameters_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_(builder);
      
        _init_prod__parameters_Header__tags_Tags_layouts_LAYOUTLIST_lit_module_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_params_ModuleParameters_layouts_LAYOUTLIST_imports_iter_star_seps__Import__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Import {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Import__lit_import_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(6938, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(6936, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(6932, 2, "ImportedModule", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6930, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(6928, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Import__lit_import_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__extend_Import__lit_extend_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(6922, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(6920, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(6916, 2, "ImportedModule", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6914, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(6912, 0, prod__lit_extend__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {101,120,116,101,110,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__extend_Import__lit_extend_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__external_Import__lit_import_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_at_LocationLiteral_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(6972, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(6970, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(6966, 6, "LocationLiteral", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(6964, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(6962, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(6960, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(6956, 2, "QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6954, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(6952, 0, prod__lit_import__char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {105,109,112,111,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__external_Import__lit_import_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_at_LocationLiteral_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__syntax_Import__syntax_SyntaxDefinition_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(6944, 0, "SyntaxDefinition", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__syntax_Import__syntax_SyntaxDefinition_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Import__lit_import_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__extend_Import__lit_extend_layouts_LAYOUTLIST_module_ImportedModule_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__external_Import__lit_import_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_at_LocationLiteral_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__syntax_Import__syntax_SyntaxDefinition_(builder);
      
    }
  }
	
  protected static class ImportedModule {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__actuals_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(1408, 2, "ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1406, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1402, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__actuals_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_, tmp);
	}
    protected static final void _init_prod__actualsRenaming_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_layouts_LAYOUTLIST_renamings_Renamings_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1386, 4, "Renamings", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1384, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(1380, 2, "ModuleActuals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1378, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1374, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__actualsRenaming_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_layouts_LAYOUTLIST_renamings_Renamings_, tmp);
	}
    protected static final void _init_prod__default_ImportedModule__name_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1394, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_ImportedModule__name_QualifiedName_, tmp);
	}
    protected static final void _init_prod__renamings_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_renamings_Renamings_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(1366, 2, "Renamings", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1364, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1360, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__renamings_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_renamings_Renamings_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__actuals_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_(builder);
      
        _init_prod__actualsRenaming_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_actuals_ModuleActuals_layouts_LAYOUTLIST_renamings_Renamings_(builder);
      
        _init_prod__default_ImportedModule__name_QualifiedName_(builder);
      
        _init_prod__renamings_ImportedModule__name_QualifiedName_layouts_LAYOUTLIST_renamings_Renamings_(builder);
      
    }
  }
	
  protected static class IntegerLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__decimalIntegerLiteral_IntegerLiteral__decimal_DecimalIntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(6810, 0, "DecimalIntegerLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__decimalIntegerLiteral_IntegerLiteral__decimal_DecimalIntegerLiteral_, tmp);
	}
    protected static final void _init_prod__hexIntegerLiteral_IntegerLiteral__hex_HexIntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(6802, 0, "HexIntegerLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__hexIntegerLiteral_IntegerLiteral__hex_HexIntegerLiteral_, tmp);
	}
    protected static final void _init_prod__octalIntegerLiteral_IntegerLiteral__octal_OctalIntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(6794, 0, "OctalIntegerLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__octalIntegerLiteral_IntegerLiteral__octal_OctalIntegerLiteral_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__decimalIntegerLiteral_IntegerLiteral__decimal_DecimalIntegerLiteral_(builder);
      
        _init_prod__hexIntegerLiteral_IntegerLiteral__hex_HexIntegerLiteral_(builder);
      
        _init_prod__octalIntegerLiteral_IntegerLiteral__octal_OctalIntegerLiteral_(builder);
      
    }
  }
	
  protected static class Kind {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__alias_Kind__lit_alias_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6688, 0, prod__lit_alias__char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_, new int[] {97,108,105,97,115}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__alias_Kind__lit_alias_, tmp);
	}
    protected static final void _init_prod__all_Kind__lit_all_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6736, 0, prod__lit_all__char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_, new int[] {97,108,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__all_Kind__lit_all_, tmp);
	}
    protected static final void _init_prod__anno_Kind__lit_anno_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6718, 0, prod__lit_anno__char_class___range__97_97_char_class___range__110_110_char_class___range__110_110_char_class___range__111_111_, new int[] {97,110,110,111}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__anno_Kind__lit_anno_, tmp);
	}
    protected static final void _init_prod__data_Kind__lit_data_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6712, 0, prod__lit_data__char_class___range__100_100_char_class___range__97_97_char_class___range__116_116_char_class___range__97_97_, new int[] {100,97,116,97}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__data_Kind__lit_data_, tmp);
	}
    protected static final void _init_prod__function_Kind__lit_function_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6700, 0, prod__lit_function__char_class___range__102_102_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_, new int[] {102,117,110,99,116,105,111,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__function_Kind__lit_function_, tmp);
	}
    protected static final void _init_prod__module_Kind__lit_module_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6730, 0, prod__lit_module__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_, new int[] {109,111,100,117,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__module_Kind__lit_module_, tmp);
	}
    protected static final void _init_prod__tag_Kind__lit_tag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6706, 0, prod__lit_tag__char_class___range__116_116_char_class___range__97_97_char_class___range__103_103_, new int[] {116,97,103}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__tag_Kind__lit_tag_, tmp);
	}
    protected static final void _init_prod__variable_Kind__lit_variable_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6724, 0, prod__lit_variable__char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_, new int[] {118,97,114,105,97,98,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__variable_Kind__lit_variable_, tmp);
	}
    protected static final void _init_prod__view_Kind__lit_view_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6694, 0, prod__lit_view__char_class___range__118_118_char_class___range__105_105_char_class___range__101_101_char_class___range__119_119_, new int[] {118,105,101,119}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__view_Kind__lit_view_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__alias_Kind__lit_alias_(builder);
      
        _init_prod__all_Kind__lit_all_(builder);
      
        _init_prod__anno_Kind__lit_anno_(builder);
      
        _init_prod__data_Kind__lit_data_(builder);
      
        _init_prod__function_Kind__lit_function_(builder);
      
        _init_prod__module_Kind__lit_module_(builder);
      
        _init_prod__tag_Kind__lit_tag_(builder);
      
        _init_prod__variable_Kind__lit_variable_(builder);
      
        _init_prod__view_Kind__lit_view_(builder);
      
    }
  }
	
  protected static class Label {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Label__name_Name_layouts_LAYOUTLIST_lit___58_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(3198, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3196, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3192, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Label__name_Name_layouts_LAYOUTLIST_lit___58_, tmp);
	}
    protected static final void _init_prod__empty_Label__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(3186, 0);
      builder.addAlternative(ObjectRascalRascal.prod__empty_Label__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Label__name_Name_layouts_LAYOUTLIST_lit___58_(builder);
      
        _init_prod__empty_Label__(builder);
      
    }
  }
	
  protected static class Literal {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__boolean_Literal__booleanLiteral_BooleanLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(172, 0, "BooleanLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__boolean_Literal__booleanLiteral_BooleanLiteral_, tmp);
	}
    protected static final void _init_prod__dateTime_Literal__dateTimeLiteral_DateTimeLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(124, 0, "DateTimeLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__dateTime_Literal__dateTimeLiteral_DateTimeLiteral_, tmp);
	}
    protected static final void _init_prod__integer_Literal__integerLiteral_IntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(180, 0, "IntegerLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__integer_Literal__integerLiteral_IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__location_Literal__locationLiteral_LocationLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(148, 0, "LocationLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__location_Literal__locationLiteral_LocationLiteral_, tmp);
	}
    protected static final void _init_prod__rational_Literal__rationalLiteral_RationalLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(156, 0, "RationalLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__rational_Literal__rationalLiteral_RationalLiteral_, tmp);
	}
    protected static final void _init_prod__real_Literal__realLiteral_RealLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(140, 0, "RealLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__real_Literal__realLiteral_RealLiteral_, tmp);
	}
    protected static final void _init_prod__regExp_Literal__regExpLiteral_RegExpLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(164, 0, "RegExpLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__regExp_Literal__regExpLiteral_RegExpLiteral_, tmp);
	}
    protected static final void _init_prod__string_Literal__stringLiteral_StringLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(132, 0, "StringLiteral", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__string_Literal__stringLiteral_StringLiteral_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__boolean_Literal__booleanLiteral_BooleanLiteral_(builder);
      
        _init_prod__dateTime_Literal__dateTimeLiteral_DateTimeLiteral_(builder);
      
        _init_prod__integer_Literal__integerLiteral_IntegerLiteral_(builder);
      
        _init_prod__location_Literal__locationLiteral_LocationLiteral_(builder);
      
        _init_prod__rational_Literal__rationalLiteral_RationalLiteral_(builder);
      
        _init_prod__real_Literal__realLiteral_RealLiteral_(builder);
      
        _init_prod__regExp_Literal__regExpLiteral_RegExpLiteral_(builder);
      
        _init_prod__string_Literal__stringLiteral_StringLiteral_(builder);
      
    }
  }
	
  protected static class LocalVariableDeclaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_LocalVariableDeclaration__declarator_Declarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4362, 0, "Declarator", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_LocalVariableDeclaration__declarator_Declarator_, tmp);
	}
    protected static final void _init_prod__dynamic_LocalVariableDeclaration__lit_dynamic_layouts_LAYOUTLIST_declarator_Declarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(4354, 2, "Declarator", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4352, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4350, 0, prod__lit_dynamic__char_class___range__100_100_char_class___range__121_121_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__99_99_, new int[] {100,121,110,97,109,105,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__dynamic_LocalVariableDeclaration__lit_dynamic_layouts_LAYOUTLIST_declarator_Declarator_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_LocalVariableDeclaration__declarator_Declarator_(builder);
      
        _init_prod__dynamic_LocalVariableDeclaration__lit_dynamic_layouts_LAYOUTLIST_declarator_Declarator_(builder);
      
    }
  }
	
  protected static class LocationLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_LocationLiteral__protocolPart_ProtocolPart_layouts_LAYOUTLIST_pathPart_PathPart_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(8330, 2, "PathPart", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(8328, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(8324, 0, "ProtocolPart", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_LocationLiteral__protocolPart_ProtocolPart_layouts_LAYOUTLIST_pathPart_PathPart_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_LocationLiteral__protocolPart_ProtocolPart_layouts_LAYOUTLIST_pathPart_PathPart_(builder);
      
    }
  }
	
  protected static class Module {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Module__header_Header_layouts_LAYOUTLIST_body_Body_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(198, 2, "Body", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(196, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(192, 0, "Header", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Module__header_Header_layouts_LAYOUTLIST_body_Body_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Module__header_Header_layouts_LAYOUTLIST_body_Body_(builder);
      
    }
  }
	
  protected static class ModuleActuals {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_ModuleActuals__lit___91_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(5790, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5788, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(5784, 2, regular__iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5776, 0, "Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5778, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5780, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5782, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5774, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5772, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_ModuleActuals__lit___91_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_ModuleActuals__lit___91_layouts_LAYOUTLIST_types_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class ModuleParameters {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_ModuleParameters__lit___91_layouts_LAYOUTLIST_parameters_iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(5914, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5912, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(5908, 2, regular__iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5900, 0, "TypeVar", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5902, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5904, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5906, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5898, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(5896, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_ModuleParameters__lit___91_layouts_LAYOUTLIST_parameters_iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_ModuleParameters__lit___91_layouts_LAYOUTLIST_parameters_iter_seps__TypeVar__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class Parameters {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(4236, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4234, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4230, 2, "Formals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4228, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4226, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__varArgs_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___46_46_46_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(4256, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4254, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(4252, 4, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new int[] {46,46,46}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4250, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4246, 2, "Formals", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4244, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4242, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__varArgs_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___46_46_46_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__varArgs_Parameters__lit___40_layouts_LAYOUTLIST_formals_Formals_layouts_LAYOUTLIST_lit___46_46_46_layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class PathPart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__interpolated_PathPart__pre_PrePathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(7526, 4, "PathTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(7524, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(7520, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(7518, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(7514, 0, "PrePathChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__interpolated_PathPart__pre_PrePathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_, tmp);
	}
    protected static final void _init_prod__nonInterpolated_PathPart__pathChars_PathChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(7506, 0, "PathChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__nonInterpolated_PathPart__pathChars_PathChars_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__interpolated_PathPart__pre_PrePathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_(builder);
      
        _init_prod__nonInterpolated_PathPart__pathChars_PathChars_(builder);
      
    }
  }
	
  protected static class PathTail {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__mid_PathTail__mid_MidPathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4630, 4, "PathTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4628, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4624, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4622, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4618, 0, "MidPathChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__mid_PathTail__mid_MidPathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_, tmp);
	}
    protected static final void _init_prod__post_PathTail__post_PostPathChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4638, 0, "PostPathChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__post_PathTail__post_PostPathChars_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__mid_PathTail__mid_MidPathChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_PathTail_(builder);
      
        _init_prod__post_PathTail__post_PostPathChars_(builder);
      
    }
  }
	
  protected static class Pattern {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__anti_Pattern__lit___33_layouts_LAYOUTLIST_pattern_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(11656, 2, "Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11654, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11652, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__anti_Pattern__lit___33_layouts_LAYOUTLIST_pattern_Pattern_, tmp);
	}
    protected static final void _init_prod__asType_Pattern__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(11614, 6, "Pattern", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(11612, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(11610, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11608, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(11604, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11602, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11600, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__asType_Pattern__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Pattern_, tmp);
	}
    protected static final void _init_prod__callOrTree_Pattern__expression_Pattern_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(11580, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(11578, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(11574, 4, regular__iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(11566, 0, "Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(11568, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(11570, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(11572, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11564, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(11562, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11560, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(11556, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__callOrTree_Pattern__expression_Pattern_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__descendant_Pattern__lit___47_layouts_LAYOUTLIST_pattern_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(11626, 2, "Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11624, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11622, 0, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__descendant_Pattern__lit___47_layouts_LAYOUTLIST_pattern_Pattern_, tmp);
	}
    protected static final void _init_prod__list_Pattern__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(11406, 4, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11404, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(11400, 2, regular__iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(11392, 0, "Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(11394, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(11396, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(11398, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11390, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11388, 0, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__list_Pattern__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__literal_Pattern__literal_Literal_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(11412, 0, "Literal", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__literal_Pattern__literal_Literal_, tmp);
	}
    protected static final void _init_prod__map_Pattern__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(11382, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11380, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(11376, 2, regular__iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(11368, 0, "Mapping__Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(11370, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(11372, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(11374, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11364, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11362, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__map_Pattern__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__multiVariable_Pattern__qualifiedName_QualifiedName_layouts_LAYOUTLIST_lit___42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(11482, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11480, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(11476, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__multiVariable_Pattern__qualifiedName_QualifiedName_layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__negative_Pattern__lit___layouts_LAYOUTLIST_argument_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(11444, 2, "Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11442, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11440, 0, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__negative_Pattern__lit___layouts_LAYOUTLIST_argument_Pattern_, tmp);
	}
    protected static final void _init_prod__qualifiedName_Pattern__qualifiedName_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(11432, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__qualifiedName_Pattern__qualifiedName_QualifiedName_, tmp);
	}
    protected static final void _init_prod__reifiedType_Pattern__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Pattern_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Pattern_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(11550, 10, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(11548, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(11544, 8, "Pattern", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(11542, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(11540, 6, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(11538, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(11534, 4, "Pattern", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11532, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(11530, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11528, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11526, 0, prod__lit_type__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_, new int[] {116,121,112,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__reifiedType_Pattern__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Pattern_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Pattern_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__set_Pattern__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(11520, 4, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11518, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(11514, 2, regular__iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(11506, 0, "Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(11508, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(11510, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(11512, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11504, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11502, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__set_Pattern__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__splice_Pattern__lit___42_layouts_LAYOUTLIST_argument_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(11590, 2, "Pattern", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11588, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11586, 0, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__splice_Pattern__lit___42_layouts_LAYOUTLIST_argument_Pattern_, tmp);
	}
    protected static final void _init_prod__splicePlus_Pattern__lit___43_layouts_LAYOUTLIST_argument_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode<IConstructor>(11420, 0, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11422, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(11424, 2, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__splicePlus_Pattern__lit___43_layouts_LAYOUTLIST_argument_Pattern_, tmp);
	}
    protected static final void _init_prod__tuple_Pattern__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(11470, 4, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11468, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(11464, 2, regular__iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(11456, 0, "Pattern", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(11458, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(11460, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(11462, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11454, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11452, 0, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__tuple_Pattern__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_, tmp);
	}
    protected static final void _init_prod__typedVariable_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(11494, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11492, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(11488, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__typedVariable_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    protected static final void _init_prod__typedVariableBecomes_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(11680, 6, "Pattern", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(11678, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(11676, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11674, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(11670, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11668, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(11664, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__typedVariableBecomes_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_, tmp);
	}
    protected static final void _init_prod__variableBecomes_Pattern__name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(11644, 4, "Pattern", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11642, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(11640, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11638, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(11634, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__variableBecomes_Pattern__name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__anti_Pattern__lit___33_layouts_LAYOUTLIST_pattern_Pattern_(builder);
      
        _init_prod__asType_Pattern__lit___91_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___93_layouts_LAYOUTLIST_argument_Pattern_(builder);
      
        _init_prod__callOrTree_Pattern__expression_Pattern_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__descendant_Pattern__lit___47_layouts_LAYOUTLIST_pattern_Pattern_(builder);
      
        _init_prod__list_Pattern__lit___91_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__literal_Pattern__literal_Literal_(builder);
      
        _init_prod__map_Pattern__lit___40_layouts_LAYOUTLIST_mappings_iter_star_seps__Mapping__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__multiVariable_Pattern__qualifiedName_QualifiedName_layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__negative_Pattern__lit___layouts_LAYOUTLIST_argument_Pattern_(builder);
      
        _init_prod__qualifiedName_Pattern__qualifiedName_QualifiedName_(builder);
      
        _init_prod__reifiedType_Pattern__lit_type_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_symbol_Pattern_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_definitions_Pattern_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__set_Pattern__lit___123_layouts_LAYOUTLIST_elements_iter_star_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__splice_Pattern__lit___42_layouts_LAYOUTLIST_argument_Pattern_(builder);
      
        _init_prod__splicePlus_Pattern__lit___43_layouts_LAYOUTLIST_argument_Pattern_(builder);
      
        _init_prod__tuple_Pattern__lit___60_layouts_LAYOUTLIST_elements_iter_seps__Pattern__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_(builder);
      
        _init_prod__typedVariable_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_(builder);
      
        _init_prod__typedVariableBecomes_Pattern__type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_(builder);
      
        _init_prod__variableBecomes_Pattern__name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_pattern_Pattern_(builder);
      
    }
  }
	
  protected static class PatternWithAction {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__arbitrary_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4534, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4532, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4530, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4528, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4524, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__arbitrary_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement_, tmp);
	}
    protected static final void _init_prod__replacing_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_replacement_Replacement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4516, 4, "Replacement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4514, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4512, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new int[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4510, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4506, 0, "Pattern", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__replacing_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_replacement_Replacement_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__arbitrary_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_statement_Statement_(builder);
      
        _init_prod__replacing_PatternWithAction__pattern_Pattern_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_replacement_Replacement_(builder);
      
    }
  }
	
  protected static class PreModule {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_PreModule__header_Header_layouts_LAYOUTLIST_empty_layouts_LAYOUTLIST_rest_Rest_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(3038, 4, "Rest", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3036, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new EmptyStackNode<IConstructor>(3034, 2, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {108,101,120,105,99,97,108}), new StringFollowRestriction(new int[] {105,109,112,111,114,116}), new StringFollowRestriction(new int[] {115,116,97,114,116}), new StringFollowRestriction(new int[] {115,121,110,116,97,120}), new StringFollowRestriction(new int[] {101,120,116,101,110,100}), new StringFollowRestriction(new int[] {108,97,121,111,117,116}), new StringFollowRestriction(new int[] {107,101,121,119,111,114,100})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(3028, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3024, 0, "Header", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_PreModule__header_Header_layouts_LAYOUTLIST_empty_layouts_LAYOUTLIST_rest_Rest_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_PreModule__header_Header_layouts_LAYOUTLIST_empty_layouts_LAYOUTLIST_rest_Rest_(builder);
      
    }
  }
	
  protected static class Prod {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__all_Prod__lhs_Prod_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_rhs_Prod__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(11264, 4, "Prod", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11262, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(11260, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11258, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(11254, 0, "Prod", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__all_Prod__lhs_Prod_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_rhs_Prod__assoc__left, tmp);
	}
    protected static final void _init_prod__associativityGroup_Prod__associativity_Assoc_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_group_Prod_layouts_LAYOUTLIST_lit___41__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(11176, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(11174, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(11170, 4, "Prod", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11168, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(11166, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11164, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(11160, 0, "Assoc", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__associativityGroup_Prod__associativity_Assoc_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_group_Prod_layouts_LAYOUTLIST_lit___41__tag__Foldable, tmp);
	}
    protected static final void _init_prod__first_Prod__lhs_Prod_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_rhs_Prod__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(11286, 4, "Prod", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11284, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(11282, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {62})});
      tmp[1] = new NonTerminalStackNode<IConstructor>(11276, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(11272, 0, "Prod", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__first_Prod__lhs_Prod_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_rhs_Prod__assoc__left, tmp);
	}
    protected static final void _init_prod__labeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new SeparatedListStackNode<IConstructor>(11218, 6, regular__iter_star_seps__Sym__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(11214, 0, "Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(11216, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(11212, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(11210, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11208, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(11204, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11202, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(11198, 0, regular__iter_star_seps__ProdModifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(11194, 0, "ProdModifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(11196, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__labeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__others_Prod__lit___46_46_46_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(11226, 0, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new int[] {46,46,46}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__others_Prod__lit___46_46_46_, tmp);
	}
    protected static final void _init_prod__reference_Prod__lit___58_layouts_LAYOUTLIST_referenced_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(11186, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11184, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11182, 0, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__reference_Prod__lit___58_layouts_LAYOUTLIST_referenced_Name_, tmp);
	}
    protected static final void _init_prod__unlabeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode<IConstructor>(11246, 2, regular__iter_star_seps__Sym__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(11242, 0, "Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(11244, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11240, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(11236, 0, regular__iter_star_seps__ProdModifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(11232, 0, "ProdModifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(11234, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__unlabeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__all_Prod__lhs_Prod_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_rhs_Prod__assoc__left(builder);
      
        _init_prod__associativityGroup_Prod__associativity_Assoc_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_group_Prod_layouts_LAYOUTLIST_lit___41__tag__Foldable(builder);
      
        _init_prod__first_Prod__lhs_Prod_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_rhs_Prod__assoc__left(builder);
      
        _init_prod__labeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_(builder);
      
        _init_prod__others_Prod__lit___46_46_46_(builder);
      
        _init_prod__reference_Prod__lit___58_layouts_LAYOUTLIST_referenced_Name_(builder);
      
        _init_prod__unlabeled_Prod__modifiers_iter_star_seps__ProdModifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_args_iter_star_seps__Sym__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class ProdModifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__associativity_ProdModifier__associativity_Assoc_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(10858, 0, "Assoc", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__associativity_ProdModifier__associativity_Assoc_, tmp);
	}
    protected static final void _init_prod__bracket_ProdModifier__lit_bracket_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(10874, 0, prod__lit_bracket__char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_, new int[] {98,114,97,99,107,101,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__bracket_ProdModifier__lit_bracket_, tmp);
	}
    protected static final void _init_prod__tag_ProdModifier__tag_Tag_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(10866, 0, "Tag", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__tag_ProdModifier__tag_Tag_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__associativity_ProdModifier__associativity_Assoc_(builder);
      
        _init_prod__bracket_ProdModifier__lit_bracket_(builder);
      
        _init_prod__tag_ProdModifier__tag_Tag_(builder);
      
    }
  }
	
  protected static class ProtocolPart {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__interpolated_ProtocolPart__pre_PreProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(3082, 4, "ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3080, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(3076, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3074, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3070, 0, "PreProtocolChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__interpolated_ProtocolPart__pre_PreProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_, tmp);
	}
    protected static final void _init_prod__nonInterpolated_ProtocolPart__protocolChars_ProtocolChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3062, 0, "ProtocolChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__nonInterpolated_ProtocolPart__protocolChars_ProtocolChars_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__interpolated_ProtocolPart__pre_PreProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_(builder);
      
        _init_prod__nonInterpolated_ProtocolPart__protocolChars_ProtocolChars_(builder);
      
    }
  }
	
  protected static class ProtocolTail {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__mid_ProtocolTail__mid_MidProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(8838, 4, "ProtocolTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(8836, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(8832, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(8830, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(8826, 0, "MidProtocolChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__mid_ProtocolTail__mid_MidProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_, tmp);
	}
    protected static final void _init_prod__post_ProtocolTail__post_PostProtocolChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(8818, 0, "PostProtocolChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__post_ProtocolTail__post_PostProtocolChars_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__mid_ProtocolTail__mid_MidProtocolChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_ProtocolTail_(builder);
      
        _init_prod__post_ProtocolTail__post_PostProtocolChars_(builder);
      
    }
  }
	
  protected static class QualifiedName {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_QualifiedName__names_iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(8512, 0, regular__iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(8498, 0, "Name", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(8500, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(8502, 2, prod__lit___58_58__char_class___range__58_58_char_class___range__58_58_, new int[] {58,58}, null, null), new NonTerminalStackNode<IConstructor>(8504, 3, "layouts_LAYOUTLIST", null, null)}, true, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {58,58})});
      builder.addAlternative(ObjectRascalRascal.prod__default_QualifiedName__names_iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_QualifiedName__names_iter_seps__Name__layouts_LAYOUTLIST_lit___58_58_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Range {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__character_Range__character_Char_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(8312, 0, "Char", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__character_Range__character_Char_, tmp);
	}
    protected static final void _init_prod__fromTo_Range__start_Char_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_end_Char_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(8304, 4, "Char", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(8302, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(8300, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(8298, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(8294, 0, "Char", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__fromTo_Range__start_Char_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_end_Char_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__character_Range__character_Char_(builder);
      
        _init_prod__fromTo_Range__start_Char_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_end_Char_(builder);
      
    }
  }
	
  protected static class Renaming {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Renaming__from_Name_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_to_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(480, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(478, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(476, 2, prod__lit___61_62__char_class___range__61_61_char_class___range__62_62_, new int[] {61,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(474, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(470, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Renaming__from_Name_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_to_Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Renaming__from_Name_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_to_Name_(builder);
      
    }
  }
	
  protected static class Renamings {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Renamings__lit_renaming_layouts_LAYOUTLIST_renamings_iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new SeparatedListStackNode<IConstructor>(9136, 2, regular__iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(9128, 0, "Renaming", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(9130, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(9132, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(9134, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9126, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(9124, 0, prod__lit_renaming__char_class___range__114_114_char_class___range__101_101_char_class___range__110_110_char_class___range__97_97_char_class___range__109_109_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_, new int[] {114,101,110,97,109,105,110,103}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Renamings__lit_renaming_layouts_LAYOUTLIST_renamings_iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Renamings__lit_renaming_layouts_LAYOUTLIST_renamings_iter_seps__Renaming__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Replacement {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__conditional_Replacement__replacementExpression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode<IConstructor>(7802, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7794, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7796, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(7798, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(7800, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(7792, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(7790, 2, prod__lit_when__char_class___range__119_119_char_class___range__104_104_char_class___range__101_101_char_class___range__110_110_, new int[] {119,104,101,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(7788, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(7784, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__conditional_Replacement__replacementExpression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_, tmp);
	}
    protected static final void _init_prod__unconditional_Replacement__replacementExpression_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(7776, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__unconditional_Replacement__replacementExpression_Expression_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__conditional_Replacement__replacementExpression_Expression_layouts_LAYOUTLIST_lit_when_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(builder);
      
        _init_prod__unconditional_Replacement__replacementExpression_Expression_(builder);
      
    }
  }
	
  protected static class ShellCommand {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__edit_ShellCommand__lit_edit_layouts_LAYOUTLIST_name_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(8382, 2, "QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(8380, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(8378, 0, prod__lit_edit__char_class___range__101_101_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_, new int[] {101,100,105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__edit_ShellCommand__lit_edit_layouts_LAYOUTLIST_name_QualifiedName_, tmp);
	}
    protected static final void _init_prod__help_ShellCommand__lit_help_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(8414, 0, prod__lit_help__char_class___range__104_104_char_class___range__101_101_char_class___range__108_108_char_class___range__112_112_, new int[] {104,101,108,112}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__help_ShellCommand__lit_help_, tmp);
	}
    protected static final void _init_prod__history_ShellCommand__lit_history_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(8390, 0, prod__lit_history__char_class___range__104_104_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__121_121_, new int[] {104,105,115,116,111,114,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__history_ShellCommand__lit_history_, tmp);
	}
    protected static final void _init_prod__listDeclarations_ShellCommand__lit_declarations_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(8342, 0, prod__lit_declarations__char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_, new int[] {100,101,99,108,97,114,97,116,105,111,110,115}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__listDeclarations_ShellCommand__lit_declarations_, tmp);
	}
    protected static final void _init_prod__listModules_ShellCommand__lit_modules_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(8354, 0, prod__lit_modules__char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_, new int[] {109,111,100,117,108,101,115}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__listModules_ShellCommand__lit_modules_, tmp);
	}
    protected static final void _init_prod__quit_ShellCommand__lit_quit_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(8396, 0, prod__lit_quit__char_class___range__113_113_char_class___range__117_117_char_class___range__105_105_char_class___range__116_116_, new int[] {113,117,105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__quit_ShellCommand__lit_quit_, tmp);
	}
    protected static final void _init_prod__setOption_ShellCommand__lit_set_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_expression_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(8370, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(8368, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(8364, 2, "QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(8362, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(8360, 0, prod__lit_set__char_class___range__115_115_char_class___range__101_101_char_class___range__116_116_, new int[] {115,101,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__setOption_ShellCommand__lit_set_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_expression_Expression_, tmp);
	}
    protected static final void _init_prod__test_ShellCommand__lit_test_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(8348, 0, prod__lit_test__char_class___range__116_116_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_, new int[] {116,101,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__test_ShellCommand__lit_test_, tmp);
	}
    protected static final void _init_prod__undeclare_ShellCommand__lit_undeclare_layouts_LAYOUTLIST_name_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(8406, 2, "QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(8404, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(8402, 0, prod__lit_undeclare__char_class___range__117_117_char_class___range__110_110_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__101_101_, new int[] {117,110,100,101,99,108,97,114,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__undeclare_ShellCommand__lit_undeclare_layouts_LAYOUTLIST_name_QualifiedName_, tmp);
	}
    protected static final void _init_prod__unimport_ShellCommand__lit_unimport_layouts_LAYOUTLIST_name_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(8424, 2, "QualifiedName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(8422, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(8420, 0, prod__lit_unimport__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {117,110,105,109,112,111,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__unimport_ShellCommand__lit_unimport_layouts_LAYOUTLIST_name_QualifiedName_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__edit_ShellCommand__lit_edit_layouts_LAYOUTLIST_name_QualifiedName_(builder);
      
        _init_prod__help_ShellCommand__lit_help_(builder);
      
        _init_prod__history_ShellCommand__lit_history_(builder);
      
        _init_prod__listDeclarations_ShellCommand__lit_declarations_(builder);
      
        _init_prod__listModules_ShellCommand__lit_modules_(builder);
      
        _init_prod__quit_ShellCommand__lit_quit_(builder);
      
        _init_prod__setOption_ShellCommand__lit_set_layouts_LAYOUTLIST_name_QualifiedName_layouts_LAYOUTLIST_expression_Expression_(builder);
      
        _init_prod__test_ShellCommand__lit_test_(builder);
      
        _init_prod__undeclare_ShellCommand__lit_undeclare_layouts_LAYOUTLIST_name_QualifiedName_(builder);
      
        _init_prod__unimport_ShellCommand__lit_unimport_layouts_LAYOUTLIST_name_QualifiedName_(builder);
      
    }
  }
	
  protected static class Signature {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__noThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(510, 6, "Parameters", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(492, 0, "FunctionModifiers", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(496, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(498, 2, "Type", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(502, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(504, 4, "Name", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(508, 5, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__noThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_, tmp);
	}
    protected static final void _init_prod__withThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit_throws_layouts_LAYOUTLIST_exceptions_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new SeparatedListStackNode<IConstructor>(554, 10, regular__iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(546, 0, "Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(548, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(550, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(552, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(544, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(542, 8, prod__lit_throws__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_char_class___range__115_115_, new int[] {116,104,114,111,119,115}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(540, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(536, 6, "Parameters", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(534, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(530, 4, "Name", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(528, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(524, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(522, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(518, 0, "FunctionModifiers", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__withThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit_throws_layouts_LAYOUTLIST_exceptions_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__noThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_(builder);
      
        _init_prod__withThrows_Signature__modifiers_FunctionModifiers_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_parameters_Parameters_layouts_LAYOUTLIST_lit_throws_layouts_LAYOUTLIST_exceptions_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Start {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__absent_Start__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(4790, 0);
      builder.addAlternative(ObjectRascalRascal.prod__absent_Start__, tmp);
	}
    protected static final void _init_prod__present_Start__lit_start_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4786, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__present_Start__lit_start_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__absent_Start__(builder);
      
        _init_prod__present_Start__lit_start_(builder);
      
    }
  }
	
  protected static class Statement {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__append_Statement__lit_append_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(9280, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9276, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(9272, 2, "DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9270, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(9268, 0, prod__lit_append__char_class___range__97_97_char_class___range__112_112_char_class___range__112_112_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_, new int[] {97,112,112,101,110,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__append_Statement__lit_append_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable, tmp);
	}
    protected static final void _init_prod__assert_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(9574, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9572, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(9568, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9566, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(9564, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__assert_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__assertWithMessage_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_message_Expression_layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(9624, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(9622, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(9618, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(9616, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(9614, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9612, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(9608, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9606, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(9604, 0, prod__lit_assert__char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {97,115,115,101,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__assertWithMessage_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_message_Expression_layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__assignment_Statement__assignable_Assignable_layouts_LAYOUTLIST_operator_Assignment_layouts_LAYOUTLIST_statement_Statement__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(9322, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9318, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(9314, 2, "Assignment", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9312, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(9308, 0, "Assignable", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__assignment_Statement__assignable_Assignable_layouts_LAYOUTLIST_operator_Assignment_layouts_LAYOUTLIST_statement_Statement__tag__breakable, tmp);
	}
    protected static final void _init_prod__break_Statement__lit_break_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(9848, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9846, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(9842, 2, "Target", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9840, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(9838, 0, prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,114,101,97,107}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__break_Statement__lit_break_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__continue_Statement__lit_continue_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(9598, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9596, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(9592, 2, "Target", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9590, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(9588, 0, prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_, new int[] {99,111,110,116,105,110,117,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__continue_Statement__lit_continue_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__doWhile_Statement__label_Label_layouts_LAYOUTLIST_lit_do_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(9464, 14, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(9462, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(9460, 12, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(9458, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(9454, 10, "Expression", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(9452, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(9450, 8, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(9448, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(9446, 6, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(9444, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(9440, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9438, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(9436, 2, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new int[] {100,111}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9434, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(9430, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__doWhile_Statement__label_Label_layouts_LAYOUTLIST_lit_do_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__emptyStatement_Statement__lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(9424, 0, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__emptyStatement_Statement__lit___59_, tmp);
	}
    protected static final void _init_prod__expression_Statement__expression_Expression_layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(9690, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9688, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(9684, 0, "Expression", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__expression_Statement__expression_Expression_layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__fail_Statement__lit_fail_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(9558, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9556, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(9552, 2, "Target", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9550, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(9548, 0, prod__lit_fail__char_class___range__102_102_char_class___range__97_97_char_class___range__105_105_char_class___range__108_108_, new int[] {102,97,105,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__fail_Statement__lit_fail_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__filter_Statement__lit_filter_layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(9900, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9898, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(9896, 0, prod__lit_filter__char_class___range__102_102_char_class___range__105_105_char_class___range__108_108_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new int[] {102,105,108,116,101,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__filter_Statement__lit_filter_layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__for_Statement__label_Label_layouts_LAYOUTLIST_lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement__tag__breakable___123_103_101_110_101_114_97_116_111_114_115_125_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(9384, 0, "Label", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9388, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(9390, 2, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9392, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(9394, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(9396, 5, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(9406, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(9398, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(9400, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(9402, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(9404, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(9410, 7, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(9412, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(9414, 9, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(9416, 10, "Statement", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__for_Statement__label_Label_layouts_LAYOUTLIST_lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement__tag__breakable___123_103_101_110_101_114_97_116_111_114_115_125_tag__breakable, tmp);
	}
    protected static final void _init_prod__functionDeclaration_Statement__functionDeclaration_FunctionDeclaration__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(9580, 0, "FunctionDeclaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__functionDeclaration_Statement__functionDeclaration_FunctionDeclaration__tag__breakable, tmp);
	}
    protected static final void _init_prod__globalDirective_Statement__lit_global_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_names_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(9720, 6, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(9718, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(9714, 4, regular__iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(9706, 0, "QualifiedName", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(9708, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(9710, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(9712, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9704, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(9700, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9698, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(9696, 0, prod__lit_global__char_class___range__103_103_char_class___range__108_108_char_class___range__111_111_char_class___range__98_98_char_class___range__97_97_char_class___range__108_108_, new int[] {103,108,111,98,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__globalDirective_Statement__lit_global_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_names_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__ifThen_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_empty__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new EmptyStackNode<IConstructor>(9770, 12, regular__empty, null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {101,108,115,101})});
      tmp[11] = new NonTerminalStackNode<IConstructor>(9764, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(9760, 10, "Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(9756, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(9754, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(9752, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(9748, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(9740, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(9742, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(9744, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(9746, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(9738, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(9736, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9734, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(9732, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9730, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(9726, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ifThen_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_empty__tag__breakable, tmp);
	}
    protected static final void _init_prod__ifThenElse_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_elseStatement_Statement__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(9630, 0, "Label", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9634, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(9636, 2, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9638, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(9640, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(9642, 5, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(9652, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(9644, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(9646, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(9648, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(9650, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(9656, 7, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(9658, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(9660, 9, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(9662, 10, "Statement", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(9666, 11, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(9668, 12, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(9670, 13, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new NonTerminalStackNode<IConstructor>(9674, 14, "Statement", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ifThenElse_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_elseStatement_Statement__tag__breakable, tmp);
	}
    protected static final void _init_prod__insert_Statement__lit_insert_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(9300, 4, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9296, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(9292, 2, "DataTarget", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9290, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(9288, 0, prod__lit_insert__char_class___range__105_105_char_class___range__110_110_char_class___range__115_115_char_class___range__101_101_char_class___range__114_114_char_class___range__116_116_, new int[] {105,110,115,101,114,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__insert_Statement__lit_insert_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable, tmp);
	}
    protected static final void _init_prod__nonEmptyBlock_Statement__label_Label_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(9818, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(9816, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(9812, 4, regular__iter_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(9808, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(9810, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9806, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(9804, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9802, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(9798, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__nonEmptyBlock_Statement__label_Label_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__return_Statement__lit_return_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(9260, 2, "Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9256, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(9254, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new int[] {114,101,116,117,114,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__return_Statement__lit_return_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable, tmp);
	}
    protected static final void _init_prod__solve_Statement__lit_solve_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_variables_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_bound_Bound_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode<IConstructor>(9888, 10, "Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(9884, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(9882, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(9880, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(9876, 6, "Bound", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(9874, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(9870, 4, regular__iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(9862, 0, "QualifiedName", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(9864, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(9866, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(9868, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9860, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(9858, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9856, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(9854, 0, prod__lit_solve__char_class___range__115_115_char_class___range__111_111_char_class___range__108_108_char_class___range__118_118_char_class___range__101_101_, new int[] {115,111,108,118,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__solve_Statement__lit_solve_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_variables_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_bound_Bound_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement__tag__breakable, tmp);
	}
    protected static final void _init_prod__switch_Statement__label_Label_layouts_LAYOUTLIST_lit_switch_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(9542, 14, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(9540, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(9536, 12, regular__iter_seps__Case__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(9532, 0, "Case", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(9534, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(9530, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(9528, 10, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(9526, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(9524, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(9522, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(9518, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(9516, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(9514, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9512, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(9510, 2, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {115,119,105,116,99,104}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9508, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(9504, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__switch_Statement__label_Label_layouts_LAYOUTLIST_lit_switch_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125__tag__breakable, tmp);
	}
    protected static final void _init_prod__throw_Statement__lit_throw_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(9246, 2, "Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9242, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(9240, 0, prod__lit_throw__char_class___range__116_116_char_class___range__104_104_char_class___range__114_114_char_class___range__111_111_char_class___range__119_119_, new int[] {116,104,114,111,119}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__throw_Statement__lit_throw_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable, tmp);
	}
    protected static final void _init_prod__try_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST__assoc__non_assoc_tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new SeparatedListStackNode<IConstructor>(9790, 4, regular__iter_seps__Catch__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(9786, 0, "Catch", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(9788, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9784, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(9780, 2, "Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9778, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(9776, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new int[] {116,114,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__try_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST__assoc__non_assoc_tag__breakable, tmp);
	}
    protected static final void _init_prod__tryFinally_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit_finally_layouts_LAYOUTLIST_finallyBody_Statement__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode<IConstructor>(9496, 8, "Statement", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(9492, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(9490, 6, prod__lit_finally__char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__108_108_char_class___range__121_121_, new int[] {102,105,110,97,108,108,121}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(9488, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(9484, 4, regular__iter_seps__Catch__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(9480, 0, "Catch", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(9482, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9478, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(9474, 2, "Statement", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9472, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(9470, 0, prod__lit_try__char_class___range__116_116_char_class___range__114_114_char_class___range__121_121_, new int[] {116,114,121}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__tryFinally_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit_finally_layouts_LAYOUTLIST_finallyBody_Statement__tag__breakable, tmp);
	}
    protected static final void _init_prod__variableDeclaration_Statement__declaration_LocalVariableDeclaration_layouts_LAYOUTLIST_lit___59__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(9336, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9334, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(9330, 0, "LocalVariableDeclaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__variableDeclaration_Statement__declaration_LocalVariableDeclaration_layouts_LAYOUTLIST_lit___59__tag__breakable, tmp);
	}
    protected static final void _init_prod__visit_Statement__label_Label_layouts_LAYOUTLIST_visit_Visit__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(9830, 2, "Visit", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9828, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(9824, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__visit_Statement__label_Label_layouts_LAYOUTLIST_visit_Visit__tag__breakable, tmp);
	}
    protected static final void _init_prod__while_Statement__label_Label_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement__tag__breakable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new NonTerminalStackNode<IConstructor>(9376, 10, "Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(9372, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(9370, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(9368, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(9364, 6, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(9356, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(9358, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(9360, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(9362, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(9354, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(9352, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9350, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(9348, 2, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9346, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(9342, 0, "Label", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__while_Statement__label_Label_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement__tag__breakable, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__append_Statement__lit_append_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable(builder);
      
        _init_prod__assert_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__assertWithMessage_Statement__lit_assert_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_message_Expression_layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__assignment_Statement__assignable_Assignable_layouts_LAYOUTLIST_operator_Assignment_layouts_LAYOUTLIST_statement_Statement__tag__breakable(builder);
      
        _init_prod__break_Statement__lit_break_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__continue_Statement__lit_continue_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__doWhile_Statement__label_Label_layouts_LAYOUTLIST_lit_do_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__emptyStatement_Statement__lit___59_(builder);
      
        _init_prod__expression_Statement__expression_Expression_layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__fail_Statement__lit_fail_layouts_LAYOUTLIST_target_Target_layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__filter_Statement__lit_filter_layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__for_Statement__label_Label_layouts_LAYOUTLIST_lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement__tag__breakable___123_103_101_110_101_114_97_116_111_114_115_125_tag__breakable(builder);
      
        _init_prod__functionDeclaration_Statement__functionDeclaration_FunctionDeclaration__tag__breakable(builder);
      
        _init_prod__globalDirective_Statement__lit_global_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_names_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__ifThen_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_empty__tag__breakable(builder);
      
        _init_prod__ifThenElse_Statement__label_Label_layouts_LAYOUTLIST_lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_thenStatement_Statement_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_elseStatement_Statement__tag__breakable(builder);
      
        _init_prod__insert_Statement__lit_insert_layouts_LAYOUTLIST_dataTarget_DataTarget_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable(builder);
      
        _init_prod__nonEmptyBlock_Statement__label_Label_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_statements_iter_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__return_Statement__lit_return_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable(builder);
      
        _init_prod__solve_Statement__lit_solve_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_variables_iter_seps__QualifiedName__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_bound_Bound_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement__tag__breakable(builder);
      
        _init_prod__switch_Statement__label_Label_layouts_LAYOUTLIST_lit_switch_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125__tag__breakable(builder);
      
        _init_prod__throw_Statement__lit_throw_layouts_LAYOUTLIST_statement_Statement__assoc__non_assoc_tag__breakable(builder);
      
        _init_prod__try_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST__assoc__non_assoc_tag__breakable(builder);
      
        _init_prod__tryFinally_Statement__lit_try_layouts_LAYOUTLIST_body_Statement_layouts_LAYOUTLIST_handlers_iter_seps__Catch__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit_finally_layouts_LAYOUTLIST_finallyBody_Statement__tag__breakable(builder);
      
        _init_prod__variableDeclaration_Statement__declaration_LocalVariableDeclaration_layouts_LAYOUTLIST_lit___59__tag__breakable(builder);
      
        _init_prod__visit_Statement__label_Label_layouts_LAYOUTLIST_visit_Visit__tag__breakable(builder);
      
        _init_prod__while_Statement__label_Label_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_body_Statement__tag__breakable(builder);
      
    }
  }
	
  protected static class Strategy {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__bottomUp_Strategy__lit_bottom_up_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6282, 0, prod__lit_bottom_up__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_, new int[] {98,111,116,116,111,109,45,117,112}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__bottomUp_Strategy__lit_bottom_up_, tmp);
	}
    protected static final void _init_prod__bottomUpBreak_Strategy__lit_bottom_up_break_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6294, 0, prod__lit_bottom_up_break__char_class___range__98_98_char_class___range__111_111_char_class___range__116_116_char_class___range__116_116_char_class___range__111_111_char_class___range__109_109_char_class___range__45_45_char_class___range__117_117_char_class___range__112_112_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,111,116,116,111,109,45,117,112,45,98,114,101,97,107}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__bottomUpBreak_Strategy__lit_bottom_up_break_, tmp);
	}
    protected static final void _init_prod__innermost_Strategy__lit_innermost_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6276, 0, prod__lit_innermost__char_class___range__105_105_char_class___range__110_110_char_class___range__110_110_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_, new int[] {105,110,110,101,114,109,111,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__innermost_Strategy__lit_innermost_, tmp);
	}
    protected static final void _init_prod__outermost_Strategy__lit_outermost_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6288, 0, prod__lit_outermost__char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__109_109_char_class___range__111_111_char_class___range__115_115_char_class___range__116_116_, new int[] {111,117,116,101,114,109,111,115,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__outermost_Strategy__lit_outermost_, tmp);
	}
    protected static final void _init_prod__topDown_Strategy__lit_top_down_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6264, 0, prod__lit_top_down__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_, new int[] {116,111,112,45,100,111,119,110}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__topDown_Strategy__lit_top_down_, tmp);
	}
    protected static final void _init_prod__topDownBreak_Strategy__lit_top_down_break_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(6270, 0, prod__lit_top_down_break__char_class___range__116_116_char_class___range__111_111_char_class___range__112_112_char_class___range__45_45_char_class___range__100_100_char_class___range__111_111_char_class___range__119_119_char_class___range__110_110_char_class___range__45_45_char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {116,111,112,45,100,111,119,110,45,98,114,101,97,107}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__topDownBreak_Strategy__lit_top_down_break_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__bottomUp_Strategy__lit_bottom_up_(builder);
      
        _init_prod__bottomUpBreak_Strategy__lit_bottom_up_break_(builder);
      
        _init_prod__innermost_Strategy__lit_innermost_(builder);
      
        _init_prod__outermost_Strategy__lit_outermost_(builder);
      
        _init_prod__topDown_Strategy__lit_top_down_(builder);
      
        _init_prod__topDownBreak_Strategy__lit_top_down_break_(builder);
      
    }
  }
	
  protected static class StringLiteral {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__interpolated_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(9000, 4, "StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(8998, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(8994, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(8992, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(8988, 0, "PreStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__interpolated_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_, tmp);
	}
    protected static final void _init_prod__nonInterpolated_StringLiteral__constant_StringConstant_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(9028, 0, "StringConstant", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__nonInterpolated_StringLiteral__constant_StringConstant_, tmp);
	}
    protected static final void _init_prod__template_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(9020, 4, "StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(9018, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(9014, 2, "StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(9012, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(9008, 0, "PreStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__template_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__interpolated_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_(builder);
      
        _init_prod__nonInterpolated_StringLiteral__constant_StringConstant_(builder);
      
        _init_prod__template_StringLiteral__pre_PreStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_(builder);
      
    }
  }
	
  protected static class StringMiddle {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__interpolated_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringMiddle_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(8466, 4, "StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(8464, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(8460, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(8458, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(8454, 0, "MidStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__interpolated_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringMiddle_, tmp);
	}
    protected static final void _init_prod__mid_StringMiddle__mid_MidStringChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(8446, 0, "MidStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__mid_StringMiddle__mid_MidStringChars_, tmp);
	}
    protected static final void _init_prod__template_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringMiddle_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(8486, 4, "StringMiddle", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(8484, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(8480, 2, "StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(8478, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(8474, 0, "MidStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__template_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringMiddle_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__interpolated_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringMiddle_(builder);
      
        _init_prod__mid_StringMiddle__mid_MidStringChars_(builder);
      
        _init_prod__template_StringMiddle__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringMiddle_(builder);
      
    }
  }
	
  protected static class StringTail {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__midInterpolated_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4466, 4, "StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4464, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4460, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4458, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4454, 0, "MidStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__midInterpolated_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_, tmp);
	}
    protected static final void _init_prod__midTemplate_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(4486, 4, "StringTail", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4484, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4480, 2, "StringTemplate", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4478, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4474, 0, "MidStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__midTemplate_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_, tmp);
	}
    protected static final void _init_prod__post_StringTail__post_PostStringChars_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4494, 0, "PostStringChars", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__post_StringTail__post_PostStringChars_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__midInterpolated_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_expression_Expression_layouts_LAYOUTLIST_tail_StringTail_(builder);
      
        _init_prod__midTemplate_StringTail__mid_MidStringChars_layouts_LAYOUTLIST_template_StringTemplate_layouts_LAYOUTLIST_tail_StringTail_(builder);
      
        _init_prod__post_StringTail__post_PostStringChars_(builder);
      
    }
  }
	
  protected static class StringTemplate {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__doWhile_StringTemplate__lit_do_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[19];
      
      tmp[18] = new LiteralStackNode<IConstructor>(7388, 18, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[17] = new NonTerminalStackNode<IConstructor>(7386, 17, "layouts_LAYOUTLIST", null, null);
      tmp[16] = new NonTerminalStackNode<IConstructor>(7382, 16, "Expression", null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(7380, 15, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new LiteralStackNode<IConstructor>(7378, 14, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(7376, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(7374, 12, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(7372, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(7370, 10, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(7368, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new SeparatedListStackNode<IConstructor>(7364, 8, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7360, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7362, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(7358, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(7354, 6, "StringMiddle", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(7352, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(7348, 4, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7344, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7346, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(7342, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(7340, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(7338, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(7336, 0, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new int[] {100,111}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__doWhile_StringTemplate__lit_do_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__for_StringTemplate__lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode<IConstructor>(7330, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(7328, 15, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(7324, 14, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7320, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7322, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(7318, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(7314, 12, "StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(7312, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(7308, 10, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7304, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7306, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(7302, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(7300, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(7298, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(7296, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(7294, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(7290, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7282, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7284, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(7286, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(7288, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(7280, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(7278, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(7276, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(7274, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__for_StringTemplate__lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__ifThen_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode<IConstructor>(7450, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(7448, 15, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(7444, 14, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7440, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7442, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(7438, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(7434, 12, "StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(7432, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(7428, 10, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7424, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7426, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(7422, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(7420, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(7418, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(7416, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(7414, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(7410, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7402, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7404, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(7406, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(7408, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(7400, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(7398, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(7396, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(7394, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ifThen_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__ifThenElse_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_thenString_StringMiddle_layouts_LAYOUTLIST_postStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_elseString_StringMiddle_layouts_LAYOUTLIST_postStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[29];
      
      tmp[28] = new LiteralStackNode<IConstructor>(7268, 28, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[27] = new NonTerminalStackNode<IConstructor>(7266, 27, "layouts_LAYOUTLIST", null, null);
      tmp[26] = new SeparatedListStackNode<IConstructor>(7262, 26, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7258, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7260, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[25] = new NonTerminalStackNode<IConstructor>(7256, 25, "layouts_LAYOUTLIST", null, null);
      tmp[24] = new NonTerminalStackNode<IConstructor>(7252, 24, "StringMiddle", null, null);
      tmp[23] = new NonTerminalStackNode<IConstructor>(7250, 23, "layouts_LAYOUTLIST", null, null);
      tmp[22] = new SeparatedListStackNode<IConstructor>(7246, 22, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7242, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7244, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[21] = new NonTerminalStackNode<IConstructor>(7240, 21, "layouts_LAYOUTLIST", null, null);
      tmp[20] = new LiteralStackNode<IConstructor>(7238, 20, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[19] = new NonTerminalStackNode<IConstructor>(7236, 19, "layouts_LAYOUTLIST", null, null);
      tmp[18] = new LiteralStackNode<IConstructor>(7234, 18, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      tmp[17] = new NonTerminalStackNode<IConstructor>(7232, 17, "layouts_LAYOUTLIST", null, null);
      tmp[16] = new LiteralStackNode<IConstructor>(7230, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(7228, 15, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(7224, 14, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7220, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7222, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(7218, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(7214, 12, "StringMiddle", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(7174, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(7176, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(7178, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(7180, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(7190, 4, regular__iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7182, 0, "Expression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7184, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(7186, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(7188, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(7194, 5, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(7196, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(7198, 7, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(7200, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(7202, 9, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(7208, 10, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7204, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7206, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(7212, 11, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__ifThenElse_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_thenString_StringMiddle_layouts_LAYOUTLIST_postStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_elseString_StringMiddle_layouts_LAYOUTLIST_postStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__while_StringTemplate__lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[17];
      
      tmp[16] = new LiteralStackNode<IConstructor>(7168, 16, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(7166, 15, "layouts_LAYOUTLIST", null, null);
      tmp[14] = new SeparatedListStackNode<IConstructor>(7162, 14, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7158, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7160, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(7156, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new NonTerminalStackNode<IConstructor>(7152, 12, "StringMiddle", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(7150, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(7146, 10, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(7142, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(7144, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(7140, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(7138, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(7136, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(7134, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(7132, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(7128, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(7126, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(7124, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(7122, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(7120, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__while_StringTemplate__lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__doWhile_StringTemplate__lit_do_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__for_StringTemplate__lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_generators_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__ifThen_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__ifThenElse_StringTemplate__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_conditions_iter_seps__Expression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_thenString_StringMiddle_layouts_LAYOUTLIST_postStatsThen_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_elseString_StringMiddle_layouts_LAYOUTLIST_postStatsElse_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__while_StringTemplate__lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_condition_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_preStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_body_StringMiddle_layouts_LAYOUTLIST_postStats_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class StructuredType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_StructuredType__basicType_BasicType_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_arguments_iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(5006, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5004, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(5000, 4, regular__iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4992, 0, "TypeArg", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4994, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(4996, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(4998, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4990, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4988, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4986, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4982, 0, "BasicType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_StructuredType__basicType_BasicType_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_arguments_iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_StructuredType__basicType_BasicType_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_arguments_iter_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class Sym {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__alternative_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_alternatives_iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(736, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(734, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(730, 6, regular__iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(722, 0, "Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(724, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(726, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null), new NonTerminalStackNode<IConstructor>(728, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(720, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(718, 4, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(716, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(712, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(710, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(708, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__alternative_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_alternatives_iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__caseInsensitiveLiteral_Sym__cistring_CaseInsensitiveStringConstant_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(796, 0, "CaseInsensitiveStringConstant", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__caseInsensitiveLiteral_Sym__cistring_CaseInsensitiveStringConstant_, tmp);
	}
    protected static final void _init_prod__characterClass_Sym__charClass_Class_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(610, 0, "Class", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__characterClass_Sym__charClass_Class_, tmp);
	}
    protected static final void _init_prod__column_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_column_IntegerLiteral_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(764, 4, "IntegerLiteral", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(762, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(760, 2, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(758, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(754, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__column_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_column_IntegerLiteral_, tmp);
	}
    protected static final void _init_prod__empty_Sym__lit___40_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(604, 2, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(602, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(600, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__empty_Sym__lit___40_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__endOfLine_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___36_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(748, 2, prod__lit___36__char_class___range__36_36_, new int[] {36}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(746, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(742, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__endOfLine_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___36_, tmp);
	}
    protected static final void _init_prod__except_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_layouts_LAYOUTLIST_label_NonterminalLabel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(926, 4, "NonterminalLabel", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(924, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(922, 2, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(920, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(916, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__except_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_layouts_LAYOUTLIST_label_NonterminalLabel_, tmp);
	}
    protected static final void _init_prod__follow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_match_Sym__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(964, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(962, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(960, 2, prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_, new int[] {62,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(958, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(954, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__follow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_match_Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__iter_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___43_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(910, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(908, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(904, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__iter_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___43_, tmp);
	}
    protected static final void _init_prod__iterSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(886, 8, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(884, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(882, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(880, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(876, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(874, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(870, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(868, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(866, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__iterSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_, tmp);
	}
    protected static final void _init_prod__iterStar_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(650, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(648, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(644, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__iterStar_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__iterStarSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(702, 8, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(700, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(698, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(696, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(692, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(690, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(686, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(684, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(682, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__iterStarSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_, tmp);
	}
    protected static final void _init_prod__labeled_Sym__symbol_Sym_layouts_LAYOUTLIST_label_NonterminalLabel_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(656, 0, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(660, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(662, 2, "NonterminalLabel", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__labeled_Sym__symbol_Sym_layouts_LAYOUTLIST_label_NonterminalLabel_, tmp);
	}
    protected static final void _init_prod__literal_Sym__string_StringConstant_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(824, 0, "StringConstant", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__literal_Sym__string_StringConstant_, tmp);
	}
    protected static final void _init_prod__nonterminal_Sym__nonterminal_Nonterminal_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(778, 0, "Nonterminal", null, new ICompletionFilter[] {new StringFollowRestriction(new int[] {91})});
      builder.addAlternative(ObjectRascalRascal.prod__nonterminal_Sym__nonterminal_Nonterminal_, tmp);
	}
    protected static final void _init_prod__notFollow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_62_62_layouts_LAYOUTLIST_match_Sym__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(946, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(944, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(942, 2, prod__lit___33_62_62__char_class___range__33_33_char_class___range__62_62_char_class___range__62_62_, new int[] {33,62,62}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(940, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(936, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__notFollow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_62_62_layouts_LAYOUTLIST_match_Sym__assoc__left, tmp);
	}
    protected static final void _init_prod__notPrecede_Sym__match_Sym_layouts_LAYOUTLIST_lit___33_60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(986, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(984, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(982, 2, prod__lit___33_60_60__char_class___range__33_33_char_class___range__60_60_char_class___range__60_60_, new int[] {33,60,60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(980, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(976, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__notPrecede_Sym__match_Sym_layouts_LAYOUTLIST_lit___33_60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__optional_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___63_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(790, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(788, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(784, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__optional_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___63_, tmp);
	}
    protected static final void _init_prod__parameter_Sym__lit___38_layouts_LAYOUTLIST_nonterminal_Nonterminal_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(674, 2, "Nonterminal", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(672, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(670, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__parameter_Sym__lit___38_layouts_LAYOUTLIST_nonterminal_Nonterminal_, tmp);
	}
    protected static final void _init_prod__parametrized_Sym__nonterminal_Nonterminal_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(860, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(858, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(854, 4, regular__iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(846, 0, "Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(848, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(850, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(852, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(844, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(842, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(840, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(838, 0, "Nonterminal", null, new ICompletionFilter[] {new StringFollowRequirement(new int[] {91})});
      builder.addAlternative(ObjectRascalRascal.prod__parametrized_Sym__nonterminal_Nonterminal_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__precede_Sym__match_Sym_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1004, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1002, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1000, 2, prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_, new int[] {60,60}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(998, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(994, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__precede_Sym__match_Sym_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right, tmp);
	}
    protected static final void _init_prod__sequence_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_sequence_iter_seps__Sym__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(638, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(636, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(632, 4, regular__iter_seps__Sym__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(628, 0, "Sym", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(630, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(626, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(622, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(620, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(618, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__sequence_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_sequence_iter_seps__Sym__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__start_Sym__lit_start_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_nonterminal_Nonterminal_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[0] = new LiteralStackNode<IConstructor>(804, 0, prod__lit_start__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_, new int[] {115,116,97,114,116}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(806, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(808, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(810, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(812, 4, "Nonterminal", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(816, 5, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(818, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__start_Sym__lit_start_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_nonterminal_Nonterminal_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__startOfLine_Sym__lit___94_layouts_LAYOUTLIST_symbol_Sym_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(896, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(894, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(892, 0, prod__lit___94__char_class___range__94_94_, new int[] {94}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__startOfLine_Sym__lit___94_layouts_LAYOUTLIST_symbol_Sym_, tmp);
	}
    protected static final void _init_prod__unequal_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___92_layouts_LAYOUTLIST_match_Sym__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(1022, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1020, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1018, 2, prod__lit___92__char_class___range__92_92_, new int[] {92}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1016, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1012, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__unequal_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___92_layouts_LAYOUTLIST_match_Sym__assoc__left, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__alternative_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_alternatives_iter_seps__Sym__layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__caseInsensitiveLiteral_Sym__cistring_CaseInsensitiveStringConstant_(builder);
      
        _init_prod__characterClass_Sym__charClass_Class_(builder);
      
        _init_prod__column_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___64_layouts_LAYOUTLIST_column_IntegerLiteral_(builder);
      
        _init_prod__empty_Sym__lit___40_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__endOfLine_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___36_(builder);
      
        _init_prod__except_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_layouts_LAYOUTLIST_label_NonterminalLabel_(builder);
      
        _init_prod__follow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_match_Sym__assoc__left(builder);
      
        _init_prod__iter_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___43_(builder);
      
        _init_prod__iterSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_(builder);
      
        _init_prod__iterStar_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__iterStarSep_Sym__lit___123_layouts_LAYOUTLIST_symbol_Sym_layouts_LAYOUTLIST_sep_Sym_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_(builder);
      
        _init_prod__labeled_Sym__symbol_Sym_layouts_LAYOUTLIST_label_NonterminalLabel_(builder);
      
        _init_prod__literal_Sym__string_StringConstant_(builder);
      
        _init_prod__nonterminal_Sym__nonterminal_Nonterminal_(builder);
      
        _init_prod__notFollow_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___33_62_62_layouts_LAYOUTLIST_match_Sym__assoc__left(builder);
      
        _init_prod__notPrecede_Sym__match_Sym_layouts_LAYOUTLIST_lit___33_60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right(builder);
      
        _init_prod__optional_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___63_(builder);
      
        _init_prod__parameter_Sym__lit___38_layouts_LAYOUTLIST_nonterminal_Nonterminal_(builder);
      
        _init_prod__parametrized_Sym__nonterminal_Nonterminal_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Sym__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__precede_Sym__match_Sym_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_symbol_Sym__assoc__right(builder);
      
        _init_prod__sequence_Sym__lit___40_layouts_LAYOUTLIST_first_Sym_layouts_LAYOUTLIST_sequence_iter_seps__Sym__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__start_Sym__lit_start_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_nonterminal_Nonterminal_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__startOfLine_Sym__lit___94_layouts_LAYOUTLIST_symbol_Sym_(builder);
      
        _init_prod__unequal_Sym__symbol_Sym_layouts_LAYOUTLIST_lit___92_layouts_LAYOUTLIST_match_Sym__assoc__left(builder);
      
    }
  }
	
  protected static class SyntaxDefinition {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__keyword_SyntaxDefinition__lit_keyword_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(6588, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(6586, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(6582, 6, "Prod", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(6580, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(6578, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(6576, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(6572, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6570, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(6568, 0, prod__lit_keyword__char_class___range__107_107_char_class___range__101_101_char_class___range__121_121_char_class___range__119_119_char_class___range__111_111_char_class___range__114_114_char_class___range__100_100_, new int[] {107,101,121,119,111,114,100}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__keyword_SyntaxDefinition__lit_keyword_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__language_SyntaxDefinition__start_Start_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(6620, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(6618, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(6614, 8, "Prod", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(6612, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(6610, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(6608, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(6604, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(6602, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(6600, 2, prod__lit_syntax__char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_, new int[] {115,121,110,116,97,120}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6598, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(6594, 0, "Start", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__language_SyntaxDefinition__start_Start_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__layout_SyntaxDefinition__vis_Visibility_layouts_LAYOUTLIST_lit_layout_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[10] = new LiteralStackNode<IConstructor>(6678, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(6676, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(6672, 8, "Prod", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(6670, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(6668, 6, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(6666, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(6662, 4, "Sym", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(6660, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(6658, 2, prod__lit_layout__char_class___range__108_108_char_class___range__97_97_char_class___range__121_121_char_class___range__111_111_char_class___range__117_117_char_class___range__116_116_, new int[] {108,97,121,111,117,116}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6656, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(6652, 0, "Visibility", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__layout_SyntaxDefinition__vis_Visibility_layouts_LAYOUTLIST_lit_layout_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    protected static final void _init_prod__lexical_SyntaxDefinition__lit_lexical_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new LiteralStackNode<IConstructor>(6646, 8, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(6644, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(6640, 6, "Prod", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(6638, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(6636, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(6634, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(6630, 2, "Sym", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6628, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(6626, 0, prod__lit_lexical__char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_, new int[] {108,101,120,105,99,97,108}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__lexical_SyntaxDefinition__lit_lexical_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__keyword_SyntaxDefinition__lit_keyword_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__language_SyntaxDefinition__start_Start_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__layout_SyntaxDefinition__vis_Visibility_layouts_LAYOUTLIST_lit_layout_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
        _init_prod__lexical_SyntaxDefinition__lit_lexical_layouts_LAYOUTLIST_defined_Sym_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_production_Prod_layouts_LAYOUTLIST_lit___59__tag__Foldable(builder);
      
    }
  }
	
  protected static class Tag {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_contents_TagString__tag__Folded_tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(11764, 4, "TagString", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11762, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(11758, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11756, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11754, 0, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_contents_TagString__tag__Folded_tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__empty_Tag__lit___64_layouts_LAYOUTLIST_name_Name__tag__Folded_tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(11798, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11796, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11794, 0, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__empty_Tag__lit___64_layouts_LAYOUTLIST_name_Name__tag__Folded_tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__expression_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression__tag__Folded_tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(11786, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(11784, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(11782, 4, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(11780, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(11776, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(11774, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(11772, 0, prod__lit___64__char_class___range__64_64_, new int[] {64}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__expression_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression__tag__Folded_tag__category___67_111_109_109_101_110_116, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_contents_TagString__tag__Folded_tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__empty_Tag__lit___64_layouts_LAYOUTLIST_name_Name__tag__Folded_tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__expression_Tag__lit___64_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_expression_Expression__tag__Folded_tag__category___67_111_109_109_101_110_116(builder);
      
    }
  }
	
  protected static class Tags {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Tags__tags_iter_star_seps__Tag__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(4740, 0, regular__iter_star_seps__Tag__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4736, 0, "Tag", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4738, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_Tags__tags_iter_star_seps__Tag__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Tags__tags_iter_star_seps__Tag__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class Target {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__empty_Target__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(6776, 0);
      builder.addAlternative(ObjectRascalRascal.prod__empty_Target__, tmp);
	}
    protected static final void _init_prod__labeled_Target__name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(6782, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__labeled_Target__name_Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__empty_Target__(builder);
      
        _init_prod__labeled_Target__name_Name_(builder);
      
    }
  }
	
  protected static class Toplevel {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__givenVisibility_Toplevel__declaration_Declaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(10908, 0, "Declaration", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__givenVisibility_Toplevel__declaration_Declaration_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__givenVisibility_Toplevel__declaration_Declaration_(builder);
      
    }
  }
	
  protected static class Type {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__basic_Type__basic_BasicType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(10190, 0, "BasicType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__basic_Type__basic_BasicType_, tmp);
	}
    protected static final void _init_prod__bracket_Type__lit___40_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new LiteralStackNode<IConstructor>(10184, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10182, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(10178, 2, "Type", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10176, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(10174, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__bracket_Type__lit___40_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__function_Type__function_FunctionType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(10166, 0, "FunctionType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__function_Type__function_FunctionType_, tmp);
	}
    protected static final void _init_prod__selector_Type__selector_DataTypeSelector_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(10228, 0, "DataTypeSelector", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__selector_Type__selector_DataTypeSelector_, tmp);
	}
    protected static final void _init_prod__structured_Type__structured_StructuredType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(10236, 0, "StructuredType", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__structured_Type__structured_StructuredType_, tmp);
	}
    protected static final void _init_prod__symbol_Type__symbol_Sym_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(10220, 0, "Sym", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__symbol_Type__symbol_Sym_, tmp);
	}
    protected static final void _init_prod__user_Type__user_UserType_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(10212, 0, "UserType", null, new ICompletionFilter[] {new StringMatchRestriction(new int[] {108,101,120,105,99,97,108}), new StringMatchRestriction(new int[] {105,109,112,111,114,116}), new StringMatchRestriction(new int[] {115,116,97,114,116}), new StringMatchRestriction(new int[] {115,121,110,116,97,120}), new StringMatchRestriction(new int[] {107,101,121,119,111,114,100}), new StringMatchRestriction(new int[] {101,120,116,101,110,100}), new StringMatchRestriction(new int[] {108,97,121,111,117,116})});
      builder.addAlternative(ObjectRascalRascal.prod__user_Type__user_UserType_, tmp);
	}
    protected static final void _init_prod__variable_Type__typeVar_TypeVar_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(10198, 0, "TypeVar", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__variable_Type__typeVar_TypeVar_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__basic_Type__basic_BasicType_(builder);
      
        _init_prod__bracket_Type__lit___40_layouts_LAYOUTLIST_type_Type_layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__function_Type__function_FunctionType_(builder);
      
        _init_prod__selector_Type__selector_DataTypeSelector_(builder);
      
        _init_prod__structured_Type__structured_StructuredType_(builder);
      
        _init_prod__symbol_Type__symbol_Sym_(builder);
      
        _init_prod__user_Type__user_UserType_(builder);
      
        _init_prod__variable_Type__typeVar_TypeVar_(builder);
      
    }
  }
	
  protected static class TypeArg {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_TypeArg__type_Type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(358, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__default_TypeArg__type_Type_, tmp);
	}
    protected static final void _init_prod__named_TypeArg__type_Type_layouts_LAYOUTLIST_name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(372, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(370, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(366, 0, "Type", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__named_TypeArg__type_Type_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_TypeArg__type_Type_(builder);
      
        _init_prod__named_TypeArg__type_Type_layouts_LAYOUTLIST_name_Name_(builder);
      
    }
  }
	
  protected static class TypeVar {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__bounded_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___60_58_layouts_LAYOUTLIST_bound_Type_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new NonTerminalStackNode<IConstructor>(10958, 6, "Type", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(10956, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(10954, 4, prod__lit___60_58__char_class___range__60_60_char_class___range__58_58_, new int[] {60,58}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(10952, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(10948, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10946, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(10944, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__bounded_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___60_58_layouts_LAYOUTLIST_bound_Type_, tmp);
	}
    protected static final void _init_prod__free_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(10936, 2, "Name", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(10934, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(10932, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__free_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__bounded_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_layouts_LAYOUTLIST_lit___60_58_layouts_LAYOUTLIST_bound_Type_(builder);
      
        _init_prod__free_TypeVar__lit___38_layouts_LAYOUTLIST_name_Name_(builder);
      
    }
  }
	
  protected static class UserType {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__name_UserType__name_QualifiedName_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(6866, 0, "QualifiedName", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__name_UserType__name_QualifiedName_, tmp);
	}
    protected static final void _init_prod__parametric_UserType__name_QualifiedName_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(6902, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(6900, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(6896, 4, regular__iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(6888, 0, "Type", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(6890, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(6892, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(6894, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(6886, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(6884, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6882, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(6880, 0, "QualifiedName", null, new ICompletionFilter[] {new StringFollowRequirement(new int[] {91})});
      builder.addAlternative(ObjectRascalRascal.prod__parametric_UserType__name_QualifiedName_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__name_UserType__name_QualifiedName_(builder);
      
        _init_prod__parametric_UserType__name_QualifiedName_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_parameters_iter_seps__Type__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_(builder);
      
    }
  }
	
  protected static class Variable {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__initialized_Variable__name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_initial_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(402, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(400, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(398, 2, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(396, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(392, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__initialized_Variable__name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_initial_Expression_, tmp);
	}
    protected static final void _init_prod__unInitialized_Variable__name_Name_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(384, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__unInitialized_Variable__name_Name_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__initialized_Variable__name_Name_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_initial_Expression_(builder);
      
        _init_prod__unInitialized_Variable__name_Name_(builder);
      
    }
  }
	
  protected static class Variant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__nAryConstructor_Variant__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(5196, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(5194, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(5190, 4, regular__iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5182, 0, "TypeArg", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5184, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(5186, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(5188, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5180, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5178, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5176, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5172, 0, "Name", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__nAryConstructor_Variant__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__nAryConstructor_Variant__name_Name_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_arguments_iter_star_seps__TypeArg__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
    }
  }
	
  protected static class Visibility {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__default_Visibility__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(8960, 0);
      builder.addAlternative(ObjectRascalRascal.prod__default_Visibility__, tmp);
	}
    protected static final void _init_prod__private_Visibility__lit_private_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(8956, 0, prod__lit_private__char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__118_118_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_, new int[] {112,114,105,118,97,116,101}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__private_Visibility__lit_private_, tmp);
	}
    protected static final void _init_prod__public_Visibility__lit_public_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(8966, 0, prod__lit_public__char_class___range__112_112_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__105_105_char_class___range__99_99_, new int[] {112,117,98,108,105,99}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__public_Visibility__lit_public_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__default_Visibility__(builder);
      
        _init_prod__private_Visibility__lit_private_(builder);
      
        _init_prod__public_Visibility__lit_public_(builder);
      
    }
  }
	
  protected static class Visit {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__defaultStrategy_Visit__lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(8754, 12, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(8752, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(8748, 10, regular__iter_seps__Case__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(8744, 0, "Case", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(8746, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(8742, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(8740, 8, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(8738, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(8736, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(8734, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(8730, 4, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(8728, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(8726, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(8724, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(8722, 0, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new int[] {118,105,115,105,116}, null, null);
      builder.addAlternative(ObjectRascalRascal.prod__defaultStrategy_Visit__lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__givenStrategy_Visit__strategy_Strategy_layouts_LAYOUTLIST_lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[14] = new LiteralStackNode<IConstructor>(8716, 14, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(8714, 13, "layouts_LAYOUTLIST", null, null);
      tmp[12] = new SeparatedListStackNode<IConstructor>(8710, 12, regular__iter_seps__Case__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(8706, 0, "Case", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(8708, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(8704, 11, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(8702, 10, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(8700, 9, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(8698, 8, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(8696, 7, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(8692, 6, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(8690, 5, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(8688, 4, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(8686, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(8684, 2, prod__lit_visit__char_class___range__118_118_char_class___range__105_105_char_class___range__115_115_char_class___range__105_105_char_class___range__116_116_, new int[] {118,105,115,105,116}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(8682, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(8678, 0, "Strategy", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__givenStrategy_Visit__strategy_Strategy_layouts_LAYOUTLIST_lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__defaultStrategy_Visit__lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__givenStrategy_Visit__strategy_Strategy_layouts_LAYOUTLIST_lit_visit_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_subject_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_cases_iter_seps__Case__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class start__Command {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__Command__layouts_LAYOUTLIST_top_Command_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(6832, 2, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(6828, 1, "Command", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(6826, 0, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__start__Command__layouts_LAYOUTLIST_top_Command_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__Command__layouts_LAYOUTLIST_top_Command_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class start__Commands {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__Commands__layouts_LAYOUTLIST_top_Commands_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(4700, 2, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4696, 1, "Commands", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4694, 0, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__start__Commands__layouts_LAYOUTLIST_top_Commands_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__Commands__layouts_LAYOUTLIST_top_Commands_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class start__EvalCommand {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__EvalCommand__layouts_LAYOUTLIST_top_EvalCommand_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(5762, 2, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5758, 1, "EvalCommand", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5756, 0, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__start__EvalCommand__layouts_LAYOUTLIST_top_EvalCommand_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__EvalCommand__layouts_LAYOUTLIST_top_EvalCommand_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class start__Module {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__Module__layouts_LAYOUTLIST_top_Module_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(4410, 2, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4406, 1, "Module", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4404, 0, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__start__Module__layouts_LAYOUTLIST_top_Module_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__Module__layouts_LAYOUTLIST_top_Module_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class start__PreModule {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__PreModule__layouts_LAYOUTLIST_top_PreModule_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(5128, 2, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5124, 1, "PreModule", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5122, 0, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(ObjectRascalRascal.prod__start__PreModule__layouts_LAYOUTLIST_top_PreModule_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
      
        _init_prod__start__PreModule__layouts_LAYOUTLIST_top_PreModule_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  public ObjectRascalRascal() {
    super();
  }

  // Parse methods    
  
  public AbstractStackNode<IConstructor>[] HeaderKeyword() {
    return HeaderKeyword.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] RascalKeywords() {
    return RascalKeywords.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] layouts_$BACKTICKS() {
    return layouts_$BACKTICKS.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] layouts_$QUOTES() {
    return layouts_$QUOTES.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] layouts_$default$() {
    return layouts_$default$.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] layouts_LAYOUTLIST() {
    return layouts_LAYOUTLIST.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Backslash() {
    return Backslash.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] BooleanLiteral() {
    return BooleanLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] CaseInsensitiveStringConstant() {
    return CaseInsensitiveStringConstant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Char() {
    return Char.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Comment() {
    return Comment.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] DateAndTime() {
    return DateAndTime.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] DatePart() {
    return DatePart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] DecimalIntegerLiteral() {
    return DecimalIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] HexIntegerLiteral() {
    return HexIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] JustDate() {
    return JustDate.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] JustTime() {
    return JustTime.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] LAYOUT() {
    return LAYOUT.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] MidPathChars() {
    return MidPathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] MidProtocolChars() {
    return MidProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] MidStringChars() {
    return MidStringChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Name() {
    return Name.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] NamedBackslash() {
    return NamedBackslash.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] NamedRegExp() {
    return NamedRegExp.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Nonterminal() {
    return Nonterminal.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] NonterminalLabel() {
    return NonterminalLabel.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] OctalIntegerLiteral() {
    return OctalIntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PathChars() {
    return PathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PostPathChars() {
    return PostPathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PostProtocolChars() {
    return PostProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PostStringChars() {
    return PostStringChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PrePathChars() {
    return PrePathChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PreProtocolChars() {
    return PreProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PreStringChars() {
    return PreStringChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ProtocolChars() {
    return ProtocolChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] RationalLiteral() {
    return RationalLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] RealLiteral() {
    return RealLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] RegExp() {
    return RegExp.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] RegExpLiteral() {
    return RegExpLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] RegExpModifier() {
    return RegExpModifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Rest() {
    return Rest.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StringCharacter() {
    return StringCharacter.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StringConstant() {
    return StringConstant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] TagString() {
    return TagString.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] TimePartNoTZ() {
    return TimePartNoTZ.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] TimeZonePart() {
    return TimeZonePart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] URLChars() {
    return URLChars.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] UnicodeEscape() {
    return UnicodeEscape.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Mapping__Expression() {
    return Mapping__Expression.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Mapping__Pattern() {
    return Mapping__Pattern.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Assignable() {
    return Assignable.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Assignment() {
    return Assignment.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Assoc() {
    return Assoc.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] BasicType() {
    return BasicType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Body() {
    return Body.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Bound() {
    return Bound.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Case() {
    return Case.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Catch() {
    return Catch.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Class() {
    return Class.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Command() {
    return Command.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Commands() {
    return Commands.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Comprehension() {
    return Comprehension.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] DataTarget() {
    return DataTarget.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] DataTypeSelector() {
    return DataTypeSelector.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] DateTimeLiteral() {
    return DateTimeLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Declaration() {
    return Declaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Declarator() {
    return Declarator.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] EvalCommand() {
    return EvalCommand.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Expression() {
    return Expression.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Field() {
    return Field.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Formals() {
    return Formals.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] FunctionBody() {
    return FunctionBody.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] FunctionDeclaration() {
    return FunctionDeclaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] FunctionModifier() {
    return FunctionModifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] FunctionModifiers() {
    return FunctionModifiers.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] FunctionType() {
    return FunctionType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Header() {
    return Header.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Import() {
    return Import.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ImportedModule() {
    return ImportedModule.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] IntegerLiteral() {
    return IntegerLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Kind() {
    return Kind.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Label() {
    return Label.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Literal() {
    return Literal.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] LocalVariableDeclaration() {
    return LocalVariableDeclaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] LocationLiteral() {
    return LocationLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Module() {
    return Module.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ModuleActuals() {
    return ModuleActuals.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ModuleParameters() {
    return ModuleParameters.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Parameters() {
    return Parameters.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PathPart() {
    return PathPart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PathTail() {
    return PathTail.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Pattern() {
    return Pattern.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PatternWithAction() {
    return PatternWithAction.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PreModule() {
    return PreModule.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Prod() {
    return Prod.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ProdModifier() {
    return ProdModifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ProtocolPart() {
    return ProtocolPart.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ProtocolTail() {
    return ProtocolTail.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] QualifiedName() {
    return QualifiedName.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Range() {
    return Range.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Renaming() {
    return Renaming.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Renamings() {
    return Renamings.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Replacement() {
    return Replacement.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ShellCommand() {
    return ShellCommand.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Signature() {
    return Signature.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Start() {
    return Start.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Statement() {
    return Statement.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Strategy() {
    return Strategy.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StringLiteral() {
    return StringLiteral.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StringMiddle() {
    return StringMiddle.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StringTail() {
    return StringTail.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StringTemplate() {
    return StringTemplate.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StructuredType() {
    return StructuredType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Sym() {
    return Sym.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] SyntaxDefinition() {
    return SyntaxDefinition.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Tag() {
    return Tag.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Tags() {
    return Tags.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Target() {
    return Target.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Toplevel() {
    return Toplevel.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Type() {
    return Type.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] TypeArg() {
    return TypeArg.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] TypeVar() {
    return TypeVar.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] UserType() {
    return UserType.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Variable() {
    return Variable.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Variant() {
    return Variant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Visibility() {
    return Visibility.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Visit() {
    return Visit.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__Command() {
    return start__Command.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__Commands() {
    return start__Commands.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__EvalCommand() {
    return start__EvalCommand.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__Module() {
    return start__Module.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__PreModule() {
    return start__PreModule.EXPECTS;
  }
}