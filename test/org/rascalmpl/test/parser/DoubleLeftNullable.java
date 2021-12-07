package org.rascalmpl.test.parser;

import java.io.IOException;
import java.io.StringReader;

import org.rascalmpl.parser.gtd.preprocessing.ExpectBuilder;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.CharStackNode;
import org.rascalmpl.parser.gtd.stack.EmptyStackNode;
import org.rascalmpl.parser.gtd.stack.EpsilonStackNode;
import org.rascalmpl.parser.gtd.stack.ListStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.stack.filter.ICompletionFilter;
import org.rascalmpl.parser.gtd.stack.filter.follow.CharFollowRestriction;
import org.rascalmpl.parser.gtd.util.IntegerMap;
import org.rascalmpl.parser.uptr.UPTRNodeFactory;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;

import io.usethesource.vallang.IConstructor;
import io.usethesource.vallang.ISourceLocation;
import io.usethesource.vallang.IValue;
import io.usethesource.vallang.IValueFactory;
import io.usethesource.vallang.exceptions.FactTypeUseException;
import io.usethesource.vallang.io.StandardTextReader;
import io.usethesource.vallang.type.TypeFactory;

/*
layout WS = [\ \t\r\n]* ;

start syntax Stmt
    = "if" "(" Expr ")" Stmt ()
    | () "if" "(" Expr ")" Stmt "else" Stmt
    | "{" "}"
    | bla: "bla"
    ;

syntax Expr = "x";

Issue #1543 would cause an ArrayIndexOutOfBounds exception on this input

str example =    "if (x)
                 '   if (x)
                 '       {}";
*/
@SuppressWarnings({"unchecked"})
public class DoubleLeftNullable extends org.rascalmpl.parser.gtd.SGTDBF<IConstructor, ITree, ISourceLocation> implements IParserTest {
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
 
  // Production declarations
	
  // Production declarations
	
