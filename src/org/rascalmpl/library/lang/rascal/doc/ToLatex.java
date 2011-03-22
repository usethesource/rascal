
package org.rascalmpl.library.lang.rascal.doc;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.IMap;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.rascalmpl.parser.gtd.stack.*;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.IntegerMap;
import org.rascalmpl.values.uptr.Factory;
import org.rascalmpl.parser.ASTBuilder;
import org.rascalmpl.parser.IParserInfo;

public class ToLatex extends org.rascalmpl.parser.gtd.SGTDBF implements IParserInfo {
    
	protected static IValue _read(java.lang.String s, org.eclipse.imp.pdb.facts.type.Type type){
		try{
			return new StandardTextReader().read(vf, org.rascalmpl.values.uptr.Factory.uptr, type, new ByteArrayInputStream(s.getBytes()));
		}catch(FactTypeUseException e){
			throw new RuntimeException("unexpected exception in generated parser", e);  
		}catch(IOException e){
			throw new RuntimeException("unexpected exception in generated parser", e);  
		}
	}
	
	protected static final TypeFactory _tf = TypeFactory.getInstance();
	
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
    

    private static final IntegerMap _resultStoreIdMappings;
    private static final IntegerKeyedHashMap<IntegerList> _dontNest;
	private static final java.util.HashMap<IConstructor, org.rascalmpl.ast.LanguageAction> _languageActions;
	
	private static void _putDontNest(IntegerKeyedHashMap<IntegerList> result, int parentId, int childId) {
    	IntegerList donts = result.get(childId);
    	if(donts == null){
    		donts = new IntegerList();
    		result.put(childId, donts);
    	}
    	donts.add(parentId);
    }
    
    protected static void _putResultStoreIdMapping(IntegerMap result, int parentId, int resultStoreId){
       result.putUnsafe(parentId, resultStoreId);
    }
    
    protected int getResultStoreId(int parentId){
       return _resultStoreIdMappings.get(parentId);
    }
    
    protected static IntegerKeyedHashMap<IntegerList> _initDontNest() {
      IntegerKeyedHashMap<IntegerList> result = new IntegerKeyedHashMap<IntegerList>(); 
    
      for (IValue e : (IRelation) _read(_concat("{}"), _tf.relType(_tf.integerType(),_tf.integerType()))) {
        ITuple t = (ITuple) e;
        _putDontNest(result, ((IInteger) t.get(0)).intValue(), ((IInteger) t.get(1)).intValue());
      }
      
      return result;
    }
    
    protected static IntegerMap _initDontNestGroups() {
      IntegerMap result = new IntegerMap();
      int resultStoreId = result.size();
    
      for (IValue t : (IRelation) _read(_concat("{}"), _tf.relType(_tf.setType(_tf.integerType()),_tf.setType(_tf.integerType())))) {
        ++resultStoreId;

        ISet parentIds = (ISet) ((ITuple) t).get(1);
        for (IValue pid : parentIds) {
          _putResultStoreIdMapping(result, ((IInteger) pid).intValue(), resultStoreId);
        }
      }
      
      return result;
    }
    
    protected IntegerList getFilteredParents(int childId) {
		return _dontNest.get(childId);
	}
    
    public org.rascalmpl.ast.LanguageAction getAction(IConstructor prod) {
      return _languageActions.get(prod);
    }
    
    protected static java.util.HashMap<IConstructor, org.rascalmpl.ast.LanguageAction> _initLanguageActions() {
      java.util.HashMap<IConstructor, org.rascalmpl.ast.LanguageAction> result = new java.util.HashMap<IConstructor, org.rascalmpl.ast.LanguageAction>();
      ASTBuilder astBuilder = new ASTBuilder();
      IMap tmp = (IMap) _read(_concat("()"), _tf.mapType(Factory.Production, Factory.Tree));
      for (IValue key : tmp) {
        result.put((IConstructor) key, (org.rascalmpl.ast.LanguageAction) astBuilder.buildValue(tmp.get(key)));
      }
      
      return result;
    }
    
    // initialize priorities and actions    
    static {
      _languageActions = _initLanguageActions();
      _dontNest = _initDontNest();
      _resultStoreIdMappings = _initDontNestGroups();
    }
    
    // Production declarations
	
