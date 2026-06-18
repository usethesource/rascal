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
public class $Layout 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.lang.rascal.grammar.definition.$Layout_$I {

    private final $Layout_$I $me;
    private final IList $constants;
    
    
    public final rascal.lang.rascal.syntax.$Rascal M_lang_rascal_syntax_Rascal;
    public final rascal.$ParseTree M_ParseTree;
    public final rascal.$Type M_Type;
    public final rascal.$List M_List;
    public final rascal.$Grammar M_Grammar;
    public final rascal.$Message M_Message;

    
    
    public final io.usethesource.vallang.type.Type $T6;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T3;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T7;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T2;	/*aset(astr())*/
    public final io.usethesource.vallang.type.Type ADT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T8;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Item;	/*aadt("Item",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarModule;	/*aadt("GrammarModule",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T9;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T5;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T11;	/*alist(aadt("Symbol",[],dataSyntax(),alabel="sep"))*/
    public final io.usethesource.vallang.type.Type ADT_Body;	/*aadt("Body",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Body;	/*aadt("Body",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Start;	/*aadt("Start",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Start;	/*aadt("Start",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PrePathChars;	/*aadt("PrePathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PrePathChars;	/*aadt("PrePathChars",[],lexicalSyntax())*/
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
    public final io.usethesource.vallang.type.Type $T1;	/*aset(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T4;	/*aset(aadt("Attr",[],dataSyntax()))*/
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
    public final io.usethesource.vallang.type.Type $T0;	/*alist(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedBackslash;	/*aadt("NamedBackslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedBackslash;	/*aadt("NamedBackslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RascalKeywords;	/*aadt("RascalKeywords",[],keywordSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T10;	/*alist(aadt("Symbol",[],dataSyntax(),alabel="x"))*/
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
    public final io.usethesource.vallang.type.Type ADT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/

