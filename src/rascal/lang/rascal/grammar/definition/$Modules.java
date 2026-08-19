package rascal.lang.rascal.grammar.definition;
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
public class $Modules 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.lang.rascal.grammar.definition.$Modules_$I {

    private final $Modules_$I $me;
    private final IList $constants;
    
    
    public final rascal.$Set M_Set;
    public final rascal.lang.rascal.syntax.$Rascal M_lang_rascal_syntax_Rascal;
    public final rascal.lang.rascal.grammar.definition.$Layout M_lang_rascal_grammar_definition_Layout;
    public final rascal.$ParseTree M_ParseTree;
    public final rascal.lang.rascal.grammar.definition.$Productions M_lang_rascal_grammar_definition_Productions;
    public final rascal.$Type M_Type;
    public final rascal.$List M_List;
    public final rascal.lang.rascal.grammar.definition.$Names M_lang_rascal_grammar_definition_Names;
    public final rascal.lang.rascal.grammar.definition.$Literals M_lang_rascal_grammar_definition_Literals;
    public final rascal.$Grammar M_Grammar;
    public final rascal.$Message M_Message;

    
    
    public final io.usethesource.vallang.type.Type $T3;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T6;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T11;	/*astr(alabel="name")*/
    public final io.usethesource.vallang.type.Type $T18;	/*aparameter("A",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T7;	/*aparameter("A",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T4;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T1;	/*aset(aadt("Import",[],contextFreeSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T16;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T13;	/*aset(astr(),alabel="extends")*/
    public final io.usethesource.vallang.type.Type ADT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T12;	/*aset(astr(),alabel="imports")*/
    public final io.usethesource.vallang.type.Type ADT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T14;	/*aset(aadt("SyntaxDefinition",[],contextFreeSyntax()),alabel="defs")*/
    public final io.usethesource.vallang.type.Type $T15;	/*atuple(atypeList([aset(astr(),alabel="imports"),aset(astr(),alabel="extends"),aset(aadt("SyntaxDefinition",[],contextFreeSyntax()),alabel="defs")]),alabel="mod")*/
    public final io.usethesource.vallang.type.Type $T10;	/*amap(astr(alabel="name"),atuple(atypeList([aset(astr(),alabel="imports"),aset(astr(),alabel="extends"),aset(aadt("SyntaxDefinition",[],contextFreeSyntax()),alabel="defs")]),alabel="mod"))*/
    public final io.usethesource.vallang.type.Type ADT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Item;	/*aadt("Item",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarModule;	/*aadt("GrammarModule",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T17;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_1;	/*aadt("Maybe",[aparameter("A",avalue(),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarDefinition;	/*aadt("GrammarDefinition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_HexIntegerLiteral;	/*aadt("HexIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_HexIntegerLiteral;	/*aadt("HexIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostStringChars;	/*aadt("PostStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostStringChars;	/*aadt("PostStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TimeZonePart;	/*aadt("TimeZonePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TimeZonePart;	/*aadt("TimeZonePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Declaration;	/*aadt("Declaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Declaration;	/*aadt("Declaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ShellCommand;	/*aadt("ShellCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ShellCommand;	/*aadt("ShellCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T0;	/*start(aadt("Module",[],contextFreeSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T5;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Symbol;	/*aadt("Maybe",[aadt("Symbol",[],dataSyntax())],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Body;	/*aadt("Body",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Body;	/*aadt("Body",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Associativity;	/*aadt("Maybe",[aadt("Associativity",[],dataSyntax(),alabel="a")],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Start;	/*aadt("Start",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Start;	/*aadt("Start",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PrePathChars;	/*aadt("PrePathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PrePathChars;	/*aadt("PrePathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Backslash;	/*aadt("Backslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Backslash;	/*aadt("Backslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringTail;	/*aadt("StringTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringTail;	/*aadt("StringTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T8;	/*aset(aadt("Production",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Attr;	/*aadt("Maybe",[aadt("Attr",[],dataSyntax())],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUTLIST;	/*aadt("LAYOUTLIST",[],layoutSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Grammar;	/*aadt("Grammar",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T19;	/*\iter-star-seps(aadt("Import",[],contextFreeSyntax()),[aadt("LAYOUTLIST",[],layoutSyntax())])*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type Maybe_Symbol_just_Symbol;	/*acons(aadt("Maybe",[aadt("Symbol",[],dataSyntax())],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="val")],[],alabel="just")*/
    public final io.usethesource.vallang.type.Type ADT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T2;	/*aset(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringTemplate;	/*aadt("StringTemplate",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringTemplate;	/*aadt("StringTemplate",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OctalIntegerLiteral;	/*aadt("OctalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OctalIntegerLiteral;	/*aadt("OctalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolPart;	/*aadt("ProtocolPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolPart;	/*aadt("ProtocolPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedBackslash;	/*aadt("NamedBackslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedBackslash;	/*aadt("NamedBackslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Associativity;	/*aadt("Associativity",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RascalKeywords;	/*aadt("RascalKeywords",[],keywordSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comment;	/*aadt("Comment",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comment;	/*aadt("Comment",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Commands;	/*aadt("Commands",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Commands;	/*aadt("Commands",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visit;	/*aadt("Visit",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visit;	/*aadt("Visit",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Command;	/*aadt("Command",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Command;	/*aadt("Command",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StructuredType;	/*aadt("StructuredType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StructuredType;	/*aadt("StructuredType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T9;	/*aset(start(aadt("Module",[],contextFreeSyntax())))*/
    public final io.usethesource.vallang.type.Type ADT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/

