package rascal.lang.rascal.format;
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
public class $Grammar 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.lang.rascal.format.$Grammar_$I {

    private final $Grammar_$I $me;
    private final IList $constants;
    final java.util.Map<java.lang.String,IValue> $kwpDefaults_lang_rascal_format_Grammar_alt2r$755f83a423fa71e2;

    
    public final rascal.$Set M_Set;
    public final rascal.lang.rascal.grammar.definition.$Characters M_lang_rascal_grammar_definition_Characters;
    public final rascal.analysis.graphs.$Graph M_analysis_graphs_Graph;
    public final rascal.$Exception M_Exception;
    public final rascal.lang.rascal.format.$Escape M_lang_rascal_format_Escape;
    public final rascal.analysis.grammars.$Dependency M_analysis_grammars_Dependency;
    public final rascal.$ValueIO M_ValueIO;
    public final rascal.$Type M_Type;
    public final rascal.$Relation M_Relation;
    public final rascal.$List M_List;
    public final rascal.$IO M_IO;
    public final rascal.lang.rascal.grammar.definition.$Literals M_lang_rascal_grammar_definition_Literals;
    public final rascal.$Grammar M_Grammar;
    public final rascal.$Message M_Message;
    public final rascal.$String M_String;
    public final rascal.$ParseTree M_ParseTree;

    
    
    public ISet rascalKeywords;
    public final io.usethesource.vallang.type.Type $T2;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T8;	/*aparameter("T2",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T12;	/*aparameter("T4",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T10;	/*aparameter("T3",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T3;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T31;	/*aint()*/
    public final io.usethesource.vallang.type.Type $T20;	/*aparameter("T",avalue(),closed=false,alabel="b")*/
    public final io.usethesource.vallang.type.Type $T27;	/*avoid()*/
    public final io.usethesource.vallang.type.Type $T6;	/*aparameter("T0",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T16;	/*aloc()*/
    public final io.usethesource.vallang.type.Type $T7;	/*aparameter("T1",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T0;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T19;	/*aparameter("T",avalue(),closed=false,alabel="a")*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T21;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Item;	/*aadt("Item",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarModule;	/*aadt("GrammarModule",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T22;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T11;	/*arel(atypeList([aparameter("T0",avalue(),closed=false),aparameter("T1",avalue(),closed=false),aparameter("T2",avalue(),closed=false),aparameter("T3",avalue(),closed=false),aparameter("T4",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarDefinition;	/*aadt("GrammarDefinition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T13;	/*alist(aadt("CharRange",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostStringChars;	/*aadt("PostStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostStringChars;	/*aadt("PostStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Field;	/*aadt("Field",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Field;	/*aadt("Field",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T18;	/*abool()*/
    public final io.usethesource.vallang.type.Type $T17;	/*afunc(abool(),[aparameter("T",avalue(),closed=false,alabel="a"),aparameter("T",avalue(),closed=false,alabel="b")],[])*/
    public final io.usethesource.vallang.type.Type ADT_LocationLiteral;	/*aadt("LocationLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LocationLiteral;	/*aadt("LocationLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tag;	/*aadt("Tag",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Tag;	/*aadt("Tag",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Type;	/*aadt("Type",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Type;	/*aadt("Type",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TimeZonePart;	/*aadt("TimeZonePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TimeZonePart;	/*aadt("TimeZonePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_HexIntegerLiteral;	/*aadt("HexIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_HexIntegerLiteral;	/*aadt("HexIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ShellCommand;	/*aadt("ShellCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ShellCommand;	/*aadt("ShellCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionBody;	/*aadt("FunctionBody",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionBody;	/*aadt("FunctionBody",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Declaration;	/*aadt("Declaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Declaration;	/*aadt("Declaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T4;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T14;	/*arel(atypeList([aparameter("T0",avalue(),closed=false),aparameter("T1",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Associativity;	/*aadt("Associativity",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T23;	/*aset(aadt("Production",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Start;	/*aadt("Start",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Start;	/*aadt("Start",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Body;	/*aadt("Body",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Body;	/*aadt("Body",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PrePathChars;	/*aadt("PrePathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PrePathChars;	/*aadt("PrePathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T29;	/*aset(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T15;	/*alist(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T25;	/*aset(aadt("Attr",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T9;	/*arel(atypeList([aparameter("T0",avalue(),closed=false),aparameter("T1",avalue(),closed=false),aparameter("T2",avalue(),closed=false),aparameter("T3",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type ADT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUTLIST;	/*aadt("LAYOUTLIST",[],layoutSyntax())*/
    public final io.usethesource.vallang.type.Type $T24;	/*alist(aadt("Production",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringTail;	/*aadt("StringTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringTail;	/*aadt("StringTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Backslash;	/*aadt("Backslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Backslash;	/*aadt("Backslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RascalKeywords;	/*aadt("RascalKeywords",[],keywordSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T1;	/*aset(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_StringTemplate;	/*aadt("StringTemplate",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringTemplate;	/*aadt("StringTemplate",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OctalIntegerLiteral;	/*aadt("OctalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OctalIntegerLiteral;	/*aadt("OctalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolPart;	/*aadt("ProtocolPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolPart;	/*aadt("ProtocolPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedBackslash;	/*aadt("NamedBackslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedBackslash;	/*aadt("NamedBackslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T28;	/*anode([])*/
    public final io.usethesource.vallang.type.Type ADT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Grammar;	/*aadt("Grammar",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T5;	/*arel(atypeList([aparameter("T0",avalue(),closed=false),aparameter("T1",avalue(),closed=false),aparameter("T2",avalue(),closed=false)]))*/
    public final io.usethesource.vallang.type.Type ADT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T30;	/*aset(aadt("Condition",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comment;	/*aadt("Comment",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comment;	/*aadt("Comment",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Commands;	/*aadt("Commands",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Commands;	/*aadt("Commands",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visit;	/*aadt("Visit",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visit;	/*aadt("Visit",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StructuredType;	/*aadt("StructuredType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StructuredType;	/*aadt("StructuredType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T26;	/*aset(avoid())*/
    public final io.usethesource.vallang.type.Type ADT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Command;	/*aadt("Command",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Command;	/*aadt("Command",[],contextFreeSyntax())*/

    public $Grammar(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Grammar(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Grammar_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.lang.rascal.format.$Grammar.class, this);
       
       mstore.importModule(rascal.$Set.class, rex, rascal.$Set::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Characters.class, rex, rascal.lang.rascal.grammar.definition.$Characters::new);
       mstore.importModule(rascal.analysis.graphs.$Graph.class, rex, rascal.analysis.graphs.$Graph::new);
       mstore.importModule(rascal.$Exception.class, rex, rascal.$Exception::new);
       mstore.importModule(rascal.lang.rascal.format.$Escape.class, rex, rascal.lang.rascal.format.$Escape::new);
       mstore.importModule(rascal.analysis.grammars.$Dependency.class, rex, rascal.analysis.grammars.$Dependency::new);
       mstore.importModule(rascal.$ValueIO.class, rex, rascal.$ValueIO::new);
       mstore.importModule(rascal.$Type.class, rex, rascal.$Type::new);
       mstore.importModule(rascal.$Relation.class, rex, rascal.$Relation::new);
       mstore.importModule(rascal.$List.class, rex, rascal.$List::new);
       mstore.importModule(rascal.$IO.class, rex, rascal.$IO::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Literals.class, rex, rascal.lang.rascal.grammar.definition.$Literals::new);
       mstore.importModule(rascal.$Grammar.class, rex, rascal.$Grammar::new);
       mstore.importModule(rascal.$Message.class, rex, rascal.$Message::new);
       mstore.importModule(rascal.$String.class, rex, rascal.$String::new);
       mstore.importModule(rascal.$ParseTree.class, rex, rascal.$ParseTree::new); 
       
       M_Set = mstore.getModule(rascal.$Set.class);
       M_lang_rascal_grammar_definition_Characters = mstore.getModule(rascal.lang.rascal.grammar.definition.$Characters.class);
       M_analysis_graphs_Graph = mstore.getModule(rascal.analysis.graphs.$Graph.class);
       M_Exception = mstore.getModule(rascal.$Exception.class);
       M_lang_rascal_format_Escape = mstore.getModule(rascal.lang.rascal.format.$Escape.class);
       M_analysis_grammars_Dependency = mstore.getModule(rascal.analysis.grammars.$Dependency.class);
       M_ValueIO = mstore.getModule(rascal.$ValueIO.class);
       M_Type = mstore.getModule(rascal.$Type.class);
       M_Relation = mstore.getModule(rascal.$Relation.class);
       M_List = mstore.getModule(rascal.$List.class);
       M_IO = mstore.getModule(rascal.$IO.class);
       M_lang_rascal_grammar_definition_Literals = mstore.getModule(rascal.lang.rascal.grammar.definition.$Literals.class);
       M_Grammar = mstore.getModule(rascal.$Grammar.class);
       M_Message = mstore.getModule(rascal.$Message.class);
       M_String = mstore.getModule(rascal.$String.class);
       M_ParseTree = mstore.getModule(rascal.$ParseTree.class); 
       
                          
       
       $TS.importStore(M_Set.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Characters.$TS);
       $TS.importStore(M_analysis_graphs_Graph.$TS);
       $TS.importStore(M_Exception.$TS);
       $TS.importStore(M_lang_rascal_format_Escape.$TS);
       $TS.importStore(M_analysis_grammars_Dependency.$TS);
       $TS.importStore(M_ValueIO.$TS);
       $TS.importStore(M_Type.$TS);
       $TS.importStore(M_Relation.$TS);
       $TS.importStore(M_List.$TS);
       $TS.importStore(M_IO.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Literals.$TS);
       $TS.importStore(M_Grammar.$TS);
       $TS.importStore(M_Message.$TS);
       $TS.importStore(M_String.$TS);
       $TS.importStore(M_ParseTree.$TS);
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/lang/rascal/format/$Grammar.constants", 57, "47ade45c3a75c5387a0d31ddfc1085d7");
       NT_ProtocolChars = $lex("ProtocolChars");
       ADT_ProtocolChars = $adt("ProtocolChars");
       NT_Replacement = $sort("Replacement");
       ADT_Replacement = $adt("Replacement");
       NT_Visibility = $sort("Visibility");
       ADT_Visibility = $adt("Visibility");
       NT_PostProtocolChars = $lex("PostProtocolChars");
       ADT_PostProtocolChars = $adt("PostProtocolChars");
       ADT_Attr = $adt("Attr");
       NT_Expression = $sort("Expression");
       ADT_Expression = $adt("Expression");
       NT_Strategy = $sort("Strategy");
       ADT_Strategy = $adt("Strategy");
       ADT_Tree = $adt("Tree");
       NT_MidStringChars = $lex("MidStringChars");
       ADT_MidStringChars = $adt("MidStringChars");
       NT_TagString = $lex("TagString");
       ADT_TagString = $adt("TagString");
       NT_Name = $lex("Name");
       ADT_Name = $adt("Name");
       NT_NonterminalLabel = $lex("NonterminalLabel");
       ADT_NonterminalLabel = $adt("NonterminalLabel");
       NT_TypeArg = $sort("TypeArg");
       ADT_TypeArg = $adt("TypeArg");
       ADT_IOCapability = $adt("IOCapability");
       NT_Range = $sort("Range");
       ADT_Range = $adt("Range");
       NT_Pattern = $sort("Pattern");
       ADT_Pattern = $adt("Pattern");
       NT_FunctionType = $sort("FunctionType");
       ADT_FunctionType = $adt("FunctionType");
       ADT_LocationChangeType = $adt("LocationChangeType");
       NT_Variable = $sort("Variable");
       ADT_Variable = $adt("Variable");
       NT_Concrete = $lex("Concrete");
       ADT_Concrete = $adt("Concrete");
       ADT_Item = $adt("Item");
       NT_RationalLiteral = $lex("RationalLiteral");
       ADT_RationalLiteral = $adt("RationalLiteral");
       NT_RegExpLiteral = $lex("RegExpLiteral");
       ADT_RegExpLiteral = $adt("RegExpLiteral");
       NT_Declarator = $sort("Declarator");
       ADT_Declarator = $adt("Declarator");
       ADT_GrammarModule = $adt("GrammarModule");
       NT_IntegerLiteral = $sort("IntegerLiteral");
       ADT_IntegerLiteral = $adt("IntegerLiteral");
       NT_Prod = $sort("Prod");
       ADT_Prod = $adt("Prod");
       NT_ModuleParameters = $sort("ModuleParameters");
       ADT_ModuleParameters = $adt("ModuleParameters");
       NT_Output = $lex("Output");
       ADT_Output = $adt("Output");
       NT_SyntaxDefinition = $sort("SyntaxDefinition");
       ADT_SyntaxDefinition = $adt("SyntaxDefinition");
       NT_ImportedModule = $sort("ImportedModule");
       ADT_ImportedModule = $adt("ImportedModule");
       NT_Case = $sort("Case");
       ADT_Case = $adt("Case");
       NT_LocalVariableDeclaration = $sort("LocalVariableDeclaration");
       ADT_LocalVariableDeclaration = $adt("LocalVariableDeclaration");
       NT_BooleanLiteral = $lex("BooleanLiteral");
       ADT_BooleanLiteral = $adt("BooleanLiteral");
       NT_LAYOUT = $lex("LAYOUT");
       ADT_LAYOUT = $adt("LAYOUT");
       NT_Target = $sort("Target");
       ADT_Target = $adt("Target");
       NT_DatePart = $lex("DatePart");
       ADT_DatePart = $adt("DatePart");
       NT_TimePartNoTZ = $lex("TimePartNoTZ");
       ADT_TimePartNoTZ = $adt("TimePartNoTZ");
       NT_KeywordFormals = $sort("KeywordFormals");
       ADT_KeywordFormals = $adt("KeywordFormals");
       NT_DataTarget = $sort("DataTarget");
       ADT_DataTarget = $adt("DataTarget");
       NT_Renaming = $sort("Renaming");
       ADT_Renaming = $adt("Renaming");
       NT_Catch = $sort("Catch");
       ADT_Catch = $adt("Catch");
       ADT_Production = $adt("Production");
       NT_OptionalExpression = $sort("OptionalExpression");
       ADT_OptionalExpression = $adt("OptionalExpression");
       ADT_GrammarDefinition = $adt("GrammarDefinition");
       ADT_CharRange = $adt("CharRange");
       NT_PostStringChars = $lex("PostStringChars");
       ADT_PostStringChars = $adt("PostStringChars");
       NT_Field = $sort("Field");
       ADT_Field = $adt("Field");
       NT_LocationLiteral = $sort("LocationLiteral");
       ADT_LocationLiteral = $adt("LocationLiteral");
       NT_Tag = $sort("Tag");
       ADT_Tag = $adt("Tag");
       NT_Type = $sort("Type");
       ADT_Type = $adt("Type");
       NT_TimeZonePart = $lex("TimeZonePart");
       ADT_TimeZonePart = $adt("TimeZonePart");
       NT_HexIntegerLiteral = $lex("HexIntegerLiteral");
       ADT_HexIntegerLiteral = $adt("HexIntegerLiteral");
       NT_ShellCommand = $sort("ShellCommand");
       ADT_ShellCommand = $adt("ShellCommand");
       NT_FunctionBody = $sort("FunctionBody");
       ADT_FunctionBody = $adt("FunctionBody");
       NT_Declaration = $sort("Declaration");
       ADT_Declaration = $adt("Declaration");
       ADT_Symbol = $adt("Symbol");
       NT_Nonterminal = $lex("Nonterminal");
       ADT_Nonterminal = $adt("Nonterminal");
       ADT_Message = $adt("Message");
       NT_PreStringChars = $lex("PreStringChars");
       ADT_PreStringChars = $adt("PreStringChars");
       NT_StringLiteral = $sort("StringLiteral");
       ADT_StringLiteral = $adt("StringLiteral");
       NT_RegExp = $lex("RegExp");
       ADT_RegExp = $adt("RegExp");
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
       NT_Tags = $sort("Tags");
       ADT_Tags = $adt("Tags");
       NT_ConcreteHole = $sort("ConcreteHole");
       ADT_ConcreteHole = $adt("ConcreteHole");
       NT_ModuleActuals = $sort("ModuleActuals");
       ADT_ModuleActuals = $adt("ModuleActuals");
       ADT_Condition = $adt("Condition");
       NT_URLChars = $lex("URLChars");
       ADT_URLChars = $adt("URLChars");
       NT_Renamings = $sort("Renamings");
       ADT_Renamings = $adt("Renamings");
       NT_FunctionDeclaration = $sort("FunctionDeclaration");
       ADT_FunctionDeclaration = $adt("FunctionDeclaration");
       NT_TypeVar = $sort("TypeVar");
       ADT_TypeVar = $adt("TypeVar");
       ADT_LocationType = $adt("LocationType");
       NT_JustDate = $lex("JustDate");
       ADT_JustDate = $adt("JustDate");
       NT_Start = $sort("Start");
       ADT_Start = $adt("Start");
       NT_Body = $sort("Body");
       ADT_Body = $adt("Body");
       NT_PrePathChars = $lex("PrePathChars");
       ADT_PrePathChars = $adt("PrePathChars");
       NT_StringConstant = $lex("StringConstant");
       ADT_StringConstant = $adt("StringConstant");
       NT_OptionalComma = $lex("OptionalComma");
       ADT_OptionalComma = $adt("OptionalComma");
       NT_Header = $sort("Header");
       ADT_Header = $adt("Header");
       NT_Statement = $sort("Statement");
       ADT_Statement = $adt("Statement");
       NT_StringCharacter = $lex("StringCharacter");
       ADT_StringCharacter = $adt("StringCharacter");
       NT_Variant = $sort("Variant");
       ADT_Variant = $adt("Variant");
       NT_ConcretePart = $lex("ConcretePart");
       ADT_ConcretePart = $adt("ConcretePart");
       NT_Literal = $sort("Literal");
       ADT_Literal = $adt("Literal");
       ADT_Exception = $adt("Exception");
       NT_Assignment = $sort("Assignment");
       ADT_Assignment = $adt("Assignment");
       NT_Module = $sort("Module");
       ADT_Module = $adt("Module");
       NT_PatternWithAction = $sort("PatternWithAction");
       ADT_PatternWithAction = $adt("PatternWithAction");
       NT_Toplevel = $sort("Toplevel");
       ADT_Toplevel = $adt("Toplevel");
       NT_Sym = $sort("Sym");
       ADT_Sym = $adt("Sym");
       NT_MidPathChars = $lex("MidPathChars");
       ADT_MidPathChars = $adt("MidPathChars");
       NT_Import = $sort("Import");
       ADT_Import = $adt("Import");
       NT_Comprehension = $sort("Comprehension");
       ADT_Comprehension = $adt("Comprehension");
       NT_FunctionModifiers = $sort("FunctionModifiers");
       ADT_FunctionModifiers = $adt("FunctionModifiers");
       NT_QualifiedName = $sort("QualifiedName");
       ADT_QualifiedName = $adt("QualifiedName");
       NT_Formals = $sort("Formals");
       ADT_Formals = $adt("Formals");
       NT_StringMiddle = $sort("StringMiddle");
       ADT_StringMiddle = $adt("StringMiddle");
       NT_Char = $lex("Char");
       ADT_Char = $adt("Char");
       NT_CaseInsensitiveStringConstant = $lex("CaseInsensitiveStringConstant");
       ADT_CaseInsensitiveStringConstant = $adt("CaseInsensitiveStringConstant");
       ADT_LAYOUTLIST = $layouts("LAYOUTLIST");
       NT_RealLiteral = $lex("RealLiteral");
       ADT_RealLiteral = $adt("RealLiteral");
       NT_DateAndTime = $lex("DateAndTime");
       ADT_DateAndTime = $adt("DateAndTime");
       NT_StringTail = $sort("StringTail");
       ADT_StringTail = $adt("StringTail");
       NT_Backslash = $lex("Backslash");
       ADT_Backslash = $adt("Backslash");
       NT_MidProtocolChars = $lex("MidProtocolChars");
       ADT_MidProtocolChars = $adt("MidProtocolChars");
       ADT_RascalKeywords = $keywords("RascalKeywords");
       NT_Parameters = $sort("Parameters");
       ADT_Parameters = $adt("Parameters");
       NT_PathTail = $sort("PathTail");
       ADT_PathTail = $adt("PathTail");
       NT_StringTemplate = $sort("StringTemplate");
       ADT_StringTemplate = $adt("StringTemplate");
       NT_OctalIntegerLiteral = $lex("OctalIntegerLiteral");
       ADT_OctalIntegerLiteral = $adt("OctalIntegerLiteral");
       NT_ProtocolPart = $sort("ProtocolPart");
       ADT_ProtocolPart = $adt("ProtocolPart");
       NT_NamedBackslash = $lex("NamedBackslash");
       ADT_NamedBackslash = $adt("NamedBackslash");
       NT_PreProtocolChars = $lex("PreProtocolChars");
       ADT_PreProtocolChars = $adt("PreProtocolChars");
       NT_Label = $sort("Label");
       ADT_Label = $adt("Label");
       NT_Kind = $sort("Kind");
       ADT_Kind = $adt("Kind");
       NT_RegExpModifier = $lex("RegExpModifier");
       ADT_RegExpModifier = $adt("RegExpModifier");
       NT_DateTimeLiteral = $sort("DateTimeLiteral");
       ADT_DateTimeLiteral = $adt("DateTimeLiteral");
       ADT_RuntimeException = $adt("RuntimeException");
       NT_NamedRegExp = $lex("NamedRegExp");
       ADT_NamedRegExp = $adt("NamedRegExp");
       NT_Assoc = $sort("Assoc");
       ADT_Assoc = $adt("Assoc");
       ADT_Grammar = $adt("Grammar");
       NT_DecimalIntegerLiteral = $lex("DecimalIntegerLiteral");
       ADT_DecimalIntegerLiteral = $adt("DecimalIntegerLiteral");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       NT_JustTime = $lex("JustTime");
       ADT_JustTime = $adt("JustTime");
       NT_FunctionModifier = $sort("FunctionModifier");
       ADT_FunctionModifier = $adt("FunctionModifier");
       NT_UserType = $sort("UserType");
       ADT_UserType = $adt("UserType");
       NT_ProdModifier = $sort("ProdModifier");
       ADT_ProdModifier = $adt("ProdModifier");
       NT_PathChars = $lex("PathChars");
       ADT_PathChars = $adt("PathChars");
       NT_DataTypeSelector = $sort("DataTypeSelector");
       ADT_DataTypeSelector = $adt("DataTypeSelector");
       NT_EvalCommand = $sort("EvalCommand");
       ADT_EvalCommand = $adt("EvalCommand");
       NT_PostPathChars = $lex("PostPathChars");
       ADT_PostPathChars = $adt("PostPathChars");
       NT_UnicodeEscape = $lex("UnicodeEscape");
       ADT_UnicodeEscape = $adt("UnicodeEscape");
       NT_Comment = $lex("Comment");
       ADT_Comment = $adt("Comment");
       NT_Commands = $sort("Commands");
       ADT_Commands = $adt("Commands");
       NT_Visit = $sort("Visit");
       ADT_Visit = $adt("Visit");
       NT_StructuredType = $sort("StructuredType");
       ADT_StructuredType = $adt("StructuredType");
       NT_Assignable = $sort("Assignable");
       ADT_Assignable = $adt("Assignable");
       NT_CommonKeywordParameters = $sort("CommonKeywordParameters");
       ADT_CommonKeywordParameters = $adt("CommonKeywordParameters");
       NT_ProtocolTail = $sort("ProtocolTail");
       ADT_ProtocolTail = $adt("ProtocolTail");
       NT_BasicType = $sort("BasicType");
       ADT_BasicType = $adt("BasicType");
       NT_Command = $sort("Command");
       ADT_Command = $adt("Command");
       $T2 = $TF.valueType();
       $T8 = $TF.parameterType("T2", $T2);
       $T12 = $TF.parameterType("T4", $T2);
       $T10 = $TF.parameterType("T3", $T2);
       $T3 = $TF.parameterType("T", $T2);
       $T31 = $TF.integerType();
       $T20 = $TF.parameterType("T", $T2);
       $T27 = $TF.voidType();
       $T6 = $TF.parameterType("T0", $T2);
       $T16 = $TF.sourceLocationType();
       $T7 = $TF.parameterType("T1", $T2);
       $T0 = $TF.stringType();
       $T19 = $TF.parameterType("T", $T2);
       NT_Mapping_Expression = $parameterizedSort("Mapping", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       $T21 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_1 = $parameterizedSort("KeywordArguments", new Type[] { $T21 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       NT_Mapping_Pattern = $parameterizedSort("Mapping", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       $T22 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_Expression = $parameterizedSort("KeywordArguments", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       $T11 = $TF.setType($TF.tupleType($T6, $T7, $T8, $T10, $T12));
       $T13 = $TF.listType(ADT_CharRange);
       NT_KeywordArgument_1 = $parameterizedSort("KeywordArgument", new Type[] { $T21 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       $T18 = $TF.boolType();
       $T17 = $TF.functionType($T18, $TF.tupleType($T19, "a", $T20, "b"), $TF.tupleEmpty());
       $T4 = $TF.listType($T3);
       $T14 = $TF.setType($TF.tupleType($T6, $T7));
       NT_Mapping_1 = $parameterizedSort("Mapping", new Type[] { $T21 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T21 });
       NT_KeywordArguments_Pattern = $parameterizedSort("KeywordArguments", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       $T23 = $TF.setType(ADT_Production);
       $T29 = $TF.setType(ADT_Symbol);
       $T15 = $TF.listType(ADT_Symbol);
       $T25 = $TF.setType(ADT_Attr);
       $T9 = $TF.setType($TF.tupleType($T6, $T7, $T8, $T10));
       $T24 = $TF.listType(ADT_Production);
       $T1 = $TF.setType($T3);
       $T28 = $TF.nodeType();
       $T5 = $TF.setType($TF.tupleType($T6, $T7, $T8));
       $T30 = $TF.setType(ADT_Condition);
       $T26 = $TF.setType($T27);
       ADT_Mapping_Expression = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Expression });
       ADT_KeywordArguments_1 = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { $T21 });
       ADT_Mapping_Pattern = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Pattern });
       ADT_KeywordArguments_Expression = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Expression });
       ADT_KeywordArgument_1 = $TF.abstractDataType($TS, "KeywordArgument", new Type[] { $T21 });
       ADT_Mapping_1 = $TF.abstractDataType($TS, "Mapping", new Type[] { $T21 });
       ADT_KeywordArguments_Pattern = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Pattern });
    
