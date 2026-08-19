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
public class $Characters 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.lang.rascal.grammar.definition.$Characters_$I {

    private final $Characters_$I $me;
    private final IList $constants;
    
    
    public final rascal.lang.rascal.syntax.$Rascal M_lang_rascal_syntax_Rascal;
    public final rascal.$Exception M_Exception;
    public final rascal.$Type M_Type;
    public final rascal.$List M_List;
    public final rascal.$Grammar M_Grammar;
    public final rascal.$Message M_Message;
    public final rascal.$String M_String;
    public final rascal.$ParseTree M_ParseTree;

    
    
    public final io.usethesource.vallang.type.Type $T4;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T1;	/*aint()*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T2;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Item;	/*aadt("Item",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarModule;	/*aadt("GrammarModule",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T3;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostStringChars;	/*aadt("PostStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostStringChars;	/*aadt("PostStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarDefinition;	/*aadt("GrammarDefinition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T0;	/*alist(aadt("CharRange",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationLiteral;	/*aadt("LocationLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LocationLiteral;	/*aadt("LocationLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Field;	/*aadt("Field",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Field;	/*aadt("Field",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tag;	/*aadt("Tag",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Tag;	/*aadt("Tag",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Type;	/*aadt("Type",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Type;	/*aadt("Type",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TimeZonePart;	/*aadt("TimeZonePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TimeZonePart;	/*aadt("TimeZonePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_HexIntegerLiteral;	/*aadt("HexIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_HexIntegerLiteral;	/*aadt("HexIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Declaration;	/*aadt("Declaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Declaration;	/*aadt("Declaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ShellCommand;	/*aadt("ShellCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ShellCommand;	/*aadt("ShellCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionBody;	/*aadt("FunctionBody",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionBody;	/*aadt("FunctionBody",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUTLIST;	/*aadt("LAYOUTLIST",[],layoutSyntax())*/
    public final io.usethesource.vallang.type.Type $T5;	/*\iter-star-seps(aadt("Range",[],contextFreeSyntax()),[aadt("LAYOUTLIST",[],layoutSyntax())])*/
    public final io.usethesource.vallang.type.Type ADT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Associativity;	/*aadt("Associativity",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PrePathChars;	/*aadt("PrePathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PrePathChars;	/*aadt("PrePathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Start;	/*aadt("Start",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Start;	/*aadt("Start",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringTail;	/*aadt("StringTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringTail;	/*aadt("StringTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Backslash;	/*aadt("Backslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Backslash;	/*aadt("Backslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Body;	/*aadt("Body",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Body;	/*aadt("Body",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Grammar;	/*aadt("Grammar",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type CharRange_empty_range_;	/*acons(aadt("CharRange",[],dataSyntax()),[],[],alabel="empty-range")*/
    public final io.usethesource.vallang.type.Type ADT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolPart;	/*aadt("ProtocolPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolPart;	/*aadt("ProtocolPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringTemplate;	/*aadt("StringTemplate",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringTemplate;	/*aadt("StringTemplate",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedBackslash;	/*aadt("NamedBackslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedBackslash;	/*aadt("NamedBackslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RascalKeywords;	/*aadt("RascalKeywords",[],keywordSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visit;	/*aadt("Visit",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visit;	/*aadt("Visit",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comment;	/*aadt("Comment",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comment;	/*aadt("Comment",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Command;	/*aadt("Command",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Command;	/*aadt("Command",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StructuredType;	/*aadt("StructuredType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StructuredType;	/*aadt("StructuredType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OctalIntegerLiteral;	/*aadt("OctalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OctalIntegerLiteral;	/*aadt("OctalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Commands;	/*aadt("Commands",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Commands;	/*aadt("Commands",[],contextFreeSyntax())*/

