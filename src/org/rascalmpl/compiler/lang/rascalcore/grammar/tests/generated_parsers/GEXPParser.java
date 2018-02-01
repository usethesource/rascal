package org.rascalmpl.core.library.lang.rascalcore.grammar.tests.generated_parsers;

import java.io.IOException;
import java.io.StringReader;

import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.io.StandardTextReader;
import org.rascalmpl.core.parser.gtd.stack.*;
import org.rascalmpl.core.parser.gtd.stack.filter.*;
import org.rascalmpl.core.parser.gtd.stack.filter.follow.*;
import org.rascalmpl.core.parser.gtd.stack.filter.match.*;
import org.rascalmpl.core.parser.gtd.stack.filter.precede.*;
import org.rascalmpl.core.parser.gtd.preprocessing.ExpectBuilder;
import org.rascalmpl.core.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.core.parser.gtd.util.IntegerList;
import org.rascalmpl.core.parser.gtd.util.IntegerMap;
import org.rascalmpl.core.values.ValueFactoryFactory;
import org.rascalmpl.core.values.uptr.RascalValueFactory;
import org.rascalmpl.core.values.uptr.ITree;

@SuppressWarnings("all")
public class GEXPParser extends org.rascalmpl.parser.gtd.SGTDBF<IConstructor, ITree, ISourceLocation> {
  protected final static IValueFactory VF = ValueFactoryFactory.getValueFactory();

