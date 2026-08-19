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
public class $Lookahead 
    extends
        org.rascalmpl.runtime.$RascalModule
    implements 
    	rascal.lang.rascal.grammar.$Lookahead_$I {

    private final $Lookahead_$I $me;
    private final IList $constants;
    
    
    public final rascal.$IO M_IO;
    public final rascal.lang.rascal.grammar.definition.$Characters M_lang_rascal_grammar_definition_Characters;
    public final rascal.$ParseTree M_ParseTree;
    public final rascal.lang.rascal.grammar.definition.$Productions M_lang_rascal_grammar_definition_Productions;
    public final rascal.$List M_List;
    public final rascal.$Grammar M_Grammar;
    public final rascal.$Message M_Message;
    public final rascal.lang.rascal.grammar.definition.$Regular M_lang_rascal_grammar_definition_Regular;
    public final rascal.$Type M_Type;

    
    
    public final io.usethesource.vallang.type.Type $T6;	/*avalue()*/
    public final io.usethesource.vallang.type.Type $T10;	/*astr()*/
    public final io.usethesource.vallang.type.Type $T18;	/*avoid()*/
    public final io.usethesource.vallang.type.Type $T13;	/*aparameter("A",avalue(),closed=true)*/
    public final io.usethesource.vallang.type.Type $T7;	/*aparameter("A",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type $T9;	/*aparameter("T",avalue(),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variable;	/*aadt("Variable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Symbol;	/*aadt("Symbol",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type Symbol_eoi_;	/*acons(aadt("Symbol",[],dataSyntax()),[],[],alabel="eoi")*/
    public final io.usethesource.vallang.type.Type ADT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Replacement;	/*aadt("Replacement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Production;	/*aadt("Production",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T16;	/*aset(aadt("Symbol",[],dataSyntax()),alabel="classes")*/
    public final io.usethesource.vallang.type.Type Production_lookahead_Symbol_set_Symbol_Production;	/*acons(aadt("Production",[],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="def"),aset(aadt("Symbol",[],dataSyntax()),alabel="classes"),aadt("Production",[],dataSyntax(),alabel="production")],[],alabel="lookahead")*/
    public final io.usethesource.vallang.type.Type ADT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Name;	/*aadt("Name",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Attr;	/*aadt("Attr",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Expression;	/*aadt("Expression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Expression;	/*aadt("Mapping",[aadt("Expression",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Strategy;	/*aadt("Strategy",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Tree;	/*aadt("Tree",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T11;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_1;	/*aadt("KeywordArguments",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidStringChars;	/*aadt("MidStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolChars;	/*aadt("ProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Range;	/*aadt("Range",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Concrete;	/*aadt("Concrete",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Pattern;	/*aadt("Pattern",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_Pattern;	/*aadt("Mapping",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeType;	/*aadt("LocationChangeType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TagString;	/*aadt("TagString",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Visibility;	/*aadt("Visibility",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionType;	/*aadt("FunctionType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostProtocolChars;	/*aadt("PostProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_IOCapability;	/*aadt("IOCapability",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DatePart;	/*aadt("DatePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RationalLiteral;	/*aadt("RationalLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_SyntaxDefinition;	/*aadt("SyntaxDefinition",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpLiteral;	/*aadt("RegExpLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarModule;	/*aadt("GrammarModule",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Output;	/*aadt("Output",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocationChangeEvent;	/*aadt("LocationChangeEvent",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Prod;	/*aadt("Prod",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Label;	/*aadt("Label",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleParameters;	/*aadt("ModuleParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Item;	/*aadt("Item",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExpModifier;	/*aadt("RegExpModifier",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Case;	/*aadt("Case",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BooleanLiteral;	/*aadt("BooleanLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LAYOUT;	/*aadt("LAYOUT",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type $T12;	/*aparameter("T",aadt("Tree",[],dataSyntax()),closed=false)*/
    public final io.usethesource.vallang.type.Type ADT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ImportedModule;	/*aadt("ImportedModule",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_LocalVariableDeclaration;	/*aadt("LocalVariableDeclaration",[],contextFreeSyntax())*/
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
    public final io.usethesource.vallang.type.Type $T22;	/*abool()*/
    public final io.usethesource.vallang.type.Type $T21;	/*afunc(abool(),[aadt("CharRange",[],dataSyntax(),alabel="r1"),aadt("CharRange",[],dataSyntax(),alabel="r2")],[],returnsViaAllPath=true,abstractFingerprint=0)*/
    public final io.usethesource.vallang.type.Type ADT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renaming;	/*aadt("Renaming",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_1;	/*aadt("Maybe",[aparameter("A",avalue(),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T1;	/*aset(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type $T5;	/*amap(aadt("Symbol",[],dataSyntax()),aset(aadt("Symbol",[],dataSyntax())))*/
    public final io.usethesource.vallang.type.Type ADT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Catch;	/*aadt("Catch",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_GrammarDefinition;	/*aadt("GrammarDefinition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArgument_1;	/*aadt("KeywordArgument",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTarget;	/*aadt("DataTarget",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CharRange;	/*aadt("CharRange",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T4;	/*alist(aadt("CharRange",[],dataSyntax()))*/
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
    public final io.usethesource.vallang.type.Type ADT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormals;	/*aadt("KeywordFormals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_HexIntegerLiteral;	/*aadt("HexIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_HexIntegerLiteral;	/*aadt("HexIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ShellCommand;	/*aadt("ShellCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ShellCommand;	/*aadt("ShellCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TimeZonePart;	/*aadt("TimeZonePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TimeZonePart;	/*aadt("TimeZonePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Declaration;	/*aadt("Declaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Declaration;	/*aadt("Declaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringLiteral;	/*aadt("StringLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreStringChars;	/*aadt("PreStringChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordArguments_Pattern;	/*aadt("KeywordArguments",[aadt("Pattern",[],contextFreeSyntax())],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T8;	/*alist(aparameter("T",avalue(),closed=false))*/
    public final io.usethesource.vallang.type.Type ADT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Tags;	/*aadt("Tags",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NonterminalLabel;	/*aadt("NonterminalLabel",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Mapping_1;	/*aadt("Mapping",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeArg;	/*aadt("TypeArg",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RegExp;	/*aadt("RegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Bound;	/*aadt("Bound",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Signature;	/*aadt("Signature",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathPart;	/*aadt("PathPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Associativity;	/*aadt("Associativity",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Renamings;	/*aadt("Renamings",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_KeywordFormal;	/*aadt("KeywordFormal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalExpression;	/*aadt("OptionalExpression",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ModuleActuals;	/*aadt("ModuleActuals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Nonterminal;	/*aadt("Nonterminal",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_URLChars;	/*aadt("URLChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Condition;	/*aadt("Condition",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Symbol;	/*aadt("Maybe",[aadt("Symbol",[],dataSyntax())],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Class;	/*aadt("Class",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DataTypeSelector;	/*aadt("DataTypeSelector",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T17;	/*alist(avoid())*/
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
    public final io.usethesource.vallang.type.Type $T15;	/*aset(aadt("Production",[],dataSyntax()),alabel="productions")*/
    public final io.usethesource.vallang.type.Type ADT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_TypeVar;	/*aadt("TypeVar",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T14;	/*aset(aadt("Symbol",[],dataSyntax()),alabel="starts")*/
    public final io.usethesource.vallang.type.Type ADT_LocationType;	/*aadt("LocationType",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustDate;	/*aadt("JustDate",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Header;	/*aadt("Header",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OptionalComma;	/*aadt("OptionalComma",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringConstant;	/*aadt("StringConstant",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionDeclaration;	/*aadt("FunctionDeclaration",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T3;	/*aset(aadt("Production",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Import;	/*aadt("Import",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Variant;	/*aadt("Variant",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T23;	/*aset(aadt("Attr",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifiers;	/*aadt("FunctionModifiers",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Comprehension;	/*aadt("Comprehension",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Maybe_Attr;	/*aadt("Maybe",[aadt("Attr",[],dataSyntax())],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcreteHole;	/*aadt("ConcreteHole",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_TreeSearchResult_1;	/*aadt("TreeSearchResult",[aparameter("T",aadt("Tree",[],dataSyntax()),closed=true)],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Message;	/*aadt("Message",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Grammar;	/*aadt("Grammar",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type $T0;	/*alist(aadt("Symbol",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Sym;	/*aadt("Sym",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringMiddle;	/*aadt("StringMiddle",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T20;	/*alist(aadt("Production",[],dataSyntax()))*/
    public final io.usethesource.vallang.type.Type ADT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_RealLiteral;	/*aadt("RealLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateAndTime;	/*aadt("DateAndTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Formals;	/*aadt("Formals",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UserType;	/*aadt("UserType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_QualifiedName;	/*aadt("QualifiedName",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidPathChars;	/*aadt("MidPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolPart;	/*aadt("ProtocolPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolPart;	/*aadt("ProtocolPart",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringTemplate;	/*aadt("StringTemplate",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringTemplate;	/*aadt("StringTemplate",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_RascalKeywords;	/*aadt("RascalKeywords",[],keywordSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedBackslash;	/*aadt("NamedBackslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedBackslash;	/*aadt("NamedBackslash",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type Grammar_simple_set_Symbol_set_Production;	/*acons(aadt("Grammar",[],dataSyntax()),[aset(aadt("Symbol",[],dataSyntax()),alabel="starts"),aset(aadt("Production",[],dataSyntax()),alabel="productions")],[],alabel="simple")*/
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
    public final io.usethesource.vallang.type.Type ADT_RuntimeException;	/*aadt("RuntimeException",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_NamedRegExp;	/*aadt("NamedRegExp",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assoc;	/*aadt("Assoc",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DateTimeLiteral;	/*aadt("DateTimeLiteral",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_DecimalIntegerLiteral;	/*aadt("DecimalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Kind;	/*aadt("Kind",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_MidProtocolChars;	/*aadt("MidProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_OctalIntegerLiteral;	/*aadt("OctalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_OctalIntegerLiteral;	/*aadt("OctalIntegerLiteral",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Parameters;	/*aadt("Parameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathTail;	/*aadt("PathTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type Maybe_Symbol_just_Symbol;	/*acons(aadt("Maybe",[aadt("Symbol",[],dataSyntax())],dataSyntax()),[aadt("Symbol",[],dataSyntax(),alabel="val")],[],alabel="just")*/
    public final io.usethesource.vallang.type.Type ADT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PreProtocolChars;	/*aadt("PreProtocolChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Statement;	/*aadt("Statement",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_StringCharacter;	/*aadt("StringCharacter",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Literal;	/*aadt("Literal",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Toplevel;	/*aadt("Toplevel",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ConcretePart;	/*aadt("ConcretePart",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PathChars;	/*aadt("PathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Exception;	/*aadt("Exception",[],dataSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignment;	/*aadt("Assignment",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Module;	/*aadt("Module",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PatternWithAction;	/*aadt("PatternWithAction",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_Assignable;	/*aadt("Assignable",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type $T2;	/*arel(atypeList([aadt("Symbol",[],dataSyntax()),aadt("Symbol",[],dataSyntax())]))*/
    public final io.usethesource.vallang.type.Type $T19;	/*aset(avoid())*/
    public final io.usethesource.vallang.type.Type ADT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_BasicType;	/*aadt("BasicType",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_CommonKeywordParameters;	/*aadt("CommonKeywordParameters",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProtocolTail;	/*aadt("ProtocolTail",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_EvalCommand;	/*aadt("EvalCommand",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_PostPathChars;	/*aadt("PostPathChars",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_FunctionModifier;	/*aadt("FunctionModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type NT_ProdModifier;	/*aadt("ProdModifier",[],contextFreeSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_JustTime;	/*aadt("JustTime",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type ADT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/
    public final io.usethesource.vallang.type.Type NT_UnicodeEscape;	/*aadt("UnicodeEscape",[],lexicalSyntax())*/

    public $Lookahead(RascalExecutionContext rex){
        this(rex, null);
    }
    
    public $Lookahead(RascalExecutionContext rex, Object extended){
       super(rex);
       this.$me = extended == null ? this : ($Lookahead_$I)extended;
       ModuleStore mstore = rex.getModuleStore();
       mstore.put(rascal.lang.rascal.grammar.$Lookahead.class, this);
       
       mstore.importModule(rascal.$IO.class, rex, rascal.$IO::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Characters.class, rex, rascal.lang.rascal.grammar.definition.$Characters::new);
       mstore.importModule(rascal.$ParseTree.class, rex, rascal.$ParseTree::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Productions.class, rex, rascal.lang.rascal.grammar.definition.$Productions::new);
       mstore.importModule(rascal.$List.class, rex, rascal.$List::new);
       mstore.importModule(rascal.$Grammar.class, rex, rascal.$Grammar::new);
       mstore.importModule(rascal.$Message.class, rex, rascal.$Message::new);
       mstore.importModule(rascal.lang.rascal.grammar.definition.$Regular.class, rex, rascal.lang.rascal.grammar.definition.$Regular::new);
       mstore.importModule(rascal.$Type.class, rex, rascal.$Type::new); 
       
       M_IO = mstore.getModule(rascal.$IO.class);
       M_lang_rascal_grammar_definition_Characters = mstore.getModule(rascal.lang.rascal.grammar.definition.$Characters.class);
       M_ParseTree = mstore.getModule(rascal.$ParseTree.class);
       M_lang_rascal_grammar_definition_Productions = mstore.getModule(rascal.lang.rascal.grammar.definition.$Productions.class);
       M_List = mstore.getModule(rascal.$List.class);
       M_Grammar = mstore.getModule(rascal.$Grammar.class);
       M_Message = mstore.getModule(rascal.$Message.class);
       M_lang_rascal_grammar_definition_Regular = mstore.getModule(rascal.lang.rascal.grammar.definition.$Regular.class);
       M_Type = mstore.getModule(rascal.$Type.class); 
       
                          
       
       $TS.importStore(M_IO.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Characters.$TS);
       $TS.importStore(M_ParseTree.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Productions.$TS);
       $TS.importStore(M_List.$TS);
       $TS.importStore(M_Grammar.$TS);
       $TS.importStore(M_Message.$TS);
       $TS.importStore(M_lang_rascal_grammar_definition_Regular.$TS);
       $TS.importStore(M_Type.$TS);
       
       $constants = readBinaryConstantsFile(this.getClass(), "rascal/lang/rascal/grammar/$Lookahead.constants", 9, "9bc577738fe318ef5f0e2fd2eb751537");
       NT_Variable = $sort("Variable");
       ADT_Variable = $adt("Variable");
       ADT_Symbol = $adt("Symbol");
       NT_Replacement = $sort("Replacement");
       ADT_Replacement = $adt("Replacement");
       ADT_Production = $adt("Production");
       NT_Name = $lex("Name");
       ADT_Name = $adt("Name");
       ADT_Attr = $adt("Attr");
       NT_Expression = $sort("Expression");
       ADT_Expression = $adt("Expression");
       NT_Strategy = $sort("Strategy");
       ADT_Strategy = $adt("Strategy");
       ADT_Tree = $adt("Tree");
       NT_MidStringChars = $lex("MidStringChars");
       ADT_MidStringChars = $adt("MidStringChars");
       NT_ProtocolChars = $lex("ProtocolChars");
       ADT_ProtocolChars = $adt("ProtocolChars");
       NT_Range = $sort("Range");
       ADT_Range = $adt("Range");
       NT_Concrete = $lex("Concrete");
       ADT_Concrete = $adt("Concrete");
       NT_Pattern = $sort("Pattern");
       ADT_Pattern = $adt("Pattern");
       ADT_LocationChangeType = $adt("LocationChangeType");
       NT_TagString = $lex("TagString");
       ADT_TagString = $adt("TagString");
       NT_Visibility = $sort("Visibility");
       ADT_Visibility = $adt("Visibility");
       NT_FunctionType = $sort("FunctionType");
       ADT_FunctionType = $adt("FunctionType");
       NT_PostProtocolChars = $lex("PostProtocolChars");
       ADT_PostProtocolChars = $adt("PostProtocolChars");
       ADT_IOCapability = $adt("IOCapability");
       NT_DatePart = $lex("DatePart");
       ADT_DatePart = $adt("DatePart");
       NT_RationalLiteral = $lex("RationalLiteral");
       ADT_RationalLiteral = $adt("RationalLiteral");
       NT_SyntaxDefinition = $sort("SyntaxDefinition");
       ADT_SyntaxDefinition = $adt("SyntaxDefinition");
       NT_RegExpLiteral = $lex("RegExpLiteral");
       ADT_RegExpLiteral = $adt("RegExpLiteral");
       ADT_GrammarModule = $adt("GrammarModule");
       NT_Output = $lex("Output");
       ADT_Output = $adt("Output");
       ADT_LocationChangeEvent = $adt("LocationChangeEvent");
       NT_Prod = $sort("Prod");
       ADT_Prod = $adt("Prod");
       NT_Label = $sort("Label");
       ADT_Label = $adt("Label");
       NT_ModuleParameters = $sort("ModuleParameters");
       ADT_ModuleParameters = $adt("ModuleParameters");
       ADT_Item = $adt("Item");
       NT_RegExpModifier = $lex("RegExpModifier");
       ADT_RegExpModifier = $adt("RegExpModifier");
       NT_Case = $sort("Case");
       ADT_Case = $adt("Case");
       NT_BooleanLiteral = $lex("BooleanLiteral");
       ADT_BooleanLiteral = $adt("BooleanLiteral");
       NT_LAYOUT = $lex("LAYOUT");
       ADT_LAYOUT = $adt("LAYOUT");
       NT_ImportedModule = $sort("ImportedModule");
       ADT_ImportedModule = $adt("ImportedModule");
       NT_LocalVariableDeclaration = $sort("LocalVariableDeclaration");
       ADT_LocalVariableDeclaration = $adt("LocalVariableDeclaration");
       NT_IntegerLiteral = $sort("IntegerLiteral");
       ADT_IntegerLiteral = $adt("IntegerLiteral");
       NT_Declarator = $sort("Declarator");
       ADT_Declarator = $adt("Declarator");
       NT_Target = $sort("Target");
       ADT_Target = $adt("Target");
       NT_TimePartNoTZ = $lex("TimePartNoTZ");
       ADT_TimePartNoTZ = $adt("TimePartNoTZ");
       NT_Renaming = $sort("Renaming");
       ADT_Renaming = $adt("Renaming");
       NT_Catch = $sort("Catch");
       ADT_Catch = $adt("Catch");
       ADT_GrammarDefinition = $adt("GrammarDefinition");
       NT_DataTarget = $sort("DataTarget");
       ADT_DataTarget = $adt("DataTarget");
       ADT_CharRange = $adt("CharRange");
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
       NT_KeywordFormals = $sort("KeywordFormals");
       ADT_KeywordFormals = $adt("KeywordFormals");
       NT_HexIntegerLiteral = $lex("HexIntegerLiteral");
       ADT_HexIntegerLiteral = $adt("HexIntegerLiteral");
       NT_ShellCommand = $sort("ShellCommand");
       ADT_ShellCommand = $adt("ShellCommand");
       NT_TimeZonePart = $lex("TimeZonePart");
       ADT_TimeZonePart = $adt("TimeZonePart");
       NT_Declaration = $sort("Declaration");
       ADT_Declaration = $adt("Declaration");
       NT_StringLiteral = $sort("StringLiteral");
       ADT_StringLiteral = $adt("StringLiteral");
       NT_PreStringChars = $lex("PreStringChars");
       ADT_PreStringChars = $adt("PreStringChars");
       NT_Tags = $sort("Tags");
       ADT_Tags = $adt("Tags");
       NT_NonterminalLabel = $lex("NonterminalLabel");
       ADT_NonterminalLabel = $adt("NonterminalLabel");
       NT_TypeArg = $sort("TypeArg");
       ADT_TypeArg = $adt("TypeArg");
       NT_RegExp = $lex("RegExp");
       ADT_RegExp = $adt("RegExp");
       NT_Bound = $sort("Bound");
       ADT_Bound = $adt("Bound");
       NT_Signature = $sort("Signature");
       ADT_Signature = $adt("Signature");
       NT_PathPart = $sort("PathPart");
       ADT_PathPart = $adt("PathPart");
       ADT_Associativity = $adt("Associativity");
       NT_Renamings = $sort("Renamings");
       ADT_Renamings = $adt("Renamings");
       NT_KeywordFormal = $sort("KeywordFormal");
       ADT_KeywordFormal = $adt("KeywordFormal");
       NT_OptionalExpression = $sort("OptionalExpression");
       ADT_OptionalExpression = $adt("OptionalExpression");
       NT_ModuleActuals = $sort("ModuleActuals");
       ADT_ModuleActuals = $adt("ModuleActuals");
       NT_Nonterminal = $lex("Nonterminal");
       ADT_Nonterminal = $adt("Nonterminal");
       NT_URLChars = $lex("URLChars");
       ADT_URLChars = $adt("URLChars");
       ADT_Condition = $adt("Condition");
       NT_Class = $sort("Class");
       ADT_Class = $adt("Class");
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
       NT_TypeVar = $sort("TypeVar");
       ADT_TypeVar = $adt("TypeVar");
       ADT_LocationType = $adt("LocationType");
       NT_JustDate = $lex("JustDate");
       ADT_JustDate = $adt("JustDate");
       NT_Header = $sort("Header");
       ADT_Header = $adt("Header");
       NT_OptionalComma = $lex("OptionalComma");
       ADT_OptionalComma = $adt("OptionalComma");
       NT_StringConstant = $lex("StringConstant");
       ADT_StringConstant = $adt("StringConstant");
       NT_FunctionDeclaration = $sort("FunctionDeclaration");
       ADT_FunctionDeclaration = $adt("FunctionDeclaration");
       NT_Import = $sort("Import");
       ADT_Import = $adt("Import");
       NT_Variant = $sort("Variant");
       ADT_Variant = $adt("Variant");
       NT_FunctionModifiers = $sort("FunctionModifiers");
       ADT_FunctionModifiers = $adt("FunctionModifiers");
       NT_Comprehension = $sort("Comprehension");
       ADT_Comprehension = $adt("Comprehension");
       NT_ConcreteHole = $sort("ConcreteHole");
       ADT_ConcreteHole = $adt("ConcreteHole");
       ADT_Message = $adt("Message");
       ADT_Grammar = $adt("Grammar");
       NT_Sym = $sort("Sym");
       ADT_Sym = $adt("Sym");
       NT_StringMiddle = $sort("StringMiddle");
       ADT_StringMiddle = $adt("StringMiddle");
       NT_RealLiteral = $lex("RealLiteral");
       ADT_RealLiteral = $adt("RealLiteral");
       NT_DateAndTime = $lex("DateAndTime");
       ADT_DateAndTime = $adt("DateAndTime");
       NT_Formals = $sort("Formals");
       ADT_Formals = $adt("Formals");
       NT_UserType = $sort("UserType");
       ADT_UserType = $adt("UserType");
       NT_QualifiedName = $sort("QualifiedName");
       ADT_QualifiedName = $adt("QualifiedName");
       NT_MidPathChars = $lex("MidPathChars");
       ADT_MidPathChars = $adt("MidPathChars");
       NT_ProtocolPart = $sort("ProtocolPart");
       ADT_ProtocolPart = $adt("ProtocolPart");
       NT_StringTemplate = $sort("StringTemplate");
       ADT_StringTemplate = $adt("StringTemplate");
       ADT_RascalKeywords = $keywords("RascalKeywords");
       NT_NamedBackslash = $lex("NamedBackslash");
       ADT_NamedBackslash = $adt("NamedBackslash");
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
       ADT_RuntimeException = $adt("RuntimeException");
       NT_NamedRegExp = $lex("NamedRegExp");
       ADT_NamedRegExp = $adt("NamedRegExp");
       NT_Assoc = $sort("Assoc");
       ADT_Assoc = $adt("Assoc");
       NT_DateTimeLiteral = $sort("DateTimeLiteral");
       ADT_DateTimeLiteral = $adt("DateTimeLiteral");
       NT_DecimalIntegerLiteral = $lex("DecimalIntegerLiteral");
       ADT_DecimalIntegerLiteral = $adt("DecimalIntegerLiteral");
       NT_Kind = $sort("Kind");
       ADT_Kind = $adt("Kind");
       NT_MidProtocolChars = $lex("MidProtocolChars");
       ADT_MidProtocolChars = $adt("MidProtocolChars");
       NT_OctalIntegerLiteral = $lex("OctalIntegerLiteral");
       ADT_OctalIntegerLiteral = $adt("OctalIntegerLiteral");
       NT_Parameters = $sort("Parameters");
       ADT_Parameters = $adt("Parameters");
       NT_PathTail = $sort("PathTail");
       ADT_PathTail = $adt("PathTail");
       NT_PreProtocolChars = $lex("PreProtocolChars");
       ADT_PreProtocolChars = $adt("PreProtocolChars");
       NT_Statement = $sort("Statement");
       ADT_Statement = $adt("Statement");
       NT_StringCharacter = $lex("StringCharacter");
       ADT_StringCharacter = $adt("StringCharacter");
       NT_Literal = $sort("Literal");
       ADT_Literal = $adt("Literal");
       NT_Toplevel = $sort("Toplevel");
       ADT_Toplevel = $adt("Toplevel");
       NT_ConcretePart = $lex("ConcretePart");
       ADT_ConcretePart = $adt("ConcretePart");
       NT_PathChars = $lex("PathChars");
       ADT_PathChars = $adt("PathChars");
       ADT_Exception = $adt("Exception");
       NT_Assignment = $sort("Assignment");
       ADT_Assignment = $adt("Assignment");
       NT_Module = $sort("Module");
       ADT_Module = $adt("Module");
       NT_PatternWithAction = $sort("PatternWithAction");
       ADT_PatternWithAction = $adt("PatternWithAction");
       NT_Assignable = $sort("Assignable");
       ADT_Assignable = $adt("Assignable");
       NT_BasicType = $sort("BasicType");
       ADT_BasicType = $adt("BasicType");
       NT_CommonKeywordParameters = $sort("CommonKeywordParameters");
       ADT_CommonKeywordParameters = $adt("CommonKeywordParameters");
       NT_ProtocolTail = $sort("ProtocolTail");
       ADT_ProtocolTail = $adt("ProtocolTail");
       NT_EvalCommand = $sort("EvalCommand");
       ADT_EvalCommand = $adt("EvalCommand");
       NT_PostPathChars = $lex("PostPathChars");
       ADT_PostPathChars = $adt("PostPathChars");
       NT_FunctionModifier = $sort("FunctionModifier");
       ADT_FunctionModifier = $adt("FunctionModifier");
       NT_ProdModifier = $sort("ProdModifier");
       ADT_ProdModifier = $adt("ProdModifier");
       NT_JustTime = $lex("JustTime");
       ADT_JustTime = $adt("JustTime");
       NT_UnicodeEscape = $lex("UnicodeEscape");
       ADT_UnicodeEscape = $adt("UnicodeEscape");
       $T6 = $TF.valueType();
       $T10 = $TF.stringType();
       $T18 = $TF.voidType();
       $T13 = $TF.parameterType("A", $T6);
       $T7 = $TF.parameterType("A", $T6);
       $T9 = $TF.parameterType("T", $T6);
       $T16 = $TF.setType(ADT_Symbol);
       NT_Mapping_Expression = $parameterizedSort("Mapping", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       $T11 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_1 = $parameterizedSort("KeywordArguments", new Type[] { $T11 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       NT_Mapping_Pattern = $parameterizedSort("Mapping", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       $T12 = $TF.parameterType("T", ADT_Tree);
       NT_KeywordArguments_Expression = $parameterizedSort("KeywordArguments", new Type[] { ADT_Expression }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Expression"))));
       $T22 = $TF.boolType();
       $T21 = $TF.functionType($T22, $TF.tupleType(ADT_CharRange, "r1", ADT_CharRange, "r2"), $TF.tupleEmpty());
       ADT_Maybe_1 = $parameterizedAdt("Maybe", new Type[] { $T13 });
       $T1 = $TF.setType(ADT_Symbol);
       $T5 = $TF.mapType(ADT_Symbol,$T1);
       NT_KeywordArgument_1 = $parameterizedSort("KeywordArgument", new Type[] { $T11 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       $T4 = $TF.listType(ADT_CharRange);
       NT_KeywordArguments_Pattern = $parameterizedSort("KeywordArguments", new Type[] { ADT_Pattern }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Sort, $RVF.string("Pattern"))));
       $T8 = $TF.listType($T9);
       NT_Mapping_1 = $parameterizedSort("Mapping", new Type[] { $T11 }, $RVF.list($RVF.constructor(RascalValueFactory.Symbol_Parameter, $RVF.string("T"), $RVF.constructor(RascalValueFactory.Symbol_Adt, $RVF.string("Tree"), $RVF.list()))));
       ADT_Maybe_Symbol = $parameterizedAdt("Maybe", new Type[] { ADT_Symbol });
       $T17 = $TF.listType($T18);
       ADT_Maybe_Associativity = $parameterizedAdt("Maybe", new Type[] { ADT_Associativity });
       $T15 = $TF.setType(ADT_Production);
       $T14 = $TF.setType(ADT_Symbol);
       $T3 = $TF.setType(ADT_Production);
       $T23 = $TF.setType(ADT_Attr);
       ADT_Maybe_Attr = $parameterizedAdt("Maybe", new Type[] { ADT_Attr });
       ADT_TreeSearchResult_1 = $parameterizedAdt("TreeSearchResult", new Type[] { $T11 });
       $T0 = $TF.listType(ADT_Symbol);
       $T20 = $TF.listType(ADT_Production);
       $T2 = $TF.setType($TF.tupleType(ADT_Symbol, ADT_Symbol));
       $T19 = $TF.setType($T18);
       ADT_Mapping_Expression = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Expression });
       ADT_KeywordArguments_1 = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { $T11 });
       ADT_Mapping_Pattern = $TF.abstractDataType($TS, "Mapping", new Type[] { ADT_Pattern });
       ADT_KeywordArguments_Expression = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Expression });
       ADT_KeywordArgument_1 = $TF.abstractDataType($TS, "KeywordArgument", new Type[] { $T11 });
       ADT_KeywordArguments_Pattern = $TF.abstractDataType($TS, "KeywordArguments", new Type[] { ADT_Pattern });
       ADT_Mapping_1 = $TF.abstractDataType($TS, "Mapping", new Type[] { $T11 });
       Symbol_eoi_ = $TF.constructor($TS, ADT_Symbol, "eoi");
       Production_lookahead_Symbol_set_Symbol_Production = $TF.constructor($TS, ADT_Production, "lookahead", M_ParseTree.ADT_Symbol, "def", $TF.setType(ADT_Symbol), "classes", M_ParseTree.ADT_Production, "production");
       Grammar_simple_set_Symbol_set_Production = $TF.constructor($TS, ADT_Grammar, "simple", $TF.setType(ADT_Symbol), "starts", $TF.setType(ADT_Production), "productions");
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
    public IValue removeLabels(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (IValue)lang_rascal_grammar_Lookahead_removeLabels$018dc4c5ab035b44((IConstructor) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T0)){
         $result = (IValue)lang_rascal_grammar_Lookahead_removeLabels$9c574341c6d9ec97((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IConstructor priority(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IConstructor) M_ParseTree.priority($P0, $P1);
    }
    public IConstructor compileLookaheads(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (IConstructor)lang_rascal_grammar_Lookahead_compileLookaheads$0c93fa49bdcf5b1b((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet diff(IValue $P0, IValue $P1){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T1) && $isSubtypeOf($P1Type,$T1)){
         $result = (ISet)lang_rascal_grammar_Lookahead_diff$a7f51e4503b5c204((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IConstructor complement(IValue $P0){ // Generated by Resolver
       return (IConstructor) M_lang_rascal_grammar_definition_Characters.complement($P0);
    }
    public IBool isNodeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isNodeType($P0);
    }
    public IConstructor computeLookaheads(IValue $P0, IValue $P1){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar) && $isSubtypeOf($P1Type,$T2)){
         $result = (IConstructor)lang_rascal_grammar_Lookahead_computeLookaheads$26dfcc12b08365da((IConstructor) $P0, (ISet) $P1);
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
    public IValue intersection(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_lang_rascal_grammar_definition_Characters.intersection($P0, $P1);
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
    public IConstructor optimizeLookaheads(IValue $P0, IValue $P1){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type,$T3)){
         $result = (IConstructor)lang_rascal_grammar_Lookahead_optimizeLookaheads$dd084f7d40e64b07((IConstructor) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IList order(IValue $P0){ // Generated by Resolver
       IList $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T4)){
         $result = (IList)lang_rascal_grammar_Lookahead_order$4eb2216b315c6b46((IList) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue union(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_lang_rascal_grammar_definition_Characters.union($P0, $P1);
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
    public IConstructor removeLabel(IValue $P0){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol)){
         $result = (IConstructor)lang_rascal_grammar_Lookahead_removeLabel$dc7038cda9b1bd2d((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isLocType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isLocType($P0);
    }
    public IMap first(IValue $P0){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (IMap)lang_rascal_grammar_Lookahead_first$8d738115fa89afe8((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public ISet first(IValue $P0, IValue $P1){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T0) && $isSubtypeOf($P1Type,$T5)){
         $result = (ISet)lang_rascal_grammar_Lookahead_first$7ecc3715cf16dd3a((IList) $P0, (IMap) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public ITuple headTail(IValue $P0){ // Generated by Resolver
       return (ITuple) M_List.headTail($P0);
    }
    public IConstructor treeAt(IValue $P0, IValue $P1, IValue $P2){ // Generated by Resolver
       return (IConstructor) M_ParseTree.treeAt($P0, $P1, $P2);
    }
    public IMap follow(IValue $P0, IValue $P1){ // Generated by Resolver
       IMap $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar) && $isSubtypeOf($P1Type,$T5)){
         $result = (IMap)lang_rascal_grammar_Lookahead_follow$c4198b787ccc5199((IConstructor) $P0, (IMap) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isRegular(IValue $P0){ // Generated by Resolver
       return (IBool) M_lang_rascal_grammar_definition_Regular.isRegular($P0);
    }
    public ISet definedSymbols(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (ISet)lang_rascal_grammar_Lookahead_definedSymbols$d175465ff362118c((IConstructor) $P0);
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
    public IBool isTupleType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isTupleType($P0);
    }
    public IValue mergeCC(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T5)){
         $result = (IValue)lang_rascal_grammar_Lookahead_mergeCC$40d82b0eeb5fa303((IMap) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T1)){
         $result = (IValue)lang_rascal_grammar_Lookahead_mergeCC$1d434fe22b76cf3a((ISet) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isBagType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isBagType($P0);
    }
    public ITuple firstAndFollow(IValue $P0){ // Generated by Resolver
       ITuple $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (ITuple)lang_rascal_grammar_Lookahead_firstAndFollow$9a18a48b90b1e31c((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isVoidType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isVoidType($P0);
    }
    public IBool isNonTerminalType(IValue $P0){ // Generated by Resolver
       return (IBool) M_ParseTree.isNonTerminalType($P0);
    }
    public IConstructor optimizeLookaheadsOld(IValue $P0, IValue $P1){ // Generated by Resolver
       IConstructor $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type,$T3)){
         $result = (IConstructor)lang_rascal_grammar_Lookahead_optimizeLookaheadsOld$8d0cc058fe472758((IConstructor) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
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
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Associativity) && $isSubtypeOf($P2Type,$T3)){
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
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_Symbol) && $isSubtypeOf($P1Type, M_ParseTree.ADT_Associativity) && $isSubtypeOf($P2Type,$T3)){
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
    public IValue difference(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IValue) M_lang_rascal_grammar_definition_Characters.difference($P0, $P1);
    }
    public ISet usedSymbols(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (ISet)lang_rascal_grammar_Lookahead_usedSymbols$2c1765cdce8f88b5((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IValue sort(IValue $P0){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type,$T8)){
         $result = (IValue)M_List.List_sort$1fe4426c8c8039da((IList) $P0);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type,$T10)){
         return $RVF.constructor(M_ParseTree.Symbol_sort_str, new IValue[]{(IString) $P0});
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IList sort(IValue $P0, IValue $P1){ // Generated by Resolver
       return (IList) M_List.sort($P0, $P1);
    }
    public IValue intersect(IValue $P0, IValue $P1){ // Generated by Resolver
       IValue $result = null;
       Type $P0Type = $P0.getType();
       Type $P1Type = $P1.getType();
       if($isSubtypeOf($P0Type,$T1) && $isSubtypeOf($P1Type,$T1)){
         $result = (IValue)lang_rascal_grammar_Lookahead_intersect$652d8dca76a2dc21((ISet) $P0, (ISet) $P1);
         if($result != null) return $result;
       }
       if($isSubtypeOf($P0Type, M_ParseTree.ADT_CharRange) && $isSubtypeOf($P1Type, M_ParseTree.ADT_CharRange)){
         $result = (IValue)M_lang_rascal_grammar_definition_Characters.lang_rascal_grammar_definition_Characters_intersect$2c8ff0e2841a0a40((IConstructor) $P0, (IConstructor) $P1);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0, $P1));
    }
    public IBool isIntType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isIntType($P0);
    }
    public ISet terminalSymbols(IValue $P0){ // Generated by Resolver
       ISet $result = null;
       Type $P0Type = $P0.getType();
       if($isSubtypeOf($P0Type, M_Grammar.ADT_Grammar)){
         $result = (ISet)lang_rascal_grammar_Lookahead_terminalSymbols$a998324b5ea47532((IConstructor) $P0);
         if($result != null) return $result;
       }
       
       throw RuntimeExceptionFactory.callFailed($RVF.list($P0));
    }
    public IBool isDateTimeType(IValue $P0){ // Generated by Resolver
       return (IBool) M_Type.isDateTimeType($P0);
    }

    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(1026,874,<26,0>,<50,1>) 
    public IConstructor lang_rascal_grammar_Lookahead_computeLookaheads$26dfcc12b08365da(IConstructor G_0, ISet $aux_extra_1){ 
        ValueRef<ISet> extra_1 = new ValueRef<ISet>("extra_1", $aux_extra_1);
    
        
        try {
            IConstructor G2_2 = ((IConstructor)(M_lang_rascal_grammar_definition_Regular.expandRegularSymbols(((IConstructor)($me.removeLabels(((IConstructor)G_0)))))));
            final ISetWriter $setwriter0 = (ISetWriter)$RVF.setWriter();
            ;
            /*muExists*/$SCOMP1: 
                do {
                    $SCOMP1_DESC1366:
                    for(IValue $elem2 : new DescendantMatchIterator(G2_2, 
                        new DescendantDescriptorAlwaysTrue($RVF.bool(false)))){
                        if($isComparable($elem2.getType(), M_ParseTree.ADT_Production)){
                           if($isSubtypeOf($elem2.getType(),M_ParseTree.ADT_Production)){
                              if($has_type_and_arity($elem2, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                 IValue $arg0_5 = (IValue)($subscript_int(((IValue)($elem2)),0));
                                 if($isComparable($arg0_5.getType(), $T6)){
                                    IValue $arg1_4 = (IValue)($subscript_int(((IValue)($elem2)),1));
                                    if($isComparable($arg1_4.getType(), $T6)){
                                       IValue $arg2_3 = (IValue)($subscript_int(((IValue)($elem2)),2));
                                       if($isComparable($arg2_3.getType(), $T6)){
                                          IConstructor p_5 = ((IConstructor)($elem2));
                                          $setwriter0.insert(p_5);
                                       
                                       } else {
                                          continue $SCOMP1_DESC1366;
                                       }
                                    } else {
                                       continue $SCOMP1_DESC1366;
                                    }
                                 } else {
                                    continue $SCOMP1_DESC1366;
                                 }
                              } else {
                                 continue $SCOMP1_DESC1366;
                              }
                           } else {
                              continue $SCOMP1_DESC1366;
                           }
                        } else {
                           continue $SCOMP1_DESC1366;
                        }
                    }
                    
                                 
                } while(false);
            ITuple $TMP6 = (ITuple)($me.firstAndFollow(((IConstructor)($RVF.constructor(Grammar_simple_set_Symbol_set_Production, new IValue[]{((ISet)(((ISet)($aadt_get_field(((IConstructor)G2_2), "starts"))))), ((ISet)($setwriter0.done()))})))));
            final ValueRef<IMap> fst_3 = new ValueRef<IMap>("fst", ((IMap)($atuple_subscript_int(((ITuple)($TMP6)),0))));
            final ValueRef<IMap> fol_4 = new ValueRef<IMap>("fol", ((IMap)($atuple_subscript_int(((ITuple)($TMP6)),1))));
            IValue $visitResult = $TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptorAlwaysTrue($RVF.bool(false)),
                 G_0,
                 (IVisitFunction) (IValue $VISIT0_subject, TraversalState $traversalState) -> {
                     VISIT0:switch(Fingerprint.getFingerprint($VISIT0_subject)){
                     
                         case 110389984:
                             if($isSubtypeOf($VISIT0_subject.getType(),M_ParseTree.ADT_Production)){
                                /*muExists*/CASE_110389984_0: 
                                    do {
                                        if($has_type_and_arity($VISIT0_subject, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                           IValue $arg0_10 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),0));
                                           if($isComparable($arg0_10.getType(), M_ParseTree.ADT_Symbol)){
                                              ValueRef<IConstructor> rhs_7 = new ValueRef<IConstructor>();
                                              IValue $arg1_9 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),1));
                                              if($isComparable($arg1_9.getType(), $T17)){
                                                 if($arg1_9.equals(((IList)$constants.get(0)/*[]*/))){
                                                   IValue $arg2_8 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),2));
                                                   if($isComparable($arg2_8.getType(), $T6)){
                                                      IConstructor p_6 = ((IConstructor)($VISIT0_subject));
                                                      IConstructor $replacement7 = (IConstructor)($RVF.constructor(Production_lookahead_Symbol_set_Symbol_Production, new IValue[]{((IConstructor)($arg0_10)), ((ISet)($amap_subscript(fol_4.getValue(),((IConstructor)($arg0_10))))), ((IConstructor)p_6)}));
                                                      if($isSubtypeOf($replacement7.getType(),$VISIT0_subject.getType())){
                                                         $traversalState.setMatchedAndChanged(true, true);
                                                         return $replacement7;
                                                      
                                                      } else {
                                                         break VISIT0;// switch
                                                      
                                                      }
                                                   }
                                                 
                                                 }
                                              
                                              }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
                             if($isSubtypeOf($VISIT0_subject.getType(),M_ParseTree.ADT_Production)){
                                /*muExists*/CASE_110389984_1: 
                                    do {
                                        if($has_type_and_arity($VISIT0_subject, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                           IValue $arg0_13 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),0));
                                           if($isComparable($arg0_13.getType(), M_ParseTree.ADT_Symbol)){
                                              IConstructor rhs_9 = ((IConstructor)($arg0_13));
                                              IValue $arg1_12 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),1));
                                              if($isComparable($arg1_12.getType(), $T0)){
                                                 IList lhs_10 = ((IList)($arg1_12));
                                                 IValue $arg2_11 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT0_subject)),2));
                                                 if($isComparable($arg2_11.getType(), $T6)){
                                                    IConstructor p_8 = ((IConstructor)($VISIT0_subject));
                                                    $arg1_12 = ((IValue)($me.removeLabels(((IList)($arg1_12)))));
                                                    ISet classes_11 = ((ISet)($me.first(((IList)($arg1_12)), fst_3.getValue())));
                                                    if((((IBool)($equal(((IList)($arg1_12)), ((IList)$constants.get(0)/*[]*/))))).getValue()){
                                                       classes_11 = ((ISet)($aset_add_aset(((ISet)classes_11),((ISet)($amap_subscript(fol_4.getValue(),((IConstructor)($arg0_13))))))));
                                                    
                                                    } else {
                                                       if((((IBool)($RVF.bool(((ISet)classes_11).contains(((IConstructor)$constants.get(1)/*empty()*/)))))).getValue()){
                                                          classes_11 = ((ISet)($aset_add_aset(((ISet)classes_11),((ISet)($amap_subscript(fol_4.getValue(),((IConstructor)($arg0_13))))))));
                                                       
                                                       }
                                                    
                                                    }classes_11 = ((ISet)(((ISet)classes_11).delete(((IConstructor)$constants.get(1)/*empty()*/))));
                                                    $traversalState.setMatchedAndChanged(true, true);
                                                    return $RVF.constructor(Production_lookahead_Symbol_set_Symbol_Production, new IValue[]{((IConstructor)($arg0_13)), ((ISet)($me.mergeCC(((ISet)($aset_add_aset(((ISet)classes_11),((ISet)($arel_subscript1_noset(extra_1.getValue(),((IConstructor)($arg0_13))))))))))), ((IConstructor)p_8)});
                                                 
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(1902,647,<52,0>,<66,1>) 
    public IConstructor lang_rascal_grammar_Lookahead_compileLookaheads$0c93fa49bdcf5b1b(IConstructor G_0){ 
        
        
        try {
            G_0 = ((IConstructor)($TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptorAlwaysTrue($RVF.bool(false)),
                 G_0,
                 (IVisitFunction) (IValue $VISIT2_subject, TraversalState $traversalState) -> {
                     VISIT2:switch(Fingerprint.getFingerprint($VISIT2_subject)){
                     
                         case -2132978880:
                             if($isSubtypeOf($VISIT2_subject.getType(),M_ParseTree.ADT_Production)){
                                /*muExists*/CASE_2132978880_2: 
                                    do {
                                        if($has_type_and_arity($VISIT2_subject, M_ParseTree.Production_associativity_Symbol_Associativity_set_Production, 3)){
                                           IValue $arg0_27 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT2_subject)),0));
                                           if($isComparable($arg0_27.getType(), M_ParseTree.ADT_Symbol)){
                                              ValueRef<IConstructor> rhs_5 = new ValueRef<IConstructor>();
                                              IValue $arg1_26 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT2_subject)),1));
                                              if($isComparable($arg1_26.getType(), $T6)){
                                                 IValue $arg2_25 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT2_subject)),2));
                                                 if($isComparable($arg2_25.getType(), $T3)){
                                                    ValueRef<ISet> alts_6 = new ValueRef<ISet>();
                                                    IConstructor $replacement24 = (IConstructor)(M_Type.choice(((IConstructor)($arg0_27)), ((ISet)($arg2_25))));
                                                    if($isSubtypeOf($replacement24.getType(),$VISIT2_subject.getType())){
                                                       $traversalState.setMatchedAndChanged(true, true);
                                                       return $replacement24;
                                                    
                                                    } else {
                                                       break VISIT2;// switch
                                                    
                                                    }
                                                 }
                                              
                                              }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                         case 1852264512:
                             if($isSubtypeOf($VISIT2_subject.getType(),M_ParseTree.ADT_Production)){
                                /*muExists*/CASE_1852264512_0: 
                                    do {
                                        if($has_type_and_arity($VISIT2_subject, Production_lookahead_Symbol_set_Symbol_Production, 3)){
                                           IValue $arg0_17 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT2_subject)),0));
                                           if($isComparable($arg0_17.getType(), M_ParseTree.ADT_Symbol)){
                                              ValueRef<IConstructor> rhs_1 = new ValueRef<IConstructor>();
                                              IValue $arg1_16 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT2_subject)),1));
                                              if($isComparable($arg1_16.getType(), $T19)){
                                                 if($arg1_16.equals(((ISet)$constants.get(2)/*{}*/))){
                                                   IValue $arg2_15 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT2_subject)),2));
                                                   if($isComparable($arg2_15.getType(), $T6)){
                                                      IConstructor $replacement14 = (IConstructor)(M_Type.choice(((IConstructor)($arg0_17)), ((ISet)$constants.get(2)/*{}*/)));
                                                      if($isSubtypeOf($replacement14.getType(),$VISIT2_subject.getType())){
                                                         $traversalState.setMatchedAndChanged(true, true);
                                                         return $replacement14;
                                                      
                                                      } else {
                                                         break VISIT2;// switch
                                                      
                                                      }
                                                   }
                                                 
                                                 }
                                              
                                              }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                         case -1467508160:
                             if($isSubtypeOf($VISIT2_subject.getType(),M_ParseTree.ADT_Production)){
                                /*muExists*/CASE_1467508160_1: 
                                    do {
                                        if($has_type_and_arity($VISIT2_subject, M_ParseTree.Production_priority_Symbol_list_Production, 2)){
                                           IValue $arg0_23 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT2_subject)),0));
                                           if($isComparable($arg0_23.getType(), M_ParseTree.ADT_Symbol)){
                                              ValueRef<IConstructor> rhs_2 = new ValueRef<IConstructor>();
                                              IValue $arg1_22 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT2_subject)),1));
                                              if($isComparable($arg1_22.getType(), $T20)){
                                                 ValueRef<IList> order_3 = new ValueRef<IList>();
                                                 final ISetWriter $setwriter19 = (ISetWriter)$RVF.setWriter();
                                                 ;
                                                 $SCOMP20_GEN2349:
                                                 for(IValue $elem21_for : ((IList)($arg1_22))){
                                                     IConstructor $elem21 = (IConstructor) $elem21_for;
                                                     ValueRef<IConstructor> p_4 = new ValueRef<IConstructor>();
                                                     $setwriter19.insert($elem21);
                                                 
                                                 }
                                                 
                                                             IConstructor $replacement18 = (IConstructor)(M_Type.choice(((IConstructor)($arg0_23)), ((ISet)($setwriter19.done()))));
                                                 if($isSubtypeOf($replacement18.getType(),$VISIT2_subject.getType())){
                                                    $traversalState.setMatchedAndChanged(true, true);
                                                    return $replacement18;
                                                 
                                                 } else {
                                                    break VISIT2;// switch
                                                 
                                                 }
                                              }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                     
                     }
                     return $VISIT2_subject;
                 })));
            IValue $visitResult = $TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptorAlwaysTrue($RVF.bool(false)),
                 G_0,
                 (IVisitFunction) (IValue $VISIT3_subject, TraversalState $traversalState) -> {
                     VISIT3:switch(Fingerprint.getFingerprint($VISIT3_subject)){
                     
                         case -304752112:
                             if($isSubtypeOf($VISIT3_subject.getType(),M_ParseTree.ADT_Production)){
                                /*muExists*/CASE_304752112_0: 
                                    do {
                                        if($has_type_and_arity($VISIT3_subject, M_Type.Production_choice_Symbol_set_Production, 2)){
                                           IValue $arg0_30 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT3_subject)),0));
                                           if($isComparable($arg0_30.getType(), M_ParseTree.ADT_Symbol)){
                                              IConstructor rhs_7 = ((IConstructor)($arg0_30));
                                              IValue $arg1_29 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT3_subject)),1));
                                              if($isComparable($arg1_29.getType(), $T3)){
                                                 ISet alts_8 = ((ISet)($arg1_29));
                                                 IConstructor $replacement28 = (IConstructor)($me.optimizeLookaheads(((IConstructor)($arg0_30)), ((ISet)($arg1_29))));
                                                 if($isSubtypeOf($replacement28.getType(),$VISIT3_subject.getType())){
                                                    $traversalState.setMatchedAndChanged(true, true);
                                                    return $replacement28;
                                                 
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
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(2744,107,<72,0>,<74,1>) 
    public IList lang_rascal_grammar_Lookahead_order$4eb2216b315c6b46(IList x_0){ 
        
        
        final IListWriter $listwriter31 = (IListWriter)$RVF.listWriter();
        $LCOMP32_GEN2809:
        for(IValue $elem33_for : ((IList)x_0)){
            IConstructor $elem33 = (IConstructor) $elem33_for;
            IConstructor e_1 = null;
            if((((IBool)($equal(((IConstructor)($elem33)),((IConstructor)($RVF.constructor(M_lang_rascal_grammar_definition_Characters.CharRange_empty_range_, new IValue[]{})))).not()))).getValue()){
              $listwriter31.append($elem33);
            
            } else {
              continue $LCOMP32_GEN2809;
            }
        
        }
        
                    return ((IList)(M_List.sort(((IList)($listwriter31.done())), new TypedFunctionInstance2<IValue,IValue,IValue>(($2464_0, $2464_1) -> { return M_lang_rascal_grammar_definition_Characters.lessThan($2464_0, $2464_1); }, $T21))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(2855,2003,<76,0>,<126,1>) 
    public IConstructor lang_rascal_grammar_Lookahead_optimizeLookaheads$dd084f7d40e64b07(IConstructor rhs_0, ISet alts_1){ 
        
        
        IList l_2 = ((IList)$constants.get(0)/*[]*/);
        /*muExists*/FOR4: 
            do {
                /*muExists*/FOR4_GEN3051_CONS_lookahead: 
                    do {
                        FOR4_GEN3051:
                        for(IValue $elem52_for : ((ISet)alts_1)){
                            IConstructor $elem52 = (IConstructor) $elem52_for;
                            if($has_type_and_arity($elem52, Production_lookahead_Symbol_set_Symbol_Production, 3)){
                               IValue $arg0_55 = (IValue)($aadt_subscript_int(((IConstructor)($elem52)),0));
                               if($isComparable($arg0_55.getType(), $T6)){
                                  IValue $arg1_54 = (IValue)($aadt_subscript_int(((IConstructor)($elem52)),1));
                                  if($isComparable($arg1_54.getType(), $T16)){
                                     if(true){
                                        ISet classes_3 = ((ISet)($arg1_54));
                                        IValue $arg2_53 = (IValue)($aadt_subscript_int(((IConstructor)($elem52)),2));
                                        if($isComparable($arg2_53.getType(), M_ParseTree.ADT_Production)){
                                           /*muExists*/FOR5: 
                                               do {
                                                   /*muExists*/FOR5_GEN3119_CONS_char_class: 
                                                       do {
                                                           FOR5_GEN3119:
                                                           for(IValue $elem50_for : ((ISet)($arg1_54))){
                                                               IConstructor $elem50 = (IConstructor) $elem50_for;
                                                               if($has_type_and_arity($elem50, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                                                                  IValue $arg0_51 = (IValue)($aadt_subscript_int(((IConstructor)($elem50)),0));
                                                                  if($isComparable($arg0_51.getType(), $T4)){
                                                                     IList rs_4 = ((IList)($arg0_51));
                                                                     FOR5_GEN3119_CONS_char_class_GEN3147:
                                                                     for(IValue $elem49_for : ((IList)($arg0_51))){
                                                                         IConstructor $elem49 = (IConstructor) $elem49_for;
                                                                         IConstructor r_5 = ((IConstructor)($elem49));
                                                                         /*muExists*/IF6: 
                                                                             do {
                                                                                 final IList $subject46 = ((IList)l_2);
                                                                                 int $subject46_cursor = 0;
                                                                                 if($isSubtypeOf($subject46.getType(),$T4)){
                                                                                    final int $subject46_len = (int)((IList)($subject46)).length();
                                                                                    if($subject46_len >= 0){
                                                                                       final int $pre_648_start = (int)$subject46_cursor;
                                                                                       IF6_LIST_MVARpre:
                                                                                       
                                                                                       for(int $pre_648_len = 0; $pre_648_len <= $subject46_len - $pre_648_start - 0; $pre_648_len += 1){
                                                                                          IList pre_6 = ((IList)($subject46.sublist($pre_648_start, $pre_648_len)));
                                                                                          $subject46_cursor = $pre_648_start + $pre_648_len;
                                                                                          final int $post_747_start = (int)$subject46_cursor;
                                                                                          IF6_LIST_MVARpre_MVARpost:
                                                                                          
                                                                                          for(int $post_747_len = 0; $post_747_len <= $subject46_len - $post_747_start - 0; $post_747_len += 1){
                                                                                             IList post_7 = ((IList)($subject46.sublist($post_747_start, $post_747_len)));
                                                                                             $subject46_cursor = $post_747_start + $post_747_len;
                                                                                             if($subject46_cursor == $subject46_len){
                                                                                                IBool $done43 = (IBool)(((IBool)$constants.get(3)/*true*/));
                                                                                                $ALL44_GEN3257:
                                                                                                for(IValue $elem45_for : ((IList)post_7)){
                                                                                                    IConstructor $elem45 = (IConstructor) $elem45_for;
                                                                                                    IConstructor z_8 = ((IConstructor)($elem45));
                                                                                                    if((((IBool)(M_lang_rascal_grammar_definition_Characters.lessThan(((IConstructor)z_8), ((IConstructor)r_5))))).getValue()){
                                                                                                      $done43 = ((IBool)$constants.get(4)/*false*/);
                                                                                                      break $ALL44_GEN3257; // muBreak
                                                                                                    
                                                                                                    } else {
                                                                                                      continue $ALL44_GEN3257;
                                                                                                    
                                                                                                    }
                                                                                                
                                                                                                }
                                                                                                
                                                                                                            if((((IBool)($done43))).getValue()){
                                                                                                  /*muExists*/IF7: 
                                                                                                      do {
                                                                                                          final IList $subject39 = ((IList)post_7);
                                                                                                          int $subject39_cursor = 0;
                                                                                                          if($isSubtypeOf($subject39.getType(),$T4)){
                                                                                                             final int $subject39_len = (int)((IList)($subject39)).length();
                                                                                                             if($subject39_len >= 0){
                                                                                                                final int $overlapping_941_start = (int)$subject39_cursor;
                                                                                                                IF7_LIST_MVARoverlapping:
                                                                                                                
                                                                                                                for(int $overlapping_941_len = 0; $overlapping_941_len <= $subject39_len - $overlapping_941_start - 0; $overlapping_941_len += 1){
                                                                                                                   IList overlapping_9 = ((IList)($subject39.sublist($overlapping_941_start, $overlapping_941_len)));
                                                                                                                   $subject39_cursor = $overlapping_941_start + $overlapping_941_len;
                                                                                                                   final int $post2_1040_start = (int)$subject39_cursor;
                                                                                                                   IF7_LIST_MVARoverlapping_MVARpost2:
                                                                                                                   
                                                                                                                   for(int $post2_1040_len = 0; $post2_1040_len <= $subject39_len - $post2_1040_start - 0; $post2_1040_len += 1){
                                                                                                                      IList post2_10 = ((IList)($subject39.sublist($post2_1040_start, $post2_1040_len)));
                                                                                                                      $subject39_cursor = $post2_1040_start + $post2_1040_len;
                                                                                                                      if($subject39_cursor == $subject39_len){
                                                                                                                         IBool $done36 = (IBool)(((IBool)$constants.get(3)/*true*/));
                                                                                                                         $ALL37_GEN3404:
                                                                                                                         for(IValue $elem38_for : ((IList)overlapping_9)){
                                                                                                                             IConstructor $elem38 = (IConstructor) $elem38_for;
                                                                                                                             IConstructor o_11 = ((IConstructor)($elem38));
                                                                                                                             if((((IBool)($equal(((IConstructor)(M_lang_rascal_grammar_definition_Characters.intersect(((IConstructor)r_5), ((IConstructor)o_11)))),((IConstructor)($RVF.constructor(M_lang_rascal_grammar_definition_Characters.CharRange_empty_range_, new IValue[]{})))).not()))).getValue()){
                                                                                                                               continue $ALL37_GEN3404;
                                                                                                                             
                                                                                                                             } else {
                                                                                                                               $done36 = ((IBool)$constants.get(4)/*false*/);
                                                                                                                               break $ALL37_GEN3404; // muBreak
                                                                                                                             
                                                                                                                             }
                                                                                                                         
                                                                                                                         }
                                                                                                                         
                                                                                                                                     if((((IBool)($done36))).getValue()){
                                                                                                                           IList common_12 = ((IList)(M_lang_rascal_grammar_definition_Characters.intersection(((IList)overlapping_9), ((IList)($RVF.list(((IConstructor)r_5)))))));
                                                                                                                           IList onlyR_13 = ((IList)(M_lang_rascal_grammar_definition_Characters.difference(((IList)($RVF.list(((IConstructor)r_5)))), ((IList)overlapping_9))));
                                                                                                                           IList onlyOverlapping_14 = ((IList)(M_lang_rascal_grammar_definition_Characters.difference(((IList)overlapping_9), ((IList)($RVF.list(((IConstructor)r_5)))))));
                                                                                                                           l_2 = ((IList)($alist_add_alist(((IList)($alist_add_alist(((IList)pre_6),((IList)($me.order(((IList)($alist_add_alist(((IList)($alist_add_alist(((IList)onlyR_13),((IList)common_12)))),((IList)onlyOverlapping_14)))))))))),((IList)post2_10))));
                                                                                                                           continue IF7;
                                                                                                                         } else {
                                                                                                                           continue IF7_LIST_MVARoverlapping_MVARpost2;
                                                                                                                         }
                                                                                                                      
                                                                                                                      } else {
                                                                                                                         continue IF7_LIST_MVARoverlapping_MVARpost2;/*list match1*/
                                                                                                                      }
                                                                                                                   }
                                                                                                                   continue IF7_LIST_MVARoverlapping;/*computeFail*/
                                                                                                                
                                                                                                                }
                                                                                                                
                                                                                                             
                                                                                                             }
                                                                                                          
                                                                                                          }
                                                                                                  
                                                                                                      } while(false);
                                                                                                  l_2 = ((IList)($alist_add_alist(((IList)($alist_add_alist(((IList)pre_6),((IList)($RVF.list(((IConstructor)r_5))))))),((IList)post_7))));
                                                                                                  continue IF6;
                                                                                                } else {
                                                                                                  if((((IBool)($equal(((IList)post_7), ((IList)$constants.get(0)/*[]*/))))).getValue()){
                                                                                                    /*muExists*/IF7: 
                                                                                                        do {
                                                                                                            final IList $subject39 = ((IList)post_7);
                                                                                                            int $subject39_cursor = 0;
                                                                                                            if($isSubtypeOf($subject39.getType(),$T4)){
                                                                                                               final int $subject39_len = (int)((IList)($subject39)).length();
                                                                                                               if($subject39_len >= 0){
                                                                                                                  final int $overlapping_941_start = (int)$subject39_cursor;
                                                                                                                  IF7_LIST_MVARoverlapping:
                                                                                                                  
                                                                                                                  for(int $overlapping_941_len = 0; $overlapping_941_len <= $subject39_len - $overlapping_941_start - 0; $overlapping_941_len += 1){
                                                                                                                     IList overlapping_9 = ((IList)($subject39.sublist($overlapping_941_start, $overlapping_941_len)));
                                                                                                                     $subject39_cursor = $overlapping_941_start + $overlapping_941_len;
                                                                                                                     final int $post2_1040_start = (int)$subject39_cursor;
                                                                                                                     IF7_LIST_MVARoverlapping_MVARpost2:
                                                                                                                     
                                                                                                                     for(int $post2_1040_len = 0; $post2_1040_len <= $subject39_len - $post2_1040_start - 0; $post2_1040_len += 1){
                                                                                                                        IList post2_10 = ((IList)($subject39.sublist($post2_1040_start, $post2_1040_len)));
                                                                                                                        $subject39_cursor = $post2_1040_start + $post2_1040_len;
                                                                                                                        if($subject39_cursor == $subject39_len){
                                                                                                                           IBool $done36 = (IBool)(((IBool)$constants.get(3)/*true*/));
                                                                                                                           $ALL37_GEN3404:
                                                                                                                           for(IValue $elem38_for : ((IList)overlapping_9)){
                                                                                                                               IConstructor $elem38 = (IConstructor) $elem38_for;
                                                                                                                               IConstructor o_11 = ((IConstructor)($elem38));
                                                                                                                               if((((IBool)($equal(((IConstructor)(M_lang_rascal_grammar_definition_Characters.intersect(((IConstructor)r_5), ((IConstructor)o_11)))),((IConstructor)($RVF.constructor(M_lang_rascal_grammar_definition_Characters.CharRange_empty_range_, new IValue[]{})))).not()))).getValue()){
                                                                                                                                 continue $ALL37_GEN3404;
                                                                                                                               
                                                                                                                               } else {
                                                                                                                                 $done36 = ((IBool)$constants.get(4)/*false*/);
                                                                                                                                 break $ALL37_GEN3404; // muBreak
                                                                                                                               
                                                                                                                               }
                                                                                                                           
                                                                                                                           }
                                                                                                                           
                                                                                                                                       if((((IBool)($done36))).getValue()){
                                                                                                                             IList common_12 = ((IList)(M_lang_rascal_grammar_definition_Characters.intersection(((IList)overlapping_9), ((IList)($RVF.list(((IConstructor)r_5)))))));
                                                                                                                             IList onlyR_13 = ((IList)(M_lang_rascal_grammar_definition_Characters.difference(((IList)($RVF.list(((IConstructor)r_5)))), ((IList)overlapping_9))));
                                                                                                                             IList onlyOverlapping_14 = ((IList)(M_lang_rascal_grammar_definition_Characters.difference(((IList)overlapping_9), ((IList)($RVF.list(((IConstructor)r_5)))))));
                                                                                                                             l_2 = ((IList)($alist_add_alist(((IList)($alist_add_alist(((IList)pre_6),((IList)($me.order(((IList)($alist_add_alist(((IList)($alist_add_alist(((IList)onlyR_13),((IList)common_12)))),((IList)onlyOverlapping_14)))))))))),((IList)post2_10))));
                                                                                                                             continue IF7;
                                                                                                                           } else {
                                                                                                                             continue IF7_LIST_MVARoverlapping_MVARpost2;
                                                                                                                           }
                                                                                                                        
                                                                                                                        } else {
                                                                                                                           continue IF7_LIST_MVARoverlapping_MVARpost2;/*list match1*/
                                                                                                                        }
                                                                                                                     }
                                                                                                                     continue IF7_LIST_MVARoverlapping;/*computeFail*/
                                                                                                                  
                                                                                                                  }
                                                                                                                  
                                                                                                               
                                                                                                               }
                                                                                                            
                                                                                                            }
                                                                                                    
                                                                                                        } while(false);
                                                                                                    l_2 = ((IList)($alist_add_alist(((IList)($alist_add_alist(((IList)pre_6),((IList)($RVF.list(((IConstructor)r_5))))))),((IList)post_7))));
                                                                                                    continue IF6;
                                                                                                  } else {
                                                                                                    continue IF6_LIST_MVARpre_MVARpost;
                                                                                                  }
                                                                                                
                                                                                                }
                                                                                             
                                                                                             } else {
                                                                                                continue IF6_LIST_MVARpre_MVARpost;/*list match1*/
                                                                                             }
                                                                                          }
                                                                                          continue IF6_LIST_MVARpre;/*computeFail*/
                                                                                       
                                                                                       }
                                                                                       
                                                                                    
                                                                                    }
                                                                                 
                                                                                 }
                                                                         
                                                                             } while(false);
                                                                         final Template $template34 = (Template)new Template($RVF, "does this ever happen? ");
                                                                         $template34.beginIndent("                       ");
                                                                         $template34.addVal(r_5);
                                                                         $template34.endIndent("                       ");
                                                                         $template34.addStr(" and ");
                                                                         $template34.beginIndent("     ");
                                                                         $template34.addVal(l_2);
                                                                         $template34.endIndent("     ");
                                                                         M_IO.println(((IValue)($template34.close())));
                                                                         l_2 = ((IList)($alist_add_alist(((IList)($RVF.list(((IConstructor)r_5)))),((IList)l_2))));
                                                                     
                                                                     }
                                                                     continue FOR5_GEN3119;
                                                                                 
                                                                  } else {
                                                                     continue FOR5_GEN3119;
                                                                  }
                                                               } else {
                                                                  continue FOR5_GEN3119;
                                                               }
                                                           }
                                                           continue FOR5;
                                                                       
                                                       } while(false);
                                           
                                               } while(false);
                                           /* void:  muCon([]) */
                                        } else {
                                           continue FOR4_GEN3051;
                                        }
                                     } else {
                                        continue FOR4_GEN3051;
                                     }
                                  } else {
                                     continue FOR4_GEN3051;
                                  }
                               } else {
                                  continue FOR4_GEN3051;
                               }
                            } else {
                               continue FOR4_GEN3051;
                            }
                        }
                        continue FOR4;
                                    
                    } while(false);
        
            } while(false);
        /* void:  muCon([]) */IMap m_15 = ((IMap)$constants.get(5)/*()*/);
        ISet init_16 = ((ISet)$constants.get(2)/*{}*/);
        /*muExists*/FOR8: 
            do {
                /*muExists*/FOR8_GEN4124_CONS_lookahead: 
                    do {
                        FOR8_GEN4124:
                        for(IValue $elem59_for : ((ISet)alts_1)){
                            IConstructor $elem59 = (IConstructor) $elem59_for;
                            if($has_type_and_arity($elem59, Production_lookahead_Symbol_set_Symbol_Production, 3)){
                               IValue $arg0_62 = (IValue)($aadt_subscript_int(((IConstructor)($elem59)),0));
                               if($isComparable($arg0_62.getType(), $T6)){
                                  IValue $arg1_61 = (IValue)($aadt_subscript_int(((IConstructor)($elem59)),1));
                                  if($isComparable($arg1_61.getType(), $T16)){
                                     if(true){
                                        ISet classes_17 = ((ISet)($arg1_61));
                                        IValue $arg2_60 = (IValue)($aadt_subscript_int(((IConstructor)($elem59)),2));
                                        if($isComparable($arg2_60.getType(), M_ParseTree.ADT_Production)){
                                           if(true){
                                              IConstructor p_18 = ((IConstructor)($arg2_60));
                                              FOR8_GEN4124_CONS_lookahead_GEN4180:
                                              for(IValue $elem57_for : ((ISet)($arg1_61))){
                                                  IConstructor $elem57 = (IConstructor) $elem57_for;
                                                  if($has_type_and_arity($elem57, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                                                     IValue $arg0_58 = (IValue)($aadt_subscript_int(((IConstructor)($elem57)),0));
                                                     if($isComparable($arg0_58.getType(), $T4)){
                                                        IList rs_19 = ((IList)($arg0_58));
                                                        /*muExists*/FOR9: 
                                                            do {
                                                                FOR9_GEN4219:
                                                                for(IValue $elem56_for : ((IList)l_2)){
                                                                    IConstructor $elem56 = (IConstructor) $elem56_for;
                                                                    IConstructor r_20 = ((IConstructor)($elem56));
                                                                    if((((IBool)($equal(((IList)(M_lang_rascal_grammar_definition_Characters.intersection(((IList)($RVF.list(((IConstructor)r_20)))), ((IList)($arg0_58))))),((IList)$constants.get(0)/*[]*/)).not()))).getValue()){
                                                                       GuardedIValue guarded11 = $guarded_map_subscript(((IMap)m_15),((IConstructor)r_20));
                                                                       m_15 = ((IMap)($amap_update(r_20,$aset_add_aset(((ISet)(($is_defined_value(guarded11) ? ((ISet)$get_defined_value(guarded11)) : init_16))),((ISet)($RVF.set(((IConstructor)($arg2_60)))))), ((IMap)(((IMap)m_15))))));
                                                                    
                                                                    }
                                                                
                                                                }
                                                                continue FOR9;
                                                                            
                                                            } while(false);
                                                        /* void:  muCon([]) */
                                                     } else {
                                                        continue FOR8_GEN4124_CONS_lookahead_GEN4180;
                                                     }
                                                  } else {
                                                     continue FOR8_GEN4124_CONS_lookahead_GEN4180;
                                                  }
                                              }
                                              continue FOR8_GEN4124;
                                                          
                                           } else {
                                              continue FOR8_GEN4124;
                                           }
                                        } else {
                                           continue FOR8_GEN4124;
                                        }
                                     } else {
                                        continue FOR8_GEN4124;
                                     }
                                  } else {
                                     continue FOR8_GEN4124;
                                  }
                               } else {
                                  continue FOR8_GEN4124;
                               }
                            } else {
                               continue FOR8_GEN4124;
                            }
                        }
                        continue FOR8;
                                    
                    } while(false);
        
            } while(false);
        /* void:  muCon([]) */IMap mInv_21 = ((IMap)$constants.get(5)/*()*/);
        ISet init2_22 = ((ISet)$constants.get(2)/*{}*/);
        /*muExists*/FOR12: 
            do {
                FOR12_GEN4497:
                for(IValue $elem63_for : ((ISet)($amap_field_project((IMap)((IMap)m_15), ((IInteger)$constants.get(6)/*0*/), ((IInteger)$constants.get(7)/*1*/))))){
                    IValue $elem63 = (IValue) $elem63_for;
                    final IValue $tuple_subject64 = ((IValue)($elem63));
                    if($tuple_subject64 instanceof ITuple && ((ITuple)$tuple_subject64).arity() == 2){
                       /*muExists*/FOR12_GEN4497_TUPLE: 
                           do {
                               IConstructor r_23 = null;
                               ISet s_24 = null;
                               GuardedIValue guarded13 = $guarded_map_subscript(((IMap)mInv_21),((ISet)($subscript_int(((IValue)($tuple_subject64)),1))));
                               mInv_21 = ((IMap)($amap_update($subscript_int(((IValue)($tuple_subject64)),1),$aset_add_aset(((ISet)(($is_defined_value(guarded13) ? ((ISet)$get_defined_value(guarded13)) : init2_22))),((ISet)($RVF.set(((IConstructor)($subscript_int(((IValue)($tuple_subject64)),0))))))), ((IMap)(((IMap)mInv_21))))));
                       
                           } while(false);
                    
                    } else {
                       continue FOR12_GEN4497;
                    }
                }
                continue FOR12;
                            
            } while(false);
        /* void:  muCon([]) */final ISetWriter $setwriter65 = (ISetWriter)$RVF.setWriter();
        ;
        /*muExists*/$SCOMP66_GEN4586_CONS_lookahead: 
            do {
                $SCOMP66_GEN4586:
                for(IValue $elem67_for : ((ISet)alts_1)){
                    IConstructor $elem67 = (IConstructor) $elem67_for;
                    if($has_type_and_arity($elem67, Production_lookahead_Symbol_set_Symbol_Production, 3)){
                       IValue $arg0_70 = (IValue)($aadt_subscript_int(((IConstructor)($elem67)),0));
                       if($isComparable($arg0_70.getType(), $T6)){
                          IValue $arg1_69 = (IValue)($aadt_subscript_int(((IConstructor)($elem67)),1));
                          if($isComparable($arg1_69.getType(), $T16)){
                             ISet classes_26 = null;
                             IValue $arg2_68 = (IValue)($aadt_subscript_int(((IConstructor)($elem67)),2));
                             if($isComparable($arg2_68.getType(), M_ParseTree.ADT_Production)){
                                IConstructor p_27 = null;
                                if((((IBool)($RVF.bool(((ISet)($arg1_69)).contains(((IConstructor)($RVF.constructor(Symbol_eoi_, new IValue[]{})))))))).getValue()){
                                  $setwriter65.insert($arg2_68);
                                
                                } else {
                                  continue $SCOMP66_GEN4586;
                                }
                             
                             } else {
                                continue $SCOMP66_GEN4586;
                             }
                          } else {
                             continue $SCOMP66_GEN4586;
                          }
                       } else {
                          continue $SCOMP66_GEN4586;
                       }
                    } else {
                       continue $SCOMP66_GEN4586;
                    }
                }
                
                            
            } while(false);
        ISet endOfInputClasses_25 = ((ISet)($setwriter65.done()));
        final ISetWriter $setwriter71 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP72_GEN4730:
        for(IValue $elem76_for : ((IMap)mInv_21)){
            ISet $elem76 = (ISet) $elem76_for;
            ISet s_29 = null;
            final IListWriter $listwriter73 = (IListWriter)$RVF.listWriter();
            $LCOMP74_GEN4695:
            for(IValue $elem75_for : ((ISet)($amap_subscript(((IMap)mInv_21),((ISet)($elem76)))))){
                IConstructor $elem75 = (IConstructor) $elem75_for;
                IConstructor r_28 = null;
                $listwriter73.append($elem75);
            
            }
            
                        $setwriter71.insert($RVF.constructor(Production_lookahead_Symbol_set_Symbol_Production, new IValue[]{((IConstructor)rhs_0), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)($listwriter73.done()))})))))), ((IConstructor)(M_Type.choice(((IConstructor)rhs_0), ((ISet)($elem76)))))}));
        
        }
        
                    return ((IConstructor)(M_Type.choice(((IConstructor)rhs_0), ((ISet)($aset_add_aset(((ISet)($setwriter71.done())),((ISet)(((((IBool)($equal(((ISet)endOfInputClasses_25),((ISet)$constants.get(2)/*{}*/)).not()))).getValue() ? $RVF.set(((IConstructor)($RVF.constructor(Production_lookahead_Symbol_set_Symbol_Production, new IValue[]{((IConstructor)rhs_0), ((ISet)($RVF.set(((IConstructor)($RVF.constructor(Symbol_eoi_, new IValue[]{})))))), ((IConstructor)(M_Type.choice(((IConstructor)rhs_0), ((ISet)endOfInputClasses_25))))})))) : ((ISet)$constants.get(2)/*{}*/))))))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(4860,901,<128,0>,<153,1>) 
    public IConstructor lang_rascal_grammar_Lookahead_optimizeLookaheadsOld$8d0cc058fe472758(IConstructor rhs_0, ISet alts_1){ 
        
        
        int $iterations78 = 1000000;
        if($iterations78 <= 0){
         throw RuntimeExceptionFactory.indexOutOfBounds($RVF.integer($iterations78));
        }
        boolean $change79 = true;
        while14:
            while($change79 && $iterations78 >= 0){
                $change79 = false;
                ISet $alts80 = (ISet)(alts_1);
                /*muExists*/FOR15: 
                    do {
                        /*muExists*/FOR15_GEN4962_CONS_lookahead: 
                            do {
                                FOR15_GEN4962:
                                for(IValue $elem85_for : ((ISet)alts_1)){
                                    IConstructor $elem85 = (IConstructor) $elem85_for;
                                    if($has_type_and_arity($elem85, Production_lookahead_Symbol_set_Symbol_Production, 3)){
                                       IValue $arg0_88 = (IValue)($aadt_subscript_int(((IConstructor)($elem85)),0));
                                       if($isComparable($arg0_88.getType(), $T6)){
                                          IValue $arg1_87 = (IValue)($aadt_subscript_int(((IConstructor)($elem85)),1));
                                          if($isComparable($arg1_87.getType(), $T1)){
                                             ISet c1_3 = ((ISet)($arg1_87));
                                             IValue $arg2_86 = (IValue)($aadt_subscript_int(((IConstructor)($elem85)),2));
                                             if($isComparable($arg2_86.getType(), M_ParseTree.ADT_Production)){
                                                IConstructor p1_4 = ((IConstructor)($arg2_86));
                                                IConstructor a_2 = ((IConstructor)($elem85));
                                                FOR15_GEN4962_CONS_lookahead_GEN4992:
                                                for(IValue $elem81_for : ((ISet)alts_1)){
                                                    IConstructor $elem81 = (IConstructor) $elem81_for;
                                                    if($has_type_and_arity($elem81, Production_lookahead_Symbol_set_Symbol_Production, 3)){
                                                       IValue $arg0_84 = (IValue)($aadt_subscript_int(((IConstructor)($elem81)),0));
                                                       if($isComparable($arg0_84.getType(), $T6)){
                                                          IValue $arg1_83 = (IValue)($aadt_subscript_int(((IConstructor)($elem81)),1));
                                                          if($isComparable($arg1_83.getType(), $T1)){
                                                             ISet c2_6 = ((ISet)($arg1_83));
                                                             IValue $arg2_82 = (IValue)($aadt_subscript_int(((IConstructor)($elem81)),2));
                                                             if($isComparable($arg2_82.getType(), M_ParseTree.ADT_Production)){
                                                                IConstructor p2_7 = ((IConstructor)($arg2_82));
                                                                IConstructor b_5 = ((IConstructor)($elem81));
                                                                if((((IBool)($equal(((IConstructor)a_2),((IConstructor)b_5)).not()))).getValue()){
                                                                  if((((IBool)($equal(((ISet)($arg1_87)), ((ISet)($arg1_83)))))).getValue()){
                                                                     alts_1 = ((ISet)(((ISet)alts_1).subtract(((ISet)($RVF.set(((IConstructor)a_2), b_5))))));
                                                                     alts_1 = ((ISet)($aset_add_aset(((ISet)alts_1),((ISet)($RVF.set(((IConstructor)($RVF.constructor(Production_lookahead_Symbol_set_Symbol_Production, new IValue[]{((IConstructor)rhs_0), ((ISet)($arg1_87)), ((IConstructor)(M_Type.choice(((IConstructor)rhs_0), ((ISet)($RVF.set(((IConstructor)($arg2_86)), $arg2_82))))))})))))))));
                                                                  
                                                                  } else {
                                                                     ISet common_8 = ((ISet)($me.intersect(((ISet)($arg1_87)), ((ISet)($arg1_83)))));
                                                                     if((((IBool)($equal(((ISet)common_8),((ISet)$constants.get(2)/*{}*/)).not()))).getValue()){
                                                                        ISet diff1_9 = ((ISet)($me.diff(((ISet)($arg1_87)), ((ISet)($arg1_83)))));
                                                                        ISet diff2_10 = ((ISet)($me.diff(((ISet)($arg1_83)), ((ISet)($arg1_87)))));
                                                                        alts_1 = ((ISet)(((ISet)alts_1).subtract(((ISet)($RVF.set(((IConstructor)a_2), b_5))))));
                                                                        alts_1 = ((ISet)($aset_add_aset(((ISet)alts_1),((ISet)($RVF.set(((IConstructor)($RVF.constructor(Production_lookahead_Symbol_set_Symbol_Production, new IValue[]{((IConstructor)rhs_0), ((ISet)common_8), ((IConstructor)(M_Type.choice(((IConstructor)rhs_0), ((ISet)($RVF.set(((IConstructor)($arg2_86)), $arg2_82))))))})))))))));
                                                                        if((((IBool)($equal(((ISet)diff1_9),((ISet)$constants.get(2)/*{}*/)).not()))).getValue()){
                                                                           alts_1 = ((ISet)($aset_add_elm(((ISet)alts_1),((IConstructor)($RVF.constructor(Production_lookahead_Symbol_set_Symbol_Production, new IValue[]{((IConstructor)rhs_0), ((ISet)diff1_9), ((IConstructor)($arg2_86))}))))));
                                                                        
                                                                        }
                                                                        if((((IBool)($equal(((ISet)diff2_10),((ISet)$constants.get(2)/*{}*/)).not()))).getValue()){
                                                                           alts_1 = ((ISet)($aset_add_elm(((ISet)alts_1),((IConstructor)($RVF.constructor(Production_lookahead_Symbol_set_Symbol_Production, new IValue[]{((IConstructor)rhs_0), ((ISet)diff2_10), ((IConstructor)($arg2_82))}))))));
                                                                        
                                                                        }
                                                                     
                                                                     }
                                                                  
                                                                  }
                                                                } else {
                                                                  continue FOR15_GEN4962_CONS_lookahead_GEN4992;
                                                                }
                                                             
                                                             } else {
                                                                continue FOR15_GEN4962_CONS_lookahead_GEN4992;
                                                             }
                                                          } else {
                                                             continue FOR15_GEN4962_CONS_lookahead_GEN4992;
                                                          }
                                                       } else {
                                                          continue FOR15_GEN4962_CONS_lookahead_GEN4992;
                                                       }
                                                    } else {
                                                       continue FOR15_GEN4962_CONS_lookahead_GEN4992;
                                                    }
                                                }
                                                continue FOR15_GEN4962;
                                                            
                                             } else {
                                                continue FOR15_GEN4962;
                                             }
                                          } else {
                                             continue FOR15_GEN4962;
                                          }
                                       } else {
                                          continue FOR15_GEN4962;
                                       }
                                    } else {
                                       continue FOR15_GEN4962;
                                    }
                                }
                                continue FOR15;
                                            
                            } while(false);
                
                    } while(false);
                /* void:  muCon([]) */if((((IBool)($equal(((ISet)($alts80)),((ISet)alts_1)).not()))).getValue()){
                   $change79 = true;
                
                }
                $iterations78 += -1;
        
            }
        return ((IConstructor)(M_Type.choice(((IConstructor)rhs_0), ((ISet)alts_1))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(5763,211,<155,0>,<160,1>) 
    public ISet lang_rascal_grammar_Lookahead_intersect$652d8dca76a2dc21(ISet u1_0, ISet u2_1){ 
        
        
        /*muExists*/IF20: 
            do {
                ISet $subject100 = (ISet)(u1_0);
                IF20_SET_CONS_char_class$_DFLT_SET_ELM107:
                for(IValue $elem106_for : ((ISet)($subject100))){
                    IConstructor $elem106 = (IConstructor) $elem106_for;
                    if($has_type_and_arity($elem106, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                       IValue $arg0_108 = (IValue)($aadt_subscript_int(((IConstructor)($elem106)),0));
                       if($isComparable($arg0_108.getType(), $T4)){
                          IList r1_2 = ((IList)($arg0_108));
                          final ISet $subject102 = ((ISet)(((ISet)($subject100)).delete(((IConstructor)($elem106)))));
                          IF20_SET_CONS_char_class_MVAR$_89:
                          for(IValue $elem104_for : new SubSetGenerator(((ISet)($subject102)))){
                              ISet $elem104 = (ISet) $elem104_for;
                              final ISet $subject103 = ((ISet)(((ISet)($subject102)).subtract(((ISet)($elem104)))));
                              if(((ISet)($subject103)).size() == 0){
                                 ISet $subject91 = (ISet)(u2_1);
                                 IF20_SET_CONS_char_class$_DFLT_SET_ELM98:
                                 for(IValue $elem97_for : ((ISet)($subject91))){
                                     IConstructor $elem97 = (IConstructor) $elem97_for;
                                     if($has_type_and_arity($elem97, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                                        IValue $arg0_99 = (IValue)($aadt_subscript_int(((IConstructor)($elem97)),0));
                                        if($isComparable($arg0_99.getType(), $T4)){
                                           IList r2_3 = ((IList)($arg0_99));
                                           final ISet $subject93 = ((ISet)(((ISet)($subject91)).delete(((IConstructor)($elem97)))));
                                           IF20_SET_CONS_char_class_MVAR$_90:
                                           for(IValue $elem95_for : new SubSetGenerator(((ISet)($subject93)))){
                                               ISet $elem95 = (ISet) $elem95_for;
                                               final ISet $subject94 = ((ISet)(((ISet)($subject93)).subtract(((ISet)($elem95)))));
                                               if(((ISet)($subject94)).size() == 0){
                                                  return ((ISet)($aset_add_aset(((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)(M_lang_rascal_grammar_definition_Characters.intersection(((IList)($arg0_108)), ((IList)($arg0_99)))))})))))),((ISet)(((ISet)u1_0).intersect(((ISet)u2_1)))))));
                                               
                                               } else {
                                                  continue IF20_SET_CONS_char_class_MVAR$_90;/*set pat3*/
                                               }
                                           }
                                           continue IF20_SET_CONS_char_class$_DFLT_SET_ELM98;/*redirected IF20_SET_CONS_char_class to IF20_SET_CONS_char_class$_DFLT_SET_ELM98; set pat4*/
                                                       
                                        } else {
                                           continue IF20_SET_CONS_char_class$_DFLT_SET_ELM98;/*default set elem*/
                                        }
                                     } else {
                                        continue IF20_SET_CONS_char_class$_DFLT_SET_ELM98;/*default set elem*/
                                     }
                                 }
                                 continue IF20_SET_CONS_char_class_MVAR$_89;
                                             
                              } else {
                                 continue IF20_SET_CONS_char_class_MVAR$_89;/*set pat3*/
                              }
                          }
                          continue IF20_SET_CONS_char_class$_DFLT_SET_ELM107;/*redirected IF20_SET_CONS_char_class to IF20_SET_CONS_char_class$_DFLT_SET_ELM107; set pat4*/
                                      
                       } else {
                          continue IF20_SET_CONS_char_class$_DFLT_SET_ELM107;/*default set elem*/
                       }
                    } else {
                       continue IF20_SET_CONS_char_class$_DFLT_SET_ELM107;/*default set elem*/
                    }
                }
                
                            
            } while(false);
        return ((ISet)(((ISet)u1_0).intersect(((ISet)u2_1))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(5976,206,<162,0>,<167,1>) 
    public ISet lang_rascal_grammar_Lookahead_diff$a7f51e4503b5c204(ISet u1_0, ISet u2_1){ 
        
        
        /*muExists*/IF21: 
            do {
                ISet $subject117 = (ISet)(u1_0);
                IF21_SET_CONS_char_class$_DFLT_SET_ELM123:
                for(IValue $elem122_for : ((ISet)($subject117))){
                    IConstructor $elem122 = (IConstructor) $elem122_for;
                    if($has_type_and_arity($elem122, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                       IValue $arg0_124 = (IValue)($aadt_subscript_int(((IConstructor)($elem122)),0));
                       if($isComparable($arg0_124.getType(), $T4)){
                          IList r1_2 = ((IList)($arg0_124));
                          final ISet $subject119 = ((ISet)(((ISet)($subject117)).delete(((IConstructor)($elem122)))));
                          IF21_SET_CONS_char_class_MVARs1:
                          for(IValue $elem121_for : new SubSetGenerator(((ISet)($subject119)))){
                              ISet $elem121 = (ISet) $elem121_for;
                              ISet s1_3 = ((ISet)($elem121));
                              final ISet $subject120 = ((ISet)(((ISet)($subject119)).subtract(((ISet)($elem121)))));
                              if(((ISet)($subject120)).size() == 0){
                                 ISet $subject109 = (ISet)(u2_1);
                                 IF21_SET_CONS_char_class$_DFLT_SET_ELM115:
                                 for(IValue $elem114_for : ((ISet)($subject109))){
                                     IConstructor $elem114 = (IConstructor) $elem114_for;
                                     if($has_type_and_arity($elem114, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                                        IValue $arg0_116 = (IValue)($aadt_subscript_int(((IConstructor)($elem114)),0));
                                        if($isComparable($arg0_116.getType(), $T4)){
                                           IList r2_4 = ((IList)($arg0_116));
                                           final ISet $subject111 = ((ISet)(((ISet)($subject109)).delete(((IConstructor)($elem114)))));
                                           IF21_SET_CONS_char_class_MVARs2:
                                           for(IValue $elem113_for : new SubSetGenerator(((ISet)($subject111)))){
                                               ISet $elem113 = (ISet) $elem113_for;
                                               ISet s2_5 = ((ISet)($elem113));
                                               final ISet $subject112 = ((ISet)(((ISet)($subject111)).subtract(((ISet)($elem113)))));
                                               if(((ISet)($subject112)).size() == 0){
                                                  return ((ISet)($aset_add_aset(((ISet)($RVF.set(((IConstructor)($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)(M_lang_rascal_grammar_definition_Characters.difference(((IList)($arg0_124)), ((IList)($arg0_116)))))})))))),((ISet)(((ISet)s1_3).subtract(((ISet)s2_5)))))));
                                               
                                               } else {
                                                  continue IF21_SET_CONS_char_class_MVARs2;/*set pat3*/
                                               }
                                           }
                                           continue IF21_SET_CONS_char_class$_DFLT_SET_ELM115;/*redirected IF21_SET_CONS_char_class to IF21_SET_CONS_char_class$_DFLT_SET_ELM115; set pat4*/
                                                       
                                        } else {
                                           continue IF21_SET_CONS_char_class$_DFLT_SET_ELM115;/*default set elem*/
                                        }
                                     } else {
                                        continue IF21_SET_CONS_char_class$_DFLT_SET_ELM115;/*default set elem*/
                                     }
                                 }
                                 continue IF21_SET_CONS_char_class_MVARs1;
                                             
                              } else {
                                 continue IF21_SET_CONS_char_class_MVARs1;/*set pat3*/
                              }
                          }
                          continue IF21_SET_CONS_char_class$_DFLT_SET_ELM123;/*redirected IF21_SET_CONS_char_class to IF21_SET_CONS_char_class$_DFLT_SET_ELM123; set pat4*/
                                      
                       } else {
                          continue IF21_SET_CONS_char_class$_DFLT_SET_ELM123;/*default set elem*/
                       }
                    } else {
                       continue IF21_SET_CONS_char_class$_DFLT_SET_ELM123;/*default set elem*/
                    }
                }
                
                            
            } while(false);
        return ((ISet)(((ISet)u1_0).subtract(((ISet)u2_1))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(6210,142,<172,0>,<176,1>) 
    public IConstructor lang_rascal_grammar_Lookahead_removeLabels$018dc4c5ab035b44(IConstructor G_0){ 
        
        
        try {
            IValue $visitResult = $TRAVERSE.traverse(DIRECTION.BottomUp, PROGRESS.Continuing, FIXEDPOINT.No, REBUILD.Yes, 
                 new DescendantDescriptorAlwaysTrue($RVF.bool(false)),
                 G_0,
                 (IVisitFunction) (IValue $VISIT22_subject, TraversalState $traversalState) -> {
                     VISIT22:switch(Fingerprint.getFingerprint($VISIT22_subject)){
                     
                         case 110389984:
                             if($isSubtypeOf($VISIT22_subject.getType(),M_ParseTree.ADT_Production)){
                                /*muExists*/CASE_110389984_0: 
                                    do {
                                        if($has_type_and_arity($VISIT22_subject, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                                           IValue $arg0_128 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT22_subject)),0));
                                           if($isComparable($arg0_128.getType(), M_ParseTree.ADT_Symbol)){
                                              IConstructor rhs_1 = ((IConstructor)($arg0_128));
                                              IValue $arg1_127 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT22_subject)),1));
                                              if($isComparable($arg1_127.getType(), $T0)){
                                                 IList lhs_2 = ((IList)($arg1_127));
                                                 IValue $arg2_126 = (IValue)($aadt_subscript_int(((IConstructor)($VISIT22_subject)),2));
                                                 if($isComparable($arg2_126.getType(), $T23)){
                                                    ISet a_3 = ((ISet)($arg2_126));
                                                    IConstructor $replacement125 = (IConstructor)($RVF.constructor(M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, new IValue[]{((IConstructor)($me.removeLabel(((IConstructor)($arg0_128))))), ((IList)($me.removeLabels(((IList)($arg1_127))))), ((ISet)($arg2_126))}));
                                                    if($isSubtypeOf($replacement125.getType(),$VISIT22_subject.getType())){
                                                       $traversalState.setMatchedAndChanged(true, true);
                                                       return $replacement125;
                                                    
                                                    } else {
                                                       break VISIT22;// switch
                                                    
                                                    }
                                                 }
                                              
                                              }
                                           
                                           }
                                        
                                        }
                                
                                    } while(false);
                             
                             }
            
                     
                     
                     }
                     return $VISIT22_subject;
                 });
            return (IConstructor)$visitResult;
        
        } catch (ReturnFromTraversalException e) {
            return (IConstructor) e.getValue();
        }
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(6354,78,<178,0>,<180,1>) 
    public IConstructor lang_rascal_grammar_Lookahead_removeLabel$dc7038cda9b1bd2d(IConstructor s_0){ 
        
        
        /*muExists*/$RET129: 
            do {
                if($has_type_and_arity(s_0, M_Type.Symbol_label_str_Symbol, 2)){
                   IValue $arg0_131 = (IValue)($aadt_subscript_int(((IConstructor)s_0),0));
                   if($isComparable($arg0_131.getType(), $T6)){
                      IValue $arg1_130 = (IValue)($aadt_subscript_int(((IConstructor)s_0),1));
                      if($isComparable($arg1_130.getType(), M_ParseTree.ADT_Symbol)){
                         IConstructor s2_1 = null;
                         return ((IConstructor)($arg1_130));
                      
                      }
                   
                   }
                
                }
        
            } while(false);
        return ((IConstructor)s_0);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(6434,96,<182,0>,<184,1>) 
    public IList lang_rascal_grammar_Lookahead_removeLabels$9c574341c6d9ec97(IList syms_0){ 
        
        
        final IListWriter $listwriter132 = (IListWriter)$RVF.listWriter();
        $LCOMP133_GEN6516:
        for(IValue $elem134_for : ((IList)syms_0)){
            IConstructor $elem134 = (IConstructor) $elem134_for;
            IConstructor s_1 = ((IConstructor)($elem134));
            $listwriter132.append($me.removeLabel(((IConstructor)s_1)));
        
        }
        
                    return ((IList)($listwriter132.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(6532,131,<186,0>,<188,1>) 
    public ISet lang_rascal_grammar_Lookahead_usedSymbols$2c1765cdce8f88b5(IConstructor G_0){ 
        
        
        final ISetWriter $setwriter135 = (ISetWriter)$RVF.setWriter();
        ;
        /*muExists*/$SCOMP136_GEN6593_CONS_prod: 
            do {
                $SCOMP136_GEN6593:
                for(IValue $elem139_for : ((ISet)(((ISet)($aadt_get_field(((IConstructor)G_0), "productions")))))){
                    IConstructor $elem139 = (IConstructor) $elem139_for;
                    if($has_type_and_arity($elem139, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                       IValue $arg0_142 = (IValue)($aadt_subscript_int(((IConstructor)($elem139)),0));
                       if($isComparable($arg0_142.getType(), $T6)){
                          IValue $arg1_141 = (IValue)($aadt_subscript_int(((IConstructor)($elem139)),1));
                          if($isComparable($arg1_141.getType(), $T6)){
                             IValue $arg2_140 = (IValue)($aadt_subscript_int(((IConstructor)($elem139)),2));
                             if($isComparable($arg2_140.getType(), $T6)){
                                IConstructor p_1 = ((IConstructor)($elem139));
                                $SCOMP136_GEN6593_CONS_prod_GEN6636:
                                for(IValue $elem137_for : ((IList)(((IList)($aadt_get_field(((IConstructor)p_1), "symbols")))))){
                                    IConstructor $elem137 = (IConstructor) $elem137_for;
                                    $SCOMP136_GEN6593_CONS_prod_GEN6636_DESC6636:
                                    for(IValue $elem138 : new DescendantMatchIterator($elem137, 
                                        new DescendantDescriptor(new io.usethesource.vallang.type.Type[]{$TF.listType(ADT_Symbol), M_Type.ADT_Exception, $TF.setType(ADT_Symbol), $TF.setType(ADT_Condition), ADT_KeywordArguments_1, M_ParseTree.ADT_Tree, M_ParseTree.ADT_TreeSearchResult_1, M_ParseTree.ADT_Condition, M_ParseTree.ADT_Production, M_ParseTree.ADT_Symbol, M_Grammar.ADT_Grammar, M_ParseTree.ADT_CharRange, M_Grammar.ADT_Item, M_Grammar.ADT_GrammarModule, $TF.listType(ADT_CharRange), M_Grammar.ADT_GrammarDefinition}, 
                                                                 new io.usethesource.vallang.IConstructor[]{}, 
                                                                 $RVF.bool(false)))){
                                        if($isComparable($elem138.getType(), M_ParseTree.ADT_Symbol)){
                                           if($isSubtypeOf($elem138.getType(),M_ParseTree.ADT_Symbol)){
                                              IConstructor s_2 = null;
                                              $setwriter135.insert($elem138);
                                           
                                           } else {
                                              continue $SCOMP136_GEN6593_CONS_prod_GEN6636_DESC6636;
                                           }
                                        } else {
                                           continue $SCOMP136_GEN6593_CONS_prod_GEN6636_DESC6636;
                                        }
                                    }
                                    continue $SCOMP136_GEN6593_CONS_prod_GEN6636;
                                                 
                                }
                                continue $SCOMP136_GEN6593;
                                            
                             } else {
                                continue $SCOMP136_GEN6593;
                             }
                          } else {
                             continue $SCOMP136_GEN6593;
                          }
                       } else {
                          continue $SCOMP136_GEN6593;
                       }
                    } else {
                       continue $SCOMP136_GEN6593;
                    }
                }
                
                            
            } while(false);
        return ((ISet)($setwriter135.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(6665,102,<190,0>,<192,1>) 
    public ISet lang_rascal_grammar_Lookahead_definedSymbols$d175465ff362118c(IConstructor G_0){ 
        
        
        final ISetWriter $setwriter143 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP144_GEN6734:
        for(IValue $elem145_for : ((ISet)(((ISet)($aadt_get_field(((IConstructor)G_0), "productions")))))){
            IConstructor $elem145 = (IConstructor) $elem145_for;
            IConstructor p_1 = null;
            $setwriter143.insert(((IConstructor)($aadt_get_field(((IConstructor)($elem145)), "def"))));
        
        }
        
                    return ((ISet)($setwriter143.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(6769,102,<194,0>,<196,1>) 
    public ISet lang_rascal_grammar_Lookahead_terminalSymbols$a998324b5ea47532(IConstructor G_0){ 
        
        
        final ISetWriter $setwriter146 = (ISetWriter)$RVF.setWriter();
        ;
        /*muExists*/$SCOMP147_GEN6833_CONS_char_class: 
            do {
                $SCOMP147_GEN6833:
                for(IValue $elem148_for : ((ISet)($me.usedSymbols(((IConstructor)G_0))))){
                    IConstructor $elem148 = (IConstructor) $elem148_for;
                    if($has_type_and_arity($elem148, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                       IValue $arg0_149 = (IValue)($aadt_subscript_int(((IConstructor)($elem148)),0));
                       if($isComparable($arg0_149.getType(), $T6)){
                          IConstructor S_1 = ((IConstructor)($elem148));
                          $setwriter146.insert(S_1);
                       
                       } else {
                          continue $SCOMP147_GEN6833;
                       }
                    } else {
                       continue $SCOMP147_GEN6833;
                    }
                }
                
                            
            } while(false);
        return ((ISet)($setwriter146.done()));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(6990,264,<202,0>,<214,1>) 
    public ISet lang_rascal_grammar_Lookahead_first$7ecc3715cf16dd3a(IList symbols_0, IMap FIRST_1){ 
        
        
        ISet result_2 = ((ISet)$constants.get(2)/*{}*/);
        /*muExists*/FOR23: 
            do {
                FOR23_GEN7091:
                for(IValue $elem150_for : ((IList)symbols_0)){
                    IConstructor $elem150 = (IConstructor) $elem150_for;
                    IConstructor S_3 = null;
                    ISet f_4 = ((ISet)($amap_subscript(((IMap)FIRST_1),((IConstructor)($elem150)))));
                    if((((IBool)($RVF.bool(!(((ISet)f_4)).contains(((IConstructor)$constants.get(1)/*empty()*/)))))).getValue()){
                       return ((ISet)($aset_add_aset(((ISet)(((ISet)result_2).delete(((IConstructor)$constants.get(1)/*empty()*/)))),((ISet)f_4))));
                    
                    } else {
                       result_2 = ((ISet)($aset_add_aset(((ISet)result_2),((ISet)f_4))));
                    
                    }
                }
                continue FOR23;
                            
            } while(false);
        /* void:  muCon([]) */return ((ISet)result_2);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(7283,452,<218,0>,<233,1>) 
    public IMap lang_rascal_grammar_Lookahead_first$8d738115fa89afe8(IConstructor G_0){ 
        
        
        ISet defSymbols_1 = ((ISet)($me.definedSymbols(((IConstructor)G_0))));
        final IMapWriter $mapwriter151 = (IMapWriter)$RVF.mapWriter();
        $MCOMP152_GEN7389:
        for(IValue $elem153_for : ((ISet)($me.terminalSymbols(((IConstructor)G_0))))){
            IConstructor $elem153 = (IConstructor) $elem153_for;
            IConstructor trm_3 = null;
            $mapwriter151.insert($RVF.tuple($elem153, $RVF.set(((IConstructor)($elem153)))));
        
        }
        
                    final IMapWriter $mapwriter154 = (IMapWriter)$RVF.mapWriter();
        $MCOMP155_GEN7459:
        for(IValue $elem156_for : ((ISet)defSymbols_1)){
            IConstructor $elem156 = (IConstructor) $elem156_for;
            IConstructor S_4 = null;
            $mapwriter154.insert($RVF.tuple($elem156, ((ISet)$constants.get(2)/*{}*/)));
        
        }
        
                    IMap FIRST_2 = ((IMap)($amap_add_amap(((IMap)($mapwriter151.done())),((IMap)($mapwriter154.done())))));
        final ISetWriter $setwriter157 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP158_GEN7521:
        for(IValue $elem163_for : ((ISet)defSymbols_1)){
            IConstructor $elem163 = (IConstructor) $elem163_for;
            IConstructor S_6 = ((IConstructor)($elem163));
            $SCOMP158_GEN7521_GEN7538:
            for(IValue $elem159_for : ((ISet)(((ISet)($aadt_get_field(((IConstructor)G_0), "productions")))))){
                IConstructor $elem159 = (IConstructor) $elem159_for;
                if($has_type_and_arity($elem159, M_ParseTree.Production_prod_Symbol_list_Symbol_set_Attr, 3)){
                   IValue $arg0_162 = (IValue)($aadt_subscript_int(((IConstructor)($elem159)),0));
                   if($isComparable($arg0_162.getType(), M_ParseTree.ADT_Symbol)){
                      if((S_6 != null)){
                         if(S_6.match($arg0_162)){
                            IValue $arg1_161 = (IValue)($aadt_subscript_int(((IConstructor)($elem159)),1));
                            if($isComparable($arg1_161.getType(), $T0)){
                               IList lhs_7 = null;
                               IValue $arg2_160 = (IValue)($aadt_subscript_int(((IConstructor)($elem159)),2));
                               if($isComparable($arg2_160.getType(), $T6)){
                                  $setwriter157.insert($RVF.tuple(((IConstructor)($arg0_162)), ((IList)($arg1_161))));
                               
                               } else {
                                  continue $SCOMP158_GEN7521_GEN7538;
                               }
                            } else {
                               continue $SCOMP158_GEN7521_GEN7538;
                            }
                         } else {
                            continue $SCOMP158_GEN7521_GEN7538;
                         }
                      } else {
                         S_6 = ((IConstructor)($arg0_162));
                         IValue $arg1_161 = (IValue)($aadt_subscript_int(((IConstructor)($elem159)),1));
                         if($isComparable($arg1_161.getType(), $T0)){
                            IList lhs_7 = null;
                            IValue $arg2_160 = (IValue)($aadt_subscript_int(((IConstructor)($elem159)),2));
                            if($isComparable($arg2_160.getType(), $T6)){
                               $setwriter157.insert($RVF.tuple(((IConstructor)($arg0_162)), ((IList)($arg1_161))));
                            
                            } else {
                               continue $SCOMP158_GEN7521_GEN7538;
                            }
                         } else {
                            continue $SCOMP158_GEN7521_GEN7538;
                         }
                      }
                   } else {
                      continue $SCOMP158_GEN7521_GEN7538;
                   }
                } else {
                   continue $SCOMP158_GEN7521_GEN7538;
                }
            }
            continue $SCOMP158_GEN7521;
                        
        }
        
                    ISet def2lhs_5 = ((ISet)($setwriter157.done()));
        int $iterations164 = 1000000;
        if($iterations164 <= 0){
         throw RuntimeExceptionFactory.indexOutOfBounds($RVF.integer($iterations164));
        }
        boolean $change165 = true;
        while25:
            while($change165 && $iterations164 >= 0){
                $change165 = false;
                IMap $FIRST166 = (IMap)(FIRST_2);
                /*muExists*/FOR26: 
                    do {
                        FOR26_GEN7603:
                        for(IValue $elem168_for : ((ISet)def2lhs_5)){
                            IValue $elem168 = (IValue) $elem168_for;
                            final IValue $tuple_subject169 = ((IValue)($elem168));
                            if($tuple_subject169 instanceof ITuple && ((ITuple)$tuple_subject169).arity() == 2){
                               /*muExists*/FOR26_GEN7603_TUPLE: 
                                   do {
                                       IConstructor S_8 = ((IConstructor)($subscript_int(((IValue)($tuple_subject169)),0)));
                                       IList lhs_9 = ((IList)($subscript_int(((IValue)($tuple_subject169)),1)));
                                       FIRST_2 = ((IMap)($amap_update(S_8,$aset_add_aset(((ISet)($amap_subscript(((IMap)FIRST_2),((IConstructor)S_8)))),((ISet)(((((IBool)(M_List.isEmpty(((IList)lhs_9))))).getValue() ? ((ISet)$constants.get(8)/*{empty()}*/) : $me.first(((IList)lhs_9), ((IMap)FIRST_2)))))), ((IMap)(((IMap)FIRST_2))))));
                               
                                   } while(false);
                            
                            } else {
                               continue FOR26_GEN7603;
                            }
                        }
                        continue FOR26;
                                    
                    } while(false);
                /* void:  muCon([]) */if((((IBool)($equal(((IMap)($FIRST166)),((IMap)FIRST_2)).not()))).getValue()){
                   $change165 = true;
                
                }
                $iterations164 += -1;
        
            }
        return ((IMap)FIRST_2);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(7738,618,<236,0>,<255,1>) 
    public IMap lang_rascal_grammar_Lookahead_follow$c4198b787ccc5199(IConstructor G_0, IMap FIRST_1){ 
        
        
        ISet defSymbols_2 = ((ISet)($me.definedSymbols(((IConstructor)G_0))));
        final ISetWriter $setwriter170 = (ISetWriter)$RVF.setWriter();
        ;
        $SCOMP171_GEN7872:
        for(IValue $elem172_for : ((ISet)(((ISet)($aadt_get_field(((IConstructor)G_0), "starts")))))){
            IConstructor $elem172 = (IConstructor) $elem172_for;
            IConstructor S_4 = null;
            $setwriter170.insert($RVF.tuple(((IConstructor)($elem172)), ((IConstructor)($RVF.constructor(Symbol_eoi_, new IValue[]{})))));
        
        }
        
                    ISet F_3 = ((ISet)($setwriter170.done()));
        /*muExists*/FOR27: 
            do {
                FOR27_GEN7907:
                for(IValue $elem181_for : ((ISet)(((ISet)($aadt_get_field(((IConstructor)G_0), "productions")))))){
                    IConstructor $elem181 = (IConstructor) $elem181_for;
                    if(true){
                       IConstructor p_5 = ((IConstructor)($elem181));
                       final IList $subject_val177 = ((IList)(((IList)($aadt_get_field(((IConstructor)p_5), "symbols")))));
                       final IList $subject178 = ((IList)($subject_val177));
                       int $subject178_cursor = 0;
                       if($isSubtypeOf($subject178.getType(),$T0)){
                          final int $subject178_len = (int)((IList)($subject178)).length();
                          if($subject178_len >= 1){
                             final int $__1180_start = (int)$subject178_cursor;
                             FOR27_GEN7907_LIST_MVAR$_173:
                             
                             for(int $__1180_len = 0; $__1180_len <= $subject178_len - $__1180_start - 1; $__1180_len += 1){
                                $subject178_cursor = $__1180_start + $__1180_len;
                                if($subject178_cursor < $subject178_len){
                                   IConstructor current_6 = ((IConstructor)($alist_subscript_int(((IList)($subject178)),$subject178_cursor)));
                                   $subject178_cursor += 1;
                                   final int $symbols_7179_start = (int)$subject178_cursor;
                                   FOR27_GEN7907_LIST_MVAR$_173_VARcurrent_MVARsymbols:
                                   
                                   for(int $symbols_7179_len = 0; $symbols_7179_len <= $subject178_len - $symbols_7179_start - 0; $symbols_7179_len += 1){
                                      IList symbols_7 = ((IList)($subject178.sublist($symbols_7179_start, $symbols_7179_len)));
                                      $subject178_cursor = $symbols_7179_start + $symbols_7179_len;
                                      if($subject178_cursor == $subject178_len){
                                         if((((IBool)($RVF.bool(((ISet)defSymbols_2).contains(((IConstructor)current_6)))))).getValue()){
                                            ISet flw_8 = ((ISet)($me.first(((IList)symbols_7), ((IMap)FIRST_1))));
                                            if((((IBool)($RVF.bool(((ISet)flw_8).contains(((IConstructor)$constants.get(1)/*empty()*/)))))).getValue()){
                                               flw_8 = ((ISet)(((ISet)flw_8).subtract(((ISet)$constants.get(8)/*{empty()}*/))));
                                               flw_8 = ((ISet)($aset_add_aset(((ISet)flw_8),((ISet)($RVF.set(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_5), "def")))))))))));
                                            
                                            } else {
                                               if((((IBool)(M_List.isEmpty(((IList)symbols_7))))).getValue()){
                                                  flw_8 = ((ISet)(((ISet)flw_8).subtract(((ISet)$constants.get(8)/*{empty()}*/))));
                                                  flw_8 = ((ISet)($aset_add_aset(((ISet)flw_8),((ISet)($RVF.set(((IConstructor)(((IConstructor)($aadt_get_field(((IConstructor)p_5), "def")))))))))));
                                               
                                               }
                                            
                                            }final ISetWriter $setwriter174 = (ISetWriter)$RVF.setWriter();
                                            ;
                                            $SCOMP175_GEN8210:
                                            for(IValue $elem176_for : ((ISet)flw_8)){
                                                IConstructor $elem176 = (IConstructor) $elem176_for;
                                                IConstructor s_9 = null;
                                                $setwriter174.insert($RVF.tuple(((IConstructor)current_6), ((IConstructor)($elem176))));
                                            
                                            }
                                            
                                                        F_3 = ((ISet)($aset_add_aset(((ISet)F_3),((ISet)($setwriter174.done())))));
                                         
                                         }
                                      
                                      } else {
                                         continue FOR27_GEN7907_LIST_MVAR$_173_VARcurrent_MVARsymbols;/*list match1*/
                                      }
                                   }
                                   continue FOR27_GEN7907_LIST_MVAR$_173;/*computeFail*/
                                
                                } else {
                                   continue FOR27_GEN7907_LIST_MVAR$_173;/*computeFail*/
                                }
                             }
                             continue FOR27_GEN7907;
                          
                          } else {
                             continue FOR27_GEN7907;
                          }
                       } else {
                          continue FOR27_GEN7907;
                       }
                    } else {
                       continue FOR27_GEN7907;
                    }
                }
                continue FOR27;
                            
            } while(false);
        /* void:  muCon([]) */F_3 = ((ISet)(((ISet)F_3).asRelation().closureStar()));
        final IMapWriter $mapwriter182 = (IMapWriter)$RVF.mapWriter();
        $MCOMP183_GEN8296:
        for(IValue $elem184_for : ((ISet)($aset_add_aset(((ISet)defSymbols_2),((ISet)(((ISet)($aadt_get_field(((IConstructor)G_0), "starts"))))))))){
            IConstructor $elem184 = (IConstructor) $elem184_for;
            IConstructor defSym_11 = null;
            $mapwriter182.insert($RVF.tuple($elem184, ((ISet)($arel_subscript1_noset(((ISet)F_3),((IConstructor)($elem184))))).subtract(((ISet)defSymbols_2))));
        
        }
        
                    IMap FOLLOW_10 = ((IMap)($mapwriter182.done()));
        return ((IMap)FOLLOW_10);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(8358,134,<257,0>,<260,1>) 
    public ITuple lang_rascal_grammar_Lookahead_firstAndFollow$9a18a48b90b1e31c(IConstructor G_0){ 
        
        
        IMap fst_1 = ((IMap)($me.first(((IConstructor)G_0))));
        return ((ITuple)($RVF.tuple(((IMap)($me.mergeCC(((IMap)fst_1)))), ((IMap)($me.mergeCC(((IMap)($me.follow(((IConstructor)G_0), ((IMap)fst_1))))))))));
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(8496,108,<262,0>,<266,1>) 
    public IMap lang_rascal_grammar_Lookahead_mergeCC$40d82b0eeb5fa303(IMap su_0){ 
        
        
        /*muExists*/FOR30: 
            do {
                FOR30_GEN8545:
                for(IValue $elem185_for : ((IMap)su_0)){
                    IConstructor $elem185 = (IConstructor) $elem185_for;
                    IConstructor s_1 = ((IConstructor)($elem185));
                    su_0 = ((IMap)($amap_update(s_1,$me.mergeCC(((ISet)($amap_subscript(((IMap)su_0),((IConstructor)s_1))))), ((IMap)(((IMap)su_0))))));
                
                }
                continue FOR30;
                            
            } while(false);
        /* void:  muCon([]) */return ((IMap)su_0);
    
    }
    
    // Source: |file:///Users/paulklint/git/rascal/src/org/rascalmpl/library/lang/rascal/grammar/Lookahead.rsc|(8606,295,<268,0>,<284,1>) 
    public ISet lang_rascal_grammar_Lookahead_mergeCC$1d434fe22b76cf3a(ISet su_0){ 
        
        
        ISet result_1 = ((ISet)$constants.get(2)/*{}*/);
        if((((IBool)($RVF.bool(((ISet)su_0).contains(((IConstructor)$constants.get(1)/*empty()*/)))))).getValue()){
           result_1 = ((ISet)($aset_add_elm(((ISet)result_1),((IConstructor)$constants.get(1)/*empty()*/))));
        
        }
        if((((IBool)($RVF.bool(((ISet)su_0).contains(((IConstructor)($RVF.constructor(Symbol_eoi_, new IValue[]{})))))))).getValue()){
           result_1 = ((ISet)($aset_add_elm(((ISet)result_1),((IConstructor)($RVF.constructor(Symbol_eoi_, new IValue[]{}))))));
        
        }
        IList rs_2 = ((IList)$constants.get(0)/*[]*/);
        /*muExists*/FOR33: 
            do {
                /*muExists*/FOR33_GEN8781_CONS_char_class: 
                    do {
                        FOR33_GEN8781:
                        for(IValue $elem186_for : ((ISet)su_0)){
                            IConstructor $elem186 = (IConstructor) $elem186_for;
                            if($has_type_and_arity($elem186, M_ParseTree.Symbol_char_class_list_CharRange, 1)){
                               IValue $arg0_187 = (IValue)($aadt_subscript_int(((IConstructor)($elem186)),0));
                               if($isComparable($arg0_187.getType(), $T4)){
                                  IList r_3 = ((IList)($arg0_187));
                                  rs_2 = ((IList)(M_lang_rascal_grammar_definition_Characters.union(((IList)rs_2), ((IList)($arg0_187)))));
                               
                               } else {
                                  continue FOR33_GEN8781;
                               }
                            } else {
                               continue FOR33_GEN8781;
                            }
                        }
                        continue FOR33;
                                    
                    } while(false);
        
            } while(false);
        /* void:  muCon([]) */if((((IBool)($equal(((IList)rs_2),((IList)$constants.get(0)/*[]*/)).not()))).getValue()){
           result_1 = ((ISet)($aset_add_elm(((ISet)result_1),((IConstructor)($RVF.constructor(M_ParseTree.Symbol_char_class_list_CharRange, new IValue[]{((IList)rs_2)}))))));
        
        }
        return ((ISet)result_1);
    
    }
    

    public static void main(String[] args) {
      throw new RuntimeException("No function `main` found in Rascal module `lang::rascal::grammar::Lookahead`");
    }
}