       rascalKeywords = ((ISet)$constants.get(56)/*{"loc","continue","throws","int","test","map","if","all","default","node","mod","set","start","false ...*/);
    
       $kwpDefaults_lang_rascal_format_Grammar_alt2r$755f83a423fa71e2 = Util.kwpMap("sep", ((IString)$constants.get(16)/*"="*/));
    
    }
    public IBool isTypeVar(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isTypeVar($P0);
    }
    public IList addLabels(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_Type.addLabels($P0, $P1);
    }
    public IBool CC(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_format_Grammar_CC$e54e4692db276314();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool sameType(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IBool) M_ParseTree.sameType($P0, $P1);
    }
    public IBool isAliasType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isAliasType($P0);
    }
    public IValue head(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.head($P0);
    }
    public IList head(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.head($P0, $P1);
    }
    public IInteger size(IValue $P0){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IInteger)M_String.String_size$4611676944e933d5((IString) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IInteger)M_Set.Set_size$215788d71e8b2455((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T4)){
         $result = (IInteger)M_List.List_size$ba7443328d8b4a27((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor choice(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_Type.choice($P0, $P1);
    }
    public IBool isValueType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isValueType($P0);
    }
    public IBool noAttrs(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_format_Grammar_noAttrs$5cd43d89bef1f2a4();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool isStrType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isStrType($P0);
    }
    public IString escape(IValue $P0){ // Generated by Resolver
       return (IString) M_lang_rascal_format_Escape.escape($P0);
    }
    public IString escape(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IString) M_String.escape($P0, $P1);
    }
    public ISet symbolDependencies(IValue $P0){ // Generated by Resolver
       return (ISet) M_analysis_grammars_Dependency.symbolDependencies($P0);
    }
    public IConstructor cleanIdentifiers(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (IConstructor)lang_rascal_format_Grammar_cleanIdentifiers$b3714ba292c6e08a((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString attr2mod(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Attr)){
         $result = (IString)lang_rascal_format_Grammar_attr2mod$2f0363e8223967f1((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isADTType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isADTType($P0);
    }
    public IBool isListType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isListType($P0);
    }
    public IBool isRealType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRealType($P0);
    }
    public IConstructor priority(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_ParseTree.priority($P0, $P1);
    }
    public IString symbol2rascal(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IString)lang_rascal_format_Grammar_symbol2rascal$7cd5ea92dc101f32((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString prod2rascal(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Production)){
         $result = (IString)lang_rascal_format_Grammar_prod2rascal$452015edf458efd4((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool same(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Production) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Production)){
         $result = (IBool)lang_rascal_format_Grammar_same$2f0264ee40551335((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IValue complement(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IValue)M_lang_rascal_grammar_definition_Characters.lang_rascal_grammar_definition_Characters_complement$b568586ea6930aa8((IConstructor) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T5)){
         $result = (IValue)M_Relation.Relation_complement$00086bfeeba07066((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T9)){
         $result = (IValue)M_Relation.Relation_complement$1dc10ce8d46ef909((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T11)){
         $result = (IValue)M_Relation.Relation_complement$56d58cd1d8429e72((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T13)){
         $result = (IValue)M_lang_rascal_grammar_definition_Characters.lang_rascal_grammar_definition_Characters_complement$9bb082bbab5b8dd8((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T14)){
         $result = (IValue)M_Relation.Relation_complement$4bb4b4dc0b4215a5((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IValue)M_lang_rascal_grammar_definition_Characters.lang_rascal_grammar_definition_Characters_complement$e4978c5ddcaf2671((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool AttrsAndCons(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_format_Grammar_AttrsAndCons$189b7fcef1590cf4();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool isNodeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNodeType($P0);
    }
    public IBool Prio(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_format_Grammar_Prio$3f3ee762d60b2b22();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IString reserved(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)lang_rascal_format_Grammar_reserved$ddb60b559dd45997((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isReifiedType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isReifiedType($P0);
    }
    public ITuple takeOneFrom(IValue $P0){ // Generated by Resolver
       ITuple $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T4)){
         $result = (ITuple)M_List.List_takeOneFrom$48bb3b6062ea97b1((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T1)){
         $result = (ITuple)M_Set.Set_takeOneFrom$291ddec83a7e9a61((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString alt2r(IValue $P0, IValue $P1, java.util.Map<java.lang.String,IValue> $kwpActuals){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Production)){
         $result = (IString)lang_rascal_format_Grammar_alt2r$755f83a423fa71e2((IConstructor) $P0, (IConstructor) $P1, $kwpActuals);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRelType($P0);
    }
    public IString definition2rascal(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_GrammarDefinition)){
         $result = (IString)lang_rascal_format_Grammar_definition2rascal$c2f97d7f1a79d87c((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor intersection(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_lang_rascal_grammar_definition_Characters.intersection($P0, $P1);
    }
    public IBool isConstructorType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isConstructorType($P0);
    }
    public IBool isListRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isListRelType($P0);
    }
    public IList addParamLabels(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_Type.addParamLabels($P0, $P1);
    }
    public IBool isMapType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isMapType($P0);
    }
    public IBool isBoolType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isBoolType($P0);
    }
    public IConstructor union(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_lang_rascal_grammar_definition_Characters.union($P0, $P1);
    }
    public IString iterseps2rascal(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type,$T15) && $isSubtypeOf($P2Type,$T0)){
         $result = (IString)lang_rascal_format_Grammar_iterseps2rascal$fe1027980573bd82((IConstructor) $P0, (IList) $P1, (IString) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public void println(IValue $P0){ // Generated by Resolver
        M_IO.println($P0);
    }
    public void println(){ // Generated by Resolver
        M_IO.println();
    }
    public IString topProd2rascal(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Production)){
         $result = (IString)lang_rascal_format_Grammar_topProd2rascal$a02b35d0088dbf93((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isLocType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isLocType($P0);
    }
    public IString module2rascal(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_GrammarModule)){
         $result = (IString)lang_rascal_format_Grammar_module2rascal$d9c2b846cb25969f((IConstructor) $P0);
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
    public ITuple headTail(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.headTail($P0);
    }
    public IConstructor treeAt(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IConstructor) M_ParseTree.treeAt($P0, $P1, $P2);
    }
    public IBool isSetType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isSetType($P0);
    }
    public IString alt2rascal(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case -2132978880:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Production)){
       		  $result = (IString)lang_rascal_format_Grammar_alt2rascal$27f049e7b59c20ec((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 101776608:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Production)){
       		  $result = (IString)lang_rascal_format_Grammar_alt2rascal$5a9bf4d6d6c6afd9((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -1467508160:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Production)){
       		  $result = (IString)lang_rascal_format_Grammar_alt2rascal$ad6d80d0ee92e40c((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 110389984:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Production)){
       		  $result = (IString)lang_rascal_format_Grammar_alt2rascal$6bd6165f23667a28((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Production)){
         $result = (IString)lang_rascal_format_Grammar_alt2rascal$39a84c47a7bfe678((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor delabel(IValue $P0){ // Generated by Resolver
       return (IConstructor) M_analysis_grammars_Dependency.delabel($P0);
    }
    public IBool isRatType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRatType($P0);
    }
    public IString layoutname(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IString)lang_rascal_format_Grammar_layoutname$e0986cf68da33e28((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString grammar2rascal(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (IString)lang_rascal_format_Grammar_grammar2rascal$18a0bd44045f05d6((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString grammar2rascal(IValue $P0, IValue $P1){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar) && $isSubtypeOf($P1Type,$T15)){
         $result = (IString)lang_rascal_format_Grammar_grammar2rascal$c79cc807e40e464a((IConstructor) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar) && $isSubtypeOf($P1Type,$T0)){
         $result = (IString)lang_rascal_format_Grammar_grammar2rascal$bdf0d923d57cb62b((IConstructor) $P0, (IString) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IString params2rascal(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T15)){
         $result = (IString)lang_rascal_format_Grammar_params2rascal$f4c1186947f6bb00((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isNumType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNumType($P0);
    }
    public IBool isTupleType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isTupleType($P0);
    }
    public IBool isBagType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isBagType($P0);
    }
    public ISet carrier(IValue $P0){ // Generated by Resolver
       return (ISet) M_Relation.carrier($P0);
    }
    public IBool isVoidType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isVoidType($P0);
    }
    public IBool isNonTerminalType(IValue $P0){ // Generated by Resolver
       return (IBool) M_ParseTree.isNonTerminalType($P0);
    }
    public IValue lub(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_Type.lub($P0, $P1);
    }
    public IBool subtype(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IBool) M_Type.subtype($P0, $P1);
    }
    public IString associativity(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Associativity)){
         $result = (IString)lang_rascal_format_Grammar_associativity$9acd60ef49279caa((IConstructor) $P0);
         if($result != null) return $result;
         $result = (IString)lang_rascal_format_Grammar_associativity$271cfecb7fc70442((IConstructor) $P0);
         if($result != null) return $result;
         $result = (IString)lang_rascal_format_Grammar_associativity$95950f301748ef8c((IConstructor) $P0);
         if($result != null) return $result;
         $result = (IString)lang_rascal_format_Grammar_associativity$6c4b5afff021251a((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor associativity(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IConstructor) M_ParseTree.associativity($P0, $P1, $P2);
    }
    public void definition2disk(IValue $P0, IValue $P1){ // Generated by Resolver
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T16) && $isSubtypeOf($P1Type, M_Grammar.ADT_GrammarDefinition)){
         try { lang_rascal_format_Grammar_definition2disk$5062e2919e188785((ISourceLocation) $P0, (IConstructor) $P1); return; } catch (FailReturnFromVoidException e){};
       
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IString cc2rascal(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T13)){
         $result = (IString)lang_rascal_format_Grammar_cc2rascal$0d43214752b1b902((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isFunctionType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isFunctionType($P0);
    }
    public IValue glb(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_Type.glb($P0, $P1);
    }
    public IConstructor difference(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_lang_rascal_grammar_definition_Characters.difference($P0, $P1);
    }
    public ISet range(IValue $P0){ // Generated by Resolver
       return (ISet) M_Relation.range($P0);
    }
    public IValue sort(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T4)){
         $result = (IValue)M_List.List_sort$1fe4426c8c8039da((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IValue)M_Set.Set_sort$2d7ce904b21febd4((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         return $RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{(IString) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList sort(IValue $P0, IValue $P1){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T1) && $isSubtypeOf($P1Type,$T17)){
         $result = (IList)M_Set.Set_sort$4b3ff1abd5c398df((ISet) $P0, (TypedFunctionInstance2<IValue, IValue, IValue>) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T4) && $isSubtypeOf($P1Type,$T17)){
         $result = (IList)M_List.List_sort$a9bbc6fca4e60d0a((IList) $P0, (TypedFunctionInstance2<IValue, IValue, IValue>) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isIntType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isIntType($P0);
    }
    public IBool isDateTimeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isDateTimeType($P0);
    }
    public IString range2rascal(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_CharRange)){
         $result = (IString)lang_rascal_format_Grammar_range2rascal$07747f28a4b93d11((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(959,211,<29,0>,<33,1>) 
    public void lang_rascal_format_Grammar_definition2disk$5062e2919e188785(ISourceLocation prefix_0, IConstructor def_1){ 
        
        
        try {
            /*muExists*/FOR0: 
                do {
                    FOR0_GEN1031:
                    for(IValue $elem3_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)def_1), "modules")))))){
                        IString $elem3 = (IString) $elem3_for;
                        IString m_2 = ((IString)($elem3));
                        M_IO.writeFile(((ISourceLocation)($aloc_field_update("extension", ((IString)$constants.get(0)/*".rsc"*/), ((ISourceLocation)($aloc_add_astr(((ISourceLocation)($aloc_add_astr(((ISourceLocation)prefix_0),((IString)$constants.get(1)/*"/"*/)))),((IString)($TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                             new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{$TF.stringType()}, 
                                                      new io.usethesource.vallang.IConstructor[]{}, 
                                                      $RVF.bool(false)),
                             m_2,
                             (IVisitFunction) (IValue $VISIT1_subject, TraversalState $traversalState) -> {
                                 VISIT1:switch(Fingerprint.getFingerprint($VISIT1_subject)){
                                 
                                     case 0:
                                         
                                 
                                     default: 
                                         if($isSubtypeOf($VISIT1_subject.getType(),$T0)){
                                            /*muExists*/CASE_0_0: 
                                                do {
                                                    final Matcher $matcher1 = (Matcher)$regExpCompile("::", ((IString)($VISIT1_subject)).getValue());
                                                    boolean $found2 = true;
                                                    
                                                        while($found2){
                                                            $found2 = $matcher1.find();
                                                            if($found2){
                                                               $traversalState.setBegin($matcher1.start());
                                                               $traversalState.setEnd($matcher1.end());
                                                               IString $replacement0 = (IString)(((IString)$constants.get(1)/*"/"*/));
                                                               if($isSubtypeOf($replacement0.getType(),$VISIT1_subject.getType())){
                                                                  $traversalState.setMatchedAndChanged(true, true);
                                                                  return $replacement0;
                                                               
                                                               } else {
                                                                  break VISIT1;// switch
                                                               
                                                               }
                                                            }
                                                    
                                                        }
                                            
                                                } while(false);
                                         
                                         }
                        
                                 }
                                 return $VISIT1_subject;
                             }))))))))), $RVF.list($me.module2rascal(((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_1), "modules"))))),((IString)m_2)))))), Util.kwpMap());
                    
                    }
                    continue FOR0;
                                
                } while(false);
            /* void:  muCon([]) */return;
        
        } catch (ReturnFromTraversalException e) {
            return ;
        }
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(1172,134,<35,0>,<37,1>) 
    public IString lang_rascal_format_Grammar_definition2rascal$c2f97d7f1a79d87c(IConstructor def_0){ 
        
        
        IString $reducer5 = (IString)(((IString)$constants.get(2)/*""*/));
        $REDUCER4_GEN1286:
        for(IValue $elem7_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)def_0), "modules")))))){
            IString $elem7 = (IString) $elem7_for;
            IString m_2 = ((IString)($elem7));
            final Template $template6 = (Template)new Template($RVF, "\n\n");
            $template6.beginIndent("    ");
            $template6.addStr(((IString)($me.module2rascal(((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_0), "modules"))))),((IString)m_2))))))).getValue());
            $template6.endIndent("    ");
            $reducer5 = ((IString)($astr_add_astr(((IString)($reducer5)),((IString)($template6.close())))));
        
        }
        
                    return ((IString)($reducer5));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(1308,232,<39,0>,<46,1>) 
    public IString lang_rascal_format_Grammar_module2rascal$d9c2b846cb25969f(IConstructor m_0){ 
        
        
        final Template $template8 = (Template)new Template($RVF, "module ");
        $template8.beginIndent("       ");
        $template8.addStr(((IString)(((IString)($aadt_get_field(((IConstructor)m_0), "name"))))).getValue());
        $template8.endIndent("       ");
        $template8.addStr(" \n");
        /*muExists*/LAB3: 
            do {
                LAB3_GEN1395:
                for(IValue $elem9_for : ((ISet)(((ISet)($aadt_get_field(((IConstructor)m_0), "imports")))))){
                    IString $elem9 = (IString) $elem9_for;
                    IString i_1 = null;
                    $template8.addStr("import ");
                    $template8.beginIndent("       ");
                    $template8.addStr(((IString)($elem9)).getValue());
                    $template8.endIndent("       ");
                    $template8.addStr(";\n");
                
                }
                continue LAB3;
                            
            } while(false);
        $template8.addStr("\n");
        /*muExists*/LAB4: 
            do {
                LAB4_GEN1455:
                for(IValue $elem10_for : ((ISet)(((ISet)($aadt_get_field(((IConstructor)m_0), "extends")))))){
                    IString $elem10 = (IString) $elem10_for;
                    IString i_2 = null;
                    $template8.addStr("extend ");
                    $template8.beginIndent("       ");
                    $template8.addStr(((IString)($elem10)).getValue());
                    $template8.endIndent("       ");
                    $template8.addStr(";\n");
                
                }
                continue LAB4;
                            
            } while(false);
        $template8.addStr("\n");
        $template8.addStr(((IString)($me.grammar2rascal(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)m_0), "grammar")))))))).getValue());
        return ((IString)($template8.close()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(1542,96,<48,0>,<50,1>) 
    public IString lang_rascal_format_Grammar_grammar2rascal$bdf0d923d57cb62b(IConstructor g_0, IString name_1){ 
        
        
        final Template $template11 = (Template)new Template($RVF, "module ");
        $template11.beginIndent("       ");
        $template11.addStr(((IString)name_1).getValue());
        $template11.endIndent("       ");
        $template11.addStr(" ");
        $template11.beginIndent(" ");
        $template11.addStr(((IString)($me.grammar2rascal(((IConstructor)g_0)))).getValue());
        $template11.endIndent(" ");
        return ((IString)($template11.close()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(1640,299,<52,0>,<60,1>) 
    public IString lang_rascal_format_Grammar_grammar2rascal$18a0bd44045f05d6(IConstructor g_0){ 
        
        
        g_0 = ((IConstructor)($me.cleanIdentifiers(((IConstructor)g_0))));
        ISet deps_1 = ((ISet)(M_analysis_grammars_Dependency.symbolDependencies(((IConstructor)g_0))));
        IList ordered_2 = ((IList)(M_analysis_graphs_Graph.order(((ISet)deps_1))));
        final IListWriter $listwriter12 = (IListWriter)$RVF.listWriter();
        $LCOMP13_GEN1783:
        for(IValue $elem14_for : ((ISet)(((ISet)($amap_field_project((IMap)((IMap)(((IMap)($aadt_get_field(((IConstructor)g_0), "rules"))))), ((IInteger)$constants.get(3)/*0*/)))).subtract(((ISet)(M_Relation.carrier(((ISet)deps_1)))))))){
            IConstructor $elem14 = (IConstructor) $elem14_for;
            IConstructor e_4 = null;
            $listwriter12.append($elem14);
        
        }
        
                    IList unordered_3 = ((IList)($listwriter12.done()));
        return ((IString)($me.grammar2rascal(((IConstructor)g_0), ((IList)$constants.get(4)/*[]*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(1941,406,<62,0>,<70,1>) 
    public IConstructor lang_rascal_format_Grammar_cleanIdentifiers$b3714ba292c6e08a(IConstructor g_0){ 
        
        
        try {
            IValue $visitResult = $TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptorAlwaysTrue($RVF.bool(false)),
                 g_0,
                 (IVisitFunction) (IValue $VISIT5_subject, TraversalState $traversalState) -> {
                     VISIT5:switch(Fingerprint.getFingerprint($VISIT5_subject)){
                     
                         case 1643638592:
                             if($isSubtypeOf($VISIT5_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_1643638592_4: 
                                    do {
                                        if($has_type_and_arity($VISIT5_subject, M_Type.Symbol_label_str_Symbol, 2)){
                                           IValue $arg0_34 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),0));
                                           if($isComparable($arg0_34.getType(), $T0)){
                                              final Matcher $matcher35 = (Matcher)$regExpCompile("(.*)-(.*)", ((IString)($arg0_34)).getValue());
                                              boolean $found36 = true;
                                              
                                                  while($found36){
                                                      $found36 = $matcher35.find();
                                                      if($found36){
                                                         IString pre_5 = ((IString)($RVF.string($matcher35.group(1))));
                                                         IString post_6 = ((IString)($RVF.string($matcher35.group(2))));
                                                         IValue $arg1_33 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),1));
                                                         if($isComparable($arg1_33.getType(), M_ParseTree.ADT_Symbol)){
                                                            ValueRef<IConstructor> s_7 = new ValueRef<IConstructor>();
                                                            final Template $template32 = (Template)new Template($RVF, "\\");
                                                            $template32.beginIndent("  ");
                                                            $template32.addStr(((IString)pre_5).getValue());
                                                            $template32.endIndent("  ");
                                                            $template32.addStr("-");
                                                            $template32.beginIndent(" ");
                                                            $template32.addStr(((IString)post_6).getValue());
                                                            $template32.endIndent(" ");
                                                            IConstructor $replacement31 = (IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)($template32.close())), ((IConstructor)($arg1_33))}));
                                                            if($isSubtypeOf($replacement31.getType(),$VISIT5_subject.getType())){
                                                               $traversalState.setMatchedAndChanged(true, true);
                                                               return $replacement31;
                                                            
                                                            } else {
                                                               break VISIT5;// switch
                                                            
                                                            }
                                                         }
                                                      
                                                      }
                                              
                                                  }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                         case -109773488:
                             if($isSubtypeOf($VISIT5_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_109773488_3: 
                                    do {
                                        if($has_type_and_arity($VISIT5_subject, M_ParseTree.Symbol_keywords_str, 1)){
                                           IValue $arg0_28 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),0));
                                           if($isComparable($arg0_28.getType(), $T0)){
                                              final Matcher $matcher29 = (Matcher)$regExpCompile(".*-.*", ((IString)($arg0_28)).getValue());
                                              boolean $found30 = true;
                                              
                                                  while($found30){
                                                      $found30 = $matcher29.find();
                                                      if($found30){
                                                         IConstructor s_4 = ((IConstructor)($VISIT5_subject));
                                                         IConstructor $replacement27 = (IConstructor)($RVF.constructor(M_ParseTree.Symbol_keywords_str, new IValue[]{((IString)(M_String.replaceAll(((IString)(((IString)($aadt_get_field(((IConstructor)s_4), "name"))))), ((IString)$constants.get(5)/*"-"*/), ((IString)$constants.get(6)/*"_"*/))))}));
                                                         if($isSubtypeOf($replacement27.getType(),$VISIT5_subject.getType())){
                                                            $traversalState.setMatchedAndChanged(true, true);
                                                            return $replacement27;
                                                         
                                                         } else {
                                                            break VISIT5;// switch
                                                         
                                                         }
                                                      }
                                              
                                                  }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                         case 856312:
                             if($isSubtypeOf($VISIT5_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_856312_2: 
                                    do {
                                        if($has_type_and_arity($VISIT5_subject, M_ParseTree.Symbol_lex_str, 1)){
                                           IValue $arg0_24 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),0));
                                           if($isComparable($arg0_24.getType(), $T0)){
                                              final Matcher $matcher25 = (Matcher)$regExpCompile(".*-.*", ((IString)($arg0_24)).getValue());
                                              boolean $found26 = true;
                                              
                                                  while($found26){
                                                      $found26 = $matcher25.find();
                                                      if($found26){
                                                         IConstructor s_3 = ((IConstructor)($VISIT5_subject));
                                                         IConstructor $replacement23 = (IConstructor)($RVF.constructor(M_ParseTree.Symbol_lex_str, new IValue[]{((IString)(M_String.replaceAll(((IString)(((IString)($aadt_get_field(((IConstructor)s_3), "name"))))), ((IString)$constants.get(5)/*"-"*/), ((IString)$constants.get(6)/*"_"*/))))}));
                                                         if($isSubtypeOf($replacement23.getType(),$VISIT5_subject.getType())){
                                                            $traversalState.setMatchedAndChanged(true, true);
                                                            return $replacement23;
                                                         
                                                         } else {
                                                            break VISIT5;// switch
                                                         
                                                         }
                                                      }
                                              
                                                  }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                         case 28290288:
                             if($isSubtypeOf($VISIT5_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_28290288_0: 
                                    do {
                                        if($has_type_and_arity($VISIT5_subject, M_ParseTree.Symbol_sort_str, 1)){
                                           IValue $arg0_16 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),0));
                                           if($isComparable($arg0_16.getType(), $T0)){
                                              final Matcher $matcher17 = (Matcher)$regExpCompile(".*-.*", ((IString)($arg0_16)).getValue());
                                              boolean $found18 = true;
                                              
                                                  while($found18){
                                                      $found18 = $matcher17.find();
                                                      if($found18){
                                                         IConstructor s_1 = ((IConstructor)($VISIT5_subject));
                                                         IConstructor $replacement15 = (IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)(M_String.replaceAll(((IString)(((IString)($aadt_get_field(((IConstructor)s_1), "name"))))), ((IString)$constants.get(5)/*"-"*/), ((IString)$constants.get(6)/*"_"*/))))}));
                                                         if($isSubtypeOf($replacement15.getType(),$VISIT5_subject.getType())){
                                                            $traversalState.setMatchedAndChanged(true, true);
                                                            return $replacement15;
                                                         
                                                         } else {
                                                            break VISIT5;// switch
                                                         
                                                         }
                                                      }
                                              
                                                  }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                         case -333228984:
                             if($isSubtypeOf($VISIT5_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_333228984_1: 
                                    do {
                                        if($has_type_and_arity($VISIT5_subject, M_ParseTree.Symbol_layouts_str, 1)){
                                           IValue $arg0_20 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),0));
                                           if($isComparable($arg0_20.getType(), $T0)){
                                              final Matcher $matcher21 = (Matcher)$regExpCompile(".*-.*", ((IString)($arg0_20)).getValue());
                                              boolean $found22 = true;
                                              
                                                  while($found22){
                                                      $found22 = $matcher21.find();
                                                      if($found22){
                                                         IConstructor s_2 = ((IConstructor)($VISIT5_subject));
                                                         IConstructor $replacement19 = (IConstructor)($RVF.constructor(M_ParseTree.Symbol_layouts_str, new IValue[]{((IString)(M_String.replaceAll(((IString)(((IString)($aadt_get_field(((IConstructor)s_2), "name"))))), ((IString)$constants.get(5)/*"-"*/), ((IString)$constants.get(6)/*"_"*/))))}));
                                                         if($isSubtypeOf($replacement19.getType(),$VISIT5_subject.getType())){
                                                            $traversalState.setMatchedAndChanged(true, true);
                                                            return $replacement19;
                                                         
                                                         } else {
                                                            break VISIT5;// switch
                                                         
                                                         }
                                                      }
                                              
                                                  }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                     
                     }
                     return $VISIT5_subject;
                 });
            return (IConstructor)$visitResult;
        
        } catch (ReturnFromTraversalException e) {
            return (IConstructor) e.getValue();
        }
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(2350,166,<72,0>,<76,1>) 
    public IString lang_rascal_format_Grammar_grammar2rascal$c79cc807e40e464a(IConstructor g_0, IList $__1){ 
        
        
        final Template $template37 = (Template)new Template($RVF, "");
        /*muExists*/LAB11: 
            do {
                LAB11_GEN2437:
                for(IValue $elem38_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)g_0), "rules")))))){
                    IConstructor $elem38 = (IConstructor) $elem38_for;
                    IConstructor nont_1 = ((IConstructor)($elem38));
                    $template37.addStr("\n");
                    $template37.addStr(((IString)($me.topProd2rascal(((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)g_0), "rules"))))),((IConstructor)nont_1))))))).getValue());
                    $template37.addStr("\n");
                
                }
                continue LAB11;
                            
            } while(false);
        return ((IString)($template37.close()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(2518,66,<78,0>,<80,1>) 
    public IBool lang_rascal_format_Grammar_same$2f0264ee40551335(IConstructor p_0, IConstructor q_1){ 
        
        
        return ((IBool)($equal(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_0), "def"))))), ((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)q_1), "def"))))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(2586,655,<82,0>,<105,1>) 
    public IString lang_rascal_format_Grammar_topProd2rascal$a02b35d0088dbf93(IConstructor p_0){ 
        
        
        IBool $aux16 = (IBool)(((IBool)$constants.get(7)/*false*/));
        $aux16 = ((IBool)$constants.get(7)/*false*/);
        /*muExists*/$EXP40: 
            do {
                if($has_type_and_arity(p_0, M_ParseTree.Production_regular_Symbol, 1)){
                   IValue $arg0_41 = (IValue)($aadt_subscript_int(((IConstructor)p_0),0));
                   if($isComparable($arg0_41.getType(), $T2)){
                      $aux16 = ((IBool)$constants.get(8)/*true*/);
                      break $EXP40; // muSucceed
                   } else {
                      $aux16 = ((IBool)$constants.get(7)/*false*/);
                      continue $EXP40;
                   }
                } else {
                   $aux16 = ((IBool)$constants.get(7)/*false*/);
                   continue $EXP40;
                }
            } while(false);
        if((((IBool)($aux16))).getValue()){
           return ((IString)$constants.get(2)/*""*/);
        
        } else {
           if((((IBool)($equal(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_0), "def"))))), ((IConstructor)$constants.get(9)/*empty()*/))))).getValue()){
              return ((IString)$constants.get(2)/*""*/);
           
           } else {
              if((((IBool)($equal(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_0), "def"))))), ((IConstructor)$constants.get(10)/*layouts("$default$")*/))))).getValue()){
                 return ((IString)$constants.get(2)/*""*/);
              
              }
           
           }
        }/*muExists*/IF13: 
            do {
                if($has_type_and_arity(p_0, M_Type.Production_choice_Symbol_set_Production, 2)){
                   IValue $arg0_52 = (IValue)($aadt_subscript_int(((IConstructor)p_0),0));
                   if($isComparable($arg0_52.getType(), M_ParseTree.ADT_Symbol)){
                      IConstructor nt_1 = ((IConstructor)($arg0_52));
                      IValue $arg1_43 = (IValue)($aadt_subscript_int(((IConstructor)p_0),1));
                      if($isComparable($arg1_43.getType(), $T23)){
                         ISet $subject44 = (ISet)($arg1_43);
                         IF13_CONS_choice_SET_CONS_priority_NAMED_SET_ELM:
                         for(IValue $elem49_for : ((ISet)($subject44))){
                             IConstructor $elem49 = (IConstructor) $elem49_for;
                             if($has_type_and_arity($elem49, M_ParseTree.Production_priority_Symbol_list_Production, 2)){
                                IValue $arg0_51 = (IValue)($aadt_subscript_int(((IConstructor)($elem49)),0));
                                if($isComparable($arg0_51.getType(), $T2)){
                                   IValue $arg1_50 = (IValue)($aadt_subscript_int(((IConstructor)($elem49)),1));
                                   if($isComparable($arg1_50.getType(), $T2)){
                                      IConstructor q_2 = ((IConstructor)($elem49));
                                      final ISet $subject46 = ((ISet)(((ISet)($subject44)).delete(((IConstructor)($elem49)))));
                                      q_2 = ((IConstructor)($elem49));
                                      IF13_CONS_choice_SET_CONS_priority_MVARr:
                                      for(IValue $elem48_for : new SubSetGenerator(((ISet)($subject46)))){
                                          ISet $elem48 = (ISet) $elem48_for;
                                          ISet r_3 = ((ISet)($elem48));
                                          final ISet $subject47 = ((ISet)(((ISet)($subject46)).subtract(((ISet)($elem48)))));
                                          if(((ISet)($subject47)).size() == 0){
                                             if((((IBool)($equal(((ISet)r_3),((ISet)$constants.get(11)/*{}*/)).not()))).getValue()){
                                                final Template $template42 = (Template)new Template($RVF, "");
                                                $template42.addStr(((IString)($me.topProd2rascal(((IConstructor)(M_Type.choice(((IConstructor)($arg0_52)), ((ISet)r_3))))))).getValue());
                                                $template42.addStr("\n\n");
                                                $template42.addStr(((IString)($me.topProd2rascal(((IConstructor)q_2)))).getValue());
                                                return ((IString)($template42.close()));
                                             
                                             } else {
                                                continue IF13_CONS_choice_SET_CONS_priority_MVARr;
                                             }
                                          } else {
                                             continue IF13_CONS_choice_SET_CONS_priority_MVARr;/*set pat3*/
                                          }
                                      }
                                      continue IF13;/*set pat4*/
                                                  
                                   } else {
                                      continue IF13_CONS_choice_SET_CONS_priority_NAMED_SET_ELM;
                                   }
                                } else {
                                   continue IF13_CONS_choice_SET_CONS_priority_NAMED_SET_ELM;
                                }
                             } else {
                                continue IF13_CONS_choice_SET_CONS_priority_NAMED_SET_ELM;
                             }
                         }
                         
                                     
                      }
                   
                   }
                
                }
        
            } while(false);
        IString kind_4 = ((IString)$constants.get(12)/*"syntax"*/);
        /*muExists*/IF16: 
            do {
                final IConstructor $subject_val64 = ((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_0), "def")))));
                IF16_DESC2897:
                for(IValue $elem65 : new DescendantMatchIterator($subject_val64, 
                    new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{$TF.listType(ADT_Symbol), M_Type.ADT_Exception, $TF.setType(ADT_Symbol), $TF.setType(ADT_Condition), ADT_KeywordArguments_1, M_ParseTree.ADT_Tree, M_ParseTree.ADT_TreeSearchResult_1, M_ParseTree.ADT_Condition, M_ParseTree.ADT_Production, M_ParseTree.ADT_Symbol, M_Grammar.ADT_Grammar, M_ParseTree.ADT_CharRange, M_Grammar.ADT_Item, M_Grammar.ADT_GrammarModule, $TF.listType(ADT_CharRange), M_Grammar.ADT_GrammarDefinition}, 
                                             new io.usethesource.vallang.IConstructor[]{}, 
                                             $RVF.bool(false)))){
                    if($isComparable($elem65.getType(), M_ParseTree.ADT_Symbol)){
                       if($has_type_and_arity($elem65, M_ParseTree.Symbol_layouts_str, 1)){
                          IValue $arg0_66 = (IValue)($subscript_int(((IValue)($elem65)),0));
                          if($isComparable($arg0_66.getType(), $T0)){
                             IString n_5 = null;
                             final Template $template63 = (Template)new Template($RVF, "layout ");
                             $template63.beginIndent("       ");
                             $template63.addStr(((IString)($arg0_66)).getValue());
                             $template63.endIndent("       ");
                             kind_4 = ((IString)($template63.close()));
                             continue IF16;
                          } else {
                             continue IF16_DESC2897;
                          }
                       } else {
                          continue IF16_DESC2897;
                       }
                    } else {
                       continue IF16_DESC2897;
                    }
                }
                
                             
            } while(false);
        /*muExists*/IF15: 
            do {
                final IConstructor $subject_val60 = ((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_0), "def")))));
                IF15_DESC2955:
                for(IValue $elem61 : new DescendantMatchIterator($subject_val60, 
                    new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{$TF.listType(ADT_Symbol), M_Type.ADT_Exception, $TF.setType(ADT_Symbol), $TF.setType(ADT_Condition), ADT_KeywordArguments_1, M_ParseTree.ADT_Tree, M_ParseTree.ADT_TreeSearchResult_1, M_ParseTree.ADT_Condition, M_ParseTree.ADT_Production, M_ParseTree.ADT_Symbol, M_Grammar.ADT_Grammar, M_ParseTree.ADT_CharRange, M_Grammar.ADT_Item, M_Grammar.ADT_GrammarModule, $TF.listType(ADT_CharRange), M_Grammar.ADT_GrammarDefinition}, 
                                             new io.usethesource.vallang.IConstructor[]{}, 
                                             $RVF.bool(false)))){
                    if($isComparable($elem61.getType(), M_ParseTree.ADT_Symbol)){
                       if($has_type_and_arity($elem61, M_ParseTree.Symbol_lex_str, 1)){
                          IValue $arg0_62 = (IValue)($subscript_int(((IValue)($elem61)),0));
                          if($isComparable($arg0_62.getType(), $T2)){
                             kind_4 = ((IString)$constants.get(13)/*"lexical"*/);
                             continue IF15_DESC2955;/*redirected IF15 to IF15_DESC2955; */
                          } else {
                             continue IF15_DESC2955;
                          }
                       } else {
                          continue IF15_DESC2955;
                       }
                    } else {
                       continue IF15_DESC2955;
                    }
                }
                final IConstructor $subject_val56 = ((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_0), "def")))));
                IF15_DESC2975:
                for(IValue $elem57 : new DescendantMatchIterator($subject_val56, 
                    new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{$TF.listType(ADT_Symbol), M_Type.ADT_Exception, $TF.setType(ADT_Symbol), $TF.setType(ADT_Condition), ADT_KeywordArguments_1, M_ParseTree.ADT_Tree, M_ParseTree.ADT_TreeSearchResult_1, M_ParseTree.ADT_Condition, M_ParseTree.ADT_Production, M_ParseTree.ADT_Symbol, M_Grammar.ADT_Grammar, M_ParseTree.ADT_CharRange, M_Grammar.ADT_Item, M_Grammar.ADT_GrammarModule, $TF.listType(ADT_CharRange), M_Grammar.ADT_GrammarDefinition}, 
                                             new io.usethesource.vallang.IConstructor[]{}, 
                                             $RVF.bool(false)))){
                    if($isComparable($elem57.getType(), M_ParseTree.ADT_Symbol)){
                       if($has_type_and_arity($elem57, M_ParseTree.Symbol_parameterized_lex_str_list_Symbol, 2)){
                          IValue $arg0_59 = (IValue)($subscript_int(((IValue)($elem57)),0));
                          if($isComparable($arg0_59.getType(), $T2)){
                             IValue $arg1_58 = (IValue)($subscript_int(((IValue)($elem57)),1));
                             if($isComparable($arg1_58.getType(), $T2)){
                                kind_4 = ((IString)$constants.get(13)/*"lexical"*/);
                                continue IF15_DESC2975;/*redirected IF15 to IF15_DESC2975; */
                             } else {
                                continue IF15_DESC2975;
                             }
                          } else {
                             continue IF15_DESC2975;
                          }
                       } else {
                          continue IF15_DESC2975;
                       }
                    } else {
                       continue IF15_DESC2975;
                    }
                }
                
                             
                             
            } while(false);
        /*muExists*/IF14: 
            do {
                final IConstructor $subject_val53 = ((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_0), "def")))));
                IF14_DESC3043:
                for(IValue $elem54 : new DescendantMatchIterator($subject_val53, 
                    new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{$TF.listType(ADT_Symbol), M_Type.ADT_Exception, $TF.setType(ADT_Symbol), $TF.setType(ADT_Condition), ADT_KeywordArguments_1, M_ParseTree.ADT_Tree, M_ParseTree.ADT_TreeSearchResult_1, M_ParseTree.ADT_Condition, M_ParseTree.ADT_Production, M_ParseTree.ADT_Symbol, M_Grammar.ADT_Grammar, M_ParseTree.ADT_CharRange, M_Grammar.ADT_Item, M_Grammar.ADT_GrammarModule, $TF.listType(ADT_CharRange), M_Grammar.ADT_GrammarDefinition}, 
                                             new io.usethesource.vallang.IConstructor[]{}, 
                                             $RVF.bool(false)))){
                    if($isComparable($elem54.getType(), M_ParseTree.ADT_Symbol)){
                       if($has_type_and_arity($elem54, M_ParseTree.Symbol_keywords_str, 1)){
                          IValue $arg0_55 = (IValue)($subscript_int(((IValue)($elem54)),0));
                          if($isComparable($arg0_55.getType(), $T2)){
                             kind_4 = ((IString)$constants.get(14)/*"keyword"*/);
                             continue IF14;
                          } else {
                             continue IF14_DESC3043;
                          }
                       } else {
                          continue IF14_DESC3043;
                       }
                    } else {
                       continue IF14_DESC3043;
                    }
                }
                
                             
            } while(false);
        final IConstructor $subject_val67 = ((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_0), "def")))));
        if($has_type_and_arity($subject_val67, M_ParseTree.Symbol_start_Symbol, 1)){
           IValue $arg0_68 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val67)),0));
           if($isComparable($arg0_68.getType(), $T2)){
              kind_4 = ((IString)($astr_add_astr(((IString)$constants.get(15)/*"start "*/),((IString)kind_4))));
           
           }
        
        }
        final Template $template69 = (Template)new Template($RVF, "");
        $template69.addStr(((IString)kind_4).getValue());
        $template69.addStr(" ");
        $template69.beginIndent(" ");
        $template69.addStr(((IString)($me.symbol2rascal(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_0), "def")))))))).getValue());
        $template69.endIndent(" ");
        $template69.addStr(" =\n  ");
        $template69.beginIndent("  ");
        $template69.addStr(((IString)($me.prod2rascal(((IConstructor)p_0)))).getValue());
        $template69.endIndent("  ");
        $template69.addStr("\n  ;");
        return ((IString)($template69.close()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(3243,102,<107,0>,<111,1>) 
    public IString lang_rascal_format_Grammar_layoutname$e0986cf68da33e28(IConstructor s_0){ 
        
        
        if($has_type_and_arity(s_0, M_ParseTree.Symbol_layouts_str, 1)){
           IValue $arg0_70 = (IValue)($aadt_subscript_int(((IConstructor)s_0),0));
           if($isComparable($arg0_70.getType(), $T0)){
              IString name_1 = null;
              return ((IString)($arg0_70));
           
           }
        
        }
        final Template $template71 = (Template)new Template($RVF, "unexpected ");
        $template71.beginIndent("           ");
        $template71.addVal(s_0);
        $template71.endIndent("           ");
        throw new Throw($template71.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(3347,144,<113,0>,<113,144>) 
    public IString lang_rascal_format_Grammar_alt2r$755f83a423fa71e2(IConstructor _def_0, IConstructor p_0, java.util.Map<java.lang.String,IValue> $kwpActuals){ 
        
        java.util.Map<java.lang.String,IValue> $kwpDefaults = $kwpDefaults_lang_rascal_format_Grammar_alt2r$755f83a423fa71e2;
    
        final Template $template72 = (Template)new Template($RVF, "");
        $template72.addStr(((IString)($me.symbol2rascal(((IConstructor)(($is(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_0), "def"))))),((IString)$constants.get(17)/*"label"*/)) ? ((IConstructor)($aadt_get_field(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_0), "def"))))), "symbol"))) : ((IConstructor)($aadt_get_field(((IConstructor)p_0), "def"))))))))).getValue());
        $template72.addStr(" ");
        $template72.beginIndent(" ");
        $template72.addStr(((IString)(((IString) ($kwpActuals.containsKey("sep") ? $kwpActuals.get("sep") : $kwpDefaults.get("sep"))))).getValue());
        $template72.endIndent(" ");
        $template72.addStr(" ");
        $template72.beginIndent(" ");
        $template72.addStr(((IString)($me.prod2rascal(((IConstructor)p_0)))).getValue());
        $template72.endIndent(" ");
        return ((IString)($template72.close()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(3492,66,<114,0>,<114,66>) 
    public IString lang_rascal_format_Grammar_alt2rascal$6bd6165f23667a28(IConstructor p_0){ 
        
        
        if($has_type_and_arity(p_0, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
           IValue $arg0_76 = (IValue)($aadt_subscript_int(((IConstructor)p_0),0));
           if($isComparable($arg0_76.getType(), M_ParseTree.ADT_Symbol)){
              IConstructor def_1 = ((IConstructor)($arg0_76));
              IValue $arg1_75 = (IValue)($aadt_subscript_int(((IConstructor)p_0),1));
              if($isComparable($arg1_75.getType(), $T2)){
                 IValue $arg2_74 = (IValue)($aadt_subscript_int(((IConstructor)p_0),2));
                 if($isComparable($arg2_74.getType(), $T2)){
                    return ((IString)($me.alt2r(((IConstructor)($arg0_76)), ((IConstructor)p_0), Util.kwpMap())));
                 
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(3559,80,<115,0>,<115,80>) 
    public IString lang_rascal_format_Grammar_alt2rascal$ad6d80d0ee92e40c(IConstructor p_0){ 
        
        
        if($has_type_and_arity(p_0, M_ParseTree.Production_priority_Symbol_list_Production, 2)){
           IValue $arg0_78 = (IValue)($aadt_subscript_int(((IConstructor)p_0),0));
           if($isComparable($arg0_78.getType(), M_ParseTree.ADT_Symbol)){
              IConstructor def_1 = ((IConstructor)($arg0_78));
              IValue $arg1_77 = (IValue)($aadt_subscript_int(((IConstructor)p_0),1));
              if($isComparable($arg1_77.getType(), $T2)){
                 return ((IString)($me.alt2r(((IConstructor)($arg0_78)), ((IConstructor)p_0), Util.kwpMap("sep", ((IString)$constants.get(18)/*">"*/)))));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(3640,229,<116,0>,<119,1>) 
    public IString lang_rascal_format_Grammar_alt2rascal$27f049e7b59c20ec(IConstructor p_0){ 
        
        
        if($has_type_and_arity(p_0, M_ParseTree.Production_associativity_Symbol_Associativity_set_Production, 3)){
           IValue $arg0_82 = (IValue)($aadt_subscript_int(((IConstructor)p_0),0));
           if($isComparable($arg0_82.getType(), M_ParseTree.ADT_Symbol)){
              IConstructor def_1 = ((IConstructor)($arg0_82));
              IValue $arg1_81 = (IValue)($aadt_subscript_int(((IConstructor)p_0),1));
              if($isComparable($arg1_81.getType(), M_ParseTree.ADT_Associativity)){
                 IConstructor a_2 = ((IConstructor)($arg1_81));
                 IValue $arg2_80 = (IValue)($aadt_subscript_int(((IConstructor)p_0),2));
                 if($isComparable($arg2_80.getType(), $T2)){
                    final Template $template79 = (Template)new Template($RVF, "= ");
                    $template79.beginIndent("  ");
                    $template79.addStr(((IString)($me.associativity(((IConstructor)($arg1_81))))).getValue());
                    $template79.endIndent("  ");
                    IString sepVal_3 = ((IString)($template79.close()));
                    return ((IString)($me.alt2r(((IConstructor)($arg0_82)), ((IConstructor)p_0), Util.kwpMap("sep", sepVal_3))));
                 
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(3871,70,<121,0>,<121,70>) 
    public IString lang_rascal_format_Grammar_alt2rascal$5a9bf4d6d6c6afd9(IConstructor p_0){ 
        
        
        if($has_type_and_arity(p_0, M_ParseTree.Production_regular_Symbol, 1)){
           IValue $arg0_83 = (IValue)($aadt_subscript_int(((IConstructor)p_0),0));
           if($isComparable($arg0_83.getType(), $T2)){
              return ((IString)($me.symbol2rascal(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_0), "def"))))))));
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(3942,67,<122,0>,<122,67>) 
    public IString lang_rascal_format_Grammar_alt2rascal$39a84c47a7bfe678(IConstructor p_0){ 
        
        
        final Template $template84 = (Template)new Template($RVF, "forgot ");
        $template84.beginIndent("       ");
        $template84.addVal(p_0);
        $template84.endIndent("       ");
        throw new Throw($template84.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(4012,1199,<125,0>,<158,1>) 
    public IString lang_rascal_format_Grammar_prod2rascal$452015edf458efd4(IConstructor p_0){ 
        
        
        final IConstructor $switchVal85 = ((IConstructor)p_0);
        boolean noCaseMatched_$switchVal85 = true;
        SWITCH19: switch(Fingerprint.getFingerprint($switchVal85)){
        
            case 101776608:
                if(noCaseMatched_$switchVal85){
                    noCaseMatched_$switchVal85 = false;
                    if($isSubtypeOf($switchVal85.getType(),M_ParseTree.ADT_Production)){
                       /*muExists*/CASE_101776608_6: 
                           do {
                               if($has_type_and_arity($switchVal85, M_ParseTree.Production_regular_Symbol, 1)){
                                  IValue $arg0_128 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),0));
                                  if($isComparable($arg0_128.getType(), $T2)){
                                     return ((IString)$constants.get(2)/*""*/);
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -1467508160:
                if(noCaseMatched_$switchVal85){
                    noCaseMatched_$switchVal85 = false;
                    if($isSubtypeOf($switchVal85.getType(),M_ParseTree.ADT_Production)){
                       /*muExists*/CASE_1467508160_1: 
                           do {
                               if($has_type_and_arity($switchVal85, M_ParseTree.Production_priority_Symbol_list_Production, 2)){
                                  IValue $arg0_102 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),0));
                                  if($isComparable($arg0_102.getType(), $T2)){
                                     IValue $arg1_101 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),1));
                                     if($isComparable($arg1_101.getType(), $T24)){
                                        IList alts_6 = ((IList)($arg1_101));
                                        final Template $template99 = (Template)new Template($RVF, "");
                                        $template99.addStr(((IString)($me.prod2rascal(((IConstructor)(M_List.head(((IList)($arg1_101)))))))).getValue());
                                        ;/*muExists*/LAB22: 
                                            do {
                                                LAB22_GEN4389:
                                                for(IValue $elem100_for : ((IList)(M_List.tail(((IList)($arg1_101)))))){
                                                    IConstructor $elem100 = (IConstructor) $elem100_for;
                                                    IConstructor pr_7 = ((IConstructor)($elem100));
                                                    $template99.addStr("\n> ");
                                                    $template99.beginIndent("   ");
                                                    $template99.addStr(((IString)($me.prod2rascal(((IConstructor)pr_7)))).getValue());
                                                    $template99.endIndent("   ");
                                                
                                                }
                                                continue LAB22;
                                                            
                                            } while(false);
                                        return ((IString)($template99.close()));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 110389984:
                if(noCaseMatched_$switchVal85){
                    noCaseMatched_$switchVal85 = false;
                    if($isSubtypeOf($switchVal85.getType(),M_ParseTree.ADT_Production)){
                       /*muExists*/CASE_110389984_3: 
                           do {
                               if($has_type_and_arity($switchVal85, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                  IValue $arg0_114 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),0));
                                  if($isComparable($arg0_114.getType(), M_ParseTree.ADT_Symbol)){
                                     if($has_type_and_arity($arg0_114, M_Type.Symbol_label_str_Symbol, 2)){
                                        IValue $arg0_116 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_114)),0));
                                        if($isComparable($arg0_116.getType(), $T0)){
                                           IString n_13 = ((IString)($arg0_116));
                                           IValue $arg1_115 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_114)),1));
                                           if($isComparable($arg1_115.getType(), M_ParseTree.ADT_Symbol)){
                                              IValue $arg1_113 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),1));
                                              if($isComparable($arg1_113.getType(), $T15)){
                                                 IList lhs_14 = ((IList)($arg1_113));
                                                 IValue $arg2_112 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),2));
                                                 if($isComparable($arg2_112.getType(), $T25)){
                                                    ISet as_15 = ((ISet)($arg2_112));
                                                    final Template $template109 = (Template)new Template($RVF, "");
                                                    /*muExists*/LAB24: 
                                                        do {
                                                            LAB24_GEN4780:
                                                            for(IValue $elem110_for : ((ISet)($arg2_112))){
                                                                IConstructor $elem110 = (IConstructor) $elem110_for;
                                                                IConstructor a_16 = ((IConstructor)($elem110));
                                                                ;$template109.addStr(((IString)($me.attr2mod(((IConstructor)a_16)))).getValue());
                                                                $template109.addStr(" ");
                                                            
                                                            }
                                                            continue LAB24;
                                                                        
                                                        } while(false);
                                                    ;$template109.addStr(((IString)($me.reserved(((IString)($arg0_116))))).getValue());
                                                    $template109.addStr(": ");
                                                    /*muExists*/LAB25: 
                                                        do {
                                                            LAB25_GEN4828:
                                                            for(IValue $elem111_for : ((IList)($arg1_113))){
                                                                IConstructor $elem111 = (IConstructor) $elem111_for;
                                                                IConstructor s_17 = ((IConstructor)($elem111));
                                                                ;$template109.addStr(((IString)($me.symbol2rascal(((IConstructor)s_17)))).getValue());
                                                                $template109.addStr(" ");
                                                            
                                                            }
                                                            continue LAB25;
                                                                        
                                                        } while(false);
                                                    return ((IString)($template109.close()));
                                                 
                                                 }
                                              
                                              }
                                           
                                           }
                                        
                                        }
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal85.getType(),M_ParseTree.ADT_Production)){
                       /*muExists*/CASE_110389984_4: 
                           do {
                               if($has_type_and_arity($switchVal85, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                  IValue $arg0_121 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),0));
                                  if($isComparable($arg0_121.getType(), M_ParseTree.ADT_Symbol)){
                                     IValue $arg1_120 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),1));
                                     if($isComparable($arg1_120.getType(), $T15)){
                                        IList lhs_18 = ((IList)($arg1_120));
                                        IValue $arg2_119 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),2));
                                        if($isComparable($arg2_119.getType(), $T26)){
                                           if($arg2_119.equals(((ISet)$constants.get(11)/*{}*/))){
                                             final Template $template117 = (Template)new Template($RVF, "");
                                             /*muExists*/LAB26: 
                                                 do {
                                                     LAB26_GEN4932:
                                                     for(IValue $elem118_for : ((IList)($arg1_120))){
                                                         IConstructor $elem118 = (IConstructor) $elem118_for;
                                                         IConstructor s_19 = ((IConstructor)($elem118));
                                                         ;$template117.addStr(((IString)($me.symbol2rascal(((IConstructor)s_19)))).getValue());
                                                         $template117.addStr(" ");
                                                     
                                                     }
                                                     continue LAB26;
                                                                 
                                                 } while(false);
                                             return ((IString)($template117.close()));
                                           
                                           }
                                        
                                        }
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal85.getType(),M_ParseTree.ADT_Production)){
                       /*muExists*/CASE_110389984_5: 
                           do {
                               if($has_type_and_arity($switchVal85, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                  IValue $arg0_127 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),0));
                                  if($isComparable($arg0_127.getType(), M_ParseTree.ADT_Symbol)){
                                     IValue $arg1_126 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),1));
                                     if($isComparable($arg1_126.getType(), $T15)){
                                        IList lhs_20 = ((IList)($arg1_126));
                                        IValue $arg2_125 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),2));
                                        if($isComparable($arg2_125.getType(), $T25)){
                                           ISet as_21 = ((ISet)($arg2_125));
                                           final Template $template122 = (Template)new Template($RVF, "");
                                           /*muExists*/LAB27: 
                                               do {
                                                   LAB27_GEN5048:
                                                   for(IValue $elem123_for : ((ISet)($arg2_125))){
                                                       IConstructor $elem123 = (IConstructor) $elem123_for;
                                                       IConstructor a_22 = ((IConstructor)($elem123));
                                                       ;$template122.addStr(((IString)($me.attr2mod(((IConstructor)a_22)))).getValue());
                                                       $template122.addStr(" ");
                                                   
                                                   }
                                                   continue LAB27;
                                                               
                                               } while(false);
                                           ;/*muExists*/LAB28: 
                                               do {
                                                   LAB28_GEN5081:
                                                   for(IValue $elem124_for : ((IList)($arg1_126))){
                                                       IConstructor $elem124 = (IConstructor) $elem124_for;
                                                       IConstructor s_23 = ((IConstructor)($elem124));
                                                       ;$template122.addStr(((IString)($me.symbol2rascal(((IConstructor)s_23)))).getValue());
                                                       $template122.addStr(" ");
                                                   
                                                   }
                                                   continue LAB28;
                                                               
                                               } while(false);
                                           return ((IString)($template122.close()));
                                        
                                        }
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -304752112:
                if(noCaseMatched_$switchVal85){
                    noCaseMatched_$switchVal85 = false;
                    if($isSubtypeOf($switchVal85.getType(),M_ParseTree.ADT_Production)){
                       /*muExists*/CASE_304752112_0: 
                           do {
                               if($has_type_and_arity($switchVal85, M_Type.Production_choice_Symbol_set_Production, 2)){
                                  IValue $arg0_98 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),0));
                                  if($isComparable($arg0_98.getType(), $T2)){
                                     IValue $arg1_97 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),1));
                                     if($isComparable($arg1_97.getType(), $T23)){
                                        ISet alts_1 = ((ISet)($arg1_97));
                                        ITuple $TMP87 = (ITuple)(M_Set.takeOneFrom(((ISet)($arg1_97))));
                                        IConstructor fst_2 = ((IConstructor)($atuple_subscript_int(((ITuple)($TMP87)),0)));
                                        ISet rest_3 = ((ISet)($atuple_subscript_int(((ITuple)($TMP87)),1)));
                                        final Template $template88 = (Template)new Template($RVF, "");
                                        $template88.addStr(((IString)($me.prod2rascal(((IConstructor)fst_2)))).getValue());
                                        ;/*muExists*/LAB20: 
                                            do {
                                                /*muExists*/LAB20_GEN4172_CONS_prod: 
                                                    do {
                                                        LAB20_GEN4172:
                                                        for(IValue $elem89_for : ((ISet)rest_3)){
                                                            IConstructor $elem89 = (IConstructor) $elem89_for;
                                                            if($has_type_and_arity($elem89, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                                               IValue $arg0_92 = (IValue)($aadt_subscript_int(((IConstructor)($elem89)),0));
                                                               if($isComparable($arg0_92.getType(), $T2)){
                                                                  IValue $arg1_91 = (IValue)($aadt_subscript_int(((IConstructor)($elem89)),1));
                                                                  if($isComparable($arg1_91.getType(), $T2)){
                                                                     IValue $arg2_90 = (IValue)($aadt_subscript_int(((IConstructor)($elem89)),2));
                                                                     if($isComparable($arg2_90.getType(), $T2)){
                                                                        IConstructor pr_4 = ((IConstructor)($elem89));
                                                                        $template88.addStr("\n| ");
                                                                        $template88.beginIndent("  ");
                                                                        $template88.addStr(((IString)($me.prod2rascal(((IConstructor)pr_4)))).getValue());
                                                                        $template88.endIndent("  ");
                                                                     
                                                                     } else {
                                                                        continue LAB20_GEN4172;
                                                                     }
                                                                  } else {
                                                                     continue LAB20_GEN4172;
                                                                  }
                                                               } else {
                                                                  continue LAB20_GEN4172;
                                                               }
                                                            } else {
                                                               continue LAB20_GEN4172;
                                                            }
                                                        }
                                                        continue LAB20;
                                                                    
                                                    } while(false);
                                        
                                            } while(false);
                                        ;/*muExists*/LAB21: 
                                            do {
                                                LAB21_GEN4238:
                                                for(IValue $elem96_for : ((ISet)rest_3)){
                                                    IConstructor $elem96 = (IConstructor) $elem96_for;
                                                    IConstructor pr_5 = ((IConstructor)($elem96));
                                                    if($has_type_and_arity(pr_5, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                                       IValue $arg0_95 = (IValue)($aadt_subscript_int(((IConstructor)pr_5),0));
                                                       if($isComparable($arg0_95.getType(), $T2)){
                                                          IValue $arg1_94 = (IValue)($aadt_subscript_int(((IConstructor)pr_5),1));
                                                          if($isComparable($arg1_94.getType(), $T2)){
                                                             IValue $arg2_93 = (IValue)($aadt_subscript_int(((IConstructor)pr_5),2));
                                                             if($isComparable($arg2_93.getType(), $T2)){
                                                                $template88.addStr("\n| ");
                                                                $template88.beginIndent("  ");
                                                                $template88.addStr(((IString)($me.prod2rascal(((IConstructor)pr_5)))).getValue());
                                                                $template88.endIndent("  ");
                                                             
                                                             } else {
                                                                break LAB21_GEN4238; // muSucceed
                                                             }
                                                          } else {
                                                             break LAB21_GEN4238; // muSucceed
                                                          }
                                                       } else {
                                                          break LAB21_GEN4238; // muSucceed
                                                       }
                                                    } else {
                                                       break LAB21_GEN4238; // muSucceed
                                                    }
                                                }
                                                continue LAB21;
                                                            
                                            } while(false);
                                        return ((IString)($template88.close()));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -2132978880:
                if(noCaseMatched_$switchVal85){
                    noCaseMatched_$switchVal85 = false;
                    if($isSubtypeOf($switchVal85.getType(),M_ParseTree.ADT_Production)){
                       /*muExists*/CASE_2132978880_2: 
                           do {
                               if($has_type_and_arity($switchVal85, M_ParseTree.Production_associativity_Symbol_Associativity_set_Production, 3)){
                                  IValue $arg0_108 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),0));
                                  if($isComparable($arg0_108.getType(), $T2)){
                                     IValue $arg1_107 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),1));
                                     if($isComparable($arg1_107.getType(), M_ParseTree.ADT_Associativity)){
                                        IConstructor a_8 = ((IConstructor)($arg1_107));
                                        IValue $arg2_106 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal85)),2));
                                        if($isComparable($arg2_106.getType(), $T23)){
                                           ISet alts_9 = ((ISet)($arg2_106));
                                           ITuple $TMP103 = (ITuple)(M_Set.takeOneFrom(((ISet)($arg2_106))));
                                           IConstructor fst_10 = ((IConstructor)($atuple_subscript_int(((ITuple)($TMP103)),0)));
                                           ISet rest_11 = ((ISet)($atuple_subscript_int(((ITuple)($TMP103)),1)));
                                           final Template $template104 = (Template)new Template($RVF, "");
                                           $template104.addStr(((IString)($me.associativity(((IConstructor)($arg1_107))))).getValue());
                                           $template104.addStr(" \n  ( ");
                                           $template104.beginIndent("    ");
                                           $template104.addStr(((IString)($me.prod2rascal(((IConstructor)fst_10)))).getValue());
                                           $template104.endIndent("    ");
                                           ;/*muExists*/LAB23: 
                                               do {
                                                   LAB23_GEN4609:
                                                   for(IValue $elem105_for : ((ISet)rest_11)){
                                                       IConstructor $elem105 = (IConstructor) $elem105_for;
                                                       IConstructor pr_12 = ((IConstructor)($elem105));
                                                       $template104.addStr("\n  | ");
                                                       $template104.beginIndent("    ");
                                                       $template104.addStr(((IString)($me.prod2rascal(((IConstructor)pr_12)))).getValue());
                                                       $template104.endIndent("    ");
                                                   
                                                   }
                                                   continue LAB23;
                                                               
                                               } while(false);
                                           $template104.addStr("\n  )");
                                           return ((IString)($template104.close()));
                                        
                                        }
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            default: final Template $template86 = (Template)new Template($RVF, "missed a case ");
                     $template86.beginIndent("              ");
                     $template86.addVal(p_0);
                     $template86.endIndent("              ");
                      
                     throw new Throw($template86.close());
        }
        
                   
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(5213,36,<160,0>,<160,36>) 
    public IString lang_rascal_format_Grammar_associativity$9acd60ef49279caa(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Associativity_left_, 0)){
           return ((IString)$constants.get(19)/*"left"*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(5250,38,<161,0>,<161,38>) 
    public IString lang_rascal_format_Grammar_associativity$271cfecb7fc70442(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Associativity_right_, 0)){
           return ((IString)$constants.get(20)/*"right"*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(5289,38,<162,0>,<162,38>) 
    public IString lang_rascal_format_Grammar_associativity$95950f301748ef8c(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Associativity_assoc_, 0)){
           return ((IString)$constants.get(21)/*"assoc"*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(5328,46,<163,0>,<163,46>) 
    public IString lang_rascal_format_Grammar_associativity$6c4b5afff021251a(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Associativity_non_assoc_, 0)){
           return ((IString)$constants.get(22)/*"non-assoc"*/);
        
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(6145,89,<167,0>,<169,1>) 
    public IString lang_rascal_format_Grammar_reserved$ddb60b559dd45997(IString name_0){ 
        
        
        /*muExists*/$RET129: 
            do {
                if((((IBool)($RVF.bool(((ISet)rascalKeywords).contains(((IString)name_0)))))).getValue()){
                   final Template $template130 = (Template)new Template($RVF, "\\");
                   $template130.beginIndent("  ");
                   $template130.addStr(((IString)name_0).getValue());
                   $template130.endIndent("  ");
                   return ((IString)($template130.close()));
                
                }
        
            } while(false);
        return ((IString)name_0);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(6236,131,<171,0>,<172,30>) 
    public IBool lang_rascal_format_Grammar_noAttrs$5cd43d89bef1f2a4(){ 
        
        
        return ((IBool)($equal(((IString)($me.prod2rascal(((IConstructor)$constants.get(23)/*prod(sort("ID-TYPE"),[sort("PICO-ID"),lit(":"),sort("TYPE")],{})*/)))), ((IString)$constants.get(28)/*"PICO-ID ":" TYPE "*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(6369,189,<174,0>,<177,43>) 
    public IBool lang_rascal_format_Grammar_AttrsAndCons$189b7fcef1590cf4(){ 
        
        
        return ((IBool)($equal(((IString)($me.prod2rascal(((IConstructor)$constants.get(29)/*prod(label("decl",sort("ID-TYPE")),[sort("PICO-ID"),lit(":"),sort("TYPE")],{})*/)))), ((IString)$constants.get(31)/*"decl: PICO-ID ":" TYPE "*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(6575,185,<179,0>,<181,37>) 
    public IBool lang_rascal_format_Grammar_CC$e54e4692db276314(){ 
        
        
        return ((IBool)($equal(((IString)($me.prod2rascal(((IConstructor)$constants.get(32)/*prod(label("whitespace",sort("LAYOUT")),[\char-class([range(9,9),range(10,10),range(13,13),range(32, ...*/)))), ((IString)$constants.get(36)/*"whitespace: [\t \n \a0D \ ] "*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(6762,329,<183,0>,<187,57>) 
    public IBool lang_rascal_format_Grammar_Prio$3f3ee762d60b2b22(){ 
        
        
        return ((IBool)($equal(((IString)($me.prod2rascal(((IConstructor)$constants.get(37)/*priority(sort("EXP"),[prod(sort("EXP"),[sort("EXP"),lit("||"),sort("EXP")],{}),prod(sort("EXP"),[sor ...*/)))), ((IString)$constants.get(45)/*"EXP "||" EXP 
        > EXP "-" EXP 
        > EXP "+" EXP "*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(7094,309,<189,0>,<197,1>) 
    public IString lang_rascal_format_Grammar_attr2mod$2f0363e8223967f1(IConstructor a_0){ 
        
        
        final IConstructor $switchVal131 = ((IConstructor)a_0);
        boolean noCaseMatched_$switchVal131 = true;
        SWITCH29: switch(Fingerprint.getFingerprint($switchVal131)){
        
            case 916688:
                if(noCaseMatched_$switchVal131){
                    noCaseMatched_$switchVal131 = false;
                    if($isSubtypeOf($switchVal131.getType(),M_ParseTree.ADT_Attr)){
                       /*muExists*/CASE_916688_1: 
                           do {
                               if($has_type_and_arity($switchVal131, M_Type.Attr_tag_value, 1)){
                                  IValue $arg0_135 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal131)),0));
                                  if($isComparable($arg0_135.getType(), $T28)){
                                     /*muExists*/CASE_916688_1_CONS_tag_CONS_x: 
                                         do {
                                             if($isSubtypeOf($arg0_135.getType(),$T28)){
                                                final IString $fun_name_subject137 = ((IString)($anode_get_name((INode)((INode)($arg0_135)))));
                                                IString x_1 = ((IString)($fun_name_subject137));
                                                if($arg0_135 instanceof INode && ((INode)$arg0_135).arity() == 1 && ((INode)$arg0_135).getName().equals(((IString)($fun_name_subject137)).getValue())){
                                                   IValue $arg0_136 = (IValue)($anode_subscript_int(((INode)($arg0_135)),0));
                                                   if($isComparable($arg0_136.getType(), $T0)){
                                                      IString y_2 = ((IString)($arg0_136));
                                                      final Template $template134 = (Template)new Template($RVF, "@");
                                                      $template134.beginIndent(" ");
                                                      $template134.addStr(((IString)x_1).getValue());
                                                      $template134.endIndent(" ");
                                                      $template134.addStr("=\"");
                                                      $template134.beginIndent("   ");
                                                      $template134.addStr(((IString)(M_lang_rascal_format_Escape.escape(((IString)($arg0_136))))).getValue());
                                                      $template134.endIndent("   ");
                                                      $template134.addStr("\"");
                                                      return ((IString)($template134.close()));
                                                   
                                                   }
                                                
                                                }
                                             
                                             }
                                     
                                         } while(false);
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal131.getType(),M_ParseTree.ADT_Attr)){
                       /*muExists*/CASE_916688_2: 
                           do {
                               if($has_type_and_arity($switchVal131, M_Type.Attr_tag_value, 1)){
                                  IValue $arg0_139 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal131)),0));
                                  if($isComparable($arg0_139.getType(), $T28)){
                                     /*muExists*/CASE_916688_2_CONS_tag_CONS_x: 
                                         do {
                                             if($isSubtypeOf($arg0_139.getType(),$T28)){
                                                final IString $fun_name_subject140 = ((IString)($anode_get_name((INode)((INode)($arg0_139)))));
                                                IString x_3 = null;
                                                if($arg0_139 instanceof INode && ((INode)$arg0_139).arity() == 0 && ((INode)$arg0_139).getName().equals(((IString)($fun_name_subject140)).getValue())){
                                                   final Template $template138 = (Template)new Template($RVF, "@");
                                                   $template138.beginIndent(" ");
                                                   $template138.addStr(((IString)($fun_name_subject140)).getValue());
                                                   $template138.endIndent(" ");
                                                   return ((IString)($template138.close()));
                                                
                                                }
                                             
                                             }
                                     
                                         } while(false);
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 744972456:
                if(noCaseMatched_$switchVal131){
                    noCaseMatched_$switchVal131 = false;
                    if($isSubtypeOf($switchVal131.getType(),M_ParseTree.ADT_Attr)){
                       /*muExists*/CASE_744972456_3: 
                           do {
                               if($has_type_and_arity($switchVal131, M_ParseTree.Attr_assoc_Associativity, 1)){
                                  IValue $arg0_141 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal131)),0));
                                  if($isComparable($arg0_141.getType(), M_ParseTree.ADT_Associativity)){
                                     IConstructor as_4 = null;
                                     return ((IString)($me.associativity(((IConstructor)($arg0_141)))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 0:
                if(noCaseMatched_$switchVal131){
                    noCaseMatched_$switchVal131 = false;
                    
                }
                
        
            default: if($isSubtypeOf($switchVal131.getType(),M_ParseTree.ADT_Attr)){
                        /*muExists*/CASE_0_0: 
                            do {
                                if($has_type_and_arity($switchVal131, M_ParseTree.Attr_bracket_, 0)){
                                   return ((IString)$constants.get(46)/*"bracket"*/);
                                
                                }
                        
                            } while(false);
                     
                     }
                     final Template $template132 = (Template)new Template($RVF, "@Unsupported(\"");
                     $template132.beginIndent("               ");
                     final Template $template133 = (Template)new Template($RVF, "");
                     $template133.addVal(a_0);
                     $template132.addStr(((IString)(M_lang_rascal_format_Escape.escape(((IString)($template133.close()))))).getValue());
                     $template132.endIndent("               ");
                     $template132.addStr("\")");
                     return ((IString)($template132.close()));
        
        }
        
                   
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(7405,3357,<199,0>,<288,1>) 
    public IString lang_rascal_format_Grammar_symbol2rascal$7cd5ea92dc101f32(IConstructor sym_0){ 
        
        
        final IConstructor $switchVal142 = ((IConstructor)sym_0);
        boolean noCaseMatched_$switchVal142 = true;
        SWITCH30: switch(Fingerprint.getFingerprint($switchVal142)){
        
            case 1643638592:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1643638592_0: 
                           do {
                               if($has_type_and_arity($switchVal142, M_Type.Symbol_label_str_Symbol, 2)){
                                  IValue $arg0_145 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_145.getType(), $T0)){
                                     IString l_1 = ((IString)($arg0_145));
                                     IValue $arg1_144 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_144.getType(), M_ParseTree.ADT_Symbol)){
                                        IConstructor x_2 = ((IConstructor)($arg1_144));
                                        final Template $template143 = (Template)new Template($RVF, "");
                                        $template143.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg1_144))))).getValue());
                                        $template143.addStr(" ");
                                        $template143.beginIndent(" ");
                                        $template143.addStr(((IString)($arg0_145)).getValue());
                                        $template143.endIndent(" ");
                                        return ((IString)($template143.close()));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -964239440:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_964239440_15: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_iter_star_seps_Symbol_list_Symbol, 2)){
                                  IValue $arg0_179 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_179.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor x_24 = ((IConstructor)($arg0_179));
                                     IValue $arg1_178 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_178.getType(), $T15)){
                                        IList seps_25 = ((IList)($arg1_178));
                                        return ((IString)($me.iterseps2rascal(((IConstructor)($arg0_179)), ((IList)($arg1_178)), ((IString)$constants.get(47)/*"*"*/))));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 1444258592:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1444258592_6: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_parameterized_sort_str_list_Symbol, 2)){
                                  IValue $arg0_155 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_155.getType(), $T0)){
                                     IString name_8 = ((IString)($arg0_155));
                                     IValue $arg1_154 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_154.getType(), $T15)){
                                        IList parameters_9 = ((IList)($arg1_154));
                                        final Template $template153 = (Template)new Template($RVF, "");
                                        $template153.addStr(((IString)($arg0_155)).getValue());
                                        $template153.addStr("[");
                                        $template153.beginIndent(" ");
                                        $template153.addStr(((IString)($me.params2rascal(((IList)($arg1_154))))).getValue());
                                        $template153.endIndent(" ");
                                        $template153.addStr("]");
                                        return ((IString)($template153.close()));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 1206598288:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1206598288_8: 
                           do {
                               if($has_type_and_arity($switchVal142, M_Type.Symbol_parameter_str_Symbol, 2)){
                                  IValue $arg0_161 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_161.getType(), $T0)){
                                     IString t_12 = null;
                                     IValue $arg1_160 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_160.getType(), $T2)){
                                        final Template $template159 = (Template)new Template($RVF, "&");
                                        $template159.beginIndent(" ");
                                        $template159.addStr(((IString)($arg0_161)).getValue());
                                        $template159.endIndent(" ");
                                        return ((IString)($template159.close()));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 757310344:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_757310344_3: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_cilit_str, 1)){
                                  IValue $arg0_150 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_150.getType(), $T0)){
                                     IString x_5 = ((IString)($arg0_150));
                                     final Template $template149 = (Template)new Template($RVF, "\'");
                                     $template149.beginIndent("  ");
                                     $template149.addStr(((IString)(M_lang_rascal_format_Escape.escape(((IString)($arg0_150))))).getValue());
                                     $template149.endIndent("  ");
                                     $template149.addStr("\'");
                                     return ((IString)($template149.close()));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 856312:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_856312_4: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_lex_str, 1)){
                                  IValue $arg0_151 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_151.getType(), $T0)){
                                     IString x_6 = null;
                                     return ((IString)($arg0_151));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 1154855088:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1154855088_7: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_parameterized_lex_str_list_Symbol, 2)){
                                  IValue $arg0_158 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_158.getType(), $T0)){
                                     IString name_10 = ((IString)($arg0_158));
                                     IValue $arg1_157 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_157.getType(), $T15)){
                                        IList parameters_11 = ((IList)($arg1_157));
                                        final Template $template156 = (Template)new Template($RVF, "");
                                        $template156.addStr(((IString)($arg0_158)).getValue());
                                        $template156.addStr("[");
                                        $template156.beginIndent(" ");
                                        $template156.addStr(((IString)($me.params2rascal(((IList)($arg1_157))))).getValue());
                                        $template156.endIndent(" ");
                                        $template156.addStr("]");
                                        return ((IString)($template156.close()));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 910072:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_910072_10: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_seq_list_Symbol, 1)){
                                  IValue $arg0_169 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_169.getType(), $T15)){
                                     IList syms_17 = ((IList)($arg0_169));
                                     final Template $template167 = (Template)new Template($RVF, "( ");
                                     /*muExists*/LAB32: 
                                         do {
                                             LAB32_GEN8347:
                                             for(IValue $elem168_for : ((IList)($arg0_169))){
                                                 IConstructor $elem168 = (IConstructor) $elem168_for;
                                                 IConstructor s_18 = ((IConstructor)($elem168));
                                                 $template167.addStr(" ");
                                                 $template167.beginIndent(" ");
                                                 $template167.addStr(((IString)($me.symbol2rascal(((IConstructor)s_18)))).getValue());
                                                 $template167.endIndent(" ");
                                                 $template167.addStr(" ");
                                             
                                             }
                                             continue LAB32;
                                                         
                                         } while(false);
                                     $template167.addStr(" )");
                                     return ((IString)($template167.close()));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_910072_17: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_seq_list_Symbol, 1)){
                                  IValue $arg0_191 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_191.getType(), $T15)){
                                     IList ss_31 = ((IList)($arg0_191));
                                     ITuple $TMP186 = (ITuple)(M_List.takeOneFrom(((IList)($arg0_191))));
                                     IConstructor f_32 = ((IConstructor)($atuple_subscript_int(((ITuple)($TMP186)),0)));
                                     IList as_33 = ((IList)($atuple_subscript_int(((ITuple)($TMP186)),1)));
                                     IString $reducer188 = (IString)($me.symbol2rascal(((IConstructor)f_32)));
                                     $REDUCER187_GEN9012:
                                     for(IValue $elem190_for : ((IList)as_33)){
                                         IConstructor $elem190 = (IConstructor) $elem190_for;
                                         IConstructor a_35 = ((IConstructor)($elem190));
                                         final Template $template189 = (Template)new Template($RVF, "");
                                         $template189.addStr(((IString)($reducer188)).getValue());
                                         $template189.addStr(" ");
                                         $template189.beginIndent(" ");
                                         $template189.addStr(((IString)($me.symbol2rascal(((IConstructor)a_35)))).getValue());
                                         $template189.endIndent(" ");
                                         $reducer188 = ((IString)($template189.close()));
                                     
                                     }
                                     
                                                 return ((IString)($astr_add_astr(((IString)($astr_add_astr(((IString)$constants.get(48)/*"("*/),((IString)($reducer188))))),((IString)$constants.get(49)/*")"*/))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 826203960:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_826203960_13: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_iter_star_Symbol, 1)){
                                  IValue $arg0_175 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_175.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor x_21 = ((IConstructor)($arg0_175));
                                     final Template $template174 = (Template)new Template($RVF, "");
                                     $template174.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_175))))).getValue());
                                     $template174.addStr("*");
                                     return ((IString)($template174.close()));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 773448:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_773448_16: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_alt_set_Symbol, 1)){
                                  IValue $arg0_185 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_185.getType(), $T29)){
                                     ISet alts_26 = ((ISet)($arg0_185));
                                     ITuple $TMP180 = (ITuple)(M_Set.takeOneFrom(((ISet)($arg0_185))));
                                     IConstructor f_27 = ((IConstructor)($atuple_subscript_int(((ITuple)($TMP180)),0)));
                                     ISet as_28 = ((ISet)($atuple_subscript_int(((ITuple)($TMP180)),1)));
                                     IString $reducer182 = (IString)($me.symbol2rascal(((IConstructor)f_27)));
                                     $REDUCER181_GEN8853:
                                     for(IValue $elem184_for : ((ISet)as_28)){
                                         IConstructor $elem184 = (IConstructor) $elem184_for;
                                         IConstructor a_30 = ((IConstructor)($elem184));
                                         final Template $template183 = (Template)new Template($RVF, "");
                                         $template183.addStr(((IString)($reducer182)).getValue());
                                         $template183.addStr(" | ");
                                         $template183.beginIndent("   ");
                                         $template183.addStr(((IString)($me.symbol2rascal(((IConstructor)a_30)))).getValue());
                                         $template183.endIndent("   ");
                                         $reducer182 = ((IString)($template183.close()));
                                     
                                     }
                                     
                                                 return ((IString)($astr_add_astr(((IString)($astr_add_astr(((IString)$constants.get(48)/*"("*/),((IString)($reducer182))))),((IString)$constants.get(49)/*")"*/))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 1652184736:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1652184736_14: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_iter_seps_Symbol_list_Symbol, 2)){
                                  IValue $arg0_177 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_177.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor x_22 = ((IConstructor)($arg0_177));
                                     IValue $arg1_176 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_176.getType(), $T15)){
                                        IList seps_23 = ((IList)($arg1_176));
                                        return ((IString)($me.iterseps2rascal(((IConstructor)($arg0_177)), ((IList)($arg1_176)), ((IString)$constants.get(50)/*"+"*/))));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -333228984:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_333228984_18: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_layouts_str, 1)){
                                  IValue $arg0_192 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_192.getType(), $T0)){
                                     return ((IString)$constants.get(2)/*""*/);
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 0:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    
                }
                
        
            case 857272:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_857272_2: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_lit_str, 1)){
                                  IValue $arg0_148 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_148.getType(), $T0)){
                                     IString x_4 = ((IString)($arg0_148));
                                     final Template $template147 = (Template)new Template($RVF, "\"");
                                     $template147.beginIndent("  ");
                                     $template147.addStr(((IString)(M_lang_rascal_format_Escape.escape(((IString)($arg0_148))))).getValue());
                                     $template147.endIndent("  ");
                                     $template147.addStr("\"");
                                     return ((IString)($template147.close()));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -109773488:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_109773488_5: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_keywords_str, 1)){
                                  IValue $arg0_152 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_152.getType(), $T0)){
                                     IString x_7 = null;
                                     return ((IString)($arg0_152));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 25942208:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_25942208_12: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_iter_Symbol, 1)){
                                  IValue $arg0_173 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_173.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor x_20 = ((IConstructor)($arg0_173));
                                     final Template $template172 = (Template)new Template($RVF, "");
                                     $template172.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_173))))).getValue());
                                     $template172.addStr("+");
                                     return ((IString)($template172.close()));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 878060304:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_878060304_19: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_start_Symbol, 1)){
                                  IValue $arg0_193 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_193.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor x_36 = ((IConstructor)($arg0_193));
                                     return ((IString)($me.symbol2rascal(((IConstructor)($arg0_193)))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 882072:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_882072_11: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_opt_Symbol, 1)){
                                  IValue $arg0_171 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_171.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor x_19 = ((IConstructor)($arg0_171));
                                     final Template $template170 = (Template)new Template($RVF, "");
                                     $template170.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_171))))).getValue());
                                     $template170.addStr("?");
                                     return ((IString)($template170.close()));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 28290288:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_28290288_1: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_sort_str, 1)){
                                  IValue $arg0_146 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_146.getType(), $T0)){
                                     IString x_3 = null;
                                     return ((IString)($arg0_146));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -1948270072:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1948270072_9: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                                  IValue $arg0_166 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_166.getType(), $T13)){
                                     IList x_13 = ((IList)($arg0_166));
                                     final IConstructor $subject_val164 = ((IConstructor)(M_lang_rascal_grammar_definition_Characters.complement(((IConstructor)sym_0))));
                                     if($has_type_and_arity($subject_val164, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                                        IValue $arg0_165 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val164)),0));
                                        if($isComparable($arg0_165.getType(), $T13)){
                                           IList y_14 = ((IList)($arg0_165));
                                           IString norm_15 = ((IString)($me.cc2rascal(((IList)($arg0_166)))));
                                           IString comp_16 = ((IString)($me.cc2rascal(((IList)($arg0_165)))));
                                           /*muExists*/$RET162: 
                                               do {
                                                   if((((IBool)($aint_lessequal_aint(((IInteger)(M_String.size(((IString)norm_15)))),((IInteger)(M_String.size(((IString)comp_16))))).not()))).getValue()){
                                                      final Template $template163 = (Template)new Template($RVF, "!");
                                                      $template163.beginIndent(" ");
                                                      $template163.addStr(((IString)comp_16).getValue());
                                                      $template163.endIndent(" ");
                                                      return ((IString)($template163.close()));
                                                   
                                                   }
                                           
                                               } while(false);
                                           return ((IString)norm_15);
                                        
                                        } else {
                                           throw new Throw(((IString)$constants.get(51)/*"weird result of character class complement"*/));
                                        }
                                     } else {
                                        throw new Throw(((IString)$constants.get(51)/*"weird result of character class complement"*/));
                                     }
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case -2144737184:
                if(noCaseMatched_$switchVal142){
                    noCaseMatched_$switchVal142 = false;
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_2144737184_20: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
                                  IValue $arg0_204 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_204.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_37 = ((IConstructor)($arg0_204));
                                     IValue $arg1_195 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_195.getType(), $T30)){
                                        ISet $subject196 = (ISet)($arg1_195);
                                        if(((ISet)($subject196)).size() >= 1){
                                           CASE_2144737184_20_CONS_conditional_SET_VARc:
                                           for(IValue $elem203_for : ((ISet)($subject196))){
                                               IConstructor $elem203 = (IConstructor) $elem203_for;
                                               IConstructor c_38 = ((IConstructor)($elem203));
                                               final ISet $subject198 = ((ISet)(((ISet)($subject196)).delete(((IConstructor)c_38))));
                                               if(((ISet)($subject198)).size() >= 1){
                                                  CASE_2144737184_20_CONS_conditional_SET_VARc_VARd:
                                                  for(IValue $elem202_for : ((ISet)($subject198))){
                                                      IConstructor $elem202 = (IConstructor) $elem202_for;
                                                      IConstructor d_39 = ((IConstructor)($elem202));
                                                      final ISet $subject199 = ((ISet)(((ISet)($subject198)).delete(((IConstructor)d_39))));
                                                      CASE_2144737184_20_CONS_conditional_SET_VARc_VARd_MVARr:
                                                      for(IValue $elem201_for : new SubSetGenerator(((ISet)($subject199)))){
                                                          ISet $elem201 = (ISet) $elem201_for;
                                                          ISet r_40 = ((ISet)($elem201));
                                                          final ISet $subject200 = ((ISet)(((ISet)($subject199)).subtract(((ISet)($elem201)))));
                                                          if(((ISet)($subject200)).size() == 0){
                                                             final ISetWriter $writer194 = (ISetWriter)$RVF.setWriter();
                                                             ;
                                                             $writer194.insert(d_39);
                                                             $setwriter_splice($writer194,r_40);
                                                             return ((IString)($me.symbol2rascal(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_conditional_Symbol_set_Condition, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_conditional_Symbol_set_Condition, new IValue[]{((IConstructor)($arg0_204)), ((ISet)($RVF.set(((IConstructor)c_38))))}))), ((ISet)($writer194.done()))}))))));
                                                          
                                                          } else {
                                                             continue CASE_2144737184_20_CONS_conditional_SET_VARc_VARd_MVARr;/*set pat3*/
                                                          }
                                                      }
                                                      continue CASE_2144737184_20_CONS_conditional_SET_VARc_VARd;/*set pat4*/
                                                                  
                                                  }
                                                  continue CASE_2144737184_20_CONS_conditional_SET_VARc;/*set pat4*/
                                                              
                                               } else {
                                                  continue CASE_2144737184_20_CONS_conditional_SET_VARc;/*set pat4*/
                                               }
                                           }
                                           
                                                       
                                        }
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_2144737184_21: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
                                  IValue $arg0_213 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_213.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_41 = ((IConstructor)($arg0_213));
                                     IValue $arg1_206 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_206.getType(), $T30)){
                                        ISet $subject207 = (ISet)($arg1_206);
                                        CASE_2144737184_21_CONS_conditional_SET_CONS_delete$_DFLT_SET_ELM211:
                                        for(IValue $elem210_for : ((ISet)($subject207))){
                                            IConstructor $elem210 = (IConstructor) $elem210_for;
                                            if($has_type_and_arity($elem210, M_ParseTree.Condition_delete_Symbol, 1)){
                                               IValue $arg0_212 = (IValue)($aadt_subscript_int(((IConstructor)($elem210)),0));
                                               if($isComparable($arg0_212.getType(), M_ParseTree.ADT_Symbol)){
                                                  IConstructor t_42 = ((IConstructor)($arg0_212));
                                                  final ISet $subject209 = ((ISet)(((ISet)($subject207)).delete(((IConstructor)($elem210)))));
                                                  if(((ISet)($subject209)).size() == 0){
                                                     final Template $template205 = (Template)new Template($RVF, "");
                                                     $template205.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_213))))).getValue());
                                                     $template205.addStr(" \\ ");
                                                     $template205.beginIndent("    ");
                                                     $template205.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_212))))).getValue());
                                                     $template205.endIndent("    ");
                                                     return ((IString)($template205.close()));
                                                  
                                                  } else {
                                                     continue CASE_2144737184_21_CONS_conditional_SET_CONS_delete$_DFLT_SET_ELM211;/*redirected CASE_2144737184_21_CONS_conditional_SET_CONS_delete to CASE_2144737184_21_CONS_conditional_SET_CONS_delete$_DFLT_SET_ELM211; set pat3*/
                                                  }
                                               } else {
                                                  continue CASE_2144737184_21_CONS_conditional_SET_CONS_delete$_DFLT_SET_ELM211;/*default set elem*/
                                               }
                                            } else {
                                               continue CASE_2144737184_21_CONS_conditional_SET_CONS_delete$_DFLT_SET_ELM211;/*default set elem*/
                                            }
                                        }
                                        
                                                    
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_2144737184_22: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
                                  IValue $arg0_222 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_222.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_43 = ((IConstructor)($arg0_222));
                                     IValue $arg1_215 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_215.getType(), $T30)){
                                        ISet $subject216 = (ISet)($arg1_215);
                                        CASE_2144737184_22_CONS_conditional_SET_CONS_follow$_DFLT_SET_ELM220:
                                        for(IValue $elem219_for : ((ISet)($subject216))){
                                            IConstructor $elem219 = (IConstructor) $elem219_for;
                                            if($has_type_and_arity($elem219, M_ParseTree.Condition_follow_Symbol, 1)){
                                               IValue $arg0_221 = (IValue)($aadt_subscript_int(((IConstructor)($elem219)),0));
                                               if($isComparable($arg0_221.getType(), M_ParseTree.ADT_Symbol)){
                                                  IConstructor t_44 = ((IConstructor)($arg0_221));
                                                  final ISet $subject218 = ((ISet)(((ISet)($subject216)).delete(((IConstructor)($elem219)))));
                                                  if(((ISet)($subject218)).size() == 0){
                                                     final Template $template214 = (Template)new Template($RVF, "");
                                                     $template214.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_222))))).getValue());
                                                     $template214.addStr(" >> ");
                                                     $template214.beginIndent("      ");
                                                     $template214.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_221))))).getValue());
                                                     $template214.endIndent("      ");
                                                     return ((IString)($template214.close()));
                                                  
                                                  } else {
                                                     continue CASE_2144737184_22_CONS_conditional_SET_CONS_follow$_DFLT_SET_ELM220;/*redirected CASE_2144737184_22_CONS_conditional_SET_CONS_follow to CASE_2144737184_22_CONS_conditional_SET_CONS_follow$_DFLT_SET_ELM220; set pat3*/
                                                  }
                                               } else {
                                                  continue CASE_2144737184_22_CONS_conditional_SET_CONS_follow$_DFLT_SET_ELM220;/*default set elem*/
                                               }
                                            } else {
                                               continue CASE_2144737184_22_CONS_conditional_SET_CONS_follow$_DFLT_SET_ELM220;/*default set elem*/
                                            }
                                        }
                                        
                                                    
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_2144737184_23: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
                                  IValue $arg0_231 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_231.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_45 = ((IConstructor)($arg0_231));
                                     IValue $arg1_224 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_224.getType(), $T30)){
                                        ISet $subject225 = (ISet)($arg1_224);
                                        CASE_2144737184_23_CONS_conditional_SET_CONS_not_follow$_DFLT_SET_ELM229:
                                        for(IValue $elem228_for : ((ISet)($subject225))){
                                            IConstructor $elem228 = (IConstructor) $elem228_for;
                                            if($has_type_and_arity($elem228, M_ParseTree.Condition_not_follow_Symbol, 1)){
                                               IValue $arg0_230 = (IValue)($aadt_subscript_int(((IConstructor)($elem228)),0));
                                               if($isComparable($arg0_230.getType(), M_ParseTree.ADT_Symbol)){
                                                  IConstructor t_46 = ((IConstructor)($arg0_230));
                                                  final ISet $subject227 = ((ISet)(((ISet)($subject225)).delete(((IConstructor)($elem228)))));
                                                  if(((ISet)($subject227)).size() == 0){
                                                     final Template $template223 = (Template)new Template($RVF, "");
                                                     $template223.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_231))))).getValue());
                                                     $template223.addStr(" !>> ");
                                                     $template223.beginIndent("       ");
                                                     $template223.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_230))))).getValue());
                                                     $template223.endIndent("       ");
                                                     return ((IString)($template223.close()));
                                                  
                                                  } else {
                                                     continue CASE_2144737184_23_CONS_conditional_SET_CONS_not_follow$_DFLT_SET_ELM229;/*redirected CASE_2144737184_23_CONS_conditional_SET_CONS_not_follow to CASE_2144737184_23_CONS_conditional_SET_CONS_not_follow$_DFLT_SET_ELM229; set pat3*/
                                                  }
                                               } else {
                                                  continue CASE_2144737184_23_CONS_conditional_SET_CONS_not_follow$_DFLT_SET_ELM229;/*default set elem*/
                                               }
                                            } else {
                                               continue CASE_2144737184_23_CONS_conditional_SET_CONS_not_follow$_DFLT_SET_ELM229;/*default set elem*/
                                            }
                                        }
                                        
                                                    
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_2144737184_24: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
                                  IValue $arg0_240 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_240.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_47 = ((IConstructor)($arg0_240));
                                     IValue $arg1_233 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_233.getType(), $T30)){
                                        ISet $subject234 = (ISet)($arg1_233);
                                        CASE_2144737184_24_CONS_conditional_SET_CONS_precede$_DFLT_SET_ELM238:
                                        for(IValue $elem237_for : ((ISet)($subject234))){
                                            IConstructor $elem237 = (IConstructor) $elem237_for;
                                            if($has_type_and_arity($elem237, M_ParseTree.Condition_precede_Symbol, 1)){
                                               IValue $arg0_239 = (IValue)($aadt_subscript_int(((IConstructor)($elem237)),0));
                                               if($isComparable($arg0_239.getType(), M_ParseTree.ADT_Symbol)){
                                                  IConstructor t_48 = ((IConstructor)($arg0_239));
                                                  final ISet $subject236 = ((ISet)(((ISet)($subject234)).delete(((IConstructor)($elem237)))));
                                                  if(((ISet)($subject236)).size() == 0){
                                                     final Template $template232 = (Template)new Template($RVF, "");
                                                     $template232.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_239))))).getValue());
                                                     $template232.addStr(" << ");
                                                     $template232.beginIndent("      ");
                                                     $template232.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_240))))).getValue());
                                                     $template232.endIndent("      ");
                                                     $template232.addStr(" ");
                                                     return ((IString)($template232.close()));
                                                  
                                                  } else {
                                                     continue CASE_2144737184_24_CONS_conditional_SET_CONS_precede$_DFLT_SET_ELM238;/*redirected CASE_2144737184_24_CONS_conditional_SET_CONS_precede to CASE_2144737184_24_CONS_conditional_SET_CONS_precede$_DFLT_SET_ELM238; set pat3*/
                                                  }
                                               } else {
                                                  continue CASE_2144737184_24_CONS_conditional_SET_CONS_precede$_DFLT_SET_ELM238;/*default set elem*/
                                               }
                                            } else {
                                               continue CASE_2144737184_24_CONS_conditional_SET_CONS_precede$_DFLT_SET_ELM238;/*default set elem*/
                                            }
                                        }
                                        
                                                    
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_2144737184_25: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
                                  IValue $arg0_249 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_249.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_49 = ((IConstructor)($arg0_249));
                                     IValue $arg1_242 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_242.getType(), $T30)){
                                        ISet $subject243 = (ISet)($arg1_242);
                                        CASE_2144737184_25_CONS_conditional_SET_CONS_not_precede$_DFLT_SET_ELM247:
                                        for(IValue $elem246_for : ((ISet)($subject243))){
                                            IConstructor $elem246 = (IConstructor) $elem246_for;
                                            if($has_type_and_arity($elem246, M_ParseTree.Condition_not_precede_Symbol, 1)){
                                               IValue $arg0_248 = (IValue)($aadt_subscript_int(((IConstructor)($elem246)),0));
                                               if($isComparable($arg0_248.getType(), M_ParseTree.ADT_Symbol)){
                                                  IConstructor t_50 = ((IConstructor)($arg0_248));
                                                  final ISet $subject245 = ((ISet)(((ISet)($subject243)).delete(((IConstructor)($elem246)))));
                                                  if(((ISet)($subject245)).size() == 0){
                                                     final Template $template241 = (Template)new Template($RVF, "");
                                                     $template241.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_248))))).getValue());
                                                     $template241.addStr(" !<< ");
                                                     $template241.beginIndent("       ");
                                                     $template241.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_249))))).getValue());
                                                     $template241.endIndent("       ");
                                                     $template241.addStr(" ");
                                                     return ((IString)($template241.close()));
                                                  
                                                  } else {
                                                     continue CASE_2144737184_25_CONS_conditional_SET_CONS_not_precede$_DFLT_SET_ELM247;/*redirected CASE_2144737184_25_CONS_conditional_SET_CONS_not_precede to CASE_2144737184_25_CONS_conditional_SET_CONS_not_precede$_DFLT_SET_ELM247; set pat3*/
                                                  }
                                               } else {
                                                  continue CASE_2144737184_25_CONS_conditional_SET_CONS_not_precede$_DFLT_SET_ELM247;/*default set elem*/
                                               }
                                            } else {
                                               continue CASE_2144737184_25_CONS_conditional_SET_CONS_not_precede$_DFLT_SET_ELM247;/*default set elem*/
                                            }
                                        }
                                        
                                                    
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_2144737184_26: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
                                  IValue $arg0_258 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_258.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_51 = ((IConstructor)($arg0_258));
                                     IValue $arg1_251 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_251.getType(), $T30)){
                                        ISet $subject252 = (ISet)($arg1_251);
                                        CASE_2144737184_26_CONS_conditional_SET_CONS_at_column$_DFLT_SET_ELM256:
                                        for(IValue $elem255_for : ((ISet)($subject252))){
                                            IConstructor $elem255 = (IConstructor) $elem255_for;
                                            if($has_type_and_arity($elem255, M_ParseTree.Condition_at_column_int, 1)){
                                               IValue $arg0_257 = (IValue)($aadt_subscript_int(((IConstructor)($elem255)),0));
                                               if($isComparable($arg0_257.getType(), $T31)){
                                                  if(true){
                                                     IInteger i_52 = ((IInteger)($arg0_257));
                                                     final ISet $subject254 = ((ISet)(((ISet)($subject252)).delete(((IConstructor)($elem255)))));
                                                     if(((ISet)($subject254)).size() == 0){
                                                        final Template $template250 = (Template)new Template($RVF, "");
                                                        $template250.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_258))))).getValue());
                                                        $template250.addStr("@");
                                                        $template250.beginIndent(" ");
                                                        $template250.addVal($arg0_257);
                                                        $template250.endIndent(" ");
                                                        return ((IString)($template250.close()));
                                                     
                                                     } else {
                                                        continue CASE_2144737184_26_CONS_conditional_SET_CONS_at_column$_DFLT_SET_ELM256;/*redirected CASE_2144737184_26_CONS_conditional_SET_CONS_at_column to CASE_2144737184_26_CONS_conditional_SET_CONS_at_column$_DFLT_SET_ELM256; set pat3*/
                                                     }
                                                  } else {
                                                     continue CASE_2144737184_26_CONS_conditional_SET_CONS_at_column$_DFLT_SET_ELM256;/*default set elem*/
                                                  }
                                               } else {
                                                  continue CASE_2144737184_26_CONS_conditional_SET_CONS_at_column$_DFLT_SET_ELM256;/*default set elem*/
                                               }
                                            } else {
                                               continue CASE_2144737184_26_CONS_conditional_SET_CONS_at_column$_DFLT_SET_ELM256;/*default set elem*/
                                            }
                                        }
                                        
                                                    
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_2144737184_27: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
                                  IValue $arg0_266 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_266.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_53 = ((IConstructor)($arg0_266));
                                     IValue $arg1_260 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_260.getType(), $T30)){
                                        ISet $subject261 = (ISet)($arg1_260);
                                        CASE_2144737184_27_CONS_conditional_SET_CONS_begin_of_line$_DFLT_SET_ELM265:
                                        for(IValue $elem264_for : ((ISet)($subject261))){
                                            IConstructor $elem264 = (IConstructor) $elem264_for;
                                            if($has_type_and_arity($elem264, M_ParseTree.Condition_begin_of_line_, 0)){
                                               final ISet $subject263 = ((ISet)(((ISet)($subject261)).delete(((IConstructor)($elem264)))));
                                               if(((ISet)($subject263)).size() == 0){
                                                  final Template $template259 = (Template)new Template($RVF, "^");
                                                  $template259.beginIndent(" ");
                                                  $template259.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_266))))).getValue());
                                                  $template259.endIndent(" ");
                                                  return ((IString)($template259.close()));
                                               
                                               } else {
                                                  continue CASE_2144737184_27_CONS_conditional_SET_CONS_begin_of_line$_DFLT_SET_ELM265;/*redirected CASE_2144737184_27_CONS_conditional_SET_CONS_begin_of_line to CASE_2144737184_27_CONS_conditional_SET_CONS_begin_of_line$_DFLT_SET_ELM265; set pat3*/
                                               }
                                            } else {
                                               continue CASE_2144737184_27_CONS_conditional_SET_CONS_begin_of_line$_DFLT_SET_ELM265;/*default set elem*/
                                            }
                                        }
                                        
                                                    
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_2144737184_28: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
                                  IValue $arg0_274 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_274.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_54 = ((IConstructor)($arg0_274));
                                     IValue $arg1_268 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_268.getType(), $T30)){
                                        ISet $subject269 = (ISet)($arg1_268);
                                        CASE_2144737184_28_CONS_conditional_SET_CONS_end_of_line$_DFLT_SET_ELM273:
                                        for(IValue $elem272_for : ((ISet)($subject269))){
                                            IConstructor $elem272 = (IConstructor) $elem272_for;
                                            if($has_type_and_arity($elem272, M_ParseTree.Condition_end_of_line_, 0)){
                                               final ISet $subject271 = ((ISet)(((ISet)($subject269)).delete(((IConstructor)($elem272)))));
                                               if(((ISet)($subject271)).size() == 0){
                                                  final Template $template267 = (Template)new Template($RVF, "");
                                                  $template267.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_274))))).getValue());
                                                  $template267.addStr("$");
                                                  return ((IString)($template267.close()));
                                               
                                               } else {
                                                  continue CASE_2144737184_28_CONS_conditional_SET_CONS_end_of_line$_DFLT_SET_ELM273;/*redirected CASE_2144737184_28_CONS_conditional_SET_CONS_end_of_line to CASE_2144737184_28_CONS_conditional_SET_CONS_end_of_line$_DFLT_SET_ELM273; set pat3*/
                                               }
                                            } else {
                                               continue CASE_2144737184_28_CONS_conditional_SET_CONS_end_of_line$_DFLT_SET_ELM273;/*default set elem*/
                                            }
                                        }
                                        
                                                    
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_2144737184_29: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
                                  IValue $arg0_283 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_283.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_55 = ((IConstructor)($arg0_283));
                                     IValue $arg1_276 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_276.getType(), $T30)){
                                        ISet $subject277 = (ISet)($arg1_276);
                                        CASE_2144737184_29_CONS_conditional_SET_CONS_except$_DFLT_SET_ELM281:
                                        for(IValue $elem280_for : ((ISet)($subject277))){
                                            IConstructor $elem280 = (IConstructor) $elem280_for;
                                            if($has_type_and_arity($elem280, M_ParseTree.Condition_except_str, 1)){
                                               IValue $arg0_282 = (IValue)($aadt_subscript_int(((IConstructor)($elem280)),0));
                                               if($isComparable($arg0_282.getType(), $T0)){
                                                  if(true){
                                                     IString x_56 = ((IString)($arg0_282));
                                                     final ISet $subject279 = ((ISet)(((ISet)($subject277)).delete(((IConstructor)($elem280)))));
                                                     if(((ISet)($subject279)).size() == 0){
                                                        final Template $template275 = (Template)new Template($RVF, "");
                                                        $template275.addStr(((IString)($me.symbol2rascal(((IConstructor)($arg0_283))))).getValue());
                                                        $template275.addStr("!");
                                                        $template275.beginIndent(" ");
                                                        $template275.addStr(((IString)($arg0_282)).getValue());
                                                        $template275.endIndent(" ");
                                                        return ((IString)($template275.close()));
                                                     
                                                     } else {
                                                        continue CASE_2144737184_29_CONS_conditional_SET_CONS_except$_DFLT_SET_ELM281;/*redirected CASE_2144737184_29_CONS_conditional_SET_CONS_except to CASE_2144737184_29_CONS_conditional_SET_CONS_except$_DFLT_SET_ELM281; set pat3*/
                                                     }
                                                  } else {
                                                     continue CASE_2144737184_29_CONS_conditional_SET_CONS_except$_DFLT_SET_ELM281;/*default set elem*/
                                                  }
                                               } else {
                                                  continue CASE_2144737184_29_CONS_conditional_SET_CONS_except$_DFLT_SET_ELM281;/*default set elem*/
                                               }
                                            } else {
                                               continue CASE_2144737184_29_CONS_conditional_SET_CONS_except$_DFLT_SET_ELM281;/*default set elem*/
                                            }
                                        }
                                        
                                                    
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_2144737184_30: 
                           do {
                               if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
                                  IValue $arg0_286 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),0));
                                  if($isComparable($arg0_286.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor s_57 = ((IConstructor)($arg0_286));
                                     IValue $arg1_285 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal142)),1));
                                     if($isComparable($arg1_285.getType(), $T26)){
                                        if($arg1_285.equals(((ISet)$constants.get(11)/*{}*/))){
                                          final Template $template284 = (Template)new Template($RVF, "WARNING: empty conditional ");
                                          $template284.beginIndent("                           ");
                                          $template284.addVal(sym_0);
                                          $template284.endIndent("                           ");
                                          M_IO.println(((IValue)($template284.close())));
                                          return ((IString)($me.symbol2rascal(((IConstructor)($arg0_286)))));
                                        
                                        }
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            default: if($isSubtypeOf($switchVal142.getType(),M_ParseTree.ADT_Symbol)){
                        /*muExists*/CASE_0_31: 
                            do {
                                if($has_type_and_arity($switchVal142, M_ParseTree.Symbol_empty_, 0)){
                                   return ((IString)$constants.get(52)/*"()"*/);
                                
                                }
                        
                            } while(false);
                     
                     }
        
        }
        
                   final Template $template287 = (Template)new Template($RVF, "symbol2rascal: missing case ");
        $template287.beginIndent("                            ");
        $template287.addVal(sym_0);
        $template287.endIndent("                            ");
        throw new Throw($template287.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(10764,270,<290,0>,<296,1>) 
    public IString lang_rascal_format_Grammar_iterseps2rascal$fe1027980573bd82(IConstructor sym_0, IList seps_1, IString iter_2){ 
        
        
        final Template $template288 = (Template)new Template($RVF, "");
        /*muExists*/LAB33: 
            do {
                LAB33_GEN10854:
                for(IValue $elem289_for : ((IList)seps_1)){
                    IConstructor $elem289 = (IConstructor) $elem289_for;
                    IConstructor sp_4 = ((IConstructor)($elem289));
                    ;$template288.addStr(((IString)($me.symbol2rascal(((IConstructor)sp_4)))).getValue());
                
                }
                continue LAB33;
                            
            } while(false);
        IString separators_3 = ((IString)($template288.close()));
        if((((IBool)($equal(((IString)separators_3),((IString)$constants.get(2)/*""*/)).not()))).getValue()){
           final Template $template291 = (Template)new Template($RVF, "{");
           $template291.beginIndent(" ");
           $template291.addStr(((IString)($me.symbol2rascal(((IConstructor)sym_0)))).getValue());
           $template291.endIndent(" ");
           $template291.addStr(" ");
           $template291.beginIndent(" ");
           $template291.addStr(((IString)separators_3).getValue());
           $template291.endIndent(" ");
           $template291.addStr("}");
           $template291.beginIndent(" ");
           $template291.addStr(((IString)iter_2).getValue());
           $template291.endIndent(" ");
           return ((IString)($template291.close()));
        
        } else {
           final Template $template290 = (Template)new Template($RVF, "");
           $template290.addStr(((IString)($me.symbol2rascal(((IConstructor)sym_0)))).getValue());
           ;$template290.addStr(((IString)separators_3).getValue());
           ;$template290.addStr(((IString)iter_2).getValue());
           return ((IString)($template290.close()));
        
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(11036,275,<298,0>,<311,1>) 
    public IString lang_rascal_format_Grammar_params2rascal$f4c1186947f6bb00(IList params_0){ 
        
        
        IInteger len_1 = ((IInteger)(M_List.size(((IList)params_0))));
        if((((IBool)($equal(((IInteger)len_1), ((IInteger)$constants.get(3)/*0*/))))).getValue()){
           return ((IString)$constants.get(2)/*""*/);
        
        }
        if((((IBool)($equal(((IInteger)len_1), ((IInteger)$constants.get(53)/*1*/))))).getValue()){
           return ((IString)($me.symbol2rascal(((IConstructor)($alist_subscript_int(((IList)params_0),0))))));
        
        }
        IString sep_2 = ((IString)$constants.get(2)/*""*/);
        IString res_3 = ((IString)$constants.get(2)/*""*/);
        /*muExists*/FOR37: 
            do {
                FOR37_GEN11215:
                for(IValue $elem292_for : ((IList)params_0)){
                    IConstructor $elem292 = (IConstructor) $elem292_for;
                    IConstructor p_4 = ((IConstructor)($elem292));
                    res_3 = ((IString)($astr_add_astr(((IString)res_3),((IString)($astr_add_astr(((IString)sep_2),((IString)($me.symbol2rascal(((IConstructor)p_4))))))))));
                    sep_2 = ((IString)$constants.get(54)/*", "*/);
                
                }
                continue FOR37;
                            
            } while(false);
        /* void:  muCon([]) */return ((IString)res_3);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(11313,172,<313,0>,<316,1>) 
    public IString lang_rascal_format_Grammar_cc2rascal$0d43214752b1b902(IList ranges_0){ 
        
        
        if((((IBool)($equal(((IList)ranges_0), ((IList)$constants.get(4)/*[]*/))))).getValue()){
           return ((IString)$constants.get(55)/*"[]"*/);
        
        }
        final Template $template293 = (Template)new Template($RVF, "[");
        $template293.beginIndent(" ");
        $template293.addStr(((IString)($me.range2rascal(((IConstructor)(M_List.head(((IList)ranges_0))))))).getValue());
        $template293.endIndent(" ");
        ;/*muExists*/LAB39: 
            do {
                LAB39_GEN11439:
                for(IValue $elem294_for : ((IList)(M_List.tail(((IList)ranges_0))))){
                    IConstructor $elem294 = (IConstructor) $elem294_for;
                    IConstructor r_1 = ((IConstructor)($elem294));
                    $template293.addStr(" ");
                    $template293.beginIndent(" ");
                    $template293.addStr(((IString)($me.range2rascal(((IConstructor)r_1)))).getValue());
                    $template293.endIndent(" ");
                
                }
                continue LAB39;
                            
            } while(false);
        $template293.addStr("]");
        return ((IString)($template293.close()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/format/Grammar.rsc|(11487,315,<318,0>,<327,1>) 
    public IString lang_rascal_format_Grammar_range2rascal$07747f28a4b93d11(IConstructor r_0){ 
        
        
        final IConstructor $switchVal295 = ((IConstructor)r_0);
        boolean noCaseMatched_$switchVal295 = true;
        SWITCH40: switch(Fingerprint.getFingerprint($switchVal295)){
        
            case 1732482000:
                if(noCaseMatched_$switchVal295){
                    noCaseMatched_$switchVal295 = false;
                    if($isSubtypeOf($switchVal295.getType(),M_ParseTree.ADT_CharRange)){
                       /*muExists*/CASE_1732482000_0: 
                           do {
                               if($has_type_and_arity($switchVal295, M_ParseTree.CharRange_range_int_int, 2)){
                                  IValue $arg0_298 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal295)),0));
                                  if($isComparable($arg0_298.getType(), $T31)){
                                     IInteger c_1 = ((IInteger)($arg0_298));
                                     IValue $arg1_297 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal295)),1));
                                     if($isComparable($arg1_297.getType(), $T31)){
                                        if(($arg0_298 != null)){
                                           if($arg0_298.match($arg1_297)){
                                              return ((IString)(M_lang_rascal_format_Escape.makeCharClassChar(((IInteger)($arg1_297)))));
                                           
                                           }
                                        
                                        } else {
                                           $arg0_298 = ((IValue)($arg1_297));
                                           return ((IString)(M_lang_rascal_format_Escape.makeCharClassChar(((IInteger)($arg1_297)))));
                                        
                                        }
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
                    if($isSubtypeOf($switchVal295.getType(),M_ParseTree.ADT_CharRange)){
                       /*muExists*/CASE_1732482000_1: 
                           do {
                               if($has_type_and_arity($switchVal295, M_ParseTree.CharRange_range_int_int, 2)){
                                  IValue $arg0_301 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal295)),0));
                                  if($isComparable($arg0_301.getType(), $T31)){
                                     IInteger c_2 = ((IInteger)($arg0_301));
                                     IValue $arg1_300 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal295)),1));
                                     if($isComparable($arg1_300.getType(), $T31)){
                                        IInteger d_3 = ((IInteger)($arg1_300));
                                        final Template $template299 = (Template)new Template($RVF, "");
                                        $template299.addStr(((IString)(M_lang_rascal_format_Escape.makeCharClassChar(((IInteger)($arg0_301))))).getValue());
                                        $template299.addStr("-");
                                        $template299.beginIndent(" ");
                                        $template299.addStr(((IString)(M_lang_rascal_format_Escape.makeCharClassChar(((IInteger)($arg1_300))))).getValue());
                                        $template299.endIndent(" ");
                                        return ((IString)($template299.close()));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            default: final Template $template296 = (Template)new Template($RVF, "range2rascal: missing case ");
                     $template296.beginIndent("                           ");
                     $template296.addVal(r_0);
                     $template296.endIndent("                           ");
                      
                     throw new Throw($template296.close());
        }
        
                   
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `lang::rascal::format::Grammar`");
    }
}