    public $Modules(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Modules(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Modules_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.lang.rascal.grammar.definition.$Modules.class, this);
       
       mstore.importModule(rascal.$Set.class, rex, rascal.$Set::new);
       mstore.importModule(rascal.lang.rascal.syntax.$Rascal.class, rex, rascal.lang.rascal.syntax.$Rascal::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Layout.class, rex, rascal.lang.rascal.grammar.definition.$Layout::new);
       mstore.importModule(rascal.$ParseTree.class, rex, rascal.$ParseTree::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Productions.class, rex, rascal.lang.rascal.grammar.definition.$Productions::new);
       mstore.importModule(rascal.$Type.class, rex, rascal.$Type::new);
       mstore.importModule(rascal.$List.class, rex, rascal.$List::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Names.class, rex, rascal.lang.rascal.grammar.definition.$Names::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Literals.class, rex, rascal.lang.rascal.grammar.definition.$Literals::new);
       mstore.importModule(rascal.$Grammar.class, rex, rascal.$Grammar::new);
       mstore.importModule(rascal.$Message.class, rex, rascal.$Message::new); 
       
       M_Set = mstore.getModule(rascal.$Set.class);
       M_lang_rascal_syntax_Rascal = mstore.getModule(rascal.lang.rascal.syntax.$Rascal.class);
       M_lang_rascal_grammar_definition_Layout = mstore.getModule(rascal.lang.rascal.grammar.definition.$Layout.class);
       M_ParseTree = mstore.getModule(rascal.$ParseTree.class);
       M_lang_rascal_grammar_definition_Productions = mstore.getModule(rascal.lang.rascal.grammar.definition.$Productions.class);
       M_Type = mstore.getModule(rascal.$Type.class);
       M_List = mstore.getModule(rascal.$List.class);
       M_lang_rascal_grammar_definition_Names = mstore.getModule(rascal.lang.rascal.grammar.definition.$Names.class);
       M_lang_rascal_grammar_definition_Literals = mstore.getModule(rascal.lang.rascal.grammar.definition.$Literals.class);
       M_Grammar = mstore.getModule(rascal.$Grammar.class);
       M_Message = mstore.getModule(rascal.$Message.class); 
       
                          
       
       $TS.importStore(M_Set.$TS);
       $TS.importStore(M_lang_rascal_syntax_Rascal.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Layout.$TS);
       $TS.importStore(M_ParseTree.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Productions.$TS);
       $TS.importStore(M_Type.$TS);
       $TS.importStore(M_List.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Names.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Literals.$TS);
       $TS.importStore(M_Grammar.$TS);
       $TS.importStore(M_Message.$TS);
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/lang/rascal/grammar/definition/$Modules.constants", 1777, "a2cb44e64d7df9b1b7b28f08bb78e774");
       NT_Visibility = $sort("Visibility");
       ADT_Visibility = $adt("Visibility");
       NT_Replacement = $sort("Replacement");
       ADT_Replacement = $adt("Replacement");
       NT_Name = $lex("Name");
       ADT_Name = $adt("Name");
       NT_PostProtocolChars = $lex("PostProtocolChars");
       ADT_PostProtocolChars = $adt("PostProtocolChars");
       NT_FunctionType = $sort("FunctionType");
       ADT_FunctionType = $adt("FunctionType");
       NT_Strategy = $sort("Strategy");
       ADT_Strategy = $adt("Strategy");
       NT_Import = $sort("Import");
       ADT_Import = $adt("Import");
       NT_MidStringChars = $lex("MidStringChars");
       ADT_MidStringChars = $adt("MidStringChars");
       NT_Variable = $sort("Variable");
       ADT_Variable = $adt("Variable");
       ADT_Tree = $adt("Tree");
       NT_Range = $sort("Range");
       ADT_Range = $adt("Range");
       NT_Pattern = $sort("Pattern");
       ADT_Pattern = $adt("Pattern");
       ADT_LocationChangeType = $adt("LocationChangeType");
       NT_ProtocolChars = $lex("ProtocolChars");
       ADT_ProtocolChars = $adt("ProtocolChars");
       NT_TagString = $lex("TagString");
       ADT_TagString = $adt("TagString");
       NT_SyntaxDefinition = $sort("SyntaxDefinition");
       ADT_SyntaxDefinition = $adt("SyntaxDefinition");
       NT_Concrete = $lex("Concrete");
       ADT_Concrete = $adt("Concrete");
       ADT_IOCapability = $adt("IOCapability");
       NT_RegExpLiteral = $lex("RegExpLiteral");
       ADT_RegExpLiteral = $adt("RegExpLiteral");
       ADT_Item = $adt("Item");
       ADT_GrammarModule = $adt("GrammarModule");
       NT_IntegerLiteral = $sort("IntegerLiteral");
       ADT_IntegerLiteral = $adt("IntegerLiteral");
       NT_Declarator = $sort("Declarator");
       ADT_Declarator = $adt("Declarator");
       NT_Target = $sort("Target");
       ADT_Target = $adt("Target");
       NT_ModuleParameters = $sort("ModuleParameters");
       ADT_ModuleParameters = $adt("ModuleParameters");
       NT_DatePart = $lex("DatePart");
       ADT_DatePart = $adt("DatePart");
       NT_Output = $lex("Output");
       ADT_Output = $adt("Output");
       NT_RationalLiteral = $lex("RationalLiteral");
       ADT_RationalLiteral = $adt("RationalLiteral");
       NT_BooleanLiteral = $lex("BooleanLiteral");
       ADT_BooleanLiteral = $adt("BooleanLiteral");
       NT_LocalVariableDeclaration = $sort("LocalVariableDeclaration");
       ADT_LocalVariableDeclaration = $adt("LocalVariableDeclaration");
       ADT_Attr = $adt("Attr");
       NT_Expression = $sort("Expression");
       ADT_Expression = $adt("Expression");
       NT_LAYOUT = $lex("LAYOUT");
       ADT_LAYOUT = $adt("LAYOUT");
       NT_TimePartNoTZ = $lex("TimePartNoTZ");
       ADT_TimePartNoTZ = $adt("TimePartNoTZ");
       NT_ImportedModule = $sort("ImportedModule");
       ADT_ImportedModule = $adt("ImportedModule");
       NT_Case = $sort("Case");
       ADT_Case = $adt("Case");
       NT_KeywordFormals = $sort("KeywordFormals");
       ADT_KeywordFormals = $adt("KeywordFormals");
       NT_DataTarget = $sort("DataTarget");
       ADT_DataTarget = $adt("DataTarget");
       NT_Renaming = $sort("Renaming");
       ADT_Renaming = $adt("Renaming");
       ADT_Production = $adt("Production");
       NT_Renamings = $sort("Renamings");
       ADT_Renamings = $adt("Renamings");
       NT_KeywordFormal = $sort("KeywordFormal");
       ADT_KeywordFormal = $adt("KeywordFormal");
       NT_OptionalExpression = $sort("OptionalExpression");
       ADT_OptionalExpression = $adt("OptionalExpression");
       ADT_GrammarDefinition = $adt("GrammarDefinition");
       NT_Catch = $sort("Catch");
       ADT_Catch = $adt("Catch");
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
       NT_HexIntegerLiteral = $lex("HexIntegerLiteral");
       ADT_HexIntegerLiteral = $adt("HexIntegerLiteral");
       NT_PostStringChars = $lex("PostStringChars");
       ADT_PostStringChars = $adt("PostStringChars");
       NT_TimeZonePart = $lex("TimeZonePart");
       ADT_TimeZonePart = $adt("TimeZonePart");
       NT_Declaration = $sort("Declaration");
       ADT_Declaration = $adt("Declaration");
       NT_ShellCommand = $sort("ShellCommand");
       ADT_ShellCommand = $adt("ShellCommand");
       ADT_Symbol = $adt("Symbol");
       NT_PreStringChars = $lex("PreStringChars");
       ADT_PreStringChars = $adt("PreStringChars");
       NT_Nonterminal = $lex("Nonterminal");
       ADT_Nonterminal = $adt("Nonterminal");
       NT_StringLiteral = $sort("StringLiteral");
       ADT_StringLiteral = $adt("StringLiteral");
       NT_Module = $sort("Module");
       ADT_Module = $adt("Module");
       NT_TypeArg = $sort("TypeArg");
       ADT_TypeArg = $adt("TypeArg");
       NT_NonterminalLabel = $lex("NonterminalLabel");
       ADT_NonterminalLabel = $adt("NonterminalLabel");
       NT_Bound = $sort("Bound");
       ADT_Bound = $adt("Bound");
       NT_RegExp = $lex("RegExp");
       ADT_RegExp = $adt("RegExp");
       NT_Class = $sort("Class");
       ADT_Class = $adt("Class");
       ADT_Condition = $adt("Condition");
       NT_PathPart = $sort("PathPart");
       ADT_PathPart = $adt("PathPart");
       NT_Signature = $sort("Signature");
       ADT_Signature = $adt("Signature");
       NT_Tags = $sort("Tags");
       ADT_Tags = $adt("Tags");
       NT_ModuleActuals = $sort("ModuleActuals");
       ADT_ModuleActuals = $adt("ModuleActuals");
       NT_URLChars = $lex("URLChars");
       ADT_URLChars = $adt("URLChars");
       NT_Body = $sort("Body");
       ADT_Body = $adt("Body");
       NT_Start = $sort("Start");
       ADT_Start = $adt("Start");
       NT_OptionalComma = $lex("OptionalComma");
       ADT_OptionalComma = $adt("OptionalComma");
       NT_PrePathChars = $lex("PrePathChars");
       ADT_PrePathChars = $adt("PrePathChars");
       NT_StringConstant = $lex("StringConstant");
       ADT_StringConstant = $adt("StringConstant");
       NT_Backslash = $lex("Backslash");
       ADT_Backslash = $adt("Backslash");
       NT_DateAndTime = $lex("DateAndTime");
       ADT_DateAndTime = $adt("DateAndTime");
       NT_StringTail = $sort("StringTail");
       ADT_StringTail = $adt("StringTail");
       NT_CaseInsensitiveStringConstant = $lex("CaseInsensitiveStringConstant");
       ADT_CaseInsensitiveStringConstant = $adt("CaseInsensitiveStringConstant");
       NT_Char = $lex("Char");
       ADT_Char = $adt("Char");
       NT_Assignment = $sort("Assignment");
       ADT_Assignment = $adt("Assignment");
       NT_Header = $sort("Header");
       ADT_Header = $adt("Header");
       ADT_Exception = $adt("Exception");
       NT_PatternWithAction = $sort("PatternWithAction");
       ADT_PatternWithAction = $adt("PatternWithAction");
       NT_PostPathChars = $lex("PostPathChars");
       ADT_PostPathChars = $adt("PostPathChars");
       NT_FunctionDeclaration = $sort("FunctionDeclaration");
       ADT_FunctionDeclaration = $adt("FunctionDeclaration");
       NT_DataTypeSelector = $sort("DataTypeSelector");
       ADT_DataTypeSelector = $adt("DataTypeSelector");
       NT_JustDate = $lex("JustDate");
       ADT_JustDate = $adt("JustDate");
       NT_TypeVar = $sort("TypeVar");
       ADT_TypeVar = $adt("TypeVar");
       NT_Variant = $sort("Variant");
       ADT_Variant = $adt("Variant");
       NT_UserType = $sort("UserType");
       ADT_UserType = $adt("UserType");
       NT_FunctionModifiers = $sort("FunctionModifiers");
       ADT_FunctionModifiers = $adt("FunctionModifiers");
       NT_Comprehension = $sort("Comprehension");
       ADT_Comprehension = $adt("Comprehension");
       ADT_LAYOUTLIST = $layouts("LAYOUTLIST");
       NT_ConcreteHole = $sort("ConcreteHole");
       ADT_ConcreteHole = $adt("ConcreteHole");
       ADT_Grammar = $adt("Grammar");
       ADT_Message = $adt("Message");
       NT_StringMiddle = $sort("StringMiddle");
       ADT_StringMiddle = $adt("StringMiddle");
       NT_Sym = $sort("Sym");
       ADT_Sym = $adt("Sym");
       NT_RealLiteral = $lex("RealLiteral");
       ADT_RealLiteral = $adt("RealLiteral");
       ADT_LocationType = $adt("LocationType");
       NT_Formals = $sort("Formals");
       ADT_Formals = $adt("Formals");
       ADT_CharRange = $adt("CharRange");
       NT_QualifiedName = $sort("QualifiedName");
       ADT_QualifiedName = $adt("QualifiedName");
       NT_MidPathChars = $lex("MidPathChars");
       ADT_MidPathChars = $adt("MidPathChars");
       NT_Parameters = $sort("Parameters");
       ADT_Parameters = $adt("Parameters");
       NT_MidProtocolChars = $lex("MidProtocolChars");
       ADT_MidProtocolChars = $adt("MidProtocolChars");
       NT_PathTail = $sort("PathTail");
       ADT_PathTail = $adt("PathTail");
       NT_PreProtocolChars = $lex("PreProtocolChars");
       ADT_PreProtocolChars = $adt("PreProtocolChars");
       NT_StringTemplate = $sort("StringTemplate");
       ADT_StringTemplate = $adt("StringTemplate");
       NT_OctalIntegerLiteral = $lex("OctalIntegerLiteral");
       ADT_OctalIntegerLiteral = $adt("OctalIntegerLiteral");
       NT_ProtocolPart = $sort("ProtocolPart");
       ADT_ProtocolPart = $adt("ProtocolPart");
       NT_NamedBackslash = $lex("NamedBackslash");
       ADT_NamedBackslash = $adt("NamedBackslash");
       NT_Label = $sort("Label");
       ADT_Label = $adt("Label");
       NT_Kind = $sort("Kind");
       ADT_Kind = $adt("Kind");
       NT_RegExpModifier = $lex("RegExpModifier");
       ADT_RegExpModifier = $adt("RegExpModifier");
       NT_Prod = $sort("Prod");
       ADT_Prod = $adt("Prod");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       ADT_Associativity = $adt("Associativity");
       NT_NamedRegExp = $lex("NamedRegExp");
       ADT_NamedRegExp = $adt("NamedRegExp");
       ADT_RascalKeywords = $keywords("RascalKeywords");
       ADT_RuntimeException = $adt("RuntimeException");
       NT_DecimalIntegerLiteral = $lex("DecimalIntegerLiteral");
       ADT_DecimalIntegerLiteral = $adt("DecimalIntegerLiteral");
       NT_Assoc = $sort("Assoc");
       ADT_Assoc = $adt("Assoc");
       NT_EvalCommand = $sort("EvalCommand");
       ADT_EvalCommand = $adt("EvalCommand");
       NT_PathChars = $lex("PathChars");
       ADT_PathChars = $adt("PathChars");
       NT_FunctionModifier = $sort("FunctionModifier");
       ADT_FunctionModifier = $adt("FunctionModifier");
       NT_ProdModifier = $sort("ProdModifier");
       ADT_ProdModifier = $adt("ProdModifier");
       NT_JustTime = $lex("JustTime");
       ADT_JustTime = $adt("JustTime");
       NT_UnicodeEscape = $lex("UnicodeEscape");
       ADT_UnicodeEscape = $adt("UnicodeEscape");
       NT_StringCharacter = $lex("StringCharacter");
       ADT_StringCharacter = $adt("StringCharacter");
       NT_Statement = $sort("Statement");
       ADT_Statement = $adt("Statement");
       NT_ConcretePart = $lex("ConcretePart");
       ADT_ConcretePart = $adt("ConcretePart");
       NT_Literal = $sort("Literal");
       ADT_Literal = $adt("Literal");
       NT_Comment = $lex("Comment");
       ADT_Comment = $adt("Comment");
       NT_Commands = $sort("Commands");
       ADT_Commands = $adt("Commands");
       NT_Visit = $sort("Visit");
       ADT_Visit = $adt("Visit");
       NT_Command = $sort("Command");
       ADT_Command = $adt("Command");
       NT_StructuredType = $sort("StructuredType");
       ADT_StructuredType = $adt("StructuredType");
       NT_DateTimeLiteral = $sort("DateTimeLiteral");
       ADT_DateTimeLiteral = $adt("DateTimeLiteral");
       NT_Assignable = $sort("Assignable");
       ADT_Assignable = $adt("Assignable");
       NT_Toplevel = $sort("Toplevel");
       ADT_Toplevel = $adt("Toplevel");
       NT_CommonKeywordParameters = $sort("CommonKeywordParameters");
       ADT_CommonKeywordParameters = $adt("CommonKeywordParameters");
       NT_BasicType = $sort("BasicType");
       ADT_BasicType = $adt("BasicType");
       NT_ProtocolTail = $sort("ProtocolTail");
       ADT_ProtocolTail = $adt("ProtocolTail");
       $T3 = $TF.valueType();
       $T6 = $TF.stringType();
       $T11 = $TF.stringType();
       $T18 = $TF.parameterType("A", $T3);
       $T7 = $TF.parameterType("A", $T3);
       $T4 = $TF.parameterType("T", $T3);
       $T1 = $TF.setType(NT_Import);
       $T16 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_1 = $parameterizedSort("KeywordArguments", new Type[] { $T16 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       $T13 = $TF.setType($T6);
       NT_Mapping_Pattern = $parameterizedSort("Mapping", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       $T12 = $TF.setType($T6);
       $T14 = $TF.setType(NT_SyntaxDefinition);
       $T15 = $TF.tupleType($T12, $T13, $T14);
       $T10 = $TF.mapType($T11, "name", $T15, "mod");
       $T17 = $TF.parameterType("T", ADT_Tree);
       NT_Mapping_Expression = $parameterizedSort("Mapping", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       NT_KeywordArguments_Expression = $parameterizedSort("KeywordArguments", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       NT_Mapping_1 = $parameterizedSort("Mapping", new Type[] { $T16 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       ADT_Maybe_1 = $parameterizedAdt("Maybe", new Type[] { $T18 });
       $T0 = $TF.fromSymbol($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Module")), $TS, p -> Collections.emptySet());
       NT_KeywordArguments_Pattern = $parameterizedSort("KeywordArguments", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       NT_KeywordArgument_1 = $parameterizedSort("KeywordArgument", new Type[] { $T16 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       $T5 = $TF.listType($T4);
       ADT_Maybe_Symbol = $parameterizedAdt("Maybe", new Type[] { ADT_Symbol });
       ADT_Maybe_Associativity = $parameterizedAdt("Maybe", new Type[] { ADT_Associativity });
       $T8 = $TF.setType(ADT_Production);
       ADT_Maybe_Attr = $parameterizedAdt("Maybe", new Type[] { ADT_Attr });
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T16 });
       $T19 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_IterStarSeps, $RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Import")), $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")))));
       $T2 = $TF.setType($T4);
       $T9 = $TF.setType($T0);
       ADT_KeywordArguments_1 = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { $T16 });
       ADT_Mapping_Pattern = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Pattern });
       ADT_Mapping_Expression = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Expression });
       ADT_KeywordArguments_Expression = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Expression });
       ADT_Mapping_1 = $TF.abstractDataType($TS, "Mapping", new Type[] { $T16 });
       ADT_KeywordArguments_Pattern = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Pattern });
       ADT_KeywordArgument_1 = $TF.abstractDataType($TS, "KeywordArgument", new Type[] { $T16 });
       Maybe_Symbol_just_Symbol = $TF.constructor($TS, ADT_Maybe_Symbol, "just", M_ParseTree.ADT_Symbol, "val");
    
       
       
    }
    public IList addLabels(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_Type.addLabels($P0, $P1);
    }
    public IBool sameType(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IBool) M_ParseTree.sameType($P0, $P1);
    }
    public IValue head(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.head($P0);
    }
    public IBool isAliasType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isAliasType($P0);
    }
    public IBool isStrType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isStrType($P0);
    }
    public IBool isADTType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isADTType($P0);
    }
    public IConstructor fuse(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_GrammarDefinition)){
         $result = (IConstructor)lang_rascal_grammar_definition_Modules_fuse$9ee7a01b63c7e42d((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor module2grammar(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IConstructor)lang_rascal_grammar_definition_Modules_module2grammar$3c550cf85c31c8d8((ITree) $P0);
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
    public IBool isListType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isListType($P0);
    }
    public IBool isRealType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRealType($P0);
    }
    public IBool isTypeVar(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isTypeVar($P0);
    }
    public IConstructor imports2grammar(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IConstructor)lang_rascal_grammar_definition_Modules_imports2grammar$71cc9c7471b0ea07((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor priority(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_ParseTree.priority($P0, $P1);
    }
    public IBool isNodeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNodeType($P0);
    }
    public IConstructor grammar(IValue $P0){ // Generated by Resolver
       return (IConstructor) M_Grammar.grammar($P0);
    }
    public IConstructor grammar(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_Grammar.grammar($P0, $P1);
    }
    public IBool isReifiedType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isReifiedType($P0);
    }
    public ITuple takeOneFrom(IValue $P0){ // Generated by Resolver
       ITuple $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T2)){
         $result = (ITuple)M_Set.Set_takeOneFrom$291ddec83a7e9a61((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T5)){
         $result = (ITuple)M_List.List_takeOneFrom$48bb3b6062ea97b1((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRelType($P0);
    }
    public INode layouts(IValue $P0){ // Generated by Resolver
       INode $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_GrammarDefinition)){
         $result = (INode)M_lang_rascal_grammar_definition_Layout.lang_rascal_grammar_definition_Layout_layouts$a6e96418ea183c23((IConstructor) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T6)){
         return $RVF.constructor(M_ParseTree.Symbol_layouts_str, new IValue[]{(IString) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor layouts(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IConstructor) M_lang_rascal_grammar_definition_Layout.layouts($P0, $P1, $P2);
    }
    public ISet collect(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ISet)lang_rascal_grammar_definition_Modules_collect$b7817bf58b452605((ITree) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
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
    public IList tail(IValue $P0){ // Generated by Resolver
       return (IList) M_List.tail($P0);
    }
    public IBool isLocType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isLocType($P0);
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
    public IBool isRatType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRatType($P0);
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
    public IConstructor associativity(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Maybe_1) && $isSubtypeOf($P2Type, M_ParseTree.ADT_Production)){
         $result = (IConstructor)M_lang_rascal_grammar_definition_Productions.lang_rascal_grammar_definition_Productions_associativity$09cd814bba935894((IConstructor) $P0, (IConstructor) $P1, (IConstructor) $P2);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Associativity) && $isSubtypeOf($P2Type,$T8)){
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
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Associativity) && $isSubtypeOf($P2Type,$T8)){
         return $RVF.constructor(M_ParseTree.Production_associativity_Symbol_Associativity_set_Production, new IValue[]{(IConstructor) $P0, (IConstructor) $P1, (ISet) $P2});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public ITuple getModuleMetaInf(IValue $P0){ // Generated by Resolver
       ITuple $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ITuple)lang_rascal_grammar_definition_Modules_getModuleMetaInf$57fe6e2936821b52((ITree) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet getModuleSyntaxDefinitions(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (ISet)lang_rascal_grammar_definition_Modules_getModuleSyntaxDefinitions$be12600ac17b4c6d((ITree) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IString deslash(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T6)){
         $result = (IString)lang_rascal_grammar_definition_Modules_deslash$836f5a6d7e3d0227((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor modules2grammar(IValue $P0, IValue $P1){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T6) && $isSubtypeOf($P1Type,$T9)){
         $result = (IConstructor)lang_rascal_grammar_definition_Modules_modules2grammar$ef4b475df081e853((IString) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T6) && $isSubtypeOf($P1Type,$T10)){
         $result = (IConstructor)lang_rascal_grammar_definition_Modules_modules2grammar$4d282ab3aff12876((IString) $P0, (IMap) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isFunctionType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isFunctionType($P0);
    }
    public IValue glb(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_Type.glb($P0, $P1);
    }
    public IConstructor modules2definition(IValue $P0, IValue $P1){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T6) && $isSubtypeOf($P1Type,$T9)){
         $result = (IConstructor)lang_rascal_grammar_definition_Modules_modules2definition$fc81bfed4569bcf7((IString) $P0, (ISet) $P1);
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

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Modules.rsc|(667,735,<19,0>,<31,1>) 
    private final ExpiringFunctionResultCache<IValue> $memo_lang_rascal_grammar_definition_Modules_modules2grammar$4d282ab3aff12876 = new ExpiringFunctionResultCache<IValue>(-1, -1);
    public IConstructor lang_rascal_grammar_definition_Modules_modules2grammar$4d282ab3aff12876(IString main_0, IMap mods_1){ 
        
        
        final IValue[] $actuals = new IValue[] {main_0,mods_1};
        IValue $memoVal = $memo_lang_rascal_grammar_definition_Modules_modules2grammar$4d282ab3aff12876.lookup($actuals, Collections.emptyMap());
        if($memoVal != null) return (IConstructor) $memoVal;
        final IMapWriter $mapwriter0 = (IMapWriter)$RVF.mapWriter();
        $MCOMP1_GEN1350:
        for(IValue $elem2_for : ((IMap)mods_1)){
            IString $elem2 = (IString) $elem2_for;
            IString m_3 = ((IString)($elem2));
            $mapwriter0.insert($RVF.tuple(m_3, $RVF.constructor(M_Grammar.GrammarModule_module_str_set_str_set_str_Grammar, new IValue[]{((IString)m_3), ((ISet)($atuple_field_project((ITuple)((ITuple)($amap_subscript(((IMap)mods_1),((IString)m_3)))), ((IInteger)$constants.get(0)/*0*/)))), ((ISet)($atuple_field_project((ITuple)((ITuple)($amap_subscript(((IMap)mods_1),((IString)m_3)))), ((IInteger)$constants.get(1)/*1*/)))), ((IConstructor)(M_lang_rascal_grammar_definition_Productions.syntax2grammar(((ISet)($atuple_field_project((ITuple)((ITuple)($amap_subscript(((IMap)mods_1),((IString)m_3)))), ((IInteger)$constants.get(2)/*2*/)))))))})));
        
        }
        
                    IConstructor def_2 = ((IConstructor)($RVF.constructor(M_Grammar.GrammarDefinition_definition_str_map_str_GrammarModule, new IValue[]{((IString)main_0), ((IMap)($mapwriter0.done()))})));
        $memoVal = M_lang_rascal_grammar_definition_Names.resolve(((IConstructor)($me.fuse(((IConstructor)(M_lang_rascal_grammar_definition_Layout.layouts(((IConstructor)def_2))))))));
        $memo_lang_rascal_grammar_definition_Modules_modules2grammar$4d282ab3aff12876.store($actuals, Collections.emptyMap(), $memoVal);
        return (IConstructor)$memoVal;
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Modules.rsc|(1404,242,<33,0>,<37,1>) 
    private final ExpiringFunctionResultCache<IValue> $memo_lang_rascal_grammar_definition_Modules_modules2grammar$ef4b475df081e853 = new ExpiringFunctionResultCache<IValue>(-1, -1);
    public IConstructor lang_rascal_grammar_definition_Modules_modules2grammar$ef4b475df081e853(IString main_0, ISet modules_1){ 
        
        
        final IValue[] $actuals = new IValue[] {main_0,modules_1};
        IValue $memoVal = $memo_lang_rascal_grammar_definition_Modules_modules2grammar$ef4b475df081e853.lookup($actuals, Collections.emptyMap());
        if($memoVal != null) return (IConstructor) $memoVal;
        $memoVal = M_lang_rascal_grammar_definition_Names.resolve(((IConstructor)($me.fuse(((IConstructor)(M_lang_rascal_grammar_definition_Layout.layouts(((IConstructor)($me.modules2definition(((IString)main_0), ((ISet)modules_1)))))))))));
        $memo_lang_rascal_grammar_definition_Modules_modules2grammar$ef4b475df081e853.store($actuals, Collections.emptyMap(), $memoVal);
        return (IConstructor)$memoVal;
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Modules.rsc|(1648,246,<39,0>,<42,1>) 
    public IConstructor lang_rascal_grammar_definition_Modules_modules2definition$fc81bfed4569bcf7(IString main_0, ISet modules_1){ 
        
        
        final IMapWriter $mapwriter5 = (IMapWriter)$RVF.mapWriter();
        $MCOMP6_GEN1850:
        for(IValue $elem8_for : ((ISet)modules_1)){
            ITree $elem8 = (ITree) $elem8_for;
            ITree m_2 = ((ITree)($elem8));
            final IConstructor $subject_val7 = ((IConstructor)($me.module2grammar(((ITree)m_2))));
            IConstructor mod_3 = null;
            $mapwriter5.insert($RVF.tuple(((IString)($aadt_get_field(((IConstructor)($subject_val7)), "name"))), $subject_val7));
        
        }
        
                    return ((IConstructor)($RVF.constructor(M_Grammar.GrammarDefinition_definition_str_map_str_GrammarModule, new IValue[]{((IString)main_0), ((IMap)($mapwriter5.done()))})));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Modules.rsc|(1896,600,<44,0>,<63,1>) 
    public IConstructor lang_rascal_grammar_definition_Modules_fuse$9ee7a01b63c7e42d(IConstructor def_0){ 
        
        
        IConstructor result_1 = ((IConstructor)$constants.get(3)/*grammar({},())*/);
        ISet todo_2 = ((ISet)($RVF.set(((IString)(((IString)($aadt_get_field(((IConstructor)def_0), "main"))))))));
        ISet done_3 = ((ISet)$constants.get(4)/*{}*/);
        ISet deps_4 = ((ISet)(M_Grammar.dependencies(((IConstructor)def_0))));
        /*muExists*/WHILE0_BT: 
            do {
                WHILE0:
                    while((((IBool)($equal(((ISet)todo_2),((ISet)$constants.get(4)/*{}*/)).not()))).getValue()){
                        ITuple $TMP9 = (ITuple)(M_Set.takeOneFrom(((ISet)todo_2)));
                        IString nm_5 = ((IString)($atuple_subscript_int(((ITuple)($TMP9)),0)));
                        todo_2 = ((ISet)($atuple_subscript_int(((ITuple)($TMP9)),1)));
                        done_3 = ((ISet)($aset_add_elm(((ISet)done_3),((IString)nm_5))));
                        if($is_defined_value($guarded_map_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_0), "modules"))))),((IString)nm_5)))){
                           IConstructor mod_6 = ((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_0), "modules"))))),((IString)nm_5))));
                           IConstructor $reducer11 = (IConstructor)(M_Grammar.compose(((IConstructor)result_1), ((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)mod_6), "grammar")))))));
                           $REDUCER10_GEN2393:
                           for(IValue $elem12_for : ((ISet)($arel_subscript1_noset(((ISet)deps_4),((IString)nm_5))))){
                               IString $elem12 = (IString) $elem12_for;
                               IString i_8 = ((IString)($elem12));
                               if($is_defined_value($guarded_map_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_0), "modules"))))),((IString)i_8)))){
                                 $reducer11 = ((IConstructor)(M_Grammar.compose(((IConstructor)($reducer11)), ((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_0), "modules"))))),((IString)i_8)))), "grammar"))))))));
                               
                               } else {
                                 continue $REDUCER10_GEN2393;
                               }
                           
                           }
                           
                                       result_1 = ((IConstructor)($reducer11));
                           todo_2 = ((ISet)($aset_add_aset(((ISet)todo_2),((ISet)(((ISet)(((ISet)($aadt_get_field(((IConstructor)mod_6), "extends"))))).subtract(((ISet)done_3)))))));
                        
                        }
                
                    }
        
            } while(false);
        /* void:  muCon([]) */return ((IConstructor)result_1);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Modules.rsc|(2501,162,<67,0>,<70,1>) 
    public IConstructor lang_rascal_grammar_definition_Modules_module2grammar$3c550cf85c31c8d8(ITree mod_0){ 
        
        
        ITuple $TMP13 = (ITuple)($me.getModuleMetaInf(((ITree)mod_0)));
        IString nm_1 = ((IString)($atuple_subscript_int(((ITuple)($TMP13)),0)));
        ISet imps_2 = ((ISet)($atuple_subscript_int(((ITuple)($TMP13)),1)));
        ISet exts_3 = ((ISet)($atuple_subscript_int(((ITuple)($TMP13)),2)));
        return ((IConstructor)($RVF.constructor(M_Grammar.GrammarModule_module_str_set_str_set_str_Grammar, new IValue[]{((IString)nm_1), ((ISet)imps_2), ((ISet)exts_3), ((IConstructor)(M_lang_rascal_grammar_definition_Productions.syntax2grammar(((ISet)($me.collect(((ITree)mod_0)))))))})));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Modules.rsc|(2666,735,<72,0>,<85,1>) 
    public ITuple lang_rascal_grammar_definition_Modules_getModuleMetaInf$57fe6e2936821b52(ITree mod_0){ 
        
        
        final ITree $switchVal14 = ((ITree)mod_0);
        boolean noCaseMatched_$switchVal14 = true;
        SWITCH2: switch(Fingerprint.getFingerprint($switchVal14)){
        
            case 0:
                if(noCaseMatched_$switchVal14){
                    noCaseMatched_$switchVal14 = false;
                    
                }
                
        
            default: if($isSubtypeOf($switchVal14.getType(),$T0)){
                        /*muExists*/CASE_0_0: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal14, "default", 2)){
                                   IValue $arg0_35 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal14))), ((IInteger)$constants.get(0)/*0*/).intValue()));
                                   if($isComparable($arg0_35.getType(), M_lang_rascal_syntax_Rascal.NT_Header)){
                                      if($nonterminal_has_name_and_arity($arg0_35, "parameters", 4)){
                                         IValue $arg0_39 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg0_35))), ((IInteger)$constants.get(0)/*0*/).intValue()));
                                         if($isComparable($arg0_39.getType(), $T3)){
                                            IValue $arg1_38 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg0_35))), ((IInteger)$constants.get(1)/*1*/).intValue()));
                                            if($isComparable($arg1_38.getType(), M_lang_rascal_syntax_Rascal.NT_QualifiedName)){
                                               ITree name_1 = ((ITree)($arg1_38));
                                               IValue $arg2_37 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg0_35))), ((IInteger)$constants.get(2)/*2*/).intValue()));
                                               if($isComparable($arg2_37.getType(), $T3)){
                                                  IValue $arg3_36 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg0_35))), ((IInteger)$constants.get(5)/*3*/).intValue()));
                                                  if($isComparable($arg3_36.getType(), $T19)){
                                                     ITree is_2 = ((ITree)($arg3_36));
                                                     IValue $arg1_34 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal14))), ((IInteger)$constants.get(1)/*1*/).intValue()));
                                                     if($isComparable($arg1_34.getType(), $T3)){
                                                        final Template $template15 = (Template)new Template($RVF, "");
                                                        $template15.addVal($arg1_38);
                                                        final ISetWriter $setwriter16 = (ISetWriter)$RVF.setWriter();
                                                        ;
                                                        /*muExists*/$SCOMP17_GEN2994_CONS_$default: 
                                                            do {
                                                                final ITree $exp20 = ((ITree)($arg3_36));
                                                                final int $last21 = (int)((ITree)($exp20)).getArgs().length() - 1;
                                                                $SCOMP17_GEN2994:
                                                                
                                                                for(int $i22 = 0; $i22 <= $last21; $i22 += 2){
                                                                   final ITree $elem19 = ((ITree)($iter_subscript($exp20, $i22)));
                                                                   if($elem19 instanceof INode && ((INode)$elem19).arity() == 1 && ((INode)$elem19).getName().equals("default")){
                                                                      IValue $arg0_23 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($elem19))), ((IInteger)$constants.get(0)/*0*/).intValue()));
                                                                      if($isComparable($arg0_23.getType(), M_lang_rascal_syntax_Rascal.NT_ImportedModule)){
                                                                         if($arg0_23 instanceof INode && ((INode)$arg0_23).arity() == 1 && ((INode)$arg0_23).getName().equals("default")){
                                                                            IValue $arg0_24 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg0_23))), ((IInteger)$constants.get(0)/*0*/).intValue()));
                                                                            if($isComparable($arg0_24.getType(), M_lang_rascal_syntax_Rascal.NT_QualifiedName)){
                                                                               ITree i_3 = null;
                                                                               final Template $template18 = (Template)new Template($RVF, "");
                                                                               $template18.addVal($arg0_24);
                                                                               $setwriter16.insert($template18.close());
                                                                            
                                                                            } else {
                                                                               continue $SCOMP17_GEN2994;
                                                                            }
                                                                         } else {
                                                                            continue $SCOMP17_GEN2994;
                                                                         }
                                                                      } else {
                                                                         continue $SCOMP17_GEN2994;
                                                                      }
                                                                   } else {
                                                                      continue $SCOMP17_GEN2994;
                                                                   }
                                                                }
                                                                
                                                        
                                                            } while(false);
                                                        final ISetWriter $setwriter25 = (ISetWriter)$RVF.setWriter();
                                                        ;
                                                        /*muExists*/$SCOMP26_GEN3071_CONS_extend: 
                                                            do {
                                                                final ITree $exp29 = ((ITree)($arg3_36));
                                                                final int $last30 = (int)((ITree)($exp29)).getArgs().length() - 1;
                                                                $SCOMP26_GEN3071:
                                                                
                                                                for(int $i31 = 0; $i31 <= $last30; $i31 += 2){
                                                                   final ITree $elem28 = ((ITree)($iter_subscript($exp29, $i31)));
                                                                   if($nonterminal_has_name_and_arity($elem28, "extend", 1)){
                                                                      IValue $arg0_32 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($elem28))), ((IInteger)$constants.get(0)/*0*/).intValue()));
                                                                      if($isComparable($arg0_32.getType(), M_lang_rascal_syntax_Rascal.NT_ImportedModule)){
                                                                         if($arg0_32 instanceof INode && ((INode)$arg0_32).arity() == 1 && ((INode)$arg0_32).getName().equals("default")){
                                                                            IValue $arg0_33 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg0_32))), ((IInteger)$constants.get(0)/*0*/).intValue()));
                                                                            if($isComparable($arg0_33.getType(), M_lang_rascal_syntax_Rascal.NT_QualifiedName)){
                                                                               ITree i_4 = null;
                                                                               final Template $template27 = (Template)new Template($RVF, "");
                                                                               $template27.addVal($arg0_33);
                                                                               $setwriter25.insert($template27.close());
                                                                            
                                                                            } else {
                                                                               continue $SCOMP26_GEN3071;
                                                                            }
                                                                         } else {
                                                                            continue $SCOMP26_GEN3071;
                                                                         }
                                                                      } else {
                                                                         continue $SCOMP26_GEN3071;
                                                                      }
                                                                   } else {
                                                                      continue $SCOMP26_GEN3071;
                                                                   }
                                                                }
                                                                
                                                        
                                                            } while(false);
                                                        return ((ITuple)($RVF.tuple(((IString)($me.deslash(((IString)($template15.close()))))), ((ISet)($setwriter16.done())), ((ISet)($setwriter25.done())))));
                                                     
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
                     if($isSubtypeOf($switchVal14.getType(),$T0)){
                        /*muExists*/CASE_0_1: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal14, "default", 2)){
                                   IValue $arg0_60 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal14))), ((IInteger)$constants.get(0)/*0*/).intValue()));
                                   if($isComparable($arg0_60.getType(), M_lang_rascal_syntax_Rascal.NT_Header)){
                                      if($nonterminal_has_name_and_arity($arg0_60, "default", 3)){
                                         IValue $arg0_63 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg0_60))), ((IInteger)$constants.get(0)/*0*/).intValue()));
                                         if($isComparable($arg0_63.getType(), $T3)){
                                            IValue $arg1_62 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg0_60))), ((IInteger)$constants.get(1)/*1*/).intValue()));
                                            if($isComparable($arg1_62.getType(), M_lang_rascal_syntax_Rascal.NT_QualifiedName)){
                                               ITree name_5 = ((ITree)($arg1_62));
                                               IValue $arg2_61 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg0_60))), ((IInteger)$constants.get(2)/*2*/).intValue()));
                                               if($isComparable($arg2_61.getType(), $T19)){
                                                  ITree is_6 = ((ITree)($arg2_61));
                                                  IValue $arg1_59 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal14))), ((IInteger)$constants.get(1)/*1*/).intValue()));
                                                  if($isComparable($arg1_59.getType(), $T3)){
                                                     final Template $template40 = (Template)new Template($RVF, "");
                                                     $template40.addVal($arg1_62);
                                                     final ISetWriter $setwriter41 = (ISetWriter)$RVF.setWriter();
                                                     ;
                                                     /*muExists*/$SCOMP42_GEN3227_CONS_$default: 
                                                         do {
                                                             final ITree $exp45 = ((ITree)($arg2_61));
                                                             final int $last46 = (int)((ITree)($exp45)).getArgs().length() - 1;
                                                             $SCOMP42_GEN3227:
                                                             
                                                             for(int $i47 = 0; $i47 <= $last46; $i47 += 2){
                                                                final ITree $elem44 = ((ITree)($iter_subscript($exp45, $i47)));
                                                                if($elem44 instanceof INode && ((INode)$elem44).arity() == 1 && ((INode)$elem44).getName().equals("default")){
                                                                   IValue $arg0_48 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($elem44))), ((IInteger)$constants.get(0)/*0*/).intValue()));
                                                                   if($isComparable($arg0_48.getType(), M_lang_rascal_syntax_Rascal.NT_ImportedModule)){
                                                                      if($arg0_48 instanceof INode && ((INode)$arg0_48).arity() == 1 && ((INode)$arg0_48).getName().equals("default")){
                                                                         IValue $arg0_49 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg0_48))), ((IInteger)$constants.get(0)/*0*/).intValue()));
                                                                         if($isComparable($arg0_49.getType(), M_lang_rascal_syntax_Rascal.NT_QualifiedName)){
                                                                            ITree i_7 = null;
                                                                            final Template $template43 = (Template)new Template($RVF, "");
                                                                            $template43.addVal($arg0_49);
                                                                            $setwriter41.insert($template43.close());
                                                                         
                                                                         } else {
                                                                            continue $SCOMP42_GEN3227;
                                                                         }
                                                                      } else {
                                                                         continue $SCOMP42_GEN3227;
                                                                      }
                                                                   } else {
                                                                      continue $SCOMP42_GEN3227;
                                                                   }
                                                                } else {
                                                                   continue $SCOMP42_GEN3227;
                                                                }
                                                             }
                                                             
                                                     
                                                         } while(false);
                                                     final ISetWriter $setwriter50 = (ISetWriter)$RVF.setWriter();
                                                     ;
                                                     /*muExists*/$SCOMP51_GEN3304_CONS_extend: 
                                                         do {
                                                             final ITree $exp54 = ((ITree)($arg2_61));
                                                             final int $last55 = (int)((ITree)($exp54)).getArgs().length() - 1;
                                                             $SCOMP51_GEN3304:
                                                             
                                                             for(int $i56 = 0; $i56 <= $last55; $i56 += 2){
                                                                final ITree $elem53 = ((ITree)($iter_subscript($exp54, $i56)));
                                                                if($nonterminal_has_name_and_arity($elem53, "extend", 1)){
                                                                   IValue $arg0_57 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($elem53))), ((IInteger)$constants.get(0)/*0*/).intValue()));
                                                                   if($isComparable($arg0_57.getType(), M_lang_rascal_syntax_Rascal.NT_ImportedModule)){
                                                                      if($arg0_57 instanceof INode && ((INode)$arg0_57).arity() == 1 && ((INode)$arg0_57).getName().equals("default")){
                                                                         IValue $arg0_58 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($arg0_57))), ((IInteger)$constants.get(0)/*0*/).intValue()));
                                                                         if($isComparable($arg0_58.getType(), M_lang_rascal_syntax_Rascal.NT_QualifiedName)){
                                                                            ITree i_8 = null;
                                                                            final Template $template52 = (Template)new Template($RVF, "");
                                                                            $template52.addVal($arg0_58);
                                                                            $setwriter50.insert($template52.close());
                                                                         
                                                                         } else {
                                                                            continue $SCOMP51_GEN3304;
                                                                         }
                                                                      } else {
                                                                         continue $SCOMP51_GEN3304;
                                                                      }
                                                                   } else {
                                                                      continue $SCOMP51_GEN3304;
                                                                   }
                                                                } else {
                                                                   continue $SCOMP51_GEN3304;
                                                                }
                                                             }
                                                             
                                                     
                                                         } while(false);
                                                     return ((ITuple)($RVF.tuple(((IString)($me.deslash(((IString)($template40.close()))))), ((ISet)($setwriter41.done())), ((ISet)($setwriter50.done())))));
                                                  
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
        
                   final Template $template64 = (Template)new Template($RVF, "unexpected module syntax ");
        $template64.beginIndent("                         ");
        $template64.addVal(mod_0);
        $template64.endIndent("                         ");
        throw new Throw($template64.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Modules.rsc|(3404,85,<87,0>,<87,85>) 
    public ISet lang_rascal_grammar_definition_Modules_getModuleSyntaxDefinitions$be12600ac17b4c6d(ITree mod_0){ 
        
        
        return ((ISet)($me.collect(((ITree)mod_0))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Modules.rsc|(3492,74,<89,0>,<93,1>) 
    public IString lang_rascal_grammar_definition_Modules_deslash$836f5a6d7e3d0227(IString input_0){ 
        
        
        try {
            IValue $visitResult = $TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{$TF.stringType()}, 
                                          new io.usethesource.vallang.IConstructor[]{}, 
                                          $RVF.bool(false)),
                 input_0,
                 (IVisitFunction) (IValue $VISIT3_subject, TraversalState $traversalState) -> {
                     VISIT3:switch(Fingerprint.getFingerprint($VISIT3_subject)){
                     
                         case 0:
                             
                     
                         default: 
                             if($isSubtypeOf($VISIT3_subject.getType(),$T6)){
                                /*muExists*/CASE_0_0: 
                                    do {
                                        final Matcher $matcher66 = (Matcher)$regExpCompile("\\\\", ((IString)($VISIT3_subject)).getValue());
                                        boolean $found67 = true;
                                        
                                            while($found67){
                                                $found67 = $matcher66.find();
                                                if($found67){
                                                   $traversalState.setBegin($matcher66.start());
                                                   $traversalState.setEnd($matcher66.end());
                                                   IString $replacement65 = (IString)(((IString)$constants.get(6)/*""*/));
                                                   if($isSubtypeOf($replacement65.getType(),$VISIT3_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement65;
                                                   
                                                   } else {
                                                      break VISIT3;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
            
                     }
                     return $VISIT3_subject;
                 });
            return (IString)$visitResult;
        
        } catch (ReturnFromTraversalException e) {
            return (IString) e.getValue();
        }
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Modules.rsc|(3568,127,<95,0>,<97,1>) 
    public IConstructor lang_rascal_grammar_definition_Modules_imports2grammar$71cc9c7471b0ea07(ISet imports_0){ 
        
        
        final ISetWriter $setwriter68 = (ISetWriter)$RVF.setWriter();
        ;
        /*muExists*/$SCOMP69_GEN3652_CONS_syntax: 
            do {
                $SCOMP69_GEN3652:
                for(IValue $elem70_for : ((ISet)imports_0)){
                    ITree $elem70 = (ITree) $elem70_for;
                    if($nonterminal_has_name_and_arity($elem70, "syntax", 1)){
                       IValue $arg0_71 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($elem70))), ((IInteger)$constants.get(0)/*0*/).intValue()));
                       if($isComparable($arg0_71.getType(), M_lang_rascal_syntax_Rascal.NT_SyntaxDefinition)){
                          ITree s_1 = null;
                          $setwriter68.insert($arg0_71);
                       
                       } else {
                          continue $SCOMP69_GEN3652;
                       }
                    } else {
                       continue $SCOMP69_GEN3652;
                    }
                }
                
                            
            } while(false);
        return ((IConstructor)(M_lang_rascal_grammar_definition_Productions.syntax2grammar(((ISet)($setwriter68.done())))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Modules.rsc|(3698,212,<99,0>,<107,1>) 
    public ISet lang_rascal_grammar_definition_Modules_collect$b7817bf58b452605(ITree mod_0){ 
        
        
        try {
            final ValueRef<ISet> result_1 = new ValueRef<ISet>("result", ((ISet)$constants.get(4)/*{}*/));
            $TRAVERSE.traverse(DIRECTION.TopDown, PROGRESS.Breaking, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{}, 
                                          new io.usethesource.vallang.IConstructor[]{((IConstructor)$constants.get(7)/*prod(label("empty",sort("Tag")),[lit("@"),layouts("LAYOUTLIST"),label("name",lex("Name"))],{tag("Fol ...*/), ((IConstructor)$constants.get(14)/*prod(label("default",sort("Import")),[lit("import"),layouts("LAYOUTLIST"),label("module",sort("Impor ...*/), ((IConstructor)$constants.get(21)/*prod(keywords("RascalKeywords"),[lit("tuple")],{})*/), ((IConstructor)$constants.get(24)/*prod(label("undeclare",sort("ShellCommand")),[lit("undeclare"),layouts("LAYOUTLIST"),label("name",so ...*/), ((IConstructor)$constants.get(30)/*prod(lex("RegExp"),[lit("\<"),lex("Name"),lit(":"),\iter-star(lex("NamedRegExp")),lit("\>")],{})*/), ((IConstructor)$constants.get(37)/*choice(sort("Class"),{prod(label("simpleCharclass",sort("Class")),[lit("["),layouts("LAYOUTLIST"),la ...*/), ((IConstructor)$constants.get(50)/*prod(label("intersection",sort("Class")),[label("lhs",sort("Class")),layouts("LAYOUTLIST"),lit("&&") ...*/), ((IConstructor)$constants.get(55)/*prod(label("default",sort("Catch")),[lit("catch"),layouts("LAYOUTLIST"),lit(":"),layouts("LAYOUTLIST ...*/), ((IConstructor)$constants.get(61)/*prod(label("outermost",sort("Strategy")),[lit("outermost")],{})*/), ((IConstructor)$constants.get(65)/*prod(label("default",sort("StructuredType")),[label("basicType",sort("BasicType")),layouts("LAYOUTLI ...*/), ((IConstructor)$constants.get(74)/*prod(label("dateTime",sort("BasicType")),[lit("datetime")],{})*/), ((IConstructor)$constants.get(77)/*prod(lex("DecimalIntegerLiteral"),[conditional(lit("0"),{\not-follow(\char-class([range(48,57),range ...*/), ((IConstructor)$constants.get(82)/*prod(label("iter",sort("Sym")),[label("symbol",sort("Sym")),layouts("LAYOUTLIST"),lit("+")],{})*/), ((IConstructor)$constants.get(87)/*prod(label("expression",sort("OptionalExpression")),[label("expression",sort("Expression"))],{})*/), ((IConstructor)$constants.get(92)/*prod(lex("HexIntegerLiteral"),[\char-class([range(48,48)]),\char-class([range(88,88),range(120,120)] ...*/), ((IConstructor)$constants.get(99)/*prod(label("absent",sort("Start")),[],{})*/), ((IConstructor)$constants.get(102)/*prod(label("symbol",sort("Type")),[label("symbol",conditional(sort("Sym"),{except("labeled"),except( ...*/), ((IConstructor)$constants.get(107)/*prod(label("ifThenElse",sort("StringTemplate")),[lit("if"),layouts("LAYOUTLIST"),lit("("),layouts("L ...*/), ((IConstructor)$constants.get(126)/*prod(label("fieldUpdate",sort("Expression")),[label("expression",sort("Expression")),layouts("LAYOUT ...*/), ((IConstructor)$constants.get(131)/*prod(label("subscript",sort("Expression")),[conditional(label("expression",sort("Expression")),{exce ...*/), ((IConstructor)$constants.get(135)/*prod(label("join",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"),lit("jo ...*/), ((IConstructor)$constants.get(140)/*prod(label("bool",sort("BasicType")),[lit("bool")],{})*/), ((IConstructor)$constants.get(143)/*prod(label("right",sort("Assoc")),[lit("right")],{})*/), ((IConstructor)$constants.get(147)/*prod(lex("PostPathChars"),[lit("\>"),lex("URLChars"),lit("|")],{})*/), ((IConstructor)$constants.get(151)/*prod(keywords("RascalKeywords"),[lit("int")],{})*/), ((IConstructor)$constants.get(153)/*prod(label("function",sort("Declaration")),[label("functionDeclaration",sort("FunctionDeclaration")) ...*/), ((IConstructor)$constants.get(158)/*prod(keywords("RascalKeywords"),[lit("syntax")],{})*/), ((IConstructor)$constants.get(160)/*associativity(sort("Expression"),\non-assoc(),{prod(label("in",sort("Expression")),[label("lhs",sort ...*/), ((IConstructor)$constants.get(167)/*prod(label("labeled",sort("Sym")),[label("symbol",sort("Sym")),layouts("LAYOUTLIST"),label("label",l ...*/), ((IConstructor)$constants.get(171)/*prod(label("literal",sort("Pattern")),[label("literal",sort("Literal"))],{})*/), ((IConstructor)$constants.get(176)/*prod(label("default",sort("Parameters")),[lit("("),layouts("LAYOUTLIST"),label("formals",sort("Forma ...*/), ((IConstructor)$constants.get(183)/*prod(label("intersection",sort("Assignment")),[lit("&=")],{})*/), ((IConstructor)$constants.get(187)/*prod(label("subtraction",sort("Assignment")),[lit("-=")],{})*/), ((IConstructor)$constants.get(190)/*prod(label("initialized",sort("Variable")),[label("name",lex("Name")),layouts("LAYOUTLIST"),lit("=") ...*/), ((IConstructor)$constants.get(194)/*prod(label("globalDirective",sort("Statement")),[lit("global"),layouts("LAYOUTLIST"),label("type",so ...*/), ((IConstructor)$constants.get(200)/*prod(keywords("RascalKeywords"),[lit("num")],{})*/), ((IConstructor)$constants.get(202)/*prod(label("template",sort("StringMiddle")),[label("mid",lex("MidStringChars")),layouts("LAYOUTLIST" ...*/), ((IConstructor)$constants.get(208)/*prod(keywords("RascalKeywords"),[lit("tag")],{})*/), ((IConstructor)$constants.get(210)/*prod(label("hole",lex("ConcretePart")),[label("hole",sort("ConcreteHole"))],{tag("category"("variabl ...*/), ((IConstructor)$constants.get(215)/*prod(label("union",sort("Class")),[label("lhs",sort("Class")),layouts("LAYOUTLIST"),lit("||"),layout ...*/), ((IConstructor)$constants.get(218)/*prod(label("alternative",sort("Sym")),[lit("("),layouts("LAYOUTLIST"),label("first",sort("Sym")),lay ...*/), ((IConstructor)$constants.get(223)/*associativity(sort("Class"),left(),{prod(label("union",sort("Class")),[label("lhs",sort("Class")),la ...*/), ((IConstructor)$constants.get(224)/*prod(label("startOfLine",sort("Sym")),[lit("^"),layouts("LAYOUTLIST"),label("symbol",sort("Sym"))],{ ...*/), ((IConstructor)$constants.get(227)/*choice(sort("Class"),{associativity(sort("Class"),left(),{prod(label("union",sort("Class")),[label(" ...*/), ((IConstructor)$constants.get(230)/*prod(start(sort("Commands")),[layouts("LAYOUTLIST"),label("top",sort("Commands")),layouts("LAYOUTLIS ...*/), ((IConstructor)$constants.get(234)/*prod(keywords("RascalKeywords"),[lit("import")],{})*/), ((IConstructor)$constants.get(235)/*prod(label("midTemplate",sort("StringTail")),[label("mid",lex("MidStringChars")),layouts("LAYOUTLIST ...*/), ((IConstructor)$constants.get(229)/*prod(label("bracket",sort("Class")),[lit("("),layouts("LAYOUTLIST"),label("charClass",sort("Class")) ...*/), ((IConstructor)$constants.get(239)/*prod(label("fieldAccess",sort("Assignable")),[label("receiver",sort("Assignable")),layouts("LAYOUTLI ...*/), ((IConstructor)$constants.get(245)/*prod(label("bracket",sort("Assignable")),[lit("("),layouts("LAYOUTLIST"),label("arg",sort("Assignabl ...*/), ((IConstructor)$constants.get(248)/*prod(label("replacing",sort("PatternWithAction")),[label("pattern",sort("Pattern")),layouts("LAYOUTL ...*/), ((IConstructor)$constants.get(255)/*prod(keywords("RascalKeywords"),[lit("data")],{})*/), ((IConstructor)$constants.get(257)/*prod(label("reifyType",sort("Expression")),[lit("#"),layouts("LAYOUTLIST"),conditional(label("type", ...*/), ((IConstructor)$constants.get(261)/*associativity(sort("Expression"),\non-assoc(),{prod(label("lessThan",sort("Expression")),[label("lhs ...*/), ((IConstructor)$constants.get(274)/*regular(\iter-seps(sort("Case"),[layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(277)/*prod(label("fieldAccess",sort("Expression")),[label("expression",sort("Expression")),layouts("LAYOUT ...*/), ((IConstructor)$constants.get(279)/*prod(lex("PostProtocolChars"),[lit("\>"),lex("URLChars"),lit("://")],{})*/), ((IConstructor)$constants.get(282)/*regular(\iter-seps(\parameterized-sort("KeywordArgument",[sort("Pattern")]),[layouts("LAYOUTLIST"),l ...*/), ((IConstructor)$constants.get(285)/*prod(label("public",sort("Visibility")),[lit("public")],{})*/), ((IConstructor)$constants.get(289)/*prod(keywords("RascalKeywords"),[lit("assert")],{})*/), ((IConstructor)$constants.get(291)/*prod(label("break",sort("Statement")),[lit("break"),layouts("LAYOUTLIST"),label("target",sort("Targe ...*/), ((IConstructor)$constants.get(296)/*prod(label("addition",sort("Assignment")),[lit("+=")],{})*/), ((IConstructor)$constants.get(299)/*prod(keywords("RascalKeywords"),[lit("loc")],{})*/), ((IConstructor)$constants.get(301)/*regular(\iter-star-seps(sort("Statement"),[layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(302)/*prod(label("default",\parameterized-sort("KeywordArgument",[parameter("T",adt("Tree",[]))])),[label( ...*/), ((IConstructor)$constants.get(308)/*prod(label("asType",sort("Expression")),[lit("["),layouts("LAYOUTLIST"),label("type",sort("Type")),l ...*/), ((IConstructor)$constants.get(312)/*prod(label("tag",sort("Kind")),[lit("tag")],{})*/), ((IConstructor)$constants.get(315)/*prod(label("map",sort("Comprehension")),[lit("("),layouts("LAYOUTLIST"),label("from",sort("Expressio ...*/), ((IConstructor)$constants.get(321)/*regular(\iter-star-seps(sort("FunctionModifier"),[layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(324)/*prod(lex("RegExp"),[lit("\<"),lex("Name"),lit("\>")],{})*/), ((IConstructor)$constants.get(325)/*regular(\iter-star-seps(sort("Toplevel"),[layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(328)/*prod(label("syntax",sort("Import")),[label("syntax",sort("SyntaxDefinition"))],{})*/), ((IConstructor)$constants.get(166)/*prod(label("notIn",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"),lit("n ...*/), ((IConstructor)$constants.get(332)/*prod(label("variable",sort("Declaration")),[label("tags",sort("Tags")),layouts("LAYOUTLIST"),label(" ...*/), ((IConstructor)$constants.get(339)/*prod(label("expression",sort("Statement")),[label("expression",conditional(sort("Expression"),{excep ...*/), ((IConstructor)$constants.get(343)/*prod(label("bs",lex("ConcretePart")),[lit("\\\\")],{tag("category"("string"))})*/), ((IConstructor)$constants.get(346)/*prod(label("index",sort("Field")),[label("fieldIndex",sort("IntegerLiteral"))],{})*/), ((IConstructor)$constants.get(351)/*prod(label("all",sort("Kind")),[lit("all")],{})*/), ((IConstructor)$constants.get(354)/*prod(label("append",sort("Assignment")),[lit("\<\<=")],{})*/), ((IConstructor)$constants.get(357)/*prod(label("stderrOutput",lex("Output")),[conditional(lit("⚠"),{\begin-of-line()}),\iter-star(\char- ...*/), ((IConstructor)$constants.get(365)/*prod(keywords("RascalKeywords"),[lit("filter")],{})*/), ((IConstructor)$constants.get(367)/*regular(\iter-star(\char-class([range(9,9),range(32,32),range(160,160),range(5760,5760),range(8192,8 ...*/), ((IConstructor)$constants.get(370)/*prod(lex("LAYOUT"),[\char-class([range(9,13),range(32,32),range(133,133),range(160,160),range(5760,5 ...*/), ((IConstructor)$constants.get(373)/*regular(\iter-star-seps(sort("ProdModifier"),[layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(376)/*associativity(sort("Expression"),\non-assoc(),{prod(label("noMatch",sort("Expression")),[label("patt ...*/), ((IConstructor)$constants.get(386)/*prod(label("iterSep",sort("Sym")),[lit("{"),layouts("LAYOUTLIST"),label("symbol",sort("Sym")),layout ...*/), ((IConstructor)$constants.get(389)/*prod(label("unequal",sort("Sym")),[label("symbol",sort("Sym")),layouts("LAYOUTLIST"),lit("\\"),layou ...*/), ((IConstructor)$constants.get(393)/*prod(label("empty",sort("Sym")),[lit("("),layouts("LAYOUTLIST"),lit(")")],{})*/), ((IConstructor)$constants.get(395)/*prod(label("default",sort("QualifiedName")),[conditional(label("names",\iter-seps(lex("Name"),[layou ...*/), ((IConstructor)$constants.get(401)/*prod(label("test",sort("FunctionModifier")),[lit("test")],{})*/), ((IConstructor)$constants.get(404)/*prod(label("typedVariableBecomes",sort("Pattern")),[label("type",sort("Type")),layouts("LAYOUTLIST") ...*/), ((IConstructor)$constants.get(406)/*prod(label("default",sort("Assignment")),[lit("=")],{})*/), ((IConstructor)$constants.get(408)/*prod(keywords("RascalKeywords"),[lit("datetime")],{})*/), ((IConstructor)$constants.get(409)/*prod(label("mid",sort("StringMiddle")),[label("mid",lex("MidStringChars"))],{})*/), ((IConstructor)$constants.get(411)/*prod(lex("StringCharacter"),[\char-class([range(10,10)]),\iter-star(\char-class([range(9,9),range(32 ...*/), ((IConstructor)$constants.get(414)/*prod(keywords("RascalKeywords"),[lit("fail")],{})*/), ((IConstructor)$constants.get(416)/*prod(label("all",sort("Expression")),[lit("all"),layouts("LAYOUTLIST"),lit("("),layouts("LAYOUTLIST" ...*/), ((IConstructor)$constants.get(418)/*prod(label("bq",lex("ConcretePart")),[lit("\\`")],{tag("category"("string"))})*/), ((IConstructor)$constants.get(421)/*prod(label("external",sort("Import")),[lit("import"),layouts("LAYOUTLIST"),label("name",sort("Qualif ...*/), ((IConstructor)$constants.get(425)/*regular(\iter-seps(sort("Pattern"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(427)/*prod(label("modulo",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"),lit(" ...*/), ((IConstructor)$constants.get(430)/*prod(label("name",sort("Field")),[label("fieldName",lex("Name"))],{})*/), ((IConstructor)$constants.get(433)/*associativity(sort("Statement"),\non-assoc(),{prod(label("try",sort("Statement")),[lit("try"),layout ...*/), ((IConstructor)$constants.get(439)/*prod(label("present",sort("CommonKeywordParameters")),[lit("("),layouts("LAYOUTLIST"),label("keyword ...*/), ((IConstructor)$constants.get(445)/*priority(sort("Expression"),[choice(sort("Expression"),{prod(label("it",sort("Expression")),[conditi ...*/), ((IConstructor)$constants.get(625)/*prod(lex("RegExpLiteral"),[lit("/"),\iter-star(lex("RegExp")),lit("/"),lex("RegExpModifier")],{})*/), ((IConstructor)$constants.get(629)/*prod(label("import",sort("EvalCommand")),[label("imported",sort("Import"))],{})*/), ((IConstructor)$constants.get(633)/*prod(keywords("RascalKeywords"),[lit("append")],{})*/), ((IConstructor)$constants.get(635)/*prod(label("template",sort("StringLiteral")),[label("pre",lex("PreStringChars")),layouts("LAYOUTLIST ...*/), ((IConstructor)$constants.get(640)/*prod(label("quit",sort("ShellCommand")),[lit("quit")],{})*/), ((IConstructor)$constants.get(643)/*prod(lex("MidProtocolChars"),[lit("\>"),lex("URLChars"),lit("\<")],{})*/), ((IConstructor)$constants.get(547)/*prod(label("range",sort("Expression")),[lit("["),layouts("LAYOUTLIST"),label("first",sort("Expressio ...*/), ((IConstructor)$constants.get(645)/*prod(lex("JustTime"),[lit("$T"),lex("TimePartNoTZ"),lex("TimeZonePart"),lit("$")],{})*/), ((IConstructor)$constants.get(651)/*prod(label("utf16",lex("UnicodeEscape")),[lit("\\"),\char-class([range(117,117)]),\char-class([range ...*/), ((IConstructor)$constants.get(655)/*prod(label("tag",sort("Declaration")),[label("tags",sort("Tags")),layouts("LAYOUTLIST"),label("visib ...*/), ((IConstructor)$constants.get(661)/*prod(label("emptyStatement",sort("Statement")),[lit(";")],{})*/), ((IConstructor)$constants.get(663)/*prod(label("empty",sort("Label")),[],{})*/), ((IConstructor)$constants.get(665)/*prod(label("list",sort("Pattern")),[lit("["),layouts("LAYOUTLIST"),label("elements0",\iter-star-seps ...*/), ((IConstructor)$constants.get(669)/*prod(keywords("RascalKeywords"),[lit("mod")],{})*/), ((IConstructor)$constants.get(670)/*prod(keywords("RascalKeywords"),[lit("extend")],{})*/), ((IConstructor)$constants.get(672)/*prod(label("iterStar",sort("Sym")),[label("symbol",sort("Sym")),layouts("LAYOUTLIST"),lit("*")],{})*/), ((IConstructor)$constants.get(674)/*prod(keywords("RascalKeywords"),[lit("while")],{})*/), ((IConstructor)$constants.get(676)/*prod(keywords("RascalKeywords"),[lit("notin")],{})*/), ((IConstructor)$constants.get(677)/*prod(label("tuple",sort("Assignable")),[lit("\<"),layouts("LAYOUTLIST"),label("elements",\iter-seps( ...*/), ((IConstructor)$constants.get(681)/*associativity(sort("Statement"),\non-assoc(),{prod(label("throw",sort("Statement")),[lit("throw"),la ...*/), ((IConstructor)$constants.get(697)/*prod(label("variable",sort("Kind")),[lit("variable")],{})*/), ((IConstructor)$constants.get(700)/*prod(keywords("RascalKeywords"),[lit("type")],{})*/), ((IConstructor)$constants.get(701)/*prod(label("bracket",sort("ProdModifier")),[lit("bracket")],{})*/), ((IConstructor)$constants.get(704)/*prod(label("default",sort("LocationLiteral")),[label("protocolPart",sort("ProtocolPart")),layouts("L ...*/), ((IConstructor)$constants.get(710)/*prod(keywords("RascalKeywords"),[lit("switch")],{})*/), ((IConstructor)$constants.get(712)/*prod(label("history",sort("ShellCommand")),[lit("history")],{})*/), ((IConstructor)$constants.get(715)/*prod(label("default",sort("FunctionBody")),[lit("{"),layouts("LAYOUTLIST"),label("statements",\iter- ...*/), ((IConstructor)$constants.get(719)/*prod(keywords("RascalKeywords"),[lit("layout")],{})*/), ((IConstructor)$constants.get(721)/*prod(keywords("RascalKeywords"),[lit("case")],{})*/), ((IConstructor)$constants.get(723)/*prod(label("associativity",sort("ProdModifier")),[label("associativity",sort("Assoc"))],{})*/), ((IConstructor)$constants.get(726)/*prod(label("labeled",sort("DataTarget")),[label("label",lex("Name")),layouts("LAYOUTLIST"),lit(":")] ...*/), ((IConstructor)$constants.get(729)/*regular(\iter-seps(sort("Catch"),[layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(521)/*prod(label("getAnnotation",sort("Expression")),[conditional(label("expression",sort("Expression")),{ ...*/), ((IConstructor)$constants.get(730)/*prod(label("nAryConstructor",sort("Variant")),[label("name",lex("Name")),layouts("LAYOUTLIST"),lit(" ...*/), ((IConstructor)$constants.get(502)/*prod(label("slice",sort("Expression")),[conditional(label("expression",sort("Expression")),{except(" ...*/), ((IConstructor)$constants.get(497)/*prod(label("is",sort("Expression")),[label("expression",sort("Expression")),layouts("LAYOUTLIST"),li ...*/), ((IConstructor)$constants.get(385)/*prod(label("match",sort("Expression")),[label("pattern",sort("Pattern")),layouts("LAYOUTLIST"),lit(" ...*/), ((IConstructor)$constants.get(736)/*prod(label("structured",sort("Type")),[label("structured",sort("StructuredType"))],{})*/), ((IConstructor)$constants.get(739)/*choice(sort("Pattern"),{prod(label("list",sort("Pattern")),[lit("["),layouts("LAYOUTLIST"),label("el ...*/), ((IConstructor)$constants.get(773)/*prod(label("innermost",sort("Strategy")),[lit("innermost")],{})*/), ((IConstructor)$constants.get(757)/*prod(label("reifiedType",sort("Pattern")),[lit("type"),layouts("LAYOUTLIST"),lit("("),layouts("LAYOU ...*/), ((IConstructor)$constants.get(598)/*prod(label("nonEquals",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"),li ...*/), ((IConstructor)$constants.get(776)/*prod(label("dateAndTimeLiteral",sort("DateTimeLiteral")),[label("dateAndTime",lex("DateAndTime"))],{ ...*/), ((IConstructor)$constants.get(781)/*prod(start(sort("Command")),[layouts("LAYOUTLIST"),label("top",sort("Command")),layouts("LAYOUTLIST" ...*/), ((IConstructor)$constants.get(785)/*prod(label("nonInterpolated",sort("PathPart")),[label("pathChars",lex("PathChars"))],{})*/), ((IConstructor)$constants.get(789)/*prod(label("product",sort("Assignment")),[lit("*=")],{})*/), ((IConstructor)$constants.get(792)/*prod(label("optional",sort("Sym")),[label("symbol",sort("Sym")),layouts("LAYOUTLIST"),lit("?")],{})*/), ((IConstructor)$constants.get(794)/*prod(lex("BooleanLiteral"),[lit("true")],{})*/), ((IConstructor)$constants.get(797)/*prod(label("empty",sort("Bound")),[],{})*/), ((IConstructor)$constants.get(800)/*prod(lex("PreStringChars"),[\char-class([range(34,34)]),\iter-star(lex("StringCharacter")),\char-cla ...*/), ((IConstructor)$constants.get(804)/*regular(alt({conditional(\char-class([range(42,42)]),{\not-follow(\char-class([range(47,47)]))}),\ch ...*/), ((IConstructor)$constants.get(810)/*prod(label("alias",sort("Kind")),[lit("alias")],{})*/), ((IConstructor)$constants.get(813)/*prod(label("interpolated",sort("ProtocolPart")),[label("pre",lex("PreProtocolChars")),layouts("LAYOU ...*/), ((IConstructor)$constants.get(819)/*prod(label("test",sort("ShellCommand")),[lit("test")],{})*/), ((IConstructor)$constants.get(821)/*prod(label("givenStrategy",sort("Visit")),[label("strategy",sort("Strategy")),layouts("LAYOUTLIST"), ...*/), ((IConstructor)$constants.get(827)/*prod(label("declaration",sort("Command")),[label("declaration",sort("Declaration"))],{})*/), ((IConstructor)$constants.get(830)/*prod(label("data",sort("Declaration")),[label("tags",sort("Tags")),layouts("LAYOUTLIST"),label("visi ...*/), ((IConstructor)$constants.get(837)/*prod(label("notFollow",sort("Sym")),[label("symbol",sort("Sym")),layouts("LAYOUTLIST"),lit("!\>\>"), ...*/), ((IConstructor)$constants.get(840)/*prod(label("parametric",sort("UserType")),[conditional(label("name",sort("QualifiedName")),{follow(l ...*/), ((IConstructor)$constants.get(844)/*prod(keywords("RascalKeywords"),[lit("bool")],{})*/), ((IConstructor)$constants.get(845)/*prod(label("nonInterpolated",sort("ProtocolPart")),[label("protocolChars",lex("ProtocolChars"))],{})*/), ((IConstructor)$constants.get(745)/*prod(label("callOrTree",sort("Pattern")),[label("expression",sort("Pattern")),layouts("LAYOUTLIST"), ...*/), ((IConstructor)$constants.get(849)/*prod(label("functionDeclaration",sort("Statement")),[label("functionDeclaration",sort("FunctionDecla ...*/), ((IConstructor)$constants.get(696)/*prod(label("insert",sort("Statement")),[lit("insert"),layouts("LAYOUTLIST"),label("dataTarget",sort( ...*/), ((IConstructor)$constants.get(543)/*prod(label("any",sort("Expression")),[lit("any"),layouts("LAYOUTLIST"),lit("("),layouts("LAYOUTLIST" ...*/), ((IConstructor)$constants.get(614)/*prod(label("and",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"),lit("&&" ...*/), ((IConstructor)$constants.get(851)/*prod(label("start",sort("Sym")),[lit("start"),layouts("LAYOUTLIST"),lit("["),layouts("LAYOUTLIST"),l ...*/), ((IConstructor)$constants.get(856)/*prod(label("annotation",sort("Assignable")),[label("receiver",sort("Assignable")),layouts("LAYOUTLIS ...*/), ((IConstructor)$constants.get(563)/*associativity(sort("Expression"),left(),{prod(label("composition",sort("Expression")),[label("lhs",s ...*/), ((IConstructor)$constants.get(859)/*prod(label("none",\parameterized-sort("KeywordArguments",[sort("Pattern")])),[],{})*/), ((IConstructor)$constants.get(861)/*prod(keywords("RascalKeywords"),[lit("throw")],{})*/), ((IConstructor)$constants.get(602)/*associativity(sort("Expression"),\non-assoc(),{prod(label("nonEquals",sort("Expression")),[label("lh ...*/), ((IConstructor)$constants.get(551)/*prod(label("isDefined",sort("Expression")),[label("argument",sort("Expression")),layouts("LAYOUTLIST ...*/), ((IConstructor)$constants.get(772)/*prod(label("splicePlus",sort("Pattern")),[lit("+"),layouts("LAYOUTLIST"),label("argument",sort("Patt ...*/), ((IConstructor)$constants.get(862)/*prod(label("interpolated",sort("PathPart")),[label("pre",lex("PrePathChars")),layouts("LAYOUTLIST"), ...*/), ((IConstructor)$constants.get(868)/*prod(label("variable",sort("Type")),[label("typeVar",sort("TypeVar"))],{})*/), ((IConstructor)$constants.get(872)/*prod(label("dataAbstract",sort("Declaration")),[label("tags",sort("Tags")),layouts("LAYOUTLIST"),lab ...*/), ((IConstructor)$constants.get(874)/*prod(label("user",sort("Type")),[label("user",sort("UserType"))],{})*/), ((IConstructor)$constants.get(876)/*prod(label("extend",sort("Import")),[lit("extend"),layouts("LAYOUTLIST"),label("module",sort("Import ...*/), ((IConstructor)$constants.get(878)/*prod(label("present",sort("Start")),[lit("start")],{})*/), ((IConstructor)$constants.get(880)/*prod(label("left",sort("Assoc")),[lit("left")],{})*/), ((IConstructor)$constants.get(494)/*prod(label("sliceStep",sort("Expression")),[conditional(label("expression",sort("Expression")),{exce ...*/), ((IConstructor)$constants.get(883)/*prod(lex("Comment"),[lit("/*"),\iter-star(alt({conditional(\char-class([range(42,42)]),{\not-follow( ...*/), ((IConstructor)$constants.get(888)/*prod(lex("TimePartNoTZ"),[\char-class([range(48,50)]),\char-class([range(48,57)]),lit(":"),\char-cla ...*/), ((IConstructor)$constants.get(898)/*regular(\iter-seps(sort("Variable"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(899)/*prod(label("selector",sort("Type")),[label("selector",sort("DataTypeSelector"))],{})*/), ((IConstructor)$constants.get(511)/*prod(label("stepRange",sort("Expression")),[lit("["),layouts("LAYOUTLIST"),label("first",sort("Expre ...*/), ((IConstructor)$constants.get(593)/*prod(label("appendAfter",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"), ...*/), ((IConstructor)$constants.get(480)/*prod(label("nonEmptyBlock",sort("Expression")),[lit("{"),layouts("LAYOUTLIST"),label("statements",\i ...*/), ((IConstructor)$constants.get(903)/*regular(\iter-seps(sort("TypeArg"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(904)/*regular(iter(\char-class([range(48,55)])))*/), ((IConstructor)$constants.get(907)/*prod(label("nonEmptyBlock",sort("Statement")),[label("label",sort("Label")),layouts("LAYOUTLIST"),li ...*/), ((IConstructor)$constants.get(909)/*prod(lex("MidStringChars"),[\char-class([range(62,62)]),\iter-star(lex("StringCharacter")),\char-cla ...*/), ((IConstructor)$constants.get(693)/*prod(label("return",sort("Statement")),[lit("return"),layouts("LAYOUTLIST"),label("statement",condit ...*/), ((IConstructor)$constants.get(624)/*associativity(sort("Expression"),right(),{prod(label("ifThenElse",sort("Expression")),[label("condit ...*/), ((IConstructor)$constants.get(911)/*prod(label("rational",sort("BasicType")),[lit("rat")],{})*/), ((IConstructor)$constants.get(914)/*associativity(sort("Class"),left(),{prod(label("intersection",sort("Class")),[label("lhs",sort("Clas ...*/), ((IConstructor)$constants.get(915)/*prod(label("post",sort("PathTail")),[label("post",lex("PostPathChars"))],{})*/), ((IConstructor)$constants.get(918)/*prod(label("set",sort("Comprehension")),[lit("{"),layouts("LAYOUTLIST"),label("results",\iter-seps(s ...*/), ((IConstructor)$constants.get(921)/*prod(label("binding",sort("Catch")),[lit("catch"),layouts("LAYOUTLIST"),label("pattern",sort("Patter ...*/), ((IConstructor)$constants.get(923)/*prod(lex("URLChars"),[\iter-star(\char-class([range(1,8),range(11,12),range(14,31),range(33,59),rang ...*/), ((IConstructor)$constants.get(926)/*prod(label("doWhile",sort("StringTemplate")),[lit("do"),layouts("LAYOUTLIST"),lit("{"),layouts("LAYO ...*/), ((IConstructor)$constants.get(932)/*prod(lex("MidPathChars"),[lit("\>"),lex("URLChars"),lit("\<")],{})*/), ((IConstructor)$constants.get(934)/*prod(label("concrete",sort("Expression")),[label("concrete",lex("Concrete"))],{})*/), ((IConstructor)$constants.get(938)/*prod(keywords("RascalKeywords"),[lit("insert")],{})*/), ((IConstructor)$constants.get(939)/*prod(keywords("RascalKeywords"),[lit("anno")],{})*/), ((IConstructor)$constants.get(941)/*prod(lex("RealLiteral"),[conditional(lit("."),{\not-precede(\char-class([range(46,46)]))}),iter(\cha ...*/), ((IConstructor)$constants.get(948)/*prod(lex("RealLiteral"),[iter(\char-class([range(48,57)])),lit("."),\iter-star(\char-class([range(48 ...*/), ((IConstructor)$constants.get(953)/*prod(label("regExp",sort("Literal")),[label("regExpLiteral",lex("RegExpLiteral"))],{tag("category"(" ...*/), ((IConstructor)$constants.get(956)/*prod(keywords("RascalKeywords"),[lit("catch")],{})*/), ((IConstructor)$constants.get(957)/*prod(label("precede",sort("Sym")),[label("match",sort("Sym")),layouts("LAYOUTLIST"),lit("\<\<"),layo ...*/), ((IConstructor)$constants.get(959)/*choice(sort("Sym"),{prod(label("except",sort("Sym")),[label("symbol",sort("Sym")),layouts("LAYOUTLIS ...*/), ((IConstructor)$constants.get(993)/*regular(iter(\char-class([range(48,57),range(65,70),range(97,102)])))*/), ((IConstructor)$constants.get(994)/*prod(keywords("RascalKeywords"),[lit("default")],{})*/), ((IConstructor)$constants.get(996)/*prod(lex("TimeZonePart"),[\char-class([range(43,43),range(45,45)]),\char-class([range(48,49)]),\char ...*/), ((IConstructor)$constants.get(998)/*prod(label("num",sort("BasicType")),[lit("num")],{})*/), ((IConstructor)$constants.get(583)/*prod(label("insertBefore",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST") ...*/), ((IConstructor)$constants.get(1000)/*prod(lex("CaseInsensitiveStringConstant"),[lit("\'"),label("chars",\iter-star(lex("StringCharacter") ...*/), ((IConstructor)$constants.get(1003)/*regular(\iter-seps(sort("Type"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1004)/*regular(alt({seq([lit("\\"),\char-class([range(123,123),range(125,125)])]),lex("TagString"),\char-cl ...*/), ((IConstructor)$constants.get(1010)/*prod(label("list",sort("BasicType")),[lit("list")],{})*/), ((IConstructor)$constants.get(1013)/*regular(\iter-seps(sort("Field"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1014)/*prod(keywords("RascalKeywords"),[lit("alias")],{})*/), ((IConstructor)$constants.get(1015)/*prod(label("for",sort("StringTemplate")),[lit("for"),layouts("LAYOUTLIST"),lit("("),layouts("LAYOUTL ...*/), ((IConstructor)$constants.get(1018)/*prod(label("default",sort("Module")),[label("header",sort("Header")),layouts("LAYOUTLIST"),label("bo ...*/), ((IConstructor)$constants.get(559)/*choice(sort("Expression"),{prod(label("asType",sort("Expression")),[lit("["),layouts("LAYOUTLIST"),l ...*/), ((IConstructor)$constants.get(1025)/*prod(keywords("RascalKeywords"),[lit("throws")],{})*/), ((IConstructor)$constants.get(518)/*prod(label("setAnnotation",sort("Expression")),[label("expression",sort("Expression")),layouts("LAYO ...*/), ((IConstructor)$constants.get(1027)/*choice(sort("Pattern"),{prod(label("descendant",sort("Pattern")),[lit("/"),layouts("LAYOUTLIST"),lab ...*/), ((IConstructor)$constants.get(1036)/*prod(label("lt",lex("ConcretePart")),[lit("\\\<")],{tag("category"("string"))})*/), ((IConstructor)$constants.get(1039)/*regular(\iter-star-seps(sort("Sym"),[layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1041)/*prod(label("conditional",sort("FunctionDeclaration")),[label("tags",sort("Tags")),layouts("LAYOUTLIS ...*/), ((IConstructor)$constants.get(1046)/*prod(label("assertWithMessage",sort("Statement")),[lit("assert"),layouts("LAYOUTLIST"),label("expres ...*/), ((IConstructor)$constants.get(1049)/*regular(\iter-star(\char-class([range(1,9),range(11,12),range(14,1114111)])))*/), ((IConstructor)$constants.get(1050)/*prod(lex("RegExp"),[lex("Backslash")],{})*/), ((IConstructor)$constants.get(1052)/*prod(label("arbitrary",sort("PatternWithAction")),[label("pattern",sort("Pattern")),layouts("LAYOUTL ...*/), ((IConstructor)$constants.get(1055)/*prod(lex("StringCharacter"),[\char-class([range(1,33),range(35,38),range(40,59),range(61,61),range(6 ...*/), ((IConstructor)$constants.get(507)/*prod(label("comprehension",sort("Expression")),[label("comprehension",sort("Comprehension"))],{})*/), ((IConstructor)$constants.get(1057)/*regular(\iter-seps(sort("Renaming"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(750)/*prod(label("set",sort("Pattern")),[lit("{"),layouts("LAYOUTLIST"),label("elements0",\iter-star-seps( ...*/), ((IConstructor)$constants.get(1060)/*prod(label("default",sort("Bound")),[lit(";"),layouts("LAYOUTLIST"),label("expression",sort("Express ...*/), ((IConstructor)$constants.get(1062)/*prod(keywords("RascalKeywords"),[lit("module")],{})*/), ((IConstructor)$constants.get(1064)/*prod(keywords("RascalKeywords"),[lit("one")],{})*/), ((IConstructor)$constants.get(1066)/*prod(keywords("RascalKeywords"),[lit("public")],{})*/), ((IConstructor)$constants.get(617)/*prod(label("or",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"),lit("||") ...*/), ((IConstructor)$constants.get(1067)/*prod(lex("Char"),[lex("UnicodeEscape")],{tag("category"("string"))})*/), ((IConstructor)$constants.get(1069)/*prod(label("labeled",sort("Target")),[label("name",lex("Name"))],{})*/), ((IConstructor)$constants.get(1071)/*regular(\iter-seps(\parameterized-sort("KeywordArgument",[sort("Expression")]),[layouts("LAYOUTLIST" ...*/), ((IConstructor)$constants.get(1074)/*prod(label("post",sort("ProtocolTail")),[label("post",lex("PostProtocolChars"))],{})*/), ((IConstructor)$constants.get(1077)/*prod(label("free",sort("TypeVar")),[lit("&"),layouts("LAYOUTLIST"),label("name",lex("Name"))],{})*/), ((IConstructor)$constants.get(1079)/*regular(\iter-star(lex("NamedRegExp")))*/), ((IConstructor)$constants.get(1080)/*prod(lex("TimeZonePart"),[lit("Z")],{})*/), ((IConstructor)$constants.get(1082)/*prod(lex("BooleanLiteral"),[lit("false")],{})*/), ((IConstructor)$constants.get(1084)/*prod(label("actualsRenaming",sort("ImportedModule")),[label("name",sort("QualifiedName")),layouts("L ...*/), ((IConstructor)$constants.get(1090)/*prod(label("text",lex("ConcretePart")),[conditional(iter(\char-class([range(1,9),range(11,59),range( ...*/), ((IConstructor)$constants.get(1095)/*prod(label("nonInterpolated",sort("StringLiteral")),[label("constant",lex("StringConstant"))],{})*/), ((IConstructor)$constants.get(1098)/*regular(\iter-seps(sort("Expression"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1099)/*prod(lex("TagString"),[conditional(lit("{"),{\not-precede(lit("\\"))}),label("contents",\iter-star(a ...*/), ((IConstructor)$constants.get(1104)/*prod(lex("Backslash"),[conditional(\char-class([range(92,92)]),{\not-follow(\char-class([range(47,47 ...*/), ((IConstructor)$constants.get(1108)/*prod(lex("NamedRegExp"),[lex("NamedBackslash")],{})*/), ((IConstructor)$constants.get(1110)/*prod(label("language",sort("SyntaxDefinition")),[label("start",sort("Start")),layouts("LAYOUTLIST"), ...*/), ((IConstructor)$constants.get(1116)/*regular(\iter-star-seps(sort("Import"),[layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1118)/*regular(\iter-seps(sort("KeywordFormal"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(768)/*prod(label("map",sort("Pattern")),[lit("("),layouts("LAYOUTLIST"),label("mappings",\iter-star-seps(\ ...*/), ((IConstructor)$constants.get(1119)/*prod(label("mid",sort("PathTail")),[label("mid",lex("MidPathChars")),layouts("LAYOUTLIST"),label("ex ...*/), ((IConstructor)$constants.get(273)/*prod(label("lessThanOrEq",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST") ...*/), ((IConstructor)$constants.get(1122)/*prod(label("default",sort("KeywordFormal")),[label("type",sort("Type")),layouts("LAYOUTLIST"),label( ...*/), ((IConstructor)$constants.get(1124)/*prod(keywords("RascalKeywords"),[lit("node")],{})*/), ((IConstructor)$constants.get(1126)/*prod(label("reference",sort("Prod")),[lit(":"),layouts("LAYOUTLIST"),label("referenced",lex("Name")) ...*/), ((IConstructor)$constants.get(1129)/*prod(start(sort("Module")),[layouts("LAYOUTLIST"),label("top",sort("Module")),layouts("LAYOUTLIST")] ...*/), ((IConstructor)$constants.get(557)/*prod(label("splice",sort("Expression")),[lit("*"),layouts("LAYOUTLIST"),label("argument",sort("Expre ...*/), ((IConstructor)$constants.get(1132)/*prod(label("commandlist",sort("Commands")),[label("commands",\iter-seps(sort("EvalCommand"),[layouts ...*/), ((IConstructor)$constants.get(1136)/*prod(label("renamings",sort("ImportedModule")),[label("name",sort("QualifiedName")),layouts("LAYOUTL ...*/), ((IConstructor)$constants.get(1138)/*prod(label("absent",sort("CommonKeywordParameters")),[],{})*/), ((IConstructor)$constants.get(1140)/*prod(lex("RationalLiteral"),[\char-class([range(49,57)]),\iter-star(\char-class([range(48,57)])),\ch ...*/), ((IConstructor)$constants.get(1145)/*prod(lex("DateAndTime"),[lit("$"),lex("DatePart"),lit("T"),conditional(lex("TimePartNoTZ"),{\not-fol ...*/), ((IConstructor)$constants.get(1149)/*prod(label("while",sort("Statement")),[label("label",sort("Label")),layouts("LAYOUTLIST"),lit("while ...*/), ((IConstructor)$constants.get(571)/*prod(label("product",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"),lit( ...*/), ((IConstructor)$constants.get(1152)/*regular(\iter-star(\char-class([range(1,8),range(11,12),range(14,31),range(33,59),range(61,123),rang ...*/), ((IConstructor)$constants.get(1153)/*prod(lex("StringConstant"),[lit("\""),label("chars",\iter-star(lex("StringCharacter"))),lit("\"")],{ ...*/), ((IConstructor)$constants.get(1035)/*prod(label("anti",sort("Pattern")),[lit("!"),layouts("LAYOUTLIST"),label("pattern",sort("Pattern"))] ...*/), ((IConstructor)$constants.get(558)/*associativity(sort("Expression"),\non-assoc(),{prod(label("splice",sort("Expression")),[lit("*"),lay ...*/), ((IConstructor)$constants.get(1155)/*prod(label("default",sort("KeywordFormals")),[label("optionalComma",lex("OptionalComma")),layouts("L ...*/), ((IConstructor)$constants.get(1161)/*prod(keywords("RascalKeywords"),[lit("str")],{})*/), ((IConstructor)$constants.get(1163)/*prod(label("default",\parameterized-sort("Mapping",[sort("Expression")])),[label("from",conditional( ...*/), ((IConstructor)$constants.get(1167)/*regular(\iter-star(lex("StringCharacter")))*/), ((IConstructor)$constants.get(574)/*prod(label("remainder",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"),li ...*/), ((IConstructor)$constants.get(1168)/*prod(label("typed",lex("Concrete")),[lit("("),label("l1",layouts("LAYOUTLIST")),label("symbol",sort( ...*/), ((IConstructor)$constants.get(1176)/*prod(label("first",sort("Prod")),[label("lhs",sort("Prod")),layouts("LAYOUTLIST"),conditional(lit("\ ...*/), ((IConstructor)$constants.get(1181)/*prod(lex("NamedRegExp"),[lit("\<"),lex("Name"),lit("\>")],{})*/), ((IConstructor)$constants.get(1182)/*regular(\iter-star(lex("ConcretePart")))*/), ((IConstructor)$constants.get(763)/*prod(label("negative",sort("Pattern")),[lit("-"),layouts("LAYOUTLIST"),label("argument",sort("Patter ...*/), ((IConstructor)$constants.get(1183)/*prod(label("tag",sort("ProdModifier")),[label("tag",sort("Tag"))],{})*/), ((IConstructor)$constants.get(1186)/*prod(label("statement",sort("EvalCommand")),[label("statement",conditional(sort("Statement"),{except ...*/), ((IConstructor)$constants.get(1190)/*prod(label("for",sort("Statement")),[label("label",sort("Label")),layouts("LAYOUTLIST"),lit("for"),l ...*/), ((IConstructor)$constants.get(1192)/*prod(label("default",sort("Renaming")),[label("from",lex("Name")),layouts("LAYOUTLIST"),lit("=\>"),l ...*/), ((IConstructor)$constants.get(612)/*associativity(sort("Expression"),\non-assoc(),{prod(label("implication",sort("Expression")),[label(" ...*/), ((IConstructor)$constants.get(1196)/*prod(label("view",sort("Kind")),[lit("view")],{})*/), ((IConstructor)$constants.get(1199)/*prod(label("visit",sort("Statement")),[label("label",sort("Label")),layouts("LAYOUTLIST"),label("vis ...*/), ((IConstructor)$constants.get(1201)/*prod(label("clear",sort("ShellCommand")),[lit("clear")],{})*/), ((IConstructor)$constants.get(515)/*prod(label("fieldProject",sort("Expression")),[conditional(label("expression",sort("Expression")),{e ...*/), ((IConstructor)$constants.get(1204)/*prod(lex("RealLiteral"),[iter(\char-class([range(48,57)])),\char-class([range(69,69),range(101,101)] ...*/), ((IConstructor)$constants.get(1205)/*prod(lex("RealLiteral"),[iter(\char-class([range(48,57)])),conditional(lit("."),{\not-follow(lit("." ...*/), ((IConstructor)$constants.get(1207)/*prod(label("bottomUp",sort("Strategy")),[lit("bottom-up")],{})*/), ((IConstructor)$constants.get(1210)/*prod(lex("JustDate"),[lit("$"),lex("DatePart"),lit("$")],{})*/), ((IConstructor)$constants.get(1212)/*prod(label("associative",sort("Assoc")),[lit("assoc")],{})*/), ((IConstructor)$constants.get(1215)/*prod(label("map",sort("BasicType")),[lit("map")],{})*/), ((IConstructor)$constants.get(1218)/*prod(label("tuple",sort("BasicType")),[lit("tuple")],{})*/), ((IConstructor)$constants.get(488)/*prod(label("reifiedType",sort("Expression")),[lit("type"),layouts("LAYOUTLIST"),lit("("),layouts("LA ...*/), ((IConstructor)$constants.get(1220)/*priority(sort("Sym"),[choice(sort("Sym"),{prod(label("except",sort("Sym")),[label("symbol",sort("Sym ...*/), ((IConstructor)$constants.get(1228)/*prod(label("nonAssociative",sort("Assoc")),[lit("non-assoc")],{})*/), ((IConstructor)$constants.get(1231)/*prod(label("ifThenElse",sort("Statement")),[label("label",sort("Label")),layouts("LAYOUTLIST"),lit(" ...*/), ((IConstructor)$constants.get(1235)/*prod(lex("OctalIntegerLiteral"),[\char-class([range(48,48)]),conditional(iter(\char-class([range(48, ...*/), ((IConstructor)$constants.get(1238)/*prod(lex("PreProtocolChars"),[lit("|"),lex("URLChars"),lit("\<")],{})*/), ((IConstructor)$constants.get(770)/*prod(label("qualifiedName",sort("Pattern")),[label("qualifiedName",sort("QualifiedName"))],{})*/), ((IConstructor)$constants.get(1239)/*prod(label("listDeclarations",sort("ShellCommand")),[lit("declarations")],{})*/), ((IConstructor)$constants.get(1242)/*prod(label("parameters",sort("Header")),[label("tags",sort("Tags")),layouts("LAYOUTLIST"),lit("modul ...*/), ((IConstructor)$constants.get(379)/*prod(label("noMatch",sort("Expression")),[label("pattern",sort("Pattern")),layouts("LAYOUTLIST"),lit ...*/), ((IConstructor)$constants.get(1247)/*regular(\iter-seps(sort("QualifiedName"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(690)/*prod(label("append",sort("Statement")),[lit("append"),layouts("LAYOUTLIST"),label("dataTarget",sort( ...*/), ((IConstructor)$constants.get(1248)/*regular(\iter-seps(sort("Sym"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1249)/*prod(label("shell",sort("Command")),[lit(":"),layouts("LAYOUTLIST"),label("command",sort("ShellComma ...*/), ((IConstructor)$constants.get(1252)/*regular(\iter-star-seps(sort("Tag"),[layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1254)/*prod(lex("TimeZonePart"),[\char-class([range(43,43),range(45,45)]),\char-class([range(48,49)]),\char ...*/), ((IConstructor)$constants.get(1255)/*prod(keywords("RascalKeywords"),[lit("any")],{})*/), ((IConstructor)$constants.get(966)/*prod(label("parametrized",sort("Sym")),[conditional(label("nonterminal",lex("Nonterminal")),{follow( ...*/), ((IConstructor)$constants.get(1256)/*prod(label("private",sort("Visibility")),[lit("private")],{})*/), ((IConstructor)$constants.get(1259)/*prod(lex("Name"),[conditional(seq([conditional(\char-class([range(65,90),range(95,95),range(97,122)] ...*/), ((IConstructor)$constants.get(1265)/*prod(lex("RegExp"),[\char-class([range(92,92)]),\char-class([range(47,47),range(60,60),range(62,62), ...*/), ((IConstructor)$constants.get(608)/*prod(label("implication",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"), ...*/), ((IConstructor)$constants.get(1266)/*prod(label("withThrows",sort("Signature")),[label("modifiers",sort("FunctionModifiers")),layouts("LA ...*/), ((IConstructor)$constants.get(1271)/*prod(keywords("RascalKeywords"),[lit("keyword")],{})*/), ((IConstructor)$constants.get(1273)/*priority(sort("Class"),[choice(sort("Class"),{prod(label("simpleCharclass",sort("Class")),[lit("["), ...*/), ((IConstructor)$constants.get(1277)/*prod(keywords("RascalKeywords"),[lit("void")],{})*/), ((IConstructor)$constants.get(1279)/*prod(label("rational",sort("Literal")),[label("rationalLiteral",lex("RationalLiteral"))],{tag("categ ...*/), ((IConstructor)$constants.get(1282)/*prod(lex("StringCharacter"),[lit("\\"),\char-class([range(34,34),range(39,39),range(60,60),range(62, ...*/), ((IConstructor)$constants.get(265)/*prod(label("lessThan",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"),con ...*/), ((IConstructor)$constants.get(1284)/*prod(label("octalIntegerLiteral",sort("IntegerLiteral")),[label("octal",lex("OctalIntegerLiteral"))] ...*/), ((IConstructor)$constants.get(1287)/*prod(label("edit",sort("ShellCommand")),[lit("edit"),layouts("LAYOUTLIST"),label("name",sort("Qualif ...*/), ((IConstructor)$constants.get(1290)/*regular(\iter-star-seps(sort("TypeArg"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1291)/*regular(\iter-star(alt({conditional(\char-class([range(42,42)]),{\not-follow(\char-class([range(47,4 ...*/), ((IConstructor)$constants.get(1292)/*prod(lex("Name"),[\char-class([range(92,92)]),\char-class([range(65,90),range(95,95),range(97,122)]) ...*/), ((IConstructor)$constants.get(1296)/*prod(lex("NamedBackslash"),[conditional(\char-class([range(92,92)]),{\not-follow(\char-class([range( ...*/), ((IConstructor)$constants.get(984)/*prod(label("sequence",sort("Sym")),[lit("("),layouts("LAYOUTLIST"),label("first",sort("Sym")),layout ...*/), ((IConstructor)$constants.get(1299)/*prod(lex("RegExp"),[\char-class([range(1,46),range(48,59),range(61,61),range(63,91),range(93,1114111 ...*/), ((IConstructor)$constants.get(1301)/*prod(keywords("RascalKeywords"),[lit("finally")],{})*/), ((IConstructor)$constants.get(1303)/*prod(label("default",lex("OptionalComma")),[opt(lit(","))],{})*/), ((IConstructor)$constants.get(1306)/*prod(keywords("RascalKeywords"),[lit("real")],{})*/), ((IConstructor)$constants.get(978)/*prod(label("endOfLine",sort("Sym")),[label("symbol",sort("Sym")),layouts("LAYOUTLIST"),lit("$")],{})*/), ((IConstructor)$constants.get(1308)/*prod(label("assert",sort("Statement")),[lit("assert"),layouts("LAYOUTLIST"),label("expression",sort( ...*/), ((IConstructor)$constants.get(1310)/*prod(label("lexical",sort("SyntaxDefinition")),[lit("lexical"),layouts("LAYOUTLIST"),label("defined" ...*/), ((IConstructor)$constants.get(1313)/*prod(label("value",sort("BasicType")),[lit("value")],{})*/), ((IConstructor)$constants.get(1316)/*prod(keywords("RascalKeywords"),[lit("true")],{})*/), ((IConstructor)$constants.get(580)/*associativity(sort("Expression"),left(),{prod(label("intersection",sort("Expression")),[label("lhs", ...*/), ((IConstructor)$constants.get(1226)/*associativity(sort("Sym"),left(),{prod(label("notFollow",sort("Sym")),[label("symbol",sort("Sym")),l ...*/), ((IConstructor)$constants.get(1317)/*prod(keywords("RascalKeywords"),[lit("private")],{})*/), ((IConstructor)$constants.get(1318)/*prod(label("timeLiteral",sort("DateTimeLiteral")),[label("time",lex("JustTime"))],{})*/), ((IConstructor)$constants.get(1321)/*prod(keywords("RascalKeywords"),[lit("try")],{})*/), ((IConstructor)$constants.get(611)/*prod(label("equivalence",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"), ...*/), ((IConstructor)$constants.get(1322)/*prod(label("noThrows",sort("Signature")),[label("modifiers",sort("FunctionModifiers")),layouts("LAYO ...*/), ((IConstructor)$constants.get(1324)/*prod(label("midInterpolated",sort("StringTail")),[label("mid",lex("MidStringChars")),layouts("LAYOUT ...*/), ((IConstructor)$constants.get(466)/*prod(label("closure",sort("Expression")),[label("type",sort("Type")),layouts("LAYOUTLIST"),label("pa ...*/), ((IConstructor)$constants.get(1326)/*prod(keywords("RascalKeywords"),[lit("all")],{})*/), ((IConstructor)$constants.get(1327)/*regular(\iter-star-seps(\parameterized-sort("Mapping",[sort("Pattern")]),[layouts("LAYOUTLIST"),lit( ...*/), ((IConstructor)$constants.get(1328)/*prod(label("named",sort("TypeArg")),[label("type",sort("Type")),layouts("LAYOUTLIST"),label("name",l ...*/), ((IConstructor)$constants.get(686)/*prod(label("throw",sort("Statement")),[lit("throw"),layouts("LAYOUTLIST"),label("statement",conditio ...*/), ((IConstructor)$constants.get(1330)/*prod(label("anno",sort("Kind")),[lit("anno")],{})*/), ((IConstructor)$constants.get(1332)/*prod(label("modifierlist",sort("FunctionModifiers")),[label("modifiers",\iter-star-seps(sort("Functi ...*/), ((IConstructor)$constants.get(1335)/*prod(label("default",\parameterized-sort("Mapping",[sort("Pattern")])),[label("from",conditional(sor ...*/), ((IConstructor)$constants.get(973)/*prod(label("nonterminal",sort("Sym")),[conditional(label("nonterminal",lex("Nonterminal")),{\not-fol ...*/), ((IConstructor)$constants.get(1339)/*prod(label("variable",sort("Assignable")),[label("qualifiedName",sort("QualifiedName"))],{})*/), ((IConstructor)$constants.get(1341)/*prod(label("ifDefined",sort("Assignment")),[lit("?=")],{})*/), ((IConstructor)$constants.get(1344)/*prod(label("patternWithAction",sort("Case")),[lit("case"),layouts("LAYOUTLIST"),label("patternWithAc ...*/), ((IConstructor)$constants.get(1347)/*prod(lex("Comment"),[lit("//"),conditional(\iter-star(\char-class([range(1,9),range(11,1114111)])),{ ...*/), ((IConstructor)$constants.get(1353)/*prod(label("concrete",sort("Pattern")),[label("concrete",lex("Concrete"))],{})*/), ((IConstructor)$constants.get(453)/*prod(label("tuple",sort("Expression")),[lit("\<"),layouts("LAYOUTLIST"),label("elements",\iter-seps( ...*/), ((IConstructor)$constants.get(1355)/*regular(\iter-star(\char-class([range(1,9),range(11,1114111)])))*/), ((IConstructor)$constants.get(1356)/*prod(label("ifThen",sort("Statement")),[label("label",sort("Label")),layouts("LAYOUTLIST"),lit("if") ...*/), ((IConstructor)$constants.get(1360)/*prod(label("default",sort("Formals")),[label("formals",\iter-star-seps(sort("Pattern"),[layouts("LAY ...*/), ((IConstructor)$constants.get(1363)/*regular(\iter-seps(sort("Sym"),[layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1364)/*prod(keywords("RascalKeywords"),[lit("visit")],{})*/), ((IConstructor)$constants.get(1365)/*priority(sort("Pattern"),[choice(sort("Pattern"),{prod(label("list",sort("Pattern")),[lit("["),layou ...*/), ((IConstructor)$constants.get(1366)/*prod(label("default",sort("Label")),[label("name",lex("Name")),layouts("LAYOUTLIST"),lit(":")],{})*/), ((IConstructor)$constants.get(1368)/*prod(label("function",sort("Kind")),[lit("function")],{})*/), ((IConstructor)$constants.get(1371)/*prod(label("unInitialized",sort("Variable")),[label("name",lex("Name"))],{})*/), ((IConstructor)$constants.get(1373)/*prod(label("givenVisibility",sort("Toplevel")),[label("declaration",sort("Declaration"))],{})*/), ((IConstructor)$constants.get(1375)/*prod(lex("DecimalIntegerLiteral"),[\char-class([range(49,57)]),conditional(\iter-star(\char-class([r ...*/), ((IConstructor)$constants.get(1376)/*prod(keywords("RascalKeywords"),[lit("map")],{})*/), ((IConstructor)$constants.get(1377)/*prod(label("string",sort("BasicType")),[lit("str")],{})*/), ((IConstructor)$constants.get(1379)/*prod(label("while",sort("StringTemplate")),[lit("while"),layouts("LAYOUTLIST"),lit("("),layouts("LAY ...*/), ((IConstructor)$constants.get(1381)/*regular(\iter-seps(sort("Sym"),[layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(461)/*prod(label("visit",sort("Expression")),[label("label",sort("Label")),layouts("LAYOUTLIST"),label("vi ...*/), ((IConstructor)$constants.get(1382)/*prod(label("loc",sort("BasicType")),[lit("loc")],{})*/), ((IConstructor)$constants.get(604)/*prod(label("ifDefinedOtherwise",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUT ...*/), ((IConstructor)$constants.get(1222)/*prod(label("follow",sort("Sym")),[label("symbol",sort("Sym")),layouts("LAYOUTLIST"),lit("\>\>"),layo ...*/), ((IConstructor)$constants.get(1384)/*prod(label("real",sort("BasicType")),[lit("real")],{})*/), ((IConstructor)$constants.get(1386)/*prod(label("list",sort("Comprehension")),[lit("["),layouts("LAYOUTLIST"),label("results",\iter-seps( ...*/), ((IConstructor)$constants.get(1388)/*prod(lex("RealLiteral"),[conditional(lit("."),{\not-precede(\char-class([range(46,46)]))}),iter(\cha ...*/), ((IConstructor)$constants.get(1389)/*prod(label("default",sort("FunctionDeclaration")),[label("tags",sort("Tags")),layouts("LAYOUTLIST"), ...*/), ((IConstructor)$constants.get(1392)/*prod(layouts("$default$"),[],{})*/), ((IConstructor)$constants.get(438)/*prod(label("try",sort("Statement")),[lit("try"),layouts("LAYOUTLIST"),label("body",sort("Statement") ...*/), ((IConstructor)$constants.get(1394)/*prod(label("declaration",sort("EvalCommand")),[label("declaration",sort("Declaration"))],{})*/), ((IConstructor)$constants.get(1396)/*regular(\iter-star(lex("LAYOUT")))*/), ((IConstructor)$constants.get(618)/*associativity(sort("Expression"),left(),{prod(label("or",sort("Expression")),[label("lhs",sort("Expr ...*/), ((IConstructor)$constants.get(615)/*associativity(sort("Expression"),left(),{prod(label("and",sort("Expression")),[label("lhs",sort("Exp ...*/), ((IConstructor)$constants.get(761)/*prod(label("multiVariable",sort("Pattern")),[label("qualifiedName",sort("QualifiedName")),layouts("L ...*/), ((IConstructor)$constants.get(500)/*prod(label("voidClosure",sort("Expression")),[label("parameters",sort("Parameters")),layouts("LAYOUT ...*/), ((IConstructor)$constants.get(540)/*prod(label("transitiveClosure",sort("Expression")),[label("argument",sort("Expression")),layouts("LA ...*/), ((IConstructor)$constants.get(1398)/*prod(lex("DatePart"),[\char-class([range(48,57)]),\char-class([range(48,57)]),\char-class([range(48, ...*/), ((IConstructor)$constants.get(1400)/*prod(label("set",sort("BasicType")),[lit("set")],{})*/), ((IConstructor)$constants.get(1403)/*prod(label("assignment",sort("Statement")),[label("assignable",sort("Assignable")),layouts("LAYOUTLI ...*/), ((IConstructor)$constants.get(1407)/*prod(label("annotation",sort("Declaration")),[label("tags",sort("Tags")),layouts("LAYOUTLIST"),label ...*/), ((IConstructor)$constants.get(1411)/*regular(\iter-star(\char-class([range(48,57),range(65,90),range(95,95),range(97,122)])))*/), ((IConstructor)$constants.get(1412)/*prod(label("default",sort("Tags")),[label("tags",\iter-star-seps(sort("Tag"),[layouts("LAYOUTLIST")] ...*/), ((IConstructor)$constants.get(1415)/*prod(layouts("LAYOUTLIST"),[conditional(\iter-star(lex("LAYOUT")),{\not-follow(\char-class([range(9, ...*/), ((IConstructor)$constants.get(1417)/*prod(label("default",sort("Renamings")),[lit("renaming"),layouts("LAYOUTLIST"),label("renamings",\it ...*/), ((IConstructor)$constants.get(1275)/*prod(label("difference",sort("Class")),[label("lhs",sort("Class")),layouts("LAYOUTLIST"),lit("-"),la ...*/), ((IConstructor)$constants.get(1421)/*prod(keywords("RascalKeywords"),[lit("lexical")],{})*/), ((IConstructor)$constants.get(1422)/*prod(label("filter",sort("Statement")),[lit("filter"),layouts("LAYOUTLIST"),lit(";")],{tag("breakabl ...*/), ((IConstructor)$constants.get(1424)/*regular(\iter-star(\char-class([range(45,45),range(48,57),range(65,90),range(95,95),range(97,122)])) ...*/), ((IConstructor)$constants.get(504)/*prod(label("list",sort("Expression")),[lit("["),layouts("LAYOUTLIST"),label("elements0",\iter-star-s ...*/), ((IConstructor)$constants.get(990)/*prod(label("caseInsensitiveLiteral",sort("Sym")),[label("cistring",lex("CaseInsensitiveStringConstan ...*/), ((IConstructor)$constants.get(1425)/*prod(keywords("RascalKeywords"),[lit("value")],{})*/), ((IConstructor)$constants.get(270)/*prod(label("greaterThan",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"), ...*/), ((IConstructor)$constants.get(1426)/*prod(label("ifDefinedOrDefault",sort("Assignable")),[label("receiver",sort("Assignable")),layouts("L ...*/), ((IConstructor)$constants.get(1429)/*regular(\iter-star-seps(sort("Expression"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1430)/*prod(label("dateTime",sort("Literal")),[label("dateTimeLiteral",sort("DateTimeLiteral"))],{})*/), ((IConstructor)$constants.get(1433)/*prod(lex("RegExpModifier"),[\iter-star(\char-class([range(100,100),range(105,105),range(109,109),ran ...*/), ((IConstructor)$constants.get(1436)/*prod(lex("StringCharacter"),[lex("UnicodeEscape")],{})*/), ((IConstructor)$constants.get(1437)/*prod(label("dateLiteral",sort("DateTimeLiteral")),[label("date",lex("JustDate"))],{})*/), ((IConstructor)$constants.get(483)/*prod(label("set",sort("Expression")),[lit("{"),layouts("LAYOUTLIST"),label("elements0",\iter-star-se ...*/), ((IConstructor)$constants.get(1440)/*regular(\iter-seps(sort("Variant"),[layouts("LAYOUTLIST"),lit("|"),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1033)/*prod(label("variableBecomes",sort("Pattern")),[label("name",lex("Name")),layouts("LAYOUTLIST"),lit(" ...*/), ((IConstructor)$constants.get(585)/*prod(label("addition",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"),lit ...*/), ((IConstructor)$constants.get(1441)/*prod(keywords("RascalKeywords"),[lit("non-assoc")],{})*/), ((IConstructor)$constants.get(1442)/*associativity(sort("Prod"),left(),{prod(label("first",sort("Prod")),[label("lhs",sort("Prod")),layou ...*/), ((IConstructor)$constants.get(1443)/*prod(label("toplevels",sort("Body")),[label("toplevels",\iter-star-seps(sort("Toplevel"),[layouts("L ...*/), ((IConstructor)$constants.get(1446)/*prod(label("java",sort("FunctionModifier")),[lit("java")],{})*/), ((IConstructor)$constants.get(1449)/*prod(label("output",sort("EvalCommand")),[lex("Output")],{})*/), ((IConstructor)$constants.get(1451)/*prod(label("real",sort("Literal")),[label("realLiteral",lex("RealLiteral"))],{tag("category"("number ...*/), ((IConstructor)$constants.get(1454)/*prod(lex("PrePathChars"),[lex("URLChars"),lit("\<")],{})*/), ((IConstructor)$constants.get(1455)/*prod(keywords("RascalKeywords"),[lit("if")],{})*/), ((IConstructor)$constants.get(1225)/*prod(label("notPrecede",sort("Sym")),[label("match",sort("Sym")),layouts("LAYOUTLIST"),lit("!\<\<"), ...*/), ((IConstructor)$constants.get(1456)/*prod(label("relation",sort("BasicType")),[lit("rel")],{})*/), ((IConstructor)$constants.get(1459)/*prod(label("expression",sort("FunctionDeclaration")),[label("tags",sort("Tags")),layouts("LAYOUTLIST ...*/), ((IConstructor)$constants.get(1461)/*prod(label("void",sort("BasicType")),[lit("void")],{})*/), ((IConstructor)$constants.get(1463)/*regular(\iter-star-seps(sort("Range"),[layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1464)/*prod(label("post",sort("StringTail")),[label("post",lex("PostStringChars"))],{})*/), ((IConstructor)$constants.get(1468)/*prod(label("ifThen",sort("StringTemplate")),[lit("if"),layouts("LAYOUTLIST"),lit("("),layouts("LAYOU ...*/), ((IConstructor)$constants.get(1470)/*choice(sort("Prod"),{prod(label("associativityGroup",sort("Prod")),[label("associativity",sort("Asso ...*/), ((IConstructor)$constants.get(1480)/*prod(label("default",sort("Declarator")),[label("type",sort("Type")),layouts("LAYOUTLIST"),label("va ...*/), ((IConstructor)$constants.get(1483)/*prod(keywords("RascalKeywords"),[lit("join")],{})*/), ((IConstructor)$constants.get(1484)/*prod(label("unconditional",sort("Replacement")),[label("replacementExpression",sort("Expression"))], ...*/), ((IConstructor)$constants.get(1487)/*prod(label("decimalIntegerLiteral",sort("IntegerLiteral")),[label("decimal",lex("DecimalIntegerLiter ...*/), ((IConstructor)$constants.get(589)/*prod(label("subtraction",sort("Expression")),[label("lhs",conditional(sort("Expression"),{except("tr ...*/), ((IConstructor)$constants.get(1490)/*prod(label("data",sort("Kind")),[lit("data")],{})*/), ((IConstructor)$constants.get(1492)/*prod(label("setOption",sort("ShellCommand")),[lit("set"),layouts("LAYOUTLIST"),label("name",sort("Qu ...*/), ((IConstructor)$constants.get(1494)/*prod(lex("DateAndTime"),[lit("$"),lex("DatePart"),lit("T"),lex("TimePartNoTZ"),lex("TimeZonePart"),l ...*/), ((IConstructor)$constants.get(1495)/*prod(label("module",sort("Kind")),[lit("module")],{})*/), ((IConstructor)$constants.get(1497)/*prod(keywords("RascalKeywords"),[lit("else")],{})*/), ((IConstructor)$constants.get(1498)/*prod(keywords("RascalKeywords"),[lit("in")],{})*/), ((IConstructor)$constants.get(1499)/*prod(keywords("RascalKeywords"),[lit("it")],{})*/), ((IConstructor)$constants.get(1500)/*prod(keywords("RascalKeywords"),[lit("false")],{})*/), ((IConstructor)$constants.get(1501)/*prod(label("empty",sort("Target")),[],{})*/), ((IConstructor)$constants.get(1503)/*prod(keywords("RascalKeywords"),[lit("return")],{})*/), ((IConstructor)$constants.get(1504)/*prod(label("default",sort("TypeArg")),[label("type",sort("Type"))],{})*/), ((IConstructor)$constants.get(1506)/*prod(label("continue",sort("Statement")),[lit("continue"),layouts("LAYOUTLIST"),label("target",sort( ...*/), ((IConstructor)$constants.get(555)/*prod(label("negation",sort("Expression")),[lit("!"),layouts("LAYOUTLIST"),label("argument",condition ...*/), ((IConstructor)$constants.get(1509)/*prod(lex("DatePart"),[\char-class([range(48,57)]),\char-class([range(48,57)]),\char-class([range(48, ...*/), ((IConstructor)$constants.get(1510)/*prod(keywords("RascalKeywords"),[lit("for")],{})*/), ((IConstructor)$constants.get(1511)/*prod(label("newline",lex("ConcretePart")),[lit("\n"),\iter-star(\char-class([range(9,9),range(32,32) ...*/), ((IConstructor)$constants.get(1514)/*prod(label("doWhile",sort("Statement")),[label("label",sort("Label")),layouts("LAYOUTLIST"),lit("do" ...*/), ((IConstructor)$constants.get(1516)/*prod(label("int",sort("BasicType")),[lit("int")],{})*/), ((IConstructor)$constants.get(1518)/*prod(label("bounded",sort("TypeVar")),[lit("&"),layouts("LAYOUTLIST"),label("name",lex("Name")),layo ...*/), ((IConstructor)$constants.get(1522)/*prod(label("one",sort("ConcreteHole")),[lit("\<"),layouts("LAYOUTLIST"),label("symbol",sort("Sym")), ...*/), ((IConstructor)$constants.get(1524)/*prod(label("hexIntegerLiteral",sort("IntegerLiteral")),[label("hex",lex("HexIntegerLiteral"))],{})*/), ((IConstructor)$constants.get(1527)/*prod(label("location",sort("Literal")),[label("locationLiteral",sort("LocationLiteral"))],{tag("cate ...*/), ((IConstructor)$constants.get(1530)/*prod(label("default",sort("FunctionModifier")),[lit("default")],{})*/), ((IConstructor)$constants.get(1532)/*prod(label("default",sort("Visibility")),[],{})*/), ((IConstructor)$constants.get(1534)/*regular(\iter-star(lex("RegExp")))*/), ((IConstructor)$constants.get(1535)/*prod(label("interpolated",sort("StringMiddle")),[label("mid",lex("MidStringChars")),layouts("LAYOUTL ...*/), ((IConstructor)$constants.get(961)/*prod(label("except",sort("Sym")),[label("symbol",sort("Sym")),layouts("LAYOUTLIST"),lit("!"),layouts ...*/), ((IConstructor)$constants.get(532)/*prod(label("qualifiedName",sort("Expression")),[label("qualifiedName",sort("QualifiedName"))],{})*/), ((IConstructor)$constants.get(1537)/*prod(label("conditional",sort("Replacement")),[label("replacementExpression",sort("Expression")),lay ...*/), ((IConstructor)$constants.get(1539)/*prod(keywords("RascalKeywords"),[lit("break")],{})*/), ((IConstructor)$constants.get(1540)/*prod(label("tryFinally",sort("Statement")),[lit("try"),layouts("LAYOUTLIST"),label("body",sort("Stat ...*/), ((IConstructor)$constants.get(1543)/*prod(label("default",sort("Tag")),[lit("@"),layouts("LAYOUTLIST"),label("name",lex("Name")),layouts( ...*/), ((IConstructor)$constants.get(1546)/*prod(label("name",sort("UserType")),[label("name",sort("QualifiedName"))],{})*/), ((IConstructor)$constants.get(1548)/*prod(label("dynamic",sort("LocalVariableDeclaration")),[lit("dynamic"),layouts("LAYOUTLIST"),label(" ...*/), ((IConstructor)$constants.get(537)/*prod(label("map",sort("Expression")),[lit("("),layouts("LAYOUTLIST"),label("mappings",\iter-star-sep ...*/), ((IConstructor)$constants.get(1553)/*prod(label("variableDeclaration",sort("Statement")),[label("declaration",sort("LocalVariableDeclarat ...*/), ((IConstructor)$constants.get(575)/*associativity(sort("Expression"),left(),{prod(label("division",sort("Expression")),[label("lhs",sort ...*/), ((IConstructor)$constants.get(1556)/*prod(lex("LAYOUT"),[lex("Comment")],{})*/), ((IConstructor)$constants.get(1557)/*prod(lex("NamedRegExp"),[\char-class([range(1,46),range(48,59),range(61,61),range(63,91),range(93,11 ...*/), ((IConstructor)$constants.get(623)/*prod(label("ifThenElse",sort("Expression")),[label("condition",sort("Expression")),layouts("LAYOUTLI ...*/), ((IConstructor)$constants.get(1558)/*prod(label("keyword",sort("SyntaxDefinition")),[lit("keyword"),layouts("LAYOUTLIST"),label("defined" ...*/), ((IConstructor)$constants.get(455)/*prod(label("literal",sort("Expression")),[label("literal",sort("Literal"))],{})*/), ((IConstructor)$constants.get(1560)/*prod(lex("TimeZonePart"),[\char-class([range(43,43),range(45,45)]),\char-class([range(48,49)]),\char ...*/), ((IConstructor)$constants.get(562)/*prod(label("composition",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"), ...*/), ((IConstructor)$constants.get(1561)/*prod(label("fail",sort("Statement")),[lit("fail"),layouts("LAYOUTLIST"),label("target",sort("Target" ...*/), ((IConstructor)$constants.get(1563)/*prod(label("bracket",sort("Type")),[lit("("),layouts("LAYOUTLIST"),label("type",sort("Type")),layout ...*/), ((IConstructor)$constants.get(1565)/*prod(label("resultOutput",lex("Output")),[lit("⇨"),\iter-star(\char-class([range(1,9),range(11,12),r ...*/), ((IConstructor)$constants.get(1568)/*prod(label("empty",sort("DataTarget")),[],{})*/), ((IConstructor)$constants.get(1570)/*prod(keywords("RascalKeywords"),[lit("test")],{})*/), ((IConstructor)$constants.get(1571)/*prod(label("all",sort("Prod")),[label("lhs",sort("Prod")),layouts("LAYOUTLIST"),lit("|"),layouts("LA ...*/), ((IConstructor)$constants.get(986)/*prod(label("characterClass",sort("Sym")),[label("charClass",sort("Class"))],{})*/), ((IConstructor)$constants.get(566)/*prod(label("division",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"),lit ...*/), ((IConstructor)$constants.get(1573)/*prod(lex("Char"),[lit("\\"),\char-class([range(32,32),range(34,34),range(39,39),range(45,45),range(6 ...*/), ((IConstructor)$constants.get(1575)/*prod(label("import",sort("Command")),[label("imported",sort("Import"))],{})*/), ((IConstructor)$constants.get(1577)/*prod(label("selector",sort("DataTypeSelector")),[label("sort",sort("QualifiedName")),layouts("LAYOUT ...*/), ((IConstructor)$constants.get(1276)/*associativity(sort("Class"),left(),{prod(label("difference",sort("Class")),[label("lhs",sort("Class" ...*/), ((IConstructor)$constants.get(268)/*prod(label("greaterThanOrEq",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIS ...*/), ((IConstructor)$constants.get(1580)/*prod(lex("NamedRegExp"),[\char-class([range(92,92)]),\char-class([range(47,47),range(60,60),range(62 ...*/), ((IConstructor)$constants.get(748)/*prod(label("tuple",sort("Pattern")),[lit("\<"),layouts("LAYOUTLIST"),label("elements",\iter-seps(sor ...*/), ((IConstructor)$constants.get(1581)/*prod(label("default",sort("ImportedModule")),[label("name",sort("QualifiedName"))],{})*/), ((IConstructor)$constants.get(1583)/*prod(label("default",\parameterized-sort("KeywordArguments",[sort("Expression")])),[label("optionalC ...*/), ((IConstructor)$constants.get(1587)/*prod(label("function",sort("Type")),[label("function",sort("FunctionType"))],{})*/), ((IConstructor)$constants.get(992)/*prod(label("parameter",sort("Sym")),[lit("&"),layouts("LAYOUTLIST"),label("nonterminal",lex("Nonterm ...*/), ((IConstructor)$constants.get(524)/*prod(label("has",sort("Expression")),[label("expression",sort("Expression")),layouts("LAYOUTLIST"),l ...*/), ((IConstructor)$constants.get(1591)/*prod(label("switch",sort("Statement")),[label("label",sort("Label")),layouts("LAYOUTLIST"),lit("swit ...*/), ((IConstructor)$constants.get(1593)/*prod(empty(),[],{})*/), ((IConstructor)$constants.get(1594)/*prod(keywords("RascalKeywords"),[sort("BasicType")],{})*/), ((IConstructor)$constants.get(1595)/*prod(keywords("RascalKeywords"),[lit("list")],{})*/), ((IConstructor)$constants.get(1596)/*prod(keywords("RascalKeywords"),[lit("rel")],{})*/), ((IConstructor)$constants.get(553)/*prod(label("negative",sort("Expression")),[lit("-"),layouts("LAYOUTLIST"),label("argument",sort("Exp ...*/), ((IConstructor)$constants.get(1597)/*regular(\iter-star(\char-class([range(100,100),range(105,105),range(109,109),range(115,115)])))*/), ((IConstructor)$constants.get(1598)/*prod(label("none",sort("KeywordFormals")),[],{})*/), ((IConstructor)$constants.get(1600)/*prod(label("default",sort("Case")),[lit("default"),layouts("LAYOUTLIST"),lit(":"),layouts("LAYOUTLIS ...*/), ((IConstructor)$constants.get(1031)/*prod(label("asType",sort("Pattern")),[lit("["),layouts("LAYOUTLIST"),label("type",sort("Type")),layo ...*/), ((IConstructor)$constants.get(579)/*prod(label("intersection",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST") ...*/), ((IConstructor)$constants.get(1602)/*prod(label("actuals",sort("ImportedModule")),[label("name",sort("QualifiedName")),layouts("LAYOUTLIS ...*/), ((IConstructor)$constants.get(1604)/*prod(label("listRelation",sort("BasicType")),[lit("lrel")],{})*/), ((IConstructor)$constants.get(1607)/*priority(sort("Prod"),[choice(sort("Prod"),{prod(label("associativityGroup",sort("Prod")),[label("as ...*/), ((IConstructor)$constants.get(1609)/*prod(label("topDownBreak",sort("Strategy")),[lit("top-down-break")],{})*/), ((IConstructor)$constants.get(1612)/*prod(label("none",\parameterized-sort("KeywordArguments",[sort("Expression")])),[],{})*/), ((IConstructor)$constants.get(1614)/*prod(label("default",sort("LocalVariableDeclaration")),[label("declarator",sort("Declarator"))],{})*/), ((IConstructor)$constants.get(1616)/*prod(label("division",sort("Assignment")),[lit("/=")],{})*/), ((IConstructor)$constants.get(1619)/*prod(keywords("RascalKeywords"),[lit("lrel")],{})*/), ((IConstructor)$constants.get(1620)/*prod(label("stdoutOutput",lex("Output")),[conditional(lit("≫"),{\begin-of-line()}),\iter-star(\char- ...*/), ((IConstructor)$constants.get(1624)/*prod(lex("RealLiteral"),[iter(\char-class([range(48,57)])),\char-class([range(68,68),range(70,70),ra ...*/), ((IConstructor)$constants.get(1608)/*associativity(sort("Prod"),left(),{prod(label("all",sort("Prod")),[label("lhs",sort("Prod")),layouts ...*/), ((IConstructor)$constants.get(1625)/*prod(label("slice",sort("Assignable")),[label("receiver",sort("Assignable")),layouts("LAYOUTLIST"),l ...*/), ((IConstructor)$constants.get(1627)/*prod(label("bottomUpBreak",sort("Strategy")),[lit("bottom-up-break")],{})*/), ((IConstructor)$constants.get(976)/*prod(label("column",sort("Sym")),[label("symbol",sort("Sym")),layouts("LAYOUTLIST"),lit("@"),layouts ...*/), ((IConstructor)$constants.get(1630)/*regular(iter(\char-class([range(48,57)])))*/), ((IConstructor)$constants.get(1631)/*prod(keywords("RascalKeywords"),[lit("bracket")],{})*/), ((IConstructor)$constants.get(1632)/*prod(keywords("RascalKeywords"),[lit("continue")],{})*/), ((IConstructor)$constants.get(1633)/*prod(label("mid",sort("ProtocolTail")),[label("mid",lex("MidProtocolChars")),layouts("LAYOUTLIST"),l ...*/), ((IConstructor)$constants.get(1636)/*prod(label("gt",lex("ConcretePart")),[lit("\\\>")],{tag("category"("string"))})*/), ((IConstructor)$constants.get(1639)/*regular(\iter-seps(sort("TypeVar"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1641)/*prod(label("unimport",sort("ShellCommand")),[lit("unimport"),layouts("LAYOUTLIST"),label("name",sort ...*/), ((IConstructor)$constants.get(1644)/*regular(\iter-seps(sort("EvalCommand"),[layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(45)/*prod(label("simpleCharclass",sort("Class")),[lit("["),layouts("LAYOUTLIST"),label("ranges",\iter-sta ...*/), ((IConstructor)$constants.get(1645)/*prod(label("default",sort("ModuleActuals")),[lit("["),layouts("LAYOUTLIST"),label("types",\iter-seps ...*/), ((IConstructor)$constants.get(1647)/*prod(label("subscript",sort("Assignable")),[label("receiver",sort("Assignable")),layouts("LAYOUTLIST ...*/), ((IConstructor)$constants.get(1649)/*prod(label("node",sort("BasicType")),[lit("node")],{})*/), ((IConstructor)$constants.get(1651)/*prod(lex("NonterminalLabel"),[\char-class([range(97,122)]),conditional(\iter-star(\char-class([range ...*/), ((IConstructor)$constants.get(1653)/*prod(label("help",sort("ShellCommand")),[lit("help")],{})*/), ((IConstructor)$constants.get(1656)/*prod(label("default",sort("ModuleParameters")),[lit("["),layouts("LAYOUTLIST"),label("parameters",\i ...*/), ((IConstructor)$constants.get(545)/*prod(label("bracket",sort("Expression")),[lit("("),layouts("LAYOUTLIST"),label("expression",sort("Ex ...*/), ((IConstructor)$constants.get(1659)/*prod(label("statement",sort("Command")),[label("statement",conditional(sort("Statement"),{except("va ...*/), ((IConstructor)$constants.get(1477)/*prod(label("unlabeled",sort("Prod")),[label("modifiers",\iter-star-seps(sort("ProdModifier"),[layout ...*/), ((IConstructor)$constants.get(601)/*prod(label("equals",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"),lit(" ...*/), ((IConstructor)$constants.get(1661)/*prod(label("topDown",sort("Strategy")),[lit("top-down")],{})*/), ((IConstructor)$constants.get(1664)/*regular(\iter-seps(lex("Name"),[layouts("LAYOUTLIST"),lit("::"),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1665)/*prod(label("interpolated",sort("StringLiteral")),[label("pre",lex("PreStringChars")),layouts("LAYOUT ...*/), ((IConstructor)$constants.get(1667)/*prod(label("string",sort("Literal")),[label("stringLiteral",sort("StringLiteral"))],{})*/), ((IConstructor)$constants.get(1670)/*prod(label("layout",sort("SyntaxDefinition")),[label("vis",sort("Visibility")),layouts("LAYOUTLIST") ...*/), ((IConstructor)$constants.get(1479)/*prod(label("labeled",sort("Prod")),[label("modifiers",\iter-star-seps(sort("ProdModifier"),[layouts( ...*/), ((IConstructor)$constants.get(753)/*prod(label("splice",sort("Pattern")),[lit("*"),layouts("LAYOUTLIST"),label("argument",sort("Pattern" ...*/), ((IConstructor)$constants.get(970)/*prod(label("literal",sort("Sym")),[label("string",lex("StringConstant"))],{})*/), ((IConstructor)$constants.get(1673)/*prod(label("basic",sort("Type")),[label("basic",sort("BasicType"))],{})*/), ((IConstructor)$constants.get(1676)/*regular(\iter-seps(sort("Statement"),[layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1677)/*prod(keywords("RascalKeywords"),[lit("start")],{})*/), ((IConstructor)$constants.get(1678)/*prod(label("default",\parameterized-sort("KeywordArguments",[sort("Pattern")])),[label("optionalComm ...*/), ((IConstructor)$constants.get(1682)/*prod(label("defaultStrategy",sort("Visit")),[lit("visit"),layouts("LAYOUTLIST"),lit("("),layouts("LA ...*/), ((IConstructor)$constants.get(1684)/*prod(keywords("RascalKeywords"),[lit("o")],{})*/), ((IConstructor)$constants.get(1685)/*prod(label("boolean",sort("Literal")),[label("booleanLiteral",lex("BooleanLiteral"))],{})*/), ((IConstructor)$constants.get(1688)/*prod(lex("TimePartNoTZ"),[\char-class([range(48,50)]),\char-class([range(48,57)]),\char-class([range ...*/), ((IConstructor)$constants.get(1689)/*prod(label("fromTo",sort("Range")),[label("start",lex("Char")),layouts("LAYOUTLIST"),lit("-"),layout ...*/), ((IConstructor)$constants.get(1693)/*prod(keywords("RascalKeywords"),[lit("set")],{})*/), ((IConstructor)$constants.get(1694)/*prod(label("constructor",sort("Assignable")),[label("name",lex("Name")),layouts("LAYOUTLIST"),lit("( ...*/), ((IConstructor)$constants.get(1697)/*regular(\iter-star(\char-class([range(48,57)])))*/), ((IConstructor)$constants.get(1698)/*prod(lex("Char"),[\char-class([range(1,31),range(33,33),range(35,38),range(40,44),range(46,59),range ...*/), ((IConstructor)$constants.get(1700)/*prod(lex("RationalLiteral"),[\char-class([range(48,57)]),\iter-star(\char-class([range(48,57)])),\ch ...*/), ((IConstructor)$constants.get(1701)/*prod(label("sliceStep",sort("Assignable")),[label("receiver",sort("Assignable")),layouts("LAYOUTLIST ...*/), ((IConstructor)$constants.get(1703)/*regular(\iter-star-seps(\parameterized-sort("Mapping",[sort("Expression")]),[layouts("LAYOUTLIST"),l ...*/), ((IConstructor)$constants.get(1029)/*prod(label("descendant",sort("Pattern")),[lit("/"),layouts("LAYOUTLIST"),label("pattern",sort("Patte ...*/), ((IConstructor)$constants.get(1704)/*prod(label("noExpression",sort("OptionalExpression")),[],{})*/), ((IConstructor)$constants.get(1706)/*regular(\iter-star(alt({seq([lit("\\"),\char-class([range(123,123),range(125,125)])]),lex("TagString ...*/), ((IConstructor)$constants.get(1707)/*prod(label("integer",sort("Literal")),[label("integerLiteral",sort("IntegerLiteral"))],{tag("categor ...*/), ((IConstructor)$constants.get(1710)/*prod(label("expression",sort("Tag")),[lit("@"),layouts("LAYOUTLIST"),label("name",lex("Name")),layou ...*/), ((IConstructor)$constants.get(1713)/*prod(label("solve",sort("Statement")),[lit("solve"),layouts("LAYOUTLIST"),lit("("),layouts("LAYOUTLI ...*/), ((IConstructor)$constants.get(1718)/*regular(\iter-star-seps(sort("Pattern"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1719)/*prod(keywords("RascalKeywords"),[lit("assoc")],{})*/), ((IConstructor)$constants.get(759)/*prod(label("typedVariable",sort("Pattern")),[label("type",sort("Type")),layouts("LAYOUTLIST"),label( ...*/), ((IConstructor)$constants.get(163)/*prod(label("in",sort("Expression")),[label("lhs",sort("Expression")),layouts("LAYOUTLIST"),lit("in") ...*/), ((IConstructor)$constants.get(1720)/*prod(label("character",sort("Range")),[label("character",lex("Char"))],{})*/), ((IConstructor)$constants.get(1723)/*prod(keywords("RascalKeywords"),[lit("bag")],{})*/), ((IConstructor)$constants.get(1725)/*prod(label("ascii",lex("UnicodeEscape")),[lit("\\"),\char-class([range(97,97)]),\char-class([range(4 ...*/), ((IConstructor)$constants.get(1728)/*prod(lex("PostStringChars"),[\char-class([range(62,62)]),\iter-star(lex("StringCharacter")),\char-cl ...*/), ((IConstructor)$constants.get(1729)/*prod(label("listModules",sort("ShellCommand")),[lit("modules")],{})*/), ((IConstructor)$constants.get(450)/*prod(label("it",sort("Expression")),[conditional(lit("it"),{\not-precede(\char-class([range(65,90),r ...*/), ((IConstructor)$constants.get(1732)/*prod(label("bag",sort("BasicType")),[lit("bag")],{})*/), ((IConstructor)$constants.get(595)/*associativity(sort("Expression"),left(),{prod(label("modulo",sort("Expression")),[label("lhs",sort(" ...*/), ((IConstructor)$constants.get(1734)/*prod(label("expression",sort("Command")),[label("expression",conditional(sort("Expression"),{except( ...*/), ((IConstructor)$constants.get(382)/*prod(label("enumerator",sort("Expression")),[label("pattern",sort("Pattern")),layouts("LAYOUTLIST"), ...*/), ((IConstructor)$constants.get(1738)/*prod(lex("JustTime"),[lit("$T"),conditional(lex("TimePartNoTZ"),{\not-follow(\char-class([range(43,4 ...*/), ((IConstructor)$constants.get(1739)/*prod(label("alias",sort("Declaration")),[label("tags",sort("Tags")),layouts("LAYOUTLIST"),label("vis ...*/), ((IConstructor)$constants.get(1742)/*prod(keywords("RascalKeywords"),[lit("dynamic")],{})*/), ((IConstructor)$constants.get(980)/*prod(label("iterStarSep",sort("Sym")),[lit("{"),layouts("LAYOUTLIST"),label("symbol",sort("Sym")),la ...*/), ((IConstructor)$constants.get(49)/*prod(label("complement",sort("Class")),[lit("!"),layouts("LAYOUTLIST"),label("charClass",sort("Class ...*/), ((IConstructor)$constants.get(1743)/*prod(label("typeArguments",sort("FunctionType")),[label("type",sort("Type")),layouts("LAYOUTLIST"),l ...*/), ((IConstructor)$constants.get(478)/*prod(label("callOrTree",sort("Expression")),[label("expression",conditional(sort("Expression"),{exce ...*/), ((IConstructor)$constants.get(1227)/*associativity(sort("Sym"),left(),{prod(label("unequal",sort("Sym")),[label("symbol",sort("Sym")),lay ...*/), ((IConstructor)$constants.get(1745)/*prod(label("default",sort("Header")),[label("tags",sort("Tags")),layouts("LAYOUTLIST"),lit("module") ...*/), ((IConstructor)$constants.get(1747)/*prod(keywords("RascalKeywords"),[lit("solve")],{})*/), ((IConstructor)$constants.get(1748)/*prod(label("abstract",sort("FunctionDeclaration")),[label("tags",sort("Tags")),layouts("LAYOUTLIST") ...*/), ((IConstructor)$constants.get(605)/*associativity(sort("Expression"),\non-assoc(),{prod(label("ifDefinedOtherwise",sort("Expression")),[ ...*/), ((IConstructor)$constants.get(1750)/*regular(\iter-seps(sort("Assignable"),[layouts("LAYOUTLIST"),lit(","),layouts("LAYOUTLIST")]))*/), ((IConstructor)$constants.get(1751)/*prod(start(sort("EvalCommand")),[layouts("LAYOUTLIST"),label("top",sort("EvalCommand")),layouts("LAY ...*/), ((IConstructor)$constants.get(529)/*prod(label("transitiveReflexiveClosure",sort("Expression")),[label("argument",sort("Expression")),la ...*/), ((IConstructor)$constants.get(1754)/*prod(lex("ProtocolChars"),[\char-class([range(124,124)]),lex("URLChars"),conditional(lit("://"),{\no ...*/), ((IConstructor)$constants.get(1473)/*prod(label("associativityGroup",sort("Prod")),[label("associativity",sort("Assoc")),layouts("LAYOUTL ...*/), ((IConstructor)$constants.get(1758)/*prod(label("varArgs",sort("Parameters")),[lit("("),layouts("LAYOUTLIST"),label("formals",sort("Forma ...*/), ((IConstructor)$constants.get(594)/*associativity(sort("Expression"),left(),{prod(label("insertBefore",sort("Expression")),[label("lhs", ...*/), ((IConstructor)$constants.get(1761)/*prod(lex("PathChars"),[lex("URLChars"),\char-class([range(124,124)])],{})*/), ((IConstructor)$constants.get(470)/*prod(label("reducer",sort("Expression")),[lit("("),layouts("LAYOUTLIST"),label("init",sort("Expressi ...*/), ((IConstructor)$constants.get(1762)/*prod(lex("Nonterminal"),[conditional(seq([conditional(\char-class([range(65,90)]),{\not-precede(\cha ...*/), ((IConstructor)$constants.get(1767)/*prod(label("utf32",lex("UnicodeEscape")),[lit("\\"),\char-class([range(85,85)]),alt({lit("10"),seq([ ...*/), ((IConstructor)$constants.get(548)/*choice(sort("Expression"),{prod(label("it",sort("Expression")),[conditional(lit("it"),{\not-precede( ...*/), ((IConstructor)$constants.get(1773)/*regular(iter(\char-class([range(1,9),range(11,59),range(61,61),range(63,91),range(93,95),range(97,11 ...*/), ((IConstructor)$constants.get(1774)/*prod(keywords("RascalKeywords"),[lit("rat")],{})*/), ((IConstructor)$constants.get(1775)/*prod(label("type",sort("BasicType")),[lit("type")],{})*/)}, 
                                          $RVF.bool(true)),
                 mod_0,
                 (IVisitFunction) (IValue $VISIT5_subject, TraversalState $traversalState) -> {
                     VISIT5:switch(Fingerprint.getConcreteFingerprint($VISIT5_subject)){
                     
                         case 0:
                             
                     
                         default: 
                             if($isSubtypeOf($VISIT5_subject.getType(),M_lang_rascal_syntax_Rascal.NT_SyntaxDefinition)){
                                /*muExists*/CASE_0_0: 
                                    do {
                                        ValueRef<ITree> s_2 = new ValueRef<ITree>();
                                        ITree $aux_1 = ((ITree)($VISIT5_subject));
                                        result_1.setValue($aset_add_elm(result_1.getValue(),$aux_1));
                                        $traversalState.setMatched(true); 
                                        break VISIT5;
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT5_subject.getType(),M_lang_rascal_syntax_Rascal.NT_Body)){
                                /*muExists*/CASE_0_1: 
                                    do {
                                        ValueRef<ITree> b_3 = new ValueRef<ITree>();
                                        ITree $replacement72 = (ITree)($VISIT5_subject);
                                        if($isSubtypeOf($replacement72.getType(),$VISIT5_subject.getType())){
                                           $traversalState.setMatchedAndChanged(true, true);
                                           return $replacement72;
                                        
                                        } else {
                                           break VISIT5;// switch
                                        
                                        }
                                    } while(false);
                             
                             }
            
                     }
                     return $VISIT5_subject;
                 });return result_1.getValue();
        
        } catch (ReturnFromTraversalException e) {
            return (ISet) e.getValue();
        }
    
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `lang::rascal::grammar::definition::Modules`");
    }
}