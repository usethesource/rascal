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
public class GEXPPRIOParser extends org.rascalmpl.parser.gtd.SGTDBF<IConstructor, IConstructor, ISourceLocation> {
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
	
  private static final IConstructor prod__$MetaHole_E__char_class___range__0_0_lit___115_111_114_116_40_34_69_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__E = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"E\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"E\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"E\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_E1__char_class___range__0_0_lit___115_111_114_116_40_34_69_49_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__E1 = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"E1\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"E1\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"E1\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_F__char_class___range__0_0_lit___115_111_114_116_40_34_70_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__F = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"F\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"F\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"F\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_T__char_class___range__0_0_lit___115_111_114_116_40_34_84_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__T = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"T\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"T\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"T\")))})", Factory.Production);
  private static final IConstructor prod__$MetaHole_T1__char_class___range__0_0_lit___115_111_114_116_40_34_84_49_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__T1 = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"T1\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"T1\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"T1\")))})", Factory.Production);
  private static final IConstructor prod__lit___40__char_class___range__40_40_ = (IConstructor) _read("prod(lit(\"(\"),[\\char-class([range(40,40)])],{})", Factory.Production);
  private static final IConstructor prod__lit___41__char_class___range__41_41_ = (IConstructor) _read("prod(lit(\")\"),[\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___42__char_class___range__42_42_ = (IConstructor) _read("prod(lit(\"*\"),[\\char-class([range(42,42)])],{})", Factory.Production);
  private static final IConstructor prod__lit___43__char_class___range__43_43_ = (IConstructor) _read("prod(lit(\"+\"),[\\char-class([range(43,43)])],{})", Factory.Production);
  private static final IConstructor prod__lit___58__char_class___range__58_58_ = (IConstructor) _read("prod(lit(\":\"),[\\char-class([range(58,58)])],{})", Factory.Production);
  private static final IConstructor prod__lit_id__char_class___range__105_105_char_class___range__100_100_ = (IConstructor) _read("prod(lit(\"id\"),[\\char-class([range(105,105)]),\\char-class([range(100,100)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_69_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"E\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(69,69)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_69_49_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__49_49_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"E1\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(69,69)]),\\char-class([range(49,49)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_70_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__70_70_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"F\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(70,70)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_84_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__84_84_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"T\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(84,84)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__lit___115_111_114_116_40_34_84_49_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__84_84_char_class___range__49_49_char_class___range__34_34_char_class___range__41_41_ = (IConstructor) _read("prod(lit(\"sort(\\\"T1\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(84,84)]),\\char-class([range(49,49)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", Factory.Production);
  private static final IConstructor prod__E__T_E1_ = (IConstructor) _read("prod(sort(\"E\"),[sort(\"T\"),sort(\"E1\")],{})", Factory.Production);
  private static final IConstructor prod__E1__ = (IConstructor) _read("prod(sort(\"E1\"),[],{})", Factory.Production);
  private static final IConstructor prod__E1__lit___43_T_E1_ = (IConstructor) _read("prod(sort(\"E1\"),[lit(\"+\"),sort(\"T\"),sort(\"E1\")],{})", Factory.Production);
  private static final IConstructor prod__F__lit_id_ = (IConstructor) _read("prod(sort(\"F\"),[lit(\"id\")],{})", Factory.Production);
  private static final IConstructor prod__F__lit___42_F_T1_ = (IConstructor) _read("prod(sort(\"F\"),[lit(\"*\"),sort(\"F\"),sort(\"T1\")],{})", Factory.Production);
  private static final IConstructor prod__F__lit___40_E_lit___41_ = (IConstructor) _read("prod(sort(\"F\"),[lit(\"(\"),sort(\"E\"),lit(\")\")],{})", Factory.Production);
  private static final IConstructor prod__T__F_T1_ = (IConstructor) _read("prod(sort(\"T\"),[sort(\"F\"),sort(\"T1\")],{})", Factory.Production);
  private static final IConstructor prod__T1__ = (IConstructor) _read("prod(sort(\"T1\"),[],{})", Factory.Production);
  private static final IConstructor regular__iter__char_class___range__48_57 = (IConstructor) _read("regular(iter(\\char-class([range(48,57)])))", Factory.Production);
    
  // Item declarations
	
	
  protected static class E {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_E__char_class___range__0_0_lit___115_111_114_116_40_34_69_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__E(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(110, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(109, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(107, 1, prod__lit___115_111_114_116_40_34_69_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,69,34,41}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(106, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(111, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(108, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(GEXPPRIOParser.prod__$MetaHole_E__char_class___range__0_0_lit___115_111_114_116_40_34_69_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__E, tmp);
	}
    protected static final void _init_prod__E__T_E1_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(103, 1, "E1", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(102, 0, "T", null, null);
      builder.addAlternative(GEXPPRIOParser.prod__E__T_E1_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_E__char_class___range__0_0_lit___115_111_114_116_40_34_69_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__E(builder);
      
        _init_prod__E__T_E1_(builder);
      
    }
  }
	
  protected static class E1 {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_E1__char_class___range__0_0_lit___115_111_114_116_40_34_69_49_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__E1(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(59, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(64, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(61, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(63, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(62, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(60, 1, prod__lit___115_111_114_116_40_34_69_49_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__69_69_char_class___range__49_49_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,69,49,34,41}, null, null);
      builder.addAlternative(GEXPPRIOParser.prod__$MetaHole_E1__char_class___range__0_0_lit___115_111_114_116_40_34_69_49_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__E1, tmp);
	}
    protected static final void _init_prod__E1__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(52, 0);
      builder.addAlternative(GEXPPRIOParser.prod__E1__, tmp);
	}
    protected static final void _init_prod__E1__lit___43_T_E1_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[0] = new LiteralStackNode<IConstructor>(54, 0, prod__lit___43__char_class___range__43_43_, new int[] {43}, null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(56, 2, "E1", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(55, 1, "T", null, null);
      builder.addAlternative(GEXPPRIOParser.prod__E1__lit___43_T_E1_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_E1__char_class___range__0_0_lit___115_111_114_116_40_34_69_49_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__E1(builder);
      
        _init_prod__E1__(builder);
      
        _init_prod__E1__lit___43_T_E1_(builder);
      
    }
  }
	
  protected static class F {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_F__char_class___range__0_0_lit___115_111_114_116_40_34_70_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__F(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(123, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(128, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(125, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(124, 1, prod__lit___115_111_114_116_40_34_70_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__70_70_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,70,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(127, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(126, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(GEXPPRIOParser.prod__$MetaHole_F__char_class___range__0_0_lit___115_111_114_116_40_34_70_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__F, tmp);
	}
    protected static final void _init_prod__F__lit_id_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(120, 0, prod__lit_id__char_class___range__105_105_char_class___range__100_100_, new int[] {105,100}, null, null);
      builder.addAlternative(GEXPPRIOParser.prod__F__lit_id_, tmp);
	}
    protected static final void _init_prod__F__lit___42_F_T1_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(135, 2, "T1", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(134, 1, "F", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(133, 0, prod__lit___42__char_class___range__42_42_, new int[] {42}, null, null);
      builder.addAlternative(GEXPPRIOParser.prod__F__lit___42_F_T1_, tmp);
	}
    protected static final void _init_prod__F__lit___40_E_lit___41_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new LiteralStackNode<IConstructor>(118, 2, prod__lit___41__char_class___range__41_41_, new int[] {41}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(116, 0, prod__lit___40__char_class___range__40_40_, new int[] {40}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(117, 1, "E", null, null);
      builder.addAlternative(GEXPPRIOParser.prod__F__lit___40_E_lit___41_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_F__char_class___range__0_0_lit___115_111_114_116_40_34_70_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__F(builder);
      
        _init_prod__F__lit_id_(builder);
      
        _init_prod__F__lit___42_F_T1_(builder);
      
        _init_prod__F__lit___40_E_lit___41_(builder);
      
    }
  }
	
  protected static class T {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_T__char_class___range__0_0_lit___115_111_114_116_40_34_84_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__T(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(16, 1, prod__lit___115_111_114_116_40_34_84_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__84_84_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,84,34,41}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(17, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(20, 4, new int[][]{{0,0}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(15, 0, new int[][]{{0,0}}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(19, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(18, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(GEXPPRIOParser.prod__$MetaHole_T__char_class___range__0_0_lit___115_111_114_116_40_34_84_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__T, tmp);
	}
    protected static final void _init_prod__T__F_T1_(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[2];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(23, 0, "F", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(24, 1, "T1", null, null);
      builder.addAlternative(GEXPPRIOParser.prod__T__F_T1_, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_T__char_class___range__0_0_lit___115_111_114_116_40_34_84_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__T(builder);
      
        _init_prod__T__F_T1_(builder);
      
    }
  }
	
  protected static class T1 {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_prod__$MetaHole_T1__char_class___range__0_0_lit___115_111_114_116_40_34_84_49_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__T1(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[3] = new ListStackNode<IConstructor>(142, 3, regular__iter__char_class___range__48_57, new CharStackNode<IConstructor>(141, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(139, 1, prod__lit___115_111_114_116_40_34_84_49_34_41__char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__40_40_char_class___range__34_34_char_class___range__84_84_char_class___range__49_49_char_class___range__34_34_char_class___range__41_41_, new int[] {115,111,114,116,40,34,84,49,34,41}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(138, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(143, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(140, 2, prod__lit___58__char_class___range__58_58_, new int[] {58}, null, null);
      builder.addAlternative(GEXPPRIOParser.prod__$MetaHole_T1__char_class___range__0_0_lit___115_111_114_116_40_34_84_49_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__T1, tmp);
	}
    protected static final void _init_prod__T1__(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(145, 0);
      builder.addAlternative(GEXPPRIOParser.prod__T1__, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_prod__$MetaHole_T1__char_class___range__0_0_lit___115_111_114_116_40_34_84_49_34_41_lit___58_iter__char_class___range__48_57_char_class___range__0_0__tag__holeType__T1(builder);
      
        _init_prod__T1__(builder);
      
    }
  }
	
  // Parse methods    
  
  public AbstractStackNode<IConstructor>[] E() {
    return E.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] E1() {
    return E1.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] F() {
    return F.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] T() {
    return T.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] T1() {
    return T1.EXPECTS;
  }
}