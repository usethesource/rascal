
package org.rascalmpl.library.lang.sdf2.syntax;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;

import org.eclipse.imp.pdb.facts.IConstructor;
import org.eclipse.imp.pdb.facts.IInteger;
import org.eclipse.imp.pdb.facts.IRelation;
import org.eclipse.imp.pdb.facts.ISet;
import org.eclipse.imp.pdb.facts.ITuple;
import org.eclipse.imp.pdb.facts.IValue;
import org.eclipse.imp.pdb.facts.exceptions.FactTypeUseException;
import org.eclipse.imp.pdb.facts.io.StandardTextReader;
import org.eclipse.imp.pdb.facts.type.TypeFactory;
import org.rascalmpl.parser.gtd.SGTDBF;
import org.rascalmpl.parser.gtd.result.action.VoidActionExecutor;
import org.rascalmpl.parser.gtd.stack.AbstractStackNode;
import org.rascalmpl.parser.gtd.stack.CharStackNode;
import org.rascalmpl.parser.gtd.stack.EpsilonStackNode;
import org.rascalmpl.parser.gtd.stack.IMatchableStackNode;
import org.rascalmpl.parser.gtd.stack.ListStackNode;
import org.rascalmpl.parser.gtd.stack.LiteralStackNode;
import org.rascalmpl.parser.gtd.stack.NonTerminalStackNode;
import org.rascalmpl.parser.gtd.stack.SeparatedListStackNode;
import org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;
import org.rascalmpl.parser.gtd.util.IntegerList;
import org.rascalmpl.parser.gtd.util.IntegerMap;
import org.rascalmpl.values.uptr.Factory;

public class SDF2 extends SGTDBF {
	protected static IValue _read(java.lang.String s, org.eclipse.imp.pdb.facts.type.Type type){
		try{
			return new StandardTextReader().read(VF, org.rascalmpl.values.uptr.Factory.uptr, type, new ByteArrayInputStream(s.getBytes()));
		}catch(FactTypeUseException e){
			throw new RuntimeException("unexpected exception in generated parser", e);  
		}catch(IOException e){
			throw new RuntimeException("unexpected exception in generated parser", e);  
		}
	}
	
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
	
	protected static final TypeFactory _tf = TypeFactory.getInstance();

    private static final IntegerMap _resultStoreIdMappings;
    private static final IntegerKeyedHashMap<IntegerList> _dontNest;
	
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
    
      for (IValue e : (IRelation) _read(_concat("{<2274,2280>,<914,1100>,<470,484>,<76,76>,<484,498>,<922,1114>,<1044,1100>,<916,1100>,<916,1114>,<1044,1114>,<922,1100>,<1500,1506>,<458,498>,<442,484>,<914,1114>,<2266,2272>,<908,1114>,<1100,1114>,<1038,1114>,<1088,1100>,<470,498>,<472,498>,<442,498>,<1038,1100>,<1088,1114>,<458,484>,<2196,2208>,<908,1100>,<442,470>}"), _tf.relType(_tf.integerType(),_tf.integerType()))) {
        ITuple t = (ITuple) e;
        _putDontNest(result, ((IInteger) t.get(0)).intValue(), ((IInteger) t.get(1)).intValue());
      }
      
      return result;
    }
    
    protected static IntegerMap _initDontNestGroups() {
      IntegerMap result = new IntegerMap();
      int resultStoreId = result.size();
    
      for (IValue t : (IRelation) _read(_concat("{<{76},{76}>,<{1114},{1100}>,<{1100,1114},{1038,908,922,916,1044,914,1088}>,<{470,484,498},{442}>,<{1506},{1500}>,<{484,498},{458,470}>,<{2208},{2196}>,<{2272},{2266}>,<{498},{472,484}>,<{2280},{2274}>}"), _tf.relType(_tf.setType(_tf.integerType()),_tf.setType(_tf.integerType())))) {
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
    
    // initialize priorities and actions    
    static {
      _dontNest = _initDontNest();
      _resultStoreIdMappings = _initDontNestGroups();
    }
    
    // Production declarations
	
	private static final IConstructor prod___lit_bracket_Attribute_attrs___term__cons_Bracket = (IConstructor) _read("prod([lit(\"bracket\")],sort(\"Attribute\"),attrs([term(cons(\"Bracket\"))]))", Factory.Production);
	private static final IConstructor regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_no_attrs = (IConstructor) _read("regular(\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(97,122)])),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___lit_lexical_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_LexicalRestrictions = (IConstructor) _read("prod([lit(\"lexical\"),layouts(\"LAYOUTLIST\"),lit(\"restrictions\"),layouts(\"LAYOUTLIST\"),sort(\"Restrictions\")],sort(\"Grammar\"),attrs([term(cons(\"LexicalRestrictions\"))]))", Factory.Production);
	private static final IConstructor prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Seq = (IConstructor) _read("prod([lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),\\iter-seps(sort(\"Symbol\"),[layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\")\")],sort(\"Symbol\"),attrs([term(cons(\"Seq\"))]))", Factory.Production);
	private static final IConstructor regular__iter_star_seps__Symbol__layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Symbol\"),[layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor regular__iter_star__char_class___range__0_9_range__11_65535_no_attrs = (IConstructor) _read("regular(\\iter-star(\\char-class([range(0,9),range(11,65535)])),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs = (IConstructor) _read("prod([sort(\"Label\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\")],sort(\"Symbol\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__66_66_char_class___range__79_79_char_class___range__84_84_lit___92_66_79_84_attrs___literal = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(66,66)]),\\char-class([range(79,79)]),\\char-class([range(84,84)])],lit(\"\\\\BOT\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt = (IConstructor) _read("prod([sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\"?\")],sort(\"Symbol\"),attrs([term(cons(\"Opt\"))]))", Factory.Production);
	private static final IConstructor prod___lit_assoc_Associativity_attrs___term__cons_Assoc = (IConstructor) _read("prod([lit(\"assoc\")],sort(\"Associativity\"),attrs([term(cons(\"Assoc\"))]))", Factory.Production);
	private static final IConstructor prod___OptCharRanges_attrs___term__cons_Absent = (IConstructor) _read("prod([],sort(\"OptCharRanges\"),attrs([term(cons(\"Absent\"))]))", Factory.Production);
	private static final IConstructor prod___ImpSection_Grammar_attrs___term__cons_ImpSection = (IConstructor) _read("prod([sort(\"ImpSection\")],sort(\"Grammar\"),attrs([term(cons(\"ImpSection\"))]))", Factory.Production);
	private static final IConstructor prod___ShortChar_Character_attrs___term__cons_short = (IConstructor) _read("prod([sort(\"ShortChar\")],sort(\"Character\"),attrs([term(cons(\"short\"))]))", Factory.Production);
	private static final IConstructor prod___lit___40_layouts_LAYOUTLIST_Lookaheads_layouts_LAYOUTLIST_lit___41_Lookaheads_attrs___term__cons_Bracket = (IConstructor) _read("prod([lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"Lookaheads\"),layouts(\"LAYOUTLIST\"),lit(\")\")],sort(\"Lookaheads\"),attrs([term(cons(\"Bracket\"))]))", Factory.Production);
	private static final IConstructor regular__iter_star__LAYOUT_no_attrs = (IConstructor) _read("regular(\\iter-star(sort(\"LAYOUT\")),\\no-attrs())", Factory.Production);
	private static final IConstructor regular__iter_star_seps__Restriction__layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Restriction\"),[layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_lit_variables_attrs___literal = (IConstructor) _read("prod([\\char-class([range(118,118)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(97,97)]),\\char-class([range(98,98)]),\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(115,115)])],lit(\"variables\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep = (IConstructor) _read("prod([lit(\"{\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"*\")],sort(\"Symbol\"),attrs([term(cons(\"IterStarSep\"))]))", Factory.Production);
	private static final IConstructor prod___iter_star_seps__Restriction__layouts_LAYOUTLIST_Restrictions_no_attrs = (IConstructor) _read("prod([\\iter-star-seps(sort(\"Restriction\"),[layouts(\"LAYOUTLIST\")])],sort(\"Restrictions\"),\\no-attrs())", Factory.Production);
	private static final IConstructor regular__iter_star_seps__ImpSection__layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"ImpSection\"),[layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___Associativity_Attribute_attrs___term__cons_Assoc = (IConstructor) _read("prod([sort(\"Associativity\")],sort(\"Attribute\"),attrs([term(cons(\"Assoc\"))]))", Factory.Production);
	private static final IConstructor prod___ATerm_ATermAttribute_attrs___term__cons_Default = (IConstructor) _read("prod([sort(\"ATerm\")],sort(\"ATermAttribute\"),attrs([term(cons(\"Default\"))]))", Factory.Production);
	private static final IConstructor prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Tuple = (IConstructor) _read("prod([lit(\"\\<\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\"),\\iter-seps(sort(\"Symbol\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],sort(\"Symbol\"),attrs([term(cons(\"Tuple\"))]))", Factory.Production);
	private static final IConstructor regular__iter_star__StrChar_no_attrs = (IConstructor) _read("regular(\\iter-star(sort(\"StrChar\")),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__9_10_range__13_13_range__32_32_LAYOUT_attrs___term__cons_Whitespace_lex = (IConstructor) _read("prod([\\char-class([range(9,10),range(13,13),range(32,32)])],sort(\"LAYOUT\"),attrs([term(cons(\"Whitespace\")),lex()]))", Factory.Production);
	private static final IConstructor prod___lit___123_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_AssocGroup = (IConstructor) _read("prod([lit(\"{\"),layouts(\"LAYOUTLIST\"),sort(\"Associativity\"),layouts(\"LAYOUTLIST\"),lit(\":\"),layouts(\"LAYOUTLIST\"),sort(\"Productions\"),layouts(\"LAYOUTLIST\"),lit(\"}\")],sort(\"Group\"),attrs([term(cons(\"AssocGroup\"))]))", Factory.Production);
	private static final IConstructor prod___iter_star__LAYOUT_layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("prod([\\iter-star(sort(\"LAYOUT\"))],layouts(\"LAYOUTLIST\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_lit_lexical_attrs___literal = (IConstructor) _read("prod([\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(105,105)]),\\char-class([range(99,99)]),\\char-class([range(97,97)]),\\char-class([range(108,108)])],lit(\"lexical\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor regular__iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Symbol\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor regular__iter_star_seps__Priority__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Priority\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___lit_syntax_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"syntax\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___lit___37_char_class___range__0_9_range__11_36_range__38_65535_lit___37_LAYOUT_attrs___term__cons_Nested_lex = (IConstructor) _read("prod([lit(\"%\"),\\char-class([range(0,9),range(11,36),range(38,65535)]),lit(\"%\")],sort(\"LAYOUT\"),attrs([term(cons(\"Nested\")),lex()]))", Factory.Production);
	private static final IConstructor prod___Character_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_Character_CharRange_attrs___term__cons_Range = (IConstructor) _read("prod([sort(\"Character\"),layouts(\"LAYOUTLIST\"),lit(\"-\"),layouts(\"LAYOUTLIST\"),sort(\"Character\")],sort(\"CharRange\"),attrs([term(cons(\"Range\"))]))", Factory.Production);
	private static final IConstructor prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__CF_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_CF = (IConstructor) _read("prod([lit(\"\\<\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\"-CF\"),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],sort(\"Symbol\"),attrs([term(cons(\"CF\"))]))", Factory.Production);
	private static final IConstructor prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_Attribute_attrs___term__cons_Id = (IConstructor) _read("prod([lit(\"id\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"ModuleName\"),layouts(\"LAYOUTLIST\"),lit(\")\")],sort(\"Attribute\"),attrs([term(cons(\"Id\"))]))", Factory.Production);
	private static final IConstructor prod___NumChar_Character_attrs___term__cons_Numeric = (IConstructor) _read("prod([sort(\"NumChar\")],sort(\"Character\"),attrs([term(cons(\"Numeric\"))]))", Factory.Production);
	private static final IConstructor prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar = (IConstructor) _read("prod([sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\"*\")],sort(\"Symbol\"),attrs([term(cons(\"IterStar\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__40_40_char_class___range__47_47_char_class___range__41_41_lit___40_47_41_attrs___literal = (IConstructor) _read("prod([\\char-class([range(40,40)]),\\char-class([range(47,47)]),\\char-class([range(41,41)])],lit(\"(/)\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___iter_star_seps__Module__layouts_LAYOUTLIST_Definition_no_attrs = (IConstructor) _read("prod([\\iter-star-seps(sort(\"Module\"),[layouts(\"LAYOUTLIST\")])],sort(\"Definition\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___lit___43_layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Positive = (IConstructor) _read("prod([lit(\"+\"),layouts(\"LAYOUTLIST\"),sort(\"NatCon\")],sort(\"IntCon\"),attrs([term(cons(\"Positive\"))]))", Factory.Production);
	private static final IConstructor prod___lit_context_free_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"context-free\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor regular__iter_seps__Group__layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-seps(sort(\"Group\"),[layouts(\"LAYOUTLIST\"),lit(\"\\>\"),layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_lit_bracket_attrs___literal = (IConstructor) _read("prod([\\char-class([range(98,98)]),\\char-class([range(114,114)]),\\char-class([range(97,97)]),\\char-class([range(99,99)]),\\char-class([range(107,107)]),\\char-class([range(101,101)]),\\char-class([range(116,116)])],lit(\"bracket\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit_prefer_ATermAttribute_attrs___reject = (IConstructor) _read("prod([lit(\"prefer\")],sort(\"ATermAttribute\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__104_104_char_class___range__105_105_char_class___range__100_100_char_class___range__100_100_char_class___range__101_101_char_class___range__110_110_char_class___range__115_115_lit_hiddens_attrs___literal = (IConstructor) _read("prod([\\char-class([range(104,104)]),\\char-class([range(105,105)]),\\char-class([range(100,100)]),\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(110,110)]),\\char-class([range(115,115)])],lit(\"hiddens\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__69_69_char_class___range__79_79_char_class___range__70_70_lit___92_69_79_70_attrs___literal = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(69,69)]),\\char-class([range(79,79)]),\\char-class([range(70,70)])],lit(\"\\\\EOF\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___IdCon_FunctionName_attrs___term__cons_UnquotedFun = (IConstructor) _read("prod([sort(\"IdCon\")],sort(\"FunctionName\"),attrs([term(cons(\"UnquotedFun\"))]))", Factory.Production);
	private static final IConstructor prod___lit_imports_layouts_LAYOUTLIST_Imports_ImpSection_attrs___term__cons_Imports = (IConstructor) _read("prod([lit(\"imports\"),layouts(\"LAYOUTLIST\"),sort(\"Imports\")],sort(\"ImpSection\"),attrs([term(cons(\"Imports\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__39_39_SingleQuotedStrChar_char_class___range__39_39_SingleQuotedStrCon_attrs___term__cons_Default_lex = (IConstructor) _read("prod([\\char-class([range(39,39)]),sort(\"SingleQuotedStrChar\"),\\char-class([range(39,39)])],sort(\"SingleQuotedStrCon\"),attrs([term(cons(\"Default\")),lex()]))", Factory.Production);
	private static final IConstructor prod___CharClass_Symbol_attrs___term__cons_CharClass = (IConstructor) _read("prod([sort(\"CharClass\")],sort(\"Symbol\"),attrs([term(cons(\"CharClass\"))]))", Factory.Production);
	private static final IConstructor prod___Group_layouts_LAYOUTLIST_lit___46_Group_attrs___term__cons_NonTransitive_assoc__non_assoc = (IConstructor) _read("prod([sort(\"Group\"),layouts(\"LAYOUTLIST\"),lit(\".\")],sort(\"Group\"),attrs([term(cons(\"NonTransitive\")),assoc(\\non-assoc())]))", Factory.Production);
	private static final IConstructor prod___lit_hiddens_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Hiddens = (IConstructor) _read("prod([lit(\"hiddens\"),layouts(\"LAYOUTLIST\"),sort(\"Grammar\")],sort(\"Section\"),attrs([term(cons(\"Hiddens\"))]))", Factory.Production);
	private static final IConstructor prod___lit___47_ModuleId_ModuleId_attrs___term__cons_Root_lex = (IConstructor) _read("prod([lit(\"/\"),sort(\"ModuleId\")],sort(\"ModuleId\"),attrs([term(cons(\"Root\")),lex()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__60_60_char_class___range__83_83_char_class___range__84_84_char_class___range__65_65_char_class___range__82_82_char_class___range__84_84_char_class___range__62_62_lit___60_83_84_65_82_84_62_attrs___literal = (IConstructor) _read("prod([\\char-class([range(60,60)]),\\char-class([range(83,83)]),\\char-class([range(84,84)]),\\char-class([range(65,65)]),\\char-class([range(82,82)]),\\char-class([range(84,84)]),\\char-class([range(62,62)])],lit(\"\\<START\\>\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___SingleQuotedStrCon_Symbol_attrs___term__cons_CILit = (IConstructor) _read("prod([sort(\"SingleQuotedStrCon\")],sort(\"Symbol\"),attrs([term(cons(\"CILit\"))]))", Factory.Production);
	private static final IConstructor regular__iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-seps(sort(\"ATerm\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor regular__iter_star__char_class___range__9_10_range__13_13_range__32_32_no_attrs = (IConstructor) _read("regular(\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)])),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___ModuleName_layouts_LAYOUTLIST_Renamings_Import_attrs___term__cons_RenamedModule = (IConstructor) _read("prod([sort(\"ModuleName\"),layouts(\"LAYOUTLIST\"),sort(\"Renamings\")],sort(\"Import\"),attrs([term(cons(\"RenamedModule\"))]))", Factory.Production);
	private static final IConstructor regular__iter_star_seps__Import__layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__84_84_char_class___range__79_79_char_class___range__80_80_lit___92_84_79_80_attrs___literal = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(84,84)]),\\char-class([range(79,79)]),\\char-class([range(80,80)])],lit(\"\\\\TOP\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__111_111_char_class___range__114_114_char_class___range__105_105_char_class___range__116_116_char_class___range__105_105_char_class___range__101_101_char_class___range__115_115_lit_priorities_attrs___literal = (IConstructor) _read("prod([\\char-class([range(112,112)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(101,101)]),\\char-class([range(115,115)])],lit(\"priorities\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__76_76_char_class___range__65_65_char_class___range__89_89_char_class___range__79_79_char_class___range__85_85_char_class___range__84_84_lit_LAYOUT_attrs___literal = (IConstructor) _read("prod([\\char-class([range(76,76)]),\\char-class([range(65,65)]),\\char-class([range(89,89)]),\\char-class([range(79,79)]),\\char-class([range(85,85)]),\\char-class([range(84,84)])],lit(\"LAYOUT\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___IdCon_Label_attrs___term__cons_IdCon = (IConstructor) _read("prod([sort(\"IdCon\")],sort(\"Label\"),attrs([term(cons(\"IdCon\"))]))", Factory.Production);
	private static final IConstructor prod___CharRanges_layouts_LAYOUTLIST_CharRanges_CharRanges_attrs___term__cons_Conc_assoc__right = (IConstructor) _read("prod([sort(\"CharRanges\"),layouts(\"LAYOUTLIST\"),sort(\"CharRanges\")],sort(\"CharRanges\"),attrs([term(cons(\"Conc\")),assoc(right())]))", Factory.Production);
	private static final IConstructor prod___char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_char_class___range__115_115_lit_aliases_attrs___literal = (IConstructor) _read("prod([\\char-class([range(97,97)]),\\char-class([range(108,108)]),\\char-class([range(105,105)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(101,101)]),\\char-class([range(115,115)])],lit(\"aliases\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___EMPTY_no_attrs = (IConstructor) _read("prod([],sort(\"EMPTY\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___IntCon_ATerm_attrs___term__cons_Int = (IConstructor) _read("prod([sort(\"IntCon\")],sort(\"ATerm\"),attrs([term(cons(\"Int\"))]))", Factory.Production);
	private static final IConstructor prod___StrCon_Symbol_attrs___term__cons_Lit = (IConstructor) _read("prod([sort(\"StrCon\")],sort(\"Symbol\"),attrs([term(cons(\"Lit\"))]))", Factory.Production);
	private static final IConstructor prod___StrCon_FunctionName_attrs___term__cons_QuotedFun = (IConstructor) _read("prod([sort(\"StrCon\")],sort(\"FunctionName\"),attrs([term(cons(\"QuotedFun\"))]))", Factory.Production);
	private static final IConstructor prod___lit_prefer_Attribute_attrs___term__cons_Prefer = (IConstructor) _read("prod([lit(\"prefer\")],sort(\"Attribute\"),attrs([term(cons(\"Prefer\"))]))", Factory.Production);
	private static final IConstructor prod___iter_star_seps__Section__layouts_LAYOUTLIST_Sections_no_attrs = (IConstructor) _read("prod([\\iter-star-seps(sort(\"Section\"),[layouts(\"LAYOUTLIST\")])],sort(\"Sections\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__76_76_char_class___range__65_65_char_class___range__66_66_char_class___range__69_69_char_class___range__76_76_char_class___range__95_95_char_class___range__83_83_char_class___range__84_84_char_class___range__65_65_char_class___range__82_82_char_class___range__84_84_lit___92_76_65_66_69_76_95_83_84_65_82_84_attrs___literal = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(76,76)]),\\char-class([range(65,65)]),\\char-class([range(66,66)]),\\char-class([range(69,69)]),\\char-class([range(76,76)]),\\char-class([range(95,95)]),\\char-class([range(83,83)]),\\char-class([range(84,84)]),\\char-class([range(65,65)]),\\char-class([range(82,82)]),\\char-class([range(84,84)])],lit(\"\\\\LABEL_START\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_Alias_attrs___term__cons_Alias = (IConstructor) _read("prod([sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\"-\\>\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\")],sort(\"Alias\"),attrs([term(cons(\"Alias\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_iter__char_class___range__48_57_NumChar_attrs___term__cons_Digits_lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),iter(\\char-class([range(48,57)]))],sort(\"NumChar\"),attrs([term(cons(\"Digits\")),lex()]))", Factory.Production);
	private static final IConstructor prod___lit_exports_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"exports\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___lit___40_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Empty = (IConstructor) _read("prod([lit(\"(\"),layouts(\"LAYOUTLIST\"),lit(\")\")],sort(\"Symbol\"),attrs([term(cons(\"Empty\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__114_114_char_class___range__101_101_char_class___range__106_106_char_class___range__101_101_char_class___range__99_99_char_class___range__116_116_lit_reject_attrs___literal = (IConstructor) _read("prod([\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(106,106)]),\\char-class([range(101,101)]),\\char-class([range(99,99)]),\\char-class([range(116,116)])],lit(\"reject\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep = (IConstructor) _read("prod([lit(\"{\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\"}\"),layouts(\"LAYOUTLIST\"),lit(\"+\")],sort(\"Symbol\"),attrs([term(cons(\"IterSep\"))]))", Factory.Production);
	private static final IConstructor prod___RealCon_ATerm_attrs___term__cons_Real = (IConstructor) _read("prod([sort(\"RealCon\")],sort(\"ATerm\"),attrs([term(cons(\"Real\"))]))", Factory.Production);
	private static final IConstructor prod___ModuleId_ModuleName_attrs___term__cons_Unparameterized = (IConstructor) _read("prod([sort(\"ModuleId\")],sort(\"ModuleName\"),attrs([term(cons(\"Unparameterized\"))]))", Factory.Production);
	private static final IConstructor prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right = (IConstructor) _read("prod([sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\")],sort(\"Symbol\"),attrs([term(cons(\"Alt\")),assoc(right())]))", Factory.Production);
	private static final IConstructor prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__VAR_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Varsym = (IConstructor) _read("prod([lit(\"\\<\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\"-VAR\"),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],sort(\"Symbol\"),attrs([term(cons(\"Varsym\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__45_45_char_class___range__86_86_char_class___range__65_65_char_class___range__82_82_lit__VAR_attrs___literal = (IConstructor) _read("prod([\\char-class([range(45,45)]),\\char-class([range(86,86)]),\\char-class([range(65,65)]),\\char-class([range(82,82)])],lit(\"-VAR\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___CharClass_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Diff = (IConstructor) _read("prod([sort(\"CharClass\"),layouts(\"LAYOUTLIST\"),lit(\"/\"),layouts(\"LAYOUTLIST\"),sort(\"CharClass\")],sort(\"CharClass\"),attrs([term(cons(\"Diff\"))]))", Factory.Production);
	private static final IConstructor prod___iter_seps__Group__layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_Priority_attrs___term__cons_Chain = (IConstructor) _read("prod([\\iter-seps(sort(\"Group\"),[layouts(\"LAYOUTLIST\"),lit(\"\\>\"),layouts(\"LAYOUTLIST\")])],sort(\"Priority\"),attrs([term(cons(\"Chain\"))]))", Factory.Production);
	private static final IConstructor prod___CharClass_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_Lookaheads_Lookahead_attrs___term__cons_Seq = (IConstructor) _read("prod([sort(\"CharClass\"),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),sort(\"Lookaheads\")],sort(\"Lookahead\"),attrs([term(cons(\"Seq\"))]))", Factory.Production);
	private static final IConstructor prod___lit_non_assoc_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"non-assoc\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___Symbols_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_Prod = (IConstructor) _read("prod([sort(\"Symbols\"),layouts(\"LAYOUTLIST\"),lit(\"-\\>\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),sort(\"Attributes\")],sort(\"Production\"),attrs([term(cons(\"Prod\"))]))", Factory.Production);
	private static final IConstructor prod___lit___40_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Func = (IConstructor) _read("prod([lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"Symbols\"),layouts(\"LAYOUTLIST\"),lit(\"=\\>\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\")\")],sort(\"Symbol\"),attrs([term(cons(\"Func\"))]))", Factory.Production);
	private static final IConstructor prod___ModuleWord_lit___47_ModuleId_ModuleId_attrs___term__cons_Path_lex = (IConstructor) _read("prod([sort(\"ModuleWord\"),lit(\"/\"),sort(\"ModuleId\")],sort(\"ModuleId\"),attrs([term(cons(\"Path\")),lex()]))", Factory.Production);
	private static final IConstructor prod___lit___37_37_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10_LAYOUT_attrs___term__cons_Line_lex = (IConstructor) _read("prod([lit(\"%%\"),\\iter-star(\\char-class([range(0,9),range(11,65535)])),\\char-class([range(10,10)])],sort(\"LAYOUT\"),attrs([term(cons(\"Line\")),lex()]))", Factory.Production);
	private static final IConstructor prod___lit_imports_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"imports\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___AFun_ATerm_attrs___term__cons_Fun = (IConstructor) _read("prod([sort(\"AFun\")],sort(\"ATerm\"),attrs([term(cons(\"Fun\"))]))", Factory.Production);
	private static final IConstructor prod___NatCon_IntCon_attrs___term__cons_Natural = (IConstructor) _read("prod([sort(\"NatCon\")],sort(\"IntCon\"),attrs([term(cons(\"Natural\"))]))", Factory.Production);
	private static final IConstructor prod___lit_lexical_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"lexical\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___lit_module_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_iter_star_seps__ImpSection__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Sections_Module_attrs___term__cons_Module = (IConstructor) _read("prod([lit(\"module\"),layouts(\"LAYOUTLIST\"),sort(\"ModuleName\"),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"ImpSection\"),[layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),sort(\"Sections\")],sort(\"Module\"),attrs([term(cons(\"Module\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__65_90_Sort_attrs___term__cons_OneChar_lex = (IConstructor) _read("prod([\\char-class([range(65,90)])],sort(\"Sort\"),attrs([term(cons(\"OneChar\")),lex()]))", Factory.Production);
	private static final IConstructor prod___Lookahead_Lookaheads_attrs___term__cons_Single = (IConstructor) _read("prod([sort(\"Lookahead\")],sort(\"Lookaheads\"),attrs([term(cons(\"Single\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__105_105_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_lit_definition_attrs___literal = (IConstructor) _read("prod([\\char-class([range(100,100)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(105,105)]),\\char-class([range(110,110)]),\\char-class([range(105,105)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)])],lit(\"definition\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___AFun_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ATerm_attrs___term__cons_Appl = (IConstructor) _read("prod([sort(\"AFun\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),\\iter-seps(sort(\"ATerm\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\")\")],sort(\"ATerm\"),attrs([term(cons(\"Appl\"))]))", Factory.Production);
	private static final IConstructor prod___Symbol_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_Renaming_attrs___term__cons_Symbol = (IConstructor) _read("prod([sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\"=\\>\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\")],sort(\"Renaming\"),attrs([term(cons(\"Symbol\"))]))", Factory.Production);
	private static final IConstructor prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_ATermAttribute_attrs___reject = (IConstructor) _read("prod([lit(\"id\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"ModuleName\"),layouts(\"LAYOUTLIST\"),lit(\")\")],sort(\"ATermAttribute\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___Associativity_ATermAttribute_attrs___reject = (IConstructor) _read("prod([sort(\"Associativity\")],sort(\"ATermAttribute\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___lit___123_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Annotation_attrs___term__cons_Default = (IConstructor) _read("prod([lit(\"{\"),layouts(\"LAYOUTLIST\"),\\iter-seps(sort(\"ATerm\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"}\")],sort(\"Annotation\"),attrs([term(cons(\"Default\"))]))", Factory.Production);
	private static final IConstructor prod___lit_variables_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"variables\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___lit_bracket_ATermAttribute_attrs___reject = (IConstructor) _read("prod([lit(\"bracket\")],sort(\"ATermAttribute\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___lit_context_free_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_ContextFreeSyntax = (IConstructor) _read("prod([lit(\"context-free\"),layouts(\"LAYOUTLIST\"),lit(\"syntax\"),layouts(\"LAYOUTLIST\"),sort(\"Productions\")],sort(\"Grammar\"),attrs([term(cons(\"ContextFreeSyntax\"))]))", Factory.Production);
	private static final IConstructor prod___CharClass_layouts_LAYOUTLIST_lit___47_92_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_ISect = (IConstructor) _read("prod([sort(\"CharClass\"),layouts(\"LAYOUTLIST\"),lit(\"/\\\\\"),layouts(\"LAYOUTLIST\"),sort(\"CharClass\")],sort(\"CharClass\"),attrs([term(cons(\"ISect\"))]))", Factory.Production);
	private static final IConstructor prod___StrCon_Label_attrs___term__cons_Quoted = (IConstructor) _read("prod([sort(\"StrCon\")],sort(\"Label\"),attrs([term(cons(\"Quoted\"))]))", Factory.Production);
	private static final IConstructor prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__Renaming__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_Renamings_attrs___term__cons_Renamings = (IConstructor) _read("prod([lit(\"[\"),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"Renaming\"),[layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"]\")],sort(\"Renamings\"),attrs([term(cons(\"Renamings\"))]))", Factory.Production);
	private static final IConstructor prod___lit_priorities_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"priorities\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___ATerm_layouts_LAYOUTLIST_Annotation_ATerm_attrs___term__cons_Annotated = (IConstructor) _read("prod([sort(\"ATerm\"),layouts(\"LAYOUTLIST\"),sort(\"Annotation\")],sort(\"ATerm\"),attrs([term(cons(\"Annotated\"))]))", Factory.Production);
	private static final IConstructor prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter = (IConstructor) _read("prod([sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\"+\")],sort(\"Symbol\"),attrs([term(cons(\"Iter\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__65_90_range__97_122_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_IdCon_attrs___term__cons_Default_lex = (IConstructor) _read("prod([\\char-class([range(65,90),range(97,122)]),\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(97,122)]))],sort(\"IdCon\"),attrs([term(cons(\"Default\")),lex()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_lit_left_attrs___literal = (IConstructor) _read("prod([\\char-class([range(108,108)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(116,116)])],lit(\"left\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___iter_star__char_class___range__9_10_range__13_13_range__32_32_layouts_$QUOTES_attrs___term__lex = (IConstructor) _read("prod([\\iter-star(\\char-class([range(9,10),range(13,13),range(32,32)]))],layouts(\"$QUOTES\"),attrs([term(lex())]))", Factory.Production);
	private static final IConstructor prod___char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__115_115_lit_sorts_attrs___literal = (IConstructor) _read("prod([\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(115,115)])],lit(\"sorts\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit_reject_Attribute_attrs___term__cons_Reject = (IConstructor) _read("prod([lit(\"reject\")],sort(\"Attribute\"),attrs([term(cons(\"Reject\"))]))", Factory.Production);
	private static final IConstructor prod___iter_star_seps__Production__layouts_LAYOUTLIST_Productions_no_attrs = (IConstructor) _read("prod([\\iter-star-seps(sort(\"Production\"),[layouts(\"LAYOUTLIST\")])],sort(\"Productions\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___lit_non_assoc_Associativity_attrs___term__cons_NonAssoc = (IConstructor) _read("prod([lit(\"non-assoc\")],sort(\"Associativity\"),attrs([term(cons(\"NonAssoc\"))]))", Factory.Production);
	private static final IConstructor prod___lit___40_layouts_LAYOUTLIST_CharRanges_layouts_LAYOUTLIST_lit___41_CharRanges_attrs___term__cons_Bracket = (IConstructor) _read("prod([lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"CharRanges\"),layouts(\"LAYOUTLIST\"),lit(\")\")],sort(\"CharRanges\"),attrs([term(cons(\"Bracket\"))]))", Factory.Production);
	private static final IConstructor prod___lit___60_layouts_LAYOUTLIST_iter_seps__NatCon__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ArgumentIndicator_attrs___term__cons_Default = (IConstructor) _read("prod([lit(\"\\<\"),layouts(\"LAYOUTLIST\"),\\iter-seps(sort(\"NatCon\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],sort(\"ArgumentIndicator\"),attrs([term(cons(\"Default\"))]))", Factory.Production);
	private static final IConstructor prod___lit_definition_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"definition\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___FunctionName_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_PrefixFun = (IConstructor) _read("prod([sort(\"FunctionName\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"Symbol\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\")\"),layouts(\"LAYOUTLIST\"),sort(\"Attributes\")],sort(\"Production\"),attrs([term(cons(\"PrefixFun\"))]))", Factory.Production);
	private static final IConstructor prod___layouts_LAYOUTLIST_SDF_layouts_LAYOUTLIST_start__SDF_no_attrs = (IConstructor) _read("prod([layouts(\"LAYOUTLIST\"),sort(\"SDF\"),layouts(\"LAYOUTLIST\")],start(sort(\"SDF\")),\\no-attrs())", Factory.Production);
	private static final IConstructor regular__iter_seps__NatCon__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-seps(sort(\"NatCon\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___lit_avoid_ATermAttribute_attrs___reject = (IConstructor) _read("prod([lit(\"avoid\")],sort(\"ATermAttribute\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___Production_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Production_Renaming_attrs___term__cons_production = (IConstructor) _read("prod([sort(\"Production\"),layouts(\"LAYOUTLIST\"),lit(\"=\\>\"),layouts(\"LAYOUTLIST\"),sort(\"Production\")],sort(\"Renaming\"),attrs([term(cons(\"production\"))]))", Factory.Production);
	private static final IConstructor prod___lit_bracket_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"bracket\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___OptExp_attrs___term__cons_Absent = (IConstructor) _read("prod([],sort(\"OptExp\"),attrs([term(cons(\"Absent\"))]))", Factory.Production);
	private static final IConstructor prod___iter__char_class___range__48_57_NatCon_attrs___term__cons_Digits_lex = (IConstructor) _read("prod([iter(\\char-class([range(48,57)]))],sort(\"NatCon\"),attrs([term(cons(\"Digits\")),lex()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__123_123_lit___123_attrs___literal = (IConstructor) _read("prod([\\char-class([range(123,123)])],lit(\"{\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__126_126_lit___126_attrs___literal = (IConstructor) _read("prod([\\char-class([range(126,126)])],lit(\"~\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___ATermAttribute_Attribute_attrs___term__cons_Term = (IConstructor) _read("prod([sort(\"ATermAttribute\")],sort(\"Attribute\"),attrs([term(cons(\"Term\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__124_124_lit___124_attrs___literal = (IConstructor) _read("prod([\\char-class([range(124,124)])],lit(\"|\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__125_125_lit___125_attrs___literal = (IConstructor) _read("prod([\\char-class([range(125,125)])],lit(\"}\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_StrChar_attrs___term__cons_Decimal_lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)])],sort(\"StrChar\"),attrs([term(cons(\"Decimal\")),lex()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__65_90_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_char_class___range__48_57_range__65_90_range__97_122_Sort_attrs___term__cons_MoreChars_lex = (IConstructor) _read("prod([\\char-class([range(65,90)]),\\iter-star(\\char-class([range(45,45),range(48,57),range(65,90),range(97,122)])),\\char-class([range(48,57),range(65,90),range(97,122)])],sort(\"Sort\"),attrs([term(cons(\"MoreChars\")),lex()]))", Factory.Production);
	private static final IConstructor prod___lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_Priorities = (IConstructor) _read("prod([lit(\"priorities\"),layouts(\"LAYOUTLIST\"),sort(\"Priorities\")],sort(\"Grammar\"),attrs([term(cons(\"Priorities\"))]))", Factory.Production);
	private static final IConstructor prod___lit___92_69_79_70_Character_attrs___term__cons_eof = (IConstructor) _read("prod([lit(\"\\\\EOF\")],sort(\"Character\"),attrs([term(cons(\"eof\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__96_96_lit___96_attrs___literal = (IConstructor) _read("prod([\\char-class([range(96,96)])],lit(\"`\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit_LAYOUT_Sort_attrs___reject = (IConstructor) _read("prod([lit(\"LAYOUT\")],sort(\"Sort\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__110_110_StrChar_attrs___term__cons_NewLine_lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(110,110)])],sort(\"StrChar\"),attrs([term(cons(\"NewLine\")),lex()]))", Factory.Production);
	private static final IConstructor prod___Sort_Symbol_attrs___term__cons_Sort = (IConstructor) _read("prod([sort(\"Sort\")],sort(\"Symbol\"),attrs([term(cons(\"Sort\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__0_8_range__11_33_range__35_91_range__93_65535_StrChar_attrs___term__cons_Normal_lex = (IConstructor) _read("prod([\\char-class([range(0,8),range(11,33),range(35,91),range(93,65535)])],sort(\"StrChar\"),attrs([term(cons(\"Normal\")),lex()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__101_101_lit_e_attrs___literal = (IConstructor) _read("prod([\\char-class([range(101,101)])],lit(\"e\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Syntax = (IConstructor) _read("prod([lit(\"syntax\"),layouts(\"LAYOUTLIST\"),sort(\"Productions\")],sort(\"Grammar\"),attrs([term(cons(\"Syntax\"))]))", Factory.Production);
	private static final IConstructor prod___StrCon_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbols_attrs___reject = (IConstructor) _read("prod([sort(\"StrCon\"),layouts(\"LAYOUTLIST\"),lit(\"(\"),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"Symbol\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\")\")],sort(\"Symbols\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___lit___126_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Comp = (IConstructor) _read("prod([lit(\"~\"),layouts(\"LAYOUTLIST\"),sort(\"CharClass\")],sort(\"CharClass\"),attrs([term(cons(\"Comp\"))]))", Factory.Production);
	private static final IConstructor prod___lit_context_free_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_ContextFreeRestrictions = (IConstructor) _read("prod([lit(\"context-free\"),layouts(\"LAYOUTLIST\"),lit(\"restrictions\"),layouts(\"LAYOUTLIST\"),sort(\"Restrictions\")],sort(\"Grammar\"),attrs([term(cons(\"ContextFreeRestrictions\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__115_115_lit_imports_attrs___literal = (IConstructor) _read("prod([\\char-class([range(105,105)]),\\char-class([range(109,109)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(115,115)])],lit(\"imports\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit___40_layouts_LAYOUTLIST_CharClass_layouts_LAYOUTLIST_lit___41_CharClass_attrs___term__cons_Bracket = (IConstructor) _read("prod([lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"CharClass\"),layouts(\"LAYOUTLIST\"),lit(\")\")],sort(\"CharClass\"),attrs([term(cons(\"Bracket\"))]))", Factory.Production);
	private static final IConstructor prod___lit___40_layouts_LAYOUTLIST_Import_layouts_LAYOUTLIST_lit___41_Import_attrs___term__cons_Bracket = (IConstructor) _read("prod([lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"Import\"),layouts(\"LAYOUTLIST\"),lit(\")\")],sort(\"Import\"),attrs([term(cons(\"Bracket\"))]))", Factory.Production);
	private static final IConstructor prod___lit_context_free_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_ContextFreePriorities = (IConstructor) _read("prod([lit(\"context-free\"),layouts(\"LAYOUTLIST\"),lit(\"priorities\"),layouts(\"LAYOUTLIST\"),sort(\"Priorities\")],sort(\"Grammar\"),attrs([term(cons(\"ContextFreePriorities\"))]))", Factory.Production);
	private static final IConstructor prod___lit___layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Negative = (IConstructor) _read("prod([lit(\"-\"),layouts(\"LAYOUTLIST\"),sort(\"NatCon\")],sort(\"IntCon\"),attrs([term(cons(\"Negative\"))]))", Factory.Production);
	private static final IConstructor prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Bracket = (IConstructor) _read("prod([lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\")\")],sort(\"Symbol\"),attrs([term(cons(\"Bracket\"))]))", Factory.Production);
	private static final IConstructor prod___lit_lexical_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalSyntax = (IConstructor) _read("prod([lit(\"lexical\"),layouts(\"LAYOUTLIST\"),lit(\"syntax\"),layouts(\"LAYOUTLIST\"),sort(\"Productions\")],sort(\"Grammar\"),attrs([term(cons(\"LexicalSyntax\"))]))", Factory.Production);
	private static final IConstructor regular__iter_star_seps__Renaming__layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Renaming\"),[layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___lit___60_83_84_65_82_84_62_Symbol_attrs___term__cons_Start = (IConstructor) _read("prod([lit(\"\\<START\\>\")],sort(\"Symbol\"),attrs([term(cons(\"Start\"))]))", Factory.Production);
	private static final IConstructor prod___lit___40_layouts_LAYOUTLIST_Grammar_layouts_LAYOUTLIST_lit___41_Grammar_attrs___term__cons_Bracket = (IConstructor) _read("prod([lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"Grammar\"),layouts(\"LAYOUTLIST\"),lit(\")\")],sort(\"Grammar\"),attrs([term(cons(\"Bracket\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__91_91_lit___91_attrs___literal = (IConstructor) _read("prod([\\char-class([range(91,91)])],lit(\"[\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_lit_non_assoc_attrs___literal = (IConstructor) _read("prod([\\char-class([range(110,110)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(45,45)]),\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],lit(\"non-assoc\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__93_93_lit___93_attrs___literal = (IConstructor) _read("prod([\\char-class([range(93,93)])],lit(\"]\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__91_91_char_class___range__91_91_lit___91_91_attrs___literal = (IConstructor) _read("prod([\\char-class([range(91,91)]),\\char-class([range(91,91)])],lit(\"[[\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___EMPTY_layouts_LAYOUTLIST_lit_definition_layouts_LAYOUTLIST_Definition_layouts_LAYOUTLIST_EMPTY_SDF_attrs___term__cons_Definition = (IConstructor) _read("prod([sort(\"EMPTY\"),layouts(\"LAYOUTLIST\"),lit(\"definition\"),layouts(\"LAYOUTLIST\"),sort(\"Definition\"),layouts(\"LAYOUTLIST\"),sort(\"EMPTY\")],sort(\"SDF\"),attrs([term(cons(\"Definition\"))]))", Factory.Production);
	private static final IConstructor prod___lit___91_layouts_LAYOUTLIST_OptCharRanges_layouts_LAYOUTLIST_lit___93_CharClass_attrs___term__cons_SimpleCharClass = (IConstructor) _read("prod([lit(\"[\"),layouts(\"LAYOUTLIST\"),sort(\"OptCharRanges\"),layouts(\"LAYOUTLIST\"),lit(\"]\")],sort(\"CharClass\"),attrs([term(cons(\"SimpleCharClass\"))]))", Factory.Production);
	private static final IConstructor prod___lit___96_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___96_Symbol_attrs___term__cons_Lifting = (IConstructor) _read("prod([lit(\"`\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\"`\")],sort(\"Symbol\"),attrs([term(cons(\"Lifting\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__34_34_StrChar_attrs___term__cons_Quote_lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(34,34)])],sort(\"StrChar\"),attrs([term(cons(\"Quote\")),lex()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__93_93_char_class___range__93_93_lit___93_93_attrs___literal = (IConstructor) _read("prod([\\char-class([range(93,93)]),\\char-class([range(93,93)])],lit(\"]]\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___CharClass_layouts_LAYOUTLIST_lit___92_47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Union = (IConstructor) _read("prod([sort(\"CharClass\"),layouts(\"LAYOUTLIST\"),lit(\"\\\\/\"),layouts(\"LAYOUTLIST\"),sort(\"CharClass\")],sort(\"CharClass\"),attrs([term(cons(\"Union\"))]))", Factory.Production);
	private static final IConstructor regular__iter_star_seps__Module__layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Module\"),[layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___lit_sorts_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"sorts\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___IntCon_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_NatCon_layouts_LAYOUTLIST_OptExp_RealCon_attrs___term__cons_RealCon = (IConstructor) _read("prod([sort(\"IntCon\"),layouts(\"LAYOUTLIST\"),lit(\".\"),layouts(\"LAYOUTLIST\"),sort(\"NatCon\"),layouts(\"LAYOUTLIST\"),sort(\"OptExp\")],sort(\"RealCon\"),attrs([term(cons(\"RealCon\"))]))", Factory.Production);
	private static final IConstructor prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc = (IConstructor) _read("prod([sort(\"Grammar\"),layouts(\"LAYOUTLIST\"),sort(\"Grammar\")],sort(\"Grammar\"),attrs([term(cons(\"ConcGrammars\")),assoc(assoc())]))", Factory.Production);
	private static final IConstructor prod___char_class___range__58_58_lit___58_attrs___literal = (IConstructor) _read("prod([\\char-class([range(58,58)])],lit(\":\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__47_47_lit___92_47_attrs___literal = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(47,47)])],lit(\"\\\\/\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit___123_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_ProdsGroup = (IConstructor) _read("prod([lit(\"{\"),layouts(\"LAYOUTLIST\"),sort(\"Productions\"),layouts(\"LAYOUTLIST\"),lit(\"}\")],sort(\"Group\"),attrs([term(cons(\"ProdsGroup\"))]))", Factory.Production);
	private static final IConstructor prod___Character_CharRange_no_attrs = (IConstructor) _read("prod([sort(\"Character\")],sort(\"CharRange\"),\\no-attrs())", Factory.Production);
	private static final IConstructor regular__iter__char_class___range__48_57_no_attrs = (IConstructor) _read("regular(iter(\\char-class([range(48,57)])),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___IdCon_AFun_attrs___term__cons_Unquoted = (IConstructor) _read("prod([sort(\"IdCon\")],sort(\"AFun\"),attrs([term(cons(\"Unquoted\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__62_62_lit___62_attrs___literal = (IConstructor) _read("prod([\\char-class([range(62,62)])],lit(\"\\>\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__63_63_lit___63_attrs___literal = (IConstructor) _read("prod([\\char-class([range(63,63)])],lit(\"?\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___iter_star_seps__Symbol__layouts_LAYOUTLIST_Symbols_no_attrs = (IConstructor) _read("prod([\\iter-star-seps(sort(\"Symbol\"),[layouts(\"LAYOUTLIST\")])],sort(\"Symbols\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__60_60_lit___60_attrs___literal = (IConstructor) _read("prod([\\char-class([range(60,60)])],lit(\"\\<\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit_lexical_layouts_LAYOUTLIST_lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalVariables = (IConstructor) _read("prod([lit(\"lexical\"),layouts(\"LAYOUTLIST\"),lit(\"variables\"),layouts(\"LAYOUTLIST\"),sort(\"Productions\")],sort(\"Grammar\"),attrs([term(cons(\"LexicalVariables\"))]))", Factory.Production);
	private static final IConstructor prod___lit_right_Associativity_attrs___term__cons_Right = (IConstructor) _read("prod([lit(\"right\")],sort(\"Associativity\"),attrs([term(cons(\"Right\"))]))", Factory.Production);
	private static final IConstructor prod___lit_aliases_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"aliases\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___Lookaheads_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Lookaheads_Lookaheads_attrs___term__cons_Alt_assoc__right = (IConstructor) _read("prod([sort(\"Lookaheads\"),layouts(\"LAYOUTLIST\"),lit(\"|\"),layouts(\"LAYOUTLIST\"),sort(\"Lookaheads\")],sort(\"Lookaheads\"),attrs([term(cons(\"Alt\")),assoc(right())]))", Factory.Production);
	private static final IConstructor prod___lit___91_91_layouts_LAYOUTLIST_iter_star_seps__Lookahead__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Lookaheads_attrs___term__cons_List = (IConstructor) _read("prod([lit(\"[[\"),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"Lookahead\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"]]\")],sort(\"Lookaheads\"),attrs([term(cons(\"List\"))]))", Factory.Production);
	private static final IConstructor prod___iter_star_seps__Import__layouts_LAYOUTLIST_Imports_no_attrs = (IConstructor) _read("prod([\\iter-star-seps(sort(\"Import\"),[layouts(\"LAYOUTLIST\")])],sort(\"Imports\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__37_37_lit___37_attrs___literal = (IConstructor) _read("prod([\\char-class([range(37,37)])],lit(\"%\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit_lexical_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_LexicalPriorities = (IConstructor) _read("prod([lit(\"lexical\"),layouts(\"LAYOUTLIST\"),lit(\"priorities\"),layouts(\"LAYOUTLIST\"),sort(\"Priorities\")],sort(\"Grammar\"),attrs([term(cons(\"LexicalPriorities\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__42_42_lit___42_attrs___literal = (IConstructor) _read("prod([\\char-class([range(42,42)])],lit(\"*\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit___92_84_79_80_Character_attrs___term__cons_top = (IConstructor) _read("prod([lit(\"\\\\TOP\")],sort(\"Character\"),attrs([term(cons(\"top\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__43_43_lit___43_attrs___literal = (IConstructor) _read("prod([\\char-class([range(43,43)])],lit(\"+\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__40_40_lit___40_attrs___literal = (IConstructor) _read("prod([\\char-class([range(40,40)])],lit(\"(\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__41_41_lit___41_attrs___literal = (IConstructor) _read("prod([\\char-class([range(41,41)])],lit(\")\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__105_105_char_class___range__100_100_lit_id_attrs___literal = (IConstructor) _read("prod([\\char-class([range(105,105)]),\\char-class([range(100,100)])],lit(\"id\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit_avoid_Attribute_attrs___term__cons_Avoid = (IConstructor) _read("prod([lit(\"avoid\")],sort(\"Attribute\"),attrs([term(cons(\"Avoid\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__46_46_lit___46_attrs___literal = (IConstructor) _read("prod([\\char-class([range(46,46)])],lit(\".\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___layouts_LAYOUTLIST_Module_layouts_LAYOUTLIST_start__Module_no_attrs = (IConstructor) _read("prod([layouts(\"LAYOUTLIST\"),sort(\"Module\"),layouts(\"LAYOUTLIST\")],start(sort(\"Module\")),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__47_47_lit___47_attrs___literal = (IConstructor) _read("prod([\\char-class([range(47,47)])],lit(\"/\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_lit_right_attrs___literal = (IConstructor) _read("prod([\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(103,103)]),\\char-class([range(104,104)]),\\char-class([range(116,116)])],lit(\"right\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_Restrictions = (IConstructor) _read("prod([lit(\"restrictions\"),layouts(\"LAYOUTLIST\"),sort(\"Restrictions\")],sort(\"Grammar\"),attrs([term(cons(\"Restrictions\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__44_44_lit___44_attrs___literal = (IConstructor) _read("prod([\\char-class([range(44,44)])],lit(\",\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___iter_star_seps__Alias__layouts_LAYOUTLIST_Aliases_no_attrs = (IConstructor) _read("prod([\\iter-star-seps(sort(\"Alias\"),[layouts(\"LAYOUTLIST\")])],sort(\"Aliases\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__45_45_lit___attrs___literal = (IConstructor) _read("prod([\\char-class([range(45,45)])],lit(\"-\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__92_92_SingleQuotedStrChar_attrs___term__cons_Backslash_lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(92,92)])],sort(\"SingleQuotedStrChar\"),attrs([term(cons(\"Backslash\")),lex()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__0_47_range__58_64_range__91_96_range__110_110_range__114_114_range__116_116_range__123_65535_ShortChar_attrs___term__cons_Escaped_lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(0,47),range(58,64),range(91,96),range(110,110),range(114,114),range(116,116),range(123,65535)])],sort(\"ShortChar\"),attrs([term(cons(\"Escaped\")),lex()]))", Factory.Production);
	private static final IConstructor prod___lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Variables = (IConstructor) _read("prod([lit(\"variables\"),layouts(\"LAYOUTLIST\"),sort(\"Productions\")],sort(\"Grammar\"),attrs([term(cons(\"Variables\"))]))", Factory.Production);
	private static final IConstructor regular__iter_star_seps__Production__layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Production\"),[layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___lit___92_76_65_66_69_76_95_83_84_65_82_84_Character_attrs___term__cons_label_start = (IConstructor) _read("prod([lit(\"\\\\LABEL_START\")],sort(\"Character\"),attrs([term(cons(\"label_start\"))]))", Factory.Production);
	private static final IConstructor prod___ModuleWord_ModuleId_attrs___term__cons_Leaf_lex = (IConstructor) _read("prod([sort(\"ModuleWord\")],sort(\"ModuleId\"),attrs([term(cons(\"Leaf\")),lex()]))", Factory.Production);
	private static final IConstructor prod___Symbols_layouts_LAYOUTLIST_lit___45_47_45_layouts_LAYOUTLIST_Lookaheads_Restriction_attrs___term__cons_Follow = (IConstructor) _read("prod([sort(\"Symbols\"),layouts(\"LAYOUTLIST\"),lit(\"-/-\"),layouts(\"LAYOUTLIST\"),sort(\"Lookaheads\")],sort(\"Restriction\"),attrs([term(cons(\"Follow\"))]))", Factory.Production);
	private static final IConstructor prod___lit_context_free_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_ContextFreeStartSymbols = (IConstructor) _read("prod([lit(\"context-free\"),layouts(\"LAYOUTLIST\"),lit(\"start-symbols\"),layouts(\"LAYOUTLIST\"),sort(\"Symbols\")],sort(\"Grammar\"),attrs([term(cons(\"ContextFreeStartSymbols\"))]))", Factory.Production);
	private static final IConstructor prod___lit___123_layouts_LAYOUTLIST_iter_star_seps__Attribute__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Attributes_attrs___term__cons_Attrs = (IConstructor) _read("prod([lit(\"{\"),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"Attribute\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"}\")],sort(\"Attributes\"),attrs([term(cons(\"Attrs\"))]))", Factory.Production);
	private static final IConstructor prod___CharRange_CharRanges_no_attrs = (IConstructor) _read("prod([sort(\"CharRange\")],sort(\"CharRanges\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_char_class___range__45_45_char_class___range__115_115_char_class___range__121_121_char_class___range__109_109_char_class___range__98_98_char_class___range__111_111_char_class___range__108_108_char_class___range__115_115_lit_start_symbols_attrs___literal = (IConstructor) _read("prod([\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(45,45)]),\\char-class([range(115,115)]),\\char-class([range(121,121)]),\\char-class([range(109,109)]),\\char-class([range(98,98)]),\\char-class([range(111,111)]),\\char-class([range(108,108)]),\\char-class([range(115,115)])],lit(\"start-symbols\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___Group_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_Group_Priority_attrs___term__cons_Assoc = (IConstructor) _read("prod([sort(\"Group\"),layouts(\"LAYOUTLIST\"),sort(\"Associativity\"),layouts(\"LAYOUTLIST\"),sort(\"Group\")],sort(\"Priority\"),attrs([term(cons(\"Assoc\"))]))", Factory.Production);
	private static final IConstructor prod___lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_KernalStartSymbols = (IConstructor) _read("prod([lit(\"start-symbols\"),layouts(\"LAYOUTLIST\"),sort(\"Symbols\")],sort(\"Grammar\"),attrs([term(cons(\"KernalStartSymbols\"))]))", Factory.Production);
	private static final IConstructor prod___lit_sorts_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_Sorts = (IConstructor) _read("prod([lit(\"sorts\"),layouts(\"LAYOUTLIST\"),sort(\"Symbols\")],sort(\"Grammar\"),attrs([term(cons(\"Sorts\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__34_34_iter_star__StrChar_char_class___range__34_34_StrCon_attrs___term__cons_Default_lex = (IConstructor) _read("prod([\\char-class([range(34,34)]),\\iter-star(sort(\"StrChar\")),\\char-class([range(34,34)])],sort(\"StrCon\"),attrs([term(cons(\"Default\")),lex()]))", Factory.Production);
	private static final IConstructor prod___lit_LAYOUT_Symbol_attrs___term__cons_Layout = (IConstructor) _read("prod([lit(\"LAYOUT\")],sort(\"Symbol\"),attrs([term(cons(\"Layout\"))]))", Factory.Production);
	private static final IConstructor regular__iter_star_seps__Section__layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Section\"),[layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__116_116_SingleQuotedStrChar_attrs___term__cons_Tab_lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(116,116)])],sort(\"SingleQuotedStrChar\"),attrs([term(cons(\"Tab\")),lex()]))", Factory.Production);
	private static final IConstructor prod___ModuleId_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___93_ModuleName_attrs___term__cons_Parameterized = (IConstructor) _read("prod([sort(\"ModuleId\"),layouts(\"LAYOUTLIST\"),lit(\"[\"),layouts(\"LAYOUTLIST\"),sort(\"Symbols\"),layouts(\"LAYOUTLIST\"),lit(\"]\")],sort(\"ModuleName\"),attrs([term(cons(\"Parameterized\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_lit_module_attrs___literal = (IConstructor) _read("prod([\\char-class([range(109,109)]),\\char-class([range(111,111)]),\\char-class([range(100,100)]),\\char-class([range(117,117)]),\\char-class([range(108,108)]),\\char-class([range(101,101)])],lit(\"module\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit_left_Associativity_attrs___term__cons_Left = (IConstructor) _read("prod([lit(\"left\")],sort(\"Associativity\"),attrs([term(cons(\"Left\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__97_97_char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_lit_avoid_attrs___literal = (IConstructor) _read("prod([\\char-class([range(97,97)]),\\char-class([range(118,118)]),\\char-class([range(111,111)]),\\char-class([range(105,105)]),\\char-class([range(100,100)])],lit(\"avoid\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit___40_47_41_Grammar_attrs___term__cons_EmptyGrammar = (IConstructor) _read("prod([lit(\"(/)\")],sort(\"Grammar\"),attrs([term(cons(\"EmptyGrammar\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_SingleQuotedStrChar_attrs___term__cons_Decimal_lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(48,57)]),\\char-class([range(48,57)]),\\char-class([range(48,57)])],sort(\"SingleQuotedStrChar\"),attrs([term(cons(\"Decimal\")),lex()]))", Factory.Production);
	private static final IConstructor regular__iter_star_seps__Alias__layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Alias\"),[layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_lit_assoc_attrs___literal = (IConstructor) _read("prod([\\char-class([range(97,97)]),\\char-class([range(115,115)]),\\char-class([range(115,115)]),\\char-class([range(111,111)]),\\char-class([range(99,99)])],lit(\"assoc\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__110_110_SingleQuotedStrChar_attrs___term__cons_NewLine_lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(110,110)])],sort(\"SingleQuotedStrChar\"),attrs([term(cons(\"NewLine\")),lex()]))", Factory.Production);
	private static final IConstructor regular__iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-seps(sort(\"Symbol\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___iter__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_ModuleWord_attrs___term__cons_Word_lex = (IConstructor) _read("prod([iter(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)]))],sort(\"ModuleWord\"),attrs([term(cons(\"Word\")),lex()]))", Factory.Production);
	private static final IConstructor prod___Group_layouts_LAYOUTLIST_ArgumentIndicator_Group_attrs___term__cons_WithArguments_assoc__non_assoc = (IConstructor) _read("prod([sort(\"Group\"),layouts(\"LAYOUTLIST\"),sort(\"ArgumentIndicator\")],sort(\"Group\"),attrs([term(cons(\"WithArguments\")),assoc(\\non-assoc())]))", Factory.Production);
	private static final IConstructor prod___lit_restrictions_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"restrictions\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__0_8_range__11_38_range__40_91_range__93_65535_SingleQuotedStrChar_attrs___term__cons_Normal_lex = (IConstructor) _read("prod([\\char-class([range(0,8),range(11,38),range(40,91),range(93,65535)])],sort(\"SingleQuotedStrChar\"),attrs([term(cons(\"Normal\")),lex()]))", Factory.Production);
	private static final IConstructor prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__LEX_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Lex = (IConstructor) _read("prod([lit(\"\\<\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\"-LEX\"),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],sort(\"Symbol\"),attrs([term(cons(\"Lex\"))]))", Factory.Production);
	private static final IConstructor prod___ModuleName_Import_attrs___term__cons_Module = (IConstructor) _read("prod([sort(\"ModuleName\")],sort(\"Import\"),attrs([term(cons(\"Module\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__45_45_char_class___range__67_67_char_class___range__70_70_lit__CF_attrs___literal = (IConstructor) _read("prod([\\char-class([range(45,45)]),\\char-class([range(67,67)]),\\char-class([range(70,70)])],lit(\"-CF\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___StrCon_AFun_attrs___term__cons_Quoted = (IConstructor) _read("prod([sort(\"StrCon\")],sort(\"AFun\"),attrs([term(cons(\"Quoted\"))]))", Factory.Production);
	private static final IConstructor prod___lit_hiddens_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"hiddens\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__39_39_SingleQuotedStrChar_attrs___term__cons_Quote_lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(39,39)])],sort(\"SingleQuotedStrChar\"),attrs([term(cons(\"Quote\")),lex()]))", Factory.Production);
	private static final IConstructor prod___lit_exports_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Exports = (IConstructor) _read("prod([lit(\"exports\"),layouts(\"LAYOUTLIST\"),sort(\"Grammar\")],sort(\"Section\"),attrs([term(cons(\"Exports\"))]))", Factory.Production);
	private static final IConstructor prod___Associativity_Label_attrs___reject = (IConstructor) _read("prod([sort(\"Associativity\")],sort(\"Label\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___lit_left_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"left\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__101_101_char_class___range__120_120_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__115_115_lit_exports_attrs___literal = (IConstructor) _read("prod([\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(112,112)]),\\char-class([range(111,111)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(115,115)])],lit(\"exports\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__47_47_char_class___range__92_92_lit___47_92_attrs___literal = (IConstructor) _read("prod([\\char-class([range(47,47)]),\\char-class([range(92,92)])],lit(\"/\\\\\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor regular__iter_seps__Symbol__layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-seps(sort(\"Symbol\"),[layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___Attributes_attrs___term__cons_NoAttrs = (IConstructor) _read("prod([],sort(\"Attributes\"),attrs([term(cons(\"NoAttrs\"))]))", Factory.Production);
	private static final IConstructor prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ATerm_attrs___term__cons_List = (IConstructor) _read("prod([lit(\"[\"),layouts(\"LAYOUTLIST\"),\\iter-star-seps(sort(\"ATerm\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"]\")],sort(\"ATerm\"),attrs([term(cons(\"List\"))]))", Factory.Production);
	private static final IConstructor prod___lit_right_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"right\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___CharClass_Lookahead_attrs___term__cons_CharClass = (IConstructor) _read("prod([sort(\"CharClass\")],sort(\"Lookahead\"),attrs([term(cons(\"CharClass\"))]))", Factory.Production);
	private static final IConstructor prod___lit_aliases_layouts_LAYOUTLIST_Aliases_Grammar_attrs___term__cons_Aliases = (IConstructor) _read("prod([lit(\"aliases\"),layouts(\"LAYOUTLIST\"),sort(\"Aliases\")],sort(\"Grammar\"),attrs([term(cons(\"Aliases\"))]))", Factory.Production);
	private static final IConstructor prod___Production_Group_attrs___term__cons_SimpleGroup = (IConstructor) _read("prod([sort(\"Production\")],sort(\"Group\"),attrs([term(cons(\"SimpleGroup\"))]))", Factory.Production);
	private static final IConstructor prod___lit_assoc_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"assoc\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor regular__iter__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_no_attrs = (IConstructor) _read("regular(iter(\\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)])),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___iter_star_seps__Priority__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_Priorities_no_attrs = (IConstructor) _read("prod([\\iter-star-seps(sort(\"Priority\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")])],sort(\"Priorities\"),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___lit_e_layouts_LAYOUTLIST_IntCon_OptExp_attrs___term__cons_Present = (IConstructor) _read("prod([lit(\"e\"),layouts(\"LAYOUTLIST\"),sort(\"IntCon\")],sort(\"OptExp\"),attrs([term(cons(\"Present\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__45_45_char_class___range__102_102_char_class___range__114_114_char_class___range__101_101_char_class___range__101_101_lit_context_free_attrs___literal = (IConstructor) _read("prod([\\char-class([range(99,99)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(101,101)]),\\char-class([range(120,120)]),\\char-class([range(116,116)]),\\char-class([range(45,45)]),\\char-class([range(102,102)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(101,101)])],lit(\"context-free\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit_module_ModuleName_attrs___reject = (IConstructor) _read("prod([lit(\"module\")],sort(\"ModuleName\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__37_37_char_class___range__37_37_lit___37_37_attrs___literal = (IConstructor) _read("prod([\\char-class([range(37,37)]),\\char-class([range(37,37)])],lit(\"%%\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__45_45_char_class___range__62_62_lit___45_62_attrs___literal = (IConstructor) _read("prod([\\char-class([range(45,45)]),\\char-class([range(62,62)])],lit(\"-\\>\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor regular__iter_star_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"ATerm\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__45_45_char_class___range__47_47_char_class___range__45_45_lit___45_47_45_attrs___literal = (IConstructor) _read("prod([\\char-class([range(45,45)]),\\char-class([range(47,47)]),\\char-class([range(45,45)])],lit(\"-/-\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit_lexical_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_LexicalStartSymbols = (IConstructor) _read("prod([lit(\"lexical\"),layouts(\"LAYOUTLIST\"),lit(\"start-symbols\"),layouts(\"LAYOUTLIST\"),sort(\"Symbols\")],sort(\"Grammar\"),attrs([term(cons(\"LexicalStartSymbols\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__114_114_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__105_105_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_lit_restrictions_attrs___literal = (IConstructor) _read("prod([\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(115,115)]),\\char-class([range(116,116)]),\\char-class([range(114,114)]),\\char-class([range(105,105)]),\\char-class([range(99,99)]),\\char-class([range(116,116)]),\\char-class([range(105,105)]),\\char-class([range(111,111)]),\\char-class([range(110,110)]),\\char-class([range(115,115)])],lit(\"restrictions\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit___60_83_116_97_114_116_62_Symbol_attrs___term__cons_FileStart = (IConstructor) _read("prod([lit(\"\\<Start\\>\")],sort(\"Symbol\"),attrs([term(cons(\"FileStart\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__48_57_range__65_90_range__97_122_ShortChar_attrs___term__cons_Regular_lex = (IConstructor) _read("prod([\\char-class([range(48,57),range(65,90),range(97,122)])],sort(\"ShortChar\"),attrs([term(cons(\"Regular\")),lex()]))", Factory.Production);
	private static final IConstructor prod___lit___92_66_79_84_Character_attrs___term__cons_bot = (IConstructor) _read("prod([lit(\"\\\\BOT\")],sort(\"Character\"),attrs([term(cons(\"bot\"))]))", Factory.Production);
	private static final IConstructor regular__iter_star_seps__Lookahead__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Lookahead\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
	private static final IConstructor prod___char_class___range__112_112_char_class___range__114_114_char_class___range__101_101_char_class___range__102_102_char_class___range__101_101_char_class___range__114_114_lit_prefer_attrs___literal = (IConstructor) _read("prod([\\char-class([range(112,112)]),\\char-class([range(114,114)]),\\char-class([range(101,101)]),\\char-class([range(102,102)]),\\char-class([range(101,101)]),\\char-class([range(114,114)])],lit(\"prefer\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Strategy = (IConstructor) _read("prod([lit(\"(\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\"-\\>\"),layouts(\"LAYOUTLIST\"),sort(\"Symbol\"),layouts(\"LAYOUTLIST\"),lit(\")\")],sort(\"Symbol\"),attrs([term(cons(\"Strategy\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__116_116_StrChar_attrs___term__cons_Tab_lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(116,116)])],sort(\"StrChar\"),attrs([term(cons(\"Tab\")),lex()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_lit_syntax_attrs___literal = (IConstructor) _read("prod([\\char-class([range(115,115)]),\\char-class([range(121,121)]),\\char-class([range(110,110)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(120,120)])],lit(\"syntax\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit_reject_ATermAttribute_attrs___reject = (IConstructor) _read("prod([lit(\"reject\")],sort(\"ATermAttribute\"),attrs([reject()]))", Factory.Production);
	private static final IConstructor prod___CharRanges_OptCharRanges_attrs___term__cons_Present = (IConstructor) _read("prod([sort(\"CharRanges\")],sort(\"OptCharRanges\"),attrs([term(cons(\"Present\"))]))", Factory.Production);
	private static final IConstructor prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort = (IConstructor) _read("prod([sort(\"Sort\"),layouts(\"LAYOUTLIST\"),lit(\"[[\"),layouts(\"LAYOUTLIST\"),\\iter-seps(sort(\"Symbol\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),layouts(\"LAYOUTLIST\"),lit(\"]]\")],sort(\"Symbol\"),attrs([term(cons(\"ParameterizedSort\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__60_60_char_class___range__83_83_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_char_class___range__62_62_lit___60_83_116_97_114_116_62_attrs___literal = (IConstructor) _read("prod([\\char-class([range(60,60)]),\\char-class([range(83,83)]),\\char-class([range(116,116)]),\\char-class([range(97,97)]),\\char-class([range(114,114)]),\\char-class([range(116,116)]),\\char-class([range(62,62)])],lit(\"\\<Start\\>\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___lit___60_layouts_LAYOUTLIST_ATerm_layouts_LAYOUTLIST_lit___62_ATerm_attrs___term__cons_Placeholder = (IConstructor) _read("prod([lit(\"\\<\"),layouts(\"LAYOUTLIST\"),sort(\"ATerm\"),layouts(\"LAYOUTLIST\"),lit(\"\\>\")],sort(\"ATerm\"),attrs([term(cons(\"Placeholder\"))]))", Factory.Production);
	private static final IConstructor prod___char_class___range__45_45_char_class___range__76_76_char_class___range__69_69_char_class___range__88_88_lit__LEX_attrs___literal = (IConstructor) _read("prod([\\char-class([range(45,45)]),\\char-class([range(76,76)]),\\char-class([range(69,69)]),\\char-class([range(88,88)])],lit(\"-LEX\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__92_92_char_class___range__92_92_StrChar_attrs___term__cons_Backslash_lex = (IConstructor) _read("prod([\\char-class([range(92,92)]),\\char-class([range(92,92)])],sort(\"StrChar\"),attrs([term(cons(\"Backslash\")),lex()]))", Factory.Production);
	private static final IConstructor prod___char_class___range__61_61_char_class___range__62_62_lit___61_62_attrs___literal = (IConstructor) _read("prod([\\char-class([range(61,61)]),\\char-class([range(62,62)])],lit(\"=\\>\"),attrs([literal()]))", Factory.Production);
	private static final IConstructor regular__iter_star_seps__Attribute__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs = (IConstructor) _read("regular(\\iter-star-seps(sort(\"Attribute\"),[layouts(\"LAYOUTLIST\"),lit(\",\"),layouts(\"LAYOUTLIST\")]),\\no-attrs())", Factory.Production);
    
	// Item declarations
	
	
	private static class Grammar {
		
		public final static AbstractStackNode[] prod___lit_context_free_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_ContextFreePriorities = new AbstractStackNode[5];
		static{
			prod___lit_context_free_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_ContextFreePriorities[4] = new NonTerminalStackNode(40, 4 , "Priorities");
			prod___lit_context_free_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_ContextFreePriorities[3] = new NonTerminalStackNode(36, 3 , new IMatchableStackNode[] {new CharStackNode(38, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_context_free_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_ContextFreePriorities[2] = new LiteralStackNode(34, 2, prod___char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__111_111_char_class___range__114_114_char_class___range__105_105_char_class___range__116_116_char_class___range__105_105_char_class___range__101_101_char_class___range__115_115_lit_priorities_attrs___literal , new char[] {112,114,105,111,114,105,116,105,101,115});
			prod___lit_context_free_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_ContextFreePriorities[1] = new NonTerminalStackNode(30, 1 , new IMatchableStackNode[] {new CharStackNode(32, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_context_free_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_ContextFreePriorities[0] = new LiteralStackNode(28, 0, prod___char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__45_45_char_class___range__102_102_char_class___range__114_114_char_class___range__101_101_char_class___range__101_101_lit_context_free_attrs___literal , new char[] {99,111,110,116,101,120,116,45,102,114,101,101});
		}
		public final static AbstractStackNode[] prod___lit_context_free_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_ContextFreeSyntax = new AbstractStackNode[5];
		static{
			prod___lit_context_free_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_ContextFreeSyntax[0] = new LiteralStackNode(28, 0, prod___char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__45_45_char_class___range__102_102_char_class___range__114_114_char_class___range__101_101_char_class___range__101_101_lit_context_free_attrs___literal , new char[] {99,111,110,116,101,120,116,45,102,114,101,101});
			prod___lit_context_free_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_ContextFreeSyntax[1] = new NonTerminalStackNode(30, 1 , new IMatchableStackNode[] {new CharStackNode(32, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_context_free_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_ContextFreeSyntax[2] = new LiteralStackNode(48, 2, prod___char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_lit_syntax_attrs___literal , new char[] {115,121,110,116,97,120});
			prod___lit_context_free_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_ContextFreeSyntax[3] = new NonTerminalStackNode(50, 3 , new IMatchableStackNode[] {new CharStackNode(52, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_context_free_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_ContextFreeSyntax[4] = new NonTerminalStackNode(54, 4 , "Productions");
		}
		public final static AbstractStackNode[] prod___lit_lexical_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_LexicalRestrictions = new AbstractStackNode[5];
		static{
			prod___lit_lexical_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_LexicalRestrictions[4] = new NonTerminalStackNode(68, 4 , "Restrictions");
			prod___lit_lexical_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_LexicalRestrictions[3] = new NonTerminalStackNode(64, 3 , new IMatchableStackNode[] {new CharStackNode(66, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_lexical_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_LexicalRestrictions[2] = new LiteralStackNode(62, 2, prod___char_class___range__114_114_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__105_105_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_lit_restrictions_attrs___literal , new char[] {114,101,115,116,114,105,99,116,105,111,110,115});
			prod___lit_lexical_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_LexicalRestrictions[1] = new NonTerminalStackNode(58, 1 , new IMatchableStackNode[] {new CharStackNode(60, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_lexical_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_LexicalRestrictions[0] = new LiteralStackNode(56, 0, prod___char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_lit_lexical_attrs___literal , new char[] {108,101,120,105,99,97,108});
		}
		public final static AbstractStackNode[] prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc = new AbstractStackNode[3];
		static{
			prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc[0] = new NonTerminalStackNode(70, 0 , "Grammar");
			prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc[1] = new NonTerminalStackNode(72, 1 , new IMatchableStackNode[] {new CharStackNode(74, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc[2] = new NonTerminalStackNode(76, 2 , "Grammar");
		}
		public final static AbstractStackNode[] prod___lit_lexical_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalSyntax = new AbstractStackNode[5];
		static{
			prod___lit_lexical_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalSyntax[4] = new NonTerminalStackNode(90, 4 , "Productions");
			prod___lit_lexical_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalSyntax[3] = new NonTerminalStackNode(86, 3 , new IMatchableStackNode[] {new CharStackNode(88, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_lexical_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalSyntax[2] = new LiteralStackNode(84, 2, prod___char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_lit_syntax_attrs___literal , new char[] {115,121,110,116,97,120});
			prod___lit_lexical_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalSyntax[1] = new NonTerminalStackNode(58, 1 , new IMatchableStackNode[] {new CharStackNode(60, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_lexical_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalSyntax[0] = new LiteralStackNode(56, 0, prod___char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_lit_lexical_attrs___literal , new char[] {108,101,120,105,99,97,108});
		}
		public final static AbstractStackNode[] prod___lit___40_layouts_LAYOUTLIST_Grammar_layouts_LAYOUTLIST_lit___41_Grammar_attrs___term__cons_Bracket = new AbstractStackNode[5];
		static{
			prod___lit___40_layouts_LAYOUTLIST_Grammar_layouts_LAYOUTLIST_lit___41_Grammar_attrs___term__cons_Bracket[4] = new LiteralStackNode(104, 4, prod___char_class___range__41_41_lit___41_attrs___literal , new char[] {41});
			prod___lit___40_layouts_LAYOUTLIST_Grammar_layouts_LAYOUTLIST_lit___41_Grammar_attrs___term__cons_Bracket[3] = new NonTerminalStackNode(100, 3 , new IMatchableStackNode[] {new CharStackNode(102, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Grammar_layouts_LAYOUTLIST_lit___41_Grammar_attrs___term__cons_Bracket[2] = new NonTerminalStackNode(98, 2 , "Grammar");
			prod___lit___40_layouts_LAYOUTLIST_Grammar_layouts_LAYOUTLIST_lit___41_Grammar_attrs___term__cons_Bracket[1] = new NonTerminalStackNode(94, 1 , new IMatchableStackNode[] {new CharStackNode(96, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Grammar_layouts_LAYOUTLIST_lit___41_Grammar_attrs___term__cons_Bracket[0] = new LiteralStackNode(92, 0, prod___char_class___range__40_40_lit___40_attrs___literal , new char[] {40});
		}
		public final static AbstractStackNode[] prod___lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Variables = new AbstractStackNode[3];
		static{
			prod___lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Variables[2] = new NonTerminalStackNode(112, 2 , "Productions");
			prod___lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Variables[1] = new NonTerminalStackNode(108, 1 , new IMatchableStackNode[] {new CharStackNode(110, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Variables[0] = new LiteralStackNode(106, 0, prod___char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_lit_variables_attrs___literal , new char[] {118,97,114,105,97,98,108,101,115});
		}
		public final static AbstractStackNode[] prod___lit_context_free_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_ContextFreeStartSymbols = new AbstractStackNode[5];
		static{
			prod___lit_context_free_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_ContextFreeStartSymbols[4] = new NonTerminalStackNode(126, 4 , "Symbols");
			prod___lit_context_free_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_ContextFreeStartSymbols[3] = new NonTerminalStackNode(122, 3 , new IMatchableStackNode[] {new CharStackNode(124, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_context_free_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_ContextFreeStartSymbols[2] = new LiteralStackNode(120, 2, prod___char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_char_class___range__45_45_char_class___range__115_115_char_class___range__121_121_char_class___range__109_109_char_class___range__98_98_char_class___range__111_111_char_class___range__108_108_char_class___range__115_115_lit_start_symbols_attrs___literal , new char[] {115,116,97,114,116,45,115,121,109,98,111,108,115});
			prod___lit_context_free_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_ContextFreeStartSymbols[1] = new NonTerminalStackNode(30, 1 , new IMatchableStackNode[] {new CharStackNode(32, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_context_free_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_ContextFreeStartSymbols[0] = new LiteralStackNode(28, 0, prod___char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__45_45_char_class___range__102_102_char_class___range__114_114_char_class___range__101_101_char_class___range__101_101_lit_context_free_attrs___literal , new char[] {99,111,110,116,101,120,116,45,102,114,101,101});
		}
		public final static AbstractStackNode[] prod___lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_Priorities = new AbstractStackNode[3];
		static{
			prod___lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_Priorities[2] = new NonTerminalStackNode(134, 2 , "Priorities");
			prod___lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_Priorities[1] = new NonTerminalStackNode(130, 1 , new IMatchableStackNode[] {new CharStackNode(132, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_Priorities[0] = new LiteralStackNode(128, 0, prod___char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__111_111_char_class___range__114_114_char_class___range__105_105_char_class___range__116_116_char_class___range__105_105_char_class___range__101_101_char_class___range__115_115_lit_priorities_attrs___literal , new char[] {112,114,105,111,114,105,116,105,101,115});
		}
		public final static AbstractStackNode[] prod___lit_lexical_layouts_LAYOUTLIST_lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalVariables = new AbstractStackNode[5];
		static{
			prod___lit_lexical_layouts_LAYOUTLIST_lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalVariables[0] = new LiteralStackNode(56, 0, prod___char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_lit_lexical_attrs___literal , new char[] {108,101,120,105,99,97,108});
			prod___lit_lexical_layouts_LAYOUTLIST_lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalVariables[1] = new NonTerminalStackNode(58, 1 , new IMatchableStackNode[] {new CharStackNode(60, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_lexical_layouts_LAYOUTLIST_lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalVariables[2] = new LiteralStackNode(142, 2, prod___char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_lit_variables_attrs___literal , new char[] {118,97,114,105,97,98,108,101,115});
			prod___lit_lexical_layouts_LAYOUTLIST_lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalVariables[3] = new NonTerminalStackNode(144, 3 , new IMatchableStackNode[] {new CharStackNode(146, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_lexical_layouts_LAYOUTLIST_lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalVariables[4] = new NonTerminalStackNode(148, 4 , "Productions");
		}
		public final static AbstractStackNode[] prod___lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_KernalStartSymbols = new AbstractStackNode[3];
		static{
			prod___lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_KernalStartSymbols[2] = new NonTerminalStackNode(156, 2 , "Symbols");
			prod___lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_KernalStartSymbols[1] = new NonTerminalStackNode(152, 1 , new IMatchableStackNode[] {new CharStackNode(154, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_KernalStartSymbols[0] = new LiteralStackNode(150, 0, prod___char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_char_class___range__45_45_char_class___range__115_115_char_class___range__121_121_char_class___range__109_109_char_class___range__98_98_char_class___range__111_111_char_class___range__108_108_char_class___range__115_115_lit_start_symbols_attrs___literal , new char[] {115,116,97,114,116,45,115,121,109,98,111,108,115});
		}
		public final static AbstractStackNode[] prod___lit_sorts_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_Sorts = new AbstractStackNode[3];
		static{
			prod___lit_sorts_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_Sorts[2] = new NonTerminalStackNode(164, 2 , "Symbols");
			prod___lit_sorts_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_Sorts[1] = new NonTerminalStackNode(160, 1 , new IMatchableStackNode[] {new CharStackNode(162, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_sorts_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_Sorts[0] = new LiteralStackNode(158, 0, prod___char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__115_115_lit_sorts_attrs___literal , new char[] {115,111,114,116,115});
		}
		public final static AbstractStackNode[] prod___lit_lexical_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_LexicalStartSymbols = new AbstractStackNode[5];
		static{
			prod___lit_lexical_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_LexicalStartSymbols[4] = new NonTerminalStackNode(178, 4 , "Symbols");
			prod___lit_lexical_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_LexicalStartSymbols[3] = new NonTerminalStackNode(174, 3 , new IMatchableStackNode[] {new CharStackNode(176, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_lexical_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_LexicalStartSymbols[2] = new LiteralStackNode(172, 2, prod___char_class___range__115_115_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_char_class___range__45_45_char_class___range__115_115_char_class___range__121_121_char_class___range__109_109_char_class___range__98_98_char_class___range__111_111_char_class___range__108_108_char_class___range__115_115_lit_start_symbols_attrs___literal , new char[] {115,116,97,114,116,45,115,121,109,98,111,108,115});
			prod___lit_lexical_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_LexicalStartSymbols[1] = new NonTerminalStackNode(58, 1 , new IMatchableStackNode[] {new CharStackNode(60, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_lexical_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_LexicalStartSymbols[0] = new LiteralStackNode(56, 0, prod___char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_lit_lexical_attrs___literal , new char[] {108,101,120,105,99,97,108});
		}
		public final static AbstractStackNode[] prod___ImpSection_Grammar_attrs___term__cons_ImpSection = new AbstractStackNode[1];
		static{
			prod___ImpSection_Grammar_attrs___term__cons_ImpSection[0] = new NonTerminalStackNode(180, 0 , "ImpSection");
		}
		public final static AbstractStackNode[] prod___lit_aliases_layouts_LAYOUTLIST_Aliases_Grammar_attrs___term__cons_Aliases = new AbstractStackNode[3];
		static{
			prod___lit_aliases_layouts_LAYOUTLIST_Aliases_Grammar_attrs___term__cons_Aliases[2] = new NonTerminalStackNode(188, 2 , "Aliases");
			prod___lit_aliases_layouts_LAYOUTLIST_Aliases_Grammar_attrs___term__cons_Aliases[1] = new NonTerminalStackNode(184, 1 , new IMatchableStackNode[] {new CharStackNode(186, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_aliases_layouts_LAYOUTLIST_Aliases_Grammar_attrs___term__cons_Aliases[0] = new LiteralStackNode(182, 0, prod___char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_char_class___range__115_115_lit_aliases_attrs___literal , new char[] {97,108,105,97,115,101,115});
		}
		public final static AbstractStackNode[] prod___lit_lexical_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_LexicalPriorities = new AbstractStackNode[5];
		static{
			prod___lit_lexical_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_LexicalPriorities[4] = new NonTerminalStackNode(202, 4 , "Priorities");
			prod___lit_lexical_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_LexicalPriorities[3] = new NonTerminalStackNode(198, 3 , new IMatchableStackNode[] {new CharStackNode(200, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_lexical_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_LexicalPriorities[2] = new LiteralStackNode(196, 2, prod___char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__111_111_char_class___range__114_114_char_class___range__105_105_char_class___range__116_116_char_class___range__105_105_char_class___range__101_101_char_class___range__115_115_lit_priorities_attrs___literal , new char[] {112,114,105,111,114,105,116,105,101,115});
			prod___lit_lexical_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_LexicalPriorities[1] = new NonTerminalStackNode(58, 1 , new IMatchableStackNode[] {new CharStackNode(60, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_lexical_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_LexicalPriorities[0] = new LiteralStackNode(56, 0, prod___char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_lit_lexical_attrs___literal , new char[] {108,101,120,105,99,97,108});
		}
		public final static AbstractStackNode[] prod___lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Syntax = new AbstractStackNode[3];
		static{
			prod___lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Syntax[2] = new NonTerminalStackNode(210, 2 , "Productions");
			prod___lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Syntax[1] = new NonTerminalStackNode(206, 1 , new IMatchableStackNode[] {new CharStackNode(208, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Syntax[0] = new LiteralStackNode(204, 0, prod___char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_lit_syntax_attrs___literal , new char[] {115,121,110,116,97,120});
		}
		public final static AbstractStackNode[] prod___lit_context_free_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_ContextFreeRestrictions = new AbstractStackNode[5];
		static{
			prod___lit_context_free_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_ContextFreeRestrictions[4] = new NonTerminalStackNode(224, 4 , "Restrictions");
			prod___lit_context_free_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_ContextFreeRestrictions[3] = new NonTerminalStackNode(220, 3 , new IMatchableStackNode[] {new CharStackNode(222, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_context_free_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_ContextFreeRestrictions[2] = new LiteralStackNode(218, 2, prod___char_class___range__114_114_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__105_105_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_lit_restrictions_attrs___literal , new char[] {114,101,115,116,114,105,99,116,105,111,110,115});
			prod___lit_context_free_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_ContextFreeRestrictions[1] = new NonTerminalStackNode(30, 1 , new IMatchableStackNode[] {new CharStackNode(32, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_context_free_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_ContextFreeRestrictions[0] = new LiteralStackNode(28, 0, prod___char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__45_45_char_class___range__102_102_char_class___range__114_114_char_class___range__101_101_char_class___range__101_101_lit_context_free_attrs___literal , new char[] {99,111,110,116,101,120,116,45,102,114,101,101});
		}
		public final static AbstractStackNode[] prod___lit___40_47_41_Grammar_attrs___term__cons_EmptyGrammar = new AbstractStackNode[1];
		static{
			prod___lit___40_47_41_Grammar_attrs___term__cons_EmptyGrammar[0] = new LiteralStackNode(226, 0, prod___char_class___range__40_40_char_class___range__47_47_char_class___range__41_41_lit___40_47_41_attrs___literal , new char[] {40,47,41});
		}
		public final static AbstractStackNode[] prod___lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_Restrictions = new AbstractStackNode[3];
		static{
			prod___lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_Restrictions[2] = new NonTerminalStackNode(234, 2 , "Restrictions");
			prod___lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_Restrictions[1] = new NonTerminalStackNode(230, 1 , new IMatchableStackNode[] {new CharStackNode(232, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_Restrictions[0] = new LiteralStackNode(228, 0, prod___char_class___range__114_114_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__105_105_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_lit_restrictions_attrs___literal , new char[] {114,101,115,116,114,105,99,116,105,111,110,115});
		}
	}
	
	private static class start__SDF {
		
		public final static AbstractStackNode[] prod___layouts_LAYOUTLIST_SDF_layouts_LAYOUTLIST_start__SDF_no_attrs = new AbstractStackNode[3];
		static{
			prod___layouts_LAYOUTLIST_SDF_layouts_LAYOUTLIST_start__SDF_no_attrs[2] = new NonTerminalStackNode(248, 2 , new IMatchableStackNode[] {new CharStackNode(250, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___layouts_LAYOUTLIST_SDF_layouts_LAYOUTLIST_start__SDF_no_attrs[1] = new NonTerminalStackNode(246, 1 , "SDF");
			prod___layouts_LAYOUTLIST_SDF_layouts_LAYOUTLIST_start__SDF_no_attrs[0] = new NonTerminalStackNode(242, 0 , new IMatchableStackNode[] {new CharStackNode(244, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
		}
	}
	
	private static class Module {
		
		public final static AbstractStackNode[] prod___lit_module_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_iter_star_seps__ImpSection__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Sections_Module_attrs___term__cons_Module = new AbstractStackNode[7];
		static{
			prod___lit_module_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_iter_star_seps__ImpSection__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Sections_Module_attrs___term__cons_Module[6] = new NonTerminalStackNode(294, 6 , "Sections");
			prod___lit_module_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_iter_star_seps__ImpSection__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Sections_Module_attrs___term__cons_Module[5] = new NonTerminalStackNode(290, 5 , new IMatchableStackNode[] {new CharStackNode(292, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_module_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_iter_star_seps__ImpSection__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Sections_Module_attrs___term__cons_Module[4] = new SeparatedListStackNode(282, 4, regular__iter_star_seps__ImpSection__layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(284, 0 , "ImpSection"), new AbstractStackNode[]{new NonTerminalStackNode(286, 1 , new IMatchableStackNode[] {new CharStackNode(288, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, false);
			prod___lit_module_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_iter_star_seps__ImpSection__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Sections_Module_attrs___term__cons_Module[3] = new NonTerminalStackNode(278, 3 , new IMatchableStackNode[] {new CharStackNode(280, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_module_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_iter_star_seps__ImpSection__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Sections_Module_attrs___term__cons_Module[2] = new NonTerminalStackNode(274, 2 , new IMatchableStackNode[] {new CharStackNode(276, 0, new char[][]{{45,45},{48,57},{65,90},{95,95},{97,122}})}, "ModuleName");
			prod___lit_module_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_iter_star_seps__ImpSection__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Sections_Module_attrs___term__cons_Module[1] = new NonTerminalStackNode(270, 1 , new IMatchableStackNode[] {new CharStackNode(272, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_module_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_iter_star_seps__ImpSection__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Sections_Module_attrs___term__cons_Module[0] = new LiteralStackNode(268, 0, prod___char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_lit_module_attrs___literal , new char[] {109,111,100,117,108,101});
		}
	}
	
	private static class OptExp {
		
		public final static AbstractStackNode[] prod___OptExp_attrs___term__cons_Absent = new AbstractStackNode[1];
		static{
			prod___OptExp_attrs___term__cons_Absent[0] = new EpsilonStackNode(304, 0);
		}
		public final static AbstractStackNode[] prod___lit_e_layouts_LAYOUTLIST_IntCon_OptExp_attrs___term__cons_Present = new AbstractStackNode[3];
		static{
			prod___lit_e_layouts_LAYOUTLIST_IntCon_OptExp_attrs___term__cons_Present[2] = new NonTerminalStackNode(312, 2 , "IntCon");
			prod___lit_e_layouts_LAYOUTLIST_IntCon_OptExp_attrs___term__cons_Present[1] = new NonTerminalStackNode(308, 1 , new IMatchableStackNode[] {new CharStackNode(310, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_e_layouts_LAYOUTLIST_IntCon_OptExp_attrs___term__cons_Present[0] = new LiteralStackNode(306, 0, prod___char_class___range__101_101_lit_e_attrs___literal , new char[] {101});
		}
	}
	
	private static class Renaming {
		
		public final static AbstractStackNode[] prod___Production_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Production_Renaming_attrs___term__cons_production = new AbstractStackNode[5];
		static{
			prod___Production_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Production_Renaming_attrs___term__cons_production[4] = new NonTerminalStackNode(372, 4 , "Production");
			prod___Production_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Production_Renaming_attrs___term__cons_production[3] = new NonTerminalStackNode(368, 3 , new IMatchableStackNode[] {new CharStackNode(370, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Production_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Production_Renaming_attrs___term__cons_production[2] = new LiteralStackNode(366, 2, prod___char_class___range__61_61_char_class___range__62_62_lit___61_62_attrs___literal , new char[] {61,62});
			prod___Production_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Production_Renaming_attrs___term__cons_production[1] = new NonTerminalStackNode(362, 1 , new IMatchableStackNode[] {new CharStackNode(364, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Production_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Production_Renaming_attrs___term__cons_production[0] = new NonTerminalStackNode(360, 0 , "Production");
		}
		public final static AbstractStackNode[] prod___Symbol_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_Renaming_attrs___term__cons_Symbol = new AbstractStackNode[5];
		static{
			prod___Symbol_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_Renaming_attrs___term__cons_Symbol[4] = new NonTerminalStackNode(386, 4 , "Symbol");
			prod___Symbol_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_Renaming_attrs___term__cons_Symbol[3] = new NonTerminalStackNode(382, 3 , new IMatchableStackNode[] {new CharStackNode(384, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Symbol_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_Renaming_attrs___term__cons_Symbol[2] = new LiteralStackNode(380, 2, prod___char_class___range__61_61_char_class___range__62_62_lit___61_62_attrs___literal , new char[] {61,62});
			prod___Symbol_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_Renaming_attrs___term__cons_Symbol[1] = new NonTerminalStackNode(376, 1 , new IMatchableStackNode[] {new CharStackNode(378, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Symbol_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_Renaming_attrs___term__cons_Symbol[0] = new NonTerminalStackNode(374, 0 , "Symbol");
		}
	}
	
	private static class ImpSection {
		
		public final static AbstractStackNode[] prod___lit_imports_layouts_LAYOUTLIST_Imports_ImpSection_attrs___term__cons_Imports = new AbstractStackNode[3];
		static{
			prod___lit_imports_layouts_LAYOUTLIST_Imports_ImpSection_attrs___term__cons_Imports[2] = new NonTerminalStackNode(394, 2 , "Imports");
			prod___lit_imports_layouts_LAYOUTLIST_Imports_ImpSection_attrs___term__cons_Imports[1] = new NonTerminalStackNode(390, 1 , new IMatchableStackNode[] {new CharStackNode(392, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_imports_layouts_LAYOUTLIST_Imports_ImpSection_attrs___term__cons_Imports[0] = new LiteralStackNode(388, 0, prod___char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__115_115_lit_imports_attrs___literal , new char[] {105,109,112,111,114,116,115});
		}
	}
	
	private static class CharClass {
		
		public final static AbstractStackNode[] prod___lit___91_layouts_LAYOUTLIST_OptCharRanges_layouts_LAYOUTLIST_lit___93_CharClass_attrs___term__cons_SimpleCharClass = new AbstractStackNode[5];
		static{
			prod___lit___91_layouts_LAYOUTLIST_OptCharRanges_layouts_LAYOUTLIST_lit___93_CharClass_attrs___term__cons_SimpleCharClass[4] = new LiteralStackNode(434, 4, prod___char_class___range__93_93_lit___93_attrs___literal , new char[] {93});
			prod___lit___91_layouts_LAYOUTLIST_OptCharRanges_layouts_LAYOUTLIST_lit___93_CharClass_attrs___term__cons_SimpleCharClass[3] = new NonTerminalStackNode(430, 3 , new IMatchableStackNode[] {new CharStackNode(432, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___91_layouts_LAYOUTLIST_OptCharRanges_layouts_LAYOUTLIST_lit___93_CharClass_attrs___term__cons_SimpleCharClass[2] = new NonTerminalStackNode(428, 2 , "OptCharRanges");
			prod___lit___91_layouts_LAYOUTLIST_OptCharRanges_layouts_LAYOUTLIST_lit___93_CharClass_attrs___term__cons_SimpleCharClass[1] = new NonTerminalStackNode(424, 1 , new IMatchableStackNode[] {new CharStackNode(426, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___91_layouts_LAYOUTLIST_OptCharRanges_layouts_LAYOUTLIST_lit___93_CharClass_attrs___term__cons_SimpleCharClass[0] = new LiteralStackNode(422, 0, prod___char_class___range__91_91_lit___91_attrs___literal , new char[] {91});
		}
		public final static AbstractStackNode[] prod___CharClass_layouts_LAYOUTLIST_lit___47_92_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_ISect = new AbstractStackNode[5];
		static{
			prod___CharClass_layouts_LAYOUTLIST_lit___47_92_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_ISect[4] = new NonTerminalStackNode(484, 4 , "CharClass");
			prod___CharClass_layouts_LAYOUTLIST_lit___47_92_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_ISect[3] = new NonTerminalStackNode(480, 3 , new IMatchableStackNode[] {new CharStackNode(482, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___CharClass_layouts_LAYOUTLIST_lit___47_92_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_ISect[2] = new LiteralStackNode(478, 2, prod___char_class___range__47_47_char_class___range__92_92_lit___47_92_attrs___literal , new char[] {47,92});
			prod___CharClass_layouts_LAYOUTLIST_lit___47_92_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_ISect[1] = new NonTerminalStackNode(474, 1 , new IMatchableStackNode[] {new CharStackNode(476, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___CharClass_layouts_LAYOUTLIST_lit___47_92_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_ISect[0] = new NonTerminalStackNode(472, 0 , "CharClass");
		}
		public final static AbstractStackNode[] prod___CharClass_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Diff = new AbstractStackNode[5];
		static{
			prod___CharClass_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Diff[4] = new NonTerminalStackNode(470, 4 , "CharClass");
			prod___CharClass_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Diff[3] = new NonTerminalStackNode(466, 3 , new IMatchableStackNode[] {new CharStackNode(468, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___CharClass_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Diff[2] = new LiteralStackNode(464, 2, prod___char_class___range__47_47_lit___47_attrs___literal , new char[] {47});
			prod___CharClass_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Diff[1] = new NonTerminalStackNode(474, 1 , new IMatchableStackNode[] {new CharStackNode(476, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___CharClass_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Diff[0] = new NonTerminalStackNode(472, 0 , "CharClass");
		}
		public final static AbstractStackNode[] prod___lit___126_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Comp = new AbstractStackNode[3];
		static{
			prod___lit___126_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Comp[2] = new NonTerminalStackNode(442, 2 , "CharClass");
			prod___lit___126_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Comp[1] = new NonTerminalStackNode(438, 1 , new IMatchableStackNode[] {new CharStackNode(440, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___126_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Comp[0] = new LiteralStackNode(436, 0, prod___char_class___range__126_126_lit___126_attrs___literal , new char[] {126});
		}
		public final static AbstractStackNode[] prod___lit___40_layouts_LAYOUTLIST_CharClass_layouts_LAYOUTLIST_lit___41_CharClass_attrs___term__cons_Bracket = new AbstractStackNode[5];
		static{
			prod___lit___40_layouts_LAYOUTLIST_CharClass_layouts_LAYOUTLIST_lit___41_CharClass_attrs___term__cons_Bracket[4] = new LiteralStackNode(456, 4, prod___char_class___range__41_41_lit___41_attrs___literal , new char[] {41});
			prod___lit___40_layouts_LAYOUTLIST_CharClass_layouts_LAYOUTLIST_lit___41_CharClass_attrs___term__cons_Bracket[3] = new NonTerminalStackNode(452, 3 , new IMatchableStackNode[] {new CharStackNode(454, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_CharClass_layouts_LAYOUTLIST_lit___41_CharClass_attrs___term__cons_Bracket[2] = new NonTerminalStackNode(450, 2 , "CharClass");
			prod___lit___40_layouts_LAYOUTLIST_CharClass_layouts_LAYOUTLIST_lit___41_CharClass_attrs___term__cons_Bracket[1] = new NonTerminalStackNode(446, 1 , new IMatchableStackNode[] {new CharStackNode(448, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_CharClass_layouts_LAYOUTLIST_lit___41_CharClass_attrs___term__cons_Bracket[0] = new LiteralStackNode(444, 0, prod___char_class___range__40_40_lit___40_attrs___literal , new char[] {40});
		}
		public final static AbstractStackNode[] prod___CharClass_layouts_LAYOUTLIST_lit___92_47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Union = new AbstractStackNode[5];
		static{
			prod___CharClass_layouts_LAYOUTLIST_lit___92_47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Union[4] = new NonTerminalStackNode(498, 4 , "CharClass");
			prod___CharClass_layouts_LAYOUTLIST_lit___92_47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Union[3] = new NonTerminalStackNode(494, 3 , new IMatchableStackNode[] {new CharStackNode(496, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___CharClass_layouts_LAYOUTLIST_lit___92_47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Union[2] = new LiteralStackNode(492, 2, prod___char_class___range__92_92_char_class___range__47_47_lit___92_47_attrs___literal , new char[] {92,47});
			prod___CharClass_layouts_LAYOUTLIST_lit___92_47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Union[1] = new NonTerminalStackNode(474, 1 , new IMatchableStackNode[] {new CharStackNode(476, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___CharClass_layouts_LAYOUTLIST_lit___92_47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Union[0] = new NonTerminalStackNode(472, 0 , "CharClass");
		}
	}
	
	private static class FunctionName {
		
		public final static AbstractStackNode[] prod___IdCon_FunctionName_attrs___term__cons_UnquotedFun = new AbstractStackNode[1];
		static{
			prod___IdCon_FunctionName_attrs___term__cons_UnquotedFun[0] = new NonTerminalStackNode(518, 0 , new IMatchableStackNode[] {new CharStackNode(520, 0, new char[][]{{45,45},{48,57},{65,90},{97,122}})}, "IdCon");
		}
		public final static AbstractStackNode[] prod___StrCon_FunctionName_attrs___term__cons_QuotedFun = new AbstractStackNode[1];
		static{
			prod___StrCon_FunctionName_attrs___term__cons_QuotedFun[0] = new NonTerminalStackNode(522, 0 , "StrCon");
		}
	}
	
	private static class SingleQuotedStrCon {
		
		public final static AbstractStackNode[] prod___char_class___range__39_39_SingleQuotedStrChar_char_class___range__39_39_SingleQuotedStrCon_attrs___term__cons_Default_lex = new AbstractStackNode[3];
		static{
			prod___char_class___range__39_39_SingleQuotedStrChar_char_class___range__39_39_SingleQuotedStrCon_attrs___term__cons_Default_lex[2] = new CharStackNode(540, 2, new char[][]{{39,39}});
			prod___char_class___range__39_39_SingleQuotedStrChar_char_class___range__39_39_SingleQuotedStrCon_attrs___term__cons_Default_lex[1] = new NonTerminalStackNode(538, 1 , "SingleQuotedStrChar");
			prod___char_class___range__39_39_SingleQuotedStrChar_char_class___range__39_39_SingleQuotedStrCon_attrs___term__cons_Default_lex[0] = new CharStackNode(536, 0, new char[][]{{39,39}});
		}
	}
	
	private static class Restriction {
		
		public final static AbstractStackNode[] prod___Symbols_layouts_LAYOUTLIST_lit___45_47_45_layouts_LAYOUTLIST_Lookaheads_Restriction_attrs___term__cons_Follow = new AbstractStackNode[5];
		static{
			prod___Symbols_layouts_LAYOUTLIST_lit___45_47_45_layouts_LAYOUTLIST_Lookaheads_Restriction_attrs___term__cons_Follow[4] = new NonTerminalStackNode(556, 4 , "Lookaheads");
			prod___Symbols_layouts_LAYOUTLIST_lit___45_47_45_layouts_LAYOUTLIST_Lookaheads_Restriction_attrs___term__cons_Follow[3] = new NonTerminalStackNode(552, 3 , new IMatchableStackNode[] {new CharStackNode(554, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Symbols_layouts_LAYOUTLIST_lit___45_47_45_layouts_LAYOUTLIST_Lookaheads_Restriction_attrs___term__cons_Follow[2] = new LiteralStackNode(550, 2, prod___char_class___range__45_45_char_class___range__47_47_char_class___range__45_45_lit___45_47_45_attrs___literal , new char[] {45,47,45});
			prod___Symbols_layouts_LAYOUTLIST_lit___45_47_45_layouts_LAYOUTLIST_Lookaheads_Restriction_attrs___term__cons_Follow[1] = new NonTerminalStackNode(546, 1 , new IMatchableStackNode[] {new CharStackNode(548, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Symbols_layouts_LAYOUTLIST_lit___45_47_45_layouts_LAYOUTLIST_Lookaheads_Restriction_attrs___term__cons_Follow[0] = new NonTerminalStackNode(544, 0 , "Symbols");
		}
	}
	
	private static class ModuleWord {
		
		public final static AbstractStackNode[] prod___iter__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_ModuleWord_attrs___term__cons_Word_lex = new AbstractStackNode[1];
		static{
			prod___iter__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_ModuleWord_attrs___term__cons_Word_lex[0] = new ListStackNode(592, 0, regular__iter__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_no_attrs , new CharStackNode(594, 0, new char[][]{{45,45},{48,57},{65,90},{95,95},{97,122}}), true);
		}
	}
	
	private static class ArgumentIndicator {
		
		public final static AbstractStackNode[] prod___lit___60_layouts_LAYOUTLIST_iter_seps__NatCon__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ArgumentIndicator_attrs___term__cons_Default = new AbstractStackNode[5];
		static{
			prod___lit___60_layouts_LAYOUTLIST_iter_seps__NatCon__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ArgumentIndicator_attrs___term__cons_Default[4] = new LiteralStackNode(632, 4, prod___char_class___range__62_62_lit___62_attrs___literal , new char[] {62});
			prod___lit___60_layouts_LAYOUTLIST_iter_seps__NatCon__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ArgumentIndicator_attrs___term__cons_Default[3] = new NonTerminalStackNode(628, 3 , new IMatchableStackNode[] {new CharStackNode(630, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_iter_seps__NatCon__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ArgumentIndicator_attrs___term__cons_Default[2] = new SeparatedListStackNode(612, 2, regular__iter_seps__NatCon__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(614, 0 , new IMatchableStackNode[] {new CharStackNode(616, 0, new char[][]{{48,57}})}, "NatCon"), new AbstractStackNode[]{new NonTerminalStackNode(618, 1 , new IMatchableStackNode[] {new CharStackNode(620, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST"), new LiteralStackNode(622, 2, prod___char_class___range__44_44_lit___44_attrs___literal , new char[] {44}), new NonTerminalStackNode(624, 3 , new IMatchableStackNode[] {new CharStackNode(626, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, true);
			prod___lit___60_layouts_LAYOUTLIST_iter_seps__NatCon__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ArgumentIndicator_attrs___term__cons_Default[1] = new NonTerminalStackNode(608, 1 , new IMatchableStackNode[] {new CharStackNode(610, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_iter_seps__NatCon__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ArgumentIndicator_attrs___term__cons_Default[0] = new LiteralStackNode(606, 0, prod___char_class___range__60_60_lit___60_attrs___literal , new char[] {60});
		}
	}
	
	private static class StrCon {
		
		public final static AbstractStackNode[] prod___char_class___range__34_34_iter_star__StrChar_char_class___range__34_34_StrCon_attrs___term__cons_Default_lex = new AbstractStackNode[3];
		static{
			prod___char_class___range__34_34_iter_star__StrChar_char_class___range__34_34_StrCon_attrs___term__cons_Default_lex[2] = new CharStackNode(656, 2, new char[][]{{34,34}});
			prod___char_class___range__34_34_iter_star__StrChar_char_class___range__34_34_StrCon_attrs___term__cons_Default_lex[1] = new ListStackNode(652, 1, regular__iter_star__StrChar_no_attrs , new NonTerminalStackNode(654, 0 , "StrChar"), false);
			prod___char_class___range__34_34_iter_star__StrChar_char_class___range__34_34_StrCon_attrs___term__cons_Default_lex[0] = new CharStackNode(650, 0, new char[][]{{34,34}});
		}
	}
	
	private static class Sort {
		
		public final static AbstractStackNode[] prod___char_class___range__65_90_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_char_class___range__48_57_range__65_90_range__97_122_Sort_attrs___term__cons_MoreChars_lex = new AbstractStackNode[3];
		static{
			prod___char_class___range__65_90_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_char_class___range__48_57_range__65_90_range__97_122_Sort_attrs___term__cons_MoreChars_lex[2] = new CharStackNode(664, 2, new char[][]{{48,57},{65,90},{97,122}});
			prod___char_class___range__65_90_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_char_class___range__48_57_range__65_90_range__97_122_Sort_attrs___term__cons_MoreChars_lex[1] = new ListStackNode(660, 1, regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_no_attrs , new CharStackNode(662, 0, new char[][]{{45,45},{48,57},{65,90},{97,122}}), false);
			prod___char_class___range__65_90_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_char_class___range__48_57_range__65_90_range__97_122_Sort_attrs___term__cons_MoreChars_lex[0] = new CharStackNode(658, 0, new char[][]{{65,90}});
		}
		public final static AbstractStackNode[] prod___lit_LAYOUT_Sort_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_LAYOUT_Sort_attrs___reject[0] = new LiteralStackNode(670, 0, prod___char_class___range__76_76_char_class___range__65_65_char_class___range__89_89_char_class___range__79_79_char_class___range__85_85_char_class___range__84_84_lit_LAYOUT_attrs___literal , new char[] {76,65,89,79,85,84});
		}
		public final static AbstractStackNode[] prod___char_class___range__65_90_Sort_attrs___term__cons_OneChar_lex = new AbstractStackNode[1];
		static{
			prod___char_class___range__65_90_Sort_attrs___term__cons_OneChar_lex[0] = new CharStackNode(658, 0, new char[][]{{65,90}});
		}
	}
	
	private static class OptCharRanges {
		
		public final static AbstractStackNode[] prod___CharRanges_OptCharRanges_attrs___term__cons_Present = new AbstractStackNode[1];
		static{
			prod___CharRanges_OptCharRanges_attrs___term__cons_Present[0] = new NonTerminalStackNode(672, 0 , "CharRanges");
		}
		public final static AbstractStackNode[] prod___OptCharRanges_attrs___term__cons_Absent = new AbstractStackNode[1];
		static{
			prod___OptCharRanges_attrs___term__cons_Absent[0] = new EpsilonStackNode(674, 0);
		}
	}
	
	private static class Productions {
		
		public final static AbstractStackNode[] prod___iter_star_seps__Production__layouts_LAYOUTLIST_Productions_no_attrs = new AbstractStackNode[1];
		static{
			prod___iter_star_seps__Production__layouts_LAYOUTLIST_Productions_no_attrs[0] = new SeparatedListStackNode(690, 0, regular__iter_star_seps__Production__layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(692, 0 , "Production"), new AbstractStackNode[]{new NonTerminalStackNode(694, 1 , new IMatchableStackNode[] {new CharStackNode(696, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, false);
		}
	}
	
	private static class Symbols {
		
		public final static AbstractStackNode[] prod___StrCon_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbols_attrs___reject = new AbstractStackNode[7];
		static{
			prod___StrCon_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbols_attrs___reject[6] = new LiteralStackNode(736, 6, prod___char_class___range__41_41_lit___41_attrs___literal , new char[] {41});
			prod___StrCon_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbols_attrs___reject[5] = new NonTerminalStackNode(732, 5 , new IMatchableStackNode[] {new CharStackNode(734, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___StrCon_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbols_attrs___reject[4] = new SeparatedListStackNode(718, 4, regular__iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(720, 0 , "Symbol"), new AbstractStackNode[]{new NonTerminalStackNode(722, 1 , new IMatchableStackNode[] {new CharStackNode(724, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST"), new LiteralStackNode(726, 2, prod___char_class___range__44_44_lit___44_attrs___literal , new char[] {44}), new NonTerminalStackNode(728, 3 , new IMatchableStackNode[] {new CharStackNode(730, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, false);
			prod___StrCon_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbols_attrs___reject[3] = new NonTerminalStackNode(714, 3 , new IMatchableStackNode[] {new CharStackNode(716, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___StrCon_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbols_attrs___reject[2] = new LiteralStackNode(712, 2, prod___char_class___range__40_40_lit___40_attrs___literal , new char[] {40});
			prod___StrCon_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbols_attrs___reject[1] = new NonTerminalStackNode(708, 1 , new IMatchableStackNode[] {new CharStackNode(710, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___StrCon_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbols_attrs___reject[0] = new NonTerminalStackNode(706, 0 , "StrCon");
		}
		public final static AbstractStackNode[] prod___iter_star_seps__Symbol__layouts_LAYOUTLIST_Symbols_no_attrs = new AbstractStackNode[1];
		static{
			prod___iter_star_seps__Symbol__layouts_LAYOUTLIST_Symbols_no_attrs[0] = new SeparatedListStackNode(698, 0, regular__iter_star_seps__Symbol__layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(700, 0 , "Symbol"), new AbstractStackNode[]{new NonTerminalStackNode(702, 1 , new IMatchableStackNode[] {new CharStackNode(704, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, false);
		}
	}
	
private static class Symbol {
		
		public final static AbstractStackNode[] prod___lit___40_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Empty = new AbstractStackNode[3];
		static{
			prod___lit___40_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Empty[2] = new LiteralStackNode(756, 2, prod___char_class___range__41_41_lit___41_attrs___literal , new char[] {41});
			prod___lit___40_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Empty[1] = new NonTerminalStackNode(760, 1 , new IMatchableStackNode[] {new CharStackNode(762, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Empty[0] = new LiteralStackNode(758, 0, prod___char_class___range__40_40_lit___40_attrs___literal , new char[] {40});
		}
		public final static AbstractStackNode[] prod___lit___40_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Func = new AbstractStackNode[9];
		static{
			prod___lit___40_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Func[0] = new LiteralStackNode(758, 0, prod___char_class___range__40_40_lit___40_attrs___literal , new char[] {40});
			prod___lit___40_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Func[1] = new NonTerminalStackNode(760, 1 , new IMatchableStackNode[] {new CharStackNode(762, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Func[2] = new NonTerminalStackNode(764, 2 , "Symbols");
			prod___lit___40_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Func[3] = new NonTerminalStackNode(766, 3 , new IMatchableStackNode[] {new CharStackNode(768, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Func[4] = new LiteralStackNode(770, 4, prod___char_class___range__61_61_char_class___range__62_62_lit___61_62_attrs___literal , new char[] {61,62});
			prod___lit___40_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Func[5] = new NonTerminalStackNode(772, 5 , new IMatchableStackNode[] {new CharStackNode(774, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Func[6] = new NonTerminalStackNode(776, 6 , "Symbol");
			prod___lit___40_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Func[7] = new NonTerminalStackNode(778, 7 , new IMatchableStackNode[] {new CharStackNode(780, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Func[8] = new LiteralStackNode(782, 8, prod___char_class___range__41_41_lit___41_attrs___literal , new char[] {41});
		}
		public final static AbstractStackNode[] prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep = new AbstractStackNode[9];
		static{
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep[8] = new LiteralStackNode(808, 8, prod___char_class___range__42_42_lit___42_attrs___literal , new char[] {42});
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep[7] = new NonTerminalStackNode(804, 7 , new IMatchableStackNode[] {new CharStackNode(806, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep[6] = new LiteralStackNode(802, 6, prod___char_class___range__125_125_lit___125_attrs___literal , new char[] {125});
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep[5] = new NonTerminalStackNode(798, 5 , new IMatchableStackNode[] {new CharStackNode(800, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep[4] = new NonTerminalStackNode(796, 4 , "Symbol");
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep[3] = new NonTerminalStackNode(792, 3 , new IMatchableStackNode[] {new CharStackNode(794, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep[2] = new NonTerminalStackNode(790, 2 , "Symbol");
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep[1] = new NonTerminalStackNode(786, 1 , new IMatchableStackNode[] {new CharStackNode(788, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep[0] = new LiteralStackNode(784, 0, prod___char_class___range__123_123_lit___123_attrs___literal , new char[] {123});
		}
		public final static AbstractStackNode[] prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Bracket = new AbstractStackNode[5];
		static{
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Bracket[0] = new LiteralStackNode(758, 0, prod___char_class___range__40_40_lit___40_attrs___literal , new char[] {40});
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Bracket[1] = new NonTerminalStackNode(760, 1 , new IMatchableStackNode[] {new CharStackNode(762, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Bracket[2] = new NonTerminalStackNode(868, 2 , "Symbol");
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Bracket[3] = new NonTerminalStackNode(870, 3 , new IMatchableStackNode[] {new CharStackNode(872, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Bracket[4] = new LiteralStackNode(874, 4, prod___char_class___range__41_41_lit___41_attrs___literal , new char[] {41});
		}
		public final static AbstractStackNode[] prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Seq = new AbstractStackNode[7];
		static{
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Seq[6] = new LiteralStackNode(834, 6, prod___char_class___range__41_41_lit___41_attrs___literal , new char[] {41});
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Seq[5] = new NonTerminalStackNode(830, 5 , new IMatchableStackNode[] {new CharStackNode(832, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Seq[4] = new SeparatedListStackNode(822, 4, regular__iter_seps__Symbol__layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(824, 0 , "Symbol"), new AbstractStackNode[]{new NonTerminalStackNode(826, 1 , new IMatchableStackNode[] {new CharStackNode(828, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, true);
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Seq[3] = new NonTerminalStackNode(870, 3 , new IMatchableStackNode[] {new CharStackNode(872, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Seq[2] = new NonTerminalStackNode(868, 2 , "Symbol");
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Seq[1] = new NonTerminalStackNode(760, 1 , new IMatchableStackNode[] {new CharStackNode(762, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Seq[0] = new LiteralStackNode(758, 0, prod___char_class___range__40_40_lit___40_attrs___literal , new char[] {40});
		}
		public final static AbstractStackNode[] prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Strategy = new AbstractStackNode[9];
		static{
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Strategy[8] = new LiteralStackNode(860, 8, prod___char_class___range__41_41_lit___41_attrs___literal , new char[] {41});
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Strategy[7] = new NonTerminalStackNode(856, 7 , new IMatchableStackNode[] {new CharStackNode(858, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Strategy[6] = new NonTerminalStackNode(854, 6 , "Symbol");
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Strategy[5] = new NonTerminalStackNode(850, 5 , new IMatchableStackNode[] {new CharStackNode(852, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Strategy[0] = new LiteralStackNode(758, 0, prod___char_class___range__40_40_lit___40_attrs___literal , new char[] {40});
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Strategy[1] = new NonTerminalStackNode(760, 1 , new IMatchableStackNode[] {new CharStackNode(762, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Strategy[2] = new NonTerminalStackNode(868, 2 , "Symbol");
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Strategy[3] = new NonTerminalStackNode(870, 3 , new IMatchableStackNode[] {new CharStackNode(872, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Strategy[4] = new LiteralStackNode(848, 4, prod___char_class___range__45_45_char_class___range__62_62_lit___45_62_attrs___literal , new char[] {45,62});
		}
		public final static AbstractStackNode[] prod___lit___60_83_84_65_82_84_62_Symbol_attrs___term__cons_Start = new AbstractStackNode[1];
		static{
			prod___lit___60_83_84_65_82_84_62_Symbol_attrs___term__cons_Start[0] = new LiteralStackNode(876, 0, prod___char_class___range__60_60_char_class___range__83_83_char_class___range__84_84_char_class___range__65_65_char_class___range__82_82_char_class___range__84_84_char_class___range__62_62_lit___60_83_84_65_82_84_62_attrs___literal , new char[] {60,83,84,65,82,84,62});
		}
		public final static AbstractStackNode[] prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep = new AbstractStackNode[9];
		static{
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep[8] = new LiteralStackNode(902, 8, prod___char_class___range__43_43_lit___43_attrs___literal , new char[] {43});
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep[7] = new NonTerminalStackNode(804, 7 , new IMatchableStackNode[] {new CharStackNode(806, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep[6] = new LiteralStackNode(802, 6, prod___char_class___range__125_125_lit___125_attrs___literal , new char[] {125});
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep[5] = new NonTerminalStackNode(798, 5 , new IMatchableStackNode[] {new CharStackNode(800, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep[4] = new NonTerminalStackNode(796, 4 , "Symbol");
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep[3] = new NonTerminalStackNode(792, 3 , new IMatchableStackNode[] {new CharStackNode(794, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep[2] = new NonTerminalStackNode(790, 2 , "Symbol");
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep[1] = new NonTerminalStackNode(786, 1 , new IMatchableStackNode[] {new CharStackNode(788, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep[0] = new LiteralStackNode(784, 0, prod___char_class___range__123_123_lit___123_attrs___literal , new char[] {123});
		}
		public final static AbstractStackNode[] prod___CharClass_Symbol_attrs___term__cons_CharClass = new AbstractStackNode[1];
		static{
			prod___CharClass_Symbol_attrs___term__cons_CharClass[0] = new NonTerminalStackNode(904, 0 , "CharClass");
		}
		public final static AbstractStackNode[] prod___StrCon_Symbol_attrs___term__cons_Lit = new AbstractStackNode[1];
		static{
			prod___StrCon_Symbol_attrs___term__cons_Lit[0] = new NonTerminalStackNode(906, 0 , "StrCon");
		}
		public final static AbstractStackNode[] prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs = new AbstractStackNode[5];
		static{
			prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs[4] = new NonTerminalStackNode(1114, 4 , "Symbol");
			prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs[3] = new NonTerminalStackNode(1110, 3 , new IMatchableStackNode[] {new CharStackNode(1112, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs[2] = new LiteralStackNode(1108, 2, prod___char_class___range__58_58_lit___58_attrs___literal , new char[] {58});
			prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs[1] = new NonTerminalStackNode(1104, 1 , new IMatchableStackNode[] {new CharStackNode(1106, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs[0] = new NonTerminalStackNode(1102, 0 , "Label");
		}
		public final static AbstractStackNode[] prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter = new AbstractStackNode[3];
		static{
			prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter[2] = new LiteralStackNode(914, 2, prod___char_class___range__43_43_lit___43_attrs___literal , new char[] {43});
			prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter[1] = new NonTerminalStackNode(918, 1 , new IMatchableStackNode[] {new CharStackNode(920, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter[0] = new NonTerminalStackNode(916, 0 , "Symbol");
		}
		public final static AbstractStackNode[] prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt = new AbstractStackNode[3];
		static{
			prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt[2] = new LiteralStackNode(922, 2, prod___char_class___range__63_63_lit___63_attrs___literal , new char[] {63});
			prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt[1] = new NonTerminalStackNode(918, 1 , new IMatchableStackNode[] {new CharStackNode(920, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt[0] = new NonTerminalStackNode(916, 0 , "Symbol");
		}
		public final static AbstractStackNode[] prod___SingleQuotedStrCon_Symbol_attrs___term__cons_CILit = new AbstractStackNode[1];
		static{
			prod___SingleQuotedStrCon_Symbol_attrs___term__cons_CILit[0] = new NonTerminalStackNode(924, 0 , "SingleQuotedStrCon");
		}
		public final static AbstractStackNode[] prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right = new AbstractStackNode[5];
		static{
			prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right[4] = new NonTerminalStackNode(1100, 4 , "Symbol");
			prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right[3] = new NonTerminalStackNode(1096, 3 , new IMatchableStackNode[] {new CharStackNode(1098, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right[2] = new LiteralStackNode(1094, 2, prod___char_class___range__124_124_lit___124_attrs___literal , new char[] {124});
			prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right[1] = new NonTerminalStackNode(1090, 1 , new IMatchableStackNode[] {new CharStackNode(1092, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right[0] = new NonTerminalStackNode(1088, 0 , "Symbol");
		}
		public final static AbstractStackNode[] prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__CF_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_CF = new AbstractStackNode[7];
		static{
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__CF_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_CF[6] = new LiteralStackNode(964, 6, prod___char_class___range__62_62_lit___62_attrs___literal , new char[] {62});
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__CF_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_CF[5] = new NonTerminalStackNode(960, 5 , new IMatchableStackNode[] {new CharStackNode(962, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__CF_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_CF[4] = new LiteralStackNode(958, 4, prod___char_class___range__45_45_char_class___range__67_67_char_class___range__70_70_lit__CF_attrs___literal , new char[] {45,67,70});
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__CF_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_CF[3] = new NonTerminalStackNode(954, 3 , new IMatchableStackNode[] {new CharStackNode(956, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__CF_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_CF[2] = new NonTerminalStackNode(952, 2 , "Symbol");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__CF_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_CF[1] = new NonTerminalStackNode(948, 1 , new IMatchableStackNode[] {new CharStackNode(950, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__CF_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_CF[0] = new LiteralStackNode(946, 0, prod___char_class___range__60_60_lit___60_attrs___literal , new char[] {60});
		}
		public final static AbstractStackNode[] prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__LEX_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Lex = new AbstractStackNode[7];
		static{
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__LEX_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Lex[6] = new LiteralStackNode(944, 6, prod___char_class___range__62_62_lit___62_attrs___literal , new char[] {62});
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__LEX_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Lex[5] = new NonTerminalStackNode(940, 5 , new IMatchableStackNode[] {new CharStackNode(942, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__LEX_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Lex[4] = new LiteralStackNode(938, 4, prod___char_class___range__45_45_char_class___range__76_76_char_class___range__69_69_char_class___range__88_88_lit__LEX_attrs___literal , new char[] {45,76,69,88});
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__LEX_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Lex[3] = new NonTerminalStackNode(954, 3 , new IMatchableStackNode[] {new CharStackNode(956, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__LEX_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Lex[2] = new NonTerminalStackNode(952, 2 , "Symbol");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__LEX_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Lex[1] = new NonTerminalStackNode(948, 1 , new IMatchableStackNode[] {new CharStackNode(950, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__LEX_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Lex[0] = new LiteralStackNode(946, 0, prod___char_class___range__60_60_lit___60_attrs___literal , new char[] {60});
		}
		public final static AbstractStackNode[] prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Tuple = new AbstractStackNode[9];
		static{
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Tuple[8] = new LiteralStackNode(1036, 8, prod___char_class___range__62_62_lit___62_attrs___literal , new char[] {62});
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Tuple[7] = new NonTerminalStackNode(1032, 7 , new IMatchableStackNode[] {new CharStackNode(1034, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Tuple[6] = new SeparatedListStackNode(1018, 6, regular__iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(1020, 0 , "Symbol"), new AbstractStackNode[]{new NonTerminalStackNode(1022, 1 , new IMatchableStackNode[] {new CharStackNode(1024, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST"), new LiteralStackNode(1026, 2, prod___char_class___range__44_44_lit___44_attrs___literal , new char[] {44}), new NonTerminalStackNode(1028, 3 , new IMatchableStackNode[] {new CharStackNode(1030, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, true);
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Tuple[5] = new NonTerminalStackNode(1014, 5 , new IMatchableStackNode[] {new CharStackNode(1016, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Tuple[4] = new LiteralStackNode(1012, 4, prod___char_class___range__44_44_lit___44_attrs___literal , new char[] {44});
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Tuple[3] = new NonTerminalStackNode(954, 3 , new IMatchableStackNode[] {new CharStackNode(956, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Tuple[2] = new NonTerminalStackNode(952, 2 , "Symbol");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Tuple[1] = new NonTerminalStackNode(948, 1 , new IMatchableStackNode[] {new CharStackNode(950, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Tuple[0] = new LiteralStackNode(946, 0, prod___char_class___range__60_60_lit___60_attrs___literal , new char[] {60});
		}
		public final static AbstractStackNode[] prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort = new AbstractStackNode[7];
		static{
			prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort[6] = new LiteralStackNode(998, 6, prod___char_class___range__93_93_char_class___range__93_93_lit___93_93_attrs___literal , new char[] {93,93});
			prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort[5] = new NonTerminalStackNode(994, 5 , new IMatchableStackNode[] {new CharStackNode(996, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort[4] = new SeparatedListStackNode(980, 4, regular__iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(982, 0 , "Symbol"), new AbstractStackNode[]{new NonTerminalStackNode(984, 1 , new IMatchableStackNode[] {new CharStackNode(986, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST"), new LiteralStackNode(988, 2, prod___char_class___range__44_44_lit___44_attrs___literal , new char[] {44}), new NonTerminalStackNode(990, 3 , new IMatchableStackNode[] {new CharStackNode(992, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, true);
			prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort[3] = new NonTerminalStackNode(976, 3 , new IMatchableStackNode[] {new CharStackNode(978, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort[2] = new LiteralStackNode(974, 2, prod___char_class___range__91_91_char_class___range__91_91_lit___91_91_attrs___literal , new char[] {91,91});
			prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort[1] = new NonTerminalStackNode(970, 1 , new IMatchableStackNode[] {new CharStackNode(972, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort[0] = new NonTerminalStackNode(966, 0 , new IMatchableStackNode[] {new CharStackNode(968, 0, new char[][]{{48,57},{65,90},{97,122}})}, "Sort");
		}
		public final static AbstractStackNode[] prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__VAR_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Varsym = new AbstractStackNode[7];
		static{
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__VAR_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Varsym[6] = new LiteralStackNode(1064, 6, prod___char_class___range__62_62_lit___62_attrs___literal , new char[] {62});
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__VAR_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Varsym[5] = new NonTerminalStackNode(1060, 5 , new IMatchableStackNode[] {new CharStackNode(1062, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__VAR_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Varsym[4] = new LiteralStackNode(1058, 4, prod___char_class___range__45_45_char_class___range__86_86_char_class___range__65_65_char_class___range__82_82_lit__VAR_attrs___literal , new char[] {45,86,65,82});
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__VAR_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Varsym[3] = new NonTerminalStackNode(954, 3 , new IMatchableStackNode[] {new CharStackNode(956, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__VAR_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Varsym[2] = new NonTerminalStackNode(952, 2 , "Symbol");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__VAR_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Varsym[1] = new NonTerminalStackNode(948, 1 , new IMatchableStackNode[] {new CharStackNode(950, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__VAR_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Varsym[0] = new LiteralStackNode(946, 0, prod___char_class___range__60_60_lit___60_attrs___literal , new char[] {60});
		}
		public final static AbstractStackNode[] prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar = new AbstractStackNode[3];
		static{
			prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar[2] = new LiteralStackNode(1044, 2, prod___char_class___range__42_42_lit___42_attrs___literal , new char[] {42});
			prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar[1] = new NonTerminalStackNode(918, 1 , new IMatchableStackNode[] {new CharStackNode(920, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar[0] = new NonTerminalStackNode(916, 0 , "Symbol");
		}
		public final static AbstractStackNode[] prod___lit___96_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___96_Symbol_attrs___term__cons_Lifting = new AbstractStackNode[5];
		static{
			prod___lit___96_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___96_Symbol_attrs___term__cons_Lifting[4] = new LiteralStackNode(1082, 4, prod___char_class___range__96_96_lit___96_attrs___literal , new char[] {96});
			prod___lit___96_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___96_Symbol_attrs___term__cons_Lifting[3] = new NonTerminalStackNode(1078, 3 , new IMatchableStackNode[] {new CharStackNode(1080, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___96_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___96_Symbol_attrs___term__cons_Lifting[2] = new NonTerminalStackNode(1076, 2 , "Symbol");
			prod___lit___96_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___96_Symbol_attrs___term__cons_Lifting[1] = new NonTerminalStackNode(1072, 1 , new IMatchableStackNode[] {new CharStackNode(1074, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___96_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___96_Symbol_attrs___term__cons_Lifting[0] = new LiteralStackNode(1070, 0, prod___char_class___range__96_96_lit___96_attrs___literal , new char[] {96});
		}
		public final static AbstractStackNode[] prod___Sort_Symbol_attrs___term__cons_Sort = new AbstractStackNode[1];
		static{
			prod___Sort_Symbol_attrs___term__cons_Sort[0] = new NonTerminalStackNode(1066, 0 , new IMatchableStackNode[] {new CharStackNode(1068, 0, new char[][]{{48,57},{65,90},{97,122}})}, "Sort");
		}
		public final static AbstractStackNode[] prod___lit_LAYOUT_Symbol_attrs___term__cons_Layout = new AbstractStackNode[1];
		static{
			prod___lit_LAYOUT_Symbol_attrs___term__cons_Layout[0] = new LiteralStackNode(1084, 0, prod___char_class___range__76_76_char_class___range__65_65_char_class___range__89_89_char_class___range__79_79_char_class___range__85_85_char_class___range__84_84_lit_LAYOUT_attrs___literal , new char[] {76,65,89,79,85,84});
		}
		public final static AbstractStackNode[] prod___lit___60_83_116_97_114_116_62_Symbol_attrs___term__cons_FileStart = new AbstractStackNode[1];
		static{
			prod___lit___60_83_116_97_114_116_62_Symbol_attrs___term__cons_FileStart[0] = new LiteralStackNode(1086, 0, prod___char_class___range__60_60_char_class___range__83_83_char_class___range__116_116_char_class___range__97_97_char_class___range__114_114_char_class___range__116_116_char_class___range__62_62_lit___60_83_116_97_114_116_62_attrs___literal , new char[] {60,83,116,97,114,116,62});
		}
	}
	
	private static class ModuleId {
		
		public final static AbstractStackNode[] prod___ModuleWord_lit___47_ModuleId_ModuleId_attrs___term__cons_Path_lex = new AbstractStackNode[3];
		static{
			prod___ModuleWord_lit___47_ModuleId_ModuleId_attrs___term__cons_Path_lex[2] = new NonTerminalStackNode(1134, 2 , new IMatchableStackNode[] {new CharStackNode(1136, 0, new char[][]{{47,47}})}, "ModuleId");
			prod___ModuleWord_lit___47_ModuleId_ModuleId_attrs___term__cons_Path_lex[1] = new LiteralStackNode(1132, 1, prod___char_class___range__47_47_lit___47_attrs___literal , new char[] {47});
			prod___ModuleWord_lit___47_ModuleId_ModuleId_attrs___term__cons_Path_lex[0] = new NonTerminalStackNode(1128, 0 , new IMatchableStackNode[] {new CharStackNode(1130, 0, new char[][]{{45,45},{48,57},{65,90},{95,95},{97,122}})}, "ModuleWord");
		}
		public final static AbstractStackNode[] prod___lit___47_ModuleId_ModuleId_attrs___term__cons_Root_lex = new AbstractStackNode[2];
		static{
			prod___lit___47_ModuleId_ModuleId_attrs___term__cons_Root_lex[1] = new NonTerminalStackNode(1140, 1 , new IMatchableStackNode[] {new CharStackNode(1142, 0, new char[][]{{47,47}})}, "ModuleId");
			prod___lit___47_ModuleId_ModuleId_attrs___term__cons_Root_lex[0] = new LiteralStackNode(1138, 0, prod___char_class___range__47_47_lit___47_attrs___literal , new char[] {47});
		}
		public final static AbstractStackNode[] prod___ModuleWord_ModuleId_attrs___term__cons_Leaf_lex = new AbstractStackNode[1];
		static{
			prod___ModuleWord_ModuleId_attrs___term__cons_Leaf_lex[0] = new NonTerminalStackNode(1128, 0 , new IMatchableStackNode[] {new CharStackNode(1130, 0, new char[][]{{45,45},{48,57},{65,90},{95,95},{97,122}})}, "ModuleWord");
		}
	}
	
	private static class Priority {
		
		public final static AbstractStackNode[] prod___Group_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_Group_Priority_attrs___term__cons_Assoc = new AbstractStackNode[5];
		static{
			prod___Group_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_Group_Priority_attrs___term__cons_Assoc[4] = new NonTerminalStackNode(1162, 4 , "Group");
			prod___Group_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_Group_Priority_attrs___term__cons_Assoc[3] = new NonTerminalStackNode(1158, 3 , new IMatchableStackNode[] {new CharStackNode(1160, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Group_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_Group_Priority_attrs___term__cons_Assoc[2] = new NonTerminalStackNode(1156, 2 , "Associativity");
			prod___Group_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_Group_Priority_attrs___term__cons_Assoc[1] = new NonTerminalStackNode(1152, 1 , new IMatchableStackNode[] {new CharStackNode(1154, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Group_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_Group_Priority_attrs___term__cons_Assoc[0] = new NonTerminalStackNode(1150, 0 , "Group");
		}
		public final static AbstractStackNode[] prod___iter_seps__Group__layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_Priority_attrs___term__cons_Chain = new AbstractStackNode[1];
		static{
			prod___iter_seps__Group__layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_Priority_attrs___term__cons_Chain[0] = new SeparatedListStackNode(1164, 0, regular__iter_seps__Group__layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(1166, 0 , "Group"), new AbstractStackNode[]{new NonTerminalStackNode(1168, 1 , new IMatchableStackNode[] {new CharStackNode(1170, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST"), new LiteralStackNode(1172, 2, prod___char_class___range__62_62_lit___62_attrs___literal , new char[] {62}), new NonTerminalStackNode(1174, 3 , new IMatchableStackNode[] {new CharStackNode(1176, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, true);
		}
	}
	
	private static class Section {
		
		public final static AbstractStackNode[] prod___lit_exports_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Exports = new AbstractStackNode[3];
		static{
			prod___lit_exports_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Exports[2] = new NonTerminalStackNode(1224, 2 , "Grammar");
			prod___lit_exports_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Exports[1] = new NonTerminalStackNode(1220, 1 , new IMatchableStackNode[] {new CharStackNode(1222, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_exports_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Exports[0] = new LiteralStackNode(1218, 0, prod___char_class___range__101_101_char_class___range__120_120_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__115_115_lit_exports_attrs___literal , new char[] {101,120,112,111,114,116,115});
		}
		public final static AbstractStackNode[] prod___lit_hiddens_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Hiddens = new AbstractStackNode[3];
		static{
			prod___lit_hiddens_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Hiddens[2] = new NonTerminalStackNode(1232, 2 , "Grammar");
			prod___lit_hiddens_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Hiddens[1] = new NonTerminalStackNode(1228, 1 , new IMatchableStackNode[] {new CharStackNode(1230, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_hiddens_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Hiddens[0] = new LiteralStackNode(1226, 0, prod___char_class___range__104_104_char_class___range__105_105_char_class___range__100_100_char_class___range__100_100_char_class___range__101_101_char_class___range__110_110_char_class___range__115_115_lit_hiddens_attrs___literal , new char[] {104,105,100,100,101,110,115});
		}
	}
	
	private static class Import {
		
		public final static AbstractStackNode[] prod___ModuleName_Import_attrs___term__cons_Module = new AbstractStackNode[1];
		static{
			prod___ModuleName_Import_attrs___term__cons_Module[0] = new NonTerminalStackNode(1190, 0 , new IMatchableStackNode[] {new CharStackNode(1192, 0, new char[][]{{45,45},{48,57},{65,90},{95,95},{97,122}})}, "ModuleName");
		}
		public final static AbstractStackNode[] prod___lit___40_layouts_LAYOUTLIST_Import_layouts_LAYOUTLIST_lit___41_Import_attrs___term__cons_Bracket = new AbstractStackNode[5];
		static{
			prod___lit___40_layouts_LAYOUTLIST_Import_layouts_LAYOUTLIST_lit___41_Import_attrs___term__cons_Bracket[4] = new LiteralStackNode(1206, 4, prod___char_class___range__41_41_lit___41_attrs___literal , new char[] {41});
			prod___lit___40_layouts_LAYOUTLIST_Import_layouts_LAYOUTLIST_lit___41_Import_attrs___term__cons_Bracket[3] = new NonTerminalStackNode(1202, 3 , new IMatchableStackNode[] {new CharStackNode(1204, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Import_layouts_LAYOUTLIST_lit___41_Import_attrs___term__cons_Bracket[2] = new NonTerminalStackNode(1200, 2 , "Import");
			prod___lit___40_layouts_LAYOUTLIST_Import_layouts_LAYOUTLIST_lit___41_Import_attrs___term__cons_Bracket[1] = new NonTerminalStackNode(1196, 1 , new IMatchableStackNode[] {new CharStackNode(1198, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Import_layouts_LAYOUTLIST_lit___41_Import_attrs___term__cons_Bracket[0] = new LiteralStackNode(1194, 0, prod___char_class___range__40_40_lit___40_attrs___literal , new char[] {40});
		}
		public final static AbstractStackNode[] prod___ModuleName_layouts_LAYOUTLIST_Renamings_Import_attrs___term__cons_RenamedModule = new AbstractStackNode[3];
		static{
			prod___ModuleName_layouts_LAYOUTLIST_Renamings_Import_attrs___term__cons_RenamedModule[2] = new NonTerminalStackNode(1216, 2 , "Renamings");
			prod___ModuleName_layouts_LAYOUTLIST_Renamings_Import_attrs___term__cons_RenamedModule[1] = new NonTerminalStackNode(1212, 1 , new IMatchableStackNode[] {new CharStackNode(1214, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___ModuleName_layouts_LAYOUTLIST_Renamings_Import_attrs___term__cons_RenamedModule[0] = new NonTerminalStackNode(1190, 0 , new IMatchableStackNode[] {new CharStackNode(1192, 0, new char[][]{{45,45},{48,57},{65,90},{95,95},{97,122}})}, "ModuleName");
		}
	}
	
	private static class NatCon {
		
		public final static AbstractStackNode[] prod___iter__char_class___range__48_57_NatCon_attrs___term__cons_Digits_lex = new AbstractStackNode[1];
		static{
			prod___iter__char_class___range__48_57_NatCon_attrs___term__cons_Digits_lex[0] = new ListStackNode(1258, 0, regular__iter__char_class___range__48_57_no_attrs , new CharStackNode(1260, 0, new char[][]{{48,57}}), true);
		}
	}
	
	private static class ATermAttribute {
		
		public final static AbstractStackNode[] prod___lit_avoid_ATermAttribute_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_avoid_ATermAttribute_attrs___reject[0] = new LiteralStackNode(1280, 0, prod___char_class___range__97_97_char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_lit_avoid_attrs___literal , new char[] {97,118,111,105,100});
		}
		public final static AbstractStackNode[] prod___lit_prefer_ATermAttribute_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_prefer_ATermAttribute_attrs___reject[0] = new LiteralStackNode(1282, 0, prod___char_class___range__112_112_char_class___range__114_114_char_class___range__101_101_char_class___range__102_102_char_class___range__101_101_char_class___range__114_114_lit_prefer_attrs___literal , new char[] {112,114,101,102,101,114});
		}
		public final static AbstractStackNode[] prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_ATermAttribute_attrs___reject = new AbstractStackNode[7];
		static{
			prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_ATermAttribute_attrs___reject[6] = new LiteralStackNode(1304, 6, prod___char_class___range__41_41_lit___41_attrs___literal , new char[] {41});
			prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_ATermAttribute_attrs___reject[5] = new NonTerminalStackNode(1300, 5 , new IMatchableStackNode[] {new CharStackNode(1302, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_ATermAttribute_attrs___reject[4] = new NonTerminalStackNode(1296, 4 , new IMatchableStackNode[] {new CharStackNode(1298, 0, new char[][]{{45,45},{48,57},{65,90},{95,95},{97,122}})}, "ModuleName");
			prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_ATermAttribute_attrs___reject[3] = new NonTerminalStackNode(1292, 3 , new IMatchableStackNode[] {new CharStackNode(1294, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_ATermAttribute_attrs___reject[2] = new LiteralStackNode(1290, 2, prod___char_class___range__40_40_lit___40_attrs___literal , new char[] {40});
			prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_ATermAttribute_attrs___reject[1] = new NonTerminalStackNode(1286, 1 , new IMatchableStackNode[] {new CharStackNode(1288, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_ATermAttribute_attrs___reject[0] = new LiteralStackNode(1284, 0, prod___char_class___range__105_105_char_class___range__100_100_lit_id_attrs___literal , new char[] {105,100});
		}
		public final static AbstractStackNode[] prod___lit_reject_ATermAttribute_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_reject_ATermAttribute_attrs___reject[0] = new LiteralStackNode(1306, 0, prod___char_class___range__114_114_char_class___range__101_101_char_class___range__106_106_char_class___range__101_101_char_class___range__99_99_char_class___range__116_116_lit_reject_attrs___literal , new char[] {114,101,106,101,99,116});
		}
		public final static AbstractStackNode[] prod___Associativity_ATermAttribute_attrs___reject = new AbstractStackNode[1];
		static{
			prod___Associativity_ATermAttribute_attrs___reject[0] = new NonTerminalStackNode(1308, 0 , "Associativity");
		}
		public final static AbstractStackNode[] prod___ATerm_ATermAttribute_attrs___term__cons_Default = new AbstractStackNode[1];
		static{
			prod___ATerm_ATermAttribute_attrs___term__cons_Default[0] = new NonTerminalStackNode(1278, 0 , "ATerm");
		}
		public final static AbstractStackNode[] prod___lit_bracket_ATermAttribute_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_bracket_ATermAttribute_attrs___reject[0] = new LiteralStackNode(1310, 0, prod___char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_lit_bracket_attrs___literal , new char[] {98,114,97,99,107,101,116});
		}
	}
	
	private static class Label {
		
		public final static AbstractStackNode[] prod___IdCon_Label_attrs___term__cons_IdCon = new AbstractStackNode[1];
		static{
			prod___IdCon_Label_attrs___term__cons_IdCon[0] = new NonTerminalStackNode(1328, 0 , new IMatchableStackNode[] {new CharStackNode(1330, 0, new char[][]{{45,45},{48,57},{65,90},{97,122}})}, "IdCon");
		}
		public final static AbstractStackNode[] prod___StrCon_Label_attrs___term__cons_Quoted = new AbstractStackNode[1];
		static{
			prod___StrCon_Label_attrs___term__cons_Quoted[0] = new NonTerminalStackNode(1332, 0 , "StrCon");
		}
		public final static AbstractStackNode[] prod___Associativity_Label_attrs___reject = new AbstractStackNode[1];
		static{
			prod___Associativity_Label_attrs___reject[0] = new NonTerminalStackNode(1334, 0 , "Associativity");
		}
	}
	
	private static class ShortChar {
		
		public final static AbstractStackNode[] prod___char_class___range__92_92_char_class___range__0_47_range__58_64_range__91_96_range__110_110_range__114_114_range__116_116_range__123_65535_ShortChar_attrs___term__cons_Escaped_lex = new AbstractStackNode[2];
		static{
			prod___char_class___range__92_92_char_class___range__0_47_range__58_64_range__91_96_range__110_110_range__114_114_range__116_116_range__123_65535_ShortChar_attrs___term__cons_Escaped_lex[1] = new CharStackNode(1346, 1, new char[][]{{0,47},{58,64},{91,96},{110,110},{114,114},{116,116},{123,65535}});
			prod___char_class___range__92_92_char_class___range__0_47_range__58_64_range__91_96_range__110_110_range__114_114_range__116_116_range__123_65535_ShortChar_attrs___term__cons_Escaped_lex[0] = new CharStackNode(1344, 0, new char[][]{{92,92}});
		}
		public final static AbstractStackNode[] prod___char_class___range__48_57_range__65_90_range__97_122_ShortChar_attrs___term__cons_Regular_lex = new AbstractStackNode[1];
		static{
			prod___char_class___range__48_57_range__65_90_range__97_122_ShortChar_attrs___term__cons_Regular_lex[0] = new CharStackNode(1348, 0, new char[][]{{48,57},{65,90},{97,122}});
		}
	}
	
	private static class Aliases {
		
		public final static AbstractStackNode[] prod___iter_star_seps__Alias__layouts_LAYOUTLIST_Aliases_no_attrs = new AbstractStackNode[1];
		static{
			prod___iter_star_seps__Alias__layouts_LAYOUTLIST_Aliases_no_attrs[0] = new SeparatedListStackNode(1358, 0, regular__iter_star_seps__Alias__layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(1360, 0 , "Alias"), new AbstractStackNode[]{new NonTerminalStackNode(1362, 1 , new IMatchableStackNode[] {new CharStackNode(1364, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, false);
		}
	}
	
	private static class NumChar {
		
		public final static AbstractStackNode[] prod___char_class___range__92_92_iter__char_class___range__48_57_NumChar_attrs___term__cons_Digits_lex = new AbstractStackNode[2];
		static{
			prod___char_class___range__92_92_iter__char_class___range__48_57_NumChar_attrs___term__cons_Digits_lex[1] = new ListStackNode(1368, 1, regular__iter__char_class___range__48_57_no_attrs , new CharStackNode(1370, 0, new char[][]{{48,57}}), true);
			prod___char_class___range__92_92_iter__char_class___range__48_57_NumChar_attrs___term__cons_Digits_lex[0] = new CharStackNode(1366, 0, new char[][]{{92,92}});
		}
	}
	
	private static class Production {
		
		public final static AbstractStackNode[] prod___Symbols_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_Prod = new AbstractStackNode[7];
		static{
			prod___Symbols_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_Prod[6] = new NonTerminalStackNode(1406, 6 , "Attributes");
			prod___Symbols_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_Prod[5] = new NonTerminalStackNode(1402, 5 , new IMatchableStackNode[] {new CharStackNode(1404, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Symbols_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_Prod[4] = new NonTerminalStackNode(1400, 4 , "Symbol");
			prod___Symbols_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_Prod[3] = new NonTerminalStackNode(1396, 3 , new IMatchableStackNode[] {new CharStackNode(1398, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Symbols_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_Prod[2] = new LiteralStackNode(1394, 2, prod___char_class___range__45_45_char_class___range__62_62_lit___45_62_attrs___literal , new char[] {45,62});
			prod___Symbols_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_Prod[1] = new NonTerminalStackNode(1390, 1 , new IMatchableStackNode[] {new CharStackNode(1392, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Symbols_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_Prod[0] = new NonTerminalStackNode(1388, 0 , "Symbols");
		}
		public final static AbstractStackNode[] prod___FunctionName_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_PrefixFun = new AbstractStackNode[9];
		static{
			prod___FunctionName_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_PrefixFun[8] = new NonTerminalStackNode(1444, 8 , "Attributes");
			prod___FunctionName_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_PrefixFun[7] = new NonTerminalStackNode(1440, 7 , new IMatchableStackNode[] {new CharStackNode(1442, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___FunctionName_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_PrefixFun[6] = new LiteralStackNode(1438, 6, prod___char_class___range__41_41_lit___41_attrs___literal , new char[] {41});
			prod___FunctionName_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_PrefixFun[5] = new NonTerminalStackNode(1434, 5 , new IMatchableStackNode[] {new CharStackNode(1436, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___FunctionName_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_PrefixFun[4] = new SeparatedListStackNode(1420, 4, regular__iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(1422, 0 , "Symbol"), new AbstractStackNode[]{new NonTerminalStackNode(1424, 1 , new IMatchableStackNode[] {new CharStackNode(1426, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST"), new LiteralStackNode(1428, 2, prod___char_class___range__44_44_lit___44_attrs___literal , new char[] {44}), new NonTerminalStackNode(1430, 3 , new IMatchableStackNode[] {new CharStackNode(1432, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, false);
			prod___FunctionName_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_PrefixFun[3] = new NonTerminalStackNode(1416, 3 , new IMatchableStackNode[] {new CharStackNode(1418, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___FunctionName_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_PrefixFun[2] = new LiteralStackNode(1414, 2, prod___char_class___range__40_40_lit___40_attrs___literal , new char[] {40});
			prod___FunctionName_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_PrefixFun[1] = new NonTerminalStackNode(1410, 1 , new IMatchableStackNode[] {new CharStackNode(1412, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___FunctionName_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_PrefixFun[0] = new NonTerminalStackNode(1408, 0 , "FunctionName");
		}
	}
	
	private static class RealCon {
		
		public final static AbstractStackNode[] prod___IntCon_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_NatCon_layouts_LAYOUTLIST_OptExp_RealCon_attrs___term__cons_RealCon = new AbstractStackNode[7];
		static{
			prod___IntCon_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_NatCon_layouts_LAYOUTLIST_OptExp_RealCon_attrs___term__cons_RealCon[6] = new NonTerminalStackNode(1470, 6 , "OptExp");
			prod___IntCon_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_NatCon_layouts_LAYOUTLIST_OptExp_RealCon_attrs___term__cons_RealCon[5] = new NonTerminalStackNode(1466, 5 , new IMatchableStackNode[] {new CharStackNode(1468, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___IntCon_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_NatCon_layouts_LAYOUTLIST_OptExp_RealCon_attrs___term__cons_RealCon[4] = new NonTerminalStackNode(1462, 4 , new IMatchableStackNode[] {new CharStackNode(1464, 0, new char[][]{{48,57}})}, "NatCon");
			prod___IntCon_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_NatCon_layouts_LAYOUTLIST_OptExp_RealCon_attrs___term__cons_RealCon[3] = new NonTerminalStackNode(1458, 3 , new IMatchableStackNode[] {new CharStackNode(1460, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___IntCon_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_NatCon_layouts_LAYOUTLIST_OptExp_RealCon_attrs___term__cons_RealCon[2] = new LiteralStackNode(1456, 2, prod___char_class___range__46_46_lit___46_attrs___literal , new char[] {46});
			prod___IntCon_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_NatCon_layouts_LAYOUTLIST_OptExp_RealCon_attrs___term__cons_RealCon[1] = new NonTerminalStackNode(1452, 1 , new IMatchableStackNode[] {new CharStackNode(1454, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___IntCon_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_NatCon_layouts_LAYOUTLIST_OptExp_RealCon_attrs___term__cons_RealCon[0] = new NonTerminalStackNode(1450, 0 , "IntCon");
		}
	}
	
	private static class CharRanges {
		
		public final static AbstractStackNode[] prod___CharRange_CharRanges_no_attrs = new AbstractStackNode[1];
		static{
			prod___CharRange_CharRanges_no_attrs[0] = new NonTerminalStackNode(1498, 0 , "CharRange");
		}
		public final static AbstractStackNode[] prod___CharRanges_layouts_LAYOUTLIST_CharRanges_CharRanges_attrs___term__cons_Conc_assoc__right = new AbstractStackNode[3];
		static{
			prod___CharRanges_layouts_LAYOUTLIST_CharRanges_CharRanges_attrs___term__cons_Conc_assoc__right[2] = new NonTerminalStackNode(1506, 2 , "CharRanges");
			prod___CharRanges_layouts_LAYOUTLIST_CharRanges_CharRanges_attrs___term__cons_Conc_assoc__right[1] = new NonTerminalStackNode(1502, 1 , new IMatchableStackNode[] {new CharStackNode(1504, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___CharRanges_layouts_LAYOUTLIST_CharRanges_CharRanges_attrs___term__cons_Conc_assoc__right[0] = new NonTerminalStackNode(1500, 0 , "CharRanges");
		}
		public final static AbstractStackNode[] prod___lit___40_layouts_LAYOUTLIST_CharRanges_layouts_LAYOUTLIST_lit___41_CharRanges_attrs___term__cons_Bracket = new AbstractStackNode[5];
		static{
			prod___lit___40_layouts_LAYOUTLIST_CharRanges_layouts_LAYOUTLIST_lit___41_CharRanges_attrs___term__cons_Bracket[4] = new LiteralStackNode(1520, 4, prod___char_class___range__41_41_lit___41_attrs___literal , new char[] {41});
			prod___lit___40_layouts_LAYOUTLIST_CharRanges_layouts_LAYOUTLIST_lit___41_CharRanges_attrs___term__cons_Bracket[3] = new NonTerminalStackNode(1516, 3 , new IMatchableStackNode[] {new CharStackNode(1518, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_CharRanges_layouts_LAYOUTLIST_lit___41_CharRanges_attrs___term__cons_Bracket[2] = new NonTerminalStackNode(1514, 2 , "CharRanges");
			prod___lit___40_layouts_LAYOUTLIST_CharRanges_layouts_LAYOUTLIST_lit___41_CharRanges_attrs___term__cons_Bracket[1] = new NonTerminalStackNode(1510, 1 , new IMatchableStackNode[] {new CharStackNode(1512, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_CharRanges_layouts_LAYOUTLIST_lit___41_CharRanges_attrs___term__cons_Bracket[0] = new LiteralStackNode(1508, 0, prod___char_class___range__40_40_lit___40_attrs___literal , new char[] {40});
		}
	}
	
	private static class StrChar {
		
		public final static AbstractStackNode[] prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_StrChar_attrs___term__cons_Decimal_lex = new AbstractStackNode[4];
		static{
			prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_StrChar_attrs___term__cons_Decimal_lex[3] = new CharStackNode(1620, 3, new char[][]{{48,57}});
			prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_StrChar_attrs___term__cons_Decimal_lex[2] = new CharStackNode(1618, 2, new char[][]{{48,57}});
			prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_StrChar_attrs___term__cons_Decimal_lex[1] = new CharStackNode(1616, 1, new char[][]{{48,57}});
			prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_StrChar_attrs___term__cons_Decimal_lex[0] = new CharStackNode(1614, 0, new char[][]{{92,92}});
		}
		public final static AbstractStackNode[] prod___char_class___range__92_92_char_class___range__110_110_StrChar_attrs___term__cons_NewLine_lex = new AbstractStackNode[2];
		static{
			prod___char_class___range__92_92_char_class___range__110_110_StrChar_attrs___term__cons_NewLine_lex[1] = new CharStackNode(1624, 1, new char[][]{{110,110}});
			prod___char_class___range__92_92_char_class___range__110_110_StrChar_attrs___term__cons_NewLine_lex[0] = new CharStackNode(1622, 0, new char[][]{{92,92}});
		}
		public final static AbstractStackNode[] prod___char_class___range__0_8_range__11_33_range__35_91_range__93_65535_StrChar_attrs___term__cons_Normal_lex = new AbstractStackNode[1];
		static{
			prod___char_class___range__0_8_range__11_33_range__35_91_range__93_65535_StrChar_attrs___term__cons_Normal_lex[0] = new CharStackNode(1626, 0, new char[][]{{0,8},{11,33},{35,91},{93,65535}});
		}
		public final static AbstractStackNode[] prod___char_class___range__92_92_char_class___range__92_92_StrChar_attrs___term__cons_Backslash_lex = new AbstractStackNode[2];
		static{
			prod___char_class___range__92_92_char_class___range__92_92_StrChar_attrs___term__cons_Backslash_lex[1] = new CharStackNode(1630, 1, new char[][]{{92,92}});
			prod___char_class___range__92_92_char_class___range__92_92_StrChar_attrs___term__cons_Backslash_lex[0] = new CharStackNode(1628, 0, new char[][]{{92,92}});
		}
		public final static AbstractStackNode[] prod___char_class___range__92_92_char_class___range__34_34_StrChar_attrs___term__cons_Quote_lex = new AbstractStackNode[2];
		static{
			prod___char_class___range__92_92_char_class___range__34_34_StrChar_attrs___term__cons_Quote_lex[1] = new CharStackNode(1634, 1, new char[][]{{34,34}});
			prod___char_class___range__92_92_char_class___range__34_34_StrChar_attrs___term__cons_Quote_lex[0] = new CharStackNode(1632, 0, new char[][]{{92,92}});
		}
		public final static AbstractStackNode[] prod___char_class___range__92_92_char_class___range__116_116_StrChar_attrs___term__cons_Tab_lex = new AbstractStackNode[2];
		static{
			prod___char_class___range__92_92_char_class___range__116_116_StrChar_attrs___term__cons_Tab_lex[1] = new CharStackNode(1638, 1, new char[][]{{116,116}});
			prod___char_class___range__92_92_char_class___range__116_116_StrChar_attrs___term__cons_Tab_lex[0] = new CharStackNode(1636, 0, new char[][]{{92,92}});
		}
	}
	
	private static class Character {
		
		public final static AbstractStackNode[] prod___lit___92_69_79_70_Character_attrs___term__cons_eof = new AbstractStackNode[1];
		static{
			prod___lit___92_69_79_70_Character_attrs___term__cons_eof[0] = new LiteralStackNode(1666, 0, prod___char_class___range__92_92_char_class___range__69_69_char_class___range__79_79_char_class___range__70_70_lit___92_69_79_70_attrs___literal , new char[] {92,69,79,70});
		}
		public final static AbstractStackNode[] prod___lit___92_66_79_84_Character_attrs___term__cons_bot = new AbstractStackNode[1];
		static{
			prod___lit___92_66_79_84_Character_attrs___term__cons_bot[0] = new LiteralStackNode(1668, 0, prod___char_class___range__92_92_char_class___range__66_66_char_class___range__79_79_char_class___range__84_84_lit___92_66_79_84_attrs___literal , new char[] {92,66,79,84});
		}
		public final static AbstractStackNode[] prod___NumChar_Character_attrs___term__cons_Numeric = new AbstractStackNode[1];
		static{
			prod___NumChar_Character_attrs___term__cons_Numeric[0] = new NonTerminalStackNode(1670, 0 , new IMatchableStackNode[] {new CharStackNode(1672, 0, new char[][]{{48,57}})}, "NumChar");
		}
		public final static AbstractStackNode[] prod___ShortChar_Character_attrs___term__cons_short = new AbstractStackNode[1];
		static{
			prod___ShortChar_Character_attrs___term__cons_short[0] = new NonTerminalStackNode(1674, 0 , "ShortChar");
		}
		public final static AbstractStackNode[] prod___lit___92_84_79_80_Character_attrs___term__cons_top = new AbstractStackNode[1];
		static{
			prod___lit___92_84_79_80_Character_attrs___term__cons_top[0] = new LiteralStackNode(1676, 0, prod___char_class___range__92_92_char_class___range__84_84_char_class___range__79_79_char_class___range__80_80_lit___92_84_79_80_attrs___literal , new char[] {92,84,79,80});
		}
		public final static AbstractStackNode[] prod___lit___92_76_65_66_69_76_95_83_84_65_82_84_Character_attrs___term__cons_label_start = new AbstractStackNode[1];
		static{
			prod___lit___92_76_65_66_69_76_95_83_84_65_82_84_Character_attrs___term__cons_label_start[0] = new LiteralStackNode(1678, 0, prod___char_class___range__92_92_char_class___range__76_76_char_class___range__65_65_char_class___range__66_66_char_class___range__69_69_char_class___range__76_76_char_class___range__95_95_char_class___range__83_83_char_class___range__84_84_char_class___range__65_65_char_class___range__82_82_char_class___range__84_84_lit___92_76_65_66_69_76_95_83_84_65_82_84_attrs___literal , new char[] {92,76,65,66,69,76,95,83,84,65,82,84});
		}
	}
	
	private static class layouts_LAYOUTLIST {
		
		public final static AbstractStackNode[] prod___iter_star__LAYOUT_layouts_LAYOUTLIST_no_attrs = new AbstractStackNode[1];
		static{
			prod___iter_star__LAYOUT_layouts_LAYOUTLIST_no_attrs[0] = new ListStackNode(1680, 0, regular__iter_star__LAYOUT_no_attrs , new NonTerminalStackNode(1682, 0 , "LAYOUT"), false);
			prod___iter_star__LAYOUT_layouts_LAYOUTLIST_no_attrs[0].markAsLayout();
		}
	}
	
	private static class SingleQuotedStrChar {
		
		public final static AbstractStackNode[] prod___char_class___range__0_8_range__11_38_range__40_91_range__93_65535_SingleQuotedStrChar_attrs___term__cons_Normal_lex = new AbstractStackNode[1];
		static{
			prod___char_class___range__0_8_range__11_38_range__40_91_range__93_65535_SingleQuotedStrChar_attrs___term__cons_Normal_lex[0] = new CharStackNode(1686, 0, new char[][]{{0,8},{11,38},{40,91},{93,65535}});
		}
		public final static AbstractStackNode[] prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_SingleQuotedStrChar_attrs___term__cons_Decimal_lex = new AbstractStackNode[4];
		static{
			prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_SingleQuotedStrChar_attrs___term__cons_Decimal_lex[3] = new CharStackNode(1694, 3, new char[][]{{48,57}});
			prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_SingleQuotedStrChar_attrs___term__cons_Decimal_lex[2] = new CharStackNode(1692, 2, new char[][]{{48,57}});
			prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_SingleQuotedStrChar_attrs___term__cons_Decimal_lex[1] = new CharStackNode(1690, 1, new char[][]{{48,57}});
			prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_SingleQuotedStrChar_attrs___term__cons_Decimal_lex[0] = new CharStackNode(1688, 0, new char[][]{{92,92}});
		}
		public final static AbstractStackNode[] prod___char_class___range__92_92_char_class___range__39_39_SingleQuotedStrChar_attrs___term__cons_Quote_lex = new AbstractStackNode[2];
		static{
			prod___char_class___range__92_92_char_class___range__39_39_SingleQuotedStrChar_attrs___term__cons_Quote_lex[1] = new CharStackNode(1698, 1, new char[][]{{39,39}});
			prod___char_class___range__92_92_char_class___range__39_39_SingleQuotedStrChar_attrs___term__cons_Quote_lex[0] = new CharStackNode(1696, 0, new char[][]{{92,92}});
		}
		public final static AbstractStackNode[] prod___char_class___range__92_92_char_class___range__110_110_SingleQuotedStrChar_attrs___term__cons_NewLine_lex = new AbstractStackNode[2];
		static{
			prod___char_class___range__92_92_char_class___range__110_110_SingleQuotedStrChar_attrs___term__cons_NewLine_lex[1] = new CharStackNode(1702, 1, new char[][]{{110,110}});
			prod___char_class___range__92_92_char_class___range__110_110_SingleQuotedStrChar_attrs___term__cons_NewLine_lex[0] = new CharStackNode(1700, 0, new char[][]{{92,92}});
		}
		public final static AbstractStackNode[] prod___char_class___range__92_92_char_class___range__92_92_SingleQuotedStrChar_attrs___term__cons_Backslash_lex = new AbstractStackNode[2];
		static{
			prod___char_class___range__92_92_char_class___range__92_92_SingleQuotedStrChar_attrs___term__cons_Backslash_lex[1] = new CharStackNode(1706, 1, new char[][]{{92,92}});
			prod___char_class___range__92_92_char_class___range__92_92_SingleQuotedStrChar_attrs___term__cons_Backslash_lex[0] = new CharStackNode(1704, 0, new char[][]{{92,92}});
		}
		public final static AbstractStackNode[] prod___char_class___range__92_92_char_class___range__116_116_SingleQuotedStrChar_attrs___term__cons_Tab_lex = new AbstractStackNode[2];
		static{
			prod___char_class___range__92_92_char_class___range__116_116_SingleQuotedStrChar_attrs___term__cons_Tab_lex[1] = new CharStackNode(1710, 1, new char[][]{{116,116}});
			prod___char_class___range__92_92_char_class___range__116_116_SingleQuotedStrChar_attrs___term__cons_Tab_lex[0] = new CharStackNode(1708, 0, new char[][]{{92,92}});
		}
	}
	
	private static class SDF {
		
		public final static AbstractStackNode[] prod___EMPTY_layouts_LAYOUTLIST_lit_definition_layouts_LAYOUTLIST_Definition_layouts_LAYOUTLIST_EMPTY_SDF_attrs___term__cons_Definition = new AbstractStackNode[7];
		static{
			prod___EMPTY_layouts_LAYOUTLIST_lit_definition_layouts_LAYOUTLIST_Definition_layouts_LAYOUTLIST_EMPTY_SDF_attrs___term__cons_Definition[6] = new NonTerminalStackNode(1730, 6 , "EMPTY");
			prod___EMPTY_layouts_LAYOUTLIST_lit_definition_layouts_LAYOUTLIST_Definition_layouts_LAYOUTLIST_EMPTY_SDF_attrs___term__cons_Definition[5] = new NonTerminalStackNode(1726, 5 , new IMatchableStackNode[] {new CharStackNode(1728, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___EMPTY_layouts_LAYOUTLIST_lit_definition_layouts_LAYOUTLIST_Definition_layouts_LAYOUTLIST_EMPTY_SDF_attrs___term__cons_Definition[4] = new NonTerminalStackNode(1724, 4 , "Definition");
			prod___EMPTY_layouts_LAYOUTLIST_lit_definition_layouts_LAYOUTLIST_Definition_layouts_LAYOUTLIST_EMPTY_SDF_attrs___term__cons_Definition[3] = new NonTerminalStackNode(1720, 3 , new IMatchableStackNode[] {new CharStackNode(1722, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___EMPTY_layouts_LAYOUTLIST_lit_definition_layouts_LAYOUTLIST_Definition_layouts_LAYOUTLIST_EMPTY_SDF_attrs___term__cons_Definition[2] = new LiteralStackNode(1718, 2, prod___char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__105_105_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_lit_definition_attrs___literal , new char[] {100,101,102,105,110,105,116,105,111,110});
			prod___EMPTY_layouts_LAYOUTLIST_lit_definition_layouts_LAYOUTLIST_Definition_layouts_LAYOUTLIST_EMPTY_SDF_attrs___term__cons_Definition[1] = new NonTerminalStackNode(1714, 1 , new IMatchableStackNode[] {new CharStackNode(1716, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___EMPTY_layouts_LAYOUTLIST_lit_definition_layouts_LAYOUTLIST_Definition_layouts_LAYOUTLIST_EMPTY_SDF_attrs___term__cons_Definition[0] = new NonTerminalStackNode(1712, 0 , "EMPTY");
		}
	}
	
	private static class start__Module {
		
		public final static AbstractStackNode[] prod___layouts_LAYOUTLIST_Module_layouts_LAYOUTLIST_start__Module_no_attrs = new AbstractStackNode[3];
		static{
			prod___layouts_LAYOUTLIST_Module_layouts_LAYOUTLIST_start__Module_no_attrs[2] = new NonTerminalStackNode(1788, 2 , new IMatchableStackNode[] {new CharStackNode(1790, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___layouts_LAYOUTLIST_Module_layouts_LAYOUTLIST_start__Module_no_attrs[1] = new NonTerminalStackNode(1786, 1 , "Module");
			prod___layouts_LAYOUTLIST_Module_layouts_LAYOUTLIST_start__Module_no_attrs[0] = new NonTerminalStackNode(1782, 0 , new IMatchableStackNode[] {new CharStackNode(1784, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
		}
	}
	
	private static class EMPTY {
		
		public final static AbstractStackNode[] prod___EMPTY_no_attrs = new AbstractStackNode[1];
		static{
			prod___EMPTY_no_attrs[0] = new EpsilonStackNode(1798, 0);
		}
	}
	
	private static class LAYOUT {
		
		public final static AbstractStackNode[] prod___lit___37_37_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10_LAYOUT_attrs___term__cons_Line_lex = new AbstractStackNode[3];
		static{
			prod___lit___37_37_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10_LAYOUT_attrs___term__cons_Line_lex[2] = new CharStackNode(1820, 2, new char[][]{{10,10}});
			prod___lit___37_37_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10_LAYOUT_attrs___term__cons_Line_lex[1] = new ListStackNode(1816, 1, regular__iter_star__char_class___range__0_9_range__11_65535_no_attrs , new CharStackNode(1818, 0, new char[][]{{0,9},{11,65535}}), false);
			prod___lit___37_37_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10_LAYOUT_attrs___term__cons_Line_lex[0] = new LiteralStackNode(1814, 0, prod___char_class___range__37_37_char_class___range__37_37_lit___37_37_attrs___literal , new char[] {37,37});
		}
		public final static AbstractStackNode[] prod___char_class___range__9_10_range__13_13_range__32_32_LAYOUT_attrs___term__cons_Whitespace_lex = new AbstractStackNode[1];
		static{
			prod___char_class___range__9_10_range__13_13_range__32_32_LAYOUT_attrs___term__cons_Whitespace_lex[0] = new CharStackNode(1822, 0, new char[][]{{9,10},{13,13},{32,32}});
		}
		public final static AbstractStackNode[] prod___lit___37_char_class___range__0_9_range__11_36_range__38_65535_lit___37_LAYOUT_attrs___term__cons_Nested_lex = new AbstractStackNode[3];
		static{
			prod___lit___37_char_class___range__0_9_range__11_36_range__38_65535_lit___37_LAYOUT_attrs___term__cons_Nested_lex[2] = new LiteralStackNode(1828, 2, prod___char_class___range__37_37_lit___37_attrs___literal , new char[] {37});
			prod___lit___37_char_class___range__0_9_range__11_36_range__38_65535_lit___37_LAYOUT_attrs___term__cons_Nested_lex[1] = new CharStackNode(1826, 1, new char[][]{{0,9},{11,36},{38,65535}});
			prod___lit___37_char_class___range__0_9_range__11_36_range__38_65535_lit___37_LAYOUT_attrs___term__cons_Nested_lex[0] = new LiteralStackNode(1824, 0, prod___char_class___range__37_37_lit___37_attrs___literal , new char[] {37});
		}
	}
	
	private static class ModuleName {
		
		public final static AbstractStackNode[] prod___lit_context_free_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_context_free_ModuleName_attrs___reject[0] = new LiteralStackNode(1858, 0, prod___char_class___range__99_99_char_class___range__111_111_char_class___range__110_110_char_class___range__116_116_char_class___range__101_101_char_class___range__120_120_char_class___range__116_116_char_class___range__45_45_char_class___range__102_102_char_class___range__114_114_char_class___range__101_101_char_class___range__101_101_lit_context_free_attrs___literal , new char[] {99,111,110,116,101,120,116,45,102,114,101,101});
		}
		public final static AbstractStackNode[] prod___lit_bracket_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_bracket_ModuleName_attrs___reject[0] = new LiteralStackNode(1860, 0, prod___char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_lit_bracket_attrs___literal , new char[] {98,114,97,99,107,101,116});
		}
		public final static AbstractStackNode[] prod___lit_hiddens_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_hiddens_ModuleName_attrs___reject[0] = new LiteralStackNode(1862, 0, prod___char_class___range__104_104_char_class___range__105_105_char_class___range__100_100_char_class___range__100_100_char_class___range__101_101_char_class___range__110_110_char_class___range__115_115_lit_hiddens_attrs___literal , new char[] {104,105,100,100,101,110,115});
		}
		public final static AbstractStackNode[] prod___lit_imports_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_imports_ModuleName_attrs___reject[0] = new LiteralStackNode(1864, 0, prod___char_class___range__105_105_char_class___range__109_109_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__115_115_lit_imports_attrs___literal , new char[] {105,109,112,111,114,116,115});
		}
		public final static AbstractStackNode[] prod___lit_module_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_module_ModuleName_attrs___reject[0] = new LiteralStackNode(1866, 0, prod___char_class___range__109_109_char_class___range__111_111_char_class___range__100_100_char_class___range__117_117_char_class___range__108_108_char_class___range__101_101_lit_module_attrs___literal , new char[] {109,111,100,117,108,101});
		}
		public final static AbstractStackNode[] prod___lit_priorities_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_priorities_ModuleName_attrs___reject[0] = new LiteralStackNode(1868, 0, prod___char_class___range__112_112_char_class___range__114_114_char_class___range__105_105_char_class___range__111_111_char_class___range__114_114_char_class___range__105_105_char_class___range__116_116_char_class___range__105_105_char_class___range__101_101_char_class___range__115_115_lit_priorities_attrs___literal , new char[] {112,114,105,111,114,105,116,105,101,115});
		}
		public final static AbstractStackNode[] prod___lit_syntax_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_syntax_ModuleName_attrs___reject[0] = new LiteralStackNode(1870, 0, prod___char_class___range__115_115_char_class___range__121_121_char_class___range__110_110_char_class___range__116_116_char_class___range__97_97_char_class___range__120_120_lit_syntax_attrs___literal , new char[] {115,121,110,116,97,120});
		}
		public final static AbstractStackNode[] prod___lit_restrictions_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_restrictions_ModuleName_attrs___reject[0] = new LiteralStackNode(1872, 0, prod___char_class___range__114_114_char_class___range__101_101_char_class___range__115_115_char_class___range__116_116_char_class___range__114_114_char_class___range__105_105_char_class___range__99_99_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_char_class___range__115_115_lit_restrictions_attrs___literal , new char[] {114,101,115,116,114,105,99,116,105,111,110,115});
		}
		public final static AbstractStackNode[] prod___lit_left_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_left_ModuleName_attrs___reject[0] = new LiteralStackNode(1874, 0, prod___char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_lit_left_attrs___literal , new char[] {108,101,102,116});
		}
		public final static AbstractStackNode[] prod___ModuleId_ModuleName_attrs___term__cons_Unparameterized = new AbstractStackNode[1];
		static{
			prod___ModuleId_ModuleName_attrs___term__cons_Unparameterized[0] = new NonTerminalStackNode(1852, 0 , new IMatchableStackNode[] {new CharStackNode(1854, 0, new char[][]{{47,47}})}, "ModuleId");
		}
		public final static AbstractStackNode[] prod___lit_lexical_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_lexical_ModuleName_attrs___reject[0] = new LiteralStackNode(1876, 0, prod___char_class___range__108_108_char_class___range__101_101_char_class___range__120_120_char_class___range__105_105_char_class___range__99_99_char_class___range__97_97_char_class___range__108_108_lit_lexical_attrs___literal , new char[] {108,101,120,105,99,97,108});
		}
		public final static AbstractStackNode[] prod___lit_aliases_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_aliases_ModuleName_attrs___reject[0] = new LiteralStackNode(1878, 0, prod___char_class___range__97_97_char_class___range__108_108_char_class___range__105_105_char_class___range__97_97_char_class___range__115_115_char_class___range__101_101_char_class___range__115_115_lit_aliases_attrs___literal , new char[] {97,108,105,97,115,101,115});
		}
		public final static AbstractStackNode[] prod___lit_exports_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_exports_ModuleName_attrs___reject[0] = new LiteralStackNode(1880, 0, prod___char_class___range__101_101_char_class___range__120_120_char_class___range__112_112_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__115_115_lit_exports_attrs___literal , new char[] {101,120,112,111,114,116,115});
		}
		public final static AbstractStackNode[] prod___lit_right_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_right_ModuleName_attrs___reject[0] = new LiteralStackNode(1882, 0, prod___char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_lit_right_attrs___literal , new char[] {114,105,103,104,116});
		}
		public final static AbstractStackNode[] prod___ModuleId_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___93_ModuleName_attrs___term__cons_Parameterized = new AbstractStackNode[7];
		static{
			prod___ModuleId_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___93_ModuleName_attrs___term__cons_Parameterized[6] = new LiteralStackNode(1850, 6, prod___char_class___range__93_93_lit___93_attrs___literal , new char[] {93});
			prod___ModuleId_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___93_ModuleName_attrs___term__cons_Parameterized[5] = new NonTerminalStackNode(1846, 5 , new IMatchableStackNode[] {new CharStackNode(1848, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___ModuleId_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___93_ModuleName_attrs___term__cons_Parameterized[4] = new NonTerminalStackNode(1844, 4 , "Symbols");
			prod___ModuleId_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___93_ModuleName_attrs___term__cons_Parameterized[3] = new NonTerminalStackNode(1840, 3 , new IMatchableStackNode[] {new CharStackNode(1842, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___ModuleId_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___93_ModuleName_attrs___term__cons_Parameterized[2] = new LiteralStackNode(1838, 2, prod___char_class___range__91_91_lit___91_attrs___literal , new char[] {91});
			prod___ModuleId_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___93_ModuleName_attrs___term__cons_Parameterized[1] = new NonTerminalStackNode(1834, 1 , new IMatchableStackNode[] {new CharStackNode(1836, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___ModuleId_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___93_ModuleName_attrs___term__cons_Parameterized[0] = new NonTerminalStackNode(1830, 0 , new IMatchableStackNode[] {new CharStackNode(1832, 0, new char[][]{{47,47}})}, "ModuleId");
		}
		public final static AbstractStackNode[] prod___lit_definition_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_definition_ModuleName_attrs___reject[0] = new LiteralStackNode(1884, 0, prod___char_class___range__100_100_char_class___range__101_101_char_class___range__102_102_char_class___range__105_105_char_class___range__110_110_char_class___range__105_105_char_class___range__116_116_char_class___range__105_105_char_class___range__111_111_char_class___range__110_110_lit_definition_attrs___literal , new char[] {100,101,102,105,110,105,116,105,111,110});
		}
		public final static AbstractStackNode[] prod___lit_variables_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_variables_ModuleName_attrs___reject[0] = new LiteralStackNode(1886, 0, prod___char_class___range__118_118_char_class___range__97_97_char_class___range__114_114_char_class___range__105_105_char_class___range__97_97_char_class___range__98_98_char_class___range__108_108_char_class___range__101_101_char_class___range__115_115_lit_variables_attrs___literal , new char[] {118,97,114,105,97,98,108,101,115});
		}
		public final static AbstractStackNode[] prod___lit_sorts_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_sorts_ModuleName_attrs___reject[0] = new LiteralStackNode(1888, 0, prod___char_class___range__115_115_char_class___range__111_111_char_class___range__114_114_char_class___range__116_116_char_class___range__115_115_lit_sorts_attrs___literal , new char[] {115,111,114,116,115});
		}
		public final static AbstractStackNode[] prod___lit_non_assoc_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_non_assoc_ModuleName_attrs___reject[0] = new LiteralStackNode(1890, 0, prod___char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_lit_non_assoc_attrs___literal , new char[] {110,111,110,45,97,115,115,111,99});
		}
		public final static AbstractStackNode[] prod___lit_assoc_ModuleName_attrs___reject = new AbstractStackNode[1];
		static{
			prod___lit_assoc_ModuleName_attrs___reject[0] = new LiteralStackNode(1892, 0, prod___char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_lit_assoc_attrs___literal , new char[] {97,115,115,111,99});
		}
	}
	
	private static class IntCon {
		
		public final static AbstractStackNode[] prod___lit___layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Negative = new AbstractStackNode[3];
		static{
			prod___lit___layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Negative[2] = new NonTerminalStackNode(1912, 2 , new IMatchableStackNode[] {new CharStackNode(1914, 0, new char[][]{{48,57}})}, "NatCon");
			prod___lit___layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Negative[1] = new NonTerminalStackNode(1908, 1 , new IMatchableStackNode[] {new CharStackNode(1910, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Negative[0] = new LiteralStackNode(1906, 0, prod___char_class___range__45_45_lit___attrs___literal , new char[] {45});
		}
		public final static AbstractStackNode[] prod___NatCon_IntCon_attrs___term__cons_Natural = new AbstractStackNode[1];
		static{
			prod___NatCon_IntCon_attrs___term__cons_Natural[0] = new NonTerminalStackNode(1916, 0 , new IMatchableStackNode[] {new CharStackNode(1918, 0, new char[][]{{48,57}})}, "NatCon");
		}
		public final static AbstractStackNode[] prod___lit___43_layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Positive = new AbstractStackNode[3];
		static{
			prod___lit___43_layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Positive[2] = new NonTerminalStackNode(1926, 2 , new IMatchableStackNode[] {new CharStackNode(1928, 0, new char[][]{{48,57}})}, "NatCon");
			prod___lit___43_layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Positive[1] = new NonTerminalStackNode(1922, 1 , new IMatchableStackNode[] {new CharStackNode(1924, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___43_layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Positive[0] = new LiteralStackNode(1920, 0, prod___char_class___range__43_43_lit___43_attrs___literal , new char[] {43});
		}
	}
	
	private static class Alias {
		
		public final static AbstractStackNode[] prod___Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_Alias_attrs___term__cons_Alias = new AbstractStackNode[5];
		static{
			prod___Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_Alias_attrs___term__cons_Alias[4] = new NonTerminalStackNode(1942, 4 , "Symbol");
			prod___Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_Alias_attrs___term__cons_Alias[3] = new NonTerminalStackNode(1938, 3 , new IMatchableStackNode[] {new CharStackNode(1940, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_Alias_attrs___term__cons_Alias[2] = new LiteralStackNode(1936, 2, prod___char_class___range__45_45_char_class___range__62_62_lit___45_62_attrs___literal , new char[] {45,62});
			prod___Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_Alias_attrs___term__cons_Alias[1] = new NonTerminalStackNode(1932, 1 , new IMatchableStackNode[] {new CharStackNode(1934, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_Alias_attrs___term__cons_Alias[0] = new NonTerminalStackNode(1930, 0 , "Symbol");
		}
	}
	
	private static class Renamings {
		
		public final static AbstractStackNode[] prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__Renaming__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_Renamings_attrs___term__cons_Renamings = new AbstractStackNode[5];
		static{
			prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__Renaming__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_Renamings_attrs___term__cons_Renamings[4] = new LiteralStackNode(1974, 4, prod___char_class___range__93_93_lit___93_attrs___literal , new char[] {93});
			prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__Renaming__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_Renamings_attrs___term__cons_Renamings[3] = new NonTerminalStackNode(1970, 3 , new IMatchableStackNode[] {new CharStackNode(1972, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__Renaming__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_Renamings_attrs___term__cons_Renamings[2] = new SeparatedListStackNode(1962, 2, regular__iter_star_seps__Renaming__layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(1964, 0 , "Renaming"), new AbstractStackNode[]{new NonTerminalStackNode(1966, 1 , new IMatchableStackNode[] {new CharStackNode(1968, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, false);
			prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__Renaming__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_Renamings_attrs___term__cons_Renamings[1] = new NonTerminalStackNode(1958, 1 , new IMatchableStackNode[] {new CharStackNode(1960, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__Renaming__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_Renamings_attrs___term__cons_Renamings[0] = new LiteralStackNode(1956, 0, prod___char_class___range__91_91_lit___91_attrs___literal , new char[] {91});
		}
	}
	
	private static class Restrictions {
		
		public final static AbstractStackNode[] prod___iter_star_seps__Restriction__layouts_LAYOUTLIST_Restrictions_no_attrs = new AbstractStackNode[1];
		static{
			prod___iter_star_seps__Restriction__layouts_LAYOUTLIST_Restrictions_no_attrs[0] = new SeparatedListStackNode(1978, 0, regular__iter_star_seps__Restriction__layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(1980, 0 , "Restriction"), new AbstractStackNode[]{new NonTerminalStackNode(1982, 1 , new IMatchableStackNode[] {new CharStackNode(1984, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, false);
		}
	}
	
	private static class IdCon {
		
		public final static AbstractStackNode[] prod___char_class___range__65_90_range__97_122_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_IdCon_attrs___term__cons_Default_lex = new AbstractStackNode[2];
		static{
			prod___char_class___range__65_90_range__97_122_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_IdCon_attrs___term__cons_Default_lex[1] = new ListStackNode(1988, 1, regular__iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_no_attrs , new CharStackNode(1990, 0, new char[][]{{45,45},{48,57},{65,90},{97,122}}), false);
			prod___char_class___range__65_90_range__97_122_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_IdCon_attrs___term__cons_Default_lex[0] = new CharStackNode(1986, 0, new char[][]{{65,90},{97,122}});
		}
	}
	
	private static class layouts_$QUOTES {
		
		public final static AbstractStackNode[] prod___iter_star__char_class___range__9_10_range__13_13_range__32_32_layouts_$QUOTES_attrs___term__lex = new AbstractStackNode[1];
		static{
			prod___iter_star__char_class___range__9_10_range__13_13_range__32_32_layouts_$QUOTES_attrs___term__lex[0] = new ListStackNode(1994, 0, regular__iter_star__char_class___range__9_10_range__13_13_range__32_32_no_attrs , new CharStackNode(1996, 0, new char[][]{{9,10},{13,13},{32,32}}), false);
		}
	}
	
	private static class Imports {
		
		public final static AbstractStackNode[] prod___iter_star_seps__Import__layouts_LAYOUTLIST_Imports_no_attrs = new AbstractStackNode[1];
		static{
			prod___iter_star_seps__Import__layouts_LAYOUTLIST_Imports_no_attrs[0] = new SeparatedListStackNode(2010, 0, regular__iter_star_seps__Import__layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(2012, 0 , "Import"), new AbstractStackNode[]{new NonTerminalStackNode(2014, 1 , new IMatchableStackNode[] {new CharStackNode(2016, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, false);
		}
	}
	
	private static class Sections {
		
		public final static AbstractStackNode[] prod___iter_star_seps__Section__layouts_LAYOUTLIST_Sections_no_attrs = new AbstractStackNode[1];
		static{
			prod___iter_star_seps__Section__layouts_LAYOUTLIST_Sections_no_attrs[0] = new SeparatedListStackNode(2018, 0, regular__iter_star_seps__Section__layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(2020, 0 , "Section"), new AbstractStackNode[]{new NonTerminalStackNode(2022, 1 , new IMatchableStackNode[] {new CharStackNode(2024, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, false);
		}
	}
	
	private static class Annotation {
		
		public final static AbstractStackNode[] prod___lit___123_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Annotation_attrs___term__cons_Default = new AbstractStackNode[5];
		static{
			prod___lit___123_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Annotation_attrs___term__cons_Default[4] = new LiteralStackNode(2050, 4, prod___char_class___range__125_125_lit___125_attrs___literal , new char[] {125});
			prod___lit___123_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Annotation_attrs___term__cons_Default[3] = new NonTerminalStackNode(2046, 3 , new IMatchableStackNode[] {new CharStackNode(2048, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Annotation_attrs___term__cons_Default[2] = new SeparatedListStackNode(2032, 2, regular__iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(2034, 0 , "ATerm"), new AbstractStackNode[]{new NonTerminalStackNode(2036, 1 , new IMatchableStackNode[] {new CharStackNode(2038, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST"), new LiteralStackNode(2040, 2, prod___char_class___range__44_44_lit___44_attrs___literal , new char[] {44}), new NonTerminalStackNode(2042, 3 , new IMatchableStackNode[] {new CharStackNode(2044, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, true);
			prod___lit___123_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Annotation_attrs___term__cons_Default[1] = new NonTerminalStackNode(2028, 1 , new IMatchableStackNode[] {new CharStackNode(2030, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Annotation_attrs___term__cons_Default[0] = new LiteralStackNode(2026, 0, prod___char_class___range__123_123_lit___123_attrs___literal , new char[] {123});
		}
	}
	
	private static class ATerm {
		
		public final static AbstractStackNode[] prod___lit___60_layouts_LAYOUTLIST_ATerm_layouts_LAYOUTLIST_lit___62_ATerm_attrs___term__cons_Placeholder = new AbstractStackNode[5];
		static{
			prod___lit___60_layouts_LAYOUTLIST_ATerm_layouts_LAYOUTLIST_lit___62_ATerm_attrs___term__cons_Placeholder[4] = new LiteralStackNode(2082, 4, prod___char_class___range__62_62_lit___62_attrs___literal , new char[] {62});
			prod___lit___60_layouts_LAYOUTLIST_ATerm_layouts_LAYOUTLIST_lit___62_ATerm_attrs___term__cons_Placeholder[3] = new NonTerminalStackNode(2078, 3 , new IMatchableStackNode[] {new CharStackNode(2080, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_ATerm_layouts_LAYOUTLIST_lit___62_ATerm_attrs___term__cons_Placeholder[2] = new NonTerminalStackNode(2076, 2 , "ATerm");
			prod___lit___60_layouts_LAYOUTLIST_ATerm_layouts_LAYOUTLIST_lit___62_ATerm_attrs___term__cons_Placeholder[1] = new NonTerminalStackNode(2072, 1 , new IMatchableStackNode[] {new CharStackNode(2074, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___60_layouts_LAYOUTLIST_ATerm_layouts_LAYOUTLIST_lit___62_ATerm_attrs___term__cons_Placeholder[0] = new LiteralStackNode(2070, 0, prod___char_class___range__60_60_lit___60_attrs___literal , new char[] {60});
		}
		public final static AbstractStackNode[] prod___AFun_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ATerm_attrs___term__cons_Appl = new AbstractStackNode[7];
		static{
			prod___AFun_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ATerm_attrs___term__cons_Appl[6] = new LiteralStackNode(2114, 6, prod___char_class___range__41_41_lit___41_attrs___literal , new char[] {41});
			prod___AFun_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ATerm_attrs___term__cons_Appl[5] = new NonTerminalStackNode(2110, 5 , new IMatchableStackNode[] {new CharStackNode(2112, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___AFun_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ATerm_attrs___term__cons_Appl[4] = new SeparatedListStackNode(2096, 4, regular__iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(2098, 0 , "ATerm"), new AbstractStackNode[]{new NonTerminalStackNode(2100, 1 , new IMatchableStackNode[] {new CharStackNode(2102, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST"), new LiteralStackNode(2104, 2, prod___char_class___range__44_44_lit___44_attrs___literal , new char[] {44}), new NonTerminalStackNode(2106, 3 , new IMatchableStackNode[] {new CharStackNode(2108, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, true);
			prod___AFun_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ATerm_attrs___term__cons_Appl[3] = new NonTerminalStackNode(2092, 3 , new IMatchableStackNode[] {new CharStackNode(2094, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___AFun_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ATerm_attrs___term__cons_Appl[2] = new LiteralStackNode(2090, 2, prod___char_class___range__40_40_lit___40_attrs___literal , new char[] {40});
			prod___AFun_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ATerm_attrs___term__cons_Appl[1] = new NonTerminalStackNode(2086, 1 , new IMatchableStackNode[] {new CharStackNode(2088, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___AFun_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ATerm_attrs___term__cons_Appl[0] = new NonTerminalStackNode(2116, 0 , "AFun");
		}
		public final static AbstractStackNode[] prod___AFun_ATerm_attrs___term__cons_Fun = new AbstractStackNode[1];
		static{
			prod___AFun_ATerm_attrs___term__cons_Fun[0] = new NonTerminalStackNode(2116, 0 , "AFun");
		}
		public final static AbstractStackNode[] prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ATerm_attrs___term__cons_List = new AbstractStackNode[5];
		static{
			prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ATerm_attrs___term__cons_List[4] = new LiteralStackNode(2142, 4, prod___char_class___range__93_93_lit___93_attrs___literal , new char[] {93});
			prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ATerm_attrs___term__cons_List[3] = new NonTerminalStackNode(2138, 3 , new IMatchableStackNode[] {new CharStackNode(2140, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ATerm_attrs___term__cons_List[2] = new SeparatedListStackNode(2124, 2, regular__iter_star_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(2126, 0 , "ATerm"), new AbstractStackNode[]{new NonTerminalStackNode(2128, 1 , new IMatchableStackNode[] {new CharStackNode(2130, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST"), new LiteralStackNode(2132, 2, prod___char_class___range__44_44_lit___44_attrs___literal , new char[] {44}), new NonTerminalStackNode(2134, 3 , new IMatchableStackNode[] {new CharStackNode(2136, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, false);
			prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ATerm_attrs___term__cons_List[1] = new NonTerminalStackNode(2120, 1 , new IMatchableStackNode[] {new CharStackNode(2122, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ATerm_attrs___term__cons_List[0] = new LiteralStackNode(2118, 0, prod___char_class___range__91_91_lit___91_attrs___literal , new char[] {91});
		}
		public final static AbstractStackNode[] prod___RealCon_ATerm_attrs___term__cons_Real = new AbstractStackNode[1];
		static{
			prod___RealCon_ATerm_attrs___term__cons_Real[0] = new NonTerminalStackNode(2144, 0 , "RealCon");
		}
		public final static AbstractStackNode[] prod___IntCon_ATerm_attrs___term__cons_Int = new AbstractStackNode[1];
		static{
			prod___IntCon_ATerm_attrs___term__cons_Int[0] = new NonTerminalStackNode(2146, 0 , "IntCon");
		}
		public final static AbstractStackNode[] prod___ATerm_layouts_LAYOUTLIST_Annotation_ATerm_attrs___term__cons_Annotated = new AbstractStackNode[3];
		static{
			prod___ATerm_layouts_LAYOUTLIST_Annotation_ATerm_attrs___term__cons_Annotated[2] = new NonTerminalStackNode(2154, 2 , "Annotation");
			prod___ATerm_layouts_LAYOUTLIST_Annotation_ATerm_attrs___term__cons_Annotated[1] = new NonTerminalStackNode(2150, 1 , new IMatchableStackNode[] {new CharStackNode(2152, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___ATerm_layouts_LAYOUTLIST_Annotation_ATerm_attrs___term__cons_Annotated[0] = new NonTerminalStackNode(2148, 0 , "ATerm");
		}
	}
	
	private static class Lookaheads {
		
		public final static AbstractStackNode[] prod___Lookahead_Lookaheads_attrs___term__cons_Single = new AbstractStackNode[1];
		static{
			prod___Lookahead_Lookaheads_attrs___term__cons_Single[0] = new NonTerminalStackNode(2168, 0 , "Lookahead");
		}
		public final static AbstractStackNode[] prod___lit___91_91_layouts_LAYOUTLIST_iter_star_seps__Lookahead__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Lookaheads_attrs___term__cons_List = new AbstractStackNode[5];
		static{
			prod___lit___91_91_layouts_LAYOUTLIST_iter_star_seps__Lookahead__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Lookaheads_attrs___term__cons_List[4] = new LiteralStackNode(2194, 4, prod___char_class___range__93_93_char_class___range__93_93_lit___93_93_attrs___literal , new char[] {93,93});
			prod___lit___91_91_layouts_LAYOUTLIST_iter_star_seps__Lookahead__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Lookaheads_attrs___term__cons_List[3] = new NonTerminalStackNode(2190, 3 , new IMatchableStackNode[] {new CharStackNode(2192, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___91_91_layouts_LAYOUTLIST_iter_star_seps__Lookahead__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Lookaheads_attrs___term__cons_List[2] = new SeparatedListStackNode(2176, 2, regular__iter_star_seps__Lookahead__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(2178, 0 , "Lookahead"), new AbstractStackNode[]{new NonTerminalStackNode(2180, 1 , new IMatchableStackNode[] {new CharStackNode(2182, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST"), new LiteralStackNode(2184, 2, prod___char_class___range__44_44_lit___44_attrs___literal , new char[] {44}), new NonTerminalStackNode(2186, 3 , new IMatchableStackNode[] {new CharStackNode(2188, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, false);
			prod___lit___91_91_layouts_LAYOUTLIST_iter_star_seps__Lookahead__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Lookaheads_attrs___term__cons_List[1] = new NonTerminalStackNode(2172, 1 , new IMatchableStackNode[] {new CharStackNode(2174, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___91_91_layouts_LAYOUTLIST_iter_star_seps__Lookahead__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Lookaheads_attrs___term__cons_List[0] = new LiteralStackNode(2170, 0, prod___char_class___range__91_91_char_class___range__91_91_lit___91_91_attrs___literal , new char[] {91,91});
		}
		public final static AbstractStackNode[] prod___Lookaheads_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Lookaheads_Lookaheads_attrs___term__cons_Alt_assoc__right = new AbstractStackNode[5];
		static{
			prod___Lookaheads_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Lookaheads_Lookaheads_attrs___term__cons_Alt_assoc__right[4] = new NonTerminalStackNode(2208, 4 , "Lookaheads");
			prod___Lookaheads_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Lookaheads_Lookaheads_attrs___term__cons_Alt_assoc__right[3] = new NonTerminalStackNode(2204, 3 , new IMatchableStackNode[] {new CharStackNode(2206, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Lookaheads_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Lookaheads_Lookaheads_attrs___term__cons_Alt_assoc__right[2] = new LiteralStackNode(2202, 2, prod___char_class___range__124_124_lit___124_attrs___literal , new char[] {124});
			prod___Lookaheads_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Lookaheads_Lookaheads_attrs___term__cons_Alt_assoc__right[1] = new NonTerminalStackNode(2198, 1 , new IMatchableStackNode[] {new CharStackNode(2200, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Lookaheads_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Lookaheads_Lookaheads_attrs___term__cons_Alt_assoc__right[0] = new NonTerminalStackNode(2196, 0 , "Lookaheads");
		}
		public final static AbstractStackNode[] prod___lit___40_layouts_LAYOUTLIST_Lookaheads_layouts_LAYOUTLIST_lit___41_Lookaheads_attrs___term__cons_Bracket = new AbstractStackNode[5];
		static{
			prod___lit___40_layouts_LAYOUTLIST_Lookaheads_layouts_LAYOUTLIST_lit___41_Lookaheads_attrs___term__cons_Bracket[4] = new LiteralStackNode(2222, 4, prod___char_class___range__41_41_lit___41_attrs___literal , new char[] {41});
			prod___lit___40_layouts_LAYOUTLIST_Lookaheads_layouts_LAYOUTLIST_lit___41_Lookaheads_attrs___term__cons_Bracket[3] = new NonTerminalStackNode(2218, 3 , new IMatchableStackNode[] {new CharStackNode(2220, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Lookaheads_layouts_LAYOUTLIST_lit___41_Lookaheads_attrs___term__cons_Bracket[2] = new NonTerminalStackNode(2216, 2 , "Lookaheads");
			prod___lit___40_layouts_LAYOUTLIST_Lookaheads_layouts_LAYOUTLIST_lit___41_Lookaheads_attrs___term__cons_Bracket[1] = new NonTerminalStackNode(2212, 1 , new IMatchableStackNode[] {new CharStackNode(2214, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___40_layouts_LAYOUTLIST_Lookaheads_layouts_LAYOUTLIST_lit___41_Lookaheads_attrs___term__cons_Bracket[0] = new LiteralStackNode(2210, 0, prod___char_class___range__40_40_lit___40_attrs___literal , new char[] {40});
		}
	}
	
	private static class Group {
		
		public final static AbstractStackNode[] prod___lit___123_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_AssocGroup = new AbstractStackNode[9];
		static{
			prod___lit___123_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_AssocGroup[0] = new LiteralStackNode(2240, 0, prod___char_class___range__123_123_lit___123_attrs___literal , new char[] {123});
			prod___lit___123_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_AssocGroup[1] = new NonTerminalStackNode(2242, 1 , new IMatchableStackNode[] {new CharStackNode(2244, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_AssocGroup[2] = new NonTerminalStackNode(2246, 2 , "Associativity");
			prod___lit___123_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_AssocGroup[3] = new NonTerminalStackNode(2248, 3 , new IMatchableStackNode[] {new CharStackNode(2250, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_AssocGroup[4] = new LiteralStackNode(2252, 4, prod___char_class___range__58_58_lit___58_attrs___literal , new char[] {58});
			prod___lit___123_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_AssocGroup[5] = new NonTerminalStackNode(2254, 5 , new IMatchableStackNode[] {new CharStackNode(2256, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_AssocGroup[6] = new NonTerminalStackNode(2258, 6 , "Productions");
			prod___lit___123_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_AssocGroup[7] = new NonTerminalStackNode(2260, 7 , new IMatchableStackNode[] {new CharStackNode(2262, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_AssocGroup[8] = new LiteralStackNode(2264, 8, prod___char_class___range__125_125_lit___125_attrs___literal , new char[] {125});
		}
		public final static AbstractStackNode[] prod___Production_Group_attrs___term__cons_SimpleGroup = new AbstractStackNode[1];
		static{
			prod___Production_Group_attrs___term__cons_SimpleGroup[0] = new NonTerminalStackNode(2238, 0 , "Production");
		}
		public final static AbstractStackNode[] prod___lit___123_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_ProdsGroup = new AbstractStackNode[5];
		static{
			prod___lit___123_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_ProdsGroup[4] = new LiteralStackNode(2236, 4, prod___char_class___range__125_125_lit___125_attrs___literal , new char[] {125});
			prod___lit___123_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_ProdsGroup[3] = new NonTerminalStackNode(2232, 3 , new IMatchableStackNode[] {new CharStackNode(2234, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_ProdsGroup[2] = new NonTerminalStackNode(2230, 2 , "Productions");
			prod___lit___123_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_ProdsGroup[1] = new NonTerminalStackNode(2226, 1 , new IMatchableStackNode[] {new CharStackNode(2228, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_ProdsGroup[0] = new LiteralStackNode(2224, 0, prod___char_class___range__123_123_lit___123_attrs___literal , new char[] {123});
		}
		public final static AbstractStackNode[] prod___Group_layouts_LAYOUTLIST_lit___46_Group_attrs___term__cons_NonTransitive_assoc__non_assoc = new AbstractStackNode[3];
		static{
			prod___Group_layouts_LAYOUTLIST_lit___46_Group_attrs___term__cons_NonTransitive_assoc__non_assoc[2] = new LiteralStackNode(2272, 2, prod___char_class___range__46_46_lit___46_attrs___literal , new char[] {46});
			prod___Group_layouts_LAYOUTLIST_lit___46_Group_attrs___term__cons_NonTransitive_assoc__non_assoc[1] = new NonTerminalStackNode(2268, 1 , new IMatchableStackNode[] {new CharStackNode(2270, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Group_layouts_LAYOUTLIST_lit___46_Group_attrs___term__cons_NonTransitive_assoc__non_assoc[0] = new NonTerminalStackNode(2266, 0 , "Group");
		}
		public final static AbstractStackNode[] prod___Group_layouts_LAYOUTLIST_ArgumentIndicator_Group_attrs___term__cons_WithArguments_assoc__non_assoc = new AbstractStackNode[3];
		static{
			prod___Group_layouts_LAYOUTLIST_ArgumentIndicator_Group_attrs___term__cons_WithArguments_assoc__non_assoc[2] = new NonTerminalStackNode(2280, 2 , "ArgumentIndicator");
			prod___Group_layouts_LAYOUTLIST_ArgumentIndicator_Group_attrs___term__cons_WithArguments_assoc__non_assoc[1] = new NonTerminalStackNode(2268, 1 , new IMatchableStackNode[] {new CharStackNode(2270, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Group_layouts_LAYOUTLIST_ArgumentIndicator_Group_attrs___term__cons_WithArguments_assoc__non_assoc[0] = new NonTerminalStackNode(2266, 0 , "Group");
		}
	}
	
	private static class CharRange {
		
		public final static AbstractStackNode[] prod___Character_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_Character_CharRange_attrs___term__cons_Range = new AbstractStackNode[5];
		static{
			prod___Character_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_Character_CharRange_attrs___term__cons_Range[4] = new NonTerminalStackNode(2314, 4 , "Character");
			prod___Character_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_Character_CharRange_attrs___term__cons_Range[3] = new NonTerminalStackNode(2310, 3 , new IMatchableStackNode[] {new CharStackNode(2312, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Character_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_Character_CharRange_attrs___term__cons_Range[2] = new LiteralStackNode(2308, 2, prod___char_class___range__45_45_lit___attrs___literal , new char[] {45});
			prod___Character_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_Character_CharRange_attrs___term__cons_Range[1] = new NonTerminalStackNode(2304, 1 , new IMatchableStackNode[] {new CharStackNode(2306, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___Character_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_Character_CharRange_attrs___term__cons_Range[0] = new NonTerminalStackNode(2316, 0 , "Character");
		}
		public final static AbstractStackNode[] prod___Character_CharRange_no_attrs = new AbstractStackNode[1];
		static{
			prod___Character_CharRange_no_attrs[0] = new NonTerminalStackNode(2316, 0 , "Character");
		}
	}
	
	private static class Priorities {
		
		public final static AbstractStackNode[] prod___iter_star_seps__Priority__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_Priorities_no_attrs = new AbstractStackNode[1];
		static{
			prod___iter_star_seps__Priority__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_Priorities_no_attrs[0] = new SeparatedListStackNode(2338, 0, regular__iter_star_seps__Priority__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(2340, 0 , "Priority"), new AbstractStackNode[]{new NonTerminalStackNode(2342, 1 , new IMatchableStackNode[] {new CharStackNode(2344, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST"), new LiteralStackNode(2346, 2, prod___char_class___range__44_44_lit___44_attrs___literal , new char[] {44}), new NonTerminalStackNode(2348, 3 , new IMatchableStackNode[] {new CharStackNode(2350, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, false);
		}
	}
	
	private static class Lookahead {
		
		public final static AbstractStackNode[] prod___CharClass_Lookahead_attrs___term__cons_CharClass = new AbstractStackNode[1];
		static{
			prod___CharClass_Lookahead_attrs___term__cons_CharClass[0] = new NonTerminalStackNode(2322, 0 , "CharClass");
		}
		public final static AbstractStackNode[] prod___CharClass_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_Lookaheads_Lookahead_attrs___term__cons_Seq = new AbstractStackNode[5];
		static{
			prod___CharClass_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_Lookaheads_Lookahead_attrs___term__cons_Seq[4] = new NonTerminalStackNode(2336, 4 , "Lookaheads");
			prod___CharClass_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_Lookaheads_Lookahead_attrs___term__cons_Seq[3] = new NonTerminalStackNode(2332, 3 , new IMatchableStackNode[] {new CharStackNode(2334, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___CharClass_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_Lookaheads_Lookahead_attrs___term__cons_Seq[2] = new LiteralStackNode(2330, 2, prod___char_class___range__46_46_lit___46_attrs___literal , new char[] {46});
			prod___CharClass_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_Lookaheads_Lookahead_attrs___term__cons_Seq[1] = new NonTerminalStackNode(2326, 1 , new IMatchableStackNode[] {new CharStackNode(2328, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___CharClass_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_Lookaheads_Lookahead_attrs___term__cons_Seq[0] = new NonTerminalStackNode(2322, 0 , "CharClass");
		}
	}
	
	private static class Attributes {
		
		public final static AbstractStackNode[] prod___Attributes_attrs___term__cons_NoAttrs = new AbstractStackNode[1];
		static{
			prod___Attributes_attrs___term__cons_NoAttrs[0] = new EpsilonStackNode(2368, 0);
		}
		public final static AbstractStackNode[] prod___lit___123_layouts_LAYOUTLIST_iter_star_seps__Attribute__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Attributes_attrs___term__cons_Attrs = new AbstractStackNode[5];
		static{
			prod___lit___123_layouts_LAYOUTLIST_iter_star_seps__Attribute__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Attributes_attrs___term__cons_Attrs[4] = new LiteralStackNode(2394, 4, prod___char_class___range__125_125_lit___125_attrs___literal , new char[] {125});
			prod___lit___123_layouts_LAYOUTLIST_iter_star_seps__Attribute__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Attributes_attrs___term__cons_Attrs[3] = new NonTerminalStackNode(2390, 3 , new IMatchableStackNode[] {new CharStackNode(2392, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_iter_star_seps__Attribute__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Attributes_attrs___term__cons_Attrs[2] = new SeparatedListStackNode(2376, 2, regular__iter_star_seps__Attribute__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(2378, 0 , "Attribute"), new AbstractStackNode[]{new NonTerminalStackNode(2380, 1 , new IMatchableStackNode[] {new CharStackNode(2382, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST"), new LiteralStackNode(2384, 2, prod___char_class___range__44_44_lit___44_attrs___literal , new char[] {44}), new NonTerminalStackNode(2386, 3 , new IMatchableStackNode[] {new CharStackNode(2388, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, false);
			prod___lit___123_layouts_LAYOUTLIST_iter_star_seps__Attribute__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Attributes_attrs___term__cons_Attrs[1] = new NonTerminalStackNode(2372, 1 , new IMatchableStackNode[] {new CharStackNode(2374, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit___123_layouts_LAYOUTLIST_iter_star_seps__Attribute__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Attributes_attrs___term__cons_Attrs[0] = new LiteralStackNode(2370, 0, prod___char_class___range__123_123_lit___123_attrs___literal , new char[] {123});
		}
	}
	
	private static class Definition {
		
		public final static AbstractStackNode[] prod___iter_star_seps__Module__layouts_LAYOUTLIST_Definition_no_attrs = new AbstractStackNode[1];
		static{
			prod___iter_star_seps__Module__layouts_LAYOUTLIST_Definition_no_attrs[0] = new SeparatedListStackNode(2408, 0, regular__iter_star_seps__Module__layouts_LAYOUTLIST_no_attrs , new NonTerminalStackNode(2410, 0 , "Module"), new AbstractStackNode[]{new NonTerminalStackNode(2412, 1 , new IMatchableStackNode[] {new CharStackNode(2414, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST")}, false);
		}
	}
	
	private static class AFun {
		
		public final static AbstractStackNode[] prod___StrCon_AFun_attrs___term__cons_Quoted = new AbstractStackNode[1];
		static{
			prod___StrCon_AFun_attrs___term__cons_Quoted[0] = new NonTerminalStackNode(2418, 0 , "StrCon");
		}
		public final static AbstractStackNode[] prod___IdCon_AFun_attrs___term__cons_Unquoted = new AbstractStackNode[1];
		static{
			prod___IdCon_AFun_attrs___term__cons_Unquoted[0] = new NonTerminalStackNode(2420, 0 , new IMatchableStackNode[] {new CharStackNode(2422, 0, new char[][]{{45,45},{48,57},{65,90},{97,122}})}, "IdCon");
		}
	}
	
	private static class Attribute {
		
		public final static AbstractStackNode[] prod___lit_bracket_Attribute_attrs___term__cons_Bracket = new AbstractStackNode[1];
		static{
			prod___lit_bracket_Attribute_attrs___term__cons_Bracket[0] = new LiteralStackNode(2438, 0, prod___char_class___range__98_98_char_class___range__114_114_char_class___range__97_97_char_class___range__99_99_char_class___range__107_107_char_class___range__101_101_char_class___range__116_116_lit_bracket_attrs___literal , new char[] {98,114,97,99,107,101,116});
		}
		public final static AbstractStackNode[] prod___lit_reject_Attribute_attrs___term__cons_Reject = new AbstractStackNode[1];
		static{
			prod___lit_reject_Attribute_attrs___term__cons_Reject[0] = new LiteralStackNode(2440, 0, prod___char_class___range__114_114_char_class___range__101_101_char_class___range__106_106_char_class___range__101_101_char_class___range__99_99_char_class___range__116_116_lit_reject_attrs___literal , new char[] {114,101,106,101,99,116});
		}
		public final static AbstractStackNode[] prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_Attribute_attrs___term__cons_Id = new AbstractStackNode[7];
		static{
			prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_Attribute_attrs___term__cons_Id[6] = new LiteralStackNode(2462, 6, prod___char_class___range__41_41_lit___41_attrs___literal , new char[] {41});
			prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_Attribute_attrs___term__cons_Id[5] = new NonTerminalStackNode(2458, 5 , new IMatchableStackNode[] {new CharStackNode(2460, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_Attribute_attrs___term__cons_Id[4] = new NonTerminalStackNode(2454, 4 , new IMatchableStackNode[] {new CharStackNode(2456, 0, new char[][]{{45,45},{48,57},{65,90},{95,95},{97,122}})}, "ModuleName");
			prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_Attribute_attrs___term__cons_Id[3] = new NonTerminalStackNode(2450, 3 , new IMatchableStackNode[] {new CharStackNode(2452, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_Attribute_attrs___term__cons_Id[2] = new LiteralStackNode(2448, 2, prod___char_class___range__40_40_lit___40_attrs___literal , new char[] {40});
			prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_Attribute_attrs___term__cons_Id[1] = new NonTerminalStackNode(2444, 1 , new IMatchableStackNode[] {new CharStackNode(2446, 0, new char[][]{{9,10},{13,13},{32,32},{37,37}})}, "layouts_LAYOUTLIST");
			prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_Attribute_attrs___term__cons_Id[0] = new LiteralStackNode(2442, 0, prod___char_class___range__105_105_char_class___range__100_100_lit_id_attrs___literal , new char[] {105,100});
		}
		public final static AbstractStackNode[] prod___Associativity_Attribute_attrs___term__cons_Assoc = new AbstractStackNode[1];
		static{
			prod___Associativity_Attribute_attrs___term__cons_Assoc[0] = new NonTerminalStackNode(2464, 0 , "Associativity");
		}
		public final static AbstractStackNode[] prod___lit_avoid_Attribute_attrs___term__cons_Avoid = new AbstractStackNode[1];
		static{
			prod___lit_avoid_Attribute_attrs___term__cons_Avoid[0] = new LiteralStackNode(2466, 0, prod___char_class___range__97_97_char_class___range__118_118_char_class___range__111_111_char_class___range__105_105_char_class___range__100_100_lit_avoid_attrs___literal , new char[] {97,118,111,105,100});
		}
		public final static AbstractStackNode[] prod___lit_prefer_Attribute_attrs___term__cons_Prefer = new AbstractStackNode[1];
		static{
			prod___lit_prefer_Attribute_attrs___term__cons_Prefer[0] = new LiteralStackNode(2468, 0, prod___char_class___range__112_112_char_class___range__114_114_char_class___range__101_101_char_class___range__102_102_char_class___range__101_101_char_class___range__114_114_lit_prefer_attrs___literal , new char[] {112,114,101,102,101,114});
		}
		public final static AbstractStackNode[] prod___ATermAttribute_Attribute_attrs___term__cons_Term = new AbstractStackNode[1];
		static{
			prod___ATermAttribute_Attribute_attrs___term__cons_Term[0] = new NonTerminalStackNode(2470, 0 , "ATermAttribute");
		}
	}
	
	private static class Associativity {
		
		public final static AbstractStackNode[] prod___lit_assoc_Associativity_attrs___term__cons_Assoc = new AbstractStackNode[1];
		static{
			prod___lit_assoc_Associativity_attrs___term__cons_Assoc[0] = new LiteralStackNode(2472, 0, prod___char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_lit_assoc_attrs___literal , new char[] {97,115,115,111,99});
		}
		public final static AbstractStackNode[] prod___lit_right_Associativity_attrs___term__cons_Right = new AbstractStackNode[1];
		static{
			prod___lit_right_Associativity_attrs___term__cons_Right[0] = new LiteralStackNode(2474, 0, prod___char_class___range__114_114_char_class___range__105_105_char_class___range__103_103_char_class___range__104_104_char_class___range__116_116_lit_right_attrs___literal , new char[] {114,105,103,104,116});
		}
		public final static AbstractStackNode[] prod___lit_non_assoc_Associativity_attrs___term__cons_NonAssoc = new AbstractStackNode[1];
		static{
			prod___lit_non_assoc_Associativity_attrs___term__cons_NonAssoc[0] = new LiteralStackNode(2476, 0, prod___char_class___range__110_110_char_class___range__111_111_char_class___range__110_110_char_class___range__45_45_char_class___range__97_97_char_class___range__115_115_char_class___range__115_115_char_class___range__111_111_char_class___range__99_99_lit_non_assoc_attrs___literal , new char[] {110,111,110,45,97,115,115,111,99});
		}
		public final static AbstractStackNode[] prod___lit_left_Associativity_attrs___term__cons_Left = new AbstractStackNode[1];
		static{
			prod___lit_left_Associativity_attrs___term__cons_Left[0] = new LiteralStackNode(2478, 0, prod___char_class___range__108_108_char_class___range__101_101_char_class___range__102_102_char_class___range__116_116_lit_left_attrs___literal , new char[] {108,101,102,116});
		}
	}
	
	public SDF2(){
		super();
	}
	
	// Parse methods    
	
      public void Grammar() {
            
            if ((lookAheadChar == 97)) {
                  // prod([sort("Grammar"),layouts("LAYOUTLIST"),sort("Grammar")],sort("Grammar"),attrs([term(cons("ConcGrammars")),assoc(assoc())]))
	expect(prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc, Grammar.prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc);
                // prod([lit("aliases"),layouts("LAYOUTLIST"),sort("Aliases")],sort("Grammar"),attrs([term(cons("Aliases"))]))
	expect(prod___lit_aliases_layouts_LAYOUTLIST_Aliases_Grammar_attrs___term__cons_Aliases, Grammar.prod___lit_aliases_layouts_LAYOUTLIST_Aliases_Grammar_attrs___term__cons_Aliases);
                
                } else {
                  if ((lookAheadChar == 99)) {
                  // prod([lit("context-free"),layouts("LAYOUTLIST"),lit("priorities"),layouts("LAYOUTLIST"),sort("Priorities")],sort("Grammar"),attrs([term(cons("ContextFreePriorities"))]))
	expect(prod___lit_context_free_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_ContextFreePriorities, Grammar.prod___lit_context_free_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_ContextFreePriorities);
                // prod([lit("context-free"),layouts("LAYOUTLIST"),lit("syntax"),layouts("LAYOUTLIST"),sort("Productions")],sort("Grammar"),attrs([term(cons("ContextFreeSyntax"))]))
	expect(prod___lit_context_free_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_ContextFreeSyntax, Grammar.prod___lit_context_free_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_ContextFreeSyntax);
                // prod([sort("Grammar"),layouts("LAYOUTLIST"),sort("Grammar")],sort("Grammar"),attrs([term(cons("ConcGrammars")),assoc(assoc())]))
	expect(prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc, Grammar.prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc);
                // prod([lit("context-free"),layouts("LAYOUTLIST"),lit("restrictions"),layouts("LAYOUTLIST"),sort("Restrictions")],sort("Grammar"),attrs([term(cons("ContextFreeRestrictions"))]))
	expect(prod___lit_context_free_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_ContextFreeRestrictions, Grammar.prod___lit_context_free_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_ContextFreeRestrictions);
                // prod([lit("context-free"),layouts("LAYOUTLIST"),lit("start-symbols"),layouts("LAYOUTLIST"),sort("Symbols")],sort("Grammar"),attrs([term(cons("ContextFreeStartSymbols"))]))
	expect(prod___lit_context_free_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_ContextFreeStartSymbols, Grammar.prod___lit_context_free_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_ContextFreeStartSymbols);
                
                } else {
                  if ((lookAheadChar == 115)) {
                  // prod([lit("start-symbols"),layouts("LAYOUTLIST"),sort("Symbols")],sort("Grammar"),attrs([term(cons("KernalStartSymbols"))]))
	expect(prod___lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_KernalStartSymbols, Grammar.prod___lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_KernalStartSymbols);
                // prod([sort("Grammar"),layouts("LAYOUTLIST"),sort("Grammar")],sort("Grammar"),attrs([term(cons("ConcGrammars")),assoc(assoc())]))
	expect(prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc, Grammar.prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc);
                // prod([lit("sorts"),layouts("LAYOUTLIST"),sort("Symbols")],sort("Grammar"),attrs([term(cons("Sorts"))]))
	expect(prod___lit_sorts_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_Sorts, Grammar.prod___lit_sorts_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_Sorts);
                // prod([lit("syntax"),layouts("LAYOUTLIST"),sort("Productions")],sort("Grammar"),attrs([term(cons("Syntax"))]))
	expect(prod___lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Syntax, Grammar.prod___lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Syntax);
                
                } else {
                  if ((lookAheadChar == 40)) {
                  // prod([sort("Grammar"),layouts("LAYOUTLIST"),sort("Grammar")],sort("Grammar"),attrs([term(cons("ConcGrammars")),assoc(assoc())]))
	expect(prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc, Grammar.prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc);
                // prod([lit("("),layouts("LAYOUTLIST"),sort("Grammar"),layouts("LAYOUTLIST"),lit(")")],sort("Grammar"),attrs([term(cons("Bracket"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_Grammar_layouts_LAYOUTLIST_lit___41_Grammar_attrs___term__cons_Bracket, Grammar.prod___lit___40_layouts_LAYOUTLIST_Grammar_layouts_LAYOUTLIST_lit___41_Grammar_attrs___term__cons_Bracket);
                // prod([lit("(/)")],sort("Grammar"),attrs([term(cons("EmptyGrammar"))]))
	expect(prod___lit___40_47_41_Grammar_attrs___term__cons_EmptyGrammar, Grammar.prod___lit___40_47_41_Grammar_attrs___term__cons_EmptyGrammar);
                
                } else {
                  if ((lookAheadChar == 112)) {
                  // prod([lit("priorities"),layouts("LAYOUTLIST"),sort("Priorities")],sort("Grammar"),attrs([term(cons("Priorities"))]))
	expect(prod___lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_Priorities, Grammar.prod___lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_Priorities);
                // prod([sort("Grammar"),layouts("LAYOUTLIST"),sort("Grammar")],sort("Grammar"),attrs([term(cons("ConcGrammars")),assoc(assoc())]))
	expect(prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc, Grammar.prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc);
                
                } else {
                  if ((lookAheadChar == 118)) {
                  // prod([sort("Grammar"),layouts("LAYOUTLIST"),sort("Grammar")],sort("Grammar"),attrs([term(cons("ConcGrammars")),assoc(assoc())]))
	expect(prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc, Grammar.prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc);
                // prod([lit("variables"),layouts("LAYOUTLIST"),sort("Productions")],sort("Grammar"),attrs([term(cons("Variables"))]))
	expect(prod___lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Variables, Grammar.prod___lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Variables);
                
                } else {
                  if ((lookAheadChar == 108)) {
                  // prod([lit("lexical"),layouts("LAYOUTLIST"),lit("variables"),layouts("LAYOUTLIST"),sort("Productions")],sort("Grammar"),attrs([term(cons("LexicalVariables"))]))
	expect(prod___lit_lexical_layouts_LAYOUTLIST_lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalVariables, Grammar.prod___lit_lexical_layouts_LAYOUTLIST_lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalVariables);
                // prod([sort("Grammar"),layouts("LAYOUTLIST"),sort("Grammar")],sort("Grammar"),attrs([term(cons("ConcGrammars")),assoc(assoc())]))
	expect(prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc, Grammar.prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc);
                // prod([lit("lexical"),layouts("LAYOUTLIST"),lit("restrictions"),layouts("LAYOUTLIST"),sort("Restrictions")],sort("Grammar"),attrs([term(cons("LexicalRestrictions"))]))
	expect(prod___lit_lexical_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_LexicalRestrictions, Grammar.prod___lit_lexical_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_LexicalRestrictions);
                // prod([lit("lexical"),layouts("LAYOUTLIST"),lit("start-symbols"),layouts("LAYOUTLIST"),sort("Symbols")],sort("Grammar"),attrs([term(cons("LexicalStartSymbols"))]))
	expect(prod___lit_lexical_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_LexicalStartSymbols, Grammar.prod___lit_lexical_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_LexicalStartSymbols);
                // prod([lit("lexical"),layouts("LAYOUTLIST"),lit("syntax"),layouts("LAYOUTLIST"),sort("Productions")],sort("Grammar"),attrs([term(cons("LexicalSyntax"))]))
	expect(prod___lit_lexical_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalSyntax, Grammar.prod___lit_lexical_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalSyntax);
                // prod([lit("lexical"),layouts("LAYOUTLIST"),lit("priorities"),layouts("LAYOUTLIST"),sort("Priorities")],sort("Grammar"),attrs([term(cons("LexicalPriorities"))]))
	expect(prod___lit_lexical_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_LexicalPriorities, Grammar.prod___lit_lexical_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_LexicalPriorities);
                
                } else {
                  if ((lookAheadChar == 105)) {
                  // prod([sort("Grammar"),layouts("LAYOUTLIST"),sort("Grammar")],sort("Grammar"),attrs([term(cons("ConcGrammars")),assoc(assoc())]))
	expect(prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc, Grammar.prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc);
                // prod([sort("ImpSection")],sort("Grammar"),attrs([term(cons("ImpSection"))]))
	expect(prod___ImpSection_Grammar_attrs___term__cons_ImpSection, Grammar.prod___ImpSection_Grammar_attrs___term__cons_ImpSection);
                
                } else {
                  if ((lookAheadChar == 114)) {
                  // prod([sort("Grammar"),layouts("LAYOUTLIST"),sort("Grammar")],sort("Grammar"),attrs([term(cons("ConcGrammars")),assoc(assoc())]))
	expect(prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc, Grammar.prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc);
                // prod([lit("restrictions"),layouts("LAYOUTLIST"),sort("Restrictions")],sort("Grammar"),attrs([term(cons("Restrictions"))]))
	expect(prod___lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_Restrictions, Grammar.prod___lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_Restrictions);
                
                } else {
                  if ((lookAheadChar == 60)) {
                  // prod([lit("context-free"),layouts("LAYOUTLIST"),lit("priorities"),layouts("LAYOUTLIST"),sort("Priorities")],sort("Grammar"),attrs([term(cons("ContextFreePriorities"))]))
	expect(prod___lit_context_free_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_ContextFreePriorities, Grammar.prod___lit_context_free_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_ContextFreePriorities);
                // prod([lit("context-free"),layouts("LAYOUTLIST"),lit("syntax"),layouts("LAYOUTLIST"),sort("Productions")],sort("Grammar"),attrs([term(cons("ContextFreeSyntax"))]))
	expect(prod___lit_context_free_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_ContextFreeSyntax, Grammar.prod___lit_context_free_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_ContextFreeSyntax);
                // prod([sort("Grammar"),layouts("LAYOUTLIST"),sort("Grammar")],sort("Grammar"),attrs([term(cons("ConcGrammars")),assoc(assoc())]))
	expect(prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc, Grammar.prod___Grammar_layouts_LAYOUTLIST_Grammar_Grammar_attrs___term__cons_ConcGrammars_assoc__assoc);
                // prod([lit("lexical"),layouts("LAYOUTLIST"),lit("restrictions"),layouts("LAYOUTLIST"),sort("Restrictions")],sort("Grammar"),attrs([term(cons("LexicalRestrictions"))]))
	expect(prod___lit_lexical_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_LexicalRestrictions, Grammar.prod___lit_lexical_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_LexicalRestrictions);
                // prod([lit("lexical"),layouts("LAYOUTLIST"),lit("syntax"),layouts("LAYOUTLIST"),sort("Productions")],sort("Grammar"),attrs([term(cons("LexicalSyntax"))]))
	expect(prod___lit_lexical_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalSyntax, Grammar.prod___lit_lexical_layouts_LAYOUTLIST_lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalSyntax);
                // prod([lit("("),layouts("LAYOUTLIST"),sort("Grammar"),layouts("LAYOUTLIST"),lit(")")],sort("Grammar"),attrs([term(cons("Bracket"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_Grammar_layouts_LAYOUTLIST_lit___41_Grammar_attrs___term__cons_Bracket, Grammar.prod___lit___40_layouts_LAYOUTLIST_Grammar_layouts_LAYOUTLIST_lit___41_Grammar_attrs___term__cons_Bracket);
                // prod([lit("variables"),layouts("LAYOUTLIST"),sort("Productions")],sort("Grammar"),attrs([term(cons("Variables"))]))
	expect(prod___lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Variables, Grammar.prod___lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Variables);
                // prod([lit("context-free"),layouts("LAYOUTLIST"),lit("start-symbols"),layouts("LAYOUTLIST"),sort("Symbols")],sort("Grammar"),attrs([term(cons("ContextFreeStartSymbols"))]))
	expect(prod___lit_context_free_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_ContextFreeStartSymbols, Grammar.prod___lit_context_free_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_ContextFreeStartSymbols);
                // prod([lit("priorities"),layouts("LAYOUTLIST"),sort("Priorities")],sort("Grammar"),attrs([term(cons("Priorities"))]))
	expect(prod___lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_Priorities, Grammar.prod___lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_Priorities);
                // prod([lit("lexical"),layouts("LAYOUTLIST"),lit("variables"),layouts("LAYOUTLIST"),sort("Productions")],sort("Grammar"),attrs([term(cons("LexicalVariables"))]))
	expect(prod___lit_lexical_layouts_LAYOUTLIST_lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalVariables, Grammar.prod___lit_lexical_layouts_LAYOUTLIST_lit_variables_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_LexicalVariables);
                // prod([lit("start-symbols"),layouts("LAYOUTLIST"),sort("Symbols")],sort("Grammar"),attrs([term(cons("KernalStartSymbols"))]))
	expect(prod___lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_KernalStartSymbols, Grammar.prod___lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_KernalStartSymbols);
                // prod([lit("sorts"),layouts("LAYOUTLIST"),sort("Symbols")],sort("Grammar"),attrs([term(cons("Sorts"))]))
	expect(prod___lit_sorts_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_Sorts, Grammar.prod___lit_sorts_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_Sorts);
                // prod([lit("lexical"),layouts("LAYOUTLIST"),lit("start-symbols"),layouts("LAYOUTLIST"),sort("Symbols")],sort("Grammar"),attrs([term(cons("LexicalStartSymbols"))]))
	expect(prod___lit_lexical_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_LexicalStartSymbols, Grammar.prod___lit_lexical_layouts_LAYOUTLIST_lit_start_symbols_layouts_LAYOUTLIST_Symbols_Grammar_attrs___term__cons_LexicalStartSymbols);
                // prod([sort("ImpSection")],sort("Grammar"),attrs([term(cons("ImpSection"))]))
	expect(prod___ImpSection_Grammar_attrs___term__cons_ImpSection, Grammar.prod___ImpSection_Grammar_attrs___term__cons_ImpSection);
                // prod([lit("lexical"),layouts("LAYOUTLIST"),lit("priorities"),layouts("LAYOUTLIST"),sort("Priorities")],sort("Grammar"),attrs([term(cons("LexicalPriorities"))]))
	expect(prod___lit_lexical_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_LexicalPriorities, Grammar.prod___lit_lexical_layouts_LAYOUTLIST_lit_priorities_layouts_LAYOUTLIST_Priorities_Grammar_attrs___term__cons_LexicalPriorities);
                // prod([lit("aliases"),layouts("LAYOUTLIST"),sort("Aliases")],sort("Grammar"),attrs([term(cons("Aliases"))]))
	expect(prod___lit_aliases_layouts_LAYOUTLIST_Aliases_Grammar_attrs___term__cons_Aliases, Grammar.prod___lit_aliases_layouts_LAYOUTLIST_Aliases_Grammar_attrs___term__cons_Aliases);
                // prod([lit("syntax"),layouts("LAYOUTLIST"),sort("Productions")],sort("Grammar"),attrs([term(cons("Syntax"))]))
	expect(prod___lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Syntax, Grammar.prod___lit_syntax_layouts_LAYOUTLIST_Productions_Grammar_attrs___term__cons_Syntax);
                // prod([lit("context-free"),layouts("LAYOUTLIST"),lit("restrictions"),layouts("LAYOUTLIST"),sort("Restrictions")],sort("Grammar"),attrs([term(cons("ContextFreeRestrictions"))]))
	expect(prod___lit_context_free_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_ContextFreeRestrictions, Grammar.prod___lit_context_free_layouts_LAYOUTLIST_lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_ContextFreeRestrictions);
                // prod([lit("restrictions"),layouts("LAYOUTLIST"),sort("Restrictions")],sort("Grammar"),attrs([term(cons("Restrictions"))]))
	expect(prod___lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_Restrictions, Grammar.prod___lit_restrictions_layouts_LAYOUTLIST_Restrictions_Grammar_attrs___term__cons_Restrictions);
                // prod([lit("(/)")],sort("Grammar"),attrs([term(cons("EmptyGrammar"))]))
	expect(prod___lit___40_47_41_Grammar_attrs___term__cons_EmptyGrammar, Grammar.prod___lit___40_47_41_Grammar_attrs___term__cons_EmptyGrammar);
                
               }
                }
                }
                }
                }
                }
                }
                }
                }
                }
          }
	
      public void start__SDF() {
            
            if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 37)  || (lookAheadChar == 100) ) {
                  // prod([layouts("LAYOUTLIST"),sort("SDF"),layouts("LAYOUTLIST")],start(sort("SDF")),\no-attrs())
	expect(prod___layouts_LAYOUTLIST_SDF_layouts_LAYOUTLIST_start__SDF_no_attrs, start__SDF.prod___layouts_LAYOUTLIST_SDF_layouts_LAYOUTLIST_start__SDF_no_attrs);
                
               }
          }
	
      public void Module() {
            
            if ((lookAheadChar == 60) || (lookAheadChar == 109) ) {
                  // prod([lit("module"),layouts("LAYOUTLIST"),sort("ModuleName"),layouts("LAYOUTLIST"),\iter-star-seps(sort("ImpSection"),[layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),sort("Sections")],sort("Module"),attrs([term(cons("Module"))]))
	expect(prod___lit_module_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_iter_star_seps__ImpSection__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Sections_Module_attrs___term__cons_Module, Module.prod___lit_module_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_iter_star_seps__ImpSection__layouts_LAYOUTLIST_layouts_LAYOUTLIST_Sections_Module_attrs___term__cons_Module);
                
               }
          }
	
      public void OptExp() {
            
            if ((lookAheadChar == 60) || (lookAheadChar == 101) ) {
                  // prod([lit("e"),layouts("LAYOUTLIST"),sort("IntCon")],sort("OptExp"),attrs([term(cons("Present"))]))
	expect(prod___lit_e_layouts_LAYOUTLIST_IntCon_OptExp_attrs___term__cons_Present, OptExp.prod___lit_e_layouts_LAYOUTLIST_IntCon_OptExp_attrs___term__cons_Present);
                
                } else {
                  if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 37)  || (lookAheadChar == 41)  || (lookAheadChar == 44)  || (lookAheadChar == 62)  || (lookAheadChar == 93)  || (lookAheadChar == 123)  || (lookAheadChar == 125) ) {
                  // prod([],sort("OptExp"),attrs([term(cons("Absent"))]))
	expect(prod___OptExp_attrs___term__cons_Absent, OptExp.prod___OptExp_attrs___term__cons_Absent);
                
               }
                }
          }
	
      public void Renaming() {
            
            if ((lookAheadChar == 34) || ((lookAheadChar >= 39) && (lookAheadChar <= 40))  || (lookAheadChar == 60)  || ((lookAheadChar >= 65) && (lookAheadChar <= 91))  || ((lookAheadChar >= 96) && (lookAheadChar <= 123))  || (lookAheadChar == 126) ) {
                  // prod([sort("Production"),layouts("LAYOUTLIST"),lit("=\>"),layouts("LAYOUTLIST"),sort("Production")],sort("Renaming"),attrs([term(cons("production"))]))
	expect(prod___Production_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Production_Renaming_attrs___term__cons_production, Renaming.prod___Production_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Production_Renaming_attrs___term__cons_production);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("=\>"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Renaming"),attrs([term(cons("Symbol"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_Renaming_attrs___term__cons_Symbol, Renaming.prod___Symbol_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_Renaming_attrs___term__cons_Symbol);
                
                } else {
                  if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 37)  || (lookAheadChar == 45) ) {
                  // prod([sort("Production"),layouts("LAYOUTLIST"),lit("=\>"),layouts("LAYOUTLIST"),sort("Production")],sort("Renaming"),attrs([term(cons("production"))]))
	expect(prod___Production_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Production_Renaming_attrs___term__cons_production, Renaming.prod___Production_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Production_Renaming_attrs___term__cons_production);
                
               }
                }
          }
	
      public void ImpSection() {
            
            if ((lookAheadChar == 60) || (lookAheadChar == 105) ) {
                  // prod([lit("imports"),layouts("LAYOUTLIST"),sort("Imports")],sort("ImpSection"),attrs([term(cons("Imports"))]))
	expect(prod___lit_imports_layouts_LAYOUTLIST_Imports_ImpSection_attrs___term__cons_Imports, ImpSection.prod___lit_imports_layouts_LAYOUTLIST_Imports_ImpSection_attrs___term__cons_Imports);
                
               }
          }
	
      public void CharClass() {
            
            if ((lookAheadChar == 126)) {
                  // prod([sort("CharClass"),layouts("LAYOUTLIST"),lit("/\\"),layouts("LAYOUTLIST"),sort("CharClass")],sort("CharClass"),attrs([term(cons("ISect"))]))
	expect(prod___CharClass_layouts_LAYOUTLIST_lit___47_92_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_ISect, CharClass.prod___CharClass_layouts_LAYOUTLIST_lit___47_92_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_ISect);
                // prod([sort("CharClass"),layouts("LAYOUTLIST"),lit("/"),layouts("LAYOUTLIST"),sort("CharClass")],sort("CharClass"),attrs([term(cons("Diff"))]))
	expect(prod___CharClass_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Diff, CharClass.prod___CharClass_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Diff);
                // prod([lit("~"),layouts("LAYOUTLIST"),sort("CharClass")],sort("CharClass"),attrs([term(cons("Comp"))]))
	expect(prod___lit___126_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Comp, CharClass.prod___lit___126_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Comp);
                // prod([sort("CharClass"),layouts("LAYOUTLIST"),lit("\\/"),layouts("LAYOUTLIST"),sort("CharClass")],sort("CharClass"),attrs([term(cons("Union"))]))
	expect(prod___CharClass_layouts_LAYOUTLIST_lit___92_47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Union, CharClass.prod___CharClass_layouts_LAYOUTLIST_lit___92_47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Union);
                
                } else {
                  if ((lookAheadChar == 91)) {
                  // prod([sort("CharClass"),layouts("LAYOUTLIST"),lit("/\\"),layouts("LAYOUTLIST"),sort("CharClass")],sort("CharClass"),attrs([term(cons("ISect"))]))
	expect(prod___CharClass_layouts_LAYOUTLIST_lit___47_92_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_ISect, CharClass.prod___CharClass_layouts_LAYOUTLIST_lit___47_92_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_ISect);
                // prod([lit("["),layouts("LAYOUTLIST"),sort("OptCharRanges"),layouts("LAYOUTLIST"),lit("]")],sort("CharClass"),attrs([term(cons("SimpleCharClass"))]))
	expect(prod___lit___91_layouts_LAYOUTLIST_OptCharRanges_layouts_LAYOUTLIST_lit___93_CharClass_attrs___term__cons_SimpleCharClass, CharClass.prod___lit___91_layouts_LAYOUTLIST_OptCharRanges_layouts_LAYOUTLIST_lit___93_CharClass_attrs___term__cons_SimpleCharClass);
                // prod([sort("CharClass"),layouts("LAYOUTLIST"),lit("/"),layouts("LAYOUTLIST"),sort("CharClass")],sort("CharClass"),attrs([term(cons("Diff"))]))
	expect(prod___CharClass_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Diff, CharClass.prod___CharClass_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Diff);
                // prod([sort("CharClass"),layouts("LAYOUTLIST"),lit("\\/"),layouts("LAYOUTLIST"),sort("CharClass")],sort("CharClass"),attrs([term(cons("Union"))]))
	expect(prod___CharClass_layouts_LAYOUTLIST_lit___92_47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Union, CharClass.prod___CharClass_layouts_LAYOUTLIST_lit___92_47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Union);
                
                } else {
                  if ((lookAheadChar == 60)) {
                  // prod([sort("CharClass"),layouts("LAYOUTLIST"),lit("/\\"),layouts("LAYOUTLIST"),sort("CharClass")],sort("CharClass"),attrs([term(cons("ISect"))]))
	expect(prod___CharClass_layouts_LAYOUTLIST_lit___47_92_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_ISect, CharClass.prod___CharClass_layouts_LAYOUTLIST_lit___47_92_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_ISect);
                // prod([lit("["),layouts("LAYOUTLIST"),sort("OptCharRanges"),layouts("LAYOUTLIST"),lit("]")],sort("CharClass"),attrs([term(cons("SimpleCharClass"))]))
	expect(prod___lit___91_layouts_LAYOUTLIST_OptCharRanges_layouts_LAYOUTLIST_lit___93_CharClass_attrs___term__cons_SimpleCharClass, CharClass.prod___lit___91_layouts_LAYOUTLIST_OptCharRanges_layouts_LAYOUTLIST_lit___93_CharClass_attrs___term__cons_SimpleCharClass);
                // prod([sort("CharClass"),layouts("LAYOUTLIST"),lit("/"),layouts("LAYOUTLIST"),sort("CharClass")],sort("CharClass"),attrs([term(cons("Diff"))]))
	expect(prod___CharClass_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Diff, CharClass.prod___CharClass_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Diff);
                // prod([lit("~"),layouts("LAYOUTLIST"),sort("CharClass")],sort("CharClass"),attrs([term(cons("Comp"))]))
	expect(prod___lit___126_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Comp, CharClass.prod___lit___126_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Comp);
                // prod([lit("("),layouts("LAYOUTLIST"),sort("CharClass"),layouts("LAYOUTLIST"),lit(")")],sort("CharClass"),attrs([term(cons("Bracket"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_CharClass_layouts_LAYOUTLIST_lit___41_CharClass_attrs___term__cons_Bracket, CharClass.prod___lit___40_layouts_LAYOUTLIST_CharClass_layouts_LAYOUTLIST_lit___41_CharClass_attrs___term__cons_Bracket);
                // prod([sort("CharClass"),layouts("LAYOUTLIST"),lit("\\/"),layouts("LAYOUTLIST"),sort("CharClass")],sort("CharClass"),attrs([term(cons("Union"))]))
	expect(prod___CharClass_layouts_LAYOUTLIST_lit___92_47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Union, CharClass.prod___CharClass_layouts_LAYOUTLIST_lit___92_47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Union);
                
                } else {
                  if ((lookAheadChar == 40)) {
                  // prod([sort("CharClass"),layouts("LAYOUTLIST"),lit("/\\"),layouts("LAYOUTLIST"),sort("CharClass")],sort("CharClass"),attrs([term(cons("ISect"))]))
	expect(prod___CharClass_layouts_LAYOUTLIST_lit___47_92_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_ISect, CharClass.prod___CharClass_layouts_LAYOUTLIST_lit___47_92_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_ISect);
                // prod([sort("CharClass"),layouts("LAYOUTLIST"),lit("/"),layouts("LAYOUTLIST"),sort("CharClass")],sort("CharClass"),attrs([term(cons("Diff"))]))
	expect(prod___CharClass_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Diff, CharClass.prod___CharClass_layouts_LAYOUTLIST_lit___47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Diff);
                // prod([lit("("),layouts("LAYOUTLIST"),sort("CharClass"),layouts("LAYOUTLIST"),lit(")")],sort("CharClass"),attrs([term(cons("Bracket"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_CharClass_layouts_LAYOUTLIST_lit___41_CharClass_attrs___term__cons_Bracket, CharClass.prod___lit___40_layouts_LAYOUTLIST_CharClass_layouts_LAYOUTLIST_lit___41_CharClass_attrs___term__cons_Bracket);
                // prod([sort("CharClass"),layouts("LAYOUTLIST"),lit("\\/"),layouts("LAYOUTLIST"),sort("CharClass")],sort("CharClass"),attrs([term(cons("Union"))]))
	expect(prod___CharClass_layouts_LAYOUTLIST_lit___92_47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Union, CharClass.prod___CharClass_layouts_LAYOUTLIST_lit___92_47_layouts_LAYOUTLIST_CharClass_CharClass_attrs___term__cons_Union);
                
               }
                }
                }
                }
          }
	
      public void FunctionName() {
            
            if (((lookAheadChar >= 65) && (lookAheadChar <= 90)) || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([sort("IdCon")],sort("FunctionName"),attrs([term(cons("UnquotedFun"))]))
	expect(prod___IdCon_FunctionName_attrs___term__cons_UnquotedFun, FunctionName.prod___IdCon_FunctionName_attrs___term__cons_UnquotedFun);
                
                } else {
                  if ((lookAheadChar == 34)) {
                  // prod([sort("StrCon")],sort("FunctionName"),attrs([term(cons("QuotedFun"))]))
	expect(prod___StrCon_FunctionName_attrs___term__cons_QuotedFun, FunctionName.prod___StrCon_FunctionName_attrs___term__cons_QuotedFun);
                
                } else {
                  if ((lookAheadChar == 60)) {
                  // prod([sort("IdCon")],sort("FunctionName"),attrs([term(cons("UnquotedFun"))]))
	expect(prod___IdCon_FunctionName_attrs___term__cons_UnquotedFun, FunctionName.prod___IdCon_FunctionName_attrs___term__cons_UnquotedFun);
                // prod([sort("StrCon")],sort("FunctionName"),attrs([term(cons("QuotedFun"))]))
	expect(prod___StrCon_FunctionName_attrs___term__cons_QuotedFun, FunctionName.prod___StrCon_FunctionName_attrs___term__cons_QuotedFun);
                
               }
                }
                }
          }
	
      public void SingleQuotedStrCon() {
            
            if ((lookAheadChar == 39) || (lookAheadChar == 60) ) {
                  // prod([\char-class([range(39,39)]),sort("SingleQuotedStrChar"),\char-class([range(39,39)])],sort("SingleQuotedStrCon"),attrs([term(cons("Default")),lex()]))
	expect(prod___char_class___range__39_39_SingleQuotedStrChar_char_class___range__39_39_SingleQuotedStrCon_attrs___term__cons_Default_lex, SingleQuotedStrCon.prod___char_class___range__39_39_SingleQuotedStrChar_char_class___range__39_39_SingleQuotedStrCon_attrs___term__cons_Default_lex);
                
               }
          }
	
      public void Restriction() {
            
            if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 34)  || (lookAheadChar == 37)  || ((lookAheadChar >= 39) && (lookAheadChar <= 40))  || (lookAheadChar == 45)  || (lookAheadChar == 60)  || ((lookAheadChar >= 65) && (lookAheadChar <= 91))  || ((lookAheadChar >= 96) && (lookAheadChar <= 123))  || (lookAheadChar == 126) ) {
                  // prod([sort("Symbols"),layouts("LAYOUTLIST"),lit("-/-"),layouts("LAYOUTLIST"),sort("Lookaheads")],sort("Restriction"),attrs([term(cons("Follow"))]))
	expect(prod___Symbols_layouts_LAYOUTLIST_lit___45_47_45_layouts_LAYOUTLIST_Lookaheads_Restriction_attrs___term__cons_Follow, Restriction.prod___Symbols_layouts_LAYOUTLIST_lit___45_47_45_layouts_LAYOUTLIST_Lookaheads_Restriction_attrs___term__cons_Follow);
                
               }
          }
	
      public void ModuleWord() {
            
            if ((lookAheadChar == 45) || ((lookAheadChar >= 48) && (lookAheadChar <= 57))  || (lookAheadChar == 60)  || ((lookAheadChar >= 65) && (lookAheadChar <= 90))  || (lookAheadChar == 95)  || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([iter(\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)]))],sort("ModuleWord"),attrs([term(cons("Word")),lex()]))
	expect(prod___iter__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_ModuleWord_attrs___term__cons_Word_lex, ModuleWord.prod___iter__char_class___range__45_45_range__48_57_range__65_90_range__95_95_range__97_122_ModuleWord_attrs___term__cons_Word_lex);
                
               }
          }
	
      public void ArgumentIndicator() {
            
            if ((lookAheadChar == 60)) {
                  // prod([lit("\<"),layouts("LAYOUTLIST"),\iter-seps(sort("NatCon"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit("\>")],sort("ArgumentIndicator"),attrs([term(cons("Default"))]))
	expect(prod___lit___60_layouts_LAYOUTLIST_iter_seps__NatCon__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ArgumentIndicator_attrs___term__cons_Default, ArgumentIndicator.prod___lit___60_layouts_LAYOUTLIST_iter_seps__NatCon__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_ArgumentIndicator_attrs___term__cons_Default);
                
               }
          }
	
      public void StrCon() {
            
            if ((lookAheadChar == 34) || (lookAheadChar == 60) ) {
                  // prod([\char-class([range(34,34)]),\iter-star(sort("StrChar")),\char-class([range(34,34)])],sort("StrCon"),attrs([term(cons("Default")),lex()]))
	expect(prod___char_class___range__34_34_iter_star__StrChar_char_class___range__34_34_StrCon_attrs___term__cons_Default_lex, StrCon.prod___char_class___range__34_34_iter_star__StrChar_char_class___range__34_34_StrCon_attrs___term__cons_Default_lex);
                
               }
          }
	
      public void Sort() {
            
            if ((lookAheadChar == 60) || (lookAheadChar == 76) ) {
                  // prod([lit("LAYOUT")],sort("Sort"),attrs([reject()]))
	expectReject(prod___lit_LAYOUT_Sort_attrs___reject, Sort.prod___lit_LAYOUT_Sort_attrs___reject);
               }
                
                if ((lookAheadChar == 60) || ((lookAheadChar >= 65) && (lookAheadChar <= 90)) ) {
                  // prod([\char-class([range(65,90)]),\iter-star(\char-class([range(45,45),range(48,57),range(65,90),range(97,122)])),\char-class([range(48,57),range(65,90),range(97,122)])],sort("Sort"),attrs([term(cons("MoreChars")),lex()]))
	expect(prod___char_class___range__65_90_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_char_class___range__48_57_range__65_90_range__97_122_Sort_attrs___term__cons_MoreChars_lex, Sort.prod___char_class___range__65_90_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_char_class___range__48_57_range__65_90_range__97_122_Sort_attrs___term__cons_MoreChars_lex);
                // prod([\char-class([range(65,90)])],sort("Sort"),attrs([term(cons("OneChar")),lex()]))
	expect(prod___char_class___range__65_90_Sort_attrs___term__cons_OneChar_lex, Sort.prod___char_class___range__65_90_Sort_attrs___term__cons_OneChar_lex);
                
               }
          }
	
      public void OptCharRanges() {
            
            if ((lookAheadChar == 40) || ((lookAheadChar >= 48) && (lookAheadChar <= 57))  || (lookAheadChar == 60)  || ((lookAheadChar >= 65) && (lookAheadChar <= 90))  || (lookAheadChar == 92)  || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([sort("CharRanges")],sort("OptCharRanges"),attrs([term(cons("Present"))]))
	expect(prod___CharRanges_OptCharRanges_attrs___term__cons_Present, OptCharRanges.prod___CharRanges_OptCharRanges_attrs___term__cons_Present);
                
                } else {
                  if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 37)  || (lookAheadChar == 93) ) {
                  // prod([],sort("OptCharRanges"),attrs([term(cons("Absent"))]))
	expect(prod___OptCharRanges_attrs___term__cons_Absent, OptCharRanges.prod___OptCharRanges_attrs___term__cons_Absent);
                
               }
                }
          }
	
      public void Productions() {
            
            if (lookAheadChar == 0) {
                  // prod([\iter-star-seps(sort("Production"),[layouts("LAYOUTLIST")])],sort("Productions"),\no-attrs())
	expect(prod___iter_star_seps__Production__layouts_LAYOUTLIST_Productions_no_attrs, Productions.prod___iter_star_seps__Production__layouts_LAYOUTLIST_Productions_no_attrs);
                
                } else {
                  if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 34)  || (lookAheadChar == 37)  || ((lookAheadChar >= 39) && (lookAheadChar <= 41))  || (lookAheadChar == 45)  || (lookAheadChar == 60)  || ((lookAheadChar >= 65) && (lookAheadChar <= 91))  || ((lookAheadChar >= 96) && (lookAheadChar <= 123))  || ((lookAheadChar >= 125) && (lookAheadChar <= 126)) ) {
                  // prod([\iter-star-seps(sort("Production"),[layouts("LAYOUTLIST")])],sort("Productions"),\no-attrs())
	expect(prod___iter_star_seps__Production__layouts_LAYOUTLIST_Productions_no_attrs, Productions.prod___iter_star_seps__Production__layouts_LAYOUTLIST_Productions_no_attrs);
                
               }
                }
          }
	
      public void Symbols() {
            
            if ((lookAheadChar == 34) || (lookAheadChar == 60) ) {
                  // prod([sort("StrCon"),layouts("LAYOUTLIST"),lit("("),layouts("LAYOUTLIST"),\iter-star-seps(sort("Symbol"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit(")")],sort("Symbols"),attrs([reject()]))
	expectReject(prod___StrCon_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbols_attrs___reject, Symbols.prod___StrCon_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbols_attrs___reject);
               }
                
                if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 34)  || (lookAheadChar == 37)  || ((lookAheadChar >= 39) && (lookAheadChar <= 41))  || (lookAheadChar == 45)  || ((lookAheadChar >= 60) && (lookAheadChar <= 61))  || ((lookAheadChar >= 65) && (lookAheadChar <= 91))  || (lookAheadChar == 93)  || ((lookAheadChar >= 96) && (lookAheadChar <= 123))  || (lookAheadChar == 126) ) {
                  // prod([\iter-star-seps(sort("Symbol"),[layouts("LAYOUTLIST")])],sort("Symbols"),\no-attrs())
	expect(prod___iter_star_seps__Symbol__layouts_LAYOUTLIST_Symbols_no_attrs, Symbols.prod___iter_star_seps__Symbol__layouts_LAYOUTLIST_Symbols_no_attrs);
                
                } else {
                  if (lookAheadChar == 0) {
                  // prod([\iter-star-seps(sort("Symbol"),[layouts("LAYOUTLIST")])],sort("Symbols"),\no-attrs())
	expect(prod___iter_star_seps__Symbol__layouts_LAYOUTLIST_Symbols_no_attrs, Symbols.prod___iter_star_seps__Symbol__layouts_LAYOUTLIST_Symbols_no_attrs);
                
               }
                }
          }
	
      public void Symbol() {
            
            if ((lookAheadChar == 39)) {
                  // prod([sort("SingleQuotedStrCon")],sort("Symbol"),attrs([term(cons("CILit"))]))
	expect(prod___SingleQuotedStrCon_Symbol_attrs___term__cons_CILit, Symbol.prod___SingleQuotedStrCon_Symbol_attrs___term__cons_CILit);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("*")],sort("Symbol"),attrs([term(cons("IterStar"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("+")],sort("Symbol"),attrs([term(cons("Iter"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("?")],sort("Symbol"),attrs([term(cons("Opt"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt);
                
                } else {
                  if ((lookAheadChar == 34)) {
                  // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("*")],sort("Symbol"),attrs([term(cons("IterStar"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar);
                // prod([sort("StrCon")],sort("Symbol"),attrs([term(cons("Lit"))]))
	expect(prod___StrCon_Symbol_attrs___term__cons_Lit, Symbol.prod___StrCon_Symbol_attrs___term__cons_Lit);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("+")],sort("Symbol"),attrs([term(cons("Iter"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter);
                // prod([sort("Label"),layouts("LAYOUTLIST"),lit(":"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),\no-attrs())
	expect(prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs, Symbol.prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("?")],sort("Symbol"),attrs([term(cons("Opt"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt);
                
                } else {
                  if (((lookAheadChar >= 65) && (lookAheadChar <= 75)) || ((lookAheadChar >= 77) && (lookAheadChar <= 90)) ) {
                  // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right);
                // prod([sort("Sort"),layouts("LAYOUTLIST"),lit("[["),layouts("LAYOUTLIST"),\iter-seps(sort("Symbol"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit("]]")],sort("Symbol"),attrs([term(cons("ParameterizedSort"))]))
	expect(prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort, Symbol.prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("*")],sort("Symbol"),attrs([term(cons("IterStar"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar);
                // prod([sort("Sort")],sort("Symbol"),attrs([term(cons("Sort"))]))
	expect(prod___Sort_Symbol_attrs___term__cons_Sort, Symbol.prod___Sort_Symbol_attrs___term__cons_Sort);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("+")],sort("Symbol"),attrs([term(cons("Iter"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter);
                // prod([sort("Label"),layouts("LAYOUTLIST"),lit(":"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),\no-attrs())
	expect(prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs, Symbol.prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("?")],sort("Symbol"),attrs([term(cons("Opt"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt);
                
                } else {
                  if (((lookAheadChar >= 97) && (lookAheadChar <= 123))) {
                  // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("*")],sort("Symbol"),attrs([term(cons("IterStar"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar);
                // prod([lit("{"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit("}"),layouts("LAYOUTLIST"),lit("*")],sort("Symbol"),attrs([term(cons("IterStarSep"))]))
	expect(prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep, Symbol.prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep);
                // prod([lit("{"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit("}"),layouts("LAYOUTLIST"),lit("+")],sort("Symbol"),attrs([term(cons("IterSep"))]))
	expect(prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep, Symbol.prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("+")],sort("Symbol"),attrs([term(cons("Iter"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter);
                // prod([sort("Label"),layouts("LAYOUTLIST"),lit(":"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),\no-attrs())
	expect(prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs, Symbol.prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("?")],sort("Symbol"),attrs([term(cons("Opt"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt);
                
                } else {
                  if (((lookAheadChar >= 76) && (lookAheadChar <= 91))) {
                  // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right);
                // prod([sort("Sort"),layouts("LAYOUTLIST"),lit("[["),layouts("LAYOUTLIST"),\iter-seps(sort("Symbol"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit("]]")],sort("Symbol"),attrs([term(cons("ParameterizedSort"))]))
	expect(prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort, Symbol.prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("*")],sort("Symbol"),attrs([term(cons("IterStar"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar);
                // prod([sort("Sort")],sort("Symbol"),attrs([term(cons("Sort"))]))
	expect(prod___Sort_Symbol_attrs___term__cons_Sort, Symbol.prod___Sort_Symbol_attrs___term__cons_Sort);
                // prod([lit("LAYOUT")],sort("Symbol"),attrs([term(cons("Layout"))]))
	expect(prod___lit_LAYOUT_Symbol_attrs___term__cons_Layout, Symbol.prod___lit_LAYOUT_Symbol_attrs___term__cons_Layout);
                // prod([sort("CharClass")],sort("Symbol"),attrs([term(cons("CharClass"))]))
	expect(prod___CharClass_Symbol_attrs___term__cons_CharClass, Symbol.prod___CharClass_Symbol_attrs___term__cons_CharClass);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("+")],sort("Symbol"),attrs([term(cons("Iter"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter);
                // prod([sort("Label"),layouts("LAYOUTLIST"),lit(":"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),\no-attrs())
	expect(prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs, Symbol.prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("?")],sort("Symbol"),attrs([term(cons("Opt"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt);
                
                } else {
                  if ((lookAheadChar == 96)) {
                  // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("*")],sort("Symbol"),attrs([term(cons("IterStar"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar);
                // prod([lit("`"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit("`")],sort("Symbol"),attrs([term(cons("Lifting"))]))
	expect(prod___lit___96_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___96_Symbol_attrs___term__cons_Lifting, Symbol.prod___lit___96_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___96_Symbol_attrs___term__cons_Lifting);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("+")],sort("Symbol"),attrs([term(cons("Iter"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("?")],sort("Symbol"),attrs([term(cons("Opt"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt);
                
                } else {
                  if ((lookAheadChar == 60)) {
                  // prod([lit("("),layouts("LAYOUTLIST"),sort("Symbols"),layouts("LAYOUTLIST"),lit("=\>"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit(")")],sort("Symbol"),attrs([term(cons("Func"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Func, Symbol.prod___lit___40_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Func);
                // prod([lit("("),layouts("LAYOUTLIST"),lit(")")],sort("Symbol"),attrs([term(cons("Empty"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Empty, Symbol.prod___lit___40_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Empty);
                // prod([lit("{"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit("}"),layouts("LAYOUTLIST"),lit("*")],sort("Symbol"),attrs([term(cons("IterStarSep"))]))
	expect(prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep, Symbol.prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep);
                // prod([lit("("),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit("-\>"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit(")")],sort("Symbol"),attrs([term(cons("Strategy"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Strategy, Symbol.prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Strategy);
                // prod([lit("("),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),\iter-seps(sort("Symbol"),[layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit(")")],sort("Symbol"),attrs([term(cons("Seq"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Seq, Symbol.prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Seq);
                // prod([lit("("),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit(")")],sort("Symbol"),attrs([term(cons("Bracket"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Bracket, Symbol.prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Bracket);
                // prod([lit("{"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit("}"),layouts("LAYOUTLIST"),lit("+")],sort("Symbol"),attrs([term(cons("IterSep"))]))
	expect(prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep, Symbol.prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep);
                // prod([lit("\<START\>")],sort("Symbol"),attrs([term(cons("Start"))]))
	expect(prod___lit___60_83_84_65_82_84_62_Symbol_attrs___term__cons_Start, Symbol.prod___lit___60_83_84_65_82_84_62_Symbol_attrs___term__cons_Start);
                // prod([sort("CharClass")],sort("Symbol"),attrs([term(cons("CharClass"))]))
	expect(prod___CharClass_Symbol_attrs___term__cons_CharClass, Symbol.prod___CharClass_Symbol_attrs___term__cons_CharClass);
                // prod([sort("StrCon")],sort("Symbol"),attrs([term(cons("Lit"))]))
	expect(prod___StrCon_Symbol_attrs___term__cons_Lit, Symbol.prod___StrCon_Symbol_attrs___term__cons_Lit);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("+")],sort("Symbol"),attrs([term(cons("Iter"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter);
                // prod([sort("Label"),layouts("LAYOUTLIST"),lit(":"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),\no-attrs())
	expect(prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs, Symbol.prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("?")],sort("Symbol"),attrs([term(cons("Opt"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt);
                // prod([sort("SingleQuotedStrCon")],sort("Symbol"),attrs([term(cons("CILit"))]))
	expect(prod___SingleQuotedStrCon_Symbol_attrs___term__cons_CILit, Symbol.prod___SingleQuotedStrCon_Symbol_attrs___term__cons_CILit);
                // prod([lit("\<"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit("-CF"),layouts("LAYOUTLIST"),lit("\>")],sort("Symbol"),attrs([term(cons("CF"))]))
	expect(prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__CF_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_CF, Symbol.prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__CF_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_CF);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right);
                // prod([lit("\<"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit("-LEX"),layouts("LAYOUTLIST"),lit("\>")],sort("Symbol"),attrs([term(cons("Lex"))]))
	expect(prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__LEX_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Lex, Symbol.prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__LEX_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Lex);
                // prod([sort("Sort"),layouts("LAYOUTLIST"),lit("[["),layouts("LAYOUTLIST"),\iter-seps(sort("Symbol"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit("]]")],sort("Symbol"),attrs([term(cons("ParameterizedSort"))]))
	expect(prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort, Symbol.prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort);
                // prod([lit("\<"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST"),\iter-seps(sort("Symbol"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit("\>")],sort("Symbol"),attrs([term(cons("Tuple"))]))
	expect(prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Tuple, Symbol.prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Tuple);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("*")],sort("Symbol"),attrs([term(cons("IterStar"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar);
                // prod([lit("\<"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit("-VAR"),layouts("LAYOUTLIST"),lit("\>")],sort("Symbol"),attrs([term(cons("Varsym"))]))
	expect(prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__VAR_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Varsym, Symbol.prod___lit___60_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit__VAR_layouts_LAYOUTLIST_lit___62_Symbol_attrs___term__cons_Varsym);
                // prod([lit("`"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit("`")],sort("Symbol"),attrs([term(cons("Lifting"))]))
	expect(prod___lit___96_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___96_Symbol_attrs___term__cons_Lifting, Symbol.prod___lit___96_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___96_Symbol_attrs___term__cons_Lifting);
                // prod([sort("Sort")],sort("Symbol"),attrs([term(cons("Sort"))]))
	expect(prod___Sort_Symbol_attrs___term__cons_Sort, Symbol.prod___Sort_Symbol_attrs___term__cons_Sort);
                // prod([lit("LAYOUT")],sort("Symbol"),attrs([term(cons("Layout"))]))
	expect(prod___lit_LAYOUT_Symbol_attrs___term__cons_Layout, Symbol.prod___lit_LAYOUT_Symbol_attrs___term__cons_Layout);
                // prod([lit("\<Start\>")],sort("Symbol"),attrs([term(cons("FileStart"))]))
	expect(prod___lit___60_83_116_97_114_116_62_Symbol_attrs___term__cons_FileStart, Symbol.prod___lit___60_83_116_97_114_116_62_Symbol_attrs___term__cons_FileStart);
                
                } else {
                  if (((lookAheadChar >= 76) && (lookAheadChar <= 90))) {
                  // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right);
                // prod([sort("Sort"),layouts("LAYOUTLIST"),lit("[["),layouts("LAYOUTLIST"),\iter-seps(sort("Symbol"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit("]]")],sort("Symbol"),attrs([term(cons("ParameterizedSort"))]))
	expect(prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort, Symbol.prod___Sort_layouts_LAYOUTLIST_lit___91_91_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Symbol_attrs___term__cons_ParameterizedSort);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("*")],sort("Symbol"),attrs([term(cons("IterStar"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar);
                // prod([sort("Sort")],sort("Symbol"),attrs([term(cons("Sort"))]))
	expect(prod___Sort_Symbol_attrs___term__cons_Sort, Symbol.prod___Sort_Symbol_attrs___term__cons_Sort);
                // prod([lit("LAYOUT")],sort("Symbol"),attrs([term(cons("Layout"))]))
	expect(prod___lit_LAYOUT_Symbol_attrs___term__cons_Layout, Symbol.prod___lit_LAYOUT_Symbol_attrs___term__cons_Layout);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("+")],sort("Symbol"),attrs([term(cons("Iter"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter);
                // prod([sort("Label"),layouts("LAYOUTLIST"),lit(":"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),\no-attrs())
	expect(prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs, Symbol.prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("?")],sort("Symbol"),attrs([term(cons("Opt"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt);
                
                } else {
                  if ((lookAheadChar == 123)) {
                  // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("*")],sort("Symbol"),attrs([term(cons("IterStar"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar);
                // prod([lit("{"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit("}"),layouts("LAYOUTLIST"),lit("*")],sort("Symbol"),attrs([term(cons("IterStarSep"))]))
	expect(prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep, Symbol.prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStarSep);
                // prod([lit("{"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit("}"),layouts("LAYOUTLIST"),lit("+")],sort("Symbol"),attrs([term(cons("IterSep"))]))
	expect(prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep, Symbol.prod___lit___123_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___125_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_IterSep);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("+")],sort("Symbol"),attrs([term(cons("Iter"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("?")],sort("Symbol"),attrs([term(cons("Opt"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt);
                
                } else {
                  if ((lookAheadChar == 91) || (lookAheadChar == 126) ) {
                  // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("*")],sort("Symbol"),attrs([term(cons("IterStar"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar);
                // prod([sort("CharClass")],sort("Symbol"),attrs([term(cons("CharClass"))]))
	expect(prod___CharClass_Symbol_attrs___term__cons_CharClass, Symbol.prod___CharClass_Symbol_attrs___term__cons_CharClass);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("+")],sort("Symbol"),attrs([term(cons("Iter"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("?")],sort("Symbol"),attrs([term(cons("Opt"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt);
                
                } else {
                  if (((lookAheadChar >= 97) && (lookAheadChar <= 122))) {
                  // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("*")],sort("Symbol"),attrs([term(cons("IterStar"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("+")],sort("Symbol"),attrs([term(cons("Iter"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter);
                // prod([sort("Label"),layouts("LAYOUTLIST"),lit(":"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),\no-attrs())
	expect(prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs, Symbol.prod___Label_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Symbol_Symbol_no_attrs);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("?")],sort("Symbol"),attrs([term(cons("Opt"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt);
                
                } else {
                  if ((lookAheadChar == 40)) {
                  // prod([lit("("),layouts("LAYOUTLIST"),sort("Symbols"),layouts("LAYOUTLIST"),lit("=\>"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit(")")],sort("Symbol"),attrs([term(cons("Func"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Func, Symbol.prod___lit___40_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___61_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Func);
                // prod([lit("("),layouts("LAYOUTLIST"),lit(")")],sort("Symbol"),attrs([term(cons("Empty"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Empty, Symbol.prod___lit___40_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Empty);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Symbol"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Symbol_Symbol_attrs___term__cons_Alt_assoc__right);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("*")],sort("Symbol"),attrs([term(cons("IterStar"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___42_Symbol_attrs___term__cons_IterStar);
                // prod([lit("("),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit("-\>"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit(")")],sort("Symbol"),attrs([term(cons("Strategy"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Strategy, Symbol.prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Strategy);
                // prod([lit("("),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),\iter-seps(sort("Symbol"),[layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit(")")],sort("Symbol"),attrs([term(cons("Seq"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Seq, Symbol.prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_iter_seps__Symbol__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Seq);
                // prod([lit("("),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),lit(")")],sort("Symbol"),attrs([term(cons("Bracket"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Bracket, Symbol.prod___lit___40_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_lit___41_Symbol_attrs___term__cons_Bracket);
                // prod([sort("CharClass")],sort("Symbol"),attrs([term(cons("CharClass"))]))
	expect(prod___CharClass_Symbol_attrs___term__cons_CharClass, Symbol.prod___CharClass_Symbol_attrs___term__cons_CharClass);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("+")],sort("Symbol"),attrs([term(cons("Iter"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___43_Symbol_attrs___term__cons_Iter);
                // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("?")],sort("Symbol"),attrs([term(cons("Opt"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt, Symbol.prod___Symbol_layouts_LAYOUTLIST_lit___63_Symbol_attrs___term__cons_Opt);
                
               }
                }
                }
                }
                }
                }
                }
                }
                }
                }
                }
                }
          }
	
      public void ModuleId() {
            
            if ((lookAheadChar == 60)) {
                  // prod([sort("ModuleWord"),lit("/"),sort("ModuleId")],sort("ModuleId"),attrs([term(cons("Path")),lex()]))
	expect(prod___ModuleWord_lit___47_ModuleId_ModuleId_attrs___term__cons_Path_lex, ModuleId.prod___ModuleWord_lit___47_ModuleId_ModuleId_attrs___term__cons_Path_lex);
                // prod([lit("/"),sort("ModuleId")],sort("ModuleId"),attrs([term(cons("Root")),lex()]))
	expect(prod___lit___47_ModuleId_ModuleId_attrs___term__cons_Root_lex, ModuleId.prod___lit___47_ModuleId_ModuleId_attrs___term__cons_Root_lex);
                // prod([sort("ModuleWord")],sort("ModuleId"),attrs([term(cons("Leaf")),lex()]))
	expect(prod___ModuleWord_ModuleId_attrs___term__cons_Leaf_lex, ModuleId.prod___ModuleWord_ModuleId_attrs___term__cons_Leaf_lex);
                
                } else {
                  if ((lookAheadChar == 45) || ((lookAheadChar >= 48) && (lookAheadChar <= 57))  || ((lookAheadChar >= 65) && (lookAheadChar <= 90))  || (lookAheadChar == 95)  || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([sort("ModuleWord"),lit("/"),sort("ModuleId")],sort("ModuleId"),attrs([term(cons("Path")),lex()]))
	expect(prod___ModuleWord_lit___47_ModuleId_ModuleId_attrs___term__cons_Path_lex, ModuleId.prod___ModuleWord_lit___47_ModuleId_ModuleId_attrs___term__cons_Path_lex);
                // prod([sort("ModuleWord")],sort("ModuleId"),attrs([term(cons("Leaf")),lex()]))
	expect(prod___ModuleWord_ModuleId_attrs___term__cons_Leaf_lex, ModuleId.prod___ModuleWord_ModuleId_attrs___term__cons_Leaf_lex);
                
                } else {
                  if ((lookAheadChar == 47)) {
                  // prod([lit("/"),sort("ModuleId")],sort("ModuleId"),attrs([term(cons("Root")),lex()]))
	expect(prod___lit___47_ModuleId_ModuleId_attrs___term__cons_Root_lex, ModuleId.prod___lit___47_ModuleId_ModuleId_attrs___term__cons_Root_lex);
                
               }
                }
                }
          }
	
      public void Priority() {
            
            if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 34)  || (lookAheadChar == 37)  || ((lookAheadChar >= 39) && (lookAheadChar <= 40))  || (lookAheadChar == 45)  || (lookAheadChar == 60)  || ((lookAheadChar >= 65) && (lookAheadChar <= 91))  || ((lookAheadChar >= 96) && (lookAheadChar <= 123))  || (lookAheadChar == 126) ) {
                  // prod([sort("Group"),layouts("LAYOUTLIST"),sort("Associativity"),layouts("LAYOUTLIST"),sort("Group")],sort("Priority"),attrs([term(cons("Assoc"))]))
	expect(prod___Group_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_Group_Priority_attrs___term__cons_Assoc, Priority.prod___Group_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_Group_Priority_attrs___term__cons_Assoc);
                // prod([\iter-seps(sort("Group"),[layouts("LAYOUTLIST"),lit("\>"),layouts("LAYOUTLIST")])],sort("Priority"),attrs([term(cons("Chain"))]))
	expect(prod___iter_seps__Group__layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_Priority_attrs___term__cons_Chain, Priority.prod___iter_seps__Group__layouts_LAYOUTLIST_lit___62_layouts_LAYOUTLIST_Priority_attrs___term__cons_Chain);
                
               }
          }
	
      public void Import() {
            
            if ((lookAheadChar == 40)) {
                  // prod([lit("("),layouts("LAYOUTLIST"),sort("Import"),layouts("LAYOUTLIST"),lit(")")],sort("Import"),attrs([term(cons("Bracket"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_Import_layouts_LAYOUTLIST_lit___41_Import_attrs___term__cons_Bracket, Import.prod___lit___40_layouts_LAYOUTLIST_Import_layouts_LAYOUTLIST_lit___41_Import_attrs___term__cons_Bracket);
                
                } else {
                  if ((lookAheadChar == 45) || ((lookAheadChar >= 47) && (lookAheadChar <= 57))  || ((lookAheadChar >= 65) && (lookAheadChar <= 90))  || (lookAheadChar == 95)  || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([sort("ModuleName")],sort("Import"),attrs([term(cons("Module"))]))
	expect(prod___ModuleName_Import_attrs___term__cons_Module, Import.prod___ModuleName_Import_attrs___term__cons_Module);
                // prod([sort("ModuleName"),layouts("LAYOUTLIST"),sort("Renamings")],sort("Import"),attrs([term(cons("RenamedModule"))]))
	expect(prod___ModuleName_layouts_LAYOUTLIST_Renamings_Import_attrs___term__cons_RenamedModule, Import.prod___ModuleName_layouts_LAYOUTLIST_Renamings_Import_attrs___term__cons_RenamedModule);
                
                } else {
                  if ((lookAheadChar == 60)) {
                  // prod([sort("ModuleName")],sort("Import"),attrs([term(cons("Module"))]))
	expect(prod___ModuleName_Import_attrs___term__cons_Module, Import.prod___ModuleName_Import_attrs___term__cons_Module);
                // prod([lit("("),layouts("LAYOUTLIST"),sort("Import"),layouts("LAYOUTLIST"),lit(")")],sort("Import"),attrs([term(cons("Bracket"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_Import_layouts_LAYOUTLIST_lit___41_Import_attrs___term__cons_Bracket, Import.prod___lit___40_layouts_LAYOUTLIST_Import_layouts_LAYOUTLIST_lit___41_Import_attrs___term__cons_Bracket);
                // prod([sort("ModuleName"),layouts("LAYOUTLIST"),sort("Renamings")],sort("Import"),attrs([term(cons("RenamedModule"))]))
	expect(prod___ModuleName_layouts_LAYOUTLIST_Renamings_Import_attrs___term__cons_RenamedModule, Import.prod___ModuleName_layouts_LAYOUTLIST_Renamings_Import_attrs___term__cons_RenamedModule);
                
               }
                }
                }
          }
	
      public void Section() {
            
            if ((lookAheadChar == 101)) {
                  // prod([lit("exports"),layouts("LAYOUTLIST"),sort("Grammar")],sort("Section"),attrs([term(cons("Exports"))]))
	expect(prod___lit_exports_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Exports, Section.prod___lit_exports_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Exports);
                
                } else {
                  if ((lookAheadChar == 60)) {
                  // prod([lit("exports"),layouts("LAYOUTLIST"),sort("Grammar")],sort("Section"),attrs([term(cons("Exports"))]))
	expect(prod___lit_exports_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Exports, Section.prod___lit_exports_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Exports);
                // prod([lit("hiddens"),layouts("LAYOUTLIST"),sort("Grammar")],sort("Section"),attrs([term(cons("Hiddens"))]))
	expect(prod___lit_hiddens_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Hiddens, Section.prod___lit_hiddens_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Hiddens);
                
                } else {
                  if ((lookAheadChar == 104)) {
                  // prod([lit("hiddens"),layouts("LAYOUTLIST"),sort("Grammar")],sort("Section"),attrs([term(cons("Hiddens"))]))
	expect(prod___lit_hiddens_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Hiddens, Section.prod___lit_hiddens_layouts_LAYOUTLIST_Grammar_Section_attrs___term__cons_Hiddens);
                
               }
                }
                }
          }
	
      public void NatCon() {
            
            if (((lookAheadChar >= 48) && (lookAheadChar <= 57)) || (lookAheadChar == 60) ) {
                  // prod([iter(\char-class([range(48,57)]))],sort("NatCon"),attrs([term(cons("Digits")),lex()]))
	expect(prod___iter__char_class___range__48_57_NatCon_attrs___term__cons_Digits_lex, NatCon.prod___iter__char_class___range__48_57_NatCon_attrs___term__cons_Digits_lex);
                
               }
          }
	
      public void ATermAttribute() {
            
            if ((lookAheadChar == 60) || (lookAheadChar == 114) ) {
                  // prod([lit("reject")],sort("ATermAttribute"),attrs([reject()]))
	expectReject(prod___lit_reject_ATermAttribute_attrs___reject, ATermAttribute.prod___lit_reject_ATermAttribute_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 112) ) {
                  // prod([lit("prefer")],sort("ATermAttribute"),attrs([reject()]))
	expectReject(prod___lit_prefer_ATermAttribute_attrs___reject, ATermAttribute.prod___lit_prefer_ATermAttribute_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 97)  || (lookAheadChar == 108)  || (lookAheadChar == 110)  || (lookAheadChar == 114) ) {
                  // prod([sort("Associativity")],sort("ATermAttribute"),attrs([reject()]))
	expectReject(prod___Associativity_ATermAttribute_attrs___reject, ATermAttribute.prod___Associativity_ATermAttribute_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 97) ) {
                  // prod([lit("avoid")],sort("ATermAttribute"),attrs([reject()]))
	expectReject(prod___lit_avoid_ATermAttribute_attrs___reject, ATermAttribute.prod___lit_avoid_ATermAttribute_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 98) ) {
                  // prod([lit("bracket")],sort("ATermAttribute"),attrs([reject()]))
	expectReject(prod___lit_bracket_ATermAttribute_attrs___reject, ATermAttribute.prod___lit_bracket_ATermAttribute_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 105) ) {
                  // prod([lit("id"),layouts("LAYOUTLIST"),lit("("),layouts("LAYOUTLIST"),sort("ModuleName"),layouts("LAYOUTLIST"),lit(")")],sort("ATermAttribute"),attrs([reject()]))
	expectReject(prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_ATermAttribute_attrs___reject, ATermAttribute.prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_ATermAttribute_attrs___reject);
               }
                
                if ((lookAheadChar == 34) || (lookAheadChar == 43)  || (lookAheadChar == 45)  || ((lookAheadChar >= 48) && (lookAheadChar <= 57))  || (lookAheadChar == 60)  || ((lookAheadChar >= 65) && (lookAheadChar <= 91))  || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([sort("ATerm")],sort("ATermAttribute"),attrs([term(cons("Default"))]))
	expect(prod___ATerm_ATermAttribute_attrs___term__cons_Default, ATermAttribute.prod___ATerm_ATermAttribute_attrs___term__cons_Default);
                
               }
          }
	
      public void Label() {
            
            if ((lookAheadChar == 60) || (lookAheadChar == 97)  || (lookAheadChar == 108)  || (lookAheadChar == 110)  || (lookAheadChar == 114) ) {
                  // prod([sort("Associativity")],sort("Label"),attrs([reject()]))
	expectReject(prod___Associativity_Label_attrs___reject, Label.prod___Associativity_Label_attrs___reject);
               }
                
                if ((lookAheadChar == 60)) {
                  // prod([sort("IdCon")],sort("Label"),attrs([term(cons("IdCon"))]))
	expect(prod___IdCon_Label_attrs___term__cons_IdCon, Label.prod___IdCon_Label_attrs___term__cons_IdCon);
                // prod([sort("StrCon")],sort("Label"),attrs([term(cons("Quoted"))]))
	expect(prod___StrCon_Label_attrs___term__cons_Quoted, Label.prod___StrCon_Label_attrs___term__cons_Quoted);
                
                } else {
                  if ((lookAheadChar == 34)) {
                  // prod([sort("StrCon")],sort("Label"),attrs([term(cons("Quoted"))]))
	expect(prod___StrCon_Label_attrs___term__cons_Quoted, Label.prod___StrCon_Label_attrs___term__cons_Quoted);
                
                } else {
                  if (((lookAheadChar >= 65) && (lookAheadChar <= 90)) || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([sort("IdCon")],sort("Label"),attrs([term(cons("IdCon"))]))
	expect(prod___IdCon_Label_attrs___term__cons_IdCon, Label.prod___IdCon_Label_attrs___term__cons_IdCon);
                
               }
                }
                }
          }
	
      public void ShortChar() {
            
            if ((lookAheadChar == 60)) {
                  // prod([\char-class([range(92,92)]),\char-class([range(0,47),range(58,64),range(91,96),range(110,110),range(114,114),range(116,116),range(123,65535)])],sort("ShortChar"),attrs([term(cons("Escaped")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__0_47_range__58_64_range__91_96_range__110_110_range__114_114_range__116_116_range__123_65535_ShortChar_attrs___term__cons_Escaped_lex, ShortChar.prod___char_class___range__92_92_char_class___range__0_47_range__58_64_range__91_96_range__110_110_range__114_114_range__116_116_range__123_65535_ShortChar_attrs___term__cons_Escaped_lex);
                // prod([\char-class([range(48,57),range(65,90),range(97,122)])],sort("ShortChar"),attrs([term(cons("Regular")),lex()]))
	expect(prod___char_class___range__48_57_range__65_90_range__97_122_ShortChar_attrs___term__cons_Regular_lex, ShortChar.prod___char_class___range__48_57_range__65_90_range__97_122_ShortChar_attrs___term__cons_Regular_lex);
                
                } else {
                  if ((lookAheadChar == 92)) {
                  // prod([\char-class([range(92,92)]),\char-class([range(0,47),range(58,64),range(91,96),range(110,110),range(114,114),range(116,116),range(123,65535)])],sort("ShortChar"),attrs([term(cons("Escaped")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__0_47_range__58_64_range__91_96_range__110_110_range__114_114_range__116_116_range__123_65535_ShortChar_attrs___term__cons_Escaped_lex, ShortChar.prod___char_class___range__92_92_char_class___range__0_47_range__58_64_range__91_96_range__110_110_range__114_114_range__116_116_range__123_65535_ShortChar_attrs___term__cons_Escaped_lex);
                
                } else {
                  if (((lookAheadChar >= 48) && (lookAheadChar <= 57)) || ((lookAheadChar >= 65) && (lookAheadChar <= 90))  || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([\char-class([range(48,57),range(65,90),range(97,122)])],sort("ShortChar"),attrs([term(cons("Regular")),lex()]))
	expect(prod___char_class___range__48_57_range__65_90_range__97_122_ShortChar_attrs___term__cons_Regular_lex, ShortChar.prod___char_class___range__48_57_range__65_90_range__97_122_ShortChar_attrs___term__cons_Regular_lex);
                
               }
                }
                }
          }
	
      public void Aliases() {
            
            if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 34)  || (lookAheadChar == 37)  || ((lookAheadChar >= 39) && (lookAheadChar <= 41))  || (lookAheadChar == 60)  || ((lookAheadChar >= 65) && (lookAheadChar <= 91))  || ((lookAheadChar >= 96) && (lookAheadChar <= 123))  || (lookAheadChar == 126) ) {
                  // prod([\iter-star-seps(sort("Alias"),[layouts("LAYOUTLIST")])],sort("Aliases"),\no-attrs())
	expect(prod___iter_star_seps__Alias__layouts_LAYOUTLIST_Aliases_no_attrs, Aliases.prod___iter_star_seps__Alias__layouts_LAYOUTLIST_Aliases_no_attrs);
                
                } else {
                  if (lookAheadChar == 0) {
                  // prod([\iter-star-seps(sort("Alias"),[layouts("LAYOUTLIST")])],sort("Aliases"),\no-attrs())
	expect(prod___iter_star_seps__Alias__layouts_LAYOUTLIST_Aliases_no_attrs, Aliases.prod___iter_star_seps__Alias__layouts_LAYOUTLIST_Aliases_no_attrs);
                
               }
                }
          }
	
      public void NumChar() {
            
            if ((lookAheadChar == 60) || (lookAheadChar == 92) ) {
                  // prod([\char-class([range(92,92)]),iter(\char-class([range(48,57)]))],sort("NumChar"),attrs([term(cons("Digits")),lex()]))
	expect(prod___char_class___range__92_92_iter__char_class___range__48_57_NumChar_attrs___term__cons_Digits_lex, NumChar.prod___char_class___range__92_92_iter__char_class___range__48_57_NumChar_attrs___term__cons_Digits_lex);
                
               }
          }
	
      public void Production() {
            
            if ((lookAheadChar == 34) || (lookAheadChar == 60)  || ((lookAheadChar >= 65) && (lookAheadChar <= 90))  || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([sort("Symbols"),layouts("LAYOUTLIST"),lit("-\>"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),sort("Attributes")],sort("Production"),attrs([term(cons("Prod"))]))
	expect(prod___Symbols_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_Prod, Production.prod___Symbols_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_Prod);
                // prod([sort("FunctionName"),layouts("LAYOUTLIST"),lit("("),layouts("LAYOUTLIST"),\iter-star-seps(sort("Symbol"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit(")"),layouts("LAYOUTLIST"),sort("Attributes")],sort("Production"),attrs([term(cons("PrefixFun"))]))
	expect(prod___FunctionName_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_PrefixFun, Production.prod___FunctionName_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_star_seps__Symbol__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_PrefixFun);
                
                } else {
                  if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 37)  || ((lookAheadChar >= 39) && (lookAheadChar <= 40))  || (lookAheadChar == 45)  || (lookAheadChar == 91)  || (lookAheadChar == 96)  || (lookAheadChar == 123)  || (lookAheadChar == 126) ) {
                  // prod([sort("Symbols"),layouts("LAYOUTLIST"),lit("-\>"),layouts("LAYOUTLIST"),sort("Symbol"),layouts("LAYOUTLIST"),sort("Attributes")],sort("Production"),attrs([term(cons("Prod"))]))
	expect(prod___Symbols_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_Prod, Production.prod___Symbols_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_layouts_LAYOUTLIST_Attributes_Production_attrs___term__cons_Prod);
                
               }
                }
          }
	
      public void RealCon() {
            
            if ((lookAheadChar == 43) || (lookAheadChar == 45)  || ((lookAheadChar >= 48) && (lookAheadChar <= 57))  || (lookAheadChar == 60) ) {
                  // prod([sort("IntCon"),layouts("LAYOUTLIST"),lit("."),layouts("LAYOUTLIST"),sort("NatCon"),layouts("LAYOUTLIST"),sort("OptExp")],sort("RealCon"),attrs([term(cons("RealCon"))]))
	expect(prod___IntCon_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_NatCon_layouts_LAYOUTLIST_OptExp_RealCon_attrs___term__cons_RealCon, RealCon.prod___IntCon_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_NatCon_layouts_LAYOUTLIST_OptExp_RealCon_attrs___term__cons_RealCon);
                
               }
          }
	
      public void CharRanges() {
            
            if (((lookAheadChar >= 48) && (lookAheadChar <= 57)) || ((lookAheadChar >= 65) && (lookAheadChar <= 90))  || (lookAheadChar == 92)  || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([sort("CharRange")],sort("CharRanges"),\no-attrs())
	expect(prod___CharRange_CharRanges_no_attrs, CharRanges.prod___CharRange_CharRanges_no_attrs);
                // prod([sort("CharRanges"),layouts("LAYOUTLIST"),sort("CharRanges")],sort("CharRanges"),attrs([term(cons("Conc")),assoc(right())]))
	expect(prod___CharRanges_layouts_LAYOUTLIST_CharRanges_CharRanges_attrs___term__cons_Conc_assoc__right, CharRanges.prod___CharRanges_layouts_LAYOUTLIST_CharRanges_CharRanges_attrs___term__cons_Conc_assoc__right);
                
                } else {
                  if ((lookAheadChar == 60)) {
                  // prod([sort("CharRange")],sort("CharRanges"),\no-attrs())
	expect(prod___CharRange_CharRanges_no_attrs, CharRanges.prod___CharRange_CharRanges_no_attrs);
                // prod([sort("CharRanges"),layouts("LAYOUTLIST"),sort("CharRanges")],sort("CharRanges"),attrs([term(cons("Conc")),assoc(right())]))
	expect(prod___CharRanges_layouts_LAYOUTLIST_CharRanges_CharRanges_attrs___term__cons_Conc_assoc__right, CharRanges.prod___CharRanges_layouts_LAYOUTLIST_CharRanges_CharRanges_attrs___term__cons_Conc_assoc__right);
                // prod([lit("("),layouts("LAYOUTLIST"),sort("CharRanges"),layouts("LAYOUTLIST"),lit(")")],sort("CharRanges"),attrs([term(cons("Bracket"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_CharRanges_layouts_LAYOUTLIST_lit___41_CharRanges_attrs___term__cons_Bracket, CharRanges.prod___lit___40_layouts_LAYOUTLIST_CharRanges_layouts_LAYOUTLIST_lit___41_CharRanges_attrs___term__cons_Bracket);
                
                } else {
                  if ((lookAheadChar == 40)) {
                  // prod([sort("CharRanges"),layouts("LAYOUTLIST"),sort("CharRanges")],sort("CharRanges"),attrs([term(cons("Conc")),assoc(right())]))
	expect(prod___CharRanges_layouts_LAYOUTLIST_CharRanges_CharRanges_attrs___term__cons_Conc_assoc__right, CharRanges.prod___CharRanges_layouts_LAYOUTLIST_CharRanges_CharRanges_attrs___term__cons_Conc_assoc__right);
                // prod([lit("("),layouts("LAYOUTLIST"),sort("CharRanges"),layouts("LAYOUTLIST"),lit(")")],sort("CharRanges"),attrs([term(cons("Bracket"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_CharRanges_layouts_LAYOUTLIST_lit___41_CharRanges_attrs___term__cons_Bracket, CharRanges.prod___lit___40_layouts_LAYOUTLIST_CharRanges_layouts_LAYOUTLIST_lit___41_CharRanges_attrs___term__cons_Bracket);
                
               }
                }
                }
          }
	
      public void StrChar() {
            
            if (((lookAheadChar >= 0) && (lookAheadChar <= 8)) || ((lookAheadChar >= 11) && (lookAheadChar <= 33))  || ((lookAheadChar >= 35) && (lookAheadChar <= 59))  || ((lookAheadChar >= 61) && (lookAheadChar <= 91))  || ((lookAheadChar >= 93) && (lookAheadChar <= 65535)) ) {
                  // prod([\char-class([range(0,8),range(11,33),range(35,91),range(93,65535)])],sort("StrChar"),attrs([term(cons("Normal")),lex()]))
	expect(prod___char_class___range__0_8_range__11_33_range__35_91_range__93_65535_StrChar_attrs___term__cons_Normal_lex, StrChar.prod___char_class___range__0_8_range__11_33_range__35_91_range__93_65535_StrChar_attrs___term__cons_Normal_lex);
                
                } else {
                  if ((lookAheadChar == 60)) {
                  // prod([\char-class([range(92,92)]),\char-class([range(48,57)]),\char-class([range(48,57)]),\char-class([range(48,57)])],sort("StrChar"),attrs([term(cons("Decimal")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_StrChar_attrs___term__cons_Decimal_lex, StrChar.prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_StrChar_attrs___term__cons_Decimal_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(110,110)])],sort("StrChar"),attrs([term(cons("NewLine")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__110_110_StrChar_attrs___term__cons_NewLine_lex, StrChar.prod___char_class___range__92_92_char_class___range__110_110_StrChar_attrs___term__cons_NewLine_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(116,116)])],sort("StrChar"),attrs([term(cons("Tab")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__116_116_StrChar_attrs___term__cons_Tab_lex, StrChar.prod___char_class___range__92_92_char_class___range__116_116_StrChar_attrs___term__cons_Tab_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(92,92)])],sort("StrChar"),attrs([term(cons("Backslash")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__92_92_StrChar_attrs___term__cons_Backslash_lex, StrChar.prod___char_class___range__92_92_char_class___range__92_92_StrChar_attrs___term__cons_Backslash_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(34,34)])],sort("StrChar"),attrs([term(cons("Quote")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__34_34_StrChar_attrs___term__cons_Quote_lex, StrChar.prod___char_class___range__92_92_char_class___range__34_34_StrChar_attrs___term__cons_Quote_lex);
                // prod([\char-class([range(0,8),range(11,33),range(35,91),range(93,65535)])],sort("StrChar"),attrs([term(cons("Normal")),lex()]))
	expect(prod___char_class___range__0_8_range__11_33_range__35_91_range__93_65535_StrChar_attrs___term__cons_Normal_lex, StrChar.prod___char_class___range__0_8_range__11_33_range__35_91_range__93_65535_StrChar_attrs___term__cons_Normal_lex);
                
                } else {
                  if ((lookAheadChar == 92)) {
                  // prod([\char-class([range(92,92)]),\char-class([range(48,57)]),\char-class([range(48,57)]),\char-class([range(48,57)])],sort("StrChar"),attrs([term(cons("Decimal")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_StrChar_attrs___term__cons_Decimal_lex, StrChar.prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_StrChar_attrs___term__cons_Decimal_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(110,110)])],sort("StrChar"),attrs([term(cons("NewLine")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__110_110_StrChar_attrs___term__cons_NewLine_lex, StrChar.prod___char_class___range__92_92_char_class___range__110_110_StrChar_attrs___term__cons_NewLine_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(116,116)])],sort("StrChar"),attrs([term(cons("Tab")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__116_116_StrChar_attrs___term__cons_Tab_lex, StrChar.prod___char_class___range__92_92_char_class___range__116_116_StrChar_attrs___term__cons_Tab_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(92,92)])],sort("StrChar"),attrs([term(cons("Backslash")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__92_92_StrChar_attrs___term__cons_Backslash_lex, StrChar.prod___char_class___range__92_92_char_class___range__92_92_StrChar_attrs___term__cons_Backslash_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(34,34)])],sort("StrChar"),attrs([term(cons("Quote")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__34_34_StrChar_attrs___term__cons_Quote_lex, StrChar.prod___char_class___range__92_92_char_class___range__34_34_StrChar_attrs___term__cons_Quote_lex);
                
               }
                }
                }
          }
	
      public void Character() {
            
            if ((lookAheadChar == 60) || (lookAheadChar == 92) ) {
                  // prod([lit("\\EOF")],sort("Character"),attrs([term(cons("eof"))]))
	expect(prod___lit___92_69_79_70_Character_attrs___term__cons_eof, Character.prod___lit___92_69_79_70_Character_attrs___term__cons_eof);
                // prod([lit("\\BOT")],sort("Character"),attrs([term(cons("bot"))]))
	expect(prod___lit___92_66_79_84_Character_attrs___term__cons_bot, Character.prod___lit___92_66_79_84_Character_attrs___term__cons_bot);
                // prod([sort("NumChar")],sort("Character"),attrs([term(cons("Numeric"))]))
	expect(prod___NumChar_Character_attrs___term__cons_Numeric, Character.prod___NumChar_Character_attrs___term__cons_Numeric);
                // prod([sort("ShortChar")],sort("Character"),attrs([term(cons("short"))]))
	expect(prod___ShortChar_Character_attrs___term__cons_short, Character.prod___ShortChar_Character_attrs___term__cons_short);
                // prod([lit("\\TOP")],sort("Character"),attrs([term(cons("top"))]))
	expect(prod___lit___92_84_79_80_Character_attrs___term__cons_top, Character.prod___lit___92_84_79_80_Character_attrs___term__cons_top);
                // prod([lit("\\LABEL_START")],sort("Character"),attrs([term(cons("label_start"))]))
	expect(prod___lit___92_76_65_66_69_76_95_83_84_65_82_84_Character_attrs___term__cons_label_start, Character.prod___lit___92_76_65_66_69_76_95_83_84_65_82_84_Character_attrs___term__cons_label_start);
                
                } else {
                  if (((lookAheadChar >= 48) && (lookAheadChar <= 57)) || ((lookAheadChar >= 65) && (lookAheadChar <= 90))  || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([sort("ShortChar")],sort("Character"),attrs([term(cons("short"))]))
	expect(prod___ShortChar_Character_attrs___term__cons_short, Character.prod___ShortChar_Character_attrs___term__cons_short);
                
               }
                }
          }
	
      public void layouts_LAYOUTLIST() {
            
            if (lookAheadChar == 0) {
                  // prod([\iter-star(sort("LAYOUT"))],layouts("LAYOUTLIST"),\no-attrs())
	expect(prod___iter_star__LAYOUT_layouts_LAYOUTLIST_no_attrs, layouts_LAYOUTLIST.prod___iter_star__LAYOUT_layouts_LAYOUTLIST_no_attrs);
                
                } else {
                  if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 34)  || (lookAheadChar == 37)  || ((lookAheadChar >= 39) && (lookAheadChar <= 58))  || ((lookAheadChar >= 60) && (lookAheadChar <= 63))  || ((lookAheadChar >= 65) && (lookAheadChar <= 93))  || ((lookAheadChar >= 95) && (lookAheadChar <= 126)) ) {
                  // prod([\iter-star(sort("LAYOUT"))],layouts("LAYOUTLIST"),\no-attrs())
	expect(prod___iter_star__LAYOUT_layouts_LAYOUTLIST_no_attrs, layouts_LAYOUTLIST.prod___iter_star__LAYOUT_layouts_LAYOUTLIST_no_attrs);
                
               }
                }
          }
	
      public void SingleQuotedStrChar() {
            
            if ((lookAheadChar == 60)) {
                  // prod([\char-class([range(92,92)]),\char-class([range(48,57)]),\char-class([range(48,57)]),\char-class([range(48,57)])],sort("SingleQuotedStrChar"),attrs([term(cons("Decimal")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_SingleQuotedStrChar_attrs___term__cons_Decimal_lex, SingleQuotedStrChar.prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_SingleQuotedStrChar_attrs___term__cons_Decimal_lex);
                // prod([\char-class([range(0,8),range(11,38),range(40,91),range(93,65535)])],sort("SingleQuotedStrChar"),attrs([term(cons("Normal")),lex()]))
	expect(prod___char_class___range__0_8_range__11_38_range__40_91_range__93_65535_SingleQuotedStrChar_attrs___term__cons_Normal_lex, SingleQuotedStrChar.prod___char_class___range__0_8_range__11_38_range__40_91_range__93_65535_SingleQuotedStrChar_attrs___term__cons_Normal_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(39,39)])],sort("SingleQuotedStrChar"),attrs([term(cons("Quote")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__39_39_SingleQuotedStrChar_attrs___term__cons_Quote_lex, SingleQuotedStrChar.prod___char_class___range__92_92_char_class___range__39_39_SingleQuotedStrChar_attrs___term__cons_Quote_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(110,110)])],sort("SingleQuotedStrChar"),attrs([term(cons("NewLine")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__110_110_SingleQuotedStrChar_attrs___term__cons_NewLine_lex, SingleQuotedStrChar.prod___char_class___range__92_92_char_class___range__110_110_SingleQuotedStrChar_attrs___term__cons_NewLine_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(92,92)])],sort("SingleQuotedStrChar"),attrs([term(cons("Backslash")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__92_92_SingleQuotedStrChar_attrs___term__cons_Backslash_lex, SingleQuotedStrChar.prod___char_class___range__92_92_char_class___range__92_92_SingleQuotedStrChar_attrs___term__cons_Backslash_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(116,116)])],sort("SingleQuotedStrChar"),attrs([term(cons("Tab")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__116_116_SingleQuotedStrChar_attrs___term__cons_Tab_lex, SingleQuotedStrChar.prod___char_class___range__92_92_char_class___range__116_116_SingleQuotedStrChar_attrs___term__cons_Tab_lex);
                
                } else {
                  if (((lookAheadChar >= 0) && (lookAheadChar <= 8)) || ((lookAheadChar >= 11) && (lookAheadChar <= 38))  || ((lookAheadChar >= 40) && (lookAheadChar <= 59))  || ((lookAheadChar >= 61) && (lookAheadChar <= 91))  || ((lookAheadChar >= 93) && (lookAheadChar <= 65535)) ) {
                  // prod([\char-class([range(0,8),range(11,38),range(40,91),range(93,65535)])],sort("SingleQuotedStrChar"),attrs([term(cons("Normal")),lex()]))
	expect(prod___char_class___range__0_8_range__11_38_range__40_91_range__93_65535_SingleQuotedStrChar_attrs___term__cons_Normal_lex, SingleQuotedStrChar.prod___char_class___range__0_8_range__11_38_range__40_91_range__93_65535_SingleQuotedStrChar_attrs___term__cons_Normal_lex);
                
                } else {
                  if ((lookAheadChar == 92)) {
                  // prod([\char-class([range(92,92)]),\char-class([range(48,57)]),\char-class([range(48,57)]),\char-class([range(48,57)])],sort("SingleQuotedStrChar"),attrs([term(cons("Decimal")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_SingleQuotedStrChar_attrs___term__cons_Decimal_lex, SingleQuotedStrChar.prod___char_class___range__92_92_char_class___range__48_57_char_class___range__48_57_char_class___range__48_57_SingleQuotedStrChar_attrs___term__cons_Decimal_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(39,39)])],sort("SingleQuotedStrChar"),attrs([term(cons("Quote")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__39_39_SingleQuotedStrChar_attrs___term__cons_Quote_lex, SingleQuotedStrChar.prod___char_class___range__92_92_char_class___range__39_39_SingleQuotedStrChar_attrs___term__cons_Quote_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(110,110)])],sort("SingleQuotedStrChar"),attrs([term(cons("NewLine")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__110_110_SingleQuotedStrChar_attrs___term__cons_NewLine_lex, SingleQuotedStrChar.prod___char_class___range__92_92_char_class___range__110_110_SingleQuotedStrChar_attrs___term__cons_NewLine_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(92,92)])],sort("SingleQuotedStrChar"),attrs([term(cons("Backslash")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__92_92_SingleQuotedStrChar_attrs___term__cons_Backslash_lex, SingleQuotedStrChar.prod___char_class___range__92_92_char_class___range__92_92_SingleQuotedStrChar_attrs___term__cons_Backslash_lex);
                // prod([\char-class([range(92,92)]),\char-class([range(116,116)])],sort("SingleQuotedStrChar"),attrs([term(cons("Tab")),lex()]))
	expect(prod___char_class___range__92_92_char_class___range__116_116_SingleQuotedStrChar_attrs___term__cons_Tab_lex, SingleQuotedStrChar.prod___char_class___range__92_92_char_class___range__116_116_SingleQuotedStrChar_attrs___term__cons_Tab_lex);
                
               }
                }
                }
          }
	
      public void SDF() {
            
            if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 37)  || (lookAheadChar == 60)  || (lookAheadChar == 100) ) {
                  // prod([sort("EMPTY"),layouts("LAYOUTLIST"),lit("definition"),layouts("LAYOUTLIST"),sort("Definition"),layouts("LAYOUTLIST"),sort("EMPTY")],sort("SDF"),attrs([term(cons("Definition"))]))
	expect(prod___EMPTY_layouts_LAYOUTLIST_lit_definition_layouts_LAYOUTLIST_Definition_layouts_LAYOUTLIST_EMPTY_SDF_attrs___term__cons_Definition, SDF.prod___EMPTY_layouts_LAYOUTLIST_lit_definition_layouts_LAYOUTLIST_Definition_layouts_LAYOUTLIST_EMPTY_SDF_attrs___term__cons_Definition);
                
               }
          }
	
      public void start__Module() {
            
            if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 37)  || (lookAheadChar == 109) ) {
                  // prod([layouts("LAYOUTLIST"),sort("Module"),layouts("LAYOUTLIST")],start(sort("Module")),\no-attrs())
	expect(prod___layouts_LAYOUTLIST_Module_layouts_LAYOUTLIST_start__Module_no_attrs, start__Module.prod___layouts_LAYOUTLIST_Module_layouts_LAYOUTLIST_start__Module_no_attrs);
                
               }
          }
	
      public void EMPTY() {
            
            if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 37)  || (lookAheadChar == 100) ) {
                  // prod([],sort("EMPTY"),\no-attrs())
	expect(prod___EMPTY_no_attrs, EMPTY.prod___EMPTY_no_attrs);
                
                } else {
                  if (lookAheadChar == 0) {
                  // prod([],sort("EMPTY"),\no-attrs())
	expect(prod___EMPTY_no_attrs, EMPTY.prod___EMPTY_no_attrs);
                
               }
                }
          }
	
      public void LAYOUT() {
            
            if ((lookAheadChar == 60)) {
                  // prod([lit("%%"),\iter-star(\char-class([range(0,9),range(11,65535)])),\char-class([range(10,10)])],sort("LAYOUT"),attrs([term(cons("Line")),lex()]))
	expect(prod___lit___37_37_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10_LAYOUT_attrs___term__cons_Line_lex, LAYOUT.prod___lit___37_37_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10_LAYOUT_attrs___term__cons_Line_lex);
                // prod([\char-class([range(9,10),range(13,13),range(32,32)])],sort("LAYOUT"),attrs([term(cons("Whitespace")),lex()]))
	expect(prod___char_class___range__9_10_range__13_13_range__32_32_LAYOUT_attrs___term__cons_Whitespace_lex, LAYOUT.prod___char_class___range__9_10_range__13_13_range__32_32_LAYOUT_attrs___term__cons_Whitespace_lex);
                // prod([lit("%"),\char-class([range(0,9),range(11,36),range(38,65535)]),lit("%")],sort("LAYOUT"),attrs([term(cons("Nested")),lex()]))
	expect(prod___lit___37_char_class___range__0_9_range__11_36_range__38_65535_lit___37_LAYOUT_attrs___term__cons_Nested_lex, LAYOUT.prod___lit___37_char_class___range__0_9_range__11_36_range__38_65535_lit___37_LAYOUT_attrs___term__cons_Nested_lex);
                
                } else {
                  if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32) ) {
                  // prod([\char-class([range(9,10),range(13,13),range(32,32)])],sort("LAYOUT"),attrs([term(cons("Whitespace")),lex()]))
	expect(prod___char_class___range__9_10_range__13_13_range__32_32_LAYOUT_attrs___term__cons_Whitespace_lex, LAYOUT.prod___char_class___range__9_10_range__13_13_range__32_32_LAYOUT_attrs___term__cons_Whitespace_lex);
                
                } else {
                  if ((lookAheadChar == 37)) {
                  // prod([lit("%%"),\iter-star(\char-class([range(0,9),range(11,65535)])),\char-class([range(10,10)])],sort("LAYOUT"),attrs([term(cons("Line")),lex()]))
	expect(prod___lit___37_37_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10_LAYOUT_attrs___term__cons_Line_lex, LAYOUT.prod___lit___37_37_iter_star__char_class___range__0_9_range__11_65535_char_class___range__10_10_LAYOUT_attrs___term__cons_Line_lex);
                // prod([lit("%"),\char-class([range(0,9),range(11,36),range(38,65535)]),lit("%")],sort("LAYOUT"),attrs([term(cons("Nested")),lex()]))
	expect(prod___lit___37_char_class___range__0_9_range__11_36_range__38_65535_lit___37_LAYOUT_attrs___term__cons_Nested_lex, LAYOUT.prod___lit___37_char_class___range__0_9_range__11_36_range__38_65535_lit___37_LAYOUT_attrs___term__cons_Nested_lex);
                
               }
                }
                }
          }
	
      public void ModuleName() {
            
            if ((lookAheadChar == 60) || (lookAheadChar == 105) ) {
                  // prod([lit("imports")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_imports_ModuleName_attrs___reject, ModuleName.prod___lit_imports_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 104) ) {
                  // prod([lit("hiddens")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_hiddens_ModuleName_attrs___reject, ModuleName.prod___lit_hiddens_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 110) ) {
                  // prod([lit("non-assoc")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_non_assoc_ModuleName_attrs___reject, ModuleName.prod___lit_non_assoc_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 108) ) {
                  // prod([lit("left")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_left_ModuleName_attrs___reject, ModuleName.prod___lit_left_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 109) ) {
                  // prod([lit("module")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_module_ModuleName_attrs___reject, ModuleName.prod___lit_module_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 115) ) {
                  // prod([lit("syntax")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_syntax_ModuleName_attrs___reject, ModuleName.prod___lit_syntax_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 98) ) {
                  // prod([lit("bracket")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_bracket_ModuleName_attrs___reject, ModuleName.prod___lit_bracket_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 100) ) {
                  // prod([lit("definition")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_definition_ModuleName_attrs___reject, ModuleName.prod___lit_definition_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 108) ) {
                  // prod([lit("lexical")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_lexical_ModuleName_attrs___reject, ModuleName.prod___lit_lexical_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 99) ) {
                  // prod([lit("context-free")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_context_free_ModuleName_attrs___reject, ModuleName.prod___lit_context_free_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 115) ) {
                  // prod([lit("sorts")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_sorts_ModuleName_attrs___reject, ModuleName.prod___lit_sorts_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 114) ) {
                  // prod([lit("right")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_right_ModuleName_attrs___reject, ModuleName.prod___lit_right_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 97) ) {
                  // prod([lit("aliases")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_aliases_ModuleName_attrs___reject, ModuleName.prod___lit_aliases_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 101) ) {
                  // prod([lit("exports")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_exports_ModuleName_attrs___reject, ModuleName.prod___lit_exports_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 114) ) {
                  // prod([lit("restrictions")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_restrictions_ModuleName_attrs___reject, ModuleName.prod___lit_restrictions_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 97) ) {
                  // prod([lit("assoc")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_assoc_ModuleName_attrs___reject, ModuleName.prod___lit_assoc_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 112) ) {
                  // prod([lit("priorities")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_priorities_ModuleName_attrs___reject, ModuleName.prod___lit_priorities_ModuleName_attrs___reject);
               }
                if ((lookAheadChar == 60) || (lookAheadChar == 118) ) {
                  // prod([lit("variables")],sort("ModuleName"),attrs([reject()]))
	expectReject(prod___lit_variables_ModuleName_attrs___reject, ModuleName.prod___lit_variables_ModuleName_attrs___reject);
               }
                
                if ((lookAheadChar == 45) || ((lookAheadChar >= 47) && (lookAheadChar <= 57))  || (lookAheadChar == 60)  || ((lookAheadChar >= 65) && (lookAheadChar <= 90))  || (lookAheadChar == 95)  || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([sort("ModuleId"),layouts("LAYOUTLIST"),lit("["),layouts("LAYOUTLIST"),sort("Symbols"),layouts("LAYOUTLIST"),lit("]")],sort("ModuleName"),attrs([term(cons("Parameterized"))]))
	expect(prod___ModuleId_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___93_ModuleName_attrs___term__cons_Parameterized, ModuleName.prod___ModuleId_layouts_LAYOUTLIST_lit___91_layouts_LAYOUTLIST_Symbols_layouts_LAYOUTLIST_lit___93_ModuleName_attrs___term__cons_Parameterized);
                // prod([sort("ModuleId")],sort("ModuleName"),attrs([term(cons("Unparameterized"))]))
	expect(prod___ModuleId_ModuleName_attrs___term__cons_Unparameterized, ModuleName.prod___ModuleId_ModuleName_attrs___term__cons_Unparameterized);
                
               }
          }
	
      public void IntCon() {
            
            if ((lookAheadChar == 60)) {
                  // prod([lit("-"),layouts("LAYOUTLIST"),sort("NatCon")],sort("IntCon"),attrs([term(cons("Negative"))]))
	expect(prod___lit___layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Negative, IntCon.prod___lit___layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Negative);
                // prod([sort("NatCon")],sort("IntCon"),attrs([term(cons("Natural"))]))
	expect(prod___NatCon_IntCon_attrs___term__cons_Natural, IntCon.prod___NatCon_IntCon_attrs___term__cons_Natural);
                // prod([lit("+"),layouts("LAYOUTLIST"),sort("NatCon")],sort("IntCon"),attrs([term(cons("Positive"))]))
	expect(prod___lit___43_layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Positive, IntCon.prod___lit___43_layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Positive);
                
                } else {
                  if ((lookAheadChar == 43)) {
                  // prod([lit("+"),layouts("LAYOUTLIST"),sort("NatCon")],sort("IntCon"),attrs([term(cons("Positive"))]))
	expect(prod___lit___43_layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Positive, IntCon.prod___lit___43_layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Positive);
                
                } else {
                  if ((lookAheadChar == 45)) {
                  // prod([lit("-"),layouts("LAYOUTLIST"),sort("NatCon")],sort("IntCon"),attrs([term(cons("Negative"))]))
	expect(prod___lit___layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Negative, IntCon.prod___lit___layouts_LAYOUTLIST_NatCon_IntCon_attrs___term__cons_Negative);
                
                } else {
                  if (((lookAheadChar >= 48) && (lookAheadChar <= 57))) {
                  // prod([sort("NatCon")],sort("IntCon"),attrs([term(cons("Natural"))]))
	expect(prod___NatCon_IntCon_attrs___term__cons_Natural, IntCon.prod___NatCon_IntCon_attrs___term__cons_Natural);
                
               }
                }
                }
                }
          }
	
      public void Alias() {
            
            if ((lookAheadChar == 34) || ((lookAheadChar >= 39) && (lookAheadChar <= 40))  || (lookAheadChar == 60)  || ((lookAheadChar >= 65) && (lookAheadChar <= 91))  || ((lookAheadChar >= 96) && (lookAheadChar <= 123))  || (lookAheadChar == 126) ) {
                  // prod([sort("Symbol"),layouts("LAYOUTLIST"),lit("-\>"),layouts("LAYOUTLIST"),sort("Symbol")],sort("Alias"),attrs([term(cons("Alias"))]))
	expect(prod___Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_Alias_attrs___term__cons_Alias, Alias.prod___Symbol_layouts_LAYOUTLIST_lit___45_62_layouts_LAYOUTLIST_Symbol_Alias_attrs___term__cons_Alias);
                
               }
          }
	
      public void Renamings() {
            
            if ((lookAheadChar == 60) || (lookAheadChar == 91) ) {
                  // prod([lit("["),layouts("LAYOUTLIST"),\iter-star-seps(sort("Renaming"),[layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit("]")],sort("Renamings"),attrs([term(cons("Renamings"))]))
	expect(prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__Renaming__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_Renamings_attrs___term__cons_Renamings, Renamings.prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__Renaming__layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_Renamings_attrs___term__cons_Renamings);
                
               }
          }
	
      public void Restrictions() {
            
            if (lookAheadChar == 0) {
                  // prod([\iter-star-seps(sort("Restriction"),[layouts("LAYOUTLIST")])],sort("Restrictions"),\no-attrs())
	expect(prod___iter_star_seps__Restriction__layouts_LAYOUTLIST_Restrictions_no_attrs, Restrictions.prod___iter_star_seps__Restriction__layouts_LAYOUTLIST_Restrictions_no_attrs);
                
                } else {
                  if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 34)  || (lookAheadChar == 37)  || ((lookAheadChar >= 39) && (lookAheadChar <= 41))  || (lookAheadChar == 45)  || (lookAheadChar == 60)  || ((lookAheadChar >= 65) && (lookAheadChar <= 91))  || ((lookAheadChar >= 96) && (lookAheadChar <= 123))  || (lookAheadChar == 126) ) {
                  // prod([\iter-star-seps(sort("Restriction"),[layouts("LAYOUTLIST")])],sort("Restrictions"),\no-attrs())
	expect(prod___iter_star_seps__Restriction__layouts_LAYOUTLIST_Restrictions_no_attrs, Restrictions.prod___iter_star_seps__Restriction__layouts_LAYOUTLIST_Restrictions_no_attrs);
                
               }
                }
          }
	
      public void IdCon() {
            
            if ((lookAheadChar == 60) || ((lookAheadChar >= 65) && (lookAheadChar <= 90))  || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([\char-class([range(65,90),range(97,122)]),\iter-star(\char-class([range(45,45),range(48,57),range(65,90),range(97,122)]))],sort("IdCon"),attrs([term(cons("Default")),lex()]))
	expect(prod___char_class___range__65_90_range__97_122_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_IdCon_attrs___term__cons_Default_lex, IdCon.prod___char_class___range__65_90_range__97_122_iter_star__char_class___range__45_45_range__48_57_range__65_90_range__97_122_IdCon_attrs___term__cons_Default_lex);
                
               }
          }
	
      public void layouts_$QUOTES() {
            
            if ((true /*every char*/)) {
                  // prod([\iter-star(\char-class([range(9,10),range(13,13),range(32,32)]))],layouts("$QUOTES"),attrs([term(lex())]))
	expect(prod___iter_star__char_class___range__9_10_range__13_13_range__32_32_layouts_$QUOTES_attrs___term__lex, layouts_$QUOTES.prod___iter_star__char_class___range__9_10_range__13_13_range__32_32_layouts_$QUOTES_attrs___term__lex);
                
               }
          }
	
      public void Imports() {
            
            if (lookAheadChar == 0) {
                  // prod([\iter-star-seps(sort("Import"),[layouts("LAYOUTLIST")])],sort("Imports"),\no-attrs())
	expect(prod___iter_star_seps__Import__layouts_LAYOUTLIST_Imports_no_attrs, Imports.prod___iter_star_seps__Import__layouts_LAYOUTLIST_Imports_no_attrs);
                
                } else {
                  if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 37)  || ((lookAheadChar >= 40) && (lookAheadChar <= 41))  || (lookAheadChar == 45)  || ((lookAheadChar >= 47) && (lookAheadChar <= 57))  || (lookAheadChar == 60)  || ((lookAheadChar >= 65) && (lookAheadChar <= 90))  || (lookAheadChar == 95)  || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([\iter-star-seps(sort("Import"),[layouts("LAYOUTLIST")])],sort("Imports"),\no-attrs())
	expect(prod___iter_star_seps__Import__layouts_LAYOUTLIST_Imports_no_attrs, Imports.prod___iter_star_seps__Import__layouts_LAYOUTLIST_Imports_no_attrs);
                
               }
                }
          }
	
      public void Sections() {
            
            if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 37)  || (lookAheadChar == 60)  || (lookAheadChar == 101)  || (lookAheadChar == 104)  || (lookAheadChar == 109) ) {
                  // prod([\iter-star-seps(sort("Section"),[layouts("LAYOUTLIST")])],sort("Sections"),\no-attrs())
	expect(prod___iter_star_seps__Section__layouts_LAYOUTLIST_Sections_no_attrs, Sections.prod___iter_star_seps__Section__layouts_LAYOUTLIST_Sections_no_attrs);
                
                } else {
                  if (lookAheadChar == 0) {
                  // prod([\iter-star-seps(sort("Section"),[layouts("LAYOUTLIST")])],sort("Sections"),\no-attrs())
	expect(prod___iter_star_seps__Section__layouts_LAYOUTLIST_Sections_no_attrs, Sections.prod___iter_star_seps__Section__layouts_LAYOUTLIST_Sections_no_attrs);
                
               }
                }
          }
	
      public void Annotation() {
            
            if ((lookAheadChar == 60) || (lookAheadChar == 123) ) {
                  // prod([lit("{"),layouts("LAYOUTLIST"),\iter-seps(sort("ATerm"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit("}")],sort("Annotation"),attrs([term(cons("Default"))]))
	expect(prod___lit___123_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Annotation_attrs___term__cons_Default, Annotation.prod___lit___123_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Annotation_attrs___term__cons_Default);
                
               }
          }
	
      public void ATerm() {
            
            if ((lookAheadChar == 43) || (lookAheadChar == 45)  || ((lookAheadChar >= 48) && (lookAheadChar <= 57)) ) {
                  // prod([sort("RealCon")],sort("ATerm"),attrs([term(cons("Real"))]))
	expect(prod___RealCon_ATerm_attrs___term__cons_Real, ATerm.prod___RealCon_ATerm_attrs___term__cons_Real);
                // prod([sort("IntCon")],sort("ATerm"),attrs([term(cons("Int"))]))
	expect(prod___IntCon_ATerm_attrs___term__cons_Int, ATerm.prod___IntCon_ATerm_attrs___term__cons_Int);
                // prod([sort("ATerm"),layouts("LAYOUTLIST"),sort("Annotation")],sort("ATerm"),attrs([term(cons("Annotated"))]))
	expect(prod___ATerm_layouts_LAYOUTLIST_Annotation_ATerm_attrs___term__cons_Annotated, ATerm.prod___ATerm_layouts_LAYOUTLIST_Annotation_ATerm_attrs___term__cons_Annotated);
                
                } else {
                  if ((lookAheadChar == 34) || ((lookAheadChar >= 65) && (lookAheadChar <= 90))  || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([sort("AFun"),layouts("LAYOUTLIST"),lit("("),layouts("LAYOUTLIST"),\iter-seps(sort("ATerm"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit(")")],sort("ATerm"),attrs([term(cons("Appl"))]))
	expect(prod___AFun_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ATerm_attrs___term__cons_Appl, ATerm.prod___AFun_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ATerm_attrs___term__cons_Appl);
                // prod([sort("AFun")],sort("ATerm"),attrs([term(cons("Fun"))]))
	expect(prod___AFun_ATerm_attrs___term__cons_Fun, ATerm.prod___AFun_ATerm_attrs___term__cons_Fun);
                // prod([sort("ATerm"),layouts("LAYOUTLIST"),sort("Annotation")],sort("ATerm"),attrs([term(cons("Annotated"))]))
	expect(prod___ATerm_layouts_LAYOUTLIST_Annotation_ATerm_attrs___term__cons_Annotated, ATerm.prod___ATerm_layouts_LAYOUTLIST_Annotation_ATerm_attrs___term__cons_Annotated);
                
                } else {
                  if ((lookAheadChar == 60)) {
                  // prod([lit("\<"),layouts("LAYOUTLIST"),sort("ATerm"),layouts("LAYOUTLIST"),lit("\>")],sort("ATerm"),attrs([term(cons("Placeholder"))]))
	expect(prod___lit___60_layouts_LAYOUTLIST_ATerm_layouts_LAYOUTLIST_lit___62_ATerm_attrs___term__cons_Placeholder, ATerm.prod___lit___60_layouts_LAYOUTLIST_ATerm_layouts_LAYOUTLIST_lit___62_ATerm_attrs___term__cons_Placeholder);
                // prod([sort("AFun"),layouts("LAYOUTLIST"),lit("("),layouts("LAYOUTLIST"),\iter-seps(sort("ATerm"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit(")")],sort("ATerm"),attrs([term(cons("Appl"))]))
	expect(prod___AFun_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ATerm_attrs___term__cons_Appl, ATerm.prod___AFun_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_iter_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___41_ATerm_attrs___term__cons_Appl);
                // prod([sort("AFun")],sort("ATerm"),attrs([term(cons("Fun"))]))
	expect(prod___AFun_ATerm_attrs___term__cons_Fun, ATerm.prod___AFun_ATerm_attrs___term__cons_Fun);
                // prod([lit("["),layouts("LAYOUTLIST"),\iter-star-seps(sort("ATerm"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit("]")],sort("ATerm"),attrs([term(cons("List"))]))
	expect(prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ATerm_attrs___term__cons_List, ATerm.prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ATerm_attrs___term__cons_List);
                // prod([sort("RealCon")],sort("ATerm"),attrs([term(cons("Real"))]))
	expect(prod___RealCon_ATerm_attrs___term__cons_Real, ATerm.prod___RealCon_ATerm_attrs___term__cons_Real);
                // prod([sort("IntCon")],sort("ATerm"),attrs([term(cons("Int"))]))
	expect(prod___IntCon_ATerm_attrs___term__cons_Int, ATerm.prod___IntCon_ATerm_attrs___term__cons_Int);
                // prod([sort("ATerm"),layouts("LAYOUTLIST"),sort("Annotation")],sort("ATerm"),attrs([term(cons("Annotated"))]))
	expect(prod___ATerm_layouts_LAYOUTLIST_Annotation_ATerm_attrs___term__cons_Annotated, ATerm.prod___ATerm_layouts_LAYOUTLIST_Annotation_ATerm_attrs___term__cons_Annotated);
                
                } else {
                  if ((lookAheadChar == 91)) {
                  // prod([lit("["),layouts("LAYOUTLIST"),\iter-star-seps(sort("ATerm"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit("]")],sort("ATerm"),attrs([term(cons("List"))]))
	expect(prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ATerm_attrs___term__cons_List, ATerm.prod___lit___91_layouts_LAYOUTLIST_iter_star_seps__ATerm__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_ATerm_attrs___term__cons_List);
                // prod([sort("ATerm"),layouts("LAYOUTLIST"),sort("Annotation")],sort("ATerm"),attrs([term(cons("Annotated"))]))
	expect(prod___ATerm_layouts_LAYOUTLIST_Annotation_ATerm_attrs___term__cons_Annotated, ATerm.prod___ATerm_layouts_LAYOUTLIST_Annotation_ATerm_attrs___term__cons_Annotated);
                
               }
                }
                }
                }
          }
	
      public void Lookaheads() {
            
            if ((lookAheadChar == 40)) {
                  // prod([sort("Lookahead")],sort("Lookaheads"),attrs([term(cons("Single"))]))
	expect(prod___Lookahead_Lookaheads_attrs___term__cons_Single, Lookaheads.prod___Lookahead_Lookaheads_attrs___term__cons_Single);
                // prod([sort("Lookaheads"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Lookaheads")],sort("Lookaheads"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Lookaheads_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Lookaheads_Lookaheads_attrs___term__cons_Alt_assoc__right, Lookaheads.prod___Lookaheads_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Lookaheads_Lookaheads_attrs___term__cons_Alt_assoc__right);
                // prod([lit("("),layouts("LAYOUTLIST"),sort("Lookaheads"),layouts("LAYOUTLIST"),lit(")")],sort("Lookaheads"),attrs([term(cons("Bracket"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_Lookaheads_layouts_LAYOUTLIST_lit___41_Lookaheads_attrs___term__cons_Bracket, Lookaheads.prod___lit___40_layouts_LAYOUTLIST_Lookaheads_layouts_LAYOUTLIST_lit___41_Lookaheads_attrs___term__cons_Bracket);
                
                } else {
                  if ((lookAheadChar == 60)) {
                  // prod([sort("Lookahead")],sort("Lookaheads"),attrs([term(cons("Single"))]))
	expect(prod___Lookahead_Lookaheads_attrs___term__cons_Single, Lookaheads.prod___Lookahead_Lookaheads_attrs___term__cons_Single);
                // prod([sort("Lookaheads"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Lookaheads")],sort("Lookaheads"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Lookaheads_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Lookaheads_Lookaheads_attrs___term__cons_Alt_assoc__right, Lookaheads.prod___Lookaheads_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Lookaheads_Lookaheads_attrs___term__cons_Alt_assoc__right);
                // prod([lit("[["),layouts("LAYOUTLIST"),\iter-star-seps(sort("Lookahead"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit("]]")],sort("Lookaheads"),attrs([term(cons("List"))]))
	expect(prod___lit___91_91_layouts_LAYOUTLIST_iter_star_seps__Lookahead__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Lookaheads_attrs___term__cons_List, Lookaheads.prod___lit___91_91_layouts_LAYOUTLIST_iter_star_seps__Lookahead__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Lookaheads_attrs___term__cons_List);
                // prod([lit("("),layouts("LAYOUTLIST"),sort("Lookaheads"),layouts("LAYOUTLIST"),lit(")")],sort("Lookaheads"),attrs([term(cons("Bracket"))]))
	expect(prod___lit___40_layouts_LAYOUTLIST_Lookaheads_layouts_LAYOUTLIST_lit___41_Lookaheads_attrs___term__cons_Bracket, Lookaheads.prod___lit___40_layouts_LAYOUTLIST_Lookaheads_layouts_LAYOUTLIST_lit___41_Lookaheads_attrs___term__cons_Bracket);
                
                } else {
                  if ((lookAheadChar == 126)) {
                  // prod([sort("Lookahead")],sort("Lookaheads"),attrs([term(cons("Single"))]))
	expect(prod___Lookahead_Lookaheads_attrs___term__cons_Single, Lookaheads.prod___Lookahead_Lookaheads_attrs___term__cons_Single);
                // prod([sort("Lookaheads"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Lookaheads")],sort("Lookaheads"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Lookaheads_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Lookaheads_Lookaheads_attrs___term__cons_Alt_assoc__right, Lookaheads.prod___Lookaheads_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Lookaheads_Lookaheads_attrs___term__cons_Alt_assoc__right);
                
                } else {
                  if ((lookAheadChar == 91)) {
                  // prod([sort("Lookahead")],sort("Lookaheads"),attrs([term(cons("Single"))]))
	expect(prod___Lookahead_Lookaheads_attrs___term__cons_Single, Lookaheads.prod___Lookahead_Lookaheads_attrs___term__cons_Single);
                // prod([sort("Lookaheads"),layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST"),sort("Lookaheads")],sort("Lookaheads"),attrs([term(cons("Alt")),assoc(right())]))
	expect(prod___Lookaheads_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Lookaheads_Lookaheads_attrs___term__cons_Alt_assoc__right, Lookaheads.prod___Lookaheads_layouts_LAYOUTLIST_lit___124_layouts_LAYOUTLIST_Lookaheads_Lookaheads_attrs___term__cons_Alt_assoc__right);
                // prod([lit("[["),layouts("LAYOUTLIST"),\iter-star-seps(sort("Lookahead"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit("]]")],sort("Lookaheads"),attrs([term(cons("List"))]))
	expect(prod___lit___91_91_layouts_LAYOUTLIST_iter_star_seps__Lookahead__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Lookaheads_attrs___term__cons_List, Lookaheads.prod___lit___91_91_layouts_LAYOUTLIST_iter_star_seps__Lookahead__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___93_93_Lookaheads_attrs___term__cons_List);
                
               }
                }
                }
                }
          }
	
      public void Group() {
            
            if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 34)  || (lookAheadChar == 37)  || ((lookAheadChar >= 39) && (lookAheadChar <= 40))  || (lookAheadChar == 45)  || ((lookAheadChar >= 65) && (lookAheadChar <= 91))  || ((lookAheadChar >= 96) && (lookAheadChar <= 122))  || (lookAheadChar == 126) ) {
                  // prod([sort("Production")],sort("Group"),attrs([term(cons("SimpleGroup"))]))
	expect(prod___Production_Group_attrs___term__cons_SimpleGroup, Group.prod___Production_Group_attrs___term__cons_SimpleGroup);
                // prod([sort("Group"),layouts("LAYOUTLIST"),lit(".")],sort("Group"),attrs([term(cons("NonTransitive")),assoc(\non-assoc())]))
	expect(prod___Group_layouts_LAYOUTLIST_lit___46_Group_attrs___term__cons_NonTransitive_assoc__non_assoc, Group.prod___Group_layouts_LAYOUTLIST_lit___46_Group_attrs___term__cons_NonTransitive_assoc__non_assoc);
                // prod([sort("Group"),layouts("LAYOUTLIST"),sort("ArgumentIndicator")],sort("Group"),attrs([term(cons("WithArguments")),assoc(\non-assoc())]))
	expect(prod___Group_layouts_LAYOUTLIST_ArgumentIndicator_Group_attrs___term__cons_WithArguments_assoc__non_assoc, Group.prod___Group_layouts_LAYOUTLIST_ArgumentIndicator_Group_attrs___term__cons_WithArguments_assoc__non_assoc);
                
                } else {
                  if ((lookAheadChar == 60) || (lookAheadChar == 123) ) {
                  // prod([sort("Production")],sort("Group"),attrs([term(cons("SimpleGroup"))]))
	expect(prod___Production_Group_attrs___term__cons_SimpleGroup, Group.prod___Production_Group_attrs___term__cons_SimpleGroup);
                // prod([lit("{"),layouts("LAYOUTLIST"),sort("Associativity"),layouts("LAYOUTLIST"),lit(":"),layouts("LAYOUTLIST"),sort("Productions"),layouts("LAYOUTLIST"),lit("}")],sort("Group"),attrs([term(cons("AssocGroup"))]))
	expect(prod___lit___123_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_AssocGroup, Group.prod___lit___123_layouts_LAYOUTLIST_Associativity_layouts_LAYOUTLIST_lit___58_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_AssocGroup);
                // prod([lit("{"),layouts("LAYOUTLIST"),sort("Productions"),layouts("LAYOUTLIST"),lit("}")],sort("Group"),attrs([term(cons("ProdsGroup"))]))
	expect(prod___lit___123_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_ProdsGroup, Group.prod___lit___123_layouts_LAYOUTLIST_Productions_layouts_LAYOUTLIST_lit___125_Group_attrs___term__cons_ProdsGroup);
                // prod([sort("Group"),layouts("LAYOUTLIST"),lit(".")],sort("Group"),attrs([term(cons("NonTransitive")),assoc(\non-assoc())]))
	expect(prod___Group_layouts_LAYOUTLIST_lit___46_Group_attrs___term__cons_NonTransitive_assoc__non_assoc, Group.prod___Group_layouts_LAYOUTLIST_lit___46_Group_attrs___term__cons_NonTransitive_assoc__non_assoc);
                // prod([sort("Group"),layouts("LAYOUTLIST"),sort("ArgumentIndicator")],sort("Group"),attrs([term(cons("WithArguments")),assoc(\non-assoc())]))
	expect(prod___Group_layouts_LAYOUTLIST_ArgumentIndicator_Group_attrs___term__cons_WithArguments_assoc__non_assoc, Group.prod___Group_layouts_LAYOUTLIST_ArgumentIndicator_Group_attrs___term__cons_WithArguments_assoc__non_assoc);
                
               }
                }
          }
	
      public void CharRange() {
            
            if (((lookAheadChar >= 48) && (lookAheadChar <= 57)) || (lookAheadChar == 60)  || ((lookAheadChar >= 65) && (lookAheadChar <= 90))  || (lookAheadChar == 92)  || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([sort("Character"),layouts("LAYOUTLIST"),lit("-"),layouts("LAYOUTLIST"),sort("Character")],sort("CharRange"),attrs([term(cons("Range"))]))
	expect(prod___Character_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_Character_CharRange_attrs___term__cons_Range, CharRange.prod___Character_layouts_LAYOUTLIST_lit___layouts_LAYOUTLIST_Character_CharRange_attrs___term__cons_Range);
                // prod([sort("Character")],sort("CharRange"),\no-attrs())
	expect(prod___Character_CharRange_no_attrs, CharRange.prod___Character_CharRange_no_attrs);
                
               }
          }
	
      public void Lookahead() {
            
            if ((lookAheadChar == 40) || (lookAheadChar == 60)  || (lookAheadChar == 91)  || (lookAheadChar == 126) ) {
                  // prod([sort("CharClass")],sort("Lookahead"),attrs([term(cons("CharClass"))]))
	expect(prod___CharClass_Lookahead_attrs___term__cons_CharClass, Lookahead.prod___CharClass_Lookahead_attrs___term__cons_CharClass);
                // prod([sort("CharClass"),layouts("LAYOUTLIST"),lit("."),layouts("LAYOUTLIST"),sort("Lookaheads")],sort("Lookahead"),attrs([term(cons("Seq"))]))
	expect(prod___CharClass_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_Lookaheads_Lookahead_attrs___term__cons_Seq, Lookahead.prod___CharClass_layouts_LAYOUTLIST_lit___46_layouts_LAYOUTLIST_Lookaheads_Lookahead_attrs___term__cons_Seq);
                
               }
          }
	
      public void Priorities() {
            
            if (lookAheadChar == 0) {
                  // prod([\iter-star-seps(sort("Priority"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")])],sort("Priorities"),\no-attrs())
	expect(prod___iter_star_seps__Priority__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_Priorities_no_attrs, Priorities.prod___iter_star_seps__Priority__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_Priorities_no_attrs);
                
                } else {
                  if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 34)  || (lookAheadChar == 37)  || ((lookAheadChar >= 39) && (lookAheadChar <= 41))  || (lookAheadChar == 45)  || (lookAheadChar == 60)  || ((lookAheadChar >= 65) && (lookAheadChar <= 91))  || ((lookAheadChar >= 96) && (lookAheadChar <= 123))  || (lookAheadChar == 126) ) {
                  // prod([\iter-star-seps(sort("Priority"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")])],sort("Priorities"),\no-attrs())
	expect(prod___iter_star_seps__Priority__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_Priorities_no_attrs, Priorities.prod___iter_star_seps__Priority__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_Priorities_no_attrs);
                
               }
                }
          }
	
      public void Attributes() {
            
            if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 34)  || (lookAheadChar == 37)  || ((lookAheadChar >= 39) && (lookAheadChar <= 41))  || ((lookAheadChar >= 44) && (lookAheadChar <= 46))  || ((lookAheadChar >= 61) && (lookAheadChar <= 62))  || ((lookAheadChar >= 65) && (lookAheadChar <= 91))  || (lookAheadChar == 93)  || ((lookAheadChar >= 96) && (lookAheadChar <= 122))  || ((lookAheadChar >= 125) && (lookAheadChar <= 126)) ) {
                  // prod([],sort("Attributes"),attrs([term(cons("NoAttrs"))]))
	expect(prod___Attributes_attrs___term__cons_NoAttrs, Attributes.prod___Attributes_attrs___term__cons_NoAttrs);
                
                } else {
                  if (lookAheadChar == 0) {
                  // prod([],sort("Attributes"),attrs([term(cons("NoAttrs"))]))
	expect(prod___Attributes_attrs___term__cons_NoAttrs, Attributes.prod___Attributes_attrs___term__cons_NoAttrs);
                
                } else {
                  if ((lookAheadChar == 60) || (lookAheadChar == 123) ) {
                  // prod([],sort("Attributes"),attrs([term(cons("NoAttrs"))]))
	expect(prod___Attributes_attrs___term__cons_NoAttrs, Attributes.prod___Attributes_attrs___term__cons_NoAttrs);
                // prod([lit("{"),layouts("LAYOUTLIST"),\iter-star-seps(sort("Attribute"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]),layouts("LAYOUTLIST"),lit("}")],sort("Attributes"),attrs([term(cons("Attrs"))]))
	expect(prod___lit___123_layouts_LAYOUTLIST_iter_star_seps__Attribute__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Attributes_attrs___term__cons_Attrs, Attributes.prod___lit___123_layouts_LAYOUTLIST_iter_star_seps__Attribute__layouts_LAYOUTLIST_lit___44_layouts_LAYOUTLIST_layouts_LAYOUTLIST_lit___125_Attributes_attrs___term__cons_Attrs);
                
               }
                }
                }
          }
	
      public void Definition() {
            
            if (lookAheadChar == 0) {
                  // prod([\iter-star-seps(sort("Module"),[layouts("LAYOUTLIST")])],sort("Definition"),\no-attrs())
	expect(prod___iter_star_seps__Module__layouts_LAYOUTLIST_Definition_no_attrs, Definition.prod___iter_star_seps__Module__layouts_LAYOUTLIST_Definition_no_attrs);
                
                } else {
                  if (((lookAheadChar >= 9) && (lookAheadChar <= 10)) || (lookAheadChar == 13)  || (lookAheadChar == 32)  || (lookAheadChar == 37)  || (lookAheadChar == 60)  || (lookAheadChar == 109) ) {
                  // prod([\iter-star-seps(sort("Module"),[layouts("LAYOUTLIST")])],sort("Definition"),\no-attrs())
	expect(prod___iter_star_seps__Module__layouts_LAYOUTLIST_Definition_no_attrs, Definition.prod___iter_star_seps__Module__layouts_LAYOUTLIST_Definition_no_attrs);
                
               }
                }
          }
	
      public void AFun() {
            
            if ((lookAheadChar == 60)) {
                  // prod([sort("StrCon")],sort("AFun"),attrs([term(cons("Quoted"))]))
	expect(prod___StrCon_AFun_attrs___term__cons_Quoted, AFun.prod___StrCon_AFun_attrs___term__cons_Quoted);
                // prod([sort("IdCon")],sort("AFun"),attrs([term(cons("Unquoted"))]))
	expect(prod___IdCon_AFun_attrs___term__cons_Unquoted, AFun.prod___IdCon_AFun_attrs___term__cons_Unquoted);
                
                } else {
                  if (((lookAheadChar >= 65) && (lookAheadChar <= 90)) || ((lookAheadChar >= 97) && (lookAheadChar <= 122)) ) {
                  // prod([sort("IdCon")],sort("AFun"),attrs([term(cons("Unquoted"))]))
	expect(prod___IdCon_AFun_attrs___term__cons_Unquoted, AFun.prod___IdCon_AFun_attrs___term__cons_Unquoted);
                
                } else {
                  if ((lookAheadChar == 34)) {
                  // prod([sort("StrCon")],sort("AFun"),attrs([term(cons("Quoted"))]))
	expect(prod___StrCon_AFun_attrs___term__cons_Quoted, AFun.prod___StrCon_AFun_attrs___term__cons_Quoted);
                
               }
                }
                }
          }
	
      public void Attribute() {
            
            if ((lookAheadChar == 60)) {
                  // prod([lit("bracket")],sort("Attribute"),attrs([term(cons("Bracket"))]))
	expect(prod___lit_bracket_Attribute_attrs___term__cons_Bracket, Attribute.prod___lit_bracket_Attribute_attrs___term__cons_Bracket);
                // prod([lit("reject")],sort("Attribute"),attrs([term(cons("Reject"))]))
	expect(prod___lit_reject_Attribute_attrs___term__cons_Reject, Attribute.prod___lit_reject_Attribute_attrs___term__cons_Reject);
                // prod([lit("id"),layouts("LAYOUTLIST"),lit("("),layouts("LAYOUTLIST"),sort("ModuleName"),layouts("LAYOUTLIST"),lit(")")],sort("Attribute"),attrs([term(cons("Id"))]))
	expect(prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_Attribute_attrs___term__cons_Id, Attribute.prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_Attribute_attrs___term__cons_Id);
                // prod([sort("Associativity")],sort("Attribute"),attrs([term(cons("Assoc"))]))
	expect(prod___Associativity_Attribute_attrs___term__cons_Assoc, Attribute.prod___Associativity_Attribute_attrs___term__cons_Assoc);
                // prod([lit("avoid")],sort("Attribute"),attrs([term(cons("Avoid"))]))
	expect(prod___lit_avoid_Attribute_attrs___term__cons_Avoid, Attribute.prod___lit_avoid_Attribute_attrs___term__cons_Avoid);
                // prod([lit("prefer")],sort("Attribute"),attrs([term(cons("Prefer"))]))
	expect(prod___lit_prefer_Attribute_attrs___term__cons_Prefer, Attribute.prod___lit_prefer_Attribute_attrs___term__cons_Prefer);
                // prod([sort("ATermAttribute")],sort("Attribute"),attrs([term(cons("Term"))]))
	expect(prod___ATermAttribute_Attribute_attrs___term__cons_Term, Attribute.prod___ATermAttribute_Attribute_attrs___term__cons_Term);
                
                } else {
                  if (((lookAheadChar >= 99) && (lookAheadChar <= 122))) {
                  // prod([lit("reject")],sort("Attribute"),attrs([term(cons("Reject"))]))
	expect(prod___lit_reject_Attribute_attrs___term__cons_Reject, Attribute.prod___lit_reject_Attribute_attrs___term__cons_Reject);
                // prod([lit("id"),layouts("LAYOUTLIST"),lit("("),layouts("LAYOUTLIST"),sort("ModuleName"),layouts("LAYOUTLIST"),lit(")")],sort("Attribute"),attrs([term(cons("Id"))]))
	expect(prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_Attribute_attrs___term__cons_Id, Attribute.prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_Attribute_attrs___term__cons_Id);
                // prod([sort("Associativity")],sort("Attribute"),attrs([term(cons("Assoc"))]))
	expect(prod___Associativity_Attribute_attrs___term__cons_Assoc, Attribute.prod___Associativity_Attribute_attrs___term__cons_Assoc);
                // prod([lit("prefer")],sort("Attribute"),attrs([term(cons("Prefer"))]))
	expect(prod___lit_prefer_Attribute_attrs___term__cons_Prefer, Attribute.prod___lit_prefer_Attribute_attrs___term__cons_Prefer);
                // prod([sort("ATermAttribute")],sort("Attribute"),attrs([term(cons("Term"))]))
	expect(prod___ATermAttribute_Attribute_attrs___term__cons_Term, Attribute.prod___ATermAttribute_Attribute_attrs___term__cons_Term);
                
                } else {
                  if ((lookAheadChar == 112)) {
                  // prod([lit("prefer")],sort("Attribute"),attrs([term(cons("Prefer"))]))
	expect(prod___lit_prefer_Attribute_attrs___term__cons_Prefer, Attribute.prod___lit_prefer_Attribute_attrs___term__cons_Prefer);
                // prod([sort("ATermAttribute")],sort("Attribute"),attrs([term(cons("Term"))]))
	expect(prod___ATermAttribute_Attribute_attrs___term__cons_Term, Attribute.prod___ATermAttribute_Attribute_attrs___term__cons_Term);
                
                } else {
                  if ((lookAheadChar == 34) || (lookAheadChar == 43)  || (lookAheadChar == 45)  || ((lookAheadChar >= 48) && (lookAheadChar <= 57))  || ((lookAheadChar >= 65) && (lookAheadChar <= 91)) ) {
                  // prod([sort("ATermAttribute")],sort("Attribute"),attrs([term(cons("Term"))]))
	expect(prod___ATermAttribute_Attribute_attrs___term__cons_Term, Attribute.prod___ATermAttribute_Attribute_attrs___term__cons_Term);
                
                } else {
                  if ((lookAheadChar == 114)) {
                  // prod([lit("reject")],sort("Attribute"),attrs([term(cons("Reject"))]))
	expect(prod___lit_reject_Attribute_attrs___term__cons_Reject, Attribute.prod___lit_reject_Attribute_attrs___term__cons_Reject);
                // prod([sort("Associativity")],sort("Attribute"),attrs([term(cons("Assoc"))]))
	expect(prod___Associativity_Attribute_attrs___term__cons_Assoc, Attribute.prod___Associativity_Attribute_attrs___term__cons_Assoc);
                // prod([sort("ATermAttribute")],sort("Attribute"),attrs([term(cons("Term"))]))
	expect(prod___ATermAttribute_Attribute_attrs___term__cons_Term, Attribute.prod___ATermAttribute_Attribute_attrs___term__cons_Term);
                
                } else {
                  if ((lookAheadChar == 105)) {
                  // prod([lit("id"),layouts("LAYOUTLIST"),lit("("),layouts("LAYOUTLIST"),sort("ModuleName"),layouts("LAYOUTLIST"),lit(")")],sort("Attribute"),attrs([term(cons("Id"))]))
	expect(prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_Attribute_attrs___term__cons_Id, Attribute.prod___lit_id_layouts_LAYOUTLIST_lit___40_layouts_LAYOUTLIST_ModuleName_layouts_LAYOUTLIST_lit___41_Attribute_attrs___term__cons_Id);
                // prod([sort("ATermAttribute")],sort("Attribute"),attrs([term(cons("Term"))]))
	expect(prod___ATermAttribute_Attribute_attrs___term__cons_Term, Attribute.prod___ATermAttribute_Attribute_attrs___term__cons_Term);
                
                } else {
                  if ((lookAheadChar == 98)) {
                  // prod([lit("bracket")],sort("Attribute"),attrs([term(cons("Bracket"))]))
	expect(prod___lit_bracket_Attribute_attrs___term__cons_Bracket, Attribute.prod___lit_bracket_Attribute_attrs___term__cons_Bracket);
                // prod([sort("ATermAttribute")],sort("Attribute"),attrs([term(cons("Term"))]))
	expect(prod___ATermAttribute_Attribute_attrs___term__cons_Term, Attribute.prod___ATermAttribute_Attribute_attrs___term__cons_Term);
                
                } else {
                  if ((lookAheadChar == 108) || (lookAheadChar == 110) ) {
                  // prod([sort("Associativity")],sort("Attribute"),attrs([term(cons("Assoc"))]))
	expect(prod___Associativity_Attribute_attrs___term__cons_Assoc, Attribute.prod___Associativity_Attribute_attrs___term__cons_Assoc);
                // prod([sort("ATermAttribute")],sort("Attribute"),attrs([term(cons("Term"))]))
	expect(prod___ATermAttribute_Attribute_attrs___term__cons_Term, Attribute.prod___ATermAttribute_Attribute_attrs___term__cons_Term);
                
                } else {
                  if ((lookAheadChar == 97)) {
                  // prod([sort("Associativity")],sort("Attribute"),attrs([term(cons("Assoc"))]))
	expect(prod___Associativity_Attribute_attrs___term__cons_Assoc, Attribute.prod___Associativity_Attribute_attrs___term__cons_Assoc);
                // prod([lit("avoid")],sort("Attribute"),attrs([term(cons("Avoid"))]))
	expect(prod___lit_avoid_Attribute_attrs___term__cons_Avoid, Attribute.prod___lit_avoid_Attribute_attrs___term__cons_Avoid);
                // prod([sort("ATermAttribute")],sort("Attribute"),attrs([term(cons("Term"))]))
	expect(prod___ATermAttribute_Attribute_attrs___term__cons_Term, Attribute.prod___ATermAttribute_Attribute_attrs___term__cons_Term);
                
               }
                }
                }
                }
                }
                }
                }
                }
                }
          }
	
      public void Associativity() {
            
            if ((lookAheadChar == 97)) {
                  // prod([lit("assoc")],sort("Associativity"),attrs([term(cons("Assoc"))]))
	expect(prod___lit_assoc_Associativity_attrs___term__cons_Assoc, Associativity.prod___lit_assoc_Associativity_attrs___term__cons_Assoc);
                
                } else {
                  if ((lookAheadChar == 110)) {
                  // prod([lit("non-assoc")],sort("Associativity"),attrs([term(cons("NonAssoc"))]))
	expect(prod___lit_non_assoc_Associativity_attrs___term__cons_NonAssoc, Associativity.prod___lit_non_assoc_Associativity_attrs___term__cons_NonAssoc);
                
                } else {
                  if ((lookAheadChar == 60)) {
                  // prod([lit("assoc")],sort("Associativity"),attrs([term(cons("Assoc"))]))
	expect(prod___lit_assoc_Associativity_attrs___term__cons_Assoc, Associativity.prod___lit_assoc_Associativity_attrs___term__cons_Assoc);
                // prod([lit("right")],sort("Associativity"),attrs([term(cons("Right"))]))
	expect(prod___lit_right_Associativity_attrs___term__cons_Right, Associativity.prod___lit_right_Associativity_attrs___term__cons_Right);
                // prod([lit("non-assoc")],sort("Associativity"),attrs([term(cons("NonAssoc"))]))
	expect(prod___lit_non_assoc_Associativity_attrs___term__cons_NonAssoc, Associativity.prod___lit_non_assoc_Associativity_attrs___term__cons_NonAssoc);
                // prod([lit("left")],sort("Associativity"),attrs([term(cons("Left"))]))
	expect(prod___lit_left_Associativity_attrs___term__cons_Left, Associativity.prod___lit_left_Associativity_attrs___term__cons_Left);
                
                } else {
                  if ((lookAheadChar == 108)) {
                  // prod([lit("left")],sort("Associativity"),attrs([term(cons("Left"))]))
	expect(prod___lit_left_Associativity_attrs___term__cons_Left, Associativity.prod___lit_left_Associativity_attrs___term__cons_Left);
                
                } else {
                  if ((lookAheadChar == 114)) {
                  // prod([lit("right")],sort("Associativity"),attrs([term(cons("Right"))]))
	expect(prod___lit_right_Associativity_attrs___term__cons_Right, Associativity.prod___lit_right_Associativity_attrs___term__cons_Right);
                
               }
                }
                }
                }
                }
          }
	
      private final static int ITERATIONS = 100;
      public static void main(String[] args) throws Exception{
    	  	File inputFile = new File("/ufs/lankamp/drowzee/SDFGrammars/Sdf2.def");
    	  	
    	  	NonTerminalStackNode START = new NonTerminalStackNode(-1, 0, "SDF");
    	  	START.setProduction(new AbstractStackNode[]{START});
    	  
    	  	int inputFileLength = (int) inputFile.length();
	  		char[] input = new char[inputFileLength];
	  		Reader in = new BufferedReader(new FileReader(inputFile));
	  		try{
	  			in.read(input, 0, inputFileLength);
	  		}finally{
	  			in.close();
	  		}
    	  
    	  for(int i = 19; i >= 0; --i){
        	  SDF2 parser = new SDF2();
	    	  parser.parse(START, null, input, new VoidActionExecutor());
    	  }
    	  
    	  ThreadMXBean tmxb = ManagementFactory.getThreadMXBean();
    	  
    	  long total = 0;
    	  long lowest = Long.MAX_VALUE;
    	  for(int i = ITERATIONS - 1; i >= 0; --i){
        	  long start = tmxb.getCurrentThreadCpuTime();
	    	  SDF2 parser = new SDF2();
	    	  parser.parse(START, null, input, new VoidActionExecutor());
	    	  long end = tmxb.getCurrentThreadCpuTime();
	    	  
	    	  long time = (end - start) / 1000000;
	    	  if(time < lowest) lowest = time;
	    	  total += time;
    	  }
    	  
    	  long average = (total / ITERATIONS);
    	  System.out.println("Average: "+average+"ms");
    	  System.out.println("Lowest: "+lowest+"ms");
    	  System.out.println((inputFileLength * 1000 / average)+" - "+(inputFileLength * 1000 / lowest)+" character/sec");
      }
}
