package org.rascalmpl.test.parser;

import java.io.IOException;
import java.io.StringReader;

import io.usethesource.vallang.type.TypeFactory;
import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.io.StandardTextReader;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.stack.*;
import org.rascalmpl.parser.gtd.stack.filter.*;
import org.rascalmpl.parser.gtd.stack.filter.follow.*;
import org.rascalmpl.parser.gtd.preprocessing.ExpectBuilder;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.IntegerMap;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.parsetrees.ITree;

@SuppressWarnings("all")
public class SharedTripleNullableSeparator extends org.rascalmpl.parser.gtd.SGTDBF<IConstructor, ITree, ISourceLocation> implements IParserTest {
  protected final static IValueFactory VF = ValueFactoryFactory.getValueFactory();

  protected static IValue _read(java.lang.String s, io.usethesource.vallang.type.Type type) {
    try {
      return new StandardTextReader().read(VF, org.rascalmpl.values.RascalValueFactory.uptr, type, new StringReader(s));
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
    return (_dontNest.size() != 0);
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

  private static final IConstructor cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzZXEoW2xpdCgiLiIpLGxheW91dHMoIkxheW91dCIpLGxleCgiRGlnaXRzIildKSksW1xjaGFyLWNsYXNzKFtyYW5nZSgwLDApXSksbGl0KCJzZXEoW2xpdChcIi5cIiksc29ydChcIkRpZ2l0c1wiKV0pIiksbGl0KCI6IiksaXRlcihcY2hhci1jbGFzcyhbcmFuZ2UoNDgsNTcpXSkpLFxjaGFyLWNsYXNzKFtyYW5nZSgwLDApXSldLHt0YWcoImhvbGVUeXBlIihzZXEoW2xpdCgiLiIpLGxheW91dHMoIkxheW91dCIpLGxleCgiRGlnaXRzIildKSkpfSk00 = (IConstructor) _read("prod(label(\"$MetaHole\",seq([lit(\".\"),layouts(\"Layout\"),lex(\"Digits\")])),[\\char-class([range(0,0)]),lit(\"seq([lit(\\\".\\\"),sort(\\\"Digits\\\")])\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(seq([lit(\".\"),layouts(\"Layout\"),lex(\"Digits\")])))})", RascalValueFactory.Production);
  private static final IConstructor cmVndWxhcihzZXEoW2xpdCgiLiIpLGxheW91dHMoIkxheW91dCIpLGxleCgiRGlnaXRzIildKSk00 = (IConstructor) _read("regular(seq([lit(\".\"),layouts(\"Layout\"),lex(\"Digits\")]))", RascalValueFactory.Production);
  private static final IConstructor cmVndWxhcihcaXRlci1zdGFyKGxpdCgiICIpKSk00 = (IConstructor) _read("regular(\\iter-star(lit(\" \")))", RascalValueFactory.Production);
  private static final IConstructor cHJvZChzb3J0KCJMaXN0V2l0aEJyYWNrZXRzIiksW2NpbGl0KCJbIiksbGF5b3V0cygiTGF5b3V0Iiksc29ydCgiTGlzdCIpLGxheW91dHMoIkxheW91dCIpLGNpbGl0KCJdIildLHt9KQ0000 = (IConstructor) _read("prod(sort(\"ListWithBrackets\"),[cilit(\"[\"),layouts(\"Layout\"),sort(\"List\"),layouts(\"Layout\"),cilit(\"]\")],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChzb3J0KCJMaXN0IiksW1xpdGVyLXNlcHMoc29ydCgiRWxlbSIpLFtsYXlvdXRzKCJMYXlvdXQiKSxvcHQobGl0KCIsIikpLGxheW91dHMoIkxheW91dCIpXSldLHt9KQ0000 = (IConstructor) _read("prod(sort(\"List\"),[\\iter-seps(sort(\"Elem\"),[layouts(\"Layout\"),opt(lit(\",\")),layouts(\"Layout\")])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYWJlbCgiJE1ldGFIb2xlIixsaXQoIiAiKSksW1xjaGFyLWNsYXNzKFtyYW5nZSgwLDApXSksbGl0KCJcXGl0ZXItc3RhcihsaXQoXCIgXCIpKSIpLGxpdCgiOiIpLGl0ZXIoXGNoYXItY2xhc3MoW3JhbmdlKDQ4LDU3KV0pKSxcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pXSx7dGFnKCJob2xlVHlwZSIoXGl0ZXItc3RhcihsaXQoIiAiKSkpKX0p = (IConstructor) _read("prod(label(\"$MetaHole\",lit(\" \")),[\\char-class([range(0,0)]),lit(\"\\\\iter-star(lit(\\\" \\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-star(lit(\" \"))))})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoInNvcnQoXCJMaXN0XCIpIiksW1xjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTEsMTExKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDc2LDc2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMDUsMTA1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgzNCwzNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSldLHt9KQ0000 = (IConstructor) _read("prod(lit(\"sort(\\\"List\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(76,76)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYXlvdXRzKCIkZGVmYXVsdCQiKSxbXSx7fSk00 = (IConstructor) _read("prod(layouts(\"$default$\"),[],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChlbXB0eSgpLFtdLHt9KQ0000 = (IConstructor) _read("prod(empty(),[],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoInNvcnQoXCJEaWdpdHNcIikiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDExNSwxMTUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExMSwxMTEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNCwxMTQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNiwxMTYpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQwLDQwKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgzNCwzNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNjgsNjgpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwNSwxMDUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwMywxMDMpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwNSwxMDUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNiwxMTYpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNSwxMTUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDM0LDM0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MSw0MSldKV0se30p = (IConstructor) _read("prod(lit(\"sort(\\\"Digits\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(68,68)]),\\char-class([range(105,105)]),\\char-class([range(103,103)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(115,115)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIm9wdChsaXQoXCIsXCIpKSIpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMTExLDExMSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTEyLDExMildKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE2LDExNildKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDAsNDApXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwOCwxMDgpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwNSwxMDUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNiwxMTYpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQwLDQwKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgzNCwzNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDQsNDQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDM0LDM0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MSw0MSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSldLHt9KQ0000 = (IConstructor) _read("prod(lit(\"opt(lit(\\\",\\\"))\"),[\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(44,44)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIlxcaXRlci1zZXBzKHNvcnQoXCJFbGVtXCIpLFtvcHQobGl0KFwiLFwiKSldKSIpLFtcY2hhci1jbGFzcyhbcmFuZ2UoOTIsOTIpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwNSwxMDUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNiwxMTYpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwMSwxMDEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNCwxMTQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQ1LDQ1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMDEsMTAxKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTIsMTEyKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE1LDExNSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTExLDExMSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE0LDExNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE2LDExNildKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDAsNDApXSksXGNoYXItY2xhc3MoW3JhbmdlKDM0LDM0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg2OSw2OSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA4LDEwOCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTAxLDEwMSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA5LDEwOSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQxLDQxKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0NCw0NCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoOTEsOTEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExMSwxMTEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExMiwxMTIpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNiwxMTYpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQwLDQwKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMDgsMTA4KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMDUsMTA1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQ0LDQ0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgzNCwzNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQxLDQxKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg5Myw5MyldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSldLHt9KQ0000 = (IConstructor) _read("prod(lit(\"\\\\iter-seps(sort(\\\"Elem\\\"),[opt(lit(\\\",\\\"))])\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(112,112)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(69,69)]),\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(109,109)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(91,91)]),\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(44,44)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJFbGVtIikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgiXFxpdGVyLXNlcHMoc29ydChcIkVsZW1cIiksW29wdChsaXQoXCIsXCIpKV0pIiksbGl0KCI6IiksaXRlcihcY2hhci1jbGFzcyhbcmFuZ2UoNDgsNTcpXSkpLFxjaGFyLWNsYXNzKFtyYW5nZSgwLDApXSldLHt0YWcoImhvbGVUeXBlIihcaXRlci1zZXBzKHNvcnQoIkVsZW0iKSxbbGF5b3V0cygiTGF5b3V0Iiksb3B0KGxpdCgiLCIpKSxsYXlvdXRzKCJMYXlvdXQiKV0pKSl9KQ0000 = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Elem\")),[\\char-class([range(0,0)]),lit(\"\\\\iter-seps(sort(\\\"Elem\\\"),[opt(lit(\\\",\\\"))])\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-seps(sort(\"Elem\"),[layouts(\"Layout\"),opt(lit(\",\")),layouts(\"Layout\")])))})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChjaWxpdCgiXSIpLFtcY2hhci1jbGFzcyhbcmFuZ2UoOTMsOTMpXSldLHt9KQ0000 = (IConstructor) _read("prod(cilit(\"]\"),[\\char-class([range(93,93)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsZXgoIkRpZ2l0cyIpLFtjb25kaXRpb25hbChpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSkse1xub3QtZm9sbG93KFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSl9KV0se30p = (IConstructor) _read("prod(lex(\"Digits\"),[conditional(iter(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57)]))})],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIlxcaXRlci1zdGFyKGxpdChcIiBcIikpIiksW1xjaGFyLWNsYXNzKFtyYW5nZSg5Miw5MildKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA1LDEwNSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE2LDExNildKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTAxLDEwMSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE0LDExNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDUsNDUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNSwxMTUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNiwxMTYpXSksXGNoYXItY2xhc3MoW3JhbmdlKDk3LDk3KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA4LDEwOCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA1LDEwNSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE2LDExNildKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDAsNDApXSksXGNoYXItY2xhc3MoW3JhbmdlKDM0LDM0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgzMiwzMildKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQxLDQxKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MSw0MSldKV0se30p = (IConstructor) _read("prod(lit(\"\\\\iter-star(lit(\\\" \\\"))\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(32,32)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYWJlbCgiJE1ldGFIb2xlIixcY2hhci1jbGFzcyhbcmFuZ2UoNDgsNTcpXSkpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgiaXRlcihcXGNoYXItY2xhc3MoW3JhbmdlKDQ4LDU3KV0pKSIpLGxpdCgiOiIpLGl0ZXIoXGNoYXItY2xhc3MoW3JhbmdlKDQ4LDU3KV0pKSxcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pXSx7dGFnKCJob2xlVHlwZSIoaXRlcihcY2hhci1jbGFzcyhbcmFuZ2UoNDgsNTcpXSkpKSl9KQ0000 = (IConstructor) _read("prod(label(\"$MetaHole\",\\char-class([range(48,57)])),[\\char-class([range(0,0)]),lit(\"iter(\\\\char-class([range(48,57)]))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(iter(\\char-class([range(48,57)]))))})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJMaXN0IikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIkxpc3RcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIkxpc3QiKSkpfSk00 = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"List\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"List\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"List\")))})", RascalValueFactory.Production);
  private static final IConstructor cmVndWxhcihpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSkp = (IConstructor) _read("regular(iter(\\char-class([range(48,57)])))", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIjoiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDU4LDU4KV0pXSx7fSk00 = (IConstructor) _read("prod(lit(\":\"),[\\char-class([range(58,58)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIml0ZXIoXFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSkiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDEwNSwxMDUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNiwxMTYpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwMSwxMDEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNCwxMTQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQwLDQwKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg5Miw5MildKSxcY2hhci1jbGFzcyhbcmFuZ2UoOTksOTkpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwNCwxMDQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDk3LDk3KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0NSw0NSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoOTksOTkpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwOCwxMDgpXSksXGNoYXItY2xhc3MoW3JhbmdlKDk3LDk3KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoOTEsOTEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNCwxMTQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDk3LDk3KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTAsMTEwKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMDMsMTAzKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMDEsMTAxKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNTIsNTIpXSksXGNoYXItY2xhc3MoW3JhbmdlKDU2LDU2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0NCw0NCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNTMsNTMpXSksXGNoYXItY2xhc3MoW3JhbmdlKDU1LDU1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MSw0MSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoOTMsOTMpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQxLDQxKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MSw0MSldKV0se30p = (IConstructor) _read("prod(lit(\"iter(\\\\char-class([range(48,57)]))\"),[\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(92,92)]),\\char-class([range(99,99)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(52,52)]),\\char-class([range(56,56)]),\\char-class([range(44,44)]),\\char-class([range(53,53)]),\\char-class([range(55,55)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cmVndWxhcihvcHQobGl0KCIsIikpKQ0000 = (IConstructor) _read("regular(opt(lit(\",\")))", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJFbGVtIikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIkVsZW1cIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIkVsZW0iKSkpfSk00 = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Elem\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Elem\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"Elem\")))})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYWJlbCgiJE1ldGFIb2xlIixsaXQoIiwiKSksW1xjaGFyLWNsYXNzKFtyYW5nZSgwLDApXSksbGl0KCJvcHQobGl0KFwiLFwiKSkiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKG9wdChsaXQoIiwiKSkpKX0p = (IConstructor) _read("prod(label(\"$MetaHole\",lit(\",\")),[\\char-class([range(0,0)]),lit(\"opt(lit(\\\",\\\"))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(opt(lit(\",\"))))})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoInNvcnQoXCJMaXN0V2l0aEJyYWNrZXRzXCIpIiksW1xjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTEsMTExKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDc2LDc2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMDUsMTA1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg4Nyw4NyldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA1LDEwNSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE2LDExNildKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA0LDEwNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNjYsNjYpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNCwxMTQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDk3LDk3KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg5OSw5OSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA3LDEwNyldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTAxLDEwMSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE2LDExNildKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE1LDExNSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQxLDQxKV0pXSx7fSk00 = (IConstructor) _read("prod(lit(\"sort(\\\"ListWithBrackets\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(76,76)]),\\char-class([range(105,105)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(87,87)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(104,104)]),\\char-class([range(66,66)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(99,99)]),\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(116,116)]),\\char-class([range(115,115)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoInNvcnQoXCJFbGVtXCIpIiksW1xjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTEsMTExKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDY5LDY5KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMDgsMTA4KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMDEsMTAxKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMDksMTA5KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgzNCwzNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSldLHt9KQ0000 = (IConstructor) _read("prod(lit(\"sort(\\\"Elem\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(69,69)]),\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(109,109)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYWJlbCgiJE1ldGFIb2xlIixsZXgoIkRpZ2l0cyIpKSxbXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKSxsaXQoInNvcnQoXCJEaWdpdHNcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKGxleCgiRGlnaXRzIikpKX0p = (IConstructor) _read("prod(label(\"$MetaHole\",lex(\"Digits\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Digits\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(lex(\"Digits\")))})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIi4iKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ2LDQ2KV0pXSx7fSk00 = (IConstructor) _read("prod(lit(\".\"),[\\char-class([range(46,46)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIiAiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDMyLDMyKV0pXSx7fSk00 = (IConstructor) _read("prod(lit(\" \"),[\\char-class([range(32,32)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChzb3J0KCJFbGVtIiksW2xleCgiRGlnaXRzIiksbGF5b3V0cygiTGF5b3V0Iiksb3B0KHNlcShbbGl0KCIuIiksbGF5b3V0cygiTGF5b3V0IiksbGV4KCJEaWdpdHMiKV0pKV0se30p = (IConstructor) _read("prod(sort(\"Elem\"),[lex(\"Digits\"),layouts(\"Layout\"),opt(seq([lit(\".\"),layouts(\"Layout\"),lex(\"Digits\")]))],{})", RascalValueFactory.Production);
  private static final IConstructor cmVndWxhcihvcHQoc2VxKFtsaXQoIi4iKSxsYXlvdXRzKCJMYXlvdXQiKSxsZXgoIkRpZ2l0cyIpXSkpKQ0000 = (IConstructor) _read("regular(opt(seq([lit(\".\"),layouts(\"Layout\"),lex(\"Digits\")])))", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzZXEoW2xpdCgiLiIpLGxheW91dHMoIkxheW91dCIpLGxleCgiRGlnaXRzIildKSksW1xjaGFyLWNsYXNzKFtyYW5nZSgwLDApXSksbGl0KCJvcHQoc2VxKFtsaXQoXCIuXCIpLHNvcnQoXCJEaWdpdHNcIildKSkiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKG9wdChzZXEoW2xpdCgiLiIpLGxheW91dHMoIkxheW91dCIpLGxleCgiRGlnaXRzIildKSkpKX0p = (IConstructor) _read("prod(label(\"$MetaHole\",seq([lit(\".\"),layouts(\"Layout\"),lex(\"Digits\")])),[\\char-class([range(0,0)]),lit(\"opt(seq([lit(\\\".\\\"),sort(\\\"Digits\\\")]))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(opt(seq([lit(\".\"),layouts(\"Layout\"),lex(\"Digits\")]))))})", RascalValueFactory.Production);
  private static final IConstructor cmVndWxhcihcaXRlci1zZXBzKHNvcnQoIkVsZW0iKSxbbGF5b3V0cygiTGF5b3V0Iiksb3B0KGxpdCgiLCIpKSxsYXlvdXRzKCJMYXlvdXQiKV0pKQ0000 = (IConstructor) _read("regular(\\iter-seps(sort(\"Elem\"),[layouts(\"Layout\"),opt(lit(\",\")),layouts(\"Layout\")]))", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJMaXN0V2l0aEJyYWNrZXRzIikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIkxpc3RXaXRoQnJhY2tldHNcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIkxpc3RXaXRoQnJhY2tldHMiKSkpfSk00 = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"ListWithBrackets\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"ListWithBrackets\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"ListWithBrackets\")))})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYXlvdXRzKCJMYXlvdXQiKSxbY29uZGl0aW9uYWwoXGl0ZXItc3RhcihsaXQoIiAiKSkse1xub3QtZm9sbG93KFxjaGFyLWNsYXNzKFtyYW5nZSgzMiwzMildKSl9KV0se30p = (IConstructor) _read("prod(layouts(\"Layout\"),[conditional(\\iter-star(lit(\" \")),{\\not-follow(\\char-class([range(32,32)]))})],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoInNlcShbbGl0KFwiLlwiKSxzb3J0KFwiRGlnaXRzXCIpXSkiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDExNSwxMTUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwMSwxMDEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExMywxMTMpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQwLDQwKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg5MSw5MSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA4LDEwOCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA1LDEwNSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE2LDExNildKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDAsNDApXSksXGNoYXItY2xhc3MoW3JhbmdlKDM0LDM0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0Niw0NildKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQxLDQxKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0NCw0NCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE1LDExNSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTExLDExMSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE0LDExNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE2LDExNildKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDAsNDApXSksXGNoYXItY2xhc3MoW3JhbmdlKDM0LDM0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg2OCw2OCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA1LDEwNSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTAzLDEwMyldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA1LDEwNSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE2LDExNildKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE1LDExNSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQxLDQxKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg5Myw5MyldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSldLHt9KQ0000 = (IConstructor) _read("prod(lit(\"seq([lit(\\\".\\\"),sort(\\\"Digits\\\")])\"),[\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(113,113)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(46,46)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(68,68)]),\\char-class([range(105,105)]),\\char-class([range(103,103)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(115,115)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIiwiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ0LDQ0KV0pXSx7fSk00 = (IConstructor) _read("prod(lit(\",\"),[\\char-class([range(44,44)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIm9wdChzZXEoW2xpdChcIi5cIiksc29ydChcIkRpZ2l0c1wiKV0pKSIpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMTExLDExMSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTEyLDExMildKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE2LDExNildKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDAsNDApXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNSwxMTUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwMSwxMDEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExMywxMTMpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQwLDQwKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg5MSw5MSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA4LDEwOCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA1LDEwNSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE2LDExNildKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDAsNDApXSksXGNoYXItY2xhc3MoW3JhbmdlKDM0LDM0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0Niw0NildKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQxLDQxKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0NCw0NCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE1LDExNSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTExLDExMSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE0LDExNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE2LDExNildKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDAsNDApXSksXGNoYXItY2xhc3MoW3JhbmdlKDM0LDM0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg2OCw2OCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA1LDEwNSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTAzLDEwMyldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA1LDEwNSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE2LDExNildKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE1LDExNSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQxLDQxKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg5Myw5MyldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQxLDQxKV0pXSx7fSk00 = (IConstructor) _read("prod(lit(\"opt(seq([lit(\\\".\\\"),sort(\\\"Digits\\\")]))\"),[\\char-class([range(111,111)]),\\char-class([range(112,112)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(113,113)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(46,46)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(68,68)]),\\char-class([range(105,105)]),\\char-class([range(103,103)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(115,115)]),\\char-class([range(34,34)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChjaWxpdCgiWyIpLFtcY2hhci1jbGFzcyhbcmFuZ2UoOTEsOTEpXSldLHt9KQ0000 = (IConstructor) _read("prod(cilit(\"[\"),[\\char-class([range(91,91)])],{})", RascalValueFactory.Production);

  // Item declarations


  protected static class List {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_dontNest, _resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }

    protected static final void _init_cHJvZChzb3J0KCJMaXN0IiksW1xpdGVyLXNlcHMoc29ydCgiRWxlbSIpLFtsYXlvdXRzKCJMYXlvdXQiKSxvcHQobGl0KCIsIikpLGxheW91dHMoIkxheW91dCIpXSldLHt9KQ0000(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];

      tmp[0] = new SeparatedListStackNode<IConstructor>(61, 0, cmVndWxhcihcaXRlci1zZXBzKHNvcnQoIkVsZW0iKSxbbGF5b3V0cygiTGF5b3V0Iiksb3B0KGxpdCgiLCIpKSxsYXlvdXRzKCJMYXlvdXQiKV0pKQ0000, new NonTerminalStackNode<IConstructor>(56, 0, "Elem", null, null), (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new NonTerminalStackNode<IConstructor>(57, 1, "layouts_Layout", null, null), new OptionalStackNode<IConstructor>(59, 2, cmVndWxhcihvcHQobGl0KCIsIikpKQ0000, new LiteralStackNode<IConstructor>(58, 0, cHJvZChsaXQoIiwiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ0LDQ0KV0pXSx7fSk00, new int[] {44}, null, null), null, null), new NonTerminalStackNode<IConstructor>(60, 3, "layouts_Layout", null, null)}, true, null, null);
      builder.addAlternative(SharedTripleNullableSeparator.cHJvZChzb3J0KCJMaXN0IiksW1xpdGVyLXNlcHMoc29ydCgiRWxlbSIpLFtsYXlvdXRzKCJMYXlvdXQiKSxvcHQobGl0KCIsIikpLGxheW91dHMoIkxheW91dCIpXSldLHt9KQ0000, tmp);
    }
    public static void init(ExpectBuilder<IConstructor> builder){

      _init_cHJvZChzb3J0KCJMaXN0IiksW1xpdGVyLXNlcHMoc29ydCgiRWxlbSIpLFtsYXlvdXRzKCJMYXlvdXQiKSxvcHQobGl0KCIsIikpLGxheW91dHMoIkxheW91dCIpXSldLHt9KQ0000(builder);

    }
  }

  protected static class layouts_$default$ {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_dontNest, _resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }

    protected static final void _init_cHJvZChsYXlvdXRzKCIkZGVmYXVsdCQiKSxbXSx7fSk00(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];

      tmp[0] = new EpsilonStackNode<IConstructor>(155, 0);
      builder.addAlternative(SharedTripleNullableSeparator.cHJvZChsYXlvdXRzKCIkZGVmYXVsdCQiKSxbXSx7fSk00, tmp);
    }
    public static void init(ExpectBuilder<IConstructor> builder){

      _init_cHJvZChsYXlvdXRzKCIkZGVmYXVsdCQiKSxbXSx7fSk00(builder);

    }
  }

  protected static class layouts_Layout {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_dontNest, _resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }

    protected static final void _init_cHJvZChsYXlvdXRzKCJMYXlvdXQiKSxbY29uZGl0aW9uYWwoXGl0ZXItc3RhcihsaXQoIiAiKSkse1xub3QtZm9sbG93KFxjaGFyLWNsYXNzKFtyYW5nZSgzMiwzMildKSl9KV0se30p(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];

      tmp[0] = new ListStackNode<IConstructor>(309, 0, cmVndWxhcihcaXRlci1zdGFyKGxpdCgiICIpKSk00, new LiteralStackNode<IConstructor>(306, 0, cHJvZChsaXQoIiAiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDMyLDMyKV0pXSx7fSk00, new int[] {32}, null, null), false, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{32,32}})});
      builder.addAlternative(SharedTripleNullableSeparator.cHJvZChsYXlvdXRzKCJMYXlvdXQiKSxbY29uZGl0aW9uYWwoXGl0ZXItc3RhcihsaXQoIiAiKSkse1xub3QtZm9sbG93KFxjaGFyLWNsYXNzKFtyYW5nZSgzMiwzMildKSl9KV0se30p, tmp);
    }
    public static void init(ExpectBuilder<IConstructor> builder){

      _init_cHJvZChsYXlvdXRzKCJMYXlvdXQiKSxbY29uZGl0aW9uYWwoXGl0ZXItc3RhcihsaXQoIiAiKSkse1xub3QtZm9sbG93KFxjaGFyLWNsYXNzKFtyYW5nZSgzMiwzMildKSl9KV0se30p(builder);

    }
  }

  protected static class Elem {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_dontNest, _resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }

    protected static final void _init_cHJvZChzb3J0KCJFbGVtIiksW2xleCgiRGlnaXRzIiksbGF5b3V0cygiTGF5b3V0Iiksb3B0KHNlcShbbGl0KCIuIiksbGF5b3V0cygiTGF5b3V0IiksbGV4KCJEaWdpdHMiKV0pKV0se30p(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];

      tmp[0] = new NonTerminalStackNode<IConstructor>(373, 0, "Digits", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(374, 1, "layouts_Layout", null, null);
      tmp[2] = new OptionalStackNode<IConstructor>(379, 2, cmVndWxhcihvcHQoc2VxKFtsaXQoIi4iKSxsYXlvdXRzKCJMYXlvdXQiKSxsZXgoIkRpZ2l0cyIpXSkpKQ0000, new SequenceStackNode<IConstructor>(378, 0, cmVndWxhcihzZXEoW2xpdCgiLiIpLGxheW91dHMoIkxheW91dCIpLGxleCgiRGlnaXRzIildKSk00, (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{new LiteralStackNode<IConstructor>(375, 0, cHJvZChsaXQoIi4iKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ2LDQ2KV0pXSx7fSk00, new int[] {46}, null, null), new NonTerminalStackNode<IConstructor>(376, 1, "layouts_Layout", null, null), new NonTerminalStackNode<IConstructor>(377, 2, "Digits", null, null)}, null, null), null, null);
      builder.addAlternative(SharedTripleNullableSeparator.cHJvZChzb3J0KCJFbGVtIiksW2xleCgiRGlnaXRzIiksbGF5b3V0cygiTGF5b3V0Iiksb3B0KHNlcShbbGl0KCIuIiksbGF5b3V0cygiTGF5b3V0IiksbGV4KCJEaWdpdHMiKV0pKV0se30p, tmp);
    }
    public static void init(ExpectBuilder<IConstructor> builder){

      _init_cHJvZChzb3J0KCJFbGVtIiksW2xleCgiRGlnaXRzIiksbGF5b3V0cygiTGF5b3V0Iiksb3B0KHNlcShbbGl0KCIuIiksbGF5b3V0cygiTGF5b3V0IiksbGV4KCJEaWdpdHMiKV0pKV0se30p(builder);

    }
  }

  protected static class ListWithBrackets {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_dontNest, _resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }

    protected static final void _init_cHJvZChzb3J0KCJMaXN0V2l0aEJyYWNrZXRzIiksW2NpbGl0KCJbIiksbGF5b3V0cygiTGF5b3V0Iiksc29ydCgiTGlzdCIpLGxheW91dHMoIkxheW91dCIpLGNpbGl0KCJdIildLHt9KQ0000(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];

      tmp[0] = new CaseInsensitiveLiteralStackNode<IConstructor>(396, 0, cHJvZChjaWxpdCgiWyIpLFtcY2hhci1jbGFzcyhbcmFuZ2UoOTEsOTEpXSldLHt9KQ0000, new int[] {91}, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(397, 1, "layouts_Layout", null, null);
      tmp[2] = new NonTerminalStackNode<IConstructor>(398, 2, "List", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(399, 3, "layouts_Layout", null, null);
      tmp[4] = new CaseInsensitiveLiteralStackNode<IConstructor>(400, 4, cHJvZChjaWxpdCgiXSIpLFtcY2hhci1jbGFzcyhbcmFuZ2UoOTMsOTMpXSldLHt9KQ0000, new int[] {93}, null, null);
      builder.addAlternative(SharedTripleNullableSeparator.cHJvZChzb3J0KCJMaXN0V2l0aEJyYWNrZXRzIiksW2NpbGl0KCJbIiksbGF5b3V0cygiTGF5b3V0Iiksc29ydCgiTGlzdCIpLGxheW91dHMoIkxheW91dCIpLGNpbGl0KCJdIildLHt9KQ0000, tmp);
    }

    public static void init(ExpectBuilder<IConstructor> builder){

      _init_cHJvZChzb3J0KCJMaXN0V2l0aEJyYWNrZXRzIiksW2NpbGl0KCJbIiksbGF5b3V0cygiTGF5b3V0Iiksc29ydCgiTGlzdCIpLGxheW91dHMoIkxheW91dCIpLGNpbGl0KCJdIildLHt9KQ0000(builder);

    }
  }

  protected static class Digits {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_dontNest, _resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }

    protected static final void _init_cHJvZChsZXgoIkRpZ2l0cyIpLFtjb25kaXRpb25hbChpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSkse1xub3QtZm9sbG93KFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSl9KV0se30p(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];

      tmp[0] = new ListStackNode<IConstructor>(415, 0, cmVndWxhcihpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSkp, new CharStackNode<IConstructor>(412, 0, new int[][]{{48,57}}, null, null), true, null, new ICompletionFilter[] {new CharFollowRestriction(new int[][]{{48,57}})});
      builder.addAlternative(SharedTripleNullableSeparator.cHJvZChsZXgoIkRpZ2l0cyIpLFtjb25kaXRpb25hbChpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSkse1xub3QtZm9sbG93KFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSl9KV0se30p, tmp);
    }

    public static void init(ExpectBuilder<IConstructor> builder){

      _init_cHJvZChsZXgoIkRpZ2l0cyIpLFtjb25kaXRpb25hbChpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSkse1xub3QtZm9sbG93KFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSl9KV0se30p(builder);

    }
  }

  private int nextFreeStackNodeId = 467;
  protected int getFreeStackNodeId() {
    return nextFreeStackNodeId++;
  }

  // Parse methods

  public AbstractStackNode<IConstructor>[] List() {
    return List.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] layouts_$default$() {
    return layouts_$default$.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] layouts_Layout() {
    return layouts_Layout.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Elem() {
    return Elem.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] ListWithBrackets() {
    return ListWithBrackets.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Digits() {
    return Digits.EXPECTS;
  }

  private final static AbstractStackNode<IConstructor> NONTERMINAL_START = new NonTerminalStackNode<IConstructor>(AbstractStackNode.START_SYMBOL_ID, 0, "ListWithBrackets");

  public ITree executeParser(){
    return parse(NONTERMINAL_START, null, "[1 2]".toCharArray(), new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(true));
  }

  public IValue getExpectedResult() throws IOException{
    String expectedInput = "appl(prod(sort(\"ListWithBrackets\"),[cilit(\"[\"),layouts(\"Layout\"),sort(\"List\"),layouts(\"Layout\"),cilit(\"]\")],{}),[appl(prod(cilit(\"[\"),[\\char-class([range(91,91)])],{}),[char(91)]),appl(prod(layouts(\"Layout\"),[conditional(\\iter-star(lit(\" \")),{\\not-follow(\\char-class([range(32,32)]))})],{}),[appl(regular(\\iter-star(lit(\" \"))),[])]),appl(prod(sort(\"List\"),[\\iter-seps(sort(\"Elem\"),[layouts(\"Layout\"),opt(lit(\",\")),layouts(\"Layout\")])],{}),[appl(regular(\\iter-seps(sort(\"Elem\"),[layouts(\"Layout\"),opt(lit(\",\")),layouts(\"Layout\")])),[appl(prod(sort(\"Elem\"),[lex(\"Digits\"),layouts(\"Layout\"),opt(seq([lit(\".\"),layouts(\"Layout\"),lex(\"Digits\")]))],{}),[appl(prod(lex(\"Digits\"),[conditional(iter(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57)]))})],{}),[appl(regular(iter(\\char-class([range(48,57)]))),[char(49)])]),appl(prod(layouts(\"Layout\"),[conditional(\\iter-star(lit(\" \")),{\\not-follow(\\char-class([range(32,32)]))})],{}),[appl(regular(\\iter-star(lit(\" \"))),[appl(prod(lit(\" \"),[\\char-class([range(32,32)])],{}),[char(32)])])]),appl(regular(opt(seq([lit(\".\"),layouts(\"Layout\"),lex(\"Digits\")]))),[])]),appl(prod(layouts(\"Layout\"),[conditional(\\iter-star(lit(\" \")),{\\not-follow(\\char-class([range(32,32)]))})],{}),[appl(regular(\\iter-star(lit(\" \"))),[])]),appl(regular(opt(lit(\",\"))),[]),appl(prod(layouts(\"Layout\"),[conditional(\\iter-star(lit(\" \")),{\\not-follow(\\char-class([range(32,32)]))})],{}),[appl(regular(\\iter-star(lit(\" \"))),[])]),appl(prod(sort(\"Elem\"),[lex(\"Digits\"),layouts(\"Layout\"),opt(seq([lit(\".\"),layouts(\"Layout\"),lex(\"Digits\")]))],{}),[appl(prod(lex(\"Digits\"),[conditional(iter(\\char-class([range(48,57)])),{\\not-follow(\\char-class([range(48,57)]))})],{}),[appl(regular(iter(\\char-class([range(48,57)]))),[char(50)])]),appl(prod(layouts(\"Layout\"),[conditional(\\iter-star(lit(\" \")),{\\not-follow(\\char-class([range(32,32)]))})],{}),[appl(regular(\\iter-star(lit(\" \"))),[])]),appl(regular(opt(seq([lit(\".\"),layouts(\"Layout\"),lex(\"Digits\")]))),[])])])]),appl(prod(layouts(\"Layout\"),[conditional(\\iter-star(lit(\" \")),{\\not-follow(\\char-class([range(32,32)]))})],{}),[appl(regular(\\iter-star(lit(\" \"))),[])]),appl(prod(cilit(\"]\"),[\\char-class([range(93,93)])],{}),[char(93)])])";
    return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
  }

  public static void main(String[] args){
    SharedTripleNullableSeparator stns = new SharedTripleNullableSeparator();
    IConstructor result = stns.executeParser();
    System.out.println(result);
  }
}