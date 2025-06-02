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
public class $Regular 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.lang.rascal.grammar.definition.$Regular_$I {

    private final $Regular_$I $me;
    private final IList $constants;
    
    
    public final rascal.lang.rascal.grammar.definition.$Modules M_lang_rascal_grammar_definition_Modules;
    public final rascal.$ParseTree M_ParseTree;
    public final rascal.lang.rascal.grammar.definition.$Productions M_lang_rascal_grammar_definition_Productions;
    public final rascal.$Type M_Type;
    public final rascal.$List M_List;
    public final rascal.$Grammar M_Grammar;
    public final rascal.$Message M_Message;

    
    
    public final io.usethesource.vallang.type.Type $T1;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T5;	/*aparameter("A",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T2;	/*aparameter("A",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T3;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Item;	/*aadt("Item",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T4;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TimePartNoTZ;	/*aadt("TimePartNoTZ",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarModule;	/*aadt("GrammarModule",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_IntegerLiteral;	/*aadt("IntegerLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Expression;	/*aadt("KeywordArguments",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Target;	/*aadt("Target",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_1;	/*aadt("Maybe",[aparameter("A",avalue(),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Symbol;	/*aadt("Maybe",[aadt("Symbol",[],dataSyntax())],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Associativity;	/*aadt("Maybe",[aadt("Associativity",[],dataSyntax(),alabel="a")],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CaseInsensitiveStringConstant;	/*aadt("CaseInsensitiveStringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Body;	/*aadt("Body",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Body;	/*aadt("Body",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PrePathChars;	/*aadt("PrePathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PrePathChars;	/*aadt("PrePathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Start;	/*aadt("Start",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Start;	/*aadt("Start",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Backslash;	/*aadt("Backslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Backslash;	/*aadt("Backslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Char;	/*aadt("Char",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringTail;	/*aadt("StringTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringTail;	/*aadt("StringTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T0;	/*aset(aadt("Production",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type $T7;	/*aset(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Grammar;	/*aadt("Grammar",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUTLIST;	/*aadt("LAYOUTLIST",[],layoutSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T6;	/*alist(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Attr;	/*aadt("Maybe",[aadt("Attr",[],dataSyntax())],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type Maybe_Symbol_just_Symbol;	/*acons(aadt("Maybe",[aadt("Symbol",[],dataSyntax())],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="val")],[],alabel="just")*/
    public final io.usethesource.vallang.type.Type ADT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comment;	/*aadt("Comment",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comment;	/*aadt("Comment",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Commands;	/*aadt("Commands",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Commands;	/*aadt("Commands",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_Visit;	/*aadt("Visit",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visit;	/*aadt("Visit",[],contextFreeSyntax())*/

    public $Regular(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Regular(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Regular_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.lang.rascal.grammar.definition.$Regular.class, this);
       
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Modules.class, rex, rascal.lang.rascal.grammar.definition.$Modules::new);
       mstore.importModule(rascal.$ParseTree.class, rex, rascal.$ParseTree::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Productions.class, rex, rascal.lang.rascal.grammar.definition.$Productions::new);
       mstore.importModule(rascal.$Type.class, rex, rascal.$Type::new);
       mstore.importModule(rascal.$List.class, rex, rascal.$List::new);
       mstore.importModule(rascal.$Grammar.class, rex, rascal.$Grammar::new);
       mstore.importModule(rascal.$Message.class, rex, rascal.$Message::new); 
       
       M_lang_rascal_grammar_definition_Modules = mstore.getModule(rascal.lang.rascal.grammar.definition.$Modules.class);
       M_ParseTree = mstore.getModule(rascal.$ParseTree.class);
       M_lang_rascal_grammar_definition_Productions = mstore.getModule(rascal.lang.rascal.grammar.definition.$Productions.class);
       M_Type = mstore.getModule(rascal.$Type.class);
       M_List = mstore.getModule(rascal.$List.class);
       M_Grammar = mstore.getModule(rascal.$Grammar.class);
       M_Message = mstore.getModule(rascal.$Message.class); 
       
                          
       
