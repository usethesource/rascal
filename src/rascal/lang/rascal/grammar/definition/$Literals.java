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
public class $Literals 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.lang.rascal.grammar.definition.$Literals_$I {

    private final $Literals_$I $me;
    private final IList $constants;
    
    
    public final rascal.lang.rascal.syntax.$Rascal M_lang_rascal_syntax_Rascal;
    public final rascal.$Exception M_Exception;
    public final rascal.$Type M_Type;
    public final rascal.$List M_List;
    public final rascal.$Grammar M_Grammar;
    public final rascal.$Message M_Message;
    public final rascal.$String M_String;
    public final rascal.$ParseTree M_ParseTree;

    
    
    public final io.usethesource.vallang.type.Type $T2;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T3;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T0;	/*astr()*/
    public final io.usethesource.vallang.type.Type ADT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T4;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
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
    public final io.usethesource.vallang.type.Type ADT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostStringChars;	/*aadt("PostStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostStringChars;	/*aadt("PostStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarDefinition;	/*aadt("GrammarDefinition",[],dataSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T1;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Associativity;	/*aadt("Associativity",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringTail;	/*aadt("StringTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringTail;	/*aadt("StringTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUTLIST;	/*aadt("LAYOUTLIST",[],layoutSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Grammar;	/*aadt("Grammar",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Backslash;	/*aadt("Backslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Backslash;	/*aadt("Backslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RascalKeywords;	/*aadt("RascalKeywords",[],keywordSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comment;	/*aadt("Comment",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comment;	/*aadt("Comment",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Commands;	/*aadt("Commands",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Commands;	/*aadt("Commands",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StructuredType;	/*aadt("StructuredType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StructuredType;	/*aadt("StructuredType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visit;	/*aadt("Visit",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visit;	/*aadt("Visit",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Command;	/*aadt("Command",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Command;	/*aadt("Command",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/

    public $Literals(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Literals(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Literals_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.lang.rascal.grammar.definition.$Literals.class, this);
       
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
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/lang/rascal/grammar/definition/$Literals.constants", 25, "91c33619d90ec2b18e2e9ff4d2adcbee");
       NT_PostProtocolChars = $lex("PostProtocolChars");
       ADT_PostProtocolChars = $adt("PostProtocolChars");
       NT_ProtocolChars = $lex("ProtocolChars");
       ADT_ProtocolChars = $adt("ProtocolChars");
       NT_Visibility = $sort("Visibility");
       ADT_Visibility = $adt("Visibility");
       NT_FunctionType = $sort("FunctionType");
       ADT_FunctionType = $adt("FunctionType");
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
       NT_Replacement = $sort("Replacement");
       ADT_Replacement = $adt("Replacement");
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
       NT_ImportedModule = $sort("ImportedModule");
       ADT_ImportedModule = $adt("ImportedModule");
       NT_Case = $sort("Case");
       ADT_Case = $adt("Case");
       NT_LocalVariableDeclaration = $sort("LocalVariableDeclaration");
       ADT_LocalVariableDeclaration = $adt("LocalVariableDeclaration");
       NT_BooleanLiteral = $lex("BooleanLiteral");
       ADT_BooleanLiteral = $adt("BooleanLiteral");
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
       NT_OptionalExpression = $sort("OptionalExpression");
       ADT_OptionalExpression = $adt("OptionalExpression");
       NT_Renamings = $sort("Renamings");
       ADT_Renamings = $adt("Renamings");
       ADT_GrammarDefinition = $adt("GrammarDefinition");
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
       NT_PreStringChars = $lex("PreStringChars");
       ADT_PreStringChars = $adt("PreStringChars");
       NT_Nonterminal = $lex("Nonterminal");
       ADT_Nonterminal = $adt("Nonterminal");
       NT_StringLiteral = $sort("StringLiteral");
       ADT_StringLiteral = $adt("StringLiteral");
       NT_NonterminalLabel = $lex("NonterminalLabel");
       ADT_NonterminalLabel = $adt("NonterminalLabel");
       NT_Variable = $sort("Variable");
       ADT_Variable = $adt("Variable");
       NT_Bound = $sort("Bound");
       ADT_Bound = $adt("Bound");
       NT_TypeArg = $sort("TypeArg");
       ADT_TypeArg = $adt("TypeArg");
       NT_RegExp = $lex("RegExp");
       ADT_RegExp = $adt("RegExp");
       NT_PathPart = $sort("PathPart");
       ADT_PathPart = $adt("PathPart");
       ADT_Associativity = $adt("Associativity");
       NT_Signature = $sort("Signature");
       ADT_Signature = $adt("Signature");
       NT_KeywordFormal = $sort("KeywordFormal");
       ADT_KeywordFormal = $adt("KeywordFormal");
       NT_ModuleActuals = $sort("ModuleActuals");
       ADT_ModuleActuals = $adt("ModuleActuals");
       NT_Tags = $sort("Tags");
       ADT_Tags = $adt("Tags");
       ADT_Condition = $adt("Condition");
       NT_URLChars = $lex("URLChars");
       ADT_URLChars = $adt("URLChars");
       NT_Class = $sort("Class");
       ADT_Class = $adt("Class");
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
       NT_Char = $lex("Char");
       ADT_Char = $adt("Char");
       NT_StringTail = $sort("StringTail");
       ADT_StringTail = $adt("StringTail");
       NT_DataTypeSelector = $sort("DataTypeSelector");
       ADT_DataTypeSelector = $adt("DataTypeSelector");
       NT_CaseInsensitiveStringConstant = $lex("CaseInsensitiveStringConstant");
       ADT_CaseInsensitiveStringConstant = $adt("CaseInsensitiveStringConstant");
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
       NT_UserType = $sort("UserType");
       ADT_UserType = $adt("UserType");
       ADT_CharRange = $adt("CharRange");
       NT_Import = $sort("Import");
       ADT_Import = $adt("Import");
       NT_Variant = $sort("Variant");
       ADT_Variant = $adt("Variant");
       NT_Comprehension = $sort("Comprehension");
       ADT_Comprehension = $adt("Comprehension");
       ADT_LAYOUTLIST = $layouts("LAYOUTLIST");
       ADT_Message = $adt("Message");
       NT_ConcreteHole = $sort("ConcreteHole");
       ADT_ConcreteHole = $adt("ConcreteHole");
       ADT_Grammar = $adt("Grammar");
       NT_StringMiddle = $sort("StringMiddle");
       ADT_StringMiddle = $adt("StringMiddle");
       NT_Sym = $sort("Sym");
       ADT_Sym = $adt("Sym");
       NT_DateAndTime = $lex("DateAndTime");
       ADT_DateAndTime = $adt("DateAndTime");
       NT_RealLiteral = $lex("RealLiteral");
       ADT_RealLiteral = $adt("RealLiteral");
       NT_Backslash = $lex("Backslash");
       ADT_Backslash = $adt("Backslash");
       NT_Formals = $sort("Formals");
       ADT_Formals = $adt("Formals");
       NT_FunctionModifiers = $sort("FunctionModifiers");
       ADT_FunctionModifiers = $adt("FunctionModifiers");
       NT_MidPathChars = $lex("MidPathChars");
       ADT_MidPathChars = $adt("MidPathChars");
       NT_QualifiedName = $sort("QualifiedName");
       ADT_QualifiedName = $adt("QualifiedName");
       NT_MidProtocolChars = $lex("MidProtocolChars");
       ADT_MidProtocolChars = $adt("MidProtocolChars");
       ADT_RascalKeywords = $keywords("RascalKeywords");
       NT_PathTail = $sort("PathTail");
       ADT_PathTail = $adt("PathTail");
       NT_Parameters = $sort("Parameters");
       ADT_Parameters = $adt("Parameters");
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
       NT_EvalCommand = $sort("EvalCommand");
       ADT_EvalCommand = $adt("EvalCommand");
       NT_PathChars = $lex("PathChars");
       ADT_PathChars = $adt("PathChars");
       NT_JustTime = $lex("JustTime");
       ADT_JustTime = $adt("JustTime");
       NT_FunctionModifier = $sort("FunctionModifier");
       ADT_FunctionModifier = $adt("FunctionModifier");
       NT_ProdModifier = $sort("ProdModifier");
       ADT_ProdModifier = $adt("ProdModifier");
       NT_Statement = $sort("Statement");
       ADT_Statement = $adt("Statement");
       NT_ConcretePart = $lex("ConcretePart");
       ADT_ConcretePart = $adt("ConcretePart");
       NT_Literal = $sort("Literal");
       ADT_Literal = $adt("Literal");
       NT_Toplevel = $sort("Toplevel");
       ADT_Toplevel = $adt("Toplevel");
       NT_Comment = $lex("Comment");
       ADT_Comment = $adt("Comment");
       NT_Commands = $sort("Commands");
       ADT_Commands = $adt("Commands");
       NT_StructuredType = $sort("StructuredType");
       ADT_StructuredType = $adt("StructuredType");
       NT_Visit = $sort("Visit");
       ADT_Visit = $adt("Visit");
       NT_Command = $sort("Command");
       ADT_Command = $adt("Command");
       NT_Assignable = $sort("Assignable");
       ADT_Assignable = $adt("Assignable");
       NT_UnicodeEscape = $lex("UnicodeEscape");
       ADT_UnicodeEscape = $adt("UnicodeEscape");
       NT_BasicType = $sort("BasicType");
       ADT_BasicType = $adt("BasicType");
       NT_CommonKeywordParameters = $sort("CommonKeywordParameters");
       ADT_CommonKeywordParameters = $adt("CommonKeywordParameters");
       NT_ProtocolTail = $sort("ProtocolTail");
       ADT_ProtocolTail = $adt("ProtocolTail");
       $T2 = $TF.valueType();
       $T3 = $TF.parameterType("T", $T2);
       $T0 = $TF.stringType();
       NT_Mapping_Expression = $parameterizedSort("Mapping", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       $T4 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_1 = $parameterizedSort("KeywordArguments", new Type[] { $T4 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       NT_Mapping_Pattern = $parameterizedSort("Mapping", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       $T5 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_Expression = $parameterizedSort("KeywordArguments", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       NT_Mapping_1 = $parameterizedSort("Mapping", new Type[] { $T4 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       $T1 = $TF.listType($T3);
       NT_KeywordArguments_Pattern = $parameterizedSort("KeywordArguments", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       NT_KeywordArgument_1 = $parameterizedSort("KeywordArgument", new Type[] { $T4 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T4 });
       ADT_Mapping_Expression = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Expression });
       ADT_KeywordArguments_1 = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { $T4 });
       ADT_Mapping_Pattern = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Pattern });
       ADT_KeywordArguments_Expression = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Expression });
       ADT_Mapping_1 = $TF.abstractDataType($TS, "Mapping", new Type[] { $T4 });
       ADT_KeywordArguments_Pattern = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Pattern });
       ADT_KeywordArgument_1 = $TF.abstractDataType($TS, "KeywordArgument", new Type[] { $T4 });
    
       
       
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
    public IValue head(IValue $P0){ // Generated by Resolver
       return (IValue) M_List.head($P0);
    }
    public IBool isAliasType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isAliasType($P0);
    }
    public IInteger size(IValue $P0){ // Generated by Resolver
       IInteger $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IInteger)M_String.String_size$4611676944e933d5((IString) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IInteger)M_List.List_size$ba7443328d8b4a27((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
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
    public IConstructor literals(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (IConstructor)lang_rascal_grammar_definition_Literals_literals$e024186903b8b684((IConstructor) $P0);
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
    public IConstructor ciliteral(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IConstructor)lang_rascal_grammar_definition_Literals_ciliteral$0db2671e2383e06a((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRelType($P0);
    }
    public IValue character(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isNonTerminal($P0Type, ((IConstructor)$constants.get(0)/*lex("StringCharacter")*/))){
         $result = (IValue)lang_rascal_grammar_definition_Literals_character$d5195547a7cb317f((ITree) $P0);
         if($result != null) return $result;
       }
       if($isNonTerminal($P0Type, ((IConstructor)$constants.get(1)/*lex("Char")*/))){
         return $RVF.constructor(M_lang_rascal_syntax_Rascal.Range_character_Char, new IValue[]{(ITree) $P0});
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
    public IList str2syms(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)lang_rascal_grammar_definition_Literals_str2syms$dee81798dbf5a1ce((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isBoolType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isBoolType($P0);
    }
    public INode literal(IValue $P0){ // Generated by Resolver
       INode $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (INode)lang_rascal_grammar_definition_Literals_literal$e1a9d9e83f386de5((IString) $P0);
         if($result != null) return $result;
       }
       if($isNonTerminal($P0Type, ((IConstructor)$constants.get(2)/*lex("StringConstant")*/))){
         return $RVF.constructor(M_lang_rascal_syntax_Rascal.Sym_literal_StringConstant, new IValue[]{(ITree) $P0});
       }
       if($isNonTerminal($P0Type, M_lang_rascal_syntax_Rascal.NT_Literal)){
         throw new RuntimeException("Constructor `literal` is overloaded and can only be called with qualifier");
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
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
    public IList cistr2syms(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)lang_rascal_grammar_definition_Literals_cistr2syms$2389f577bccc5335((IString) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
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
    public IString unescapeLiteral(IValue $P0){ // Generated by Resolver
       IString $result = null;
       Type $P0Type = $P0.getType();
       if($isNonTerminal($P0Type, ((IConstructor)$constants.get(3)/*lex("CaseInsensitiveStringConstant")*/))){
         $result = (IString)lang_rascal_grammar_definition_Literals_unescapeLiteral$1ee78916103732f2((ITree) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IString)lang_rascal_grammar_definition_Literals_unescapeLiteral$e6a60df99649b8e5((IString) $P0);
         if($result != null) return $result;
       }
       if($isNonTerminal($P0Type, ((IConstructor)$constants.get(2)/*lex("StringConstant")*/))){
         $result = (IString)lang_rascal_grammar_definition_Literals_unescapeLiteral$f2188665ba46f5ed((ITree) $P0);
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
    public IBool isFunctionType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isFunctionType($P0);
    }
    public IValue glb(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_Type.glb($P0, $P1);
    }
    public IBool isIntType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isIntType($P0);
    }
    public IBool isDateTimeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isDateTimeType($P0);
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Literals.rsc|(482,135,<16,0>,<18,1>) 
    public IConstructor lang_rascal_grammar_definition_Literals_literals$e024186903b8b684(IConstructor g_0){ 
        
        
        final ISetWriter $setwriter0 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP1_GEN565:
        for(IValue $elem2_for : ((IConstructor)g_0)){
            IValue $elem2 = (IValue) $elem2_for;
            $SCOMP1_GEN565_DESC565:
            for(IValue $elem3 : new DescendantMatchIterator($elem2, 
                new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                if($isComparable($elem3.getType(), M_ParseTree.ADT_Symbol)){
                   if($has_type_and_arity($elem3, M_ParseTree.Symbol_lit_str, 1)){
                      IValue $arg0_4 = (IValue)($subscript_int(((IValue)($elem3)),0));
                      if($isComparable($arg0_4.getType(), $T0)){
                         IString s_1 = ((IString)($arg0_4));
                         $setwriter0.insert($me.literal(((IString)($arg0_4))));
                      
                      } else {
                         continue $SCOMP1_GEN565_DESC565;
                      }
                   } else {
                      continue $SCOMP1_GEN565_DESC565;
                   }
                } else {
                   continue $SCOMP1_GEN565_DESC565;
                }
            }
            continue $SCOMP1_GEN565;
                         
        }
        
                    final ISetWriter $setwriter5 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP6_GEN597:
        for(IValue $elem7_for : ((IConstructor)g_0)){
            IValue $elem7 = (IValue) $elem7_for;
            $SCOMP6_GEN597_DESC597:
            for(IValue $elem8 : new DescendantMatchIterator($elem7, 
                new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                if($isComparable($elem8.getType(), M_ParseTree.ADT_Symbol)){
                   if($has_type_and_arity($elem8, M_ParseTree.Symbol_cilit_str, 1)){
                      IValue $arg0_9 = (IValue)($subscript_int(((IValue)($elem8)),0));
                      if($isComparable($arg0_9.getType(), $T0)){
                         IString s_2 = ((IString)($arg0_9));
                         $setwriter5.insert($me.ciliteral(((IString)($arg0_9))));
                      
                      } else {
                         continue $SCOMP6_GEN597_DESC597;
                      }
                   } else {
                      continue $SCOMP6_GEN597_DESC597;
                   }
                } else {
                   continue $SCOMP6_GEN597_DESC597;
                }
            }
            continue $SCOMP6_GEN597;
                         
        }
        
                    return ((IConstructor)(M_Grammar.compose(((IConstructor)g_0), ((IConstructor)(M_Grammar.grammar(((ISet)$constants.get(4)/*{}*/), ((ISet)($aset_add_aset(((ISet)($setwriter0.done())),((ISet)($setwriter5.done())))))))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Literals.rsc|(619,63,<20,0>,<20,63>) 
    public IConstructor lang_rascal_grammar_definition_Literals_literal$e1a9d9e83f386de5(IString s_0){ 
        
        
        return ((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)s_0)}))), ((IList)($me.str2syms(((IString)s_0)))), ((ISet)$constants.get(4)/*{}*/)})));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Literals.rsc|(683,69,<21,0>,<21,69>) 
    public IConstructor lang_rascal_grammar_definition_Literals_ciliteral$0db2671e2383e06a(IString s_0){ 
        
        
        return ((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_cilit_str, new IValue[]{((IString)s_0)}))), ((IList)($me.cistr2syms(((IString)s_0)))), ((ISet)$constants.get(4)/*{}*/)})));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Literals.rsc|(754,145,<23,0>,<26,1>) 
    public IList lang_rascal_grammar_definition_Literals_str2syms$dee81798dbf5a1ce(IString x_0){ 
        
        
        if((((IBool)($equal(((IString)x_0), ((IString)$constants.get(5)/*""*/))))).getValue()){
           return ((IList)$constants.get(6)/*[]*/);
        
        }
        final IListWriter $listwriter10 = (IListWriter)$RVF.listWriter();
        final IInteger $lst2 = ((IInteger)(M_String.size(((IString)x_0))));
        final boolean $dir3 = ((IInteger)$constants.get(7)/*0*/).less($lst2).getValue();
        
        $LCOMP11_GEN856:
        for(IInteger $elem13 = ((IInteger)$constants.get(7)/*0*/); $dir3 ? $aint_less_aint($elem13,$lst2).getValue() 
                                  : $aint_lessequal_aint($elem13,$lst2).not().getValue(); $elem13 = $aint_add_aint($elem13,$dir3 ? ((IInteger)$constants.get(8)/*1*/) : ((IInteger)$constants.get(9)/*-1*/))){
            IInteger i_1 = ((IInteger)($elem13));
            final IInteger $subject_val12 = ((IInteger)(M_String.charAt(((IString)x_0), ((IInteger)i_1))));
            IInteger c_2 = null;
            $listwriter10.append($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)($subject_val12)), ((IInteger)($subject_val12))}))))))}));
        }
        
        return ((IList)($listwriter10.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Literals.rsc|(901,344,<28,0>,<37,1>) 
    public IList lang_rascal_grammar_definition_Literals_cistr2syms$2389f577bccc5335(IString x_0){ 
        
        
        final IListWriter listwriter_FOR1 = (IListWriter)$RVF.listWriter();
        /*muExists*/FOR1: 
            do {
                final IInteger $lst7 = ((IInteger)(M_String.size(((IString)x_0))));
                final boolean $dir8 = ((IInteger)$constants.get(7)/*0*/).less($lst7).getValue();
                
                FOR1_GEN948:
                for(IInteger $elem15 = ((IInteger)$constants.get(7)/*0*/); $dir8 ? $aint_less_aint($elem15,$lst7).getValue() 
                                          : $aint_lessequal_aint($elem15,$lst7).not().getValue(); $elem15 = $aint_add_aint($elem15,$dir8 ? ((IInteger)$constants.get(8)/*1*/) : ((IInteger)$constants.get(9)/*-1*/))){
                    IInteger i_1 = ((IInteger)($elem15));
                    final IInteger $subject_val14 = ((IInteger)(M_String.charAt(((IString)x_0), ((IInteger)i_1))));
                    IInteger c_2 = null;
                    if((((IBool)($aint_less_aint(((IInteger)($subject_val14)),((IInteger)$constants.get(10)/*65*/)).not()))).getValue()){
                       if((((IBool)($aint_lessequal_aint(((IInteger)($subject_val14)),((IInteger)$constants.get(11)/*90*/))))).getValue()){
                          listwriter_FOR1.append($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)($subject_val14)), ((IInteger)($subject_val14))}))), $RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)($aint_add_aint(((IInteger)($subject_val14)),((IInteger)$constants.get(12)/*32*/)))), ((IInteger)($aint_add_aint(((IInteger)($subject_val14)),((IInteger)$constants.get(12)/*32*/))))}))))}));
                       
                       } else {
                          if((((IBool)($aint_less_aint(((IInteger)($subject_val14)),((IInteger)$constants.get(13)/*97*/)).not()))).getValue()){
                             if((((IBool)($aint_lessequal_aint(((IInteger)($subject_val14)),((IInteger)$constants.get(14)/*122*/))))).getValue()){
                                listwriter_FOR1.append($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)($subject_val14)), ((IInteger)($subject_val14))}))), $RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)(((IInteger) ((IInteger)($subject_val14)).subtract(((IInteger)$constants.get(12)/*32*/))))), ((IInteger)(((IInteger) ((IInteger)($subject_val14)).subtract(((IInteger)$constants.get(12)/*32*/)))))}))))}));
                             
                             } else {
                                listwriter_FOR1.append($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)($subject_val14)), ((IInteger)($subject_val14))}))))))}));
                             
                             }
                          } else {
                             listwriter_FOR1.append($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)($subject_val14)), ((IInteger)($subject_val14))}))))))}));
                          
                          }
                       }
                    } else {
                       if((((IBool)($aint_less_aint(((IInteger)($subject_val14)),((IInteger)$constants.get(13)/*97*/)).not()))).getValue()){
                          if((((IBool)($aint_lessequal_aint(((IInteger)($subject_val14)),((IInteger)$constants.get(14)/*122*/))))).getValue()){
                             listwriter_FOR1.append($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)($subject_val14)), ((IInteger)($subject_val14))}))), $RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)(((IInteger) ((IInteger)($subject_val14)).subtract(((IInteger)$constants.get(12)/*32*/))))), ((IInteger)(((IInteger) ((IInteger)($subject_val14)).subtract(((IInteger)$constants.get(12)/*32*/)))))}))))}));
                          
                          } else {
                             listwriter_FOR1.append($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)($subject_val14)), ((IInteger)($subject_val14))}))))))}));
                          
                          }
                       } else {
                          listwriter_FOR1.append($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.CharRange_range_int_int, new IValue[]{((IInteger)($subject_val14)), ((IInteger)($subject_val14))}))))))}));
                       
                       }
                    }}
                continue FOR1;
        
            } while(false);
        return ((IList)(listwriter_FOR1.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Literals.rsc|(1247,122,<39,0>,<39,122>) 
    public IString lang_rascal_grammar_definition_Literals_unescapeLiteral$1ee78916103732f2(ITree s_0){ 
        
        
        final Template $template16 = (Template)new Template($RVF, "");
        /*muExists*/LAB4: 
            do {
                final ITree $exp18 = ((ITree)(((ITree)($aadt_get_field(((ITree)s_0), "chars")))));
                final int $last19 = (int)((ITree)($exp18)).getArgs().length() - 1;
                LAB4_GEN1316:
                
                for(int $i20 = 0; $i20 <= $last19; $i20 += 1){
                   final ITree $elem17 = ((ITree)($iter_subscript($exp18, $i20)));
                   ITree ch_1 = ((ITree)($elem17));
                   ;$template16.addStr(((IString)($me.character(((ITree)ch_1)))).getValue());
                
                }
                continue LAB4;
        
            } while(false);
        return ((IString)($template16.close()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Literals.rsc|(1371,107,<41,0>,<41,107>) 
    public IString lang_rascal_grammar_definition_Literals_unescapeLiteral$f2188665ba46f5ed(ITree s_0){ 
        
        
        final Template $template21 = (Template)new Template($RVF, "");
        /*muExists*/LAB5: 
            do {
                final ITree $exp23 = ((ITree)(((ITree)($aadt_get_field(((ITree)s_0), "chars")))));
                final int $last24 = (int)((ITree)($exp23)).getArgs().length() - 1;
                LAB5_GEN1425:
                
                for(int $i25 = 0; $i25 <= $last24; $i25 += 1){
                   final ITree $elem22 = ((ITree)($iter_subscript($exp23, $i25)));
                   ITree ch_1 = ((ITree)($elem22));
                   ;$template21.addStr(((IString)($me.character(((ITree)ch_1)))).getValue());
                
                }
                continue LAB5;
        
            } while(false);
        return ((IString)($template21.close()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Literals.rsc|(1480,1032,<43,0>,<60,1>) 
    public IString lang_rascal_grammar_definition_Literals_character$d5195547a7cb317f(ITree c_0){ 
        
        
        final ITree $switchVal26 = ((ITree)c_0);
        boolean noCaseMatched_$switchVal26 = true;
        SWITCH6: switch(Fingerprint.getConcreteFingerprint($switchVal26)){
        
            case 0:
                if(noCaseMatched_$switchVal26){
                    noCaseMatched_$switchVal26 = false;
                    
                }
                
        
            default: if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                        /*muExists*/CASE_0_0: 
                            do {
                                if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                                   final Matcher $matcher29 = (Matcher)$regExpCompile("^([^\"\'\\\\\\>\\<])", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal26));
                                   boolean $found30 = true;
                                   
                                       while($found30){
                                           $found30 = $matcher29.find();
                                           if($found30){
                                              IString ch_1 = ((IString)($RVF.string($matcher29.group(1))));
                                              final Template $template28 = (Template)new Template($RVF, "");
                                              $template28.addStr(((IString)ch_1).getValue());
                                              return ((IString)($template28.close()));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                        /*muExists*/CASE_0_1: 
                            do {
                                if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                                   final Matcher $matcher31 = (Matcher)$regExpCompile("^\\\\n", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal26));
                                   boolean $found32 = true;
                                   
                                       while($found32){
                                           $found32 = $matcher31.find();
                                           if($found32){
                                              return ((IString)$constants.get(15)/*"
                                              "*/);
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                        /*muExists*/CASE_0_2: 
                            do {
                                if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                                   final Matcher $matcher33 = (Matcher)$regExpCompile("^\\\\t", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal26));
                                   boolean $found34 = true;
                                   
                                       while($found34){
                                           $found34 = $matcher33.find();
                                           if($found34){
                                              return ((IString)$constants.get(16)/*"	"*/);
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                        /*muExists*/CASE_0_3: 
                            do {
                                if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                                   final Matcher $matcher35 = (Matcher)$regExpCompile("^\\\\b", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal26));
                                   boolean $found36 = true;
                                   
                                       while($found36){
                                           $found36 = $matcher35.find();
                                           if($found36){
                                              return ((IString)$constants.get(17)/*""*/);
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                        /*muExists*/CASE_0_4: 
                            do {
                                if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                                   final Matcher $matcher37 = (Matcher)$regExpCompile("^\\\\r", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal26));
                                   boolean $found38 = true;
                                   
                                       while($found38){
                                           $found38 = $matcher37.find();
                                           if($found38){
                                              return ((IString)$constants.get(18)/*""*/);
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                        /*muExists*/CASE_0_5: 
                            do {
                                if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                                   final Matcher $matcher39 = (Matcher)$regExpCompile("^\\\\f", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal26));
                                   boolean $found40 = true;
                                   
                                       while($found40){
                                           $found40 = $matcher39.find();
                                           if($found40){
                                              return ((IString)$constants.get(19)/*""*/);
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                        /*muExists*/CASE_0_6: 
                            do {
                                if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                                   final Matcher $matcher41 = (Matcher)$regExpCompile("^\\\\\\>", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal26));
                                   boolean $found42 = true;
                                   
                                       while($found42){
                                           $found42 = $matcher41.find();
                                           if($found42){
                                              return ((IString)$constants.get(20)/*">"*/);
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                        /*muExists*/CASE_0_7: 
                            do {
                                if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                                   final Matcher $matcher43 = (Matcher)$regExpCompile("^\\\\\\<", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal26));
                                   boolean $found44 = true;
                                   
                                       while($found44){
                                           $found44 = $matcher43.find();
                                           if($found44){
                                              return ((IString)$constants.get(21)/*"<"*/);
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                        /*muExists*/CASE_0_8: 
                            do {
                                if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                                   final Matcher $matcher46 = (Matcher)$regExpCompile("^\\\\([\"\'\\\\ ])", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal26));
                                   boolean $found47 = true;
                                   
                                       while($found47){
                                           $found47 = $matcher46.find();
                                           if($found47){
                                              IString esc_2 = ((IString)($RVF.string($matcher46.group(1))));
                                              final Template $template45 = (Template)new Template($RVF, "");
                                              $template45.addStr(((IString)esc_2).getValue());
                                              return ((IString)($template45.close()));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                        /*muExists*/CASE_0_9: 
                            do {
                                if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                                   final Matcher $matcher49 = (Matcher)$regExpCompile("^\\\\u([0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F])", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal26));
                                   boolean $found50 = true;
                                   
                                       while($found50){
                                           $found50 = $matcher49.find();
                                           if($found50){
                                              IString hex_3 = ((IString)($RVF.string($matcher49.group(1))));
                                              final Template $template48 = (Template)new Template($RVF, "0x");
                                              $template48.beginIndent("  ");
                                              $template48.addStr(((IString)hex_3).getValue());
                                              $template48.endIndent("  ");
                                              return ((IString)(M_String.stringChar(((IInteger)(M_String.toInt(((IString)($template48.close()))))))));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                        /*muExists*/CASE_0_10: 
                            do {
                                if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                                   final Matcher $matcher52 = (Matcher)$regExpCompile("^\\\\U([0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F][0-9a-fA-F])", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal26));
                                   boolean $found53 = true;
                                   
                                       while($found53){
                                           $found53 = $matcher52.find();
                                           if($found53){
                                              IString hex_4 = ((IString)($RVF.string($matcher52.group(1))));
                                              final Template $template51 = (Template)new Template($RVF, "0x");
                                              $template51.beginIndent("  ");
                                              $template51.addStr(((IString)hex_4).getValue());
                                              $template51.endIndent("  ");
                                              return ((IString)(M_String.stringChar(((IInteger)(M_String.toInt(((IString)($template51.close()))))))));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                        /*muExists*/CASE_0_11: 
                            do {
                                if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                                   final Matcher $matcher55 = (Matcher)$regExpCompile("^\\\\a([0-7][0-9a-fA-F])", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal26));
                                   boolean $found56 = true;
                                   
                                       while($found56){
                                           $found56 = $matcher55.find();
                                           if($found56){
                                              IString hex_5 = ((IString)($RVF.string($matcher55.group(1))));
                                              final Template $template54 = (Template)new Template($RVF, "0x");
                                              $template54.beginIndent("  ");
                                              $template54.addStr(((IString)hex_5).getValue());
                                              $template54.endIndent("  ");
                                              return ((IString)(M_String.stringChar(((IInteger)(M_String.toInt(((IString)($template54.close()))))))));
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                        /*muExists*/CASE_0_12: 
                            do {
                                if($isSubtypeOf($switchVal26.getType(),M_lang_rascal_syntax_Rascal.NT_StringCharacter)){
                                   final Matcher $matcher57 = (Matcher)$regExpCompile("^\n[ \t]* \'", org.rascalmpl.values.parsetrees.TreeAdapter.yield($switchVal26));
                                   boolean $found58 = true;
                                   
                                       while($found58){
                                           $found58 = $matcher57.find();
                                           if($found58){
                                              return ((IString)$constants.get(15)/*"
                                              "*/);
                                           
                                           }
                                   
                                       }
                                
                                }
                        
                            } while(false);
                     
                     }
                     final Template $template27 = (Template)new Template($RVF, "character, missed a case ");
                     $template27.beginIndent("                         ");
                     $template27.addVal(c_0);
                     $template27.endIndent("                         ");
                     throw new Throw($template27.close());
        }
        
                   
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Literals.rsc|(2514,315,<62,0>,<75,1>) 
    public IString lang_rascal_grammar_definition_Literals_unescapeLiteral$e6a60df99649b8e5(IString s_0){ 
        
        
        try {
            IValue $visitResult = $TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{$TF.stringType()}, 
                                          new io.usethesource.vallang.IConstructor[]{}, 
                                          $RVF.bool(false)),
                 s_0,
                 (IVisitFunction) (IValue $VISIT20_subject, TraversalState $traversalState) -> {
                     VISIT20:switch(Fingerprint.getFingerprint($VISIT20_subject)){
                     
                         case 0:
                             
                     
                         default: 
                             if($isSubtypeOf($VISIT20_subject.getType(),$T0)){
                                /*muExists*/CASE_0_0: 
                                    do {
                                        final Matcher $matcher60 = (Matcher)$regExpCompile("\\\\b", ((IString)($VISIT20_subject)).getValue());
                                        boolean $found61 = true;
                                        
                                            while($found61){
                                                $found61 = $matcher60.find();
                                                if($found61){
                                                   $traversalState.setBegin($matcher60.start());
                                                   $traversalState.setEnd($matcher60.end());
                                                   IString $replacement59 = (IString)(((IString)$constants.get(17)/*""*/));
                                                   if($isSubtypeOf($replacement59.getType(),$VISIT20_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement59;
                                                   
                                                   } else {
                                                      break VISIT20;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT20_subject.getType(),$T0)){
                                /*muExists*/CASE_0_1: 
                                    do {
                                        final Matcher $matcher63 = (Matcher)$regExpCompile("\\\\f", ((IString)($VISIT20_subject)).getValue());
                                        boolean $found64 = true;
                                        
                                            while($found64){
                                                $found64 = $matcher63.find();
                                                if($found64){
                                                   $traversalState.setBegin($matcher63.start());
                                                   $traversalState.setEnd($matcher63.end());
                                                   IString $replacement62 = (IString)(((IString)$constants.get(19)/*""*/));
                                                   if($isSubtypeOf($replacement62.getType(),$VISIT20_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement62;
                                                   
                                                   } else {
                                                      break VISIT20;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT20_subject.getType(),$T0)){
                                /*muExists*/CASE_0_2: 
                                    do {
                                        final Matcher $matcher66 = (Matcher)$regExpCompile("\\\\n", ((IString)($VISIT20_subject)).getValue());
                                        boolean $found67 = true;
                                        
                                            while($found67){
                                                $found67 = $matcher66.find();
                                                if($found67){
                                                   $traversalState.setBegin($matcher66.start());
                                                   $traversalState.setEnd($matcher66.end());
                                                   IString $replacement65 = (IString)(((IString)$constants.get(15)/*"
                                                   "*/));
                                                   if($isSubtypeOf($replacement65.getType(),$VISIT20_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement65;
                                                   
                                                   } else {
                                                      break VISIT20;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT20_subject.getType(),$T0)){
                                /*muExists*/CASE_0_3: 
                                    do {
                                        final Matcher $matcher69 = (Matcher)$regExpCompile("\\\\t", ((IString)($VISIT20_subject)).getValue());
                                        boolean $found70 = true;
                                        
                                            while($found70){
                                                $found70 = $matcher69.find();
                                                if($found70){
                                                   $traversalState.setBegin($matcher69.start());
                                                   $traversalState.setEnd($matcher69.end());
                                                   IString $replacement68 = (IString)(((IString)$constants.get(16)/*"	"*/));
                                                   if($isSubtypeOf($replacement68.getType(),$VISIT20_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement68;
                                                   
                                                   } else {
                                                      break VISIT20;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT20_subject.getType(),$T0)){
                                /*muExists*/CASE_0_4: 
                                    do {
                                        final Matcher $matcher72 = (Matcher)$regExpCompile("\\\\r", ((IString)($VISIT20_subject)).getValue());
                                        boolean $found73 = true;
                                        
                                            while($found73){
                                                $found73 = $matcher72.find();
                                                if($found73){
                                                   $traversalState.setBegin($matcher72.start());
                                                   $traversalState.setEnd($matcher72.end());
                                                   IString $replacement71 = (IString)(((IString)$constants.get(18)/*""*/));
                                                   if($isSubtypeOf($replacement71.getType(),$VISIT20_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement71;
                                                   
                                                   } else {
                                                      break VISIT20;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT20_subject.getType(),$T0)){
                                /*muExists*/CASE_0_5: 
                                    do {
                                        final Matcher $matcher75 = (Matcher)$regExpCompile("\\\\\"", ((IString)($VISIT20_subject)).getValue());
                                        boolean $found76 = true;
                                        
                                            while($found76){
                                                $found76 = $matcher75.find();
                                                if($found76){
                                                   $traversalState.setBegin($matcher75.start());
                                                   $traversalState.setEnd($matcher75.end());
                                                   IString $replacement74 = (IString)(((IString)$constants.get(22)/*"""*/));
                                                   if($isSubtypeOf($replacement74.getType(),$VISIT20_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement74;
                                                   
                                                   } else {
                                                      break VISIT20;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT20_subject.getType(),$T0)){
                                /*muExists*/CASE_0_6: 
                                    do {
                                        final Matcher $matcher78 = (Matcher)$regExpCompile("\\\\\'", ((IString)($VISIT20_subject)).getValue());
                                        boolean $found79 = true;
                                        
                                            while($found79){
                                                $found79 = $matcher78.find();
                                                if($found79){
                                                   $traversalState.setBegin($matcher78.start());
                                                   $traversalState.setEnd($matcher78.end());
                                                   IString $replacement77 = (IString)(((IString)$constants.get(23)/*"'"*/));
                                                   if($isSubtypeOf($replacement77.getType(),$VISIT20_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement77;
                                                   
                                                   } else {
                                                      break VISIT20;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT20_subject.getType(),$T0)){
                                /*muExists*/CASE_0_7: 
                                    do {
                                        final Matcher $matcher81 = (Matcher)$regExpCompile("\\\\\\\\", ((IString)($VISIT20_subject)).getValue());
                                        boolean $found82 = true;
                                        
                                            while($found82){
                                                $found82 = $matcher81.find();
                                                if($found82){
                                                   $traversalState.setBegin($matcher81.start());
                                                   $traversalState.setEnd($matcher81.end());
                                                   IString $replacement80 = (IString)(((IString)$constants.get(24)/*"\"*/));
                                                   if($isSubtypeOf($replacement80.getType(),$VISIT20_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement80;
                                                   
                                                   } else {
                                                      break VISIT20;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT20_subject.getType(),$T0)){
                                /*muExists*/CASE_0_8: 
                                    do {
                                        final Matcher $matcher84 = (Matcher)$regExpCompile("\\\\\\<", ((IString)($VISIT20_subject)).getValue());
                                        boolean $found85 = true;
                                        
                                            while($found85){
                                                $found85 = $matcher84.find();
                                                if($found85){
                                                   $traversalState.setBegin($matcher84.start());
                                                   $traversalState.setEnd($matcher84.end());
                                                   IString $replacement83 = (IString)(((IString)$constants.get(21)/*"<"*/));
                                                   if($isSubtypeOf($replacement83.getType(),$VISIT20_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement83;
                                                   
                                                   } else {
                                                      break VISIT20;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT20_subject.getType(),$T0)){
                                /*muExists*/CASE_0_9: 
                                    do {
                                        final Matcher $matcher87 = (Matcher)$regExpCompile("\\\\\\>", ((IString)($VISIT20_subject)).getValue());
                                        boolean $found88 = true;
                                        
                                            while($found88){
                                                $found88 = $matcher87.find();
                                                if($found88){
                                                   $traversalState.setBegin($matcher87.start());
                                                   $traversalState.setEnd($matcher87.end());
                                                   IString $replacement86 = (IString)(((IString)$constants.get(20)/*">"*/));
                                                   if($isSubtypeOf($replacement86.getType(),$VISIT20_subject.getType())){
                                                      $traversalState.setMatchedAndChanged(true, true);
                                                      return $replacement86;
                                                   
                                                   } else {
                                                      break VISIT20;// switch
                                                   
                                                   }
                                                }
                                        
                                            }
                                
                                    } while(false);
                             
                             }
            
                     }
                     return $VISIT20_subject;
                 });
            return (IString)$visitResult;
        
        } catch (ReturnFromTraversalException e) {
            return (IString) e.getValue();
        }
    
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `lang::rascal::grammar::definition::Literals`");
    }
}