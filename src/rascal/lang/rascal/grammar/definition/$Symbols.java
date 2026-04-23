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
public class $Symbols 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.lang.rascal.grammar.definition.$Symbols_$I {

    private final $Symbols_$I $me;
    private final IList $constants;
    
    
    public final rascal.lang.rascal.syntax.$Rascal M_lang_rascal_syntax_Rascal;
    public final rascal.lang.rascal.grammar.definition.$Literals M_lang_rascal_grammar_definition_Literals;
    public final rascal.$Message M_Message;
    public final rascal.lang.rascal.grammar.definition.$Characters M_lang_rascal_grammar_definition_Characters;
    public final rascal.$Exception M_Exception;
    public final rascal.$Type M_Type;
    public final rascal.$List M_List;
    public final rascal.$String M_String;
    public final rascal.$ParseTree M_ParseTree;

    
    
    public final io.usethesource.vallang.type.Type $T9;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T7;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T8;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T11;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T1;	/*alit(",")*/
    public final io.usethesource.vallang.type.Type ADT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUTLIST;	/*aadt("LAYOUTLIST",[],layoutSyntax())*/
    public final io.usethesource.vallang.type.Type $T15;	/*\iter-seps(aadt("Sym",[],contextFreeSyntax()),[aadt("LAYOUTLIST",[],layoutSyntax())])*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T14;	/*alit("|")*/
    public final io.usethesource.vallang.type.Type $T13;	/*\iter-seps(aadt("Sym",[],contextFreeSyntax()),[aadt("LAYOUTLIST",[],layoutSyntax()),alit("|"),aadt("LAYOUTLIST",[],layoutSyntax())])*/
    public final io.usethesource.vallang.type.Type ADT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Item;	/*aadt("Item",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarModule;	/*aadt("GrammarModule",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T12;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Declarator;	/*aadt("Declarator",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T6;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T0;	/*\iter-seps(aadt("Sym",[],contextFreeSyntax()),[aadt("LAYOUTLIST",[],layoutSyntax()),alit(","),aadt("LAYOUTLIST",[],layoutSyntax())])*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Associativity;	/*aadt("Associativity",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Body;	/*aadt("Body",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Body;	/*aadt("Body",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PrePathChars;	/*aadt("PrePathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PrePathChars;	/*aadt("PrePathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Start;	/*aadt("Start",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Start;	/*aadt("Start",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T5;	/*aset(aadt("Symbol",[],dataSyntax()))*/
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
    public final io.usethesource.vallang.type.Type ADT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Grammar;	/*aadt("Grammar",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T10;	/*alist(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T4;	/*\iter-seps(aadt("Expression",[],contextFreeSyntax()),[aadt("LAYOUTLIST",[],layoutSyntax()),alit(","),aadt("LAYOUTLIST",[],layoutSyntax())])*/
    public final io.usethesource.vallang.type.Type ADT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T2;	/*\iter-star-seps(aadt("Sym",[],contextFreeSyntax()),[aadt("LAYOUTLIST",[],layoutSyntax())])*/
    public final io.usethesource.vallang.type.Type ADT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
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
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T3;	/*aset(aadt("Condition",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
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

    public $Symbols(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Symbols(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Symbols_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.lang.rascal.grammar.definition.$Symbols.class, this);
       
       mstore.importModule(rascal.lang.rascal.syntax.$Rascal.class, rex, rascal.lang.rascal.syntax.$Rascal::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Literals.class, rex, rascal.lang.rascal.grammar.definition.$Literals::new);
       mstore.importModule(rascal.$Message.class, rex, rascal.$Message::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Characters.class, rex, rascal.lang.rascal.grammar.definition.$Characters::new);
       mstore.importModule(rascal.$Exception.class, rex, rascal.$Exception::new);
       mstore.importModule(rascal.$Type.class, rex, rascal.$Type::new);
       mstore.importModule(rascal.$List.class, rex, rascal.$List::new);
       mstore.importModule(rascal.$String.class, rex, rascal.$String::new);
       mstore.importModule(rascal.$ParseTree.class, rex, rascal.$ParseTree::new); 
       
       M_lang_rascal_syntax_Rascal = mstore.getModule(rascal.lang.rascal.syntax.$Rascal.class);
       M_lang_rascal_grammar_definition_Literals = mstore.getModule(rascal.lang.rascal.grammar.definition.$Literals.class);
       M_Message = mstore.getModule(rascal.$Message.class);
       M_lang_rascal_grammar_definition_Characters = mstore.getModule(rascal.lang.rascal.grammar.definition.$Characters.class);
       M_Exception = mstore.getModule(rascal.$Exception.class);
       M_Type = mstore.getModule(rascal.$Type.class);
       M_List = mstore.getModule(rascal.$List.class);
       M_String = mstore.getModule(rascal.$String.class);
       M_ParseTree = mstore.getModule(rascal.$ParseTree.class); 
       
                          
       
       $TS.importStore(M_lang_rascal_syntax_Rascal.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Literals.$TS);
       $TS.importStore(M_Message.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Characters.$TS);
       $TS.importStore(M_Exception.$TS);
       $TS.importStore(M_Type.$TS);
       $TS.importStore(M_List.$TS);
       $TS.importStore(M_String.$TS);
       $TS.importStore(M_ParseTree.$TS);
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/lang/rascal/grammar/definition/$Symbols.constants", 8, "095e2965e6051c6664c9edfcca3e5484");
       NT_Expression = $sort("Expression");
       ADT_Expression = $adt("Expression");
       NT_Strategy = $sort("Strategy");
       ADT_Strategy = $adt("Strategy");
       NT_Visibility = $sort("Visibility");
       ADT_Visibility = $adt("Visibility");
       NT_ProtocolChars = $lex("ProtocolChars");
       ADT_ProtocolChars = $adt("ProtocolChars");
       NT_FunctionType = $sort("FunctionType");
       ADT_FunctionType = $adt("FunctionType");
       ADT_Attr = $adt("Attr");
       ADT_Tree = $adt("Tree");
       NT_MidStringChars = $lex("MidStringChars");
       ADT_MidStringChars = $adt("MidStringChars");
       NT_Replacement = $sort("Replacement");
       ADT_Replacement = $adt("Replacement");
       ADT_LocationChangeType = $adt("LocationChangeType");
       NT_Sym = $sort("Sym");
       ADT_Sym = $adt("Sym");
       ADT_LAYOUTLIST = $layouts("LAYOUTLIST");
       ADT_IOCapability = $adt("IOCapability");
       NT_Concrete = $lex("Concrete");
       ADT_Concrete = $adt("Concrete");
       NT_PostProtocolChars = $lex("PostProtocolChars");
       ADT_PostProtocolChars = $adt("PostProtocolChars");
       NT_Range = $sort("Range");
       ADT_Range = $adt("Range");
       NT_LAYOUT = $lex("LAYOUT");
       ADT_LAYOUT = $adt("LAYOUT");
       NT_Pattern = $sort("Pattern");
       ADT_Pattern = $adt("Pattern");
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
       NT_Declarator = $sort("Declarator");
       ADT_Declarator = $adt("Declarator");
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
       NT_TagString = $lex("TagString");
       ADT_TagString = $adt("TagString");
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
       ADT_Symbol = $adt("Symbol");
       NT_Nonterminal = $lex("Nonterminal");
       ADT_Nonterminal = $adt("Nonterminal");
       NT_RegExp = $lex("RegExp");
       ADT_RegExp = $adt("RegExp");
       NT_PreStringChars = $lex("PreStringChars");
       ADT_PreStringChars = $adt("PreStringChars");
       NT_StringLiteral = $sort("StringLiteral");
       ADT_StringLiteral = $adt("StringLiteral");
       NT_Variable = $sort("Variable");
       ADT_Variable = $adt("Variable");
       NT_Name = $lex("Name");
       ADT_Name = $adt("Name");
       NT_NonterminalLabel = $lex("NonterminalLabel");
       ADT_NonterminalLabel = $adt("NonterminalLabel");
       NT_Bound = $sort("Bound");
       ADT_Bound = $adt("Bound");
       NT_TypeArg = $sort("TypeArg");
       ADT_TypeArg = $adt("TypeArg");
       NT_Class = $sort("Class");
       ADT_Class = $adt("Class");
       ADT_Condition = $adt("Condition");
       NT_PathPart = $sort("PathPart");
       ADT_PathPart = $adt("PathPart");
       NT_Signature = $sort("Signature");
       ADT_Signature = $adt("Signature");
       NT_Tags = $sort("Tags");
       ADT_Tags = $adt("Tags");
       NT_URLChars = $lex("URLChars");
       ADT_URLChars = $adt("URLChars");
       NT_ModuleActuals = $sort("ModuleActuals");
       ADT_ModuleActuals = $adt("ModuleActuals");
       ADT_Associativity = $adt("Associativity");
       NT_DataTypeSelector = $sort("DataTypeSelector");
       ADT_DataTypeSelector = $adt("DataTypeSelector");
       NT_Body = $sort("Body");
       ADT_Body = $adt("Body");
       NT_PrePathChars = $lex("PrePathChars");
       ADT_PrePathChars = $adt("PrePathChars");
       NT_Start = $sort("Start");
       ADT_Start = $adt("Start");
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
       NT_OptionalComma = $lex("OptionalComma");
       ADT_OptionalComma = $adt("OptionalComma");
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
       NT_FunctionModifiers = $sort("FunctionModifiers");
       ADT_FunctionModifiers = $adt("FunctionModifiers");
       NT_Import = $sort("Import");
       ADT_Import = $adt("Import");
       NT_Formals = $sort("Formals");
       ADT_Formals = $adt("Formals");
       NT_ConcreteHole = $sort("ConcreteHole");
       ADT_ConcreteHole = $adt("ConcreteHole");
       ADT_Grammar = $adt("Grammar");
       ADT_Message = $adt("Message");
       NT_RealLiteral = $lex("RealLiteral");
       ADT_RealLiteral = $adt("RealLiteral");
       NT_StringMiddle = $sort("StringMiddle");
       ADT_StringMiddle = $adt("StringMiddle");
       NT_Comprehension = $sort("Comprehension");
       ADT_Comprehension = $adt("Comprehension");
       NT_QualifiedName = $sort("QualifiedName");
       ADT_QualifiedName = $adt("QualifiedName");
       NT_MidPathChars = $lex("MidPathChars");
       ADT_MidPathChars = $adt("MidPathChars");
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
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       NT_PathChars = $lex("PathChars");
       ADT_PathChars = $adt("PathChars");
       NT_Toplevel = $sort("Toplevel");
       ADT_Toplevel = $adt("Toplevel");
       NT_EvalCommand = $sort("EvalCommand");
       ADT_EvalCommand = $adt("EvalCommand");
       NT_PostPathChars = $lex("PostPathChars");
       ADT_PostPathChars = $adt("PostPathChars");
       NT_StringCharacter = $lex("StringCharacter");
       ADT_StringCharacter = $adt("StringCharacter");
       NT_Module = $sort("Module");
       ADT_Module = $adt("Module");
       NT_Statement = $sort("Statement");
       ADT_Statement = $adt("Statement");
       NT_ConcretePart = $lex("ConcretePart");
       ADT_ConcretePart = $adt("ConcretePart");
       NT_Literal = $sort("Literal");
       ADT_Literal = $adt("Literal");
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
       $T9 = $TF.stringType();
       $T7 = $TF.valueType();
       $T8 = $TF.parameterType("T", $T7);
       NT_Mapping_Expression = $parameterizedSort("Mapping", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       $T11 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_1 = $parameterizedSort("KeywordArguments", new Type[] { $T11 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       $T1 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_Lit, $RVF.string(",")));
       $T15 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_IterSeps, $RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Sym")), $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")))));
       $T14 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_Lit, $RVF.string("|")));
       $T13 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_IterSeps, $RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Sym")), $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")), $RVF.constructor(RascalValueFactory.Symbol_Lit, $RVF.string("|")), $RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")))));
       NT_Mapping_Pattern = $parameterizedSort("Mapping", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       $T12 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_Expression = $parameterizedSort("KeywordArguments", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       NT_Mapping_1 = $parameterizedSort("Mapping", new Type[] { $T11 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       $T6 = $TF.listType($T8);
       NT_KeywordArgument_1 = $parameterizedSort("KeywordArgument", new Type[] { $T11 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       $T0 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_IterSeps, $RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Sym")), $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")), $RVF.constructor(RascalValueFactory.Symbol_Lit, $RVF.string(",")), $RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")))));
       NT_KeywordArguments_Pattern = $parameterizedSort("KeywordArguments", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       $T5 = $TF.setType(ADT_Symbol);
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T11 });
       $T10 = $TF.listType(ADT_Symbol);
       $T4 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_IterSeps, $RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression")), $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")), $RVF.constructor(RascalValueFactory.Symbol_Lit, $RVF.string(",")), $RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")))));
       $T2 = $RTF.nonTerminalType($RVF.constructor(RascalValueFactory.Symbol_IterStarSeps, $RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Sym")), $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Layouts, $RVF.string("LAYOUTLIST")))));
       $T3 = $TF.setType(ADT_Condition);
       ADT_Mapping_Expression = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Expression });
       ADT_KeywordArguments_1 = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { $T11 });
       ADT_Mapping_Pattern = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Pattern });
       ADT_KeywordArguments_Expression = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Expression });
       ADT_Mapping_1 = $TF.abstractDataType($TS, "Mapping", new Type[] { $T11 });
       ADT_KeywordArgument_1 = $TF.abstractDataType($TS, "KeywordArgument", new Type[] { $T11 });
       ADT_KeywordArguments_Pattern = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Pattern });
    
       
       
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
    public IBool isTypeVar(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isTypeVar($P0);
    }
    public IConstructor sym2symbol(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isNonTerminal($P0Type, M_lang_rascal_syntax_Rascal.NT_Sym)){
         $result = (IConstructor)lang_rascal_grammar_definition_Symbols_sym2symbol$dfa495965feb0c03((ITree) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList separgs2symbols(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IList)lang_rascal_grammar_definition_Symbols_separgs2symbols$b28c720b0d5f3bbd((ITree) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor priority(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_ParseTree.priority($P0, $P1);
    }
    public IBool match(IValue $P0, IValue $P1){ // Generated by Resolver
       IBool $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Symbol)){
         $result = (IBool)lang_rascal_grammar_definition_Symbols_match$81f02fcbec53ce11((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IList args2symbols(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T2)){
         $result = (IList)lang_rascal_grammar_definition_Symbols_args2symbols$df41e76e71525e83((ITree) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor complement(IValue $P0){ // Generated by Resolver
       return (IConstructor) M_lang_rascal_grammar_definition_Characters.complement($P0);
    }
    public IBool isNodeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNodeType($P0);
    }
    public INode conditional(IValue $P0, IValue $P1){ // Generated by Resolver
       INode $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type,$T3)){
         $result = (INode)lang_rascal_grammar_definition_Symbols_conditional$f9ac60504818807f((IConstructor) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type,$T3)){
         $result = (INode)lang_rascal_grammar_definition_Symbols_conditional$a78f69e7726562ef((IConstructor) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isNonTerminal($P0Type, M_lang_rascal_syntax_Rascal.NT_Expression) && $isSubtypeOf($P1Type,$T4)){
         return $RVF.constructor(M_lang_rascal_syntax_Rascal.Replacement_conditional_Expression_iter_seps_Expression, new IValue[]{(ITree) $P0, (ITree) $P1});
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type,$T3)){
         return $RVF.constructor(M_ParseTree.Symbol_conditional_Symbol_set_Condition, new IValue[]{(IConstructor) $P0, (ISet) $P1});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isReifiedType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isReifiedType($P0);
    }
    public IConstructor alt(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T5)){
         $result = (IConstructor)lang_rascal_grammar_definition_Symbols_alt$01fd93bf17a1bf85((ISet) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T5)){
         return $RVF.constructor(M_ParseTree.Symbol_alt_set_Symbol, new IValue[]{(ISet) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isRelType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isRelType($P0);
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
    public IList delete(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.delete($P0, $P1);
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
    public IConstructor delabel(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IConstructor)lang_rascal_grammar_definition_Symbols_delabel$b76df962b72ed5c5((IConstructor) $P0);
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
       return (IString) M_lang_rascal_grammar_definition_Literals.unescapeLiteral($P0);
    }
    public IBool isNumType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNumType($P0);
    }
    public IBool isTupleType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isTupleType($P0);
    }
    public IConstructor striprec(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IConstructor)lang_rascal_grammar_definition_Symbols_striprec$2adcb69e86cd0492((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isBagType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isBagType($P0);
    }
    public IBool isVoidType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isVoidType($P0);
    }
    public IConstructor strip(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       switch(Fingerprint.getFingerprint($P0)){
       	
       case 1643638592:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IConstructor)lang_rascal_grammar_definition_Symbols_strip$9567130339c14a87((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;	
       case -2144737184:
       		if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
       		  $result = (IConstructor)lang_rascal_grammar_definition_Symbols_strip$cbd5896823d6fc07((IConstructor) $P0);
       		  if($result != null) return $result;
       		}
       		break;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IConstructor)lang_rascal_grammar_definition_Symbols_strip$b22f60b98614c4fe((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
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
    public IConstructor difference(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_lang_rascal_grammar_definition_Characters.difference($P0, $P1);
    }
    public IValue sort(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T6)){
         $result = (IValue)M_List.List_sort$1fe4426c8c8039da((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T9)){
         return $RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{(IString) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList sort(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.sort($P0, $P1);
    }
    public IConstructor seq(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T10)){
         $result = (IConstructor)lang_rascal_grammar_definition_Symbols_seq$5dde90ea795fac79((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T10)){
         return $RVF.constructor(M_ParseTree.Symbol_seq_list_Symbol, new IValue[]{(IList) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isIntType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isIntType($P0);
    }
    public IBool isDateTimeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isDateTimeType($P0);
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Symbols.rsc|(531,157,<18,0>,<21,2>) 
    public IConstructor lang_rascal_grammar_definition_Symbols_striprec$2adcb69e86cd0492(IConstructor s_ori_0){ 
        
        
        try {
            IValue $visitResult = $TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{$TF.listType(ADT_Symbol), M_Type.ADT_Exception, $TF.setType(ADT_Symbol), $TF.setType(ADT_Condition), M_lang_rascal_syntax_Rascal.ADT_KeywordArguments_1, M_ParseTree.ADT_Tree, M_ParseTree.ADT_TreeSearchResult_1, M_ParseTree.ADT_Condition, M_ParseTree.ADT_Production, M_ParseTree.ADT_Symbol, ADT_Grammar, M_ParseTree.ADT_CharRange, ADT_Item, ADT_GrammarModule, $TF.listType(ADT_CharRange), ADT_GrammarDefinition}, 
                                          new io.usethesource.vallang.IConstructor[]{}, 
                                          $RVF.bool(false)),
                 s_ori_0,
                 (IVisitFunction) (IValue $VISIT0_subject, TraversalState $traversalState) -> {
                     VISIT0:switch(Fingerprint.getFingerprint($VISIT0_subject)){
                     
                         case 1643638592:
                             if($isSubtypeOf($VISIT0_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_1643638592_0: 
                                    do {
                                        if($has_type_and_arity($VISIT0_subject, M_Type.Symbol_label_str_Symbol, 2)){
                                           IValue $arg0_2 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),0));
                                           if($isComparable($arg0_2.getType(), $T9)){
                                              IValue $arg1_1 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),1));
                                              if($isComparable($arg1_1.getType(), M_ParseTree.ADT_Symbol)){
                                                 ValueRef<IConstructor> s_1 = new ValueRef<IConstructor>();
                                                 IConstructor $replacement0 = (IConstructor)($me.strip(((IConstructor)($arg1_1))));
                                                 if($isSubtypeOf($replacement0.getType(),$VISIT0_subject.getType())){
                                                    $traversalState.setMatchedAndChanged(true, true);
                                                    return $replacement0;
                                                 
                                                 } else {
                                                    break VISIT0;// switch
                                                 
                                                 }
                                              }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                         case -2144737184:
                             if($isSubtypeOf($VISIT0_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_2144737184_1: 
                                    do {
                                        if($has_type_and_arity($VISIT0_subject, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
                                           IValue $arg0_5 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),0));
                                           if($isComparable($arg0_5.getType(), M_ParseTree.ADT_Symbol)){
                                              ValueRef<IConstructor> s_2 = new ValueRef<IConstructor>();
                                              IValue $arg1_4 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),1));
                                              if($isComparable($arg1_4.getType(), $T3)){
                                                 IConstructor $replacement3 = (IConstructor)($me.strip(((IConstructor)($arg0_5))));
                                                 if($isSubtypeOf($replacement3.getType(),$VISIT0_subject.getType())){
                                                    $traversalState.setMatchedAndChanged(true, true);
                                                    return $replacement3;
                                                 
                                                 } else {
                                                    break VISIT0;// switch
                                                 
                                                 }
                                              }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                     
                     }
                     return $VISIT0_subject;
                 });
            return (IConstructor)$visitResult;
        
        } catch (ReturnFromTraversalException e) {
            return (IConstructor) e.getValue();
        }
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Symbols.rsc|(767,48,<23,0>,<23,48>) 
    public IConstructor lang_rascal_grammar_definition_Symbols_strip$9567130339c14a87(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_Type.Symbol_label_str_Symbol, 2)){
           IValue $arg0_7 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_7.getType(), $T9)){
              IValue $arg1_6 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_6.getType(), M_ParseTree.ADT_Symbol)){
                 IConstructor s_0 = null;
                 return ((IConstructor)($me.strip(((IConstructor)($arg1_6)))));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Symbols.rsc|(816,65,<24,0>,<24,65>) 
    public IConstructor lang_rascal_grammar_definition_Symbols_strip$cbd5896823d6fc07(IConstructor $0){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
           IValue $arg0_9 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_9.getType(), M_ParseTree.ADT_Symbol)){
              IConstructor s_0 = null;
              IValue $arg1_8 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_8.getType(), $T3)){
                 return ((IConstructor)($me.strip(((IConstructor)($arg0_9)))));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Symbols.rsc|(882,35,<25,0>,<25,35>) 
    public IConstructor lang_rascal_grammar_definition_Symbols_strip$b22f60b98614c4fe(IConstructor s_0){ 
        
        
        return ((IConstructor)s_0);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Symbols.rsc|(919,271,<27,0>,<34,1>) 
    public IBool lang_rascal_grammar_definition_Symbols_match$81f02fcbec53ce11(IConstructor checked_0, IConstructor referenced_1){ 
        
        
        /*muExists*/WHILE1_BT: 
            do {
                WHILE1:
                    while((((IBool)(($is(((IConstructor)checked_0),((IString)$constants.get(0)/*"conditional"*/)) ? ((IBool)$constants.get(1)/*true*/) : $RVF.bool($is(((IConstructor)checked_0),((IString)$constants.get(2)/*"label"*/))))))).getValue()){
                        checked_0 = ((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)checked_0), "symbol")))));
                
                    }
        
            } while(false);
        /* void:  muCon([]) *//*muExists*/WHILE2_BT: 
            do {
                WHILE2:
                    while((((IBool)(($is(((IConstructor)referenced_1),((IString)$constants.get(0)/*"conditional"*/)) ? ((IBool)$constants.get(1)/*true*/) : $RVF.bool($is(((IConstructor)referenced_1),((IString)$constants.get(2)/*"label"*/))))))).getValue()){
                        referenced_1 = ((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)referenced_1), "symbol")))));
                
                    }
        
            } while(false);
        /* void:  muCon([]) */return ((IBool)($equal(((IConstructor)referenced_1), ((IConstructor)checked_0))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Symbols.rsc|(1193,68,<36,0>,<36,68>) 
    public IConstructor lang_rascal_grammar_definition_Symbols_delabel$b76df962b72ed5c5(IConstructor s_0){ 
        
        
        try {
            IValue $visitResult = $TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{$TF.listType(ADT_Symbol), M_Type.ADT_Exception, $TF.setType(ADT_Symbol), $TF.setType(ADT_Condition), M_lang_rascal_syntax_Rascal.ADT_KeywordArguments_1, M_ParseTree.ADT_Tree, M_ParseTree.ADT_TreeSearchResult_1, M_ParseTree.ADT_Condition, M_ParseTree.ADT_Production, M_ParseTree.ADT_Symbol, ADT_Grammar, M_ParseTree.ADT_CharRange, ADT_Item, ADT_GrammarModule, $TF.listType(ADT_CharRange), ADT_GrammarDefinition}, 
                                          new io.usethesource.vallang.IConstructor[]{}, 
                                          $RVF.bool(false)),
                 s_0,
                 (IVisitFunction) (IValue $VISIT3_subject, TraversalState $traversalState) -> {
                     VISIT3:switch(Fingerprint.getFingerprint($VISIT3_subject)){
                     
                         case 1643638592:
                             if($isSubtypeOf($VISIT3_subject.getType(),M_ParseTree.ADT_Symbol)){
                                /*muExists*/CASE_1643638592_0: 
                                    do {
                                        if($has_type_and_arity($VISIT3_subject, M_Type.Symbol_label_str_Symbol, 2)){
                                           IValue $arg0_14 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT3_subject)),0));
                                           if($isComparable($arg0_14.getType(), $T7)){
                                              IValue $arg1_13 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT3_subject)),1));
                                              if($isComparable($arg1_13.getType(), M_ParseTree.ADT_Symbol)){
                                                 ValueRef<IConstructor> t_1 = new ValueRef<IConstructor>();
                                                 IConstructor $replacement12 = (IConstructor)($arg1_13);
                                                 if($isSubtypeOf($replacement12.getType(),$VISIT3_subject.getType())){
                                                    $traversalState.setMatchedAndChanged(true, true);
                                                    return $replacement12;
                                                 
                                                 } else {
                                                    break VISIT3;// switch
                                                 
                                                 }
                                              }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                     
                     }
                     return $VISIT3_subject;
                 });
            return (IConstructor)$visitResult;
        
        } catch (ReturnFromTraversalException e) {
            return (IConstructor) e.getValue();
        }
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Symbols.rsc|(1263,2503,<38,0>,<93,1>) 
    public IConstructor lang_rascal_grammar_definition_Symbols_sym2symbol$dfa495965feb0c03(ITree sym_0){ 
        
        
        final ITree $switchVal15 = ((ITree)sym_0);
        boolean noCaseMatched_$switchVal15 = true;
        SWITCH4: switch(Fingerprint.getFingerprint($switchVal15)){
        
            case 0:
                if(noCaseMatched_$switchVal15){
                    noCaseMatched_$switchVal15 = false;
                    
                }
                
        
            default: if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_0: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "nonterminal", 1)){
                                   IValue $arg0_18 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_18.getType(), M_lang_rascal_syntax_Rascal.NT_Nonterminal)){
                                      ITree n_1 = null;
                                      final Template $template17 = (Template)new Template($RVF, "");
                                      $template17.addVal($arg0_18);
                                      return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)($template17.close()))})));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_1: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "start", 1)){
                                   IValue $arg0_20 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_20.getType(), M_lang_rascal_syntax_Rascal.NT_Nonterminal)){
                                      ITree n_2 = null;
                                      final Template $template19 = (Template)new Template($RVF, "");
                                      $template19.addVal($arg0_20);
                                      return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_start_Symbol, new IValue[]{((IConstructor)($RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{((IString)($template19.close()))})))})));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_2: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "literal", 1)){
                                   IValue $arg0_21 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_21.getType(), M_lang_rascal_syntax_Rascal.NT_StringConstant)){
                                      ITree l_3 = ((ITree)($arg0_21));
                                      return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_lit_str, new IValue[]{((IString)(M_lang_rascal_grammar_definition_Literals.unescapeLiteral(((ITree)($arg0_21)))))})));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_3: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "caseInsensitiveLiteral", 1)){
                                   IValue $arg0_22 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_22.getType(), M_lang_rascal_syntax_Rascal.NT_CaseInsensitiveStringConstant)){
                                      ITree l_4 = ((ITree)($arg0_22));
                                      return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_cilit_str, new IValue[]{((IString)(M_lang_rascal_grammar_definition_Literals.unescapeLiteral(((ITree)($arg0_22)))))})));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_4: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "parametrized", 2)){
                                   IValue $arg0_25 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_25.getType(), M_lang_rascal_syntax_Rascal.NT_Nonterminal)){
                                      ITree n_5 = ((ITree)($arg0_25));
                                      IValue $arg1_24 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(4)/*1*/).intValue()));
                                      if($isComparable($arg1_24.getType(), $T0)){
                                         if(org.rascalmpl.values.parsetrees.TreeAdapter.getArgs((ITree)$arg1_24).length() >= 1){
                                            ITree syms_6 = ((ITree)($arg1_24));
                                            final Template $template23 = (Template)new Template($RVF, "");
                                            $template23.addVal($arg0_25);
                                            return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_parameterized_sort_str_list_Symbol, new IValue[]{((IString)($template23.close())), ((IList)($me.separgs2symbols(((ITree)($arg1_24)))))})));
                                         
                                         }
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_5: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "labeled", 2)){
                                   IValue $arg0_28 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_28.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree s_7 = ((ITree)($arg0_28));
                                      IValue $arg1_27 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(4)/*1*/).intValue()));
                                      if($isComparable($arg1_27.getType(), M_lang_rascal_syntax_Rascal.NT_NonterminalLabel)){
                                         ITree n_8 = ((ITree)($arg1_27));
                                         final Template $template26 = (Template)new Template($RVF, "");
                                         $template26.addVal($arg1_27);
                                         return ((IConstructor)($RVF.constructor(M_Type.Symbol_label_str_Symbol, new IValue[]{((IString)($template26.close())), ((IConstructor)($me.sym2symbol(((ITree)($arg0_28)))))})));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_6: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "optional", 1)){
                                   IValue $arg0_29 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_29.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree s_9 = ((ITree)($arg0_29));
                                      return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_opt_Symbol, new IValue[]{((IConstructor)($me.sym2symbol(((ITree)($arg0_29)))))})));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_7: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "characterClass", 1)){
                                   IValue $arg0_30 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_30.getType(), M_lang_rascal_syntax_Rascal.NT_Class)){
                                      ITree cc_10 = ((ITree)($arg0_30));
                                      return ((IConstructor)(M_lang_rascal_grammar_definition_Characters.cc2ranges(((ITree)($arg0_30)))));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_8: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "parameter", 1)){
                                   IValue $arg0_32 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_32.getType(), M_lang_rascal_syntax_Rascal.NT_Nonterminal)){
                                      ITree n_11 = null;
                                      final Template $template31 = (Template)new Template($RVF, "");
                                      $template31.addVal($arg0_32);
                                      return ((IConstructor)($RVF.constructor(M_Type.Symbol_parameter_str_Symbol, new IValue[]{((IString)($template31.close())), ((IConstructor)$constants.get(5)/*adt("Tree",[])*/)})));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_9: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "empty", 0)){
                                   return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_empty_, new IValue[]{})));
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_10: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "alternative", 2)){
                                   IValue $arg0_40 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_40.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree first_12 = ((ITree)($arg0_40));
                                      IValue $arg1_39 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(4)/*1*/).intValue()));
                                      if($isComparable($arg1_39.getType(), $T13)){
                                         if(org.rascalmpl.values.parsetrees.TreeAdapter.getArgs((ITree)$arg1_39).length() >= 1){
                                            ITree alts_13 = ((ITree)($arg1_39));
                                            final ISetWriter $setwriter33 = (ISetWriter)$RVF.setWriter();
                                            ;
                                            final ITree $exp36 = ((ITree)($arg1_39));
                                            final int $last37 = (int)((ITree)($exp36)).getArgs().length() - 1;
                                            $SCOMP34_GEN2280:
                                            
                                            for(int $i38 = 0; $i38 <= $last37; $i38 += 4){
                                               final ITree $elem35 = ((ITree)($iter_subscript($exp36, $i38)));
                                               ITree elem_14 = ((ITree)($elem35));
                                               $setwriter33.insert($me.sym2symbol(((ITree)elem_14)));
                                            
                                            }
                                            
                                            return ((IConstructor)($me.alt(((ISet)($aset_add_aset(((ISet)($RVF.set(((IConstructor)($me.sym2symbol(((ITree)($arg0_40)))))))),((ISet)($setwriter33.done()))))))));
                                         
                                         }
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_11: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "iterStar", 1)){
                                   IValue $arg0_41 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_41.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree s_15 = ((ITree)($arg0_41));
                                      return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_iter_star_Symbol, new IValue[]{((IConstructor)($me.sym2symbol(((ITree)($arg0_41)))))})));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_12: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "iter", 1)){
                                   IValue $arg0_42 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_42.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree s_16 = ((ITree)($arg0_42));
                                      return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_iter_Symbol, new IValue[]{((IConstructor)($me.sym2symbol(((ITree)($arg0_42)))))})));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_13: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "iterStarSep", 2)){
                                   IValue $arg0_44 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_44.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree s_17 = ((ITree)($arg0_44));
                                      IValue $arg1_43 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(4)/*1*/).intValue()));
                                      if($isComparable($arg1_43.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                         ITree sep_18 = ((ITree)($arg1_43));
                                         return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_iter_star_seps_Symbol_list_Symbol, new IValue[]{((IConstructor)($me.sym2symbol(((ITree)($arg0_44))))), ((IList)($RVF.list(((IConstructor)($me.sym2symbol(((ITree)($arg1_43))))))))})));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_14: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "iterSep", 2)){
                                   IValue $arg0_46 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_46.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree s_19 = ((ITree)($arg0_46));
                                      IValue $arg1_45 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(4)/*1*/).intValue()));
                                      if($isComparable($arg1_45.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                         ITree sep_20 = ((ITree)($arg1_45));
                                         return ((IConstructor)($RVF.constructor(M_ParseTree.Symbol_iter_seps_Symbol_list_Symbol, new IValue[]{((IConstructor)($me.sym2symbol(((ITree)($arg0_46))))), ((IList)($RVF.list(((IConstructor)($me.sym2symbol(((ITree)($arg1_45))))))))})));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_15: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "sequence", 2)){
                                   IValue $arg0_54 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_54.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree first_21 = ((ITree)($arg0_54));
                                      IValue $arg1_53 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(4)/*1*/).intValue()));
                                      if($isComparable($arg1_53.getType(), $T15)){
                                         if(org.rascalmpl.values.parsetrees.TreeAdapter.getArgs((ITree)$arg1_53).length() >= 1){
                                            ITree sequence_22 = ((ITree)($arg1_53));
                                            final IListWriter $listwriter47 = (IListWriter)$RVF.listWriter();
                                            final ITree $exp50 = ((ITree)($arg1_53));
                                            final int $last51 = (int)((ITree)($exp50)).getArgs().length() - 1;
                                            $LCOMP48_GEN2764:
                                            
                                            for(int $i52 = 0; $i52 <= $last51; $i52 += 2){
                                               final ITree $elem49 = ((ITree)($iter_subscript($exp50, $i52)));
                                               ITree elem_23 = ((ITree)($elem49));
                                               $listwriter47.append($me.sym2symbol(((ITree)elem_23)));
                                            
                                            }
                                            
                                            return ((IConstructor)($me.seq(((IList)($alist_add_alist(((IList)($RVF.list(((IConstructor)($me.sym2symbol(((ITree)($arg0_54)))))))),((IList)($listwriter47.done()))))))));
                                         
                                         }
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_16: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "startOfLine", 1)){
                                   IValue $arg0_55 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_55.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree s_24 = ((ITree)($arg0_55));
                                      return ((IConstructor)($me.conditional(((IConstructor)($me.sym2symbol(((ITree)($arg0_55))))), ((ISet)$constants.get(6)/*{\begin-of-line()}*/))));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_17: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "endOfLine", 1)){
                                   IValue $arg0_56 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_56.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree s_25 = ((ITree)($arg0_56));
                                      return ((IConstructor)($me.conditional(((IConstructor)($me.sym2symbol(((ITree)($arg0_56))))), ((ISet)$constants.get(7)/*{\end-of-line()}*/))));
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_18: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "column", 2)){
                                   IValue $arg0_59 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_59.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree s_26 = ((ITree)($arg0_59));
                                      IValue $arg1_58 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(4)/*1*/).intValue()));
                                      if($isComparable($arg1_58.getType(), M_lang_rascal_syntax_Rascal.NT_IntegerLiteral)){
                                         ITree i_27 = ((ITree)($arg1_58));
                                         final Template $template57 = (Template)new Template($RVF, "");
                                         $template57.addVal($arg1_58);
                                         return ((IConstructor)($me.conditional(((IConstructor)($me.sym2symbol(((ITree)($arg0_59))))), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Condition_at_column_int, new IValue[]{((IInteger)(M_String.toInt(((IString)($template57.close())))))})))))))));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_19: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "follow", 2)){
                                   IValue $arg0_61 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_61.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree s_28 = ((ITree)($arg0_61));
                                      IValue $arg1_60 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(4)/*1*/).intValue()));
                                      if($isComparable($arg1_60.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                         ITree r_29 = ((ITree)($arg1_60));
                                         return ((IConstructor)($me.conditional(((IConstructor)($me.sym2symbol(((ITree)($arg0_61))))), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Condition_follow_Symbol, new IValue[]{((IConstructor)($me.sym2symbol(((ITree)($arg1_60)))))})))))))));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_20: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "notFollow", 2)){
                                   IValue $arg0_63 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_63.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree s_30 = ((ITree)($arg0_63));
                                      IValue $arg1_62 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(4)/*1*/).intValue()));
                                      if($isComparable($arg1_62.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                         ITree r_31 = ((ITree)($arg1_62));
                                         return ((IConstructor)($me.conditional(((IConstructor)($me.sym2symbol(((ITree)($arg0_63))))), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Condition_not_follow_Symbol, new IValue[]{((IConstructor)($me.sym2symbol(((ITree)($arg1_62)))))})))))))));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_21: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "precede", 2)){
                                   IValue $arg0_65 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_65.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree s_32 = ((ITree)($arg0_65));
                                      IValue $arg1_64 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(4)/*1*/).intValue()));
                                      if($isComparable($arg1_64.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                         ITree r_33 = ((ITree)($arg1_64));
                                         return ((IConstructor)($me.conditional(((IConstructor)($me.sym2symbol(((ITree)($arg1_64))))), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Condition_precede_Symbol, new IValue[]{((IConstructor)($me.sym2symbol(((ITree)($arg0_65)))))})))))))));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_22: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "notPrecede", 2)){
                                   IValue $arg0_67 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_67.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree s_34 = ((ITree)($arg0_67));
                                      IValue $arg1_66 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(4)/*1*/).intValue()));
                                      if($isComparable($arg1_66.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                         ITree r_35 = ((ITree)($arg1_66));
                                         return ((IConstructor)($me.conditional(((IConstructor)($me.sym2symbol(((ITree)($arg1_66))))), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Condition_not_precede_Symbol, new IValue[]{((IConstructor)($me.sym2symbol(((ITree)($arg0_67)))))})))))))));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_23: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "unequal", 2)){
                                   IValue $arg0_69 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_69.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree s_36 = ((ITree)($arg0_69));
                                      IValue $arg1_68 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(4)/*1*/).intValue()));
                                      if($isComparable($arg1_68.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                         ITree r_37 = ((ITree)($arg1_68));
                                         return ((IConstructor)($me.conditional(((IConstructor)($me.sym2symbol(((ITree)($arg0_69))))), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Condition_delete_Symbol, new IValue[]{((IConstructor)($me.sym2symbol(((ITree)($arg1_68)))))})))))))));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     if($isSubtypeOf($switchVal15.getType(),M_lang_rascal_syntax_Rascal.NT_Sym)){
                        /*muExists*/CASE_0_24: 
                            do {
                                if($nonterminal_has_name_and_arity($switchVal15, "except", 2)){
                                   IValue $arg0_72 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(3)/*0*/).intValue()));
                                   if($isComparable($arg0_72.getType(), M_lang_rascal_syntax_Rascal.NT_Sym)){
                                      ITree s_38 = ((ITree)($arg0_72));
                                      IValue $arg1_71 = (IValue)($nonterminal_get_arg(((ITree)((ITree)($switchVal15))), ((IInteger)$constants.get(4)/*1*/).intValue()));
                                      if($isComparable($arg1_71.getType(), M_lang_rascal_syntax_Rascal.NT_NonterminalLabel)){
                                         ITree n_39 = ((ITree)($arg1_71));
                                         final Template $template70 = (Template)new Template($RVF, "");
                                         $template70.addVal($arg1_71);
                                         return ((IConstructor)($me.conditional(((IConstructor)($me.sym2symbol(((ITree)($arg0_72))))), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Condition_except_str, new IValue[]{((IString)($template70.close()))})))))))));
                                      
                                      }
                                   
                                   }
                                
                                }
                        
                            } while(false);
                     
                     }
                     final Template $template16 = (Template)new Template($RVF, "sym2symbol, missed a case ");
                     $template16.beginIndent("                          ");
                     $template16.addVal(sym_0);
                     $template16.endIndent("                          ");
                     throw new Throw($template16.close());
        }
        
                   
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Symbols.rsc|(3768,89,<95,0>,<97,1>) 
    public IList lang_rascal_grammar_definition_Symbols_args2symbols$df41e76e71525e83(ITree args_0){ 
        
        
        final IListWriter $listwriter73 = (IListWriter)$RVF.listWriter();
        final ITree $exp76 = ((ITree)args_0);
        final int $last77 = (int)((ITree)($exp76)).getArgs().length() - 1;
        $LCOMP74_GEN3840:
        
        for(int $i78 = 0; $i78 <= $last77; $i78 += 2){
           final ITree $elem75 = ((ITree)($iter_subscript($exp76, $i78)));
           ITree s_1 = ((ITree)($elem75));
           $listwriter73.append($me.sym2symbol(((ITree)s_1)));
        
        }
        
        return ((IList)($listwriter73.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Symbols.rsc|(3859,98,<99,0>,<101,1>) 
    public IList lang_rascal_grammar_definition_Symbols_separgs2symbols$b28c720b0d5f3bbd(ITree args_0){ 
        
        
        final IListWriter $listwriter79 = (IListWriter)$RVF.listWriter();
        final ITree $exp82 = ((ITree)args_0);
        final int $last83 = (int)((ITree)($exp82)).getArgs().length() - 1;
        $LCOMP80_GEN3940:
        
        for(int $i84 = 0; $i84 <= $last83; $i84 += 4){
           final ITree $elem81 = ((ITree)($iter_subscript($exp82, $i84)));
           ITree s_1 = ((ITree)($elem81));
           $listwriter79.append($me.sym2symbol(((ITree)s_1)));
        
        }
        
        return ((IList)($listwriter79.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Symbols.rsc|(4003,83,<104,0>,<104,83>) 
    public IConstructor lang_rascal_grammar_definition_Symbols_seq$5dde90ea795fac79(IList $0){ 
        
        
        /*muExists*/seq: 
            do {
                final IList $subject85 = ((IList)$0);
                int $subject85_cursor = 0;
                if($isSubtypeOf($subject85.getType(),$T10)){
                   final int $subject85_len = (int)((IList)($subject85)).length();
                   if($subject85_len >= 1){
                      final int $a_089_start = (int)$subject85_cursor;
                      seq_LIST_MVARa:
                      
                      for(int $a_089_len = 0; $a_089_len <= $subject85_len - $a_089_start - 1; $a_089_len += 1){
                         IList a_0 = ((IList)($subject85.sublist($a_089_start, $a_089_len)));
                         $subject85_cursor = $a_089_start + $a_089_len;
                         final IConstructor $subject87 = ((IConstructor)($alist_subscript_int(((IList)($subject85)),$subject85_cursor)));
                         if($has_type_and_arity($subject87, M_ParseTree.Symbol_seq_list_Symbol, 1)){
                            IValue $arg0_88 = (IValue)($aadt_subscript_int(((IConstructor)($subject87)),0));
                            if($isComparable($arg0_88.getType(), $T10)){
                               if(true){
                                  IList b_1 = null;
                                  $subject85_cursor += 1;
                                  final int $c_286_start = (int)$subject85_cursor;
                                  seq_LIST_MVARa_CONS_seq_MVARc:
                                  
                                  for(int $c_286_len = 0; $c_286_len <= $subject85_len - $c_286_start - 0; $c_286_len += 1){
                                     IList c_2 = ((IList)($subject85.sublist($c_286_start, $c_286_len)));
                                     $subject85_cursor = $c_286_start + $c_286_len;
                                     if($subject85_cursor == $subject85_len){
                                        return ((IConstructor)($me.seq(((IList)($alist_add_alist(((IList)($alist_add_alist(((IList)a_0),((IList)($arg0_88))))),((IList)c_2)))))));
                                     
                                     } else {
                                        continue seq_LIST_MVARa_CONS_seq_MVARc;/*list match1*/
                                     }
                                  }
                                  continue seq_LIST_MVARa;/*computeFail*/
                               
                               } else {
                                  continue seq_LIST_MVARa;/*computeFail*/
                               }
                            } else {
                               continue seq_LIST_MVARa;/*computeFail*/
                            }
                         } else {
                            continue seq_LIST_MVARa;/*computeFail*/
                         }
                      }
                      return null;
                   
                   } else {
                      return null;
                   }
                } else {
                   return null;
                }
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Symbols.rsc|(4088,67,<106,0>,<106,67>) 
    public IConstructor lang_rascal_grammar_definition_Symbols_alt$01fd93bf17a1bf85(ISet $0){ 
        
        
        /*muExists*/alt: 
            do {
                ISet $subject90 = (ISet)($0);
                alt_SET_MVARa:
                for(IValue $elem97_for : new SubSetGenerator(((ISet)($subject90)))){
                    ISet $elem97 = (ISet) $elem97_for;
                    ISet a_0 = ((ISet)($elem97));
                    final ISet $subject92 = ((ISet)(((ISet)($subject90)).subtract(((ISet)($elem97)))));
                    alt_SET_MVARa_CONS_alt$_DFLT_SET_ELM95:
                    for(IValue $elem94_for : ((ISet)($subject92))){
                        IConstructor $elem94 = (IConstructor) $elem94_for;
                        if($has_type_and_arity($elem94, M_ParseTree.Symbol_alt_set_Symbol, 1)){
                           IValue $arg0_96 = (IValue)($aadt_subscript_int(((IConstructor)($elem94)),0));
                           if($isComparable($arg0_96.getType(), $T5)){
                              if(true){
                                 ISet b_1 = null;
                                 final ISet $subject93 = ((ISet)(((ISet)($subject92)).delete(((IConstructor)($elem94)))));
                                 if(((ISet)($subject93)).size() == 0){
                                    return ((IConstructor)($me.alt(((ISet)($aset_add_aset(((ISet)a_0),((ISet)($arg0_96))))))));
                                 
                                 } else {
                                    continue alt_SET_MVARa_CONS_alt$_DFLT_SET_ELM95;/*redirected alt_SET_MVARa_CONS_alt to alt_SET_MVARa_CONS_alt$_DFLT_SET_ELM95; set pat3*/
                                 }
                              } else {
                                 continue alt_SET_MVARa_CONS_alt$_DFLT_SET_ELM95;/*default set elem*/
                              }
                           } else {
                              continue alt_SET_MVARa_CONS_alt$_DFLT_SET_ELM95;/*default set elem*/
                           }
                        } else {
                           continue alt_SET_MVARa_CONS_alt$_DFLT_SET_ELM95;/*default set elem*/
                        }
                    }
                    continue alt_SET_MVARa;/*set pat4*/
                                
                }
                return null;
                            
            } while(false);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Symbols.rsc|(4189,123,<110,0>,<111,31>) 
    public IConstructor lang_rascal_grammar_definition_Symbols_conditional$f9ac60504818807f(IConstructor $0, ISet cs2_2){ 
        
        
        if($has_type_and_arity($0, M_ParseTree.Symbol_conditional_Symbol_set_Condition, 2)){
           IValue $arg0_99 = (IValue)($aadt_subscript_int(((IConstructor)$0),0));
           if($isComparable($arg0_99.getType(), M_ParseTree.ADT_Symbol)){
              IConstructor s_0 = null;
              IValue $arg1_98 = (IValue)($aadt_subscript_int(((IConstructor)$0),1));
              if($isComparable($arg1_98.getType(), $T3)){
                 ISet cs1_1 = null;
                 return ((IConstructor)($me.conditional(((IConstructor)($arg0_99)), ((ISet)($aset_add_aset(((ISet)($arg1_98)),((ISet)cs2_2)))))));
              
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/definition/Symbols.rsc|(4314,353,<113,0>,<119,1>) 
    public IConstructor lang_rascal_grammar_definition_Symbols_conditional$a78f69e7726562ef(IConstructor s_0, ISet cs_1){ 
        
        
        /*muExists*/IF5: 
            do {
                IF5_GEN4495:
                for(IValue $elem101_for : ((ISet)cs_1)){
                    IConstructor $elem101 = (IConstructor) $elem101_for;
                    IConstructor c_2 = null;
                    if($aadt_has_field(((IConstructor)($elem101)),"symbol")){
                       if($is(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($elem101)), "symbol"))))),((IString)$constants.get(0)/*"conditional"*/))){
                          final ISetWriter $writer100 = (ISetWriter)$RVF.setWriter();
                          ;
                          $writer100.insert(((IConstructor)($aadt_field_update("symbol", ((IConstructor)($aadt_get_field(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($elem101)), "symbol"))))), "symbol"))), ((IConstructor)($elem101))))));
                          $setwriter_splice($writer100,((ISet)($aadt_get_field(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)($elem101)), "symbol"))))), "conditions"))));
                          $setwriter_splice($writer100,((ISet)cs_1).subtract(((ISet)($RVF.set(((IConstructor)($elem101)))))));
                          return ((IConstructor)($me.conditional(((IConstructor)s_0), ((ISet)($writer100.done())))));
                       
                       } else {
                          continue IF5_GEN4495;
                       }
                    } else {
                       continue IF5_GEN4495;
                    }
                }
                
                            
            } while(false);
        return null;
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `lang::rascal::grammar::definition::Symbols`");
    }
}