       $TS.importStore(M_lang_rascal_grammar_definition_Modules.$TS);
       $TS.importStore(M_ParseTree.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Productions.$TS);
       $TS.importStore(M_Type.$TS);
       $TS.importStore(M_List.$TS);
       $TS.importStore(M_Grammar.$TS);
       $TS.importStore(M_Message.$TS);
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/lang/rascal/grammar/definition/$Regular.constants", 10, "44ef26f08b35fdcf5a798e71351f6212");
       ADT_Tree = $adt("Tree");
       NT_Visibility = $sort("Visibility");
       ADT_Visibility = $adt("Visibility");
       NT_Replacement = $sort("Replacement");
       ADT_Replacement = $adt("Replacement");
       NT_ProtocolChars = $lex("ProtocolChars");
       ADT_ProtocolChars = $adt("ProtocolChars");
       NT_LocalVariableDeclaration = $sort("LocalVariableDeclaration");
       ADT_LocalVariableDeclaration = $adt("LocalVariableDeclaration");
       NT_BooleanLiteral = $lex("BooleanLiteral");
       ADT_BooleanLiteral = $adt("BooleanLiteral");
       NT_Expression = $sort("Expression");
       ADT_Expression = $adt("Expression");
       NT_Strategy = $sort("Strategy");
       ADT_Strategy = $adt("Strategy");
       ADT_Attr = $adt("Attr");
       ADT_LocationChangeType = $adt("LocationChangeType");
       NT_LAYOUT = $lex("LAYOUT");
       ADT_LAYOUT = $adt("LAYOUT");
       NT_TagString = $lex("TagString");
       ADT_TagString = $adt("TagString");
       ADT_IOCapability = $adt("IOCapability");
       NT_Name = $lex("Name");
       ADT_Name = $adt("Name");
       NT_FunctionType = $sort("FunctionType");
       ADT_FunctionType = $adt("FunctionType");
       NT_MidStringChars = $lex("MidStringChars");
       ADT_MidStringChars = $adt("MidStringChars");
       NT_PostProtocolChars = $lex("PostProtocolChars");
       ADT_PostProtocolChars = $adt("PostProtocolChars");
       NT_Range = $sort("Range");
       ADT_Range = $adt("Range");
       NT_Concrete = $lex("Concrete");
       ADT_Concrete = $adt("Concrete");
       NT_Output = $lex("Output");
       ADT_Output = $adt("Output");
       NT_SyntaxDefinition = $sort("SyntaxDefinition");
       ADT_SyntaxDefinition = $adt("SyntaxDefinition");
       NT_DatePart = $lex("DatePart");
       ADT_DatePart = $adt("DatePart");
       ADT_Item = $adt("Item");
       NT_RationalLiteral = $lex("RationalLiteral");
       ADT_RationalLiteral = $adt("RationalLiteral");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       NT_RegExpModifier = $lex("RegExpModifier");
       ADT_RegExpModifier = $adt("RegExpModifier");
       NT_Prod = $sort("Prod");
       ADT_Prod = $adt("Prod");
       NT_ModuleParameters = $sort("ModuleParameters");
       ADT_ModuleParameters = $adt("ModuleParameters");
       NT_RegExpLiteral = $lex("RegExpLiteral");
       ADT_RegExpLiteral = $adt("RegExpLiteral");
       NT_TimePartNoTZ = $lex("TimePartNoTZ");
       ADT_TimePartNoTZ = $adt("TimePartNoTZ");
       NT_Pattern = $sort("Pattern");
       ADT_Pattern = $adt("Pattern");
       NT_Case = $sort("Case");
       ADT_Case = $adt("Case");
       NT_ImportedModule = $sort("ImportedModule");
       ADT_ImportedModule = $adt("ImportedModule");
       NT_Declarator = $sort("Declarator");
       ADT_Declarator = $adt("Declarator");
       ADT_GrammarModule = $adt("GrammarModule");
       NT_IntegerLiteral = $sort("IntegerLiteral");
       ADT_IntegerLiteral = $adt("IntegerLiteral");
       NT_Target = $sort("Target");
       ADT_Target = $adt("Target");
       NT_Renaming = $sort("Renaming");
       ADT_Renaming = $adt("Renaming");
       NT_KeywordFormals = $sort("KeywordFormals");
       ADT_KeywordFormals = $adt("KeywordFormals");
       NT_Catch = $sort("Catch");
       ADT_Catch = $adt("Catch");
       ADT_Production = $adt("Production");
       NT_OptionalExpression = $sort("OptionalExpression");
       ADT_OptionalExpression = $adt("OptionalExpression");
       NT_Renamings = $sort("Renamings");
       ADT_Renamings = $adt("Renamings");
       NT_DataTarget = $sort("DataTarget");
       ADT_DataTarget = $adt("DataTarget");
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
       ADT_Symbol = $adt("Symbol");
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
       ADT_Condition = $adt("Condition");
       NT_CaseInsensitiveStringConstant = $lex("CaseInsensitiveStringConstant");
       ADT_CaseInsensitiveStringConstant = $adt("CaseInsensitiveStringConstant");
       NT_Body = $sort("Body");
       ADT_Body = $adt("Body");
       NT_PrePathChars = $lex("PrePathChars");
       ADT_PrePathChars = $adt("PrePathChars");
       NT_Start = $sort("Start");
       ADT_Start = $adt("Start");
       NT_DateAndTime = $lex("DateAndTime");
       ADT_DateAndTime = $adt("DateAndTime");
       NT_RealLiteral = $lex("RealLiteral");
       ADT_RealLiteral = $adt("RealLiteral");
       NT_Backslash = $lex("Backslash");
       ADT_Backslash = $adt("Backslash");
       NT_Char = $lex("Char");
       ADT_Char = $adt("Char");
       NT_StringTail = $sort("StringTail");
       ADT_StringTail = $adt("StringTail");
       ADT_LocationType = $adt("LocationType");
       NT_JustDate = $lex("JustDate");
       ADT_JustDate = $adt("JustDate");
       NT_Header = $sort("Header");
       ADT_Header = $adt("Header");
       ADT_Exception = $adt("Exception");
       NT_Assignment = $sort("Assignment");
       ADT_Assignment = $adt("Assignment");
       NT_StringConstant = $lex("StringConstant");
       ADT_StringConstant = $adt("StringConstant");
       NT_OptionalComma = $lex("OptionalComma");
       ADT_OptionalComma = $adt("OptionalComma");
       NT_FunctionDeclaration = $sort("FunctionDeclaration");
       ADT_FunctionDeclaration = $adt("FunctionDeclaration");
       NT_TypeVar = $sort("TypeVar");
       ADT_TypeVar = $adt("TypeVar");
       ADT_CharRange = $adt("CharRange");
       ADT_Grammar = $adt("Grammar");
       NT_UserType = $sort("UserType");
       ADT_UserType = $adt("UserType");
       NT_Import = $sort("Import");
       ADT_Import = $adt("Import");
       NT_Variant = $sort("Variant");
       ADT_Variant = $adt("Variant");
       NT_DataTypeSelector = $sort("DataTypeSelector");
       ADT_DataTypeSelector = $adt("DataTypeSelector");
       ADT_LAYOUTLIST = $layouts("LAYOUTLIST");
       ADT_Message = $adt("Message");
       NT_ConcreteHole = $sort("ConcreteHole");
       ADT_ConcreteHole = $adt("ConcreteHole");
       NT_MidPathChars = $lex("MidPathChars");
       ADT_MidPathChars = $adt("MidPathChars");
       NT_Sym = $sort("Sym");
       ADT_Sym = $adt("Sym");
       NT_FunctionModifiers = $sort("FunctionModifiers");
       ADT_FunctionModifiers = $adt("FunctionModifiers");
       NT_Comprehension = $sort("Comprehension");
       ADT_Comprehension = $adt("Comprehension");
       NT_QualifiedName = $sort("QualifiedName");
       ADT_QualifiedName = $adt("QualifiedName");
       NT_Formals = $sort("Formals");
       ADT_Formals = $adt("Formals");
       NT_StringMiddle = $sort("StringMiddle");
       ADT_StringMiddle = $adt("StringMiddle");
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
       NT_DecimalIntegerLiteral = $lex("DecimalIntegerLiteral");
       ADT_DecimalIntegerLiteral = $adt("DecimalIntegerLiteral");
       NT_Assoc = $sort("Assoc");
       ADT_Assoc = $adt("Assoc");
       NT_DateTimeLiteral = $sort("DateTimeLiteral");
       ADT_DateTimeLiteral = $adt("DateTimeLiteral");
       NT_Label = $sort("Label");
       ADT_Label = $adt("Label");
       NT_Kind = $sort("Kind");
       ADT_Kind = $adt("Kind");
       NT_PathTail = $sort("PathTail");
       ADT_PathTail = $adt("PathTail");
       NT_PreProtocolChars = $lex("PreProtocolChars");
       ADT_PreProtocolChars = $adt("PreProtocolChars");
       ADT_RuntimeException = $adt("RuntimeException");
       NT_NamedRegExp = $lex("NamedRegExp");
       ADT_NamedRegExp = $adt("NamedRegExp");
       NT_KeywordFormal = $sort("KeywordFormal");
       ADT_KeywordFormal = $adt("KeywordFormal");
       NT_ConcretePart = $lex("ConcretePart");
       ADT_ConcretePart = $adt("ConcretePart");
       NT_Literal = $sort("Literal");
       ADT_Literal = $adt("Literal");
       NT_Toplevel = $sort("Toplevel");
       ADT_Toplevel = $adt("Toplevel");
       NT_EvalCommand = $sort("EvalCommand");
       ADT_EvalCommand = $adt("EvalCommand");
       NT_PathChars = $lex("PathChars");
       ADT_PathChars = $adt("PathChars");
       NT_Module = $sort("Module");
       ADT_Module = $adt("Module");
       NT_PatternWithAction = $sort("PatternWithAction");
       ADT_PatternWithAction = $adt("PatternWithAction");
       NT_PostPathChars = $lex("PostPathChars");
       ADT_PostPathChars = $adt("PostPathChars");
       NT_Statement = $sort("Statement");
       ADT_Statement = $adt("Statement");
       NT_StringCharacter = $lex("StringCharacter");
       ADT_StringCharacter = $adt("StringCharacter");
       NT_BasicType = $sort("BasicType");
       ADT_BasicType = $adt("BasicType");
       NT_CommonKeywordParameters = $sort("CommonKeywordParameters");
       ADT_CommonKeywordParameters = $adt("CommonKeywordParameters");
       NT_ProtocolTail = $sort("ProtocolTail");
       ADT_ProtocolTail = $adt("ProtocolTail");
       NT_Comment = $lex("Comment");
       ADT_Comment = $adt("Comment");
       NT_Commands = $sort("Commands");
       ADT_Commands = $adt("Commands");
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
       NT_Visit = $sort("Visit");
       ADT_Visit = $adt("Visit");
       $T1 = $TF.valueType();
       $T5 = $TF.parameterType("A", $T1);
       $T2 = $TF.parameterType("A", $T1);
       $T3 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_1 = $parameterizedSort("KeywordArguments", new Type[] { $T3 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       NT_Mapping_Expression = $parameterizedSort("Mapping", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       $T4 = $TF.parameterType("T", ADT_Tree);
       NT_Mapping_1 = $parameterizedSort("Mapping", new Type[] { $T3 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       NT_KeywordArguments_Expression = $parameterizedSort("KeywordArguments", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       NT_Mapping_Pattern = $parameterizedSort("Mapping", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       ADT_Maybe_1 = $parameterizedAdt("Maybe", new Type[] { $T5 });
       NT_KeywordArguments_Pattern = $parameterizedSort("KeywordArguments", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       NT_KeywordArgument_1 = $parameterizedSort("KeywordArgument", new Type[] { $T3 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       ADT_Maybe_Symbol = $parameterizedAdt("Maybe", new Type[] { ADT_Symbol });
       ADT_Maybe_Associativity = $parameterizedAdt("Maybe", new Type[] { ADT_Associativity });
       $T0 = $TF.setType(ADT_Production);
       $T7 = $TF.setType(ADT_Symbol);
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T3 });
       $T6 = $TF.listType(ADT_Symbol);
       ADT_Maybe_Attr = $parameterizedAdt("Maybe", new Type[] { ADT_Attr });
       ADT_KeywordArguments_1 = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { $T3 });
       ADT_Mapping_Expression = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Expression });
       ADT_Mapping_1 = $TF.abstractDataType($TS, "Mapping", new Type[] { $T3 });
       ADT_KeywordArguments_Expression = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Expression });
       ADT_Mapping_Pattern = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Pattern });
       ADT_KeywordArguments_Pattern = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Pattern });
       ADT_KeywordArgument_1 = $TF.abstractDataType($TS, "KeywordArgument", new Type[] { $T3 });
       Maybe_Symbol_just_Symbol = $TF.constructor($TS, ADT_Maybe_Symbol, "just", M_ParseTree.ADT_Symbol, "val");
    
       
       
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
    public IBool isStrType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isStrType($P0);
    }
    public IConstructor choice(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_Type.choice($P0, $P1);
    }
    public IBool isADTType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isADTType($P0);
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
    public IConstructor priority(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_ParseTree.priority($P0, $P1);
    }
    public ISet getRegular(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (ISet)lang_rascal_grammar_definition_Regular_getRegular$dc88b31b2388d039((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
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
    public IBool isRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRelType($P0);
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
    public IBool isRegular(IValue $P0){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case -964239440:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)lang_rascal_grammar_definition_Regular_isRegular$9bacb7fa82397bfb((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 25942208:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)lang_rascal_grammar_definition_Regular_isRegular$f947ba45ba6b0909((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 882072:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)lang_rascal_grammar_definition_Regular_isRegular$06a984188fd4b184((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 910072:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)lang_rascal_grammar_definition_Regular_isRegular$94ceadf38ea13420((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 826203960:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)lang_rascal_grammar_definition_Regular_isRegular$37e7401c498ee679((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 773448:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)lang_rascal_grammar_definition_Regular_isRegular$675b2191ad001c7a((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case 1652184736:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IBool)lang_rascal_grammar_definition_Regular_isRegular$d99711b91b43578c((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IBool)lang_rascal_grammar_definition_Regular_isRegular$2b09f4996753f97a((IConstructor) $P0);
         if($result != null) return $result;
         $result = (IBool)lang_rascal_grammar_definition_Regular_isRegular$f3d2c62796e85a2b((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
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
    public IValue makeRegularStubs(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IValue)lang_rascal_grammar_definition_Regular_makeRegularStubs$763ad0487a5ebc29((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (IValue)lang_rascal_grammar_definition_Regular_makeRegularStubs$9fde2b9da42b71a3((IConstructor) $P0);
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
    public IConstructor expandRegularSymbols(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (IConstructor)lang_rascal_grammar_definition_Regular_expandRegularSymbols$ff83722a850ab62e((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue lub(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_Type.lub($P0, $P1);
    }
    public IBool subtype(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IBool) M_Type.subtype($P0, $P1);
    }
    public ISet expand(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (ISet)lang_rascal_grammar_definition_Regular_expand$b032fe4f7908b299((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor associativity(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       Type $P2Type = $P2.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Associativity) && $isSubtypeOf($P2Type,$T0)){
         $result = (IConstructor)M_ParseTree.ParseTree_associativity$9299e943b00366a7((IConstructor) $P0, (IConstructor) $P1, (ISet) $P2);
         if($result != null) return $result;
         $result = (IConstructor)M_ParseTree.ParseTree_associativity$95843a2f3959b22f((IConstructor) $P0, (IConstructor) $P1, (ISet) $P2);
         if($result != null) return $result;
         $result = (IConstructor)M_ParseTree.ParseTree_associativity$05ee42b13b7e96fb((IConstructor) $P0, (IConstructor) $P1, (ISet) $P2);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Maybe_1) && $isSubtypeOf($P2Type, M_ParseTree.ADT_Production)){
         $result = (IConstructor)M_lang_rascal_grammar_definition_Productions.lang_rascal_grammar_definition_Productions_associativity$09cd814bba935894((IConstructor) $P0, (IConstructor) $P1, (IConstructor) $P2);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, ADT_Maybe_Associativity) && $isSubtypeOf($P2Type, M_ParseTree.ADT_Production)){
         $result = (IConstructor)M_lang_rascal_grammar_definition_Productions.lang_rascal_grammar_definition_Productions_associativity$fe1234ba22a8be5e((IConstructor) $P0, (IConstructor) $P1, (IConstructor) $P2);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Associativity) && $isSubtypeOf($P2Type,$T0)){
         return $RVF.constructor(M_ParseTree.Production_associativity_Symbol_Associativity_set_Production, new IValue[]{(IConstructor) $P0, (IConstructor) $P1, (ISet) $P2});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1, $P2));
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

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Regular.rsc|(534,313,<16,0>,<27,1>) 
    public IConstructor lang_rascal_grammar_definition_Regular_expandRegularSymbols$ff83722a850ab62e(IConstructor G_0){ 
        
        
        /*muExists*/FOR0: 
            do {
                FOR0_GEN590:
                for(IValue $elem10_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)G_0), "rules")))))){
                    IConstructor $elem10 = (IConstructor) $elem10_for;
                    if(true){
                       IConstructor def_1 = ((IConstructor)($elem10));
                       /*muExists*/IF1: 
                           do {
                               final IConstructor $subject_val1 = ((IConstructor)($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)G_0), "rules"))))),((IConstructor)def_1))));
                               if($has_type_and_arity($subject_val1, M_Type.Production_choice_Symbol_set_Production, 2)){
                                  IValue $arg0_9 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val1)),0));
                                  if($isComparable($arg0_9.getType(), M_ParseTree.ADT_Symbol)){
                                     if((def_1 != null)){
                                        if(def_1.match($arg0_9)){
                                           IValue $arg1_2 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val1)),1));
                                           if($isComparable($arg1_2.getType(), $T0)){
                                              ISet $subject3 = (ISet)($arg1_2);
                                              IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7:
                                              for(IValue $elem6_for : ((ISet)($subject3))){
                                                  IConstructor $elem6 = (IConstructor) $elem6_for;
                                                  if($has_type_and_arity($elem6, M_ParseTree.Production_regular_Symbol, 1)){
                                                     IValue $arg0_8 = (IValue)($aadt_subscript_int(((IConstructor)($elem6)),0));
                                                     if($isComparable($arg0_8.getType(), M_ParseTree.ADT_Symbol)){
                                                        if(($arg0_9 != null)){
                                                           if($arg0_9.match($arg0_8)){
                                                              final ISet $subject5 = ((ISet)(((ISet)($subject3)).delete(((IConstructor)($elem6)))));
                                                              if(((ISet)($subject5)).size() == 0){
                                                                 IConstructor init_2 = ((IConstructor)(M_Type.choice(((IConstructor)($arg0_8)), ((ISet)$constants.get(0)/*{}*/))));
                                                                 /*muExists*/FOR2: 
                                                                     do {
                                                                         FOR2_GEN729:
                                                                         for(IValue $elem0_for : ((ISet)($me.expand(((IConstructor)($arg0_8)))))){
                                                                             IConstructor $elem0 = (IConstructor) $elem0_for;
                                                                             IConstructor p_3 = null;
                                                                             GuardedIValue guarded3 = $guarded_map_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)G_0), "rules"))))),((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($elem0)), "def"))))));
                                                                             G_0 = ((IConstructor)(((IConstructor)($aadt_field_update("rules", $amap_update(((IConstructor)($aadt_get_field(((IConstructor)($elem0)), "def"))),M_Type.choice(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($elem0)), "def"))))), ((ISet)($RVF.set(((IConstructor)($elem0)), ($is_defined_value(guarded3) ? ((IConstructor)$get_defined_value(guarded3)) : init_2))))), ((IMap)(((IMap)($aadt_get_field(((IConstructor)G_0), "rules")))))), ((IConstructor)G_0))))));
                                                                         
                                                                         }
                                                                         continue FOR2;
                                                                                     
                                                                     } while(false);
                                                                 /* void:  muCon([]) */continue IF1;
                                                              } else {
                                                                 continue IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7;/*redirected IF1_CONS_choice_SET_CONS_regular to IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7; set pat3*/
                                                              }
                                                           } else {
                                                              continue IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7;/*default set elem*/
                                                           }
                                                        } else {
                                                           $arg0_9 = ((IValue)($arg0_8));
                                                           final ISet $subject5 = ((ISet)(((ISet)($subject3)).delete(((IConstructor)($elem6)))));
                                                           if(((ISet)($subject5)).size() == 0){
                                                              IConstructor init_2 = ((IConstructor)(M_Type.choice(((IConstructor)($arg0_8)), ((ISet)$constants.get(0)/*{}*/))));
                                                              /*muExists*/FOR2: 
                                                                  do {
                                                                      FOR2_GEN729:
                                                                      for(IValue $elem0_for : ((ISet)($me.expand(((IConstructor)($arg0_8)))))){
                                                                          IConstructor $elem0 = (IConstructor) $elem0_for;
                                                                          IConstructor p_3 = null;
                                                                          GuardedIValue guarded3 = $guarded_map_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)G_0), "rules"))))),((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($elem0)), "def"))))));
                                                                          G_0 = ((IConstructor)(((IConstructor)($aadt_field_update("rules", $amap_update(((IConstructor)($aadt_get_field(((IConstructor)($elem0)), "def"))),M_Type.choice(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($elem0)), "def"))))), ((ISet)($RVF.set(((IConstructor)($elem0)), ($is_defined_value(guarded3) ? ((IConstructor)$get_defined_value(guarded3)) : init_2))))), ((IMap)(((IMap)($aadt_get_field(((IConstructor)G_0), "rules")))))), ((IConstructor)G_0))))));
                                                                      
                                                                      }
                                                                      continue FOR2;
                                                                                  
                                                                  } while(false);
                                                              /* void:  muCon([]) */continue IF1;
                                                           } else {
                                                              continue IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7;/*redirected IF1_CONS_choice_SET_CONS_regular to IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7; set pat3*/
                                                           }
                                                        }
                                                     } else {
                                                        continue IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7;/*default set elem*/
                                                     }
                                                  } else {
                                                     continue IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7;/*default set elem*/
                                                  }
                                              }
                                              
                                                          
                                           }
                                        
                                        }
                                     
                                     } else {
                                        def_1 = ((IConstructor)($arg0_9));
                                        IValue $arg1_2 = (IValue)($aadt_subscript_int(((IConstructor)($subject_val1)),1));
                                        if($isComparable($arg1_2.getType(), $T0)){
                                           ISet $subject3 = (ISet)($arg1_2);
                                           IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7:
                                           for(IValue $elem6_for : ((ISet)($subject3))){
                                               IConstructor $elem6 = (IConstructor) $elem6_for;
                                               if($has_type_and_arity($elem6, M_ParseTree.Production_regular_Symbol, 1)){
                                                  IValue $arg0_8 = (IValue)($aadt_subscript_int(((IConstructor)($elem6)),0));
                                                  if($isComparable($arg0_8.getType(), M_ParseTree.ADT_Symbol)){
                                                     if(($arg0_9 != null)){
                                                        if($arg0_9.match($arg0_8)){
                                                           final ISet $subject5 = ((ISet)(((ISet)($subject3)).delete(((IConstructor)($elem6)))));
                                                           if(((ISet)($subject5)).size() == 0){
                                                              IConstructor init_2 = ((IConstructor)(M_Type.choice(((IConstructor)($arg0_8)), ((ISet)$constants.get(0)/*{}*/))));
                                                              /*muExists*/FOR2: 
                                                                  do {
                                                                      FOR2_GEN729:
                                                                      for(IValue $elem0_for : ((ISet)($me.expand(((IConstructor)($arg0_8)))))){
                                                                          IConstructor $elem0 = (IConstructor) $elem0_for;
                                                                          IConstructor p_3 = null;
                                                                          GuardedIValue guarded3 = $guarded_map_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)G_0), "rules"))))),((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($elem0)), "def"))))));
                                                                          G_0 = ((IConstructor)(((IConstructor)($aadt_field_update("rules", $amap_update(((IConstructor)($aadt_get_field(((IConstructor)($elem0)), "def"))),M_Type.choice(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($elem0)), "def"))))), ((ISet)($RVF.set(((IConstructor)($elem0)), ($is_defined_value(guarded3) ? ((IConstructor)$get_defined_value(guarded3)) : init_2))))), ((IMap)(((IMap)($aadt_get_field(((IConstructor)G_0), "rules")))))), ((IConstructor)G_0))))));
                                                                      
                                                                      }
                                                                      continue FOR2;
                                                                                  
                                                                  } while(false);
                                                              /* void:  muCon([]) */continue IF1;
                                                           } else {
                                                              continue IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7;/*redirected IF1_CONS_choice_SET_CONS_regular to IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7; set pat3*/
                                                           }
                                                        } else {
                                                           continue IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7;/*default set elem*/
                                                        }
                                                     } else {
                                                        $arg0_9 = ((IValue)($arg0_8));
                                                        final ISet $subject5 = ((ISet)(((ISet)($subject3)).delete(((IConstructor)($elem6)))));
                                                        if(((ISet)($subject5)).size() == 0){
                                                           IConstructor init_2 = ((IConstructor)(M_Type.choice(((IConstructor)($arg0_8)), ((ISet)$constants.get(0)/*{}*/))));
                                                           /*muExists*/FOR2: 
                                                               do {
                                                                   FOR2_GEN729:
                                                                   for(IValue $elem0_for : ((ISet)($me.expand(((IConstructor)($arg0_8)))))){
                                                                       IConstructor $elem0 = (IConstructor) $elem0_for;
                                                                       IConstructor p_3 = null;
                                                                       GuardedIValue guarded3 = $guarded_map_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)G_0), "rules"))))),((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($elem0)), "def"))))));
                                                                       G_0 = ((IConstructor)(((IConstructor)($aadt_field_update("rules", $amap_update(((IConstructor)($aadt_get_field(((IConstructor)($elem0)), "def"))),M_Type.choice(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($elem0)), "def"))))), ((ISet)($RVF.set(((IConstructor)($elem0)), ($is_defined_value(guarded3) ? ((IConstructor)$get_defined_value(guarded3)) : init_2))))), ((IMap)(((IMap)($aadt_get_field(((IConstructor)G_0), "rules")))))), ((IConstructor)G_0))))));
                                                                   
                                                                   }
                                                                   continue FOR2;
                                                                               
                                                               } while(false);
                                                           /* void:  muCon([]) */continue IF1;
                                                        } else {
                                                           continue IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7;/*redirected IF1_CONS_choice_SET_CONS_regular to IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7; set pat3*/
                                                        }
                                                     }
                                                  } else {
                                                     continue IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7;/*default set elem*/
                                                  }
                                               } else {
                                                  continue IF1_CONS_choice_SET_CONS_regular$_DFLT_SET_ELM7;/*default set elem*/
                                               }
                                           }
                                           
                                                       
                                        }
                                     
                                     }
                                  }
                               
                               }
                       
                           } while(false);
                    
                    } else {
                       continue FOR0_GEN590;
                    }
                }
                continue FOR0;
                            
            } while(false);
        /* void:  muCon([]) */return ((IConstructor)G_0);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Regular.rsc|(849,1046,<29,0>,<51,1>) 
    public ISet lang_rascal_grammar_definition_Regular_expand$b032fe4f7908b299(IConstructor s_0){ 
        
        
        final IConstructor $switchVal11 = ((IConstructor)s_0);
        boolean noCaseMatched_$switchVal11 = true;
        SWITCH4: switch(Fingerprint.getFingerprint($switchVal11)){
        
            case -964239440:
                if(noCaseMatched_$switchVal11){
                    noCaseMatched_$switchVal11 = false;
                    if($isSubtypeOf($switchVal11.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_964239440_4: 
                           do {
                               if($has_type_and_arity($switchVal11, M_ParseTree.Symbol_iter_star_seps_Symbol_list_Symbol, 2)){
                                  IValue $arg0_19 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal11)),0));
                                  if($isComparable($arg0_19.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor t_6 = ((IConstructor)($arg0_19));
                                     IValue $arg1_18 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal11)),1));
                                     if($isComparable($arg1_18.getType(), $T6)){
                                        IList seps_7 = ((IList)($arg1_18));
                                        return ((ISet)($aset_add_aset(((ISet)($RVF.set(((IConstructor)(M_Type.choice(((IConstructor)s_0), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)$constants.get(1)/*"empty"*/), ((IConstructor)s_0)}))), ((IList)$constants.get(2)/*[]*/), ((ISet)$constants.get(0)/*{}*/)}))), $RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)$constants.get(3)/*"nonEmpty"*/), ((IConstructor)s_0)}))), ((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_iter_seps_Symbol_list_Symbol, new IValue[]{((IConstructor)($arg0_19)), ((IList)($arg1_18))})))))), ((ISet)$constants.get(0)/*{}*/)})))))))))),((ISet)($me.expand(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_iter_seps_Symbol_list_Symbol, new IValue[]{((IConstructor)($arg0_19)), ((IList)($arg1_18))})))))))));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 25942208:
                if(noCaseMatched_$switchVal11){
                    noCaseMatched_$switchVal11 = false;
                    if($isSubtypeOf($switchVal11.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_25942208_1: 
                           do {
                               if($has_type_and_arity($switchVal11, M_ParseTree.Symbol_iter_Symbol, 1)){
                                  IValue $arg0_13 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal11)),0));
                                  if($isComparable($arg0_13.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor t_2 = null;
                                     return ((ISet)($RVF.set(((IConstructor)(M_Type.choice(((IConstructor)s_0), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)$constants.get(4)/*"single"*/), ((IConstructor)s_0)}))), ((IList)($RVF.list(((IConstructor)($arg0_13))))), ((ISet)$constants.get(0)/*{}*/)}))), $RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)$constants.get(5)/*"multiple"*/), ((IConstructor)s_0)}))), ((IList)($RVF.list(((IConstructor)($arg0_13)), s_0))), ((ISet)$constants.get(0)/*{}*/)}))))))))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 882072:
                if(noCaseMatched_$switchVal11){
                    noCaseMatched_$switchVal11 = false;
                    if($isSubtypeOf($switchVal11.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_882072_0: 
                           do {
                               if($has_type_and_arity($switchVal11, M_ParseTree.Symbol_opt_Symbol, 1)){
                                  IValue $arg0_12 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal11)),0));
                                  if($isComparable($arg0_12.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor t_1 = null;
                                     return ((ISet)($RVF.set(((IConstructor)(M_Type.choice(((IConstructor)s_0), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)$constants.get(6)/*"absent"*/), ((IConstructor)s_0)}))), ((IList)$constants.get(2)/*[]*/), ((ISet)$constants.get(0)/*{}*/)}))), $RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)$constants.get(7)/*"present"*/), ((IConstructor)s_0)}))), ((IList)($RVF.list(((IConstructor)($arg0_12))))), ((ISet)$constants.get(0)/*{}*/)}))))))))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 910072:
                if(noCaseMatched_$switchVal11){
                    noCaseMatched_$switchVal11 = false;
                    if($isSubtypeOf($switchVal11.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_910072_6: 
                           do {
                               if($has_type_and_arity($switchVal11, M_ParseTree.Symbol_seq_list_Symbol, 1)){
                                  IValue $arg0_24 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal11)),0));
                                  if($isComparable($arg0_24.getType(), $T6)){
                                     IList elems_10 = null;
                                     return ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)s_0), ((IList)($arg0_24)), ((ISet)$constants.get(0)/*{}*/)}))))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 826203960:
                if(noCaseMatched_$switchVal11){
                    noCaseMatched_$switchVal11 = false;
                    if($isSubtypeOf($switchVal11.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_826203960_2: 
                           do {
                               if($has_type_and_arity($switchVal11, M_ParseTree.Symbol_iter_star_Symbol, 1)){
                                  IValue $arg0_14 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal11)),0));
                                  if($isComparable($arg0_14.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor t_3 = ((IConstructor)($arg0_14));
                                     return ((ISet)($aset_add_aset(((ISet)($RVF.set(((IConstructor)(M_Type.choice(((IConstructor)s_0), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)$constants.get(1)/*"empty"*/), ((IConstructor)s_0)}))), ((IList)$constants.get(2)/*[]*/), ((ISet)$constants.get(0)/*{}*/)}))), $RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)$constants.get(3)/*"nonEmpty"*/), ((IConstructor)s_0)}))), ((IList)($RVF.list(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_iter_Symbol, new IValue[]{((IConstructor)($arg0_14))})))))), ((ISet)$constants.get(0)/*{}*/)})))))))))),((ISet)($me.expand(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_iter_Symbol, new IValue[]{((IConstructor)($arg0_14))})))))))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 773448:
                if(noCaseMatched_$switchVal11){
                    noCaseMatched_$switchVal11 = false;
                    if($isSubtypeOf($switchVal11.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_773448_5: 
                           do {
                               if($has_type_and_arity($switchVal11, M_ParseTree.Symbol_alt_set_Symbol, 1)){
                                  IValue $arg0_23 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal11)),0));
                                  if($isComparable($arg0_23.getType(), $T7)){
                                     ISet alts_8 = null;
                                     final ISetWriter $setwriter20 = (ISetWriter)$RVF.setWriter();
                                     ;
                                     $SCOMP21_GEN1694:
                                     for(IValue $elem22_for : ((ISet)($arg0_23))){
                                         IConstructor $elem22 = (IConstructor) $elem22_for;
                                         IConstructor a_9 = null;
                                         $setwriter20.insert($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)s_0), ((IList)($RVF.list(((IConstructor)($elem22))))), ((ISet)$constants.get(0)/*{}*/)}));
                                     
                                     }
                                     
                                                 return ((ISet)($RVF.set(((IConstructor)(M_Type.choice(((IConstructor)s_0), ((ISet)($setwriter20.done()))))))));
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 1652184736:
                if(noCaseMatched_$switchVal11){
                    noCaseMatched_$switchVal11 = false;
                    if($isSubtypeOf($switchVal11.getType(),M_ParseTree.ADT_Symbol)){
                       /*muExists*/CASE_1652184736_3: 
                           do {
                               if($has_type_and_arity($switchVal11, M_ParseTree.Symbol_iter_seps_Symbol_list_Symbol, 2)){
                                  IValue $arg0_17 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal11)),0));
                                  if($isComparable($arg0_17.getType(), M_ParseTree.ADT_Symbol)){
                                     IConstructor t_4 = null;
                                     IValue $arg1_16 = (IValue)($aadt_subscript_int(((IConstructor)($switchVal11)),1));
                                     if($isComparable($arg1_16.getType(), $T6)){
                                        IList seps_5 = null;
                                        final IListWriter $writer15 = (IListWriter)$RVF.listWriter();
                                        $writer15.append($arg0_17);
                                        $listwriter_splice($writer15,$arg1_16);
                                        $writer15.append(s_0);
                                        return ((ISet)($RVF.set(((IConstructor)(M_Type.choice(((IConstructor)s_0), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)$constants.get(4)/*"single"*/), ((IConstructor)s_0)}))), ((IList)($RVF.list(((IConstructor)($arg0_17))))), ((ISet)$constants.get(0)/*{}*/)}))), $RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)$constants.get(5)/*"multiple"*/), ((IConstructor)s_0)}))), ((IList)($writer15.done())), ((ISet)$constants.get(0)/*{}*/)}))))))))));
                                     
                                     }
                                  
                                  }
                               
                               }
                       
                           } while(false);
                    
                    }
        
                }
                
        
            case 0:
                if(noCaseMatched_$switchVal11){
                    noCaseMatched_$switchVal11 = false;
                    
                }
                
        
            default: if($isSubtypeOf($switchVal11.getType(),M_ParseTree.ADT_Symbol)){
                        /*muExists*/CASE_0_7: 
                            do {
                                if($has_type_and_arity($switchVal11, M_ParseTree.Symbol_empty_, 0)){
                                   return ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)s_0), ((IList)$constants.get(2)/*[]*/), ((ISet)$constants.get(0)/*{}*/)}))))));
                                
                                }
                        
                            } while(false);
                     
                     }
        
        }
        
                   final Template $template25 = (Template)new Template($RVF, "expand, missed a case ");
        $template25.beginIndent("                      ");
        $template25.addVal(s_0);
        $template25.endIndent("                      ");
        throw new Throw($template25.close());
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Regular.rsc|(1897,173,<53,0>,<57,1>) 
    public IConstructor lang_rascal_grammar_definition_Regular_makeRegularStubs$9fde2b9da42b71a3(IConstructor g_0){ 
        
        
        final ISetWriter $setwriter26 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP27_GEN1969:
        for(IValue $elem28_for : ((IMap)(((IMap)($aadt_get_field(((IConstructor)g_0), "rules")))))){
            IConstructor $elem28 = (IConstructor) $elem28_for;
            IConstructor nont_2 = null;
            $setwriter26.insert($amap_subscript(((IMap)(((IMap)($aadt_get_field(((IConstructor)g_0), "rules"))))),((IConstructor)($elem28))));
        
        }
        
                    ISet prods_1 = ((ISet)($setwriter26.done()));
        ISet stubs_3 = ((ISet)($me.makeRegularStubs(((ISet)prods_1))));
        return ((IConstructor)(M_Grammar.compose(((IConstructor)g_0), ((IConstructor)(M_Grammar.grammar(((ISet)$constants.get(0)/*{}*/), ((ISet)stubs_3)))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Regular.rsc|(2072,171,<59,0>,<61,1>) 
    public ISet lang_rascal_grammar_definition_Regular_makeRegularStubs$763ad0487a5ebc29(ISet prods_0){ 
        
        
        final ISetWriter $setwriter29 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP30_GEN2162:
        for(IValue $elem33_for : ((ISet)prods_0)){
            IConstructor $elem33 = (IConstructor) $elem33_for;
            $SCOMP30_GEN2162_DESC2162:
            for(IValue $elem34 : new DescendantMatchIterator($elem33, 
                new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                if($isComparable($elem34.getType(), M_ParseTree.ADT_Production)){
                   if($isSubtypeOf($elem34.getType(),M_ParseTree.ADT_Production)){
                      if($has_type_and_arity($elem34, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                         IValue $arg0_37 = (IValue)($subscript_int(((IValue)($elem34)),0));
                         if($isComparable($arg0_37.getType(), $T1)){
                            IValue $arg1_36 = (IValue)($subscript_int(((IValue)($elem34)),1));
                            if($isComparable($arg1_36.getType(), $T1)){
                               IValue $arg2_35 = (IValue)($subscript_int(((IValue)($elem34)),2));
                               if($isComparable($arg2_35.getType(), $T1)){
                                  IConstructor p_1 = ((IConstructor)($elem34));
                                  $SCOMP30_GEN2162_DESC2162_GEN2198:
                                  for(IValue $elem32_for : ((IList)(((IList)($aadt_get_field(((IConstructor)p_1), "symbols")))))){
                                      IConstructor $elem32 = (IConstructor) $elem32_for;
                                      IConstructor sym_2 = ((IConstructor)($elem32));
                                      $SCOMP30_GEN2162_DESC2162_GEN2198_GEN2216:
                                      for(IValue $elem31_for : ((ISet)($me.getRegular(((IConstructor)sym_2))))){
                                          IConstructor $elem31 = (IConstructor) $elem31_for;
                                          IConstructor reg_3 = null;
                                          $setwriter29.insert($RVF.constructor(M_ParseTree.Production_regular_Symbol, new IValue[]{((IConstructor)($elem31))}));
                                      
                                      }
                                      continue $SCOMP30_GEN2162_DESC2162_GEN2198;
                                                  
                                  }
                                  continue $SCOMP30_GEN2162_DESC2162;
                                              
                               } else {
                                  continue $SCOMP30_GEN2162_DESC2162;
                               }
                            } else {
                               continue $SCOMP30_GEN2162_DESC2162;
                            }
                         } else {
                            continue $SCOMP30_GEN2162_DESC2162;
                         }
                      } else {
                         continue $SCOMP30_GEN2162_DESC2162;
                      }
                   } else {
                      continue $SCOMP30_GEN2162_DESC2162;
                   }
                } else {
                   continue $SCOMP30_GEN2162_DESC2162;
                }
            }
            continue $SCOMP30_GEN2162;
                         
        }
        
                    return ((ISet)($setwriter29.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Regular.rsc|(2245,80,<63,0>,<63,80>) 
    public ISet lang_rascal_grammar_definition_Regular_getRegular$dc88b31b2388d039(IConstructor s_0){ 
        
        
        final ISetWriter $setwriter38 = (ISetWriter)$RVF.setWriter();
        ;
        /*muExists*/$SCOMP39: 
            do {
                $SCOMP39_DESC2294:
                for(IValue $elem40 : new DescendantMatchIterator(s_0, 
                    new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{$TF.listType(ADT_Symbol), M_Type.ADT_Exception, $TF.setType(ADT_Symbol), $TF.setType(ADT_Condition), ADT_KeywordArguments_1, M_ParseTree.ADT_Tree, M_ParseTree.ADT_TreeSearchResult_1, M_ParseTree.ADT_Condition, M_ParseTree.ADT_Production, M_ParseTree.ADT_Symbol, M_Grammar.ADT_Grammar, M_ParseTree.ADT_CharRange, M_Grammar.ADT_Item, M_Grammar.ADT_GrammarModule, $TF.listType(ADT_CharRange), M_Grammar.ADT_GrammarDefinition}, 
                                             new io.usethesource.vallang.IConstructor[]{}, 
                                             $RVF.bool(false)))){
                    if($isComparable($elem40.getType(), M_ParseTree.ADT_Symbol)){
                       if($isSubtypeOf($elem40.getType(),M_ParseTree.ADT_Symbol)){
                          IConstructor t_1 = null;
                          if((((IBool)($me.isRegular(((IConstructor)($elem40)))))).getValue()){
                            $setwriter38.insert($elem40);
                          
                          } else {
                            continue $SCOMP39_DESC2294;
                          }
                       
                       } else {
                          continue $SCOMP39_DESC2294;
                       }
                    } else {
                       continue $SCOMP39_DESC2294;
                    }
                }
                
                             
            } while(false);
        return ((ISet)($setwriter38.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Regular.rsc|(2328,48,<65,0>,<65,48>) 
    public IBool lang_rascal_grammar_definition_Regular_isRegular$f3d2c62796e85a2b(IConstructor s_0){ 
        
        
        return ((IBool)$constants.get(8)/*false*/);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Regular.rsc|(2377,44,<66,0>,<66,44>) 
    public IBool lang_rascal_grammar_definition_Regular_isRegular$06a984188fd4b184(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_opt_Symbol, 1)){
           IValue $arg0_41 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_41.getType(), M_ParseTree.ADT_Symbol)){
              return ((IBool)$constants.get(9)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Regular.rsc|(2422,45,<67,0>,<67,45>) 
    public IBool lang_rascal_grammar_definition_Regular_isRegular$f947ba45ba6b0909(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_iter_Symbol, 1)){
           IValue $arg0_42 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_42.getType(), M_ParseTree.ADT_Symbol)){
              return ((IBool)$constants.get(9)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Regular.rsc|(2468,51,<68,0>,<68,51>) 
    public IBool lang_rascal_grammar_definition_Regular_isRegular$37e7401c498ee679(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_iter_star_Symbol, 1)){
           IValue $arg0_43 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_43.getType(), M_ParseTree.ADT_Symbol)){
              return ((IBool)$constants.get(9)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Regular.rsc|(2520,67,<69,0>,<69,67>) 
    public IBool lang_rascal_grammar_definition_Regular_isRegular$d99711b91b43578c(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_iter_seps_Symbol_list_Symbol, 2)){
           IValue $arg0_45 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_45.getType(), M_ParseTree.ADT_Symbol)){
              IValue $arg1_44 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_44.getType(), $T6)){
                 return ((IBool)$constants.get(9)/*true*/);
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Regular.rsc|(2588,72,<70,0>,<70,72>) 
    public IBool lang_rascal_grammar_definition_Regular_isRegular$9bacb7fa82397bfb(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_iter_star_seps_Symbol_list_Symbol, 2)){
           IValue $arg0_47 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_47.getType(), M_ParseTree.ADT_Symbol)){
              IValue $arg1_46 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_46.getType(), $T6)){
                 return ((IBool)$constants.get(9)/*true*/);
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Regular.rsc|(2661,49,<71,0>,<71,49>) 
    public IBool lang_rascal_grammar_definition_Regular_isRegular$675b2191ad001c7a(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_alt_set_Symbol, 1)){
           IValue $arg0_48 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_48.getType(), $T7)){
              return ((IBool)$constants.get(9)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Regular.rsc|(2711,50,<72,0>,<72,50>) 
    public IBool lang_rascal_grammar_definition_Regular_isRegular$94ceadf38ea13420(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_seq_list_Symbol, 1)){
           IValue $arg0_49 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_49.getType(), $T6)){
              return ((IBool)$constants.get(9)/*true*/);
           
           } else {
              return null;
           }
        } else {
           return null;
        }
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Regular.rsc|(2762,38,<73,0>,<73,38>) 
    public IBool lang_rascal_grammar_definition_Regular_isRegular$2b09f4996753f97a(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_empty_, 0)){
           return ((IBool)$constants.get(9)/*true*/);
        
        } else {
           return null;
        }
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `lang::rascal::grammar::definition::Regular`");
    }
}