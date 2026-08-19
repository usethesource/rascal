package rascal.lang.rascal.grammar;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import io.usethesource.vallang.*;
import io.usethesource.vallang.type.*;
import org.rascalmpl.runtime.*;
import org.rascalmpl.runtime.RascalExecutionContext;
import org.rascalmpl.runtime.function.*;
import org.rascalmpl.runtime.traverse.*;
import org.rascalmpl.runtime.utils.*;
import org.rascalmpl.exceptions.RuntimeExceptionFactory;
import org.rascalmpl.exceptions.Throw; 
import org.rascalmpl.runtime.RascalExecutionContext;
import org.rascalmpl.interpreter.control_exceptions.Filtered;
import org.rascalmpl.types.NonTerminalType;
import org.rascalmpl.types.RascalTypeFactory;
import org.rascalmpl.util.ExpiringFunctionResultCache;
import org.rascalmpl.values.RascalValueFactory;
import org.rascalmpl.values.ValueFactoryFactory;
import org.rascalmpl.values.parsetrees.ITree;
import org.rascalmpl.values.parsetrees.TreeAdapter;



@SuppressWarnings({"unused","unchecked","deprecation"})
public class $ParserGenerator 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.lang.rascal.grammar.$ParserGenerator_$I {

    private final $ParserGenerator_$I $me;
    private final IList $constants;
    
    
    public final rascal.lang.rascal.grammar.definition.$Priorities M_lang_rascal_grammar_definition_Priorities;
    public final rascal.lang.rascal.grammar.definition.$Literals M_lang_rascal_grammar_definition_Literals;
    public final rascal.$Exception M_Exception;
    public final rascal.lang.rascal.grammar.$Lookahead M_lang_rascal_grammar_Lookahead;
    public final rascal.lang.rascal.grammar.$ConcreteSyntax M_lang_rascal_grammar_ConcreteSyntax;
    public final rascal.lang.rascal.grammar.definition.$Symbols M_lang_rascal_grammar_definition_Symbols;
    public final rascal.$List M_List;
    public final rascal.$Set M_Set;
    public final rascal.$Node M_Node;
    public final rascal.lang.rascal.syntax.$Rascal M_lang_rascal_syntax_Rascal;
    public final rascal.lang.rascal.grammar.definition.$Modules M_lang_rascal_grammar_definition_Modules;
    public final rascal.$Grammar M_Grammar;
    public final rascal.$Message M_Message;
    public final rascal.lang.rascal.grammar.definition.$Keywords M_lang_rascal_grammar_definition_Keywords;
    public final rascal.lang.rascal.grammar.definition.$Parameters M_lang_rascal_grammar_definition_Parameters;
    public final rascal.$String M_String;
    public final rascal.$ParseTree M_ParseTree;
    public final rascal.util.$Monitor M_util_Monitor;
    public final rascal.lang.rascal.grammar.definition.$Productions M_lang_rascal_grammar_definition_Productions;
    public final rascal.lang.rascal.grammar.definition.$Regular M_lang_rascal_grammar_definition_Regular;
    public final rascal.$Type M_Type;

    
    
