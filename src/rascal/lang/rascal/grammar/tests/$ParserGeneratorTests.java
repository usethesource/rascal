package rascal.lang.rascal.grammar.tests;
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
public class $ParserGeneratorTests 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.lang.rascal.grammar.tests.$ParserGeneratorTests_$I {

    private final $ParserGeneratorTests_$I $me;
    private final IList $constants;
    
    
    public final rascal.lang.rascal.grammar.$ParserGenerator M_lang_rascal_grammar_ParserGenerator;
    public final rascal.lang.rascal.grammar.definition.$Parameters M_lang_rascal_grammar_definition_Parameters;
    public final rascal.$Exception M_Exception;
    public final rascal.lang.rascal.grammar.tests.$TestGrammars M_lang_rascal_grammar_tests_TestGrammars;
    public final rascal.$Type M_Type;
    public final rascal.$List M_List;
    public final rascal.$IO M_IO;
    public final rascal.lang.rascal.grammar.definition.$Literals M_lang_rascal_grammar_definition_Literals;
    public final rascal.$Grammar M_Grammar;
    public final rascal.$Message M_Message;
    public final rascal.$String M_String;
    public final rascal.$ParseTree M_ParseTree;

    
    
    public ISourceLocation ParserBaseLoc;
    public final io.usethesource.vallang.type.Type $T1;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T2;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T6;	/*aparameter("A",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T3;	/*astr()*/
    public final io.usethesource.vallang.type.Type ADT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T4;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Production;	/*aadt("Maybe",[aadt("Production",[],dataSyntax())],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Item;	/*aadt("Item",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarModule;	/*aadt("GrammarModule",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T5;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_1;	/*aadt("Maybe",[aparameter("A",avalue(),closed=false)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T0;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Associativity;	/*aadt("Associativity",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Symbol;	/*aadt("Maybe",[aadt("Symbol",[],dataSyntax())],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_StringTail;	/*aadt("StringTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringTail;	/*aadt("StringTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Backslash;	/*aadt("Backslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Backslash;	/*aadt("Backslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUTLIST;	/*aadt("LAYOUTLIST",[],layoutSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Grammar;	/*aadt("Grammar",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Associativity;	/*aadt("Maybe",[aadt("Associativity",[],dataSyntax())],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RascalKeywords;	/*aadt("RascalKeywords",[],keywordSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_$default$;	/*aadt("$default$",[],layoutSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/

    public $ParserGeneratorTests(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $ParserGeneratorTests(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($ParserGeneratorTests_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.lang.rascal.grammar.tests.$ParserGeneratorTests.class, this);
       
       mstore.importModule(rascal.lang.rascal.grammar.$ParserGenerator.class, rex, rascal.lang.rascal.grammar.$ParserGenerator::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Parameters.class, rex, rascal.lang.rascal.grammar.definition.$Parameters::new);
       mstore.importModule(rascal.$Exception.class, rex, rascal.$Exception::new);
       mstore.importModule(rascal.lang.rascal.grammar.tests.$TestGrammars.class, rex, rascal.lang.rascal.grammar.tests.$TestGrammars::new);
       mstore.importModule(rascal.$Type.class, rex, rascal.$Type::new);
       mstore.importModule(rascal.$List.class, rex, rascal.$List::new);
       mstore.importModule(rascal.$IO.class, rex, rascal.$IO::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Literals.class, rex, rascal.lang.rascal.grammar.definition.$Literals::new);
       mstore.importModule(rascal.$Grammar.class, rex, rascal.$Grammar::new);
       mstore.importModule(rascal.$Message.class, rex, rascal.$Message::new);
       mstore.importModule(rascal.$String.class, rex, rascal.$String::new);
       mstore.importModule(rascal.$ParseTree.class, rex, rascal.$ParseTree::new); 
       
       M_lang_rascal_grammar_ParserGenerator = mstore.getModule(rascal.lang.rascal.grammar.$ParserGenerator.class);
       M_lang_rascal_grammar_definition_Parameters = mstore.getModule(rascal.lang.rascal.grammar.definition.$Parameters.class);
       M_Exception = mstore.getModule(rascal.$Exception.class);
       M_lang_rascal_grammar_tests_TestGrammars = mstore.getModule(rascal.lang.rascal.grammar.tests.$TestGrammars.class);
       M_Type = mstore.getModule(rascal.$Type.class);
       M_List = mstore.getModule(rascal.$List.class);
       M_IO = mstore.getModule(rascal.$IO.class);
       M_lang_rascal_grammar_definition_Literals = mstore.getModule(rascal.lang.rascal.grammar.definition.$Literals.class);
       M_Grammar = mstore.getModule(rascal.$Grammar.class);
       M_Message = mstore.getModule(rascal.$Message.class);
       M_String = mstore.getModule(rascal.$String.class);
       M_ParseTree = mstore.getModule(rascal.$ParseTree.class); 
       
                          
       
       $TS.importStore(M_lang_rascal_grammar_ParserGenerator.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Parameters.$TS);
       $TS.importStore(M_Exception.$TS);
       $TS.importStore(M_lang_rascal_grammar_tests_TestGrammars.$TS);
       $TS.importStore(M_Type.$TS);
       $TS.importStore(M_List.$TS);
       $TS.importStore(M_IO.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Literals.$TS);
       $TS.importStore(M_Grammar.$TS);
       $TS.importStore(M_Message.$TS);
       $TS.importStore(M_String.$TS);
       $TS.importStore(M_ParseTree.$TS);
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/lang/rascal/grammar/tests/$ParserGeneratorTests.constants", 109, "83e1694863522113ad5cc7237ded579a");
       NT_FunctionType = $sort("FunctionType");
       ADT_FunctionType = $adt("FunctionType");
       NT_Visibility = $sort("Visibility");
       ADT_Visibility = $adt("Visibility");
       NT_PostProtocolChars = $lex("PostProtocolChars");
       ADT_PostProtocolChars = $adt("PostProtocolChars");
       NT_Strategy = $sort("Strategy");
       ADT_Strategy = $adt("Strategy");
       ADT_Attr = $adt("Attr");
       NT_MidStringChars = $lex("MidStringChars");
       ADT_MidStringChars = $adt("MidStringChars");
       NT_Replacement = $sort("Replacement");
       ADT_Replacement = $adt("Replacement");
       ADT_Tree = $adt("Tree");
       NT_ProtocolChars = $lex("ProtocolChars");
       ADT_ProtocolChars = $adt("ProtocolChars");
       ADT_LocationChangeType = $adt("LocationChangeType");
       ADT_IOCapability = $adt("IOCapability");
       NT_Name = $lex("Name");
       ADT_Name = $adt("Name");
       NT_TagString = $lex("TagString");
       ADT_TagString = $adt("TagString");
       NT_Concrete = $lex("Concrete");
       ADT_Concrete = $adt("Concrete");
       ADT_Production = $adt("Production");
       NT_Range = $sort("Range");
       ADT_Range = $adt("Range");
       NT_LAYOUT = $lex("LAYOUT");
       ADT_LAYOUT = $adt("LAYOUT");
       NT_Pattern = $sort("Pattern");
       ADT_Pattern = $adt("Pattern");
       ADT_Item = $adt("Item");
       NT_RationalLiteral = $lex("RationalLiteral");
       ADT_RationalLiteral = $adt("RationalLiteral");
       NT_RegExpLiteral = $lex("RegExpLiteral");
       ADT_RegExpLiteral = $adt("RegExpLiteral");
       NT_Declarator = $sort("Declarator");
       ADT_Declarator = $adt("Declarator");
       ADT_GrammarModule = $adt("GrammarModule");
       NT_ModuleParameters = $sort("ModuleParameters");
       ADT_ModuleParameters = $adt("ModuleParameters");
       NT_SyntaxDefinition = $sort("SyntaxDefinition");
       ADT_SyntaxDefinition = $adt("SyntaxDefinition");
       NT_DatePart = $lex("DatePart");
       ADT_DatePart = $adt("DatePart");
       NT_Output = $lex("Output");
       ADT_Output = $adt("Output");
       NT_IntegerLiteral = $sort("IntegerLiteral");
       ADT_IntegerLiteral = $adt("IntegerLiteral");
       NT_Case = $sort("Case");
       ADT_Case = $adt("Case");
       NT_ImportedModule = $sort("ImportedModule");
       ADT_ImportedModule = $adt("ImportedModule");
       NT_BooleanLiteral = $lex("BooleanLiteral");
       ADT_BooleanLiteral = $adt("BooleanLiteral");
       NT_Expression = $sort("Expression");
       ADT_Expression = $adt("Expression");
       NT_LocalVariableDeclaration = $sort("LocalVariableDeclaration");
       ADT_LocalVariableDeclaration = $adt("LocalVariableDeclaration");
       NT_Target = $sort("Target");
       ADT_Target = $adt("Target");
       NT_TimePartNoTZ = $lex("TimePartNoTZ");
       ADT_TimePartNoTZ = $adt("TimePartNoTZ");
       NT_Variable = $sort("Variable");
       ADT_Variable = $adt("Variable");
       NT_KeywordFormals = $sort("KeywordFormals");
       ADT_KeywordFormals = $adt("KeywordFormals");
       NT_DataTarget = $sort("DataTarget");
       ADT_DataTarget = $adt("DataTarget");
       NT_Renaming = $sort("Renaming");
       ADT_Renaming = $adt("Renaming");
       NT_Catch = $sort("Catch");
       ADT_Catch = $adt("Catch");
       NT_Renamings = $sort("Renamings");
       ADT_Renamings = $adt("Renamings");
       NT_KeywordFormal = $sort("KeywordFormal");
       ADT_KeywordFormal = $adt("KeywordFormal");
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
       NT_StringLiteral = $sort("StringLiteral");
       ADT_StringLiteral = $adt("StringLiteral");
       NT_PreStringChars = $lex("PreStringChars");
       ADT_PreStringChars = $adt("PreStringChars");
       NT_TypeArg = $sort("TypeArg");
       ADT_TypeArg = $adt("TypeArg");
       NT_NonterminalLabel = $lex("NonterminalLabel");
       ADT_NonterminalLabel = $adt("NonterminalLabel");
       NT_Bound = $sort("Bound");
       ADT_Bound = $adt("Bound");
       NT_Nonterminal = $lex("Nonterminal");
       ADT_Nonterminal = $adt("Nonterminal");
       NT_RegExp = $lex("RegExp");
       ADT_RegExp = $adt("RegExp");
       NT_PathPart = $sort("PathPart");
       ADT_PathPart = $adt("PathPart");
       NT_Class = $sort("Class");
       ADT_Class = $adt("Class");
       ADT_Associativity = $adt("Associativity");
       NT_Signature = $sort("Signature");
       ADT_Signature = $adt("Signature");
       NT_ModuleActuals = $sort("ModuleActuals");
       ADT_ModuleActuals = $adt("ModuleActuals");
       NT_URLChars = $lex("URLChars");
       ADT_URLChars = $adt("URLChars");
       ADT_Symbol = $adt("Symbol");
       NT_Tags = $sort("Tags");
       ADT_Tags = $adt("Tags");
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
       NT_StringTail = $sort("StringTail");
       ADT_StringTail = $adt("StringTail");
       NT_Backslash = $lex("Backslash");
       ADT_Backslash = $adt("Backslash");
       NT_Char = $lex("Char");
       ADT_Char = $adt("Char");
       NT_DataTypeSelector = $sort("DataTypeSelector");
       ADT_DataTypeSelector = $adt("DataTypeSelector");
       NT_CaseInsensitiveStringConstant = $lex("CaseInsensitiveStringConstant");
       ADT_CaseInsensitiveStringConstant = $adt("CaseInsensitiveStringConstant");
       ADT_Exception = $adt("Exception");
       NT_Assignment = $sort("Assignment");
       ADT_Assignment = $adt("Assignment");
       NT_PatternWithAction = $sort("PatternWithAction");
       ADT_PatternWithAction = $adt("PatternWithAction");
       NT_PostPathChars = $lex("PostPathChars");
       ADT_PostPathChars = $adt("PostPathChars");
       NT_Module = $sort("Module");
       ADT_Module = $adt("Module");
       NT_TypeVar = $sort("TypeVar");
       ADT_TypeVar = $adt("TypeVar");
       NT_FunctionDeclaration = $sort("FunctionDeclaration");
       ADT_FunctionDeclaration = $adt("FunctionDeclaration");
       NT_JustDate = $lex("JustDate");
       ADT_JustDate = $adt("JustDate");
       NT_Header = $sort("Header");
       ADT_Header = $adt("Header");
       ADT_LocationType = $adt("LocationType");
       NT_UserType = $sort("UserType");
       ADT_UserType = $adt("UserType");
       ADT_CharRange = $adt("CharRange");
       NT_Variant = $sort("Variant");
       ADT_Variant = $adt("Variant");
       NT_Comprehension = $sort("Comprehension");
       ADT_Comprehension = $adt("Comprehension");
       NT_Import = $sort("Import");
       ADT_Import = $adt("Import");
       ADT_LAYOUTLIST = $layouts("LAYOUTLIST");
       NT_ConcreteHole = $sort("ConcreteHole");
       ADT_ConcreteHole = $adt("ConcreteHole");
       ADT_Grammar = $adt("Grammar");
       ADT_Message = $adt("Message");
       NT_Sym = $sort("Sym");
       ADT_Sym = $adt("Sym");
       NT_StringMiddle = $sort("StringMiddle");
       ADT_StringMiddle = $adt("StringMiddle");
       NT_DateAndTime = $lex("DateAndTime");
       ADT_DateAndTime = $adt("DateAndTime");
       NT_RealLiteral = $lex("RealLiteral");
       ADT_RealLiteral = $adt("RealLiteral");
       NT_Formals = $sort("Formals");
       ADT_Formals = $adt("Formals");
       NT_FunctionModifiers = $sort("FunctionModifiers");
       ADT_FunctionModifiers = $adt("FunctionModifiers");
       NT_QualifiedName = $sort("QualifiedName");
       ADT_QualifiedName = $adt("QualifiedName");
       NT_MidPathChars = $lex("MidPathChars");
       ADT_MidPathChars = $adt("MidPathChars");
       NT_Statement = $sort("Statement");
       ADT_Statement = $adt("Statement");
       NT_MidProtocolChars = $lex("MidProtocolChars");
       ADT_MidProtocolChars = $adt("MidProtocolChars");
       ADT_RascalKeywords = $keywords("RascalKeywords");
       NT_Parameters = $sort("Parameters");
       ADT_Parameters = $adt("Parameters");
       NT_PreProtocolChars = $lex("PreProtocolChars");
       ADT_PreProtocolChars = $adt("PreProtocolChars");
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
       NT_Label = $sort("Label");
       ADT_Label = $adt("Label");
       NT_Kind = $sort("Kind");
       ADT_Kind = $adt("Kind");
       NT_RegExpModifier = $lex("RegExpModifier");
       ADT_RegExpModifier = $adt("RegExpModifier");
       NT_Prod = $sort("Prod");
       ADT_Prod = $adt("Prod");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       ADT_RuntimeException = $adt("RuntimeException");
       NT_NamedRegExp = $lex("NamedRegExp");
       ADT_NamedRegExp = $adt("NamedRegExp");
       NT_Assoc = $sort("Assoc");
       ADT_Assoc = $adt("Assoc");
       NT_DateTimeLiteral = $sort("DateTimeLiteral");
       ADT_DateTimeLiteral = $adt("DateTimeLiteral");
       NT_DecimalIntegerLiteral = $lex("DecimalIntegerLiteral");
       ADT_DecimalIntegerLiteral = $adt("DecimalIntegerLiteral");
       NT_EvalCommand = $sort("EvalCommand");
       ADT_EvalCommand = $adt("EvalCommand");
       NT_StringCharacter = $lex("StringCharacter");
       ADT_StringCharacter = $adt("StringCharacter");
       NT_FunctionModifier = $sort("FunctionModifier");
       ADT_FunctionModifier = $adt("FunctionModifier");
       NT_ProdModifier = $sort("ProdModifier");
       ADT_ProdModifier = $adt("ProdModifier");
       NT_JustTime = $lex("JustTime");
       ADT_JustTime = $adt("JustTime");
       NT_ConcretePart = $lex("ConcretePart");
       ADT_ConcretePart = $adt("ConcretePart");
       NT_Literal = $sort("Literal");
       ADT_Literal = $adt("Literal");
       NT_Toplevel = $sort("Toplevel");
       ADT_Toplevel = $adt("Toplevel");
       ADT_$default$ = $layouts("$default$");
       NT_PathChars = $lex("PathChars");
       ADT_PathChars = $adt("PathChars");
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
       NT_Assignable = $sort("Assignable");
       ADT_Assignable = $adt("Assignable");
       NT_UnicodeEscape = $lex("UnicodeEscape");
       ADT_UnicodeEscape = $adt("UnicodeEscape");
       NT_CommonKeywordParameters = $sort("CommonKeywordParameters");
       ADT_CommonKeywordParameters = $adt("CommonKeywordParameters");
       NT_ProtocolTail = $sort("ProtocolTail");
       ADT_ProtocolTail = $adt("ProtocolTail");
       NT_BasicType = $sort("BasicType");
       ADT_BasicType = $adt("BasicType");
       ADT_Condition = $adt("Condition");
       $T1 = $TF.valueType();
       $T2 = $TF.parameterType("T", $T1);
       $T6 = $TF.parameterType("A", $T1);
       $T3 = $TF.stringType();
       $T4 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_1 = $parameterizedSort("KeywordArguments", new Type[] { $T4 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       ADT_Maybe_Production = $parameterizedAdt("Maybe", new Type[] { ADT_Production });
       NT_Mapping_Pattern = $parameterizedSort("Mapping", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       $T5 = $TF.parameterType("T", ADT_Tree);
       NT_Mapping_Expression = $parameterizedSort("Mapping", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       NT_KeywordArguments_Expression = $parameterizedSort("KeywordArguments", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       NT_Mapping_1 = $parameterizedSort("Mapping", new Type[] { $T4 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       ADT_Maybe_1 = $parameterizedAdt("Maybe", new Type[] { $T6 });
       NT_KeywordArguments_Pattern = $parameterizedSort("KeywordArguments", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       $T0 = $TF.listType($T2);
       NT_KeywordArgument_1 = $parameterizedSort("KeywordArgument", new Type[] { $T4 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       ADT_Maybe_Symbol = $parameterizedAdt("Maybe", new Type[] { ADT_Symbol });
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T4 });
       ADT_Maybe_Associativity = $parameterizedAdt("Maybe", new Type[] { ADT_Associativity });
       ADT_KeywordArguments_1 = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { $T4 });
       ADT_Mapping_Pattern = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Pattern });
       ADT_Mapping_Expression = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Expression });
       ADT_KeywordArguments_Expression = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Expression });
       ADT_Mapping_1 = $TF.abstractDataType($TS, "Mapping", new Type[] { $T4 });
       ADT_KeywordArguments_Pattern = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Pattern });
       ADT_KeywordArgument_1 = $TF.abstractDataType($TS, "KeywordArgument", new Type[] { $T4 });
    
       ParserBaseLoc = ((ISourceLocation)($create_aloc(((IString)$constants.get(108)/*"project://rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/generated_parsers/"*/))));
    
       
    }
    public IBool tstExpandParameterizedSymbols4(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstExpandParameterizedSymbols4$bff4c6ef51ef72c6();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstExpandParameterizedSymbols1(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstExpandParameterizedSymbols1$0841c20c388175c8();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IValue head(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.head($P0);
    }
    public IConstructor choice(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_Type.choice($P0, $P1);
    }
    public IBool tstExpandParameterizedSymbols2(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstExpandParameterizedSymbols2$eb5ad6d10cecc5e4();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IString getParserMethodName(IValue $P0){ // Generated by Resolver
       return (IString) M_lang_rascal_grammar_ParserGenerator.getParserMethodName($P0);
    }
    public IInteger size(IValue $P0){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IInteger)M_List.List_size$ba7443328d8b4a27((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IInteger)M_String.String_size$4611676944e933d5((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool tstExpandParameterizedSymbols3(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstExpandParameterizedSymbols3$5c3e8bbe9623842a();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstExpandParameterizedSymbols5(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstExpandParameterizedSymbols5$4798a5517a5aea97();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstNewGenerateGEMPTY(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstNewGenerateGEMPTY$3ffe02b6dd93ee4e();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstGenerateNewItems1(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstGenerateNewItems1$f0906250d019df65();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IConstructor grammar(IValue $P0){ // Generated by Resolver
       return (IConstructor) M_Grammar.grammar($P0);
    }
    public IConstructor grammar(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_Grammar.grammar($P0, $P1);
    }
    public IBool tstGenerateNewItems2(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstGenerateNewItems2$4608cf9bff693c0b();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstGenerateNewItems3(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstGenerateNewItems3$125f77ee68340d60();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstNewGenerateG0(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstNewGenerateG0$626d69ed168bf92a();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstComputeDontNests1(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstComputeDontNests1$178065f57b4fed4b();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstUnique0(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstUnique0$099a35bdf29c9ce7();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstComputeDontNests2(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstComputeDontNests2$86b7bef9c88d6554();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstUnique1(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstUnique1$c4ceda669e9faa4d();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstComputeDontNests3(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstComputeDontNests3$4cbf45cd51bd7a8a();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstUnique2(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstUnique2$bfc17195569aa30c();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstComputeDontNests4(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstComputeDontNests4$29d1a74761dcb91d();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public void println(IValue $P0){ // Generated by Resolver
        M_IO.println($P0);
    }
    public void println(){ // Generated by Resolver
        M_IO.println();
    }
    public IList tail(IValue $P0){ // Generated by Resolver
       return (IList) M_List.tail($P0);
    }
    public IConstructor getType(IValue $P0){ // Generated by Resolver
       return (IConstructor) M_lang_rascal_grammar_ParserGenerator.getType($P0);
    }
    public IBool tstEsc1(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc1$daafe86c4d28093e();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstEsc2(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc2$f53994ad485c37bc();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstEsc3(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc3$d1531db13a74cd8a();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstEsc4(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc4$cd668b916d83a82f();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstEsc5(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc5$20bd8083e7f8b79a();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IString esc(IValue $P0){ // Generated by Resolver
       return (IString) M_lang_rascal_grammar_ParserGenerator.esc($P0);
    }
    public IBool tstEsc6(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc6$1f0d5c4a361fc470();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IList removeEmptyLines(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IList)lang_rascal_grammar_tests_ParserGeneratorTests_removeEmptyLines$9fbb4822aae1800c((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool tstNewGenerateGEXP(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstNewGenerateGEXP$f6b27f467ead5c91();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstEsc7(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc7$b259f88a2232a422();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstLiterals1(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstLiterals1$b1b9d1b2d10cdb63();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstEsc8(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc8$3ece6d2670010dc0();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstLiterals2(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstLiterals2$cda1b8838486a95f();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstEsc9(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc9$c0477f400e811ebd();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IValue main(){ // Generated by Resolver
       IValue $result = null;
       $result = (IValue)lang_rascal_grammar_tests_ParserGeneratorTests_main$cf8e6ccdc28df2e3();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool sameLines(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T3) && $isSubtypeOf($P1Type,$T3)){
         $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_sameLines$9f2b894a20ecc2fd((IString) $P0, (IString) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool tstLiterals3(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstLiterals3$22b279a978cc4f0f();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IValue split(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T3)){
         $result = (IValue)M_lang_rascal_grammar_ParserGenerator.lang_rascal_grammar_ParserGenerator_split$452f305967b0884c((IString) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IValue)M_List.List_split$19c747b75c8a251d((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList split(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_String.split($P0, $P1);
    }
    public IBool tstLiterals4(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstLiterals4$695f65df3e2234ce();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IBool tstNewGenerateGEXPPRIO(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstNewGenerateGEXPPRIO$a7647a8a5293299c();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public IValue sort(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IValue)M_List.List_sort$1fe4426c8c8039da((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T3)){
         return $RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{(IString) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList sort(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.sort($P0, $P1);
    }
    public IBool tstEsc10(){ // Generated by Resolver
       IBool $result = null;
       $result = (IBool)lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc10$2b81cb9a31f6bb6e();
       if($result != null) return $result;
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }
    public void generateParsers(){ // Generated by Resolver
       try { lang_rascal_grammar_tests_ParserGeneratorTests_generateParsers$41133e6df5c3877f(); return; } catch (FailReturnFromVoidException e){};
       
       throw RuntimeExceptionFactory.callFailed($RVF.list());
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(377,58,<14,0>,<14,58>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc1$daafe86c4d28093e(){ 
        
        
        return ((IBool)($equal(((IString)(M_lang_rascal_grammar_ParserGenerator.esc(((IConstructor)$constants.get(0)/*sort("S")*/)))), ((IString)$constants.get(1)/*"sort(\"S\")"*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(436,56,<15,0>,<15,56>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc2$f53994ad485c37bc(){ 
        
        
        return ((IBool)($equal(((IString)(M_lang_rascal_grammar_ParserGenerator.esc(((IConstructor)$constants.get(2)/*lit(":")*/)))), ((IString)$constants.get(3)/*"lit(\":\")"*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(493,64,<16,0>,<16,64>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc3$d1531db13a74cd8a(){ 
        
        
        return ((IBool)($equal(((IString)(M_lang_rascal_grammar_ParserGenerator.esc(((IConstructor)$constants.get(4)/*lit("\"")*/)))), ((IString)$constants.get(5)/*"lit(\"\\\"\")"*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(559,62,<18,0>,<18,62>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc4$cd668b916d83a82f(){ 
        
        
        final Template $template0 = (Template)new Template($RVF, "");
        $template0.addStr(((IString)(M_lang_rascal_grammar_ParserGenerator.esc(((IConstructor)$constants.get(0)/*sort("S")*/)))).getValue());
        return ((IBool)($equal(((IString)($template0.close())), ((IString)$constants.get(1)/*"sort(\"S\")"*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(622,68,<19,0>,<19,68>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc5$20bd8083e7f8b79a(){ 
        
        
        final Template $template1 = (Template)new Template($RVF, "");
        $template1.addStr(((IString)(M_lang_rascal_grammar_ParserGenerator.esc(((IConstructor)$constants.get(4)/*lit("\"")*/)))).getValue());
        return ((IBool)($equal(((IString)($template1.close())), ((IString)$constants.get(5)/*"lit(\"\\\"\")"*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(692,66,<21,0>,<21,66>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc6$1f0d5c4a361fc470(){ 
        
        
        final Template $template2 = (Template)new Template($RVF, "");
        $template2.addStr(((IString)(M_lang_rascal_grammar_ParserGenerator.esc(((IConstructor)$constants.get(0)/*sort("S")*/)))).getValue());
        final Template $template3 = (Template)new Template($RVF, "");
        $template3.addStr("sort(\\\"S\\\")");
        return ((IBool)($equal(((IString)($template2.close())), ((IString)($template3.close())))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(759,64,<22,0>,<22,64>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc7$b259f88a2232a422(){ 
        
        
        final Template $template4 = (Template)new Template($RVF, "");
        $template4.addStr(((IString)(M_lang_rascal_grammar_ParserGenerator.esc(((IConstructor)$constants.get(2)/*lit(":")*/)))).getValue());
        final Template $template5 = (Template)new Template($RVF, "");
        $template5.addStr("lit(\\\":\\\")");
        return ((IBool)($equal(((IString)($template4.close())), ((IString)($template5.close())))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(824,72,<23,0>,<23,72>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc8$3ece6d2670010dc0(){ 
        
        
        final Template $template6 = (Template)new Template($RVF, "");
        $template6.addStr(((IString)(M_lang_rascal_grammar_ParserGenerator.esc(((IConstructor)$constants.get(4)/*lit("\"")*/)))).getValue());
        final Template $template7 = (Template)new Template($RVF, "");
        $template7.addStr("lit(\\\"\\\\\\\"\\\")");
        return ((IBool)($equal(((IString)($template6.close())), ((IString)($template7.close())))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(898,114,<25,0>,<25,114>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc9$c0477f400e811ebd(){ 
        
        
        final Template $template8 = (Template)new Template($RVF, "");
        /*muExists*/LAB0: 
            do {
                LAB0_GEN926:
                for(IValue $elem9_for : ((IList)$constants.get(6)/*[sort("S"),lit("\"")]*/)){
                    IConstructor $elem9 = (IConstructor) $elem9_for;
                    IConstructor s_0 = ((IConstructor)($elem9));
                    ;$template8.addStr(((IString)(M_lang_rascal_grammar_ParserGenerator.esc(((IConstructor)s_0)))).getValue());
                
                }
                continue LAB0;
                            
            } while(false);
        return ((IBool)($equal(((IString)($template8.close())), ((IString)$constants.get(7)/*"sort(\"S\")lit(\"\\\"\")"*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(1013,135,<26,0>,<26,135>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstEsc10$2b81cb9a31f6bb6e(){ 
        
        
        final Template $template10 = (Template)new Template($RVF, "\"");
        /*muExists*/LAB1: 
            do {
                LAB1_GEN1044:
                for(IValue $elem11_for : ((IList)$constants.get(6)/*[sort("S"),lit("\"")]*/)){
                    IConstructor $elem11 = (IConstructor) $elem11_for;
                    IConstructor s_0 = ((IConstructor)($elem11));
                    $template10.addStr("\"");
                    $template10.beginIndent("  ");
                    $template10.addStr(((IString)(M_lang_rascal_grammar_ParserGenerator.esc(((IConstructor)s_0)))).getValue());
                    $template10.endIndent("  ");
                    $template10.addStr("\"");
                
                }
                continue LAB1;
                            
            } while(false);
        $template10.addStr("\"");
        return ((IBool)($equal(((IString)($template10.close())), ((IString)$constants.get(8)/*"""sort(\"S\")""lit(\"\\\"\")"""*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(1150,90,<28,0>,<28,90>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstExpandParameterizedSymbols1$0841c20c388175c8(){ 
        
        
        return ((IBool)($equal(((IConstructor)(M_lang_rascal_grammar_definition_Parameters.expandParameterizedSymbols(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEMPTY)))), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEMPTY))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(1241,82,<29,0>,<29,82>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstExpandParameterizedSymbols2$eb5ad6d10cecc5e4(){ 
        
        
        return ((IBool)($equal(((IConstructor)(M_lang_rascal_grammar_definition_Parameters.expandParameterizedSymbols(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.G0)))), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.G0))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(1324,86,<30,0>,<30,86>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstExpandParameterizedSymbols3$5c3e8bbe9623842a(){ 
        
        
        return ((IBool)($equal(((IConstructor)(M_lang_rascal_grammar_definition_Parameters.expandParameterizedSymbols(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXP)))), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXP))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(1411,94,<31,0>,<31,94>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstExpandParameterizedSymbols4$bff4c6ef51ef72c6(){ 
        
        
        return ((IBool)($equal(((IConstructor)(M_lang_rascal_grammar_definition_Parameters.expandParameterizedSymbols(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXPPRIO)))), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXPPRIO))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(1511,54,<33,0>,<33,54>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstLiterals1$b1b9d1b2d10cdb63(){ 
        
        
        return ((IBool)($equal(((IConstructor)(M_lang_rascal_grammar_definition_Literals.literals(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEMPTY)))), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEMPTY))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(1566,46,<34,0>,<34,46>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstLiterals2$cda1b8838486a95f(){ 
        
        
        return ((IBool)($equal(((IConstructor)(M_lang_rascal_grammar_definition_Literals.literals(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.G0)))), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.G0))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(1613,50,<35,0>,<35,50>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstLiterals3$22b279a978cc4f0f(){ 
        
        
        return ((IBool)($equal(((IConstructor)(M_lang_rascal_grammar_definition_Literals.literals(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXP)))), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXP))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(1664,58,<36,0>,<36,58>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstLiterals4$695f65df3e2234ce(){ 
        
        
        return ((IBool)($equal(((IConstructor)(M_lang_rascal_grammar_definition_Literals.literals(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXPPRIO)))), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXPPRIO))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(1724,77,<38,0>,<40,6>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstUnique0$099a35bdf29c9ce7(){ 
        
        
        return ((IBool)($equal(((IConstructor)(M_lang_rascal_grammar_ParserGenerator.makeUnique(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEMPTY)))), ((IConstructor)$constants.get(9)/*grammar({sort("S")},())*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(1806,375,<42,1>,<59,5>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstUnique1$c4ceda669e9faa4d(){ 
        
        
        return ((IBool)($equal(((IConstructor)(M_lang_rascal_grammar_ParserGenerator.makeUnique(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.G0)))), ((IConstructor)($RVF.constructor(M_Grammar.Grammar_grammar_set_Symbol_map_Symbol_Production, new IValue[]{((ISet)$constants.get(10)/*{sort("S")}*/), ((IMap)($buildMap(((IConstructor)$constants.get(0)/*sort("S")*/), ((IConstructor)(M_Type.choice(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)$constants.get(11)/*"S"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(12)/*2*/))))), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)$constants.get(11)/*"S"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(13)/*3*/))))), ((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)$constants.get(14)/*"0"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(15)/*4*/)))))))), ((ISet)$constants.get(16)/*{}*/)}))))))))), ((IConstructor)$constants.get(17)/*lit("0")*/), M_Type.choice(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)$constants.get(14)/*"0"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(18)/*5*/))))), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)$constants.get(14)/*"0"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(19)/*6*/))))), ((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)$constants.get(20)/*[range(48,48)]*/)}, Util.kwpMap("id", ((IInteger)$constants.get(21)/*7*/)))))))), ((ISet)$constants.get(16)/*{}*/)}))))))))))}))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(2187,227,<61,2>,<68,3>) 
    public IValue lang_rascal_grammar_tests_ParserGeneratorTests_main$cf8e6ccdc28df2e3(){ 
        
        
        IConstructor g_0 = ((IConstructor)$constants.get(9)/*grammar({sort("S")},())*/);
        final ISetWriter $setwriter12 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP13_GEN2282:
        for(IValue $elem14_for : ((IConstructor)g_0)){
            IValue $elem14 = (IValue) $elem14_for;
            $SCOMP13_GEN2282_DESC2282:
            for(IValue $elem15 : new DescendantMatchIterator($elem14, 
                new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                if($isComparable($elem15.getType(), M_ParseTree.ADT_Symbol)){
                   if($has_type_and_arity($elem15, M_ParseTree.Symbol_lit_str, 1)){
                      IValue $arg0_16 = (IValue)($subscript_int(((IValue)($elem15)),0));
                      if($isComparable($arg0_16.getType(), $T3)){
                         IString s_2 = ((IString)($arg0_16));
                         $setwriter12.insert(M_lang_rascal_grammar_definition_Literals.literal(((IString)($arg0_16))));
                      
                      } else {
                         continue $SCOMP13_GEN2282_DESC2282;
                      }
                   } else {
                      continue $SCOMP13_GEN2282_DESC2282;
                   }
                } else {
                   continue $SCOMP13_GEN2282_DESC2282;
                }
            }
            continue $SCOMP13_GEN2282;
                         
        }
        
                    IConstructor el_1 = ((IConstructor)(M_Grammar.compose(((IConstructor)g_0), ((IConstructor)(M_Grammar.grammar(((ISet)$constants.get(16)/*{}*/), ((ISet)($setwriter12.done()))))))));
        M_IO.println(((IString)$constants.get(22)/*"el:"*/));
        M_IO.iprintln(((IValue)el_1), Util.kwpMap());
        M_IO.println(((IString)$constants.get(23)/*"g"*/));
        M_IO.iprintln(((IValue)g_0), Util.kwpMap());
        final Template $template17 = (Template)new Template($RVF, "g == el: ");
        $template17.beginIndent("         ");
        $template17.addVal($equal(((IConstructor)g_0), ((IConstructor)el_1)));
        $template17.endIndent("         ");
        M_IO.println(((IValue)($template17.close())));
        return ((IInteger)$constants.get(24)/*0*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(2416,1481,<70,0>,<141,5>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstUnique2$bfc17195569aa30c(){ 
        
        
        return ((IBool)($equal(((IConstructor)(M_lang_rascal_grammar_ParserGenerator.makeUnique(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXP)))), ((IConstructor)($RVF.constructor(M_Grammar.Grammar_grammar_set_Symbol_map_Symbol_Production, new IValue[]{((ISet)$constants.get(25)/*{sort("E")}*/), ((IMap)($buildMap(((IConstructor)$constants.get(26)/*lit("+")*/), ((IConstructor)(M_Type.choice(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)$constants.get(27)/*"+"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(12)/*2*/))))), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)$constants.get(27)/*"+"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(13)/*3*/))))), ((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)$constants.get(28)/*[range(43,43)]*/)}, Util.kwpMap("id", ((IInteger)$constants.get(15)/*4*/)))))))), ((ISet)$constants.get(16)/*{}*/)}))))))))), ((IConstructor)$constants.get(29)/*lit("*")*/), M_Type.choice(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)$constants.get(30)/*"*"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(18)/*5*/))))), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)$constants.get(30)/*"*"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(19)/*6*/))))), ((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)$constants.get(31)/*[range(42,42)]*/)}, Util.kwpMap("id", ((IInteger)$constants.get(21)/*7*/)))))))), ((ISet)$constants.get(16)/*{}*/)}))))))), ((IConstructor)$constants.get(32)/*sort("B")*/), M_Type.choice(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)$constants.get(33)/*"B"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(34)/*8*/))))), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)$constants.get(33)/*"B"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(35)/*11*/))))), ((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)$constants.get(36)/*"1"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(37)/*12*/)))))))), ((ISet)$constants.get(16)/*{}*/)}))), $RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)$constants.get(33)/*"B"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(38)/*9*/))))), ((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)$constants.get(14)/*"0"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(39)/*10*/)))))))), ((ISet)$constants.get(16)/*{}*/)}))))), ((IConstructor)$constants.get(17)/*lit("0")*/), M_Type.choice(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)$constants.get(14)/*"0"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(40)/*13*/))))), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)$constants.get(14)/*"0"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(41)/*14*/))))), ((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)$constants.get(20)/*[range(48,48)]*/)}, Util.kwpMap("id", ((IInteger)$constants.get(42)/*15*/)))))))), ((ISet)$constants.get(16)/*{}*/)}))))))), ((IConstructor)$constants.get(43)/*sort("E")*/), M_Type.choice(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)$constants.get(44)/*"E"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(45)/*16*/))))), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)$constants.get(44)/*"E"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(46)/*23*/))))), ((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)$constants.get(44)/*"E"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(47)/*24*/))))), $RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)$constants.get(30)/*"*"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(48)/*25*/))), $RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)$constants.get(33)/*"B"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(49)/*26*/)))))), ((ISet)$constants.get(16)/*{}*/)}))), $RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)$constants.get(44)/*"E"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(50)/*17*/))))), ((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)$constants.get(33)/*"B"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(51)/*18*/)))))))), ((ISet)$constants.get(16)/*{}*/)}), $RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)$constants.get(44)/*"E"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(52)/*19*/))))), ((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)$constants.get(44)/*"E"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(53)/*20*/))))), $RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)$constants.get(27)/*"+"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(54)/*21*/))), $RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)$constants.get(33)/*"B"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(55)/*22*/)))))), ((ISet)$constants.get(16)/*{}*/)}))))), ((IConstructor)$constants.get(56)/*lit("1")*/), M_Type.choice(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)$constants.get(36)/*"1"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(57)/*27*/))))), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)$constants.get(36)/*"1"*/)}, Util.kwpMap("id", ((IInteger)$constants.get(58)/*28*/))))), ((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)$constants.get(59)/*[range(49,49)]*/)}, Util.kwpMap("id", ((IInteger)$constants.get(60)/*29*/)))))))), ((ISet)$constants.get(16)/*{}*/)}))))))))))}))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(3899,78,<143,0>,<143,78>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstGenerateNewItems1$f0906250d019df65(){ 
        
        
        return ((IBool)($equal(((IMap)(M_lang_rascal_grammar_ParserGenerator.generateNewItems(((IConstructor)(M_lang_rascal_grammar_ParserGenerator.makeUnique(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEMPTY))))))), ((IMap)$constants.get(61)/*()*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(3979,504,<145,0>,<159,2>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstGenerateNewItems2$4608cf9bff693c0b(){ 
        
        
        return ((IBool)($equal(((IMap)(M_lang_rascal_grammar_ParserGenerator.generateNewItems(((IConstructor)(M_lang_rascal_grammar_ParserGenerator.makeUnique(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.G0))))))), ((IMap)($buildMap(((IConstructor)$constants.get(0)/*sort("S")*/), ((IMap)($buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)$constants.get(62)/*prod(sort("S"),[lit("0")],{})*/), ((IInteger)$constants.get(24)/*0*/)}))), ((ITuple)$constants.get(63)/*<"new LiteralStackNode\<IConstructor\>(4, 0, cHJvZChsaXQoIjAiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ4LDQ4KV0 ...*/)))), ((IConstructor)$constants.get(17)/*lit("0")*/), $buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)$constants.get(64)/*prod(lit("0"),[\char-class([range(48,48)])],{})*/), ((IInteger)$constants.get(24)/*0*/)}))), ((ITuple)$constants.get(66)/*<"new CharStackNode\<IConstructor\>(7, 0, new int[][]{{48,48}}, null, null)",7>*/))))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(4485,2964,<161,0>,<269,2>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstGenerateNewItems3$125f77ee68340d60(){ 
        
        
        return ((IBool)($equal(((IMap)(M_lang_rascal_grammar_ParserGenerator.generateNewItems(((IConstructor)(M_lang_rascal_grammar_ParserGenerator.makeUnique(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXP))))))), ((IMap)($buildMap(((IConstructor)$constants.get(26)/*lit("+")*/), ((IMap)($buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)$constants.get(67)/*prod(lit("+"),[\char-class([range(43,43)])],{})*/), ((IInteger)$constants.get(24)/*0*/)}))), ((ITuple)$constants.get(69)/*<"new CharStackNode\<IConstructor\>(4, 0, new int[][]{{43,43}}, null, null)",4>*/)))), ((IConstructor)$constants.get(29)/*lit("*")*/), $buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)$constants.get(70)/*prod(lit("*"),[\char-class([range(42,42)])],{})*/), ((IInteger)$constants.get(24)/*0*/)}))), ((ITuple)$constants.get(72)/*<"new CharStackNode\<IConstructor\>(7, 0, new int[][]{{42,42}}, null, null)",7>*/)), ((IConstructor)$constants.get(32)/*sort("B")*/), $buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)$constants.get(73)/*prod(sort("B"),[lit("0")],{})*/), ((IInteger)$constants.get(24)/*0*/)}))), ((ITuple)$constants.get(74)/*<"new LiteralStackNode\<IConstructor\>(10, 0, cHJvZChsaXQoIjAiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ4LDQ4KV ...*/), $RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)$constants.get(75)/*prod(sort("B"),[lit("1")],{})*/), ((IInteger)$constants.get(24)/*0*/)}), ((ITuple)$constants.get(76)/*<"new LiteralStackNode\<IConstructor\>(12, 0, cHJvZChsaXQoIjEiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQ5LDQ5KV ...*/)), ((IConstructor)$constants.get(17)/*lit("0")*/), $buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)$constants.get(64)/*prod(lit("0"),[\char-class([range(48,48)])],{})*/), ((IInteger)$constants.get(24)/*0*/)}))), ((ITuple)$constants.get(77)/*<"new CharStackNode\<IConstructor\>(15, 0, new int[][]{{48,48}}, null, null)",15>*/)), ((IConstructor)$constants.get(43)/*sort("E")*/), $buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)$constants.get(78)/*prod(sort("E"),[sort("B")],{})*/), ((IInteger)$constants.get(24)/*0*/)}))), ((ITuple)$constants.get(79)/*<"new NonTerminalStackNode\<IConstructor\>(18, 0, \"B\", null, null)",18>*/), $RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)$constants.get(80)/*prod(sort("E"),[sort("E"),lit("*"),sort("B")],{})*/), ((IInteger)$constants.get(81)/*1*/)}), ((ITuple)$constants.get(82)/*<"new LiteralStackNode\<IConstructor\>(25, 1, cHJvZChsaXQoIioiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQyLDQyKV ...*/), $RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)$constants.get(83)/*prod(sort("E"),[sort("E"),lit("+"),sort("B")],{})*/), ((IInteger)$constants.get(81)/*1*/)}), ((ITuple)$constants.get(84)/*<"new LiteralStackNode\<IConstructor\>(21, 1, cHJvZChsaXQoIisiKSxbXGNoYXItY2xhc3MoW3JhbmdlKDQzLDQzKV ...*/), $RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)$constants.get(83)/*prod(sort("E"),[sort("E"),lit("+"),sort("B")],{})*/), ((IInteger)$constants.get(24)/*0*/)}), ((ITuple)$constants.get(85)/*<"new NonTerminalStackNode\<IConstructor\>(20, 0, \"E\", null, null)",20>*/), $RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)$constants.get(80)/*prod(sort("E"),[sort("E"),lit("*"),sort("B")],{})*/), ((IInteger)$constants.get(24)/*0*/)}), ((ITuple)$constants.get(86)/*<"new NonTerminalStackNode\<IConstructor\>(24, 0, \"E\", null, null)",24>*/), $RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)$constants.get(80)/*prod(sort("E"),[sort("E"),lit("*"),sort("B")],{})*/), ((IInteger)$constants.get(12)/*2*/)}), ((ITuple)$constants.get(87)/*<"new NonTerminalStackNode\<IConstructor\>(26, 2, \"B\", null, null)",26>*/), $RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)$constants.get(83)/*prod(sort("E"),[sort("E"),lit("+"),sort("B")],{})*/), ((IInteger)$constants.get(12)/*2*/)}), ((ITuple)$constants.get(88)/*<"new NonTerminalStackNode\<IConstructor\>(22, 2, \"B\", null, null)",22>*/)), ((IConstructor)$constants.get(56)/*lit("1")*/), $buildMap(((IConstructor)($RVF.constructor(M_Grammar.Item_item_Production_int, new IValue[]{((IConstructor)$constants.get(89)/*prod(lit("1"),[\char-class([range(49,49)])],{})*/), ((IInteger)$constants.get(24)/*0*/)}))), ((ITuple)$constants.get(91)/*<"new CharStackNode\<IConstructor\>(29, 0, new int[][]{{49,49}}, null, null)",29>*/))))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(7451,124,<271,0>,<271,124>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstComputeDontNests1$178065f57b4fed4b(){ 
        
        
        return ((IBool)($equal(((ISet)(M_lang_rascal_grammar_ParserGenerator.computeDontNests(((IMap)(M_lang_rascal_grammar_ParserGenerator.generateNewItems(((IConstructor)(M_lang_rascal_grammar_ParserGenerator.makeUnique(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEMPTY))))))), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEMPTY), ((IConstructor)(M_lang_rascal_grammar_ParserGenerator.makeUnique(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEMPTY))))))), ((ISet)$constants.get(16)/*{}*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(7576,112,<272,0>,<272,112>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstComputeDontNests2$86b7bef9c88d6554(){ 
        
        
        return ((IBool)($equal(((ISet)(M_lang_rascal_grammar_ParserGenerator.computeDontNests(((IMap)(M_lang_rascal_grammar_ParserGenerator.generateNewItems(((IConstructor)(M_lang_rascal_grammar_ParserGenerator.makeUnique(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.G0))))))), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.G0), ((IConstructor)(M_lang_rascal_grammar_ParserGenerator.makeUnique(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.G0))))))), ((ISet)$constants.get(16)/*{}*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(7689,118,<273,0>,<273,118>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstComputeDontNests3$4cbf45cd51bd7a8a(){ 
        
        
        return ((IBool)($equal(((ISet)(M_lang_rascal_grammar_ParserGenerator.computeDontNests(((IMap)(M_lang_rascal_grammar_ParserGenerator.generateNewItems(((IConstructor)(M_lang_rascal_grammar_ParserGenerator.makeUnique(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXP))))))), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXP), ((IConstructor)(M_lang_rascal_grammar_ParserGenerator.makeUnique(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXP))))))), ((ISet)$constants.get(16)/*{}*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(7808,130,<274,0>,<274,130>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstComputeDontNests4$29d1a74761dcb91d(){ 
        
        
        return ((IBool)($equal(((ISet)(M_lang_rascal_grammar_ParserGenerator.computeDontNests(((IMap)(M_lang_rascal_grammar_ParserGenerator.generateNewItems(((IConstructor)(M_lang_rascal_grammar_ParserGenerator.makeUnique(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXPPRIO))))))), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXPPRIO), ((IConstructor)(M_lang_rascal_grammar_ParserGenerator.makeUnique(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXPPRIO))))))), ((ISet)$constants.get(16)/*{}*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(7940,352,<276,0>,<292,5>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstExpandParameterizedSymbols5$4798a5517a5aea97(){ 
        
        
        return ((IBool)($equal(((IConstructor)(M_lang_rascal_grammar_definition_Parameters.expandParameterizedSymbols(((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.G0)))), ((IConstructor)($RVF.constructor(M_Grammar.Grammar_grammar_set_Symbol_map_Symbol_Production, new IValue[]{((ISet)$constants.get(10)/*{sort("S")}*/), ((IMap)($buildMap(((IConstructor)$constants.get(0)/*sort("S")*/), ((IConstructor)(M_Type.choice(((IConstructor)$constants.get(0)/*sort("S")*/), ((ISet)$constants.get(92)/*{prod(sort("S"),[lit("0")],{})}*/)))), ((IConstructor)$constants.get(17)/*lit("0")*/), M_Type.choice(((IConstructor)$constants.get(17)/*lit("0")*/), ((ISet)$constants.get(93)/*{prod(lit("0"),[\char-class([range(48,48)])],{})}*/)))))}))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(8408,658,<296,0>,<301,1>) 
    public void lang_rascal_grammar_tests_ParserGeneratorTests_generateParsers$41133e6df5c3877f(){ 
        
        
        M_IO.writeFile(((ISourceLocation)($aloc_add_astr(((ISourceLocation)ParserBaseLoc),((IString)$constants.get(94)/*"GEMPTYParser.java.gz"*/)))), $RVF.list(M_lang_rascal_grammar_ParserGenerator.newGenerate(((IString)$constants.get(95)/*"org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers"*/), ((IString)$constants.get(96)/*"GEMPTYParser"*/), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEMPTY))), Util.kwpMap());
        M_IO.writeFile(((ISourceLocation)($aloc_add_astr(((ISourceLocation)ParserBaseLoc),((IString)$constants.get(97)/*"G0Parser.java.gz"*/)))), $RVF.list(M_lang_rascal_grammar_ParserGenerator.newGenerate(((IString)$constants.get(95)/*"org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers"*/), ((IString)$constants.get(98)/*"G0Parser"*/), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.G0))), Util.kwpMap());
        M_IO.writeFile(((ISourceLocation)($aloc_add_astr(((ISourceLocation)ParserBaseLoc),((IString)$constants.get(99)/*"GEXPParser.java.gz"*/)))), $RVF.list(M_lang_rascal_grammar_ParserGenerator.newGenerate(((IString)$constants.get(95)/*"org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers"*/), ((IString)$constants.get(100)/*"GEXPParser"*/), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXP))), Util.kwpMap());
        M_IO.writeFile(((ISourceLocation)($aloc_add_astr(((ISourceLocation)ParserBaseLoc),((IString)$constants.get(101)/*"GEXPPRIOParser.java.gz"*/)))), $RVF.list(M_lang_rascal_grammar_ParserGenerator.newGenerate(((IString)$constants.get(95)/*"org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers"*/), ((IString)$constants.get(102)/*"GEXPPRIOParser"*/), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXPPRIO))), Util.kwpMap());
        return;
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(9069,102,<303,0>,<304,58>) 
    public IList lang_rascal_grammar_tests_ParserGeneratorTests_removeEmptyLines$9fbb4822aae1800c(IString s_0){ 
        
        
        final IListWriter $listwriter18 = (IListWriter)$RVF.listWriter();
        $LCOMP19_GEN9126:
        for(IValue $elem22_for : ((IList)(M_String.split(((IString)$constants.get(103)/*"
        "*/), ((IString)s_0))))){
            IString $elem22 = (IString) $elem22_for;
            IString line_1 = null;
            final Matcher $matcher20 = (Matcher)$regExpCompile("^[ \t]*$", ((IString)($elem22)).getValue());
            boolean $found21 = true;
            
                while($found21){
                    $found21 = $matcher20.find();
                    if($found21){
                       $listwriter18.append($elem22);
                    
                    } else {
                       break $LCOMP19_GEN9126; // muSucceed
                    }
                }
        
        }
        
                    return ((IList)($listwriter18.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(9173,88,<306,0>,<306,88>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_sameLines$9f2b894a20ecc2fd(IString s1_0, IString s2_1){ 
        
        
        return ((IBool)($equal(((IInteger)(M_List.size(((IList)(((IList)($me.removeEmptyLines(((IString)s1_0)))).subtract(((IList)($me.removeEmptyLines(((IString)s2_1)))))))))), ((IInteger)$constants.get(24)/*0*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(9264,247,<308,0>,<310,93>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstNewGenerateGEMPTY$3ffe02b6dd93ee4e(){ 
        
        
        return ((IBool)($me.sameLines(((IString)(M_lang_rascal_grammar_ParserGenerator.newGenerate(((IString)$constants.get(95)/*"org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers"*/), ((IString)$constants.get(96)/*"GEMPTYParser"*/), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEMPTY)))), ((IString)(M_IO.readFile(((ISourceLocation)($create_aloc(((IString)$constants.get(104)/*"std:///lang/rascal/grammar/tests/generated_parsers/GEMPTYParser.java.gz"*/)))), Util.kwpMap()))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(9521,234,<312,0>,<314,92>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstNewGenerateG0$626d69ed168bf92a(){ 
        
        
        return ((IBool)($me.sameLines(((IString)(M_lang_rascal_grammar_ParserGenerator.newGenerate(((IString)$constants.get(95)/*"org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers"*/), ((IString)$constants.get(98)/*"G0Parser"*/), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.G0)))), ((IString)(M_IO.readFile(((ISourceLocation)($create_aloc(((IString)$constants.get(105)/*"std:///lang/rascal/grammar/tests/generated_parsers/G0Parser.java.gz"*/)))), Util.kwpMap()))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(9768,232,<316,0>,<318,84>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstNewGenerateGEXP$f6b27f467ead5c91(){ 
        
        
        return ((IBool)($me.sameLines(((IString)(M_lang_rascal_grammar_ParserGenerator.newGenerate(((IString)$constants.get(95)/*"org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers"*/), ((IString)$constants.get(100)/*"GEXPParser"*/), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXP)))), ((IString)(M_IO.readFile(((ISourceLocation)($create_aloc(((IString)$constants.get(106)/*"std:///lang/rascal/grammar/tests/generated_parsers/GEXPParser.java.gz"*/)))), Util.kwpMap()))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/tests/ParserGeneratorTests.rsc|(10003,255,<320,0>,<322,95>) 
    public IBool lang_rascal_grammar_tests_ParserGeneratorTests_tstNewGenerateGEXPPRIO$a7647a8a5293299c(){ 
        
        
        return ((IBool)($me.sameLines(((IString)(M_lang_rascal_grammar_ParserGenerator.newGenerate(((IString)$constants.get(95)/*"org.rascalmpl.library.lang.rascal.grammar.tests.generated_parsers"*/), ((IString)$constants.get(102)/*"GEXPPRIOParser"*/), ((IConstructor)M_lang_rascal_grammar_tests_TestGrammars.GEXPPRIO)))), ((IString)(M_IO.readFile(((ISourceLocation)($create_aloc(((IString)$constants.get(107)/*"std:///lang/rascal/grammar/tests/generated_parsers/GEXPPRIOParser.java.gz"*/)))), Util.kwpMap()))))));
    
    }
    

    public static void main(String[] args) {
      long start_time = System.currentTimeMillis();
      RascalExecutionContext rex = new RascalExecutionContext(new InputStreamReader(System.in), new PrintWriter(System.out), new PrintWriter(System.err), null, null, rascal.lang.rascal.grammar.tests.$ParserGeneratorTests.class);
      $ParserGeneratorTests instance = new $ParserGeneratorTests(rex);
      long init_time = System.currentTimeMillis();
      
      IValue res = instance.lang_rascal_grammar_tests_ParserGeneratorTests_main$cf8e6ccdc28df2e3();
      
      long end_time = System.currentTimeMillis();
      if (res == null) {
         throw new RuntimeException("Main function failed"); 
      } else {
         System.out.println(res);
      }
      System.err.println("Running lang.rascal.grammar.tests.$ParserGeneratorTests: init: " + (init_time - start_time) + " ms, exec: " + (end_time - init_time) + " ms, total: " + (end_time - start_time) + " ms"); 
                        
    }
}