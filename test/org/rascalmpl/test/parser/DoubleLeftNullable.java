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
	
  protected static final TypeFactory _tf = TypeFactory.getInstance();
 
  // Production declarations
	
  private static final IConstructor layoutsWS = (IConstructor) _read("prod(layouts(\"WS\"),[\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],{})", RascalValueFactory.Production);
  private static final IConstructor empty = (IConstructor) _read("regular(empty())", RascalValueFactory.Production);
  private static final IConstructor bracketOpen = (IConstructor) _read("prod(lit(\"{\"),[\\char-class([range(123,123)])],{})", RascalValueFactory.Production);
  private static final IConstructor ifLit = (IConstructor) _read("prod(lit(\"if\"),[\\char-class([range(105,105)]),\\char-class([range(102,102)])],{})", RascalValueFactory.Production);
  private static final IConstructor bracketClose = (IConstructor) _read("prod(lit(\"}\"),[\\char-class([range(125,125)])],{})", RascalValueFactory.Production);
  private static final IConstructor braceOpen = (IConstructor) _read("prod(lit(\"(\"),[\\char-class([range(40,40)])],{})", RascalValueFactory.Production);
  private static final IConstructor ifThenElse = (IConstructor) _read("prod(sort(\"Stmt\"),[empty(),layouts(\"WS\"),lit(\"if\"),layouts(\"WS\"),lit(\"(\"),layouts(\"WS\"),sort(\"Expr\"),layouts(\"WS\"),lit(\")\"),layouts(\"WS\"),sort(\"Stmt\"),layouts(\"WS\"),lit(\"else\"),layouts(\"WS\"),sort(\"Stmt\")],{})", RascalValueFactory.Production);
  private static final IConstructor ExprX = (IConstructor) _read("prod(sort(\"Expr\"),[lit(\"x\")],{})", RascalValueFactory.Production);
  private static final IConstructor StatEmpty = (IConstructor) _read("prod(sort(\"Stmt\"),[lit(\"{\"),layouts(\"WS\"),lit(\"}\")],{})", RascalValueFactory.Production);
  private static final IConstructor SpacesList = (IConstructor) _read("regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)])))", RascalValueFactory.Production);
  private static final IConstructor defaultLayout = (IConstructor) _read("prod(layouts(\"$default$\"),[],{})", RascalValueFactory.Production);
  private static final IConstructor elseLit = (IConstructor) _read("prod(lit(\"else\"),[\\char-class([range(101,101)]),\\char-class([range(108,108)]),\\char-class([range(115,115)]),\\char-class([range(101,101)])],{})", RascalValueFactory.Production);
  private static final IConstructor startStat = (IConstructor) _read("prod(start(sort(\"Stmt\")),[layouts(\"WS\"),label(\"top\",sort(\"Stmt\")),layouts(\"WS\")],{})", RascalValueFactory.Production);
  private static final IConstructor ifThen = (IConstructor) _read("prod(sort(\"Stmt\"),[lit(\"if\"),layouts(\"WS\"),lit(\"(\"),layouts(\"WS\"),sort(\"Expr\"),layouts(\"WS\"),lit(\")\"),layouts(\"WS\"),sort(\"Stmt\"),layouts(\"WS\"),empty()],{})", RascalValueFactory.Production);
  private static final IConstructor braceClose = (IConstructor) _read("prod(lit(\")\"),[\\char-class([range(41,41)])],{})", RascalValueFactory.Production);
  private static final IConstructor litX = (IConstructor) _read("prod(lit(\"x\"),[\\char-class([range(120,120)])],{})", RascalValueFactory.Production);
    
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
      builder.addAlternative(DoubleLeftNullable.defaultLayout, tmp);
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
      builder.addAlternative(DoubleLeftNullable.startStat, tmp);
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
      
      tmp[0] = new LiteralStackNode<IConstructor>(34, 0, litX, new int[] {120}, null, null);
      builder.addAlternative(DoubleLeftNullable.ExprX, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
        _init_cHJvZChzb3J0KCJFeHByIiksW2xpdCgieCIpXSx7fSk00(builder);
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
      tmp[2] = new LiteralStackNode<IConstructor>(59, 2, bracketClose, new int[] {125}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(57, 0, bracketOpen, new int[] {123}, null, null);
      builder.addAlternative(DoubleLeftNullable.StatEmpty, tmp);
	}
    protected static final void _init_cHJvZChzb3J0KCJTdG10IiksW2VtcHR5KCksbGF5b3V0cygiV1MiKSxsaXQoImlmIiksbGF5b3V0cygiV1MiKSxsaXQoIigiKSxsYXlvdXRzKCJXUyIpLHNvcnQoIkV4cHIiKSxsYXlvdXRzKCJXUyIpLGxpdCgiKSIpLGxheW91dHMoIldTIiksc29ydCgiU3RtdCIpLGxheW91dHMoIldTIiksbGl0KCJlbHNlIiksbGF5b3V0cygiV1MiKSxzb3J0KCJTdG10IildLHt9KQ0000(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[15];
      
      tmp[9] = new NonTerminalStackNode<IConstructor>(70, 9, "layouts_WS", null, null);
      tmp[14] = new NonTerminalStackNode<IConstructor>(75, 14, "Stmt", null, null);
      tmp[4] = new LiteralStackNode<IConstructor>(65, 4, braceOpen, new int[] {40}, null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(63, 2, ifLit, new int[] {105,102}, null, null);
      tmp[8] = new LiteralStackNode<IConstructor>(69, 8, braceClose, new int[] {41}, null, null);
      tmp[10] = new NonTerminalStackNode<IConstructor>(71, 10, "Stmt", null, null);
      tmp[0] = new EmptyStackNode<IConstructor>(61, 0, empty, null, null);
      tmp[12] = new LiteralStackNode<IConstructor>(73, 12, elseLit, new int[] {101,108,115,101}, null, null);
      tmp[6] = new NonTerminalStackNode<IConstructor>(67, 6, "Expr", null, null);
      tmp[11] = new NonTerminalStackNode<IConstructor>(72, 11, "layouts_WS", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(64, 3, "layouts_WS", null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(66, 5, "layouts_WS", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(62, 1, "layouts_WS", null, null);
      tmp[7] = new NonTerminalStackNode<IConstructor>(68, 7, "layouts_WS", null, null);
      tmp[13] = new NonTerminalStackNode<IConstructor>(74, 13, "layouts_WS", null, null);
      builder.addAlternative(DoubleLeftNullable.ifThenElse, tmp);
	}
    protected static final void _init_cHJvZChzb3J0KCJTdG10IiksW2xpdCgiaWYiKSxsYXlvdXRzKCJXUyIpLGxpdCgiKCIpLGxheW91dHMoIldTIiksc29ydCgiRXhwciIpLGxheW91dHMoIldTIiksbGl0KCIpIiksbGF5b3V0cygiV1MiKSxzb3J0KCJTdG10IiksbGF5b3V0cygiV1MiKSxlbXB0eSgpXSx7fSk00(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[11];
      
      tmp[7] = new NonTerminalStackNode<IConstructor>(84, 7, "layouts_WS", null, null);
      tmp[2] = new LiteralStackNode<IConstructor>(79, 2, braceOpen, new int[] {40}, null, null);
      tmp[6] = new LiteralStackNode<IConstructor>(83, 6, braceClose, new int[] {41}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(81, 4, "Expr", null, null);
      tmp[9] = new NonTerminalStackNode<IConstructor>(86, 9, "layouts_WS", null, null);
      tmp[10] = new EmptyStackNode<IConstructor>(87, 10, empty, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(77, 0, ifLit, new int[] {105,102}, null, null);
      tmp[5] = new NonTerminalStackNode<IConstructor>(82, 5, "layouts_WS", null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(78, 1, "layouts_WS", null, null);
      tmp[3] = new NonTerminalStackNode<IConstructor>(80, 3, "layouts_WS", null, null);
      tmp[8] = new NonTerminalStackNode<IConstructor>(85, 8, "Stmt", null, null);
      builder.addAlternative(DoubleLeftNullable.ifThen, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_cHJvZChzb3J0KCJTdG10IiksW2xpdCgieyIpLGxheW91dHMoIldTIiksbGl0KCJ9IildLHt9KQ0000(builder);
      
        _init_cHJvZChzb3J0KCJTdG10IiksW2VtcHR5KCksbGF5b3V0cygiV1MiKSxsaXQoImlmIiksbGF5b3V0cygiV1MiKSxsaXQoIigiKSxsYXlvdXRzKCJXUyIpLHNvcnQoIkV4cHIiKSxsYXlvdXRzKCJXUyIpLGxpdCgiKSIpLGxheW91dHMoIldTIiksc29ydCgiU3RtdCIpLGxheW91dHMoIldTIiksbGl0KCJlbHNlIiksbGF5b3V0cygiV1MiKSxzb3J0KCJTdG10IildLHt9KQ0000(builder);
      
        _init_cHJvZChzb3J0KCJTdG10IiksW2xpdCgiaWYiKSxsYXlvdXRzKCJXUyIpLGxpdCgiKCIpLGxheW91dHMoIldTIiksc29ydCgiRXhwciIpLGxheW91dHMoIldTIiksbGl0KCIpIiksbGF5b3V0cygiV1MiKSxzb3J0KCJTdG10IiksbGF5b3V0cygiV1MiKSxlbXB0eSgpXSx7fSk00(builder);
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
      
      tmp[0] = new ListStackNode<IConstructor>(129, 0, SpacesList, new CharStackNode<IConstructor>(128, 0, new int[][]{{9,10},{13,13},{32,32}}, null, null), false, null, null);
      builder.addAlternative(DoubleLeftNullable.layoutsWS, tmp);
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