  private static final IConstructor cHJvZChsYWJlbCgiJE1ldGFIb2xlIixcY2hhci1jbGFzcyhbcmFuZ2UoOSwxMCkscmFuZ2UoMTMsMTMpLHJhbmdlKDMyLDMyKV0pKSxbXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKSxsaXQoIlxcaXRlci1zdGFyKFxcY2hhci1jbGFzcyhbcmFuZ2UoOSwxMCkscmFuZ2UoMTMsMTMpLHJhbmdlKDMyLDMyKV0pKSIpLGxpdCgiOiIpLGl0ZXIoXGNoYXItY2xhc3MoW3JhbmdlKDQ4LDU3KV0pKSxcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pXSx7dGFnKCJob2xlVHlwZSIoXGl0ZXItc3RhcihcY2hhci1jbGFzcyhbcmFuZ2UoOSwxMCkscmFuZ2UoMTMsMTMpLHJhbmdlKDMyLDMyKV0pKSkpfSk00 = (IConstructor) _read("prod(label(\"$MetaHole\",\\char-class([range(9,10),range(13,13),range(32,32)])),[\\char-class([range(0,0)]),lit(\"\\\\iter-star(\\\\char-class([range(9,10),range(13,13),range(32,32)]))\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))))})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYXlvdXRzKCJXUyIpLFtcaXRlci1zdGFyKFxjaGFyLWNsYXNzKFtyYW5nZSg5LDEwKSxyYW5nZSgxMywxMykscmFuZ2UoMzIsMzIpXSkpXSx7fSk00 = (IConstructor) _read("prod(layouts(\"WS\"),[\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],{})", RascalValueFactory.Production);
  private static final IConstructor cmVndWxhcihpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSkp = (IConstructor) _read("regular(iter(\\char-class([range(48,57)])))", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoInNvcnQoXCJFeHByXCIpIiksW1xjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTEsMTExKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDY5LDY5KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMjAsMTIwKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTIsMTEyKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgzNCwzNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSldLHt9KQ0000 = (IConstructor) _read("prod(lit(\"sort(\\\"Expr\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(69,69)]),\\char-class([range(120,120)]),\\char-class([range(112,112)]),\\char-class([range(114,114)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cmVndWxhcihlbXB0eSgpKQ0000 = (IConstructor) _read("regular(empty())", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoInsiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDEyMywxMjMpXSldLHt9KQ0000 = (IConstructor) _read("prod(lit(\"{\"),[\\char-class([range(123,123)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIjoiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDU4LDU4KV0pXSx7fSk00 = (IConstructor) _read("prod(lit(\":\"),[\\char-class([range(58,58)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJFeHByIikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIkV4cHJcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIkV4cHIiKSkpfSk00 = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Expr\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Expr\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"Expr\")))})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoImlmIiksW1xjaGFyLWNsYXNzKFtyYW5nZSgxMDUsMTA1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMDIsMTAyKV0pXSx7fSk00 = (IConstructor) _read("prod(lit(\"if\"),[\\char-class([range(105,105)]),\\char-class([range(102,102)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIn0iKSxbXGNoYXItY2xhc3MoW3JhbmdlKDEyNSwxMjUpXSldLHt9KQ0000 = (IConstructor) _read("prod(lit(\"}\"),[\\char-class([range(125,125)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIigiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQwLDQwKV0pXSx7fSk00 = (IConstructor) _read("prod(lit(\"(\"),[\\char-class([range(40,40)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChzb3J0KCJTdG10IiksW2VtcHR5KCksbGF5b3V0cygiV1MiKSxsaXQoImlmIiksbGF5b3V0cygiV1MiKSxsaXQoIigiKSxsYXlvdXRzKCJXUyIpLHNvcnQoIkV4cHIiKSxsYXlvdXRzKCJXUyIpLGxpdCgiKSIpLGxheW91dHMoIldTIiksc29ydCgiU3RtdCIpLGxheW91dHMoIldTIiksbGl0KCJlbHNlIiksbGF5b3V0cygiV1MiKSxzb3J0KCJTdG10IildLHt9KQ0000 = (IConstructor) _read("prod(sort(\"Stmt\"),[empty(),layouts(\"WS\"),lit(\"if\"),layouts(\"WS\"),lit(\"(\"),layouts(\"WS\"),sort(\"Expr\"),layouts(\"WS\"),lit(\")\"),layouts(\"WS\"),sort(\"Stmt\"),layouts(\"WS\"),lit(\"else\"),layouts(\"WS\"),sort(\"Stmt\")],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChzb3J0KCJFeHByIiksW2xpdCgieCIpXSx7fSk00 = (IConstructor) _read("prod(sort(\"Expr\"),[lit(\"x\")],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChzb3J0KCJTdG10IiksW2xpdCgieyIpLGxheW91dHMoIldTIiksbGl0KCJ9IildLHt9KQ0000 = (IConstructor) _read("prod(sort(\"Stmt\"),[lit(\"{\"),layouts(\"WS\"),lit(\"}\")],{})", RascalValueFactory.Production);
  private static final IConstructor cmVndWxhcihcaXRlci1zdGFyKFxjaGFyLWNsYXNzKFtyYW5nZSg5LDEwKSxyYW5nZSgxMywxMykscmFuZ2UoMzIsMzIpXSkpKQ0000 = (IConstructor) _read("regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)])))", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoInNvcnQoXCJTdG10XCIpIiksW1xjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTEsMTExKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDgzLDgzKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMDksMTA5KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgzNCwzNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSldLHt9KQ0000 = (IConstructor) _read("prod(lit(\"sort(\\\"Stmt\\\")\"),[\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(40,40)]),\\char-class([range(34,34)]),\\char-class([range(83,83)]),\\char-class([range(116,116)]),\\char-class([range(109,109)]),\\char-class([range(116,116)]),\\char-class([range(34,34)]),\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYXlvdXRzKCIkZGVmYXVsdCQiKSxbXSx7fSk00 = (IConstructor) _read("prod(layouts(\"$default$\"),[],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIlxcaXRlci1zdGFyKFxcY2hhci1jbGFzcyhbcmFuZ2UoOSwxMCkscmFuZ2UoMTMsMTMpLHJhbmdlKDMyLDMyKV0pKSIpLFtcY2hhci1jbGFzcyhbcmFuZ2UoOTIsOTIpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwNSwxMDUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNiwxMTYpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwMSwxMDEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNCwxMTQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQ1LDQ1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg5Nyw5NyldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE0LDExNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDAsNDApXSksXGNoYXItY2xhc3MoW3JhbmdlKDkyLDkyKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg5OSw5OSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA0LDEwNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoOTcsOTcpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNCwxMTQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQ1LDQ1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg5OSw5OSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTA4LDEwOCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoOTcsOTcpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNSwxMTUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNSwxMTUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQwLDQwKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg5MSw5MSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTE0LDExNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoOTcsOTcpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExMCwxMTApXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwMywxMDMpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwMSwxMDEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQwLDQwKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg1Nyw1NyldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDQsNDQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQ5LDQ5KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw0OCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQ0LDQ0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg5Nyw5NyldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTEwLDExMCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTAzLDEwMyldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTAxLDEwMSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDAsNDApXSksXGNoYXItY2xhc3MoW3JhbmdlKDQ5LDQ5KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg1MSw1MSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDQsNDQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQ5LDQ5KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg1MSw1MSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDQ0LDQ0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg5Nyw5NyldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTEwLDExMCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTAzLDEwMyldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMTAxLDEwMSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDAsNDApXSksXGNoYXItY2xhc3MoW3JhbmdlKDUxLDUxKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg1MCw1MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDQsNDQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDUxLDUxKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg1MCw1MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDkzLDkzKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MSw0MSldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSldLHt9KQ0000 = (IConstructor) _read("prod(lit(\"\\\\iter-star(\\\\char-class([range(9,10),range(13,13),range(32,32)]))\"),[\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(40,40)]),\\char-class([range(92,92)]),\\char-class([range(99,99)]),\\char-class([range(104,104)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(45,45)]),\\char-class([range(99,99)]),\\char-class([range(108,108)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(40,40)]),\\char-class([range(91,91)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(57,57)]),\\char-class([range(44,44)]),\\char-class([range(49,49)]),\\char-class([range(48,48)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(49,49)]),\\char-class([range(51,51)]),\\char-class([range(44,44)]),\\char-class([range(49,49)]),\\char-class([range(51,51)]),\\char-class([range(41,41)]),\\char-class([range(44,44)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(110,110)]),\\char-class([range(103,103)]),\\char-class([range(101,101)]),\\char-class([range(40,40)]),\\char-class([range(51,51)]),\\char-class([range(50,50)]),\\char-class([range(44,44)]),\\char-class([range(51,51)]),\\char-class([range(50,50)]),\\char-class([range(41,41)]),\\char-class([range(93,93)]),\\char-class([range(41,41)]),\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoImVsc2UiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDEwMSwxMDEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwOCwxMDgpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNSwxMTUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwMSwxMDEpXSldLHt9KQ0000 = (IConstructor) _read("prod(lit(\"else\"),[\\char-class([range(101,101)]),\\char-class([range(108,108)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChzdGFydChzb3J0KCJTdG10IikpLFtsYXlvdXRzKCJXUyIpLGxhYmVsKCJ0b3AiLHNvcnQoIlN0bXQiKSksbGF5b3V0cygiV1MiKV0se30p = (IConstructor) _read("prod(start(sort(\"Stmt\")),[layouts(\"WS\"),label(\"top\",sort(\"Stmt\")),layouts(\"WS\")],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChlbXB0eSgpLFtdLHt9KQ0000 = (IConstructor) _read("prod(empty(),[],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChzb3J0KCJTdG10IiksW2xpdCgiaWYiKSxsYXlvdXRzKCJXUyIpLGxpdCgiKCIpLGxheW91dHMoIldTIiksc29ydCgiRXhwciIpLGxheW91dHMoIldTIiksbGl0KCIpIiksbGF5b3V0cygiV1MiKSxzb3J0KCJTdG10IiksbGF5b3V0cygiV1MiKSxlbXB0eSgpXSx7fSk00 = (IConstructor) _read("prod(sort(\"Stmt\"),[lit(\"if\"),layouts(\"WS\"),lit(\"(\"),layouts(\"WS\"),sort(\"Expr\"),layouts(\"WS\"),lit(\")\"),layouts(\"WS\"),sort(\"Stmt\"),layouts(\"WS\"),empty()],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJTdG10IikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIlN0bXRcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIlN0bXQiKSkpfSk00 = (IConstructor) _read("prod(label(\"$MetaHole\",sort(\"Stmt\")),[\\char-class([range(0,0)]),lit(\"sort(\\\"Stmt\\\")\"),lit(\":\"),iter(\\char-class([range(48,57)])),\\char-class([range(0,0)])],{tag(\"holeType\"(sort(\"Stmt\")))})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIikiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQxLDQxKV0pXSx7fSk00 = (IConstructor) _read("prod(lit(\")\"),[\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor cHJvZChsaXQoIngiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDEyMCwxMjApXSldLHt9KQ0000 = (IConstructor) _read("prod(lit(\"x\"),[\\char-class([range(120,120)])],{})", RascalValueFactory.Production);
    
  // Item declarations
	
	
  protected static class layouts_$default$ {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(new IntegerMap());
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_cHJvZChsYXlvdXRzKCIkZGVmYXVsdCQiKSxbXSx7fSk00(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new EpsilonStackNode<IConstructor>(3, 0);
      builder.addAlternative(DoubleLeftNullable.cHJvZChsYXlvdXRzKCIkZGVmYXVsdCQiKSxbXSx7fSk00, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_cHJvZChsYXlvdXRzKCIkZGVmYXVsdCQiKSxbXSx7fSk00(builder);
      
    }
  }
	
  protected static class start__Stmt {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(new IntegerMap());
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_cHJvZChzdGFydChzb3J0KCJTdG10IikpLFtsYXlvdXRzKCJXUyIpLGxhYmVsKCJ0b3AiLHNvcnQoIlN0bXQiKSksbGF5b3V0cygiV1MiKV0se30p(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[2] = new NonTerminalStackNode<IConstructor>(17, 2, "layouts_WS", null, null);
      tmp[0] = new NonTerminalStackNode<IConstructor>(14, 0, "layouts_WS", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(15, 1, "Stmt", null, null);
      builder.addAlternative(DoubleLeftNullable.cHJvZChzdGFydChzb3J0KCJTdG10IikpLFtsYXlvdXRzKCJXUyIpLGxhYmVsKCJ0b3AiLHNvcnQoIlN0bXQiKSksbGF5b3V0cygiV1MiKV0se30p, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_cHJvZChzdGFydChzb3J0KCJTdG10IikpLFtsYXlvdXRzKCJXUyIpLGxhYmVsKCJ0b3AiLHNvcnQoIlN0bXQiKSksbGF5b3V0cygiV1MiKV0se30p(builder);
      
    }
  }
	
  protected static class Expr {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(new IntegerMap());
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_cHJvZChzb3J0KCJFeHByIiksW2xpdCgieCIpXSx7fSk00(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(34, 0, cHJvZChsaXQoIngiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDEyMCwxMjApXSldLHt9KQ0000, new int[] {120}, null, null);
      builder.addAlternative(DoubleLeftNullable.cHJvZChzb3J0KCJFeHByIiksW2xpdCgieCIpXSx7fSk00, tmp);
	}
    protected static final void _init_cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJFeHByIikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIkV4cHJcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIkV4cHIiKSkpfSk00(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[4] = new CharStackNode<IConstructor>(42, 4, new int[][]{{0,0}}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(39, 2, cHJvZChsaXQoIjoiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDU4LDU4KV0pXSx7fSk00, new int[] {58}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(37, 0, new int[][]{{0,0}}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(41, 3, cmVndWxhcihpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSkp, new CharStackNode<IConstructor>(40, 0, new int[][]{{48,57}}, null, null), true, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(38, 1, cHJvZChsaXQoInNvcnQoXCJFeHByXCIpIiksW1xjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTEsMTExKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDY5LDY5KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMjAsMTIwKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTIsMTEyKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgzNCwzNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSldLHt9KQ0000, new int[] {115,111,114,116,40,34,69,120,112,114,34,41}, null, null);
      builder.addAlternative(DoubleLeftNullable.cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJFeHByIikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIkV4cHJcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIkV4cHIiKSkpfSk00, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_cHJvZChzb3J0KCJFeHByIiksW2xpdCgieCIpXSx7fSk00(builder);
      
        _init_cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJFeHByIikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIkV4cHJcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIkV4cHIiKSkpfSk00(builder);
      
    }
  }
	
  protected static class Stmt {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(new IntegerMap());
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_cHJvZChzb3J0KCJTdG10IiksW2xpdCgieyIpLGxheW91dHMoIldTIiksbGl0KCJ9IildLHt9KQ0000(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[3];
      
      tmp[1] = new NonTerminalStackNode<IConstructor>(58, 1, "layouts_WS", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(59, 2, cHJvZChsaXQoIn0iKSxbXGNoYXItY2xhc3MoW3JhbmdlKDEyNSwxMjUpXSldLHt9KQ0000, new int[] {125}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(57, 0, cHJvZChsaXQoInsiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDEyMywxMjMpXSldLHt9KQ0000, new int[] {123}, null, null);
      builder.addAlternative(DoubleLeftNullable.cHJvZChzb3J0KCJTdG10IiksW2xpdCgieyIpLGxheW91dHMoIldTIiksbGl0KCJ9IildLHt9KQ0000, tmp);
	}
    protected static final void _init_cHJvZChzb3J0KCJTdG10IiksW2VtcHR5KCksbGF5b3V0cygiV1MiKSxsaXQoImlmIiksbGF5b3V0cygiV1MiKSxsaXQoIigiKSxsYXlvdXRzKCJXUyIpLHNvcnQoIkV4cHIiKSxsYXlvdXRzKCJXUyIpLGxpdCgiKSIpLGxheW91dHMoIldTIiksc29ydCgiU3RtdCIpLGxheW91dHMoIldTIiksbGl0KCJlbHNlIiksbGF5b3V0cygiV1MiKSxzb3J0KCJTdG10IildLHt9KQ0000(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[9] = new NonTerminalStackNode<IConstructor>(70, 9, "layouts_WS", null, null);
      tmp[14] = new NonTerminalStackNode<IConstructor>(75, 14, "Stmt", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(65, 4, cHJvZChsaXQoIigiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQwLDQwKV0pXSx7fSk00, new int[] {40}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(63, 2, cHJvZChsaXQoImlmIiksW1xjaGFyLWNsYXNzKFtyYW5nZSgxMDUsMTA1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMDIsMTAyKV0pXSx7fSk00, new int[] {105,102}, null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(69, 8, cHJvZChsaXQoIikiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQxLDQxKV0pXSx7fSk00, new int[] {41}, null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(71, 10, "Stmt", null, null);
      tmp[0] = new EmptyStackNode<IConstructor>(61, 0, cmVndWxhcihlbXB0eSgpKQ0000, null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(73, 12, cHJvZChsaXQoImVsc2UiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDEwMSwxMDEpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwOCwxMDgpXSksXGNoYXItY2xhc3MoW3JhbmdlKDExNSwxMTUpXSksXGNoYXItY2xhc3MoW3JhbmdlKDEwMSwxMDEpXSldLHt9KQ0000, new int[] {101,108,115,101}, null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(67, 6, "Expr", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(72, 11, "layouts_WS", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(64, 3, "layouts_WS", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(66, 5, "layouts_WS", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(62, 1, "layouts_WS", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(68, 7, "layouts_WS", null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(74, 13, "layouts_WS", null, null);
      builder.addAlternative(DoubleLeftNullable.cHJvZChzb3J0KCJTdG10IiksW2VtcHR5KCksbGF5b3V0cygiV1MiKSxsaXQoImlmIiksbGF5b3V0cygiV1MiKSxsaXQoIigiKSxsYXlvdXRzKCJXUyIpLHNvcnQoIkV4cHIiKSxsYXlvdXRzKCJXUyIpLGxpdCgiKSIpLGxheW91dHMoIldTIiksc29ydCgiU3RtdCIpLGxheW91dHMoIldTIiksbGl0KCJlbHNlIiksbGF5b3V0cygiV1MiKSxzb3J0KCJTdG10IildLHt9KQ0000, tmp);
	}
    protected static final void _init_cHJvZChzb3J0KCJTdG10IiksW2xpdCgiaWYiKSxsYXlvdXRzKCJXUyIpLGxpdCgiKCIpLGxheW91dHMoIldTIiksc29ydCgiRXhwciIpLGxheW91dHMoIldTIiksbGl0KCIpIiksbGF5b3V0cygiV1MiKSxzb3J0KCJTdG10IiksbGF5b3V0cygiV1MiKSxlbXB0eSgpXSx7fSk00(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[7] = new NonTerminalStackNode<IConstructor>(84, 7, "layouts_WS", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(79, 2, cHJvZChsaXQoIigiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQwLDQwKV0pXSx7fSk00, new int[] {40}, null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(83, 6, cHJvZChsaXQoIikiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQxLDQxKV0pXSx7fSk00, new int[] {41}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(81, 4, "Expr", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(86, 9, "layouts_WS", null, null);
      tmp[10] = new EmptyStackNode<IConstructor>(87, 10, cmVndWxhcihlbXB0eSgpKQ0000, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(77, 0, cHJvZChsaXQoImlmIiksW1xjaGFyLWNsYXNzKFtyYW5nZSgxMDUsMTA1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMDIsMTAyKV0pXSx7fSk00, new int[] {105,102}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(82, 5, "layouts_WS", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(78, 1, "layouts_WS", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(80, 3, "layouts_WS", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(85, 8, "Stmt", null, null);
      builder.addAlternative(DoubleLeftNullable.cHJvZChzb3J0KCJTdG10IiksW2xpdCgiaWYiKSxsYXlvdXRzKCJXUyIpLGxpdCgiKCIpLGxheW91dHMoIldTIiksc29ydCgiRXhwciIpLGxheW91dHMoIldTIiksbGl0KCIpIiksbGF5b3V0cygiV1MiKSxzb3J0KCJTdG10IiksbGF5b3V0cygiV1MiKSxlbXB0eSgpXSx7fSk00, tmp);
	}
    protected static final void _init_cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJTdG10IikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIlN0bXRcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIlN0bXQiKSkpfSk00(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new LiteralStackNode<IConstructor>(92, 2, cHJvZChsaXQoIjoiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDU4LDU4KV0pXSx7fSk00, new int[] {58}, null, null);
      tmp[4] = new CharStackNode<IConstructor>(95, 4, new int[][]{{0,0}}, null, null);
      tmp[0] = new CharStackNode<IConstructor>(90, 0, new int[][]{{0,0}}, null, null);
      tmp[1] = new LiteralStackNode<IConstructor>(91, 1, cHJvZChsaXQoInNvcnQoXCJTdG10XCIpIiksW1xjaGFyLWNsYXNzKFtyYW5nZSgxMTUsMTE1KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTEsMTExKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTQsMTE0KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSg0MCw0MCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoMzQsMzQpXSksXGNoYXItY2xhc3MoW3JhbmdlKDgzLDgzKV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMDksMTA5KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgxMTYsMTE2KV0pLFxjaGFyLWNsYXNzKFtyYW5nZSgzNCwzNCldKSxcY2hhci1jbGFzcyhbcmFuZ2UoNDEsNDEpXSldLHt9KQ0000, new int[] {115,111,114,116,40,34,83,116,109,116,34,41}, null, null);
      tmp[3] = new ListStackNode<IConstructor>(94, 3, cmVndWxhcihpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSkp, new CharStackNode<IConstructor>(93, 0, new int[][]{{48,57}}, null, null), true, null, null);
      builder.addAlternative(DoubleLeftNullable.cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJTdG10IikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIlN0bXRcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIlN0bXQiKSkpfSk00, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_cHJvZChzb3J0KCJTdG10IiksW2xpdCgieyIpLGxheW91dHMoIldTIiksbGl0KCJ9IildLHt9KQ0000(builder);
      
        _init_cHJvZChzb3J0KCJTdG10IiksW2VtcHR5KCksbGF5b3V0cygiV1MiKSxsaXQoImlmIiksbGF5b3V0cygiV1MiKSxsaXQoIigiKSxsYXlvdXRzKCJXUyIpLHNvcnQoIkV4cHIiKSxsYXlvdXRzKCJXUyIpLGxpdCgiKSIpLGxheW91dHMoIldTIiksc29ydCgiU3RtdCIpLGxheW91dHMoIldTIiksbGl0KCJlbHNlIiksbGF5b3V0cygiV1MiKSxzb3J0KCJTdG10IildLHt9KQ0000(builder);
      
        _init_cHJvZChzb3J0KCJTdG10IiksW2xpdCgiaWYiKSxsYXlvdXRzKCJXUyIpLGxpdCgiKCIpLGxheW91dHMoIldTIiksc29ydCgiRXhwciIpLGxheW91dHMoIldTIiksbGl0KCIpIiksbGF5b3V0cygiV1MiKSxzb3J0KCJTdG10IiksbGF5b3V0cygiV1MiKSxlbXB0eSgpXSx7fSk00(builder);
      
        _init_cHJvZChsYWJlbCgiJE1ldGFIb2xlIixzb3J0KCJTdG10IikpLFtcY2hhci1jbGFzcyhbcmFuZ2UoMCwwKV0pLGxpdCgic29ydChcIlN0bXRcIikiKSxsaXQoIjoiKSxpdGVyKFxjaGFyLWNsYXNzKFtyYW5nZSg0OCw1NyldKSksXGNoYXItY2xhc3MoW3JhbmdlKDAsMCldKV0se3RhZygiaG9sZVR5cGUiKHNvcnQoIlN0bXQiKSkpfSk00(builder);
      
    }
  }
	
  protected static class layouts_WS {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(new IntegerMap());
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_cHJvZChsYXlvdXRzKCJXUyIpLFtcaXRlci1zdGFyKFxjaGFyLWNsYXNzKFtyYW5nZSg5LDEwKSxyYW5nZSgxMywxMykscmFuZ2UoMzIsMzIpXSkpXSx7fSk00(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new ListStackNode<IConstructor>(129, 0, cmVndWxhcihcaXRlci1zdGFyKFxjaGFyLWNsYXNzKFtyYW5nZSg5LDEwKSxyYW5nZSgxMywxMykscmFuZ2UoMzIsMzIpXSkpKQ0000, new CharStackNode<IConstructor>(128, 0, new int[][]{{9,10},{13,13},{32,32}}, null, null), false, null, null);
      builder.addAlternative(DoubleLeftNullable.cHJvZChsYXlvdXRzKCJXUyIpLFtcaXRlci1zdGFyKFxjaGFyLWNsYXNzKFtyYW5nZSg5LDEwKSxyYW5nZSgxMywxMykscmFuZ2UoMzIsMzIpXSkpXSx7fSk00, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_cHJvZChsYXlvdXRzKCJXUyIpLFtcaXRlci1zdGFyKFxjaGFyLWNsYXNzKFtyYW5nZSg5LDEwKSxyYW5nZSgxMywxMykscmFuZ2UoMzIsMzIpXSkpXSx7fSk00(builder);
      
    }
  }
	
  // Parse methods    
  
  public AbstractStackNode<IConstructor>[] layouts_$default$() {
    return layouts_$default$.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] start__Stmt() {
    return start__Stmt.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Expr() {
    return Expr.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] Stmt() {
    return Stmt.EXPECTS;
  }
  public AbstractStackNode<IConstructor>[] layouts_WS() {
    return layouts_WS.EXPECTS;
  }

  @Override
  public ITree executeParser() {
    return parse("start__Stmt", null, "if (x)\n\tif (x)\n\t\t{}".toCharArray(), new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(false));
  }

  @Override
  public IValue getExpectedResult() throws IOException {
    String expectedInput = "appl(prod(start(sort(\"Stmt\")),[layouts(\"WS\"),label(\"top\",sort(\"Stmt\")),layouts(\"WS\")],{}),[appl(prod(layouts(\"WS\"),[\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],{}),[appl(regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))),[])]),appl(prod(sort(\"Stmt\"),[lit(\"if\"),layouts(\"WS\"),lit(\"(\"),layouts(\"WS\"),sort(\"Expr\"),layouts(\"WS\"),lit(\")\"),layouts(\"WS\"),sort(\"Stmt\"),layouts(\"WS\"),empty()],{}),[appl(prod(lit(\"if\"),[\\char-class([range(105,105)]),\\char-class([range(102,102)])],{}),[char(105),char(102)]),appl(prod(layouts(\"WS\"),[\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],{}),[appl(regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))),[char(32)])]),appl(prod(lit(\"(\"),[\\char-class([range(40,40)])],{}),[char(40)]),appl(prod(layouts(\"WS\"),[\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],{}),[appl(regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))),[])]),appl(prod(sort(\"Expr\"),[lit(\"x\")],{}),[appl(prod(lit(\"x\"),[\\char-class([range(120,120)])],{}),[char(120)])]),appl(prod(layouts(\"WS\"),[\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],{}),[appl(regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))),[])]),appl(prod(lit(\")\"),[\\char-class([range(41,41)])],{}),[char(41)]),appl(prod(layouts(\"WS\"),[\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],{}),[appl(regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))),[char(10),char(9)])]),appl(prod(sort(\"Stmt\"),[lit(\"if\"),layouts(\"WS\"),lit(\"(\"),layouts(\"WS\"),sort(\"Expr\"),layouts(\"WS\"),lit(\")\"),layouts(\"WS\"),sort(\"Stmt\"),layouts(\"WS\"),empty()],{}),[appl(prod(lit(\"if\"),[\\char-class([range(105,105)]),\\char-class([range(102,102)])],{}),[char(105),char(102)]),appl(prod(layouts(\"WS\"),[\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],{}),[appl(regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))),[char(32)])]),appl(prod(lit(\"(\"),[\\char-class([range(40,40)])],{}),[char(40)]),appl(prod(layouts(\"WS\"),[\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],{}),[appl(regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))),[])]),appl(prod(sort(\"Expr\"),[lit(\"x\")],{}),[appl(prod(lit(\"x\"),[\\char-class([range(120,120)])],{}),[char(120)])]),appl(prod(layouts(\"WS\"),[\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],{}),[appl(regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))),[])]),appl(prod(lit(\")\"),[\\char-class([range(41,41)])],{}),[char(41)]),appl(prod(layouts(\"WS\"),[\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],{}),[appl(regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))),[char(10),char(9),char(9)])]),appl(prod(sort(\"Stmt\"),[lit(\"{\"),layouts(\"WS\"),lit(\"}\")],{}),[appl(prod(lit(\"{\"),[\\char-class([range(123,123)])],{}),[char(123)]),appl(prod(layouts(\"WS\"),[\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],{}),[appl(regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))),[])]),appl(prod(lit(\"}\"),[\\char-class([range(125,125)])],{}),[char(125)])]),appl(prod(layouts(\"WS\"),[\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],{}),[appl(regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))),[])]),appl(regular(empty()),[])]),appl(prod(layouts(\"WS\"),[\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],{}),[appl(regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))),[])]),appl(regular(empty()),[])]),appl(prod(layouts(\"WS\"),[\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],{}),[appl(regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))),[])])])";
		return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
  }
}