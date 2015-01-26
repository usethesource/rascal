package org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers;

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
public class CParser extends org.rascalmpl.parser.gtd.SGTDBF<IConstructor, IConstructor, ISourceLocation> {
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
	
  protected static java.lang.String _concat(java.lang.String ...args) {
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
    
    
    
    
    _putDontNest(result, 166, 235);
    
    _putDontNest(result, 166, 240);
    
    _putDontNest(result, 166, 247);
    
    _putDontNest(result, 166, 253);
    
    _putDontNest(result, 166, 257);
    
    _putDontNest(result, 166, 261);
    
    _putDontNest(result, 166, 265);
    
    _putDontNest(result, 166, 273);
    
    _putDontNest(result, 166, 277);
    
    _putDontNest(result, 166, 281);
    
    _putDontNest(result, 166, 288);
    
    _putDontNest(result, 166, 294);
    
    _putDontNest(result, 166, 302);
    
    _putDontNest(result, 166, 310);
    
    _putDontNest(result, 166, 316);
    
    _putDontNest(result, 166, 323);
    
    _putDontNest(result, 166, 329);
    
    _putDontNest(result, 166, 336);
    
    _putDontNest(result, 166, 342);
    
    _putDontNest(result, 166, 348);
    
    _putDontNest(result, 166, 354);
    
    _putDontNest(result, 166, 361);
    
    _putDontNest(result, 166, 367);
    
    _putDontNest(result, 166, 373);
    
    _putDontNest(result, 166, 379);
    
    _putDontNest(result, 166, 385);
    
    _putDontNest(result, 166, 391);
    
    _putDontNest(result, 166, 397);
    
    _putDontNest(result, 166, 407);
    
    _putDontNest(result, 166, 414);
    
    _putDontNest(result, 166, 420);
    
    _putDontNest(result, 166, 426);
    
    _putDontNest(result, 166, 432);
    
    _putDontNest(result, 166, 438);
    
    _putDontNest(result, 166, 444);
    
    _putDontNest(result, 166, 450);
    
    _putDontNest(result, 166, 456);
    
    _putDontNest(result, 166, 462);
    
    _putDontNest(result, 166, 468);
    
    _putDontNest(result, 166, 474);
    
    _putDontNest(result, 166, 481);
    
    _putDontNest(result, 177, 235);
    
    _putDontNest(result, 177, 240);
    
    _putDontNest(result, 177, 247);
    
    _putDontNest(result, 177, 253);
    
    _putDontNest(result, 177, 257);
    
    _putDontNest(result, 177, 261);
    
    _putDontNest(result, 177, 265);
    
    _putDontNest(result, 177, 273);
    
    _putDontNest(result, 177, 277);
    
    _putDontNest(result, 177, 281);
    
    _putDontNest(result, 177, 288);
    
    _putDontNest(result, 177, 294);
    
    _putDontNest(result, 177, 302);
    
    _putDontNest(result, 177, 310);
    
    _putDontNest(result, 177, 316);
    
    _putDontNest(result, 177, 323);
    
    _putDontNest(result, 177, 329);
    
    _putDontNest(result, 177, 336);
    
    _putDontNest(result, 177, 342);
    
    _putDontNest(result, 177, 348);
    
    _putDontNest(result, 177, 354);
    
    _putDontNest(result, 177, 361);
    
    _putDontNest(result, 177, 367);
    
    _putDontNest(result, 177, 373);
    
    _putDontNest(result, 177, 379);
    
    _putDontNest(result, 177, 385);
    
    _putDontNest(result, 177, 391);
    
    _putDontNest(result, 177, 397);
    
    _putDontNest(result, 177, 407);
    
    _putDontNest(result, 177, 414);
    
    _putDontNest(result, 177, 420);
    
    _putDontNest(result, 177, 426);
    
    _putDontNest(result, 177, 432);
    
    _putDontNest(result, 177, 438);
    
    _putDontNest(result, 177, 444);
    
    _putDontNest(result, 177, 450);
    
    _putDontNest(result, 177, 456);
    
    _putDontNest(result, 177, 462);
    
    _putDontNest(result, 177, 468);
    
    _putDontNest(result, 177, 474);
    
    _putDontNest(result, 177, 481);
    
    _putDontNest(result, 189, 235);
    
    _putDontNest(result, 189, 240);
    
    _putDontNest(result, 189, 247);
    
    _putDontNest(result, 189, 253);
    
    _putDontNest(result, 189, 257);
    
    _putDontNest(result, 189, 261);
    
    _putDontNest(result, 189, 265);
    
    _putDontNest(result, 189, 273);
    
    _putDontNest(result, 189, 277);
    
    _putDontNest(result, 189, 281);
    
    _putDontNest(result, 189, 288);
    
    _putDontNest(result, 189, 294);
    
    _putDontNest(result, 189, 302);
    
    _putDontNest(result, 189, 310);
    
    _putDontNest(result, 189, 316);
    
    _putDontNest(result, 189, 323);
    
    _putDontNest(result, 189, 329);
    
    _putDontNest(result, 189, 336);
    
    _putDontNest(result, 189, 342);
    
    _putDontNest(result, 189, 348);
    
    _putDontNest(result, 189, 354);
    
    _putDontNest(result, 189, 361);
    
    _putDontNest(result, 189, 367);
    
    _putDontNest(result, 189, 373);
    
    _putDontNest(result, 189, 379);
    
    _putDontNest(result, 189, 385);
    
    _putDontNest(result, 189, 391);
    
    _putDontNest(result, 189, 397);
    
    _putDontNest(result, 189, 407);
    
    _putDontNest(result, 189, 414);
    
    _putDontNest(result, 189, 420);
    
    _putDontNest(result, 189, 426);
    
    _putDontNest(result, 189, 432);
    
    _putDontNest(result, 189, 438);
    
    _putDontNest(result, 189, 444);
    
    _putDontNest(result, 189, 450);
    
    _putDontNest(result, 189, 456);
    
    _putDontNest(result, 189, 462);
    
    _putDontNest(result, 189, 468);
    
    _putDontNest(result, 189, 474);
    
    _putDontNest(result, 189, 481);
    
    _putDontNest(result, 195, 235);
    
    _putDontNest(result, 195, 240);
    
    _putDontNest(result, 195, 247);
    
    _putDontNest(result, 195, 253);
    
    _putDontNest(result, 195, 257);
    
    _putDontNest(result, 195, 261);
    
    _putDontNest(result, 195, 265);
    
    _putDontNest(result, 195, 273);
    
    _putDontNest(result, 195, 277);
    
    _putDontNest(result, 195, 281);
    
    _putDontNest(result, 195, 288);
    
    _putDontNest(result, 195, 294);
    
    _putDontNest(result, 195, 302);
    
    _putDontNest(result, 195, 310);
    
    _putDontNest(result, 195, 316);
    
    _putDontNest(result, 195, 323);
    
    _putDontNest(result, 195, 329);
    
    _putDontNest(result, 195, 336);
    
    _putDontNest(result, 195, 342);
    
    _putDontNest(result, 195, 348);
    
    _putDontNest(result, 195, 354);
    
    _putDontNest(result, 195, 361);
    
    _putDontNest(result, 195, 367);
    
    _putDontNest(result, 195, 373);
    
    _putDontNest(result, 195, 379);
    
    _putDontNest(result, 195, 385);
    
    _putDontNest(result, 195, 391);
    
    _putDontNest(result, 195, 397);
    
    _putDontNest(result, 195, 407);
    
    _putDontNest(result, 195, 414);
    
    _putDontNest(result, 195, 420);
    
    _putDontNest(result, 195, 426);
    
    _putDontNest(result, 195, 432);
    
    _putDontNest(result, 195, 438);
    
    _putDontNest(result, 195, 444);
    
    _putDontNest(result, 195, 450);
    
    _putDontNest(result, 195, 456);
    
    _putDontNest(result, 195, 462);
    
    _putDontNest(result, 195, 468);
    
    _putDontNest(result, 195, 474);
    
    _putDontNest(result, 195, 481);
    
    _putDontNest(result, 203, 235);
    
    _putDontNest(result, 203, 240);
    
    _putDontNest(result, 203, 247);
    
    _putDontNest(result, 203, 253);
    
    _putDontNest(result, 203, 257);
    
    _putDontNest(result, 203, 261);
    
    _putDontNest(result, 203, 265);
    
    _putDontNest(result, 203, 273);
    
    _putDontNest(result, 203, 277);
    
    _putDontNest(result, 203, 281);
    
    _putDontNest(result, 203, 288);
    
    _putDontNest(result, 203, 294);
    
    _putDontNest(result, 203, 302);
    
    _putDontNest(result, 203, 310);
    
    _putDontNest(result, 203, 316);
    
    _putDontNest(result, 203, 323);
    
    _putDontNest(result, 203, 329);
    
    _putDontNest(result, 203, 336);
    
    _putDontNest(result, 203, 342);
    
    _putDontNest(result, 203, 348);
    
    _putDontNest(result, 203, 354);
    
    _putDontNest(result, 203, 361);
    
    _putDontNest(result, 203, 367);
    
    _putDontNest(result, 203, 373);
    
    _putDontNest(result, 203, 379);
    
    _putDontNest(result, 203, 385);
    
    _putDontNest(result, 203, 391);
    
    _putDontNest(result, 203, 397);
    
    _putDontNest(result, 203, 407);
    
    _putDontNest(result, 203, 414);
    
    _putDontNest(result, 203, 420);
    
    _putDontNest(result, 203, 426);
    
    _putDontNest(result, 203, 432);
    
    _putDontNest(result, 203, 438);
    
    _putDontNest(result, 203, 444);
    
    _putDontNest(result, 203, 450);
    
    _putDontNest(result, 203, 456);
    
    _putDontNest(result, 203, 462);
    
    _putDontNest(result, 203, 468);
    
    _putDontNest(result, 203, 474);
    
    _putDontNest(result, 203, 481);
    
    _putDontNest(result, 219, 235);
    
    _putDontNest(result, 219, 240);
    
    _putDontNest(result, 219, 247);
    
    _putDontNest(result, 219, 253);
    
    _putDontNest(result, 219, 257);
    
    _putDontNest(result, 219, 261);
    
    _putDontNest(result, 219, 265);
    
    _putDontNest(result, 219, 273);
    
    _putDontNest(result, 219, 277);
    
    _putDontNest(result, 219, 281);
    
    _putDontNest(result, 219, 288);
    
    _putDontNest(result, 219, 294);
    
    _putDontNest(result, 219, 302);
    
    _putDontNest(result, 219, 310);
    
    _putDontNest(result, 219, 316);
    
    _putDontNest(result, 219, 323);
    
    _putDontNest(result, 219, 329);
    
    _putDontNest(result, 219, 336);
    
    _putDontNest(result, 219, 342);
    
    _putDontNest(result, 219, 348);
    
    _putDontNest(result, 219, 354);
    
    _putDontNest(result, 219, 361);
    
    _putDontNest(result, 219, 367);
    
    _putDontNest(result, 219, 373);
    
    _putDontNest(result, 219, 379);
    
    _putDontNest(result, 219, 385);
    
    _putDontNest(result, 219, 391);
    
    _putDontNest(result, 219, 397);
    
    _putDontNest(result, 219, 407);
    
    _putDontNest(result, 219, 414);
    
    _putDontNest(result, 219, 420);
    
    _putDontNest(result, 219, 426);
    
    _putDontNest(result, 219, 432);
    
    _putDontNest(result, 219, 438);
    
    _putDontNest(result, 219, 444);
    
    _putDontNest(result, 219, 450);
    
    _putDontNest(result, 219, 456);
    
    _putDontNest(result, 219, 462);
    
    _putDontNest(result, 219, 468);
    
    _putDontNest(result, 219, 474);
    
    _putDontNest(result, 219, 481);
    
    _putDontNest(result, 235, 288);
    
    _putDontNest(result, 235, 294);
    
    _putDontNest(result, 235, 302);
    
    _putDontNest(result, 235, 310);
    
    _putDontNest(result, 235, 316);
    
    _putDontNest(result, 235, 323);
    
    _putDontNest(result, 235, 329);
    
    _putDontNest(result, 235, 336);
    
    _putDontNest(result, 235, 342);
    
    _putDontNest(result, 235, 348);
    
    _putDontNest(result, 235, 354);
    
    _putDontNest(result, 235, 361);
    
    _putDontNest(result, 235, 367);
    
    _putDontNest(result, 235, 373);
    
    _putDontNest(result, 235, 379);
    
    _putDontNest(result, 235, 385);
    
    _putDontNest(result, 235, 391);
    
    _putDontNest(result, 235, 397);
    
    _putDontNest(result, 235, 407);
    
    _putDontNest(result, 235, 414);
    
    _putDontNest(result, 235, 420);
    
    _putDontNest(result, 235, 426);
    
    _putDontNest(result, 235, 432);
    
    _putDontNest(result, 235, 438);
    
    _putDontNest(result, 235, 444);
    
    _putDontNest(result, 235, 450);
    
    _putDontNest(result, 235, 456);
    
    _putDontNest(result, 235, 462);
    
    _putDontNest(result, 235, 468);
    
    _putDontNest(result, 235, 474);
    
    _putDontNest(result, 235, 481);
    
    _putDontNest(result, 240, 288);
    
    _putDontNest(result, 240, 294);
    
    _putDontNest(result, 240, 302);
    
    _putDontNest(result, 240, 310);
    
    _putDontNest(result, 240, 316);
    
    _putDontNest(result, 240, 323);
    
    _putDontNest(result, 240, 329);
    
    _putDontNest(result, 240, 336);
    
    _putDontNest(result, 240, 342);
    
    _putDontNest(result, 240, 348);
    
    _putDontNest(result, 240, 354);
    
    _putDontNest(result, 240, 361);
    
    _putDontNest(result, 240, 367);
    
    _putDontNest(result, 240, 373);
    
    _putDontNest(result, 240, 379);
    
    _putDontNest(result, 240, 385);
    
    _putDontNest(result, 240, 391);
    
    _putDontNest(result, 240, 397);
    
    _putDontNest(result, 240, 407);
    
    _putDontNest(result, 240, 414);
    
    _putDontNest(result, 240, 420);
    
    _putDontNest(result, 240, 426);
    
    _putDontNest(result, 240, 432);
    
    _putDontNest(result, 240, 438);
    
    _putDontNest(result, 240, 444);
    
    _putDontNest(result, 240, 450);
    
    _putDontNest(result, 240, 456);
    
    _putDontNest(result, 240, 462);
    
    _putDontNest(result, 240, 468);
    
    _putDontNest(result, 240, 474);
    
    _putDontNest(result, 240, 481);
    
    _putDontNest(result, 247, 288);
    
    _putDontNest(result, 247, 294);
    
    _putDontNest(result, 247, 302);
    
    _putDontNest(result, 247, 310);
    
    _putDontNest(result, 247, 316);
    
    _putDontNest(result, 247, 323);
    
    _putDontNest(result, 247, 329);
    
    _putDontNest(result, 247, 336);
    
    _putDontNest(result, 247, 342);
    
    _putDontNest(result, 247, 348);
    
    _putDontNest(result, 247, 354);
    
    _putDontNest(result, 247, 361);
    
    _putDontNest(result, 247, 367);
    
    _putDontNest(result, 247, 373);
    
    _putDontNest(result, 247, 379);
    
    _putDontNest(result, 247, 385);
    
    _putDontNest(result, 247, 391);
    
    _putDontNest(result, 247, 397);
    
    _putDontNest(result, 247, 407);
    
    _putDontNest(result, 247, 414);
    
    _putDontNest(result, 247, 420);
    
    _putDontNest(result, 247, 426);
    
    _putDontNest(result, 247, 432);
    
    _putDontNest(result, 247, 438);
    
    _putDontNest(result, 247, 444);
    
    _putDontNest(result, 247, 450);
    
    _putDontNest(result, 247, 456);
    
    _putDontNest(result, 247, 462);
    
    _putDontNest(result, 247, 468);
    
    _putDontNest(result, 247, 474);
    
    _putDontNest(result, 247, 481);
    
    _putDontNest(result, 253, 288);
    
    _putDontNest(result, 253, 294);
    
    _putDontNest(result, 253, 302);
    
    _putDontNest(result, 253, 310);
    
    _putDontNest(result, 253, 316);
    
    _putDontNest(result, 253, 323);
    
    _putDontNest(result, 253, 329);
    
    _putDontNest(result, 253, 336);
    
    _putDontNest(result, 253, 342);
    
    _putDontNest(result, 253, 348);
    
    _putDontNest(result, 253, 354);
    
    _putDontNest(result, 253, 361);
    
    _putDontNest(result, 253, 367);
    
    _putDontNest(result, 253, 373);
    
    _putDontNest(result, 253, 379);
    
    _putDontNest(result, 253, 385);
    
    _putDontNest(result, 253, 391);
    
    _putDontNest(result, 253, 397);
    
    _putDontNest(result, 253, 407);
    
    _putDontNest(result, 253, 414);
    
    _putDontNest(result, 253, 420);
    
    _putDontNest(result, 253, 426);
    
    _putDontNest(result, 253, 432);
    
    _putDontNest(result, 253, 438);
    
    _putDontNest(result, 253, 444);
    
    _putDontNest(result, 253, 450);
    
    _putDontNest(result, 253, 456);
    
    _putDontNest(result, 253, 462);
    
    _putDontNest(result, 253, 468);
    
    _putDontNest(result, 253, 474);
    
    _putDontNest(result, 253, 481);
    
    _putDontNest(result, 257, 288);
    
    _putDontNest(result, 257, 294);
    
    _putDontNest(result, 257, 302);
    
    _putDontNest(result, 257, 310);
    
    _putDontNest(result, 257, 316);
    
    _putDontNest(result, 257, 323);
    
    _putDontNest(result, 257, 329);
    
    _putDontNest(result, 257, 336);
    
    _putDontNest(result, 257, 342);
    
    _putDontNest(result, 257, 348);
    
    _putDontNest(result, 257, 354);
    
    _putDontNest(result, 257, 361);
    
    _putDontNest(result, 257, 367);
    
    _putDontNest(result, 257, 373);
    
    _putDontNest(result, 257, 379);
    
    _putDontNest(result, 257, 385);
    
    _putDontNest(result, 257, 391);
    
    _putDontNest(result, 257, 397);
    
    _putDontNest(result, 257, 407);
    
    _putDontNest(result, 257, 414);
    
    _putDontNest(result, 257, 420);
    
    _putDontNest(result, 257, 426);
    
    _putDontNest(result, 257, 432);
    
    _putDontNest(result, 257, 438);
    
    _putDontNest(result, 257, 444);
    
    _putDontNest(result, 257, 450);
    
    _putDontNest(result, 257, 456);
    
    _putDontNest(result, 257, 462);
    
    _putDontNest(result, 257, 468);
    
    _putDontNest(result, 257, 474);
    
    _putDontNest(result, 257, 481);
    
    _putDontNest(result, 261, 288);
    
    _putDontNest(result, 261, 294);
    
    _putDontNest(result, 261, 302);
    
    _putDontNest(result, 261, 310);
    
    _putDontNest(result, 261, 316);
    
    _putDontNest(result, 261, 323);
    
    _putDontNest(result, 261, 329);
    
    _putDontNest(result, 261, 336);
    
    _putDontNest(result, 261, 342);
    
    _putDontNest(result, 261, 348);
    
    _putDontNest(result, 261, 354);
    
    _putDontNest(result, 261, 361);
    
    _putDontNest(result, 261, 367);
    
    _putDontNest(result, 261, 373);
    
    _putDontNest(result, 261, 379);
    
    _putDontNest(result, 261, 385);
    
    _putDontNest(result, 261, 391);
    
    _putDontNest(result, 261, 397);
    
    _putDontNest(result, 261, 407);
    
    _putDontNest(result, 261, 414);
    
    _putDontNest(result, 261, 420);
    
    _putDontNest(result, 261, 426);
    
    _putDontNest(result, 261, 432);
    
    _putDontNest(result, 261, 438);
    
    _putDontNest(result, 261, 444);
    
    _putDontNest(result, 261, 450);
    
    _putDontNest(result, 261, 456);
    
    _putDontNest(result, 261, 462);
    
    _putDontNest(result, 261, 468);
    
    _putDontNest(result, 261, 474);
    
    _putDontNest(result, 261, 481);
    
    _putDontNest(result, 265, 288);
    
    _putDontNest(result, 265, 294);
    
    _putDontNest(result, 265, 302);
    
    _putDontNest(result, 265, 310);
    
    _putDontNest(result, 265, 316);
    
    _putDontNest(result, 265, 323);
    
    _putDontNest(result, 265, 329);
    
    _putDontNest(result, 265, 336);
    
    _putDontNest(result, 265, 342);
    
    _putDontNest(result, 265, 348);
    
    _putDontNest(result, 265, 354);
    
    _putDontNest(result, 265, 361);
    
    _putDontNest(result, 265, 367);
    
    _putDontNest(result, 265, 373);
    
    _putDontNest(result, 265, 379);
    
    _putDontNest(result, 265, 385);
    
    _putDontNest(result, 265, 391);
    
    _putDontNest(result, 265, 397);
    
    _putDontNest(result, 265, 407);
    
    _putDontNest(result, 265, 414);
    
    _putDontNest(result, 265, 420);
    
    _putDontNest(result, 265, 426);
    
    _putDontNest(result, 265, 432);
    
    _putDontNest(result, 265, 438);
    
    _putDontNest(result, 265, 444);
    
    _putDontNest(result, 265, 450);
    
    _putDontNest(result, 265, 456);
    
    _putDontNest(result, 265, 462);
    
    _putDontNest(result, 265, 468);
    
    _putDontNest(result, 265, 474);
    
    _putDontNest(result, 265, 481);
    
    _putDontNest(result, 273, 288);
    
    _putDontNest(result, 273, 294);
    
    _putDontNest(result, 273, 302);
    
    _putDontNest(result, 273, 310);
    
    _putDontNest(result, 273, 316);
    
    _putDontNest(result, 273, 323);
    
    _putDontNest(result, 273, 329);
    
    _putDontNest(result, 273, 336);
    
    _putDontNest(result, 273, 342);
    
    _putDontNest(result, 273, 348);
    
    _putDontNest(result, 273, 354);
    
    _putDontNest(result, 273, 361);
    
    _putDontNest(result, 273, 367);
    
    _putDontNest(result, 273, 373);
    
    _putDontNest(result, 273, 379);
    
    _putDontNest(result, 273, 385);
    
    _putDontNest(result, 273, 391);
    
    _putDontNest(result, 273, 397);
    
    _putDontNest(result, 273, 407);
    
    _putDontNest(result, 273, 414);
    
    _putDontNest(result, 273, 420);
    
    _putDontNest(result, 273, 426);
    
    _putDontNest(result, 273, 432);
    
    _putDontNest(result, 273, 438);
    
    _putDontNest(result, 273, 444);
    
    _putDontNest(result, 273, 450);
    
    _putDontNest(result, 273, 456);
    
    _putDontNest(result, 273, 462);
    
    _putDontNest(result, 273, 468);
    
    _putDontNest(result, 273, 474);
    
    _putDontNest(result, 273, 481);
    
    _putDontNest(result, 277, 288);
    
    _putDontNest(result, 277, 294);
    
    _putDontNest(result, 277, 302);
    
    _putDontNest(result, 277, 310);
    
    _putDontNest(result, 277, 316);
    
    _putDontNest(result, 277, 323);
    
    _putDontNest(result, 277, 329);
    
    _putDontNest(result, 277, 336);
    
    _putDontNest(result, 277, 342);
    
    _putDontNest(result, 277, 348);
    
    _putDontNest(result, 277, 354);
    
    _putDontNest(result, 277, 361);
    
    _putDontNest(result, 277, 367);
    
    _putDontNest(result, 277, 373);
    
    _putDontNest(result, 277, 379);
    
    _putDontNest(result, 277, 385);
    
    _putDontNest(result, 277, 391);
    
    _putDontNest(result, 277, 397);
    
    _putDontNest(result, 277, 407);
    
    _putDontNest(result, 277, 414);
    
    _putDontNest(result, 277, 420);
    
    _putDontNest(result, 277, 426);
    
    _putDontNest(result, 277, 432);
    
    _putDontNest(result, 277, 438);
    
    _putDontNest(result, 277, 444);
    
    _putDontNest(result, 277, 450);
    
    _putDontNest(result, 277, 456);
    
    _putDontNest(result, 277, 462);
    
    _putDontNest(result, 277, 468);
    
    _putDontNest(result, 277, 474);
    
    _putDontNest(result, 277, 481);
    
    _putDontNest(result, 281, 288);
    
    _putDontNest(result, 281, 294);
    
    _putDontNest(result, 281, 302);
    
    _putDontNest(result, 281, 310);
    
    _putDontNest(result, 281, 316);
    
    _putDontNest(result, 281, 323);
    
    _putDontNest(result, 281, 329);
    
    _putDontNest(result, 281, 336);
    
    _putDontNest(result, 281, 342);
    
    _putDontNest(result, 281, 348);
    
    _putDontNest(result, 281, 354);
    
    _putDontNest(result, 281, 361);
    
    _putDontNest(result, 281, 367);
    
    _putDontNest(result, 281, 373);
    
    _putDontNest(result, 281, 379);
    
    _putDontNest(result, 281, 385);
    
    _putDontNest(result, 281, 391);
    
    _putDontNest(result, 281, 397);
    
    _putDontNest(result, 281, 407);
    
    _putDontNest(result, 281, 414);
    
    _putDontNest(result, 281, 420);
    
    _putDontNest(result, 281, 426);
    
    _putDontNest(result, 281, 432);
    
    _putDontNest(result, 281, 438);
    
    _putDontNest(result, 281, 444);
    
    _putDontNest(result, 281, 450);
    
    _putDontNest(result, 281, 456);
    
    _putDontNest(result, 281, 462);
    
    _putDontNest(result, 281, 468);
    
    _putDontNest(result, 281, 474);
    
    _putDontNest(result, 281, 481);
    
    _putDontNest(result, 284, 310);
    
    _putDontNest(result, 284, 316);
    
    _putDontNest(result, 284, 323);
    
    _putDontNest(result, 284, 329);
    
    _putDontNest(result, 284, 336);
    
    _putDontNest(result, 284, 342);
    
    _putDontNest(result, 284, 348);
    
    _putDontNest(result, 284, 354);
    
    _putDontNest(result, 284, 361);
    
    _putDontNest(result, 284, 367);
    
    _putDontNest(result, 284, 373);
    
    _putDontNest(result, 284, 379);
    
    _putDontNest(result, 284, 385);
    
    _putDontNest(result, 284, 391);
    
    _putDontNest(result, 284, 397);
    
    _putDontNest(result, 284, 407);
    
    _putDontNest(result, 284, 414);
    
    _putDontNest(result, 284, 420);
    
    _putDontNest(result, 284, 426);
    
    _putDontNest(result, 284, 432);
    
    _putDontNest(result, 284, 438);
    
    _putDontNest(result, 284, 444);
    
    _putDontNest(result, 284, 450);
    
    _putDontNest(result, 284, 456);
    
    _putDontNest(result, 284, 462);
    
    _putDontNest(result, 284, 468);
    
    _putDontNest(result, 284, 474);
    
    _putDontNest(result, 284, 481);
    
    _putDontNest(result, 288, 288);
    
    _putDontNest(result, 288, 294);
    
    _putDontNest(result, 288, 302);
    
    _putDontNest(result, 288, 310);
    
    _putDontNest(result, 288, 316);
    
    _putDontNest(result, 288, 323);
    
    _putDontNest(result, 288, 329);
    
    _putDontNest(result, 288, 336);
    
    _putDontNest(result, 288, 342);
    
    _putDontNest(result, 288, 348);
    
    _putDontNest(result, 288, 354);
    
    _putDontNest(result, 288, 361);
    
    _putDontNest(result, 288, 367);
    
    _putDontNest(result, 288, 373);
    
    _putDontNest(result, 288, 379);
    
    _putDontNest(result, 288, 385);
    
    _putDontNest(result, 288, 391);
    
    _putDontNest(result, 288, 397);
    
    _putDontNest(result, 288, 407);
    
    _putDontNest(result, 288, 414);
    
    _putDontNest(result, 288, 420);
    
    _putDontNest(result, 288, 426);
    
    _putDontNest(result, 288, 432);
    
    _putDontNest(result, 288, 438);
    
    _putDontNest(result, 288, 444);
    
    _putDontNest(result, 288, 450);
    
    _putDontNest(result, 288, 456);
    
    _putDontNest(result, 288, 462);
    
    _putDontNest(result, 288, 468);
    
    _putDontNest(result, 288, 474);
    
    _putDontNest(result, 288, 481);
    
    _putDontNest(result, 290, 310);
    
    _putDontNest(result, 290, 316);
    
    _putDontNest(result, 290, 323);
    
    _putDontNest(result, 290, 329);
    
    _putDontNest(result, 290, 336);
    
    _putDontNest(result, 290, 342);
    
    _putDontNest(result, 290, 348);
    
    _putDontNest(result, 290, 354);
    
    _putDontNest(result, 290, 361);
    
    _putDontNest(result, 290, 367);
    
    _putDontNest(result, 290, 373);
    
    _putDontNest(result, 290, 379);
    
    _putDontNest(result, 290, 385);
    
    _putDontNest(result, 290, 391);
    
    _putDontNest(result, 290, 397);
    
    _putDontNest(result, 290, 407);
    
    _putDontNest(result, 290, 414);
    
    _putDontNest(result, 290, 420);
    
    _putDontNest(result, 290, 426);
    
    _putDontNest(result, 290, 432);
    
    _putDontNest(result, 290, 438);
    
    _putDontNest(result, 290, 444);
    
    _putDontNest(result, 290, 450);
    
    _putDontNest(result, 290, 456);
    
    _putDontNest(result, 290, 462);
    
    _putDontNest(result, 290, 468);
    
    _putDontNest(result, 290, 474);
    
    _putDontNest(result, 290, 481);
    
    _putDontNest(result, 294, 288);
    
    _putDontNest(result, 294, 294);
    
    _putDontNest(result, 294, 302);
    
    _putDontNest(result, 294, 310);
    
    _putDontNest(result, 294, 316);
    
    _putDontNest(result, 294, 323);
    
    _putDontNest(result, 294, 329);
    
    _putDontNest(result, 294, 336);
    
    _putDontNest(result, 294, 342);
    
    _putDontNest(result, 294, 348);
    
    _putDontNest(result, 294, 354);
    
    _putDontNest(result, 294, 361);
    
    _putDontNest(result, 294, 367);
    
    _putDontNest(result, 294, 373);
    
    _putDontNest(result, 294, 379);
    
    _putDontNest(result, 294, 385);
    
    _putDontNest(result, 294, 391);
    
    _putDontNest(result, 294, 397);
    
    _putDontNest(result, 294, 407);
    
    _putDontNest(result, 294, 414);
    
    _putDontNest(result, 294, 420);
    
    _putDontNest(result, 294, 426);
    
    _putDontNest(result, 294, 432);
    
    _putDontNest(result, 294, 438);
    
    _putDontNest(result, 294, 444);
    
    _putDontNest(result, 294, 450);
    
    _putDontNest(result, 294, 456);
    
    _putDontNest(result, 294, 462);
    
    _putDontNest(result, 294, 468);
    
    _putDontNest(result, 294, 474);
    
    _putDontNest(result, 294, 481);
    
    _putDontNest(result, 297, 310);
    
    _putDontNest(result, 297, 316);
    
    _putDontNest(result, 297, 323);
    
    _putDontNest(result, 297, 329);
    
    _putDontNest(result, 297, 336);
    
    _putDontNest(result, 297, 342);
    
    _putDontNest(result, 297, 348);
    
    _putDontNest(result, 297, 354);
    
    _putDontNest(result, 297, 361);
    
    _putDontNest(result, 297, 367);
    
    _putDontNest(result, 297, 373);
    
    _putDontNest(result, 297, 379);
    
    _putDontNest(result, 297, 385);
    
    _putDontNest(result, 297, 391);
    
    _putDontNest(result, 297, 397);
    
    _putDontNest(result, 297, 407);
    
    _putDontNest(result, 297, 414);
    
    _putDontNest(result, 297, 420);
    
    _putDontNest(result, 297, 426);
    
    _putDontNest(result, 297, 432);
    
    _putDontNest(result, 297, 438);
    
    _putDontNest(result, 297, 444);
    
    _putDontNest(result, 297, 450);
    
    _putDontNest(result, 297, 456);
    
    _putDontNest(result, 297, 462);
    
    _putDontNest(result, 297, 468);
    
    _putDontNest(result, 297, 474);
    
    _putDontNest(result, 297, 481);
    
    _putDontNest(result, 302, 288);
    
    _putDontNest(result, 302, 294);
    
    _putDontNest(result, 302, 302);
    
    _putDontNest(result, 302, 310);
    
    _putDontNest(result, 302, 316);
    
    _putDontNest(result, 302, 323);
    
    _putDontNest(result, 302, 329);
    
    _putDontNest(result, 302, 336);
    
    _putDontNest(result, 302, 342);
    
    _putDontNest(result, 302, 348);
    
    _putDontNest(result, 302, 354);
    
    _putDontNest(result, 302, 361);
    
    _putDontNest(result, 302, 367);
    
    _putDontNest(result, 302, 373);
    
    _putDontNest(result, 302, 379);
    
    _putDontNest(result, 302, 385);
    
    _putDontNest(result, 302, 391);
    
    _putDontNest(result, 302, 397);
    
    _putDontNest(result, 302, 407);
    
    _putDontNest(result, 302, 414);
    
    _putDontNest(result, 302, 420);
    
    _putDontNest(result, 302, 426);
    
    _putDontNest(result, 302, 432);
    
    _putDontNest(result, 302, 438);
    
    _putDontNest(result, 302, 444);
    
    _putDontNest(result, 302, 450);
    
    _putDontNest(result, 302, 456);
    
    _putDontNest(result, 302, 462);
    
    _putDontNest(result, 302, 468);
    
    _putDontNest(result, 302, 474);
    
    _putDontNest(result, 302, 481);
    
    _putDontNest(result, 306, 323);
    
    _putDontNest(result, 306, 329);
    
    _putDontNest(result, 306, 336);
    
    _putDontNest(result, 306, 342);
    
    _putDontNest(result, 306, 348);
    
    _putDontNest(result, 306, 354);
    
    _putDontNest(result, 306, 361);
    
    _putDontNest(result, 306, 367);
    
    _putDontNest(result, 306, 373);
    
    _putDontNest(result, 306, 379);
    
    _putDontNest(result, 306, 385);
    
    _putDontNest(result, 306, 391);
    
    _putDontNest(result, 306, 397);
    
    _putDontNest(result, 306, 407);
    
    _putDontNest(result, 306, 414);
    
    _putDontNest(result, 306, 420);
    
    _putDontNest(result, 306, 426);
    
    _putDontNest(result, 306, 432);
    
    _putDontNest(result, 306, 438);
    
    _putDontNest(result, 306, 444);
    
    _putDontNest(result, 306, 450);
    
    _putDontNest(result, 306, 456);
    
    _putDontNest(result, 306, 462);
    
    _putDontNest(result, 306, 468);
    
    _putDontNest(result, 306, 474);
    
    _putDontNest(result, 306, 481);
    
    _putDontNest(result, 310, 310);
    
    _putDontNest(result, 310, 316);
    
    _putDontNest(result, 310, 323);
    
    _putDontNest(result, 310, 329);
    
    _putDontNest(result, 310, 336);
    
    _putDontNest(result, 310, 342);
    
    _putDontNest(result, 310, 348);
    
    _putDontNest(result, 310, 354);
    
    _putDontNest(result, 310, 361);
    
    _putDontNest(result, 310, 367);
    
    _putDontNest(result, 310, 373);
    
    _putDontNest(result, 310, 379);
    
    _putDontNest(result, 310, 385);
    
    _putDontNest(result, 310, 391);
    
    _putDontNest(result, 310, 397);
    
    _putDontNest(result, 310, 407);
    
    _putDontNest(result, 310, 414);
    
    _putDontNest(result, 310, 420);
    
    _putDontNest(result, 310, 426);
    
    _putDontNest(result, 310, 432);
    
    _putDontNest(result, 310, 438);
    
    _putDontNest(result, 310, 444);
    
    _putDontNest(result, 310, 450);
    
    _putDontNest(result, 310, 456);
    
    _putDontNest(result, 310, 462);
    
    _putDontNest(result, 310, 468);
    
    _putDontNest(result, 310, 474);
    
    _putDontNest(result, 310, 481);
    
    _putDontNest(result, 312, 323);
    
    _putDontNest(result, 312, 329);
    
    _putDontNest(result, 312, 336);
    
    _putDontNest(result, 312, 342);
    
    _putDontNest(result, 312, 348);
    
    _putDontNest(result, 312, 354);
    
    _putDontNest(result, 312, 361);
    
    _putDontNest(result, 312, 367);
    
    _putDontNest(result, 312, 373);
    
    _putDontNest(result, 312, 379);
    
    _putDontNest(result, 312, 385);
    
    _putDontNest(result, 312, 391);
    
    _putDontNest(result, 312, 397);
    
    _putDontNest(result, 312, 407);
    
    _putDontNest(result, 312, 414);
    
    _putDontNest(result, 312, 420);
    
    _putDontNest(result, 312, 426);
    
    _putDontNest(result, 312, 432);
    
    _putDontNest(result, 312, 438);
    
    _putDontNest(result, 312, 444);
    
    _putDontNest(result, 312, 450);
    
    _putDontNest(result, 312, 456);
    
    _putDontNest(result, 312, 462);
    
    _putDontNest(result, 312, 468);
    
    _putDontNest(result, 312, 474);
    
    _putDontNest(result, 312, 481);
    
    _putDontNest(result, 316, 310);
    
    _putDontNest(result, 316, 316);
    
    _putDontNest(result, 316, 323);
    
    _putDontNest(result, 316, 329);
    
    _putDontNest(result, 316, 336);
    
    _putDontNest(result, 316, 342);
    
    _putDontNest(result, 316, 348);
    
    _putDontNest(result, 316, 354);
    
    _putDontNest(result, 316, 361);
    
    _putDontNest(result, 316, 367);
    
    _putDontNest(result, 316, 373);
    
    _putDontNest(result, 316, 379);
    
    _putDontNest(result, 316, 385);
    
    _putDontNest(result, 316, 391);
    
    _putDontNest(result, 316, 397);
    
    _putDontNest(result, 316, 407);
    
    _putDontNest(result, 316, 414);
    
    _putDontNest(result, 316, 420);
    
    _putDontNest(result, 316, 426);
    
    _putDontNest(result, 316, 432);
    
    _putDontNest(result, 316, 438);
    
    _putDontNest(result, 316, 444);
    
    _putDontNest(result, 316, 450);
    
    _putDontNest(result, 316, 456);
    
    _putDontNest(result, 316, 462);
    
    _putDontNest(result, 316, 468);
    
    _putDontNest(result, 316, 474);
    
    _putDontNest(result, 316, 481);
    
    _putDontNest(result, 319, 336);
    
    _putDontNest(result, 319, 342);
    
    _putDontNest(result, 319, 348);
    
    _putDontNest(result, 319, 354);
    
    _putDontNest(result, 319, 361);
    
    _putDontNest(result, 319, 367);
    
    _putDontNest(result, 319, 373);
    
    _putDontNest(result, 319, 379);
    
    _putDontNest(result, 319, 385);
    
    _putDontNest(result, 319, 391);
    
    _putDontNest(result, 319, 397);
    
    _putDontNest(result, 319, 407);
    
    _putDontNest(result, 319, 414);
    
    _putDontNest(result, 319, 420);
    
    _putDontNest(result, 319, 426);
    
    _putDontNest(result, 319, 432);
    
    _putDontNest(result, 319, 438);
    
    _putDontNest(result, 319, 444);
    
    _putDontNest(result, 319, 450);
    
    _putDontNest(result, 319, 456);
    
    _putDontNest(result, 319, 462);
    
    _putDontNest(result, 319, 468);
    
    _putDontNest(result, 319, 474);
    
    _putDontNest(result, 319, 481);
    
    _putDontNest(result, 323, 323);
    
    _putDontNest(result, 323, 329);
    
    _putDontNest(result, 323, 336);
    
    _putDontNest(result, 323, 342);
    
    _putDontNest(result, 323, 348);
    
    _putDontNest(result, 323, 354);
    
    _putDontNest(result, 323, 361);
    
    _putDontNest(result, 323, 367);
    
    _putDontNest(result, 323, 373);
    
    _putDontNest(result, 323, 379);
    
    _putDontNest(result, 323, 385);
    
    _putDontNest(result, 323, 391);
    
    _putDontNest(result, 323, 397);
    
    _putDontNest(result, 323, 407);
    
    _putDontNest(result, 323, 414);
    
    _putDontNest(result, 323, 420);
    
    _putDontNest(result, 323, 426);
    
    _putDontNest(result, 323, 432);
    
    _putDontNest(result, 323, 438);
    
    _putDontNest(result, 323, 444);
    
    _putDontNest(result, 323, 450);
    
    _putDontNest(result, 323, 456);
    
    _putDontNest(result, 323, 462);
    
    _putDontNest(result, 323, 468);
    
    _putDontNest(result, 323, 474);
    
    _putDontNest(result, 323, 481);
    
    _putDontNest(result, 325, 336);
    
    _putDontNest(result, 325, 342);
    
    _putDontNest(result, 325, 348);
    
    _putDontNest(result, 325, 354);
    
    _putDontNest(result, 325, 361);
    
    _putDontNest(result, 325, 367);
    
    _putDontNest(result, 325, 373);
    
    _putDontNest(result, 325, 379);
    
    _putDontNest(result, 325, 385);
    
    _putDontNest(result, 325, 391);
    
    _putDontNest(result, 325, 397);
    
    _putDontNest(result, 325, 407);
    
    _putDontNest(result, 325, 414);
    
    _putDontNest(result, 325, 420);
    
    _putDontNest(result, 325, 426);
    
    _putDontNest(result, 325, 432);
    
    _putDontNest(result, 325, 438);
    
    _putDontNest(result, 325, 444);
    
    _putDontNest(result, 325, 450);
    
    _putDontNest(result, 325, 456);
    
    _putDontNest(result, 325, 462);
    
    _putDontNest(result, 325, 468);
    
    _putDontNest(result, 325, 474);
    
    _putDontNest(result, 325, 481);
    
    _putDontNest(result, 329, 323);
    
    _putDontNest(result, 329, 329);
    
    _putDontNest(result, 329, 336);
    
    _putDontNest(result, 329, 342);
    
    _putDontNest(result, 329, 348);
    
    _putDontNest(result, 329, 354);
    
    _putDontNest(result, 329, 361);
    
    _putDontNest(result, 329, 367);
    
    _putDontNest(result, 329, 373);
    
    _putDontNest(result, 329, 379);
    
    _putDontNest(result, 329, 385);
    
    _putDontNest(result, 329, 391);
    
    _putDontNest(result, 329, 397);
    
    _putDontNest(result, 329, 407);
    
    _putDontNest(result, 329, 414);
    
    _putDontNest(result, 329, 420);
    
    _putDontNest(result, 329, 426);
    
    _putDontNest(result, 329, 432);
    
    _putDontNest(result, 329, 438);
    
    _putDontNest(result, 329, 444);
    
    _putDontNest(result, 329, 450);
    
    _putDontNest(result, 329, 456);
    
    _putDontNest(result, 329, 462);
    
    _putDontNest(result, 329, 468);
    
    _putDontNest(result, 329, 474);
    
    _putDontNest(result, 329, 481);
    
    _putDontNest(result, 332, 361);
    
    _putDontNest(result, 332, 367);
    
    _putDontNest(result, 332, 373);
    
    _putDontNest(result, 332, 379);
    
    _putDontNest(result, 332, 385);
    
    _putDontNest(result, 332, 391);
    
    _putDontNest(result, 332, 397);
    
    _putDontNest(result, 332, 407);
    
    _putDontNest(result, 332, 414);
    
    _putDontNest(result, 332, 420);
    
    _putDontNest(result, 332, 426);
    
    _putDontNest(result, 332, 432);
    
    _putDontNest(result, 332, 438);
    
    _putDontNest(result, 332, 444);
    
    _putDontNest(result, 332, 450);
    
    _putDontNest(result, 332, 456);
    
    _putDontNest(result, 332, 462);
    
    _putDontNest(result, 332, 468);
    
    _putDontNest(result, 332, 474);
    
    _putDontNest(result, 332, 481);
    
    _putDontNest(result, 336, 336);
    
    _putDontNest(result, 336, 342);
    
    _putDontNest(result, 336, 348);
    
    _putDontNest(result, 336, 354);
    
    _putDontNest(result, 336, 361);
    
    _putDontNest(result, 336, 367);
    
    _putDontNest(result, 336, 373);
    
    _putDontNest(result, 336, 379);
    
    _putDontNest(result, 336, 385);
    
    _putDontNest(result, 336, 391);
    
    _putDontNest(result, 336, 397);
    
    _putDontNest(result, 336, 407);
    
    _putDontNest(result, 336, 414);
    
    _putDontNest(result, 336, 420);
    
    _putDontNest(result, 336, 426);
    
    _putDontNest(result, 336, 432);
    
    _putDontNest(result, 336, 438);
    
    _putDontNest(result, 336, 444);
    
    _putDontNest(result, 336, 450);
    
    _putDontNest(result, 336, 456);
    
    _putDontNest(result, 336, 462);
    
    _putDontNest(result, 336, 468);
    
    _putDontNest(result, 336, 474);
    
    _putDontNest(result, 336, 481);
    
    _putDontNest(result, 338, 361);
    
    _putDontNest(result, 338, 367);
    
    _putDontNest(result, 338, 373);
    
    _putDontNest(result, 338, 379);
    
    _putDontNest(result, 338, 385);
    
    _putDontNest(result, 338, 391);
    
    _putDontNest(result, 338, 397);
    
    _putDontNest(result, 338, 407);
    
    _putDontNest(result, 338, 414);
    
    _putDontNest(result, 338, 420);
    
    _putDontNest(result, 338, 426);
    
    _putDontNest(result, 338, 432);
    
    _putDontNest(result, 338, 438);
    
    _putDontNest(result, 338, 444);
    
    _putDontNest(result, 338, 450);
    
    _putDontNest(result, 338, 456);
    
    _putDontNest(result, 338, 462);
    
    _putDontNest(result, 338, 468);
    
    _putDontNest(result, 338, 474);
    
    _putDontNest(result, 338, 481);
    
    _putDontNest(result, 342, 336);
    
    _putDontNest(result, 342, 342);
    
    _putDontNest(result, 342, 348);
    
    _putDontNest(result, 342, 354);
    
    _putDontNest(result, 342, 361);
    
    _putDontNest(result, 342, 367);
    
    _putDontNest(result, 342, 373);
    
    _putDontNest(result, 342, 379);
    
    _putDontNest(result, 342, 385);
    
    _putDontNest(result, 342, 391);
    
    _putDontNest(result, 342, 397);
    
    _putDontNest(result, 342, 407);
    
    _putDontNest(result, 342, 414);
    
    _putDontNest(result, 342, 420);
    
    _putDontNest(result, 342, 426);
    
    _putDontNest(result, 342, 432);
    
    _putDontNest(result, 342, 438);
    
    _putDontNest(result, 342, 444);
    
    _putDontNest(result, 342, 450);
    
    _putDontNest(result, 342, 456);
    
    _putDontNest(result, 342, 462);
    
    _putDontNest(result, 342, 468);
    
    _putDontNest(result, 342, 474);
    
    _putDontNest(result, 342, 481);
    
    _putDontNest(result, 344, 361);
    
    _putDontNest(result, 344, 367);
    
    _putDontNest(result, 344, 373);
    
    _putDontNest(result, 344, 379);
    
    _putDontNest(result, 344, 385);
    
    _putDontNest(result, 344, 391);
    
    _putDontNest(result, 344, 397);
    
    _putDontNest(result, 344, 407);
    
    _putDontNest(result, 344, 414);
    
    _putDontNest(result, 344, 420);
    
    _putDontNest(result, 344, 426);
    
    _putDontNest(result, 344, 432);
    
    _putDontNest(result, 344, 438);
    
    _putDontNest(result, 344, 444);
    
    _putDontNest(result, 344, 450);
    
    _putDontNest(result, 344, 456);
    
    _putDontNest(result, 344, 462);
    
    _putDontNest(result, 344, 468);
    
    _putDontNest(result, 344, 474);
    
    _putDontNest(result, 344, 481);
    
    _putDontNest(result, 348, 336);
    
    _putDontNest(result, 348, 342);
    
    _putDontNest(result, 348, 348);
    
    _putDontNest(result, 348, 354);
    
    _putDontNest(result, 348, 361);
    
    _putDontNest(result, 348, 367);
    
    _putDontNest(result, 348, 373);
    
    _putDontNest(result, 348, 379);
    
    _putDontNest(result, 348, 385);
    
    _putDontNest(result, 348, 391);
    
    _putDontNest(result, 348, 397);
    
    _putDontNest(result, 348, 407);
    
    _putDontNest(result, 348, 414);
    
    _putDontNest(result, 348, 420);
    
    _putDontNest(result, 348, 426);
    
    _putDontNest(result, 348, 432);
    
    _putDontNest(result, 348, 438);
    
    _putDontNest(result, 348, 444);
    
    _putDontNest(result, 348, 450);
    
    _putDontNest(result, 348, 456);
    
    _putDontNest(result, 348, 462);
    
    _putDontNest(result, 348, 468);
    
    _putDontNest(result, 348, 474);
    
    _putDontNest(result, 348, 481);
    
    _putDontNest(result, 350, 361);
    
    _putDontNest(result, 350, 367);
    
    _putDontNest(result, 350, 373);
    
    _putDontNest(result, 350, 379);
    
    _putDontNest(result, 350, 385);
    
    _putDontNest(result, 350, 391);
    
    _putDontNest(result, 350, 397);
    
    _putDontNest(result, 350, 407);
    
    _putDontNest(result, 350, 414);
    
    _putDontNest(result, 350, 420);
    
    _putDontNest(result, 350, 426);
    
    _putDontNest(result, 350, 432);
    
    _putDontNest(result, 350, 438);
    
    _putDontNest(result, 350, 444);
    
    _putDontNest(result, 350, 450);
    
    _putDontNest(result, 350, 456);
    
    _putDontNest(result, 350, 462);
    
    _putDontNest(result, 350, 468);
    
    _putDontNest(result, 350, 474);
    
    _putDontNest(result, 350, 481);
    
    _putDontNest(result, 354, 336);
    
    _putDontNest(result, 354, 342);
    
    _putDontNest(result, 354, 348);
    
    _putDontNest(result, 354, 354);
    
    _putDontNest(result, 354, 361);
    
    _putDontNest(result, 354, 367);
    
    _putDontNest(result, 354, 373);
    
    _putDontNest(result, 354, 379);
    
    _putDontNest(result, 354, 385);
    
    _putDontNest(result, 354, 391);
    
    _putDontNest(result, 354, 397);
    
    _putDontNest(result, 354, 407);
    
    _putDontNest(result, 354, 414);
    
    _putDontNest(result, 354, 420);
    
    _putDontNest(result, 354, 426);
    
    _putDontNest(result, 354, 432);
    
    _putDontNest(result, 354, 438);
    
    _putDontNest(result, 354, 444);
    
    _putDontNest(result, 354, 450);
    
    _putDontNest(result, 354, 456);
    
    _putDontNest(result, 354, 462);
    
    _putDontNest(result, 354, 468);
    
    _putDontNest(result, 354, 474);
    
    _putDontNest(result, 354, 481);
    
    _putDontNest(result, 357, 373);
    
    _putDontNest(result, 357, 379);
    
    _putDontNest(result, 357, 385);
    
    _putDontNest(result, 357, 391);
    
    _putDontNest(result, 357, 397);
    
    _putDontNest(result, 357, 407);
    
    _putDontNest(result, 357, 414);
    
    _putDontNest(result, 357, 420);
    
    _putDontNest(result, 357, 426);
    
    _putDontNest(result, 357, 432);
    
    _putDontNest(result, 357, 438);
    
    _putDontNest(result, 357, 444);
    
    _putDontNest(result, 357, 450);
    
    _putDontNest(result, 357, 456);
    
    _putDontNest(result, 357, 462);
    
    _putDontNest(result, 357, 468);
    
    _putDontNest(result, 357, 474);
    
    _putDontNest(result, 357, 481);
    
    _putDontNest(result, 361, 361);
    
    _putDontNest(result, 361, 367);
    
    _putDontNest(result, 361, 373);
    
    _putDontNest(result, 361, 379);
    
    _putDontNest(result, 361, 385);
    
    _putDontNest(result, 361, 391);
    
    _putDontNest(result, 361, 397);
    
    _putDontNest(result, 361, 407);
    
    _putDontNest(result, 361, 414);
    
    _putDontNest(result, 361, 420);
    
    _putDontNest(result, 361, 426);
    
    _putDontNest(result, 361, 432);
    
    _putDontNest(result, 361, 438);
    
    _putDontNest(result, 361, 444);
    
    _putDontNest(result, 361, 450);
    
    _putDontNest(result, 361, 456);
    
    _putDontNest(result, 361, 462);
    
    _putDontNest(result, 361, 468);
    
    _putDontNest(result, 361, 474);
    
    _putDontNest(result, 361, 481);
    
    _putDontNest(result, 363, 373);
    
    _putDontNest(result, 363, 379);
    
    _putDontNest(result, 363, 385);
    
    _putDontNest(result, 363, 391);
    
    _putDontNest(result, 363, 397);
    
    _putDontNest(result, 363, 407);
    
    _putDontNest(result, 363, 414);
    
    _putDontNest(result, 363, 420);
    
    _putDontNest(result, 363, 426);
    
    _putDontNest(result, 363, 432);
    
    _putDontNest(result, 363, 438);
    
    _putDontNest(result, 363, 444);
    
    _putDontNest(result, 363, 450);
    
    _putDontNest(result, 363, 456);
    
    _putDontNest(result, 363, 462);
    
    _putDontNest(result, 363, 468);
    
    _putDontNest(result, 363, 474);
    
    _putDontNest(result, 363, 481);
    
    _putDontNest(result, 367, 361);
    
    _putDontNest(result, 367, 367);
    
    _putDontNest(result, 367, 373);
    
    _putDontNest(result, 367, 379);
    
    _putDontNest(result, 367, 385);
    
    _putDontNest(result, 367, 391);
    
    _putDontNest(result, 367, 397);
    
    _putDontNest(result, 367, 407);
    
    _putDontNest(result, 367, 414);
    
    _putDontNest(result, 367, 420);
    
    _putDontNest(result, 367, 426);
    
    _putDontNest(result, 367, 432);
    
    _putDontNest(result, 367, 438);
    
    _putDontNest(result, 367, 444);
    
    _putDontNest(result, 367, 450);
    
    _putDontNest(result, 367, 456);
    
    _putDontNest(result, 367, 462);
    
    _putDontNest(result, 367, 468);
    
    _putDontNest(result, 367, 474);
    
    _putDontNest(result, 367, 481);
    
    _putDontNest(result, 369, 379);
    
    _putDontNest(result, 369, 385);
    
    _putDontNest(result, 369, 391);
    
    _putDontNest(result, 369, 397);
    
    _putDontNest(result, 369, 407);
    
    _putDontNest(result, 369, 414);
    
    _putDontNest(result, 369, 420);
    
    _putDontNest(result, 369, 426);
    
    _putDontNest(result, 369, 432);
    
    _putDontNest(result, 369, 438);
    
    _putDontNest(result, 369, 444);
    
    _putDontNest(result, 369, 450);
    
    _putDontNest(result, 369, 456);
    
    _putDontNest(result, 369, 462);
    
    _putDontNest(result, 369, 468);
    
    _putDontNest(result, 369, 474);
    
    _putDontNest(result, 369, 481);
    
    _putDontNest(result, 373, 373);
    
    _putDontNest(result, 373, 379);
    
    _putDontNest(result, 373, 385);
    
    _putDontNest(result, 373, 391);
    
    _putDontNest(result, 373, 397);
    
    _putDontNest(result, 373, 407);
    
    _putDontNest(result, 373, 414);
    
    _putDontNest(result, 373, 420);
    
    _putDontNest(result, 373, 426);
    
    _putDontNest(result, 373, 432);
    
    _putDontNest(result, 373, 438);
    
    _putDontNest(result, 373, 444);
    
    _putDontNest(result, 373, 450);
    
    _putDontNest(result, 373, 456);
    
    _putDontNest(result, 373, 462);
    
    _putDontNest(result, 373, 468);
    
    _putDontNest(result, 373, 474);
    
    _putDontNest(result, 373, 481);
    
    _putDontNest(result, 375, 385);
    
    _putDontNest(result, 375, 391);
    
    _putDontNest(result, 375, 397);
    
    _putDontNest(result, 375, 407);
    
    _putDontNest(result, 375, 414);
    
    _putDontNest(result, 375, 420);
    
    _putDontNest(result, 375, 426);
    
    _putDontNest(result, 375, 432);
    
    _putDontNest(result, 375, 438);
    
    _putDontNest(result, 375, 444);
    
    _putDontNest(result, 375, 450);
    
    _putDontNest(result, 375, 456);
    
    _putDontNest(result, 375, 462);
    
    _putDontNest(result, 375, 468);
    
    _putDontNest(result, 375, 474);
    
    _putDontNest(result, 375, 481);
    
    _putDontNest(result, 379, 379);
    
    _putDontNest(result, 379, 385);
    
    _putDontNest(result, 379, 391);
    
    _putDontNest(result, 379, 397);
    
    _putDontNest(result, 379, 407);
    
    _putDontNest(result, 379, 414);
    
    _putDontNest(result, 379, 420);
    
    _putDontNest(result, 379, 426);
    
    _putDontNest(result, 379, 432);
    
    _putDontNest(result, 379, 438);
    
    _putDontNest(result, 379, 444);
    
    _putDontNest(result, 379, 450);
    
    _putDontNest(result, 379, 456);
    
    _putDontNest(result, 379, 462);
    
    _putDontNest(result, 379, 468);
    
    _putDontNest(result, 379, 474);
    
    _putDontNest(result, 379, 481);
    
    _putDontNest(result, 381, 391);
    
    _putDontNest(result, 381, 397);
    
    _putDontNest(result, 381, 407);
    
    _putDontNest(result, 381, 414);
    
    _putDontNest(result, 381, 420);
    
    _putDontNest(result, 381, 426);
    
    _putDontNest(result, 381, 432);
    
    _putDontNest(result, 381, 438);
    
    _putDontNest(result, 381, 444);
    
    _putDontNest(result, 381, 450);
    
    _putDontNest(result, 381, 456);
    
    _putDontNest(result, 381, 462);
    
    _putDontNest(result, 381, 468);
    
    _putDontNest(result, 381, 474);
    
    _putDontNest(result, 381, 481);
    
    _putDontNest(result, 385, 385);
    
    _putDontNest(result, 385, 391);
    
    _putDontNest(result, 385, 397);
    
    _putDontNest(result, 385, 407);
    
    _putDontNest(result, 385, 414);
    
    _putDontNest(result, 385, 420);
    
    _putDontNest(result, 385, 426);
    
    _putDontNest(result, 385, 432);
    
    _putDontNest(result, 385, 438);
    
    _putDontNest(result, 385, 444);
    
    _putDontNest(result, 385, 450);
    
    _putDontNest(result, 385, 456);
    
    _putDontNest(result, 385, 462);
    
    _putDontNest(result, 385, 468);
    
    _putDontNest(result, 385, 474);
    
    _putDontNest(result, 385, 481);
    
    _putDontNest(result, 387, 397);
    
    _putDontNest(result, 387, 407);
    
    _putDontNest(result, 387, 414);
    
    _putDontNest(result, 387, 420);
    
    _putDontNest(result, 387, 426);
    
    _putDontNest(result, 387, 432);
    
    _putDontNest(result, 387, 438);
    
    _putDontNest(result, 387, 444);
    
    _putDontNest(result, 387, 450);
    
    _putDontNest(result, 387, 456);
    
    _putDontNest(result, 387, 462);
    
    _putDontNest(result, 387, 468);
    
    _putDontNest(result, 387, 474);
    
    _putDontNest(result, 387, 481);
    
    _putDontNest(result, 391, 391);
    
    _putDontNest(result, 391, 397);
    
    _putDontNest(result, 391, 407);
    
    _putDontNest(result, 391, 414);
    
    _putDontNest(result, 391, 420);
    
    _putDontNest(result, 391, 426);
    
    _putDontNest(result, 391, 432);
    
    _putDontNest(result, 391, 438);
    
    _putDontNest(result, 391, 444);
    
    _putDontNest(result, 391, 450);
    
    _putDontNest(result, 391, 456);
    
    _putDontNest(result, 391, 462);
    
    _putDontNest(result, 391, 468);
    
    _putDontNest(result, 391, 474);
    
    _putDontNest(result, 391, 481);
    
    _putDontNest(result, 393, 407);
    
    _putDontNest(result, 393, 414);
    
    _putDontNest(result, 393, 420);
    
    _putDontNest(result, 393, 426);
    
    _putDontNest(result, 393, 432);
    
    _putDontNest(result, 393, 438);
    
    _putDontNest(result, 393, 444);
    
    _putDontNest(result, 393, 450);
    
    _putDontNest(result, 393, 456);
    
    _putDontNest(result, 393, 462);
    
    _putDontNest(result, 393, 468);
    
    _putDontNest(result, 393, 474);
    
    _putDontNest(result, 393, 481);
    
    _putDontNest(result, 397, 397);
    
    _putDontNest(result, 397, 407);
    
    _putDontNest(result, 397, 414);
    
    _putDontNest(result, 397, 420);
    
    _putDontNest(result, 397, 426);
    
    _putDontNest(result, 397, 432);
    
    _putDontNest(result, 397, 438);
    
    _putDontNest(result, 397, 444);
    
    _putDontNest(result, 397, 450);
    
    _putDontNest(result, 397, 456);
    
    _putDontNest(result, 397, 462);
    
    _putDontNest(result, 397, 468);
    
    _putDontNest(result, 397, 474);
    
    _putDontNest(result, 397, 481);
    
    _putDontNest(result, 399, 407);
    
    _putDontNest(result, 399, 414);
    
    _putDontNest(result, 399, 420);
    
    _putDontNest(result, 399, 426);
    
    _putDontNest(result, 399, 432);
    
    _putDontNest(result, 399, 438);
    
    _putDontNest(result, 399, 444);
    
    _putDontNest(result, 399, 450);
    
    _putDontNest(result, 399, 456);
    
    _putDontNest(result, 399, 462);
    
    _putDontNest(result, 399, 468);
    
    _putDontNest(result, 399, 474);
    
    _putDontNest(result, 399, 481);
    
    _putDontNest(result, 407, 414);
    
    _putDontNest(result, 407, 420);
    
    _putDontNest(result, 407, 426);
    
    _putDontNest(result, 407, 432);
    
    _putDontNest(result, 407, 438);
    
    _putDontNest(result, 407, 444);
    
    _putDontNest(result, 407, 450);
    
    _putDontNest(result, 407, 456);
    
    _putDontNest(result, 407, 462);
    
    _putDontNest(result, 407, 468);
    
    _putDontNest(result, 407, 474);
    
    _putDontNest(result, 407, 481);
    
    _putDontNest(result, 410, 414);
    
    _putDontNest(result, 410, 420);
    
    _putDontNest(result, 410, 426);
    
    _putDontNest(result, 410, 432);
    
    _putDontNest(result, 410, 438);
    
    _putDontNest(result, 410, 444);
    
    _putDontNest(result, 410, 450);
    
    _putDontNest(result, 410, 456);
    
    _putDontNest(result, 410, 462);
    
    _putDontNest(result, 410, 468);
    
    _putDontNest(result, 410, 474);
    
    _putDontNest(result, 410, 481);
    
    _putDontNest(result, 414, 481);
    
    _putDontNest(result, 416, 414);
    
    _putDontNest(result, 416, 420);
    
    _putDontNest(result, 416, 426);
    
    _putDontNest(result, 416, 432);
    
    _putDontNest(result, 416, 438);
    
    _putDontNest(result, 416, 444);
    
    _putDontNest(result, 416, 450);
    
    _putDontNest(result, 416, 456);
    
    _putDontNest(result, 416, 462);
    
    _putDontNest(result, 416, 468);
    
    _putDontNest(result, 416, 474);
    
    _putDontNest(result, 416, 481);
    
    _putDontNest(result, 420, 481);
    
    _putDontNest(result, 422, 414);
    
    _putDontNest(result, 422, 420);
    
    _putDontNest(result, 422, 426);
    
    _putDontNest(result, 422, 432);
    
    _putDontNest(result, 422, 438);
    
    _putDontNest(result, 422, 444);
    
    _putDontNest(result, 422, 450);
    
    _putDontNest(result, 422, 456);
    
    _putDontNest(result, 422, 462);
    
    _putDontNest(result, 422, 468);
    
    _putDontNest(result, 422, 474);
    
    _putDontNest(result, 422, 481);
    
    _putDontNest(result, 426, 481);
    
    _putDontNest(result, 428, 414);
    
    _putDontNest(result, 428, 420);
    
    _putDontNest(result, 428, 426);
    
    _putDontNest(result, 428, 432);
    
    _putDontNest(result, 428, 438);
    
    _putDontNest(result, 428, 444);
    
    _putDontNest(result, 428, 450);
    
    _putDontNest(result, 428, 456);
    
    _putDontNest(result, 428, 462);
    
    _putDontNest(result, 428, 468);
    
    _putDontNest(result, 428, 474);
    
    _putDontNest(result, 428, 481);
    
    _putDontNest(result, 432, 481);
    
    _putDontNest(result, 434, 414);
    
    _putDontNest(result, 434, 420);
    
    _putDontNest(result, 434, 426);
    
    _putDontNest(result, 434, 432);
    
    _putDontNest(result, 434, 438);
    
    _putDontNest(result, 434, 444);
    
    _putDontNest(result, 434, 450);
    
    _putDontNest(result, 434, 456);
    
    _putDontNest(result, 434, 462);
    
    _putDontNest(result, 434, 468);
    
    _putDontNest(result, 434, 474);
    
    _putDontNest(result, 434, 481);
    
    _putDontNest(result, 438, 481);
    
    _putDontNest(result, 440, 414);
    
    _putDontNest(result, 440, 420);
    
    _putDontNest(result, 440, 426);
    
    _putDontNest(result, 440, 432);
    
    _putDontNest(result, 440, 438);
    
    _putDontNest(result, 440, 444);
    
    _putDontNest(result, 440, 450);
    
    _putDontNest(result, 440, 456);
    
    _putDontNest(result, 440, 462);
    
    _putDontNest(result, 440, 468);
    
    _putDontNest(result, 440, 474);
    
    _putDontNest(result, 440, 481);
    
    _putDontNest(result, 444, 481);
    
    _putDontNest(result, 446, 414);
    
    _putDontNest(result, 446, 420);
    
    _putDontNest(result, 446, 426);
    
    _putDontNest(result, 446, 432);
    
    _putDontNest(result, 446, 438);
    
    _putDontNest(result, 446, 444);
    
    _putDontNest(result, 446, 450);
    
    _putDontNest(result, 446, 456);
    
    _putDontNest(result, 446, 462);
    
    _putDontNest(result, 446, 468);
    
    _putDontNest(result, 446, 474);
    
    _putDontNest(result, 446, 481);
    
    _putDontNest(result, 450, 481);
    
    _putDontNest(result, 452, 414);
    
    _putDontNest(result, 452, 420);
    
    _putDontNest(result, 452, 426);
    
    _putDontNest(result, 452, 432);
    
    _putDontNest(result, 452, 438);
    
    _putDontNest(result, 452, 444);
    
    _putDontNest(result, 452, 450);
    
    _putDontNest(result, 452, 456);
    
    _putDontNest(result, 452, 462);
    
    _putDontNest(result, 452, 468);
    
    _putDontNest(result, 452, 474);
    
    _putDontNest(result, 452, 481);
    
    _putDontNest(result, 456, 481);
    
    _putDontNest(result, 458, 414);
    
    _putDontNest(result, 458, 420);
    
    _putDontNest(result, 458, 426);
    
    _putDontNest(result, 458, 432);
    
    _putDontNest(result, 458, 438);
    
    _putDontNest(result, 458, 444);
    
    _putDontNest(result, 458, 450);
    
    _putDontNest(result, 458, 456);
    
    _putDontNest(result, 458, 462);
    
    _putDontNest(result, 458, 468);
    
    _putDontNest(result, 458, 474);
    
    _putDontNest(result, 458, 481);
    
    _putDontNest(result, 462, 481);
    
    _putDontNest(result, 464, 414);
    
    _putDontNest(result, 464, 420);
    
    _putDontNest(result, 464, 426);
    
    _putDontNest(result, 464, 432);
    
    _putDontNest(result, 464, 438);
    
    _putDontNest(result, 464, 444);
    
    _putDontNest(result, 464, 450);
    
    _putDontNest(result, 464, 456);
    
    _putDontNest(result, 464, 462);
    
    _putDontNest(result, 464, 468);
    
    _putDontNest(result, 464, 474);
    
    _putDontNest(result, 464, 481);
    
    _putDontNest(result, 468, 481);
    
    _putDontNest(result, 470, 414);
    
    _putDontNest(result, 470, 420);
    
    _putDontNest(result, 470, 426);
    
    _putDontNest(result, 470, 432);
    
    _putDontNest(result, 470, 438);
    
    _putDontNest(result, 470, 444);
    
    _putDontNest(result, 470, 450);
    
    _putDontNest(result, 470, 456);
    
    _putDontNest(result, 470, 462);
    
    _putDontNest(result, 470, 468);
    
    _putDontNest(result, 470, 474);
    
    _putDontNest(result, 470, 481);
    
    _putDontNest(result, 474, 481);
    
    _putDontNest(result, 481, 481);
    
    _putDontNest(result, 1191, 1225);
    
    _putDontNest(result, 1206, 1225);
    
    _putDontNest(result, 1441, 1475);
    
    _putDontNest(result, 1453, 1475);
    
    _putDontNest(result, 1495, 1534);
    
    _putDontNest(result, 1507, 1534);
   return result;
  }
    
  protected static IntegerMap _initDontNestGroups() {
    IntegerMap result = new IntegerMap();
    int resultStoreId = result.size();
    
    
    ++resultStoreId;
    
    result.putUnsafe(407, resultStoreId);
    result.putUnsafe(410, resultStoreId);
    result.putUnsafe(416, resultStoreId);
    result.putUnsafe(422, resultStoreId);
    result.putUnsafe(428, resultStoreId);
    result.putUnsafe(434, resultStoreId);
    result.putUnsafe(440, resultStoreId);
    result.putUnsafe(446, resultStoreId);
    result.putUnsafe(452, resultStoreId);
    result.putUnsafe(458, resultStoreId);
    result.putUnsafe(464, resultStoreId);
    result.putUnsafe(470, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1441, resultStoreId);
    result.putUnsafe(1453, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(393, resultStoreId);
    result.putUnsafe(399, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1191, resultStoreId);
    result.putUnsafe(1206, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(1495, resultStoreId);
    result.putUnsafe(1507, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(387, resultStoreId);
    result.putUnsafe(397, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(369, resultStoreId);
    result.putUnsafe(379, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(332, resultStoreId);
    result.putUnsafe(338, resultStoreId);
    result.putUnsafe(344, resultStoreId);
    result.putUnsafe(350, resultStoreId);
    result.putUnsafe(361, resultStoreId);
    result.putUnsafe(367, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(414, resultStoreId);
    result.putUnsafe(420, resultStoreId);
    result.putUnsafe(426, resultStoreId);
    result.putUnsafe(432, resultStoreId);
    result.putUnsafe(438, resultStoreId);
    result.putUnsafe(444, resultStoreId);
    result.putUnsafe(450, resultStoreId);
    result.putUnsafe(456, resultStoreId);
    result.putUnsafe(462, resultStoreId);
    result.putUnsafe(468, resultStoreId);
    result.putUnsafe(474, resultStoreId);
    result.putUnsafe(481, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(381, resultStoreId);
    result.putUnsafe(391, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(375, resultStoreId);
    result.putUnsafe(385, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(357, resultStoreId);
    result.putUnsafe(363, resultStoreId);
    result.putUnsafe(373, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(319, resultStoreId);
    result.putUnsafe(325, resultStoreId);
    result.putUnsafe(336, resultStoreId);
    result.putUnsafe(342, resultStoreId);
    result.putUnsafe(348, resultStoreId);
    result.putUnsafe(354, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(306, resultStoreId);
    result.putUnsafe(312, resultStoreId);
    result.putUnsafe(323, resultStoreId);
    result.putUnsafe(329, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(284, resultStoreId);
    result.putUnsafe(290, resultStoreId);
    result.putUnsafe(297, resultStoreId);
    result.putUnsafe(310, resultStoreId);
    result.putUnsafe(316, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(235, resultStoreId);
    result.putUnsafe(240, resultStoreId);
    result.putUnsafe(247, resultStoreId);
    result.putUnsafe(253, resultStoreId);
    result.putUnsafe(257, resultStoreId);
    result.putUnsafe(261, resultStoreId);
    result.putUnsafe(265, resultStoreId);
    result.putUnsafe(273, resultStoreId);
    result.putUnsafe(277, resultStoreId);
    result.putUnsafe(281, resultStoreId);
    result.putUnsafe(288, resultStoreId);
    result.putUnsafe(294, resultStoreId);
    result.putUnsafe(302, resultStoreId);
    ++resultStoreId;
    
    result.putUnsafe(166, resultStoreId);
    result.putUnsafe(177, resultStoreId);
    result.putUnsafe(189, resultStoreId);
    result.putUnsafe(195, resultStoreId);
    result.putUnsafe(203, resultStoreId);
    result.putUnsafe(219, resultStoreId);
      
    return result;
  }
  
  protected boolean hasNestingRestrictions(java.lang.String name){
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
  private static final IConstructor prod__Keyword__lit_default_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"default\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_unsigned_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"unsigned\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_float_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"float\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_union_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"union\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_volatile_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"volatile\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_double_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"double\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_static_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"static\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_sizeof_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"sizeof\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_long_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"long\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_case_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"case\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_while_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"while\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_signed_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"signed\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_switch_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"switch\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_int_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"int\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_register_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"register\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_struct_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"struct\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_const_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"const\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_typedef_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"typedef\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_extern_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"extern\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_do_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"do\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_else_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"else\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_goto_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"goto\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_break_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"break\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_short_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"short\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_continue_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"continue\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_for_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"for\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_void_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_char_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"char\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_auto_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"auto\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_enum_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"enum\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_if_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"if\")],{})", Factory.Production);
  private static final IConstructor prod__Keyword__lit_return_ = (IConstructor) _read("prod(keywords(\"Keyword\"),[lit(\"return\")],{})", Factory.Production);
  private static final IConstructor prod__$MetaHole_char_class___range__76_76__char_class___range__0_0_lit___111_112_116_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_55_54_44_55_54_41_93_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__char_class___range__76_76 = (IConstructor) _read("prod(label(\"$MetaHole\",\\char-class([range(76,76)])),[\\char-class([range(0,0)]),lit(\"opt(\\\\char-class([range(76,76)]))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(opt(\\char-class([range(76,76)]))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_char_class___range__70_70_range__76_76_range__102_102_range__108_108__char_class___range__0_0_lit___111_112_116_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_55_48_44_55_48_41_44_114_97_110_103_101_40_55_54_44_55_54_41_44_114_97_110_103_101_40_49_48_50_44_49_48_50_41_44_114_97_110_103_101_40_49_48_56_44_49_48_56_41_93_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108 = (IConstructor) _read("prod(label(\"$MetaHole\",\\char-class([range(70,70),range(76,76),range(102,102),range(108,108)])),[\\char-class([range(0,0)]),lit(\"opt(\\\\char-class([range(70,70),range(76,76),range(102,102),range(108,108)]))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(opt(\\char-class([range(70,70),range(76,76),range(102,102),range(108,108)]))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_char_class___range__1_9_range__11_16777215__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_49_44_57_41_44_114_97_110_103_101_40_49_49_44_49_54_55_55_55_50_49_53_41_93_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__char_class___range__1_9_range__11_16777215 = (IConstructor) _read("prod(label(\"$MetaHole\",\\char-class([range(1,9),range(11,16777215)])),[\\char-class([range(0,0)]),lit(\"\\\\iter-star(\\\\char-class([range(1,9),range(11,16777215)]))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-star(\\char-class([range(1,9),range(11,16777215)]))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_char_class___range__43_43_range__45_45__char_class___range__0_0_lit___111_112_116_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_52_51_44_52_51_41_44_114_97_110_103_101_40_52_53_44_52_53_41_93_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__char_class___range__43_43_range__45_45 = (IConstructor) _read("prod(label(\"$MetaHole\",\\char-class([range(43,43),range(45,45)])),[\\char-class([range(0,0)]),lit(\"opt(\\\\char-class([range(43,43),range(45,45)]))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(opt(\\char-class([range(43,43),range(45,45)]))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_char_class___range__48_57__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_52_56_44_53_55_41_93_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__char_class___range__48_57 = (IConstructor) _read("prod(label(\"$MetaHole\",\\char-class([range(48,57)])),[\\char-class([range(0,0)]),lit(\"\\\\iter-star(\\\\char-class([range(48,57)]))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-star(\\char-class([range(48,57)]))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_char_class___range__76_76_range__85_85_range__108_108_range__117_117__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_55_54_44_55_54_41_44_114_97_110_103_101_40_56_53_44_56_53_41_44_114_97_110_103_101_40_49_48_56_44_49_48_56_41_44_114_97_110_103_101_40_49_49_55_44_49_49_55_41_93_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__char_class___range__76_76_range__85_85_range__108_108_range__117_117 = (IConstructor) _read("prod(label(\"$MetaHole\",\\char-class([range(76,76),range(85,85),range(108,108),range(117,117)])),[\\char-class([range(0,0)]),lit(\"\\\\iter-star(\\\\char-class([range(76,76),range(85,85),range(108,108),range(117,117)]))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-star(\\char-class([range(76,76),range(85,85),range(108,108),range(117,117)]))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_char_class___range__48_57__char_class___range__0_0_lit___105_116_101_114_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_52_56_44_53_55_41_93_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter__char_class___range__48_57 = (IConstructor) _read("prod(label(\"$MetaHole\",\\char-class([range(48,57)])),[\\char-class([range(0,0)]),lit(\"iter(\\\\char-class([range(48,57)]))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(iter(\\char-class([range(48,57)]))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_char_class___range__48_57_range__65_70_range__97_102__char_class___range__0_0_lit___105_116_101_114_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_52_56_44_53_55_41_44_114_97_110_103_101_40_54_53_44_55_48_41_44_114_97_110_103_101_40_57_55_44_49_48_50_41_93_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter__char_class___range__48_57_range__65_70_range__97_102 = (IConstructor) _read("prod(label(\"$MetaHole\",\\char-class([range(48,57),range(65,70),range(97,102)])),[\\char-class([range(0,0)]),lit(\"iter(\\\\char-class([range(48,57),range(65,70),range(97,102)]))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(iter(\\char-class([range(48,57),range(65,70),range(97,102)]))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_char_class___range__48_57_range__65_90_range__95_95_range__97_122__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_52_56_44_53_55_41_44_114_97_110_103_101_40_54_53_44_57_48_41_44_114_97_110_103_101_40_57_53_44_57_53_41_44_114_97_110_103_101_40_57_55_44_49_50_50_41_93_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("prod(label(\"$MetaHole\",\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),[\\char-class([range(0,0)]),lit(\"\\\\iter-star(\\\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Asterisk__char_class___range__0_0_lit___115_111_114_116_40_34_65_115_116_101_114_105_115_107_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Asterisk = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"Asterisk\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Asterisk\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(lex(\"Asterisk\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_CharacterConstant__char_class___range__0_0_lit___115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__CharacterConstant = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"CharacterConstant\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"CharacterConstant\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(lex(\"CharacterConstant\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_CharacterConstantContent__char_class___range__0_0_lit___105_116_101_114_40_115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter__CharacterConstantContent = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"CharacterConstantContent\")),[\\char-class([range(0,0)]),lit(\"iter(sort(\\\"CharacterConstantContent\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(iter(lex(\"CharacterConstantContent\"))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_CharacterConstantContent__char_class___range__0_0_lit___115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__CharacterConstantContent = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"CharacterConstantContent\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"CharacterConstantContent\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(lex(\"CharacterConstantContent\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Comment__char_class___range__0_0_lit___115_111_114_116_40_34_67_111_109_109_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Comment = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"Comment\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Comment\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(lex(\"Comment\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Exponent__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_69_120_112_111_110_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Exponent = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"Exponent\")),[\\char-class([range(0,0)]),lit(\"opt(sort(\\\"Exponent\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(opt(lex(\"Exponent\"))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Exponent__char_class___range__0_0_lit___115_111_114_116_40_34_69_120_112_111_110_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Exponent = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"Exponent\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Exponent\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(lex(\"Exponent\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_FloatingPointConstant__char_class___range__0_0_lit___115_111_114_116_40_34_70_108_111_97_116_105_110_103_80_111_105_110_116_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__FloatingPointConstant = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"FloatingPointConstant\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"FloatingPointConstant\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(lex(\"FloatingPointConstant\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_HexadecimalConstant__char_class___range__0_0_lit___115_111_114_116_40_34_72_101_120_97_100_101_99_105_109_97_108_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__HexadecimalConstant = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"HexadecimalConstant\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"HexadecimalConstant\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(lex(\"HexadecimalConstant\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Identifier__char_class___range__0_0_lit___115_111_114_116_40_34_73_100_101_110_116_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Identifier = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"Identifier\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Identifier\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(lex(\"Identifier\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_IntegerConstant__char_class___range__0_0_lit___115_111_114_116_40_34_73_110_116_101_103_101_114_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__IntegerConstant = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"IntegerConstant\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"IntegerConstant\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(lex(\"IntegerConstant\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_LAYOUT__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_76_65_89_79_85_84_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__LAYOUT = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"LAYOUT\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-star(sort(\\\"LAYOUT\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-star(lex(\"LAYOUT\"))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_LAYOUT__char_class___range__0_0_lit___115_111_114_116_40_34_76_65_89_79_85_84_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__LAYOUT = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"LAYOUT\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"LAYOUT\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(lex(\"LAYOUT\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_MultiLineCommentBodyToken__char_class___range__0_0_lit___115_111_114_116_40_34_77_117_108_116_105_76_105_110_101_67_111_109_109_101_110_116_66_111_100_121_84_111_107_101_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__MultiLineCommentBodyToken = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"MultiLineCommentBodyToken\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"MultiLineCommentBodyToken\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(lex(\"MultiLineCommentBodyToken\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_MultiLineCommentBodyToken__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_77_117_108_116_105_76_105_110_101_67_111_109_109_101_110_116_66_111_100_121_84_111_107_101_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__MultiLineCommentBodyToken = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"MultiLineCommentBodyToken\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-star(sort(\\\"MultiLineCommentBodyToken\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-star(lex(\"MultiLineCommentBodyToken\"))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_StringConstant__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StringConstant = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"StringConstant\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"StringConstant\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(lex(\"StringConstant\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_StringConstantContent__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__StringConstantContent = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"StringConstantContent\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-star(sort(\\\"StringConstantContent\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-star(lex(\"StringConstantContent\"))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_StringConstantContent__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StringConstantContent = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"StringConstantContent\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"StringConstantContent\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(lex(\"StringConstantContent\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_lit___44__char_class___range__0_0_lit___111_112_116_40_108_105_116_40_34_44_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__lit___44 = (IConstructor) _read("prod(label(\"$MetaHole\",lit(\",\")),[\\char-class([range(0,0)]),lit(\"opt(lit(\\\",\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(opt(lit(\",\"))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122__char_class___range__0_0_lit___115_101_113_40_91_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_54_53_44_57_48_41_44_114_97_110_103_101_40_57_53_44_57_53_41_44_114_97_110_103_101_40_57_55_44_49_50_50_41_93_41_44_99_111_110_100_105_116_105_111_110_97_108_40_92_105_116_101_114_45_115_116_97_114_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_52_56_44_53_55_41_44_114_97_110_103_101_40_54_53_44_57_48_41_44_114_97_110_103_101_40_57_53_44_57_53_41_44_114_97_110_103_101_40_57_55_44_49_50_50_41_93_41_41_44_123_92_110_111_116_45_102_111_108_108_111_119_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_52_56_44_53_55_41_44_114_97_110_103_101_40_54_53_44_57_48_41_44_114_97_110_103_101_40_57_53_44_57_53_41_44_114_97_110_103_101_40_57_55_44_49_50_50_41_93_41_41_125_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("prod(label(\"$MetaHole\",seq([\\char-class([range(65,90),range(95,95),range(97,122)]),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})])),[\\char-class([range(0,0)]),lit(\"seq([\\\\char-class([range(65,90),range(95,95),range(97,122)]),conditional(\\\\iter-star(\\\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\\\not-follow(\\\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})])\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(seq([\\char-class([range(65,90),range(95,95),range(97,122)]),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})])))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_AbstractDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_65_98_115_116_114_97_99_116_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__AbstractDeclarator = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"AbstractDeclarator\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"AbstractDeclarator\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"AbstractDeclarator\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_AnonymousIdentifier__char_class___range__0_0_lit___115_111_114_116_40_34_65_110_111_110_121_109_111_117_115_73_100_101_110_116_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__AnonymousIdentifier = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"AnonymousIdentifier\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"AnonymousIdentifier\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"AnonymousIdentifier\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Declaration__char_class___range__0_0_lit___115_111_114_116_40_34_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Declaration = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Declaration\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Declaration\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"Declaration\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Declaration__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_68_101_99_108_97_114_97_116_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__Declaration__layouts_LAYOUTLIST = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Declaration\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-star(sort(\\\"Declaration\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-star-seps(sort(\"Declaration\"),[layouts(\"LAYOUTLIST\")])))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Declarator__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_68_101_99_108_97_114_97_116_111_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Declarator = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Declarator\")),[\\char-class([range(0,0)]),lit(\"opt(sort(\\\"Declarator\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(opt(sort(\"Declarator\"))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Declarator__char_class___range__0_0_lit___115_111_114_116_40_34_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Declarator = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Declarator\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Declarator\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"Declarator\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Enumerator__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_69_110_117_109_101_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Enumerator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Enumerator\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-seps(sort(\\\"Enumerator\\\"),[lit(\\\",\\\")])\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-seps(sort(\"Enumerator\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Enumerator__char_class___range__0_0_lit___115_111_114_116_40_34_69_110_117_109_101_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Enumerator = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Enumerator\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Enumerator\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"Enumerator\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Expression__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_69_120_112_114_101_115_115_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Expression = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Expression\")),[\\char-class([range(0,0)]),lit(\"opt(sort(\\\"Expression\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(opt(sort(\"Expression\"))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Expression__char_class___range__0_0_lit___115_111_114_116_40_34_69_120_112_114_101_115_115_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Expression = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Expression\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Expression\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"Expression\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_ExternalDeclaration__char_class___range__0_0_lit___115_111_114_116_40_34_69_120_116_101_114_110_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__ExternalDeclaration = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"ExternalDeclaration\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"ExternalDeclaration\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"ExternalDeclaration\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_ExternalDeclaration__char_class___range__0_0_lit___105_116_101_114_40_115_111_114_116_40_34_69_120_116_101_114_110_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__ExternalDeclaration__layouts_LAYOUTLIST = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"ExternalDeclaration\")),[\\char-class([range(0,0)]),lit(\"iter(sort(\\\"ExternalDeclaration\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-seps(sort(\"ExternalDeclaration\"),[layouts(\"LAYOUTLIST\")])))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_FunctionDefinition__char_class___range__0_0_lit___115_111_114_116_40_34_70_117_110_99_116_105_111_110_68_101_102_105_110_105_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__FunctionDefinition = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"FunctionDefinition\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"FunctionDefinition\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"FunctionDefinition\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_FunctionPrototype__char_class___range__0_0_lit___115_111_114_116_40_34_70_117_110_99_116_105_111_110_80_114_111_116_111_116_121_112_101_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__FunctionPrototype = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"FunctionPrototype\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"FunctionPrototype\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"FunctionPrototype\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_GlobalDeclaration__char_class___range__0_0_lit___115_111_114_116_40_34_71_108_111_98_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__GlobalDeclaration = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"GlobalDeclaration\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"GlobalDeclaration\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"GlobalDeclaration\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_InitDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_73_110_105_116_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__InitDeclarator = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"InitDeclarator\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"InitDeclarator\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"InitDeclarator\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_InitDeclarator__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_73_110_105_116_68_101_99_108_97_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__InitDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"InitDeclarator\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-seps(sort(\\\"InitDeclarator\\\"),[lit(\\\",\\\")])\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-seps(sort(\"InitDeclarator\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Initializer__char_class___range__0_0_lit___115_111_114_116_40_34_73_110_105_116_105_97_108_105_122_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Initializer = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Initializer\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Initializer\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"Initializer\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Initializer__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_73_110_105_116_105_97_108_105_122_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Initializer__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Initializer\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-seps(sort(\\\"Initializer\\\"),[lit(\\\",\\\")])\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-seps(sort(\"Initializer\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_MoreParameters__char_class___range__0_0_lit___115_111_114_116_40_34_77_111_114_101_80_97_114_97_109_101_116_101_114_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__MoreParameters = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"MoreParameters\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"MoreParameters\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"MoreParameters\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_MoreParameters__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_77_111_114_101_80_97_114_97_109_101_116_101_114_115_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__MoreParameters = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"MoreParameters\")),[\\char-class([range(0,0)]),lit(\"opt(sort(\\\"MoreParameters\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(opt(sort(\"MoreParameters\"))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_NonCommaExpression__char_class___range__0_0_lit___115_111_114_116_40_34_78_111_110_67_111_109_109_97_69_120_112_114_101_115_115_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__NonCommaExpression = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"NonCommaExpression\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"NonCommaExpression\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"NonCommaExpression\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_NonCommaExpression__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_45_115_101_112_115_40_115_111_114_116_40_34_78_111_110_67_111_109_109_97_69_120_112_114_101_115_115_105_111_110_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__NonCommaExpression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"NonCommaExpression\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-star-seps(sort(\\\"NonCommaExpression\\\"),[lit(\\\",\\\")])\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-star-seps(sort(\"NonCommaExpression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Parameter__char_class___range__0_0_lit___115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Parameter = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Parameter\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Parameter\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"Parameter\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Parameter__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Parameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Parameter\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-seps(sort(\\\"Parameter\\\"),[lit(\\\",\\\")])\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-seps(sort(\"Parameter\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Parameters__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_115_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Parameters = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Parameters\")),[\\char-class([range(0,0)]),lit(\"opt(sort(\\\"Parameters\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(opt(sort(\"Parameters\"))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Parameters__char_class___range__0_0_lit___115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Parameters = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Parameters\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Parameters\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"Parameters\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_PrototypeDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__PrototypeDeclarator = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"PrototypeDeclarator\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"PrototypeDeclarator\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"PrototypeDeclarator\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_PrototypeParameter__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__PrototypeParameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"PrototypeParameter\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-seps(sort(\\\"PrototypeParameter\\\"),[lit(\\\",\\\")])\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-seps(sort(\"PrototypeParameter\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_PrototypeParameter__char_class___range__0_0_lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__PrototypeParameter = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"PrototypeParameter\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"PrototypeParameter\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"PrototypeParameter\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_PrototypeParameters__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_115_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__PrototypeParameters = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"PrototypeParameters\")),[\\char-class([range(0,0)]),lit(\"opt(sort(\\\"PrototypeParameters\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(opt(sort(\"PrototypeParameters\"))))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_PrototypeParameters__char_class___range__0_0_lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__PrototypeParameters = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"PrototypeParameters\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"PrototypeParameters\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"PrototypeParameters\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Specifier__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__Specifier__layouts_LAYOUTLIST = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Specifier\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-star(sort(\\\"Specifier\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-star-seps(sort(\"Specifier\"),[layouts(\"LAYOUTLIST\")])))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Specifier__char_class___range__0_0_lit___105_116_101_114_40_115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Specifier__layouts_LAYOUTLIST = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Specifier\")),[\\char-class([range(0,0)]),lit(\"iter(sort(\\\"Specifier\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-seps(sort(\"Specifier\"),[layouts(\"LAYOUTLIST\")])))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Specifier__char_class___range__0_0_lit___115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Specifier = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Specifier\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Specifier\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"Specifier\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Statement__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_97_116_101_109_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Statement = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Statement\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Statement\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"Statement\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_Statement__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_97_116_101_109_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__Statement__layouts_LAYOUTLIST = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Statement\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-star(sort(\\\"Statement\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")])))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_StorageClass__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_111_114_97_103_101_67_108_97_115_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StorageClass = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"StorageClass\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"StorageClass\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"StorageClass\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_StructDeclaration__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__StructDeclaration__layouts_LAYOUTLIST = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"StructDeclaration\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-star(sort(\\\"StructDeclaration\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-star-seps(sort(\"StructDeclaration\"),[layouts(\"LAYOUTLIST\")])))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_StructDeclaration__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StructDeclaration = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"StructDeclaration\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"StructDeclaration\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"StructDeclaration\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_StructDeclarator__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__StructDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"StructDeclarator\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-seps(sort(\\\"StructDeclarator\\\"),[lit(\\\",\\\")])\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-seps(sort(\"StructDeclarator\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_StructDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StructDeclarator = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"StructDeclarator\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"StructDeclarator\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"StructDeclarator\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_TranslationUnit__char_class___range__0_0_lit___115_111_114_116_40_34_84_114_97_110_115_108_97_116_105_111_110_85_110_105_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TranslationUnit = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"TranslationUnit\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"TranslationUnit\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"TranslationUnit\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_TypeName__char_class___range__0_0_lit___115_111_114_116_40_34_84_121_112_101_78_97_109_101_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TypeName = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"TypeName\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"TypeName\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"TypeName\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_TypeQualifier__char_class___range__0_0_lit___115_111_114_116_40_34_84_121_112_101_81_117_97_108_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TypeQualifier = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"TypeQualifier\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"TypeQualifier\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"TypeQualifier\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_TypeQualifier__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_84_121_112_101_81_117_97_108_105_102_105_101_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__TypeQualifier__layouts_LAYOUTLIST = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"TypeQualifier\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-star(sort(\\\"TypeQualifier\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-star-seps(sort(\"TypeQualifier\"),[layouts(\"LAYOUTLIST\")])))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_TypeSpecifier__char_class___range__0_0_lit___115_111_114_116_40_34_84_121_112_101_83_112_101_99_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TypeSpecifier = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"TypeSpecifier\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"TypeSpecifier\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"TypeSpecifier\")))})", Factory.Production);
  private static final IConstructor prod__arrayDeclarator_AbstractDeclarator__decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_exp_opt__Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"arrayDeclarator\",sort(\"AbstractDeclarator\")),[label(\"decl\",sort(\"AbstractDeclarator\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"exp\",opt(sort(\"Expression\"))),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__arrayDeclarator_Declarator__decl_Declarator_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_exp_opt__Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"arrayDeclarator\",sort(\"Declarator\")),[label(\"decl\",sort(\"Declarator\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"exp\",opt(sort(\"Expression\"))),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__arrayDeclarator_PrototypeDeclarator__proto__decl_PrototypeDeclarator_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_exp_opt__Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(label(\"arrayDeclarator\",sort(\"PrototypeDeclarator\")),[label(\"proto_decl\",sort(\"PrototypeDeclarator\")),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),label(\"exp\",opt(sort(\"Expression\"))),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__bracket_AbstractDeclarator__lit___40_layouts_LAYOUTLIST_decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"AbstractDeclarator\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"decl\",sort(\"AbstractDeclarator\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__bracket_Declarator__lit___40_layouts_LAYOUTLIST_decl_Declarator_layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"Declarator\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"decl\",sort(\"Declarator\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__bracket_Expression__lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"Expression\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__bracket_PrototypeDeclarator__lit___40_layouts_LAYOUTLIST_abs__decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___41__bracket = (IConstructor) _read("prod(label(\"bracket\",sort(\"PrototypeDeclarator\")),[lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"abs_decl\",sort(\"AbstractDeclarator\")),layouts(\"LAYOUTLIST\"),lit(\")\")],{bracket()})", Factory.Production);
  private static final IConstructor prod__char_TypeSpecifier__lit_char_ = (IConstructor) _read("prod(label(\"char\",sort(\"TypeSpecifier\")),[lit(\"char\")],{})", Factory.Production);
  private static final IConstructor prod__commaExpression_Expression__Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(label(\"commaExpression\",sort(\"Expression\")),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__comment_LAYOUT__Comment__tag__category___67_111_109_109_101_110_116 = (IConstructor) _read("prod(label(\"comment\",lex(\"LAYOUT\")),[lex(\"Comment\")],{tag(\"category\"(\"Comment\"))})", Factory.Production);
  private static final IConstructor prod__declarationWithInitDecls_Declaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_initDeclarators_iter_seps__InitDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"declarationWithInitDecls\",sort(\"Declaration\")),[label(\"specs\",\\iter-seps(sort(\"Specifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"initDeclarators\",\\iter-seps(sort(\"InitDeclarator\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__declarationWithoutInitDecls_Declaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"declarationWithoutInitDecls\",sort(\"Declaration\")),[label(\"specs\",\\iter-seps(sort(\"Specifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__defaultFunctionDefinition_FunctionDefinition__specs_iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Declarator_layouts_LAYOUTLIST_iter_star_seps__Declaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__Declaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"defaultFunctionDefinition\",sort(\"FunctionDefinition\")),[label(\"specs\",\\iter-star-seps(sort(\"Specifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),sort(\"Declarator\"),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"Declaration\"),[layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"Declaration\"),[layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__defaultFunctionPrototype_FunctionPrototype__specs_iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_PrototypeDeclarator_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"defaultFunctionPrototype\",sort(\"FunctionPrototype\")),[label(\"specs\",\\iter-star-seps(sort(\"Specifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"decl\",sort(\"PrototypeDeclarator\")),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__double_TypeSpecifier__lit_double_ = (IConstructor) _read("prod(label(\"double\",sort(\"TypeSpecifier\")),[lit(\"double\")],{})", Factory.Production);
  private static final IConstructor prod__enum_TypeSpecifier__lit_enum_layouts_LAYOUTLIST_Identifier_ = (IConstructor) _read("prod(label(\"enum\",sort(\"TypeSpecifier\")),[lit(\"enum\"),layouts(\"LAYOUTLIST\"),lex(\"Identifier\")],{})", Factory.Production);
  private static final IConstructor prod__enumAnonDecl_TypeSpecifier__lit_enum_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_seps__Enumerator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"enumAnonDecl\",sort(\"TypeSpecifier\")),[lit(\"enum\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),\\iter-seps(sort(\"Enumerator\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__enumDecl_TypeSpecifier__lit_enum_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_seps__Enumerator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"enumDecl\",sort(\"TypeSpecifier\")),[lit(\"enum\"),layouts(\"LAYOUTLIST\"),lex(\"Identifier\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),\\iter-seps(sort(\"Enumerator\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__float_TypeSpecifier__lit_float_ = (IConstructor) _read("prod(label(\"float\",sort(\"TypeSpecifier\")),[lit(\"float\")],{})", Factory.Production);
  private static final IConstructor prod__functionDeclarator_AbstractDeclarator__decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_params_opt__Parameters_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"functionDeclarator\",sort(\"AbstractDeclarator\")),[label(\"decl\",sort(\"AbstractDeclarator\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"params\",opt(sort(\"Parameters\"))),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__functionDeclarator_Declarator__decl_Declarator_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_params_opt__Parameters_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"functionDeclarator\",sort(\"Declarator\")),[label(\"decl\",sort(\"Declarator\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"params\",opt(sort(\"Parameters\"))),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__functionDeclarator_PrototypeDeclarator__proto__decl_PrototypeDeclarator_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_params_opt__PrototypeParameters_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(label(\"functionDeclarator\",sort(\"PrototypeDeclarator\")),[label(\"proto_decl\",sort(\"PrototypeDeclarator\")),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),label(\"params\",opt(sort(\"PrototypeParameters\"))),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__globalDeclarationWithInitDecls_GlobalDeclaration__specs0_iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_initDeclarators_iter_seps__InitDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"globalDeclarationWithInitDecls\",sort(\"GlobalDeclaration\")),[label(\"specs0\",\\iter-star-seps(sort(\"Specifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"initDeclarators\",\\iter-seps(sort(\"InitDeclarator\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__globalDeclarationWithoutInitDecls_GlobalDeclaration__specs1_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"globalDeclarationWithoutInitDecls\",sort(\"GlobalDeclaration\")),[label(\"specs1\",\\iter-seps(sort(\"Specifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__identifier_AbstractDeclarator__AnonymousIdentifier_ = (IConstructor) _read("prod(label(\"identifier\",sort(\"AbstractDeclarator\")),[sort(\"AnonymousIdentifier\")],{})", Factory.Production);
  private static final IConstructor prod__identifier_Declarator__Identifier_ = (IConstructor) _read("prod(label(\"identifier\",sort(\"Declarator\")),[lex(\"Identifier\")],{})", Factory.Production);
  private static final IConstructor prod__identifier_PrototypeDeclarator__Identifier_ = (IConstructor) _read("prod(label(\"identifier\",sort(\"PrototypeDeclarator\")),[lex(\"Identifier\")],{})", Factory.Production);
  private static final IConstructor prod__identifier_TypeSpecifier__Identifier_ = (IConstructor) _read("prod(label(\"identifier\",sort(\"TypeSpecifier\")),[lex(\"Identifier\")],{})", Factory.Production);
  private static final IConstructor prod__int_TypeSpecifier__lit_int_ = (IConstructor) _read("prod(label(\"int\",sort(\"TypeSpecifier\")),[lit(\"int\")],{})", Factory.Production);
  private static final IConstructor prod__long_TypeSpecifier__lit_long_ = (IConstructor) _read("prod(label(\"long\",sort(\"TypeSpecifier\")),[lit(\"long\")],{})", Factory.Production);
  private static final IConstructor prod__multiplicationExpression_Expression__lexp_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_rexp_Expression__assoc__left = (IConstructor) _read("prod(label(\"multiplicationExpression\",sort(\"Expression\")),[label(\"lexp\",sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"rexp\",sort(\"Expression\"))],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__nonCommaExpression_NonCommaExpression__expr_Expression_ = (IConstructor) _read("prod(label(\"nonCommaExpression\",sort(\"NonCommaExpression\")),[label(\"expr\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__pointerDeclarator_AbstractDeclarator__lit___42_layouts_LAYOUTLIST_qualifiers_iter_star_seps__TypeQualifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_AbstractDeclarator_ = (IConstructor) _read("prod(label(\"pointerDeclarator\",sort(\"AbstractDeclarator\")),[lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"qualifiers\",\\iter-star-seps(sort(\"TypeQualifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"decl\",sort(\"AbstractDeclarator\"))],{})", Factory.Production);
  private static final IConstructor prod__pointerDeclarator_Declarator__lit___42_layouts_LAYOUTLIST_qualifiers_iter_star_seps__TypeQualifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_Declarator_ = (IConstructor) _read("prod(label(\"pointerDeclarator\",sort(\"Declarator\")),[lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"qualifiers\",\\iter-star-seps(sort(\"TypeQualifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"decl\",sort(\"Declarator\"))],{})", Factory.Production);
  private static final IConstructor prod__pointerDeclarator_PrototypeDeclarator__lit___42_layouts_LAYOUTLIST_qualifiers_iter_star_seps__TypeQualifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_PrototypeDeclarator_ = (IConstructor) _read("prod(label(\"pointerDeclarator\",sort(\"PrototypeDeclarator\")),[lit(\"*\"),layouts(\"LAYOUTLIST\"),label(\"qualifiers\",\\iter-star-seps(sort(\"TypeQualifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),label(\"decl\",sort(\"PrototypeDeclarator\"))],{})", Factory.Production);
  private static final IConstructor prod__short_TypeSpecifier__lit_short_ = (IConstructor) _read("prod(label(\"short\",sort(\"TypeSpecifier\")),[lit(\"short\")],{})", Factory.Production);
  private static final IConstructor prod__sizeOfExpression_Expression__lit_sizeof_layouts_LAYOUTLIST_exp_Expression_ = (IConstructor) _read("prod(label(\"sizeOfExpression\",sort(\"Expression\")),[lit(\"sizeof\"),layouts(\"LAYOUTLIST\"),label(\"exp\",sort(\"Expression\"))],{})", Factory.Production);
  private static final IConstructor prod__storageClass_Specifier__StorageClass_ = (IConstructor) _read("prod(label(\"storageClass\",sort(\"Specifier\")),[sort(\"StorageClass\")],{})", Factory.Production);
  private static final IConstructor prod__struct_TypeSpecifier__lit_struct_layouts_LAYOUTLIST_Identifier_ = (IConstructor) _read("prod(label(\"struct\",sort(\"TypeSpecifier\")),[lit(\"struct\"),layouts(\"LAYOUTLIST\"),lex(\"Identifier\")],{})", Factory.Production);
  private static final IConstructor prod__structAnonDecl_TypeSpecifier__lit_struct_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"structAnonDecl\",sort(\"TypeSpecifier\")),[lit(\"struct\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"StructDeclaration\"),[layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__structDecl_TypeSpecifier__lit_struct_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"structDecl\",sort(\"TypeSpecifier\")),[lit(\"struct\"),layouts(\"LAYOUTLIST\"),lex(\"Identifier\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"StructDeclaration\"),[layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__structDeclWithDecl_StructDeclaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_iter_seps__StructDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"structDeclWithDecl\",sort(\"StructDeclaration\")),[label(\"specs\",\\iter-seps(sort(\"Specifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),\\iter-seps(sort(\"StructDeclarator\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__structDeclWithoutDecl_StructDeclaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(label(\"structDeclWithoutDecl\",sort(\"StructDeclaration\")),[label(\"specs\",\\iter-seps(sort(\"Specifier\"),[layouts(\"LAYOUTLIST\")])),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__typeDef_StorageClass__lit_typedef_ = (IConstructor) _read("prod(label(\"typeDef\",sort(\"StorageClass\")),[lit(\"typedef\")],{})", Factory.Production);
  private static final IConstructor prod__typeQualifier_Specifier__TypeQualifier_ = (IConstructor) _read("prod(label(\"typeQualifier\",sort(\"Specifier\")),[sort(\"TypeQualifier\")],{})", Factory.Production);
  private static final IConstructor prod__typeSpecifier_Specifier__TypeSpecifier_ = (IConstructor) _read("prod(label(\"typeSpecifier\",sort(\"Specifier\")),[sort(\"TypeSpecifier\")],{})", Factory.Production);
  private static final IConstructor prod__union_TypeSpecifier__lit_union_layouts_LAYOUTLIST_Identifier_ = (IConstructor) _read("prod(label(\"union\",sort(\"TypeSpecifier\")),[lit(\"union\"),layouts(\"LAYOUTLIST\"),lex(\"Identifier\")],{})", Factory.Production);
  private static final IConstructor prod__unionAnonDecl_TypeSpecifier__lit_union_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"unionAnonDecl\",sort(\"TypeSpecifier\")),[lit(\"union\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"StructDeclaration\"),[layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__unionDecl_TypeSpecifier__lit_union_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(label(\"unionDecl\",sort(\"TypeSpecifier\")),[lit(\"union\"),layouts(\"LAYOUTLIST\"),lex(\"Identifier\"),layouts(\"LAYOUTLIST\"),lit(\"{\"),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"StructDeclaration\"),[layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__variable_Expression__Identifier_ = (IConstructor) _read("prod(label(\"variable\",sort(\"Expression\")),[lex(\"Identifier\")],{})", Factory.Production);
  private static final IConstructor prod__void_TypeSpecifier__lit_void_ = (IConstructor) _read("prod(label(\"void\",sort(\"TypeSpecifier\")),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__whitespace_LAYOUT__char_class___range__9_10_range__13_13_range__32_32_ = (IConstructor) _read("prod(label(\"whitespace\",lex(\"LAYOUT\")),[\\char-class([range(9,10),range(13,13),range(32,32)])],{})", Factory.Production);
  private static final IConstructor prod__layouts_$default$__ = (IConstructor) _read("prod(layouts(\"$default$\"),[],{})", Factory.Production);
  private static final IConstructor prod__layouts_LAYOUTLIST__iter_star__LAYOUT_ = (IConstructor) _read("prod(layouts(\"LAYOUTLIST\"),[conditional(\\iter-star(lex(\"LAYOUT\")),{\\not-follow(\\char-class([range(9,10),range(13,13),range(32,32)]))})],{})", Factory.Production);
  private static final IConstructor prod__Asterisk__char_class___range__42_42_ = (IConstructor) _read("prod(lex(\"Asterisk\"),[conditional(\\char-class([range(42,42)]),{\\not-follow(\\char-class([range(47,47)]))})],{})", Factory.Production);
  private static final IConstructor prod__CharacterConstant__opt__char_class___range__76_76_char_class___range__39_39_iter__CharacterConstantContent_char_class___range__39_39_ = (IConstructor) _read("prod(lex(\"CharacterConstant\"),[opt(\\char-class([range(76,76)])),\\char-class([range(39,39)]),iter(lex(\"CharacterConstantContent\")),\\char-class([range(39,39)])],{})", Factory.Production);
  private static final IConstructor prod__CharacterConstantContent__char_class___range__1_38_range__40_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"CharacterConstantContent\"),[\\char-class([range(1,38),range(40,91),range(93,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__CharacterConstantContent__char_class___range__92_92_char_class___range__1_16777215_ = (IConstructor) _read("prod(lex(\"CharacterConstantContent\"),[\\char-class([range(92,92)]),\\char-class([range(1,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__Comment__char_class___range__47_47_char_class___range__42_42_iter_star__MultiLineCommentBodyToken_char_class___range__42_42_char_class___range__47_47_ = (IConstructor) _read("prod(lex(\"Comment\"),[\\char-class([range(47,47)]),\\char-class([range(42,42)]),\\iter-star(lex(\"MultiLineCommentBodyToken\")),\\char-class([range(42,42)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__Comment__lit___47_47_iter_star__char_class___range__1_9_range__11_16777215_char_class___range__10_10_ = (IConstructor) _read("prod(lex(\"Comment\"),[lit(\"//\"),\\iter-star(\\char-class([range(1,9),range(11,16777215)])),\\char-class([range(10,10)])],{})", Factory.Production);
  private static final IConstructor prod__Exponent__char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_ = (IConstructor) _read("prod(lex(\"Exponent\"),[\\char-class([range(69,69),range(101,101)]),opt(\\char-class([range(43,43),range(45,45)])),conditional(iter(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57)]))})],{})", Factory.Production);
  private static final IConstructor prod__FloatingPointConstant__iter_star__char_class___range__48_57_char_class___range__46_46_iter__char_class___range__48_57_opt__Exponent_opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108_ = (IConstructor) _read("prod(lex(\"FloatingPointConstant\"),[\\iter-star(\\char-class([range(48,57)])),\\char-class([range(46,46)]),conditional(iter(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57)]))}),opt(lex(\"Exponent\")),opt(\\char-class([range(70,70),range(76,76),range(102,102),range(108,108)]))],{})", Factory.Production);
  private static final IConstructor prod__FloatingPointConstant__iter__char_class___range__48_57_Exponent_opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108_ = (IConstructor) _read("prod(lex(\"FloatingPointConstant\"),[iter(\\char-class([range(48,57)])),lex(\"Exponent\"),opt(\\char-class([range(70,70),range(76,76),range(102,102),range(108,108)]))],{})", Factory.Production);
  private static final IConstructor prod__FloatingPointConstant__iter__char_class___range__48_57_char_class___range__46_46_opt__Exponent_opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108_ = (IConstructor) _read("prod(lex(\"FloatingPointConstant\"),[iter(\\char-class([range(48,57)])),\\char-class([range(46,46)]),opt(lex(\"Exponent\")),opt(\\char-class([range(70,70),range(76,76),range(102,102),range(108,108)]))],{})", Factory.Production);
  private static final IConstructor prod__HexadecimalConstant__char_class___range__48_48_char_class___range__88_88_range__120_120_iter__char_class___range__48_57_range__65_70_range__97_102_iter_star__char_class___range__76_76_range__85_85_range__108_108_range__117_117_ = (IConstructor) _read("prod(lex(\"HexadecimalConstant\"),[\\char-class([range(48,48)]),\\char-class([range(88,88),range(120,120)]),iter(\\char-class([range(48,57),range(65,70),range(97,102)])),conditional(\\iter-star(\\char-class([range(76,76),range(85,85),range(108,108),range(117,117)])),{\\not-follow(\\char-class([range(48,57),range(65,70),range(97,102)]))})],{})", Factory.Production);
  private static final IConstructor prod__Identifier__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_ = (IConstructor) _read("prod(lex(\"Identifier\"),[conditional(seq([\\char-class([range(65,90),range(95,95),range(97,122)]),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})]),{delete(keywords(\"Keyword\"))})],{})", Factory.Production);
  private static final IConstructor prod__IntegerConstant__iter__char_class___range__48_57_iter_star__char_class___range__76_76_range__85_85_range__108_108_range__117_117_ = (IConstructor) _read("prod(lex(\"IntegerConstant\"),[iter(\\char-class([range(48,57)])),conditional(\\iter-star(\\char-class([range(76,76),range(85,85),range(108,108),range(117,117)])),{\\not-follow(\\char-class([range(48,57)]))})],{})", Factory.Production);
  private static final IConstructor prod__MultiLineCommentBodyToken__Asterisk_ = (IConstructor) _read("prod(lex(\"MultiLineCommentBodyToken\"),[lex(\"Asterisk\")],{})", Factory.Production);
  private static final IConstructor prod__MultiLineCommentBodyToken__char_class___range__1_41_range__43_16777215_ = (IConstructor) _read("prod(lex(\"MultiLineCommentBodyToken\"),[\\char-class([range(1,41),range(43,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__StringConstant__opt__char_class___range__76_76_char_class___range__34_34_iter_star__StringConstantContent_char_class___range__34_34_ = (IConstructor) _read("prod(lex(\"StringConstant\"),[opt(\\char-class([range(76,76)])),\\char-class([range(34,34)]),\\iter-star(lex(\"StringConstantContent\")),\\char-class([range(34,34)])],{})", Factory.Production);
  private static final IConstructor prod__StringConstantContent__char_class___range__1_33_range__35_91_range__93_16777215_ = (IConstructor) _read("prod(lex(\"StringConstantContent\"),[\\char-class([range(1,33),range(35,91),range(93,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__StringConstantContent__char_class___range__92_92_char_class___range__1_16777215_ = (IConstructor) _read("prod(lex(\"StringConstantContent\"),[\\char-class([range(92,92)]),\\char-class([range(1,16777215)])],{})", Factory.Production);
  private static final IConstructor prod__lit___10_9_32_32_32_32_32_32_32_32_124_32_61__char_class___range__10_10_char_class___range__9_9_char_class___range__32_32_char_class___range__32_32_char_class___range__32_32_char_class___range__32_32_char_class___range__32_32_char_class___range__32_32_char_class___range__32_32_char_class___range__32_32_char_class___range__124_124_char_class___range__32_32_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\n\\t        | =\"),[\\char-class([range(10,10)]),\\char-class([range(9,9)]),\\char-class([range(32,32)]),\\char-class([range(32,32)]),\\char-class([range(32,32)]),\\char-class([range(32,32)]),\\char-class([range(32,32)]),\\char-class([range(32,32)]),\\char-class([range(32,32)]),\\char-class([range(32,32)]),\\char-class([range(124,124)]),\\char-class([range(32,32)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___33__char_class___range__33_33_ = (IConstructor) _read("prod(lit(\"!\"),[\\char-class([range(33,33)])],{})", Factory.Production);
  private static final IConstructor prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"!=\"),[\\char-class([range(33,33)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___37__char_class___range__37_37_ = (IConstructor) _read("prod(lit(\"%\"),[\\char-class([range(37,37)])],{})", Factory.Production);
  private static final IConstructor prod__lit___37_61__char_class___range__37_37_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"%=\"),[\\char-class([range(37,37)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___38__char_class___range__38_38_ = (IConstructor) _read("prod(lit(\"&\"),[\\char-class([range(38,38)])],{})", Factory.Production);
  private static final IConstructor prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_ = (IConstructor) _read("prod(lit(\"&&\"),[\\char-class([range(38,38)]),\\char-class([range(38,38)])],{})", Factory.Production);
  private static final IConstructor prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"&=\"),[\\char-class([range(38,38)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___40__char_class___range__40_40_ = (IConstructor) _read("prod(lit(\"(\"),[\\char-class([range(40,40)])],{})", Factory.Production);
  private static final IConstructor prod__lit___41__char_class___range__41_41_ = (IConstructor) _read("prod(lit(\")\"),[\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___42__char_class___range__42_42_ = (IConstructor) _read("prod(lit(\"*\"),[\\char-class([range(42,42)])],{})", Factory.Production);
  private static final IConstructor prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"*=\"),[\\char-class([range(42,42)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___43__char_class___range__43_43_ = (IConstructor) _read("prod(lit(\"+\"),[\\char-class([range(43,43)])],{})", Factory.Production);
  private static final IConstructor prod__lit___43_43__char_class___range__43_43_char_class___range__43_43_ = (IConstructor) _read("prod(lit(\"++\"),[\\char-class([range(43,43)]),\\char-class([range(43,43)])],{})", Factory.Production);
  private static final IConstructor prod__lit___43_61__char_class___range__43_43_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"+=\"),[\\char-class([range(43,43)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___44__char_class___range__44_44_ = (IConstructor) _read("prod(lit(\",\"),[\\char-class([range(44,44)])],{})", Factory.Production);
  private static final IConstructor prod__lit____char_class___range__45_45_ = (IConstructor) _read("prod(lit(\"-\"),[\\char-class([range(45,45)])],{})", Factory.Production);
  private static final IConstructor prod__lit_____char_class___range__45_45_char_class___range__45_45_ = (IConstructor) _read("prod(lit(\"--\"),[\\char-class([range(45,45)]),\\char-class([range(45,45)])],{})", Factory.Production);
  private static final IConstructor prod__lit___45_61__char_class___range__45_45_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"-=\"),[\\char-class([range(45,45)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___45_62__char_class___range__45_45_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"-\\>\"),[\\char-class([range(45,45)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___46__char_class___range__46_46_ = (IConstructor) _read("prod(lit(\".\"),[\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_ = (IConstructor) _read("prod(lit(\"...\"),[\\char-class([range(46,46)]),\\char-class([range(46,46)]),\\char-class([range(46,46)])],{})", Factory.Production);
  private static final IConstructor prod__lit___47__char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"/\"),[\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_ = (IConstructor) _read("prod(lit(\"//\"),[\\char-class([range(47,47)]),\\char-class([range(47,47)])],{})", Factory.Production);
  private static final IConstructor prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"/=\"),[\\char-class([range(47,47)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58__char_class___range__58_58_ = (IConstructor) _read("prod(lit(\":\"),[\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__lit___59__char_class___range__59_59_ = (IConstructor) _read("prod(lit(\";\"),[\\char-class([range(59,59)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60__char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\<\"),[\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_ = (IConstructor) _read("prod(lit(\"\\<\\<\"),[\\char-class([range(60,60)]),\\char-class([range(60,60)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_60_61__char_class___range__60_60_char_class___range__60_60_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\<\\<=\"),[\\char-class([range(60,60)]),\\char-class([range(60,60)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\<=\"),[\\char-class([range(60,60)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___61__char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"=\"),[\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"==\"),[\\char-class([range(61,61)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___62__char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\>\"),[\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___62_61__char_class___range__62_62_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\>=\"),[\\char-class([range(62,62)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_ = (IConstructor) _read("prod(lit(\"\\>\\>\"),[\\char-class([range(62,62)]),\\char-class([range(62,62)])],{})", Factory.Production);
  private static final IConstructor prod__lit___62_62_61__char_class___range__62_62_char_class___range__62_62_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"\\>\\>=\"),[\\char-class([range(62,62)]),\\char-class([range(62,62)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit___63__char_class___range__63_63_ = (IConstructor) _read("prod(lit(\"?\"),[\\char-class([range(63,63)])],{})", Factory.Production);
  private static final IConstructor prod__lit___91__char_class___range__91_91_ = (IConstructor) _read("prod(lit(\"[\"),[\\char-class([range(91,91)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_69_110_117_109_101_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__101_101_char_class___range__112_112_char_class___range__115_115_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_char_class___range__101_101_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__44_44_char_class___range__91_91_char_class___range__108_108_char_class___range__105_105_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__44_44_char_class___range__34_34_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-seps(sort(\\\"Enumerator\\\"),[lit(\\\",\\\")])\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(112,112)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(69,69)]),\\char-class([range(110,110)]),\\char-class([range(117,117)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(91,91)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(44,44)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_73_110_105_116_68_101_99_108_97_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__101_101_char_class___range__112_112_char_class___range__115_115_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__73_73_char_class___range__110_110_char_class___range__105_105_char_class___range__116_116_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__44_44_char_class___range__91_91_char_class___range__108_108_char_class___range__105_105_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__44_44_char_class___range__34_34_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-seps(sort(\\\"InitDeclarator\\\"),[lit(\\\",\\\")])\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(112,112)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(73,73)]),\\char-class([range(110,110)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(91,91)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(44,44)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_73_110_105_116_105_97_108_105_122_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__101_101_char_class___range__112_112_char_class___range__115_115_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__73_73_char_class___range__110_110_char_class___range__105_105_char_class___range__116_116_char_class___range__105_105_char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__122_122_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__44_44_char_class___range__91_91_char_class___range__108_108_char_class___range__105_105_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__44_44_char_class___range__34_34_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-seps(sort(\\\"Initializer\\\"),[lit(\\\",\\\")])\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(112,112)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(73,73)]),\\char-class([range(110,110)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(122,122)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(91,91)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(44,44)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__101_101_char_class___range__112_112_char_class___range__115_115_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__44_44_char_class___range__91_91_char_class___range__108_108_char_class___range__105_105_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__44_44_char_class___range__34_34_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-seps(sort(\\\"Parameter\\\"),[lit(\\\",\\\")])\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(112,112)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(80,80)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(91,91)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(44,44)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__101_101_char_class___range__112_112_char_class___range__115_115_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__114_114_char_class___range__111_111_char_class___range__116_116_char_class___range__111_111_char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__44_44_char_class___range__91_91_char_class___range__108_108_char_class___range__105_105_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__44_44_char_class___range__34_34_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-seps(sort(\\\"PrototypeParameter\\\"),[lit(\\\",\\\")])\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(112,112)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(80,80)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(80,80)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(91,91)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(44,44)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__101_101_char_class___range__112_112_char_class___range__115_115_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__44_44_char_class___range__91_91_char_class___range__108_108_char_class___range__105_105_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__44_44_char_class___range__34_34_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-seps(sort(\\\"StructDeclarator\\\"),[lit(\\\",\\\")])\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(112,112)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(83,83)]),\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(117,117)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(91,91)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(44,44)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_116_97_114_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_49_44_57_41_44_114_97_110_103_101_40_49_49_44_49_54_55_55_55_50_49_53_41_93_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__92_92_char_class___range__99_99_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__45_45_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__40_40_char_class___range__91_91_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__49_49_char_class___range__44_44_char_class___range__57_57_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__49_49_char_class___range__49_49_char_class___range__44_44_char_class___range__49_49_char_class___range__54_54_char_class___range__55_55_char_class___range__55_55_char_class___range__55_55_char_class___range__50_50_char_class___range__49_49_char_class___range__53_53_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-star(\\\\char-class([range(1,9),range(11,16777215)]))\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(92,92)]),\\char-class([range(99,99)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(49,49)]),\\char-class([range(44,44)]),\\char-class([range(57,57)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(49,49)]),\\char-class([range(49,49)]),\\char-class([range(44,44)]),\\char-class([range(49,49)]),\\char-class([range(54,54)]),\\char-class([range(55,55)]),\\char-class([range(55,55)]),\\char-class([range(55,55)]),\\char-class([range(50,50)]),\\char-class([range(49,49)]),\\char-class([range(53,53)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_116_97_114_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_52_56_44_53_55_41_44_114_97_110_103_101_40_54_53_44_57_48_41_44_114_97_110_103_101_40_57_53_44_57_53_41_44_114_97_110_103_101_40_57_55_44_49_50_50_41_93_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__92_92_char_class___range__99_99_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__45_45_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__40_40_char_class___range__91_91_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__52_52_char_class___range__56_56_char_class___range__44_44_char_class___range__53_53_char_class___range__55_55_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__54_54_char_class___range__53_53_char_class___range__44_44_char_class___range__57_57_char_class___range__48_48_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__57_57_char_class___range__53_53_char_class___range__44_44_char_class___range__57_57_char_class___range__53_53_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__57_57_char_class___range__55_55_char_class___range__44_44_char_class___range__49_49_char_class___range__50_50_char_class___range__50_50_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-star(\\\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(92,92)]),\\char-class([range(99,99)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(52,52)]),\\char-class([range(56,56)]),\\char-class([range(44,44)]),\\char-class([range(53,53)]),\\char-class([range(55,55)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(54,54)]),\\char-class([range(53,53)]),\\char-class([range(44,44)]),\\char-class([range(57,57)]),\\char-class([range(48,48)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(57,57)]),\\char-class([range(53,53)]),\\char-class([range(44,44)]),\\char-class([range(57,57)]),\\char-class([range(53,53)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(57,57)]),\\char-class([range(55,55)]),\\char-class([range(44,44)]),\\char-class([range(49,49)]),\\char-class([range(50,50)]),\\char-class([range(50,50)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_116_97_114_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_52_56_44_53_55_41_93_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__92_92_char_class___range__99_99_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__45_45_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__40_40_char_class___range__91_91_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__52_52_char_class___range__56_56_char_class___range__44_44_char_class___range__53_53_char_class___range__55_55_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-star(\\\\char-class([range(48,57)]))\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(92,92)]),\\char-class([range(99,99)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(52,52)]),\\char-class([range(56,56)]),\\char-class([range(44,44)]),\\char-class([range(53,53)]),\\char-class([range(55,55)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_116_97_114_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_55_54_44_55_54_41_44_114_97_110_103_101_40_56_53_44_56_53_41_44_114_97_110_103_101_40_49_48_56_44_49_48_56_41_44_114_97_110_103_101_40_49_49_55_44_49_49_55_41_93_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__92_92_char_class___range__99_99_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__45_45_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__40_40_char_class___range__91_91_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__55_55_char_class___range__54_54_char_class___range__44_44_char_class___range__55_55_char_class___range__54_54_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__56_56_char_class___range__53_53_char_class___range__44_44_char_class___range__56_56_char_class___range__53_53_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__49_49_char_class___range__48_48_char_class___range__56_56_char_class___range__44_44_char_class___range__49_49_char_class___range__48_48_char_class___range__56_56_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__49_49_char_class___range__49_49_char_class___range__55_55_char_class___range__44_44_char_class___range__49_49_char_class___range__49_49_char_class___range__55_55_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-star(\\\\char-class([range(76,76),range(85,85),range(108,108),range(117,117)]))\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(92,92)]),\\char-class([range(99,99)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(55,55)]),\\char-class([range(54,54)]),\\char-class([range(44,44)]),\\char-class([range(55,55)]),\\char-class([range(54,54)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(56,56)]),\\char-class([range(53,53)]),\\char-class([range(44,44)]),\\char-class([range(56,56)]),\\char-class([range(53,53)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(49,49)]),\\char-class([range(48,48)]),\\char-class([range(56,56)]),\\char-class([range(44,44)]),\\char-class([range(49,49)]),\\char-class([range(48,48)]),\\char-class([range(56,56)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(49,49)]),\\char-class([range(49,49)]),\\char-class([range(55,55)]),\\char-class([range(44,44)]),\\char-class([range(49,49)]),\\char-class([range(49,49)]),\\char-class([range(55,55)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_68_101_99_108_97_114_97_116_105_111_110_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-star(sort(\\\"Declaration\\\"))\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_76_65_89_79_85_84_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__76_76_char_class___range__65_65_char_class___range__89_89_char_class___range__79_79_char_class___range__85_85_char_class___range__84_84_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-star(sort(\\\"LAYOUT\\\"))\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(76,76)]),\\char-class([range(65,65)]),\\char-class([range(89,89)]),\\char-class([range(79,79)]),\\char-class([range(85,85)]),\\char-class([range(84,84)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_77_117_108_116_105_76_105_110_101_67_111_109_109_101_110_116_66_111_100_121_84_111_107_101_110_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__77_77_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_char_class___range__105_105_char_class___range__76_76_char_class___range__105_105_char_class___range__110_110_char_class___range__101_101_char_class___range__67_67_char_class___range__111_111_char_class___range__109_109_char_class___range__109_109_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__66_66_char_class___range__111_111_char_class___range__100_100_char_class___range__121_121_char_class___range__84_84_char_class___range__111_111_char_class___range__107_107_char_class___range__101_101_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-star(sort(\\\"MultiLineCommentBodyToken\\\"))\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(77,77)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(76,76)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(101,101)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(66,66)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(121,121)]),\\char-class([range(84,84)]),\\char-class([range(111,111)]),\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__112_112_char_class___range__101_101_char_class___range__99_99_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-star(sort(\\\"Specifier\\\"))\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(83,83)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(105,105)]),\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_97_116_101_109_101_110_116_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__109_109_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-star(sort(\\\"Statement\\\"))\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(83,83)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__114_114_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-star(sort(\\\"StringConstantContent\\\"))\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(83,83)]),\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_105_111_110_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-star(sort(\\\"StructDeclaration\\\"))\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(83,83)]),\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(117,117)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_84_121_112_101_81_117_97_108_105_102_105_101_114_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__84_84_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__81_81_char_class___range__117_117_char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-star(sort(\\\"TypeQualifier\\\"))\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(84,84)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(81,81)]),\\char-class([range(117,117)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___92_105_116_101_114_45_115_116_97_114_45_115_101_112_115_40_115_111_114_116_40_34_78_111_110_67_111_109_109_97_69_120_112_114_101_115_115_105_111_110_34_41_44_91_108_105_116_40_34_44_34_41_93_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__101_101_char_class___range__112_112_char_class___range__115_115_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__78_78_char_class___range__111_111_char_class___range__110_110_char_class___range__67_67_char_class___range__111_111_char_class___range__109_109_char_class___range__109_109_char_class___range__97_97_char_class___range__69_69_char_class___range__120_120_char_class___range__112_112_char_class___range__114_114_char_class___range__101_101_char_class___range__115_115_char_class___range__115_115_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_char_class___range__44_44_char_class___range__91_91_char_class___range__108_108_char_class___range__105_105_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__44_44_char_class___range__34_34_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"\\\\iter-star-seps(sort(\\\"NonCommaExpression\\\"),[lit(\\\",\\\")])\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(112,112)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(78,78)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(109,109)]),\\char-class([range(97,97)]),\\char-class([range(69,69)]),\\char-class([range(120,120)]),\\char-class([range(112,112)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(91,91)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(44,44)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___93__char_class___range__93_93_ = (IConstructor) _read("prod(lit(\"]\"),[\\char-class([range(93,93)])],{})", Factory.Production);
  private static final IConstructor prod__lit___94__char_class___range__94_94_ = (IConstructor) _read("prod(lit(\"^\"),[\\char-class([range(94,94)])],{})", Factory.Production);
  private static final IConstructor prod__lit___94_61__char_class___range__94_94_char_class___range__61_61_ = (IConstructor) _read("prod(lit(\"^=\"),[\\char-class([range(94,94)]),\\char-class([range(61,61)])],{})", Factory.Production);
  private static final IConstructor prod__lit_auto__char_class___range__97_97_char_class___range__117_117_char_class___range__116_116_char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"auto\"),[\\char-class([range(97,97)]),\\char-class([range(117,117)]),\\char-class([range(116,116)]),\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_ = (IConstructor) _read("prod(lit(\"break\"),[\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(97,97)]),\\char-class([range(107,107)])],{})", Factory.Production);
  private static final IConstructor prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"case\"),[\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_char__char_class___range__99_99_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"char\"),[\\char-class([range(99,99)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__lit_const__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"const\"),[\\char-class([range(99,99)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"continue\"),[\\char-class([range(99,99)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(117,117)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"default\"),[\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(97,97)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_do__char_class___range__100_100_char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"do\"),[\\char-class([range(100,100)]),\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__lit_double__char_class___range__100_100_char_class___range__111_111_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"double\"),[\\char-class([range(100,100)]),\\char-class([range(111,111)]),\\char-class([range(117,117)]),\\char-class([range(98,98)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"else\"),[\\char-class([range(101,101)]),\\char-class([range(108,108)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_enum__char_class___range__101_101_char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_ = (IConstructor) _read("prod(lit(\"enum\"),[\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(117,117)]),\\char-class([range(109,109)])],{})", Factory.Production);
  private static final IConstructor prod__lit_extern__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"extern\"),[\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_float__char_class___range__102_102_char_class___range__108_108_char_class___range__111_111_char_class___range__97_97_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"float\"),[\\char-class([range(102,102)]),\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(97,97)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"for\"),[\\char-class([range(102,102)]),\\char-class([range(111,111)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__lit_goto__char_class___range__103_103_char_class___range__111_111_char_class___range__116_116_char_class___range__111_111_ = (IConstructor) _read("prod(lit(\"goto\"),[\\char-class([range(103,103)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(111,111)])],{})", Factory.Production);
  private static final IConstructor prod__lit_if__char_class___range__105_105_char_class___range__102_102_ = (IConstructor) _read("prod(lit(\"if\"),[\\char-class([range(105,105)]),\\char-class([range(102,102)])],{})", Factory.Production);
  private static final IConstructor prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"int\"),[\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit___105_116_101_114_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_52_56_44_53_55_41_44_114_97_110_103_101_40_54_53_44_55_48_41_44_114_97_110_103_101_40_57_55_44_49_48_50_41_93_41_41__char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__40_40_char_class___range__92_92_char_class___range__99_99_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__45_45_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__40_40_char_class___range__91_91_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__52_52_char_class___range__56_56_char_class___range__44_44_char_class___range__53_53_char_class___range__55_55_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__54_54_char_class___range__53_53_char_class___range__44_44_char_class___range__55_55_char_class___range__48_48_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__57_57_char_class___range__55_55_char_class___range__44_44_char_class___range__49_49_char_class___range__48_48_char_class___range__50_50_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"iter(\\\\char-class([range(48,57),range(65,70),range(97,102)]))\"),[\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(92,92)]),\\char-class([range(99,99)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(52,52)]),\\char-class([range(56,56)]),\\char-class([range(44,44)]),\\char-class([range(53,53)]),\\char-class([range(55,55)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(54,54)]),\\char-class([range(53,53)]),\\char-class([range(44,44)]),\\char-class([range(55,55)]),\\char-class([range(48,48)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(57,57)]),\\char-class([range(55,55)]),\\char-class([range(44,44)]),\\char-class([range(49,49)]),\\char-class([range(48,48)]),\\char-class([range(50,50)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___105_116_101_114_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_52_56_44_53_55_41_93_41_41__char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__40_40_char_class___range__92_92_char_class___range__99_99_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__45_45_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__40_40_char_class___range__91_91_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__52_52_char_class___range__56_56_char_class___range__44_44_char_class___range__53_53_char_class___range__55_55_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"iter(\\\\char-class([range(48,57)]))\"),[\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(92,92)]),\\char-class([range(99,99)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(52,52)]),\\char-class([range(56,56)]),\\char-class([range(44,44)]),\\char-class([range(53,53)]),\\char-class([range(55,55)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___105_116_101_114_40_115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_41__char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__67_67_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"iter(sort(\\\"CharacterConstantContent\\\"))\"),[\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(67,67)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___105_116_101_114_40_115_111_114_116_40_34_69_120_116_101_114_110_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41_41__char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"iter(sort(\\\"ExternalDeclaration\\\"))\"),[\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(69,69)]),\\char-class([range(120,120)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___105_116_101_114_40_115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_41__char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__112_112_char_class___range__101_101_char_class___range__99_99_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"iter(sort(\\\"Specifier\\\"))\"),[\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(83,83)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(105,105)]),\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit_long__char_class___range__108_108_char_class___range__111_111_char_class___range__110_110_char_class___range__103_103_ = (IConstructor) _read("prod(lit(\"long\"),[\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(103,103)])],{})", Factory.Production);
  private static final IConstructor prod__lit___111_112_116_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_52_51_44_52_51_41_44_114_97_110_103_101_40_52_53_44_52_53_41_93_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__92_92_char_class___range__99_99_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__45_45_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__40_40_char_class___range__91_91_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__52_52_char_class___range__51_51_char_class___range__44_44_char_class___range__52_52_char_class___range__51_51_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__52_52_char_class___range__53_53_char_class___range__44_44_char_class___range__52_52_char_class___range__53_53_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"opt(\\\\char-class([range(43,43),range(45,45)]))\"),[\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(92,92)]),\\char-class([range(99,99)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(52,52)]),\\char-class([range(51,51)]),\\char-class([range(44,44)]),\\char-class([range(52,52)]),\\char-class([range(51,51)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(52,52)]),\\char-class([range(53,53)]),\\char-class([range(44,44)]),\\char-class([range(52,52)]),\\char-class([range(53,53)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___111_112_116_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_55_48_44_55_48_41_44_114_97_110_103_101_40_55_54_44_55_54_41_44_114_97_110_103_101_40_49_48_50_44_49_48_50_41_44_114_97_110_103_101_40_49_48_56_44_49_48_56_41_93_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__92_92_char_class___range__99_99_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__45_45_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__40_40_char_class___range__91_91_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__55_55_char_class___range__48_48_char_class___range__44_44_char_class___range__55_55_char_class___range__48_48_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__55_55_char_class___range__54_54_char_class___range__44_44_char_class___range__55_55_char_class___range__54_54_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__49_49_char_class___range__48_48_char_class___range__50_50_char_class___range__44_44_char_class___range__49_49_char_class___range__48_48_char_class___range__50_50_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__49_49_char_class___range__48_48_char_class___range__56_56_char_class___range__44_44_char_class___range__49_49_char_class___range__48_48_char_class___range__56_56_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"opt(\\\\char-class([range(70,70),range(76,76),range(102,102),range(108,108)]))\"),[\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(92,92)]),\\char-class([range(99,99)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(55,55)]),\\char-class([range(48,48)]),\\char-class([range(44,44)]),\\char-class([range(55,55)]),\\char-class([range(48,48)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(55,55)]),\\char-class([range(54,54)]),\\char-class([range(44,44)]),\\char-class([range(55,55)]),\\char-class([range(54,54)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(49,49)]),\\char-class([range(48,48)]),\\char-class([range(50,50)]),\\char-class([range(44,44)]),\\char-class([range(49,49)]),\\char-class([range(48,48)]),\\char-class([range(50,50)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(49,49)]),\\char-class([range(48,48)]),\\char-class([range(56,56)]),\\char-class([range(44,44)]),\\char-class([range(49,49)]),\\char-class([range(48,48)]),\\char-class([range(56,56)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___111_112_116_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_55_54_44_55_54_41_93_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__92_92_char_class___range__99_99_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__45_45_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__40_40_char_class___range__91_91_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__55_55_char_class___range__54_54_char_class___range__44_44_char_class___range__55_55_char_class___range__54_54_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"opt(\\\\char-class([range(76,76)]))\"),[\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(92,92)]),\\char-class([range(99,99)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(55,55)]),\\char-class([range(54,54)]),\\char-class([range(44,44)]),\\char-class([range(55,55)]),\\char-class([range(54,54)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___111_112_116_40_108_105_116_40_34_44_34_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__108_108_char_class___range__105_105_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__44_44_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"opt(lit(\\\",\\\"))\"),[\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(44,44)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___111_112_116_40_115_111_114_116_40_34_68_101_99_108_97_114_97_116_111_114_34_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"opt(sort(\\\"Declarator\\\"))\"),[\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___111_112_116_40_115_111_114_116_40_34_69_120_112_111_110_101_110_116_34_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__120_120_char_class___range__112_112_char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"opt(sort(\\\"Exponent\\\"))\"),[\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(69,69)]),\\char-class([range(120,120)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___111_112_116_40_115_111_114_116_40_34_69_120_112_114_101_115_115_105_111_110_34_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__120_120_char_class___range__112_112_char_class___range__114_114_char_class___range__101_101_char_class___range__115_115_char_class___range__115_115_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"opt(sort(\\\"Expression\\\"))\"),[\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(69,69)]),\\char-class([range(120,120)]),\\char-class([range(112,112)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___111_112_116_40_115_111_114_116_40_34_77_111_114_101_80_97_114_97_109_101_116_101_114_115_34_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__77_77_char_class___range__111_111_char_class___range__114_114_char_class___range__101_101_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__115_115_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"opt(sort(\\\"MoreParameters\\\"))\"),[\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(77,77)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(80,80)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(115,115)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___111_112_116_40_115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_115_34_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__115_115_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"opt(sort(\\\"Parameters\\\"))\"),[\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(80,80)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(115,115)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___111_112_116_40_115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_115_34_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__114_114_char_class___range__111_111_char_class___range__116_116_char_class___range__111_111_char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__115_115_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"opt(sort(\\\"PrototypeParameters\\\"))\"),[\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(80,80)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(80,80)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(115,115)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit_register__char_class___range__114_114_char_class___range__101_101_char_class___range__103_103_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_ = (IConstructor) _read("prod(lit(\"register\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(103,103)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)])],{})", Factory.Production);
  private static final IConstructor prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"return\"),[\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(117,117)]),\\char-class([range(114,114)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_101_113_40_91_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_54_53_44_57_48_41_44_114_97_110_103_101_40_57_53_44_57_53_41_44_114_97_110_103_101_40_57_55_44_49_50_50_41_93_41_44_99_111_110_100_105_116_105_111_110_97_108_40_92_105_116_101_114_45_115_116_97_114_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_52_56_44_53_55_41_44_114_97_110_103_101_40_54_53_44_57_48_41_44_114_97_110_103_101_40_57_53_44_57_53_41_44_114_97_110_103_101_40_57_55_44_49_50_50_41_93_41_41_44_123_92_110_111_116_45_102_111_108_108_111_119_40_92_99_104_97_114_45_99_108_97_115_115_40_91_114_97_110_103_101_40_52_56_44_53_55_41_44_114_97_110_103_101_40_54_53_44_57_48_41_44_114_97_110_103_101_40_57_53_44_57_53_41_44_114_97_110_103_101_40_57_55_44_49_50_50_41_93_41_41_125_41_93_41__char_class___range__115_115_char_class___range__101_101_char_class___range__113_113_char_class___range__40_40_char_class___range__91_91_char_class___range__92_92_char_class___range__99_99_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__45_45_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__40_40_char_class___range__91_91_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__54_54_char_class___range__53_53_char_class___range__44_44_char_class___range__57_57_char_class___range__48_48_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__57_57_char_class___range__53_53_char_class___range__44_44_char_class___range__57_57_char_class___range__53_53_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__57_57_char_class___range__55_55_char_class___range__44_44_char_class___range__49_49_char_class___range__50_50_char_class___range__50_50_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_char_class___range__44_44_char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__100_100_char_class___range__105_105_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__40_40_char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__92_92_char_class___range__99_99_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__45_45_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__40_40_char_class___range__91_91_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__52_52_char_class___range__56_56_char_class___range__44_44_char_class___range__53_53_char_class___range__55_55_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__54_54_char_class___range__53_53_char_class___range__44_44_char_class___range__57_57_char_class___range__48_48_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__57_57_char_class___range__53_53_char_class___range__44_44_char_class___range__57_57_char_class___range__53_53_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__57_57_char_class___range__55_55_char_class___range__44_44_char_class___range__49_49_char_class___range__50_50_char_class___range__50_50_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_char_class___range__41_41_char_class___range__44_44_char_class___range__123_123_char_class___range__92_92_char_class___range__110_110_char_class___range__111_111_char_class___range__116_116_char_class___range__45_45_char_class___range__102_102_char_class___range__111_111_char_class___range__108_108_char_class___range__108_108_char_class___range__111_111_char_class___range__119_119_char_class___range__40_40_char_class___range__92_92_char_class___range__99_99_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__45_45_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__40_40_char_class___range__91_91_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__52_52_char_class___range__56_56_char_class___range__44_44_char_class___range__53_53_char_class___range__55_55_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__54_54_char_class___range__53_53_char_class___range__44_44_char_class___range__57_57_char_class___range__48_48_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__57_57_char_class___range__53_53_char_class___range__44_44_char_class___range__57_57_char_class___range__53_53_char_class___range__41_41_char_class___range__44_44_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__103_103_char_class___range__101_101_char_class___range__40_40_char_class___range__57_57_char_class___range__55_55_char_class___range__44_44_char_class___range__49_49_char_class___range__50_50_char_class___range__50_50_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_char_class___range__41_41_char_class___range__125_125_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"seq([\\\\char-class([range(65,90),range(95,95),range(97,122)]),conditional(\\\\iter-star(\\\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\\\not-follow(\\\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})])\"),[\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(113,113)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(92,92)]),\\char-class([range(99,99)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(54,54)]),\\char-class([range(53,53)]),\\char-class([range(44,44)]),\\char-class([range(57,57)]),\\char-class([range(48,48)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(57,57)]),\\char-class([range(53,53)]),\\char-class([range(44,44)]),\\char-class([range(57,57)]),\\char-class([range(53,53)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(57,57)]),\\char-class([range(55,55)]),\\char-class([range(44,44)]),\\char-class([range(49,49)]),\\char-class([range(50,50)]),\\char-class([range(50,50)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(99,99)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(100,100)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(40,40)]),\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(92,92)]),\\char-class([range(99,99)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(52,52)]),\\char-class([range(56,56)]),\\char-class([range(44,44)]),\\char-class([range(53,53)]),\\char-class([range(55,55)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(54,54)]),\\char-class([range(53,53)]),\\char-class([range(44,44)]),\\char-class([range(57,57)]),\\char-class([range(48,48)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(57,57)]),\\char-class([range(53,53)]),\\char-class([range(44,44)]),\\char-class([range(57,57)]),\\char-class([range(53,53)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(57,57)]),\\char-class([range(55,55)]),\\char-class([range(44,44)]),\\char-class([range(49,49)]),\\char-class([range(50,50)]),\\char-class([range(50,50)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(123,123)]),\\char-class([range(92,92)]),\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(45,45)]),\\char-class([range(102,102)]),\\char-class([range(111,111)]),\\char-class([range(108,108)]),\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(119,119)]),\\char-class([range(40,40)]),\\char-class([range(92,92)]),\\char-class([range(99,99)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(52,52)]),\\char-class([range(56,56)]),\\char-class([range(44,44)]),\\char-class([range(53,53)]),\\char-class([range(55,55)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(54,54)]),\\char-class([range(53,53)]),\\char-class([range(44,44)]),\\char-class([range(57,57)]),\\char-class([range(48,48)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(57,57)]),\\char-class([range(53,53)]),\\char-class([range(44,44)]),\\char-class([range(57,57)]),\\char-class([range(53,53)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(57,57)]),\\char-class([range(55,55)]),\\char-class([range(44,44)]),\\char-class([range(49,49)]),\\char-class([range(50,50)]),\\char-class([range(50,50)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)]),\\char-class([range(41,41)]),\\char-class([range(125,125)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit_short__char_class___range__115_115_char_class___range__104_104_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"short\"),[\\char-class([range(115,115)]),\\char-class([range(104,104)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_signed__char_class___range__115_115_char_class___range__105_105_char_class___range__103_103_char_class___range__110_110_char_class___range__101_101_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"signed\"),[\\char-class([range(115,115)]),\\char-class([range(105,105)]),\\char-class([range(103,103)]),\\char-class([range(110,110)]),\\char-class([range(101,101)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__lit_sizeof__char_class___range__115_115_char_class___range__105_105_char_class___range__122_122_char_class___range__101_101_char_class___range__111_111_char_class___range__102_102_ = (IConstructor) _read("prod(lit(\"sizeof\"),[\\char-class([range(115,115)]),\\char-class([range(105,105)]),\\char-class([range(122,122)]),\\char-class([range(101,101)]),\\char-class([range(111,111)]),\\char-class([range(102,102)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_65_98_115_116_114_97_99_116_68_101_99_108_97_114_97_116_111_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__65_65_char_class___range__98_98_char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__116_116_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"AbstractDeclarator\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(65,65)]),\\char-class([range(98,98)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_65_110_111_110_121_109_111_117_115_73_100_101_110_116_105_102_105_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__65_65_char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__121_121_char_class___range__109_109_char_class___range__111_111_char_class___range__117_117_char_class___range__115_115_char_class___range__73_73_char_class___range__100_100_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"AnonymousIdentifier\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(65,65)]),\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(121,121)]),\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(117,117)]),\\char-class([range(115,115)]),\\char-class([range(73,73)]),\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_65_115_116_101_114_105_115_107_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__65_65_char_class___range__115_115_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__105_105_char_class___range__115_115_char_class___range__107_107_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"Asterisk\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(65,65)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(107,107)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__67_67_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"CharacterConstant\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(67,67)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__67_67_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"CharacterConstantContent\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(67,67)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_67_111_109_109_101_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__67_67_char_class___range__111_111_char_class___range__109_109_char_class___range__109_109_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"Comment\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_68_101_99_108_97_114_97_116_105_111_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"Declaration\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_68_101_99_108_97_114_97_116_111_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"Declarator\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_69_110_117_109_101_114_97_116_111_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_char_class___range__101_101_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"Enumerator\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(69,69)]),\\char-class([range(110,110)]),\\char-class([range(117,117)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_69_120_112_111_110_101_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__120_120_char_class___range__112_112_char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"Exponent\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(69,69)]),\\char-class([range(120,120)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_69_120_112_114_101_115_115_105_111_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__120_120_char_class___range__112_112_char_class___range__114_114_char_class___range__101_101_char_class___range__115_115_char_class___range__115_115_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"Expression\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(69,69)]),\\char-class([range(120,120)]),\\char-class([range(112,112)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_69_120_116_101_114_110_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"ExternalDeclaration\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(69,69)]),\\char-class([range(120,120)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(110,110)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_70_108_111_97_116_105_110_103_80_111_105_110_116_67_111_110_115_116_97_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__70_70_char_class___range__108_108_char_class___range__111_111_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_char_class___range__80_80_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"FloatingPointConstant\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(70,70)]),\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(80,80)]),\\char-class([range(111,111)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_70_117_110_99_116_105_111_110_68_101_102_105_110_105_116_105_111_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__70_70_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__68_68_char_class___range__101_101_char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__105_105_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"FunctionDefinition\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(70,70)]),\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_70_117_110_99_116_105_111_110_80_114_111_116_111_116_121_112_101_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__70_70_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__80_80_char_class___range__114_114_char_class___range__111_111_char_class___range__116_116_char_class___range__111_111_char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"FunctionPrototype\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(70,70)]),\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(80,80)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_71_108_111_98_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__71_71_char_class___range__108_108_char_class___range__111_111_char_class___range__98_98_char_class___range__97_97_char_class___range__108_108_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"GlobalDeclaration\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(71,71)]),\\char-class([range(108,108)]),\\char-class([range(111,111)]),\\char-class([range(98,98)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_72_101_120_97_100_101_99_105_109_97_108_67_111_110_115_116_97_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__72_72_char_class___range__101_101_char_class___range__120_120_char_class___range__97_97_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__105_105_char_class___range__109_109_char_class___range__97_97_char_class___range__108_108_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"HexadecimalConstant\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(72,72)]),\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(97,97)]),\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_73_100_101_110_116_105_102_105_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__73_73_char_class___range__100_100_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"Identifier\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(73,73)]),\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_73_110_105_116_68_101_99_108_97_114_97_116_111_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__73_73_char_class___range__110_110_char_class___range__105_105_char_class___range__116_116_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"InitDeclarator\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(73,73)]),\\char-class([range(110,110)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_73_110_105_116_105_97_108_105_122_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__73_73_char_class___range__110_110_char_class___range__105_105_char_class___range__116_116_char_class___range__105_105_char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__122_122_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"Initializer\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(73,73)]),\\char-class([range(110,110)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(122,122)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_73_110_116_101_103_101_114_67_111_110_115_116_97_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__73_73_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__103_103_char_class___range__101_101_char_class___range__114_114_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"IntegerConstant\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(73,73)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_76_65_89_79_85_84_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__76_76_char_class___range__65_65_char_class___range__89_89_char_class___range__79_79_char_class___range__85_85_char_class___range__84_84_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"LAYOUT\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(76,76)]),\\char-class([range(65,65)]),\\char-class([range(89,89)]),\\char-class([range(79,79)]),\\char-class([range(85,85)]),\\char-class([range(84,84)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_77_111_114_101_80_97_114_97_109_101_116_101_114_115_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__77_77_char_class___range__111_111_char_class___range__114_114_char_class___range__101_101_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__115_115_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"MoreParameters\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(77,77)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(80,80)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(115,115)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_77_117_108_116_105_76_105_110_101_67_111_109_109_101_110_116_66_111_100_121_84_111_107_101_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__77_77_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_char_class___range__105_105_char_class___range__76_76_char_class___range__105_105_char_class___range__110_110_char_class___range__101_101_char_class___range__67_67_char_class___range__111_111_char_class___range__109_109_char_class___range__109_109_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__66_66_char_class___range__111_111_char_class___range__100_100_char_class___range__121_121_char_class___range__84_84_char_class___range__111_111_char_class___range__107_107_char_class___range__101_101_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"MultiLineCommentBodyToken\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(77,77)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(76,76)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(101,101)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(66,66)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(121,121)]),\\char-class([range(84,84)]),\\char-class([range(111,111)]),\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_78_111_110_67_111_109_109_97_69_120_112_114_101_115_115_105_111_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__78_78_char_class___range__111_111_char_class___range__110_110_char_class___range__67_67_char_class___range__111_111_char_class___range__109_109_char_class___range__109_109_char_class___range__97_97_char_class___range__69_69_char_class___range__120_120_char_class___range__112_112_char_class___range__114_114_char_class___range__101_101_char_class___range__115_115_char_class___range__115_115_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"NonCommaExpression\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(78,78)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(109,109)]),\\char-class([range(109,109)]),\\char-class([range(97,97)]),\\char-class([range(69,69)]),\\char-class([range(120,120)]),\\char-class([range(112,112)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"Parameter\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(80,80)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_115_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__115_115_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"Parameters\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(80,80)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(115,115)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_68_101_99_108_97_114_97_116_111_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__114_114_char_class___range__111_111_char_class___range__116_116_char_class___range__111_111_char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"PrototypeDeclarator\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(80,80)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__114_114_char_class___range__111_111_char_class___range__116_116_char_class___range__111_111_char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"PrototypeParameter\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(80,80)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(80,80)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_115_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__114_114_char_class___range__111_111_char_class___range__116_116_char_class___range__111_111_char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__115_115_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"PrototypeParameters\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(80,80)]),\\char-class([range(114,114)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(116,116)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(80,80)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(115,115)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__112_112_char_class___range__101_101_char_class___range__99_99_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"Specifier\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(83,83)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(105,105)]),\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_83_116_97_116_101_109_101_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__109_109_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"Statement\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(83,83)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_83_116_111_114_97_103_101_67_108_97_115_115_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__97_97_char_class___range__103_103_char_class___range__101_101_char_class___range__67_67_char_class___range__108_108_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"StorageClass\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(83,83)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(67,67)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__114_114_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"StringConstant\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(83,83)]),\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__114_114_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"StringConstantContent\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(83,83)]),\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(67,67)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_105_111_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"StructDeclaration\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(83,83)]),\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(117,117)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_111_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"StructDeclarator\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(83,83)]),\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(117,117)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(68,68)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_84_114_97_110_115_108_97_116_105_111_110_85_110_105_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__84_84_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__115_115_char_class___range__108_108_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__85_85_char_class___range__110_110_char_class___range__105_105_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"TranslationUnit\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(84,84)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(85,85)]),\\char-class([range(110,110)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_84_121_112_101_78_97_109_101_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__84_84_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__78_78_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"TypeName\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(84,84)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(78,78)]),\\char-class([range(97,97)]),\\char-class([range(109,109)]),\\char-class([range(101,101)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_84_121_112_101_81_117_97_108_105_102_105_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__84_84_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__81_81_char_class___range__117_117_char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"TypeQualifier\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(84,84)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(81,81)]),\\char-class([range(117,117)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_84_121_112_101_83_112_101_99_105_102_105_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__84_84_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__83_83_char_class___range__112_112_char_class___range__101_101_char_class___range__99_99_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"TypeSpecifier\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(84,84)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(83,83)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(105,105)]),\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit_static__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__99_99_ = (IConstructor) _read("prod(lit(\"static\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(99,99)])],{})", Factory.Production);
  private static final IConstructor prod__lit_struct__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_ = (IConstructor) _read("prod(lit(\"struct\"),[\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(117,117)]),\\char-class([range(99,99)]),\\char-class([range(116,116)])],{})", Factory.Production);
  private static final IConstructor prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_ = (IConstructor) _read("prod(lit(\"switch\"),[\\char-class([range(115,115)]),\\char-class([range(119,119)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(99,99)]),\\char-class([range(104,104)])],{})", Factory.Production);
  private static final IConstructor prod__lit_typedef__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_ = (IConstructor) _read("prod(lit(\"typedef\"),[\\char-class([range(116,116)]),\\char-class([range(121,121)]),\\char-class([range(112,112)]),\\char-class([range(101,101)]),\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(102,102)])],{})", Factory.Production);
  private static final IConstructor prod__lit_union__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_ = (IConstructor) _read("prod(lit(\"union\"),[\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)])],{})", Factory.Production);
  private static final IConstructor prod__lit_unsigned__char_class___range__117_117_char_class___range__110_110_char_class___range__115_115_char_class___range__105_105_char_class___range__103_103_char_class___range__110_110_char_class___range__101_101_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"unsigned\"),[\\char-class([range(117,117)]),\\char-class([range(110,110)]),\\char-class([range(115,115)]),\\char-class([range(105,105)]),\\char-class([range(103,103)]),\\char-class([range(110,110)]),\\char-class([range(101,101)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"void\"),[\\char-class([range(118,118)]),\\char-class([range(111,111)]),\\char-class([range(105,105)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__lit_volatile__char_class___range__118_118_char_class___range__111_111_char_class___range__108_108_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"volatile\"),[\\char-class([range(118,118)]),\\char-class([range(111,111)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_ = (IConstructor) _read("prod(lit(\"while\"),[\\char-class([range(119,119)]),\\char-class([range(104,104)]),\\char-class([range(105,105)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],{})", Factory.Production);
  private static final IConstructor prod__lit___123__char_class___range__123_123_ = (IConstructor) _read("prod(lit(\"{\"),[\\char-class([range(123,123)])],{})", Factory.Production);
  private static final IConstructor prod__lit___124__char_class___range__124_124_ = (IConstructor) _read("prod(lit(\"|\"),[\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_ = (IConstructor) _read("prod(lit(\"||\"),[\\char-class([range(124,124)]),\\char-class([range(124,124)])],{})", Factory.Production);
  private static final IConstructor prod__lit___125__char_class___range__125_125_ = (IConstructor) _read("prod(lit(\"}\"),[\\char-class([range(125,125)])],{})", Factory.Production);
  private static final IConstructor prod__lit___126__char_class___range__126_126_ = (IConstructor) _read("prod(lit(\"~\"),[\\char-class([range(126,126)])],{})", Factory.Production);
  private static final IConstructor prod__AnonymousIdentifier__ = (IConstructor) _read("prod(sort(\"AnonymousIdentifier\"),[],{})", Factory.Production);
  private static final IConstructor prod__Enumerator__Identifier_ = (IConstructor) _read("prod(sort(\"Enumerator\"),[lex(\"Identifier\")],{})", Factory.Production);
  private static final IConstructor prod__Enumerator__Identifier_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_NonCommaExpression_ = (IConstructor) _read("prod(sort(\"Enumerator\"),[lex(\"Identifier\"),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),sort(\"NonCommaExpression\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"\\>\\>\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__lit___38_layouts_LAYOUTLIST_Expression_ = (IConstructor) _read("prod(sort(\"Expression\"),[lit(\"&\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"&&\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__CharacterConstant__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(sort(\"Expression\"),[lex(\"CharacterConstant\")],{tag(\"category\"(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Expression__lit___126_layouts_LAYOUTLIST_Expression_ = (IConstructor) _read("prod(sort(\"Expression\"),[lit(\"~\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___47_61_layouts_LAYOUTLIST_Expression__assoc__right = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"/=\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Expression__assoc__right = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"?\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___42_61_layouts_LAYOUTLIST_Expression__assoc__right = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"*=\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__Expression__lit___33_layouts_LAYOUTLIST_Expression_ = (IConstructor) _read("prod(sort(\"Expression\"),[lit(\"!\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___93_ = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"]\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__IntegerConstant__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(sort(\"Expression\"),[lex(\"IntegerConstant\")],{tag(\"category\"(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"||\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___38_61_layouts_LAYOUTLIST_Expression__assoc__right = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"&=\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_61_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"\\<=\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__lit___43_43_layouts_LAYOUTLIST_Expression_ = (IConstructor) _read("prod(sort(\"Expression\"),[conditional(lit(\"++\"),{\\not-precede(\\char-class([range(43,43)]))}),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__StringConstant__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(sort(\"Expression\"),[lex(\"StringConstant\")],{tag(\"category\"(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit____ = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"--\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Identifier_ = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"-\\>\"),layouts(\"LAYOUTLIST\"),lex(\"Identifier\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__lit____layouts_LAYOUTLIST_Expression_ = (IConstructor) _read("prod(sort(\"Expression\"),[conditional(lit(\"--\"),{\\not-precede(\\char-class([range(45,45)]))}),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__lit___42_layouts_LAYOUTLIST_Expression_ = (IConstructor) _read("prod(sort(\"Expression\"),[lit(\"*\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__lit___43_layouts_LAYOUTLIST_Expression_ = (IConstructor) _read("prod(sort(\"Expression\"),[lit(\"+\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___61_61_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"==\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___43_61_layouts_LAYOUTLIST_Expression__assoc__right = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"+=\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__Expression__lit___40_layouts_LAYOUTLIST_TypeName_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Expression_ = (IConstructor) _read("prod(sort(\"Expression\"),[lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"TypeName\"),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___94_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"^\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"\\<\\<\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_62_61_layouts_LAYOUTLIST_Expression__assoc__right = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"\\>\\>=\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__Expression__FloatingPointConstant__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(sort(\"Expression\"),[lex(\"FloatingPointConstant\")],{tag(\"category\"(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Expression__lit___layouts_LAYOUTLIST_Expression_ = (IConstructor) _read("prod(sort(\"Expression\"),[lit(\"-\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"\\>=\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__lit_sizeof_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_TypeName_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(sort(\"Expression\"),[lit(\"sizeof\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"TypeName\"),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_Expression__assoc__right = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___45_61_layouts_LAYOUTLIST_Expression__assoc__right = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"-=\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_Identifier_ = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),lex(\"Identifier\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___10_9_32_32_32_32_32_32_32_32_124_32_61_layouts_LAYOUTLIST_Expression__assoc__right = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"\\n\\t        | =\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_60_61_layouts_LAYOUTLIST_Expression__assoc__right = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"\\<\\<=\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___37_61_layouts_LAYOUTLIST_Expression__assoc__right = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"%=\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__Expression__HexadecimalConstant__tag__category___67_111_110_115_116_97_110_116 = (IConstructor) _read("prod(sort(\"Expression\"),[lex(\"HexadecimalConstant\")],{tag(\"category\"(\"Constant\"))})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___43_43_ = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"++\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___43_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"+\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__NonCommaExpression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"NonCommaExpression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"/\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___94_61_layouts_LAYOUTLIST_Expression__assoc__right = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"^=\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(right())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"\\<\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"\\>\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"%\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"!=\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__Expression__Expression_layouts_LAYOUTLIST_lit___38_layouts_LAYOUTLIST_Expression__assoc__left = (IConstructor) _read("prod(sort(\"Expression\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\"&\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{assoc(left())})", Factory.Production);
  private static final IConstructor prod__ExternalDeclaration__FunctionDefinition_ = (IConstructor) _read("prod(sort(\"ExternalDeclaration\"),[sort(\"FunctionDefinition\")],{})", Factory.Production);
  private static final IConstructor prod__ExternalDeclaration__FunctionPrototype_ = (IConstructor) _read("prod(sort(\"ExternalDeclaration\"),[sort(\"FunctionPrototype\")],{})", Factory.Production);
  private static final IConstructor prod__ExternalDeclaration__GlobalDeclaration_ = (IConstructor) _read("prod(sort(\"ExternalDeclaration\"),[sort(\"GlobalDeclaration\")],{})", Factory.Production);
  private static final IConstructor prod__InitDeclarator__decl_Declarator_ = (IConstructor) _read("prod(sort(\"InitDeclarator\"),[label(\"decl\",sort(\"Declarator\"))],{})", Factory.Production);
  private static final IConstructor prod__InitDeclarator__decl_Declarator_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_Initializer_ = (IConstructor) _read("prod(sort(\"InitDeclarator\"),[label(\"decl\",sort(\"Declarator\")),layouts(\"LAYOUTLIST\"),lit(\"=\"),layouts(\"LAYOUTLIST\"),sort(\"Initializer\")],{})", Factory.Production);
  private static final IConstructor prod__Initializer__lit___123_layouts_LAYOUTLIST_iter_seps__Initializer__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_opt__lit___44_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(sort(\"Initializer\"),[lit(\"{\"),layouts(\"LAYOUTLIST\"),\\iter-seps(sort(\"Initializer\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),opt(lit(\",\")),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__Initializer__NonCommaExpression_ = (IConstructor) _read("prod(sort(\"Initializer\"),[sort(\"NonCommaExpression\")],{})", Factory.Production);
  private static final IConstructor prod__MoreParameters__lit___44_layouts_LAYOUTLIST_lit___46_46_46_ = (IConstructor) _read("prod(sort(\"MoreParameters\"),[lit(\",\"),layouts(\"LAYOUTLIST\"),lit(\"...\")],{})", Factory.Production);
  private static final IConstructor prod__Parameter__iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Declarator_ = (IConstructor) _read("prod(sort(\"Parameter\"),[\\iter-star-seps(sort(\"Specifier\"),[layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),sort(\"Declarator\")],{})", Factory.Production);
  private static final IConstructor prod__Parameters__iter_seps__Parameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_opt__MoreParameters_ = (IConstructor) _read("prod(sort(\"Parameters\"),[\\iter-seps(sort(\"Parameter\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),opt(sort(\"MoreParameters\"))],{})", Factory.Production);
  private static final IConstructor prod__Parameters__lit_void_ = (IConstructor) _read("prod(sort(\"Parameters\"),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__PrototypeParameter__iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_AbstractDeclarator_ = (IConstructor) _read("prod(sort(\"PrototypeParameter\"),[\\iter-star-seps(sort(\"Specifier\"),[layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),sort(\"AbstractDeclarator\")],{})", Factory.Production);
  private static final IConstructor prod__PrototypeParameters__lit_void_ = (IConstructor) _read("prod(sort(\"PrototypeParameters\"),[lit(\"void\")],{})", Factory.Production);
  private static final IConstructor prod__PrototypeParameters__iter_seps__PrototypeParameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_opt__MoreParameters_ = (IConstructor) _read("prod(sort(\"PrototypeParameters\"),[\\iter-seps(sort(\"PrototypeParameter\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),opt(sort(\"MoreParameters\"))],{})", Factory.Production);
  private static final IConstructor prod__Statement__lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_opt__Expression_layouts_LAYOUTLIST_lit___59_layouts_LAYOUTLIST_opt__Expression_layouts_LAYOUTLIST_lit___59_layouts_LAYOUTLIST_opt__Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_ = (IConstructor) _read("prod(sort(\"Statement\"),[lit(\"for\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),opt(sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\"),layouts(\"LAYOUTLIST\"),opt(sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\";\"),layouts(\"LAYOUTLIST\"),opt(sort(\"Expression\")),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),sort(\"Statement\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__lit_switch_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_ = (IConstructor) _read("prod(sort(\"Statement\"),[lit(\"switch\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),sort(\"Statement\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__lit_continue_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(sort(\"Statement\"),[lit(\"continue\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__lit_goto_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(sort(\"Statement\"),[lit(\"goto\"),layouts(\"LAYOUTLIST\"),lex(\"Identifier\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__lit_case_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Statement_ = (IConstructor) _read("prod(sort(\"Statement\"),[lit(\"case\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),sort(\"Statement\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__Identifier_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Statement_ = (IConstructor) _read("prod(sort(\"Statement\"),[lex(\"Identifier\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),sort(\"Statement\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__lit_return_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(sort(\"Statement\"),[lit(\"return\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_ = (IConstructor) _read("prod(sort(\"Statement\"),[lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),sort(\"Statement\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__Expression_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(sort(\"Statement\"),[sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__lit___59_ = (IConstructor) _read("prod(sort(\"Statement\"),[lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__lit_break_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(sort(\"Statement\"),[lit(\"break\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Statement_ = (IConstructor) _read("prod(sort(\"Statement\"),[lit(\"default\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),sort(\"Statement\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__lit_return_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(sort(\"Statement\"),[lit(\"return\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_Statement_ = (IConstructor) _read("prod(sort(\"Statement\"),[lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),sort(\"Statement\"),layouts(\"LAYOUTLIST\"),lit(\"else\"),layouts(\"LAYOUTLIST\"),sort(\"Statement\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__lit_do_layouts_LAYOUTLIST_Statement_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___59_ = (IConstructor) _read("prod(sort(\"Statement\"),[lit(\"do\"),layouts(\"LAYOUTLIST\"),sort(\"Statement\"),layouts(\"LAYOUTLIST\"),lit(\"while\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),lit(\";\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_ = (IConstructor) _read("prod(sort(\"Statement\"),[lit(\"if\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\"),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),sort(\"Statement\")],{})", Factory.Production);
  private static final IConstructor prod__Statement__lit___123_layouts_LAYOUTLIST_iter_star_seps__Declaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_ = (IConstructor) _read("prod(sort(\"Statement\"),[lit(\"{\"),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"Declaration\"),[layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"}\")],{})", Factory.Production);
  private static final IConstructor prod__StorageClass__lit_static_ = (IConstructor) _read("prod(sort(\"StorageClass\"),[lit(\"static\")],{})", Factory.Production);
  private static final IConstructor prod__StorageClass__lit_auto_ = (IConstructor) _read("prod(sort(\"StorageClass\"),[lit(\"auto\")],{})", Factory.Production);
  private static final IConstructor prod__StorageClass__lit_register_ = (IConstructor) _read("prod(sort(\"StorageClass\"),[lit(\"register\")],{})", Factory.Production);
  private static final IConstructor prod__StorageClass__lit_extern_ = (IConstructor) _read("prod(sort(\"StorageClass\"),[lit(\"extern\")],{})", Factory.Production);
  private static final IConstructor prod__StructDeclarator__opt__Declarator_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Expression_ = (IConstructor) _read("prod(sort(\"StructDeclarator\"),[opt(sort(\"Declarator\")),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),sort(\"Expression\")],{})", Factory.Production);
  private static final IConstructor prod__StructDeclarator__Declarator_ = (IConstructor) _read("prod(sort(\"StructDeclarator\"),[sort(\"Declarator\")],{})", Factory.Production);
  private static final IConstructor prod__TranslationUnit__iter_seps__ExternalDeclaration__layouts_LAYOUTLIST_ = (IConstructor) _read("prod(sort(\"TranslationUnit\"),[\\iter-seps(sort(\"ExternalDeclaration\"),[layouts(\"LAYOUTLIST\")])],{})", Factory.Production);
  private static final IConstructor prod__TypeName__iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_AbstractDeclarator_ = (IConstructor) _read("prod(sort(\"TypeName\"),[\\iter-seps(sort(\"Specifier\"),[layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),sort(\"AbstractDeclarator\")],{})", Factory.Production);
  private static final IConstructor prod__TypeQualifier__lit_volatile_ = (IConstructor) _read("prod(sort(\"TypeQualifier\"),[lit(\"volatile\")],{})", Factory.Production);
  private static final IConstructor prod__TypeQualifier__lit_const_ = (IConstructor) _read("prod(sort(\"TypeQualifier\"),[lit(\"const\")],{})", Factory.Production);
  private static final IConstructor prod__TypeSpecifier__lit_unsigned_ = (IConstructor) _read("prod(sort(\"TypeSpecifier\"),[lit(\"unsigned\")],{})", Factory.Production);
  private static final IConstructor prod__TypeSpecifier__lit_signed_ = (IConstructor) _read("prod(sort(\"TypeSpecifier\"),[lit(\"signed\")],{})", Factory.Production);
  private static final IConstructor prod__start__TranslationUnit__layouts_LAYOUTLIST_top_TranslationUnit_layouts_LAYOUTLIST_ = (IConstructor) _read("prod(start(sort(\"TranslationUnit\")),[layouts(\"LAYOUTLIST\"),label(\"top\",sort(\"TranslationUnit\")),layouts(\"LAYOUTLIST\")],{})", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_57 = (IConstructor) _read("regular(iter(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_57_range__65_70_range__97_102 = (IConstructor) _read("regular(iter(\\char-class([range(48,57),range(65,70),range(97,102)])))", Factory.Production);
  private static final IConstructor regular__iter__CharacterConstantContent = (IConstructor) _read("regular(iter(lex(\"CharacterConstantContent\")))", Factory.Production);
  private static final IConstructor regular__iter_seps__Enumerator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Enumerator\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__ExternalDeclaration__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"ExternalDeclaration\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__InitDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"InitDeclarator\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Initializer__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Initializer\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Parameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Parameter\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__PrototypeParameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"PrototypeParameter\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__Specifier__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"Specifier\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_seps__StructDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-seps(sort(\"StructDeclarator\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__48_57 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(48,57)])))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__1_9_range__11_16777215 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(1,9),range(11,16777215)])))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])))", Factory.Production);
  private static final IConstructor regular__iter_star__char_class___range__76_76_range__85_85_range__108_108_range__117_117 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(76,76),range(85,85),range(108,108),range(117,117)])))", Factory.Production);
  private static final IConstructor regular__iter_star__LAYOUT = (IConstructor) _read("regular(\\iter-star(lex(\"LAYOUT\")))", Factory.Production);
  private static final IConstructor regular__iter_star__MultiLineCommentBodyToken = (IConstructor) _read("regular(\\iter-star(lex(\"MultiLineCommentBodyToken\")))", Factory.Production);
  private static final IConstructor regular__iter_star__StringConstantContent = (IConstructor) _read("regular(\\iter-star(lex(\"StringConstantContent\")))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Declaration__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Declaration\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__NonCommaExpression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"NonCommaExpression\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Specifier__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Specifier\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__Statement__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Statement\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__StructDeclaration__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"StructDeclaration\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__iter_star_seps__TypeQualifier__layouts_LAYOUTLIST = (IConstructor) _read("regular(\\iter-star-seps(sort(\"TypeQualifier\"),[layouts(\"LAYOUTLIST\")]))", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__76_76 = (IConstructor) _read("regular(opt(\\char-class([range(76,76)])))", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108 = (IConstructor) _read("regular(opt(\\char-class([range(70,70),range(76,76),range(102,102),range(108,108)])))", Factory.Production);
  private static final IConstructor regular__opt__char_class___range__43_43_range__45_45 = (IConstructor) _read("regular(opt(\\char-class([range(43,43),range(45,45)])))", Factory.Production);
  private static final IConstructor regular__opt__Exponent = (IConstructor) _read("regular(opt(lex(\"Exponent\")))", Factory.Production);
  private static final IConstructor regular__opt__lit___44 = (IConstructor) _read("regular(opt(lit(\",\")))", Factory.Production);
  private static final IConstructor regular__opt__Declarator = (IConstructor) _read("regular(opt(sort(\"Declarator\")))", Factory.Production);
  private static final IConstructor regular__opt__Expression = (IConstructor) _read("regular(opt(sort(\"Expression\")))", Factory.Production);
  private static final IConstructor regular__opt__MoreParameters = (IConstructor) _read("regular(opt(sort(\"MoreParameters\")))", Factory.Production);
  private static final IConstructor regular__opt__Parameters = (IConstructor) _read("regular(opt(sort(\"Parameters\")))", Factory.Production);
  private static final IConstructor regular__opt__PrototypeParameters = (IConstructor) _read("regular(opt(sort(\"PrototypeParameters\")))", Factory.Production);
  private static final IConstructor regular__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122 = (IConstructor) _read("regular(seq([\\char-class([range(65,90),range(95,95),range(97,122)]),conditional(\\iter-star(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])),{\\not-follow(\\char-class([range(48,57),range(65,90),range(95,95),range(97,122)]))})]))", Factory.Production);
    
  // Item declarations
	
	
  protected static class Keyword {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__Keyword__lit_default_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2398, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_default_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_switch_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2396, 0, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {115,119,105,116,99,104}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_switch_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_long_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2402, 0, prod__lit_long__char_class___range__108_108_char_class___range__111_111_char_class___range__110_110_char_class___range__103_103_, new int[] {108,111,110,103}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_long_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_float_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2400, 0, prod__lit_float__char_class___range__102_102_char_class___range__108_108_char_class___range__111_111_char_class___range__97_97_char_class___range__116_116_, new int[] {102,108,111,97,116}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_float_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_struct_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2390, 0, prod__lit_struct__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_, new int[] {115,116,114,117,99,116}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_struct_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_double_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2388, 0, prod__lit_double__char_class___range__100_100_char_class___range__111_111_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_, new int[] {100,111,117,98,108,101}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_double_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_while_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2394, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_while_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_case_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2392, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new int[] {99,97,115,101}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_case_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_if_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2414, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_if_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_enum_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2412, 0, prod__lit_enum__char_class___range__101_101_char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new int[] {101,110,117,109}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_enum_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_else_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2418, 0, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_else_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_return_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2416, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new int[] {114,101,116,117,114,110}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_return_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_sizeof_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2406, 0, prod__lit_sizeof__char_class___range__115_115_char_class___range__105_105_char_class___range__122_122_char_class___range__101_101_char_class___range__111_111_char_class___range__102_102_, new int[] {115,105,122,101,111,102}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_sizeof_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_static_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2404, 0, prod__lit_static__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__99_99_, new int[] {115,116,97,116,105,99}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_static_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_void_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2410, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new int[] {118,111,105,100}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_void_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_volatile_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2408, 0, prod__lit_volatile__char_class___range__118_118_char_class___range__111_111_char_class___range__108_108_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {118,111,108,97,116,105,108,101}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_volatile_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_typedef_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2366, 0, prod__lit_typedef__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_, new int[] {116,121,112,101,100,101,102}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_typedef_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_const_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2364, 0, prod__lit_const__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_, new int[] {99,111,110,115,116}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_const_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_union_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2370, 0, prod__lit_union__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_, new int[] {117,110,105,111,110}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_union_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_register_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2368, 0, prod__lit_register__char_class___range__114_114_char_class___range__101_101_char_class___range__103_103_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new int[] {114,101,103,105,115,116,101,114}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_register_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_int_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2358, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new int[] {105,110,116}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_int_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_extern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2356, 0, prod__lit_extern__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__110_110_, new int[] {101,120,116,101,114,110}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_extern_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_char_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2362, 0, prod__lit_char__char_class___range__99_99_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_, new int[] {99,104,97,114}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_char_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_signed_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2360, 0, prod__lit_signed__char_class___range__115_115_char_class___range__105_105_char_class___range__103_103_char_class___range__110_110_char_class___range__101_101_char_class___range__100_100_, new int[] {115,105,103,110,101,100}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_signed_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_for_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2382, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_for_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_continue_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2380, 0, prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_, new int[] {99,111,110,116,105,110,117,101}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_continue_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_short_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2386, 0, prod__lit_short__char_class___range__115_115_char_class___range__104_104_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {115,104,111,114,116}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_short_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_break_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2384, 0, prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,114,101,97,107}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_break_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_auto_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2374, 0, prod__lit_auto__char_class___range__97_97_char_class___range__117_117_char_class___range__116_116_char_class___range__111_111_, new int[] {97,117,116,111}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_auto_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_unsigned_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2372, 0, prod__lit_unsigned__char_class___range__117_117_char_class___range__110_110_char_class___range__115_115_char_class___range__105_105_char_class___range__103_103_char_class___range__110_110_char_class___range__101_101_char_class___range__100_100_, new int[] {117,110,115,105,103,110,101,100}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_unsigned_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_do_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2378, 0, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new int[] {100,111}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_do_, tmp);
	}
    protected static final void _init_prod__Keyword__lit_goto_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2376, 0, prod__lit_goto__char_class___range__103_103_char_class___range__111_111_char_class___range__116_116_char_class___range__111_111_, new int[] {103,111,116,111}, null, null);
      builder.addAlternative(CParser.prod__Keyword__lit_goto_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__Keyword__lit_default_(builder);
      
        _init_prod__Keyword__lit_switch_(builder);
      
        _init_prod__Keyword__lit_long_(builder);
      
        _init_prod__Keyword__lit_float_(builder);
      
        _init_prod__Keyword__lit_struct_(builder);
      
        _init_prod__Keyword__lit_double_(builder);
      
        _init_prod__Keyword__lit_while_(builder);
      
        _init_prod__Keyword__lit_case_(builder);
      
        _init_prod__Keyword__lit_if_(builder);
      
        _init_prod__Keyword__lit_enum_(builder);
      
        _init_prod__Keyword__lit_else_(builder);
      
        _init_prod__Keyword__lit_return_(builder);
      
        _init_prod__Keyword__lit_sizeof_(builder);
      
        _init_prod__Keyword__lit_static_(builder);
      
        _init_prod__Keyword__lit_void_(builder);
      
        _init_prod__Keyword__lit_volatile_(builder);
      
        _init_prod__Keyword__lit_typedef_(builder);
      
        _init_prod__Keyword__lit_const_(builder);
      
        _init_prod__Keyword__lit_union_(builder);
      
        _init_prod__Keyword__lit_register_(builder);
      
        _init_prod__Keyword__lit_int_(builder);
      
        _init_prod__Keyword__lit_extern_(builder);
      
        _init_prod__Keyword__lit_char_(builder);
      
        _init_prod__Keyword__lit_signed_(builder);
      
        _init_prod__Keyword__lit_for_(builder);
      
        _init_prod__Keyword__lit_continue_(builder);
      
        _init_prod__Keyword__lit_short_(builder);
      
        _init_prod__Keyword__lit_break_(builder);
      
        _init_prod__Keyword__lit_auto_(builder);
      
        _init_prod__Keyword__lit_unsigned_(builder);
      
        _init_prod__Keyword__lit_do_(builder);
      
        _init_prod__Keyword__lit_goto_(builder);
      
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
      
      tmp[0] = new EpsilonStackNode<IConstructor>(520, 0);
      builder.addAlternative(CParser.prod__layouts_$default$__, tmp);
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
      
      tmp[0] = new ListStackNode<IConstructor>(2073, 0, regular__iter_star__LAYOUT, new NonTerminalStackNode<IConstructor>(2070, 0, "LAYOUT", null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{9,10},{13,13},{32,32}})});
      builder.addAlternative(CParser.prod__layouts_LAYOUTLIST__iter_star__LAYOUT_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__layouts_LAYOUTLIST__iter_star__LAYOUT_(builder);
      
    }
  }
	
  protected static class Asterisk {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_Asterisk__char_class___range__0_0_lit___115_111_114_116_40_34_65_115_116_101_114_105_115_107_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Asterisk(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(644, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(641, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(639, 0, new int[][]{{0,0}}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(643, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(642, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(640, 1, prod__lit___115_111_114_116_40_34_65_115_116_101_114_105_115_107_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__65_65_char_class___range__115_115_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__105_105_char_class___range__115_115_char_class___range__107_107_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,65,115,116,101,114,105,115,107,34,41}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Asterisk__char_class___range__0_0_lit___115_111_114_116_40_34_65_115_116_101_114_105_115_107_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Asterisk, tmp);
	}
    protected static final void _init_prod__Asterisk__char_class___range__42_42_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(636, 0, new int[][]{{42,42}}, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{47,47}})});
      builder.addAlternative(CParser.prod__Asterisk__char_class___range__42_42_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_Asterisk__char_class___range__0_0_lit___115_111_114_116_40_34_65_115_116_101_114_105_115_107_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Asterisk(builder);
      
        _init_prod__Asterisk__char_class___range__42_42_(builder);
      
    }
  }
	
  protected static class CharacterConstant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_CharacterConstant__char_class___range__0_0_lit___115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__CharacterConstant(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(4410, 1, prod__lit___115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__67_67_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,67,104,97,114,97,99,116,101,114,67,111,110,115,116,97,110,116,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(4413, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4412, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4411, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4414, 4, new int[][]{{0,0}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4409, 0, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_CharacterConstant__char_class___range__0_0_lit___115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__CharacterConstant, tmp);
	}
    protected static final void _init_prod__CharacterConstant__opt__char_class___range__76_76_char_class___range__39_39_iter__CharacterConstantContent_char_class___range__39_39_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[1] = new CharStackNode<IConstructor>(4419, 1, new int[][]{{39,39}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(4422, 3, new int[][]{{39,39}}, null, null);
      tmp[0] = new OptionalStackNode<IConstructor>(4418, 0, regular__opt__char_class___range__76_76, new CharStackNode<IConstructor>(4417, 0, new int[][]{{76,76}}, null, null), null, null);
      tmp[2] = new ListStackNode<IConstructor>(4421, 2, regular__iter__CharacterConstantContent, new NonTerminalStackNode<IConstructor>(4420, 0, "CharacterConstantContent", null, null), true, null, null);
      builder.addAlternative(CParser.prod__CharacterConstant__opt__char_class___range__76_76_char_class___range__39_39_iter__CharacterConstantContent_char_class___range__39_39_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_CharacterConstant__char_class___range__0_0_lit___115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__CharacterConstant(builder);
      
        _init_prod__CharacterConstant__opt__char_class___range__76_76_char_class___range__39_39_iter__CharacterConstantContent_char_class___range__39_39_(builder);
      
    }
  }
	
  protected static class CharacterConstantContent {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_CharacterConstantContent__char_class___range__0_0_lit___115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__CharacterConstantContent(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(4598, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4603, 4, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(4599, 1, prod__lit___115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__67_67_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,67,104,97,114,97,99,116,101,114,67,111,110,115,116,97,110,116,67,111,110,116,101,110,116,34,41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4600, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(4602, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4601, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_CharacterConstantContent__char_class___range__0_0_lit___115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__CharacterConstantContent, tmp);
	}
    protected static final void _init_prod__$MetaHole_CharacterConstantContent__char_class___range__0_0_lit___105_116_101_114_40_115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter__CharacterConstantContent(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(4590, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4589, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(4587, 1, prod__lit___105_116_101_114_40_115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_41__char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__67_67_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {105,116,101,114,40,115,111,114,116,40,34,67,104,97,114,97,99,116,101,114,67,111,110,115,116,97,110,116,67,111,110,116,101,110,116,34,41,41}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4586, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4591, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4588, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_CharacterConstantContent__char_class___range__0_0_lit___105_116_101_114_40_115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter__CharacterConstantContent, tmp);
	}
    protected static final void _init_prod__CharacterConstantContent__char_class___range__1_38_range__40_91_range__93_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(4595, 0, new int[][]{{1,38},{40,91},{93,16777215}}, null, null);
      builder.addAlternative(CParser.prod__CharacterConstantContent__char_class___range__1_38_range__40_91_range__93_16777215_, tmp);
	}
    protected static final void _init_prod__CharacterConstantContent__char_class___range__92_92_char_class___range__1_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[0] = new CharStackNode<IConstructor>(4606, 0, new int[][]{{92,92}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(4607, 1, new int[][]{{1,16777215}}, null, null);
      builder.addAlternative(CParser.prod__CharacterConstantContent__char_class___range__92_92_char_class___range__1_16777215_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_CharacterConstantContent__char_class___range__0_0_lit___115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__CharacterConstantContent(builder);
      
        _init_prod__$MetaHole_CharacterConstantContent__char_class___range__0_0_lit___105_116_101_114_40_115_111_114_116_40_34_67_104_97_114_97_99_116_101_114_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter__CharacterConstantContent(builder);
      
        _init_prod__CharacterConstantContent__char_class___range__1_38_range__40_91_range__93_16777215_(builder);
      
        _init_prod__CharacterConstantContent__char_class___range__92_92_char_class___range__1_16777215_(builder);
      
    }
  }
	
  protected static class Comment {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_Comment__char_class___range__0_0_lit___115_111_114_116_40_34_67_111_109_109_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Comment(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(2112, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2114, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(2117, 4, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(2113, 1, prod__lit___115_111_114_116_40_34_67_111_109_109_101_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__67_67_char_class___range__111_111_char_class___range__109_109_char_class___range__109_109_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,67,111,109,109,101,110,116,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(2116, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(2115, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Comment__char_class___range__0_0_lit___115_111_114_116_40_34_67_111_109_109_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Comment, tmp);
	}
    protected static final void _init_prod__Comment__char_class___range__47_47_char_class___range__42_42_iter_star__MultiLineCommentBodyToken_char_class___range__42_42_char_class___range__47_47_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(2120, 0, new int[][]{{47,47}}, null, null);
      tmp[2] = new ListStackNode<IConstructor>(2123, 2, regular__iter_star__MultiLineCommentBodyToken, new NonTerminalStackNode<IConstructor>(2122, 0, "MultiLineCommentBodyToken", null, null), false, null, null);
      tmp[4] = new CharStackNode<IConstructor>(2125, 4, new int[][]{{47,47}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(2121, 1, new int[][]{{42,42}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(2124, 3, new int[][]{{42,42}}, null, null);
      builder.addAlternative(CParser.prod__Comment__char_class___range__47_47_char_class___range__42_42_iter_star__MultiLineCommentBodyToken_char_class___range__42_42_char_class___range__47_47_, tmp);
	}
    protected static final void _init_prod__Comment__lit___47_47_iter_star__char_class___range__1_9_range__11_16777215_char_class___range__10_10_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new CharStackNode<IConstructor>(2109, 2, new int[][]{{10,10}}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(2106, 0, prod__lit___47_47__char_class___range__47_47_char_class___range__47_47_, new int[] {47,47}, null, null);
      tmp[1] = new ListStackNode<IConstructor>(2108, 1, regular__iter_star__char_class___range__1_9_range__11_16777215, new CharStackNode<IConstructor>(2107, 0, new int[][]{{1,9},{11,16777215}}, null, null), false, null, null);
      builder.addAlternative(CParser.prod__Comment__lit___47_47_iter_star__char_class___range__1_9_range__11_16777215_char_class___range__10_10_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_Comment__char_class___range__0_0_lit___115_111_114_116_40_34_67_111_109_109_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Comment(builder);
      
        _init_prod__Comment__char_class___range__47_47_char_class___range__42_42_iter_star__MultiLineCommentBodyToken_char_class___range__42_42_char_class___range__47_47_(builder);
      
        _init_prod__Comment__lit___47_47_iter_star__char_class___range__1_9_range__11_16777215_char_class___range__10_10_(builder);
      
    }
  }
	
  protected static class Exponent {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_Exponent__char_class___range__0_0_lit___115_111_114_116_40_34_69_120_112_111_110_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Exponent(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(5262, 0, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(5263, 1, prod__lit___115_111_114_116_40_34_69_120_112_111_110_101_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__120_120_char_class___range__112_112_char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,69,120,112,111,110,101,110,116,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(5266, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(5265, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5264, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(5267, 4, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Exponent__char_class___range__0_0_lit___115_111_114_116_40_34_69_120_112_111_110_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Exponent, tmp);
	}
    protected static final void _init_prod__$MetaHole_Exponent__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_69_120_112_111_110_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Exponent(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(5271, 0, new int[][]{{0,0}}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(5275, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(5274, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(5272, 1, prod__lit___111_112_116_40_115_111_114_116_40_34_69_120_112_111_110_101_110_116_34_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__120_120_char_class___range__112_112_char_class___range__111_111_char_class___range__110_110_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {111,112,116,40,115,111,114,116,40,34,69,120,112,111,110,101,110,116,34,41,41}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(5276, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5273, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Exponent__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_69_120_112_111_110_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Exponent, tmp);
	}
    protected static final void _init_prod__Exponent__char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new OptionalStackNode<IConstructor>(5255, 1, regular__opt__char_class___range__43_43_range__45_45, new CharStackNode<IConstructor>(5254, 0, new int[][]{{43,43},{45,45}}, null, null), null, null);
      tmp[0] = new CharStackNode<IConstructor>(5253, 0, new int[][]{{69,69},{101,101}}, null, null);
      tmp[2] = new ListStackNode<IConstructor>(5259, 2, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(5256, 0, new int[][]{{48,57}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57}})});
      builder.addAlternative(CParser.prod__Exponent__char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_Exponent__char_class___range__0_0_lit___115_111_114_116_40_34_69_120_112_111_110_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Exponent(builder);
      
        _init_prod__$MetaHole_Exponent__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_69_120_112_111_110_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Exponent(builder);
      
        _init_prod__Exponent__char_class___range__69_69_range__101_101_opt__char_class___range__43_43_range__45_45_iter__char_class___range__48_57_(builder);
      
    }
  }
	
  protected static class FloatingPointConstant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_FloatingPointConstant__char_class___range__0_0_lit___115_111_114_116_40_34_70_108_111_97_116_105_110_103_80_111_105_110_116_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__FloatingPointConstant(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(5418, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(5417, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(5415, 1, prod__lit___115_111_114_116_40_34_70_108_111_97_116_105_110_103_80_111_105_110_116_67_111_110_115_116_97_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__70_70_char_class___range__108_108_char_class___range__111_111_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_char_class___range__80_80_char_class___range__111_111_char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,70,108,111,97,116,105,110,103,80,111,105,110,116,67,111,110,115,116,97,110,116,34,41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5416, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(5414, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(5419, 4, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_FloatingPointConstant__char_class___range__0_0_lit___115_111_114_116_40_34_70_108_111_97_116_105_110_103_80_111_105_110_116_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__FloatingPointConstant, tmp);
	}
    protected static final void _init_prod__FloatingPointConstant__iter__char_class___range__48_57_char_class___range__46_46_opt__Exponent_opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[1] = new CharStackNode<IConstructor>(5436, 1, new int[][]{{46,46}}, null, null);
      tmp[2] = new OptionalStackNode<IConstructor>(5438, 2, regular__opt__Exponent, new NonTerminalStackNode<IConstructor>(5437, 0, "Exponent", null, null), null, null);
      tmp[0] = new ListStackNode<IConstructor>(5435, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(5434, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[3] = new OptionalStackNode<IConstructor>(5440, 3, regular__opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108, new CharStackNode<IConstructor>(5439, 0, new int[][]{{70,70},{76,76},{102,102},{108,108}}, null, null), null, null);
      builder.addAlternative(CParser.prod__FloatingPointConstant__iter__char_class___range__48_57_char_class___range__46_46_opt__Exponent_opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108_, tmp);
	}
    protected static final void _init_prod__FloatingPointConstant__iter_star__char_class___range__48_57_char_class___range__46_46_iter__char_class___range__48_57_opt__Exponent_opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new ListStackNode<IConstructor>(5428, 2, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(5425, 0, new int[][]{{48,57}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57}})});
      tmp[4] = new OptionalStackNode<IConstructor>(5432, 4, regular__opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108, new CharStackNode<IConstructor>(5431, 0, new int[][]{{70,70},{76,76},{102,102},{108,108}}, null, null), null, null);
      tmp[0] = new ListStackNode<IConstructor>(5423, 0, regular__iter_star__char_class___range__48_57, new CharStackNode<IConstructor>(5422, 0, new int[][]{{48,57}}, null, null), false, null, null);
      tmp[1] = new CharStackNode<IConstructor>(5424, 1, new int[][]{{46,46}}, null, null);
      tmp[3] = new OptionalStackNode<IConstructor>(5430, 3, regular__opt__Exponent, new NonTerminalStackNode<IConstructor>(5429, 0, "Exponent", null, null), null, null);
      builder.addAlternative(CParser.prod__FloatingPointConstant__iter_star__char_class___range__48_57_char_class___range__46_46_iter__char_class___range__48_57_opt__Exponent_opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108_, tmp);
	}
    protected static final void _init_prod__FloatingPointConstant__iter__char_class___range__48_57_Exponent_opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(5444, 1, "Exponent", null, null);
      tmp[0] = new ListStackNode<IConstructor>(5443, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(5442, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new OptionalStackNode<IConstructor>(5446, 2, regular__opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108, new CharStackNode<IConstructor>(5445, 0, new int[][]{{70,70},{76,76},{102,102},{108,108}}, null, null), null, null);
      builder.addAlternative(CParser.prod__FloatingPointConstant__iter__char_class___range__48_57_Exponent_opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_FloatingPointConstant__char_class___range__0_0_lit___115_111_114_116_40_34_70_108_111_97_116_105_110_103_80_111_105_110_116_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__FloatingPointConstant(builder);
      
        _init_prod__FloatingPointConstant__iter__char_class___range__48_57_char_class___range__46_46_opt__Exponent_opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108_(builder);
      
        _init_prod__FloatingPointConstant__iter_star__char_class___range__48_57_char_class___range__46_46_iter__char_class___range__48_57_opt__Exponent_opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108_(builder);
      
        _init_prod__FloatingPointConstant__iter__char_class___range__48_57_Exponent_opt__char_class___range__70_70_range__76_76_range__102_102_range__108_108_(builder);
      
    }
  }
	
  protected static class HexadecimalConstant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_HexadecimalConstant__char_class___range__0_0_lit___115_111_114_116_40_34_72_101_120_97_100_101_99_105_109_97_108_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__HexadecimalConstant(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(5132, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(5131, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(5129, 1, prod__lit___115_111_114_116_40_34_72_101_120_97_100_101_99_105_109_97_108_67_111_110_115_116_97_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__72_72_char_class___range__101_101_char_class___range__120_120_char_class___range__97_97_char_class___range__100_100_char_class___range__101_101_char_class___range__99_99_char_class___range__105_105_char_class___range__109_109_char_class___range__97_97_char_class___range__108_108_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,72,101,120,97,100,101,99,105,109,97,108,67,111,110,115,116,97,110,116,34,41}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(5128, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(5133, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5130, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_HexadecimalConstant__char_class___range__0_0_lit___115_111_114_116_40_34_72_101_120_97_100_101_99_105_109_97_108_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__HexadecimalConstant, tmp);
	}
    protected static final void _init_prod__HexadecimalConstant__char_class___range__48_48_char_class___range__88_88_range__120_120_iter__char_class___range__48_57_range__65_70_range__97_102_iter_star__char_class___range__76_76_range__85_85_range__108_108_range__117_117_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new ListStackNode<IConstructor>(5125, 3, regular__iter_star__char_class___range__76_76_range__85_85_range__108_108_range__117_117, new CharStackNode<IConstructor>(5122, 0, new int[][]{{76,76},{85,85},{108,108},{117,117}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,70},{97,102}})});
      tmp[1] = new CharStackNode<IConstructor>(5119, 1, new int[][]{{88,88},{120,120}}, null, null);
      tmp[2] = new ListStackNode<IConstructor>(5121, 2, regular__iter__char_class___range__48_57_range__65_70_range__97_102, new CharStackNode<IConstructor>(5120, 0, new int[][]{{48,57},{65,70},{97,102}}, null, null), true, null, null);
      tmp[0] = new CharStackNode<IConstructor>(5118, 0, new int[][]{{48,48}}, null, null);
      builder.addAlternative(CParser.prod__HexadecimalConstant__char_class___range__48_48_char_class___range__88_88_range__120_120_iter__char_class___range__48_57_range__65_70_range__97_102_iter_star__char_class___range__76_76_range__85_85_range__108_108_range__117_117_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_HexadecimalConstant__char_class___range__0_0_lit___115_111_114_116_40_34_72_101_120_97_100_101_99_105_109_97_108_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__HexadecimalConstant(builder);
      
        _init_prod__HexadecimalConstant__char_class___range__48_48_char_class___range__88_88_range__120_120_iter__char_class___range__48_57_range__65_70_range__97_102_iter_star__char_class___range__76_76_range__85_85_range__108_108_range__117_117_(builder);
      
    }
  }
	
  protected static class Identifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_Identifier__char_class___range__0_0_lit___115_111_114_116_40_34_73_100_101_110_116_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Identifier(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(3950, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(3955, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3952, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(3954, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3953, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(3951, 1, prod__lit___115_111_114_116_40_34_73_100_101_110_116_105_102_105_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__73_73_char_class___range__100_100_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,73,100,101,110,116,105,102,105,101,114,34,41}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Identifier__char_class___range__0_0_lit___115_111_114_116_40_34_73_100_101_110_116_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Identifier, tmp);
	}
    protected static final void _init_prod__Identifier__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SequenceStackNode<IConstructor>(3947, 0, regular__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new CharStackNode<IConstructor>(3940, 0, new int[][]{{65,90},{95,95},{97,122}}, null, null), new ListStackNode<IConstructor>(3944, 1, regular__iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122, new CharStackNode<IConstructor>(3941, 0, new int[][]{{48,57},{65,90},{95,95},{97,122}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57},{65,90},{95,95},{97,122}})})}, null, new ICompletionFilter[] {new StringMatchRestriction(new int[] {101,108,115,101}), new StringMatchRestriction(new int[] {115,105,122,101,111,102}), new StringMatchRestriction(new int[] {98,114,101,97,107}), new StringMatchRestriction(new int[] {105,102}), new StringMatchRestriction(new int[] {99,104,97,114}), new StringMatchRestriction(new int[] {102,111,114}), new StringMatchRestriction(new int[] {115,116,97,116,105,99}), new StringMatchRestriction(new int[] {101,120,116,101,114,110}), new StringMatchRestriction(new int[] {99,111,110,116,105,110,117,101}), new StringMatchRestriction(new int[] {115,105,103,110,101,100}), new StringMatchRestriction(new int[] {102,108,111,97,116}), new StringMatchRestriction(new int[] {108,111,110,103}), new StringMatchRestriction(new int[] {118,111,108,97,116,105,108,101}), new StringMatchRestriction(new int[] {115,104,111,114,116}), new StringMatchRestriction(new int[] {119,104,105,108,101}), new StringMatchRestriction(new int[] {100,111,117,98,108,101}), new StringMatchRestriction(new int[] {99,97,115,101}), new StringMatchRestriction(new int[] {114,101,116,117,114,110}), new StringMatchRestriction(new int[] {115,116,114,117,99,116}), new StringMatchRestriction(new int[] {117,110,115,105,103,110,101,100}), new StringMatchRestriction(new int[] {115,119,105,116,99,104}), new StringMatchRestriction(new int[] {118,111,105,100}), new StringMatchRestriction(new int[] {116,121,112,101,100,101,102}), new StringMatchRestriction(new int[] {100,111}), new StringMatchRestriction(new int[] {105,110,116}), new StringMatchRestriction(new int[] {117,110,105,111,110}), new StringMatchRestriction(new int[] {97,117,116,111}), new StringMatchRestriction(new int[] {99,111,110,115,116}), new StringMatchRestriction(new int[] {114,101,103,105,115,116,101,114}), new StringMatchRestriction(new int[] {103,111,116,111}), new StringMatchRestriction(new int[] {100,101,102,97,117,108,116}), new StringMatchRestriction(new int[] {101,110,117,109})});
      builder.addAlternative(CParser.prod__Identifier__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_Identifier__char_class___range__0_0_lit___115_111_114_116_40_34_73_100_101_110_116_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Identifier(builder);
      
        _init_prod__Identifier__seq___char_class___range__65_90_range__95_95_range__97_122_iter_star__char_class___range__48_57_range__65_90_range__95_95_range__97_122_(builder);
      
    }
  }
	
  protected static class IntegerConstant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_IntegerConstant__char_class___range__0_0_lit___115_111_114_116_40_34_73_110_116_101_103_101_114_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__IntegerConstant(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(3031, 1, prod__lit___115_111_114_116_40_34_73_110_116_101_103_101_114_67_111_110_115_116_97_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__73_73_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__103_103_char_class___range__101_101_char_class___range__114_114_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,73,110,116,101,103,101,114,67,111,110,115,116,97,110,116,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(3034, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3033, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new CharStackNode<IConstructor>(3030, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3032, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(3035, 4, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_IntegerConstant__char_class___range__0_0_lit___115_111_114_116_40_34_73_110_116_101_103_101_114_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__IntegerConstant, tmp);
	}
    protected static final void _init_prod__IntegerConstant__iter__char_class___range__48_57_iter_star__char_class___range__76_76_range__85_85_range__108_108_range__117_117_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new ListStackNode<IConstructor>(3027, 1, regular__iter_star__char_class___range__76_76_range__85_85_range__108_108_range__117_117, new CharStackNode<IConstructor>(3024, 0, new int[][]{{76,76},{85,85},{108,108},{117,117}}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57}})});
      tmp[0] = new ListStackNode<IConstructor>(3023, 0, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3022, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(CParser.prod__IntegerConstant__iter__char_class___range__48_57_iter_star__char_class___range__76_76_range__85_85_range__108_108_range__117_117_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_IntegerConstant__char_class___range__0_0_lit___115_111_114_116_40_34_73_110_116_101_103_101_114_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__IntegerConstant(builder);
      
        _init_prod__IntegerConstant__iter__char_class___range__48_57_iter_star__char_class___range__76_76_range__85_85_range__108_108_range__117_117_(builder);
      
    }
  }
	
  protected static class LAYOUT {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_LAYOUT__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_76_65_89_79_85_84_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__LAYOUT(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(4387, 4, new int[][]{{0,0}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4382, 0, new int[][]{{0,0}}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(4386, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4385, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(4383, 1, prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_76_65_89_79_85_84_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__76_76_char_class___range__65_65_char_class___range__89_89_char_class___range__79_79_char_class___range__85_85_char_class___range__84_84_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {92,105,116,101,114,45,115,116,97,114,40,115,111,114,116,40,34,76,65,89,79,85,84,34,41,41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4384, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_LAYOUT__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_76_65_89_79_85_84_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__LAYOUT, tmp);
	}
    protected static final void _init_prod__$MetaHole_LAYOUT__char_class___range__0_0_lit___115_111_114_116_40_34_76_65_89_79_85_84_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__LAYOUT(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(4371, 1, prod__lit___115_111_114_116_40_34_76_65_89_79_85_84_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__76_76_char_class___range__65_65_char_class___range__89_89_char_class___range__79_79_char_class___range__85_85_char_class___range__84_84_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,76,65,89,79,85,84,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(4374, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4373, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4372, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4375, 4, new int[][]{{0,0}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4370, 0, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_LAYOUT__char_class___range__0_0_lit___115_111_114_116_40_34_76_65_89_79_85_84_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__LAYOUT, tmp);
	}
    protected static final void _init_prod__comment_LAYOUT__Comment__tag__category___67_111_109_109_101_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4392, 0, "Comment", null, null);
      builder.addAlternative(CParser.prod__comment_LAYOUT__Comment__tag__category___67_111_109_109_101_110_116, tmp);
	}
    protected static final void _init_prod__whitespace_LAYOUT__char_class___range__9_10_range__13_13_range__32_32_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(4379, 0, new int[][]{{9,10},{13,13},{32,32}}, null, null);
      builder.addAlternative(CParser.prod__whitespace_LAYOUT__char_class___range__9_10_range__13_13_range__32_32_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_LAYOUT__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_76_65_89_79_85_84_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__LAYOUT(builder);
      
        _init_prod__$MetaHole_LAYOUT__char_class___range__0_0_lit___115_111_114_116_40_34_76_65_89_79_85_84_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__LAYOUT(builder);
      
        _init_prod__comment_LAYOUT__Comment__tag__category___67_111_109_109_101_110_116(builder);
      
        _init_prod__whitespace_LAYOUT__char_class___range__9_10_range__13_13_range__32_32_(builder);
      
    }
  }
	
  protected static class MultiLineCommentBodyToken {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_MultiLineCommentBodyToken__char_class___range__0_0_lit___115_111_114_116_40_34_77_117_108_116_105_76_105_110_101_67_111_109_109_101_110_116_66_111_100_121_84_111_107_101_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__MultiLineCommentBodyToken(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(3132, 4, new int[][]{{0,0}}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(3131, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3130, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(3128, 1, prod__lit___115_111_114_116_40_34_77_117_108_116_105_76_105_110_101_67_111_109_109_101_110_116_66_111_100_121_84_111_107_101_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__77_77_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_char_class___range__105_105_char_class___range__76_76_char_class___range__105_105_char_class___range__110_110_char_class___range__101_101_char_class___range__67_67_char_class___range__111_111_char_class___range__109_109_char_class___range__109_109_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__66_66_char_class___range__111_111_char_class___range__100_100_char_class___range__121_121_char_class___range__84_84_char_class___range__111_111_char_class___range__107_107_char_class___range__101_101_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,77,117,108,116,105,76,105,110,101,67,111,109,109,101,110,116,66,111,100,121,84,111,107,101,110,34,41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3129, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(3127, 0, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_MultiLineCommentBodyToken__char_class___range__0_0_lit___115_111_114_116_40_34_77_117_108_116_105_76_105_110_101_67_111_109_109_101_110_116_66_111_100_121_84_111_107_101_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__MultiLineCommentBodyToken, tmp);
	}
    protected static final void _init_prod__$MetaHole_MultiLineCommentBodyToken__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_77_117_108_116_105_76_105_110_101_67_111_109_109_101_110_116_66_111_100_121_84_111_107_101_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__MultiLineCommentBodyToken(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(3141, 4, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(3137, 1, prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_77_117_108_116_105_76_105_110_101_67_111_109_109_101_110_116_66_111_100_121_84_111_107_101_110_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__77_77_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_char_class___range__105_105_char_class___range__76_76_char_class___range__105_105_char_class___range__110_110_char_class___range__101_101_char_class___range__67_67_char_class___range__111_111_char_class___range__109_109_char_class___range__109_109_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__66_66_char_class___range__111_111_char_class___range__100_100_char_class___range__121_121_char_class___range__84_84_char_class___range__111_111_char_class___range__107_107_char_class___range__101_101_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {92,105,116,101,114,45,115,116,97,114,40,115,111,114,116,40,34,77,117,108,116,105,76,105,110,101,67,111,109,109,101,110,116,66,111,100,121,84,111,107,101,110,34,41,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(3140, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3139, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new CharStackNode<IConstructor>(3136, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3138, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_MultiLineCommentBodyToken__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_77_117_108_116_105_76_105_110_101_67_111_109_109_101_110_116_66_111_100_121_84_111_107_101_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__MultiLineCommentBodyToken, tmp);
	}
    protected static final void _init_prod__MultiLineCommentBodyToken__char_class___range__1_41_range__43_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(3122, 0, new int[][]{{1,41},{43,16777215}}, null, null);
      builder.addAlternative(CParser.prod__MultiLineCommentBodyToken__char_class___range__1_41_range__43_16777215_, tmp);
	}
    protected static final void _init_prod__MultiLineCommentBodyToken__Asterisk_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3124, 0, "Asterisk", null, null);
      builder.addAlternative(CParser.prod__MultiLineCommentBodyToken__Asterisk_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_MultiLineCommentBodyToken__char_class___range__0_0_lit___115_111_114_116_40_34_77_117_108_116_105_76_105_110_101_67_111_109_109_101_110_116_66_111_100_121_84_111_107_101_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__MultiLineCommentBodyToken(builder);
      
        _init_prod__$MetaHole_MultiLineCommentBodyToken__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_77_117_108_116_105_76_105_110_101_67_111_109_109_101_110_116_66_111_100_121_84_111_107_101_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__MultiLineCommentBodyToken(builder);
      
        _init_prod__MultiLineCommentBodyToken__char_class___range__1_41_range__43_16777215_(builder);
      
        _init_prod__MultiLineCommentBodyToken__Asterisk_(builder);
      
    }
  }
	
  protected static class StringConstant {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_StringConstant__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StringConstant(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(834, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(833, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(831, 1, prod__lit___115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__114_114_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,83,116,114,105,110,103,67,111,110,115,116,97,110,116,34,41}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(835, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(832, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(830, 0, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_StringConstant__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StringConstant, tmp);
	}
    protected static final void _init_prod__StringConstant__opt__char_class___range__76_76_char_class___range__34_34_iter_star__StringConstantContent_char_class___range__34_34_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[2] = new ListStackNode<IConstructor>(842, 2, regular__iter_star__StringConstantContent, new NonTerminalStackNode<IConstructor>(841, 0, "StringConstantContent", null, null), false, null, null);
      tmp[0] = new OptionalStackNode<IConstructor>(839, 0, regular__opt__char_class___range__76_76, new CharStackNode<IConstructor>(838, 0, new int[][]{{76,76}}, null, null), null, null);
      tmp[1] = new CharStackNode<IConstructor>(840, 1, new int[][]{{34,34}}, null, null);
      tmp[3] = new CharStackNode<IConstructor>(843, 3, new int[][]{{34,34}}, null, null);
      builder.addAlternative(CParser.prod__StringConstant__opt__char_class___range__76_76_char_class___range__34_34_iter_star__StringConstantContent_char_class___range__34_34_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_StringConstant__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StringConstant(builder);
      
        _init_prod__StringConstant__opt__char_class___range__76_76_char_class___range__34_34_iter_star__StringConstantContent_char_class___range__34_34_(builder);
      
    }
  }
	
  protected static class StringConstantContent {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_StringConstantContent__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__StringConstantContent(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(3286, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(3291, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3288, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(3290, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3289, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(3287, 1, prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__114_114_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {92,105,116,101,114,45,115,116,97,114,40,115,111,114,116,40,34,83,116,114,105,110,103,67,111,110,115,116,97,110,116,67,111,110,116,101,110,116,34,41,41}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_StringConstantContent__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__StringConstantContent, tmp);
	}
    protected static final void _init_prod__$MetaHole_StringConstantContent__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StringConstantContent(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(3273, 1, prod__lit___115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__114_114_char_class___range__105_105_char_class___range__110_110_char_class___range__103_103_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__110_110_char_class___range__116_116_char_class___range__67_67_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,83,116,114,105,110,103,67,111,110,115,116,97,110,116,67,111,110,116,101,110,116,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(3276, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3275, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new CharStackNode<IConstructor>(3272, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3274, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(3277, 4, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_StringConstantContent__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StringConstantContent, tmp);
	}
    protected static final void _init_prod__StringConstantContent__char_class___range__92_92_char_class___range__1_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[0] = new CharStackNode<IConstructor>(3280, 0, new int[][]{{92,92}}, null, null);
      tmp[1] = new CharStackNode<IConstructor>(3281, 1, new int[][]{{1,16777215}}, null, null);
      builder.addAlternative(CParser.prod__StringConstantContent__char_class___range__92_92_char_class___range__1_16777215_, tmp);
	}
    protected static final void _init_prod__StringConstantContent__char_class___range__1_33_range__35_91_range__93_16777215_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new CharStackNode<IConstructor>(3283, 0, new int[][]{{1,33},{35,91},{93,16777215}}, null, null);
      builder.addAlternative(CParser.prod__StringConstantContent__char_class___range__1_33_range__35_91_range__93_16777215_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_StringConstantContent__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star__StringConstantContent(builder);
      
        _init_prod__$MetaHole_StringConstantContent__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_105_110_103_67_111_110_115_116_97_110_116_67_111_110_116_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StringConstantContent(builder);
      
        _init_prod__StringConstantContent__char_class___range__92_92_char_class___range__1_16777215_(builder);
      
        _init_prod__StringConstantContent__char_class___range__1_33_range__35_91_range__93_16777215_(builder);
      
    }
  }
	
  protected static class AbstractDeclarator {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_AbstractDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_65_98_115_116_114_97_99_116_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__AbstractDeclarator(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(1483, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(1482, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(1480, 1, prod__lit___115_111_114_116_40_34_65_98_115_116_114_97_99_116_68_101_99_108_97_114_97_116_111_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__65_65_char_class___range__98_98_char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__116_116_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,65,98,115,116,114,97,99,116,68,101,99,108,97,114,97,116,111,114,34,41}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(1479, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(1484, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1481, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_AbstractDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_65_98_115_116_114_97_99_116_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__AbstractDeclarator, tmp);
	}
    protected static final void _init_prod__arrayDeclarator_AbstractDeclarator__decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_exp_opt__Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[4] = new OptionalStackNode<IConstructor>(1447, 4, regular__opt__Expression, new NonTerminalStackNode<IConstructor>(1446, 0, "Expression", null, null), null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1443, 1, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1449, 5, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1445, 3, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1441, 0, "AbstractDeclarator", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(1450, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1444, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      builder.addAlternative(CParser.prod__arrayDeclarator_AbstractDeclarator__decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_exp_opt__Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__bracket_AbstractDeclarator__lit___40_layouts_LAYOUTLIST_decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(1437, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1434, 1, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(1438, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(1435, 2, "AbstractDeclarator", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1433, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(CParser.prod__bracket_AbstractDeclarator__lit___40_layouts_LAYOUTLIST_decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__functionDeclarator_AbstractDeclarator__decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_params_opt__Parameters_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[2] = new LiteralStackNode<IConstructor>(1456, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1453, 0, "AbstractDeclarator", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1457, 3, "layouts_LAYOUTLIST", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(1462, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[4] = new OptionalStackNode<IConstructor>(1459, 4, regular__opt__Parameters, new NonTerminalStackNode<IConstructor>(1458, 0, "Parameters", null, null), null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1461, 5, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1455, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__functionDeclarator_AbstractDeclarator__decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_params_opt__Parameters_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__identifier_AbstractDeclarator__AnonymousIdentifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1465, 0, "AnonymousIdentifier", null, null);
      builder.addAlternative(CParser.prod__identifier_AbstractDeclarator__AnonymousIdentifier_, tmp);
	}
    protected static final void _init_prod__pointerDeclarator_AbstractDeclarator__lit___42_layouts_LAYOUTLIST_qualifiers_iter_star_seps__TypeQualifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_AbstractDeclarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1468, 0, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(1472, 2, regular__iter_star_seps__TypeQualifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1470, 0, "TypeQualifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1471, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1474, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1469, 1, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(1475, 4, "AbstractDeclarator", null, null);
      builder.addAlternative(CParser.prod__pointerDeclarator_AbstractDeclarator__lit___42_layouts_LAYOUTLIST_qualifiers_iter_star_seps__TypeQualifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_AbstractDeclarator_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_AbstractDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_65_98_115_116_114_97_99_116_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__AbstractDeclarator(builder);
      
        _init_prod__arrayDeclarator_AbstractDeclarator__decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_exp_opt__Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__bracket_AbstractDeclarator__lit___40_layouts_LAYOUTLIST_decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__functionDeclarator_AbstractDeclarator__decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_params_opt__Parameters_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__identifier_AbstractDeclarator__AnonymousIdentifier_(builder);
      
        _init_prod__pointerDeclarator_AbstractDeclarator__lit___42_layouts_LAYOUTLIST_qualifiers_iter_star_seps__TypeQualifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_AbstractDeclarator_(builder);
      
    }
  }
	
  protected static class AnonymousIdentifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_AnonymousIdentifier__char_class___range__0_0_lit___115_111_114_116_40_34_65_110_111_110_121_109_111_117_115_73_100_101_110_116_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__AnonymousIdentifier(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(2227, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(2232, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2229, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(2231, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(2230, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(2228, 1, prod__lit___115_111_114_116_40_34_65_110_111_110_121_109_111_117_115_73_100_101_110_116_105_102_105_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__65_65_char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__121_121_char_class___range__109_109_char_class___range__111_111_char_class___range__117_117_char_class___range__115_115_char_class___range__73_73_char_class___range__100_100_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,65,110,111,110,121,109,111,117,115,73,100,101,110,116,105,102,105,101,114,34,41}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_AnonymousIdentifier__char_class___range__0_0_lit___115_111_114_116_40_34_65_110_111_110_121_109_111_117_115_73_100_101_110_116_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__AnonymousIdentifier, tmp);
	}
    protected static final void _init_prod__AnonymousIdentifier__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(2234, 0);
      builder.addAlternative(CParser.prod__AnonymousIdentifier__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_AnonymousIdentifier__char_class___range__0_0_lit___115_111_114_116_40_34_65_110_111_110_121_109_111_117_115_73_100_101_110_116_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__AnonymousIdentifier(builder);
      
        _init_prod__AnonymousIdentifier__(builder);
      
    }
  }
	
  protected static class Declaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_Declaration__char_class___range__0_0_lit___115_111_114_116_40_34_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Declaration(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(670, 1, prod__lit___115_111_114_116_40_34_68_101_99_108_97_114_97_116_105_111_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,68,101,99,108,97,114,97,116,105,111,110,34,41}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(669, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(674, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(671, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(673, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(672, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Declaration__char_class___range__0_0_lit___115_111_114_116_40_34_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Declaration, tmp);
	}
    protected static final void _init_prod__$MetaHole_Declaration__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_68_101_99_108_97_114_97_116_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__Declaration__layouts_LAYOUTLIST(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(654, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(653, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(651, 1, prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_68_101_99_108_97_114_97_116_105_111_110_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {92,105,116,101,114,45,115,116,97,114,40,115,111,114,116,40,34,68,101,99,108,97,114,97,116,105,111,110,34,41,41}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(655, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(652, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(650, 0, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Declaration__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_68_101_99_108_97_114_97_116_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__Declaration__layouts_LAYOUTLIST, tmp);
	}
    protected static final void _init_prod__declarationWithInitDecls_Declaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_initDeclarators_iter_seps__InitDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new SeparatedListStackNode<IConstructor>(687, 2, regular__iter_seps__InitDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(683, 0, "InitDeclarator", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(684, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(685, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(686, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(690, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(680, 0, regular__iter_seps__Specifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(678, 0, "Specifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(679, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(682, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(689, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__declarationWithInitDecls_Declaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_initDeclarators_iter_seps__InitDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__declarationWithoutInitDecls_Declaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(665, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(663, 0, regular__iter_seps__Specifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(661, 0, "Specifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(662, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(666, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(CParser.prod__declarationWithoutInitDecls_Declaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_Declaration__char_class___range__0_0_lit___115_111_114_116_40_34_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Declaration(builder);
      
        _init_prod__$MetaHole_Declaration__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_68_101_99_108_97_114_97_116_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__Declaration__layouts_LAYOUTLIST(builder);
      
        _init_prod__declarationWithInitDecls_Declaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_initDeclarators_iter_seps__InitDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__declarationWithoutInitDecls_Declaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(builder);
      
    }
  }
	
  protected static class Declarator {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_Declarator__char_class___range__0_0_lit___115_111_114_116_40_34_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Declarator(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(1230, 1, prod__lit___115_111_114_116_40_34_68_101_99_108_97_114_97_116_111_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,68,101,99,108,97,114,97,116,111,114,34,41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1231, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(1233, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(1232, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new CharStackNode<IConstructor>(1229, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(1234, 4, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Declarator__char_class___range__0_0_lit___115_111_114_116_40_34_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Declarator, tmp);
	}
    protected static final void _init_prod__$MetaHole_Declarator__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_68_101_99_108_97_114_97_116_111_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Declarator(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(1242, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(1241, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new CharStackNode<IConstructor>(1238, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(1243, 4, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(1239, 1, prod__lit___111_112_116_40_115_111_114_116_40_34_68_101_99_108_97_114_97_116_111_114_34_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {111,112,116,40,115,111,114,116,40,34,68,101,99,108,97,114,97,116,111,114,34,41,41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1240, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Declarator__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_68_101_99_108_97_114_97_116_111_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Declarator, tmp);
	}
    protected static final void _init_prod__arrayDeclarator_Declarator__decl_Declarator_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_exp_opt__Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1206, 0, "Declarator", null, null);
      tmp[4] = new OptionalStackNode<IConstructor>(1212, 4, regular__opt__Expression, new NonTerminalStackNode<IConstructor>(1211, 0, "Expression", null, null), null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1209, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(1215, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1210, 3, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1214, 5, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1208, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__arrayDeclarator_Declarator__decl_Declarator_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_exp_opt__Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__bracket_Declarator__lit___40_layouts_LAYOUTLIST_decl_Declarator_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(1184, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(1185, 2, "Declarator", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1187, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(1188, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1183, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      builder.addAlternative(CParser.prod__bracket_Declarator__lit___40_layouts_LAYOUTLIST_decl_Declarator_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__functionDeclarator_Declarator__decl_Declarator_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_params_opt__Parameters_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(1195, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new OptionalStackNode<IConstructor>(1197, 4, regular__opt__Parameters, new NonTerminalStackNode<IConstructor>(1196, 0, "Parameters", null, null), null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(1200, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1194, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1191, 0, "Declarator", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1193, 1, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1199, 5, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__functionDeclarator_Declarator__decl_Declarator_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_params_opt__Parameters_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__identifier_Declarator__Identifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1203, 0, "Identifier", null, null);
      builder.addAlternative(CParser.prod__identifier_Declarator__Identifier_, tmp);
	}
    protected static final void _init_prod__pointerDeclarator_Declarator__lit___42_layouts_LAYOUTLIST_qualifiers_iter_star_seps__TypeQualifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_Declarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1218, 0, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(1222, 2, regular__iter_star_seps__TypeQualifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1220, 0, "TypeQualifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1221, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(1225, 4, "Declarator", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1219, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1224, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__pointerDeclarator_Declarator__lit___42_layouts_LAYOUTLIST_qualifiers_iter_star_seps__TypeQualifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_Declarator_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_Declarator__char_class___range__0_0_lit___115_111_114_116_40_34_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Declarator(builder);
      
        _init_prod__$MetaHole_Declarator__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_68_101_99_108_97_114_97_116_111_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Declarator(builder);
      
        _init_prod__arrayDeclarator_Declarator__decl_Declarator_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_exp_opt__Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__bracket_Declarator__lit___40_layouts_LAYOUTLIST_decl_Declarator_layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__functionDeclarator_Declarator__decl_Declarator_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_params_opt__Parameters_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__identifier_Declarator__Identifier_(builder);
      
        _init_prod__pointerDeclarator_Declarator__lit___42_layouts_LAYOUTLIST_qualifiers_iter_star_seps__TypeQualifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_Declarator_(builder);
      
    }
  }
	
  protected static class Enumerator {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_Enumerator__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_69_110_117_109_101_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Enumerator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(5049, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(5054, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5051, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(5053, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(5052, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(5050, 1, prod__lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_69_110_117_109_101_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__101_101_char_class___range__112_112_char_class___range__115_115_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_char_class___range__101_101_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__44_44_char_class___range__91_91_char_class___range__108_108_char_class___range__105_105_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__44_44_char_class___range__34_34_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_, new int[] {92,105,116,101,114,45,115,101,112,115,40,115,111,114,116,40,34,69,110,117,109,101,114,97,116,111,114,34,41,44,91,108,105,116,40,34,44,34,41,93,41}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Enumerator__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_69_110_117_109_101_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Enumerator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, tmp);
	}
    protected static final void _init_prod__$MetaHole_Enumerator__char_class___range__0_0_lit___115_111_114_116_40_34_69_110_117_109_101_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Enumerator(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(5036, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(5035, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[4] = new CharStackNode<IConstructor>(5037, 4, new int[][]{{0,0}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(5032, 0, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(5033, 1, prod__lit___115_111_114_116_40_34_69_110_117_109_101_114_97_116_111_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_char_class___range__101_101_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,69,110,117,109,101,114,97,116,111,114,34,41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5034, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Enumerator__char_class___range__0_0_lit___115_111_114_116_40_34_69_110_117_109_101_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Enumerator, tmp);
	}
    protected static final void _init_prod__Enumerator__Identifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5040, 0, "Identifier", null, null);
      builder.addAlternative(CParser.prod__Enumerator__Identifier_, tmp);
	}
    protected static final void _init_prod__Enumerator__Identifier_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_NonCommaExpression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(5046, 4, "NonCommaExpression", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(5042, 0, "Identifier", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(5045, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5044, 2, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5043, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Enumerator__Identifier_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_NonCommaExpression_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_Enumerator__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_69_110_117_109_101_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Enumerator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST(builder);
      
        _init_prod__$MetaHole_Enumerator__char_class___range__0_0_lit___115_111_114_116_40_34_69_110_117_109_101_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Enumerator(builder);
      
        _init_prod__Enumerator__Identifier_(builder);
      
        _init_prod__Enumerator__Identifier_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_NonCommaExpression_(builder);
      
    }
  }
	
  protected static class Expression {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_Expression__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_69_120_112_114_101_115_115_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Expression(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(484, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(489, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(486, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(488, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(487, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(485, 1, prod__lit___111_112_116_40_115_111_114_116_40_34_69_120_112_114_101_115_115_105_111_110_34_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__120_120_char_class___range__112_112_char_class___range__114_114_char_class___range__101_101_char_class___range__115_115_char_class___range__115_115_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {111,112,116,40,115,111,114,116,40,34,69,120,112,114,101,115,115,105,111,110,34,41,41}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Expression__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_69_120_112_114_101_115_115_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Expression, tmp);
	}
    protected static final void _init_prod__$MetaHole_Expression__char_class___range__0_0_lit___115_111_114_116_40_34_69_120_112_114_101_115_115_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Expression(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(155, 1, prod__lit___115_111_114_116_40_34_69_120_112_114_101_115_115_105_111_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__120_120_char_class___range__112_112_char_class___range__114_114_char_class___range__101_101_char_class___range__115_115_char_class___range__115_115_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,69,120,112,114,101,115,115,105,111,110,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(158, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(157, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new CharStackNode<IConstructor>(154, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(156, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(159, 4, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Expression__char_class___range__0_0_lit___115_111_114_116_40_34_69_120_112_114_101_115_115_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Expression, tmp);
	}
    protected static final void _init_prod__bracket_Expression__lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new LiteralStackNode<IConstructor>(224, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(226, 2, "Expression", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(228, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(225, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(227, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__bracket_Expression__lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__commaExpression_Expression__Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(481, 4, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(479, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(477, 0, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(480, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(478, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__commaExpression_Expression__Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__multiplicationExpression_Expression__lexp_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_rexp_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(302, 4, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(300, 2, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(297, 0, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(301, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(299, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__multiplicationExpression_Expression__lexp_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_rexp_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__sizeOfExpression_Expression__lit_sizeof_layouts_LAYOUTLIST_exp_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode<IConstructor>(238, 0, prod__lit_sizeof__char_class___range__115_115_char_class___range__105_105_char_class___range__122_122_char_class___range__101_101_char_class___range__111_111_char_class___range__102_102_, new int[] {115,105,122,101,111,102}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(240, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(239, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__sizeOfExpression_Expression__lit_sizeof_layouts_LAYOUTLIST_exp_Expression_, tmp);
	}
    protected static final void _init_prod__variable_Expression__Identifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(173, 0, "Identifier", null, null);
      builder.addAlternative(CParser.prod__variable_Expression__Identifier_, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new LiteralStackNode<IConstructor>(286, 2, prod__lit___37__char_class___range__37_37_, new int[] {37}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(288, 4, "Expression", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(284, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(285, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(287, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__NonCommaExpression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[2] = new LiteralStackNode<IConstructor>(179, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(187, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(185, 4, regular__iter_star_seps__NonCommaExpression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(181, 0, "NonCommaExpression", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(182, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(183, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(184, 3, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(177, 0, "Expression", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(186, 5, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(178, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(180, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__NonCommaExpression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new LiteralStackNode<IConstructor>(292, 2, prod__lit___47__char_class___range__47_47_, new int[] {47}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(294, 4, "Expression", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(290, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(291, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(293, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___38_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new LiteralStackNode<IConstructor>(371, 2, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(373, 4, "Expression", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(369, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(370, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(372, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___38_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___43_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new LiteralStackNode<IConstructor>(308, 2, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(310, 4, "Expression", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(306, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(307, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(309, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___43_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__HexadecimalConstant__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(175, 0, "HexadecimalConstant", null, null);
      builder.addAlternative(CParser.prod__Expression__HexadecimalConstant__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_60_61_layouts_LAYOUTLIST_Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new LiteralStackNode<IConstructor>(472, 2, prod__lit___60_60_61__char_class___range__60_60_char_class___range__60_60_char_class___range__61_61_, new int[] {60,60,61}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(474, 4, "Expression", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(470, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(471, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(473, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_60_61_layouts_LAYOUTLIST_Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___45_61_layouts_LAYOUTLIST_Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new LiteralStackNode<IConstructor>(436, 2, prod__lit___45_61__char_class___range__45_45_char_class___range__61_61_, new int[] {45,61}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(438, 4, "Expression", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(434, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(435, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(437, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___45_61_layouts_LAYOUTLIST_Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new LiteralStackNode<IConstructor>(346, 2, prod__lit___62__char_class___range__62_62_, new int[] {62}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(348, 4, "Expression", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(344, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(345, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(347, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new LiteralStackNode<IConstructor>(365, 2, prod__lit___33_61__char_class___range__33_33_char_class___range__61_61_, new int[] {33,61}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(367, 4, "Expression", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(363, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(364, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(366, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new LiteralStackNode<IConstructor>(352, 2, prod__lit___60__char_class___range__60_60_, new int[] {60}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(354, 4, "Expression", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(350, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(351, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(353, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(195, 0, "Expression", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(199, 4, "Expression", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(201, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(197, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(198, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(196, 1, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(200, 5, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___47_61_layouts_LAYOUTLIST_Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(452, 0, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(454, 2, prod__lit___47_61__char_class___range__47_47_char_class___range__61_61_, new int[] {47,61}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(456, 4, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(453, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(455, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___47_61_layouts_LAYOUTLIST_Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(399, 0, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(401, 2, prod__lit___63__char_class___range__63_63_, new int[] {63}, null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(405, 6, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(403, 4, "Expression", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(407, 8, "Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(406, 7, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(400, 1, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(404, 5, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(402, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___38_61_layouts_LAYOUTLIST_Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(458, 0, "Expression", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(462, 4, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(460, 2, prod__lit___38_61__char_class___range__38_38_char_class___range__61_61_, new int[] {38,61}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(461, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(459, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___38_61_layouts_LAYOUTLIST_Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__Expression__CharacterConstant__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(230, 0, "CharacterConstant", null, null);
      builder.addAlternative(CParser.prod__Expression__CharacterConstant__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___94_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(375, 0, "Expression", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(379, 4, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(377, 2, prod__lit___94__char_class___range__94_94_, new int[] {94}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(378, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(376, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___94_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit____(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(203, 0, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(205, 2, prod__lit_____char_class___range__45_45_char_class___range__45_45_, new int[] {45,45}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(204, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit____, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new LiteralStackNode<IConstructor>(314, 2, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(316, 4, "Expression", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(312, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(313, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(315, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__lit____layouts_LAYOUTLIST_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode<IConstructor>(245, 0, prod__lit_____char_class___range__45_45_char_class___range__45_45_, new int[] {45,45}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{45,45}})}, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(247, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(246, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__lit____layouts_LAYOUTLIST_Expression_, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___10_9_32_32_32_32_32_32_32_32_124_32_61_layouts_LAYOUTLIST_Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(446, 0, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(448, 2, prod__lit___10_9_32_32_32_32_32_32_32_32_124_32_61__char_class___range__10_10_char_class___range__9_9_char_class___range__32_32_char_class___range__32_32_char_class___range__32_32_char_class___range__32_32_char_class___range__32_32_char_class___range__32_32_char_class___range__32_32_char_class___range__32_32_char_class___range__124_124_char_class___range__32_32_char_class___range__61_61_, new int[] {10,9,32,32,32,32,32,32,32,32,124,32,61}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(450, 4, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(447, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(449, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___10_9_32_32_32_32_32_32_32_32_124_32_61_layouts_LAYOUTLIST_Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__Expression__lit_sizeof_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_TypeName_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[0] = new LiteralStackNode<IConstructor>(211, 0, prod__lit_sizeof__char_class___range__115_115_char_class___range__105_105_char_class___range__122_122_char_class___range__101_101_char_class___range__111_111_char_class___range__102_102_, new int[] {115,105,122,101,111,102}, null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(217, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(213, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(215, 4, "TypeName", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(212, 1, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(216, 5, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(214, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__lit_sizeof_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_TypeName_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__Expression__FloatingPointConstant__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(164, 0, "FloatingPointConstant", null, null);
      builder.addAlternative(CParser.prod__Expression__FloatingPointConstant__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___61_61_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(358, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(360, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(359, 2, prod__lit___61_61__char_class___range__61_61_char_class___range__61_61_, new int[] {61,61}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(361, 4, "Expression", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(357, 0, "Expression", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___61_61_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__lit___43_43_layouts_LAYOUTLIST_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode<IConstructor>(251, 0, prod__lit___43_43__char_class___range__43_43_char_class___range__43_43_, new int[] {43,43}, new IEnterFilter[] {new CharPrecedeRestriction(new int[][]{{43,43}})}, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(253, 2, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(252, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__lit___43_43_layouts_LAYOUTLIST_Expression_, tmp);
	}
    protected static final void _init_prod__Expression__StringConstant__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(209, 0, "StringConstant", null, null);
      builder.addAlternative(CParser.prod__Expression__StringConstant__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(396, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(394, 1, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(397, 4, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(395, 2, prod__lit___124_124__char_class___range__124_124_char_class___range__124_124_, new int[] {124,124}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(393, 0, "Expression", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__lit___126_layouts_LAYOUTLIST_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(234, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(235, 2, "Expression", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(233, 0, prod__lit___126__char_class___range__126_126_, new int[] {126}, null, null);
      builder.addAlternative(CParser.prod__Expression__lit___126_layouts_LAYOUTLIST_Expression_, tmp);
	}
    protected static final void _init_prod__Expression__IntegerConstant__tag__category___67_111_110_115_116_97_110_116(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(207, 0, "IntegerConstant", null, null);
      builder.addAlternative(CParser.prod__Expression__IntegerConstant__tag__category___67_111_110_115_116_97_110_116, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Identifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(169, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(167, 1, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(170, 4, "Identifier", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(168, 2, prod__lit___45_62__char_class___range__45_45_char_class___range__62_62_, new int[] {45,62}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(166, 0, "Expression", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Identifier_, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___42_61_layouts_LAYOUTLIST_Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(431, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(429, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(428, 0, "Expression", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(432, 4, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(430, 2, prod__lit___42_61__char_class___range__42_42_char_class___range__61_61_, new int[] {42,61}, null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___42_61_layouts_LAYOUTLIST_Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(411, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(413, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(412, 2, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(414, 4, "Expression", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(410, 0, "Expression", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_Identifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(193, 4, "Identifier", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(191, 2, prod__lit___46__char_class___range__46_46_, new int[] {46}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(189, 0, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(192, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(190, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_Identifier_, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(381, 0, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(383, 2, prod__lit___124__char_class___range__124_124_, new int[] {124}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(385, 4, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(382, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(384, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(333, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(335, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(334, 2, prod__lit___62_61__char_class___range__62_62_char_class___range__61_61_, new int[] {62,61}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(336, 4, "Expression", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(332, 0, "Expression", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___94_61_layouts_LAYOUTLIST_Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(444, 4, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(442, 2, prod__lit___94_61__char_class___range__94_94_char_class___range__61_61_, new int[] {94,61}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(440, 0, "Expression", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(443, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(441, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___94_61_layouts_LAYOUTLIST_Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__Expression__lit___38_layouts_LAYOUTLIST_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(280, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(279, 0, prod__lit___38__char_class___range__38_38_, new int[] {38}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(281, 2, "Expression", null, null);
      builder.addAlternative(CParser.prod__Expression__lit___38_layouts_LAYOUTLIST_Expression_, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(390, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(388, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(387, 0, "Expression", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(391, 4, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(389, 2, prod__lit___38_38__char_class___range__38_38_char_class___range__38_38_, new int[] {38,38}, null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__lit___40_layouts_LAYOUTLIST_TypeName_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(268, 1, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(272, 5, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(270, 3, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(267, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(273, 6, "Expression", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(269, 2, "TypeName", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(271, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      builder.addAlternative(CParser.prod__Expression__lit___40_layouts_LAYOUTLIST_TypeName_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Expression_, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___37_61_layouts_LAYOUTLIST_Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new LiteralStackNode<IConstructor>(466, 2, prod__lit___37_61__char_class___range__37_37_char_class___range__61_61_, new int[] {37,61}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(468, 4, "Expression", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(464, 0, "Expression", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(465, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(467, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___37_61_layouts_LAYOUTLIST_Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___43_43_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(219, 0, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(221, 2, prod__lit___43_43__char_class___range__43_43_char_class___range__43_43_, new int[] {43,43}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(220, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___43_43_, tmp);
	}
    protected static final void _init_prod__Expression__lit___33_layouts_LAYOUTLIST_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(276, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(275, 0, prod__lit___33__char_class___range__33_33_, new int[] {33}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(277, 2, "Expression", null, null);
      builder.addAlternative(CParser.prod__Expression__lit___33_layouts_LAYOUTLIST_Expression_, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(328, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(326, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(325, 0, "Expression", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(329, 4, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(327, 2, prod__lit___62_62__char_class___range__62_62_char_class___range__62_62_, new int[] {62,62}, null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_62_61_layouts_LAYOUTLIST_Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(417, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(419, 3, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(416, 0, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(418, 2, prod__lit___62_62_61__char_class___range__62_62_char_class___range__62_62_char_class___range__61_61_, new int[] {62,62,61}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(420, 4, "Expression", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_62_61_layouts_LAYOUTLIST_Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__Expression__lit___layouts_LAYOUTLIST_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(256, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(255, 0, prod__lit____char_class___range__45_45_, new int[] {45}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(257, 2, "Expression", null, null);
      builder.addAlternative(CParser.prod__Expression__lit___layouts_LAYOUTLIST_Expression_, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_61_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(341, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(339, 1, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(342, 4, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(340, 2, prod__lit___60_61__char_class___range__60_60_char_class___range__61_61_, new int[] {60,61}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(338, 0, "Expression", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_61_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__lit___42_layouts_LAYOUTLIST_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(264, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(263, 0, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(265, 2, "Expression", null, null);
      builder.addAlternative(CParser.prod__Expression__lit___42_layouts_LAYOUTLIST_Expression_, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___43_61_layouts_LAYOUTLIST_Expression__assoc__right(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(423, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(425, 3, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(422, 0, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(424, 2, prod__lit___43_61__char_class___range__43_43_char_class___range__61_61_, new int[] {43,61}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(426, 4, "Expression", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___43_61_layouts_LAYOUTLIST_Expression__assoc__right, tmp);
	}
    protected static final void _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_Expression__assoc__left(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(322, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(320, 1, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(323, 4, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(321, 2, prod__lit___60_60__char_class___range__60_60_char_class___range__60_60_, new int[] {60,60}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(319, 0, "Expression", null, null);
      builder.addAlternative(CParser.prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_Expression__assoc__left, tmp);
	}
    protected static final void _init_prod__Expression__lit___43_layouts_LAYOUTLIST_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(260, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(259, 0, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(261, 2, "Expression", null, null);
      builder.addAlternative(CParser.prod__Expression__lit___43_layouts_LAYOUTLIST_Expression_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_Expression__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_69_120_112_114_101_115_115_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Expression(builder);
      
        _init_prod__$MetaHole_Expression__char_class___range__0_0_lit___115_111_114_116_40_34_69_120_112_114_101_115_115_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Expression(builder);
      
        _init_prod__bracket_Expression__lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__commaExpression_Expression__Expression_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__multiplicationExpression_Expression__lexp_Expression_layouts_LAYOUTLIST_lit___42_layouts_LAYOUTLIST_rexp_Expression__assoc__left(builder);
      
        _init_prod__sizeOfExpression_Expression__lit_sizeof_layouts_LAYOUTLIST_exp_Expression_(builder);
      
        _init_prod__variable_Expression__Identifier_(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___37_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__NonCommaExpression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___38_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___43_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__HexadecimalConstant__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_60_61_layouts_LAYOUTLIST_Expression__assoc__right(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___45_61_layouts_LAYOUTLIST_Expression__assoc__right(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___33_61_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___47_61_layouts_LAYOUTLIST_Expression__assoc__right(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___63_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Expression__assoc__right(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___38_61_layouts_LAYOUTLIST_Expression__assoc__right(builder);
      
        _init_prod__Expression__CharacterConstant__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___94_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit____(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__lit____layouts_LAYOUTLIST_Expression_(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___10_9_32_32_32_32_32_32_32_32_124_32_61_layouts_LAYOUTLIST_Expression__assoc__right(builder);
      
        _init_prod__Expression__lit_sizeof_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_TypeName_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__Expression__FloatingPointConstant__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___61_61_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__lit___43_43_layouts_LAYOUTLIST_Expression_(builder);
      
        _init_prod__Expression__StringConstant__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___124_124_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__lit___126_layouts_LAYOUTLIST_Expression_(builder);
      
        _init_prod__Expression__IntegerConstant__tag__category___67_111_110_115_116_97_110_116(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Identifier_(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___42_61_layouts_LAYOUTLIST_Expression__assoc__right(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_Expression__assoc__right(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_Identifier_(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_61_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___94_61_layouts_LAYOUTLIST_Expression__assoc__right(builder);
      
        _init_prod__Expression__lit___38_layouts_LAYOUTLIST_Expression_(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___38_38_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__lit___40_layouts_LAYOUTLIST_TypeName_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Expression_(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___37_61_layouts_LAYOUTLIST_Expression__assoc__right(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___43_43_(builder);
      
        _init_prod__Expression__lit___33_layouts_LAYOUTLIST_Expression_(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_62_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___62_62_61_layouts_LAYOUTLIST_Expression__assoc__right(builder);
      
        _init_prod__Expression__lit___layouts_LAYOUTLIST_Expression_(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_61_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__lit___42_layouts_LAYOUTLIST_Expression_(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___43_61_layouts_LAYOUTLIST_Expression__assoc__right(builder);
      
        _init_prod__Expression__Expression_layouts_LAYOUTLIST_lit___60_60_layouts_LAYOUTLIST_Expression__assoc__left(builder);
      
        _init_prod__Expression__lit___43_layouts_LAYOUTLIST_Expression_(builder);
      
    }
  }
	
  protected static class ExternalDeclaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_ExternalDeclaration__char_class___range__0_0_lit___115_111_114_116_40_34_69_120_116_101_114_110_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__ExternalDeclaration(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(3753, 1, prod__lit___115_111_114_116_40_34_69_120_116_101_114_110_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,69,120,116,101,114,110,97,108,68,101,99,108,97,114,97,116,105,111,110,34,41}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(3752, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3754, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(3757, 4, new int[][]{{0,0}}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(3756, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3755, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_ExternalDeclaration__char_class___range__0_0_lit___115_111_114_116_40_34_69_120_116_101_114_110_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__ExternalDeclaration, tmp);
	}
    protected static final void _init_prod__$MetaHole_ExternalDeclaration__char_class___range__0_0_lit___105_116_101_114_40_115_111_114_116_40_34_69_120_116_101_114_110_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__ExternalDeclaration__layouts_LAYOUTLIST(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(3763, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3765, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(3768, 4, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(3764, 1, prod__lit___105_116_101_114_40_115_111_114_116_40_34_69_120_116_101_114_110_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41_41__char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__110_110_char_class___range__97_97_char_class___range__108_108_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {105,116,101,114,40,115,111,114,116,40,34,69,120,116,101,114,110,97,108,68,101,99,108,97,114,97,116,105,111,110,34,41,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(3767, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3766, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_ExternalDeclaration__char_class___range__0_0_lit___105_116_101_114_40_115_111_114_116_40_34_69_120_116_101_114_110_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__ExternalDeclaration__layouts_LAYOUTLIST, tmp);
	}
    protected static final void _init_prod__ExternalDeclaration__FunctionPrototype_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3773, 0, "FunctionPrototype", null, null);
      builder.addAlternative(CParser.prod__ExternalDeclaration__FunctionPrototype_, tmp);
	}
    protected static final void _init_prod__ExternalDeclaration__FunctionDefinition_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3749, 0, "FunctionDefinition", null, null);
      builder.addAlternative(CParser.prod__ExternalDeclaration__FunctionDefinition_, tmp);
	}
    protected static final void _init_prod__ExternalDeclaration__GlobalDeclaration_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3760, 0, "GlobalDeclaration", null, null);
      builder.addAlternative(CParser.prod__ExternalDeclaration__GlobalDeclaration_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_ExternalDeclaration__char_class___range__0_0_lit___115_111_114_116_40_34_69_120_116_101_114_110_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__ExternalDeclaration(builder);
      
        _init_prod__$MetaHole_ExternalDeclaration__char_class___range__0_0_lit___105_116_101_114_40_115_111_114_116_40_34_69_120_116_101_114_110_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__ExternalDeclaration__layouts_LAYOUTLIST(builder);
      
        _init_prod__ExternalDeclaration__FunctionPrototype_(builder);
      
        _init_prod__ExternalDeclaration__FunctionDefinition_(builder);
      
        _init_prod__ExternalDeclaration__GlobalDeclaration_(builder);
      
    }
  }
	
  protected static class FunctionDefinition {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_FunctionDefinition__char_class___range__0_0_lit___115_111_114_116_40_34_70_117_110_99_116_105_111_110_68_101_102_105_110_105_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__FunctionDefinition(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(2784, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(2783, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new CharStackNode<IConstructor>(2780, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2782, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(2781, 1, prod__lit___115_111_114_116_40_34_70_117_110_99_116_105_111_110_68_101_102_105_110_105_116_105_111_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__70_70_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__68_68_char_class___range__101_101_char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__105_105_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,70,117,110,99,116,105,111,110,68,101,102,105,110,105,116,105,111,110,34,41}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(2785, 4, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_FunctionDefinition__char_class___range__0_0_lit___115_111_114_116_40_34_70_117_110_99_116_105_111_110_68_101_102_105_110_105_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__FunctionDefinition, tmp);
	}
    protected static final void _init_prod__defaultFunctionDefinition_FunctionDefinition__specs_iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Declarator_layouts_LAYOUTLIST_iter_star_seps__Declaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__Declaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new LiteralStackNode<IConstructor>(2810, 12, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(2798, 4, regular__iter_star_seps__Declaration__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2796, 0, "Declaration", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2797, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(2800, 6, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(2794, 2, "Declarator", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(2801, 7, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(2791, 0, regular__iter_star_seps__Specifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2789, 0, "Specifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2790, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[10] = new SeparatedListStackNode<IConstructor>(2808, 10, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2806, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2807, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(2805, 9, "layouts_LAYOUTLIST", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(2809, 11, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2795, 3, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(2799, 5, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2793, 1, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new SeparatedListStackNode<IConstructor>(2804, 8, regular__iter_star_seps__Declaration__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2802, 0, "Declaration", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2803, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(CParser.prod__defaultFunctionDefinition_FunctionDefinition__specs_iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Declarator_layouts_LAYOUTLIST_iter_star_seps__Declaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__Declaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_FunctionDefinition__char_class___range__0_0_lit___115_111_114_116_40_34_70_117_110_99_116_105_111_110_68_101_102_105_110_105_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__FunctionDefinition(builder);
      
        _init_prod__defaultFunctionDefinition_FunctionDefinition__specs_iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Declarator_layouts_LAYOUTLIST_iter_star_seps__Declaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__Declaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
    }
  }
	
  protected static class FunctionPrototype {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_FunctionPrototype__char_class___range__0_0_lit___115_111_114_116_40_34_70_117_110_99_116_105_111_110_80_114_111_116_111_116_121_112_101_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__FunctionPrototype(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(3046, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3043, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(3041, 0, new int[][]{{0,0}}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(3045, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3044, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(3042, 1, prod__lit___115_111_114_116_40_34_70_117_110_99_116_105_111_110_80_114_111_116_111_116_121_112_101_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__70_70_char_class___range__117_117_char_class___range__110_110_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__80_80_char_class___range__114_114_char_class___range__111_111_char_class___range__116_116_char_class___range__111_111_char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,70,117,110,99,116,105,111,110,80,114,111,116,111,116,121,112,101,34,41}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_FunctionPrototype__char_class___range__0_0_lit___115_111_114_116_40_34_70_117_110_99_116_105_111_110_80_114_111_116_111_116_121_112_101_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__FunctionPrototype, tmp);
	}
    protected static final void _init_prod__defaultFunctionPrototype_FunctionPrototype__specs_iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_PrototypeDeclarator_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(3052, 0, regular__iter_star_seps__Specifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3050, 0, "Specifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3051, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(3058, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(3055, 2, "PrototypeDeclarator", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3057, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3054, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__defaultFunctionPrototype_FunctionPrototype__specs_iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_PrototypeDeclarator_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_FunctionPrototype__char_class___range__0_0_lit___115_111_114_116_40_34_70_117_110_99_116_105_111_110_80_114_111_116_111_116_121_112_101_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__FunctionPrototype(builder);
      
        _init_prod__defaultFunctionPrototype_FunctionPrototype__specs_iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_PrototypeDeclarator_layouts_LAYOUTLIST_lit___59_(builder);
      
    }
  }
	
  protected static class GlobalDeclaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_GlobalDeclaration__char_class___range__0_0_lit___115_111_114_116_40_34_71_108_111_98_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__GlobalDeclaration(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(2158, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2155, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(2154, 1, prod__lit___115_111_114_116_40_34_71_108_111_98_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__71_71_char_class___range__108_108_char_class___range__111_111_char_class___range__98_98_char_class___range__97_97_char_class___range__108_108_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,71,108,111,98,97,108,68,101,99,108,97,114,97,116,105,111,110,34,41}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(2153, 0, new int[][]{{0,0}}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(2157, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(2156, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_GlobalDeclaration__char_class___range__0_0_lit___115_111_114_116_40_34_71_108_111_98_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__GlobalDeclaration, tmp);
	}
    protected static final void _init_prod__globalDeclarationWithInitDecls_GlobalDeclaration__specs0_iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_initDeclarators_iter_seps__InitDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(2134, 1, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(2142, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(2139, 2, regular__iter_seps__InitDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2135, 0, "InitDeclarator", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2136, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(2137, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(2138, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(2141, 3, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(2132, 0, regular__iter_star_seps__Specifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2130, 0, "Specifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2131, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(CParser.prod__globalDeclarationWithInitDecls_GlobalDeclaration__specs0_iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_initDeclarators_iter_seps__InitDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__globalDeclarationWithoutInitDecls_GlobalDeclaration__specs1_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(2149, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(2147, 0, regular__iter_seps__Specifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2145, 0, "Specifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2146, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2150, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(CParser.prod__globalDeclarationWithoutInitDecls_GlobalDeclaration__specs1_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_GlobalDeclaration__char_class___range__0_0_lit___115_111_114_116_40_34_71_108_111_98_97_108_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__GlobalDeclaration(builder);
      
        _init_prod__globalDeclarationWithInitDecls_GlobalDeclaration__specs0_iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_initDeclarators_iter_seps__InitDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__globalDeclarationWithoutInitDecls_GlobalDeclaration__specs1_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(builder);
      
    }
  }
	
  protected static class InitDeclarator {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_InitDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_73_110_105_116_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__InitDeclarator(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(3919, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3921, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(3924, 4, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(3920, 1, prod__lit___115_111_114_116_40_34_73_110_105_116_68_101_99_108_97_114_97_116_111_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__73_73_char_class___range__110_110_char_class___range__105_105_char_class___range__116_116_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,73,110,105,116,68,101,99,108,97,114,97,116,111,114,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(3923, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3922, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_InitDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_73_110_105_116_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__InitDeclarator, tmp);
	}
    protected static final void _init_prod__$MetaHole_InitDeclarator__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_73_110_105_116_68_101_99_108_97_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__InitDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(3904, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3901, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(3899, 0, new int[][]{{0,0}}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(3903, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3902, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(3900, 1, prod__lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_73_110_105_116_68_101_99_108_97_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__101_101_char_class___range__112_112_char_class___range__115_115_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__73_73_char_class___range__110_110_char_class___range__105_105_char_class___range__116_116_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__44_44_char_class___range__91_91_char_class___range__108_108_char_class___range__105_105_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__44_44_char_class___range__34_34_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_, new int[] {92,105,116,101,114,45,115,101,112,115,40,115,111,114,116,40,34,73,110,105,116,68,101,99,108,97,114,97,116,111,114,34,41,44,91,108,105,116,40,34,44,34,41,93,41}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_InitDeclarator__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_73_110_105_116_68_101_99_108_97_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__InitDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, tmp);
	}
    protected static final void _init_prod__InitDeclarator__decl_Declarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3895, 0, "Declarator", null, null);
      builder.addAlternative(CParser.prod__InitDeclarator__decl_Declarator_, tmp);
	}
    protected static final void _init_prod__InitDeclarator__decl_Declarator_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_Initializer_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(3911, 0, "Declarator", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3914, 2, prod__lit___61__char_class___range__61_61_, new int[] {61}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3913, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3915, 3, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(3916, 4, "Initializer", null, null);
      builder.addAlternative(CParser.prod__InitDeclarator__decl_Declarator_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_Initializer_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_InitDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_73_110_105_116_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__InitDeclarator(builder);
      
        _init_prod__$MetaHole_InitDeclarator__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_73_110_105_116_68_101_99_108_97_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__InitDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST(builder);
      
        _init_prod__InitDeclarator__decl_Declarator_(builder);
      
        _init_prod__InitDeclarator__decl_Declarator_layouts_LAYOUTLIST_lit___61_layouts_LAYOUTLIST_Initializer_(builder);
      
    }
  }
	
  protected static class Initializer {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_Initializer__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_73_110_105_116_105_97_108_105_122_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Initializer__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(4316, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4318, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4321, 4, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(4317, 1, prod__lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_73_110_105_116_105_97_108_105_122_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__101_101_char_class___range__112_112_char_class___range__115_115_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__73_73_char_class___range__110_110_char_class___range__105_105_char_class___range__116_116_char_class___range__105_105_char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__122_122_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__44_44_char_class___range__91_91_char_class___range__108_108_char_class___range__105_105_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__44_44_char_class___range__34_34_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_, new int[] {92,105,116,101,114,45,115,101,112,115,40,115,111,114,116,40,34,73,110,105,116,105,97,108,105,122,101,114,34,41,44,91,108,105,116,40,34,44,34,41,93,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(4320, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4319, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Initializer__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_73_110_105_116_105_97_108_105_122_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Initializer__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, tmp);
	}
    protected static final void _init_prod__$MetaHole_Initializer__char_class___range__0_0_lit___115_111_114_116_40_34_73_110_105_116_105_97_108_105_122_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Initializer(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(4329, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4334, 4, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(4330, 1, prod__lit___115_111_114_116_40_34_73_110_105_116_105_97_108_105_122_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__73_73_char_class___range__110_110_char_class___range__105_105_char_class___range__116_116_char_class___range__105_105_char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__122_122_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,73,110,105,116,105,97,108,105,122,101,114,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(4333, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4332, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4331, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Initializer__char_class___range__0_0_lit___115_111_114_116_40_34_73_110_105_116_105_97_108_105_122_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Initializer, tmp);
	}
    protected static final void _init_prod__Initializer__lit___123_layouts_LAYOUTLIST_iter_seps__Initializer__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_opt__lit___44_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(4307, 3, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4300, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(4306, 2, regular__iter_seps__Initializer__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4302, 0, "Initializer", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4303, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(4304, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(4305, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4310, 5, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4301, 1, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new OptionalStackNode<IConstructor>(4309, 4, regular__opt__lit___44, new LiteralStackNode<IConstructor>(4308, 0, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(4311, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      builder.addAlternative(CParser.prod__Initializer__lit___123_layouts_LAYOUTLIST_iter_seps__Initializer__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_opt__lit___44_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Initializer__NonCommaExpression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4313, 0, "NonCommaExpression", null, null);
      builder.addAlternative(CParser.prod__Initializer__NonCommaExpression_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_Initializer__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_73_110_105_116_105_97_108_105_122_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Initializer__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST(builder);
      
        _init_prod__$MetaHole_Initializer__char_class___range__0_0_lit___115_111_114_116_40_34_73_110_105_116_105_97_108_105_122_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Initializer(builder);
      
        _init_prod__Initializer__lit___123_layouts_LAYOUTLIST_iter_seps__Initializer__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_opt__lit___44_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__Initializer__NonCommaExpression_(builder);
      
    }
  }
	
  protected static class MoreParameters {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_MoreParameters__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_77_111_114_101_80_97_114_97_109_101_116_101_114_115_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__MoreParameters(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(936, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(941, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(938, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(940, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(939, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(937, 1, prod__lit___111_112_116_40_115_111_114_116_40_34_77_111_114_101_80_97_114_97_109_101_116_101_114_115_34_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__77_77_char_class___range__111_111_char_class___range__114_114_char_class___range__101_101_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__115_115_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {111,112,116,40,115,111,114,116,40,34,77,111,114,101,80,97,114,97,109,101,116,101,114,115,34,41,41}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_MoreParameters__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_77_111_114_101_80_97_114_97_109_101_116_101_114_115_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__MoreParameters, tmp);
	}
    protected static final void _init_prod__$MetaHole_MoreParameters__char_class___range__0_0_lit___115_111_114_116_40_34_77_111_114_101_80_97_114_97_109_101_116_101_114_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__MoreParameters(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(924, 1, prod__lit___115_111_114_116_40_34_77_111_114_101_80_97_114_97_109_101_116_101_114_115_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__77_77_char_class___range__111_111_char_class___range__114_114_char_class___range__101_101_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__115_115_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,77,111,114,101,80,97,114,97,109,101,116,101,114,115,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(927, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(926, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(925, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(928, 4, new int[][]{{0,0}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(923, 0, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_MoreParameters__char_class___range__0_0_lit___115_111_114_116_40_34_77_111_114_101_80_97_114_97_109_101_116_101_114_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__MoreParameters, tmp);
	}
    protected static final void _init_prod__MoreParameters__lit___44_layouts_LAYOUTLIST_lit___46_46_46_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(932, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(931, 0, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(933, 2, prod__lit___46_46_46__char_class___range__46_46_char_class___range__46_46_char_class___range__46_46_, new int[] {46,46,46}, null, null);
      builder.addAlternative(CParser.prod__MoreParameters__lit___44_layouts_LAYOUTLIST_lit___46_46_46_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_MoreParameters__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_77_111_114_101_80_97_114_97_109_101_116_101_114_115_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__MoreParameters(builder);
      
        _init_prod__$MetaHole_MoreParameters__char_class___range__0_0_lit___115_111_114_116_40_34_77_111_114_101_80_97_114_97_109_101_116_101_114_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__MoreParameters(builder);
      
        _init_prod__MoreParameters__lit___44_layouts_LAYOUTLIST_lit___46_46_46_(builder);
      
    }
  }
	
  protected static class NonCommaExpression {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_NonCommaExpression__char_class___range__0_0_lit___115_111_114_116_40_34_78_111_110_67_111_109_109_97_69_120_112_114_101_115_115_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__NonCommaExpression(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(4093, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4092, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(4090, 1, prod__lit___115_111_114_116_40_34_78_111_110_67_111_109_109_97_69_120_112_114_101_115_115_105_111_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__78_78_char_class___range__111_111_char_class___range__110_110_char_class___range__67_67_char_class___range__111_111_char_class___range__109_109_char_class___range__109_109_char_class___range__97_97_char_class___range__69_69_char_class___range__120_120_char_class___range__112_112_char_class___range__114_114_char_class___range__101_101_char_class___range__115_115_char_class___range__115_115_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,78,111,110,67,111,109,109,97,69,120,112,114,101,115,115,105,111,110,34,41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4091, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4089, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4094, 4, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_NonCommaExpression__char_class___range__0_0_lit___115_111_114_116_40_34_78_111_110_67_111_109_109_97_69_120_112_114_101_115_115_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__NonCommaExpression, tmp);
	}
    protected static final void _init_prod__$MetaHole_NonCommaExpression__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_45_115_101_112_115_40_115_111_114_116_40_34_78_111_110_67_111_109_109_97_69_120_112_114_101_115_115_105_111_110_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__NonCommaExpression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(4076, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4075, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(4073, 1, prod__lit___92_105_116_101_114_45_115_116_97_114_45_115_101_112_115_40_115_111_114_116_40_34_78_111_110_67_111_109_109_97_69_120_112_114_101_115_115_105_111_110_34_41_44_91_108_105_116_40_34_44_34_41_93_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__101_101_char_class___range__112_112_char_class___range__115_115_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__78_78_char_class___range__111_111_char_class___range__110_110_char_class___range__67_67_char_class___range__111_111_char_class___range__109_109_char_class___range__109_109_char_class___range__97_97_char_class___range__69_69_char_class___range__120_120_char_class___range__112_112_char_class___range__114_114_char_class___range__101_101_char_class___range__115_115_char_class___range__115_115_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_char_class___range__44_44_char_class___range__91_91_char_class___range__108_108_char_class___range__105_105_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__44_44_char_class___range__34_34_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_, new int[] {92,105,116,101,114,45,115,116,97,114,45,115,101,112,115,40,115,111,114,116,40,34,78,111,110,67,111,109,109,97,69,120,112,114,101,115,115,105,111,110,34,41,44,91,108,105,116,40,34,44,34,41,93,41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4074, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4077, 4, new int[][]{{0,0}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4072, 0, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_NonCommaExpression__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_45_115_101_112_115_40_115_111_114_116_40_34_78_111_110_67_111_109_109_97_69_120_112_114_101_115_115_105_111_110_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__NonCommaExpression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, tmp);
	}
    protected static final void _init_prod__nonCommaExpression_NonCommaExpression__expr_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4085, 0, "Expression", null, null);
      builder.addAlternative(CParser.prod__nonCommaExpression_NonCommaExpression__expr_Expression_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_NonCommaExpression__char_class___range__0_0_lit___115_111_114_116_40_34_78_111_110_67_111_109_109_97_69_120_112_114_101_115_115_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__NonCommaExpression(builder);
      
        _init_prod__$MetaHole_NonCommaExpression__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_45_115_101_112_115_40_115_111_114_116_40_34_78_111_110_67_111_109_109_97_69_120_112_114_101_115_115_105_111_110_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__NonCommaExpression__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST(builder);
      
        _init_prod__nonCommaExpression_NonCommaExpression__expr_Expression_(builder);
      
    }
  }
	
  protected static class Parameter {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_Parameter__char_class___range__0_0_lit___115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Parameter(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(2338, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2335, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(2337, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(2336, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new CharStackNode<IConstructor>(2333, 0, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(2334, 1, prod__lit___115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,80,97,114,97,109,101,116,101,114,34,41}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Parameter__char_class___range__0_0_lit___115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Parameter, tmp);
	}
    protected static final void _init_prod__$MetaHole_Parameter__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Parameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(2347, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2344, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(2342, 0, new int[][]{{0,0}}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(2346, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(2345, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(2343, 1, prod__lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__101_101_char_class___range__112_112_char_class___range__115_115_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__44_44_char_class___range__91_91_char_class___range__108_108_char_class___range__105_105_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__44_44_char_class___range__34_34_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_, new int[] {92,105,116,101,114,45,115,101,112,115,40,115,111,114,116,40,34,80,97,114,97,109,101,116,101,114,34,41,44,91,108,105,116,40,34,44,34,41,93,41}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Parameter__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Parameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, tmp);
	}
    protected static final void _init_prod__Parameter__iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Declarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(2330, 2, "Declarator", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(2329, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(2328, 0, regular__iter_star_seps__Specifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2326, 0, "Specifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2327, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(CParser.prod__Parameter__iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Declarator_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_Parameter__char_class___range__0_0_lit___115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Parameter(builder);
      
        _init_prod__$MetaHole_Parameter__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Parameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST(builder);
      
        _init_prod__Parameter__iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Declarator_(builder);
      
    }
  }
	
  protected static class Parameters {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_Parameters__char_class___range__0_0_lit___115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Parameters(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(2929, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(2928, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(2926, 1, prod__lit___115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_115_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__115_115_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,80,97,114,97,109,101,116,101,114,115,34,41}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(2925, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(2930, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2927, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Parameters__char_class___range__0_0_lit___115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Parameters, tmp);
	}
    protected static final void _init_prod__$MetaHole_Parameters__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_115_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Parameters(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(2907, 1, prod__lit___111_112_116_40_115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_115_34_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__115_115_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {111,112,116,40,115,111,114,116,40,34,80,97,114,97,109,101,116,101,114,115,34,41,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(2910, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(2909, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(2908, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(2911, 4, new int[][]{{0,0}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(2906, 0, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Parameters__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_115_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Parameters, tmp);
	}
    protected static final void _init_prod__Parameters__iter_seps__Parameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_opt__MoreParameters_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(2920, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(2919, 0, regular__iter_seps__Parameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(2915, 0, "Parameter", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(2916, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(2917, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(2918, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[2] = new OptionalStackNode<IConstructor>(2922, 2, regular__opt__MoreParameters, new NonTerminalStackNode<IConstructor>(2921, 0, "MoreParameters", null, null), null, null);
      builder.addAlternative(CParser.prod__Parameters__iter_seps__Parameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_opt__MoreParameters_, tmp);
	}
    protected static final void _init_prod__Parameters__lit_void_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(2933, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new int[] {118,111,105,100}, null, null);
      builder.addAlternative(CParser.prod__Parameters__lit_void_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_Parameters__char_class___range__0_0_lit___115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Parameters(builder);
      
        _init_prod__$MetaHole_Parameters__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_80_97_114_97_109_101_116_101_114_115_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__Parameters(builder);
      
        _init_prod__Parameters__iter_seps__Parameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_opt__MoreParameters_(builder);
      
        _init_prod__Parameters__lit_void_(builder);
      
    }
  }
	
  protected static class PrototypeDeclarator {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_PrototypeDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__PrototypeDeclarator(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(1543, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1540, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(1538, 0, new int[][]{{0,0}}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(1542, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(1541, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(1539, 1, prod__lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_68_101_99_108_97_114_97_116_111_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__114_114_char_class___range__111_111_char_class___range__116_116_char_class___range__111_111_char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,80,114,111,116,111,116,121,112,101,68,101,99,108,97,114,97,116,111,114,34,41}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_PrototypeDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__PrototypeDeclarator, tmp);
	}
    protected static final void _init_prod__arrayDeclarator_PrototypeDeclarator__proto__decl_PrototypeDeclarator_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_exp_opt__Expression_layouts_LAYOUTLIST_lit___93_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[6] = new LiteralStackNode<IConstructor>(1504, 6, prod__lit___93__char_class___range__93_93_, new int[] {93}, null, null);
      tmp[4] = new OptionalStackNode<IConstructor>(1501, 4, regular__opt__Expression, new NonTerminalStackNode<IConstructor>(1500, 0, "Expression", null, null), null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1498, 2, prod__lit___91__char_class___range__91_91_, new int[] {91}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(1495, 0, "PrototypeDeclarator", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1499, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1497, 1, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1503, 5, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__arrayDeclarator_PrototypeDeclarator__proto__decl_PrototypeDeclarator_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_exp_opt__Expression_layouts_LAYOUTLIST_lit___93_, tmp);
	}
    protected static final void _init_prod__bracket_PrototypeDeclarator__lit___40_layouts_LAYOUTLIST_abs__decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___41__bracket(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(1520, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1519, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(1524, 4, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(1521, 2, "AbstractDeclarator", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1523, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__bracket_PrototypeDeclarator__lit___40_layouts_LAYOUTLIST_abs__decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___41__bracket, tmp);
	}
    protected static final void _init_prod__functionDeclarator_PrototypeDeclarator__proto__decl_PrototypeDeclarator_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_params_opt__PrototypeParameters_layouts_LAYOUTLIST_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1507, 0, "PrototypeDeclarator", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(1516, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1510, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[4] = new OptionalStackNode<IConstructor>(1513, 4, regular__opt__PrototypeParameters, new NonTerminalStackNode<IConstructor>(1512, 0, "PrototypeParameters", null, null), null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(1509, 1, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(1515, 5, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1511, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__functionDeclarator_PrototypeDeclarator__proto__decl_PrototypeDeclarator_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_params_opt__PrototypeParameters_layouts_LAYOUTLIST_lit___41_, tmp);
	}
    protected static final void _init_prod__identifier_PrototypeDeclarator__Identifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1492, 0, "Identifier", null, null);
      builder.addAlternative(CParser.prod__identifier_PrototypeDeclarator__Identifier_, tmp);
	}
    protected static final void _init_prod__pointerDeclarator_PrototypeDeclarator__lit___42_layouts_LAYOUTLIST_qualifiers_iter_star_seps__TypeQualifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_PrototypeDeclarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(1528, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(1533, 3, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(1527, 0, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(1531, 2, regular__iter_star_seps__TypeQualifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1529, 0, "TypeQualifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1530, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(1534, 4, "PrototypeDeclarator", null, null);
      builder.addAlternative(CParser.prod__pointerDeclarator_PrototypeDeclarator__lit___42_layouts_LAYOUTLIST_qualifiers_iter_star_seps__TypeQualifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_PrototypeDeclarator_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_PrototypeDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__PrototypeDeclarator(builder);
      
        _init_prod__arrayDeclarator_PrototypeDeclarator__proto__decl_PrototypeDeclarator_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_exp_opt__Expression_layouts_LAYOUTLIST_lit___93_(builder);
      
        _init_prod__bracket_PrototypeDeclarator__lit___40_layouts_LAYOUTLIST_abs__decl_AbstractDeclarator_layouts_LAYOUTLIST_lit___41__bracket(builder);
      
        _init_prod__functionDeclarator_PrototypeDeclarator__proto__decl_PrototypeDeclarator_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_params_opt__PrototypeParameters_layouts_LAYOUTLIST_lit___41_(builder);
      
        _init_prod__identifier_PrototypeDeclarator__Identifier_(builder);
      
        _init_prod__pointerDeclarator_PrototypeDeclarator__lit___42_layouts_LAYOUTLIST_qualifiers_iter_star_seps__TypeQualifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_decl_PrototypeDeclarator_(builder);
      
    }
  }
	
  protected static class PrototypeParameter {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_PrototypeParameter__char_class___range__0_0_lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__PrototypeParameter(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(5198, 4, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(5194, 1, prod__lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__114_114_char_class___range__111_111_char_class___range__116_116_char_class___range__111_111_char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,80,114,111,116,111,116,121,112,101,80,97,114,97,109,101,116,101,114,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(5197, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(5196, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new CharStackNode<IConstructor>(5193, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5195, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_PrototypeParameter__char_class___range__0_0_lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__PrototypeParameter, tmp);
	}
    protected static final void _init_prod__$MetaHole_PrototypeParameter__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__PrototypeParameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(5179, 4, new int[][]{{0,0}}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(5178, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(5177, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(5175, 1, prod__lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__101_101_char_class___range__112_112_char_class___range__115_115_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__114_114_char_class___range__111_111_char_class___range__116_116_char_class___range__111_111_char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__44_44_char_class___range__91_91_char_class___range__108_108_char_class___range__105_105_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__44_44_char_class___range__34_34_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_, new int[] {92,105,116,101,114,45,115,101,112,115,40,115,111,114,116,40,34,80,114,111,116,111,116,121,112,101,80,97,114,97,109,101,116,101,114,34,41,44,91,108,105,116,40,34,44,34,41,93,41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5176, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(5174, 0, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_PrototypeParameter__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__PrototypeParameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, tmp);
	}
    protected static final void _init_prod__PrototypeParameter__iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_AbstractDeclarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(5190, 2, "AbstractDeclarator", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(5188, 0, regular__iter_star_seps__Specifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(5186, 0, "Specifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(5187, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5189, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__PrototypeParameter__iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_AbstractDeclarator_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_PrototypeParameter__char_class___range__0_0_lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__PrototypeParameter(builder);
      
        _init_prod__$MetaHole_PrototypeParameter__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__PrototypeParameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST(builder);
      
        _init_prod__PrototypeParameter__iter_star_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_AbstractDeclarator_(builder);
      
    }
  }
	
  protected static class PrototypeParameters {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_PrototypeParameters__char_class___range__0_0_lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__PrototypeParameters(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(3339, 1, prod__lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_115_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__114_114_char_class___range__111_111_char_class___range__116_116_char_class___range__111_111_char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__115_115_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,80,114,111,116,111,116,121,112,101,80,97,114,97,109,101,116,101,114,115,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(3342, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3341, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3340, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(3338, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(3343, 4, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_PrototypeParameters__char_class___range__0_0_lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__PrototypeParameters, tmp);
	}
    protected static final void _init_prod__$MetaHole_PrototypeParameters__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_115_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__PrototypeParameters(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(3320, 1, prod__lit___111_112_116_40_115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_115_34_41_41__char_class___range__111_111_char_class___range__112_112_char_class___range__116_116_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__80_80_char_class___range__114_114_char_class___range__111_111_char_class___range__116_116_char_class___range__111_111_char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__80_80_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__115_115_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {111,112,116,40,115,111,114,116,40,34,80,114,111,116,111,116,121,112,101,80,97,114,97,109,101,116,101,114,115,34,41,41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3321, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(3323, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3322, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[4] = new CharStackNode<IConstructor>(3324, 4, new int[][]{{0,0}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(3319, 0, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_PrototypeParameters__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_115_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__PrototypeParameters, tmp);
	}
    protected static final void _init_prod__PrototypeParameters__iter_seps__PrototypeParameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_opt__MoreParameters_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new OptionalStackNode<IConstructor>(3335, 2, regular__opt__MoreParameters, new NonTerminalStackNode<IConstructor>(3334, 0, "MoreParameters", null, null), null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(3332, 0, regular__iter_seps__PrototypeParameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3328, 0, "PrototypeParameter", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3329, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(3330, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(3331, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3333, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__PrototypeParameters__iter_seps__PrototypeParameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_opt__MoreParameters_, tmp);
	}
    protected static final void _init_prod__PrototypeParameters__lit_void_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3346, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new int[] {118,111,105,100}, null, null);
      builder.addAlternative(CParser.prod__PrototypeParameters__lit_void_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_PrototypeParameters__char_class___range__0_0_lit___115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__PrototypeParameters(builder);
      
        _init_prod__$MetaHole_PrototypeParameters__char_class___range__0_0_lit___111_112_116_40_115_111_114_116_40_34_80_114_111_116_111_116_121_112_101_80_97_114_97_109_101_116_101_114_115_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__opt__PrototypeParameters(builder);
      
        _init_prod__PrototypeParameters__iter_seps__PrototypeParameter__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_opt__MoreParameters_(builder);
      
        _init_prod__PrototypeParameters__lit_void_(builder);
      
    }
  }
	
  protected static class Specifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_Specifier__char_class___range__0_0_lit___115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Specifier(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(1058, 1, prod__lit___115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__112_112_char_class___range__101_101_char_class___range__99_99_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,83,112,101,99,105,102,105,101,114,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(1061, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(1060, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new CharStackNode<IConstructor>(1057, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1059, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(1062, 4, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Specifier__char_class___range__0_0_lit___115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Specifier, tmp);
	}
    protected static final void _init_prod__$MetaHole_Specifier__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__Specifier__layouts_LAYOUTLIST(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new LiteralStackNode<IConstructor>(1068, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(1066, 0, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(1067, 1, prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__112_112_char_class___range__101_101_char_class___range__99_99_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {92,105,116,101,114,45,115,116,97,114,40,115,111,114,116,40,34,83,112,101,99,105,102,105,101,114,34,41,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(1070, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(1069, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[4] = new CharStackNode<IConstructor>(1071, 4, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Specifier__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__Specifier__layouts_LAYOUTLIST, tmp);
	}
    protected static final void _init_prod__$MetaHole_Specifier__char_class___range__0_0_lit___105_116_101_114_40_115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Specifier__layouts_LAYOUTLIST(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(1047, 1, prod__lit___105_116_101_114_40_115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_41__char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__112_112_char_class___range__101_101_char_class___range__99_99_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {105,116,101,114,40,115,111,114,116,40,34,83,112,101,99,105,102,105,101,114,34,41,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(1050, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(1049, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new CharStackNode<IConstructor>(1046, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1048, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(1051, 4, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Specifier__char_class___range__0_0_lit___105_116_101_114_40_115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Specifier__layouts_LAYOUTLIST, tmp);
	}
    protected static final void _init_prod__storageClass_Specifier__StorageClass_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1080, 0, "StorageClass", null, null);
      builder.addAlternative(CParser.prod__storageClass_Specifier__StorageClass_, tmp);
	}
    protected static final void _init_prod__typeQualifier_Specifier__TypeQualifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1077, 0, "TypeQualifier", null, null);
      builder.addAlternative(CParser.prod__typeQualifier_Specifier__TypeQualifier_, tmp);
	}
    protected static final void _init_prod__typeSpecifier_Specifier__TypeSpecifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(1043, 0, "TypeSpecifier", null, null);
      builder.addAlternative(CParser.prod__typeSpecifier_Specifier__TypeSpecifier_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_Specifier__char_class___range__0_0_lit___115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Specifier(builder);
      
        _init_prod__$MetaHole_Specifier__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__Specifier__layouts_LAYOUTLIST(builder);
      
        _init_prod__$MetaHole_Specifier__char_class___range__0_0_lit___105_116_101_114_40_115_111_114_116_40_34_83_112_101_99_105_102_105_101_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__Specifier__layouts_LAYOUTLIST(builder);
      
        _init_prod__storageClass_Specifier__StorageClass_(builder);
      
        _init_prod__typeQualifier_Specifier__TypeQualifier_(builder);
      
        _init_prod__typeSpecifier_Specifier__TypeSpecifier_(builder);
      
    }
  }
	
  protected static class Statement {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_Statement__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_97_116_101_109_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__Statement__layouts_LAYOUTLIST(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(3569, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3568, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(3566, 1, prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_97_116_101_109_101_110_116_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__109_109_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {92,105,116,101,114,45,115,116,97,114,40,115,111,114,116,40,34,83,116,97,116,101,109,101,110,116,34,41,41}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(3570, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3567, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(3565, 0, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Statement__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_97_116_101_109_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__Statement__layouts_LAYOUTLIST, tmp);
	}
    protected static final void _init_prod__$MetaHole_Statement__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_97_116_101_109_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Statement(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(3652, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3649, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(3647, 0, new int[][]{{0,0}}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(3651, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(3650, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(3648, 1, prod__lit___115_111_114_116_40_34_83_116_97_116_101_109_101_110_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__97_97_char_class___range__116_116_char_class___range__101_101_char_class___range__109_109_char_class___range__101_101_char_class___range__110_110_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,83,116,97,116,101,109,101,110,116,34,41}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_Statement__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_97_116_101_109_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Statement, tmp);
	}
    protected static final void _init_prod__Statement__lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_opt__Expression_layouts_LAYOUTLIST_lit___59_layouts_LAYOUTLIST_opt__Expression_layouts_LAYOUTLIST_lit___59_layouts_LAYOUTLIST_opt__Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[17];
      
      tmp[13] = new NonTerminalStackNode<IConstructor>(3621, 13, "layouts_LAYOUTLIST", null, null);
      tmp[15] = new NonTerminalStackNode<IConstructor>(3623, 15, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new OptionalStackNode<IConstructor>(3615, 8, regular__opt__Expression, new NonTerminalStackNode<IConstructor>(3614, 0, "Expression", null, null), null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3608, 3, "layouts_LAYOUTLIST", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(3618, 11, "layouts_LAYOUTLIST", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3613, 7, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3606, 1, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3611, 5, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3605, 0, prod__lit_for__char_class___range__102_102_char_class___range__111_111_char_class___range__114_114_, new int[] {102,111,114}, null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(3617, 10, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[16] = new NonTerminalStackNode<IConstructor>(3624, 16, "Statement", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(3616, 9, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new OptionalStackNode<IConstructor>(3610, 4, regular__opt__Expression, new NonTerminalStackNode<IConstructor>(3609, 0, "Expression", null, null), null, null);
      tmp[14] = new LiteralStackNode<IConstructor>(3622, 14, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[12] = new OptionalStackNode<IConstructor>(3620, 12, regular__opt__Expression, new NonTerminalStackNode<IConstructor>(3619, 0, "Expression", null, null), null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3607, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(3612, 6, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(CParser.prod__Statement__lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_opt__Expression_layouts_LAYOUTLIST_lit___59_layouts_LAYOUTLIST_opt__Expression_layouts_LAYOUTLIST_lit___59_layouts_LAYOUTLIST_opt__Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_, tmp);
	}
    protected static final void _init_prod__Statement__lit_return_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(3627, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3626, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new int[] {114,101,116,117,114,110}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3628, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(CParser.prod__Statement__lit_return_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Statement__lit_goto_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(3586, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3588, 3, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3585, 0, prod__lit_goto__char_class___range__103_103_char_class___range__111_111_char_class___range__116_116_char_class___range__111_111_, new int[] {103,111,116,111}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(3587, 2, "Identifier", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(3589, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(CParser.prod__Statement__lit_goto_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Statement__lit_do_layouts_LAYOUTLIST_Statement_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[8] = new NonTerminalStackNode<IConstructor>(3599, 8, "Expression", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3598, 7, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3596, 5, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3592, 1, "layouts_LAYOUTLIST", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(3602, 11, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3594, 3, "layouts_LAYOUTLIST", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(3600, 9, "layouts_LAYOUTLIST", null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(3601, 10, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3591, 0, prod__lit_do__char_class___range__100_100_char_class___range__111_111_, new int[] {100,111}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(3593, 2, "Statement", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(3597, 6, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(3595, 4, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(3603, 12, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(CParser.prod__Statement__lit_do_layouts_LAYOUTLIST_Statement_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Statement__Expression_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(3509, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3510, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3508, 0, "Expression", null, null);
      builder.addAlternative(CParser.prod__Statement__Expression_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Statement__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[13];
      
      tmp[12] = new NonTerminalStackNode<IConstructor>(3667, 12, "Statement", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(3659, 4, "Expression", null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(3661, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3657, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[10] = new LiteralStackNode<IConstructor>(3665, 10, prod__lit_else__char_class___range__101_101_char_class___range__108_108_char_class___range__115_115_char_class___range__101_101_, new int[] {101,108,115,101}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3655, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(3664, 9, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3658, 3, "layouts_LAYOUTLIST", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(3666, 11, "layouts_LAYOUTLIST", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3662, 7, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3656, 1, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3660, 5, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(3663, 8, "Statement", null, null);
      builder.addAlternative(CParser.prod__Statement__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_Statement_, tmp);
	}
    protected static final void _init_prod__Statement__lit_switch_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[8] = new NonTerminalStackNode<IConstructor>(3638, 8, "Statement", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3633, 3, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3635, 5, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3631, 1, "layouts_LAYOUTLIST", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3637, 7, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3630, 0, prod__lit_switch__char_class___range__115_115_char_class___range__119_119_char_class___range__105_105_char_class___range__116_116_char_class___range__99_99_char_class___range__104_104_, new int[] {115,119,105,116,99,104}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(3634, 4, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3632, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(3636, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      builder.addAlternative(CParser.prod__Statement__lit_switch_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_, tmp);
	}
    protected static final void _init_prod__Statement__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(3643, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3641, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3640, 0, prod__lit_default__char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__97_97_char_class___range__117_117_char_class___range__108_108_char_class___range__116_116_, new int[] {100,101,102,97,117,108,116}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(3644, 4, "Statement", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3642, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(CParser.prod__Statement__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Statement_, tmp);
	}
    protected static final void _init_prod__Statement__lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3548, 0, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(CParser.prod__Statement__lit___59_, tmp);
	}
    protected static final void _init_prod__Statement__lit_continue_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode<IConstructor>(3550, 0, prod__lit_continue__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__105_105_char_class___range__110_110_char_class___range__117_117_char_class___range__101_101_, new int[] {99,111,110,116,105,110,117,101}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3552, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3551, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Statement__lit_continue_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Statement__lit_case_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(3527, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3525, 1, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3529, 5, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3524, 0, prod__lit_case__char_class___range__99_99_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_, new int[] {99,97,115,101}, null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(3528, 4, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(3530, 6, "Statement", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(3526, 2, "Expression", null, null);
      builder.addAlternative(CParser.prod__Statement__lit_case_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Statement_, tmp);
	}
    protected static final void _init_prod__Statement__lit_return_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(3534, 2, "Expression", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(3536, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3532, 0, prod__lit_return__char_class___range__114_114_char_class___range__101_101_char_class___range__116_116_char_class___range__117_117_char_class___range__114_114_char_class___range__110_110_, new int[] {114,101,116,117,114,110}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3533, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3535, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Statement__lit_return_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Statement__lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(3578, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3576, 1, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3580, 5, "layouts_LAYOUTLIST", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3582, 7, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(3583, 8, "Statement", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(3579, 4, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3577, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(3581, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3575, 0, prod__lit_while__char_class___range__119_119_char_class___range__104_104_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {119,104,105,108,101}, null, null);
      builder.addAlternative(CParser.prod__Statement__lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_, tmp);
	}
    protected static final void _init_prod__Statement__lit___123_layouts_LAYOUTLIST_iter_star_seps__Declaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[5] = new NonTerminalStackNode<IConstructor>(3521, 5, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3513, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3517, 3, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3512, 0, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(3522, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(3516, 2, regular__iter_star_seps__Declaration__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3514, 0, "Declaration", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3515, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(3520, 4, regular__iter_star_seps__Statement__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(3518, 0, "Statement", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(3519, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(CParser.prod__Statement__lit___123_layouts_LAYOUTLIST_iter_star_seps__Declaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__Statement__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(3557, 3, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(3559, 5, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3555, 1, "layouts_LAYOUTLIST", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(3561, 7, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(3562, 8, "Statement", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(3558, 4, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3556, 2, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(3560, 6, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3554, 0, prod__lit_if__char_class___range__105_105_char_class___range__102_102_, new int[] {105,102}, null, null);
      builder.addAlternative(CParser.prod__Statement__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_, tmp);
	}
    protected static final void _init_prod__Statement__lit_break_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(3546, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(3544, 0, prod__lit_break__char_class___range__98_98_char_class___range__114_114_char_class___range__101_101_char_class___range__97_97_char_class___range__107_107_, new int[] {98,114,101,97,107}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3545, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Statement__lit_break_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__Statement__Identifier_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Statement_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new NonTerminalStackNode<IConstructor>(3542, 4, "Statement", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(3540, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(3538, 0, "Identifier", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(3541, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(3539, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__Statement__Identifier_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Statement_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_Statement__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_97_116_101_109_101_110_116_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__Statement__layouts_LAYOUTLIST(builder);
      
        _init_prod__$MetaHole_Statement__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_97_116_101_109_101_110_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__Statement(builder);
      
        _init_prod__Statement__lit_for_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_opt__Expression_layouts_LAYOUTLIST_lit___59_layouts_LAYOUTLIST_opt__Expression_layouts_LAYOUTLIST_lit___59_layouts_LAYOUTLIST_opt__Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_(builder);
      
        _init_prod__Statement__lit_return_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Statement__lit_goto_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Statement__lit_do_layouts_LAYOUTLIST_Statement_layouts_LAYOUTLIST_lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Statement__Expression_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Statement__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_layouts_LAYOUTLIST_lit_else_layouts_LAYOUTLIST_Statement_(builder);
      
        _init_prod__Statement__lit_switch_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_(builder);
      
        _init_prod__Statement__lit_default_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Statement_(builder);
      
        _init_prod__Statement__lit___59_(builder);
      
        _init_prod__Statement__lit_continue_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Statement__lit_case_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Statement_(builder);
      
        _init_prod__Statement__lit_return_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Statement__lit_while_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_(builder);
      
        _init_prod__Statement__lit___123_layouts_LAYOUTLIST_iter_star_seps__Declaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_iter_star_seps__Statement__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__Statement__lit_if_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_Expression_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Statement_(builder);
      
        _init_prod__Statement__lit_break_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__Statement__Identifier_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Statement_(builder);
      
    }
  }
	
  protected static class StorageClass {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_StorageClass__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_111_114_97_103_101_67_108_97_115_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StorageClass(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(818, 1, prod__lit___115_111_114_116_40_34_83_116_111_114_97_103_101_67_108_97_115_115_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__97_97_char_class___range__103_103_char_class___range__101_101_char_class___range__67_67_char_class___range__108_108_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,83,116,111,114,97,103,101,67,108,97,115,115,34,41}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(822, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(819, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(821, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(820, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[0] = new CharStackNode<IConstructor>(817, 0, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_StorageClass__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_111_114_97_103_101_67_108_97_115_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StorageClass, tmp);
	}
    protected static final void _init_prod__typeDef_StorageClass__lit_typedef_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(808, 0, prod__lit_typedef__char_class___range__116_116_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_, new int[] {116,121,112,101,100,101,102}, null, null);
      builder.addAlternative(CParser.prod__typeDef_StorageClass__lit_typedef_, tmp);
	}
    protected static final void _init_prod__StorageClass__lit_auto_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(825, 0, prod__lit_auto__char_class___range__97_97_char_class___range__117_117_char_class___range__116_116_char_class___range__111_111_, new int[] {97,117,116,111}, null, null);
      builder.addAlternative(CParser.prod__StorageClass__lit_auto_, tmp);
	}
    protected static final void _init_prod__StorageClass__lit_static_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(812, 0, prod__lit_static__char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__99_99_, new int[] {115,116,97,116,105,99}, null, null);
      builder.addAlternative(CParser.prod__StorageClass__lit_static_, tmp);
	}
    protected static final void _init_prod__StorageClass__lit_register_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(814, 0, prod__lit_register__char_class___range__114_114_char_class___range__101_101_char_class___range__103_103_char_class___range__105_105_char_class___range__115_115_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_, new int[] {114,101,103,105,115,116,101,114}, null, null);
      builder.addAlternative(CParser.prod__StorageClass__lit_register_, tmp);
	}
    protected static final void _init_prod__StorageClass__lit_extern_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(810, 0, prod__lit_extern__char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__110_110_, new int[] {101,120,116,101,114,110}, null, null);
      builder.addAlternative(CParser.prod__StorageClass__lit_extern_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_StorageClass__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_111_114_97_103_101_67_108_97_115_115_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StorageClass(builder);
      
        _init_prod__typeDef_StorageClass__lit_typedef_(builder);
      
        _init_prod__StorageClass__lit_auto_(builder);
      
        _init_prod__StorageClass__lit_static_(builder);
      
        _init_prod__StorageClass__lit_register_(builder);
      
        _init_prod__StorageClass__lit_extern_(builder);
      
    }
  }
	
  protected static class StructDeclaration {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_StructDeclaration__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StructDeclaration(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(4549, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4554, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4551, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(4553, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4552, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(4550, 1, prod__lit___115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_105_111_110_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,83,116,114,117,99,116,68,101,99,108,97,114,97,116,105,111,110,34,41}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_StructDeclaration__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StructDeclaration, tmp);
	}
    protected static final void _init_prod__$MetaHole_StructDeclaration__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__StructDeclaration__layouts_LAYOUTLIST(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(4538, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4540, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4543, 4, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(4539, 1, prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_105_111_110_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {92,105,116,101,114,45,115,116,97,114,40,115,111,114,116,40,34,83,116,114,117,99,116,68,101,99,108,97,114,97,116,105,111,110,34,41,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(4542, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4541, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_StructDeclaration__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__StructDeclaration__layouts_LAYOUTLIST, tmp);
	}
    protected static final void _init_prod__structDeclWithDecl_StructDeclaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_iter_seps__StructDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(4520, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4526, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new SeparatedListStackNode<IConstructor>(4525, 2, regular__iter_seps__StructDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4521, 0, "StructDeclarator", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4522, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(4523, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(4524, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(4518, 0, regular__iter_seps__Specifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4516, 0, "Specifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4517, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(4527, 4, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      builder.addAlternative(CParser.prod__structDeclWithDecl_StructDeclaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_iter_seps__StructDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    protected static final void _init_prod__structDeclWithoutDecl_StructDeclaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(4534, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4535, 2, prod__lit___59__char_class___range__59_59_, new int[] {59}, null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(4532, 0, regular__iter_seps__Specifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4530, 0, "Specifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4531, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      builder.addAlternative(CParser.prod__structDeclWithoutDecl_StructDeclaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_StructDeclaration__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_105_111_110_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StructDeclaration(builder);
      
        _init_prod__$MetaHole_StructDeclaration__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_105_111_110_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__StructDeclaration__layouts_LAYOUTLIST(builder);
      
        _init_prod__structDeclWithDecl_StructDeclaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_iter_seps__StructDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(builder);
      
        _init_prod__structDeclWithoutDecl_StructDeclaration__specs_iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___59_(builder);
      
    }
  }
	
  protected static class StructDeclarator {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_StructDeclarator__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__StructDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(5473, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(5472, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(5470, 1, prod__lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__101_101_char_class___range__112_112_char_class___range__115_115_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__44_44_char_class___range__91_91_char_class___range__108_108_char_class___range__105_105_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__44_44_char_class___range__34_34_char_class___range__41_41_char_class___range__93_93_char_class___range__41_41_, new int[] {92,105,116,101,114,45,115,101,112,115,40,115,111,114,116,40,34,83,116,114,117,99,116,68,101,99,108,97,114,97,116,111,114,34,41,44,91,108,105,116,40,34,44,34,41,93,41}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(5469, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(5474, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5471, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_StructDeclarator__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__StructDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, tmp);
	}
    protected static final void _init_prod__$MetaHole_StructDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StructDeclarator(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(5452, 1, prod__lit___115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_111_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__83_83_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_char_class___range__68_68_char_class___range__101_101_char_class___range__99_99_char_class___range__108_108_char_class___range__97_97_char_class___range__114_114_char_class___range__97_97_char_class___range__116_116_char_class___range__111_111_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,83,116,114,117,99,116,68,101,99,108,97,114,97,116,111,114,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(5455, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(5454, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[4] = new CharStackNode<IConstructor>(5456, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5453, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(5451, 0, new int[][]{{0,0}}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_StructDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StructDeclarator, tmp);
	}
    protected static final void _init_prod__StructDeclarator__opt__Declarator_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Expression_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new NonTerminalStackNode<IConstructor>(5463, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(5461, 1, "layouts_LAYOUTLIST", null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(5464, 4, "Expression", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(5462, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new OptionalStackNode<IConstructor>(5460, 0, regular__opt__Declarator, new NonTerminalStackNode<IConstructor>(5459, 0, "Declarator", null, null), null, null);
      builder.addAlternative(CParser.prod__StructDeclarator__opt__Declarator_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Expression_, tmp);
	}
    protected static final void _init_prod__StructDeclarator__Declarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(5466, 0, "Declarator", null, null);
      builder.addAlternative(CParser.prod__StructDeclarator__Declarator_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_StructDeclarator__char_class___range__0_0_lit___92_105_116_101_114_45_115_101_112_115_40_115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_111_114_34_41_44_91_108_105_116_40_34_44_34_41_93_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_seps__StructDeclarator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST(builder);
      
        _init_prod__$MetaHole_StructDeclarator__char_class___range__0_0_lit___115_111_114_116_40_34_83_116_114_117_99_116_68_101_99_108_97_114_97_116_111_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__StructDeclarator(builder);
      
        _init_prod__StructDeclarator__opt__Declarator_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Expression_(builder);
      
        _init_prod__StructDeclarator__Declarator_(builder);
      
    }
  }
	
  protected static class TranslationUnit {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_TranslationUnit__char_class___range__0_0_lit___115_111_114_116_40_34_84_114_97_110_115_108_97_116_105_111_110_85_110_105_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TranslationUnit(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(1643, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1645, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(1648, 4, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(1644, 1, prod__lit___115_111_114_116_40_34_84_114_97_110_115_108_97_116_105_111_110_85_110_105_116_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__84_84_char_class___range__114_114_char_class___range__97_97_char_class___range__110_110_char_class___range__115_115_char_class___range__108_108_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__85_85_char_class___range__110_110_char_class___range__105_105_char_class___range__116_116_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,84,114,97,110,115,108,97,116,105,111,110,85,110,105,116,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(1647, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(1646, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_TranslationUnit__char_class___range__0_0_lit___115_111_114_116_40_34_84_114_97_110_115_108_97_116_105_111_110_85_110_105_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TranslationUnit, tmp);
	}
    protected static final void _init_prod__TranslationUnit__iter_seps__ExternalDeclaration__layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new SeparatedListStackNode<IConstructor>(1640, 0, regular__iter_seps__ExternalDeclaration__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(1638, 0, "ExternalDeclaration", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(1639, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      builder.addAlternative(CParser.prod__TranslationUnit__iter_seps__ExternalDeclaration__layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_TranslationUnit__char_class___range__0_0_lit___115_111_114_116_40_34_84_114_97_110_115_108_97_116_105_111_110_85_110_105_116_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TranslationUnit(builder);
      
        _init_prod__TranslationUnit__iter_seps__ExternalDeclaration__layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  protected static class TypeName {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_TypeName__char_class___range__0_0_lit___115_111_114_116_40_34_84_121_112_101_78_97_109_101_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TypeName(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(4481, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4480, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(4478, 1, prod__lit___115_111_114_116_40_34_84_121_112_101_78_97_109_101_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__84_84_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__78_78_char_class___range__97_97_char_class___range__109_109_char_class___range__101_101_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,84,121,112,101,78,97,109,101,34,41}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4482, 4, new int[][]{{0,0}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4477, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4479, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_TypeName__char_class___range__0_0_lit___115_111_114_116_40_34_84_121_112_101_78_97_109_101_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TypeName, tmp);
	}
    protected static final void _init_prod__TypeName__iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_AbstractDeclarator_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(4473, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4474, 2, "AbstractDeclarator", null, null);
      tmp[0] = new SeparatedListStackNode<IConstructor>(4472, 0, regular__iter_seps__Specifier__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4470, 0, "Specifier", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4471, 1, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      builder.addAlternative(CParser.prod__TypeName__iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_AbstractDeclarator_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_TypeName__char_class___range__0_0_lit___115_111_114_116_40_34_84_121_112_101_78_97_109_101_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TypeName(builder);
      
        _init_prod__TypeName__iter_seps__Specifier__layouts_LAYOUTLIST_layouts_LAYOUTLIST_AbstractDeclarator_(builder);
      
    }
  }
	
  protected static class TypeQualifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_TypeQualifier__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_84_121_112_101_81_117_97_108_105_102_105_101_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__TypeQualifier__layouts_LAYOUTLIST(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new LiteralStackNode<IConstructor>(1312, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(1315, 4, new int[][]{{0,0}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(1310, 0, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(1311, 1, prod__lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_84_121_112_101_81_117_97_108_105_102_105_101_114_34_41_41__char_class___range__92_92_char_class___range__105_105_char_class___range__116_116_char_class___range__101_101_char_class___range__114_114_char_class___range__45_45_char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__40_40_char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__84_84_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__81_81_char_class___range__117_117_char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_char_class___range__41_41_, new int[] {92,105,116,101,114,45,115,116,97,114,40,115,111,114,116,40,34,84,121,112,101,81,117,97,108,105,102,105,101,114,34,41,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(1314, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(1313, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_TypeQualifier__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_84_121_112_101_81_117_97_108_105_102_105_101_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__TypeQualifier__layouts_LAYOUTLIST, tmp);
	}
    protected static final void _init_prod__$MetaHole_TypeQualifier__char_class___range__0_0_lit___115_111_114_116_40_34_84_121_112_101_81_117_97_108_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TypeQualifier(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(1321, 0, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(1323, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(1326, 4, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(1322, 1, prod__lit___115_111_114_116_40_34_84_121_112_101_81_117_97_108_105_102_105_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__84_84_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__81_81_char_class___range__117_117_char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,84,121,112,101,81,117,97,108,105,102,105,101,114,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(1325, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(1324, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_TypeQualifier__char_class___range__0_0_lit___115_111_114_116_40_34_84_121_112_101_81_117_97_108_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TypeQualifier, tmp);
	}
    protected static final void _init_prod__TypeQualifier__lit_const_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1329, 0, prod__lit_const__char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_char_class___range__116_116_, new int[] {99,111,110,115,116}, null, null);
      builder.addAlternative(CParser.prod__TypeQualifier__lit_const_, tmp);
	}
    protected static final void _init_prod__TypeQualifier__lit_volatile_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(1307, 0, prod__lit_volatile__char_class___range__118_118_char_class___range__111_111_char_class___range__108_108_char_class___range__97_97_char_class___range__116_116_char_class___range__105_105_char_class___range__108_108_char_class___range__101_101_, new int[] {118,111,108,97,116,105,108,101}, null, null);
      builder.addAlternative(CParser.prod__TypeQualifier__lit_volatile_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_TypeQualifier__char_class___range__0_0_lit___92_105_116_101_114_45_115_116_97_114_40_115_111_114_116_40_34_84_121_112_101_81_117_97_108_105_102_105_101_114_34_41_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__iter_star_seps__TypeQualifier__layouts_LAYOUTLIST(builder);
      
        _init_prod__$MetaHole_TypeQualifier__char_class___range__0_0_lit___115_111_114_116_40_34_84_121_112_101_81_117_97_108_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TypeQualifier(builder);
      
        _init_prod__TypeQualifier__lit_const_(builder);
      
        _init_prod__TypeQualifier__lit_volatile_(builder);
      
    }
  }
	
  protected static class TypeSpecifier {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_TypeSpecifier__char_class___range__0_0_lit___115_111_114_116_40_34_84_121_112_101_83_112_101_99_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TypeSpecifier(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(4866, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(4865, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(4863, 1, prod__lit___115_111_114_116_40_34_84_121_112_101_83_112_101_99_105_102_105_101_114_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__84_84_char_class___range__121_121_char_class___range__112_112_char_class___range__101_101_char_class___range__83_83_char_class___range__112_112_char_class___range__101_101_char_class___range__99_99_char_class___range__105_105_char_class___range__102_102_char_class___range__105_105_char_class___range__101_101_char_class___range__114_114_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,84,121,112,101,83,112,101,99,105,102,105,101,114,34,41}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(4862, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(4867, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4864, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(CParser.prod__$MetaHole_TypeSpecifier__char_class___range__0_0_lit___115_111_114_116_40_34_84_121_112_101_83_112_101_99_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TypeSpecifier, tmp);
	}
    protected static final void _init_prod__char_TypeSpecifier__lit_char_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4852, 0, prod__lit_char__char_class___range__99_99_char_class___range__104_104_char_class___range__97_97_char_class___range__114_114_, new int[] {99,104,97,114}, null, null);
      builder.addAlternative(CParser.prod__char_TypeSpecifier__lit_char_, tmp);
	}
    protected static final void _init_prod__double_TypeSpecifier__lit_double_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4932, 0, prod__lit_double__char_class___range__100_100_char_class___range__111_111_char_class___range__117_117_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_, new int[] {100,111,117,98,108,101}, null, null);
      builder.addAlternative(CParser.prod__double_TypeSpecifier__lit_double_, tmp);
	}
    protected static final void _init_prod__enum_TypeSpecifier__lit_enum_layouts_LAYOUTLIST_Identifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(4947, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4946, 0, prod__lit_enum__char_class___range__101_101_char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new int[] {101,110,117,109}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4948, 2, "Identifier", null, null);
      builder.addAlternative(CParser.prod__enum_TypeSpecifier__lit_enum_layouts_LAYOUTLIST_Identifier_, tmp);
	}
    protected static final void _init_prod__enumAnonDecl_TypeSpecifier__lit_enum_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_seps__Enumerator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[5] = new NonTerminalStackNode<IConstructor>(4925, 5, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4917, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4919, 3, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4918, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(4926, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(4924, 4, regular__iter_seps__Enumerator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4920, 0, "Enumerator", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4921, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(4922, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(4923, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4916, 0, prod__lit_enum__char_class___range__101_101_char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new int[] {101,110,117,109}, null, null);
      builder.addAlternative(CParser.prod__enumAnonDecl_TypeSpecifier__lit_enum_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_seps__Enumerator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__enumDecl_TypeSpecifier__lit_enum_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_seps__Enumerator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[7] = new NonTerminalStackNode<IConstructor>(4882, 7, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(4883, 8, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4874, 3, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4876, 5, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4872, 1, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4871, 0, prod__lit_enum__char_class___range__101_101_char_class___range__110_110_char_class___range__117_117_char_class___range__109_109_, new int[] {101,110,117,109}, null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(4875, 4, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4873, 2, "Identifier", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(4881, 6, regular__iter_seps__Enumerator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4877, 0, "Enumerator", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4878, 1, "layouts_LAYOUTLIST", null, null), new LiteralStackNode<IConstructor>(4879, 2, prod__lit___44__char_class___range__44_44_, new int[] {44}, null, null), new NonTerminalStackNode<IConstructor>(4880, 3, "layouts_LAYOUTLIST", null, null)}, true, null, null);
      builder.addAlternative(CParser.prod__enumDecl_TypeSpecifier__lit_enum_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_seps__Enumerator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__float_TypeSpecifier__lit_float_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4913, 0, prod__lit_float__char_class___range__102_102_char_class___range__108_108_char_class___range__111_111_char_class___range__97_97_char_class___range__116_116_, new int[] {102,108,111,97,116}, null, null);
      builder.addAlternative(CParser.prod__float_TypeSpecifier__lit_float_, tmp);
	}
    protected static final void _init_prod__identifier_TypeSpecifier__Identifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(4886, 0, "Identifier", null, null);
      builder.addAlternative(CParser.prod__identifier_TypeSpecifier__Identifier_, tmp);
	}
    protected static final void _init_prod__int_TypeSpecifier__lit_int_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4892, 0, prod__lit_int__char_class___range__105_105_char_class___range__110_110_char_class___range__116_116_, new int[] {105,110,116}, null, null);
      builder.addAlternative(CParser.prod__int_TypeSpecifier__lit_int_, tmp);
	}
    protected static final void _init_prod__long_TypeSpecifier__lit_long_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4929, 0, prod__lit_long__char_class___range__108_108_char_class___range__111_111_char_class___range__110_110_char_class___range__103_103_, new int[] {108,111,110,103}, null, null);
      builder.addAlternative(CParser.prod__long_TypeSpecifier__lit_long_, tmp);
	}
    protected static final void _init_prod__short_TypeSpecifier__lit_short_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4966, 0, prod__lit_short__char_class___range__115_115_char_class___range__104_104_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_, new int[] {115,104,111,114,116}, null, null);
      builder.addAlternative(CParser.prod__short_TypeSpecifier__lit_short_, tmp);
	}
    protected static final void _init_prod__struct_TypeSpecifier__lit_struct_layouts_LAYOUTLIST_Identifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(4858, 1, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4859, 2, "Identifier", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4857, 0, prod__lit_struct__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_, new int[] {115,116,114,117,99,116}, null, null);
      builder.addAlternative(CParser.prod__struct_TypeSpecifier__lit_struct_layouts_LAYOUTLIST_Identifier_, tmp);
	}
    protected static final void _init_prod__structAnonDecl_TypeSpecifier__lit_struct_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[2] = new LiteralStackNode<IConstructor>(4971, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(4977, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(4975, 4, regular__iter_star_seps__StructDeclaration__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4973, 0, "StructDeclaration", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4974, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4969, 0, prod__lit_struct__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_, new int[] {115,116,114,117,99,116}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4976, 5, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4970, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4972, 3, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__structAnonDecl_TypeSpecifier__lit_struct_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__structDecl_TypeSpecifier__lit_struct_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4900, 0, prod__lit_struct__char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__117_117_char_class___range__99_99_char_class___range__116_116_, new int[] {115,116,114,117,99,116}, null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(4904, 4, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4902, 2, "Identifier", null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(4908, 6, regular__iter_star_seps__StructDeclaration__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4906, 0, "StructDeclaration", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4907, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(4910, 8, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4903, 3, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4901, 1, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4905, 5, "layouts_LAYOUTLIST", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(4909, 7, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__structDecl_TypeSpecifier__lit_struct_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__union_TypeSpecifier__lit_union_layouts_LAYOUTLIST_Identifier_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4895, 0, prod__lit_union__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_, new int[] {117,110,105,111,110}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4897, 2, "Identifier", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4896, 1, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__union_TypeSpecifier__lit_union_layouts_LAYOUTLIST_Identifier_, tmp);
	}
    protected static final void _init_prod__unionAnonDecl_TypeSpecifier__lit_union_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[7];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(4936, 1, "layouts_LAYOUTLIST", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4942, 5, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4938, 3, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4935, 0, prod__lit_union__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_, new int[] {117,110,105,111,110}, null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(4943, 6, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(4937, 2, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      tmp[4] = new SeparatedListStackNode<IConstructor>(4941, 4, regular__iter_star_seps__StructDeclaration__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4939, 0, "StructDeclaration", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4940, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      builder.addAlternative(CParser.prod__unionAnonDecl_TypeSpecifier__lit_union_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__unionDecl_TypeSpecifier__lit_union_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[9];
      
      tmp[7] = new NonTerminalStackNode<IConstructor>(4962, 7, "layouts_LAYOUTLIST", null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(4963, 8, prod__lit___125__char_class___range__125_125_, new int[] {125}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(4958, 5, "layouts_LAYOUTLIST", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(4954, 1, "layouts_LAYOUTLIST", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(4956, 3, "layouts_LAYOUTLIST", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(4953, 0, prod__lit_union__char_class___range__117_117_char_class___range__110_110_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_, new int[] {117,110,105,111,110}, null, null);
      tmp[6] = new SeparatedListStackNode<IConstructor>(4961, 6, regular__iter_star_seps__StructDeclaration__layouts_LAYOUTLIST, new NonTerminalStackNode<IConstructor>(4959, 0, "StructDeclaration", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(4960, 1, "layouts_LAYOUTLIST", null, null)}, false, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4955, 2, "Identifier", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(4957, 4, prod__lit___123__char_class___range__123_123_, new int[] {123}, null, null);
      builder.addAlternative(CParser.prod__unionDecl_TypeSpecifier__lit_union_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_, tmp);
	}
    protected static final void _init_prod__void_TypeSpecifier__lit_void_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4889, 0, prod__lit_void__char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_, new int[] {118,111,105,100}, null, null);
      builder.addAlternative(CParser.prod__void_TypeSpecifier__lit_void_, tmp);
	}
    protected static final void _init_prod__TypeSpecifier__lit_unsigned_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4854, 0, prod__lit_unsigned__char_class___range__117_117_char_class___range__110_110_char_class___range__115_115_char_class___range__105_105_char_class___range__103_103_char_class___range__110_110_char_class___range__101_101_char_class___range__100_100_, new int[] {117,110,115,105,103,110,101,100}, null, null);
      builder.addAlternative(CParser.prod__TypeSpecifier__lit_unsigned_, tmp);
	}
    protected static final void _init_prod__TypeSpecifier__lit_signed_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(4950, 0, prod__lit_signed__char_class___range__115_115_char_class___range__105_105_char_class___range__103_103_char_class___range__110_110_char_class___range__101_101_char_class___range__100_100_, new int[] {115,105,103,110,101,100}, null, null);
      builder.addAlternative(CParser.prod__TypeSpecifier__lit_signed_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_TypeSpecifier__char_class___range__0_0_lit___115_111_114_116_40_34_84_121_112_101_83_112_101_99_105_102_105_101_114_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__TypeSpecifier(builder);
      
        _init_prod__char_TypeSpecifier__lit_char_(builder);
      
        _init_prod__double_TypeSpecifier__lit_double_(builder);
      
        _init_prod__enum_TypeSpecifier__lit_enum_layouts_LAYOUTLIST_Identifier_(builder);
      
        _init_prod__enumAnonDecl_TypeSpecifier__lit_enum_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_seps__Enumerator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__enumDecl_TypeSpecifier__lit_enum_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_seps__Enumerator__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__float_TypeSpecifier__lit_float_(builder);
      
        _init_prod__identifier_TypeSpecifier__Identifier_(builder);
      
        _init_prod__int_TypeSpecifier__lit_int_(builder);
      
        _init_prod__long_TypeSpecifier__lit_long_(builder);
      
        _init_prod__short_TypeSpecifier__lit_short_(builder);
      
        _init_prod__struct_TypeSpecifier__lit_struct_layouts_LAYOUTLIST_Identifier_(builder);
      
        _init_prod__structAnonDecl_TypeSpecifier__lit_struct_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__structDecl_TypeSpecifier__lit_struct_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__union_TypeSpecifier__lit_union_layouts_LAYOUTLIST_Identifier_(builder);
      
        _init_prod__unionAnonDecl_TypeSpecifier__lit_union_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__unionDecl_TypeSpecifier__lit_union_layouts_LAYOUTLIST_Identifier_layouts_LAYOUTLIST_lit___123_layouts_LAYOUTLIST_iter_star_seps__StructDeclaration__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_(builder);
      
        _init_prod__void_TypeSpecifier__lit_void_(builder);
      
        _init_prod__TypeSpecifier__lit_unsigned_(builder);
      
        _init_prod__TypeSpecifier__lit_signed_(builder);
      
    }
  }
	
  protected static class start__TranslationUnit {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__start__TranslationUnit__layouts_LAYOUTLIST_top_TranslationUnit_layouts_LAYOUTLIST_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(4148, 1, "TranslationUnit", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(4147, 0, "layouts_LAYOUTLIST", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(4150, 2, "layouts_LAYOUTLIST", null, null);
      builder.addAlternative(CParser.prod__start__TranslationUnit__layouts_LAYOUTLIST_top_TranslationUnit_layouts_LAYOUTLIST_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__start__TranslationUnit__layouts_LAYOUTLIST_top_TranslationUnit_layouts_LAYOUTLIST_(builder);
      
    }
  }
	
  // Parse methods    
  
  public AbstractStackNode<IConstructor>[] Keyword() {
    return Keyword.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] layouts_$default$() {
    return layouts_$default$.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] layouts_LAYOUTLIST() {
    return layouts_LAYOUTLIST.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Asterisk() {
    return Asterisk.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] CharacterConstant() {
    return CharacterConstant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] CharacterConstantContent() {
    return CharacterConstantContent.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Comment() {
    return Comment.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Exponent() {
    return Exponent.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] FloatingPointConstant() {
    return FloatingPointConstant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] HexadecimalConstant() {
    return HexadecimalConstant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Identifier() {
    return Identifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] IntegerConstant() {
    return IntegerConstant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] LAYOUT() {
    return LAYOUT.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] MultiLineCommentBodyToken() {
    return MultiLineCommentBodyToken.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StringConstant() {
    return StringConstant.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StringConstantContent() {
    return StringConstantContent.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] AbstractDeclarator() {
    return AbstractDeclarator.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] AnonymousIdentifier() {
    return AnonymousIdentifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Declaration() {
    return Declaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Declarator() {
    return Declarator.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Enumerator() {
    return Enumerator.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Expression() {
    return Expression.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ExternalDeclaration() {
    return ExternalDeclaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] FunctionDefinition() {
    return FunctionDefinition.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] FunctionPrototype() {
    return FunctionPrototype.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] GlobalDeclaration() {
    return GlobalDeclaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] InitDeclarator() {
    return InitDeclarator.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Initializer() {
    return Initializer.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] MoreParameters() {
    return MoreParameters.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] NonCommaExpression() {
    return NonCommaExpression.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Parameter() {
    return Parameter.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Parameters() {
    return Parameters.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PrototypeDeclarator() {
    return PrototypeDeclarator.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PrototypeParameter() {
    return PrototypeParameter.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] PrototypeParameters() {
    return PrototypeParameters.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Specifier() {
    return Specifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Statement() {
    return Statement.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StorageClass() {
    return StorageClass.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StructDeclaration() {
    return StructDeclaration.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] StructDeclarator() {
    return StructDeclarator.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] TranslationUnit() {
    return TranslationUnit.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] TypeName() {
    return TypeName.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] TypeQualifier() {
    return TypeQualifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] TypeSpecifier() {
    return TypeSpecifier.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__TranslationUnit() {
    return start__TranslationUnit.EXPECTS;
  }
}