    public $Characters(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Characters(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Characters_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.lang.rascal.grammar.definition.$Characters.class, this);
       
       mstore.importModule(rascal.lang.rascal.syntax.$Rascal.class, rex, rascal.lang.rascal.syntax.$Rascal::new);
       mstore.importModule(rascal.$Exception.class, rex, rascal.$Exception::new);
       mstore.importModule(rascal.$Type.class, rex, rascal.$Type::new);
       mstore.importModule(rascal.$List.class, rex, rascal.$List::new);
       mstore.importModule(rascal.$Grammar.class, rex, rascal.$Grammar::new);
       mstore.importModule(rascal.$Message.class, rex, rascal.$Message::new);
       mstore.importModule(rascal.$String.class, rex, rascal.$String::new);
       mstore.importModule(rascal.$ParseTree.class, rex, rascal.$ParseTree::new); 
       
       M_lang_rascal_syntax_Rascal = mstore.getModule(rascal.lang.rascal.syntax.$Rascal.class);
       M_Exception = mstore.getModule(rascal.$Exception.class);
       M_Type = mstore.getModule(rascal.$Type.class);
       M_List = mstore.getModule(rascal.$List.class);
       M_Grammar = mstore.getModule(rascal.$Grammar.class);
       M_Message = mstore.getModule(rascal.$Message.class);
       M_String = mstore.getModule(rascal.$String.class);
       M_ParseTree = mstore.getModule(rascal.$ParseTree.class); 
       
                          
       
       $TS.importStore(M_lang_rascal_syntax_Rascal.$TS);
       $TS.importStore(M_Exception.$TS);
       $TS.importStore(M_Type.$TS);
       $TS.importStore(M_List.$TS);
       $TS.importStore(M_Grammar.$TS);
       $TS.importStore(M_Message.$TS);
       $TS.importStore(M_String.$TS);
       $TS.importStore(M_ParseTree.$TS);
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/lang/rascal/grammar/definition/$Characters.constants", 13, "f5505f32e90b93c4e92e9d805990a1de");
       NT_ProtocolChars = $lex("ProtocolChars");
       ADT_ProtocolChars = $adt("ProtocolChars");
       NT_Replacement = $sort("Replacement");
       ADT_Replacement = $adt("Replacement");
       NT_PostProtocolChars = $lex("PostProtocolChars");
       ADT_PostProtocolChars = $adt("PostProtocolChars");
       NT_Visibility = $sort("Visibility");
       ADT_Visibility = $adt("Visibility");
       ADT_Attr = $adt("Attr");
       NT_Expression = $sort("Expression");
       ADT_Expression = $adt("Expression");
       NT_Strategy = $sort("Strategy");
       ADT_Strategy = $adt("Strategy");
       ADT_Tree = $adt("Tree");
       NT_MidStringChars = $lex("MidStringChars");
       ADT_MidStringChars = $adt("MidStringChars");
       ADT_LocationChangeType = $adt("LocationChangeType");
       ADT_IOCapability = $adt("IOCapability");
       NT_Name = $lex("Name");
       ADT_Name = $adt("Name");
       NT_TagString = $lex("TagString");
       ADT_TagString = $adt("TagString");
       NT_Concrete = $lex("Concrete");
       ADT_Concrete = $adt("Concrete");
       NT_FunctionType = $sort("FunctionType");
       ADT_FunctionType = $adt("FunctionType");
       NT_Range = $sort("Range");
       ADT_Range = $adt("Range");
       NT_LAYOUT = $lex("LAYOUT");
       ADT_LAYOUT = $adt("LAYOUT");
       NT_Pattern = $sort("Pattern");
       ADT_Pattern = $adt("Pattern");
       NT_Variable = $sort("Variable");
       ADT_Variable = $adt("Variable");
       ADT_Item = $adt("Item");
       NT_RationalLiteral = $lex("RationalLiteral");
       ADT_RationalLiteral = $adt("RationalLiteral");
       ADT_GrammarModule = $adt("GrammarModule");
       NT_RegExpLiteral = $lex("RegExpLiteral");
       ADT_RegExpLiteral = $adt("RegExpLiteral");
       NT_Declarator = $sort("Declarator");
       ADT_Declarator = $adt("Declarator");
       NT_ModuleParameters = $sort("ModuleParameters");
       ADT_ModuleParameters = $adt("ModuleParameters");
       NT_Output = $lex("Output");
       ADT_Output = $adt("Output");
       NT_SyntaxDefinition = $sort("SyntaxDefinition");
       ADT_SyntaxDefinition = $adt("SyntaxDefinition");
       NT_DatePart = $lex("DatePart");
       ADT_DatePart = $adt("DatePart");
       NT_ImportedModule = $sort("ImportedModule");
       ADT_ImportedModule = $adt("ImportedModule");
       NT_BooleanLiteral = $lex("BooleanLiteral");
       ADT_BooleanLiteral = $adt("BooleanLiteral");
       NT_Case = $sort("Case");
       ADT_Case = $adt("Case");
       NT_LocalVariableDeclaration = $sort("LocalVariableDeclaration");
       ADT_LocalVariableDeclaration = $adt("LocalVariableDeclaration");
       NT_Target = $sort("Target");
       ADT_Target = $adt("Target");
       NT_IntegerLiteral = $sort("IntegerLiteral");
       ADT_IntegerLiteral = $adt("IntegerLiteral");
       NT_TimePartNoTZ = $lex("TimePartNoTZ");
       ADT_TimePartNoTZ = $adt("TimePartNoTZ");
       NT_Renaming = $sort("Renaming");
       ADT_Renaming = $adt("Renaming");
       NT_KeywordFormals = $sort("KeywordFormals");
       ADT_KeywordFormals = $adt("KeywordFormals");
       NT_Catch = $sort("Catch");
       ADT_Catch = $adt("Catch");
       ADT_Production = $adt("Production");
       NT_PostStringChars = $lex("PostStringChars");
       ADT_PostStringChars = $adt("PostStringChars");
       ADT_GrammarDefinition = $adt("GrammarDefinition");
       ADT_CharRange = $adt("CharRange");
       NT_DataTarget = $sort("DataTarget");
       ADT_DataTarget = $adt("DataTarget");
       NT_LocationLiteral = $sort("LocationLiteral");
       ADT_LocationLiteral = $adt("LocationLiteral");
       NT_Field = $sort("Field");
       ADT_Field = $adt("Field");
       NT_Tag = $sort("Tag");
       ADT_Tag = $adt("Tag");
       NT_Type = $sort("Type");
       ADT_Type = $adt("Type");
       ADT_Symbol = $adt("Symbol");
       NT_TimeZonePart = $lex("TimeZonePart");
       ADT_TimeZonePart = $adt("TimeZonePart");
       NT_HexIntegerLiteral = $lex("HexIntegerLiteral");
       ADT_HexIntegerLiteral = $adt("HexIntegerLiteral");
       NT_Declaration = $sort("Declaration");
       ADT_Declaration = $adt("Declaration");
       NT_ShellCommand = $sort("ShellCommand");
       ADT_ShellCommand = $adt("ShellCommand");
       NT_FunctionBody = $sort("FunctionBody");
       ADT_FunctionBody = $adt("FunctionBody");
       NT_StringLiteral = $sort("StringLiteral");
       ADT_StringLiteral = $adt("StringLiteral");
       NT_PreStringChars = $lex("PreStringChars");
       ADT_PreStringChars = $adt("PreStringChars");
       ADT_LAYOUTLIST = $layouts("LAYOUTLIST");
       NT_Tags = $sort("Tags");
       ADT_Tags = $adt("Tags");
       NT_TypeArg = $sort("TypeArg");
       ADT_TypeArg = $adt("TypeArg");
       NT_NonterminalLabel = $lex("NonterminalLabel");
       ADT_NonterminalLabel = $adt("NonterminalLabel");
       NT_RegExp = $lex("RegExp");
       ADT_RegExp = $adt("RegExp");
       NT_Bound = $sort("Bound");
       ADT_Bound = $adt("Bound");
       NT_Nonterminal = $lex("Nonterminal");
       ADT_Nonterminal = $adt("Nonterminal");
       ADT_Associativity = $adt("Associativity");
       NT_Signature = $sort("Signature");
       ADT_Signature = $adt("Signature");
       NT_Renamings = $sort("Renamings");
       ADT_Renamings = $adt("Renamings");
       NT_KeywordFormal = $sort("KeywordFormal");
       ADT_KeywordFormal = $adt("KeywordFormal");
       NT_OptionalExpression = $sort("OptionalExpression");
       ADT_OptionalExpression = $adt("OptionalExpression");
       NT_URLChars = $lex("URLChars");
       ADT_URLChars = $adt("URLChars");
       NT_ModuleActuals = $sort("ModuleActuals");
       ADT_ModuleActuals = $adt("ModuleActuals");
       NT_Class = $sort("Class");
       ADT_Class = $adt("Class");
       ADT_Condition = $adt("Condition");
       NT_PathPart = $sort("PathPart");
       ADT_PathPart = $adt("PathPart");
       NT_PrePathChars = $lex("PrePathChars");
       ADT_PrePathChars = $adt("PrePathChars");
       NT_Start = $sort("Start");
       ADT_Start = $adt("Start");
       NT_StringConstant = $lex("StringConstant");
       ADT_StringConstant = $adt("StringConstant");
       NT_OptionalComma = $lex("OptionalComma");
       ADT_OptionalComma = $adt("OptionalComma");
       NT_FunctionDeclaration = $sort("FunctionDeclaration");
       ADT_FunctionDeclaration = $adt("FunctionDeclaration");
       NT_StringTail = $sort("StringTail");
       ADT_StringTail = $adt("StringTail");
       NT_Backslash = $lex("Backslash");
       ADT_Backslash = $adt("Backslash");
       NT_CaseInsensitiveStringConstant = $lex("CaseInsensitiveStringConstant");
       ADT_CaseInsensitiveStringConstant = $adt("CaseInsensitiveStringConstant");
       NT_Char = $lex("Char");
       ADT_Char = $adt("Char");
       NT_Body = $sort("Body");
       ADT_Body = $adt("Body");
       NT_PatternWithAction = $sort("PatternWithAction");
       ADT_PatternWithAction = $adt("PatternWithAction");
       ADT_Exception = $adt("Exception");
       NT_PostPathChars = $lex("PostPathChars");
       ADT_PostPathChars = $adt("PostPathChars");
       NT_Module = $sort("Module");
       ADT_Module = $adt("Module");
       NT_StringCharacter = $lex("StringCharacter");
       ADT_StringCharacter = $adt("StringCharacter");
       NT_JustDate = $lex("JustDate");
       ADT_JustDate = $adt("JustDate");
       NT_TypeVar = $sort("TypeVar");
       ADT_TypeVar = $adt("TypeVar");
       NT_Header = $sort("Header");
       ADT_Header = $adt("Header");
       ADT_LocationType = $adt("LocationType");
       NT_Assignment = $sort("Assignment");
       ADT_Assignment = $adt("Assignment");
       ADT_Grammar = $adt("Grammar");
       NT_Variant = $sort("Variant");
       ADT_Variant = $adt("Variant");
       NT_UserType = $sort("UserType");
       ADT_UserType = $adt("UserType");
       NT_Import = $sort("Import");
       ADT_Import = $adt("Import");
       NT_DataTypeSelector = $sort("DataTypeSelector");
       ADT_DataTypeSelector = $adt("DataTypeSelector");
       NT_ConcreteHole = $sort("ConcreteHole");
       ADT_ConcreteHole = $adt("ConcreteHole");
       ADT_Message = $adt("Message");
       NT_Sym = $sort("Sym");
       ADT_Sym = $adt("Sym");
       NT_RealLiteral = $lex("RealLiteral");
       ADT_RealLiteral = $adt("RealLiteral");
       NT_StringMiddle = $sort("StringMiddle");
       ADT_StringMiddle = $adt("StringMiddle");
       NT_DateAndTime = $lex("DateAndTime");
       ADT_DateAndTime = $adt("DateAndTime");
       NT_FunctionModifiers = $sort("FunctionModifiers");
       ADT_FunctionModifiers = $adt("FunctionModifiers");
       NT_Comprehension = $sort("Comprehension");
       ADT_Comprehension = $adt("Comprehension");
       NT_QualifiedName = $sort("QualifiedName");
       ADT_QualifiedName = $adt("QualifiedName");
       NT_Formals = $sort("Formals");
       ADT_Formals = $adt("Formals");
       NT_MidPathChars = $lex("MidPathChars");
       ADT_MidPathChars = $adt("MidPathChars");
       NT_MidProtocolChars = $lex("MidProtocolChars");
       ADT_MidProtocolChars = $adt("MidProtocolChars");
       NT_PathTail = $sort("PathTail");
       ADT_PathTail = $adt("PathTail");
       NT_Parameters = $sort("Parameters");
       ADT_Parameters = $adt("Parameters");
       NT_PreProtocolChars = $lex("PreProtocolChars");
       ADT_PreProtocolChars = $adt("PreProtocolChars");
       NT_ProtocolPart = $sort("ProtocolPart");
       ADT_ProtocolPart = $adt("ProtocolPart");
       NT_StringTemplate = $sort("StringTemplate");
       ADT_StringTemplate = $adt("StringTemplate");
       NT_NamedBackslash = $lex("NamedBackslash");
       ADT_NamedBackslash = $adt("NamedBackslash");
       ADT_RascalKeywords = $keywords("RascalKeywords");
       NT_Label = $sort("Label");
       ADT_Label = $adt("Label");
       NT_Kind = $sort("Kind");
       ADT_Kind = $adt("Kind");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       NT_RegExpModifier = $lex("RegExpModifier");
       ADT_RegExpModifier = $adt("RegExpModifier");
       NT_Prod = $sort("Prod");
       ADT_Prod = $adt("Prod");
       ADT_RuntimeException = $adt("RuntimeException");
       NT_NamedRegExp = $lex("NamedRegExp");
       ADT_NamedRegExp = $adt("NamedRegExp");
       NT_DecimalIntegerLiteral = $lex("DecimalIntegerLiteral");
       ADT_DecimalIntegerLiteral = $adt("DecimalIntegerLiteral");
       NT_Assoc = $sort("Assoc");
       ADT_Assoc = $adt("Assoc");
       NT_DateTimeLiteral = $sort("DateTimeLiteral");
       ADT_DateTimeLiteral = $adt("DateTimeLiteral");
       NT_FunctionModifier = $sort("FunctionModifier");
       ADT_FunctionModifier = $adt("FunctionModifier");
       NT_EvalCommand = $sort("EvalCommand");
       ADT_EvalCommand = $adt("EvalCommand");
       NT_ProdModifier = $sort("ProdModifier");
       ADT_ProdModifier = $adt("ProdModifier");
       NT_JustTime = $lex("JustTime");
       ADT_JustTime = $adt("JustTime");
       NT_UnicodeEscape = $lex("UnicodeEscape");
       ADT_UnicodeEscape = $adt("UnicodeEscape");
       NT_Literal = $sort("Literal");
       ADT_Literal = $adt("Literal");
       NT_Statement = $sort("Statement");
       ADT_Statement = $adt("Statement");
       NT_Toplevel = $sort("Toplevel");
       ADT_Toplevel = $adt("Toplevel");
       NT_ConcretePart = $lex("ConcretePart");
       ADT_ConcretePart = $adt("ConcretePart");
       NT_PathChars = $lex("PathChars");
       ADT_PathChars = $adt("PathChars");
       NT_Visit = $sort("Visit");
       ADT_Visit = $adt("Visit");
       NT_Comment = $lex("Comment");
       ADT_Comment = $adt("Comment");
       NT_Command = $sort("Command");
       ADT_Command = $adt("Command");
       NT_StructuredType = $sort("StructuredType");
       ADT_StructuredType = $adt("StructuredType");
       NT_OctalIntegerLiteral = $lex("OctalIntegerLiteral");
       ADT_OctalIntegerLiteral = $adt("OctalIntegerLiteral");
       NT_CommonKeywordParameters = $sort("CommonKeywordParameters");
       ADT_CommonKeywordParameters = $adt("CommonKeywordParameters");
       NT_Assignable = $sort("Assignable");
       ADT_Assignable = $adt("Assignable");
       NT_ProtocolTail = $sort("ProtocolTail");
       ADT_ProtocolTail = $adt("ProtocolTail");
       NT_BasicType = $sort("BasicType");
       ADT_BasicType = $adt("BasicType");
       NT_Commands = $sort("Commands");
       ADT_Commands = $adt("Commands");
       $T4 = $TF.valueType();
       $T1 = $TF.integerType();
       NT_Mapping_Expression = $parameterizedSort("Mapping", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       $T2 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_1 = $parameterizedSort("KeywordArguments", new Type[] { $T2 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       NT_Mapping_Pattern = $parameterizedSort("Mapping", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       $T3 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_Expression = $parameterizedSort("KeywordArguments", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       NT_Mapping_1 = $parameterizedSort("Mapping", new Type[] { $T2 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       $T0 = $TF.listType(ADT_CharRange);
       NT_KeywordArgument_1 = $parameterizedSort("KeywordArgument", new Type[] { $T2 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       NT_KeywordArguments_Pattern = $parameterizedSort("KeywordArguments", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       $T5 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_IterStarSeps, $RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Range")), $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")))));
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T2 });
       ADT_Mapping_Expression = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Expression });
       ADT_KeywordArguments_1 = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { $T2 });
       ADT_Mapping_Pattern = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Pattern });
       ADT_KeywordArguments_Expression = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Expression });
       ADT_Mapping_1 = $TF.abstractDataType($TS, "Mapping", new Type[] { $T2 });
       ADT_KeywordArgument_1 = $TF.abstractDataType($TS, "KeywordArgument", new Type[] { $T2 });
       ADT_KeywordArguments_Pattern = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Pattern });
       CharRange_empty_range_ = $TF.constructor($TS, ADT_CharRange, "empty-range");
    
       
       
    }
    public IBool isTypeVar(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isTypeVar($P0);
    }
    public IList addLabels(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_Type.addLabels($P0, $P1);
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
    public IBool isStrType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isStrType($P0);
    }
    public IConstructor choice(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_Type.choice($P0, $P1);
    }
    public IBool isValueType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isValueType($P0);
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
    public IValue complement(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IValue)lang_rascal_grammar_definition_Characters_complement$b568586ea6930aa8((IConstructor) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IValue)lang_rascal_grammar_definition_Characters_complement$9bb082bbab5b8dd8((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IValue)lang_rascal_grammar_definition_Characters_complement$e4978c5ddcaf2671((IConstructor) $P0);
         if($result != null) return $result;
       }
       if($isNonTerminal($P0Type, M_lang_rascal_syntax_Rascal.NT_Class)){
         return $RVF.constructor(M_lang_rascal_syntax_Rascal.Class_complement_Class, new IValue[]{(ITree) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isNodeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNodeType($P0);
    }
    public IConstructor cc2ranges(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isNonTerminal($P0Type, M_lang_rascal_syntax_Rascal.NT_Class)){
         $result = (IConstructor)lang_rascal_grammar_definition_Characters_cc2ranges$54a54e4321a65bcb((ITree) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isReifiedType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isReifiedType($P0);
    }
    public IBool isRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRelType($P0);
    }
    public IValue intersection(IValue $P0, IValue $P1){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
         $result = (IValue)lang_rascal_grammar_definition_Characters_intersection$b97501af85a6ce29((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IValue)lang_rascal_grammar_definition_Characters_intersection$2244f3914dc6dda2((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
         $result = (IValue)lang_rascal_grammar_definition_Characters_intersection$1412bfd05092c19b((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isNonTerminal($P0Type, M_lang_rascal_syntax_Rascal.NT_Class) && $isNonTerminal($P1Type, M_lang_rascal_syntax_Rascal.NT_Class)){
         return $RVF.constructor(M_lang_rascal_syntax_Rascal.Class_intersection_Class_Class, new IValue[]{(ITree) $P0, (ITree) $P1});
       }
       if($isNonTerminal($P0Type, M_lang_rascal_syntax_Rascal.NT_Expression) && $isNonTerminal($P1Type, M_lang_rascal_syntax_Rascal.NT_Expression)){
         return $RVF.constructor(M_lang_rascal_syntax_Rascal.Expression_intersection_Expression_Expression, new IValue[]{(ITree) $P0, (ITree) $P1});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
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
    public IConstructor new_char_class(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IConstructor)lang_rascal_grammar_definition_Characters_new_char_class$5e7c03a98ce83659((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue union(IValue $P0, IValue $P1){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
         $result = (IValue)lang_rascal_grammar_definition_Characters_union$19bdbbc222bf3a4b((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IValue)lang_rascal_grammar_definition_Characters_union$bf68e50d1ee7fd6b((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
         $result = (IValue)lang_rascal_grammar_definition_Characters_union$5d96096e72e9da4a((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isNonTerminal($P0Type, M_lang_rascal_syntax_Rascal.NT_Class) && $isNonTerminal($P1Type, M_lang_rascal_syntax_Rascal.NT_Class)){
         return $RVF.constructor(M_lang_rascal_syntax_Rascal.Class_union_Class_Class, new IValue[]{(ITree) $P0, (ITree) $P1});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isLocType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isLocType($P0);
    }
    public IConstructor new_range(IValue $P0, IValue $P1){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T1) && $isSubtypeOf($P1Type,$T1)){
         $result = (IConstructor)lang_rascal_grammar_definition_Characters_new_range$10d5c4049fe689a1((IInteger) $P0, (IInteger) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
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
    public IInteger toInt(IValue $P0){ // Generated by Resolver
       return (IInteger) M_String.toInt($P0);
    }
    public IInteger toInt(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IInteger) M_String.toInt($P0, $P1);
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
       return (IConstructor) M_ParseTree.associativity($P0, $P1, $P2);
    }
    public IBool lessThan(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_CharRange) && $isSubtypeOf($P1Type, M_ParseTree.ADT_CharRange)){
         $result = (IBool)lang_rascal_grammar_definition_Characters_lessThan$3bd4f51b080cac40((IConstructor) $P0, (IConstructor) $P1);
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
    public IValue difference(IValue $P0, IValue $P1){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
         $result = (IValue)lang_rascal_grammar_definition_Characters_difference$b274dba4e7c84de6((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_CharRange) && $isSubtypeOf($P1Type, M_ParseTree.ADT_CharRange)){
         $result = (IValue)lang_rascal_grammar_definition_Characters_difference$d1d431e6985d0b49((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T0)){
         $result = (IValue)lang_rascal_grammar_definition_Characters_difference$73ce40b5ac114748((IList) $P0, (IList) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
         $result = (IValue)lang_rascal_grammar_definition_Characters_difference$c32239e262a94923((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       if($isNonTerminal($P0Type, M_lang_rascal_syntax_Rascal.NT_Class) && $isNonTerminal($P1Type, M_lang_rascal_syntax_Rascal.NT_Class)){
         return $RVF.constructor(M_lang_rascal_syntax_Rascal.Class_difference_Class_Class, new IValue[]{(ITree) $P0, (ITree) $P1});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IConstructor range(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isNonTerminal($P0Type, M_lang_rascal_syntax_Rascal.NT_Range)){
         $result = (IConstructor)lang_rascal_grammar_definition_Characters_range$1a0b128882305baa((ITree) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IInteger charToInt(IValue $P0){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       if($isNonTerminal($P0Type, ((IConstructor)$constants.get(0)/*lex("Char")*/))){
         $result = (IInteger)lang_rascal_grammar_definition_Characters_charToInt$e48d9965143ec1dd((ITree) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor intersect(IValue $P0, IValue $P1){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_CharRange) && $isSubtypeOf($P1Type, M_ParseTree.ADT_CharRange)){
         $result = (IConstructor)lang_rascal_grammar_definition_Characters_intersect$2c8ff0e2841a0a40((IConstructor) $P0, (IConstructor) $P1);
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

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(722,87,<23,0>,<23,87>) 
    public IConstructor lang_rascal_grammar_definition_Characters_new_range$10d5c4049fe689a1(IInteger from_0, IInteger to_1){ 
        
        
        if((((IBool)($aint_lessequal_aint(((IInteger)from_0),((IInteger)to_1))))).getValue()){
           return ((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)from_0), ((IInteger)to_1)})));
        
        } else {
           return ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{})));
        
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(812,108,<25,0>,<26,53>) 
    public IConstructor lang_rascal_grammar_definition_Characters_new_char_class$5e7c03a98ce83659(IList ranges_0){ 
        
        
        IList $reducer2 = (IList)(((IList)$constants.get(1)/*[]*/));
        $REDUCER1_GEN906:
        for(IValue $elem3_for : ((IList)ranges_0)){
            IConstructor $elem3 = (IConstructor) $elem3_for;
            IConstructor r_2 = ((IConstructor)($elem3));
            $reducer2 = ((IList)($me.union(((IList)($reducer2)), ((IList)($RVF.list(((IConstructor)r_2)))))));
        
        }
        
                    return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)($reducer2))})));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(1373,124,<33,0>,<34,64>) 
    public IConstructor lang_rascal_grammar_definition_Characters_complement$b568586ea6930aa8(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
           IValue $arg0_7 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_7.getType(), $T0)){
              if(true){
                 IList r1_0 = ((IList)($arg0_7));
                 final IListWriter $listwriter4 = (IListWriter)$RVF.listWriter();
                 $LCOMP5_GEN1453:
                 for(IValue $elem6_for : ((IList)($me.complement(((IList)($arg0_7)))))){
                     IConstructor $elem6 = (IConstructor) $elem6_for;
                     IConstructor r_1 = null;
                     if($is(((IConstructor)($elem6)),((IString)$constants.get(2)/*"empty-range"*/))){
                       continue $LCOMP5_GEN1453;
                     } else {
                       $listwriter4.append($elem6);
                     
                     }
                 
                 }
                 
                             return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)($listwriter4.done()))})));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(1501,113,<36,0>,<38,1>) 
    public IConstructor lang_rascal_grammar_definition_Characters_complement$e4978c5ddcaf2671(IConstructor s_0){ 
        
        
        final Template $template8 = (Template)new Template($RVF, "unsupported symbol for character class complement: ");
        $template8.beginIndent("                                                   ");
        $template8.addVal(s_0);
        $template8.endIndent("                                                   ");
        throw new Throw($template8.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(1618,161,<40,0>,<41,68>) 
    public IConstructor lang_rascal_grammar_definition_Characters_difference$b274dba4e7c84de6(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
           IValue $arg0_13 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_13.getType(), $T0)){
              if(true){
                 IList r1_0 = ((IList)($arg0_13));
                 if($has_type_and_arity($1, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                    IValue $arg0_12 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                    if($isComparable($arg0_12.getType(), $T0)){
                       if(true){
                          IList r2_1 = ((IList)($arg0_12));
                          final IListWriter $listwriter9 = (IListWriter)$RVF.listWriter();
                          $LCOMP10_GEN1732:
                          for(IValue $elem11_for : ((IList)($me.difference(((IList)($arg0_13)), ((IList)($arg0_12)))))){
                              IConstructor $elem11 = (IConstructor) $elem11_for;
                              IConstructor r_2 = null;
                              if($is(((IConstructor)($elem11)),((IString)$constants.get(2)/*"empty-range"*/))){
                                continue $LCOMP10_GEN1732;
                              } else {
                                $listwriter9.append($elem11);
                              
                              }
                          
                          }
                          
                                      return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)($listwriter9.done()))})));
                       
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(1781,133,<43,0>,<45,1>) 
    public IConstructor lang_rascal_grammar_definition_Characters_difference$c32239e262a94923(IConstructor s_0, IConstructor t_1){ 
        
        
        final Template $template14 = (Template)new Template($RVF, "unsupported symbols for  character class difference: ");
        $template14.beginIndent("                                                     ");
        $template14.addVal(s_0);
        $template14.endIndent("                                                     ");
        $template14.addStr(" and ");
        $template14.beginIndent("     ");
        $template14.addVal(t_1);
        $template14.endIndent("     ");
        throw new Throw($template14.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(1916,149,<47,0>,<48,63>) 
    public IConstructor lang_rascal_grammar_definition_Characters_union$19bdbbc222bf3a4b(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
           IValue $arg0_19 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_19.getType(), $T0)){
              if(true){
                 IList r1_0 = ((IList)($arg0_19));
                 if($has_type_and_arity($1, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                    IValue $arg0_18 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                    if($isComparable($arg0_18.getType(), $T0)){
                       if(true){
                          IList r2_1 = ((IList)($arg0_18));
                          final IListWriter $listwriter15 = (IListWriter)$RVF.listWriter();
                          $LCOMP16_GEN2023:
                          for(IValue $elem17_for : ((IList)($me.union(((IList)($arg0_19)), ((IList)($arg0_18)))))){
                              IConstructor $elem17 = (IConstructor) $elem17_for;
                              IConstructor r_2 = null;
                              if($is(((IConstructor)($elem17)),((IString)$constants.get(2)/*"empty-range"*/))){
                                continue $LCOMP16_GEN2023;
                              } else {
                                $listwriter15.append($elem17);
                              
                              }
                          
                          }
                          
                                      return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)($listwriter15.done()))})));
                       
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(2068,106,<50,0>,<52,1>) 
    public IConstructor lang_rascal_grammar_definition_Characters_union$5d96096e72e9da4a(IConstructor s_0, IConstructor t_1){ 
        
        
        final Template $template20 = (Template)new Template($RVF, "unsupported symbols for union: ");
        $template20.beginIndent("                               ");
        $template20.addVal(s_0);
        $template20.endIndent("                               ");
        $template20.addStr(" and ");
        $template20.beginIndent("     ");
        $template20.addVal(t_1);
        $template20.endIndent("     ");
        throw new Throw($template20.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(2176,164,<54,0>,<55,70>) 
    public IConstructor lang_rascal_grammar_definition_Characters_intersection$b97501af85a6ce29(IConstructor $0, IConstructor $1){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
           IValue $arg0_25 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_25.getType(), $T0)){
              if(true){
                 IList r1_0 = ((IList)($arg0_25));
                 if($has_type_and_arity($1, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                    IValue $arg0_24 = (IValue)($aadt_subscript_int(((IConstructor)$1),0));
                    if($isComparable($arg0_24.getType(), $T0)){
                       if(true){
                          IList r2_1 = ((IList)($arg0_24));
                          final IListWriter $listwriter21 = (IListWriter)$RVF.listWriter();
                          $LCOMP22_GEN2291:
                          for(IValue $elem23_for : ((IList)($me.intersection(((IList)($arg0_25)), ((IList)($arg0_24)))))){
                              IConstructor $elem23 = (IConstructor) $elem23_for;
                              IConstructor r_2 = null;
                              if($is(((IConstructor)($elem23)),((IString)$constants.get(2)/*"empty-range"*/))){
                                continue $LCOMP22_GEN2291;
                              } else {
                                $listwriter21.append($elem23);
                              
                              }
                          
                          }
                          
                                      return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)($listwriter21.done()))})));
                       
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(2342,120,<57,0>,<59,1>) 
    public IConstructor lang_rascal_grammar_definition_Characters_intersection$1412bfd05092c19b(IConstructor s_0, IConstructor t_1){ 
        
        
        final Template $template26 = (Template)new Template($RVF, "unsupported symbols for intersection: ");
        $template26.beginIndent("                                      ");
        $template26.addVal(s_0);
        $template26.endIndent("                                      ");
        $template26.addStr(" and ");
        $template26.beginIndent("     ");
        $template26.addVal(t_1);
        $template26.endIndent("     ");
        throw new Throw($template26.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(2464,165,<61,0>,<66,1>) 
    public IBool lang_rascal_grammar_definition_Characters_lessThan$3bd4f51b080cac40(IConstructor r1_0, IConstructor r2_1){ 
        
        
        if($has_type_and_arity(r1_0, M_ParseTree.CharRange_range_int_int, 2)){
           IValue $arg0_30 = (IValue)($aadt_subscript_int(((IConstructor)r1_0),0));
           if($isComparable($arg0_30.getType(), $T4)){
              IValue $arg1_29 = (IValue)($aadt_subscript_int(((IConstructor)r1_0),1));
              if($isComparable($arg1_29.getType(), $T1)){
                 IInteger e1_2 = null;
                 if($has_type_and_arity(r2_1, M_ParseTree.CharRange_range_int_int, 2)){
                    IValue $arg0_28 = (IValue)($aadt_subscript_int(((IConstructor)r2_1),0));
                    if($isComparable($arg0_28.getType(), $T1)){
                       IInteger s2_3 = null;
                       IValue $arg1_27 = (IValue)($aadt_subscript_int(((IConstructor)r2_1),1));
                       if($isComparable($arg1_27.getType(), $T4)){
                          return ((IBool)($aint_less_aint(((IInteger)($arg1_29)),((IInteger)($arg0_28)))));
                       
                       }
                    
                    }
                 
                 }
              
              }
           
           }
        
        }
        final Template $template31 = (Template)new Template($RVF, "unexpected ranges ");
        $template31.beginIndent("                  ");
        $template31.addVal(r1_0);
        $template31.endIndent("                  ");
        $template31.addStr(" and ");
        $template31.beginIndent("     ");
        $template31.addVal(r2_1);
        $template31.endIndent("     ");
        throw new Throw($template31.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(2631,1064,<68,0>,<110,1>) 
    public IConstructor lang_rascal_grammar_definition_Characters_difference$d1d431e6985d0b49(IConstructor l_0, IConstructor r_1){ 
        
        
        if((((IBool)($equal(((IConstructor)l_0), ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{}))))))).getValue()){
           return ((IConstructor)l_0);
        
        } else {
           if((((IBool)($equal(((IConstructor)r_1), ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{}))))))).getValue()){
              return ((IConstructor)l_0);
           
           }
        
        }if($has_type_and_arity(l_0, M_ParseTree.CharRange_range_int_int, 2)){
           IValue $arg0_35 = (IValue)($aadt_subscript_int(((IConstructor)l_0),0));
           if($isComparable($arg0_35.getType(), $T1)){
              IInteger ls_2 = ((IInteger)($arg0_35));
              IValue $arg1_34 = (IValue)($aadt_subscript_int(((IConstructor)l_0),1));
              if($isComparable($arg1_34.getType(), $T1)){
                 IInteger le_3 = ((IInteger)($arg1_34));
                 if($has_type_and_arity(r_1, M_ParseTree.CharRange_range_int_int, 2)){
                    IValue $arg0_33 = (IValue)($aadt_subscript_int(((IConstructor)r_1),0));
                    if($isComparable($arg0_33.getType(), $T1)){
                       IInteger rs_4 = ((IInteger)($arg0_33));
                       IValue $arg1_32 = (IValue)($aadt_subscript_int(((IConstructor)r_1),1));
                       if($isComparable($arg1_32.getType(), $T1)){
                          IInteger re_5 = ((IInteger)($arg1_32));
                          if((((IBool)($aint_lessequal_aint(((IInteger)($arg0_35)),((IInteger)($arg1_32))).not()))).getValue()){
                             return ((IConstructor)l_0);
                          
                          }
                          if((((IBool)($aint_less_aint(((IInteger)($arg1_34)),((IInteger)($arg0_33)))))).getValue()){
                             return ((IConstructor)l_0);
                          
                          }
                          if((((IBool)($aint_less_aint(((IInteger)($arg0_35)),((IInteger)($arg0_33))).not()))).getValue()){
                             if((((IBool)($aint_lessequal_aint(((IInteger)($arg1_34)),((IInteger)($arg1_32)))))).getValue()){
                                return ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{})));
                             
                             }
                          
                          }
                          if((((IBool)($aint_less_aint(((IInteger)($arg0_33)),((IInteger)($arg0_35))).not()))).getValue()){
                             if((((IBool)($aint_lessequal_aint(((IInteger)($arg1_32)),((IInteger)($arg1_34)))))).getValue()){
                                return ((IConstructor)($me.new_range(((IInteger)($arg0_35)), ((IInteger)(((IInteger) ((IInteger)($arg0_33)).subtract(((IInteger)$constants.get(3)/*1*/))))))));
                             
                             }
                          
                          }
                          if((((IBool)($aint_less_aint(((IInteger)($arg1_34)),((IInteger)($arg1_32)))))).getValue()){
                             return ((IConstructor)($me.new_range(((IInteger)($arg0_35)), ((IInteger)(((IInteger) ((IInteger)($arg0_33)).subtract(((IInteger)$constants.get(3)/*1*/))))))));
                          
                          }
                          if((((IBool)($aint_lessequal_aint(((IInteger)($arg0_35)),((IInteger)($arg0_33))).not()))).getValue()){
                             return ((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)($aint_add_aint(((IInteger)($arg1_32)),((IInteger)$constants.get(3)/*1*/)))), ((IInteger)($arg1_34))})));
                          
                          }
                       
                       }
                    
                    }
                 
                 }
              
              }
           
           }
        
        }
        final Template $template36 = (Template)new Template($RVF, "did not expect to end up here! ");
        $template36.beginIndent("                               ");
        $template36.addVal(l_0);
        $template36.endIndent("                               ");
        $template36.addStr(" - ");
        $template36.beginIndent("   ");
        $template36.addVal(r_1);
        $template36.endIndent("   ");
        throw new Throw($template36.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(3697,1089,<112,0>,<154,1>) 
    public IConstructor lang_rascal_grammar_definition_Characters_intersect$2c8ff0e2841a0a40(IConstructor r1_0, IConstructor r2_1){ 
        
        
        if((((IBool)($equal(((IConstructor)r1_0), ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{}))))))).getValue()){
           return ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{})));
        
        } else {
           if((((IBool)($equal(((IConstructor)r2_1), ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{}))))))).getValue()){
              return ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{})));
           
           }
        
        }if($has_type_and_arity(r1_0, M_ParseTree.CharRange_range_int_int, 2)){
           IValue $arg0_40 = (IValue)($aadt_subscript_int(((IConstructor)r1_0),0));
           if($isComparable($arg0_40.getType(), $T1)){
              IInteger s1_2 = null;
              IValue $arg1_39 = (IValue)($aadt_subscript_int(((IConstructor)r1_0),1));
              if($isComparable($arg1_39.getType(), $T1)){
                 IInteger e1_3 = null;
                 if($has_type_and_arity(r2_1, M_ParseTree.CharRange_range_int_int, 2)){
                    IValue $arg0_38 = (IValue)($aadt_subscript_int(((IConstructor)r2_1),0));
                    if($isComparable($arg0_38.getType(), $T1)){
                       IInteger s2_4 = null;
                       IValue $arg1_37 = (IValue)($aadt_subscript_int(((IConstructor)r2_1),1));
                       if($isComparable($arg1_37.getType(), $T1)){
                          IInteger e2_5 = null;
                          if((((IBool)($aint_lessequal_aint(((IInteger)($arg0_40)),((IInteger)($arg1_37))).not()))).getValue()){
                             return ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{})));
                          
                          }
                          if((((IBool)($aint_less_aint(((IInteger)($arg1_39)),((IInteger)($arg0_38)))))).getValue()){
                             return ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{})));
                          
                          }
                          if((((IBool)($aint_less_aint(((IInteger)($arg0_40)),((IInteger)($arg0_38))).not()))).getValue()){
                             if((((IBool)($aint_lessequal_aint(((IInteger)($arg1_39)),((IInteger)($arg1_37)))))).getValue()){
                                return ((IConstructor)r1_0);
                             
                             }
                          
                          }
                          if((((IBool)($aint_less_aint(((IInteger)($arg0_38)),((IInteger)($arg0_40))).not()))).getValue()){
                             if((((IBool)($aint_lessequal_aint(((IInteger)($arg1_37)),((IInteger)($arg1_39)))))).getValue()){
                                return ((IConstructor)r2_1);
                             
                             }
                          
                          }
                          if((((IBool)($aint_less_aint(((IInteger)($arg1_39)),((IInteger)($arg1_37)))))).getValue()){
                             return ((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)($arg0_38)), ((IInteger)($arg1_39))})));
                          
                          }
                          if((((IBool)($aint_lessequal_aint(((IInteger)($arg0_40)),((IInteger)($arg0_38))).not()))).getValue()){
                             return ((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)($arg0_40)), ((IInteger)($arg1_37))})));
                          
                          }
                       
                       }
                    
                    }
                 
                 }
              
              }
           
           }
        
        }
        final Template $template41 = (Template)new Template($RVF, "unexpected ranges ");
        $template41.beginIndent("                  ");
        $template41.addVal(r1_0);
        $template41.endIndent("                  ");
        $template41.addStr(" and ");
        $template41.beginIndent("     ");
        $template41.addVal(r2_1);
        $template41.endIndent("     ");
        throw new Throw($template41.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(4788,131,<156,0>,<158,1>) 
    public IList lang_rascal_grammar_definition_Characters_complement$9bb082bbab5b8dd8(IList s_0){ 
        
        
        return ((IList)($me.difference(((IList)$constants.get(4)/*[range(1,1114111)]*/), ((IList)s_0))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(4921,1485,<160,0>,<211,1>) 
    public IList lang_rascal_grammar_definition_Characters_intersection$2244f3914dc6dda2(IList l_0, IList r_1){ 
        
        
        if((((IBool)($equal(((IList)l_0), ((IList)r_1))))).getValue()){
           return ((IList)l_0);
        
        }
        if((((IBool)($equal(((IList)l_0), ((IList)$constants.get(1)/*[]*/))))).getValue()){
           return ((IList)$constants.get(1)/*[]*/);
        
        } else {
           if((((IBool)($equal(((IList)r_1), ((IList)$constants.get(1)/*[]*/))))).getValue()){
              return ((IList)$constants.get(1)/*[]*/);
           
           }
        
        }ITuple $TMP42 = (ITuple)($RVF.tuple(((IConstructor)(M_List.head(((IList)l_0)))), ((IList)(M_List.tail(((IList)l_0))))));
        IConstructor lhead_2 = ((IConstructor)($atuple_subscript_int(((ITuple)($TMP42)),0)));
        IList ltail_3 = ((IList)($atuple_subscript_int(((ITuple)($TMP42)),1)));
        ITuple $TMP43 = (ITuple)($RVF.tuple(((IConstructor)(M_List.head(((IList)r_1)))), ((IList)(M_List.tail(((IList)r_1))))));
        IConstructor rhead_4 = ((IConstructor)($atuple_subscript_int(((ITuple)($TMP43)),0)));
        IList rtail_5 = ((IList)($atuple_subscript_int(((ITuple)($TMP43)),1)));
        if((((IBool)($equal(((IConstructor)lhead_2), ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{}))))))).getValue()){
           return ((IList)($me.intersection(((IList)ltail_3), ((IList)r_1))));
        
        }
        if((((IBool)($equal(((IConstructor)rhead_4), ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{}))))))).getValue()){
           return ((IList)($me.intersection(((IList)l_0), ((IList)rtail_5))));
        
        }
        if((((IBool)($aint_lessequal_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end")))))).not()))).getValue()){
           return ((IList)($me.intersection(((IList)l_0), ((IList)rtail_5))));
        
        }
        if((((IBool)($aint_less_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin"))))))))).getValue()){
           return ((IList)($me.intersection(((IList)ltail_3), ((IList)r_1))));
        
        }
        if((((IBool)($aint_less_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin")))))).not()))).getValue()){
           if((((IBool)($aint_lessequal_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end"))))))))).getValue()){
              return ((IList)($elm_add_alist(((IConstructor)lhead_2),((IList)($me.intersection(((IList)ltail_3), ((IList)r_1)))))));
           
           }
        
        }
        if((((IBool)($aint_less_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin")))))).not()))).getValue()){
           if((((IBool)($aint_lessequal_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end"))))))))).getValue()){
              return ((IList)($elm_add_alist(((IConstructor)rhead_4),((IList)($me.intersection(((IList)l_0), ((IList)rtail_5)))))));
           
           }
        
        }
        if((((IBool)($aint_less_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end"))))))))).getValue()){
           return ((IList)($elm_add_alist(((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin"))))), ((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end")))))}))),((IList)($me.intersection(((IList)ltail_3), ((IList)r_1)))))));
        
        }
        if((((IBool)($aint_lessequal_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin")))))).not()))).getValue()){
           return ((IList)($elm_add_alist(((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin"))))), ((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end")))))}))),((IList)($me.intersection(((IList)l_0), ((IList)rtail_5)))))));
        
        }
        final Template $template44 = (Template)new Template($RVF, "did not expect to end up here! ");
        $template44.beginIndent("                               ");
        $template44.addVal(l_0);
        $template44.endIndent("                               ");
        $template44.addStr(" - ");
        $template44.beginIndent("   ");
        $template44.addVal(r_1);
        $template44.endIndent("   ");
        throw new Throw($template44.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(6409,1451,<213,0>,<265,1>) 
    public IList lang_rascal_grammar_definition_Characters_union$bf68e50d1ee7fd6b(IList l_0, IList r_1){ 
        
        
        if((((IBool)($equal(((IList)l_0), ((IList)r_1))))).getValue()){
           return ((IList)l_0);
        
        }
        if((((IBool)($equal(((IList)l_0), ((IList)$constants.get(1)/*[]*/))))).getValue()){
           return ((IList)r_1);
        
        }
        if((((IBool)($equal(((IList)r_1), ((IList)$constants.get(1)/*[]*/))))).getValue()){
           return ((IList)l_0);
        
        }
        ITuple $TMP45 = (ITuple)($RVF.tuple(((IConstructor)(M_List.head(((IList)l_0)))), ((IList)(M_List.tail(((IList)l_0))))));
        IConstructor lhead_2 = ((IConstructor)($atuple_subscript_int(((ITuple)($TMP45)),0)));
        IList ltail_3 = ((IList)($atuple_subscript_int(((ITuple)($TMP45)),1)));
        ITuple $TMP46 = (ITuple)($RVF.tuple(((IConstructor)(M_List.head(((IList)r_1)))), ((IList)(M_List.tail(((IList)r_1))))));
        IConstructor rhead_4 = ((IConstructor)($atuple_subscript_int(((ITuple)($TMP46)),0)));
        IList rtail_5 = ((IList)($atuple_subscript_int(((ITuple)($TMP46)),1)));
        if((((IBool)($equal(((IConstructor)lhead_2), ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{}))))))).getValue()){
           return ((IList)($me.union(((IList)ltail_3), ((IList)r_1))));
        
        }
        if((((IBool)($equal(((IConstructor)rhead_4), ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{}))))))).getValue()){
           return ((IList)($me.union(((IList)l_0), ((IList)rtail_5))));
        
        }
        if((((IBool)($aint_lessequal_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin"))))),((IInteger)($aint_add_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end"))))),((IInteger)$constants.get(3)/*1*/))))).not()))).getValue()){
           return ((IList)($elm_add_alist(((IConstructor)rhead_4),((IList)($me.union(((IList)l_0), ((IList)rtail_5)))))));
        
        }
        if((((IBool)($aint_less_aint(((IInteger)($aint_add_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end"))))),((IInteger)$constants.get(3)/*1*/)))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin"))))))))).getValue()){
           return ((IList)($elm_add_alist(((IConstructor)lhead_2),((IList)($me.union(((IList)ltail_3), ((IList)r_1)))))));
        
        }
        if((((IBool)($aint_less_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin")))))).not()))).getValue()){
           if((((IBool)($aint_lessequal_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end"))))))))).getValue()){
              return ((IList)($me.union(((IList)ltail_3), ((IList)r_1))));
           
           }
        
        }
        if((((IBool)($aint_less_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin")))))).not()))).getValue()){
           if((((IBool)($aint_lessequal_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end"))))))))).getValue()){
              return ((IList)($me.union(((IList)l_0), ((IList)rtail_5))));
           
           }
        
        }
        if((((IBool)($aint_less_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end"))))))))).getValue()){
           return ((IList)($me.union(((IList)($elm_add_alist(((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin"))))), ((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end")))))}))),((IList)ltail_3)))), ((IList)rtail_5))));
        
        }
        if((((IBool)($aint_lessequal_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin")))))).not()))).getValue()){
           return ((IList)($me.union(((IList)ltail_3), ((IList)($elm_add_alist(((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin"))))), ((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end")))))}))),((IList)rtail_5)))))));
        
        }
        final Template $template47 = (Template)new Template($RVF, "did not expect to end up here! ");
        $template47.beginIndent("                               ");
        $template47.addVal(l_0);
        $template47.endIndent("                               ");
        $template47.addStr(" - ");
        $template47.beginIndent("   ");
        $template47.addVal(r_1);
        $template47.endIndent("   ");
        throw new Throw($template47.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(7989,1550,<271,0>,<322,1>) 
    public IList lang_rascal_grammar_definition_Characters_difference$73ce40b5ac114748(IList l_0, IList r_1){ 
        
        
        if((((IBool)($equal(((IList)l_0), ((IList)$constants.get(1)/*[]*/))))).getValue()){
           return ((IList)l_0);
        
        } else {
           if((((IBool)($equal(((IList)r_1), ((IList)$constants.get(1)/*[]*/))))).getValue()){
              return ((IList)l_0);
           
           }
        
        }if((((IBool)($equal(((IList)l_0), ((IList)r_1))))).getValue()){
           return ((IList)$constants.get(1)/*[]*/);
        
        }
        ITuple $TMP48 = (ITuple)($RVF.tuple(((IConstructor)(M_List.head(((IList)l_0)))), ((IList)(M_List.tail(((IList)l_0))))));
        IConstructor lhead_2 = ((IConstructor)($atuple_subscript_int(((ITuple)($TMP48)),0)));
        IList ltail_3 = ((IList)($atuple_subscript_int(((ITuple)($TMP48)),1)));
        ITuple $TMP49 = (ITuple)($RVF.tuple(((IConstructor)(M_List.head(((IList)r_1)))), ((IList)(M_List.tail(((IList)r_1))))));
        IConstructor rhead_4 = ((IConstructor)($atuple_subscript_int(((ITuple)($TMP49)),0)));
        IList rtail_5 = ((IList)($atuple_subscript_int(((ITuple)($TMP49)),1)));
        if((((IBool)($equal(((IConstructor)lhead_2), ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{}))))))).getValue()){
           return ((IList)($me.difference(((IList)ltail_3), ((IList)r_1))));
        
        }
        if((((IBool)($equal(((IConstructor)rhead_4), ((IConstructor)($RVF.constructor(CharRange_empty_range_, new IValue[]{}))))))).getValue()){
           return ((IList)($me.difference(((IList)l_0), ((IList)rtail_5))));
        
        }
        if((((IBool)($aint_lessequal_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end")))))).not()))).getValue()){
           return ((IList)($me.difference(((IList)l_0), ((IList)rtail_5))));
        
        }
        if((((IBool)($aint_less_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin"))))))))).getValue()){
           return ((IList)($elm_add_alist(((IConstructor)lhead_2),((IList)($me.difference(((IList)ltail_3), ((IList)r_1)))))));
        
        }
        if((((IBool)($aint_less_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin")))))).not()))).getValue()){
           if((((IBool)($aint_lessequal_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end"))))))))).getValue()){
              return ((IList)($me.difference(((IList)ltail_3), ((IList)r_1))));
           
           }
        
        }
        if((((IBool)($aint_less_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin")))))).not()))).getValue()){
           if((((IBool)($aint_lessequal_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end"))))))))).getValue()){
              return ((IList)($elm_add_alist(((IConstructor)($me.new_range(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin"))))), ((IInteger)(((IInteger) ((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin"))))).subtract(((IInteger)$constants.get(3)/*1*/)))))))),((IList)($me.difference(((IList)($elm_add_alist(((IConstructor)($me.new_range(((IInteger)($aint_add_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end"))))),((IInteger)$constants.get(3)/*1*/)))), ((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end")))))))),((IList)ltail_3)))), ((IList)rtail_5)))))));
           
           }
        
        }
        if((((IBool)($aint_less_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end"))))))))).getValue()){
           return ((IList)($elm_add_alist(((IConstructor)($me.new_range(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin"))))), ((IInteger)(((IInteger) ((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin"))))).subtract(((IInteger)$constants.get(3)/*1*/)))))))),((IList)($me.difference(((IList)ltail_3), ((IList)r_1)))))));
        
        }
        if((((IBool)($aint_lessequal_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "begin"))))),((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "begin")))))).not()))).getValue()){
           return ((IList)($me.difference(((IList)($elm_add_alist(((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)($aint_add_aint(((IInteger)(((IInteger)($aadt_get_field(((IConstructor)rhead_4), "end"))))),((IInteger)$constants.get(3)/*1*/)))), ((IInteger)(((IInteger)($aadt_get_field(((IConstructor)lhead_2), "end")))))}))),((IList)ltail_3)))), ((IList)rtail_5))));
        
        }
        final Template $template50 = (Template)new Template($RVF, "did not expect to end up here! ");
        $template50.beginIndent("                               ");
        $template50.addVal(l_0);
        $template50.endIndent("                               ");
        $template50.addStr(" - ");
        $template50.beginIndent("   ");
        $template50.addVal(r_1);
        $template50.endIndent("   ");
        throw new Throw($template50.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(10924,580,<339,0>,<349,1>) 
    public IConstructor lang_rascal_grammar_definition_Characters_cc2ranges$54a54e4321a65bcb(ITree cc_0){ 
        
        
        final ITree $switchVal51 = ((ITree)cc_0);
        boolean noCaseMatched_$switchVal51 = true;
        SWITCH48: switch(Fingerprint.getFingerprint($switchVal51)){
        
            case 0:
                if(noCaseMatched_$switchVal51){
                    noCaseMatched_$switchVal51 = false;
                    
                }
                
        
            default: if($isSubtypeOf($switchVal51.getType(),M_lang_rascal_syntax_Rascal.NT_Class)){
                        /*muExists*/CASE_0_0: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal51, "simpleCharclass", 1)){
                                   IValue $arg0_59 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal51))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_59.getType(), $T5)){
                                      ITree ranges_1 = ((ITree)($arg0_59));
                                      final IListWriter $listwriter53 = (IListWriter)$RVF.listWriter();
                                      final ITree $exp56 = ((ITree)($arg0_59));
                                      final int $last57 = (int)((ITree)($exp56)).getArgs().length() - 1;
                                      $LCOMP54_GEN11055:
                                      
                                      for(int $i58 = 0; $i58 <= $last57; $i58 += 2){
                                         final ITree $elem55 = ((ITree)($iter_subscript($exp56, $i58)));
                                         ITree r_2 = ((ITree)($elem55));
                                         $listwriter53.append($me.range(((ITree)r_2)));
                                      
                                      }
                                      
                                      return ((IConstructor)($me.new_char_class(((IList)($listwriter53.done())))));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal51.getType(),M_lang_rascal_syntax_Rascal.NT_Class)){
                        /*muExists*/CASE_0_1: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal51, "bracket", 1)){
                                   IValue $arg0_60 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal51))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_60.getType(), M_lang_rascal_syntax_Rascal.NT_Class)){
                                      ITree c_3 = ((ITree)($arg0_60));
                                      return ((IConstructor)($me.cc2ranges(((ITree)($arg0_60)))));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal51.getType(),M_lang_rascal_syntax_Rascal.NT_Class)){
                        /*muExists*/CASE_0_2: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal51, "complement", 1)){
                                   IValue $arg0_61 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal51))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_61.getType(), M_lang_rascal_syntax_Rascal.NT_Class)){
                                      ITree c_4 = ((ITree)($arg0_61));
                                      return ((IConstructor)($me.complement(((IConstructor)($me.cc2ranges(((ITree)($arg0_61))))))));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal51.getType(),M_lang_rascal_syntax_Rascal.NT_Class)){
                        /*muExists*/CASE_0_3: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal51, "intersection", 2)){
                                   IValue $arg0_63 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal51))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_63.getType(), M_lang_rascal_syntax_Rascal.NT_Class)){
                                      ITree l_5 = ((ITree)($arg0_63));
                                      IValue $arg1_62 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal51))), ((IInteger)$constants.get(3)/*1*/).intValue()));
                                      if($isComparable($arg1_62.getType(), M_lang_rascal_syntax_Rascal.NT_Class)){
                                         ITree r_6 = ((ITree)($arg1_62));
                                         return ((IConstructor)($me.intersection(((IConstructor)($me.cc2ranges(((ITree)($arg0_63))))), ((IConstructor)($me.cc2ranges(((ITree)($arg1_62))))))));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal51.getType(),M_lang_rascal_syntax_Rascal.NT_Class)){
                        /*muExists*/CASE_0_4: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal51, "union", 2)){
                                   IValue $arg0_65 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal51))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_65.getType(), M_lang_rascal_syntax_Rascal.NT_Class)){
                                      ITree l_7 = ((ITree)($arg0_65));
                                      IValue $arg1_64 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal51))), ((IInteger)$constants.get(3)/*1*/).intValue()));
                                      if($isComparable($arg1_64.getType(), M_lang_rascal_syntax_Rascal.NT_Class)){
                                         ITree r_8 = ((ITree)($arg1_64));
                                         return ((IConstructor)($me.union(((IConstructor)($me.cc2ranges(((ITree)($arg0_65))))), ((IConstructor)($me.cc2ranges(((ITree)($arg1_64))))))));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal51.getType(),M_lang_rascal_syntax_Rascal.NT_Class)){
                        /*muExists*/CASE_0_5: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal51, "difference", 2)){
                                   IValue $arg0_67 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal51))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_67.getType(), M_lang_rascal_syntax_Rascal.NT_Class)){
                                      ITree l_9 = ((ITree)($arg0_67));
                                      IValue $arg1_66 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal51))), ((IInteger)$constants.get(3)/*1*/).intValue()));
                                      if($isComparable($arg1_66.getType(), M_lang_rascal_syntax_Rascal.NT_Class)){
                                         ITree r_10 = ((ITree)($arg1_66));
                                         return ((IConstructor)($me.difference(((IConstructor)($me.cc2ranges(((ITree)($arg0_67))))), ((IConstructor)($me.cc2ranges(((ITree)($arg1_66))))))));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     final Template $template52 = (Template)new Template($RVF, "cc2ranges, missed a case ");
                     $template52.beginIndent("                         ");
                     $template52.addVal(cc_0);
                     $template52.endIndent("                         ");
                     throw new Throw($template52.close());
        }
        
                   
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(11512,414,<351,0>,<361,1>) 
    public IConstructor lang_rascal_grammar_definition_Characters_range$1a0b128882305baa(ITree r_0){ 
        
        
        final ITree $switchVal68 = ((ITree)r_0);
        boolean noCaseMatched_$switchVal68 = true;
        SWITCH49: switch(Fingerprint.getFingerprint($switchVal68)){
        
            case 0:
                if(noCaseMatched_$switchVal68){
                    noCaseMatched_$switchVal68 = false;
                    
                }
                
        
            default: if($isSubtypeOf($switchVal68.getType(),M_lang_rascal_syntax_Rascal.NT_Range)){
                        /*muExists*/CASE_0_0: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal68, "character", 1)){
                                   IValue $arg0_70 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal68))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_70.getType(), M_lang_rascal_syntax_Rascal.NT_Char)){
                                      ITree c_1 = ((ITree)($arg0_70));
                                      return ((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)($me.charToInt(((ITree)($arg0_70))))), ((IInteger)($me.charToInt(((ITree)($arg0_70)))))})));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal68.getType(),M_lang_rascal_syntax_Rascal.NT_Range)){
                        /*muExists*/CASE_0_1: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal68, "fromTo", 2)){
                                   IValue $arg0_74 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal68))), ((IInteger)$constants.get(5)/*0*/).intValue()));
                                   if($isComparable($arg0_74.getType(), M_lang_rascal_syntax_Rascal.NT_Char)){
                                      ITree l1_2 = ((ITree)($arg0_74));
                                      IValue $arg1_73 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal68))), ((IInteger)$constants.get(3)/*1*/).intValue()));
                                      if($isComparable($arg1_73.getType(), M_lang_rascal_syntax_Rascal.NT_Char)){
                                         ITree r1_3 = ((ITree)($arg1_73));
                                         ITuple $TMP71 = (ITuple)($RVF.tuple(((IInteger)($me.charToInt(((ITree)($arg0_74))))), ((IInteger)($me.charToInt(((ITree)($arg1_73)))))));
                                         IInteger cL_4 = ((IInteger)($atuple_subscript_int(((ITuple)($TMP71)),0)));
                                         IInteger cR_5 = ((IInteger)($atuple_subscript_int(((ITuple)($TMP71)),1)));
                                         /*muExists*/$RET72: 
                                             do {
                                                 if((((IBool)($aint_lessequal_aint(((IInteger)cL_4),((IInteger)cR_5))))).getValue()){
                                                    return ((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)cL_4), ((IInteger)cR_5)})));
                                                 
                                                 }
                                         
                                             } while(false);
                                         return ((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)cR_5), ((IInteger)cL_4)})));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     final Template $template69 = (Template)new Template($RVF, "range, missed a case ");
                     $template69.beginIndent("                     ");
                     $template69.addVal(r_0);
                     $template69.endIndent("                     ");
                     throw new Throw($template69.close());
        }
        
                   
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Characters.rsc|(11930,890,<363,0>,<379,1>) 
    public IInteger lang_rascal_grammar_definition_Characters_charToInt$e48d9965143ec1dd(ITree c_0){ 
        
        
        final ITree $switchVal75 = ((ITree)c_0);
        boolean noCaseMatched_$switchVal75 = true;
        SWITCH50: switch(Fingerprint.getConcreteFingerprint($switchVal75)){
        
            case 0:
                if(noCaseMatched_$switchVal75){
                    noCaseMatched_$switchVal75 = false;
                    
                }
                
        
            default: if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                        /*muExists*/CASE_0_0: 
                            do {
                                if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                                   final Matcher $matcher77 = (Matcher)$regExpCompile("^([^\"\'\\-\\[\\]\\\\\\>\\< ])", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal75));
                                   boolean $found78 = true;
                                   
                                       while($found78){
                                           $found78 = $matcher77.find();
                                           if($found78){
                                              IString ch_1 = ((IString)($RVF.string($matcher77.group(1))));
                                              return ((IInteger)(M_String.charAt(((IString)ch_1), ((IInteger)$constants.get(5)/*0*/))));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                        /*muExists*/CASE_0_1: 
                            do {
                                if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                                   final Matcher $matcher79 = (Matcher)$regExpCompile("^\\\\n", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal75));
                                   boolean $found80 = true;
                                   
                                       while($found80){
                                           $found80 = $matcher79.find();
                                           if($found80){
                                              return ((IInteger)(M_String.charAt(((IString)$constants.get(6)/*"
                                              "*/), ((IInteger)$constants.get(5)/*0*/))));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                        /*muExists*/CASE_0_2: 
                            do {
                                if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                                   final Matcher $matcher81 = (Matcher)$regExpCompile("^\\\\t", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal75));
                                   boolean $found82 = true;
                                   
                                       while($found82){
                                           $found82 = $matcher81.find();
                                           if($found82){
                                              return ((IInteger)(M_String.charAt(((IString)$constants.get(7)/*"	"*/), ((IInteger)$constants.get(5)/*0*/))));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                        /*muExists*/CASE_0_3: 
                            do {
                                if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                                   final Matcher $matcher83 = (Matcher)$regExpCompile("^\\\\b", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal75));
                                   boolean $found84 = true;
                                   
                                       while($found84){
                                           $found84 = $matcher83.find();
                                           if($found84){
                                              return ((IInteger)(M_String.charAt(((IString)$constants.get(8)/*""*/), ((IInteger)$constants.get(5)/*0*/))));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                        /*muExists*/CASE_0_4: 
                            do {
                                if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                                   final Matcher $matcher85 = (Matcher)$regExpCompile("^\\\\r", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal75));
                                   boolean $found86 = true;
                                   
                                       while($found86){
                                           $found86 = $matcher85.find();
                                           if($found86){
                                              return ((IInteger)(M_String.charAt(((IString)$constants.get(9)/*""*/), ((IInteger)$constants.get(5)/*0*/))));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                        /*muExists*/CASE_0_5: 
                            do {
                                if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                                   final Matcher $matcher87 = (Matcher)$regExpCompile("^\\\\f", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal75));
                                   boolean $found88 = true;
                                   
                                       while($found88){
                                           $found88 = $matcher87.find();
                                           if($found88){
                                              return ((IInteger)(M_String.charAt(((IString)$constants.get(10)/*""*/), ((IInteger)$constants.get(5)/*0*/))));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                        /*muExists*/CASE_0_6: 
                            do {
                                if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                                   final Matcher $matcher89 = (Matcher)$regExpCompile("^\\\\\\>", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal75));
                                   boolean $found90 = true;
                                   
                                       while($found90){
                                           $found90 = $matcher89.find();
                                           if($found90){
                                              return ((IInteger)(M_String.charAt(((IString)$constants.get(11)/*">"*/), ((IInteger)$constants.get(5)/*0*/))));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                        /*muExists*/CASE_0_7: 
                            do {
                                if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                                   final Matcher $matcher91 = (Matcher)$regExpCompile("^\\\\\\<", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal75));
                                   boolean $found92 = true;
                                   
                                       while($found92){
                                           $found92 = $matcher91.find();
                                           if($found92){
                                              return ((IInteger)(M_String.charAt(((IString)$constants.get(12)/*"<"*/), ((IInteger)$constants.get(5)/*0*/))));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                        /*muExists*/CASE_0_8: 
                            do {
                                if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                                   final Matcher $matcher93 = (Matcher)$regExpCompile("^\\\\([\"\'\\-\\[\\]\\\\ ])", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal75));
                                   boolean $found94 = true;
                                   
                                       while($found94){
                                           $found94 = $matcher93.find();
                                           if($found94){
                                              IString esc_2 = ((IString)($RVF.string($matcher93.group(1))));
                                              return ((IInteger)(M_String.charAt(((IString)esc_2), ((IInteger)$constants.get(5)/*0*/))));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                        /*muExists*/CASE_0_9: 
                            do {
                                if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                                   final Matcher $matcher96 = (Matcher)$regExpCompile("^\\\\u([0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F])", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal75));
                                   boolean $found97 = true;
                                   
                                       while($found97){
                                           $found97 = $matcher96.find();
                                           if($found97){
                                              IString hex_3 = ((IString)($RVF.string($matcher96.group(1))));
                                              final Template $template95 = (Template)new Template($RVF, "0x");
                                              $template95.beginIndent("  ");
                                              $template95.addStr(((IString)hex_3).getValue());
                                              $template95.endIndent("  ");
                                              return ((IInteger)(M_String.toInt(((IString)($template95.close())))));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                        /*muExists*/CASE_0_10: 
                            do {
                                if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                                   final Matcher $matcher99 = (Matcher)$regExpCompile("^\\\\U([0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F])", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal75));
                                   boolean $found100 = true;
                                   
                                       while($found100){
                                           $found100 = $matcher99.find();
                                           if($found100){
                                              IString hex_4 = ((IString)($RVF.string($matcher99.group(1))));
                                              final Template $template98 = (Template)new Template($RVF, "0x");
                                              $template98.beginIndent("  ");
                                              $template98.addStr(((IString)hex_4).getValue());
                                              $template98.endIndent("  ");
                                              return ((IInteger)(M_String.toInt(((IString)($template98.close())))));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                        /*muExists*/CASE_0_11: 
                            do {
                                if($isSubtypeOf($switchVal75.getType(),M_lang_rascal_syntax_Rascal.NT_Char)){
                                   final Matcher $matcher102 = (Matcher)$regExpCompile("^\\\\a([0-7][0-9a-fA-F])", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal75));
                                   boolean $found103 = true;
                                   
                                       while($found103){
                                           $found103 = $matcher102.find();
                                           if($found103){
                                              IString hex_5 = ((IString)($RVF.string($matcher102.group(1))));
                                              final Template $template101 = (Template)new Template($RVF, "0x");
                                              $template101.beginIndent("  ");
                                              $template101.addStr(((IString)hex_5).getValue());
                                              $template101.endIndent("  ");
                                              return ((IInteger)(M_String.toInt(((IString)($template101.close())))));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     final Template $template76 = (Template)new Template($RVF, "charToInt, missed a case ");
                     $template76.beginIndent("                         ");
                     $template76.addVal(c_0);
                     $template76.endIndent("                         ");
                     throw new Throw($template76.close());
        }
        
                   
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `lang::rascal::grammar::definition::Characters`");
    }
}