    public IMap javaStringEscapes;
    public IMap javaIdEscapes;
    public final io.usethesource.vallang.type.Type $T13;	/*astr(alabel="new")*/
    public final io.usethesource.vallang.type.Type $T1;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T21;	/*aparameter("A",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T23;	/*avoid()*/
    public final io.usethesource.vallang.type.Type $T3;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T25;	/*aint(alabel="w")*/
    public final io.usethesource.vallang.type.Type $T17;	/*aparameter("A",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T7;	/*aint()*/
    public final io.usethesource.vallang.type.Type $T24;	/*astr(alabel="m")*/
    public final io.usethesource.vallang.type.Type $T14;	/*aint(alabel="itemId")*/
    public final io.usethesource.vallang.type.Type $T5;	/*astr()*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T19;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T10;	/*alit(",")*/
    public final io.usethesource.vallang.type.Type ADT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T15;	/*atuple(atypeList([astr(alabel="new"),aint(alabel="itemId")]),alabel="new")*/
    public final io.usethesource.vallang.type.Type $T16;	/*amap(aadt("Item",[],dataSyntax(),alabel="item"),atuple(atypeList([astr(alabel="new"),aint(alabel="itemId")]),alabel="new"))*/
    public final io.usethesource.vallang.type.Type $T12;	/*amap(aadt("Symbol",[],dataSyntax()),amap(aadt("Item",[],dataSyntax(),alabel="item"),atuple(atypeList([astr(alabel="new"),aint(alabel="itemId")]),alabel="new")))*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Production;	/*aadt("Maybe",[aadt("Production",[],dataSyntax())],dataSyntax())*/
    public final io.usethesource.vallang.type.Type Maybe_Production_just_Production;	/*acons(aadt("Maybe",[aadt("Production",[],dataSyntax())],dataSyntax()),[aadt("Production",[],dataSyntax(),alabel="val")],[],alabel="just")*/
    public final io.usethesource.vallang.type.Type ADT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Item;	/*aadt("Item",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarModule;	/*aadt("GrammarModule",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T20;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_1;	/*aadt("Maybe",[aparameter("A",avalue(),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T26;	/*afunc(avoid(),[astr(alabel="m"),aint(alabel="w")],[],alabel="worked")*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarDefinition;	/*aadt("GrammarDefinition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Field;	/*aadt("Field",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Field;	/*aadt("Field",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionBody;	/*aadt("FunctionBody",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionBody;	/*aadt("FunctionBody",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationLiteral;	/*aadt("LocationLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LocationLiteral;	/*aadt("LocationLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tag;	/*aadt("Tag",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Tag;	/*aadt("Tag",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Type;	/*aadt("Type",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Type;	/*aadt("Type",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostStringChars;	/*aadt("PostStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostStringChars;	/*aadt("PostStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T11;	/*alist(aadt("CharRange",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_HexIntegerLiteral;	/*aadt("HexIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_HexIntegerLiteral;	/*aadt("HexIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ShellCommand;	/*aadt("ShellCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ShellCommand;	/*aadt("ShellCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TimeZonePart;	/*aadt("TimeZonePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TimeZonePart;	/*aadt("TimeZonePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Declaration;	/*aadt("Declaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Declaration;	/*aadt("Declaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T4;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Associativity;	/*aadt("Associativity",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Symbol;	/*aadt("Maybe",[aadt("Symbol",[],dataSyntax())],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T28;	/*alist(avoid())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Associativity;	/*aadt("Maybe",[aadt("Associativity",[],dataSyntax(),alabel="a")],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Start;	/*aadt("Start",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Start;	/*aadt("Start",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Body;	/*aadt("Body",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Body;	/*aadt("Body",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PrePathChars;	/*aadt("PrePathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PrePathChars;	/*aadt("PrePathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringTail;	/*aadt("StringTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringTail;	/*aadt("StringTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Backslash;	/*aadt("Backslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Backslash;	/*aadt("Backslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUTLIST;	/*aadt("LAYOUTLIST",[],layoutSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T22;	/*afunc(astr(),[afunc(avoid(),[astr(alabel="m"),aint(alabel="w")],[],alabel="worked")],[],returnsViaAllPath=true)*/
    public final io.usethesource.vallang.type.Type ADT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T0;	/*aset(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T18;	/*aset(aadt("Production",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type $T27;	/*aset(aadt("Attr",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Attr;	/*aadt("Maybe",[aadt("Attr",[],dataSyntax())],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Grammar;	/*aadt("Grammar",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T9;	/*\iter-seps(aadt("Expression",[],contextFreeSyntax()),[aadt("LAYOUTLIST",[],layoutSyntax()),alit(","),aadt("LAYOUTLIST",[],layoutSyntax())])*/
    public final io.usethesource.vallang.type.Type ADT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T6;	/*alist(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_RascalKeywords;	/*aadt("RascalKeywords",[],keywordSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedBackslash;	/*aadt("NamedBackslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedBackslash;	/*aadt("NamedBackslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type Maybe_Symbol_just_Symbol;	/*acons(aadt("Maybe",[aadt("Symbol",[],dataSyntax())],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="val")],[],alabel="just")*/
    public final io.usethesource.vallang.type.Type ADT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T2;	/*aset(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_Command;	/*aadt("Command",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Command;	/*aadt("Command",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StructuredType;	/*aadt("StructuredType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StructuredType;	/*aadt("StructuredType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OctalIntegerLiteral;	/*aadt("OctalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OctalIntegerLiteral;	/*aadt("OctalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolPart;	/*aadt("ProtocolPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolPart;	/*aadt("ProtocolPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringTemplate;	/*aadt("StringTemplate",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringTemplate;	/*aadt("StringTemplate",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T8;	/*aset(aadt("Condition",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_$default$;	/*aadt("$default$",[],layoutSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Commands;	/*aadt("Commands",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Commands;	/*aadt("Commands",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visit;	/*aadt("Visit",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visit;	/*aadt("Visit",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comment;	/*aadt("Comment",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comment;	/*aadt("Comment",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/

    public $ParserGenerator(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $ParserGenerator(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($ParserGenerator_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.lang.rascal.grammar.$ParserGenerator.class, this);
       
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Priorities.class, rex, rascal.lang.rascal.grammar.definition.$Priorities::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Literals.class, rex, rascal.lang.rascal.grammar.definition.$Literals::new);
       mstore.importModule(rascal.$Exception.class, rex, rascal.$Exception::new);
       mstore.importModule(rascal.lang.rascal.grammar.$Lookahead.class, rex, rascal.lang.rascal.grammar.$Lookahead::new);
       mstore.importModule(rascal.lang.rascal.grammar.$ConcreteSyntax.class, rex, rascal.lang.rascal.grammar.$ConcreteSyntax::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Symbols.class, rex, rascal.lang.rascal.grammar.definition.$Symbols::new);
       mstore.importModule(rascal.$List.class, rex, rascal.$List::new);
       mstore.importModule(rascal.$Set.class, rex, rascal.$Set::new);
       mstore.importModule(rascal.$Node.class, rex, rascal.$Node::new);
       mstore.importModule(rascal.lang.rascal.syntax.$Rascal.class, rex, rascal.lang.rascal.syntax.$Rascal::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Modules.class, rex, rascal.lang.rascal.grammar.definition.$Modules::new);
       mstore.importModule(rascal.$Grammar.class, rex, rascal.$Grammar::new);
       mstore.importModule(rascal.$Message.class, rex, rascal.$Message::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Keywords.class, rex, rascal.lang.rascal.grammar.definition.$Keywords::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Parameters.class, rex, rascal.lang.rascal.grammar.definition.$Parameters::new);
       mstore.importModule(rascal.$String.class, rex, rascal.$String::new);
       mstore.importModule(rascal.$ParseTree.class, rex, rascal.$ParseTree::new);
       mstore.importModule(rascal.util.$Monitor.class, rex, rascal.util.$Monitor::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Productions.class, rex, rascal.lang.rascal.grammar.definition.$Productions::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Regular.class, rex, rascal.lang.rascal.grammar.definition.$Regular::new);
       mstore.importModule(rascal.$Type.class, rex, rascal.$Type::new); 
       
       M_lang_rascal_grammar_definition_Priorities = mstore.getModule(rascal.lang.rascal.grammar.definition.$Priorities.class);
       M_lang_rascal_grammar_definition_Literals = mstore.getModule(rascal.lang.rascal.grammar.definition.$Literals.class);
       M_Exception = mstore.getModule(rascal.$Exception.class);
       M_lang_rascal_grammar_Lookahead = mstore.getModule(rascal.lang.rascal.grammar.$Lookahead.class);
       M_lang_rascal_grammar_ConcreteSyntax = mstore.getModule(rascal.lang.rascal.grammar.$ConcreteSyntax.class);
       M_lang_rascal_grammar_definition_Symbols = mstore.getModule(rascal.lang.rascal.grammar.definition.$Symbols.class);
       M_List = mstore.getModule(rascal.$List.class);
       M_Set = mstore.getModule(rascal.$Set.class);
       M_Node = mstore.getModule(rascal.$Node.class);
       M_lang_rascal_syntax_Rascal = mstore.getModule(rascal.lang.rascal.syntax.$Rascal.class);
       M_lang_rascal_grammar_definition_Modules = mstore.getModule(rascal.lang.rascal.grammar.definition.$Modules.class);
       M_Grammar = mstore.getModule(rascal.$Grammar.class);
       M_Message = mstore.getModule(rascal.$Message.class);
       M_lang_rascal_grammar_definition_Keywords = mstore.getModule(rascal.lang.rascal.grammar.definition.$Keywords.class);
       M_lang_rascal_grammar_definition_Parameters = mstore.getModule(rascal.lang.rascal.grammar.definition.$Parameters.class);
       M_String = mstore.getModule(rascal.$String.class);
       M_ParseTree = mstore.getModule(rascal.$ParseTree.class);
       M_util_Monitor = mstore.getModule(rascal.util.$Monitor.class);
       M_lang_rascal_grammar_definition_Productions = mstore.getModule(rascal.lang.rascal.grammar.definition.$Productions.class);
       M_lang_rascal_grammar_definition_Regular = mstore.getModule(rascal.lang.rascal.grammar.definition.$Regular.class);
       M_Type = mstore.getModule(rascal.$Type.class); 
       
                          
       
       $TS.importStore(M_lang_rascal_grammar_definition_Priorities.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Literals.$TS);
       $TS.importStore(M_Exception.$TS);
       $TS.importStore(M_lang_rascal_grammar_Lookahead.$TS);
       $TS.importStore(M_lang_rascal_grammar_ConcreteSyntax.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Symbols.$TS);
       $TS.importStore(M_List.$TS);
       $TS.importStore(M_Set.$TS);
       $TS.importStore(M_Node.$TS);
       $TS.importStore(M_lang_rascal_syntax_Rascal.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Modules.$TS);
       $TS.importStore(M_Grammar.$TS);
       $TS.importStore(M_Message.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Keywords.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Parameters.$TS);
       $TS.importStore(M_String.$TS);
       $TS.importStore(M_ParseTree.$TS);
       $TS.importStore(M_util_Monitor.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Productions.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Regular.$TS);
       $TS.importStore(M_Type.$TS);
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/lang/rascal/grammar/$ParserGenerator.constants", 38, "6b2e4254e18eb94ae44665bdf45ca2b1");
       NT_ProtocolChars = $lex("ProtocolChars");
       ADT_ProtocolChars = $adt("ProtocolChars");
       NT_Name = $lex("Name");
       ADT_Name = $adt("Name");
       NT_Visibility = $sort("Visibility");
       ADT_Visibility = $adt("Visibility");
       NT_TagString = $lex("TagString");
       ADT_TagString = $adt("TagString");
       NT_FunctionType = $sort("FunctionType");
       ADT_FunctionType = $adt("FunctionType");
       ADT_Attr = $adt("Attr");
       ADT_Tree = $adt("Tree");
       NT_Variable = $sort("Variable");
       ADT_Variable = $adt("Variable");
       NT_Replacement = $sort("Replacement");
       ADT_Replacement = $adt("Replacement");
       ADT_Symbol = $adt("Symbol");
       NT_LAYOUT = $lex("LAYOUT");
       ADT_LAYOUT = $adt("LAYOUT");
       NT_Range = $sort("Range");
       ADT_Range = $adt("Range");
       NT_Concrete = $lex("Concrete");
       ADT_Concrete = $adt("Concrete");
       NT_Pattern = $sort("Pattern");
       ADT_Pattern = $adt("Pattern");
       NT_PostProtocolChars = $lex("PostProtocolChars");
       ADT_PostProtocolChars = $adt("PostProtocolChars");
       ADT_IOCapability = $adt("IOCapability");
       ADT_LocationChangeType = $adt("LocationChangeType");
       ADT_Production = $adt("Production");
       NT_RegExpLiteral = $lex("RegExpLiteral");
       ADT_RegExpLiteral = $adt("RegExpLiteral");
       NT_RationalLiteral = $lex("RationalLiteral");
       ADT_RationalLiteral = $adt("RationalLiteral");
       NT_Declarator = $sort("Declarator");
       ADT_Declarator = $adt("Declarator");
       NT_IntegerLiteral = $sort("IntegerLiteral");
       ADT_IntegerLiteral = $adt("IntegerLiteral");
       ADT_Item = $adt("Item");
       NT_ModuleParameters = $sort("ModuleParameters");
       ADT_ModuleParameters = $adt("ModuleParameters");
       NT_SyntaxDefinition = $sort("SyntaxDefinition");
       ADT_SyntaxDefinition = $adt("SyntaxDefinition");
       NT_Prod = $sort("Prod");
       ADT_Prod = $adt("Prod");
       NT_DatePart = $lex("DatePart");
       ADT_DatePart = $adt("DatePart");
       ADT_GrammarModule = $adt("GrammarModule");
       NT_Output = $lex("Output");
       ADT_Output = $adt("Output");
       NT_LocalVariableDeclaration = $sort("LocalVariableDeclaration");
       ADT_LocalVariableDeclaration = $adt("LocalVariableDeclaration");
       NT_Expression = $sort("Expression");
       ADT_Expression = $adt("Expression");
       NT_Case = $sort("Case");
       ADT_Case = $adt("Case");
       NT_MidStringChars = $lex("MidStringChars");
       ADT_MidStringChars = $adt("MidStringChars");
       NT_BooleanLiteral = $lex("BooleanLiteral");
       ADT_BooleanLiteral = $adt("BooleanLiteral");
       NT_TimePartNoTZ = $lex("TimePartNoTZ");
       ADT_TimePartNoTZ = $adt("TimePartNoTZ");
       NT_Target = $sort("Target");
       ADT_Target = $adt("Target");
       NT_ImportedModule = $sort("ImportedModule");
       ADT_ImportedModule = $adt("ImportedModule");
       NT_Strategy = $sort("Strategy");
       ADT_Strategy = $adt("Strategy");
       NT_KeywordFormals = $sort("KeywordFormals");
       ADT_KeywordFormals = $adt("KeywordFormals");
       NT_DataTarget = $sort("DataTarget");
       ADT_DataTarget = $adt("DataTarget");
       NT_Renaming = $sort("Renaming");
       ADT_Renaming = $adt("Renaming");
       NT_Catch = $sort("Catch");
       ADT_Catch = $adt("Catch");
       NT_OptionalExpression = $sort("OptionalExpression");
       ADT_OptionalExpression = $adt("OptionalExpression");
       ADT_GrammarDefinition = $adt("GrammarDefinition");
       NT_Field = $sort("Field");
       ADT_Field = $adt("Field");
       NT_FunctionBody = $sort("FunctionBody");
       ADT_FunctionBody = $adt("FunctionBody");
       NT_LocationLiteral = $sort("LocationLiteral");
       ADT_LocationLiteral = $adt("LocationLiteral");
       NT_Tag = $sort("Tag");
       ADT_Tag = $adt("Tag");
       NT_Type = $sort("Type");
       ADT_Type = $adt("Type");
       NT_PostStringChars = $lex("PostStringChars");
       ADT_PostStringChars = $adt("PostStringChars");
       ADT_CharRange = $adt("CharRange");
       NT_HexIntegerLiteral = $lex("HexIntegerLiteral");
       ADT_HexIntegerLiteral = $adt("HexIntegerLiteral");
       NT_ShellCommand = $sort("ShellCommand");
       ADT_ShellCommand = $adt("ShellCommand");
       NT_TimeZonePart = $lex("TimeZonePart");
       ADT_TimeZonePart = $adt("TimeZonePart");
       NT_Declaration = $sort("Declaration");
       ADT_Declaration = $adt("Declaration");
       NT_PreStringChars = $lex("PreStringChars");
       ADT_PreStringChars = $adt("PreStringChars");
       NT_Nonterminal = $lex("Nonterminal");
       ADT_Nonterminal = $adt("Nonterminal");
       NT_StringLiteral = $sort("StringLiteral");
       ADT_StringLiteral = $adt("StringLiteral");
       NT_NonterminalLabel = $lex("NonterminalLabel");
       ADT_NonterminalLabel = $adt("NonterminalLabel");
       NT_TypeArg = $sort("TypeArg");
       ADT_TypeArg = $adt("TypeArg");
       NT_Bound = $sort("Bound");
       ADT_Bound = $adt("Bound");
       NT_PathPart = $sort("PathPart");
       ADT_PathPart = $adt("PathPart");
       NT_Class = $sort("Class");
       ADT_Class = $adt("Class");
       NT_Signature = $sort("Signature");
       ADT_Signature = $adt("Signature");
       NT_KeywordFormal = $sort("KeywordFormal");
       ADT_KeywordFormal = $adt("KeywordFormal");
       ADT_Associativity = $adt("Associativity");
       NT_Renamings = $sort("Renamings");
       ADT_Renamings = $adt("Renamings");
       NT_Tags = $sort("Tags");
       ADT_Tags = $adt("Tags");
       NT_RegExp = $lex("RegExp");
       ADT_RegExp = $adt("RegExp");
       NT_ModuleActuals = $sort("ModuleActuals");
       ADT_ModuleActuals = $adt("ModuleActuals");
       NT_URLChars = $lex("URLChars");
       ADT_URLChars = $adt("URLChars");
       NT_DataTypeSelector = $sort("DataTypeSelector");
       ADT_DataTypeSelector = $adt("DataTypeSelector");
       NT_Start = $sort("Start");
       ADT_Start = $adt("Start");
       NT_Body = $sort("Body");
       ADT_Body = $adt("Body");
       NT_PrePathChars = $lex("PrePathChars");
       ADT_PrePathChars = $adt("PrePathChars");
       NT_StringTail = $sort("StringTail");
       ADT_StringTail = $adt("StringTail");
       NT_Backslash = $lex("Backslash");
       ADT_Backslash = $adt("Backslash");
       NT_Char = $lex("Char");
       ADT_Char = $adt("Char");
       ADT_LAYOUTLIST = $layouts("LAYOUTLIST");
       NT_CaseInsensitiveStringConstant = $lex("CaseInsensitiveStringConstant");
       ADT_CaseInsensitiveStringConstant = $adt("CaseInsensitiveStringConstant");
       NT_Header = $sort("Header");
       ADT_Header = $adt("Header");
       ADT_LocationType = $adt("LocationType");
       NT_Assignment = $sort("Assignment");
       ADT_Assignment = $adt("Assignment");
       ADT_Exception = $adt("Exception");
       NT_PatternWithAction = $sort("PatternWithAction");
       ADT_PatternWithAction = $adt("PatternWithAction");
       NT_StringConstant = $lex("StringConstant");
       ADT_StringConstant = $adt("StringConstant");
       NT_OptionalComma = $lex("OptionalComma");
       ADT_OptionalComma = $adt("OptionalComma");
       NT_FunctionDeclaration = $sort("FunctionDeclaration");
       ADT_FunctionDeclaration = $adt("FunctionDeclaration");
       NT_TypeVar = $sort("TypeVar");
       ADT_TypeVar = $adt("TypeVar");
       NT_Import = $sort("Import");
       ADT_Import = $adt("Import");
       NT_FunctionModifiers = $sort("FunctionModifiers");
       ADT_FunctionModifiers = $adt("FunctionModifiers");
       NT_Comprehension = $sort("Comprehension");
       ADT_Comprehension = $adt("Comprehension");
       ADT_Message = $adt("Message");
       NT_ConcreteHole = $sort("ConcreteHole");
       ADT_ConcreteHole = $adt("ConcreteHole");
       ADT_Grammar = $adt("Grammar");
       NT_UserType = $sort("UserType");
       ADT_UserType = $adt("UserType");
       NT_Variant = $sort("Variant");
       ADT_Variant = $adt("Variant");
       NT_StringMiddle = $sort("StringMiddle");
       ADT_StringMiddle = $adt("StringMiddle");
       NT_RealLiteral = $lex("RealLiteral");
       ADT_RealLiteral = $adt("RealLiteral");
       NT_DateAndTime = $lex("DateAndTime");
       ADT_DateAndTime = $adt("DateAndTime");
       NT_JustDate = $lex("JustDate");
       ADT_JustDate = $adt("JustDate");
       NT_QualifiedName = $sort("QualifiedName");
       ADT_QualifiedName = $adt("QualifiedName");
       NT_Formals = $sort("Formals");
       ADT_Formals = $adt("Formals");
       NT_MidPathChars = $lex("MidPathChars");
       ADT_MidPathChars = $adt("MidPathChars");
       NT_Sym = $sort("Sym");
       ADT_Sym = $adt("Sym");
       ADT_RascalKeywords = $keywords("RascalKeywords");
       NT_NamedBackslash = $lex("NamedBackslash");
       ADT_NamedBackslash = $adt("NamedBackslash");
       NT_MidProtocolChars = $lex("MidProtocolChars");
       ADT_MidProtocolChars = $adt("MidProtocolChars");
       NT_Parameters = $sort("Parameters");
       ADT_Parameters = $adt("Parameters");
       NT_Command = $sort("Command");
       ADT_Command = $adt("Command");
       NT_StructuredType = $sort("StructuredType");
       ADT_StructuredType = $adt("StructuredType");
       NT_OctalIntegerLiteral = $lex("OctalIntegerLiteral");
       ADT_OctalIntegerLiteral = $adt("OctalIntegerLiteral");
       NT_ProtocolPart = $sort("ProtocolPart");
       ADT_ProtocolPart = $adt("ProtocolPart");
       NT_StringTemplate = $sort("StringTemplate");
       ADT_StringTemplate = $adt("StringTemplate");
       NT_Kind = $sort("Kind");
       ADT_Kind = $adt("Kind");
       NT_DateTimeLiteral = $sort("DateTimeLiteral");
       ADT_DateTimeLiteral = $adt("DateTimeLiteral");
       NT_Label = $sort("Label");
       ADT_Label = $adt("Label");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       NT_RegExpModifier = $lex("RegExpModifier");
       ADT_RegExpModifier = $adt("RegExpModifier");
       ADT_Condition = $adt("Condition");
       NT_PreProtocolChars = $lex("PreProtocolChars");
       ADT_PreProtocolChars = $adt("PreProtocolChars");
       NT_PathTail = $sort("PathTail");
       ADT_PathTail = $adt("PathTail");
       NT_NamedRegExp = $lex("NamedRegExp");
       ADT_NamedRegExp = $adt("NamedRegExp");
       NT_Assoc = $sort("Assoc");
       ADT_Assoc = $adt("Assoc");
       ADT_RuntimeException = $adt("RuntimeException");
       NT_PathChars = $lex("PathChars");
       ADT_PathChars = $adt("PathChars");
       NT_PostPathChars = $lex("PostPathChars");
       ADT_PostPathChars = $adt("PostPathChars");
       NT_StringCharacter = $lex("StringCharacter");
       ADT_StringCharacter = $adt("StringCharacter");
       ADT_$default$ = $layouts("$default$");
       NT_EvalCommand = $sort("EvalCommand");
       ADT_EvalCommand = $adt("EvalCommand");
       NT_Statement = $sort("Statement");
       ADT_Statement = $adt("Statement");
       NT_Module = $sort("Module");
       ADT_Module = $adt("Module");
       NT_ConcretePart = $lex("ConcretePart");
       ADT_ConcretePart = $adt("ConcretePart");
       NT_Literal = $sort("Literal");
       ADT_Literal = $adt("Literal");
       NT_Toplevel = $sort("Toplevel");
       ADT_Toplevel = $adt("Toplevel");
       NT_ProtocolTail = $sort("ProtocolTail");
       ADT_ProtocolTail = $adt("ProtocolTail");
       NT_BasicType = $sort("BasicType");
       ADT_BasicType = $adt("BasicType");
       NT_Commands = $sort("Commands");
       ADT_Commands = $adt("Commands");
       NT_Visit = $sort("Visit");
       ADT_Visit = $adt("Visit");
       NT_Comment = $lex("Comment");
       ADT_Comment = $adt("Comment");
       NT_DecimalIntegerLiteral = $lex("DecimalIntegerLiteral");
       ADT_DecimalIntegerLiteral = $adt("DecimalIntegerLiteral");
       NT_JustTime = $lex("JustTime");
       ADT_JustTime = $adt("JustTime");
       NT_FunctionModifier = $sort("FunctionModifier");
       ADT_FunctionModifier = $adt("FunctionModifier");
       NT_ProdModifier = $sort("ProdModifier");
       ADT_ProdModifier = $adt("ProdModifier");
       NT_Assignable = $sort("Assignable");
       ADT_Assignable = $adt("Assignable");
       NT_UnicodeEscape = $lex("UnicodeEscape");
       ADT_UnicodeEscape = $adt("UnicodeEscape");
       NT_CommonKeywordParameters = $sort("CommonKeywordParameters");
       ADT_CommonKeywordParameters = $adt("CommonKeywordParameters");
       $T13 = $TF.stringType();
       $T1 = $TF.valueType();
       $T21 = $TF.parameterType("A", $T1);
       $T23 = $TF.voidType();
       $T3 = $TF.parameterType("T", $T1);
       $T25 = $TF.integerType();
       $T17 = $TF.parameterType("A", $T1);
       $T7 = $TF.integerType();
       $T24 = $TF.stringType();
       $T14 = $TF.integerType();
       $T5 = $TF.stringType();
       $T19 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_1 = $parameterizedSort("KeywordArguments", new Type[] { $T19 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       $T10 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_Lit, $RVF.string(",")));
       $T15 = $TF.tupleType($T13, $T14);
       $T16 = $TF.mapType(ADT_Item, "item", $T15, "new");
       $T12 = $TF.mapType(ADT_Symbol,$T16);
       NT_Mapping_Pattern = $parameterizedSort("Mapping", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       ADT_Maybe_Production = $parameterizedAdt("Maybe", new Type[] { ADT_Production });
       $T20 = $TF.parameterType("T", ADT_Tree);
       NT_Mapping_Expression = $parameterizedSort("Mapping", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       NT_Mapping_1 = $parameterizedSort("Mapping", new Type[] { $T19 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       NT_KeywordArguments_Expression = $parameterizedSort("KeywordArguments", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       ADT_Maybe_1 = $parameterizedAdt("Maybe", new Type[] { $T21 });
       $T26 = $TF.functionType($T23, $TF.tupleType($T24, "m", $T25, "w"), $TF.tupleEmpty());
       NT_KeywordArgument_1 = $parameterizedSort("KeywordArgument", new Type[] { $T19 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       $T11 = $TF.listType(ADT_CharRange);
       $T4 = $TF.listType($T3);
       NT_KeywordArguments_Pattern = $parameterizedSort("KeywordArguments", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T19 });
       ADT_Maybe_Symbol = $parameterizedAdt("Maybe", new Type[] { ADT_Symbol });
       $T28 = $TF.listType($T23);
       ADT_Maybe_Associativity = $parameterizedAdt("Maybe", new Type[] { ADT_Associativity });
       $T22 = $TF.functionType($T5, $TF.tupleType($T26, "worked"), $TF.tupleEmpty());
       $T0 = $TF.setType(ADT_Symbol);
       $T18 = $TF.setType(ADT_Production);
       $T27 = $TF.setType(ADT_Attr);
       ADT_Maybe_Attr = $parameterizedAdt("Maybe", new Type[] { ADT_Attr });
       $T9 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_IterSeps, $RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression")), $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")), $RVF.constructor(RascalValueFactory.Symbol_Lit, $RVF.string(",")), $RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")))));
       $T6 = $TF.listType(ADT_Symbol);
       $T2 = $TF.setType($T3);
       $T8 = $TF.setType(ADT_Condition);
       ADT_KeywordArguments_1 = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { $T19 });
       ADT_Mapping_Pattern = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Pattern });
       ADT_Mapping_Expression = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Expression });
       ADT_Mapping_1 = $TF.abstractDataType($TS, "Mapping", new Type[] { $T19 });
       ADT_KeywordArguments_Expression = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Expression });
       ADT_KeywordArgument_1 = $TF.abstractDataType($TS, "KeywordArgument", new Type[] { $T19 });
       ADT_KeywordArguments_Pattern = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Pattern });
       Maybe_Production_just_Production = $TF.constructor($TS, ADT_Maybe_Production, "just", M_ParseTree.ADT_Production, "val");
       Maybe_Symbol_just_Symbol = $TF.constructor($TS, ADT_Maybe_Symbol, "just", M_ParseTree.ADT_Symbol, "val");
    
       javaStringEscapes = ((IMap)$constants.get(36)/*("\"":"\\\"","\t":"\\t","\n":"\\n","\r":"\\r","\\u":"\\\\u","\\":"\\\\")*/);
       javaIdEscapes = ((IMap)$constants.get(37)/*("\"":"\\\"","\t":"\\t","\n":"\\n","\\u":"\\\\u","\\":"\\\\","_":"__","\r":"\\r","-":"_")*/);
    
       
    }
    public IString generateClassConditional(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_generateClassConditional$31ecade9ec2e3a26((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString uu(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_uu$082b41e3c596335e((IValue) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue head(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.head($P0);
    }
    public IList head(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.head($P0, $P1);
    }
    public IConstructor choice(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_Type.choice($P0, $P1);
    }
    public ISet extract(IValue $P0){ // Generated by Resolver
       return (ISet) M_lang_rascal_grammar_definition_Priorities.extract($P0);
    }
    public ISet extract(IValue $P0, IValue $P1){ // Generated by Resolver
       return (ISet) M_lang_rascal_grammar_definition_Priorities.extract($P0, $P1);
    }
    public IString getParserMethodName(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IString)lang_rascal_grammar_ParserGenerator_getParserMethodName$d1974595c71599c0((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -2144737184:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IString)lang_rascal_grammar_ParserGenerator_getParserMethodName$b82322412e38a376((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isNonTerminal($P0Type, M_lang_rascal_syntax_Rascal.NT_Sym)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_getParserMethodName$6a70b6fdc8e3b126((ITree) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_getParserMethodName$96f1d27ce96731f2((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IInteger size(IValue $P0){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T2)){
         $result = (IInteger)M_Set.Set_size$215788d71e8b2455((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T4)){
         $result = (IInteger)M_List.List_size$ba7443328d8b4a27((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T5)){
         $result = (IInteger)M_String.String_size$4611676944e933d5((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor priority(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_ParseTree.priority($P0, $P1);
    }
    public IString generateSequenceExpects(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar) && $isSubtypeOf($P1Type,$T6)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_generateSequenceExpects$6e68a18132b0b513((IConstructor) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IConstructor expandKeywords(IValue $P0){ // Generated by Resolver
       return (IConstructor) M_lang_rascal_grammar_definition_Keywords.expandKeywords($P0);
    }
    public ISet expandKeywords(IValue $P0, IValue $P1){ // Generated by Resolver
       return (ISet) M_lang_rascal_grammar_definition_Keywords.expandKeywords($P0, $P1);
    }
    public IInteger getItemId(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type,$T7) && $isSubtypeOf($P2Type, M_ParseTree.ADT_Production)){
         $result = (IInteger)lang_rascal_grammar_ParserGenerator_getItemId$47cbee5644cf9020((IConstructor) $P0, (IInteger) $P1, (IConstructor) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IString generateAltExpects(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar) && $isSubtypeOf($P1Type,$T6)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_generateAltExpects$a62b47034d27d427((IConstructor) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public INode conditional(IValue $P0, IValue $P1){ // Generated by Resolver
       INode $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type,$T8)){
         $result = (INode)M_lang_rascal_grammar_definition_Symbols.lang_rascal_grammar_definition_Symbols_conditional$f9ac60504818807f((IConstructor) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type,$T8)){
         $result = (INode)M_lang_rascal_grammar_definition_Symbols.lang_rascal_grammar_definition_Symbols_conditional$a78f69e7726562ef((IConstructor) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isNonTerminal($P0Type, M_lang_rascal_syntax_Rascal.NT_Expression) && $isSubtypeOf($P1Type,$T9)){
         return $RVF.constructor(M_lang_rascal_syntax_Rascal.Replacement_conditional_Expression_iter_seps_Expression, new IValue[]{(ITree) $P0, (ITree) $P1});
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type,$T8)){
         return $RVF.constructor(M_ParseTree.Symbol_conditional_Symbol_set_Condition, new IValue[]{(IConstructor) $P0, (ISet) $P1});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IString generateCharClassArrays(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T11)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_generateCharClassArrays$701635e3542145f3((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue unsetRec(IValue $P0){ // Generated by Resolver
       return (IValue) M_Node.unsetRec($P0);
    }
    public INode unsetRec(IValue $P0, IValue $P1){ // Generated by Resolver
       return (INode) M_Node.unsetRec($P0, $P1);
    }
    public ITuple sym2newitem(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       ITuple $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P2Type,$T7)){
         $result = (ITuple)lang_rascal_grammar_ParserGenerator_sym2newitem$10f0cc965395b37c((IConstructor) $P0, (IConstructor) $P1, (IInteger) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IConstructor lang_rascal_grammar_ParserGenerator_generateNewItems$2a1734bec0bd57ea_cl(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Production)){
         $result = (IConstructor)lang_rascal_grammar_ParserGenerator_cl$43bfc4d41d835634((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString generateSeparatorExpects(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar) && $isSubtypeOf($P1Type,$T6)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_generateSeparatorExpects$ffd9aa0c2930f956((IConstructor) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IConstructor lang_rascal_grammar_ParserGenerator_makeUnique$20b231c389f60af1_rewrite(IValue $P0, ValueRef<IInteger> uniqueItem_1){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Production)){
         $result = (IConstructor)lang_rascal_grammar_ParserGenerator_rewrite$3113ca4dfaeecfe9((IConstructor) $P0, uniqueItem_1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor lang_rascal_grammar_ParserGenerator_$CLOSURE_0_rewrite(IValue $P0, ValueRef<IInteger> uniqueItem_1){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Production)){
         $result = (IConstructor)lang_rascal_grammar_ParserGenerator_rewrite$9e6276e11df9decc((IConstructor) $P0, uniqueItem_1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString ciliterals2ints(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T6)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_ciliterals2ints$ea715e0e62cedd32((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString v2i(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_v2i$0f91624d0e98f18d((IValue) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString escId(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T5)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_escId$1ab026bcf1c9c400((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor getType(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IConstructor)lang_rascal_grammar_ParserGenerator_getType$18c51bf3658df5a8((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -2144737184:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IConstructor)lang_rascal_grammar_ParserGenerator_getType$cc1e9058e7705307((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Production)){
         $result = (IConstructor)lang_rascal_grammar_ParserGenerator_getType$37ff38bece5b0e2f((IConstructor) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IConstructor)lang_rascal_grammar_ParserGenerator_getType$38f71884bfc4046e((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList tail(IValue $P0){ // Generated by Resolver
       return (IList) M_List.tail($P0);
    }
    public IList tail(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.tail($P0, $P1);
    }
    public IString substring(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IString) M_String.substring($P0, $P1, $P2);
    }
    public IString substring(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IString) M_String.substring($P0, $P1);
    }
    public IValue index(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T4)){
         $result = (IValue)M_List.List_index$90228c781d131b76((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T2)){
         $result = (IValue)M_Set.Set_index$31fadea181d3071e((ISet) $P0);
         if($result != null) return $result;
       }
       if($isNonTerminal($P0Type, M_lang_rascal_syntax_Rascal.NT_IntegerLiteral)){
         return $RVF.constructor(M_lang_rascal_syntax_Rascal.Field_index_IntegerLiteral, new IValue[]{(ITree) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString literals2ints(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T6)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_literals2ints$e5702b42d5b21efa((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isRegular(IValue $P0){ // Generated by Resolver
       return (IBool) M_lang_rascal_grammar_definition_Regular.isRegular($P0);
    }
    public IInteger lang_rascal_grammar_ParserGenerator_$CLOSURE_0_newItem(ValueRef<IInteger> uniqueItem_1){ // Generated by Resolver
       IInteger $result = null;
       $result = (IInteger)lang_rascal_grammar_ParserGenerator_newItem$ebf5e4c7af0be0a4(uniqueItem_1);
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IInteger lang_rascal_grammar_ParserGenerator_makeUnique$20b231c389f60af1_newItem(ValueRef<IInteger> uniqueItem_1){ // Generated by Resolver
       IInteger $result = null;
       $result = (IInteger)lang_rascal_grammar_ParserGenerator_newItem$056db57e92a0e96d(uniqueItem_1);
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public ISet computeDontNests(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T12) && $isSubtypeOf($P1Type, M_Grammar.ADT_Grammar) && $isSubtypeOf($P2Type, M_Grammar.ADT_Grammar)){
         $result = (ISet)lang_rascal_grammar_ParserGenerator_computeDontNests$f6f668d3e8c3d6db((IMap) $P0, (IConstructor) $P1, (IConstructor) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IString esc(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T5)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_esc$3f747747bc8e51bf((IString) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_esc$b0384bd678427cdc((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IMap generateNewItems(IValue $P0){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (IMap)lang_rascal_grammar_ParserGenerator_generateNewItems$2a1734bec0bd57ea((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString newGenerate(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T5) && $isSubtypeOf($P1Type,$T5) && $isSubtypeOf($P2Type, M_Grammar.ADT_Grammar)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_newGenerate$119399aa64603f31((IString) $P0, (IString) $P1, (IConstructor) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IString sym2name(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_sym2name$43e2df165cbb6f65((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue makeRegularStubs(IValue $P0){ // Generated by Resolver
       return (IValue) M_lang_rascal_grammar_definition_Regular.makeRegularStubs($P0);
    }
    public ISet except(IValue $P0, IValue $P1){ // Generated by Resolver
       return (ISet) M_lang_rascal_grammar_definition_Priorities.except($P0, $P1);
    }
    public IConstructor associativity(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Maybe_1) && $isSubtypeOf($P2Type, M_ParseTree.ADT_Production)){
         $result = (IConstructor)M_lang_rascal_grammar_definition_Productions.lang_rascal_grammar_definition_Productions_associativity$09cd814bba935894((IConstructor) $P0, (IConstructor) $P1, (IConstructor) $P2);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Associativity) && $isSubtypeOf($P2Type,$T18)){
         $result = (IConstructor)M_ParseTree.ParseTree_associativity$9299e943b00366a7((IConstructor) $P0, (IConstructor) $P1, (ISet) $P2);
         if($result != null) return $result;
         $result = (IConstructor)M_ParseTree.ParseTree_associativity$95843a2f3959b22f((IConstructor) $P0, (IConstructor) $P1, (ISet) $P2);
         if($result != null) return $result;
         $result = (IConstructor)M_ParseTree.ParseTree_associativity$05ee42b13b7e96fb((IConstructor) $P0, (IConstructor) $P1, (ISet) $P2);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Maybe_Associativity) && $isSubtypeOf($P2Type, M_ParseTree.ADT_Production)){
         $result = (IConstructor)M_lang_rascal_grammar_definition_Productions.lang_rascal_grammar_definition_Productions_associativity$fe1234ba22a8be5e((IConstructor) $P0, (IConstructor) $P1, (IConstructor) $P2);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Associativity) && $isSubtypeOf($P2Type,$T18)){
         return $RVF.constructor(M_ParseTree.Production_associativity_Symbol_Associativity_set_Production, new IValue[]{(IConstructor) $P0, (IConstructor) $P1, (ISet) $P2});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IValue split(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T5)){
         $result = (IValue)lang_rascal_grammar_ParserGenerator_split$452f305967b0884c((IString) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T4)){
         $result = (IValue)M_List.List_split$19c747b75c8a251d((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList split(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_String.split($P0, $P1);
    }
    public IString value2id(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_value2id$7b72ead30df47401((IValue) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue job(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       return (IValue) M_util_Monitor.job($P0, $P1, $kwpActuals);
    }
    public IString generateRangeConditional(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_CharRange)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_generateRangeConditional$f80249e131b2a443((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor seq(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T6)){
         $result = (IConstructor)M_lang_rascal_grammar_definition_Symbols.lang_rascal_grammar_definition_Symbols_seq$5dde90ea795fac79((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T6)){
         return $RVF.constructor(M_ParseTree.Symbol_seq_list_Symbol, new IValue[]{(IList) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor makeUnique(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (IConstructor)lang_rascal_grammar_ParserGenerator_makeUnique$20b231c389f60af1((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isNonterminal(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IBool)lang_rascal_grammar_ParserGenerator_isNonterminal$ae0c546df6bca290((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString generateParseMethod(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T12) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Production)){
         $result = (IString)lang_rascal_grammar_ParserGenerator_generateParseMethod$c163c6517d408211((IMap) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(1396,79,<39,0>,<39,79>) 
    public IString lang_rascal_grammar_ParserGenerator_getParserMethodName$6a70b6fdc8e3b126(ITree sym_0){ 
        
        
        return ((IString)($me.getParserMethodName(((IConstructor)(M_lang_rascal_grammar_definition_Symbols.sym2symbol(((ITree)sym_0)))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(1476,68,<40,0>,<40,68>) 
    public IString lang_rascal_grammar_ParserGenerator_getParserMethodName$d1974595c71599c0(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_Type.Symbol_label_str_Symbol, 2)){
           IValue $arg0_1 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_1.getType(), $T1)){
              IValue $arg1_0 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_0.getType(), M_ParseTree.ADT_Symbol)){
                 IConstructor s_0 = null;
                 return ((IString)($me.getParserMethodName(((IConstructor)($arg1_0)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(1545,75,<41,0>,<41,75>) 
    public IString lang_rascal_grammar_ParserGenerator_getParserMethodName$b82322412e38a376(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
           IValue $arg0_3 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_3.getType(), M_ParseTree.ADT_Symbol)){
              IConstructor s_0 = null;
              IValue $arg1_2 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_2.getType(), $T1)){
                 return ((IString)($me.getParserMethodName(((IConstructor)($arg0_3)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(1621,56,<42,0>,<42,56>) 
    public IString lang_rascal_grammar_ParserGenerator_getParserMethodName$96f1d27ce96731f2(IConstructor s_0){ 
        
        
        return ((IString)($me.value2id(((IValue)s_0))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(1969,53,<47,4>,<47,57>) 
    public IInteger lang_rascal_grammar_ParserGenerator_newItem$ebf5e4c7af0be0a4(ValueRef<IInteger> uniqueItem_1){ 
        
        
        uniqueItem_1.setValue(((IInteger)($aint_add_aint(uniqueItem_1.getValue(),((IInteger)$constants.get(0)/*1*/)))));
        return uniqueItem_1.getValue();
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(2533,105,<65,4>,<68,8>) 
    public IConstructor lang_rascal_grammar_ParserGenerator_rewrite$9e6276e11df9decc(IConstructor p_0, ValueRef<IInteger> uniqueItem_1){ 
        
        
        try {
            IValue $visitResult = $TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptorAlwaysTrue($RVF.bool(false)),
                 p_0,
                 (IVisitFunction) (IValue $VISIT1_subject, TraversalState $traversalState) -> {
                     VISIT1:switch(Fingerprint.getFingerprint($VISIT1_subject)){
                     
                         case 0:
                             
                     
                         default: 
                             if($isSubtypeOf($VISIT1_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_0_0: 
                                    do {
                                        IConstructor s_4 = ((IConstructor)($VISIT1_subject));
                                        IConstructor $replacement15 = (IConstructor)(((IConstructor)($aadt_field_update("id", lang_rascal_grammar_ParserGenerator_$CLOSURE_0_newItem(uniqueItem_1), ((IConstructor)s_4)))));
                                        if($isSubtypeOf($replacement15.getType(),$VISIT1_subject.getType())){
                                           $traversalState.setMatchedAndChanged(true, true);
                                           return $replacement15;
                                        
                                        } else {
                                           break VISIT1;// switch
                                        
                                        }
                                    } while(false);
                             
                             }
            
                     }
                     return $VISIT1_subject;
                 });
            return (IConstructor)$visitResult;
        
        } catch (ReturnFromTraversalException e) {
            return (IConstructor) e.getValue();
        }
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(1850,9138,<45,110>,<245,5>) 
    public IString $CLOSURE_0(TypedFunctionInstance2<IValue, IValue, IValue> worked_0, ValueRef<IConstructor> gr_2, ValueRef<IString> name_1, ValueRef<IString> $package_0){ 
        
        
        final ValueRef<IInteger> uniqueItem_1 = new ValueRef<IInteger>("uniqueItem", ((IInteger)$constants.get(0)/*1*/));
        ((TypedFunctionInstance2<IValue, IValue, IValue>)worked_0).typedCall(((IString)$constants.get(1)/*"expanding parameterized symbols"*/), ((IInteger)$constants.get(0)/*1*/));
        gr_2.setValue(((IConstructor)(M_lang_rascal_grammar_definition_Parameters.expandParameterizedSymbols(gr_2.getValue()))));
        ((TypedFunctionInstance2<IValue, IValue, IValue>)worked_0).typedCall(((IString)$constants.get(2)/*"generating stubs for regular"*/), ((IInteger)$constants.get(0)/*1*/));
        gr_2.setValue(((IConstructor)(M_lang_rascal_grammar_definition_Regular.makeRegularStubs(gr_2.getValue()))));
        ((TypedFunctionInstance2<IValue, IValue, IValue>)worked_0).typedCall(((IString)$constants.get(3)/*"generating syntax for holes"*/), ((IInteger)$constants.get(0)/*1*/));
        gr_2.setValue(((IConstructor)(M_lang_rascal_grammar_ConcreteSyntax.addHoles(gr_2.getValue()))));
        ((TypedFunctionInstance2<IValue, IValue, IValue>)worked_0).typedCall(((IString)$constants.get(4)/*"generating literals"*/), ((IInteger)$constants.get(0)/*1*/));
        gr_2.setValue(((IConstructor)(M_lang_rascal_grammar_definition_Literals.literals(gr_2.getValue()))));
        ((TypedFunctionInstance2<IValue, IValue, IValue>)worked_0).typedCall(((IString)$constants.get(5)/*"establishing production set"*/), ((IInteger)$constants.get(0)/*1*/));
        final ISetWriter $setwriter6 = (ISetWriter)$RVF.setWriter();
        ;
        /*muExists*/$SCOMP7: 
            do {
                $SCOMP7_DESC2418:
                for(IValue $elem14 : new DescendantMatchIterator(gr_2.getValue(), 
                    new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                    if($isComparable($elem14.getType(), M_ParseTree.ADT_Production)){
                       if($isSubtypeOf($elem14.getType(),M_ParseTree.ADT_Production)){
                          IConstructor p_3 = null;
                          IBool $aux18 = (IBool)(((IBool)$constants.get(6)/*false*/));
                          $aux18 = ((IBool)$constants.get(6)/*false*/);
                          /*muExists*/$EXP8: 
                              do {
                                  if($has_type_and_arity($elem14, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                     IValue $arg0_11 = (IValue)($aadt_subscript_int(((IConstructor)($elem14)),0));
                                     if($isComparable($arg0_11.getType(), $T1)){
                                        IValue $arg1_10 = (IValue)($aadt_subscript_int(((IConstructor)($elem14)),1));
                                        if($isComparable($arg1_10.getType(), $T1)){
                                           IValue $arg2_9 = (IValue)($aadt_subscript_int(((IConstructor)($elem14)),2));
                                           if($isComparable($arg2_9.getType(), $T1)){
                                              $aux18 = ((IBool)$constants.get(7)/*true*/);
                                              break $EXP8; // muSucceed
                                           } else {
                                              $aux18 = ((IBool)$constants.get(6)/*false*/);
                                              continue $EXP8;
                                           }
                                        } else {
                                           $aux18 = ((IBool)$constants.get(6)/*false*/);
                                           continue $EXP8;
                                        }
                                     } else {
                                        $aux18 = ((IBool)$constants.get(6)/*false*/);
                                        continue $EXP8;
                                     }
                                  } else {
                                     $aux18 = ((IBool)$constants.get(6)/*false*/);
                                     continue $EXP8;
                                  }
                              } while(false);
                          if((((IBool)($aux18))).getValue()){
                            $setwriter6.insert($elem14);
                          
                          } else {
                            IBool $aux17 = (IBool)(((IBool)$constants.get(6)/*false*/));
                            $aux17 = ((IBool)$constants.get(6)/*false*/);
                            /*muExists*/$EXP12: 
                                do {
                                    if($has_type_and_arity($elem14, M_ParseTree.Production_regular_Symbol, 1)){
                                       IValue $arg0_13 = (IValue)($aadt_subscript_int(((IConstructor)($elem14)),0));
                                       if($isComparable($arg0_13.getType(), $T1)){
                                          $aux17 = ((IBool)$constants.get(7)/*true*/);
                                          break $EXP12; // muSucceed
                                       } else {
                                          $aux17 = ((IBool)$constants.get(6)/*false*/);
                                          continue $EXP12;
                                       }
                                    } else {
                                       $aux17 = ((IBool)$constants.get(6)/*false*/);
                                       continue $EXP12;
                                    }
                                } while(false);
                            if((((IBool)($aux17))).getValue()){
                              $setwriter6.insert($elem14);
                            
                            } else {
                              continue $SCOMP7_DESC2418;
                            }
                          
                          }
                       
                       } else {
                          continue $SCOMP7_DESC2418;
                       }
                    } else {
                       continue $SCOMP7_DESC2418;
                    }
                }
                
                             
            } while(false);
        ISet uniqueProductions_2 = ((ISet)($setwriter6.done()));
        ((TypedFunctionInstance2<IValue, IValue, IValue>)worked_0).typedCall(((IString)$constants.get(8)/*"assigning unique ids to symbols"*/), ((IInteger)$constants.get(0)/*1*/));
        IConstructor beforeUniqueGr_5 = gr_2.getValue();
        final IMapWriter $mapwriter16 = (IMapWriter)$RVF.mapWriter();
        $MCOMP17_GEN2710:
        for(IValue $elem18_for : ((IMap)(((IMap)($aadt_get_field(gr_2.getValue(), "rules")))))){
            IConstructor $elem18 = (IConstructor) $elem18_for;
            IConstructor s_6 = ((IConstructor)($elem18));
            $mapwriter16.insert($RVF.tuple(s_6, lang_rascal_grammar_ParserGenerator_$CLOSURE_0_rewrite(((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(gr_2.getValue(), "rules"))))),((IConstructor)s_6)))), uniqueItem_1)));
        
        }
        
                    gr_2.setValue(((IConstructor)(((IConstructor)($aadt_field_update("rules", $mapwriter16.done(), gr_2.getValue()))))));
        ((TypedFunctionInstance2<IValue, IValue, IValue>)worked_0).typedCall(((IString)$constants.get(9)/*"generating item allocations"*/), ((IInteger)$constants.get(0)/*1*/));
        IMap newItems_7 = ((IMap)($me.generateNewItems(gr_2.getValue())));
        ((TypedFunctionInstance2<IValue, IValue, IValue>)worked_0).typedCall(((IString)$constants.get(10)/*"computing priority and associativity filter"*/), ((IInteger)$constants.get(0)/*1*/));
        ISet dontNest_8 = ((ISet)($me.computeDontNests(((IMap)newItems_7), ((IConstructor)beforeUniqueGr_5), gr_2.getValue())));
        final ISetWriter $setwriter19 = (ISetWriter)$RVF.setWriter();
        ;
        /*muExists*/$SCOMP20: 
            do {
                final ISetWriter $setwriter22 = (ISetWriter)$RVF.setWriter();
                ;
                $SCOMP23_GEN3191:
                for(IValue $elem24_for : ((ISet)($arel_field_project((ISet)((ISet)dontNest_8), ((IInteger)$constants.get(11)/*0*/))))){
                    IInteger $elem24 = (IInteger) $elem24_for;
                    IInteger p_11 = null;
                    $setwriter22.insert($RVF.tuple(((ISet)($arel_subscript1_noset(((ISet)dontNest_8),((IInteger)($elem24))))), ((IInteger)($elem24))));
                
                }
                
                            final ISet $subject_val25 = ((ISet)($setwriter22.done()));
                ISet g_10 = null;
                $SCOMP20_GEN3214:
                for(IValue $elem21_for : ((ISet)($arel_field_project((ISet)((ISet)($subject_val25)), ((IInteger)$constants.get(11)/*0*/))))){
                    ISet $elem21 = (ISet) $elem21_for;
                    ISet c_12 = null;
                    $setwriter19.insert($RVF.tuple(((ISet)($elem21)), ((ISet)($arel2_subscript1_aset(((ISet)($subject_val25)),((ISet)($elem21)))))));
                
                }
                continue $SCOMP20;
                            
            } while(false);
        ISet dontNestGroups_9 = ((ISet)($setwriter19.done()));
        ((TypedFunctionInstance2<IValue, IValue, IValue>)worked_0).typedCall(((IString)$constants.get(12)/*"source code template"*/), ((IInteger)$constants.get(0)/*1*/));
        final Template $template26 = (Template)new Template($RVF, "package ");
        $template26.beginIndent("        ");
        $template26.addStr($package_0.getValue().getValue());
        $template26.endIndent("        ");
        $template26.addStr(";\n\nimport java.io.IOException;\nimport java.io.StringReader;\n\nimport io.usethesource.vallang.type.TypeFactory;\nimport io.usethesource.vallang.IConstructor;\nimport io.usethesource.vallang.ISourceLocation;\nimport io.usethesource.vallang.IValue;\nimport io.usethesource.vallang.IValueFactory;\nimport io.usethesource.vallang.exceptions.FactTypeUseException;\nimport io.usethesource.vallang.io.StandardTextReader;\nimport org.rascalmpl.parser.gtd.stack.*;\nimport org.rascalmpl.parser.gtd.stack.filter.*;\nimport org.rascalmpl.parser.gtd.stack.filter.follow.*;\nimport org.rascalmpl.parser.gtd.stack.filter.match.*;\nimport org.rascalmpl.parser.gtd.stack.filter.precede.*;\nimport org.rascalmpl.parser.gtd.preprocessing.ExpectBuilder;\nimport org.rascalmpl.parser.gtd.util.IntegerKeyedHashMap;\nimport org.rascalmpl.parser.gtd.util.IntegerList;\nimport org.rascalmpl.parser.gtd.util.IntegerMap;\nimport org.rascalmpl.values.ValueFactoryFactory;\nimport org.rascalmpl.values.RascalValueFactory;\nimport org.rascalmpl.values.parsetrees.ITree;\n\n@SuppressWarnings(\"all\")\npublic class ");
        $template26.beginIndent("             ");
        $template26.addStr(name_1.getValue().getValue());
        $template26.endIndent("             ");
        $template26.addStr(" extends org.rascalmpl.parser.gtd.SGTDBF<IConstructor, ITree, ISourceLocation> {\n  protected final static IValueFactory VF = ValueFactoryFactory.getValueFactory();\n\n  protected static IValue _read(java.lang.String s, io.usethesource.vallang.type.Type type) {\n    try {\n      return new StandardTextReader().read(VF, org.rascalmpl.values.RascalValueFactory.uptr, type, new StringReader(s));\n    }\n    catch (FactTypeUseException e) {\n      throw new RuntimeException(\"unexpected exception in generated parser\", e);  \n    } catch (IOException e) {\n      throw new RuntimeException(\"unexpected exception in generated parser\", e);  \n    }\n  }\n\t\n  protected static java.lang.String _concat(java.lang.String ...args) {\n    int length = 0;\n    for (java.lang.String s :args) {\n      length += s.length();\n    }\n    java.lang.StringBuilder b = new java.lang.StringBuilder(length);\n    for (java.lang.String s : args) {\n      b.append(s);\n    }\n    return b.toString();\n  }\n  protected static final TypeFactory _tf = TypeFactory.getInstance();\n \n  private static final IntegerMap _resultStoreIdMappings;\n  private static final IntegerKeyedHashMap<IntegerList> _dontNest;\n\t\n  protected static void _putDontNest(IntegerKeyedHashMap<IntegerList> result, int parentId, int childId) {\n    IntegerList donts = result.get(childId);\n    if (donts == null) {\n      donts = new IntegerList();\n      result.put(childId, donts);\n    }\n    donts.add(parentId);\n  }\n    \n  protected int getResultStoreId(int parentId) {\n    return _resultStoreIdMappings.get(parentId);\n  }\n    \n  protected static IntegerKeyedHashMap<IntegerList> _initDontNest() {\n    IntegerKeyedHashMap<IntegerList> result = new IntegerKeyedHashMap<IntegerList>(); \n    \n    ");
        IInteger i_13 = ((IInteger)$constants.get(11)/*0*/);
        $template26.addStr("\n    ");
        /*muExists*/LAB3: 
            do {
                LAB3_GEN7217:
                for(IValue $elem27_for : ((ISet)dontNest_8)){
                    IValue $elem27 = (IValue) $elem27_for;
                    final IValue $tuple_subject28 = ((IValue)($elem27));
                    if($tuple_subject28 instanceof ITuple && ((ITuple)$tuple_subject28).arity() == 2){
                       /*muExists*/LAB3_GEN7217_TUPLE: 
                           do {
                               IInteger f_14 = null;
                               IInteger c_15 = null;
                               i_13 = ((IInteger)($aint_add_aint(((IInteger)i_13),((IInteger)$constants.get(0)/*1*/))));
                               $template26.addStr("\n    ");
                               if((((IBool)($equal(((IInteger)(((IInteger)i_13).remainder(((IInteger)$constants.get(13)/*2000*/)))), ((IInteger)$constants.get(11)/*0*/))))).getValue()){
                                  $template26.addStr("\n    _initDontNest");
                                  $template26.beginIndent("                 ");
                                  $template26.addVal(i_13);
                                  $template26.endIndent("                 ");
                                  $template26.addStr("(result);\n    ");
                                  if((((IBool)($equal(((IInteger)i_13), ((IInteger)$constants.get(13)/*2000*/))))).getValue()){
                                     $template26.addStr("return result;");
                                  
                                  }
                                  $template26.addStr("\n  }\n  protected static void _initDontNest");
                                  $template26.beginIndent("                                     ");
                                  $template26.addVal(i_13);
                                  $template26.endIndent("                                     ");
                                  $template26.addStr("(IntegerKeyedHashMap<IntegerList> result) {");
                               
                               }
                               $template26.addStr("\n    _putDontNest(result, ");
                               $template26.beginIndent("                         ");
                               $template26.addVal($subscript_int(((IValue)($tuple_subject28)),0));
                               $template26.endIndent("                         ");
                               $template26.addStr(", ");
                               $template26.beginIndent("  ");
                               $template26.addVal($subscript_int(((IValue)($tuple_subject28)),1));
                               $template26.endIndent("  ");
                               $template26.addStr(");");
                       
                           } while(false);
                    
                    } else {
                       continue LAB3_GEN7217;
                    }
                }
                continue LAB3;
                            
            } while(false);
        $template26.addStr("\n   ");
        if((((IBool)($aint_less_aint(((IInteger)i_13),((IInteger)$constants.get(13)/*2000*/))))).getValue()){
           $template26.addStr("return result;");
        
        }
        $template26.addStr("\n  }\n    \n  protected static IntegerMap _initDontNestGroups() {\n    IntegerMap result = new IntegerMap();\n    int resultStoreId = result.size();\n    \n    ");
        /*muExists*/LAB7: 
            do {
                LAB7_GEN7846:
                for(IValue $elem30_for : ((ISet)dontNestGroups_9)){
                    IValue $elem30 = (IValue) $elem30_for;
                    final IValue $tuple_subject31 = ((IValue)($elem30));
                    if($tuple_subject31 instanceof ITuple && ((ITuple)$tuple_subject31).arity() == 2){
                       /*muExists*/LAB7_GEN7846_TUPLE: 
                           do {
                               ISet parentIds_16 = null;
                               $template26.addStr("\n    ++resultStoreId;\n    ");
                               /*muExists*/LAB8: 
                                   do {
                                       LAB8_GEN7940:
                                       for(IValue $elem29_for : ((ISet)($subscript_int(((IValue)($tuple_subject31)),1)))){
                                           IInteger $elem29 = (IInteger) $elem29_for;
                                           IInteger pid_17 = null;
                                           $template26.addStr("\n    result.putUnsafe(");
                                           $template26.beginIndent("                     ");
                                           $template26.addVal($elem29);
                                           $template26.endIndent("                     ");
                                           $template26.addStr(", resultStoreId);");
                                       
                                       }
                                       continue LAB8;
                                                   
                                   } while(false);
                       
                           } while(false);
                    
                    } else {
                       continue LAB7_GEN7846;
                    }
                }
                continue LAB7;
                            
            } while(false);
        $template26.addStr("\n      \n    return result;\n  }\n  \n  protected boolean hasNestingRestrictions(java.lang.String name){\n\t\treturn (_dontNest.size() != 0); // TODO Make more specific.\n  }\n    \n  protected IntegerList getFilteredParents(int childId) {\n\t\treturn _dontNest.get(childId);\n  }\n    \n  // initialize priorities     \n  static {\n    _dontNest = _initDontNest();\n    _resultStoreIdMappings = _initDontNestGroups();\n  }\n    \n  // Production declarations\n\t");
        /*muExists*/LAB9: 
            do {
                LAB9_GEN8709:
                for(IValue $elem33_for : ((ISet)uniqueProductions_2)){
                    IConstructor $elem33 = (IConstructor) $elem33_for;
                    IConstructor p_18 = ((IConstructor)($elem33));
                    $template26.addStr("\n  private static final IConstructor ");
                    $template26.beginIndent("                                    ");
                    $template26.addStr(((IString)($me.value2id(((IValue)p_18)))).getValue());
                    $template26.endIndent("                                    ");
                    $template26.addStr(" = (IConstructor) _read(\"");
                    $template26.beginIndent("                          ");
                    final Template $template32 = (Template)new Template($RVF, "");
                    $template32.addVal(p_18);
                    $template26.addStr(((IString)($me.esc(((IString)($template32.close()))))).getValue());
                    $template26.endIndent("                          ");
                    $template26.addStr("\", RascalValueFactory.Production);");
                
                }
                continue LAB9;
                            
            } while(false);
        $template26.addStr("\n    \n  // Item declarations\n\t");
        /*muExists*/LAB10: 
            do {
                LAB10_GEN8947:
                for(IValue $elem39_for : ((ISet)($amap_field_project((IMap)((IMap)newItems_7), ((IInteger)$constants.get(11)/*0*/))))){
                    IConstructor $elem39 = (IConstructor) $elem39_for;
                    if(true){
                       IConstructor s_19 = ((IConstructor)($elem39));
                       if((((IBool)($me.isNonterminal(((IConstructor)s_19))))).getValue()){
                         IMap items_20 = ((IMap)($amap_subscript(((IMap)newItems_7),((IConstructor)(M_Node.unsetRec(((IValue)s_19)))))));
                         IMap alts_21 = ((IMap)$constants.get(14)/*()*/);
                         /*muExists*/FOR11: 
                             do {
                                 FOR11_GEN9116:
                                 for(IValue $elem34_for : ((IMap)items_20)){
                                     IConstructor $elem34 = (IConstructor) $elem34_for;
                                     IConstructor item_22 = null;
                                     IConstructor prod_23 = ((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($elem34)), "production")))));
                                     if((((IBool)($RVF.bool(((IMap)alts_21).containsKey(((IConstructor)prod_23)))))).getValue()){
                                        alts_21 = ((IMap)($amap_update(prod_23,$alist_add_elm(((IList)($amap_subscript(((IMap)alts_21),((IConstructor)prod_23)))),((IConstructor)($elem34))), ((IMap)(((IMap)alts_21))))));
                                     
                                     } else {
                                        alts_21 = ((IMap)($amap_update(prod_23,$RVF.list(((IConstructor)($elem34))), ((IMap)(((IMap)alts_21))))));
                                     
                                     }
                                 }
                                 continue FOR11;
                                             
                             } while(false);
                         /* void:  muCon([]) */$template26.addStr("\n\t\n  protected static class ");
                         $template26.beginIndent("                         ");
                         $template26.addStr(((IString)($me.value2id(((IValue)s_19)))).getValue());
                         $template26.endIndent("                         ");
                         $template26.addStr(" {\n    public final static AbstractStackNode<IConstructor>[] EXPECTS;\n    static{\n      ExpectBuilder<IConstructor> builder = new ExpectBuilder<IConstructor>(_dontNest, _resultStoreIdMappings);\n      init(builder);\n      EXPECTS = builder.buildExpectArray();\n    }\n    ");
                         /*muExists*/LAB13: 
                             do {
                                 LAB13_GEN9758:
                                 for(IValue $elem37_for : ((ISet)($amap_field_project((IMap)((IMap)alts_21), ((IInteger)$constants.get(11)/*0*/))))){
                                     IConstructor $elem37 = (IConstructor) $elem37_for;
                                     if(true){
                                        IConstructor alt_24 = ((IConstructor)($elem37));
                                        IList lhses_25 = ((IList)($amap_subscript(((IMap)alts_21),((IConstructor)alt_24))));
                                        IString id_26 = ((IString)($me.value2id(((IValue)alt_24))));
                                        $template26.addStr("\n    protected static final void _init_");
                                        $template26.beginIndent("                                      ");
                                        $template26.addStr(((IString)id_26).getValue());
                                        $template26.endIndent("                                      ");
                                        $template26.addStr("(ExpectBuilder<IConstructor> builder) {\n      AbstractStackNode<IConstructor>[] tmp = (AbstractStackNode<IConstructor>[]) new AbstractStackNode[");
                                        $template26.beginIndent("                                                                                                            ");
                                        $template26.addVal(M_List.size(((IList)lhses_25)));
                                        $template26.endIndent("                                                                                                            ");
                                        $template26.addStr("];\n      ");
                                        /*muExists*/LAB14: 
                                            do {
                                                LAB14_GEN10099:
                                                for(IValue $elem36_for : ((IList)lhses_25)){
                                                    IConstructor $elem36 = (IConstructor) $elem36_for;
                                                    IConstructor i_27 = ((IConstructor)($elem36));
                                                    IInteger ii_28 = null;
                                                    if((((IBool)($equal(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)i_27), "index"))))),((IInteger)$constants.get(15)/*-1*/)).not()))).getValue()){
                                                       ii_28 = ((IInteger)(((IInteger)($aadt_get_field(((IConstructor)i_27), "index")))));
                                                    
                                                    } else {
                                                       ii_28 = ((IInteger)$constants.get(11)/*0*/);
                                                    
                                                    }$template26.addStr("\n      tmp[");
                                                    $template26.beginIndent("          ");
                                                    $template26.addVal(ii_28);
                                                    $template26.endIndent("          ");
                                                    $template26.addStr("] = ");
                                                    $template26.beginIndent("    ");
                                                    $template26.addStr(((IString)($atuple_field_project((ITuple)((ITuple)($amap_subscript(((IMap)items_20),((IConstructor)(M_Node.unsetRec(((IValue)i_27))))))), ((IInteger)$constants.get(11)/*0*/)))).getValue());
                                                    $template26.endIndent("    ");
                                                    $template26.addStr(";");
                                                
                                                }
                                                continue LAB14;
                                                            
                                            } while(false);
                                        $template26.addStr("\n      builder.addAlternative(");
                                        $template26.beginIndent("                             ");
                                        $template26.addStr(name_1.getValue().getValue());
                                        $template26.endIndent("                             ");
                                        $template26.addStr(".");
                                        $template26.beginIndent(" ");
                                        $template26.addStr(((IString)id_26).getValue());
                                        $template26.endIndent(" ");
                                        $template26.addStr(", tmp);\n\t}");
                                     
                                     } else {
                                        continue LAB13_GEN9758;
                                     }
                                 }
                                 continue LAB13;
                                             
                             } while(false);
                         $template26.addStr("\n    public static void init(ExpectBuilder<IConstructor> builder){\n      ");
                         /*muExists*/LAB15: 
                             do {
                                 LAB15_GEN10395:
                                 for(IValue $elem38_for : ((ISet)($amap_field_project((IMap)((IMap)alts_21), ((IInteger)$constants.get(11)/*0*/))))){
                                     IConstructor $elem38 = (IConstructor) $elem38_for;
                                     IConstructor alt_29 = ((IConstructor)($elem38));
                                     IList lhses_30 = ((IList)($amap_subscript(((IMap)alts_21),((IConstructor)alt_29))));
                                     IString id_31 = ((IString)($me.value2id(((IValue)alt_29))));
                                     $template26.addStr("\n        _init_");
                                     $template26.beginIndent("              ");
                                     $template26.addStr(((IString)id_31).getValue());
                                     $template26.endIndent("              ");
                                     $template26.addStr("(builder);\n      ");
                                 
                                 }
                                 continue LAB15;
                                             
                             } while(false);
                         $template26.addStr("\n    }\n  }");
                       
                       } else {
                         continue LAB10_GEN8947;
                       }
                    
                    } else {
                       continue LAB10_GEN8947;
                    }
                }
                continue LAB10;
                            
            } while(false);
        $template26.addStr("\n\n  private int nextFreeStackNodeId = ");
        $template26.beginIndent("                                    ");
        $template26.addVal(lang_rascal_grammar_ParserGenerator_$CLOSURE_0_newItem(uniqueItem_1));
        $template26.endIndent("                                    ");
        $template26.addStr(";\n  protected int getFreeStackNodeId() {\n    return nextFreeStackNodeId++;\n  }\n\n  // Parse methods    \n  ");
        /*muExists*/LAB16: 
            do {
                LAB16_GEN10835:
                for(IValue $elem40_for : ((ISet)($amap_field_project((IMap)((IMap)(((IMap)($aadt_get_field(gr_2.getValue(), "rules"))))), ((IInteger)$constants.get(11)/*0*/))))){
                    IConstructor $elem40 = (IConstructor) $elem40_for;
                    if(true){
                       IConstructor nont_32 = ((IConstructor)($elem40));
                       if((((IBool)($me.isNonterminal(((IConstructor)nont_32))))).getValue()){
                         $template26.addStr("\n  ");
                         $template26.beginIndent("  ");
                         $template26.addStr(((IString)($me.generateParseMethod(((IMap)newItems_7), ((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(gr_2.getValue(), "rules"))))),((IConstructor)(M_Node.unsetRec(((IValue)nont_32)))))))))).getValue());
                         $template26.endIndent("  ");
                       
                       } else {
                         continue LAB16_GEN10835;
                       }
                    
                    } else {
                       continue LAB16_GEN10835;
                    }
                }
                continue LAB16;
                            
            } while(false);
        $template26.addStr("\n}");
        return ((IString)($template26.close()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(1679,9332,<44,0>,<246,1>) 
    public IString lang_rascal_grammar_ParserGenerator_newGenerate$119399aa64603f31(IString $aux_$package_0, IString $aux_name_1, IConstructor $aux_gr_2){ 
        ValueRef<IString> $package_0 = new ValueRef<IString>("$package_0", $aux_$package_0);
        ValueRef<IString> name_1 = new ValueRef<IString>("name_1", $aux_name_1);
        ValueRef<IConstructor> gr_2 = new ValueRef<IConstructor>("gr_2", $aux_gr_2);
    
        
        final Template $template4 = (Template)new Template($RVF, "Generating parser; ");
        /*muExists*/LAB0: 
            do {
                LAB0_GEN1781:
                for(IValue $elem5_for : ((IMap)(((IMap)($aadt_get_field(gr_2.getValue(), "rules")))))){
                    IConstructor $elem5 = (IConstructor) $elem5_for;
                    IConstructor st_3 = null;
                    if($is(((IConstructor)($elem5)),((IString)$constants.get(16)/*"sort"*/))){
                      ;$template4.addVal($reifiedAType((IConstructor) $elem5, ((IMap)$constants.get(14)/*()*/)));
                      $template4.addStr(" ");
                    
                    } else {
                      if($is(((IConstructor)($elem5)),((IString)$constants.get(17)/*"lex"*/))){
                        ;$template4.addVal($reifiedAType((IConstructor) $elem5, ((IMap)$constants.get(14)/*()*/)));
                        $template4.addStr(" ");
                      
                      } else {
                        continue LAB0_GEN1781;
                      }
                    
                    }
                
                }
                continue LAB0;
                            
            } while(false);
        return ((IString)(M_util_Monitor.job(((IString)($astr_slice(((IString)($template4.close())), null, null, -1))), new TypedFunctionInstance1<IValue,IValue>(($1850_0) -> { return $CLOSURE_0((TypedFunctionInstance2<IValue, IValue, IValue>)$1850_0, gr_2, name_1, $package_0); }, $T22), Util.kwpMap("totalWork", ((IInteger)$constants.get(18)/*9*/)))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(11015,1011,<248,0>,<261,1>) 
    public ISet lang_rascal_grammar_ParserGenerator_computeDontNests$f6f668d3e8c3d6db(IMap items_0, IConstructor grammar_1, IConstructor uniqueGrammar_2){ 
        
        
        final IMapWriter $mapwriter41 = (IMapWriter)$RVF.mapWriter();
        /*muExists*/$MCOMP42: 
            do {
                $MCOMP42_DESC11265:
                for(IValue $elem43 : new DescendantMatchIterator(grammar_1, 
                    new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                    if($isComparable($elem43.getType(), M_ParseTree.ADT_Production)){
                       if($isSubtypeOf($elem43.getType(),M_ParseTree.ADT_Production)){
                          if($has_type_and_arity($elem43, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                             IValue $arg0_46 = (IValue)($subscript_int(((IValue)($elem43)),0));
                             if($isComparable($arg0_46.getType(), M_ParseTree.ADT_Symbol)){
                                if(true){
                                   IConstructor rhs_5 = ((IConstructor)($arg0_46));
                                   IValue $arg1_45 = (IValue)($subscript_int(((IValue)($elem43)),1));
                                   if($isComparable($arg1_45.getType(), $T6)){
                                      if(true){
                                         IList lhs_6 = ((IList)($arg1_45));
                                         IValue $arg2_44 = (IValue)($subscript_int(((IValue)($elem43)),2));
                                         if($isComparable($arg2_44.getType(), $T1)){
                                            IConstructor p_4 = ((IConstructor)($elem43));
                                            $mapwriter41.insert($RVF.tuple(p_4, $atuple_field_project((ITuple)((ITuple)($amap_subscript(((IMap)($amap_subscript(((IMap)items_0),((IConstructor)($me.getType(((IConstructor)($arg0_46)))))))),((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)p_4), ((IInteger)(((IInteger) ((IInteger)(M_List.size(((IList)($arg1_45))))).subtract(((IInteger)$constants.get(0)/*1*/)))))})))))), ((IInteger)$constants.get(0)/*1*/))));
                                         
                                         } else {
                                            continue $MCOMP42_DESC11265;
                                         }
                                      } else {
                                         continue $MCOMP42_DESC11265;
                                      }
                                   } else {
                                      continue $MCOMP42_DESC11265;
                                   }
                                } else {
                                   continue $MCOMP42_DESC11265;
                                }
                             } else {
                                continue $MCOMP42_DESC11265;
                             }
                          } else {
                             continue $MCOMP42_DESC11265;
                          }
                       } else {
                          continue $MCOMP42_DESC11265;
                       }
                    } else {
                       continue $MCOMP42_DESC11265;
                    }
                }
                
                             
            } while(false);
        IMap prodItems_3 = ((IMap)($mapwriter41.done()));
        ISet dnn_7 = ((ISet)(M_lang_rascal_grammar_definition_Priorities.doNotNest(((IConstructor)grammar_1))));
        final ISetWriter $setwriter47 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP48_GEN11847:
        for(IValue $elem49_for : ((ISet)dnn_7)){
            IValue $elem49 = (IValue) $elem49_for;
            final IValue $tuple_subject50 = ((IValue)($elem49));
            if($tuple_subject50 instanceof ITuple && ((ITuple)$tuple_subject50).arity() == 3){
               /*muExists*/$SCOMP48_GEN11847_TUPLE: 
                   do {
                       IConstructor father_8 = null;
                       IInteger pos_9 = null;
                       IConstructor child_10 = null;
                       if($is(((IConstructor)($subscript_int(((IValue)($tuple_subject50)),0))),((IString)$constants.get(19)/*"prod"*/))){
                         $setwriter47.insert($RVF.tuple(((IInteger)($atuple_field_project((ITuple)((ITuple)($amap_subscript(((IMap)($amap_subscript(((IMap)items_0),((IConstructor)($me.getType(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($subscript_int(((IValue)($tuple_subject50)),0))), "def"))))))))))),((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)($subscript_int(((IValue)($tuple_subject50)),0))), ((IInteger)($subscript_int(((IValue)($tuple_subject50)),1)))})))))), ((IInteger)$constants.get(0)/*1*/)))), ((IInteger)($amap_subscript(((IMap)prodItems_3),((IConstructor)($subscript_int(((IValue)($tuple_subject50)),2))))))));
                       
                       } else {
                         continue $SCOMP48_GEN11847_TUPLE;
                       }
               
                   } while(false);
            
            } else {
               continue $SCOMP48_GEN11847;
            }
        }
        
                    final ISetWriter $setwriter51 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP52_GEN11947:
        for(IValue $elem54_for : ((ISet)dnn_7)){
            IValue $elem54 = (IValue) $elem54_for;
            final IValue $tuple_subject55 = ((IValue)($elem54));
            if($tuple_subject55 instanceof ITuple && ((ITuple)$tuple_subject55).arity() == 3){
               /*muExists*/$SCOMP52_GEN11947_TUPLE: 
                   do {
                       final IConstructor $subject56 = ((IConstructor)($subscript_int(((IValue)($tuple_subject55)),0)));
                       if($has_type_and_arity($subject56, M_ParseTree.Production_regular_Symbol, 1)){
                          IValue $arg0_57 = (IValue)($aadt_subscript_int(((IConstructor)($subject56)),0));
                          if($isComparable($arg0_57.getType(), M_ParseTree.ADT_Symbol)){
                             IConstructor s_11 = ((IConstructor)($arg0_57));
                             IInteger pos_12 = ((IInteger)($subscript_int(((IValue)($tuple_subject55)),1)));
                             IConstructor child_13 = ((IConstructor)($subscript_int(((IValue)($tuple_subject55)),2)));
                             $SCOMP52_GEN11947_TUPLE_DESC11978:
                             for(IValue $elem53 : new DescendantMatchIterator(uniqueGrammar_2, 
                                 new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                                 if($isComparable($elem53.getType(), M_ParseTree.ADT_Symbol)){
                                    if($isSubtypeOf($elem53.getType(),M_ParseTree.ADT_Symbol)){
                                       IConstructor t_14 = ((IConstructor)($elem53));
                                       if((((IBool)($equal(((IConstructor)(M_Node.unsetRec(((IValue)t_14)))), ((IConstructor)($arg0_57)))))).getValue()){
                                         $setwriter51.insert($RVF.tuple(((IInteger)($me.getItemId(((IConstructor)t_14), ((IInteger)pos_12), ((IConstructor)child_13)))), ((IInteger)($amap_subscript(((IMap)prodItems_3),((IConstructor)child_13))))));
                                       
                                       } else {
                                         continue $SCOMP52_GEN11947_TUPLE_DESC11978;
                                       }
                                    
                                    } else {
                                       continue $SCOMP52_GEN11947_TUPLE_DESC11978;
                                    }
                                 } else {
                                    continue $SCOMP52_GEN11947_TUPLE_DESC11978;
                                 }
                             }
                             continue $SCOMP52_GEN11947_TUPLE;
                                          
                          } else {
                             continue $SCOMP52_GEN11947;
                          }
                       } else {
                          continue $SCOMP52_GEN11947;
                       }
                   } while(false);
            
            } else {
               continue $SCOMP52_GEN11947;
            }
        }
        
                    return ((ISet)($aset_add_aset(((ISet)($setwriter47.done())),((ISet)($setwriter51.done())))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(12028,869,<263,0>,<277,1>) 
    public IInteger lang_rascal_grammar_ParserGenerator_getItemId$47cbee5644cf9020(IConstructor s_0, IInteger pos_1, IConstructor $2){ 
        
        
        if($has_type_and_arity($2, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
           IValue $arg0_87 = (IValue)($aadt_subscript_int(((IConstructor)$2),0));
           if($isComparable($arg0_87.getType(), M_ParseTree.ADT_Symbol)){
              if($has_type_and_arity($arg0_87, M_Type.Symbol_label_str_Symbol, 2)){
                 IValue $arg0_89 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_87)),0));
                 if($isComparable($arg0_89.getType(), $T5)){
                    if(true){
                       IString l_2 = null;
                       IValue $arg1_88 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_87)),1));
                       if($isComparable($arg1_88.getType(), M_ParseTree.ADT_Symbol)){
                          IValue $arg1_86 = (IValue)($aadt_subscript_int(((IConstructor)$2),1));
                          if($isComparable($arg1_86.getType(), $T6)){
                             IValue $arg2_85 = (IValue)($aadt_subscript_int(((IConstructor)$2),2));
                             if($isComparable($arg2_85.getType(), $T27)){
                                final IConstructor $switchVal58 = ((IConstructor)s_0);
                                boolean noCaseMatched_$switchVal58 = true;
                                SWITCH17: switch(Fingerprint.getFingerprint($switchVal58)){
                                
                                    case -964239440:
                                        if(noCaseMatched_$switchVal58){
                                            noCaseMatched_$switchVal58 = false;
                                            if($isSubtypeOf($switchVal58.getType(),M_ParseTree.ADT_Symbol)){
                                               /*muExists*/CASE_964239440_5: 
                                                   do {
                                                       if($has_type_and_arity($switchVal58, M_ParseTree.Symbol_iter_star_seps_Symbol_list_Symbol, 2)){
                                                          IValue $arg0_67 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal58)),0));
                                                          if($isComparable($arg0_67.getType(), M_ParseTree.ADT_Symbol)){
                                                             IConstructor t_8 = null;
                                                             IValue $arg1_66 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal58)),1));
                                                             if($isComparable($arg1_66.getType(), $T1)){
                                                                if((((IBool)($equal(((IInteger)pos_1), ((IInteger)$constants.get(11)/*0*/))))).getValue()){
                                                                   return ((IInteger)($getkw_Symbol_id(((IConstructor)($arg0_67)))));
                                                                
                                                                } else {
                                                                   continue CASE_964239440_5;
                                                                }
                                                             }
                                                          
                                                          }
                                                       
                                                       }
                                               
                                                   } while(false);
                                            
                                            }
                                            if($isSubtypeOf($switchVal58.getType(),M_ParseTree.ADT_Symbol)){
                                               /*muExists*/CASE_964239440_6: 
                                                   do {
                                                       if($has_type_and_arity($switchVal58, M_ParseTree.Symbol_iter_star_seps_Symbol_list_Symbol, 2)){
                                                          IValue $arg0_69 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal58)),0));
                                                          if($isComparable($arg0_69.getType(), $T1)){
                                                             IValue $arg1_68 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal58)),1));
                                                             if($isComparable($arg1_68.getType(), $T6)){
                                                                IList ss_9 = null;
                                                                if((((IBool)($aint_lessequal_aint(((IInteger)pos_1),((IInteger)$constants.get(11)/*0*/)).not()))).getValue()){
                                                                   return ((IInteger)($getkw_Symbol_id(((IConstructor)($alist_subscript_int(((IList)($arg1_68)),((IInteger)(((IInteger) ((IInteger)pos_1).subtract(((IInteger)$constants.get(0)/*1*/))))).intValue()))))));
                                                                
                                                                } else {
                                                                   continue CASE_964239440_6;
                                                                }
                                                             }
                                                          
                                                          }
                                                       
                                                       }
                                               
                                                   } while(false);
                                            
                                            }
                                
                                        }
                                        
                                
                                    case 25942208:
                                        if(noCaseMatched_$switchVal58){
                                            noCaseMatched_$switchVal58 = false;
                                            if($isSubtypeOf($switchVal58.getType(),M_ParseTree.ADT_Symbol)){
                                               /*muExists*/CASE_25942208_1: 
                                                   do {
                                                       if($has_type_and_arity($switchVal58, M_ParseTree.Symbol_iter_Symbol, 1)){
                                                          IValue $arg0_60 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal58)),0));
                                                          if($isComparable($arg0_60.getType(), M_ParseTree.ADT_Symbol)){
                                                             IConstructor t_4 = null;
                                                             return ((IInteger)($getkw_Symbol_id(((IConstructor)($arg0_60)))));
                                                          
                                                          }
                                                       
                                                       }
                                               
                                                   } while(false);
                                            
                                            }
                                
                                        }
                                        
                                
                                    case 882072:
                                        if(noCaseMatched_$switchVal58){
                                            noCaseMatched_$switchVal58 = false;
                                            if($isSubtypeOf($switchVal58.getType(),M_ParseTree.ADT_Symbol)){
                                               /*muExists*/CASE_882072_0: 
                                                   do {
                                                       if($has_type_and_arity($switchVal58, M_ParseTree.Symbol_opt_Symbol, 1)){
                                                          IValue $arg0_59 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal58)),0));
                                                          if($isComparable($arg0_59.getType(), M_ParseTree.ADT_Symbol)){
                                                             IConstructor t_3 = null;
                                                             return ((IInteger)($getkw_Symbol_id(((IConstructor)($arg0_59)))));
                                                          
                                                          }
                                                       
                                                       }
                                               
                                                   } while(false);
                                            
                                            }
                                
                                        }
                                        
                                
                                    case 910072:
                                        if(noCaseMatched_$switchVal58){
                                            noCaseMatched_$switchVal58 = false;
                                            if($isSubtypeOf($switchVal58.getType(),M_ParseTree.ADT_Symbol)){
                                               /*muExists*/CASE_910072_7: 
                                                   do {
                                                       if($has_type_and_arity($switchVal58, M_ParseTree.Symbol_seq_list_Symbol, 1)){
                                                          IValue $arg0_70 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal58)),0));
                                                          if($isComparable($arg0_70.getType(), $T6)){
                                                             IList ss_10 = null;
                                                             return ((IInteger)($getkw_Symbol_id(((IConstructor)($alist_subscript_int(((IList)($arg0_70)),((IInteger)pos_1).intValue()))))));
                                                          
                                                          }
                                                       
                                                       }
                                               
                                                   } while(false);
                                            
                                            }
                                
                                        }
                                        
                                
                                    case 826203960:
                                        if(noCaseMatched_$switchVal58){
                                            noCaseMatched_$switchVal58 = false;
                                            if($isSubtypeOf($switchVal58.getType(),M_ParseTree.ADT_Symbol)){
                                               /*muExists*/CASE_826203960_2: 
                                                   do {
                                                       if($has_type_and_arity($switchVal58, M_ParseTree.Symbol_iter_star_Symbol, 1)){
                                                          IValue $arg0_61 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal58)),0));
                                                          if($isComparable($arg0_61.getType(), M_ParseTree.ADT_Symbol)){
                                                             IConstructor t_5 = null;
                                                             return ((IInteger)($getkw_Symbol_id(((IConstructor)($arg0_61)))));
                                                          
                                                          }
                                                       
                                                       }
                                               
                                                   } while(false);
                                            
                                            }
                                
                                        }
                                        
                                
                                    case 773448:
                                        if(noCaseMatched_$switchVal58){
                                            noCaseMatched_$switchVal58 = false;
                                            if($isSubtypeOf($switchVal58.getType(),M_ParseTree.ADT_Symbol)){
                                               /*muExists*/CASE_773448_8: 
                                                   do {
                                                       if($has_type_and_arity($switchVal58, M_ParseTree.Symbol_alt_set_Symbol, 1)){
                                                          IValue $arg0_84 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal58)),0));
                                                          if($isComparable($arg0_84.getType(), $T0)){
                                                             ISet aa_11 = null;
                                                             /*muExists*/IF22: 
                                                                 do {
                                                                     /*muExists*/IF22_GEN12768_CONS_conditional: 
                                                                         do {
                                                                             IF22_GEN12768:
                                                                             for(IValue $elem72_for : ((ISet)($arg0_84))){
                                                                                 IConstructor $elem72 = (IConstructor) $elem72_for;
                                                                                 if($has_type_and_arity($elem72, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
                                                                                    IValue $arg0_83 = (IValue)($aadt_subscript_int(((IConstructor)($elem72)),0));
                                                                                    if($isComparable($arg0_83.getType(), $T1)){
                                                                                       IValue $arg1_73 = (IValue)($aadt_subscript_int(((IConstructor)($elem72)),1));
                                                                                       if($isComparable($arg1_73.getType(), $T8)){
                                                                                          ISet $subject74 = (ISet)($arg1_73);
                                                                                          IF22_GEN12768_CONS_conditional_SET_MVAR$_71:
                                                                                          for(IValue $elem81_for : new SubSetGenerator(((ISet)($subject74)))){
                                                                                              ISet $elem81 = (ISet) $elem81_for;
                                                                                              final ISet $subject76 = ((ISet)(((ISet)($subject74)).subtract(((ISet)($elem81)))));
                                                                                              IF22_GEN12768_CONS_conditional_SET_MVAR$_71_CONS_except$_DFLT_SET_ELM79:
                                                                                              for(IValue $elem78_for : ((ISet)($subject76))){
                                                                                                  IConstructor $elem78 = (IConstructor) $elem78_for;
                                                                                                  if($has_type_and_arity($elem78, M_ParseTree.Condition_except_str, 1)){
                                                                                                     IValue $arg0_80 = (IValue)($aadt_subscript_int(((IConstructor)($elem78)),0));
                                                                                                     if($isComparable($arg0_80.getType(), $T5)){
                                                                                                        if(($arg0_89 != null)){
                                                                                                           if($arg0_89.match($arg0_80)){
                                                                                                              final ISet $subject77 = ((ISet)(((ISet)($subject76)).delete(((IConstructor)($elem78)))));
                                                                                                              if(((ISet)($subject77)).size() == 0){
                                                                                                                 IConstructor a_12 = ((IConstructor)($elem72));
                                                                                                                 return ((IInteger)($getkw_Symbol_id(((IConstructor)a_12))));
                                                                                                              
                                                                                                              } else {
                                                                                                                 continue IF22_GEN12768_CONS_conditional_SET_MVAR$_71_CONS_except$_DFLT_SET_ELM79;/*redirected IF22_GEN12768_CONS_conditional_SET_MVAR$_71_CONS_except to IF22_GEN12768_CONS_conditional_SET_MVAR$_71_CONS_except$_DFLT_SET_ELM79; set pat3*/
                                                                                                              }
                                                                                                           } else {
                                                                                                              continue IF22_GEN12768_CONS_conditional_SET_MVAR$_71_CONS_except$_DFLT_SET_ELM79;/*default set elem*/
                                                                                                           }
                                                                                                        } else {
                                                                                                           $arg0_89 = ((IValue)($arg0_80));
                                                                                                           final ISet $subject77 = ((ISet)(((ISet)($subject76)).delete(((IConstructor)($elem78)))));
                                                                                                           if(((ISet)($subject77)).size() == 0){
                                                                                                              IConstructor a_12 = ((IConstructor)($elem72));
                                                                                                              return ((IInteger)($getkw_Symbol_id(((IConstructor)a_12))));
                                                                                                           
                                                                                                           } else {
                                                                                                              continue IF22_GEN12768_CONS_conditional_SET_MVAR$_71_CONS_except$_DFLT_SET_ELM79;/*redirected IF22_GEN12768_CONS_conditional_SET_MVAR$_71_CONS_except to IF22_GEN12768_CONS_conditional_SET_MVAR$_71_CONS_except$_DFLT_SET_ELM79; set pat3*/
                                                                                                           }
                                                                                                        }
                                                                                                     } else {
                                                                                                        continue IF22_GEN12768_CONS_conditional_SET_MVAR$_71_CONS_except$_DFLT_SET_ELM79;/*default set elem*/
                                                                                                     }
                                                                                                  } else {
                                                                                                     continue IF22_GEN12768_CONS_conditional_SET_MVAR$_71_CONS_except$_DFLT_SET_ELM79;/*default set elem*/
                                                                                                  }
                                                                                              }
                                                                                              continue IF22_GEN12768_CONS_conditional_SET_MVAR$_71;/*set pat4*/
                                                                                                          
                                                                                          }
                                                                                          continue IF22_GEN12768;
                                                                                                      
                                                                                       } else {
                                                                                          continue IF22_GEN12768;
                                                                                       }
                                                                                    } else {
                                                                                       continue IF22_GEN12768;
                                                                                    }
                                                                                 } else {
                                                                                    continue IF22_GEN12768;
                                                                                 }
                                                                             }
                                                                             
                                                                                         
                                                                         } while(false);
                                                             
                                                                 } while(false);
                                                             break SWITCH17;// succeedSwitch
                                                          }
                                                       
                                                       }
                                               
                                                   } while(false);
                                            
                                            }
                                
                                        }
                                        
                                
                                    case 1652184736:
                                        if(noCaseMatched_$switchVal58){
                                            noCaseMatched_$switchVal58 = false;
                                            if($isSubtypeOf($switchVal58.getType(),M_ParseTree.ADT_Symbol)){
                                               /*muExists*/CASE_1652184736_3: 
                                                   do {
                                                       if($has_type_and_arity($switchVal58, M_ParseTree.Symbol_iter_seps_Symbol_list_Symbol, 2)){
                                                          IValue $arg0_63 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal58)),0));
                                                          if($isComparable($arg0_63.getType(), M_ParseTree.ADT_Symbol)){
                                                             IConstructor t_6 = null;
                                                             IValue $arg1_62 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal58)),1));
                                                             if($isComparable($arg1_62.getType(), $T1)){
                                                                if((((IBool)($equal(((IInteger)pos_1), ((IInteger)$constants.get(11)/*0*/))))).getValue()){
                                                                   return ((IInteger)($getkw_Symbol_id(((IConstructor)($arg0_63)))));
                                                                
                                                                } else {
                                                                   continue CASE_1652184736_3;
                                                                }
                                                             }
                                                          
                                                          }
                                                       
                                                       }
                                               
                                                   } while(false);
                                            
                                            }
                                            if($isSubtypeOf($switchVal58.getType(),M_ParseTree.ADT_Symbol)){
                                               /*muExists*/CASE_1652184736_4: 
                                                   do {
                                                       if($has_type_and_arity($switchVal58, M_ParseTree.Symbol_iter_seps_Symbol_list_Symbol, 2)){
                                                          IValue $arg0_65 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal58)),0));
                                                          if($isComparable($arg0_65.getType(), $T1)){
                                                             IValue $arg1_64 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal58)),1));
                                                             if($isComparable($arg1_64.getType(), $T6)){
                                                                IList ss_7 = null;
                                                                if((((IBool)($aint_lessequal_aint(((IInteger)pos_1),((IInteger)$constants.get(11)/*0*/)).not()))).getValue()){
                                                                   return ((IInteger)($getkw_Symbol_id(((IConstructor)($alist_subscript_int(((IList)($arg1_64)),((IInteger)(((IInteger) ((IInteger)pos_1).subtract(((IInteger)$constants.get(0)/*1*/))))).intValue()))))));
                                                                
                                                                } else {
                                                                   continue CASE_1652184736_4;
                                                                }
                                                             }
                                                          
                                                          }
                                                       
                                                       }
                                               
                                                   } while(false);
                                            
                                            }
                                
                                        }
                                        
                                
                                    default: 
                                }
                                
                                           return ((IInteger)($getkw_Symbol_id(((IConstructor)s_0))));
                             
                             } else {
                                return null;
                             }
                          } else {
                             return null;
                          }
                       } else {
                          return null;
                       }
                    } else {
                       return null;
                    }
                 } else {
                    return null;
                 }
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(12901,46,<281,0>,<281,46>) 
    public IConstructor lang_rascal_grammar_ParserGenerator_getType$37ff38bece5b0e2f(IConstructor p_0){ 
        
        
        return ((IConstructor)($me.getType(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_0), "def"))))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(12948,52,<282,0>,<282,52>) 
    public IConstructor lang_rascal_grammar_ParserGenerator_getType$18c51bf3658df5a8(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_Type.Symbol_label_str_Symbol, 2)){
           IValue $arg0_91 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_91.getType(), $T5)){
              IValue $arg1_90 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_90.getType(), M_ParseTree.ADT_Symbol)){
                 IConstructor s_0 = null;
                 return ((IConstructor)($me.getType(((IConstructor)($arg1_90)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(13001,70,<283,0>,<283,70>) 
    public IConstructor lang_rascal_grammar_ParserGenerator_getType$cc1e9058e7705307(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
           IValue $arg0_93 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_93.getType(), M_ParseTree.ADT_Symbol)){
              IConstructor s_0 = null;
              IValue $arg1_92 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_92.getType(), $T8)){
                 ISet cs_1 = null;
                 return ((IConstructor)($me.getType(((IConstructor)($arg0_93)))));
              
              } else {
                 return null;
              }
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(13072,47,<284,0>,<284,47>) 
    public IConstructor lang_rascal_grammar_ParserGenerator_getType$38f71884bfc4046e(IConstructor s_0){ 
        
        
        return ((IConstructor)(M_Node.unsetRec(((IValue)s_0))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(13590,42,<293,2>,<293,44>) 
    public IConstructor lang_rascal_grammar_ParserGenerator_cl$43bfc4d41d835634(IConstructor p_0){ 
        
        
        return ((IConstructor)(M_Node.unsetRec(((IValue)p_0))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(13122,2413,<287,0>,<344,1>) 
    public IMap lang_rascal_grammar_ParserGenerator_generateNewItems$2a1734bec0bd57ea(IConstructor $aux_g_0){ 
        ValueRef<IConstructor> g_0 = new ValueRef<IConstructor>("g_0", $aux_g_0);
    
        
        try {
            final ValueRef<IMap> items_1 = new ValueRef<IMap>("items", ((IMap)$constants.get(14)/*()*/));
            final ValueRef<IMap> fresh_2 = new ValueRef<IMap>("fresh", ((IMap)$constants.get(14)/*()*/));
            $TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.No, 
                 new DescendantDescriptorAlwaysTrue($RVF.bool(false)),
                 g_0.getValue(),
                 (IVisitFunction) (IValue $VISIT23_subject, TraversalState $traversalState) -> {
                     VISIT23:switch(Fingerprint.getFingerprint($VISIT23_subject)){
                     
                         case 101776608:
                             if($isSubtypeOf($VISIT23_subject.getType(),M_ParseTree.ADT_Production)){
                                /*muExists*/CASE_101776608_2: 
                                    do {
                                        if($has_type_and_arity($VISIT23_subject, M_ParseTree.Production_regular_Symbol, 1)){
                                           IValue $arg0_118 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT23_subject)),0));
                                           if($isComparable($arg0_118.getType(), M_ParseTree.ADT_Symbol)){
                                              IConstructor s_10 = ((IConstructor)($arg0_118));
                                              IConstructor p_9 = ((IConstructor)($VISIT23_subject));
                                              /*muExists*/WHILE27_BT: 
                                                  do {
                                                      WHILE27:
                                                          while((((IBool)(($is(((IConstructor)($arg0_118)),((IString)$constants.get(20)/*"conditional"*/)) ? ((IBool)$constants.get(7)/*true*/) : $RVF.bool($is(((IConstructor)($arg0_118)),((IString)$constants.get(21)/*"label"*/))))))).getValue()){
                                                              $arg0_118 = ((IValue)(((IConstructor)($aadt_get_field(((IConstructor)($arg0_118)), "symbol")))));
                                                      
                                                          }
                                              
                                                  } while(false);
                                              /* void:  muCon([]) */IConstructor us_11 = ((IConstructor)(M_Node.unsetRec(((IValue)($arg0_118)))));
                                              p_9 = ((IConstructor)(M_Node.unsetRec(((IValue)p_9))));
                                              final IConstructor $switchVal103 = ((IConstructor)($arg0_118));
                                              boolean noCaseMatched_$switchVal103 = true;
                                              SWITCH28: switch(Fingerprint.getFingerprint($switchVal103)){
                                              
                                                  case -964239440:
                                                      if(noCaseMatched_$switchVal103){
                                                          noCaseMatched_$switchVal103 = false;
                                                          if($isSubtypeOf($switchVal103.getType(),M_ParseTree.ADT_Symbol)){
                                                             /*muExists*/CASE_964239440_3: 
                                                                 do {
                                                                     if($has_type_and_arity($switchVal103, M_ParseTree.Symbol_iter_star_seps_Symbol_list_Symbol, 2)){
                                                                        IValue $arg0_112 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal103)),0));
                                                                        if($isComparable($arg0_112.getType(), M_ParseTree.ADT_Symbol)){
                                                                           IConstructor elem_17 = ((IConstructor)($arg0_112));
                                                                           IValue $arg1_111 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal103)),1));
                                                                           if($isComparable($arg1_111.getType(), $T6)){
                                                                              IList seps_18 = ((IList)($arg1_111));
                                                                              GuardedIValue guarded35 = $guarded_map_subscript(items_1.getValue(),((IConstructor)us_11));
                                                                              items_1.setValue(((IMap)($amap_update(us_11,$amap_add_amap(((IMap)(($is_defined_value(guarded35) ? ((IMap)$get_defined_value(guarded35)) : fresh_2.getValue()))),((IMap)($buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)p_9), ((IInteger)$constants.get(11)/*0*/)}))), ((ITuple)($me.sym2newitem(g_0.getValue(), ((IConstructor)($arg0_112)), ((IInteger)$constants.get(11)/*0*/)))))))), ((IMap)(items_1.getValue()))))));
                                                                              /*muExists*/FOR36: 
                                                                                  do {
                                                                                      FOR36_GEN14777:
                                                                                      for(IValue $elem110_for : ((IList)(M_List.index(((IList)($arg1_111)))))){
                                                                                          IInteger $elem110 = (IInteger) $elem110_for;
                                                                                          IInteger i_19 = ((IInteger)($elem110));
                                                                                          GuardedIValue guarded37 = $guarded_map_subscript(items_1.getValue(),((IConstructor)us_11));
                                                                                          items_1.setValue(((IMap)($amap_update(us_11,$amap_add_amap(((IMap)(($is_defined_value(guarded37) ? ((IMap)$get_defined_value(guarded37)) : fresh_2.getValue()))),((IMap)($buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)p_9), ((IInteger)($aint_add_aint(((IInteger)i_19),((IInteger)$constants.get(0)/*1*/))))}))), ((ITuple)($me.sym2newitem(g_0.getValue(), ((IConstructor)($alist_subscript_int(((IList)($arg1_111)),((IInteger)i_19).intValue()))), ((IInteger)($aint_add_aint(((IInteger)i_19),((IInteger)$constants.get(0)/*1*/))))))))))), ((IMap)(items_1.getValue()))))));
                                                                                      
                                                                                      }
                                                                                      continue FOR36;
                                                                                                  
                                                                                  } while(false);
                                                                              /* void:  muCon([]) */break SWITCH28;// succeedSwitch
                                                                           }
                                                                        
                                                                        }
                                                                     
                                                                     }
                                                             
                                                                 } while(false);
                                                          
                                                          }
                                              
                                                      }
                                                      
                                              
                                                  case 25942208:
                                                      if(noCaseMatched_$switchVal103){
                                                          noCaseMatched_$switchVal103 = false;
                                                          if($isSubtypeOf($switchVal103.getType(),M_ParseTree.ADT_Symbol)){
                                                             /*muExists*/CASE_25942208_0: 
                                                                 do {
                                                                     if($has_type_and_arity($switchVal103, M_ParseTree.Symbol_iter_Symbol, 1)){
                                                                        IValue $arg0_105 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal103)),0));
                                                                        if($isComparable($arg0_105.getType(), M_ParseTree.ADT_Symbol)){
                                                                           IConstructor elem_12 = ((IConstructor)($arg0_105));
                                                                           GuardedIValue guarded30 = $guarded_map_subscript(items_1.getValue(),((IConstructor)us_11));
                                                                           items_1.setValue(((IMap)($amap_update(us_11,$amap_add_amap(((IMap)(($is_defined_value(guarded30) ? ((IMap)$get_defined_value(guarded30)) : fresh_2.getValue()))),((IMap)($buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)p_9), ((IInteger)$constants.get(11)/*0*/)}))), ((ITuple)($me.sym2newitem(g_0.getValue(), ((IConstructor)($arg0_105)), ((IInteger)$constants.get(11)/*0*/)))))))), ((IMap)(items_1.getValue()))))));
                                                                           break SWITCH28;// succeedSwitch
                                                                        }
                                                                     
                                                                     }
                                                             
                                                                 } while(false);
                                                          
                                                          }
                                              
                                                      }
                                                      
                                              
                                                  case 882072:
                                                      if(noCaseMatched_$switchVal103){
                                                          noCaseMatched_$switchVal103 = false;
                                                          if($isSubtypeOf($switchVal103.getType(),M_ParseTree.ADT_Symbol)){
                                                             /*muExists*/CASE_882072_5: 
                                                                 do {
                                                                     if($has_type_and_arity($switchVal103, M_ParseTree.Symbol_opt_Symbol, 1)){
                                                                        IValue $arg0_115 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal103)),0));
                                                                        if($isComparable($arg0_115.getType(), M_ParseTree.ADT_Symbol)){
                                                                           IConstructor elem_22 = ((IConstructor)($arg0_115));
                                                                           GuardedIValue guarded40 = $guarded_map_subscript(items_1.getValue(),((IConstructor)us_11));
                                                                           items_1.setValue(((IMap)($amap_update(us_11,$amap_add_amap(((IMap)(($is_defined_value(guarded40) ? ((IMap)$get_defined_value(guarded40)) : fresh_2.getValue()))),((IMap)($buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)p_9), ((IInteger)$constants.get(11)/*0*/)}))), ((ITuple)($me.sym2newitem(g_0.getValue(), ((IConstructor)($arg0_115)), ((IInteger)$constants.get(11)/*0*/)))))))), ((IMap)(items_1.getValue()))))));
                                                                           break SWITCH28;// succeedSwitch
                                                                        }
                                                                     
                                                                     }
                                                             
                                                                 } while(false);
                                                          
                                                          }
                                              
                                                      }
                                                      
                                              
                                                  case 910072:
                                                      if(noCaseMatched_$switchVal103){
                                                          noCaseMatched_$switchVal103 = false;
                                                          if($isSubtypeOf($switchVal103.getType(),M_ParseTree.ADT_Symbol)){
                                                             /*muExists*/CASE_910072_4: 
                                                                 do {
                                                                     if($has_type_and_arity($switchVal103, M_ParseTree.Symbol_seq_list_Symbol, 1)){
                                                                        IValue $arg0_114 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal103)),0));
                                                                        if($isComparable($arg0_114.getType(), $T6)){
                                                                           IList elems_20 = ((IList)($arg0_114));
                                                                           /*muExists*/FOR38: 
                                                                               do {
                                                                                   FOR38_GEN14983:
                                                                                   for(IValue $elem113_for : ((IList)(M_List.index(((IList)($arg0_114)))))){
                                                                                       IInteger $elem113 = (IInteger) $elem113_for;
                                                                                       IInteger i_21 = ((IInteger)($elem113));
                                                                                       GuardedIValue guarded39 = $guarded_map_subscript(items_1.getValue(),((IConstructor)us_11));
                                                                                       items_1.setValue(((IMap)($amap_update(us_11,$amap_add_amap(((IMap)(($is_defined_value(guarded39) ? ((IMap)$get_defined_value(guarded39)) : fresh_2.getValue()))),((IMap)($buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)p_9), ((IInteger)($aint_add_aint(((IInteger)i_21),((IInteger)$constants.get(0)/*1*/))))}))), ((ITuple)($me.sym2newitem(g_0.getValue(), ((IConstructor)($alist_subscript_int(((IList)($arg0_114)),((IInteger)i_21).intValue()))), ((IInteger)($aint_add_aint(((IInteger)i_21),((IInteger)$constants.get(0)/*1*/))))))))))), ((IMap)(items_1.getValue()))))));
                                                                                   
                                                                                   }
                                                                                   continue FOR38;
                                                                                               
                                                                               } while(false);
                                                                           /* void:  muCon([]) */break SWITCH28;// succeedSwitch
                                                                        }
                                                                     
                                                                     }
                                                             
                                                                 } while(false);
                                                          
                                                          }
                                              
                                                      }
                                                      
                                              
                                                  case 826203960:
                                                      if(noCaseMatched_$switchVal103){
                                                          noCaseMatched_$switchVal103 = false;
                                                          if($isSubtypeOf($switchVal103.getType(),M_ParseTree.ADT_Symbol)){
                                                             /*muExists*/CASE_826203960_1: 
                                                                 do {
                                                                     if($has_type_and_arity($switchVal103, M_ParseTree.Symbol_iter_star_Symbol, 1)){
                                                                        IValue $arg0_106 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal103)),0));
                                                                        if($isComparable($arg0_106.getType(), M_ParseTree.ADT_Symbol)){
                                                                           IConstructor elem_13 = ((IConstructor)($arg0_106));
                                                                           GuardedIValue guarded31 = $guarded_map_subscript(items_1.getValue(),((IConstructor)us_11));
                                                                           items_1.setValue(((IMap)($amap_update(us_11,$amap_add_amap(((IMap)(($is_defined_value(guarded31) ? ((IMap)$get_defined_value(guarded31)) : fresh_2.getValue()))),((IMap)($buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)p_9), ((IInteger)$constants.get(11)/*0*/)}))), ((ITuple)($me.sym2newitem(g_0.getValue(), ((IConstructor)($arg0_106)), ((IInteger)$constants.get(11)/*0*/)))))))), ((IMap)(items_1.getValue()))))));
                                                                           break SWITCH28;// succeedSwitch
                                                                        }
                                                                     
                                                                     }
                                                             
                                                                 } while(false);
                                                          
                                                          }
                                              
                                                      }
                                                      
                                              
                                                  case 773448:
                                                      if(noCaseMatched_$switchVal103){
                                                          noCaseMatched_$switchVal103 = false;
                                                          if($isSubtypeOf($switchVal103.getType(),M_ParseTree.ADT_Symbol)){
                                                             /*muExists*/CASE_773448_6: 
                                                                 do {
                                                                     if($has_type_and_arity($switchVal103, M_ParseTree.Symbol_alt_set_Symbol, 1)){
                                                                        IValue $arg0_117 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal103)),0));
                                                                        if($isComparable($arg0_117.getType(), $T0)){
                                                                           ISet alts_23 = ((ISet)($arg0_117));
                                                                           /*muExists*/FOR41: 
                                                                               do {
                                                                                   FOR41_GEN15258:
                                                                                   for(IValue $elem116_for : ((ISet)($arg0_117))){
                                                                                       IConstructor $elem116 = (IConstructor) $elem116_for;
                                                                                       IConstructor elem_24 = ((IConstructor)($elem116));
                                                                                       GuardedIValue guarded42 = $guarded_map_subscript(items_1.getValue(),((IConstructor)us_11));
                                                                                       items_1.setValue(((IMap)($amap_update(us_11,$amap_add_amap(((IMap)(($is_defined_value(guarded42) ? ((IMap)$get_defined_value(guarded42)) : fresh_2.getValue()))),((IMap)($buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)p_9), ((IInteger)$constants.get(11)/*0*/)}))), ((ITuple)($me.sym2newitem(g_0.getValue(), ((IConstructor)elem_24), ((IInteger)$constants.get(11)/*0*/)))))))), ((IMap)(items_1.getValue()))))));
                                                                                   
                                                                                   }
                                                                                   continue FOR41;
                                                                                               
                                                                               } while(false);
                                                                           /* void:  muCon([]) */break SWITCH28;// succeedSwitch
                                                                        }
                                                                     
                                                                     }
                                                             
                                                                 } while(false);
                                                          
                                                          }
                                              
                                                      }
                                                      
                                              
                                                  case 1652184736:
                                                      if(noCaseMatched_$switchVal103){
                                                          noCaseMatched_$switchVal103 = false;
                                                          if($isSubtypeOf($switchVal103.getType(),M_ParseTree.ADT_Symbol)){
                                                             /*muExists*/CASE_1652184736_2: 
                                                                 do {
                                                                     if($has_type_and_arity($switchVal103, M_ParseTree.Symbol_iter_seps_Symbol_list_Symbol, 2)){
                                                                        IValue $arg0_109 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal103)),0));
                                                                        if($isComparable($arg0_109.getType(), M_ParseTree.ADT_Symbol)){
                                                                           IConstructor elem_14 = ((IConstructor)($arg0_109));
                                                                           IValue $arg1_108 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal103)),1));
                                                                           if($isComparable($arg1_108.getType(), $T6)){
                                                                              IList seps_15 = ((IList)($arg1_108));
                                                                              GuardedIValue guarded32 = $guarded_map_subscript(items_1.getValue(),((IConstructor)us_11));
                                                                              items_1.setValue(((IMap)($amap_update(us_11,$amap_add_amap(((IMap)(($is_defined_value(guarded32) ? ((IMap)$get_defined_value(guarded32)) : fresh_2.getValue()))),((IMap)($buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)p_9), ((IInteger)$constants.get(11)/*0*/)}))), ((ITuple)($me.sym2newitem(g_0.getValue(), ((IConstructor)($arg0_109)), ((IInteger)$constants.get(11)/*0*/)))))))), ((IMap)(items_1.getValue()))))));
                                                                              /*muExists*/FOR33: 
                                                                                  do {
                                                                                      FOR33_GEN14523:
                                                                                      for(IValue $elem107_for : ((IList)(M_List.index(((IList)($arg1_108)))))){
                                                                                          IInteger $elem107 = (IInteger) $elem107_for;
                                                                                          IInteger i_16 = ((IInteger)($elem107));
                                                                                          GuardedIValue guarded34 = $guarded_map_subscript(items_1.getValue(),((IConstructor)us_11));
                                                                                          items_1.setValue(((IMap)($amap_update(us_11,$amap_add_amap(((IMap)(($is_defined_value(guarded34) ? ((IMap)$get_defined_value(guarded34)) : fresh_2.getValue()))),((IMap)($buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)p_9), ((IInteger)($aint_add_aint(((IInteger)i_16),((IInteger)$constants.get(0)/*1*/))))}))), ((ITuple)($me.sym2newitem(g_0.getValue(), ((IConstructor)($alist_subscript_int(((IList)($arg1_108)),((IInteger)i_16).intValue()))), ((IInteger)($aint_add_aint(((IInteger)i_16),((IInteger)$constants.get(0)/*1*/))))))))))), ((IMap)(items_1.getValue()))))));
                                                                                      
                                                                                      }
                                                                                      continue FOR33;
                                                                                                  
                                                                                  } while(false);
                                                                              /* void:  muCon([]) */break SWITCH28;// succeedSwitch
                                                                           }
                                                                        
                                                                        }
                                                                     
                                                                     }
                                                             
                                                                 } while(false);
                                                          
                                                          }
                                              
                                                      }
                                                      
                                              
                                                  case 0:
                                                      if(noCaseMatched_$switchVal103){
                                                          noCaseMatched_$switchVal103 = false;
                                                          
                                                      }
                                                      
                                              
                                                  default: if($isSubtypeOf($switchVal103.getType(),M_ParseTree.ADT_Symbol)){
                                                              /*muExists*/CASE_0_7: 
                                                                  do {
                                                                      if($has_type_and_arity($switchVal103, M_ParseTree.Symbol_empty_, 0)){
                                                                         GuardedIValue guarded29 = $guarded_map_subscript(items_1.getValue(),((IConstructor)us_11));
                                                                         final Template $template104 = (Template)new Template($RVF, "new EpsilonStackNode<IConstructor>(");
                                                                         $template104.beginIndent("                                     ");
                                                                         $template104.addVal($getkw_Symbol_id(((IConstructor)($arg0_118))));
                                                                         $template104.endIndent("                                     ");
                                                                         $template104.addStr(", 0)");
                                                                         items_1.setValue(((IMap)($amap_update(us_11,$amap_add_amap(((IMap)(($is_defined_value(guarded29) ? ((IMap)$get_defined_value(guarded29)) : fresh_2.getValue()))),((IMap)($buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)p_9), ((IInteger)$constants.get(15)/*-1*/)}))), ((ITuple)($RVF.tuple(((IString)($template104.close())), ((IInteger)($getkw_Symbol_id(((IConstructor)($arg0_118)))))))))))), ((IMap)(items_1.getValue()))))));
                                                                         break SWITCH28;// succeedSwitch
                                                                      }
                                                              
                                                                  } while(false);
                                                           
                                                           }
                                              
                                              }
                                              
                                                         $traversalState.setMatched(true); 
                                              break VISIT23;
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                         case 110389984:
                             if($isSubtypeOf($VISIT23_subject.getType(),M_ParseTree.ADT_Production)){
                                /*muExists*/CASE_110389984_0: 
                                    do {
                                        if($has_type_and_arity($VISIT23_subject, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                           IValue $arg0_97 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT23_subject)),0));
                                           if($isComparable($arg0_97.getType(), M_ParseTree.ADT_Symbol)){
                                              IConstructor s_4 = ((IConstructor)($arg0_97));
                                              IValue $arg1_96 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT23_subject)),1));
                                              if($isComparable($arg1_96.getType(), $T28)){
                                                 if($arg1_96.equals(((IList)$constants.get(22)/*[]*/))){
                                                   IValue $arg2_95 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT23_subject)),2));
                                                   if($isComparable($arg2_95.getType(), $T1)){
                                                      IConstructor p_3 = ((IConstructor)($VISIT23_subject));
                                                      GuardedIValue guarded24 = $guarded_map_subscript(items_1.getValue(),((IConstructor)($me.getType(((IConstructor)($arg0_97))))));
                                                      final Template $template94 = (Template)new Template($RVF, "new EpsilonStackNode<IConstructor>(");
                                                      $template94.beginIndent("                                     ");
                                                      $template94.addVal($getkw_Symbol_id(((IConstructor)($arg0_97))));
                                                      $template94.endIndent("                                     ");
                                                      $template94.addStr(", 0)");
                                                      items_1.setValue(((IMap)($amap_update($me.getType(((IConstructor)($arg0_97))),$amap_add_amap(((IMap)(($is_defined_value(guarded24) ? ((IMap)$get_defined_value(guarded24)) : fresh_2.getValue()))),((IMap)($buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)(lang_rascal_grammar_ParserGenerator_generateNewItems$2a1734bec0bd57ea_cl(((IConstructor)p_3)))), ((IInteger)$constants.get(15)/*-1*/)}))), ((ITuple)($RVF.tuple(((IString)($template94.close())), ((IInteger)($getkw_Symbol_id(((IConstructor)($arg0_97)))))))))))), ((IMap)(items_1.getValue()))))));
                                                      $traversalState.setMatched(true); 
                                                      break VISIT23;
                                                   }
                                                 
                                                 }
                                              
                                              }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT23_subject.getType(),M_ParseTree.ADT_Production)){
                                /*muExists*/CASE_110389984_1: 
                                    do {
                                        if($has_type_and_arity($VISIT23_subject, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                           IValue $arg0_101 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT23_subject)),0));
                                           if($isComparable($arg0_101.getType(), M_ParseTree.ADT_Symbol)){
                                              IConstructor s_6 = ((IConstructor)($arg0_101));
                                              IValue $arg1_100 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT23_subject)),1));
                                              if($isComparable($arg1_100.getType(), $T6)){
                                                 IList lhs_7 = ((IList)($arg1_100));
                                                 IValue $arg2_99 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT23_subject)),2));
                                                 if($isComparable($arg2_99.getType(), $T1)){
                                                    IConstructor p_5 = ((IConstructor)($VISIT23_subject));
                                                    /*muExists*/FOR25: 
                                                        do {
                                                            FOR25_GEN13878:
                                                            for(IValue $elem98_for : ((IList)(M_List.index(((IList)($arg1_100)))))){
                                                                IInteger $elem98 = (IInteger) $elem98_for;
                                                                IInteger i_8 = ((IInteger)($elem98));
                                                                GuardedIValue guarded26 = $guarded_map_subscript(items_1.getValue(),((IConstructor)($me.getType(((IConstructor)($arg0_101))))));
                                                                items_1.setValue(((IMap)($amap_update($me.getType(((IConstructor)($arg0_101))),$amap_add_amap(((IMap)(($is_defined_value(guarded26) ? ((IMap)$get_defined_value(guarded26)) : fresh_2.getValue()))),((IMap)($buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)(lang_rascal_grammar_ParserGenerator_generateNewItems$2a1734bec0bd57ea_cl(((IConstructor)p_5)))), ((IInteger)i_8)}))), ((ITuple)($me.sym2newitem(g_0.getValue(), ((IConstructor)($alist_subscript_int(((IList)($arg1_100)),((IInteger)i_8).intValue()))), ((IInteger)i_8)))))))), ((IMap)(items_1.getValue()))))));
                                                            
                                                            }
                                                            continue FOR25;
                                                                        
                                                        } while(false);
                                                    /* void:  muCon([]) */$traversalState.setMatched(true); 
                                                    break VISIT23;
                                                 }
                                              
                                              }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                     
                     }
                     return $VISIT23_subject;
                 });return items_1.getValue();
        
        } catch (ReturnFromTraversalException e) {
            return (IMap) e.getValue();
        }
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(15537,166,<346,0>,<353,1>) 
    public IString lang_rascal_grammar_ParserGenerator_split$452f305967b0884c(IString x_0){ 
        
        
        if((((IBool)($aint_lessequal_aint(((IInteger)(M_String.size(((IString)x_0)))),((IInteger)$constants.get(23)/*20000*/))))).getValue()){
           final Template $template120 = (Template)new Template($RVF, "\"");
           $template120.beginIndent("  ");
           $template120.addStr(((IString)($me.esc(((IString)x_0)))).getValue());
           $template120.endIndent("  ");
           $template120.addStr("\"");
           return ((IString)($template120.close()));
        
        } else {
           final Template $template119 = (Template)new Template($RVF, "");
           $template119.addStr(((IString)($me.split(((IString)(M_String.substring(((IString)x_0), ((IInteger)$constants.get(11)/*0*/), ((IInteger)$constants.get(24)/*10000*/))))))).getValue());
           $template119.addStr(", ");
           $template119.beginIndent("  ");
           $template119.addStr(((IString)($me.split(((IString)(M_String.substring(((IString)x_0), ((IInteger)$constants.get(24)/*10000*/))))))).getValue());
           $template119.endIndent("  ");
           return ((IString)($template119.close()));
        
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(15705,549,<355,0>,<368,1>) 
    public IBool lang_rascal_grammar_ParserGenerator_isNonterminal$ae0c546df6bca290(IConstructor s_0){ 
        
        
        final IConstructor $switchVal121 = ((IConstructor)s_0);
        boolean noCaseMatched_$switchVal121 = true;
        SWITCH44: switch(Fingerprint.getFingerprint($switchVal121)){
        
            case 1643638592:
                if(noCaseMatched_$switchVal121){
                    noCaseMatched_$switchVal121 = false;
                    if($isSubtypeOf($switchVal121.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1643638592_0: 
                           do {
                               if($has_type_and_arity($switchVal121, M_Type.Symbol_label_str_Symbol, 2)){
                                  IValue $arg0_123 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal121)),0));
                                  if($isComparable($arg0_123.getType(), $T1)){
                                     IValue $arg1_122 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal121)),1));
                                     if($isComparable($arg1_122.getType(), M_ParseTree.ADT_Symbol)){
                                        IConstructor x_1 = ((IConstructor)($arg1_122));
                                        return ((IBool)($me.isNonterminal(((IConstructor)($arg1_122)))));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 1444258592:
                if(noCaseMatched_$switchVal121){
                    noCaseMatched_$switchVal121 = false;
                    if($isSubtypeOf($switchVal121.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1444258592_4: 
                           do {
                               if($has_type_and_arity($switchVal121, M_ParseTree.Symbol_parameterized_sort_str_list_Symbol, 2)){
                                  IValue $arg0_128 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal121)),0));
                                  if($isComparable($arg0_128.getType(), $T1)){
                                     IValue $arg1_127 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal121)),1));
                                     if($isComparable($arg1_127.getType(), $T1)){
                                        return ((IBool)$constants.get(7)/*true*/);
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -109773488:
                if(noCaseMatched_$switchVal121){
                    noCaseMatched_$switchVal121 = false;
                    if($isSubtypeOf($switchVal121.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_109773488_3: 
                           do {
                               if($has_type_and_arity($switchVal121, M_ParseTree.Symbol_keywords_str, 1)){
                                  IValue $arg0_126 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal121)),0));
                                  if($isComparable($arg0_126.getType(), $T1)){
                                     return ((IBool)$constants.get(7)/*true*/);
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 878060304:
                if(noCaseMatched_$switchVal121){
                    noCaseMatched_$switchVal121 = false;
                    if($isSubtypeOf($switchVal121.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_878060304_6: 
                           do {
                               if($has_type_and_arity($switchVal121, M_ParseTree.Symbol_start_Symbol, 1)){
                                  IValue $arg0_131 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal121)),0));
                                  if($isComparable($arg0_131.getType(), $T1)){
                                     return ((IBool)$constants.get(7)/*true*/);
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 856312:
                if(noCaseMatched_$switchVal121){
                    noCaseMatched_$switchVal121 = false;
                    if($isSubtypeOf($switchVal121.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_856312_2: 
                           do {
                               if($has_type_and_arity($switchVal121, M_ParseTree.Symbol_lex_str, 1)){
                                  IValue $arg0_125 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal121)),0));
                                  if($isComparable($arg0_125.getType(), $T1)){
                                     return ((IBool)$constants.get(7)/*true*/);
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 1154855088:
                if(noCaseMatched_$switchVal121){
                    noCaseMatched_$switchVal121 = false;
                    if($isSubtypeOf($switchVal121.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1154855088_5: 
                           do {
                               if($has_type_and_arity($switchVal121, M_ParseTree.Symbol_parameterized_lex_str_list_Symbol, 2)){
                                  IValue $arg0_130 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal121)),0));
                                  if($isComparable($arg0_130.getType(), $T1)){
                                     IValue $arg1_129 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal121)),1));
                                     if($isComparable($arg1_129.getType(), $T1)){
                                        return ((IBool)$constants.get(7)/*true*/);
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 28290288:
                if(noCaseMatched_$switchVal121){
                    noCaseMatched_$switchVal121 = false;
                    if($isSubtypeOf($switchVal121.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_28290288_1: 
                           do {
                               if($has_type_and_arity($switchVal121, M_ParseTree.Symbol_sort_str, 1)){
                                  IValue $arg0_124 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal121)),0));
                                  if($isComparable($arg0_124.getType(), $T1)){
                                     return ((IBool)$constants.get(7)/*true*/);
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -333228984:
                if(noCaseMatched_$switchVal121){
                    noCaseMatched_$switchVal121 = false;
                    if($isSubtypeOf($switchVal121.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_333228984_7: 
                           do {
                               if($has_type_and_arity($switchVal121, M_ParseTree.Symbol_layouts_str, 1)){
                                  IValue $arg0_132 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal121)),0));
                                  if($isComparable($arg0_132.getType(), $T1)){
                                     return ((IBool)$constants.get(7)/*true*/);
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            default: return ((IBool)$constants.get(6)/*false*/);
        }
        
                   
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(16256,192,<370,0>,<374,1>) 
    public IString lang_rascal_grammar_ParserGenerator_generateParseMethod$c163c6517d408211(IMap $__0, IConstructor p_0){ 
        
        
        final Template $template133 = (Template)new Template($RVF, "public AbstractStackNode<IConstructor>[] ");
        $template133.beginIndent("                                           ");
        $template133.addStr(((IString)($me.sym2name(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_0), "def")))))))).getValue());
        $template133.endIndent("                                           ");
        $template133.addStr("() {\n  return ");
        $template133.beginIndent("         ");
        $template133.addStr(((IString)($me.sym2name(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_0), "def")))))))).getValue());
        $template133.endIndent("         ");
        $template133.addStr(".EXPECTS;\n}");
        return ((IString)($template133.close()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(16450,476,<376,0>,<388,1>) 
    public IString lang_rascal_grammar_ParserGenerator_generateClassConditional$31ecade9ec2e3a26(ISet classes_0){ 
        
        
        if((((IBool)($RVF.bool(((ISet)classes_0).contains(((IConstructor)($RVF.constructor(M_lang_rascal_grammar_Lookahead.Symbol_eoi_, new IValue[]{}, Util.kwpMap())))))))).getValue()){
           IString $reducer145 = (IString)(((IString)$constants.get(25)/*"lookAheadChar == 0"*/));
           /*muExists*/$REDUCER144_GEN16629_CONS_char_class: 
               do {
                   $REDUCER144_GEN16629:
                   for(IValue $elem148_for : ((ISet)classes_0)){
                       IConstructor $elem148 = (IConstructor) $elem148_for;
                       if($has_type_and_arity($elem148, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                          IValue $arg0_149 = (IValue)($aadt_subscript_int(((IConstructor)($elem148)),0));
                          if($isComparable($arg0_149.getType(), $T11)){
                             if(true){
                                IList ranges_2 = ((IList)($arg0_149));
                                $REDUCER144_GEN16629_CONS_char_class_GEN16677:
                                for(IValue $elem147_for : ((IList)($arg0_149))){
                                    IConstructor $elem147 = (IConstructor) $elem147_for;
                                    IConstructor r_3 = ((IConstructor)($elem147));
                                    final Template $template146 = (Template)new Template($RVF, " || ");
                                    $template146.beginIndent("    ");
                                    $template146.addStr(((IString)($me.generateRangeConditional(((IConstructor)r_3)))).getValue());
                                    $template146.endIndent("    ");
                                    $reducer145 = ((IString)($astr_add_astr(((IString)($reducer145)),((IString)($template146.close())))));
                                
                                }
                                continue $REDUCER144_GEN16629;
                                            
                             } else {
                                continue $REDUCER144_GEN16629;
                             }
                          } else {
                             continue $REDUCER144_GEN16629;
                          }
                       } else {
                          continue $REDUCER144_GEN16629;
                       }
                   }
                   
                               
               } while(false);
           return ((IString)($reducer145));
        
        } else {
           final IListWriter $listwriter134 = (IListWriter)$RVF.listWriter();
           /*muExists*/$LCOMP135_GEN16722_CONS_char_class: 
               do {
                   $LCOMP135_GEN16722:
                   for(IValue $elem137_for : ((ISet)classes_0)){
                       IConstructor $elem137 = (IConstructor) $elem137_for;
                       if($has_type_and_arity($elem137, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                          IValue $arg0_138 = (IValue)($aadt_subscript_int(((IConstructor)($elem137)),0));
                          if($isComparable($arg0_138.getType(), $T11)){
                             if(true){
                                IList ranges_5 = null;
                                $LCOMP135_GEN16722_CONS_char_class_GEN16770:
                                for(IValue $elem136_for : ((IList)($arg0_138))){
                                    IConstructor $elem136 = (IConstructor) $elem136_for;
                                    IConstructor r_6 = null;
                                    $listwriter134.append($elem136);
                                
                                }
                                continue $LCOMP135_GEN16722;
                                            
                             } else {
                                continue $LCOMP135_GEN16722;
                             }
                          } else {
                             continue $LCOMP135_GEN16722;
                          }
                       } else {
                          continue $LCOMP135_GEN16722;
                       }
                   }
                   
                               
               } while(false);
           IList ranges_4 = ((IList)($listwriter134.done()));
           final Template $template141 = (Template)new Template($RVF, "");
           $template141.addStr(((IString)($me.generateRangeConditional(((IConstructor)(M_List.head(((IList)ranges_4))))))).getValue());
           IString $reducer140 = (IString)($template141.close());
           $REDUCER139_GEN16900:
           for(IValue $elem143_for : ((IList)(M_List.tail(((IList)ranges_4))))){
               IConstructor $elem143 = (IConstructor) $elem143_for;
               IConstructor r_8 = ((IConstructor)($elem143));
               final Template $template142 = (Template)new Template($RVF, " || ");
               $template142.beginIndent("    ");
               $template142.addStr(((IString)($me.generateRangeConditional(((IConstructor)r_8)))).getValue());
               $template142.endIndent("    ");
               $template142.addStr(" ");
               $reducer140 = ((IString)($astr_add_astr(((IString)($reducer140)),((IString)($template142.close())))));
           
           }
           
                       return ((IString)($reducer140));
        
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(16928,315,<390,0>,<397,1>) 
    public IString lang_rascal_grammar_ParserGenerator_generateRangeConditional$f80249e131b2a443(IConstructor r_0){ 
        
        
        final IConstructor $switchVal150 = ((IConstructor)r_0);
        boolean noCaseMatched_$switchVal150 = true;
        SWITCH46: switch(Fingerprint.getFingerprint($switchVal150)){
        
            case 1732482000:
                if(noCaseMatched_$switchVal150){
                    noCaseMatched_$switchVal150 = false;
                    if($isSubtypeOf($switchVal150.getType(),M_ParseTree.ADT_CharRange)){
                       /*muExists*/CASE_1732482000_0: 
                           do {
                               if($has_type_and_arity($switchVal150, M_ParseTree.CharRange_range_int_int, 2)){
                                  IValue $arg0_153 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal150)),0));
                                  if($isComparable($arg0_153.getType(), $T7)){
                                     if(((IInteger)$constants.get(11)/*0*/).equals($arg0_153)){
                                        IValue $arg1_152 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal150)),1));
                                        if($isComparable($arg1_152.getType(), $T7)){
                                           if(((IInteger)$constants.get(26)/*1048575*/).equals($arg1_152)){
                                              return ((IString)$constants.get(27)/*"(true /*every char*\/)"*/);
                                           
                                           }
                                        
                                        }
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal150.getType(),M_ParseTree.ADT_CharRange)){
                       /*muExists*/CASE_1732482000_1: 
                           do {
                               if($has_type_and_arity($switchVal150, M_ParseTree.CharRange_range_int_int, 2)){
                                  IValue $arg0_156 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal150)),0));
                                  if($isComparable($arg0_156.getType(), $T7)){
                                     IInteger i_1 = null;
                                     IValue $arg1_155 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal150)),1));
                                     if($isComparable($arg1_155.getType(), $T7)){
                                        if(($arg0_156 != null)){
                                           if($arg0_156.match($arg1_155)){
                                              final Template $template154 = (Template)new Template($RVF, "(lookAheadChar == ");
                                              $template154.beginIndent("                  ");
                                              $template154.addVal($arg1_155);
                                              $template154.endIndent("                  ");
                                              $template154.addStr(")");
                                              return ((IString)($template154.close()));
                                           
                                           }
                                        
                                        } else {
                                           $arg0_156 = ((IValue)($arg1_155));
                                           final Template $template154 = (Template)new Template($RVF, "(lookAheadChar == ");
                                           $template154.beginIndent("                  ");
                                           $template154.addVal($arg1_155);
                                           $template154.endIndent("                  ");
                                           $template154.addStr(")");
                                           return ((IString)($template154.close()));
                                        
                                        }
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal150.getType(),M_ParseTree.ADT_CharRange)){
                       /*muExists*/CASE_1732482000_2: 
                           do {
                               if($has_type_and_arity($switchVal150, M_ParseTree.CharRange_range_int_int, 2)){
                                  IValue $arg0_159 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal150)),0));
                                  if($isComparable($arg0_159.getType(), $T7)){
                                     IInteger i_2 = null;
                                     IValue $arg1_158 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal150)),1));
                                     if($isComparable($arg1_158.getType(), $T7)){
                                        IInteger j_3 = null;
                                        final Template $template157 = (Template)new Template($RVF, "((lookAheadChar >= ");
                                        $template157.beginIndent("                    ");
                                        $template157.addVal($arg0_159);
                                        $template157.endIndent("                    ");
                                        $template157.addStr(") && (lookAheadChar <= ");
                                        $template157.beginIndent("                        ");
                                        $template157.addVal($arg1_158);
                                        $template157.endIndent("                        ");
                                        $template157.addStr("))");
                                        return ((IString)($template157.close()));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            default: final Template $template151 = (Template)new Template($RVF, "unexpected range type: ");
                     $template151.beginIndent("                       ");
                     $template151.addVal(r_0);
                     $template151.endIndent("                       ");
                      
                     throw new Throw($template151.close());
        }
        
                   
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(17245,257,<399,0>,<405,1>) 
    public IString lang_rascal_grammar_ParserGenerator_generateSeparatorExpects$ffd9aa0c2930f956(IConstructor grammar_0, IList seps_1){ 
        
        
        if((((IBool)($equal(((IList)seps_1), ((IList)$constants.get(22)/*[]*/))))).getValue()){
           return ((IString)$constants.get(28)/*""*/);
        
        }
        IString $reducer161 = (IString)($atuple_field_project((ITuple)((ITuple)($me.sym2newitem(((IConstructor)grammar_0), ((IConstructor)(M_List.head(((IList)seps_1)))), ((IInteger)$constants.get(0)/*1*/)))), ((IInteger)$constants.get(11)/*0*/)));
        $REDUCER160_GEN17472:
        for(IValue $elem163_for : ((IList)(M_List.index(((IList)(M_List.tail(((IList)seps_1)))))))){
            IInteger $elem163 = (IInteger) $elem163_for;
            IInteger i_3 = ((IInteger)($elem163));
            final Template $template162 = (Template)new Template($RVF, ", ");
            $template162.beginIndent("  ");
            $template162.addStr(((IString)($atuple_field_project((ITuple)((ITuple)($me.sym2newitem(((IConstructor)grammar_0), ((IConstructor)($alist_subscript_int(((IList)seps_1),((IInteger)($aint_add_aint(((IInteger)i_3),((IInteger)$constants.get(0)/*1*/)))).intValue()))), ((IInteger)($aint_add_aint(((IInteger)i_3),((IInteger)$constants.get(29)/*2*/))))))), ((IInteger)$constants.get(11)/*0*/)))).getValue());
            $template162.endIndent("  ");
            $reducer161 = ((IString)($astr_add_astr(((IString)($reducer161)),((IString)($template162.close())))));
        
        }
        
                    return ((IString)($reducer161));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(17504,256,<407,0>,<413,1>) 
    public IString lang_rascal_grammar_ParserGenerator_generateSequenceExpects$6e68a18132b0b513(IConstructor grammar_0, IList seps_1){ 
        
        
        if((((IBool)($equal(((IList)seps_1), ((IList)$constants.get(22)/*[]*/))))).getValue()){
           return ((IString)$constants.get(28)/*""*/);
        
        }
        IString $reducer165 = (IString)($atuple_field_project((ITuple)((ITuple)($me.sym2newitem(((IConstructor)grammar_0), ((IConstructor)(M_List.head(((IList)seps_1)))), ((IInteger)$constants.get(11)/*0*/)))), ((IInteger)$constants.get(11)/*0*/)));
        $REDUCER164_GEN17730:
        for(IValue $elem167_for : ((IList)(M_List.index(((IList)(M_List.tail(((IList)seps_1)))))))){
            IInteger $elem167 = (IInteger) $elem167_for;
            IInteger i_3 = ((IInteger)($elem167));
            final Template $template166 = (Template)new Template($RVF, ", ");
            $template166.beginIndent("  ");
            $template166.addStr(((IString)($atuple_field_project((ITuple)((ITuple)($me.sym2newitem(((IConstructor)grammar_0), ((IConstructor)($alist_subscript_int(((IList)seps_1),((IInteger)($aint_add_aint(((IInteger)i_3),((IInteger)$constants.get(0)/*1*/)))).intValue()))), ((IInteger)($aint_add_aint(((IInteger)i_3),((IInteger)$constants.get(0)/*1*/))))))), ((IInteger)$constants.get(11)/*0*/)))).getValue());
            $template166.endIndent("  ");
            $reducer165 = ((IString)($astr_add_astr(((IString)($reducer165)),((IString)($template166.close())))));
        
        }
        
                    return ((IString)($reducer165));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(17762,249,<415,0>,<421,1>) 
    public IString lang_rascal_grammar_ParserGenerator_generateAltExpects$a62b47034d27d427(IConstructor grammar_0, IList seps_1){ 
        
        
        if((((IBool)($equal(((IList)seps_1), ((IList)$constants.get(22)/*[]*/))))).getValue()){
           return ((IString)$constants.get(28)/*""*/);
        
        }
        IString $reducer169 = (IString)($atuple_field_project((ITuple)((ITuple)($me.sym2newitem(((IConstructor)grammar_0), ((IConstructor)(M_List.head(((IList)seps_1)))), ((IInteger)$constants.get(11)/*0*/)))), ((IInteger)$constants.get(11)/*0*/)));
        $REDUCER168_GEN17981:
        for(IValue $elem171_for : ((IList)(M_List.index(((IList)(M_List.tail(((IList)seps_1)))))))){
            IInteger $elem171 = (IInteger) $elem171_for;
            IInteger i_3 = ((IInteger)($elem171));
            final Template $template170 = (Template)new Template($RVF, ", ");
            $template170.beginIndent("  ");
            $template170.addStr(((IString)($atuple_field_project((ITuple)((ITuple)($me.sym2newitem(((IConstructor)grammar_0), ((IConstructor)($alist_subscript_int(((IList)seps_1),((IInteger)($aint_add_aint(((IInteger)i_3),((IInteger)$constants.get(0)/*1*/)))).intValue()))), ((IInteger)$constants.get(11)/*0*/)))), ((IInteger)$constants.get(11)/*0*/)))).getValue());
            $template170.endIndent("  ");
            $reducer169 = ((IString)($astr_add_astr(((IString)($reducer169)),((IString)($template170.close())))));
        
        }
        
                    return ((IString)($reducer169));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(18013,263,<423,0>,<435,1>) 
    public IString lang_rascal_grammar_ParserGenerator_literals2ints$e5702b42d5b21efa(IList chars_0){ 
        
        
        if((((IBool)($equal(((IList)chars_0), ((IList)$constants.get(22)/*[]*/))))).getValue()){
           return ((IString)$constants.get(28)/*""*/);
        
        }
        final Template $template172 = (Template)new Template($RVF, "");
        $template172.addVal(((IInteger)($aadt_get_field(((IConstructor)(M_List.head(((IList)(((IList)($aadt_get_field(((IConstructor)(M_List.head(((IList)chars_0)))), "ranges")))))))), "begin"))));
        IString result_1 = ((IString)($template172.close()));
        /*muExists*/FOR51: 
            do {
                FOR51_GEN18178:
                for(IValue $elem174_for : ((IList)(M_List.tail(((IList)chars_0))))){
                    IConstructor $elem174 = (IConstructor) $elem174_for;
                    IConstructor ch_2 = null;
                    final Template $template173 = (Template)new Template($RVF, ",");
                    $template173.beginIndent(" ");
                    $template173.addVal(((IInteger)($aadt_get_field(((IConstructor)(M_List.head(((IList)(((IList)($aadt_get_field(((IConstructor)($elem174)), "ranges")))))))), "begin"))));
                    $template173.endIndent(" ");
                    result_1 = ((IString)($astr_add_astr(((IString)result_1),((IString)($template173.close())))));
                
                }
                continue FOR51;
                            
            } while(false);
        /* void:  muCon([]) */return ((IString)result_1);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(18278,440,<437,0>,<454,1>) 
    public IString lang_rascal_grammar_ParserGenerator_ciliterals2ints$ea715e0e62cedd32(IList chars_0){ 
        
        
        if((((IBool)($equal(((IList)chars_0), ((IList)$constants.get(22)/*[]*/))))).getValue()){
           return ((IString)$constants.get(28)/*""*/);
        
        }
        IString result_1 = ((IString)$constants.get(28)/*""*/);
        /*muExists*/FOR53: 
            do {
                /*muExists*/FOR53_GEN18401_CONS_char_class: 
                    do {
                        FOR53_GEN18401:
                        for(IValue $elem189_for : ((IList)chars_0)){
                            IConstructor $elem189 = (IConstructor) $elem189_for;
                            if($has_type_and_arity($elem189, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                               IValue $arg0_190 = (IValue)($aadt_subscript_int(((IConstructor)($elem189)),0));
                               if($isComparable($arg0_190.getType(), $T11)){
                                  IList ranges_2 = null;
                                  final IList $switchVal175 = ((IList)($arg0_190));
                                  boolean noCaseMatched_$switchVal175 = true;
                                  SWITCH54: switch(Fingerprint.getFingerprint($switchVal175)){
                                  
                                      case 3322014:
                                          if(noCaseMatched_$switchVal175){
                                              noCaseMatched_$switchVal175 = false;
                                              if($isSubtypeOf($switchVal175.getType(),$T11)){
                                                 /*muExists*/CASE_3322014_0: 
                                                     do {
                                                         final IList $subject177 = ((IList)($switchVal175));
                                                         int $subject177_cursor = 0;
                                                         if($isSubtypeOf($subject177.getType(),$T11)){
                                                            final int $subject177_len = (int)((IList)($subject177)).length();
                                                            if($subject177_len == 1){
                                                               final IConstructor $subject178 = ((IConstructor)($alist_subscript_int(((IList)($subject177)),$subject177_cursor)));
                                                               if($has_type_and_arity($subject178, M_ParseTree.CharRange_range_int_int, 2)){
                                                                  IValue $arg0_180 = (IValue)($aadt_subscript_int(((IConstructor)($subject178)),0));
                                                                  if($isComparable($arg0_180.getType(), $T7)){
                                                                     IInteger i_3 = null;
                                                                     IValue $arg1_179 = (IValue)($aadt_subscript_int(((IConstructor)($subject178)),1));
                                                                     if($isComparable($arg1_179.getType(), $T7)){
                                                                        IInteger j_4 = null;
                                                                        $subject177_cursor += 1;
                                                                        if($subject177_cursor == $subject177_len){
                                                                           final Template $template176 = (Template)new Template($RVF, "{");
                                                                           $template176.beginIndent(" ");
                                                                           $template176.addVal($arg0_180);
                                                                           $template176.endIndent(" ");
                                                                           $template176.addStr(", ");
                                                                           $template176.beginIndent("  ");
                                                                           $template176.addVal($arg1_179);
                                                                           $template176.endIndent("  ");
                                                                           $template176.addStr("},");
                                                                           result_1 = ((IString)($astr_add_astr(((IString)result_1),((IString)($template176.close())))));
                                                                           break SWITCH54;// succeedSwitch
                                                                        } else {
                                                                           continue CASE_3322014_0;/*list match1*/
                                                                        }
                                                                     }
                                                                  
                                                                  }
                                                               
                                                               }
                                                            
                                                            }
                                                         
                                                         }
                                                 
                                                     } while(false);
                                              
                                              }
                                              if($isSubtypeOf($switchVal175.getType(),$T11)){
                                                 /*muExists*/CASE_3322014_1: 
                                                     do {
                                                         final IList $subject182 = ((IList)($switchVal175));
                                                         int $subject182_cursor = 0;
                                                         if($isSubtypeOf($subject182.getType(),$T11)){
                                                            final int $subject182_len = (int)((IList)($subject182)).length();
                                                            if($subject182_len == 2){
                                                               final IConstructor $subject186 = ((IConstructor)($alist_subscript_int(((IList)($subject182)),$subject182_cursor)));
                                                               if($has_type_and_arity($subject186, M_ParseTree.CharRange_range_int_int, 2)){
                                                                  IValue $arg0_188 = (IValue)($aadt_subscript_int(((IConstructor)($subject186)),0));
                                                                  if($isComparable($arg0_188.getType(), $T7)){
                                                                     IInteger i_5 = null;
                                                                     IValue $arg1_187 = (IValue)($aadt_subscript_int(((IConstructor)($subject186)),1));
                                                                     if($isComparable($arg1_187.getType(), $T7)){
                                                                        if(($arg0_188 != null)){
                                                                           if($arg0_188.match($arg1_187)){
                                                                              $subject182_cursor += 1;
                                                                              final IConstructor $subject183 = ((IConstructor)($alist_subscript_int(((IList)($subject182)),$subject182_cursor)));
                                                                              if($has_type_and_arity($subject183, M_ParseTree.CharRange_range_int_int, 2)){
                                                                                 IValue $arg0_185 = (IValue)($aadt_subscript_int(((IConstructor)($subject183)),0));
                                                                                 if($isComparable($arg0_185.getType(), $T7)){
                                                                                    IInteger j_6 = null;
                                                                                    IValue $arg1_184 = (IValue)($aadt_subscript_int(((IConstructor)($subject183)),1));
                                                                                    if($isComparable($arg1_184.getType(), $T7)){
                                                                                       if(($arg0_185 != null)){
                                                                                          if($arg0_185.match($arg1_184)){
                                                                                             $subject182_cursor += 1;
                                                                                             if($subject182_cursor == $subject182_len){
                                                                                                final Template $template181 = (Template)new Template($RVF, "{");
                                                                                                $template181.beginIndent(" ");
                                                                                                $template181.addVal($arg1_187);
                                                                                                $template181.endIndent(" ");
                                                                                                $template181.addStr(", ");
                                                                                                $template181.beginIndent("  ");
                                                                                                $template181.addVal($arg1_184);
                                                                                                $template181.endIndent("  ");
                                                                                                $template181.addStr("},");
                                                                                                result_1 = ((IString)($astr_add_astr(((IString)result_1),((IString)($template181.close())))));
                                                                                                break SWITCH54;// succeedSwitch
                                                                                             } else {
                                                                                                continue CASE_3322014_1;/*list match1*/
                                                                                             }
                                                                                          }
                                                                                       
                                                                                       } else {
                                                                                          $arg0_185 = ((IValue)($arg1_184));
                                                                                          $subject182_cursor += 1;
                                                                                          if($subject182_cursor == $subject182_len){
                                                                                             final Template $template181 = (Template)new Template($RVF, "{");
                                                                                             $template181.beginIndent(" ");
                                                                                             $template181.addVal($arg1_187);
                                                                                             $template181.endIndent(" ");
                                                                                             $template181.addStr(", ");
                                                                                             $template181.beginIndent("  ");
                                                                                             $template181.addVal($arg1_184);
                                                                                             $template181.endIndent("  ");
                                                                                             $template181.addStr("},");
                                                                                             result_1 = ((IString)($astr_add_astr(((IString)result_1),((IString)($template181.close())))));
                                                                                             break SWITCH54;// succeedSwitch
                                                                                          } else {
                                                                                             continue CASE_3322014_1;/*list match1*/
                                                                                          }
                                                                                       }
                                                                                    }
                                                                                 
                                                                                 }
                                                                              
                                                                              }
                                                                           
                                                                           }
                                                                        
                                                                        } else {
                                                                           $arg0_188 = ((IValue)($arg1_187));
                                                                           $subject182_cursor += 1;
                                                                           final IConstructor $subject183 = ((IConstructor)($alist_subscript_int(((IList)($subject182)),$subject182_cursor)));
                                                                           if($has_type_and_arity($subject183, M_ParseTree.CharRange_range_int_int, 2)){
                                                                              IValue $arg0_185 = (IValue)($aadt_subscript_int(((IConstructor)($subject183)),0));
                                                                              if($isComparable($arg0_185.getType(), $T7)){
                                                                                 IInteger j_6 = null;
                                                                                 IValue $arg1_184 = (IValue)($aadt_subscript_int(((IConstructor)($subject183)),1));
                                                                                 if($isComparable($arg1_184.getType(), $T7)){
                                                                                    if(($arg0_185 != null)){
                                                                                       if($arg0_185.match($arg1_184)){
                                                                                          $subject182_cursor += 1;
                                                                                          if($subject182_cursor == $subject182_len){
                                                                                             final Template $template181 = (Template)new Template($RVF, "{");
                                                                                             $template181.beginIndent(" ");
                                                                                             $template181.addVal($arg1_187);
                                                                                             $template181.endIndent(" ");
                                                                                             $template181.addStr(", ");
                                                                                             $template181.beginIndent("  ");
                                                                                             $template181.addVal($arg1_184);
                                                                                             $template181.endIndent("  ");
                                                                                             $template181.addStr("},");
                                                                                             result_1 = ((IString)($astr_add_astr(((IString)result_1),((IString)($template181.close())))));
                                                                                             break SWITCH54;// succeedSwitch
                                                                                          } else {
                                                                                             continue CASE_3322014_1;/*list match1*/
                                                                                          }
                                                                                       }
                                                                                    
                                                                                    } else {
                                                                                       $arg0_185 = ((IValue)($arg1_184));
                                                                                       $subject182_cursor += 1;
                                                                                       if($subject182_cursor == $subject182_len){
                                                                                          final Template $template181 = (Template)new Template($RVF, "{");
                                                                                          $template181.beginIndent(" ");
                                                                                          $template181.addVal($arg1_187);
                                                                                          $template181.endIndent(" ");
                                                                                          $template181.addStr(", ");
                                                                                          $template181.beginIndent("  ");
                                                                                          $template181.addVal($arg1_184);
                                                                                          $template181.endIndent("  ");
                                                                                          $template181.addStr("},");
                                                                                          result_1 = ((IString)($astr_add_astr(((IString)result_1),((IString)($template181.close())))));
                                                                                          break SWITCH54;// succeedSwitch
                                                                                       } else {
                                                                                          continue CASE_3322014_1;/*list match1*/
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
                                                 
                                                     } while(false);
                                              
                                              }
                                  
                                          }
                                          
                                  
                                      default: 
                                  }
                                  
                                             
                               } else {
                                  continue FOR53_GEN18401;
                               }
                            } else {
                               continue FOR53_GEN18401;
                            }
                        }
                        continue FOR53;
                                    
                    } while(false);
        
            } while(false);
        /* void:  muCon([]) */return ((IString)($astr_slice(((IString)(result_1)), null, null, -1)));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(18720,7439,<456,0>,<561,1>) 
    public ITuple lang_rascal_grammar_ParserGenerator_sym2newitem$10f0cc965395b37c(IConstructor grammar_0, IConstructor sym_1, IInteger dot_2){ 
        
        
        if($has_type_and_arity(sym_1, M_Type.Symbol_label_str_Symbol, 2)){
           IValue $arg0_192 = (IValue)($aadt_subscript_int(((IConstructor)sym_1),0));
           if($isComparable($arg0_192.getType(), $T1)){
              IValue $arg1_191 = (IValue)($aadt_subscript_int(((IConstructor)sym_1),1));
              if($isComparable($arg1_191.getType(), M_ParseTree.ADT_Symbol)){
                 IConstructor sym1_3 = null;
                 sym_1 = ((IConstructor)($arg1_191));
              
              }
           
           }
        
        }
        IInteger itemId_4 = ((IInteger)($getkw_Symbol_id(((IConstructor)sym_1))));
        if((((IBool)($equal(((IInteger)itemId_4),((IInteger)$constants.get(11)/*0*/)).not()))).getValue()){
           /* void:  muCon(true) */
        } else {
           $assert_fails(((IString)$constants.get(28)/*""*/));
        }IList enters_5 = ((IList)$constants.get(22)/*[]*/);
        IList exits_6 = ((IList)$constants.get(22)/*[]*/);
        IString filters_7 = ((IString)$constants.get(28)/*""*/);
        if($has_type_and_arity(sym_1, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
           IValue $arg0_297 = (IValue)($aadt_subscript_int(((IConstructor)sym_1),0));
           if($isComparable($arg0_297.getType(), $T1)){
              IValue $arg1_296 = (IValue)($aadt_subscript_int(((IConstructor)sym_1),1));
              if($isComparable($arg1_296.getType(), $T8)){
                 ISet conds_9 = ((ISet)($arg1_296));
                 conds_9 = ((ISet)(M_lang_rascal_grammar_definition_Keywords.expandKeywords(((IConstructor)grammar_0), ((ISet)conds_9))));
                 final IListWriter $listwriter193 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP194_GEN19201_CONS_follow: 
                     do {
                         $LCOMP194_GEN19201:
                         for(IValue $elem196_for : ((ISet)conds_9)){
                             IConstructor $elem196 = (IConstructor) $elem196_for;
                             if($has_type_and_arity($elem196, M_ParseTree.Condition_follow_Symbol, 1)){
                                IValue $arg0_197 = (IValue)($aadt_subscript_int(((IConstructor)($elem196)),0));
                                if($isComparable($arg0_197.getType(), M_ParseTree.ADT_Symbol)){
                                   if($has_type_and_arity($arg0_197, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                                      IValue $arg0_198 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_197)),0));
                                      if($isComparable($arg0_198.getType(), $T11)){
                                         IList ranges_10 = ((IList)($arg0_198));
                                         final Template $template195 = (Template)new Template($RVF, "new CharFollowRequirement(new int[][]{");
                                         $template195.beginIndent("                                      ");
                                         $template195.addStr(((IString)($me.generateCharClassArrays(((IList)($arg0_198))))).getValue());
                                         $template195.endIndent("                                      ");
                                         $template195.addStr("})");
                                         $listwriter193.append($template195.close());
                                      
                                      } else {
                                         continue $LCOMP194_GEN19201;
                                      }
                                   } else {
                                      continue $LCOMP194_GEN19201;
                                   }
                                } else {
                                   continue $LCOMP194_GEN19201;
                                }
                             } else {
                                continue $LCOMP194_GEN19201;
                             }
                         }
                         
                                     
                     } while(false);
                 exits_6 = ((IList)($alist_add_alist(((IList)exits_6),((IList)($listwriter193.done())))));
                 final IListWriter $listwriter199 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP200_GEN19330_CONS_follow: 
                     do {
                         $LCOMP200_GEN19330:
                         for(IValue $elem202_for : ((ISet)conds_9)){
                             IConstructor $elem202 = (IConstructor) $elem202_for;
                             if($has_type_and_arity($elem202, M_ParseTree.Condition_follow_Symbol, 1)){
                                IValue $arg0_203 = (IValue)($aadt_subscript_int(((IConstructor)($elem202)),0));
                                if($isComparable($arg0_203.getType(), M_ParseTree.ADT_Symbol)){
                                   if($has_type_and_arity($arg0_203, M_ParseTree.Symbol_lit_str, 1)){
                                      IValue $arg0_204 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_203)),0));
                                      if($isComparable($arg0_204.getType(), $T5)){
                                         IString s_11 = ((IString)($arg0_204));
                                         final Template $template201 = (Template)new Template($RVF, "new StringFollowRequirement(new int[] {");
                                         $template201.beginIndent("                                       ");
                                         $template201.addStr(((IString)($me.literals2ints(((IList)(M_lang_rascal_grammar_definition_Literals.str2syms(((IString)($arg0_204)))))))).getValue());
                                         $template201.endIndent("                                       ");
                                         $template201.addStr("})");
                                         $listwriter199.append($template201.close());
                                      
                                      } else {
                                         continue $LCOMP200_GEN19330;
                                      }
                                   } else {
                                      continue $LCOMP200_GEN19330;
                                   }
                                } else {
                                   continue $LCOMP200_GEN19330;
                                }
                             } else {
                                continue $LCOMP200_GEN19330;
                             }
                         }
                         
                                     
                     } while(false);
                 exits_6 = ((IList)($alist_add_alist(((IList)exits_6),((IList)($listwriter199.done())))));
                 final IListWriter $listwriter205 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP206_GEN19467_CONS_follow: 
                     do {
                         $LCOMP206_GEN19467:
                         for(IValue $elem208_for : ((ISet)conds_9)){
                             IConstructor $elem208 = (IConstructor) $elem208_for;
                             if($has_type_and_arity($elem208, M_ParseTree.Condition_follow_Symbol, 1)){
                                IValue $arg0_209 = (IValue)($aadt_subscript_int(((IConstructor)($elem208)),0));
                                if($isComparable($arg0_209.getType(), M_ParseTree.ADT_Symbol)){
                                   if($has_type_and_arity($arg0_209, M_ParseTree.Symbol_cilit_str, 1)){
                                      IValue $arg0_210 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_209)),0));
                                      if($isComparable($arg0_210.getType(), $T5)){
                                         IString s_12 = ((IString)($arg0_210));
                                         final Template $template207 = (Template)new Template($RVF, "new CaseInsensitiveStringFollowRequirement(new int[][]{");
                                         $template207.beginIndent("                                                       ");
                                         $template207.addStr(((IString)($me.ciliterals2ints(((IList)(M_lang_rascal_grammar_definition_Literals.cistr2syms(((IString)($arg0_210)))))))).getValue());
                                         $template207.endIndent("                                                       ");
                                         $template207.addStr("})");
                                         $listwriter205.append($template207.close());
                                      
                                      } else {
                                         continue $LCOMP206_GEN19467;
                                      }
                                   } else {
                                      continue $LCOMP206_GEN19467;
                                   }
                                } else {
                                   continue $LCOMP206_GEN19467;
                                }
                             } else {
                                continue $LCOMP206_GEN19467;
                             }
                         }
                         
                                     
                     } while(false);
                 exits_6 = ((IList)($alist_add_alist(((IList)exits_6),((IList)($listwriter205.done())))));
                 final IListWriter $listwriter211 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP212_GEN19590_CONS_not_follow: 
                     do {
                         $LCOMP212_GEN19590:
                         for(IValue $elem214_for : ((ISet)conds_9)){
                             IConstructor $elem214 = (IConstructor) $elem214_for;
                             if($has_type_and_arity($elem214, M_ParseTree.Condition_not_follow_Symbol, 1)){
                                IValue $arg0_215 = (IValue)($aadt_subscript_int(((IConstructor)($elem214)),0));
                                if($isComparable($arg0_215.getType(), M_ParseTree.ADT_Symbol)){
                                   if($has_type_and_arity($arg0_215, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                                      IValue $arg0_216 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_215)),0));
                                      if($isComparable($arg0_216.getType(), $T11)){
                                         IList ranges_13 = ((IList)($arg0_216));
                                         final Template $template213 = (Template)new Template($RVF, "new CharFollowRestriction(new int[][]{");
                                         $template213.beginIndent("                                      ");
                                         $template213.addStr(((IString)($me.generateCharClassArrays(((IList)($arg0_216))))).getValue());
                                         $template213.endIndent("                                      ");
                                         $template213.addStr("})");
                                         $listwriter211.append($template213.close());
                                      
                                      } else {
                                         continue $LCOMP212_GEN19590;
                                      }
                                   } else {
                                      continue $LCOMP212_GEN19590;
                                   }
                                } else {
                                   continue $LCOMP212_GEN19590;
                                }
                             } else {
                                continue $LCOMP212_GEN19590;
                             }
                         }
                         
                                     
                     } while(false);
                 exits_6 = ((IList)($alist_add_alist(((IList)exits_6),((IList)($listwriter211.done())))));
                 final IListWriter $listwriter217 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP218_GEN19724_CONS_not_follow: 
                     do {
                         $LCOMP218_GEN19724:
                         for(IValue $elem220_for : ((ISet)conds_9)){
                             IConstructor $elem220 = (IConstructor) $elem220_for;
                             if($has_type_and_arity($elem220, M_ParseTree.Condition_not_follow_Symbol, 1)){
                                IValue $arg0_221 = (IValue)($aadt_subscript_int(((IConstructor)($elem220)),0));
                                if($isComparable($arg0_221.getType(), M_ParseTree.ADT_Symbol)){
                                   if($has_type_and_arity($arg0_221, M_ParseTree.Symbol_lit_str, 1)){
                                      IValue $arg0_222 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_221)),0));
                                      if($isComparable($arg0_222.getType(), $T5)){
                                         IString s_14 = ((IString)($arg0_222));
                                         final Template $template219 = (Template)new Template($RVF, "new StringFollowRestriction(new int[] {");
                                         $template219.beginIndent("                                       ");
                                         $template219.addStr(((IString)($me.literals2ints(((IList)(M_lang_rascal_grammar_definition_Literals.str2syms(((IString)($arg0_222)))))))).getValue());
                                         $template219.endIndent("                                       ");
                                         $template219.addStr("})");
                                         $listwriter217.append($template219.close());
                                      
                                      } else {
                                         continue $LCOMP218_GEN19724;
                                      }
                                   } else {
                                      continue $LCOMP218_GEN19724;
                                   }
                                } else {
                                   continue $LCOMP218_GEN19724;
                                }
                             } else {
                                continue $LCOMP218_GEN19724;
                             }
                         }
                         
                                     
                     } while(false);
                 exits_6 = ((IList)($alist_add_alist(((IList)exits_6),((IList)($listwriter217.done())))));
                 final IListWriter $listwriter223 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP224_GEN19865_CONS_not_follow: 
                     do {
                         $LCOMP224_GEN19865:
                         for(IValue $elem226_for : ((ISet)conds_9)){
                             IConstructor $elem226 = (IConstructor) $elem226_for;
                             if($has_type_and_arity($elem226, M_ParseTree.Condition_not_follow_Symbol, 1)){
                                IValue $arg0_227 = (IValue)($aadt_subscript_int(((IConstructor)($elem226)),0));
                                if($isComparable($arg0_227.getType(), M_ParseTree.ADT_Symbol)){
                                   if($has_type_and_arity($arg0_227, M_ParseTree.Symbol_cilit_str, 1)){
                                      IValue $arg0_228 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_227)),0));
                                      if($isComparable($arg0_228.getType(), $T5)){
                                         IString s_15 = ((IString)($arg0_228));
                                         final Template $template225 = (Template)new Template($RVF, "new CaseInsensitiveStringFollowRestriction(new int[][]{");
                                         $template225.beginIndent("                                                       ");
                                         $template225.addStr(((IString)($me.ciliterals2ints(((IList)(M_lang_rascal_grammar_definition_Literals.cistr2syms(((IString)($arg0_228)))))))).getValue());
                                         $template225.endIndent("                                                       ");
                                         $template225.addStr("})");
                                         $listwriter223.append($template225.close());
                                      
                                      } else {
                                         continue $LCOMP224_GEN19865;
                                      }
                                   } else {
                                      continue $LCOMP224_GEN19865;
                                   }
                                } else {
                                   continue $LCOMP224_GEN19865;
                                }
                             } else {
                                continue $LCOMP224_GEN19865;
                             }
                         }
                         
                                     
                     } while(false);
                 exits_6 = ((IList)($alist_add_alist(((IList)exits_6),((IList)($listwriter223.done())))));
                 final IListWriter $listwriter229 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP230_GEN19992_CONS_delete: 
                     do {
                         $LCOMP230_GEN19992:
                         for(IValue $elem232_for : ((ISet)conds_9)){
                             IConstructor $elem232 = (IConstructor) $elem232_for;
                             if($has_type_and_arity($elem232, M_ParseTree.Condition_delete_Symbol, 1)){
                                IValue $arg0_233 = (IValue)($aadt_subscript_int(((IConstructor)($elem232)),0));
                                if($isComparable($arg0_233.getType(), M_ParseTree.ADT_Symbol)){
                                   if($has_type_and_arity($arg0_233, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                                      IValue $arg0_234 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_233)),0));
                                      if($isComparable($arg0_234.getType(), $T11)){
                                         IList ranges_16 = ((IList)($arg0_234));
                                         final Template $template231 = (Template)new Template($RVF, "new CharMatchRestriction(new int[][]{");
                                         $template231.beginIndent("                                     ");
                                         $template231.addStr(((IString)($me.generateCharClassArrays(((IList)($arg0_234))))).getValue());
                                         $template231.endIndent("                                     ");
                                         $template231.addStr("})");
                                         $listwriter229.append($template231.close());
                                      
                                      } else {
                                         continue $LCOMP230_GEN19992;
                                      }
                                   } else {
                                      continue $LCOMP230_GEN19992;
                                   }
                                } else {
                                   continue $LCOMP230_GEN19992;
                                }
                             } else {
                                continue $LCOMP230_GEN19992;
                             }
                         }
                         
                                     
                     } while(false);
                 exits_6 = ((IList)($alist_add_alist(((IList)exits_6),((IList)($listwriter229.done())))));
                 final IListWriter $listwriter235 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP236_GEN20121_CONS_delete: 
                     do {
                         $LCOMP236_GEN20121:
                         for(IValue $elem238_for : ((ISet)conds_9)){
                             IConstructor $elem238 = (IConstructor) $elem238_for;
                             if($has_type_and_arity($elem238, M_ParseTree.Condition_delete_Symbol, 1)){
                                IValue $arg0_239 = (IValue)($aadt_subscript_int(((IConstructor)($elem238)),0));
                                if($isComparable($arg0_239.getType(), M_ParseTree.ADT_Symbol)){
                                   if($has_type_and_arity($arg0_239, M_ParseTree.Symbol_lit_str, 1)){
                                      IValue $arg0_240 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_239)),0));
                                      if($isComparable($arg0_240.getType(), $T5)){
                                         IString s_17 = ((IString)($arg0_240));
                                         final Template $template237 = (Template)new Template($RVF, "new StringMatchRestriction(new int[] {");
                                         $template237.beginIndent("                                      ");
                                         $template237.addStr(((IString)($me.literals2ints(((IList)(M_lang_rascal_grammar_definition_Literals.str2syms(((IString)($arg0_240)))))))).getValue());
                                         $template237.endIndent("                                      ");
                                         $template237.addStr("})");
                                         $listwriter235.append($template237.close());
                                      
                                      } else {
                                         continue $LCOMP236_GEN20121;
                                      }
                                   } else {
                                      continue $LCOMP236_GEN20121;
                                   }
                                } else {
                                   continue $LCOMP236_GEN20121;
                                }
                             } else {
                                continue $LCOMP236_GEN20121;
                             }
                         }
                         
                                     
                     } while(false);
                 exits_6 = ((IList)($alist_add_alist(((IList)exits_6),((IList)($listwriter235.done())))));
                 final IListWriter $listwriter241 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP242_GEN20257_CONS_delete: 
                     do {
                         $LCOMP242_GEN20257:
                         for(IValue $elem244_for : ((ISet)conds_9)){
                             IConstructor $elem244 = (IConstructor) $elem244_for;
                             if($has_type_and_arity($elem244, M_ParseTree.Condition_delete_Symbol, 1)){
                                IValue $arg0_245 = (IValue)($aadt_subscript_int(((IConstructor)($elem244)),0));
                                if($isComparable($arg0_245.getType(), M_ParseTree.ADT_Symbol)){
                                   if($has_type_and_arity($arg0_245, M_ParseTree.Symbol_cilit_str, 1)){
                                      IValue $arg0_246 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_245)),0));
                                      if($isComparable($arg0_246.getType(), $T5)){
                                         IString s_18 = ((IString)($arg0_246));
                                         final Template $template243 = (Template)new Template($RVF, "new CaseInsensitiveStringMatchRestriction(new int[][]{");
                                         $template243.beginIndent("                                                      ");
                                         $template243.addStr(((IString)($me.ciliterals2ints(((IList)(M_lang_rascal_grammar_definition_Literals.cistr2syms(((IString)($arg0_246)))))))).getValue());
                                         $template243.endIndent("                                                      ");
                                         $template243.addStr("})");
                                         $listwriter241.append($template243.close());
                                      
                                      } else {
                                         continue $LCOMP242_GEN20257;
                                      }
                                   } else {
                                      continue $LCOMP242_GEN20257;
                                   }
                                } else {
                                   continue $LCOMP242_GEN20257;
                                }
                             } else {
                                continue $LCOMP242_GEN20257;
                             }
                         }
                         
                                     
                     } while(false);
                 exits_6 = ((IList)($alist_add_alist(((IList)exits_6),((IList)($listwriter241.done())))));
                 final IListWriter $listwriter247 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP248_GEN20335_CONS_end_of_line: 
                     do {
                         $LCOMP248_GEN20335:
                         for(IValue $elem249_for : ((ISet)conds_9)){
                             IConstructor $elem249 = (IConstructor) $elem249_for;
                             if($has_type_and_arity($elem249, M_ParseTree.Condition_end_of_line_, 0)){
                                $listwriter247.append(((IString)$constants.get(30)/*"new AtEndOfLineRequirement()"*/));
                             
                             } else {
                                continue $LCOMP248_GEN20335;
                             }
                         }
                         
                                     
                     } while(false);
                 exits_6 = ((IList)($alist_add_alist(((IList)exits_6),((IList)($listwriter247.done())))));
                 final IListWriter $listwriter250 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP251_GEN20458_CONS_precede: 
                     do {
                         $LCOMP251_GEN20458:
                         for(IValue $elem253_for : ((ISet)conds_9)){
                             IConstructor $elem253 = (IConstructor) $elem253_for;
                             if($has_type_and_arity($elem253, M_ParseTree.Condition_precede_Symbol, 1)){
                                IValue $arg0_254 = (IValue)($aadt_subscript_int(((IConstructor)($elem253)),0));
                                if($isComparable($arg0_254.getType(), M_ParseTree.ADT_Symbol)){
                                   if($has_type_and_arity($arg0_254, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                                      IValue $arg0_255 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_254)),0));
                                      if($isComparable($arg0_255.getType(), $T11)){
                                         IList ranges_19 = ((IList)($arg0_255));
                                         final Template $template252 = (Template)new Template($RVF, "new CharPrecedeRequirement(new int[][]{");
                                         $template252.beginIndent("                                       ");
                                         $template252.addStr(((IString)($me.generateCharClassArrays(((IList)($arg0_255))))).getValue());
                                         $template252.endIndent("                                       ");
                                         $template252.addStr("})");
                                         $listwriter250.append($template252.close());
                                      
                                      } else {
                                         continue $LCOMP251_GEN20458;
                                      }
                                   } else {
                                      continue $LCOMP251_GEN20458;
                                   }
                                } else {
                                   continue $LCOMP251_GEN20458;
                                }
                             } else {
                                continue $LCOMP251_GEN20458;
                             }
                         }
                         
                                     
                     } while(false);
                 enters_5 = ((IList)($alist_add_alist(((IList)enters_5),((IList)($listwriter250.done())))));
                 final IListWriter $listwriter256 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP257_GEN20590_CONS_precede: 
                     do {
                         $LCOMP257_GEN20590:
                         for(IValue $elem259_for : ((ISet)conds_9)){
                             IConstructor $elem259 = (IConstructor) $elem259_for;
                             if($has_type_and_arity($elem259, M_ParseTree.Condition_precede_Symbol, 1)){
                                IValue $arg0_260 = (IValue)($aadt_subscript_int(((IConstructor)($elem259)),0));
                                if($isComparable($arg0_260.getType(), M_ParseTree.ADT_Symbol)){
                                   if($has_type_and_arity($arg0_260, M_ParseTree.Symbol_lit_str, 1)){
                                      IValue $arg0_261 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_260)),0));
                                      if($isComparable($arg0_261.getType(), $T5)){
                                         IString s_20 = ((IString)($arg0_261));
                                         final Template $template258 = (Template)new Template($RVF, "new StringPrecedeRequirement(new int[] {");
                                         $template258.beginIndent("                                        ");
                                         $template258.addStr(((IString)($me.literals2ints(((IList)(M_lang_rascal_grammar_definition_Literals.str2syms(((IString)($arg0_261)))))))).getValue());
                                         $template258.endIndent("                                        ");
                                         $template258.addStr("})");
                                         $listwriter256.append($template258.close());
                                      
                                      } else {
                                         continue $LCOMP257_GEN20590;
                                      }
                                   } else {
                                      continue $LCOMP257_GEN20590;
                                   }
                                } else {
                                   continue $LCOMP257_GEN20590;
                                }
                             } else {
                                continue $LCOMP257_GEN20590;
                             }
                         }
                         
                                     
                     } while(false);
                 enters_5 = ((IList)($alist_add_alist(((IList)enters_5),((IList)($listwriter256.done())))));
                 final IListWriter $listwriter262 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP263_GEN20730_CONS_precede: 
                     do {
                         $LCOMP263_GEN20730:
                         for(IValue $elem265_for : ((ISet)conds_9)){
                             IConstructor $elem265 = (IConstructor) $elem265_for;
                             if($has_type_and_arity($elem265, M_ParseTree.Condition_precede_Symbol, 1)){
                                IValue $arg0_266 = (IValue)($aadt_subscript_int(((IConstructor)($elem265)),0));
                                if($isComparable($arg0_266.getType(), M_ParseTree.ADT_Symbol)){
                                   if($has_type_and_arity($arg0_266, M_ParseTree.Symbol_cilit_str, 1)){
                                      IValue $arg0_267 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_266)),0));
                                      if($isComparable($arg0_267.getType(), $T5)){
                                         IString s_21 = ((IString)($arg0_267));
                                         final Template $template264 = (Template)new Template($RVF, "new CaseInsensitiveStringPrecedeRequirement(new int[][]{");
                                         $template264.beginIndent("                                                        ");
                                         $template264.addStr(((IString)($me.ciliterals2ints(((IList)(M_lang_rascal_grammar_definition_Literals.cistr2syms(((IString)($arg0_267)))))))).getValue());
                                         $template264.endIndent("                                                        ");
                                         $template264.addStr("})");
                                         $listwriter262.append($template264.close());
                                      
                                      } else {
                                         continue $LCOMP263_GEN20730;
                                      }
                                   } else {
                                      continue $LCOMP263_GEN20730;
                                   }
                                } else {
                                   continue $LCOMP263_GEN20730;
                                }
                             } else {
                                continue $LCOMP263_GEN20730;
                             }
                         }
                         
                                     
                     } while(false);
                 enters_5 = ((IList)($alist_add_alist(((IList)enters_5),((IList)($listwriter262.done())))));
                 final IListWriter $listwriter268 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP269_GEN20856_CONS_not_precede: 
                     do {
                         $LCOMP269_GEN20856:
                         for(IValue $elem271_for : ((ISet)conds_9)){
                             IConstructor $elem271 = (IConstructor) $elem271_for;
                             if($has_type_and_arity($elem271, M_ParseTree.Condition_not_precede_Symbol, 1)){
                                IValue $arg0_272 = (IValue)($aadt_subscript_int(((IConstructor)($elem271)),0));
                                if($isComparable($arg0_272.getType(), M_ParseTree.ADT_Symbol)){
                                   if($has_type_and_arity($arg0_272, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                                      IValue $arg0_273 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_272)),0));
                                      if($isComparable($arg0_273.getType(), $T11)){
                                         IList ranges_22 = ((IList)($arg0_273));
                                         final Template $template270 = (Template)new Template($RVF, "new CharPrecedeRestriction(new int[][]{");
                                         $template270.beginIndent("                                       ");
                                         $template270.addStr(((IString)($me.generateCharClassArrays(((IList)($arg0_273))))).getValue());
                                         $template270.endIndent("                                       ");
                                         $template270.addStr("})");
                                         $listwriter268.append($template270.close());
                                      
                                      } else {
                                         continue $LCOMP269_GEN20856;
                                      }
                                   } else {
                                      continue $LCOMP269_GEN20856;
                                   }
                                } else {
                                   continue $LCOMP269_GEN20856;
                                }
                             } else {
                                continue $LCOMP269_GEN20856;
                             }
                         }
                         
                                     
                     } while(false);
                 enters_5 = ((IList)($alist_add_alist(((IList)enters_5),((IList)($listwriter268.done())))));
                 final IListWriter $listwriter274 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP275_GEN20993_CONS_not_precede: 
                     do {
                         $LCOMP275_GEN20993:
                         for(IValue $elem277_for : ((ISet)conds_9)){
                             IConstructor $elem277 = (IConstructor) $elem277_for;
                             if($has_type_and_arity($elem277, M_ParseTree.Condition_not_precede_Symbol, 1)){
                                IValue $arg0_278 = (IValue)($aadt_subscript_int(((IConstructor)($elem277)),0));
                                if($isComparable($arg0_278.getType(), M_ParseTree.ADT_Symbol)){
                                   if($has_type_and_arity($arg0_278, M_ParseTree.Symbol_lit_str, 1)){
                                      IValue $arg0_279 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_278)),0));
                                      if($isComparable($arg0_279.getType(), $T5)){
                                         IString s_23 = ((IString)($arg0_279));
                                         final Template $template276 = (Template)new Template($RVF, "new StringPrecedeRestriction(new int[] {");
                                         $template276.beginIndent("                                        ");
                                         $template276.addStr(((IString)($me.literals2ints(((IList)(M_lang_rascal_grammar_definition_Literals.str2syms(((IString)($arg0_279)))))))).getValue());
                                         $template276.endIndent("                                        ");
                                         $template276.addStr("})");
                                         $listwriter274.append($template276.close());
                                      
                                      } else {
                                         continue $LCOMP275_GEN20993;
                                      }
                                   } else {
                                      continue $LCOMP275_GEN20993;
                                   }
                                } else {
                                   continue $LCOMP275_GEN20993;
                                }
                             } else {
                                continue $LCOMP275_GEN20993;
                             }
                         }
                         
                                     
                     } while(false);
                 enters_5 = ((IList)($alist_add_alist(((IList)enters_5),((IList)($listwriter274.done())))));
                 final IListWriter $listwriter280 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP281_GEN21137_CONS_not_precede: 
                     do {
                         $LCOMP281_GEN21137:
                         for(IValue $elem283_for : ((ISet)conds_9)){
                             IConstructor $elem283 = (IConstructor) $elem283_for;
                             if($has_type_and_arity($elem283, M_ParseTree.Condition_not_precede_Symbol, 1)){
                                IValue $arg0_284 = (IValue)($aadt_subscript_int(((IConstructor)($elem283)),0));
                                if($isComparable($arg0_284.getType(), M_ParseTree.ADT_Symbol)){
                                   if($has_type_and_arity($arg0_284, M_ParseTree.Symbol_cilit_str, 1)){
                                      IValue $arg0_285 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_284)),0));
                                      if($isComparable($arg0_285.getType(), $T5)){
                                         IString s_24 = ((IString)($arg0_285));
                                         final Template $template282 = (Template)new Template($RVF, "new CaseInsensitiveStringPrecedeRestriction(new int[][]{");
                                         $template282.beginIndent("                                                        ");
                                         $template282.addStr(((IString)($me.ciliterals2ints(((IList)(M_lang_rascal_grammar_definition_Literals.cistr2syms(((IString)($arg0_285)))))))).getValue());
                                         $template282.endIndent("                                                        ");
                                         $template282.addStr("})");
                                         $listwriter280.append($template282.close());
                                      
                                      } else {
                                         continue $LCOMP281_GEN21137;
                                      }
                                   } else {
                                      continue $LCOMP281_GEN21137;
                                   }
                                } else {
                                   continue $LCOMP281_GEN21137;
                                }
                             } else {
                                continue $LCOMP281_GEN21137;
                             }
                         }
                         
                                     
                     } while(false);
                 enters_5 = ((IList)($alist_add_alist(((IList)enters_5),((IList)($listwriter280.done())))));
                 final IListWriter $listwriter286 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP287_GEN21222_CONS_at_column: 
                     do {
                         $LCOMP287_GEN21222:
                         for(IValue $elem289_for : ((ISet)conds_9)){
                             IConstructor $elem289 = (IConstructor) $elem289_for;
                             if($has_type_and_arity($elem289, M_ParseTree.Condition_at_column_int, 1)){
                                IValue $arg0_290 = (IValue)($aadt_subscript_int(((IConstructor)($elem289)),0));
                                if($isComparable($arg0_290.getType(), $T7)){
                                   IInteger i_25 = null;
                                   final Template $template288 = (Template)new Template($RVF, "new AtColumnRequirement(");
                                   $template288.beginIndent("                        ");
                                   $template288.addVal($arg0_290);
                                   $template288.endIndent("                        ");
                                   $template288.addStr(")");
                                   $listwriter286.append($template288.close());
                                
                                } else {
                                   continue $LCOMP287_GEN21222;
                                }
                             } else {
                                continue $LCOMP287_GEN21222;
                             }
                         }
                         
                                     
                     } while(false);
                 enters_5 = ((IList)($alist_add_alist(((IList)enters_5),((IList)($listwriter286.done())))));
                 final IListWriter $listwriter291 = (IListWriter)$RVF.listWriter();
                 /*muExists*/$LCOMP292_GEN21303_CONS_begin_of_line: 
                     do {
                         $LCOMP292_GEN21303:
                         for(IValue $elem293_for : ((ISet)conds_9)){
                             IConstructor $elem293 = (IConstructor) $elem293_for;
                             if($has_type_and_arity($elem293, M_ParseTree.Condition_begin_of_line_, 0)){
                                $listwriter291.append(((IString)$constants.get(31)/*"new AtStartOfLineRequirement()"*/));
                             
                             } else {
                                continue $LCOMP292_GEN21303;
                             }
                         }
                         
                                     
                     } while(false);
                 enters_5 = ((IList)($alist_add_alist(((IList)enters_5),((IList)($listwriter291.done())))));
                 sym_1 = ((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)sym_1), "symbol")))));
                 if($has_type_and_arity(sym_1, M_Type.Symbol_label_str_Symbol, 2)){
                    IValue $arg0_295 = (IValue)($aadt_subscript_int(((IConstructor)sym_1),0));
                    if($isComparable($arg0_295.getType(), $T1)){
                       IValue $arg1_294 = (IValue)($aadt_subscript_int(((IConstructor)sym_1),1));
                       if($isComparable($arg1_294.getType(), M_ParseTree.ADT_Symbol)){
                          IConstructor sym1_26 = null;
                          sym_1 = ((IConstructor)($arg1_294));
                       
                       }
                    
                    }
                 
                 }
              
              }
           
           }
        
        }
        filters_7 = ((IString)$constants.get(28)/*""*/);
        if((((IBool)($equal(((IList)enters_5),((IList)$constants.get(22)/*[]*/)).not()))).getValue()){
           final Template $template298 = (Template)new Template($RVF, "new IEnterFilter[] {");
           $template298.beginIndent("                    ");
           $template298.addStr(((IString)(M_List.head(((IList)enters_5)))).getValue());
           $template298.endIndent("                    ");
           ;/*muExists*/LAB59: 
               do {
                   if((((IBool)($equal(((IList)enters_5),((IList)$constants.get(22)/*[]*/)).not()))).getValue()){
                      LAB59_GEN21545:
                      for(IValue $elem299_for : ((IList)(M_List.tail(((IList)enters_5))))){
                          IString $elem299 = (IString) $elem299_for;
                          IString f_27 = null;
                          $template298.addStr(", ");
                          $template298.beginIndent("  ");
                          $template298.addStr(((IString)($elem299)).getValue());
                          $template298.endIndent("  ");
                      
                      }
                      continue LAB59;
                                  
                   } else {
                      continue LAB59;
                   }
               } while(false);
           $template298.addStr("}");
           filters_7 = ((IString)($astr_add_astr(((IString)filters_7),((IString)($template298.close())))));
        
        } else {
           filters_7 = ((IString)($astr_add_astr(((IString)filters_7),((IString)$constants.get(32)/*"null"*/))));
        
        }if((((IBool)($equal(((IList)exits_6),((IList)$constants.get(22)/*[]*/)).not()))).getValue()){
           final Template $template300 = (Template)new Template($RVF, ", new ICompletionFilter[] {");
           $template300.beginIndent("                           ");
           $template300.addStr(((IString)(M_List.head(((IList)exits_6)))).getValue());
           $template300.endIndent("                           ");
           ;/*muExists*/LAB61: 
               do {
                   if((((IBool)($equal(((IList)exits_6),((IList)$constants.get(22)/*[]*/)).not()))).getValue()){
                      LAB61_GEN21721:
                      for(IValue $elem301_for : ((IList)(M_List.tail(((IList)exits_6))))){
                          IString $elem301 = (IString) $elem301_for;
                          IString f_28 = null;
                          $template300.addStr(", ");
                          $template300.beginIndent("  ");
                          $template300.addStr(((IString)($elem301)).getValue());
                          $template300.endIndent("  ");
                      
                      }
                      continue LAB61;
                                  
                   } else {
                      continue LAB61;
                   }
               } while(false);
           $template300.addStr("}");
           filters_7 = ((IString)($astr_add_astr(((IString)filters_7),((IString)($template300.close())))));
        
        } else {
           filters_7 = ((IString)($astr_add_astr(((IString)filters_7),((IString)$constants.get(33)/*", null"*/))));
        
        }final IConstructor $switchVal302 = ((IConstructor)sym_1);
        boolean noCaseMatched_$switchVal302 = true;
        SWITCH62: switch(Fingerprint.getFingerprint($switchVal302)){
        
            case -964239440:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_964239440_14: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_iter_star_seps_Symbol_list_Symbol, 2)){
                                  IValue $arg0_351 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_351.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_40 = ((IConstructor)($arg0_351));
                                     IValue $arg1_350 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),1));
                                     if($isComparable($arg1_350.getType(), $T6)){
                                        IList seps_41 = ((IList)($arg1_350));
                                        IConstructor reg_42 = ((IConstructor)($RVF.constructor(M_ParseTree.Production_regular_Symbol, new IValue[]{((IConstructor)sym_1)})));
                                        final Template $template349 = (Template)new Template($RVF, "new SeparatedListStackNode<IConstructor>(");
                                        $template349.beginIndent("                                           ");
                                        $template349.addVal(itemId_4);
                                        $template349.endIndent("                                           ");
                                        $template349.addStr(", ");
                                        $template349.beginIndent("  ");
                                        $template349.addVal(dot_2);
                                        $template349.endIndent("  ");
                                        $template349.addStr(", ");
                                        $template349.beginIndent("  ");
                                        $template349.addStr(((IString)($me.value2id(((IValue)reg_42)))).getValue());
                                        $template349.endIndent("  ");
                                        $template349.addStr(", ");
                                        $template349.beginIndent("  ");
                                        $template349.addStr(((IString)($atuple_field_project((ITuple)((ITuple)($me.sym2newitem(((IConstructor)grammar_0), ((IConstructor)($arg0_351)), ((IInteger)$constants.get(11)/*0*/)))), ((IInteger)$constants.get(11)/*0*/)))).getValue());
                                        $template349.endIndent("  ");
                                        $template349.addStr(", (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{");
                                        $template349.beginIndent("                                                                ");
                                        $template349.addStr(((IString)($me.generateSeparatorExpects(((IConstructor)grammar_0), ((IList)($arg1_350))))).getValue());
                                        $template349.endIndent("                                                                ");
                                        $template349.addStr("}, false, ");
                                        $template349.beginIndent("          ");
                                        $template349.addStr(((IString)filters_7).getValue());
                                        $template349.endIndent("          ");
                                        $template349.addStr(")");
                                        return ((ITuple)($RVF.tuple(((IString)($template349.close())), ((IInteger)itemId_4))));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 1444258592:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1444258592_5: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_parameterized_sort_str_list_Symbol, 2)){
                                  IValue $arg0_315 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_315.getType(), $T1)){
                                     IValue $arg1_314 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),1));
                                     if($isComparable($arg1_314.getType(), $T1)){
                                        final Template $template313 = (Template)new Template($RVF, "new NonTerminalStackNode<IConstructor>(");
                                        $template313.beginIndent("                                         ");
                                        $template313.addVal(itemId_4);
                                        $template313.endIndent("                                         ");
                                        $template313.addStr(", ");
                                        $template313.beginIndent("  ");
                                        $template313.addVal(dot_2);
                                        $template313.endIndent("  ");
                                        $template313.addStr(", \"");
                                        $template313.beginIndent("    ");
                                        $template313.addStr(((IString)($me.sym2name(((IConstructor)sym_1)))).getValue());
                                        $template313.endIndent("    ");
                                        $template313.addStr("\", ");
                                        $template313.beginIndent("    ");
                                        $template313.addStr(((IString)filters_7).getValue());
                                        $template313.endIndent("    ");
                                        $template313.addStr(")");
                                        return ((ITuple)($RVF.tuple(((IString)($template313.close())), ((IInteger)itemId_4))));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 1206598288:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1206598288_7: 
                           do {
                               if($has_type_and_arity($switchVal302, M_Type.Symbol_parameter_str_Symbol, 2)){
                                  IValue $arg0_321 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_321.getType(), $T1)){
                                     IValue $arg1_320 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),1));
                                     if($isComparable($arg1_320.getType(), $T1)){
                                        final Template $template319 = (Template)new Template($RVF, "All parameters should have been instantiated by now: ");
                                        $template319.beginIndent("                                                     ");
                                        $template319.addVal(sym_1);
                                        $template319.endIndent("                                                     ");
                                        throw new Throw($template319.close());
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 757310344:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_757310344_10: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_cilit_str, 1)){
                                  IValue $arg0_341 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_341.getType(), $T5)){
                                     IString l_32 = ((IString)($arg0_341));
                                     /*muExists*/IF64: 
                                         do {
                                             final IConstructor $subject_val335 = ((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)grammar_0), "rules"))))),((IConstructor)($me.getType(((IConstructor)sym_1)))))));
                                             IF64_DESC23603:
                                             for(IValue $elem336 : new DescendantMatchIterator($subject_val335, 
                                                 new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                                                 if($isComparable($elem336.getType(), M_ParseTree.ADT_Production)){
                                                    if($has_type_and_arity($elem336, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                                       IValue $arg0_339 = (IValue)($subscript_int(((IValue)($elem336)),0));
                                                       if($isComparable($arg0_339.getType(), M_ParseTree.ADT_Symbol)){
                                                          if($has_type_and_arity($arg0_339, M_ParseTree.Symbol_cilit_str, 1)){
                                                             IValue $arg0_340 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_339)),0));
                                                             if($isComparable($arg0_340.getType(), $T5)){
                                                                if(($arg0_341 != null)){
                                                                   if($arg0_341.match($arg0_340)){
                                                                      IValue $arg1_338 = (IValue)($subscript_int(((IValue)($elem336)),1));
                                                                      if($isComparable($arg1_338.getType(), $T6)){
                                                                         if(true){
                                                                            IList chars_34 = ((IList)($arg1_338));
                                                                            IValue $arg2_337 = (IValue)($subscript_int(((IValue)($elem336)),2));
                                                                            if($isComparable($arg2_337.getType(), $T1)){
                                                                               IConstructor p_33 = ((IConstructor)($elem336));
                                                                               final Template $template334 = (Template)new Template($RVF, "new CaseInsensitiveLiteralStackNode<IConstructor>(");
                                                                               $template334.beginIndent("                                                    ");
                                                                               $template334.addVal(itemId_4);
                                                                               $template334.endIndent("                                                    ");
                                                                               $template334.addStr(", ");
                                                                               $template334.beginIndent("  ");
                                                                               $template334.addVal(dot_2);
                                                                               $template334.endIndent("  ");
                                                                               $template334.addStr(", ");
                                                                               $template334.beginIndent("  ");
                                                                               $template334.addStr(((IString)($me.value2id(((IValue)p_33)))).getValue());
                                                                               $template334.endIndent("  ");
                                                                               $template334.addStr(", new int[] {");
                                                                               $template334.beginIndent("             ");
                                                                               $template334.addStr(((IString)($me.literals2ints(((IList)($arg1_338))))).getValue());
                                                                               $template334.endIndent("             ");
                                                                               $template334.addStr("}, ");
                                                                               $template334.beginIndent("   ");
                                                                               $template334.addStr(((IString)filters_7).getValue());
                                                                               $template334.endIndent("   ");
                                                                               $template334.addStr(")");
                                                                               return ((ITuple)($RVF.tuple(((IString)($template334.close())), ((IInteger)itemId_4))));
                                                                            
                                                                            } else {
                                                                               continue IF64_DESC23603;
                                                                            }
                                                                         } else {
                                                                            continue IF64_DESC23603;
                                                                         }
                                                                      } else {
                                                                         continue IF64_DESC23603;
                                                                      }
                                                                   } else {
                                                                      continue IF64_DESC23603;
                                                                   }
                                                                } else {
                                                                   $arg0_341 = ((IValue)($arg0_340));
                                                                   IValue $arg1_338 = (IValue)($subscript_int(((IValue)($elem336)),1));
                                                                   if($isComparable($arg1_338.getType(), $T6)){
                                                                      if(true){
                                                                         IList chars_34 = ((IList)($arg1_338));
                                                                         IValue $arg2_337 = (IValue)($subscript_int(((IValue)($elem336)),2));
                                                                         if($isComparable($arg2_337.getType(), $T1)){
                                                                            IConstructor p_33 = ((IConstructor)($elem336));
                                                                            final Template $template334 = (Template)new Template($RVF, "new CaseInsensitiveLiteralStackNode<IConstructor>(");
                                                                            $template334.beginIndent("                                                    ");
                                                                            $template334.addVal(itemId_4);
                                                                            $template334.endIndent("                                                    ");
                                                                            $template334.addStr(", ");
                                                                            $template334.beginIndent("  ");
                                                                            $template334.addVal(dot_2);
                                                                            $template334.endIndent("  ");
                                                                            $template334.addStr(", ");
                                                                            $template334.beginIndent("  ");
                                                                            $template334.addStr(((IString)($me.value2id(((IValue)p_33)))).getValue());
                                                                            $template334.endIndent("  ");
                                                                            $template334.addStr(", new int[] {");
                                                                            $template334.beginIndent("             ");
                                                                            $template334.addStr(((IString)($me.literals2ints(((IList)($arg1_338))))).getValue());
                                                                            $template334.endIndent("             ");
                                                                            $template334.addStr("}, ");
                                                                            $template334.beginIndent("   ");
                                                                            $template334.addStr(((IString)filters_7).getValue());
                                                                            $template334.endIndent("   ");
                                                                            $template334.addStr(")");
                                                                            return ((ITuple)($RVF.tuple(((IString)($template334.close())), ((IInteger)itemId_4))));
                                                                         
                                                                         } else {
                                                                            continue IF64_DESC23603;
                                                                         }
                                                                      } else {
                                                                         continue IF64_DESC23603;
                                                                      }
                                                                   } else {
                                                                      continue IF64_DESC23603;
                                                                   }
                                                                }
                                                             } else {
                                                                continue IF64_DESC23603;
                                                             }
                                                          } else {
                                                             continue IF64_DESC23603;
                                                          }
                                                       } else {
                                                          continue IF64_DESC23603;
                                                       }
                                                    } else {
                                                       continue IF64_DESC23603;
                                                    }
                                                 } else {
                                                    continue IF64_DESC23603;
                                                 }
                                             }
                                             
                                                          
                                         } while(false);
                                     final Template $template333 = (Template)new Template($RVF, "ci-literal not found in grammar: ");
                                     $template333.beginIndent("                                 ");
                                     $template333.addVal(grammar_0);
                                     $template333.endIndent("                                 ");
                                     throw new Throw($template333.close());
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 856312:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_856312_2: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_lex_str, 1)){
                                  IValue $arg0_308 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_308.getType(), $T1)){
                                     final Template $template307 = (Template)new Template($RVF, "new NonTerminalStackNode<IConstructor>(");
                                     $template307.beginIndent("                                         ");
                                     $template307.addVal(itemId_4);
                                     $template307.endIndent("                                         ");
                                     $template307.addStr(", ");
                                     $template307.beginIndent("  ");
                                     $template307.addVal(dot_2);
                                     $template307.endIndent("  ");
                                     $template307.addStr(", \"");
                                     $template307.beginIndent("    ");
                                     $template307.addStr(((IString)($me.sym2name(((IConstructor)sym_1)))).getValue());
                                     $template307.endIndent("    ");
                                     $template307.addStr("\", ");
                                     $template307.beginIndent("    ");
                                     $template307.addStr(((IString)filters_7).getValue());
                                     $template307.endIndent("    ");
                                     $template307.addStr(")");
                                     return ((ITuple)($RVF.tuple(((IString)($template307.close())), ((IInteger)itemId_4))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 1154855088:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1154855088_6: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_parameterized_lex_str_list_Symbol, 2)){
                                  IValue $arg0_318 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_318.getType(), $T1)){
                                     IValue $arg1_317 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),1));
                                     if($isComparable($arg1_317.getType(), $T1)){
                                        final Template $template316 = (Template)new Template($RVF, "new NonTerminalStackNode<IConstructor>(");
                                        $template316.beginIndent("                                         ");
                                        $template316.addVal(itemId_4);
                                        $template316.endIndent("                                         ");
                                        $template316.addStr(", ");
                                        $template316.beginIndent("  ");
                                        $template316.addVal(dot_2);
                                        $template316.endIndent("  ");
                                        $template316.addStr(", \"");
                                        $template316.beginIndent("    ");
                                        $template316.addStr(((IString)($me.sym2name(((IConstructor)sym_1)))).getValue());
                                        $template316.endIndent("    ");
                                        $template316.addStr("\", ");
                                        $template316.beginIndent("    ");
                                        $template316.addStr(((IString)filters_7).getValue());
                                        $template316.endIndent("    ");
                                        $template316.addStr(")");
                                        return ((ITuple)($RVF.tuple(((IString)($template316.close())), ((IInteger)itemId_4))));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 910072:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_910072_17: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_seq_list_Symbol, 1)){
                                  IValue $arg0_360 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_360.getType(), $T6)){
                                     IList ss_48 = ((IList)($arg0_360));
                                     final Template $template359 = (Template)new Template($RVF, "new SequenceStackNode<IConstructor>(");
                                     $template359.beginIndent("                                      ");
                                     $template359.addVal(itemId_4);
                                     $template359.endIndent("                                      ");
                                     $template359.addStr(", ");
                                     $template359.beginIndent("  ");
                                     $template359.addVal(dot_2);
                                     $template359.endIndent("  ");
                                     $template359.addStr(", ");
                                     $template359.beginIndent("  ");
                                     $template359.addStr(((IString)($me.value2id(((IValue)($RVF.constructor(M_ParseTree.Production_regular_Symbol, new IValue[]{((IConstructor)sym_1)})))))).getValue());
                                     $template359.endIndent("  ");
                                     $template359.addStr(", (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{");
                                     $template359.beginIndent("                                                                ");
                                     $template359.addStr(((IString)($me.generateSequenceExpects(((IConstructor)grammar_0), ((IList)($arg0_360))))).getValue());
                                     $template359.endIndent("                                                                ");
                                     $template359.addStr("}, ");
                                     $template359.beginIndent("   ");
                                     $template359.addStr(((IString)filters_7).getValue());
                                     $template359.endIndent("   ");
                                     $template359.addStr(")");
                                     return ((ITuple)($RVF.tuple(((IString)($template359.close())), ((IInteger)itemId_4))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 826203960:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_826203960_12: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_iter_star_Symbol, 1)){
                                  IValue $arg0_345 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_345.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_36 = ((IConstructor)($arg0_345));
                                     final Template $template344 = (Template)new Template($RVF, "new ListStackNode<IConstructor>(");
                                     $template344.beginIndent("                                  ");
                                     $template344.addVal(itemId_4);
                                     $template344.endIndent("                                  ");
                                     $template344.addStr(", ");
                                     $template344.beginIndent("  ");
                                     $template344.addVal(dot_2);
                                     $template344.endIndent("  ");
                                     $template344.addStr(", ");
                                     $template344.beginIndent("  ");
                                     $template344.addStr(((IString)($me.value2id(((IValue)($RVF.constructor(M_ParseTree.Production_regular_Symbol, new IValue[]{((IConstructor)sym_1)})))))).getValue());
                                     $template344.endIndent("  ");
                                     $template344.addStr(", ");
                                     $template344.beginIndent("  ");
                                     $template344.addStr(((IString)($atuple_field_project((ITuple)((ITuple)($me.sym2newitem(((IConstructor)grammar_0), ((IConstructor)($arg0_345)), ((IInteger)$constants.get(11)/*0*/)))), ((IInteger)$constants.get(11)/*0*/)))).getValue());
                                     $template344.endIndent("  ");
                                     $template344.addStr(", false, ");
                                     $template344.beginIndent("         ");
                                     $template344.addStr(((IString)filters_7).getValue());
                                     $template344.endIndent("         ");
                                     $template344.addStr(")");
                                     return ((ITuple)($RVF.tuple(((IString)($template344.close())), ((IInteger)itemId_4))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 773448:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_773448_16: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_alt_set_Symbol, 1)){
                                  IValue $arg0_358 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_358.getType(), $T0)){
                                     ISet as_45 = ((ISet)($arg0_358));
                                     final IListWriter $listwriter354 = (IListWriter)$RVF.listWriter();
                                     $LCOMP355_GEN25338:
                                     for(IValue $elem356_for : ((ISet)($arg0_358))){
                                         IConstructor $elem356 = (IConstructor) $elem356_for;
                                         IConstructor a_47 = null;
                                         $listwriter354.append($elem356);
                                     
                                     }
                                     
                                                 IList alts_46 = ((IList)($listwriter354.done()));
                                     final Template $template357 = (Template)new Template($RVF, "new AlternativeStackNode<IConstructor>(");
                                     $template357.beginIndent("                                         ");
                                     $template357.addVal(itemId_4);
                                     $template357.endIndent("                                         ");
                                     $template357.addStr(", ");
                                     $template357.beginIndent("  ");
                                     $template357.addVal(dot_2);
                                     $template357.endIndent("  ");
                                     $template357.addStr(", ");
                                     $template357.beginIndent("  ");
                                     $template357.addStr(((IString)($me.value2id(((IValue)($RVF.constructor(M_ParseTree.Production_regular_Symbol, new IValue[]{((IConstructor)sym_1)})))))).getValue());
                                     $template357.endIndent("  ");
                                     $template357.addStr(", (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{");
                                     $template357.beginIndent("                                                                ");
                                     $template357.addStr(((IString)($me.generateAltExpects(((IConstructor)grammar_0), ((IList)alts_46)))).getValue());
                                     $template357.endIndent("                                                                ");
                                     $template357.addStr("}, ");
                                     $template357.beginIndent("   ");
                                     $template357.addStr(((IString)filters_7).getValue());
                                     $template357.endIndent("   ");
                                     $template357.addStr(")");
                                     return ((ITuple)($RVF.tuple(((IString)($template357.close())), ((IInteger)itemId_4))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 1652184736:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1652184736_13: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_iter_seps_Symbol_list_Symbol, 2)){
                                  IValue $arg0_348 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_348.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_37 = ((IConstructor)($arg0_348));
                                     IValue $arg1_347 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),1));
                                     if($isComparable($arg1_347.getType(), $T6)){
                                        IList seps_38 = ((IList)($arg1_347));
                                        IConstructor reg_39 = ((IConstructor)($RVF.constructor(M_ParseTree.Production_regular_Symbol, new IValue[]{((IConstructor)sym_1)})));
                                        final Template $template346 = (Template)new Template($RVF, "new SeparatedListStackNode<IConstructor>(");
                                        $template346.beginIndent("                                           ");
                                        $template346.addVal(itemId_4);
                                        $template346.endIndent("                                           ");
                                        $template346.addStr(", ");
                                        $template346.beginIndent("  ");
                                        $template346.addVal(dot_2);
                                        $template346.endIndent("  ");
                                        $template346.addStr(", ");
                                        $template346.beginIndent("  ");
                                        $template346.addStr(((IString)($me.value2id(((IValue)reg_39)))).getValue());
                                        $template346.endIndent("  ");
                                        $template346.addStr(", ");
                                        $template346.beginIndent("  ");
                                        $template346.addStr(((IString)($atuple_field_project((ITuple)((ITuple)($me.sym2newitem(((IConstructor)grammar_0), ((IConstructor)($arg0_348)), ((IInteger)$constants.get(11)/*0*/)))), ((IInteger)$constants.get(11)/*0*/)))).getValue());
                                        $template346.endIndent("  ");
                                        $template346.addStr(", (AbstractStackNode<IConstructor>[]) new AbstractStackNode[]{");
                                        $template346.beginIndent("                                                                ");
                                        $template346.addStr(((IString)($me.generateSeparatorExpects(((IConstructor)grammar_0), ((IList)($arg1_347))))).getValue());
                                        $template346.endIndent("                                                                ");
                                        $template346.addStr("}, true, ");
                                        $template346.beginIndent("         ");
                                        $template346.addStr(((IString)filters_7).getValue());
                                        $template346.endIndent("         ");
                                        $template346.addStr(")");
                                        return ((ITuple)($RVF.tuple(((IString)($template346.close())), ((IInteger)itemId_4))));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -333228984:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_333228984_4: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_layouts_str, 1)){
                                  IValue $arg0_312 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_312.getType(), $T1)){
                                     final Template $template311 = (Template)new Template($RVF, "new NonTerminalStackNode<IConstructor>(");
                                     $template311.beginIndent("                                         ");
                                     $template311.addVal(itemId_4);
                                     $template311.endIndent("                                         ");
                                     $template311.addStr(", ");
                                     $template311.beginIndent("  ");
                                     $template311.addVal(dot_2);
                                     $template311.endIndent("  ");
                                     $template311.addStr(", \"");
                                     $template311.beginIndent("    ");
                                     $template311.addStr(((IString)($me.sym2name(((IConstructor)sym_1)))).getValue());
                                     $template311.endIndent("    ");
                                     $template311.addStr("\", ");
                                     $template311.beginIndent("    ");
                                     $template311.addStr(((IString)filters_7).getValue());
                                     $template311.endIndent("    ");
                                     $template311.addStr(")");
                                     return ((ITuple)($RVF.tuple(((IString)($template311.close())), ((IInteger)itemId_4))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 0:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    
                }
                
        
            case -1948270072:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1948270072_18: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                                  IValue $arg0_362 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_362.getType(), $T11)){
                                     IList ranges_49 = ((IList)($arg0_362));
                                     final Template $template361 = (Template)new Template($RVF, "new CharStackNode<IConstructor>(");
                                     $template361.beginIndent("                                  ");
                                     $template361.addVal(itemId_4);
                                     $template361.endIndent("                                  ");
                                     $template361.addStr(", ");
                                     $template361.beginIndent("  ");
                                     $template361.addVal(dot_2);
                                     $template361.endIndent("  ");
                                     $template361.addStr(", new int[][]{");
                                     $template361.beginIndent("              ");
                                     $template361.addStr(((IString)($me.generateCharClassArrays(((IList)($arg0_362))))).getValue());
                                     $template361.endIndent("              ");
                                     $template361.addStr("}, ");
                                     $template361.beginIndent("   ");
                                     $template361.addStr(((IString)filters_7).getValue());
                                     $template361.endIndent("   ");
                                     $template361.addStr(")");
                                     return ((ITuple)($RVF.tuple(((IString)($template361.close())), ((IInteger)itemId_4))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 857272:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_857272_9: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_lit_str, 1)){
                                  IValue $arg0_332 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_332.getType(), $T5)){
                                     IString l_29 = ((IString)($arg0_332));
                                     /*muExists*/IF63: 
                                         do {
                                             final IConstructor $subject_val326 = ((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)grammar_0), "rules"))))),((IConstructor)($me.getType(((IConstructor)sym_1)))))));
                                             IF63_DESC23262:
                                             for(IValue $elem327 : new DescendantMatchIterator($subject_val326, 
                                                 new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                                                 if($isComparable($elem327.getType(), M_ParseTree.ADT_Production)){
                                                    if($has_type_and_arity($elem327, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                                       IValue $arg0_330 = (IValue)($subscript_int(((IValue)($elem327)),0));
                                                       if($isComparable($arg0_330.getType(), M_ParseTree.ADT_Symbol)){
                                                          if($has_type_and_arity($arg0_330, M_ParseTree.Symbol_lit_str, 1)){
                                                             IValue $arg0_331 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_330)),0));
                                                             if($isComparable($arg0_331.getType(), $T5)){
                                                                if(($arg0_332 != null)){
                                                                   if($arg0_332.match($arg0_331)){
                                                                      IValue $arg1_329 = (IValue)($subscript_int(((IValue)($elem327)),1));
                                                                      if($isComparable($arg1_329.getType(), $T6)){
                                                                         if(true){
                                                                            IList chars_31 = ((IList)($arg1_329));
                                                                            IValue $arg2_328 = (IValue)($subscript_int(((IValue)($elem327)),2));
                                                                            if($isComparable($arg2_328.getType(), $T1)){
                                                                               IConstructor p_30 = ((IConstructor)($elem327));
                                                                               final Template $template325 = (Template)new Template($RVF, "new LiteralStackNode<IConstructor>(");
                                                                               $template325.beginIndent("                                     ");
                                                                               $template325.addVal(itemId_4);
                                                                               $template325.endIndent("                                     ");
                                                                               $template325.addStr(", ");
                                                                               $template325.beginIndent("  ");
                                                                               $template325.addVal(dot_2);
                                                                               $template325.endIndent("  ");
                                                                               $template325.addStr(", ");
                                                                               $template325.beginIndent("  ");
                                                                               $template325.addStr(((IString)($me.value2id(((IValue)p_30)))).getValue());
                                                                               $template325.endIndent("  ");
                                                                               $template325.addStr(", new int[] {");
                                                                               $template325.beginIndent("             ");
                                                                               $template325.addStr(((IString)($me.literals2ints(((IList)($arg1_329))))).getValue());
                                                                               $template325.endIndent("             ");
                                                                               $template325.addStr("}, ");
                                                                               $template325.beginIndent("   ");
                                                                               $template325.addStr(((IString)filters_7).getValue());
                                                                               $template325.endIndent("   ");
                                                                               $template325.addStr(")");
                                                                               return ((ITuple)($RVF.tuple(((IString)($template325.close())), ((IInteger)itemId_4))));
                                                                            
                                                                            } else {
                                                                               continue IF63_DESC23262;
                                                                            }
                                                                         } else {
                                                                            continue IF63_DESC23262;
                                                                         }
                                                                      } else {
                                                                         continue IF63_DESC23262;
                                                                      }
                                                                   } else {
                                                                      continue IF63_DESC23262;
                                                                   }
                                                                } else {
                                                                   $arg0_332 = ((IValue)($arg0_331));
                                                                   IValue $arg1_329 = (IValue)($subscript_int(((IValue)($elem327)),1));
                                                                   if($isComparable($arg1_329.getType(), $T6)){
                                                                      if(true){
                                                                         IList chars_31 = ((IList)($arg1_329));
                                                                         IValue $arg2_328 = (IValue)($subscript_int(((IValue)($elem327)),2));
                                                                         if($isComparable($arg2_328.getType(), $T1)){
                                                                            IConstructor p_30 = ((IConstructor)($elem327));
                                                                            final Template $template325 = (Template)new Template($RVF, "new LiteralStackNode<IConstructor>(");
                                                                            $template325.beginIndent("                                     ");
                                                                            $template325.addVal(itemId_4);
                                                                            $template325.endIndent("                                     ");
                                                                            $template325.addStr(", ");
                                                                            $template325.beginIndent("  ");
                                                                            $template325.addVal(dot_2);
                                                                            $template325.endIndent("  ");
                                                                            $template325.addStr(", ");
                                                                            $template325.beginIndent("  ");
                                                                            $template325.addStr(((IString)($me.value2id(((IValue)p_30)))).getValue());
                                                                            $template325.endIndent("  ");
                                                                            $template325.addStr(", new int[] {");
                                                                            $template325.beginIndent("             ");
                                                                            $template325.addStr(((IString)($me.literals2ints(((IList)($arg1_329))))).getValue());
                                                                            $template325.endIndent("             ");
                                                                            $template325.addStr("}, ");
                                                                            $template325.beginIndent("   ");
                                                                            $template325.addStr(((IString)filters_7).getValue());
                                                                            $template325.endIndent("   ");
                                                                            $template325.addStr(")");
                                                                            return ((ITuple)($RVF.tuple(((IString)($template325.close())), ((IInteger)itemId_4))));
                                                                         
                                                                         } else {
                                                                            continue IF63_DESC23262;
                                                                         }
                                                                      } else {
                                                                         continue IF63_DESC23262;
                                                                      }
                                                                   } else {
                                                                      continue IF63_DESC23262;
                                                                   }
                                                                }
                                                             } else {
                                                                continue IF63_DESC23262;
                                                             }
                                                          } else {
                                                             continue IF63_DESC23262;
                                                          }
                                                       } else {
                                                          continue IF63_DESC23262;
                                                       }
                                                    } else {
                                                       continue IF63_DESC23262;
                                                    }
                                                 } else {
                                                    continue IF63_DESC23262;
                                                 }
                                             }
                                             
                                                          
                                         } while(false);
                                     final Template $template324 = (Template)new Template($RVF, "literal not found in grammar: ");
                                     $template324.beginIndent("                              ");
                                     $template324.addVal(grammar_0);
                                     $template324.endIndent("                              ");
                                     throw new Throw($template324.close());
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -109773488:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_109773488_3: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_keywords_str, 1)){
                                  IValue $arg0_310 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_310.getType(), $T1)){
                                     final Template $template309 = (Template)new Template($RVF, "new NonTerminalStackNode<IConstructor>(");
                                     $template309.beginIndent("                                         ");
                                     $template309.addVal(itemId_4);
                                     $template309.endIndent("                                         ");
                                     $template309.addStr(", ");
                                     $template309.beginIndent("  ");
                                     $template309.addVal(dot_2);
                                     $template309.endIndent("  ");
                                     $template309.addStr(", \"");
                                     $template309.beginIndent("    ");
                                     $template309.addStr(((IString)($me.sym2name(((IConstructor)sym_1)))).getValue());
                                     $template309.endIndent("    ");
                                     $template309.addStr("\", ");
                                     $template309.beginIndent("    ");
                                     $template309.addStr(((IString)filters_7).getValue());
                                     $template309.endIndent("    ");
                                     $template309.addStr(")");
                                     return ((ITuple)($RVF.tuple(((IString)($template309.close())), ((IInteger)itemId_4))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 25942208:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_25942208_11: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_iter_Symbol, 1)){
                                  IValue $arg0_343 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_343.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_35 = ((IConstructor)($arg0_343));
                                     final Template $template342 = (Template)new Template($RVF, "new ListStackNode<IConstructor>(");
                                     $template342.beginIndent("                                  ");
                                     $template342.addVal(itemId_4);
                                     $template342.endIndent("                                  ");
                                     $template342.addStr(", ");
                                     $template342.beginIndent("  ");
                                     $template342.addVal(dot_2);
                                     $template342.endIndent("  ");
                                     $template342.addStr(", ");
                                     $template342.beginIndent("  ");
                                     $template342.addStr(((IString)($me.value2id(((IValue)($RVF.constructor(M_ParseTree.Production_regular_Symbol, new IValue[]{((IConstructor)sym_1)})))))).getValue());
                                     $template342.endIndent("  ");
                                     $template342.addStr(", ");
                                     $template342.beginIndent("  ");
                                     $template342.addStr(((IString)($atuple_field_project((ITuple)((ITuple)($me.sym2newitem(((IConstructor)grammar_0), ((IConstructor)($arg0_343)), ((IInteger)$constants.get(11)/*0*/)))), ((IInteger)$constants.get(11)/*0*/)))).getValue());
                                     $template342.endIndent("  ");
                                     $template342.addStr(", true, ");
                                     $template342.beginIndent("        ");
                                     $template342.addStr(((IString)filters_7).getValue());
                                     $template342.endIndent("        ");
                                     $template342.addStr(")");
                                     return ((ITuple)($RVF.tuple(((IString)($template342.close())), ((IInteger)itemId_4))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 878060304:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_878060304_8: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_start_Symbol, 1)){
                                  IValue $arg0_323 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_323.getType(), $T1)){
                                     final Template $template322 = (Template)new Template($RVF, "new NonTerminalStackNode<IConstructor>(");
                                     $template322.beginIndent("                                         ");
                                     $template322.addVal(itemId_4);
                                     $template322.endIndent("                                         ");
                                     $template322.addStr(", ");
                                     $template322.beginIndent("  ");
                                     $template322.addVal(dot_2);
                                     $template322.endIndent("  ");
                                     $template322.addStr(", \"");
                                     $template322.beginIndent("    ");
                                     $template322.addStr(((IString)($me.sym2name(((IConstructor)sym_1)))).getValue());
                                     $template322.endIndent("    ");
                                     $template322.addStr("\", ");
                                     $template322.beginIndent("    ");
                                     $template322.addStr(((IString)filters_7).getValue());
                                     $template322.endIndent("    ");
                                     $template322.addStr(")");
                                     return ((ITuple)($RVF.tuple(((IString)($template322.close())), ((IInteger)itemId_4))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 882072:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_882072_15: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_opt_Symbol, 1)){
                                  IValue $arg0_353 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_353.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_43 = ((IConstructor)($arg0_353));
                                     IConstructor reg_44 = ((IConstructor)($RVF.constructor(M_ParseTree.Production_regular_Symbol, new IValue[]{((IConstructor)sym_1)})));
                                     final Template $template352 = (Template)new Template($RVF, "new OptionalStackNode<IConstructor>(");
                                     $template352.beginIndent("                                      ");
                                     $template352.addVal(itemId_4);
                                     $template352.endIndent("                                      ");
                                     $template352.addStr(", ");
                                     $template352.beginIndent("  ");
                                     $template352.addVal(dot_2);
                                     $template352.endIndent("  ");
                                     $template352.addStr(", ");
                                     $template352.beginIndent("  ");
                                     $template352.addStr(((IString)($me.value2id(((IValue)reg_44)))).getValue());
                                     $template352.endIndent("  ");
                                     $template352.addStr(", ");
                                     $template352.beginIndent("  ");
                                     $template352.addStr(((IString)($atuple_field_project((ITuple)((ITuple)($me.sym2newitem(((IConstructor)grammar_0), ((IConstructor)($arg0_353)), ((IInteger)$constants.get(11)/*0*/)))), ((IInteger)$constants.get(11)/*0*/)))).getValue());
                                     $template352.endIndent("  ");
                                     $template352.addStr(", ");
                                     $template352.beginIndent("  ");
                                     $template352.addStr(((IString)filters_7).getValue());
                                     $template352.endIndent("  ");
                                     $template352.addStr(")");
                                     return ((ITuple)($RVF.tuple(((IString)($template352.close())), ((IInteger)itemId_4))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 28290288:
                if(noCaseMatched_$switchVal302){
                    noCaseMatched_$switchVal302 = false;
                    if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_28290288_0: 
                           do {
                               if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_sort_str, 1)){
                                  IValue $arg0_306 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal302)),0));
                                  if($isComparable($arg0_306.getType(), $T1)){
                                     final Template $template305 = (Template)new Template($RVF, "new NonTerminalStackNode<IConstructor>(");
                                     $template305.beginIndent("                                         ");
                                     $template305.addVal(itemId_4);
                                     $template305.endIndent("                                         ");
                                     $template305.addStr(", ");
                                     $template305.beginIndent("  ");
                                     $template305.addVal(dot_2);
                                     $template305.endIndent("  ");
                                     $template305.addStr(", \"");
                                     $template305.beginIndent("    ");
                                     $template305.addStr(((IString)($me.sym2name(((IConstructor)sym_1)))).getValue());
                                     $template305.endIndent("    ");
                                     $template305.addStr("\", ");
                                     $template305.beginIndent("    ");
                                     $template305.addStr(((IString)filters_7).getValue());
                                     $template305.endIndent("    ");
                                     $template305.addStr(")");
                                     return ((ITuple)($RVF.tuple(((IString)($template305.close())), ((IInteger)itemId_4))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            default: if($isSubtypeOf($switchVal302.getType(),M_ParseTree.ADT_Symbol)){
                        /*muExists*/CASE_0_1: 
                            do {
                                if($has_type_and_arity($switchVal302, M_ParseTree.Symbol_empty_, 0)){
                                   final Template $template304 = (Template)new Template($RVF, "new EmptyStackNode<IConstructor>(");
                                   $template304.beginIndent("                                   ");
                                   $template304.addVal(itemId_4);
                                   $template304.endIndent("                                   ");
                                   $template304.addStr(", ");
                                   $template304.beginIndent("  ");
                                   $template304.addVal(dot_2);
                                   $template304.endIndent("  ");
                                   $template304.addStr(", ");
                                   $template304.beginIndent("  ");
                                   $template304.addStr(((IString)($me.value2id(((IValue)($RVF.constructor(M_ParseTree.Production_regular_Symbol, new IValue[]{((IConstructor)sym_1)})))))).getValue());
                                   $template304.endIndent("  ");
                                   $template304.addStr(", ");
                                   $template304.beginIndent("  ");
                                   $template304.addStr(((IString)filters_7).getValue());
                                   $template304.endIndent("  ");
                                   $template304.addStr(")");
                                   return ((ITuple)($RVF.tuple(((IString)($template304.close())), ((IInteger)itemId_4))));
                                
                                }
                        
                            } while(false);
                     
                     }
                     final Template $template303 = (Template)new Template($RVF, "unexpected symbol ");
                     $template303.beginIndent("                  ");
                     $template303.addVal(sym_1);
                     $template303.endIndent("                  ");
                     $template303.addStr(" while generating parser code");
                     throw new Throw($template303.close());
        }
        
                   
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(26161,282,<563,0>,<571,1>) 
    public IString lang_rascal_grammar_ParserGenerator_generateCharClassArrays$701635e3542145f3(IList ranges_0){ 
        
        
        if((((IBool)($equal(((IList)ranges_0), ((IList)$constants.get(22)/*[]*/))))).getValue()){
           return ((IString)$constants.get(28)/*""*/);
        
        }
        IString result_1 = ((IString)$constants.get(28)/*""*/);
        final IConstructor $subject_val364 = ((IConstructor)(M_List.head(((IList)ranges_0))));
        if($has_type_and_arity($subject_val364, M_ParseTree.CharRange_range_int_int, 2)){
           IValue $arg0_366 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val364)),0));
           if($isComparable($arg0_366.getType(), $T7)){
              IInteger from_2 = null;
              IValue $arg1_365 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val364)),1));
              if($isComparable($arg1_365.getType(), $T7)){
                 IInteger to_3 = null;
                 final Template $template363 = (Template)new Template($RVF, "{");
                 $template363.beginIndent(" ");
                 $template363.addVal($arg0_366);
                 $template363.endIndent(" ");
                 $template363.addStr(",");
                 $template363.beginIndent(" ");
                 $template363.addVal($arg1_365);
                 $template363.endIndent(" ");
                 $template363.addStr("}");
                 result_1 = ((IString)($astr_add_astr(((IString)result_1),((IString)($template363.close())))));
              
              }
           
           }
        
        }
        /*muExists*/FOR67: 
            do {
                /*muExists*/FOR67_GEN26354_CONS_range: 
                    do {
                        FOR67_GEN26354:
                        for(IValue $elem368_for : ((IList)(M_List.tail(((IList)ranges_0))))){
                            IConstructor $elem368 = (IConstructor) $elem368_for;
                            if($has_type_and_arity($elem368, M_ParseTree.CharRange_range_int_int, 2)){
                               IValue $arg0_370 = (IValue)($aadt_subscript_int(((IConstructor)($elem368)),0));
                               if($isComparable($arg0_370.getType(), $T7)){
                                  IInteger from_4 = null;
                                  IValue $arg1_369 = (IValue)($aadt_subscript_int(((IConstructor)($elem368)),1));
                                  if($isComparable($arg1_369.getType(), $T7)){
                                     IInteger to_5 = null;
                                     final Template $template367 = (Template)new Template($RVF, ",{");
                                     $template367.beginIndent("  ");
                                     $template367.addVal($arg0_370);
                                     $template367.endIndent("  ");
                                     $template367.addStr(",");
                                     $template367.beginIndent(" ");
                                     $template367.addVal($arg1_369);
                                     $template367.endIndent(" ");
                                     $template367.addStr("}");
                                     result_1 = ((IString)($astr_add_astr(((IString)result_1),((IString)($template367.close())))));
                                  
                                  } else {
                                     continue FOR67_GEN26354;
                                  }
                               } else {
                                  continue FOR67_GEN26354;
                               }
                            } else {
                               continue FOR67_GEN26354;
                            }
                        }
                        continue FOR67;
                                    
                    } while(false);
        
            } while(false);
        /* void:  muCon([]) */return ((IString)result_1);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(26445,50,<573,0>,<575,1>) 
    public IString lang_rascal_grammar_ParserGenerator_esc$b0384bd678427cdc(IConstructor s_0){ 
        
        
        final Template $template371 = (Template)new Template($RVF, "");
        $template371.addVal(s_0);
        return ((IString)($me.esc(((IString)($template371.close())))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(26609,65,<579,0>,<581,1>) 
    public IString lang_rascal_grammar_ParserGenerator_esc$3f747747bc8e51bf(IString s_0){ 
        
        
        return ((IString)(M_String.escape(((IString)s_0), ((IMap)javaStringEscapes))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(26829,63,<585,0>,<587,1>) 
    public IString lang_rascal_grammar_ParserGenerator_escId$1ab026bcf1c9c400(IString s_0){ 
        
        
        return ((IString)(M_String.escape(((IString)s_0), ((IMap)javaIdEscapes))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(26894,179,<589,0>,<595,1>) 
    public IString lang_rascal_grammar_ParserGenerator_sym2name$43e2df165cbb6f65(IConstructor s_0){ 
        
        
        final IConstructor $switchVal372 = ((IConstructor)s_0);
        boolean noCaseMatched_$switchVal372 = true;
        SWITCH68: switch(Fingerprint.getFingerprint($switchVal372)){
        
            case 1643638592:
                if(noCaseMatched_$switchVal372){
                    noCaseMatched_$switchVal372 = false;
                    if($isSubtypeOf($switchVal372.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1643638592_1: 
                           do {
                               if($has_type_and_arity($switchVal372, M_Type.Symbol_label_str_Symbol, 2)){
                                  IValue $arg0_376 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal372)),0));
                                  if($isComparable($arg0_376.getType(), $T1)){
                                     IValue $arg1_375 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal372)),1));
                                     if($isComparable($arg1_375.getType(), M_ParseTree.ADT_Symbol)){
                                        IConstructor x_2 = ((IConstructor)($arg1_375));
                                        return ((IString)($me.sym2name(((IConstructor)($arg1_375)))));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 28290288:
                if(noCaseMatched_$switchVal372){
                    noCaseMatched_$switchVal372 = false;
                    if($isSubtypeOf($switchVal372.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_28290288_0: 
                           do {
                               if($has_type_and_arity($switchVal372, M_ParseTree.Symbol_sort_str, 1)){
                                  IValue $arg0_374 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal372)),0));
                                  if($isComparable($arg0_374.getType(), $T5)){
                                     IString x_1 = null;
                                     final Template $template373 = (Template)new Template($RVF, "");
                                     $template373.addStr(((IString)($arg0_374)).getValue());
                                     return ((IString)($template373.close()));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            default: return ((IString)($me.value2id(((IValue)s_0))));
        }
        
                   
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(27075,55,<597,0>,<600,1>) 
    public IString lang_rascal_grammar_ParserGenerator_value2id$7b72ead30df47401(IValue v_0){ 
        
        
        return ((IString)($me.v2i(((IValue)v_0))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(27132,81,<602,0>,<602,81>) 
    public IString lang_rascal_grammar_ParserGenerator_uu$082b41e3c596335e(IValue s_0){ 
        
        
        final Template $template377 = (Template)new Template($RVF, "");
        $template377.addVal(M_Node.unsetRec(((IValue)s_0)));
        return ((IString)(M_String.escape(((IString)(M_String.toBase64(((IString)($template377.close())), Util.kwpMap()))), ((IMap)$constants.get(34)/*("+":"11","/":"22","=":"00")*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(27215,1021,<604,0>,<622,1>) 
    public IString lang_rascal_grammar_ParserGenerator_v2i$0f91624d0e98f18d(IValue v_0){ 
        
        
        final IValue $switchVal378 = ((IValue)v_0);
        boolean noCaseMatched_$switchVal378 = true;
        SWITCH69: switch(Fingerprint.getFingerprint($switchVal378)){
        
            case 1643638592:
                if(noCaseMatched_$switchVal378){
                    noCaseMatched_$switchVal378 = false;
                    if($isSubtypeOf($switchVal378.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1643638592_2: 
                           do {
                               if($has_type_and_arity($switchVal378, M_Type.Symbol_label_str_Symbol, 2)){
                                  IValue $arg0_395 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),0));
                                  if($isComparable($arg0_395.getType(), $T5)){
                                     IString x_5 = ((IString)($arg0_395));
                                     IValue $arg1_394 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),1));
                                     if($isComparable($arg1_394.getType(), M_ParseTree.ADT_Symbol)){
                                        IConstructor u_6 = ((IConstructor)($arg1_394));
                                        return ((IString)($astr_add_astr(((IString)($astr_add_astr(((IString)($me.escId(((IString)($arg0_395))))),((IString)$constants.get(35)/*"_"*/)))),((IString)($me.v2i(((IValue)($arg1_394))))))));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 1444258592:
                if(noCaseMatched_$switchVal378){
                    noCaseMatched_$switchVal378 = false;
                    if($isSubtypeOf($switchVal378.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1444258592_8: 
                           do {
                               if($has_type_and_arity($switchVal378, M_ParseTree.Symbol_parameterized_sort_str_list_Symbol, 2)){
                                  IValue $arg0_408 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),0));
                                  if($isComparable($arg0_408.getType(), $T5)){
                                     IString s_12 = ((IString)($arg0_408));
                                     IValue $arg1_407 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),1));
                                     if($isComparable($arg1_407.getType(), $T6)){
                                        IList args_13 = ((IList)($arg1_407));
                                        final Template $template406 = (Template)new Template($RVF, "");
                                        $template406.addStr(((IString)($arg0_408)).getValue());
                                        $template406.addStr("_");
                                        $template406.beginIndent(" ");
                                        $template406.addStr(((IString)($me.uu(((IValue)($arg1_407))))).getValue());
                                        $template406.endIndent(" ");
                                        return ((IString)($template406.close()));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 878060304:
                if(noCaseMatched_$switchVal378){
                    noCaseMatched_$switchVal378 = false;
                    if($isSubtypeOf($switchVal378.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_878060304_0: 
                           do {
                               if($has_type_and_arity($switchVal378, M_ParseTree.Symbol_start_Symbol, 1)){
                                  IValue $arg0_387 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),0));
                                  if($isComparable($arg0_387.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_1 = ((IConstructor)($arg0_387));
                                     final Template $template386 = (Template)new Template($RVF, "start__");
                                     $template386.beginIndent("       ");
                                     $template386.addStr(((IString)($me.v2i(((IValue)($arg0_387))))).getValue());
                                     $template386.endIndent("       ");
                                     return ((IString)($template386.close()));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 757310344:
                if(noCaseMatched_$switchVal378){
                    noCaseMatched_$switchVal378 = false;
                    if($isSubtypeOf($switchVal378.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_757310344_10: 
                           do {
                               if($has_type_and_arity($switchVal378, M_ParseTree.Symbol_cilit_str, 1)){
                                  IValue $arg0_413 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),0));
                                  if($isComparable($arg0_413.getType(), $T5)){
                                     final Matcher $matcher414 = (Matcher)$regExpCompile("(^[A-Za-z0-9\\-\\_]+$)", ((IString)($arg0_413)).getValue());
                                     boolean $found415 = true;
                                     
                                         while($found415){
                                             $found415 = $matcher414.find();
                                             if($found415){
                                                IString s_16 = ((IString)($RVF.string($matcher414.group(1))));
                                                final Template $template412 = (Template)new Template($RVF, "cilit_");
                                                $template412.beginIndent("      ");
                                                $template412.addStr(((IString)($me.escId(((IString)s_16)))).getValue());
                                                $template412.endIndent("      ");
                                                return ((IString)($template412.close()));
                                             
                                             }
                                     
                                         }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 856312:
                if(noCaseMatched_$switchVal378){
                    noCaseMatched_$switchVal378 = false;
                    if($isSubtypeOf($switchVal378.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_856312_6: 
                           do {
                               if($has_type_and_arity($switchVal378, M_ParseTree.Symbol_lex_str, 1)){
                                  IValue $arg0_403 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),0));
                                  if($isComparable($arg0_403.getType(), $T5)){
                                     IString s_10 = null;
                                     final Template $template402 = (Template)new Template($RVF, "");
                                     $template402.addStr(((IString)($arg0_403)).getValue());
                                     return ((IString)($template402.close()));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 1154855088:
                if(noCaseMatched_$switchVal378){
                    noCaseMatched_$switchVal378 = false;
                    if($isSubtypeOf($switchVal378.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1154855088_9: 
                           do {
                               if($has_type_and_arity($switchVal378, M_ParseTree.Symbol_parameterized_lex_str_list_Symbol, 2)){
                                  IValue $arg0_411 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),0));
                                  if($isComparable($arg0_411.getType(), $T5)){
                                     IString s_14 = ((IString)($arg0_411));
                                     IValue $arg1_410 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),1));
                                     if($isComparable($arg1_410.getType(), $T6)){
                                        IList args_15 = ((IList)($arg1_410));
                                        final Template $template409 = (Template)new Template($RVF, "");
                                        $template409.addStr(((IString)($arg0_411)).getValue());
                                        $template409.addStr("_");
                                        $template409.beginIndent(" ");
                                        $template409.addStr(((IString)($me.uu(((IValue)($arg1_410))))).getValue());
                                        $template409.endIndent(" ");
                                        return ((IString)($template409.close()));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 28290288:
                if(noCaseMatched_$switchVal378){
                    noCaseMatched_$switchVal378 = false;
                    if($isSubtypeOf($switchVal378.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_28290288_5: 
                           do {
                               if($has_type_and_arity($switchVal378, M_ParseTree.Symbol_sort_str, 1)){
                                  IValue $arg0_401 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),0));
                                  if($isComparable($arg0_401.getType(), $T5)){
                                     IString s_9 = null;
                                     final Template $template400 = (Template)new Template($RVF, "");
                                     $template400.addStr(((IString)($arg0_401)).getValue());
                                     return ((IString)($template400.close()));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 51884336:
                if(noCaseMatched_$switchVal378){
                    noCaseMatched_$switchVal378 = false;
                    if($isSubtypeOf($switchVal378.getType(),M_Grammar.ADT_Item)){
                       /*muExists*/CASE_51884336_1: 
                           do {
                               if($has_type_and_arity($switchVal378, M_Grammar.Item_item_Production_int, 2)){
                                  IValue $arg0_390 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),0));
                                  if($isComparable($arg0_390.getType(), M_ParseTree.ADT_Production)){
                                     if($has_type_and_arity($arg0_390, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                        IValue $arg0_393 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_390)),0));
                                        if($isComparable($arg0_393.getType(), M_ParseTree.ADT_Symbol)){
                                           IConstructor u_3 = ((IConstructor)($arg0_393));
                                           IValue $arg1_392 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_390)),1));
                                           if($isComparable($arg1_392.getType(), $T1)){
                                              IValue $arg2_391 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_390)),2));
                                              if($isComparable($arg2_391.getType(), $T1)){
                                                 IConstructor p_2 = ((IConstructor)($arg0_390));
                                                 IValue $arg1_389 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),1));
                                                 if($isComparable($arg1_389.getType(), $T7)){
                                                    IInteger i_4 = ((IInteger)($arg1_389));
                                                    final Template $template388 = (Template)new Template($RVF, "");
                                                    $template388.addStr(((IString)($me.v2i(((IValue)($arg0_393))))).getValue());
                                                    $template388.addStr(".");
                                                    $template388.beginIndent(" ");
                                                    $template388.addStr(((IString)($me.v2i(((IValue)($arg0_390))))).getValue());
                                                    $template388.endIndent(" ");
                                                    $template388.addStr("_");
                                                    $template388.beginIndent(" ");
                                                    $template388.addStr(((IString)($me.v2i(((IValue)($arg1_389))))).getValue());
                                                    $template388.endIndent(" ");
                                                    return ((IString)($template388.close()));
                                                 
                                                 }
                                              
                                              }
                                           
                                           }
                                        
                                        }
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -333228984:
                if(noCaseMatched_$switchVal378){
                    noCaseMatched_$switchVal378 = false;
                    if($isSubtypeOf($switchVal378.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_333228984_3: 
                           do {
                               if($has_type_and_arity($switchVal378, M_ParseTree.Symbol_layouts_str, 1)){
                                  IValue $arg0_397 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),0));
                                  if($isComparable($arg0_397.getType(), $T5)){
                                     IString x_7 = ((IString)($arg0_397));
                                     final Template $template396 = (Template)new Template($RVF, "layouts_");
                                     $template396.beginIndent("        ");
                                     $template396.addStr(((IString)($me.escId(((IString)($arg0_397))))).getValue());
                                     $template396.endIndent("        ");
                                     return ((IString)($template396.close()));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 0:
                if(noCaseMatched_$switchVal378){
                    noCaseMatched_$switchVal378 = false;
                    
                }
                
        
            case -2144737184:
                if(noCaseMatched_$switchVal378){
                    noCaseMatched_$switchVal378 = false;
                    if($isSubtypeOf($switchVal378.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_2144737184_4: 
                           do {
                               if($has_type_and_arity($switchVal378, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
                                  IValue $arg0_399 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),0));
                                  if($isComparable($arg0_399.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_8 = ((IConstructor)($arg0_399));
                                     IValue $arg1_398 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),1));
                                     if($isComparable($arg1_398.getType(), $T1)){
                                        return ((IString)($me.v2i(((IValue)($arg0_399)))));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 857272:
                if(noCaseMatched_$switchVal378){
                    noCaseMatched_$switchVal378 = false;
                    if($isSubtypeOf($switchVal378.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_857272_11: 
                           do {
                               if($has_type_and_arity($switchVal378, M_ParseTree.Symbol_lit_str, 1)){
                                  IValue $arg0_417 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),0));
                                  if($isComparable($arg0_417.getType(), $T5)){
                                     final Matcher $matcher418 = (Matcher)$regExpCompile("(^[A-Za-z0-9\\-\\_]+$)", ((IString)($arg0_417)).getValue());
                                     boolean $found419 = true;
                                     
                                         while($found419){
                                             $found419 = $matcher418.find();
                                             if($found419){
                                                IString s_17 = ((IString)($RVF.string($matcher418.group(1))));
                                                final Template $template416 = (Template)new Template($RVF, "lit_");
                                                $template416.beginIndent("    ");
                                                $template416.addStr(((IString)($me.escId(((IString)s_17)))).getValue());
                                                $template416.endIndent("    ");
                                                return ((IString)($template416.close()));
                                             
                                             }
                                     
                                         }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -109773488:
                if(noCaseMatched_$switchVal378){
                    noCaseMatched_$switchVal378 = false;
                    if($isSubtypeOf($switchVal378.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_109773488_7: 
                           do {
                               if($has_type_and_arity($switchVal378, M_ParseTree.Symbol_keywords_str, 1)){
                                  IValue $arg0_405 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal378)),0));
                                  if($isComparable($arg0_405.getType(), $T5)){
                                     IString s_11 = null;
                                     final Template $template404 = (Template)new Template($RVF, "");
                                     $template404.addStr(((IString)($arg0_405)).getValue());
                                     return ((IString)($template404.close()));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            default: if($isSubtypeOf($switchVal378.getType(),$T7)){
                        /*muExists*/CASE_0_12: 
                            do {
                                IInteger i_18 = null;
                                /*muExists*/$RET379: 
                                    do {
                                        if((((IBool)($aint_less_aint(((IInteger)($switchVal378)),((IInteger)$constants.get(11)/*0*/))))).getValue()){
                                           final Template $template380 = (Template)new Template($RVF, "min_");
                                           $template380.beginIndent("    ");
                                           $template380.addVal(((IInteger)($switchVal378)).negate());
                                           $template380.endIndent("    ");
                                           return ((IString)($template380.close()));
                                        
                                        }
                                
                                    } while(false);
                                final Template $template381 = (Template)new Template($RVF, "");
                                $template381.addVal($switchVal378);
                                return ((IString)($template381.close()));
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal378.getType(),$T5)){
                        /*muExists*/CASE_0_13: 
                            do {
                                IString s_19 = ((IString)($switchVal378));
                                IString $reducer383 = (IString)(((IString)$constants.get(28)/*""*/));
                                final IInteger $lst2 = ((IInteger)(M_String.size(((IString)s_19))));
                                final boolean $dir3 = ((IInteger)$constants.get(11)/*0*/).less($lst2).getValue();
                                
                                $REDUCER382_GEN28166:
                                for(IInteger $elem385 = ((IInteger)$constants.get(11)/*0*/); $dir3 ? $aint_less_aint($elem385,$lst2).getValue() 
                                                          : $aint_lessequal_aint($elem385,$lst2).not().getValue(); $elem385 = $aint_add_aint($elem385,$dir3 ? ((IInteger)$constants.get(0)/*1*/) : ((IInteger)$constants.get(15)/*-1*/))){
                                    IInteger i_21 = ((IInteger)($elem385));
                                    final Template $template384 = (Template)new Template($RVF, "_");
                                    $template384.beginIndent(" ");
                                    $template384.addVal(M_String.charAt(((IString)s_19), ((IInteger)i_21)));
                                    $template384.endIndent(" ");
                                    $reducer383 = ((IString)($astr_add_astr(((IString)($reducer383)),((IString)($template384.close())))));
                                }
                                
                                return ((IString)($reducer383));
                        
                            } while(false);
                     
                     }
                     return ((IString)($me.uu(((IValue)v_0))));
        
        }
        
                   
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(28355,53,<627,4>,<627,57>) 
    public IInteger lang_rascal_grammar_ParserGenerator_newItem$056db57e92a0e96d(ValueRef<IInteger> uniqueItem_1){ 
        
        
        uniqueItem_1.setValue(((IInteger)($aint_add_aint(uniqueItem_1.getValue(),((IInteger)$constants.get(0)/*1*/)))));
        return uniqueItem_1.getValue();
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(28414,104,<628,4>,<631,8>) 
    public IConstructor lang_rascal_grammar_ParserGenerator_rewrite$3113ca4dfaeecfe9(IConstructor p_0, ValueRef<IInteger> uniqueItem_1){ 
        
        
        try {
            IValue $visitResult = $TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptorAlwaysTrue($RVF.bool(false)),
                 p_0,
                 (IVisitFunction) (IValue $VISIT72_subject, TraversalState $traversalState) -> {
                     VISIT72:switch(Fingerprint.getFingerprint($VISIT72_subject)){
                     
                         case 0:
                             
                     
                         default: 
                             if($isSubtypeOf($VISIT72_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_0_0: 
                                    do {
                                        IConstructor s_2 = ((IConstructor)($VISIT72_subject));
                                        IConstructor $replacement420 = (IConstructor)(((IConstructor)($aadt_field_update("id", lang_rascal_grammar_ParserGenerator_makeUnique$20b231c389f60af1_newItem(uniqueItem_1), ((IConstructor)s_2)))));
                                        if($isSubtypeOf($replacement420.getType(),$VISIT72_subject.getType())){
                                           $traversalState.setMatchedAndChanged(true, true);
                                           return $replacement420;
                                        
                                        } else {
                                           break VISIT72;// switch
                                        
                                        }
                                    } while(false);
                             
                             }
            
                     }
                     return $VISIT72_subject;
                 });
            return (IConstructor)$visitResult;
        
        } catch (ReturnFromTraversalException e) {
            return (IConstructor) e.getValue();
        }
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(28239,354,<625,0>,<634,1>) 
    public IConstructor lang_rascal_grammar_ParserGenerator_makeUnique$20b231c389f60af1(IConstructor gr_0){ 
        
        
        final ValueRef<IInteger> uniqueItem_1 = new ValueRef<IInteger>("uniqueItem", ((IInteger)$constants.get(0)/*1*/));
        final IMapWriter $mapwriter421 = (IMapWriter)$RVF.mapWriter();
        $MCOMP422_GEN28575:
        for(IValue $elem423_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)gr_0), "rules")))))){
            IConstructor $elem423 = (IConstructor) $elem423_for;
            IConstructor s_3 = ((IConstructor)($elem423));
            $mapwriter421.insert($RVF.tuple(s_3, lang_rascal_grammar_ParserGenerator_makeUnique$20b231c389f60af1_rewrite(((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)gr_0), "rules"))))),((IConstructor)s_3)))), uniqueItem_1)));
        
        }
        
                    return ((IConstructor)(((IConstructor)($aadt_field_update("rules", $mapwriter421.done(), ((IConstructor)gr_0))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(0,28593,<1,0>,<634,1>) 
    public IInteger $getkw_Symbol_id(IConstructor $getkw_Symbol_id){ 
        
        
        if($getkw_Symbol_id.asWithKeywordParameters().hasParameter("id")){
           return ((IInteger)(((IInteger)$getkw_Symbol_id.asWithKeywordParameters().getParameter("id"))));
        
        } else {
           return ((IInteger)$constants.get(11)/*0*/);
        
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/ParserGenerator.rsc|(0,28593,<1,0>,<634,1>) 
    public IString $getkw_Symbol_prefix(IConstructor $getkw_Symbol_prefix){ 
        
        
        if($getkw_Symbol_prefix.asWithKeywordParameters().hasParameter("prefix")){
           return ((IString)(((IString)$getkw_Symbol_prefix.asWithKeywordParameters().getParameter("prefix"))));
        
        } else {
           return ((IString)$constants.get(28)/*""*/);
        
        }
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `lang::rascal::grammar::ParserGenerator`");
    }
}