	private static final IConstructor prod___iter__char_class___range__0_91_range__93_122_range__124_124_range__126_65535_Content_attrs___lex = (IConstructor) _read("prod([iter(\\char-class([range(0,91),range(93,122),range(124,124),range(126,65535)]))],sort(\"Content\"),attrs([lex()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__92_92_char_class___range__92_92_char_class___range__8_8_char_class___range__101_101_char_class___range__103_103_char_class___range__105_105_char_class___range__110_110_char_class___range__123_123_char_class___range__114_114_char_class___range__97_97_char_class___range__115_115_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_char_class___range__125_125_lit___92_92_92_8_101_103_105_110_123_114_97_115_99_97_108_125_attrs___literal = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(92,92)]),\\char-class([range(92,92)]),\\char-class([range(8,8)]),\\char-class([range(101,101)]),\\char-class([range(103,103)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(123,123)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(125,125)])],lit(\"\\\\\\\\\\\\egin{rascal}\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__0_91_range__93_65535_Water_attrs___lex = (IConstructor) _read("prod([\\char-class([range(0,91),range(93,65535)])],sort(\"Water\"),attrs([lex()]))", Factory.Production);
	private static final IConstructor prod___Begin_iter__Content_End_Block_attrs___lex = (IConstructor) _read("prod([sort(\"Begin\"),iter(sort(\"Content\")),sort(\"End\")],sort(\"Block\"),attrs([lex()]))", Factory.Production);
	private static final IConstructor prod___lit___92_92_92_8_101_103_105_110_123_114_97_115_99_97_108_125_Content_no_attrs = (IConstructor) _read("prod([lit(\"\\\\\\\\\\\\egin{rascal}\")],sort(\"Content\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_lit___101_110_100_123_114_97_115_99_97_108_125_End_attrs___lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),lit(\"end{rascal}\")],sort(\"End\"),attrs([lex()]))", Factory.Production);
	private static final IConstructor prod___layouts_WS_Document_layouts_WS_start__Document_no_attrs = (IConstructor) _read("prod([layouts(\"WS\"),sort(\"Document\"),layouts(\"WS\")],start(sort(\"Document\")),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__98_98_char_class___range__101_101_char_class___range__103_103_char_class___range__105_105_char_class___range__110_110_char_class___range__123_123_char_class___range__114_114_char_class___range__97_97_char_class___range__115_115_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_char_class___range__125_125_lit___98_101_103_105_110_123_114_97_115_99_97_108_125_attrs___literal = (IConstructor) _read("prod([\\char-class([range(98,98)]),\\char-class([range(101,101)]),\\char-class([range(103,103)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(123,123)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(125,125)])],lit(\"begin{rascal}\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__125_125_lit___125_attrs___literal = (IConstructor) _read("prod([\\char-class([range(125,125)])],lit(\"}\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit___92_92_125_Content_no_attrs = (IConstructor) _read("prod([lit(\"\\\\\\\\}\")],sort(\"Content\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_char_class___range__123_123_char_class___range__114_114_char_class___range__97_97_char_class___range__115_115_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_char_class___range__125_125_lit___101_110_100_123_114_97_115_99_97_108_125_attrs___literal = (IConstructor) _read("prod([\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(100,100)]),\\char-class([range(123,123)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(125,125)])],lit(\"end{rascal}\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___IBegin_iter__Content_IEnd_Inline_attrs___lex = (IConstructor) _read("prod([sort(\"IBegin\"),iter(sort(\"Content\")),sort(\"IEnd\")],sort(\"Inline\"),attrs([lex()]))", Factory.Production);
	private static final IConstructor regular__iter__char_class___range__0_91_range__93_122_range__124_124_range__126_65535_no_attrs = (IConstructor) _read("regular(iter(\\char-class([range(0,91),range(93,122),range(124,124),range(126,65535)])),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___lit___125_IEnd_attrs___lex = (IConstructor) _read("prod([lit(\"}\")],sort(\"IEnd\"),attrs([lex()]))", Factory.Production);
	private static final IConstructor regular__iter__Content_no_attrs = (IConstructor) _read("regular(iter(sort(\"Content\")),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__123_123_range__125_125_Content_attrs___lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(123,123),range(125,125)])],sort(\"Content\"),attrs([lex()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__92_92_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_char_class___range__123_123_char_class___range__114_114_char_class___range__97_97_char_class___range__115_115_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_char_class___range__125_125_lit___92_92_101_110_100_123_114_97_115_99_97_108_125_attrs___literal = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(92,92)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(100,100)]),\\char-class([range(123,123)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(125,125)])],lit(\"\\\\\\\\end{rascal}\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit___92_92_105_114_97_115_99_97_108_123_Content_no_attrs = (IConstructor) _read("prod([lit(\"\\\\\\\\irascal{\")],sort(\"Content\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___Inline_Snippet_no_attrs = (IConstructor) _read("prod([sort(\"Inline\")],sort(\"Snippet\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___layouts_WS_no_attrs = (IConstructor) _read("prod([],layouts(\"WS\"),\\no-attrs())", Factory.Production);
	private static final IConstructor regular__iter_star_seps__Content__layouts_WS_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Content\"),[layouts(\"WS\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___Backslash_Content_no_attrs = (IConstructor) _read("prod([sort(\"Backslash\")],sort(\"Content\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___layouts_EMPTY_LAYOUT_no_attrs = (IConstructor) _read("prod([],layouts(\"EMPTY_LAYOUT\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__92_92_char_class___range__105_105_char_class___range__114_114_char_class___range__97_97_char_class___range__115_115_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_char_class___range__123_123_lit___92_92_105_114_97_115_99_97_108_123_attrs___literal = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(92,92)]),\\char-class([range(105,105)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(123,123)])],lit(\"\\\\\\\\irascal{\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__123_123_layouts_WS_iter_star_seps__Content__layouts_WS_layouts_WS_char_class___range__125_125_Content_no_attrs = (IConstructor) _read("prod([\\char-class([range(123,123)]),layouts(\"WS\"),\\iter-star-seps(sort(\"Content\"),[layouts(\"WS\")]),layouts(\"WS\"),\\char-class([range(125,125)])],sort(\"Content\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___Block_Snippet_no_attrs = (IConstructor) _read("prod([sort(\"Block\")],sort(\"Snippet\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__105_105_char_class___range__114_114_char_class___range__97_97_char_class___range__115_115_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_char_class___range__123_123_lit___105_114_97_115_99_97_108_123_attrs___literal = (IConstructor) _read("prod([\\char-class([range(105,105)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(123,123)])],lit(\"irascal{\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_Backslash_attrs___lex = (IConstructor) _read("prod([\\char-class([range(92,92)])],sort(\"Backslash\"),attrs([lex()]))", Factory.Production);
	private static final IConstructor prod___WBackslash_Water_attrs___lex = (IConstructor) _read("prod([sort(\"WBackslash\")],sort(\"Water\"),attrs([lex()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_WBackslash_no_attrs = (IConstructor) _read("prod([\\char-class([range(92,92)])],sort(\"WBackslash\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_lit___98_101_103_105_110_123_114_97_115_99_97_108_125_Begin_attrs___lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),lit(\"begin{rascal}\")],sort(\"Begin\"),attrs([lex()]))", Factory.Production);
	private static final IConstructor prod___Snippet_Chunk_no_attrs = (IConstructor) _read("prod([sort(\"Snippet\")],sort(\"Chunk\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_lit___105_114_97_115_99_97_108_123_IBegin_attrs___lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),lit(\"irascal{\")],sort(\"IBegin\"),attrs([lex()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__92_92_char_class___range__125_125_lit___92_92_125_attrs___literal = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(92,92)]),\\char-class([range(125,125)])],lit(\"\\\\\\\\}\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___iter_star_seps__Chunk__layouts_WS_Document_no_attrs = (IConstructor) _read("prod([\\iter-star-seps(sort(\"Chunk\"),[layouts(\"WS\")])],sort(\"Document\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___lit___92_92_101_110_100_123_114_97_115_99_97_108_125_Content_no_attrs = (IConstructor) _read("prod([lit(\"\\\\\\\\end{rascal}\")],sort(\"Content\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___Water_Chunk_no_attrs = (IConstructor) _read("prod([sort(\"Water\")],sort(\"Chunk\"),\\no-attrs())", Factory.Production);
	private static final IConstructor regular__iter_star_seps__Chunk__layouts_WS_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Chunk\"),[layouts(\"WS\")]),\\no-attrs())", Factory.Production);
    
	// Item declarations
	
	
	private static class layouts_WS {
		
		public final static AbstractStackNode[] prod___layouts_WS_no_attrs = new AbstractStackNode[1];
		static{
			prod___layouts_WS_no_attrs[0] = new EpsilonStackNode(-29, 0);
		}
	}
	
	private static class layouts_EMPTY_LAYOUT {
		
		public final static AbstractStackNode[] prod___layouts_EMPTY_LAYOUT_no_attrs = new AbstractStackNode[1];
		static{
			prod___layouts_EMPTY_LAYOUT_no_attrs[0] = new EpsilonStackNode(-28, 0);
		}
	}
	
	private static class IBegin {
		
		public final static AbstractStackNode[] prod___char_class___range__92_92_lit___105_114_97_115_99_97_108_123_IBegin_attrs___lex = new AbstractStackNode[2];
		static{
			prod___char_class___range__92_92_lit___105_114_97_115_99_97_108_123_IBegin_attrs___lex[1] = new LiteralStackNode(-31, 1, prod___char_class___range__105_105_char_class___range__114_114_char_class___range__97_97_char_class___range__115_115_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_char_class___range__123_123_lit___105_114_97_115_99_97_108_123_attrs___literal , new char[] {105,114,97,115,99,97,108,123});
			prod___char_class___range__92_92_lit___105_114_97_115_99_97_108_123_IBegin_attrs___lex[0] = new CharStackNode(-30, 0, new char[][]{{92,92}});
		}
	}
	
	private static class start__Document {
		
		public final static AbstractStackNode[] prod___layouts_WS_Document_layouts_WS_start__Document_no_attrs = new AbstractStackNode[3];
		static{
			prod___layouts_WS_Document_layouts_WS_start__Document_no_attrs[2] = new NonTerminalStackNode(-42, 2 , "layouts_WS");
			prod___layouts_WS_Document_layouts_WS_start__Document_no_attrs[1] = new NonTerminalStackNode(-41, 1 , "Document");
			prod___layouts_WS_Document_layouts_WS_start__Document_no_attrs[0] = new NonTerminalStackNode(-40, 0 , "layouts_WS");
		}
	}
	
	private static class Chunk {
		
		public final static AbstractStackNode[] prod___Snippet_Chunk_no_attrs = new AbstractStackNode[1];
		static{
			prod___Snippet_Chunk_no_attrs[0] = new NonTerminalStackNode(-38, 0 , "Snippet");
		}
		public final static AbstractStackNode[] prod___Water_Chunk_no_attrs = new AbstractStackNode[1];
		static{
			prod___Water_Chunk_no_attrs[0] = new NonTerminalStackNode(-39, 0 , "Water");
		}
	}
	
	private static class Block {
		
		public final static AbstractStackNode[] prod___Begin_iter__Content_End_Block_attrs___lex = new AbstractStackNode[3];
		static{
			prod___Begin_iter__Content_End_Block_attrs___lex[2] = new NonTerminalStackNode(-37, 2 , "End");
			prod___Begin_iter__Content_End_Block_attrs___lex[1] = new ListStackNode(-35, 1, regular__iter__Content_no_attrs , new NonTerminalStackNode(-36, 0 , "Content"), true);
			prod___Begin_iter__Content_End_Block_attrs___lex[0] = new NonTerminalStackNode(-34, 0 , "Begin");
		}
	}
	
	private static class Begin {
		
		public final static AbstractStackNode[] prod___char_class___range__92_92_lit___98_101_103_105_110_123_114_97_115_99_97_108_125_Begin_attrs___lex = new AbstractStackNode[2];
		static{
			prod___char_class___range__92_92_lit___98_101_103_105_110_123_114_97_115_99_97_108_125_Begin_attrs___lex[1] = new LiteralStackNode(-48, 1, prod___char_class___range__98_98_char_class___range__101_101_char_class___range__103_103_char_class___range__105_105_char_class___range__110_110_char_class___range__123_123_char_class___range__114_114_char_class___range__97_97_char_class___range__115_115_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_char_class___range__125_125_lit___98_101_103_105_110_123_114_97_115_99_97_108_125_attrs___literal , new char[] {98,101,103,105,110,123,114,97,115,99,97,108,125});
			prod___char_class___range__92_92_lit___98_101_103_105_110_123_114_97_115_99_97_108_125_Begin_attrs___lex[0] = new CharStackNode(-47, 0, new char[][]{{92,92}});
		}
	}
	
	private static class Backslash {
		
		public final static AbstractStackNode[] prod___char_class___range__92_92_Backslash_attrs___lex = new AbstractStackNode[1];
		static{
			prod___char_class___range__92_92_Backslash_attrs___lex[0] = new CharStackNode(-50, 0, new char[][]{{92,92}});
		}
	}
	
	private static class Water {
		
		public final static AbstractStackNode[] prod___char_class___range__0_91_range__93_65535_Water_attrs___lex = new AbstractStackNode[1];
		static{
			prod___char_class___range__0_91_range__93_65535_Water_attrs___lex[0] = new CharStackNode(-53, 0, new char[][]{{0,91},{93,65535}});
		}
		public final static AbstractStackNode[] prod___WBackslash_Water_attrs___lex = new AbstractStackNode[1];
		static{
			prod___WBackslash_Water_attrs___lex[0] = new NonTerminalStackNode(-54, 0 , new IMatchableStackNode[] {new LiteralStackNode(-55, 0, prod___char_class___range__105_105_char_class___range__114_114_char_class___range__97_97_char_class___range__115_115_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_char_class___range__123_123_lit___105_114_97_115_99_97_108_123_attrs___literal , new char[] {105,114,97,115,99,97,108,123}), new LiteralStackNode(-56, 0, prod___char_class___range__98_98_char_class___range__101_101_char_class___range__103_103_char_class___range__105_105_char_class___range__110_110_char_class___range__123_123_char_class___range__114_114_char_class___range__97_97_char_class___range__115_115_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_char_class___range__125_125_lit___98_101_103_105_110_123_114_97_115_99_97_108_125_attrs___literal , new char[] {98,101,103,105,110,123,114,97,115,99,97,108,125})}, "WBackslash");
		}
	}
	
	private static class IEnd {
		
		public final static AbstractStackNode[] prod___lit___125_IEnd_attrs___lex = new AbstractStackNode[1];
		static{
			prod___lit___125_IEnd_attrs___lex[0] = new LiteralStackNode(-70, 0, prod___char_class___range__125_125_lit___125_attrs___literal , new char[] {125});
		}
	}
	
	private static class WBackslash {
		
		public final static AbstractStackNode[] prod___char_class___range__92_92_WBackslash_no_attrs = new AbstractStackNode[1];
		static{
			prod___char_class___range__92_92_WBackslash_no_attrs[0] = new CharStackNode(-88, 0, new char[][]{{92,92}});
		}
	}
	
	private static class Snippet {
		
		public final static AbstractStackNode[] prod___Block_Snippet_no_attrs = new AbstractStackNode[1];
		static{
			prod___Block_Snippet_no_attrs[0] = new NonTerminalStackNode(-109, 0 , "Block");
		}
		public final static AbstractStackNode[] prod___Inline_Snippet_no_attrs = new AbstractStackNode[1];
		static{
			prod___Inline_Snippet_no_attrs[0] = new NonTerminalStackNode(-110, 0 , "Inline");
		}
	}
	
	private static class End {
		
		public final static AbstractStackNode[] prod___char_class___range__92_92_lit___101_110_100_123_114_97_115_99_97_108_125_End_attrs___lex = new AbstractStackNode[2];
		static{
			prod___char_class___range__92_92_lit___101_110_100_123_114_97_115_99_97_108_125_End_attrs___lex[1] = new LiteralStackNode(-115, 1, prod___char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_char_class___range__123_123_char_class___range__114_114_char_class___range__97_97_char_class___range__115_115_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_char_class___range__125_125_lit___101_110_100_123_114_97_115_99_97_108_125_attrs___literal , new char[] {101,110,100,123,114,97,115,99,97,108,125});
			prod___char_class___range__92_92_lit___101_110_100_123_114_97_115_99_97_108_125_End_attrs___lex[0] = new CharStackNode(-114, 0, new char[][]{{92,92}});
		}
	}
	
	private static class Document {
		
		public final static AbstractStackNode[] prod___iter_star_seps__Chunk__layouts_WS_Document_no_attrs = new AbstractStackNode[1];
		static{
			prod___iter_star_seps__Chunk__layouts_WS_Document_no_attrs[0] = new SeparatedListStackNode(-111, 0, regular__iter_star_seps__Chunk__layouts_WS_no_attrs , new NonTerminalStackNode(-112, 0 , "Chunk"), new AbstractStackNode[]{new NonTerminalStackNode(-113, 1 , "layouts_WS")}, false);
		}
	}
	
	private static class Inline {
		
		public final static AbstractStackNode[] prod___IBegin_iter__Content_IEnd_Inline_attrs___lex = new AbstractStackNode[3];
		static{
			prod___IBegin_iter__Content_IEnd_Inline_attrs___lex[2] = new NonTerminalStackNode(-139, 2 , "IEnd");
			prod___IBegin_iter__Content_IEnd_Inline_attrs___lex[1] = new ListStackNode(-137, 1, regular__iter__Content_no_attrs , new NonTerminalStackNode(-138, 0 , "Content"), true);
			prod___IBegin_iter__Content_IEnd_Inline_attrs___lex[0] = new NonTerminalStackNode(-136, 0 , "IBegin");
		}
	}
	
	private static class Content {
		
		public final static AbstractStackNode[] prod___char_class___range__123_123_layouts_WS_iter_star_seps__Content__layouts_WS_layouts_WS_char_class___range__125_125_Content_no_attrs = new AbstractStackNode[5];
		static{
			prod___char_class___range__123_123_layouts_WS_iter_star_seps__Content__layouts_WS_layouts_WS_char_class___range__125_125_Content_no_attrs[4] = new CharStackNode(-124, 4, new char[][]{{125,125}});
			prod___char_class___range__123_123_layouts_WS_iter_star_seps__Content__layouts_WS_layouts_WS_char_class___range__125_125_Content_no_attrs[3] = new NonTerminalStackNode(-123, 3 , "layouts_WS");
			prod___char_class___range__123_123_layouts_WS_iter_star_seps__Content__layouts_WS_layouts_WS_char_class___range__125_125_Content_no_attrs[2] = new SeparatedListStackNode(-120, 2, regular__iter_star_seps__Content__layouts_WS_no_attrs , new NonTerminalStackNode(-121, 0 , "Content"), new AbstractStackNode[]{new NonTerminalStackNode(-122, 1 , "layouts_WS")}, false);
			prod___char_class___range__123_123_layouts_WS_iter_star_seps__Content__layouts_WS_layouts_WS_char_class___range__125_125_Content_no_attrs[1] = new NonTerminalStackNode(-119, 1 , "layouts_WS");
			prod___char_class___range__123_123_layouts_WS_iter_star_seps__Content__layouts_WS_layouts_WS_char_class___range__125_125_Content_no_attrs[0] = new CharStackNode(-118, 0, new char[][]{{123,123}});
		}
		public final static AbstractStackNode[] prod___iter__char_class___range__0_91_range__93_122_range__124_124_range__126_65535_Content_attrs___lex = new AbstractStackNode[1];
		static{
			prod___iter__char_class___range__0_91_range__93_122_range__124_124_range__126_65535_Content_attrs___lex[0] = new ListStackNode(-125, 0, regular__iter__char_class___range__0_91_range__93_122_range__124_124_range__126_65535_no_attrs , new CharStackNode(-126, 0, new char[][]{{0,91},{93,122},{124,124},{126,65535}}), true);
		}
		public final static AbstractStackNode[] prod___char_class___range__92_92_char_class___range__123_123_range__125_125_Content_attrs___lex = new AbstractStackNode[2];
		static{
			prod___char_class___range__92_92_char_class___range__123_123_range__125_125_Content_attrs___lex[1] = new CharStackNode(-128, 1, new char[][]{{123,123},{125,125}});
			prod___char_class___range__92_92_char_class___range__123_123_range__125_125_Content_attrs___lex[0] = new CharStackNode(-127, 0, new char[][]{{92,92}});
		}
		public final static AbstractStackNode[] prod___lit___92_92_125_Content_no_attrs = new AbstractStackNode[1];
		static{
			prod___lit___92_92_125_Content_no_attrs[0] = new LiteralStackNode(-129, 0, prod___char_class___range__92_92_char_class___range__92_92_char_class___range__125_125_lit___92_92_125_attrs___literal , new char[] {92,92,125});
		}
		public final static AbstractStackNode[] prod___lit___92_92_92_8_101_103_105_110_123_114_97_115_99_97_108_125_Content_no_attrs = new AbstractStackNode[1];
		static{
			prod___lit___92_92_92_8_101_103_105_110_123_114_97_115_99_97_108_125_Content_no_attrs[0] = new LiteralStackNode(-130, 0, prod___char_class___range__92_92_char_class___range__92_92_char_class___range__92_92_char_class___range__8_8_char_class___range__101_101_char_class___range__103_103_char_class___range__105_105_char_class___range__110_110_char_class___range__123_123_char_class___range__114_114_char_class___range__97_97_char_class___range__115_115_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_char_class___range__125_125_lit___92_92_92_8_101_103_105_110_123_114_97_115_99_97_108_125_attrs___literal , new char[] {92,92,92,8,101,103,105,110,123,114,97,115,99,97,108,125});
		}
		public final static AbstractStackNode[] prod___lit___92_92_105_114_97_115_99_97_108_123_Content_no_attrs = new AbstractStackNode[1];
		static{
			prod___lit___92_92_105_114_97_115_99_97_108_123_Content_no_attrs[0] = new LiteralStackNode(-131, 0, prod___char_class___range__92_92_char_class___range__92_92_char_class___range__105_105_char_class___range__114_114_char_class___range__97_97_char_class___range__115_115_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_char_class___range__123_123_lit___92_92_105_114_97_115_99_97_108_123_attrs___literal , new char[] {92,92,105,114,97,115,99,97,108,123});
		}
		public final static AbstractStackNode[] prod___Backslash_Content_no_attrs = new AbstractStackNode[1];
		static{
			prod___Backslash_Content_no_attrs[0] = new NonTerminalStackNode(-132, 0 , new IMatchableStackNode[] {new LiteralStackNode(-133, 0, prod___char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_char_class___range__123_123_char_class___range__114_114_char_class___range__97_97_char_class___range__115_115_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_char_class___range__125_125_lit___101_110_100_123_114_97_115_99_97_108_125_attrs___literal , new char[] {101,110,100,123,114,97,115,99,97,108,125}), new CharStackNode(-134, 0, new char[][]{{123,123},{125,125}})}, "Backslash");
		}
		public final static AbstractStackNode[] prod___lit___92_92_101_110_100_123_114_97_115_99_97_108_125_Content_no_attrs = new AbstractStackNode[1];
		static{
			prod___lit___92_92_101_110_100_123_114_97_115_99_97_108_125_Content_no_attrs[0] = new LiteralStackNode(-135, 0, prod___char_class___range__92_92_char_class___range__92_92_char_class___range__101_101_char_class___range__110_110_char_class___range__100_100_char_class___range__123_123_char_class___range__114_114_char_class___range__97_97_char_class___range__115_115_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_char_class___range__125_125_lit___92_92_101_110_100_123_114_97_115_99_97_108_125_attrs___literal , new char[] {92,92,101,110,100,123,114,97,115,99,97,108,125});
		}
	}
	
	public ToLatex(){
		super();
	}
	
	// Parse methods    
	
      public void layouts_EMPTY_LAYOUT() {
            
            // prod([],layouts("EMPTY_LAYOUT"),\no-attrs())
	expect(prod___layouts_EMPTY_LAYOUT_no_attrs, layouts_EMPTY_LAYOUT.prod___layouts_EMPTY_LAYOUT_no_attrs);
                
          }
	
      public void layouts_WS() {
            
            // prod([],layouts("WS"),\no-attrs())
	expect(prod___layouts_WS_no_attrs, layouts_WS.prod___layouts_WS_no_attrs);
                
          }
	
      public void IBegin() {
            
            // prod([\char-class([range(92,92)]),lit("irascal{")],sort("IBegin"),attrs([lex()]))
	expect(prod___char_class___range__92_92_lit___105_114_97_115_99_97_108_123_IBegin_attrs___lex, IBegin.prod___char_class___range__92_92_lit___105_114_97_115_99_97_108_123_IBegin_attrs___lex);
                
          }
	
      public void Block() {
            
            // prod([sort("Begin"),iter(sort("Content")),sort("End")],sort("Block"),attrs([lex()]))
	expect(prod___Begin_iter__Content_End_Block_attrs___lex, Block.prod___Begin_iter__Content_End_Block_attrs___lex);
                
          }
	
      public void Chunk() {
            
            // prod([sort("Snippet")],sort("Chunk"),\no-attrs())
	expect(prod___Snippet_Chunk_no_attrs, Chunk.prod___Snippet_Chunk_no_attrs);
                // prod([sort("Water")],sort("Chunk"),\no-attrs())
	expect(prod___Water_Chunk_no_attrs, Chunk.prod___Water_Chunk_no_attrs);
                
          }
	
      public void start__Document() {
            
            // prod([layouts("WS"),sort("Document"),layouts("WS")],start(sort("Document")),\no-attrs())
	expect(prod___layouts_WS_Document_layouts_WS_start__Document_no_attrs, start__Document.prod___layouts_WS_Document_layouts_WS_start__Document_no_attrs);
                
          }
	
      public void Begin() {
            
            // prod([\char-class([range(92,92)]),lit("begin{rascal}")],sort("Begin"),attrs([lex()]))
	expect(prod___char_class___range__92_92_lit___98_101_103_105_110_123_114_97_115_99_97_108_125_Begin_attrs___lex, Begin.prod___char_class___range__92_92_lit___98_101_103_105_110_123_114_97_115_99_97_108_125_Begin_attrs___lex);
                
          }
	
      public void Backslash() {
            
            // prod([\char-class([range(92,92)])],sort("Backslash"),attrs([lex()]))
	expect(prod___char_class___range__92_92_Backslash_attrs___lex, Backslash.prod___char_class___range__92_92_Backslash_attrs___lex);
                
          }
	
      public void Water() {
            
            // prod([\char-class([range(0,91),range(93,65535)])],sort("Water"),attrs([lex()]))
	expect(prod___char_class___range__0_91_range__93_65535_Water_attrs___lex, Water.prod___char_class___range__0_91_range__93_65535_Water_attrs___lex);
                // prod([sort("WBackslash")],sort("Water"),attrs([lex()]))
	expect(prod___WBackslash_Water_attrs___lex, Water.prod___WBackslash_Water_attrs___lex);
                
          }
	
      public void IEnd() {
            
            // prod([lit("}")],sort("IEnd"),attrs([lex()]))
	expect(prod___lit___125_IEnd_attrs___lex, IEnd.prod___lit___125_IEnd_attrs___lex);
                
          }
	
      public void WBackslash() {
            
            // prod([\char-class([range(92,92)])],sort("WBackslash"),\no-attrs())
	expect(prod___char_class___range__92_92_WBackslash_no_attrs, WBackslash.prod___char_class___range__92_92_WBackslash_no_attrs);
                
          }
	
      public void Snippet() {
            
            // prod([sort("Block")],sort("Snippet"),\no-attrs())
	expect(prod___Block_Snippet_no_attrs, Snippet.prod___Block_Snippet_no_attrs);
                // prod([sort("Inline")],sort("Snippet"),\no-attrs())
	expect(prod___Inline_Snippet_no_attrs, Snippet.prod___Inline_Snippet_no_attrs);
                
          }
	
      public void Document() {
            
            // prod([\iter-star-seps(sort("Chunk"),[layouts("WS")])],sort("Document"),\no-attrs())
	expect(prod___iter_star_seps__Chunk__layouts_WS_Document_no_attrs, Document.prod___iter_star_seps__Chunk__layouts_WS_Document_no_attrs);
                
          }
	
      public void End() {
            
            // prod([\char-class([range(92,92)]),lit("end{rascal}")],sort("End"),attrs([lex()]))
	expect(prod___char_class___range__92_92_lit___101_110_100_123_114_97_115_99_97_108_125_End_attrs___lex, End.prod___char_class___range__92_92_lit___101_110_100_123_114_97_115_99_97_108_125_End_attrs___lex);
                
          }
	
      public void Content() {
            
            // prod([\char-class([range(123,123)]),layouts("WS"),\iter-star-seps(sort("Content"),[layouts("WS")]),layouts("WS"),\char-class([range(125,125)])],sort("Content"),\no-attrs())
	expect(prod___char_class___range__123_123_layouts_WS_iter_star_seps__Content__layouts_WS_layouts_WS_char_class___range__125_125_Content_no_attrs, Content.prod___char_class___range__123_123_layouts_WS_iter_star_seps__Content__layouts_WS_layouts_WS_char_class___range__125_125_Content_no_attrs);
                // prod([iter(\char-class([range(0,91),range(93,122),range(124,124),range(126,65535)]))],sort("Content"),attrs([lex()]))
	expect(prod___iter__char_class___range__0_91_range__93_122_range__124_124_range__126_65535_Content_attrs___lex, Content.prod___iter__char_class___range__0_91_range__93_122_range__124_124_range__126_65535_Content_attrs___lex);
                // prod([\char-class([range(92,92)]),\char-class([range(123,123),range(125,125)])],sort("Content"),attrs([lex()]))
	expect(prod___char_class___range__92_92_char_class___range__123_123_range__125_125_Content_attrs___lex, Content.prod___char_class___range__92_92_char_class___range__123_123_range__125_125_Content_attrs___lex);
                // prod([lit("\\\\}")],sort("Content"),\no-attrs())
	expect(prod___lit___92_92_125_Content_no_attrs, Content.prod___lit___92_92_125_Content_no_attrs);
                // prod([lit("\\\\\\egin{rascal}")],sort("Content"),\no-attrs())
	expect(prod___lit___92_92_92_8_101_103_105_110_123_114_97_115_99_97_108_125_Content_no_attrs, Content.prod___lit___92_92_92_8_101_103_105_110_123_114_97_115_99_97_108_125_Content_no_attrs);
                // prod([lit("\\\\irascal{")],sort("Content"),\no-attrs())
	expect(prod___lit___92_92_105_114_97_115_99_97_108_123_Content_no_attrs, Content.prod___lit___92_92_105_114_97_115_99_97_108_123_Content_no_attrs);
                // prod([sort("Backslash")],sort("Content"),\no-attrs())
	expect(prod___Backslash_Content_no_attrs, Content.prod___Backslash_Content_no_attrs);
                // prod([lit("\\\\end{rascal}")],sort("Content"),\no-attrs())
	expect(prod___lit___92_92_101_110_100_123_114_97_115_99_97_108_125_Content_no_attrs, Content.prod___lit___92_92_101_110_100_123_114_97_115_99_97_108_125_Content_no_attrs);
                
          }
	
      public void Inline() {
            
            // prod([sort("IBegin"),iter(sort("Content")),sort("IEnd")],sort("Inline"),attrs([lex()]))
	expect(prod___IBegin_iter__Content_IEnd_Inline_attrs___lex, Inline.prod___IBegin_iter__Content_IEnd_Inline_attrs___lex);
                
          }
	
}
