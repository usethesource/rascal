package org.rascalmpl.test.parser;

import java.io.IOException;
import java.io.StringReader;

import org.rascalmpl.parser.gtd.preprocessing.ExpectBuilder;
import org.rascalmpl.parser.gtd.result.out.DefaultNodeFlattener;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.EmptyStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerList;
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

@SuppressWarnings("all")
public class DoubleRightNullableWithPrefixSharing extends org.rascalmpl.parser.gtd.SGTDBF<IConstructor, ITree, ISourceLocation> implements IParserTest {
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
 
  private static final IntegerKeyedHashMap<IntegerList> _dontNest = new IntegerKeyedHashMap<IntegerList>();
  private static final IntegerMap _resultStoreIdMappings = new IntegerMap();
	
  // Production declarations
	
  private static final IConstructor bangLit = (IConstructor) _read("prod(lit(\"!\"),[\\char-class([range(33,33)])],{})", RascalValueFactory.Production);
  private static final IConstructor empty = (IConstructor) _read("regular(empty())", RascalValueFactory.Production);
  private static final IConstructor Stmt_bang = (IConstructor) _read("prod(lex(\"Stmt\"),[lit(\"!\")],{})", RascalValueFactory.Production);
  private static final IConstructor Stmt_else = (IConstructor) _read("prod(lex(\"Stmt\"),[lit(\"i\"),lex(\"Stmt\"),empty(),lit(\"e\"),lex(\"Stmt\")],{})", RascalValueFactory.Production);
  private static final IConstructor lit_e = (IConstructor) _read("prod(lit(\"e\"),[\\char-class([range(101,101)])],{})", RascalValueFactory.Production);
  private static final IConstructor Stmt_if = (IConstructor) _read("prod(lex(\"Stmt\"),[lit(\"i\"),lex(\"Stmt\"),empty(),empty()],{})", RascalValueFactory.Production);
  private static final IConstructor prodEmpty = (IConstructor) _read("prod(empty(),[],{})", RascalValueFactory.Production);
  private static final IConstructor lit_i = (IConstructor) _read("prod(lit(\"i\"),[\\char-class([range(105,105)])],{})", RascalValueFactory.Production);
    
  // Item declarations
	
  protected static class Stmt {
    public final static AbstractStackNode<IConstructor>[] EXPECTS;
    static{
      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_dontNest, _resultStoreIdMappings);
      init(builder);
      EXPECTS = builder.buildExpectArray();
    }
    
    protected static final void _init_bang(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[1];
      
      tmp[0] = new LiteralStackNode<IConstructor>(41, 0, bangLit, new int[] {33}, null, null);
      builder.addAlternative(DoubleRightNullableWithPrefixSharing.Stmt_bang, tmp);
	}
    protected static final void _init_else(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[5];
      
      tmp[2] = new EmptyStackNode<IConstructor>(45, 2, empty, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(44, 1, "Stmt", null, null);
      tmp[3] = new LiteralStackNode<IConstructor>(46, 3, lit_e, new int[] {101}, null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(43, 0, lit_i, new int[] {105}, null, null);
      tmp[4] = new NonTerminalStackNode<IConstructor>(47, 4, "Stmt", null, null);
      builder.addAlternative(DoubleRightNullableWithPrefixSharing.Stmt_else, tmp);
	}
    protected static final void _init_if(ExpectBuilder<IConstructor> builder) {
      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[4];
      
      tmp[3] = new EmptyStackNode<IConstructor>(52, 3, empty, null, null);
      tmp[1] = new NonTerminalStackNode<IConstructor>(50, 1, "Stmt", null, null);
      tmp[0] = new LiteralStackNode<IConstructor>(49, 0, lit_i, new int[] {105}, null, null);
      tmp[2] = new EmptyStackNode<IConstructor>(51, 2, empty, null, null);
      builder.addAlternative(DoubleRightNullableWithPrefixSharing.Stmt_if, tmp);
	}
    public static void init(ExpectBuilder<IConstructor> builder){
      
        _init_bang(builder);
        _init_else(builder);
        _init_if(builder);
      
    }
  }
	
  // Parse methods    
  
  public AbstractStackNode<IConstructor>[] Stmt() {
    return Stmt.EXPECTS;
  }

  @Override
  public ITree executeParser() {
    return parse("Stmt", null, "ii!".toCharArray(), new DefaultNodeFlattener<IConstructor, ITree, ISourceLocation>(), new UPTRNodeFactory(false));
  }

  @Override
  public IValue getExpectedResult() throws IOException {
     String expectedInput = "appl(prod(lex(\"Stmt\"),[lit(\"i\"),lex(\"Stmt\"),empty(),empty()],{}),[appl(prod(lit(\"i\"),[\\char-class([range(105,105)])],{}),[char(105)]),appl(prod(lex(\"Stmt\"),[lit(\"i\"),lex(\"Stmt\"),empty(),empty()],{}),[appl(prod(lit(\"i\"),[\\char-class([range(105,105)])],{}),[char(105)]),appl(prod(lex(\"Stmt\"),[lit(\"!\")],{}),[appl(prod(lit(\"!\"),[\\char-class([range(33,33)])],{}),[char(33)])]),appl(regular(empty()),[]),appl(regular(empty()),[])]),appl(regular(empty()),[]),appl(regular(empty()),[])])";
		 return new StandardTextReader().read(ValueFactoryFactory.getValueFactory(), RascalValueFactory.uptr, RascalValueFactory.Tree, new StringReader(expectedInput));
  }

  /**
   * This test case triggers issue #1542; if the bug is not fixed than
   * a parse error occurs where there is none. 
   * 
   * * By removing the `else` rule, the bug dissappears even though it is not used by the input sentence
   * * by removing one `i` from the input (and thus one level of nesting) the bug dissappears
   */
  public static void main(String[] args){
		DoubleRightNullableWithPrefixSharing ce = new DoubleRightNullableWithPrefixSharing();
		IConstructor result = ce.executeParser();
		System.out.println(result);
	}
}
