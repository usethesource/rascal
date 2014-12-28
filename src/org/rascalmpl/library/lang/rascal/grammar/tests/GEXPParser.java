package org.rascalmpl.library.lang.rascal.grammar.tests;

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
public class GEXPParser extends org.rascalmpl.parser.gtd.SGTDBF<IConstructor, IConstructor, ISourceLocation> {
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
    
    
    
   return result;
  }
    
  protected static IntegerMap _initDontNestGroups() {
    IntegerMap result = new IntegerMap();
    int resultStoreId = result.size();
    
    
      
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
	
  private static final IConstructor prod__$MetaHole_B__char_class___range__0_0_lit___115_111_114_116_40_34_66_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__B = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"B\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"B\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"B\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_E__char_class___range__0_0_lit___115_111_114_116_40_34_69_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__E = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"E\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"E\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"E\")))})", Factory.Production);
  private static final IConstructor prod__lit___42__char_class___range__42_42_ = (IConstructor) _read("prod(lit(\"*\"),[\\char-class([range(42,42)])],{})", Factory.Production);
  private static final IConstructor prod__lit___43__char_class___range__43_43_ = (IConstructor) _read("prod(lit(\"+\"),[\\char-class([range(43,43)])],{})", Factory.Production);
  private static final IConstructor prod__lit_0__char_class___range__48_48_ = (IConstructor) _read("prod(lit(\"0\"),[\\char-class([range(48,48)])],{})", Factory.Production);
  private static final IConstructor prod__lit_1__char_class___range__49_49_ = (IConstructor) _read("prod(lit(\"1\"),[\\char-class([range(49,49)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58__char_class___range__58_58_ = (IConstructor) _read("prod(lit(\":\"),[\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_66_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__66_66_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"B\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(66,66)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_69_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"E\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(69,69)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__B__lit_0_ = (IConstructor) _read("prod(sort(\"B\"),[lit(\"0\")],{})", Factory.Production);
  private static final IConstructor prod__B__lit_1_ = (IConstructor) _read("prod(sort(\"B\"),[lit(\"1\")],{})", Factory.Production);
  private static final IConstructor prod__E__B_ = (IConstructor) _read("prod(sort(\"E\"),[sort(\"B\")],{})", Factory.Production);
  private static final IConstructor prod__E__E_lit___42_B_ = (IConstructor) _read("prod(sort(\"E\"),[sort(\"E\"),lit(\"*\"),sort(\"B\")],{})", Factory.Production);
  private static final IConstructor prod__E__E_lit___43_B_ = (IConstructor) _read("prod(sort(\"E\"),[sort(\"E\"),lit(\"+\"),sort(\"B\")],{})", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_57 = (IConstructor) _read("regular(iter(\\char-class([range(48,57)])))", Factory.Production);
    
  // Item declarations
	
	
  protected static class B {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_B__char_class___range__0_0_lit___115_111_114_116_40_34_66_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__B(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(38, 1, prod__lit___115_111_114_116_40_34_66_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__66_66_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,66,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(41, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(40, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(39, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(37, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(42, 4, new int[][]{{0,0}}, null, null);
      builder.addAlternative(GEXPParser.prod__$MetaHole_B__char_class___range__0_0_lit___115_111_114_116_40_34_66_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__B, tmp);
	}
    protected static final void _init_prod__B__lit_1_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(47, 0, prod__lit_1__char_class___range__49_49_, new int[] {49}, null, null);
      builder.addAlternative(GEXPParser.prod__B__lit_1_, tmp);
	}
    protected static final void _init_prod__B__lit_0_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(45, 0, prod__lit_0__char_class___range__48_48_, new int[] {48}, null, null);
      builder.addAlternative(GEXPParser.prod__B__lit_0_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_B__char_class___range__0_0_lit___115_111_114_116_40_34_66_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__B(builder);
      
        _init_prod__B__lit_1_(builder);
      
        _init_prod__B__lit_0_(builder);
      
    }
  }
	
  protected static class E {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_E__char_class___range__0_0_lit___115_111_114_116_40_34_69_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__E(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(69, 1, prod__lit___115_111_114_116_40_34_69_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,69,34,41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(70, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(72, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(71, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[4] = new CharStackNode<IConstructor>(73, 4, new int[][]{{0,0}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(68, 0, new int[][]{{0,0}}, null, null);
      builder.addAlternative(GEXPParser.prod__$MetaHole_E__char_class___range__0_0_lit___115_111_114_116_40_34_69_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__E, tmp);
	}
    protected static final void _init_prod__E__B_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(76, 0, "B", null, null);
      builder.addAlternative(GEXPParser.prod__E__B_, tmp);
	}
    protected static final void _init_prod__E__E_lit___42_B_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new LiteralStackNode<IConstructor>(83, 1, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(84, 2, "B", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(82, 0, "E", null, null);
      builder.addAlternative(GEXPParser.prod__E__E_lit___42_B_, tmp);
	}
    protected static final void _init_prod__E__E_lit___43_B_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new LiteralStackNode<IConstructor>(79, 1, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(78, 0, "E", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(80, 2, "B", null, null);
      builder.addAlternative(GEXPParser.prod__E__E_lit___43_B_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_E__char_class___range__0_0_lit___115_111_114_116_40_34_69_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__E(builder);
      
        _init_prod__E__B_(builder);
      
        _init_prod__E__E_lit___42_B_(builder);
      
        _init_prod__E__E_lit___43_B_(builder);
      
    }
  }
	
  // Parse methods    
  
  public AbstractStackNode<IConstructor>[] B() {
    return B.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] E() {
    return E.EXPECTS;
  }
}