    public $Layout(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Layout(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Layout_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.lang.rascal.grammar.definition.$Layout.class, this);
       
       mstore.importModule(rascal.lang.rascal.syntax.$Rascal.class, rex, rascal.lang.rascal.syntax.$Rascal::new);
       mstore.importModule(rascal.$ParseTree.class, rex, rascal.$ParseTree::new);
       mstore.importModule(rascal.$Type.class, rex, rascal.$Type::new);
       mstore.importModule(rascal.$List.class, rex, rascal.$List::new);
       mstore.importModule(rascal.$Grammar.class, rex, rascal.$Grammar::new);
       mstore.importModule(rascal.$Message.class, rex, rascal.$Message::new); 
       
       M_lang_rascal_syntax_Rascal = mstore.getModule(rascal.lang.rascal.syntax.$Rascal.class);
       M_ParseTree = mstore.getModule(rascal.$ParseTree.class);
       M_Type = mstore.getModule(rascal.$Type.class);
       M_List = mstore.getModule(rascal.$List.class);
       M_Grammar = mstore.getModule(rascal.$Grammar.class);
       M_Message = mstore.getModule(rascal.$Message.class); 
       
                          
       
       $TS.importStore(M_lang_rascal_syntax_Rascal.$TS);
       $TS.importStore(M_ParseTree.$TS);
       $TS.importStore(M_Type.$TS);
       $TS.importStore(M_List.$TS);
       $TS.importStore(M_Grammar.$TS);
       $TS.importStore(M_Message.$TS);
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/lang/rascal/grammar/definition/$Layout.constants", 6, "aef30cd4ac2750082470e27f0f9016dc");
       NT_FunctionType = $sort("FunctionType");
       ADT_FunctionType = $adt("FunctionType");
       NT_Replacement = $sort("Replacement");
       ADT_Replacement = $adt("Replacement");
       NT_Visibility = $sort("Visibility");
       ADT_Visibility = $adt("Visibility");
       NT_ProtocolChars = $lex("ProtocolChars");
       ADT_ProtocolChars = $adt("ProtocolChars");
       ADT_Attr = $adt("Attr");
       NT_Expression = $sort("Expression");
       ADT_Expression = $adt("Expression");
       NT_Strategy = $sort("Strategy");
       ADT_Strategy = $adt("Strategy");
       NT_MidStringChars = $lex("MidStringChars");
       ADT_MidStringChars = $adt("MidStringChars");
       ADT_LocationChangeType = $adt("LocationChangeType");
       NT_LAYOUT = $lex("LAYOUT");
       ADT_LAYOUT = $adt("LAYOUT");
       ADT_Tree = $adt("Tree");
       NT_TagString = $lex("TagString");
       ADT_TagString = $adt("TagString");
       ADT_IOCapability = $adt("IOCapability");
       NT_PostProtocolChars = $lex("PostProtocolChars");
       ADT_PostProtocolChars = $adt("PostProtocolChars");
       NT_Concrete = $lex("Concrete");
       ADT_Concrete = $adt("Concrete");
       NT_Pattern = $sort("Pattern");
       ADT_Pattern = $adt("Pattern");
       NT_Range = $sort("Range");
       ADT_Range = $adt("Range");
       NT_Name = $lex("Name");
       ADT_Name = $adt("Name");
       NT_RationalLiteral = $lex("RationalLiteral");
       ADT_RationalLiteral = $adt("RationalLiteral");
       NT_DatePart = $lex("DatePart");
       ADT_DatePart = $adt("DatePart");
       ADT_Item = $adt("Item");
       ADT_GrammarModule = $adt("GrammarModule");
       NT_RegExpLiteral = $lex("RegExpLiteral");
       ADT_RegExpLiteral = $adt("RegExpLiteral");
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
       NT_BooleanLiteral = $lex("BooleanLiteral");
       ADT_BooleanLiteral = $adt("BooleanLiteral");
       NT_Case = $sort("Case");
       ADT_Case = $adt("Case");
       NT_IntegerLiteral = $sort("IntegerLiteral");
       ADT_IntegerLiteral = $adt("IntegerLiteral");
       NT_Declarator = $sort("Declarator");
       ADT_Declarator = $adt("Declarator");
       NT_Target = $sort("Target");
       ADT_Target = $adt("Target");
       NT_TimePartNoTZ = $lex("TimePartNoTZ");
       ADT_TimePartNoTZ = $adt("TimePartNoTZ");
       NT_LocalVariableDeclaration = $sort("LocalVariableDeclaration");
       ADT_LocalVariableDeclaration = $adt("LocalVariableDeclaration");
       NT_KeywordFormals = $sort("KeywordFormals");
       ADT_KeywordFormals = $adt("KeywordFormals");
       NT_DataTarget = $sort("DataTarget");
       ADT_DataTarget = $adt("DataTarget");
       NT_Renaming = $sort("Renaming");
       ADT_Renaming = $adt("Renaming");
       NT_Catch = $sort("Catch");
       ADT_Catch = $adt("Catch");
       ADT_Production = $adt("Production");
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
       NT_TypeArg = $sort("TypeArg");
       ADT_TypeArg = $adt("TypeArg");
       NT_RegExp = $lex("RegExp");
       ADT_RegExp = $adt("RegExp");
       NT_Bound = $sort("Bound");
       ADT_Bound = $adt("Bound");
       NT_PathPart = $sort("PathPart");
       ADT_PathPart = $adt("PathPart");
       NT_Class = $sort("Class");
       ADT_Class = $adt("Class");
       ADT_Associativity = $adt("Associativity");
       NT_Signature = $sort("Signature");
       ADT_Signature = $adt("Signature");
       NT_ModuleActuals = $sort("ModuleActuals");
       ADT_ModuleActuals = $adt("ModuleActuals");
       NT_Tags = $sort("Tags");
       ADT_Tags = $adt("Tags");
       NT_URLChars = $lex("URLChars");
       ADT_URLChars = $adt("URLChars");
       ADT_Symbol = $adt("Symbol");
       ADT_Condition = $adt("Condition");
       NT_Body = $sort("Body");
       ADT_Body = $adt("Body");
       NT_DataTypeSelector = $sort("DataTypeSelector");
       ADT_DataTypeSelector = $adt("DataTypeSelector");
       NT_Start = $sort("Start");
       ADT_Start = $adt("Start");
       NT_OptionalComma = $lex("OptionalComma");
       ADT_OptionalComma = $adt("OptionalComma");
       NT_PrePathChars = $lex("PrePathChars");
       ADT_PrePathChars = $adt("PrePathChars");
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
       NT_FunctionDeclaration = $sort("FunctionDeclaration");
       ADT_FunctionDeclaration = $adt("FunctionDeclaration");
       NT_StringConstant = $lex("StringConstant");
       ADT_StringConstant = $adt("StringConstant");
       NT_TypeVar = $sort("TypeVar");
       ADT_TypeVar = $adt("TypeVar");
       ADT_LocationType = $adt("LocationType");
       NT_JustDate = $lex("JustDate");
       ADT_JustDate = $adt("JustDate");
       NT_UserType = $sort("UserType");
       ADT_UserType = $adt("UserType");
       ADT_CharRange = $adt("CharRange");
       NT_Variant = $sort("Variant");
       ADT_Variant = $adt("Variant");
       NT_Import = $sort("Import");
       ADT_Import = $adt("Import");
       ADT_LAYOUTLIST = $layouts("LAYOUTLIST");
       NT_ConcreteHole = $sort("ConcreteHole");
       ADT_ConcreteHole = $adt("ConcreteHole");
       ADT_Grammar = $adt("Grammar");
       ADT_Message = $adt("Message");
       NT_Sym = $sort("Sym");
       ADT_Sym = $adt("Sym");
       NT_RealLiteral = $lex("RealLiteral");
       ADT_RealLiteral = $adt("RealLiteral");
       NT_StringMiddle = $sort("StringMiddle");
       ADT_StringMiddle = $adt("StringMiddle");
       NT_FunctionModifiers = $sort("FunctionModifiers");
       ADT_FunctionModifiers = $adt("FunctionModifiers");
       NT_Comprehension = $sort("Comprehension");
       ADT_Comprehension = $adt("Comprehension");
       NT_Formals = $sort("Formals");
       ADT_Formals = $adt("Formals");
       NT_MidPathChars = $lex("MidPathChars");
       ADT_MidPathChars = $adt("MidPathChars");
       NT_QualifiedName = $sort("QualifiedName");
       ADT_QualifiedName = $adt("QualifiedName");
       NT_Module = $sort("Module");
       ADT_Module = $adt("Module");
       NT_NamedBackslash = $lex("NamedBackslash");
       ADT_NamedBackslash = $adt("NamedBackslash");
       ADT_RascalKeywords = $keywords("RascalKeywords");
       NT_Parameters = $sort("Parameters");
       ADT_Parameters = $adt("Parameters");
       NT_MidProtocolChars = $lex("MidProtocolChars");
       ADT_MidProtocolChars = $adt("MidProtocolChars");
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
       NT_DateTimeLiteral = $sort("DateTimeLiteral");
       ADT_DateTimeLiteral = $adt("DateTimeLiteral");
       NT_DecimalIntegerLiteral = $lex("DecimalIntegerLiteral");
       ADT_DecimalIntegerLiteral = $adt("DecimalIntegerLiteral");
       NT_Kind = $sort("Kind");
       ADT_Kind = $adt("Kind");
       NT_RegExpModifier = $lex("RegExpModifier");
       ADT_RegExpModifier = $adt("RegExpModifier");
       NT_Label = $sort("Label");
       ADT_Label = $adt("Label");
       NT_PreProtocolChars = $lex("PreProtocolChars");
       ADT_PreProtocolChars = $adt("PreProtocolChars");
       NT_PathTail = $sort("PathTail");
       ADT_PathTail = $adt("PathTail");
       NT_NamedRegExp = $lex("NamedRegExp");
       ADT_NamedRegExp = $adt("NamedRegExp");
       NT_Assoc = $sort("Assoc");
       ADT_Assoc = $adt("Assoc");
       ADT_RuntimeException = $adt("RuntimeException");
       NT_Toplevel = $sort("Toplevel");
       ADT_Toplevel = $adt("Toplevel");
       NT_ConcretePart = $lex("ConcretePart");
       ADT_ConcretePart = $adt("ConcretePart");
       NT_PathChars = $lex("PathChars");
       ADT_PathChars = $adt("PathChars");
       NT_FunctionModifier = $sort("FunctionModifier");
       ADT_FunctionModifier = $adt("FunctionModifier");
       NT_EvalCommand = $sort("EvalCommand");
       ADT_EvalCommand = $adt("EvalCommand");
       NT_StringCharacter = $lex("StringCharacter");
       ADT_StringCharacter = $adt("StringCharacter");
       NT_PostPathChars = $lex("PostPathChars");
       ADT_PostPathChars = $adt("PostPathChars");
       NT_Statement = $sort("Statement");
       ADT_Statement = $adt("Statement");
       NT_Literal = $sort("Literal");
       ADT_Literal = $adt("Literal");
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
       NT_ProdModifier = $sort("ProdModifier");
       ADT_ProdModifier = $adt("ProdModifier");
       NT_JustTime = $lex("JustTime");
       ADT_JustTime = $adt("JustTime");
       NT_UnicodeEscape = $lex("UnicodeEscape");
       ADT_UnicodeEscape = $adt("UnicodeEscape");
       NT_CommonKeywordParameters = $sort("CommonKeywordParameters");
       ADT_CommonKeywordParameters = $adt("CommonKeywordParameters");
       NT_Assignable = $sort("Assignable");
       ADT_Assignable = $adt("Assignable");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       $T6 = $TF.valueType();
       $T3 = $TF.stringType();
       $T7 = $TF.parameterType("T", $T6);
       NT_Mapping_Expression = $parameterizedSort("Mapping", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       $T2 = $TF.setType($T3);
       $T8 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_1 = $parameterizedSort("KeywordArguments", new Type[] { $T8 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       NT_Mapping_Pattern = $parameterizedSort("Mapping", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       $T9 = $TF.parameterType("T", ADT_Tree);
       NT_Mapping_1 = $parameterizedSort("Mapping", new Type[] { $T8 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       NT_KeywordArguments_Expression = $parameterizedSort("KeywordArguments", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       NT_KeywordArguments_Pattern = $parameterizedSort("KeywordArguments", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       $T5 = $TF.listType($T7);
       NT_KeywordArgument_1 = $parameterizedSort("KeywordArgument", new Type[] { $T8 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       $T11 = $TF.listType(ADT_Symbol);
       $T1 = $TF.setType(ADT_Symbol);
       $T4 = $TF.setType(ADT_Attr);
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T8 });
       $T0 = $TF.listType(ADT_Symbol);
       $T10 = $TF.listType(ADT_Symbol);
       ADT_Mapping_Expression = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Expression });
       ADT_KeywordArguments_1 = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { $T8 });
       ADT_Mapping_Pattern = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Pattern });
       ADT_Mapping_1 = $TF.abstractDataType($TS, "Mapping", new Type[] { $T8 });
       ADT_KeywordArguments_Expression = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Expression });
       ADT_KeywordArguments_Pattern = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Pattern });
       ADT_KeywordArgument_1 = $TF.abstractDataType($TS, "KeywordArgument", new Type[] { $T8 });
    
       
       
    }
    public IBool isTypeVar(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isTypeVar($P0);
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
    public IList addLabels(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_Type.addLabels($P0, $P1);
    }
    public IList intermix(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P2Type,$T1)){
         $result = (IList)lang_rascal_grammar_definition_Layout_intermix$06d1dda5e7a58de1((IList) $P0, (IConstructor) $P1, (ISet) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
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
    public IBool sepInOthers(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type,$T1)){
         $result = (IBool)lang_rascal_grammar_definition_Layout_sepInOthers$77348538892e7a01((IConstructor) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isNodeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNodeType($P0);
    }
    public ISet allLayouts(IValue $P0, IValue $P1){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T2) && $isSubtypeOf($P1Type, M_Grammar.ADT_GrammarDefinition)){
         $result = (ISet)lang_rascal_grammar_definition_Layout_allLayouts$88ae4a4dba60dc36((ISet) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isReifiedType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isReifiedType($P0);
    }
    public IBool isRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRelType($P0);
    }
    public INode layouts(IValue $P0){ // Generated by Resolver
       INode $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_GrammarDefinition)){
         $result = (INode)lang_rascal_grammar_definition_Layout_layouts$a6e96418ea183c23((IConstructor) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T3)){
         return $RVF.constructor(M_ParseTree.Symbol_layouts_str, new IValue[]{(IString) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor layouts(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P2Type,$T1)){
         $result = (IConstructor)lang_rascal_grammar_definition_Layout_layouts$05fdb6399102471a((IConstructor) $P0, (IConstructor) $P1, (ISet) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
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
    public IBool lang_rascal_grammar_definition_Layout_activeLayout$74d077129237df15_isManual(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T4)){
         $result = (IBool)lang_rascal_grammar_definition_Layout_isManual$a3dc33f0f7e5db9f((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor activeLayout(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type,$T3) && $isSubtypeOf($P1Type,$T2) && $isSubtypeOf($P2Type, M_Grammar.ADT_GrammarDefinition)){
         $result = (IConstructor)lang_rascal_grammar_definition_Layout_activeLayout$74d077129237df15((IString) $P0, (ISet) $P1, (IConstructor) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
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
    public IConstructor regulars(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P2Type,$T1)){
         $result = (IConstructor)lang_rascal_grammar_definition_Layout_regulars$602edae583c47845((IConstructor) $P0, (IConstructor) $P1, (ISet) $P2);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
    }
    public IBool lang_rascal_grammar_definition_Layout_activeLayout$74d077129237df15_isDefault(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IBool)lang_rascal_grammar_definition_Layout_isDefault$bd3ab635f3493f69((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
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
    public IValue sort(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T5)){
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
    public IBool isIntType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isIntType($P0);
    }
    public IBool isDateTimeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isDateTimeType($P0);
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Layout.rsc|(465,446,<15,0>,<26,1>) 
    public IConstructor lang_rascal_grammar_definition_Layout_layouts$a6e96418ea183c23(IConstructor def_0){ 
        
        
        ISet deps_1 = ((ISet)(M_Grammar.dependencies(((IConstructor)def_0))));
        /*muExists*/FOR0: 
            do {
                FOR0_GEN665:
                for(IValue $elem0_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)def_0), "modules")))))){
                    IString $elem0 = (IString) $elem0_for;
                    IString name_2 = ((IString)($elem0));
                    def_0 = ((IConstructor)(((IConstructor)($aadt_field_update("modules", $amap_update(name_2,((IConstructor)($aadt_field_update("grammar", $me.layouts(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_0), "modules"))))),((IString)name_2)))), "grammar"))))), ((IConstructor)($me.activeLayout(((IString)name_2), ((ISet)($arel_subscript1_noset(((ISet)deps_1),((IString)name_2)))), ((IConstructor)def_0)))), ((ISet)($me.allLayouts(((ISet)($aset_add_aset(((ISet)($arel_subscript1_noset(((ISet)deps_1),((IString)name_2)))),((ISet)($RVF.set(((IString)name_2))))))), ((IConstructor)def_0))))), ((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_0), "modules"))))),((IString)name_2))))))), ((IMap)(((IMap)($aadt_get_field(((IConstructor)def_0), "modules")))))), ((IConstructor)def_0))))));
                
                }
                continue FOR0;
                            
            } while(false);
        /* void:  muCon([]) */return ((IConstructor)def_0);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Layout.rsc|(913,371,<28,0>,<32,3>) 
    public ISet lang_rascal_grammar_definition_Layout_allLayouts$88ae4a4dba60dc36(ISet defs_0, IConstructor def_1){ 
        
        
        final ISetWriter $setwriter1 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP2_GEN1109:
        for(IValue $elem9_for : ((ISet)defs_0)){
            IString $elem9 = (IString) $elem9_for;
            IString m_2 = null;
            if($is_defined_value($guarded_map_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_1), "modules"))))),((IString)($elem9))))){
              final IConstructor $subject_val3 = ((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_1), "modules"))))),((IString)($elem9)))));
              $SCOMP2_GEN1109_DESC1137:
              for(IValue $elem4 : new DescendantMatchIterator($subject_val3, 
                  new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                  if($isComparable($elem4.getType(), M_ParseTree.ADT_Production)){
                     if($has_type_and_arity($elem4, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                        IValue $arg0_7 = (IValue)($subscript_int(((IValue)($elem4)),0));
                        if($isComparable($arg0_7.getType(), M_ParseTree.ADT_Symbol)){
                           if($has_type_and_arity($arg0_7, M_ParseTree.Symbol_layouts_str, 1)){
                              IValue $arg0_8 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_7)),0));
                              if($isComparable($arg0_8.getType(), $T3)){
                                 if(true){
                                    IString l_3 = null;
                                    IValue $arg1_6 = (IValue)($subscript_int(((IValue)($elem4)),1));
                                    if($isComparable($arg1_6.getType(), $T6)){
                                       IValue $arg2_5 = (IValue)($subscript_int(((IValue)($elem4)),2));
                                       if($isComparable($arg2_5.getType(), $T6)){
                                          $setwriter1.insert($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)($arg0_8))}));
                                       
                                       } else {
                                          continue $SCOMP2_GEN1109_DESC1137;
                                       }
                                    } else {
                                       continue $SCOMP2_GEN1109_DESC1137;
                                    }
                                 } else {
                                    continue $SCOMP2_GEN1109_DESC1137;
                                 }
                              } else {
                                 continue $SCOMP2_GEN1109_DESC1137;
                              }
                           } else {
                              continue $SCOMP2_GEN1109_DESC1137;
                           }
                        } else {
                           continue $SCOMP2_GEN1109_DESC1137;
                        }
                     } else {
                        continue $SCOMP2_GEN1109_DESC1137;
                     }
                  } else {
                     continue $SCOMP2_GEN1109_DESC1137;
                  }
              }
              continue $SCOMP2_GEN1109;
                           
            } else {
              continue $SCOMP2_GEN1109;
            }
        
        }
        
                    final ISetWriter $setwriter10 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP11_GEN1198:
        for(IValue $elem20_for : ((ISet)defs_0)){
            IString $elem20 = (IString) $elem20_for;
            IString m_4 = null;
            if($is_defined_value($guarded_map_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_1), "modules"))))),((IString)($elem20))))){
              final IConstructor $subject_val12 = ((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_1), "modules"))))),((IString)($elem20)))));
              $SCOMP11_GEN1198_DESC1226:
              for(IValue $elem13 : new DescendantMatchIterator($subject_val12, 
                  new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                  if($isComparable($elem13.getType(), M_ParseTree.ADT_Production)){
                     if($has_type_and_arity($elem13, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                        IValue $arg0_16 = (IValue)($subscript_int(((IValue)($elem13)),0));
                        if($isComparable($arg0_16.getType(), M_ParseTree.ADT_Symbol)){
                           if($has_type_and_arity($arg0_16, M_Type.Symbol_label_str_Symbol, 2)){
                              IValue $arg0_19 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_16)),0));
                              if($isComparable($arg0_19.getType(), $T6)){
                                 IValue $arg1_17 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_16)),1));
                                 if($isComparable($arg1_17.getType(), M_ParseTree.ADT_Symbol)){
                                    if($has_type_and_arity($arg1_17, M_ParseTree.Symbol_layouts_str, 1)){
                                       IValue $arg0_18 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_17)),0));
                                       if($isComparable($arg0_18.getType(), $T3)){
                                          if(true){
                                             IString l_5 = null;
                                             IValue $arg1_15 = (IValue)($subscript_int(((IValue)($elem13)),1));
                                             if($isComparable($arg1_15.getType(), $T6)){
                                                IValue $arg2_14 = (IValue)($subscript_int(((IValue)($elem13)),2));
                                                if($isComparable($arg2_14.getType(), $T6)){
                                                   $setwriter10.insert($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)($arg0_18))}));
                                                
                                                } else {
                                                   continue $SCOMP11_GEN1198_DESC1226;
                                                }
                                             } else {
                                                continue $SCOMP11_GEN1198_DESC1226;
                                             }
                                          } else {
                                             continue $SCOMP11_GEN1198_DESC1226;
                                          }
                                       } else {
                                          continue $SCOMP11_GEN1198_DESC1226;
                                       }
                                    } else {
                                       continue $SCOMP11_GEN1198_DESC1226;
                                    }
                                 } else {
                                    continue $SCOMP11_GEN1198_DESC1226;
                                 }
                              } else {
                                 continue $SCOMP11_GEN1198_DESC1226;
                              }
                           } else {
                              continue $SCOMP11_GEN1198_DESC1226;
                           }
                        } else {
                           continue $SCOMP11_GEN1198_DESC1226;
                        }
                     } else {
                        continue $SCOMP11_GEN1198_DESC1226;
                     }
                  } else {
                     continue $SCOMP11_GEN1198_DESC1226;
                  }
              }
              continue $SCOMP11_GEN1198;
                           
            } else {
              continue $SCOMP11_GEN1198;
            }
        
        }
        
                    return ((ISet)($aset_add_aset(((ISet)($setwriter1.done())),((ISet)($setwriter10.done())))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Layout.rsc|(2252,61,<47,4>,<47,65>) 
    public IBool lang_rascal_grammar_definition_Layout_isManual$a3dc33f0f7e5db9f(ISet as_0){ 
        
        
        return ((IBool)($RVF.bool(((ISet)as_0).contains(((IConstructor)($RVF.constructor(M_Type.Attr_tag_value, new IValue[]{((INode)$constants.get(0)/*"manual"()*/)})))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Layout.rsc|(2318,55,<48,4>,<48,59>) 
    public IBool lang_rascal_grammar_definition_Layout_isDefault$bd3ab635f3493f69(IConstructor s_0){ 
        
        
        return ((IBool)($equal(((IConstructor)s_0), ((IConstructor)$constants.get(1)/*layouts("$default$")*/))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Layout.rsc|(1619,1282,<40,0>,<60,1>) 
    public IConstructor lang_rascal_grammar_definition_Layout_activeLayout$74d077129237df15(IString name_0, ISet deps_1, IConstructor def_2){ 
        
        
        /*muExists*/IF4: 
            do {
                final IConstructor $subject_val45 = ((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_2), "modules"))))),((IString)name_0))));
                IF4_DESC2383:
                for(IValue $elem46 : new DescendantMatchIterator($subject_val45, 
                    new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                    if($isComparable($elem46.getType(), M_ParseTree.ADT_Production)){
                       if($has_type_and_arity($elem46, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                          IValue $arg0_49 = (IValue)($subscript_int(((IValue)($elem46)),0));
                          if($isComparable($arg0_49.getType(), M_ParseTree.ADT_Symbol)){
                             if($has_type_and_arity($arg0_49, M_ParseTree.Symbol_layouts_str, 1)){
                                IValue $arg0_50 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_49)),0));
                                if($isComparable($arg0_50.getType(), $T6)){
                                   IConstructor l_3 = ((IConstructor)($arg0_49));
                                   IValue $arg1_48 = (IValue)($subscript_int(((IValue)($elem46)),1));
                                   if($isComparable($arg1_48.getType(), $T6)){
                                      IValue $arg2_47 = (IValue)($subscript_int(((IValue)($elem46)),2));
                                      if($isComparable($arg2_47.getType(), $T4)){
                                         ISet as_4 = ((ISet)($arg2_47));
                                         if((((IBool)(lang_rascal_grammar_definition_Layout_activeLayout$74d077129237df15_isDefault(((IConstructor)($arg0_49)))))).getValue()){
                                            continue IF4_DESC2383;
                                         } else {
                                            if((((IBool)(lang_rascal_grammar_definition_Layout_activeLayout$74d077129237df15_isManual(((ISet)($arg2_47)))))).getValue()){
                                               continue IF4_DESC2383;
                                            } else {
                                               return ((IConstructor)($arg0_49));
                                            
                                            }
                                         }
                                      } else {
                                         continue IF4_DESC2383;
                                      }
                                   } else {
                                      continue IF4_DESC2383;
                                   }
                                } else {
                                   continue IF4_DESC2383;
                                }
                             } else {
                                continue IF4_DESC2383;
                             }
                          } else {
                             continue IF4_DESC2383;
                          }
                       } else {
                          continue IF4_DESC2383;
                       }
                    } else {
                       continue IF4_DESC2383;
                    }
                }
                
                             
            } while(false);
        /*muExists*/IF3: 
            do {
                final IConstructor $subject_val37 = ((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_2), "modules"))))),((IString)name_0))));
                IF3_DESC2486:
                for(IValue $elem38 : new DescendantMatchIterator($subject_val37, 
                    new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                    if($isComparable($elem38.getType(), M_ParseTree.ADT_Production)){
                       if($has_type_and_arity($elem38, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                          IValue $arg0_41 = (IValue)($subscript_int(((IValue)($elem38)),0));
                          if($isComparable($arg0_41.getType(), M_ParseTree.ADT_Symbol)){
                             if($has_type_and_arity($arg0_41, M_Type.Symbol_label_str_Symbol, 2)){
                                IValue $arg0_44 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_41)),0));
                                if($isComparable($arg0_44.getType(), $T6)){
                                   IValue $arg1_42 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_41)),1));
                                   if($isComparable($arg1_42.getType(), M_ParseTree.ADT_Symbol)){
                                      if($has_type_and_arity($arg1_42, M_ParseTree.Symbol_layouts_str, 1)){
                                         IValue $arg0_43 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_42)),0));
                                         if($isComparable($arg0_43.getType(), $T6)){
                                            IConstructor l_5 = ((IConstructor)($arg1_42));
                                            IValue $arg1_40 = (IValue)($subscript_int(((IValue)($elem38)),1));
                                            if($isComparable($arg1_40.getType(), $T6)){
                                               IValue $arg2_39 = (IValue)($subscript_int(((IValue)($elem38)),2));
                                               if($isComparable($arg2_39.getType(), $T4)){
                                                  ISet as_6 = ((ISet)($arg2_39));
                                                  if((((IBool)(lang_rascal_grammar_definition_Layout_activeLayout$74d077129237df15_isDefault(((IConstructor)($arg1_42)))))).getValue()){
                                                     continue IF3_DESC2486;
                                                  } else {
                                                     if((((IBool)(lang_rascal_grammar_definition_Layout_activeLayout$74d077129237df15_isManual(((ISet)($arg2_39)))))).getValue()){
                                                        continue IF3_DESC2486;
                                                     } else {
                                                        return ((IConstructor)($arg1_42));
                                                     
                                                     }
                                                  }
                                               } else {
                                                  continue IF3_DESC2486;
                                               }
                                            } else {
                                               continue IF3_DESC2486;
                                            }
                                         } else {
                                            continue IF3_DESC2486;
                                         }
                                      } else {
                                         continue IF3_DESC2486;
                                      }
                                   } else {
                                      continue IF3_DESC2486;
                                   }
                                } else {
                                   continue IF3_DESC2486;
                                }
                             } else {
                                continue IF3_DESC2486;
                             }
                          } else {
                             continue IF3_DESC2486;
                          }
                       } else {
                          continue IF3_DESC2486;
                       }
                    } else {
                       continue IF3_DESC2486;
                    }
                }
                
                             
            } while(false);
        /*muExists*/IF2: 
            do {
                IF2_GEN2600:
                for(IValue $elem36_for : ((ISet)deps_1)){
                    IString $elem36 = (IString) $elem36_for;
                    IString i_7 = ((IString)($elem36));
                    if($is_defined_value($guarded_map_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_2), "modules"))))),((IString)i_7)))){
                       final IConstructor $subject_val30 = ((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_2), "modules"))))),((IString)i_7))));
                       IF2_GEN2600_DESC2628:
                       for(IValue $elem31 : new DescendantMatchIterator($subject_val30, 
                           new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                           if($isComparable($elem31.getType(), M_ParseTree.ADT_Production)){
                              if($has_type_and_arity($elem31, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                 IValue $arg0_34 = (IValue)($subscript_int(((IValue)($elem31)),0));
                                 if($isComparable($arg0_34.getType(), M_ParseTree.ADT_Symbol)){
                                    if($has_type_and_arity($arg0_34, M_ParseTree.Symbol_layouts_str, 1)){
                                       IValue $arg0_35 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_34)),0));
                                       if($isComparable($arg0_35.getType(), $T6)){
                                          IConstructor l_8 = ((IConstructor)($arg0_34));
                                          IValue $arg1_33 = (IValue)($subscript_int(((IValue)($elem31)),1));
                                          if($isComparable($arg1_33.getType(), $T6)){
                                             IValue $arg2_32 = (IValue)($subscript_int(((IValue)($elem31)),2));
                                             if($isComparable($arg2_32.getType(), $T4)){
                                                ISet as_9 = ((ISet)($arg2_32));
                                                if((((IBool)(lang_rascal_grammar_definition_Layout_activeLayout$74d077129237df15_isDefault(((IConstructor)($arg0_34)))))).getValue()){
                                                   continue IF2_GEN2600_DESC2628;
                                                } else {
                                                   if((((IBool)(lang_rascal_grammar_definition_Layout_activeLayout$74d077129237df15_isManual(((ISet)($arg2_32)))))).getValue()){
                                                      continue IF2_GEN2600_DESC2628;
                                                   } else {
                                                      return ((IConstructor)($arg0_34));
                                                   
                                                   }
                                                }
                                             } else {
                                                continue IF2_GEN2600_DESC2628;
                                             }
                                          } else {
                                             continue IF2_GEN2600_DESC2628;
                                          }
                                       } else {
                                          continue IF2_GEN2600_DESC2628;
                                       }
                                    } else {
                                       continue IF2_GEN2600_DESC2628;
                                    }
                                 } else {
                                    continue IF2_GEN2600_DESC2628;
                                 }
                              } else {
                                 continue IF2_GEN2600_DESC2628;
                              }
                           } else {
                              continue IF2_GEN2600_DESC2628;
                           }
                       }
                       continue IF2_GEN2600;
                                    
                    } else {
                       continue IF2_GEN2600;
                    }
                }
                
                            
            } while(false);
        /*muExists*/IF1: 
            do {
                IF1_GEN2729:
                for(IValue $elem29_for : ((ISet)deps_1)){
                    IString $elem29 = (IString) $elem29_for;
                    IString i_10 = ((IString)($elem29));
                    if($is_defined_value($guarded_map_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_2), "modules"))))),((IString)i_10)))){
                       final IConstructor $subject_val21 = ((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)def_2), "modules"))))),((IString)i_10))));
                       IF1_GEN2729_DESC2758:
                       for(IValue $elem22 : new DescendantMatchIterator($subject_val21, 
                           new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                           if($isComparable($elem22.getType(), M_ParseTree.ADT_Production)){
                              if($has_type_and_arity($elem22, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                 IValue $arg0_25 = (IValue)($subscript_int(((IValue)($elem22)),0));
                                 if($isComparable($arg0_25.getType(), M_ParseTree.ADT_Symbol)){
                                    if($has_type_and_arity($arg0_25, M_Type.Symbol_label_str_Symbol, 2)){
                                       IValue $arg0_28 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_25)),0));
                                       if($isComparable($arg0_28.getType(), $T6)){
                                          IValue $arg1_26 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_25)),1));
                                          if($isComparable($arg1_26.getType(), M_ParseTree.ADT_Symbol)){
                                             if($has_type_and_arity($arg1_26, M_ParseTree.Symbol_layouts_str, 1)){
                                                IValue $arg0_27 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_26)),0));
                                                if($isComparable($arg0_27.getType(), $T6)){
                                                   IConstructor l_11 = ((IConstructor)($arg1_26));
                                                   IValue $arg1_24 = (IValue)($subscript_int(((IValue)($elem22)),1));
                                                   if($isComparable($arg1_24.getType(), $T6)){
                                                      IValue $arg2_23 = (IValue)($subscript_int(((IValue)($elem22)),2));
                                                      if($isComparable($arg2_23.getType(), $T4)){
                                                         ISet as_12 = ((ISet)($arg2_23));
                                                         if((((IBool)(lang_rascal_grammar_definition_Layout_activeLayout$74d077129237df15_isDefault(((IConstructor)($arg1_26)))))).getValue()){
                                                            continue IF1_GEN2729_DESC2758;
                                                         } else {
                                                            if((((IBool)(lang_rascal_grammar_definition_Layout_activeLayout$74d077129237df15_isManual(((ISet)($arg2_23)))))).getValue()){
                                                               continue IF1_GEN2729_DESC2758;
                                                            } else {
                                                               return ((IConstructor)($arg1_26));
                                                            
                                                            }
                                                         }
                                                      } else {
                                                         continue IF1_GEN2729_DESC2758;
                                                      }
                                                   } else {
                                                      continue IF1_GEN2729_DESC2758;
                                                   }
                                                } else {
                                                   continue IF1_GEN2729_DESC2758;
                                                }
                                             } else {
                                                continue IF1_GEN2729_DESC2758;
                                             }
                                          } else {
                                             continue IF1_GEN2729_DESC2758;
                                          }
                                       } else {
                                          continue IF1_GEN2729_DESC2758;
                                       }
                                    } else {
                                       continue IF1_GEN2729_DESC2758;
                                    }
                                 } else {
                                    continue IF1_GEN2729_DESC2758;
                                 }
                              } else {
                                 continue IF1_GEN2729_DESC2758;
                              }
                           } else {
                              continue IF1_GEN2729_DESC2758;
                           }
                       }
                       continue IF1_GEN2729;
                                    
                    } else {
                       continue IF1_GEN2729;
                    }
                }
                
                            
            } while(false);
        return ((IConstructor)$constants.get(1)/*layouts("$default$")*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Layout.rsc|(2905,708,<62,0>,<71,1>) 
    public IConstructor lang_rascal_grammar_definition_Layout_layouts$05fdb6399102471a(IConstructor g_0, IConstructor $aux_l_1, ISet $aux_others_2){ 
        ValueRef<IConstructor> l_1 = new ValueRef<IConstructor>("l_1", $aux_l_1);
        ValueRef<ISet> others_2 = new ValueRef<ISet>("others_2", $aux_others_2);
    
        
        try {
            IValue $visitResult = $TRAVERSE.traverse(DIRECTION.TopDown, PROGRESS.Breaking, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptorAlwaysTrue($RVF.bool(false)),
                 g_0,
                 (IVisitFunction) (IValue $VISIT5_subject, TraversalState $traversalState) -> {
                     VISIT5:switch(Fingerprint.getFingerprint($VISIT5_subject)){
                     
                         case 110389984:
                             if($isSubtypeOf($VISIT5_subject.getType(),M_ParseTree.ADT_Production)){
                                /*muExists*/CASE_110389984_0: 
                                    do {
                                        if($has_type_and_arity($VISIT5_subject, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                           IValue $arg0_55 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),0));
                                           if($isComparable($arg0_55.getType(), M_ParseTree.ADT_Symbol)){
                                              if($has_type_and_arity($arg0_55, M_ParseTree.Symbol_start_Symbol, 1)){
                                                 IValue $arg0_56 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_55)),0));
                                                 if($isComparable($arg0_56.getType(), M_ParseTree.ADT_Symbol)){
                                                    ValueRef<IConstructor> y_3 = new ValueRef<IConstructor>();
                                                    IValue $arg1_53 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),1));
                                                    if($isComparable($arg1_53.getType(), $T10)){
                                                       final IList $subject54 = ((IList)($arg1_53));
                                                       int $subject54_cursor = 0;
                                                       if($isSubtypeOf($subject54.getType(),$T10)){
                                                          final int $subject54_len = (int)((IList)($subject54)).length();
                                                          if($subject54_len == 1){
                                                             if($subject54_cursor < $subject54_len){
                                                                IConstructor x_4 = ((IConstructor)($alist_subscript_int(((IList)($subject54)),$subject54_cursor)));
                                                                $subject54_cursor += 1;
                                                                if($subject54_cursor == $subject54_len){
                                                                   IValue $arg2_52 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),2));
                                                                   if($isComparable($arg2_52.getType(), $T4)){
                                                                      ValueRef<ISet> as_5 = new ValueRef<ISet>();
                                                                      IConstructor $replacement51 = (IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_start_Symbol, new IValue[]{((IConstructor)($arg0_56))}))), ((IList)($RVF.list(l_1.getValue(), x_4, l_1.getValue()))), ((ISet)($arg2_52))}));
                                                                      if($isSubtypeOf($replacement51.getType(),$VISIT5_subject.getType())){
                                                                         $traversalState.setMatchedAndChanged(true, true);
                                                                         return $replacement51;
                                                                      
                                                                      } else {
                                                                         break VISIT5;// switch
                                                                      
                                                                      }
                                                                   }
                                                                
                                                                } else {
                                                                   continue CASE_110389984_0;/*list match1*/
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
                             if($isSubtypeOf($VISIT5_subject.getType(),M_ParseTree.ADT_Production)){
                                /*muExists*/CASE_110389984_1: 
                                    do {
                                        if($has_type_and_arity($VISIT5_subject, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                           IValue $arg0_60 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),0));
                                           if($isComparable($arg0_60.getType(), M_ParseTree.ADT_Symbol)){
                                              if($has_type_and_arity($arg0_60, M_ParseTree.Symbol_sort_str, 1)){
                                                 IValue $arg0_61 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_60)),0));
                                                 if($isComparable($arg0_61.getType(), $T3)){
                                                    IString s_6 = ((IString)($arg0_61));
                                                    IValue $arg1_59 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),1));
                                                    if($isComparable($arg1_59.getType(), $T0)){
                                                       IList lhs_7 = ((IList)($arg1_59));
                                                       IValue $arg2_58 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),2));
                                                       if($isComparable($arg2_58.getType(), $T4)){
                                                          ISet as_8 = ((ISet)($arg2_58));
                                                          IConstructor $replacement57 = (IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)($arg0_61))}))), ((IList)($me.intermix(((IList)($arg1_59)), l_1.getValue(), others_2.getValue()))), ((ISet)($arg2_58))}));
                                                          if($isSubtypeOf($replacement57.getType(),$VISIT5_subject.getType())){
                                                             $traversalState.setMatchedAndChanged(true, true);
                                                             return $replacement57;
                                                          
                                                          } else {
                                                             break VISIT5;// switch
                                                          
                                                          }
                                                       }
                                                    
                                                    }
                                                 
                                                 }
                                              
                                              }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT5_subject.getType(),M_ParseTree.ADT_Production)){
                                /*muExists*/CASE_110389984_2: 
                                    do {
                                        if($has_type_and_arity($VISIT5_subject, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                           IValue $arg0_65 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),0));
                                           if($isComparable($arg0_65.getType(), M_ParseTree.ADT_Symbol)){
                                              if($has_type_and_arity($arg0_65, M_ParseTree.Symbol_parameterized_sort_str_list_Symbol, 2)){
                                                 IValue $arg0_67 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_65)),0));
                                                 if($isComparable($arg0_67.getType(), $T3)){
                                                    IString s_9 = ((IString)($arg0_67));
                                                    IValue $arg1_66 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_65)),1));
                                                    if($isComparable($arg1_66.getType(), $T0)){
                                                       IList n_10 = ((IList)($arg1_66));
                                                       IValue $arg1_64 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),1));
                                                       if($isComparable($arg1_64.getType(), $T0)){
                                                          IList lhs_11 = ((IList)($arg1_64));
                                                          IValue $arg2_63 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),2));
                                                          if($isComparable($arg2_63.getType(), $T4)){
                                                             ISet as_12 = ((ISet)($arg2_63));
                                                             IConstructor $replacement62 = (IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_parameterized_sort_str_list_Symbol, new IValue[]{((IString)($arg0_67)), ((IList)($arg1_66))}))), ((IList)($me.intermix(((IList)($arg1_64)), l_1.getValue(), others_2.getValue()))), ((ISet)($arg2_63))}));
                                                             if($isSubtypeOf($replacement62.getType(),$VISIT5_subject.getType())){
                                                                $traversalState.setMatchedAndChanged(true, true);
                                                                return $replacement62;
                                                             
                                                             } else {
                                                                break VISIT5;// switch
                                                             
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
                             if($isSubtypeOf($VISIT5_subject.getType(),M_ParseTree.ADT_Production)){
                                /*muExists*/CASE_110389984_3: 
                                    do {
                                        if($has_type_and_arity($VISIT5_subject, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                           IValue $arg0_71 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),0));
                                           if($isComparable($arg0_71.getType(), M_ParseTree.ADT_Symbol)){
                                              if($has_type_and_arity($arg0_71, M_Type.Symbol_label_str_Symbol, 2)){
                                                 IValue $arg0_74 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_71)),0));
                                                 if($isComparable($arg0_74.getType(), $T3)){
                                                    IString t_13 = ((IString)($arg0_74));
                                                    IValue $arg1_72 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_71)),1));
                                                    if($isComparable($arg1_72.getType(), M_ParseTree.ADT_Symbol)){
                                                       if($has_type_and_arity($arg1_72, M_ParseTree.Symbol_sort_str, 1)){
                                                          IValue $arg0_73 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_72)),0));
                                                          if($isComparable($arg0_73.getType(), $T3)){
                                                             IString s_14 = ((IString)($arg0_73));
                                                             IValue $arg1_70 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),1));
                                                             if($isComparable($arg1_70.getType(), $T0)){
                                                                IList lhs_15 = ((IList)($arg1_70));
                                                                IValue $arg2_69 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),2));
                                                                if($isComparable($arg2_69.getType(), $T4)){
                                                                   ISet as_16 = ((ISet)($arg2_69));
                                                                   IConstructor $replacement68 = (IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)($arg0_74)), ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)($arg0_73))})))}))), ((IList)($me.intermix(((IList)($arg1_70)), l_1.getValue(), others_2.getValue()))), ((ISet)($arg2_69))}));
                                                                   if($isSubtypeOf($replacement68.getType(),$VISIT5_subject.getType())){
                                                                      $traversalState.setMatchedAndChanged(true, true);
                                                                      return $replacement68;
                                                                   
                                                                   } else {
                                                                      break VISIT5;// switch
                                                                   
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
                             if($isSubtypeOf($VISIT5_subject.getType(),M_ParseTree.ADT_Production)){
                                /*muExists*/CASE_110389984_4: 
                                    do {
                                        if($has_type_and_arity($VISIT5_subject, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                           IValue $arg0_78 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),0));
                                           if($isComparable($arg0_78.getType(), M_ParseTree.ADT_Symbol)){
                                              if($has_type_and_arity($arg0_78, M_Type.Symbol_label_str_Symbol, 2)){
                                                 IValue $arg0_82 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_78)),0));
                                                 if($isComparable($arg0_82.getType(), $T3)){
                                                    IString t_17 = ((IString)($arg0_82));
                                                    IValue $arg1_79 = (IValue)($aadt_subscript_int(((IConstructor)($arg0_78)),1));
                                                    if($isComparable($arg1_79.getType(), M_ParseTree.ADT_Symbol)){
                                                       if($has_type_and_arity($arg1_79, M_ParseTree.Symbol_parameterized_sort_str_list_Symbol, 2)){
                                                          IValue $arg0_81 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_79)),0));
                                                          if($isComparable($arg0_81.getType(), $T3)){
                                                             IString s_18 = ((IString)($arg0_81));
                                                             IValue $arg1_80 = (IValue)($aadt_subscript_int(((IConstructor)($arg1_79)),1));
                                                             if($isComparable($arg1_80.getType(), $T0)){
                                                                IList n_19 = ((IList)($arg1_80));
                                                                IValue $arg1_77 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),1));
                                                                if($isComparable($arg1_77.getType(), $T0)){
                                                                   IList lhs_20 = ((IList)($arg1_77));
                                                                   IValue $arg2_76 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT5_subject)),2));
                                                                   if($isComparable($arg2_76.getType(), $T4)){
                                                                      ISet as_21 = ((ISet)($arg2_76));
                                                                      IConstructor $replacement75 = (IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)($arg0_82)), ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_parameterized_sort_str_list_Symbol, new IValue[]{((IString)($arg0_81)), ((IList)($arg1_80))})))}))), ((IList)($me.intermix(((IList)($arg1_77)), l_1.getValue(), others_2.getValue()))), ((ISet)($arg2_76))}));
                                                                      if($isSubtypeOf($replacement75.getType(),$VISIT5_subject.getType())){
                                                                         $traversalState.setMatchedAndChanged(true, true);
                                                                         return $replacement75;
                                                                      
                                                                      } else {
                                                                         break VISIT5;// switch
                                                                      
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
                     return $VISIT5_subject;
                 });
            return (IConstructor)$visitResult;
        
        } catch (ReturnFromTraversalException e) {
            return (IConstructor) e.getValue();
        }
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Layout.rsc|(3616,540,<73,0>,<87,1>) 
    public IList lang_rascal_grammar_definition_Layout_intermix$06d1dda5e7a58de1(IList syms_0, IConstructor l_1, ISet others_2){ 
        
        
        if((((IBool)($equal(((IList)syms_0), ((IList)$constants.get(2)/*[]*/))))).getValue()){
           return ((IList)syms_0);
        
        }
        final IListWriter $listwriter83 = (IListWriter)$RVF.listWriter();
        $LCOMP84_GEN3791:
        for(IValue $elem86_for : ((IList)syms_0)){
            IConstructor $elem86 = (IConstructor) $elem86_for;
            IConstructor sym_3 = ((IConstructor)($elem86));
            $listwriter83.append(($is(((IConstructor)sym_3),((IString)$constants.get(3)/*"layouts"*/)) ? sym_3 : $me.regulars(((IConstructor)sym_3), ((IConstructor)l_1), ((ISet)others_2))));
        
        }
        
                    syms_0 = ((IList)($listwriter83.done()));
        others_2 = ((ISet)($aset_add_aset(((ISet)others_2),((ISet)($RVF.set(((IConstructor)l_1)))))));
        WHILE7:
        //WHILE7_BT:
            while(true){
            WHILE7_BT: 
               do {
                 final IList $subject89 = ((IList)syms_0);
                 int $subject89_cursor = 0;
                 if($isSubtypeOf($subject89.getType(),$T0)){
                    final int $subject89_len = (int)((IList)($subject89)).length();
                    if($subject89_len >= 2){
                       final int $pre_491_start = (int)$subject89_cursor;
                       WHILE7_BT_LIST_MVARpre:
                       
                       for(int $pre_491_len = 0; $pre_491_len <= $subject89_len - $pre_491_start - 2; $pre_491_len += 1){
                          IList pre_4 = ((IList)($subject89.sublist($pre_491_start, $pre_491_len)));
                          $subject89_cursor = $pre_491_start + $pre_491_len;
                          if($subject89_cursor < $subject89_len){
                             IConstructor sym1_5 = ((IConstructor)($alist_subscript_int(((IList)($subject89)),$subject89_cursor)));
                             $subject89_cursor += 1;
                             if($subject89_cursor < $subject89_len){
                                IConstructor sym2_6 = ((IConstructor)($alist_subscript_int(((IList)($subject89)),$subject89_cursor)));
                                $subject89_cursor += 1;
                                final int $pst_790_start = (int)$subject89_cursor;
                                WHILE7_BT_LIST_MVARpre_VARsym1_VARsym2_MVARpst:
                                
                                for(int $pst_790_len = 0; $pst_790_len <= $subject89_len - $pst_790_start - 0; $pst_790_len += 1){
                                   IList pst_7 = ((IList)($subject89.sublist($pst_790_start, $pst_790_len)));
                                   $subject89_cursor = $pst_790_start + $pst_790_len;
                                   if($subject89_cursor == $subject89_len){
                                      if((((IBool)($RVF.bool(((ISet)others_2).contains(((IConstructor)sym1_5)))))).getValue()){
                                         continue WHILE7_BT_LIST_MVARpre_VARsym1_VARsym2_MVARpst;
                                      } else {
                                         if((((IBool)($RVF.bool(((ISet)others_2).contains(((IConstructor)sym2_6)))))).getValue()){
                                            continue WHILE7_BT_LIST_MVARpre_VARsym1_VARsym2_MVARpst;
                                         } else {
                                            final IListWriter $writer87 = (IListWriter)$RVF.listWriter();
                                            $listwriter_splice($writer87,pre_4);
                                            $writer87.append(sym1_5);
                                            $writer87.append(l_1);
                                            $writer87.append(sym2_6);
                                            $listwriter_splice($writer87,pst_7);
                                            syms_0 = ((IList)($writer87.done()));
                                            continue WHILE7;
                                         
                                         }
                                      }
                                   } else {
                                      continue WHILE7_BT_LIST_MVARpre_VARsym1_VARsym2_MVARpst;/*list match1*/
                                   }
                                }
                                continue WHILE7_BT_LIST_MVARpre;/*computeFail*/
                             
                             } else {
                                continue WHILE7_BT_LIST_MVARpre;/*computeFail*/
                             }
                          } else {
                             continue WHILE7_BT_LIST_MVARpre;/*computeFail*/
                          }
                       }
                       break WHILE7; // muBreak
                       
                    
                    } else {
                       break WHILE7; // muBreak
                    
                    }
                 } else {
                    break WHILE7; // muBreak
                 
                 }
                  //break WHILE7; //muWhileDo
               } while(false);
        
            }
        /* void:  muCon([]) */return ((IList)syms_0);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Layout.rsc|(4158,308,<89,0>,<95,5>) 
    public IBool lang_rascal_grammar_definition_Layout_sepInOthers$77348538892e7a01(IConstructor sep_0, ISet others_1){ 
        
        
        if((((IBool)($RVF.bool(((ISet)others_1).contains(((IConstructor)sep_0)))))).getValue()){
           return ((IBool)$constants.get(4)/*true*/);
        
        }
        /*muExists*/IF9: 
            do {
                if($has_type_and_arity(sep_0, M_ParseTree.Symbol_seq_list_Symbol, 1)){
                   IValue $arg0_94 = (IValue)($aadt_subscript_int(((IConstructor)sep_0),0));
                   if($isComparable($arg0_94.getType(), $T0)){
                      final IList $subject95 = ((IList)($arg0_94));
                      int $subject95_cursor = 0;
                      if($isSubtypeOf($subject95.getType(),$T0)){
                         final int $subject95_len = (int)((IList)($subject95)).length();
                         if($subject95_len == 3){
                            if($subject95_cursor < $subject95_len){
                               IConstructor a_2 = ((IConstructor)($alist_subscript_int(((IList)($subject95)),$subject95_cursor)));
                               $subject95_cursor += 1;
                               if($subject95_cursor < $subject95_len){
                                  $subject95_cursor += 1;
                                  if($subject95_cursor < $subject95_len){
                                     IConstructor b_3 = ((IConstructor)($alist_subscript_int(((IList)($subject95)),$subject95_cursor)));
                                     $subject95_cursor += 1;
                                     if($subject95_cursor == $subject95_len){
                                        if((((IBool)($RVF.bool(((ISet)others_1).contains(((IConstructor)a_2)))))).getValue()){
                                           return ((IBool)$constants.get(4)/*true*/);
                                        
                                        } else {
                                           return ((IBool)($RVF.bool(((ISet)others_1).contains(((IConstructor)b_3)))));
                                        
                                        }
                                     } else {
                                        continue IF9;/*list match1*/
                                     }
                                  }
                               
                               }
                            
                            }
                         
                         }
                      
                      }
                   
                   }
                
                }
        
            } while(false);
        return ((IBool)$constants.get(5)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Layout.rsc|(4468,482,<97,0>,<105,1>) 
    public IConstructor lang_rascal_grammar_definition_Layout_regulars$602edae583c47845(IConstructor s_0, IConstructor $aux_l_1, ISet $aux_others_2){ 
        ValueRef<IConstructor> l_1 = new ValueRef<IConstructor>("l_1", $aux_l_1);
        ValueRef<ISet> others_2 = new ValueRef<ISet>("others_2", $aux_others_2);
    
        
        try {
            IValue $visitResult = $TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{$TF.listType(ADT_Symbol), M_Type.ADT_Exception, $TF.setType(ADT_Symbol), $TF.setType(ADT_Condition), M_lang_rascal_syntax_Rascal.ADT_KeywordArguments_1, M_ParseTree.ADT_Tree, M_ParseTree.ADT_TreeSearchResult_1, M_ParseTree.ADT_Condition, M_ParseTree.ADT_Production, M_ParseTree.ADT_Symbol, M_Grammar.ADT_Grammar, M_ParseTree.ADT_CharRange, M_Grammar.ADT_Item, M_Grammar.ADT_GrammarModule, $TF.listType(ADT_CharRange), M_Grammar.ADT_GrammarDefinition}, 
                                          new io.usethesource.vallang.IConstructor[]{}, 
                                          $RVF.bool(false)),
                 s_0,
                 (IVisitFunction) (IValue $VISIT10_subject, TraversalState $traversalState) -> {
                     VISIT10:switch(Fingerprint.getFingerprint($VISIT10_subject)){
                     
                         case -964239440:
                             if($isSubtypeOf($VISIT10_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_964239440_3: 
                                    do {
                                        if($has_type_and_arity($VISIT10_subject, M_ParseTree.Symbol_iter_star_seps_Symbol_list_Symbol, 2)){
                                           IValue $arg0_107 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT10_subject)),0));
                                           if($isComparable($arg0_107.getType(), M_ParseTree.ADT_Symbol)){
                                              IConstructor n_7 = ((IConstructor)($arg0_107));
                                              IValue $arg1_105 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT10_subject)),1));
                                              if($isComparable($arg1_105.getType(), $T11)){
                                                 final IList $subject106 = ((IList)($arg1_105));
                                                 int $subject106_cursor = 0;
                                                 if($isSubtypeOf($subject106.getType(),$T11)){
                                                    final int $subject106_len = (int)((IList)($subject106)).length();
                                                    if($subject106_len == 1){
                                                       if($subject106_cursor < $subject106_len){
                                                          IConstructor sep_8 = ((IConstructor)($alist_subscript_int(((IList)($subject106)),$subject106_cursor)));
                                                          $subject106_cursor += 1;
                                                          if($subject106_cursor == $subject106_len){
                                                             if((((IBool)($me.sepInOthers(((IConstructor)sep_8), others_2.getValue())))).getValue()){
                                                                
                                                             } else {
                                                                IConstructor $replacement104 = (IConstructor)($RVF.constructor(M_ParseTree.Symbol_iter_star_seps_Symbol_list_Symbol, new IValue[]{((IConstructor)($arg0_107)), ((IList)($RVF.list(l_1.getValue(), sep_8, l_1.getValue())))}));
                                                                if($isSubtypeOf($replacement104.getType(),$VISIT10_subject.getType())){
                                                                   $traversalState.setMatchedAndChanged(true, true);
                                                                   return $replacement104;
                                                                
                                                                } else {
                                                                   break VISIT10;// switch
                                                                
                                                                }
                                                             }
                                                          } else {
                                                             continue CASE_964239440_3;/*list match1*/
                                                          }
                                                       }
                                                    
                                                    }
                                                 
                                                 }
                                              
                                              }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                         case 25942208:
                             if($isSubtypeOf($VISIT10_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_25942208_0: 
                                    do {
                                        if($has_type_and_arity($VISIT10_subject, M_ParseTree.Symbol_iter_Symbol, 1)){
                                           IValue $arg0_97 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT10_subject)),0));
                                           if($isComparable($arg0_97.getType(), M_ParseTree.ADT_Symbol)){
                                              ValueRef<IConstructor> n_3 = new ValueRef<IConstructor>();
                                              IConstructor $replacement96 = (IConstructor)($RVF.constructor(M_ParseTree.Symbol_iter_seps_Symbol_list_Symbol, new IValue[]{((IConstructor)($arg0_97)), ((IList)($RVF.list(l_1.getValue())))}));
                                              if($isSubtypeOf($replacement96.getType(),$VISIT10_subject.getType())){
                                                 $traversalState.setMatchedAndChanged(true, true);
                                                 return $replacement96;
                                              
                                              } else {
                                                 break VISIT10;// switch
                                              
                                              }
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                         case 910072:
                             if($isSubtypeOf($VISIT10_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_910072_4: 
                                    do {
                                        if($has_type_and_arity($VISIT10_subject, M_ParseTree.Symbol_seq_list_Symbol, 1)){
                                           IValue $arg0_109 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT10_subject)),0));
                                           if($isComparable($arg0_109.getType(), $T0)){
                                              IList elems_9 = ((IList)($arg0_109));
                                              IConstructor $replacement108 = (IConstructor)($RVF.constructor(M_ParseTree.Symbol_seq_list_Symbol, new IValue[]{((IList)($me.intermix(((IList)($arg0_109)), l_1.getValue(), others_2.getValue())))}));
                                              if($isSubtypeOf($replacement108.getType(),$VISIT10_subject.getType())){
                                                 $traversalState.setMatchedAndChanged(true, true);
                                                 return $replacement108;
                                              
                                              } else {
                                                 break VISIT10;// switch
                                              
                                              }
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                         case 826203960:
                             if($isSubtypeOf($VISIT10_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_826203960_1: 
                                    do {
                                        if($has_type_and_arity($VISIT10_subject, M_ParseTree.Symbol_iter_star_Symbol, 1)){
                                           IValue $arg0_99 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT10_subject)),0));
                                           if($isComparable($arg0_99.getType(), M_ParseTree.ADT_Symbol)){
                                              ValueRef<IConstructor> n_4 = new ValueRef<IConstructor>();
                                              IConstructor $replacement98 = (IConstructor)($RVF.constructor(M_ParseTree.Symbol_iter_star_seps_Symbol_list_Symbol, new IValue[]{((IConstructor)($arg0_99)), ((IList)($RVF.list(l_1.getValue())))}));
                                              if($isSubtypeOf($replacement98.getType(),$VISIT10_subject.getType())){
                                                 $traversalState.setMatchedAndChanged(true, true);
                                                 return $replacement98;
                                              
                                              } else {
                                                 break VISIT10;// switch
                                              
                                              }
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                         case 1652184736:
                             if($isSubtypeOf($VISIT10_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_1652184736_2: 
                                    do {
                                        if($has_type_and_arity($VISIT10_subject, M_ParseTree.Symbol_iter_seps_Symbol_list_Symbol, 2)){
                                           IValue $arg0_103 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT10_subject)),0));
                                           if($isComparable($arg0_103.getType(), M_ParseTree.ADT_Symbol)){
                                              IConstructor n_5 = ((IConstructor)($arg0_103));
                                              IValue $arg1_101 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT10_subject)),1));
                                              if($isComparable($arg1_101.getType(), $T11)){
                                                 final IList $subject102 = ((IList)($arg1_101));
                                                 int $subject102_cursor = 0;
                                                 if($isSubtypeOf($subject102.getType(),$T11)){
                                                    final int $subject102_len = (int)((IList)($subject102)).length();
                                                    if($subject102_len == 1){
                                                       if($subject102_cursor < $subject102_len){
                                                          IConstructor sep_6 = ((IConstructor)($alist_subscript_int(((IList)($subject102)),$subject102_cursor)));
                                                          $subject102_cursor += 1;
                                                          if($subject102_cursor == $subject102_len){
                                                             if((((IBool)($me.sepInOthers(((IConstructor)sep_6), others_2.getValue())))).getValue()){
                                                                
                                                             } else {
                                                                IConstructor $replacement100 = (IConstructor)($RVF.constructor(M_ParseTree.Symbol_iter_seps_Symbol_list_Symbol, new IValue[]{((IConstructor)($arg0_103)), ((IList)($RVF.list(l_1.getValue(), sep_6, l_1.getValue())))}));
                                                                if($isSubtypeOf($replacement100.getType(),$VISIT10_subject.getType())){
                                                                   $traversalState.setMatchedAndChanged(true, true);
                                                                   return $replacement100;
                                                                
                                                                } else {
                                                                   break VISIT10;// switch
                                                                
                                                                }
                                                             }
                                                          } else {
                                                             continue CASE_1652184736_2;/*list match1*/
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
                     return $VISIT10_subject;
                 });
            return (IConstructor)$visitResult;
        
        } catch (ReturnFromTraversalException e) {
            return (IConstructor) e.getValue();
        }
    
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `lang::rascal::grammar::definition::Layout`");
    }
}