  protected static IValue _read(java.lang.String s, io.usethesource.vallang.type.Type type) {
    try {
      return new StandardTextReader().read(VF, org.rascalmpl.values.uptr.RascalValueFactory.uptr, type, new StringReader(s));
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
	
  private static final IConstructor cHJvZChsaXQoIioiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQyLDQyKV0pXSx7fSk00 = (IConstructor) _read("prod(lit(\"*\"),[\\char-class([range(42,42)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIjAiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ4LDQ4KV0pXSx7fSk00 = (IConstructor) _read("prod(lit(\"0\"),[\\char-class([range(48,48)])],{})", RascalValueFactory.Production);
  private static final IConstructor cmVndWxhcihpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSkp = (IConstructor) _read("regular(iter(\\char-class([range(48,57)])))", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIjEiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ5LDQ5KV0pXSx7fSk00 = (IConstructor) _read("prod(lit(\"1\"),[\\char-class([range(49,49)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIjoiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDU4LDU4KV0pXSx7fSk00 = (IConstructor) _read("prod(lit(\":\"),[\\char-class([range(58,58)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChzb3J0KCJFIiksW3NvcnQoIkIiKV0se30p = (IConstructor) _read("prod(sort(\"E\"),[sort(\"B\")],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJCIikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIkJcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIkIiKSkpfSk00 = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"B\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"B\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"B\")))})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIisiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQzLDQzKV0pXSx7fSk00 = (IConstructor) _read("prod(lit(\"+\"),[\\char-class([range(43,43)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChzb3J0KCJFIiksW3NvcnQoIkUiKSxsaXQoIisiKSxzb3J0KCJCIildLHt9KQ0000 = (IConstructor) _read("prod(sort(\"E\"),[sort(\"E\"),lit(\"+\"),sort(\"B\")],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChzb3J0KCJFIiksW3NvcnQoIkUiKSxsaXQoIioiKSxzb3J0KCJCIildLHt9KQ0000 = (IConstructor) _read("prod(sort(\"E\"),[sort(\"E\"),lit(\"*\"),sort(\"B\")],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoInNvcnQoXCJFXCIpIiksW1xjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTEsMTExKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDY5LDY5KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgzNCwzNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSldLHt9KQ0000 = (IConstructor) _read("prod(lit(\"sort(\\\"E\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(69,69)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJFIikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIkVcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIkUiKSkpfSk00 = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"E\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"E\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"E\")))})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoInNvcnQoXCJCXCIpIiksW1xjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTEsMTExKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDY2LDY2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgzNCwzNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSldLHt9KQ0000 = (IConstructor) _read("prod(lit(\"sort(\\\"B\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(66,66)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChzb3J0KCJCIiksW2xpdCgiMCIpXSx7fSk00 = (IConstructor) _read("prod(sort(\"B\"),[lit(\"0\")],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChzb3J0KCJCIiksW2xpdCgiMSIpXSx7fSk00 = (IConstructor) _read("prod(sort(\"B\"),[lit(\"1\")],{})", RascalValueFactory.Production);
    
  // Item declarations
	
	
  protected static class B {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJCIikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIkJcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIkIiKSkpfSk00(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[0] = new CharStackNode<IConstructor>(37, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(42, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(39, 2, cHJvZChsaXQoIjoiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDU4LDU4KV0pXSx7fSk00, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(41, 3, cmVndWxhcihpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSkp, new CharStackNode<IConstructor>(40, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(38, 1, cHJvZChsaXQoInNvcnQoXCJCXCIpIiksW1xjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTEsMTExKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDY2LDY2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgzNCwzNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSldLHt9KQ0000, new int[] {115,111,114,116,40,34,66,34,41}, null, null);
      builder.addAlternative(GEXPParser.cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJCIikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIkJcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIkIiKSkpfSk00, tmp);
	}
    protected static final void _init_cHJvZChzb3J0KCJCIiksW2xpdCgiMCIpXSx7fSk00(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(45, 0, cHJvZChsaXQoIjAiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ4LDQ4KV0pXSx7fSk00, new int[] {48}, null, null);
      builder.addAlternative(GEXPParser.cHJvZChzb3J0KCJCIiksW2xpdCgiMCIpXSx7fSk00, tmp);
	}
    protected static final void _init_cHJvZChzb3J0KCJCIiksW2xpdCgiMSIpXSx7fSk00(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(47, 0, cHJvZChsaXQoIjEiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ5LDQ5KV0pXSx7fSk00, new int[] {49}, null, null);
      builder.addAlternative(GEXPParser.cHJvZChzb3J0KCJCIiksW2xpdCgiMSIpXSx7fSk00, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJCIikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIkJcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIkIiKSkpfSk00(builder);
      
        _init_cHJvZChzb3J0KCJCIiksW2xpdCgiMCIpXSx7fSk00(builder);
      
        _init_cHJvZChzb3J0KCJCIiksW2xpdCgiMSIpXSx7fSk00(builder);
      
    }
  }
	
  protected static class E {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJFIikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIkVcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIkUiKSkpfSk00(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[1] = new LiteralStackNode<IConstructor>(69, 1, cHJvZChsaXQoInNvcnQoXCJFXCIpIiksW1xjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTEsMTExKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDY5LDY5KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgzNCwzNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSldLHt9KQ0000, new int[] {115,111,114,116,40,34,69,34,41}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(68, 0, new int[][]{{0,0}}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(73, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(70, 2, cHJvZChsaXQoIjoiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDU4LDU4KV0pXSx7fSk00, new int[] {58}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(72, 3, cmVndWxhcihpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSkp, new CharStackNode<IConstructor>(71, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(GEXPParser.cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJFIikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIkVcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIkUiKSkpfSk00, tmp);
	}
    protected static final void _init_cHJvZChzb3J0KCJFIiksW3NvcnQoIkIiKV0se30p(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new NonTerminalStackNode<IConstructor>(76, 0, "B", null, null);
      builder.addAlternative(GEXPParser.cHJvZChzb3J0KCJFIiksW3NvcnQoIkIiKV0se30p, tmp);
	}
    protected static final void _init_cHJvZChzb3J0KCJFIiksW3NvcnQoIkUiKSxsaXQoIisiKSxzb3J0KCJCIildLHt9KQ0000(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new LiteralStackNode<IConstructor>(79, 1, cHJvZChsaXQoIisiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQzLDQzKV0pXSx7fSk00, new int[] {43}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(78, 0, "E", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(80, 2, "B", null, null);
      builder.addAlternative(GEXPParser.cHJvZChzb3J0KCJFIiksW3NvcnQoIkUiKSxsaXQoIisiKSxzb3J0KCJCIildLHt9KQ0000, tmp);
	}
    protected static final void _init_cHJvZChzb3J0KCJFIiksW3NvcnQoIkUiKSxsaXQoIioiKSxzb3J0KCJCIildLHt9KQ0000(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new LiteralStackNode<IConstructor>(83, 1, cHJvZChsaXQoIioiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQyLDQyKV0pXSx7fSk00, new int[] {42}, null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(82, 0, "E", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(84, 2, "B", null, null);
      builder.addAlternative(GEXPParser.cHJvZChzb3J0KCJFIiksW3NvcnQoIkUiKSxsaXQoIioiKSxzb3J0KCJCIildLHt9KQ0000, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJFIikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIkVcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIkUiKSkpfSk00(builder);
      
        _init_cHJvZChzb3J0KCJFIiksW3NvcnQoIkIiKV0se30p(builder);
      
        _init_cHJvZChzb3J0KCJFIiksW3NvcnQoIkUiKSxsaXQoIisiKSxzb3J0KCJCIildLHt9KQ0000(builder);
      
        _init_cHJvZChzb3J0KCJFIiksW3NvcnQoIkUiKSxsaXQoIioiKSxzb3J0KCJCIildLHt9KQ0000(builder);
      
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
